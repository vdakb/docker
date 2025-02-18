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
    CLASS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   UserProfileEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    UserProfileEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/
package bka.iam.access.oidc.extension.spi;

import java.util.Map;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.HeaderParam;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import bka.iam.access.oidc.extension.Extension;

import bka.iam.access.oidc.extension.model.TokenInfo;
import bka.iam.access.oidc.extension.model.ErrorMessage;

import bka.iam.access.oidc.extension.utils.TokenUtil;
import bka.iam.access.oidc.extension.utils.UserProfileUtil;

////////////////////////////////////////////////////////////////////////////////
// class UserProfileEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The user profile endpoint returns standard and custom claims about the
 ** authenticated user; this endpoint is protected by a bearer token.
 ** <p>
 ** The user profile endpoint is an OAuth 2.0 Protected Resource that returns
 ** Claims about the authenticated End-User.
 ** <br>
 ** To obtain the requested Claims about the End-User, the Client makes a
 ** request to the user profile endpoint using an Access Token obtained through
 ** OpenID Connect Authentication. These Claims are normally represented by a
 ** JSON object that contains a collection of name and value pairs for the
 ** Claims.
 ** <p>
 ** Communication with the UserInfo Endpoint <b>must</b> utilize TLS.
 ** <p>
 ** The user profile endpoint supports the use of the HTTP GET and HTTP POST
 ** methods defined in <a href="https://openid.net/specs/openid-connect-core-1_0.html#RFC7231">RFC 7231 [RFC7231]</a>.
 ** <p>
 ** The user profile endpoint accepts Access Tokens as
 ** <a href="https://openid.net/specs/openid-connect-core-1_0.html#RFC6750">OAuth 2.0 Bearer Token Usage [RFC6750]</a>.
 ** <p>
 ** The user profile endpoint supports the use of
 ** <a href="https://openid.net/specs/openid-connect-core-1_0.html#RFC6750">Cross-Origin Resource Sharing (CORS) [CORS]</a>
 ** and/or other methods as appropriate to enable JavaScript Clients and other
 ** Browser-Based Clients to access it.
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/userprofile")
public class UserProfileEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link Class} name captured for logging purpose.
   */
  private static String    CLASS    = UserProfileEndpoint.class.getName();
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
   ** Constructs a <code>UserProfileEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserProfileEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profileGet
  /**
   ** Populates the extended token data requested by a HTTP GET Request.
   **
   ** @return                    the marshalled access token in JSON format.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response profile() {
    final String method = "profile";
    LOGGER.entering(CLASS, method);

    // prevent bogus state
    if (this.authorization == null || this.domain == null) {
      LOGGER.exiting(CLASS, method, "authorization == null || domain == null");
      return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.authorization()).build();
    }

    Response response = null;
    try {
      // remove the authorization prefix Bearer
      this.authorization = TokenUtil.tokenValue(this.authorization);
      // validate Authorization token
      TokenInfo origin = TokenUtil.info(Extension.Endpoint.INFO, this.authorization, this.domain);
      LOGGER.config("token: " + origin);
      if (origin.getIss() == null) {
        LOGGER.exiting(CLASS, method, "(tokenInfo.getIss() == null");
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.invalidRequest("Access token has expired or is not valid")).build();
      }

      final Map<String, ?> profile = UserProfileUtil.prepare(this.authorization, this.domain);
      response = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(Extension.MAPPER.writeValueAsString(profile)).build();
    }
    catch (Exception e) {
      LOGGER.throwing(CLASS, method, e);
      response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.invalidRequest(e.getMessage())).build();
    }
    LOGGER.exiting(CLASS, method, response.toString());
    return response;
  }
}