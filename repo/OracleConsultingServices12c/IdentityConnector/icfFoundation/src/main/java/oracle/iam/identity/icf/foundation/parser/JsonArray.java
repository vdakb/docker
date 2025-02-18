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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   JsonArray.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonArray.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.parser;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

import java.io.StringWriter;

////////////////////////////////////////////////////////////////////////////////
// class JsonArray
// ~~~~~ ~~~~~~~~~
/**
 ** Represents a JSON array, an ordered collection of JSON values.
 ** <p>
 ** Elements can be added using the <code>add(...)</code> methods which accept
 ** instances of {@link JsonValue}, strings, primitive numbers, and boolean
 ** values. To replace an element of an array, use the
 ** <code>set(int, ...)</code> methods.
 ** <p>
 ** Elements can be accessed by their index using {@link #get(int)}. This class
 ** also supports iterating over the elements in document order using an
 ** {@link #iterator()} or an enhanced for loop:
 ** <pre>
 **   for( JsonValue value : jsonArray ) {
 **     ...
 **   }
 ** </pre>
 ** An equivalent {@link List} can be obtained from the method
 ** {@link #values()}.
 ** <p>
 ** Note that this class is <strong>not thread-safe</strong>. If multiple
 ** threads access a <code>JsonArray</code> instance concurrently, while at
 ** least one of these threads modifies the contents of this array, access to
 ** the instance must be synchronized externally. Failure to do so may lead to
 ** an inconsistent state.
 ** <p>
 ** This class is <strong>not supposed to be extended</strong> by clients.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JsonArray extends JsonValue implements Iterable<JsonValue> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6838158677116367067")
  private static final long serialVersionUID = -4526571598992185346L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<JsonValue> values;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** A builder for creating {@link JsonArray} models from scratch. This
   ** implementation initializes an empty JSON array model and provides methods
   ** to add values to the array model and to return the resulting array. The
   ** methods in this class can be chained to add multiple values to the array.
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
     ** Returns the current array.
     **
     ** @return                  the current JSON array.
     */
    public final JsonArray build() {
      return new JsonArray();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JsonArray</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  JsonArray() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.values = new ArrayList<JsonValue>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>JsonArray</code> with the contents of the specified
   ** JSON array.
   ** <br>
   ** Copy Constructor
   **
   ** @param array               the <code>JsonArray</code> to get the initial
   **                            contents from, must not be <code>null</code>.
   */
  JsonArray(final JsonArray array) {
    // ensure inheritance
    this(array, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>JsonArray</code> with the contents of the specified
   ** JSON array.
   **
   ** @param  array              the <code>JsonArray</code> to get the initial
   **                            contents from, must not be <code>null</code>.
   */
  private JsonArray(final JsonArray array, final boolean unmodifiable) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (array == null)
      throw new NullPointerException("array must not be null");

    // initialize instance attributes
    if (unmodifiable)
      this.values = Collections.unmodifiableList(array.values);
    else
      this.values = new ArrayList<JsonValue>(array.values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of elements in this array.
   **
   ** @return                    the array itself, to enable method chaining.
   */
  public int size() {
    return this.values.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty
  /**
   ** Returns <code>true</code> if this array contains no elements.
   **
   ** @return                    the array itself, to enable method chaining.
   */
  public boolean isEmpty() {
    return this.values.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Returns the value of the element at the specified position in this array.
   **
   ** @param  index              the index of the array element to return.
   **
   ** @return                    the value of the element at the specified
   **                            position.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range, i.e.
   **                                   <code>index &lt; 0</code> or
   **                                   <code>index &gt;= size</code>.
   */
  public JsonValue get(int index) {
    return this.values.get(index);
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
    return Type.ARRAY;
  }

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
    return this.values.hashCode();
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

    final JsonArray that = (JsonArray)other;
    return this.values.equals(that.values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (JsonValue)
  /**
   ** Returns the JSON string for JSON value in its minimal form, without any
   ** additional whitespace.
   ** <br>
   ** The result is guaranteed to be a valid input for the method
   ** {@link JsonParser#readArray()} and to create a value that is
   ** <em>equal</em> to this object.
   **
   ** @return                   a JSON string that represents this literal.
   */
  @Override
  public final String toString() {
    final StringWriter   output = new StringWriter();
    final JsonSerializer writer = new JsonSerializer(output);

    writer.writeStartArray();
    for(JsonValue cursor : this.values) {
      writer.write(cursor);
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
   ** Returns an unmodifiable wrapper for the specified <code>JsonArray</code>.
   ** <br>
   ** This method allows to provide read-only access to a
   ** <code>JsonArray</code>.
   ** <p>
   ** The returned <code>JsonArray</code> is backed by the given array and
   ** reflects subsequent changes. Attempts to modify the returned
   ** <code>JsonArray</code> result in an
   ** <code>UnsupportedOperationException</code>.
   **
   ** @param  array              the <code>JsonArray</code> for which an
   **                            unmodifiable <code>JsonArray</code> is to be
   **                            returned.
   **
   ** @return                    an unmodifiable view of the specified
   **                            <code>JsonArray</code>.
   */
  public static JsonArray unmodifiable(final JsonArray array) {
    return new JsonArray(array, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asArray
  /**
   ** Returns this JSON array as {@link JsonArray}, assuming that this array
   ** represents a JSON array.
   **
   ** @return                   a {@link JsonArray} for this value.
   */
  @Override
  public JsonArray asArray() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends the JSON representation of the specified <code>boolean</code>
   ** value to the end of this array.
   **
   ** @param  value              the value to add to the array.
   **
   ** @return                    the array itself, to enable method chaining.
   */
  public JsonArray add(final boolean value) {
    this.values.add(valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends the JSON representation of the specified <code>int</code> value to
   ** the end of this array.
   **
   ** @param  value              the value to add to the array.
   **
   ** @return                    the array itself, to enable method chaining.
   */
  public JsonArray add(final int value) {
    this.values.add(valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends the JSON representation of the specified <code>long</code> value
   ** to the end of this array.
   **
   ** @param  value              the value to add to the array.
   **
   ** @return                    the array itself, to enable method chaining.
   */
  public JsonArray add(final long value) {
    this.values.add(valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends the JSON representation of the specified <code>float</code> value
   ** to the end of this array.
   **
   ** @param  value              the value to add to the array.
   **
   ** @return                    the array itself, to enable method chaining.
   */
  public JsonArray add(final float value) {
    this.values.add(valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends the JSON representation of the specified <code>double</code> value
   ** to the end of this array.
   **
   ** @param  value              the value to add to the array.
   **
   ** @return                    the array itself, to enable method chaining.
   */
  public JsonArray add(final double value) {
    this.values.add(valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends the JSON representation of the specified string to the end of this
   ** array.
   **
   ** @param  value              the value to add to the array.
   **
   ** @return                    the array itself, to enable method chaining.
   */
  public JsonArray add(final String value) {
    this.values.add(valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Appends the specified JSON value to the end of this array.
   **
   ** @param  value              the {@link JsonValue} to add to the array, must
   **                            not be <code>null</code>.
   **
   ** @return                    the array itself, to enable method chaining.
   */
  public JsonArray add(final JsonValue value) {
    if (value == null)
      throw new NullPointerException("value must not be null");

    this.values.add(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Replaces the element at the specified position in this array with the JSON
   ** representation of the specified <code>boolean</code> value.
   **
   ** @param  index              the index of the array element to replace.
   ** @param  value              the value to be stored at the specified array
   **                            position.
   **
   ** @return                    the array itself, to enable method chaining.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range, i.e.
   **                                   <code>index &lt; 0</code> or
   **                                   <code>index &gt;= size</code>.
   */
  public JsonArray set(final int index, final boolean value) {
    this.values.set(index, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Replaces the element at the specified position in this array with the JSON
   ** representation of the specified <code>int</code> value.
   **
   ** @param  index              the index of the array element to replace.
   ** @param  value              the value to be stored at the specified array
   **                            position.
   **
   ** @return                    the array itself, to enable method chaining.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range, i.e.
   **                                   <code>index &lt; 0</code> or
   **                                   <code>index &gt;= size</code>.
   */
  public JsonArray set(final int index, final int value) {
    this.values.set(index, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Replaces the element at the specified position in this array with the JSON
   ** representation of the specified <code>long</code> value.
   **
   ** @param  index              the index of the array element to replace.
   ** @param  value              the value to be stored at the specified array
   **                            position.
   **
   ** @return                    the array itself, to enable method chaining.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range, i.e.
   **                                   <code>index &lt; 0</code> or
   **                                   <code>index &gt;= size</code>.
   */
  public JsonArray set(final int index, final long value) {
    this.values.set(index, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Replaces the element at the specified position in this array with the JSON
   ** representation of the specified <code>float</code> value.
   **
   ** @param  index              the index of the array element to replace.
   ** @param  value              the value to be stored at the specified array
   **                            position.
   **
   ** @return                    the array itself, to enable method chaining.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range, i.e.
   **                                   <code>index &lt; 0</code> or
   **                                   <code>index &gt;= size</code>.
   */
  public JsonArray set(final int index, final float value) {
    this.values.set(index, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Replaces the element at the specified position in this array with the JSON
   ** representation of the specified <code>double</code> value.
   **
   ** @param  index              the index of the array element to replace.
   ** @param  value              the value to be stored at the specified array
   **                            position.
   **
   ** @return                    the array itself, to enable method chaining.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range, i.e.
   **                                   <code>index &lt; 0</code> or
   **                                   <code>index &gt;= size</code>.
   */
  public JsonArray set(final int index, final double value) {
    this.values.set(index, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Replaces the element at the specified position in this array with the JSON
   ** representation of the specified string.
   **
   ** @param  index              the index of the array element to replace.
   ** @param  value              the value to be stored at the specified array
   **                            position.
   **
   ** @return                    the array itself, to enable method chaining.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range, i.e.
   **                                   <code>index &lt; 0</code> or
   **                                   <code>index &gt;= size</code>.
   */
  public JsonArray set(final int index, final String value) {
    this.values.set(index, valueOf(value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Replaces the element at the specified position in this array with the JSON
   ** representation of the specified {@link JsonValue}.
   **
   ** @param  index              the index of the array element to replace.
   ** @param  value              the {@link JsonValue} to be stored at the
   **                            specified array position, must not be
   **                            <code>null</code>
   **
   ** @return                    the array itself, to enable method chaining.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range, i.e.
   **                                   <code>index &lt; 0</code> or
   **                                   <code>index &gt;= size</code>.
   */
  public JsonArray set(final int index, final JsonValue value) {
    if (value == null)
      throw new NullPointerException("value must not be null");

    this.values.set(index, value);
    return this;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes the element at the specified index from this array.
   **
   ** @param  index              the index of the element to remove
   **
   ** @return                    the array itself, to enable method chaining.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range, i.e.
   **                                   <code>index &lt; 0</code> or
   **                                   <code>index &gt;= size</code>.
   */
  public JsonArray remove(int index) {
    this.values.remove(index);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values
  /**
   ** Returns a list of the values in this array in document order.
   ** <br>
   ** The returned list is backed by this array and will reflect subsequent
   ** changes. It cannot be used to modify this array.
   ** <br>
   ** Attempts to modify the returned list will result in an exception.
   **
   ** @return                    a list of the values in this array.
   */
  public List<JsonValue> values() {
    return Collections.unmodifiableList(this.values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator
  /**
   ** Returns an {@link Iterator} over the values of this array in document
   ** order.
   ** <br>
   ** The returned {@link Iterator} cannot be used to modify this array.
   **
   ** @return                    an {@link Iterator} over the values of this
   **                            array.
   */
  public Iterator<JsonValue> iterator() {
    final Iterator<JsonValue> iterator = this.values.iterator();
    return new Iterator<JsonValue>() {
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }
      @Override
      public JsonValue next() {
        return iterator.next();
      }
      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}