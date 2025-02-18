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
    Subsystem   :   Sandbox Service Utilities 11g

    File        :   Import.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Import.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.task;

import java.io.File;

import oracle.iam.identity.common.FeatureSandboxTask;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.common.spi.SandboxImport;
import oracle.iam.identity.common.spi.SandboxProvider;

import oracle.iam.identity.sandbox.type.ImportFile;

////////////////////////////////////////////////////////////////////////////////
// class Import
// ~~~~~ ~~~~~~
/**
 ** Imports a sandbox from Oracle Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Import extends FeatureSandboxTask {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Export</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Import() {
    // ensure inheritance
    super(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setArchive
  /**
   ** Sets the location of the file to import.
   **
   ** @param  archive            the abstract {@link File} path of the archive
   **                            to import.
   */
  public void setArchive(final File archive) {
    ((SandboxImport)this.provider).archive(archive);
  }

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
    this.provider.forceOverride(forceOverride);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPublish
  /**
   ** Set it to <code>true</code> the imported sandbox has to be published.
   **
   ** @param  publish            <code>true</code> if the imported sandbox has
   **                            to be published.
   */
  public final void setPublish(final boolean publish) {
    ((SandboxImport)this.provider).publish(publish);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProvider (FeatureSandboxTask)
  /**
   ** Factory method to create the appropriate {@link SandboxProvider}.
   **
   ** @return                    the appropriate {@link SandboxProvider}.
   */
  @Override
  protected final SandboxProvider createProvider() {
    return new SandboxImport(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredImportFile
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link ImportFile}.
   **
   ** @param  importFile         the file a sandbox will be imported from.
   **
   ** @throws BuildException     if the {@link ImportFile} already contained in
   **                            the collection of this import operation.
   */
  public void addConfiguredImportFile(final ImportFile importFile)
    throws BuildException {

    ((SandboxImport)this.provider).addImportFile(importFile);
  }
}