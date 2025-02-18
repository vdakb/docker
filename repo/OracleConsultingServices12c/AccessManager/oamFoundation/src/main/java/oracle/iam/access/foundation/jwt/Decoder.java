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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Decoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Decoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

import java.util.Map;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

import java.util.function.Function;

import java.nio.charset.StandardCharsets;

////////////////////////////////////////////////////////////////////////////////
// class Decoder
// ~~~~~ ~~~~~~~
/**
 ** ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Decoder {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static Decoder instance;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Decoder</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Decoder() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Return a singleton instance of the JSON Web Token Decoder.
   **
   ** @return                    the one and only instance of the JSON Web Token
   **                            decoder.
   */
  public static Decoder instance() {
    if (instance == null) {
      instance = new Decoder();
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   base64Decode
  /**
   ** Decode the provided base64 encoded string.
   **
   ** @param  string             the input string to decode, it is expected to
   **                            be a valid base64 encoded string.
   **
   ** @return                    a decoded byte array.
   */
  public static byte[] base64Decode(final String string) {
    try {
      // equal to calling : .decode(string.getBytes(StandardCharsets.ISO_8859_1))
      // if this is a properly base64 encoded string, decoding using ISO_8859_1 should be fine.
      return Base64.getDecoder().decode(string.replace('-', '+').replace('_', '/'));
    }
    catch (IllegalArgumentException e) {
      throw new InvalidTokenException("The encoded JSON Web Token is not properly Base64 encoded.", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode the JSON Web {@link Token} using one of they provided verifiers.
   ** A JSON Web Token  header value named <code>kid</code> is expected to
   ** contain the key to lookup the correct verifier.
   ** <p>
   ** A JSON Web {@link Token} that is expired or not yet valid will not be
   ** decoded, instead a {@link TokenExpiredException} or
   ** {@link TokenUnavailableException} exception will be thrown respectively.
   **
   ** @param  encoded            the encoded JSON Web Token  in string format.
   ** @param  verifiers          a map of verifiers.
   **
   ** @return                    a decoded JSON Web Token.
   */
  public Token decode(final String encoded, final Map<String, Verifier> verifiers) {
    return decode(encoded, verifiers, h -> h.get("kid"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode the JSON Web {@link Token} using one of they provided verifiers.
   ** <br>
   ** One more verifiers may be provided, the first verifier found supporting
   ** the algorithm reported by the JSON Web Token  header will be utilized.
   ** <p>
   ** A JSON Web {@link Token} that is expired or not yet valid will not be
   ** decoded, instead a {@link TokenExpiredException} or
   ** {@link TokenUnavailableException} exception will be thrown respectively.
   **
   ** @param  encoded            the encoded JSON Web {@link Token} in string
   **                            format.
   ** @param  verifiers          a array of verifiers.
   **
   ** @return                    a decoded JSON Web {@link Token}.
   */
  public Token decode(final String encoded, final Verifier... verifiers) {
    Objects.requireNonNull(encoded);
    Objects.requireNonNull(verifiers);

    final String[] parts    = parts(encoded);
    final Header   header   = Mapper.deserialize(base64Decode(parts[0]), Header.class);
    final Verifier verifier = Arrays.stream(verifiers).filter(v -> v.accept(header.algorithm)).findFirst().orElse(null);

    // The 'none' algorithm is only allowed when no verifiers are provided.
    boolean allowNoneAlgorithm = verifiers.length == 0;
    return validate(encoded, parts, header, verifier, allowNoneAlgorithm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode the JSON Web {@link Token} using one of the provided verifiers.
   ** <br>
   ** The key used to lookup the correct verifier is provided by the
   ** <code>function</code>. The key function is provided the JSON Web
   ** {@link Token} header and is expected to return a string key to look up the
   ** correct verifier.
   ** <p>
   ** A JSON Web {@link Token} that is expired or not yet valid will not be
   ** decoded, instead a {@link TokenExpiredException} or
   ** {@link TokenUnavailableException} exception will be thrown respectively.
   **
   ** @param  encoded            the encoded JSON Web Token  in string format.
   ** @param  verifiers          a map of verifiers.
   ** @param  function           a function used to lookup the verifier key from
   **                            the header.
   **
   ** @return                    a decoded JSON Web Token.
   */
  public Token decode(final String encoded, final Map<String, Verifier> verifiers, final Function<Header, String> function) {
    Objects.requireNonNull(encoded);
    Objects.requireNonNull(verifiers);
    Objects.requireNonNull(function);

    final String[] parts    = parts(encoded);
    final Header   header   = Mapper.deserialize(base64Decode(parts[0]), Header.class);
    final String   key      = function.apply(header);
    final Verifier verifier = verifiers.get(key);

    // The 'none' algorithm is only allowed when no verifiers are provided.
    boolean none = verifiers.isEmpty();
    return validate(encoded, parts, header, verifier, none);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parts
  /**
   ** Split the encoded JSON Web Token on a period (.), and return the parts.
   ** <p>
   ** A secured JSON Web Token will be in the format:
   ** <code>XXXXX.YYYYY.ZZZZZ</code> and an un-secured JSON Web Token (no
   ** signature) will be in the format <code>XXXXX.YYYYY</code>.
   **
   ** @param  encoded            the encoded form of the JSON Web Token.
   **
   ** @return                    an array of parts, 2 for an un-secured JSON Web
   **                            Token, and 3 parts for a secured JSON Web Token.
   */
  private String[] parts(final String encoded) {
    final String[] parts = encoded.split("\\.");
    if (parts.length == 3 || (parts.length == 2 && encoded.endsWith("."))) {
      return parts;
    }
    throw new InvalidTokenException("The encoded JSON Web Token is not properly formatted. Expected a three part dot separated string.");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validate the encoded JSON Web Token and return the constructed
   ** {@link Token} object if valid.
   **
   ** @param  encoded          the encoded JSON Web Token.
   ** @param  parts            the parts of the encoded JSON Web Token.
   ** @param  header           the JSON Web Token header.
   ** @param  verifier         the selected JSON Web Token verifier.
   ** @param  none             <code>true</code> if un-secured JSON Web
   **                          Tokens may be decoded, i.e. 'none'
   **                          algorithm is allowed.
   **
   ** @return                  the constructed JSON Web Token object
   **                          containing identity claims.
   */
  private Token validate(final String encoded, final String[] parts, final Header header, final Verifier verifier, final boolean none) {
    // when parts.length == 2, we have no signature.
    //  - case 1: If one or more verifiers are provided, we will not decode an
    //            un-secured JSON Web Token. Throw NoneNotAllowedException
    //  - case 2: If no verifiers are provided, we will decode an un-secured
    //            JSON Web Token, the algorithm must be 'none'.
    if (parts.length == 2) {
      if (!none) {
        throw new NoneNotAllowedException();
      }

      if (header.algorithm != Algorithm.none)
        throw new MissingSignatureException("Your provided a JSON Web Token with the algorithm [" + header.algorithm.type() + "] but it is missing a signature");
    }
    else {
      // When parts.length == 3, we have a signature.
      // - Case 1: The algorithm in the header is 'none', we do not expect a
      //           signature.
      // - Case 2: No verifier was provided that can verify the algorithm in the
      //           header, or no verifier found by the kid in the header
      // - Case 3: The requested verifier cannot verify the signature based upon
      //           the algorithm value in the header
      if (header.algorithm == Algorithm.none)
        throw new InvalidTokenException("You provided a JSON Web Token with a signature and an algorithm of none");

      if (verifier == null)
        throw new MissingVerifierException("No Verifier has been provided for verify a signature signed using [" + header.algorithm.type() + "]");

      // When the verifier has been selected based upon the 'kid' or other
      // identifier in the header, we must verify it can accept the algorithm.
      // - When multiple verifiers are provided to .decode w/out a kid, we may
      //   have already called 'accept', this is ok.
      if (!verifier.accept(header.algorithm))
        throw new MissingVerifierException("No Verifier has been provided for verify a signature signed using [" + header.algorithm.type() + "]");

      verifySignature(verifier, header, parts[2], encoded);
    }

    // Signature is valid or there is no signature to validate for an un-secured
    // JSON Web Token, verify time based JSON Web Token  claims
    Token token = Mapper.deserialize(base64Decode(parts[1]), Token.class);

    // Verify expiration claim
    if (token.expired()) {
      throw new TokenExpiredException();
    }

    // Verify the notBefore claim
    if (token.isUnavailableForProcessing()) {
      throw new TokenUnavailableException();
    }

    return token;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifySignature
  /**
   ** Verify the signature of the encoded JSON Web Token.
   ** <br>
   ** If the signature is invalid a {@link InvalidTokenSignatureException} will be thrown.
   **
   ** @param  verifier           the verifier
   ** @param  header             the JSON Web Token header
   ** @param  signature          the JSON Web Token signature
   ** @param  encoded            the encoded JSON Web Token
   **
   ** @throws InvalidTokenSignatureException if the JSON Web Token signature is invalid.
   */
  private void verifySignature(final Verifier verifier, final Header header, final String signature, final String encoded) {
    // the message comprises the first two segments of the entire JSON Web Token,
    // the signature is the last segment.
    int    index          = encoded.lastIndexOf(".");
    byte[] message        = encoded.substring(0, index).getBytes(StandardCharsets.UTF_8);
    byte[] signatureBytes = base64Decode(signature);
    verifier.verify(header.algorithm, message, signatureBytes);
  }
}