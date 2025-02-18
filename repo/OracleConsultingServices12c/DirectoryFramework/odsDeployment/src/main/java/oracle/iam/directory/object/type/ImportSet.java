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

    File        :   ImportSet.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ImportSet.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.object.type;

import java.io.File;

import oracle.iam.directory.common.spi.instance.FileInstance;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.FileList;

import oracle.iam.directory.common.spi.instance.ImportInstance;

////////////////////////////////////////////////////////////////////////////////
// class ImportSet
// ~~~~~ ~~~~~~~~~
/**
 ** <code>ImportSet</code> is a special {@link FileList} that will be imported
 ** to a Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ImportSet extends FileList {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final private ImportInstance delegate = new ImportInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ImportSet</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ImportSet(){
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link ImportInstance} delegate of Directory Service object to
   ** handle.
   **
   ** @return                    the {@link ImportInstance} delegate.
   */
  public final ImportInstance instance() {
    if (isReference())
      return ((ImportSet)getCheckedRef()).instance();

    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredImportFile
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link ImportFile}.
   **
   ** @param  importFile         the subject of import.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>ImportSet</code>
   */
  public void addConfiguredImportFile(final ImportFile importFile)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    // get the directory related to the file list
    final File directory = getDir(getProject());
    // add it to the operational bean thats leads to the file and validation
    // of the file that may throw an exception
    this.delegate.add(new FileInstance(new File(directory, importFile.getName()), importFile.instance().format(), importFile.instance().version()));
  }
}