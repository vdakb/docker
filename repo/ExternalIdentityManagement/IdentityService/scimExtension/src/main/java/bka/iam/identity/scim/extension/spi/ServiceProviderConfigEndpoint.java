/*
    Oracle Deutschland BV & Co. KG

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information"). You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2022. All Rights reserved.

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   ServiceProviderConfigEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class ServiceProviderConfigEndpoint.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-01-13  SBernet     First release version
*/

package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.parser.ResponseBuilder;
import bka.iam.identity.scim.extension.resource.ServiceProviderConfig;
import bka.iam.identity.scim.extension.rest.OIMScimContext;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
////////////////////////////////////////////////////////////////////////////////
// class ServiceProviderConfigEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** RESTful endpoint for managing SCIM Service Provider Configuration.
 ** <p>
 ** This endpoint provides operations for querying the SCIM Service Provider Configurations
 ** as defined in <a href="https://datatracker.ietf.org/doc/html/rfc7643#section-5">RFC 7643 Section 5</a>.
 ** </p>
 **
 ** <p>
 ** Key operations include:
 ** <ul>
 **   <li>GET for searching service provider configurations</li>
 ** </ul>
 ** </p>
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/ServiceProviderConfigs")
@Consumes({ "application/scim+json" })
@Produces({ "application/scim+json" })
public class ServiceProviderConfigEndpoint extends AbstractEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Retrieves the SCIM ServiceProviderConfig based on the provided parameters.
   ** <p>
   ** This method handles HTTP GET requests for querying service provider configurations
   ** according to <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.2">RFC 7644 Section 3.4.2</a>.
   ** </p>
   **
   ** @param  emit               The {@link Set} of attributes to include in the response.
   **                            If null, all attributes are returned.
   ** @param  omit               The {@link Set} of attributes to exclude from the response.
   ** @param  filter             The filter criteria for resource retrieval.
   **                            SCIM-compliant filters should be used here.
   ** @param  sortBy             The attribute by which the results should be sorted.
   ** @param  sortOrder          The sorting order (ascending or descending).
   ** @param  startIndex         The index of the first result to return.
   ** @param  count              The number of results to return.
   ** @param  uriInfo            The {@link UriInfo} containing URI-related information.
   ** @param  httpRequest        The {@link HttpServletRequest} for HTTP-related context information.
   ** @param  sc                 The {@link SecurityContext} for security-related information.
   ** @param  request            The {@link Request} representing the HTTP request.
   **
   ** @return                    A {@link Response} containing the list of matching SCIM Service Provider Configurations.
   **                            The response conforms to SCIM specifications.
   **
   ** @throws ScimException      If a SCIM protocol error occurs.
   */
  @GET
  public Response search(
      @QueryParam("attributes") Set<String> emit,
      @QueryParam("excludedAttributes") Set<String> omit,
      @QueryParam("filter") String filter,
      @QueryParam("sortBy") String sortBy,
      @QueryParam("sortOrder") String sortOrder,
      @QueryParam("startIndex") Integer startIndex,
      @QueryParam("count") Integer count,
      @Context UriInfo uriInfo,
      @Context HttpServletRequest httpRequest,
      @Context SecurityContext sc,
      @Context Request request)
    throws ScimException {

    final String method = "search";
    this.entering(method);

    // Retrieve query parameters for filtering and attribute selection
    final Map<String, Object> option = getQueryParameter(httpRequest, OIMScimContext.ATTRIBUTE_QUERY_PARAM, OIMScimContext.EXCLUDE_ATTRIBUTE_QUERY_PARAM);

    // Perform the SCIM search request through the context
    ListResponse<ServiceProviderConfig> serviceProviderConfigs = OIMScimContext.build(this, httpRequest)
                                                                               .searchServiceProviderConfigs(option)
                                                                               .invokeList(ServiceProviderConfig.class);

    // Exiting and responding with the results
    this.exiting(method, serviceProviderConfigs);
    return ResponseBuilder.response(200, serviceProviderConfigs, omit, emit, null, null);
  }
}
