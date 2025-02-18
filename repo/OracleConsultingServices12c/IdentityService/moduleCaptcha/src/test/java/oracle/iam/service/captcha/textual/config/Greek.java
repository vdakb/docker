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

    File        :   Greek.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Greek.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual.config;

import java.util.ArrayList;

import java.io.File;

////////////////////////////////////////////////////////////////////////////////
// class Greek
// ~~~~~ ~~~~~~
/**
 ** The <code>Greek</code> {@link Alphabet} dictionary generator for textual
 ** <code>CAPTCHA</code>s.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country  code greek (el)
 **   <li>language code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Greek extends Alphabet {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Greek</code> {@link Alphabet}.
   **
   ** @param  path               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   */
  private Greek(final File path) {
    // ensure inheritance
    super(path, "el");
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
    this.letter.add(entry('\u0391', '\u0391')); // A
    this.letter.add(entry('\u0392', '\u0392')); // B
    this.letter.add(entry('\u0393', '\u0393')); // Γ
    this.letter.add(entry('\u0394', '\u0394')); // Δ
    this.letter.add(entry('\u0395', '\u0395')); // E
    this.letter.add(entry('\u0396', '\u0396')); // Z
    this.letter.add(entry('\u0397', '\u0397')); // H
    this.letter.add(entry('\u0398', '\u0398')); // Θ
    this.letter.add(entry('\u0399', '\u0399')); // I
    this.letter.add(entry('\u039A', '\u039A')); // K
    this.letter.add(entry('\u039B', '\u039B')); // Λ
    this.letter.add(entry('\u039C', '\u039C')); // M
    this.letter.add(entry('\u039D', '\u039D')); // N
    this.letter.add(entry('\u039E', '\u039E')); // Ξ
    this.letter.add(entry('\u039F', '\u039F')); // O
    this.letter.add(entry('\u03A0', '\u03A0')); // Π
    this.letter.add(entry('\u03A1', '\u03A1')); // P
    this.letter.add(entry('\u03A3', '\u03A3')); // Σ
    this.letter.add(entry('\u03A4', '\u03A4')); // T
    this.letter.add(entry('\u03A5', '\u03A5')); // Υ
    this.letter.add(entry('\u03A6', '\u03A6')); // Φ
    this.letter.add(entry('\u03A7', '\u03A7')); // Χ
    this.letter.add(entry('\u03A8', '\u03A8')); // Ψ
    this.letter.add(entry('\u03A9', '\u03A9')); // Ω

    this.letter.add(entry('\u03B1', '\u0391')); // α
    this.letter.add(entry('\u03B2', '\u0392')); // β
    this.letter.add(entry('\u03B3', '\u0393')); // γ
    this.letter.add(entry('\u03B4', '\u0394')); // δ
    this.letter.add(entry('\u03B5', '\u0395')); // ε
    this.letter.add(entry('\u03B6', '\u0396')); // ζ
    this.letter.add(entry('\u03B7', '\u0397')); // η
    this.letter.add(entry('\u03B8', '\u0398')); // θ
    this.letter.add(entry('\u03B9', '\u0399')); // ι
    this.letter.add(entry('\u03BA', '\u039A')); // κ
    this.letter.add(entry('\u03BB', '\u039B')); // λ
    this.letter.add(entry('\u03BC', '\u039C')); // μ
    this.letter.add(entry('\u03BD', '\u039D')); // ν
    this.letter.add(entry('\u03BE', '\u039E')); // ξ
    this.letter.add(entry('\u03BF', '\u039F')); // ο
    this.letter.add(entry('\u03C0', '\u03A0')); // π
    this.letter.add(entry('\u03C1', '\u03A1')); // ρ
    this.letter.add(entry('\u03C3', '\u03A3')); // σ
    this.letter.add(entry('\u03C4', '\u03A4')); // τ
    this.letter.add(entry('\u03C5', '\u03A5')); // υ
    this.letter.add(entry('\u03C6', '\u03A6')); // φ
    this.letter.add(entry('\u03C7', '\u03A7')); // χ
    this.letter.add(entry('\u03C8', '\u03A8')); // ψ
    this.letter.add(entry('\u03C9', '\u03A9')); // ω
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Greek</code> {@link Alphabet}.
   ** properties.
   **
   ** @param  path               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    <code>Greek</code> {@link Alphabet}
   **                            <br>
   **                            Possible object is <code>Greek</code>.
   */
  public static Greek build(final File path) {
    return new Greek(path);
  }
}