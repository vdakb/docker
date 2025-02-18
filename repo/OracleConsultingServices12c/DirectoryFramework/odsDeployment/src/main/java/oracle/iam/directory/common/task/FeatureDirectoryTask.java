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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   FeatureDirectoryTask.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureDirectoryTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.ODSServerContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeatureDirectoryTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant LDAP tasks that invokes operations
 ** on a Directory Server.
 ** <p>
 ** Implementations of  <code>FeatureDirectoryTask</code> inherit its attributes
 ** (see below) for connecting to the Directory Server.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class FeatureDirectoryTask extends FeaturePlatformTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String           contextRoot = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>FeatureDirectoryTask</code> event handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected FeatureDirectoryTask() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>FeatureDirectoryTask</code> that use the specified
   ** {@link ODSServerContext} <code>context</code> as the runtime environment.
   **
   ** @param  context            the {@link ODSServerContext} used as the
   **                            runtime environment.
   */
  protected FeatureDirectoryTask(final ODSServerContext context) {
    // ensure inheritance
    super(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextRoot
  /**
   ** Call by the ANT kernel to inject the argument for parameter contextRoot.
   **
   ** @param  contextRoot        the fullqualified context URL to the server.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public void setContextRoot(final String contextRoot)
    throws BuildException {

    this.contextRoot = contextRoot;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextRoot
  /**
   ** Return the fullqualified URL to the remote server.
   **
   ** @return                    the fullqualified context URL to the server.
   */
  public final String contextRoot() {
    return this.contextRoot;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // Method:   connected (overridden)
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
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException    in case an error does occur.
   */
  @Override
  protected void validate() {
	// ensure inheritance
	super.validate();

    if (this.contextRoot == null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.CONTEXT_MANDATORY));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (overridden)
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

    info(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTEXT_CONNECTING, this.context.contextURL(this.contextRoot)));
    try {
      this.context.connect(this.contextRoot);
      info(ServiceResourceBundle.string(ServiceMessage.SERVER_CONNECTED));
    }
    catch (ServiceException e) {
      error(ServiceResourceBundle.format(ServiceError.CONTEXT_CONNECTION, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (overridden)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  protected void disconnect()
    throws ServiceException {

    debug(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTEXT_DISCONNECTING, this.context.contextURL(contextRoot)));
    try {
      this.context.disconnect();
      debug(ServiceResourceBundle.string(ServiceMessage.SERVER_DISCONNECTED));
    }
    catch (ServiceException e) {
      error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }
}