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

    File        :   Curve.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Curve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Map;
import java.util.Objects;

import java.math.BigInteger;

import java.security.spec.ECPoint;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;
import java.security.spec.ECParameterSpec;

import javax.json.JsonObject;

import oracle.hst.platform.core.marshal.JsonEnum;
import oracle.hst.platform.core.marshal.JsonMarshaller;

import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// enum Curve
// ~~~~ ~~~~~
/**
 ** Cryptographic curve.
 ** <p>
 ** Includes constants for the following standard cryptographic curves:
 ** <ul>
 **   <li>{@link #P256 P-256}
 **   <li>{@link #SECP256K1 SECP256K1}
 **   <li>{@link #P384 P-384}
 **   <li>{@link #P521 P-521}
 **   <li>{@link #ED25519 Ed25519}
 **   <li>{@link #ED448 Ed448}
 **   <li>{@link #X25519 X25519}
 **   <li>{@link #X448 X448}
 ** </ul>
 ** See
 ** <ul>
 **   <li>"Digital Signature Standard (DSS)",
 **        FIPS PUB 186-3, June 2009,
 **        National Institute of Standards and Technology (NIST).
 **   <li>CFRG Elliptic Curve Diffie-Hellman (ECDH) and Signatures in JSON
 **       Object Signing and Encryption (JOSE) (RFC 8037).
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum Curve implements JsonEnum<Curve, String> {
    /**
     ** P-256 curve (OID = 1.2.840.10045.3.1.7).
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-7.6.2">RFC 7518, section 7.6.2. Initial Registry Contents</a>
     */
    P256("P-256", "1.2.840.10045.3.1.7")
    /**
     ** P-384 curve (OID = 1.3.132.0.34).
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-7.6.2">RFC 7518, section 7.6.2. Initial Registry Contents</a>
     */
  , P384("P-384", "1.3.132.0.34")
    /**
     ** P-521 curve (OID = 1.3.132.0.35).
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-7.6.2">RFC 7518, section 7.6.2. Initial Registry Contents</a>
     */
  , P521("P-521", "1.3.132.0.35")
    /**
     ** secp256k1 curve (OID = 1.3.132.0.10).
     */
  , SECP256K1("secp256k1", "1.3.132.0.10")
    /**
     ** Ed448 signature algorithm key pairs (OID = null).
     */
  , ED448("Ed448", null)
    /**
     ** Ed25519 signature algorithm key pairs (OID = null).
     */
  , ED25519("Ed25519", null)
    /**
     ** X448 function key pairs (OID = null).
     */
  , X448("X448", null)
    /**
     ** X25519 function key pairs (OID = null).
     */
  , X25519("X25519", null)
  ;

  ////////////////////////////////////////////////////////////////////////////
  // static final attributes
  ////////////////////////////////////////////////////////////////////////////

  /**
   ** JWK parameter for EC curve.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.2.1.1">RFC 7518, section 6.2.1.1. "crv" (Curve) Parameter</a>
   **
   ** @see Curve#P256
   ** @see Curve#P384
   ** @see Curve#P521
   */
  static final String                       TAG = "crv";

  static final ECParameterSpec P256S = new ECParameterSpec(
    new EllipticCurve(
      new ECFieldFp(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"))
    , new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948")
    , new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291")
    )
  , new ECPoint(
      new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286")
    , new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109")
    )
  , new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369")
  , 1
  );

  static final ECParameterSpec P384S = new ECParameterSpec(
    new EllipticCurve(
      new ECFieldFp(new BigInteger("39402006196394479212279040100143613805079739270465446667948293404245721771496870329047266088258938001861606973112319"))
    , new BigInteger("39402006196394479212279040100143613805079739270465446667948293404245721771496870329047266088258938001861606973112316")
    , new BigInteger("27580193559959705877849011840389048093056905856361568521428707301988689241309860865136260764883745107765439761230575")
    )
  , new ECPoint(
      new BigInteger("26247035095799689268623156744566981891852923491109213387815615900925518854738050089022388053975719786650872476732087")
    , new BigInteger("8325710961489029985546751289520108179287853048861315594709205902480503199884419224438643760392947333078086511627871")
    )
  , new BigInteger("39402006196394479212279040100143613805079739270465446667946905279627659399113263569398956308152294913554433653942643")
  , 1
  );

  static final ECParameterSpec P521S = new ECParameterSpec(
    new EllipticCurve(
      new ECFieldFp(new BigInteger("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151"))
    , new BigInteger("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057148")
    , new BigInteger("1093849038073734274511112390766805569936207598951683748994586394495953116150735016013708737573759623248592132296706313309438452531591012912142327488478985984")
    )
  , new ECPoint(
      new BigInteger("2661740802050217063228768716723360960729859168756973147706671368418802944996427808491545080627771902352094241225065558662157113545570916814161637315895999846")
    , new BigInteger("3757180025770020463545507224491183603594455134769762486694567779615544477440556316691234405012945539562144444537289428522585666729196580810124344277578376784")
    )
  , new BigInteger("6864797660130609714981900799081393217269435300143305409394463459185543183397655394245057746333217197532963996371363321113864768612440380340372808892707005449")
  , 1
  );

  static final ECParameterSpec SECP256K1S = new ECParameterSpec(
    new EllipticCurve(
      new ECFieldFp(new BigInteger("115792089237316195423570985008687907853269984665640564039457584007908834671663"))
    , new BigInteger("0")
    , new BigInteger("7")
    )
  , new ECPoint(
      new BigInteger("55066263022277343669578718895168534326250603453777594175500187360389116729240")
    , new BigInteger("32670510020758816978083085130507043184471273380659243275938904335757337482424")
    )
  , new BigInteger("115792089237316195423570985008687907852837564279074904382605163141518161494337")
  , 1
  );

  // maps named curves to EC parameters specifications
  static final Map<String, ECParameterSpec> MAP = CollectionUtility.map(
    P256.id,      P256S
  , P384.id,      P384S
  , P521.id,      P521S
  , SECP256K1.id, SECP256K1S
  );

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The JOSE curve name. */
  public final String id;

  /** The standard object identifier for the curve */
  private final String oid;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new cryptographic curve with the specified name.
   **
   ** @param  id                 the identifier of the cryptographic curve.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  oid                the object identifier (OID) of the
   **                            cryptographic  curve.
   **                            <br>
   **                            May be null.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Curve(final String id, final String oid) {
    // initialize instance attributes
    this.id  = id;
    this.oid = oid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: of
  /**
   ** Factory method to create a proper <code>Curve</code> constraint from
   ** the given string value.
   **
   ** @param  value              the string value the type constraint should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Curve</code> constraint.
   **                            <br>
   **                            Possible object is <code>Curve</code>.
   **
   ** @throws IllegalArgumentException if no matching <code>Curve</code> could
   **                                  be found.
   */
  @Override
  public final Curve of(final String value) {
    return from(value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: id
  /**
   ** Returns the value that identifies the <code>JsonEnum</code>
   **
   ** @return                    a value that can be used later in
   **                            {@link #of(Object)}
   **                            <br>
   **                            Possible object is {@link String}.
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
   ** Factory method to create a public Curve JWK from the specified JSON
   ** object representation.
   **
   ** @param  object             the JSON objects to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the parsed cryptographic <code>Curve</code>.
   **                            <br>
   **                            Possible object is <code>Curve</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code> or.
   ** @throws IllegalArgumentException if no matching <code>Curve</code> could
   **                                  be found.
   */
  public static Curve from(final JsonObject object) {
    return from(JsonMarshaller.stringValue(object, TAG));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: from
  /**
   ** Lookup method to return a cryptographic <code>Curve</code> algorithm from
   ** the specified string representation.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed cryptographic <code>Curve</code>.
   **                            <br>
   **                            Possible object is <code>Curve</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code> or.
   ** @throws IllegalArgumentException if no matching <code>Curve</code> could
   **                                  be found.
   */
  public static Curve from(final String value) {
    // prevent bogus input
    Objects.requireNonNull(value, "The curve algorithm string must not be null");

    for (Curve cursor : Curve.values()) {
      if (cursor.id.equals(value))
       return cursor;
    }
    throw new IllegalArgumentException(value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: from
  /**
   ** Lookup method to return a cryptographic <code>Curve</code> algorithm from
   ** the specified {@link ECParameterSpec} <code>value</code>.
   **
   ** @param  value              the {@link ECParameterSpec} to parse.
   **                            <br>
   **                            Allowed object is {@link ECParameterSpec}.
   **
   ** @return                    the parsed cryptographic <code>Curve</code>.
   **                            <br>
   **                            Possible object is <code>Curve</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code> or.
   ** @throws IllegalArgumentException if no matching <code>Curve</code> could
   **                                  be found.
   */
  public static Curve from(final ECParameterSpec value) {
    // prevent bogus input
    if (value == null)
      return null;

    if (value.getCurve().getField().getFieldSize() == P256S.getCurve().getField().getFieldSize()
     && value.getCurve().getA().equals(P256S.getCurve().getA())
     && value.getCurve().getB().equals(P256S.getCurve().getB())
     && value.getGenerator().getAffineX().equals(P256S.getGenerator().getAffineX())
     && value.getGenerator().getAffineY().equals(P256S.getGenerator().getAffineY())
     && value.getOrder().equals(P256S.getOrder())
     && value.getCofactor() == P256S.getCofactor()) {
      return Curve.P256;
    }
    else if (value.getCurve().getField().getFieldSize() == P384S.getCurve().getField().getFieldSize()
     && value.getCurve().getA().equals(P384S.getCurve().getA())
     && value.getCurve().getB().equals(P384S.getCurve().getB())
     && value.getGenerator().getAffineX().equals(P384S.getGenerator().getAffineX())
     && value.getGenerator().getAffineY().equals(P384S.getGenerator().getAffineY())
     && value.getOrder().equals(P384S.getOrder())
     && value.getCofactor() == P384S.getCofactor()) {
      return Curve.P384;
    }
    else if (value.getCurve().getField().getFieldSize() == P521S.getCurve().getField().getFieldSize()
     && value.getCurve().getA().equals(P521S.getCurve().getA())
     && value.getCurve().getB().equals(P521S.getCurve().getB())
     && value.getGenerator().getAffineX().equals(P521S.getGenerator().getAffineX())
     && value.getGenerator().getAffineY().equals(P521S.getGenerator().getAffineY())
     && value.getOrder().equals(P521S.getOrder())
     && value.getCofactor() == P521S.getCofactor()) {
      return Curve.P521;
    }
    else if (value.getCurve().getField().getFieldSize() == SECP256K1S.getCurve().getField().getFieldSize()
     && value.getCurve().getA().equals(SECP256K1S.getCurve().getA())
     && value.getCurve().getB().equals(SECP256K1S.getCurve().getB())
     && value.getGenerator().getAffineX().equals(SECP256K1S.getGenerator().getAffineX())
     && value.getGenerator().getAffineY().equals(SECP256K1S.getGenerator().getAffineY())
     && value.getOrder().equals(SECP256K1S.getOrder())
     && value.getCofactor() == SECP256K1S.getCofactor()) {
      return Curve.SECP256K1;
    }
    else {
      return null;
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: oid
  /**
   ** Lookup method to return a cryptographic Curve Algorithm from the specified
   ** string representation.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed cryptographic <code>Curve</code>.
   **                            <br>
   **                            Possible object is <code>Curve</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code> or.
   ** @throws IllegalArgumentException if no matching <code>Curve</code> could
   **                                  be found.
   */
  public static Curve oid(final String value) {
    // prevent bogus input
    Objects.requireNonNull(value, "The curve algorithm string must not be null");

    for (Curve cursor : Curve.values()) {
      if (cursor.oid != null && cursor.oid.equals(value))
       return cursor;
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: spec
  /**
   ** Returns the parameter specification for this cryptographic curve.
   **
   ** @return                    the cryptographic Curve parameter
   **                            specification or <code>null</code> if it cannot
   **                            be determined.
   **                            <br>
   **                            Possible object is {@link ECParameterSpec}.
   */
  public ECParameterSpec spec() {
    return MAP.get(this.id);
  }
}