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

    File        :   Export.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Export.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.task;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.common.FeatureSandboxTask;

import oracle.iam.identity.common.spi.SandboxExport;
import oracle.iam.identity.common.spi.SandboxProvider;

import oracle.iam.identity.sandbox.type.ExportFile;

////////////////////////////////////////////////////////////////////////////////
// class Export
// ~~~~~ ~~~~~~
/**
 ** Exports a sandbox from Oracle Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Export extends FeatureSandboxTask {

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
  public Export() {
    // ensure inheritance
    super(true);
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
    this.provider.forceOverride(forceOverride);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setArchive
  /**
   ** Sets the location of the file to import.
   **
   ** @param  archive            the abstract {@link File} path of the archive
   **                            to import.
   */
  public void setArchive(final File archive) {
    ((SandboxExport)this.provider).archive(archive);
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
    return new SandboxExport(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredExportSet
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link ExportFile}.
   **
   ** @param  exportFile         the file a sandbox will be exported to.
   **
   ** @throws BuildException     if the {@link ExportFile} is contained in the
   **                            collection is of this export operation.
   */
  public void addConfiguredExportFile(final ExportFile exportFile)
    throws BuildException {

    ((SandboxExport)this.provider).addExportFile(exportFile);
  }
}