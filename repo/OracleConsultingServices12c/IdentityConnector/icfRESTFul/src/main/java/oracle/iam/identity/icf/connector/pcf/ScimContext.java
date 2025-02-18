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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   ScimContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ScimContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf;

import java.util.List;

import java.net.URI;

import java.util.ArrayList;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.schema.Resource;

import oracle.iam.identity.icf.scim.request.Search;
import oracle.iam.identity.icf.scim.request.Lookup;
import oracle.iam.identity.icf.scim.request.Create;
import oracle.iam.identity.icf.scim.request.Delete;
import oracle.iam.identity.icf.scim.request.Resolve;
import oracle.iam.identity.icf.scim.request.Replace;

import oracle.iam.identity.icf.scim.response.ListResponse;

import oracle.iam.identity.icf.scim.v1.request.Patch;
import oracle.iam.identity.icf.scim.v1.request.Operation;

import oracle.iam.identity.icf.connector.pcf.scim.schema.Group;
import oracle.iam.identity.icf.connector.pcf.scim.schema.UserResource;
import oracle.iam.identity.icf.connector.pcf.scim.schema.GroupResource;
import oracle.iam.identity.icf.connector.pcf.scim.schema.SchemaFactory;
import oracle.iam.identity.icf.connector.pcf.scim.schema.SchemaResource;

