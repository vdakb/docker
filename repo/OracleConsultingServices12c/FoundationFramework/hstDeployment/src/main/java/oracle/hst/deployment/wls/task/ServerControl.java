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

    File        :   ServerControl.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerControl.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wls.task;

import javax.management.ObjectName;
import javax.management.MBeanException;

import org.apache.tools.ant.BuildException;

import weblogic.server.ServerLifecycleException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.ServerControlOperation;

import oracle.hst.deployment.spi.AbstractInvocationHandler;

////////////////////////////////////////////////////////////////////////////////
// class ServerControl
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to start and stop Oracle WebLogic Managed
 ** Server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerControl extends ServerStatus {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static Object[]        PARAMETER    = null;
  private static String[]        SIGNATURE    = null;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static ObjectName      serverObject;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ServerControlOperation operation    = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerControl</code> Ant taskthat allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServerControl() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setOperation
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>operation</code>.
   **
   ** @param  operation          the operation how to manage the server.
   */
  public void setOperation(final ServerControlOperation operation) {
    this.operation = operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation (AbstractInvokerTask)
  /**
   ** Returns the <code>operation</code> name this task will execute.
   **
   ** @return                    the <code>operation</code> name this task will
   **                            execute.
   */
  protected final String operation() {
    return this.operation.type();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter (AbstractInvokerTask)
  /**
   ** Returns operation's parameter string.and signature arrays.
   **
   ** @return                    the operation's parameter string.
   */
  protected final Object[] parameter() {
    return PARAMETER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signature (AbstractInvokerTask)
  /**
   ** Returns operation's signature arrays.
   **
   ** @return                    the operation's signature arrays.
   */
  protected final String[] signature() {
    return SIGNATURE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvokerTask)
  /**
   ** Returns the <code>JMX ObjectName</code> name this task will execute.
   **
   ** @return                    the <code>JMX ObjectName</code> name this task
   **                            will execute.
   **
   ** @throws ServiceException   if the <code>JMX</code> {@link ObjectName}
   **                            cannot be resolved.
   */
  protected final ObjectName objectName()
    throws ServiceException {

    if (ServerControl.serverObject == null) {
      synchronized(AbstractInvocationHandler.DOMAIN_SERVICE) {
        ServerControl.serverObject = findObjectName(serverLifeCycle(), serverName());
        if (ServerControl.serverObject == null)
          throw new ServiceException(ServiceError.CONTROL_SERVERNAME_NOTFOUND, serverName());
      }
    }
    return ServerControl.serverObject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractTask)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  protected void validate()
    throws BuildException {

    if (this.operation == null)
      handleAttributeMissing("operation");

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (overridden)
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
  public void onExecution()
    throws ServiceException {

    final String[] argument =  { serverName(), operation() };
    info(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTROL, argument));
    try {
      this.connection.invoke(objectName(), operation(), parameter(), signature());
      info(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTROL_COMPLETE, argument));
    }
    catch (MBeanException e) {
      error(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTROL_FAILED, argument));
      final Exception target = e.getTargetException();
      if ( target instanceof ServerLifecycleException)
        error(e.getLocalizedMessage());
      else
        throw new ServiceException(target);
    }
    catch (Exception e) {
      error(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_ERROR, operation));
      throw new ServiceException(e);
    }
  }
}