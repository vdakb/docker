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

    File        :   TargetReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TargetReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.service.reconciliation;

import java.util.Map;
import java.util.List;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseReference;

import oracle.iam.identity.foundation.reconciliation.Descriptor;

////////////////////////////////////////////////////////////////////////////////
// class TargetReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TargetReconciliation</code> acts as the service end point for
 ** the Oracle Identity Manager to reconcile account information from a
 ** Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class TargetReconciliation extends AccountReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute       = {
    /** the task attribute that holds the value of the last run */
    TaskAttribute.build(TIMESTAMP,        TaskAttribute.MANDATORY)
    /** the task attribute IT Resource */
  , TaskAttribute.build(IT_RESOURCE,      TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT, TaskAttribute.MANDATORY)
  , TaskAttribute.build(INCREMENTAL,      SystemConstant.TRUE)
  , TaskAttribute.build(EXCLUDED_USER)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TargetReconciliation</code> scheduled task
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TargetReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for reconciliation for an particular
   ** subject.
   **
   ** @param  username           the name of the user to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected void processSubject(final String username)
    throws TaskException {

    // check if the current thread is able to execute or a stop signal might be
    // pending
    if (isStopped())
       return;

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    // initialize the eventKey for further processing and reconcile data
    long eventKey = -1L;
    try {
      Map<String, Object> master = this.connection.accountDetail(username, returningAttributes());
      master = transformMaster(createMaster(master, true));
      // handle the account form at first to check if changes are detected that
      // requires to create an event
      if (!ignoreEvent(master)) {
        // create a reconciliation event where child data may be appended to
        eventKey = changelogEvent(master, false);
        // produce the logging output only if the logging level is enabled for
        if (this.logger.debugLevel())
          debug(method, TaskBundle.format(TaskMessage.EVENT_CREATED, Long.toString(eventKey)));
      }

      final Map<String, Object> reference = this.descriptor.entitlement();
      // for each multi-valued attribute object in the map, resolve the
      // reference and assign it to the owning object
      if (!reference.isEmpty()) {
        for (String permissionName : reference.keySet()) {
          DatabaseReference permission = (DatabaseReference)reference.get(permissionName);
          // populate the permission of the account and add the retrieved data
          // to the event
          // if a new event has to be opened it is created by addEventData and
          // obtained in the variabled passed to the call to retain on the event
          eventKey = processPermission(eventKey, master, permissionName, permission);
        }
      }

      // handle the identity form at first to check if changes are detected that
      // requires to create an event
      if (eventKey != -1L)
        closeEvent(eventKey);
      else
        warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, this.descriptor.identifier(), username));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from a path.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal()
    throws TaskException {

    final String method = "unmarshal";
    trace(method, SystemMessage.METHOD_ENTRY);

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processPermission
  /**
   ** Do all action which should take place for reconciliation of privileges,
   ** roles and/or object permissions where the specified <code>master</code>
   ** data is assigned to.
   **
   ** @param  event              the key for the reconciliation event the data
   **                            has to be added to.
   **                            if <code>-1</code> is passed and the event
   **                            cannot be ignored a new event will be created
   **                            for the master data and the created event key
   **                            will be returned.
   ** @param  permissionId       the reconciliation identifier of the child data
   **                            set.
   ** @param  master             the mapping containing the field-value pairs
   **                            for the owning data received from the target
   **                            system to match the account the child data
   **                            belonging to.
   ** @param  permission         the descriptor of the permission to handle;
   **                            must not be <code>null</code>.
   **
   ** @return                    the key for the reconciliation event to
   **                            process.
   **
   ** @throws TaskException      if the operation fails for any reason.
   */
  protected long processPermission(final long event, final Map<String, Object> master, final String permissionId, final DatabaseReference permission)
    throws TaskException {

    final String method = "processPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    // try to resolve all references that the specified entry can have
    try {
      // populate the permission of the account and add the retrieved data
      // to the mapping
      List<Map<String, Object>> content = populatePermission((String)master.get(this.descriptor.identifier()), permission);
      for (int j = 0; j < content.size(); j++) {
        Map<String, Object> record = permission.attributeMapping().filterByEncoded(content.get(j));
        if (record.isEmpty())
          throw new TaskException(TaskError.ATTRIBUTE_MAPPING_EMPTY);

        if (permission.transformationEnabled()) {
          record = permission.transformationMapping().transform(record);
          if (record.isEmpty())
            throw new TaskException(TaskError.TRANSFORMATION_MAPPING_EMPTY);
        }
        content.set(j, record);
      }
      // if a new event has to be opened it is created by addEventData and
      // obtained in the variabled passed to the call to retain on the
      // event
      return addEventData(event, master, permissionId, content);
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}