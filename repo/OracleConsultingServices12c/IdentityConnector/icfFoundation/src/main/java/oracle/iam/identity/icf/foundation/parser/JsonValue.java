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

    File        :   JsonValue.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonValue.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.parser;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class JsonValue
// ~~~~~ ~~~~~~~~~
/**
 ** Represents represents an immutable JSON value.
 ** <br>
 ** This can be a JSON <strong>object</strong>, an <strong>array</strong>, a
 ** <strong>number</strong>, a <strong>string</strong>, or one of the literals
 ** <strong>true</strong>, <strong>false</strong>, and <strong>null</strong>.
 ** <p>
 ** The literals <strong>true</strong>, <strong>false</strong>, and
 ** <strong>null</strong> are represented by the constants
 ** {@link JsonLiteral#TRUE}, {@link JsonLiteral#FALSE}, and
 ** {@link JsonLiteral#NULL}.
 ** <p>
 ** JSON <strong>objects</strong> and <strong>arrays</strong> are represented by
 ** the subtypes {@link JsonObject} and {@link JsonArray}. Instances of these
 ** types can be created using the public constructors of these classes.
 ** <p>
 ** Instances that represent JSON <strong>numbers</strong>,
 ** <strong>strings</strong> and <strong>boolean</strong> values can be created
 ** using the static factory methods {@link #valueOf(String)},
 ** {@link #valueOf(long)}, {@link #valueOf(double)}, etc.
 ** <p>
 ** In order to find out whether an instance of this class is of a certain type,
 ** the methods  {@link #type()} can be used.
 ** <p>
 ** This class is <strong>not supposed to be extended</strong> by clients.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class JsonValue implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-488789241542183752")
  private static final long serialVersionUID = 4212879438989392463L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** Indicates the type of a <code>JsonValue</code> object.
   */
  enum Type {
      /** JSON null */
      NULL
      /** JSON true */
    , TRUE
      /** JSON false */
    , FALSE
      /** JSON string */
    , STRING
      /** JSON number */
    , NUMBER
      /** JSON array */
    , ARRAY
      /** JSON object */
    , OBJECT
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JsonValue</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  JsonValue() {
    // prevent subclasses outside of this package
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the value type of this JSON value.
   **
   ** @return                    the JSON value type.
   */
  public abstract Type type();

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Returns a <code>JsonValue</code> instance that represents the given
   ** <code>boolean</code> value.
   **
   ** @param  value              the value to get a JSON representation for.
   **
   ** @return                    a JSON value that represents the given value.
   */
  public static JsonValue valueOf(final boolean value) {
    return value ? JsonLiteral.TRUE : JsonLiteral.FALSE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Returns a <code>JsonValue</code> instance that represents the given
   ** <code>int</code> value.
   **
   ** @param  value              the value to get a JSON representation for.
   **
   ** @return                    a JSON value that represents the given value.
   */
  public static JsonValue valueOf(final int value) {
    return new JsonInteger(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Returns a <code>JsonValue</code> instance that represents the given
   ** <code>long</code> value.
   **
   ** @param  value              the value to get a JSON representation for.
   **
   ** @return                    a JSON value that represents the given value.
   */
  public static JsonValue valueOf(final long value) {
    return new JsonLong(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Returns a <code>JsonValue</code> instance that represents the given
   ** <code>double</code> value.
   **
   ** @param  value              the value to get a JSON representation for.
   **
   ** @return                    a JSON value that represents the given value.
   */
  public static JsonValue valueOf(final double value) {
    if (Double.isInfinite(value) || Double.isNaN(value))
      throw new IllegalArgumentException("Infinite and NaN values not permitted in JSON");

    return new JsonDouble(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Returns a <code>JsonValue</code> instance that represents the given
   ** <code>float</code> value.
   **
   ** @param  value              the value to get a JSON representation for.
   **
   ** @return                    a JSON value that represents the given value.
   */
  public static JsonValue valueOf(final float value) {
    if (Float.isInfinite(value) || Float.isNaN(value))
      throw new IllegalArgumentException("Infinite and NaN values not permitted in JSON");

    return new JsonFloat(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Returns a <code>JsonValue</code> instance that represents the given
   ** string.
   **
   ** @param  string              the string to get a JSON representation for.
   **
   ** @return                    a JSON value that represents the given value.
   */
  public static JsonValue valueOf(final String string) {
    return string == null ? JsonLiteral.NULL : new JsonString(string);
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
   */
  @Override
  public abstract int hashCode();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>JsonNumber</code>s are considered equal if and only if they
   ** represent the same JSON text. As a consequence, two given
   ** <code>JsonNumber</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  object             the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public abstract boolean equals(final Object object);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the JSON string for JSON value in its minimal form, without any
   ** additional whitespace.
   ** <br>
   ** The result is guaranteed to be a valid input for the method
   ** {@link JsonParser#readValue()} and to create a value that is
   ** <em>equal</em> to this object.
   **
   ** @return                   a JSON string that represents this literal.
   */
  @Override
  public abstract String toString();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asArray
  /**
   ** Returns this JSON value as {@link JsonArray}, assuming that this value
   ** represents a JSON array.
   ** <br>
   ** If this is not the case, an exception is thrown.
   **
   ** @return                   a {@link JsonArray} for this value.
   **
   ** @throws UnsupportedOperationException if this value is not a JSON array.
   */
  public JsonArray asArray() {
    throw new UnsupportedOperationException("Not an array: " + toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asObject
  /**
   ** Returns this JSON value as {@link JsonObject}, assuming that this value
   ** represents a JSON object.
   ** <br>
   ** If this is not the case, an exception is thrown.
   **
   ** @return                   a {@link JsonObject} for this value.
   **
   ** @throws UnsupportedOperationException if this value is not a JSON object.
   */
  public JsonObject asObject() {
    throw new UnsupportedOperationException("Not an object: " + toString());
  }
}