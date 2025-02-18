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

    File        :   Controller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Controller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-18  DSteding    First release version
*/

package oracle.iam.identity.txt.service;

import java.io.File;
import java.io.IOException;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.utility.file.FlatFileError;
import oracle.iam.identity.utility.file.FlatFileException;
import oracle.iam.identity.utility.file.FlatFileOperation;
import oracle.iam.identity.utility.file.FlatFileDescriptor;
import oracle.iam.identity.utility.file.FlatFileDescriptorFactory;

import oracle.iam.identity.txt.resource.ControllerBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Controller
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Controller</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Scheduler which handles data
 ** provided by or delivered to a flat file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Controller extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Defines the string to specify ISO-LATIN-1 file encoding. */
  protected static final String  ISO_8859_1   = "ISO-8859-1";

  /** Defines the string to specify UTF-8 file encoding. */
  protected static final String  UTF_ENCODING = "UTF-8";

  /** Defines the string to specify Windows  file encoding. */
  protected static final String  WINDOWS_1252 = "windows-1252";

  /**
   ** Attribute tag which must be defined on a scheduled task to tell the
   ** process how the raw files are encoded.
   */
  protected static final String  FILE_ENCODING   = "File Encoding";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the file that specifies the mapping for import.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATA_DESCRIPTOR = "Data Descriptor";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the raw file to reconcile.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATA_FILE       = "Data Filename";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the folder the file to reconcile is located in the filesystem.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATA_FOLDER     = "Data Folder";

  /**
   ** Attribute tag which can be defined on a scheduled task to hold the
   ** location where the raw files should be copied to after they are detected
   ** in the data folder; the working folder in the filesystem.
   */
  protected static final String  WORKING_FOLDER  = "Working Folder";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the
   ** location where of the raw files should be copied after they are proceed by
   ** the preprocessor and might be some errors detected.
   */
  protected static final String  ERROR_FOLDER    = "Error Folder";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the descriptor of the CSV structure */
  private FlatFileDescriptor     dataDescriptor  = null;

  /** the abstract file which this connector should be use as the data folder */
  private File                   dataFolder      = null;

  /** the abstract file which this connector should be use as the working folder */
  private File                   workingFolder   = null;

  /**
   ** the abstract file which this connector should be use as the folder to
   ** place there the files which contains errors
   */
  private File                   errorFolder     = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Controller</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  public Controller(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataDescriptorFileName
  /**
   ** Returns the absolute path to the file which contains the descriptor of the
   ** files.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #DATA_DESCRIPTOR}.
   **
   ** @return                    the absolute path to the file which contains
   **                            the descriptor.
   */
  protected final String dataDescriptorFileName() {
    return stringValue(DATA_DESCRIPTOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFileName
  /**
   ** Returns the name of the file which is used to read or write raw data.
   ** <br>
   ** Convenience method to shortens the access to the task attribute
   ** {@link #DATA_FILE}.
   **
   ** @return                    the name of the file which is used to read or
   **                            write raw data.
   */
  protected final String dataFileName() {
    return stringValue(DATA_FILE);
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
  // Method:   workingFolder
  /**
   ** Returns the abstract {@link File} handle of the file which is used as the
   ** working folder.
   **
   ** @return                    the abstract {@link File} handle of the file
   **                            which is used as the working folder.
   */
  protected final File workingFolder() {
    return this.workingFolder;
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
  // Method:   dataDescriptor
  /**
   ** Returns or creates the {@link FlatFileDescriptor} used by this scheduled
   ** task to extract or write the payload from or to a <code>CSVRecord</code>.
   **
   ** @return                    the {@link FlatFileDescriptor} currently
   **                            assigned to this scheduled task or the created
   **                            {@link FlatFileDescriptor} if no descriptor is
   **                            associated with this task at time of invocation
   **                            of this method.
   **
   ** @throws TaskException      if the CSV file descriptor cannot be created.
   */
  protected final FlatFileDescriptor dataDescriptor()
    throws TaskException {

    if (this.dataDescriptor != null)
      return this.dataDescriptor;

    final String method = "dataDescriptor";
    final String path   = stringValue(DATA_DESCRIPTOR);
    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session  = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      if (document == null)
        throw new TaskException(TaskError.METADATA_OBJECT_NOTFOUND, path);

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      // create a flat file descriptor
      this.dataDescriptor = FlatFileDescriptorFactory.create(document);
      // produce the logging output only if the logging level is enabled for
      if (this.logger != null && this.logger.debugLevel())
        debug(method, this.dataDescriptor.toString());

      return this.dataDescriptor;
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
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
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDataFolder
  /**
   ** Creates the abstract {@link File} used as the data folder.
   ** <br>
   ** Convenience method to shortens the creation of the data folder specified
   ** by the task attribute {@link #DATA_FOLDER}.
   **
   ** @return                    the abstract {@link File} representing the
   **                            data directory.
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
  // Method:   createWorkFolder
  /**
   ** Creates the abstract {@link File} used as the working folder.
   ** <br>
   ** Convenience method to shortens the creation of the working folder
   ** specified by the task attribute {@link #WORKING_FOLDER}.
   **
   ** @return                    the abstract {@link File} representing the
   **                            working directory.
   **
   ** @throws TaskException      if the folder doesn't fit the requirements.
   */
  protected final File createWorkFolder()
    throws TaskException {

    if (this.workingFolder == null)
      this.workingFolder = createFolder(WORKING_FOLDER);

    return this.workingFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createErrorFolder
  /**
   ** Creates the abstract {@link File} used as the error folder.
   ** <br>
   ** Convenience method to shortens the creation of the error folder specified
   ** by the task attribute {@link #ERROR_FOLDER}.
   **
   ** @return                    the abstract {@link File} representing the
   **                            directory for files with errors.
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
   ** <code>fileName</code> in the specified <code>folderName</code> and check
   ** if the resulting file is writable by this IT Resource.
   **
   ** @param  pathName           the folder in the filesystem where the
   **                            specified <code>fileName</code> should be
   **                            contained.
   ** @param  fileName           the name of the file in the filesystem file to
   **                            wrapp.
   **
   ** @return                    the abstract {@link File} constructed from
   **                            <code>pathName</code> and
   **                            <code>fileName</code>.
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

    File file = null;
    if (StringUtility.isEmpty(fileName))
      try {
        file = FileSystem.createTempFile(FlatFileOperation.TMPPREFIX, FlatFileOperation.TMPEXTENSION, this.workingFolder());
      }
      catch (IOException e) {
        throw TaskException.general(e);
      }
    else
      file = createFile(folder.getAbsolutePath(), fileName);

    // check it's really a file
    if (file.isDirectory()) {
      final String[] values = { fileName , file.getAbsolutePath()};
      throw new FlatFileException(FlatFileError.NOTAFILE, values);
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
  private final File createFolder(final String folderName)
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
      throw new FlatFileException(FlatFileError.NOTAFOLDER, values);
    }

    // check if the abstract file writable
    if (!file.canWrite()) {
      String[] values = { folderName, path };
      throw new FlatFileException(FlatFileError.NOTWRITABLE, values);
    }

    return file;
  }
}