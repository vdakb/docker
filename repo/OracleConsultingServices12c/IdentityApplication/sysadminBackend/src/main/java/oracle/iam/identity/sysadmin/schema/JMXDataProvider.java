/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Administration Management

    File        :   JMXDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JMXDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.InstanceNotFoundException;
import javax.management.AttributeNotFoundException;
import javax.management.MalformedObjectNameException;

import oracle.hst.foundation.naming.ServiceLocator;
import oracle.hst.foundation.naming.LocatorException;

import oracle.iam.ui.platform.model.common.ModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// abstract class JMXDataProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by any customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** This Data Provider Object extends the base {@link AbstractDataProvider} by
 ** JMX functionality.
 ** <p>
 ** Unfortunately a {@link MBeanServer} isn't serializable hence this
 ** {@link AbstractDataProvider} has no failover capabilities.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public abstract class JMXDataProvider<T extends ModelAdapterBean> extends AbstractDataProvider<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Provides a common access point for navigating to all runtime and
   ** configuration MBeans in the domain as well as to MBeans that provide
   ** domain-wide services (such as controlling and monitoring the life cycles
   ** of servers and message-driven EJBs and coordinating the migration of
   ** migratable services).
   ** <p>
   ** This MBean is available only on the Administration Server.
   */
  public static final String    DOMAIN_SERVER     = "/jndi/weblogic.management.mbeanservers.domainruntime";

  /**
   ** To monitor only active configuration MBeans (and not runtime MBeans), use
   ** a Runtime MBean Server.
   ** <p>
   ** Monitoring through a Runtime MBean Server requires less memory and network
   ** traffic than monitoring through the Domain Runtime MBean Server.
   ** (WebLogic Server does not initialize the Domain Runtime MBean Server until
   ** a client requests a connection to it.)
   */
  public static final String    RUNTIME_SERVER    = "/jndi/weblogic.management.mbeanservers.runtime";

  /**
   ** The Domain Runtime MBean Server.
   ** <p>
   ** Provides access to MBeans for domain-wide services such as application
   ** deployment, JMS servers, and JDBC data sources. It also is a single point
   ** for accessing the hierarchies of all runtime MBeans and all active
   ** configuration MBeans for all servers in the domain.
   ** <p>
   ** This MBean is available only on the Administration Server.
   */
  protected static String       DOMAIN_SERVICE    = "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean";

  /**
   ** Attribute of the service above and contains the DomainRuntimeMBean for the
   ** current WebLogic Server domain.
   */
  protected static String       DOMAIN_RUNTIME    = "DomainRuntime";

  /**
   ** Attribute of the <code>DomainRuntime</code> service contains the array of
   ** server runtime Lifecycle services all configured servers in the domain.
   */
  protected static String       LIFECYCLE_RUNTIME = "ServerLifeCycleRuntimes";

  /**
   ** The Server Runtime MBean Server.
   ** <p>
   ** Provides access to MBeans for server-wide services such as application
   ** deployment, JMS servers, and JDBC data sources. It also is a single point
   ** for accessing the hierarchies of all runtime MBeans and all active
   ** configuration MBeans for all servers in the domain.
   ** <p>
   ** This MBean is available only on the Administration Server.
   */
  protected static String       RUNTIME_SERVICE   = "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean";

  /**
   ** Attribute of the service above and contains the ServerNameMBean
   ** for the current WebLogic Server.
   */
  protected static String       SERVER_NAME       = "ServerName";

  /**
   ** Attribute of the service above and contains the ServerConfigurationMBean
   ** for the current WebLogic Server.
   */
  protected static String       SERVER_CONFIG     = "ServerConfiguration";

  /**
   ** Attribute of the service above and contains the ServerRuntimeMBean
   ** for the current WebLogic Server.
   */
  protected static String       SERVER_RUNTIME    = "ServerRuntime";

  @SuppressWarnings("compatibility:6407641972484339765")
  private static final long     serialVersionUID  = -8844012240221647458L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient MBeanServer runtimeServer     = null;
  private transient ObjectName  runtimeService    = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JMXDataProvider</code> data access object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JMXDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the name of the current server.
   **
   ** @return                    the name of the current server.
   **
   ** @throws MalformedObjectNameException the string used for the object name
   **                                      to build does not have the right
   **                                      format.
   ** @throws LocatorException             if JMX Runtime Server could not be
   **                                      found.
   ** @throws MBeanException               an exception thrown by the MBean's
   **                                      getter.
   ** @throws InstanceNotFoundException    the MBean expected is not registered
   **                                      in the MBean server.
   ** @throws ReflectionException          an exception thrown when trying to
   **                                      invoke the setter.
   ** @throws AttributeNotFoundException   the attribute specified is not
   **                                      accessible in the MBean.
   **
   */
  public final String serverName()
    throws MalformedObjectNameException
    ,      LocatorException
    ,      MBeanException
    ,      ReflectionException
    ,      InstanceNotFoundException
    ,      AttributeNotFoundException {

    return (String)attribute(runtimeService(), SERVER_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverConfiguration
  /**
   ** Returns the {@link ObjectName} representing the server configuration.
   **
   ** @return                    the {@link ObjectName} representing the server
   **                            configuration.
   **
   ** @throws MalformedObjectNameException the string used for the object name
   **                                      to build does not have the right
   **                                      format.
   ** @throws LocatorException             if JMX Runtime Server could not be
   **                                      found.
   ** @throws MBeanException               an exception thrown by the MBean's
   **                                      getter.
   ** @throws InstanceNotFoundException    the MBean expected is not registered
   **                                      in the MBean server.
   ** @throws ReflectionException          an exception thrown when trying to
   **                                      invoke the setter.
   ** @throws AttributeNotFoundException   the attribute specified is not
   **                                      accessible in the MBean.
   **
   */
  public final ObjectName serverConfiguration()
    throws MalformedObjectNameException
    ,      LocatorException
    ,      MBeanException
    ,      ReflectionException
    ,      InstanceNotFoundException
    ,      AttributeNotFoundException {

    return (ObjectName)attribute(runtimeService(), SERVER_CONFIG);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the an attribute value from the specified object.
   **
   ** @param  object             the {@link ObjectName} of the MBean from which
   **                            the attribute value is to be retrieved.
   ** @param  attributeName      a String specifying the name of the attribute
   **                            to be retrieved.
   **
   ** @return                    the an attribute value from the specified
   **                            object mapped for the specfied
   **                            <code>attributeName</code>.
   **
   ** @throws MalformedObjectNameException the string used for the object name
   **                                      to build does not have the right
   **                                      format.
   ** @throws LocatorException             if JMX Runtime Server could not be
   **                                      found.
   ** @throws MBeanException               an exception thrown by the MBean's
   **                                      getter.
   ** @throws InstanceNotFoundException    the MBean expected is not registered
   **                                      in the MBean server.
   ** @throws ReflectionException          an exception thrown when trying to
   **                                      invoke the setter.
   ** @throws AttributeNotFoundException   the attribute specified is not
   **                                      accessible in the MBean.
   **
   */
  public final Object attribute(final ObjectName object, final String attributeName)
    throws MalformedObjectNameException
    ,      LocatorException
    ,      MBeanException
    ,      ReflectionException
    ,      InstanceNotFoundException
    ,      AttributeNotFoundException {

    return runtimeServer().getAttribute(object, attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes an operation on an MBean.
   ** <p>
   ** Because of the need for a signature to differentiate possibly-overloaded
   ** operations, it is much simpler to invoke operations through an MBean proxy
   ** where possible.
   **
   ** @param  instance           the {@link ObjectName} of the MBean on which
   **                            the method is to be invoked.
   ** @param  operation          the name of the operation to invoke.
   ** @param  parameter          an array containing the parameters to be set
   **                            when the operation is invoked.
   ** @param  signature          an array containing the signature of the
   **                            operation. The class objects will be loaded
   **                            using the same class loader as the one used for
   **                            loading the MBean on which the operation was
   **                            invoked.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws LocatorException          if JMX Runtime Server could not be
   **                                   found.
   ** @throws MBeanException            an exception thrown by the MBean's
   **                                   invoked method.
   ** @throws ReflectionException       an exception thrown while trying to
   **                                   invoke the method
   **                                   <code>operation</code>.
   ** @throws InstanceNotFoundException the MBean expected is not registered in
   **                                   the MBean server.
   */
  protected Object invoke(final ObjectName instance, final String operation, final Object[] parameter, final String[] signature)
    throws LocatorException
    ,      MBeanException
    ,      ReflectionException
    ,      InstanceNotFoundException {

    Object result = runtimeServer().invoke(instance, operation, parameter, signature);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runtimeService
  /**
   ** Returns the {@link ObjectName}.
   **
   ** @return                    the {@link ObjectName}
   **
   ** @throws MalformedObjectNameException the string used for the object name
   **                                      to build does not have the right
   **                                      format.
   */
  public final ObjectName runtimeService()
    throws MalformedObjectNameException {

    if (this.runtimeService == null) {
      synchronized(RUNTIME_SERVER) {
        this.runtimeService = new ObjectName(RUNTIME_SERVICE);
      }
    }
    return this.runtimeService;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName
  /**
   ** Returns the {@link ObjectName} for the specified string representation
   ** <code>name</code>.
   **
   ** @param  name               a string representation of the object name to
   **                            build.
   **
   ** @return                    the {@link ObjectName}
   **
   ** @throws MalformedObjectNameException the string used for the object name
   **                                      to build does not have the right
   **                                      format.
   */
  public final ObjectName objectName(final String name)
    throws MalformedObjectNameException {

    return new ObjectName(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runtimeServer
  /**
   ** Returns the {@link MBeanServer}.
   **
   ** @return                    the {@link MBeanServer}
   **
   ** @throws LocatorException   if JMX Runtime Server could not be found.
   */
  public final MBeanServer runtimeServer()
    throws LocatorException {

     if (this.runtimeServer == null) {
      synchronized(RUNTIME_SERVER) {
        this.runtimeServer = ServiceLocator.getRuntimeServer();
      }
    }
   return this.runtimeServer;
  }
}