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

    File        :   SecureHeader.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SecureHeader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.Map;
import java.util.List;

import java.net.URI;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class SecureHeader
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Base class for JSON Web Signature (JWS) and JSON Web Encryption (JWE)
 ** header.
 ** <p>
 ** Supports all reserved header parameters of the JWS and JWE specification:
 ** <ul>
 **   <li>alg
 **   <li>jku
 **   <li>jwk
 **   <li>zip
 **   <li>x5u
 **   <li>x5t
 **   <li>x5c
 **   <li>x5t
 **   <li>kid
 **   <li>typ
 **   <li>cty
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class SecureHeader<T extends SecureHeader> extends Header<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The encryption {@link Algorithm} (<code>enc</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.2">RFC 7516 "enc" (Encryption Algorithm) Header Parameter</a>
   */
  public static final String       ENC              = "enc";

  /**
   ** The compression algorithm (<code>zip</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.3">RFC 7516 "zip" (Compression Algorithm) Header Parameter</a>
   */
  public static final String       ZIP              = "zip";

  /**
   ** The JSON Web Key {@link URI} (<code>jku</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.2">RFC 7515 "jku" (JWK Set URL) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.4">RFC 7516 "jku" (JWK Set URL) Header Parameter</a>
   */
  public static final String       JKU              = "jku";

  /**
   ** The JSON Web Key (<code>jwk</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.3">RFC 7515 "jwk" (JSON Web Key) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.5">RFC 7516 "jwk" (JSON Web Key) Header Parameter</a>
   */
  public static final String       JWK              = "jwk";


  /**
   ** The JSON Web Key ID (<code>kid</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.4">RFC 7515 "kid" (Key ID) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.6">RFC 7516 "kid" (Key ID) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.5">RFC 7517 "kid" (Key ID) Parameter.</a>
   */
  public static final String       KID             = "kid";

  /**
   ** The X.509 certificate {@link URI} (<code>jku</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.5">RFC 7515 "x5u" (X.509 URL) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.7">RFC 7516 "x5u" (X.509 URL) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.6">RFC 7517 "x5u" (X.509 Certificate URL) Parameter.</a>
   */
  public static final String       X5U              = "x5u";

  /**
   ** The X.509 certificate chain (<code>x5c</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.6">RFC 7515 "x5c" (X.509 Certificate Chain) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.8">RFC 7516 "x5c" (X.509 Certificate Chain) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.7">RFC 7517 "x5c" (X.509 Certificate Chain) Parameter.</a>
   */
  public static final String       X5C              = "x5c";

  /**
   ** The X.509 certificate SHA-1 thumbprint (<code>x5t</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.7">RFC 7515 "x5t" (X.509 Certificate SHA-1 Thumbprint) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.9">RFC 7516 "x5t" (X.509 Certificate SHA-1 Thumbprint) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.8">RFC 7517"x5t" (X.509 Certificate SHA-1 Thumbprint) Parameter.</a>
   */
  public static final String       X5T              = "x5t";

  /**
   ** The X.509 certificate SHA-256 thumbprint (<code>x5t256</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.8">RFC 7515 "x5t#S256" (X.509 Certificate SHA-256 Thumbprint) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.10">RFC 7516 "x5t#S256" (X.509 Certificate SHA-256 Thumbprint) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.9">RFC 7517 "x5t#S256" (X.509 Certificate SHA-256 Thumbprint) Parameter.</a>
   */
  public static final String       X5TS256          = "x5t#S256";

  /**
   ** The critical (<code>crit</code>) parameter.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7515#section-4.1.11">RFC 7515 "crit" (Critical) Header Parameter</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7516#section-4.1.13">RFC 7516 "crit" (Critical) Header Parameter</a>
   */
  public static final String       CTR              = "crit";

  /** The reserved parameter names. */
  private static final Set<String> RESERVED         = CollectionUtility.unmodifiableSet(
    JKU
  , JWK
  , X5U
  , X5T
  , X5TS256
  , X5C
  , KID
  , CTR
  );

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2813954102985412207")
  private static final long        serialVersionUID = 3463041091267527110L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The JWK URI or <code>null</code> if not specified. */
  public final URI           jku;

  /** The X.509 certificate URI or <code>null</code> if not specified. */
  public final URI           x5u;

  /**
   ** The X.509 certificate SHA-1 thumbprint or <code>null</code> if not
   ** specified.
   */
  public final EncodedURL    x5t;

  /**
   ** The X.509 certificate SHA-256 thumbprint or <code>null</code> if not
   ** specified.
   */
  public final EncodedURL    x5t256;

  /**
   ** The X.509 certificate chain corresponding to the key used to sign or
   ** encrypt the JWS/JWE object or <code>null</code> if not specified.
   */
  public final List<Encoded> x5c;

  /** Json Web Key or <code>null</code> if not specified. */
  public final JsonWebKey    jwk;

  /** The JSON Web Key ID or <code>null</code> if not specified. */
  public final String        kid;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Builder
  // ~~~~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Builder for constructing secured encryption and signature headers.
   ** <br>
   ** This implementation provides the mutator methods to the common JSON Web
   ** Key Header properties:
   ** <ul>
   **   <li>{@link #jku} (optional)
   **   <li>{@link #x5u} (optional)
   **   <li>{@link #x5t} (optional, deprecated)
   **   <li>{@link #x5t256} (optional)
   **   <li>{@link #x5c} (optional)
   **   <li>{@link #jwk} (optional)
   **   <li>{@link #kid} (optional)
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
  static abstract class Builder<B extends Builder, T> extends Header.Builder<B, T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The JWK Set URL or <code>null</code> if not specified. */
    protected URI             jku;

    /** The X.509 certificate URL or <code>null</code> if not specified. */
    protected URI             x5u;

    /**
     ** The X.509 certificate SHA-1 thumbprint or <code>null</code> if not
     ** specified.
     */
    protected EncodedURL      x5t;

    /**
     ** The X.509 certificate SHA-256 thumbprint or <code>null</code> if not
     ** specified.
     */
    protected EncodedURL      x5t256;

    /**
     ** The X.509 certificate chain corresponding to the key used to sign or
     ** encrypt the JWS/JWE object or <code>null</code> if not specified.
     */
    protected List<Encoded>  x5c;

    /** Json Web Key or <code>null</code> if not specified. */
    protected JsonWebKey     jwk;

    /** The key ID or <code>null</code> if not specified. */
    protected String         kid;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a new {@link SecureHeader} <code>Builder</code> for the
     ** {@link Algorithm} specified.
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
      // enure inheritance
      super(algorithm);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: jku
    /**
     ** Sets the JSON Web Key (JWK) Set URI (<code>jku</code>) parameter of the
     ** <code>SecureHeader</code>.
     **
     ** @param  value            the JSON Web Key (JWK) Set URI parameter,
     **                          <code>null</code> if not specified.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the {@link SecureHeader} <code>Builder</code>
     **                          to allow method chaining.
     **                          <br>
     **                          Possible object is @link SecureHeader}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B jku(final URI value) {
      this.jku = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: x5u
    /**
     ** Sets the X.509 certificate URI (<code>x5u</code>) parameter of the
     ** <code>SecureHeader</code>.
     **
     ** @param  value            the X.509 certificate URI parameter,
     **                          <code>null</code> if not specified.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the {@link SecureHeader} <code>Builder</code>
     **                          to allow method chaining.
     **                          <br>
     **                          Possible object is @link SecureHeader}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B x5u(final URI value) {
      this.x5u = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: x5t
    /**
     ** Sets the X.509 certificate thumbprint (<code>x5t</code>) parameter of
     ** the <code>SecureHeader</code>.
     **
     ** @param  value            the X.509 certificate thumbprint parameter,
     **                          <code>null</code> if not specified.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the {@link SecureHeader} <code>Builder</code>
     **                          to allow method chaining.
     **                          <br>
     **                          Possible object is @link SecureHeader}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B x5t(final EncodedURL value) {
      this.x5t = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: x5t256
    /**
     ** Sets the X.509 SHA-256 certificate thumbprint (<code>x5t#S256</code>)
     ** parameter of the <code>SecureHeader</code>.
     **
     ** @param  value            the X.509 SHA-256 certificate thumbprint
     **                          parameter, <code>null</code> if not specified.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the {@link SecureHeader} <code>Builder</code>
     **                          to allow method chaining.
     **                          <br>
     **                          Possible object is @link SecureHeader}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B x5t256(final EncodedURL value) {
      this.x5t256 = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: x5c
    /**
     ** Sets the X.509 certificate chain (<code>x5c</code>) parameter of the
     ** <code>SecureHeader</code>.
     **
     ** @param  value            the X.509 certificate chain parameter,
     **                          <code>null</code> if not specified.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type of {@link Encoded}.
     **
     ** @return                  the {@link SecureHeader} <code>Builder</code>
     **                          to allow method chaining.
     **                          <br>
     **                          Possible object is @link SecureHeader}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B x5c(final List<Encoded> value) {
      this.x5c = CollectionUtility.unmodifiable(value);
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: jwk
    /**
     ** Sets the JSON Web Key (JWK) (<code>jwk</code>) parameter of the
     ** <code>SecureHeader</code>.
     **
     ** @param  value            the JSON Web Key (JWK) (<code>jwk</code>),
     **                          <code>null</code> if not specified.
     **                          <br>
     **                          Allowed object is {@link JsonWebKey}.
     **
     ** @return                  the {@link SecureHeader} <code>Builder</code>
     **                          to allow method chaining.
     **                          <br>
     **                          Possible object is @link SecureHeader}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B jwk(final JsonWebKey value) {
      this.jwk = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: kid
    /**
     ** Sets the JSON Web Key (JWK) key ID (<code>kid</code>) parameter of the
     ** <code>SecureHeader</code>.
     **
     ** @param  value            the JSON Web Key (JWK) (<code>kid</code>),
     **                          <code>null</code> if not specified.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link SecureHeader} <code>Builder</code>
     **                          to allow method chaining.
     **                          <br>
     **                          Possible object is @link SecureHeader}
     **                          <code>Builder</code> of type <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B kid(final String value) {
      this.kid = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: parameter (overridden)
    /**
     ** Adds a additional custom (non-reserved) parameter of this
     ** <code>EncryptionHeader</code>.
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
     ** @return                  the {@link SecureHeader} <code>Builder</code>
     **                          to allow method chaining.
     **                          <br>
     **                          Possible object is @link SecureHeader}
     **                          <code>Builder</code> of type <code>B</code>.
     **
     ** @throws IllegalArgumentException if the specified parameter name matches
     **                                  a reserved parameter name.
     */
    @Override
    protected B parameter(final String name, final String value) {
      // prevent bogus input
      if (RESERVED.contains(name))
        throw new IllegalArgumentException("The parameter name \"" + name + "\" matches a reserved name");

      return super.parameter(name, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>SecureHeader</code> from the specified
   ** <code>SecureHeader</code>.
   **
   ** @param  other              the <code>SecureHeader</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>SecureHeader</code> of
   **                            type <code>T</code>.
   */
  protected SecureHeader(final SecureHeader<T> other) {
    this(other.jku, other.x5u, other.x5t, other.x5t256, other.x5c, other.jwk, other.kid, other.alg, other.typ, other.cty, other.prm, other.b64);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>SecureHeader</code> with the specified key
   ** {@link Algorithm}.
   **
   ** @param  jku                the JSON Web Key (JWK) Set URI parameter,
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  x5u                the X.509 certificate URI parameter,
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  x5t                the X.509 SHA-1 certificate thumbprint
   **                            parameter, <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  x5t256             the X.509 SHA-256 certificate thumbprint
   **                            parameter, <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  x5c                the X.509 certificate chain parameter,
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type of {@link Encoded}.
   ** @param  jwk                the JSON Web Key (JWK) (<code>jwk</code>),
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link JsonWebKey}.
   ** @param  kid                the JSON Web Key (JWK) (<code>kid</code>),
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  algorithm          the key {@link Algorithm}.
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
   ** @param  parsed             the optional parsed {@link EncodedURL}.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   */
  protected SecureHeader(final URI jku, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final JsonWebKey jwk, final String kid, final Algorithm algorithm, final Type type, final String contentType, final Map<String, String> parameter, final EncodedURL parsed) {
    // ensure inheritance
    super(algorithm, type, contentType, parameter, parsed);

    // initiaalize instance attributes
    this.jku    = jku;
    this.x5u    = x5u;
    this.x5t    = x5t;
    this.x5t256 = x5t256;
    this.x5c    = x5c;
    this.jwk    = jwk;
    this.kid    = kid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify (overridden)
  /**
   ** Returns a JSON object representation of this <code>SecureHeader</code>.
   ** <p>
   ** This method is intended to be called from extending classes.
   ** <br>
   ** The result is guaranteed to be a valid input for the method
   ** {@code JsonReader#readObject(Reader)} and to create a value that is
   ** <em>equal</em> to this object.
   **
   ** @return                    the JSON object representation.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  @Override
  public JsonObjectBuilder jsonify() {
    final JsonObjectBuilder builder = super.jsonify();
    if (this.jku != null)
      builder.add(JKU, this.jku.toString());
    if (this.jwk != null)
      builder.add(JWK, this.jwk.jsonify());
    if (this.x5u != null)
      builder.add(X5U, this.x5u.toString());
    if (this.x5t != null)
      builder.add(X5T, this.x5t.toString());
    if (this.x5c != null) {
      final JsonArrayBuilder array = Json.createArrayBuilder();
      for (Encoded cursor : this.x5c)
        array.add(cursor.toString());
      builder.add(X5C, array);
      /*
      Stream.of(this.x5c).map(e -> array.add(e.toString()));
      builder.add(X5C, Stream.of(this.x5c).map(e -> e.toString()).collect(Collectors.toList()));
      Stream.of(this.x5c).collect(e -> e.toString(), JsonCollector.array()).build();
      */
    }
    if (this.kid != null)
      builder.add(KID, this.kid);
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificateChain
  /**
   ** Parses the optional X.509 certificate chain (<code>x5c</code>)from the
   ** specified {@link JsonObject} representation of a <code>JsonWebKey</code>.
   **
   ** @param  object             the {@link JsonObject} to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object id {@link JsonObject}.
   ** @param  key                the JSON object member key.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the X.509 certificate chain.
   **                            <br>
   **                            Possible object is array of {@link Encoded}.
   **
   ** @throws IllegalArgumentException if the X.509 certificate chain couldn't
   **                                  be parsed.
   */
  static List<Encoded> certificateChain(final JsonObject object, final String key)
    throws IllegalArgumentException {

    // https://datatracker.ietf.org/doc/html/rfc7517#section-4.7
    final JsonArray array = object.getJsonArray(key);
    final int       size  = array == null ? 0 : array.size();
    final Encoded[] chain = new Encoded[size];
    for (int i = 0; i < size; i++) {
      final String item = array.getString(i);
      if (item == null)
        throw new IllegalArgumentException("The X.509 certificate at position " + i + " must not be null");

      if  (!(item instanceof String))
        throw new IllegalArgumentException("The X.509 certificate must be encoded as a Base64 string");

      chain[i] = new Encoded(item);
    }
    return CollectionUtility.list(chain);
  }
}