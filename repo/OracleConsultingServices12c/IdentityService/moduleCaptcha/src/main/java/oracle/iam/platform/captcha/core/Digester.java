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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Digester.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Digester.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.captcha.core;

import java.util.UUID;
import java.util.Random;

import java.nio.charset.StandardCharsets;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Digester       instance          = new Digester();

  private static final MessageDigest DIGESTER          = digester();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Random               random            = new SecureRandom();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Digester</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Digester() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hash
  /**
   ** Computes and returns the digest using the specified <code>payload</code>
   ** and <code>salt</code>.
   ** <br>
   ** The {@link #DIGESTER} is reset after this call is made.
   **
   ** @param  payload            the payload to hash.
   **                            <br>
   **                            Allowed object is {@link String}.
   * @param  salt
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the hash computation where final operations
   **                            such as padding are applied.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String hash(final String payload, final String salt) {
    DIGESTER.update((payload + salt).getBytes());
    return new String(DIGESTER.digest(), StandardCharsets.UTF_8);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   randomUUID
  /**
   ** Factory to retrieve a type 4 (pseudo randomly generated) UUID.
   ** <p>
   ** The {@code UUID} is generated using a cryptographically strong pseudo
   ** random number generator.
   **
   ** @return                    a randomly generated {@link String}.
   **                            <br>
   **                            Possible object is {@link String}.
     */
  public final String randomUUID() {
   return UUID.randomUUID().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextInt
  /**
   ** Returns the next pseudorandom, uniformly distributed <code>int</code>
   ** value from the random number generator's sequence.
   ** <p>
   ** The method <code>nextInt</code> is backed by class
   ** <code>SecureRandom</code>.
   **
   ** @return                    the next pseudorandom, uniformly distributed
   **                            <code>int</code> value from the random number
   **                            generator's sequence.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int nextInt() {
    return this.random.nextInt();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextInt
  /**
   ** Returns the next pseudorandom, uniformly distributed <code>int</code>
   ** value between 0 (inclusive) and the specified <code>bound</code> value
   ** (exclusive), drawn from the random number generator's sequence.
   ** <p>
   ** The method <code>nextInt</code> is backed by class
   ** <code>SecureRandom</code>.
   **
   ** @param  bound              the upper bound (exclusive).
   **                            <br>
   **                            Must be positive.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the next pseudorandom, uniformly distributed
   **                            <code>int</code> value from the random number
   **                            generator's sequence.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int nextInt(final int bound) {
    return this.random.nextInt(bound);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextFloat
  /**
   ** Returns the next pseudorandom, uniformly distributed <code>float</code>
   ** value between <code>0.0</code> and <code>1.0</code> from the random number
   ** generator's sequence.
   ** <p>
   ** The method <code>nextInt</code> is backed by class
   ** <code>SecureRandom</code>.
   **
   ** @return                    the next pseudorandom, uniformly distributed
   **                            <code>float</code> value from the random number
   **                            generator's sequence.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public final float nextFloat() {
    return this.random.nextFloat();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextDouble
  /**
   ** Returns the next pseudorandom, uniformly distributed <code>double</code>
   ** value <code>0.0</code> and <code>1.0</code> from the random number
   ** generator's sequence.
   ** <p>
   ** The method <code>nextInt</code> is backed by class
   ** <code>SecureRandom</code>.
   **
   ** @return                    the next pseudorandom, uniformly distributed
   **                            <code>double</code> value from the random
   **                            number generator's sequence.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public final double nextDouble() {
    return this.random.nextDouble();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digester
  /**
   ** Returns a {@link MessageDigest} object that implements the
   ** <code>MD5</code> digest algorithm.
   **
   ** @return                    a <code>MD5</code> {@link MessageDigest}er.
   **                            <br>
   **                            Possible object is {@link MessageDigest}er.
   */
  private static MessageDigest digester() {
    try {
      return MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException e) {
      System.out.println("Error: " + e);
    }
    return null;
  }
}
