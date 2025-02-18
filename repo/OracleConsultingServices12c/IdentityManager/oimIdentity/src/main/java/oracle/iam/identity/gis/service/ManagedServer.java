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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Identity Manager Connector

    File        :   ManagedServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ManagedServer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.gis.service;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;

import javax.naming.Context;

import oracle.iam.identity.exception.RoleGrantException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.exception.RoleSearchException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.AccessDeniedException;
import oracle.iam.identity.exception.UserMembershipException;
import oracle.iam.identity.exception.RoleGrantRevokeException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.RoleCategorySearchException;
import oracle.iam.identity.exception.OrganizationManagerException;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleCategory;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleCategoryManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.platform.authopss.vo.AdminRole;
import oracle.iam.platform.authopss.vo.AdminRoleMembership;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.platformservice.api.AdminRoleService;

import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractMetadataTask;

import oracle.iam.identity.foundation.rmi.IdentityServer;
import oracle.iam.identity.foundation.rmi.IdentityServerError;
import oracle.iam.identity.foundation.rmi.IdentityServerFeature;
import oracle.iam.identity.foundation.rmi.IdentityServerConstant;
import oracle.iam.identity.foundation.rmi.IdentityServerException;
import oracle.iam.identity.foundation.rmi.IdentityServerResource;

import oracle.iam.identity.gis.resource.ProvisioningBundle;

import oracle.iam.identity.gis.service.provisioning.ProvisioningError;

