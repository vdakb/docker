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
    Subsystem   :   Identity Governance Connector

    File        :   Service.java
    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Service.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.oig;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import oracle.iam.accesspolicy.vo.AccessPolicy;
import oracle.iam.identity.exception.AccessDeniedException;
import oracle.iam.identity.exception.NoSuchOrganizationException;
import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.exception.RoleGrantException;
import oracle.iam.identity.exception.RoleGrantRevokeException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.RoleSearchException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.UserMembershipException;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.jes.integration.oig.aph.service.APHPolicyEvaluation;
import oracle.iam.identity.icf.connector.oig.schema.ServiceSchema;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.jes.ServerContext;
import oracle.iam.identity.icf.jes.ServerEndpoint;
import oracle.iam.identity.icf.jes.ServerError;
import oracle.iam.identity.icf.jes.ServerException;
import oracle.iam.identity.icf.resource.ServerBundle;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleManagerResult;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.utils.Constants;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authopss.vo.AdminRole;
import oracle.iam.platform.authopss.vo.AdminRoleMembership;
import oracle.iam.platform.authopss.vo.AdminRoleRuleScope;
import oracle.iam.platform.authopss.vo.AdminRoleVO;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platformservice.api.AdminRoleService;

///////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** This is connector server specific context class that extends Java Enterprise
 ** Service interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Service extends ServerContext<Service> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The system identifier of organization <code>Top</code>
   */
  public static final String      TOP              = "3";
  /**
   ** The collections of system identifiers of roles that are never part of an
   ** operation this application context performs.
   */
  public static final Set<String> preventRevoke    = CollectionUtility.set(
    ServiceSchema.ROLE_ALLUSERS
  , ServiceSchema.ROLE_SYSADMIN
  , ServiceSchema.ROLE_SELFOPER
  , ServiceSchema.ROLE_OPERATORS
  );
  /**
   ** The collections of system identifiers of user that are never part of an
   ** operation this application context performs
   */
  public static final Set<Long>   preventOperation = CollectionUtility.set(
    /** System Administrator (xelsysadm) */
    Long.valueOf(1L)
    /** System Operator (xeloperator) */
  , Long.valueOf(2L)
    /** Weblogic User (weblogic) */
  , Long.valueOf(3L)
    /** Internal User (oiminternal) */
  , Long.valueOf(4L)
  );
  
  /**
   ** SQL Query to get Access Policy attributes with policy owner name
   ** based on the owner type. This SQL is used in the OURole lookup 
   ** reconciliation
   */
  private static final String ACCESS_POLICY_SQL =
    " SELECT * FROM " + 
    " ( " + 
    "  SELECT pol.pol_key, pol.pol_name, pol.pol_owner_type owner_type, usr.usr_login owner" + 
    "  FROM  pol " + 
    "  JOIN  usr ON pol.pol_owner = usr.usr_key " + 
    "  WHERE pol.pol_owner_type = 'USER' " + 
    " UNION " + 
    "  SELECT pol.pol_key, pol.pol_name, pol.pol_owner_type owner_type, ugp.ugp_name owner " + 
    "  FROM pol " + 
    "  JOIN ugp ON pol.pol_owner = ugp.ugp_key " + 
    "  WHERE pol.pol_owner_type = 'ROLE' " + 
    " ) " + 
    " WHERE 1 = 1 ";
  
  
  

  /**
   ** The collections of grant option applied on system roles to force to grant
   ** a role immediately for an endless time
   */
  @SuppressWarnings("unchecked")
  static final List<Map<String, Serializable>> PERIOD = CollectionUtility.list(
    Collections.singletonMap("startDate", null)
  , Collections.singletonMap("endDate",   null)
  );

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Service</code> which is associated with the
   ** specified {@link ServerEndpoint} for configuration purpose.
   **
   ** @param  endpoint           the {@link ServerEndpoint}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServerEndpoint}.
   */
  protected Service(final ServerEndpoint endpoint) {
    // ensure inheritance
    super(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login (overridden)
  /**
   ** Establish a connection to Identity Manager server.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @throws SystemException    if an error occurs attempting to establish a
   **                            connection.
   */
  public abstract void login()
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logout
  /**
   ** Close a connection to Identity Manager server.
   */
  public abstract void logout();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shutdown
  /**
   ** Callback notification to signal dependency injection is done to perform
   ** free up resources and the instance becomes out of service.
   */
  public abstract void shutdown();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrganization
  /**
   ** Lookup a certain organization by its unique name and returns the internal
   ** system identifier of that organization if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Identity Governance organization resource.
   **                            <br>
   **                            Possible object is {@link Organization}.
   **
   ** @throws ServerException    if the desired organization does not exists.
   ** @throws SystemException    if the operation fails.
   */
  public Organization lookupOrganization(final String name)
    throws SystemException {

    return lookupOrganization(name, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUser
  /**
   ** Create a certain user for the login name and returns the internal system
   ** identifier of that identity if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved system identifier of
   **                            an Identity Governance user resource.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the desired identity does not exists.
   */
  public String createUser(final String name)
    throws SystemException {

    final String method = "createUser";
    entering(method);
    try {
      return lookupUser(name).getEntityId();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteUser
  /**
   ** Delete a certain user by the internal system identifier of that user.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ServerException    if the desired user does not exists.
   */
  public void deleteUser(final String id)
    throws SystemException {

    final String method = "deleteUser";
    entering(method);
    final Set<String> beneficiary = new HashSet<>();
    try {
      try {
        // validate that the user to delete exists; looks like an unnecessary
        // operation, but it helps us to populate the collection of users
        // straight away
        beneficiary.add(service(UserManager.class).getDetails(id, null, false).getEntityId());
      }
      // if operation not permitted
      catch (AccessDeniedException e) {
        throw ServerException.accessDenied("UserManager.getDetails");
      }
      // if user not exists
      catch (NoSuchUserException e) {
        throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, UserManagerConstants.USER_ENTITY, UserManagerConstants.AttributeName.USER_KEY.getId(), id));
      }
      // if operation fails
      catch (UserLookupException e) {
        throw ServerException.general(e);
      }
      // don't touch users that are system immanent
      if (!preventOperation.contains(Long.valueOf(id))) {
        // remove all admin roles assigned to a user to ensure that the next
        // time an account is created conflicts on assign system roles cannot
        // occure
        // admin roles have to removed first because some of them granting also
        // system roles which hopefully will be removed automatically
        try {
          final AdminRoleService service = service(AdminRoleService.class);
          // we don't need to query any additional criteria like role name,
          // scope etc., due to all memberships have to revoked
          final List<AdminRoleMembership> memberOf = service.listUsersMembership(id, null, null, false, null);
          for (AdminRoleMembership cursor : memberOf) {
            service.removeAdminRoleMembership(cursor);          }
        }
        // if operation not permitted
        catch (AccessDeniedException e) {
          throw ServerException.accessDenied("AdminRoleService.getAdminRolesForUser");
        }
        // remove all roles assigned to a user to ensure that the next time an
        // account is created conflicts on assign system roles cannot occure
        // but this procedure will only be applied on roles that are not
        // managed by the system it self (e.g. ALL USERS)
        try {
          final RoleManager service = service(RoleManager.class);
          // query for role grants that are directly assigned to the user in
          // scope
          final List<Role>  member  = service.getUserMemberships(id, false);
          for (Role cursor : member) {
            if (!preventRevoke.contains(cursor.getName())) {
              // revoke the role and trigger whether to evaluate policies or not
              // when user is revoked from the role
              final RoleManagerResult result = service.revokeRoleGrantDirect(cursor.getEntityId(), beneficiary, true);
              if (!"COMPLETED".equals(result.getStatus())) {
                final Map<String, String> reason = result.getFailedResults();
                throw ServerException.abort(reason.toString());
              }
            }
          }
        }
        // if operation not permitted
        catch (AccessDeniedException e) {
          throw ServerException.accessDenied("RoleManager.getUserMemberships");
        }
        // if validation or operation fails
        catch (ValidationFailedException | RoleGrantRevokeException | UserMembershipException e) {
          throw ServerException.general(e);
        }
      }
    }
    // rethrow any server exception occured
    catch (ServerException e) {
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchUser
  /**
   ** Reads all {@link User}s from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the search criteria based on which entries will
   **                            be retrieved from the backend. The
   **                            {@link SearchCriteria} operators supported are
   **                            <ul>
   **                              <li><code>AND</code>
   **                              <li><code>OR</code>
   **                              <li><code>NOT</code>
   **                              <li><code>GREATER_THAN</code>
   **                              <li><code>GREATER_EQUAL</code>
   **                              <li><code>LESS_THAN</code>
   **                              <li><code>LESS_EQUAL</code>
   **                              <li><code>EQUAL</code>
   **                              <li><code>NOT_EQUAL</code>
   **                            </ul>
   **                            For additional comparisons like contains the
   **                            {@link SearchCriteria} o0perator will be
   **                            <code>EQUAL</code> with value to be searched
   **                            will be '*&lt;value&gt;*'
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  startRow           the start row of a page to fetch from the
   **                            backend.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  lastRow            the last row of a page to fetch from the
   **                            backend.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    a {@link List} containing the available values
   **                            for the specified query string.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link User}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final List<User> searchUser(final SearchCriteria criteria, final Integer startRow, final Integer lastRow)
    throws SystemException {

    final String method = "searchUser";
    entering(method);
    final HashMap<String, Object> option = new HashMap<>();
    option.put(Constants.SEARCH_STARTROW, startRow);
    option.put(Constants.SEARCH_ENDROW,   lastRow);
    // build a search criteria that excludes system related users
    final SearchCriteria filter = new SearchCriteria(UserManagerConstants.AttributeName.USER_KEY.getId(), 10, SearchCriteria.Operator.GREATER_THAN);
    try {
      // perform operation
      return service(UserManager.class).search(criteria == null ? filter : new SearchCriteria(filter, criteria, SearchCriteria.Operator.AND), ServiceSchema.user, option);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("UserManager.search");
    }
    // if operation fails
    catch (UserSearchException e) {
      throw ServerException.general(e);
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupUser
  /**
   ** Lookup a certain user by its login name and returns the internal system
   ** identifier of that identity if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>loginName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Identity Governance user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ServerException    if the desired identity does not exists.
   ** @throws SystemException    if the operation fails.
   */
  public User lookupUser(final String name)
    throws SystemException {

    return lookupUser(name, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSystemRole
  /**
   ** Create a certain system role for the unique name and returns the internal
   ** system identifier of that identity if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved system identifier of
   **                            an Identity Governance role resource.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ServerException    if the desired identity does not exists.
   ** @throws SystemException    if the operation fails.
   */
  public String createSystemRole(final String name)
    throws SystemException {

    final String method = "createSystemRole";
    entering(method);
    try {
      return lookupSystemRole(name).getEntityId();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSystemRole
  /**
   ** Delete a certain system role for the internal system identifier of that
   ** system role if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ServerException    if the desired system role does not exists.
   */
  public void deleteSystemRole(final String id)
    throws SystemException {

    final String method = "deleteSystemRole";
    entering(method);
    try {
      service(RoleManager.class).getDetails(id, null);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("RoleManager.getDetails");
    }
    // if role not exists
    catch (NoSuchRoleException e) {
      throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, RoleManagerConstants.ROLE_ENTITY_NAME, RoleManagerConstants.ROLE_KEY, id));
    }
    // if operation fails
    catch (RoleLookupException e) {
      throw ServerException.general(e);
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSystemRole
  /**
   ** Reads all {@link Role}s from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the search criteria based on which entries will
   **                            be retrieved from the backend. The
   **                            {@link SearchCriteria} operators supported are
   **                            <ul>
   **                              <li><code>AND</code>
   **                              <li><code>OR</code>
   **                              <li><code>NOT</code>
   **                              <li><code>GREATER_THAN</code>
   **                              <li><code>GREATER_EQUAL</code>
   **                              <li><code>LESS_THAN</code>
   **                              <li><code>LESS_EQUAL</code>
   **                              <li><code>EQUAL</code>
   **                              <li><code>NOT_EQUAL</code>
   **                            </ul>
   **                            For additional comparisons like contains the
   **                            {@link SearchCriteria} o0perator will be
   **                            <code>EQUAL</code> with value to be searched
   **                            will be '*&lt;value&gt;*'
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  startRow           the start row of a page to fetch from the
   **                            backend.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  lastRow            the last row of a page to fetch from the
   **                            backend.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    a {@link List} containing the available values
   **                            for the specified query string.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Role}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final List<Role> searchSystemRole(final SearchCriteria criteria, final Integer startRow, final Integer lastRow)
    throws SystemException {

    final String method = "searchSystemRole";
    entering(method);
    // prepare a control option for the batch requested
    final HashMap<String, Object> option = new HashMap<String, Object>();
    option.put(Constants.SEARCH_STARTROW, startRow);
    option.put(Constants.SEARCH_ENDROW,   lastRow);
    // the criteria always to apply on the search to exclude any role that is
    // not in the namespace of OIM Roles (category key = 2)
    final SearchCriteria filter = new SearchCriteria(RoleManagerConstants.ROLE_CATEGORY_KEY, ServiceSchema.ROLE_CATEGORY, SearchCriteria.Operator.EQUAL);
    try {
      // perform operation
      return service(RoleManager.class).search(criteria == null ? filter : new SearchCriteria(filter, criteria, SearchCriteria.Operator.AND), ServiceSchema.role, option);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("RoleManager.search");
    }
    // if operation fails
    catch (RoleSearchException e) {
      throw ServerException.general(e);
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSystemRole
  /**
   ** Lookup a certain role by its unique name and returns the internal system
   ** identifier of that role if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of a an
   **                            Identity Governance role resource.
   **                            <br>
   **                            Possible object is {@link Role}.
   **
   ** @throws ServerException    if the desired role does not exists.
   ** @throws SystemException    if the operation fails.
   */
  public Role lookupSystemRole(final String name)
    throws SystemException {

    final String method = "lookupSystemRole";
    entering(method);
    // prepare a filter that match the name with the unqiue name of the role
    final SearchCriteria match = new SearchCriteria(RoleManagerConstants.ROLE_NAME, name, SearchCriteria.Operator.EQUAL);
    try {
      final List<Role> result = service(RoleManager.class).search(match, ServiceSchema.role, null);
      if (result == null || result.size() != 1)
        throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, RoleManagerConstants.ROLE_ENTITY_NAME, RoleManagerConstants.ROLE_NAME, name));
      return result.get(0);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("RoleManager.search");
    }
    // if operation fails
    catch (RoleSearchException e) {
      throw ServerException.general(e);
    }
    // if validation fails just rethrow
    catch (SystemException e) {
      // simply rethrow
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignSystemRole
  /**
   ** Assigns an <code>Identity</code> to a {@link Role} in Identity Governance.
   **
   ** @param  subject            the identifier of a <code>Role</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a role
   **                            not the unique name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void assignSystemRole(final String subject, final String beneficiary)
    throws SystemException {

    final String method = "assignSystemRole";
    entering(method);

    try {
      // grant the role and trigger whether to evaluate policies or not when
      // user is granted to the role
      // furthermore the role is granted immediately hance the optional
      // relationship attributes left null
      final RoleManagerResult result = service(RoleManager.class).grantRoleDirect(subject, Collections.singletonList(beneficiary), PERIOD, true);
      if (!"COMPLETED".equals(result.getStatus())) {
        final Map<String, String> reason = result.getFailedResults();
        throw ServerException.abort(reason.toString());
      }
    }
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("RoleManager.grantRole");
    }
    catch (ValidationFailedException e) {
      throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, RoleManagerConstants.ROLE_ENTITY_NAME, RoleManagerConstants.ROLE_NAME, subject));
    }
    catch (RoleGrantException e) {
      throw ServerException.abort(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeSystemRole
  /**
   ** Revokes an <code>Identity</code> from a {@link Role} in Identity
   ** Governance.
   **
   ** @param  subject            the identifier of a <code>Role</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a role
   **                            not the unique name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void revokeSystemRole(final String subject, final String beneficiary)
    throws SystemException {

    final String method = "revokeSystemRole";
    final Set<String> grantee = new HashSet<String>(1);
    grantee.add(beneficiary);
    try {
      // revoke the role and trigger whether to evaluate policies or not when
      // user is revoked from the role
      final RoleManagerResult result = service(RoleManager.class).revokeRoleGrantDirect(subject, grantee, true);
      if (!"COMPLETED".equals(result.getStatus())) {
        final Map<String, String> reason = result.getFailedResults();
        throw ServerException.abort(reason.toString());
      }
    }
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("RoleManager.revokeRoleGrant");
    }
    catch (ValidationFailedException e) {
      throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, RoleManagerConstants.ROLE_ENTITY_NAME, RoleManagerConstants.ROLE_NAME, subject));
    }
    catch (RoleGrantRevokeException e) {
      throw ServerException.abort(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userSystemRole
  /**
   ** Returns a list of user's role memberships based on the parameters.
   **
   ** @param  identifier         the system identifier of a <code>User</code>
   **                            identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the ollection of {@link Role}s granted to the
   **                            user represented by .
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Role}.
   **
   ** @throws ServerException    if the operation fails.
   */
  public List<Role> userSystemRole(final String identifier)
    throws SystemException {

    final String method = "userSystemRole";
    entering(method);

    // prepare a control option for the batch requested
    final HashMap<String, Object> option = new HashMap<>();
    option.put(Constants.SEARCH_STARTROW, 1);
    option.put(Constants.SEARCH_ENDROW,   Integer.MAX_VALUE);
    List<Role> result = null;
    try {
      result = service(RoleManager.class).getUserMemberships(
        identifier
        // apply a search criteria the correspondend to the criterias applied by
        // searchSystemRole
      , new SearchCriteria(RoleManagerConstants.ROLE_CATEGORY_KEY, ServiceSchema.ROLE_CATEGORY, SearchCriteria.Operator.EQUAL)
      , ServiceSchema.role
      , option
      , false
      );
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.getAdminRolesForUser");
    }
    // if role not exists
    catch (UserMembershipException e) {
      throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, UserManagerConstants.USER_ENTITY, UserManagerConstants.AttributeName.USER_KEY, identifier));
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleMember
  /**
   ** Return the collection of system identifiers of <code>User</code>s that are
   ** member of any standard Role in Identity Governance.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The system identifiers of
   ** <ul>
   **   <li>System Administrator (xelsysadm)
   **   <li>System Operator (xeloperator)
   **   <li>Weblogic User (weblogic)
   **   <li>Internal User (oiminternal)
   ** </ul>
   ** are never returned in the resulting collection.
   **
   ** @return                    the collection of system identifiers of
   **                            <code>User</code>s that are member of any
   **                            standard role in Identity Governance.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Long}.
   **
   ** @throws ServerException    if the operation fails.
   */
  public Set<Long> roleMember()
    throws SystemException {

    final String method = "roleMember";
    entering(method);
    final Set<Long> member = new HashSet<Long>();
    try {
      final List<Role> subject = searchSystemRole(null, 1, Integer.MAX_VALUE);
      for (Role role : subject) {
        final List<User> stock = systemRoleMember(role.getEntityId());
        for (User cursor : stock) {
          member.add(Long.valueOf(cursor.getEntityId()));
        }
      }
    }
    finally {
      exiting(method);
    }
    // remove privileged users (XELSYSADM, XELOPERATOR, WEBLOGIC, OIMINTERNAL)
    member.removeAll(preventOperation);
    return member;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGlobalRole
  /**
   ** Create a certain role for the unique name and returns the internal system
   ** identifier of that global admin role if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully system identifier of an
   **                            Identity Governance scoped admin role resource.
   **                            <br>
   **                            Possible object is {@link Long}.
   **
   ** @throws ServerException    if the desired identity does not exists.
   ** @throws SystemException    if the operation fails.
   */
  public Long createGlobalRole(final String name)
    throws SystemException {

    final String method = "createGlobalRole";
    entering(method);
    try {
      return lookupAdminRole(name, true).getRoleId();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteGlobalRole
  /**
   ** Delete a certain global admin role by the internal system identifier of
   ** that global admin role.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @throws ServerException    if the desired global admin role does not
   **                            exists.
   */
  public void deleteGlobalRole(final Long id)
    throws SystemException {

    final String method = "deleteGlobalRole";
    entering(method);
    try {
      lookupAdminRole(id, true);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchGlobalRole
  /**
   ** Reads all {@link AdminRole}s from the Service Provider.
   **
   ** @return                    a {@link List} containing the available
   **                            {@link AdminRole}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link AdminRole}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final List<AdminRole> searchGlobalRole()
    throws SystemException {

    final String method = "searchGlobalRole";
    entering(method);
    try {
      // perform operation
      return service(AdminRoleService.class).getGlobalAdminRoles();
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.getGlobalAdminRoles");
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupGlobalRole
  /**
   ** Lookup a certain global admin role by its unique name and returns the
   ** entity record of that role if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved entity record of a
   **                            an Identity Governance admin role resource.
   **                            <br>
   **                            Possible object is {@link AdminRole}.
   **
   ** @throws ServerException    if the desired role does not exists.
   ** @throws SystemException    if the operation fails.
   */
  public final AdminRole lookupGlobalRole(final String name)
    throws SystemException {

    return lookupAdminRole(name, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignGlobalRole
  /**
   ** Assigns an <code>Identity</code> to an global {@link AdminRole} in
   ** Identity Governance.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link AdminRole} that is
   **                            target of the operation.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of an
   **                            {@link AdminRole} not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public final void assignGlobalRole(final String beneficiary, final Long subject)
    throws SystemException {

    final String method = "assignGlobalRole";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final AdminRoleVO adminrole = lookupGlobalRole(subject);
      // create a assign operation by implicitly verifying that the entities
      // involved are valid
      assignAdminRole(adminrole, lookupUser(beneficiary, false));
      // apply the changes
      service(AdminRoleService.class).modifyAdminRole(adminrole);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.modifyAdminRole");
    }
    // if validation fails just rethrow
    catch (SystemException e) {
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeGlobalRole
  /**
   ** Revokes an <code>Identity</code> from an global {@link AdminRole} in
   ** Identity Governance.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link AdminRole} that is
   **                            target of the operation.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of an
   **                            {@link AdminRole} not the name.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public final void revokeGlobalRole(final String beneficiary, final Long subject)
    throws SystemException {

    final String method = "revokeGlobalRole";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final AdminRoleVO role = lookupGlobalRole(subject);
      // create a revoke operation by implicitly verifying that the entities
      // involved are valid
      revokeAdminRole(role, lookupUser(beneficiary, false));
      // apply the changes
      service(AdminRoleService.class).modifyAdminRole(role);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.modifyAdminRole");
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userGlobalRole
  /**
   ** Returns a list of user's scoped admin role memberships for the specified
   ** user represented by <code>identifier</code>.
   ** <br>
   ** If the specified <code>identifier</code> does not refere to a valid user
   ** the returned collection of {@link AdminRole}s is empty but never
   ** <code>null</code>.
   **
   ** @param  identifier         the system identifier of a <code>User</code>
   **                            identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AdminRole}s granted to the user.
   **                            <br>
   **                            Never <code>null</code>
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link AdminRoleMembership}.
   **
   ** @throws ServerException    if the operation fails.
   */
  public List<AdminRoleMembership> userGlobalRole(final String identifier)
    throws SystemException {

    final String method = "userGlobalRole";
    entering(method);
    try {
      return service(AdminRoleService.class).listUsersMembership(identifier, null, TOP, false, null);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.listUsersMembership");
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createScopedRole
  /**
   ** Create a certain role for the unique name and returns the internal system
   ** identifier of that scoped admin role if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully system identifier of an
   **                            Identity Governance scoped admin role resource.
   **                            <br>
   **                            Possible object is {@link Long}.
   **
   ** @throws ServerException    if the desired identity does not exists.
   ** @throws SystemException    if the operation fails.
   */
  public Long createScopedRole(final String name)
    throws SystemException {

    final String method = "createScopedRole";
    entering(method);
    try {
      return lookupAdminRole(name, false).getRoleId();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteScopedRole
  /**
   ** Delete a certain scoped admin role by the internal system identifier of
   ** that scoped admin role.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @throws ServerException    if the desired global admin role does not
   **                            exists.
   */
  public void deleteScopedRole(final Long id)
    throws SystemException {

    final String method = "deleteScopedRole";
    entering(method);
    try {
      lookupAdminRole(id, false);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchScopedRole
  /**
   ** Reads all {@link AdminRole}s from the Service Provider.
   **
   ** @return                    a {@link List} containing the available
   **                            {@link AdminRole}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link AdminRole}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final List<AdminRole> searchScopedRole()
    throws SystemException {

    final String method = "searchScopedRole";
    entering(method);
    try {
      // perform operation
      return service(AdminRoleService.class).getScopedAdminRoles();
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.getScopedAdminRoles");
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupScopedRole
  /**
   ** Lookup a certain scoped admin role by its unique name and returns the
   ** entity record of that role if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of a an
   **                            Identity Governance admin role resource.
   **                            <br>
   **                            Possible object is {@link AdminRole}.
   **
   ** @throws ServerException    if the desired role does not exists.
   ** @throws SystemException    if the operation fails.
   */
  public final AdminRole lookupScopedRole(final String name)
    throws SystemException {

    return lookupAdminRole(name, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignScopedRole
  /**
   ** Assigns an <code>Identity</code> to an global {@link AdminRole} in
   ** Identity Governance.
   ** <p>
   ** <b>Note</b>;
   ** <br>
   ** Implicitily validate the entities involved due to it's very important that
   ** only valid entities are passed to the API because it does not apply those
   ** required checks and the UI will not display admin role details anymore if
   ** the persistance layer becomes inconsistent.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link AdminRole} that is
   **                            target of the operation.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of an
   **                            {@link AdminRole} not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  scope              the identifier of an {@link Organization}
   **                            scope.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of an
   **                            {@link Organization} not the unique name.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  hierarchy          <code>true</code> if the permission should be
   **                            assigned delegated within in hierarchy;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws SystemException    if the operation fails.
   */
  public final void assignScopedRole(final String beneficiary, final Long subject, final String scope, final boolean hierarchy)
    throws SystemException {

    final String method = "assignScopedRole";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final AdminRoleVO  adminrole = lookupScopedRole(subject);
      // create a publish operation by implicitly verifying that the entities
      // involved are valid
      publishAdminRole(adminrole, lookupOrganization(scope, false), hierarchy);
      // create a assign operation by implicitly verifying that the entities
      // involved are valid
      assignAdminRole(adminrole, lookupUser(beneficiary, false));
      // apply the changes
      service(AdminRoleService.class).modifyAdminRole(adminrole);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.modifyAdminRole");
    }
    // if validation fails just rethrow
    catch (SystemException e) {
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeScopedRole
  /**
   ** Revokes an <code>Identity</code> from an global {@link AdminRole} in
   ** Identity Governance.
   ** <p>
   ** <b>Note</b>;
   ** <br>
   ** Implicitily validate the entities involved due to it's very important that
   ** only valid entities are passed to the API because it does not apply those
   ** required checks and the UI will not display admin role details anymore if
   ** the persistance layer becomes inconsistent.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the identifier of a {@link AdminRole} that is
   **                            target of the operation.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of an
   **                            {@link AdminRole} not the name.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public final void revokeScopedRole(final String beneficiary, final Long subject)
    throws SystemException {

    final String method = "revokeScopedRole";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final AdminRoleVO  adminrole = lookupScopedRole(subject);
      // create a revoke operation by implicitly verifying that the entities
      // involved are valid
      revokeAdminRole(adminrole, lookupUser(beneficiary, false));
      // apply the changes
      service(AdminRoleService.class).modifyAdminRole(adminrole);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.modifyAdminRole");
    }
    // if validation fails just rethrow
    catch (SystemException e) {
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userScopedRole
  /**
   ** Returns a list of user's scoped admin role memberships for the specified
   ** user represented by <code>identifier</code>.
   ** <br>
   ** If the specified <code>identifier</code> does not refere to a valid user
   ** the returned collection of {@link AdminRole}s is empty but never
   ** <code>null</code>.
   **
   ** @param  identifier         the system identifier of a <code>User</code>
   **                            identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collection of global {@link AdminRole}s
   **                            granted to the user.
   **                            <br>
   **                            Never <code>null</code> but might be empty.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link AdminRoleMembership}.
   **
   ** @throws ServerException    if the operation fails.
   */
  public List<AdminRoleMembership> userScopedRole(final String identifier)
    throws SystemException {

    final String method = "userScopedRole";
    entering(method);
    try {
      // the returned collection contains also the roles that are global with
      // the scope of Top
      final List<AdminRoleMembership> global = service(AdminRoleService.class).listUsersMembership(identifier, null, null, false, null);
      // return only those permisssion that meets the specific requirements of a
      // scoped admin role (isScoped = true)
      return global.stream().filter(e -> e.getAdminRole().isScoped()).collect(Collectors.toList());
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.listUsersMembership");
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adminRoleMember
  /**
   ** Return the collection of system identifiers of <code>User</code>s that are
   ** member of any administrative role in Identity Governance.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The system identifiers of
   ** <ul>
   **   <li>System Administrator (xelsysadm)
   **   <li>System Operator (xeloperator)
   **   <li>Weblogic User (weblogic)
   **   <li>Internal User (oiminternal)
   ** </ul>
   ** are never returned in the resulting collection.
   **
   ** @return                    the collection of system identifiers of
   **                            <code>User</code>s that are member of any
   **                            administrative role in Identity Governance.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Long}.
   **
   ** @throws ServerException    if the operation fails.
   */
  public Set<Long> adminRoleMember()
    throws SystemException {

    final String method = "adminRoleMember";
    entering(method);
    final Set<Long> member = new HashSet<Long>();
    try {
      for (Organization cursor : searchOrganization(null, 1, Integer.MAX_VALUE)) {
        // Gather users that have AdminRoles assigned
        final List<AdminRoleMembership> scope = adminRoleMember(String.valueOf(cursor.getAttribute(OrganizationManagerConstants.AttributeName.ID_FIELD.getId())));
        for (AdminRoleMembership subject : scope) {
          member.add(Long.valueOf(subject.getUserId()));
        }
      }
    }
    finally {
      exiting(method);
    }
    // remove privilegdes users (XELSYSADM, XELOPERATOR, WEBLOGIC, OIMINTERNAL)
    member.removeAll(preventOperation);
    exiting(method);
    return member;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrganization
  /**
   ** Create a certain organization for the unique name and returns the internal
   ** system identifier of that organization if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved system identifier of
   **                            an Identity Governance organization resource.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the desired identity does not exists.
   */
  public String createOrganization(final String name)
    throws SystemException {

    final String method = "createOrganization";
    entering(method);
    try {
      return lookupOrganization(name).getEntityId();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrganization
  /**
   ** Delete a certain organization by the internal system identifier of
   ** that organization.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ServerException    if the desired organization does not exists.
   */
  public void deleteOrganization(final String id)
    throws SystemException {

    final String method = "deleteOrganization";
    entering(method);
    try {
      service(OrganizationManager.class).getDetails(id, null, false);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("OrganizationManager.getDetails");
    }
    // if organization not exists
    catch (NoSuchOrganizationException e) {
      throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, OrganizationManagerConstants.ORG_ENTITY_NAME, OrganizationManagerConstants.AttributeName.ID_FIELD.getId(), id));
    }
    // if operation fails
    catch (OrganizationManagerException e) {
      throw ServerException.general(e);
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchOrganization
  /**
   ** Reads all {@link Organization}s from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the search criteria based on which entries will
   **                            be retrieved from the backend. The
   **                            {@link SearchCriteria} operators supported are
   **                            <ul>
   **                              <li><code>AND</code>
   **                              <li><code>OR</code>
   **                              <li><code>NOT</code>
   **                              <li><code>GREATER_THAN</code>
   **                              <li><code>GREATER_EQUAL</code>
   **                              <li><code>LESS_THAN</code>
   **                              <li><code>LESS_EQUAL</code>
   **                              <li><code>EQUAL</code>
   **                              <li><code>NOT_EQUAL</code>
   **                            </ul>
   **                            For additional comparisons like contains the
   **                            {@link SearchCriteria} o0perator will be
   **                            <code>EQUAL</code> with value to be searched
   **                            will be '*&lt;value&gt;*'
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  startRow           the start row of a page to fetch from the
   **                            backend.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  lastRow            the last row of a page to fetch from the
   **                            backend.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    a {@link List} containing the available values
   **                            for the specified query string.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Organization}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final List<Organization> searchOrganization(final SearchCriteria criteria, final Integer startRow, final Integer lastRow)
    throws SystemException {

    final String method = "searchOrganization";
    entering(method);
    // prepare a control option for the batch requested
    final HashMap<String, Object> option = new HashMap<>();
    option.put(Constants.SEARCH_STARTROW, startRow);
    option.put(Constants.SEARCH_ENDROW,   lastRow);
    // build a search criteria that excludes system related organizations
    final SearchCriteria filter = new SearchCriteria(
      new SearchCriteria(OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), "Xellerate Users", SearchCriteria.Operator.NOT_EQUAL)
    , new SearchCriteria(OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), "Requests",        SearchCriteria.Operator.NOT_EQUAL)
    , SearchCriteria.Operator.AND
    );
    try {
      // perform operation
      return service(OrganizationManager.class).search(criteria == null ? filter : new SearchCriteria(filter, criteria, SearchCriteria.Operator.AND), ServiceSchema.organization, option);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("OrganizationManager.search");
    }
    // if operation fails
    catch (OrganizationManagerException e) {
      throw ServerException.general(e);
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeOURole
  /**
   ** Revokes an <code>Identity</code> from an OU Role {@link AccessPolicy} in
   ** Identity Governance.
   ** <p>
   ** <b>Note</b>;
   ** <br>
   ** Implicitily validate the entities involved due to it's very important that
   ** only valid entities are passed to the API because it does not apply those
   ** required checks and the UI will not display admin role details anymore if
   ** the persistance layer becomes inconsistent.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  accessPolicyKey    the identifier of a {@link AccessPolicy} that is
   **                            target of the operation.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of an
   **                            {@link AccessPolicy} not the name.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  ouName             Organization Unit name liked with access policy                            
   **
   ** @throws SystemException    if the operation fails.
   */
  public final void revokeOURole(final String beneficiary, final Long accessPolicyKey, String ouName)
    throws SystemException {

    final String method = "revokeOURole";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // Execute policy evaluation on revoke operation
      APHPolicyEvaluation aph = new APHPolicyEvaluation();
      aph.removeOURole(Long.parseLong(beneficiary), accessPolicyKey, ouName);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.modifyAdminRole");
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchOURole
  /**
   ** Reads all {@link AccessPolicy}s from the Service Provider.
   **
   ** @return                    a {@link List} containing the available
   **                            {@link AdminRole}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link AdminRole}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final List<AccessPolicy> searchOURole(final SearchCriteria criteria)
    throws SystemException {

    final String method = "searchOURole";
    entering(method);
    
    try {
      // perform operation
      //return service(AdminRoleService.class).getScopedAdminRoles();
      List<AccessPolicy> policies = new ArrayList<AccessPolicy>();
      
      // Apply the filter, curretnly only owner and owner_type is supported
      String sqlCondition = "";
      if(criteria != null){
        if(criteria.getOperator() == SearchCriteria.Operator.AND){
          sqlCondition = "AND ("+convertToSQL((SearchCriteria)criteria.getFirstArgument()) + " AND " + convertToSQL((SearchCriteria)criteria.getSecondArgument())+")";
        }
        else if(criteria.getOperator() == SearchCriteria.Operator.OR){
          sqlCondition = "AND ("+convertToSQL((SearchCriteria)criteria.getFirstArgument()) + " OR " + convertToSQL((SearchCriteria)criteria.getSecondArgument())+")";
        }
        else{
          sqlCondition = "AND "+convertToSQL(criteria);
        }
      }
      
      // Execute SQL query
      Connection dbConnection = Platform.getOperationalDS().getConnection();
      String sqlStatement = ACCESS_POLICY_SQL + sqlCondition;
      trace(method, "SQL for access policy: "+sqlStatement);
      PreparedStatement ps = dbConnection.prepareStatement(sqlStatement);
      ResultSet rs = null;
      try{
        rs = ps.executeQuery();
        while(rs.next()){
          AccessPolicy ap = new AccessPolicy(rs.getString(1),false);
          ap.setName(rs.getString(2));
          ap.setOwnerType(AccessPolicy.OwnerType.getOwnerType(rs.getString(3)));
          ap.setOwnerDisplayName(rs.getString(4));
            
          policies.add(ap);
        }
      }
      catch(SQLException e){
        warning(method,"Unalbe to get access policy for SQL:\n"+sqlStatement);
        throw e;
      }
      finally{
        if(rs != null)
          rs.close();
        if(ps != null)
          ps.close();
        if(dbConnection != null)
          dbConnection.close();
      }
      
      return policies;
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("OURoleService.getOURoles");
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

 private  String convertToSQL(SearchCriteria criteria){
    String sqlCondition = "";
    String attribute = (String)criteria.getFirstArgument();
    String value = (String)criteria.getSecondArgument();
    SearchCriteria.Operator operator = criteria.getOperator();
    if(operator == SearchCriteria.Operator.EQUAL){
        sqlCondition = attribute +" = '"+value+"'";
    }
    if(operator == SearchCriteria.Operator.NOT_EQUAL){
        sqlCondition = attribute +" != '"+value+"'";
    }
    if(operator == SearchCriteria.Operator.BEGINS_WITH){
        sqlCondition = attribute +" LIKE('"+value+"%')";
    }
    if(operator == SearchCriteria.Operator.DOES_NOT_BEGIN_WITH){
      sqlCondition = attribute +" NOT LIKE('"+value+"%')";
    }
    if(operator == SearchCriteria.Operator.ENDS_WITH){
        sqlCondition = attribute +" LIKE('%"+value+"')";
    }
    if(operator == SearchCriteria.Operator.DOES_NOT_END_WITH){
        sqlCondition = attribute +" NOT LIKE(%"+value+"')";
    }
    if(operator == SearchCriteria.Operator.IN){
        sqlCondition = attribute +" IN ('"+value+"')";
    }
    if(operator == SearchCriteria.Operator.NOT_IN){
        sqlCondition = attribute +" NOT IN ('"+value+"')";
    }
    if(operator == SearchCriteria.Operator.GREATER_EQUAL){
        sqlCondition = attribute +" >= "+value+"";
    }
    if(operator == SearchCriteria.Operator.GREATER_THAN){
        sqlCondition = attribute +" > "+value+"";
    }
    if(operator == SearchCriteria.Operator.LESS_EQUAL){
        sqlCondition = attribute +" <= "+value+"";
    }
    if(operator == SearchCriteria.Operator.LESS_THAN){
        sqlCondition = attribute +" < "+value+"";
    }

    return sqlCondition;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns an instance of a Business Facade by invoking the method platform
   ** service resolver to return the appropriate instance of the desired
   ** Business Facade.
   ** <br>
   ** The utility factory keeps track of created Business Facade and on
   ** execution of close() will free all aquired resources of the created
   ** Business Facade.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @param  <T>                the expected class type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  protected abstract <T> T service(final Class<T> serviceClass);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemRoleMember
  /**
  /**
   ** Return the collection of system identifiers of <code>User</code>s that are
   ** member of any standard Role in Identity Governance specified by
   ** <code>identifier</code>.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The system identifiers of
   ** <ul>
   **   <li>System Administrator (xelsysadm)
   **   <li>System Operator (xeloperator)
   **   <li>Weblogic User (weblogic)
   **   <li>Internal User (oiminternal)
   ** </ul>
   ** are never returned in the resulting collection.
   **
   ** @param  identifier         the system identifier of the standard role to
   **                            populate the members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collection of system identifiers of
   **                            <code>User</code>s that are member of any
   **                            standard role in Identity Governance.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Long}.
   **
   ** @throws SystemException    if the operation fails.
   */
  private List<User> systemRoleMember(final String identifier)
    throws SystemException {

    final String method = "systemRoleMember";
    entering(method);
    try {
      return (service(RoleManager.class)).getRoleMembers(identifier, true);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("RoleManager.getRoleMembers");
    }
    // if operation fails
    catch (RoleMemberException e) {
      throw ServerException.general(e);
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupGlobalRole
  /**
   ** Lookup a certain global admin role by its internal system identifier and
   ** returns the entity record of that role if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the successfully retrieved record of a an
   **                            Identity Governance admin role resource.
   **                            <br>
   **                            Possible object is {@link AdminRoleVO}.
   **
   ** @throws ServerException    if the desired role does not exists.
   ** @throws SystemException    if the operation fails.
   */
  private final AdminRoleVO lookupGlobalRole(final Long id)
    throws SystemException {

    return lookupAdminRole(id, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupScopedRole
  /**
   ** Lookup a certain scoped admin role by its internal system and returns the
   ** entity record of that role if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the successfully retrieved record of a an
   **                            Identity Governance admin role resource.
   **                            <br>
   **                            Possible object is {@link AdminRoleVO}.
   **
   ** @throws ServerException    if the desired role does not exists.
   ** @throws SystemException    if the operation fails.
   */
  private final AdminRoleVO lookupScopedRole(final Long id)
    throws SystemException {

    return lookupAdminRole(id, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAdminRole
  /**
   ** Lookup a certain admin role by its internal system identifier of that role
   ** if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  global             <code>true</code> if global permissions has to
   **                            be retrieved; <code>false</code> if scoped
   **                            permissions has to be retrieved.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the successfully retrieved record of a an
   **                            Identity Governance admin role resource.
   **                            <br>
   **                            Possible object is {@link AdminRoleVO}.
   **
   ** @throws ServerException    if the desired role does not exists.
   ** @throws SystemException    if the operation fails.
   */
  private AdminRoleVO lookupAdminRole(final Long id, final boolean global)
    throws SystemException {

    final AdminRoleVO identity = lookupAdminRole(id);
    if (identity == null || (global && identity.getAdminRole().isScoped()))
      throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, global ? "Global Admin Role" : "Scoped Admin Role", "Id", id));

    return identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAdminRole
  /**
   ** Lookup a certain admin role by its internal system identifier of that role
   ** if its exists.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the successfully retrieved record of a an
   **                            Identity Governance admin role resource.
   **                            <br>
   **                            Possible object is {@link AdminRole}.
   **
   ** @throws ServerException    if the desired role does not exists.
   ** @throws SystemException    if the operation fails.
   */
  private AdminRoleVO lookupAdminRole(final Long id)
    throws SystemException {

    final String method = "lookupAdminRole";
    entering(method);
    try {
      return service(AdminRoleService.class).getAdminRoleVO(String.valueOf(id));
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.getAdminRoleVO");
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAdminRole
  /**
   ** Lookup a certain admin role by its unique name and returns the internal
   ** system identifier of that role if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  global             <code>true</code> if global permissions has to
   **                            be retrieved; <code>false</code> if scoped
   **                            permissions has to be retrieved.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the successfully retrieved record of a an
   **                            Identity Governance admin role resource.
   **                            <br>
   **                            Possible object is {@link AdminRole}.
   **
   ** @throws ServerException    if the desired role does not exists.
   ** @throws SystemException    if the operation fails.
   */
  private AdminRole lookupAdminRole(final String name, final boolean global)
    throws SystemException {

    final String method = "lookupAdminRole";
    entering(method);
    try {
      final AdminRole identity = service(AdminRoleService.class).getAdminRole(name);
      if (identity == null || (global && identity.isScoped()))
        throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, global ? "Global Admin Role" : "Scoped Admin Role", "Name", name));

      return identity;
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.getAdminRole");
    }
    catch (SystemException e) {
      // simply rethrow
      throw e;
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignAdminRole
  /**
   ** Assignes an <code>Identity</code> to an {@link AdminRole} in Identity
   ** Governance.
   **
   ** @param  subject            the identifier of the {@link AdminRole} that is
   **                            target of the operation.
   **                            <br>
   **                            Allowed object is {@link AdminRole}.
   ** @param  beneficiary        the <code>User</code> identity to be assigned
   **                            the administrative role.
   **                            <br>
   **                            Allowed object is {@link User}.
   */
  private void assignAdminRole(final AdminRoleVO subject, final User beneficiary) {
    final String method = "assignAdminRole";
    entering(method);
    final List<String> member   = subject.getUserAssignedStatic();
    if (!member.contains(beneficiary.getEntityId())) {
      member.add(beneficiary.getEntityId());
    }
    exiting(method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeAdminRole
  /**
   ** Revokes an <code>Identity</code> from an {@link AdminRole} in Identity
   ** Governance.
   **
   ** @param  subject            the {@link AdminRoleVO} that is target of the
   **                            operation.
   **                            <br>
   **                            Allowed object is {@link AdminRoleVO}.
   ** @param  beneficiary        the <code>User</code> identity to be revoke the
   **                            administrative role.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a
   **                            {@link User} not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void revokeAdminRole(final AdminRoleVO subject, final User beneficiary)
    throws SystemException {

    final String method = "revokeAdminRole";
    entering(method);
    final List<String> member = subject.getUserAssignedStatic();
    if (member.contains(beneficiary.getEntityId()))
      member.remove(beneficiary.getEntityId());
    exiting(method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  publishAdminRole
  /**
   ** Publish an {@link AdminRole} at the given scope .
   **
   ** @param  subject            the  {@link AdminRole} that needs to be
   **                            published.
   **                            <br>
   **                            Allowed object is {@link AdminRole}.
   ** @param  organization       the {@link Organization} scope to publish.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  hierarchy          <code>true</code> if the permission should be
   **                            assigned delegated within in hierarchy;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  private void publishAdminRole(final AdminRoleVO subject, final Organization organization, final boolean hierarchy) {
    final AdminRoleRuleScope rule = new AdminRoleRuleScope();
    rule.setHierarchicalScope(hierarchy);
    rule.setScopeId(Long.valueOf(organization.getEntityId()));
    final List<AdminRoleRuleScope> scope = subject.getAdminRoleRuleScopes();
    if (!scope.contains(rule)) {
      scope.add(rule);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adminRoleMember
  /**
   ** Returns a list of user's scoped admin role memberships for the specified
   ** user represented by <code>identifier</code>.
   ** <br>
   ** If the specified <code>identifier</code> does not refere to a valid user
   ** the returned collection of {@link AdminRole}s is empty but never
   ** <code>null</code>.
   **
   ** @param  identifier         the system identifier of an
   **                            <code>Organization</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of an
   **                            organization not the unique name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collection of global
   **                            {@link AdminRoleMembership}s granted to the
   **                            organizational scope.
   **                            <br>
   **                            Never <code>null</code> but might be empty.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link AdminRoleMembership}.
   **
   ** @throws SystemException    if the operation fails.
   */
  private List<AdminRoleMembership> adminRoleMember(final String identifier)
    throws SystemException {

    final String method = "adminRoleMember";
    entering(method);
    try {
      return service(AdminRoleService.class).listMembershipsInScope(identifier, null, false, new HashMap<String, Object>());
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("AdminRoleService.listMembershipsInScope");
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupUser
  /**
   ** Lookup a certain user by its login name and returns the internal system
   ** identifier of that identity if its exists.
   **
   ** @param  identifier         the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  loginName          <code>true</code> if <code>name</code> contains
   **                            user login name and <code>false</code> if
   **                            <code>name</code> contains the system
   **                            identifier of a user.
   **
   ** @return                    the successfully retrieved record of an
   **                            Identity Governance user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ServerException    if the desired identity does not exists.
   ** @throws SystemException    if the operation fails.
   */
  private User lookupUser(final String identifier, final boolean loginName)
    throws SystemException {

    final String method = "lookupUser";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      return service(UserManager.class).getDetails(identifier, null, loginName);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("UserManager.getDetails");
    }
    // if user not exists
    catch (NoSuchUserException e) {
      throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, UserManagerConstants.USER_ENTITY, loginName ? UserManagerConstants.AttributeName.USER_LOGIN.getId() : UserManagerConstants.AttributeName.USER_KEY.getId(), identifier));
    }
    // if operation fails
    catch (UserLookupException e) {
      throw ServerException.general(e);
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrganization
  /**
   ** Lookup a certain user by its login name and returns the internal system
   ** identifier of that identity if its exists.
   **
   ** @param  identifier               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  uniqueName          <code>true</code> if <code>name</code> contains
   **                            user login name and <code>false</code> if
   **                            <code>name</code> contains the system
   **                            identifier of a user.
   **
   ** @return                    the successfully retrieved record of an
   **                            Identity Governance user resource.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ServerException    if the desired identity does not exists.
   ** @throws SystemException    if the operation fails.
   */
  private Organization lookupOrganization(final String identifier, final boolean uniqueName)
    throws SystemException {

    final String method = "lookupOrganization";
    entering(method);
    try {
      // lookup the identity with the smallest possible set of attributes
      return service(OrganizationManager.class).getDetails(identifier, null, uniqueName);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw ServerException.accessDenied("OrganizationManager.getDetails");
    }
    // if organization not exists
    catch (NoSuchOrganizationException e) {
      throw ServerException.notFound(ServerBundle.string(ServerError.OBJECT_NOT_EXISTS, OrganizationManagerConstants.ORG_ENTITY_NAME, uniqueName ? OrganizationManagerConstants.AttributeName.ORG_NAME.getId() : OrganizationManagerConstants.AttributeName.ID_FIELD.getId(), identifier));
    }
    // if operation fails
    catch (OrganizationManagerException e) {
      throw ServerException.general(e);
    }
    // for any other reason
    catch (Exception e) {
      throw ServerException.unhandled(e);
    }
    finally {
      exiting(method);
    }
  }
}