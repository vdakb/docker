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

    File        :   Polish.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Polish.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual.config;

import java.util.ArrayList;

import java.io.File;

////////////////////////////////////////////////////////////////////////////////
// class Polish
// ~~~~~ ~~~~~~
/**
 ** The <code>Polish</code> {@link Alphabet} dictionary generator for
 ** textual <code>CAPTCHA</code>s.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code polish (pl)
 **   <li>country  code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Polish extends Alphabet {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Polish</code> {@link Alphabet}.
   **
   ** @param  path               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   */
  private Polish(final File path) {
    // ensure inheritance
    super(path, "pl");
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
    // uppercase letter 'C' is not applicable
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
    this.letter.add(entry('\u0059', '\u0059')); // Y
    this.letter.add(entry('\u005A', '\u005A')); // Z

    this.letter.add(entry('\u0061', '\u0041')); // a
    this.letter.add(entry('\u0062', '\u0042')); // b
    // lowercase letter 'c' is not applicable
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
    this.letter.add(entry('\u0079', '\u0059')); // y
    this.letter.add(entry('\u007A', '\u005A')); // z

    // extend the dictionary with the polish special characters
    this.letter.add(entry('\u00D3', '\u00D3')); // Ó
    this.letter.add(entry('\u00F3', '\u00D3')); // ó
    this.letter.add(entry('\u0104', '\u0104')); // Ą
    this.letter.add(entry('\u0105', '\u0104')); // ą
    this.letter.add(entry('\u0106', '\u0106')); // Ć
    this.letter.add(entry('\u0107', '\u0106')); // ć
    this.letter.add(entry('\u0118', '\u0118')); // Ę
    this.letter.add(entry('\u0119', '\u0118')); // ę
    this.letter.add(entry('\u0141', '\u0141')); // Ł
    this.letter.add(entry('\u0142', '\u0141')); // ł
    this.letter.add(entry('\u0143', '\u0143')); // Ń
    this.letter.add(entry('\u0144', '\u0143')); // ń
    this.letter.add(entry('\u015A', '\u015A')); // Ś
    this.letter.add(entry('\u015B', '\u015A')); // ś
    this.letter.add(entry('\u0179', '\u0179')); // Ź
    this.letter.add(entry('\u017A', '\u0179')); // ź
    this.letter.add(entry('\u017B', '\u017B')); // Ż
    this.letter.add(entry('\u017C', '\u017B')); // ż
 }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Polish</code> {@link Alphabet}.
   ** properties.
   **
   ** @param  path               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    <code>Polish</code> {@link Alphabet}
   **                            <br>
   **                            Possible object is <code>Polish</code>.
   */
  public static Polish build(final File path) {
    return new Polish(path);
  }
}