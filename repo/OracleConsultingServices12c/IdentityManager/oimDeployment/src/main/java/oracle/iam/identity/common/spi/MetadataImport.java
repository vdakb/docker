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

    File        :   MetadataImport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    MetadataImport.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;

import org.apache.tools.ant.BuildException;

import oracle.mds.core.MDSSession;
import oracle.mds.core.MOReference;
import oracle.mds.core.MDSInstance;
import oracle.mds.core.StreamedObject;
import oracle.mds.core.MetadataExistsException;

import oracle.mds.config.FileTypeInfo;
import oracle.mds.config.FileTypeManager;
import oracle.mds.config.DocumentContentType;

import oracle.mds.exception.MDSException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.MetadataProvider;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class MetadataImport
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle metadata artifacts that are
 ** imported in the Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MetadataImport extends MetadataProvider {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataImport</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public MetadataImport(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Import the metadata definition into a Oracle Metadata Store represented by
   ** {@link MDSInstance}.
   **
   ** @param  instance           the {@link MDSInstance} used to perform the
   **                            operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void execute(final MDSInstance instance)
    throws ServiceException {

    // create a session to the metadata store without any specific session
    // options and state handlers
    MDSSession session = createSession(instance);

    try {
      for (String namespace : this.fileMapping.keySet()) {
        final Map<String, List<File>> pathMapping = this.fileMapping.get(namespace);
        for (String path : pathMapping.keySet()) {
          final String     targetPath = createPath(namespace, path);
          final List<File> fileList = pathMapping.get(path);
          for (File file : fileList) {
            final String name = file.getName();
            final int    pos = name.indexOf(SystemConstant.PERIOD);
            final String extension = name.substring(pos + 1);
            try {
              if (streamedObject(instance, extension))
                uploadStreamedObject(session, targetPath, file);
              else
                uploadDocumentObject(session, targetPath, file);
            }
            catch (MDSException e) {
              final String[] args = { file.getName(), e.getLocalizedMessage() };
              error(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_UPLOAD, args));
              if (failonerror()) {
                // rollback all changes done so far
                session.cancelChanges();
                throw new ServiceException(ServiceError.UNHANDLED, e);
              }
            }
          }
        }
      }
    }
    finally {
      try {
        session.flushChanges();
      }
      catch (MDSException e) {
        error(ServiceResourceBundle.format(ServiceError.METADATA_SESSION_COMMIT, e.getLocalizedMessage()));
        if (failonerror())
          throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addFile
  /**
   ** Called to add a sinlge file to the set of file to import.
   **
   ** @param  namespace          the root path to add the files.
   ** @param  path               the subordinated path in the namespace the
   **                            files will be imported to.
   ** @param  file               the {@link File} where an import has get from.
   **
   ** @throws BuildException     if the specified {@link File} is already part
   **                            of this import operation.
   */
  @Override
  public void addFile(final String namespace, final String path, final File file)
    throws BuildException {

    // check if we are able to import the file
    if (!file.exists())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_NOTEXISTS, file.getName()));

    // we cannot allow to handle a omplete directory
    if (file.isDirectory())
      throw new BuildException(FeatureResourceBundle.string(FeatureError.IMPORT_FILE_ISDIRECTORY));

    // check if we have permissions to read the file
    if (!file.canRead())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_NOPERMISSION, file.getName()));

    // ensure inheritance
    super.addFile(namespace, path, file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uploadStreamedObject
  /**
   ** Import the metadata definition into Oracle Metadata Store represented by
   ** {@link MDSSession} to the specified path from the given file.
   **
   ** @param  session            the {@link MDSSession} used to perform the
   **                            operation.
   ** @param  path               the full qualified path where the file
   **                            <code>file</code> has to be loaded to.
   ** @param  file               the file in the local filesystem to upload.
   **
   ** @throws MDSException       if the {@link FileInputStream} could not be
   **                            uploaded to the metadata store.
   ** @throws ServiceException   in case an error does occur.
   */
  private void uploadStreamedObject(final MDSSession session, final String path, final File file)
    throws MDSException
    ,      ServiceException {

    FileInputStream stream = null;
    try {
      stream = new FileInputStream(file);
    }
    catch (IOException e) {
      // rollback all changes done so far
      session.cancelChanges();
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }

    final String[] arguments = { ServiceResourceBundle.string(ServiceMessage.METADATA_DOCUMENT_STREAMED_MODE), createPath(path, file.getName()) };
    try {
      StreamedObject.createStreamedObject(session, arguments[1], stream);
      info(ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_UPLOADED, arguments[1]));
    }
    catch (MetadataExistsException e) {
      int response = confirmOverride(file.getName(), path);
      if (response == 0) {
        StreamedObject.deleteStreamedObject(session, MOReference.create(arguments[1]));
        warning(ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_UPLOAD_DELETED, arguments));
        StreamedObject.createStreamedObject(session, arguments[1], stream);
        info(ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_UPLOADED, arguments));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uploadDocumentObject
  /**
   ** Import the metadata definition into Oracle Metadata Store represented by
   ** {@link MDSSession} to the specified path from the given file.
   **
   ** @param  session            the {@link MDSSession} used to perform the
   **                            operation.
   ** @param  path               the full qualified path where the file
   **                            <code>file</code> has to be loaded to.
   ** @param  file               the file in the local filesystem to upload.
   **
   ** @throws MDSException       if the {@link Document} could not be uploaded
   **                            to the metadata store.
   ** @throws ServiceException   if a parsing error occurs processing
   **                            the {@link File} <code>file</code>
   */
  private void uploadDocumentObject(final MDSSession session, final String path, final File file)
    throws MDSException
    ,      ServiceException {

    // create the DocumentBuilderinstances
    createBuilder(null);
    final Document document = createDocument(file);

    final String[] arguments = { ServiceResourceBundle.string(ServiceMessage.METADATA_DOCUMENT_DOCUMENT_MODE), createPath(path, file.getName()) };
    try {
      session.createMetadataObject(arguments[1], document);
      info(ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_UPLOADED, arguments));
    }
    catch (MetadataExistsException e) {
      int response = 0;
      if (!this.forceOverride())
        response = confirmOverride(file.getName(), path);
      if (response == 0) {
        session.deleteMetadataObject(MOReference.create(arguments[1]));
        warning(ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_UPLOAD_DELETED, arguments));
        session.createMetadataObject(arguments[1], document);
        info(ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_UPLOADED, arguments));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamedObject
  /**
   ** Checks if a file with the specified extension hast to be uploaded as a
   ** {@link StreamedObject}.
   **
   ** @param  instance           the {@link MDSInstance} used to perform the
   **                            operation.
   ** @param  extension          the file extension to check.
   **
   ** @return                    <code>true</code> if the file for the specified
   **                            extension as to be uploaded as a stream;
   **                            otherwise <code>false</code>.
   */
  private boolean streamedObject(final MDSInstance instance, final String extension) {
    FileTypeManager manager = instance.getFileTypeManager();
    if (manager == null)
      return false;

    FileTypeInfo info = manager.getFileTypeInfoObject(extension);
    if (info == null)
      return false;

    DocumentContentType contentType = info.getContentType();
    return (contentType != null) && (contentType.equals(DocumentContentType.STREAMED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirmOverride
  /**
   ** Aks user for confirmation to override an existing file.
   **
   ** @param  path               the pathname to the file to override.
   ** @param  file               the name of the file to override.
   **
   ** @return                    <code>0</code> if the file should be override.
   */
  private int confirmOverride(final String file, final String path) {
    int response = 0;
    if (!this.forceOverride()) {
      response = JOptionPane.showConfirmDialog
        (null
      , ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_CONFIRMATION_MESSAGE, file, path)
      , ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_CONFIRMATION_TITLE,   file)
      , JOptionPane.YES_NO_OPTION
      , JOptionPane.QUESTION_MESSAGE
      );
    }
    return response;
  }
}