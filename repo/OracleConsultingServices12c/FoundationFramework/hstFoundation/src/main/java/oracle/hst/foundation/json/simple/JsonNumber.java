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
    Subsystem   :   Minimalistic JSON Parser

    File        :   JsonNumber.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonNumber.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.json.simple;

import java.math.BigInteger;
import java.math.BigDecimal;

////////////////////////////////////////////////////////////////////////////////
// class JsonNumber
// ~~~~~ ~~~~~~~~~~
/**
 ** An immutable JSON number value.
 ** <p>
 ** Implementations may use a {@link BigDecimal} object to store the numeric
 ** value internally.
 ** <p>
 ** The <code>BigDecimal</code> object can be constructed from the following
 ** types:
 ** <ul>
 **   <li>{@link BigDecimal#BigDecimal(int) int}
 **   <li>{@link BigDecimal#BigDecimal(long) long}
 **   <li>{@link BigDecimal#valueOf(double) double}
 **   <li>{@link BigDecimal#BigDecimal(BigInteger) BigInteger}
 **   <li>{@link BigDecimal#BigDecimal(String) String}
 ** </ul>
 ** Some of the method semantics in this class are defined using the
 ** {@link BigDecimal} semantics.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class JsonNumber extends JsonValue {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1295174098942793704")
  private static final long serialVersionUID = 7827949514355356069L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

	protected BigDecimal decimal;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>JsonNumber</code> with the contents of the specified
   ** String value.
   */
  JsonNumber() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JsonNumber</code> that wrappes the specified
   ** {@link BigDecimal} <code>value</code>.
   **
   ** @param  value              the {@link BigDecimal} to get the initial
   **                            contents from, must not be <code>null</code>.
   **                            <br>
   **                            Allowed object {@link BigDecimal}.
   */
  protected JsonNumber(final BigDecimal value) {
    // ensure inheritance
    super();

    // initialize instance attribute
    this.decimal = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigInteger (JsonNumber)
  /**
   ** Returns this JSON number as a {@link BigInteger} object.
   ** <br>
   ** This is a convenience method for
   ** <code>bigDecimalValue().toBigInteger()</code>.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This conversion can lose information about the overall magnitude and
   ** precision of the number value as well as return a result with the
   ** opposite sign.
   **
   ** @return                    an {@link BigInteger} representation of the
   **                            JSON number.
   **                            <br>
   **                            Possible object {@link BigInteger}.
   **
   ** @see    java.math.BigDecimal#toBigInteger()
   */
  public final BigInteger bigInteger() {
    return bigDecimalValue().toBigInteger ();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigIntegerExact (JsonNumber)
  /**
   ** Returns this JSON number as a {@link BigInteger} object.
   ** <br>
   ** This is a convenience method for
   ** <code>bigDecimalValue().toBigIntegerExact()</code>.
   **
   ** @return                    an {@link BigInteger} representation of the
   **                            JSON number.
   **                            <br>
   **                            Possible object {@link BigInteger}.
   */
  public final BigInteger bigIntegerExact() {
    return bigDecimalValue().toBigIntegerExact();
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
   **                            <br>
   **                            Possible object {@link Type}.
   */
  public final Type type() {
    return Type.NUMBER;
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return bigDecimalValue().hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Factory method to create an new <code>JsonNumeric</code> with the
   ** specified <code>int</code> value.
   **
   ** @param  value              a <code>int</code> value.
   **
   ** @return                    an instance of <code>JsonValue</code>
   **                            with the specified <code>int</code> value.
   **                            <br>
   **                            Possible object is {@link JsonValue}.
   */
  public static JsonValue valueOf(final int value) {
    return new JsonInteger(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Factory method to create an new <code>JsonNumeric</code> with the
   ** specified <code>long</code> value.
   **
   ** @param  value              a <code>long</code> value.
   **
   ** @return                    an instance of <code>JsonValue</code>
   **                            with the specified <code>long</code> value.
   **                            <br>
   **                            Possible object {@link JsonValue}.
   */
  public static JsonValue valueOf(final long value) {
    return new JsonLong(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Factory method to create an new <code>JsonNumeric</code> with the
   ** specified <code>double</code> value.
   **
   ** @param  value              a <code>double</code> value.
   **
   ** @return                    an instance of <code>JsonValue</code>
   **                            with the specified <code>double</code> value.
   **                            <br>
   **                            Possible object {@link JsonValue}.
   */
  public static JsonValue valueOf(final double value) {
    return new JsonDouble(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Factory method to create an new <code>JsonNumeric</code> with the
   ** specified <code>float</code> value.
   **
   ** @param  value              a <code>float</code> value.
   **
   ** @return                    an instance of <code>JsonValue</code>
   **                            with the specified <code>float</code> value.
   **                            <br>
   **                            Possible object {@link JsonValue}.
   */
  public static JsonValue valueOf(final float value) {
    return new JsonFloat(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Factory method to create an new <code>JsonNumeric</code> with the
   ** specified {@link BigInteger} value.
   **
   ** @param  value              a {@link BigInteger} value.
   **
   ** @return                    an instance of <code>JsonValue</code>
   **                            with the specified <code>double</code> value.
   **                            <br>
   **                            Possible object {@link JsonValue}.
   */
  public static JsonValue valueOf(final BigInteger value) {
    return new JsonBigInteger(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Factory method to create an new <code>JsonNumeric</code> with the
   ** specified {@link BigDecimal} value.
   **
   ** @param  value              a {@link BigDecimal} value.
   **
   ** @return                    an instance of <code>JsonValue</code>
   **                            with the specified <code>double</code> value.
   **                            <br>
   **                            Possible object {@link JsonValue}.
   */
  public static JsonValue valueOf(final BigDecimal value) {
    return new JsonBigDecimal(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Factory method to create an new <code>JsonNumeric</code> with the
   ** specified <code>String</code> value.
   **
   ** @param  value              <code>String</code> value.
   **
   ** @return                    an instance of <code>JsonValue</code>
   **                            with the specified <code>String</code> value.
   **                            <br>
   **                            Possible object {@link JsonValue}.
   */
  public static JsonValue valueOf(final String value) {
    return new JsonDouble(Double.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intValue
  /**
   ** Returns this JSON number as an <code>int</code> value, assuming that this
   ** value represents a JSON number that can be interpreted as Java
   ** <code>int</code>.
   ** <br>
   ** If this is not the case, an exception is thrown.
   ** <p>
   ** To be interpreted as Java <code>int</code>, the JSON number must neither
   ** contain an exponent nor a fraction part. Moreover, the number must be in
   ** the <code>Integer</code> range.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This conversion can lose information about the overall magnitude and
   ** precision of the number value as well as return a result with the
   ** opposite sign.
   **
   ** @return                    an <code>int</code> representation of the JSON
   **                            number.
   **                            <br>
   **                            Possible object <code>int</code>.
   **
   ** @throws UnsupportedOperationException if this number is not a JSON number
   ** @throws NumberFormatException         if this JSON number can not be
   **                                       interpreted as <code>int</code>
   **                                       value.
   */
  public abstract int intValue();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intValueExact
  /**
   ** Returns this JSON number as an <code>int</code> value, assuming that this
   ** value represents a JSON number that can be interpreted as Java
   ** <code>int</code>.
   ** <br>
   ** If this is not the case, an exception is thrown.
   ** <p>
   ** To be interpreted as Java <code>int</code>, the JSON number must neither
   ** contain an exponent nor a fraction part. Moreover, the number must be in
   ** the <code>Integer</code> range.
   **
   ** @return                    an <code>int</code> representation of the JSON
   **                            number.
   **                            <br>
   **                            Possible object <code>int</code>.
   **
   ** @throws UnsupportedOperationException if this number is not a JSON number
   ** @throws NumberFormatException         if this JSON number can not be
   **                                       interpreted as <code>int</code>
   **                                       value.
   */
  public abstract int intValueExact();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns this JSON number as a <code>long</code> value, assuming that this
   ** value represents a JSON number that can be interpreted as Java
   ** <code>long</code>.
   ** <br>
   ** If this is not the case, an exception is thrown.
   ** <p>
   ** To be interpreted as Java <code>long</code>, the JSON number must neither
   ** contain an exponent nor a fraction part. Moreover, the number must be in
   ** the <code>Long</code> range.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This conversion can lose information about the overall magnitude and
   ** precision of the number value as well as return a result with the
   ** opposite sign.
   **
   ** @return                    an <code>long</code> representation of the JSON
   **                            number.
   **                            <br>
   **                            Possible object <code>long</code>.
   **
   ** @throws UnsupportedOperationException if this value is not a JSON number.
   ** @throws NumberFormatException          if this JSON number can not be
   **                                        interpreted as <code>long</code>
   **                                        value.
   */
  public abstract long longValue();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns this JSON number as a <code>long</code> value, assuming that this
   ** value represents a JSON number that can be interpreted as Java
   ** <code>long</code>.
   ** <br>
   ** If this is not the case, an exception is thrown.
   ** <p>
   ** To be interpreted as Java <code>long</code>, the JSON number must neither
   ** contain an exponent nor a fraction part. Moreover, the number must be in
   ** the <code>Long</code> range.
   **
   ** @return                    an <code>long</code> representation of the JSON
   **                            number.
   **                            <br>
   **                            Possible object <code>long</code>.
   **
   ** @throws UnsupportedOperationException if this value is not a JSON number.
   ** @throws NumberFormatException          if this JSON number can not be
   **                                        interpreted as <code>long</code>
   **                                        value.
   */
  public abstract long longValueExact();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue
  /**
   ** Returns this JSON number as a <code>double</code> value, assuming that
   ** this value represents a JSON number.
   ** <br>
   ** If this is not the case, an exception is thrown.
   ** <p>
   ** If the JSON number is out of the <code>Double</code> range,
   ** {@link Double#POSITIVE_INFINITY} or {@link Double#NEGATIVE_INFINITY} is
   ** returned.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This conversion can lose information about the overall magnitude and
   ** precision of the number value as well as return a result with the
   ** opposite sign.
   **
   ** @return                    this number as <code>double</code>.
   **                            <br>
   **                            Possible object <code>double</code>.
   **
   ** @throws UnsupportedOperationException if this value is not a JSON number.
   */
  public abstract double doubleValue();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asFloat
  /**
   ** Returns this JSON number as a <code>float</code> value, assuming that this
   ** value represents a JSON number.
   ** <br>
   ** If this is not the case, an exception is thrown.
   ** <p>
   ** If the JSON number is out of the <code>Float</code> range,
   ** {@link Float#POSITIVE_INFINITY} or {@link Float#NEGATIVE_INFINITY} is
   ** returned.
   **
   ** @return                    this number as <code>float</code>.
   **                            <br>
   **                            Possible object <code>float</code>.
   **
   ** @throws UnsupportedOperationException if this value is not a JSON number.
   */
  public abstract float floatValue();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigDecimalValue (JsonNumber)
  /**
   ** Returns this JSON number as a {@link BigDecimal} object.
   **
   ** @return                    a {@link BigDecimal} representation of the JSON
   **                            number
   **                            <br>
   **                            Possible object {@link BigDecimal}.
   */
  public abstract BigDecimal bigDecimalValue();
}