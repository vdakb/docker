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

    File        :   DirectoryValue.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryValue.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;
import oracle.jdeveloper.workspace.iam.utility.CollectionUtility;

import oracle.jdeveloper.connection.iam.service.DirectoryService;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryValue
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryValue</code> implements the base functionality
 ** for describing an attribute value in a Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryValue extends ArrayList<DirectoryValue.Item> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6171561294761967452")
  private static final long serialVersionUID = 7727182834760764886L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  public final DirectoryAttribute type;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Item
  // ~~~~~ ~~~~
  /**
   ** The item element of a {@link DirectoryValue}.
   */
  public static class Item implements Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-6469176551522655345")
    private static final long               serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final boolean                   binary;
    private final int                       length;
    private final Set<DirectorySchema.Flag> flag;

    private transient Object                origin;
    private transient Object                value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Item</code> which belongs to the specified value.
     **
     ** @param  value            the value of the attribute.
     **                          <br>
     **                          Allowed object is {@link Object}.
     ** @param  flag             the flags for the attribute.
     **                          <br>
     **                          <code>null</code> means clear all flags.
     **                          <br>
     **                          Allowed object is is {@link Set} where each
     **                          element is of type
     **                          {@link DirectorySchema.Flag}.
     */
    private Item(final Object value, final Set<DirectorySchema.Flag> flag) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.flag   = flag;
      this.binary = (value instanceof byte[]);
      this.length = this.binary ? ((byte[])value).length : 1;
      this.origin = this.binary ? StringUtility.bytesToString((byte[])value) : value;
      this.value  = this.origin;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: readable
    /**
     ** Determines if the attribute is readable.
     **
     ** @return                  <code>true</code> if the attribute is readable;
     **                          otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean readable() {
      return !this.flag.contains(DirectorySchema.Flag.NOT_READABLE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: createable
    /**
     ** Determines if the attribute is writable on create.
     **
     ** @return                  <code>true</code> if the attribute is writable
     **                          on create; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean createable() {
      return !this.flag.contains(DirectorySchema.Flag.NOT_CREATEABLE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updateable
    /**
     ** Determines if the attribute is writable on update.
     **
     ** @return                  <code>true</code> if the attribute is writable
     **                          on update; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean updateable() {
      return !this.flag.contains(DirectorySchema.Flag.NOT_UPDATEABLE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: readonly
    /**
     ** Determines if the attribute is readonly.
     **
     ** @return                  <code>true</code> if the attribute is readonly;
     **                          otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean readonly() {
      return this.flag.contains(DirectorySchema.Flag.OPERATIONAL)
          || this.flag.contains(DirectorySchema.Flag.NOT_CREATEABLE)
          || this.flag.contains(DirectorySchema.Flag.NOT_UPDATEABLE)
      ;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: required
    /**
     ** Determines if the attribute is required for an object.
     **
     ** @return                  <code>true</code> if the attribute is required
     **                          for an object; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean required() {
      return this.flag.contains(DirectorySchema.Flag.REQUIRED);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: operational
    /**
     ** Determines if the attribute is operational for an object.
     **
     ** @return                  <code>true</code> if the attribute is
     **                          operational for an object; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean operational() {
      return this.flag.contains(DirectorySchema.Flag.OPERATIONAL);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: objectClass
    /**
     ** Determines if the attribute the object class for an object.
     **
     ** @return                  <code>true</code> if the attribute the object
     **                          class for an object; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean objectClass() {
      return this.flag.contains(DirectorySchema.Flag.OBJECTCLASS);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns a string representation of this instance.
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String value() {
      if (this.value == null)
        // hack to get broken swing printing working
        return StringUtility.EMPTY;

      // moved this line of code to before the following one...seems to fix the
      // displaying of '(binary Value)' to empty attribute values when the reset
      // button is clicked.
      if (this.binary)
        return "(binary)";

      // hack to get broken swing printing working
      final String returning = this.value.toString();
      return (returning.length() == 0) ? StringUtility.EMPTY : returning;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: created
    /**
     ** Returns <code>true</code> if the actual value of this instance is not
     ** <code>null</code> but the original value stored at is <code>null</code>.
     **
     ** @return                  <code>true</code> if the actual value of this
     **                          instance is not <code>null</code> but the
     **                          original values <code>null</code>.
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean created() {
      return created(this.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: created
    /**
     ** Returns <code>true</code> if an {@link Object} is reated due to the
     ** original value is null.
     **
     ** @param  value            the value to compare with the origin.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if an {@link Object} created;
     **                          otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean created(final Object value) {
      return (value != null && this.origin == null);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: changed
    /**
     ** Returns <code>true</code> if the actual value of this instance is not
     ** equal to the original value stored at this instance.
     **
     ** @return                  <code>true</code> if the actual value of this
     **                          instance is not equal to the original value
     **                          stored at this instance; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean changed() {
      return changed(this.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: changed
    /**
     ** Returns <code>true</code> if an {@link Object} is not equal to the
     ** original value stored at this instance.
     **
     ** @param  value            the value to compare with the origin.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if an {@link Object} is not
     **                          equal to the original value stored at this
     **                          instance; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean changed(final Object value) {
      boolean changed = false;
      if (value == null) {
        // handle empty strings and nulls...
        changed = !(this.origin == null || StringUtility.EMPTY.equals(this.origin));
      }
      else if (this.binary && value != null) {
        // do a string compare
        changed = (!value.equals(this.origin));
      }
      else
        // if value not the same as original, it's changed!
        changed = (!value.equals(this.origin));

      return changed;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this instance.
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return value();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: commit
    /**
     ** Commit the value to what it was changed.
     */
    public void commit() {
      this.origin = this.value;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Method: revert
    /**
     ** Revert the value back to what it was initialized with.
     */
    public void revert() {
      this.value = this.origin;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   update
    /**
     ** Update object
     **
     ** @param  value            the value for update.
     **                          <br>
     **                          Allowed object is {@link Object}.
     */
    public void update(final Object value) {
      this.value = this.binary ? StringUtility.bytesToString((byte[])value) : value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryValue</code> which belongs to the specified
   ** type and has the specified value.
   **
   ** @param  type               the type of the attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryAttribute}.
   ** @param  value              the value collection of the attribute.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private DirectoryValue(final DirectoryAttribute type, final List<Object> value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type  = type;
    if (value != null) {
      for (Object cursor : value)
        add(item(cursor, type.flag));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printable
  /**
   ** Returns <code>true</code> if this attribute value is printable (seems to
   ** be a string).
   **
   ** @return                    <code>true</code> if this attribute value is
   **                            printable (seems to be a string); otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean printable() {
    return !this.type.binary();
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
   **                            at index <code>index</code> that was replaced.
   **                            <code>null</code> if the attribute value was
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Item}.
   */
  public Item set(final int index, final byte[] value) {
    return update(index, value);
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
   ** @param   index             the index of the value in the ordered list of
   **                            attribute values.
   **                            <code>0 &lt;= index &lt;= size()</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  value              the possibly <code>null</code> attribute value
   **                            to use.
   **                            If <code>null</code>, <code>null</code>
   **                            replaces the old value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the possibly <code>null</code> attribute value
   **                            at index <code>index</code> that was replaced.
   **                            <code>null</code> if the attribute value was
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Item}.
   */
  public Item set(final int index, final String value) {
    return update(index, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueAt
  /**
   ** Returns the value of this value holder.
   **
   ** @param  index              the index of the element value to return.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the value of this value holder.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  public final Object valueAt(int index) {
    return get(index).value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Determines if the value is changed.
   **
   ** @return                    <code>true</code> if any value is changed in
   **                            the collection; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean changed() {
    boolean dirty = false;
    for (Item cursor : this)
      if (cursor.changed()) {
        dirty = true;
        break;  
      }
    return dirty;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (List)
  /**
   ** Adds an attribute value to the list of attribute values.
   ** <br>
   ** This method adds <code>value</code> to the list of attribute values at
   ** the end of the list of attribute values.
   ** <br>
   ** If the attribute values are unordered and already have <code>value</code>,
   ** <code>IllegalStateException</code> is thrown.
   **
   ** @param  value              the possibly <code>null</code> attribute value
   **                            to add; if <code>null</code>, <code>null</code>
   **                            is the value added.
   **                            <br>
   **                            Allowed object is {@link Item}.
   **
   ** @return                    <code>true</code> (as specified by
   **                            {@link List#add}).
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean add(final Item value) {
    if (contains(value))
      throw new IllegalStateException("Cannot add duplicate to unordered attribute");

    // ensure inheritance
    return super.add(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (List)
  /**
   ** Inserts an attribute value to the list of attribute values at the
   ** specified position.
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
   ** @param  index              the index in the ordered list of attribute
   **                            values to add the new value.
   **                            <code>0 &lt;= index &lt;= size()</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  value              the possibly <code>null</code> attribute value
   **                            to add; if <code>null</code>, <code>null</code>
   **                            is the value added.
   **                            <br>
   **                            Allowed object is {@link Item}.
   */
  @Override
  public void add(final int index, final Item value) {
    if (contains(value))
      throw new IllegalStateException("Cannot add duplicate to unordered attribute");

    super.add(index, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   item
  /**
   ** Convenience method to create a DirectoryValue.Item.
   **
   ** @param  value              the value of the attribute.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  flag               the flags for the attribute.
   **                            <br>
   **                            <code>null</code> means clear all flags.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema.Flag}.
   **
   ** @return                    the collection of values.
   **                            <br>
   **                            Possible object is 
   **                            {@link DirectoryValue.Item}.
   */
  public static DirectoryValue.Item item(final Object value, final Set<DirectorySchema.Flag> flag) {
    return new DirectoryValue.Item(value, flag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create a <code>DirectoryValue</code>.
   **
   ** @param  type               the type of the attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryAttribute}.
   **
   ** @return                    the DirectoryValue.
   **                            <br>
   **                            Possible object is 
   **                            <code>DirectoryValue</code>.
   */
  public static DirectoryValue build(final DirectoryAttribute type) {
    return build(type, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create a DirectoryValue.
   **
   ** @param  type               the type of the attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryAttribute}.
   ** @param  value              the value of the attribute.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the DirectoryValue.
   **                            <br>
   **                            Possible object is 
   **                            <code>DirectoryValue</code>.
   */
  public static DirectoryValue build(final DirectoryAttribute type, final Object value) {
    return build(type, CollectionUtility.list(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert
  /**
   ** Revert the values back to what it was initialized with.
   */
  public void revert() {
    forEach(Item::revert);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create a DirectoryValue.
   **
   ** @param  type               the type of the attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryAttribute}.
   ** @param  value              the value of the attribute.
   **                            <br>
   **                            Allowed object is linl List} where each
   **                            element is of type {@link Object}.
   **
   ** @return                    the DirectoryValue.
   **                            <br>
   **                            Possible object is 
   **                            <code>DirectoryValue</code>.
   */
  private static DirectoryValue build(final DirectoryAttribute type, final List<Object> value) {
    return new DirectoryValue(type, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Update object
   **
   ** @param  index              the index of the value in the ordered list of
   **                            attribute values.
   **                            <code>0 &lt;= index &lt;= size()</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  value              the value for update.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the element previously at the specified
   **                            position.
   **                            <br>
   **                            Possible object is {@link Item}.
   */
  private Item update(final int index, final Object value) {
    return set(index, item(value, this.type.flag));
  }
}