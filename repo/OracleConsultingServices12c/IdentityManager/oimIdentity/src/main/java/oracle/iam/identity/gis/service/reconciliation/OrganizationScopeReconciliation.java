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

    File        :   OrganizationScopeReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OrganizationScopeReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.service.reconciliation;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.util.Set;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.identity.utils.Constants;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.exception.OrganizationManagerException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationScopeReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrganizationScopeReconciliation</code> acts as the service end
 ** point for the Oracle Identity Manager to reconcile organisation information
 ** from Identity Manager itself.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class OrganizationScopeReconciliation extends LookupReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  protected static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,      TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT, TaskAttribute.MANDATORY)
    /**
     ** the task attribute with reconciliation operation
     ** <br>
     ** Allowed values Refresh | Update
     */
  , TaskAttribute.build(OPERATION,        TaskAttribute.MANDATORY)
    /** the task attribute to resolve the name of the Lookup Group */
  , TaskAttribute.build(LOOKUPGROUP,      TaskAttribute.MANDATORY)
    /** the task attribute to resolve the encoded value */
  , TaskAttribute.build(ENCODEDVALUE,     TaskAttribute.MANDATORY)
    /** the task attribute to resolve the decoded value */
  , TaskAttribute.build(DECODEDVALUE,     TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OrganizationScopeReconciliation</code> scheduled
   ** task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OrganizationScopeReconciliation() {
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
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateChanges (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation by fetching the
   ** data from the target system.
   **
   ** @param  bulkSize           the size of a block processed in a thread
   ** @param  returning          the attributes whose values have to be returned.
   **                            Set it to <code>null</code>, if all attribute
   **                            values have to be returned
   **
   ** @throws TaskException      if the operation fails
   */
  @Override
  protected void populateChanges(final int bulkSize, final Set<String> returning)
    throws TaskException {

    // check if a request to stop the execution is pending and return without
    // further actions if it evaluates to true
    if (isStopped())
      return;

    final String method = "populateChanges";
    trace(method, SystemMessage.METHOD_ENTRY);
    // create a task timer to gather performance metrics
    timerStart(method);

    // set the current date as the timestamp on which this task was last
    // reconciled at start
    // setting it at this time that we have next time this scheduled task will
    // run the changes made during the execution of this task
    lastReconciled(this.server.systemTime());

    final Batch               batch     = new Batch(batchSize());
    final Map<String, Object> control   = new HashMap<String, Object>();

    // prepare a filter that fetch only those organization that are allowed for
    // an identity
    final SearchCriteria      filter    = new SearchCriteria(
      new SearchCriteria(OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), "Xellerate Users", SearchCriteria.Operator.NOT_EQUAL)
    , new SearchCriteria(OrganizationManagerConstants.AttributeName.ORG_TYPE.getId(), "Requests",        SearchCriteria.Operator.NOT_EQUAL)
    , SearchCriteria.Operator.AND
    );
    // set up the attribute names to be returned from the search
    final String[]            attribute = returning.toArray(new String[0]);
    final OrganizationManager facade    = this.server.service(OrganizationManager.class);
    try {
      List<Organization> result = null;
      do {
        // check if a request to stop the execution is pending and return without
        // further actions if it evaluates to true
        if (isStopped())
          break;

        control.put(Constants.SEARCH_STARTROW, new Integer(batch.start()));
        control.put(Constants.SEARCH_ENDROW,   new Integer(batch.end()));
        info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));
        result = facade.search(filter, CollectionUtility.set(returning), control);
        info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
        // if the result set is empty and the request batch is still on start
        // position we have no changes
        if (result.size() == 0 && batch.start() == 1)
          info(TaskBundle.string(TaskMessage.NOTHING_TO_CHANGE));
        else if (result.size() > 0) {
          info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(result.size())));
          // if we should really do reconcile ?
          if (gatherOnly()) {
            info(TaskBundle.string(TaskMessage.RECONCILE_SKIP));
            incrementIgnored(result.size());
          }
          else {
            for (Organization entity : result) {
              // check if a request to stop the execution is pending and
              // return without further actions if it evaluates to true
              if (isStopped())
                break;

              mergeLookup((String)entity.getAttribute(attribute[0]), (String)entity.getAttribute(attribute[1]));
            }
            info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
          }
        }
        // position the batch to the next view
        batch.next();
      } while (batch.size() <= result.size());
    }
    catch (OrganizationManagerException e) {
      throw new TaskException(e);
    }
    finally {
      this.server.disconnect();
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}