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

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   ResourceTypesEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    ResourceTypesEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-25-05  JLakic     First release version
*/
package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.parser.ResponseBuilder;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.resource.ResourceType;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

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

/**
 ** REST endpoint for SCIM Resource Types as defined by
 ** <a href="https://datatracker.ietf.org/doc/html/rfc7644">RFC 7644</a>.
 ** <br>
 ** This class provides operations to retrieve resource type definitions with extensions.
 **
 ** Supported operations:
 ** - Search for resource types (GET request)
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/ResourceTypes")
@Consumes({"application/scim+json"})
@Produces({"application/scim+json" })
public class ResourceTypesEndpoint extends AbstractEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceTypesEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ResourceTypesEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Retrieves SCIM resource types with extensions.
   **
   ** @param  emit               the {@link Set} of attributes to include in the
   **                            response.
   ** @param  omit               the {@link Set} of attributes to exclude from
   **                            the response.
   ** @param  filter             the filter criteria for user retrieval.
   ** @param  sortBy             the attribute by which the results should be
   **                            sorted.
   ** @param  sortOrder          the sorting order (ascending or descending).
   ** @param  startIndex         the index of the first result to return.
   ** @param  count              the number of results to return.
   ** @param  uriInfo            the {@link UriInfo} for URI-related
   **                            information.
   ** @param  httpRequest        the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param  sc                 the {@link SecurityContext} for
   **                            security-related information.
   ** @param  request            the {@link Request} for handling HTTP request.
   **
   ** @return                    the {@link Response} containing the list of
   **                            SCIM resource types with extensions.
   **
   ** @throws ScimException      if a SCIM-related error occurs.
   */
  @GET
  public Response search(@QueryParam("attributes") Set<String> emit, @QueryParam("excludedAttributes") Set<String> omit, @QueryParam("filter") String filter, @QueryParam("sortBy") String sortBy, @QueryParam("sortOrder") String sortOrder, @QueryParam("startIndex") Integer startIndex, @QueryParam("count") Integer count, @Context UriInfo uriInfo, @Context HttpServletRequest httpRequest, @Context SecurityContext sc, @Context Request request)
    throws ScimException {

    final String method = "search";
    this.entering(method);
    
    ListResponse<ResourceType> listResult = null;
    ListResponse<ResourceType> listResponse = new ListResponse<>();
    
    
    try {
      listResult = OIMScimContext.build(this, httpRequest).searchResourceTypes()
                                                          .invokeList(ResourceType.class);
      
      for (ResourceType resourceType : listResult.getResources()) {
        if (resourceType.getName().equals(OIMSchema.USER)) {
          resourceType.addSchemaExtensions(new URI("urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:UserApplication"), false);
          
          listResponse.add(resourceType);
        }
        else if (resourceType.getName().equals(OIMSchema.GROUP)) {
          listResponse.add(resourceType);
        }
      }
      
      for (int i = 0; i < OIMSchema.localResourceTypePath.length; i++) {
        listResponse.add(loadLocalResourceType(httpRequest, OIMSchema.localResourceTypePath[i]));
      }
      
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (URISyntaxException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    finally {
      this.exiting(method, listResponse);
    }
    return ResponseBuilder.response(200, listResponse, omit, emit, count, startIndex);
  }
  
  private ResourceType loadLocalResourceType(final HttpServletRequest httpRequest, final String path)
  throws IOException, ScimException {
    final ObjectMapper mapper   = new ObjectMapper();
    final File         is       = new File(httpRequest.getServletContext().getRealPath(path));
    final JsonNode     jsonNode = mapper.readTree(new FileInputStream(is));
    
    return Unmarshaller.jsonNodeToResource(jsonNode, null, ResourceType.class);
  }
}