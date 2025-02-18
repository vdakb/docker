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

    File        :   ApplicationEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    ApplicationEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-03-17  JLakic     First release version
*/

package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.MultiValueSimpleAttribute;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;
import bka.iam.identity.scim.extension.parser.ResponseBuilder;
import bka.iam.identity.scim.extension.resource.Application;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.scimold.extension.utils.WLSUtil;
import bka.iam.identity.zero.api.AccountsFacade;
import bka.iam.identity.zero.api.AppInstanceFacade;
import bka.iam.identity.zero.api.OIMEntitlementFacade;
import bka.iam.identity.zero.model.AppEntitlements;
import bka.iam.identity.zero.model.AppInstance;
import bka.iam.identity.zero.model.EntitlementAssignee;
import bka.iam.identity.zero.model.OIMEntitlement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;

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

/**
 * SCIM endpoint for managing applications.
 */
@Path("/Applications")
@Consumes({"application/scim+json"})
@Produces({"application/scim+json"})
public class ApplicationEndpoint extends AbstractEndpoint {

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
   ** Constructs a <code>ApplicationEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ApplicationEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Retrieves a list of applications based on the provided query parameters.
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
  public Response getApplications(final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @QueryParam("filter") String filter, final @QueryParam("sortBy") String sortBy, final @QueryParam("sortOrder") String sortOrder, final @QueryParam("startIndex") Integer startIndex, final @QueryParam("count") Integer count, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws Exception {

    final String method = "getApplications";
    trace(method, SystemMessage.METHOD_ENTRY);
    ListResponse<Application> listResponse = new ListResponse<Application>();
    trace(method, SystemMessage.METHOD_EXIT);
    return ResponseBuilder.response(200, getApplications(startIndex, count, filter, null), omit, emit, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getApplication
  /**
   ** Retrieves a application based on the provided application name.
   **
   ** @param  application        the name of the application to retrieve.
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
   ** @throws ScimException          if an error occurs during the operation.
   */
  @GET
  @Path("/{application}")
  public Response getApplication(final @PathParam("application") String application, final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws ScimException {
    
    final String method = "getApplication";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final String      appFilter = "name eq \"" + application + "\"";

    ListResponse<Application> applications = getApplications(null, null, appFilter, null);
    if (applications.getResources().isEmpty()) {
      trace(method, SystemMessage.METHOD_EXIT);
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.APPLICATION_NOTFOUND, application));
    }

    trace(method, SystemMessage.METHOD_EXIT);
    return ResponseBuilder.response(200, applications.getResources().get(0), omit, emit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getApplicationEntitlementsByNamespace
  /**
   ** Retrieves a application namespace based on the provided application name
   ** and namespace.
   **
   ** @param  application        the name of the application to retrieve.
   ** @param  namespace          the entitlement namespace of the application to
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
   ** @return                    the {@link Response} containing the application
   **                            entitlements per namespace.
   **
   ** @throws ScimException      if an error occurs during the operation.
   */
   @GET
   @Path("/{application}/{namespace}")
  public Response getApplicationEntitlementsByNamespace(final @PathParam("application") String application, final @PathParam("namespace") String namespace, final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws ScimException {

    final String method = "getApplicationEntitlementsByNamespace";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String    appFilter = "name eq \"" + application + "\"";
    final String    entFilter = "namespace eq \"" + namespace + "\"";
    ListResponse<Application> applications = getApplications(null, null, appFilter, entFilter);
    final Application appFound = applications.getResources().get(0);
    if (applications.getResources().isEmpty()) {
      trace(method, SystemMessage.METHOD_EXIT);
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.APPLICATION_NOTFOUND, application));
    }
      
    Attribute namespaceFound = appFound.getNamespaces();
    if (namespaceFound.getValues().length == 0) {
      trace(method, SystemMessage.METHOD_EXIT);
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.NAMESPACE_NOTFOUND, namespace));
    }
    return ResponseBuilder.response(200, applications.getResources().get(0), omit, emit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getApplicationEntitlementByNamespace
  /**
   ** Retrieves a application namespace based on the provided application name
   ** and namespace.
   **
   ** @param  application        the name of the application to retrieve.
   ** @param  namespace          the entitlement namespace of
   **                            <code>application</code> to retrieve.
   ** @param  entitlement        the entitlement in <code>namespace</code> to
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
   ** @return                    the {@link Response} containing the entitlement
   **                            in namespace.
   **
   ** @throws ScimException      if an error occurs during the operation.
   */
  @GET
  @Path("/{application}/{namespace}/{entitlement}")
  public Response getApplicationEntitlementByNamespace(final @PathParam("application") String application, @PathParam("namespace") String namespace, @PathParam("entitlement") String entitlement, final @QueryParam("attributes") Set<String> emit, final @QueryParam("excludedAttributes") Set<String> omit, final @Context UriInfo uriInfo, final @Context HttpServletRequest httpRequest, final @Context SecurityContext sc, final @Context Request request)
    throws ScimException {

    final String method = "getApplicationEntitlementByNamespace";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String      appFilter = "name eq \"" + application + "\"";
    final String      entFilter = "namespace eq \"" + namespace + "\" AND " + "key eq \"" + entitlement + "\"";

    try {
      final ListResponse<Application> applications = getApplications(null, null, appFilter, entFilter);
      final Application appFound = applications.getResources().get(0);
      if (applications.getResources().isEmpty()) {
        trace(method, SystemMessage.METHOD_EXIT);
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.APPLICATION_NOTFOUND, application));
      }
      
      Attribute namespaceFound = appFound.getNamespaces();
      if (namespaceFound.getValues().length == 0) {
        trace(method, SystemMessage.METHOD_EXIT);
        throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.NAMESPACE_ENTITLEMENT_NOTFOUND, namespace, entitlement));
      }
      
      return ResponseBuilder.response(200, appFound, omit, emit);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }


  
  
