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

    Copyright � 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   OIMScimContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class OIMScimContext.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-11-26  SBernet     First release version
*/
package bka.iam.identity.scim.extension.rest;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.PatchRequest;
import bka.iam.identity.scim.extension.model.SchemaDescriptor;
import bka.iam.identity.scim.extension.resource.Group;
import bka.iam.identity.scim.extension.resource.User;
import bka.iam.identity.scim.extension.rest.operation.Delete;
import bka.iam.identity.scim.extension.rest.operation.Get;
import bka.iam.identity.scim.extension.rest.operation.Patch;
import bka.iam.identity.scim.extension.rest.operation.Post;
import bka.iam.identity.scim.extension.rest.operation.Put;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import oracle.hst.foundation.logging.Loggable;
////////////////////////////////////////////////////////////////////////////////
// class OIMScimContext
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This class is responsible for managing HTTP operations for
 ** SCIM resources such as Users and Groups .
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OIMScimContext extends ServiceClient {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // Root context for OIM native SCIM API
  public static final String     OIM_ENDPOINT_SCIM                 = "/iam/governance/scim/v1";
  
  // Root context for OIM native SCIM API
  public static final String     OIM_EXTENTION_ENDPOINT_SCIM       = "/igs/scim/v2";
  
  // Endpoint for ServiceProviderConfigs resources
  public static final String     ENDPOINT_SERVICEPROVIDERCONFIG    = "ServiceProviderConfigs";
  
  // Endpoint for ServiceProviderVersion resources
  public static final String     ENDPOINT_SERVICEPROVIDERVERSION   = "ServiceProviderVersion";
  
  // Endpoint for SCIM Schemas
  public static final String     ENDPOINT_SCHEMAS                  = "Schemas";
  
  // Endpoint for SCIM Resource Types
  public static final String     ENDPOINT_RESOURCETYPE             = "ResourceTypes";
    
  // Endpoint for User resources
  public static final String     ENDPOINT_USERS                    = "Users";

  // Endpoint for Group resources
  public static final String     ENDPOINT_GROUPS                   = "Groups";
  
  // Endpoint for Applications resources
  public static final String     ENDPOINT_APPLICATIONS             = "Applications";
  
  // Endpoint for Application Attributes resources
  public static final String     ENDPOINT_APPLICATION_ATTRIBUTES   = "ApplicationAttributes";
  
  // Endpoint for Entitlements resources
  public static final String     ENDPOINT_ENTITLEMENTS             = "Entitlements";
  
  // Endpoint for Policies resources
  public static final String     ENDPOINT_POLICIES                 = "Policies";
  
  // Endpoint for SCIM Search
  public static final String     ENDPOINT_SEARCH                   = ".search";

  // Query parameter name to lookup for resource attribute
  public static final String     ATTRIBUTE_QUERY_PARAM             = "attributes";
  
  // Query parameter name to exclude resource attribute
  public static final String     EXCLUDE_ATTRIBUTE_QUERY_PARAM     = "excludedAttributes";
    
  // Query parameter name to filter resources
  public static final String     FILTER_ATTRIBUTE_QUERY_PARAM      = "filter";
  
  // Query parameter for start index
  public static final String     START_ATTRIBUTE_QUERY_PARAM       = "startIndex";
  
  // Query parameter for count
  public static final String     COUNT_ATTRIBUTE_QUERY_PARAM       = "count";
  
  // Query parameter for sort order
  public static final String     SORT_ORDER_ATTRIBUTE_QUERY_PARAM  = "sortOrder";
  
  // Query parameter for sort by attribute
  public static final String     SORT_BY_ATTRIBUTE_QUERY_PARAM     = "sortBy";
  
  /// Header for Content Type
  public static final String     CONTENT_TYPE_HEADER               = "Content-Type";
  
  // Header for Authorization
  public static final String     AUTHORIZATION_HEADER              = "Authorization";
  
  // Header for X-Requested-By
  public static final String     X_REQUEST_BY_HEADER               = "X-Requested-By";
  
  // Default Content Type for SCIM API
  public static final Integer    COUNT_ATTRIBUTE_DEFAULT           = 500;
  
  // Default Content Type for SCIM API
  public static final String     CONTENT_TYPE_HEADER_DEFAULT       = "application/scim+json";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The target endpoint for SCIM API operations */
  private final WebTarget target;

  /** The servlet request containing HTTP headers */
  private final HttpServletRequest servlet;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new OIMScimContext with the specified loggable and servlet.
   **
   ** @param loggable           A {@link Loggable} object for logging.
   **                           Allowed object is {@link Loggable}.
   ** @param servlet            The {@link HttpServletRequest} containing HTTP
   **                           headers for the context.
   **                           Allowed object is {@link HttpServletRequest}.
   */
  private OIMScimContext(final Loggable loggable, final HttpServletRequest servlet) {
    // ensure inheritance
    super(loggable);

    // initialize instance attributes
    this.target = client.target(this.contextURL());
    
    this.servlet = servlet;
    if (servlet.getHeader(HTTPContext.X_REQUESTED_BY_HEADER) == null) {
      this.servlet.setAttribute(HTTPContext.X_REQUESTED_BY_HEADER, this.servlet.getRemoteUser());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a default context.
   **
   ** @param loggable           A {@link Loggable} object for logging.
   **                           Allowed object is {@link Loggable}.
   ** @param servlet            The {@link HttpServletRequest} containing HTTP
   **                           headers for the context.
   **                           Allowed object is {@link HttpServletRequest}.
   **
   ** @return                   A new instance of {@link OIMScimContext}.
   **                           Possible object is {@link OIMScimContext}.
   */
  public static OIMScimContext build(final Loggable loggable, final HttpServletRequest servlet) {
    return new OIMScimContext(loggable, servlet);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSchema
  /**
   ** Searches for SCIM schemas.
   **
   ** @return                   A {@link Get} request to retrieve SCIM schemas.
   **                           Possible object is {@link Get}.
   */
  public Get searchSchema() {

    final String method = "searchSchema";
    this.entering(method);
    try {
      return searchRequest(ENDPOINT_SCHEMAS);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSchema
  /**
   ** Retrieves a SCIM schema by its identifier.
   **
   ** @param id                 The schema identifier to lookup.
   **                           Allowed object is {@link String}.
   **
   ** @return                   The {@link SchemaDescriptor} for the specified
   **                           schema identifier.
   **                           Possible object is {@link SchemaDescriptor}.
   **
   ** @throws ScimException     If the schema cannot be retrieved.
   */
  public SchemaDescriptor lookupSchema(final String id)
    throws ScimException {

    final String method = "lookupSchema";
    this.entering(method);
    try {
      return lookupRequest(ENDPOINT_SCHEMAS, id).invokeSchema();
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchResourceTypes
  /**
   ** Searches for SCIM resource types.
   **
   ** @return                   A {@link Get} request to retrieve SCIM resource
   **                           types.
   **                           Possible object is {@link Get}.
   */
  public Get searchResourceTypes() {

    final String method = "searchResourceTypes";
    this.entering(method);
    try {
      return searchRequest(ENDPOINT_RESOURCETYPE);
    }
    finally {
      this.exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchUsers
  /**
   ** Searches for SCIM user resources.
   **
   ** @return                   A {@link Get} request to retrieve SCIM user
   **                           resources.
   **                           Possible object is {@link Get}.
   */
  public Get searchUsers(final Map<String, Object> option) {

    final String method = "searchUsers";
    this.entering(method);
    try {
      return searchRequest(ENDPOINT_USERS, option);
    }
    finally {
      this.exiting(method);
    }
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceUser
  /**
   ** Replaces an existing SCIM user resource on the Service Provider using a
   ** PATCH request.
   **
   ** This method constructs a PATCH request to the SCIM user endpoint with the
   ** specified group ID and updates the user resource with the changes defined
   ** in the {@link PatchRequest} object.
   **
   ** @param  id                 The unique identifier of the user to be
   **                            replaced.
   **                            Allowed object is {@link String}.
   ** @param  user               The {@link PatchRequest} object containing the
   **                            changes.
   **                            Allowed object is {@link PatchRequest}.
   **
   ** @return                    The updated {@link User} object representing
   **                            the replaced group resource.
   **                            Possible object is {@link User}.
   **
   ** @throws ScimException      If there is an issue with the request or
   **                            response handling.
   */
  public User replaceUser(final String id, final PatchRequest user)
    throws ScimException {
    final String method = "replaceUser";
    this.entering(method);
    try {
      return patchRequest(ENDPOINT_USERS, id).invoke(User.class, user);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyUser
  /**
   ** Modifies an existing SCIM user resource on the Service Provider.
   **
   ** This method constructs a PUT request to the SCIM user endpoint with the
   ** specified user ID and updates the group resource with the attributes
   ** provided in the {@link User} object.
   **
   ** @param  id                 The unique identifier of the user to be modified.
   **                            Allowed object is {@link String}.
   ** @param  user               The {@link User} object containing updated
   **                            attributes.
   **                            Allowed object is {@link User}.
   **
   ** @return                    The updated {@link User} object representing
   **                            the modified group resource.
   **                            Possible object is {@link User}.
   **
   ** @throws ScimException      If there is an issue with the request or
   **                            response handling.
   ** @throws ServerException    If a server-related error occurs during the
   **                            request execution.
   */
  public User modifyUser(final String id, final User user)
    throws ScimException {
    final String method = "modifyUser";
    this.entering(method);
    try {
      return putRequest(ENDPOINT_USERS, id).invoke(User.class, user);
    }
    finally {
      this.exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAccount
  /**
   ** Looks up a specific SCIM user resource by its unique identifier.
   **
   ** @param  id                 The unique identifier of the SCIM user resource.
   **                            Allowed object is {@link String}.
   ** @param  option             Additional query parameters for the request.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    The {@link User} resource corresponding to the
   **                            provided ID.
   **                            Possible object is {@link User}.
   **
   ** @throws ScimException      If there is an issue during the lookup process.
   ** @throws ServerException    If a server-related error occurs during the lookup.
   */
  public User lookupUser(final String id, final Map<String, Object> option)
    throws ScimException {
    final String method = "lookupUser";
    this.entering(method);
    try {
      return lookupRequest(ENDPOINT_USERS, id, option).invoke(User.class);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUser
  /**
   ** Creates a new SCIM user resource in the target system.
   **
   ** @param  user               The {@link User} object representing the new
   **                            user to be created.
   **                            Allowed object is {@link User}.
   ** @param  option             Additional query parameters or headers for the
   **                            request.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    The created {@link User} resource returned by
   **                            the target system.
   **                            Possible object is {@link User}.
   **
   ** @throws ScimException      If there is an issue during the user creation
   **                            process.
   ** @throws ServerException    If a server-related error occurs during the
   **                            user creation.
   */
  public User createUser(User user, final Map<String, Object> option)
    throws ScimException {
    final String method = "createUser";
    this.entering(method);
    try {
      return postRequest(ENDPOINT_USERS, option).invoke(User.class, user);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteUser
  /**
   ** Deletes an existing SCIM user resource from the Service Provider.
   **
   ** This method constructs a DELETE request to the SCIM user endpoint and
   ** removes the user resource identified by the specified group ID.
   **
   ** @param  id                 The unique identifier of the group to be
   **                            deleted.
   **                            Allowed object is {@link String}.
   **
   ** @return                    The {@link Response} object representing the
   **                            result of the delete operation.
   **                            Possible object is {@link Response}.
   **
   ** @throws ScimException      If there is an issue with the request or
   **                            response handling.
   */
  public Response deleteUser(final String id)
    throws ScimException {
    final String method = "deleteUser";
    this.entering(method);
    try {
      return deleteRequest(ENDPOINT_USERS, id).invoke();
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchGroups
  /**
   ** Retrieves a list of SCIM group resources from the target system based on
   ** the specified options.
   **
   ** @param  option             A map containing query parameters to filter and
   **                            customize the group search.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    A {@link Get} object configured to perform the
   **                            group search operation.
   **                            Possible object is {@link Get}.
   */
  public Get searchGroups(final Map<String, Object> option) {
    final String method = "searchGroups";
    this.entering(method);
    try {
      return searchRequest(ENDPOINT_GROUPS, option);
    }
    finally {
      this.exiting(method);
    }
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   postSearchUsers
  /**
   ** Creates and configures a POST request to search for SCIM user resources
   ** using advanced criteria or filters.
   **
   ** This method constructs a POST request targeting the SCIM user endpoint
   ** combined with the ".search" sub-context for more complex queries.
   **
   ** @return                    A {@link Post} object configured to execute
   **                            the advanced user search operation.
   **                            Possible object is {@link Post}.
   */
  public Post postSearchUsers() {
    final String method = "postSearchUsers";
    this.entering(method);
    try {
      return postRequest(ENDPOINT_USERS, ENDPOINT_SEARCH);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   postSearchGroups
  /**
   ** Creates and configures a POST request to search for SCIM group resources
   ** using advanced criteria or filters.
   **
   ** This method constructs a POST request targeting the SCIM group endpoint
   ** combined with the ".search" sub-context for more complex queries.
   **
   ** @return                    A {@link Post} object configured to execute
   **                            the advanced group search operation.
   **                            Possible object is {@link Post}.
   */
  public Post postSearchGroups() {
    final String method = "postSearchGroups";
    this.entering(method);
    try {
      return postRequest(ENDPOINT_GROUPS, ENDPOINT_SEARCH);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupGroup
  /**
   ** Retrieves a specific SCIM group resource from the Service Provider by its
   ** identifier.
   **
   ** This method constructs a GET request to the SCIM group endpoint, appends
   ** the group identifier, and includes additional query parameters if provided.
   ** It then invokes the
   ** request and returns the resulting {@link Group} object.
   **
   ** @param  id                 The unique identifier of the group resource to
   **                            retrieve.
   **                            Allowed object is {@link String}.
   ** @param  option             A map of additional query parameters to include
   **                            in the request.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    The {@link Group} object representing the
   **                            retrieved group resource.
   **                            Possible object is {@link Group}.
   **
   ** @throws ScimException      If there is an issue with the request or
   **                            response handling.
   */
  public Group lookupGroup(final String id, final Map<String, Object> option)
    throws ScimException {
    final String method = "lookupGroup";
    this.entering(method);
    try {
      return lookupRequest(ENDPOINT_GROUPS, id, option).invoke(Group.class);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGroup
  /**
   ** Creates a new SCIM group resource on the Service Provider.
   **
   ** This method constructs a POST request to the SCIM group endpoint, appends
   ** any additional query parameters provided, and sends the {@link Group}
   ** object representing the new group to be created.
   **
   ** @param  group              The {@link Group} object containing the
   **                            attributes of the new group.
   **                            Allowed object is {@link Group}.
   ** @param  option             A map of additional query parameters to include
   **                            in the request.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    The {@link Group} object representing the
   **                            created group resource.
   **                            Possible object is {@link Group}.
   **
   ** @throws ScimException      If there is an issue with the request or
   **                            response handling.
   */
  public Group createGroup(Group group, final Map<String, Object> option)
    throws ScimException {
    final String method = "lookupAccount";
    this.entering(method);
    try {
      return postRequest(ENDPOINT_GROUPS, option).invoke(Group.class, group);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyGroup
  /**
   ** Modifies an existing SCIM group resource on the Service Provider.
   **
   ** This method constructs a PUT request to the SCIM group endpoint with the
   ** specified group ID and updates the group resource with the attributes
   ** provided in the {@link Group} object.
   **
   ** @param  id                 The unique identifier of the group to be modified.
   **                            Allowed object is {@link String}.
   ** @param  group              The {@link Group} object containing updated
   **                            attributes.
   **                            Allowed object is {@link Group}.
   **
   ** @return                    The updated {@link Group} object representing
   **                            the modified group resource.
   **                            Possible object is {@link Group}.
   **
   ** @throws ScimException      If there is an issue with the request or
   **                            response handling.
   */
  public Group modifyGroup(final String id, final Group group)
    throws ScimException {
    final String method = "modifyGroup";
    this.entering(method);
    try {
      return putRequest(ENDPOINT_GROUPS, id).invoke(Group.class, group);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceGroup
  /**
   ** Replaces an existing SCIM group resource on the Service Provider using a
   ** PATCH request.
   **
   ** This method constructs a PATCH request to the SCIM group endpoint with the
   ** specified group ID and updates the group resource with the changes defined
   ** in the {@link PatchRequest} object.
   **
   ** @param  id                 The unique identifier of the group to be
   **                            replaced.
   **                            Allowed object is {@link String}.
   ** @param  group              The {@link PatchRequest} object containing the
   **                            changes.
   **                            Allowed object is {@link PatchRequest}.
   **
   ** @return                    The updated {@link Group} object representing
   **                            the replaced group resource.
   **                            Possible object is {@link Group}.
   **
   ** @throws ScimException      If there is an issue with the request or
   **                            response handling.
   */
  public Group replaceGroup(final String id, final PatchRequest group)
    throws ScimException {
    final String method = "replaceGroup";
    this.entering(method);
    try {
      return patchRequest(ENDPOINT_GROUPS, id).invoke(Group.class, group);
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteGroup
  /**
   ** Deletes an existing SCIM group resource from the Service Provider.
   **
   ** This method constructs a DELETE request to the SCIM group endpoint and
   ** removes the group resource identified by the specified group ID.
   **
   ** @param  id                 The unique identifier of the group to be
   **                            deleted.
   **                            Allowed object is {@link String}.
   **
   ** @return                    The {@link Response} object representing the
   **                            result of the delete operation.
   **                            Possible object is {@link Response}.
   **
   ** @throws ScimException      If there is an issue with the request or
   **                            response handling.
   */
  public Response deleteGroup(final String id)
    throws ScimException {
    final String method = "deleteGroup";
    this.entering(method);
    try {
      return deleteRequest(ENDPOINT_GROUPS, id).invoke();
    }
    finally {
      this.exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchServiceProviderConfigs
  /**
   ** Retrieves a list of SCIM searchServiceProviderConfigs resources from the
   ** target system based on the specified options.
   **
   ** @param  option             A map containing query parameters to filter and
   **                            customize the ServiceProviderConfigs
   **                            search.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    A {@link Get} object configured to perform the
   **                            ServiceProviderConfigs search operation.
   **                            Possible object is {@link Get}.
   */
  public Get searchServiceProviderConfigs(final Map<String, Object> option) {
    final String method = "searchGroups";
    this.entering(method);
    try {
      return searchRequest(ENDPOINT_SERVICEPROVIDERCONFIG, option);
    }
    finally {
      this.exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   ** Constructs a GET request to search and retrieve SCIM resources from the
   ** specified context.
   **
   ** This method builds a GET request for querying resources of a specific
   ** resource type (e.g., Users, Groups) using the SCIM API. The request
   ** includes default headers such as content type, authorization, and request
   ** origin.
   **
   ** @param  context            The URI context of the resource type to be
   **                            queried.
   **                            Allowed values include:
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                              <li>"<code>Schemas</code>"
   **                              <li>"<code>ResourceTypes</code>"
   **                            </ul>
   **                            Allowed object is {@link String}.
   **
   ** @return                    A {@link Get} object configured to perform the
   **                            search.
   **                            Possible object is {@link Get}.
   */
  protected Get searchRequest(final String context) {
    return Get.build(this.target.path(context))
              .header(CONTENT_TYPE_HEADER, this.servlet.getContentType() != null ? this.servlet.getContentType() : CONTENT_TYPE_HEADER_DEFAULT)
              .header(AUTHORIZATION_HEADER, this.servlet.getHeader(AUTHORIZATION_HEADER))
              .header(X_REQUEST_BY_HEADER, this.servlet.getHeader(X_REQUEST_BY_HEADER));
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   ** Constructs a GET request to search and retrieve SCIM resources from the
   ** specified context.
   **
   ** This method builds a GET request for querying resources of a specific
   ** resource type (e.g., Users, Groups) using the SCIM API. The request
   ** includes default headers such as content type, authorization, and request
   ** origin.
   **
   ** @param  context            The URI context of the resource type to be
   **                            queried.
   **                            Allowed values include:
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                              <li>"<code>Schemas</code>"
   **                              <li>"<code>ResourceTypes</code>"
   **                            </ul>
   **                            Allowed object is {@link String}.
   ** @param  option             A map of additional query parameters to include
   **                            in the request.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    A {@link Get} object configured to perform the
   **                            search.
   **                            Possible object is {@link Get}.
   */
  protected Get searchRequest(final String context, final Map<String, Object> option) {
    Get request = searchRequest(context);
    
    for (Map.Entry<String, Object> queryParam : option.entrySet()) {
      if (queryParam.getValue() != null) {
        request = request.parameter(queryParam.getKey(), queryParam.getValue());
      }
    }
    
    return request;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Constructs a GET request to retrieve a specific SCIM resource by its 
   ** identifier.
   **
   ** This method builds a GET request for accessing a single SCIM resource
   ** (e.g., User, Group) using its unique identifier. The request includes
   ** default headers such as content type, authorization, and request origin.
   **
   ** @param  context            The URI context of the resource type to be
   **                            accessed.
   **                            Allowed values include:
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                              <li>"<code>Schemas</code>"
   **                              <li>"<code>ResourceTypes</code>"
   **                            </ul>
   **                            Allowed object is {@link String}.
   ** @param  id                 The unique identifier of the SCIM resource to
   **                            be retrieved.
   **                            Allowed object is {@link String}.
   **
   ** @return                    A {@link Get} object configured to perform the
   **                            lookup.
   **                            Possible object is {@link Get}.
   */
  public Get lookupRequest(final String context, final String id) {
    return Get.build(requestTarget(context, id))
              .header(CONTENT_TYPE_HEADER, this.servlet.getContentType())
              .header(AUTHORIZATION_HEADER, this.servlet.getHeader(AUTHORIZATION_HEADER))
              .header(X_REQUEST_BY_HEADER, this.servlet.getHeader(X_REQUEST_BY_HEADER));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Constructs a GET request to retrieve a specific SCIM resource by its
   ** identifier.
   **
   ** This method builds a GET request for accessing a single SCIM resource
   ** (e.g., User, Group) using its unique identifier. The request includes
   ** default headers such as content type, authorization, and request origin.
   **
   ** @param  context            The URI context of the resource type to be
   **                            accessed.
   **                            Allowed values include:
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                              <li>"<code>Schemas</code>"
   **                              <li>"<code>ResourceTypes</code>"
   **                            </ul>
   **                            Allowed object is {@link String}.
   ** @param  id                 The unique identifier of the SCIM resource to
   **                            be retrieved.
   **                            Allowed object is {@link String}.
   ** @param  option             A map of additional query parameters to include
   **                            in the request.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    A {@link Get} object configured to perform the
   **                            lookup.
   **                            Possible object is {@link Get}.
   */
  public Get lookupRequest(final String context, final String id, final Map<String, Object> option) {
    Get request = lookupRequest(context, id);
    
    for (Map.Entry<String, Object> queryParam : option.entrySet()) {
      if (queryParam.getValue() != null)
        request = request.parameter(queryParam.getKey(), queryParam.getValue());
    }
    return request;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   postRequest
  /**
   ** Constructs a POST request to create or modify a SCIM resource at a nested
   ** context.
   **
   ** This method builds a POST request for performing operations targeted at a
   ** nested endpoint of the SCIM service. It allows targeting subpaths for
   ** specific operations or custom behaviors, such as executing queries or
   ** creating resources in a sub-context.
   **
   ** @param  context            The URI context of the primary resource type or
   **                            endpoint (e.g., "Users", "Groups").
   **                            Allowed object is {@link String}.
   **
   ** @return                    A {@link Post} object configured to perform the
   **                            operation
   **                            at the specified context and sub-context.
   **                            Possible object is {@link Post}.
   */
  public Post postRequest(final String context) {
    return Post.build(this.target.path(context))
              .header(CONTENT_TYPE_HEADER, this.servlet.getContentType())
              .header(AUTHORIZATION_HEADER, this.servlet.getHeader(AUTHORIZATION_HEADER))
              .header(X_REQUEST_BY_HEADER, this.servlet.getHeader(X_REQUEST_BY_HEADER));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   postRequest
  /**
   ** Constructs a POST request to create or modify a SCIM resource at a nested
   ** context.
   **
   ** This method builds a POST request for performing operations targeted at a
   ** nested endpoint of the SCIM service. It allows targeting subpaths for
   ** specific operations or custom behaviors, such as executing queries or
   ** creating resources in a sub-context.
   **
   ** @param  context            The URI context of the primary resource type or
   **                            endpoint (e.g., "Users", "Groups").
   **                            Allowed object is {@link String}.
   ** @param  subContext         The URI sub-context appended to the primary
   **                            context.
   **                            Allowed object is {@link String}.
   **
   ** @return                    A {@link Post} object configured to perform the
   **                            operation at the specified context and sub-context.
   **                            Possible object is {@link Post}.
   */
  public Post postRequest(final String context, final String subContext) {
    return Post.build(requestTarget(context, subContext))
              .header(CONTENT_TYPE_HEADER, this.servlet.getContentType())
              .header(AUTHORIZATION_HEADER, this.servlet.getHeader(AUTHORIZATION_HEADER))
              .header(X_REQUEST_BY_HEADER, this.servlet.getHeader(X_REQUEST_BY_HEADER));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   postRequest
  /**
   ** Constructs a POST request to create or modify a SCIM resource.
   **
   ** This method builds a POST request for performing operations such as
   ** creating a new SCIM resource (e.g., User, Group) or executing custom
   ** queries. The request includes default headers for content type,
   ** authorization, and request origin.
   **
   ** @param  context            The URI context of the resource type or
   **                            endpoint
   **                            to which the POST request is targeted.
   **                            Allowed values include:
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                              <li>"<code>Schemas</code>"
   **                              <li>"<code>ResourceTypes</code>"
   **                            </ul>
   **                            Allowed object is {@link String}.
   ** @param  option             A map of additional query parameters to include
   **                            in the request.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    A {@link Post} object configured to perform the
   **                            operation.
   **                            Possible object is {@link Post}.
   */
  public Post postRequest(final String context, final Map<String, Object> option) {
    Post request = postRequest(context);
    
    for (Map.Entry<String, Object> queryParam : option.entrySet()) {
      if (queryParam.getValue() != null)
        request = request.parameter(queryParam.getKey(), queryParam.getValue());
    }
    return request;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   patchRequest
  /**
   ** Constructs a PATCH request to update or modify a SCIM resource at a
   ** specific endpoint.
   **
   ** This method builds a PATCH request for performing partial updates or
   ** modifications to a SCIM resource identified by its unique `id`. It
   ** supports operations such as adding, replacing, or removing attributes of
   ** the resource.
   **
   ** @param  context            The URI context of the resource type or endpoint
   **                            (e.g., "Users", "Groups").
   **                            Allowed object is {@link String}.
   ** @param  id                 The unique identifier of the resource to be
   **                            modified.
   **                            Allowed object is {@link String}.
   **
   ** @return                    A {@link Patch} object configured to perform
   **                            the PATCH operation at the specified resource
   **                            endpoint.
   **                            Possible object is {@link Patch}.
   */
  public Patch patchRequest(final String context, final String id) {
    return Patch.build(requestTarget(context, id))
              .header(CONTENT_TYPE_HEADER, this.servlet.getContentType())
              .header(AUTHORIZATION_HEADER, this.servlet.getHeader(AUTHORIZATION_HEADER))
              .header(X_REQUEST_BY_HEADER, this.servlet.getHeader(X_REQUEST_BY_HEADER));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   patchRequest
  /**
   ** Constructs a PATCH request to update or modify a SCIM resource at a
   ** specific endpoint.
   **
   ** This method builds a PATCH request for performing partial updates or
   ** modifications to a SCIM resource identified by its unique `id`. It
   ** supports operations such as adding, replacing, or removing attributes of
   ** the resource.
   **
   ** @param  context            The URI context of the resource type or endpoint
   **                            (e.g., "Users", "Groups").
   **                            Allowed object is {@link String}.
   ** @param  id                 The unique identifier of the resource to be
   **                            modified.
   **                            Allowed object is {@link String}.
   ** @param  option             A map of additional query parameters to include
   **                            in the request.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    A {@link Patch} object configured to perform
   **                            the PATCH operation at the specified resource
   **                            endpoint.
   **                            Possible object is {@link Patch}.
   */
  public Patch patchRequest(final String context, final String id, final Map<String, Object> option) {
    Patch request = patchRequest(context, id);
    
    for (Map.Entry<String, Object> queryParam : option.entrySet()) {
      if (queryParam.getValue() != null)
        request = request.parameter(queryParam.getKey(), queryParam.getValue());
    }
    return request;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   putRequest
  /**
   ** Constructs a PUT request to replace a SCIM resource at a specific endpoint.
   **
   ** This method builds a PUT request for replacing an existing SCIM resource
   ** identified by its unique `id`. PUT operations overwrite the entire resource
   ** with the provided data.
   **
   ** @param  context            The URI context of the resource type or endpoint
   **                            (e.g., "Users", "Groups").
   **                            Allowed object is {@link String}.
   ** @param  id                 The unique identifier of the resource to be
   **                            replaced.
   **                            Allowed object is {@link String}.
   **
   ** @return                    A {@link Put} object configured to perform the
   **                            PUT operation at the specified resource endpoint.
   **                            Possible object is {@link Put}.
   */
  public Put putRequest(final String context, final String id) {
    return Put.build(requestTarget(context, id))
              .header(CONTENT_TYPE_HEADER, this.servlet.getContentType())
              .header(AUTHORIZATION_HEADER, this.servlet.getHeader(AUTHORIZATION_HEADER))
              .header(X_REQUEST_BY_HEADER, this.servlet.getHeader(X_REQUEST_BY_HEADER));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   putRequest
  /**
   ** Constructs a PUT request to replace a SCIM resource at a specific endpoint.
   **
   ** This method builds a PUT request for replacing an existing SCIM resource
   ** identified by its unique `id`. PUT operations overwrite the entire resource
   ** with the provided data.
   **
   ** @param  context            The URI context of the resource type or endpoint
   **                            (e.g., "Users", "Groups").
   **                            Allowed object is {@link String}.
   ** @param  id                 The unique identifier of the resource to be
   **                            replaced.
   **                            Allowed object is {@link String}.
   ** @param  option             A map of additional query parameters to include
   **                            in the request.
   **                            Allowed object is {@link Map}.
   **
   ** @return                    A {@link Put} object configured to perform the
   **                            PUT operation at the specified resource endpoint.
   **                            Possible object is {@link Put}.
   */
  public Put putRequest(final String context, final String id, final Map<String, Object> option) {
    Put request = putRequest(context, id);
    
    for (Map.Entry<String, Object> queryParam : option.entrySet()) {
      if (queryParam.getValue() != null)
        request = request.parameter(queryParam.getKey(), queryParam.getValue());
    }
    return request;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Constructs a DELETE request to remove a SCIM resource at a specific endpoint.
   **
   ** This method builds a DELETE request for deleting an existing SCIM resource
   ** identified by its unique `id`.
   **
   ** @param  context            The URI context of the resource type or endpoint
   **                            (e.g., "Users", "Groups").
   **                            Allowed object is {@link String}.
   ** @param  id                 The unique identifier of the resource to be
   **                            deleted.
   **                            Allowed object is {@link String}.
   **
   ** @return                    A {@link Delete} object configured to perform
   **                            the DELETE operation at the specified resource
   **                            endpoint.
   **                            Possible object is {@link Delete}.
   */
  public Delete deleteRequest(final String context, final String id) {
    return Delete.build(requestTarget(context, id))
              .header(CONTENT_TYPE_HEADER, this.servlet.getContentType())
              .header(AUTHORIZATION_HEADER, this.servlet.getHeader(AUTHORIZATION_HEADER))
              .header(X_REQUEST_BY_HEADER, this.servlet.getHeader(X_REQUEST_BY_HEADER));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Constructs a DELETE request to remove a SCIM resource at a specific endpoint.
   **
   ** This method builds a DELETE request for deleting an existing SCIM resource
   ** identified by its unique `id`.
   **
   ** @param  context            The URI context of the resource type or endpoint
   **                            (e.g., "Users", "Groups").
   **                            Allowed object is {@link String}.
   ** @param  id                 The unique identifier of the resource to be
   **                            deleted.
   **                            Allowed object is {@link String}.
   ** @param  option             A map of additional query parameters to include
   **                            in the request.
  **                             Allowed object is {@link Map}.
   **
   ** @return                    A {@link Delete} object configured to perform
   **                            the DELETE operation at the specified resource
   **                            endpoint.
   **                            Possible object is {@link Delete}.
   */
  public Delete deleteRequest(final String context, final String id, final Map<String, Object> option) {
    Delete request = deleteRequest(context, id);
    
    for (Map.Entry<String, Object> queryParam : option.entrySet()) {
      if (queryParam.getValue() != null)
        request = request.parameter(queryParam.getKey(), queryParam.getValue());
    }
    return request;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestTarget
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   ** <p>
   ** Create a JAX-RS web target whose URI refers to the
   ** <code>ServiceEndpoint.contextURL()</code> the JAX-RS / Jersey application
   ** is deployed at.
   ** <p>
   ** This method is an equivalent of calling
   ** <code>client().target(endpoint.contextURL())</code>.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                              <li>"<code>Schemas</code>"
   **                              <li>"<code>ResourceTypes</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  private final WebTarget requestTarget(final String context, final String identifier) {
    return this.target.path(context).path(identifier);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Implemented abstract method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Retrieve the root context path of the SCIM service.
   **
   ** @return The root context path for SCIM services.
   **         Possible object is {@link String}.
   */
  @Override
  public String rootContext() {
    return this.OIM_ENDPOINT_SCIM;
  }
}