////////////////////////////////////////////////////////////////////////////////
// class ManagedServer
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>ManagedServer</code> is responsible to store and retrieve
 ** information from Identity Manager Managed Server.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ManagedServer extends IdentityServer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String RECON_MAP_ACCOUNT_USERLOGIN           = "userlogin";
  public static final String RECON_MAP_ACCOUNT_FNAME               = "firstname";
  public static final String RECON_MAP_ACCOUNT_LNAME               = "lastname";

  public static final String RECON_MAP_SYSTEM_PERMISSION           = "system";
  public static final String RECON_MAP_SYSTEM_PERMISSION_NAME      = "name";

  public static final String RECON_MAP_GLOBAL_PERMISSION           = "global";
  public static final String RECON_MAP_GLOBAL_PERMISSION_NAME      = "name";

  public static final String RECON_MAP_SCOPED_PERMISSION           = "scoped";
  public static final String RECON_MAP_SCOPED_PERMISSION_NAME      = "name";
  public static final String RECON_MAP_SCOPED_PERMISSION_HIERARCHY = "hierarchy";
  public static final String RECON_MAP_SCOPED_PERMISSION_SCOPE     = "scope";

  protected static final String ENCODEDCOLUMN                      = "Lookup Definition.Lookup Code Information.Code Key";
  protected static final String DECODEDCOLUMN                      = "Lookup Definition.Lookup Code Information.Decode";
  protected static final String SYSTEMPERMISSIONMAPPINGLK          = "GIS.System Permission.Mapping";

  static final String                   oimAdminRoleCategory       = "OIM Roles";
  static final Set<String>              preventRevoke              = new HashSet<String>();
  static final Set<String>              preventUsrRecon            = new HashSet<String>();

  static final Set<String>              roleAttribute              = new HashSet<String>();
  static final Set<String>              scopeAttribute             = new HashSet<String>();
  static final Set<String>              identityAttribute          = new HashSet<String>();
  static final Set<String>              organizationAttribute      = new HashSet<String>();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    preventRevoke.add("ALL USERS");

    preventUsrRecon.add("XELSYSADM");
    preventUsrRecon.add("OIMINTERNAL");
    preventUsrRecon.add("XELOPERATOR");
    preventUsrRecon.add("WEBLOGIC");

    roleAttribute.add(RoleManagerConstants.ROLE_NAME);
    roleAttribute.add(RoleManagerConstants.ROLE_DISPLAY_NAME);

    scopeAttribute.add(OrganizationManagerConstants.AttributeName.ORG_NAME.getId());

    identityAttribute.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
    identityAttribute.add(UserManagerConstants.AttributeName.DISPLAYNAME.getId());

    organizationAttribute.add(OrganizationManagerConstants.AttributeName.ID_FIELD.getId());
    organizationAttribute.add(OrganizationManagerConstants.AttributeName.ORG_NAME.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ManagedServer</code> which is associated with the
   ** specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link IdentityServer}.
   ** @param  resource           the {@link IdentityServerResource} IT
   **                            Resource definition where this connector is
   **                            associated with.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public ManagedServer(final AbstractMetadataTask task, final IdentityServerResource resource)
    throws TaskException {

    // ensure inheritance
    super(task, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ManagementServer</code> which is associated with the
   ** specified task.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link IdentityServer}.
   ** @param  resource           the {@link IdentityServerResource} IT
   **                            Resource definition where this connector is
   **                            associated with.
   ** @param  feature            the Lookup Definition providing the target
   **                            system specific features like objectClasses,
   **                            attribute id's etc.
   **
   ** @throws TaskException      if the server type specified by
   **                            {@link IdentityServerResource}
   **                            <code>resource</code> isn't supported.
   */
  public ManagedServer(final Loggable loggable, final IdentityServerResource resource, final IdentityServerFeature feature)
    throws TaskException {

    // ensure inheritance
    super(loggable, resource, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementPrefixRequired
  /**
   ** Returns the <code>true</code> if the entitlements has to be prefixed with
   ** the internal system identifier and the name of the
   ** <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerConstant#ENTITLEMENT_PREFIX_REQUIRED}.
   **
   ** @return                    <code>true</code> the entitlements has to be
   **                            prefixed with the internal system identifier
   **                            and the name of the <code>IT Resource</code>;
   **                            otherwise <code>false</code>.
   */
  public final boolean entitlementPrefixRequired() {
    return this.feature.entitlementPrefixRequired();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** with this task that afterwards can be passed to establish a connection to
   ** the target system.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Managed Server.
   */
  protected Properties environment() {
    return environment(serviceURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of this task that
   ** afterwards can be passed to establish a connection to the target system.
   **
   ** @param  providerURL        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **
   ** @return                    the context this connector use to communicate
   **                            with the RMI server.
   */
  protected Properties environment(final String providerURL) {
    Properties environment = new Properties();
    // Set up environment for creating initial context
    environment.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory());
    environment.put(Context.PROVIDER_URL,            providerURL);
    environment.put(Context.SECURITY_PRINCIPAL,      this.serverPrincipalName());
    environment.put(Context.SECURITY_CREDENTIALS,    this.serverPrincipalPassword());
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Constructs an service URL to bind to.
   ** <p>
   ** At first it checks if the context URL is set. If so it will return it as
   ** it is.
   **
   ** @return                    the service to bind to.
   */
  protected String serviceURL() {
    // Create a URL string from the parts describe by protocol, host and port
    return String.format("%s://%s:%d/%s", this.secureSocket() ? IdentityServerConstant.PROTOCOL_WEBLOGIC_SECURE : IdentityServerConstant.PROTOCOL_WEBLOGIC_DEFAULT, this.serverName(), this.serverPort(), this.serverContext());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupScope
  /**
   ** Lookup the specified organization scope by its name and returns the
   ** internal system identifier of this organization.
   **
   ** @param  organizationName   the name of an organization to lookup,
   **
   ** @return                    the internal system identifier of the
   **                            organization.
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public Organization lookupScope(final String organizationName)
    throws IdentityServerException {

    final String method = "lookupScope";
    trace(method, SystemMessage.METHOD_ENTRY);

    Organization identity = null;
    // check if entitlement prefix has to be removed from the organization
    final String name = this.feature.entitlementPrefixRequired() ? unescapePrefix(organizationName) : organizationName;
    try {
      identity = service(OrganizationManager.class).getDetails(name, scopeAttribute, true);
    }
    // if the logged-in user does not have the required authorization.
    catch (AccessDeniedException e) {
      final String[] arguments = { organizationName, "OrganizationManager.getDetails"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    // if operation fails.
    catch (OrganizationManagerException e) {
      throw new IdentityServerException(IdentityServerError.ORGANIZATION_NOT_EXISTS, organizationName);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupScopeName
  /**
   ** Lookup the specified organization scope by its name and returns the
   ** internal system identifier of this organization.
   **
   ** @param  organizationKey    the Key of an organization to lookup,
   **
   ** @return                    the internal system identifier of the
   **                            organization.
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public Organization lookupScopeName(final String organizationKey)
    throws IdentityServerException {

    final String method = "lookupScopeName";
    trace(method, SystemMessage.METHOD_ENTRY);

    Organization identity = null;
    try {
      identity = service(OrganizationManager.class).getDetails(organizationKey,organizationAttribute, false);
    }
    // if the logged-in user does not have the required authorization.
    catch (AccessDeniedException e) {
      final String[] arguments = { organizationKey, "OrganizationManager.getDetails"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    // if operation fails.
    catch (OrganizationManagerException e) {
      throw new IdentityServerException(IdentityServerError.ORGANIZATION_NOT_EXISTS, organizationKey);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOrganizations
  /**
   ** Returns a list of all non-default Organization
   **
   ** @return                 the List {@link Organization} or
   **                         <code>null</code> if there no organization
   **
   ** @throws IdentityServerException if the operation fails.
   */
  private List<Organization> getOrganizations()
    throws IdentityServerException {

    final String method = "getOrganizations";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<Organization> listOrganization = null;

    final SearchCriteria      searchCriteria1    = new SearchCriteria(
       new SearchCriteria(OrganizationManagerConstants.AttributeName.ORG_STATUS.getId(), "ACTIVE", SearchCriteria.Operator.EQUAL)
     , new SearchCriteria(OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), "Xellerate Users", SearchCriteria.Operator.NOT_EQUAL)
     , SearchCriteria.Operator.AND
    );
    final SearchCriteria      searchCriteriaFinal    = new SearchCriteria(
       searchCriteria1
     , new SearchCriteria(OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), "Requests", SearchCriteria.Operator.NOT_EQUAL)
     , SearchCriteria.Operator.AND
    );

    try {
      listOrganization = (service(OrganizationManager.class)).search(searchCriteriaFinal, organizationAttribute, null);
    }
    catch (AccessDeniedException e) {
      final String[] arguments = { "OrganizationManager.search"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    catch (OrganizationManagerException e) {
      throw new IdentityServerException(IdentityServerError.ORGANIZATION_NOT_EXISTS);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return listOrganization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRole
  /**
   ** Lookup the specified role by its name and returns the internal system
   ** identifier of this role.
   **
   ** @param  roleName           the name of a role to lookup,
   **
   ** @return                    the internal system identifier of the role.
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public Role lookupRole(final String roleName)
    throws IdentityServerException {

    final String method = "lookupRole";
    trace(method, SystemMessage.METHOD_ENTRY);

    Role identity = null;
    // check if entitlement prefix has to be removed from the role name
    final String name = this.feature.entitlementPrefixRequired() ? unescapePrefix(roleName) : roleName;
    try {
      identity = service(RoleManager.class).getDetails(RoleManagerConstants.ROLE_NAME, name, roleAttribute);
    }
    catch (AccessDeniedException e) {
      final String[] arguments = { name, "RoleManager.getDetails"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    catch (NoSuchRoleException e) {
      throw new IdentityServerException(IdentityServerError.ROLE_NOT_EXISTS, name);
    }
    catch (RoleLookupException e) {
      throw new IdentityServerException(IdentityServerError.ROLE_AMBIGUOUS, name);
    }
    catch (SearchKeyNotUniqueException e) {
      throw new IdentityServerException(IdentityServerError.ROLE_AMBIGUOUS, name);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookuptUserMembership
  /**
   ** Lookup the specified role by its name and returns the internal system
   ** identifier of this role.
   **
   ** @param  userkey         the user key of a user to lookup.
   **
   ** @return                 the List {@link Role} or
   **                         <code>null</code> if there no role
   **
   ** @throws IdentityServerException if the operation fails.
   */
  private List<Role> lookuptUserMembership(final String userkey)
    throws IdentityServerException {

    final String method = "lookuptUserMembership";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<Role> listRole = null;
    try {
      final String       category  = lookuptRoleCategoryKey(oimAdminRoleCategory).get(0).getEntityId();
      final Set<String>  attribute = new HashSet<String>();
      attribute.add(RoleManagerConstants.ROLE_NAME);

      final SearchCriteria      SearchFilter    = new SearchCriteria(
        new SearchCriteria(RoleManagerConstants.ROLE_CATEGORY_KEY, category, SearchCriteria.Operator.EQUAL)
      , new SearchCriteria(RoleManagerConstants.ROLE_NAME, "ALL USERS", SearchCriteria.Operator.NOT_EQUAL)
      , SearchCriteria.Operator.AND
      );
      //JPB Changed
      listRole = service(RoleManager.class).getUserMemberships(userkey,SearchFilter,attribute,null,true);

    } catch (UserMembershipException e) {
      throw new IdentityServerException(IdentityServerError.PERMISSION_NOT_EXISTS, e);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return listRole;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountSystemMember
  /**
   ** Validates if the specified {@link User} <code>identity</code> is member of
   ** the {@link Role}.
   ** <p>
   ** The validation ask for role that is granted directly to the identity.
   **
   ** @param  identity           the {@link User} the membership has to be
   **                            validate for the role specified by the name
   **                            <code>permission</code>.
   ** @param  role               the {@link Role} to validate.
   **
   ** @return                    <code>true</code> if {@link User}
   **                            <code>identity</code> is member if the
   **                            {@link AdminRole} <code>permission</code> in
   **                            the specified {@link Organization}
   **                            <code>scope</code>; otherwise
   **                            <code>false</code>.
   **
   ** @throws IdentityServerException if the operation fails
   */
  public boolean accountSystemMember(final User identity, final Role role)
    throws IdentityServerException {

    final String method = "accountSystemMember";
    trace(method, SystemMessage.METHOD_ENTRY);
    boolean exists = false;
    final RoleManager           service    = service(RoleManager.class);
    // Check only the direct membership
    try {
      final List<Role> collection = service.getUserMemberships(identity.getEntityId(),false);

      for (Role grant : collection) {
        if (grant.getEntityId().equals(role.getEntityId())) {
          exists = true;
          break;
        }
      }
      } catch (UserMembershipException e) {
        throw new IdentityServerException(IdentityServerError.PERMISSION_NOT_EXISTS, e);
      }
      catch (Exception e) {
        throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
      }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return exists;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSystemRoles
  /**
  ** Returns a list of all system roles.
  **
  ** @return                 the List {@link Role} or
  **                         <code>null</code> if there no Role
  **
  ** @throws IdentityServerException if the operation fails.
  */
  private List<Role>  getSystemRoles()
    throws IdentityServerException {

    final String method = "getSystemRoles";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<Role> listRole = null;

    final Set<String>  attribute  = new HashSet<String>();
    attribute.add(RoleManagerConstants.ROLE_KEY);
    attribute.add(RoleManagerConstants.ROLE_NAME);
    final SearchCriteria      SearchFilter    = new SearchCriteria(
      new SearchCriteria(RoleManagerConstants.ROLE_CATEGORY_NAME, oimAdminRoleCategory, SearchCriteria.Operator.EQUAL)
    //, onlySystemRoleFilterSearchCriteria()
    , new SearchCriteria(RoleManagerConstants.ROLE_NAME, "ALL USERS", SearchCriteria.Operator.NOT_EQUAL)
    , SearchCriteria.Operator.AND
    );

    try {
      listRole = (service(RoleManager.class)).search(SearchFilter, attribute, null);
    }
    catch (AccessDeniedException e) {
      final String[] arguments = { "RoleManager.search"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    catch (RoleSearchException e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return listRole;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupUserMembership
  /**
   ** Returns a list of user's system role memberships based on role key..
   **
   ** @param  roleKey         the id of the role .
   **
   ** @return                 the {@link User} or <code>null</code> if there
   **                         no user
   **
   ** @throws IdentityServerException if the operation fails.
   */
  private List<User> lookupUserMembership(final String roleKey)
    throws IdentityServerException {

    final String method = "lookupUserMembership";
    trace(method, SystemMessage.METHOD_ENTRY);
    List<User> listUsr = null;

    try {
      if (StringUtility.isEmpty(roleKey)){
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "roleKey"));
      }
      else {
        listUsr = (service(RoleManager.class)).getRoleMembers(roleKey,true);
      }
    }
    catch (AccessDeniedException e) {
      final String[] arguments = { roleKey, "RoleManager.getRoleMembers"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    } catch (RoleMemberException  e)
    {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return listUsr;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupPermission
  /**
   ** Lookup the specified admin role by its name and returns the entire entity.
   **
   ** @param  permission         the name of a admin role to lookup.
   **
   ** @return                    the admin role or <code>null</code> if its not
   **                            mapped in the system.
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public AdminRole lookupPermission(final String permission)
    throws IdentityServerException {

    final String method = "lookupPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    AdminRole subject;
    try{
      // check if entitlement prefix has to be removed from the organization
      final String           name    = this.feature.entitlementPrefixRequired() ? unescapePrefix(permission) : permission;
      final AdminRoleService service = service(AdminRoleService.class);
      subject = service.getAdminRole(name);
      if (subject == null)
        throw new IdentityServerException(IdentityServerError.PERMISSION_NOT_EXISTS, permission);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return subject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPermissionList
  /**
   ** Retrieves all admin role an account belongs to.
   **
   ** @param  identity           the {@link User} representing an
   **                            <code>Account</code> the membership has to be
   **                            retrieved for.
   **
   ** @return                    the collection of admin roles belonging to
   **                            <code>identity</code>.
   **
   ** @throws IdentityServerException if the operation fails
   */
  public List<AdminRoleMembership> accountPermissionList(final User identity)
    throws IdentityServerException {

    final String method = "accountPermissionList";
    trace(method, SystemMessage.METHOD_ENTRY);

    final AdminRoleService service = service(AdminRoleService.class);

    List<AdminRoleMembership> collection = null;
    try {
      // we have to ask for permission without any delegation in hierarchy to
      // collect only those permissions that are directly granted to the
      // identity
      collection = service.listUsersMembership(identity.getEntityId(), null, null, false, null);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return collection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUsersWithSystemPermission
  /**
   ** Return an UserKey Set of users that have Admin Role in OIM
   **
   ** @return                 the {@link Set} UserKey or
   **                         <code>null</code> if there no user
   **
   ** @throws TaskException   if the operation fails.
   */
  private final Set getUsersWithSystemPermission()
  throws IdentityServerException
  {
    final String method = "listUsersWithSystemRole";
    trace(method, SystemMessage.METHOD_ENTRY);

    Set sysRoleUsr  = new HashSet();
    try {
      List<Role> systemRoles = getSystemRoles();

      for (Role sysRole : systemRoles) {
        List<User> usrSystemRoles =lookupUserMembership(sysRole.getEntityId());
        for (User roleMshp : usrSystemRoles) {
        sysRoleUsr.add(roleMshp.getLogin());
        }
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    //Remove provilegdes users (XELSYSADM,XELOPERATOR,WEBLOGIC,OIMINTERNAL)
    sysRoleUsr.removeAll(preventUsrRecon);
    return sysRoleUsr;
  }

  public final Set usersWithSystemPermission()
    throws IdentityServerException{
    return getUsersWithSystemPermission();
  }

  public Set getPrivilegeUseLogin(){
    return preventUsrRecon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPermissionMember
  /**
   ** Validates if the specified {@link User} <code>identity</code> is member of
   ** the {@link AdminRole} <code>permission</code> in the specified
   ** {@link Organization} <code>scope</code>.
   ** <p>
   ** The validation ask for permission without any delegation in hierarchy to
   ** check only those permissions that are granted directly to the identity.
   **
   ** @param  identity           the {@link User} the membership has to be
   **                            validate for the role specified by the name
   **                            <code>permission</code>.
   ** @param  permission         the {@link AdminRole} to validate.
   ** @param  scope              the organizational scope of the role to
   **                            validate.
   **
   ** @return                    <code>true</code> if {@link User}
   **                            <code>identity</code> is member if the
   **                            {@link AdminRole} <code>permission</code> in
   **                            the specified {@link Organization}
   **                            <code>scope</code>; otherwise
   **                            <code>false</code>.
   **
   ** @throws IdentityServerException if the operation fails
   */
  public boolean accountPermissionMember(final User identity, final AdminRole permission, final Organization scope)
    throws IdentityServerException {

    final String method = "accountPermissionMember";
    trace(method, SystemMessage.METHOD_ENTRY);

    // we have to ask for permission without any delegation in hierarchy to
    // check only those permissions that are granted directly to the identity
    boolean exists = false;
    try{
      final AdminRoleService          service    = service(AdminRoleService.class);

      final List<AdminRoleMembership> collection = service.listUsersMembership(identity.getEntityId(), permission.getRoleName(), scope.getEntityId(), false, null);
      // check if the membership relation is contained in the retrieved
      // collection
      for (AdminRoleMembership grant : collection) {
        final AdminRole grantedRole     = grant.getAdminRole();
        final String    grantedScope    = grant.getScopeId();
        //final Boolean   grantedHierarchy = grant.isHierarchicalScope();
        if (permission.getRoleId().equals(grantedRole.getRoleId())
            && scope.getEntityId().equals(grantedScope)) {
          exists = true;
          break;
        }
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return exists;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listAdminRole
  /**
   ** Returns list of admin roles available in an installation.
   **
   ** @return                    the {@link AdminRole} or <code>null</code> if there
   **                            no role
   */
  //
  public List<AdminRole> getAdminRoles()
  {
    return (service(AdminRoleService.class)).getAdminRoles();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listMembershipsInScope
  /**
   ** Returns a list of admin role membership in the given scope.
   **
   ** @param  orgID         the id of the scoped organization.
   **
   ** @return               the {@link AdminRoleMembership} or <code>null</code> if there
   **                       no role
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public List<AdminRoleMembership> listMembershipsInScope(String orgID)
  throws IdentityServerException {

    final String method = "listMembershipsInScope";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<AdminRoleMembership> listAdminRoleMshp = null;

    try {
      if (StringUtility.isEmpty(orgID)) {
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "organization ID"));
        return listAdminRoleMshp;
      }
      else {
          listAdminRoleMshp =  (service(AdminRoleService.class)).listMembershipsInScope(orgID, null,  false, new HashMap<String, Object>());
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return listAdminRoleMshp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listMembershipsForUserByRoleName
  /**
   ** Returns a list of user's admin role memberships based on roles name.
   **
   ** @param  usrID         the name of a admin role to lookup.
   **
   ** @param  roleName      List of admin roles name
   **
   ** @return               the {@link AdminRoleMembership} or <code>null</code> if there
   **                       no role
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public List<AdminRoleMembership> listMembershipsForUserByRoleName(String usrID, List<String> roleName)
  throws IdentityServerException {

    final String method = "listMembershipsForUserByRoleName";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<AdminRoleMembership> listAdminRoleMshp = null;
    try {
      if (StringUtility.isEmpty(usrID)){
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "user ID"));
        return listAdminRoleMshp;
      }
      else if (roleName==null) {
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "roleName"));
        return listAdminRoleMshp;
      }
      else if (roleName.isEmpty()) {
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "roleName"));
        return listAdminRoleMshp;
      }
      else {
        listAdminRoleMshp = (service(AdminRoleService.class)).listMembershipsForUserByRoleName(usrID, roleName);
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return listAdminRoleMshp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookuptRoleCategoryKey
  /**
   ** Revokes an admin role from an account.
   **
   ** @param  roleCategoryName   The name of the role category to llokup
   **
   ** @return               List of {@link RoleCategory} or <code>null</code> if there
   **                       no rolecategory
   **
   ** @throws IdentityServerException if the operation fails
   */
  private List<RoleCategory> lookuptRoleCategoryKey (final String roleCategoryName)
  throws IdentityServerException {

    final String method = "lookuptRoleCategoryKey";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<RoleCategory> listRoleCategory = null;
    try {
      final Set<String>  attribute  = new HashSet<String>();
      attribute.add(RoleManagerConstants.ROLE_CATEGORY_KEY);
      final SearchCriteria      SearchFilter    = new SearchCriteria(RoleManagerConstants.ROLE_CATEGORY_NAME, roleCategoryName, SearchCriteria.Operator.EQUAL);
      listRoleCategory = service(RoleCategoryManager.class).search(SearchFilter,attribute,null);
    } catch (RoleCategorySearchException  e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    } catch (AccessDeniedException  e) {
      final String[] arguments = { roleCategoryName, "RoleCategoryManager.search"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return listRoleCategory;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupIdentity
  /**
   ** Lookup the specified identity by its login name and returns the internal
   ** system identifier of this identity.
   **
   ** @param  userLogin          the login name of an identity to lookup,
   **
   ** @return                    the internal system identifier of the identity.
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public User lookupIdentity(String userLogin)
    throws IdentityServerException {

    final String method = "lookupIdentity";
    trace(method, SystemMessage.METHOD_ENTRY);

    //OIMClient connect()

    User identity = null;
    try {
      identity = service(UserManager.class).getDetails(UserManagerConstants.AttributeName.USER_LOGIN.getId(), userLogin, identityAttribute);
    }
    catch (AccessDeniedException e) {
      e.printStackTrace();
      //final String[] arguments = { userLogin, "UserManager.getDetails"};
      //throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    catch (NoSuchUserException e) {
      e.printStackTrace();
      //throw new IdentityServerException(IdentityServerError.IDENTITY_NOT_EXISTS, userLogin);
    }
    catch (UserLookupException e) {
      e.printStackTrace();
      //throw new IdentityServerException(IdentityServerError.IDENTITY_AMBIGUOUS, userLogin);
    }
    catch (Exception e) {
      e.printStackTrace();
  //    throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupIdentityLogin
  /**
   ** Lookup the specified identity by its internal system identifier
   ** and returns the login name of this identity.
   **
   ** @param  usrKey             internal identity system identifier to lookup.
   **
   ** @return                    login name of the identity.
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public User lookupIdentityLogin(final String usrKey)
      throws IdentityServerException {

      User identity = null;
      final Set<String>  retAttrs  = new HashSet<String>();
      retAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
      try {
        identity = service(UserManager.class).getDetails(usrKey,retAttrs,false);
      }
      catch (AccessDeniedException e) {
        final String[] arguments = { usrKey, "UserManager.getDetails"};
        throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
      }
      catch (NoSuchUserException e) {
        throw new IdentityServerException(IdentityServerError.IDENTITY_NOT_EXISTS, usrKey);
      }
      catch (UserLookupException e) {
        throw new IdentityServerException(IdentityServerError.IDENTITY_AMBIGUOUS, usrKey);
      }
      catch (Exception e) {
        throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
      }
      return identity;
    }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUsersWithAdminPermission
  /**
   ** Return an UserKey Set of "User Login" that have Admin Role in OIM
   **
   ** @return                 the {@link Set} UserKey or
   **                         <code>null</code> if there no user
   **
   ** @throws TaskException   if the operation fails.
   */
  private final Set getUsersWithAdminPermission()
  throws IdentityServerException
  {

    final String method = "getUsersWithAdminPermission";
    trace(method, SystemMessage.METHOD_ENTRY);

    Set admRoleUsr  = new HashSet();

    try{

      List<Organization> organizationList = getOrganizations();

      for(Organization organization:organizationList){
        // Gather users that have AdminRoles assigned
        List<AdminRoleMembership> usrAdminRoles = listMembershipsInScope(String.valueOf(organization.getAttribute(OrganizationManagerConstants.AttributeName.ID_FIELD.getId())));
        for (AdminRoleMembership roleMshp : usrAdminRoles) {
          admRoleUsr.add(lookupIdentityLogin(roleMshp.getUserId()).getLogin());
        }
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    //Remove provilegdes users (XELSYSADM,XELOPERATOR,WEBLOGIC,OIMINTERNAL)
    admRoleUsr.removeAll(preventUsrRecon);
    return admRoleUsr;
  }

  public Set usersWithAdminPermission()
  throws IdentityServerException{
    return getUsersWithAdminPermission();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listUsersWithAccount
  /**
   ** Return an UserKey Set with the users that have an account for the
   ** specifies application instance.
   **
   ** @param  appInstance     the application instance to join with.
   **
   ** @return                 the {@link Set} UserKey or
   **                         <code>null</code> if there no user
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public final Set listUsersWithAccount(String appInstance)
  throws IdentityServerException
  {

    final String method = "listUsersWithAccount";
    trace(method, SystemMessage.METHOD_ENTRY);

    Set accountUsr  = new HashSet();

    try{
      List<Account> accounts = getProvisionedAccountsForAppInstance(appInstance);
      for (Account account : accounts) {
          accountUsr.add(account.getUserKey());
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    return accountUsr;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountDelete
  /**
   ** Drops an identity account.
   **
   ** @param  identity           the name of the account to delete.
   **
   ** @throws IdentityServerException if the operation fails
   */
  public void accountDelete(final User identity)
    throws IdentityServerException {

    final String method = "accountDelete";
    trace(method, SystemMessage.METHOD_ENTRY);

    final RoleManager      roleService  = service(RoleManager.class);
    final AdminRoleService adminRoleservice = service(AdminRoleService.class);
    try {
      // retrieve all system roles which are granted directly to the specified
      // user name
      // System Roles
      final List<Role>  granted = roleService.getUserMemberships(identity.getEntityId(), false);

      // revoke all directly granted system roles if there are some
      if (granted.size() > 0) {
        final Set<String> revoke  = new HashSet<String>(granted.size() - preventRevoke.size());
        for (Role role : granted) {
          // add only roles to the collection of revocations which can manually
          // assigned
          if (!preventRevoke.contains(role.getName()))
            revoke.add(role.getEntityId());
        }
        roleService.revokeRoleGrants(identity.getEntityId(), revoke);
      }
      //AdminRoles
      final List<AdminRoleMembership> collection = adminRoleservice.listUsersMembership(identity.getEntityId(), null, null, false, null);
      // revoke all directly granted system roles if there are some
      if (collection.size() > 0) {
        //final Set<String> revoke  = new HashSet<String>(granted.size() - preventRevoke.size());
        for (AdminRoleMembership adminRole : collection) {
          // add only roles to the collection of revocations which can manually
          // assigned
          adminRoleservice.removeAdminRoleMembership(adminRole);
        }
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }

    final AdminRoleService adminService = service(AdminRoleService.class);
    try {
      // retrieve all admin roles that belongs to the specified user name
      // without any additional query parameters like SORTEDBY or STARTROW and
      // ENDROW
      final List<AdminRole> granted = adminService.getAdminRolesForUser(identity.getEntityId(), null);
      for (AdminRole permission : granted) {
        final AdminRoleMembership member = new AdminRoleMembership();
        member.setAdminRole(permission);
        member.setUserId(identity.getLogin());
        adminService.removeAdminRoleMembership(member);
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPermissionAssign
  /**
   ** Grants an system permission to an account.
   **
   ** @param  identity           the identity <code>Account</code> the
   **                            membership has to be assigned to the permission
   **                            specified by {@link Role}
   **                            <code>permission</code>.
   ** @param  permission         the {@link Role} permission to assign.
   **
   ** @throws IdentityServerException if the operation fails
   */
  public void accountPermissionAssign(final User identity, final Role permission)
    throws IdentityServerException {

    final String method = "accountPermissionAssign";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Set<String> grantee = new HashSet<String>(1);
    grantee.add(identity.getEntityId());
    try {
      final RoleManager service = service(RoleManager.class);
      service.grantRole(permission.getEntityId(), grantee);
    }
    catch (AccessDeniedException e) {
      throw new IdentityServerException(IdentityServerError.PERMISSION_ACCESS_DENEID, method);
    }
    catch (ValidationFailedException e) {
      throw new IdentityServerException(IdentityServerError.PERMISSION_NOT_EXISTS, permission.getDisplayName());
    }
    catch (RoleGrantException e) {
      final String[] arguments = {permission.getDisplayName(), identity.getLogin()};
      throw new IdentityServerException(IdentityServerError.PERMISSION_NOT_GRANTED, arguments, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPermissionRevoke
  /**
   ** Revokes an system permission from an account.
   **
   ** @param  identity           the identity <code>Account</code> the
   **                            membership has to be revoked from the
   **                            permission specified by {@link Role}
   **                            <code>permission</code>.
   ** @param  permission         the {@link Role} permission to revoke.
   **
   ** @throws IdentityServerException if the operation fails
   */
  public void accountPermissionRevoke(final User identity, final Role permission)
    throws IdentityServerException {

    final String method = "accountPermissionRevoke";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Set<String> grantee = new HashSet<String>(1);
    grantee.add(identity.getEntityId());

    trace(method, "User:"  + identity.getEntityId());
    trace(method, "Role: " + permission.getEntityId());

    try {
      final RoleManager service = service(RoleManager.class);
      service.revokeRoleGrant(permission.getEntityId(), grantee);
    }
    catch (AccessDeniedException e) {
      throw new IdentityServerException(IdentityServerError.PERMISSION_ACCESS_DENEID, method);
    }
    catch (ValidationFailedException e) {
      throw new IdentityServerException(IdentityServerError.PERMISSION_NOT_EXISTS, permission.getDisplayName());
    }
    catch (RoleGrantRevokeException e) {
      final String[] arguments = {permission.getDisplayName(), identity.getLogin()};
      throw new IdentityServerException(IdentityServerError.PERMISSION_NOT_REVOKED, arguments, e);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPermissionAssign
  /**
   ** Grants an admin role to an account.
   **
   ** @param  identity           the identity <code>Account</code> the
   **                            membership has to be assigned to the role
   **                            specified by {@link AdminRole}
   **                            <code>permission</code>.
   ** @param  permission         the {@link AdminRole} to assign.
   ** @param  scope              the organizational scope of the role to assign.
   ** @param  delegated          <code>true</code> if the permission should be
   **                            assigned delegated within in hierarchy;
   **                            otherwise <code>false</code>.
   **
   ** @throws IdentityServerException if the operation fails
   */
  public void accountPermissionAssign(final User identity, final AdminRole permission, final Organization scope, final boolean delegated)
    throws IdentityServerException {

    final String method = "accountPermissionAssign";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final Long                organization = new Long(Long.parseLong(scope.getEntityId()));
      final AdminRoleMembership membership   = new AdminRoleMembership(identity.getEntityId(), permission, organization, delegated);
      final AdminRoleService    service      = service(AdminRoleService.class);
      service.addAdminRoleMembership(membership);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPermissionRevoke
  /**
   ** Revokes an admin role from an account.
   **
   ** @param  identity           the primary identifier of an
   **                            <code>Account</code> the membership has to be
   **                            revoked from the role specified by the name
   **                            <code>permission</code>.
   ** @param  permission         the {@link AdminRole} to revoke.
   ** @param  scope              the organizational scope of the role to revoke.
   **
   ** @throws IdentityServerException if the operation fails
   */
  public void accountPermissionRevoke(final User identity, final AdminRole permission, final Organization scope)
    throws IdentityServerException {

    final String method = "accountPermissionRevoke";
    trace(method, SystemMessage.METHOD_ENTRY);

    final AdminRoleService service = service(AdminRoleService.class);
    try {
      // we have to ask for permission without any delegation in hierarchy to
      // remove only those permissions that are directly granted to the identity
      final List<AdminRoleMembership> collection = service.listUsersMembership(identity.getEntityId(), permission.getRoleName(), scope.getEntityId(), false, null);
      // now we are able to remove all memberships because the role membership
      // provides the data for the delegation in hierarchy
      for (AdminRoleMembership membership : collection)
        service.removeAdminRoleMembership(membership);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPermissionUpdate
  /**
   ** Updates an admin role from an account.
   **
   ** @param  identity           the primary identifier of an
   **                            <code>Account</code> the membership has to be
   **                            updated for the role specified by the name
   **                            <code>permission</code>.
   ** @param  permission         the name of the role to update.
   ** @param  origin             the old organizational scope of the role to
   **                            revoke.
   ** @param  scope              the new organizational scope of the role to
   **                            assign.
   ** @param  delegated          <code>true</code> if the permission should be
   **                            updated delegated within in hierarchy;
   **                            otherwise <code>false</code>.
   **
   ** @throws IdentityServerException if the operation fails
   */
  public void accountPermissionUpdate(final User identity, final AdminRole permission, final Organization origin, final Organization scope, final boolean delegated)
    throws IdentityServerException {

    final String method = "accountPermissionUpdate";
    trace(method, SystemMessage.METHOD_ENTRY);

    final AdminRoleService service = service(AdminRoleService.class);
    try {
      // we have to ask for permission without any delegation in hierarchy to
      // remove only those permissions that are directly granted to the identity
      final List<AdminRoleMembership> collection = service.listUsersMembership(identity.getEntityId(), permission.getRoleName(), origin.getEntityId(), false, null);
      for (AdminRoleMembership member : collection) {
        member.setScopeId(scope.getEntityId());
        member.setRootScopeId(scope.getEntityId());
        member.setHierarchicalScope(delegated);
        service.updateRoleMemberships(member);
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProvisionedAccountsForAppInstance
  /**
   ** Return a List of accounts that have the specifies application instance
   **
   ** @param  appInstance     the Application INstance Name name to lookup
   **f
   ** @return                 the {@link List} Account or
   **                         <code>null</code> if there no user
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public final List<Account> getProvisionedAccountsForAppInstance(final String appInstance)
    throws IdentityServerException {

    final String method = "getProvisionedAccountsForAppInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<Account> listAccount = null;

    final SearchCriteria scFilter =  new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), "Provisioned", SearchCriteria.Operator.EQUAL);
    try {
      if (StringUtility.isEmpty(appInstance)){
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "appInstance"));
      }
      else {
        listAccount = ( (service(ProvisioningService.class)).getProvisionedAccountsForAppInstance(appInstance, scFilter, null));
      }
    }
      // if operation fails.
      catch (AccessDeniedException e) {
        final String[] arguments = {"ProvisioningService.getProvisionedAccountsForAppInstance"};
        throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
      }
      catch (GenericProvisioningException e) {
        throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
      }
      catch (Exception e) {
        throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
      }
      finally {
        trace(method, SystemMessage.METHOD_EXIT);
      }

    return listAccount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findApplicationInstance
  /**
   ** Return a List of ApplicationInstance that match the specified application instance name
   **
   ** @param  appInstance     the Application INstance Name name to lookup
   **
   ** @return                 the {@link List} ApplicationInstance or
   **                         <code>null</code> if there no user
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public final List<ApplicationInstance> findApplicationInstance(final String appInstance)
    throws IdentityServerException {

    final String method = "findApplicationInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<ApplicationInstance> listAppInstance = null;

    final SearchCriteria scFilter =  new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.APPINST_NAME.getId(), appInstance, SearchCriteria.Operator.EQUAL);
    HashMap<String, Object> configParams = new HashMap<String, Object>();
    configParams.put("FORMDETAILS", "EXCLUDE");

    try {
      if (StringUtility.isEmpty(appInstance)){
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "appInstance"));
      }
      else {
       listAppInstance = ((service(ApplicationInstanceService.class)).findApplicationInstance(scFilter, configParams));
      }
    }
    // if operation fails.
    catch (AccessDeniedException e) {
      final String[] arguments = { "", "ApplicationInstanceService.findApplicationInstance"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    catch (GenericAppInstanceServiceException e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return listAppInstance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountDetails4Reconciliation
  /**
   ** Return a the user details of the specified user Login
   **
   ** @param  userLogin       the login name of an identity to lookup,
   **
   ** @return                 the {@link Map} with Reconciliation Attributes
   **                         name -&gt; vale or <code>null</code> if there no
   **                         user.
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public Map<String, Object> getAccountDetails4Reconciliation(final String userLogin)
    throws IdentityServerException {

    final String method = "getAccountDetails4Reconciliation";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Map<String, Object> returnData = new HashMap<String, Object>();

    try {
      if (StringUtility.isEmpty(userLogin)) {
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "usrLogin"));
        return returnData;
      }
      else {
        Set<String> retAttrs = new HashSet<String>();
        retAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
        retAttrs.add(UserManagerConstants.AttributeName.FIRSTNAME.getId());
        retAttrs.add(UserManagerConstants.AttributeName.LASTNAME.getId());
        User returnUser = null;
        returnUser = service(UserManager.class).getDetails(userLogin, retAttrs, true);
        if (!preventUsrRecon.contains(returnUser.getLogin())) {
          returnData.put(RECON_MAP_ACCOUNT_USERLOGIN, returnUser.getLogin());
          returnData.put(RECON_MAP_ACCOUNT_FNAME, returnUser.getFirstName());
          returnData.put(RECON_MAP_ACCOUNT_LNAME, returnUser.getLastName());
        }
      }
    }
    catch (AccessDeniedException e) {
      final String[] arguments = { userLogin, "UserManager.getDetails" };
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    catch (NoSuchUserException e) {
      throw new IdentityServerException(IdentityServerError.IDENTITY_NOT_EXISTS, userLogin);
    }
    catch (UserLookupException e) {
      throw new IdentityServerException(IdentityServerError.IDENTITY_AMBIGUOUS, userLogin);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return returnData;
  }
  // Method:   getOrganizationName
  /**
   ** Return the name of the organization for the specified organization key
   **
   ** @param  organizationKey internal identity system identifier to lookup.
   **
   ** @return                 the {@link String} with organization name
   **                         <code>null</code> if there no organization
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public final String getOrganizationName(final String organizationKey)
    throws IdentityServerException {

    return String.valueOf(lookupScopeName(organizationKey).getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPermission4Reconciliation
  /**
   ** Return an List with the Permission data for the specified user Key and Permission Type
   **
   ** @param  usrKey             internal identity system identifier to lookup.
   ** @param  permissionType     type of permission.
   ** @param  encodedPrefix      IT resource prefix for reconciliation
   ** @return                    {@link List} with permission data for reconciliation
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public List<Map<String, Object>> accountPermission4Reconciliation(final String usrKey, final String permissionType, final String encodedPrefix)
    throws IdentityServerException {

    final String method = "accountPermission4Reconciliation";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<Map<String, Object>> returnData  =  new ArrayList<Map<String, Object>>();
    final AdminRoleService    service     = service(AdminRoleService.class);
    String entitlemetPrefix = "";

    if (entitlementPrefixRequired())
          entitlemetPrefix = String.format(encodedPrefix, this.resource.instance(), this.resource.name(),"");

    if (StringUtility.isEmpty(usrKey)){
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "usrKey"));
      return returnData;
    }
    else if (StringUtility.isEmpty(permissionType)){
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "permissionType"));
      return returnData;
    }

    try {
      //Get "Top" Organization Key
      if(!permissionType.equalsIgnoreCase("System Permission")){
        final List<AdminRoleMembership> collection;
        if (permissionType.equalsIgnoreCase("Global Permission")) // TODO get from Map
          collection = service.listUsersMembership(usrKey, null, lookupScope("Top").getEntityId(), false, null);
        else// Scoped Permission
          collection = service.listUsersMembership(usrKey, null, null, false, null);

        for (AdminRoleMembership grant : collection) {
          final Map<String, Object> permissionData  = new HashMap<String, Object>();
          final AdminRole           grantedRole     = grant.getAdminRole();

          if ( (permissionType.equals("Global Permission"))&& (!grantedRole.isScoped()) ){
            permissionData.put(RECON_MAP_GLOBAL_PERMISSION_NAME, entitlemetPrefix+grant.getAdminRoleName());
            returnData.add(permissionData);
          }
          else if ( (permissionType.equals("Scoped Permission")) && (grantedRole.isScoped()) ) {
            permissionData.put(RECON_MAP_SCOPED_PERMISSION_NAME, entitlemetPrefix+grant.getAdminRoleName());
            permissionData.put(RECON_MAP_SCOPED_PERMISSION_HIERARCHY, (grant.isHierarchicalScope() ? "1":"0"));
            permissionData.put(RECON_MAP_SCOPED_PERMISSION_SCOPE, entitlemetPrefix + getOrganizationName(grant.getScopeId()));
            returnData.add(permissionData);
          }
        }
      }
      else{ //System Permission
        final List<Role> collection = lookuptUserMembership(usrKey);
        for (Role grant : collection) {
          final Map<String, Object> permissionData  = new HashMap<String, Object>();
          permissionData.put(RECON_MAP_GLOBAL_PERMISSION_NAME, entitlemetPrefix+grant.getName());
          returnData.add(permissionData);
        }
      }
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return returnData;
  }
}