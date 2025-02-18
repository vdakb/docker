/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Generator.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Generator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.pkce;

import java.util.Base64;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

////////////////////////////////////////////////////////////////////////////////
// class Generator
// ~~~~~ ~~~~~~~~~
/**
 ** Used to implement Proof Key for Code Exchange by OAuth Public Clients
 ** https://tools.ietf.org/html/rfc7636
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Generator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final SecureRandom RANDOM = new SecureRandom();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The number of octets to randomly generate. */
  private final int octet;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Default
  // ~~~~~ ~~~~~~~
  private static class Default {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    private static final Generator INSTANCE = new Generator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Generator</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Generator() {
    // ensure inheritance
    this(32);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Generator</code> with the specified number of octets
   ** to generate.
   **
   ** @param  octet              the number of octets to randomly generate.
   **                            <br>
   **                            Allowed object is <code>int</code>
   */
  private Generator(final int octet) {
    // ensure inheritance
    super();

    // ensure inheritance
    this.octet = octet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to to generate Proof Key for Code Exchange.
   ** <br>
   ** Usage:
   ** <pre>
   **   java oracle.iam.platform.oauth.pkce.Generator [PLAIN | S256]
   ** </pre>
   **
   ** @param  args               the method.
   */
  public static void main(final String[] args) {
    // prevent bogus input
    if (args.length > 1)
      throw new RuntimeException("Unexpected argument: " + args[1]);

    final Challenge code = Default.INSTANCE.challenge((args.length != 0 && args[0].equalsIgnoreCase("plain")) ? Challenge.Method.PLAIN : Challenge.Method.S256);
    System.out.println("Proof Key for Code Exchange");
    System.out.println("---------------------------");
    System.out.println(String.format("%-22s: %s", Challenge.METHOD,    code.method().name()));
    System.out.println(String.format("%-22s: %s", Challenge.CHALLENGE, code.challenge()));
    System.out.println(String.format("%-22s: %s", Challenge.VERIFIER,  code.verifier()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  public static Generator instance() {
    return Default.INSTANCE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   challenge
  /**
   ** Upon receipt of the request at the token endpoint, the server verifies it
   ** by calculating the code challenge from the received code verifier and
   ** comparing it with the previously associated code challenge, after first
   ** transforming it according to the code challenge method specified by the
   ** client.
   ** <p>
   ** If the code challenge method is <code>S256</code>, the received
   ** <code>verifier</code> is hashed by SHA-256a and returned
   ** base64url-encoded.
   **
   ** @param  method             the method used to generate the code
   **                            challenged.
   **                            <br>
   **                            Allowed object is {@link Challenge.Method}.
   **
   ** @return                    the generated {@link Challenge}
   **                            <br>
   **                            Allowed object is {@link Challenge}.
   */
  public Challenge challenge(final Challenge.Method method) {
    return challenge(new Challenge().method(method).verifier(random()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   random
  /**
   ** Return a randomly filled byte array of the configured length.
   **
   ** @return                    a randomly filled byte array of the configured
   **                            length.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String random() {
    final byte[] bytes = new byte[this.octet];
    RANDOM.nextBytes(bytes);
    return encode(bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Return a Base64 encoded string.
   **
   ** @param  bytes              the binary array to encode
   **                            <br>
   **                            Allowed object is array of<code>byte</code>.
   **
   ** @return                    a Base64 encoded string evaluated from
   **                            <code>bytes</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String encode(final byte[] bytes) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   challenge
  /**
   ** Upon receipt of the request at the token endpoint, the server verifies it
   ** by calculating the code challenge from the received code verifier and
   ** comparing it with the previously associated code challenge, after first
   ** transforming it according to the code challenge method specified by the
   ** client.
   ** <p>
   ** If the code challenge method is <code>S256</code>, the received
   ** <code>verifier</code> is hashed by SHA-256a and returned
   ** base64url-encoded.
   **
   ** @param  verifier           the randomly generated code verifier.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Challenge} with the evaluated code
   **                            challenge for the passed <code>verifier</code>.
   **                            <br>
   **                            Allowed object is {@link Challenge}.
   */
  public Challenge challenge(final String verifier) {
    return challenge(new Challenge().verifier(verifier));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   challenge
  /**
   ** Upon receipt of the request at the token endpoint, the server verifies it
   ** by calculating the code challenge from the received code verifier and
   ** comparing it with the previously associated code challenge, after first
   ** transforming it according to the code challenge method specified by the
   ** client.
   ** <p>
   ** If the code challenge method is <code>S256</code>, the received
   ** <code>verifier</code> is hashed by SHA-256a and returned
   ** base64url-encoded.
   **
   ** @param  code               the {@link Challenge} providing code verifier
   **                            an code method to evaluate the code challenge.
   **                            <br>
   **                            Allowed object is {@link Challenge}.
   **
   ** @return                    the {@link Challenge} with the evaluated code
   **                            challenge for the passed <code>verifier</code>.
   **                            <br>
   **                            Possible object is {@link Challenge}.
   */
  public Challenge challenge(final Challenge code) {
    try {
      code.challenge(code.method().challenge(code.verifier()));
    }
    catch (NoSuchAlgorithmException e) {
      // fall back to plain text if the hash algorithm isn't available
      code.method(Challenge.Method.PLAIN);
      try {
        code.challenge(Challenge.Method.PLAIN.challenge(code.verifier()));
      }
      catch (NoSuchAlgorithmException x) {
        throw new IllegalStateException("It's just cannot be", x);
      }
    }
    return code;
  }
}