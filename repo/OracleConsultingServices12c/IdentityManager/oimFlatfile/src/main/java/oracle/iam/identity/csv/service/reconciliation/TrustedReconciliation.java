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
    Subsystem   :   CSV Flatfile Connector

    File        :   TrustedReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TrustedReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.service.reconciliation;

import java.util.Map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.utility.file.CSVError;
import oracle.iam.identity.utility.file.CSVReader;
import oracle.iam.identity.utility.file.CSVWriter;
import oracle.iam.identity.utility.file.CSVException;
import oracle.iam.identity.utility.file.CSVAttribute;
import oracle.iam.identity.utility.file.CSVDescriptor;
import oracle.iam.identity.utility.file.CSVDependencyCollector;

import oracle.iam.identity.csv.service.ControllerMessage;

import oracle.iam.identity.csv.resource.ControllerBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class TrustedReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TrustedReconciliation</code> implements the base functionality of
 ** a service end point for the Oracle Identity Manager Scheduler which handles
 ** data provided by a CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 */
public abstract class TrustedReconciliation extends EntityReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which can be defined on this task to specify how often the
   ** task should try to process remaining entries.
   ** <br>
   ** This attribute is optional and defaults to <code>10</code>.
   **
   ** @see #RETRY_REMAINING_ENTRIES_DEFAULT
   */
  protected static final String    RETRY_REMAINING_ENTRIES           = "Retry Remaining Entries";

  /**
   ** Default value for Task Attribute {@link #RETRY_REMAINING_ENTRIES}
   */
  protected static final String    RETRY_REMAINING_ENTRIES_DEFAULT   = "10";

  /**
   ** Attribute tag which can be defined on this task to specify after which
   ** number of loops the dependency check chould no longer applied.
   ** <br>
   ** This attribute is optional and defaults to <code>10</code>.
   **
   ** @see #RETRY_REMAINING_THRESHOLD_DEFAULT
   */
  protected static final String    RETRY_REMAINING_THRESHOLD         = "Retry Remaining Threshold";

  /**
   ** Default value for Task Attribute {@link #RETRY_REMAINING_THRESHOLD}
   */
  protected static final String    RETRY_REMAINING_THRESHOLD_DEFAULT = "10";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the remaining hierarchy that holds all unprocessed entries that have an
  // unresolved manager parent
  protected CSVDependencyCollector collector;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TrustedReconciliation</code> task adapter that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TrustedReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the reconciliation task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, parameter));
    if (this.dataFileAvailable(lastReconciled())) {
      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, reconcileObject()));

      try {
        this.createWorkingContext();
        this.createWorkingFile();

        // choose what is requested
        if (booleanValue(INCREMENTAL)) {
          // extend the descriptor to fetch the transaction code from the
          // created working file with a readonly attribute
          CSVAttribute transaction = new CSVAttribute(CSVDescriptor.TRANSACTION, CSVAttribute.STRING, true, null);
          dataDescriptor().addAttribute(transaction);
        }

        int pass = 1;
        processFile(pass);

        // if we have something that can be resolved
        int attempts = integerValue(RETRY_REMAINING_ENTRIES);
        while ((this.collector.hasResolveables() || this.collector.hasUnresolveables()) && (pass++ < attempts)) {
          if (isStopped())
            break;

          // delete the working file this will create a new file at next time
          // getWorkingFile() is called
          deleteWorkingFile();
          // merge the dependencies with a new created working file
          mergeDependencies(workingFile());
          try {
            // now cleanup the collector for the next run
            this.collector.clear();
            // recreate the CSVProcessor owned by this task by assigning a
            // CSVReader to it that use the recreated working file
            createProcessor(new CSVReader(new FileReader(workingFile()), this.singleValueSeparator, this.enclosingCharacter, true, true));
          }
          catch (IOException e) {
            // in any case of an unhandled exception
            throw new TaskException(e);
          }

          // process the remaining set located in the working file
          processFile(pass);
        }

        // if we have still unresolved dependencies we writing the record back
        // to the error file
        if ((this.collector.hasResolveables() || this.collector.hasUnresolveables())) {
          // write the remaining entries to the error file
          mergeDependencies(errorFile());

          try {
            // now cleanup the collector for the next run
            this.collector.clear();
          }
          catch (IOException e) {
            // in any case of an unhandled exception
            throw new TaskException(e);
          }
        }

        // copy the new file over the oldfile
        // we let the new file where it is, so the next time the scheduled
        // task is running we have the same file or a new one
        try {
          FileSystem.copy(dataFile(), proceedFile());
        }
        catch (SystemException e) {
          throw new TaskException(e);
        }

        this.updateLastReconciled();
        info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE,      parameter));
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, parameter));
      }
      catch (TaskException e) {
        // copy the data file to the error folder
        // we let the new file where it is, so the next time the scheduled
        // task is running we have the same file or a new one
        try {
          FileSystem.copy(dataFile(), errorFile());
        }
        catch (SystemException ex) {
          warning(ex.getLocalizedMessage());
        }
        // notify user about the problem
        warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, parameter));
        throw e;
      }
      finally {
        if (this.processor() != null) {
          // close processor this will also free the resources associated with
          // the processor
          this.processor().close();
        }

        // remove temporary files
        deleteWorkingFile();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeDependencies
  /**
   ** Merge the dependencies by recreating the work file
   **
   ** @mergeFile                 the abstract {@link File) that will contaion
   **                            the merge.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  private void mergeDependencies(final File mergeFile)
    throws TaskException {

    // create a writer that understand the protocol
    CSVWriter writer = null;
     try {
       // create a writer that understand the protocoll
       writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(mergeFile), stringValue(FILE_ENCODING)), 1, this.singleValueSeparator, this.enclosingCharacter, true);

       // put the header at the first line in the new created strem
       dataDescriptor().write(writer);

       info(ControllerBundle.format(ControllerMessage.RECONCILE_RESOLVABLES, reconcileObject()));
       this.collector.mergeResolved(writer);
       info(ControllerBundle.format(ControllerMessage.RECONCILE_UNRESOLVABLES, reconcileObject()));
       this.collector.mergeUnresolved(writer);

       // flush and close the writer
       writer.close();
     }
     catch (IOException e) {
       String[] parameter = {WORKING_FILE, workingFile().getAbsolutePath() };
       throw new CSVException(CSVError.NOTWRITABLE, parameter);
     }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createWorkingContext (overridden)
  /**
   ** Creates the context of this scheduled task needed for execution.
   **
   ** @throws TaskException       if the CSV file descriptor cannot be created.
   */
  protected void createWorkingContext()
    throws TaskException {

    // ensure inheritance
    super.createWorkingContext();

    this.collector = new CSVDependencyCollector(dataDescriptor());
    this.collector.logger(logger());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pushBack
  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @param  identifier         the identifier of a particular entry that
   **                            cannot be reconciled because it depends on the
   **                            passed <code>owner</code>.
   ** @param  owner              the identifier of a particular entry that
   **                            currently is not yet reconciled.
   ** @param  subject            the subject a s a {@link Map} which has to be
   **                            proceed later because the repository is not in
   **                            the state to reconcile the subject.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  protected void pushBack(String identifier, String owner, Map<String, Object> subject)
    throws TaskException {

    synchronized(this.collector) {
      try {
        this.collector.insert(identifier, owner, subject);
      }
      catch (IOException e) {
        throw new TaskException(e);
      }
    }
  }
}