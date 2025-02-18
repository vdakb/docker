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

    System      :   Foundation Shared Library
    Subsystem   :   Simple JSON Parser

    File        :   JsonObject.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonObject.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.json.simple;

import java.io.StringWriter;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// final class JsonObject
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Represents a JSON object, a set of name/value pairs, where the names are
 ** strings and the values are JSON values.
 ** <p>
 ** Members can be added using the <code>add(String, ...)</code> methods which
 ** accept instances of {@link JsonValue}, strings, primitive numbers, and
 ** boolean values. To modify certain values of an object, use the
 ** <code>set(String, ...)</code> methods. Please note that the <code>add</code>
 ** methods are faster than <code>set</code> as they do not search for existing
 ** members. On the other hand, the <code>add</code> methods do not prevent
 ** adding multiple members with the same name.
 ** <br>
 ** Duplicate names are discouraged but not prohibited by JSON.
 ** <p>
 ** Members can be accessed by their name using {@link #get(String)}. A list of
 ** all names can be obtained from the method {@link #names()}. This class also
 ** supports iterating over the members in document order using an
 ** {@link #iterator()} or an enhanced for loop:
 ** <pre>
 **   for (Pair member : jsonObject) {
 **     final String    name  = member.getKey();
 **     final JsonValue value = member.getValue();
 **     ...
 **   }
 ** </pre>
 ** Even though JSON objects are unordered by definition, instances of this
 ** class preserve the order of members to allow processing in document order
 ** and to guarantee a predictable output.
 ** <p>
 ** Note that this class is <strong>not thread-safe</strong>. If multiple
 ** threads access a <code>JsonObject</code> instance concurrently, while at
 ** least one of these threads modifies the contents of this object, access to
 ** the instance must be synchronized externally. Failure to do so may lead to
 ** an inconsistent state.
 ** <p>
 ** This class is <strong>not supposed to be extended</strong> by clients.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class JsonObject extends JsonValue implements Iterable<Pair<String, JsonValue>> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7664658679278261283")
  private static final long serialVersionUID = 1305090778443458644L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final     List<String>    names;
  private final     List<JsonValue> values;
  private transient HashIndex       table  = new HashIndex();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** A builder for creating {@link JsonObject} models from scratch.
   ** <br>
   ** This implementation initializes an empty JSON object model and provides
   ** methods to add name/value pairs to the object model and to return the
   ** resulting object. The methods in this class can be chained to add multiple
   ** name/value pairs to the object.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default connector <code>Builder</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Returns the JSON object associated with this object builder.
     ** <br>
     ** The iteration order for the {@link JsonObject} is based on the order in
     ** which name/value pairs are added to the object using this builder.
     **
     ** @return                  the JSON object that is being built.
     */
    public final JsonObject build() {
      return new JsonObject();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class HashIndex
  // ~~~~~ ~~~~~~~~~
  /**
   **
   */
  static class HashIndex {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // must be a power of two
    private final byte[] hashTable = new byte[32];

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>HashIndex</code>.
     */
    public HashIndex() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new <code>HashIndex</code> with the contents of the specified
     ** origin.
     ** <br>
     ** Copy Constructor
     **
     ** @param  original           the <code>HashIndex</code> to get the
     **                            initial contents from, must not be
     **                            <code>null</code>.
     */
    public HashIndex(final HashIndex origin) {
      System.arraycopy(origin.hashTable, 0, this.hashTable, 0, this.hashTable.length);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Adds the name at the specified slot in the index table..
     **
     ** @param  name             the name of the member to add, must not be
     **                          <code>null</code>.
     ** @param  index            the index of the hash element element to add.
     */
    void add(final String name, final int index) {
      int slot = hashSlotFor(name);
      if (index < 0xff) {
        // increment by 1, 0 stands for empty
        this.hashTable[slot] = (byte)(index + 1);
      }
      else {
        this.hashTable[slot] = 0;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove
    /**
     ** Removes the element at the specified slot from the index table..
     **
     ** @param  index            the index of the hash element element to
     **                          remove.
     */
    void remove(final int index) {
      for (int i = 0; i < this.hashTable.length; i++) {
        if (this.hashTable[i] == index + 1) {
          this.hashTable[i] = 0;
        }
        else if (this.hashTable[i] > index + 1) {
          this.hashTable[i]--;
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: get
    /**
     ** Returns the element for the specified position from this index table.
     **
     ** @param  name             the name of the member to return, must not be
     **                          <code>null</code>.
     **
     ** @return                  the index of the element for the specified
     **                          name.
     */
    int get(final Object name) {
      int slot = hashSlotFor(name);
      // subtract 1, 0 stands for empty
      return (this.hashTable[slot] & 0xff) - 1;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashSlotFor
    /**
     ** Returns the index of the specified element from this index table.
     **
     ** @param  element          the the member the index is to be returned for,
     **                          must not be <code>null</code>.
     **
     ** @return                  the index of the element for the specified
     **                          name.
     */
    private int hashSlotFor(final Object element) {
      return element.hashCode() & this.hashTable.length - 1;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JsonObject</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  JsonObject() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.names  = new ArrayList<String>();
    this.values = new ArrayList<JsonValue>();
    this.table  = new HashIndex();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>JsonObject</code> with the contents of the specified
   ** JSON object.
   ** <br>
   ** Copy Constructor
   **
   ** @param  object             the <code>JsonObject</code> to get the initial
   **                            contents from, must not be <code>null</code>.
   */
  JsonObject(final JsonObject object) {
    // ensure inheritance
    this(object, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>JsonObject</code> with the contents of the specified
   ** JSON array.
   ** <br>
   ** Copy Constructor
   **
   ** @param  object             the <code>JsonObject</code> to get the initial
   **                            contents from, must not be <code>null</code>.
   */
  private JsonObject(final JsonObject object, final boolean unmodifiable) {
    // prevent bogus input
    if (object == null)
      throw new NullPointerException("object is null");

    if (unmodifiable) {
      this.names  = CollectionUtility.unmodifiableList(object.names);
      this.values = CollectionUtility.unmodifiableList(object.values);
    }
    else {
      this.names  = CollectionUtility.list(object.names);
      this.values = CollectionUtility.list(object.values);
    }
    this.table  = new HashIndex();
    updateHashIndex();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of members (name/value pairs) in this object.
   **
   ** @return                    the number of members in this object.
   */
  public int size() {
    return this.names.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty
  /**
   ** Returns <code>true</code> if this object contains no members.
   **
   ** @return                    <code>true</code> if this object contains no
   **                            members.
   */
  public boolean isEmpty() {
    return this.names.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type (JsonValue)
  /**
   ** Returns the value type of this JSON value.
   **
   ** @return                    the JSON value type.
   */
  public final Type type() {
    return Type.OBJECT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Iterable)
  /**
   ** Returns an {@link Iterator} over the members of this object in document
   ** order.
   ** <br>
   ** The returned {@link Iterator} cannot be used to modify this object.
   **
   ** @return                    an {@link Iterator} over the members of this
   **                            object.
   */
  @Override
  public Iterator<Pair<String, JsonValue>> iterator() {
    final Iterator<String>    names  = this.names.iterator();
    final Iterator<JsonValue> values = this.values.iterator();
    return new Iterator<Pair<String, JsonValue>>() {
      @Override
      public boolean hasNext() {
        return names.hasNext();
      }
      @Override
      public Pair<String, JsonValue> next() {
        return Pair.of(names.next(), values.next());
      }
      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (JsonValue)
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
   */
  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + this.names.hashCode();
    result = 31 * result + this.values.hashCode();
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (JsonValue)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>JsonArray</code>s are considered equal if and only if they
   ** represent the same JSON text. As a consequence, two given
   ** <code>JsonArray</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (other == null)
      return false;

    if (this == other)
      return true;

    if (getClass() != other.getClass())
      return false;

    final JsonObject that = (JsonObject)other;
    return this.names.equals(that.names) && this.values.equals(that.values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (JsonValue)
  /**
   ** Returns the JSON string for JSON value in its minimal form, without any
   ** additional whitespace.
   ** <br>
   ** The result is guaranteed to be a valid input for the method
   ** {@link JsonParser#readObject()} and to create a value that is
   ** <em>equal</em> to this object.
   **
   ** @return                   a JSON string that represents this literal.
   */
  @Override
  public final String toString() {
    final StringWriter   output = new StringWriter();
    final JsonSerializer writer = new JsonSerializer(output);

    writer.writeStartObject();
    for (int i = 0; i < this.names.size(); i++) {
      writer.write(this.names.get(i), this.values.get(i));
    }
    writer.writeEnd();
    writer.close();
    return output.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiable
  /**
   ** Returns an unmodifiable wrapper for the specified <code>JsonObject</code>.
   ** <br>
   ** This method allows to provide read-only access to a
   ** <code>JsonObject</code>.
   ** <p>
   ** The returned <code>JsonObject</code> is backed by the given object and
   ** reflects subsequent changes. Attempts to modify the returned
   ** <code>JsonObject</code> result in an
   ** <code>UnsupportedOperationException</code>.
   **
   ** @param  object             the <code>JsonObject</code> for which an
   **                            unmodifiable <code>JsonObject</code> is to be
   **                            returned.
   **
   ** @return                    an unmodifiable view of the specified
   **                            <code>JsonObject</code>.
   */
  public static JsonObject unmodifiable(final JsonObject object) {
    return new JsonObject(object, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asObject
  /**
   ** Returns this JSON object as {@link JsonObject}, assuming that this object
   ** represents a JSON object.
   **
   ** @return                   a {@link JsonObject} for this object.
   */
  @Override
  public JsonObject asObject() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends a new member to the end of this object, with the specified name
   ** and the JSON representation of the specified <code>boolean</code> value.
   ** <p>
   ** This method <strong>does not prevent duplicate names</strong>. Calling
   ** this method with a name that already exists in the object will append
   ** another member with the same name. In order to replace existing members,
   ** use the method <code>set(name, value)</code> instead. However,
   ** <strong><em>add</em> is much faster than <em>set</em></strong> (because it
   ** does not need to search for existing members). Therefore <em>add</em>
   ** should be preferred when constructing new objects.
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject add(final String name, final boolean value) {
    add(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends a new member to the end of this object, with the specified name
   ** and the JSON representation of the specified <code>int</code> value.
   ** <p>
   ** This method <strong>does not prevent duplicate names</strong>. Calling
   ** this method with a name that already exists in the object will append
   ** another member with the same name. In order to replace existing members,
   ** use the method <code>set(name, value)</code> instead. However,
   ** <strong><em>add</em> is much faster than <em>set</em></strong> (because it
   ** does not need to search for existing members). Therefore <em>add</em>
   ** should be preferred when constructing new objects.
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject add(final String name, final int value) {
    add(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends a new member to the end of this object, with the specified name
   ** and the JSON representation of the specified <code>long</code> value.
   ** <p>
   ** This method <strong>does not prevent duplicate names</strong>. Calling
   ** this method with a name that already exists in the object will append
   ** another member with the same name. In order to replace existing members,
   ** use the method <code>set(name, value)</code> instead. However,
   ** <strong><em>add</em> is much faster than <em>set</em></strong> (because it
   ** does not need to search for existing members). Therefore <em>add</em>
   ** should be preferred when constructing new objects.
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject add(final String name, final long value) {
    add(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends a new member to the end of this object, with the specified name
   ** and the JSON representation of the specified <code>float</code> value.
   ** <p>
   ** This method <strong>does not prevent duplicate names</strong>. Calling
   ** this method with a name that already exists in the object will append
   ** another member with the same name. In order to replace existing members,
   ** use the method <code>set(name, value)</code> instead. However,
   ** <strong><em>add</em> is much faster than <em>set</em></strong> (because it
   ** does not need to search for existing members). Therefore <em>add</em>
   ** should be preferred when constructing new objects.
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add, must not be
   **                            <code>null</code>.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject add(final String name, final float value) {
    add(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends a new member to the end of this object, with the specified name
   ** and the JSON representation of the specified <code>double</code> value.
   ** <p>
   ** This method <strong>does not prevent duplicate names</strong>. Calling
   ** this method with a name that already exists in the object will append
   ** another member with the same name. In order to replace existing members,
   ** use the method <code>set(name, value)</code> instead. However,
   ** <strong><em>add</em> is much faster than <em>set</em></strong> (because it
   ** does not need to search for existing members). Therefore <em>add</em>
   ** should be preferred when constructing new objects.
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add, must not be
   **                            <code>null</code>.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject add(final String name, final double value) {
    add(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends a new member to the end of this object, with the specified name
   ** and the JSON representation of the specified string.
   ** <p>
   ** This method <strong>does not prevent duplicate names</strong>. Calling
   ** this method with a name that already exists in the object will append
   ** another member with the same name. In order to replace existing members,
   ** use the method <code>set(name, value)</code> instead. However,
   ** <strong><em>add</em> is much faster than <em>set</em></strong> (because it
   ** does not need to search for existing members). Therefore <em>add</em>
   ** should be preferred when constructing new objects.
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject add(final String name, final String value) {
    add(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends a new member to the end of this object, with the specified name
   ** and the JSON representation here the value will be a JsonArray which is
   ** produced from a Collection.
   ** <p>
   ** This method <strong>does not prevent duplicate names</strong>. Calling
   ** this method with a name that already exists in the object will append
   ** another member with the same name. In order to replace existing members,
   ** use the method <code>set(name, value)</code> instead. However,
   ** <strong><em>add</em> is much faster than <em>set</em></strong> (because it
   ** does not need to search for existing members). Therefore <em>add</em>
   ** should be preferred when constructing new objects.
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add, must not be
   **                            <code>null</code>.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject add(final String name, final Object value) {
    if (name == null)
      throw new NullPointerException("name is null");

    if (value == null)
      throw new NullPointerException("value is null");

    this.table.add(name, this.names.size());
    this.names.add(name);
    if (value instanceof Boolean) {
      this.values.add(valueOf((Boolean)value));
    }
    else if (value instanceof Integer) {
      this.values.add(valueOf((Integer)value));
    }
    else if (value instanceof Long) {
      this.values.add(valueOf((Long)value));
    }
    else if (value instanceof Double) {
      this.values.add(valueOf((Double)value));
    }
    else if (value instanceof Float) {
      this.values.add(valueOf((Float)value));
    }
    else if (value instanceof String) {
      this.values.add(valueOf((String)value));
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends a new member to the end of this object, with the specified name
   ** and the JSON representation of the specified JSON value.
   ** <p>
   ** This method <strong>does not prevent duplicate names</strong>. Calling
   ** this method with a name that already exists in the object will append
   ** another member with the same name. In order to replace existing members,
   ** use the method <code>set(name, value)</code> instead. However,
   ** <strong><em>add</em> is much faster than <em>set</em></strong> (because it
   ** does not need to search for existing members). Therefore <em>add</em>
   ** should be preferred when constructing new objects.
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add, must not be
   **                            <code>null</code>.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject add(final String name, final JsonValue value) {
    if (name == null)
      throw new NullPointerException("name is null");

    if (value == null)
      throw new NullPointerException("value is null");

    this.table.add(name, this.names.size());
    this.names.add(name);
    this.values.add(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the value of the member with the specified name to the JSON
   ** representation of the specified <code>boolean</code> value.
   ** <br>
   ** If this object does not contain a member with this name, a new member is
   ** added at the end of the object. If this object contains multiple members
   ** with this name, only the last one is changed.
   ** <p>
   ** This method should <strong>only be used to modify existing objects</strong>.
   ** To fill a new object with members, the method
   ** <code>add(name, value)</code> should be preferred which is much faster (as
   ** it does not need to search for existing members).
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject set(final String name, final boolean value) {
    set(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the value of the member with the specified name to the JSON
   ** representation of the specified <code>int</code> value.
   ** <br>
   ** If this object does not contain a member with this name, a new member is
   ** added at the end of the object. If this object contains multiple members
   ** with this name, only the last one is changed.
   ** <p>
   ** This method should <strong>only be used to modify existing objects</strong>.
   ** To fill a new object with members, the method
   ** <code>add(name, value)</code> should be preferred which is much faster (as
   ** it does not need to search for existing members).
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject set(final String name, final int value) {
    set(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the value of the member with the specified name to the JSON
   ** representation of the specified <code>long</code> value.
   ** <br>
   ** If this object does not contain a member with this name, a new member is
   ** added at the end of the object. If this object contains multiple members
   ** with this name, only the last one is changed.
   ** <p>
   ** This method should <strong>only be used to modify existing objects</strong>.
   ** To fill a new object with members, the method
   ** <code>add(name, value)</code> should be preferred which is much faster (as
   ** it does not need to search for existing members).
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject set(final String name, final long value) {
    set(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the value of the member with the specified name to the JSON
   ** representation of the specified <code>float</code> value.
   ** <br>
   ** If this object does not contain a member with this name, a new member is
   ** added at the end of the object. If this object contains multiple members
   ** with this name, only the last one is changed.
   ** <p>
   ** This method should <strong>only be used to modify existing objects</strong>.
   ** To fill a new object with members, the method
   ** <code>add(name, value)</code> should be preferred which is much faster (as
   ** it does not need to search for existing members).
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject set(final String name, final float value) {
    set(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the value of the member with the specified name to the JSON
   ** representation of the specified <code>double</code> value.
   ** <br>
   ** If this object does not contain a member with this name, a new member is
   ** added at the end of the object. If this object contains multiple members
   ** with this name, only the last one is changed.
   ** <p>
   ** This method should <strong>only be used to modify existing objects</strong>.
   ** To fill a new object with members, the method
   ** <code>add(name, value)</code> should be preferred which is much faster (as
   ** it does not need to search for existing members).
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject set(final String name, final double value) {
    set(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the value of the member with the specified name to the JSON
   ** representation of the specified string.
   ** <br>
   ** If this object does not contain a member with this name, a new member is
   ** added at the end of the object. If this object contains multiple members
   ** with this name, only the last one is changed.
   ** <p>
   ** This method should <strong>only be used to modify existing objects</strong>.
   ** To fill a new object with members, the method
   ** <code>add(name, value)</code> should be preferred which is much faster (as
   ** it does not need to search for existing members).
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject set(final String name, final String value) {
    set(name, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the value of the member with the specified name to the JSON
   ** representation of the specified JSON value.
   ** <br>
   ** If this object does not contain a member with this name, a new member is
   ** added at the end of the object. If this object contains multiple members
   ** with this name, only the last one is changed.
   ** <p>
   ** This method should <strong>only be used to modify existing objects</strong>.
   ** To fill a new object with members, the method
   ** <code>add(name, value)</code> should be preferred which is much faster (as
   ** it does not need to search for existing members).
   **
   ** @param  name               the name of the member to add, must not be
   **                            <code>null</code>.
   ** @param  value              the value of the member to add, must not be
   **                            <code>null</code>.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject set(final String name, final JsonValue value) {
    if (name == null)
      throw new NullPointerException("name is null");

    if (value == null)
      throw new NullPointerException("value is null");

    int index = indexOf(name);
    if (index != -1) {
      this.values.set(index, value);
    }
    else {
      this.table.add(name, names.size());
      this.names.add(name);
      this.values.add(value);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBoolean
  /**
   ** Returns the <code>boolean</code> value of the member with the specified
   ** name in this object.
   ** <br>
   ** If this object does not contain a member with this name, the given default
   ** value is returned. If this object contains multiple members with the given
   ** name, the last one will be picked. If this member's value does not
   ** represent a JSON <code>true</code> or <code>false</code> value, an
   ** exception is thrown.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   ** @param  defaultValue       the value to be returned if the requested
   **                            member is missing.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or the given default value if this object
   **                            does not contain a member with that name.
   */
  public boolean getBoolean(final String name, final boolean defaultValue) {
    final JsonLiteral value = (JsonLiteral)get(name);
    return value != null ? value.type() == JsonValue.Type.TRUE ? true : false : defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInt
  /**
   ** Returns the <code>int</code> value of the member with the specified
   ** name in this object.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or the given default value if this object
   **                            does not contain a member with that name.
   **
   ** @throws ClassCastException if the value to which the specified name is
   **                            mapped is not assignable to {@link JsonNumber}
   **                            type.
   */
  public int getInt(final String name) {
    final JsonNumber value = getJsonNumber(name);
    return value != null ? value.intValue() : -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInt
  /**
   ** Returns the <code>int</code> value of the member with the specified
   ** name in this object.
   ** <br>
   ** If this object does not contain a member with this name, the given default
   ** value is returned. If this object contains multiple members with the given
   ** name, the last one will be picked. If this member's value does not
   ** represent a JSON number or if it cannot be interpreted as Java
   ** <code>int</code>, an exception is thrown.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   ** @param  defaultValue       the value to be returned if the requested
   **                            member is missing.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or the given default value if this object
   **                            does not contain a member with that name.
   **
   ** @throws ClassCastException if the value to which the specified name is
   **                            mapped is not assignable to {@link JsonNumber}
   **                            type.
   */
  public int getInt(final String name, final int defaultValue) {
    final JsonNumber value = getJsonNumber(name);
    return value != null ? value.intValue() : defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLong
  /**
   ** Returns the <code>long</code> value of the member with the specified
   ** name in this object.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or the given default value if this object
   **                            does not contain a member with that name.
   **
   ** @throws ClassCastException if the value to which the specified name is
   **                            mapped is not assignable to {@link JsonNumber}
   **                            type.
   */
  public long getLong(final String name) {
    final JsonNumber value = getJsonNumber(name);
    return value.longValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLong
  /**
   ** Returns the <code>long</code> value of the member with the specified
   ** name in this object.
   ** <br>
   ** If this object does not contain a member with this name, the given default
   ** value is returned. If this object contains multiple members with the given
   ** name, the last one will be picked. If this member's value does not
   ** represent a JSON JSON number or if it cannot be interpreted as Java
   ** <code>long</code>, an exception is thrown.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   ** @param  defaultValue       the value to be returned if the requested
   **                            member is missing.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or the given default value if this object
   **                            does not contain a member with that name.
   **
   ** @throws ClassCastException if the value to which the specified name is
   **                            mapped is not assignable to {@link JsonNumber}
   **                            type.
   */
  public long getLong(final String name, final long defaultValue) {
    final JsonNumber value = getJsonNumber(name);
    return value != null ? value.longValue() : defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFloat
  /**
   ** Returns the <code>float</code> value of the member with the specified
   ** name in this object.
   ** <br>
   ** If this object does not contain a member with this name, the given default
   ** value is returned. If this object contains multiple members with the given
   ** name, the last one will be picked. If this member's value does not
   ** represent a JSON number or if it cannot be interpreted as Java
   ** <code>float</code>, an exception is thrown.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   ** @param  defaultValue       the value to be returned if the requested
   **                            member is missing.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or the given default value if this object
   **                            does not contain a member with that name.
   **
   ** @throws ClassCastException if the value to which the specified name is
   **                            mapped is not assignable to {@link JsonNumber}
   **                            type.
   */
  public float getFloat(final String name, final float defaultValue) {
    final JsonNumber value = getJsonNumber(name);
    return value != null ? value.floatValue() : defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDouble
  /**
   ** Returns the <code>double</code> value of the member with the specified
   ** name in this object.
   ** <br>
   ** If this object does not contain a member with this name, the given default
   ** value is returned. If this object contains multiple members with the given
   ** name, the last one will be picked. If this member's value does not
   ** represent a JSON number or if it cannot be interpreted as Java
   ** <code>double</code>, an exception is thrown.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   ** @param  defaultValue       the value to be returned if the requested
   **                            member is missing.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or the given default value if this object
   **                            does not contain a member with that name.
   **
   ** @throws ClassCastException if the value to which the specified name is
   **                            mapped is not assignable to {@link JsonNumber}
   **                            type.
   */
  public double getDouble(final String name, final double defaultValue) {
    final JsonNumber value = getJsonNumber(name);
    return value != null ? value.doubleValue() : defaultValue;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getString
  /**
   ** Returns the <code>String</code> value of the member with the specified
   ** name in this object.
   ** <br>
   ** If this object does not contain a member with this name, the given default
   ** value is returned. If this object contains multiple members with the given
   ** name, the last one will be picked. If this member's value does not
   ** represent a JSON string, an exception is thrown.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or the given default value if this object
   **                            does not contain a member with that name.
   */
  public String getString(final String name) {
    final JsonString value = (JsonString)get(name);
    return value != null ? value.toString() : JsonLiteral.NULL.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getString
  /**
   ** Returns the <code>String</code> value of the member with the specified
   ** name in this object.
   ** <br>
   ** If this object does not contain a member with this name, the given default
   ** value is returned. If this object contains multiple members with the given
   ** name, the last one will be picked. If this member's value does not
   ** represent a JSON string, an exception is thrown.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   ** @param  defaultValue       the value to be returned if the requested
   **                            member is missing.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or the given default value if this object
   **                            does not contain a member with that name.
   */
  public String getString(final String name, final String defaultValue) {
    final JsonString value = (JsonString)get(name);
    return value != null ? value.toString() : defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getJsonNumber
  /**
   ** Returns the number value to which the specified name is mapped.
   ** <br>
   ** This is a convenience method for <code>(JsonNumber)get(name)}</code> to
   ** get the value.
   **
   ** @param  name               the name whose associated value is to be
   **                            returned.
   **
   ** @return                    the number value to which the specified name is
   **                            mapped, or <code>null</code> if this object
   **                            contains no mapping for the name,
   **
   ** @throws ClassCastException if the value to which the specified name is
   **                            mapped is not assignable to {@link JsonNumber}
   **                            type.
   */
  public JsonNumber getJsonNumber(final String name) {
    return (JsonNumber)get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Returns the value of the member with the specified name in this object.
   ** If this object contains multiple members with the given name, this method
   ** will return the last one.
   **
   ** @param  name               the name of the member whose value is to be
   **                            returned, must not be <code>null</code>.
   **
   ** @return                    the value of the last member with the specified
   **                            name, or <code>null</code> if this object does
   **                            not contain a member with that name.
   */
  public JsonValue get(final String name) {
    if (name == null)
      throw new NullPointerException("name is null");

    int index = indexOf(name);
    return index != -1 ? this.values.get(index) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes a member with the specified name from this object.
   ** <br>
   ** If this object contains multiple members with the given name, only the
   ** last one is removed. If this object does not contain a member with the
   ** specified name, the object is not modified.
   **
   ** @param  name               the name of the member to remove, must not be
   **                            <code>null</code>.
   **
   ** @return                    the object itself, to enable method chaining.
   */
  public JsonObject remove(final String name) {
    if (name == null)
      throw new NullPointerException("name is null");

    int index = indexOf(name);
    if (index != -1) {
      this.table.remove(index);
      this.names.remove(index);
      this.values.remove(index);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   names
  /**
   ** Returns a list of the names in this object in document order.
   ** <br>
   ** The returned list is backed by this object and will reflect subsequent
   ** changes. It cannot be used to modify this object.
   ** <br>
   ** Attempts to modify the returned list will result in an exception.
   **
   ** @return                    a list of the names in this object.
   */
  public List<String> names() {
    return CollectionUtility.unmodifiableList(this.names);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexOf
  /**
   ** Returns the index position from the index table for the specified name.
   **
   ** @param  name               the name of the member the index have to be
   **                            returned, must not be
   **                            <code>null</code>.
   **
   ** @return                    the index of the element for the specified
   **                            name.
   */
  int indexOf(final String name) {
    int index = this.table.get(name);
    if (index != -1 && name.equals(this.names.get(index)))
      return index;

    return this.names.lastIndexOf(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateHashIndex
  private void updateHashIndex() {
    int size = this.names.size();
    for (int i = 0; i < size; i++) {
      this.table.add(this.names.get(i), i);
    }
  }
}