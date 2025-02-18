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

    File        :   LDAPWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LDAPWriter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import javax.naming.directory.Attribute;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// abstract class LDAPWriter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** LDAP Data Interchange Format (LDIF) is a file format used to import and
 ** export directory data from an LDAP server and to describe a set of changes
 ** to be applied to data in a directory. This format is described in the
 ** Internet draft
 ** <a href="ftp://ftp.ietf.org/internet-drafts/draft-good-ldap-ldif-00.txt" target="_blank">The LDAP Data Interchange Format (LDIF) - Technical Specification</a>.
 ** <p>
 ** This class implements an LDIF or DSML output operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public abstract class LDAPWriter extends LDAPFile {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String    STANDARD        = "System.out";

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DirectorySchema  schema;
  protected final PrintWriter      writer;
  protected final List<String>     binaryAttribute = new ArrayList<String>();

  protected boolean                attributesOnly  = false;
  protected boolean                omitReadonly    = true;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to write the LDIF, DSML or
   ** cJSON data to the specified file.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  file               the abstract path of the LDIF file to parse or
   **                            write.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  protected LDAPWriter(final DirectorySchema schema, final File file)
    throws DirectoryException {

    this(schema, file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWritec</code> object to write the LDIF, DSML or
   ** JSON data to the specified file.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  file               the abstract path of the LDIF file to parse or
   **                            write.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  protected LDAPWriter(final DirectorySchema schema, final File file, final char separator)
    throws DirectoryException {

    // ensure inheritance
    super(file, separator);

    // initialize instance attributes
    this.schema         = schema;

    // initialize the output stream
    try {
      this.writer = new PrintWriter(file);
    }
    catch (FileNotFoundException e) {
      throw new DirectoryException(Bundle.format(Bundle.FILE_MISSING, file.getAbsolutePath()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   omitReadonly
  /**
   ** Sets the option that only attribute names should be written to the file.
   **
   ** @param  value              <code>true</code> if attribute that are
   **                            declared readonly by the schema should be
   **                            omitted; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public void omitReadonly(final boolean value) {
    this.omitReadonly = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   omitReadonly
  /**
   ** Returns the option that only attribute names should be written to the
   ** file.
   **
   ** @return                    <code>true</code> if attribute that are
   **                            declared readonly by the schema should be
   **                            omitted; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean omitReadonly() {
    return this.omitReadonly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesOnly
  /**
   ** Sets the option that only attribute names should be written to the file.
   **
   ** @param  value              <code>true</code> if only the attribute names
   **                            should be written to the file.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public void attributesOnly(final boolean value) {
    this.attributesOnly = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesOnly
  /**
   ** Returns the option that only attribute names should be written to the
   ** file.
   **
   ** @return                    <code>true</code> if only the attribute names
   **                            should be written to the file: otherwise
   **                            <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean attributesOnly() {
    return this.attributesOnly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (LDAPFile)
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
  // Method:   build
  /**
   ** Factory method to create an instance of <code>LDAPWriter</code> which
   ** spools out the reaoutl set of an LDAP search.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  format             the format descriptor one of
   **                            <ul>
   **                              <li>{@link Format#DSML2}
   **                              <li>{@link Format#DSML1}
   **                              <li>{@link Format#LDIF}
   **                              <li>{@link Format#JSON}
   **                            </ul>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  export             the absolut {@link File} path of the
   **                            destination.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    an instance of <code>LDAPWriter</code>.
   **                            <br>
   **                            Possible object is <code>LDAPWriter</code>.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public static LDAPWriter build(final  DirectorySchema schema, final String format, final File export)
    throws DirectoryException {

    switch (Format.from(format)) {
      case DSML1 : return DSMLWriter.build(schema, export, 1);
      case DSML2 : return DSMLWriter.build(schema, export, 2);
      case JSON  : return JSONWriter.build(schema, export);
      case LDIF  :
      default    : return LDIFWriter.build(schema, export);// TODO: throw a DirectoryException
    }
  }

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
   ** @param  distinguishedName  the DN of the entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public abstract void printEntryStart(final String distinguishedName);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printAttribute
  /**
   ** Print an attribute of an entry
   **
   ** @param  attribute          the attribute to format to the output stream.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  public abstract void printAttribute(final Attribute attribute);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printComment
  /**
   ** Print a comment.
   **
   ** @param  comment            the comment to print to the output stream.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public abstract void printComment(final String comment);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntryEnd
  /**
   ** Print epilogue to entry.
   **
   ** @param  distinguishedName  the DN of the entry
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public abstract void printEntryEnd(final String distinguishedName);
}