/*
    Oracle Deutschland GmbH

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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM 2 Connector

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.scim.v2;

import java.util.List;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;

import oracle.iam.identity.icf.scim.Feature;

import oracle.iam.identity.icf.scim.request.Search;

import oracle.iam.identity.icf.scim.schema.Resource;

import oracle.iam.identity.icf.scim.response.ListResponse;

import oracle.iam.identity.icf.scim.v2.schema.UserResource;
import oracle.iam.identity.icf.scim.v2.schema.GroupResource;
import oracle.iam.identity.icf.scim.v2.schema.SchemaResource;
import oracle.iam.identity.icf.scim.v2.schema.ResourceTypeResource;

import oracle.iam.identity.icf.scim.v2.request.Patch;
import oracle.iam.identity.icf.scim.v2.request.Operation;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends Feature {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceContext</code> which is associated with the
   ** specified {@link ServiceEndpoint} for configuration purpose.
   **
   ** @param  client             the {@link ServiceClient}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceClient}.
   */
  private Context(final ServiceClient client) {
    // ensure inheritance
    super(client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a default context.
   **
   ** @param  client             the {@link ServiceClient}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceClient}.
   **
   ** @return                    a default context.
   **                            <br>
   **                            Possible object is {@link ServiceContext}.
   */
  public static Context build(final ServiceClient client) {
    return new Context(client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceTypes
  /**
   ** Returns the resource types supported by the Service Provider.
   **
   ** @return                    the list of resource types supported by the
   **                            Service Provider.
   **
   ** @throws SystemException    if an error occurs.
   */
  public ListResponse<ResourceTypeResource> resourceTypes()
      throws SystemException {

    return search(ENDPOINT_RESOURCE_TYPES).invoke(ResourceTypeResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceType
  /**
   ** Returns a known resource type supported by the Service Provider.
   **
   ** @param  name               the name of the resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the resource type with the provided name.
   **                            <br>
   **                            Possible object is
   **                            {@link ResourceTypeResource}.
   **
   **
   ** @throws SystemException    if an error occurs.
   */
  public ResourceTypeResource resourceType(final String name)
    throws SystemException {

    return lookup(ENDPOINT_RESOURCE_TYPES, name, ResourceTypeResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemas
  /**
   ** Returns the schemas supported by the Service Provider.
   **
   ** @return                    the list of schemas supported by the Service
   **                            Provider.
   **                            <br>
   **                            Possible object is {@link ListResponse} of type
   **                            {@link SchemaResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public ListResponse<SchemaResource> schemas()
    throws SystemException {

    return search(ENDPOINT_SCHEMAS).invoke(SchemaResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Returns a known schema supported by the Service Provider.
   **
   ** @param  id                 the schema URN.
   **
   ** @return                    the resource type with the provided URN.
   **
   ** @throws SystemException    if an error occurs.
   */
  public SchemaResource schema(final String id)
    throws SystemException {

    return lookup(ENDPOINT_SCHEMAS, id, SchemaResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAccount
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to accounts.
   **
   ** @param  start              the 1-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter criteria used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchAccount(final int start, final int count, final String criteria) {
    return searchRequest(ENDPOINT_USERS).page(start, count).filter(criteria);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchGroup
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to groups.
   **
   ** @param  start              the 1-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter criteria used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchGroup(final int start, final int count, final String criteria) {
    return searchRequest(ENDPOINT_GROUPS).page(start, count).filter(criteria);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAccount
  /**
   ** Returns a known SCIM 2 user resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM 2 user
   **                            resource.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public UserResource lookupAccount(final String id)
    throws SystemException {

    return lookup(ENDPOINT_USERS, id, UserResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveAccount
  /**
   ** Returns a known SCIM 2 user resource from the Service Provider by its
   ** username.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved identifier of a
   **                            SCIM 2 user resource.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public String resolveAccount(final String id)
    throws SystemException {

    return resolve(ENDPOINT_USERS, "userName", id, UserResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupGroup
  /**
   ** Returns a known SCIM 2 group resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM 2 group
   **                            resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource lookupGroup(final String id)
    throws SystemException {

    return lookup(ENDPOINT_GROUPS, id, GroupResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveGroup
  /**
   ** Returns a known SCIM 2 group resource from the Service Provider by its
   ** display name.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM 2 group
   **                            resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public String resolveGroup(final String id)
    throws SystemException {

    return resolve(ENDPOINT_GROUPS, "displayName", id, GroupResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the Service Provider.
   **
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   **
   ** @return                    the successfully created SCIM User resource.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public UserResource createAccount(final UserResource resource)
    throws SystemException {

    return create(ENDPOINT_USERS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGroup
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the Service Provider.
   **
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is {@link GroupResource}.
   **
   ** @return                    the successfully created SCIM User resource.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource createGroup(final GroupResource resource)
    throws SystemException {

    return create(ENDPOINT_GROUPS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAccount
  /**
   ** Build and executes the request to modify the SCIM 2 user resource at the
   ** Service Provider with the given SCIM 2 user resource.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes all attributes are
   ** send, regardless of each attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  resource           the SCIM 2 user resource to modify.
   **                            <br>
   **                            Allowed object is  {@link UserResource}.
   **
   ** @return                    the successfully modified SCIM 2 user resource.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public UserResource modifyAccount(final UserResource resource)
    throws SystemException {

    return modify(ENDPOINT_USERS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyGroup
  /**
   ** Build and executes the request to modify the SCIM 2 group resource at the
   ** Service Provider with the given SCIM 2 group resource.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes all attributes are
   ** send, regardless of each attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  resource           the SCIM 2 group resource to modify.
   **                            <br>
   **                            Allowed object is  {@link GroupResource}.
   **
   ** @return                    the successfully modified SCIM 2 group resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource modifyGroup(final GroupResource resource)
    throws SystemException {

    return modify(ENDPOINT_GROUPS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAccount
  /**
   ** Build and executes the request to modify the SCIM 2 user resource at the
   ** Service Provider with the given <code>operation</code>s.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   **
   ** @param  identifier         the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.                            
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the successfully modified SCIM 2 user resource.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public UserResource modifyAccount(final String identifier, final List<Operation> operation)
    throws SystemException {

    return modify(ENDPOINT_USERS, identifier, operation, UserResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyGroup
  /**
   ** Build and executes the request to modify the SCIM 2 group resource at the
   ** Service Provider with the given patch operations.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   **
   ** @param  identifier         the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the successfully modified SCIM 2 group resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource modifyGroup(final String identifier, final List<Operation> operation)
    throws SystemException {

    return modify(ENDPOINT_GROUPS, identifier, operation, GroupResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteAccount
  /**
   ** Delete the SCIM user resource in the Service Provider that belongs to the
   ** given identifier.
   **
   ** @param  identifier         the identifier of the user resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deleteAccount(final String identifier)
    throws SystemException {

    delete(ENDPOINT_USERS, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteGroup
  /**
   ** Delete the SCIM group resource in the Service Provider that belongs to the
   ** given identifier.
   **
   ** @param  identifier         the identifier of the group resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deleteGroup(final String identifier)
    throws SystemException {

    delete(ENDPOINT_GROUPS, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modify a SCIM 2 resource by updating one or more attributes using a
   ** sequence of operations to "<code>add</code>", "<code>remove</code>", or
   ** "<code>replace</code>" values.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <br>
   ** The Service Provider configuration maybe used to discover service provider
   ** support for <code>PATCH</code>.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.                            
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the successfully modified SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} for type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  protected final <T extends Resource> T modify(final String context, final String identifier, final List<Operation> operation, final Class<T> clazz)
    throws SystemException {

    return modifyRequest(context, identifier, operation).invoke(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRequest
  /**
   ** Build a request to patch a SCIM 2 resource at the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.                            
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Patch} for type
   **                            <code>T</code>.
   */
  private <T extends Resource> Patch<T> modifyRequest(final String context, final String id, final List<Operation> operation) {
    return new Patch<T>(requestTarget(context, id), operation, this.client.typeContent(), this.client.typeAccept());
  }
}