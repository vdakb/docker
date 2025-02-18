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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   LDAPFile.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPFile.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.util.HashSet;
import java.util.Collection;

import java.io.File;

import oracle.hst.foundation.utility.Base64Transcoder;

import oracle.iam.directory.common.FeatureException;

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
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class LDAPFile {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static char           NULL      = '\u0000';
  public final static char           COMMENT   = '#';
  public static final char           SEPARATOR = ':';

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final char               separator;
  protected final String             handle;

  protected final Collection<String> binaries  = new HashSet<String>();
  protected final Collection<String> excludes  = new HashSet<String>();
  protected final Collection<String> includes  = new HashSet<String>();

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
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF or
   ** DSML from/to a specified file.
   **
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
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
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF or
   ** DSML from/to a specified file.
   **
   ** @param  file               the abstract path of the LDIF/DSML file to
   **                            parse or write.
   */
  protected LDAPFile(final File file) {
    // ensure inheritance
    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF
   ** or DSML from/to a specified file.
   **
   ** @param  file               the abstract path of the LDIF/DSML file to
   **                            parse or write.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  protected LDAPFile(final File file, final char separator) {
    // ensure inheritance
    this(file.getAbsolutePath(), separator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF
   ** or DSML from/to a specified file.
   **
   ** @param  handle             the name of the LDIF/DSML file to parse or
   **                            write.
   */
  protected LDAPFile(final String handle) {
    // ensure inheritance
    this(handle, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPFile</code> object to parse/write the LDIF
   ** or DSML from/to a specified file.
   **
   ** @param  handle             the name of the LDIF/DSML file to parse or
   **                            write.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  protected LDAPFile(final String handle, final char separator) {
    // ensure inheritance
    super();

    this.handle    = handle;
    this.separator = separator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaries
  /**
   ** Returns <code>true</code> if the specified name is enlisted as a binary
   ** attribute.
   **
   ** @param  name               the name of the attribute that might be
   **                            handled as binary.
   **
   ** @return                    <code>true</code> if the specified name is
   **                            enlisted as a binary attribute; otherwise
   **                            <code>false</code>.
   */
  public boolean binaries(final String name) {
    return (this.binaries.isEmpty()) ? false : this.binaries.contains(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludes
  /**
   ** Returns <code>true</code> if the specified name is enlisted as an
   ** attribute that has to be omitted.
   **
   ** @param  name               the name of the attribute that might be
   **                            omitted.
   **
   ** @return                    <code>true</code> if the specified name is
   **                            enlisted as an attribute that has to be
   **                            omitted; otherwise <code>false</code>.
   */
  public boolean excludes(final String name) {
    return (this.excludes.isEmpty()) ? false : this.excludes.contains(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   includes
  /**
   ** Returns <code>true</code> if the specified name is enlisted as an
   ** attribute that has to be fetched.
   **
   ** @param  name               the name of the attribute that might be
   **                            fetched.
   **
   ** @return                    <code>true</code> if the specified name is
   **                            enlisted as an attribute that has to be
   **                            fetched; otherwise <code>false</code>.
   */
  public boolean includes(final String name) {
    return (this.includes.isEmpty()) ? false : this.includes.contains(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerBinaries
  /**
   ** Register an attribute name where the content will be provided as base64
   ** encoded.
   **
   ** @param  name               the attribute name where the content will be
   **                            provided as base64 encoded to register.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerBinaries(final String name) {
    return this.binaries.add(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerBinary
  /**
   ** Register a {@link Collection} of attribute names where the content will be
   ** provided as base64 encoded.
   **
   ** @param  collection         the {@link Collection} of attribute names where
   **                            the content will be provided as base64 encoded
   **                            to register.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerBinaries(final Collection<String> collection) {
    return this.binaries.addAll(collection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterBinaries
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
  public boolean unregisterBinaries(final String name) {
    return this.binaries.remove(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerExcludes
  /**
   ** Register an attribute name where the content will be skipped during parse.
   **
   ** @param  name               the attribute name that has to be skipped.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerExcludes(final String name) {
    return this.excludes.add(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerExcludes
  /**
   ** Register a {@link Collection} of attribute names where the content will be
   ** skipped during parse.
   **
   ** @param  collection         the {@link Collection} of attribute names that
   **                            has to be skipped.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerExcludes(final Collection<String> collection) {
    return this.excludes.addAll(collection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterExcludes
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
  public boolean unregisterExcludes(final String name) {
    return this.excludes.remove(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerIncludes
  /**
   ** Register an attribute name where the content will be fetched during parse.
   **
   ** @param  name               the attribute name that has to be fetched.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerIncludes(final String name) {
    return this.includes.add(name.toLowerCase().trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerIncludes
  /**
   ** Register a {@link Collection} of attribute names where the content will be
   ** fetched during parse.
   **
   ** @param  collection         the {@link Collection} of attribute names that
   **                            has to be fetched during parse.
   **
   ** @return                    <code>true</code> if the attribute name was
   **                            registered succesfully; otherwise
   **                            <code>false</code>.
   */
  public boolean registerIncludes(final Collection<String> collection) {
    return this.includes.addAll(collection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregisterIncludes
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
  public boolean unregisterIncludes(final String name) {
    return this.includes.remove(name.toLowerCase().trim());
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
   ** @throws FeatureException   if an I/O error occurs
   */
  public abstract void close()
    throws FeatureException;

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
    return new String( encodeBytes(line.getBytes()));
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
  protected static  byte[] encodeBytes(final byte[] bytes) {
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
    return Base64Transcoder.decode(bytes);
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