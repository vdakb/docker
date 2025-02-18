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

    File        :   LDIFModification.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDIFModification.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import javax.naming.NamingException;

import javax.naming.directory.DirContext;
import javax.naming.directory.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class LDIFModification
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Specifies changes to be made to the values of an attribute. The change is
 ** specified in terms of the following aspects:
 ** <ul>
 **   <li>the type of modification
 **       (add, replace, or delete the value of an attribute)
 **   <li>the type of value being modified (string or binary)
 **   <li>the name of the attribute being modified
 **   <li>the actual value
 ** </ul>
 ** After you specify a change to an attribute, you can execute the change by
 ** calling the <code>LDAPConnection.modify</code> method and specifying the DN
 ** of the entry that you want to modify.
 */
public class LDIFModification {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private int             operation;
  private Attribute       attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a modification from another existing modification.
   ** <p>
   ** Effectively, this makes a copy of the existing attributes and operation.
   **
   ** @param  other              the modification to copy
   */
  public LDIFModification(final LDIFModification other) {

    this(other.operation(), (Attribute)other.attribute.clone());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Specifies a modification to be made to an attribute.
   **
   ** @param  operation          the type of modification to make.
   **                            This can be one of the following:
   **                            <ul>
   **                              <li>{@link DirContext#ADD_ATTRIBUTE}
   **                                  (the value should be added to the
   **                                  attribute)
   **                              <li>{@link DirContext#REMOVE_ATTRIBUTE}
   **                                  (the value should be removed from the
   **                                  attribute)
   **                              <li>{@link DirContext#REPLACE_ATTRIBUTE}
   **                              (the value should replace the existing value
   **                              of the attribute)
   **                            </ul>
   ** @param  attribute          the attribute (possibly with values) to modify.
   **
   ** @see    Attribute
   */
  public LDIFModification(final int operation, final Attribute attribute) {

    // ensure inheritance
    super();

    this.operation = operation;
    this.attribute = attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Returns the type of modification specified by this object.
   **
   ** @return                    one of the following types of modifications:
   **                            <ul>
   **                              <li>{@link DirContext#ADD_ATTRIBUTE}
   **                                  (the value should be added to the
   **                                  attribute)
   **                              <li>{@link DirContext#REMOVE_ATTRIBUTE}
   **                                  (the value should be removed from the
   **                                  attribute)
   **                              <li>{@link DirContext#REPLACE_ATTRIBUTE}
   **                              (the value should replace the existing value
   **                              of the attribute)
   **                            </ul>
   */
  public int operation() {
    return this.operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (Attribute)
  /**
   ** Retrieves the number of values in this attribute.
   **
   ** @return                    the nonnegative number of values in this
   **                            attribute.
   */
  public int size() {
    return this.attribute.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getID (Attribute)
  /**
   ** Retrieves the id of this attribute.
   **
   ** @return                    the id of this attribute. It cannot be null.
   */
  public String getID() {
    return this.attribute.getID();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Retrieves the attribute value from the ordered list of attribute values.
   ** <p>
   ** This method returns the value at the <code>index</code> index of the list
   ** of attribute values. If the attribute values are unordered, this method
   ** returns the value that happens to be at that index.
   **
   ** @param  index              the index of the value in the ordered list of
   **                            attribute values. 0 &lt;= <code>index</code>
   **                             &lt; <code>size()</code>.
   **
   ** @return                    the possibly <code>null</code> attribute value
   **                            at index <code>ix</code>; <code>null</code> if
   **                            the attribute value is <code>null</code>.
   **
   ** @throws NamingException    if a naming exception was encountered while
   **                            retrieving the value.
   */
  public Object get(final int index)
    throws NamingException {

    return this.attribute.get(index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the {@link Attribute} of modification specified by this object.
   **
   ** @return                    the {@link Attribute} of modification specified
   **                            by this object.
   */
  public Attribute attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation of the current modification.
   ** <p>
   ** For example:
   ** <pre>
   **   LDIFModification: REPLACE, LDAPAttribute {type='mail', values='babs@ace.com'}
   **   LDIFModification: ADD, LDAPAttribute {type='description', values='This entry was modified with the modattrs program'}
   ** </pre>
   **
   ** @return                    string representation of the current modification.
   */
  public String toString() {
    String s = "LDIFModification: ";
    switch (this.operation) {
      case DirContext.ADD_ATTRIBUTE     : s += "ADD, "; break;
      case DirContext.REMOVE_ATTRIBUTE  : s += "DELETE, "; break;
      case DirContext.REPLACE_ATTRIBUTE : s += "REPLACE, "; break;
      default                           : s += "INVALID OP, ";
    }
    s += this;
    return s;
  }
}