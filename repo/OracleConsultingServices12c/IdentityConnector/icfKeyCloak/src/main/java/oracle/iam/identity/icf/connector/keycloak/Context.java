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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 12c
7
    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak;

import oracle.iam.identity.icf.connector.keycloak.request.*;
import oracle.iam.identity.icf.connector.keycloak.schema.*;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;
import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceException;
import oracle.iam.identity.icf.rest.resource.ServiceBundle;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.List;

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

  /** the endpoint to access the Keycloak API **/
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
  // Method:   countUser
  /**
   ** Build a request to query the ammount of Keycloak {@link User} resources
   ** from the Service Provider.
   **
   ** @param  criteria           the filter used to query a subset of user
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  public final long countUser(final String criteria)
    throws SystemException {

    final String method = "countUser";
    entering(method);
    try {
      final Count<Long> request = Count.<Long>user(this.target).filter(criteria);
      return request.invoke(Long.class);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchUser
  /**
   ** Build a request to query and retrieve Keycloak User resources from the
   ** Service Provider.
   **
   ** @param  start              the 0-based index of the first query result.
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
        // populate all groups a user belongs to
        memberOfGroup(cursor);
        // populate all roles a user belongs to
        memberOfRole(cursor);
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
   ** Lookup a certain user by its internal system identifier and returns that
   ** identity if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the username.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Keycloak user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  @SuppressWarnings("unchecked")
  public User lookupUser(final String id, final boolean memberships)
    throws SystemException {

    final String method = "lookupUser";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final User result = Lookup.user(this.target, id).invoke(User.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Keycloak.ENTITY_USER, Service.ID, id));

      if (memberships) {
        // populate all groups a user belongs to
        memberOfGroup(result);
        // populate all roles a user belongs to
        memberOfRole(result);
      }
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
   **                            Keycloak user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  @SuppressWarnings("unchecked")
  public User resolveUser(final String name)
    throws SystemException {

    final String method = "resolveUser";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final User result = Resolve.user(this.target, name).invoke(User.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Keycloak.ENTITY_USER, Service.USERNAME, name));

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
  @SuppressWarnings("unchecked")
  public User createUser(final User user)
    throws SystemException {

    final String method = "createUser";
    entering(method);
    try {
      Create.user(this.target).invoke(user);
      // Keycloak is such a dump system: on any transactional operation it
      // returns the response status only therefore we need to call back the
      // content by our own to get back the created user
      // lookup the identity with the smallest possible set of attributes
      final User result = Resolve.user(this.target, user.username()).invoke(User.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Keycloak.ENTITY_USER, Service.USERNAME, user.username()));

      return result;
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyUser
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
  public User modifyUser(final String id, final User resource)
    throws SystemException {

    final String method = "modifyUser";
    entering(method);
    try {
      return Modify.user(this.target, id).invoke(resource);
    }
    finally {
      exiting(method);
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
   ** @param  temporary          whether the password <code>Credential</code> is
   **                            temporarly usebale only.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the password {@link Credential} created.
   **                            <br>
   **                            Possible object is {@link Credential}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Credential resetPassword(final String id, final String password, final Boolean temporary)
    throws SystemException {

    final String method = "resetPassword";
    entering(method);
    final Credential credential = Credential.password("Identity Governance Password", password, temporary);
    try {
      Password.reset(this.target, id).invoke(credential);
    }
    finally {
      exiting(method);
    }
    return credential;
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
  // Method:   countRole
  /**
   ** Build a request to query the ammount of Keycloak {@link Role} resources
   ** from the Service Provider.
   **
   ** @param  criteria           the filter used to query a subset of role
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  public final long countRole(final String criteria)
    throws SystemException {

    final String method = "countRole";
    entering(method);
    try {
      final Count<Long> request = Count.<Long>role(this.target).filter(criteria);
      return request.invoke(Long.class);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRole
  /**
   ** Build a request to query and retrieve Keycloak role resources from the
   ** Service Provider.
   **
   ** @param  start              the 0-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter used to query a subset of role
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
   ** @throws SystemException    if the operation failes.
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
  // Method:   searchClientRole
  /**
   ** Build a request to query and retrieve Keycloak role resources from the
   ** Service Provider.
   **
   ** @param  start              the 0-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter used to query a subset of role
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
   ** @throws SystemException    if the operation failes.
   */
  @SuppressWarnings("unchecked")
  public List<Client> searchClientRole(final int start, final int count, final String criteria)
          throws SystemException {

    final String method = "searchRole";
    entering(method);
    try {
      List<Client> result = Search.client(this.target).page(start, count).filter(criteria).invoke(Client.class);
      for (Client cursor : result) {
        List<Role> roles = Search.clientRole(this.target, cursor.id()).page(start, count).filter(criteria).invoke(Role.class);
        cursor.role(roles);
      }
      return result;
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupGroup
  /**
   ** Lookup a certain role by its internal system identifier and returns that
   ** identity if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to
   **                            match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a role
   **                            not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Keycloak role resource.
   **                            <br>
   **                            Possible object is {@link Role}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  @SuppressWarnings("unchecked")
  public Group lookupGroup(final String id)
    throws SystemException {

    final String method = "lookupGroup";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final Group result = Lookup.group(this.target, id).invoke(Group.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Keycloak.ENTITY_GROUP, Service.ID, id));
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
  // Method:   resolveRole
  /**
   ** Lookup a certain role by its name and returns the internal system
   ** identifier of that identity if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>name</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Keycloak role resource.
   **                            <br>
   **                            Possible object is {@link Role}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  @SuppressWarnings("unchecked")
  public Role resolveRole(final String name)
    throws SystemException {

    final String method = "resolveRole";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final Role result = Resolve.role(this.target, name).invoke(Role.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Keycloak.ENTITY_ROLE, Service.NAME, name));
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
  // Method:   assignRole
  /**
   ** Assigns a <code>User</code> to a {@link Role} in Keycloak.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the {@link Role} resources to assign.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void assignRole(final String beneficiary, final Role subject)
    throws SystemException {

    final String method = "assignRole";
    entering(method);
    try {
      Assign.role(this.target, beneficiary).invoke(CollectionUtility.list(subject));
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignClientRole
  /**
   ** Assigns a <code>User</code> to {@link Client} Roles in Keycloak.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  client             the identifier of a <code>Client</code> resource.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a role
   **                            not the role name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the {@link Client} resources to assign.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void assignClientRole(final String beneficiary, final String client, final Role subject)
          throws SystemException {

    final String method = "assignRole";
    entering(method);
    try {
      Assign.role(this.target, beneficiary, client).invoke(CollectionUtility.list(subject));
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeRole
  /**
   ** Revokes a <code>User</code> from a {@link Role} in Keycloak.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the {@link Role} resource to revoke.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void revokeRole(final String beneficiary, final Role subject)
    throws SystemException {

    final String method = "revokeRole";
    entering(method);
    try {
      Revoke.role(this.target, beneficiary).invoke(CollectionUtility.list(subject));
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeClientRole
  /**
   ** Revokes a <code>User</code> from a {@link Role} in Keycloak.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  client             the identifier of a <code>Client</code> resource.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a role
   **                            not the role name.
   **                            <br>
   **
   ** @param  subject            the {@link Role} resource to revoke.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void revokeClientRole(final String beneficiary, final String client,  final Role subject)
          throws SystemException {

    final String method = "revokeClientRole";
    entering(method);
    try {
        Revoke.role(this.target, beneficiary, client).invoke(CollectionUtility.list(subject));
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countGroup
  /**
   ** Build a request to query the ammount of Keycloak {@link Group} resources
   ** from the Service Provider.
   **
   ** @param  criteria           the filter used to query a subset of group
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  public final long countGroup(final String criteria)
    throws SystemException {

    final String method = "countRole";
    entering(method);
    try {
      final Count<Total> request = Count.<Total>group(this.target).filter(criteria);
      return request.invoke(Total.class).longValue();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchGroup
  /**
   ** Build a request to query and retrieve Keycloak group resources from the
   ** Service Provider.
   **
   ** @param  start              the 0-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter used to query a subset of group
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
    **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Group}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  public List<Group> searchGroup(final int start, final int count, final String criteria)
    throws SystemException {

    final String method = "searchGroup";
    entering(method);
    try {
      return (List<Group>) Search.group(this.target).page(start, count).filter(criteria).invoke(Group.class);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRole
  /**
   ** Lookup a certain role by its internal system identifier and returns that
   ** identity if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a role
   **                            not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of a Keycloak
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
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Keycloak.ENTITY_ROLE, Service.ID, id));
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
  // Method:   resolveGroup
  /**
   ** Lookup a certain group by its user name and returns the internal system
   ** identifier of that identity if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>name</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Keycloak group resource.
   **                            <br>
   **                            Possible object is {@link Group}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ServiceException   if the desired identity does not exists.
   */
  public Group resolveGroup(final String name)
    throws SystemException {

    final String method = "resolveGroup";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      final Group result = Resolve.group(this.target, name).invoke(Group.class);
      if (result == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Keycloak.ENTITY_GROUP, Service.NAME, name));
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
  // Method:   assignGroup
  /**
   ** Assigns a <code>User</code> to a {@link Group} in Keycloak.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link Group} resource.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a group
   **                            not the unique name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void assignGroup(final String beneficiary, final String subject)
    throws SystemException {

    final String method = "assignGroup";
    entering(method);

    try {
      Assign.group(this.target, beneficiary, subject).invoke();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeGroup
  /**
   ** Revokes a <code>User</code> from a {@link Group} in Keycloak.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link Group} resource.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a role
   **                            not the unique name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void revokeGroup(final String beneficiary, final String subject)
    throws SystemException {


    final String method = "revokeGroup";
    entering(method);
    try {
      Revoke.group(this.target, beneficiary, subject).invoke();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOfGroup
  /**
   ** Build a request to query and retrieve Keycloak group resources from the
   ** Service Provider that ar granted to the specified {@link User}
   ** represented by <code>beneficiary</code>.
   **
   ** @param  beneficiary        the {@link User} resource to populate the
   **                            membership in {@link Group}s for.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  public void memberOfGroup(final User beneficiary)
    throws SystemException {

    final String method = "memberOfGroup";
    entering(method);
    try {
      beneficiary.group(Search.memberOfGroup(this.target, beneficiary.id()).page(0, -1).invoke(Group.class));
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOfRole
  /**
   ** Build a request to query and retrieve Keycloak role resources from the
   ** Service Provider that ar granted to the specified {@link User}
   ** represented by <code>beneficiary</code>.
   **
   ** @param  beneficiary        the {@link User} resource to populate the
   **                            membership in realm {@link Role}s and client
   **                            {@link Role}s for.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void memberOfRole(final User beneficiary)
    throws SystemException {

    final String method = "memberOfRole";
    entering(method);
    try {
      // populate all roles a user belongs to
      final Member member = Lookup.member(this.target, beneficiary.id()).invoke(Member.class);
      beneficiary.role(member.realm());
      beneficiary.client(member.client() != null ? new ArrayList<>(member.client().values()) : new ArrayList<>());
    }
    finally {
      exiting(method);
    }
  }
}