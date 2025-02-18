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

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   TokenUtil.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    TokenUtil.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/
package bka.iam.access.oidc.extension.utils;

import java.util.Map;
import java.util.Arrays;
import java.util.Base64;
import java.util.ArrayList;
import java.util.Collection;

import java.util.logging.Logger;

import java.util.regex.Pattern;

import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;

import oracle.security.restsec.jwt.JwtToken;

import bka.iam.access.oidc.extension.Extension;
import bka.iam.access.oidc.extension.Extension.GrantType;
import bka.iam.access.oidc.extension.ProcessingException;

import bka.iam.access.oidc.extension.model.TokenInfo;
import bka.iam.access.oidc.extension.model.ErrorMessage;
import bka.iam.access.oidc.extension.model.TokenResponse;

////////////////////////////////////////////////////////////////////////////////
// class TokenInfoUtil
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TokenUtil {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Class name captured for logging purpose
   */
  private static final String CLASS  = TokenUtil.class.getName();
  /**
   ** Logger created based on the class name
   */
  private static Logger      LOGGER  = Logger.getLogger(CLASS);
  /**
   ** The bit length of an IDToken hash value.
   */
  private static int         BITS    = 128;
  /**
   ** The regular expression to match exactly the oidc scope in the scope.
   */
  private static Pattern     PATTERN = Pattern.compile("\\bopenid\\b");

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Execute OAM RESTFull endpoint /oauth2/rest/token/info and return data in
   ** POJO object TokenInfo
   **
   ** @param  endpoint           the OAM endpoint URL that provides token info.
   ** @param  accessToken        the access token string issued by
   **                            <code>IdentityDomain</code>
   **                            <code>domain</code>.
   ** @param  domain             the name of the <code>IdentityDomain</code>
   **                            that issued <code>accessToken</code>.
   **
   ** @return                    the unmarshalled <code>accessToken</code>
   **                            wrapped for convinience in {@link TokenInfo}.
   **
   ** @throws Exception          in case any error occured.
   */
  public static TokenInfo info(final WebTarget endpoint, final String accessToken, final String domain)
    throws Exception {

    final String method = "info";
    LOGGER.entering(CLASS, method);
    try {
      // call end point
      final Response r = endpoint
        .queryParam("access_token", accessToken)
        .request(MediaType.APPLICATION_JSON)
        .header(Extension.Header.IDENTITYDOMAIN, domain)
        .get();
      LOGGER.finest("Response status of token info service is: " + r.getStatusInfo().getFamily());
      if (Response.Status.Family.SUCCESSFUL == r.getStatusInfo().getFamily()) {
        return r.readEntity(TokenInfo.class);
      }
      else {
        throw ProcessingException.of(r.readEntity(ErrorMessage.class));
      }
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Execute OAM RESTFull <code>endpoint</code> and return data in POJO object
   ** {@link TokenResponse}.
   **
   ** @param  endpoint           the OAM endpoint URL that issues a token.
   ** @param  authorization      the OAuth client authorization string.
   ** @param  domain             the name of the <code>IdentityDomain</code>
   **                            that shuold issue the access token.
   ** @param  form               the request body parameters to request the
   **                            access token.
   **
   ** @return                    the unmarshalled access token wrapped for
   **                            convinience in {@link TokenResponse}.
   **
   ** @throws Exception          in case any error occured.
   */
  public static TokenResponse request(final WebTarget endpoint, final String authorization, final String domain, final MultivaluedMap<String, String> form)
    throws Exception {

    final String method = "request";
    LOGGER.entering(CLASS, method);
    TokenResponse response = null;
    try {
      // prepare end point
      final Invocation.Builder b = endpoint.request(MediaType.APPLICATION_JSON);
      b.header(Extension.Header.IDENTITYDOMAIN, domain);
      if (authorization != null && authorization.length() > 0) {
        b.header(Extension.Header.AUTHORIZATION,  authorization);
      }
      // call end point
      final Response r = b.post(Entity.form(new Form(form)));
      LOGGER.finest("Response status of token service is: " + r.getStatusInfo().getFamily());
      if (Response.Status.Family.SUCCESSFUL == r.getStatusInfo().getFamily()) {
        response = r.readEntity(TokenResponse.class);
        // it can be assumed if grant_type is client credential that the
        // correspondending client id is not a regular user because it belongs
        // only to the OAuth configuration hence populating group memberships
        // is useless
        final GrantType grantType = GrantType.from(form.getFirst("grant_type"));
        if (grantType.equals(GrantType.AUTHORIZATION) || grantType.equals(GrantType.REFRESH) || grantType.equals(GrantType.TOKEN_EXCHANGE)) {
          memberOf(response);
          // handle the id token if its scoped
          if (PATTERN.matcher(response.getScope()).find()) {
            LOGGER.finest("Id token hash value (at_hash) is required.");
            response.setId_token(hashToken(domain, response.getId_token(), response.getAccess_token()));
          }
        }
      }
      else {
        throw ProcessingException.of(r.readEntity(ErrorMessage.class));
      }
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanup
  /**
   ** Removes the goup claim from a token
   **
   ** @param  value              the token string where the group claim have to
   **                            removed from.
   **
   ** @return                    the token string where the group claim is
   **                            removed from.
   **                            
   ** @throws Exception          if <code>value</code> does not represent a
   **                            valid access token or if key algorithm or key
   **                            specification required by the signing the
   **                            changed access token isn't supported.
   */
  public static String cleanup(final String value)
    throws Exception {

    final String method = "cleanup";
    LOGGER.entering(CLASS, method);
    try {
      JwtToken token = new JwtToken(value);
      LOGGER.info("Removing groups from token: " + token);

      final Map<String, Object> header    = token.getHeaderParameters();
      final Map<String, Object> claims    = token.getClaimParameters();
      final String              domain    = (String)claims.get("domain");
      final Collection<String>  scopes    = (Collection<String>)claims.get("scope");
      final Collection<String>  resources = OAuthUtil.resourceServerFromScope(scopes);
      final Collection<String>  attribute = OAuthUtil.resourceServerGroupAttribute(domain, resources);
      for (String cursor : attribute) {
        claims.remove(cursor);
      }
      return createToken(domain, header, claims);
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenValue
  /**
   ** Remove Bearer string from Authorization token
   **
   ** @param  value              the token string where the group claim have to
   **                            removed from.
   **
   ** @return                    the token string where the group claim is
   **                            removed from.
   **                            
   ** @throws Exception          if <code>value</code> does not represent a
   **                            valid access token or if key algorithm or key
   **                            specification required by the signing the
   **                            changed access token isn't supported.
   */
  public static String tokenValue(final String value)
    throws Exception {

    // prevent bogus input
    if (value != null && value.length() > 0) {
      // remove Bearer string from Authorization token
      return value.replaceFirst("Bearer ", "");
    }
    else {
      throw ProcessingException.of(ErrorMessage.authorization());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenValue
  /**
   ** Access Token hash value (at_hash).
   ** <br>
   ** Its value is the base64url encoding of the left-most half of the hash of
   ** the octets of the ASCII representation of the access_token value, where
   ** the hash algorithm used is the hash algorithm used in the alg Header
   ** Parameter of the ID Token's JOSE Header.
   ** <br>
   ** For instance, if the alg is RS256, hash the access_token value with
   ** SHA-256, then take the left-most 128 bits and base64url-encode them.
   ** <br>
   ** The at_hash value is a case-sensitive string.
   ** 
   ** @param  value              the string representation of a signed access
   **                            token.
   **
   ** @return                    the hash value for the signed access token.
   **                            
   ** @throws Exception          if hash algorithm isn't supported.
   */
  public static String tokenHash(final String value)
    throws Exception {

    final String method = "tokenHash";
    LOGGER.entering(CLASS, method);
    try {
      final byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
      final int    length = Math.min(digest.length, BITS / 8);
      final byte[] left   = Arrays.copyOfRange(digest, 0, length);
      return new String(Base64.getUrlEncoder().withoutPadding().encode(left));
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOf
  /**
   ** Populates the collection of groups belonging to the specified
   ** <code>token</code>.
   ** <p>
   ** The implementation obtains the {@link UserIdentityProvider} from the
   ** configuration mapped at <code>identityStore</code>.
   ** <p>
   ** The collection of <code>ResourceServer</code> in scope are examined by
   ** <code>resources</code>.
   **
   ** @param  username           the name of the user to locate in the
   **                            <code>IdentityStore</code configured at
   **                            <code>identityStore</code>.
   ** @param  identityStore      the name of the <code>IdentityStore</code>
   **                            providing access to the underlying directory.
   ** @param  resources          the colection of <code>ResourceServer</code>s
   **                            to examine.
   **
   ** @return                    the collection of simple groups names.
   **                            
   ** @throws Exception          if <code>response</code> does not provide a
   **                            valid access token or if key algorithm or key
   **                            specification required by the signing the
   **                            changed access token isn't supported.
   */
  private static void memberOf(final TokenResponse response)
    throws Exception {

    final String method = "memberOf";
    LOGGER.entering(CLASS, method);
    try {
      final JwtToken            token   = new JwtToken(response.getAccess_token());
      final Map<String, Object> header  = token.getHeaderParameters();
      final Map<String, Object> claims  = token.getClaimParameters();
      final String              domain  = (String)claims.get("domain");
      final Collection<String>  scopes  = new ArrayList<String>();
      final Object              request = claims.get("scope");
      if (request instanceof String) {
        scopes.addAll(Arrays.asList(((String)request).split(" ")));
      }
      else {
        scopes.addAll(Collection.class.cast(request));
      }

      final Collection<String> resources  = OAuthUtil.resourceServerFromScope(scopes);
      final Collection<String> attributes = OAuthUtil.resourceServerGroupAttribute(domain, resources);

      if (!attributes.isEmpty()) {
        final String provider = OAuthUtil.domainIdentityStore(domain);
        for (String cursor : attributes) {
          claims.put(cursor, UserProfileUtil.memberOf(token.getSubject(), provider, resources));
        }
      }
      response.setAccess_token(createToken(domain, header, claims));
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createToken
  /**
   ** Factory method to create asigned access token string. populated with the
   ** header and claim segments.
   **
   ** @param  domain             the name of the <code>IdentityDomain</code>
   **                            that issued <code>token</code>.
   ** @param  header             the key-value pairs of the header segment.
   ** @param  claim              the key-value pairs of the claim segment.
   **
   ** @return                    the signed access token string.
   **                            
   ** @throws Exception          if key algorithm or key specification required
   **                            by the signing procedure isn't supported.
   */
  private static String createToken(final String domain, final Map<String, Object> header, final Map<String, Object> claim)
    throws Exception {

    final String method = "createToken";
    LOGGER.entering(CLASS, method);
    final JwtToken token = new JwtToken();
    for (Map.Entry<String, Object> entry : header.entrySet()) {
      token.setHeaderParameter(entry.getKey(), entry.getValue());
    }
    for (Map.Entry<String, Object> entry : claim.entrySet()) {
      token.setClaimParameter(entry.getKey(), entry.getValue());
    }
    try {
      return token.signAndSerialize(OAuthUtil.domainPrivateKey(domain));
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashToken
  /**
   ** Evaluate the at_hash claim of an id token for sthe specified access token.
   **
   ** @param  domain             the name of the <code>IdentityDomain</code>
   **                            that issued <code>token</code>.
   ** @param  identity           the original id token recied from the OAuth
   **                            endpoint of the Access Server.
   ** @param  access             the access token that might be changed by
   **                            population the memberships of the user subject.
   **
   ** @return                    the signed id token string.
   **                            
   ** @throws Exception          if key algorithm or key specification required
   **                            by the signing procedure isn't supported.
   */
  private static String hashToken(final String domain, final String identity, final String access)
    throws Exception {

    final String method = "hashToken";
    LOGGER.entering(CLASS, method);
    try {
      final JwtToken  idToken = new JwtToken(identity);
      idToken.setClaimParameter("at_hash", tokenHash(access));
      return idToken.signAndSerialize(OAuthUtil.domainPrivateKey(domain));
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
  }
}