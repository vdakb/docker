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

    File        :   Header.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Header.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.Map;
import java.util.Objects;
import java.util.Collections;
import java.util.LinkedHashMap;

import java.io.Serializable;

import java.net.URI;
import java.net.URISyntaxException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.core.marshal.JsonEnum;
import oracle.hst.platform.core.marshal.JsonMarshaller;

import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class Header
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** The base abstract class for plaintext, JSON Web Signature (JWS) and JSON Web
 ** Encryption (JWE) headers.
 ** <p>
 ** The header may also carry {@link #parameter custom parameters};
 ** these will be serialized and parsed along the reserved ones.
 **
 ** @param  <T>                the type of the <code>Header</code>
 **                            implementation.
 **                            <br>
 **                            This parameter is used for convenience to allow
 **                            better implementations of <code>Header</code>s
 **                            derived from this abstract class
 **                            (<code>Header</code>s can return their own
 **                            specific type instead of type defined by this
 **                            abstract class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Header<T extends Header> implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link Algorithm} family (<code>alg</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.1">RFC 7515 "alg" (JWS Algorithm) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.1">RFC 7516 "alg" (JWE Algorithm) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.4">RFC 7517 "alg" (Algorithm) Parameter.</a>
   */
  public static final String       ALG              = "alg";

  /**
   ** The {@link Type} (<code>typ</code>) parameter
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.9">RFC 7515 "typ" (Type) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.11">RFC 7516 "typ" (Type) Header Parameter</a>
   */
  public static final String       TYP              = "typ";

  /**
   ** The content type (<code>cty</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.10">RFC 7515 "cty" (Content Type) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.12">RFC 7516 "cty" (Content Type) Header Parameter</a>
   */
  public static final String       CTY              = "cty";

  /** The reserved parameter names. */
  private static final Set<String> RESERVED         = CollectionUtility.unmodifiableSet(ALG, TYP, CTY);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-982052847220091616")
  private static final long        serialVersionUID = -7637839917507966645L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The {@link Algorithm} (<code>alg</code>) parameter. */
  public final Algorithm           alg;

  /** The {@link Type} (<code>typ</code>) parameter. */
  public final Type                typ;

  /** The content type (<code>cty</code>) parameter. */
  public final String              cty;

  /**
   ** The original parsed EncodedURL, <code>null</code> if the header was
   ** created from scratch.
   */
  public final EncodedURL          b64;

  /** The custom header parameters. */
  public final Map<String, String> prm;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Builder
  // ~~~~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Abstract <code>Builder</code> for constructing JSON Web Key Header.
   ** <p>
   ** This implementation provides the mutator methods to the common JSON Web
   ** Key Header properties:
   ** <ul>
   **   <li>{@link #alg} (required)
   **   <li>{@link #typ} (required)
   **   <li>{@link #cty} (optional)
   ** </ul>
   **
   ** @param  <B>                the type of the <code>Builder</code>
   **                            implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of <code>Builder</code>s
   **                            derived from this abstract class
   **                            (<code>Builder</code>s can return their own
   **                            specific type instead of type defined by this
   **                            abstract class only).
   **                            <br>
   **                            Allowed object is <code>&lt;B&gt;</code>.
   */
  public static abstract class Builder<B extends Builder, T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The {@link Algorithm} (<code>alg</code>) parameter. */
    protected final Algorithm           alg;

    /** The custom header parameters. */
    protected final Map<String, String> prm = new LinkedHashMap<String, String>();

    /** The {@link Type} (<code>typ</code>) parameter. */
    protected Type                      typ;

    /** The content type (<code>cty</code>) parameter. */
    protected String                    cty;

    /**
     ** The original parsed {@link EncodedURL}, <code>null</code> if the header
     ** was created from scratch.
     */
    protected EncodedURL                b64;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Builder
    /**
     ** Creates a JSON Web Key Header builder for the specified
     ** {@link Algorithm}.
     **
     ** @param  algorithm        the algorithm type.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Algorithm}.
     **
     ** @throws NullPointerException if <code>algorithm</code> is
     **                                 <code>null</code>.
     */
    protected Builder(final Algorithm algorithm) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.alg = Objects.requireNonNull(algorithm, "The algorithm \"alg\" header parameter must not be null");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Sets the <code>type</code> property of the {@link Header}
     ** <code>Builder</code>.
     **
     ** @param  value            the <code>type</code> property of the
     **                          {@link Header} to set.
     **                          <br>
     **                          Allowed object is {@link Type}.
     **
     ** @return                  the {@link Header} <code>Builder</code> to
     **                          allow method chaining.
     **                          <br>
     **                          Possible object is @link Header}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B type(final Type value) {
      this.typ = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: contentType
    /**
     ** Sets the <code>contentType</code> property of the {@link Header}
     ** <code>Builder</code>.
     **
     ** @param  value            the <code>contentType</code> property of
     **                          the {@link Header} to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link Header} <code>Builder</code> to
     **                          allow method chaining.
     **                          <br>
     **                          Possible object is @link Header}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B contentType(final String value) {
      this.cty = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: parameter
    /**
     ** Adds a additional custom (non-reserved) parameter of this {@link Header}
     ** <code>Builder</code>.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** Invoker and extending classes should ensure the parameter name doesn't
     ** match a reserved parameter name.
     **
     ** @param  name             the name of the additional custom parameter to
     **                          add.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the value of the additional custom parameter to
     **                          add.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link Header} <code>Builder</code> to
     **                          allow method chaining.
     **                          <br>
     **                          Possible object is @link Header}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    protected B parameter(final String name, final String value) {
      // prevent bogus input
      if (RESERVED.contains(name))
        throw new IllegalArgumentException("The parameter name \"" + name + "\" matches a reserved name");

      this.prm.put(name, value);
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: origin
    /**
     ** Sets the original parsed {@link EncodedURL} <code>b64</code> property of
     ** the {@link Header} <code>Builder</code>.
     **
     ** @param  value            the original parsed {@link EncodedURL} of the
     **                          {@link Header} to set.
   **                            <br>
   **                            May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the {@link Header} <code>Builder</code> to
     **                          allow method chaining.
     **                          <br>
     **                          Possible object is @link Header}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B origin(final EncodedURL value) {
      this.b64 = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a {@link Header} with the properties
     ** configured.
     **
     ** @return                  the created {@link Header} populated with
     **                          the properties configured.
     **                          <br>
     **                          Possible object is @link Header}
     **                          <code>Builder</code> of type <code>B</code>.
     **
     ** @throws IllegalStateException if the JSON Web Key Header properties were
     **                               inconsistently specified.
     */
    public abstract T build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** Represents the <code>typ</code> header parameter in plain JSON Web
   ** Signature (JWS) and JSON Web Encryption (JWE) objects.
   ** <p>
   ** Includes constants for the following standard types:
   ** <ul>
   **   <li>{@link #JWS}
   **   <li>{@link #JWE}
   **   <li>{@link #JWT}
   ** </ul>
   */
  public static enum Type implements JsonEnum<Type, String> {
      /** JWS object type. */
      JWS("JWS"),

    /** JWE object type. */
    JWE("JWE"),

    /** generic object type. */
    JWT("JWT")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The object type value. */
    private final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a new object type.
     **
     ** @param  id               the type identifier value.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Type(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a proper <code>Type</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Type</code> constraint.
     **                          <br>
     **                          Possible object is <code>Type</code>.
     **
     ** @throws IllegalArgumentException if no matching <code>Type</code> could
     **                                  be found.
     */
    @Override
    public final Type of(final String value) {
      return from(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the value that identifies the <code>JsonEnum</code>
     **
     ** @return                  a value that can be used later in
     **                          {@link #of(Object)}
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public final String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a <code>Type</code> from the specified JSON
     ** object representation.
     **
     ** @param  object           the JSON objects to parse.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link JsonObject}.
     **
     ** @return                  the parsed cryptographic <code>Type</code>.
     **                          <br>
     **                          Possible object is <code>Type</code>.
     **
     ** @throws NullPointerException     if <code>value</code> is
     **                                  <code>null</code> or.
     ** @throws IllegalArgumentException if no matching <code>Type</code> could
     **                                  be found.
     */
    public static Type from(final JsonObject object) {
      return from(JsonMarshaller.stringValue(object, TYP));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Type</code> constraint from the
     ** given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Type</code> constraint.
     **                          <br>
     **                          Possible object is <code>Type</code>.
     */
    public static Type from(final String value) {
      for (Type cursor : Type.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns the string representation for the <code>Type</code> in its
     ** minimal form, without any additional whitespace.
     **
     ** @return                  a string representation that represents this
     **                          <code>Type</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public final String toString() {
      return this.id;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>Header</code> from the specified
   ** <code>Header</code>.
   **
   ** @param  other              the <code>Header</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>Header</code> of type
   **                            <code>T</code>.
   */
  protected Header(final Header<T> other) {
    this(other.alg, other.typ, other.cty, other.prm, other.b64);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>Header</code> with the specified key
   ** {@link Algorithm}.
   **
   ** @param  algorithm          the {@link Algorithm algorithm}
   **                            (<code>alg</code>) parameter.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Algorithm}.
   ** @param  type               the optional {@link Type type}
   **                            (<code>typ</code>) parameter.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  contentType        the optional content type (<code>cty</code>)
   **                            parameter.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the optional custom parameter parameter.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} for the value.
   ** @param  origin             the optional parsed {@link EncodedURL}.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   */
  protected Header(final Algorithm algorithm, final Type type, final String contentType, final Map<String, String> parameter, final EncodedURL origin) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.alg = algorithm;
    this.typ = type;
    this.cty = contentType;
    this.prm = parameter;
    this.b64 = origin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   included
  /**
   ** Returns the names of all included parameters (reserved and custom) in the
   ** header.
   **
   ** @return                    the included parameters.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public abstract Set<String> included();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Returns a additional custom (non-reserved) parameter of this
   ** <code>Header</code>.
   **
   ** @param  name               the name of the additional custom
   **                            (non-reserved) parameter to return.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of the additional custom
   **                            (non-reserved) parameter mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String parameter(final String name) {
    return this.prm.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Returns the additional custom (non-reserved) parameter of this
   ** <code>Header</code>.
   **
   ** @return                    the additional custom (non-reserved) parameter
   **                            of this <code>Header</code>.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link String} as the value.
   */
  public final Map<String, String> parameter() {
    return Collections.unmodifiableMap(this.prm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parsed
  /**
   ** Returns the <code>parsed</code> property of the <code>Header</code>.
   **
   ** @return                    the <code>parsed</code> property of
   **                            the <code>Header</code>.
   **                            <br>
   **                            Possible object is {@link EncodedURL}.
   */
  public final EncodedURL parsed() {
    return this.b64;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a <code>Header</code> from the specified {@link JsonObject}
   ** representation.
   **
   ** @param  object             the {@link JsonObject} to examine.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the parsed <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Possible object is <code>JsonWebKeySet</code>.
   **
   ** @throws NullPointerException     if <code>object</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the specified JSON object doesn't
   **                                  represent a valid header.
   */
  public static Header from(final JsonObject object)
    throws IllegalArgumentException {

    // prevent bogus input
    final Algorithm alg = algorithm(Objects.requireNonNull(object, "The Json Object must not be null"));
    switch (alg) {
      case NONE           : return PlainHeader.from(object);
      case HS256          :
      case HS384          :
      case HS512          :
      case RS256          :
      case RS384          :
      case RS512          :
      case ES256          :
      case ES384          :
      case ES512          : return SignatureHeader.from(object);
      case RSA1_5         :
      case RSA_OAEP       :
      case A128KW         :
      case A256KW         :
      case DIRECT         :
      case ECDH_ES        :
      case ECDH_ES_A128KW :
      case ECDH_ES_A256KW : return EncryptionHeader.from(object);
      default             : throw new AssertionError("Unexpected algorithm type: " + alg.id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm
  /**
   ** Parses an {@link Algorithm} (<code>alg</code>) parameter from the
   ** specified header JSON object.
   ** <br>
   ** Intended for initial parsing of plain, JWS and JWE headers.
   ** <p>
   ** The algorithm type (none, JWS or JWE) is determined by inspecting the
   ** algorithm name for "none" and the presence of an "enc" parameter.
   **
   ** @param  object             the Json object to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    The {@link Algorithm}  instance.
   **                            <br>
   **                            Possible object is {@link Algorithm}.
   *
   * @throws IllegalArgumentException if the {<code>alg</code> parameter
   *                                  couldn't be parsed.
   */
  public static Algorithm algorithm(final JsonObject object)
    throws IllegalArgumentException {

    // infer algorithm type
    // NONE/JWE/JWS
    return Algorithm.from(object.getString(ALG));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for the <code>Header</code> value in
   ** its minimal form, without any additional whitespace.
   **
   ** @return                    a string representation that represents this
   **                            literal.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String toString() {
    return jsonify().build().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoded
  /**
   ** Returns a {@link EncodedURL} representation of this header.
   ** <p>
   ** If the header was already returns the original {@link EncodedURL}
   ** (required for JWS validation and authenticated JWE decryption).
   **
   ** @return                 the original parsed {@link EncodedURL}
   **                         representation of this header.
   **                         <br>
   **                         Possible object is {@link EncodedURL}.
   */
  public EncodedURL encoded() {
    return EncodedURL.of(toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  /**
   ** Returns a JSON object representation of this <code>Header</code>.
   ** <p>
   ** This method is intended to be called from extending classes.
   ** <br>
   ** The result is guaranteed to be a valid input for the method
   ** {@code JsonReader#readObject(Reader)} and to create a value that is
   ** <em>equal</em> to this object.
   **
   ** @return                    the JSON object representation.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public JsonObjectBuilder jsonify() {
    final JsonObjectBuilder builder = Json.createObjectBuilder();
    // include custom parameters, they will be overwritten if their names match
    // specified reserved ones
    for (Map.Entry<String, String> cursor : this.prm.entrySet()) {
      builder.add(cursor.getKey(), cursor.getValue());
    }
    // algorithm is always defined
    builder.add(ALG, this.alg.toString());
    if (this.typ != null)
      builder.add(TYP, this.typ.toString());
    if (this.cty != null)
      builder.add(CTY, this.cty);

    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificateURI
  /**
   ** Parses a string member of a JSON object as {@link URI}.
   **
   ** @param  object             the JSON object to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  key                the JSON object member key.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the member value.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @throws IllegalArgumentException if the value is missing,
   **                                  <code>null</code> or not of the expected
   **                                  type.
   */
  protected static URI certificateURI(final JsonObject object, final String key)
    throws IllegalArgumentException {

    final String value = JsonMarshaller.stringValue(object, key);
    try {
      return value == null ? null : new URI(value);
    }
    catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }
}