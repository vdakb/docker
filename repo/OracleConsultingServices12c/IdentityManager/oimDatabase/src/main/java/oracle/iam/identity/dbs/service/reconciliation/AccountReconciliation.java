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

    File        :   AccountReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.service.reconciliation;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcObjectNotFoundException;

import java.util.ArrayList;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.foundation.persistence.DatabaseReference;

import oracle.iam.identity.dbs.persistence.Catalog;
import oracle.iam.identity.dbs.persistence.AdministrationMessage;

import oracle.iam.identity.dbs.resource.AdministrationBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AccountReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountReconciliation</code> acts as the service end point for the
 ** Oracle Identity Manager to reconcile account information from an Oracle
 ** Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class AccountReconciliation extends Reconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which can be defined on this task to specify which accounts
   ** should be excluded from reconciliation
   ** <br>
   ** This attribute is optional.
   */
  protected static final String EXCLUDED_USER = "Exclude User Control";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Set<String>                excludeUser;
  private List<Pair<String, String>> returning;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountReconciliation</code> scheduled task that
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
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Company Phonebook.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    final String[] infoParameter = { reconcileObject(), getName(), resourceName() };
    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, infoParameter));

    int size = batchSize();
    final Batch   batch       = new Batch(size);
    final Date    time        = lastReconciled();
    final boolean incremental = booleanValue(INCREMENTAL);
    try {
      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, this.reconcileObject()));
      // set the current date as the timestamp on which this task has been last
      // reconciled at the start of execution
      // setting it here to have it the next time this scheduled task will
      // run the changes made during the execution of this task
      // updating this attribute will not perform to write it back to the
      // scheduled job attributes it's still in memory; updateLastReconciled()
      // will persist the change that we do here only if the job completes
      // successful
      lastReconciled(this.connection.systemTime());

      while (true) {
        // check if the current thread is able to execute or a stop signal
        // is pending
        if (isStopped())
          break;

        // retrieve the next batch of entries from the target system which are
        // created since the last remebered execution of this scheduled task
        Set<String> entries = this.connection.accountSearch(incremental ? time : null, batch.start(), batch.end());
        size = entries.size();
        // check if there is a valid result set because if nothing is returned
        // and the batch is never incremented than it is pretty sure that there
        // is no entry available that match the filter condition
        if (size == 0 && batch.start() == 1) {
          info(TaskBundle.string(TaskMessage.NOTHING_TO_CHANGE));
          break;
        }
        else {
          info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(size)));
          // if we should really do reconcile ?
          if (gatherOnly())
            info(TaskBundle.string(TaskMessage.RECONCILE_SKIP));
          else {
            // reconciliation
            for (String identifier : entries) {
              // check if the current thread is able to execute or a stop signal
              // is pending
              if (isStopped())
                break;
              // handle exclusions
              if (this.excludeUser != null && this.excludeUser.contains(identifier)) {
                final String[] arguments = {identifier, stringValue(EXCLUDED_USER) };
                warning(AdministrationBundle.format(AdministrationMessage.EXCLUDE_ACCOUNT, arguments));
              }
              else {
                processSubject(identifier);
              }
            }
            info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
          }
        }
        // if the returned size is less than the requested batch size it is the
        // last page that we will get from the server for the specified filter
        // condition hence we can break the loop
        if (size < batch.size()) {
          info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, this.reconcileObject()));
          break;
        }
        batch.next();
      }
      updateLastReconciled();
    }
    finally {
      if (isStopped())
        warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, infoParameter));
      else
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, infoParameter));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** The call back method just invoked before reconciliation takes place.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    this.excludeUser = populateControlList(stringValue(EXCLUDED_USER));

    // ToDO: We should use the new interfaces to discover the reconciliation
    // capabilities
    // check out:
    //   ConfigUtils.getDefaultProfileNameFromObjName(reconcileObject())
    //   ProfileManager profilemgr = Platform.getBean(ProfileManager.class);
    //   Profile profile = profilemgr.lookupProfile(profileName);
    //   ArrayList<ReconAttribute> list = profile.getAllReconAttributes();

    // iterate over the reconciliation field and check for multi-valued
    // attributes
    String columnName = null;
    String fieldName  = null;
    // get the reconciliation field definition from the Resource Object handled
    // be this task
    final Map<String, String> filter = new HashMap<String, String>(1);
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
        // appropritate exception
        if (resultSet.getRowCount() == 0)
          throw new TaskException(TaskError.RESOURCE_RECONFIELD, reconcileObject());
      }
      catch (tcColumnNotFoundException e) {
        throw new TaskException(TaskError.COLUMN_NOT_FOUND, ResourceObject.KEY);
      }
      catch (tcObjectNotFoundException e) {
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, reconcileObject());
      }

      try {
        for (int i = 0; i < resultSet.getRowCount(); i++) {
          resultSet.goToRow(i);
          columnName  = ResourceObject.RECON_FIELD_TYPE;
          if (DatabaseReference.MULTI_VALUE_TYPE.equals(resultSet.getStringValue(columnName))) {
            columnName = ResourceObject.RECON_FIELD_NAME;
            fieldName  = resultSet.getStringValue(columnName);

            if (!this.descriptor.entitlement().containsKey(fieldName)) {
              String[] arguments = { reconcileObject(), fieldName };
              warning(TaskBundle.format(TaskError.RESOURCE_RECON_MULTIVALUE, arguments));
            }
          }
        }
      }
      catch (tcColumnNotFoundException e) {
        throw new TaskException(TaskError.COLUMN_NOT_FOUND, ResourceObject.KEY);
      }
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }

    // ensure inheritance
    super.beforeExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returningAttributes
  /**
   ** Returns the array of attribute names that will be passed to a Database
   ** Server search operation to specify which attributes the Database Server
   ** should return for an account.
   **
   ** @return                    the array of attribute names that this task
   **                            will be reconcile.
   */
  public final List<Pair<String, String>> returningAttributes() {
    // Lazy initialization of the attribute names returned for an account to
    // reconcile
    if (this.returning != null)
      return this.returning;

    final Set<String> name = this.descriptor.returningAttributes();
    this.returning = new ArrayList<Pair<String, String>>(name.size());
    for (String cursor : name) {
      this.returning.add(Pair.of(cursor, cursor));
    }
    return this.returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for reconciliation of a database
   ** account; either target or trusted reconciliation.
   **
   ** @param  username           the name of the account to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected abstract void processSubject(String username)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populatePermission
  /**
   ** Do all action which should take place for permission reconciliation by
   ** fetching the data from the target system.
   ** <p>
   ** The returning {@link List} containes {@link Map}s. A {@link Map} has
   ** always a size of the attribute mapping specified by the descriptor. The
   ** key of the {@link Map} is the decoded value of the attribute mapping. The
   ** value associated with each key is the value retrieved form the server
   ** attribute and specified by the encoded value of the attribute mapping.
   **
   ** @param  username           the account name used as the filter expression
   **                            to use for the search; may not be
   **                            <code>null</code>.
   ** @param  permission         the {@link DatabaseReference} that describes
   **                            the linkage.
   **
   ** @return                    a {@link List} where each entry is a
   **                            {link Map}.
   **                            The contained {@link Map} has always a size of
   **                            attribute mapping specified by the descriptor.
   **                            The key returned in this {@link Map} is the
   **                            decoded value of the attribute mapping. The
   **                            value associated with each key is the value
   **                            retrieved form the server attribute and
   **                            determined by the encoded value of the
   **                            attribute mapping.
   **
   ** @throws TaskException      if the operation fails for any reason
   */
  protected List<Map<String, Object>> populatePermission(final String username, final DatabaseReference permission)
    throws TaskException {

    final String method = "populatePermission";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Catalog.Type type = Catalog.Type.valueOf(permission.catalogueQuery());
    try {
      return this.connection.loadGrantedPermission(username, type);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}