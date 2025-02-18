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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   MetadataExport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    MetadataExport.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;

import java.io.File;

import javax.swing.JOptionPane;

import org.apache.tools.ant.BuildException;

import oracle.mds.naming.ReferenceException;

import oracle.mds.core.MDSSession;
import oracle.mds.core.MDSInstance;
import oracle.mds.core.MetadataObject;
import oracle.mds.core.MetadataNotFoundException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.hst.deployment.task.MetadataProvider;

////////////////////////////////////////////////////////////////////////////////
// class MetadataExport
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle metadata artifacts that are
 ** exported from the Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MetadataExport extends MetadataProvider {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean createFolder = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataExport</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public MetadataExport(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setForceOverride
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>forceOverride</code>.
   **
   ** @param  forceOverride    <code>true</code> to override the existing file
   **                          without to aks for user confirmation.
   */
  public void setForceOverride(final boolean forceOverride) {
    this.forceOverride(forceOverride);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFolder
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>createFolder</code>.
   **
   ** @param  createFolder       <code>true</code> if the path to the file
   **                            should be created if its not exists.
   */
  public void createFolder(final boolean createFolder) {
    this.createFolder = createFolder;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   createFolder
  /**
   ** Returns <code>true</code> if the path to the file should be created if
   ** its not exists.
   **
   ** @return                    <code>true</code> if the path to the file
   **                            should be created if its not exists;
   **                            otherwise <code>false</code>
   */
  public final boolean createFolder() {
    return this.createFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Export the metadata definition from a Oracle Metadata Store represented by
   ** {@link MDSInstance}.
   **
   ** @param  instance           the {@link MDSInstance} used to perform the
   **                            operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void execute(final MDSInstance instance)
    throws ServiceException {

    // create the DocumentBuilder and Transformer instances
    createBuilder(new MetadataDocumentHandler());
    createTransformer();

    // create a session to the metadata store without any specific session
    // options and state handlers
    MDSSession session = createSession(instance);

    for (String namespace : this.fileMapping.keySet()) {
      final Map<String, List<File>> pathMapping = this.fileMapping.get(namespace);
      for (String path : pathMapping.keySet()) {
        final String     sourcePath = createPath(namespace, path);
        final List<File> fileList = pathMapping.get(path);
        for (File file : fileList) {
          final String sourceFile = createPath(sourcePath, file.getName());
          try {
            MetadataObject objectFile = session.getMetadataObject(sourceFile);
            this.document(objectFile.getDocument());
            exportDocument(file);
          }
          catch (MetadataNotFoundException e) {
            final String[] arguments = { file.getName(), sourcePath };
            error(ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_NOTEXISTS, arguments));
          }
          catch (ReferenceException e) {
            final String[] arguments = { sourcePath, e.getLocalizedMessage() };
            if (failonerror())
              throw new ServiceException(ServiceError.METADATA_DOCUMENT_REFERENCE, arguments);
            else
              error(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_REFERENCE, arguments));
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addFile (overridden)
  /**
   ** Called to add a sinlge file to the set of file to export.
   **
   ** @param  namespace          the root path to add the files.
   ** @param  path               the subordinated path in the namespace the
   **                            files will be export from.
   ** @param  file               the {@link File} where an export will be
   **                            written to.
   **
   ** @throws BuildException   if the specified {@link File} is already part
   **                            of this import operation.
   */
  @Override
  public void addFile(final String namespace, final String path, final File file)
    throws BuildException {

    // we cannot allow to handle a omplete directory
    if (file.isDirectory())
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_ISDIRECTORY));

    // check if we have permissions to write the file if it's already exists
    if (!this.createFolder && !file.getParentFile().exists())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.EXPORT_FILE_NODIRECTORY, file.getParent()));

    // check if we have permissions to write the file if it's already exists
    if (file.exists() && !file.canWrite())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.EXPORT_FILE_NOPERMISSION, file.getName()));

    // ensure inheritance
    super.addFile(namespace, path, file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportDocument
  /**
   ** Writes the XML document to the local file system
   **
   ** @param  exportFile         the {@link File} to write as an XML document to
   **                            the local file system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void exportDocument(final File exportFile)
    throws ServiceException {

    int response = 0;
    if (!this.forceOverride() && exportFile.exists()) {
      response = JOptionPane.showConfirmDialog
        (null
      , ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_CONFIRMATION_MESSAGE, exportFile.getName(), exportFile.getParent())
      , ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_CONFIRMATION_TITLE,   exportFile.getName())
      , JOptionPane.YES_NO_OPTION
      , JOptionPane.QUESTION_MESSAGE
      );
    }

    // if requested in the task configuration create the path to the file on the
    // fly
    final File folder = exportFile.getParentFile();
    if (this.createFolder && !folder.exists())
      folder.mkdir();

    if (response == 0)
      writeDocument(exportFile);
  }
}