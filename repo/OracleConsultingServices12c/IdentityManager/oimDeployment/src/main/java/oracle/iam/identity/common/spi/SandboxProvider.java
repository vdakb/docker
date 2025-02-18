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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   SandboxProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SandboxProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import org.apache.tools.ant.BuildException;

import oracle.mds.core.Predicate;
import oracle.mds.core.MDSInstance;

import oracle.mds.config.PConfig;
import oracle.mds.config.MDSConfig;

import oracle.mds.change.MOChange;

import oracle.mds.naming.Namespace;
import oracle.mds.naming.PackageName;
import oracle.mds.naming.DocumentName;
import oracle.mds.naming.NamespaceRestriction;

import oracle.mds.persistence.PContext;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;
import oracle.mds.persistence.MetadataStoreConnection;

import oracle.mds.sandbox.Sandbox;
import oracle.mds.sandbox.SandboxExistsException;
import oracle.mds.sandbox.SandboxNotFoundException;

import oracle.mds.transfer.MDSTransfer;
import oracle.mds.transfer.TransferType;
import oracle.mds.transfer.TransferParams;
import oracle.mds.transfer.TransferOptions;
import oracle.mds.transfer.TransferUnitList;
import oracle.mds.transfer.ExtendedTransferType;
import oracle.mds.transfer.SandboxTransferUnitList;

import oracle.mds.transfer.store.ArchiveMetadataStore;

import oracle.mds.versioning.VersionHelper;

import oracle.mds.internal.MDSConstants;

import oracle.mds.internal.util.xml.SAXHelper;

import oracle.mds.internal.persistence.PersistenceUtils;

import oracle.mds.internal.persistence.file.ArchiveMSConnection;
import oracle.mds.internal.persistence.file.ArchiveMetadataStoreImpl;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.ServiceProvider;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeatureException;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.sandbox.type.ExportFile;
import oracle.iam.identity.sandbox.type.ImportFile;

