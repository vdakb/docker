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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Resource Connector

    File        :   Controller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Controller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service;

import java.util.Date;
import java.util.GregorianCalendar;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import oracle.iam.identity.ots.resource.ControllerBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Controller
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Controller</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Scheduler which handles data
 ** provided by or delivered to a XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public abstract class Controller extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on a scheduled task to tell the
   ** process how the raw files are encoded.
   */
  protected static final String  FILE_ENCODING = "File Encoding";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the raw file to reconcile.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATAFILE      = "Data Filename";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the folder the file to reconcile is located in the filesystem.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATA_FOLDER   = "Data Folder";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the
   ** location where of the raw files should be copied after they are proceed by
   ** the preprocessor and might be some errors detected.
   */
  protected static final String  ERROR_FOLDER  = "Error Folder";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstract file which this connector should be use as the data folder */
  private File                   dataFolder    = null;

  /**
   ** the abstract file which this connector should be use as the folder to
   ** place there the files which contains errors
   */
  private File                   errorFolder   = null;

  /** the abstract file which contains the data */
  private File                   dataFile      = null;

  /** the abstract file which contains the produced errors */
  private File                   errorFile     = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Controller</code> which use the specified category for
   ** logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected Controller(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFileName
  /**
   ** Returns the name of the file which is used to read or write raw data.
   ** <br>
   ** Convenience method to shortens the access to the task attribute
   ** {@link #DATAFILE}.
   **
   ** @return                    the name of the file which is used to read or
   **                            write raw data.
   */
  protected final String dataFileName() {
    return stringValue(DATAFILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFolder
  /**
   ** Returns the abstract {@link File} handle of the file which is used as the
   ** data folder.
   **
   ** @return                    the abstract {@link File} handle of the file
   **                            which is used as the data folder.
   */
  protected final File dataFolder() {
    return this.dataFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorFolder
  /**
   ** Returns the abstract {@link File} handle of the file which is used as the
   ** error folder.
   **
   ** @return                    the abstract {@link File} handle of the file
   **                            which is used as the error folder.
   */
  protected final File errorFolder() {
    return this.errorFolder;
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

    if (this.dataFile == null) {
      final String fileName = stringValue(DATAFILE);
      // create the file handle
      this.dataFile = createFile(dataFolder(), fileName);
    }
    return this.dataFile;
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
        this.errorFile.createNewFile();
      }
      catch (IOException e) {
        throw TaskException.general(e);
      }

      // check if we have read and write access to the file
      if (this.errorFile.exists()) {
        final String[] values = { DATAFILE, fileName};
        // check if we have read access to the file
        if (!this.errorFile.canRead())
          throw new ControllerException(ControllerError.FILE_NOT_READABLE, values);

        // check if we have write access to the file
        if (!this.errorFile.canWrite())
          throw new ControllerException(ControllerError.FILE_NOT_WRITABLE, values);
      }
    }
    return this.errorFile;
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

    createDataFolder();
    createErrorFolder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFileAvailable
  /**
   ** Check if a file is in the transfer directory.
   **
   ** @param  timeStamp          the timestamp this task was last time executed.
   **
   ** @return                    <code>true</code> if a data file is avaliable
   **                            in the transfare directory which modify
   **                            timestamp is after the specified timestamp:
   **                            otherwise <code>false</code>.
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
      // check if we have read access to the file
      if (!this.dataFile.canRead()) {
        final String[] values = { DATAFILE, dataFile.getName()};
        throw new ControllerException(ControllerError.FILE_NOT_READABLE, values);
      }

      // create a new date instance by passing last modified information
      final Date lastModified = new Date(dataFile.lastModified());
      // compare both dates to decided if a change on the files happend by:
      // if the outcome of the comparision is less than or equal to zero we
      // assume the file is untouched
      final int changed = lastModified.compareTo(timeStamp);
      if (changed <= 0) {
        exists = false;
        info(TaskBundle.format(TaskMessage.NOTCHANGED, dataFile.getName()));
      }
      // set the last modified date of the file as the timestamp on which this
      // task has last reconciled
      lastReconciled(lastModified);
    }
    else
      info(TaskBundle.format(TaskMessage.NOTAVAILABLE, DATA_FOLDER, dataFile.getName()));

    trace(method, SystemMessage.METHOD_EXIT);
    return exists;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDataFolder
  /**
   ** Creates the abstract {@link File} used as the data folder.
   ** <br>
   ** Convenience method to shortens the creation of the data folder specified
   ** by the task attribute {@link #DATA_FOLDER}.
   **
   ** @return                    the validated {@link File} for the
   **                            {@link #DATA_FOLDER} property.
   **
   ** @throws TaskException      if the folder doesn't fit the requirements.
   */
  protected final File createDataFolder()
    throws TaskException {

    if (this.dataFolder == null)
      this.dataFolder = createFolder(DATA_FOLDER);

    return this.dataFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createErrorFolder
  /**
   ** Creates the abstract {@link File} used as the error folder.
   ** <br>
   ** Convenience method to shortens the creation of the error folder specified
   ** by the task attribute {@link #ERROR_FOLDER}.
   **
   ** @return                    the validated {@link File} for the
   **                            {@link #ERROR_FOLDER} property.
   **
   ** @throws TaskException      if the folder doesn't fit the requirements.
   */
  protected final File createErrorFolder()
    throws TaskException {

    if (this.errorFolder == null)
      this.errorFolder = createFolder(ERROR_FOLDER);

    return this.errorFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFile
  /**
   ** Creates the abstract {@link File} for the specified
   ** <code>fileName</code> in the specified <code>pathName</code> and check
   ** if the resulting file is writable by this IT Resource.
   **
   ** @param  pathName           the folder in the filesystem where the
   **                            specified <code>fileName</code> should be
   **                            contained.
   ** @param  fileName           the name of the file in the filesystem file to
   **                            wrap.
   **
   ** @return                    the validated {@link File} for the specified
   **                            <code>fileName</code> in the specified
   **                            <code>pathName</code>.
   */
  protected final File createFile(String pathName, String fileName) {
    final String method = "createFile";
    if (!pathName.endsWith(File.separator))
      pathName = pathName + File.separator;

    final String path = pathName + fileName;
    // produce the logging output only if the logging level is enabled for
    if (this.logger().debugLevel())
      debug(method, ControllerBundle.format(ControllerMessage.CREATING_FILE, path));

    return new File(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFile
  /**
   ** Creates the abstract {@link File} for the specified
   ** <code>fileName</code> in the specified <code>folderName</code> and check
   ** if the resulting file is writable by this scheduled task.
   **
   ** @param  folder             the abstract {@link File} of the filesystem
   **                            folder where the specified
   **                            <code>fileName</code> should be contained.
   ** @param  fileName           the name of the filesystem file to create and
   **                            check.
   **
   ** @return                    the abstract {@link File} constructed from
   **                            <code>folder</code> and <code>fileName</code>.
   **
   ** @throws TaskException      if the file doesn't fit the requirements.
   */
  protected File createFile(final File folder, final String fileName)
    throws TaskException {

    final File file = createFile(folder.getAbsolutePath(), fileName);
    // check it's really a file
    if (file.isDirectory()) {
      final String[] values = { fileName , file.getAbsolutePath()};
      throw new ControllerException(ControllerError.FILE_NOT_FILE, values);
    }

    return file;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFolder
  /**
   ** Creates the abstract {@link File} for the specified
   ** <code>folderName</code> and check if the resulting file is writable by
   ** this scheduled task.
   **
   ** @param  folderName         the name of the filesystem folder to create and
   **                            check.
   **
   ** @throws TaskException      if the folder doesn't fit the requirements.
   */
  private File createFolder(final String folderName)
    throws TaskException {

    final String method = "createFolder";
    final String path   = stringValue(folderName);
    // produce the logging output only if the logging level is enabled for
    if (this.logger().debugLevel())
      debug(method, ControllerBundle.format(ControllerMessage.CREATING_FOLDER, path));

    final File   file   = new File(path);
    // check if the abstract file is a directory
    if (!file.isDirectory()) {
      String[] values = { folderName, path };
      throw new ControllerException(ControllerError.FILE_NOT_FOLDER, values);
    }

    // check if the abstract file writable
    if (!file.canWrite()) {
      String[] values = { folderName, path };
      throw new ControllerException(ControllerError.FILE_NOT_WRITABLE, values);
    }

    return file;
  }
}