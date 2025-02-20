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

    Copyright 2022 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Identity Services Integration

    File        :   ServiceResource.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceResource.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.rest;

import java.util.Map;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractResource;
import oracle.iam.identity.foundation.ITResourceAttribute;

////////////////////////////////////////////////////////////////////////////////
// class ServiceResource
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>ServiceResource</code> implements the base functionality of a
 ** Service <code>IT Resource</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceResource extends AbstractResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the protocol each Web Service is using */
  public static final String SCHEMA_DEFAULT             = "http";

  /** the protocol each Web Service is using over SSL */
  public static final String SCHEMA_DEFAULT_SECURE      = "https";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the content media type send at any request except
   ** authentication requests to the Service Provider.
   */
  public static final String TYPE_CONTENT              = "Content Type";
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the content media type accepted by the client at any request
   ** except authentication requests.
   */
  public static final String TYPE_ACCEPT               = "Accept Type";
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the authentication scheme.
   ** <br>
   ** Possible values are:
   ** <ul>
   **   <li>none
   **   <li>basic
   **   <li>password
   **   <li>credential
   ** </ul>
   */
  public static final String AUTHENTICATION_SCHEME     = "Authentication Scheme";
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the authorization server to authenticate the security
   ** principal at the Service Provider.
   ** <p>
   ** This attribute will be mandatory if the authentication scheme is
   ** Endpoint#Authentication#password or
   ** Endpoint#Authentication#CREDENTIAL.
   */
  public static final String AUTHORIZATION_SERVER      = "Authorization Server";
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the identifier of the client to authenticate the connector at
   ** the Service Provider.
   ** <p>
   ** This attribute will be mandatory if the authentication scheme is
   ** Endpoint#Authentication#password or
   ** Endpoint#Authentication#CREDENTIAL.
   */
  public static final String CLIENT_IDENTIFIER         = "Client Identifier";
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the identifier of the client to authenticate the connector at
   ** the Service Provider.
   ** <p>
   ** This attribute willl be mandatory if the authentication scheme is
   ** Authentication#Scheme#ClientCredential or
   ** Authentication#Scheme#Password.
   */
  public static final String CLIENT_SECRET             = "Client Secret";
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the identifier of the resource owner to authenticate the
   ** connector at the Service Provider.
   ** <p>
   ** This attribute willl be mandatory if the authentication scheme is
   ** Endpoint#Authentication#PASSWORD.
   */
  public static final String RESOURCE_OWNER            = "Resource Owner";
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the credential of the resource owner to authenticate the
   ** connector at the Service Provider.
   ** <p>
   ** This attribute will be mandatory if the authentication scheme is
   ** Endpoint#Authentication#PASSWORD.
   */
  public static final String RESOURCE_CREDENTIAL       = "Resource Credential";

  /** the array with the attributes for the <code>IT Resource</code> */
  private static final ITResourceAttribute[] attribute = {
    ITResourceAttribute.build(SERVER_NAME,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PORT,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(ROOT_CONTEXT,          ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(TYPE_CONTENT,          ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(TYPE_ACCEPT,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURE_SOCKET,         ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(AUTHENTICATION_SCHEME, ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(CLIENT_IDENTIFIER,     ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(CLIENT_SECRET,         ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(PRINCIPAL_NAME,        ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(PRINCIPAL_PASSWORD,    ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(RESOURCE_OWNER,        ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(RESOURCE_CREDENTIAL,   ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(AUTHORIZATION_SERVER,  ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_LANGUAGE,       ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_COUNTRY,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_TIMEZONE,       ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_FEATURE,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(CONNECTION_TIMEOUT,    CONNECTION_TIMEOUT_DEFAULT)
  , ITResourceAttribute.build(RESPONSE_TIMEOUT,      RESPONSE_TIMEOUT_DEFAULT)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Authentication
  // ~~~~ ~~~~~~~~~~~~~~
  /**
   ** An authentication scheme specifies how a user is to be challenged for a
   ** set of credentials, name-value string pairs (for example userid and
   ** password) that are used to authenticate a user.
   ** <ul>
   **   <li>NONE         -
   **   <li>PREEMTIVE    -
   **   <li>NONPREEMTIVE -
   **   <li>TOKEN        -
   **   <li>CLIENT       - The client can request an access token using only its
   **                      client credentials (or other supported means of
   **                      authentication) when the client is requesting access
   **                      to the protected resources under its control, or
   **                      those of another resource owner that have been
   **                      previously arranged with the authorization server
   **                      (the method of which is beyond the scope of this
   **                      specification).
   **   <li>PASSWORD     - The resource owner password credentials grant type is
   **                      suitable in cases where the resource owner has a
   **                      trust relationship with the client, such as the
   **                      device operating system or a highly privileged
   **                      application. The authorization server should take
   **                      special care when enabling this grant type and only
   **                      allow it when other flows are not viable.
   ** </ul>
   */
  public enum Authentication {
      NONE("none"),

    /**
     ** The most simple way to deal with authentication is to use HTTP basic
     ** authentication. We use a special HTTP header where we add
     ** 'username:password' encoded in base64.
     ** <b>Note</b>:
     ** <br>
     ** Even though the credentials are encoded, they are not encrypted! It is
     ** very easy to retrieve the username and password from a basic
     ** authentication.
     ** <br>
     ** Do not use this authentication scheme on plain HTTP, but only through
     ** SSL/TLS.
     ** <p>
     ** Preemptive authentication way i.e. information is send always with
     ** each HTTP request.
     */
    PREEMTIVE("basic-preemptive"),

    /**
     ** non-preemptive authentication way i.e. auth information is added only
     ** when server refuses the request with 401 status code and then the
     ** request is repeated with authentication information.
     */
    NONPREEMTIVE("basic-non-preemptive"),

    /**
     ** The Json Web Token (JWT) Profile RFC7523 implements the RFC7521
     ** abstract assertion protocol. It aims to extend the OAuth2 protocol to
     ** use JWT as an additional authorization grant.
     */
    TOKEN("token"),

    /**
     ** The OAuth2 resource owner password credentials grant type is suitable
     ** in cases where the resource owner has a trust relationship with the
     ** client, such as the device operating system or a highly privileged
     ** application. The authorization server should take special care when
     ** enabling this grant type and only allow it when other flows are not
     ** viable.
     */
    PASSWORD("oauth-password"),

    /**
     ** The OAuth2 client can request an access token using only its client
     ** credentials (or other supported means of authentication) when the
     ** client is requesting access to the protected resources under its
     ** control, or those of another resource owner that have been previously
     ** arranged with the authorization server (the method of which is beyond
     ** the scope of this specification).
     */
    CREDENTIAL("oauth-credential")
    ;

    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the tag name of the authentication scheme. */
    public final String tag;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Authentication</code> with a tag name.
     **
     ** @param  tag              the logical name of this authentication scheme
     **                          to lookup.
     */
    Authentication(final String tag) {
      this.tag = tag;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>AuthenticationScheme</code> from
     ** the given string value.
     **
     ** @param  value              the string value the authentication scheme
     **                            should be returned for.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>AuthenticationScheme</code>.
     **                            <br>
     **                            Possible object
     **                            <code>AuthenticationScheme</code>.
     */
    public static Authentication from(final String value) {
      for (Authentication cursor : Authentication.values()) {
        if (cursor.tag.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable} for logging purpose.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public ServiceResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance key.
   ** <br>
   ** The <code>IT Resource</code> will not be populated from the repository of
   ** Identity Manager. Only instance key and instance name are obtained.
   ** <p>
   ** Usual an instance of this wrapper will be created in this manner if
   ** the Connection Pool is used to aquire a connection
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries.
   */
  private ServiceResource(final Loggable loggable, final Long instance)
    throws TaskException {

    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance name.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of
   ** Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the public identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries or one or
   **                            more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private ServiceResource(final Loggable loggable, final String instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverName         the host name or IP address of the target
   **                            system on which the Service Provider is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the Service Provider is listening on.
   **                            Allowed object is {@link String}.
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
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  typeContent        the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  typeAccept         the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authentication     the authentication scheme to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authorization      the <code>Authorization Server</code> attribute
   **                            exposed by the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientIdentifier   the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientSecret       the credential of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the name of the Service Provider this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourceOwner      the resource owner account to authenticate at
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourceCredential the password of the resource owner account
   **                            specified by the <code>resourceUsername</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeTimeZone     use this parameter to specify the time zone of
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
   ** @param  feature            the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private ServiceResource(final Loggable loggable, final String serverName, final String serverPort, final String rootContext, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final String authorization, final String clientIdentifier, final String clientSecret, final String principalName, final String principalPassword, final String resourceOwner, final String resourceCredential, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    attribute(SERVER_NAME,           serverName);
    attribute(SERVER_PORT,           serverPort);
    attribute(ROOT_CONTEXT,          rootContext);
    attribute(SECURE_SOCKET,         secureSocket  ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(TYPE_CONTENT,          typeContent);
    attribute(TYPE_ACCEPT,           typeAccept);
    attribute(AUTHENTICATION_SCHEME, authentication);
    attribute(AUTHORIZATION_SERVER,  authorization);
    attribute(CLIENT_IDENTIFIER,     clientIdentifier);
    attribute(CLIENT_SECRET,         clientSecret);
    attribute(PRINCIPAL_NAME,        principalName);
    attribute(PRINCIPAL_PASSWORD,    principalPassword);
    attribute(RESOURCE_OWNER,        resourceOwner);
    attribute(RESOURCE_CREDENTIAL,   resourceCredential);
    attribute(LOCALE_LANGUAGE,       localeLanguage);
    attribute(LOCALE_COUNTRY,        localeCountry);
    attribute(LOCALE_TIMEZONE,       localeTimeZone);
    attribute(SERVER_FEATURE,        feature);

    validateAttributes(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeContent
  /**
   ** Returns authentication schema to authenticate security principal for the
   ** Service Provider.
   **
   ** @return                    the schema used to authenticate security
   **                            principal for the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String typeContent() {
    return stringValue(TYPE_CONTENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeAccept
  /**
   ** Returns authentication schema to authenticate security principal for the
   ** Service Provider.
   **
   ** @return                    the schema used to authenticate security
   **                            principal for the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String typeAccept() {
    return stringValue(TYPE_ACCEPT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticationScheme
  /**
   ** Returns authentication schema to authenticate security principal for the
   ** Service Provider.
   **
   ** @return                    the schema used to authenticate security
   **                            principal for the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String authenticationScheme() {
    return stringValue(AUTHENTICATION_SCHEME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizationServer
  /**
   ** Returns the authorization server to authenticate the security principal at
   ** the Service Provider.
   **
   ** @return                    the authorization server to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String authorizationServer() {
    return stringValue(AUTHORIZATION_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientIdentifier
  /**
   ** Returns client identifier to authenticate the service client at the
   **Service Provider.
   **
   ** @return                    token to authenticate the service client at the
   **                            Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String clientIdentifier() {
    return stringValue(CLIENT_IDENTIFIER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientSecret
  /**
   ** Returns credential to authenticate the service client at the Service
   ** Provider.
   **
   ** @return                    token to authenticate the service client at the
   **                            Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String clientSecret() {
    return stringValue(CLIENT_SECRET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceOwner
  /**
   ** Returns the <code>name</code> of the <code>Resource Owner</code>
   ** attribute for the Service Provider endpoint.
   **
   ** @return                    the <code>username</code> of the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String resourceOwner() {
    return stringValue(RESOURCE_OWNER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceCredential
  /**
   ** Returns the <code>password</code> attribute for the
   ** <code>Resource Owner</code> attribute for the Service Provider endpoint.
   **
   ** @return                    the <code>password</code> attribute for the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String resourceCredential() {
    return stringValue(RESOURCE_CREDENTIAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Web Service.
   ** <p>
   ** The URL consists of the server part of the HTTP url, http(s)://host:port
   ** and the absolute path to the resource. The entry is post fixed with the
   ** context root of the connection and the specified <code>contextPath</code>.
   ** <p>
   ** This version of retrieving the context URl returns always the context root
   ** configured in the <code>IT Resource</code> definition associated with this
   ** instance.
   **
   ** @param  contextPath        a component to a Web Resource path.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the context URl the context root configured in
   **                            the <code>IT Resource</code> definition
   **                            associated with this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String contextURL(final String contextPath) {
    // lazy initialization of the configured service URL's
    final StringBuilder context = new StringBuilder(contextURL());
    if (this.rootContext() != null) {
      if (context.charAt(context.length() - 1) != '/' && contextPath.charAt(0) != '/')
        context.append('/');
      context.append(contextPath);
    }
    return context.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Web Service.
   ** <p>
   ** The URL consists of the server part of the HTTP url, http(s)://host:port
   ** and the absolute path to the resource. The entry is post fixed with the
   ** context root of the connection.
   ** <p>
   ** This version of retrieving the context URl returns always the context root
   ** configured in the <code>IT Resource</code> definition associated with this
   ** instance.
   **
   ** @return                    the context URl the context root configured in
   **                            the <code>IT Resource</code> definition
   **                            associated with this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String contextURL() {
    // lazy initialization of the configured service URL's
    final StringBuilder context = new StringBuilder(serviceURL());
    if (this.rootContext() != null) {
      if (context.charAt(context.length() - 1) != '/' && this.rootContext().charAt(0) != '/')
        context.append('/');
      context.append(this.rootContext());
    }

    return context.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server parts of the HTTP url, http(s)://host:port
   **
   ** @return                    the server part of the HTTP url,
   **                            http(s)://host:port
   */
  public String serviceURL() {
    // lazy initialization of the configured service URL's
    return serviceURL(this.secureSocket() ? SCHEMA_DEFAULT_SECURE : SCHEMA_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server part of an url, <em>protocol</em>://host:port.
   **
   ** @param  protocol           the protocol of the url to build.
   **
   ** @return                    the server part of the an url,
   **                            <em>protocol</em>://host:port
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String serviceURL(final String protocol) {
    final StringBuilder service = new StringBuilder(protocol);
    service.append("://");
    if (this.serverName() != null)
      service.append(this.serverName());
    if (this.serverPort() != -1)
      service.append(':').append(this.serverPort());

    return service.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified task and belongs to the IT Resource
   ** specified by the given identifier.
   ** <p>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> key.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **                            <br>
   **                            Possible object is {@link ServiceResource}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries for the
   **                            given key or one or more attributes are missing
   **                            on the <code>IT Resource</code> instance.
   */
  public static ServiceResource build(final Loggable loggable, final Long instance)
    throws TaskException {

    return new ServiceResource(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified task and belongs to the IT Resource
   ** specified by the given name.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> name.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the name of the IT Resource instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **                            <br>
   **                            Possible object is {@link ServiceResource}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries for the
   **                            given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  public static ServiceResource build(final Loggable loggable, final String instance)
    throws TaskException {

    return new ServiceResource(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given parameter.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverName         the host name or IP address of the target
   **                            system on which the Service Provider is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the Service Provider is listening on.
   **                            <br>
   **                            Allowed object is {@link String}.
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
   ** @param  typeContent        the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  typeAccept         the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authentication     the authentication scheme to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authorization      the <code>Authorization Server</code> attribute
   **                            exposed by the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientIdentifier   the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientSecret       the credential of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the target system account to authenticate at
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourceUsername   the resource owner account to authenticate at
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourcePassword   the password of the resource owner account
   **                            specified by the <code>resourceUsername</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeTimeZone     use this parameter to specify the time zone of
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
   ** @param  feature            the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **                            <br>
   **                            Possible object is {@link ServiceResource}.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static ServiceResource build(final Loggable loggable, final String serverName, final String serverPort, final String rootContext, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final String authorization, final String clientIdentifier, final String clientSecret, final String principalName, final String principalPassword, final String resourceUsername, final String resourcePassword, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    return new ServiceResource(loggable, serverName, serverPort, rootContext, secureSocket, typeContent, typeAccept, authentication, authorization, clientIdentifier, clientSecret, principalName, principalPassword, resourceUsername, resourcePassword, localeCountry, localeLanguage, localeTimeZone, feature);
  }
}