////////////////////////////////////////////////////////////////////////////////
// abstract class SandboxProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle sandbox artifacts that are
 ** written to or loaded from a file in the local file system the task is
 ** executed on.
 ** <p>
 ** All customizations and form management are performed in a sandbox. A sandbox
 ** allows to isolate and experiment with customizations without affecting the
 ** environment of other users. Any changes made to a sandbox are visible only
 ** in the sandbox.
 ** <p>
 ** It is mandatory to create and activate a sandbox to begin using the
 ** customization and form management features. After customizations and
 ** extending forms are complete, a sandbox can be published to make the
 ** customizations available to other users.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class SandboxProvider extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String     NAMESPACE         = "http://xmlns.oracle.com/mds/sandbox";

  public static final String     NAME_PREFIX       = "IdM_";
  public static final String     CREATE_PREFIX     = "Creation_";
  public static final String     IMPORT_PREFIX     = CREATE_PREFIX + "Imported_";
  public static final String     PREMERGE_PREFIX   = "PreMerge_";
  public static final String     POSTMERGE_PREFIX  = "PostMerge_";

  public static final String     TIMESUFFIX_FORMAT = "hh:mm:ss";

  private static final Predicate USER_METADATA     = Predicate.create("user_metadata", NAMESPACE);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final List<File> flattenSet   = new ArrayList<File>();
  private boolean            forceOverride = false;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~~
  /**
   ** Handles parsing of the SandBox Metadata XML.
   */
  private static class Parser extends DefaultHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String  key;
    private String  value;
    private boolean metadata;

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: startElement (overridden)
    /**
     ** Receive notification of the beginning of an element.
     ** <p>
     ** The Parser will invoke this method at the beginning of every element in
     ** the XML document; there will be a corresponding
     ** {@link #endElement(String, String, String) endElement} event for every
     ** startElement event (even when the element is empty). All of the
     ** element's content will be reported, in order, before the corresponding
     ** endElement event.
     ** <p>
     ** This event allows up to three name components for each element:
     ** <ol>
     **   <li>the Namespace URI;</li>
     **   <li>the local name; and</li>
     **   <li>the qualified (prefixed) name.</li>
     ** </ol>
     ** Any or all of these may be provided, depending on the values of the
     ** <code>http://xml.org/sax/features/namespaces</code> and the
     ** <code>http://xml.org/sax/features/namespace-prefixes</code> properties:
     ** <ul>
     **   <li>the Namespace URI and local name are required when the namespaces
     **       property is <code>true</code> (the default), and are optional when
     **       the namespaces property is <code>false</code> (if one is
     **       specified, both must be);
     **   <li>the qualified name is required when the namespace-prefixes
     **       property is <code>true</code>, and is optional when the
     **       namespace-prefixes property is <code>false</code> (the default).
     ** </ul>
     ** Note that the attribute list provided will contain only attributes with
     ** explicit values (specified or defaulted):
     **   #IMPLIED attributes will be omitted.
     **   The attribute list will contain attributes used for Namespace
     **   declarations (xmlns* attributes) only if the
     **   <code>http://xml.org/sax/features/namespace-prefixes</code> property
     **   is <code>true</code> (it is <code>false</code> by default, and support
     **   for a <code>true</code> value is optional).
     ** <p>
     ** Like {@link #characters(char[], int, int) characters()}, attribute
     ** values may have characters that need more than one <code>char</code>
     ** value.
     **
     ** @param  uri              the Namespace URI, or the empty string if the
     **                          element has no Namespace URI or if Namespace
     **                          processing is not being performed.
     ** @param  localName        the local name (without prefix), or the empty
     **                          string if Namespace processing is not being
     **                          performed
     ** @param  qualifiedName    the qualified name (with prefix), or the empty
     **                          string if qualified names are not available.
     ** @param  attributes       the attributes attached to the element.
     **                          If there are no attributes, it shall be an
     **                          empty {@link Attributes} object. The value of
     **                          this object after startElement returns is
     **                          undefined.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     **
     ** @see    #endElement(String, String, String)
     ** @see    org.xml.sax.Attributes
     ** @see    org.xml.sax.helpers.AttributesImpl
     */
    @Override
    public void startElement(final String uri, final String localName, final String qualifiedName, final Attributes attributes)
      throws SAXException {

      Predicate element = null;
      if ((uri != null) && (NAMESPACE.equals(uri)))
        element = Predicate.create(localName, uri);

      if (USER_METADATA.equals(element))
        this.metadata = true;

      this.key = (localName == null ? qualifiedName : localName);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: characters (overridden)
    /**
     ** Receive notification of character data inside an element.
     ** <p>
     ** By default, it will be checked if the parser is able to receive the
     ** name of sandbox. Application writers may override this method to take
     ** more specific actions for each chunk of character data (such as adding
     ** the data to a node or buffer, or printing it to a file).
     **
     ** @param  ch               the characters.
     ** @param  start            the start position in the character array.
     ** @param  length           the number of characters to use from the
     **                          character array.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     **
     ** @see    org.xml.sax.ContentHandler#characters
     */
    @Override
    public void characters(final char[] ch, final int start, final int length)
      throws SAXException {

      if (this.metadata && this.key.endsWith("name")) {
        this.value = new String(ch, start + NAME_PREFIX.length(), length - NAME_PREFIX.length());
        // if we are here we are no longer interested on anything else hence
        // break the further processing
        this.metadata = false;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SandboxProvider</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public SandboxProvider(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>forceOverride</code>.
   **
   ** @param  forceOverride      <code>true</code> to override the existing file
   **                            without to aks for user confirmation.
   */
  public void forceOverride(final boolean forceOverride) {
    this.forceOverride = forceOverride;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Returns the how to handle existing files
   **
   ** @return                    <code>true</code> if the existing file will be
   **                            overridden without any further confirmation.
   */
  public final boolean forceOverride() {
    return this.forceOverride;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addFile
  /**
   ** Called to add a sinlge file to the set of file to handle.
   **
   ** @param  file               the {@link File} handle.
   **
   ** @throws BuildException     if the specified {@link File} is already part
   **                            of this import operation.
   */
  public void addFile(final File file) {
    // we cannot allow to upload a complete directory
    if (file.isDirectory())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_FILE_ISDIRECTORY, file.getName()));

    if (!file.exists())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_FILE_NOTEXISTS, file.getName()));

    // we need at least read permissions on the file to add
    if (!file.canRead())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_FILE_NOPERMISSION, file.getName()));

    // check if we have this file already
    if (this.flattenSet.contains(file))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_ONLYONCE, file.getAbsolutePath()));

    this.flattenSet.add(file);
  }

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
  // Method:   upload
  /**
   ** Imports a sandbox archive from the local file system into Identity
   ** Manager.
   **
   ** @param  target             the {@link MDSInstance} used as the destination
   **                            of the operation.
   ** @param  namespace          the {@link Namespace} the sandbox belongs to.
   ** @param  options            the {@link TransferOptions} used to perform the
   **                            operation.
   ** @param  importFile         the {@link ImportFile} to load as an archive
   **                            from the local file system.
   **
   ** @throws BuildException     if the sandbox to export does not exists.
   ** @throws ServiceException   in case an error does occur.
   */
  protected void upload(final MDSInstance target, final Namespace namespace, final TransferOptions options, final ImportFile importFile)
    throws ServiceException {

    final ArrayList<String> pattern = new ArrayList<String>();
    pattern.add("/**");

    final String name = importFile.archive().getName();
    try {
      final ArchiveMetadataStore archive = ArchiveMetadataStore.createStore(importFile.archive().getAbsolutePath());
      // interface changed for some reason
      // previously we used isUsedByTransfer now it has to be
      // setIsUsedByTransfer
      ((ArchiveMetadataStoreImpl)archive).setIsUsedByTransfer(true);

      final MDSConfig   config = new MDSConfig(null, new PConfig(archive), null);
      final MDSInstance source = MDSInstance.getOrCreateInstance(name, config);
      final String      sandbox = extractSandboxName(source, archive);

      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_IMPORT, sandbox));
      final TransferUnitList unit = TransferUnitList.create(source, pattern, options, true, false);
      final TransferParams   params = TransferParams.createForSandboxImport(ExtendedTransferType.create(TransferType.IMPORT, null), namespace, FeaturePlatformTask.USERNAME, true, importLabel(), "Sandbox created from Import");
      MDSTransfer.getInstance(source).transfer(target, unit, params, null, null, true);
      MDSInstance.releaseInstance(name);
      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_IMPORTED, sandbox));

      // publish the sandbox if requested
      if (importFile.publish())
        publish(target, namespace, sandbox, importFile.commit(), importFile.force());
    }
    catch (SandboxExistsException e) {
      if (failonerror())
        throw new FeatureException(FeatureError.SANDBOX_EXISTS);
      else
        error(FeatureResourceBundle.string(FeatureError.SANDBOX_EXISTS));
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   download
  /**
   ** Writes the sandbox archive to the local file system
   **
   ** @param  instance           the {@link MDSInstance} used to perform the
   **                            operation.
   ** @param  transfer           the {@link MDSInstance} used as the temporary
   **                            intermediate.
   ** @param  namespace          the {@link Namespace} the sandbox belongs to.
   ** @param  options            the {@link TransferOptions} used to perform the
   **                            operation.
   ** @param  exportFile         the {@link ExportFile} to write as an archive
   **                            to the local file system.
   **
   ** @throws BuildException     if the sandbox to export does not exists.
   ** @throws ServiceException   in case an error does occur.
   */
  protected void download(final MDSInstance instance, final MDSTransfer transfer, final Namespace namespace, final TransferOptions options, final ExportFile exportFile)
    throws ServiceException {

    final String fqn = fullName(exportFile.sandbox());
    try {
      final Sandbox sandbox = Sandbox.getSandbox(instance, fqn, namespace);
      // validate if the sandbox conatins any change to prevent overriding an
      // exiting file with an empty sandbox
      Iterator<MOChange> changes = sandbox.getChangesToApply();
      if (changes == null) {
        error(FeatureResourceBundle.format(FeatureMessage.SANDBOX_EXPORT_EMPTY, exportFile.sandbox()));
        return;
      }
      if (!changes.hasNext()) {
        error(FeatureResourceBundle.format(FeatureMessage.SANDBOX_EXPORT_EMPTY, exportFile.sandbox()));
        return;
      }
    }
    catch (SandboxNotFoundException e) {
      final String message = FeatureResourceBundle.format(FeatureError.SANDBOX_NOTEXISTS, exportFile.sandbox());
      if (this.failonerror())
        throw new BuildException(message);
      else {
        error(message);
        return;
      }
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }

    final File file = exportFile.archive();
    int        response = 0;
    if (!this.forceOverride() && file.exists()) {
      response = JOptionPane.showConfirmDialog
        (null
      , ServiceResourceBundle.format(ServiceMessage.DOCUMENT_CONFIRMATION_MESSAGE, file.getName(), file.getParent())
      , ServiceResourceBundle.format(ServiceMessage.DOCUMENT_CONFIRMATION_TITLE,   exportFile.sandbox())
      , JOptionPane.YES_NO_OPTION
      , JOptionPane.QUESTION_MESSAGE
      );
      if (response == 0)
        file.delete();
    }

    if (response == 0)
      try {
        warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_EXPORT, exportFile.sandbox()));
        final ArchiveMetadataStore archive = ArchiveMetadataStore.createStore(file.getAbsolutePath());
        final MDSConfig            metadata = new MDSConfig(null, new PConfig(archive), null);
        final MDSInstance          target = MDSInstance.getOrCreateInstance(fqn, metadata);

        final TransferUnitList     transferUnit = SandboxTransferUnitList.create(instance, options, true, fqn, namespace);
        final ExtendedTransferType transferType = ExtendedTransferType.create(TransferType.EXPORT, null);
        final TransferParams       transferParams = TransferParams.createForSandboxExport(transferType, fqn, namespace);

        transfer.transfer(target, transferUnit, transferParams, null, null, true);
        MDSInstance.releaseInstance(fqn);
        warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_EXPORTED, exportFile.sandbox()));
      }
      catch (SandboxNotFoundException e) {
        final String message = FeatureResourceBundle.format(FeatureError.SANDBOX_NOTEXISTS, exportFile.sandbox());
        if (this.failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
      catch (Exception e) {
        throw new ServiceException(e);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete a sandbox in Identity Manager.
   ** <p>
   ** If no sandbox is active, then changes to metadata objects are not allowed,
   ** and therefore, no UI customization is allowed.
   **
   ** @param  instance           the {@link MDSInstance} used to perform the
   **                            operation.
   ** @param  namespace          the {@link Namespace} the sandbox belongs to.
   ** @param  name               the fullqualified name of the
   **                            <code>Sandbox</code> to delete.
   **
   ** @throws BuildException     if the sandbox to delete does not exists.
   ** @throws ServiceException   in case an error does occur.
   */
  protected void delete(final MDSInstance instance, final Namespace namespace, final String name)
    throws ServiceException {

    warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_DELETE, name));
    try {
      final Sandbox sandbox = Sandbox.getSandbox(instance, fullName(name), namespace);
      VersionHelper.get(instance).deleteLabel(sandbox.getMainlineLabel().getName(), namespace);
      Sandbox.destroySandbox(instance, name, namespace);
      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_DELETED, name));
    }
    catch (SandboxNotFoundException e) {
      final String message = FeatureResourceBundle.format(FeatureError.SANDBOX_NOTEXISTS, name);
      if (this.failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publish
  /**
   ** Publishes the sandbox to the main line system.
   **
   ** @param  instance           the {@link MDSInstance} used to perform the
   **                            operation.
   ** @param  namespace          the {@link Namespace} the sandbox belongs to.
   ** @param  name               the name of the sandbox to publish.
   ** @param  commit             <code>true</code> if publishing of the
   **                            sandbox has to be committed.
   ** @param  force              <code>true</code> if publishing has to be
   **                            enforced.
   **
   ** @throws BuildException     if the sandbox to export does not exists.
   ** @throws ServiceException   in case an error does occur.
   */
  protected void publish(final MDSInstance instance, final Namespace namespace, final String name, final boolean commit, final boolean force)
    throws ServiceException {

    warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_PUBLISH, name));
    try {
      final Sandbox sandbox = Sandbox.getSandbox(instance, fullName(name), namespace);
      // validate if the sandbox conatins any change to prevent overriding an
      // exiting file with an empty sandbox
      if (sandbox.getChangesToApply() == null) {
        error(FeatureResourceBundle.format(FeatureMessage.SANDBOX_PUBLISH_EMPTY, name));
        return;
      }

      final String preMergeLabel = preMergeLabel(fullName(name));
      final String postMergeLabel = postMergeLabel(fullName(name));
      sandbox.applyToMainlineWithUndoSupport(preMergeLabel, preMergeLabel, postMergeLabel, postMergeLabel, FeaturePlatformTask.USERNAME, force);
      if (commit) {
        // commit the changes published by the sandbox
        sandbox.finalizeApplyToMainline(FeaturePlatformTask.USERNAME);
        VersionHelper.get(instance).deleteLabel(sandbox.getMainlineLabel().getName(), namespace);
      }
      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_PUBLISHED, name));
    }
    catch (SandboxNotFoundException e) {
      final String message = FeatureResourceBundle.format(FeatureError.SANDBOX_NOTEXISTS, name);
      if (this.failonerror())
        throw new BuildException(message);
      else {
        error(message);
        return;
      }
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Commits a published sandbox to the main line system.
   **
   ** @param  instance           the {@link MDSInstance} used to perform the
   **                            operation.
   ** @param  namespace          the {@link Namespace} the sandbox belongs to.
   ** @param  name               the name of the sandbox to publish.
   **
   ** @throws BuildException     if the sandbox to export does not exists.
   ** @throws ServiceException   in case an error does occur.
   */
  protected void commit(final MDSInstance instance, final Namespace namespace, final String name)
    throws ServiceException {

    warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_COMMIT, name));
    try {
      final Sandbox sandbox = Sandbox.getSandbox(instance, fullName(name), namespace);
      // validate if the sandbox conatins any change to prevent overriding an
      // exiting file with an empty sandbox
      if (sandbox.getChangesToApply() == null) {
        error(FeatureResourceBundle.format(FeatureMessage.SANDBOX_PUBLISH_EMPTY, name));
        return;
      }

      // commit the changes published by the sandbox
      sandbox.finalizeApplyToMainline(FeaturePlatformTask.USERNAME);
      VersionHelper.get(instance).deleteLabel(sandbox.getMainlineLabel().getName(), namespace);
      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_COMMITED, name));
    }
    catch (SandboxNotFoundException e) {
      final String message = FeatureResourceBundle.format(FeatureError.SANDBOX_NOTEXISTS, name);
      if (this.failonerror())
        throw new BuildException(message);
      else {
        error(message);
        return;
      }
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rollback
  /**
   ** Rollbacks the changes applied to a main line done by publishing a sandbox.
   **
   ** @param  instance           the {@link MDSInstance} used to perform the
   **                            operation.
   ** @param  namespace          the {@link Namespace} the sandbox belongs to.
   ** @param  name               the name of the sandbox to publish.
   ** @param  force              <code>true</code> if rollback has to be
   **                            enforced.
   **
   ** @throws BuildException     if the sandbox to export does not exists.
   ** @throws ServiceException   in case an error does occur.
   */
  protected void rollback(final MDSInstance instance, final Namespace namespace, final String name, final boolean force)
    throws ServiceException {

    warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_ROLLBACK, name));
    try {
      final Sandbox sandbox = Sandbox.getSandbox(instance, fullName(name), namespace);
      // validate if the sandbox conatins any change to prevent overriding an
      // exiting file with an empty sandbox
      if (sandbox.getChangesToApply() == null) {
        error(FeatureResourceBundle.format(FeatureMessage.SANDBOX_PUBLISH_EMPTY, name));
        return;
      }

      // rollback the changes published by the sandbox
      final String preMergeLabel = preMergeLabel(name);
      final String postMergeLabel = postMergeLabel(name);
      sandbox.undoApplyToMainline(preMergeLabel, preMergeLabel, postMergeLabel, postMergeLabel, FeaturePlatformTask.USERNAME, force);
      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_ROLLEDBACK, name));
    }
    catch (SandboxNotFoundException e) {
      final String message = FeatureResourceBundle.format(FeatureError.SANDBOX_NOTEXISTS, name);
      if (this.failonerror())
        throw new BuildException(message);
      else {
        error(message);
        return;
      }
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   creationLabel
  protected static String creationLabel(final String name) {
    return CREATE_PREFIX + name + "_" + timeSuffix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preMergeLabel
  protected static String preMergeLabel(final String name) {
    return PREMERGE_PREFIX + name + "_" + timeSuffix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postMergeLabel
  protected static String postMergeLabel(final String name) {
    return POSTMERGE_PREFIX + name + "_" + timeSuffix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fullName
  protected static String fullName(final String name) {
    return NAME_PREFIX + name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importLabel
  protected static String importLabel() {
    return IMPORT_PREFIX + timeSuffix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeSuffix
  protected static String timeSuffix() {
    final DateFormat format = new SimpleDateFormat(TIMESUFFIX_FORMAT);
    return format.format(new Date());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultCustomizationNamspace
  protected Namespace defaultCustomizationNamspace(final MDSInstance instance)
    throws ServiceException {

    final PManager  persistence = instance.getPersistenceManager();
    List<Namespace> result = null;
    try {
      result = persistence.getNamespaceMappingHelper().getAllMatchingStoreNamespaces(PackageName.createPackageName("/"), NamespaceRestriction.CUSTOMIZATIONS);
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }

    return (result == null || result.get(0) == null) ? null : result.get(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extractSandboxName
  /**
   ** Extracts the name of the sandbox from the specified {@link MDSInstance}
   ** <code>source</code>.
   **
   ** @param  source
   ** @param  store
   **
   ** @return                    the native name of the sandbox provided by
   **                            the {@link ArchiveMetadataStore}
   **                            <code>strore</code>.
   **
   ** @throws ServiceException
   */
  private String extractSandboxName(final MDSInstance source, final ArchiveMetadataStore store)
    throws ServiceException {

    try {
      // Attention:
      // This is a hack to make a file based archive store parsable
      // Archive stores are normally used in transfer operations only were no
      // parsing operation are applied on the client side, but we have to know
      // the name of the sandbox configured in the metadata which needs not to
      // be the same as the nam of the archive file
      // interface changed for some reason
      // previously we we used isUsedByTransfer now it has to be
      // setIsUsedByTransfer
      ((ArchiveMetadataStoreImpl)store).setIsUsedByTransfer(true);

      final PContext                context = source.getPersistenceManager().createPContext();
      final MetadataStoreConnection connection = new ArchiveMSConnection(store);
      final PDocument               document = connection.getDocument(context, DocumentName.create(MDSConstants.MDS_SANDBOX_PATH, MDSConstants.MDS_ACTIVE_SANDBOX_METADATA_NAME));
      // Fix of Defect DE-000061
      // -----------------------
      // Importing a Sandbox that will be published at the same time throws
      // java.lang.IllegalAccessError if it's running on MDS runtime
      // implementation installed by Fusion Middleware 11.1.1.6
      // Until Fusion Middleware 11.1.1.5 the zero-argument constructor of class
      // oracle.mds.internal.sandbox.metadata.SandboxMetadataParser was public.
      // Since Fusion Middleware 11.1.1.6 the constructor is private which
      // throws the exception the defect belongs to.
      // Solution: extract the sandbox metadata manually.
      final InputSource content = document.read();
      final Parser      handler = new Parser();
      try {
        SAXHelper.parse(content, handler);
      }
      catch (SAXException e) {
        throw new ServiceException(ServiceError.ABORT, e);
      }
      catch (IOException e) {
        throw new ServiceException(e);
      }
      PersistenceUtils.closeInputSource(content);
      return handler.value;
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }
}