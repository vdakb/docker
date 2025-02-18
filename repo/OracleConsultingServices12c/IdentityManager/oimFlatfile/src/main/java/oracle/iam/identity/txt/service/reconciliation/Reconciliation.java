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

    File        :   Reconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Reconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-18  DSteding    First release version
*/

package oracle.iam.identity.txt.service.reconciliation;

import java.util.Date;
import java.util.GregorianCalendar;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.text.SimpleDateFormat;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractResource;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.utility.file.FlatFileError;
import oracle.iam.identity.utility.file.FlatFileWriter;
import oracle.iam.identity.utility.file.FlatFileReader;
import oracle.iam.identity.utility.file.FlatFileProcessor;
import oracle.iam.identity.utility.file.FlatFileException;
import oracle.iam.identity.utility.file.FlatFileComparator;

import oracle.iam.identity.txt.service.Controller;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Reconciliation</code> acts as the service end point for the Oracle
 ** Identity Manager to reconcile entities from an Offline Target Application
 ** and/or System.
 ** <br>
 ** <b>Note</b>
 ** <br>
 ** Class is package protected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class Reconciliation extends Controller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which can be defined on a scheduled task to hold the name of
   ** the raw file after reconciliation.
   */
  protected static final String PROCEED_FILE            = "Proceed Filename";

  /**
   ** Attribute tag which can be defined on a scheduled task to hold the name of
   ** the raw file compared content.
   */
  protected static final String WORKING_FILE            = "Working Filename";

  /**
   ** Attribute tag which must be defined on a <code>Scheduled Task</code>
   ** to specify if the entitlement loaded from a file needs to be prefixed with
   ** the internal system identifier and/or the name of the
   ** <code>IT Resource</code>.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String ENTITLEMENT_PREFIX     = "Entitlement Prefix Required";

  /**
   ** Attribute tag which must be defined on a <code>Scheduled Task</code>
   ** to specify if the non-existing file has to be reported as an exception.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String MISSING_FILE_EXCEPTION = "Missing File Exception";

  /**
   ** Attribute tag which may be defined on a Scheduled Task to specify the
   ** entity filter to let pass through only such entities which match the
   ** specified criteria.
   ** <br>
   ** This attribute is optional.
  */
  protected static final String SEARCH_FILTER          = "Reconciliation Filter";

  /** the category of the logging facility to use */
  protected static final String LOGGER_CATEGORY        = "OCS.TXT.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the abstraction layer to describe the generic connection to the target
   ** system
   */
  private AbstractResource      resource    = null;

  /** the abstract file which contains the data */
  private File                  importNew   = null;

  /** the abstract file which contains the produced delta */
  private File                  importOld   = null;

  /** the abstract file which contains the produced delta */
  private File                  workingFile = null;

  /** the abstract file which contains the produced errors */
  private File                  errorFile   = null;

  /** the processor of the flat file structure */
  private FlatFileProcessor     processor   = null;

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
  protected Reconciliation() {
    // ensure inheritance
    this(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> with the specified logging
   ** category.
   ** <p>
   ** Unfortunately we tried to initailize the system watch here with the
   ** appropriate name of the Job but the name of the job is not know at the
   ** time the constructor is ivoked. It looks uggly in the logs if the is an
   ** object instance of the system watch that doen't know its name. For that
   ** reason we moved the initialization of the system watch to
   ** <code>initialize</code>.
   **
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  protected Reconciliation(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processor
  /**
   ** Returns the {@link FlatFileProcessor} used by this
   ** <code>Reconciliation</code> to create a <code>FlatFileRecord</code>.
   **
   ** @return                    the {@link FlatFileProcessor} used by this
   **                            scheduled task.
   */
  protected final FlatFileProcessor processor() {
    return this.processor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the {@link AbstractResource} this task is using to describe the
   ** connection parameter to the Offline System.
   **
   ** @return                    the {@link AbstractResource} this task is
   **                            using to obtain the connection parameter to the
   **                            Offline System.
   **
   ** @throws TaskException      if the {@link AbstractResource} isn't defined
   **                            in Oracle Identity Manager.
   */
  protected final AbstractResource resource()
    throws TaskException {

    if (this.resource == null)
      this.resource = new AbstractResource(this, resourceName());

    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
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
    // this will produce the trace of the configured task parameter and create
    // the abstract file paths to the data and error directories
    super.initialize();

    createWorkFolder();
    createErrorFolder();

    // obtain the configured application instance to emit the required
    // attributes belonging to IT Resource and Resource Object
    initializeInstance();
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
        FlatFileReader readerNew = new FlatFileReader(new InputStreamReader(new FileInputStream(dataFile()),    fileEncoding));
        FlatFileReader readerOld = new FlatFileReader(new InputStreamReader(new FileInputStream(proceedFile()), fileEncoding));

        // any output Stream will also be encoded as configured
        FlatFileWriter writerWrk = new FlatFileWriter(new OutputStreamWriter(new FileOutputStream(workingFile()), fileEncoding));

        // create a comparator to compare the new and previous proceed file
        FlatFileComparator comparator = new FlatFileComparator(dataDescriptor(), fileEncoding);
        // set the logging facility so we can trace what the comparator will do
        comparator.logger(logger());

        // create the working file by comparing the both files without any
        // transformation
        comparator.compare(readerOld, readerNew, writerWrk, false, workingFolder());
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
      createProcessor(new FlatFileReader(new InputStreamReader(new FileInputStream(workingFile()), parameter(FILE_ENCODING))));
    }
    catch (IOException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessor
  /**
   ** Creates the {@link FlatFileProcessor} of this scheduled task needed for
   ** execution.
   **
   ** @param  reader             the {@link FlatFileReader} the
   **                            {@link FlatFileProcessor} will use to read the
   **                            flat file.
   **
   ** @throws TaskException      if the flat file descriptor cannot be created.
   */
  protected void createProcessor(final FlatFileReader reader)
    throws TaskException {

    this.processor = new FlatFileProcessor(dataDescriptor(), reader);
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
        if (booleanValue(MISSING_FILE_EXCEPTION))
          throw new TaskException(TaskMessage.NOTCHANGED, this.importNew.getName());
        else
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
      if (booleanValue(MISSING_FILE_EXCEPTION))
        throw new TaskException(TaskMessage.NOTAVAILABLE, values);
      else
        info(TaskBundle.format(TaskMessage.NOTAVAILABLE, values));
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return exists;
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
      final String fileName = stringValue(DATA_FILE);
      // create the file handle
      this.importNew = createFile(dataFolder(), fileName);

      // check if we have read access to the file
      if (this.importNew.exists() && !this.importNew.canRead()) {
        final String[] values = { DATA_FILE, fileName};
        throw new FlatFileException(FlatFileError.NOTREADABLE, values);
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
          throw new FlatFileException(FlatFileError.NOTREADABLE, values);

        // check if we have write access to the file
        if (!this.importOld.canWrite())
          throw new FlatFileException(FlatFileError.NOTWRITABLE, values);
      }
      else {
        // if the file does not exist create a new one and write the descriptor
        // as the header to the file
        try {
          this.importOld.createNewFile();
        }
        catch (IOException e) {
          throw new FlatFileException(FlatFileError.NOTCREATABLE, fileName, e);
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
        throw new FlatFileException(FlatFileError.NOTWRITABLE, values);
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
      final SimpleDateFormat fmt  = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss.SSSZ");

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
          throw new FlatFileException(FlatFileError.NOTREADABLE, values);

        // check if we have write access to the file
        if (!this.errorFile.canWrite())
          throw new FlatFileException(FlatFileError.NOTWRITABLE, values);
      }
    }
    return this.errorFile;
  }
}