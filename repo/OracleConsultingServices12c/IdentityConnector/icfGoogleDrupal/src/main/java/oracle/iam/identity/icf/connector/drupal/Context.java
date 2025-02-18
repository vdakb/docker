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
    Subsystem   :   Google Drupal Connector

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com based on work of dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.drupal;

import java.net.URI;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.connector.drupal.request.Search;
import oracle.iam.identity.icf.connector.drupal.request.Lookup;
import oracle.iam.identity.icf.connector.drupal.request.Create;
import oracle.iam.identity.icf.connector.drupal.request.Modify;
import oracle.iam.identity.icf.connector.drupal.request.Delete;
import oracle.iam.identity.icf.connector.drupal.request.Assign;

import oracle.iam.identity.icf.connector.drupal.schema.User;
import oracle.iam.identity.icf.connector.drupal.schema.UserJson;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends ServiceContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   * An HTTP URI to the endpoint is used to retrieve information about the
   * resource type {@link oracle.iam.identity.icf.connector.drupal.schema.User} s provided by the Service Provider.
   */
  static final String     ENDPOINT_USER            = "user";
  static final String     ENDPOINT_USER_CREATE     = "entity/user";
  static final String     ENDPOINT_USER_SEARCH_ALL = "list-users";

  /**
   ** An HTTP URI to the endpoint is used to retrieve list of the
   ** resource type "role" provided by the Service Provider.
   */
  static final String     ENDPOINT_ROLELIST    = "rolelist/existingroles";

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type sysadmin provided by the Service Provider.
   */
  static final String     ENDPOINT_USERROLE    = "userroles";

