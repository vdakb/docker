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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AdminRoleHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AdminRoleHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
    0.0.0.2     2013-18-01  DSteding    Fix DE-000062
                                        Role Handler should honor by failonerror
                                        missing roles/users.
                                        Fix DE-000065
                                        Role assignment/revokation are not
                                        handling the entire membership if one of
                                        the recipients not exist.
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import oracle.iam.platform.authopss.vo.Capability;
import oracle.iam.platform.authopss.vo.AdminRoleVO;
import oracle.iam.platform.authopss.vo.EntityPublication;
import oracle.iam.platform.authopss.vo.AdminRoleRuleScope;

import oracle.iam.platform.authopss.api.PolicyConstants;

import oracle.iam.platformservice.api.AdminRoleService;
import oracle.iam.platformservice.api.EntityPublicationService;

import oracle.iam.platform.utils.SuperRuntimeException;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.exception.NoSuchUserException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureException;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.deployment.type.AdminRole;
import oracle.iam.identity.deployment.type.MemberShip;
import oracle.iam.identity.deployment.type.Publication;

////////////////////////////////////////////////////////////////////////////////
// class AdminRoleHandler
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>AdminRoleHandler</code> creates,deletes and configures a admin role in
 ** Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class AdminRoleHandler extends EntitlementHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final
  //////////////////////////////////////////////////////////////////////////////

  private static final String ROLE_ENTITY_NAME = "Admin Role";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the business logic layer to operate on admin roles */
  private AdminRoleService roleService;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AdminRoleHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public AdminRoleHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend, PolicyConstants.Resources.ADMIN_ROLES);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMemberShip
  /**
   ** Add the specified userid to be handled.
   **
   ** @param  action             the action to perform for <code>userid</code>
   ** @param  recipient          the {@link List} of recipient user profiles
   **                            in Identity Manager to become membership of the
   **                            admin role.
   */
  public void addMemberShip(final ServiceOperation action, final List<MemberShip.Recipient> recipient) {
    if (this.single == null)
      this.single = buildElement();

    // add the value pair to the parameters
    ((AdminRoleInstance)this.single).addMemberShip(action, recipient);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (overridden)
  /**
   ** Creates a new <code>Admin Role</code> in Identity Manager through the
   ** discovered {@link AdminRoleService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.roleService         = task.service(AdminRoleService.class);
    this.userService         = task.service(UserManager.class);
    this.publisherService    = task.service(EntityPublicationService.class);
    this.organizationService = task.service(OrganizationManager.class);

    if (this.single != null) {
      final AdminRoleInstance single = (AdminRoleInstance)this.single;
      create(single);
    }

    for (CatalogElement cursor : this.multiple) {
      final AdminRoleInstance role = (AdminRoleInstance)cursor;
      create(role);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (overridden)
  /**
   ** Deletes an existing admin role from Identity Manager through the
   ** discovered {@link AdminRoleService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.roleService = task.service(AdminRoleService.class);

    if (this.single != null)
      delete(this.single);

    for (CatalogElement cursor : this.multiple)
      delete((AdminRoleInstance)cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify (overridden)
  /**
   ** Mofifies an existing admin role in Identity Manager through the discovered
   ** {@link AdminRoleService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.roleService         = task.service(AdminRoleService.class);
    this.userService         = task.service(UserManager.class);
    this.publisherService    = task.service(EntityPublicationService.class);
    this.organizationService = task.service(OrganizationManager.class);

    if (this.single != null) {
      modify(this.single);
    }

    for (CatalogElement cursor : this.multiple) {
      final AdminRoleInstance role = (AdminRoleInstance)cursor;
      modify(role);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new admin role in Identity Manager through the discovered
   ** {@link AdminRoleService}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final AdminRoleInstance instance)
    throws ServiceException {

    if (exists(instance)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ROLE_ENTITY_NAME, instance.name()));
      modify(instance);
    }
    else {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, ROLE_ENTITY_NAME, instance.name()));
      assignCapability(instance);
      final Boolean scoped = instance.isScoped();
      if (scoped != null) {
        if (instance.isScoped()) {
          disableScope(instance);
          enableScope(instance);
        }
        else {
          defaultScope(instance);
        }
      }
      assignMember(instance);
      disablePublication(instance);
      enablePublication(instance);
      try {
        this.roleService.createAdminRole(instance.identity());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, ROLE_ENTITY_NAME, instance.name()));
        // fetch through the ID if the ceated role to keep further processes in
        // sync
        lookup(instance);
      }
      catch (SuperRuntimeException e) {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_CREATE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing admin role in Identity Manager through the discovered
   ** {@link AdminRoleService}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final AdminRoleInstance instance)
    throws ServiceException {

    final String[] arguments = { ROLE_ENTITY_NAME, instance.name() };
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
    if (!exists(instance)) {
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    else {
      try {
        final boolean success = this.roleService.removeAdminRole(instance.identity().getAdminRole());
        if (success)
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
        else {
          if (failonerror())
            throw new ServiceException(ServiceError.OPERATION_DELETE_FAILED, arguments);
          else
            error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, arguments));
        }
      }
      catch (SuperRuntimeException e) {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_DELETE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing admin role in Identity Manager through the discovered
   ** {@link AdminRoleService}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final AdminRoleInstance instance)
    throws ServiceException {

    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, ROLE_ENTITY_NAME, instance.name()));
    if (!exists(instance)) {
      final String[] arguments = { ROLE_ENTITY_NAME, instance.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    else {
      if (instance.displayName() != null)
        instance.identity().getAdminRole().setRoleDisplayName(instance.displayName());
      if (instance.description() != null)
        instance.identity().getAdminRole().setRoleDescription(instance.description());
      if (instance.scoped() != null)
        instance.identity().getAdminRole().setScoped(instance.scoped().booleanValue());

      revokeCapability(instance);
      mergeCapability(instance);
      final Boolean scoped = instance.scoped();
      if (scoped != null) {
        if (scoped) {
          // clear any previuosly assinged scope and reassign the declared scope
          // this leads to an exception
          instance.identity().getAdminRoleRuleScopes().clear();
          disableScope(instance);
          enableScope(instance);
        }
        else {
          defaultScope(instance);
        }
      }
      revokeMember(instance);
      assignMember(instance);
      disablePublication(instance);
      enablePublication(instance);
      try {
        this.roleService.modifyAdminRole(instance.identity());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, ROLE_ENTITY_NAME, instance.name()));
        lookup(instance);
      }
      catch (SuperRuntimeException e) {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_MODIFY_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link AdminRoleInstance} exists in Oracle
   ** Identity Manager through the discovered {@link AdminRoleService}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to check for
   **                            existance.
   **
   ** @return                    <code>true</code> if the
   **                            {@link AdminRoleInstance} exists in the backend
   **                            system; otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final AdminRoleInstance instance)
    throws ServiceException {

    lookup(instance);
    return !StringUtility.isEmpty(instance.entityID());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns an existing admin role of the Identity Manager through the
   ** discovered {@link AdminRoleService}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to lookup.
   **
   ** @return                    the system identifier <code>EntityId</code> for
   **                            the specified {@link RoleInstance}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public AdminRoleVO lookup(final AdminRoleInstance instance)
    throws ServiceException {

    // prevent bogus state
    if (this.roleService == null)
      throw new ServiceException(ServiceError.ABORT, "roleService is null");

    final oracle.iam.platform.authopss.vo.AdminRole identity = this.roleService.getAdminRole(instance.name());
    if (identity != null) {
      instance.entityID(identity.getRoleId().toString());
      instance.identity(this.roleService.getAdminRoleVO(instance.entityID()));
    }

    return instance.identity();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRecipient
  /**
   ** Returns an existing entity of the Identity Manager through the discovered
   ** {@link OrganizationManager}.
   **
   ** @param  recipient          the {@link Publication.Recipient} to lookup.
   **
   ** @return                    the internal identifier for type and name
   **                            contained in {@link Publication.Recipient} or
   **                            <code>null</code> if the specified
   **                            {@link Publication.Recipient} does not exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected User lookupRecipient(final MemberShip.Recipient recipient)
    throws ServiceException {

    // prevent bogus state
    if (this.userService == null)
      throw new ServiceException(ServiceError.ABORT, "userService is null");

    User identity = null;
    try {
      identity = this.userService.getDetails(recipient.name(), null, true);
    }
    catch (NoSuchUserException e) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, recipient.getValue(), recipient.name()));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignCapability
  /**
   ** Assiging capabilities to the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   */
  private void assignCapability(final AdminRoleInstance instance) {
    final List<Capability> capability = instance.identity().getAdminRoleCapabilities();
    for (AdminRole.Resource cursor : instance.resourceAssign()) {
      capability.addAll(capabilities(cursor));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeCapability
  /**
   ** Assiging capabilities to the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   */
  private void mergeCapability(final AdminRoleInstance instance) {
    final List<Capability> existing = instance.identity().getAdminRoleCapabilities();
    for (AdminRole.Resource cursor : instance.resourceAssign()) {
      for (Capability assignee : capabilities(cursor))
        if (!existing.contains(assignee))
          existing.add(assignee);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeCapability
  /**
   ** Revoke capabilities from the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   */
  private void revokeCapability(final AdminRoleInstance instance) {
    final List<Capability> capability = instance.identity().getAdminRoleCapabilities();
    for (AdminRole.Resource cursor : instance.resourceRevoke()) {
      capability.removeAll(capabilities(cursor));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableScope
  /**
   ** Enables scopes for the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   **
   ** @throws ServiceException   if a organization in scope does not exists and
   **                            {@link failonerror()} determines that an
   **                            exception should thrown in this case or an
   **                            unhandled exception occured.
   */
  private void enableScope(final AdminRoleInstance instance)
    throws ServiceException {

    final List<AdminRoleRuleScope> scope = instance.identity().getAdminRoleRuleScopes();
    for (Publication.Recipient recipient : instance.scopeEnable()) {
      final Organization organization = lookupRecipient(recipient);
      if (organization == null) {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name()) };
        if (failonerror())
          throw new FeatureException(FeatureError.SCOPERULE_ASSIGN_FAILED, error);
        else
          error(FeatureResourceBundle.format(FeatureError.SCOPERULE_ASSIGN_FAILED, error));
      }
      final AdminRoleRuleScope rule = createAdminScope(organization, recipient);
      if (!scope.contains(rule)) {
        scope.add(rule);
      }
      else {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name()) };
        error(FeatureResourceBundle.format(FeatureError.SCOPERULE_ASSIGN_FAILED, error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disableScope
  /**
   ** Disables scopes for the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   **
   **
   ** @throws ServiceException   if a organization in scope does not exists and
   **                            {@link failonerror()} determines that an
   **                            exception should thrown in this case or an
   **                            unhandled exception occured.
   */
  private void disableScope(final AdminRoleInstance instance)
    throws ServiceException {

    final List<AdminRoleRuleScope> scope = instance.identity().getAdminRoleRuleScopes();
    for (Publication.Recipient recipient : instance.scopeDisable()) {
      final Organization organization = lookupRecipient(recipient);
      if (organization == null) {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name()) };
        if (failonerror())
          throw new FeatureException(FeatureError.SCOPERULE_REVOKE_FAILED, error);
        else
          error(FeatureResourceBundle.format(FeatureError.SCOPERULE_REVOKE_FAILED, error));
      }
      final AdminRoleRuleScope rule = createAdminScope(organization, recipient);
      if (scope.contains(rule)) {
        scope.remove(rule);
      }
      else {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name()) };
        error(FeatureResourceBundle.format(FeatureError.SCOPERULE_REVOKE_FAILED, error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultScope
  /**
   ** Default scope for the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   **
   ** @throws ServiceException   if a organization in scope does not exists and
   **                            {@link failonerror()} determines that an
   **                            exception should thrown in this case or an
   **                            unhandled exception occured.
   */
  private void defaultScope(final AdminRoleInstance instance)
    throws ServiceException {

    final AdminRoleRuleScope rule = new AdminRoleRuleScope();
    final Organization       organization = lookupOrganization(OrganizationManagerConstants.TOP_ORGANIZATION_NAME);
    rule.setHierarchicalScope(true);
    rule.setScopeId(Long.parseLong(organization.getEntityId()));
    instance.identity().getAdminRoleRuleScopes().clear();
    instance.identity().getAdminRoleRuleScopes().add(rule);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enablePublication
  /**
   ** Enables publication scopes for the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   **
   ** @throws ServiceException   if a organization in scope does not exists and
   **                            {@link failonerror()} determines that an
   **                            exception should thrown in this case or an
   **                            unhandled exception occured.
   */
  private void enablePublication(final AdminRoleInstance instance)
    throws ServiceException {

    final List<EntityPublication> scope = instance.identity().getAdminRolePublication();
    for (Publication.Recipient recipient : instance.enabled()) {
      final Organization organization = lookupRecipient(recipient);
      if (organization == null) {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name()) };
        if (failonerror())
          throw new FeatureException(FeatureError.PUBLICATION_ASSIGN_FAILED, error);
        else
          error(FeatureResourceBundle.format(FeatureError.PUBLICATION_ASSIGN_FAILED, error));
      }
      final EntityPublication publication = createPublication(organization, instance, recipient.hierarchy());
      if (!scope.contains(publication)) {
        scope.add(publication);
      }
      else {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name()) };
        error(FeatureResourceBundle.format(FeatureError.PUBLICATION_ASSIGN_FAILED, error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disablePublication
  /**
   ** Disables publication scopes for the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   **
   ** @throws ServiceException   if a organization in scope does not exists and
   **                            {@link failonerror()} determines that an
   **                            exception should thrown in this case or an
   **                            unhandled exception occured.
   */
  private void disablePublication(final AdminRoleInstance instance)
    throws ServiceException {

    final List<EntityPublication> scope = instance.identity().getAdminRolePublication();
    for (Publication.Recipient recipient : instance.disabled()) {
      final Organization organization = lookupRecipient(recipient);
      if (organization == null) {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name()) };
        if (failonerror())
          throw new FeatureException(FeatureError.PUBLICATION_REVOKE_FAILED, error);
        else
          error(FeatureResourceBundle.format(FeatureError.PUBLICATION_REVOKE_FAILED, error));
      }
      final EntityPublication publication = createPublication(organization, instance, recipient.hierarchy());
      if (scope.contains(publication)) {
        scope.remove(publication);
      }
      else {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, OrganizationManagerConstants.ORGANIZATION_ENTITY, recipient.name()) };
        error(FeatureResourceBundle.format(FeatureError.PUBLICATION_REVOKE_FAILED, error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignMember
  /**
   ** Assigns static user membership to the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   **
   ** @throws ServiceException   if a identity is already a static member and
   **                            {@link failonerror()} determines that an
   **                            exception should thrown in this case.
   */
  private void assignMember(final AdminRoleInstance instance)
    throws ServiceException {

    final List<String> member = instance.identity().getUserAssignedStatic();
    for (MemberShip.Recipient recipient : instance.assignee()) {
      final User identity = lookupRecipient(recipient);
      if (identity == null) {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), UserManagerConstants.USER_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, UserManagerConstants.AttributeName.USER_LOGIN.getId(), recipient.name()) };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_ASSIGN_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_ASSIGN_FAILED, error));
      }
      if (!member.contains(identity.getEntityId())) {
        member.add(identity.getEntityId());
      }
      else {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), UserManagerConstants.USER_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, UserManagerConstants.AttributeName.USER_LOGIN.getId(), recipient.name()) };
        error(ServiceResourceBundle.format(ServiceError.OPERATION_ASSIGN_FAILED, error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeMember
  /**
   ** Revoke static user membership from the specified {@link AdminRoleInstance}.
   **
   ** @param  instance           the {@link AdminRoleInstance} to update.
   **
   ** @throws ServiceException   if a identity isn't a static member and
   **                            {@link failonerror()} determines that an
   **                            exception should thrown in this case.
   */
  private void revokeMember(final AdminRoleInstance instance)
    throws ServiceException {

    final List<String> member = instance.identity().getUserAssignedStatic();
    for (MemberShip.Recipient recipient : instance.revokee()) {
      final User identity = lookupRecipient(recipient);
      if (identity == null) {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), UserManagerConstants.USER_ENTITY, recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, UserManagerConstants.AttributeName.USER_LOGIN.getId(), recipient.name()) };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_REVOKE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_REVOKE_FAILED, error));
      }
      if (member.contains(identity.getEntityId())) {
        member.remove(identity.getEntityId());
      }
      else {
        final String[] error = { ROLE_ENTITY_NAME, instance.name(), UserManagerConstants.AttributeName.USER_LOGIN.getId(), recipient.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTASSINGED, UserManagerConstants.AttributeName.USER_LOGIN.getId(), recipient.name()) };
        error(ServiceResourceBundle.format(ServiceError.OPERATION_REVOKE_FAILED, error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   capabilities
  /**
   ** Factory method to create an Admin Role wrapper with the properties of
   ** this instance.
   **
   ** @return                    the {@link List of {@link Capability} created
   **                            for this instance.
   */
  private List<Capability> capabilities(final AdminRole.Resource resource) {
    final List<Capability>          result = new ArrayList<Capability>();
    final PolicyConstants.Resources type = PolicyConstants.lookupResources(resource.getValue());
    for (PolicyConstants.Actions action : type.getActions()) {
      for (String todo : resource.action()) {
        if (action.getId().equals(todo)) {
          result.addAll(this.roleService.getCapabilitiess(type, action));
        }
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAdminScope
  /**
   ** Returns an {@link EntityPublication} to be passed to publication service
   ** method of Identity Manager through the discovered
   ** {@link EntityPublicationService}.
   **
   ** @param  scope              the details of an {@link Organization}.
   ** @param  recipient          the {@link Publication.Recipient} specifying
   **                            the scope.
   **
   ** @return                    the internal identifier for type and name
   **                            contained in {@link Publication.Recipient} or
   **                            <code>null</code> if the specified
   **                            {@link Publication.Recipient} does not exists.
   */
  protected AdminRoleRuleScope createAdminScope(final Organization scope, final Publication.Recipient recipient) {
    final AdminRoleRuleScope rule = new AdminRoleRuleScope();
    rule.setHierarchicalScope(recipient.hierarchy());
    rule.setScopeId(Long.parseLong(scope.getEntityId()));
    return rule;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildElement (overridden)
  /**
   ** Factory method to create a <code>Catalog Element</code>.
   **
   ** @return                    a {@link CatalogElement} instance.
   */
  @Override
  protected CatalogElement buildElement() {
    return new AdminRoleInstance();
  }
}