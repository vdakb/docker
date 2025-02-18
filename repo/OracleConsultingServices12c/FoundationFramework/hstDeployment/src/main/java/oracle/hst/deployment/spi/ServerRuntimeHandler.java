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

    File        :   SecurityConfigurationHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SecurityConfigurationHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import javax.management.ObjectName;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ServerControl
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage the state of servers associated with
 ** a Oracle WebLogic Server Domain.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerRuntimeHandler extends AbstractServerHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute of the service above and contains the DomainRuntimeMBean for the
   ** current WebLogic Server domain.
   */
  private static final String DOMAIN_RUNTIME    = "DomainRuntime";

  /**
   ** Attribute of the <code>DomainRuntime</code> service contains the array of
   ** server runtime Lifecycle services all configured servers in the domain.
   */
  private static final String LIFECYCLE_RUNTIME = "ServerLifeCycleRuntimes";

  private static final String ATTRIBUTE_NAME    = "Name";
  private static final String ATTRIBUTE_STATE   = "State";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String              single          = null;

  private List<String>        multiple        = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServerRuntimeHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ServerRuntimeHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvokerTask)
  /**
   ** Returns the <code>JMX ObjectName</code> name this task will execute.
   **
   ** @return                    the <code>JMX ObjectName</code> name this task
   **                            will execute.
   **
   ** @throws BuildException   if the <code>JMX</code> {@link ObjectName}
   **                            cannot be resolved.
   */
  @Override
  protected final ObjectName objectName() {
    try {
      final ObjectName domainService = new ObjectName(DOMAIN_SERVICE);
      return (ObjectName)this.connection.getAttribute(domainService, DOMAIN_RUNTIME);
    }
    catch (MalformedObjectNameException e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.CONTROL_OBJECTNAME_MALFORMED, e.getLocalizedMessage()));
    }
    catch (AttributeNotFoundException e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.CONTROL_ATTRIBUTENAME_NOTFOUND, DOMAIN_RUNTIME));
    }
    catch (InstanceNotFoundException e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.CONTROL_BEANINSTANCE_NOTFOUND, e.getLocalizedMessage()));
    }
    catch (Exception e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.CONTROL_QUERY_FAILED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the status information of the servers associated with an Oracle
   ** WebLogic Server Domain through the given {@link MBeanServerConnection}.
   **
   ** @param  connection         the {@link MBeanServerConnection} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void status(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new ServiceException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "connection"));

    this.connection = connection;

    ObjectName[] domain = serverLifeCycle();
    ObjectName[] server = null;
    if (StringUtility.isEmpty(this.single) && this.multiple.isEmpty())
      server = serverLifeCycle();
    else {
      server = new ObjectName[this.multiple.size() + (StringUtility.isEmpty(this.single) ? 0 : 1)];
      int i = 0;
      if (!StringUtility.isEmpty(this.single))
        server[i++] = findObjectName(domain, this.single);
      for (String name : this.multiple)
        server[i++] = findObjectName(domain, name);
    }

    try {
      for (int i = 0; i < server.length; i++) {
        final String[] parameter = {
          (String)this.connection.getAttribute(server[i], ATTRIBUTE_NAME)
        , (String)this.connection.getAttribute(server[i], ATTRIBUTE_STATE)
        };
        info(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTROL_STATUS, parameter));
      }
    }
    catch (AttributeNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_ATTRIBUTENAME_NOTFOUND, ATTRIBUTE_STATE);
    }
    catch (InstanceNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_BEANINSTANCE_NOTFOUND, e.getLocalizedMessage());
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.CONTROL_QUERY_FAILED, e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findObjectName
  /**
   ** Lookups an {@link ObjectName} from the JMX Context that fullfils the
   ** specified <code>match</code>.
   **
   ** @param  names              the array of lifecycle object
   ** @param  match              the desired lifecaycle name discovered from
   **                            <code>lifeCycleONames</code>.
   **
   ** @return                    the {@link ObjectName} representing the
   **                            remote interface.
   **
   ** @throws ServiceException   if the request fails overall with detailed
   **                            information about the reason.
   */
  private ObjectName findObjectName(final ObjectName[] names, final String match)
    throws ServiceException {

    ObjectName result = null;
    try {
      for (ObjectName i : names) {
        final String name = (String)this.connection.getAttribute(i, ATTRIBUTE_NAME);
        if (name != null && name.equals(match)) {
          result = i;
          break;
        }
      }
    }
    catch (AttributeNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_ATTRIBUTENAME_NOTFOUND, ATTRIBUTE_NAME);
    }
    catch (InstanceNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_BEANINSTANCE_NOTFOUND, e.getLocalizedMessage());
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.CONTROL_QUERY_FAILED, e.getLocalizedMessage());
    }

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverLifeCycle
  /**
   ** Returns the life cycle beans of all servers configured in a WebLogic
   ** Domain.
   **
   ** @return                    the life cycle beans of all servers configured
   **                            in a WebLogic Domain.
   **
   ** @throws ServiceException   if the request fails overall with detailed
   **                            information about the reason.
   */
  protected ObjectName[] serverLifeCycle()
    throws ServiceException {

    try {
      return (ObjectName[])this.connection.getAttribute(objectName(), LIFECYCLE_RUNTIME);
    }
    catch (AttributeNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_ATTRIBUTENAME_NOTFOUND, LIFECYCLE_RUNTIME);
    }
    catch (InstanceNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_BEANINSTANCE_NOTFOUND, e.getLocalizedMessage());
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.CONTROL_QUERY_FAILED, e.getLocalizedMessage());
    }
  }
}