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

    File        :   Controller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Controller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.service;

import java.io.IOException;
import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import oracle.iam.identity.utility.file.CSVError;
import oracle.iam.identity.utility.file.CSVException;
import oracle.iam.identity.utility.file.CSVDescriptor;
import oracle.iam.identity.utility.file.CSVOperation;
import oracle.iam.identity.utility.file.CSVDescriptorFactory;

import oracle.iam.identity.csv.resource.ControllerBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Controller
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Controller</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Scheduler which handles data
 ** provided by or delivered to a CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 */
public abstract class Controller extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Defines the string to specify ISO-LATIN-1 file encoding. */
  protected static final String  ISO_8859_1              = "ISO-8859-1";

  /** Defines the string to specify UTF-8 file encoding. */
  protected static final String  UTF_ENCODING            = "UTF-8";

  /** Defines the string to specify Windows  file encoding. */
  protected static final String  WINDOWS_1252            = "windows-1252";
  /**
   ** Attribute tag which must be defined on a scheduled task to tell the
   ** process how the raw files are encoded.
   */
  protected static final String  FILE_ENCODING           = "File Encoding";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the file that specifies the mapping for import.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATA_DESCRIPTOR         = "Data Descriptor";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the raw file to reconcile.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATA_FILE               = "Data Filename";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the name
   ** of the folder the file to reconcile is located in the filesystem.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String  DATA_FOLDER             = "Data Folder";

  /**
   ** Attribute tag which can be defined on a scheduled task to hold the
   ** location where the raw files should be copied to after they are detected
   ** in the data folder; the working folder in the filesystem.
   */
  protected static final String  WORKING_FOLDER          = "Working Folder";

  /**
   ** Attribute tag which must be defined on a scheduled task to hold the
   ** location where of the raw files should be copied after they are proceed by
   ** the preprocessor and might be some errors detected.
   */
  protected static final String  ERROR_FOLDER            = "Error Folder";

  /**
   ** Attribute tag which must be defined on a scheduled task to tell the
   ** process by which character the single-valued fields are separated.
   */
  protected static final String  SINGLE_VALUED_SEPARATOR = "Single-Valued Separator";

  /**
   ** Attribute tag which must be defined on a scheduled task to tell the
   ** process by which character the multi-valued fields are separated.
   */
  protected static final String  MULTI_VALUED_SEPARATOR  = "Multi-Valued Separator";

  /**
   ** Attribute tag which might be defined on a scheduled task to tell the
   ** process by which character is use to eclose fields.
   */
  protected static final String  ENCLOSING_CHARACTER     = "Enclosing Character";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the character used to separate single-valued fields */
  protected char                 singleValueSeparator    = SystemConstant.COMMA;

  /** the character used to separate multi-valued fields */
  protected char                 multiValueSeparator     = SystemConstant.COMMA;

  /** the character enclosing the fields */
  protected char                 enclosingCharacter      = SystemConstant.QUOTE;

  /** the descriptor of the CSV structure */
  private CSVDescriptor          dataDescriptor          = null;

  /** the abstract file which this connector should be use as the data folder */
  private File                   dataFolder              = null;

  /** the abstract file which this connector should be use as the working folder */
  private File                   workingFolder           = null;

  /**
   ** the abstract file which this connector should be use as the folder to
   ** place there the files which contains errors
   */
  private File                   errorFolder             = null;

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
   ** Returns or creates the {@link CSVDescriptor} used by this scheduled task
   ** to extract or write the payload from or to a <code>CSVRecord</code>.
   **
   ** @return                    the {@link CSVDescriptor} currently assigned
   **                            to this scheduled task or the created
   **                            {@link CSVDescriptor} if no descriptor is
   **                            associated with this task at time of invocation
   **                            of this method.
   **
   ** @throws TaskException      if the CSV file descriptor cannot be created.
   */
  protected final CSVDescriptor dataDescriptor()
    throws TaskException {

    if (this.dataDescriptor == null)
        // create a CVS descriptor
        this.dataDescriptor = CSVDescriptorFactory.create(descriptorURL(dataDescriptorFileName()));

    return this.dataDescriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descriptorURL
  /**
   ** Returns the {@link URL} to the file of the descriptor to use.
   **
   ** @param descriptorFileName  the absolute path to the file which contains
   **                            the descriptor.
   **
   ** @return                    the {@link URL} to the file of the descriptor
   **                            to use.
   */
  protected URL descriptorURL(final String descriptorFileName) {
    final String method = "descriptorURL";
    trace(method, SystemMessage.METHOD_ENTRY);

    // produce the logging output only if the logging level is enabled for
    if (this.logger().debugLevel())
      debug(method, ControllerBundle.format(ControllerMessage.CREATING_URL, descriptorFileName));
    try {
      return new URL(descriptorFileName);
    }
    catch (MalformedURLException e) {
      // assume it is related to the file system
      return FileSystem.url(descriptorFileName);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
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

    String character = parameter(SINGLE_VALUED_SEPARATOR);
    this.singleValueSeparator = StringUtility.isEmpty(character) ? SystemConstant.COMMA : character.charAt(0);

    character = parameter(MULTI_VALUED_SEPARATOR);
    this.multiValueSeparator  = StringUtility.isEmpty(character) ? SystemConstant.COMMA : character.charAt(0);

    character   = parameter(ENCLOSING_CHARACTER);
    this.enclosingCharacter   = StringUtility.isEmpty(character) ? SystemConstant.QUOTE : character.charAt(0);

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
        file = FileSystem.createTempFile(CSVOperation.TMPPREFIX, CSVOperation.TMPEXTENSION, this.workingFolder());
      }
      catch (IOException e) {
        throw TaskException.general(e);
      }
    else
      file = createFile(folder.getAbsolutePath(), fileName);

    // check it's really a file
    if (file.isDirectory()) {
      final String[] values = { fileName , file.getAbsolutePath()};
      throw new CSVException(CSVError.NOTAFILE, values);
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
      throw new CSVException(CSVError.NOTAFOLDER, values);
    }

    // check if the abstract file writable
    if (!file.canWrite()) {
      String[] values = { folderName, path };
      throw new CSVException(CSVError.NOTWRITABLE, values);
    }

    return file;
  }
}