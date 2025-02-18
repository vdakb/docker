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
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING, OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2022. All Rights Reserved.

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   GroupEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the SCIM-compliant GroupEndpoint class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-11-27  SBernet     First release version
*/
package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.Operation;
import bka.iam.identity.scim.extension.model.PatchRequest;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.parser.Marshaller;
import bka.iam.identity.scim.extension.parser.ResponseBuilder;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.resource.Group;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.scim.extension.rest.PATCH;
import bka.iam.identity.scim.extension.utils.PatchUtil;
import bka.iam.identity.scim.extension.utils.RoleUtil;
import bka.iam.identity.zero.api.AccountsFacade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import oracle.hst.foundation.SystemMessage;
///////////////////////////////////////////////////////////////////////////////
// class GroupEndpoint
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>GroupEndpoint</code> class provides RESTful API endpoints for
 ** managing SCIM {@link Group} resources, in compliance with RFC 7644.
 **
 ** <p>RFC 7644 specifies the System for Cross-domain Identity Management
 ** (SCIM) Protocol, which defines a RESTful interface for managing
 ** identity-related resources such as users and groups.</p>
 **
 ** <p>This class handles CRUD operations, including queries, for Group
 ** resources.</p>
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/Groups")
public class GroupEndpoint extends AbstractEndpoint {
  
  @EJB(name="AccountsFacade")
  AccountsFacade facade;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Constructor
  /**
   ** Constructs a <code>GroupEndpoint</code>, allowing its use as a JavaBean.
   ** Zero-argument constructor required by the framework.
   ** <br>
   ** Default Constructor.
   */
  public GroupEndpoint() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  ///////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** REST service search request for SCIM {@link Group} resources using GET.
   ** This method implements the SCIM specification as defined in
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.2">section 3.4.2 of RFC 7644</a>.
   **
   ** <p>Handles filtering, sorting, pagination, and attribute selection for SCIM
   ** {@link Group} resources.</p>
   **
   ** @param emit         A {@link Set} of attributes to include in the response.
   **                     <br>Allowed object is {@link Set}.
   ** @param omit         A {@link Set} of attributes to exclude from the response.
   **                     <br>Allowed object is {@link Set}.
   ** @param filter       A filter expression to match resources.
   **                     <br>Allowed object is {@link String}.
   ** @param sortBy       The attribute by which the results should be sorted.
   **                     <br>Allowed object is {@link String}.
   ** @param sortOrder    The sorting order (ascending or descending).
   **                     <br>Allowed object is {@link String}.
   ** @param startIndex   The 1-based index of the first result to return.
   **                     <br>Allowed object is {@link Integer}.
   ** @param count        The maximum number of results to return.
   **                     <br>Allowed object is {@link Integer}.
   ** @param uriInfo      The {@link UriInfo} for URI-related information.
   **                     <br>Allowed object is {@link UriInfo}.
   ** @param httpRequest  The {@link HttpServletRequest} for HTTP-related information.
   **                     <br>Allowed object is {@link HttpServletRequest}.
   **
   ** @return             A {@link Response} containing the list of SCIM {@link Group}
   **                     resources matching the search criteria.
   **                     <br>Possible object is {@link Response}.
   **
   ** @throws ScimException      If a SCIM-specific error occurs.
   */
  @GET
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response search(final @QueryParam("attributes") Set<String> emit, 
                         final @QueryParam("excludedAttributes") Set<String> omit, 
                         final @QueryParam("filter") String filter, 
                         final @QueryParam("sortBy") String sortBy, 
                         final @QueryParam("sortOrder") String sortOrder, 
                         final @QueryParam("startIndex") Integer startIndex, 
                         final @QueryParam("count") Integer count, 
                         final @Context UriInfo uriInfo, 
                         final @Context HttpServletRequest httpRequest)
    throws ScimException {

    final String method = "search";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final Map<String, Object> option = getQueryParameter(httpRequest);
    
    //ListResponse<Group> listResponse = OIMScimContext.build(this, httpRequest).searchGroups(option)
    //                                                      .invokeList(Group.class);

    final ListResponse<Group> listResponse = new ListResponse();
    final List<Group>         groups       = RoleUtil.searchRole(startIndex, count, sortOrder);
    
    for (Group group : groups) {
      listResponse.add(group);
    }
    
    if (startIndex != null)
    listResponse.setStartIndex(startIndex);
    
    listResponse.setTotalResult(RoleUtil.countRole());
    
    
    trace(method, SystemMessage.METHOD_EXIT);
    return ResponseBuilder.response(200, listResponse, omit, emit, null, null);
  }
  
