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

    File        :   DynamicClientRegistrationEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    DynamicClientRegistrationEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-07  TSebo     First release version
*/

package bka.iam.access.oidc.extension.spi;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.HttpHeaders;

import bka.iam.access.oidc.extension.Extension;
import bka.iam.access.oidc.extension.ExtensionException;

import bka.iam.access.oidc.extension.model.TokenInfo;
import bka.iam.access.oidc.extension.model.OauthClient;
import bka.iam.access.oidc.extension.model.ErrorMessage;

import bka.iam.access.oidc.extension.utils.WLSUtil;
import bka.iam.access.oidc.extension.utils.TokenUtil;
import bka.iam.access.oidc.extension.utils.OAuthClientUtil;

////////////////////////////////////////////////////////////////////////////////
// class DynamicClientRegistrationEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Dynamic Client Registration endpoint.
 ** <br>
 ** This end point can be called only in case access_token has scoper DCR.{@code <DOMAIN_NAME>}
 ** Where {@code <DOMAIN_NAME>} is OAM OAuth Domain name where we want to manage
 ** a client.
 ** <br>
 ** Supported operation are:
 ** <ol>
 **   <li>GET  - get OAuth client informations</li>
 **   <li>POST - create OAuth client</li>
 **   <li>PUT  - update OAuth client</li>
 **   <li>DELETE - delete OAuth client</li>
 ** </ol>
 **<b>Note</b>:
 ** <br>
 ** Rest Service is proxy to Admin Services which require OAM Admin username and
 ** password.
 ** <br>
 ** New credential store needs to be created in WebLogic Credential Store
 ** Framework (CSF)
 ** <pre>
 **   Map name = OAM_CONFIG
 **   Key name = oamAdmin
 ** </pre>
 ** Rest service needs to have access to weblogic credentials stored in map
 ** OAM_CONFIG
 ** <br>
 ** Following WLST command grant required permission. Incase the version is part
 ** of the deployment string 12.2.1.4 needs to be replaced with current version
 ** of the application
 ** <pre>
 **   grantPermission(codeBaseURL="file:${oracle.deployed.app.dir}/oidc.extension.access.module_12.2.1.4${oracle.deployed.app.ext}",permClass="oracle.security.jps.service.credstore.CredentialAccessPermission",permTarget="context=SYSTEM,mapName=OAM_CONFIG,keyName=*",permActions="read")
 ** </pre>
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/dcr")
public class DynamicClientRegistrationEndpoint implements Extension {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link Class} name captured for logging purpose.
   */
  private static String CLASS  = DynamicClientRegistrationEndpoint.class.getName();
  /**
   ** The {@link Logger} created based on the class name.
   */
  private static Logger LOGGER = Logger.getLogger(CLASS);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String adminURL;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DynamicClientRegistrationEndpoint</code> allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DynamicClientRegistrationEndpoint() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.adminURL = WLSUtil.adminServerURL();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOauthClient
  /**
   ** Create OAM OAuth Client and return client details.
   **
   ** @param  hh                 the HTTP Headers.
   ** @param  body               the OAuth Client details in JSON format.
   **
   ** @return                    the OAuth Client details in JSON format.
   **
   ** @see    <a href="https://docs.oracle.com/en/middleware/idm/access-manager/12.2.1.4/oroau/op-oam-services-rest-ssa-api-v1-oauthpolicyadmin-client-post.html">Add a new OAuth Client</a>
   **/
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createOauthClient(final @Context HttpHeaders hh, final String body) {

    final String method = "createOauthClient";
    LOGGER.entering(CLASS, method);

    Response response = null;
    try {
      if (isAuthorized(hh, body, null)) {
        // Create a new OAUTH Client
        response = OAuthClientUtil.create(this.adminURL, body);
        /*
        final Response oauthClientResponse = OAuthClientUtil.create(WLSUtil.getAdminsServerURL(), requestBody);
        logger.finest("oauthClientResponse status: " + oauthClientResponse.getStatus());
        logger.finest("oauthClientRespons type: " + oauthClientResponse.tygetType());
        String body = oauthClientResponse.readEntity(String.class);
        logger.finest("oauthClientResponse body " + body);
        // Return response from Create Client Internal API to caller
        response = Response.status(oauthClientResponse.getStatus())
                           .type(oauthClientResponse.getType())
                           .entity(body)
                           .build();
*/
      }
    }
    catch (ExtensionException e) {
      LOGGER.warning(e.getMessage());
      response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.invalidRequest(e.getMessage())).build();
    }
    LOGGER.exiting(CLASS, method);
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateOauthClient
  /**
   ** Update OAM OAuth Client and return client details.
   **
   ** @param  hh                 the HTTP Headers.
   ** @param  name               the optional OAuth Client name.
   ** @param  body               the client information in JSON format.
   **
   ** @return                    the OAuth Client details in JSON format.
   **
   ** @see     <a href="https://docs.oracle.com/en/middleware/idm/access-manager/12.2.1.4/oroau/op-oam-services-rest-ssa-api-v1-oauthpolicyadmin-client-put.html">Update an existing OAuth Client based on the name of the client.</a>
   **/
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@Context HttpHeaders hh, final @QueryParam("name") String name, final String body) {
    final String method = "updateOauthClient";
    LOGGER.entering(CLASS, method);

    Response response = null;
    try {
      if (isAuthorized(hh, body, null)) {
        // Update a new OAUTH Client
        response = OAuthClientUtil.update(this.adminURL, name, body);
      }
    }
    catch (ExtensionException e) {
      LOGGER.warning(e.getMessage());
      response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.invalidRequest(e.getMessage())).build();
    }
    LOGGER.exiting(CLASS, method);
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findOauthClient
  /**
   ** Find OAM OAuth Client and return client details
   ** @param hh HTTP Headers
   **
   ** @param  domain             the name of the <code>Identity Domain</code>
   **                            where client is created.
   ** @param  id                 the optional OAuth Client id.
   ** @param  name               the optional OAuth Client name.
   **
   ** @return                    the OAuth Client details in JSON format
   **
   ** @see    <a href="https://docs.oracle.com/en/middleware/idm/access-manager/12.2.1.4/oroau/op-oam-services-rest-ssa-api-v1-oauthpolicyadmin-client-get.html">Find OAuth Client by name or ID</a>
   */
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response find(@Context HttpHeaders hh, final @QueryParam("identityDomainName") String domain, final @QueryParam("id") String id, final @QueryParam("name") String name) {
    final String method = "findOauthClient";
    LOGGER.entering(CLASS, method);

    Response response = null;
    try {
      if (isAuthorized(hh, null, domain)) {
        // Find a new OAUTH Client
        response = OAuthClientUtil.find(this.adminURL, domain, id, name);
      }
    }
    catch (ExtensionException e) {
      LOGGER.warning(e.getMessage());
      response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.invalidRequest(e.getMessage())).build();
    }
    LOGGER.exiting(CLASS, method);
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOauthClient
  /**
   ** Delete OAM OAuth Client
   ** @param hh HTTP Headers
   ** @param  domain             the name of the <code>Identity Domain</code>
   **                            where client is created.
   ** @param id                  the optional OAuth Client id.
   ** @param  name               the optional OAuth Client name.
   **
   ** @return                    the response from OAM Client service
   **
   ** @see    <a href="https://docs.oracle.com/en/middleware/idm/access-manager/12.2.1.4/oroau/op-oam-services-rest-ssa-api-v1-oauthpolicyadmin-client-delete.html">Delete a Client by name or Client Id</a>
   **/
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteOauthClient(@Context HttpHeaders hh, final @QueryParam("identityDomainName") String domain, final @QueryParam("id") String id, final @QueryParam("name") String name) {
    final String method = "deleteOauthClient";
    LOGGER.entering(CLASS, method);

    Response response = null;
    try {
      if (isAuthorized(hh, null, domain)) {
        // Delete a OAUTH Client
        response = OAuthClientUtil.delete(this.adminURL, domain, id, name);
      }
    }
    catch (ExtensionException e) {
      LOGGER.warning(e.getMessage());
      response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ErrorMessage.invalidRequest(e.getMessage())).build();
    }
    LOGGER.exiting(CLASS, method);
    return response;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAuthorized
  /**
   ** Check if access token in Authorization HTTP Header is presented, valid and
   ** contains scope DCR.{@code <DOMAIN_NAME>}
   ** Where {@code <DOMAIN_NAME>} is a OAM OAUTH domain name where a new client needs to be creted, updated or deleted.
   ** @param hh Request HTTP Headers
   ** @param requestBody Reguest body in POST and PUT operation
   ** @param identityDomainName identityDomainName in GET and DELTE operation
   ** @return true in case the client is authorized to perform operation otherwise throw DynamicClientRegistrationException
   ** @throws ExtensionException if user is not authrozized to manage clients in domain
   **/
  protected boolean isAuthorized(final HttpHeaders hh, String requestBody, final String identityDomainName)
    throws ExtensionException {

    final String method = "isAuthorized";
    LOGGER.entering(CLASS, method);

    boolean authorized = false;

    String oauthIdentityDomainName = headerValue(hh.getRequestHeaders(), Header.IDENTITYDOMAIN);
    LOGGER.finest(Header.IDENTITYDOMAIN + ": " + oauthIdentityDomainName);
    try {
      // Read access_token from HTTPHeader Authorization
      final String token = TokenUtil.tokenValue(headerValue(hh.getRequestHeaders(), Header.IDENTITYDOMAIN));
      // Read clientIdDomain from parameter or input message
      String domain = identityDomainName;
      if (domain == null || domain.trim().length() < 1) {
        // read domain name from JSON body
        final OauthClient client = Extension.unmarshal(requestBody, OauthClient.class);
        domain = client.domain();
      }

      // validate Authorization token
      TokenInfo tokenInfo = TokenUtil.info(Extension.Endpoint.INFO, token, oauthIdentityDomainName);
      LOGGER.finest("tokenInfo: " + tokenInfo);

      // check if the user has right to create client in identity domain
      if (tokenInfo.has("DCR." + domain)) {
        authorized = true;
      }
      else if (tokenInfo.getIss() == null) {
        throw new ExtensionException("Access token has expired or is not valid");
      }
      else {
        throw new ExtensionException("Client is not authorized to create clients in domain " + domain);
      }
    }
    catch (Throwable  t) {
      throw new ExtensionException("Input messgae is not JSON document: " + t);
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
    return authorized;
  }
}