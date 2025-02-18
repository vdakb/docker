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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   CharacterUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CharacterUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

////////////////////////////////////////////////////////////////////////////////
// abstract class CharacterUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous character utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class CharacterUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final char    UNICODE_ESCAPE_1     = '\\';
  public static final char    UNICODE_ESCAPE_2     = 'u';

  /** Mask used to test for {@link #isXMLCharacter(int)} */
  public static final byte    MASKXMLCHARACTER     = 1 << 0;
  /** Mask used to test for {@link #isXMLLetter(char)} */
  public static final byte    MASKXMLLETTER        = 1 << 1;
  /** Mask used to test for {@link #isXMLNameStartCharacter(char)} */
  public static final byte    MASKXMLSTARTCHAR     = 1 << 2;
  /** Mask used to test for {@link #isXMLNameCharacter(char)} */
  public static final byte    MASKXMLNAMECHAR      = 1 << 3;
  /** Mask used to test for {@link #isXMLDigit(char)} */
  public static final byte    MASKXMLDIGIT         = 1 << 4;
  /** Mask used to test for {@link #isXMLCombiningChar(char)} */
  public static final byte    MASKXMLCOMBINING     = 1 << 5;
  /** Mask used to test for {@link #isURICharacter(char)} */
  public static final byte    MASKURICHAR          = 1 << 6;
  /** Mask used to test for {@link #isXMLLetterOrDigit(char)} */
  public static final byte    MASKXMLLETTERORDIGIT = MASKXMLLETTER | MASKXMLDIGIT;

  /** the seed array used with LENGTH to populate CHARFLAGS. */
  private static final byte[]   VALUE  = {
    0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x41, 0x01
  , 0x41, 0x49, 0x41, 0x59, 0x4d, 0x01, 0x41, 0x01
  , 0x41, 0x4f, 0x01, 0x4d, 0x01, 0x4f, 0x01, 0x41
  , 0x01, 0x09, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x09, 0x01, 0x29, 0x01, 0x29
  , 0x01, 0x0f, 0x09, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x29
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x29, 0x01, 0x29, 0x01, 0x29
  , 0x01, 0x29, 0x01, 0x29, 0x01, 0x29, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x09, 0x0f, 0x29
  , 0x01, 0x19, 0x01, 0x29, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x29, 0x0f, 0x29
  , 0x01, 0x29, 0x01, 0x19, 0x01, 0x29, 0x01, 0x0f
  , 0x01, 0x29, 0x0f, 0x29, 0x01, 0x29, 0x01, 0x0f
  , 0x29, 0x01, 0x19, 0x01, 0x29, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x29, 0x01, 0x29, 0x01, 0x29, 0x01
  , 0x29, 0x01, 0x29, 0x01, 0x0f, 0x01, 0x0f, 0x29
  , 0x01, 0x19, 0x0f, 0x01, 0x29, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x29, 0x01, 0x29, 0x01
  , 0x29, 0x01, 0x29, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x19, 0x29, 0x0f, 0x01, 0x29, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x29, 0x0f, 0x29, 0x01
  , 0x29, 0x01, 0x29, 0x01, 0x0f, 0x01, 0x19, 0x01
  , 0x29, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x29, 0x0f
  , 0x29, 0x01, 0x29, 0x01, 0x29, 0x01, 0x29, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x19, 0x01, 0x29, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x29, 0x01, 0x29, 0x01
  , 0x29, 0x01, 0x29, 0x01, 0x19, 0x01, 0x29, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x29, 0x01, 0x29, 0x01, 0x29, 0x01
  , 0x29, 0x01, 0x0f, 0x01, 0x19, 0x01, 0x29, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x29, 0x01, 0x29, 0x01, 0x29, 0x01
  , 0x29, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x19, 0x01
  , 0x29, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x29, 0x01, 0x29, 0x01, 0x29, 0x01
  , 0x29, 0x01, 0x0f, 0x01, 0x19, 0x01, 0x0f, 0x01
  , 0x0f, 0x29, 0x0f, 0x29, 0x01, 0x0f, 0x09, 0x29
  , 0x01, 0x19, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x0f, 0x29, 0x0f, 0x29, 0x01
  , 0x29, 0x0f, 0x01, 0x0f, 0x01, 0x09, 0x01, 0x29
  , 0x01, 0x19, 0x01, 0x29, 0x01, 0x19, 0x01, 0x29
  , 0x01, 0x29, 0x01, 0x29, 0x01, 0x29, 0x0f, 0x01
  , 0x0f, 0x01, 0x29, 0x01, 0x29, 0x01, 0x29, 0x01
  , 0x29, 0x01, 0x29, 0x01, 0x29, 0x01, 0x29, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x29, 0x01
  , 0x29, 0x01, 0x0f, 0x01, 0x0f, 0x01, 0x0f, 0x01
  , 0x0f, 0x01, 0x09, 0x01, 0x0f, 0x01, 0x0f, 0x29
  , 0x01, 0x09, 0x01, 0x0f, 0x01, 0x29, 0x01, 0x09
  , 0x01, 0x0f, 0x01, 0x09, 0x01, 0x0f, 0x01, 0x0f
  , 0x01, 0x0f, 0x01, 0x00, 0x01, 0x00};

  /** the seed array used with VALUE to populate CHARFLAGS. */
  private static final int [] LENGTH = {
       9,     2,     2,     1,    18,     1,     1,     2
  ,    9,     2,     1,    10,     1,     2,     1,     1
  ,    2,    26,     4,     1,     1,    26,     3,     1
  ,   56,     1,     8,    23,     1,    31,     1,    58
  ,    2,    11,     2,     8,     1,    53,     1,    68
  ,    9,    36,     3,     2,     4,    30,    56,    89
  ,   18,     7,    14,     2,    46,    70,    26,     2
  ,   36,     1,     1,     3,     1,     1,     1,    20
  ,    1,    44,     1,     7,     3,     1,     1,     1
  ,    1,     1,     1,     1,     1,    18,    13,    12
  ,    1,    66,     1,    12,     1,    36,     1,     4
  ,    9,    53,     2,     2,     2,     2,     3,    28
  ,    2,     8,     2,     2,    55,    38,     2,     1
  ,    7,    38,    10,    17,     1,    23,     1,     3
  ,    1,     1,     1,     2,     1,     1,    11,    27
  ,    5,     3,    46,    26,     5,     1,    10,     8
  ,   13,    10,     6,     1,    71,     2,     5,     1
  ,   15,     1,     4,     1,     1,    15,     2,     2
  ,    1,     4,     2,    10,   519,     3,     1,    53
  ,    2,     1,     1,    16,     3,     4,     3,    10
  ,    2,     2,    10,    17,     3,     1,     8,     2
  ,    2,     2,    22,     1,     7,     1,     1,     3
  ,    4,     2,     1,     1,     7,     2,     2,     2
  ,    3,     9,     1,     4,     2,     1,     3,     2
  ,    2,    10,     2,    16,     1,     2,     6,     4
  ,    2,     2,    22,     1,     7,     1,     2,     1
  ,    2,     1,     2,     2,     1,     1,     5,     4
  ,    2,     2,     3,    11,     4,     1,     1,     7
  ,   10,     2,     3,    12,     3,     1,     7,     1
  ,    1,     1,     3,     1,    22,     1,     7,     1
  ,    2,     1,     5,     2,     1,     1,     8,     1
  ,    3,     1,     3,    18,     1,     5,    10,    17
  ,    3,     1,     8,     2,     2,     2,    22,     1
  ,    7,     1,     2,     2,     4,     2,     1,     1
  ,    6,     3,     2,     2,     3,     8,     2,     4
  ,    2,     1,     3,     4,    10,    18,     2,     1
  ,    6,     3,     3,     1,     4,     3,     2,     1
  ,    1,     1,     2,     3,     2,     3,     3,     3
  ,    8,     1,     3,     4,     5,     3,     3,     1
  ,    4,     9,     1,    15,     9,    17,     3,     1
  ,    8,     1,     3,     1,    23,     1,    10,     1
  ,    5,     4,     7,     1,     3,     1,     4,     7
  ,    2,     9,     2,     4,    10,    18,     2,     1
  ,    8,     1,     3,     1,    23,     1,    10,     1
  ,    5,     4,     7,     1,     3,     1,     4,     7
  ,    2,     7,     1,     1,     2,     4,    10,    18
  ,    2,     1,     8,     1,     3,     1,    23,     1
  ,   16,     4,     6,     2,     3,     1,     4,     9
  ,    1,     8,     2,     4,    10,   145,    46,     1
  ,    1,     1,     2,     7,     5,     6,     1,     8
  ,    1,    10,    39,     2,     1,     1,     2,     2
  ,    1,     1,     2,     1,     6,     4,     1,     7
  ,    1,     3,     1,     1,     1,     1,     2,     2
  ,    1,     2,     1,     1,     1,     2,     6,     1
  ,    2,     1,     2,     5,     1,     1,     1,     6
  ,    2,    10,    62,     2,     6,    10,    11,     1
  ,    1,     1,     1,     1,     4,     2,     8,     1
  ,   33,     7,    20,     1,     6,     4,     6,     1
  ,    1,     1,    21,     3,     7,     1,     1,   230
  ,   38,    10,    39,     9,     1,     1,     2,     1
  ,    3,     1,     1,     1,     2,     1,     5,    41
  ,    1,     1,     1,     1,     1,    11,     1,     1
  ,    1,     1,     1,     3,     2,     3,     1,     5
  ,    3,     1,     1,     1,     1,     1,     1,     1
  ,    1,     3,     2,     3,     2,     1,     1,    40
  ,    1,     9,     1,     2,     1,     2,     2,     7
  ,    2,     1,     1,     1,     7,    40,     1,     4
  ,    1,     8,     1,  3078,   156,     4,    90,     6
  ,   22,     2,     6,     2,    38,     2,     6,     2
  ,    8,     1,     1,     1,     1,     1,     1,     1
  ,   31,     2,    53,     1,     7,     1,     1,     3
  ,    3,     1,     7,     3,     4,     2,     6,     4
  ,   13,     5,     3,     1,     7,   211,    13,     4
  ,    1,    68,     1,     3,     2,     2,     1,    81
  ,    3,  3714,     1,     1,     1,    25,     9,     6
  ,    1,     5,    11,    84,     4,     2,     2,     2
  ,    2,    90,     1,     3,     6,    40,  7379, 20902
  , 3162, 11172,    92,  2048,  8190,     2};

  /** the number of characters in Java. */
  private static final int    CHARCNT              = Character.MAX_VALUE + 1;

  private static final String UNICODE_ENCODE_0     = "\\u0";
  private static final String UNICODE_ENCODE_1     = UNICODE_ENCODE_0 + "0";
  private static final String UNICODE_ENCODE_2     = UNICODE_ENCODE_0 + "00";
  private static final String UNICODE_ENCODE_3     = UNICODE_ENCODE_0 + "000";

  /**
   ** An array of byte where each byte represents the roles that the
   ** corresponding character can play. Use the bit mask values to access each
   ** character's role.
   ** <p>
   ** Don't move this declaration to anywhere else it's highly depended of the
   ** order to initialize statics.
   */
  private static final byte[] CHARFLAGS            = buildMasks();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CharacterUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new CharacterUtility()" and enforces use of the public method below.
   */
  private CharacterUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAscii
  /**
   ** Whether the specified {@link Character} is an ASCII character.
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            character; otherwise <code>false</code>.
   */
  public static boolean isAscii(final Character ch) {
    return isAscii(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAscii
  /**
   ** Whether the specified character is an ASCII character.
   **
   ** @param  ch                 char to be tested.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            character; otherwise <code>false</code>.
   */
  public static boolean isAscii(final char ch) {
    return ch < 128;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAsciiPrintable
  /**
   ** Whether the specified {@link Character} is an ASCII printable character.
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            printable character; otherwise
   **                            <code>false</code>.
   */
  public static boolean isAsciiPrintable(final Character ch) {
    return isAsciiPrintable(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAsciiPrintable
  /**
   ** Whether the specified character is an ASCII printable character.
   **
   ** @param  ch                 char to be tested.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            printable character; otherwise
   **                            <code>false</code>.
   */
  public static boolean isAsciiPrintable(final char ch) {
    return ch >= 32 && ch < 128;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isControl
  /**
   ** Whether the specified {@link Character} a control character.
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if the subject is a control
   **                            character; otherwise <code>false</code>.
   */
  public static boolean isControl(final Character ch) {
    return isControl(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isControl
  /**
   ** Whether the specified character a control character.
   **
   ** @param  ch                 char to be tested.
   **
   ** @return                    <code>true</code> if the subject is a control
   **                            character; otherwise <code>false</code>.
   */
  public static boolean isControl(final char ch) {
    return (ch < ' ') || (ch == '');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isWhitespace
  /**
   ** Is specified {@link Character} a whitespace character according to
   ** production 3 of the XML 1.0 specification.
   **
   ** @param ch                  {@link Character} to check for XML whitespace
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a whitespace;
   **                            otherwise <code>false</code>.
   */
  public static boolean isWhitespace(final Character ch) {
    return isWhitespace(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isWhitespace
  /**
   ** Is specified character a whitespace character according to production 3 of
   ** the XML 1.0 specification.
   **
   ** @param ch                  <code>char</code> to check for XML whitespace
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a whitespace;
   **                            otherwise <code>false</code>.
   */
  public static boolean isWhitespace(final char ch) {
    // most of the characters are non-control characters.
    // so check that first to quickly return false for most of the cases.
    if (ch > 0x20)
      return false;

    // other than we have to do four comparisons.
    // the following if is faster than switch statements.
    // seems the implicit conversion to int is slower than the fall-through or's
    return ch == 0x09 || ch == 0x0a || ch == 0x0d || ch == 0x20;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAlphaNumeric
  /**
   ** Is the specified {@link Character} an alphanumeric character?
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if the subject is an
   **                            alphanumeric character character; otherwise
   **                            <code>false</code>.
   */
  public static boolean isAlphaNumeric(final Character ch) {
    return isAlphaNumeric(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAlphaNumeric
  /**
   ** Is the specified character an alphanumeric character?
   **
   ** @param  ch                 <code>char</code> to be tested.
   **
   ** @return                    <code>true</code> if the subject is an
   **                            alphanumeric character character; otherwise
   **                            <code>false</code>.
   */
  public static boolean isAlphaNumeric(final char ch) {
    return (isAlpha(ch) || isDigit(ch));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isHexDigit
  /**
   ** Determins whether a specified Unicode {@link Character} is a hexadecimal
   ** digit as defined in RFC 2396; that is, one of the ASCII characters 0-9,
   ** a-f, or A-F.
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if it's a hex digit;
   **                            otherwise <code>false</code>.
   */
  public static boolean isHexDigit(final Character ch) {
    return isHexDigit(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isHexDigit
  /**
   ** Determins whether a specified Unicode character is a hexadecimal digit as
   ** defined in RFC 2396; that is, one of the ASCII characters 0-9, a-f, or
   ** A-F.
   **
   ** @param  ch                 <code>char</code> to be tested.
   **
   ** @return                    <code>true</code> if it's a hex digit;
   **                            otherwise <code>false</code>.
   */
  public static boolean isHexDigit(final char ch) {
    // we suspect most characters passed to this method will be correct
    // hexadecimal digits, so we test for the true cases first.
    // If this proves to be a performance bottleneck a switch statement or
    // lookup table might optimize this.
    return (isDigit(ch)) ? true : ((ch >= 'A' && ch <= 'F') ||  (ch >= 'a' && ch <= 'f'));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDigit
  /**
   ** Is the specified {@link Character} a numeric character?
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if it's a numeric character;
   **                            otherwise <code>false</code>.
   */
  public static boolean isDigit(final Character ch) {
    return isDigit(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDigit
  /**
   ** Is the specified character a numeric character?
   **
   ** @param  ch                 <code>char</code> to be tested.
   **
   ** @return                    <code>true</code> if it's a numeric character;
   **                            otherwise <code>false</code>.
   */
  public static boolean isDigit(final char ch) {
    return (ch >= '0') && (ch <= '9');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isOctalDigit
  /**
   ** Is the specified {@link Character} an octal digit 0 through 7?
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if the subject is an octal
   **                            digit 0 through 7; otherwise <code>false</code>.
   */
  public static boolean isOctalDigit(final Character ch) {
    if (ch == null)
      return false;

    return isOctalDigit(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isOctalDigit
  /**
   ** Is the specified character an octal digit 0 through 7?
   **
   ** @param  ch                 <code>cahr</code> to be tested.
   **
   ** @return                    <code>true</code> if the subject is an octal
   **                            digit 0 through 7; otherwise <code>false</code>.
   */
  public static boolean isOctalDigit(final char ch) {
    return ch >= '0' && ch <= '7';
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAlpha
  /**
   ** Is the specified {@link Character} an alpha character?
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if the subject is an alpha
   **                            character; otherwise <code>false</code>.
   */
  public static boolean isAlpha(final Character ch) {
    return isAlpha(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAlpha
  /**
   ** Is the specified character an alpha character?
   **
   ** @param  ch                 <code>char</code> to be tested.
   **
   ** @return                    <code>true</code> if the subject is an alpha
   **                            character; otherwise <code>false</code>.
   */
  public static boolean isAlpha(final char ch) {
    return (isAlphaUpper(ch) || isAlphaLower(ch));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAlphaLower
  /**
   ** Is the specified {@link Character} an lower case alpha character?
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if the subject is a lower
   **                            case alpha {@link Character}; otherwise
   **                            <code>false</code>.
   */
  public static boolean isAlphaLower(final Character ch) {
    return isAlphaLower(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAlphaLower
  /**
   ** Is the specified character an lower case alpha character?
   **
   ** @param  ch                 <code>char</code> to be tested.
   **
   ** @return                    <code>true</code> if the subject is a lower
   **                            case alpha character character; otherwise
   **                            <code>false</code>.
   */
  public static boolean isAlphaLower(final char ch) {
    return (ch >= 'a') && (ch <= 'z');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAlphaUpper
  /**
   ** Is the specified {@link Character} an upper case alpha character?
   **
   ** @param  ch                 {@link Character} to be tested.
   **
   ** @return                    <code>true</code> if the subject is an upper
   **                            case alpha character character; otherwise
   **                            <code>false</code>.
   */
  public static boolean isAlphaUpper(final Character ch) {
    return isAlphaUpper(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAlphaUpper
  /**
   ** Is the specified character an upper case alpha character?
   **
   ** @param  ch                 <code>char</code> to be tested.
   **
   ** @return                    <code>true</code> if the subject is an upper
   **                            case alpha character character; otherwise
   **                            <code>false</code>.
   */
  public static boolean isAlphaUpper(final char ch) {
    return (ch >= 'A') && (ch <= 'Z');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDelimiter
  /**
   ** Is the character a delimiter.
   **
   ** @param  character          the character to check
   ** @param  delimiters         the delimiters
   **
   ** @return                     <code>true</code> if it is a delimiter;
   **                             <code>false</code> otherwise
   */
  public static boolean isDelimiter(char character, char[] delimiters) {
    if (delimiters == null)
      return Character.isWhitespace(character);

    for (int i = 0, isize = delimiters.length; i < isize; i++)
      if (character == delimiters[i])
        return true;

    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unicodeEscaped
  /**
   ** Converts the specified {@link Character} to the unicode string.
   ** <p>
   ** The format is the Java source code format.
   **
   ** @param  ch                 the character to convert.
   **
   ** @return                   the escaped unicode string.
   */
  public static String unicodeEscaped(final Character ch) {
    return (ch == null) ? null : unicodeEscaped(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unicodeEscaped
  /**
   ** Converts the specified character to the unicode string.
   ** <p>
   ** The format is the Java source code format.
   **
   ** @param  ch                 the character to convert.
   **
   ** @return                    the escaped unicode string.
   */
  public static String unicodeEscaped(final char ch) {
    if (ch < '\020')
      return UNICODE_ENCODE_3 + Integer.toHexString(ch);
    else if (ch < '?')
      return UNICODE_ENCODE_2 + Integer.toHexString(ch);
    else if (ch >= '?')
      return UNICODE_ENCODE_1 + Integer.toHexString(ch);

    return UNICODE_ENCODE_0 + Integer.toHexString(ch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isHighSurrogate
  /**
   ** This is a function for determining whether the specified character is the
   ** high 16 bits in a UTF-16 surrogate pair.
   ** <p>
   ** A high surrogate has the bit pattern:
   ** <pre>
   **   110110xx xxxxxxxx
   ** </pre>
   ** <code>ch &amp; 0xFC00</code> does a bit-mask of the most significant 6 bits
   ** (110110)  <code>return 0xD800 == (ch &amp; 0xFC00);</code> as it happens,
   ** it is faster to do a bit-shift,
   **
   ** @param ch                  the character to check.
   **
   ** @return                    <code>true</code> if the character is a high
   **                            surrogate, <code>false</code> otherwise.
   **
	 ** @since  1.0.1.0
   */
  public static boolean isHighSurrogate(final char ch) {
    // faster way to do it is with bit manipulation as to write
    //   return (ch >= 0xD800 && ch <= 0xDBFF);
    return 0x36 == ch >>> 10;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isLowSurrogate
  /**
   ** This is a function for determining whether the specified character is the
   ** low 16 bits in a UTF-16 surrogate pair.
   **
   ** @param ch                  the character to check.
   **
   ** @return                    <code>true</code> if the character is a low
   **                            surrogate, <code>false</code> otherwise.
   **
	 ** @since  1.0.1.0
   */
  public static boolean isLowSurrogate(final char ch) {
    // faster way to do it is with bit manipulation as to write
    //   return (ch >= 0xDC00 && ch <= 0xDFFF);
    return 0x37 == ch >>> 10;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeSurrogate
  /**
   ** This is a utility function to decode a non-BMP UTF-16 surrogate pair.
   **
   ** @param  high               the 16 high bits.
   ** @param low                 the 16 low bits.
   **
   ** @return                    decoded character
   **
	 ** @since  1.0.1.0
   */
  public static int decodeSurrogate(final char high, final char low) {
    return 0x10000 + (high - 0xD800) * 0x400 + (low - 0xDC00);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isXMLLetter
  /**
   ** Determins whether a specified character is a letter according to
   ** production 84 of the XML 1.0 specification.
   **
   ** @param  ch                 <code>char</code> to check for XML name
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a letter; otherwise
   **                            <code>false</code>.
   **
   ** @since  1.0.1.0
   */
  public static boolean isXMLLetter(final char ch) {
    return (byte)0 != (byte)(CHARFLAGS[ch] & MASKXMLLETTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isXMLDigit
  /**
   ** This is a utility function for determining whether a specified  Unicode
   ** character is a digit according to production 88 of the XML 1.0
   ** specification.
   **
   ** @param  ch                 <code>char</code> to check for XML digit
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a digit; otherwise
   **                            <code>false</code>.
   **
   ** @since  1.0.1.0
   */
  public static boolean isXMLDigit(final char ch) {
    return (byte)0 != (byte)(CHARFLAGS[ch] & MASKXMLDIGIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isXMLLetterOrDigit
  /**
   ** Determins whether a specified character is a letter or digit according to
   ** productions 84 and 88 of the XML 1.0 specification.
   **
   ** @param  ch                 <code>char</code> to check for XML
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a letter or digit;
   **                            otherwise <code>false</code>.
   **
   ** @since  1.0.1.0
   */
  public static boolean isXMLLetterOrDigit(final char ch) {
    return (byte)0 != (byte)(CHARFLAGS[ch] & MASKXMLLETTERORDIGIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isXMLCharacter
  /**
   ** Determins whether a specified character is a character according to
   ** production 2 of the XML 1.0 specification.
   **
   ** @param  ch                 <code>char</code> to check for XML
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a character;
   **                            otherwise <code>false</code>.
   **
   ** @since  1.0.1.0
   */
  public static boolean isXMLCharacter(final int ch) {
    return (ch >= CHARCNT) ? ch <= 0x10FFFF : (byte)0 != (byte)(CHARFLAGS[ch] & MASKXMLCHARACTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isXMLCombiningChar
  /**
   ** Determins whether a specified character is a combining character according
   ** to production 87 of the XML 1.0 specification.
   **
   ** @param  ch                 <code>char</code> to check for XML name
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a combining
   **                            character; otherwise <code>false</code>.
   **
   ** @since  1.0.1.0
   */
  public static boolean isXMLCombiningChar(final char ch) {
    return (byte)0 != (byte)(CHARFLAGS[ch] & MASKXMLCOMBINING);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isXMLNameStartCharacter
  /**
   ** Determins whether a specified character is a name character according to
   ** production 4 of the XML 1.0 specification.
   **
   ** @param  ch                 <code>char</code> to check for XML name
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a name character;
   **                            otherwise <code>false</code>.
   **
   ** @since  1.0.1.0
   */
  public static boolean isXMLNameCharacter(final char ch) {
    return (byte)0 != (byte)(CHARFLAGS[ch] & MASKXMLNAMECHAR) || ch == ':';
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isXMLNameStartCharacter
  /**
   ** Determins whether a specified character is a legal name start character
   ** according to production 5  of the XML 1.0 specification. This production
   ** does allow names to begin with colons which the Namespaces in XML
   ** Recommendation disallows.
   **
   ** @param  ch                 <code>char</code> to check for XML name start
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a name start
   **                            character; otherwise <code>false</code>.
   **
   ** @since  1.0.1.0
   */
  public static boolean isXMLNameStartCharacter(final char ch) {
    return (byte)0 != (byte)(CHARFLAGS[ch] & MASKXMLSTARTCHAR) || ch == ':';
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isWhitespace
  /**
   ** Determins whether a specified String is a whitespace character according
   ** to production 3 of the XML 1.0 specification.
   ** <p>
   ** This method delegates the individual calls for each character to
   ** {@link #isWhitespace(char)}.
   **
   ** @param  value              the value to inspect.
   **
   ** @return                    <code>true</code> if all characters in the
   **                            input value are all whitespace (or the string
   **                            is the empty-string).
   **
   ** @since  1.0.1.0
   */
  public static final boolean isWhitespace(final String value) {
    // doing the count-down instead of a count-up saves a single int
    // variable declaration.
    int i = value.length();
    while (--i >= 0) {
      if (!isWhitespace(value.charAt(i)))
       return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isURICharacter
  /**
   ** Determins whether a specified Unicode character is legal in URI references
   ** as determined by RFC 2396.
   **
   ** @param  ch                <code>char</code> to check for URI reference
   **                           compliance.
   **
   ** @return                    <code>true</code> if it's a URI reference;
   **                            otherwise <code>false</code>.
   */
  public static boolean isURICharacter(final char ch) {
    return (byte)0 != (byte)(CHARFLAGS[ch] & MASKURICHAR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   union
  /**
   ** Union multiple character arrays.
   **
   ** @param  list               the char[]s to union
   **
   ** @return                     the union of the char[]s
   */
  public static char[] union(char[]... list) {
    final StringBuilder builder = new StringBuilder();
    for (char[] characters : list) {
      for (int i = 0; i < list.length; i++) {
        if (!contains(builder, characters[i]))
          builder.append(list[i]);
      }
    }

    char[] union = new char[builder.length()];
    builder.getChars(0, builder.length(), union, 0);
    Arrays.sort(union);
    return union;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Returns <code>true</code> if the character <code>c</code> is contained in
   ** the provided {@link StringBuilder}.
   **
   ** @param  input 	           the {@link StringBuilder} to verify.
   ** @param  c 		             the character to check for to see if
   **                            <code>input</code> contains.
   **
   ** @return			               <code>true</code> if the specified character
   **                            <code>c</code> is contained; <code>false</code>
   **                            otherwise.
   */
  public static boolean contains(final StringBuilder input, final char c) {
    for (int i = 0; i < input.length(); i++) {
      if (input.charAt(i) == c)
        return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrayToUnmodifiableSet
  /**
   ** Converts an array of chars to a unmodifiable {@link Set} of
   ** {@link Character}s.
   **
   ** @param  array              the contents of the new {@link Set}.
   **
   ** @return                    a unmodifiable {@link Set} containing the
   **                            elements in the character array.
   */
  public static Set<Character> arrayToUnmodifiableSet(final char... array) {
    if (array == null || array.length == 0)
      return Collections.emptySet();

    if (array.length == 1)
      return Collections.singleton(array[0]);

    return Collections.unmodifiableSet(arrayToSet(array));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrayToSet
  /**
   ** Converts an array of chars to a {@link Set} of {@link Character}s.
   **
   ** @param  array              the contents of the new {@link Set}.
   **
   ** @return                    a {@link Set} containing the elements in the
   **                            character array.
   */
  public static Set<Character> arrayToSet(final char... array) {
    if (array == null || array.length == 0)
      return new HashSet<Character>();

    final Set<Character> set = new HashSet<Character>(array.length);
    for (char c : array)
      set.add(c);

    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringToUnmodifiableSet
  /**
   ** Convert a String to a unmodifiable {@link Set} of {@link Character}s.
   **
   ** @param  string              the contents of the new {@link Set}.
   **
   ** @return                    a unmodifiable {@link Set} containing the
   **                            characters in the sring. A empty {@link Set} is
   **                            returned if <code>string</code> is
   **                            <code>null</code>.
   */
  public static Set<Character> stringToUnmodifiableSet(final String string) {
    // prevent bogus input
    if (string == null || string.length() == 0)
      return Collections.emptySet();

    if (string.length() == 1)
      return Collections.singleton(string.charAt(0));

    return Collections.unmodifiableSet(stringToSet(string));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringToSet
  /**
   ** Convert a String to a {@link Set} of {@link Character}s.
   **
   ** @param  string              the contents of the new {@link Set}.
   **
   ** @return                    a {@link Set} containing the characters in the
   **                            sring. A empty {@link Set} is returned if
   **                            <code>string</code> is <code>null</code>.
   */
  public static Set<Character> stringToSet(final String string) {
    // prevent bogus input
    if (string == null || string.length() == 0)
      return new HashSet<Character>();

    final Set<Character> set = new HashSet<Character>(string.length());
    for (int i = 0; i < string.length(); i++)
      set.add(string.charAt(i));
    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears an array of potentially sensitive bytes.
   **
   ** @param  bytes              the bytes, may be <code>null</code>.
   */
  public static void clear(final byte[] bytes) {
    if (bytes != null) {
      Arrays.fill(bytes, (byte)0);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears an array of potentially sensitive characters.
   **
   ** @param  ch                 the characters, may be <code>null</code>.
   */
  public static void clear(final char[] ch) {
    if (ch != null) {
      Arrays.fill(ch, (char)0);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   charactersToBytes
  /**
   ** Converts characters to bytes without using any external functions that
   ** might allocate additional buffers for the potentially sensitive data.
   ** <br>
   ** This guarantees the caller that they only need to cleanup the input and
   ** result.
   **
   ** @param  ch                 the characters to convert into bytes.
   **
   ** @return                    the bytes converted from the specified
   **                            characters.
   */
  public static byte[] charactersToBytes(final char[] ch) {
    final byte[] bytes = new byte[ch.length * 2];
    for (int i = 0; i < ch.length; i++) {
      final char v = ch[i];
      bytes[i * 2] = (byte)(0xff & (v >> 8));
      bytes[i * 2 + 1] = (byte)(0xff & (v));
    }
    return bytes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytesToCharacters
  /**
   ** Converts bytes to characters without using any external functions that
   ** might allocate additional buffers for the potentially sensitive data.
   ** <br>
   ** This guarantees the caller that they only need to cleanup the input and
   ** result.
   **
   ** @param  bytes              the bytes to convert into characters.
   **
   ** @return                    the characters converted from the specified
   **                            bytes.
   */
  public static char[] bytesToCharacters(final byte[] bytes) {
    final char[] ch = new char[bytes.length / 2];
    for (int i = 0; i < ch.length; i++) {
      final char c = (char)(((0xFF & (bytes[i * 2])) << 8) | (0xFF & bytes[i * 2 + 1]));
      ch[i] = c;
    }
    return ch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildMasks
  /**
   ** Convert the two compressed arrays in to th CHARFLAGS array.
   **
   ** @return                   the CHARFLAGS array.
   */
  private static final byte[] buildMasks() {
    final byte[] masks = new byte[CHARCNT];
    int index = 0;
    for (int i = 0; i < VALUE.length; i++) {
      // v represents the roles a character can play.
      final byte v = VALUE[i];
      // l is the number of consecutive chars that have the same roles 'v'
      int l = LENGTH[i];
      // we need to give the next 'l' chars the role bits 'v'
      while (--l >= 0)
        masks[index++] = v;
    }
    return masks;
  }
}