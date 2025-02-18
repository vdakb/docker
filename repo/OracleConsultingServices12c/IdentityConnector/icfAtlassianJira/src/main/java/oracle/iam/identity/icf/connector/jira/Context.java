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
    Subsystem   :   Atlassian Jira Server Connector

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira;

import java.util.Map;
import java.util.List;

import java.net.URI;

import java.util.ArrayList;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.connector.jira.schema.User;
import oracle.iam.identity.icf.connector.jira.schema.Group;
import oracle.iam.identity.icf.connector.jira.schema.Project;
import oracle.iam.identity.icf.connector.jira.schema.Adressable;

import oracle.iam.identity.icf.connector.jira.request.Search;
import oracle.iam.identity.icf.connector.jira.request.Lookup;
import oracle.iam.identity.icf.connector.jira.request.Create;
import oracle.iam.identity.icf.connector.jira.request.Modify;
import oracle.iam.identity.icf.connector.jira.request.Delete;
import oracle.iam.identity.icf.connector.jira.request.Assign;
import oracle.iam.identity.icf.connector.jira.request.Password;

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
   ** resource type {@link User} provided by the Service Provider.
   */
  static final String     ENDPOINT_USER        = "user";

  /**
   ** An HTTP URI to the endpoint is used to reset password for a {@link User}
   ** provided by the Service Provider.
   */
  static final String     ENDPOINT_PASSWORD    = "user/password";

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link User}s provided by the Service Provider.
   */
  static final String     ENDPOINT_USERS       = "user/search";

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link Project.Role}s provided by the Service Provider.
   */
  static final String     ENDPOINT_ROLE        = "role";
  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link Project}s provided by the Service Provider.
   */
  static final String     ENDPOINT_PROJECT     = "project";
  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link Group}s provided by the Service Provider.
   */
  static final String     ENDPOINT_GROUP       = "group";
  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** {@link Group} membership provided by the Service Provider.
   */
  static final String     ENDPOINT_GROUP_MEMBER = "group/member";

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link Group}s provided by the Service Provider.
   */
  static final String     ENDPOINT_GROUPS       = "groups/picker";
  // IAM-438: Add possibility to (un)limit number of objects returned
  static final int        ENDPOINT_GROUPS_SIZE  = 100000;

  /**
   ** Query parameter name to lookup for user resource endpoint
   */
  static final String     USER_QUERY_PARAM     = "username";
  /**
   ** Query parameter name to lookup for group resource endpoint
   */
  static final String     GROUP_QUERY_PARAM     = "groupname";
  /**
   ** Query parameter name to lookup for any jira resource endpoint
   */
  static final String     RESOURCE_QUERY_PARAM = "key";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the endpoint to access the Jira API **/
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
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAccount
  /**
   ** Build a request to query and retrieve JIRA Application User resources from
   ** the Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchAccount() {

    final String method = "searchAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_USERS).parameter(USER_QUERY_PARAM, ".");
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAccount
  /**
   ** Returns a known REST user JIRA Application User from the Service Provider.
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
   **                            Possible object is {@link Search}.
   **
   ** @throws SystemException   if an error occurs.
   */
  @SuppressWarnings("unchecked")
  public User lookupAccount(final String id)
    throws SystemException {

    final String method = "lookupAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return (User) lookupRequest(ENDPOINT_USER, RESOURCE_QUERY_PARAM, id).parameter("includeInactive", "true").invoke(User.class);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Build and executes the request to create the provided new user resource at
   ** the Service Provider.
   **
   ** @param  user               the new user resource to create.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the successfully created Rest User resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException   if an error occurs.
   */
  public User createAccount(final User user)
    throws SystemException {

    final String method = "createAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return createRequest(ENDPOINT_USER).invoke(user);
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
  // Method:   resetPassword
  /**
   ** Set the password of an account at the Service Provider belonging to
   ** <code>id</code>.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void resetPassword(final String id, final String password)
    throws SystemException {

    final String method = "resetPassword";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      passwordRequest(USER_QUERY_PARAM, id).invoke(password);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
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

    return deleteRequest(ENDPOINT_USER, USER_QUERY_PARAM, id);
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
   ** @return                    the successfully modified Jira user
   **                            resource.
   **                            <br>
   **                            Possible object is {@link Modify} for type
   **                            {@link User}.
   **
   ** @throws SystemException    if an error occurs.
   */
  private Modify<User> accountModify(final String id)
    throws SystemException {

    return modifyRequest(ENDPOINT_USER, USER_QUERY_PARAM, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchGroup
  /**
   ** Build a request to query and retrieve JIRA Group resources from the
   ** Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchGroup() {

    final String method = "searchGroup";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // IAM-438: Add possibility to (un)limit number of objects returned
      Search searchRequest = searchRequest(ENDPOINT_GROUPS);
      if (ENDPOINT_GROUPS_SIZE > 0) {
        searchRequest.parameter("maxResults", ENDPOINT_GROUPS_SIZE);
      }
      return searchRequest;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupGroup
  /**
   ** Build a request to query and retrieve JIRA Group resources from the
   ** Service Provider.
   **
   ** @param  groupName          the resource identifier (for example the value
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
  public Search lookupGroup(final String groupName) {

    final String method = "lookupGroup";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_GROUP_MEMBER).parameter(GROUP_QUERY_PARAM, groupName).parameter("includeInactiveUsers", "true");
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignGroup
  /**
   ** Build and executes the request to assign jira <code>User</code> to a jira
   ** Project with specifed <code>Group</code>
   **
   ** @param  group              the identifier of the group to assign the
   **                            members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  beneficiary        the identifier of the user to assign as
   **                            a member.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @return                    the successfully created REST Project Role
   **                            resource.
   **                            <br>
   **                            Possible object is {@link Project.Role}.
   **
   ** @throws SystemException    if an error occurs.
   */
  @SuppressWarnings("unchecked")
  public Group assignGroup(final String group,  final String beneficiary)
    throws SystemException {

    final String method = "assignGroup";
    trace(method, Loggable.METHOD_ENTRY);
    final Assign request = Assign.build(this.target.path(ENDPOINT_GROUP).path(ENDPOINT_USER).queryParam(GROUP_QUERY_PARAM, group));
    try {
      return (Group)request.invoke(beneficiary, Group.class);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeGroup
  /**
   ** Build and executes the request to revoke jira <code>User</code> to a jira
   ** Group
   **
   ** @param  group              the identifier of the group to revoke the
   **                            members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  beneficiary        the identifier of the user to revoke as
   **                            a member.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  void revokeGroup(String group, String beneficiary)
    throws SystemException {

    final String method = "revokeGroup";
    trace(method, Loggable.METHOD_ENTRY);
   final Delete request = Delete.build(this.target.path(ENDPOINT_GROUP).path(ENDPOINT_USER).queryParam(GROUP_QUERY_PARAM, group).queryParam(USER_QUERY_PARAM, beneficiary));
    try {
      request.invoke();
    }
    finally {
     trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRole
  /**
   ** Build a request to query and retrieve JIRA Project Role resources from
   ** the Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchRole() {

    final String method = "searchRole";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_ROLE);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchProject
  /**
   ** Build a request to query and retrieve JIRA Project resources from the
   ** Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchProject() {

    final String method = "searchProject";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return searchRequest(ENDPOINT_PROJECT);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProject
  /**
   ** Build a request to query and creat JIRA Project resources from the
   ** Service Provider.
   **
   ** @param  project            the new project resource to create.
   **                            <br>
   **                            Allowed object is {@link Project}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Project createProject(final Project project)
    throws SystemException {

    final String method = "createProject";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return createRequest(ENDPOINT_PROJECT).invoke(project);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupProject
  /**
   ** Build a request to query and retrieve JIRA Project resources from the
   ** Service Provider.
   **
   ** @param  id                 the project identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Lookup lookupProject(final String id)
    throws SystemException {

    final String method = "lookupProject";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return lookupRequest(ENDPOINT_PROJECT, id);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

//////////////////////////////////////////////////////////////////////////////
  // Method:   searchProjectRoles
  /**
   ** Build a request to query and returns JIRA Roles as list for the given
   ** project from the Service Provider.
   **
   ** @param  id                 the project identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   **
   ** @throws SystemException   if an error occurs.
   */
  @SuppressWarnings("unchecked")
  public List<String> searchProjectRoles(final String id)
    throws SystemException {

    final String method = "searchProjectRoles";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      Lookup request = Lookup.build(this.target.path(ENDPOINT_PROJECT).path(id).path(ENDPOINT_ROLE));
      Map result  = (Map)request.invoke(Map.class);

      return new ArrayList<String>(result.keySet());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupProjectRole
  /**
   ** Build a request to query and retrieve JIRA Project Role resources from
   ** the Service Provider.
   **
   ** @param  projectId          the project identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  roleId             the role identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   **
   ** @throws SystemException   if an error occurs.
   */
  public Lookup lookupProjectRole(final String projectId, final String roleId)
    throws SystemException {

    final String method = "lookupProjectRole";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return lookupRequest(ENDPOINT_PROJECT, projectId, ENDPOINT_ROLE, roleId);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProjectRole
  /**
   ** Build and executes the request to create the provided new Project Role at
   ** the Service Provider.
   **
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is {@link Project.Role}.
   **
   ** @return                    the successfully created REST Project Role
   **                            resource.
   **                            <br>
   **                            Possible object is {@link Project.Role}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Project.Role createProjectRole(final Project.Role resource)
    throws SystemException {

    final String method = "createProjectRole";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return createRequest(ENDPOINT_ROLE).invoke(resource);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignProject
  /**
   ** Build and executes the request to assign jira <code>User</code> to a jira
   ** Project with specifed <code>Project.Role</code>
   **
   ** @param  project            the identifier of the project to assign the
   **                            members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  role               the identifier of the role to assign the
   **                            members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  beneficiary        the identifier of the user to assign as
   **                            a member.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  @SuppressWarnings("unchecked")
  public void assignProject(final String project, final String role, final String beneficiary)
    throws SystemException {

    final String method = "assignProject";
    trace(method, Loggable.METHOD_ENTRY);
    final Assign request = Assign.build(this.target.path(ENDPOINT_PROJECT).path(project).path(ENDPOINT_ROLE).path(role));
    try {
      request.invoke(beneficiary, Project.Role.class);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeProject
  /**
   ** Build and executes the request to revoke jira <code>User</code> to a jira
   ** Project on the specifed <code>Project.Role</code>
   **
   ** @param  project            the identifier of the project to assign the
   **                            members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  role               the identifier of the role to assign the
   **                            members for.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  beneficiary        the identifier of the user to assign as
   **                            a member.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void revokeProject(final String project, final String role, final String beneficiary)
    throws SystemException {

    final String method = "revokeProject";
    trace(method, Loggable.METHOD_ENTRY);
    final Delete request = Delete.build(this.target.path(ENDPOINT_PROJECT).path(project).path(ENDPOINT_ROLE).path(role).queryParam("user", beneficiary));
    try {
      request.invoke();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
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
   **                              <li>"<code>Projects</code>"
   **                              <li>"<code>Roles</code>"
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
  protected Search searchRequest(final String context) {
    return Search.build(this.target.path(context));
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
   **                              <li>"<code>projects</code>"
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
  @SuppressWarnings("unchecked")
  public <T extends Adressable> Lookup<T> lookupRequest(final String context, final String id) {
    return Lookup.<T>build(requestTarget(context, id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known REST resource from the Service Provider.
   ** <br>
   ** Use this method to lookup a resource with query parameter inside the URI.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>user</code>"
   **                              <li>"<code>group</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  keyParameter       the query parameter name that identify the
   **                            resource (for example the name of the parameter
   **                            for the "<code>id</code>" attribute).
   **                            <br>
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
  @SuppressWarnings("unchecked")
  public <T extends Adressable> Lookup<T> lookupRequest(final String context, final String keyParameter, final Object id) {
    return (Lookup<T>)Lookup.<T>build(this.target.path(context)).parameter(keyParameter, id);
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
   **                              <li>"<code>ProjectRoles</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  contextId          the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subContext         The sub resource context URI such as
   **                            <ul>
   **                              <li>"<code>roles</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   ** @param  subContextId       the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public <T extends Adressable> Lookup<T> lookupRequest(final String context, final String contextId, final String subContext, final String subContextId) {
    return Lookup.<T>build(requestTarget(context, contextId).path(subContext).path(subContextId));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   ** Build a request to create the provided new JIRA resource at the Service
   ** Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>groups</code>"
   **                              <li>"<code>user</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  protected <T extends Resource> Create<T> createRequest(final String context) {
    return Create.<T>build(this.target.path(context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Build a request to delete a known REST resource from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  keyParameter       the query parameter name that identify the
   **                            resource (for example the name of the parameter
   **                            for the "<code>id</code>" attribute).
   **                            <br>
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
  private Delete deleteRequest(final String context, final String keyParameter, final String id) {
    return Delete.build(target(requestTarget(context).getUri())).parameter(keyParameter, id);
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
   **                              <li>"<code>groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  query              the query parameter name that identify the
   **                            resource (for example the name of the parameter
   **                            for the "<code>id</code>" attribute).
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
   **                            Possible object is {@link Modify} for type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  private <T extends Resource> Modify<T> modifyRequest(final String context, final String query, final String id) {
    return (Modify<T>)Modify.<T>build(this.target.path(context)).parameter(query, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordRequest
  /**
   ** Build a request to reset a password at the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  query              the query parameter name that identify the
   **                            resource (for example the name of the parameter
   **                            for the "<code>id</code>" attribute).
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
   **                            Possible object is {@link Password}.
   */
  private Password passwordRequest(final String query, final String id) {
    return Password.build(this.target.path(ENDPOINT_PASSWORD)).parameter(query, id);
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
   **                              <li>"<code>user</code>"
   **                              <li>"<code>role</code>"
   **                              <li>"<code>project</code>"
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
   **                              <li>"<code>user</code>"
   **                              <li>"<code>role</code>"
   **                              <li>"<code>project</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  private final WebTarget requestTarget(final String context) {
    return this.target.path(context);
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