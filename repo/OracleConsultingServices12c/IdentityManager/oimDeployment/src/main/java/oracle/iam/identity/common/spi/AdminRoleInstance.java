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

    File        :   AdminRoleInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AdminRoleInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.authopss.vo.Capability;
import oracle.iam.platform.authopss.vo.AdminRoleVO;
import oracle.iam.platform.authopss.vo.EntityPublication;
import oracle.iam.platform.authopss.vo.AdminRoleRuleScope;

import oracle.hst.deployment.ServiceOperation;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.deployment.type.AdminRole;
import oracle.iam.identity.deployment.type.MemberShip;
import oracle.iam.identity.deployment.type.Publication;

////////////////////////////////////////////////////////////////////////////////
// class AdminRoleInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>AdminRoleInstance</code> represents an admin role in Identity Manager
 ** that might be created, deleted or changed after or during an import
 ** operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AdminRoleInstance extends EntitlementData {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Boolean scoped = null;

  private List<AdminRole.Resource> assign = new ArrayList<AdminRole.Resource>();
  private List<AdminRole.Resource> revoke = new ArrayList<AdminRole.Resource>();

  private List<Publication.Recipient> allowed = new ArrayList<Publication.Recipient>();
  private List<Publication.Recipient> denied = new ArrayList<Publication.Recipient>();

  private List<MemberShip.Recipient> assignee = new ArrayList<MemberShip.Recipient>();
  private List<MemberShip.Recipient> revokee = new ArrayList<MemberShip.Recipient>();

  private AdminRoleVO delegate = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AdminRoleInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AdminRoleInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   scoped
  /**
   ** ...
   **
   ** @param  value              <code>true</code> if this admin roles is
   **                            scoped; otherwise <code>false</code>
   */
  public final void scoped(final boolean value) {
    this.scoped = Boolean.valueOf(value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   scoped
  /**
   ** ...
   **
   ** @return                    <code>true</code> if this admin roles is
   **                            scoped; otherwise <code>false</code>
   */
  public final Boolean scoped() {
    return this.scoped;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   isScoped
  /**
   ** ...
   **
   ** @return                    <code>true</code> if this admin roles is
   **                            scoped; otherwise <code>false</code>
   */
  public final boolean isScoped() {
    return this.scoped == null ? true : this.scoped.booleanValue();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   scopeEnable
  /**
   ** Returns the {@link List} of scopes in Identity Manager to be enabled for
   ** the admin role.
   **
   ** @return                    the {@link List} of scopes in Identity Manager
   **                            to be assigned to this admin role.
   */
  public final List<Publication.Recipient> scopeEnable() {
    return this.allowed;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   scopeDisable
  /**
   ** Returns the {@link List} of scopes in Identity Manager to be disabled for
   ** the admin role.
   **
   ** @return                    the {@link List} of scopes in Identity Manager
   **                            to be disabled for this admin role.
   */
  public final List<Publication.Recipient> scopeDisable() {
    return this.denied;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   resourceAssign
  /**
   ** Returns the {@link List} of resources in Identity Manager to be assigned
   ** to the admin role.
   **
   ** @return                    the {@link List} of resources in Identity
   **                            Manager to be assigned to this admin role.
   */
  public final List<AdminRole.Resource> resourceAssign() {
    return this.assign;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   resourceRevoke
  /**
   ** Returns the {@link List} of resources in Identity Manager to be revoked
   ** from the admin role.
   **
   ** @return                    the {@link List} of resources in Identity
   **                            Manager to be revoked from this admin role.
   */
  public final List<AdminRole.Resource> resourceRevoke() {
    return this.revoke;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   assignee
  /**
   ** Returns the {@link List} of recipients in Identity Manager to be assigned
   ** to the role.
   **
   ** @return                    the {@link List} of recipients in Identity
   **                            Manager to be assigned to this role.
   */
  public final List<MemberShip.Recipient> assignee() {
    return this.assignee;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   revokee
  /**
   ** Returns the {@link List} of recipients in Identity Manager to be revoked
   ** from this role.
   **
   ** @return                    the {@link List} of recipients in Identity
   **                            Manager to be revoked from this role.
   */
  public final List<MemberShip.Recipient> revokee() {
    return this.revokee;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** The validation is performed in two ways depended on the passed in mode
   ** requested by argument <code>strict</code>. If <code>strict</code> is set
   ** to <code>true</code> the validation is extended to check for all the
   ** mandatory attributes of a role like catogory and additional parameters. If
   ** it's <code>false</code> only the name of the role has to be present.
   **
   ** @param  operation          the operational mode of validation.
   **                            If it's set to <code>create</code> the
   **                            validation is extended to check for all the
   **                            mandatory parameters of a role like displayName
   **                            etc. If it's something else only the name of
   **                            the role has to be present.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate(final ServiceOperation operation)
    throws BuildException {

    if (StringUtility.isEmpty(this.name()))
      handleAttributeMissing("name");

    // enforce validation of mandatory attributes if requested
    if (operation == ServiceOperation.create) {
      if (StringUtility.isEmpty(displayName()))
        handleAttributeMissing("displayName");

      if (this.revoke.size() > 0)
        handleElementUnexpected("permission", ServiceOperation.revoke.id());

      if (this.revokee.size() > 0)
        handleElementUnexpected("memberShip", ServiceOperation.revoke.id());
    }
    else if (operation == ServiceOperation.delete) {
      if (!this.parameter().isEmpty())
        handleElementUnexpected("parameter", operation.id());

      if (this.assign.size() > 0)
        handleElementUnexpected("permission", ServiceOperation.assign.id());

      if (this.revoke.size() > 0)
        handleElementUnexpected("permission", ServiceOperation.revoke.id());

      if (this.assignee.size() > 0)
        handleElementUnexpected("memberShip", ServiceOperation.assign.id());

      if (this.revokee.size() > 0)
        handleElementUnexpected("memberShip", ServiceOperation.revoke.id());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addResource
  /**
   ** Add the specified resource that has to be applied during an operation.
   **
   ** @param  action             the operation to apply on the specified
   **                            <code>capabilities</code>; either
   **                            {@link ServiceOperation#assign} or
   **                            {@link ServiceOperation#revoke}.
   ** @param  resource           the {@link List} of resources to be assinged
   **                            or revoked.
   */
  public void addResource(final ServiceOperation action, final List<AdminRole.Resource> resource) {
    if (ServiceOperation.assign == action)
      this.assign.addAll(resource);
    else if (ServiceOperation.revoke == action)
      this.revoke.addAll(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addScope
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  action             the operation to apply on the specified
   **                            <code>recipient</code>s; either
   **                            {@link ServiceOperation#enable} or
   **                            {@link ServiceOperation#disable}.
   ** @param  recipient          the {@link List} of organizational recipients
   **                            in Identity Manager to become access of the
   **                            entitlement.
   */
  public void addScope(final ServiceOperation action, final List<Publication.Recipient> recipient) {
    if (!isScoped()) {
      handleElementUnexpected("scope", action.id());
    }
    if (ServiceOperation.enable == action)
      this.allowed.addAll(recipient);
    else if (ServiceOperation.disable == action)
      this.denied.addAll(recipient);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMemberShip
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  action             the operation to apply on the specified
   **                            <code>userid</code>; either
   **                            {@link ServiceOperation#assign} or
   **                            {@link ServiceOperation#revoke}.
   ** @param  recipient          the {@link List} of recipients either user
   **                            profiles or roles in Identity Manager to
   **                            become membership of the role.
   */
  public void addMemberShip(final ServiceOperation action, final List<MemberShip.Recipient> recipient) {
    if (ServiceOperation.assign == action)
      this.assignee.addAll(recipient);
    else if (ServiceOperation.revoke == action)
      this.revokee.addAll(recipient);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identity
  /**
   ** Factory method to create an Admin Role wrapper with the properties of
   ** this instance.
   **
   ** @return                    the {@link AdminRoleVO} created for this instance.
   */
  public AdminRoleVO identity() {
    if (this.delegate == null) {
      this.delegate = new AdminRoleVO(new oracle.iam.platform.authopss.vo.AdminRole(name(), displayName(), description(), isScoped()));
      this.delegate.setAdminRoleCapabilities(new ArrayList<Capability>());
      this.delegate.setAdminRolePublication(new ArrayList<EntityPublication>());
      this.delegate.setAdminRoleRuleScopes(new ArrayList<AdminRoleRuleScope>());
      this.delegate.setUserAssignedStatic(new ArrayList<String>());
    }
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identity
  /**
   ** Set the Admin Role wrapper to use by this instance.
   **
   ** @param  delegate           the {@link  AdminRole}
   **                            to use by this instance.
   */
  public void identity(final AdminRoleVO delegate) {
    this.delegate = delegate;
  }
}