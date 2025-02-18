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
    Subsystem   :   Deployment Utilities

    File        :   OIMServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OIMServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import javax.security.auth.login.LoginException;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.iam.platform.OIMClient;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class OIMServerContext
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The client environment wrapper of a RMI connection to an Oracle Identity
 ** Manager Server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OIMServerContext extends RMIServerContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONTEXT_TYPE       = "oim";
  static final String        CONTEXT_REFERENCE  = "contextRef";

  static final String        LOGIN_CONFIG       = "java.security.auth.login.config";

  static final String        CONNECT_PREFIX     = "java.net.ConnectException: ";
  static final String        IOEXCEPTION_PREFIX = "java.io.IOException: ";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private RMIServerContext   server             = null;
  private OIMClient          platform           = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OIMServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OIMServerContext() {
    // ensure inheritance
    super();

    this.server = new RMIServerContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OIMServerContext</code> for the specified
   ** <code>type</code>, <code>protocol</code>, <code>host</code> and
   ** <code>port</code>.
   ** <br>
   ** The required security context is provided by <code>username</code> and
   ** <code>password</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  server             the {@link RMIServerContext} to set.
   **                            <br>
   **                            Allowed object is {@link RMIServerContext}.
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  config             the fullqualified path to the authentication
   **                            configuration.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public OIMServerContext(final RMIServerContext server, final String username, final String password, final String config) {
    // ensure inheritance
    this(server, new SecurityPrincipal(username, password), config);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OIMServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   **
   ** @param  server             the {@link RMIServerContext} to set.
   **                            <br>
   **                            Allowed object is {@link RMIServerContext}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Allowed object is {@link SecurityPrincipal}.
   ** @param  config             the fullqualified path to the authentication
   **                            configuration.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public OIMServerContext(final RMIServerContext server, final SecurityPrincipal principal, final String config) {
    // ensure inheritance
    super();

    // initialize instance attributes
    server(server);
    principal(principal);
    System.setProperty(LOGIN_CONFIG, config);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>refid</code>.
   **
   ** @param  reference          the id of this instance.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference) {
    Object other = reference.getReferencedObject(getProject());
    if(other instanceof OIMServerContext) {
      OIMServerContext that = (OIMServerContext)other;
      this.server           = that.server;
      // ensure inheritance
      super.setRefid(reference);
    }
    else
      handleReferenceError(reference, CONTEXT_TYPE, other.getClass());
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setAuthenticationConfig
  /**
   ** Call by the ANT kernel to inject the file location of authentication
   ** configuration.
   **
   ** @param  config             the fullqualified path to the authentication
   **                            configuration.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setAuthenticationConfig(final String config) {
    System.setProperty(LOGIN_CONFIG, config);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticationConfig
  /**
   ** Returns the location of the security configuration used to establish a
   ** connection to the target system.
   **
   ** @return                    the location of the security configuration used
   **                            to establish a connection to the target system.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String authenticationConfig() {
    return System.getProperty(LOGIN_CONFIG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>server</code> as a {@link Reference} to a declared
   ** {@link ServerType} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted to a
   **                            {@link OIMServerContext}.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link RMIServerContext}.
   */
  public void setContextRef(final Reference reference)
    throws BuildException {

    final Object other = reference.getReferencedObject(this.getProject());
    if (!(other instanceof RMIServerContext))
      handleReferenceError(reference, RMIServerContext.CONTEXT_TYPE, other.getClass());

    server((RMIServerContext)other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Set the server context associated with this task.
   **
   ** @param  server             the {@link RMIServerContext} to set.
   **                            <br>
   **                            Allowed object is {@link RMIServerContext}.
   */
  public final void server(final RMIServerContext server) {
    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("server");

    this.server = server;
    this.setType(server.type());
    this.setHost(server.host());
    this.setPort(server.port());
    this.setProtocol(server.protocol());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Returns the server context associated with this task.
   **
   ** @return                    the {@link ServerContext} associated with this
   **                            task.
   **                            <br>
   **                            Possible object is {@link RMIServerContext}.
   */
  public final RMIServerContext server() {
    return this.server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platform
  /**
   ** Returns the logical context object of this task.
   **
   ** @return                    the logical context object of this task.
   **                            <br>
   **                            Possible object is {@link OIMClient}.
   */
  public final OIMClient platform() {
    return this.platform;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate() {
    // a principal is required for this context type ...
    if (principal() == null)
      handleAttributeMissing("principal");

    // ... and therefore needs also be validated
    principal().validate();

    // without an authentication configuration this context isn't operational
    if (StringUtility.isEmpty(this.authenticationConfig()))
      handleAttributeError(LOGIN_CONFIG);

    // we need also the RMI context for connection purpose
    if (this.server == null)
      handleAttributeError(CONTEXT_REFERENCE);

    this.server.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextType (overridden)
  /**
   ** Returns the specific type of the implemented context.
   **
   ** @return                    the specific type of the implemented context.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String contextType() {
    return CONTEXT_TYPE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (overridden)
  /**
   ** Establish a connection to the OIM server and creates the OIMClient to
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
  public void connect()
    throws ServiceException {

    // always perform the check
    validate();

    if (this.platform == null) {
      // since 11.1.2.0 OIMClient leverages oracle.jrf.ServerPlatformSupport to
      // detect the the login handler instead of the xlconfig.xml configuration
      // The class mentioned above checks at first system properties for
      // existance in the following order
      //   1. weblogic.Name
      //   2. was.install.root || server.root
      //   3. jboss.home.dir
      // and instantiates the appropriate platform handler hence we are setting
      // the system property the implementation checks accordingly to the
      // configured type of the server
      if (this.server.type().isWebsphere())
        System.setProperty("server.root", CONTEXT_TYPE);
      else if (this.server.type().isJboss())
        System.setProperty("jboss.home.dir", CONTEXT_TYPE);
      else
        System.setProperty("weblogic.Name", CONTEXT_TYPE);
      // Passing environment in constructor disables lookup for environment in
      // setup. In any case, we can always enforce manual environment settings
      // by OIMClient.setLookupEnv(configEnv) method.
      this.platform = new OIMClient(this.server.environment());
      try {
        this.platform.login(this.principal().name(), this.principal().password().toCharArray());
        this.established(true);
      }
      catch (LoginException e) {
        this.established(false);
        final String message = e.getMessage();
        if (message.startsWith(CONNECT_PREFIX))
          throw new ServiceException(ServiceError.CONTEXT_CONNECTION, message);
        else if (message.startsWith(IOEXCEPTION_PREFIX))
          throw new ServiceException(ServiceError.CONTEXT_CONNECTION, message);
        else
          throw new ServiceException(ServiceError.CONTEXT_ACCESS_DENIED, this.principal().name(), e.getLocalizedMessage());
      }
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
  public void disconnect()
    throws ServiceException {

    if (this.platform != null) {
      this.established(false);
      this.platform.logout();
      this.platform = null;
    }
  }
}