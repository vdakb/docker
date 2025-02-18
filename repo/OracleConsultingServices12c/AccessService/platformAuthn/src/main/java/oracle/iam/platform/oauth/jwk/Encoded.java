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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   Encoded.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Encoded.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Objects;

import java.math.BigInteger;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

////////////////////////////////////////////////////////////////////////////////
// class Encoded
// ~~~~~ ~~~~~~~
/**
 ** Base64 encoded value object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Encoded implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** UTF-8 is the required charset for all JWTs. */
  public static final String CHARSET          = "utf-8";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1537629914659909650")
  private static final long  serialVersionUID = 6820573720070593724L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The encoded value. */
  public final String        value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>Encoded</code> value.
   **
   ** @param  value              the encoded object value.
   **                            <br>
   **                            The value is not validated for having
   **                            characters from a Base64 alphabet.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected Encoded(final String value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Encoded</code> value from the specified
   ** string value.
   **
   ** @param  value              the encoded value.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created <code>Encoded</code>.
   **                            <br>
   **                            Possible object is <code>Encoded</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static Encoded of(final String value) {
    try {
      // prevent bogus input
      return of(Objects.requireNonNull(value, "The encoded value must not be null").getBytes(CHARSET));
    }
    catch (UnsupportedEncodingException e) {
      // UTF-8 should always be supported
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Encoded</code> value from the specified
   ** {@link BigInteger} value without the sign bit.
   **
   ** @param  value              The big integer to encode.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created <code>Encoded</code>.
   **                            <br>
   **                            Possible object is <code>Encoded</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static Encoded of(final BigInteger value) {
    return of(unsignedByte(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Encoded</code> value from the specified
   ** {@link BigInteger} value with leading zero padding up to the specified
   ** field size in bits.
   **
   ** @param  size               the size of byte to obtain.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  value              the big integer to encode.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created <code>Encoded</code>.
   **                            <br>
   **                            Possible object is <code>Encoded</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static Encoded of(final int size, final BigInteger value) {
    int          output    = (size + 7) / 8;
    final byte[] notPadded = unsignedByte(value);
    // greater-than check to prevent exception on malformed key below
    if (notPadded.length >= output) {
      return of(notPadded);
    }
    else {
      final byte[] padded = new byte[output];
      System.arraycopy(notPadded, 0, padded, output - notPadded.length, notPadded.length);
      return of(padded);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Encoded</code> value from the specified
   ** byte array.
   **
   ** @param  bytes              the byte array to encode.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is array <code>byte</code>.
   **
   ** @return                    the created <code>Encoded</code>.
   **                            <br>
   **                            Possible object is <code>Encoded</code>.
   **
   ** @throws NullPointerException if encoded result evaluate to
   **                              <code>null</code>.
   */
  public static Encoded of(final byte[] bytes) {
    return new Encoded(Base64Codec.encodeToString(bytes, false));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raw
  /**
   ** Factory method to create a <code>Encoded</code> value from the
   ** specified string value.
   **
   ** @param  value              the value.
   **                            <br>
   **                            The value is not validated for having
   **                            characters from a Base64 encoded alphabet.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created <code>Encoded</code>.
   **                            <br>
   **                            Possible object is <code>Encoded</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static Encoded raw(final String value) {
    return new Encoded(Objects.requireNonNull(value, "The encoded value must not be null"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unsignedByte
  /**
   ** Returns a byte array representation of the specified big integer without
   ** the sign bit.
   **
   ** @param  value              the {@link BigInteger} to be converted.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link BigInteger}.
   **
   ** @return                    a byte array representation of the big integer,
   **                            without the sign bit.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   */
  public static byte[] unsignedByte(final BigInteger value) {
    // shameless copied from Apache Commons Codec 1.8
    int bitlen = value.bitLength();
    // round bitlen
    bitlen = ((bitlen + 7) >> 3) << 3;
    final byte[] bytes = value.toByteArray();
    if (((value.bitLength() % 8) != 0) && (((value.bitLength() / 8) + 1) == (bitlen / 8))) {
      return bytes;
    }

    // set up params for copying everything but sign bit
    int source = 0;
    int lenght = bytes.length;
    // if value is exactly byte-aligned, just skip signbit in copy
    if ((value.bitLength() % 8) == 0) {
      source = 1;
      lenght--;
    }

    final int    target  = bitlen / 8 - lenght; // to pad w/ nulls as per spec
    final byte[] resized = new byte[bitlen / 8];
    System.arraycopy(bytes, source, resized, target, lenght);
    return resized;
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
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of
   **       the two objects must produce distinct integer results. However,
   **       the programmer should be aware that producing distinct integer
   **       results for unequal objects may improve the performance of hash
   **       tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Encoded</code> values are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Encoded</code> values may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other            the reference object with which to compare.
   **                          <br>
   **                          Allowed object is {@link Object}.
   **
   ** @return                  <code>true</code> if this object is the same as
   **                          the object argument; <code>false</code>
   **                          otherwise.
   **                          <br>
   **                          Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    return other instanceof Encoded && this.toString().equals(other.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for the <code>Encoded</code> value in
   ** its minimal form, without any additional whitespace.
   **
   ** @return                    a string representation that represents this
   **                            literal.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String toString() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeString
  /**
   ** Decodes this <code>Encoded</code> object to a string.
   **
   ** @return                    the resulting string, in the UTF-8 character
   **                            set.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String decodeString() {
    try {
      return new String(decode(), CHARSET);
    }
    catch (UnsupportedEncodingException e) {
      // UTF-8 should always be supported
      return "";
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeBigInteger
  /**
   ** Decodes this <code>Encoded</code> object to an unsigned big integer.
   ** <p>
   ** Same as <code>new BigInteger(1, encoded.decode())</code>.
   **
   ** @return                    the resulting {@link BigInteger}.
   **                            <br>
   **                            Possible object is array {@link BigInteger}.
   */
  public BigInteger decodeBigInteger() {
    return new BigInteger(1, decode());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode this <code>Encoded</code> object to a byte array.
   **
   ** @return                    the resulting byte array.
   **                            <br>
   **                            Possible object is array <code>byte</code>.
   */
  public final byte[] decode() {
    return Base64Codec.decode(this.value);
  }
}