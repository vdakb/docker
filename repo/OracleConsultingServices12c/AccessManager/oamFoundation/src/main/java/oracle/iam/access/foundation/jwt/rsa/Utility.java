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

    File        :   Utility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Utility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt.rsa;

import java.util.Base64;

import java.io.IOException;
import java.io.ByteArrayInputStream;

import java.nio.charset.Charset;

import java.math.BigInteger;

import java.security.Key;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.GeneralSecurityException;
import java.security.InvalidParameterException;

import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;

import sun.security.util.DerValue;
import sun.security.util.DerInputStream;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.access.foundation.jwt.PEMUtility;

////////////////////////////////////////////////////////////////////////////////
// class Utility
// ~~~~~ ~~~~~~~
/**
 ** RSA Key Helper class.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Utility {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertFingerprintToThumbprint
  /**
   ** Convert a HEX <code>SHA-1</code> or <code>SHA-256</code> X.509 certificate
   ** fingerprint to an <code>x5t</code> or <code>x5t#256</code> thumbprint
   ** respectively.
   **
   ** @param  fingerprint        the SHA-1 or SHA-256 fingerprint.
   **
   ** @return                    an x5t hash.
   */
  public static String convertFingerprintToThumbprint(final String fingerprint) {
    final byte[] bytes = StringUtility.toBytes(fingerprint);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertThumbprintToFingerprint
  /**
   ** Convert an X.509 certificate thumbprint to a HEX <code>SHA-1</code> or
   ** <code>SHA-256</code> fingerprint respectively.
   ** <p>
   ** If a <code>x5t</code> thumbprint is provided, a SHA-1 HEX encoded
   ** fingerprint will be returned.
   ** <p>
   ** If a <code>x5t#256</code> thumbprint is provided, a SHA-256 HEX encoded
   ** fingerprint will be returned.
   **
   ** @param  x5tHash            the x5t hash.
   **
   ** @return                    a SHA-1 or SHA-256 fingerprint.
   */
  public static String convertThumbprintToFingerprint(final String x5tHash) {
    final byte[] bytes = Base64.getUrlDecoder().decode(x5tHash.getBytes(Charset.forName("UTF-8")));
    return StringUtility.bytesToString(bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateJWS_x5t
  /**
   ** Generate the <code>x5t</code> - the X.509 certificate thumbprint to be
   ** used in JSON Web Token header.
   **
   ** @param  encodedCertificate the Base64 encoded certificate.
   **
   ** @return                    an x5t hash.
   */
  public static String generateJWS_x5t(final String encodedCertificate) {
    return generateJWS_x5t("SHA-1", encodedCertificate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertFingerprintToThumbprint
  /**
   ** Generate the <code>x5t</code> - the X.509 certificate thumbprint to be
   ** used in JSON Web Token header.
   **
   ** @param  algorithm          the algorithm used to calculate the hash,
   **                            generally SHA-1 or SHA-256.
   ** @param encodedCertificate  the Base64 encoded certificate.
   **
   ** @return                    an x5t hash.
   */
  public static String generateJWS_x5t(final String algorithm, final String encodedCertificate) {
    byte[] bytes = Base64.getDecoder().decode(encodedCertificate.getBytes(Charset.forName("UTF-8")));
    return digest(algorithm, bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateJWS_x5t
  /**
   ** Generate the <code>x5t</code> - the X.509 certificate thumbprint to be
   ** used in JSON Web Token header.
   **
   ** @param derEncodedCertificate the DER encoded certificate.
   **
   ** @return an x5t hash.
   */
  public static String generateJWS_x5t(byte[] derEncodedCertificate) {
    return digest("SHA-1", derEncodedCertificate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateJWS_x5t
  /**
   ** Generate the <code>x5t</code> - the X.509 certificate thumbprint to be
   ** used in JSON Web Token header.
   **
   ** @param  algorithm          the algorithm used to calculate the hash,
   **                            generally SHA-1 or SHA-256.
   ** @param derEncodedCertificate the DER encoded certificate.
   **
   ** @return an x5t hash.
   */
  public static String generateJWS_x5t(final String algorithm, final byte[] derEncodedCertificate) {
    return digest(algorithm, derEncodedCertificate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pemFromPrivateKey
  /**
   ** Return the private key in a PEM formatted String.
   **
   ** @param  privateKey        the private key.
   **
   ** @return                    a string in PEM format.
   */
  public static String pemFromPrivateKey(final PrivateKey privateKey) {
    return pemFromKey(privateKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pemFromPublicKey
  /**
   ** Return the public key in a PEM formatted String.
   **
   ** @param  publicKey          the publicKey key.
   **
   ** @return                    a string in PEM format.
   */
  public static String pemFromPublicKey(final PublicKey publicKey) {
    return pemFromKey(publicKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   privateKeyFromPEM
  /**
   ** Return a Private Key from the provided private key in PEM format.
   **
   ** @param  privateKey         the private key in string format.
   **
   ** @return                    a private key
   */
  public static RSAPrivateKey privateKeyFromPEM(final String privateKey) {
    try {
      final KeySpec keySpec = rsaPrivateKeySpec(privateKey);
      return (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }
    catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicKeyFromPEM
  /**
   ** Return a PublicKey from the public key pem string.
   **
   ** @param  publicKey          the public in PEM format.
   **
   ** @return                    the public key
   */
  public static RSAPublicKey publicKeyFromPEM(final String publicKey) {
    try {
      return extractPublicKeyFromPEM(publicKey);
    }
    catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest
  private static String digest(final String algorithm, final byte[] bytes) {
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance(algorithm);
    }
    catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("No such algorithm [" + algorithm + "]");
    }

    byte[] digest = messageDigest.digest(bytes);
    return new String(Base64.getUrlEncoder().withoutPadding().encode(digest));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extractPublicKeyFromPEM
  private static RSAPublicKey extractPublicKeyFromPEM(final String publicKeyString)
    throws IOException
    ,      GeneralSecurityException {

    if (publicKeyString.contains(PEMUtility.PKCS_1_PUBLIC_KEY_PREFIX)) {
      byte[] bytes = keyBytes(publicKeyString, PEMUtility.PKCS_1_PUBLIC_KEY_PREFIX, PEMUtility.PKCS_1_PUBLIC_KEY_SUFFIX);
      @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
      final DerInputStream derReader = new DerInputStream(bytes);
      @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
      final DerValue[]     seq = derReader.getSequence(0);
      if (seq.length != 2) {
        throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
      }

      BigInteger modulus        = seq[0].getBigInteger();
      BigInteger publicExponent = seq[1].getBigInteger();
      return (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
    }
    else if (publicKeyString.contains(PEMUtility.X509_PUBLIC_KEY_PREFIX)) {
      byte[] bytes = keyBytes(publicKeyString, PEMUtility.X509_PUBLIC_KEY_PREFIX, PEMUtility.X509_PUBLIC_KEY_SUFFIX);
      return (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
    }
    else if (publicKeyString.contains(PEMUtility.X509_CERTIFICATE_PREFIX)) {
      byte[] bytes = keyBytes(publicKeyString, PEMUtility.X509_CERTIFICATE_PREFIX, PEMUtility.X509_CERTIFICATE_SUFFIX);
      final CertificateFactory factory     = CertificateFactory.getInstance("X.509");
      final Certificate        certificate = factory.generateCertificate(new ByteArrayInputStream(bytes));
      return (RSAPublicKey)certificate.getPublicKey();
    }
    else {
      throw new InvalidParameterException("Unexpected Public Key Format");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rsaPrivateKeySpec
  private static KeySpec rsaPrivateKeySpec(final String privateKey)
    throws IOException
    ,      GeneralSecurityException {

    if (privateKey.contains(PEMUtility.PKCS_1_PRIVATE_KEY_PREFIX)) {
      byte[] bytes = keyBytes(privateKey, PEMUtility.PKCS_1_PRIVATE_KEY_PREFIX, PEMUtility.PKCS_1_PRIVATE_KEY_SUFFIX);
      @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
      final DerInputStream derReader = new DerInputStream(bytes);
      @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
      final DerValue[]     sequence  = derReader.getSequence(0);
      if (sequence.length < 9) {
        throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
      }

      // skip version sequence[0];
      BigInteger modulus         = sequence[1].getBigInteger();
      BigInteger publicExponent  = sequence[2].getBigInteger();
      BigInteger privateExponent = sequence[3].getBigInteger();
      BigInteger primeP          = sequence[4].getBigInteger();
      BigInteger primeQ          = sequence[5].getBigInteger();
      BigInteger primeExponentP  = sequence[6].getBigInteger();
      BigInteger primeExponentQ  = sequence[7].getBigInteger();
      BigInteger crtCoefficient  = sequence[8].getBigInteger();
      return new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
    }
    else if (privateKey.contains(PEMUtility.PKCS_8_PRIVATE_KEY_PREFIX)) {
      final byte[] bytes = keyBytes(privateKey, PEMUtility.PKCS_8_PRIVATE_KEY_PREFIX, PEMUtility.PKCS_8_PRIVATE_KEY_SUFFIX);
      return new PKCS8EncodedKeySpec(bytes);
    }
    else {
      throw new InvalidParameterException("Unexpected Private Key Format");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyBytes
  private static byte[] keyBytes(final String key, final String keyPrefix, final String keySuffix) {
    final int    startIndex = key.indexOf(keyPrefix);
    final int    endIndex   = key.indexOf(keySuffix);
    final String base64     = key.substring(startIndex + keyPrefix.length(), endIndex).replaceAll("\\s", "");
    return Base64.getDecoder().decode(base64);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pemFromKey
  private static String pemFromKey(final Key key) {
    final StringBuilder sb = new StringBuilder();
    if (key instanceof PrivateKey) {
      if (key.getFormat().equals("PKCS#1")) {
        sb.append(PEMUtility.PKCS_1_PRIVATE_KEY_PREFIX).append("\n");
      }
      else if (key.getFormat().equals("PKCS#8")) {
        sb.append(PEMUtility.PKCS_8_PRIVATE_KEY_PREFIX).append("\n");
      }
      else {
        throw new InvalidParameterException("Unexpected Private Key Format");
      }
    }
    else {
      sb.append(PEMUtility.X509_PUBLIC_KEY_PREFIX).append("\n");
    }

    final String encoded = new String(Base64.getEncoder().encode(key.getEncoded()));
    int index      = 0;
    int lineLength = 65;
    while (index < encoded.length()) {
      sb.append(encoded, index, Math.min(index + lineLength, encoded.length())).append("\n");
      index += lineLength;
    }

    if (key instanceof PrivateKey) {
      if (key.getFormat().equals("PKCS#1")) {
        sb.append(PEMUtility.PKCS_1_PRIVATE_KEY_SUFFIX).append("\n");
      }
      else if (key.getFormat().equals("PKCS#8")) {
        sb.append(PEMUtility.PKCS_8_PRIVATE_KEY_SUFFIX).append("\n");
      }
    }
    else {
      sb.append(PEMUtility.X509_PUBLIC_KEY_SUFFIX).append("\n");
    }
    return sb.toString();
  }
}