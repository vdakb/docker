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

    File        :   EncodedURL.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EncodedURL.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Objects;

import java.math.BigInteger;

import java.io.UnsupportedEncodingException;

////////////////////////////////////////////////////////////////////////////////
// class EncodedURL
// ~~~~~ ~~~~~~~~~~
/**
 ** Base64 encoded value URL object.
 ** <p>
 ** Related specifications:
 ** <ul>
 **   <li>RFC 4648.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EncodedURL extends Encoded {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2498505428364242142")
  private static final long serialVersionUID = -8583650646824145102L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>EncodedURL</code> value.
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
  protected EncodedURL(final String value) {
    // ensure inheritance
    super(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>EncodedURL</code> value from the
   ** specified string value.
   **
   ** @param  value              the encoded value.
   **                            <br>
   **                            The value is not validated for having
   **                            characters from a encoded alphabet.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created <code>EncodedURL</code>.
   **                            <br>
   **                            Possible object is <code>EncodedURL</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static EncodedURL of(final String value) {
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
   ** Factory method to create a <code>EncodedURL</code> value from the
   ** specified {@link BigInteger} value without the sign bit.
   **
   ** @param  value              The big integer to encode.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created <code>EncodedURL</code>.
   **                            <br>
   **                            Possible object is <code>EncodedURL</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static EncodedURL of(final BigInteger value) {
    return of(unsignedByte(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>EncodedURL</code> value from the
   ** specified {@link BigInteger} value with leading zero padding up to the
   ** specified field size in bits.
   **
   ** @param  size               the size of byte to obtain.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  value              The big integer to encode.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created <code>EncodedURL</code>.
   **                            <br>
   **                            Possible object is <code>EncodedURL</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static EncodedURL of(final int size, final BigInteger value) {
    final byte[] notPadded = unsignedByte(value);
    int          output    = (size + 7) / 8;
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
   ** Factory method to create a <code>EncodedURL</code> value from the
   ** specified byte array.
   **
   ** @param  bytes              the byte array to encode.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is array <code>byte</code>.
   **
   ** @return                    the created <code>EncodedURL</code>.
   **                            <br>
   **                            Possible object is <code>EncodedURL</code>.
   **
   ** @throws NullPointerException if encoded result evaluate to
   **                              <code>null</code>.
   */
  public static EncodedURL of(final byte[] bytes) {
    return raw(Base64Codec.encodeToString(bytes, true));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raw
  /**
   ** Factory method to create a <code>EncodedURL</code> value from the
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
   ** @return                    the created <code>EncodedURL</code>.
   **                            <br>
   **                            Possible object is <code>EncodedURL</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static EncodedURL raw(final String value) {
    return new EncodedURL(Objects.requireNonNull(value, "The encoded value must not be null"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>EncodedURL</code>s values are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>EncodedURL</code>s values may be different even though they contain
   ** the same set of names with the same values, but in a different order.
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
    return other instanceof EncodedURL && this.toString().equals(other.toString());
  }
}
