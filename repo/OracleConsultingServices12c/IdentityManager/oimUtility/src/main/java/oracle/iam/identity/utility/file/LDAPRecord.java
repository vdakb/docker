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
    Subsystem   :   Common Shared LDAP Facilities

    File        :   LDAPRecord.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPRecord.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;

import javax.naming.NamingException;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import javax.naming.ldap.Control;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.ldap.DirectoryError;
import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryException;
import oracle.iam.identity.foundation.ldap.DirectoryFileWriter;

////////////////////////////////////////////////////////////////////////////////
// class LDAPRecord
// ~~~~~ ~~~~~~~~~~~
/**
 ** This class represents the content of an LDAP record.
 ** <p>
 ** An LDAP record can specify an entry or modifications to be made to an entry.
 ** <p>
 ** The following classes implement this interface:
 ** <ul>
 **   <li><code>LDIFAddContent</code> (represents the content of an LDAP record
 **       that adds a new entry)
 **   <li><code>LDIFDeleteContent</code> (represents the content of an LDAP
 **       record that deletes an entry)
 **   <li><code>LDIFModifyContent</code> (represents the content of an LDAP
 **       record that modifies an entry)
 **   <li><code>LDIFModDNContent</code> (represents the content of an LDAP
 **       record that changes the RDN or DN of an entry)
 ** </ul>
 **
 ** @see LDIFAddContent
 ** @see LDIFDeleteContent
 ** @see LDIFModifyContent
 ** @see LDIFModDNContent
 */
public class LDAPRecord {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the LDAP record specifies an entry and its attributes. */
  public final static int           CONTENT      = 0;

  /** the LDIF record specifies a new entry to be added. */
  public final static int           ADD          = 1;

  /** the LDIF record specifies an entry to be deleted. */
  public final static int           DELETE       = 2;

  /** the LDIF record specifies modifications to an entry. */
  public final static int           MODIFICATION = 3;

  /** the LDIF record specifies changes to the DN or RDN of an entry. */
  public final static int           MODDN        = 4;

  static final Map<Integer, String> clazz        = new HashMap<Integer, String>();

