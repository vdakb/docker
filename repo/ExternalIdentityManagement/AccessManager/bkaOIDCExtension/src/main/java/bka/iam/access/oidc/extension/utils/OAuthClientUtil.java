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

    File        :   OAuthClientUtil.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    OAuthClientUtil.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-07  TSebo     First release version
*/
package bka.iam.access.oidc.extension.utils;

import java.util.Map;

import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import bka.iam.access.oidc.extension.Extension;

////////////////////////////////////////////////////////////////////////////////
// class OAuthClientUtil
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** OAuthClient RESTFull service utility class
 ** <br>
 ** Utility class provide methods to get, create, update and delete oauth clients.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OAuthClientUtil {

	//////////////////////////////////////////////////////////////////////////////
	// static final attributes
	//////////////////////////////////////////////////////////////////////////////

  /**
   ** Class name captured for logging purpose
   */
  private static final String CLASS  = OAuthClientUtil.class.getName();
  /**
   ** Logger created based on the class name
   */
  private static final Logger LOGGER = Logger.getLogger(CLASS);

	/**
	 ** Name of the WebLoginc Credential map where are stored OAM Admin credentials
	 */
	private static final String OAM_ADMIN_CFS_MAP = "OAM_CONFIG";

	/**
	 ** Name of the WebLoginc Credential key where are stored OAM Admin credentials
	 */
	private static final String OAM_ADMIN_CFS_KEY = "oamAdmin";

	//////////////////////////////////////////////////////////////////////////////
	// Method:   create
	/**
	 ** Create OAuth Client in OAM
   **
   ** @param  rootURL            the Admin Root Server URL e.g.
   **                            https://oam.example.com:7001.
	 ** @param  body               the JSON request body.
   **
   ** @return                    the {@link Response} from OAM service.
	 **/
	public static Response create(final String rootURL, final String body) {
		final String method = "create";
		LOGGER.entering(CLASS, method);
		// Create REST Resource
		final WebTarget resource = getWebResource(rootURL + Extension.Endpoint.SERVICE);
		// Call service /oam/services/rest/ssa/api/v1/oauthpolicyadmin/client on admin server and return ClientResponse
		final Response response  = resource.request(MediaType.APPLICATION_JSON).post(Entity.json(body));
		LOGGER.exiting(CLASS, method);
		return response;
	}

	//////////////////////////////////////////////////////////////////////////////
	// Method:   find
	/**
	 ** Returns OAuth Client data from OAM.
   **
   ** @param  rootURL            the Admin Root Server URL e.g.
   **                            https://oam.example.com:7001.
	 ** @param  domain             the name of the <code>Identity Domain</code>
   **                            where client is created.
   ** @param  id                 the optional OAuth Client id.
	 ** @param  name               the optional OAuth Client name.
   **
   ** @return                    the {@link Response} from OAM service.
	 */
	public static Response find(final String rootURL, final String domain, final String id, final String name) {
		final String method = "find";
		LOGGER.entering(CLASS, method);
		// Create REST Resource
		final WebTarget resource = getWebResource(rootURL + Extension.Endpoint.SERVICE)
      .queryParam("identityDomainName", (domain == null ? "" : domain))
      .queryParam(id != null ? "id" : "name", id != null ? id : name != null ? name : "")
    ;
		// Call service /oam/services/rest/ssa/api/v1/oauthpolicyadmin/client on admin server and return ClientResponse
		final Response response  = resource.request(MediaType.APPLICATION_JSON).get();
		LOGGER.exiting(CLASS, method);
		return response;
	}

	//////////////////////////////////////////////////////////////////////////////
	// Method:   delete
	/**
	 ** Delete OAuth Client in OAM.
   **
   ** @param  rootURL            the Admin Root Server URL e.g.
   **                            https://oam.example.com:7001.
	 ** @param  domain             the name of the <code>Identity Domain</code>
   **                            where client is created.
	 ** @param id                  the optional OAuth Client id.
	 ** @param  name               the optional OAuth Client name.
   **
   ** @return                    the {@link Response} from OAM service.
	 */
	public static Response delete(final String rootURL, final String domain, final String id, final String name) {
		final String method = "delete";
		LOGGER.entering(CLASS, method);
		// Create REST Resource
		final WebTarget resource = getWebResource(rootURL + Extension.Endpoint.SERVICE)
      .queryParam("identityDomainName", (domain == null ? "" : domain))
      .queryParam("id",                 (id == null ? "" : id))
      .queryParam("name",               (name == null ? "" : name))
    ;
		// Call service /oam/services/rest/ssa/api/v1/oauthpolicyadmin/client on admin server and return ClientResponse
		final Response response  = resource.request(MediaType.APPLICATION_JSON).delete();
		LOGGER.exiting(CLASS, method);
		return response;
	}

	//////////////////////////////////////////////////////////////////////////////
	// Method:   update
	/**
	 ** Update OAuth Client in OAM.
   **
   ** @param  rootURL            the Admin Root Server URL e.g.
   **                            https://oam.example.com:7001.
	 ** @param  name               the optional OAuth Client name.
   ** @param  body               the client information in JSON format.
   **
   ** @return                    the {@link Response} from OAM service.
	 */
	public static Response update(final String rootURL, final String name, final String body) {
		final String method = "update";
		LOGGER.entering(CLASS, method);
		// Create REST Resource
		final WebTarget resource = getWebResource(rootURL + Extension.Endpoint.SERVICE)
      .queryParam("name", (name == null ? "" : name))
    ;
		// Call service /oam/services/rest/ssa/api/v1/oauthpolicyadmin/client on admin server and return ClientResponse
		final Response response = resource.request(MediaType.APPLICATION_JSON).put(Entity.json(body));
		LOGGER.exiting(CLASS, method);
		return response;
	}

	//////////////////////////////////////////////////////////////////////////////
	// Method:   getWebResource
	/**
	 ** Get Web Resource
	 ** @param rootURL Admin Root Server URL e.g. https://oam.example.com:7001
	 ** @return WebTarget object
	 **/
	private static WebTarget getWebResource(final String rootURL) {
    // create a copy of the global jersey Client Config
    final ClientConfig config = new ClientConfig(Extension.CONFIG);
    // may be the equivalent for .property(JSONConfiguration.FEATURE_POJO_MAPPING,  Boolean.TRUE)
    config.getClasses().add(JacksonJsonProvider.class);
		// fetch credentials from WebLogic CSF OAM_CONFIG map and key oamAdmin
		Map<String, String> creds = WLSUtil.getCredentialsFromCSF(OAM_ADMIN_CFS_MAP, OAM_ADMIN_CFS_KEY);
		// set Basic Authorization header
    final HttpAuthenticationFeature f = HttpAuthenticationFeature.basicBuilder().credentials(creds.get("username"), creds.get("password")).build();
    config.register(f);
		// create REST Resource
		final WebTarget resource = Extension.CLIENT.target(rootURL);
		return resource;
	}
}