  private ListResponse<Application> getApplications(Integer startIndex, final Integer count, String appFilter, String entFilter)
    throws ScimException {
    final String method = "getApplication";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final List<Application> resources = new LinkedList<>();
    
    int itemsPerPage = count != null ? count : 4;
    if (startIndex == null) {
      startIndex = 1;
    }

    if (appFilter != null) {
      appFilter = appFilter.replace("applicationName", "name");
    }

    oracle.hst.platform.rest.response.ListResponse<AppInstance>       appInstances = appInstanceFacade.list(startIndex, itemsPerPage, appFilter);
    
    final ObjectMapper                 mapper = new ObjectMapper();
    final Map<String, Object> appInstancesMap = mapper.convertValue(appInstances, new TypeReference<Map<String, Object>>(){});
    
    for (AppInstance appInstance : appInstances) {
      resources.add(processApplicationInstance(appInstance, entFilter));
    }
    
    trace(method, SystemMessage.METHOD_EXIT);
    return new ListResponse<Application>(resources, (Long) appInstancesMap.get(oracle.hst.platform.rest.response.ListResponse.TOTAL), startIndex, itemsPerPage);
  }
  
  
  
  private Application processApplicationInstance(AppInstance appInstance, final String entitlementFilter)
    throws ScimException {
    final String method = "processApplicationInstance";
    trace(method, SystemMessage.METHOD_ENTRY);
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Application.class);
    final SingularSimpleAttribute applicationName = new SingularSimpleAttribute("applicationName", new AttributeValue(appInstance.name()));
    final MultiValueComplexAttribute namespaces   = new MultiValueComplexAttribute("namespaces", processNamespaces(appInstance.name(), entitlementFilter).toArray(new AttributeValue[0]));
    final List<Attribute> attribute = new ArrayList<>();
    
