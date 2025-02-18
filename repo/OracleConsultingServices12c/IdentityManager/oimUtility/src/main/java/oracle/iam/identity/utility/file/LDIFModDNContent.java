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

    File        :   LDIFModDNContent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDIFModDNContent.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import javax.naming.directory.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class LDIFModDNContent
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** An object of this class represents the content of an LDIF record that
 ** specifies changes to an RDN or the DN of an entry.
 ** <p>
 ** This class implements the {@link LDAPRecord} interface.
 ** <p>
 ** To get this object from an <code>LDAPRecord</code> object, use the
 ** <code>content</code> method and cast the return value as
 ** <code>LDIFModDNContent</code>.
 */
public class LDIFModDNContent extends LDAPRecord {

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String     newParent      = null;
  private String     rdn            = null;
  private boolean    deleteOldRDN   = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDIFModDNContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use the
   ** <code>setRDN</code>, <code>setNewParent</code>, and
   ** <code>setDeleteOldRDN</code> methods.
   **
   ** @see    LDIFModDNContent#rdn(String)
   ** @see    LDIFModDNContent#parent(String)
   ** @see    LDIFModDNContent#deleteOldRDN(boolean)
   */
  public LDIFModDNContent() {
    // ensure inheritance
    super(MODDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessora dn Mutators  methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rdn
  /**
   ** Sets the new RDN that should be assigned to the entry.
   **
   ** @param  rdn                the new RDN
   **
   ** @see    LDIFModDNContent#rdn()
   */
  public void rdn(String rdn) {
    this.rdn = rdn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rdn
  /**
   ** Returns the new RDN specified in the content of the LDIF record.
   **
   ** @return                    the new RDN.
   **
   ** @see    LDIFModDNContent#rdn(String)
   */
  public String rdn() {
    return this.rdn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Sets the new parent DN that should be assigned to the entry.
   **
   ** @param  parent             the new parent DN for the entry
   **
   ** @see    LDIFModDNContent#parent()
   */
  public void parent(String parent) {
    this.newParent = parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Returns the entry's new parent DN, if specified in the content of the
   ** LDIF record.
   **
   ** @return                    the new parent of the entry.
   **
   ** @see    LDIFModDNContent#parent(String)
   */
  public String parent() {
    return this.newParent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOldRDN
  /**
   ** Sets whether or not the old RDN should be removed as an attribute in the
   ** entry.
   **
   ** @param  deleteOldRDN       if <code>true</code>, remove the attribute
   **                            representing the RDN.  If <code>false</code>,
   **                            leave the attribute in the entry.
   **
   ** @see    LDIFModDNContent#deleteOldRDN()
   */
  public void deleteOldRDN(boolean deleteOldRDN) {
    this.deleteOldRDN = deleteOldRDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOldRDN
  /**
   ** Determines if the content of the LDIF record specifies that the old RDN
   ** should be removed as an attribute in the entry.
   **
   ** @return                    <code>true</code> if the change specifies that
   **                            the attribute representing the RDN should
   **                            be removed, <code>false</code> if the change
   **                            specifies that the attribute should be left in
   **                            the entry.
   **
   ** @see    LDIFModDNContent#deleteOldRDN(boolean)
   */
  public boolean deleteOldRDN() {
    return this.deleteOldRDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (LDAPRecord)
  /**
   ** Adds an attribute to the content of the content.
   **
   ** @param  attribute          the attribute to add
   */
  @Override
  public void add(final Attribute attribute) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (LDAPRecord)
  /**
   ** Returns the string representation of the content of the LDIF record.
   **
   ** @return                    the string representation of the content of the
   **                            LDIF record.
   */
  @Override
  public String toString() {
    String s = "";
    if (this.newParent == null)
      s = s + "new parent() ";
    else
      s = s + "new parent( " + this.newParent + " ), ";
    if (this.deleteOldRDN)
      s = s + "deleteOldRDN( true ), ";
    else
      s = s + "deleteOldRDN( false ), ";
    if (this.rdn == null)
      s = s + "new rdn()";
    else
      s = s + "new rdn( " + this.rdn + " )";

    if (controls() != null) {
      s += controlString();
    }
    return "LDIFModDNContent {" + s + "}";
  }
}