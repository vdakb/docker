package oracle.jdeveloper.connection.oim.service;

import java.util.Properties;

import java.io.File;

import javax.ide.extension.Extension;

import javax.naming.Context;

import javax.security.auth.login.LoginException;

import oracle.iam.platform.OIMClient;

import oracle.ide.ExtensionRegistry;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

import oracle.jdeveloper.connection.oim.Bundle;

import oracle.jdeveloper.connection.oim.model.IdentityServer;
import oracle.jdeveloper.connection.oim.model.IdentityServerConstant;

////////////////////////////////////////////////////////////////////////////////
// class IdentityService
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>IdentityService</code> implements the base functionality
 ** of an Identity Service based on Oracle Identity Manager itself.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityService implements Manageable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String                MALFORMED_PREFIX   = "java.net.MalformedURLException: ";
  static final String                CONNECTION_PREFIX  = "java.net.ConnectException: ";
  static final String                SECURITY_PREFIX    = "javax.security.auth.login.LoginException: java.lang.SecurityException: ";
  static final String                SECURITY_CONFIG    = "config/oim";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static ClassLoader         patched            = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the wrapper of target specific parameters where this connector is attachd
   ** to
   */
  protected final IdentityServer      resource;

  /**
   ** the wrapper of target specific features where this connector is attachd
   ** to
   */
  protected final IdentityServer.Type type;

  private OIMClient                   platform   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityService</code> which is associated with the
   ** specified task.
   **
   ** @param  resource           the {@link IdentityServer} definition where
   **                            this connector is associated with.
   **
   ** @throws IdentityServiceException in the event of misconfiguration (such as
   **                                  failure to set an essential property) or
   **                                  if initialization fails.
   */
  public IdentityService(final IdentityServer resource)
    throws IdentityServiceException {

    // ensure inheritance
    super();

    // create the property mapping for the Server Type control
    this.resource = resource;
    try {
      this.type = IdentityServer.Type.fromValue(resource.serverType());
    }
    catch (IllegalArgumentException e) {
      throw new IdentityServiceException(Bundle.format(Bundle.CONTEXT_SERVERTYPE_NOTSUPPORTED, resource.serverType()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the this <code>IT Resource</code>.
   **
   ** @return                    the name of this <code>IT Resource</code>.
   */
  public final String name() {
    return this.resource.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the {@link IdentityServer} of the this <code>IT Resource</code>.
   **
   ** @return                    the {@link IdentityServer} of this
   **                           <code>IT Resource</code>.
   */
  public final IdentityServer resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the host name of the Managed Server used to connect to.
   **
   ** @return                    the host name of the Managed Server used to
   **                            connect to.
   */
  public final String serverName() {
    return this.resource.serverName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the port the Managed Server used to connect to is listening on.
   **
   ** @return                    the port the Managed Server used to connect to
   **                            is listening on.
   */
  public final int serverPort() {
    return this.resource.serverPort();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverType
  /**
   ** Returns the type of the Managed Server used to connect to.
   ** <p>
   ** If {@link IdentityServer#SERVER_TYPE} is not mapped this method
   ** returns {@link IdentityServerConstant#SERVER_TYPE_WEBLOGIC}.
   **
   ** @return                    the type of the Managed Server used to
   **                            connect to.
   */
  public final String serverType() {
    return this.resource.serverType();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverTypeProperty
  /**
   ** Returns the type of the server where the Identity Managed is running and
   ** this IT Resource is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link IdentityServer#SERVER_TYPE_PROPERTY}.
   **
   ** @return                    the property value to set for the server type
   **                            Identity Managed is deployed on
   */
  public final String serverTypeProperty() {
    return this.resource.serverTypeProperty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   securityConfig
  /**
   ** Returns the security configuration of the Managed Server used to connect
   ** to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SECURITY_CONFIG}.
   **
   ** @return                    the security configuration of the Managed
   **                            Server used to connect to.
   */
  public final String securityConfig() {
    final Extension extension = ExtensionRegistry.getExtensionRegistry().findExtension("oracle.ocs.workspace.iam");
    final File      directory = ExtensionRegistry.getExtensionRegistry().getInstallDirectory(extension);
    final File      config    = new File(directory, SECURITY_CONFIG);
    File module = null;
    if (this.type == IdentityServer.Type.WEBLOGIC)
      module = new File(config, "authwl.conf");
    else if (this.type == IdentityServer.Type.WEBSPHERE)
      module = new File(config, "authws.conf");

    return module.getAbsolutePath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns whether the connection to the Managed Server is secured by SSL.
   **
   ** @return                    <code>true</code> if the connection to the
   **                            Managed Server is secured by SSL,
   **                            <code>false</code> otherwise.
   */
  public final boolean secureSocket() {
    return this.resource.secureSocket();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Returns the application context of the Managed Server used to connect to.
   **
   ** @return                    application context of the Managed Server used
   **                            to connect to.
   */
  public final String serverContext() {
    return this.resource.serverContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Returns the name of the security principal of the Managed Server used to
   ** connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServer#PRINCIPAL_NAME}.
   **
   ** @return                    the name of the security principal Database
   **                            Server used to connect to.
   */
  public final String principalName() {
    return this.resource.principalName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password of the security principal of the Managed Server used
   ** to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServer#PRINCIPAL_PASSWORD}.
   **
   ** @return                    the password of the security principal Database
   **                            Server used to connect to.
   */
  public final String principalPassword() {
    return this.resource.principalPassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialContextFactory
  /**
   ** Returns the class name of the initial context factory.
   **
   ** @return                    the class name of the initial context factory.
   */
  public final String initialContextFactory() {
    return this.type.factory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link OIMClient} by creating the appropriate
   ** environment from the attributes of the associated model.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Application Context server.
   **
   ** @throws IdentityServiceException if the {@link OIMClient} could not be
   **                                created at the first time this method is
   **                                invoked.
   */
  public OIMClient connect()
    throws IdentityServiceException {

    if (this.platform == null)
      this.platform = connect(this.environment());

    return this.platform;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link OIMClient} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @param  environment        environment used to create the initial
   **                            {@link OIMClient}.
   **                            <code>null</code> indicates an empty
   **                            environment.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Identity Server.
   **
   ** @throws IdentityServiceException if the {@link OIMClient} could not be
   **                                created at the first time this method is
   **                                invoked.
   */
  public final OIMClient connect(final Properties environment)
    throws IdentityServiceException {

    // configue the runtime classpath
    if (patched == null)// || !patched)
      patched = IdentityLoader.patch();

    this.type.saveSystemProperties();
    

    OIMClient platform  = null;
    try {
      // since 11.1.2.0 OIMClient leverages oracle.jrf.ServerPlatformSupport to
      // detect the login handler instead of the xlconfig.xml configuration
      // The class mentioned above checks at first system properties for
      // existance in the following order
      //   1. weblogic.Name
      //   2. was.install.root || server.root
      //   3. jboss.home.dir
      // and instantiates the appropriate platform handler hence we are setting
      // the system property the implementation checks accordingly to the
      // configured type of the server
      System.setProperty(IdentityServerConstant.TIMEOUT_WEBLOGIC_CONNECT, "1");
      System.setProperty(IdentityServerConstant.JAVA_SECURITY_CONFIG, securityConfig());
      System.setProperty(this.type.property(),                        serverTypeProperty());
      
      // Passing environment in constructor disables lookup for environment in
      // setup. In any case, we can always enforce manual environment settings
      // by OIMClient.setLookupEnv(configEnv) method.
      platform = new OIMClient(environment);
      platform.login(principalName(), principalPassword().toCharArray());
    }
    catch (LoginException e) {
      final String message = e.getMessage();
      if (message.startsWith(MALFORMED_PREFIX))
        throw new IdentityServiceException(e.getLocalizedMessage());
      else if (message.startsWith(CONNECTION_PREFIX))
        throw new IdentityServiceException(Bundle.format(Bundle.CONTEXT_CONNECTION_ERROR, e.getLocalizedMessage()));
      else if (message.startsWith(SECURITY_PREFIX))
        throw new IdentityServiceException(Bundle.format(Bundle.CONTEXT_AUTHENTICATION, principalName()));
      else
        throw new IdentityServiceException(e.getLocalizedMessage());
    }
// TODO:
// Figure out how we can catch the exception below id the class path isn't in
// the shape to find it    
//    catch (oracle.iam.platform.utils.NoSuchServiceException e) {
//      throw new IdentityServiceException(e.getLocalizedMessage());
//    }
    catch (Throwable t) {
      throw new IdentityServiceException(t.getLocalizedMessage());
    }
    finally {
      // reset the system properties to the origin
      this.type.restoreSystemProperties();
    }
    return platform;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Closes the managed application context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   */
  public void disconnect() {
    this.disconnect(this.platform);
    this.platform = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Closes an unmanaged context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param  context            the {@link OIMClient} to close.
   */
  public void disconnect(final OIMClient context) {
    if (context != null)
      context.logout();
  }

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
   ** @param  <T>                the expected class type.
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   */
  public final <T> T service(final Class<T> serviceClass) {
    return this.platform.getService(serviceClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** with this task that afterwards can be passed to establish a connection to
   ** the target system.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Managed Server.
   */
  protected Properties environment() {
    return environment(serviceURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of this task that
   ** afterwards can be passed to establish a connection to the target system.
   **
   ** @param  providerURL        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **
   ** @return                    the context this connector use to communicate
   **                            with the RMI server.
   */
  protected Properties environment(final String providerURL) {
    Properties environment = new Properties();
    // Set up environment for creating initial context
    environment.put(Context.INITIAL_CONTEXT_FACTORY,       initialContextFactory());
    environment.put(Context.PROVIDER_URL,                  providerURL);
    environment.put(Context.SECURITY_PRINCIPAL,            this.principalName());
    environment.put(Context.SECURITY_CREDENTIALS,          this.principalPassword());
    environment.put("sun.rmi.transport.connectionTimeout", "1");
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Constructs an service URL to bind to.
   ** <p>
   ** At first it checks if the context URL is set. If so it will return it as
   ** it is.
   **
   ** @return                    the service to bind to.
   */
  protected String serviceURL() {
    // Create a URL string from the parts describe by protocol, host and port
    return String.format("%s://%s:%s/%s", this.secureSocket() ? IdentityServerConstant.PROTOCOL_WEBLOGIC_SECURE : IdentityServerConstant.PROTOCOL_WEBLOGIC_DEFAULT, this.serverName(), this.serverPort(), this.serverContext());
  }
}