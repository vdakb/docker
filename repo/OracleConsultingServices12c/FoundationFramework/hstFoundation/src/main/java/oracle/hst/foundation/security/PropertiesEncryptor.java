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

    File        :   PropertiesEncryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PropertiesEncryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

import java.util.Properties;

import oracle.hst.foundation.security.crypto.Registry;

////////////////////////////////////////////////////////////////////////////////
// final class PropertiesEncryptor
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Subclass of {@link Properties} which can make use of a
 ** {@link StringEncryptor} or {@link DefaultStringEncryptor} object to decrypt
 ** property values if they are encrypted in the <code>.properties</code> file.
 ** <p>
 ** A value is considered "encrypted" when it appears surrounded by
 ** <code>enc(...)</code>, like:
 ** <center>
 **   <code>my.value=enc(!"DGAS24FaIO$)</code>
 ** </center>
 ** <p>
 ** Decryption is performed on-the-fly when the {@link #getProperty(String)} or
 ** {@link #getProperty(String, String)} methods are called, and only these two
 ** methods perform decryption (note that neither {@link #get(Object)} nor
 ** {@link #toString()} do). Load and store operations are not affected by
 ** decryption in any manner.
 ** <p>
 ** Encrypted and unencrypted objects can be combined in the same properties
 ** file.
 ** <p>
 ** Please note that, altough objects of this class are
 ** <code>Serializable</code>, they cannot be serialized and then de-serialized
 ** in different classloaders or virtual machines. This is so because encryptors
 ** are not serializable themselves (they cannot, as they contain sensitive
 ** information) and so they remain in memory, and live for as long as the
 ** classloader lives.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public final class PropertiesEncryptor extends Properties {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String VALUE_PREFIX     = "enc(";
  private static final String VALUE_SUFFIX     = ")";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3345444080651692452")
  private static final long   serialVersionUID = -2138942673179556774L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Used as an identifier for the encryptor registry */
  private final Integer       identifier       = (int)(Math.random() * Integer.MAX_VALUE);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>PropertiesEncryptor</code> instance which will use the
   ** passed {@link StringEncryptor} object to decrypt encrypted values.
   **
   ** @param  cryptor            the {@link StringEncryptor} to be used to
   **                            decrypt values. It can not be
   **                            <code>null</code>.
   */
  public PropertiesEncryptor(final StringEncryptor cryptor) {
    this(null, cryptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>PropertiesEncryptor</code> instance which will use the
   ** passed {@link StringEncryptor} object to decrypt encrypted values, and the
   ** passed defaults as default values (may contain encrypted values).
   **
   ** @param  defaults           default values for properties (may be
   **                            encrypted).
   ** @param  cryptor            the {@link StringEncryptor} to be used to
   **                            decrypt values. It can not be
   **                            <code>null</code>.
   */
  public PropertiesEncryptor(final Properties defaults, final StringEncryptor cryptor) {
    // ensure inheritance
    super(defaults);

    // initialize instanve
    final Registry registry = Registry.instance();
    registry.register(this, cryptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Returns the identifier, just to be used by any registry.
   **
   ** @return                    the identifier, just to be used by any
   **                            registry.
   */
  public final Integer identifier() {
    return this.identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProperty (overridden)
  /**
   ** Searches for the property with the specified key in this property list. If
   ** the key is not found in this property list, the default property list, and
   ** its defaults, recursively, are then checked. The method returns
   ** <code>null</code> if the property is not found.
   **
   ** @param  key                the property key.
   **
   ** @return                    the value in this property list with the
   **                            specified key value.
   */
  @Override
  public String getProperty(final String key) {
    return decode(super.getProperty(key));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProperty (overridden)
  /**
   ** Searches for the property with the specified key in this property list. If
   ** the key is not found in this property list, the default property list, and
   ** its defaults, recursively, are then checked. The method returns the
   ** default value argument if the property is not found.
   **
   ** @param  key                the property key.
   ** @param  defaultValue       a default value.
   **
   ** @return                    the value in this property list with the
   **                            specified key value.
   */
  @Override
  public String getProperty(final String key, final String defaultValue) {
    return decode(super.getProperty(key, defaultValue));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypted
  /**
   ** Determins if the specified value is encrypted.
   ** <p>
   ** Encrypted in this case means that the value is enclosed in
   ** {@link #VALUE_PREFIX} and {@link #VALUE_SUFFIX}.
   **
   ** @param  value              the value to check.
   **
   ** @return                    <code>true</code> if the value is enclosed by
   **                            {@link #VALUE_PREFIX} and {@link #VALUE_SUFFIX},
   **                            otherwise <code>false</code>.
   */
  public boolean encrypted(final String value) {
    if (value == null)
      return false;

    final String trimmed = value.trim();
    return (trimmed.startsWith(VALUE_PREFIX) && trimmed.endsWith(VALUE_SUFFIX));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt
  /**
   ** Encrypts the specified value using the specified {@link StringEncryptor}
   ** and encloses the encrypted value in {@link #VALUE_PREFIX} and
   ** {@link #VALUE_SUFFIX}.
   **
   ** @param  decoded            the value to encrypt.
   ** @param  cryptor            the {@link StringEncryptor} to use for
   **                            encryption.
   **
   ** @return                    the encrypted value enclosed in
   **                            {@link #VALUE_PREFIX} and
   **                            {@link #VALUE_SUFFIX}.
   */
  public String encrypt(final String decoded, final StringEncryptor cryptor) {
    return VALUE_PREFIX + cryptor.encrypt(decoded) + VALUE_SUFFIX;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt
  /**
   ** Decrypts the specified value using the specified {@link StringEncryptor}.
   ** <p>
   ** The method removes the enclosing {@link #VALUE_PREFIX} and
   ** {@link #VALUE_SUFFIX} to obtain the encrypted value and pass the extracted
   ** value to the specified {@link StringEncryptor}.
   **
   ** @param  encoded            the value to decrypt enclosed in
   **                            {@link #VALUE_PREFIX} and
   **                            {@link #VALUE_SUFFIX}.
   ** @param  cryptor            the {@link StringEncryptor} to use for
   **                            decryption.
   **
   ** @return                    the decrypted value.
   */
  public String decrypt(final String encoded, final StringEncryptor cryptor) {
    final String value = encoded.substring(VALUE_PREFIX.length(), (encoded.length() - VALUE_SUFFIX.length()));
    return cryptor.decrypt(value.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Internal method for decoding (decrypting) a value if needed.
   **
   ** @param  encoded            the value to decrypt enclosed in
   **                            {@link #VALUE_PREFIX} and
   **                            {@link #VALUE_SUFFIX}.
   **
   ** @return                    the decrypted value.
   */
  private synchronized String decode(final String encoded) {
    if (!encrypted(encoded))
      return encoded;

    final Registry        registry = Registry.instance();
    final StringEncryptor cryptor  = registry.cryptor(this);
    if (cryptor != null)
      return decrypt(encoded, cryptor);

    // if neither a StringEncryptor nor a DefaultStringEncryptor can be
    // retrieved from the registry, this means that this PropertiesEncryptor
    // object has been serialized and then deserialized in a different
    // classloader and virtual machine, which is an unsupported behaviour.
    throw new EncryptionException("No string encryptor exist for this instance of EncryptableProperties. This is usually caused by the instance having been serialized and then de-serialized in a different classloader or virtual machine, which is an unsupported behaviour (as encryptors cannot be serialized themselves)");
  }
}
