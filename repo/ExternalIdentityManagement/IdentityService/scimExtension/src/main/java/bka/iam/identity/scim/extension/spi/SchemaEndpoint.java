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

    File        :   SchemaEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    SchemaEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-25-05  JLakic     First release version
*/
package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.parser.ResponseBuilder;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.resource.Group;
import bka.iam.identity.scim.extension.resource.ResourceType;
import bka.iam.identity.scim.extension.resource.Schema;
import bka.iam.identity.scim.extension.resource.User;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
////////////////////////////////////////////////////////////////////////////////
// class SchemaEndpoint
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A JAX-RS service provider implementation servicing the SCIM Schemas endpoint
 ** as defined by <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-4">RFC 7644 Section 4</a>.
 ** <br>
 ** This class provides methods for retrieving SCIM schema definitions.
 **
 ** Supported operations:
 ** - Search for schemas (GET request)
 ** - Retrieve a specific schema by ID (GET request with ID)
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/Schemas")
@Consumes({ "application/scim+json" })
@Produces({ "application/scim+json" })
public class SchemaEndpoint extends AbstractEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SchemaEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SchemaEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Retrieves the SCIM schemas based on the provided parameters.
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
   **                            SCIM schema resources matching the search
   **                            criteria.
   **
   ** @throws ScimException     if a SCIM-related error occurs.
   ** @throws ServerException   if a server-related error occurs.
   */
  @GET
  public Response search(@QueryParam("attributes") Set<String> emit, @QueryParam("excludedAttributes") Set<String> omit, @QueryParam("filter") String filter, @QueryParam("sortBy") String sortBy, @QueryParam("sortOrder") String sortOrder, @QueryParam("startIndex") Integer startIndex, @QueryParam("count") Integer count, @Context UriInfo uriInfo, @Context HttpServletRequest httpRequest, @Context SecurityContext sc, @Context Request request)
    throws ScimException {
    final String method = "search";
    this.entering(method);
    ListResponse<Schema> listResult = null;
    ListResponse<Schema> listResponse = new ListResponse<>();
    
    ObjectMapper         mapper = new ObjectMapper();
    try {
      listResult = OIMScimContext.build(this, httpRequest).searchSchema()
                                                          .invokeList(Schema.class);
      
      for (Schema schema : listResult.getResources()) {
        if (OIMSchema.getInstance().getAllSchemaURIs().contains(schema.getId()))
          listResponse.add(schema);
      }
      
      for (String schemaPath : OIMSchema.localSchemaPath) {
        File         is       = new File(httpRequest.getServletContext().getRealPath(schemaPath));
        JsonNode     jsonNode = mapper.readTree(new FileInputStream(is));
        Schema       schema   = Unmarshaller.jsonNodeToResource(jsonNode, null, Schema.class);
        listResponse.add(schema);
      }
      
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    finally {
      this.exiting(method, listResponse);
    }
    
    return ResponseBuilder.response(200, listResponse, omit, emit, count, startIndex);
  }

  /**
   ** Retrieves a user schema on the provided schema name.
   **
   ** @param id                  The ID of the schema to retrieve.
   ** @param  emit               the {@link Set} of attributes to include in the
   **                            response.
   ** @param  omit               the {@link Set} of attributes to exclude from
   **                            the response.
   ** @param  uriInfo            the {@link UriInfo} for URI-related
   **                            information.
   ** @param  httpRequest        the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param  sc                 the {@link SecurityContext} for
   **                            security-related information.
   ** @param  request            the {@link Request} for handling HTTP request.
   **
   ** @return                    the {@link Response} containing the chema.
   **                            criteria.
   **
   ** @throws ScimException      if a SCIM-related error occurs.
   ** @throws ServerException    if a server-related error occurs.
   */
  @GET
  @Path("/{id}")
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response lookup(@PathParam("id") String id,
                         @QueryParam("attributes") Set<String> emit,
                         @QueryParam("excludedAttributes") Set<String> omit,
                         @Context UriInfo uriInfo,
                         @Context HttpServletRequest httpRequest,
                         @Context SecurityContext sc,
                         @Context Request request)
  throws ScimException{

    final String method = "lookup";
    this.entering(method);
    
    ListResponse<Schema> listResult = null;
    ListResponse<Schema> listResponse = new ListResponse<>();
    
    ObjectMapper         mapper = new ObjectMapper();
    try {
      listResult = OIMScimContext.build(this, httpRequest).searchSchema()
                                                          .invokeList(Schema.class);
      
      for (Schema schema : listResult.getResources()) {
        if (OIMSchema.getInstance().getAllSchemaURIs().contains(schema.getId()))
          listResponse.add(schema);
      }
      
      for (String schemaPath : OIMSchema.localSchemaPath) {
        File         is       = new File(httpRequest.getServletContext().getRealPath(schemaPath));
        JsonNode     jsonNode = mapper.readTree(new FileInputStream(is));
        Schema       schema   = Unmarshaller.jsonNodeToResource(jsonNode, null, Schema.class);
        listResponse.add(schema);
      }
     
      
      
      if (listResponse.get(id) == null) {
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.SCHEMA_NOTFOUND, id));
      }
          
      return ResponseBuilder.response(200, listResponse.get(id), omit, emit);
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    finally {
      this.exiting(method);
    }
  }
  
   public Response lookup(@PathParam("id") String id,
                         @Context HttpServletRequest httpRequest,
                         @Context SecurityContext sc,
                         @Context Request request)
  throws ScimException{

    final String method = "lookup";
    this.entering(method);
    
    ListResponse<Schema> listResult = null;
    ListResponse<Schema> listResponse = new ListResponse<>();
    
    ObjectMapper         mapper = new ObjectMapper();
    try {
      listResult = OIMScimContext.build(this, httpRequest).searchSchema()
                                                          .invokeList(Schema.class);
      
      for (Schema schema : listResult.getResources()) {
        if (Arrays.asList(User.SCHEMAS).contains(schema.getId())) {
          listResponse.add(schema);
        } else if (Arrays.asList(Group.SCHEMAS).contains(schema.getId())) {
          listResponse.add(schema);
        } else if (Arrays.asList(ResourceType.SCHEMAS).contains(schema.getId())) {
          listResponse.add(schema);
        }
        else if (Arrays.asList(Schema.SCHEMAS).contains(schema.getId())) {
          listResponse.add(schema);
        }
      }
      
      //SCIM User application
      File         is       = new File(httpRequest.getServletContext().getRealPath("/resources/scim-user-application-schema.json"));
      JsonNode     jsonNode = mapper.readTree(new FileInputStream(is));
      Schema resourceType   = Unmarshaller.jsonNodeToResource(jsonNode, null, Schema.class);
      listResponse.add(resourceType);
      
      //SCIM Application
      is            = new File(httpRequest.getServletContext().getRealPath("/resources/scim-application-schema.json"));
      jsonNode      = mapper.readTree(new FileInputStream(is));
      resourceType  = Unmarshaller.jsonNodeToResource(jsonNode, null, Schema.class);
      listResponse.add(resourceType);
      
      //SCIM Application attribute
      is            = new File(httpRequest.getServletContext().getRealPath("/resources/scim-application-attributes-schema.json"));
      jsonNode      = mapper.readTree(new FileInputStream(is));
      resourceType  = Unmarshaller.jsonNodeToResource(jsonNode, null, Schema.class);
      listResponse.add(resourceType);
      
      //SCIM Policy
      is            = new File(httpRequest.getServletContext().getRealPath("/resources/scim-policy-schema.json"));
      jsonNode      = mapper.readTree(new FileInputStream(is));
      resourceType  = Unmarshaller.jsonNodeToResource(jsonNode, null, Schema.class);
      listResponse.add(resourceType);
      
      
      if (listResponse.get(id) == null) {
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, id));
      }
          
      return ResponseBuilder.response(200, listResponse.get(id), null, null);
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    finally {
      this.exiting(method);
    }
  }
  
  
}