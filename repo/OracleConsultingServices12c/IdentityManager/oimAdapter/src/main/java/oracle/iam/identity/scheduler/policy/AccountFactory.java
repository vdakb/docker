/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtual Resource Management

    File        :   AccountFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractTask;

import oracle.iam.identity.foundation.naming.User;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.Account;

////////////////////////////////////////////////////////////////////////////////
// abstract class AccountFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A factory to create <code>User Account</code>s and their instance
 ** relationships.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class AccountFactory extends Factory {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Loads the <code>User Object</code> from Oracle Identity Manager
   **
   ** @param  task               the {@link AbstractTask} which request to
   **                            instantiated the wrapper.
   ** @param  userName           the name of the <code>User Object</code> to
   **                            load.
   **
   ** @return                    an <code>Account</code> which is associated
   **                            with the specified login name.
   **
   ** @throws TaskException      if the operation fails.
   */
  public static final Account createAccount(final AbstractTask task, final String userName)
    throws TaskException {

    final String method = "createAccount";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_IDENTITY);
    parameter[1] = userName;
    task.debug(method, TaskBundle.format(TaskMessage.NAME_TORESOLVE, parameter));

    String  attribute = null;
    Account account   = null;

    // create the filter to restrict the result set to retrieve
    Map<String, String> filter = new HashMap<String, String>();
    filter.put(User.USERID, parameter[1]);
    try {
      final tcResultSet resultSet = task.userFacade().findUsers(filter);
      final int         rowCount  = resultSet.getRowCount();
      if (rowCount == 0)
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);

      attribute = User.KEY;
      account = createAccount(resultSet.getLongValue(attribute), parameter[1]);

      parameter[2] = resultSet.getStringValue(attribute);
      task.debug(method, TaskBundle.format(TaskMessage.NAME_RESOLVED, parameter));
    }
    catch (tcColumnNotFoundException e) {
      throw new ResolverException(TaskError.COLUMN_NOT_FOUND, attribute);
    }
    catch (tcAPIException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
    return account;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Loads the <code>User Object</code> from Oracle Identity Manager
   **
   ** @param  task               the {@link AbstractTask} which request to
   **                            instantiated the wrapper.
   ** @param  userName           the name of the <code>User Object</code> to
   **                            load.
   ** @param  profileAttribute   the list of fields that has to be added to the
   **                            created <code>User Object</code> as profile
   **                            data from the returned {@link tcResultSet}.
   **                            <p>
   **                            The attributes returned in the
   **                            {@link tcResultSet} can include user-defined
   **                            fields as well as any of the following metadata
   **                            codes:
   **                            <ul>
   **                              <li>Organizations.Key
   **                              <li>Organizations.Organization Name
   **                              <li>Users.AD Reference
   **                              <li>Users.Creation Date
   **                              <li>Users.Deprovisioned Date
   **                              <li>Users.Deprovisioning Date
   **                              <li>Users.Disable User
   **                              <li>Users.End Date
   **                              <li>Users.First Name
   **                              <li>Users.Identity
   **                              <li>Users.Key
   **                              <li>Users.Last Name
   **                              <li>Users.Manager Key
   **                              <li>Users.Manager Login
   **                              <li>Users.Middle Name
   **                              <li>Users.Note
   **                              <li>Users.Provisioned Date
   **                              <li>Users.Provisioning Date
   **                              <li>Users.Role
   **                              <li>Users.Row Version
   **                              <li>Users.Start Date
   **                              <li>Users.Status
   **                              <li>Users.Update Date
   **                              <li>Users.Updated By
   **                              <li>Users.User ID
   **                              <li>Users.Xellerate Type
   **                            </ul>
   **
   ** @return                    an <code>Account</code> which is associated
   **                            with the specified login name and the profile
   **                            attributes.
   **
   ** @throws TaskException      if the operation fails
   */
  public static final Account createAccount(final AbstractTask task, final String userName, final String[] profileAttribute)
    throws TaskException {

    final String method = "createAccount";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_IDENTITY);
    parameter[1] = userName;
    task.debug(method, TaskBundle.format(TaskMessage.NAME_TORESOLVE, parameter));

    String  attribute = null;
    Account account   = null;

    // create the filter to restrict the result set to retrieve
    Map<String, String> filter = new HashMap<String, String>();
    filter.put(User.USERID, parameter[1]);
    try {
      final tcResultSet resultSet = task.userFacade().findUsersFiltered(filter, profileAttribute);
      final int         rowCount  = resultSet.getRowCount();
      if (rowCount == 0)
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);

      attribute = User.KEY;
      account   = createAccount(resultSet.getLongValue(attribute), parameter[1]);
      for (int i = 0; i < profileAttribute.length; i++) {
        attribute = profileAttribute[i];
        if (User.KEY.equals(attribute))
          continue;

        account.addAttribute(attribute, resultSet.getStringValue(attribute));
      }

      attribute = User.USERID;
      parameter[2] = resultSet.getStringValue(attribute);
      task.debug(method, TaskBundle.format(TaskMessage.NAME_RESOLVED, parameter));
    }
    catch (tcColumnNotFoundException e) {
      throw new ResolverException(TaskError.COLUMN_NOT_FOUND, attribute);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
    return account;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Loads the <code>User Object</code> from Oracle Identity Manager
   **
   ** @param  task               the {@link AbstractTask} which request to
   **                            instantiated the wrapper.
   ** @param  userKey            the internal system identifier of the
   **                            <code>User Object</code> to load.
   **
   ** @return                    an <code>Account</code> which is associated
   **                            with the specified user system identifier.
   **
   ** @throws TaskException      if the operation fails.
   */
  public static final Account createAccount(final AbstractTask task, final long userKey)
    throws TaskException {

    final String method = "createAccount";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_IDENTITY);
    parameter[1] = Long.toString(userKey);
    task.debug(method, TaskBundle.format(TaskMessage.KEY_TORESOLVE, parameter));

    String  attribute = null;
    Account account   = null;

    // create the filter to restrict the result set to retrieve
    Map<String, String> filter = new HashMap<String, String>();
    filter.put(User.KEY, parameter[1]);
    try {
      final tcResultSet resultSet = task.userFacade().findUsers(filter);
      final int         rowCount  = resultSet.getRowCount();
      if (rowCount == 0)
        throw new ResolverException(TaskError.RESOURCE_NOT_FOUND, parameter);

      attribute    = User.USERID;
      parameter[2] = resultSet.getStringValue(attribute);
      account = createAccount(userKey, parameter[2]);

      task.debug(method, TaskBundle.format(TaskMessage.KEY_RESOLVED, parameter));
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, attribute);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
    return account;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Loads the <code>User Object</code> from Oracle Identity Manager
   **
   ** @param  task               the {@link AbstractTask} which request to
   **                            instantiated the wrapper.
   ** @param  userKey            the internal system identifier of the
   **                            <code>User Object</code> to load.
   ** @param  profileAttribute   the list of fields that has to be added to the
   **                            created <code>User Object</code> as profile
   **                            data from the returned {@link tcResultSet}.
   **                            <p>
   **                            The attributes returned in the
   **                            {@link tcResultSet} can include user-defined
   **                            fields as well as any of the following metadata
   **                            codes:
   **                            <ul>
   **                              <li>Organizations.Key
   **                              <li>Organizations.Organization Name
   **                              <li>Users.AD Reference
   **                              <li>Users.Creation Date
   **                              <li>Users.Deprovisioned Date
   **                              <li>Users.Deprovisioning Date
   **                              <li>Users.Disable User
   **                              <li>Users.End Date
   **                              <li>Users.First Name
   **                              <li>Users.Identity
   **                              <li>Users.Key
   **                              <li>Users.Last Name
   **                              <li>Users.Manager Key
   **                              <li>Users.Manager Login
   **                              <li>Users.Middle Name
   **                              <li>Users.Note
   **                              <li>Users.Provisioned Date
   **                              <li>Users.Provisioning Date
   **                              <li>Users.Role
   **                              <li>Users.Row Version
   **                              <li>Users.Start Date
   **                              <li>Users.Status
   **                              <li>Users.Update Date
   **                              <li>Users.Updated By
   **                              <li>Users.User ID
   **                              <li>Users.Xellerate Type
   **                            </ul>
   **
   ** @return                    an <code>Account</code> which is associated
   **                            with the specified user system identifier and
   **                            the profile attributes.
   **
   ** @throws TaskException      if the operation fails.
   */
  public static final Account createAccount(final AbstractTask task, final long userKey, final String[] profileAttribute)
    throws TaskException {

    final String method = "createAccount";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_IDENTITY);
    parameter[1] = Long.toString(userKey);
    task.debug(method, TaskBundle.format(TaskMessage.KEY_TORESOLVE, parameter));

    String  attribute = null;
    Account account   = null;

    // create the filter to restrict the result set to retrieve
    Map<String, String> filter = new HashMap<String, String>();
    filter.put(User.KEY, parameter[1]);
    try {
      final tcResultSet resultSet = task.userFacade().findUsersFiltered(filter, profileAttribute);
      final int         rowCount  = resultSet.getRowCount();
      if (rowCount == 0)
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);

      attribute = User.USERID;
      parameter[2] = resultSet.getStringValue(attribute);
      account = createAccount(userKey, parameter[2]);
      for (int i = 0; i < profileAttribute.length; i++) {
        attribute = profileAttribute[i];
        if (User.USERID.equals(attribute))
          continue;

        account.addAttribute(attribute, resultSet.getStringValue(attribute));
      }

      task.debug(method, TaskBundle.format(TaskMessage.KEY_RESOLVED, parameter));
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, attribute);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
    return account;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Creates the <code>Account</code> from Oracle Identity Manager
   **
   ** @param  userKey            the internal system identifier of the
   **                            <code>User Account</code> to load.
   ** @param  userName           the name of the <code>User Account</code>.
   **
   ** @return                    an <code>Account</code> which is associated
   **                            with the specified user system identifier and
   **                            login name.
   */
  public static final Account createAccount(final long userKey, final String userName) {
    return new Account(userKey, userName);
  }
}