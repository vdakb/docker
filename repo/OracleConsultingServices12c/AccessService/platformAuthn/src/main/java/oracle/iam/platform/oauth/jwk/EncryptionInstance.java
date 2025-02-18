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

    File        :   EncryptionInstance.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    EncryptionInstance.


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
// interface EncryptionInstance
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** JSON Web Encryption (JWE) algorithm name, represents the , represents the
 ** <code>alg</code> header parameter in JWE objects.
 ** <p>
 ** Includes constants for the following standard JWE algorithm names:
 ** <ul>
 **   <li>{@link Algorithm#RSA1_5}
 **   <li>{@link Algorithm#RSA_OAEP RSA-OAEP}
 **   <li>{@link Algorithm#A128KW}
 **   <li>{@link Algorithm#A256KW}
 **   <li>{@link Algorithm#DIRECT dir}
 **   <li>{@link Algorithm#ECDH_ES ECDH-ES}
 **   <li>{@link Algorithm#ECDH_ES_A128KW ESDH-ES+A128KW}
 **   <li>{@link Algorithm#ECDH_ES_A256KW ESDH-ES+A256KW}
 ** </ul>
 ** Additional JWE algorithm names can be defined using the constructors.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface EncryptionInstance {

  //////////////////////////////////////////////////////////////////////////////
  // interface Provider
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** Common interface for JSON Web Encryption {@link Encrypter encrypters} and
   ** {@link Decrypter decrypters}.
   ** <p>
   ** Callers can query the JsonWebSignature provider to determine its algorithm
   ** capabilities.
   */
  public static interface Provider {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: supported
    /**
     ** Returns the names of the supported JWE algorithms.
     ** <br>
     ** These correspond to the <code>alg</code> JSON Web Encryption header
     ** parameter.
     **
     ** @return                  the collection of supported JSON Web Encryption
     **                          algorithms, empty set if none.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Algorithm}.
     */
    Set<Algorithm> supported();

    ////////////////////////////////////////////////////////////////////////////
    // Method: method
    /**
     ** Returns the names of the supported JWE methods.
     ** <br>
     ** These correspond to the <code>enc</code> JSON Web Encryption header
     ** parameter.
     **
     ** @return                  the collection of supported JSON Web Encryption
     **                          methods or empty set if none.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Algorithm}.
     */
    Set<Algorithm> method();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Encrypter
  // ~~~~~~~~~ ~~~~~~~~~
  /**
   ** Encrypting JSON Web Encryption (JWE) objects.
   ** <p>
   ** Callers can query the encrypter to determine its algorithm capabilities as
   ** well as the JWE algorithms and header parameters that are accepted for
   ** processing.
   */
  public static interface Encrypter extends Provider {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: encrypt
    /**
     ** Encrypts the specified clear text of a
     ** {@link EncryptionObject JWE object}.
     **
     ** @param  header           the JSON Web Encryption (JWE) header.
     **                          <br>
     **                          Must specify a supported JWE algorithm.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncryptionHeader}.
     ** @param  clear            the clear text to encrypt.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @return                  the resulting JWE crypto parts.
     **                          <br>
     **                          Allowed object is
     **                          {@link EncryptionObject.Subject}.
     **
     ** @throws AuthorizationException if the JWE algorithm is not supported or
     **                                if encryption failed for some other
     **                                reason.
     */
    public EncryptionObject.Subject encrypt(final EncryptionHeader header, final byte[] clear)
      throws AuthorizationException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Decrypter
  // ~~~~~~~~~ ~~~~~~~~~
  /**
   ** Decrypting JSON Web Encryption (JWE) objects.
   ** <p>
   ** Callers can query the decrypter to determine its algorithm capabilities as
   ** well as the JWE algorithms and header parameters that are accepted for
   ** processing.
   */
  public static interface Decrypter {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter
    /**
     ** Returns the JWE header filter associated with the decrypter.
     ** <br>
     ** Specifies the names of those {@link Provider#supported() supported JWE
     ** algorithms} and header parameters that the decrypter is configured to
     ** accept.
     ** <p>
     ** Attempting to {@link #decrypt decrypt} a JWE object with an algorithm or
     ** header parameter that is not accepted must result in a
     ** {@link AuthorizationException}.
     **
     ** @return                  the header filter.
     **                          <br>
     **                          Possible object is
     **                          {@link EncryptionHeaderFilter}.
     */
    EncryptionHeaderFilter filter();

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrypt
    /**
     ** Decrypts the specified cipher text of a
     ** {@link EncryptionObject JWE object}.
     **
     ** @param header            the JSON Web Encryption (JWE) header.
     **                          <br>
     **                          Must specify a supported JWE algorithm and must
     **                          contain only accepted header parameters.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncryptionHeader}.
     ** @param  subject          the encryption subject to process.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is
     **                          {@link EncryptionObject.Subject}.
     **
     ** @return                  the clear text message.
     **                          <br>
     **                          Possible object is array of <code>byte</code>.
     **
     ** @throws AuthorizationException if the JWE algorithm is not accepted, if
     **                                a header parameter is not accepted, or if
     **                                decryption failed for some other reason.
     */
    public byte[] decrypt(final EncryptionHeader header, final EncryptionObject.Subject subject)
      throws AuthorizationException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method: from
  /**
   ** Factory method to create an {@link Algorithm} specified JSON object
   ** representation utilized by this <code>EncryptionInstance</code> for
   ** encryption/decryption purpose.
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
   **                                  <code>EncryptionInstance</code> could be
   **                                  found.
   */
  public static Algorithm from(final JsonObject object) {
    return from(JsonMarshaller.stringValue(object, Header.ALG));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses an <code>EncryptionInstance</code> from the specified string
   ** utilized by this <code>EncryptionInstance</code> for
   ** encryption/decryption purpose.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            May be be <code>null</code> but than the return
   **                            type is also <code>null</code>.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            An empty string will treaten like a valid
   **                            value.
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
    else if (Algorithm.RSA1_5.id.equals(value))
      return Algorithm.RSA1_5;
    else if (Algorithm.RSA_OAEP.id.equals(value))
      return Algorithm.RSA_OAEP;
    else if (Algorithm.A128KW.id.equals(value))
      return Algorithm.A128KW;
    else if (Algorithm.A256KW.id.equals(value))
      return Algorithm.A256KW;
    else if (Algorithm.DIRECT.id.equals(value))
      return Algorithm.DIRECT;
    else if (Algorithm.ECDH_ES.id.equals(value))
      return Algorithm.ECDH_ES;
    else if (Algorithm.ECDH_ES_A128KW.id.equals(value))
      return Algorithm.ECDH_ES_A128KW;
    else if (Algorithm.ECDH_ES_A256KW.id.equals(value))
      return Algorithm.ECDH_ES_A256KW;
    else
      throw new IllegalArgumentException(JoseBundle.string(JoseError.JOSE_ALGORITHM_UNEXPECTED, value));
  }
}