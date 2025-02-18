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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana;

import java.util.List;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.connector.grafana.schema.User;
import oracle.iam.identity.icf.connector.grafana.schema.Role;
import oracle.iam.identity.icf.connector.grafana.schema.Team;
import oracle.iam.identity.icf.connector.grafana.schema.Entity;
import oracle.iam.identity.icf.connector.grafana.schema.Grafana;
import oracle.iam.identity.icf.connector.grafana.schema.RoleMember;
import oracle.iam.identity.icf.connector.grafana.schema.Organization;

import oracle.iam.identity.icf.connector.grafana.request.Search;
import oracle.iam.identity.icf.connector.grafana.request.Lookup;
import oracle.iam.identity.icf.connector.grafana.request.Create;
import oracle.iam.identity.icf.connector.grafana.request.Delete;
import oracle.iam.identity.icf.connector.grafana.request.Assign;
import oracle.iam.identity.icf.connector.grafana.request.Revoke;
import oracle.iam.identity.icf.connector.grafana.request.Resolve;
import oracle.iam.identity.icf.connector.grafana.request.Permission;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends ServiceContext {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The endpoint to access the Grafana API.
   **/
  public final WebTarget target;

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
  // Method:   searchUser
  /**
   ** Build a request to query and retrieve Grafana {@link User} resources from
   ** the Service Provider.
   **
   ** @param  start              the 1-based index of the first page result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter used to query a subset of user
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link User}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  public List<User> searchUser(final int start, final int count, final String criteria)
    throws SystemException {

    final String method = "searchUser";
    entering(method);
    try {
      final List<User> result = Search.user(this.target).page(start, count).filter(criteria).invoke(User.class);
      for (User cursor : result) {
        // populate all teams a user belongs to
        cursor.team(Search.team(this.target, cursor.id()).invoke(Team.class));
        // populate all organizations a user belongs to
        cursor.organization(Search.organization(this.target, cursor.id()).invoke(Organization.class));
        // populate roles a user belongs to only if the feature is licensed
        if (endpoint().enterpriseFeature())
          cursor.role(Search.role(this.target, cursor.id()).invoke(RoleMember.class));
      }
      return result;
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupUser
  /**
   ** Lookup a certain user by its internal identifier and returns that identity
   ** if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Grafana user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  public User lookupUser(final String id)
    throws SystemException {

    final String method = "lookupUser";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final User result = Lookup.user(this.target, id).invoke(User.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Grafana.ENTITY_USER, Entity.ID, id));

      // populate all teams a user belongs to
      result.team(Search.team(this.target, result.id()).invoke(Team.class));
      // populate all organizations a user belongs to
      result.organization(Search.organization(this.target, result.id()).invoke(Organization.class));
      // populate roles a user belongs to only if the feature is licensed
      if (endpoint().enterpriseFeature())
        result.role(Search.role(this.target, result.id()).invoke(RoleMember.class));
      return result;
    }
    // if result is not as expected just rethrow
    catch (ServiceException e) {
      // simply rethrow
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServiceException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveUser
  /**
   ** Lookup a certain user by its user name and returns the internal system
   ** identifier of that identity if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>userName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Grafana user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  public User resolveUser(final String name)
    throws SystemException {

    final String method = "resolveUser";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final User result = Resolve.user(this.target, name).invoke(User.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Grafana.ENTITY_USER, User.LOGIN, name));

      return result;
    }
    // if result is not as expected just rethrow
    catch (ServiceException e) {
      // simply rethrow
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServiceException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUser
  /**
   ** Build and executes the request to create the provided new user resource at
   ** the Service Provider.
   **
   ** @param  subject            the new user resource to create.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the internal identifier of the REST user resource
   **                            created.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException   if an error occurs.
   */
  public String createUser(final User subject)
    throws SystemException {

    final String method = "createUser";
    entering(method);
    String id = "-1";
    try {
      // preform the actions required to create a user account
      id = Create.user(this.target).invoke(subject).id();
      try {
        // preform the actions required to grant global permissions to that user
        // account
        Permission.administrator(this.target, id).invoke(User.permission(subject.administrator()));
        // preform the actions required to grant organization permissions to
        // that user account
        Permission.administrator(this.target, id).invoke(User.permission(subject.administrator()));
      }
      catch (SystemException e) {
        Delete.user(this.target, id);
      }
    }
    finally {
      exiting(method);
    }
    return id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteUser
  /**
   ** Deletes a known REST user resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation failes.
   */
  public void deleteUser(final String id)
    throws SystemException {

    final String method = "deleteUser";
    entering(method);
    try {
      Delete.user(this.target, id).invoke();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRole
  /**
   ** Build a request to query and retrieve Grafana {@link Role} resources from
   ** the Service Provider.
   **
   ** @param  start              the 1-based index of the first page result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter used to query a subset of user
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Role}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  public List<Role> searchRole(final int start, final int count, final String criteria)
    throws SystemException {

    final String method = "searchRole";
    entering(method);
    try {
      return Search.role(this.target).page(start, count).filter(criteria).invoke(Role.class);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRole
  /**
   ** Lookup a certain role by its internal identifier and returns that identity
   ** if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            role not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of a Grafana
   **                            role resource.
   **                            <br>
   **                            Possible object is {@link Role}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  public Role lookupRole(final String id)
    throws SystemException {

    final String method = "lookupRole";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final Role result = Lookup.role(this.target, id).invoke(Role.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Grafana.ENTITY_ROLE, Entity.ID, id));
      return result;
    }
    // if result is not as expected just rethrow
    catch (ServiceException e) {
      // simply rethrow
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServiceException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchTeam
  /**
   ** Build a request to query and retrieve Grafana {@link Team} resources from
   ** the Service Provider.
   **
   ** @param  start              the 1-based index of the first page result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter used to query a subset of user
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Team}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  public List<Team> searchTeam(final int start, final int count, final String criteria)
    throws SystemException {

    final String method = "searchTeam";
    entering(method);
    try {
      return Search.team(this.target).page(start, count).filter(criteria).listTeam();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupTeam
  /**
   ** Lookup a certain team by its internal identifier and returns that identity
   ** if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            team not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of a Grafana
   **                            team resource.
   **                            <br>
   **                            Possible object is {@link Team}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  public Team lookupTeam(final String id)
    throws SystemException {

    final String method = "lookupTeam";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final Team result = Lookup.team(this.target, id).invoke(Team.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Grafana.ENTITY_TEAM, Entity.ID, id));
      return result;
    }
    // if result is not as expected just rethrow
    catch (ServiceException e) {
      // simply rethrow
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServiceException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveTeam
  /**
   ** Lookup a certain a team by its name and returns the internal system
   ** identifier of that identity if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>name</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Grafana user resource.
   **                            <br>
   **                            Possible object is {@link Team}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  public Team resolveTeam(final String name)
    throws SystemException {

    final String method = "resolveTeam";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final Team result = Resolve.team(this.target, name).invoke();
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Grafana.ENTITY_TEAM, Entity.NAME, name));

      return result;
    }
    // if result is not as expected just rethrow
    catch (ServiceException e) {
      // simply rethrow
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServiceException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTeam
  /**
   ** Build and executes the request to create the provided new team resource at
   ** the Service Provider.
   **
   ** @param  subject            the new team resource to create.
   **                            <br>
   **                            Allowed object is {@link Team}.
   **
   ** @return                    the internal identifier of the REST team
   **                            resource created.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException   if an error occurs.
   */
  public String createTeam(final Team subject)
    throws SystemException {

    final String method = "createTeam";
    entering(method);
    try {
      return Create.team(this.target).invoke(subject).id();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteTeam
  /**
   ** Deletes a known REST team resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation failes.
   */
  public void deleteTeam(final String id)
    throws SystemException {

    final String method = "deleteTeam";
    entering(method);
    try {
      Delete.team(this.target, id).invoke();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignTeam
  /**
   ** Assigns a <code>User</code> to a {@link Team} in Grafana Server.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link Team} resource.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            team not the unique name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void assignTeam(final String beneficiary, final String subject)
    throws SystemException {

    final String method = "assignTeam";
    entering(method);

    try {
      Assign.<Team>team(this.target, subject).invoke(beneficiary);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeTeam
  /**
   ** Revokes a <code>User</code> from a {@link Team} in Grafana Server.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link Team} resource.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            role not the unique name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void revokeTeam(final String beneficiary, final String subject)
    throws SystemException {


    final String method = "revokeTeam";
    entering(method);
    try {
      Revoke.team(this.target, subject, beneficiary).invoke();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchOrganization
  /**
   ** Build a request to query and retrieve Grafana {@link Organization}
   ** resources from the Service Provider.
   **
   ** @param  start              the 1-based index of the first page result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter used to query a subset of user
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Organization}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  public List<Organization> searchOrganization(final int start, final int count, final String criteria)
    throws SystemException {

    final String method = "searchOrganization";
    entering(method);
    try {
      // what a crappy API
      // the starting index of pagination at the /users endpoint is 1-based
      // the starting index of pagination at the /orgs endpoint is 0-based
      // are any of these idiots in these developer barracks trying to pay
      // attention to interface consistency?
      // that's exactly what I hate about these OpenSource freaks; Quality is
      // not their primary goal, they are just tinkering
      return Search.organization(this.target).page(start - 1 , count).filter(criteria).invoke(Organization.class);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrganization
  /**
   ** Lookup a certain organization by its internal identifier and returns that
   ** identity if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of an
   **                            organization not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of a Grafana
   **                            organization resource.
   **                            <br>
   **                            Possible object is {@link Organization}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  public Organization lookupOrganization(final String id)
    throws SystemException {

    final String method = "lookupOrganization";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final Organization result = Lookup.organization(this.target, id).invoke(Organization.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Grafana.ENTITY_ORGANIZATION, Entity.ID, id));
      return result;
    }
    // if result is not as expected just rethrow
    catch (ServiceException e) {
      // simply rethrow
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServiceException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrganization
  /**
   ** Lookup a certain an organization by its name and returns the internal
   ** identifier of that identity if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>name</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Grafana user resource.
   **                            <br>
   **                            Possible object is {@link Organization}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  public Organization resolveOrganization(final String name)
    throws SystemException {

    final String method = "resolveOrganization";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final Organization result = Resolve.organization(this.target, name).invoke(Organization.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Grafana.ENTITY_ORGANIZATION, Organization.NAME, name));

      return result;
    }
    // if result is not as expected just rethrow
    catch (ServiceException e) {
      // simply rethrow
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServiceException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrganization
  /**
   ** Build and executes the request to create the provided new organization
   ** resource at the Service Provider.
   **
   ** @param  subject            the new organization resource to create.
   **                            <br>
   **                            Allowed object is {@link Organization}.
   **
   ** @return                    the internal identifier of the REST
   **                            organization resource created.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException   if an error occurs.
   */
  public String createOrganization(final Organization subject)
    throws SystemException {

    final String method = "createOrganization";
    entering(method);
    try {
      return Create.organization(this.target).invoke(subject).id();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrganization
  /**
   ** Deletes a known REST organization resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation failes.
   */
  public void deleteOrganization(final String id)
    throws SystemException {

    final String method = "deleteOrganization";
    entering(method);
    try {
      Delete.organization(this.target, id).invoke();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignOrganization
  /**
   ** Assigns a <code>User</code> to an {@link Organization} in Grafana Server.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link Organization}
   **                            resource.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            organization not the unique name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the permission granted to the given resource
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void assignOrganization(final String beneficiary, final String subject, final String role)
    throws SystemException {

    final String method = "assignOrganization";
    entering(method);

    try {
      Assign.<Organization>organization(this.target, subject).invoke(beneficiary, role);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeOrganization
  /**
   ** Revokes a <code>User</code> from an {@link Organization} in Grafana Server.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link Organization}
   **                            resource.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            role not the unique name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void revokeOrganization(final String beneficiary, final String subject)
    throws SystemException {


    final String method = "revokeOrganization";
    entering(method);
    try {
      Revoke.organization(this.target, subject, beneficiary).invoke();
    }
    finally {
      exiting(method);
    }
  }
}