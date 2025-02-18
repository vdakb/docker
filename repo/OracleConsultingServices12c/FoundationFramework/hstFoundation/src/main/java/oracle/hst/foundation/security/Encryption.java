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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared security functions

    File        :   Encryption.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Encryption.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;

import java.security.NoSuchAlgorithmException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.crypto.SecretKey;

import javax.crypto.spec.SecretKeySpec;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.security.openssl.PEMReader;
import oracle.hst.foundation.security.openssl.PEMWriter;

////////////////////////////////////////////////////////////////////////////////
// abstract class Encryption
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
public abstract class Encryption {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Buffer size for read operations. */
  private static final int    BUFFER_SIZE          = 4096;

  /** X.509 certificate type used as the default. */
  public static final String  DEFAULT_TYPE         = "X.509";

  /** Encryption algorithm used for password-protected private keys. */
  public static final String  ENCRYPTION_ALGORITHM = "AES-256-CBC";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readSecretKey
  /**
   ** Reads the raw bytes of a symmetric encryption key from a {@link File}
   ** path.
   **
   ** @param  file               the {@link File} path containing key data.
   ** @param  algorithm          the symmetric cipher algorithm for which key is
   **                            used.
   **
   ** @return                    the {@link SecretKey} fetched from the file.
   **
   ** @throws IOException        on IO errors.
   */
  public static SecretKey readSecretKey(final File file, final String algorithm)
    throws IOException {

    return readSecretKey(new BufferedInputStream(new FileInputStream(file)), algorithm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readSecretKey
  /**
   ** Reads the raw bytes of a symmetric encryption key from an
   ** {@link InputStream}.
   **
   ** @param  stream             the {@link InputStream} path containing key
   **                            data.
   ** @param  algorithm          the symmetric cipher algorithm for which key is
   **                            used.
   **
   ** @return                    the {@link SecretKey} fetched from the file.
   **
   ** @throws IOException        on IO errors.
   */
  public static SecretKey readSecretKey(final InputStream stream, final String algorithm)
    throws IOException {

    return new SecretKeySpec(readData(stream), algorithm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPrivateKey
  /**
   ** Reads a DER-encoded private key in PKCS#8 format from a {@link File} path
   ** into a {@link PrivateKey} object.
   ** <p>
   ** SSLeay-format keys may also work in some cases; testing revealed
   ** SSLeay-format RSA keys generated by the OpenSSL rsa command are supported.
   **
   ** @param  file               the {@link File} path containing private key
   **                            data in DER format.
   ** @param  algorithm          the symmetric cipher algorithm used by key.
   **                            See Appendix A in the
   **                            <a href="http://java.sun.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>
   **                            for information about standard algorithm names.
   **
   ** @return                    the {@link PrivateKey} fetched from the file.
   **
   ** @throws IOException        on key read errors.
   */
  public static PrivateKey readPrivateKey(final File file, final String algorithm)
    throws IOException {

    return readPrivateKey(new BufferedInputStream(new FileInputStream(file)), algorithm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPrivateKey
  /**
   ** Reads a DER-encoded private key in PKCS#8 format from an
   ** {@link InputStream} into a {@link PrivateKey} object.
   ** <p>
   ** SSLeay-format keys may also work in some cases; testing revealed
   ** SSLeay-format RSA keys generated by the OpenSSL rsa command are supported.
   **
   ** @param  stream             the {@link InputStream} containing private key
   **                            data in DER format.
   ** @param  algorithm          the symmetric cipher algorithm used by key.
   **                            See Appendix A in the
   **                            <a href="http://java.sun.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>
   **                            for information about standard algorithm names.
   **
   ** @return                    the {@link PrivateKey} fetched from the file.
   **
   ** @throws EncryptionException if no Provider supports an implementation for
   **                             the specified algorithm.
   ** @throws IOException         on key read errors.
   */
  public static PrivateKey readPrivateKey(final InputStream stream, final String algorithm)
    throws EncryptionException
    ,      IOException {

    try {
      final PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(readData(stream));
      return KeyFactory.getInstance(algorithm).generatePrivate(keysp);
    }
    catch (InvalidKeySpecException e) {
      throw new EncryptionException(e);
    }
    catch (NoSuchAlgorithmException e) {
      throw new EncryptionException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPEMPrivateKey
  /**
   ** Reads a PEM-encoded private key from a {@link File} path into a
   ** {@link PrivateKey} object. The header of the PEM-encoded file must meet
   ** the requirements described in
   ** {@link #readPEMPrivateKey(InputStream, char[])}.
   **
   ** @param  file               the {@link File} path containing private key
   **                            data in PEM format.
   ** @param  password           the password used to encrypt private key; may
   **                            be <code>null</code> to indicate no encryption.
   **
   ** @return                    the {@link PrivateKey} fetched from the file.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  public static PrivateKey readPEMPrivateKey(final File file, final char[] password)
    throws EncryptionException
    ,      IOException {

    return readPEMPrivateKey(new BufferedInputStream(new FileInputStream(file)), password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPEMPrivateKey
  /**
   ** Reads a PEM-encoded private key from an {@link InputStream} into a
   ** {@link PrivateKey} object. The header of the encoded key
   ** <strong>MUST</strong> contain the cipher algorithm of the key, e.g.:
   ** <pre>
   ** -----BEGIN RSA PRIVATE KEY-----
   ** </pre>
   ** <p>
   ** This is the case for PEM-encoded SSLeay format private keys created by the
   ** OpenSSL rsa command, but <em>not</em> for the OpenSSL pkcs8 command.
   **
   ** @param  stream             the {@link InputStream} containing private key
   **                            data in PEM format.
   ** @param  password           the password used to encrypt private key; may
   **                            be <code>null</code> to indicate no encryption.
   **
   ** @return                    the {@link PrivateKey} fetched from the file.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  public static PrivateKey readPEMPrivateKey(final InputStream stream, final char[] password)
    throws EncryptionException
    ,      IOException {

    return decodeKey(readPEM(stream), password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPublicKey
  /**
   ** Reads a DER-encoded X.509 public key from a {@link File} path into a
   ** {@link PublicKey} object.
   **
   ** @param  file               the {@link File} path containing DER-encoded
   **                            X.509 public key.
   ** @param  algorithm          the name of encryption algorithm used by key.
   **                            See Appendix A in the
   **                            <a href="http://java.sun.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>
   **                            for information about standard algorithm names.
   **
   ** @return                    a {@link PublicKey} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  public static PublicKey readPublicKey(final File file, final String algorithm)
    throws EncryptionException
    ,      IOException {

    return readPublicKey(new BufferedInputStream(new FileInputStream(file)), algorithm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPublicKey
  /**
   ** Reads a DER-encoded X.509 public key from a an {@link InputStream} stream
   ** into a {@link PublicKey} object.
   **
   ** @param  stream             the {@link InputStream} containing DER-encoded X.509
   **                            public key data.
   ** @param  algorithm          the name of encryption algorithm used by key.
   **                            See Appendix A in the
   **                            <a href="http://java.sun.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>
   **                            for information about standard algorithm names.
   **
   ** @return                    a {@link PublicKey} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  public static PublicKey readPublicKey(final InputStream stream, final String algorithm)
    throws EncryptionException
    ,      IOException {

    try {
      final KeyFactory factory = KeyFactory.getInstance(algorithm);
      final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(readData(stream));
      return factory.generatePublic(keySpec);
    }
    catch (NoSuchAlgorithmException e) {
      throw new EncryptionException(e);
    }
    catch (InvalidKeySpecException e) {
      throw new EncryptionException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPEMPublicKey
  /**
   ** Reads a PEM-encoded public key from an {@link File} stream into a
   ** {@link PublicKey} object.
   **
   ** @param  file               the {@link File} containing DER-encoded X.509
   **                            public key data.
   **
   ** @return                    a {@link PublicKey} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  public static PublicKey readPEMPublicKey(final File file)
    throws EncryptionException
    ,      IOException {

    return readPEMPublicKey(new BufferedInputStream(new FileInputStream(file)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPEMPublicKey
  /**
   ** Reads a PEM-encoded public key from an {@link InputStream} stream into a
   ** {@link PublicKey} object.
   **
   ** @param  stream             the {@link InputStream} containing public key
   **                            data in PEM format.
   **
   ** @return                    a {@link PublicKey} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  public static PublicKey readPEMPublicKey(final InputStream stream)
    throws EncryptionException
    ,      IOException {

    return decodeKey(readPEM(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEncodedKey
  /**
   ** Writes the supplied {@link Key} to the given {@link File} path using its
   ** native encoding.
   ** <p>
   ** The format and encoding of the key is commonly determined by the key type.
   ** See {@link #writeEncodedKey(Key, OutputStream)} for more information.
   **
   ** @param  key                the {@link Key} to write to file.
   ** @param  file               the {@link File} path to write key data to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writeEncodedKey(final Key key, final File file)
    throws IOException {

    writeEncodedKey(key, new BufferedOutputStream(new FileOutputStream(file)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEncodedKey
  /**
   ** Writes the supplied {@link Key} to the given {@link File} path using its
   ** native encoding.
   ** <p>
   ** The format and encoding of the key is commonly determined by the key type:
   ** <ul>
   **   <li><code>SecretKey</code> - RAW format consisting of unmodified key
   **       material bytes.
   **   <li><code>PrivateKey</code> - DER-encoded PKCS#8 format key.
   **   <li><code>PublicKey</code> - DER-encoded X.509 format key.
   ** </ul>
   **
   ** @param  key                the {@link Key} to write to file.
   ** @param  stream             the {@link OutputStream} path to write key data
   **                            to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writeEncodedKey(final Key key, final OutputStream stream)
    throws IOException {

    writeData(stream, key.getEncoded());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writePEMKey
  /**
   ** Writes the supplied {@link PrivateKey} to the given {@link File} path in
   ** PEM format.
   **
   ** @param  key                the Private key to write to file.
   ** @param  password           the password used to decrypt private key using
   **                            256-bit AES; may be <code>null</code> to
   **                            indicate no encryption.
   ** @param  file               the {@link File} path to write private key data
   **                            to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writePEMKey(final PrivateKey key, final char[] password, final File file)
    throws IOException {

    writePEMKey(key, password, new BufferedOutputStream(new FileOutputStream(file)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writePEMKey
  /**
   ** Writes the supplied {@link PrivateKey} to the given {@link OutputStream}
   ** in PEM format.
   **
   ** @param  key                the {@link PrivateKey} to write to file.
   ** @param  password           the password used to decrypt private key using
   **                            256-bit AES; may be <code>null</code> to
   **                            indicate no encryption.
   ** @param  stream             the {@link OutputStream} path to write private
   **                            key data to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writePEMKey(final PrivateKey key, final char[] password, final OutputStream stream)
    throws IOException {

    writeData(stream, StringUtility.toAscii(encodeKey(key, password)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writePEMKey
  /**
   ** Writes the supplied {@link PublicKey} to the given {@link OutputStream}
   ** in PEM format.
   **
   ** @param  key                the {@link PublicKey} to write to file.
   ** @param  file               the {@link File} path to write private key data
   **                            to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writePEMKey(final PublicKey key, final File file)
    throws IOException {

    writePEMKey(key, new BufferedOutputStream(new FileOutputStream(file)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writePEMKey
  /**
   ** Writes the supplied {@link PublicKey} to the given {@link OutputStream}
   ** in PEM format.
   **
   ** @param  key                the {@link PublicKey} to write to file.
   ** @param  stream             the {@link OutputStream} path to write private
   **                            key data to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writePEMKey(final PublicKey key, final OutputStream stream)
    throws IOException {

    writeData(stream, StringUtility.toAscii(encodeKey(key)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readCertificate
  /**
   ** Reads a PEM or DER-encoded certificate of the given type from a
   ** {@link File} path into a {@link Certificate} object.
   **
   ** @param  file               the {@link File} containing certificate data.
   **
   ** @return                    a {@link Certificate} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  public static Certificate readCertificate(final File file)
    throws EncryptionException
    ,      IOException {

    return readCertificate(file, DEFAULT_TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readCertificate
  /**
   ** Reads a PEM or DER-encoded certificate of the given type from a
   ** {@link File} path into a {@link Certificate} object.
   **
   ** @param  file               the {@link File} containing certificate data.
   ** @param  type               the type of certificate to read, e.g. X.509.
   **
   ** @return                    a {@link Certificate} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  public static Certificate readCertificate(final File file, final String type)
    throws EncryptionException
    ,      IOException {

    return readCertificate(new BufferedInputStream(new FileInputStream(file)), type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readCertificate
  /**
   ** Reads a PEM or DER-encoded certificate of the default type certificate
   ** from an {@link InputStream} into a {@link Certificate} object.
   **
   ** @param  stream             the {@link InputStream} containing certificate
   **                            data.
   **
   ** @return                    a {@link Certificate} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  public static Certificate readCertificate(final InputStream stream)
    throws EncryptionException
    ,      IOException {

    return readCertificate(stream, DEFAULT_TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readCertificate
  /**
   ** Reads a PEM or DER-encoded certificate of the specified certificate type
   ** from an @link InputStream} into a {@link Certificate} object.
   **
   ** @param  stream             the {@link InputStream} containing certificate
   **                            data.
   ** @param  type               the type of certificate to read, e.g. X.509.
   **
   ** @return                    a {@link Certificate} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  public static Certificate readCertificate(final InputStream stream, final String type)
    throws EncryptionException
    ,      IOException {

    try {
      final CertificateFactory factory = CertificateFactory.getInstance(type);
      return factory.generateCertificate(stream);
    }
    catch (CertificateException e) {
      throw new EncryptionException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEncodedCertificates
  /**
   ** Writes the supplied {@link Certificate} to the {@link File} using its
   ** native encoding.
   ** <p>
   ** It is assumed that each certificate type would have only a single form of
   ** encoding; for example, X.509 certificates would be encoded as ASN.1 DER.
   **
   ** @param  certificate        the {@link Certificate} to write to
   **                            {@link OutputStream}.
   ** @param  file               the {@link File} to write certificate data to.
   **
   ** @throws EncryptionException if the given cert cannot be decoded to bytes.
   ** @throws IOException         on read errors.
   */
  public static void writeEncodedCertificate(final Certificate certificate, final File file)
    throws EncryptionException
    ,      IOException {

    writeEncodedCertificate(certificate,new BufferedOutputStream(new FileOutputStream(file)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEncodedCertificates
  /**
   ** Writes the supplied {@link Certificate} to the {@link OutputStream} using
   ** its native encoding.
   ** <p>
   ** It is assumed that each certificate type would have only a single form of
   ** encoding; for example, X.509 certificates would be encoded as ASN.1 DER.
   **
   ** @param  certificate        the {@link Certificate} to write to
   **                            {@link OutputStream}.
   ** @param  stream             the {@link OutputStream} to write certificate
   **                            data to.
   **
   ** @throws EncryptionException if the given cert cannot be decoded to bytes.
   ** @throws IOException         on read errors.
   */
  public static void writeEncodedCertificate(final Certificate certificate, final OutputStream stream)
    throws EncryptionException
    ,      IOException {

    try {
      writeData(stream, certificate.getEncoded());
    } catch (CertificateEncodingException e) {
      throw new EncryptionException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEncodedCertificates
  /**
   ** Writes the supplied {@link Certificate}s in sequence to the {@link File}
   ** using their native encoding. It is assumed that each certificate type
   ** would have only a single form of encoding; for example, X.509 certificates
   ** would be encoded as ASN.1 DER.
   ** <p>
   ** It is assumed that each certificate type would have only a single form of
   ** encoding; for example, X.509 certificates would be encoded as ASN.1 DER.
   **
   ** @param  certificate        the {@link Certificate}s to write to
   **                            {@link File}.
   ** @param  file               the {@link File} path to write certificate data
   **                            to.
   **
   ** @throws EncryptionException if the given cert cannot be decoded to bytes.
   ** @throws IOException         on read errors.
   */
  public static void writeEncodedCertificates(final Certificate[] certificate, final File file)
    throws EncryptionException
    ,      IOException {

    writeEncodedCertificates(certificate, new BufferedOutputStream(new FileOutputStream(file)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEncodedCertificates
  /**
   ** Writes the supplied {@link Certificate}s in sequence to the
   ** {@link OutputStream} using their native encoding.
   ** <p>
   ** It is assumed that each certificate type would have only a single form of
   ** encoding; for example, X.509 certificates would be encoded as ASN.1 DER.
   **
   ** @param  certificate        the {@link Certificate}s to write to
   **                            {@link OutputStream}.
   ** @param  stream             the {@link OutputStream} to write certificate
   **                            data to.
   **
   ** @throws EncryptionException if the given cert cannot be decoded to bytes.
   ** @throws IOException         on read errors.
   */
  public static void writeEncodedCertificates(final Certificate[] certificate, final OutputStream stream)
    throws EncryptionException
    ,      IOException {

    try {
      for (int i = 0; i < certificate.length; i++)
        stream.write(certificate[i].getEncoded());
    }
    catch (CertificateEncodingException e) {
      throw new EncryptionException(e);
    }
    finally {
      stream.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writePEMCertificates
  /**
   ** Writes the supplied {@link Certificate} to the given {@link File} in PEM
   ** format.
   **
   ** @param  certificate        the {@link Certificate} to write to
   **                            {@link File}.
   ** @param  file               the {@link File} to write certificate data to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writePEMCertificates(final Certificate certificate, final File file)
    throws IOException {

    writePEMCertificates(certificate, new BufferedOutputStream(new FileOutputStream(file)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writePEMCertificates
  /**
   ** Writes the supplied {@link Certificate} to the given {@link OutputStream}
   ** in PEM format.
   **
   ** @param  certificate        the {@link Certificate} to write to
   **                            {@link OutputStream}.
   ** @param  stream             the {@link OutputStream} to write certificate
   **                            data to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writePEMCertificates(final Certificate certificate, final OutputStream stream)
    throws IOException {

    writeData(stream, StringUtility.toAscii(encodeCertificate(certificate)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writePEMCertificates
  /**
   ** Writes the concatenation of the given {@link Certificate}s in PEM format
   ** to the given {@link File} path.
   **
   ** @param  certificate        the {@link Certificate}s to write to
   **                            {@link File}.
   ** @param  file               the {@link File} path to write certificate data
   **                            to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writePEMCertificates(final Certificate[] certificate, final File file)
    throws IOException {

    writePEMCertificates(certificate, new BufferedOutputStream(new FileOutputStream(file)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writePEMCertificates
  /**
   ** Writes the concatenation of the given {@link Certificate}s in PEM format
   ** to the given {@link OutputStream}.
   **
   ** @param  certificate        the {@link Certificate}s to write to
   **                            {@link OutputStream}.
   ** @param  stream             the {@link OutputStream} to write certificate
   **                            data to.
   **
   ** @throws IOException        on write errors.
   */
  public static void writePEMCertificates(final Certificate[] certificate, final OutputStream stream)
    throws IOException {
    try {
      for (int i = 0; i < certificate.length; i++)
        stream.write(StringUtility.toAscii(encodeCertificate(certificate[i])));
    }
    finally {
      stream.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readCertificateChain
  /**
   ** Reads a certificate chain of the default certificate type from
   ** {@link File} path containing data in any of the formats supported by
   ** {@link #readCertificateChain(InputStream, String)}.
   **
   ** @param  file               the {@link File} containing certificate chain
   **                            data.
   **
   ** @return                    an array of certificates in the order in which
   **                            they appear in the stream.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  public static Certificate[] readCertificateChain(final File file)
    throws EncryptionException
    ,      IOException {

    return readCertificateChain(file, DEFAULT_TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readCertificateChain
  /**
   ** Reads a certificate chain of the specified certificate type from a
   ** {@link File} path containing data in any of the formats supported by
   ** {@link #readCertificateChain(InputStream, String)}.
   **
   ** @param  file               the {@link File} containing certificate chain
   **                            data.
   ** @param  type               the type of certificate to read, e.g. X.509.
   **
   ** @return                    an array of certificates in the order in which
   **                            they appear in the stream.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  public static Certificate[] readCertificateChain(final File file, final String type)
    throws EncryptionException
    ,      IOException {

    return readCertificateChain(new BufferedInputStream(new FileInputStream(file)), type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readCertificateChain
  /**
   ** Reads a certificate chain of the default certificate type from an
   ** {@link InputStream} containing data in any of the formats supported by
   ** {@link #readCertificateChain(InputStream, String)}.
   **
   ** @param  stream             the {@link InputStream} containing certificate
   **                            chain data.
   **
   ** @return                    an array of certificates in the order in which
   **                            they appear in the stream.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  public static Certificate[] readCertificateChain(final InputStream stream)
    throws EncryptionException
    ,      IOException {

    return readCertificateChain(stream, DEFAULT_TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readCertificateChain
  /**
   ** Reads a certificate chain of the default certificate type from an
   ** {@link InputStream} containing data in any of the following formats:
   ** <ul>
   **   <li>Sequence of DER-encoded certificates
   **   <li>Concatenation of PEM-encoded certificates
   **   <li>PKCS#7 certificate chain
   ** </ul>
   **
   ** @param  stream             the {@link InputStream} containing certificate
   **                            chain data.
   ** @param  type               the type of certificate to read, e.g. X.509.
   **
   ** @return                    an array of certificates in the order in which
   **                            they appear in the stream.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  public static Certificate[] readCertificateChain(final InputStream stream, final String type)
    throws EncryptionException
    ,      IOException {

    InputStream is = stream;
    if (!stream.markSupported())
      is = new BufferedInputStream(stream);

    final List<Certificate> list = new ArrayList<Certificate>();
    try {
      final CertificateFactory factory = CertificateFactory.getInstance(type);
      while (is.available() > 0) {
        final Certificate certificate = factory.generateCertificate(is);
        if (certificate != null)
          list.add(certificate);
      }
    }
    catch (CertificateException e) {
      throw new EncryptionException(e);
    }

    return list.toArray(new Certificate[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeKey
  /**
   ** Encodes the given {@link PrivateKey} to PEM format.
   **
   ** @param  key                the {@link PrivateKey} to encode.
   ** @param  password           the password used to encrypt private key using
   **                            256-bit AES encryption; may be null to indicate
   **                            no encryption.
   **
   ** @return                    the PEM-encoded text of the {@link PrivateKey}.
   **
   ** @throws EncryptionException on encoding error.
   ** @throws IOException         on write errors.
   */
  public static String encodeKey(final PrivateKey key, final char[] password)
    throws EncryptionException
    ,      IOException {

    if (password == null || password.length == 0)
      return encodeObject(key);

    final StringWriter sw = new StringWriter();
    PEMWriter writer = null;
    try {
      writer = new PEMWriter(sw);
      writer.writeObject(key, ENCRYPTION_ALGORITHM, password);
    }
    finally {
      if (writer != null) {
        writer.close();
      }
    }
    return sw.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeKey
  /**
   ** Decodes the given {@link PrivateKey} from PEM format.
   *
   ** @param  privateKey         the PEM-encoded text of the {@link PrivateKey}
   **                            to decode.
   ** @param  password           the password used to decrypt private key using
   **                            DESEDE algorithm when specified; may be
   **                            <code>null</code> to indicate no encryption.
   **
   ** @return                    the decoded {@link PrivateKey}.
   **
   ** @throws EncryptionException on encoding error.
   ** @throws IOException         on read errors.
   */
  public static PrivateKey decodeKey(final String privateKey, final char[] password)
    throws EncryptionException
    ,      IOException {

    PEMReader reader = null;
    if (password == null || password.length == 0)
      reader = new PEMReader(new StringReader(privateKey));

    reader = new PEMReader(new StringReader(privateKey), new PasswordCallback() {
      public char[] credential() {
        return password;
      }
    });

    final KeyPair keyPair = (KeyPair)reader.generate();
    if (keyPair == null)
      throw new EncryptionException("Error decoding private key.");

    return keyPair.getPrivate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeKey
  /**
   ** Encodes the given {@link PublicKey} to PEM format.
   **
   ** @param  key                the {@link PublicKey} to encode.
   **
   ** @return                    the PEM-encoded text of the {@link PublicKey}.
   **
   ** @throws EncryptionException on encoding error.
   ** @throws IOException         on write errors.
   */
  public static String encodeKey(final PublicKey key)
    throws EncryptionException
    ,      IOException {

    return encodeObject(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeKey
  /**
   ** Decodes the given PEM format to a {@link PublicKey}.
   **
   ** @param  publicKey          the PEM-encoded text of the {@link PublicKey}
   **                            to decode.
   **
   ** @return                    the decoded {@link PublicKey}.
   **
   ** @throws EncryptionException on encoding error.
   ** @throws IOException         on read errors.
   */
  public static PublicKey decodeKey(final String publicKey)
    throws EncryptionException
    ,      IOException {

    final PEMReader reader = new PEMReader(new StringReader(publicKey));
    final PublicKey key = (PublicKey) reader.generate();
    if (key != null) {
      return key;
    }
    else {
      throw new EncryptionException("Error decoding public key.");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeCertificate
  /**
   ** Encodes the given {@link Certificate} to PEM format.
   **
   ** @param  certificate        the {@link Certificate} to encode.
   **
   ** @return                    the PEM-encoded text of the
   **                            {@link Certificate}.
   **
   ** @throws EncryptionException on encoding error.
   ** @throws IOException         on write errors.
   */
  public static String encodeCertificate(final Certificate certificate)
    throws IOException {

    return encodeObject(certificate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeCertificate
  /**
   ** Decodes the given {@link Certificate} from PEM format.
   **
   ** @param  certificate        the PEM-encoded text of the {@link Certificate}
   **                            to decode.
   **
   ** @return                    the decoded {@link Certificate}.
   **
   ** @throws EncryptionException on decoding error.
   ** @throws IOException         on read errors.
   */
  public static Certificate decodeCert(final String certificate)
    throws EncryptionException
    ,      IOException {

    final PEMReader reader = new PEMReader(new StringReader(certificate));
    final Certificate cert = (Certificate)reader.generate();
    if (cert != null) {
      return cert;
    }
    else {
      throw new EncryptionException("Error decoding certificate.");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeObject
  /**
   ** Encodes the given object to PEM format if possible.
   **
   ** @param  binary             the {@link Object} to encode.
   **
   ** @return                    the PEM-encoded text of the {@link Object}.
   **
   **
   ** @throws EncryptionException on encoding error.
   ** @throws IOException         on write errors.
   */
  private static String encodeObject(final Object binary)
    throws EncryptionException
    ,      IOException {

    final StringWriter sw = new StringWriter();
    PEMWriter writer = null;
    try {
      writer = new PEMWriter(sw);
      writer.writeObject(binary);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    return sw.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPEM
  /**
   ** Reads a PEM object from an input stream into a string.
   **
   ** @param  stream             the {@link InputStream} containing PEM-encoded
   **                            data.
   **
   ** @return                    the entire contents of stream as a string.
   **
   ** @throws IOException        on read errors.
   */
  private static String readPEM(final InputStream stream)
    throws IOException {

    return new String(readData(stream), StringUtility.ASCII.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readData
  /**
   ** Reads all the data in the given stream and returns the contents as a byte
   ** array.
   **
   ** @param  stream             the {@link InputStream} to read.
   **
   ** @return                    the entire contents of stream as an array of
   **                            byte.
   **
   ** @throws IOException        on read errors.
   */
  private static byte[] readData(final InputStream stream)
    throws IOException {

    final byte[]                buf = new byte[BUFFER_SIZE];
    final ByteArrayOutputStream bos = new ByteArrayOutputStream(BUFFER_SIZE);
    int count = 0;
    try {
      do {
        count = stream.read(buf, 0, BUFFER_SIZE);
        if (count > 0)
          bos.write(buf, 0, count);
      } while (count > 0);
    }
    finally {
      try {
        stream.close();
      }
      catch (IOException e) {
        // die silently
        ;
      }
    }
    return bos.toByteArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeData
  /**
   ** Writes the given data to the given {@link OutputStream} and closes it on
   ** completion.
   **
   ** @param  stream               the {@link OutputStream} to write data to.
   ** @param  data                 the binary data (as a byte array) to be
   **                              written.
   **
   ** @throws IOException          on write errors.
   */
  private static void writeData(final OutputStream stream, final byte[] data)
    throws IOException {

    try {
      stream.write(data);
    }
    finally {
      if (stream != null) {
        stream.close();
      }
    }
  }
}