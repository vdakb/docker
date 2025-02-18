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

    File        :   PEMUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    PEMUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

import java.io.ByteArrayInputStream;

import java.util.Base64;
import java.util.Objects;

import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidParameterException;

import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;

////////////////////////////////////////////////////////////////////////////////
// class PEMUtility
// ~~~~~ ~~~~~~~~~~
/**
 ** RSA Key Helper class.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PEMUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // PEM Encoded RSA Private Key file (PKCS#1) Start Tag
  public static final String PKCS_1_PRIVATE_KEY_PREFIX = "-----BEGIN RSA PRIVATE KEY-----";

  // PEM Encoded RSA Private Key file (PKCS#1) End Tag
  public static final String PKCS_1_PRIVATE_KEY_SUFFIX = "-----END RSA PRIVATE KEY-----";

  // RSA Public Key file (PKCS#1) Start Tag
  public static final String PKCS_1_PUBLIC_KEY_PREFIX = "-----BEGIN RSA PUBLIC KEY-----";

  // RSA Public Key file (PKCS#1) End Tag
  public static final String PKCS_1_PUBLIC_KEY_SUFFIX = "-----END RSA PUBLIC KEY-----";

  // PEM Encoded RSA Private Key file (PKCS#8)  Start Tag
  public static final String PKCS_8_PRIVATE_KEY_PREFIX = "-----BEGIN PRIVATE KEY-----";

  // PEM Encoded RSA Private Key file (PKCS#8) End Tag
  public static final String PKCS_8_PRIVATE_KEY_SUFFIX = "-----END PRIVATE KEY-----";

  // PEM Encoded X.509 Certificate Start Tag
  public static final String X509_CERTIFICATE_PREFIX = "-----BEGIN CERTIFICATE-----";

  // PEM Encoded X.509 Certificate End Tag
  public static final String X509_CERTIFICATE_SUFFIX = "-----END CERTIFICATE-----";

  // PEM Encoded RSA Public Key file (X.509) Start Tag
  public static final String X509_PUBLIC_KEY_PREFIX = "-----BEGIN PUBLIC KEY-----";

  // PEM Encoded RSA Public Key file (X.509) End Tag
  public static final String X509_PUBLIC_KEY_SUFFIX = "-----END PUBLIC KEY-----";

  private static final Base64.Encoder ENCODER       = Base64.getMimeEncoder(64, new byte[] { '\n' });

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeCertificate
  /**
   ** Decode a PEM encoded certificate, returning only the Base64 encoded
   ** string.
   **
   ** @param  encoded            the PEM encoded string version of the
   **                            certificate.
   **
   ** @return                    a base64 encoded version of the certificate.
   */
  public static String decodeCertificate(final String encoded) {
    final int startIndex = encoded.indexOf(X509_CERTIFICATE_PREFIX);
    final int endIndex   = encoded.indexOf(X509_CERTIFICATE_SUFFIX);
    if (startIndex == -1 || endIndex == -1)
      throw new InvalidParameterException("Unexpected Certificate Format");

    return encoded.substring(startIndex + X509_CERTIFICATE_PREFIX.length(), endIndex).replaceAll("\\s", "");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeCertificate
  /**
   ** PEM encode a Base64 encoded string version of a certificate.
   **
   ** @param  certificate        the Base64 encoded certificate
   **
   ** @return                    a PEM encoded certificate.
   */
  public static String encodeCertificate(final String certificate) {
    final StringBuilder sb = new StringBuilder(X509_CERTIFICATE_PREFIX).append("\n");
    int index = 0;
    while (index < certificate.length()) {
      sb.append(certificate.substring(index, Math.min(index + 64, certificate.length()))).append("\n");
      index = index + 64;
    }
    return sb.append(X509_CERTIFICATE_SUFFIX).toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodePrivateKey
  /**
   ** Decode a DER encoded private key certificate, returning only the Base64
   ** encoded string.
   **
   ** @param  encoded            the DER encoded version of the private key file.
   **
   ** @return                    a base64 encoded version of the private key.
   */
  public static String decodePrivateKey(final File encoded) {
    return certificateString(encoded);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   privateKey
  /**
   ** Decode a DER encoded private key certificate, returning only the Base64
   ** encoded string.
   **
   ** @param  certificate        the certificate to extract the private key.
   **
   ** @return                    a base64 encoded version of the private key.
   */
  public static PrivateKey privateKey(final String certificate) {
    try {
      final KeyFactory factory = KeyFactory.getInstance("RSA");
      // decode private key
      final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(certificate.getBytes());
      return factory.generatePrivate(spec);
    }
    catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodePrivateKey
  /**
   ** Return a Base64 encoded string representation of the provided
   ** {@link PrivateKey}.
   **
   ** @param  privateKey          the private key to Bes64 encode.
   **
   ** @return                    a Base64 encoded public key.
   */
  public static String decodePrivateKey(final PrivateKey privateKey) {
    Objects.requireNonNull(privateKey);
    if (!privateKey.getAlgorithm().equals("RSA"))
      throw new IllegalStateException("Only RSA keys are currently supported.");

    if (privateKey.getFormat().equals("X.509")) {
      final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
      return Base64.getEncoder().encodeToString(keySpec.getEncoded());
    }

    throw new IllegalStateException("Only RSA PKCS#8 keys are currently supported. Provided key format [" + privateKey.getFormat() + "]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodePrivateKey
  /**
   ** Return a PEM encoded string representation of the provided
   ** {@link PrivateKey}.
   **
   ** @param  privateKey         the private key to PEM encode.
   **
   ** @return                    a PEM encoded key.
   */
  public static String encodePrivateKey(final PrivateKey privateKey) {
    Objects.requireNonNull(privateKey);
    if (!privateKey.getAlgorithm().equals("RSA"))
      throw new IllegalStateException("Only RSA keys are currently supported.");

    if (privateKey.getFormat().equals("PKCS#8"))
      return PKCS_8_PRIVATE_KEY_PREFIX + "\n" + ENCODER.encodeToString(privateKey.getEncoded()) + "\n" + PKCS_8_PRIVATE_KEY_SUFFIX;

    throw new IllegalStateException("Only RSA PKCS#8 keys are currently supported. Provided key format [" + privateKey.getFormat() + "]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodePublicKey
  /**
   ** Decode a DER encoded public key certificate, returning only the Base64
   ** encoded string.
   **
   ** @param  encoded            the DER encoded version of the public key file.
   **
   ** @return                    a base64 encoded version of the public key.
   */
  public static String decodePublicKey(final File encoded) {
    return certificateString(encoded);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodePublicKey
  /**
   ** Return a Base64 encoded string representation of the provided
   ** {@link PublicKey}.
   **
   ** @param  publicKey          the public key to Bes64 encode.
   **
   ** @return                    a Base64 encoded public key.
   */
  public static String decodePublicKey(final PublicKey publicKey) {
    Objects.requireNonNull(publicKey);
    if (!publicKey.getAlgorithm().equals("RSA"))
      throw new IllegalStateException("Only RSA keys are currently supported.");

    if (publicKey.getFormat().equals("X.509")) {
      final X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
      return Base64.getEncoder().encodeToString(keySpec.getEncoded());
    }

    throw new IllegalStateException("Only RSA PKCS#8 keys are currently supported. Provided key format [" + publicKey.getFormat() + "]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicKey
  /**
   ** Decode a DER encoded public key certificate, returning only the Base64
   ** encoded string.
   **
   ** @param  file               the DER encoded version of the public key file.
   **
   ** @return                    a base64 encoded version of the public key.
   */
  public static PublicKey publicKey(final File file) {
    try {
      final CertificateFactory factory     = CertificateFactory.getInstance("X.509");
      final Certificate        certificate = factory.generateCertificate(new ByteArrayInputStream(certificate(file)));
      return certificate.getPublicKey();
    }
    catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodePublicKey
  /**
   ** Return a PEM encoded string representation of the provided
   ** {@link PublicKey}.
   **
   ** @param  publicKey          the public key to PEM encode.
   **
   ** @return                    a PEM encoded key.
   */
  public static String encodePublicKey(final PublicKey publicKey) {
    Objects.requireNonNull(publicKey);
    if (!publicKey.getAlgorithm().equals("RSA"))
      throw new IllegalStateException("Only RSA keys are currently supported.");

    if (publicKey.getFormat().equals("X.509"))
      return X509_PUBLIC_KEY_PREFIX + "\n" + ENCODER.encodeToString(publicKey.getEncoded()) + "\n" + X509_PUBLIC_KEY_SUFFIX;

    throw new IllegalStateException("Only RSA PKCS#8 keys are currently supported. Provided key format [" + publicKey.getFormat() + "]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificateString
  /**
   ** Return the certificate from a DER encoded file.
   **
   ** @param  file               the DER encoded version of the key file.
   **
   ** @return                    the public key.
   */
  public static String certificateString(final File file) {
    return new String(certificate(file));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Return the certificate from a DER encoded file.
   **
   ** @param  certificate        the DER encoded version of the key file.
   **
   ** @return                    the public key.
   */
  public static byte[] certificate(final File certificate) {
    DataInputStream stream = null;
    try {
      stream = new DataInputStream(new FileInputStream(certificate));
      final byte[] data = new byte[(int)certificate.length()];
      stream.readFully(data);
      return data;
    }
    catch (FileNotFoundException e) {
      throw new IllegalStateException(e);
    }
    catch (IOException e) {
      throw new IllegalStateException(e);
    }
    finally {
      if (stream != null) {
        try {
          stream.close();
        }
        catch (IOException e) {
          throw new IllegalStateException(e);
        }
      }
    }
  }
}