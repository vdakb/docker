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

    File        :   PEMReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PEMReader.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.openssl;

import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.MessageDigest;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;

import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;

import javax.crypto.SecretKey;

import javax.crypto.spec.SecretKeySpec;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.Base64Transcoder;

import oracle.hst.foundation.security.EncryptionException;
import oracle.hst.foundation.security.PasswordCallback;
import oracle.hst.foundation.security.DefaultBinaryEncryptor;

////////////////////////////////////////////////////////////////////////////////
// final class PEMReader
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** Class for reading OpenSSL PEM encoded streams containing  X509 certificates,
 ** PKCS8 encoded keys and PKCS7 objects.
 ** <p>
 ** In the case of PKCS7 objects the reader will return a CMS ContentInfo
 ** object. Keys and Certificates will be returned using the appropriate
 ** java.security type.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public final class PEMReader extends LineNumberReader {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final PasswordCallback    callback;
  private final Map<String, String> header    = new HashMap<String, String>();

  private       int                 provider  = PEM.PROVIDER_ERROR;
  private       int                 algorithm = PEM.KEYTYPE_OPENSSH;
  private       byte[]              payload   = new byte[0];

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a buffering character-input stream that uses a default-sized
   ** input buffer.
   **
   ** @param  reader             a {@link Reader}
   */
  public PEMReader(final Reader reader) {
    // ensure inheritance
    this(reader, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a buffering character-input stream that uses a default-sized
   ** input buffer with a password callback for decrypting private key.
   **
   ** @param  reader             a {@link Reader}
   ** @param  callback           the {@link PasswordCallback} to obtain the
   **                            password for private key decryption.
   */
  public PEMReader(Reader reader, PasswordCallback callback) {
    // ensure inheritance
    super(reader);

    // initialize instance
    this.callback  = callback;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  public final Object generate()
    throws IOException {

    final String type = parse();
    if (PEM.DSA_PRIVATE_KEY.equals(type))
       return generatePrivateKey();
    else if (PEM.DSA_PUBLIC_KEY.equals(type))
       return generatePublicKey("DSA");
    else if (PEM.RSA_PRIVATE_KEY.equals(type))
       return generatePrivateKey();
    else if (PEM.RSA_PUBLIC_KEY.equals(type))
       return generatePublicKey("RSA");
    else if (PEM.X509_CERTIFICATE.equals(type))
      return generateCertificate();
    else if (PEM.X509_REVOCATION.equals(type))
      return generateRevocation();
    else
      return null;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  private String parse()
    throws IOException {

    String type = null;
    String line = null;
    do {
      line = readLine();
      if (StringUtility.isEmpty(line))
        break;

      if (line.startsWith(PEM.BOUNDARY) && line.endsWith(PEM.BOUNDARY)) {
        if (line.startsWith(PEM.BEGIN)) {
          type = line.substring(PEM.BEGIN.length(), line.length() - PEM.BOUNDARY.length());
          if (type.charAt(0) == 'D' && type.charAt(1)=='S'&& type.charAt(2)=='A')
            this.provider = PEM.PROVIDER_DSS;
          else if (type.charAt(0) == 'R' && type.charAt(1)=='S'&& type.charAt(2)=='A')
            this.provider = PEM.PROVIDER_RSA;
          else if (type.charAt(0) == 'S' && type.charAt(1)=='S'&& type.charAt(2)=='H') {
            this.provider  = PEM.PROVIDER_UNKNOWN;
            this.algorithm = PEM.KEYTYPE_FSECURE;
          }
          break;
        }
        else
          throw new IOException("Invalid PEM boundary at line " + this.getLineNumber() + ": " + line);
      }
    } while (true);

    if (this.provider == PEM.PROVIDER_ERROR)
      throw new IOException("The key format is invalid! OpenSSH formatted keys must begin with -----BEGIN RSA, -----BEGIN DSA or  or -----BEGIN SSH");

    do {
      line = readLine();
      int colon = line.indexOf(':');
      if (colon == -1)
        break;
      String key = line.substring(0, colon).trim();
      if (line.endsWith("\\")) {
        String v = line.substring(colon + 1, line.length() - 1).trim();
        // multi-line value
        StringBuilder value = new StringBuilder(v);
        do {
          line = readLine();
          if (StringUtility.isEmpty(line))
            break;
          if (line.endsWith("\\")) {
            value.append(" ").append(line.substring(0, line.length() - 1).trim());
          }
          else {
            value.append(" ").append(line.trim());
            break;
          }
        } while(true);
      }
      else {
        String value = line.substring(colon + 1).trim();
        this.header.put(key, value);
      }
    } while (true);

    // first line that is not part of the header
    // could be an empty line, but if there is no header and the body begins
    // straight after the ----- then this line contains data
    if (line == null)
      throw new IOException("The key format is invalid! OpenSSH formatted keys must begin with -----BEGIN RSA or -----BEGIN DSA");

    StringBuilder body = new StringBuilder(line);
    do {
      line = readLine();
      if (line.startsWith(PEM.BOUNDARY) && line.endsWith(PEM.BOUNDARY)) {
        if (line.startsWith(PEM.END + type))
          break;
        else
          throw new IOException("Invalid PEM end boundary at line " + this.getLineNumber() + ": " + line);
      }
      body.append(line.trim());
    } while(true);

    // check for end marker; if line is mpty it's not existing
    if (line == null)
      throw new IOException(type + " not found");

    this.payload = Base64Transcoder.decode(body.toString().getBytes());
    return type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPublicKey
  /**
   ** Reads a DER-encoded X.509 public key into a {@link PublicKey} object.
   **
   ** @return                    a {@link PublicKey} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  private PublicKey readPublicKey(final String algorithm)
    throws IOException {

    return generatePublicKey(algorithm, this.payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generatePublicKey
  /**
   ** Reads a DER-encoded X.509 public key into a {@link PublicKey} object.
   **
   ** @param  algorithm          the name of the requested key algorithm.
   **                            See Appendix A in the
   **                            <a href="http://www.google.de/url?sa=t&rct=j&q=java%20cryptography%20architecture%20api%20specification&source=web&cd=1&ved=0CC8QFjAA&url=http%3A%2F%2Fdocs.oracle.com%2Fjavase%2F1.4.2%2Fdocs%2Fguide%2Fsecurity%2FCryptoSpec.html&ei=zjBST8u_BY6SOuuOhY4K&usg=AFQjCNFvog4gToQ_HL2YYLiY_PYCmiCc4Q&cad=rja">Java Cryptography Architecture API Specification &amp; Reference</a>
   **                            for information about standard algorithm names.
   ** @param  payload            the byte buffer to generate the
   **                            {@link PublicKey} from.
   **
   ** @return                    a {@link PublicKey} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  private PublicKey generatePublicKey(final String provider)
    throws IOException {

    return null;//generatePublicKey(this.algorithm, this.payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generatePublicKey
  /**
   ** Reads a DER-encoded X.509 public key into a {@link PublicKey} object.
   **
   ** @param  algorithm          the name of the requested key algorithm.
   **                            See Appendix A in the
   **                            <a href="http://www.google.de/url?sa=t&rct=j&q=java%20cryptography%20architecture%20api%20specification&source=web&cd=1&ved=0CC8QFjAA&url=http%3A%2F%2Fdocs.oracle.com%2Fjavase%2F1.4.2%2Fdocs%2Fguide%2Fsecurity%2FCryptoSpec.html&ei=zjBST8u_BY6SOuuOhY4K&usg=AFQjCNFvog4gToQ_HL2YYLiY_PYCmiCc4Q&cad=rja">Java Cryptography Architecture API Specification &amp; Reference</a>
   **                            for information about standard algorithm names.
   ** @param  payload            the byte buffer to generate the
   **                            {@link PublicKey} from.
   **
   ** @return                    a {@link PublicKey} created from data read
   **                            from stream.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  private PublicKey generatePublicKey(final String provider, final byte[] payload)
    throws IOException {

    KeySpec keySpec = new X509EncodedKeySpec(payload);
    try {
      return KeyFactory.getInstance(provider).generatePublic(keySpec);
    }
    catch (NoSuchAlgorithmException e) {
      throw new IOException("Problem creating public key: " + e.toString());
    }
    catch (InvalidKeySpecException e) {
      throw new IOException("Problem creating public key: " + e.toString());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generatePrivateKey
  /**
   ** Generates a {@link PrivateKey} from this stream.
   ** <p>
   ** The stream is positined behind the start marker of the {link PrivateKey}
   ** to read. This strem will be parsed at first until the end marker
   ** <code>marker</code> is detected. The enclosed content is than taken as
   ** the {link KeyPair} binary.
   **
   ** @return                    a {@link PrivateKey} created from data read
   **                            from this stream.
   **
   ** @throws EncryptionException on key format error.
   ** @throws IOException         on read errors.
   */
  private PrivateKey generatePrivateKey()
    throws EncryptionException
    ,      IOException {

    boolean isEncrypted = header.containsKey("Proc-Type: 4,ENCRYPTED");
    String dekInfo      = header.get("DEK-Info");

    // extract the key
//    byte[] keyBytes = this.codec.decodeBase64(this.payload);
    byte[] keyBytes = this.payload;
    if (isEncrypted) {
      if (this.callback == null)
        throw new IOException("No password finder specified, but a password is required");

      char[] credential = this.callback.credential();
      if (credential == null)
        throw new IOException("Password is null, but a password is required");

      StringTokenizer tknz       = new StringTokenizer(dekInfo, ",");
      String          dekAlgName = tknz.nextToken();
      String          algorithm  = tknz.nextToken();

      if (dekAlgName.startsWith("AES-"))
        algorithm = "AES";

      DefaultBinaryEncryptor cryptor = new DefaultBinaryEncryptor(credential);
//      cryptor.providerName(this.provider);
//      cryptor.algorithm(algorithm);
      keyBytes = cryptor.decrypt(keyBytes);
    }

    final PKCS8EncodedKeySpec keySpec  = new PKCS8EncodedKeySpec(keyBytes);
    try {
      KeyFactory factory = KeyFactory.getInstance("", "");//this.algorithm, this.provider);
      return factory.generatePrivate(keySpec);
    }
    catch (NoSuchAlgorithmException e) {
      throw new IOException("Problem creating public key: " + e.toString());
    }
    catch (InvalidKeySpecException e) {
      throw new IOException("Problem creating public key: " + e.toString());
    }
    catch (NoSuchProviderException e) {
      throw new EncryptionException("can't find provider " + this.provider);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateCertificate
  /**
   ** Generates a PEM or DER-encoded certificate from this stream into a
   ** {@link X509Certificate} object.
   **
   ** @return                    a {@link X509Certificate} created from data
   **                            read from stream.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  private X509Certificate generateCertificate()
    throws IOException {

    ByteArrayInputStream stream = new ByteArrayInputStream(this.payload);
    try {
      CertificateFactory factory = CertificateFactory.getInstance("X.509", "");//this.provider);
      return (X509Certificate)factory.generateCertificate(stream);
    }
    catch (Exception e) {
      throw new EncryptionException("problem parsing certificate: " + e.toString());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateRevocation
  /**
   ** Generates a PEM or DER-encoded certificate revocation from the given
   ** bytes into a {@link X509CRL} object.
   **
   ** @return                    a {@link X509CRL} created from the given bytes.
   **
   ** @throws EncryptionException on certificate read or format errors.
   ** @throws IOException         on read errors.
   */
  private X509CRL generateRevocation()
    throws IOException {

    ByteArrayInputStream stream = new ByteArrayInputStream(this.payload);
    try {
      CertificateFactory factory = CertificateFactory.getInstance("X.509", "");//this.provider);
      return (X509CRL)factory.generateCRL(stream);
    }
    catch (Exception e) {
      throw new EncryptionException("problem parsing certificate: " + e.toString());
    }
  }

  /**
   **
   **
   ** @param  passphrase
   ** @param  keySize
   ** @param  iv
   **
   ** @return                    a {@link SecretKey} generated from the
   **                            specified parameters.
   **
   ** @throws NoSuchAlgorithmException
   ** @throws InvalidKeySpecException
   */
  static SecretKey getKeyFromPassphrase(final String passphrase, int keySize, byte[] iv)
    throws NoSuchAlgorithmException
    ,      InvalidKeySpecException {

    byte[] passphraseBytes;
    try {
      passphraseBytes = passphrase.getBytes("US-ASCII");
    }
    catch (UnsupportedEncodingException e) {
      throw new Error("Mandatory US-ASCII character encoding is not supported by the VM");
    }

    //
    // hash is MD5
    // h(0) <- hash(passphrase, iv);
    // h(n) <- hash(h(n-1), passphrase, iv);
    // key <- (h(0),...,h(n))[0,..,key.length];
    MessageDigest hash = MessageDigest.getInstance("MD5");
    byte[] key = new byte[keySize];
    int hashesSize = keySize & 0xfffffff0;
    if ((keySize & 0xf) != 0) {
      hashesSize += PEM.MD5_HASH_BYTES;
    }

    byte[] hashes = new byte[hashesSize];
    byte[] previous;
    for (int index = 0; (index + PEM.MD5_HASH_BYTES) <= hashes.length; hash.update(previous, 0, previous.length)) {
      hash.update(passphraseBytes, 0, passphraseBytes.length);
      hash.update(iv, 0, iv.length);
      previous = hash.digest();
      System.arraycopy(previous, 0, hashes, index, previous.length);
      index += previous.length;
    }

    System.arraycopy(hashes, 0, key, 0, key.length);
    return new SecretKeySpec(key, "DESede");
  }
}