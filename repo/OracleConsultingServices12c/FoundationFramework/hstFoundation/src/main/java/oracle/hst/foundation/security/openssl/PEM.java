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

    File        :   PEM.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    PEM.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.openssl;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

////////////////////////////////////////////////////////////////////////////////
// interface PEM
// ~~~~~~~~~ ~~~
/**
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
 ** decoding the inner content. (Note that the SSLeay encrypted private key
 ** contains the encryption details at the PEM level and therefore cannot be
 ** represented in binary DER format). For the SSLeay format, the only supported
 ** encryption this utility provides is DES-EDE3-CBC. For the PKCS #8 format,
 ** the only algorithm currently supported by this utility is
 ** PBEWithHmacSHA1AndDESede (PKCS #5, v 2.0). No assumptions of key size for
 ** the RSA keypair are made. The utility was tested with key sizes in the range
 ** 1024 to 16,384 bits, the maximum size RSA key supported by the Microsoft
 ** RSA Cryptographic Service Providers. The maximum size RSA key supported by
 ** Sun's Java 2 JCE provider implementation is only 2048 bits. (Note that
 ** currently Java 2 (v 1.5.0_06) does not support PKCS #5 v2 algorithms, but
 ** only PBEWithMD5AndDES (PKCS #5, v 1.5) and PBEWithSHA1AndDESede (PKCS#12))
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface PEM {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the characters to mark the the borders of keys */
  static final String BOUNDARY         = "-----";

  /**
   ** The first line of a conforming key file MUST be a begin marker, which
   ** starts with the literal text:
   ** <pre>
   ** "----- BEGIN "
   ** </pre>
   */
  static final String BEGIN            = BOUNDARY + "BEGIN ";

  /**
   ** The last line of a conforming key file MUST be an end marker, which starts
   ** with the literal text:
   ** <pre>
   ** "----- END "
   ** </pre>
   */
  static final String END              = BOUNDARY + "END ";

  /**
   ** For the PEM DSA Private Key (DSAPrivateKey format), content between the
   ** header/footer lines is checked to see if there is encryption information.
   */
  static final String DSA_PRIVATE_KEY  = "DSA PRIVATE KEY";

  /**
   ** For the PEM DSA Public Key (DSAPublicKey format), content between the
   ** header/footer lines is checked to see if there is encryption information.
   */
  static final String DSA_PUBLIC_KEY   = "DSA PUBLIC KEY";

  /**
   ** For the PEM RSA Private Key (RSAPrivateKey format), content between the
   ** header/footer lines is checked to see if there is encryption information.
   */
  static final String RSA_PRIVATE_KEY  = "RSA PRIVATE KEY";

  /**
   ** For the PEM RSA Public Key (RSAPublicKey format), content between the
   ** header/footer lines is checked to see if there is encryption information.
   */
  static final String RSA_PUBLIC_KEY   = "RSA PUBLIC KEY";

  static final String X509_CERTIFICATE = "X509 CERTIFICATE";
  static final String X509_REVOCATION  = "X509 CRL";

  /**
   ** all wrap before 72 bytes to meet IETF document requirements; however,
   ** if it's not wrapped they are still compliant.
   */
  static final int MAX_LINE_LENGTH     = 72;

  static final int MD5_HASH_BYTES      = 0x10;

  static final int PROVIDER_ERROR      = 0;
  static final int PROVIDER_RSA        = 1;
  static final int PROVIDER_DSS        = 2;
  static final int PROVIDER_UNKNOWN    = 3;

  static final int KEYTYPE_OPENSSH     = 0;
  static final int KEYTYPE_FSECURE     = 1;
  static final int KEYTYPE_PUTTY       = 2;
}