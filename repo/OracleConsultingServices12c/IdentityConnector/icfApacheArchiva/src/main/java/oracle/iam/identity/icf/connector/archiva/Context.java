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
    Subsystem   :   Apache Archiva Connector

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva;

import java.net.URI;

import java.util.List;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.connector.archiva.request.Create;
import oracle.iam.identity.icf.connector.archiva.request.Delete;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.connector.archiva.schema.User;
import oracle.iam.identity.icf.connector.archiva.schema.Role;

import oracle.iam.identity.icf.connector.archiva.request.Search;
import oracle.iam.identity.icf.connector.archiva.request.Lookup;
//import oracle.iam.identity.icf.connector.archiva.request.Create;
//import oracle.iam.identity.icf.connector.archiva.request.Modify;
//import oracle.iam.identity.icf.connector.archiva.request.Delete;

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
public class Context extends ServiceContext {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link User}s provided by the Service Provider.
   */
  static final String     ENDPOINT_USER = "userService";

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link Role}s provided by the Service Provider.
   */
  static final String     ENDPOINT_ROLE = "roleManagementService";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the endpoint to access the Cloud Controller API **/
  private final WebTarget target;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceContext</code> which is associated with the
   ** specified {@link ServiceClient} for configuration purpose.
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

    // initialize instance attributes
    this.target = client.target(client.endpoint().contextURL());
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
  // Method:   resourcePing
  /**
   ** Returns the resource types supported by the Service Provider.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void resourcePing()
      throws SystemException {

    // intentionally left blank
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAccount
  /**
   ** Build a request to query and retrieve ARCHIVA Edge User resources from the
   ** Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   ** @throws SystemException    if an error occurs.
   */
  public List<User> searchAccount()
    throws SystemException {

    return searchRequest(ENDPOINT_USER, "getUsers").invoke(User.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAccount
  /**
   ** Returns a known REST user resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved REST user resource.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public User lookupAccount(final String id)
    throws SystemException {

    return accountLookup(id).invoke(User.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Build and executes the request to create the provided new user resource at
   ** the Service Provider.
   **
   ** @param  resource           the user resource to create.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    <code>true</code> if the REST resource
   **                            successfully created.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public boolean createAccount(final User resource)
    throws SystemException {

    return accountCreate(resource).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteAccount
  /**
   ** Deletes a known REST user resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the REST resource
   **                            successfully deleted.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public boolean deleteAccount(final String id)
    throws SystemException {

    return accountDelete(id).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAccount
  /**
   ** Modifies a known REST user resource at the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void modifyAccount(final String id, final User resource)
    throws SystemException {

//    accountModify(id, resource).invoke(User.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRole
  /**
   ** Build a request to query and retrieve ARCHIVA <code>Role</code> resources
   ** from the Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public List<Role> searchRole()
    throws SystemException {

    return searchRequest(ENDPOINT_ROLE, "allRoles").invoke(Role.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRole
  /**
   ** Build and executes a request to retrieve a known ARCHIVA <code>Role</code>
   ** resource from the Service Provider.
   **
   ** @param  uri                the {@link URI} of the resource to retrieve.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the successfully retrieved Cloud Controller
   **                            tenant resource.
   **                            <br>
   **                            Possible object is {@link Result.Tenant}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Role lookupRole(final String id)
    throws SystemException {

    return lookupRequest(ENDPOINT_ROLE, "getRole", id).invoke(Role.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountLookup
  /**
   ** Build a request to lookup a known REST <code>User</code> resource from the
   ** Service Provider.
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

    return lookupRequest(ENDPOINT_USER, "getUser", id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCreate
  /**
   ** Creates a new REST user resource at the Service Provider.
   **
   ** @param  resource           the Apigee user resource to create.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the successfully created Apigee user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  private Create<User> accountCreate(final User resource)
    throws SystemException {

    return createRequest(ENDPOINT_USER, "createUser", resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountModify
  /**
   ** Modifies a known REST user resource at the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the Apigee user resource to modify.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the successfully modified Apigee user
   **                            resource.
   **                            <br>
   **                            Possible object is {@link Result.Tenant}.
   **
   ** @throws SystemException    if an error occurs.
   */
/*
  private Modify<User> accountModify(final String id, final User resource)
    throws SystemException {

    return modifyRequest(ENDPOINT_USER, id, resource);
  }
*/
  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountDelete
  /**
   ** Build a request to delete a known REST <code>User</code> resource from the
   ** Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Delete} for type
   **                            <code>T</code>.
   **
   ** @throws ServiceException   if an error occurs.
   */
  private Delete accountDelete(final String id)
    throws ServiceException {

    return deleteRequest(ENDPOINT_USER, "deleteUser", id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>userService</code>"
   **                              <li>"<code>roleManagementService</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the call context URI such as
   **                            <ul>
   **                              <li>"<code>getUsers</code>"
   **                              <li>"<code>getUser</code>"
   **                            </ul>
   **                            as defined by the resource context.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  protected Search searchRequest(final String context, final String type) {
    return Search.build(this.target.path(context).path(type));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known REST resource from the Service Provider.
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
    return Lookup.<T>build(target(uri), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known REST resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>userService</code>"
   **                              <li>"<code>roleManagementService</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the call context URI such as
   **                            <ul>
   **                              <li>"<code>getUser</code>"
   **                              <li>"<code>getRole</code>"
   **                            </ul>
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
  public <T extends Resource> Lookup<T> lookupRequest(final String context, final String type, final String id) {
    return Lookup.<T>build(requestTarget(context, type, id), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   ** Build a request to create a new REST resource at the Service Provider.
   **
   ** @param  <T>                the type of the payload implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the request payload
   **                            extending this class (requests can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>userService</code>"
   **                              <li>"<code>roleManagementService</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the call context URI such as
   **                            <ul>
   **                              <li>"<code>createUser</code>"
   **                            </ul>
   ** @param  resource           the Apigee REST resource to create.
   **                            <br>
   **                            Allowed object is {@link Resource}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Delete} for type
   **                            <code>T</code>.
   */
  private <T extends Resource> Create<T> createRequest(final String context, final String type, final T resource) {
    return Create.<T>build(this.target.path(context).path(type), resource, this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRequest
  /**
   ** Build a request to modify a known REST resource at the Service Provider.
   **
   ** @param  <T>                the type of the payload implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the request payload
   **                            extending this class (requests can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>userService</code>"
   **                              <li>"<code>roleManagementService</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the Apigee REST resource to modify.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Delete} for type
   **                            <code>T</code>.
   */
/*  
  private <T extends Resource> Modify<T> modifyRequest(final String context, final String id, final T resource) {
    return Modify.<T>build(this.target.path(context).path(id), resource, this.client.typeContent(), this.client.typeAccept());
  }
*/
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Build a request to lookup a known REST resource from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>userService</code>"
   **                              <li>"<code>roleManagementService</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the call context URI such as
   **                            <ul>
   **                              <li>"<code>deleteUser</code>"
   **                            </ul>
   **                            as defined by the resource context.
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
   **                            Possible object is {@link Delete} for type
   **                            <code>T</code>.
   */
  private Delete deleteRequest(final String context, final String type, final String id) {
    return Delete.build(target(requestTarget(context, type, id).getUri()), this.client.typeContent(), this.client.typeAccept());
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
   **                              <li>"<code>userService</code>"
   **                              <li>"<code>roleManagementService</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the call context URI such as
   **                            <ul>
   **                              <li>"<code>getUser</code>"
   **                              <li>"<code>deleteUser</code>"
   **                            </ul>
   **                            as defined by the resource context.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  private final WebTarget requestTarget(final String context, final String type, final String identifier) {
    return this.target.path(context).path(type).path(identifier);
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