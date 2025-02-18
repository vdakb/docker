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

    File        :   DirectoryFileWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFileWriter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.naming.directory.Attribute;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryFileWriter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** LDAP Data Interchange Format (LDIF) is a file format used to import and
 ** export directory data from an LDAP server and to describe a set of changes
 ** to be applied to data in a directory. This format is described in the
 ** Internet draft
 ** <a href="ftp://ftp.ietf.org/internet-drafts/draft-good-ldap-ldif-00.txt" target="_blank">The LDAP Data Interchange Format (LDIF) - Technical Specification</a>.
 ** <p>
 ** This class implements an LDIF or DSML output operations.
 */
public abstract class DirectoryFileWriter extends DirectoryFile {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String    STANDARD        = "System.out";

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected boolean                attributesOnly;
  protected final PrintWriter      writer;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileWriter</code> object to write the LDIF or
   ** DSML data to stdout.
   */
  public DirectoryFileWriter() {
    // ensure inheritance
    super(STANDARD);

    // initialize the output stream
    this.attributesOnly = false;
    this.writer         = new PrintWriter(System.out);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileWriter</code> object to write the LDIF or
   ** DSML to a specified file.
   **
   ** @param  file               the abstract path of the LDIF file to parse or
   **                            write.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public DirectoryFileWriter(final File file)
    throws DirectoryException {

    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileWriter</code> object to write the LDIF or
   ** DSML to a specified file.
   **
   ** @param  file               the abstract path of the LDIF file to parse or
   **                            write.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public DirectoryFileWriter(final File file, final char separator)
    throws DirectoryException {

    // ensure inheritance
    super(file, separator);

    this.attributesOnly = false;
    // initialize the output stream
    try {
      this.writer = new PrintWriter(file);
    }
    catch (FileNotFoundException e) {
      throw new DirectoryException(DirectoryError.FILE_MISSING, file.getAbsolutePath());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileWriter</code> object to write the LDIF or
   ** DSML to a specified file.
   **
   ** @param  file             the name of the LDIF file to parse or write.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public DirectoryFileWriter(final String file)
    throws DirectoryException {

    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileWriter</code> object to write the LDIF or
   ** DSML to a specified file.
   **
   ** @param  file               the name of the LDIF file to parse or write.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public DirectoryFileWriter(final String file, final char separator)
    throws DirectoryException {

    // ensure inheritance
    super(file, separator);

    this.attributesOnly = false;
    // initialize the output stream
    try {
      this.writer = new PrintWriter(file, StringUtility.ASCII.name());
    }
    catch (FileNotFoundException e) {
      throw new DirectoryException(DirectoryError.FILE_MISSING, file);
    }
    catch (UnsupportedEncodingException e) {
      throw new DirectoryException(DirectoryError.ENCODING_TYPE_NOT_SUPPORTED, StringUtility.ASCII.name());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileWriter</code> object to output entries to
   ** a stream as LDIF or DSML.
   **
   ** @param  stream             output stream
   */
  public DirectoryFileWriter(final OutputStream stream) {
    this(new PrintWriter(stream), SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileWriter</code> object to output entries to
   ** a stream as LDIF or DSML.
   **
   ** @param  stream             output stream
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  public DirectoryFileWriter(final OutputStream stream, final char separator) {
    // ensure inheritance
    this(new PrintWriter(stream), separator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileWriter</code> object to output entries to
   ** a stream as LDIF or DSML.
   **
   ** @param  writer             output stream
   */
  public DirectoryFileWriter(final PrintWriter writer) {
    this(writer, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileWriter</code> object to output entries to
   ** a stream as LDIF or DSML.
   **
   ** @param  writer             output stream
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  public DirectoryFileWriter(final PrintWriter writer, final char separator) {
    // ensure inheritance
    super(separator);

    // initialize the output stream
    this.attributesOnly = false;
    this.writer         = writer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesOnly
  /**
   ** Sets the option that only attribute names should be written to the file.
   **
   ** @param  attributesOnly     <code>true</code> if only the attribute names
                                 should be written to the file.
   */
  public void attributesOnly(final boolean attributesOnly) {
    this.attributesOnly = attributesOnly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesOnly
  /**
   ** Returns the option that only attribute names should be written to the
   ** file.
   **
   ** @return                    <code>true</code> if only the attribute names
                                 should be written to the file: otherwise
   **                            <code>false</code>
   */
  public boolean attributesOnly() {
    return this.attributesOnly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (DirectoryFile)
  /**
   ** Closes the embedded stream, flushing it first.
   ** <p>
   ** Once the stream has been closed, further write() or flush() invocations
   ** will cause an IOException to be thrown.
   ** <p>
   ** Closing a previously closed stream has no effect.
   */
  public void close() {
    this.writer.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flush
  /**
   ** Flushes the embedded stream.
   ** <p>
   ** If the embedded stream has saved any characters from the various write()
   ** methods in a buffer, write them immediately to their intended destination.
   ** Then, if that destination is another character or byte stream, flush it.
   ** Thus one flush() invocation will flush all the buffers in a chain of
   ** Writers and OutputStreams.
   ** <p>
   ** If the intended destination of the embedded stream is an abstraction
   ** provided by the underlying operating system, for example a file, then
   ** flushing the stream guarantees only that bytes previously written to the
   ** embedded stream are passed to the operating system for writing; it does
   ** not guarantee that they are actually written to a physical device such as
   ** a disk drive.
   **
   ** @throws IOException        if an I/O error occurs
   */
  public void flush()
    throws IOException {

    this.writer.flush();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printPrologue
  /**
   ** Print prologue to file.
   */
  public abstract void printPrologue();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEpilogue
  /**
   ** Print epilogue to file.
   */
  public abstract void printEpilogue();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntryStart
  /**
   ** Print prologue to entry
   **
   ** @param  distinguishedName  the DN of the entry
   */
  public abstract void printEntryStart(final String distinguishedName);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printAttribute
  /**
   ** Print an attribute of an entry
   **
   ** @param  attribute          the attribute to format to the output stream
   */
  public abstract void printAttribute(final Attribute attribute);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printComment
  /**
   ** Print a comment.
   **
   ** @param  comment            the comment to print to the output stream
   */
  public abstract void printComment(final String comment);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntryEnd
  /**
   ** Print epilogue to entry.
   **
   ** @param  distinguishedName  the DN of the entry
   */
  public abstract void printEntryEnd(final String distinguishedName);
}