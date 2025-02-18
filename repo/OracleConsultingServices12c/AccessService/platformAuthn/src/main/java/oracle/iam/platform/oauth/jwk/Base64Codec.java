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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   Base64Codec.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Base64Codec.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Arrays;

import java.nio.charset.Charset;

////////////////////////////////////////////////////////////////////////////////
// final class Base64Codec
// ~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 **
 ** *** Timing-protected (tp) utility methods ***
 ** This class is used to encode/decode private keys, so we make an effort to
 ** prevent side channel leaks. Here we define a number of timing leak resistant
 ** utility methods. Boolean values are stored in ints in order to prevent
 ** optimizations that would reintroduce timing leaks.
 ** <p>
 ** Some background information on preventing side channel leaks:
 ** https://www.chosenplaintext.ca/articles/beginners-guide-constant-time-cryptography.html
 */
public final class Base64Codec {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** UTF-8 */
  public static final Charset UTF8 = Charset.forName("UTF-8");

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: encodeToString
  /**
   ** Encodes a byte array into a base 64 encoded string.
   **
   ** @param  byteArray          the bytes to convert.
   **                            If <code>null</code> or length 0 an empty array
   **                            will be returned.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   ** @param  safe               <code>true</code> to apply URL-safe encoding
   **                            (padding still included and not to spec).
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the base 64 encoded string.
   **                            <br>
   **                            Never <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String encodeToString(final byte[] byteArray, final boolean safe) {
    // check special case
    final int sLen = byteArray != null ? byteArray.length : 0;
    if (sLen == 0) {
      return "";
    }

    // length of even 24-bits
    final int eLen = (sLen / 3) * 3;
    // returned byte count
    final int dLen = computeEncodedLength(sLen, safe);
    final byte[] out = new byte[dLen];
    // encode even 24-bits
    for (int s = 0, d = 0; s < eLen; ) {
      // copy next three bytes into lower 24 bits of int, paying attention to
      // sign
      final int i = (byteArray[s++] & 0xff) << 16 | (byteArray[s++] & 0xff) << 8 | (byteArray[s++] & 0xff);
      // encode the int into four chars
      if (safe) {
        out[d++] = encodeDigitBase64URL((i >>> 18) & 0x3f);
        out[d++] = encodeDigitBase64URL((i >>> 12) & 0x3f);
        out[d++] = encodeDigitBase64URL((i >>> 6) & 0x3f);
        out[d++] = encodeDigitBase64URL(i & 0x3f);
      }
      else {
        out[d++] = encodeDigitBase64((i >>> 18) & 0x3f);
        out[d++] = encodeDigitBase64((i >>> 12) & 0x3f);
        out[d++] = encodeDigitBase64((i >>> 6) & 0x3f);
        out[d++] = encodeDigitBase64(i & 0x3f);
      }
    }
    // pad and encode last bits if source isn't even 24 bits according to
    // URL-safe switch
    final int left = sLen - eLen; // 0 - 2.
    if (left > 0) {
      // prepare the int
      final int i = ((byteArray[eLen] & 0xff) << 10) | (left == 2 ? ((byteArray[sLen - 1] & 0xff) << 2) : 0);
      // set last four chars
      if (safe) {
        if (left == 2) {
          out[dLen - 3] = encodeDigitBase64URL(i >> 12);
          out[dLen - 2] = encodeDigitBase64URL((i >>> 6) & 0x3f);
          out[dLen - 1] = encodeDigitBase64URL(i & 0x3f);
        }
        else {
          out[dLen - 2] = encodeDigitBase64URL(i >> 12);
          out[dLen - 1] = encodeDigitBase64URL((i >>> 6) & 0x3f);
        }
      }
      else {
        // original mig code with padding
        out[dLen - 4] = encodeDigitBase64(i >> 12);
        out[dLen - 3] = encodeDigitBase64((i >>> 6) & 0x3f);
        out[dLen - 2] = left == 2 ? encodeDigitBase64(i & 0x3f) : (byte)'=';
        out[dLen - 1] = (byte) '=';
      }
    }
    return new String(out, UTF8);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: decode
  /**
   ** Decodes a base 64 or base 64 URL-safe encoded string. May contain line
   ** separators. Any illegal characters are ignored.
   **
   ** @param  b64String          the base 64 or base 64 URL-safe encoded string.
   **                            <br>
   **                            May be empty or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the decoded byte array, empty if the input base
   **                            64 encoded string is empty, <code>null</code>
   **                            or corrupted.
   */
  public static byte[] decode(final String b64String) {
    // check special case
    if (b64String == null || b64String.isEmpty()) {
      return new byte[0];
    }

    final byte[] srcBytes = b64String.getBytes(UTF8);
    final int sLen = srcBytes.length;
    // calculate output length assuming zero bytes are padding or separators
    final int maxOutputLen = checkedCast((long)sLen * 6 >> 3);
    // allocate output array (may be too large)
    final byte[] dstBytes = new byte[maxOutputLen];
    // process all input bytes
    int d = 0;
    for (int s = 0; s < srcBytes.length; ) {
      // assemble three bytes into an int from four base 64 characters
      int i = 0;
      int j = 0;
      while (j < 4 && s < sLen) {
        // j only increased if a valid char was found
        final int c = decodeDigit(srcBytes[s++]);
        if (c >= 0) {
          i |= c << (18 - j * 6);
          j++;
        }
      }
      // j is now the number of valid digits decoded
      // add output bytes
      if (j >= 2) {
        dstBytes[d++] = (byte) (i >> 16);
        if (j >= 3) {
          dstBytes[d++] = (byte) (i >> 8);
          if (j >= 4) {
            dstBytes[d++] = (byte) i;
          }
        }
      }
    }
    // d is now the number of output bytes written copy dstBytes to new array of
    // proper size
    return Arrays.copyOf(dstBytes, d);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: computeEncodedLength
  /**
   ** Computes the base 64 encoded character length for the specified input byte
   ** length.
   **
   ** @param  length             the input byte length.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param   safe              <code>true</code> to ensure URL-safe encoding.
   **                            <br>
   **                            Allowed object is <code>booleana</code>.
   **
   ** @return                    the base 64 encoded character length.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  static int computeEncodedLength(final int length, final boolean safe) {
    // prevent bogus input
    if (length == 0)
      return 0;

    if (safe) {
      // compute the number of complete quads (4-char blocks)
      int fullQuadLength = (length / 3) << 2;
      // compute the remaining bytes at the end
      int remainder = length % 3;
      // compute the total
      return remainder == 0 ? fullQuadLength : fullQuadLength + remainder + 1;
    }
    else {
      // original mig code
      return ((length - 1) / 3 + 1) << 2;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: tpSelect
  /**
   ** Select one of two values based on <code>bool</code> without leaking
   ** information about which one was selected.
   **
   ** @param  bool               must be 1 or 0.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  whenTrue           the value to return if <code>bool</code> is 1.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  whenFalse          the value to return if <code>bool</code> is 0.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    either <code>whenTrue</code> or
   **                            <code>whenFalse</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  static int tpSelect(final int bool, final int whenTrue, final int whenFalse) {
    // will be 0x00000000 when bool == 1, or 0xFFFFFFFF when bool == 0
    final int mask = bool - 1;
    return whenTrue ^ (mask & (whenTrue ^ whenFalse));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: tpLT
  /**
   ** Checks if <code>lhs</code> &lt; <code>rhs</code> without leaking
   ** information about either <code>lhs</code> or <code>rhs</code>.
   **
   ** @param  lhs                any int.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  rhs                any int.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    1 if yes, 0 if no.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  static int tpLT(final int lhs, final int rhs) {
    return (int)(((long)lhs - (long)rhs) >>> 63);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: tpGT
  /**
   ** Checks if <code>lhs</code> &gt; <code>rhs</code> without leaking
   ** information about either <code>lhs</code> or <code>rhs</code>.
   **
   ** @param  lhs                any int.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  rhs                any int.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    1 if yes, 0 if no.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  static int tpGT(final int lhs, final int rhs) {
    return (int) (((long)rhs - (long)lhs) >>> 63);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: tpEQ
  /**
   ** Checks if <code>lhs</code> == <code>rhs</code> without leaking information
   ** about either <code>lhs</code> or <code>rhs</code>.
   **
   ** @param  lhs                any int.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  rhs                any int.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    1 if yes, 0 if no.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  static int tpEQ(final int lhs, final int rhs) {
    // this is magic but it will make sense
    // if you think about it for 30 minutes
    final int bit_diff          = lhs ^ rhs;
    final int msb_iff_zero_diff = (bit_diff - 1) & (~bit_diff);
    return msb_iff_zero_diff >>> 63;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: encodeDigitBase64
  /**
   ** Convert a digit index to the appropriate base 64 ASCII byte.
   ** <p>
   ** Uses '+' and '/' for 62 and 63, as required for standard base 64.
   **
   ** @param  digit              must be at least 0 and at most 63.
   **                            Output is undefined if digit_idx is not on this
   **                            range.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    an ASCII character.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  static byte encodeDigitBase64(final int digit) {
    assert digit >= 0 && digit <= 63;
    // figure out which type of digit this should be
    final int is_uppercase = tpLT(digit, 26);
    final int is_lowercase = tpGT(digit, 25) & tpLT(digit, 52);
    final int is_decimal   = tpGT(digit, 51) & tpLT(digit, 62);
    final int is_62        = tpEQ(digit, 62);
    final int is_63        = tpEQ(digit, 63);
    // translate from digit index to ASCII for each hypothetical scenario
    final int as_uppercase = digit -  0 + 65;
    final int as_lowercase = digit - 26 + 97;
    final int as_decimal   = digit - 52 + 48;
    final int as_62        = '+';
    final int as_63        = '/';
    // zero out all scenarios except for the right one, and combine
    final int ascii =
      tpSelect(is_uppercase, as_uppercase, 0) |
      tpSelect(is_lowercase, as_lowercase, 0) |
      tpSelect(is_decimal  , as_decimal  , 0) |
      tpSelect(is_62       , as_62       , 0) |
      tpSelect(is_63       , as_63       , 0);
    return (byte)ascii;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: encodeDigitBase64URL
  /**
   ** Convert a digit index to the appropriate base64url ASCII byte.
   ** <p>
   ** Uses '-' and '_' for 62 and 63, as required for the base64url encoding.
   **
   ** @param  digit              must be at least 0 and at most 63.
   **                            Output is undefined if digit_idx is not on this
   **                            range.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    an ASCII character.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  static byte encodeDigitBase64URL(final int digit) {
    assert digit >= 0 && digit <= 63;
    // figure out which type of digit this should be
    final int is_uppercase = tpLT(digit, 26);
    final int is_lowercase = tpGT(digit, 25) & tpLT(digit, 52);
    final int is_decimal   = tpGT(digit, 51) & tpLT(digit, 62);
    final int is_62        = tpEQ(digit, 62);
    final int is_63        = tpEQ(digit, 63);
    // translate from digit index to ASCII for each hypothetical scenario
    final int as_uppercase = digit -  0 + 65;
    final int as_lowercase = digit - 26 + 97;
    final int as_decimal   = digit - 52 + 48;
    final int as_62        = '-';
    final int as_63        = '_';
    // Zero out all scenarios except for the right one, and combine
    final int ascii =
      tpSelect(is_uppercase, as_uppercase, 0) |
      tpSelect(is_lowercase, as_lowercase, 0) |
      tpSelect(is_decimal  , as_decimal  , 0) |
      tpSelect(is_62       , as_62       , 0) |
      tpSelect(is_63       , as_63       , 0);
    return (byte)ascii;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: encodeDigit
  /**
   ** Decode an ASCII byte to a base 64 digit index (0 to 63), or -1 if the
   ** input is not a valid base 64 digit.
   ** <p>
   ** Supports '+' and '/' for standard base 64, but also '-' and '_' for
   ** base64url.
   **
   ** @param  ascii              an ASCII character.
   **
   ** @return                    a digit index i such that 0 <= i <= 63, or -1
   **                            if the input was not a digit.
   */
  static int decodeDigit(final byte ascii) {
    // figure out which type of digit this is
    final int is_uppercase = tpGT(ascii, 64) & tpLT(ascii, 91);
    final int is_lowercase = tpGT(ascii, 96) & tpLT(ascii, 123);
    final int is_decimal   = tpGT(ascii, 47) & tpLT(ascii, 58);
    final int is_62        = tpEQ(ascii, '-') | tpEQ(ascii, '+');
    final int is_63        = tpEQ(ascii, '_') | tpEQ(ascii, '/');
    // it should be one of the five categories
    final int is_valid = is_uppercase | is_lowercase | is_decimal | is_62 | is_63;
    // translate from ASCII to digit index for each hypothetical scenario
    final int from_uppercase = ascii - 65 +  0;
    final int from_lowercase = ascii - 97 + 26;
    final int from_decimal   = ascii - 48 + 52;
    final int from_62        = 62;
    final int from_63        = 63;
    // zero out all scenarios except for the right one, and combine
    final int digit =
      tpSelect(is_uppercase, from_uppercase, 0) |
      tpSelect(is_lowercase, from_lowercase, 0) |
      tpSelect(is_decimal  , from_decimal  , 0) |
      tpSelect(is_62       , from_62       , 0) |
      tpSelect(is_63       , from_63       , 0) |
      tpSelect(is_valid    , 0             , -1);

    assert digit >= -1 && digit <= 63;
    return digit;
  }

  private static int checkedCast(final long value) {
    int result = (int)value;
    if (result != value)
      throw new IllegalArgumentException(value + " cannot be cast to int without changing its value.");

    return result;
  }
}