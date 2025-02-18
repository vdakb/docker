package oracle.hst.security;

import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKeyFactory;

public class PBKDF2HMACSHA512Digester extends PBKDF2HMACDigester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The number of PBKDF2 output bytes.
   ** <br>
   ** By default, 64 bytes, which is 512 bits. While it may seem useful to
   ** increase the number of output bytes, doing so can actually give an
   ** advantage to the attacker, as it introduces unnecessary (avoidable)
   ** slowness to the PBKDF2 computation. 512 bits was chosen because it is
   ** <ol>
   **   <li>Default SHA512's 512-bit output (to avoid unnecessary PBKDF2
   **       overhead)
   **   <li>A multiple of 6 bits, so that the base64 encoding is optimal.
   ** </ol>
   */
  public static final int                      SIZE      = 64;

  /** default digester algorithm tag */
  public static final String                   TAG       = "{PBKDF2-HMAC-SHA512}";

  /**
   ** default encryption algorithm will be PBKDF2WithHmacSHA1
   ** PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
   ** specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
   */
  public static final String                   ALGORITHM = "PBKDF2WithHmacSHA512";

  private static ThreadLocal<SecretKeyFactory> DIGEST    =
    new ThreadLocal<SecretKeyFactory>() {
      protected SecretKeyFactory initialValue() {
        try {
          return SecretKeyFactory.getInstance(ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
          // intentionally left blank
          ;
        }
        return null;
      }
    };

  /**
   ** the one and only instance of the <code>SaltedSHA1Digester</code>
   ** <p>
   ** Singleton Pattern
   */
  private static PBKDF2HMACSHA512Digester   instance       = new PBKDF2HMACSHA512Digester();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PBKDF2HMACSHA512Digester</code> handler that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private PBKDF2HMACSHA512Digester() {
    // ensure inheritance
    super(TAG, SIZE, DIGEST.get());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionallity
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the sole instance of this credential digester.
   **
   ** @return                     the sole instance of this credential digester.
   */
  public static PBKDF2HMACSHA512Digester instance() {
    return instance;
  }
}