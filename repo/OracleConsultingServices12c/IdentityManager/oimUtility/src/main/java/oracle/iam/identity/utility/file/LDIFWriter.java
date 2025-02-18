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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared LDIF Facilities

    File        :   LDIFWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDIFWriter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Enumeration;

import java.io.File;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import javax.naming.Binding;
import javax.naming.NamingException;

import javax.naming.directory.Attribute;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.ldap.DirectoryException;
import oracle.iam.identity.foundation.ldap.DirectoryFileWriter;

////////////////////////////////////////////////////////////////////////////////
// class LDIFWriter
// ~~~~~ ~~~~~~~~~~
/**
 ** LDAP Data Interchange Format (LDIF) is a file format used to import and
 ** export directory data from an LDAP server and to describe a set of changes
 ** to be applied to data in a directory. This format is described in the
 ** Internet draft
 ** <a href="ftp://ftp.ietf.org/internet-drafts/draft-good-ldap-ldif-00.txt" target="_blank">The LDAP Data Interchange Format (LDIF) - Technical Specification</a>.
 ** <p>
 ** This class implements an LDIF file writer.
 ** <p>
 ** You can construct an object of this class to write data in LDIF format and
 ** manipulate the data as individual {@link Binding} objects.
 */
public class LDIFWriter extends DirectoryFileWriter {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static int MAX_LINE = 77;

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean         folding;
  private boolean         toFiles;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFWriter</code> object to write the LDAP data to
   ** stdin.
   */
  public LDIFWriter() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.folding = true;
    this.toFiles = false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFWriter</code> object to write the LDIF to a
   ** specified file.
   **
   ** @param  file               the name of the LDIF file to parse
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public LDIFWriter(final String file)
    throws DirectoryException {

    // ensure inheritance
    this(new File(file));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFWriter</code> object to write the LDIF to a
   ** specified file.
   **
   ** @param  file               the name of the LDIF file to parse
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public LDIFWriter(final File file)
    throws DirectoryException {

    // ensure inheritance
    super(file);

    // initialize instance attributes
    this.folding = true;
    this.toFiles = false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFWriter</code> object to write the LDAP data to an
   ** output stream.
   **
   ** @param  stream             output stream receiving the LDAP data
   */
  public LDIFWriter(final DataOutputStream stream) {
    this(new PrintWriter(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFWriter</code> object to output entries to a
   ** stream as LDIF.
   **
   ** @param  stream             output stream
   */
  public LDIFWriter(final Writer stream) {
    this(stream, SEPARATOR, true, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFWriter</code> object to output entries to a stream
   ** as LDIF.
   **
   ** @param  stream             output stream
   ** @param  separator          character to use between attribute names and
   **                            values; the default is ':'
   ** @param  folding            <code>true</code> to fold lines at 77
   **                            characters, <code>false</code> to not fold
   **                            them; the default is <code>true</code>.
   ** @param  toFiles            <code>true</code> to write each attribute value
   **                            to a file in the temp folder,
   **                            <code>false</code> to write them to the output
   **                            stream in printable format; the default is
   **                            <code>false</code>.
   */
  public LDIFWriter(final Writer stream, final char separator, final boolean folding, final boolean toFiles) {
    // ensure inheritance
    this(new PrintWriter(stream), separator, folding, toFiles);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFWriter</code> object to output entries to a
   ** stream as LDIF.
   **
   ** @param  writer             output stream
   */
  public LDIFWriter(final PrintWriter writer) {
    this(writer, SEPARATOR, true, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFWriter</code> object to output entries to a stream
   ** as LDIF.
   **
   ** @param  writer             output stream
   ** @param  separator          character to use between attribute names and
   **                            values; the default is ':'
   ** @param  folding            <code>true</code> to fold lines at 77
   **                            characters, <code>false</code> to not fold
   **                            them; the default is <code>true</code>.
   ** @param  toFiles            <code>true</code> to write each attribute value
   **                            to a file in the temp folder,
   **                            <code>false</code> to write them to the output
   **                            stream in printable format; the default is
   **                            <code>false</code>.
   */
  public LDIFWriter(final PrintWriter writer, final char separator, final boolean folding, final boolean toFiles) {
    // ensure inheritance
    super(writer, separator);

    // initialize instance attributes
    this.folding = folding;
    this.toFiles = toFiles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   folding
  /**
   ** Sets the option to fold long lines
   **
   ** @param  folding            <code>true</code> if long lines should be break
   **                            at a specific size to a continous line
   */
  public void folding(final boolean folding) {
    this.folding = folding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   folding
  /**
   ** Returns the option to fold long lines
   **
   ** @return                    <code>true</code> if long lines should be break
   **                            at a specific size to a continous line;
   **                            otherwise <code>false</code>
   */
  public boolean folding() {
    return this.folding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toFiles
  /**
   ** Sets the option that attribute values should be written to a file
   **
   ** @param  toFiles            <code>true</code> if the option that attribute
   **                            values should be written to a file
   */
  public void toFiles(final boolean toFiles) {
    this.toFiles = toFiles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toFiles
  /**
   ** Returns the option that attribute values should be written to a file
   **
   ** @return                    <code>true</code> if the option that attribute
   **                            values should be written to a file: otherwise
   **                            <code>false</code>
   */
  public boolean toFiles() {
    return this.toFiles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printPrologue (DirectoryFileWriter)
  /**
   ** Print prologue to file.
   */
  @Override
  public final void printPrologue() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEpilogue (DirectoryFileWriter)
  /**
   ** Print epilogue to file.
   */
  @Override
  public final void printEpilogue() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntryStart (DirectoryFileWriter)
  /**
   ** Print prologue to entry
   **
   ** @param  distinguishedName  the DN of the entry
   */
  @Override
  public void printEntryStart(String distinguishedName) {
    if (distinguishedName == null)
      distinguishedName = SystemConstant.EMPTY;

    printAttribute(DN, distinguishedName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntryEnd (DirectoryFileWriter)
  /**
   ** Print epilogue to entry.
   **
   ** @param  distinguishedName  the DN of the entry
   */
  @Override
  public void printEntryEnd(final String distinguishedName) {
    this.writer.println();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printAttribute (DirectoryFileWriter)
  /**
   ** Print an attribute of an entry
   **
   ** @param  attribute          the attribute to format to the output stream
   */
  @Override
  public void printAttribute(final Attribute attribute) {
    final String id = attribute.getID();
    // don't do anything if the attribute should not be part of the output
    // don't worry about upercase/lowercase letters it's already part of the
    // method being called below
    if (this.omittedAttribute(id))
      return;

    if (this.attributesOnly) {
      printLine(String.format("%s%c", id, this.separator));
      return;
    }

    Enumeration<?> value = null;
    try {
      value = attribute.getAll();
    }
    catch (NamingException e) {
      value = null;
    }
    if (value == null) {
      printLine(String.format("%s%c ", id, this.separator));
      return;
    }

    // Loop on values for this attribute
    while (value.hasMoreElements()) {
      final Object entry = value.nextElement();
      final byte[] bytes = (entry instanceof byte[]) ? (byte[])entry : ((String)entry).getBytes();
      // don't change the sequence of the evalutation below
      // the check for binary must always superseeds check for printable if
      // we have to encode a string like a password
      boolean binary = (binaryAttribute(id) || needsEncoding(bytes));
      if (this.toFiles) {
        try {
          FileOutputStream f = tempFile(id);
          f.write(binary ? encodeBytes(bytes) : bytes);
        }
        catch (Exception e) {
          System.err.println(String.format("Error writing values of %s, %s", id, e.toString()));
          System.exit(1);
        }
      }
      else {
        if (bytes.length > 0)
          if (binary)
            printLine(String.format("%s%c%c %s", id, this.separator, this.separator, encodeBytes(bytes)));
          else
            printLine(String.format("%s%c %s", id, this.separator, StringUtility.bytesToString(bytes)));
        else
          printLine(String.format("%s%c ", id, this.separator));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printComment (DirectoryFileWriter)
  /**
   ** Print a comment.
   **
   ** @param  comment            the comment to print to the output stream
   */
  @Override
  public void printComment(final String comment) {
    printLine(String.format("# %s", comment));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printSchema
  /**
   ** Default schema writer - assumes an ordinary entry
   **
   ** @param  entry              a {@link Binding} containing schema to be
   **                            formatted to the output stream
   */
  public void printSchema(final Binding entry) {
    final LDAPRecord schema = (LDAPRecord)entry.getObject();
    printEntry(schema);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntry
  /**
   ** The main method of LDIFWriter.
   ** <p>
   ** It calls printEntryStart, printAttribute, and printEntryEnd of derived
   ** classes.
   **
   ** @param  result             a {@link LDAPRecord} to be formatted to the
   **                            output stream
   */
  public void printEntry(final LDAPRecord result) {
    result.toStream(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  printAttribute
  /**
   ** Print an attribute of an entry
   **
   ** @param  name               the name of the attribute to format to the
   **                            output stream.
   ** @param  value              the value to be placed in the output.
   */
  public void printAttribute(final String name, final String value) {
    if (this.attributesOnly) {
      printLine(String.format("%s%c", name, this.separator));
      return;
    }

    final byte[] encoded = value.getBytes(StringUtility.LATIN);
    if (needsEncoding(encoded))
      printLine(String.format("%s%c%c %s", name, this.separator, this.separator, encodeBytes(encoded)));
    else
      printLine(String.format("%s%c %s", name, this.separator, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  printLine
  protected void printLine(final String line) {
    if (this.folding)
      breakLine(line);
    else {
      this.writer.print(line);
      this.writer.print(SystemConstant.LINEBREAK);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  tempFile
  /**
   ** Create a unique file name in the temp folder and open an output stream to
   ** the file.
   **
   ** @param  name               base name of file; an extension is appended
   **                            which consists of a number that makes the name
   **                            unique.
   **
   ** @return                    an open output stream to the file.
   **
   ** @throws IOException       if the file couldn't be opened for output
   */
  protected FileOutputStream tempFile(final String name)
    throws IOException {

    int num = 0;
    File f;
    String filename;
    do {
      filename = name + SystemConstant.PERIOD + num;
      f = new File(filename);
      num++;
    }
    while (f.exists());
    printLine(String.format("%s%c %s", name, this.separator, filename));
    return new FileOutputStream(f);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   breakLine
  /**
   ** Outputs the String in LDIF line-continuation format. No line will be
   ** longer than the given max. A continuation line starts with a single blank
   ** space.
   **
   ** @param  line              the given string being printed out
   */
  protected void breakLine(final String line) {
    breakLine(line, MAX_LINE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   breakLine
  /**
   ** Outputs the String in LDIF line-continuation format. No line will be
   ** longer than the given max. A continuation line starts with a single blank
   ** space.
   **
   ** @param  line               the given line being printed out
   ** @param  max                the maximum characters allowed in the line
   */
  protected void breakLine(final String line, final int max) {
    int leftToGo = line.length();
    int written  = 0;
    int maxChars = max;

    // Limit to max characters per line
    while (leftToGo > 0) {
      int toWrite = Math.min(maxChars, leftToGo);
      String s = line.substring(written, written + toWrite);
      if (written != 0)
        this.writer.print(SystemConstant.BLANK);
      else
        maxChars -= 1;

      this.writer.print(s);
      // Don't use pw.println, because it outputs an extra CR in Win32
      this.writer.print(SystemConstant.LINEBREAK);

      written  += toWrite;
      leftToGo -= toWrite;
    }
  }
}