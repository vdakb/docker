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

    File        :   Challenge.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Challenge.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.pkce;

import java.util.Base64;

import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

////////////////////////////////////////////////////////////////////////////////
// class Challenge
// ~~~~~ ~~~~~~~~~
/**
 ** Used to implement Proof Key for Code Exchange by OAuth Public Clients
 ** https://tools.ietf.org/html/rfc7636.
 ** <p>
 ** To authenticate public applications or single-page applications against
 ** Authorization server, it is recommended to adapt the Authorization Code flow
 ** with PKCE (Proof Key for Code Exchange). Here’s how it works.
 ** <ul>
 **  <li>When the user initiates an authorization flow, the application computes
 **      a <code>code_verifier</code>.
 **      <br>
 **      This is a random string between 43 and 128 characters and must contain
 **      only alphanumeric characters and punctuation characters <code>-<&code>,
 **      <code>.</code>, <code>_</code>, <code>~</code>.
 **  <li>Next up, the application computes a <code>code_challenge</code>
 **      starting from the <code>code_verifier</code>.
 **      <br>
 **      <pre>code_challenge = BASE64URL-ENCODE(SHA256(ASCII(code_verifier)))</pre>
 **  <li>The application directs the browser to a sign-in page along with the
 **      generated code challenge.
 **  <li>Once the user authenticates, APS redirects back to your native
 **      application with an authorization code.
 **  <li>Then, your application sends this code along with the code verifier to
 **      APS. APS returns an access token, refresh token, and optionally an ID
 **      token.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Challenge {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String METHOD    = "code_challenge_method";
  public static final String VERIFIER  = "code_verifier";
  public static final String CHALLENGE = "code_challenge";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Method             method    = Method.S256;
  private String             verifier;
  private String             challenge;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Method
  // ~~~~~ ~~~~~~
  /**
   ** The alrogithm method to hash a code challenge
   */
  public enum Method {
    S256 {
      @Override
      public String challenge(final String verifier)
        throws NoSuchAlgorithmException {

        return Base64.getUrlEncoder().withoutPadding().encodeToString(MessageDigest.getInstance("SHA-256").digest(verifier.getBytes(StandardCharsets.US_ASCII)));
      }
    },
    PLAIN {
      @Override
      public String challenge(final String verifier) {
        return verifier;
      }
    };

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: challenge
    public abstract String challenge(final String verifier)
      throws NoSuchAlgorithmException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Challenge</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Challenge() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   method
  /**
   ** Sets the code <code>method</code>.
   **
   ** @param  value              the value to set code <code>method</code>.
   **                            <br>
   **                            Allowed object is {@link Method}.
   **
   ** @return                    this <code>Challenge</code> for chaining
   **                            invocations.
   **                            <br>
   **                            Possible object is {@link Challenge}.
   */
  public final Challenge method(final Method value) {
    this.method = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   method
  /**
   ** Returns the code <code>method</code>.
   **
   ** @return                    the value to set code <code>method</code>.
   **                            <br>
   **                            Possible object is {@link Method}.
   */
  public final Method method() {
    return this.method;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   verifier
  /**
   ** Sets the code <code>verifier</code>.
   **
   ** @param  value              the value to set code <code>verifier</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    this <code>Challenge</code> for chaining
   **                            invocations.
   **                            <br>
   **                            Possible object is {@link Challenge}.
   */
  public final Challenge verifier(final String value) {
    this.verifier = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   verifier
  /**
   ** Returns the code <code>verifier</code>.
   **
   ** @return                    the value to set code <code>verifier</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String verifier() {
    return this.verifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   challenge
  /**
   ** Sets the code <code>challenge</code>.
   **
   ** @param  value              the value to set code <code>challenge</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    this <code>Challenge</code> for chaining
   **                            invocations.
   **                            <br>
   **                            Possible object is {@link Challenge}.
   */
  public final Challenge challenge(final String value) {
    this.challenge = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   challenge
  /**
   ** Returns the code <code>challenge</code>.
   **
   ** @return                    the value to set code <code>challenge</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String challenge() {
    return this.challenge;
  }
}