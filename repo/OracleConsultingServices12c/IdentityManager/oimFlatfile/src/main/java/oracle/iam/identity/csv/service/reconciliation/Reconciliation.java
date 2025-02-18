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

    File        :   Reconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Reconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed Defect DE-000124
                                         Batch Size is an optional argument but
                                         if its isn't defined the job loops
                                         infinite.
                                         Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.service.reconciliation;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.GregorianCalendar;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.text.SimpleDateFormat;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.utility.file.CSVError;
import oracle.iam.identity.utility.file.CSVException;
import oracle.iam.identity.utility.file.CSVAttribute;
import oracle.iam.identity.utility.file.CSVDescriptor;
import oracle.iam.identity.utility.file.CSVProcessor;
import oracle.iam.identity.utility.file.CSVReader;
import oracle.iam.identity.utility.file.CSVWriter;
import oracle.iam.identity.utility.file.CSVComparator;

import oracle.iam.identity.csv.service.Controller;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Reconciliation</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** data provided by a CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class Reconciliation extends Controller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which can be defined on a scheduled task to hold the name of
   ** the raw file after reconciliation.
   */
  protected static final String PROCEED_FILE    = "Proceed Filename";

  /**
   ** Attribute tag which can be defined on a scheduled task to hold the name of
   ** the raw file compared content.
   */
  protected static final String WORKING_FILE    = "Working Filename";

  /** the category of the logging facility to use */
  private static final String   LOGGER_CATEGORY = "OCS.CSV.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the all purpose container to search and post objects
  protected Map<String, Object> filter               = new HashMap<String, Object>();

  /** the size of a block processed in a thread */
  protected int                 bulkSize             = Integer.MIN_VALUE;

  /** the abstract file which contains the data */
  private File                  importNew            = null;

  /** the abstract file which contains the produced delta */
  private File                  importOld            = null;

  /** the abstract file which contains the produced delta */
  private File                  workingFile          = null;

  /** the abstract file which contains the produced errors */
  private File                  errorFile            = null;

  /** the processor of the CSV structure */
  private CSVProcessor          processor            = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> task adapter that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Reconciliation() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processor
  /**
   ** Returns the {@link CSVProcessor} used by this <code>Reconciliation</code>
   ** to create a <code>CSVRecord</code>.
   **
   ** @return                    the {@link CSVProcessor} used by this scheduled
   **                            task.
   */
  protected final CSVProcessor processor() {
    return this.processor;
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
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    createWorkFolder();
    createErrorFolder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   **
   ** @param  identifier         the composed identifier of the {@link Map} to
   **                            reconcile.
   ** @param  transaction        the transaction code of the {@link Map} to
   **                            reconcile.
   ** @param  data               the {@link Map} to reconcile.
   ** @param  pass               the pass the subjec ist loaded from the file.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected abstract void processSubject(final String identifier, final String transaction, final Map<String, Object> data, final int pass)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildIdentifier
  /**
   ** Create a String with the concatenated idtentifier attributes.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @return                    a String with the concatenated idtentifier
   **                            attributes.
   */
  protected final String buildIdentifier(final Map<String, Object> subject) {
    // build the identifier of the subject
    StringBuilder buffer= new StringBuilder();
    // get the identifier from the descriptor
    Iterator<CSVAttribute> i = this.processor.descriptor().identifierIterator();
    while(i.hasNext()) {
      String name = i.next().name();
      buffer.append(subject.get(name));
      if (i.hasNext())
        buffer.append(SystemConstant.BLANK);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createWorkingFile
  /**
   ** Creates the working file by comparing the new transfered file with the
   ** file which was proceed in a previuos run of the associated task.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  protected void createWorkingFile()
    throws TaskException {

    final String method ="createWorkingFile";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String fileEncoding = parameter(FILE_ENCODING);
    try {
      // choose what is requested
      if (!booleanValue(INCREMENTAL)) {
        // if the option says direct path copy the new file over the working
        // file
        try {
          FileSystem.copy(dataFile(), workingFile());
        }
        catch (SystemException e) {
          throw new TaskException(e);
        }
      }
      else {
        CSVReader readerNew = new CSVReader(new InputStreamReader(new FileInputStream(dataFile()),    fileEncoding), this.singleValueSeparator, this.enclosingCharacter, true, true);
        CSVReader readerOld = new CSVReader(new InputStreamReader(new FileInputStream(proceedFile()), fileEncoding), this.singleValueSeparator, this.enclosingCharacter, true, true);

        // any output Stream will also be encoded as configured
        CSVWriter writerWrk = new CSVWriter(new OutputStreamWriter(new FileOutputStream(workingFile()), fileEncoding), 1, this.singleValueSeparator, this.enclosingCharacter, true);

        // create a comparator to compare the new and previous proceed file
        CSVComparator comparator = new CSVComparator(dataDescriptor(), fileEncoding);
        // set the logging facility so we can trace what the comparator will do
        comparator.logger(logger());

        // create the working file by comparing the both files without any
        // transformation
        comparator.compare(readerOld, readerNew, writerWrk, false, workingFile());
      }
    }
    catch (IOException e) {
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
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createWorkingContext
  /**
   ** Creates the context of this scheduled task needed for execution.
   **
   ** @throws TaskException      the exception thrown if {@link FileInputStream}
   **                            cannot be create the working file or the
   **                            required CSV file descriptor cannot be created.
   */
  protected void createWorkingContext()
    throws TaskException {

    try {
      createProcessor(new CSVReader(new InputStreamReader(new FileInputStream(workingFile()), parameter(FILE_ENCODING)), this.singleValueSeparator, this.enclosingCharacter, true, true));
    }
    catch (IOException e) {
      throw new TaskException(e);
    }

    this.bulkSize = batchSize();
    // Fixed Defect DE-000124
    // Batch Size is an optional argument but if its isn't defined the job
    // loops infinite hence we need to fallback to a default value
    if (this.bulkSize == Integer.MIN_VALUE)
      this.bulkSize = BATCH_SIZE_DEFAULT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessor
  /**
   ** Creates the {@link CSVProcessor} of this scheduled task needed for
   ** execution.
   **
   ** @param  reader             the {@link CSVReader} the {@link CSVProcessor}
   **                            will use to read the CSV file.
   **
   ** @throws TaskException      if the CSV file descriptor cannot be created.
   */
  protected void createProcessor(final CSVReader reader)
    throws TaskException {

    this.processor = new CSVProcessor(dataDescriptor(), reader);
    this.processor.logger(logger());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteWorkingFile
  /**
   ** Deletes the <code>workingFile</code> to make it available for further
   ** operations.
   */
  protected final void deleteWorkingFile() {
    if (this.workingFile != null) {
      this.workingFile.delete();
      this.workingFile = null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFileAvailable
  /**
   ** Check if a file is in the transfer folder.
   **
   ** @param  timeStamp          the timestamp this task was last time executed.
   **
   ** @return                    <code>true</code> the abstract {@link File} to
   **                            access is touched since the last successful
   **                            import operation; otherwise <code>false</code>.
   **
   ** @throws TaskException      if the abstract file handle could not be
   **                            created.
   */
  protected final boolean dataFileAvailable(final Date timeStamp)
    throws TaskException {

    final String method ="dataFileAvailable";
    trace(method, SystemMessage.METHOD_ENTRY);

    final File dataFile = dataFile();
    boolean exists = dataFile.exists();
    if (exists) {
      // create a new date instance by passing last modified information
      final Date lastModified = new Date(dataFile.lastModified());
      // compare both dates to decided if a change on the files happend by:
      // if the outcome of the comparision is less than or equal to zero we
      // assume the file is untouched
      final int changed = lastModified.compareTo(timeStamp);
      if (changed <= 0) {
        exists = false;
        info(TaskBundle.format(TaskMessage.NOTCHANGED, this.importNew.getName()));
      }
      // set the current date as the timestamp on which this task has last
      // reconciled at start
      // setting it at this time that we have next time this scheduled task
      // will run the changes made during the execution of this task
      lastReconciled(lastModified);
    }
    else {
      final String[] values = { DATA_FOLDER, this.importNew.getName() };
      info(TaskBundle.format(TaskMessage.NOTAVAILABLE, values));
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return exists;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFile
  /**
   ** Do all action which should take place for reconciliation for the working
   ** file.
   **
   ** @param  pass               the pass this method will scan the working file.
   **                            this is passed to the
   **                            {@link #processSubject(String, String, Map, int)}
   **                            method to make it subclasses easy to decide if
   **                            they do all checks again or skip someone.
   **
   ** @throws TaskException      in case an error does occur processing the
   **                            file.
   */
  protected final void processFile(final int pass)
    throws TaskException {

    final String method = "processFile";
    // This while loop is used to read the file in blocks.
    // This should decrease memory usage and help with server load.
    while (!isStopped()) {
      List<Map<String, Object>> entries = populate(this.bulkSize);
      // if the collection is empty we had done the job
      if (entries == null || entries.size() == 0)
        break;

      // validate the effort to do
      info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(entries.size())));
      if (gatherOnly())
        info(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
      else {
        for (Map<String, Object> subject : entries) {
          if (isStopped())
            break;

          // build the identifier of the subject
          final String identifier = buildIdentifier(subject);

          // choose what is requested
          String transaction = null;
          if (booleanValue(INCREMENTAL)) {
            // get the transaction code if available
            // in case the direct path operation was configured at this scheduled
            // task the resulting string will be null, means the subject will
            // never deleted
            transaction = (String)subject.remove(CSVDescriptor.TRANSACTION);
          }

          // filter data in a new mapping
          subject = this.descriptor.attributeMapping().filterByEncoded(subject);
          if (subject.isEmpty())
            throw new TaskException(TaskError.ATTRIBUTE_MAPPING_EMPTY);

          // produce the logging output only if the logging level is enabled for
          if (this.logger != null && this.logger.debugLevel()) {
            debug(method, TaskBundle.format(TaskMessage.ENTITY_RECONCILE, this.descriptor.identifier(), subject.get(this.descriptor.identifier())));
            // produce the logging output only if the logging level is enabled for
            debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(subject)));
          }

          // apply transformation if needed
          if (this.descriptor.transformationEnabled()) {
            subject = this.descriptor.transformationMapping().transform(subject);
            if (subject.isEmpty())
              throw new TaskException(TaskError.TRANSFORMATION_MAPPING_EMPTY);

            // produce the logging output only if the logging level is enabled for
            if (this.logger != null && this.logger.debugLevel())
              this.debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_TRANSFORMATION, StringUtility.formatCollection(subject)));
          }
          processSubject(identifier, transaction, subject, pass);
        }
        info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFile
  /**
   ** Creates the abstract {@link File} to access the file to import.
   **
   ** @return                    the abstract {@link File} to access file to
   **                            import.
   **
   ** @throws TaskException      if the abstract file handle could not be
   **                           created.
   */
  protected final File dataFile()
    throws TaskException {

    if (this.importNew == null) {
      final String fileName = dataFileName();
      // create the file handle
      this.importNew = createFile(dataFolder(), fileName);

      // check if we have read access to the file
      if (!this.importNew.canRead()) {
        final String[] values = { DATA_FILE, fileName};
        throw new CSVException(CSVError.NOTREADABLE, values);
      }
    }
    return this.importNew;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proceedFile
  /**
   ** Creates the abstract {@link File} to access the file which was used in a
   ** proviuos execution of this scheduled task.
   **
   ** @return                    the abstract {@link File} to access the proceed
   **                            file.
   **
   ** @throws TaskException      if the abstract file handle could not be
   **                           created.
   */
  protected final File proceedFile()
    throws TaskException {

    if (this.importOld == null) {
      String fileName = stringValue(PROCEED_FILE);

      // create the file handle
      this.importOld = createFile(this.workingFolder(), fileName);

      // check if we have read and write access to the file
      if (this.importOld.exists()) {
        final String[] values = { PROCEED_FILE, fileName};
        // check if we have read access to the file
        if (!this.importOld.canRead())
          throw new CSVException(CSVError.NOTREADABLE, values);

        // check if we have write access to the file
        if (!this.importOld.canWrite())
          throw new CSVException(CSVError.NOTWRITABLE, values);
      }
      else {
        // if the file does not exist create a new one and write the descriptor
        // as the header to the file
        try {
          this.importOld.createNewFile();
          CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(this.importOld), parameter(FILE_ENCODING)), 1, this.singleValueSeparator, this.enclosingCharacter, true);
          this.dataDescriptor().write(writer);
          writer.close();
        }
        catch (IOException e) {
          throw new CSVException(CSVError.NOTCREATABLE, fileName, e);
        }
      }
    }

    return this.importOld;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workingFile
  /**
   ** Creates the abstract {@link File} to access the working file.
   **
   ** @return                    the abstract {@link File} to access the working
   **                            file.
   **
   ** @throws TaskException      if the abstract file handle could not be
   **                           created.
   */
  protected final File workingFile()
    throws TaskException {

    if (this.workingFile == null ) {
      final String fileName = stringValue(WORKING_FILE);
      this.workingFile = createFile(this.workingFolder(), fileName);

      // check if we have write access to the file
      if (!this.workingFile.canWrite()) {
        final String[] values = { WORKING_FILE, fileName};
        throw new CSVException(CSVError.NOTWRITABLE, values);
      }
    }

    return this.workingFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorFile
  /**
   ** Creates the abstract {@link File} to access the file which is produced
   ** if an error of this scheduled task occurs.
   **
   ** @return                    the abstract {@link File} to access the error
   **                            file.
   **
   ** @throws TaskException      if the abstract file handle could not be
   **                            created.
   */
  protected final File errorFile()
    throws TaskException {

    if (this.errorFile == null) {
      // create a timestamp that makes the file unique in the file system
      final GregorianCalendar now = new GregorianCalendar();
      final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss.SSSZ");

      String fileName = this.dataFile().getName();
      // search for the last period in the file name
      int pos = fileName.lastIndexOf(SystemConstant.PERIOD);
      if (pos != -1) {
        fileName = fileName.substring(0, pos) + "-" + fmt.format(now.getTime()) + fileName.substring(pos);
      }
      else {
       fileName = fileName + "-" + fmt.format(now.getTime());
      }

      // create the file handle
      this.errorFile = createFile(this.errorFolder(), fileName);
      try {
        // create always the file in the file system
        errorFile.createNewFile();
      }
      catch (IOException e) {
        throw TaskException.general(e);
      }

      // check if we have read and write access to the file
      if (this.errorFile.exists()) {
        final String[] values = { PROCEED_FILE, fileName};
        // check if we have read access to the file
        if (!this.errorFile.canRead())
          throw new CSVException(CSVError.NOTREADABLE, values);

        // check if we have write access to the file
        if (!this.errorFile.canWrite())
          throw new CSVException(CSVError.NOTWRITABLE, values);
      }
    }
    return this.errorFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Populates the entries which are changed since the last execution of the
   ** scheduled task.
   **
   ** @param  bulkSize           number of lines to read.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  private List<Map<String, Object>> populate(int bulkSize)
    throws TaskException {

    final String method ="populate";
    trace(method, SystemMessage.METHOD_ENTRY);

    final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

    // check if the character stream associated with the processor is ready
    // this signals that the stream may be closed that happens if we reach the
    // end of file (see below)
    if (!this.processor.ready())
      return result;

    try {
      // fetch all entities which are contained in the working file
      while (bulkSize-- > 0)
        result.add(this.processor.readEntity(true));
    }
    catch (EOFException e) {
      this.processor.close();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }
}