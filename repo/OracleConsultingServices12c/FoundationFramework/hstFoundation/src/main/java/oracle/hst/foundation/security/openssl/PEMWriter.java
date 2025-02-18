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

    File        :   PEMWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PEMWriter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.openssl;

import java.io.Writer;
import java.io.IOException;
import java.io.BufferedWriter;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;

import java.security.spec.X509EncodedKeySpec;

import java.security.cert.X509CRL;
import java.security.cert.CRLException;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateEncodingException;

import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;

import java.security.spec.PKCS8EncodedKeySpec;

import oracle.hst.foundation.utility.Hexadecimal;

import oracle.hst.foundation.utility.Base64Transcoder;

import oracle.hst.foundation.security.DefaultBinaryEncryptor;

////////////////////////////////////////////////////////////////////////////////
// final class PEMWriter
// ~~~~~ ~~~~~ ~~~~~~~~~
public class PEMWriter extends BufferedWriter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String provider;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new buffered character-output stream that uses an output
   ** buffer of the given size.
   **
   ** @param  writer             a {@link Writer},
   */
  public PEMWriter(final Writer writer) {
    // ensure inheritance
    this(writer, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new buffered character-output stream that uses an output
   ** buffer of the given size.
   **
   ** @param  writer             a {@link Writer},
   ** @param  provider           the name of the provider to be asked for the
   **                            chosen algorithm.
   */
  public PEMWriter(final Writer writer, final String provider) {
    // ensure inheritance
    super(writer);

    // initialize instance
    this.provider = provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeObject
  public void writeObject(final Object o)
    throws IOException {

    String type;
    byte[] encoding;
    if (o instanceof X509Certificate) {
      type = "CERTIFICATE";
      try {
        encoding = ((X509Certificate)o).getEncoded();
      }
      catch (CertificateEncodingException e) {
        throw new IOException("Cannot encode object: " + e.toString());
      }
    }
    else if (o instanceof X509CRL) {
      type = "X509 CRL";
      try {
        encoding = ((X509CRL)o).getEncoded();
      }
      catch (CRLException e) {
        throw new IOException("Cannot encode object: " + e.toString());
      }
    }
    else if (o instanceof KeyPair) {
      writeObject(((KeyPair)o).getPrivate());
      return;
    }
    else if (o instanceof PrivateKey) {
      if (o instanceof RSAPrivateKey) {
        type = "RSA PRIVATE KEY";
      }
      else if (o instanceof DSAPrivateKey) {
        type = "DSA PRIVATE KEY";
      }
      else {
        throw new IOException("Cannot identify private key");
      }
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(((PrivateKey)o).getEncoded());
      encoding = keySpec.getEncoded();
    }
    else if (o instanceof PublicKey) {
      if (o instanceof RSAPublicKey) {
        type = "RSA PUBLIC KEY";
      }
      else if (o instanceof DSAPublicKey) {
        type = "DSA PUBLIC KEY";
      }
      else {
        throw new IOException("Cannot identify private key");
      }
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(((PublicKey)o).getEncoded());
      encoding = keySpec.getEncoded();
    }
    else
      throw new IOException("unknown object passed - can't encode.");

    writeHeader(type);
    writeEncoded(encoding);
    writeFooter(type);
  }

  public void writeObject(final PrivateKey key, final String algorithm, final char[] password)
    throws IOException {

    String type = null;
    if (key instanceof RSAPrivateCrtKey) {
      type = "RSA PRIVATE KEY";
    }
    else if (key instanceof RSAPrivateKey) {
      type = "RSA PRIVATE KEY";
    }
    else if (key instanceof DSAPrivateKey) {
      type = "DSA PRIVATE KEY";
    }
    final PKCS8EncodedKeySpec keySpec  = new PKCS8EncodedKeySpec(key.getEncoded());
    byte[] encoding =  keySpec.getEncoded();
    if (type == null || encoding == null)
      // TODO Support other types?
      throw new IllegalArgumentException("Object type not supported: " + key.getClass().getName());

    String dekAlgName = algorithm.toUpperCase();
    // Note: For backward compatibility
    if (dekAlgName.equals("DESEDE"))
      dekAlgName = "DES-EDE3-CBC";

    final DefaultBinaryEncryptor cryptor = new DefaultBinaryEncryptor(password);
    cryptor.algorithm(algorithm);
    encoding = cryptor.encrypt(encoding);

    final byte[] salt = cryptor.salt();

    // write the data
    writeHeader(type);
    this.write("Proc-Type: 4,ENCRYPTED");
    this.newLine();
    this.write("DEK-Info: " + dekAlgName + ",");
    this.writeHexadecimal(salt);
    this.newLine();
    this.newLine();
    this.writeEncoded(encoding);
    writeFooter(type);
  }

  private void writeHeader(final String type)
    throws IOException {
    this.write("-----BEGIN " + type + "-----");
    this.newLine();
  }

  private void writeFooter(final String type)
    throws IOException {
    this.write("-----END " + type + "-----");
    this.newLine();
  }

  private void writeEncoded(byte[] bytes)
    throws IOException {

    bytes = Base64Transcoder.encode(bytes);
    for (int i = 0; i != bytes.length; i++)
      this.write((char)bytes[i]);
  }

  private void writeHexadecimal(byte[] bytes)
    throws IOException {

    bytes = Hexadecimal.encode(bytes);
    for (int i = 0; i != bytes.length; i++)
      this.write((char)bytes[i]);
  }
}