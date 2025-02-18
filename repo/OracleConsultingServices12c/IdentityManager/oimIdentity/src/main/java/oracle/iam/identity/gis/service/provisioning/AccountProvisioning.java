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

    File        :   AccountProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-06-21  DSteding    First release version
    3.1.1.0      2014-10-16  JBandeira   Fix scope permission modification
*/

package oracle.iam.identity.gis.service.provisioning;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.platform.authopss.vo.AdminRole;

import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.gis.resource.ProvisioningBundle;

////////////////////////////////////////////////////////////////////////////////
// class AccountProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountProvisioning</code> acts as the service end point for the
 ** Oracle Identity Manager to provision accounts to Identity Manager itself.
 ** <br>
 ** This is wrapper class has methods for account operations like create
 ** account, modify account, delete account etc.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class AccountProvisioning extends Provisioning {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private User         identity;
  private Organization scope;
  private AdminRole    permission;
  private Role         role;
  private boolean      delegated;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountProvisioning</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  processTaskName    the name of the process task to pass to this
   **                            adapter implementation for debugging purpose.
   */
  public AccountProvisioning(final tcDataProvider provider, final String processTaskName) {
    // ensure inheritance
    super(provider, processTaskName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessData
  /**
   ** Lookup the specified identity by its login name and returns
   ** <code>SUCCESS</code> if the identity exists.
   ** <p>
   ** The internal systemidentifier is stored internally for further processing
   ** of the identity.
   **
   ** @param  identity           the login name of an identity to lookup.
   **
   ** @return                    <code>SUCCESS</code> if the identity exists;
   **                            otherwise an appropriate error code.
   */
  public String createProcessData(final String identity) {
    final String method = "createProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (StringUtility.isEmpty(identity)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
          this.identity = this.server.lookupIdentity(identity);
      }
      catch (TaskException e) {
        // when the operation fails return static error code as the response
        responseCode = e.code();
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPermissionData
  /**
   ** Lookup the specified identity by its login name and returns
   ** <code>SUCCESS</code> if the identity exists.
   ** <p>
   ** The internal systemidentifier is stored internally for further processing
   ** of the identity.
   **
   ** @param  identity           the login name of an identity to lookup.
   ** @param  permission         the name of a permission to lookup.
   **
   ** @return                    <code>SUCCESS</code> if the identity exists;
   **                            otherwise an appropriate error code.
   */
  public String createPermissionData(final String identity, final String permission) {
    final String method = "createPermissionData";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (StringUtility.isEmpty(identity)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (StringUtility.isEmpty(permission)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "permission"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        this.identity = this.server.lookupIdentity(identity);
        this.role     = this.server.lookupRole(permission);
      }
      catch (TaskException e) {
        // when the operation fails return static error code as the response
        responseCode = e.code();
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPermissionData
  /**
   ** Lookup the specified identity by its login name and returns the internal
   ** system identifier of this identity.
   **
   ** @param  identity           the login name of an identity to lookup.
   ** @param  permission         the name of a permission to lookup.
   ** @param  scope              the organization scope of the permission to
   **                            lookup.
   **
   ** @return                    the internal system identifier of the identity.
   **                            If <code>null</code> is returned the identity
   **                            does not exists.
   */
  public String createPermissionData(final String identity, final String permission, final String scope) {
    final String method = "createPermissionData";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (StringUtility.isEmpty(identity)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else if (StringUtility.isEmpty(permission)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "permission"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else if (StringUtility.isEmpty(scope)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "scope"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        this.identity   = this.server.lookupIdentity(identity);
        this.permission = this.server.lookupPermission(permission);
        this.scope      = this.server.lookupScope(scope);
      }
      catch (TaskException e) {
        // when the operation fails return static error code as the response
        responseCode = e.code();
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPermissionData
  /**
   ** Lookup the specified identity by its login name and returns the internal
   ** system identifier of this identity.
   **
   ** @param  identity           the login name of an identity to lookup.
   ** @param  permission         the name of a permission to lookup.
   ** @param  scope              the organization scope of the permission to
   **                            lookup.
   ** @param  delegated          <code>true</code> if the permission should be
   **                            assigned delegated within in hierarchy;
   **                            otherwise <code>false</code>.
   **
   ** @return                    the internal system identifier of the identity.
   **                            If <code>null</code> is returned the identity
   **                            does not exists.
   */
  public String createPermissionData(final String identity, final String permission, final String scope, final boolean delegated) {
    final String method = "createPermissionData";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (StringUtility.isEmpty(identity)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else if (StringUtility.isEmpty(permission)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "permission"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else if (StringUtility.isEmpty(scope)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "scope"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        this.identity   = this.server.lookupIdentity(identity);
        this.permission = this.server.lookupPermission(permission);
        this.scope      = this.server.lookupScope(scope);
        this.delegated  = delegated;
      }
      catch (TaskException e) {
        // when the operation fails return static error code as the response
        responseCode = e.code();
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an Identity Manager account.
   **
   ** @return                    <code>SUCCESS</code> if the account was
   **                            created; otherwise the coded of the catched
   **                            exception.
   */
  public final String create() {
    final String method = "create";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (this.identity == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an Identity Manager account.
   **
   ** @return                    <code>SUCCESS</code> if the account was
   **                            dropped; otherwise the coded of the catched
   **                            exception.
   */
  public String delete() {
    final String method = "delete";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (this.identity == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        this.server.accountDelete(this.identity);
        responseCode = SUCCESS;
      }
      catch (TaskException e) {
        // when the operation fails in general return the error code as the
        // response
        responseCode = e.code();
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an Identity Manager account.
   **
   ** @return                    <code>SUCCESS</code> if the account was
   **                            enabled; otherwise the coded of the catched
   **                            exception.
   */
  public final String enable() {
    final String method = "enable";
    trace(method, SystemMessage.METHOD_ENTRY);
    trace(method, SystemMessage.METHOD_EXIT);
    return SUCCESS;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an Identity Manager account.
   **
   ** @return                    <code>SUCCESS</code> if the account was
   **                            enabled; otherwise the coded of the catched
   **                            exception.
   */
  public final String disable() {
    final String method = "disable";
    trace(method, SystemMessage.METHOD_ENTRY);
    trace(method, SystemMessage.METHOD_EXIT);
    return SUCCESS;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignSystemPermission
  /**
   ** Assigns an <code>Account</code> to an administrative role in Identity
   ** Manager.
   **
   ** @return                    an appropriate response message
   */
  public String assignSystemPermission() {
    final String method = "assignSystemPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (this.identity == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (this.role == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "role"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        this.server.accountPermissionAssign(this.identity, this.role);
      }
      catch (TaskException e) {
        // when the operation fails in general return the error code as the
        // response
        responseCode = e.code();
      }
      catch (Exception e) {
        // push the stacktrace to the log
        fatal(method, e);
        // when the operation fails return static error code as the response
        responseCode = TaskError.UNHANDLED;
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeSystemPermission
  /**
   ** Revokes an <code>Account</code> from an administrative role in Identity
   ** Manager.
   **
   ** @return                    an appropriate response message
   */
  public String revokeSystemPermission() {
    final String method = "revokeSystemPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (this.identity == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (this.role == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "role"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        this.server.accountPermissionRevoke(this.identity, this.role);
      }
      catch (TaskException e) {
        // when the operation fails in general return the error code as the
        // response
        responseCode = e.code();
      }
      catch (Exception e) {
        // push the stacktrace to the log
        fatal(method, e);
        // when the operation fails return static error code as the response
        responseCode = TaskError.UNHANDLED;
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignAdminPermission
  /**
   ** Assigns an <code>Account</code> to an administrative permission
   ** (AdminRole) in Identity Manager.
   **
   ** @param  delegated          <code>true</code> if the permission should be
   **                            assigned delegated within in hierarchy;
   **                            otherwise <code>false</code>.
   **
   ** @return                    an appropriate response message
   */
  public String assignAdminPermission(final boolean delegated) {
    final String method = "assignAdminPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (this.identity == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (this.permission == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "permission"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (this.scope == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "scope"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        if (this.server.accountPermissionMember(this.identity, this.permission, this.scope)) {
          error(method, ProvisioningBundle.format(ProvisioningError.PERMISSION_ALREADY_GRANTED, this.permission.getRoleName(), this.identity.getLogin()));
          responseCode = ProvisioningError.PERMISSION_ALREADY_GRANTED;
        }
        else {
          this.server.accountPermissionAssign(this.identity, this.permission, this.scope, delegated);
        }
      }
      catch (TaskException e) {
        // when the operation fails in general return the error code as the
        // response
        responseCode = e.code();
      }
      catch (Exception e) {
        // push the stacktrace to the log
        fatal(method, e);
        // when the operation fails return static error code as the response
        responseCode = TaskError.UNHANDLED;
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeAdminPermission
  /**
   ** Revokes an <code>Account</code> from an administrative permission
   ** (AdminRole) in Identity Manager.
   **
   ** @return                    an appropriate response message
   */
  public String revokeAdminPermission() {
    final String method = "revokeAdminPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // check if all required information are available
    if (this.identity == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (this.permission == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "permission"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (this.scope == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "scope"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        if (!this.server.accountPermissionMember(this.identity, this.permission, this.scope)) {
          error(method, ProvisioningBundle.format(ProvisioningError.PERMISSION_ALREADY_REVOKED, this.permission.getRoleName(), this.identity.getLogin()));
          responseCode = ProvisioningError.PERMISSION_ALREADY_REVOKED;
        }
        else {
          this.server.accountPermissionRevoke(this.identity, this.permission, this.scope);
        }
      }
      catch (TaskException e) {
        // when the operation fails in general return the error code as the
        // response
        responseCode = e.code();
      }
      catch (Exception e) {
        // push the stacktrace to the log
        fatal(method, e);
        // when the operation fails return static error code as the response
        responseCode = TaskError.UNHANDLED;
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAdminPermission
  /**
   ** Assigns an <code>Account</code> to a role in Identity Manager.
   **
   ** @param  originPermission   the old permission
   ** @param  originScope        the old organization scope of the permission
   ** @param  originDelegated    the old delegated value
   **
   ** @return                    an appropriate response message
   */
  public String modifyAdminPermission(final String originPermission, final String originScope, final boolean originDelegated) {
    final String method = "modifyAdminPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;

    // check if all required information are available
    if (this.identity == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (this.permission == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "permission"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (this.scope == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "scope"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (StringUtility.isEmpty(originPermission)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "originPermission"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (StringUtility.isEmpty(originScope)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "originScope"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        final Organization existingScope      = this.server.lookupScope(originScope);
        final AdminRole    existingPermission = this.server.lookupPermission(originPermission);
        //final Organization existing = this.server.lookupScope(originScope);
        // check if all required information is available
        if (existingScope == null) {
          error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "originScope"));
          responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
        }
        else if (existingPermission == null) {
          error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "originPermission"));
          responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
        }
        else if (!this.server.accountPermissionMember(this.identity, existingPermission, existingScope)) {
          error(method, ProvisioningBundle.format(ProvisioningError.PERMISSION_ALREADY_REVOKED, existingPermission.getRoleName(), this.identity.getLogin()));
          responseCode = ProvisioningError.PERMISSION_ALREADY_REVOKED;
        } //scope.getEntityId().equals(grantedScope)
        else if (this.server.accountPermissionMember(this.identity, this.permission, this.scope) && (String.valueOf(originDelegated).equals(String.valueOf(this.delegated)))) {
          error(method, ProvisioningBundle.format(ProvisioningError.PERMISSION_ALREADY_GRANTED, this.permission.getRoleName(), this.identity.getLogin()));
          responseCode = ProvisioningError.PERMISSION_ALREADY_GRANTED;
        }
        else {
          //If The role changed, we need to revoke it first and then provision the new.
          if(!String.valueOf(this.permission.getRoleName()).equals(String.valueOf(existingPermission.getRoleName()))){
              final AdminRole newPermission = this.permission;
              final Organization newScope   = this.scope;
              this.permission               = existingPermission;
              this.scope                    = existingScope;
              final String revoked          = revokeAdminPermission();

              if(revoked.equals("SUCCESS")){
                  this.permission       = newPermission;
                  this.scope            = newScope;
                  responseCode = assignAdminPermission(this.delegated);
                }
              else {
                responseCode = revoked;
              }
          }
          else {
            this.server.accountPermissionUpdate(this.identity, this.permission, existingScope, this.scope, this.delegated);
          }
        }
      }
      catch (TaskException e) {
        // when the operation fails in general return the error code as the
        // response
        responseCode = e.code();
      }
      catch (Exception e) {
        // push the stacktrace to the log
        fatal(method, e);
        // when the operation fails return static error code as the response
        responseCode = TaskError.UNHANDLED;
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAdminPermission
  /**
   ** Assigns an <code>Account</code> to a role in Identity Manager.
   **
   ** @param  originRole        the old role
   **
   ** @return                    an appropriate response message
   */
  public String modifySystemPermission(final String originRole) {
    final String method = "modifySystemPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;

    // check if all required information are available
    if (this.identity == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "identity"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (this.role == null) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "role"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    // check if all required information are available
    else if (StringUtility.isEmpty(originRole)) {
      error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "originRole"));
      responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
    }
    else {
      try {
        final Role existingRole = this.server.lookupRole(originRole);

        // check if all required information is available
        if (existingRole == null) {
          error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "originRole"));
          responseCode = ProvisioningError.INSUFFICIENT_INFORMATION;
        }
        else if (!this.server.accountSystemMember(this.identity, existingRole)) {
          error(method, ProvisioningBundle.format(ProvisioningError.PERMISSION_ALREADY_REVOKED, existingRole.getName(), this.identity.getLogin()));
          responseCode = ProvisioningError.PERMISSION_ALREADY_REVOKED;
        } //scope.getEntityId().equals(grantedScope)
        else if (this.server.accountSystemMember(this.identity, this.role)) {
          error(method, ProvisioningBundle.format(ProvisioningError.PERMISSION_ALREADY_GRANTED, this.role.getName(), this.identity.getLogin()));
          responseCode = ProvisioningError.PERMISSION_ALREADY_GRANTED;
        }
        else {
          final Role  newRole     = this.role;
          this.role               = existingRole;
          final String revoked    = revokeSystemPermission();

          if(revoked.equals("SUCCESS")){
              this.role       = newRole;
              responseCode    = assignSystemPermission();
            }
          else {
            responseCode = revoked;
          }
        }
      }
      catch (TaskException e) {
        // when the operation fails in general return the error code as the
        // response
        responseCode = e.code();
      }
      catch (Exception e) {
        // push the stacktrace to the log
        fatal(method, e);
        // when the operation fails return static error code as the response
        responseCode = TaskError.UNHANDLED;
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }
}