    attribute.add(ScimResource.createSchema(Application.SCHEMAS));
    attribute.add(ScimResource.createID(appInstance.name()));
    attribute.add(ScimResource.createMeta(appInstance.createDate(), appInstance.updateDate(), getURI(WLSUtil.getServerURL(), OIMScimContext.OIM_EXTENTION_ENDPOINT_SCIM, "Applications", appInstance.name()), OIMSchema.APPLICATION));
    attribute.add(applicationName);
    attribute.add(namespaces);  
    trace(method, SystemMessage.METHOD_EXIT);
    return new Application(resourceDescriptor, attribute);
  }
  
  private List<AttributeValue> processNamespaces(final String applicationName, String entitlementFilter)
    throws ScimException {
    final String method = "processNamespaces";
    trace(method, SystemMessage.METHOD_ENTRY);
    final List<AttributeValue> namespaces = new LinkedList<>(); 

    if (entitlementFilter == null) {
        entitlementFilter = "appinstancename eq \"" + applicationName + "\"";
    }
    else {
        entitlementFilter = entitlementFilter + " AND appinstancename eq \"" + applicationName + "\"";
    }
    oracle.hst.platform.rest.response.ListResponse<AppEntitlements> appEntitlements = entitlementFacade.list(1, 100000, entitlementFilter);
    for (AppEntitlements appEntitlement : appEntitlements) {
      List<OIMEntitlement>    oimEntitlements = appEntitlement.entitlements();
      if (!oimEntitlements.isEmpty()) {
        Map<String, List<AttributeValue>> entitlementByNamespaces = processEntitlements(oimEntitlements);
        for (Map.Entry<String, List<AttributeValue>> entry : entitlementByNamespaces.entrySet()) {
          Attribute[] attributes = new Attribute[2];
          attributes[0] = new SingularSimpleAttribute("namespace", new AttributeValue(entry.getKey()));
          attributes[1] = new MultiValueComplexAttribute("entitlements", entry.getValue().toArray(new AttributeValue[0]));
          namespaces.add(new AttributeValue(attributes));
        }
      }
      
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return namespaces;
  }

  
  private Map<String, List<AttributeValue>> processEntitlements(final List<OIMEntitlement>  oimEntitlements)
    throws ScimException {
    final String method = "processEntitlements";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map<String, List<AttributeValue>> entitlementByNamespace = new HashMap<>();
    for (OIMEntitlement oimEntitlement : oimEntitlements) {
      if (entitlementByNamespace.get(oimEntitlement.namespace()) == null) {
        entitlementByNamespace.put(oimEntitlement.namespace(), new LinkedList<>());
      }
      List<AttributeValue> namespaceEntitlement = entitlementByNamespace.get(oimEntitlement.namespace());
      namespaceEntitlement.add(new AttributeValue(processEntitlementValue(oimEntitlement)));
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return entitlementByNamespace;
  }
  
  private Attribute[] processEntitlementValue(final OIMEntitlement oimEntitlement)
    throws ScimException {
     final String method = "processEntitlementValue";
    trace(method, SystemMessage.METHOD_ENTRY);
    final List<Attribute> entitlements = new LinkedList<>(); 
    
    entitlements.add(new SingularSimpleAttribute("value", new AttributeValue(oimEntitlement.key())));
    entitlements.add(new SingularSimpleAttribute("$ref", new AttributeValue(getURI(WLSUtil.getServerURL(), OIMScimContext.OIM_EXTENTION_ENDPOINT_SCIM, OIMScimContext.ENDPOINT_ENTITLEMENTS, oimEntitlement.key()))));
    
    /*entitlements.add(ScimResource.createID(oimEntitlement.key()));
    entitlements.add(new SingularSimpleAttribute("displayName", new AttributeValue(oimEntitlement.name())));
    entitlements.add(new MultiValueComplexAttribute("attributeValues", processEntitlementAttributeValue(oimEntitlement)));
    */
    trace(method, SystemMessage.METHOD_EXIT);
    return entitlements.toArray(new Attribute[0]);
  }
  
  private AttributeValue[] processEntitlementAttributeValue(final OIMEntitlement oimEntitlement) {
    final List<Attribute> attributeValues = new LinkedList<>(); 

    final List<Attribute> attributes = new LinkedList<>();
    attributes.add(new SingularSimpleAttribute("name", new AttributeValue(oimEntitlement.fieldLabel())));
    attributes.add(new SingularSimpleAttribute("value", new AttributeValue(oimEntitlement.name())));
    
    final List<AttributeValue> pair = new LinkedList<AttributeValue>(){{ add(new AttributeValue(attributes.toArray(new Attribute[0])));}};
    
    attributeValues.add(new MultiValueComplexAttribute("attributes", pair.toArray(new AttributeValue[0])));
    attributeValues.add(new MultiValueSimpleAttribute("members", processEntitlementMember(oimEntitlement.assignees())));

    final List<AttributeValue> singleAttributeValue = new LinkedList<AttributeValue>();
    singleAttributeValue.add(new AttributeValue(attributeValues.toArray(new Attribute[0])));
    
    return singleAttributeValue.toArray(new AttributeValue[0]);
  }
  
  private AttributeValue[] processEntitlementMember(List<EntitlementAssignee> entitlementAssignees) {
    final List<AttributeValue> members = new LinkedList<>(); 

    for (EntitlementAssignee entitlementAssignee : entitlementAssignees) {
      final String identity = entitlementAssignee.identity();
      if (identity != null)
        members.add(new AttributeValue(identity.toLowerCase()));
    }

    return members.toArray(new AttributeValue[0]);
  }


}