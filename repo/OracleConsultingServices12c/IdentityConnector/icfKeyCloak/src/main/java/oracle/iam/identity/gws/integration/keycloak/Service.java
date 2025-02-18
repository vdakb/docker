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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   RedHat KeyCloak Connector

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Service.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-04-06  SBernet    First release version
*/

package oracle.iam.identity.gws.integration.keycloak;

import java.util.Set;
import java.util.Map;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// interface Service
// ~~~~~~~~~ ~~~~~~~~
public interface Service {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum AuthenticationScheme
  // ~~~~ ~~~~~~~~~~~~~~~~~~~~
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
  enum AuthenticationScheme {
      NONE("none")
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
    , PREEMTIVE("basic-preemptive")
      /**
       ** non-preemptive authentication way i.e. auth information is added only
       ** when server refuses the request with 401 status code and then the
       ** request is repeated with authentication information.
       */
    , NONPREEMTIVE("basic-non-preemptive")
      /**
       ** The Json Web Token (JWT) Profile RFC7523 implements the RFC7521
       ** abstract assertion protocol. It aims to extend the OAuth2 protocol to
       ** use JWT as an additional authorization grant.
       */
    , TOKEN("token")
      /**
       ** The OAuth2 resource owner password credentials grant type is suitable
       ** in cases where the resource owner has a trust relationship with the
       ** client, such as the device operating system or a highly privileged
       ** application. The authorization server should take special care when
       ** enabling this grant type and only allow it when other flows are not
       ** viable.
       */
    , PASSWORD("oauth-password")
      /**
       ** The OAuth2 client can request an access token using only its client
       ** credentials (or other supported means of authentication) when the
       ** client is requesting access to the protected resources under its
       ** control, or those of another resource owner that have been previously
       ** arranged with the authorization server (the method of which is beyond
       ** the scope of this specification).
       */
    , CREDENTIAL("oauth-credential")
    ;

    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the tag name of the authentication scheme. */
    final String  tag;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>AuthenticationScheme</code> with a tag name.
     **
     ** @param  tag              the logical name of this authentication scheme
     **                          to lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    AuthenticationScheme(final String tag) {
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
     ** @param  value            the string value the authentication scheme
     **                          should be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>AuthenticationScheme</code>.
     **                          <br>
     **                          Possible object is
     **                          <code>AuthenticationScheme</code>.
     */
    public static AuthenticationScheme from(final String value) {
      for (AuthenticationScheme cursor : AuthenticationScheme.values()) {
        if (cursor.tag.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface ICF
  // ~~~~~~~~~ ~~~
  interface ICF {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // interface Resource
    // ~~~~~~~~~ ~~~~~~~~
    public interface Resource {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the host name for the target system Service Provider.
       */
      static final String SERVER_HOST                 = "serverHost";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the listener port for the target system Service Provider.
       */
      static final String SERVER_PORT                 = "serverPort";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify if you plan to configure SSL to secure communication between
       ** Identity Manager and the target system.
       */
      static final String ROOT_CONTEXT                = "rootContext";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify if you plan to configure SSL to secure communication between
       ** Identity Manager and the target system.
       */
      static final String SECURE_SOCKET               = "secureSocket";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the content media type send at any request except
       ** authentication requests to the Service Provider.
       */
      static final String TYPE_CONTENT                = "contentType";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the content media type accepted by the client at any request
       ** except authentication requests.
       */
      static final String TYPE_ACCEPT                 = "acceptType";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the authentication scheme to authenticate the security
       ** principal at the Service Provider.
       */
      static final String AUTHENTICATION_SCHEME       = "authenticationScheme";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the authorization server to authenticate the security
       ** principal at the Service Provider.
       ** <p>
       ** This attribute willl be mandatory if the authentication scheme is
       ** Authentication#Scheme#ClientCredential or
       ** Authentication#Scheme#Password.
       */
      static final String AUTHORIZATION_SERVER        = "authorizationServer";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the identifier of the client to authenticate the connector at
       ** the Service Provider.
       ** <p>
       ** This attribute willl be mandatory if the authentication scheme is
       ** Authentication#Scheme#ClientCredential or
       ** Authentication#Scheme#Password.
       */
      static final String CLIENT_IDENTIFIER           = "clientIdentifier";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the identifier of the client to authenticate the connector at
       ** the Service Provider.
       ** <p>
       ** This attribute willl be mandatory if the authentication scheme is
       ** Authentication#Scheme#ClientCredential or
       ** Authentication#Scheme#Password.
       */
      static final String CLIENT_SECRET               = "clientSecret";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the user name of the target system account to be used to
       ** establish a connection.
       */
      static final String PRINCIPAL_NAME              = "principalUsername";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the password of the target system account specified by the
       ** #PRINCIPAL_NAME parameter.
       */
      static final String PRINCIPAL_PASSWORD          = "principalPassword";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the identifier of the resource owner to authenticate the
       ** connector at the Service Provider.
       ** <p>
       ** This attribute willl be mandatory if the authentication scheme is
       ** Authentication#Scheme#Password.
       */
      static final String RESOURCE_OWNER              = "resourceOwner";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the credential of the resource owner to authenticate the
       ** connector at the Service Provider.
       ** <p>
       ** This attribute willl be mandatory if the authentication scheme is
       ** Authentication#Scheme#Password.
       */
      static final String RESOURCE_CREDENTIAL         = "resourceCredential";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of language the server is using.
       */
      static final String LOCALE_LANGUAGE             = "language";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of language region the server is using.
       */
      static final String LOCALE_COUNTRY              = "country";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of time zone the server is using.
       */
      static final String LOCALE_TIMEZONE             = "timeZone";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the time (in milliseconds) within which the target system is
       ** expected to respond to a connection attempt.
       */
      static final String CONNECTION_TIMEOUT          = "connectionTimeOut";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the number of consecutive attempts to be made at establishing
       ** a connection with the target system.
       */
      static final String CONNECTION_RETRY_COUNT      = "connectionRetryCount";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the interval (in milliseconds) between consecutive attempts
       ** at establishing a connection with the target system.
       */
      static final String CONNECTION_RETRY_INTERVAL   = "connectionRetryInterval";
      /**
       ** Attribute tag which may be defined on a <code>IT Resource</code>
       ** to specify the timeout period the Service Provider consumer doesn't
       ** get a HTTP response.
       ** <p>
       ** If this property has not been specified, the default is to wait for the
       ** response until it is received.
       */
      static final String RESPONSE_TIMEOUT            = "responseTimeOut";
    }

    ////////////////////////////////////////////////////////////////////////////
    // interface Feature
    // ~~~~~~~~~ ~~~~~~~~
    public interface Feature {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the schemas needs to be
       ** fetched from the Service Provider.
       ** <p>
       ** Default: <code>false</code>
       */
      static final String FETCH_SCHEMA = "fetchSchema";
      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify if validation of HTTP
       ** methods is performed according to the rules of RFC-9110 or it is
       ** skipped.
       ** <p>
       ** Default: <code>true</code>
       */
      static final String RFC_9110     = "enforceRFC9100";
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface OIM
  // ~~~~~~~~~ ~~~
  interface OIM {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // interface Resource
    // ~~~~~~~~~ ~~~~~~~~
    public interface Resource {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the content media type send at any request except
       ** authentication requests to the Service Provider.
       */
      static final String TYPE_CONTENT          = "Content Type";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the content media type accepted by the client at any request
       ** except authentication requests.
       */
      static final String TYPE_ACCEPT           = "Accept Type";
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
      static final String AUTHENTICATION_SCHEME = "Authentication Scheme";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the authorization server to authenticate the security
       ** principal at the Service Provider.
       ** <p>
       ** This attribute will be mandatory if the authentication scheme is
       ** Endpoint#Authentication#password or
       ** Endpoint#Authentication#CREDENTIAL.
       */
      static final String AUTHORIZATION_SERVER  = "Authorization Server";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the identifier of the client to authenticate the connector at
       ** the Service Provider.
       ** <p>
       ** This attribute will be mandatory if the authentication scheme is
       ** Endpoint#Authentication#password or
       ** Endpoint#Authentication#CREDENTIAL.
       */
      static final String CLIENT_IDENTIFIER     = "Client Identifier";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the identifier of the client to authenticate the connector at
       ** the Service Provider.
       ** <p>
       ** This attribute willl be mandatory if the authentication scheme is
       ** Authentication#Scheme#ClientCredential.
       */
      static final String CLIENT_SECRET         = "Client Secret";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the identifier of the resource owner to authenticate the
       ** connector at the Service Provider.
       ** <p>
       ** This attribute willl be mandatory if the authentication scheme is
       ** Endpoint#Authentication#PASSWORD.
       */
      static final String RESOURCE_OWNER        = "Resource Owner";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the credential of the resource owner to authenticate the
       ** connector at the Service Provider.
       ** <p>
       ** This attribute will be mandatory if the authentication scheme is
       ** Endpoint#Authentication#PASSWORD.
       */
      static final String RESOURCE_CREDENTIAL   = "Resource Credential";
    }

    ////////////////////////////////////////////////////////////////////////////
    // interface Feature
    // ~~~~~~~~~ ~~~~~~~~
    interface Feature {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the schemas needs to be
       ** fetched from the Service Provider.
       ** <p>
       ** Default: <code>false</code>
       */
      static final String FETCH_SCHEMA = "fetch-schema";
      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify if validation of HTTP
       ** methods is performed according to the rules of RFC-9110 or it is
       ** skipped.
       ** <p>
       ** Default: <code>true</code>
       */
      static final String RFC_9110     = "enforce-rfc9110";
    }
  }
  /**
   ** The attribute mapping of the feature transfer from the
   ** <code>Metadata Descriptor</code> to <code>Identity Connector</code>
   ** configuration
   */
  static final Map<String, String> FEATURE = CollectionUtility.unmodifiableMap(
    new String[][]{
      {ICF.Feature.FETCH_SCHEMA,       OIM.Feature.FETCH_SCHEMA}
    , {ICF.Feature.RFC_9110,           OIM.Feature.RFC_9110}
    }
  );
  /**
   ** The attribute tags of the feature definition in the
   ** <code>Metadata Descriptor</code>
   */
  static final Set<String> PROPERTY = CollectionUtility.unmodifiableSet(
      OIM.Feature.FETCH_SCHEMA
    , OIM.Feature.RFC_9110
  );
  /**
   ** The attribute mapping of the feature transfer from the
   ** <code>IT Resource</code> to <code>Identity Connector</code>
   ** configuration
   */
  static final Map<String, String> RESOURCE = CollectionUtility.synchronizedMap(
    new String[][]{
      {ICF.Resource.SERVER_HOST,               ServiceResource.SERVER_NAME}
    , {ICF.Resource.SERVER_PORT,               ServiceResource.SERVER_PORT}
    , {ICF.Resource.ROOT_CONTEXT,              ServiceResource.ROOT_CONTEXT}
    , {ICF.Resource.SECURE_SOCKET,             ServiceResource.SECURE_SOCKET}
    , {ICF.Resource.TYPE_CONTENT,              OIM.Resource.TYPE_CONTENT}
    , {ICF.Resource.TYPE_ACCEPT,               OIM.Resource.TYPE_ACCEPT}
    , {ICF.Resource.AUTHENTICATION_SCHEME,     OIM.Resource.AUTHENTICATION_SCHEME}
    , {ICF.Resource.AUTHORIZATION_SERVER,      OIM.Resource.AUTHORIZATION_SERVER}
    , {ICF.Resource.CLIENT_IDENTIFIER,         OIM.Resource.CLIENT_IDENTIFIER}
    , {ICF.Resource.CLIENT_SECRET,             OIM.Resource.CLIENT_SECRET}
    , {ICF.Resource.PRINCIPAL_NAME,            ServiceResource.PRINCIPAL_NAME}
    , {ICF.Resource.PRINCIPAL_PASSWORD,        ServiceResource.PRINCIPAL_PASSWORD}
    , {ICF.Resource.RESOURCE_OWNER,            OIM.Resource.RESOURCE_OWNER}
    , {ICF.Resource.RESOURCE_CREDENTIAL,       OIM.Resource.RESOURCE_CREDENTIAL}
    , {ICF.Resource.LOCALE_LANGUAGE,           ServiceResource.LOCALE_LANGUAGE}
    , {ICF.Resource.LOCALE_COUNTRY,            ServiceResource.LOCALE_COUNTRY}
    , {ICF.Resource.LOCALE_TIMEZONE,           ServiceResource.LOCALE_TIMEZONE}
    , {ICF.Resource.CONNECTION_TIMEOUT,        ServiceResource.CONNECTION_TIMEOUT}
    , {ICF.Resource.CONNECTION_RETRY_COUNT,    ServiceResource.CONNECTION_RETRY_COUNT}
    , {ICF.Resource.CONNECTION_RETRY_INTERVAL, ServiceResource.CONNECTION_RETRY_INTERVAL}
    , {ICF.Resource.RESPONSE_TIMEOUT,          ServiceResource.RESPONSE_TIMEOUT}
    }
  );
}