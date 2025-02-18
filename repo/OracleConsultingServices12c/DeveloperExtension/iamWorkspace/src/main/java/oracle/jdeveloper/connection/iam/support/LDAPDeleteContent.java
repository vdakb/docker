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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   LDAPDeleteContent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LDAPDeleteContent.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import javax.naming.directory.Attribute;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// class LDAPDeleteContent
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** An object of this class represents the content of an LDIF record that
 ** specifies the deletion of an entry.
 ** <p>
 ** This class implements the {@link LDAPRecord} interface.
 ** <p>
 ** To get this object from an <code>LDAPRecord</code> object, use the
 ** <code>content</code> method and cast the return value as
 ** <code>LDAPDeleteContent</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LDAPDeleteContent extends LDAPRecord {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPDeleteContent</code> object to specify that an
   ** entry should be deleted.
   ** <br>
   ** (The DN identifying the entry is part of the <code>LDAPRecord</code>
   ** object.)
   */
  public LDAPDeleteContent() {
    // ensure inheritance
    super(DELETE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPDeleteContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @param  namespace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDAPDeleteContent(final String namespace) {
    // ensure inheritance
    super(DELETE, namespace);
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

    throw new DirectoryException(Bundle.string(Bundle.CONTEXT_OPERATION_NOT_SUPPORTED));
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
    String s = StringUtility.EMPTY;
    if (controls() != null)
      s += controlString();

    return "LDAPDeleteContent {" + s + "}";
  }
}