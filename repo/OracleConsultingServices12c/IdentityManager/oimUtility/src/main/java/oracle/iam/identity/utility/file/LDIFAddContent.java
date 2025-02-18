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

    File        :   LDIFAddContent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDIFAddContent.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import javax.naming.directory.Attribute;

import javax.naming.ldap.Control;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;

////////////////////////////////////////////////////////////////////////////////
// class LDIFAddContent
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** An object of this class represents the content of an LDIF record that
 ** specifies a new entry to be added.
 ** <p>
 ** This class implements the {@link LDAPRecord} interface.
 ** <p>
 ** To get this object from an <code>LDAPRecord</code> object, use the
 ** <code>content</code> method and cast the return value as
 ** <code>LDIFAddContent</code>.
 */
public class LDIFAddContent extends LDAPRecord {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDIFAddContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDIFAddContent() {
    // ensure inheritance
    super(ADD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDIFAddContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @param  nameSpace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDIFAddContent(final String nameSpace) {
    // ensure inheritance
    super(ADD, nameSpace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDIFAddContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @param  nameSpace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   ** @param  controls           an array of {@link Control} objects or
   **                            <code>null</code> if none are to be specified.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDIFAddContent(final String nameSpace, final Control[] controls) {
    // ensure inheritance
    super(ADD, nameSpace, controls);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDIFAddContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @param  controls           an array of {@link Control} objects or
   **                            <code>null</code> if none are to be specified.
   ** @param  other             the record.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDIFAddContent(final Control[] controls, final LDAPRecord other) {
    // ensure inheritance
    super(ADD, controls, other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toStream
  /**
   ** Writes the content of the LDIF writer.
   **
   ** @param  writer             the receiving LDIF writer.
   */
  public void toStream(final LDIFWriter writer) {
    writer.printAttribute(DirectoryConstant.CHANGE_TYPE, DirectoryConstant.CHANGE_TYPE_ADD);
    super.toStream(writer);
  }
}