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

    File        :   DER.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    DER.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.jca.cert;

import java.util.Base64;
import java.util.Objects;

import java.io.File;
import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.security.PublicKey;

import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateException;

import oracle.hst.platform.SecurityError;
import oracle.hst.platform.SecurityBundle;
import oracle.hst.platform.SecurityException;

////////////////////////////////////////////////////////////////////////////////
// interface DER
// ~~~~~~~~~ ~~~
/**
 ** DER (<b>D</b>istinguished <b>E</b>ncoding <b>R</b>ules) is a binary encoding
 ** for X.509 certificates and private keys.
 ** <br>
 ** A DER encoded certificate file is responsible for storing some information
 ** about the owner certificate and the specific public key. This format of
 ** files cannot store the private keys and have the capacity to store only one
 ** certificate which is X.509.
 ** <br>
 ** Unlike PEM, DER-encoded files do not contain plain text statements such as
 ** <code>-----BEGIN CERTIFICATE-----</code> or
 ** <code>-----END CERTIFICATE-----</code>).
 ** <p>
 ** Like most ASN.1 structures, DER encoded certificate always starts off with a
 ** byte 30 which is the tag encoding of an ASN.1 SEQUENCE. If you're seeing a
 ** lot of repetition in the file then this is OK; it is just the structure that
 ** is strictly defined.
 ** <p>
 ** Likewise, the Base64 within a PEM encoded file always starts off with the
 ** letter M as an ASN.1 SEQUENCE starts off with a byte 30, so the first 6 bits
 ** are 001100, which translates to the number 12, which is the index of the
 ** letter M, the thirteenth letter of the alphabet.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface DER {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Entry
  // ~~~~~ ~~~~~
  /**
   ** A generic DER byte content.
   */
  static class Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected final byte[] material;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a DER base64-encoded binary <code>Entry</code>.
     **
     ** @param  material         the base64-encoded material of the the DER
     **                          encoded <code>Entry</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    protected Entry(final String material) {
      // ensure inheritance
      this(Base64.getDecoder().decode(material));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a DER encoded binary <code>Entry</code>.
     **
     ** @param  material         the binary material of the the DER encoded
     **                          <code>Entry</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     */
    protected Entry(final byte[] material) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.material = material;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: material
    /**
     ** Returns the the type of the DER <code>Entry</code>.
     **
     ** @return                  the binary content of the the DER
     **                          <code>Entry</code>.
     **                          <br>
     **                          Possible object is array of <code>byte</code>.
     */
    public final byte[] material() {
      return this.material;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: publicKey
    /**
     ** Convenience method to get the X.509 certificate object as a
     ** {@link PublicKey} type.
     **
     ** @return                  a public key initialized with the data from the
     **                          byte buffer.
     **                          <br>
     **                          Possible object is {@link PublicKey}.
     **
     ** @throws SecurityException if no provider supports a implementation for
     **                           the  X.509 certificate.
     */
    public PublicKey publicKey()
      throws SecurityException {

      try {
        return certificate().getPublicKey();
      }
      catch (SecurityException e) {
        throw SecurityException.generatePublicKey(e.getCause());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: certificate
    /**
     ** Generates a X.509 certificate object and initializes it with the data
     ** read from the byte buffer.
     **
     ** @return                  a certificate object initialized with the data
     **                          the byte buffer.
     **                          <br>
     **                          Possible object is {@link X509Certificate}.
     **
     ** @throws SecurityException if no provider supports a implementation for
     **                           the  X.509 certificate.
     */
    public final X509Certificate certificate()
      throws SecurityException {

      try {
        final CertificateFactory factory = CertificateFactory.getInstance("X509");
        return (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(this.material));
      }
      catch (CertificateException e) {
        throw SecurityException.generateCertificate(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Decoder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Class for reading OpenSSL DER encoded streams containing X509
   ** certificates, PKCS8 encoded keys and PKCS7 objects.
   ** <p>
   ** In the case of PKCS7 objects the reader will return a CMS ContentInfo
   ** object. Keys and Certificates will be returned using the appropriate
   ** java.security type.
   */
  static final class Decoder {

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
    private Decoder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: read
    /**
     ** Read the DER encoded entry as a blob of raw data from an
     ** {@link InputStream}.
     **
     ** @param  stream           the source for the DER encoded entry to fetch.
     **                          <br>
     **                          Allowed object is {@link InputStream}.
     **
     ** @return                  the DER encoded entry as a blob of raw data
     **                          in the stream.
     **                          <br>
     **                          Possible object is {@link Entry}.
     **
     ** @throws IOException          in case of a I/O error.
     ** @throws NullPointerException if <code>stream</code> is
     **                              <code>null</code>.
     */
    public final Entry read(final InputStream stream)
      throws IOException {
      
      Objects.requireNonNull(stream, SecurityBundle.string(SecurityError.ARGUMENT_IS_NULL, "stream"));
      
      int         size = 0;
      // 4KB
      final int   length = 4 * 0x400;
      byte[]      buffer = new byte[length];
      IOException failed = null;
      try {
        // convert file into array of bytes
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
          while ((size = stream.read(buffer, 0, length)) != -1)
            out.write(buffer, 0, size);
          return read(out.toByteArray());
        }
      }
      catch (IOException e) {
        failed = e;
        throw e;
      }
      finally {
        if (failed == null)
          stream.close();
        else {
          try {
            stream.close();
          }
          catch (IOException e) {
            failed.addSuppressed(e);
          }
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read
    /**
     ** Read the DER encoded entry as a blob of raw data from an array of
     ** <code>byte</code>.
     **
     ** @param  material         the binary key material of the the DER encoded
     **                          <code>Entry</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @return                  the DER encoded entry as a blob of raw data
     **                          in the stream.
     **                          <br>
     **                          Possible object is {@link Entry}.
     */
    public final Entry read(final byte[] material) {
      return new Entry(material);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read
    /**
     ** Read the DER encoded entry as a blob of raw data from a {@link Reader}.
     **
     ** @param  stream           the source for the DER encoded entry to fetch.
     **                          <br>
     **                          Allowed object is {@link Reader}.
     **
     ** @return                  the DER encoded entry as a blob of raw data
     **                          in the stream.
     **                          <br>
     **                          Possible object is {@link Entry}.
     **
     ** @throws IOException          in case of a I/O error.
     ** @throws NullPointerException if <code>stream</code> is
     **                              <code>null</code>.
     */
    public final Entry read(final Reader stream)
      throws IOException {

      Objects.requireNonNull(stream, SecurityBundle.string(SecurityError.ARGUMENT_IS_NULL, "stream"));
      int         size = 0;
      // 4KB
      final int   length = 4 * 0x400;
      char[]      buffer = new char[length];
      IOException failed = null;
      try {
        // convert file into array of char
        try (StringWriter out = new StringWriter()) {
          while ((size = stream.read(buffer, 0, length)) != -1)
            out.write(buffer, 0, size);

          return read(out.toString());
        }
      }
      catch (IOException e) {
        failed = e;
        throw e;
      }
      finally {
        if (failed == null)
          stream.close();
        else {
          try {
            stream.close();
          }
          catch (IOException e) {
            failed.addSuppressed(e);
          }
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read
    /**
     ** Read the DER encoded entry as a blob of raw data from a {@link Reader}.
     **
     ** @param  stream           the source for the DER encoded entry to fetch.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the DER encoded entry as a blob of raw data
     **                          in the stream.
     **                          <br>
     **                          Possible object is {@link Entry}.
     **
     ** @throws IOException          in case of a I/O error.
     ** @throws NullPointerException if <code>stream</code> is
     **                              <code>null</code>.
     */
    public final Entry read(final String stream)
      throws IOException {

      Objects.requireNonNull(stream, SecurityBundle.string(SecurityError.ARGUMENT_IS_NULL, "stream"));
      return new Entry(stream);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Generates a X.509 certificate object and initializes it with the data read
   ** from the {@link File}.
   **
   ** @param  stream             the source for the DER X.509 certificate entry
   **                            to fetch.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    a certificate object initialized with the data
   **                            the byte buffer.
   **                            <br>
   **                            Possible object is {@link X509Certificate}.
   **
   ** @throws IOException          in case of a I/O error.
   ** @throws NullPointerException if <code>source</code> is <code>null</code>.
   ** @throws SecurityException    if no provider supports a implementation for
   **                              the X.509 certificate.
   */
  static X509Certificate certificate(final InputStream stream)
    throws IOException
    ,      SecurityException {

    return decoder().read(stream).certificate();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Generates a X.509 certificate object and initializes it with the data read
   ** obtained from the specified key material.
   **
   ** @param  material           the source for the DER X.509 certificate entry.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    a certificate object initialized with the data
   **                            of the byte buffer.
   **                            <br>
   **                            Possible object is {@link X509Certificate}.
   */
  static X509Certificate certificate(final byte[] material) {
    return decoder().read(material).certificate();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: decoder
  /**
   ** Factory method to create a new <code>Entry</code> instance that reads a
   ** DER encoded file.
   **
   ** @return                    a new <code>Entry</code> instance.
   **                            <br>
   **                            Possible object is <code>Decoder</code>.
   */
  static Decoder decoder() {
    return new Decoder();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: entry
  /**
   ** Factory method to create a new a DER base64-encoded binary
   ** <code>Entry</code>.
   **
   ** @param  content            the base64-encoded content of the the DER
   **                            encoded <code>Entry</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new <code>Decoder</code> instance.
   **                            <br>
   **                            Possible object is <code>Decoder</code>.
   */
  static Entry entry(final String content) {
    return new Entry(content);
  }
}