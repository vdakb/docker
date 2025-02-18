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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Directory Service Connector

    File        :   DeleteReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DeleteReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.reconciliation;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.NamingEnumeration;

import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;

import oracle.iam.platform.Platform;

import oracle.iam.reconciliation.config.vo.Form;
import oracle.iam.reconciliation.config.vo.Profile;
import oracle.iam.reconciliation.config.vo.TargetAttribute;

import oracle.iam.reconciliation.config.api.ProfileManager;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AttributeMapping;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.ldap.DirectoryConnector;

////////////////////////////////////////////////////////////////////////////////
// abstract class DeleteReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DeleteReconciliation</code> acts as the service end point for the
 ** Oracle Identity Manager to reconcile deleted accounts, organizations, roles
 ** and group information from a Directory Service.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
abstract class DeleteReconciliation extends ObjectReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify the action to
   ** perform if a deletion is detected.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String RECONCILE_OPERATION = "Reconciliation Operation";

  /**
   ** Attribute value which may be defined on this task to specify that a
   ** Delete operation has to be performed.
   */
  protected static final String OPERATION_DELETE    = "Deleted";

  /**
   ** Attribute value which may be defined on this task to specify that a
   ** Disable operation has to be performed.
   */
  protected static final String OPERATION_DISABLE   = "Disabled";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the encoded operation to perform if a deletion is detected in the source
  private String     operation;

  // the attribute name that identifies an entry in the target system
  private String     identifierRDN;

  // the mapping between reconcilaition fields and attributes of the process
  // form
  private String[][] reconciliationMapping;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DeleteReconciliation</code> task adpater that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected DeleteReconciliation() {
    // ensure inheritance
    super(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconciliationMapping
  /**
   ** Returns the mapping between the reconciliation attributes and the
   ** attributes map to those attributes in the process form.
   ** <p>
   ** Each element in the returning array is an array of two strings. The
   ** element with index <code>0</code> is the column name of the process form.
   ** The element with index <code>1</code> is the name of the attribute on the
   ** Resource Object.
   **
   ** @return                    the mapping between the reconciliation
   **                            attributes and the attributes map to those
   **                            attributes in the process form.
   */
  protected final String[][] reconciliationMapping() {
    return this.reconciliationMapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do tusted reconciliation of Oracle Identity Manager
   ** Organizations.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected final void processSubject(Map<String, Object> subject)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      if (OPERATION_DELETE.equals(this.operation)) {
        // create the reconciliation event for delete
        deleteEvent(subject);
      }
      else {
        // set status to default if no one specified
        subject.put(this.descriptor.organizationStatus(), this.operation);
        // create the reconciliation event for modify
        regularEvent(subject);
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method groupd by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    // check if the specified value provided for the reconciliation operation
    // is in range
    this.operation = stringValue(RECONCILE_OPERATION);
    if (!(OPERATION_DELETE.equalsIgnoreCase(this.operation) || OPERATION_DISABLE.equalsIgnoreCase(this.operation))) {
     final String[] parameter = {RECONCILE_OPERATION, OPERATION_DELETE + " | " + OPERATION_DISABLE };
     throw new TaskException(TaskError.TASK_ATTRIBUTE_NOT_INRANGE, parameter);
    }

    final ProfileManager   manager = Platform.getBean(ProfileManager.class);
    final Profile          profile = manager.getProfile(reconcileObject());
    final Form             form    = profile.getForm();
    final AttributeMapping mapping = this.descriptor.attributeMapping();
    final int              size    = mapping.size();

    // create a mapping between the reconciliation fields defined at the
    // Resource Object profile and the logical attributes declared on the
    // reconciliation descriptor initualized by the super class
    int i = 0;
    this.reconciliationMapping = new String[size][];
    for (TargetAttribute attribute : form.getMappedAttributes()) {
      final String attributeName = attribute.getName();
      if (mapping.containsValue(attributeName)) {
        this.reconciliationMapping[i]    = new String[2];
        this.reconciliationMapping[i][0] = attribute.getOIMAttribute().getName();
        this.reconciliationMapping[i][1] = attributeName;
        i++;
      }
    }
    if (i == 0) {
      final String[] parameter = {stringValue(RECONCILE_DESCRIPTOR), reconcileObject()};
      throw new ReconciliationException(ReconciliationError.ATTRIBUTES_NOT_MAPPED, parameter);
    }

    this.identifierRDN = mapping.encodedValue(this.descriptor.identifier());
    if (StringUtility.isEmpty(this.identifierRDN)) {
      final String[] parameter = {stringValue(RECONCILE_DESCRIPTOR), this.identifierRDN};
      throw new ReconciliationException(ReconciliationError.IDENTIFIER_NOT_MAPPED, parameter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (overridden)
  /**
   ** Reconciles the changed entries in the Directory Service.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    String[] parameter = { reconcileObject(), getName(), resourceName() };
    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, parameter));
    try {
      this.connector().connect();
      populateDeletions();
    }
    // in any case of an unhandled exception
    catch (TaskException e) {
      warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, parameter));
      throw e;
    }
    finally {
      this.connector().disconnect();
    }
    info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, parameter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evaluateBatch
  /**
   ** Evaluate the size of a batch.
   ** <p>
   ** The batch size determines how many entries will be returned from the
   ** Oracle Identity Manager for a particular entity
   **
   ** @return                    the {@link Batch} size
   */
  protected Batch evaluateBatch() {
    return new Batch(batchSize());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateDeletions
  /**
   ** Do all action which should take place for reconciliation by fetching the
   ** data from Oracle Identity Manager.
   **
   ** @throws TaskException      if the operation fails
   */
  private void populateDeletions()
    throws TaskException {

    final String method = "populateDeletions";
    trace(method, SystemMessage.METHOD_ENTRY);
    // check if a request to stop the execution is pending and return without
    // further actions if it evaluates to true
    if (isStopped())
      return;

    // set the current date as the timestamp on which this task was last
    // reconciled at start
    // setting it at this time that we have next time this scheduled task will
    // run the changes made during the execution of this task
    lastReconciled(this.connector().systemTime());

    final Batch batch = evaluateBatch();
    while(!stopped()) {
      info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));
      final List<Map<String, Object>> identity = populateBatch(batch.start(), batch.end());
      info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
      // if we should really do reconcile ?
      if (gatherOnly())
        info(TaskBundle.string(TaskMessage.RECONCILE_SKIP));
      else
        // perform all actions to detect and execute deletions
        processDeletion(identity);
      // check if we reached the end of transmission
      if (identity.size() < batch.size())
        break;
      // build the search filter to lookup the subject in the target system
      batch.next();
    }
    // update the timestamp on the task
    updateLastReconciled();
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateBatch
  /**
   ** Creates a {@link List} of entries of a specific entity in Oracle Identity
   ** Manager. Each element in the {@link List} is an instance of {@link Map}
   ** that reflects the value mapping based on the reconciliation descriptor
   ** created in the initialization of the executing Scheduled Job.
   **
   ** @param  startrow           the starting row that has to be fetched from
   **                            the Organization entity.
   **                            This controls in conjunction with argument
   **                            <code>endrow</code> the memory consumption
   **                            needed by each iteration.
   ** @param  endrow             the end row that has to be fetched from
   **                            the Organization entity.
   **                            This controls in conjunction with argument
   **                            <code>startrow</code> the memory consumption
   **                            needed by each iteration.
   **
   ** @return                    the {@link List} that contains the attributes
   **                            for each entry fetched from Oracle Identity
   **                            Manager Organization entity.
   **                            Each entry is an instance of {@link Map}.
   **
   ** @throws TaskException      if the operation fails
   */
  protected abstract List<Map<String, Object>> populateBatch(final int startrow, final int endrow)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDeletion
  /**
   ** Performs all actions required to detect a deletion in the target system.
   **
   ** @param  identity           the batch of identities to lookup for existance
   **                            in the target system.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void processDeletion(final List<Map<String, Object>> identity)
    throws TaskException {

    // check if a request to stop the execution is pending and return without
    // further actions if it evaluates to true
    if (isStopped())
      return;

    final String method = "processDeletion";
    trace(method, SystemMessage.METHOD_ENTRY);

    // create a collection that will contain all detected deletions on the
    // current batch
    final List<Map<String, Object>> deletion   = new ArrayList<Map<String, Object>>();
    // obtain the identifying attribute name to build the lookup filter criteria
    final String                    identifier = this.descriptor.identifier();
    // build the identifying attribute name to be returned
    final String[]                  returning  = { this.identifierRDN };
    // the search will not cross naming system boundaries.
    final SearchControls            controls   = DirectoryConnector.searchScope(searchScope());
    controls.setReturningAttributes(returning);

    for (Map<String, Object> subject : identity) {
      if (isStopped())
        break;
      final String entryFilter = DirectoryConnector.composeFilter(this.identifierRDN, (String)subject.get(identifier));
      // perform a lookup in the target system
      final NamingEnumeration<SearchResult> result = this.connector().search(this.searchBase(), entryFilter, controls);
      // if the entry does not exists put it in the collection to perform the
      // necessary reconciliation action later on
      if (!result.hasMoreElements())
        deletion.add(subject);
    }
    if (deletion.size()== 0)
      info(TaskBundle.string(TaskMessage.NOTHING_TO_DELETE));
    else {
      info(TaskBundle.format(TaskMessage.WILLING_TO_DELETE, deletion.size()));
      int count = 0;
      for (Map<String, Object> subject : deletion) {
        if (isStopped())
          break;
        processSubject(subject);
        count++;
      }
      info(TaskBundle.format(TaskMessage.ABLE_TO_DELETE, count));
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }
}