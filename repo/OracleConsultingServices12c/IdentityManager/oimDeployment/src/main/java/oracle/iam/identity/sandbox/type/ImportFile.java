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

    File        :   ImportFile.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ImportFile.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.type;

import java.io.File;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ImportFile
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>ImportFile</code> is a special {@link DataType} that will be import a
 ** <code>Sandbox</code> to Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ImportFile extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private File    archive;
  private boolean publish = false;
  private boolean commit = true;
  private boolean force = true;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ImportFile</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ImportFile() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another <code>Role</code>
   ** instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (this.archive != null)
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setArchive
  /**
   ** Sets the abstract archive {@link File}.
   **
   ** @param  archive            the abstract archive {@link File}.
   */
  public final void setArchive(final File archive) {
    // prevent bogus input
    checkAttributesAllowed();

    if (archive.isDirectory())
      throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_FILE_ISDIRECTORY, archive.getAbsolutePath()));

    this.archive = archive;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   archive
  /**
   ** Returns the abstract archive {@link File}.
   **
   * @return                     the abstract archive {@link File}.
   */
  public final File archive() {
    return this.archive;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPublish
  /**
   ** Sets the mode of publishing.
   ** <p>
   ** <code>true</code> advice that the sandbox the file belongs to will be
   ** published after importing, <code>false</code> requests that the sandbox
   ** created by the import remains unpublished.
   **
   ** @param  mode               <code>true</code> advice that the sandbox the
   *                             file belongs to will be published after
   *                             importing, <code>false</code> requests that the
   *                             sandbox created by the import remains
   *                             unpublished.
   */
  public final void setPublish(final boolean mode) {
    // prevent bogus input
    checkAttributesAllowed();
    this.publish = mode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publish
  /**
   ** Returns the mode of publishing.
   **
   * @return                     <code>true</code> advice that the sandbox the
   *                             file belongs to will be published after
   *                             importing, <code>false</code> requests that the
   *                             sandbox created by the import remains
   *                             unpublished.
   */
  public final boolean publish() {
    return this.publish;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setForce
  /**
   ** Set <code>true</code> if a sandox needs to enforce publishing.
   **
   ** @param  mode               <code>true</code> if a sandox needs to enforce
   **                            publishing; otherwise <code>false</code>.
   */
  public final void setForce(final boolean mode) {
    // prevent bogus input
    checkAttributesAllowed();
    this.force = mode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   force
  /**
   ** Returns <code>true</code> if a sandox needs to enforce publishing.
   **
   ** @return                    <code>true</code> if a sandox needs to enforce
   **                            publishing; otherwise <code>false</code>.
   */
  public final boolean force() {
    return this.force;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCommit
  /**
   ** Sets the commit mode during a publishing a sandbox.
   ** <p>
   ** <code>true</code> advice that the sandbox changes will be final.
   **
   ** @param  mode               <code>true</code> advice that the changes has
   **                            to be committed.
   */
  public final void setCommit(final boolean mode) {
    // prevent bogus input
    checkAttributesAllowed();
    this.commit = mode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Returns the commit mode during a publishing a sandbox.
   **
   ** @return                    <code>true</code> advice that the changes has
   **                            to be committed.
   */
  public final boolean commit() {
    return this.commit;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to perform.
   **
   ** @throws BuildException   in case an error does occur.
   */
  public void validate()
    throws BuildException {

    if (this.archive == null)
      throw new BuildException(FeatureResourceBundle.string(FeatureError.SANDBOX_FILE_MANDATORY));
  }
}