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

    File        :   EntitlementEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    EntitlementEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-12  SBernet     First release version
*/

package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.Operation;
import bka.iam.identity.scim.extension.model.Operation.OperationType;
import bka.iam.identity.scim.extension.model.PatchRequest;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;
import bka.iam.identity.scim.extension.parser.Marshaller;
import bka.iam.identity.scim.extension.parser.ResponseBuilder;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.resource.Entitlement;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.scim.extension.rest.PATCH;
import bka.iam.identity.scim.extension.utils.PatchUtil;
import bka.iam.identity.scimold.extension.utils.WLSUtil;
import bka.iam.identity.zero.api.AccountsFacade;
import bka.iam.identity.zero.api.AppInstanceFacade;
import bka.iam.identity.zero.api.OIMEntitlementFacade;
import bka.iam.identity.zero.model.AppEntitlements;
import bka.iam.identity.zero.model.OIMEntitlement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

import java.net.URI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;

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

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.AdditionalAttributeEntity;
import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.Risk;
////////////////////////////////////////////////////////////////////////////////
// class EntitlementEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** SCIM endpoint for managing entitlements.
 ** <p>
 ** This class provides the RESTful implementation for managing entitlements
 ** in compliance with SCIM standards.
 ** </p>
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/Entitlements")
@Consumes({"application/scim+json"})
@Produces({"application/scim+json"})
public class EntitlementEndpoint extends AbstractEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB(name = "AppInstanceFacade")
  AppInstanceFacade appInstanceFacade;

  @EJB(name = "AccountsFacade")
  AccountsFacade accountFacade;

  @EJB(name = "OIMEntitlementFacade")
  OIMEntitlementFacade entitlementFacade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntitlementEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Retrieves a list of entitlements based on the provided query parameters.
   **
   ** @param  emit               the {@link Set} of attributes to include in the
   **                            response.
   ** @param  omit               the {@link Set} of attributes to exclude from
   **                            the response.
   ** @param  filter             the filter criteria for application retrieval.
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
   **                            applications.
   **
   ** @throws Exception          if an error occurs during the operation.
   */
  @GET
  public Response getEntitlements(final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @QueryParam("filter") String filter, final @QueryParam("sortBy") String sortBy, final @QueryParam("sortOrder") String sortOrder, @QueryParam("startIndex") Integer startIndex, final @QueryParam("count") Integer count, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws Exception {

    final String method = "getEntitlements";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    int itemsPerPage = count != null ? count : 4;
    if (startIndex == null) {
      startIndex = 1;
    }
    
    final List<Entitlement> entitlementList = new LinkedList<Entitlement>();
    oracle.hst.platform.rest.response.ListResponse<AppEntitlements> appEntitlements = entitlementFacade.list(startIndex, itemsPerPage, null);
    for (AppEntitlements appEntitlement : appEntitlements) {
      List<OIMEntitlement>    oimEntitlements = appEntitlement.entitlements();
      if (!oimEntitlements.isEmpty()) {
        for (OIMEntitlement oimEntitlement : oimEntitlements) {
          entitlementList.add(getEntitlement(oimEntitlement.key()));
        }
      }
    }
    
    final ObjectMapper                 mapper = new ObjectMapper();
    final Map<String, Object> entInstancesMap = mapper.convertValue(appEntitlements, new TypeReference<Map<String, Object>>(){});
    
    final ListResponse<Entitlement> entitlements = new ListResponse<Entitlement>(entitlementList, (Long) entInstancesMap.get(oracle.hst.platform.rest.response.ListResponse.TOTAL), startIndex, itemsPerPage);
    
    trace(method, SystemMessage.METHOD_EXIT);
    return ResponseBuilder.response(200, entitlements, omit, emit, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlement
  /**
   ** Retrieves a specific entitlement by its identifier.e.
   **
   ** @param  id                 The unique identifier of the entitlement to
   **                            retrieve.
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
   ** @return                    the {@link Response} containing the
   **                            application.
   **
   ** @throws ScimException      if an error occurs during the operation.
   */
  @GET
  @Path("/{id}")
  public Response getEntitlement(final @PathParam("id") String id, final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws ScimException {

    final String method = "getEntitlement";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {      
      return ResponseBuilder.response(200, getEntitlement(id), omit, emit);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyEntitlementMembers
  /**
   ** TODO: Modify to get appropiate error message base on the SCIM message.
   ** Modifies entitlement members via a PATCH request.
   **
   ** @param  is                 inputStream containing the patch request
   **                            payload.
   ** @param  id                 the id of the entitlement to modify.
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
   ** @return                    the {@link Response} containing the
   **                            entitlements by namespace.
   **
   ** @throws ScimException      if an error occurs during the operation.
   */
  @PATCH
  @Path("/{id}")
  public Response modifyEntitlementMembers(final InputStream is, final @PathParam("id") String id, final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws ScimException {

    final String method = "modifyEntitlementMembers";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Entitlement.class);    
    Entitlement entitlement = null;
     try {
      final ObjectMapper mapper       = new ObjectMapper();
      final PatchRequest patchRequest = Unmarshaller.jsonNodeToPatchRequest(mapper.readTree(is));
      
      // Apply all patch operations
      for(Operation operation:patchRequest.getOperations()){
        // Lookup entitlement with backend API and convert to Policy resource
        entitlement = getEntitlement(id);
        final Map<Map<String, String>, List<String>> entitlementMembersBefore = getEntitlementsWithMember(entitlement);
        // Apply patch in  backend
        trace(method,"Patch request:\n"+mapper.writeValueAsString(Marshaller.patchRequestToJsonNode(patchRequest)));
        trace(method,"Resource before patch:\n"+mapper.writeValueAsString(Marshaller.resourceToJsonNode(entitlement, null, null)) );
        JsonNode patchedNode = PatchUtil.applyPatch(Marshaller.resourceToJsonNode(entitlement, null, null), operation);
        trace(method,"Resource after patch:\n"+mapper.writeValueAsString(patchedNode));
        
        entitlement = Unmarshaller.jsonNodeToResource(patchedNode, resourceDescriptor , Entitlement.class);  
        
        // Enforce all required attributes
        for (String requiredAttributeName : entitlement.getRegisterResourceDescriptor().getRequiredAttributeName()) {
          if (entitlement.getAttributeValue(requiredAttributeName) == null)
            throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.ATTRIBUTE_MANDATORY, requiredAttributeName));  
        }
        
        final Map<Map<String, String>, List<String>> entitlementMembersAfter = getEntitlementsWithMember(entitlement);
        
        final Map<Map<String, String>, List<String>> entitlementMemberResult =  compareEntitlementsWithMember(entitlementMembersBefore, entitlementMembersAfter, operation.getOperationType().equals(OperationType.ADD));
        
        for (Map<String, String> attributes : entitlementMemberResult.keySet()) {
          List<String> members = entitlementMemberResult.getOrDefault(attributes, new ArrayList<>());
        
        
        for (String member : members) {
          AccountEntity accountEntity = Entity.account(member);
          Map<String, List<AccountEntity>> applicationsAccounts = null;
          try {
            applicationsAccounts = this.accountFacade.listAccounts(member, null);
          }
          catch (EJBException e) {
            throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, e.getCause().getLocalizedMessage());
          }
          if (applicationsAccounts.get(entitlement.getApplication()) != null) {
            System.out.println("Found application for user");
            accountEntity = accountEntity.action(AccountEntity.Action.modify);
          }
          else {
            System.out.println("No application Found  for user");
            accountEntity = accountEntity.action(AccountEntity.Action.create); 
          }
              
          if (accountEntity != null) {
            EntitlementEntity.Action entAction = null;
            OperationType            opType    = operation.getOperationType();
            // TODO: Check again PatchOp Schema if it is really needed. 
            if (opType != null) {
              switch (opType) {
                case ADD    : entAction = EntitlementEntity.Action.assign;
                              break;
                case REPLACE:
                              entAction = EntitlementEntity.Action.modify;
                              break;
                case REMOVE :
                              entAction = EntitlementEntity.Action.revoke;
                              break;
              }
            }
            EntitlementEntity entEntity = Entity.entitlement(entAction, Risk.none);
            entEntity.putAll(attributes);
            accountEntity.namespace(Entity.namespace(entitlement.getNameSpace()).element(entEntity));
            try {
              System.out.println("ID: " + accountEntity.toString());
              System.out.println("Action: " + accountEntity.action().toString());
              System.out.println("Attribute: " + accountEntity.attribute());
              System.out.println("Form: " + accountEntity.accountForm());
              this.accountFacade.processAccountEntity(accountEntity, entitlement.getApplication(), sc.getUserPrincipal().getName());
            }
            catch (EJBException e) {
              throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, e.getCause().getLocalizedMessage());
            }
            catch (IllegalArgumentException e) {
              throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, e.getCause().getLocalizedMessage());
            }
          }
        }
        
        //this.accountFacade.processAccountEntity(accountEntity, entitlement.getApplication(), sc.getUserPrincipal().getName());
        //policyEntity = PolicyMapper.getPolicyEntity(policy);
        //this.facade.modify(name,policyEntity, true);
        } 
      }
      
      // Lookup entitlement with backend API
      entitlement = getEntitlement(id);
      trace(method,entitlement.toString());
      
      
      
      // Generate and send response back to client
      return ResponseBuilder.response(HTTPContext.StatusCode.OK.getStatusCode(), entitlement);
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
  
  private List<String> getEntitlementMembers(final Entitlement entitlement) {
    final List<String>        members = new ArrayList<String>();
    final Attribute   attributeValues = entitlement.getAttributeValues();
        for (AttributeValue subAttribute : attributeValues.getValues()) {
          final Attribute memberAttributes = subAttribute.getSubAttribute("members");
          final AttributeValue[]    member = memberAttributes.getValues();
          for (AttributeValue attributeMember : member) {
            members.add(attributeMember.getSubAttributeStringValue("value"));
          }
        }
        
    return members;
  }
  
  public static Map<Map<String, String>, List<String>> compareEntitlementsWithMember(final Map<Map<String, String>, List<String>> entitlementMappings1, final Map<Map<String, String>, List<String>> entitlementMappings2, final Boolean addedResult) {

    final Map<Map<String, String>, List<String>> resultMap = new HashMap<>();

    // Compare entitlements
    for (Map<String, String> attributes : entitlementMappings1.keySet()) {
      List<String> members1 = entitlementMappings1.getOrDefault(attributes, new ArrayList<>());
      List<String> members2 = entitlementMappings2.getOrDefault(attributes, new ArrayList<>());

      Set<String> addedMembers   = new HashSet<>(members2);
      Set<String> removedMembers = new HashSet<>(members1);

      addedMembers.removeAll(members1);  // Members present in new map but not in old
      removedMembers.removeAll(members2); // Members present in old map but not in new

      if (addedResult && !addedMembers.isEmpty()) {
        resultMap.put(attributes, new ArrayList<>(addedMembers));
      }
      else if (!removedMembers.isEmpty()) {
        resultMap.put(attributes, new ArrayList<>(removedMembers));
      }
    }

    if (addedResult) {
      // Add new entitlements that didn't exist before
      for (Map<String, String> attributes : entitlementMappings2.keySet()) {
        if (!entitlementMappings1.containsKey(attributes)) {
          resultMap.put(attributes, new ArrayList<>(entitlementMappings2.get(attributes)));
        }
      }
    } else {
      
      for (Map<String, String> attributes : entitlementMappings1.keySet()) {
        if (!entitlementMappings2.containsKey(attributes)) {
          resultMap.put(attributes, new ArrayList<>(entitlementMappings1.get(attributes)));
        }
      }
    }

    return resultMap;
  }
  
  private Map<Map<String, String>, List<String>> getEntitlementsWithMember(final Entitlement entitlement) {
    Map<Map<String, String>, List<String>> entitlementMappings = new HashMap<>();

    if (entitlement == null || entitlement.getAttributeValues() == null) {
      return entitlementMappings;
    }

    final AttributeValue[] attributeValues = entitlement.getAttributeValues().getValues();
    if (attributeValues == null)
      return entitlementMappings;

    for (AttributeValue attributeValue : attributeValues) {
      Map<String, String> attributeKeyMap = new HashMap<>();
      List<String> memberIds = new ArrayList<>();

      // Process attributes
      final Attribute attributes = attributeValue.getSubAttribute("attributes");
      if (attributes != null && attributes.getValues() != null) {
        for (AttributeValue keyValue : attributes.getValues()) {
          final String name = keyValue.getSubAttributeStringValue("name");
          final String value = keyValue.getSubAttributeStringValue("value");
          if (name != null && value != null) {
            attributeKeyMap.put(name, value);
          }
        }
      }

      // Process members
      final Attribute members = attributeValue.getSubAttribute("members");
      if (members != null && members.getValues() != null) {
        for (AttributeValue member : members.getValues()) {
          final String memberID = member.getSubAttributeStringValue("value");
          if (memberID != null) {
            memberIds.add(memberID);
          }
        }
      }

      entitlementMappings.put(attributeKeyMap, memberIds);
    }

    return entitlementMappings;
  }

  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlements
  /**
   ** Retrieves all entitlements available in the system.
   **
   ** @return                   A list of entitlements wrapped in a
   **                          ListResponse.
   ** @throws ScimException    if a SCIM error occurs.
   */
  private ListResponse<Entitlement> getEntitlements()
    throws ScimException {
    final String method = "getEntitlement";
    trace(method, SystemMessage.METHOD_ENTRY);
    final ListResponse<Entitlement> entitlements = new ListResponse<Entitlement>();
    oracle.hst.platform.rest.response.ListResponse<AppEntitlements> appEntitlements = entitlementFacade.list(0, 1000000, null);
    for (AppEntitlements appEntitlement : appEntitlements) {
      List<OIMEntitlement>    oimEntitlements = appEntitlement.entitlements();
      if (!oimEntitlements.isEmpty()) {
        for (OIMEntitlement oimEntitlement : oimEntitlements) {
          entitlements.add(getEntitlement(oimEntitlement.key()));
        }
      }
    }
    
    trace(method, SystemMessage.METHOD_EXIT);
    return entitlements;
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlements
  /**
   ** Retrieves a single entitlement based on the provided key.
   **
   ** @param   key             The unique identifier of the entitlement.
   ** 
   ** @return                  A required entitlements.
   ** 
   ** @throws ScimException    if a SCIM error occurs.
   */
  private Entitlement getEntitlement(final String key)
    throws ScimException {
    
    final String method = "getEntitlement";
    trace(method, SystemMessage.METHOD_ENTRY);
    EntitlementEntity entitlement = entitlementFacade.lookup(key, "");
    OIMEntitlement oimEntitlement = entitlementFacade.lookup(key);
    String displayName = null;
    for (Map.Entry<String, Object> cursor : entitlement.entrySet()) {
      displayName = (String) cursor.getValue();
    }
    
    if (entitlement == null) {
      trace(method, SystemMessage.METHOD_EXIT);
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.ENTITLEMENT_NOTFOUND, key));
    }
    final List<Attribute> attribute = new ArrayList<>();
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Entitlement.class);
    attribute.add(ScimResource.createID(entitlement.id()));
    attribute.add(ScimResource.createSchema(Entitlement.SCHEMAS));
    attribute.add(ScimResource.createMeta(entitlement.createDate(), entitlement.updateDate(), URI.create(WLSUtil.getServerURL() + OIMScimContext.OIM_EXTENTION_ENDPOINT_SCIM + "/" + OIMScimContext.ENDPOINT_ENTITLEMENTS + "/" + entitlement.id()), OIMSchema.ENTITLEMENT));
    attribute.add(new SingularSimpleAttribute("displayName", new AttributeValue(entitlement.displayName())));
    attribute.add(new MultiValueComplexAttribute("attributeValues", processEntitlementAttributeValue(entitlement)));  
    attribute.add(new SingularSimpleAttribute("namespace", new AttributeValue(oimEntitlement.namespace())));
    attribute.add(new SingularSimpleAttribute("application", new AttributeValue(oimEntitlement.appInstanceName())));
    
    trace(method, SystemMessage.METHOD_EXIT);
    return new Entitlement(resourceDescriptor, attribute);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   processEntitlementAttributeValue
  /**
   ** Processes the attributes of an entitlement and converts them into SCIM-compliant
   ** AttributeValue objects.
   **
   ** @param entitlementEntity   The OIMEntitlement entity.
   ** 
   ** @return                    An array of AttributeValue representing the
   **                            entitlement attributes.
    */
  private AttributeValue[] processEntitlementAttributeValue(final EntitlementEntity entitlementEntity)
    throws ScimException {
    final List<AttributeValue> singleAttributeValue = new LinkedList<AttributeValue>();
    
    
    for (AdditionalAttributeEntity additionalAttribute : entitlementEntity.additionalAttributes()) {
      final List<Attribute> attributeValues = new LinkedList<>(); 
      for (Map.Entry<String, Object> cursor : additionalAttribute.attribute().entrySet()) {
        final List<Attribute> attributes = new LinkedList<>();
        attributes.add(new SingularSimpleAttribute("name", new AttributeValue(cursor.getKey())));
        attributes.add(new SingularSimpleAttribute("value", new AttributeValue(cursor.getValue())));
        
        final List<AttributeValue> pair = new LinkedList<AttributeValue>(){{ add(new AttributeValue(attributes.toArray(new Attribute[0])));}};
        attributeValues.add(new MultiValueComplexAttribute("attributes", pair.toArray(new AttributeValue[0])));
      }
      attributeValues.add(new MultiValueComplexAttribute("members", processEntitlementMember(additionalAttribute.members())));
      singleAttributeValue.add(new AttributeValue(attributeValues.toArray(new Attribute[0])));
    }
    return singleAttributeValue.toArray(new AttributeValue[0]);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   processEntitlementMember
  /**
   ** Processes the members associated with the entitlement.
   **
   ** @param entitlementMember         The entitlement containing members to
   **                                  process.
   ** 
   ** @return                          An array of AttributeValue representing
   **                                  the entitlement members.
  */
  private AttributeValue[] processEntitlementMember(List<String> entitlementMember)
    throws ScimException {
    final List<AttributeValue> members = new LinkedList<>(); 

    for (String memberId : entitlementMember) {
      final String identity = memberId;
      if (identity != null) {
        final List<Attribute> member = new LinkedList<>();
        member.add(new SingularSimpleAttribute("value", new AttributeValue(identity.toLowerCase())));
        member.add(new SingularSimpleAttribute("$ref", new AttributeValue(getURI(WLSUtil.getServerURL(), OIMScimContext.OIM_EXTENTION_ENDPOINT_SCIM, OIMScimContext.ENDPOINT_USERS, identity.toLowerCase()))));
        members.add(new AttributeValue(member.toArray(new Attribute[0])));
      }
    }

    return members.toArray(new AttributeValue[0]);
  }
}