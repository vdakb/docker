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

    File        :   SignatureInstance.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SignatureInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;

import javax.json.JsonObject;

import oracle.hst.platform.core.marshal.JsonMarshaller;

import oracle.iam.platform.oauth.AuthorizationException;

////////////////////////////////////////////////////////////////////////////////
// interface SignatureInstance
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** JSON Web Signature (JWS) algorithm name, represents the <code>alg</code>
 ** header parameter in JWS objects. Also used to represent integrity algorithm
 ** (<code>ia</code>) header parameters in JWE objects.
 ** <p>
 ** Includes constants for the following standard JWS algorithm names:
 ** <ul>
 **   <li>{@link Algorithm#HS256}
 **   <li>{@link Algorithm#HS384}
 **   <li>{@link Algorithm#HS512}
 **   <li>{@link Algorithm#RS256}
 **   <li>{@link Algorithm#RS384}
 **   <li>{@link Algorithm#RS512}
 **   <li>{@link Algorithm#ES256}
 **   <li>{@link Algorithm#ES384}
 **   <li>{@link Algorithm#ES512}
 ** </ul>
 ** Additional JWS algorithm names can be defined using the constructors.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SignatureInstance {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Provider
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** Common interface for JSON Web Signature {@link Signer signers} and
   ** {@link Verifier verifiers}.
   ** <p>
   ** Callers can query the JSON Web Signature provider to determine its
   ** algorithm capabilities.
   */
  public static interface Provider {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: supported
    /**
     ** Returns the names of the supported JWS algorithms.
     ** <br>
     ** These correspond to the <code>alg</code> JSON Web Signature header
     ** parameter.
     **
     ** @return                  the collection of supported JSON Web Signature
     **                          algorithms, empty set if none.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Algorithm}.
     */
    Set<Algorithm> supported();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Signer
  // ~~~~~~~~~ ~~~~~~
  /**
   ** Common interfacefor signing JSON Web Signature (JWS) objects.
   ** <p>
   ** Callers can query the JSON Web Signature signer to determine its algorithm
   ** capabilities.
   */
  public static interface Signer extends Provider {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: sign
    /**
     ** Signs the specified {@link SignatureObject#content() signable content}
     ** of a {@link SignatureObject JWS object}.
     **
     ** @param header            the JSON Web Signature (JWS) header.
     **                          Must specify a supported JWS algorithm and must
     **                          not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link SignatureHeader}.
     ** @param content           the content to sign.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @return                  the resulting signature part (third part) of
     **                          the JWS object.
     **                          <br>
     **                          Possible object is {@link EncodedURL}.
     **
     ** @throws AuthorizationException if the JWS algorithm is not supported or
     **                                if signing failed for some other reason.
     */
    public EncodedURL sign(final SignatureHeader header, final byte[] content)
      throws AuthorizationException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Verifier
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** Common interface for signing JSON Web Signature (JWS) objects.
   ** <p>
   ** Callers can query the verifier to determine its algorithm capabilities as
   ** well as the JWS algorithms and header parameters that are accepted for
   ** processing.
   */
  public static interface Verifier extends Provider {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter
    /**
     ** Returns the Json Web Signature (JWS) header filter associated with the
     ** verifier.
     ** <br>
     ** Specifies the names of those
     ** {@link #supported() supported JWS algorithms} and header parameters that
     ** the verifier is configured to accept.
     ** <p>
     ** Attempting to {@link #verify(SignatureHeader, byte[], EncodedURL) verify}
     ** a JWS object signature with an algorithm or header parameter that is not
     ** accepted must result in a {@link AuthorizationException}.
     **
     ** @return                  the Json Web Signature (JWS) header filter
     **                          associated with the verifier
     **                          <br>
     **                          Possible object is
     **                          {@link SignatureHeaderFilter}.
     */
    SignatureHeaderFilter filter();

    ////////////////////////////////////////////////////////////////////////////
    // Method: verify
    /**
     ** Verifies the specified {@link SignatureObject#signature() signature} of
     ** a {@link SignatureObject JWS object}.
     **
     ** @param  header           the JSON Web Signature (JWS) header.
     **                          <br>
     **                          Must specify an accepted JWS algorithm and must
     **                          contain only accepted header parameters,
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link SignatureHeader}.
     ** @param  content          the signed content.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     ** @param signature         the signature subject of the JWS object.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  <code>true</code> if the signature was
     **                          successfully verified; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws AuthorizationException if the JWS algorithm is not accepted, if
     **                                a header parameter is not accepted, or if
     **                                signature verification failed for some
     **                                other reason.
     */
    boolean verify(final SignatureHeader header, final byte[] content, final EncodedURL signature)
      throws AuthorizationException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method: from
  /**
   ** Factory method to create an {@link Algorithm} specified JSON object
   ** representation utilized by this <code>SignatureInstance</code> for
   ** signing/verification purpose.
   **
   ** @param  object             the JSON objects to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the cryptographic {@link Algorithm} matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is {@link Algorithm}.
   **
   ** @throws NullPointerException     if <code>object</code> is
   **                                  <code>null</code> or.
   ** @throws IllegalArgumentException if no matching
   **                                  <code>SignatureInstance</code> could be
   **                                  found.
   */
  public static Algorithm from(final JsonObject object) {
    return from(JsonMarshaller.stringValue(object, Header.ALG));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses an {@link Algorithm} from the specified string utilized by this
   ** <code>SignatureInstance</code> for signing/verification purpose.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            May be be <code>null</code> but than the return
   **                            type is also <code>null</code>.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            An empty string will treaten like a valid
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the cryptographic {@link Algorithm} matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is {@link Algorithm}.
   **
   ** @throws IllegalArgumentException if <code>value</code> is not
   **                                  <code>null</code> but no match found.
   */
  public static Algorithm from(final String value) {
    if (value == null)
      return null;
    else if (Algorithm.HS256.id.equals(value))
      return Algorithm.HS256;
    else if (Algorithm.HS384.id.equals(value))
      return Algorithm.HS384;
    else if (Algorithm.HS512.id.equals(value))
      return Algorithm.HS512;
    else if (Algorithm.RS256.id.equals(value))
      return Algorithm.RS256;
    else if (Algorithm.RS384.id.equals(value))
      return Algorithm.RS384;
    else if (Algorithm.RS512.id.equals(value))
      return Algorithm.RS512;
    else if (Algorithm.ES256.id.equals(value))
      return Algorithm.ES256;
    else if (Algorithm.ES384.id.equals(value))
      return Algorithm.ES384;
    else if (Algorithm.ES512.id.equals(value))
      return Algorithm.ES512;
    else
      throw new IllegalArgumentException(JoseBundle.string(JoseError.JOSE_ALGORITHM_UNEXPECTED, value));
  }
}