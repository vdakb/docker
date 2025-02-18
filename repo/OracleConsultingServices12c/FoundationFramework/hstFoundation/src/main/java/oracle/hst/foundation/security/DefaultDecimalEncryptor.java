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

    File        :   DefaultDecimalEncryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DefaultDecimalEncryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

import java.math.BigDecimal;

import oracle.hst.foundation.security.crypto.PBEDecimalEncryptor;

////////////////////////////////////////////////////////////////////////////////
// class DefaultDecimalEncryptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Utility class for easily performing normal-strength encryption of
 ** {@link BigDecimal} objects.
 ** <p>
 ** This class internally holds a {@link PBEDecimalEncryptor} configured
 ** this way:
 ** <ul>
 **  <li>Algorithm: <code>PBEWithMD5AndDES</code>.</li>
 **  <li>Key obtention iterations: <code>1000</code>.</li>
 ** </ul>
 ** <p>
 ** The required steps to use it are:
 ** <ol>
 **   <li>Create an instance (using <code>new</code>).
 **   <li>Set a password (using <code>{@link #password(String)}</code> or
 **       <code>{@link #password(char[])}</code>).
 **   <li>Perform the desired <code>{@link #encrypt(BigDecimal)}</code> or
 **       <code>{@link #decrypt(BigDecimal)}</code> operations.
 ** </ol>
 ** <p>
 ** This class is <i>thread-safe</i>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class DefaultDecimalEncryptor implements DecimalEncryptor
                                     ,          SecurePasswordHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** default encryption algorithm will be PBEWithMD5AndDES */
  public static final String          ALGORITHM    = "PBEWithMD5AndDES";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the PBEDecimalEncryptor that will be internally used.
  private final PBEDecimalEncryptor cryptor;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>NumericPBEEncryptor</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DefaultDecimalEncryptor() {
    // ensure inheritance
    super();

    // initialize instance
    this.cryptor = new PBEDecimalEncryptor();
    this.cryptor.algorithm(ALGORITHM);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DefaultDecimalEncryptor</code> with the specified
   ** password to use.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  public DefaultDecimalEncryptor(final String password) {
    // ensure inheritance
    this();

    // initialize instance
    password(password);
    this.cryptor.initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DefaultDecimalEncryptor</code> with the specified
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
  public DefaultDecimalEncryptor(final char[] password) {
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
  // Method:   encrypt (DecimalEncryptor)
  /**
   ** Encrypts a number.
   **
   ** @param  number             the number to be encrypted.
   **
   ** @throws EncryptionException if the encryption operation fails or
   **                             initialization could not be correctly done
   **                             (for example, no message has been set),
   **                             ommitting any further information about the
   **                             cause for security reasons.
   **
   ** @see    PBEDecimalEncryptor#encrypt(BigDecimal)
   */
  @Override
  public BigDecimal encrypt(final BigDecimal number) {
    return this.cryptor.encrypt(number);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt (DecimalEncryptor)
  /**
   ** Decrypts a number.
   **
   ** @param  number             the number to be decrypted.
   **
   ** @throws EncryptionException if the decryption operation fails or
   **                             initialization could not be correctly done
   **                             (for example, no message has been set),
   **                             ommitting any further information about the
   **                             cause for security reasons.
   **
   ** @see    PBEDecimalEncryptor#decrypt(BigDecimal)
   */
  @Override
  public BigDecimal decrypt(final BigDecimal number) {
    return this.cryptor.decrypt(number);
  }
}