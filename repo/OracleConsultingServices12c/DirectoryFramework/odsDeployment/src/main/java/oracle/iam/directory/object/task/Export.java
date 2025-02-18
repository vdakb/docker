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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities

    File        :   Export.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Export.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.object.task;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.common.FeatureException;

import oracle.iam.directory.common.task.FeatureDirectoryTask;

import oracle.iam.directory.common.spi.handler.ExportHandler;

import oracle.iam.directory.object.type.ExportSet;

////////////////////////////////////////////////////////////////////////////////
// class Export
// ~~~~~ ~~~~~~
/**
 ** Exports Directory Service objects to file.
 ** <p>
 ** <b>Note</b>: The names of the attributes are case-insensitive.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Export extends FeatureDirectoryTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the service provider executing the task operation */
  private final ExportHandler     delegate;

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
  public Export(){
    // ensure inheritance
    super();

    // initialize the service provider instance
    this.delegate = new ExportHandler(this);
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
    this.delegate.forceOverride(forceOverride);
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
   ** <p>
   ** Invokes an operation on the connected Directory Information Tree (DIT) to
   ** create entries.
   **
   ** @throws FeatureException   a operational problem occurred when talking
   **                            to the LDAP server.
   */
  @Override
  public void onExecution()
    throws FeatureException {

    this.delegate.execute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredExportSet
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link ExportSet}.
   **
   ** @param  exportSet         the set of categories to export.
   **
   ** @throws BuildException     if the file the {@link ExportSet} or a
   **                            <code>Category</code> contained in the set is
   **                            already part of this export operation.
   */
  public void addConfiguredExportSet(final ExportSet exportSet)
    throws BuildException {

    this.delegate.add(exportSet.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  protected void validate()
    throws BuildException {

    this.delegate.validate();

    // ensure inheritance
    super.validate();
  }
}