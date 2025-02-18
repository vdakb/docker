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

    File        :   Import.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Import.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import java.io.File;

import org.apache.tools.ant.BuildException;

import com.thortech.xl.vo.ddm.RootObject;

import oracle.hst.deployment.ServiceException;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.common.spi.ObjectImport;

import oracle.iam.identity.deployment.type.ImportSet;
import oracle.iam.identity.deployment.type.Substitution;

////////////////////////////////////////////////////////////////////////////////
// class Import
// ~~~~~ ~~~~~~
/**
 ** Imports Identity Manager objects from file.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Import extends FeaturePlatformTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the service provider executing the task operation */
  private final ObjectImport delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Import</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Import() {
    // ensure inheritance
    super();

    // initialize the service provider instance
    this.delegate = new ObjectImport(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setImportFile
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>importFile</code> on the service delegate.
   **
   ** @param  file               the {@link File} where the import has get from.
   **
   ** @throws BuildException     if the specified {@link File} is already part
   **                            of this import operation.
   */
  public void setImportFile(final File file)
    throws BuildException {

    this.delegate.addImportFile(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredSubstitution
  /**
   ** Call by the ANT deployment to inject the argument for a substitution on
   ** <code>importFile</code> on the service delegate.
   **
   ** @param  substitution       the {@link Substitution} that has to be applied
   **                            on the content of the assined file.
   **
   ** @throws BuildException   if the specified {@link Substitution} is
   **                            already part of this import operation.
   */
  public void addConfiguredSubstitution(final Substitution substitution)
    throws BuildException {

    if (this.delegate.importFile() == null)
      throw new BuildException(FeatureResourceBundle.string(FeatureError.IMPORT_FILE_CONSTRAINT));

    // transform the substitutions to a mapping
    final RootObject origin = new RootObject(substitution.getValue(), substitution.name());
    this.delegate.addSubstitution(this.delegate.importFile(), origin, substitution.replacement());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredImportSet
  /**
   ** Call by the ANT deployment to inject the nested "fileset" element.
   **
   ** @param  importSet          the names of the file where the export hast to
   **                            be written to.
   **
   ** @throws BuildException     if the on of the resulting {@link File}s
   **                            evaluated from the {@link ImportSet} is already
   **                            part of this import operation.
   */
  public void addConfiguredImportSet(final ImportSet importSet)
    throws BuildException {

    final String[] importFile = importSet.getFiles(getProject());
    if (importFile != null) {
      // get the directory related to the file list
      final File directory = importSet.getDir(getProject());
      // iterate over the file name and build a path
      for (int i = 0; i < importFile.length; i++)
        // add it to the operational bean thats leads to the filan validation
        // of the file that may throw an exception
        this.delegate.addImportFile(new File(directory, importFile[i]));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractTask)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  public void onExecution()
    throws ServiceException {

    this.delegate.execute(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  protected void validate()
    throws BuildException {

    this.delegate.validate();

    // ensure inheritance
    super.validate();
  }
}