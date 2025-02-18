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

    File        :   JsonString.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonString.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.parser;

////////////////////////////////////////////////////////////////////////////////
// class JsonString
// ~~~~~ ~~~~~~~~~~
/**
 ** Represents a JSON string.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class JsonString extends JsonValue {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4431170395527132179")
  private static final long serialVersionUID = -288457965704299196L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String      value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>JsonLiteral</code> with the contents of the specified
   ** String value.
   **
   ** @param  value              the <code>String</code> to get the initial
   **                            contents from, must not be <code>null</code>.
   */
  JsonString(final String value) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (value == null)
      throw new NullPointerException("value must not be null");

    // initialize instance attributes
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Returns this JSON string as String, assuming that this value represents a
   ** JSON string.
   **
   ** @return                   the string represented by this value.
   */
  public final String string() {
    return this.value;
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
    return Type.STRING;
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
  public final int hashCode() {
    return this.value.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (JsonValue)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>JsonString</code>s are considered equal if and only if they
   ** represent the same JSON text. As a consequence, two given
   ** <code>JsonString</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public final boolean equals(final Object other) {
    if (other == null)
      return false;

    if (this == other)
      return true;

    if (getClass() != other.getClass())
      return false;

    final JsonString that = (JsonString)other;
    return this.value.equals(that.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (JsonValue)
  /**
   ** Returns the JSON string for JSON value in its minimal form, without any
   ** additional whitespace.
   ** <br>
   ** The result is guaranteed to be a valid input for the method
   ** {@link #readFrom(String)} and to create a value that is <em>equal</em> to
   ** this object.
   **
   ** @return                   a JSON string that represents this literal.
   */
  @Override
  public final String toString() {
    return this.value;
  }
}