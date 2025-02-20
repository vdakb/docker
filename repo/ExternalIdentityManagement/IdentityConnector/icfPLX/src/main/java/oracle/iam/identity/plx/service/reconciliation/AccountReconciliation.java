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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   AccountReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.plx.service.reconciliation;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcObjectNotFoundException;
import Thor.API.tcResultSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.connector.service.EntityReconciliation;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.ldap.DirectoryReference;
import oracle.iam.identity.foundation.naming.ResourceObject;
import oracle.iam.identity.foundation.resource.TaskBundle;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
////////////////////////////////////////////////////////////////////////////////
// class AccountReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountReconciliation</code> acts as the service end point
 ** for Identity Manager to reconcile account information from a
 ** <code>Service Provider</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccountReconciliation extends EntryReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify the base
   ** context of the group on the target system.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String GROUP_BASE     = "Group Base Context";
  /**
   ** Attribute tag which may be defined on this task to specify the base
   ** context of the role on the target system.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String ROLE_BASE     = "Role Base Context";
  /**
   ** Attribute tag which may be defined on this task to specify the base
   ** context of the scope on the target system.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String SCOPE_BASE     = "Scope Base Context";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,          TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,     TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies the search scope of the query
     ** <br>
     ** Allowed values are OneLevel | SubTree | Object
     */
  , TaskAttribute.build(SEARCH_SCOPE,         TaskAttribute.MANDATORY)
    /** the task attribute that specifies the search base of the query */
  , TaskAttribute.build(SEARCH_BASE,          SystemConstant.EMPTY)
    /** the task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCH_FILTER,        SystemConstant.EMPTY)
    /** the task attribute that specifies the search base of the group to be
     ** applied to the query */
  , TaskAttribute.build(GROUP_BASE,           SystemConstant.EMPTY)
    /** the task attribute that specifies the search base of the role to be
     ** applied to the query */
  , TaskAttribute.build(ROLE_BASE,           SystemConstant.EMPTY)
    /** the task attribute that specifies the search base of the scope to be
     ** applied to the query */
  , TaskAttribute.build(SCOPE_BASE,          SystemConstant.EMPTY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,          SystemConstant.TRUE)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountReconciliation</code> scheduler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountReconciliation() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupContext
  /**
   ** Returns the base DN where the group container is located.
   ** <br>
   **
   ** @return                  the base DN where the group container is located.
   */
  public final String groupContext() {
    return stringValue(GROUP_BASE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   scopeContext
  /**
   ** Returns the base DN where the scope container is located.
   ** <br>
   **
   ** @return                  the base DN where the scope container is located.
   */
  public final String scopeContext() {
    return stringValue(SCOPE_BASE);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleContext
  /**
   ** Returns the base DN where the role container is located.
   ** <br>
   **
   ** @return                   the base DN where the role container is located.
   */
  public final String roleContext() {
    return stringValue(ROLE_BASE);
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
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Directory Service.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, reconcileObject(), getName(), resourceName()));

    // obtain the state of the job before any of the attributes are subject of
    // change
    final OperationOptions           options = operationOptions();
    final EntityReconciliation.Batch handler = new EntityReconciliation.Batch(this, this.threadPoolSize(), ignoreDublicates());
    // set the current date as the timestamp on which this task has been last
    // reconciled at the start of execution
    // setting it here to have it the next time this scheduled task will
    // run the changes made during the execution of this task
    // updating this attribute will not perform to write it back to the
    // scheduled job attributes it's still in memory; updateLastReconciled()
    // will persist the change that we do here only if the job completes
    // successful
    lastReconciled(systemTime());
    try {
      this.connector.search(ObjectClass.ACCOUNT, filter(), handler, options);
      // process the events which might be left open
      handler.finish();
    }
    catch (ConnectorException e) {
      throw operationalException(e);
    }
    finally {
      // inform the observing user about the overall result of this task
      if (isStopped()) {
        final String[] arguments = { reconcileObject(), getName(), resourceName(), "Veto"};
        warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
      }
      // if an exception occured
      else if (getResult() != null) {
        final String[] arguments = { reconcileObject(), getName(), resourceName(), getResult().getLocalizedMessage()};
        error("onExecution", TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
      }
      // complete with success and write back timestamp
      else {
        // update the timestamp on the task
        updateLastReconciled();
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName(), resourceName()));
      }
    
    info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName(), resourceName()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method groupd by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** The call back method just invoked before reconciliation takes place.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException    in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    final String method  = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // iterate over the reconciliation field and check for multi-valued
    // attributes
    String columnName = null;
    String fieldName  = null;
    // get the reconciliation field definition from the Resource Object handled
    // be this task
    Map filter = new HashMap(1);
    filter.put(ResourceObject.NAME, reconcileObject());
    try {
      tcResultSet resultSet = null;
      // retrieve the reconciliation field definition of the Resource Object
      // that this task will reconciling
      try {
        resultSet = this.objectFacade().findObjects(filter);
        if (resultSet.getRowCount() == 0)
          throw new TaskException(TaskError.RESOURCE_NOT_FOUND, reconcileObject());
        if (resultSet.getRowCount() > 1)
          throw new TaskException(TaskError.RESOURCE_AMBIGUOUS, reconcileObject());

        resultSet = this.objectFacade().getReconciliationFields(resultSet.getLongValue(ResourceObject.KEY));
        // if no fields defined abort the execution of this task by throwing an
        // appropriate exception
        if (resultSet.getRowCount() == 0)
          throw new TaskException(TaskError.RESOURCE_RECONFIELD, reconcileObject());
      }
      catch (tcColumnNotFoundException e) {
        throw new TaskException(TaskError.COLUMN_NOT_FOUND, ResourceObject.KEY);
      }
      catch (tcObjectNotFoundException e) {
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, reconcileObject());
      }

      final Set<String> reference = this.descriptor.referenceTarget();
      try {
        for (int i = 0; i < resultSet.getRowCount(); i++) {
          resultSet.goToRow(i);
          columnName  = ResourceObject.RECON_FIELD_TYPE;
          if (DirectoryReference.MULTI_VALUE_TYPE.equals(resultSet.getStringValue(columnName))) {
            columnName = ResourceObject.RECON_FIELD_NAME;
            fieldName  = resultSet.getStringValue(columnName);

            if (!(reference.contains(fieldName))) {
              String[] arguments = { reconcileObject(), fieldName };
              warning(TaskBundle.format(TaskError.RESOURCE_RECON_MULTIVALUE, arguments));
            }
          }
        }
      }
      catch (tcColumnNotFoundException e) {
        throw new TaskException(TaskError.COLUMN_NOT_FOUND, columnName);
      }
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}