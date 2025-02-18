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

    File        :   LDIFDeleteContent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDIFDeleteContent.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import javax.naming.directory.Attribute;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryError;
import oracle.iam.identity.foundation.ldap.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// class LDIFDeleteContent
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** An object of this class represents the content of an LDIF record that
 ** specifies the deletion of an entry.
 ** <p>
 ** This class implements the {@link LDAPRecord} interface.
 ** <p>
 ** To get this object from an <code>LDAPRecord</code> object, use the
 ** <code>content</code> method and cast the return value as
 ** <code>LDIFDeleteContent</code>.
 */
public class LDIFDeleteContent extends LDAPRecord {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFDeleteContent</code> object to specify that an
   ** entry should be deleted.
   ** <br>
   ** (The DN identifying the entry is part of the <code>LDAPRecord</code>
   ** object.)
   */
  public LDIFDeleteContent() {
    // ensure inheritance
    super(DELETE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (overridden)
  /**
   ** Adds an attribute to the content of the content.
   **
   ** @param  attribute          the attribute to add
   */
  @Override
  public void add(final Attribute attribute)
    throws DirectoryException {

    throw new DirectoryException(DirectoryError.NOTIMPLEMENTED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation of the content of the LDIF record.
   **
   ** @return                    the string representation of the content of the
   **                            LDIF record.
   */
  @Override
  public String toString() {
    String s = SystemConstant.EMPTY;
    if (controls() != null)
      s += controlString();

    return "LDIFDeleteContent {" + s + "}";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toStream
  /**
   ** Writes the content of the LDIF writer.
   **
   ** @param  writer             the receiving LDIF writer.
   */
  public void toStream(final LDIFWriter writer) {
    writer.printAttribute(DirectoryConstant.CHANGE_TYPE, DirectoryConstant.CHANGE_TYPE_DELETE);
  }
}