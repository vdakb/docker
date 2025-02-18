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

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   AuthenticationFilter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AuthenticationFilter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.jacc;

import java.util.Objects;
import java.util.Optional;

import java.io.IOException;

import java.security.Principal;

import javax.annotation.Priority;

import javax.ws.rs.Priorities;
import javax.ws.rs.InternalServerErrorException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;

////////////////////////////////////////////////////////////////////////////////
// class AuthenticationFilter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** An extension implementing a container request filter.
 ** <p>
 ** By default, i.e. if no name binding is applied to the filter implementation
 ** class, the filter instance is applied globally, however only after the
 ** incoming request has been matched to a particular resource by JAX-RS
 ** runtime. If there is a @NameBinding annotation applied to the filter, the
 ** filter will also be executed at the post-match request extension point, but
 ** only in case the matched resource or sub-resource method is bound to the
 ** same name-binding annotation.
 ** <p>
 ** In case the filter should be applied at the pre-match extension point, i.e.
 ** before any request matching has been performed by JAX-RS runtime, the filter
 ** <b>must</b> be annotated with a @PreMatching annotation.
 ** <p>
 ** Use a pre-match request filter to update the input to the JAX-RS matching
 ** algorithm, e.g., the HTTP method, Accept header, return cached responses
 ** etc. Otherwise, the use of a request filter invoked at the post-match
 ** request extension point (after a successful resource method matching) is
 ** recommended.
 ** <p>
 ** Filters implementing this interface must be annotated with @Provider to be
 ** discovered by the JAX-RS runtime. Container request filter instances may
 ** also be discovered and bound dynamically to particular resource methods.
 **
 ** @param  <C>                  the type of credentials the authenticator can
 **                              authenticate.
 **                              <br>
 **                              Allowed object is <code>&lt;C&gt;</code>.
 ** @param  <P>                  the type of principals the authenticator
 **                              returns.
 **                              <br>
 **                              Allowed object is <code>&lt;P&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Priority(Priorities.AUTHENTICATION)
