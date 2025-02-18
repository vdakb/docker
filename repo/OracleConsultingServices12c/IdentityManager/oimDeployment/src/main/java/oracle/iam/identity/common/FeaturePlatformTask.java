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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   FeaturePlatformTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FeaturePlatformTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
    3.1.0.0     2015-03-01  DSteding    Fix Defect DE-000148
                                        error in FeaturePlatformTask connect
                                        method throws no exception
*/

package oracle.iam.identity.common;

import java.util.Properties;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.iam.platform.OIMClient;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.AbstractTask;

import oracle.hst.deployment.type.RMIServerContext;
import oracle.hst.deployment.type.OIMServerContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeaturePlatformTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant OIM Client tasks that invokes
 ** operations on Identity Manager server.
 ** <p>
 ** Implementations of  <code>AbstractPlatformTask</code> inherit its attributes
 ** (see below) for connecting to Identity Manager server.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class FeaturePlatformTask extends AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String USERNAME = "xelsysadm";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected OIMClient        platform = null;

  protected OIMServerContext server   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeaturePlatformTask</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FeaturePlatformTask() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeaturePlatformTask</code> with the specified
   ** environment properties and the password to authenticate the
   ** {@link #USERNAME}.
   **
   ** @param  server             the RMI/JNDI properties to establish a
   **                            connection.
   */
  public FeaturePlatformTask(final OIMServerContext server) {
    // ensure inheritance
    super();

    // initailize instance attributes
    this.server = server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeaturePlatformTask</code> with the specified
   ** environment properties and the password to authenticate the
   ** {@link #USERNAME}.
   **
   ** @param  environment        the RMI/JNDI properties to establish a
   **                            connection.
   ** @param  password           the passsword the administrative user
   **                            {@link #USERNAME} to login in  Oracle
   **                            Identity Manager.
   **
   ** @throws Exception          If there is an error during login.
   */
  protected FeaturePlatformTask(final Properties environment, final String password)
    throws Exception {

    this(environment, USERNAME, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeaturePlatformTask</code> with the specified
   ** environment properties and the password to authenticate the
   ** {@link #USERNAME}.
   **
   ** @param  environment        the RMI/JNDI properties to establish a
   **                            connection.
   ** @param  username           the administrative user used to login in
   **                            Identity Manager.
   ** @param  password           the passsword for above.
   **
   ** @throws Exception          If there is an error during login.
   */
  protected FeaturePlatformTask(final Properties environment, final String username, final String password)
    throws Exception {

    // ensure inheritance
    super();

    // Passing environment in constructor disables lookup for environment in
    // setup. In any case, we can always enforce manual environment settings by
    // OIMClient.setLookupEnv(configEnv) method.
    this.platform = new OIMClient(environment);
    this.platform.login(username, password.toCharArray());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>server</code> as a {@link Reference} to a declared
   ** {@link OIMServerContext} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted to a boolean
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link OIMServerContext}.
   */
  public void setContextRef(final Reference reference)
    throws BuildException {

    final Object object = reference.getReferencedObject(this.getProject());
    if (!(object instanceof OIMServerContext))
      handleReferenceError(reference, OIMServerContext.CONTEXT_TYPE, object.getClass());

    this.server = (OIMServerContext)object;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setAuthenticationConfig
  /**
   ** Call by the ANT kernel to inject the file location of authentication
   ** configuration.
   **
   ** @param  config        the fullqualified path to the authentication
   **                       configuration.
   */
  public void setAuthenticationConfig(final String config) {
    this.server.setAuthenticationConfig(config);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticationConfig
  /**
   ** Returns the location of the security configuration used to establish a
   ** connection to the target system.
   **
   ** @return                    the location of the security configuration used
   **                            to establish a connection to the target system.
   */
  public final String authenticationConfig() {
    return this.server.authenticationConfig();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   OIMContext
  /**
   ** Returns the context object of this task.
   **
   ** @return                    the context object of this task.
   */
  protected final OIMServerContext OIMContext() {
    return this.server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   RMIContext
  /**
   ** Returns the context object of this task.
   **
   ** @return                    the context object of this task.
   */
  protected final RMIServerContext RMIContext() {
    return this.server.server();
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
   ** Establish a connection to the OIM server and creates the connection to
   ** the use during task execution.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @throws BuildException     if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  protected void connect()
    throws BuildException {

    info(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTEXT_CONNECTING, this.server.server().contextURL()));
    try {
      this.server.connect();
      info(ServiceResourceBundle.string(ServiceMessage.SERVER_CONNECTED));
    }
    catch (ServiceException e) {
      // Fix Defect DE-000148
      // error in FeaturePlatformTask connect method throws no exception
      throw new BuildException(ServiceResourceBundle.format(ServiceError.CONTEXT_CONNECTION, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (AbstractTask)
  /**
   ** Close a connection to the target system.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  protected void disconnect()
    throws BuildException {

    debug(ServiceResourceBundle.format(ServiceMessage.SERVER_CONTEXT_DISCONNECTING, this.server.server().contextURL()));
    try {
      this.server.disconnect();
      debug(ServiceResourceBundle.string(ServiceMessage.SERVER_DISCONNECTED));
    }
    catch (ServiceException e) {
      throw new BuildException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns an instance of a Business Facade by invoking the method platform
   ** service resolver to return the appropriate instance of the desired
   ** Business Facade.
   ** <br>
   ** The utility factory keeps track of created Business Facade and on
   ** execution of close() will free all aquired resources of the created
   ** Business Facade.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @param  <T>                the expected type of the Business Facade.
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   */
  public final <T> T service(final Class<T> serviceClass) {
    return this.server.platform().getService(serviceClass);
  }
}