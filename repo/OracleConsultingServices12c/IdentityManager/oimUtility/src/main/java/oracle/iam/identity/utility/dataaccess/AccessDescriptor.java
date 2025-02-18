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

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Data Access Facilities

    File        :   AccessDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AccessDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.ClassUtility;

////////////////////////////////////////////////////////////////////////////////
// class AccessDescriptor
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>AccessDescriptor</code> describes a dataobject logically.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AccessDescriptor<E extends AccessAttribute> extends ArrayList<E> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String           KEY              = "_key";
  public static final String           ROWVERSION       = "_rowver";
  public static final String           VERSION          = "_version";
  public static final String           NOTE             = "_note";
  public static final String           CREATE           = "_create";
  public static final String           CREATEDBY        = "_createBY";
  public static final String           UPDATE           = "_update";
  public static final String           UPDATEBY         = "_updateby";

  public static final String           LOGIN            = "_login";
  public static final String           NAME             = "_name";
  public static final String           STATUS           = "_status";

  public static final AccessDescriptor<AccessAttribute> PKG = new AccessDescriptor<AccessAttribute>("pkg");
  public static final AccessDescriptor<AccessAttribute> OBJ = new AccessDescriptor<AccessAttribute>("onj");
  public static final AccessDescriptor<AccessAttribute> OIU = new AccessDescriptor<AccessAttribute>("oiu");
  public static final AccessDescriptor<AccessAttribute> ORC = new AccessDescriptor<AccessAttribute>("orc");
  public static final AccessDescriptor<AccessAttribute> OST = new AccessDescriptor<AccessAttribute>("ost");
  public static final AccessDescriptor<AccessAttribute> UGP = new AccessDescriptor<AccessAttribute>("ugp");
  public static final AccessDescriptor<AccessAttribute> ACT = new AccessDescriptor<AccessAttribute>("act");
  public static final AccessDescriptor<AccessAttribute> USR = new AccessDescriptor<AccessAttribute>("usr");

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2213748680597944973")
  private static final long            serialVersionUID = -3036514086552806342L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the raw name of the form field */
  private final String name;

  /** the mapping name of the form field */
  private final String label;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessDescriptor</code> with the initial of five.
   */
  public AccessDescriptor() {
    this(null, null, 5);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessDescriptor</code> with the passed information and
   ** with the initial of five.
   **
   ** @param  name               the raw name of the form object.
   */
  public AccessDescriptor(final String name) {
    this(name, name, 5);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessDescriptor</code> with the passed information and
   ** with the initial of five.
   **
   ** @param  name               the raw name of the form object.
   ** @param  label              the mapping name of the form object.
   */
  public AccessDescriptor(final String name, final String label) {
    this(name, label, 5);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Creates an empty  <code>AccessDescriptor</code>  with the passed information
   ** and with the specified initial capacity.
   **
   ** @param  name               the raw name of the form object.
   ** @param  label              the mapping name of the form object.
   ** @param  initialCapacity    the capacity to initialize.
   */
  public AccessDescriptor(final String name, final String label, final int initialCapacity) {
    // ensure inheritance
    super(initialCapacity);

    this.name  = name;
    this.label = label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>AccessDescriptor</code> object from the specified
   ** <code>FormObject</code>.
   ** <br>
   ** Copy Constructor.
   **
   ** @param  other              the <code>AccessDescriptor</code> to copy.
   */
  public AccessDescriptor(final AccessDescriptor<E> other) {
    // ensure inheritance
    super(other);

    this.name  = other.name;
    this.label = other.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the raw name of the form field.
   **
   ** @return                    the raw name of the form field.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Returns the mapping name of the form field.
   **
   ** @return                    the mapping name of the form field.
   */
  public final String label() {
    return this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyField
  /**
   ** Returns the mapping key name of the form field.
   **
   ** @return                    the mapping key name of the form field.
   */
  public String keyField() {
    return qualifiedColumnName(this, KEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createField
  /**
   ** Returns the mapping name of the form field.
   **
   ** @return                    the mapping name of the form field.
   */
  public String createField() {
    return qualifiedColumnName(this, CREATE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateField
  /**
   ** Returns the mapping name of the form field.
   **
   ** @return                    the mapping name of the form field.
   */
  public String updateField() {
    return qualifiedColumnName(this, UPDATE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualifiedColumnName
  /**
   ** Build a full qualified column name for a field.
   **
   ** @param  object             the data object.
   ** @param  columnnName        the name of the column.
   **
   ** @return                    the name of the form field.
   */
  public static String qualifiedColumnName(final AccessDescriptor<? extends AccessAttribute> object, String columnnName) {
    Object[] parameter = {object.name, object.name, columnnName};
    return String.format("%1$s.%2$s%3$s", parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Returns a string representation of this collection. The string
   ** representation consists of a list of the collection's elements in the
   ** order they are returned by its iterator, enclosed in square brackets
   ** ("[]"). Adjacent elements are separated by the characters ", " (comma and
   ** space). Elements are converted to strings as by String.valueOf(Object).
   ** <br>
   ** This implementation creates a string buffer initialized with the short
   ** name of the collection, appends a left square bracket, and iterates over
   ** the collection appending the string representation of each element in
   ** turn. After appending each element except the last, the string ", " is
   ** appended. Finally a right bracket is appended. A string is obtained from
   ** the string buffer, and returned.
   **
   ** @return  String            A string representation of this collection.
   */
  public String toString() {
    final StringBuilder buffer = new StringBuilder(ClassUtility.shortName(this.getClass()));

    buffer.append(SystemConstant.LIST_OPENLIST);
    buffer.append(SystemConstant.LINEBREAK);
    for (Iterator<E> i = this.iterator(); i.hasNext();) {
      buffer.append(i.next());
      buffer.append(SystemConstant.LINEBREAK);
    }

    buffer.append(SystemConstant.LIST_CLOSELIST);
    buffer.append(SystemConstant.LINEBREAK);

    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectClause
  /**
   ** Returns a string representation of this collection. The string
   ** representation consists of a list selectable elements in the
   ** order they are returned by its iterator. Adjacent elements are separated
   ** by the characters ", " (comma and space).
   **
   ** @param  filter             the {@link Set} the specifies the attributes to
   **                            include in the query
   **
   ** @return                    a string representation of this collection as
   **                            selectable elements.
   */
  public String selectClause(final Set<String> filter) {
    StringBuilder buffer = new StringBuilder();
    if (filter != null && filter.size() > 0) {
      for (int i = 0; i < size(); i++) {

        final String name = get(i).name();
        if (filter.contains(name)) {
          if (buffer.length() > 0)
            buffer.append(SystemConstant.COMMA);
          buffer.append(name());
        }
      }
    }
    else {
      for (int i = 0; i < size(); i++) {
        if (i > 0)
          buffer.append(SystemConstant.COMMA);
        buffer.append(name());
        buffer.append(SystemConstant.PERIOD);
        buffer.append(get(i).name());
      }
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromClause
  /**
   ** Returns a string representation of this collection. The string
   ** representation consists of a list selectable objects in the
   ** order they are returned by its iterator. Adjacent elements are separated
   ** by the characters ", " (comma and space).
   **
   ** @return                    a string representation of this collection as
   **                            selectable objects.
   */
  public String fromClause() {
    return this.name;
  }
}