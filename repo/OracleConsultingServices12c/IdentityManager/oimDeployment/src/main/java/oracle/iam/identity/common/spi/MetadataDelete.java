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

    File        :   MetadataDelete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    MetadataDelete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;

import java.io.File;

import oracle.mds.core.MDSInstance;
import oracle.mds.core.MDSSession;
import oracle.mds.core.MOReference;

import oracle.mds.exception.MDSException;
import oracle.mds.exception.ReadOnlyStoreException;
import oracle.mds.exception.UnsupportedUpdateException;

import oracle.mds.naming.ReferenceException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.MetadataProvider;

////////////////////////////////////////////////////////////////////////////////
// class MetadataDelete
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to delete metadata artifacts from the
 ** Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MetadataDelete extends MetadataProvider {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataDelete</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public MetadataDelete(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
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

    // create a session to the metadata store without any specific session
    // options and state handlers
    final MDSSession session = createSession(instance);

    try {
      for (String namespace : this.fileMapping.keySet()) {
        final Map<String, List<File>> pathMapping = this.fileMapping.get(namespace);
        for (Map.Entry<String, List<File>> path : pathMapping.entrySet()) {
          final String     sourcePath = createPath(namespace, path.getKey());
          final List<File> fileList = path.getValue();
          for (File file : fileList) {
            final String sourceFile = createPath(sourcePath, file.getName());
            try {
              session.deleteMetadataObject(MOReference.create(sourceFile));
              warning(ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_REMOVED, ServiceResourceBundle.format(ServiceMessage.METADATA_DOCUMENT_OBJECT_MODE, sourceFile)));
            }
            catch (ReadOnlyStoreException e) {
              error(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_READONLY, file.getName(), sourcePath, e.getLocalizedMessage()));
              if (failonerror()) {
                // rollback all changes done so far
                session.cancelChanges();
                throw new ServiceException(ServiceError.UNHANDLED, e);
              }
            }
            catch (UnsupportedUpdateException e) {
              final String[] arguments = { file.getName(), sourcePath, e.getLocalizedMessage() };
              error(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_UPDATE, arguments));
              if (failonerror()) {
                // rollback all changes done so far
                session.cancelChanges();
                throw new ServiceException(ServiceError.UNHANDLED, e);
              }
            }
            catch (ReferenceException e) {
              final String[] arguments = { sourcePath, e.getLocalizedMessage() };
              error(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_REFERENCE, arguments));
              if (failonerror())
                throw new ServiceException(ServiceError.METADATA_DOCUMENT_REFERENCE, arguments);
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
   */
  @Override
  public void addFile(final String namespace, final String path, final File file) {
    // ensure inheritance
    super.addFile(namespace, path, file);
  }
}