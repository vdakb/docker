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

    File        :   Bulgarian.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Bulgarian.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual.config;

import java.io.File;

import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////
// class Bulgarian
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>Bulgarian</code> {@link Alphabet} dictionary generator for
 ** textual <code>CAPTCHA</code>s.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code bulgarian (bg)
 **   <li>country  code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bulgarian extends Alphabet {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Bulgarian</code> {@link Alphabet}.
   **
   ** @param  path               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   */
  private Bulgarian(final File path) {
    // ensure inheritance
    super(path, "bg");
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
    this.letter.add(entry('\u0410', '\u0410')); // А
    this.letter.add(entry('\u0411', '\u0411')); // Б
    this.letter.add(entry('\u0412', '\u0412')); // В
    this.letter.add(entry('\u0413', '\u0413')); // Г
    this.letter.add(entry('\u0414', '\u0414')); // Д
    this.letter.add(entry('\u0415', '\u0415')); // Е
    this.letter.add(entry('\u0416', '\u0416')); // Ж
    this.letter.add(entry('\u0417', '\u0417')); // З
    this.letter.add(entry('\u0418', '\u0418')); // И
    this.letter.add(entry('\u0419', '\u0419')); // Й
    this.letter.add(entry('\u041A', '\u041A')); // К
    this.letter.add(entry('\u041B', '\u041B')); // Л
    this.letter.add(entry('\u041C', '\u041C')); // М
    this.letter.add(entry('\u041D', '\u041D')); // Н
    // uppercase letter '\u041E' is not applicable
    this.letter.add(entry('\u041E', '\u041E')); // О
    this.letter.add(entry('\u041F', '\u041F')); // П
    this.letter.add(entry('\u0420', '\u0420')); // Р
    this.letter.add(entry('\u0421', '\u0421')); // С
    this.letter.add(entry('\u0422', '\u0422')); // Т
    this.letter.add(entry('\u0423', '\u0423')); // У
    this.letter.add(entry('\u0424', '\u0424')); // Ф
    this.letter.add(entry('\u0425', '\u0425')); // Х
    this.letter.add(entry('\u0426', '\u0426')); // Ц
    this.letter.add(entry('\u0427', '\u0427')); // Ч
    this.letter.add(entry('\u0428', '\u0428')); // Ш
    this.letter.add(entry('\u0429', '\u0429')); // Щ
    // uppercase letter '\u042A' is not applicable
    // uppercase letter '\u042B' is not applicable
    // uppercase letter '\u042C' is not applicable
    // uppercase letter '\u042C' is not applicable
    this.letter.add(entry('\u042E', '\u042E')); // Ю
    this.letter.add(entry('\u042F', '\u042F')); // Я

    this.letter.add(entry('\u0430', '\u0410')); // а
    this.letter.add(entry('\u0431', '\u0411')); // б
    this.letter.add(entry('\u0432', '\u0412')); // в
    this.letter.add(entry('\u0433', '\u0413')); // г
    this.letter.add(entry('\u0434', '\u0414')); // д
    this.letter.add(entry('\u0435', '\u0415')); // е
    this.letter.add(entry('\u0436', '\u0416')); // ж
    this.letter.add(entry('\u0437', '\u0417')); // з
    this.letter.add(entry('\u0438', '\u0418')); // и
    this.letter.add(entry('\u0439', '\u0419')); // й
    this.letter.add(entry('\u043A', '\u041A')); // к
    this.letter.add(entry('\u043B', '\u041B')); // л
    this.letter.add(entry('\u043C', '\u041C')); // м
    this.letter.add(entry('\u043D', '\u041D')); // н
    // uppercase letter '\u043E' is not applicable
    this.letter.add(entry('\u043E', '\u041E')); // о
    this.letter.add(entry('\u043F', '\u041F')); // п
    this.letter.add(entry('\u0440', '\u0420')); // р
    this.letter.add(entry('\u0441', '\u0421')); // с
    this.letter.add(entry('\u0442', '\u0422')); // т
    this.letter.add(entry('\u0443', '\u0423')); // у
    this.letter.add(entry('\u0444', '\u0424')); // ф
    this.letter.add(entry('\u0445', '\u0425')); // х
    this.letter.add(entry('\u0446', '\u0426')); // ц
    this.letter.add(entry('\u0447', '\u0427')); // ч
    this.letter.add(entry('\u0448', '\u0428')); // ш
    this.letter.add(entry('\u0449', '\u0429')); // щ
    // uppercase letter '\u044A' is not applicable
    // uppercase letter '\u044B' is not applicable
    // uppercase letter '\u044C' is not applicable
    // uppercase letter '\u044D' is not applicable
    this.letter.add(entry('\u044E', '\u042E')); // ю
    this.letter.add(entry('\u044F', '\u042F')); // я
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Bulgarian</code> {@link Alphabet}.
   ** properties.
   **
   ** @param  path               the abstract path to spool the configuration
   **                            file.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    <code>Bulgarian</code> {@link Alphabet}
   **                            <br>
   **                            Possible object is <code>Bulgarian</code>.
   */
  public static Bulgarian build(final File path) {
    return new Bulgarian(path);
  }
}