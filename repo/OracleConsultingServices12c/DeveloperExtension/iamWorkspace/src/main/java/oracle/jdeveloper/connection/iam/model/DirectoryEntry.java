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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryEntry.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryEntry.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;

import java.io.Serializable;

import javax.naming.NamingException;

import oracle.jdeveloper.workspace.iam.utility.CollectionUtility;

///////////////////////////////////////////////////////////////////////////////
// class DirectoryEntry
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryEntry</code> implements the base functionality for
 ** describing an entry in a Directory Service.
 ** <br>
 ** An Directory Service entries may have attributes or not. Some of them are
 ** mandatory for the functionality.
 ** <p>
 ** This is a schema-aware wrapper to BasicAttribute.
 ** <p>
 ** It goes to a lot of effort to figure out whether the attribute has string
 ** values, or contains 'byte array' values. If it contains byte array values,
 ** it also tries to figure out whether they are ASN1 values. This is important,
 ** as we need to make sure that byte array values are passed correctly to JNDI,
 ** and in addition we need to make sure that ASN1 values are passed using
 ** ';binary'.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryEntry implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7736313908619106252")
  private static final long serialVersionUID = -5904036623783481871L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private DirectoryName                                           name;
  private final transient Map<DirectoryAttribute, DirectoryValue> value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryEntry</code> without a name.
   **
   ** @param  name               the name of the attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   */
  private DirectoryEntry(final DirectoryName name) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name  = name;
    this.value = new TreeMap<DirectoryAttribute, DirectoryValue>(
      // sort by keys, a,b,c..., and return a TreeMac
      // toMap() will returns HashMap by default, we need TreeMap to keep the
      // order
      new Comparator<DirectoryAttribute>() {
        @Override
        public int compare(final DirectoryAttribute lhs, final DirectoryAttribute rhs) {
          return lhs.compareTo(rhs);
        }
      }
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   with
  /**
   ** Sets the long label that represents the context element.
   **
   ** @param  name               the human readable long label of the context
   **                            element.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    this <code>DirectoryEntry</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>DirectoryEntry</code>.
   */
  public final DirectoryEntry with(final DirectoryName name) {
    this.name = name;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Provides the long label that represents the context element.
   **
   ** @return                    the human readable long label of the context
   **                            element.
   **                            <br>
   **                            Possible object is {@link DirectoryName}.
   */
  public DirectoryName name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Retrieves the collection values of this entry.
   ** <p>
   ** By default, the values returned are those passed to the constructor and/or
   ** manipulated using the add/replace/remove methods.
   ** <br>
   ** A subclass may override this to retrieve the values dynamically from the
   ** directory.
   **
   ** @return                    the collection values of this entry.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link DirectoryAttribute}
   **                            for the key and {@link DirectoryValue} as the
   **                            value.
   */
  public Map<DirectoryAttribute, DirectoryValue> value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Returns <code>true</code> if a {@link DirectoryValue} is changed.
   **
   ** @return                    <code>true</code> if a {@link DirectoryValue}
   **                            is changed; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean changed() {
    boolean dirty = false;
    for (DirectoryValue cursor : this.value.values()) {
      if (cursor.changed()) {
        dirty = true;
        break;
      }
    }
    return dirty;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create an <code>DirectoryEntry</code>.
   ** <br>
   ** Equivalent to <code>DirectoryEntry.build(DirectoryName.build(name))</code>.
   **
   ** @param  name               the name of the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>DirectoryEntry</code> with
   **                            the specified <code>name</code>.
   **                            <br>
   **                            Possible object is <code>DirectoryEntry</code>.
   **
   ** @throws NamingException    if a name name syntax violation is detected.
   */
  public static DirectoryEntry build(final String name)
    throws NamingException {

    return build(DirectoryName.build(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create an <code>DirectoryEntry</code>.
   ** <br>
   ** Equivalent to <code>DirectoryEntry.build(name, String.class)</code>.
   **
   ** @param  name               the name of the attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    an instance of <code>DirectoryEntry</code> with
   **                            the specified <code>name</code>.
   **                            <br>
   **                            Possible object is <code>DirectoryEntry</code>.
   */
  public static DirectoryEntry build(final DirectoryName name) {
    return new DirectoryEntry(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return this.name.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>DirectoryAttribute</code> object that
   ** represents the same <code>name</code> and <code>type</code> as this
   ** object.
   **
   ** @param other               the object to compare this
   **                            <code>DirectoryAttribute</code> against.
   **
   ** @return                    <code>true</code> if the
   **                            <code>DirectoryAttribute</code>s are equal;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (other instanceof DirectoryEntry) {
      final DirectoryEntry that = (DirectoryEntry)other;
      if (!this.name.equals(that.name)) {
        return false;
      }
      return CollectionUtility.equals(this.value, that.value);
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of key-value mappings in the attribute map.
   ** <br>
   ** If the map contains more than <code>Integer.MAX_VALUE</code> elements,
   ** returns <code>Integer.MAX_VALUE</code>.
   **
   ** @return                    the number of key-value mappings in the
   **                            attribute map.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int size() {
    return this.value.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Removes all attributes from an entry.
   */
  public void clear() {
    this.value.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert
  /**
   ** Revert the values back to what it was initialized with for the specified
   ** {@link DirectoryAttribute}.
   */
  public void revert() {
    this.value.values().forEach(DirectoryValue::revert);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds an attribute value to the list of attribute values.
   ** <br>
   ** This method adds <code>value</code> to the list of attribute values at
   ** index <code>0</code>.
   ** <br>
   ** Values located at indices at or greater than <code>0</code> are shifted
   ** down towards the end of the list (and their indices incremented by one).
   ** <br>
   ** If the attribute values are unordered and already have <code>value</code>,
   ** <code>IllegalStateException</code> is thrown.
   **
   ** @param  value              the {@link DirectoryValue} to manipulate.
   **                            <br>
   **                            Allowed object is {@link DirectoryValue}.
   */
  public void add(final DirectoryValue value) {
    if (!this.value.containsKey(value.type))
      this.value.put(value.type, value);
    else {
      final DirectoryValue item = this.value.get(value.type);
      if (item == null)
        this.value.put(value.type, value);
      else
        item.addAll(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds an attribute value to the list of attribute values.
   ** <br>
   ** This method adds <code>value</code> to the list of attribute values at
   ** index <code>index</code>.
   ** <br>
   ** Values located at indices at or greater than <code>index</code> are
   ** shifted down towards the end of the list (and their indices incremented by
   ** one).
   ** <br>
   ** If the attribute values are unordered and already have <code>value</code>,
   ** <code>IllegalStateException</code> is thrown.
   **
   ** @param  attribute          the {@link DirectoryAttribute} to manipulate.
   **                            <br>
   **                            Allowed object is {@link DirectoryAttribute}.
   ** @param  index              the index in the ordered list of attribute
   **                            values to add the new value.
   **                            <code>0 &lt;= index &lt;= size()</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  value              the possibly <code>null</code> attribute value
   **                            to add; if <code>null</code>, <code>null</code>
   **                            is the value added.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  public void add(final DirectoryAttribute attribute, final int index, final Object value) {
    if (!this.value.containsKey(attribute))
      this.value.put(attribute, DirectoryValue.build(attribute, value));
    else {
      final DirectoryValue item = this.value.get(attribute);
      if (item == null)
        this.value.put(attribute, DirectoryValue.build(attribute, value));
      else
        item.add(index, DirectoryValue.item(value, attribute.flag));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes an attribute from the ordered list of attribute.
   ** <br>
   ** This method removes the value entirely from the list of attribute values.
   **
   ** @param  attribute          the {@link DirectoryAttribute} to remove.
   **                            <br>
   **                            Allowed object is {@link DirectoryAttribute}.
   **
   ** @return                    the possibly <code>null</code> attribute value
   **                            that was removed; <code>null</code> if the
   **                            attribute value is <code>null</code>.
   **                            <br>
   **                            Possible object is {@link DirectoryValue}.
   */
  public DirectoryValue remove(final DirectoryAttribute attribute) {
    return this.value.remove(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes an attribute value from the ordered list of attribute values.
   ** <br>
   ** This method removes the value at the <code>index</code> index of the list
   ** of attribute values.
   ** <br>
   ** If the attribute values are unordered, this method removes the value that
   ** happens to be at that index.
   ** <br>
   ** Values located at indices greater than <code>index</code> are shifted up
   ** towards the front of the list (and their indices decremented by one).
   **
   ** @param  type               the {@link DirectoryAttribute} to manipulate.
   **                            <br>
   **                            Allowed object is {@link DirectoryValue}.
   ** @param  index              the index of the value to remove.
   **                            <code>0 &lt;= index &lt;= size()</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the possibly <code>null</code> attribute value
   **                            at index <code>index</code> that was removed;
   **                            <code>null</code> if the attribute value is
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link DirectoryValue.Item}.
   */
  public DirectoryValue.Item remove(final DirectoryAttribute type, final int index) {
    if (!this.value.containsKey(type))
      return null;

    final DirectoryValue value = this.value.remove(type);
    return (value == null) ? null : value.remove(index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes an attribute value from the ordered list of attribute values.
   ** <br>
   ** This method removes the value at the <code>index</code> index of the list
   ** of attribute values.
   ** <br>
   ** If the attribute values are unordered, this method removes the value that
   ** happens to be at that index.
   ** <br>
   ** Values located at indices greater than <code>index</code> are shifted up
   ** towards the front of the list (and their indices decremented by one).
   **
   ** @param  value              the {@link DirectoryValue} to manipulate.
   **                            <br>
   **                            Allowed object is {@link DirectoryValue}.
   ** @param  index              the index of the value to remove.
   **                            <code>0 &lt;= index &lt;= size()</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the possibly <code>null</code> attribute value
   **                            at index <code>index</code> that was removed;
   **                            <code>null</code> if the attribute value is
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  public Object remove(final DirectoryValue value, final int index) {
    return value.remove(index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets an attribute value in the ordered list of attribute values.
   ** <br>
   ** This method sets the value at the <code>index</code> index of the list of
   ** attribute values to be <code>value</code>. The old value is removed.
   ** <br>
   ** If the attribute values are unordered, this method sets the value that
   ** happens to be at that index to <code>value</code>, unless
   ** <code>value</code> is already one of the values.
   ** <br>
   ** In that case, <code>IllegalStateException</code> is thrown.
   **
   ** @param  item               the {@link DirectoryValue} to manipulate.
   **                            <br>
   **                            Allowed object is {@link DirectoryValue}.
   ** @param  index              the index of the value in the ordered list of
   **                            attribute values.
   **                            <code>0 &lt;= index &lt;= size()</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  value              the possibly <code>null</code> attribute value
   **                            to use.
   **                            If <code>null</code>, <code>null</code>
   **                            replaces the old value.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the possibly <code>null</code> attribute value
   **                            at index ix that was replaced.
   **                            <code>null</code> if the attribute value was
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link DirectoryValue.Item}.
   */
  public DirectoryValue.Item set(final DirectoryValue item, final int index, final byte[] value) {
    return item.set(index, value);
  }
}