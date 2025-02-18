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

    File        :   AbstractMetadataTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractMetadataTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.task;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.MDSServerContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractMetadataTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant OIM Client tasks that invokes
 ** operations on the Oracle Metadata database.
 ** <p>
 ** Implementations of  <code>AbstractMetadatTask</code> inherit its attributes
 ** (see below) for connecting to the Oracle Oracle Metadata database.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractMetadataTask extends AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final MetadataProvider provider;
  protected       MDSServerContext context  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractMetadataTask</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractMetadataTask() {
    // ensure inheritance
    super();

    // call the factory method of the subclasses initialize the provider instance
    this.provider = createProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>server</code> as a {@link Reference} to a declared
   ** {@link MDSServerContext} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted to a
   **                            {@link MDSServerContext}.
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link MDSServerContext}.
   */
  public void setContextRef(final Reference reference)
    throws BuildException {

    final Object object = reference.getReferencedObject(this.getProject());
    if (!(object instanceof MDSServerContext))
      handleReferenceError(reference, MDSServerContext.CONTEXT_TYPE, object.getClass());

    this.context = (MDSServerContext)object;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCancelOnException
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** cancelOnException.
   **
   ** @param  cancelOnException  a Boolean value indicating whether to stop the
   **                            delete if an exception is encountered.
   **                            <p>
   **                            If the target repository is a database
   **                            repository, the incomplete delete  will be
   **                            rolled back.
   */
  public void setCancelOnException(final boolean cancelOnException) {
    this.provider.cancelOnException(cancelOnException);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancelOnException
  /**
   ** Returns the value indicating whether to stop the delete if an exception is
   ** encountered.
   **
   ** @return                    the value indicating whether to stop the delete
   **                            if an exception is encountered.
   */
  public final boolean cancelOnException() {
    return this.provider.cancelOnException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the context object of this task.
   **
   ** @return                    the context object of this task.
   */
  protected final MDSServerContext context() {
    return this.context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractTask)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException    in case an error does occur.
   */
  @Override
  protected void validate() {

    if (this.context == null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.CONTEXT_MANDATORY));

    this.context.validate();
    this.provider.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connected (AbstractTask)
  /**
   ** Returns the state of connection.
   **
   ** @return                    the state of connection.
   */
  @Override
  protected final boolean connected() {
    return this.context.established();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (AbstractTask)
  /**
   ** Establish a connection to the JMX server and creates the MBeanServer to
   ** the use during task execution.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @throws ServiceException   if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  protected void connect()
    throws ServiceException {

    info(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTEXT_CONNECTING, this.context.contextURL()));
    try {
      this.context.connect();
      info(ServiceResourceBundle.string(ServiceMessage.SERVER_CONNECTED));
    }
    catch (ServiceException e) {
      error(ServiceResourceBundle.format(ServiceError.CONTEXT_CONNECTION, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (AbstractTask)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  protected void disconnect()
    throws ServiceException {

    debug(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTEXT_DISCONNECTING, this.context.contextURL()));
    try {
      this.context.disconnect();
      debug(ServiceResourceBundle.string(ServiceMessage.SERVER_DISCONNECTED));
    }
    catch (ServiceException e) {
      error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

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

    this.provider.execute(this.context.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProvider
  /**
   ** Factory method to create the appropriate {@link MetadataProvider}.
   **
   ** @return                    the appropriate {@link MetadataProvider}.
   */
  protected abstract MetadataProvider createProvider();
}