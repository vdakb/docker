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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AbstractServerHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractServerHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Set;

import javax.management.ObjectName;

import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractServerHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant JMX mbean tasks.
 ** Implementations of <code>AbstractServerHandler</code> inherit its attributes
 ** (see below) for connecting to the JMX MBean server.
 ** <p>
 ** Refer to the user documentation for more information and examples on how to
 ** use this handler.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AbstractServerHandler extends AbstractInvocationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The Domain Runtime MBean Server.
   ** <p>
   ** Provides access to MBeans for domain-wide services such as application
   ** deployment, JMS servers, and JDBC data sources. It also is a single point
   ** for accessing the hierarchies of all runtime MBeans and all active
   ** configuration MBeans for all servers in the domain.
   */
  protected static final String DOMAIN_SERVICE         = "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean";

  /**
   ** Attribute of the service above and contains the DomainConfigurationMBean
   ** for the current WebLogic Server domain.
   */
  protected static final String DOMAIN_CONFIGURATION   = "DomainConfiguration";

  /**
   ** Attribute of the <code>DomainConfiguration</code> service contains the
   ** server runtime Lifecycle services all configured servers in the domain.
   */
  protected static final String SECURITY_CONFIGURATION = "SecurityConfiguration";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AbstractServerHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  protected AbstractServerHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvocationHandler)
  /**
   ** Returns the JMX {@link ObjectName} to access the credential store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            credential store.
   */
  @Override
  protected ObjectName objectName() {
    try {
      return domainConfiguration();
    }
    catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainName
  /**
   ** Returns the name of the doamin this handler is connected to by asking the
   ** JMX server.
   **
   ** @return                    the name of the doamin this handler is
   **                            connected to.
   **
   ** @throws ServiceException   if the query fails in generell.
   */
  protected String domainName()
    throws ServiceException {

    String name = null;
    try {
      final ObjectName      pattern  = new ObjectName("com.bea:Type=Domain,*");
      final Set<ObjectName> services = this.connection.queryNames(pattern, null);
      // the domain is always the first one in the returned set of ObjectNames
      final ObjectName      domain   = services.iterator().next();
      name = domain.getKeyProperty("Name");
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(e);
      else
        fatal(e);
    }
    return name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationName
  /**
   ** Returns the name of the application by asking the JMX server this handler
   ** is connected to.
   **
   ** @param  application        the {@link ObjectName} of the application to
   **                            return the value of the name attribute for.
   **
   ** @return                    the name of the application by asking the JMX
   **                            server this handler is connected to.
   **
   ** @throws ServiceException   if the query fails in generell.
   */
  protected String applicationName(final ObjectName application)
    throws ServiceException {

    return stringAttribute(application, "ApplicationName");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationName
  /**
   ** Returns the version of the application by asking the JMX server this
   ** handler is connected to.
   **
   ** @param  application        the {@link ObjectName} of the application to
   **                            return the value of the version attribute for.
   **
   ** @return                    the version of the application by asking the
   **                            JMX server this handler is connected to.
   **
   ** @throws ServiceException   if the query fails in generell.
   */
  public String applicationVersion(final ObjectName application)
    throws ServiceException {

    return stringAttribute(application, "ApplicationVersion");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the name of the managed server the application is deployed on by
   ** asking the JMX server this handler is connected to.
   **
   ** @param  application        the {@link ObjectName} of the application.
   **
   ** @return                    the name of the menaged server the application
   **                            is deployed on by asking the JMX server this
   **                            handler is connected to.
   **
   ** @throws ServiceException   if the query fails in generell.
   */
  public String serverName(final ObjectName application)
    throws ServiceException {

    return serverLocation(application);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverLocation
  /**
   ** Returns the location of the managed server the application is deployed on
   ** by asking the JMX server this handler is connected to.
   **
   ** @param  application        the {@link ObjectName} of the application.
   **
   ** @return                    the location of the menaged server the
   **                            application is deployed on by asking the JMX
   **                            server this handler is connected to.
   **
   ** @throws ServiceException   if the query fails in generell.
   */
  public String serverLocation(final ObjectName application)
    throws ServiceException {

    String serverLoc = null;
    try {
      ObjectName parent = null;
      Object     value  = attribute(application, "Parent");
      if ((value instanceof ObjectName))
        parent = (ObjectName)value;
      else {
        parent = new ObjectName((String)value);
      }
      if (parent != null)
        serverLoc = parent.getKeyProperty("Name");
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(e);
      else
        fatal(e);
    }
    return serverLoc;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainConfiguration
  /**
   ** Returns the managed bean to maintain domain runtime configurations in a
   ** Oracle WebLogic Server Domain.
   **
   ** @return                    the managed bean to maintain security
   **                            configurations in a WebLogic Server Domain.
   **
   ** @throws ServiceException   if the request fails overall with detailed
   **                            information about the reason.
   */
  protected ObjectName domainConfiguration()
    throws ServiceException {

    ObjectName result = null;
    try {
      final ObjectName domainService = new ObjectName(DOMAIN_SERVICE);
      result = (ObjectName)this.connection.getAttribute(domainService, DOMAIN_CONFIGURATION);
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(e);
      else
        fatal(e);
    }
    return result;
  }
}