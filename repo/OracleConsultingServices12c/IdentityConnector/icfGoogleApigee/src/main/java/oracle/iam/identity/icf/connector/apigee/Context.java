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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.apigee;

import java.net.URI;

import java.util.List;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.connector.apigee.request.Search;
import oracle.iam.identity.icf.connector.apigee.request.Lookup;
import oracle.iam.identity.icf.connector.apigee.request.Create;
import oracle.iam.identity.icf.connector.apigee.request.Status;
import oracle.iam.identity.icf.connector.apigee.request.Modify;
import oracle.iam.identity.icf.connector.apigee.request.Delete;
import oracle.iam.identity.icf.connector.apigee.request.Assign;

import oracle.iam.identity.icf.connector.apigee.schema.User;
import oracle.iam.identity.icf.connector.apigee.schema.Tenant;
import oracle.iam.identity.icf.connector.apigee.schema.Developer;
import oracle.iam.identity.icf.connector.apigee.schema.UserResult;

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
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link User}s provided by the Service Provider.
   */
  static final String     ENDPOINT_USER     = "users";

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type sysadmin provided by the Service Provider.
   */
  static final String     ENDPOINT_USERROLE = "userroles";

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link Tenant}s provided by the Service Provider.
   */
  static final String     ENDPOINT_TENANT   = "organizations";

  /**
   ** An HTTP request path for system administrator roles provided by the
   ** Service Provider.
   */
  static final String     TYPE_SYSADMIN    = "sysadmin";
  /**
   ** An HTTP request path for role provided by the Service Provider.
   */
  static final String     TYPE_USERROLE    = "userroles";

  /**
   ** An HTTP request path for developer provided by the Service Provider.
   */
  static final String     TYPE_APPLICATION = "apps";

  /**
   ** An HTTP request path for developer provided by the Service Provider.
   */
  static final String     TYPE_DEVELOPER   = "developers";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the endpoint to access the Google Apigee Edge API **/
  private final WebTarget target;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Context</code> which is associated with the specified
   ** {@link ServiceClient} for configuration purpose.
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
   ** Build a request to query and retrieve APIGEE Edge User resources from the
   ** Service Provider.
   **
   ** @return                    the collection containing the search results.
   **                            <br>
   **                            Possible object is {@link UserResult}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public UserResult searchAccount()
    throws SystemException {

    final String method = "searchAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_USER).user();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAccount
  /**
   ** Build a request to lookup a known REST <code>User</code> resource from the
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
   **                            Possible object is {@link Lookup}.
   **
   ** @throws ServiceException   if an error occurs.
   */
  public Lookup lookupAccount(final String id)
    throws ServiceException {

    return lookupRequest(ENDPOINT_USER, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Build and executes the request to create the provided new user resource at
   ** the Service Provider.
   **
   ** @param  resource           the new the user resource to create.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the successfully created user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public User createAccount(final User resource)
    throws SystemException {

    final String method = "createAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return accountCreate().invoke(resource) ? resource : resource;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
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
   ** @throws SystemException    if an error occurs.
   */
  public void deleteAccount(final String id)
    throws SystemException {

    final String method = "deleteAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      accountDelete(id).invoke();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
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
   ** @param  resource           the user resource to modify.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the successfully modified user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public User modifyAccount(final String id, final User resource)
    throws SystemException {

    final String method = "modifyAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return accountModify(id).invoke(resource);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchTenant
  /**
   ** Build a request to query and retrieve APIGEE <code>Tenant</code>s
   ** (aka Organization} resources from the Service Provider.
   **
   ** @return                    the collection containing the search results.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public List<String> searchTenant()
    throws SystemException {

    final String method = "searchTenant";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_TENANT).list();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupTenant
  /**
   ** Build a request to lookup a known REST <code>Organization</code> resource
   ** from the Service Provider.
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
   **                            Possible object is {@link Lookup}.
   **
   ** @throws ServiceException   if an error occurs.
   */
  public Lookup lookupTenant(final String id)
    throws ServiceException {

    return lookupRequest(ENDPOINT_TENANT, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTenant
  /**
   ** Build and executes the request to create the provided new tenant resource
   ** at the Service Provider.
   **
   ** @param  resource           the new the tenant resource to create.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the successfully created user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Tenant createTenant(final Tenant resource)
    throws SystemException {

    final String method = "createTenant";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return tenantCreate().invoke(resource);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteTenant
  /**
   ** Deletes a known REST tenant resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deleteTenant(final String id)
    throws SystemException {

    final String method = "deleteTenant";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      tenantDelete(id).invoke();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyTenant
  /**
   ** Modifies a known REST tenant resource at the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the tenant resource to modify.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the successfully modified tenant resource.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Tenant modifyTenant(final String id, final Tenant resource)
    throws SystemException {

    final String method = "modifyTenant";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return tenantModify(id).invoke(resource);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRole
  /**
   ** Build a request to query and retrieve APIGEE <code>Role</code>s resources
   ** from the Service Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to retrieve the roles for.
   **                            <br>
   **                            Possible object is {@link String}.
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
  public List<String> searchRole(final String tenant)
    throws SystemException {

    final String method = "searchRole";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_TENANT, tenant, TYPE_USERROLE).list();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRole
  /**
   ** Build a request to assign APIGEE <code>Role</code> members in the Service
   ** Provider.
   **
   ** @param  name               the identifier of the role (aka Userrole)
   **                            to assign the members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  beneficiary        the identifier of the user (aka User)
   **                            to assign as a member.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public User assignRole(final String name, final String beneficiary)
    throws SystemException {

    final String method = "assignRole";
    trace(method, Loggable.METHOD_ENTRY);
    final Assign request = Assign.build(this.target.path(ENDPOINT_USERROLE).path(name).path(ENDPOINT_USER), this.client.typeContent(), this.client.typeAccept());
    try {
      return request.invoke(beneficiary);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeRole
  /**
   ** Build a request to revoke APIGEE <code>Role</code> members in the Service
   ** Provider.
   **
   ** @param  name               the identifier of the role (aka Userrole)
   **                            to revoke the member from.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  beneficiary        the identifier of the user (aka User)
   **                            to revoke as a member.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void revokeRole(final String name, final String beneficiary)
    throws SystemException {

    final String method = "revokeRole";
    trace(method, Loggable.METHOD_ENTRY);
    final Delete request = Delete.build(this.target.path(ENDPOINT_USERROLE).path(name).path(ENDPOINT_USER).path(beneficiary), this.client.typeContent(), this.client.typeAccept());
    try {
      request.invoke();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRole
  /**
   ** Build a request to assign APIGEE <code>Role</code> members in the Service
   ** Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to assign the members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  name               the identifier of the role (aka Userrole)
   **                            to assign the members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  beneficiary        the identifier of the user (aka User)
   **                            to assign as a member.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public User assignRole(final String tenant, final String name, final String beneficiary)
    throws SystemException {

    final String method = "assignRole";
    trace(method, Loggable.METHOD_ENTRY);
    final Assign request = Assign.build(this.target.path(ENDPOINT_TENANT).path(tenant).path(TYPE_USERROLE).path(name).path(ENDPOINT_USER), this.client.typeContent(), this.client.typeAccept());
    try {
      return request.invoke(beneficiary);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeRole
  /**
   ** Build a request to revoke APIGEE <code>Role</code> members in the Service
   ** Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to revoke the member from.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  name               the identifier of the role (aka Userrole)
   **                            to revoke the member from.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  beneficiary        the identifier of the user (aka User)
   **                            to revoke as a member.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void revokeRole(final String tenant, final String name, final String beneficiary)
    throws SystemException {

    final String method = "revokeRole";
    trace(method, Loggable.METHOD_ENTRY);
    final Delete request = Delete.build(this.target.path(ENDPOINT_TENANT).path(tenant).path(TYPE_USERROLE).path(name).path(ENDPOINT_USER).path(beneficiary), this.client.typeContent(), this.client.typeAccept());
    try {
      request.invoke();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchMember
  /**
   ** Build a request to query and retrieve APIGEE <code>Role</code> members
   ** from the Service Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to retrieve the members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  name               the identifier of the role (aka Userrole)
   **                            to retrieve the members for.
   **                            <br>
   **                            Possible object is {@link String}.
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
  public List<String> searchMember(final String tenant, final String name)
    throws SystemException {

    final String method = "searchMember";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_TENANT, tenant, TYPE_USERROLE, name, ENDPOINT_USER).list();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchApplication
  /**
   ** Build a request to query and retrieve APIGEE <code>Application</code>
   ** resources from the Service Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to retrieve the <code>Application</code> for.
   **                            <br>
   **                            Possible object is {@link String}.
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
  public List<String> searchApplication(final String tenant)
    throws SystemException {

    final String method = "searchApplication";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_TENANT, tenant, TYPE_APPLICATION).list();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupApplication
  /**
   ** Build a request to lookup a known REST <code>Application</code> resource
   ** from the Service Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to retrieve the <code>Application</code> for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup}.
   **
   ** @throws ServiceException   if an error occurs.
   */
  public Lookup lookupApplication(final String tenant, final String id)
    throws ServiceException {

    return lookupRequest(tenant, TYPE_APPLICATION, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchDeveloper
  /**
   ** Build a request to query and retrieve APIGEE <code>Developer</code>
   ** resources from the Service Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to retrieve the <code>Developer</code> for.
   **                            <br>
   **                            Possible object is {@link String}.
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
  public List<String> searchDeveloper(final String tenant)
    throws SystemException {

    final String method = "searchDeveloper";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_TENANT, tenant, TYPE_DEVELOPER).list();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupDeveloper
  /**
   ** Build a request to lookup a known REST <code>Developer</code> resource
   ** from the Service Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to retrieve the <code>Developer</code> for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup}.
   **
   ** @throws ServiceException   if an error occurs.
   */
  public Lookup lookupDeveloper(final String tenant, final String id)
    throws ServiceException {

    return lookupRequest(tenant, TYPE_DEVELOPER, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDeveloper
  /**
   ** Build and executes the request to create the provided new developer
   ** resource at the Service Provider.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer will associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the new the developer resource to create.
   **                            <br>
   **                            Allowed object is {@link Developer}.
   **
   ** @return                    the successfully created user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Developer createDeveloper(final String tenant, final Developer resource)
    throws SystemException {

    final String method = "createDeveloper";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return developerCreate(tenant).invoke(resource);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyDeveloper
  /**
   ** Modifies a known REST developer resource at the Service Provider.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer will associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the developer resource to modify.
   **                            <br>
   **                            Allowed object is {@link Developer}.
   **
   ** @return                    the successfully modified user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Developer modifyDeveloper(final String tenant, final String id, final Developer resource)
    throws SystemException {

    final String method = "modifyDeveloper";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return developerModify(tenant, id).invoke(resource);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   activateDeveloper
  /**
   ** Activates a known REST developer resource at the Service Provider.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer is associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void activateDeveloper(final String tenant, final String id)
    throws SystemException {

    final String method = "activateDeveloper";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      developerStatus(tenant, id).invoke(Status.Action.ACTIVE);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deactivateDeveloper
  /**
   ** Deactivates a known REST developer resource at the Service Provider.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer is associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deactivateDeveloper(final String tenant, final String id)
    throws SystemException {

    final String method = "deactivateDeveloper";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      developerStatus(tenant, id).invoke(Status.Action.INACTIVE);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteDeveloper
  /**
   ** Deletes a known REST developer resource from the Service Provider.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer will associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deleteDeveloper(final String tenant, final String id)
    throws SystemException {

    final String method = "deleteAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      developerDelete(tenant, id).invoke();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCreate
  /**
   ** Creates a new REST user resource at the Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Create}.
   **
   ** @throws SystemException    if an error occurs.
   */
  private Create accountCreate()
    throws SystemException {

    return createRequest(ENDPOINT_USER);
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
   **
   ** @return                    the successfully modified Apigee user
   **                            resource.
   **                            <br>
   **                            Possible object is {@link Modify} for type
   **                            {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  private Modify<User> accountModify(final String id)
    throws SystemException {

    return modifyRequest(ENDPOINT_USER, id);
  }

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

    return deleteRequest(ENDPOINT_USER, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenantCreate
  /**
   ** Creates a new REST tenant resource at the Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Create}.
   **
   ** @throws SystemException    if an error occurs.
   */
  private Create tenantCreate()
    throws SystemException {

    return createRequest(ENDPOINT_TENANT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenantModify
  /**
   ** Modifies a known REST tenant resource at the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully modified Apigee tenant
   **                            resource.
   **                            <br>
   **                            Possible object is {@link Modify} for type
   **                            {@link Tenant}.
   **
   ** @throws SystemException    if an error occurs.
   */
  private Modify<Tenant> tenantModify(final String id)
    throws SystemException {

    return modifyRequest(ENDPOINT_TENANT, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenantDelete
  /**
   ** Build a request to delete a known REST <code>Tenant</code> resource from the
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
  private Delete tenantDelete(final String id)
    throws ServiceException {

    return deleteRequest(ENDPOINT_TENANT, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerCreate
  /**
   ** Creates a new REST developer resource at the Service Provider.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer will associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Create}.
   **
   ** @throws SystemException    if an error occurs.
   */
  private Create developerCreate(final String tenant)
    throws SystemException {

    return createRequest(tenant, TYPE_DEVELOPER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerModify
  /**
   ** Modifies a known REST developer resource at the Service Provider.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer will associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully modified Apigee developer
   **                            resource.
   **                            <br>
   **                            Possible object is {@link Modify} for type
   **                            {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  private Modify<Developer> developerModify(final String tenant, final String id)
    throws SystemException {

    return modifyRequest(tenant, TYPE_DEVELOPER, id);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerStatus
  /**
   ** Modifies the status of a known REST developer resource at the Service
   ** Provider.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer will associated with.
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
   **                            Possible object is {@link Delete}.
   **
   **
   ** @throws SystemException    if an error occurs.
   */
  private Status developerStatus(final String tenant, final String id)
    throws SystemException {

    return statusRequest(tenant, TYPE_DEVELOPER, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerDelete
  /**
   ** Build a request to delete a known REST <code>Developer</code> resource
   ** from the Service Provider.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer will associated with.
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
   **                            Possible object is {@link Delete}.
   **
   ** @throws ServiceException   if an error occurs.
   */
  private Delete developerDelete(final String tenant, final String id)
    throws ServiceException {

    return deleteRequest(tenant, TYPE_DEVELOPER, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
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
  private Search searchRequest(final String context) {
    return Search.build(this.target.path(context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               a particular URI as defined by the associated
   **                            resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               a particular type such as
   **                            <ul>
   **                              <li>"<code>userroles</code>"
   **                            </ul>
   **                            of the URI as defined by the associated
   **                            resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  private Search searchRequest(final String context, final String name, final String type) {
    return Search.build(this.target.path(context).path(name).path(type));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  tenant             a particular URI as defined by the associated
   **                            resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               a particular type such as
   **                            <ul>
   **                              <li>"<code>userroles</code>"
   **                            </ul>
   **                            of the URI as defined by the associated
   **                            resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              a particular URI as defined by the associated
   **                            resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subtype            a particular type such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                            </ul>
   **                            of the URI as defined by the associated
   **                            resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  private Search searchRequest(final String context, final String tenant, final String type, final String value, final String subtype) {
    return Search.build(this.target.path(context).path(tenant).path(type).path(value).path(subtype));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known REST resource from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
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
   **                            Possible object is {@link Lookup}.
   */
  private Lookup lookupRequest(final String context, final String id) {
    return Lookup.build(requestTarget(context, id), this.client.typeContent(), this.client.typeAccept());
  }

 //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known REST resource from the Service Provider.
   **
   ** @param  tenant             the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) of an
   **                            organization.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>apps</code>"
   **                              <li>"<code>userroles</code>"
   **                              <li>"<code>developers</code>"
   **                              <li>"<code>apiproducts</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) the REST
   **                            resource to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup}.
   */
  private Lookup lookupRequest(final String tenant, final String type, final String id) {
    return Lookup.build(requestTarget(ENDPOINT_TENANT, tenant).path(type).path(id), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   ** Build a request to create a new REST resource at the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Create}.
   */
  private Create createRequest(final String context) {
    return Create.build(this.target.path(context), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   ** Build a request to create a new REST resource at the Service Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to retrieve the developers for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Create}.
   */
  private Create createRequest(final String tenant, final String context) {
    return Create.build(requestTarget(ENDPOINT_TENANT, tenant).path(context), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   statusRequest
  /**
   ** Build a request to modify the status of a known REST resource at the
   ** Service Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to modify the developers in.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
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
   **                            Possible object is {@link Create}.
   */
  private Status statusRequest(final String tenant, final String context, final String id) {
    return Status.build(requestTarget(ENDPOINT_TENANT, tenant).path(context).path(id), this.client.typeContent(), this.client.typeAccept());
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
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
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
   **                            Possible object is {@link Delete} for type
   **                            <code>T</code>.
   */
  private <T extends Resource> Modify<T> modifyRequest(final String context, final String id) {
    return Modify.<T>build(requestTarget(context, id), this.client.typeContent(), this.client.typeAccept());
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
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to modify the resource for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
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
   **                            Possible object is {@link Delete} for type
   **                            <code>T</code>.
   */
  private <T extends Resource> Modify<T> modifyRequest(final String tenant, final String context, final String id) {
    return Modify.<T>build(requestTarget(ENDPOINT_TENANT, tenant).path(context).path(id), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Build a request to delete a known REST resource from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
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
   **                            Possible object is {@link Delete} for type
   **                            <code>T</code>.
   */
  private Delete deleteRequest(final String context, final String id) {
    return Delete.build(target(requestTarget(context, id).getUri()), this.client.typeContent(), this.client.typeAccept());
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Build a request to delete a known REST resource from the Service Provider.
   **
   ** @param  tenant             the identifier of the tenant (aka Organization)
   **                            to modify the resource for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>userroles</code>"
   **                              <li>"<code>developers</code>"
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
   **                            Possible object is {@link Delete} for type
   **                            <code>T</code>.
   */
  private Delete deleteRequest(final String tenant, final String context, final String id) {
    return Delete.build(requestTarget(ENDPOINT_TENANT, tenant).path(context).path(id), this.client.typeContent(), this.client.typeAccept());
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
   **                              <li>"<code>users</code>"
   **                              <li>"<code>organizations</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
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