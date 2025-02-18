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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared LDIF Facilities

    File        :   LDIFOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDIFOperation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import oracle.iam.identity.foundation.ldap.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// abstract class LDIFOperation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Represents arbitrary operations that can be used with a a particular LDIF
 ** operation.
 */
public abstract class LDIFOperation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the recommended prefix of temporary file produced by any subclass of
   ** <code>LDIFOperation</code>
   */
  public static final String  TMPPREFIX     = "~ldif";

  /**
   ** the recommended file extension of temporary file produced by any subclass
   ** of <code>LDIFOperation</code>
   */
  public static final String  TMPEXTENSION  = ".tmp";

  /**
   ** the recommended file extension of CSV files produced or consumned by any
   ** subclass of <code>LDIFOperation</code>
   */
  public static final String  LDIFEXTENSION = ".ldif";

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String sourceFile;

  private String sourceDomain;
  private String targetDomain;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default constructor for the <code>LDIFOperation</code> class.
   */
  protected LDIFOperation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceFile
  /**
   ** Sets the name of the source file to use by this
   ** <code>LDIFOperation</code>.
   **
   ** @param  sourceFile         the name of the source file to use by this
   **                            <code>LDIFOperation</code>.
   */
  protected void sourceFile(final String sourceFile) {
    this.sourceFile = sourceFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceFile
  /**
   ** Returns the name of the source file this <code>LDIFOperation</code> use.
   **
   ** @return                    the name of the source file this
   **                            <code>LDIFOperation</code> use..
   */
  public String sourceFile() {
    return this.sourceFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceDomain
  /**
   ** Sets the name of the source file to use by this
   ** <code>LDIFOperation</code>.
   **
   ** @param  sourceDomain         the name of the source file to use by this
   **                            <code>LDIFOperation</code>.
   */
  protected void sourceDomain(final String sourceDomain) {
    this.sourceDomain = sourceDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceDomain
  /**
   ** Returns the name of the source file this <code>LDIFOperation</code> use.
   **
   ** @return                    the name of the source file this
   **                            <code>LDIFOperation</code> use.
   */
  public String sourceDomain() {
    return this.sourceDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   targetDomain
  /**
   ** Sets the name of the source file to use by this
   ** <code>LDIFOperation</code>.
   **
   ** @param  targetDomain         the name of the source file to use by this
   **                            <code>LDIFOperation</code>.
   */
  protected void targetDomain(final String targetDomain) {
    this.targetDomain = targetDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   targetDomain
  /**
   ** Returns the name of the source file this <code>LDIFOperation</code> use.
   **
   ** @return                    the name of the source file this
   **                            <code>LDIFOperation</code> use.
   */
  public String targetDomain() {
    return this.targetDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createReader
  /**
   ** Creates a new {@link LDIFReader} for the registered source file name of
   ** this <code>LDIFOperation</code>.
   **
   ** @return                    the new created {LDIFReader}.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  protected LDIFReader createReader()
    throws DirectoryException {

    return createReader(this.sourceFile);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createReader
  /**
   ** Creates a new {@link LDIFReader}.
   **
   ** @param  sourceFile         the file name that the {@link LDIFReader} will
   **                            use.
   **
   **
   ** @return                    the new created {LDIFReader}.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  protected LDIFReader createReader(final String sourceFile)
    throws DirectoryException {

    return new LDIFReader(sourceFile);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createWriter
  /**
   ** Creates a new {@link LDIFWriter}.
   **
   ** @param  targetFile         the file name that the {@link LDIFWriter} will
   **                            use.
   **
   **
   ** @return                    the new created {Writer}.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  protected LDIFWriter createWriter(final String targetFile)
    throws DirectoryException {

    return new LDIFWriter(targetFile);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   substituteContextSuffix
  /**
   ** Substitutes the root context suffix.
   **
   ** @param  distinguishedName  the distinguished name of the source.
   **
   ** @return                    the distinguished name.
   */
  protected String substituteContextSuffix(final String distinguishedName) {
    final String sourceDomain = this.sourceDomain();
    final String targetDomain = this.targetDomain();
    String dn = distinguishedName;
    if (sourceDomain != null && sourceDomain.length() > 0 && targetDomain != null && targetDomain.length() > 0) {
      final int position = dn.toLowerCase().trim().lastIndexOf(sourceDomain);
      if (position > 0)
        dn = dn.substring(0, position) + targetDomain;
    }
    return dn;
  }
}