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

    File        :   NameSpace.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NameSpace.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.type;

import java.util.List;
import java.util.ArrayList;

import java.io.File;

////////////////////////////////////////////////////////////////////////////////
// class NameSpace
// ~~~~~ ~~~~~~~~~
/**
 ** The environment wrapper of a specific path in a Oracle Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NameSpace extends Path {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the already registered files to import. */
  private final List<ImportSet> importSet = new ArrayList<ImportSet>();

  /** the already registered files to export. */
  private final List<ExportSet> exportSet = new ArrayList<ExportSet>();

  /** the already registered files to maintain. */
  private final List<FolderSet> folderSet = new ArrayList<FolderSet>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NameSpace</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public NameSpace() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importSet
  /**
   ** Returns the {@link List} of {@link File}s to import to this
   ** <code>NameSpace</code>.
   **
   ** @return                  the {@link List} of {@link File}s to import to
   **                          this <code>NameSpace</code>.
   */
  public List<ImportSet> importSet() {
    return this.importSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportSet
  /**
   ** Return the {@link List} of {@link File}s to export from this
   ** <code>NameSpace</code>.
   **
   ** @return                  the {@link List} of {@link File}s to export from
   **                          this <code>NameSpace</code>.
   */
  public List<ExportSet> exportSet() {
    return this.exportSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   folderSet
  /**
   ** Returns the {@link List} of {@link File}s to maintain in this
   ** <code>NameSpace</code>.
   **
   ** @return                  the {@link List} of {@link File}s to maintain in
   **                          this <code>NameSpace</code>.
   */
  public List<FolderSet> folderSet() {
    return this.folderSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return list()[0].hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>NameSpace</code> object that
   ** represents the same <code>name</code> as this object.
   **
   ** @param other               the object to compare this
   **                            <code>NameSpace</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>NameSpace</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof NameSpace))
      return false;

    final NameSpace namespace = (NameSpace)other;
    String          thisPath  = list()[0];
    String          otherPath = namespace.list()[0];
    return thisPath.equals(otherPath);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredImportSet
  /**
   ** Add the specified {@link List} of {@link File}s to the managed
   ** {@link List} of file sets.
   **
   ** @param  importSet          the collection of {@link File}s to be added.
   */
  public void addConfiguredImportSet(final ImportSet importSet) {
    this.importSet.add(importSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredExportSet
  /**
   ** Add the specified {@link List} of {@link File}s to the managed
   ** {@link List} of file sets.
   **
   ** @param  exportSet          the collection of {@link File}s to be added.
   */
  public void addConfiguredExportSet(final ExportSet exportSet) {
    this.exportSet.add(exportSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredFileSet
  /**
   ** Add the specified {@link List} of {@link File}s to the managed
   ** {@link List} of file sets.
   **
   ** @param  folderSet          the collection of {@link File}s to be added.
   */
  public void addConfiguredFolderSet(final FolderSet folderSet) {
    this.folderSet.add(folderSet);
  }
}