public abstract class AuthenticationFilter<C extends Credential, P extends Principal> implements ContainerRequestFilter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Scheme              scheme;
  protected final Authenticator<C, P> authenticator;
  protected final Authorizer<P>       authorizer;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Scheme
  // ~~~~ ~~~~~~
  /**
   ** The feature constraint of the asserter scheme.
   */
  public enum Scheme {
      /**
       ** The mode value if the identity is passed by a HTTP Basic
       ** authentication mechanism.
       */
      BASIC("Basic")
      /**
       ** The mode value if the identity is passed by a HTTP Cookie
       ** authentication mechanism.
       */
    , COOKIE("Cookie")
      /**
       ** The mode value if the identity is passed by a HTTP Bearer Token
       ** authentication mechanism.
       */
    , BEARER("Bearer")
      /**
       ** The mode value if the identity is passed by a HTTP Request Header
       ** plain text value.
       */
    , HEADER("Header")
      /**
       ** The mode value if the identity is passed by a HTTP Request Header
       ** signed SAML assertion.
       */
    , ASSERTION("Assertion")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The camel case string value for autenticator scheme. */
    public final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Scheme</code> with a constraint value.
     **
     ** @param  id               the asserter strategy of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Scheme(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the value of the asserter mode constraint.
     **
     ** @return                  the value of the asserter mode constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a proper <code>Scheme</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Scheme</code> constraint.
     **                          <br>
     **                          Possible object is <code>Scheme</code>.
     */
    public static Scheme of(final String value) {
      for (Scheme cursor : Scheme.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Basic
  // ~~~~~ ~~~~~
  public static class Basic<P extends Principal> extends AuthenticationFilter<Credential.Basic, P> {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Builder for <code>Basic</code> {@link AuthenticationFilter}.
     ** <p>
     ** An {@link Authenticator} must be provided during the building process.
     **
     ** @param  <P>                the type of principals the authenticator
     **                            returns.
     **                            <br>
     **                            Allowed object is <code>&lt;P&gt;</code>.
     */
    public static class Builder<P extends Principal> extends AuthenticationFilter.Builder<Credential.Basic, P, Basic<P>> {

      //////////////////////////////////////////////////////////////////////////
      // Methods of abstract base classes
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: create (AuthenticationFilter.Builder)
      /**
       ** Factory method to create a new <code>Basic</code> token
       ** {@link AuthenticationFilter}.
       **
       ** @return                the {@link AuthenticationFilter}.
       **                        <br>
       **                        Possible object is <code>Basic</code>
       **                        {@link AuthenticationFilter}.
       */
      @Override
      protected Basic<P> create() {
        return new Basic<>(this.authenticator, this.authorizer);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Basic</code> token
     ** {@link AuthenticationFilter}.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Bearer()" and enforces use of the public method below.
     **
     ** @param  authenticator    the <code>authenticator</code> property of this
     **                          <code>AuthenticatorFilter</code>.
     **                          <br>
     **                          Allowed object is {@link Authenticator} of type
     **                          <code>C</code> for the credentials and
     **                          <code>P</code> for the principal.
     ** @param  authorizer       the <code>authorizer</code> property of this
     **                          filter <code>AuthenticatorFilter</code>.
     **                          <br>
     **                          Allowed object is {@link Authorizer} of type
     **                          <code>P</code>.
     */
    protected Basic(final Authenticator<Credential.Basic, P> authenticator, final Authorizer<P> authorizer) {
      // ensure inheritance
      super(Scheme.BASIC, authenticator, authorizer);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interafces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter (ContainerRequestFilter)
    /**
     ** Filter method called before a request has been dispatched to a resource.
     ** <p>
     ** Filters in the filter chain are ordered according to their
     ** {@link Priority} class-level annotation value. If a request filter
     ** produces a response by calling
     ** {@link ContainerRequestContext#abortWith(javax.ws.rs.core.Response)}
     ** method, the execution of the (either pre-match or post-match) request
     ** filter chain is stopped and the response is passed to the corresponding
     ** response filter chain (either pre-match or post-match).
     ** <br>
     ** For example, a pre-match caching filter may produce a response in this
     ** way, which would effectively skip any post-match request filters as well
     ** as post-match response filters.
     ** <br>
     ** <b>Note</b>:
     ** However that a responses produced in this manner would still be processed
     ** by the pre-match response filter chain.
     **
     ** @param  request          the context of the incomming request.
     **                          <br>
     **                          Allowed object is
     **                          {@link ContainerRequestContext}.
     **
     ** @throws IOException      if an I/O exception occurs.
     */
    @Override
    public void filter(final ContainerRequestContext request)
      throws IOException {

      final Credential.Basic credential = Credential.basic(request);
      if (!authenticate(request, credential, Scheme.BASIC))
        unauthorized(request);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Bearer
  // ~~~~~ ~~~~~~
  /**
   **
   ** @param  <P>                the type of principals the authenticator
   **                            returns.
   **                            <br>
   **                            Allowed object is <code>&lt;P&gt;</code>.
   */
  public static class Bearer<P extends Principal> extends AuthenticationFilter<Credential.Token, P> {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Builder for <code>Bearer</code> token {@link AuthenticationFilter}.
     ** <p>
     ** An {@link Authenticator} must be provided during the building process.
     **
     ** @param  <P>                the type of principals the authenticator
     **                            returns.
     **                            <br>
     **                            Allowed object is <code>&lt;P&gt;</code>.
     */
    public static class Builder<P extends Principal> extends AuthenticationFilter.Builder<Credential.Token, P, Bearer<P>> {

      //////////////////////////////////////////////////////////////////////////
      // Methods of abstract base classes
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: create (AuthenticationFilter.Builder)
      /**
       ** Factory method to create a new <code>Bearer</code> token
       ** {@link AuthenticationFilter}.
       **
       ** @return                the {@link AuthenticationFilter}.
       **                        <br>
       **                        Possible object is <code>Bearer</code>
       **                        {@link AuthenticationFilter}.
       */
      @Override
      protected Bearer<P> create() {
        return new Bearer<>(this.authenticator, this.authorizer);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Bearer</code> token
     ** {@link AuthenticationFilter}.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Bearer()" and enforces use of the public method below.
     **
     ** @param  authenticator    the <code>authenticator</code> property of this
     **                          <code>AuthenticatorFilter</code>.
     **                          <br>
     **                          Allowed object is {@link Authenticator} of type
     **                          <code>C</code> for the credentials and
     **                          <code>P</code> for the principal.
     ** @param  authorizer       the <code>authorizer</code> property of this
     **                          filter <code>AuthenticatorFilter</code>.
     **                          <br>
     **                          Allowed object is {@link Authorizer} of type
     **                          <code>P</code>.
     */
    protected Bearer(final Authenticator<Credential.Token, P> authenticator, final Authorizer<P> authorizer) {
      // ensure inheritance
      super(Scheme.BEARER, authenticator, authorizer);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interafces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter (ContainerRequestFilter)
    /**
     ** Filter method called before a request has been dispatched to a resource.
     ** <p>
     ** Filters in the filter chain are ordered according to their
     ** {@link Priority} class-level annotation value. If a request filter
     ** produces a response by calling
     ** {@link ContainerRequestContext#abortWith(javax.ws.rs.core.Response)}
     ** method, the execution of the (either pre-match or post-match) request
     ** filter chain is stopped and the response is passed to the corresponding
     ** response filter chain (either pre-match or post-match).
     ** <br>
     ** For example, a pre-match caching filter may produce a response in this
     ** way, which would effectively skip any post-match request filters as well
     ** as post-match response filters.
     ** <br>
     ** <b>Note</b>:
     ** However that a responses produced in this manner would still be processed
     ** by the pre-match response filter chain.
     **
     ** @param  request          the context of the incomming request.
     **                          <br>
     **                          Allowed object is
     **                          {@link ContainerRequestContext}.
     **
     ** @throws IOException      if an I/O exception occurs.
     */
    @Override
    public void filter(final ContainerRequestContext request)
      throws IOException {

      final Credential.Token credential = Credential.token(request);
      if (!authenticate(request, credential, this.scheme))
        unauthorized(request);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Builder
  // ~~~~~~~~ ~~~~~ ~~~~~~~
  /**
   ** @param  <C>                  the type of credentials the authenticator can
   **                              authenticate.
   **                              <br>
   **                              Allowed object is <code>&lt;C&gt;</code>.
   ** @param  <P>                  the type of principals the authenticator
   **                              returns.
   **                              <br>
   **                              Allowed object is <code>&lt;P&gt;</code>.
   ** @param  <T>                  the type of the authentication filter to
   **                              apply on each request.                      
   **                              <br>
   **                              Allowed object is <code>&lt;P&gt;</code>.
   */
  public static abstract class Builder<C extends Credential, P extends Principal, T extends AuthenticationFilter<C, P>> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected String               realm         = "realm";
    protected Scheme               scheme        = Scheme.BASIC;
    protected Authenticator<C, P>  authenticator = credentials -> Optional.empty();
    protected Authorizer<P>        authorizer    = new Authorizer.PermitAll<>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default cache <code>Builder</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Builder()" and enforces use of the public method below.
     */
    protected Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: realm
    /**
     ** Sets the <code>realm</code> property this filter <code>Builder</code>.
     **
     ** @param  value            the <code>realm</code> property this filter
     **                          <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder<C, P, T> realm(final String value) {
      this.realm = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: realm
    /**
     ** Returns the <code>realm</code> property this filter
     ** <code>Builder</code>.
     **
     ** @return                  the <code>realm</code> property this filter
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String realm() {
      return this.realm;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scheme
    /**
     ** Sets the <code>scheme</code> property this filter <code>Builder</code>.
     **
     ** @param  value            the <code>scheme</code> property this filter
     **                          <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Scheme}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder<C, P, T> scheme(final Scheme value) {
      this.scheme = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scheme
    /**
     ** Returns the <code>scheme</code> property this filter
     ** <code>Builder</code>.
     **
     ** @return                  the <code>scheme</code> property this filter
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is {@link Scheme}.
     */
    public Scheme scheme() {
      return this.scheme;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: authenticator
    /**
     ** Sets the <code>authenticator</code> property this filter
     ** <code>Builder</code>.
     **
     ** @param  value            the <code>authenticator</code> property this
     **                          filter <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Authenticator} of type
     **                          <code>C</code> for the credentials and
     **                          <code>P</code> for the principal.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Authorizer</code>.
     */
    public Builder<C, P, T> authenticator(final Authenticator<C, P> value) {
      this.authenticator = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: authenticator
    /**
     ** Returns the <code>authenticator</code> property this filter
     ** <code>Builder</code>.
     **
     ** @return                  the <code>authenticator</code> property this
     **                          filter <code>Builder</code>.
     **                          <br>
     **                          Possible object is {@link Authenticator} of type
     **                          <code>C</code> for the credentials and
     **                          <code>P</code> for the principal.
     */
    public Authenticator authenticator() {
      return this.authenticator;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: authorizer
    /**
     ** Sets the <code>authorizer</code> property this filter
     ** <code>Builder</code>.
     **
     ** @param  value            the <code>authorizer</code> property this
     **                          filter <code>Builder</code>.
     **                          <br>
     **                          Allowed  object is {@link Authorizer} of type
     **                          <code>P</code> for the principal.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Authorizer</code>.
     */
    public Builder<C, P, T> authorizer(final Authorizer<P> value) {
      this.authorizer = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: authorizer
    /**
     ** Returns the <code>authorizer</code> property this filter
     ** <code>Builder</code>.
     **
     ** @return                  the <code>authorizer</code> property this
     **                          filter <code>Builder</code>.
     **                          <br>
     **                          Possible object is {@link Authorizer} of type
     **                          <code>P</code> for the principal.
     */
    public Authorizer authorizer() {
      return this.authorizer;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a new {@link AuthenticationFilter} with the
     ** configured authenticator, authorizer, scheme and realm.
     **
     ** @return                  the {@link AuthenticationFilter} configured
     **                          with an authenticator, an authorizer, a scheme
     **                          and a realm.
     **                          <br>
     **                          Possible object is
     **                          {@link AuthenticationFilter}.
     */
    public T build() {
      return create();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: create
    /**
     ** Factory method to create a new {@link AuthenticationFilter}.
     **
     ** @return                  the {@link AuthenticationFilter}.
     **                          <br>
     **                          Possible object is
     **                          {@link AuthenticationFilter}.
     */
    protected abstract T create();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AuthenticatorFilter</code> with the an authenticator,
   ** an authorizer, a scheme and a realm.
   **
   ** @param  scheme             the <code>scheme</code> property of this
   **                            <code>AuthenticatorFilter</code>.
   **                            <br>
   **                            Allowed object is {@link Scheme}.
   ** @param  authenticator      the <code>authenticator</code> property of this
   **                            <code>AuthenticatorFilter</code>.
   **                            <br>
   **                            Allowed object is {@link Authenticator} of type
   **                            <code>C</code> for the credentials and
   **                            <code>P</code> for the principal.
   ** @param  authorizer         the <code>authorizer</code> property of this
   **                            filter <code>AuthenticatorFilter</code>.
   **                            <br>
   **                            Allowed object is {@link Authorizer} of type
   **                            <code>P</code>.
   */
  protected AuthenticationFilter(final Scheme scheme, final Authenticator<C, P> authenticator, final Authorizer<P> authorizer) {
    // ensure inheritance
    super();

    // ensure inheritance
    this.scheme        = Objects.requireNonNull(scheme,        "Scheme is not set");
    this.authenticator = Objects.requireNonNull(authenticator, "Authenticator is not set");
    this.authorizer    = Objects.requireNonNull(authorizer,    "Authorizer is not set");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scheme
  /**
   ** Returns the <code>scheme</code> property this
   ** <code>AuthenticationFilter</code>.
   **
   ** @return                    the <code>scheme</code> property this
   **                            <code>AuthenticationFilter</code>.
   **                            <br>
   **                            Possible object is {@link Scheme}.
   */
  public final Scheme scheme() {
    return this.scheme;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticator
  /**
   ** Returns the <code>authenticator</code> property this
   ** <code>AuthenticationFilter</code>.
   **
   ** @return                    the <code>authenticator</code> property this
   **                            <code>AuthenticationFilter</code>.
   **                            <br>
   **                            Possible object is {@link Authenticator} of
   **                            type <code>C</code> for the credentials and
   **                            <code>P</code> for the principal.
   */
  public final Authenticator<C, P> authenticator() {
    return this.authenticator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizer
  /**
   ** Returns the <code>authorizer</code> property thisr
   ** <code>AuthenticationFilter</code>.
   **
   ** @return                    the <code>authorizer</code> property this
   **                            <code>AuthenticationFilter</code>.
   **                            <br>
   **                            Possible object is {@link Authorizer} of type
   **                            <code>P</code> for the principal.
   */
  public final Authorizer<P> authorizer() {
    return this.authorizer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticate
  /**
   ** Authenticates a request with user credentials and setup the security
   ** context.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestContext}.
   ** @param  credential         the user credentials
   **                            <br>
   **                            Possible object is {@link Credential}.
   ** @param  scheme             the authentication scheme; one of
   **                            <ul>
   **                              <li><code>BASIC</code>
   **                              <li><code>FORM</code>
   **                              <li><code>DIGEST</code>
   **                              <li><code>CLIENT_CERT</code>
   **                            </ul>
   **                            Allowed object is {@link Scheme}.
   **
   ** @return                    <code>true</code>, if the request is
   **                            authenticated, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **                            
   ** See {@link SecurityContext}
   */
  protected boolean authenticate(final ContainerRequestContext request, final Credential credential, final Scheme scheme) {
    // prevent bogus input
    if (credential == null)
      return false;

    try {
      final Optional<P> principal = this.authenticator.authenticate(credential);
      if (!principal.isPresent())
        return false;

      final SecurityContext context = request.getSecurityContext();
      final boolean         secure  = context != null && context.isSecure();
      // apply a new security context at the incomming reuqest
      request.setSecurityContext(
        new SecurityContext() {
          @Override
          public boolean isSecure() {
            return secure;
          }
          @Override
          public String getAuthenticationScheme() {
            return scheme.id;
          }
          @Override
          public Principal getUserPrincipal() {
            return principal.get();
          }
          @Override
          public boolean isUserInRole(String role) {
            return authorizer.authorize(principal.get(), role, request);
          }
        }
      );
      return true;
    }
    catch (AuthenticationException e) {
      throw new InternalServerErrorException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unauthorized
  /**
   ** 
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestContext}.
   */
  protected void unauthorized(final ContainerRequestContext request) {
    // abort the filter chain with a 401 status code response
    // the WWW-Authenticate header is sent along with the response
    request.abortWith(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, this.scheme).build());
  }
}