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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic REST Library

    File        :   ServiceEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceEndpoint.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest;

import java.net.URI;

import java.util.Map;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.icf.foundation.SystemConstant;
import oracle.iam.identity.icf.foundation.AbstractEndpoint;

import oracle.iam.identity.icf.foundation.logging.Loggable;

///////////////////////////////////////////////////////////////////////////////
// class ServiceEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>ServiceEndpoint</code> wraps the REST/SCIM Service endpoint
 ** configuration.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the endpoints
 **                              extending this class (endpoints can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceEndpoint<T extends ServiceEndpoint> extends AbstractEndpoint<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The protocol each Web Service is using */
  public static final String SCHEMA_DEFAULT        = "http";

  /** The protocol each Web Service is using over SSL */
  public static final String SCHEMA_DEFAULT_SECURE = "https";
  /**
   ** At client side, an incoming response may have an entity attached to it.
   ** To determine it's type, server uses the HTTP response header Accept-Type.
   ** Some common examples of accept types are "text/plain", "application/xml",
   ** "text/html", "application/json", "image/gif", and "image/jpeg".
   */
  public static final String  TYPE_ACCEPT          = "accept-type";
  /**
   ** At server side, an incoming request may have an entity attached to it.
   ** To determine it's type, server uses the HTTP request header Content-Type.
   ** Some common examples of content types are "text/plain", "application/xml",
   ** "text/html", "application/json", "image/gif", and "image/jpeg".
   */
  public static final String  TYPE_CONTENT         = "content-type";
  /**
   ** The authentication scheme to authenticate security principal for the
   ** Service Provider.
   */
  public static final String  AUTHN_TYPE           = "authentication-type";
  /**
   ** The authorization server exposed by the Service Provider endpoint for
   ** authentication purpose.
   */
  public static final String  AUTHN_URI            = "authentication-uri";
  /**
   ** The identifier of the <code>Client</code> to authenticate a connection.
   */
  public static final String  CLIENT_ID            = "client-id";
  /**
   ** The credentials of the <code>Client</code> to authenticate a connection.
   */
  public static final String  CLIENT_PASSWORD      = "client-password";
  /**
   ** The identifier of the <code>Resource Owner</code> to authenticate a
   ** connection.
   */
  public static final String  RESOURCE_OWNER       = "resource-owner";
  /**
   ** The credentials of the <code>Client</code> to authenticate a connection.
   */
  public static final String  RESOURCE_PASSWORD    = "resource-password";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** At server side, an incoming request may have an entity attached to it.
   ** To determine it's type, server uses the HTTP request header Content-Type.
   ** Some common examples of content types are "text/plain", "application/xml",
   ** "text/html", "application/json", "image/gif", and "image/jpeg".
   */
  private String         typeContent            = null;
  private String         typeAccept             = null;
  /**
   ** The authentication scheme to authenticate security principal for the
   ** Service Provider.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This is declared as a String to avoid unpredictable exceptions occurs if
   ** the enumeration type will instantiated instead.
   ** <br>
   ** The validation of the correct value is delayed until the first time the
   ** value is used in order to throw a proper exception back to the callee if
   ** the constraint is violated.
   */
  private String         authentication         = null;
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the client id and credentials of the <code>Client</code> to
   ** authenticate a connection.
   */
  private Principal      client                 = null;
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the user name and credentials of the Service Provider endpoint
   ** account used to authenticate a connection.
   */
  private Principal      resource               = null;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the host name and port for the <code>Proxy Server</code>
   ** frontending the Service Provider endpoint.
   */
  protected Server       proxy                  = null;
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the user name and password credentials of the Proxy Server
   ** endpoint account used to authenticate a connection.
   */
  private Principal      proxyPrincipal         = null;
  /**
   ** The authorization server exposed by the Service Provider endpoint for
   ** authentication purpose.
   */
  private URI            authorizationURI       = null;
  /**
   ** Whether the schema is fetched from the Service Provider or statically
   ** defined.
   ** <br>
   ** If its <code>true</code> the schema is fetched from the server; if
   ** <code>false</code> is configured the connector provides a statically
   ** defined schema.
   */
  private boolean        fetchSchema            = false;
  /**
   ** Either the checking of the HHTTP methods is carried out in accordance with
   ** the rules of RFC-9110 or is skipped.
   */
  private boolean        rfc9110                = true;
  /**
   ** Whether the Service Provider is licensed to use enhanced features.
   */
  private boolean        enterpriseFeature      = false;

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
   ** Constructs a <code>ServiceEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  protected ServiceEndpoint(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  server             the server properties on which the Service
   **                            Provider is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
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
   ** @param  client             the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  principal          the security principal properties used to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  resource           the resource principal properties used to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  authorizationURI   the {@link URI} to
   **                            <code>Authorization Server</code> exposed by
   **                            the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Allowed object is {@link URI}.
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
   */
  protected ServiceEndpoint(final Loggable loggable, final Server server, final String rootContext, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final Principal client, final Principal principal, final Principal resource, final URI authorizationURI, final String language, final String country, final String timeZone) {
    // ensure inheritance
    super(loggable, server, rootContext, principal, secureSocket, language, country, timeZone);

    // initialize instance attributes
    this.typeContent      = typeContent;
    this.typeAccept       = typeAccept;
    this.authentication   = authentication;
    this.client           = client;
    this.resource         = resource;
    this.authorizationURI = authorizationURI;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeContent
  /**
   ** Set the content media type send to the server at any request except
   ** authentication requests to the Service Provider.
   **
   ** @param  value              the content media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T typeContent(final String value) {
    this.typeContent= value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeContent
  /**
   ** Returns the content media type send to the server at any request except
   ** authentication requests to the Service Provider.
   **
   ** @return                    the content media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String typeContent() {
    return this.typeContent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeAccept
  /**
   ** Set the accepted content media type send to the server at any request
   ** except authentication requests to the Service Provider.
   **
   ** @param  value              the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T typeAccept(final String value) {
    this.typeAccept= value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeAccept
  /**
   ** Returns the accepted type send to the server at any request except
   ** authentication requests to the Service Provider.
   **
   ** @return                    the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String typeAccept() {
    return this.typeAccept;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticationScheme
  /**
   ** Set the authentication scheme to authenticate the security principal at
   ** the Service Provider.
   **
   ** @param  value              the authentication scheme to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T authenticationScheme(final String value) {
    this.authentication= value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticationScheme
  /**
   ** Returns the authentication scheme to authenticate security principal at
   ** the Service Provider.
   **
   ** @return                    the authentication scheme to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String authenticationScheme() {
    return this.authentication;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Set the identifier of the client to authenticate the connector at the
   ** Service Provider.
   **
   ** @param  value              the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T client(final Principal value) {
    this.client = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Returns the identifier of the client to authenticate the connector at the
   ** Service Provider.
   **
   ** @return                    the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Principal}.
   */
  public final Principal client() {
    return this.client;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   clientIdentifier
  /**
   ** Set the identifier of the client to authenticate the connector at the
   ** Service Provider.
   **
   ** @param  value              the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T clientIdentifier(final String value) {
    // prevent bogus state
    if (this.client == null)
      this.client = new Principal();

    this.client.username(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientIdentifier
  /**
   ** Returns the <code>id</code> of the <code>Client</code> attribute for the
   ** Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#username}.
   **
   ** @return                    the <code>id</code> of the <code>Client</code>
   **                            attribute for the Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String clientIdentifier() {
    // prevent bogus state
    return this.client == null ? null : this.client.username();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientSecret
  /**
   ** Sets the <code>credential</code> attribute for the <code>Client</code>
   ** attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#password}.
   **
   ** @param  value              the <code>credential</code> attribute for the
   **                            <code>Client</code> attribute for the Service
   **                            Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T clientSecret(final GuardedString value) {
    // prevent bogus state
    if (this.client == null)
      this.client = new Principal();

    this.client.password(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientSecret
  /**
   ** Returns the <code>credential</code> attribute for the
   ** <code>Resource Owner</code> attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#password}.
   **
   ** @return                    the <code>credential</code> attribute for the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString clientSecret() {
    return this.client == null ? null : this.client.password();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Sets the <code>resource</code> attribute for the Service Provider
   ** endpoint.
   **
   ** @param  value              the <code>resource</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T resource(final Principal value) {
    this.resource = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the <code>resource</code> attribute for the Service Provider
   ** endpoint.
   **
   ** @return                    the <code>resource</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link Principal}.
   */
  public final Principal resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceOwner
  /**
   ** Sets the <code>name</code> of the <code>Resource Owner</code>
   ** attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#username}.
   **
   ** @param  value              the <code>name</code> of the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T resourceOwner(final String value) {
    // prevent bogus state
    if (this.resource == null)
      this.resource = new Principal();

    this.resource.username(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceOwner
  /**
   ** Returns the <code>username</code> of the <code>Resource Owner</code>
   ** attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#username}.
   **
   ** @return                    the <code>username</code> of the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String resourceOwner() {
    // prevent bogus state
    return this.resource == null ? null : this.resource.username();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceCredential
  /**
   ** Sets the <code>credential</code> attribute for the
   ** <code>Resource Owner</code> attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#password}.
   **
   ** @param  value              the <code>credential</code> attribute for the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T resourceCredential(final GuardedString value) {
    // prevent bogus state
    if (this.resource == null)
      this.resource = new Principal();

    this.resource.password(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceCredential
  /**
   ** Returns the <code>credential</code> attribute for the
   ** <code>Resource Owner</code> attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#password}.
   **
   ** @return                    the <code>credential</code> attribute for the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString resourceCredential() {
    return this.resource == null ? null : this.resource.password();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizationURI
  /**
   ** Sets the {@link URI} to the <code>Authorization Server</code> exposed by
   ** the Service Provider endpoint for authentication purpose.
   **
   ** @param  value              the {@link URI} to the
   **                            <code>Authorization Server</code> exposed by
   **                            the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T authorizationURI(final URI value) {
    this.authorizationURI = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizationURI
  /**
   ** Returns the {@link URI} to the <code>Authorization Server</code> exposed
   ** by the Service Provider endpoint for authentication purpose.
   **
   ** @return                    the {@link URI} to the
   **                            <code>Authorization Server</code> exposed by
   **                            the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public URI authorizationURI() {
    return this.authorizationURI;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxy
  /**
   ** Sets the <code>proxy</code> attribute for the proxy server in front of a
   ** Service Provider endpoint.
   **
   ** @param  value              the <code>proxy</code> attribute for the proxy
   **                            server in front of a
   **                            Service Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link Server}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T proxy(final Server value) {
    this.proxy = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxy
  /**
   ** Returns the <code>proxy</code> attribute for the proxy server in front of
   ** a Service Provider endpoint.
   **
   ** @return                    the <code>proxy</code> attribute for the proxy
   **                            server in front of a Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link Server}.
   */
  public final Server proxy() {
    return this.proxy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyHost
  /**
   ** Sets the <code>proxyHost</code> attribute for the Service Provider
   ** endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Server#host()} of the proxy server.
   **
   ** @param  value              the <code>proxyHost</code> attribute for the
   **                            proxy server in front of a Service Provider
   **                            endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T proxyHost(final String value) {
    // prevent bogus state
    if (this.proxy == null)
      this.proxy = new Server();

    this.proxy.host(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyHost
  /**
   ** Returns the <code>primaryHost</code> attribute for the Service Provider
   ** endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Server#host} of the primary server.
   **
   ** @return                    the <code>proxyHost</code> attribute for the
   **                            proxy server in front of a Service Provider
   **                            endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String proxyHost() {
    return this.proxy.host();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyPort
  /**
   ** Sets the <code>primaryPort</code> attribute for the Service Provider
   ** endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Server#host} of the primary server.
   **
   ** @param  value              the <code>proxyPort</code> attribute for the
   **                            proxy server in front of a Service Provider
   **                            endpoint.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T proxyPort(final int value) {
    // prevent bogus state
    if (this.proxy == null)
      this.proxy = new Server();

    this.proxy.port(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryPort
  /**
   ** Returns the <code>proxyPort</code> attribute for the Service Provider
   ** endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Server#port} of the primary server.
   **
   ** @return                    the <code>proxyPort</code> attribute for the
   **                            proxy server in front of a Service Provider
   **                            endpoint.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int proxyPort() {
    return this.proxy.port();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyPrincipal
  /**
   ** Sets the <code>proxyPrincipal</code> attribute for theService Provider
   ** endpoint.
   **
   ** @param  value              the <code>proxyPrincipal</code> attribute for
   **                            the Service Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T proxyPrincipal(final Principal value) {
    this.proxyPrincipal = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyPrincipal
  /**
   ** Returns the <code>proxyPrincipal</code> attribute for the Service Provider
   ** endpoint.
   **
   ** @return                    the <code>proxyPrincipal</code> attribute for
   **                            the Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link Principal}.
   */
  public final Principal proxyPrincipal() {
    return this.proxyPrincipal;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyUsername
  /**
   ** Sets the <code>username</code> of the <code>Proxy Server</code>
   ** attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#username}.
   **
   ** @param  value              the <code>username</code> of the
   **                            <code>Proxy Server</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T proxyUsername(final String value) {
    // prevent bogus state
    if (this.proxyPrincipal == null)
      this.proxyPrincipal = new Principal();

    this.proxyPrincipal.username(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceUsername
  /**
   ** Returns the <code>username</code> of the <code>Proxy Server</code>
   ** attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#username}.
   **
   ** @return                    the <code>username</code> of the
   **                            <code>proxyPrincipal</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String proxyUsername() {
    // prevent bogus state
    return this.proxyPrincipal == null ? null : this.proxyPrincipal.username();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyPassword
  /**
   ** Sets the <code>password</code> attribute for the
   ** <code>Proxy Server</code> attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#password}.
   **
   ** @param  value              the <code>password</code> attribute for the
   **                            <code>Proxy Server</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T proxyPassword(final GuardedString value) {
    // prevent bogus state
    if (this.proxyPrincipal == null)
      this.proxyPrincipal = new Principal();

    this.proxyPrincipal.password(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyPassword
  /**
   ** Returns the <code>password</code> attribute for the
   ** <code>Proxy Server</code> attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#password}.
   **
   ** @return                    the <code>password</code> attribute for the
   **                            <code>Proxy Server</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString proxyPassword() {
    return this.proxyPrincipal == null ? null : this.proxyPrincipal.password();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchSchema
  /**
   ** Set <code>true</code> if the schema description should be obtain natively
   ** from the Service Provider.
   ** <br>
   ** If <code>false</code> is specified a well know static schmea description
   ** is populated without a roundtrip to the Service Provider.
   ** <p>
   ** Per default no roundtrip to the Service Provider happens.
   **
   ** @param  value              <code>true</code> if the schema description
   **                            should be obtain natively from the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T fetchSchema(final boolean value) {
    this.fetchSchema = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchSchema
  /**
   ** Returns <code>true</code> if the schema description should be obtain
   ** natively from the Service Provider.
   ** <br>
   ** If <code>false</code> is specified a well know static schmea description
   ** is populated without a roundtrip to the Service Provider.
   ** <p>
   ** Per default no roundtrip to the Service Provider happens.
   **
   ** @return                    <code>true</code> if the schema description
   **                            should be obtain natively from the Service
   **                            Provider.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean fetchSchema() {
    return this.fetchSchema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rfc9110
  /**
   ** Set <code>true</code> if validation of HTTP methods is performed
   ** according to the rules of RFC-9110 or it is skipped.
   ** <p>
   ** Per default the validation is performed.
   **
   ** @param  value              <code>true</code> if validation of HTTP methods
   **                            is performed according to the rules of RFC-9110
   **                            or it is skipped.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T rfc9110(final boolean value) {
    this.rfc9110 = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rfc9110
  /**
   ** Returns <code>true</code> if validation of HTTP methods is performed
   ** according to the rules of RFC-9110 or it is skipped.
   ** <p>
   ** Per default the validation is performed.
   **
   ** @return                    <code>true</code> if validation of HTTP methods
   **                            is performed according to the rules of RFC-9110
   **                            or it is skipped.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean rfc9110() {
    return this.rfc9110;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseFeature
  /**
   ** Set <code>true</code> if the the Service Provider is licensed to use
   ** enhanced features
   **
   ** @param  value              <code>true</code> if the the Service Provider
   **                            is licensed to use enhanced features; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>ServiceEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceEndpoint</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T enterpriseFeature(final boolean value) {
    this.enterpriseFeature = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterpriseFeature
  /**
   ** Returns <code>true</code> if the the Service Provider is licensed to use
   ** enhanced features
   **
   ** @return                    <code>true</code> if the the Service Provider
   **                            is licensed to use enhanced features; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean enterpriseFeature() {
    return this.enterpriseFeature;
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
    if (this.rootContext != null) {
      if (context.charAt(context.length() - 1) != SystemConstant.SLASH && contextPath.charAt(0) != SystemConstant.SLASH)
        context.append(SystemConstant.SLASH);
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
    if (this.rootContext != null) {
      if (context.charAt(context.length() - 1) != SystemConstant.SLASH && this.rootContext.charAt(0) != SystemConstant.SLASH)
        context.append(SystemConstant.SLASH);
      context.append(this.rootContext);
    }

    return context.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyURL
  /**
   ** Return the server parts of the HTTP url, http(s)://host:port blonging to
   ** the <code>Proxy Server</code> deployed in front of a Service Provider.
   **
   ** @return                    the server part of the HTTP url,
   **                            http(s)://host:port.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String proxyURL() {
    // lazy initialization of the configured service URL's
    return this.proxy.serviceURL(this.secureSocket ? SCHEMA_DEFAULT_SECURE : SCHEMA_DEFAULT);
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
    return this.primary.serviceURL(this.secureSocket ? SCHEMA_DEFAULT_SECURE : SCHEMA_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiates the <code>ServiceEndpoint</code>.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an newly created instance of
   **                            <code>ServiceEndpoint</code> providing
   **                            default values only.
   **                            <br>
   **                            Possible object <code>Endpoint</code>.
   */
  public static ServiceEndpoint build(final Loggable loggable) {
    return new ServiceEndpoint(loggable);
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
   **                            <code>ServiceEndpoint</code> configuration
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
   **                            <code>ServiceEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object <code>ServiceEndpoint</code>.
   */
  public static final ServiceEndpoint build(final Loggable loggable, final Map<String, String> parameter) {
    final Principal client    = parameter.containsKey(CLIENT_ID)      ? Principal.of(parameter.get(CLIENT_ID),      parameter.get(CLIENT_PASSWORD)) : null;
    final Principal principal = parameter.containsKey(PRINCIPAL_NAME) ? Principal.of(parameter.get(PRINCIPAL_NAME), parameter.get(PRINCIPAL_PASSWORD)) : null;
    // ensure inheritance
    final ServiceEndpoint endpoint = build(
      loggable
    , new Server(parameter.get(SERVER_HOST), Integer.parseInt(parameter.get(SERVER_PORT)))
    , parameter.get(ROOT_CONTEXT)
    , Boolean.valueOf(parameter.get(SECURE_SOCKET))
    , parameter.get(TYPE_CONTENT)
    , parameter.get(TYPE_ACCEPT)
    , parameter.get(AUTHN_TYPE)
    , client
    , principal
    , principal
    , parameter.containsKey(AUTHN_URI) ? URI.create(parameter.get(AUTHN_URI)) : null
    , parameter.get(LOCALE_LANGUAGE)
    , parameter.get(LOCALE_COUNTRY)
    , parameter.get(LOCALE_TIMEZONE)
    );
    // the default timeout value to establish a connection is undefinitly (-1)
    // unfortunately web targets rejects that value hence we have to set it
    // explicitly
    endpoint.timeOutConnect(parameter.containsKey(CONNECTION_TIMEOUT)    ? Integer.valueOf(parameter.get(CONNECTION_TIMEOUT))     : 1000);
    endpoint.retryCount(parameter.containsKey(CONNECTION_RETRY_COUNT)    ? Integer.valueOf(parameter.get(CONNECTION_RETRY_COUNT)) : CONNECTION_RETRY_COUNT_DEFAULT);
    endpoint.retryCount(parameter.containsKey(CONNECTION_RETRY_INTERVAL) ? Integer.valueOf(parameter.get(CONNECTION_RETRY_COUNT)) : CONNECTION_RETRY_INTERVAL_DEFAULT);
    // the default timeout value to establish a connection is undefinitly (-1)
    // unfortunately web targets rejects that value hence we have to set it
    // explicitly
    endpoint.timeOutResponse(parameter.containsKey(RESPONSE_TIMEOUT)    ? Integer.valueOf(parameter.get(RESPONSE_TIMEOUT))        : 10000);
    return endpoint;
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
   **                            <code>ServiceEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverHost         the host name or IP address of the target
   **                            system on which the Service Provider is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the Service Provider is listening on
   **                            <br>
   **                            Default value for non-SSL: 80
   **                            Default value for SSL: 443
   **                            <br>
   **                            Allowed object is <code>int</code>.
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
   ** @param  clientIdentifier   the identifier of the client that is used for
   **                            authorization purpose at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientSecret       the password of the client that is used for
   **                            authorization purpose at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the acccount of the Service Provider with
   **                            administrator privikeges.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  resourceUsername   the resource owner account to authenticate at
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourcePassword   the password of the resource owner account
   **                            specified by the <code>resourceUsername</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authorizationURI   the {@link URI} to
   **                            <code>Authorization Server</code> exposed by
   **                            the Service Provider endpoint for
   **                            authentication purpose.
   **                            Possible object is {@link URI}.
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
   **                            <code>ServiceEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object <code>ServiceEndpoint</code>.
   */
  public static ServiceEndpoint build(final Loggable loggable, final String serverHost, final int serverPort, final String rootContext, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final String clientIdentifier, final GuardedString clientSecret, final String principalName, final GuardedString principalPassword, final String resourceUsername, final GuardedString resourcePassword, final URI authorizationURI, final String language, final String country, final String timeZone) {
    // ensure inheritance
    return build(loggable, new Server(serverHost, serverPort), rootContext, secureSocket, typeContent, typeAccept, authentication, Principal.of(clientIdentifier, clientSecret), Principal.of(principalName, principalPassword), Principal.of(resourceUsername, resourcePassword), authorizationURI, language, country, timeZone);
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
   **                            <code>ServiceEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  primary            the primary server properties on which the
   **                            Service Provider is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
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
   ** @param  client             the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  principal          the security principal properties user to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  resource           the security principal properties user to
   **                            authorize a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  authorizationURI   the {@link URI} to
   **                            <code>Authorization Server</code> exposed by
   **                            the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Allowed object is {@link URI}.
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
   **                            <code>ServiceEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object <code>ServiceEndpoint</code>.
   */
  public static final ServiceEndpoint build(final Loggable loggable, final Server primary, final String rootContext, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final Principal client, final Principal principal, final Principal resource, final URI authorizationURI, final String language, final String country, final String timeZone) {
    // ensure inheritance
    return new ServiceEndpoint(loggable, primary, rootContext, secureSocket, typeContent, typeAccept, authentication, client, principal, resource, authorizationURI, language, country, timeZone);
  }
}