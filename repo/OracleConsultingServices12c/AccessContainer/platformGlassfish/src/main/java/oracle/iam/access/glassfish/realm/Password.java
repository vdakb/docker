/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   GlassFish Server Security Realm

    File        :   Password.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Password.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.realm;

import java.util.Base64;

import java.nio.charset.Charset;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Password {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String DIGEST_ALGORITHM_DEFAULT = "SHA-256";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Codec
  // ~~~~~ ~~~~~
  /**
   ** Tranformation of the password (encryption, hashing, etc.).
   */
  interface Codec {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Plain
    // ~~~~~ ~~~~~
    /**
     ** Plain text pattern, No transformation done.
     */
    static final class Plain implements Codec {

      ////////////////////////////////////////////////////////////////////////////
      // Methods of implemented interfaces
      ////////////////////////////////////////////////////////////////////////////

      ////////////////////////////////////////////////////////////////////////////
      // Method: encode (Digester)
      /**
       ** Encode the given password.
       **
       ** @param  source         the original password.
       **                        <br>
       **                        Allowed object is array of <code>byte</code>.
       **
       ** @return                the encoded password.
       **                        <br>
       **                        Possible object is array of <code>byte</code>.
       */
      @Override
      public byte[] encode(final byte[] source) {
        return source;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class Digest
    // ~~~~~ ~~~~~~
    /**
     ** Encode a password using the configured digest algorithm.
     */
    static final class Digest implements Codec {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      private static final Base64.Encoder encoder = Base64.getEncoder();

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private final Charset               charset;
      private final MessageDigest         digester;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** @param  algorithm      the algorithm to use for the digest.
       **                        <br>
       **                        Allowed object is {@link String}.
       ** @param  charset        the character set used for converting the
       **                        password to bytes
       **                        <br>
       **                        Allowed object is {@link Charset}.
       **
       ** @throws IllegalArgumentException if the digest encoding is not
       **                                  supported.
       */
      Digest(final String algorithm, final Charset charset) {
        // ensure inheritance
        super();

        // initialize instance attributes
        this.charset = charset;
        try {
          this.digester = MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e) {
          throw new IllegalStateException(algorithm + " is not supported on the current platform.");
        }
      }

      ////////////////////////////////////////////////////////////////////////////
      // Methods of implemented interfaces
      ////////////////////////////////////////////////////////////////////////////

      ////////////////////////////////////////////////////////////////////////////
      // Method: encode (Digester)
      /**
       ** Encode the given password.
       **
       ** @param  source         the original password.
       **                        <br>
       **                        Allowed object is array of <code>byte</code>.
       **
       ** @return                the encoded password.
       **                        <br>
       **                        Possible object is array of <code>byte</code>.
       */
      @Override
      public byte[] encode(final byte[] source) {
        this.digester.reset();
        return Digest.encoder.encode(this.digester.digest(source));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: encode
    /**
     ** Encode the given password.
     **
     ** @param  source           the original password.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @return                  the encoded password.
     **                          <br>
     **                          Possible object is array of <code>byte</code>.
     */
    byte[] encode(final byte[] source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   plain
  /**
   ** Factory method to create a plain text {@link Password.Codec}.
   **
   ** @return                    a new instance of a plain text
   **                            {@link Password.Codec}.
   **                            <br>
   **                            Possible object is {@link Password.Codec}.
   */
  static Password.Codec plain() {
    return new Password.Codec.Plain();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest
  /**
   ** Factory method to create a {@link Codec} digester.
   **
   ** @return                    a new instance of a {@link Codec} digester.
   **                            <br>
   **                            Possible object is {@link Codec}.
   **
   ** @throws IllegalArgumentException if the digest encoding is not
   **                                  supported.
   */
  static Password.Codec digest() {
    return digest(DIGEST_ALGORITHM_DEFAULT, Charset.defaultCharset());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest
  /**
   ** Factory method to create a digester {@link Codec}.
   **
   ** @param  algorithm          the algorithm to use for the digest.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  charset            the character set used for converting the
   **                            password to bytes
   **                            <br>
   **                            Allowed object is {@link Charset}.
   **
   ** @return                    a new instance of a {@link Codec} digester.
   **                            <br>
   **                            Possible object is {@link Codec}.
   **
   ** @throws IllegalArgumentException if the digest encoding is not
   **                                  supported.
   */
  static Codec digest(final String algorithm, final Charset charset) {
    return new Codec.Digest(algorithm, charset);
  }
}