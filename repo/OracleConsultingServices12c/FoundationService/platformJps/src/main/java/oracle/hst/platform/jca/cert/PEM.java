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

    System      :   Oracle Security Foundation Library
    Subsystem   :   Common shared runtime facilities

    File        :   PEM.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    PEM.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.jca.cert;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;

import oracle.hst.platform.SecurityError;
import oracle.hst.platform.SecurityBundle;
import oracle.hst.platform.SecurityException;

////////////////////////////////////////////////////////////////////////////////
// interface PEM
// ~~~~~~~~~ ~~~
/**
 ** PEM (originally "<b>P</b>rivacy <b>E</b>nhanced <b>M</b>ail") is the most
 ** common format for X.509 certificates, CSRs, and cryptographic keys.
 ** <p>
 ** A PEM file is a text file containing one or more items in Base64 ASCII
 ** encoding, each with plain-text headers and footers (e.g.
 ** <code>-----BEGIN CERTIFICATE-----</code> and
 ** <code>-----END CERTIFICATE-----</code>). A single PEM file could contain an
 ** end-entity certificate, a private key, or multiple certificates forming a
 ** complete chain of trust. Most certificate files downloaded from somwhere
 ** will be in PEM format.
 ** <p>
 ** These RSA key formats are commonly created using OpenSSL and Java 2. This
 ** utility is intended to help with RSA asymmetric key interoperability between
 ** OpenSSL, .NET and Java environments.
 ** <p>
 ** PEM encoded keys must be in one of the following formats:
 ** <pre>
 ** -----BEGIN PUBLIC KEY-----
 ** MIIBIjANBgkqhkiG9w0BAQEF...
 ** -----END PUBLIC KEY-----
 **
 ** -----BEGIN RSA PRIVATE KEY-----
 ** MIICXAIBAAKBgQDfnaXDy9v4q8PfV ...
 ** -----END RSA PRIVATE KEY-----
 **
 ** -----BEGIN RSA PRIVATE KEY-----
 ** Proc-Type: 4,ENCRYPTED
 ** DEK-Info: DES-EDE3-CBC,379AB79E55059F9A
 **
 ** gaakm48Y8qYA997fJREN4JtfVkfTdnVzaZK2 ....
 ** -----END RSA PRIVATE KEY-----
 **
 ** (PKCS #8 formats)
 **
 ** -----BEGIN PRIVATE KEY-----
 ** MIICeAIBADANBgkqhkiG9w0BA ....
 ** -----END PRIVATE KEY-----
 **
 ** -----BEGIN ENCRYPTED PRIVATE KEY-----
 ** MIICxjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG .....
 ** -----END ENCRYPTED PRIVATE KEY-----
 ** </pre>
 ** Binary DER keys are obtained by removing the header/footer lines and b64
 ** decoding the inner content.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** The SSLeay encrypted private key contains the encryption details at the PEM
 ** level and therefore cannot be represented in binary DER format.
 ** <p>
 ** For the SSLeay format, the only supported encryption this utility provides
 ** is DES-EDE3-CBC. For the PKCS#8 format, the only algorithm currently
 ** supported by this utility is PBEWithHmacSHA1AndDESede (PKCS#5, v2.0). No
 ** assumptions of key size for the RSA keypair are made. The utility was tested
 ** with key sizes in the range 1024 to 16,384 bits, the maximum size RSA key
 ** supported by the Microsoft RSA Cryptographic Service Providers. The maximum
 ** size RSA key supported by Sun's Java 2 JCE provider implementation is only
 ** 2048 bits.
 ** <br>
 ** <b>Note</b>:
 ** <br>
 ** Currently Java 2 (1.5.0_06) does not support PKCS#5 v2.0 algorithms, but
 ** only PBEWithMD5AndDES (PKCS#5, v1.5) and PBEWithSHA1AndDESede (PKCS#12)).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface PEM {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the character sequence to mark the the borders of keys */
  static final String BOUNDARY = "-----";

  /**
   ** The first line of a conforming key file <b>must</b> be a begin marker,
   ** which starts with the literal text:
   ** <pre>
   ** "-----BEGIN "
   ** </pre>
   */
  static final String BEGIN    = BOUNDARY + "BEGIN ";

  /**
   ** The last line of a conforming key file <b>must</b> be an end marker, which
   ** starts with the literal text:
   ** <pre>
   ** "-----END "
   ** </pre>
   */
  static final String END      = BOUNDARY + "END ";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Type
  // ~~~~~ ~~~~~
  /**
   ** A type of a PEM encoded entry.
   */
  static enum Type {
      Generic("CERTIFICATE"),

    /**
     ** For the PEM DSA Private Key (DSAPrivateKey format), content between
     ** the header/footer lines is checked to see if there is encryption
     ** information.
     */
    DSAPrivate("DSA PRIVATE KEY"),

    /**
     ** For the PEM DSA Public Key (DSAPublicKey format), content between the
     ** header/footer lines is checked to see if there is encryption
     ** information.
     */
    DSAPublic("DSA PUBLIC KEY"),

    /**
     ** For the PEM RSA Private Key (RSAPrivateKey format), content between
     ** the header/footer lines is checked to see if there is encryption
     ** information.
     ** <p>
     ** The content represents a key in PKCS#1 format.
     */
    RSAPrivate("RSA PRIVATE KEY"),

    /**
     ** For the PEM RSA Public Key (RSAPublicKey format), content between the
     ** header/footer lines is checked to see if there is encryption
     ** information.
     */
    RSAPublic("RSA PUBLIC KEY"),

    /**
     ** For the PEM PKCS #8 Piblic Key (PKCS8Key format), content between the
     ** header/footer lines is checked to see if there is encryption
     ** information.
     */
    PKCS8Public("PUBLIC KEY"),

    /**
     ** For the PEM PKCS #8 Private Key (PKCS8Key format), content between the
     ** header/footer lines is checked to see if there is encryption
     ** information.
     */
    PKCS8Private("PRIVATE KEY"),

    /**
     ** For the PEM PKCS #8 Private Key (PKCS8Key format), content between the
     ** header/footer lines is checked to see if there is encryption
     ** information.
     */
    PKCS8PrivateEncrypted("ENCRYPTED PRIVATE KEY")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version
    // we're compatible with
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Type</code> which has a name and class type.
     **
     ** @param  value          the type value for this attribute type.
     **                        <br>
     **                        Allowed object is {@link String}.
     */
    Type(final String value) {
      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Type</code>.
     **
     ** @return                the type value for this <code>Type</code>.
     **                        <br>
     **                        Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Type</code> from the given
     ** string value.
     **
     ** @param  value          the string value the type should be returned
     **                        for.
     **                        <br>
     **                        Allowed object is {@link String}.
     **
     ** @return                the type.
     **                        <br>
     **                        Possible object is <code>Type</code>.
     */
    public static Type from(final String value) {
      for (Type cursor : Type.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Entry
  // ~~~~~ ~~~~~
  /**
   ** A generic PEM object - type, header properties, and byte content.
   */
  static class Entry extends DER.Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Type type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Entry</code>.
     **
     ** @param  type             the type of the PEM <code>Entry</code>.
     **                          <br>
     **                          Allowed object is {@link Type}.
     ** @param  content          the literal content of the the PEM
     **                          <code>Entry</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Entry(final Type type, final String content) {
      // ensure inheritance
      super(content);

      // initialize instance attributes
      this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the type of the PEM <code>Entry</code>.
     **
     ** @return                  the type of the PEM <code>Entry</code>.
     **                          <br>
     **                          Possible object is {@link Type}.
     */
    public final Type type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: publicKey (overridden)
    /**
     ** Convenience method to generate a {@link PublicKey} from the byte buffer.
     **
     ** @return                  a public key initialized with the data from the
     **                          byte buffer.
     **                          <br>
     **                          Possible object is {@link PublicKey}.
     **
     ** @throws SecurityException if no provider supports a implementation for
     **                           the RSA key factory.
     */
    @Override
    public final PublicKey publicKey()
      throws SecurityException {

      try {
        final KeyFactory         factory = KeyFactory.getInstance("RSA");
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(this.material);
        return factory.generatePublic(keySpec);
      }
      catch (NoSuchAlgorithmException e) {
        throw SecurityException.generatePublicKey(e);
      }
      catch (InvalidKeySpecException e) {
        throw SecurityException.generatePublicKey(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Decoder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Class for reading OpenSSL PEM encoded streams containing X509
   ** certificates, PKCS8 encoded keys and PKCS7 objects.
   ** <p>
   ** In the case of PKCS7 objects the reader will return a CMS ContentInfo
   ** object. Keys and Certificates will be returned using the appropriate
   ** java.security type.
   */
  static final class Decoder extends BufferedReader {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a buffering character-input stream that uses a default-sized
     ** input buffer.
     **
     ** @param  reader           a {@link Reader}.
     **                          <br>
     **                          Allowed object is {@link Reader}.
     */
    private Decoder(final Reader reader) {
      // ensure inheritance
      super(reader);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: next
    /**
     ** Read the next PEM object as a blob of raw data with header information.
     **
     ** @return                  the next entry in the stream,
     **                          <code>null</code> if no objects left.
     **                          <br>
     **                          Possible object is {@link Entry}.
     **
     ** @throws IOException      in case of a parse error.
     */
    public final Entry next()
      throws IOException {

      String line = readLine();
      // skip any content thats something else
      while (line != null && !line.startsWith(BEGIN)) {
        line = readLine();
      }

      String type = null;
      final StringBuilder       builder = new StringBuilder();
      final Map<String, String> headers = new HashMap<String, String>();
      if (line != null) {
        line = line.substring(BEGIN.length());
        int index = line.indexOf('-');
        if (index > 0 && line.endsWith(BOUNDARY) && (line.length() - index) == 5) {
          // latch the type for later use in creating the entry
          type = line.substring(0, index);
          while ((line = readLine()) != null) {
            index = line.indexOf(':');
            if (index >= 0) {
              headers.put(line.substring(0, index), line.substring(index + 1).trim());
              continue;
            }
            if (line.indexOf(type) != -1) {
              break;
            }
            builder.append(line.trim());
          }
          if (line == null) {
            throw new IOException(SecurityBundle.string(SecurityError.KEYTYPE_FATAL, type));
          }
        }
      }
      return builder.length() == 0 ? null : new Entry(Type.from(type), builder.toString());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Generates a X.509 certificate object and initializes it with the data read
   ** from the {@link Reader}.
   **
   ** @param  source             a PEM encoded string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a certificate object initialized with the data
   **                            converted.
   **                            <br>
   **                            Possible object is {@link X509Certificate}.
   **
   ** @throws IOException          in case of a I/O error.
   ** @throws NullPointerException if <code>source</code> is <code>null</code>.
   ** @throws SecurityException    if no provider supports a implementation for
   **                              the X.509 certificate.
   */
  static X509Certificate certificate(final String source)
    throws IOException
    ,      SecurityException {

    return certificate(new StringReader(source));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Generates a X.509 certificate object and initializes it with the data read
   ** from the {@link Reader}.
   **
   ** @param  source             a {@link Reader}.
   **                            <br>
   **                            Allowed object is {@link Reader}.
   **
   ** @return                    a certificate object initialized with the data
   **                            converted.
   **                            <br>
   **                            Possible object is {@link X509Certificate}.
   **
   ** @throws IOException          in case of a I/O error.
   ** @throws NullPointerException if <code>source</code> is <code>null</code>.
   ** @throws SecurityException    if no provider supports a implementation for
   **                              the X.509 certificate.
   */
  static X509Certificate certificate(final Reader source)
    throws IOException
    ,      SecurityException {

    return decoder(source).next().certificate();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Generates a X.509 certificate object and initializes it with the data read
   ** from the {@link Reader}.
   **
   ** @param  source             a {@link InputStream}.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    a certificate object initialized with the data
   **                            converted.
   **                            <br>
   **                            Possible object is {@link X509Certificate}.
   **
   ** @throws IOException          in case of a I/O error.
   ** @throws NullPointerException if <code>source</code> is <code>null</code>.
   ** @throws SecurityException    if no provider supports a implementation for
   **                              the X.509 certificate.
   */
  static X509Certificate certificate(final InputStream source)
    throws IOException
    ,      SecurityException {

    return decoder(source).next().certificate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decoder
  /**
   ** Factory method to create a new <code>Decoder</code> instance that parse a
   ** PEM encoded {@link InputStream}.
   **
   ** @param  source             a {@link InputStream}.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    a new <code>Decoder</code> instance.
   **                            <br>
   **                            Possible object is <code>Decoder</code>.
   **
   ** @throws NullPointerException if <code>source</code> is <code>null</code>.
   */
  static Decoder decoder(final InputStream source) {
    // prevent bogus input
    Objects.requireNonNull(source, SecurityBundle.string(SecurityError.ARGUMENT_IS_NULL, "source"));
    return new Decoder(new InputStreamReader(source));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decoder
  /**
   ** Factory method to create a new <code>Decoder</code> instance that parse a
   ** PEM encoded string.
   **
   ** @param  source             a PEM encoded string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new <code>Decoder</code> instance.
   **                            <br>
   **                            Possible object is <code>Decoder</code>.
   **
   ** @throws NullPointerException if <code>source</code> is <code>null</code>.
   */
  static Decoder decoder(final String source) {
    Objects.requireNonNull(source, SecurityBundle.string(SecurityError.ARGUMENT_IS_NULL, "source"));
    return decoder(new StringReader(source));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decoder
  /**
   ** Factory method to create a new <code>Decoder</code> instance that parse a
   ** PEM encoded file.
   **
   ** @param  source             a {@link Reader}.
   **                            <br>
   **                            Allowed object is {@link Reader}.
   **
   ** @return                    a new <code>Decoder</code> instance.
   **                            <br>
   **                            Possible object is <code>Decoder</code>.
   **
   ** @throws NullPointerException if <code>source</code> is <code>null</code>.
   */
  static Decoder decoder(final Reader source) {
    Objects.requireNonNull(source, SecurityBundle.string(SecurityError.ARGUMENT_IS_NULL, "source"));
    return new Decoder(source);
  }
}