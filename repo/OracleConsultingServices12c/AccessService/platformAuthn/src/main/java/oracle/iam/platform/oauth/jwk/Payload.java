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

    File        :   Payload.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Payload.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Map;
import java.util.Objects;

import java.util.stream.Collectors;

import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.JsonObject;
import javax.json.JsonStructure;

import oracle.hst.platform.core.marshal.JsonMarshaller;

import oracle.iam.platform.oauth.jwt.Token;

////////////////////////////////////////////////////////////////////////////////
// final class Payload
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** Represents the original object that was signed with JWS or encrypted with
 ** JWE and contains views for JSON object, string, byte array and
 ** {@link EncodedURL} values.
 ** <p>
 ** Non-initial views are created on demand to conserve resources.
 ** <p>
 ** UTF-8 is the character set for all conversions between strings and byte
 * arrays.
 ** <p>
 ** Conversion relations:
 ** <pre>
 **   JsonObject &lt;=&gt; String &lt;=&gt; EncodedURL &lt;=&gt; byte[] &lt;=&gt; SignatureObject &lt;=&gt; Token.Signed
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Payload implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-133598004797012164")
  private static final long serialVersionUID = 980753535470575531L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The original payload data type. */
  public final Origin     origin;

  /** The string view. */
  private String          string;

  /** The byte array view. */
  private byte[]          bytes;

  /** The {@link EncodedURL} view. */
  private EncodedURL      encoded;

  /** The mapping view. */
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private Map<String, ?>  mapping;

  /** The signature object view. */
  private SignatureObject object;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Origin
  // ~~~~ ~~~~~~
  /**
   ** Constraint of the original data types used to create a
   */
  public static enum Origin {
      /** The payload was created from a mapping. */
      MAP,

    /** The payload was created from a byte array. */
    BYTE,

    /** The payload was created from a signed token. */
    TOKEN,

    /** The payload was created from a string. */
    STRING,

    /** The payload was created from an {@link EncodedURL}. */
    ENCODED,

    /** The payload was created from a {@link SignatureObject}. */
    SIGNATURE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs new <code>Payload</code> from the specified byte array
   ** representation.
   **
   ** @param  value              the byte array representing the payload.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  private Payload(final byte[] value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.bytes  = Objects.requireNonNull(value, "The value must not be null");
    this.origin = Origin.BYTE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs a <code>Payload</code> from the specified string representation.
   **
   ** @param  value              the string representing the payload.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  private Payload(final String value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.string = Objects.requireNonNull(value, "The value must not be null");
    this.origin = Origin.STRING;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs a <code>Payload</code> from the specified {@link EncodedURL}
   ** representation.
   **
   ** @param  value              the {@link EncodedURL} representing the
   **                            payload.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  private Payload(final EncodedURL value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.encoded = Objects.requireNonNull(value, "The value must not be null");
    this.origin  = Origin.ENCODED;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs a <code>Payload</code> from the specified mapping
   ** representation.
   **
   ** @param  mapping            the {@link Map} representing the payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any as the value.
   **
   ** @throws NullPointerException if <code>object</code> is <code>null</code>.
   */
  private Payload(final Map<String, ?> mapping) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.mapping = Objects.requireNonNull(mapping, "The mapping must not be null");
    this.origin  = Origin.MAP;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs a <code>Payload</code> from the specified
   ** {@link Token.Signed token} representation.
   **
   ** @param  token              the {@link Token.Signed token} representing the
   **                            payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SignatureObject}.
   **
   ** @throws NullPointerException     if <code>object</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the state of the passed in
   **                                  {@link Token.Signed token} is
   **                                  {@link SignatureObject.State#UNSIGNED unsigned}.
   */
  private Payload(final Token.Signed token) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (Objects.requireNonNull(token, "The signed token must not be null").state() == SignatureObject.State.UNSIGNED)
      throw new IllegalArgumentException("The signature object must be signed");

    // initialize instance attributes
    // the signed token is also a signature object
    this.object = token;
    this.origin = Origin.TOKEN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs a <code>Payload</code> from the specified
   ** {@link SignatureObject} representation.
   **
   ** @param  object             the {@link SignatureObject} representing the
   **                            payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SignatureObject}.
   **
   ** @throws NullPointerException     if <code>object</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the state of the passed in
   **                                  {@link SignatureObject} is
   **                                  {@link SignatureObject.State#UNSIGNED unsigned}.
   */
  private Payload(final SignatureObject object) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (Objects.requireNonNull(object, "The signature object must not be null").state() == SignatureObject.State.UNSIGNED)
      throw new IllegalArgumentException("The signature object must be signed");

    // initialize instance attributes
    this.object = object;
    this.origin = Origin.SIGNATURE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Payload</code> from the specified byte
   ** array representation.
   **
   ** @param  value              the byte array representing of the payload to
   **                            create.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the <code>Payload</code>.
   **                            <br>
   **                            Possible object is <code>Payload</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static Payload of(final byte[] value) {
    return new Payload(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Payload</code> from the specified string
   ** representation.
   **
   ** @param  value              the string representing of the payload to
   **                            create.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Payload</code>.
   **                            <br>
   **                            Possible object is <code>Payload</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static Payload of(final String value) {
    return new Payload(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Payload</code> from the specified
   ** {@link JsonObject} representation.
   **
   ** @param  value              the {@link EncodedURL} representing the payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>Payload</code>.
   **                            <br>
   **                            Possible object is <code>Payload</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static Payload of(final EncodedURL value) {
    return new Payload(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Payload</code> from the specified
   ** {@link JsonObject} representation.
   **
   ** @param  mapping            the mapping representation of the payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any as the value.
   **
   ** @return                    the <code>Payload</code>.
   **                            <br>
   **                            Possible object is <code>Payload</code>.
   **
   ** @throws NullPointerException if <code>mapping</code> is <code>null</code>.
   */
  public static Payload of(final Map<String, ?> mapping) {
    return new Payload(mapping);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Payload</code> from the specified
   ** {@link Token.Signed token} representation.
   **
   ** @param  token              the {@link Token.Signed token} representing the
   **                            payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SignatureObject}.
   **
   **
   ** @return                    the <code>Payload</code>.
   **                            <br>
   **                            Possible object is <code>Payload</code>.
   **
   ** @throws NullPointerException     if <code>object</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the state of the passed in
   **                                  {@link SignatureObject} is
   **                                  {@link SignatureObject.State#UNSIGNED unsigned}.
   */
  public static Payload of(final Token.Signed token) {
    return new Payload(token);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Payload</code> from the specified
   ** {@link SignatureObject} representation.
   **
   ** @param  object             the {@link SignatureObject} representing the
   **                            payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SignatureObject}.
   **
   **
   ** @return                    the <code>Payload</code>.
   **                            <br>
   **                            Possible object is <code>Payload</code>.
   **
   ** @throws NullPointerException     if <code>object</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the state of the passed in
   **                                  {@link SignatureObject} is
   **                                  {@link SignatureObject.State#UNSIGNED unsigned}.
   */
  public static Payload of(final SignatureObject object) {
    return new Payload(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for the <code>Payload</code> in its
   ** minimal form, without any additional whitespace.
   **
   ** @return                    a string representation for the
   **                            <code>Payload</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String toString() {
    // skip if already done
    if (this.string != null)
      return this.string;
    // convert
    if (this.mapping != null) {
      this.string = this.mapping.toString();
    }
    else if (this.bytes != null) {
      this.string = toString(this.bytes);
    }
    else if (this.encoded != null) {
      this.string = this.encoded.decodeString();
    }
    else if (this.object != null) {
      // FIXME: Note to myself what happens if the payload is detached?
      if (this.object.segment() != null)
        this.string = this.object.encoded();
      else
        this.string = this.object.serialize();
    }
    return this.string;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoded
  /**
   ** Returns a {@link EncodedURL} view of this <code>Payload</code>.
   **
   ** @return                    the {@link EncodedURL} representation or
   **                            <code>null</code> if the <code>Payload</code>
   **                            couldn't be converted to a {@link EncodedURL}
   **                            value.
   **                            <br>
   **                            Possible object is array of {@link EncodedURL}.
   */
  public final EncodedURL encoded() {
    // skip if already done
    if (this.encoded != null)
      return this.encoded;

    // convert
    this.encoded = EncodedURL.of(asByte());
    return this.encoded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asByte
  /**
   ** Returns a byte array view of this <code>Payload</code>.
   **
   ** @return                    the byte array representation,
   **                            <code>null</code> if the <code>Payload</code>
   **                            couldn't be converted to a byte array.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  public final byte[] asByte() {
    // skip conversion if already done
    if (this.bytes == null)
      // convert
      this.bytes = (this.encoded != null) ? this.encoded.decode() : toByte(toString());

    return this.bytes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asMap
  /**
   ** Returns a mapping object representation of this <code>Payload</code>.
   **
   ** @return                    the mapping object representation or
   **                            <code>null</code> if the <code>Payload</code>
   **                            couldn't be converted to a mapping object.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of typ {@link String} as the key
   **                            and any as the value.
   */
  public final Map<String, ?> asMap() {
    // skip conversion if already done
    if (this.mapping == null)
      // convert
      this.mapping = read(toString());

    return this.mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   asSigned
  /**
   ** Returns a signature object representation of this <code>Payload</code>.
   ** <br>
   ** Intended for signed then encrypted JOSE objects.
   **
   ** @return                    the signature object representation or
   **                            <code>null</code> if the <code>Payload</code>
   **                            couldn't be converted to a signature object.
   **                            <br>
   **                            Possible object is {@link SignatureObject}.
   */
  public SignatureObject asSigned() {
   // skip conversion if already done
    if (this.object == null)
      this.object = SignatureObject.from(toString());

    return this.object;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Converts a string to a {@link JsonObject}.
   **
   ** @param  value              the string value to convert.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the resulting object representation or
   **                            <code>null</code> if conversion failed.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of typ {@link String} as the key
   **                            and any as the value.
   */
  private static Map<String, ?> read(final String value) {
    final JsonStructure structure = Json.createReader(new StringReader(value)).read();
    if (structure.getValueType() == JsonValue.ValueType.OBJECT) {
    }
    JsonObject object = (JsonObject)structure;
    return object.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> JsonMarshaller.inbound(entry.getValue())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Constructs a new string by decoding the specified array of bytes using the
   ** {@link EncodedURL#CHARSET}.
   ** <br>
   ** The length of the string} is a function of the charset, and hence may not
   ** be equal to the length of the byte array.
   **
   ** @param  bytes              the bytes to be decoded into characters
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the converted string or <code>null</code> if
   **                            conversion failed.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String toString(final byte[] bytes) {
    try {
      // prevent bogus input
      return (bytes == null) ? null : new String(bytes, EncodedURL.CHARSET);
    }
    catch (UnsupportedEncodingException e) {
      // UTF-8 should always be supported
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toByte
  /**
   ** Encodes the specified <code>string</code> into a sequence of bytes using
   ** the {@link EncodedURL#CHARSET} charset, storing the result into a new byte
   ** array.
   **
   ** @param  value              the string to convert.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the resultant byte array or <code>null</code>
   **                            if conversion failed.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  private static byte[] toByte(final String string) {
    try {
      // prevent bogus input
      return string != null ? string.getBytes(EncodedURL.CHARSET) : null;
    }
    catch (UnsupportedEncodingException e) {
      // UTF-8 should always be supported
      return null;
     }
  }
}