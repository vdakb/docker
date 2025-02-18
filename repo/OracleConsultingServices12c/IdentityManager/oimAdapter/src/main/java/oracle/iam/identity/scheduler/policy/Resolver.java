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

    File        :   Resolver.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Resolver.


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
import java.util.List;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcObjectNotFoundException;
import Thor.API.Exceptions.tcUserNotFoundException;
import Thor.API.Exceptions.tcFormNotFoundException;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Exceptions.tcNotAtomicProcessException;
import Thor.API.Exceptions.tcProcessNotFoundException;
import Thor.API.Exceptions.tcProvisioningNotAllowedException;
import Thor.API.Exceptions.tcRevocationNotAllowedException;

import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.dataaccess.tcDataSetException;
import com.thortech.xl.dataaccess.tcDataSet;

import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.orb.dataaccess.tcDataAccessException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.collection.MultiSet;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ResourceObject;
import oracle.iam.identity.foundation.naming.ProcessDefinition;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;

import oracle.iam.identity.foundation.offline.Account;
import oracle.iam.identity.foundation.offline.RoleEntity;
import oracle.iam.identity.foundation.offline.AccessPolicy;
import oracle.iam.identity.foundation.offline.ProcessForm;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.ApplicationAccount;
import oracle.iam.identity.foundation.offline.ApplicationEntity;

import oracle.iam.identity.adapter.spi.RoleAdapter;