///////////////////////////////////////////////////////////////////////////////
// class ScimContext
// ~~~~~ ~~~~~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 ** <p>
 ** The specific of the context is that it takes care about the SCIM API the
 ** User Account and Authentication (UAA) Server expose reagrding users.
 ** <p>
 ** The primary role of UAA is as an OAuth2 provider, issuing tokens for client
 ** apps to use when they act on behalf of PAS users.
 ** <br>
 ** In collaboration with the login server, UAA can authenticate users with
 ** their Pivotal Application Service (PAS) credentials, and can act as an
 ** Signle Sign On (SSO) service using those, or other, credentials.
 ** <p>
 ** UAA has endpoints for managing user accounts and for registering OAuth2
 ** clients, as well as various other management functions.
 ** <p>
 ** Different runtimes and services use separate UAA instances.
 ** <br>
 ** PAS has two UAA instances by default: one for BOSH Director, used to
 ** bootstrap the rest of the PAS deployment; and one for the BOSH deployment,
 ** used as a shared resource by all apps that require user authentication. This
 ** is the minimum number of UAA instances PAS must have.
 ** <br>
 ** Other runtimes and services also have UAA instances. These instances are
 ** separate from each other. If you log into one runtime or service, you are
 ** not also logged into other runtimes and services that authenticate using
 ** UAA. You must log into each runtime or service separately.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** This class is decleared as public only to access it in JUNit tests.
 ** <br>
 ** This may be changed in the future.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ScimContext extends AbstractLoggable<ScimContext> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** resource schemas supported by a SCIM Service Provider.
   */
  static final String ENDPOINT_SCHEMAS = "Schemas";

  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** resource of type <code>Group</code> provided by a SCIM Service Provider.
   ** <p>
   ** SCIM provides a schema for representing groups. The core schema for
   ** resource type <code>Group</code> is identified using the following schema
   ** URI:
   ** <ul>
   **   <li>Version 1: <code>urn:scim:schemas:core:1.0</code>.
   **   <li>Version 2: <code>urn:ietf:params:scim:schemas:core:2.0:Group</code>,
   **                  urn:ietf:params:scim:schemas:extension:enterprise:2.0:Group.
   ** </ul>
   */
  static final String ENDPOINT_GROUPS  = "Groups";

  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** resources of type <code>User</code>.
   ** <p>
   ** SCIM provides a resource type for <code>User</code>s resources. The core
   ** schema for <code>User</code> is identified using the following schema URI:
   ** <ul>
   **   <li>Version 1: <code>urn:scim:schemas:core:1.0</code>.
   **   <li>Version 2: <code>urn:ietf:params:scim:schemas:core:2.0:User</code>,
   **                  <code>urn:ietf:params:scim:schemas:extension:enterprise:2.0:User</code>.
   ** </ul>
   */
  static final String     ENDPOINT_USERS   = "Users";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Service   client;

  /** the endpoint to access the User Account and Authentication API **/
  private final WebTarget target;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ScimContext</code> context which is associated with
   ** the specified {@link Endpoint} for configuration purpose.
   **
   ** @param  client             the {@link Service} <code>IT Resource</code>
   **                            instance where this connector context is
   **                            associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceClient}.
   */
  private ScimContext(final Service client) {
    // ensure inheritance
    super(client);

    this.client = client;
    this.target = client.target(client.endpoint().accountURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a default context.
   **
   ** @param  client             the {@link Service} <code>IT Resource</code>
   **                            instance where this connector context is
   **                            associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceClient}.
   **
   ** @return                    a default context.
   **                            <br>
   **                            Possible object is <code>ScimContext</code>.
   */
  public static ScimContext build(final Service client) {
    return new ScimContext(client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemas
  /**
   ** Returns the schemas supported by the Service Provider.
   **
   ** @return                    the list of schemas supported by the Service
   **                            Provider.
   **                            <br>
   **                            Possible object is {@link ListResponse} where
   **                            each element is of type {@link SchemaResource}.
   */
  public ListResponse<SchemaResource> schemas() {
    final List<SchemaResource> resource = CollectionUtility.list(SchemaFactory.schema(SchemaResource.class), SchemaFactory.schema(UserResource.class), SchemaFactory.schema(GroupResource.class));
    return new ListResponse<SchemaResource>(resource.size(), resource.size(), resource.size(), resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAccount
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to accounts.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchAccount() {
    return searchRequest(ENDPOINT_USERS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAccount
  /**
   ** Returns a known SCIM user resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM user resource.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public UserResource lookupAccount(final String id)
    throws SystemException {

    return accountLookup(id).invoke(UserResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveAccount
  /**
   ** Build and executes a request to retrieve the unique identifier of a known
   ** SCIM user resource from the Service Provider.
   **
   ** @param  name               the resource name (for example the value of the
   **                            "<code>userName</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the unique identifier of the successfully
   **                            retrieved SCIM user resource or
   **                            <code>null</code> if the specified resource
   **                            name is not mapped at the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public String resolveAccount(final String name)
    throws SystemException {

    final ListResponse<UserResource> response = accountResolve(name).invoke(UserResource.class);
    if (response.total() > 1)
      throw ServiceException.tooMany(name);

    return response.total() == 0 ? null : response.iterator().next().id();
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
  // Method:   modifyAccount
  /**
   ** Build and executes the request to modify the PCF user resource at the
   ** Service Provider with the given resource.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes are send,
   ** regardless of each attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  resource           the SCIM user resource to modify.
   **                            <br>
   **                            Allowed object is  {@link UserResource}.
   **
   ** @return                    the successfully modified SCIM user resource.
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
  // Method:   modifyAccount
  /**
   ** Build and executes the request to patch the SCIM user resource at the
   ** Service Provider with the given <code>operation</code>s.
   **
   **
   ** @param  identifier         the identifier of the resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operations to apply by the update.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the successfully modified SCIM user resource.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public UserResource modifyAccount(final String identifier, final List<Operation> operation)
    throws SystemException {

    return modifyRequest(ENDPOINT_USERS, identifier, operation).invoke(UserResource.class);
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
  // Method:   groupsForAccount
  /**
   ** Retrive the SCIM group resource in the Service Provider that belongs to
   ** the given user identifier.
   **
   ** @param  identifier         the identifier of the user resource to retrieve
   **                            the groups for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collection of group resources assign to a
   **                            user resource.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Group}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public List<String> groupsForAccount(final String identifier)
    throws SystemException {

    final List<String> groups = new ArrayList<String>();
    // set the start indes of the search to one but the count of results to a
    // higher number as one so that if a user id is ambigiously defined the
    // search will fail
    final ListResponse<UserResource> response = searchAccount().page(1, 2).emit("groups").filter(ScimTranslator.filter(ScimMarshaller.IDENTIFIER, identifier, ScimTranslator.EQ, false)).invoke(UserResource.class);
    if (response.total() > 1)
      throw ServiceException.tooMany(identifier);

    // a lambda function can avoid this loop but performance benchmarks had shown
    // saying that the overhead of Stream.forEach() compared to an ordinary for
    // loop is so significant in general that using it by default will just pile
    // up a lot of useless CPU cycles across the application
    for (Group cursor : response.iterator().next().groups()) {
      groups.add(cursor.value());
    }
    return groups.size() == 0 ? null : groups;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchGroup
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to groups.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchGroup() {
    return searchRequest(ENDPOINT_GROUPS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupGroup
  /**
   ** Returns a known SCIM group resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM group resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource lookupGroup(final String id)
    throws SystemException {

    return groupLookup(id).invoke(GroupResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveGroup
  /**
   ** Build and executes a request to retrieve the unique identifier of a known
   ** SCIM group resource from the Service Provider.
   **
   ** @param  name               the resource name (for example the value of the
   **                            "<code>displayName</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the unique identifier of the successfully
   **                            retrieved SCIM group resource or
   **                            <code>null</code> if the specified resource
   **                            name is not mapped at the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public String resolveGroup(final String name)
    throws SystemException {

    final ListResponse<GroupResource> response = groupResolve(name).invoke(GroupResource.class);
    if (response.total() > 1)
      throw ServiceException.tooMany(name);

    return response.total() == 0 ? null : response.iterator().next().id();
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
  // Method:   modifyGroup
  /**
   ** Build and executes the request to modify the SCIM group resource at the
   ** Service Provider with the given resource.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes, regardless of each
   ** attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  resource           the SCIM group resource to modify.
   **                            <br>
   **                            Allowed object is  {@link GroupResource}.
   **
   ** @return                    the successfully modified SCIM group resource.
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
  // Method:   modifyGroup
  /**
   ** Build and executes the request to modify the provided SCIM resource at the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch request to use for the update.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the successfully modified SCIM group resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource modifyGroup(final String identifier, final List<Operation> operation)
    throws SystemException {

    return modifyRequest(ENDPOINT_GROUPS, identifier, operation).invoke(GroupResource.class);
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
  // Method:   lookup
  /**
   ** Returns a known SCIM resource from the Service Provider.
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
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully retrieved SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public <T extends Resource> T lookup(final String context, final String id, final Class<T> clazz)
    throws SystemException {

    return lookupRequest(context, id).invoke(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  uri                the {@link URI} of the resource to retrieve.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully retrieved SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public <T extends Resource> T lookup(final URI uri, final Class<T> clazz)
    throws SystemException {

    return lookupRequest(uri).invoke(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create the provided new SCIM resource at the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the successfully created SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public <T extends Resource> T create(final String context, final T resource)
    throws SystemException {

    return createRequest(context, resource).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Build a request to modify a SCIM resource by replacing the resource's
   ** attributes at the Service Provider.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes, regardless of each
   ** attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
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
   ** @param  resource           the resource to modify.
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
  public final <T extends Resource> T modify(final String context, final T resource)
    throws SystemException {

    return modifyRequest(context, resource).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete the provided new SCIM resource at the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void delete(final String context, final String identifier)
    throws SystemException {

    deleteRequest(context, identifier).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountLookup
  /**
   ** Build a request to lookup a known SCIM user resource from the Service
   ** Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   **
   ** @throws ServiceException   if an error occurs.
   */
  public <T extends Resource> Lookup<T> accountLookup(final String id)
    throws ServiceException {

    return lookupRequest(ENDPOINT_USERS, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountResolve
  /**
   ** Build a request to resolve a known SCIM user resource from the Service
   ** Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>serName</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   **
   ** @throws ServiceException   if an error occurs.
   */
  public <T extends Resource> Resolve<T> accountResolve(final String id)
    throws ServiceException {

    return resolveRequest(ENDPOINT_USERS, ScimMarshaller.USERNAME, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupLookup
  /**
   ** Build a request to lookup a known SCIM group resource from the Service
   ** Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   **
   ** @throws ServiceException   if an error occurs.
   */
  public <T extends Resource> Lookup<T> groupLookup(final String id)
    throws ServiceException {

    return lookupRequest(ENDPOINT_GROUPS, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupResolve
  /**
   ** Build a request to lookup a known SCIM group resource from the Service
   ** Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>serName</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   **
   ** @throws ServiceException   if an error occurs.
   */
  public <T extends Resource> Resolve<T> groupResolve(final String id)
    throws ServiceException {

    return resolveRequest(ENDPOINT_GROUPS, ScimMarshaller.GROUPNAME, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchRequest(final String context) {
    return Search.build(this.target.path(context), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  uri                the {@link URI} of the resource to lookup.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   */
  public <T extends Resource> Lookup<T> lookupRequest(final URI uri) {
    return new Lookup<T>(target(uri), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   */
  public <T extends Resource> Lookup<T> lookupRequest(final String context, final String id) {
    return new Lookup<T>(requestTarget(context, id), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveRequest
  /**
   ** Build a request to resolve a known SCIM resource from the Service
   ** Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          the name of the attribute (for example the
   **                            value of the "<code>userName</code>"
   **                            attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the resource identifier (for example the value
   **                            of the "<code>userName</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   */
  public <T extends Resource> Resolve<T> resolveRequest(final String context, final String attribute, final String value) {
    return Resolve.<T>build(this.target.path(context), this.client.typeContent(), this.client.typeAccept()).filter(ScimTranslator.filter(attribute, value, ScimTranslator.EQ, false));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   ** Build a request to create the provided new SCIM resource at the Service
   ** Provider.
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
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public <T extends Resource> Create<T> createRequest(final String context, final T resource) {
    return Create.<T>build(this.target.path(context), resource, this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRequest
  /**
   ** Build a request to modify a SCIM resource by replacing the resource's
   ** attributes at the Service Provider.
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
   ** @param  resource           the resource to modify.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Replace} for type
   **                            <code>T</code>.
   */
  public <T extends Resource> Replace<T> modifyRequest(final String context, final T resource) {
    return Replace.<T>build(requestTarget(context, resource.id()), resource, this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRequest
  /**
   ** Build a request to modify a SCIM resource at the Service Provider.
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
   ** @param  operation          the patch operations to apply by the update.
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
  public <T extends Resource> Patch<T> modifyRequest(final String context, final String id, final List<Operation> operation) {
    return new Patch<T>(requestTarget(context, id), operation, this.client.typeContent(), this.client.typeAccept()).<T>match("*");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Build a request to create the provided SCIM resource at the Service
   ** Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Delete}.
   */
  public Delete deleteRequest(final String context, final String identifier) {
    return Delete.build(requestTarget(context, identifier), this.client.typeContent(), this.client.typeAccept());
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
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource to
   **                            request.
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
  // Method:   target
  /**
   ** Resolve a {@link URI} (relative or absolute) to a web target.
   **
   ** @param  uri                the {@link URI} to resolve.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the {@link WebTarget}.
   **                            <br>
   **                            Possible object is {@link WebTarget}.
   */
  private WebTarget target(final URI uri) {
    URI relativePath;
    if (uri.isAbsolute()) {
      relativePath = this.target.getUri().relativize(uri);
      // the given resource's location is from another service provider
      if (relativePath.equals(uri))
        throw new IllegalArgumentException("Given resource's location " + uri + " is not under this service's base path " + this.target.getUri());
    }
    else {
      relativePath = uri;
    }
    return this.target.path(relativePath.getRawPath());
  }
}