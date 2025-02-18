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

    File        :   AbstractDatabaseTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractDatabaseTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.DBSServerContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractDatabaseTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant Database tasks that invokes
 ** operations on the Oracle Database.
 ** <p>
 ** Implementations of  <code>AbstractDatabaseTask</code> inherit its attributes
 ** (see below) for connecting to the Oracle Database.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractDatabaseTask extends AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected DBSServerContext context    = null;
  protected boolean          autoCommit = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractDatabaseTask</code> task that allows use as a
   ** JavaBean.
   **
   ** @param  failOnError        how the task behavior will be in case an error
   **                            is detected.
   */
  public AbstractDatabaseTask(final boolean failOnError) {
    // ensure inheritance
    super();

    this.setFailOnError(failOnError);
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
   ** Establish a connection to the Database server and creates the connection
   ** to the use during task execution.
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
}