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

    File        :   LDIFModifyContent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDIFModifyContent.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;

import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// class LDIFModifyContent
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** An object of this class represents the content of an LDIF record that
 ** specifies modifications to an entry.
 ** <p>
 ** This class implements the {@link LDAPRecord} interface.
 ** <p>
 ** To get this object from an <code>LDAPRecord</code> object, use the
 ** <code>content</code> method and cast the return value as
 ** <code>LDIFModifyContent</code>.
 */
public class LDIFModifyContent extends LDAPRecord {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Map<Integer, String> OPERATION = new HashMap<Integer, String>(3);

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<Integer> operation = new ArrayList<Integer>();

  /////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    OPERATION.put(new Integer(DirContext.ADD_ATTRIBUTE),     DirectoryConstant.CHANGE_OPERATION_ADD);
    OPERATION.put(new Integer(DirContext.REPLACE_ATTRIBUTE), DirectoryConstant.CHANGE_OPERATION_REPLACE);
    OPERATION.put(new Integer(DirContext.REMOVE_ATTRIBUTE),  DirectoryConstant.CHANGE_OPERATION_REMOVE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDIFModifyContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDIFModifyContent() {
    // ensure inheritance
    super(MODIFICATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>LDIFModifyContent</code> object with the specified
   ** attributes.
   **
   ** @param  modification       an array of {@link LDIFModification} objects
   **                            representing the attributes of the entry to be
   **                            added.
   **
   ** @throws DirectoryException if a naming exception was encountered while
   **                            retrieving the attribute value.
   */
  public LDIFModifyContent(final LDIFModification[] modification)
    throws DirectoryException {

    // ensure inheritance
    super(MODIFICATION);

    // initialize instance
    for (int i = 0; i < modification.length; i++)
      add(modification[i].operation(), modification[i].attribute());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifications
  /**
   ** Retrieves the list of the attributes specified in the content of the LDIF
   ** record.
   **
   ** @return                    an array of <code>LDIFAttribute</code> objects
   **                            that represent modifications specified in the
   **                            content of the LDIF record.
   */
  public LDIFModification[] modifications() {
    LDIFModification mods[] = new LDIFModification[this.attributes().size()];
    /*
    Enumeration i = this.attributes().getAll();
    while (i.hasMoreElements())
      mods[i] = (LDIFModification)i;
    */
    return mods;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds an attribute to the content of the content.
   **
   ** @param  operation          the operational mode.
   ** @param  attribute          the attribute to add.
   **
   ** @throws DirectoryException if a naming exception was encountered while
   **                            retrieving the attribute value.
   */
  public void add(final int operation, final Attribute attribute)
    throws DirectoryException {

    this.operation.add(new Integer(operation));
    super.add(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toStream (overridden)
  /**
   ** Writes the content of the LDIF writer.
   **
   ** @param  writer             the receiving LDIF writer.
   */
  public void toStream(final LDIFWriter writer) {
    writer.printEntryStart(this.nameSpace());
    writer.printAttribute(DirectoryConstant.CHANGE_TYPE, DirectoryConstant.CHANGE_TYPE_MODIFY);
    int         i = 0;
    Enumeration<? extends Attribute> j = this.attributes().getAll();
    while (j.hasMoreElements()) {
      final Attribute attribute = j.nextElement();
      writer.printAttribute(OPERATION.get(this.operation.get(i++)), attribute.getID());
      writer.printAttribute(attribute);
      writer.printLine("-");
    }
  }
}