  ///////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service lookup request for a SCIM {@link Group} resource using GET.
   ** This method implements the SCIM specification as defined in
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.1">section 3.4.1 of RFC 7644</a>.
   **
   ** <p>Retrieves a single {@link Group} resource by its unique identifier.</p>
   **
   ** @param id           The unique identifier of the SCIM {@link Group} resource.
   **                     <br>Allowed object is {@link String}.
   ** @param emit         A {@link Set} of attributes to include in the response.
   **                     <br>Allowed object is {@link Set}.
   ** @param omit         A {@link Set} of attributes to exclude from the response.
   **                     <br>Allowed object is {@link Set}.
   ** @param uriInfo      The {@link UriInfo} for URI-related information.
   **                     <br>Allowed object is {@link UriInfo}.
   ** @param httpRequest  The {@link HttpServletRequest} for HTTP-related information.
   **                     <br>Allowed object is {@link HttpServletRequest}.
   ** @param sc           The {@link SecurityContext} for security-related information.
   **                     <br>Allowed object is {@link SecurityContext}.
   ** @param request      The {@link Request} for handling HTTP requests.
   **                     <br>Allowed object is {@link Request}.
   **
   ** @return             A {@link Response} containing the requested SCIM {@link Group}
   **                     resource.
   **                     <br>Possible object is {@link Response}.
   **
   ** @throws ScimException      If a SCIM-specific error occurs.
   ** @throws ServerException    If a server-side error occurs.
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
                         @Context Request request
  ) throws ScimException {
    final String method = "lookup";
    this.entering(method);
    
    /*final Map<String, Object> option = getQueryParameter(httpRequest, OIMScimContext.ATTRIBUTE_QUERY_PARAM,
                                                                      OIMScimContext.EXCLUDE_ATTRIBUTE_QUERY_PARAM);
    
    Group group = OIMScimContext.build(this, httpRequest).lookupGroup(id, option);*/
    
    Group group = RoleUtil.lookupRole(id);
    
    this.exiting(method, group);
    return ResponseBuilder.response(200, group, omit, emit);
  }

  ///////////////////////////////////////////////////////////////////////////////
  // Method:   create  
  /**
   ** REST service create request for SCIM {@link Group} resources using POST
   ** (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.3">section 3.3 of RFC 7644</a>).
   ** This operation allows clients to create a new Group resource.
   **
   ** @param is                  the {@link InputStream} containing the resource
   **                            data to create.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param uriInfo             the {@link UriInfo} for URI-related information.
   ** @param httpRequest         the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param sc                  the {@link SecurityContext} for security-related
   **                            information.
   ** @param request             the {@link Request} for handling HTTP requests.
   **
   ** @return                    the {@link Response} containing the created
   **                            SCIM {@link Group} resource.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ScimException      if a SCIM-related error occurs.
   ** @throws ServerException    if a server-related error occurs.
   */
  @POST
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response create(InputStream is,
                         @Context UriInfo uriInfo,
                         @Context HttpServletRequest httpRequest,
                         @Context SecurityContext sc,
                         @Context Request request
  ) throws ScimException {
    final String method = "create";
    
    this.entering(method);
    
    final Map<String, Object> option = getQueryParameter(httpRequest);
  
    
    final ObjectMapper mapper = new ObjectMapper();
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Group.class);    
    Group group = null;
    
    try {
      group = Unmarshaller.jsonNodeToResource((JsonNode)mapper.readTree(is), resourceDescriptor , Group.class);
      
      group = OIMScimContext.build(this, httpRequest).createGroup(group, option);
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    finally {
      this.exiting(method);
    }
    return ResponseBuilder.response(201, group, null, null);
  }
  
  /**
   ** REST service delete request for SCIM {@link Group} resources using DELETE
   ** (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.6">section 3.6 of RFC 7644</a>).
   ** This operation allows clients to delete a specific Group resource by its
   ** unique identifier.
   **
   ** @param id                  the unique identifier of the SCIM {@link Group}
   **                            resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param uriInfo             the {@link UriInfo} for URI-related information.
   ** @param httpRequest         the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param sc                  the {@link SecurityContext} for security-related
   **                            information.
   ** @param request             the {@link Request} for handling HTTP requests.
   **
   ** @return                    the {@link Response} indicating the result of
   **                            the deletion operation.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ScimException      if a SCIM-related error occurs.
   */
  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") String id,
                         @Context UriInfo uriInfo,
                         @Context HttpServletRequest httpRequest,
                         @Context SecurityContext sc,
                         @Context Request request)
  throws ScimException {
    final String method = "delete";
    this.entering(method);
    
    RoleUtil.delete(id);
    
    this.exiting(method);
    return ResponseBuilder.response(HTTPContext.StatusCode.NO_CONTENT.getStatusCode());
  }
  
  /**
   ** REST service replace request for SCIM {@link Group} resources using PATCH
   ** (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2">section 3.5.2 of RFC 7644</a>).
   ** This operation allows clients to modify a Group resource by providing a
   ** Patch request.
   **
   ** @param is                  the {@link InputStream} containing the patch data.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param id                  the unique identifier of the SCIM {@link Group}
   **                            resource to replace.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param uriInfo             the {@link UriInfo} for URI-related information.
   ** @param httpRequest         the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param sc                  the {@link SecurityContext} for
   **                            security-related information.
   ** @param request             the {@link Request} for handling HTTP requests.
   **
   ** @return                    the {@link Response} containing the updated
   **                            SCIM {@link Group} resource.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ScimException      if a SCIM-related error occurs.
   */
  @PATCH
  @Path("/{id}")
  public Response replace(InputStream is,
                          @PathParam("id") String id,
                          @Context UriInfo uriInfo,
                          @Context HttpServletRequest httpRequest,
                          @Context SecurityContext sc,
                          @Context Request request)
  throws ScimException {
    final String method = "replace";
    this.entering(method);
    
    try {
      final ObjectMapper mapper       = new ObjectMapper();
      final PatchRequest patchRequest = Unmarshaller.jsonNodeToPatchRequest(mapper.readTree(is));
      
      final Group originalGroup = RoleUtil.lookupRole(id);
      
      Group patchedGroup = (Group) originalGroup.clone();
      JsonNode groupJson   = Marshaller.resourceToJsonNode(patchedGroup, null, null);
      for (Operation operation : patchRequest.getOperations()) {
        if (!operation.getPath().startsWith("members")){
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_VALUE, ScimBundle.string(ScimMessage.GROUP_OPERATION_ONLY_MEMBER));  
        }
        groupJson = PatchUtil.applyPatch(groupJson, operation);
      }
      
      RoleUtil.modify(Unmarshaller.jsonNodeToResource(groupJson, originalGroup.getResourceDescriptor(), Group.class), this.facade);
      
      final Group        group        = RoleUtil.lookupRole(id);

      return ResponseBuilder.response(200, group, null, null);
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e);
    }
    
    finally {
      this.exiting(method);
    }
  }
  
  /**
   ** REST service modify request for SCIM {@link Group} resources using PUT
   ** (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.1">section 3.5.1 of RFC 7644</a>).
   ** This operation allows clients to fully replace a Group resource.
   **
   ** @param is                  the {@link InputStream} containing the updated
   **                            resource data.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param id                  the unique identifier of the SCIM {@link Group}
   **                            resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param uriInfo             the {@link UriInfo} for URI-related information.
   ** @param httpRequest         the {@link HttpServletRequest} for
   **                            HTTP-related information.
   ** @param sc                  the {@link SecurityContext} for
   **                            security-related information.
   ** @param request             the {@link Request} for handling HTTP requests.
   **
   ** @return                    the {@link Response} containing the updated
   **                            SCIM {@link Group} resource.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ScimException      if a SCIM-related error occurs.
   */
  @PUT
  @Path("/{id}")
  public Response modify(InputStream is,
                         @PathParam("id") String id,
                         @Context UriInfo uriInfo,
                         @Context HttpServletRequest httpRequest,
                         @Context SecurityContext sc,
                         @Context Request request)
  throws ScimException {
    final String method = "modify";
    this.entering(method);
    
    final ObjectMapper mapper       = new ObjectMapper();

    Group group = null;
    try {
      final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Group.class);    
      
      group = Unmarshaller.jsonNodeToResource((JsonNode)mapper.readTree(is), resourceDescriptor, Group.class);
      group = OIMScimContext.build(this, httpRequest).modifyGroup(id, group);
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    finally {
      this.exiting(method);
    }
    
    return ResponseBuilder.response(200, group, null, null);
  }
  
  /**
   ** REST service search request for SCIM {@link Group} resources using POST
   ** (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.3">section 3.4.3 of RFC 7644</a>).
   ** This operation allows clients to perform complex searches for Group
   ** resources.
   **
   ** @param is                  the {@link InputStream} containing the search
   **                            criteria.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param uriInfo             the {@link UriInfo} for URI-related information.
   ** @param httpRequest         the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param sc                  the {@link SecurityContext} for
   **                            security-related information.
   ** @param request             the {@link Request} for handling HTTP requests.
   **
   ** @return                    the {@link Response} containing the list of
   **                            matching SCIM {@link Group} resources.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ScimException      if a SCIM-related error occurs.
   */
  @POST
  @Path("/.search")
  public Response search(InputStream is,
                         @Context UriInfo uriInfo,
                         @Context HttpServletRequest httpRequest,
                         @Context SecurityContext sc,
                         @Context Request request)
  throws ScimException {
    final String method = "search";
    this.entering(method);

    final ListResponse listResponse  = OIMScimContext.build(this, httpRequest).postSearchGroups().invoke(Group.class, is);
    
    this.exiting(method);
    return ResponseBuilder.response(200, listResponse, null, null, null, null);
  }
}
