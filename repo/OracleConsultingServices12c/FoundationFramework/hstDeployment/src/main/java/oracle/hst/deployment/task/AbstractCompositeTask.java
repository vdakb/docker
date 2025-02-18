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

    File        :   AbstractCompositeTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractCompositeTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.task;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.SOAServerContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractCompositeTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant SOA Client tasks that invokes
 ** operations on the Oracle SOA Suite server.
 ** <p>
 ** Implementations of  <code>AbstractCompositeTask</code> inherit its
 ** attributes (see below) for connecting to the Oracle WebLogic Domain server.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractCompositeTask extends AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private SOAServerContext server = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractCompositeTask</code> Ant task that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractCompositeTask() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>server</code> as a {@link Reference} to a declared
   ** {@link SOAServerContext} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted to a
   **                            {@link SOAServerContext}.
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link SOAServerContext}.
   */
  public void setContextRef(final Reference reference)
    throws BuildException {

    final Object object = reference.getReferencedObject(this.getProject());
    if (!(object instanceof SOAServerContext))
      handleReferenceError(reference, SOAServerContext.CONTEXT_TYPE, object.getClass());

    this.server = (SOAServerContext)object;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Returns the context object of this task.
   **
   ** @return                    the context object of this task.
   */
  protected final SOAServerContext server() {
    return this.server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractTask)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  protected void validate() {

    if (this.server == null)
      handleAttributeMissing("server");

    this.server.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connected (AbstractTask)
  /**
   ** Returns the state of connection.
   **
   ** @return                    the state of connection.
   */
  @Override
  protected boolean connected() {
    return this.server.established();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (AbstractTask)
  /**
   ** Establish a connection to the SOA server and creates the connection to
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

    info(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTEXT_CONNECTING, this.server.serviceURL()));
    try {
      this.server.connect();
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

    debug(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTEXT_DISCONNECTING, this.server.contextURL()));
    this.server.disconnect();
    debug(ServiceResourceBundle.string(ServiceMessage.SERVER_DISCONNECTED));
  }
}