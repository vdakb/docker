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

    File        :   ImportInstance.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ImportInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.spi.instance;

import java.io.File;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ImportInstance
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>ImportInstance</code> represents a data instance wrapper a
 ** <code>ImportHandler</code> use to control a set of files to import.
 ** <p>
 ** Subclasses of this classes providing the data model an implementation of
 ** <code>ImportHandler</code> needs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ImportInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** a single file to import. */
  private FileInstance             importFile = null;

  /** the already registered files to import. */
  private Collection<FileInstance> importSet  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ImportInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
   public ImportInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importFile
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>importFile</code> on the service delegate.
   **
   ** @param  instance           the {@link FileInstance} where the import has
   **                            get from.
   */
  public void importFile(final FileInstance instance) {
    this.importFile = instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importFile
  /**
   ** Return the single import file this instance will handle.
   **
   ** @return                  the single import file this instance will handle.
   */
  public FileInstance importFile() {
    return this.importFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsFile
  /**
   ** Return <code>true</code> if the specified {@link File} is registered.
   **
   ** @param  file             the {@link FileInstance} to verify.
   **
   ** @return                  <code>true</code> if the specified {@link File}
   **                          is registered; otherwise <code>false</code>.
   */
  public boolean containsFile(final FileInstance file) {
    return this.importSet.contains(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importFile
  /**
   ** Return the set of import files this instance will handle.
   **
   ** @return                  the set of import files this instance will
   **                          handle.
   */
  public Collection<FileInstance> importSet() {
    return this.importSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified file context to the parameters that has to be applied.
   **
   ** @param  instance           the {@link FileInstance} to add.
   */
  public void add(final FileInstance instance) {
    if (this.importSet == null)
      this.importSet = new ArrayList<FileInstance>();

    if (this.importSet.contains(instance))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.IMPORT_FILE_ONLYONCE, instance.file().getName()));

    // add the instance to the object to handle
    this.importSet.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  public void validate()
    throws BuildException {

    if (this.importFile == null &&  this.importSet.isEmpty())
      throw new BuildException(FeatureResourceBundle.string(FeatureError.IMPORT_FILE_MANDATORY));
  }
}