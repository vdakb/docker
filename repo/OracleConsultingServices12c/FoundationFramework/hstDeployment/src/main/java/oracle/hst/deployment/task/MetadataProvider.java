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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   MetadataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    MetadataProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.hst.deployment.task;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LinkedHashMap;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import org.apache.tools.ant.BuildException;

import oracle.mds.core.MDSSession;
import oracle.mds.core.MDSInstance;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class MetadataProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle metadata artifacts that are
 ** uploaded or downloaded.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class MetadataProvider extends Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Map<String, Map<String, List<File>>> fileMapping = new LinkedHashMap<String, Map<String, List<File>>>();
  private   final List<File>                           flattenSet  = new ArrayList<File>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataProvider</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  protected MetadataProvider(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend, INDENT, INDENTNUMBER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ServiceProvider)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.fileMapping.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.METADATA_NAMESPACE_MANDATORY));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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
  public abstract void execute(final MDSInstance instance)
    throws ServiceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addFile
  /**
   ** Called to add a sinlge file to the set of file to handle.
   **
   ** @param  namespace          the root path to add the files.
   ** @param  path               the subordinated path in the namespace the
   **                            files is located in the metadata store.
   ** @param  file               the {@link File} handle.
   **
   ** @throws BuildException   if the specified {@link File} is already part
   **                            of this import operation.
   */
  public void addFile(final String namespace, final String path, final File file) {

    // check if we have this file already
    if (this.flattenSet.contains(file))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_ONLYONCE, file.getAbsolutePath()));

    Map<String, List<File>> namespaceMapping = this.fileMapping.get(namespace);
    if (namespaceMapping == null) {
      namespaceMapping = new LinkedHashMap<String, List<File>>();
      this.fileMapping.put(namespace, namespaceMapping);
    }

    List<File> fileList = namespaceMapping.get(path);
    if (fileList == null) {
      fileList = new LinkedList<File>();
      namespaceMapping.put(path, fileList);
    }

    this.flattenSet.add(file);
    fileList.add(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSession
  /**
   ** Creates a session to the metadata definition.
   **
   ** @param  instance           the {@link MDSInstance} used to create the
   **                            {@link MDSSession}.
   **
   ** @return                    the created {@link MDSSession}.
   */
  public MDSSession createSession(final MDSInstance instance) {
    // create a session to the metadata store without any specific session
    // options and state handlers
    MDSSession session = null;
    trace(ServiceResourceBundle.string(ServiceMessage.METADATA_SESSION_CREATE));
    session = instance.createSession(null, null);
    trace(ServiceResourceBundle.string(ServiceMessage.METADATA_SESSION_CREATED));
    return session;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPath
  /**
   ** Creates absolute path.
   **
   ** @param  parent             the pathname string of this abstract pathname's
   **                            parent, or <code>null</code> if
   **                            <code>parent</code> pathname does not name a
   **                            parent directory.
   ** @param  name               the name of the file or directory denoted by
   **                            this abstract pathname.
   **
   ** @return                    the absolute pathname string.
   */
  protected String createPath(final String parent, final String name) {
    final int  parentPos = parent.length() - 1;
    final char parentEnd = parent.charAt(parentPos);
    final char pathStart = name.charAt(0);

    if ((parentEnd == '/' ||  parentEnd == '\\') && (pathStart == '/' || pathStart == '\\'))
      return parent.subSequence(0, parentPos - 1) + name;
    else if ((parentEnd != '/' &&  parentEnd != '\\') && (pathStart == '/' || pathStart == '\\'))
      return parent + name;
    else if ((parentEnd == '/' ||  parentEnd == '\\') && (pathStart != '/' && pathStart != '\\'))
      return parent + name;
    else
      return parent + "/" + name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDocument
  /**
   ** Creates the metadata definition for the specfied {@link File}.
   **
   ** @param  file               the {@link File} to create the XML decument
   **                            for.
   **
   ** @return                    the creeated {@link Document} containing the
   **                            parsed content of <code>file</code>.
   **
   ** @throws ServiceException   if the required {@link DocumentBuilder} cannot
   **                            be created or a parsing error occurs processing
   **                            the {@link File} <code>file</code>
   */
  protected Document createDocument(final File file)
    throws ServiceException {

    Document document = null;
    try {
      debug(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_CREATE, file.getName()));
      document = this.builder.parse(file);
      debug(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_SUCCESS, file.getName()));
    }
    catch (Exception e) {
      final String[] arguments = { file.getName(), e.getLocalizedMessage() };
      error(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_CREATE, arguments));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return document;
  }
}