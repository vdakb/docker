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

    File        :   LDAPModDNContent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LDAPModDNContent.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import javax.naming.directory.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class LDAPModDNContent
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** An object of this class represents the content of an LDIF record that
 ** specifies changes to an RDN or the DN of an entry.
 ** <p>
 ** This class implements the {@link LDAPRecord} interface.
 ** <p>
 ** To get this object from an <code>LDAPRecord</code> object, use the
 ** <code>content</code> method and cast the return value as
 ** <code>LDAPModDNContent</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class LDAPModDNContent extends LDAPRecord {

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String  parent    = null;
  private String  rdn       = null;
  private boolean deleteOld = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPModDNContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use the
   ** <code>rdn</code>, <code>parent</code>, and <code>deleteOldRDN</code>
   ** methods.
   **
   ** @see    LDAPModDNContent#rdn(String)
   ** @see    LDAPModDNContent#parent(String)
   ** @see    LDAPModDNContent#deleteOld(boolean)
   */
  public LDAPModDNContent() {
    // ensure inheritance
    super(MODDN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPModDNContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use the
   ** <code>rdn</code>, <code>parent</code>, and <code>deleteOldRDN</code>
   ** methods.
   **
   ** @param  namespace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   ** @param  rdn                the relative distinguished name this record
   **                            belongs to representing the attributes of the
   **                            entry.
   ** @param  parent             the new parent of the entry if there is any.
   ** @param  deleteOld          <code>true</code> if existing values should be
   **                            deleted.
   **
   ** @see    LDAPModDNContent#rdn(String)
   ** @see    LDAPModDNContent#parent(String)
   ** @see    LDAPModDNContent#deleteOld(boolean)
   */
  public LDAPModDNContent(final String namespace, final String rdn, final String parent, final boolean deleteOld) {
    // ensure inheritance
    super(MODDN, namespace);

    // initialize intance attributes
    this.rdn       = rdn;
    this.parent    = parent;
    this.deleteOld = deleteOld;
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
   ** @see    LDAPModDNContent#rdn()
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
   ** @see    LDAPModDNContent#rdn(String)
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
   ** @see    LDAPModDNContent#parent()
   */
  public void parent(final String parent) {
    this.parent = parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Returns the entry's new parent DN, if specified in the content of the
   ** LDIF record.
   **
   ** @return                    the new parent of the entry.
   **
   ** @see    LDAPModDNContent#parent(String)
   */
  public String parent() {
    return this.parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOld
  /**
   ** Sets whether or not the old RDN should be removed as an attribute in the
   ** entry.
   **
   ** @param  deleteOldRDN       if <code>true</code>, remove the attribute
   **                            representing the RDN.  If <code>false</code>,
   **                            leave the attribute in the entry.
   **
   ** @see    LDAPModDNContent#deleteOld()
   */
  public void deleteOld(final boolean deleteOldRDN) {
    this.deleteOld = deleteOldRDN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOld
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
   ** @see    LDAPModDNContent#deleteOld(boolean)
   */
  public boolean deleteOld() {
    return this.deleteOld;
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
    if (this.parent == null)
      s = s + "new parent() ";
    else
      s = s + "new parent( " + this.parent + " ), ";
    if (this.deleteOld)
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
    return "LDAPModDNContent {" + s + "}";
  }
}