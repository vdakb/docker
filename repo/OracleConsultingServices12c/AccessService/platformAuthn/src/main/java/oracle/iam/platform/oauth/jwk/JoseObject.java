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

    File        :   JoseObject.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JoseObject.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.io.Serializable;

import javax.json.JsonObject;

import oracle.hst.platform.core.marshal.JsonMarshaller;

////////////////////////////////////////////////////////////////////////////////
// abstract class JoseObject
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The abstract class for plaintext, JSON Web Signature (JWS) - secured and
 ** JSON Web Encryption (JWE) - secured objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class JoseObject implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The MIME type of JOSE objects serialized to compact encoding:
   ** <code>application/jose; charset=UTF-8</code>
   */
  public static final String MIME_COMPACT = "application/jose; charset=UTF-8";

  /**
   ** The MIME type of JOSE objects serialized to JSON:
   ** <code>application/jose+json; charset=UTF-8</code>
   */
  public static final String MIME_JSON    = "application/jose+json; charset=UTF-8";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7391531837214921023")
  private static final long serialVersionUID = -1106650579633878640L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The payload (message) or <code>null</code> if not defined. */
  protected Payload         payload;

  /**
   ** The original parsed {@link EncodedURL} segments or <code>null</code> if
   ** the JOSE object was created from scratch.
   ** <br>
   ** The individual parts may be empty or <code>null</code> to indicate a
   ** missing part.
   */
  private EncodedURL[]      segment = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>JoseObject</code> with the specified payload.
   **
   ** @param  payload            the {@link Payload} or <code>null</code> if not
   **                            available (e.g for an encrypted JWE object).
   **                            <br>
   **                            Allowed object is {@link Payload}.
   */
  protected JoseObject(final Payload payload) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.payload = payload;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   header
  /**
   ** Returns the header of this JOSE object.
   **
   ** @return                    the header of this JOSE object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public abstract Header header();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   payload
  /**
   ** Returns the payload of this JOSE object.
   **
   ** @return                    the {@link Payload} or <code>null</code> if not
   **                            available (e.g for an encrypted JWE object).
   **                            <br>
   **                            Possible object is {@link Payload}.
   */
  public final Payload payload() {
    return this.payload;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   segment
  /**
   ** Sets the original parsed {@link EncodedURL} segments used to create this
   ** JOSE object.
   **
   ** @param  value              the original parsed {@link EncodedURL} segments
   **                            used to create this JOSE object,
   **                            <code>null</code> if the object was created
   **                            from scratch.
   **                            <br>
   **                            The individual segments may be empty or
   **                            <code>null</code> to indicate a missing part.
   **                            <br>
   **                            Allowed object is array of {@link EncodedURL}.
   */
  protected void segment(final EncodedURL... value) {
    this.segment = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   segment
  /**
   ** Returns the original parsed {@link EncodedURL} segments used to create
   ** this JOSE object.
   **
   ** @return                    the original parsed {@link EncodedURL} segments
   **                            used to create this JOSE object,
   **                            <code>null</code> if the object was created
   **                            from scratch.
   **                            <br>
   **                            The individual segments may be empty or
   **                            <code>null</code> to indicate a missing part.
   **                            <br>
   **                            Possible object is array of {@link EncodedURL}.
   */
  public final EncodedURL[] segment() {
    return this.segment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a <code>JoseObject</code> from the specified
   ** string in compact format.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed <code>JoseObject</code>.
   **                            <br>
   **                            Possible object is <code>JoseObject</code>.
   **
   ** @throws IllegalArgumentException if the string couldn't be parsed to a
   **                                  valid JOSE object.
   */
  public static JoseObject from(final String value)
    throws IllegalArgumentException {

    final EncodedURL[] subject = split(value);
		final JsonObject   object  = JsonMarshaller.readObject(subject[0].decodeString());
		final Header       header  = Header.from(object);
    switch (header.typ) {
      case JWE  : return EncryptionObject.from(value);
      case JWS  : return SignatureObject.from(value);
      case JWT  : return PlainObject.from(value);
      default   : throw new IllegalArgumentException("Unexpected header type: " + header.typ);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   split
  /**
   ** Splits a serialized JOSE object into its {@link EncodedURL} subjects.
   **
   ** @param  value              the serialized JOSE object to split.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the JOSE {@link EncodedURL} subjects (three for
   **                            plaintext and JWS objects, five for JWE
   **                            objects).
   **                            <br>
   **                            Possible object is arry of {@link EncodedURL}.
   **
   ** @throws IllegalArgumentException if the specified string couldn't be split
   **                                  into three or five {@link EncodedURL}
   **                                  subjects.
   */
  public static EncodedURL[] split(final String value)
    throws IllegalArgumentException {

    // String.split() cannot handle empty parts
    final int d1 = value.indexOf(".");
    if (d1 == -1)
      throw new IllegalArgumentException("Invalid serialized plain/JWS/JWE object: Missing subject delimiters");

    // we must have 2 (JWS) or 4 dots (JWE)
    final int d2 = value.indexOf(".", d1 + 1);
    if (d2 == -1)
      throw new IllegalArgumentException("Invalid serialized plain/JWS/JWE object: Missing second delimiter");

    // third dot for JWE only
    final int d3 = value.indexOf(".", d2 + 1);
    if (d3 == -1) {
      // two dots only? -> we have a JWS
      final EncodedURL[] parts = new EncodedURL[3];
      // assume the value containes the encoded from of the data
      parts[0] = EncodedURL.raw(value.substring(0,      d1));
      parts[1] = EncodedURL.raw(value.substring(d1 + 1, d2));
      parts[2] = EncodedURL.raw(value.substring(d2 + 1));
      return parts;
    }

    // fourth final dot for JWE
    final int d4 = value.indexOf(".", d3 + 1);
    if (d4 == -1)
      throw new IllegalArgumentException("Invalid serialized JWE object: Missing fourth delimiter");

    if (d4 != -1 && value.indexOf(".", d4 + 1) != -1)
      throw new IllegalArgumentException("Invalid serialized plain/JWS/JWE object: Too many part delimiters");

    // four dots -> five parts
    final EncodedURL[] parts = new EncodedURL[5];
    // assume the value containes the encoded from of the data
    parts[0] = EncodedURL.raw(value.substring(0,      d1));
    parts[1] = EncodedURL.raw(value.substring(d1 + 1, d2));
    parts[2] = EncodedURL.raw(value.substring(d2 + 1, d3));
    parts[3] = EncodedURL.raw(value.substring(d3 + 1, d4));
    parts[4] = EncodedURL.raw(value.substring(d4 + 1));
    return parts;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize
  /**
   ** Serialises this JOSE object to its compact format consisting of
   ** {@link EncodedURL} subjects delimited by period ('.') characters.
   **
   ** @return                    the serialized JOSE object.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalStateException if the JOSE object is not in a state that
   **                               permits serialisation.
   */
  public abstract String serialize();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoded
  /**
   ** Retruns the original parsed string used to create this JOSE object.
   **
   ** @return                    the parsed string used to create this JOSE
   **                            object or <code>null</code> if the object was
   **                            creates from scratch.
   **
   ** @see  #segment()
   */
  public String encoded() {
    // prevent bogus state
    if (this.segment == null)
      return null;

    final StringBuilder builder = new StringBuilder();
    for (EncodedURL segment : this.segment) {
      if (builder.length() > 0)
        builder.append('.');
      if (segment == null)
        continue;
      else
        builder.append(segment.toString());
    }
    return builder.toString();
  }
}