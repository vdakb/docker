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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   LDAPFile.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LDAPFile.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.io.File;

import oracle.javatools.codeex.Base64;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// abstract class LDAPFile
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** LDAP Data Interchange Format (LDIF) is a file format used to import and
 ** export directory data from an LDAP server and to describe a set of changes
 ** to be applied to data in a directory. This format is described in the
 ** Internet draft
 ** <a href="ftp://ftp.ietf.org/internet-drafts/draft-good-ldap-ldif-00.txt" target="_blank">The LDAP Data Interchange Format (LDIF) - Technical Specification</a>.
 ** <p>
 ** This class implements an LDIF input output operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public abstract class LDAPFile {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static char           NULL        = '\u0000';
  public final static char           COMMENT     = '#';
  public static final char           SEPARATOR   = ':';

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final char               separator;
  protected final String             handle;

  //////////////////////////////////////////////////////////////////////////////
  // enum Format
  // ~~~~ ~~~~~~
  /**
   ** Java class for output file format.
   */
  public enum Format {
      LDIF("ldif",   ".ldif")
    , DSML1("dsml1", ".xml")
    , DSML2("dsml2", ".xml")
    , JSON("json",   ".json")
    ;
    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-5291032650190054074")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String       type;
    public final String       file;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Format</code> that allows use as a JavaBean.
     **
     ** @param  type             the format type.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  file             the file extension assoviated with the format
     **                          type.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Format(final String type, final String file) {
      // initialize instance attributes
      this.type = type;
      this.file = file;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   from
    /**
     ** Factory method to create a proper format from the given string value.
     **
     ** @param  type               the string value the format should be
     **                            returned for.
     **
     ** @return                    the format.
     */
    public static Format from(final String type) {
      for (Format cursor : Format.values()) {
        if (cursor.type.equals(type))
          return cursor;
      }
      throw new IllegalArgumentException(type);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LDAPFile</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected LDAPFile() {
    // ensure inheritance
    this(SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF, DSML
   ** or JSON from/to a specified file.
   **
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **                            <br>
   **                            Allowed object is <code>char</code>.
   */
  protected LDAPFile(final char separator) {
    // ensure inheritance
    super();

    // initialize instance
    this.handle    = null;
    this.separator = separator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF, DSML
   ** or JSON from/to a specified file.
   **
   ** @param  file               the abstract path of the LDIF/DSML file to
   **                            parse or write.
   **                            <br>
   **                            Allowed object is {@link File}.
   */
  protected LDAPFile(final File file) {
    // ensure inheritance
    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF, DSML
   ** or JSON from/to a specified file.
   **
   ** @param  file               the abstract path of the LDIF/DSML file to
   **                            parse or write.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **                            <br>
   **                            Allowed object is <code>char</code>.
   */
  protected LDAPFile(final File file, final char separator) {
    // ensure inheritance
    this(file.getAbsolutePath(), separator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF, DSML
   ** or JSON from/to a specified file path.
   **
   ** @param  handle             the name of the LDIF/DSML file to parse or
   **                            write.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected LDAPFile(final String handle) {
    // ensure inheritance
    this(handle, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF, DSML
   ** or JSON from/to a specified file path.
   **
   ** @param  handle             the name of the LDIF/DSML file to parse or
   **                            write.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **                            <br>
   **                            Allowed object is <code>char</code>.
   */
  protected LDAPFile(final String handle, final char separator) {
    // ensure inheritance
    super();

    this.handle    = handle;
    this.separator = separator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   needsEncoding
  /**
   ** Checks if the input byte array contains only safe values, that is, the
   ** data does not need to be encoded for use with LDIF.
   ** <p>
   ** The rules for checking safety are based on the rules for LDIF
   ** (LDAP Data Interchange Format) per RFC 2849. The data does not need to be
   ** encoded if all the following are true:
   ** <ol>
   **   <li>The data cannot start with the following byte values:
   **       <ul>
   **         <li><code>00 (NUL)</code>
   **         <li><code>10 (LF)</code>
   **         <li><code>13 (CR)</code>
   **         <li><code>32 (SPACE)</code>
   **         <li><code>58 (:)</code>
   **         <li><code>60 (&lt;)</code>
   **         <li>Any character with value greater than 127 (Negative for a byte
   **             value)
   **       </ul>
   **   <li>The data cannot contain any of the following byte values:
   **       <ul>
   **         <li><code>00 (NUL)</code>
   **         <li><code>10 (LF)</code>
   **         <li><code>13 (CR)</code>
   **         <li>Any character with value greater than 127 (Negative for a byte
   **             value)
   **       </ul>
   **   <li>The data cannot end with a space.
   ** </ol>
   **
   ** @param  bytes              the bytes to be checked.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if encoding not required for
   **                            LDIF; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean needsEncoding(final byte[] bytes) {
    int len = bytes.length;
    if (len > 0) {
      int testChar = bytes[0];
      // unsafe if first character is a NON-SAFE-INIT-CHAR
      if (testChar == 0x00 // NUL
      ||  testChar == 0x0a // linefeeder
      ||  testChar == 0x0d // carrage return
      ||  testChar == 0x20 // space(' ')
      ||  testChar == 0x3a // colon(':')
      ||  testChar == 0x3c // less-than('<')
      ||  testChar <  0) { // non ascii (>127 is negative)
        return  true;
      }

      // unsafe if last character is a space
      if (bytes[len - 1] == ' ')
        return true;

      // unsafe if contains any non safe character
      if (len > 1) {
        for ( int i = 1; i < bytes.length; i++ ) {
          testChar = bytes[i];
          if (testChar == 0x00 // NUL
          ||  testChar == 0x0a // linefeeder
          ||  testChar == 0x0d // carrage return
          ||  testChar <  0) { // non ascii (>127 is negative)
            return true;
          }
        }
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Closes the embedded stream, flushing it first.
   ** <p>
   ** Once the stream has been closed, further write() or read() invocations
   ** will cause an IOException to be thrown.
   ** <p>
   ** Closing a previously closed stream has no effect.
   **
   ** @throws DirectoryException if an I/O error occurs.
   */
  public abstract void close()
    throws DirectoryException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeBytes
  /**
   ** Given a string, produces a sequence of output bytes using the base64
   ** encoding. If there are bytes in 'out' already, the new bytes are appended,
   ** so the caller should do `out.setLength(0)' first if that's desired.
   **
   ** @param  line               the string to encode base64.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the base64 encoded byte array.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected static String encodeBytes(final String line) {
    return encodeBytes(line.getBytes());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeBytes
  /**
   ** Given a sequence of input bytes, produces a string using the base64
   ** encoding.
   **
   ** @param  bytes              the sequence of input bytes to encode base64.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the base64 encoded byte array.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  protected static String encodeBytes(final byte[] bytes) {
    return Base64.base64Encode(bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeBytes
  /**
   ** Given a string, produces a sequence of output bytes using the base64
   ** decoding. If there are bytes in 'out' already, the new bytes are appended,
   ** so the caller should do 'out.setLength(0)' first if that's desired.
   **
   ** @param  line               the string to decode base64.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the base64 encoded string.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  protected static byte[] decodeBytes(final String line) {
    return Base64.base64Decode(line);
  }
}