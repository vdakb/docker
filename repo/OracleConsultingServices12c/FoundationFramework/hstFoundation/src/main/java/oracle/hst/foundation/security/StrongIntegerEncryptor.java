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

    File        :   StrongIntegerEncryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    StrongIntegerEncryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

import java.math.BigInteger;

import oracle.hst.foundation.security.crypto.PBEIntegerEncryptor;

////////////////////////////////////////////////////////////////////////////////
// class StrongIntegerEncryptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Utility class for easily performing high-strength encryption of
 ** {@link BigInteger} objects.
 ** <p>
 ** This class internally holds a {@link PBEIntegerEncryptor} configured
 ** this way:
 ** <ul>
 **  <li>Algorithm: <code>PBEWithMD5AndTripleDES</code>.</li>
 **  <li>Key obtention iterations: <code>1000</code>.</li>
 ** </ul>
 ** <p>
 ** The required steps to use it are:
 ** <ol>
 **   <li>Create an instance (using <code>new</code>).
 **   <li>Set a password (using <code>{@link #password(String)}</code> or
 **       <code>{@link #password(char[])}</code>).
 **   <li>Perform the desired <code>{@link #encrypt(BigInteger)}</code> or
 **       <code>{@link #decrypt(BigInteger)}</code> operations.
 ** </ol>
 ** <p>
 ** To use this class, you may need to download and install the
 ** <a href="http://java.sun.com/javase/downloads" target="_blank"><i>Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy  Files</i></a>.
 ** <p>
 ** This class is <i>thread-safe</i>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class StrongIntegerEncryptor implements IntegerEncryptor
                                    ,          SecurePasswordHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** default encryption algorithm will be PBEWithMD5AndTripleDES */
  public static final String        ALGORITHM = "PBEWithMD5AndTripleDES";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the PBEIntegerEncryptor that will be internally used.
  private final PBEIntegerEncryptor cryptor;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>StrongIntegerEncryptor</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public StrongIntegerEncryptor() {
    // ensure inheritance
    super();

    // initialize instance
    this.cryptor = new PBEIntegerEncryptor();
    this.cryptor.algorithm(ALGORITHM);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StrongIntegerEncryptor</code> with the specified
   ** password to use.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  public StrongIntegerEncryptor(final String password) {
    // ensure inheritance
    this();

    // initialize instance
    password(password);
    this.cryptor.initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StrongIntegerEncryptor</code> with the specified
   ** password to use.
   ** <p>
   ** This allows the password to be specified as a <i>cleanable</i> char[]
   ** instead of a String, in extreme security conscious environments in which
   ** no copy of the password as an immutable String should be kept in memory.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  public StrongIntegerEncryptor(final char[] password) {
    // ensure inheritance
    this();

    // initialize instance
    password(password);
    this.cryptor.initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password (PasswordHandler)
  /**
   ** Sets the password to be used.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  @Override
  public synchronized void password(final String password) {
    this.cryptor.password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password (SecurePasswordHandler)
  /**
   ** Sets the password to be used, as a char[].
   ** <p>
   ** This allows the password to be specified as a <i>cleanable</i> char[]
   ** instead of a String, in extreme security conscious environments in which
   ** no copy of the password as an immutable String should be kept in memory.
   ** <p>
   ** <b>Important</b>: the array specified as a parameter WILL BE COPIED in
   ** order to be stored as encryptor configuration. The caller of this method
   ** will therefore be responsible for its cleaning.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  @Override
  public synchronized void password(final char[] password) {
    this.cryptor.password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clean (SecurePasswordHandler)
  /**
   ** Zero-out the password char array.
   **
   ** @param  password           the char array to cleanup.
   */
  @Override
  public final void clean(final char[] password) {
    this.cryptor.clean(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt (IntegerEncryptor)
  /**
   ** Encrypts a number
   **
   ** @param  number             the number to be encrypted.
   **
   ** @see    PBEIntegerEncryptor#encrypt(BigInteger)
   */
  @Override
  public BigInteger encrypt(final BigInteger number) {
    return this.cryptor.encrypt(number);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt (IntegerEncryptor)
  /**
   ** Decrypts a number.
   **
   ** @param  number             the number to be decrypted.
   **
   ** @see    PBEIntegerEncryptor#decrypt(BigInteger)
   */
  @Override
  public BigInteger decrypt(final BigInteger number) {
    return this.cryptor.decrypt(number);
  }
}