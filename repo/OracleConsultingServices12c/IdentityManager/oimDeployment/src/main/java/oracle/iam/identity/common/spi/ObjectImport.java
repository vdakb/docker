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

    File        :   ObjectImport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ObjectImport.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcImportOperationsIntf;

import com.thortech.xl.vo.ddm.FilePreview;
import com.thortech.xl.vo.ddm.LockOwner;
import com.thortech.xl.vo.ddm.RootObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureException;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

import org.apache.tools.ant.BuildException;
////////////////////////////////////////////////////////////////////////////////
// class ObjectImport
// ~~~~~ ~~~~~~~~~~~~
public class ObjectImport extends ObjectRegistry {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** a single file to import. */
  private File importFile = null;

  /** the already registered files to import. */
  private final Collection<File> importSet = new ArrayList<File>();

  /** the subtitutions for an specific file. */
  private final Map<File, Map<RootObject, String>> importSubstitution = new HashMap<File, Map<RootObject, String>>();

  /** the business logic layer to operate on */
  private tcImportOperationsIntf facade;

  private FilePreview preview;
  private Collection  assembly;
  private Collection  missing;
  private Collection  subtitution;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ObjectImport</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ObjectImport(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend, INDENT, INDENTNUMBER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importFile
  /**
   ** Return the single import file this instance will handle.
   **
   ** @return                  the single import file this instance will handle.
   */
  public File importFile() {
    return this.importFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsFile
  /**
   ** Return <code>true</code> if the specified {@link File} is registered.
   **
   ** @param  file             the abstract path of a {@link File} to verify.
   **
   ** @return                  <code>true</code> if the specified {@link File}
   **                          is registered; otherwise <code>false</code>.
   */
  public boolean containsFile(final File file) {
    return this.importSet.contains(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   substitution
  /**
   ** Returns the {@link List} with substitutions for the specified
   ** {@link File}.
   ** <p>
   ** If not mapping exists for to the specified {@link File} this method will
   ** create a new empty mapping and assining it to the {@link File}.
   **
   ** @param  file             the abstract path of a {@link File} which may
   **                          contain substitution.
   **
   ** @return                  the {@link List} with substitutions for the
   **                          specified {@link File}.
   **                          Returns never <code>null</code>.
   */
  public Map<RootObject, String> substitution(final File file) {
    Map<RootObject, String> substitution = this.importSubstitution.get(file);
    if (substitution == null) {
      substitution = new HashMap<RootObject, String>();
      this.importSubstitution.put(file, substitution);
    }
    return substitution;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ServiceProvider)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case no import set is associated with this
   **                            import.
   */
  @Override
  public void validate()
    throws BuildException {

    if (this.importSet.isEmpty())
      throw new BuildException(FeatureResourceBundle.string(FeatureError.IMPORT_FILE_MANDATORY));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addImportFile
  /**
   ** Called to add a sinlge file to the set of file to import.
   **
   ** @param  importFile         the {@link File} where an import has get from.
   **
   ** @throws BuildException     if the specified {@link File} is already part
   **                            of this import operation.
   */
  public void addImportFile(final File importFile)
    throws BuildException {

    // check if we have this file already
    if (this.importSet.contains(importFile))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_ONLYONCE, importFile.getName()));

    // we cannot allow to import a complete directory
    if (importFile.isDirectory())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_ISDIRECTORY, importFile.getName()));

    // check if we are able to import the file
    if (!importFile.exists())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_NOTEXISTS, importFile.getName()));

