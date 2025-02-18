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

    File        :   PolicyEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    PolicyEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-10-28  TSebo     First release version
*/
package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.mapper.PolicyMapper;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.Operation;
import bka.iam.identity.scim.extension.model.PatchRequest;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.parser.Marshaller;
import bka.iam.identity.scim.extension.parser.ResponseBuilder;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.resource.Policy;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.PATCH;
import bka.iam.identity.scim.extension.utils.PatchUtil;
import bka.iam.identity.zero.api.PolicyFacade;
import bka.iam.identity.zero.api.PolicyFacade.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

import java.util.Set;

import javax.ejb.EJB;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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

import oracle.iam.identity.igs.model.PolicyEntity;

@Path("/Policies")
public class PolicyEndpoint extends AbstractEndpoint {
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB(name="PolicyFacade")
  PolicyFacade facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PolicyEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PolicyEndpoint() {
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** REST service search request for SCIM {@link Policy} resources using GET 
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
   **                            SCIM {@link Policy} resources matching the search
   **                            criteria.
   **
   ** @throws ScimException      if an scim error occurs during the operation.
   */
  @GET
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response search(final @QueryParam("attributes") Set<String> emit, 
                         final @QueryParam("excludedAttributes") Set<String> omit, 
                         final @QueryParam("filter") String filter, 
                               @QueryParam("sortBy") String sortBy, 
                         final @DefaultValue("ascending") @QueryParam("sortOrder") String sortOrder, 
                         final @DefaultValue("1")  @QueryParam("startIndex") Integer startIndex, 
                         final @DefaultValue("10") @QueryParam("count") Integer count, 
                         final @Context UriInfo uriInfo, 
                         final @Context HttpServletRequest httpRequest, 
                         final @Context SecurityContext sc, 
                         final @Context Request request)throws ScimException {
    final String method = "search";
    entering(method);    
    
    ListResponse<Policy> policies;
    
    if("id".equals(sortBy)){
      sortBy = "name";
    }
    
    try{
      oracle.hst.platform.rest.response.ListResponse<PolicyEntity> policyEntities = facade.list(startIndex,count,filter,sortBy,sortOrder,true);
      policies = PolicyMapper.getPolicy(policyEntities);
      
      // Send response back to client
      return ResponseBuilder.response(HTTPContext.StatusCode.OK.getStatusCode(),policies,omit,emit, null, null);
    }
    catch(Exception e){
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,e);
    }
    finally{
      exiting(method);  
    }
    
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service request to retrieve a certain SCIM {@link Policy} resource by
   ** its <code>id</code> using GET (as per
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.1">section 3.4.1 of RFC 7644</a>)
   **
   ** @param  id                 the identifier of the OIG policy.
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
   ** @return                    the {@link Response} containing the SCIM
   **                            {@link Policy} resource.
   **
   ** @throws ScimException      if an scim error occurs during the operation.
   */
  @GET
  @Path("/{id}")
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response lookup(final @PathParam("id") String id, 
                         final @QueryParam("attributes") Set<String> emit, 
                         final @QueryParam("excludedAttributes") Set<String> omit, 
                         final @Context UriInfo uriInfo, 
                         final @Context HttpServletRequest httpRequest, 
                         final @Context SecurityContext sc, 
                         final @Context Request request)throws ScimException {

    final String method = "lookup";
    entering(method);
    
    Policy policy = null;
    
    try{
      // Call Backend API
      PolicyEntity policyEntity = this.facade.lookup(id,true);
      if (policyEntity == null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,ScimBundle.format(ScimMessage.POLICY_NOTFOUND, id) );
      }
      
      // Convert to Policy resource
      policy = PolicyMapper.getPolicy(policyEntity);
      trace(method,policy.toString());
      
      // Send response back to client
      return ResponseBuilder.response(HTTPContext.StatusCode.OK.getStatusCode(),policy,omit,emit);
    }
    catch(Exception e){
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,e);
    }
    finally{
      exiting(method);    
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** REST service request to remove a certain SCIM {@link Policy} resource by its
   ** <code>name</code> using DELETE (as per
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.6">section 3.6 of RFC 7644</a>).
   **
   ** @param  resource           The resource path.
   ** @param  name               the identifier of the policy name to remove.
   ** @param  uriInfo            the {@link UriInfo} for URI-related
   **                            information.
   ** @param  httpRequest        the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param  sc                 the {@link SecurityContext} for
   **                            security-related information.
   ** @param  request            the {@link Request} for handling HTTP request.
   **
   ** @return                    the {@link Response} indicating the success of
   **                            the delete operation.
   **
   ** @throws Exception          if an error occurs during the operation.
   */
  @DELETE
  @Path("/{name}")
  public Response delete(final @PathParam("resource") String resource, 
                         final @PathParam("name") String name, 
                         final @Context UriInfo uriInfo, 
                         final @Context HttpServletRequest httpRequest, 
                         final @Context SecurityContext sc, 
                         final @Context Request request)throws Exception {

    final String method = "delete";
    entering(method);
    
    Response response = null;
    
    PolicyEntity policyEntity =  this.facade.lookup(name,true);
    if (policyEntity == null) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.POLICY_NOTFOUND, name));
    }
    Result result=  this.facade.delete(name,true);
    if(result == Result.ok){
      response = Response.noContent().build();
    }
    else{
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.POLICY_NOTFOUND, name));
    }
     
    exiting(method);
    return response;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** REST service request to create a certain SCIM {@link User} resource using
   ** POST (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.3">section 3.3 of RFC 7644</a>)
   **
   ** @param is                  the {@link InputStream} containing the data in
   **                            SCIM format.
   ** @param  uriInfo            the {@link UriInfo} for URI-related
   **                            information.
   ** @param  httpRequest        the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param  sc                 the {@link SecurityContext} for
   **                            security-related information.
   ** @param  request            the {@link Request} for handling HTTP request.
   **
   ** @return                    the {@link Response} containing the created
   **                            user.
   ** @throws ScimException      if an scim error occurs during the operation.
   */
  @POST
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response create(final InputStream is, 
                         final @Context UriInfo uriInfo, 
                         final @Context HttpServletRequest httpRequest, 
                         final @Context SecurityContext sc, 
                         final @Context Request request) throws ScimException {
    final String method = "create";
    entering(method);
    Policy policy = null;
    PolicyEntity policyEntity = null;

    ObjectMapper mapper = new ObjectMapper();
    try {
      // Unmarshall JSON input to Policy object
      final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Policy.class);    
      policy = Unmarshaller.jsonNodeToResource((JsonNode)mapper.readTree(is), resourceDescriptor , Policy.class);  
      
      // Enforce all required attributes
      for (String requiredAttributeName : policy.getRegisterResourceDescriptor().getRequiredAttributeName()) {
       if (policy.getAttributeValue(requiredAttributeName) == null)
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.ATTRIBUTE_MANDATORY, requiredAttributeName));  
      }
      
      // Create policy with backend API
      policyEntity = PolicyMapper.getPolicyEntity(policy);
      this.facade.create(policyEntity, true);
          
      // Lookup policy with backend API
      policyEntity = this.facade.lookup(policy.getId(),true);
      if (policyEntity == null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,ScimBundle.format(ScimMessage.POLICY_NOTFOUND, policy.getId()) );
      }
      
      // Convert to Policy resource
      policy = PolicyMapper.getPolicy(policyEntity);
      trace(method,policy.toString());
      
      // Generate and send response back to client
      return ResponseBuilder.response(HTTPContext.StatusCode.CREATED.getStatusCode(),policy);
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (Exception e){
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,e);
    }
    finally {
      exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** REST service request to modify a certain SCIM {@link Policy} resource using
   ** PUT (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.1">section 3.5.1 of RFC 7644</a>)
   **
   ** @param is                  the {@link InputStream} containing the data in
   **                            SCIM format.
   ** @param  name               the identifier of the policy name to modify
   **                           
   ** @param  uriInfo            the {@link UriInfo} for URI-related
   **                            information.
   ** @param  httpRequest        the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param  sc                 the {@link SecurityContext} for
   **                            security-related information.
   ** @param  request            the {@link Request} for handling HTTP request.
   **
   ** @return                    the {@link Response} containing the created
   **                            user.
   ** @throws ScimException      if an scim error occurs during the operation.
   */
  @PUT
  @Path("/{name}")
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response modify(final InputStream is, 
                         final @PathParam("name") String name, 
                         final @Context UriInfo uriInfo, 
                         final @Context HttpServletRequest httpRequest, 
                         final @Context SecurityContext sc, 
                         final @Context Request request) throws ScimException {
    final String method = "modify";
    entering(method);

    ObjectMapper mapper = new ObjectMapper();
    try {
      // Unmarshall JSON input to Policy object
      final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Policy.class);    
      Policy policy = Unmarshaller.jsonNodeToResource((JsonNode)mapper.readTree(is), resourceDescriptor , Policy.class);  
      
      // Enforce all required attributes
      for (String requiredAttributeName : policy.getRegisterResourceDescriptor().getRequiredAttributeName()) {
       if (policy.getAttributeValue(requiredAttributeName) == null)
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.ATTRIBUTE_MANDATORY, requiredAttributeName));  
      }
      
      // Update policy with backend API
      PolicyEntity policyEntity = PolicyMapper.getPolicyEntity(policy);
      this.facade.modify(name,policyEntity, true);
          
      // Lookup policy with backend API
      policyEntity = this.facade.lookup(policy.getId(),true);
      if (policyEntity == null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,ScimBundle.format(ScimMessage.POLICY_NOTFOUND, policy.getId()) );
      }
      
      // Convert to Policy resource
      policy = PolicyMapper.getPolicy(policyEntity);
      trace(method,policy.toString());
      
      // Generate and send response back to client
      return ResponseBuilder.response(HTTPContext.StatusCode.OK.getStatusCode(),policy);
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (Exception e){
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,e);
    }
    finally {
      exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyPolicy
  /**
   ** Retrieves a application namespace based on the provided application name and namespace.
   ** PATCH (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2">section 3.5.2 of RFC 7644</a>)
   **
   ** @param  is                 the input stream containing the patch
   **                            operations.
   ** @param  name               the identifier of the policy name to modify.
   **                            modify.
   **                            the response.
   ** @param  uriInfo            the {@link UriInfo} for URI-related
   **                            information.
   ** @param  httpRequest        the {@link HttpServletRequest} for HTTP-related
   **                            information.
   ** @param  sc                 the {@link SecurityContext} for
   **                            security-related information.
   ** @param  request            the {@link Request} for handling HTTP request.
   **
   ** @return                    the {@link Response} containing the
   **                            entitlements by namespace.
   **
   ** @throws Exception          if an error occurs during the operation.
   */
  @PATCH
  @Path("/{name}")
  @Consumes({"application/scim+json"})
  @Produces({"application/scim+json"})
  public Response replace(final InputStream is, 
                               final @PathParam("name") String name, 
                               final @Context UriInfo uriInfo, 
                               final @Context HttpServletRequest httpRequest, 
                               final @Context SecurityContext sc, 
                               final @Context Request request) throws Exception {

    final String method = "replace";
    entering(method);
    PolicyEntity policyEntity = null;
    Policy policy = null;
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Policy.class);    
    
    try {
      final ObjectMapper mapper       = new ObjectMapper();
      final PatchRequest patchRequest = Unmarshaller.jsonNodeToPatchRequest(mapper.readTree(is));
      
      // Apply all patch operations
      for(Operation operation:patchRequest.getOperations()){
        // Lookup policy with backend API and convert to Policy resource
        policyEntity = this.facade.lookup(name,true);
        if (policyEntity == null) {
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,ScimBundle.format(ScimMessage.POLICY_NOTFOUND,name ) );
        }
        policy = PolicyMapper.getPolicy(policyEntity,false); // Convert to Policy resource, empty elements are returned
        
        // Apply patch in  backend
        trace(method,"Patch request:\n"+mapper.writeValueAsString(Marshaller.patchRequestToJsonNode(patchRequest)));
        trace(method,"Resource before patch:\n"+mapper.writeValueAsString(Marshaller.resourceToJsonNode(policy, null, null)) );
        JsonNode patchedNode = PatchUtil.applyPatch(Marshaller.resourceToJsonNode(policy, null, null), operation);
        trace(method,"Resource after patch:\n"+mapper.writeValueAsString(patchedNode));
        
        policy = Unmarshaller.jsonNodeToResource(patchedNode, resourceDescriptor , Policy.class);  
        
        // Enforce all required attributes
        for (String requiredAttributeName : policy.getRegisterResourceDescriptor().getRequiredAttributeName()) {
         if (policy.getAttributeValue(requiredAttributeName) == null)
          throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.ATTRIBUTE_MANDATORY, requiredAttributeName));  
        }
        
        policyEntity = PolicyMapper.getPolicyEntity(policy);
        this.facade.modify(name,policyEntity, true);
      }      
      
      // Lookup policy with backend API
      policy = PolicyMapper.getPolicy(this.facade.lookup(name,true));
      trace(method,policy.toString());
      
      // Generate and send response back to client
      return ResponseBuilder.response(HTTPContext.StatusCode.OK.getStatusCode(),policy);
    }
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (Exception e){
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,e);
    }
    finally {
      exiting(method);
    }
  } 
}