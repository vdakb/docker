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

    File        :   ServerStatus.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerStatus.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wls.task;

import javax.management.ObjectName;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;
import oracle.hst.deployment.task.AbstractServerTask;

import oracle.hst.deployment.spi.AbstractInvocationHandler;

////////////////////////////////////////////////////////////////////////////////
// class ServerStatus
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to request status informations about the
 ** configured server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerStatus extends AbstractServerTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String ATTRIBUTE_NAME  = "Name";
  protected static final String ATTRIBUTE_STATE = "State";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String               serverName       = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerStatus</code> Ant taskthat allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServerStatus() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setServerName
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>serverName</code>.
   **
   ** @param  serverName         the name of the server to manage
   */
  public void setServerName(final String serverName) {
    this.serverName = serverName;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setServerName
  /**
   ** Retruns the argument of parameter <code>serverName</code>.
   **
   ** @return                    the name of the server to manage
   */
  protected final String serverName() {
    return this.serverName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractTask)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  protected void onExecution()
    throws ServiceException {

    ObjectName[] server = null;
    if (StringUtility.isEmpty(this.serverName))
      server = serverLifeCycle();
    else {
      server = new ObjectName[1];
      server[0] = findObjectName(serverLifeCycle(), this.serverName);
    }

    try {
      for (int i = 0; i < server.length; i++) {
        final String[] parameter = {
          (String)this.connection().getAttribute(server[i], ATTRIBUTE_NAME)
        , (String)this.connection().getAttribute(server[i], ATTRIBUTE_STATE)
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
  protected ObjectName findObjectName(final ObjectName[] names, final String match)
    throws ServiceException {

    ObjectName result = null;
    try {
      for (ObjectName i : names) {
        final String name = (String)this.connection().getAttribute(i, ATTRIBUTE_NAME);
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
  protected final ObjectName[] serverLifeCycle()
    throws ServiceException {

    try {
      return (ObjectName[])this.connection().getAttribute(AbstractInvocationHandler.domainRuntimeService(), AbstractInvocationHandler.LIFECYCLE_RUNTIME);
    }
    catch (AttributeNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_ATTRIBUTENAME_NOTFOUND, AbstractInvocationHandler.LIFECYCLE_RUNTIME);
    }
    catch (InstanceNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_BEANINSTANCE_NOTFOUND, e.getLocalizedMessage());
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.CONTROL_QUERY_FAILED, e.getLocalizedMessage());
    }
  }
}