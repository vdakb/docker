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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   LDAPRecord.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPRecord.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Enumeration;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import javax.naming.ldap.Rdn;
import javax.naming.ldap.Control;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureConstant;
import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// class LDAPRecord
// ~~~~~ ~~~~~~~~~~
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
 ** @see LDAPAddContent
 ** @see LDAPDeleteContent
 ** @see LDAPModifyContent
 ** @see LDAPModDNContent
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
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
    clazz.put(new Integer(ADD),          "LDAPAddContent");
    clazz.put(new Integer(DELETE),       "LDAPDeleteContent");
    clazz.put(new Integer(MODIFICATION), "LDAPModifyContent");
    clazz.put(new Integer(MODDN),        "LDAPModDNContent");
  }

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final int        type;
  private final Attributes attributes;
  private String           namespace  = null;
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
   ** Constructs an empty <code>LDAPRecord</code> object.
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
    this(CONTENT, other.namespace(), controls, other.attributes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPRecord</code> ofor the specified
   ** <code>type</code>.
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
   ** @param  namespace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   */
  protected LDAPRecord(final int type, final String namespace) {
    // ensure inheritance
    this(type, namespace, null);
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
   ** Constructs an empty <code>LDAPRecord</code> object.
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
    this(type, other.namespace, controls, other.attributes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPRecord</code> object with no attributes
   ** specified that has the specified type and belongs to the specified
   ** distinguished name.
   **
   ** @param  type               the content type.
   ** @param  namespace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   ** @param  controls           an array of {@link Control} objects or
   **                            <code>null</code> if none are to be specified.
   ** @param  attributes         the {@link Attributes} for this record.
   */
  protected LDAPRecord(final int type, final String namespace, final Control[] controls, final Attributes attributes) {
    // ensure inheritance
    super();

    // initialize instance
    this.type       = type;
    this.namespace  = namespace;
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
  // Method:   namespace
  /**
   ** Sets the distinguished name this content belongs to.
   **
   ** @param  namespace          the distinguished name this content belongs to.
   */
  protected final void namespace(final String namespace) {
    this.namespace = namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the distinguished name this content belongs to.
   **
   ** @return                    the distinguished name this content belongs to.
   */
  public final String namespace() {
    return this.namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controls
  /**
   ** Sets the list of controls
   **
   ** @param  controls           an array of <code>LDAPControl</code> objects or
   **                            <code>null</code> if none are to be specified.
   */
  protected void controls(Control[] controls) {
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
   ** @throws FeatureException   if the attribute is a distinguished name.
   */
  public void add(final Attribute[] attribute)
    throws FeatureException {

    for (int i = 0; i < attribute.length; i++)
      add(attribute[i]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds an attribute to the content of the content.
   **
   ** @param  attribute          the attribute to add
   **
   ** @throws FeatureException   if the attribute is a distinguished name.
   */
  public void add(final Attribute attribute)
    throws FeatureException {

    if (FeatureConstant.DN.equalsIgnoreCase(attribute.getID()))
      throw new FeatureException(FeatureError.LDIF_UNEXPECTED, FeatureConstant.DN);

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

    return clazz.get(this.type) + " {" + s + "}";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toStream
  /**
   ** Writes the content of the {@link LDAPWriter}.
   **
   ** @param  writer             the receiving {@link LDAPWriter}.
   */
  public final void toStream(final LDAPWriter writer) {
    // prevent bogus input
    if (this.attributes == null)
      return;

    try {
      // write out the DN.
      LDAPName namespace = new LDAPName(this.namespace);
      writer.printEntryStart(this.namespace);
      // since this method preserves case of the attribute name, we must
      // formulate the lookup table for attribute name normalization.
      final TreeMap<String,String> attributeName = new TreeMap<String,String>();
      for (Enumeration<? extends String> cursor = this.attributes.getIDs(); cursor.hasMoreElements(); ) {
        final String id = cursor.nextElement();
        attributeName.put(id.toLowerCase(), id);
      }
      // obtain objectClass first.
      String aname = attributeName.get(FeatureConstant.OBJECT_CLASS.toLowerCase());
      Attribute out = this.attributes.get(aname);
      if (out != null)
        writer.printAttribute(out);
      // obtain naming attribute next.
      final Rdn entryRDN = namespace.getRdn(namespace.size() - 1);
      if (entryRDN != null) {
        out = this.attributes.get(attributeName.get(entryRDN.getType()).toLowerCase());
        if (out != null)
          writer.printAttribute(out);
      }
      // finish obtaining remaining attributes, in no special sequence.
      for (Enumeration<? extends Attribute> ea = this.attributes.getAll(); ea.hasMoreElements(); ) {
        out = ea.nextElement();
        if ((!FeatureConstant.OBJECT_CLASS.equalsIgnoreCase(out.getID())) && (!entryRDN.getType().equalsIgnoreCase(out.getID()))) {
          writer.printAttribute(out);
        }
      }
      /*
      final Enumeration<? extends Attribute> i = this.attributes.getAll();
      while (i.hasMoreElements())
        writer.printAttribute(i.nextElement());
      */
      writer.printEntryEnd(this.namespace);
    }
    catch (Exception e) {
      ;
    }
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