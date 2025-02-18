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

    File        :   JsonInteger.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonInteger.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.json.simple;

import java.math.BigInteger;
import java.math.BigDecimal;

////////////////////////////////////////////////////////////////////////////////
// final class JsonInteger
// ~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** An immutable JSON number value.
 ** <p>
 ** Implementation use a <code>int</code> object to store the numeric value
 ** internally.
 ** <p>
 ** The <code>JsonInteger</code> object can be constructed from the following
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
final class JsonInteger extends JsonNumber {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3123384404726409779")
  private static final long serialVersionUID = -6520876435162955154L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final int value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JsonInteger</code> which ths specified
   ** <code>int</code> value.
   **
   ** @param  value              the <code>int</code> value
   **                            Allowed object <code>int</code>.
   */
  JsonInteger(final int value) {
    // ensure inheritance
    super();

    // initialize instance
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (JsonValue)
  /**
   ** Compares the specified object with this {@link JsonNumeric} object for
   ** equality. Returns <code>true</code> if and only if the type of the
   ** specified object is also {@link JsonNumeric} and their
   ** {@link #bigDecimalValue()} objects are <i>equal</i>.
   **
   ** @param  other              the object to be compared for equality with
   **                            this {@link JsonNumeric}.
   **
   ** @return                    <code>true</code> if the specified object is
   **                            equal to this {@link JsonNumeric}; otherwise
   **                            <code>false</code>.
   */
  @Override
  public final boolean equals(Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final JsonInteger that = (JsonInteger)other;
    return this.value == that.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (JsonValue)
  /**
   ** Returns a JSON text representation of the JSON number.
   ** <br>
   ** The representation is equivalent to {@link BigDecimal#toString()}.
   **
   ** @return                    the JSON text representation of the number.
   */
  @Override
  public final String toString() {
    return String.valueOf(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intValue (JsonNumber)
  /**
   ** Returns this JSON number as an <code>int</code>.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This conversion can lose information about the overall magnitude and
   ** precision of the number value as well as return a result with the
   ** opposite sign.
   **
   ** @return                    an <code>int</code> representation of the JSON
   **                            number.
   **
   ** @see    java.math.BigDecimal#intValue()
   */
  @Override
  public final int intValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intValueExact (JsonNumber)
  /**
   ** Returns this JSON number as an <code>int</code>.
   **
   ** @return                    an <code>int</code> representation of the JSON
   **                            number.
   */
  @Override
  public final int intValueExact() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue (JsonNumber)
  /**
   ** Returns this JSON number as a <code>long</code>.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This conversion can lose information about the overall magnitude and
   ** precision of the number value as well as return a result with the
   ** opposite sign.
   **
   ** @return                    an <code>long</code> representation of the JSON
   **                            number.
   */
  @Override
  public final long longValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValueExact (JsonNumber)
  /**
   ** Returns this JSON number as a <code>long</code>.
   **
   ** @return                    an <code>long</code> representation of the JSON
   **                            number.
   */
  @Override
  public final long longValueExact() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue (JsonNumber)
  /**
   ** Returns this JSON number as a <code>double</code>.
   ** <br>
   ** This is a a convenience method for
   ** <code>bigDecimalValue().doubleValue()</code>.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This conversion can lose information about the overall magnitude and
   ** precision of the number value as well as return a result with the
   ** opposite sign.
   **
   ** @return                    an <code>double</code> representation of the
   **                            JSON number.
   **
   ** @see    BigInteger#doubleValue()
   */
  @Override
  public final double doubleValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   floatValue (JsonNumber)
  /**
   ** Returns this JSON number as a <code>float</code>.
   ** <br>
   ** This is a a convenience method for
   ** <code>bigDecimalValue().doubleValue()</code>.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This conversion can lose information about the overall magnitude and
   ** precision of the number value as well as return a result with the
   ** opposite sign.
   **
   ** @return                    an <code>double</code> representation of the
   **                            JSON number.
   **
   ** @see    BigInteger#floatValue()
   */
  @Override
  public final float floatValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigDecimalValue (JsonNumber)
  /**
   ** Returns this JSON number as a {@link BigDecimal} object.
   **
   ** @return                    a {@link BigDecimal} representation of the JSON
   **                            number
   */
  public BigDecimal bigDecimalValue() {
    if (this.decimal == null)
      this.decimal = new BigDecimal(this.value);

    return this.decimal;
  }
}