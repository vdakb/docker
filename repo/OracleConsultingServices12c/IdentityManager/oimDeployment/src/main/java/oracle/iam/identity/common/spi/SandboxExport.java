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

    File        :   SandboxExport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SandboxExport.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.mds.core.MDSInstance;

import oracle.mds.naming.Namespace;

import oracle.mds.transfer.CustOption;
import oracle.mds.transfer.MDSTransfer;
import oracle.mds.transfer.TransferOptions;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.sandbox.type.ExportFile;

////////////////////////////////////////////////////////////////////////////////
// class SandboxExport
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle sandbox artifacts that are
 ** exported from the Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SandboxExport extends SandboxProvider {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ExportFile             single = null;
  private final List<ExportFile> multiple = new ArrayList<ExportFile>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SandboxExport</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public SandboxExport(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   archive
  /**
   ** Sets the name of a file for import.
   **
   ** @param  archive            the name of a file for import.
   */
  public final void archive(final File archive) {
    if (this.single == null)
      this.single = new ExportFile();

    this.single.setArchive(archive);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sandbox
  /**
   ** Sets the name of a sandbox to export.
   **
   ** @param  name               the name of a sandbox to export.
   */
  public final void sandbox(final String name) {
    if (this.single == null)
      this.single = new ExportFile();

    this.single.setSandbox(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (SandboxProvider)
  /**
   ** Executes the metadata operation in a Oracle Metadata Store represented by
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

    final Namespace       namespace = defaultCustomizationNamspace(instance);
    final MDSTransfer     transfer = MDSTransfer.getInstance(instance);
    final TransferOptions options = new TransferOptions(new CustOption[] { new CustOption(null, true, false) });
    if (this.single != null)
      download(instance, transfer, namespace, options, this.single);

    for (ExportFile file : this.multiple)
      download(instance, transfer, namespace, options, file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addExportSet
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link ExportFile}.
   **
   ** @param  exportFile         the file a sandbox will be exported to.
   **
   ** @throws BuildException     if the {@link ExportFile} is contained in the
   **                            collection is of this export operation.
   */
  public void addExportFile(final ExportFile exportFile)
    throws BuildException {

    // check if a export set can be added to this task or if it has to stick on
    // a single file
    if (this.single != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MIXEDUP, "exportFile"));

    // check if we have permissions to write the file if it's already exists
    final File file = exportFile.archive();
    if (file.exists() && !file.canWrite())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.EXPORT_FILE_NOPERMISSION, file.getName()));

    if (this.multiple.contains(exportFile))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_NAME_ONLYONCE, exportFile.sandbox()));

    // check if we have this file already
    addFile(file);
    this.multiple.add(exportFile);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {
    if ((this.single == null) && (this.multiple.size() == 0))
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_MANDATORY));

    try {
      if (this.single != null)
        this.single.validate();

      for (ExportFile cursor : this.multiple)
        cursor.validate();
    }
    catch (Exception e) {
      throw new BuildException(e.getLocalizedMessage());
    }
  }
}