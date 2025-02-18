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
    Subsystem   :   TXT Flatfile Connector

    File        :   AccountTargetReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountTargetReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-18  DSteding    First release version
*/

package oracle.iam.identity.txt.service.reconciliation;

import java.util.Map;
import java.util.HashMap;

import java.io.EOFException;

import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.utility.file.FlatFileAttribute;
import oracle.iam.identity.utility.file.FlatFileReference;
import oracle.iam.identity.utility.file.FlatFileDescriptor;

////////////////////////////////////////////////////////////////////////////////
// class AccountTargetReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountTargetReconciliation</code> acts as the service end point
 ** for the Oracle Identity Manager to reconcile account information from a flat
 ** file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccountTargetReconciliation extends EntityReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation target */
    TaskAttribute.build(APPLICATION_INSTANCE,   TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,              TaskAttribute.MANDATORY)
    /** the task attribute to resolve the entity filter */
  , TaskAttribute.build(SEARCH_FILTER,          TaskAttribute.OPTIONAL)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,            TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,           TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are successfully proceeded */
  , TaskAttribute.build(WORKING_FOLDER,         TaskAttribute.MANDATORY)
    /** the fullqualified filename which are specifying the mapping for import  */
  , TaskAttribute.build(DATA_DESCRIPTOR,        TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATA_FILE,              TaskAttribute.MANDATORY)
    /** the filename of the proceeded data  */
  , TaskAttribute.build(PROCEED_FILE,           TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,          TaskAttribute.MANDATORY)
  /** report the non-existing as an exception */
  , TaskAttribute.build(MISSING_FILE_EXCEPTION, TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
    */
  , TaskAttribute.build(INCREMENTAL,            TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountTargetReconciliation</code> scheduled
   ** task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountTargetReconciliation() {
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
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Company Phonebook.
   **
   ** @throws TaskException      in case an error does occur.
  */
  @Override
  protected void onExecution()
    throws TaskException {

    String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, parameter));
    if (this.dataFileAvailable(lastReconciled())) {
      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, parameter));
      try {
        this.createWorkingContext();
        this.createWorkingFile();

        if (booleanValue(INCREMENTAL)) {
          // extend the descriptor to fetch the transaction code from the
          // created working file with a readonly attribute
          FlatFileAttribute transaction = new FlatFileAttribute(FlatFileDescriptor.TRANSACTION, FlatFileAttribute.STRING, -1, -1);
          dataDescriptor().addAttribute(transaction);
        }

        processFile();
        // copy the new file over the oldfile
        // we let the new file where it is, so the next time the scheduled
        // task is running we have the same file or a new one
        FileSystem.copy(dataFile(), proceedFile());

        this.updateLastReconciled();
        info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, parameter));
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, parameter));
      }
      catch (SystemException e) {
        // copy the data file to the error folder
        // we let the new file where it is, so the next time the scheduled
        // task is running we have the same file or a new one
        try {
          FileSystem.copy(dataFile(), errorFile());
        }
        catch (SystemException ex) {
          warning(ex.getLocalizedMessage());
        }
        throw new TaskException(e);
      }
      finally {
        if (this.processor() != null) {
          // close processor this will also free the resources associated with the
          // processor
          this.processor().close();
        }
        // remove temporary files
        deleteWorkingFile();

        // inform the observing user about the overall result of this task
        if (isStopped())
          warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, reconcileObject(), getName() , dataFile().getAbsolutePath()));
        else
          info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName() , dataFile().getAbsolutePath()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFile
  /**
   ** Do all action which should take place for reconciliation for the working
   ** file.
   **
   ** @throws TaskException      if the operation fails for any reason.
   */
  protected final void processFile()
    throws TaskException {

    // validate the effort to do
    if (gatherOnly())
      info(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
    else {
      // This while loop is used to read the file in blocks.
      // This should decrease memory usage and help with server load.
      while (!isStopped()) {
        try {
          final Map<String, Object> entry = this.processor().readEntity(true);

          // build the identifier of the subject
          final String identifier = buildIdentifier(entry);

          // remove the transaction code if available
          // in case the direct path operation was configured at this scheduled
          // task the resulting string will be null, means the subject will
          // never deleted
          final String transaction = (String)entry.remove(FlatFileDescriptor.TRANSACTION);

          // remove the multi-valued attributes to prevent that they are remove
          // by mapping transformation
          final Map<String, Object> multivalued = new HashMap<String, Object>();
          for (String cursor : this.descriptor.multivalued().keySet()) {
            final FlatFileReference reference = (FlatFileReference)this.descriptor.multivalued().get(cursor);
            multivalued.put(cursor, entry.remove(reference.entryLinkAttribute()));
          }

          // remove the entitlement attributes to prevent that they are remove
          // by mapping transformation
          for (String cursor : this.descriptor.entitlement().keySet()) {
            final FlatFileReference reference = (FlatFileReference)this.descriptor.entitlement().get(cursor);
            multivalued.put(cursor, entry.remove(reference.entryLinkAttribute()));
          }

          // hmmmm, should we really chain
          final Map<String, Object> master = transformMaster(createMaster(entry, true));

          // we are recenciling an acount hance we have to plugi.in the
          // IT Resource the account belongs to
          master.put(IT_RESOURCE, stringValue(IT_RESOURCE));
          // do all action which should take place for reconciliation
          processSubject(identifier, transaction, master, multivalued);
        }
        catch (EOFException e) {
          // don't close processor here but break the loop due to EOF reached
          break;
        }
      }
    }
  }
}