    // we need at least read permissions on the file to add
    if (!importFile.canRead())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_NOPERMISSION, importFile.getName()));

    this.importFile = importFile;
    this.importSet.add(importFile);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addImportSet
  /**
   ** Add the specified {@link Collection} of {@link File}s to the managed
   ** {@link Collection} of file sets.
   **
   ** @param  importSet          the collection of {@link File}s to be added.
   **
   ** @throws BuildException     if one of the files provided by
   **                            <code>importSet</code> was added earlier by a
   **                            collection of files.
   */
  public void addImportSet(final Collection<File> importSet) {
    Iterator<File> i = importSet.iterator();
    while (i.hasNext())
      addImportFile(i.next());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSubstitution
  /**
   ** Add the specified value pair to the subtitution that has to be applied
   ** during the import operation.
   **
   ** @param  file               the {@link File} the substitution are related
   **                            to.
   ** @param  physicalType       the name of the category aka the physical type
   **                            of the Identity Manager.
   ** @param  origin             the subject of substitution.
   ** @param  replacement        the substitution value.
   */
  public void addSubstitution(final File file, final String physicalType, final String origin, final String replacement) {
    addSubstitution(file, new RootObject(physicalType, origin), replacement);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSubstitution
  /**
   ** Add the specified value pair to the subtitution that has to be applied
   ** during the import operation.
   **
   ** @param  file               the {@link File} the substitution are related
   **                            to.
   ** @param  substitution       the collection of substitution values.
   */
  public void addSubstitution(final File file, final Map<RootObject, String> substitution) {
    this.substitution(file).putAll(substitution);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSubstitution
  /**
   ** Add the specified value pair to the subtitution that has to be applied
   ** during the import operation.
   **
   ** @param  file               the {@link File} the substitution are related
   **                            to.
   ** @param  origin             the {@link RootObject} the substitutions should
   **                            be applied.
   ** @param  substitution       the substitution to apply during import.
   */
  public void addSubstitution(final File file, final RootObject origin, final String substitution) {
    this.substitution(file).put(origin, substitution);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Import the object definition into Identity Manager through the discovered
   ** {@link tcImportOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void execute(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(tcImportOperationsIntf.class);

    try {
      // aquire a lock on the object repository to ensure that this is the one and
      // only instance that applies changes
      acquireLock();

      // create the DocumentBuilderinstances
      createBuilder(null);

      // Whether a importFile attribute was specified, or at leats one nested
      // file set may be specified.
      Iterator<File> i = this.importSet.iterator();
      while (i.hasNext()) {
        this.importFile = i.next();
        previewDocument(readDocument());
        compileDocument();
        if (this.missing != null && this.missing.size() > 0)
          break;
        else if (this.assembly == null || this.assembly.size() == 0) {
          warning(FeatureResourceBundle.format(FeatureMessage.IMPORT_ASSEMBLY_EMPTY, this.importFile.getName()));
        }
        else {
          //TODO: complete implementation
          //        requestSubstitution();
          importDocument();
        }
      }
    }
    finally {
      if (this.facade != null) {
        try {
          this.facade.isLockAcquired();
        }
        catch (tcAPIException e) {
          throw new ServiceException(e);
        }
        this.facade.close();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   acquireLock
  /**
   ** The call back method just invoked before reconciliation takes place.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void acquireLock()
    throws ServiceException {

    try {
      // aquire the lock on the repository
      // if the respository is locked by a diffenrent process enforce that
      // lock for this instance
      if (this.facade.isLockAcquired())
        this.facade.acquireLock(false);
      else
        this.facade.acquireLock(true);

      // check if a lock exists and we are still the owner of the repository
      checkLock();
    }
    catch (ServiceException e) {
      throw e;
    }
    catch (Exception e) {
      error(FeatureResourceBundle.string(FeatureError.OBJECT_REPOSITORY_NOTLOCKED));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestSubstitution
  /**
   ** Requests a list of root objects that allow substitions, and are
   ** dependencies to the objects passed as {@link Collection}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected final void requestSubstitution()
    throws ServiceException {

    // check if a lock exists and we are still the owner of the repository
    checkLock();

    try {
      this.subtitution = this.facade.listPossibleSubstitutions(this.assembly);
      if (!this.subtitution.isEmpty()) {
        Iterator i = this.subtitution.iterator();
        while (i.hasNext()) {
          Object o = i.next();
          trace(o.toString());
        }
      }
    }
    catch (Exception e) {
      error(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_ERROR, importFile.getName()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   previewDocument
  /**
   ** Writes the managed XML to the specified file
   **
   ** @param  content            the content to upload to the server.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected final void previewDocument(final String content)
    throws ServiceException {

    // check if a lock exists and we are still the owner of the repository
    checkLock();

    final String filename = this.importFile.getName();
    debug(FeatureResourceBundle.format(FeatureMessage.ASSEMBLY_CREATE, filename));
    try {
      // import the raw XML to Identity Manager
      this.preview = this.facade.getFilePreview(filename, content);
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureMessage.ASSEMBLY_ERROR, filename));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compileDocument
  /**
   ** Writes the managed XML to the specified file
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected final void compileDocument()
    throws ServiceException {

    // check if a lock exists and we are still the owner of the repository
    checkLock();

    debug(FeatureResourceBundle.format(FeatureMessage.ASSEMBLY_CREATE, this.preview.filename));
    try {
      // convert it to the appropriate value holder
      this.assembly = this.facade.addLastPreviewedFile();
      // ask for all missing dependencies by pass null as the type filter
      this.missing = this.facade.getMissingDependencies(this.assembly, null);
      if (this.missing != null && this.missing.size() > 0) {
        error(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_UNRESOLVED, this.preview.filename));
        Iterator j = this.missing.iterator();
        while (j.hasNext()) {
          RootObject     object = (RootObject)j.next();
          final String[] parameter = { object.getPhysicalType(), object.getName() };
          warning(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_UNRESOLVED_OBJECT, parameter));
        }
      }
      else
        trace(FeatureResourceBundle.format(FeatureMessage.ASSEMBLY_SUCCESS, this.preview.filename));
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureMessage.ASSEMBLY_ERROR, this.preview.filename));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readDocument
  /**
   ** Reads the content form the current file.
   **
   ** @return                    the content form the current file as a string
   **                            represenration.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected final String readDocument()
    throws ServiceException {

    final String filename = this.importFile.getName();
    debug(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_CREATE, filename));

    int             size = 0;
    FileInputStream inp = null;
    String          result = null;
    try {
      inp = new FileInputStream(this.importFile);
      size = inp.available();
      if (size > 0) {
        int                   chunk = 0;
        byte[]                buffer = new byte[size];
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        do {
          chunk = inp.read(buffer, 0, size);
          if (chunk == -1)
            break;
          out.write(buffer, 0, chunk);
        } while (true);
        buffer = out.toByteArray();
        result = new String(out.toByteArray(), encoding());
        trace(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_SUCCESS, filename));
      }
    }
    catch (Exception e) {
      error(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_ERROR, filename));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
    finally {
      if (size > 0)
        try {
          inp.close();
        }
        catch (Exception e) {
          error(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_ERROR, filename));
          if (failonerror())
            throw new ServiceException(ServiceError.UNHANDLED, e);
          else
            error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
        }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkLock
  /**
   ** Check if this process is still the owner of the repository.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void checkLock()
    throws ServiceException {

    try {
      if (this.facade.isLockAcquired()) {
        // check the lock on the repository
        // if the respository is meanwhile locked by a diffenrent process
        // enforce escape
        LockOwner owner = this.facade.getCurrentLockInfo();
        if (owner != null) {
          error(FeatureResourceBundle.string(FeatureError.OBJECT_REPOSITORY_LOCKLOST));
          if (failonerror())
            throw new FeatureException(FeatureError.OBJECT_REPOSITORY_LOCKLOST);
        }
      }
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importDocument
  /**
   ** Imports the managed XML to Identity Manager.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected final void importDocument()
    throws ServiceException {

    // check if a lock exists and we are still the owner of the repository
    checkLock();

    final String filename = this.importFile.getName();
    warning(FeatureResourceBundle.format(FeatureMessage.IMPORT_OPERATION_START, filename));
    try {
      this.facade.performImport(this.assembly);
      info(FeatureResourceBundle.format(FeatureMessage.IMPORT_OPERATION_SUCCESS, filename));
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureMessage.IMPORT_OPERATION_ERROR, filename));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }
}