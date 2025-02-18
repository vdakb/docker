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

    File        :   OrganizationHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OrganizationHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.apache.tools.ant.BuildException;

import oracle.iam.platformservice.api.AdminRoleService;

import oracle.iam.platform.authopss.vo.AdminRole;
import oracle.iam.platform.authopss.vo.AdminRoleMembership;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.NoSuchOrganizationException;

import oracle.iam.identity.exception.OrganizationCreateException;
import oracle.iam.identity.exception.OrganizationDeleteException;
import oracle.iam.identity.exception.OrganizationEnableException;
import oracle.iam.identity.exception.OrganizationDisableException;
import oracle.iam.identity.exception.OrganizationModificationException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeaturePlatformTask;

import oracle.iam.identity.deployment.type.MemberShip;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>OrganizationHandler</code> creates,deletes and configures a
 ** organization in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OrganizationHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the User Identity Instance to configure or to create */
  private OrganizationInstance       single;

  /** the name of the User Identity to configure or to create */
  private List<OrganizationInstance> multiple = new ArrayList<OrganizationInstance>();

  /** the business logic layer to operate on organizations */
  private OrganizationManager        facade;

  /** the business logic layer to operate on users */
  private UserManager                userService;

  /** the business logic layer to operate on administrative roles */
  private AdminRoleService           adminService;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>OrganizationHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public OrganizationHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the organization related to Identity Manager.
   **
   ** @return                    the name of the organization related to
   **                            Identity Manager.
   */
  public final String name() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Oracle
   **                            Identity Manager organization.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager organization.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    if (this.single == null)
      this.single = new OrganizationInstance();

    // add the value pair to the parameters
    this.single.addParameter(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager organization.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that is already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    if (this.single == null)
      this.single = new OrganizationInstance();

    // add the value pairs to the parameters
    this.single.addParameter(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the <code>name</code> of the user profile related to
   ** Identity Manager.
   **
   ** @param  name               the <code>name</code> of the user profile
   **                            related to Identity Manager.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = new OrganizationInstance();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRole
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  instance           the {@link OrganizationInstance} to add.
   **
   ** @throws BuildException     if the specified {@link OrganizationInstance}
   **                            is already assigned to this task.
   */
  public void addRole(final RoleInstance instance) {
    if (this.single == null)
      this.single = new OrganizationInstance();

    // add the instance to the object to handle
    this.single.addRole(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  instance           the {@link OrganizationInstance} to add.
   **
   ** @throws BuildException     if the specified {@link OrganizationInstance}
   **                            is already assigned to this task.
   */
  public void addInstance(final OrganizationInstance instance) {
    // prevent bogus input
    if ((this.single != null && this.single.equals(instance)) || this.multiple.contains(instance))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, instance.name()));

    // add the instance to the object to handle
    this.multiple.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new user profile in Identity Manager through the discovered
   ** {@link OrganizationManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade       = task.service(OrganizationManager.class);
    this.userService  = task.service(UserManager.class);
    this.adminService = task.service(AdminRoleService.class);

    if (this.single != null)
      create(this.single);

    for (OrganizationInstance i : this.multiple)
      create(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing user profile in Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade       = task.service(OrganizationManager.class);
    this.userService  = task.service(UserManager.class);
    this.adminService = task.service(AdminRoleService.class);

    if (this.single != null)
      modify(this.single);

    for (OrganizationInstance i : this.multiple)
      modify(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing user profile from Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(OrganizationManager.class);

    if (this.single != null)
      delete(this.single);

    for (OrganizationInstance i : this.multiple)
      delete(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an existing user profile from Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void enable(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(OrganizationManager.class);

    if (this.single != null)
      enable(this.single);

    for (OrganizationInstance i : this.multiple)
      enable(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an existing user profile from Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disable(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(OrganizationManager.class);

    if (this.single != null)
      disable(this.single);

    for (OrganizationInstance i : this.multiple)
      disable(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new organization in Identity Manager through the discovered
   ** {@link OrganizationManager}.
   **
   ** @param  instance           the {@link OrganizationInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final OrganizationInstance instance)
    throws ServiceException {

    final String[] arguments = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name() };
    if (exists(instance)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, arguments));
      modify(instance);
    }
    else {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, arguments));
      try {
        // extend the attribute mapping with the name of the organization to
        // create
        final HashMap<String, Object> parameter = instance.parameter();
        parameter.put(OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), arguments[1]);
        // extend the attribute mapping with the parent key the organization
        // belongs to
        final String parentName = (String)instance.parameter().remove(OrganizationManagerConstants.AttributeName.ORG_PARENT_NAME.getId());
        if (!StringUtility.isEmpty(parentName))
          parameter.put(OrganizationManagerConstants.AttributeName.ORG_PARENT_KEY.getId(), entityID(parentName));

        this.facade.create(new Organization(null, parameter));
        final String entityID = entityID(arguments[1]);

        // check if we have to modify administrative role memberships
        if (instance.role().size() > 0)
          modifyAdministration(Long.valueOf(entityID), instance.role());

        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, arguments));
      }
      catch (OrganizationCreateException e) {
        final String[] error = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name(), e.getLocalizedMessage() };
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
   ** Deletes an existing organization in Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  instance           the {@link OrganizationInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final OrganizationInstance instance)
    throws ServiceException {

    final String[] arguments = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name() };
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
    final String entityID = entityID(instance.name());
    if (!StringUtility.isEmpty(entityID))
      try {
        this.facade.delete(entityID, true);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
      }
      catch (OrganizationDeleteException e) {
        final String[] error = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_DELETE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing organization in Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  instance           the {@link OrganizationInstance} to configure.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final OrganizationInstance instance)
    throws ServiceException {

    final String[] arguments = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name() };
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, arguments));
    final String entityID = entityID(instance.name());
    if (!StringUtility.isEmpty(entityID))
      try {
        // extend the attribute mapping with the parent key the organization
        // belongs to
        final String parentName = (String)instance.parameter().remove(OrganizationManagerConstants.AttributeName.ORG_PARENT_NAME.getId());
        if (!StringUtility.isEmpty(parentName))
          instance.parameter().put(OrganizationManagerConstants.AttributeName.ORG_PARENT_KEY.getId(), entityID(parentName));

        // check if we have to modify the instance itself
        if (instance.parameter().size() > 0) {
          final Organization identity = new Organization(entityID, instance.parameter());
          this.facade.modify(identity);
        }

        // check if we have to modify administrative role memberships
        if (instance.role().size() > 0)
          modifyAdministration(Long.valueOf(entityID), instance.role());

        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, arguments));
      }
      catch (OrganizationModificationException e) {
        final String[] error = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_MODIFY_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an existing organization in Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  instance           the {@link OrganizationInstance} to enable.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void enable(final OrganizationInstance instance)
    throws ServiceException {

    final String entityID = entityID(instance.name());
    if (!StringUtility.isEmpty(entityID))
      try {
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ENABLE_BEGIN, OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name()));
        this.facade.enable(entityID, true);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ENABLE_SUCCESS, OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name()));
      }
      catch (OrganizationEnableException e) {
        final String[] error = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_ENABLE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_ENABLE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      final String[] arguments = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an existing organization in Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  instance           the {@link OrganizationInstance} to enable.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disable(final OrganizationInstance instance)
    throws ServiceException {

    final String entityID = entityID(instance.name());
    if (!StringUtility.isEmpty(entityID))
      try {
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DISABLE_BEGIN, OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name()));
        this.facade.disable(entityID, true);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DISABLE_SUCCESS, OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name()));
      }
      catch (OrganizationDisableException e) {
        final String[] error = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_DISABLE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_DISABLE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      final String[] arguments = { OrganizationManagerConstants.ORGANIZATION_ENTITY, instance.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link OrganizationInstance} exists in Oracle
   ** Identity Manager through the discovered {@link OrganizationManager}.
   **
   ** @param  instance           the {@link OrganizationInstance} to check for
   **                            existance.
   **
   ** @return                    <code>true</code> if the
   **                            {@link OrganizationInstance} exists in the
   **                            backend system; otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final OrganizationInstance instance)
    throws ServiceException {

    return !StringUtility.isEmpty(entityID(instance.name()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityID
  /**
   ** Returns an existing organization of the Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private String entityID(final String name)
    throws ServiceException {

    Organization identity = null;
    try {
      identity = this.facade.getDetails(name, null, true);
    }
    catch (NoSuchOrganizationException e) {
      final String[] arguments = { OrganizationManagerConstants.ORGANIZATION_ENTITY, name };
      debug(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return (identity == null) ? null : identity.getEntityId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    final ServiceOperation operation = this.operation();
    if (ServiceOperation.create == operation || ServiceOperation.modify == operation) {
      final boolean strict = ServiceOperation.create == operation;
      try {
        if (this.single != null)
          this.single.validate(strict);

        for (OrganizationInstance i : this.multiple)
          i.validate(strict);
      }
      catch (Exception e) {
        throw new BuildException(e.getLocalizedMessage());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAdministration
  /**
   ** Modifies the membership of administrative roles on the specified
   ** organization.
   **
   ** @param  scopeID             the {@link Organization} were the
   **                             membership of administrative roles has to be
   **                             modified.
   ** @param  collection          the {@link RoleInstance} were the
   **                             membership of administrative roles has to be
   **                             modified.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void modifyAdministration(final Long scopeID, final List<RoleInstance> collection)
    throws ServiceException {

    final List<AdminRole> defined = this.adminService.getAdminRoles(String.valueOf(scopeID));
    for (RoleInstance role : collection) {
      for (AdminRole cursor : defined) {
        if (cursor.getRoleDisplayName().equals(role.name())) {
          // at first revoke all configured memberships
          revokeAdministration(scopeID, cursor, role.revokee());
          // at second assign all configured memberships
          grantAdministration(scopeID, cursor, role.assignee());
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grantAdministration
  /**
   ** Grants the membership of administrative roles on the specified
   ** organization to recipients.
   **
   ** @param  scopeID             the {@link Organization} were the
   **                             membership of administrative roles has to be
   **                             maintained.
   ** @param  role                the {@link AdminRole} were the
   **                             membership of administrative roles has to be
   **                             maintained.
   ** @param  collection          the {@link List} of recipient were the
   **                             membership of administrative roles has to be
   **                             maintained.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void grantAdministration(final Long scopeID, final AdminRole role, final List<MemberShip.Recipient> collection)
    throws ServiceException {

    final String[]            arguments = { "Admin Role", SystemConstant.EMPTY, UserManagerConstants.USER_ENTITY, SystemConstant.EMPTY };
    final AdminRoleMembership memberShip = new AdminRoleMembership(SystemConstant.EMPTY, role, scopeID, false);
    for (MemberShip.Recipient identity : collection) {
      final String userID = userID(identity.name());
      if (!StringUtility.isEmpty(userID)) {
        arguments[3] = identity.name();
        memberShip.setUserId(userID);
        memberShip.setHierarchicalScope(identity.hierarchy());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
        this.adminService.addAdminRoleMembership(memberShip);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeAdministration
  /**
   ** Revokes the membership of administrative roles on the specified
   ** organization from recipients.
   **
   ** @param  scopeID             the {@link Organization} were the
   **                             membership of administrative roles has to be
   **                             maintained.
   ** @param  role                the {@link AdminRole} were the
   **                             membership of administrative roles has to be
   **                             maintained.
   ** @param  collection          the {@link List} of recipient were the
   **                             membership of administrative roles has to be
   **                             maintained.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void revokeAdministration(final Long scopeID, final AdminRole role, final List<MemberShip.Recipient> collection)
    throws ServiceException {

    final String[]            arguments = { "Admin Role", SystemConstant.EMPTY, UserManagerConstants.USER_ENTITY, SystemConstant.EMPTY };
    final AdminRoleMembership memberShip = new AdminRoleMembership(SystemConstant.EMPTY, role, scopeID, false);
    for (MemberShip.Recipient identity : collection) {
      final String userID = userID(identity.name());
      if (!StringUtility.isEmpty(userID)) {
        arguments[3] = identity.name();
        memberShip.setUserId(userID);
        memberShip.setHierarchicalScope(identity.hierarchy());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
        this.adminService.removeAdminRoleMembership(memberShip);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userID
  /**
   ** Returns an existing user of the Identity Manager through the given
   ** {@link UserManager}.
   **
   ** @param  name               the login name of the user to lookup the
   **                            internal identifier.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private String userID(final String name)
    throws ServiceException {

    User identity = null;
    try {
      identity = this.userService.getDetails(name, null, true);
    }
    catch (NoSuchUserException e) {
      final String[] arguments = { UserManagerConstants.USER_ENTITY, name };
      if (failonerror())
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return (identity == null) ? null : identity.getEntityId();
  }
}