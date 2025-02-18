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

    File        :   Import.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Import.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.object.task;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.common.FeatureException;

import oracle.iam.directory.common.spi.handler.ImportHandler;

import oracle.iam.directory.common.task.FeatureDirectoryTask;

import oracle.iam.directory.object.type.ImportSet;

////////////////////////////////////////////////////////////////////////////////
// class Import
// ~~~~~ ~~~~~~
/**
 ** Imports  Directory Service objects from file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Import extends FeatureDirectoryTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the service provider executing the task operation */
  private final ImportHandler     delegate;

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
  public Import(){
    // ensure inheritance
    super();

    // initialize the service provider instance
    this.delegate = new ImportHandler(this);
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

    this.delegate.add(importSet.instance());
  }
}