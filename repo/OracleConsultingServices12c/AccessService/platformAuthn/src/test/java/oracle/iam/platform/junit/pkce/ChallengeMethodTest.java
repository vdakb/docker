package oracle.iam.platform.junit.pkce;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import oracle.iam.platform.oauth.pkce.Challenge;
import oracle.iam.platform.oauth.pkce.Generator;

////////////////////////////////////////////////////////////////////////////////
// class ChallengeMethodTest
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Test Proof Key for Code Exchange by OAuth Public Clients
 ** https://tools.ietf.org/html/rfc7636
 ** <br>
 ** Appendix B. Example for the S256 code_challenge_method
 ** <br/>
 ** https://tools.ietf.org/html/rfc7636#appendix-B
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ChallengeMethodTest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final byte[] RANDOM_BYTES = new byte[]{
          116,        24, (byte)223, (byte)180, (byte)151, (byte)153
  , (byte)224,        37,        79, (byte)250,        96,       125
  , (byte)216, (byte)173, (byte)187, (byte)186,        22, (byte)212
  ,        37,        77,       105, (byte)214, (byte)191, (byte)240
  ,        91,        88,         5,        88,        83, (byte)132
  , (byte)141,       121
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ChallengeMethodTest</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ChallengeMethodTest() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testGenerating
  @Test
  public void testGenerate() {
    final Challenge code = Generator.instance().challenge(Generator.instance().encode(RANDOM_BYTES));
    assertEquals(Challenge.Method.S256, code.method());
    assertEquals("dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk", code.verifier());
    assertEquals("E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM", code.challenge());
  }
}