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

    File        :   AbstractFlowBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractFlowBuilder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.authn.oauth.v2;

import java.net.URI;

import javax.ws.rs.client.Client;

import oracle.iam.identity.icf.foundation.AbstractEndpoint.Principal;

///////////////////////////////////////////////////////////////////////////////
// class AbstractFlowBuilder
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Abstract implementation of {@link AuthorizationFlow.Builder}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class AbstractFlowBuilder<T extends AuthorizationFlow.Builder> implements AuthorizationFlow.Builder<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final AuthorizationFlow.Builder<T> delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>AbstractFlowBuilder</code> for OAuth2 that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractFlowBuilder(final AuthorizationFlow.Builder<T> delegate) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.delegate = delegate;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessTokenURI (AuthorizationFlow.Builder)
  /**
   ** Set the access token URI on which the access token can be requested.
   ** <br>
   ** The URI points to the authorization server and is defined by the Service
   ** Provider.
   **
   ** @param  uri                the access token URI.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the {@link AuthorizationFlow.Builder} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthorizationFlow.Builder}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public T accessTokenURI(final URI uri) {
    this.delegate.accessTokenURI(uri);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshTokenURI (AuthorizationFlow.Builder)
  /**
   ** Set the refresh token URI on which the access token can be refreshed
   ** using a refresh token.
   ** <br>
   ** The URI points to the authorization server and is defined by the Service
   ** Provider.
   ** <br>
   ** If the URI is not defined by this method it will be the same as URI
   ** defined in {@link #accessTokenURI(URI)} (which is the default value
   ** defined by the OAuth2 spec).
   ** <p>
   ** Some providers do not support refreshing access tokens at all.
   **
   ** @param  uri                the refresh token URI.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the {@link AuthorizationFlow.Builder} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthorizationFlow.Builder}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public T refreshTokenURI(final URI uri) {
    this.delegate.refreshTokenURI(uri);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizationURI (AuthorizationFlow.Builder)
  /**
   ** Set the URI to which the user should be redirected to authorize the
   ** application.
   ** <br>
   ** The URI points to the authorization server and is defined by the Service
   ** Provider.
   **
   ** @param  uri                the authorization URI.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the {@link AuthorizationFlow.Builder} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthorizationFlow.Builder}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public T authorizationURI(final URI uri) {
    this.delegate.authorizationURI(uri);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redirectURI (AuthorizationFlow.Builder)
  /**
   ** Set the redirect URI to which the user (resource owner) should be
   ** redirected after he/she grants access to the application.
   ** <br>
   ** In most cases, the URI is under control of the application and request
   ** done on this URI will be used to extract query parameter
   ** <code>code</code> and <code>state</code> that will be used in
   ** {@link AuthorizationFlow#finish(String, String)} method.
   ** <p>
   ** If URI is not defined by this method, the default value
   ** <code>urn:ietf:wg:oauth:2.0:oob</code> will be used in the Authorization
   ** Flow which should cause that <code>code</code> will be passed to
   ** application in other way than request redirection (for example shown to
   ** the user using html page).
   **
   ** @param  uri                the redirect URI that should receive
   **                            authorization response from the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AuthorizationFlow.Builder} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthorizationFlow.Builder}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public T redirectURI(final String uri) {
    this.delegate.redirectURI(uri);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier (AuthorizationFlow.Builder)
  /**
   ** Set client identifier of the application that should be authorized.
   **
   ** @param  identifier         the client identifier instance.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   **
   ** @return                    the {@link AuthorizationFlow.Builder} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthorizationFlow.Builder}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public T identifier(final Principal identifier) {
    this.delegate.identifier(identifier);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scope (AuthorizationFlow.Builder)
  /**
   ** Set the scope to which the application will get authorization grant.
   ** <br>
   ** Values of this parameter are defined by the Service Provider and defines
   ** usually subset of resource and operations available in the Service
   ** Provider.
   ** <p>
   ** The parameter is optional but Service Provider might require it.
   **
   ** @param  scope              the scope.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AuthorizationFlow.Builder} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthorizationFlow.Builder}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public T scope(final String scope) {
    this.delegate.scope(scope);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client (AuthorizationFlow.Builder)
  /**
   ** Set the client that should be used internally by the
   ** {@link AuthorizationFlow} to make requests to
   ** <code>Authorization Server</code>.
   ** <br>
   ** If this method is not called, it is up to the implementation to create
   ** or get any private client instance to perform these requests.
   ** <br>
   ** This method could be used mainly for performance reasons to avoid
   ** creation of new client instances and have control about created client
   ** instances used in the application.
   **
   ** @param  client             the client identifier instance.
   **                            <br>
   **                            Allowed object is {@link Client}.
   **
   ** @return                    the {@link AuthorizationFlow.Builder} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthorizationFlow.Builder}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public T client(final Client client) {
    this.delegate.client(client);
    return (T)this;
  }

    //////////////////////////////////////////////////////////////////////////////
  // Method:   property (AuthorizationFlow.Builder)
  /**
   ** Set property (parameter) that will be added to requests or URIs as a
   ** query parameters during the {@link AuthorizationFlow}.
   ** <br>
   ** Default parameters used during the {@link AuthorizationFlow} can be also
   ** overridden by this method.
   **
   ** @param  phase              the phase of the flow in which the properties
   **                            (parameters) should be used.
   **                            <br>
   **                            For example by using a
   **                            {@link AuthorizationFlow.Phase#ACCESS},
   **                            the parameter will be added only to the http
   **                            request for access token.
   **                            <br>
   **                            Allowed object is
   **                            {@link AuthorizationFlow.Phase}.
   ** @param  key                the property key.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the property value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AuthorizationFlow.Builder} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthorizationFlow.Builder}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public T property(final AuthorizationFlow.Phase phase, final String key, final String value) {
    this.delegate.property(phase, key, value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to build a {@code AuthorizationFlow} instance.
   **
   ** @return                    the new instance of {@code AuthorizationFlow}.
   */
  @Override
  public AuthorizationFlow build() {
    return this.delegate.build();
  }
}
