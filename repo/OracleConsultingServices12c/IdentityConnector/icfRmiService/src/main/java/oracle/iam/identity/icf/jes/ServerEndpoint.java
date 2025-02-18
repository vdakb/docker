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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Java Enterprise Service Connector Library

    File        :   ServerEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServerEndpoint.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.jes;

import java.util.Map;
import java.util.LinkedHashMap;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.icf.foundation.AbstractEndpoint;

import oracle.iam.identity.icf.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// class ServerEndpoint
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>ServerEndpoint</code> implements the base functionality
 ** of an Oracle Identity Manager Connector for a JEE Service Provider targeting
 ** Oracle Identity Governance itself.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerEndpoint<T extends ServerEndpoint> extends AbstractEndpoint<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the type name of a WebLogic Managed Server */
  public static final String        SERVER_TYPE_WEBLOGIC       = "weblogic";

  /** the type name of a WebSphere Server */
  public static final String        SERVER_TYPE_WEBSPHERE      = "websphere";

   /** the name of the system property to identify a WebLogic Managed Server */
  public static final String        SYSTEM_PROPERTY_WEBLOGIC   = "weblogic.Name";

  /** the name of the system property to identify a WebSphere Server */
  public static final String        SYSTEM_PROPERTY_WEBSPHERE  = "was.install.root";

  /** the protocol each WebLogic Managed Server is using */
  public static final String        PROTOCOL_WEBLOGIC_DEFAULT  = "t3";

  /** the protocol each WebLogic Managed Server is using over SSL */
  public static final String        PROTOCOL_WEBLOGIC_SECURE   = "t3s";

  /** the protocol each WebLogic Managed Server is using */
  public static final String        PROTOCOL_WEBSPHERE_DEFAULT = "corbaname:iiop";

  /** the protocol each WebLogic Managed Server is using over SSL */
  public static final String        PROTOCOL_WEBSPHERE_SECURE  = "corbaname:iiops";

  /** the system property name to manage JAAS login configurations */
  public static final String        JAVA_SECURITY_CONFIG       = "java.security.auth.login.config";

  /**
   ** Attribute tag which must be defined on an <code>Metadata Descriptor</code>
   ** to specify the name of the target system.
   */
  public static final String         SERVER_NAME               = "server-name";

  /**
   ** Attribute tag which must be defined on an <code>Metadata Descriptor</code>
   ** to specify the type of the target system.
   */
  public static final String         SERVER_TYPE               = "server-type";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the Initial Context factory.
   */
  public static final String        INITIAL_CONTEXT_FACTORY    = "context-factory";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the path to the JAAS login module configuration.
   */
  public static final String        LOGIN_CONFIG               = "login-config";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the the domain principal.
   */
  public static final String        DOMAIN_PRINCIPAL           = "domain-principal";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the the domain password.
   */
  public static final String        DOMAIN_PASSWORD           = "domain-password";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                    serverName;
  private String                    loginConfig;
  /**
   ** The wrapper of target specific features where this connector is attached
   ** to
   */
  private Type                      type;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the username and password credentials of the
   ** <code>Service Provider</code> endpoint domain account used to
   ** authenticate a c onnection.
   */
  private Principal                 domain;

  private final Map<String, String> latch = new LinkedHashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ///////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  public enum Type {
      /** the type name of a WebLogic Managed Server */
      WLS(
        SERVER_TYPE_WEBLOGIC
      , SYSTEM_PROPERTY_WEBLOGIC
      , "weblogic.jndi.WLInitialContextFactory"
      , PROTOCOL_WEBLOGIC_DEFAULT
      , PROTOCOL_WEBLOGIC_SECURE
      ),

    /** the type name of a WebSphere Application Server */
    WAS(
        SERVER_TYPE_WEBSPHERE
      , SYSTEM_PROPERTY_WEBSPHERE
      , "com.ibm.websphere.naming.WsnInitialContextFactory"
      , PROTOCOL_WEBSPHERE_DEFAULT
      , PROTOCOL_WEBSPHERE_SECURE
      )
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String value;
    public final String property;
    public final String factory;
    public final String unsecure;
    public final String secure;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ServerType</code> that allows use as a JavaBean.
     **
     ** @param  property         the name of the system property value to
     **                          identify the server type.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  factory          the full qualified class name of the JNDI
     **                          context factory.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  unsecure         the protocol prefix for unsecured transmission.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  secure           the protocol prefix for secured transmission.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Type(final String value, final String property, final String factory, final String unsecure, final String secure) {
      // initialize instance attributes
      this.value    = value;
      this.property = property;
      this.factory  = factory;
      this.unsecure = unsecure;
      this.secure   = secure;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper server type from the given string
     ** value.
     **
     ** @param  value            the string value the server type should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the server type.
     **                          <br>
     **                          Possible object is <code>Type</code>.
     */
    public static Type from(final String value) {
      for (Type cursor : Type.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this <code>ServerEndpoint</code>.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  private ServerEndpoint(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerEndpoint</code> which is associated with the
   ** specified {@link Loggable} for logging purpose.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerEndpointResource</code>
   **                            configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  primary            the primary server properties on which the
   **                            Service Provider is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
   ** @param  name               the name of the Managed Server the connection
   **                            will established to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the type of the Managed Server where this IT
   **                            Resource is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rootContext        the name of application context.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  loginConfig        the location of JAAS configuration for the
   **                            Managed Server where this IT Resource is
   **                            configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domainPrincipal    the name of the domain principal this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domainPassword     the password of the domain system account
   **                            specified by the <code>domainPrincipal</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the name corresponding to the administrator
   **                            with System Adminsitrator permissions this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  language           the language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            the country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private ServerEndpoint(final Loggable loggable, final Server primary, final String name, final Type type, final String rootContext, final boolean secureSocket, final String loginConfig, final Principal domain, final Principal principal, final String language, final String country, final String timeZone) {
    // ensure inheritance
    super(loggable, primary, rootContext, principal, secureSocket, language, country, timeZone);

    // initialize instance attributes
    this.serverName  = name;
    this.loginConfig = loginConfig;
    this.domain      = domain;
    this.type        = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Call by the ICF framework to inject the argument for parameter
   ** <code>serverName</code>.
   **
   ** @param  value              the value for the attribute <code>name</code>
   **                            used as the database provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void serverName(final String value) {
    this.serverName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the name of the Manager Server used to connect to.
   **
   ** @return                    the name of the Manager Server used to connect
   **                            to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverName() {
    return this.serverName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverType
  /**
   ** Call by the ICF framework to inject the argument for parameter
   ** <code>type</code>.
   **
   ** @param  value              the value for the attribute <code>type</code>
   **                            used as the database provider.
   **                            <br>
   **                            Allowed object is {@link Type}.
   */
  public final void serverType(final Type value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverType
  /**
   ** Returns the type of the Manager Server used to connect to.
   **
   ** @return                    the type of the Manager Server used to
   **                            connect to.
   **                            <br>
   **                            Possible object is {@link Type}.
   */
  public final Type serverType() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginConfig
  /**
   ** Call by the ICF framework to inject the login configuration of the
   ** Managed Server used to connect to.
   **
   ** @param  value              the security configuration of the Managed
   **                            Server used to connect to.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void loginConfig(final String value) {
    this.loginConfig = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginConfig
  /**
   ** Returns the login configuration of the Managed Server used to connect
   ** to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #LOGIN_CONFIG}.
   **
   ** @return                    the security configuration of the Managed
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String loginConfig() {
    return this.loginConfig;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPrincipal
  /**
   ** Call by the ICF framework to inject the platform security principal of the
   ** Managed Server used to connect to.
   **
   ** @param  value              the value for the platform security principal
   **                            of the Managed Server used to connect to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServerEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final ServerEndpoint domainPrincipal(final String value) {
    // prevent bogus state
    if (this.domain == null)
      this.domain = new Principal();

    this.domain.username(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPrincipal
  /**
   ** Returns the name of the platform security principal of the Managed Server
   ** used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #DOMAIN_PRINCIPAL}.
   **
   ** @return                    the name of the security principal Service
   **                            Provider platform used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String domainPrincipal() {
    // prevent bogus state
    return this.domain == null ? null : this.domain.username();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPassword
  /**
   ** Call by the ICF framework to inject the domain administrator principal of
   ** the Managed Server used to connect to.
   **
   ** @param  value              the credential of the domain administrator
   **                            principal Service Provider platform used to
   **                            connect to.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   **
   ** @return                    the <code>ServerEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final ServerEndpoint domainPassword(final GuardedString value) {
    // prevent bogus state
    if (this.domain == null)
      this.domain = new Principal();

    this.domain.password(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPassword
  /**
   ** Returns the password of the domain administrator principal of the Managed
   ** Server used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #DOMAIN_PASSWORD}.
   **
   ** @return                    the credential of the domain administrator
   **                            principal Service Provider platform used to
   **                            connect to.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString domainPassword() {
    // prevent bogus state
    return this.domain == null ? null : this.domain.password();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextFactory
  /**
   ** Returns the class name of the initial context factory.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #INITIAL_CONTEXT_FACTORY}.
   **
   ** @return                    the class name of the initial context factory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextFactory() {
    return this.type.factory;
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
  public final String serviceURL() {
    // create a URL string from the parts describe by protocol, host and port
    return String.format("%s://%s:%d/%s", this.secureSocket() ? PROTOCOL_WEBLOGIC_SECURE : PROTOCOL_WEBLOGIC_DEFAULT, this.primary.host(), this.primary.port(), this.rootContext);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerEndpoint</code> without a
   ** {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @return                    an newly created instance of
   **                            <code>ServerEndpoint</code> providing default
   **                            values only.
   **                            <br>
   **                            Possible object <code>ServerEndpoint</code>.
   */
  public static ServerEndpoint build() {
    return build(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverHost         the host name or IP address of the target
   **                            system on which the database is deployed.
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the database is listening on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverName         the name of the Managed Server the connection
   **                            will established to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverType         the type of the Managed Server where this IT
   **                            Resource is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rootContext        the name of application context.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  loginConfig        the location of JAAS configuration for the
   **                            Managed Server where this IT Resource is
   **                            configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domainPrincipal    the name of the domain principal this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domainPassword     the password of the domain system account
   **                            specified by the <code>domainPrincipal</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  principalName      the name corresponding to the administrator
   **                            with System Adminsitrator permissions this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  language           the language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            the country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>ServiceEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object <code>ServiceEndpoint</code>.
   **
   ** @throws ServerException    if the <code>serverType</code> specified isn't
   **                            supported.
   */
  public static ServerEndpoint build(final Loggable loggable, final String serverHost, final int serverPort, final String serverName, final String serverType, final String rootContext, final boolean secureSocket, final String loginConfig, final String domainPrincipal, final GuardedString domainPassword, final String principalName, final GuardedString principalPassword, final String language, final String country, final String timeZone)
    throws ServerException {

    try {
      // ensure inheritance
      return build(loggable, new Server(serverHost, serverPort), serverName, Type.from(serverType), rootContext, secureSocket, loginConfig, new Principal(domainPrincipal, domainPassword), new Principal(principalName, principalPassword), language, country, timeZone);
    }
    catch (IllegalArgumentException e) {
      throw ServerException.serverTypeUnsupported(serverType);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiates the {@link ServerEndpoint}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an newly created instance of
   **                            <code>ServerEndpoint</code> providing default
   **                            values only.
   **                            <br>
   **                            Possible object <code>ServerEndpoint</code>.
   */
  public static ServerEndpoint build(final Loggable loggable) {
    return new ServerEndpoint(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of this
   **                            <code>IT Resource</code> configuration where
   **                            this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @return                    an newly created instance of
   **                            <code>ServerEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object <code>ServerEndpoint</code>.
   */
  public static final ServerEndpoint build(final Loggable loggable, final Map<String, String> parameter) {
    // ensure inheritance
    return build(
      loggable
    , Server.of(parameter.get(SERVER_HOST), Integer.parseInt(parameter.get(SERVER_PORT)))
    , parameter.get(SERVER_NAME)
    , Type.from(parameter.get(SERVER_TYPE))
    , parameter.get(ROOT_CONTEXT)
    , Boolean.valueOf(parameter.get(SECURE_SOCKET))
    , parameter.get(LOGIN_CONFIG)
    , Principal.of(parameter.get(DOMAIN_PRINCIPAL), parameter.get(DOMAIN_PASSWORD))
    , Principal.of(parameter.get(PRINCIPAL_NAME),   parameter.get(PRINCIPAL_PASSWORD))
    , parameter.get(LOCALE_LANGUAGE)
    , parameter.get(LOCALE_COUNTRY)
    , parameter.get(LOCALE_TIMEZONE)
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  primary            the primary server properties on which the
   **                            Service Provider is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
   ** @param  name               the name of the Managed Server the connection
   **                            will established to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the type of the Identity Governance Managed
   **                            Server where this IT Resource will be working
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  loginConfig        the location of JAAS configuration for the
   **                            Identity Governance Managed Server where this
   **                            IT Resource will be working on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domain             the domain security principal properties user
   **                            to authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  principal          the security principal properties user to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  language           the language code of the Service Provider.
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            the country code of the Service Provider.
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>ServerEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object <code>ServerEndpoint</code>.
   */
  public static final ServerEndpoint build(final Loggable loggable, final Server primary, final String name, final Type type, final String rootContext, final boolean secureSocket, final String loginConfig, final Principal domain, final Principal principal, final String language, final String country, final String timeZone) {
    // ensure inheritance
    return new ServerEndpoint(loggable, primary, name, type, rootContext, secureSocket, loginConfig, domain, principal, language, country, timeZone);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   saveSystemProperties
  /**
   ** Manage system properties that needs to exists et the time a connection
   ** is established to the specific server type.
   */
  public void saveSystemProperties() {
    this.latch.put(JAVA_SECURITY_CONFIG, System.getProperty(this.type.property));
    this.latch.put(this.type.property,   System.getProperty(this.type.property));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   restoreSystemProperties
  /**
   ** Restore system properties that might be overriden by
   ** {@link #saveSystemProperties()} et the time a connection was established
   ** to the specific server type.
   */
  public void restoreSystemProperties() {
    // reset the system properties to the origin
    for (Map.Entry<String, String> property : this.latch.entrySet()) {
      final String name  = property.getKey();
      final String value = property.getValue();
      if (value == null)
        System.getProperties().remove(name);
      else
        System.setProperty(name, value);
    }
  }
}