//  /**
//   * An HTTP URI to the endpoint is used to retrieve information about the
//   * resource type {@link oracle.iam.identity.icf.connector.drupal.schema.Tenant} s provided by the Service Provider.
//   */
  static final String     ENDPOINT_TENANT      = "organizations";

  /**
   ** An HTTP request path for system administrator roles provided by the
   ** Service Provider.
   */
  static final String     TYPE_SYSADMIN    = "sysadmin";
  /**
   ** An HTTP request path for role provided by the Service Provider.
   */
  static final String     TYPE_USERROLE    = "userroles";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the endpoint to access the Google Drupal API **/
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
   ** Build a request to query and retrieve Google Drupal User resources from the
   ** Service Provider.
   **
   ** @param  criteria           the {@link String} search criteria, already
   **                            normalized and simplified, e.g. 'uid eq "14"'.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @return the collection containing the search results.
   ** <br>
   ** Possible object is {@link oracle.iam.identity.icf.connector.drupal.schema.UserJson} .
   **
   ** @throws SystemException if an error occurs.
   **/
  
  public List<User> searchAccount(String criteria)
    throws SystemException {

    final String method = "Context#searchAccount(criteria)";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, "criteria: " + criteria);
    List<User> result = new ArrayList<>();
    try {
      if (criteria != null) {
        Pattern p = Pattern.compile("uid eq \"(\\d+)\"");
        Matcher m = p.matcher(criteria);
        if (m.find() && m.groupCount() == 1) {
          // Basic UID filter supplied, we're able to retrieve the individual user directly
          String criteriaUid = m.group(1);
          trace(method, "Match found, retrieved UID: " + criteriaUid);
          result = searchRequest(ENDPOINT_USER, criteriaUid).user();
        }
      } else {
        trace(method, "No criteria supplied, simply list all users");
        // "all" means "all active" merged with "all inactive" :-/
        List<User> activeUserList = Search.build(this.target.path(ENDPOINT_USER_SEARCH_ALL).queryParam("status", "1")).user();
        List<User> inactiveUserList = Search.build(this.target.path(ENDPOINT_USER_SEARCH_ALL).queryParam("status", "0")).user();
        List<User> tmpUserList = new ArrayList<>();
        tmpUserList.addAll(activeUserList);
        tmpUserList.addAll(inactiveUserList);
        trace(method, "Fetched full list of users, now fetch individual (full) attributes");
        for (User individualUser : tmpUserList) {
          // For some reason, there is a user with UID "0" returned and it's not possible to query such user, so skip
          if ("0".equals(individualUser.uid())) {
            trace(method, "User with uid=0 encountered, skipping");
            continue;
          }
          trace(method, "Fetching full attribute set for uid=" + individualUser.uid());
          // Just a single-element list to keep the typing
          List<User> tmpUser = searchRequest(ENDPOINT_USER, individualUser.uid()).user();
          result.addAll(tmpUser);
        }
      }
      if (result == null) {
        trace(method, "Null search result received");
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAccount
  /**
   * Build a request to lookup a known REST <code>User</code> resource from the
   * Service Provider.
   *
   * @param id the resource identifier (for example the value
   * of the "<code>uid</code>" attribute).
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Lookup} .
   *
   * @throws ServiceException if an error occurs.
   */
  public Lookup lookupAccount(final String id)
    throws ServiceException {

    final String method = "Context#lookupAccount()";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, "id: " + id);
    
    return lookupRequest(ENDPOINT_USER, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   * Build and executes the request to create the provided new user resource at
   * the Service Provider.
   *
   * @param resource the new the user resource to create.
   * <br>
   * Allowed object is {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   *
   * @return the successfully created user resource.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   *
   * @throws SystemException if an error occurs.
   */
  public UserJson createAccount(final UserJson resource)
    throws SystemException {

    final String method = "createAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      trace(method, "Input resource object dump: " + resource);
      trace(method, "Resource class name: " + resource.getClass().getName());
      Create retval = accountCreate();
      UserJson modifiedResource = retval.invoke(resource);
      User modifiedUser = new User(modifiedResource);
      trace(method, "Output user object dump: " + modifiedUser);
      return modifiedResource;
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

    final String method = "Context#deleteAccount()";
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
   * Modifies a known REST user resource at the Service Provider.
   *
   * @param id the resource identifier (for example the value
   * of the "<code>id</code>" attribute).
   * <br>
   * Allowed object is {@link String} .
   * @param resource the user resource to modify.
   * <br>
   * Allowed object is {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   *
   * @return the successfully modified user resource.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   *
   * @throws SystemException if an error occurs.
   */
  public User modifyAccount(final String id, final User resource)
    throws SystemException {

    final String method = "Context#modifyAccount()";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      trace(method, "Input resource object dump: " + resource);
      trace(method, "Resource class name: " + resource.getClass().getName());
      Modify<UserJson> retval = accountModify(id);
      UserJson modifiedResource = retval.invoke(new UserJson(resource));
      User modifiedUser = new User(modifiedResource);
      trace(method, "Output user object dump: " + modifiedUser);
      return modifiedUser;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRole
  /**
   ** Build a request to query and retrieve Google Drupal <code>Role</code>s resources
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
  public List<Map<String, String>> searchRole()
    throws SystemException {

    final String method = "Context#searchRole()";
    trace(method, Loggable.METHOD_ENTRY);
    List<Map<String, String>> queryResult = new ArrayList<>();
    try {
      queryResult = searchRequest(ENDPOINT_ROLELIST).list();
      trace(method, "Query result: " + queryResult);
      for (Map<String, String> cursor : queryResult) {
        trace(method, "Identified Role, id: " + cursor.get("id") + ", alias: " + cursor.get("role_alias"));
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return queryResult;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRole
  /**
   * Build a request to assign Google Drupal <code>Role</code> members in the Service
   * Provider.
   *
   * @param name the identifier of the role (aka Userrole)
   * to assign the members for.
   * <br>
   * Possible object is {@link String} .
   * @param beneficiary the identifier of the user (aka User)
   * to assign as a member.
   * <br>
   * Possible object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   *
   * @throws SystemException if an error occurs.
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
   ** Build a request to revoke Google Drupal <code>Role</code> members in the Service
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
  // Method:   accountCreate
  /**
   * Creates a new REST user resource at the Service Provider.
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Create} .
   *
   * @throws SystemException if an error occurs.
   */
  private Create accountCreate()
    throws SystemException {

    return createRequest(ENDPOINT_USER_CREATE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountModify
  /**
   * Modifies a known REST user resource at the Service Provider.
   *
   * @param id the resource identifier (for example the value
   * of the "<code>id</code>" attribute).
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the successfully modified Google Drupal user
   * resource.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Modify} for type
   * {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   *
   * @throws SystemException if an error occurs.
   */
  private Modify<UserJson> accountModify(final String id)
    throws SystemException {

    return modifyRequest(ENDPOINT_USER, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountDelete
  /**
   * Build a request to delete a known REST <code>User</code> resource from the
   * Service Provider.
   *
   * @param id the resource identifier (for example the value
   * of the "<code>id</code>" attribute).
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Delete} for type
   * <code>T</code>.
   *
   * @throws ServiceException if an error occurs.
   */
  private Delete accountDelete(final String id)
    throws ServiceException {

    return deleteRequest(ENDPOINT_USER, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   * Build a request to query and retrieve resources of a single resource type
   * from the Service Provider.
   *
   * @param context the resource context URI such as
   * <ul>
   * <li>"<code>users</code>"
   * <li>"<code>organizations</code>"
   * </ul>
   * as defined by the associated resource type.
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Search} .
   */
  private Search searchRequest(final String context) {
    final String method = "Context#searchRequest(context)";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, "context: " + context);
    return Search.build(this.target.path(context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   * Build a request to query and retrieve resources of a single resource type
   * from the Service Provider.
   *
   * @param context the resource context URI such as
   * <ul>
   * <li>"<code>users</code>"
   * <li>"<code>organizations</code>"
   * </ul>
   * as defined by the associated resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param name a particular URI as defined by the associated
   * resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param type a particular type such as
   * <ul>
   * <li>"<code>userroles</code>"
   * </ul>
   * of the URI as defined by the associated
   * resource type.
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Search} .
   */
  private Search searchRequest(final String context, final String name, final String type) {
    return Search.build(this.target.path(context).path(name).path(type));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   * Build a request to query and retrieve resources of a single resource type
   * from the Service Provider.
   *
   * @param context the resource context URI such as
   * <ul>
   * <li>"<code>users</code>"
   * <li>"<code>organizations</code>"
   * </ul>
   * as defined by the associated resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param name a particular URI as defined by the associated
   * resource type.
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Search} .
   */
  private Search searchRequest(final String context, final String id) {
    final String method = "Context#searchRequest(context, id)";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, "context: " + context);
    trace(method, "id: " + id);
    return Search.build(this.target.path(context).path(id));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   * Build a request to query and retrieve resources of a single resource type
   * from the Service Provider.
   *
   * @param context the resource context URI such as
   * <ul>
   * <li>"<code>users</code>"
   * <li>"<code>organizations</code>"
   * </ul>
   * as defined by the associated resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param tenant a particular URI as defined by the associated
   * resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param type a particular type such as
   * <ul>
   * <li>"<code>userroles</code>"
   * </ul>
   * of the URI as defined by the associated
   * resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param value a particular URI as defined by the associated
   * resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param subtype a particular type such as
   * <ul>
   * <li>"<code>users</code>"
   * </ul>
   * of the URI as defined by the associated
   * resource type.
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Search} .
   */
  private Search searchRequest(final String context, final String tenant, final String type, final String value, final String subtype) {
    return Search.build(this.target.path(context).path(tenant).path(type).path(value).path(subtype));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   * Build a request to lookup a known REST resource from the Service Provider.
   *
   * @param context the resource context URI such as
   * <ul>
   * <li>"<code>users</code>"
   * <li>"<code>organizations</code>"
   * </ul>
   * as  defined by the associated resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param id the resource identifier (for example the value
   * of the "<code>id</code>" attribute).
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Lookup} .
   */
  private Lookup lookupRequest(final String context, final String id) {
    return Lookup.build(requestTarget(context, id), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   * Build a request to create a new REST resource at the Service Provider.
   *
   * @param context the resource context URI such as
   * <ul>
   * <li>"<code>users</code>"
   * <li>"<code>organizations</code>"
   * </ul>
   * as  defined by the associated resource type.
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Create} .
   */
  private Create createRequest(final String context) {
    return Create.build(this.target.path(context), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRequest
  /**
   * Build a request to modify a known REST resource at the Service Provider.
   *
   * @param <T> the type of the payload implementation.
   * <br>
   * This parameter is used for convenience to allow
   * better implementations of the request payload
   * extending this class (requests can return their
   * own specific type instead of type defined by
   * this class only).
   * <br>
   * Allowed object is <code>T</code>.
   * @param context the resource context URI such as
   * <ul>
   * <li>"<code>users</code>"
   * <li>"<code>organizations</code>"
   * </ul>
   * as  defined by the associated resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param id the resource identifier (for example the value
   * of the "<code>id</code>" attribute).
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Delete} for type
   * <code>T</code>.
   */
  private <T extends Resource> Modify<T> modifyRequest(final String context, final String id) {
    return Modify.<T>build(requestTarget(context, id), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   * Build a request to delete a known REST resource from the Service Provider.
   *
   * @param context the resource context URI such as
   * <ul>
   * <li>"<code>users</code>"
   * <li>"<code>organizations</code>"
   * </ul>
   * as  defined by the associated resource type.
   * <br>
   * Allowed object is {@link String} .
   * @param id the resource identifier (for example the value
   * of the "<code>id</code>" attribute).
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the request builder that may be used to specify
   * additional request parameters and to invoke the
   * request.
   * <br>
   * Possible object is {@link oracle.iam.identity.icf.connector.drupal.request.Delete} for type
   * <code>T</code>.
   */
  private Delete deleteRequest(final String context, final String id) {
    return Delete.build(target(requestTarget(context, id).getUri()), this.client.typeContent(), this.client.typeAccept());
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
  
  // TODO: Dummy methods, to be cleaned
  public List<String> searchTenant() {
    return new ArrayList<String>();
  }
}