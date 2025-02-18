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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Database Account Connector

    File        :   AccountProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.service.provisioning;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.dbs.persistence.Administration;

////////////////////////////////////////////////////////////////////////////////
// class AccountProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountProvisioning</code> acts as the service end point for the
 ** Oracle Identity Manager to provision account properties to a Database.
 ** <br>
 ** This is wrapper class has methods for account operations like create account,
 ** modify account, delete account etc.
 ** <br>
 ** This class internally calls {@link Administration} to talk to the target
 ** database and returns appropriate message code.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AccountProvisioning extends Provisioning {

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountProvisioning</code> task adpater
   ** that allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public AccountProvisioning(final tcDataProvider provider)
    throws TaskException {

    // ensure inheritance
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Creates a database account with the specified password.
   ** <p>
   ** The method expects that before it's invoked the process data mapping
   ** exists and the connection to the target system is established. It enforce
   ** only that the arguments are placed in the process data mapping.
   ** <p>
   ** This method will not make any intervention to check if the requirements
   ** stated above are met.
   ** <p>
   ** The proccess data mapping has to pass at least two entries and exactly in
   ** the order below:
   ** <ol>
   **   <li>USERNAME has to be mapped by the attribute that represents the
   **                name of the account to create; either the native field
   **                name or its label.
   **   <li>PASSWORD has to be mapped by the attribute that represents the
   **                password of the account to create; either the native field
   **                name or its label.
   ** </ol>
   **
   ** @return                    <code>SUCCESS</code> if the account was
   **                            created; otherwise the coded of the catched
   **                            exception.
   */
  public final String createAccount() {
    final String method = "createAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the arbiter
      this.connection.accountCreate(this.data());
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteAccount
  /**
   ** Drops a database account.
   **
   ** @param  username           the name of the account to drop
   **
   ** @return                    <code>SUCCESS</code> if the account was
   **                            dropped; otherwise the coded of the catched
   **                            exception.
   */
  public String deleteAccount(final String username) {
    final String method = "deleteAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the arbiter
      this.connection.accountDelete(username);
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAccount
  /**
   ** Modifies an attribute of an existing account in an Oracle Database.
   **
   ** @param  username           the name of the user to modify.
   ** @param  attributeName      the name of the attribute to modify.
   ** @param  attributeValue     the value the atribute should have after modify.
   **
   ** @return                    <code>SUCCESS</code> if the account was
   **                            modifiedt; otherwise the coded of the catched
   **                            exception.
   */
  public String modifyAccount(final String username, final String attributeName, final String attributeValue) {
    final String method = "modifyAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the arbiter
      this.connection.accountModify(username, attributeName, attributeValue);
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changePassword
  /**
   ** Change the password for the specified user.
   **
   ** @param  username           the name of the user to change the password
   ** @param  password           the password to set.
   **
   ** @return                    <code>SUCCESS</code> if the password was set;
   **                            otherwise the coded of the catched
   **                            exception.
   */
  public String changePassword(final String username, final String password) {
    final String method = "changePassword";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the arbiter
      this.connection.accountPassword(username, password);
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deactivateAccount
  /**
   ** Deactivating aa account by locking the account.
   **
   ** @param  username           the name of the account to lock
   **
   ** @return                    <code>SUCCESS</code> account was locked;
   **                            otherwise the coded of the catched
   **                            exception.
   */
  public String deactivateAccount(final String username) {
    final String method = "deactivateAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the arbiter
      this.connection.accountDisable(username);
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   activateAccount
  /**
   ** Activating a locked account.
   **
   ** @param  username           the name of the account to unlock
   **
   ** @return                    <code>SUCCESS</code> if account was activated;
   **                            otherwise the coded of the catched
   **                            exception.
   */
  public String activateAccount(final String username) {
    final String method = "activateAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the arbiter
      this.connection.accountEnable(username);
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grantSystemPermission
  /**
   ** Grants the passed system permission (role or permission) to the
   ** specified user.
   **
   ** @param  username           the name of the user where the specified
   **                            permission should be granted for.
   ** @param  permission         the permission to grant.
   **                            A permission can be a Role, Profile or a system
   **                            privilege like <code>CREATE SESSION</code> etc.
   ** @param  delegated          the permission should be granted with
   **                            delegated administration privileges.
   **
   ** @return                    <code>SUCCESS</code> if the permission was
   **                            granted; otherwise the coded of the catched
   **                            exception.
   */
  public String grantSystemPermission(final String username, final String permission, final String delegated) {
    final String method = "grantSystemPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      this.connection.accountGrantPrivilege(username, permission, SystemConstant.YES.equals(delegated));
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeSystemPermission
  /**
   ** Revokes the passed system permission (role or permission) from the
   ** specified user.
   **
   ** @param  username           the name of the user where the specified
   **                            permission should be revoked from.
   ** @param  permission         the permission to revoke.
   **                            A permission can be a Role, Profile or a system
   **                            privilege like <code>CREATE SESSION</code> etc.
   **
   ** @return                    <code>SUCCESS</code> if the permission was
   **                            revoked; otherwise the coded of the catched
   **                            exception.
   */
  public String revokeSystemPermission(final String username, final String permission) {
    final String method = "revokeSystemPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the arbiter
      this.connection.accountRevokePrivilege(username, permission);
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grantObjectPermission
  /**
   ** Grants the specified permission (select, delete, etc) on the specified
   ** object (table, view, etc.) to the specified username.
   **
   ** @param  username           the name of the user where the specified
   **                            permission should be granted for.
   ** @param  object             the name of the object to which the permission
   **                            should be granted.
   **                            An object can be a name of a table, view etc.
   ** @param  permission         the permission to grant.
   **                            A permission can be a SELECT, DELETE etc.
   ** @param  delegated          the permission should be granted with
   **                            delegated administration privileges.
   **
   ** @return                    <code>SUCCESS</code> if the permission was
   **                            granted; otherwise the coded of the catched
   **                            exception.
   */
  public String grantObjectPermission(final String username, final String object, final String permission, final Boolean delegated) {
    final String method = "grantObjectPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the arbiter
      this.connection.accountGrantObject(username, object, permission, delegated.booleanValue());
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeObjectPermission
  /**
   ** Revokes the specified permission (select, delete, etc) on the specified
   ** object (table, view, etc.) from the specified username.
   **
   ** @param  username           the name of the user where the specified
   **                            permission should be granted for.
   ** @param  permission         the permission to grant.
   **                            A permission can be a SELECT, DELETE etc.
   ** @param  object             the name of the object to which the permission
   **                            should be granted.
   **                            An object can be a name of a table, view etc.
   **
   ** @return                    <code>SUCCESS</code> if the permission was
   **                            granted; otherwise the coded of the catched
   **                            exception.
   */
  public String revokeObjectPermission(final String username, final String permission, final String object) {
    final String method = "revokeObjectPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the arbiter
      this.connection.accountRevokeObject(username, permission, object);
      return SUCCESS;
    }
    catch (TaskException e) {
      // regardless whether it will be part of the adpater implementation or not
      // in case of an error we need to release the connection
      this.connection.disconnect();
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}