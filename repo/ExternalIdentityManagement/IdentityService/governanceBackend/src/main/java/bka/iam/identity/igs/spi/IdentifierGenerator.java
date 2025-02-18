/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   IdentifierGenerator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IdentifierGenerator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.spi;

import java.util.Random;

public class IdentifierGenerator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final int     lowerDigit   = 48;  // digit  '0'
  static final int     upperDigit   = 57;  // digit  '9'
  static final int     lowerAlpha   = 97;  // letter 'a'
  static final int     upperAlpha   = 122; // letter 'z'

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final int    length;
  private final Random random;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentifierGenerator</code> of randomized character
   ** sequences.
   **
   ** @param  length             the length of the character sequence to
   **                            generate.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  private IdentifierGenerator(final int length) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.length = length;
    this.random = new Random();;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a default character sequences
   ** <code>IdentifierGenerator</code>.
   **
   ** @return                    the <code>IdentifierGenerator</code> populated
   **                            with a default length of <code>8</code> for the
   **                            character sequence to generate.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentifierGenerator</code>.
   */
  public static IdentifierGenerator of() {
    return of(8);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a character sequences
   ** <code>IdentifierGenerator</code> that generates character sequences of the
   ** specified length.
   **
   ** @param  length             the length of the character sequence to
   **                            generate.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>IdentifierGenerator</code> populated
   **                            with a default length of <code>8</code> for the
   **                            character sequence to generate.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentifierGenerator</code>.
   */
  public static IdentifierGenerator of(final int length) {
    return new IdentifierGenerator(length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   randomNumeric
  /**
   ** Generates a character sequence that contains only digits.
   **
   ** @return                    the character sequence generated.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String randomNumeric() {
    return randomCharacter(lowerDigit, upperDigit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   randomAlphabetic
  /**
   ** Generates a character sequence that contains only lower case letters.
   **
   ** @return                    the character sequence generated.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String randomAlphabetic() {
    return randomCharacter(lowerAlpha, upperAlpha);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   randomAlphaNumeric
  /**
   ** Generates a character sequence that contains only lower case letters and
   ** digits.
   **
   ** @return                    the character sequence generated.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String randomAlphaNumeric() {
    return this.random.ints(lowerDigit, upperAlpha + 1)
      .filter(i -> (i <= upperDigit || i >= lowerAlpha))
      .limit(this.length)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   randomCharacter
  /**
   ** Generates a character sequence that contains only characters in the
   ** specified range limits.
   **
   ** @param lowerLimit          the lower limit of the characters.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param upperLimit          the upper limit of the characters.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the character sequence generated.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private String randomCharacter(final int lowerLimit, final int upperLimit) {
    return this.random.ints(lowerLimit, upperLimit + 1)
      .limit(this.length)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();
  }
}