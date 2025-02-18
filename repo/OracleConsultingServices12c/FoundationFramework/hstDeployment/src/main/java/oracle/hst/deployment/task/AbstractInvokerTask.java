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

    File        :   AbstractInvokerTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractInvokerTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.task;

import javax.management.ObjectName;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.InstanceNotFoundException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.JMXServerContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractInvokerTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant JMX mbean tasks that invokes
 ** operations on the JMX MBean server.
 ** <p>
 ** Implementations of <code>Service</code> inherit its attributes (see below)
 ** for connecting to the JMX MBean server.
 ** <p>
 ** Refer to the user documentation for more information and examples on how to
 ** use this task.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractInvokerTask extends AbstractServiceTask {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractInvokerTask</code> Ant task that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractInvokerTask() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractInvokerTask</code> that use the specified
   ** {@link JMXServerContext} <code>context</code> as the runtime environment.
   **
   ** @param  server             the {@link JMXServerContext} used as the
   **                            runtime environment.
   */
  protected AbstractInvokerTask(final JMXServerContext server) {
    // ensure inheritance
    super(server);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName
  /**
   ** Returns the <code>JMX</code> {@link ObjectName} name this task will
   ** execute.
   **
   ** @return                    the <code>JMX</code> {@link ObjectName} name
   **                            this task will execute.
   **
   ** @throws ServiceException   if the <code>JMX</code> {@link ObjectName}
   **                            cannot be resolved.
   */
  protected abstract ObjectName objectName()
    throws ServiceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Returns the <code>operation</code> name this task will execute.
   **
   ** @return                    the <code>operation</code> name this task will
   **                            execute.
   */
  protected abstract String operation();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Returns operation's parameter string.and signature arrays.
   **
   ** @return                    the operation's parameter string.
   */
  protected abstract Object[] parameter();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signature
  /**
   ** Returns operation's signature arrays.
   **
   ** @return                    the operation's signature arrays.
   */
  protected abstract String[] signature();

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
  public void onExecution()
    throws ServiceException {

    invoke(operation(), parameter(), signature());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes an operation on an MBean.
   ** <p>
   ** Because of the need for a signature to differentiate possibly-overloaded
   ** operations, it is much simpler to invoke operations through an MBean proxy
   ** where possible.
   **
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
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object invoke(final String operation, final Object[] parameter, final String[] signature)
    throws ServiceException {

    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE, operation));
    Object result = null;
    try {
      result = this.connection().invoke(objectName(), operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_SUCCESS, operation));
    }
    catch (InstanceNotFoundException e) {
      error(ServiceResourceBundle.format(ServiceError.MANAGEDBEAN_INSTANCE_NOTFOUND, objectName(), e.getLocalizedMessage()));
      if (failonerror())
        throw new ServiceException(ServiceError.ABORT, e);
    }
    catch (ReflectionException e) {
      error(ServiceResourceBundle.format(ServiceError.MANAGEDBEAN_SIGNATURE_NOTFOUND, objectName(), e.getLocalizedMessage()));
      if (failonerror())
        throw new ServiceException(ServiceError.ABORT, e);
    }
    catch (MBeanException e) {
      error(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_ERROR, operation));
      if (failonerror()) {
        // verify if the exception provides a meaningful reason
        // the service exception may have a wrapped exception in it which is the
        // interesting part to report
        Throwable cause = e.getTargetException();
        while (cause.getCause() != null)
          cause = cause.getCause();
        throw new ServiceException(ServiceError.ABORT, cause);
      }
    }
    catch (Exception e) {
      error(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_ERROR, operation));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   silent
  /**
   ** Invokes an operation on an MBean by supressing any message.
   ** <p>
   ** Because of the need for a signature to differentiate possibly-overloaded
   ** operations, it is much simpler to invoke operations through an MBean proxy
   ** where possible.
   **
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
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object silent(final String operation, final Object[] parameter, final String[] signature)
    throws ServiceException {

    debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE, operation));
    Object result = null;
    try {
      result = this.connection().invoke(objectName(), operation, parameter, signature);
      debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_SUCCESS, operation));
    }
    catch (Exception e) {
      debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_ERROR, operation));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return result;
  }
}