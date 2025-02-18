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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryFile.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFile.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import java.util.List;
import java.util.ArrayList;

import java.io.File;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.Base64Transcoder;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryFile
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** LDAP Data Interchange Format (LDIF) is a file format used to import and
 ** export directory data from an LDAP server and to describe a set of changes
 ** to be applied to data in a directory. This format is described in the
 ** Internet draft
 ** <a href="ftp://ftp.ietf.org/internet-drafts/draft-good-ldap-ldif-00.txt" target="_blank">The LDAP Data Interchange Format (LDIF) - Technical Specification</a>.
 ** <p>
 ** This class implements an LDIF input output operations.
 */
public abstract class DirectoryFile implements DirectoryConstant {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static char     COMMENT        = '#';
  public static final char     SEPARATOR      = ':';

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final char         separator;
  protected final String       handle;

  protected final List<String> binaryAttribute = new ArrayList<String>();
  protected final List<String> omitAttribute   = new ArrayList<String>();
  protected final List<String> fetchAttribute  = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFile</code> object to parse/write the LDIF
   ** or DSML from/to a specified file.
   **
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  protected DirectoryFile(final char separator) {
    // ensure inheritance
    super();

    // initialize instance
    this.handle    = null;
    this.separator = separator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFile</code> object to parse/write the LDIF
   ** or DSML from/to a specified file.
   **
   ** @param  file               the abstract path of the LDIF/DSML file to
   **                            parse or write.
   */
  public DirectoryFile(final File file) {
    // ensure inheritance
    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFile</code> object to parse/write the LDIF
   ** or DSML from/to a specified file.
   **
   ** @param  file               the abstract path of the LDIF/DSML file to
   **                            parse or write.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  public DirectoryFile(final File file, final char separator) {
    // ensure inheritance
    this(file.getAbsolutePath(), separator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFile</code> object to parse/write the LDIF
   ** or DSML from/to a specified file.
   **
   ** @param  handle             the name of the LDIF/DSML file to parse or
   **                            write.
   */
  public DirectoryFile(final String handle) {
    // ensure inheritance
    this(handle, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFile</code> object to parse/write the LDIF
   ** or DSML from/to a specified file.
   **
   ** @param  handle             the name of the LDIF/DSML file to parse or
   **                            write.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  public DirectoryFile(final String handle, final char separator) {
    // ensure inheritance
    super();

    this.handle    = handle;
    this.separator = separator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryAttribute
  /**
   ** Returns <code>true</code> if the specified name is enlisted as a binary
   ** attribute.
   **
   ** @param  name               the name of the attribute to verify.
   **
   ** @return                    <code>true</code> if the specified name is
   **                            enlisted as a binary attribute; otherwise
   **                            <code>false</code>.
   */
  public boolean binaryAttribute(final String name) {
    return (this.binaryAttribute.isEmpty()) ? false : this.binaryAttribute.contains(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   omittedAttribute
  /**
   ** Returns <code>true</code> if the specified name is enlisted as an
   ** attribute that has to be omitted.
   **
   ** @param  name               the attribute name to verify if its part of
   **                            the attributes to be omitted.
   **
   ** @return                    <code>true</code> if the specified name is
   **                            enlisted as an attribute that has to be
   **                            omitted; otherwise <code>false</code>.
   */
  public boolean omittedAttribute(final String name) {
    return (this.omitAttribute.isEmpty()) ? false : this.omitAttribute.contains(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAttribute
  /**
   ** Returns <code>true</code> if the specified name is enlisted as an
   ** attribute that has to be fetched.
   **
   ** @param  name               the attribute name to verify if its part of
   **                            the attributes to be fetched.
   **
   ** @return                    <code>true</code> if the specified name is
   **                            enlisted as an attribute that has to be
   **                            fetched; otherwise <code>false</code>.
   */
  public boolean fetchAttribute(final String name) {
    return (this.fetchAttribute.isEmpty()) ? false : this.fetchAttribute.contains(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerBinaryAttribute
  /**
   ** Register an attribute name where the content will be provided as base64
   ** encoded
   **
   ** @param  name               the attribute name where the content will be
   **                            provided as base64 encoded to register.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerBinaryAttribute(final String name) {
    return this.binaryAttribute.add(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterBinaryAttribute
  /**
   ** Unregister an attribute name where the content will be provided as base64
   ** encoded
   **
   ** @param  name               the attribute name where the content will be
   **                            provided as base64 encoded to unregister.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            unregistered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean unregisterBinaryAttribute(final String name) {
    return this.binaryAttribute.remove(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseBinaryAttribute
  /**
   ** Parse the attribute names that has to be handled as binary from the
   ** specified comma separated argument.
   **
   ** @param  argument           the comma separated list of attributes that has
   **                            to be handled as binary.
   */
  public void parseBinaryAttribute(final String argument) {
    parseBinaryAttribute(argument, SystemConstant.COMMA);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseBinaryAttribute
  /**
   ** Parse the attribute names that has to be handled as binary from the
   ** string delimited by the specified character.
   **
   ** @param  argument           a string delimited by the specified character
   **                            that contains the attributes to be handled as
   **                            binary.
   ** @param  delimiter          the delimiting character.
   */
  public void parseBinaryAttribute(final String argument, final char delimiter) {
    final String[] raw = argument.split(Character.toString(delimiter));
    for (int i = 0; i < raw.length; i++)
      this.binaryAttribute.add(raw[i].toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerOmittedAttribute
  /**
   ** Register an attribute name where the content will be skipped during parse
   **
   ** @param  name               the attribute name that has to be skipped.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerOmittedAttribute(final String name) {
    return this.omitAttribute.add(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterOmittedAttribute
  /**
   ** Unregister an attribute name where the content will no longer be skipped
   ** during parse.
   **
   ** @param  name               the attribute name that has no longer to be
   **                            skipped.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            unregistered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean unregisterOmittedAttribute(final String name) {
    return this.omitAttribute.remove(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseOmitAttribute
  /**
   ** Parse the attribute names from the specified comma separated argument that
   ** has to be omitted by this <code>DirectoryFileReader</code>.
   **
   ** @param  argument           the specified comma separated list of
   **                            attributes that has to be omitted by this
   **                            <code>DirectoryFileReader</code>.
   */
  public void parseOmitAttribute(final String argument) {
    parseOmitAttribute(argument, SystemConstant.COMMA);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseOmitAttribute
  /**
   ** Parse the attribute names that has to be skipped during parse from the
   ** string delimited by the specified character.
   **
   ** @param  argument           a string delimited by the specified character
   **                            that contains the attributes that has to be
   **                            skipped during parse.
   ** @param  delimiter          the delimiting character
   */
  public void parseOmitAttribute(final String argument, final char delimiter) {
    final String[] raw = argument.split(Character.toString(delimiter));
    for (int i = 0; i < raw.length; i++)
      this.omitAttribute.add(raw[i].toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerFetchedAttribute
  /**
   ** Register an attribute name where the content will be fetched during parse
   **
   ** @param  name               the attribute name that has to be fetched.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerFetchedAttribute(final String name) {
    return this.fetchAttribute.add(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterFetchedAttribute
  /**
   ** Unregister an attribute name where the content will no longer be fetched
   ** during parse.
   **
   ** @param  name               the attribute name that has no longer to be
   **                            fetched.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            unregistered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean unregisterFetchedAttribute(final String name) {
    return this.fetchAttribute.remove(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseFetchedAttribute
  /**
   ** Parse the attribute names from the specified comma separated argument that
   ** has to be fetched by this <code>DirectoryFileReader</code>.
   **
   ** @param  argument           the specified comma separated list of
   **                            attributes that has to be fetched by this
   **                            <code>DirectoryFileReader</code>.
   */
  public void parseFetchedAttribute(final String argument) {
    parseFetchedAttribute(argument, SystemConstant.COMMA);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseFetchedAttribute
  /**
   ** Parse the attribute names that has to be fetched during parse from the
   ** string delimited by the specified character.
   **
   ** @param  argument           a string delimited by the specified character
   **                            that contains the attributes that has to be
   **                            fetched during parse.
   ** @param  delimiter          the delimiting character
   */
  public void parseFetchedAttribute(final String argument, final char delimiter) {
    final String[] raw = argument.split(Character.toString(delimiter));
    for (int i = 0; i < raw.length; i++)
      this.fetchAttribute.add(raw[i].toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printable (deprecated)
  /**
   ** Returns <code>true</code> if all the bytes in the given array are valid
   ** for output as a String according to the LDIF specification. If not, the
   ** array should output base64-encoded.
   **
   ** @param  b                  the byte array to verify if are all elements
   **                            are printable characters,
   **
   ** @return                    <code>true</code> if all the bytes in the given
   **                            array are valid for output as a String
   **                            according to the LDIF specification; otherwise,
   **                            <code>false</code>.
   */
  @Deprecated
  public static boolean printable(final byte[] b) {
    for (int i = b.length - 1; i >= 0; i--) {
      if ((b[i] < ' ') || (b[i] > 127)) {
        if (b[i] != '\t')
          return false;
      }
    }
    return true;
  }

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
   **
   ** @return                    <code>true</code> if encoding not required for
   **                            LDIF; otherwise <code>false</code>.
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
   ** @throws DirectoryException if an I/O error occurs
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
   **
   ** @return                    the base64 encoded byte array.
   */
  protected static String encodeBytes(final String line) {
    return new String(encodeBytes(line.getBytes()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeBytes
  /**
   ** Given a sequence of input bytes, produces a string using the base64
   ** encoding.
   **
   ** @param  bytes              the sequence of input bytes to encode base64.
   **
   ** @return                    the base64 encoded byte array.
   */
  protected static byte[] encodeBytes(final byte[] bytes) {
    return Base64Transcoder.encode(bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeBytes
  /**
   ** Given a sequence of input bytes, produces a string using the base64
   ** decoding.
   **
   ** @param  bytes              the sequence of input bytes to encode base64.
   **
   ** @return                    the base64 encoded byte array.
   */
  protected static byte[] decodeBytes(final byte[] bytes) {
    return decodeBytes(new String(bytes));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeBytes
  /**
   ** Given a string, produces a sequence of output bytes using the base64
   ** decoding. If there are bytes in 'out' already, the new bytes are appended,
   ** so the caller should do `out.setLength(0)' first if that's desired.
   **
   ** @param  line               the string to decode base64.
   **
   ** @return                    the base64 encoded string.
   */
  protected static byte[] decodeBytes(final String line) {
    return Base64Transcoder.decode(line);
  }
}