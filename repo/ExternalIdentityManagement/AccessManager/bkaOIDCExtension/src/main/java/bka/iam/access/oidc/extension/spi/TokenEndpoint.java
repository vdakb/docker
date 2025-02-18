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

    File        :   TokenEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    TokenEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/
package bka.iam.access.oidc.extension.spi;

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


////////////////////////////////////////////////////////////////////////////////
// class TokenEndpoint
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The token endpoint is used to obtain tokens.
 ** <br>
 ** Tokens can either be obtained by exchanging an authorization code or by
 ** supplying credentials directly depending on what flow is used.
 ** <br>
 ** The token endpoint is also used to obtain new access tokens when they
 ** expire.
 ** <p>
 ** To obtain an Access Token, an ID Token, and optionally a Refresh Token, the
 ** Relying Party (Client) sends a Token Request to the Token Endpoint to obtain
 ** a Token Response, as described in Section 3.2 of
 ** <a href="https://openid.net/specs/openid-connect-core-1_0.html#RFC6749">OAuth 2.0 [RFC6749]</a>,
 ** when using the Authorization Code Flow.
 ** <p>
 ** Communication with the Token Endpoint <b>must</b> utilize TLS.
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/token")
public class TokenEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link Class} name captured for logging purpose.
   */
  private static final String    CLASS    = TokenEndpoint.class.getName();
  /**
   ** The {@link Logger} created based on the class name.
   */
  private static final Logger    LOGGER   = Logger.getLogger(CLASS);

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
   ** Constructs a <code>TokenEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TokenEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   issue
  /**
   ** Execute OAM RESTFull endpoint <code>/oauth2/rest/token</code> and return
   ** an issued token.
   **
   ** @param  form               the request body parameters to issue the token.
   **                            <br>
   **                            Allowed object is {@link MultivaluedMap} where
   **                            each element is of type {@link String} for the
   **                            key and {@link String} as the value.
   **
   **
   ** @return                    the unmarshalled access token.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response issue(final MultivaluedMap<String, String> form) {
    final String method = "issue";
    LOGGER.entering(CLASS, method, form);
    // prevent bogus state
    if (this.domain == null || this.domain.length() == 0) {
      LOGGER.exiting(CLASS, method, "domain is null or empty");
      return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.authorization()).build();
    }
    try {
      // create a new token
      return Response.status(Response.Status.OK)
        .type(MediaType.APPLICATION_JSON)
        // authorization contains the client credentials and the response of the
        // request method subsequently contains both tokens, where the ID token
        // also contains the at_hash value corresponding to the access token
        .entity(TokenUtil.request(Extension.Endpoint.TOKEN, this.authorization, this.domain, form))
        .build()
      ;
    }
    catch (ProcessingException e) {
      LOGGER.throwing(CLASS, method, e);
      final ErrorMessage error = e.error();
      return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(error).build()
      ;
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