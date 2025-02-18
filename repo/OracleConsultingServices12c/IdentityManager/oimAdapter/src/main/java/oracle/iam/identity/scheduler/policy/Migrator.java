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

    File        :   Migrator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Migrator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

import java.util.Iterator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcFormNotFoundException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Exceptions.tcNotAtomicProcessException;
import Thor.API.Exceptions.tcProcessNotFoundException;
import Thor.API.Exceptions.tcRequiredDataMissingException;
import Thor.API.Exceptions.tcUserNotFoundException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AttributeTransformation;

import oracle.iam.identity.foundation.naming.ResourceObject;
import oracle.iam.identity.foundation.naming.ProcessDefinition;

import oracle.iam.identity.foundation.offline.Account;
import oracle.iam.identity.foundation.offline.Application;
import oracle.iam.identity.foundation.offline.ApplicationAccount;
import oracle.iam.identity.foundation.offline.ApplicationEntity;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.model.Configuration;

import oracle.iam.identity.resource.ResolverBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Migrator
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** The <code>Migrator</code> act as the service end point for the Oracle
 ** Identity Manager to resolve <code>Access Policies</code>.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class Migrator extends Reconciliation {

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

  private ApplicationAccount[]    targetServer    = null;

  /** the mapping of attribute names from the source to the target system */
  private AttributeMapping        attributeMapping;

  /**
   ** the mapping of attribute trasnformation from the source to the target
   ** system
   */
  private AttributeTransformation transformationMapping;

  /**
   ** the flag indicating that the transformation of one or more attributes is
   ** required.
   */
  private boolean                 transformationEnabled;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Migrator</code> task adpater that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Migrator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   targetServer
  /**
   ** Returns the List of target server with this task.
   **
   ** @return             the List of server with this task.
   */
  protected final ApplicationAccount[] targetServer() {
    return this.targetServer;
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
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  protected void onExecution()
    throws TaskException {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeStatement (Reconciliation)
  /**
   ** Executes the SQL statement.
   ** <p>
   ** Before the statement is executed all values are bound to the placeholders.
   **
   ** @return                    the result set of the executed query.
   **
   ** @throws ResolverException  if the bind or execution fails.
   */
  protected ResultSet executeStatement(final PreparedStatement statement)
    throws ResolverException {

    // TODO: create implementation
    return null;
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
    return query;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrofitProvisioning
  /**
   ** Provisions or enables Entitlements for an {@link ApplicationAccount}.
   **
   ** @param  account            the {@link ApplicationAccount} that is subject
   **                            of this operation.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private boolean retrofitProvisioning(final Account account)
    throws TaskException {

    final String method = "retrofitProvisioning";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean                           changes         = false;
    final String[]                    status          = { ResourceObject.STATUS_PROVISIONED, ResourceObject.STATUS_ENABLED };
    ApplicationEntity                 resource        = null;
    final Iterator<ApplicationEntity> accountResource = account.applicationToAssign().iterator();

    // prepare the spool for output
    final String[] parameter = {
      TaskBundle.string(TaskMessage.ENTITY_ACCOUNT)
    , account.name()
    , null
    , null
    };
    try {
      while (accountResource.hasNext()) {
        resource = accountResource.next();

        // prepare the spool for output
        parameter[2] = TaskBundle.string(TaskMessage.ENTITY_RESOURCE);
        parameter[3] = resource.name();

        // check if the target system is already provisioned
        if (account.applicationAssigned(resource, status)) {
          info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_NOACTION, parameter));
        }
        else {
          info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_STARTED, parameter));
          // lookup if the resource already ordered for the new account
          long processInstance = createProcessInstance(account, resource, stringValue(OBJECTTYPE), stringValue(OBJECTSTATUS));
          // if the resource is not already provisioned order the resource for
          // the new account
          if (processInstance == 0L) {
            processInstance = provisionResource(account, resource);
            info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_ORDERED, parameter));
            // provision the account with the dataset
            provisionTargetProcess(account, processInstance);
            info(ResolverBundle.format(ResolverMessage.OBJECT_PROVISIONING_PROVISIONED, parameter));
          }
          changes = true;
        }
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return changes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTargetProcessData
  /**
   ** Adds the specified <code>Attribute</code> to the {@link Map} of attributes
   ** that should be provisioned to the <code>User Account</code> in the target
   ** system.
   **
   ** @param  account            the <code>User Account</code> the data should
   **                            be created for.
   ** @param  configuration      the name of the {@link Configuration} that
   **                            contains all information to copy data from the
   **                            source system to the target system.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  private void createTargetProcessData(final Account account, final Configuration configuration)
    throws ResolverException {

    final String method = "createTargetProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      // filter data in a new mapping
      account.targetData(this.attributeMapping.filterByEncoded(account.sourceData()));
      if (account.targetData().isEmpty())
        throw new ResolverException(ResolverError.NO_INBOUND_MAPPING);

//      account.targetData().put("UD_B2B_USR_SERVER",     Long.toString(configuration.targetDirectoryServer().key()));
//      account.targetData().put("UD_B2B_USR_PASSWORD",   (String)account.profileData().get("USR.USR_FSS"));
//      account.targetData().put("UD_B2B_USR_GUID",       (String)account.profileData().get("USR.USR_UDF_GUID"));
//      account.targetData().put("UD_B2B_USR_COMPANY",    (String)account.profileData().get("ACT.ACT_NAME"));

      // apply transformation if needed
      if (this.transformationEnabled) {
        account.targetData(this.transformationMapping.transform(account.targetData()));
        if (account.targetData().isEmpty())
          throw new ResolverException(ResolverError.NO_OUTBOUND_MAPPING);
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisionTargetProcess
  /**
   ** Returns the <code>Process Instance Key</code> created by a direct
   ** provisioning action.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private void provisionTargetProcess(final Account account, long processInstance)
    throws TaskException {

    final String method = "provisionTargetProcess";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      formInstanceFacade().setProcessFormData(processInstance, account.targetData());
    }
    catch (tcFormNotFoundException e) {
      throw TaskException.general(e);
    }
    catch (tcProcessNotFoundException e) {
      throw TaskException.general(e);
    }
    catch (tcNotAtomicProcessException e) {
      throw TaskException.general(e);
    }
    catch (tcInvalidValueException e) {
      throw TaskException.general(e);
    }
    catch (tcRequiredDataMissingException e) {
      throw TaskException.general(e);
    }
    catch (tcAPIException e) {
      throw TaskException.unhandled(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessInstance
  /**
   ** Creates the <code>Process Instance Key</code> for the specified
   ** {@link Application} by a direct provisioning action on the specified
   ** <code>User Account</code>.
   **
   ** @param  account            the <code>User Account</code> the Oracle
   **                            Identity Manager object should be assigned to.
   ** @param  resource           the Oracle Identity Manager object that should
   **                            be provisioned to the
   **                            <code>User Account</code>.
   ** @param  objectType         to advice which object type should be handled.
   ** @param  objectStatus       to advice which object status should be handled.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private final long createProcessInstance(final Account account, final Application resource, final String objectType, final String objectStatus)
    throws TaskException {

    long processInstance =  0L;
    String columnName    = null;
    try {
      tcResultSet resultSet = userFacade().getObjectsByTypeStatus(account.key(), objectType, objectStatus);
      int         rowCount  = resultSet.getRowCount();
      for (int i = 0; i < rowCount; i++) {
        resultSet.goToRow(i);
        columnName = ResourceObject.NAME;
        if (resource.name().equals(resultSet.getStringValue(columnName))) {
          columnName       = ProcessDefinition.INSTANCE_KEY;
          processInstance  = resultSet.getLongValue(columnName);
          // ask for the data
          final tcResultSet data  = formInstanceFacade().getProcessFormData(processInstance);
          final int         count = data.getRowCount();
          // if we got nothing the account is not provisioned to the user
          if (count == 0) {
            String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_ACCOUNT), account.name() };
            throw new ResolverException(ResolverError.NOT_PROVISIONED, parameter);
          }
          // if we got more than once the provisioned object is ambigous
          // assigned to the user
          if (count > 1) {
            String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_ACCOUNT), account.name() };
            throw new ResolverException(ResolverError.AMBIGUOS_PROVISIONED, parameter);
          }
        }
      }
    }
    catch (tcUserNotFoundException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_IDENTITY), account.name()};
      throw new ResolverException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcFormNotFoundException e) {
      throw TaskException.general(e);
    }
    catch (tcProcessNotFoundException e) {
      throw TaskException.general(e);
    }
    catch (tcNotAtomicProcessException e) {
      throw TaskException.general(e);
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, columnName);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    return processInstance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processInstance
  /**
   ** Returns the <code>Process Instance Key</code> created by a direct
   ** provisioning action.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private long processInstance(final ApplicationAccount account, long objectInstance, final String instanceType)
    throws TaskException {

    long   processInstance = 0L;
    String columnName      = null;
    try {
      final tcResultSet resultSet = userFacade().getObjects(account.key());
      final int         rowCount  = resultSet.getRowCount();
      if (instanceType.equals("OBI")) {
        for (int i = 0; i < rowCount; i++) {
          resultSet.goToRow(i);
          columnName = ResourceObject.OBJECT_INSTANCE;
          if (resultSet.getLongValue(columnName) == objectInstance) {
            columnName = ProcessDefinition.INSTANCE_KEY;
            processInstance = resultSet.getLongValue(columnName);
            break;
          }
        }
      }
      else if (instanceType.equals("OIU")) {
        for (int i = 0; i < rowCount; i++) {
          resultSet.goToRow(i);
          columnName = ResourceObject.USER_INSTANCE;
          if (resultSet.getLongValue(columnName) == objectInstance) {
            columnName = ProcessDefinition.INSTANCE_KEY;
            processInstance = resultSet.getLongValue(columnName);
            break;
          }
        }
      }
      else
       processInstance = 0L;
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, columnName);
    }
    catch (tcUserNotFoundException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_IDENTITY), account.name()};
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    return processInstance;
  }
}