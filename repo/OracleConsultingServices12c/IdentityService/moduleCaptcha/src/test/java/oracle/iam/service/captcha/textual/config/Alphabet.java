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

    File        :   Alphabet.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Alphabet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual.config;

import java.util.List;
import java.util.ArrayList;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

////////////////////////////////////////////////////////////////////////////////
// abstract class Alphabet
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** The <code>Alphabet</code> dictionary generator for textual
 ** <code>CAPTCHA</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Alphabet {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The numerical characters of the alphabet. */
  @JsonProperty("digit")
  List<Entry> digit;

  /** The alpha characters of the alphabet. */
  @JsonProperty("letter")
  List<Entry> letter;
  
  /** The abstract path to spool the configuration file. */
  @JsonIgnore
  private final File   path;

  @JsonIgnore
  private final String language;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  public static class Entry {
    @JsonProperty("c")
    public final Character character;
    
    @JsonProperty("a")
    public final String    audio;
    
    Entry(final Character character, final String language, final Character audio) {
      this.character = character;
      this.audio     = String.format("u%2$04x.wav", language, (int)audio);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Alphabet</code>.
   **
   ** @param  path               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  language               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   */
  protected Alphabet(final File path, final String language) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.language = language;
    this.path     = new File(path, String.format("alphabet-%s.json", language));

    // initialize instance
    initializeDigit();
    initializeLetter();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Return the full qualified abstract path.
   **
   ** @return                    the full qualified abstract path.
   **                            <br>
   **                            Possible object is {link File}.
   */
  public final File path() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   language
  /**
   ** Return the language.
   **
   ** @return                    the language.
   **                            <br>
   **                            Possible object is {link String}.
   */
  public final String language() {
    return this.language;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digit
  /**
   ** Return the registered letter.
   **
   ** @return                    the registered letter.
   **                            <br>
   **                            Possible object is {link List} where each
   **                            element is of type {@link Entry}.
   */
  public final List<Entry> letter() {
    return this.letter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digit
  /**
   ** Return the registered digets.
   **
   ** @return                    the registered digets.
   **                            <br>
   **                            Possible object is {link List} where each
   **                            element is of type {@link Entry}.
   */
  public final List<Entry> digit() {
    return this.digit;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeDigit
  /**
   ** Initialize the digit content
   */
  protected void initializeDigit() {
    // all language in common are the digits that are supported.
    this.digit = new ArrayList<>(9);
    this.digit.add(entry('\u0030', '\u0030')); // 0
    this.digit.add(entry('\u0031', '\u0031')); // 1
    this.digit.add(entry('\u0032', '\u0032')); // 2
    this.digit.add(entry('\u0033', '\u0033')); // 3
    this.digit.add(entry('\u0034', '\u0034')); // 4
    this.digit.add(entry('\u0035', '\u0035')); // 5
    this.digit.add(entry('\u0036', '\u0036')); // 6
    this.digit.add(entry('\u0037', '\u0037')); // 7
    this.digit.add(entry('\u0038', '\u0038')); // 8
    this.digit.add(entry('\u0039', '\u0039')); // 9
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLetter
  /**
   ** Initialize the letter content.
   */
  protected abstract void initializeLetter();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeDigit
  /**
   ** factory method to create a mapping <code>Entry</code> between the given
   ** {@link Character} <code>c</code> and a resource location.
   **
   ** @param  c                  the {@link Character} to map.
   **                            <br>
   **                            Allowed object is {@link Character}.
   **
   ** @return                    a mapping <code>Entry</code>.
   **                            <br>
   **                            Possible object is <code>Entry</code>.
   */
  protected final Entry entry(final Character origin, final Character mapped) {
    return new Entry(origin, this.language, mapped);
    
  }
}