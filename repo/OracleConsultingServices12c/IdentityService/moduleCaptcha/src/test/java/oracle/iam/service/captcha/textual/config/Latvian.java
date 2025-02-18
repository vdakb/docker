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

    File        :   Latvian.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Latvian.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual.config;

import java.util.ArrayList;

import java.io.File;

////////////////////////////////////////////////////////////////////////////////
// class Latvian
// ~~~~~ ~~~~~~~
/**
 ** The <code>Latvian</code> {@link Alphabet} dictionary generator for
 ** textual <code>CAPTCHA</code>s.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code latvian (lv)
 **   <li>country  code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Latvian extends Alphabet {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Latvian</code> {@link Alphabet}.
   **
   ** @param  path               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   */
  private Latvian(final File path) {
    // ensure inheritance
    super(path, "lv");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLetter (Alphabet)
  /**
   ** Initialize the letter content.
   */
  @Override
  protected void initializeLetter() {
    this.letter = new ArrayList<>(10);
    this.letter.add(entry('\u0041', '\u0041')); // A
    this.letter.add(entry('\u0042', '\u0042')); // B
    this.letter.add(entry('\u0043', '\u0043')); // C
    this.letter.add(entry('\u0044', '\u0044')); // D
    this.letter.add(entry('\u0045', '\u0045')); // E
    this.letter.add(entry('\u0046', '\u0046')); // F
    this.letter.add(entry('\u0047', '\u0047')); // G
    this.letter.add(entry('\u0048', '\u0048')); // H
    this.letter.add(entry('\u0049', '\u0049')); // I
    this.letter.add(entry('\u004A', '\u004A')); // J
    this.letter.add(entry('\u004B', '\u004B')); // K
    this.letter.add(entry('\u004C', '\u004C')); // L
    this.letter.add(entry('\u004D', '\u004D')); // M
    this.letter.add(entry('\u004E', '\u004E')); // N
    this.letter.add(entry('\u004F', '\u004F')); // O
    this.letter.add(entry('\u0050', '\u0050')); // P
    // uppercase letter 'Q' is not applicable
    this.letter.add(entry('\u0052', '\u0052')); // R
    this.letter.add(entry('\u0053', '\u0053')); // S
    this.letter.add(entry('\u0054', '\u0054')); // T
    this.letter.add(entry('\u0055', '\u0055')); // U
    this.letter.add(entry('\u0056', '\u0056')); // V
    // uppercase letter 'W' is not applicable
    // uppercase letter 'X' is not applicable
    // uppercase letter 'Y' is not applicable
    this.letter.add(entry('\u005A', '\u005A')); // Z

    this.letter.add(entry('\u0061', '\u0041')); // a
    this.letter.add(entry('\u0062', '\u0042')); // b
    this.letter.add(entry('\u0063', '\u0043')); // c
    this.letter.add(entry('\u0064', '\u0044')); // d
    this.letter.add(entry('\u0065', '\u0045')); // e
    this.letter.add(entry('\u0066', '\u0046')); // f
    this.letter.add(entry('\u0067', '\u0047')); // g
    this.letter.add(entry('\u0068', '\u0048')); // h
    this.letter.add(entry('\u0069', '\u0049')); // i
    this.letter.add(entry('\u006A', '\u004A')); // j
    this.letter.add(entry('\u006B', '\u004B')); // k
    this.letter.add(entry('\u006C', '\u004C')); // l
    this.letter.add(entry('\u006D', '\u004D')); // m
    this.letter.add(entry('\u006E', '\u004E')); // n
    this.letter.add(entry('\u006F', '\u004F')); // o
    this.letter.add(entry('\u0070', '\u0050')); // p
    // lowercase letter 'q' is not applicable
    this.letter.add(entry('\u0072', '\u0052')); // r
    this.letter.add(entry('\u0073', '\u0053')); // s
    this.letter.add(entry('\u0074', '\u0054')); // t
    this.letter.add(entry('\u0075', '\u0055')); // u
    this.letter.add(entry('\u0076', '\u0056')); // v
    // lowercase letter 'w' is not applicable
    // lowercase letter 'x' is not applicable
    // lowercase letter 'y' is not applicable
    this.letter.add(entry('\u007A', '\u005A')); // z

    // extend the dictionary with the latvian special characters
    this.letter.add(entry('\u0100', '\u0100')); // Ā
    this.letter.add(entry('\u0101', '\u0100')); // ā
    this.letter.add(entry('\u010C', '\u010C')); // Č
    this.letter.add(entry('\u010D', '\u010C')); // č
    this.letter.add(entry('\u0112', '\u0112')); // Ē
    this.letter.add(entry('\u0113', '\u0112')); // ē
    this.letter.add(entry('\u0122', '\u0122')); // Ģ
    this.letter.add(entry('\u0123', '\u0122')); // ģ
    this.letter.add(entry('\u012A', '\u012A')); // Ī
    this.letter.add(entry('\u012B', '\u012A')); // ī
    this.letter.add(entry('\u0136', '\u0100')); // Ķ
    this.letter.add(entry('\u0137', '\u0100')); // ķ
    this.letter.add(entry('\u013B', '\u013B')); // Ļ
    this.letter.add(entry('\u013C', '\u013B')); // ļ
    this.letter.add(entry('\u0145', '\u0145')); // Ņ
    this.letter.add(entry('\u0146', '\u0145')); // ņ
    this.letter.add(entry('\u0160', '\u0160')); // Š
    this.letter.add(entry('\u0161', '\u0160')); // š
    this.letter.add(entry('\u016A', '\u016A')); // Ū
    this.letter.add(entry('\u016B', '\u016A')); // ū
    this.letter.add(entry('\u017D', '\u017D')); // Ž
    this.letter.add(entry('\u017E', '\u017D')); // ž
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Latvian</code> {@link Alphabet}.
   ** properties.
   **
   ** @param  path               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    <code>Latvian</code> {@link Alphabet}
   **                            <br>
   **                            Possible object is <code>Latvian</code>.
   */
  public static Latvian build(final File path) {
    return new Latvian(path);
  }
}