import oracle.iam.identity.resource.ResolverBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Resolver
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** The <code>Resolver</code> act as the service end point for the Oracle
 ** Identity Manager to resolve <code>Access Policies</code>.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class Resolver extends Reconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute value which may be defined on this task to specify which
   ** operation type should take place.
   */
  protected static final String RECONCILIATION_OPERATION_REFRESH = "Refresh";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** operation type should take place.
   */
  protected static final String RECONCILIATION_OPERATION_RESTART = "Restart";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** retrofit action should take place.
   */
  protected static final String RETROFIT_OPERATION_STRICT        = "Strict";

  /**
   ** Attribute value which may be defined on this task to specify which
   ** retrofit action should take place.
   */
  protected static final String RETROFIT_OPERATION_IGNORE        = "Ignore";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** retrofit action should be performed.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String RETROFIT_OPERATION               = "Retrofit Operation";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** retrofit mode should be performed.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String RETROFIT_ACTION                  = "Retrofit Action";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute                 = {
    /** the task attribute with migration resource object */
    TaskAttribute.build(RECONCILE_OBJECT,         TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,                TaskAttribute.MANDATORY)
    /** the task attribute with operational mode */
  , TaskAttribute.build(RECONCILIATION_OPERATION, TaskAttribute.MANDATORY)
    /** the task attribute with the retrofit action */
  , TaskAttribute.build(RETROFIT_OPERATION,       TaskAttribute.MANDATORY)
    /** the task attribute with the retrofit mode */
  , TaskAttribute.build(RETROFIT_ACTION,          TaskAttribute.MANDATORY)
    /** the task attribute that specifies the server definition */
  , TaskAttribute.build(SERVER_DEFINITION,        TaskAttribute.MANDATORY)
    /** the task attribute which organizations should be handled */
  , TaskAttribute.build(FILTER_ORGANIZATION,      SystemConstant.EMPTY)
    /** the task attribute which users should be handled */
  , TaskAttribute.build(FILTER_IDENTITY,          SystemConstant.EMPTY)
    /** the task attribute which object type should be handled during provisioning */
  , TaskAttribute.build(OBJECTTYPE,               ResourceObject.TYPE_APPLICATION)
    /** the task attribute which object status should be handled during provisioning */
  , TaskAttribute.build(OBJECTSTATUS,             ResourceObject.STATUS_REVOKED)
    /** the task attribute which object type should be handled during provisioning */
  , TaskAttribute.build(PROCESSTYPE,              ProcessDefinition.PROVISIONING)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the operational mode of the task (Refresh |  Restart) */
  private String                  operation;

  /** the operational mode of the task (Strict |  Ignore) */
  private String                  retrofit_operation;

  /** the operational mode of the task (Revoke |  Disable) */
  private String                  retrofit_action;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Resolver</code> task adpater that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Resolver() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  protected final TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Company Phonebook.
   **
   ** @throws TaskException      the excepetion thrown if any goes wrong
   */
  protected void onExecution()
    throws TaskException {

    final String[] parameter = new String[3];
    parameter[0] = getName();
    parameter[1] = reconcileObject();

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, parameter));

    this.lastExecution = new Date(lastReconciled().getTime());
    info(ResolverBundle.format(ResolverMessage.PRECONDITION_STARTED, parameter));

    createConfiguration();
    createServer();

    countChanges();
    if (this.affectedEntries == 0) {
      info(ResolverBundle.format(ResolverMessage.PRECONDITION_COMPLETED, parameter));
      info(TaskBundle.format(TaskMessage.NOTHING_TO_CHANGE, parameter));
      info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, parameter));
    }
    else {
      preConditions();
      if (preConditionViolated()) {
        stopExecution();
        info(ResolverBundle.format(ResolverMessage.PRECONDITION_STOPPED, parameter));
      }
      else {
        info(ResolverBundle.format(ResolverMessage.PRECONDITION_COMPLETED, parameter));
        try {
          open();

          // remember the current date as the timestamp on which this task has
          // lastmigrated at start
          // setting it at this time that we have next time this scheduled task
          // will run the changes made during the execution of this task
          // Database Server current time is different from this system time
          lastReconciled(serverDate());

          processChanges();

          updateLastReconciled();
          info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, parameter));
        }
        // in any case of an unhandled exception
        catch (TaskException e) {
          warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, parameter));
          throw e;
        }
        finally {
          close();
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeStatement (Reconciliation)
  /**
   ** Executes the SQL statement.
   ** <p>
   ** Before the statement is executed all values are bound to the placeholders.
   **
   ** @return                    the result set of the executed query.
   **
   ** @throws TaskException      if the bind or execution fails.
   */
  @Override
  protected ResultSet executeStatement(final PreparedStatement statement)
    throws TaskException {

    int                       position = 0;
    final ApplicationEntity[] server   = this.sourceServer();
    try {
      for (; position < server.length; position++)
        statement.setLong(position + 1, server[position].key());

      // bind the required process type
      statement.setString(++position, stringValue(PROCESSTYPE));

      // bind the required object status
      statement.setString(++position, stringValue(OBJECTSTATUS));

      // bind the values of the date range to pick up the changes
      statement.setDate  (++position, this.lastExecution());
      statement.setDate  (++position, this.lastExecution());

      final String userFilter = stringValue(FILTER_IDENTITY).toUpperCase();
      if (!StringUtility.isEmpty(userFilter)) {
        if (userWildcardPos != -1)
          statement.setString(++position, userFilter.substring(0, userWildcardPos) + "%");
        else
          statement.setString(++position, userFilter);
      }

      final String orgFilter = stringValue(FILTER_ORGANIZATION);
      if (!StringUtility.isEmpty(orgFilter)) {
        if (orgWildcardPos != -1)
          statement.setString(++position, orgFilter.substring(0, orgWildcardPos) + "%");
        else
          statement.setString(++position, orgFilter);
      }

      return statement.executeQuery();
    }
    catch (SQLException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareCountQuery (Reconciliation)
  /**
   ** Build the SQL Query to count affected entries.
   **
   ** @return                    the SQL Query to count affected entries.
   */
  protected StringBuilder prepareCountQuery() {
    StringBuilder query = new StringBuilder();
    query.append("select count(USR.USR_KEY) ");
    query.append(prepareWhereClause());
    return query;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  public void initialize()
    throws TaskException {

    // ensure inheritance
    super.initialize();

    // check if the specified value provided for the reconciliation operation
    // is in range
    String[] operationRange = {RECONCILIATION_OPERATION_REFRESH, RECONCILIATION_OPERATION_RESTART};
    this.operation = validateStringValue(RECONCILIATION_OPERATION, operationRange);

    // check if the specified value provided for the retrofit action is in range
    String[] retrofitOperationRange = {RETROFIT_OPERATION_IGNORE, RETROFIT_OPERATION_STRICT};
    this.retrofit_operation = validateStringValue(RETROFIT_OPERATION, retrofitOperationRange);

    // check if the specified value provided for the retrofit action is in range
    String[] retrofitModeRange = {ResolverMessage.REVOKE, ResolverMessage.DISABLE};
    this.retrofit_action = validateStringValue(RETROFIT_ACTION, retrofitModeRange);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processChanges
  /**
   ** Load the a <code>Resource Object</code> and their dependencies.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected final void processChanges()
    throws TaskException {

    // check if any pre condition sends SIGTERM
    if (stopped())
      return;

    final String method = "processChanges";
    trace(method, SystemMessage.METHOD_ENTRY);

    ResultSet         resultSet   = null;
    PreparedStatement statement   = null;

    String[] parameter = {getName(), reconcileObject(), String.valueOf(this.affectedEntries), SystemConstant.EMPTY };
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, parameter));
    info(TaskBundle.format(TaskMessage.COLLECTING_BEGIN, parameter));
    try {
      statement = DatabaseStatement.createPreparedStatement(this.connection(), prepareDataQuery().toString());
      resultSet = executeStatement(statement);

      while (!isStopped() && resultSet.next()) {
        // reset the iterator to fetch the values
        int column = 0;
          // create a container for the detected account with  the base data
        final Account account = AccountFactory.createAccount(resultSet.getLong(++column), resultSet.getString(++column));

        // process the created account
        // the first fetched column is the object instance key, the second
        // fetched column is the process instance key
        processSubject(account, resultSet.getLong(++column), resultSet.getLong(++column));
      }
      info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
      info(TaskBundle.format(TaskMessage.COLLECTING_COMPLETE, parameter));
    }
    catch (SQLException e) {
      throw TaskException.unhandled(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      trace(method, SystemMessage.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Matchs an {@link ApplicationAccount} to the <code>Access Policies</code>
   ** of the target server.
   **
   ** @param  account            the {@link ApplicationAccount} to process.
   ** @param  objectInstance     the <code>Object Instance</code> of a
   **                            provisioned <code>Resource Object</code>
   ** @param  processInstance    the <code>Process Instance</code> providing the
   **                            data of the account.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private void processSubject(final Account account, final long objectInstance, final long processInstance)
    throws TaskException {

    // create the source data mapping for the existing account based on the data
    // the entitlements that are fetched will be translated to the target system
    createSourceProcessData(account, processInstance);

    // match the account on entitlement level; remeber the entitlements are
    // already translated to the source system
    matchSourceProcessData(account);

    String[] parameter = {
      TaskBundle.string(TaskMessage.ENTITY_ACCOUNT)
    , account.name()
    , ResolverBundle.string(ResolverMessage.PROVISION) + " / " + ResolverBundle.string(this.retrofit_action)
    };
    // check if a match was found for any resource that should be provisined
    final List<RoleEntity> assignRole = account.roleToAssign();
    // check if a match was found for any resource that has be revoked
    final List<RoleEntity> revokeRole = account.roleToRevoke();
    if ((assignRole == null || assignRole.size() == 0) && (revokeRole == null || revokeRole.size() == 0)) {
      info(ResolverBundle.format(ResolverMessage.ACCOUNT_RESOURCE_NORESOURCES, parameter));
      return;
    }

    // create all the resources that are already provisioned to the account
    // that match the required Object Type
    createProvisionedResources(account, stringValue(OBJECTTYPE));

    // revoke the unmatched entitlements if necessary
    boolean revoked = false;
    if (revokeRole != null && !revokeRole.isEmpty()) {
      final ApplicationEntity[] server = this.sourceServer();
      // start the iteration to assing all resource object as resource to
      // provision
      for (RoleEntity i : revokeRole) {
        // merge all resources that referenced by the groups overall servers
        // in a sorted set to ensure that any resource is provisioned only once
        for (int j = 0; j < server.length; j++)
          account.addApplication(server[j].dependency(i.name()));
      }
      revoked = retrofitRevocation(account);
    }
    else {
      parameter[2] = ResolverBundle.string(this.retrofit_action);
      info(ResolverBundle.format(ResolverMessage.ACCOUNT_RESOURCE_NORESOURCES, parameter));
    }

    // provision the matched entitlements if necessary
    boolean provisioned = false;
    if (assignRole != null && !assignRole.isEmpty()) {
      final ApplicationEntity[] server = this.sourceServer();
      // start the iteration to assing all resource object as resource to
      // provision
      for (RoleEntity i : assignRole) {
        // merge all resources that referenced by the groups overall servers
        // in a sorted set to ensure that any resource is provisioned only once
        for (int j = 0; j < server.length; j++)
          account.addApplication(server[j].dependency(i.name()));
      }
      provisioned = retrofitProvisioning(account, objectInstance, processInstance);
    }
    else {
      parameter[2] = ResolverBundle.string(ResolverMessage.PROVISION);
      info(ResolverBundle.format(ResolverMessage.ACCOUNT_RESOURCE_NORESOURCES, parameter));
    }

    if (provisioned || revoked)
      // here we know that something with the user will happens
      incrementSuccess();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSourceProcessData
  /**
   ** Loads the <code>User Account</code> source process data from Oracle
   ** Identity Manager Repository
   **
   ** @param  account            the <code>User Account</code> the data should
   **                            be loaded.
   ** @param  processInstanceKey the internal system identifier of the process
   **                            data for this <code>User Object</code> to load.
   **
   ** @throws TaskException      if the operation fails
   */
  private void createSourceProcessData(final Account account, final long processInstanceKey)
    throws TaskException {

    final String method = "createSourceProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);

    if (account.entitlement() != null && account.entitlement().size() > 0)
      account.entitlement().clear();

    final ApplicationEntity[] server = this.sourceServer();
    if (server != null && server.length > 0) {
      tcFormInstanceOperationsIntf facade     = formInstanceFacade();
      final String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_ACCOUNT), account.name()};

      debug(method, ResolverBundle.format(ResolverMessage.MINIMG_ACCOUNT_ENTITLEMENT_BEGIN, parameter));

      String columnName = null;
      try {
        for (int i = 0; i < server.length; i++) {
          final ProcessForm[]   processForm = server[i].childForm();

          tcResultSet data = facade.getProcessFormData(processInstanceKey);
          int         size = data.getRowCount();
          if (size == 0)
            throw new ResolverException(ResolverError.NOT_PROVISIONED, parameter);
          if (size > 1)
            throw new ResolverException(ResolverError.AMBIGUOS_PROVISIONED, parameter);

          for(int j = 0; j < processForm.length; j++) {
            data = facade.getProcessFormChildData(processForm[j].key(), processInstanceKey);
            size = data.getRowCount();
            // it can happens that the account passed in has no entitlements
            // if we don't find any entitlements proceed with the next
            // entitlement form if available
            // at the end the instance attribute entitlement on the passed
            // account can be still null
            if (size == 0)
              continue;
            final String[] fieldName = data.getColumnNames();
            // we must transform the field names to a sorted vector to ensure
            // that the order how the values are fetched from the result set are
            // in the same order as this was done on the Access Policy.
            // This is achieved by transfering all field names to a TreeSet that
            // sorts the entries in natural order and transforme this collection
            // back in a array of strings to facilitate the iteration. The data
            // provided by getProcessFormChildData() returning a tcResultSet.
            // Therefore further casting to classes is not necessary.
            // Furthermore we must filter all default columns that are
            // returned back by getProcessFormChildData. This fields can never
            // be part of an Access Policy field assignment.
            final TreeSet<String> sorter = new TreeSet<String>();
            for (int l = 0; l < fieldName.length; l++)
              if (!processForm[j].isDefault(fieldName[l]))
                sorter.add(fieldName[l]);
            final Object[] field = sorter.toArray();

            // create an array of Entitlement that big enough to hold all
            // recordes found for the account
            // Entitlement content will hashed by the wrapper so dont bother
            // that we use a variable on the stack instead of creating every
            // time a new one in case a field set will be fetched from the
            // database
            final EntitlementEntity[] entitlement = new EntitlementEntity[size];

            // create an array of string that big enough to hold all values for
            // the columns contained in the fieldSet.
            // Entitlement content will hashed by the wrapper so dont bother
            // that we use a variable on the stack instead of creating every
            // time a new one in case a field set will be fetched from the
            // database
            final String[] value = new String[field.length];

            for (int k = 0; k < size; k++) {
              // navigate to the record in the result set
              data.goToRow(k);
              // the sequence how the values has to be fetched from the result
              // set are deterministic see above; we can build a simple for loop
              // to hold the values
              for (int l = 0; l < field.length; l++) {
                columnName = (String)field[l];
                value[l] = data.getStringValue(columnName);
              }
              final long key = data.getLongValue(processForm[j].fieldName(ProcessForm.KEY));
              // create the entitlement wrapper that aggregates all single
              // values in one hash value; the row instance key is important for
              // the data adjustment
              entitlement[k] = new EntitlementEntity(server[i], key, value);
            }
            // the entitlement mapping is using the key of the process form
            // instead of the process form name.
            // the Access Policies doesn't know the name of the process form
            // where they are providing the data for, only the key is know by
            // the policies.
            // only for convinient in the matching phase we use the key of the
            // process form for the account too.
            account.addEntitlement(String.valueOf(processForm[j].key()), entitlement);
          }
        }
        if (account.entitlement() == null || account.entitlement().isEmpty())
          debug(method, ResolverBundle.format(ResolverMessage.MINIMG_ACCOUNT_ENTITLEMENT_EMPTY, parameter));
        else
          debug(method, ResolverBundle.format(ResolverMessage.MINIMG_ACCOUNT_ENTITLEMENT_COMPLETE, parameter));
      }
      catch (tcFormNotFoundException e) {
        error(method, ResolverBundle.format(ResolverMessage.MINIMG_ACCOUNT_ENTITLEMENT_FAILED, parameter));
        throw TaskException.general(e);
      }
      catch (tcColumnNotFoundException e) {
        throw new TaskException(TaskError.COLUMN_NOT_FOUND, columnName);
      }
      catch (tcProcessNotFoundException e) {
        throw TaskException.general(e);
      }
      catch (tcNotAtomicProcessException e) {
        throw TaskException.general(e);
      }
      catch (tcAPIException e) {
        throw TaskException.unhandled(e);
      }
      finally {
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   matchSourceProcessData
  /**
   ** Matchs an {@link Account} to the <code>Access Policies</code> of the target servers.
   **
   ** @param  account            the {@link ApplicationAccount} to match
   */
  private void matchSourceProcessData(final Account account) {

    final String method = "matchSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    final ApplicationAccount[]                         server         = this.sourceServer();
    final Map<String, MultiSet<EntitlementEntity>> accountMapping = account.entitlement();
    // check if the entitlements are detected that has to be matched
    if (accountMapping == null || accountMapping.isEmpty()) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_ACCOUNT), account.name()};
      info(ResolverBundle.format(ResolverMessage.ACCOUNT_RESOURCE_CANDIATE, parameter));
      for (int i = 0; i < server.length; i++) {
        Iterator<AccessPolicy> j = server[i].policy().iterator();
        while (j.hasNext()) {
          AccessPolicy         policy = j.next();
          Iterator<RoleEntity> role   = policy.role().iterator();
          while (role.hasNext())
            // FIXME: we have to provide the process data that has to be
            // disjoind from the policy
            account.addRole(role.next());
        }
      }
    }
    else {
      Iterator<String> a = accountMapping.keySet().iterator();
      while (a.hasNext()) {
        final MultiSet<EntitlementEntity> accountEntitlement = accountMapping.get(a.next());
        if (accountEntitlement == null || accountEntitlement.isEmpty())
          continue;

        for (int i = 0; i < server.length; i++) {
          Iterator<AccessPolicy> j = server[i].policy().iterator();
          while (j.hasNext()) {
            AccessPolicy policy = j.next();
            final Map<String, MultiSet<EntitlementEntity>> policyMapping = policy.entitlement();
            if (policyMapping != null && policyMapping.size() > 0) {
              Iterator<String> p = policyMapping.keySet().iterator();
              while (p.hasNext()) {
                final MultiSet<EntitlementEntity> policyEntitlement = policyMapping.get(p.next());
                 if (policyEntitlement == null || policyEntitlement.isEmpty())
                   continue;

                // check if the entitlement set is completly containd in the
                // entitlement set assigned to the account (must be a subset)
                if (policyEntitlement.subSet(accountEntitlement)) {
                  List<EntitlementEntity>     rawdata         = new ArrayList<EntitlementEntity>(policyEntitlement.size());
                  Iterator<EntitlementEntity> policyIterator  = policyEntitlement.toSet().iterator();
                  Iterator<EntitlementEntity> accountIterator = accountEntitlement.toSet().iterator();
                  while (policyIterator.hasNext()) {
                    Object requested = policyIterator.next();
                    while (accountIterator.hasNext()) {
                      Object cursor = accountIterator.next();
                      if (requested.equals(cursor))
                        rawdata.add((EntitlementEntity)cursor);
                    }
                  }
                  EntitlementEntity[] entitlement = new EntitlementEntity[rawdata.size()];
                  entitlement = rawdata.toArray(entitlement);
                  // Assign all user groups that are triggering the policy as
                  // provisionable objects to the account
                  Iterator<RoleEntity> role = policy.role().iterator();
                  while (role.hasNext())
                    account.addEntitlement(role.next().name(), entitlement);
                }
              }
            }
          }
        }
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrofitProvisioning
  /**
   ** Provisions or enables Entitlements for an {@link ApplicationAccount}.
   **
   ** @param  account            the {@link ApplicationAccount} that is subject
   **                            of this operation.
   ** @param  objectInstance     the <code>Object Instance</code> of a
   **                            provisioned <code>Resource Object</code>.
   ** @param  processInstance    the <code>Process Instance</code> providing the
   **                            data of the account.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private boolean retrofitProvisioning(final Account account, final long objectInstance, final Long processInstance)
    throws TaskException {

    final String method = "retrofitProvisioning";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean                      changes   = false;
    final String[]               status    = { ResourceObject.STATUS_PROVISIONED, ResourceObject.STATUS_ENABLED };
    RoleEntity                   role      = null;
    AccessPolicy                 policy    = null;
    final RoleAdapter            operation = new RoleAdapter(this.provider);
    final AccessPolicyEvaluator  evaluator = new AccessPolicyEvaluator(this);

    // prepare the spool for output
    final String[] parameter = {
      TaskBundle.string(TaskMessage.ENTITY_ACCOUNT)
    , account.name()
    , null
    , null
    };
    try {
      Iterator<RoleEntity> roles = account.roleToAssign().iterator();
      while (roles.hasNext()) {
        role = roles.next();

        // prepare the spool for output
        parameter[2] = TaskBundle.string(TaskMessage.ENTITY_GROUP);
        parameter[3] = role.name();
        // check if the user group is already assigned to the account
        if (operation.isMember(role.key(), account.key())) {
          info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_NOACTION, parameter));
        }
        else {
          info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_STARTED, parameter));
          // if the user is not member of the gotup assign the group to the
          // user with out triggering the access policy
          operation.assignMember(role.key(), account.key(), false);
          debug(method, ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_PROVISIONED, parameter));
        }
        // join the process data with the policy
        long[] key = new long[1];
        final Collection<EntitlementEntity> collection = account.entitlementToAssign(role);
        for (EntitlementEntity entitlement : collection) {
          // Attention: now we adjust the process data provisined
          //            a resource object can have more than one policy that
          //            handles the resource object
          //            the assumption here is that a role or something like
          //            that is handled exactly by one policy
          // FIXME: Uncommnet if we had tested
          // processWrapper.joinEntitlementWithPolicy(userGroup.policy().key(), entitlement.server(), objectInstance, entitlement.key());
          key[0] = entitlement.key();
// ToDO:
//          join(role.policy(), entitlement.account(), key);
          // change overall action status that we changed an account
          changes = true;
        }
      }
      Iterator<ApplicationEntity> resources = account.applicationToAssign().iterator();
      while (resources.hasNext()) {
        final ApplicationEntity resource = resources.next();

        // prepare the spool for output
        parameter[2] = TaskBundle.string(TaskMessage.ENTITY_RESOURCE);
        parameter[3] = resource.name();

        if (account.applicationAssigned(resource, status)) {
          info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_NOACTION, parameter));
        }
        else {
          info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_STARTED, parameter));
          // provision the Resource Oject
          // we assume that the Resource Object takes care about group
          // membership
          userFacade().provisionObject(account.key(), resource.key());
          // add the provisioned resource to the collaction of assigned
          // resources to prevent that this will not be done again if
          // another group provisions the same resource object
          account.addAssignedApplication(resource);
          debug(method, ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_PROVISIONED, parameter));
          // change overall action status that we changed an account
          changes = true;
        }
      }
      roles = account.roleToAssign().iterator();
      while (roles.hasNext()) {
        role   = roles.next();
        policy = role.policy();
        // prepare the spool for output
        parameter[2] = TaskBundle.string(TaskMessage.ENTITY_POLICY);
        parameter[3] = policy.name();

        info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_STARTED, parameter));
        evaluator.evaluate(account, policy);
        debug(method, ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_PROVISIONED, parameter));
        changes = true;
      }
    }
    catch (tcUserNotFoundException e) {
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcObjectNotFoundException e) {
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcProvisioningNotAllowedException e) {
      throw TaskException.general(e);
    }
    catch (tcAPIException e) {
      throw TaskException.unhandled(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return changes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrofitRevocation
  /**
   ** Revokes or disables Entitlements assigned to an {@link Account}.
   **
   ** @param  account            the {@link Account} that is subject of this
   **                            operation.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private boolean retrofitRevocation(final Account account)
    throws TaskException {

    final String method = "retrofitRevocation";
    trace(method, SystemMessage.METHOD_ENTRY);

    tcUserOperationsIntf process   = userFacade();

    boolean              changes   = false;
    final String[]       status    = { ResourceObject.STATUS_PROVISIONED, ResourceObject.STATUS_ENABLED };
    RoleEntity           role      = null;
    ApplicationEntity    resource  = null;
    final RoleAdapter    operation = new RoleAdapter(this.provider());

    // prepare the spool for output
    final String[] parameter = {
      TaskBundle.string(TaskMessage.ENTITY_ACCOUNT)
    , account.name()
    , null
    , null
    };

    try {
      Iterator<RoleEntity> roles = account.roleToRevoke().iterator();
      while (roles.hasNext()) {
        role = roles.next();

        // prepare the spool for output
        parameter[2] = TaskBundle.string(TaskMessage.ENTITY_GROUP);
        parameter[3] = role.name();
        // check if the user group is already assigned to the account
        if (!operation.isMember(role.key(), account.key())) {
          info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_NOACTION, parameter));
        }
        else {
          info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_REVOKED, parameter));
          // if the user is member of the group revoke the group from the user
          // with out triggering the access policy
          // Note: this doesn't work correctly because the core API does not
          //       support the operation of removing a groupmembership without
          //       triggering the access policy
          operation.removeMember(role.key(), account.key(), false);
          debug(method, ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_PROVISIONED, parameter));
          // change overall action status that we changed an account
          changes = true;
        }
      }
      Iterator<ApplicationEntity> resources = account.applicationToRevoke().iterator();
      while (resources.hasNext()) {
        resource = resources.next();

        // prepare the spool for output
        parameter[2] = TaskBundle.string(TaskMessage.ENTITY_RESOURCE);
        parameter[3] = resource.name();

        // check if the target system is already provisioned
        if (!account.applicationAssigned(resource, status)) {
          info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_NOACTION, parameter));
        }
        else {
          debug(method, ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_REVOKED, parameter));
          // revoke the Resource Oject
          // we assume that the Resource Object takes care about group
          // membership
          process.revokeObject(account.key(), resource.key());
          debug(method, ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_PROVISIONED, parameter));
          // change overall action status that we changed an account
          changes = true;
        }
      }
    }
    catch (tcUserNotFoundException e) {
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcObjectNotFoundException e) {
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcRevocationNotAllowedException e) {
      throw TaskException.general(e);
    }
    catch (tcAPIException e) {
      throw TaskException.unhandled(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return changes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preConditions
  /**
   ** Performs all necessary Pre-Condition Chekcs.
   **
   ** @throws ResolverException  in case an error does occur.
   */
  protected final void preConditions()
    throws ResolverException {

    createRoles();
    if (preConditionViolated())
      return;

    createPolicies();
    if (preConditionViolated())
      return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRoles
  /**
   ** Load the all <code>Resource Object</code>s that depends on the loaded
   ** servers.
   **
   ** @throws ResolverException  in case an error does occur.
   */
  private void createRoles() {

    final String method = "createRoles";
    trace(method, SystemMessage.METHOD_ENTRY);

    info(ResolverBundle.format(ResolverMessage.PRECONDITION_ROLE_MAPPING_BEGIN, reconcileObject()));
    try {
      final ApplicationEntity[] server = this.sourceServer();
      for (int i = 0; !preConditionViolated() && i < server.length; i++) {
        ResourceFactory.createRole(this, server[i]);
        if (!server[i].isOwner()) {
          info(ResolverBundle.format(ResolverMessage.PRECONDITION_ROLE_MAPPING_EMPTY, server[i].name()));
          preConditionViolated(true);
        }
      }
    }
    catch (ResolverException e) {
      preConditionViolated(true);
      error(method, e.getLocalizedMessage());
    }

    if (preConditionViolated())
      info(ResolverBundle.format(ResolverMessage.PRECONDITION_ROLE_MAPPING_FAILED, reconcileObject()));
    else
      info(ResolverBundle.format(ResolverMessage.PRECONDITION_ROLE_MAPPING_COMPLETE, reconcileObject()));

    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Updates the provisioned entitlements for an account in a way that the
   ** system identifier of the <code>Access Policy</code> is updates.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private int join(final AccessPolicy policy, final ApplicationEntity server, final long[] rowkey)
    throws TaskException {

    StringBuilder query = new StringBuilder();
    query.append("select sdk.sdk_name ");
    query.append("from sdh sdh,poc poc,sdk sdk, sdk sdp ");
    query.append("where sdk.sdk_type='P' and poc.pol_key=? ");
    query.append("and poc.sdk_key=sdk.sdk_key ");
    query.append("and sdh_parent_version=sdp.sdk_active_version ");
    query.append("and sdh_child_version=sdk.sdk_active_version ");
    query.append("and sdh.sdh_parent_key=poc.poc_parent_sdk_key ");
    query.append("and sdp.sdk_key=poc.poc_parent_sdk_key ");
    query.append("and poc.obj_key=? order by poc_record_number");

    PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(provider(), query.toString());
    statement.setLong(1, policy.key());
    statement.setLong(2, server.key());

    int rows = 0;
    try {
      statement.execute();

      final tcDataSet sdkData = statement.getDataSet();
      final int       sdkSize = sdkData.getRowCount();
      for (int i = 0; i < sdkSize; i++) {
        statement.setStatement(provider(), preparePolicyStatement(sdkData.getString("sdk_name"), rowkey));

        int binding = 0;
        statement.setLong(++binding, policy.key());
        for (int j = 0; j < rowkey.length; j ++)
          statement.setLong(++binding, rowkey[j]);
        statement.executeUpdate();
        rows += statement.getRowsUpdated();
      }
      return rows;
    }
    catch (tcDataAccessException e) {
      throw TaskException.general(e);
    }
    catch (tcDataSetException e) {
      throw TaskException.general(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareDataQuery
  /**
   ** Build the SQL Query to fetch affected entries.
   **
   ** @return                    the SQL Query to fetch affected entries.
   */
  private StringBuilder prepareDataQuery() {
    StringBuilder query = new StringBuilder();
    query.append("select USR.USR_KEY,USR.USR_LOGIN,ORC.ORC_KEY,ORC.ORC_PACKAGE_INSTANCE_KEY ");
    query.append(prepareWhereClause());
    return query;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareWhereClause
  /**
   ** Build the SQL Where Clause for the diffrent queries.
   **
   ** @return                    the SQL Where Clause for the diffrent queries.
   */
  private StringBuilder prepareWhereClause() {
    StringBuilder clause = new StringBuilder();
    clause.append("from obj obj,ost ost,pkg pkg,tos tos,orc orc,obi obi,usr usr,oiu oiu,act act where ");

    final ApplicationEntity[] server = this.sourceServer();
    if (server.length == 1) {
      clause.append("obj.obj_key = ? ");
    }
    else {
      clause.append("obj.obj_key in (");
      for (int i = 0; i < server.length; i++) {
        if (i > 0)
          clause.append(", ");
        clause.append("?");
      }
      clause.append(") ");
    }

    clause.append("and pkg.obj_key  = obj.obj_key ");
    // asked only for process definition that are of type Provisioning
    clause.append("and pkg.pkg_type = ? ");
    // asked only for resources that are not revoked
    clause.append("and ost.ost_status != ? ");
    clause.append("and tos.pkg_key = pkg.pkg_key ");
    clause.append("and orc.tos_key = tos.tos_key ");
    clause.append("and obi.obj_key = obj.obj_key ");
    clause.append("and oiu.obi_key = obi.obi_key ");
    clause.append("and oiu.orc_key = orc.orc_key ");
    clause.append("and oiu.ost_key = ost.ost_key ");
    clause.append("and usr.usr_key = orc.usr_key ");
    clause.append("and usr.act_key = act.act_key ");
    clause.append("and (oiu.oiu_create >= ? or oiu.oiu_update >= ?)");

    final String userFilter = stringValue(FILTER_IDENTITY);
    if (!StringUtility.isEmpty(userFilter)) {
      clause.append(" and UPPER(usr.usr_login) ");
      userWildcardPos = userFilter.indexOf("*");
      if (userWildcardPos != -1)
        clause.append("like ");
      else
        clause.append("= ");
      clause.append("?");
    }

    final String orgFilter = stringValue(FILTER_ORGANIZATION);
    if (!StringUtility.isEmpty(orgFilter)) {
      clause.append(" and usr.act_key in (select act.act_key from act where act.act_name ");
      orgWildcardPos = orgFilter.indexOf("*");
      if (orgWildcardPos != -1)
        clause.append("like ");
      else
        clause.append("= ");
      clause.append("?)");
    }
    return clause;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparePolicyStatement
  /**
   ** Build the SQL statement to update affected entries.
   **
   ** @return                    the SQL statement to update affected entries.
   */
  private String preparePolicyStatement(final String form, final long[] value) {
    StringBuilder query = new StringBuilder("update ");
    query.append(form);
    query.append(" set pol_key = ?");
    query.append(preparePolicyClause(form + "_KEY", value));
    return query.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparePolicyClause
  /**
   ** Build the SQL Where Clause for the diffrent queries.
   **
   ** @return                    the SQL Where Clause for the diffrent queries.
   */
  private StringBuilder preparePolicyClause(final String keyField, final long[] keyValue) {
    StringBuilder clause = new StringBuilder(" where ");
    clause.append(keyField);
    if (keyValue.length == 1) {
      clause.append(" = ? ");
    }
    else {
      clause.append(" in (");
      for (int i = 0; i < keyValue.length; i++) {
        if (i > 0)
          clause.append(", ");
        clause.append("?");
      }
      clause.append(")");
    }
    return clause;
  }
}