  /////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    clazz.put(new Integer(CONTENT),      "LDAPRecord");
    clazz.put(new Integer(ADD),          "LDIFAddContent");
    clazz.put(new Integer(DELETE),       "LDIFDeleteContent");
    clazz.put(new Integer(MODIFICATION), "LDIFModifyContent");
    clazz.put(new Integer(MODDN),        "LDIFModDNContent");
  }

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final int        type;
  private final Attributes attributes;
  private String           nameSpace  = null;
  private Control[]        controls   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPRecord</code> object with no attributes
   ** specified.
   ** <p>
   ** You can use the {@link #add(Attribute)} method to add attributes to
   ** this object.
   */
  public LDAPRecord() {
    // ensure inheritance
    this(CONTENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPRecord</code> object with the attributes
   ** specified.
   ** <p>
   ** You can use the {@link #add(Attribute)} method to add additonal attributes
   ** to this object.
   **
   ** @param  nameSpace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   */
  public LDAPRecord(final String nameSpace) {
    // ensure inheritance
    this(CONTENT, nameSpace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPRecord</code> object with the attributes
   ** specified.
   ** <p>
   ** You can use the {@link #add(Attribute)} method to add additonal attributes
   ** to this object.
   **
   ** @param  nameSpace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   ** @param  attributes         the {@link Attributes} for this record.
   */
  public LDAPRecord(final String nameSpace, final Attributes attributes) {
    // ensure inheritance
    this(CONTENT, nameSpace, null, attributes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPRecord</code> object with no attributes
   ** specified.
   ** <p>
   ** To specify the modifications to be made to the entry, use the
   ** <code>add</code> method.
   **
   ** @param  nameSpace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   ** @param  controls           an array of {@link Control} objects or
   **                            <code>null</code> if none are to be specified.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDAPRecord(final String nameSpace, final Control[] controls) {
    // ensure inheritance
    this(CONTENT, nameSpace, controls);
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
  public LDAPRecord(final Control[] controls, final LDAPRecord other) {
    // ensure inheritance
    this(CONTENT, other.nameSpace(), controls, other.attributes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPRecord</code> object with no attributes
   ** specified.
   ** <p>
   ** You can use the {@link #add(Attribute)} method to add attributes to
   ** this object.
   **
   ** @param  type               the content type.
   */
  protected LDAPRecord(final int type) {
    // ensure inheritance
    super();

    // initialize instance
    this.type       = type;
    this.attributes = new BasicAttributes();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPRecord</code> object with no attributes
   ** specified that has the specified type and belongs to the specified
   ** distinguished name.
   **
   ** @param  type               the content type.
   ** @param  nameSpace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   */
  protected LDAPRecord(final int type, final String nameSpace) {
    // ensure inheritance
    this(type, nameSpace, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>LDAPRecord</code> object with no attributes.
   **
   ** @param  type               the content type.
   ** @param  nameSpace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   ** @param  controls           an array of {@link Control} objects or
   **                            <code>null</code> if none are to be specified.
   */
  protected LDAPRecord(final int type, final String nameSpace, final Control[] controls) {
    // ensure inheritance
    this(type, nameSpace, controls, new BasicAttributes());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDIFAddContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @param  type               the content type.
   ** @param  controls           an array of {@link Control} objects or
   **                            <code>null</code> if none are to be specified.
   ** @param  other              the record.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  protected LDAPRecord(final int type, final Control[] controls, final LDAPRecord other) {
    // ensure inheritance
    this(type, other.nameSpace, controls, other.attributes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPRecord</code> object with no attributes
   ** specified that has the specified type and belongs to the specified
   ** distinguished name.
   **
   ** @param  type               the content type.
   ** @param  nameSpace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   ** @param  controls           an array of {@link Control} objects or
   **                            <code>null</code> if none are to be specified.
   ** @param  attributes         the {@link Attributes} for this record.
   */
  protected LDAPRecord(final int type, final String nameSpace, final Control[] controls, final Attributes attributes) {
    // ensure inheritance
    super();

    // initialize instance
    this.type       = type;
    this.nameSpace  = nameSpace;
    this.controls   = controls;
    this.attributes = attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Determines the content type.
   ** <p>
   ** You can use this with the <code>content</code> method of the
   ** <code>LDIFRecord</code> object to determine the type of content specified
   ** in the record.
   **
   ** @return                    the content type, identified by one of the
   **                            following values:
   **                            <ul>
   **                              <li>{@link #CONTENT}
   **                                   (specifies an entry and its attributes)
   **                              <li>{@link #ADD}
   **                                  (specifies a new entry to be added)
   **                              <li>{@link #DELETE}
   **                                  (specifies an entry to be deleted)
   **                              <li>{@link #MODIFICATION}
   **                                  (specifies an entry to be modified)
   **                              <li>{@link #MODDN}
   **                                  (specifies a change to the RDN or DN of an entry)
   **                            </ul>
   */
  public final int type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameSpace
  /**
   ** Sets the distinguished name this content belongs to.
   **
   ** @param  nameSpace          the distinguished name this content belongs to.
   */
  protected final void nameSpace(final String nameSpace) {
    this.nameSpace = nameSpace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameSpace
  /**
   ** Returns the distinguished name this content belongs to.
   **
   ** @return                    the distinguished name this content belongs to.
   */
  protected final String nameSpace() {
    return this.nameSpace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controls
  /**
   ** Sets the list of controls
   **
   ** @param  controls           an array of <code>LDAPControl</code> objects or
   **                            <code>null</code> if none are to be specified.
   */
  public void controls(Control[] controls) {
    this.controls = controls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controls
  /**
   ** Retrieves the list of controls specified in the content of the LDAP
   ** record, if any
   **
   ** @return                    an array of <code>LDAPControl</code> objects
   **                            that represent any controls specified in the
   **                            LDAP record, or <code>null</code> if none were
   **                            specified.
   */
  public Control[] controls() {
    return this.controls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Retrieves the list of the attributes specified in the content of the LDAP
   ** record.
   **
   ** @return                    an array of {@link Attribute}s objects
   **                            that represent modifications specified in the
   **                            content of the LDAP record.
   */
  public Attributes attributes() {
    return this.attributes;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds all attributes of the provided collaction to the content of the
   ** LDAP record.
   **
   ** @param  attribute          the attributes to add
   **
   ** @throws DirectoryException if a naming exception was encountered while
   **                            retrieving the attribute value.
   */
  public void add(final Attribute[] attribute)
    throws DirectoryException {

    for (int i = 0; i < attribute.length; i++)
      add(attribute[i]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds an attribute to the content of the content.
   **
   ** @param  attribute          the attribute to add.
   **
   ** @throws DirectoryException if a naming exception was encountered while
   **                            retrieving the attribute value.
   */
  public void add(final Attribute attribute)
    throws DirectoryException {

    if (DirectoryConstant.DN.equalsIgnoreCase(attribute.getID()))
      try {
        this.nameSpace((String)attribute.get());
      }
      catch (NamingException e) {
        throw new DirectoryException(DirectoryError.UNEXPECTED, e);
      }
    else
      this.attributes.put(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Returns the string representation of the content of the LDAP record.
   **
   ** @return                    the string representation of the content of the
   **                            LDAP record.
   */
  public String toString() {
    String s  = this.attributes.toString();

    if (controls() != null)
      s += controlString();

    return clazz.get(type) + " {" + s + "}";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toStream
  /**
   ** Writes the content of the {@link DirectoryFileWriter}.
   **
   ** @param  writer             the receiving {@link DirectoryFileWriter}.
   */
  public final void toStream(final DirectoryFileWriter writer) {
    writer.printEntryStart(this.nameSpace);

    final Enumeration<? extends Attribute> i = this.attributes.getAll();
    while (i.hasMoreElements())
      writer.printAttribute(i.nextElement());

    writer.printEntryEnd(this.nameSpace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controlString
  /**
   ** Returns the OIDs of all controls, if any, as a string
   **
   ** @return                    the OIDs of all controls, if any, as a string,
   **                            or an empty string if there are no controls.
   */
  protected String controlString() {
    String s = SystemConstant.EMPTY;
    if (controls() != null) {
      s += SystemConstant.BLANK;
      Control[] controls = controls();
      int len = controls.length;
      for (int i = 0; i < len; i++) {
        s += controls[i].toString();
        if (i < (len - 1))
          s += SystemConstant.BLANK;
      }
    }
    return s;
  }
}