package oracle.hst.foundation.security;

import java.io.File;
import java.io.IOException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.GeneralSecurityException;

////////////////////////////////////////////////////////////////////////////////
// class StrongCryptor
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** If an automated task needs to log in then we must provide a
 ** username/password. Since it's an automated task we can retrieve the
 ** username/password from something like a properties file.
 ** <p>
 ** A security best practice is to never store a password in plain text, we must
 ** encrypt it using an appropriate algorithm. The javax.crypto package provides
 ** those algorithms and AES is one of them.
 ** <p>
 ** To retrieve the original value from an encrypted string, you need to use a
 ** secret key habitually stored in a different location than the user/password
 ** file.
 */
public class StrongCryptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int     KEYGEN_KEYSIZE   = 2048;
  private static final String  KEYGEN_ALGORITHM = "RSA";
  private static final String  SIGNER_ALGORITHM = "SHA1withRSA";
  private static final String  CIPHER_ALGORITHM = "RSA";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>StrongCryptor</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public StrongCryptor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createKeyPair
  /**
   ** Generate a RSA pair key (public.key, private.key) and stores it in files.
   **
   ** @param  privateKeyFile     the {@link File} path to the private key.
   ** @param  publicKeyFile      the {@link File} path to the public key
   **
   ** @return                    the the {@link KeyPair} and stored in the
   **                            specified files.
   */
  public static KeyPair createKeyPair(final File privateKeyFile, final File publicKeyFile) {
    KeyPair pair = null;
    try {
      pair = generateKeyPair();
      Encryption.writePEMKey(pair.getPublic(),  publicKeyFile);
      Encryption.writePEMKey(pair.getPrivate(), "Abcd1234".toCharArray(), privateKeyFile);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return pair;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateKeyPair
  /**
   ** Generate a RSA pair key (public.key, private.key) and stores it in files.
   **
   ** @return                    the the {@link KeyPair} and stored in the
   **                            specified files.
   */
  public static KeyPair generateKeyPair() {
    KeyPair pair = null;
    try {
      final KeyPairGenerator generator = KeyPairGenerator.getInstance(KEYGEN_ALGORITHM);
      generator.initialize(KEYGEN_KEYSIZE);

      pair = generator.generateKeyPair();
    }
    catch (GeneralSecurityException e) {
      e.printStackTrace();
    }
    return pair;
  }
}
