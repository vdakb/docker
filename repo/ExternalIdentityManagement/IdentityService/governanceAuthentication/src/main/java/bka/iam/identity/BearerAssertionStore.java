/*
    Oracle Deutschland BV & Co. KG

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

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   BearerAssertionStore.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    BearerAssertionStore.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

import java.net.URL;

import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.AuthenticationException;

import com.nimbusds.jose.JWSAlgorithm;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;

import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.RestrictedResourceRetriever;

import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import oracle.hst.platform.core.utility.CollectionUtility;

import bka.iam.identity.jmx.BearerAsserterConfiguration;
import bka.iam.identity.jmx.BearerAsserterConfigurationMBean;

////////////////////////////////////////////////////////////////////////////////
// abstract class BearerAssertionStore
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A literal representation of the {@code BearerAssertionDefinition}.
 **
 ** @param  <T>                  the java type of the configuration the
 **                              {@code BearerAssertionDefinition} relies on.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class BearerAssertionStore<T extends BearerAsserterConfigurationMBean> extends AssertionStore<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3077605148071938628")
  private static final long   serialVersionUID = 5435131702582000757L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private ConfigurableJWTProcessor<SecurityContext> processor = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a {@link AssertionStore} that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public BearerAssertionStore() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (IdentityStore)
  /**
   ** Given a user-provided token credential, return an optional principal.
   ** <p>
   ** If the credentials are valid and map to a principal, returns a
   ** {@link CallerPrincipal}.
   **
   ** @param  credential         a user-provided token credential.
   **                            <br>
   **                            Allowed object is {@link AssertionCredential}.
   **
   ** @return                    an authenticated principal.
   **                            <br>
   **                            Possible object is {@link CallerPrincipal}.
   **
   ** @throws AssertionException if the credentials cannot be authenticated due
   **                            to an underlying error.
   */
  @Override
  public final CallerPrincipal validate(final AssertionCredential credential)
    throws AssertionException {

    // optional context parameter, not required here
    final SecurityContext ctx = null;
    try {
      // process the token
      final JWTClaimsSet claim = processor().process(credential.token, ctx);
      return authenticate(claim.getSubject()) ? new CallerPrincipal(claim.getSubject()) : null;
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      throw AssertionException.unhandled(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processor
  /**
   ** Lazy initialization of a configurable processor of
   ** {@link com.nimbusds.jwt.PlainJWT unsecured} (plain),
   ** {@link com.nimbusds.jwt.SignedJWT signed} and
   ** {@link com.nimbusds.jwt.EncryptedJWT encrypted} JSON Web Tokens (JWT).
   **
   ** @return                    the configurable processor.
   **                            <br>
   **                            Possible object is
   **                            {@link ConfigurableJWTProcessor}.
   **
   ** @throws AuthenticationException ...
   */
  private ConfigurableJWTProcessor<SecurityContext> processor()
    throws AuthenticationException {

    if (this.processor != null)
      return processor;

    // obtain the configuration
    final BearerAsserterConfiguration config = (BearerAsserterConfiguration)config();
    // create a JWT processor for the access tokens
    this.processor = new DefaultJWTProcessor<>();
    // set the required JWT claims for access tokens issued by the Access
    // Manager server, may differ with other servers
    this.processor.setJWTClaimsSetVerifier(
      BearerAssertionVerifier.build(
        // there isn't any exact match claims
        new JWTClaimsSet.Builder().build()
        // the required claims the token have to contain
      , CollectionUtility.set("iss", "sub", "iat", "jti")
        // set the accepted audience claims
      , config.getAudience()
      )
    );
    // configure the JWT processor with a key selector to feed matching public
    // RSA keys sourced from the JWK set URL with the expected JWS algorithm
    // of the access tokens
    this.processor.setJWSKeySelector(new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, serverSource()));
    return this.processor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSource
  /**
   ** Factory method to create the {@link JWKSource} containing the signature
   ** key material to verify a  JSON Web Tokens (JWT) signature.
   **
   ** @return                    the {@link JWKSource} containing the signature
   **                            key material.
   **                            <br>
   **                            Possible object is {@link JWKSource}.
   **
   ** @throws AuthenticationException ...
   */
  private JWKSource<SecurityContext> serverSource()
    throws AuthenticationException {

    try {
      return new RemoteJWKSet<SecurityContext>(new URL(config().getLocation()), retriever());
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      throw new AuthenticationException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retriever
  /**
   ** The public RSA keys to validate the signatures will be sourced from the
   ** OAuth 2.0 server's JWK set, published at a well-known URL.
   ** <br>
   ** A {@link RemoteJWKSet} caches the retrieved keys to speed up subsequent
   ** look-ups and can also handle key-rollover.
   **
   ** @return                    the retriever to lookup the public RSA keys
   **                            to validate the signatures.
   **                            <br>
   **                            Possible object is
   **                            {@link RestrictedResourceRetriever}.
   */
  private RestrictedResourceRetriever retriever() {
    // the public RSA keys to validate the signatures will be sourced from the
    // OAuth 2.0 server's JWK set, published at a well-known URL
    // the RemoteJWKSet object caches the retrieved keys to speed up subsequent
    // look-ups and can also handle key-rollover
    final Map<String, List<String>> vendor = new HashMap<>();
    vendor.put("x-oauth-identity-domain-name", Arrays.asList(config().getDomain()));
    final RestrictedResourceRetriever retriever = new DefaultResourceRetriever();
    retriever.setHeaders(vendor);
    return retriever;
  }
}