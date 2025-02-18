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

    File        :   TokenExchangeEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    TokenExchangeEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/
package bka.iam.access.oidc.extension.spi;

import java.util.LinkedList;

import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.HeaderParam;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import bka.iam.access.oidc.extension.Extension;
import bka.iam.access.oidc.extension.ProcessingException;

import bka.iam.access.oidc.extension.utils.TokenUtil;

import bka.iam.access.oidc.extension.model.ErrorMessage;
import bka.iam.access.oidc.extension.model.TokenResponse;

////////////////////////////////////////////////////////////////////////////////
// class TokenExchangeEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The token exchange endpoint is used with an extension grant type using the
 ** HTTP POST method.
 ** <p>
 ** With a rising popularity of micro-service patterns, it’s almost a given that
 ** the API being called is actually being fronted by a middleware service.
 ** Aside from load balancers, the API call will likely be passing through an
 ** API gateway like APISIX, Kong KrakenD, Tyk, etc., rather than accessing the
 ** service directly.
 ** <br>
 ** The following parameters are included in the HTTP request entity-body using
 ** the <code>application/x-www-form-urlencoded</code> format with a character
 ** encoding of UTF-8.
 ** <h3>grant_type</h3>
 ** <br>
 ** The value <code>urn:ietf:params:oauth:grant-type:token-exchange</code>
 ** indicates that a token exchange is being performed.
 ** <br>
 ** This parameter is <b>required</b>.
 ** <h3>subject_token</h3>
 ** <br>
 ** A security token that represents the identity of the party on behalf of whom
 ** the request is being made. Typically, the subject of this token will be the
 ** subject of the security token issued in response to the request.
 ** <br>
 ** This parameter is <b>required</b>.
 ** <h3>subject_token_type</h3>
 ** <br>
 ** An identifier, as described in
 ** <a href="https://www.rfc-editor.org/rfc/rfc8693#TokenTypeIdentifiers">Section 3</a>
 ** of <a href="https://www.rfc-editor.org/rfc/rfc8693#RFC8693">[RFC8693]</a>,
 ** that indicates the type of the security token in the
 ** <code>subject_token</code> parameter.
 ** <br>
 ** This parameter is <b>required</b>.
 ** <h3>actor_token</h3>
 ** <br>
 **  A security token that represents the identity of the acting party.
 ** Typically, this will be the party that is authorized to use the requested
 ** security token and act on behalf of the subject.
 ** <br>
 ** This parameter is <b>optional</b>.
 ** <h3>actor_token_type</h3>
 ** <br>
 ** An identifier, as described in
 ** <a href="https://www.rfc-editor.org/rfc/rfc8693#TokenTypeIdentifiers">Section 3</a>
 ** of <a href="https://www.rfc-editor.org/rfc/rfc8693#RFC8693">[RFC8693]</a>,
 ** that indicates the type of the security token in the
 ** <code>actor_token</code> parameter.
 ** <br>
 ** This is <b>required</b> when the <code>actor_token</code> parameter is
 ** present in the request but <b>must not</b> be included otherwise.
 ** <br>
 ** This parameter is <b>optional</b>.
 ** <h3>requested_token_type</h3>
 ** <br>
 ** An identifier, as described in
 ** <a href="https://www.rfc-editor.org/rfc/rfc8693#TokenTypeIdentifiers">Section 3</a>
 ** of <a href="https://www.rfc-editor.org/rfc/rfc8693#RFC8693">[RFC8693]</a>,
 ** for the type of the requested security token. If the requested type is
 ** unspecified, the issued token type is at the discretion of the authorization
 ** server and may be dictated by knowledge of the requirements of the service
 ** or resource indicated by the resource or audience parameter.
 ** <br>
 ** This parameter is <b>optional</b>.
 ** <h3>resource</h3>
 ** <br>
 ** A URI that indicates the target service or resource where the client intends
 ** to use the requested security token. This enables the authorization server
 ** to apply policy as appropriate for the target, such as determining the type
 ** and content of the token to be issued or if and how the token is to be
 ** encrypted. In many cases, a client will not have knowledge of the logical
 ** organization of the systems with which it interacts and will only know a URI
 ** of the service where it intends to use the token. The resource parameter
 ** allows the client to indicate to the authorization server where it intends
 ** to use the issued token by providing the location, typically as an https
 ** URL, in the token exchange request in the same form that will be used to
 ** access that resource. The authorization server will typically have the
 ** capability to map from a resource URI value to an appropriate policy. The
 ** value of the resource parameter <b>must</b> be an absolute URI, as specified
 ** by
 ** <a href="https://www.rfc-editor.org/rfc/rfc3986#section-4.3">Section 4.3</a>
 ** of <a href="https://www.rfc-editor.org/rfc/rfc8693#RFC3986">[RFC3986]</a>,
 ** that <b>may</b> include a query component and <b>must not</b> include a
 ** fragment component. Multiple resource parameters may be used to indicate
 ** that the issued token is intended to be used at the multiple resources
 ** listed.
 ** See <a href="https://www.rfc-editor.org/rfc/rfc8693#I-D.ietf-oauth-resource-indicators">[OAUTH-RESOURCE]</a>
 ** for additional background and uses of the resource parameter.
 ** <br>
 ** This parameter is <b>optional</b>.
 ** <h3>audience</h3>
 ** <br>
 ** The logical name of the target service where the client intends to use the
 ** requested security token. This serves a purpose similar to the resource
 ** parameter but with the client providing a logical name for the target
 ** service. Interpretation of the name requires that the value be something
 ** that both the client and the authorization server understand. An OAuth
 ** client identifier, a SAML entity identifier
 ** <a href="https://www.rfc-editor.org/rfc/rfc8693#OASIS.saml-core-2.0-os">[OASIS.saml-core-2.0-os]</a>,
 ** and an OpenID Connect Issuer Identifier
 ** <a href="https://www.rfc-editor.org/rfc/rfc8693#OpenID.Core">[OpenID.Core]</a>
 ** are examples of things that might be used as audience parameter values.
 ** However, audience values used with a given authorization server must be
 ** unique within that server to ensure that they are properly interpreted as
 ** the intended type of value. Multiple audience parameters may be used to
 ** indicate that the issued token is intended to be used at the multiple
 ** audiences listed. The audience and resource parameters may be used together
 ** to indicate multiple target services with a mix of logical names and
 ** resource URIs.
 ** <br>
 ** This parameter is <b>optional</b>.
 ** <h3>scope</h3>
 ** <br>
 ** A list of space-delimited, case-sensitive strings, as defined in
 ** <a href="https://www.rfc-editor.org/rfc/rfc6749#section-3.3">Section 3.3</a>
 ** of <a href="https://www.rfc-editor.org/rfc/rfc6749#RFC6749">[RFC6749]</a>,
 ** that allow the client to specify the desired scope of the requested security
 ** token in the context of the service or resource where the token will be
 ** used. The values and associated semantics of scope are service specific and
 ** expected to be described in the relevant service documentation.
 ** <br>
 ** This parameter is <b>optional</b>.
 ** <p>
 ** Communication with the Token Exchange Endpoint <b>must</b> utilize TLS.
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/token/exchange")
public class TokenExchangeEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link Class} name captured for logging purpose.
   */
  private static String    CLASS    = TokenExchangeEndpoint.class.getName();
  /**
   ** The {@link Logger} created based on the class name.
   */
  private static Logger    LOGGER   = Logger.getLogger(CLASS);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The authorization required to authenticate the request.
   */
  @HeaderParam(Extension.Header.AUTHORIZATION)
  private String authorization;

  /**
   ** The identity domain the request belongs to.
   */
  @HeaderParam(Extension.Header.IDENTITYDOMAIN)
  private String domain;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TokenExchangeEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TokenExchangeEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exchange
  /**
   ** Execute OAM RESTFull endpoint <code>/oauth2/rest/token/exchange</code> and
   ** return an issued token that is exchanged.
   **
   ** @param  form               the request body parameters to issue the token.
   **                            <br>
   **                            Allowed object is {@link MultivaluedMap} where
   **                            each element is of type {@link String} for the
   **                            key and {@link String} as the value.
   **
   ** @return                    the unmarshalled access token.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response exchange(final MultivaluedMap<String, String> form) {
    final String method = "exchange";
    LOGGER.entering(CLASS, method);
    // prevent bogus state
    if (this.authorization == null || this.domain == null) {
      LOGGER.exiting(CLASS, method, "authorization == null || domain == null");
      return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.authorization()).build();
    }
    LOGGER.info("Origin: " + form);
    try {
      final String subject = (String)((LinkedList)form.get("subject_token")).get(0);
      form.putSingle("subject_token", TokenUtil.cleanup(subject));
      // create a new Token
      TokenResponse token = TokenUtil.request(Extension.Endpoint.EXCHANGE, this.authorization, this.domain, form);
      LOGGER.finest("token: " + token);
      return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(token).build();
    }
    catch (ProcessingException e) {
      LOGGER.throwing(CLASS, method, e);
      final ErrorMessage error = e.error();
      return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(error).build();
    }
    catch (Exception e) {
      LOGGER.throwing(CLASS, method, e);
      return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.invalidRequest(e.getMessage())).build();
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
  }
}