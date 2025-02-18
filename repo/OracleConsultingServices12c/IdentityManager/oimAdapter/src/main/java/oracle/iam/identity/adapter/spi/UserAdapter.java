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

    File        :   UserAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    UserAdapter.


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
import java.util.Date;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcUserNotFoundException;

import Thor.API.Operations.tcFormInstanceOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;
import com.thortech.xl.dataaccess.tcDataSetException;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.orb.dataaccess.tcDataAccessException;

import com.thortech.xl.util.adapters.tcUtilXellerateOperations;


import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.EntityAdapter;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AttributeMapping;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.naming.User;
import oracle.iam.identity.foundation.naming.Group;
import oracle.iam.identity.foundation.naming.LookupValue;
import oracle.iam.identity.foundation.naming.ResourceObject;
import oracle.iam.identity.foundation.naming.ProcessDefinition;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.adapter.AdapterBundle;
import oracle.iam.identity.adapter.AdapterMessage;

////////////////////////////////////////////////////////////////////////////////
// class UserAdapter
// ~~~~~ ~~~~~~~~~~~
/**
 ** Adpapter dedicated to operate on the Oracle Identity Manager User Entity.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class UserAdapter extends EntityAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String LOGGER_CATEGORY = "OCS.USR.PROVISIONING";

  /** the category of the history value to fetch */
  static final String HISTORY_PREFIX  = "old";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>UserAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public UserAdapter(final tcDataProvider provider) {
    this(provider, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>UserAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public UserAdapter(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulkUpdate
  /**
   ** Oracle Identity Manager provides capability to copy the data from user
   ** form to the process forms of the resource objects. This is accomplished by
   ** defining a mapping between the USR table column and the process task that
   ** should be invoked in <code>Lookup.USR_PROCESS_TRIGGERS</code> lookup.
   ** <p>
   ** In example adding a mapping for:
   ** <pre>
   **   <code>
   **   Code Key        Decode 
   **   -------------   ----------------------  
   **   USR_MOBILE      Change Mobile
   **   </code>
   ** </pre>
   ** will cause the <code>Change Mobile</code> process task to be executed for
   ** each provisioning process the task is defined when the <code>Mobile</code>
   ** (USR_MOBILE) field for the OIM User is updated. The
   ** <code>Change Mobile</code> process task needs to update the resource
   ** objects process form with a new value which will trigger the process task
   ** that actually updates the target system with the new value. Normally, if
   ** there are not that many fields defined to be synchronized between the User
   ** form and the resource objects, this works fine and will not cause
   ** performance problems or load peaks in the target system. However if there
   ** are a lot of fields that needs to be synchronized and there are a lot of
   ** changes made for the OIM users on daily basis the customers might see some
   ** performance issues as each update is made in a single modify operation to
   ** the target system.
   ** <p>
   ** Oracle Identity Manager version 11.1.1.5 introduced a new feature called
   ** <b>Bulk Attribute Propagation</b> where the multiple updates on User form
   ** could be moved to process form in a single operation and the thus the
   ** multiple update/modify calls to the target resource can be avoided.
   ** <p>
   ** The adapter expects three variables:
   ** <pre>
   **   <code>
   **   Name                  Type       Map To
   **   --------------------  ---------- ------------------------
   **   processInstance       String     Resolve at runtime
   **   mapping               Map        Resolve at runtime
   **   data                  Map        Resolve at runtime
   **   </code>
   ** </pre>
   ** To use this feature you need to:
   ** <ol>
   **   <li>Create a lookup with name a name of your choiceto hold a mapping
   **       between USR table columns and the resources UDF columns.
   **       <br>
   **       The adapter code uses this lookup to map the change on User form
   **       to the UD table column.
   **   <li>Add a conditional process task to the provision process of each
   **       resource object the bulk updates should be propagated to. In
   **       example:
   **       <pre>
   **       Process task name: Bulk Update Process Form
   **       Mapped to:         Adapter provided by this implementation
   **       Adapter Variables mapped as:
   **         processInstance: Process Data    -&gt; Process Instance
   **         mapping        : Literal         -&gt; Lookup Definition Name
   **         data           : User Definition -&gt; Bulk Changes
   **       </pre>
   **   <li>Make sure the <b>&lt;UD table name&gt; Updated</b> process task
   **       exists for the resource and it's able to propagate all changes to
   **       the target system.
   **       <br>
   **       OIM will automatically search for this process task and execute it
   **       if there are multiple updates made to the process form data at the
   **       same time.
   **       <br>
   **       For new ICF based connectors this process task automatically exists.
   **   <li>Map the "BULK" keyword to the process task name that should be
   **       executed on bulk update of User form. In example:
   **       <pre>
   **         Code Key       Decode
   **         -------------  ----------------------
   **         BULK           Bulk Update Process Form
   **       </pre>
   ** </ol>
   ** After creating the components, when multiple User attributes are changed
   ** for some user OIM will check the <code>Lookup.USR_PROCESS_TRIGGERS</code>
   ** lookup for the process task name mapped to "BULK" keyword and executed the
   ** process task for each provisioning process the process task exists. The
   ** custom adapter (created here) will update the process form which will
   ** trigger the <b>&lt;UDF name&gt; Updated</b> task propagating all the
   ** changes to the target system in one modify/update call.
   **
   ** @param  processInstance    the <code>Process Instance</code> providing the
   **                            data of an account model.
   ** @param  attributeMapping   the name of the <code>Lookup Definition</code>
   **                            specifying the Mapping of user form fields and
   **                            their transformation that part of a
   **                            provisioning task.
   ** @param  identityData       the changed values of an identity entity like
   **                            user, organization or role etc.
   **
   ** @return                    an appropriate response code.
   **
   ** @throws TaskException      if the Lookup Definition
   **                            <code>attributeMapping</code> is not defined in
   **                            the Oracle Identity Manager meta entries or one
   **                            or more attributes are missing on the
   **                            Lookup Definition.
   */
  public String bulkUpdate(final Long processInstance, final String attributeMapping, final Map<String, Object> identityData)
    throws TaskException {

    final String method = "updateProcess";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the code returned to the Process Task in an optimistic manner
    String responseCode = SUCCESS;
    // the Lookup containing the mappings between USR table and the UD table.
    // for flexibility this will be provided as adapter variable
    final AttributeMapping lookup = new AttributeMapping(this, attributeMapping);
    debug(method, String.format("Bulk data containing the identity changes:\n%s", StringUtility.formatCollection(identityData)));
    if (!CollectionUtility.empty(lookup)) {
      // loop through the USR table changes and add the values to the process
      // form data
      final Map<String, Object> processData = lookup.filterByEncoded(identityData);
      debug(method, String.format("Bulk data containing the process changes:\n%s", StringUtility.formatCollection(processData)));
      // obtain instances of tcFormInstanceOperationsIntf API's
      tcFormInstanceOperationsIntf formInstanceFacade = formInstanceFacade();
      try {
        // update the process form with the new data
        formInstanceFacade.setProcessFormData(processInstance.longValue(), processData);
      }
      catch (Exception e) {
        responseCode = FAILURE;
      }
      finally {
        formInstanceFacade.close();
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   daysToDeprovision
  /**
   ** Returns the future date.
   **
   ** @param  daysToDeprovison the number of days, which are to be added to the
   **                          current date when the event arose.
   **
   ** @return                  the date when the user should be deprovisoned.
   */
  public Date daysToDeprovision(final int daysToDeprovison) {
    return DateAdapter.evalutaDate(daysToDeprovison);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   statusChangingTo
  /**
   ** Whether a user is transferred to activated.
   **
   ** @param  userLogin          the login identifier of the user to discover.
   ** @param  userStatus         the status where the user just have.
   **
   ** @return                    <code>Disabled</code> whether the specified
   **                            <code>userLogin</code> is just transferd from
   **                            <code>Active</code> to <code>Disabled</code>;
   **                            <code>Active</code> whether the specified
   **                            <code>userLogin</code> is just transferd from
   **                            <code>Disabled</code> to <code>Active</code>;
   **                            <code>null</code> otherwise.
   */
  public String statusChangingTo(final String userLogin, final String userStatus) {
    final String method = "statusChangingTo";
    trace(method, SystemMessage.METHOD_ENTRY);

    String statusNew = SystemConstant.EMPTY;
    String statusOld = lastUserStatus(userLogin, HISTORY_PREFIX);

    // if the discovered value is null or is the same as the status passed in
    // the user status seems to be not affected by any changes
    if (!StringUtility.isEmpty(statusOld) && !userStatus.equals(statusOld)) {
      if (User.STATUS_ACTIVE.equals(userStatus) && statusOld.equals(User.STATUS_DISABLED))
        statusNew = User.STATUS_ACTIVE;
      else if (User.STATUS_DISABLED.equals(userStatus) && statusOld.equals(User.STATUS_ACTIVE))
        statusNew = User.STATUS_DISABLED;

      String[] parameter = {User.FIELD_STATUS, statusOld, statusNew};
      debug(method, AdapterBundle.format(AdapterMessage.STATUS_CHANGINGTO, parameter));
    }
    else
      debug(method, AdapterBundle.format(AdapterMessage.STATUS_NOCHANGES, User.FIELD_STATUS));

    trace(method, SystemMessage.METHOD_EXIT);
    return statusNew;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isActivating
  /**
   ** Whether a user is transferred to activated.
   **
   ** @param  userLogin          the login identifier of the user to discover.
   **
   ** @return                    <code>true</code> whether the specified
   **                            <code>userID</code> is just transferd from
   **                            <code>Active</code> to <code>Disabled</code>;
   **                            <code>false</code> otherwise.
   */
  public boolean isActivating(final String userLogin) {
    return isStatusTransfering(userLogin, User.STATUS_ACTIVE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDeactivating
  /**
   ** Whether a user is transferred to deactivated.
   **
   ** @param  userLogin          the login identifier of the user to discover.
   **
   ** @return                    <code>true</code> whether the specified
   **                            <code>userID</code> is just transferd from
   **                            <code>Disabled</code> to <code>Active</code>;
   **                            <code>false</code> otherwise.
   */
  public boolean isDeactivating(final String userLogin) {
    return isStatusTransfering(userLogin, User.STATUS_DISABLED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isStatusTransfering
  /**
   ** Whether a user has the desired status in the last changed record.
   **
   ** @param  userLogin          the login identifier of the user to discover.
   ** @param  statusGaining      the status where the user is just transferred.
   **
   ** @return                    <code>true</code> whether the specified
   **                            <code>userID</code> has the desired
   **                            <code>status</code>.
   */
  public boolean isStatusTransfering(final String userLogin, final String statusGaining) {
    return !statusGaining.equals(lastUserStatus(userLogin, HISTORY_PREFIX));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translateStatus
  /**
   ** Translate status information accordingly to the Oracle Identity Manager
   ** predefined terminology.
   **
   ** @param  lookupDefinition   the Oracle Identity Manager Lookup Definition
   **                            which holds the mapping of the status
   **                            information to transform.
   ** @param  originStatus       the incoming status information.
   ** @param  defaultStatus      the default value of a status information if
   **                            the incoming information is not mapped
   **                            accordingly.
   **
   ** @return                    a status information accordingly to the Oracle
   **                            Identity Manager predefined terminology.
   */
  public String translateStatus(String lookupDefinition, String originStatus, String defaultStatus) {
    final String method = "translateStatus";
    trace(method, SystemMessage.METHOD_ENTRY);

    String      result    = null;
    tcResultSet resultSet =  null;
    try {
      final Map<String, String> map = new HashMap<String, String>();
      map.put(LookupValue.ENCODED, originStatus);
      resultSet = lookupFacade().getLookupValues(lookupDefinition, map);

      if (resultSet.getRowCount() == 0) {
        String[] parameter = {lookupDefinition, originStatus };
        error(method, TaskBundle.format(TaskError.LOOKUP_ENCODED_VALUE, parameter));
        result = defaultStatus;
      }
      else {
        resultSet.goToRow(0);
        result = resultSet.getStringValue(LookupValue.DECODED);
      }
      if (StringUtility.isEmpty(result))
        result = defaultStatus;
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectStatus
  /**
   ** Returns the status of a specified resource object for a user
   ** with a specified user key.
   **
   ** @param  userKey            the user key.
   ** @param  processInstance    the process instance key.
   **
   ** @return                    the status of a specified resource object for a
   **                            user with a specified user key.
   */
  public String objectStatus(long userKey, long processInstance) {
    final String method = "objectStatus";
    trace(method, SystemMessage.METHOD_ENTRY);

    String objectStatus = SystemConstant.EMPTY;
    if ((userKey == 0L) && (processInstance == 0L))
      return objectStatus;

    String   userID     = Long.toString(userKey);
    String[] parameter  = { TaskBundle.string(TaskMessage.ENTITY_IDENTITY), userID };

    tcResultSet resultSet = null;
    try {
      debug(method, TaskBundle.format(TaskMessage.KEY_TORESOLVE, parameter));
      resultSet   = userFacade().getObjects(userKey);
      for (int i = 0; i < resultSet.getRowCount(); i++) {
        resultSet.goToRow(i);
        long foundPrcInstKey = resultSet.getLongValue(ProcessDefinition.KEY);
        if (foundPrcInstKey == processInstance) {
          objectStatus=resultSet.getStringValue( ResourceObject.STATUS);
          debug(method,  ResourceObject.STATUS +  " : "+ objectStatus);
        }
      }
    }
    catch (tcColumnNotFoundException e) {
      error(method, TaskBundle.format(TaskError.COLUMN_NOT_FOUND, parameter));
    }
    catch (tcUserNotFoundException e) {
      error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
    }
    catch (tcAPIException e) {
      fatal(method, e);
    }
    debug(method, TaskBundle.format(TaskMessage.KEY_RESOLVED, parameter));
    trace(method, SystemMessage.METHOD_EXIT);

    return objectStatus;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveRole
  /**
   ** Returns the internal identifier of a role for the specified role name.
   **
   ** @param  roleName           the identifier of the role to resolve.
   **
   ** @return                    the desired internal identifier for the
   **                            specified role name.
   **                            If the role name is not in the system an empty
   **                            String will be returned.
   */
  public long resolveRole(String roleName) {
    final String method = "resolveRole";
    trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_GROUP);
    parameter[1] = roleName;
    debug(method, TaskBundle.format(TaskMessage.NAME_TORESOLVE, parameter));

    long roleKey = -1L;
    try {
      Map filter = new HashMap();
      filter.put(Group.NAME, roleName);
      tcResultSet resultSet = groupFacade().findGroups(filter);
      if (resultSet.getRowCount() == 0)
        error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
      else if (resultSet.getRowCount() > 1)
        error(method, TaskBundle.format(TaskError.RESOURCE_AMBIGUOUS, roleName));
      else {
        resultSet.goToRow(0);
        roleKey = resultSet.getLongValue(Group.KEY);
      }
    }
    catch (tcColumnNotFoundException e) {
      error(method, e.getLocalizedMessage());
    }
    catch (tcAPIException e) {
      fatal(method, e);
    }
    finally {
      parameter[2] = String.valueOf(roleKey);
      debug(method, TaskBundle.format(TaskMessage.NAME_RESOLVED, parameter));
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return roleKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isMember
  /**
   ** Check if the user is member of the specified role.
   **
   ** @param  roleKey            the internal identfier of the role that has to
   **                            be checked
   ** @param  userKey            the internal identfier of the user that has to
   **                            be checked
   **
   ** @return                    <code>true</code> if the specified user is
   **                            member of the role; otherwise
   **                            <code>false</code>.
   */
  public boolean isMember(long roleKey, long userKey) {
    final String method = "isMember";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean response = false;
    tcResultSet roles = null;
    try {
      roles = userFacade().getGroups(userKey);
      for (int i = 0; i < roles.getRowCount(); i++) {
        roles.goToRow(i);
        if (roleKey == roles.getLongValue(Group.KEY)) {
          response = true;
          break;
        }
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
  // Method:   assignToRole
  /**
   ** Adds the specified user to the specified role.
   **
   ** @param  roleKey            the internal identfier of the role that has to
   **                            be assigned
   ** @param  userKey            the internal identfier of the user that has to
   **                            be assigned
   **
   ** @return                    {@link #SUCCESS} if the specified user was
   **                            succesfully added to the specified role;
   **                            otherwise {@link #FAILURE}.
   */
  public String assignToRole(final long roleKey, final long userKey) {
    return this.assignToRole(roleKey, userKey, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignToRole
  /**
   ** Adds the specified user to the specified role.
   **
   ** @param  roleKey            the internal identfier of the role that has to
   **                            be assigned
   ** @param  userKey            the internal identfier of the user that has to
   **                            be assigned
   ** @param  evaluatePolicies   indicate whether to evaluate policies or not
   **                            when user is added to the role
   **
   ** @return                    {@link #SUCCESS} if the specified user was
   **                            succesfully added to the specified role;
   **                            otherwise {@link #FAILURE}.
   */
  public String assignToRole(final long roleKey, final long userKey, final boolean evaluatePolicies) {
    final String method = "assignToRole";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      groupFacade().addMemberUser(roleKey, userKey, evaluatePolicies);
      return SUCCESS;
    }
    catch (Exception e) {
      fatal(method, e);
      return FAILURE;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeFromRole
  /**
   ** Removes the specified user from the specified role.
   **
   ** @param  roleKey            the internal identfier of the role that has to
   **                            be revoked
   ** @param  userKey            the internal identfier of the user that has to
   **                            be revoked
   **
   ** @return                    {@link #SUCCESS} if the specified user was
   **                            succesfully removed from the specified role;
   **                            otherwise {@link #FAILURE}.
   */
  public String removeFromRole(final long roleKey, final long userKey) {
    final String method = "removeFromRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      groupFacade().removeMemberUser(roleKey, userKey);
      return SUCCESS;
    }
    catch (Exception e) {
      fatal(method, e);
      return FAILURE;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processOrdered
  /**
   ** Validates if the specified process is ordered for the specified user
   ** profile
   **
   ** @param  userKey            the internal identifier of the user profile to
   **                            validate.
   ** @param  processName        the name of the process to validate.
   **
   ** @return                    {@link #SUCCESS}  if the specified identity key
   **                            is provisioned to the specified process;
   **                            otherwise {@link #FAILURE}.
   */
  public String processOrdered(final String userKey, final String processName) {
    final String method = "processOrdered";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      return Integer.parseInt(tcUtilXellerateOperations.checkProcessOrderedForUser(this.provider(), processName, userKey)) > 0 ? SUCCESS : FAILURE;
    }
    catch (Exception e) {
      fatal(method, e);
      return FAILURE;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRegularAccount
  /**
   ** Changes a service account to a regular account.
   **
   ** @param  objectInstance     the object instance key for a user in the
   **                            system.
   **
   ** @return                    {@link #SUCCESS}  if the specified account was
   **                            succesfully change to a regular account;
   **                            otherwise {@link #FAILURE}.
   */
  public String assignRegularAccount(final String objectInstance) {
    return this.assignRegularAccount(Long.parseLong(objectInstance));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRegularAccount
  /**
   ** Changes a service account to a regular account.
   **
   ** @param  objectInstance     the object instance key for a user in the
   **                            system.
   **
   ** @return                    {@link #SUCCESS}  if the specified account was
   **                            succesfully change to a regular account;
   **                            otherwise {@link #FAILURE}.
   */
  public String assignRegularAccount(final Long objectInstance) {
    return this.assignRegularAccount(objectInstance.longValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignServiceAccount
  /**
   ** Changes a regular account to a service account.
   **
   ** @param  objectInstance     the object instance key for a user in the
   **                            system.
   **
   ** @return                    {@link #SUCCESS}  if the specified account was
   **                            succesfully change to a service account;
   **                            otherwise {@link #FAILURE}.
   */
  public String assignServiceAccount(final String objectInstance) {
    return this.assignServiceAccount(Long.parseLong(objectInstance));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignServiceAccount
  /**
   ** Changes a regular account to a service account.
   **
   ** @param  objectInstance     the object instance key for a user in the
   **                            system.
   **
   ** @return                    {@link #SUCCESS}  if the specified account was
   **                            succesfully change to a service account;
   **                            otherwise {@link #FAILURE}.
   */
  public String assignServiceAccount(final Long objectInstance) {
    return this.assignServiceAccount(objectInstance.longValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   moveServiceAccount
  /**
   ** Moves the service account from its current owner to a new owner.
   **
   ** @param  objectInstance     the object instance key for a user in the
   **                            system.
   ** @param  targetUser         the internal identifier of the user who shall
   **                            be the new owner.
   **
   ** @return                    {@link #SUCCESS}  if the specified service
   **                            account was succesfully moved to a regular
   **                            account; otherwise {@link #FAILURE}.
   */
  public String moveServiceAccount(final String objectInstance, final String targetUser) {
    return this.moveServiceAccount(Long.parseLong(objectInstance), Long.parseLong(targetUser));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   moveServiceAccount
  /**
   ** Moves the service account from its current owner to a new owner.
   **
   ** @param  objectInstance     the object instance key for a user in the
   **                            system.
   ** @param  targetUser         the internal identifier of the user who shall
   **                            be the new owner.
   **
   ** @return                    {@link #SUCCESS}  if the specified service
   **                            account was succesfully moved to a regular
   **                            account; otherwise {@link #FAILURE}.
   */
  public String moveServiceAccount(final Long objectInstance, final Long targetUser) {
    return this.moveServiceAccount(objectInstance.longValue(), targetUser.longValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastUserStatus
  /**
   ** Returns the user.
   ** <p>
   ** This method discovers the user profile history to return the last known
   ** user status.
   **
   ** @param  userLogin          the login identifier of the user to discover.
   ** @param  which              the <code>new</code> or the <code>old</code>
   **                            value of the user profile history.
   **
   ** @return                    the user status changed by the current
   **                            scheduled item.
   */
  private String lastUserStatus(final String userLogin, final String which) {
    final String method = "lastUserStatus";
    trace(method, SystemMessage.METHOD_ENTRY);

    String userStatus = null;

    // Statement picks up user status from the last change record of an
    // Oracle Identity Manager User
    final String query  = "select usr_status as old from upa_usr where usr_login = ? and upa_usr_eff_to_date is null";

    PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(provider(), query);
    statement.setString(1, userLogin);
    try {
      statement.execute(1);
      tcDataSet dataset = statement.getDataSet();
      userStatus = dataset.isEmpty() ? null : dataset.getString(which);
    }
    catch (tcDataAccessException e) {
      fatal(method, e);
    }
    catch (tcDataSetException e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);

    return userStatus;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRegularAccount
  /**
   ** Changes a service account to a regular account.
   **
   ** @param  objectInstance     the object instance key for a user in the
   **                            system.
   **
   ** @return                    {@link #SUCCESS}  if the specified account was
   **                            succesfully change to a regular account;
   **                            otherwise {@link #FAILURE}.
   */
  private String assignRegularAccount(final long objectInstance) {
    final String method = "assignRegularAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      userFacade().changeFromServiceAccount(objectInstance);
      return SUCCESS;
    }
    catch (Exception e) {
      fatal(method, e);
      return FAILURE;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignServiceAccount
  /**
   ** Changes a regular account to a service account.
   **
   ** @param  objectInstance     the object instance key for a user in the
   **                            system.
   **
   ** @return                    {@link #SUCCESS}  if the specified account was
   **                            succesfully change to a service account;
   **                            otherwise {@link #FAILURE}.
   */
  private String assignServiceAccount(final long objectInstance) {
    final String method = "assignServiceAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      userFacade().changeToServiceAccount(objectInstance);
      return SUCCESS;
    }
    catch (Exception e) {
      fatal(method, e);
      return FAILURE;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   moveServiceAccount
  /**
   ** Moves the service account from its current owner to a new owner.
   **
   ** @param  objectInstance     the object instance key for a user in the
   **                            system.
   ** @param  targetUser         the internal identifier of the user who shall
   **                            be the new owner.
   **
   ** @return                    {@link #SUCCESS}  if the specified service
   **                            account was succesfully moved to a regular
   **                            account; otherwise {@link #FAILURE}.
   */
  private String moveServiceAccount(final long objectInstance, final long targetUser) {
    final String method = "moveServiceAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      userFacade().moveServiceAccount(objectInstance, targetUser);
      return SUCCESS;
    }
    catch (Exception e) {
      fatal(method, e);
      return FAILURE;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}