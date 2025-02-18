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

    File        :   RoleHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleHandler.


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

import Thor.API.Operations.tcAccessPolicyOperationsIntf;
import Thor.API.tcResultSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;
import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.accesspolicy.vo.AccessPolicy;
import oracle.iam.catalog.api.CatalogService;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.deployment.type.MemberShip;
import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.RoleCreateException;
import oracle.iam.identity.exception.RoleDeleteException;
import oracle.iam.identity.exception.RoleModifyException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.rolemgmt.api.RoleCategoryManager;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleCategory;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.platform.authopss.api.PolicyConstants;
import oracle.iam.platformservice.api.EntityPublicationService;
////////////////////////////////////////////////////////////////////////////////
// class RoleHandler
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>RoleHandler</code> creates,deletes and configures a <code>Role</code>
 ** in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class RoleHandler extends EntitlementHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the business logic layer to operate on <code>Role</code> categories */
  private RoleCategoryManager          categoryService;

  /** the business logic layer to operate on <code>Access Policy</code> */
  private tcAccessPolicyOperationsIntf policyService;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RoleHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public RoleHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend, PolicyConstants.Resources.ROLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Called to inject the argument for parameter <code>category</code>.
   **
   ** @param  category           the category of the <code>Role</code> in
   **                            Identity Manager to handle.
   */
  public void category(final String category) {
    if (this.single == null)
      this.single = buildElement();

    ((RoleInstance)this.single).category(category);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns the category of the <code>Role</code> in Identity Manager to
   ** handle.
   **
   ** @return                    the category of the <code>Role</code> in
   **                            Identity Manager to handle.
   */
  public final String category() {
    return this.single == null ? null : ((RoleInstance)this.single).category();
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
   ** @param  recipient          the {@link List} of recipients either user
   **                            profiles or <code>Role</code>s in Identity
   **                            Manager to become membership of the
   **                            <code>Role</code>.
   */
  public void addMemberShip(final ServiceOperation action, final List<MemberShip.Recipient> recipient) {
    if (this.single == null)
      this.single = buildElement();

    // add the value pair to the parameters
    ((RoleInstance)this.single).addMemberShip(action, recipient);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>Role</code> in Identity Manager through the discovered
   ** {@link RoleManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.roleService         = task.service(RoleManager.class);
    this.userService         = task.service(UserManager.class);
    this.policyService       = task.service(tcAccessPolicyOperationsIntf.class);
    this.categoryService     = task.service(RoleCategoryManager.class);
    this.publisherService    = task.service(EntityPublicationService.class);
    this.organizationService = task.service(OrganizationManager.class);

    if (this.single != null) {
      final RoleInstance single = (RoleInstance)this.single;
      create(single);
      // at first we disable the publications ...
      disable(single);
      // ... and enabling the new publications afterwards
      enable(single);
      // ... and adding the new memberships afterwards
      assign(single);
    }

    for (CatalogElement cursor : this.multiple) {
      final RoleInstance role = (RoleInstance)cursor;
      create(role);
      // at first we disable the publications ...
      disable(role);
      // ... and enabling the new publications afterwards
      enable(role);
      // ... and adding the new memberships afterwards
      assign(role);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>Role</code> from Identity Manager through the
   ** discovered {@link RoleManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.roleService = task.service(RoleManager.class);

    if (this.single != null) {
      final RoleInstance single = (RoleInstance)this.single;
      delete(single);
    }

    for (CatalogElement cursor : this.multiple) {
      final RoleInstance role = (RoleInstance)cursor;
      delete(role);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Mofifies an existing <code>Role</code> in Identity Manager through the
   ** discovered {@link RoleManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.roleService         = task.service(RoleManager.class);
    this.userService         = task.service(UserManager.class);
    this.catalogService      = task.service(CatalogService.class);
    this.policyService       = task.service(tcAccessPolicyOperationsIntf.class);
    this.categoryService     = task.service(RoleCategoryManager.class);
    this.publisherService    = task.service(EntityPublicationService.class);
    this.organizationService = task.service(OrganizationManager.class);

    if (this.single != null) {
      final RoleInstance single = (RoleInstance)this.single;
      modify(single);
      // at first we disable the publications ...
      disable(single);
      // ... and enabling the new publications afterwards
      enable(single);
      // ... revoke the memberships afterwards
      revoke(single);
      // ... and adding the new memberships afterwards
      assign(single);
      // ... and we revoke the entity publications ...
      unpublish(single);
      // ... and adding the new entity publications afterwards
      publish(single, null);
    }

    for (CatalogElement cursor : this.multiple) {
      final RoleInstance role = (RoleInstance)cursor;
      modify(role);
      // at first we disable the publications ...
      disable(role);
      // ... and enabling the new publications afterwards
      enable(role);
      // ... revoke the memberships afterwards
      revoke(role);
      // ... and adding the new memberships afterwards
      assign(role);
      // ... and we revoke the entity publications ...
      unpublish(role);
      // ... and adding the new entity publications afterwards
      publish(role, null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>Role</code> in Identity Manager through the discovered
   ** {@link RoleManager}.
   **
   ** @param  instance           the {@link RoleInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final RoleInstance instance)
    throws ServiceException {

    // extend the attribute mapping with the name of the role to create
    final HashMap<String, Object> parameter = instance.parameter();
    parameter.put(RoleManagerConstants.ROLE_NAME, instance.name());
    parameter.put(RoleManagerConstants.ROLE_DISPLAY_NAME, instance.displayName());
    parameter.put(RoleManagerConstants.ROLE_DESCRIPTION, instance.description());

    // let identity manager decide waht the default category of a role is
    // across releases
    if (!StringUtility.isEmpty(instance.category()))
      parameter.put(RoleManagerConstants.ROLE_CATEGORY_KEY, lookupCategory(instance.category()));

    final Role subject = new Role(parameter);
    if (exists(instance)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, RoleManagerConstants.ROLE_ENTITY_NAME, instance.name()));
      modify(instance);
    }
    else
      try {
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, RoleManagerConstants.ROLE_ENTITY_NAME, instance.name()));
        this.roleService.create(subject);
        // fetch through the ID if the ceated role to keep further processes in
        // sync
        lookup(instance);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, RoleManagerConstants.ROLE_ENTITY_NAME, instance.name()));
      }
      catch (RoleCreateException e) {
        final String[] error = { RoleManagerConstants.ROLE_ENTITY_NAME, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_CREATE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>Role</code> in Identity Manager through the
   ** discovered {@link RoleManager}.
   **
   ** @param  instance           the {@link RoleInstance} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final RoleInstance instance)
    throws ServiceException {

    final String entityID = lookup(instance);
    if (!StringUtility.isEmpty(entityID)) {
      try {
        final String[] arguments = { RoleManagerConstants.ROLE_ENTITY_NAME, instance.name() };
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
        this.roleService.delete(entityID);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
      }
      catch (RoleDeleteException e) {
        final String[] error = { RoleManagerConstants.ROLE_ENTITY_NAME, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_DELETE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
    else {
      final String[] arguments = { RoleManagerConstants.GROUP_NAME, instance.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing <code>Role</code> in Identity Manager through the
   ** discovered {@link RoleManager}.
   **
   ** @param  instance           the {@link RoleInstance} to update.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final RoleInstance instance)
    throws ServiceException {

    final String entityID = lookup(instance);
    if (!StringUtility.isEmpty(entityID)) {
      // modify the role only if the are parameter that requires modification
      if (!instance.parameter().isEmpty()) {
        // extend the attribute mapping with the name of the role to create
        final HashMap<String, Object> parameter = instance.parameter();
        parameter.put(RoleManagerConstants.ROLE_CATEGORY_KEY, lookupCategory(instance.category()));
        try {
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, RoleManagerConstants.ROLE_ENTITY_NAME, instance.name()));
          final Role identity = new Role(entityID, instance.parameter());
          this.roleService.modify(identity);
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, RoleManagerConstants.ROLE_ENTITY_NAME, instance.name()));
        }
        catch (RoleModifyException e) {
          final String[] error = { RoleManagerConstants.ROLE_ENTITY_NAME, instance.name(), e.getLocalizedMessage() };
          if (failonerror())
            throw new ServiceException(ServiceError.OPERATION_MODIFY_FAILED, error);
          else
            error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, error));
        }
        catch (Exception e) {
          throw new ServiceException(ServiceError.UNHANDLED, e);
        }
      }
    }
    else {
      final String[] arguments = { RoleManagerConstants.ROLE_ENTITY_NAME, instance.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Grants membership to the <code>Role</code> in Identity Manager through the
   ** discovered {@link RoleManager}.
   **
   ** @param  instance           the {@link RoleInstance} to update.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void assign(final RoleInstance instance)
    throws ServiceException {

    final String[]             arguments = { SystemConstant.EMPTY, SystemConstant.EMPTY, RoleManagerConstants.ROLE_ENTITY_NAME, instance.name(), SystemConstant.EMPTY };
    List<MemberShip.Recipient> recipient = instance.assignee();
    if (!recipient.isEmpty()) {
      final long[] identifier = { Long.valueOf(instance.entityID()) };
      try {
        for (MemberShip.Recipient identity : recipient) {
          try {
            if (UserManagerConstants.USER_ENTITY.equals(identity.getValue())) {
              arguments[0] = UserManagerConstants.AttributeName.USER_LOGIN.getId();
              arguments[1] = identity.name();
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_BEGIN, arguments));
              this.roleService.grantRole(RoleManagerConstants.ROLE_NAME, arguments[1], arguments[2], arguments[3]);
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_SUCCESS, arguments));
            }
            else if (RoleManagerConstants.ROLE_ENTITY_NAME.equals(identity.getValue())) {
              arguments[0] = RoleManagerConstants.ROLE_NAME;
              arguments[1] = identity.name();
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_BEGIN, arguments));
              this.roleService.addRoleRelationship(RoleManagerConstants.ROLE_NAME, arguments[1], arguments[2], arguments[3]);
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_SUCCESS, arguments));
            }
            else if (AccessPolicy.ENTITY_TYPE.equals(identity.getValue())) {
              arguments[0] = AccessPolicy.ENTITY_TYPE;
              arguments[1] = identity.name();
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_BEGIN, arguments));
              final long subject = lookupPolicy(identity.name());
              if (subject != -1L) {
                boolean           skip = false;
                final tcResultSet resultSet = this.policyService.getAssignedGroups(subject);
                if (resultSet.getRowCount() > 0) {
                  for (int i = 0; i < resultSet.getRowCount(); i++) {
                    resultSet.goToRow(i);
                    if (identifier[0] == resultSet.getLongValue("Groups.Key")) {
                      skip = true;
                      break;
                    }
                  }
                }
                if (skip)
                  warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, arguments));
                else {
                  this.policyService.assignGroups(subject, identifier);
                  info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_SUCCESS, arguments));
                }
              }
            }
          }
          catch (Exception e) {
            arguments[4] = e.getLocalizedMessage();
            if (failonerror())
              throw new ServiceException(ServiceError.OPERATION_ASSIGN_FAILED, arguments);
            else
              error(ServiceResourceBundle.format(ServiceError.OPERATION_ASSIGN_FAILED, arguments));
          }
        }
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Grants membership to the <code>Role</code> in Identity Manager through the
   ** discovered {@link RoleManager}.
   **
   ** @param  instance           the {@link RoleInstance} to update.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void revoke(final RoleInstance instance)
    throws ServiceException {

    final String[]             arguments = { SystemConstant.EMPTY, SystemConstant.EMPTY, RoleManagerConstants.ROLE_ENTITY_NAME, instance.name(), SystemConstant.EMPTY };
    List<MemberShip.Recipient> recipient = instance.revokee();
    if (!recipient.isEmpty()) {
      final long[] identifier = { Long.valueOf(instance.entityID()) };
      try {
        for (MemberShip.Recipient identity : recipient) {
          try {
            if (UserManagerConstants.USER_ENTITY.equals(identity.getValue())) {
              arguments[0] = UserManagerConstants.AttributeName.USER_LOGIN.getId();
              arguments[1] = identity.name();
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
              this.roleService.revokeRoleGrant(RoleManagerConstants.ROLE_NAME, arguments[1], arguments[2], arguments[3]);
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
            }
            else if (RoleManagerConstants.ROLE_ENTITY_NAME.equals(identity.getValue())) {
              arguments[0] = RoleManagerConstants.ROLE_NAME;
              arguments[1] = identity.name();
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
              this.roleService.removeRoleRelationship(RoleManagerConstants.ROLE_NAME, arguments[1], arguments[2], arguments[3]);
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
            }
            else if (AccessPolicy.ENTITY_TYPE.equals(identity.getValue())) {
              arguments[0] = AccessPolicy.ENTITY_TYPE;
              arguments[1] = identity.name();
              info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
              final long subject = lookupPolicy(identity.name());
              if (subject != -1L) {
                boolean           skip = true;
                final tcResultSet resultSet = this.policyService.getAssignedGroups(subject);
                if (resultSet.getRowCount() > 0) {
                  for (int i = 0; i < resultSet.getRowCount(); i++) {
                    resultSet.goToRow(i);
                    if (identifier[0] == resultSet.getLongValue("Groups.Key")) {
                      skip = false;
                      break;
                    }
                  }
                }
                if (skip)
                  warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTASSINGED, arguments));
                else {
                  this.policyService.unAssignGroups(subject, identifier);
                  info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
                }
              }
            }
          }
          catch (Exception e) {
            arguments[4] = e.getLocalizedMessage();
            if (failonerror())
              throw new ServiceException(ServiceError.OPERATION_REVOKE_FAILED, arguments);
            else
              error(ServiceResourceBundle.format(ServiceError.OPERATION_REVOKE_FAILED, arguments));
          }
        }
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link RoleInstance} exists in Identity Manager
   ** through the discovered {@link RoleManager}.
   **
   ** @param  instance           the {@link RoleInstance} to check for
   **                            existance.
   **
   ** @return                    <code>true</code> if the
   **                            {@link RoleInstance} exists in the backend
   **                            system; otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final RoleInstance instance)
    throws ServiceException {

    return !StringUtility.isEmpty(lookup(instance));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns an existing <code>Role</code> from Identity Manager through the
   ** discovered {@link RoleManager}.
   **
   ** @param  instance           the {@link RoleInstance} to lookup.
   **
   ** @return                    the system identifier <code>EntityId</code> for
   **                            the specified {@link RoleInstance}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public String lookup(final RoleInstance instance)
    throws ServiceException {

    // prevent bogus state
    if (this.roleService == null)
      throw new ServiceException(ServiceError.ABORT, "roleService is null");

    Role identity = null;
    try {
      identity = this.roleService.getDetails(RoleManagerConstants.ROLE_NAME, instance.name(), null);
      instance.entityID(identity.getEntityId());
      instance.categoryID((Long)identity.getAttribute(RoleManagerConstants.ROLE_CATEGORY_KEY));
    }
    catch (NoSuchRoleException e) {
      instance.entityID(null);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return (identity == null) ? null : identity.getEntityId();
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
    return new RoleInstance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupCategory
  /**
   ** Returns an existing <code>Role</code> category in Identity Manager through
   ** the discovered {@link RoleCategoryManager}.
   **
   ** @param  category           the name of a <code>Role</code> category to
   **                            lookup.
   **
   ** @return                    the internal system identifier of the category
   **                            or <code>null</code> if the specified
   **                            <code>category</code> does not exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Long lookupCategory(final String category)
    throws ServiceException {

    // prevent bogus state
    if (this.categoryService == null)
      throw new ServiceException(ServiceError.ABORT, "categoryService is null");

    try {
      final RoleCategory identity = this.categoryService.getDetails(RoleManagerConstants.ROLE_CATEGORY_NAME, category, null);
      if (identity == null) {
        final String[] arguments = { RoleManagerConstants.ROLE_CATEGORY_NAME, category };
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      }
      return new Long(identity.getEntityId());
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupPolicy
  /**
   ** Returns an existing <code>Access Policy</code> in Identity Manager through
   ** the discovered {@link tcAccessPolicyOperationsIntf}.
   **
   ** @param  policyName         the name of a <code>Access Policy</code> to
   **                            lookup.
   **
   ** @return                    the internal system identifier of the
   **                            <code>Access Policy</code> or <code>-1L</code>
   **                            if the specified <code>Access Policy</code>
   **                            does not exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Long lookupPolicy(final String policyName)
    throws ServiceException {

    // prevent bogus state
    if (this.policyService == null)
      throw new ServiceException(ServiceError.ABORT, "policyService is null");

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put("Access Policies.Name", policyName);

    long identifier = -1L;
    try {
      @SuppressWarnings({ "oracle.jdeveloper.java.method-deprecated", "oracle.jdeveloper.java.semantic-warning" })
      tcResultSet resultSet = this.policyService.findAccessPolicies(filter);
      if (resultSet.getRowCount() == 0)
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, AccessPolicy.ENTITY_TYPE, policyName));
      else if (resultSet.getRowCount() > 1)
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, AccessPolicy.ENTITY_TYPE, policyName));
      else
        identifier = resultSet.getLongValue("Access Policies.Key");
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return identifier;
  }
}