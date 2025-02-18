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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   PasswordProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PasswordProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.provider;

import java.nio.charset.StandardCharsets;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;

import javax.crypto.spec.SecretKeySpec;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.openfire.Context;

import oracle.iam.identity.icf.connector.openfire.security.Blowfish;

import oracle.iam.identity.icf.connector.openfire.schema.SystemProperty;

////////////////////////////////////////////////////////////////////////////////
// final class PasswordProvider
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Provides static methods for working with SCRAM/SASL passwords.
 ** <p>
 ** It is easy to think that all you have to do is run the password through a
 ** cryptographic hash function and your users' passwords will be secure. This
 ** is far from the truth. There are many ways to recover passwords from plain
 ** hashes very quickly. There are several easy-to-implement techniques that
 ** make these "attacks" much less effective. To motivate the need for these
 ** techniques, consider this very website. On the front page, you can submit a
 ** list of hashes to be cracked, and receive results in less than a second.
 ** Clearly, simply hashing the password does not meet our needs for security.
 ** <p>
 ** Hash algorithms are one way functions. They turn any amount of data into a
 ** fixed-length "fingerprint" that cannot be reversed. They also have the
 ** property that if the input changes by even a tiny bit, the resulting hash is
 ** completely different. This is great for protecting passwords, because we
 ** want to store passwords in a form that protects them even if the password
 ** file itself is compromised, but at the same time, we need to be able to
 ** verify that a user's password is correct.
 ** <p>
 ** The simplest way to crack a hash is to try to guess the password, hashing
 ** each guess, and checking if the guess's hash equals the hash being cracked.
 ** If the hashes are equal, the guess is the password. The two most common ways
 ** of guessing passwords are <b>dictionary attacks</b> and
 ** <b>brute-force attacks</b>.
 ** <p>
 ** A <b>dictionary attack</b> uses a file containing words, phrases, common
 ** passwords, and other strings that are likely to be used as a password. Each
 ** word in the file is hashed, and its hash is compared to the password hash.
 ** If they match, that word is the password. These dictionary files are
 ** constructed by extracting words from large bodies of text, and even from
 ** real databases of passwords. Further processing is often applied to
 ** dictionary files, such as replacing words with their "leet speak"
 ** equivalents ("hello" becomes "h3110"), to make them more effective.
 ** <p>
 ** A <b>brute-force attack</b> tries every possible combination of characters
 ** up to a given length. These attacks are very computationally expensive, and
 ** are usually the least efficient in terms of hashes cracked per processor
 ** time, but they will always eventually find the password. Passwords should be
 ** long enough that searching through all possible character strings to find it
 ** will take too long to be worthwhile.
 ** <ul>
 **   <li><b>Lookup Tables</b>
 **       <br>
 **       Lookup tables are an extremely effective method for cracking many
 **       hashes of the same type very quickly. The general idea is to
 **       <b>pre-compute</b> the hashes of the passwords in a password
 **       dictionary and store them, and their corresponding password, in a
 **       lookup table data structure. A good implementation of a lookup table
 **       can process hundreds of hash lookups per second, even when they
 **       contain many billions of hashes.
 **   <li><b>Reverse Lookup Tables</b>
 **       This attack allows an attacker to apply a dictionary or
 **       <b>brute-force attack</b> to many hashes at the same time, without
 **       having to pre-compute a lookup table.
 **       <p>
 **       First, the attacker creates a lookup table that maps each password
 **       hash from the compromised user account database to a list of users
 **       who had that hash. The attacker then hashes each password guess and
 **       uses the lookup table to get a list of users whose password was the
 **       attacker's guess. This attack is especially effective because it is
 **       common for many users to have the same password.
 **   <li><b>Rainbow Tables</b>
 **       Rainbow tables are a time-memory trade-off technique. They are like
 **       lookup tables, except that they sacrifice hash cracking speed to make
 **       the lookup tables smaller. Because they are smaller, the solutions to
 **       more hashes can be stored in the same amount of space, making them
 **       more effective. Rainbow tables that can crack any md5 hash of a
 **       password up to 8 characters long exist.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class PasswordProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The iteration to apply if nothing is configured in the system */
  public static final int           ITERATION_DEFAULT = 4096;

  public static final String        ALGORITHM_DEFAULT = "HmacSHA1";

  private static final SecureRandom RANDOM            = new SecureRandom();

  private static final byte[]       INT               = new byte[]{0, 0, 0, 1};

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final int    iteration;
  final byte[] shaker    = new byte[24];

  byte[]       client    = null;
  byte[]       stored    = null;
  byte[]       server    = null;
  String       encrypted = null;

  Blowfish     cipher    = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PasswordProvider</code> that allows use as a JavaBean.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new PasswordProvider(Context)" and enforces use of the public method
   ** below.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  password           the clear form password, i.e. what user typed
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if initialization fails.
   */
  private PasswordProvider(final Context context, final String password)
    throws SystemException {

    // ensure inheritance
    super();

    // initialize instance attributes
    this.iteration = context.systemProperty(SystemProperty.ITERATION, ITERATION_DEFAULT);
    // store the salt and salted password so SCRAM-SHA-1 SASL auth can be used
    // later.
    RANDOM.nextBytes(this.shaker);
    try {
      final byte[] salted = salted(password, this.shaker, this.iteration);
      final byte[] client = compute(salted, "Client Key");
      this.stored    = MessageDigest.getInstance("SHA-1").digest(client);
      this.server    = compute(salted, "Server Key");
      this.encrypted = null;//cipher(context).encrypt(password);
    }
    catch (InvalidKeyException e) {
      throw SystemException.abort(e.getLocalizedMessage());
    }
    catch (NoSuchAlgorithmException e) {
      throw SystemException.general(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt
  /**
   ** Returns a decrypted version of the encrypted password.
   ** <p>
   ** Decryption is performed using the {@link Blowfish} algorithm. The
   ** key material is stored as the system property
   ** {@link SystemProperty#PASSWORD} <code>passwordKey</code>.
   ** <br>
   ** openfire will automatically generated that key if not present.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  password           the encrypted form of the password.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the decrypted version of the encrypted
   **                            password.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if internal error occur.
   */
  public static String decrypt(final Context context, final String password)
    throws SystemException {

    // prevent bogus input
    if (password == null)
      return null;

    final Blowfish cipher = cipher(context);
    if (cipher == null)
      throw new UnsupportedOperationException();

    return cipher.decryptString(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   obfuscated
  /**
   ** Generates salted password.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  password           the clear form password, i.e. what user typed
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the password wrapper providing access to the
   **                            computed values.
   **                            <br>
   **                            Possible object is
   **                            <code>PasswordProvider</code>.
   **
   ** @throws SystemException    if internal error occur.
   */
  public static final PasswordProvider obfuscate(final Context context, final String password)
    throws SystemException {

    return new PasswordProvider(context, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salted
  /**
   ** Generates salted password.
   **
   ** @param  password           the clear form password, i.e. what user typed
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  salt               the salt to be used.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   ** @param  iteration          the iterations for 'salting'
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the salted password.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   **
   ** @throws InvalidKeyException      if internal error occur while working
   **                                  with {@link SecretKeySpec}.
   ** @throws NoSuchAlgorithmException if <code>HmacSHA1</code> is not supported
   **                                  by the JVM.
   */
  private static byte[] salted(final String password, final byte[] salt, final int iteration)
    throws InvalidKeyException
    ,      NoSuchAlgorithmException {

    final Mac mac = create(password.getBytes(StandardCharsets.UTF_8));
    mac.update(salt);
    mac.update(INT);

    byte[] result   = mac.doFinal();
    byte[] previous = null;
    for (int i = 1; i < iteration; i++) {
      mac.update(previous != null ? previous : result);
      previous = mac.doFinal();
      for (int j = 0; j < result.length; j++) {
        result[j] ^= previous[j];
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compute
  /**
   ** Factory method to create a {@link Mac} object that implements the
   ** <code>HmacSHA1</code> algorithm via a call to init(byte[]).
   ** <br>
   ** That is, the object is reset and available to generate another MAC from
   ** the same key, if desired, via new calls to update and doFinal.
   ** <br>
   ** (In order to reuse this Mac object with a different key, it must be
   ** reinitialized via a call to init(Key) or init(Key, AlgorithmParameterSpec).
   **
   ** @param  key                the key material of the secret key.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>s.
   ** @param  data               the data as string.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the MAC result.
   **                            <br>
   **                            Possible object is array of <code>byte</code>s.
   **
   ** @throws InvalidKeyException      if internal error occur while working
   **                                  with {@link SecretKeySpec}.
   ** @throws NoSuchAlgorithmException if <code>HmacSHA1</code> is not supported
   **                                  by the JVM.
   */
  private static byte[] compute(final byte[] key, final String data)
    throws InvalidKeyException
    ,      NoSuchAlgorithmException {

    return compute(key, data.getBytes(StandardCharsets.UTF_8));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compute
  /**
   ** Factory method to create a {@link Mac} object that implements the
   ** <code>HmacSHA1</code> algorithm via a call to create(byte[]).
   ** <br>
   ** That is, the object is reset and available to generate another MAC from
   ** the same key, if desired, via new calls to update and doFinal.
   ** <br>
   ** (In order to reuse this Mac object with a different key, it must be
   ** reinitialized via a call to init(Key) or init(Key, AlgorithmParameterSpec).
   **
   ** @param  bytes              the key material of the secret key.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>s.
   ** @param  data               the data in bytes.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>s.
   **
   ** @return                    the MAC result.
   **                            <br>
   **                            Possible object is array of <code>byte</code>s.
   **
   ** @throws InvalidKeyException      if internal error occur while working
   **                                  with {@link SecretKeySpec}.
   ** @throws NoSuchAlgorithmException if <code>HmacSHA1</code> is not supported
   **                                  by the JVM.
   */
  private static byte[] compute(final byte[] bytes, final byte[] data)
    throws InvalidKeyException
    ,      NoSuchAlgorithmException {

    return create(bytes).doFinal(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to create and return a {@link Mac} object that implements
   ** the <code>HmacSHA1</code> algorithm.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The list of registered providers may be retrieved via the
   ** Security.getProviders() method.
   **
   ** @param  bytes              the key material of the secret key.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>s.
   **
   ** @return                    the new {@link Mac} implementing the
   **                            <code>HmacSHA1</code> algorithm.
   **                            <br>
   **                            Possible object is {@link Mac}.
   **
   ** @throws InvalidKeyException      if internal error occur while working
   **                                  with {@link SecretKeySpec}.
   ** @throws NoSuchAlgorithmException if <code>HmacSHA1</code> is not supported
   **                                  by the JVM.
   */
  private static Mac create(final byte[] bytes)
    throws InvalidKeyException
    ,      NoSuchAlgorithmException {

    final Mac mac = Mac.getInstance(ALGORITHM_DEFAULT);
    mac.init(new SecretKeySpec(bytes, ALGORITHM_DEFAULT));
    return mac;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cipher
  /**
   ** Returns a {@link Blowfish} cipher that can be used for encrypting and
   ** decrypting passwords.
   ** <br>
   ** The key material is stored as the system property
   ** {@link SystemProperty#PASSWORD} <code>passwordKey</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    the {@link Blowfish} cipher, or
   **                            <code>null</code> if its not possible to create
   **                            a Cipher; for example, during setup mode.
   **
   ** @throws SystemException    if initialization fails. e.g the system
   **                            property isn't configured.
   */
  private static synchronized Blowfish cipher(final Context context)
    throws SystemException {

    // obtain the password key, stored as a database property
    final SystemProperty password = context.systemProperty(SystemProperty.PASSWORD);
    if (password.value() == null)
      throw SystemException.propertyRequired(SystemProperty.PASSWORD);

    return new Blowfish(password.value());
  }
}