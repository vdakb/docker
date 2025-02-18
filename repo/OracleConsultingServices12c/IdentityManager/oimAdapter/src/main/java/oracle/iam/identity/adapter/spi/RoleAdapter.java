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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   RoleAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.EntityAdapter;

import oracle.iam.identity.foundation.naming.User;
import oracle.iam.identity.foundation.naming.Group;
import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.adapter.AdapterMessage;
import oracle.iam.identity.adapter.AdapterBundle;

////////////////////////////////////////////////////////////////////////////////
// class RoleAdapter
// ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>RoleAdapter</code> act as the service end point for the Oracle
 ** Identity Manager to provision Oracle Identity Manager Groups to an
 ** Oracle Identity Manager User.
 ** <br>
 ** This is wrapper class has methods for account operations like add user to
 ** roles and remove user form roles.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class RoleAdapter extends EntityAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String LOGGER_CATEGORY     = "OCS.ROLE.PROVISIONING";

  static final String KEYTYPE             = "Role";
  static final String MISMATCH            = "MISMATCH";
  static final String APPROVE             = "Approve";
  static final String REJECT              = "Reject";

  static final String REQUESTKEY          = "Requests.Key";
  static final String REQUESTDATA         = "Requests.Consolidated Data Value";
  static final String REQUESTUSER         = "UserLogin";
  static final String PROCESSINSTANCEKEY  = "Process Instance.Key";
  static final String PROCESSINSTANCENAME = "Process Instance.Name";

  static final int    ACTION_GRANT        = 1;
  static final int    ACTION_REVOKE       = 2;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the group to add the user to */
  private String             roleName        = null;

  /** the identifier of the user */
  private String             userLogin        = null;

  /** the internal identifier of the user */
  private long               userKey          = -1L;

  /** the internal identifier of the group */
  private String             roleKey          = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Manager</code> task adpater
   ** that allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public RoleAdapter(tcDataProvider provider) {
    // ensure inheritance
    super(provider, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkStatus
  /**
   ** Check whether user and group is resolved
   **
   ** @return                    the adapter status
   */
  public boolean checkStatus() {
    if ((this.userKey == -1L) || (this.roleKey.trim().equalsIgnoreCase(SystemConstant.EMPTY)))
      return false;
    else
      return true;
  }

  // End of E. Lapi changes on 02.04.2009

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isMember
  /**
   ** Check if the user is member of the specified group.
   **
   ** @return                    <code>true</code> if the specified user is
   **                            member of the group.
   */
  public boolean isMember() {
    return isMember(Long.parseLong(this.roleKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isMember
  /**
   ** Check if the user is member of the specified group.
   **
   ** @param  groupKey           the internal identfier of the group that has to
   **                            be verified.
   ** @param  userKey            the internal identfier of the user that has to
   **                            be verified.
   **
   ** @return                    <code>true</code> if the specified user is
   **                            member of the group.
   */
  public boolean isMember(long groupKey, long userKey) {
    final String method = "isMember";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean response = false;
    try {
      // check if user is member of the group
      response = groupFacade().isUserGroupMember(groupKey, userKey);
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addToGroup
  /**
   ** Adds the specified user to the specified group.
   **
   ** @return                    <code>true</code> if the specified user was
   **                            succesfully added to the specified group.
   */
  public boolean assignToGroup() {
    final String method = "addToGroup";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean response = false;
    try {
      if (!StringUtility.isEmpty(this.roleKey)) {
        String[] parameter = { this.roleName, this.userLogin };

        // add user to the group
        debug(method, AdapterBundle.format(AdapterMessage.ADD_MEMBER, parameter));
        assignMember(Long.parseLong(this.roleKey), this.userKey);
        debug(method, AdapterBundle.format(AdapterMessage.ADDED_MEMBER, parameter));

        // if we get here, then hopefully it's worked
        response = true;
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignMember
  /**
   ** Adds the specified user to the specified group.
   **
   ** @param  groupKey           the internal identfier of the group that has to
   **                            be assigned
   ** @param  userKey            the internal identfier of the user that has to
   **                            be assigned
   **
   ** @return                    <code>true</code> if the specified user was
   **                            succesfully added to the specified group.
   */
  public boolean assignMember(final long groupKey, final long userKey) {
    return this.assignMember(groupKey, userKey, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignMember
  /**
   ** Adds the specified user to the specified group.
   **
   ** @param  groupKey           the internal identfier of the group that has to
   **                            be assigned
   ** @param  userKey            the internal identfier of the user that has to
   **                            be assigned
   ** @param  evaluatePolicies   indicate whether to evaluate policies or not
   **                            when user is added to the group
   **
   ** @return                    <code>true</code> if the specified user was
   **                            succesfully added to the specified group.
   */
  public boolean assignMember(final long groupKey, final long userKey, final boolean evaluatePolicies) {
    final String method = "assignMember";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      // add user to the group
      groupFacade().addMemberUser(groupKey, userKey, evaluatePolicies);
      return true;
    }
    catch (Exception e) {
      fatal(method, e);
      return false;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeMember
  /**
   ** Removes the specified user from the specified group.
   **
   ** @return                    <code>true</code> if the specified user was
   **                            succesfully removed from the specified group.
   */
  public boolean removeMember() {
    final String method = "removeMember";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean response = false;
    try {
      if (!StringUtility.isEmpty(this.roleKey)) {
        String[] parameter = { this.roleName, this.userLogin };

        // remove user from the group
        debug(method, AdapterBundle.format(AdapterMessage.REMOVE_MEMBER, parameter));
        removeMember(Long.parseLong(this.roleKey), this.userKey);
        debug(method, AdapterBundle.format(AdapterMessage.REMOVED_MEMBER, parameter));
        // if we get here, then hopefully it's worked
        response = true;
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeMember
  /**
   ** Removes the specified user from the specified group.
   **
   ** @param  roleKey            the internal identfier of the group that has to
   **                            be removed from the user
   ** @param  userKey            the internal identfier of the user that has to
   **                            be removed from the group
   **
   ** @return                    <code>true</code> if the specified user was
   **                            succesfully removed from the specified group.
   */
  public boolean removeMember(final long roleKey, final long userKey) {
    return removeMember(roleKey, userKey, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeMember
  /**
   ** Removes the specified user from the specified group.
   **
   ** @param  roleKey            the internal identfier of the group that has to
   **                            be removed from the user
   ** @param  userKey            the internal identfier of the user that has to
   **                            be removed from the group
   ** @param  evaluatePolicies   indicate whether to evaluate policies or not
   **                            when user is removed from the group
   **
   ** @return                    <code>true</code> if the specified user was
   **                            succesfully removed from the specified group.
   */
  public boolean removeMember(final long roleKey, final long userKey, final boolean evaluatePolicies) {
    final String method = "removeFromGroup";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      // remove user from the group
      groupFacade().removeMemberUser(roleKey, userKey);
      // if we get here, then hopefully it's worked
      return true;
    }
    catch (Exception e) {
      fatal(method, e);
      return false;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getKeyType
  /**
   ** Returns the type of a internal object used for a task assignment
   **
   ** @return                    returns always <code>Group</code>
   */
  public String getKeyType() {
    return KEYTYPE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getKey
  /**
   ** Returns the internal key for the group where the approval will be assign
   ** to in the task assignment.
   ** <br>
   ** The key is evaluated by the Constructor. The Lookup Definition passed to
   ** the Constructor contains the approval group per key.
   **
   ** @return                    the internal key for the group where the
   **                            approval will be assign to
   */
  public String getKey() {
    return this.roleKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkRoleConflicts
  /**
   ** Check if the current user has any conflicts in the role assigment.
   **
   ** @param groupName           the names of the group which is subject of
   **                            provisioning.
   ** @param lookupDefinition    the Lookup Definition which contains the roles
   **                            that are not allowed to have.
   **
   ** @return                     the string signaling if there are conflicts or
   **                             not; either
   **                             <ul>
   **                               <li>{@link #APPROVE} if no conflicts
   **                                   detected.
   **                               <li>{@link #REJECT} if there are conflicts
   **                                   detected.
   **                             </ul>
   */
  public String checkRoleConflicts(String groupName, String lookupDefinition) {
    final String method = "checkRoleConflicts";
    trace(method, SystemMessage.METHOD_ENTRY);

    String      response    = FAILURE;
    tcResultSet groups      = null;
    tcResultSet segregation = null;
    try {
      // query the lookup and request only recods where the passed group name
      // belongs to
      final Map<String, String> filter = new HashMap<String, String>(1);
      filter.put(LookupValue.ENCODED, groupName);
      segregation = lookupFacade().getLookupValues(lookupDefinition, filter);

      // retrieve all groups the user currently have
      groups     = userFacade().getGroups(this.userKey);
      int i      = 0;
      boolean excluded = false;
      final String[] parameter = new String[2];
      while ((i < groups.getRowCount()) && (!excluded)) {
        groups.goToRow(i++);
        parameter[0] = groups.getStringValue(Group.NAME);
        // check if the current group raise a segregation
        int j = 0;
        while ((j < segregation.getRowCount()) && (!excluded)) {
          segregation.goToRow(j++);
          parameter[1] = segregation.getStringValue(LookupValue.DECODED);
          debug(method, AdapterBundle.format(AdapterMessage.CHECK_RESOURCE, parameter));
          if (parameter[0].equals(parameter[1]))
            excluded = true;
        }
      }
      response = (excluded) ? REJECT : APPROVE;
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findRole
  /**
   ** Returns the internal identifier of a group for the specified group name.
   **
   ** @param  lookupDefinition   the Lookup Definition containing the assignable
   **                            and removable groups.
   ** @param  groupName          the identifier of the group to resolve.
   **
   ** @return                    the desired internal identifier for the
   **                            specified group name.
   **                            If the group name is not in the system an empty
   **                            String will be returned.
   **
   ** @throws Exception          if resolving the role fails.
   */
  public final String findRole(String lookupDefinition, String groupName)
    throws Exception {

    final String method = "findRole";
    trace(method, SystemMessage.METHOD_ENTRY);

    String groupKey  = SystemConstant.EMPTY;
    try {
      final Map<String, String> map = new HashMap<String, String>();
      map.put(LookupValue.ENCODED, this.roleName);
      tcResultSet resultSet = lookupFacade().getLookupValues(lookupDefinition, map);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = {lookupDefinition, groupName };
        error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
      }
      else
        groupKey = resolveRole(groupName);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return groupKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveRole
  /**
   ** Returns the internal identifier of a group for the specified group name.
   **
   ** @param  lookupDefinition   the Lookup Definition containing the assignable
   **                            and removable groups.
   ** @param  groupName          the identifier of the group to resolve.
   **
   ** @return                    the desired internal identifier for the
   **                            specified group name.
   **                            If the group name is not in the system an empty
   **                            String will be returned.
   **
   ** @throws Exception
   */
  private String resolveRole(String lookupDefinition, String groupName)
    throws Exception {

    final String method = "resolveGroup";
    trace(method, SystemMessage.METHOD_ENTRY);

    String groupKey  = SystemConstant.EMPTY;
    try {
      HashMap map = new HashMap();
      map.put(LookupValue.ENCODED, groupName);
      tcResultSet resultSet = lookupFacade().getLookupValues(lookupDefinition, map);

      if (resultSet.getRowCount() == 0) {
        String[] parameter = {lookupDefinition, groupName };
        error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
      }
      else {
        resultSet.goToRow(0);
        groupKey = resolveRole(resultSet.getStringValue(LookupValue.DECODED));
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return groupKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveRole
  /**
   ** Returns the internal identifier of a group for the specified group name.
   **
   ** @param  groupName          the identifier of the group to resolve.
   **
   ** @return                    the desired internal identifier for the
   **                            specified group name.
   **                            If the group name is not in the system an empty
   **                            String will be returned.
   **
   ** @throws Exception          if retrieving the role fails.
   */
  public final String resolveRole(String groupName)
    throws Exception {

    final String method = "resolveRole";
    trace(method, SystemMessage.METHOD_ENTRY);

    String groupKey  = SystemConstant.EMPTY;
    try {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_GROUP), groupName };
      debug(method, TaskBundle.format(TaskMessage.NAME_TORESOLVE, parameter));
      final Map<String, String> filter = new HashMap<>();
      filter.put(Group.NAME, groupName);
      // returns a list of groups (in the form of tcResultSet) that match
      // the name attribute
      // the search is based on the permission view, which should retrieve
      // also groups where the current user has no declared privileges on
      tcResultSet resultSet = groupFacade().findGroups(filter, "view");
      if (resultSet.getRowCount() == 0)
        error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
      else if (resultSet.getRowCount() > 1)
        error(method, TaskBundle.format(TaskError.RESOURCE_AMBIGUOUS, parameter));
      else {
        resultSet.goToRow(0);
        groupKey = resultSet.getStringValue(Group.KEY);
      }
    }
    finally {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_GROUP), groupName, groupKey };
      debug(method, TaskBundle.format(TaskMessage.NAME_RESOLVED, parameter));
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return groupKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findUser
  /**
   ** Returns the internal identifier of an user for the specified login name.
   **
   ** @param  userLogin          the identifier of the user to resolve.
   **
   ** @return                    the desired internal identifier for the
   **                            specified login name.
   **                            If the login name is not in the system
   **                            <code>-1</code> will be returned.
   **
   ** @throws Exception
   */
   private long findUser(String userLogin) {
    final String method = "findUser";
    trace(method, SystemMessage.METHOD_ENTRY);

    long userKey = -1L;
    tcResultSet resultSet = null;
    try {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_ACCOUNT), userLogin };
      debug(method, TaskBundle.format(TaskMessage.NAME_TORESOLVE, parameter));

      HashMap map = new HashMap();
      map.put(User.USERID, userLogin);
      // we must use findAllUsers because this task is running in the security
      // context of the current user
      resultSet = userFacade().findAllUsers(map);
      if (resultSet.getRowCount() == 0)
        error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
      else if (resultSet.getRowCount() > 1)
        error(method, TaskBundle.format(TaskError.RESOURCE_AMBIGUOUS, parameter));
      else {
        resultSet.goToRow(0);
        userKey = resultSet.getLongValue(User.KEY);
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_ACCOUNT), userLogin, Long.toString(userKey) };
      debug(method, TaskBundle.format(TaskMessage.NAME_RESOLVED, parameter));
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return userKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isMember
  /**
   ** Check if the user is member of the specified group.
   **
   ** @return                    <code>true</code> if the specified user is
   **                            member of the group.
   */
  private boolean isMember(long groupKey) {
    return isMember(groupKey, this.userKey);
  }
}