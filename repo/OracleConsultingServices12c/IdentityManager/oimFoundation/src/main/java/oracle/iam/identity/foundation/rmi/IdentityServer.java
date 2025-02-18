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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   IdentityServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityServer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.rmi;

import java.util.Properties;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import javax.naming.Context;

import javax.security.auth.login.LoginException;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;

import oracle.iam.platform.OIMClient;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLoggable;
import oracle.iam.identity.foundation.AbstractMetadataTask;

import oracle.iam.identity.foundation.resource.IdentityServerBundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServer
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>IdentityServer</code> implements the base functionality
 ** of an Oracle Identity Manager Connector for a Generic Identity Service based
 ** on Oracle Identity Manager itself.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class IdentityServer extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String                   MALFORMED_PREFIX   = "java.net.MalformedURLException: ";
  static final String                   CONNECTION_PREFIX  = "java.net.ConnectException: ";
  static final String                   SECURITY_PREFIX    = "javax.security.auth.login.LoginException: java.lang.SecurityException: ";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the wrapper of target specific parameters where this connector is attached
   ** to
   */
  protected final IdentityServerResource resource;

  /**
   ** the wrapper of target specific features where this connector is attached
   ** to
   */
  protected final IdentityServerFeature  feature;

  /**
   ** the wrapper of target specific features where this connector is attached
   ** to
   */
  protected final IdentityServerType     type;

  private OIMClient                      platform   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityServer</code> which is associated with the
   ** specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
   **                            instantiated this {@link AbstractLoggable}.
   **                            <br>
   **                            Allowed object is {@link AbstractMetadataTask}.
   ** @param  resource           the {@link IdentityServerResource} IT
   **                            Resource definition where this connector is
   **                            associated with.
   **                            instantiated this {@link AbstractLoggable}.
   **                            <br>
   **                            Allowed object is
   **                            {@link IdentityServerResource}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected IdentityServer(final AbstractMetadataTask task, final IdentityServerResource resource)
    throws TaskException {

    // ensure inheritance
    this(task, resource, unmarshal(task, resource.feature()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityServer</code> which is associated with the
   ** specified task.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  resource           the {@link IdentityServerResource} IT
   **                            Resource definition where this connector is
   **                            associated with.
   **                            <br>
   **                            Allowed object is
   **                            {@link IdentityServerResource}.
   ** @param  feature            the Lookup Definition providing the target
   **                            system specific features like objectClasses,
   **                            attribute id's etc.
   **                            <br>
   **                            Allowed object is
   **                            {@link IdentityServerFeature}.
   **
   ** @throws TaskException      if the server type specified by
   **                            {@link IdentityServerResource}
   **                            <code>resource</code> isn't supported.
   */
  protected IdentityServer(final Loggable loggable, final IdentityServerResource resource, final IdentityServerFeature feature)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    // create the property mapping for the Database control
    this.resource = resource;
    this.feature  = feature;
    try {
      this.type = IdentityServerType.fromValue(resource.serverType());
    }
    catch (IllegalArgumentException e) {
      throw new IdentityServerException(IdentityServerError.CONTEXT_SERVERTYPE_NOTSUPPORTED, resource.serverType());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceInstance
  /**
   ** Returns the instance key of the IT Resource where this wrapper belongs to.
   **
   ** @return                    the instance key of the IT Resource where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public final long resourceInstance() {
    return this.resource.instance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceName
  /**
   ** Returns the instance name of the IT Resource where this wrapper belongs
   ** to.
   **
   ** @return                    the instance name of the IT Resource where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String resourceName() {
    return this.resource.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the host name of the Manager Server used to connect to.
   **
   ** @return                    the host name of the Manager Server used to
   **                            connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverName() {
    return this.resource.serverName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the port the Manager Server used to connect to is listening on.
   **
   ** @return                    the port the Manager Server used to connect to
   **                            is listening on.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final int serverPort() {
    return this.resource.serverPort();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverType
  /**
   ** Returns the type of the Manager Server used to connect to.
   ** <p>
   ** If {@link IdentityServerResource#SERVER_TYPE} is not mapped this method
   ** returns {@link IdentityServerConstant#SERVER_TYPE_WEBLOGIC}.
   **
   ** @return                    the type of the Manager Server used to
   **                            connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverType() {
    return this.resource.serverType();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverTypeProperty
  /**
   ** Returns the type of the server where the Identity Manager is running and
   ** this IT Resource is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link IdentityServerResource#SERVER_TYPE_PROPERTY}.
   **
   ** @return                    the property value to set for the server type
   **                            Identity Manager is deployed on
   **                            <br>
   **                            Possible object is {@link String}.
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
   ** {@link IdentityServerResource#SECURITY_CONFIG}.
   **
   ** @return                    the security configuration of the Managed
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String securityConfig() {
    return this.resource.securityConfig();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns whether the connection to the Manager Server is secured by SSL.
   **
   ** @return                    <code>true</code> if the connection to the
   **                            Manager Server is secured by SSL,
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final boolean secureSocket() {
    return this.resource.secureSocket();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Returns the application context of the Manager Server used to connect to.
   **
   ** @return                    application context of the Manager Server used
   **                            to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverContext() {
    return this.resource.rootContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPrincipalName
  /**
   ** Returns the name of the security principal of the Managed Server used to
   ** connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerResource#SERVER_PRINCIPAL_NAME}.
   **
   ** @return                    the name of the security principal Database
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverPrincipalName() {
    return this.resource.serverPrincipalName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPrincipalPassword
  /**
   ** Returns the password of the security principal of the Managed Server used
   ** to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerResource#SERVER_PRINCIPAL_PASSWORD}.
   **
   ** @return                    the password of the security principal Database
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverPrincipalPassword() {
    return this.resource.serverPrincipalPassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextPrincipalName
  /**
   ** Returns the name of the security principal of the Application Context used
   ** to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerResource#CONTEXT_PRINCIPAL_NAME}.
   **
   ** @return                    the name of the security principal Database
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextPrincipalName() {
    return this.resource.contextPrincipalName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextPrincipalPassword
  /**
   ** Returns the password of the security principal of the Application Context
   ** used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerResource#CONTEXT_PRINCIPAL_PASSWORD}.
   **
   ** @return                    the password of the security principal Database
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextPrincipalPassword() {
    return this.resource.contextPrincipalPassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeLanguage
  /**
   ** Returns the language code of the Application Context used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerResource#LOCALE_LANGUAGE}.
   **
   ** @return                    the language code of the Database Server used
   **                            to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localeLanguage() {
    return this.resource.localeLanguage();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeCountry
  /**
   ** Returns the country code of the Application Context used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerResource#LOCALE_COUNTRY}.
   **
   ** @return                    the country code of the Database Server used to
   **                            connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localeCountry() {
    return this.resource.localeCountry();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeTimeZone
  /**
   ** Returns the timezone of the Application Context used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerResource#LOCALE_TIMEZONE}.
   **
   ** @return                    the country code of the Database Server used to
   **                            connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localeTimeZone() {
    return this.resource.localeTimeZone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   feature
  /**
   ** Returns feature mapping of the connector.
   **
   ** @return                    feature mapping of the connector.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final IdentityServerFeature feature() {
    return this.feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Returns the URL encoding.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerConstant#URL_ENCODING}.
   ** <p>
   ** If {@link IdentityServerConstant#URL_ENCODING} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link IdentityServerConstant#URL_ENCODING_DEFAULT}
   **
   ** @return                    the URL encoding.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String urlEncoding() {
    return this.feature.urlEncoding();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialContextFactory
  /**
   ** Returns the class name of the initial context factory.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerConstant#INITIAL_CONTEXT_FACTORY}.
   **
   ** @return                    the class name of the initial context factory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String initialContextFactory() {
    return this.type.factory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platform
  /**
   ** Returns an object that implements the given interface to allow access to
   ** non-standard methods, or standard methods not exposed by the proxy.
   **
   ** @return                    an object that implements the interface. May be
   **                            a proxy for the actual implementing object.
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
  // Method:   build
  /**
   ** Factory method to create a <code>IdentityServer</code> which is associated
   ** with the specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
   **                            <br>
   **                            Allowed object is {@link AbstractMetadataTask}.
   ** @param  resource           the {@link IdentityServerResource} IT
   **                            Resource definition where this connector is
   **                            associated with.
   **                            <br>
   **                            Allowed object is
   **                            {@link IdentityServerResource}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as
   **                            <code>IdentityServerResource</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link IdentityServerResource}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static IdentityServer build(final AbstractMetadataTask task, final IdentityServerResource resource)
    throws TaskException {

    return new IdentityServer(task, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>IdentityServer</code> which is associated
   ** with the specified task.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  resource           the {@link IdentityServerResource} IT
   **                            Resource definition where this connector is
   **                            associated with.
   ** @param  feature            the Lookup Definition providing the target
   **                            system specific features like objectClasses,
   **                            attribute id's etc.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as
   **                            <code>IdentityServerResource</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link IdentityServerResource}.
   **
   ** @throws TaskException      if the server type specified by
   **                            {@link IdentityServerResource}
   **                            <code>resource</code> isn't supported.
   */
  public static IdentityServer build(final Loggable loggable, final IdentityServerResource resource, final IdentityServerFeature feature)
    throws TaskException {

    return new IdentityServer(loggable, resource, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder buffer = new StringBuilder(StringUtility.formatCollection(this.resource));
    buffer.append(this.feature.toString());
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link OIMClient} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Application Context server.
   **                            <br>
   **                            Possible object is {@link OIMClient}.
   **
   ** @throws IdentityServerException if the {@link OIMClient} could not be
   **                                created at the first time this method is
   **                                invoked.
   */
  public OIMClient connect()
    throws IdentityServerException {

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
   **                            <br>
   **                            Possible object is {@link OIMClient}.
   **
   ** @throws IdentityServerException if the {@link OIMClient} could not be
   **                                created at the first time this method is
   **                                invoked.
   */
  public final OIMClient connect(final Properties environment)
    throws IdentityServerException {

    final String method = "connect";
    this.type.saveSystemProperties();

    OIMClient platform  = null;
    try {
      debug(method, IdentityServerBundle.format(IdentityServerMessage.CONNECTING_TO, URLDecoder.decode(environment.getProperty(Context.PROVIDER_URL), urlEncoding())));
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
      System.setProperty(IdentityServerConstant.JAVA_SECURITY_CONFIG, securityConfig());
      System.setProperty(this.type.property(),                        serverTypeProperty());
      // Passing environment in constructor disables lookup for environment in
      // setup. In any case, we can always enforce manual environment settings
      // by OIMClient.setLookupEnv(configEnv) method.
      platform = new OIMClient(this.environment());
      platform.login(contextPrincipalName(), contextPrincipalPassword().toCharArray());
    }
    catch (UnsupportedEncodingException e) {
      throw new IdentityServerException(IdentityServerError.CONTEXT_ENCODING_NOTSUPPORTED, urlEncoding());
    }
    catch (LoginException e) {
      final String message = e.getMessage();
      if (message.startsWith(MALFORMED_PREFIX))
        throw new IdentityServerException(e);
      else if (message.startsWith(CONNECTION_PREFIX))
        throw new IdentityServerException(IdentityServerError.CONTEXT_CONNECTION_ERROR, e.getLocalizedMessage());
      else if (message.startsWith(SECURITY_PREFIX))
        throw new IdentityServerException(IdentityServerError.CONTEXT_AUTHENTICATION, contextPrincipalName());
      else
        throw new IdentityServerException(e);
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
   **                            <br>
   **                            Allowed object is {@link OIMClient}.
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
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   **                            <br>
   **                            Possible object is <code>T</code>.
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
   **                            <br>
   **                            Possible object is {@link Properties}.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the context this connector use to communicate
   **                            with the RMI server.
   **                            <br>
   **                            Possible object is {@link Properties}.
   */
  protected Properties environment(final String providerURL) {
    Properties environment = new Properties();
    // Set up environment for creating initial context
    environment.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory());
    environment.put(Context.PROVIDER_URL,            providerURL);
    environment.put(Context.SECURITY_PRINCIPAL,      this.serverPrincipalName());
    environment.put(Context.SECURITY_CREDENTIALS,    this.serverPrincipalPassword());
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String serviceURL() {
    // Create a URL string from the parts describe by protocol, host and port
    return String.format("%s://%s:%d/%s", this.secureSocket() ? IdentityServerConstant.PROTOCOL_WEBLOGIC_SECURE : IdentityServerConstant.PROTOCOL_WEBLOGIC_DEFAULT, this.serverName(), this.serverPort(), this.serverContext());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link IdentityServerFeature} from a path.
   **
   ** @param  task               the {@link AbstractMetadataTask} where the
   **                            object to create will belong to.
   **                            <br>
   **                            Allowed object is {@link AbstractMetadataTask}.
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link IdentityServerFeature} created from
   **                            the specified propertyFile.
   **                            <br>
   **                            Possible object is
   **                            {@link IdentityServerFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected static IdentityServerFeature unmarshal(final AbstractMetadataTask task, final String path)
    throws TaskException {

    final IdentityServerFeature feature = IdentityServerFeature.build(task);
    if (!StringUtility.isEmpty(path)) {
      try {
        final MDSSession session  = task.createSession();
        final PManager   manager  = session.getPersistenceManager();
        final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
        IdentityServerFeatureFactory.configure(feature, document);
      }
      catch (ReferenceException e) {
        throw new TaskException(e);
      }
    }
    return feature;
  }
}