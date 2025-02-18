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
    Subsystem   :   Generic WebService Connector

    File        :   AuthorizationClient.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AuthorizationClient.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.authn.oauth.v2;

import java.net.URI;

import oracle.iam.identity.icf.foundation.AbstractEndpoint.Principal;

import oracle.iam.identity.icf.authn.oauth.AuthorizationFeature;

///////////////////////////////////////////////////////////////////////////////
// class AuthorizationClient
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Main class to build the Authorization Flow instances and
 ** {@link javax.ws.rs.core.Feature client filter feature} that can supports
 ** performing of authenticated OAuth requests.
 ** <p>
 ** <b>Authorization flow</b>
 ** </p>
 ** For more information about authorization flow, see
 ** {@link AuthorizationFlow}.
 ** <p>
 ** <b>Client feature</b>
 ** <p>
 ** Use method {@link AuthorizationFeature#build(Token)} to build the feature.
 ** <br>
 ** OAuth2 client filter feature registers the support for performing
 ** authenticated requests to the Service Provider. The feature uses an access
 ** token to initialize the internal
 ** {@link javax.ws.rs.container.ContainerRequestFilter filter} which will add
 ** <code>Authorization</code> http header containing OAuth 2 authorization
 ** information (based on <code>bearer</code> tokens).
 ** <p>
 ** The internal filter can be controlled by properties put into the
 ** {@link javax.ws.rs.client.ClientRequestContext client request} using
 ** {@link javax.ws.rs.client.ClientRequestContext#setProperty(String, Object)}
 ** method. The property key is defined in this class as a static variables
 ** ({@link AuthorizationFeature#PROPERTY_ACCESS_TOKEN} (see its javadoc
 ** for usage).
 ** <br>
 ** Using the property a specific access token can be defined for each request.
 ** <p>
 ** Example of using specific access token for one request:
 ** <pre>
 **   final Response response = client.target("foo")
 **     .request()
 **     .property(PROPERTY_ACCESS_TOKEN, "6ab45ab465e46f54d771a")
 **     .get();
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class AuthorizationClient {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>AuthorizationClient</code> for OAuth2 that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private AuthorizationClient() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flowBuilder
  /**
   ** Factory method to create the builder of the
   ** {@link AuthorizationFlow Authorization Code Grant Flow}.
   **
   ** @param  identifier         the client identifier (id of application that
   **                            wants to be approved). Issued by the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  accessTokenURI     the access token {@link URI} on which the
   **                            access token can be requested.
   **                            <br>
   **                            The URI points to the authorization server and
   **                            is defined by the Service Provider.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the builder of the
   **                           {@link AuthorizationFlow Authorization Code Grant Flow}.
   **                            Possible object is
   **                            {@link Authorization.Builder}.
   */
  public static AuthorizationFlow.Builder flowBuilder(final Principal identifier, final URI accessTokenURI) {
    return new Authorization.Builder(identifier, accessTokenURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flowBuilder
  /**
   ** Factory method to create the builder of the
   ** {@link AuthorizationFlow Authorization Code Grant Flow}.
   **
   ** @param  identifier         the client identifier (id of application that
   **                            wants to be approved). Issued by the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  authorizationURI   the {@link URI} to which the user should be
   **                            redirected to authorize our application.
   **                            <br>
   **                            The URI points to the authorization server and
   **                            is defined by the Service Provider.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  accessTokenURI     the access token {@link URI} on which the
   **                            access token can be requested.
   **                            <br>
   **                            The URI points to the authorization server and
   **                            is defined by the Service Provider.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the builder of the
   **                            {@link AuthorizationFlow Authorization Code Grant Flow}.
   **                            Possible object is
   **                            {@link Authorization.Builder}.
   */
  public static AuthorizationFlow.Builder flowBuilder(final Principal identifier, final URI authorizationURI, final URI accessTokenURI) {
    return new Authorization.Builder(identifier, authorizationURI, accessTokenURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   googleBuilder
  /**
   ** Factory method to create the builder that can be directly used to perform
   ** Authorization Code Grant flow defined by Facebook.
   **
   ** @param  identifier         the client identifier (id of application that
   **                            wants to be approved). Issued by the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  redirectURI        the URI to which the user (resource owner)
   **                            should be redirected after he/she grants access
   **                            to the application or <code>null</code> if the
   **                            application does not support redirection (eg.
   **                            is not a web server).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  scope              the api to which an access is requested (eg.
   **                            Google tasks).
   **                            Allowed object is {@link String}.
   **
   ** @return                    the builder of the
   **                            {@link AuthorizationFlow Authorization Code Grant Flow}.
   **                            Possible object is
   **                            {@link Authorization.Builder}.
   */
  public static AuthorizationFlow.Builder googleBuilder(final Principal identifier, final String redirectURI, final String scope) {
    return new GoogleFlowBuilder()
      .authorizationURI(URI.create("https://accounts.google.com/o/oauth2/auth"))
      .accessTokenURI(URI.create("https://accounts.google.com/o/oauth2/token"))
      .redirectURI(redirectURI == null ? AuthorizationFlow.REDIRECT_OOB : redirectURI)
      .identifier(identifier)
      .scope(scope);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facebookBuilder
  /**
   ** Factory method to create the builder that can be directly used to perform
   ** Authorization Code Grant flow defined by Facebook.
   **
   ** @param  identifier         the client identifier (id of application that
   **                            wants to be approved). Issued by the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  redirectURI        the URI to which the user (resource owner)
   **                            should be redirected after he/she grants access
   **                            to the application or <code>null</code> if the
   **                            application does not support redirection (eg.
   **                            is not a web server).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the builder of the
   **                            {@link AuthorizationFlow Authorization Code Grant Flow}.
   **                            Possible object is
   **                            {@link Authorization.Builder}.
   */
  public static AuthorizationFlow.Builder facebookBuilder(final Principal identifier, final String redirectURI) {
    return null;
  }
}