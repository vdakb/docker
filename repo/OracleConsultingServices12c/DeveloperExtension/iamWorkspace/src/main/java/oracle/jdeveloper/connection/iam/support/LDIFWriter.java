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

    File        :   LDIFWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LDIFWriter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.util.Enumeration;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

import java.nio.charset.StandardCharsets;

import javax.naming.Binding;
import javax.naming.NamingException;

import javax.naming.directory.Attribute;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.model.DirectoryName;
import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.service.DirectoryService;
import oracle.jdeveloper.connection.iam.service.DirectoryException;

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
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
class LDIFWriter extends LDAPWriter {

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
   ** Constructs an <code>LDIFWriter</code> object to write the LDIF to a
   ** specified file.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  file               the name of the LDIF file to parse.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  protected LDIFWriter(final DirectorySchema schema, final File file)
    throws DirectoryException {

    // ensure inheritance
    super(schema, file);

    // initialize instance attributes
    this.folding = true;
    this.toFiles = false;
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
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void printEntryStart(String distinguishedName) {
    if (distinguishedName == null)
      distinguishedName = StringUtility.EMPTY;

    printAttribute(DirectoryService.DN, distinguishedName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntryEnd (DirectoryFileWriter)
  /**
   ** Print epilogue to entry.
   **
   ** @param  distinguishedName  the DN of the entry
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void printEntryEnd(final String distinguishedName) {
    // don't use pw.println, because it outputs an extra CR in Win32
    this.writer.print('\n');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printAttribute (DirectoryFileWriter)
  /**
   ** Print an attribute of an entry
   **
   ** @param  attribute          the attribute to format to the output stream.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  @Override
  public void printAttribute(final Attribute attribute) {
    final String id = attribute.getID();
    // skip any attribute that's omitted by declaration or is a well known
    // operational attribute
    // don't do anything if the attribute should not be part of the output
    // don't worry about upercase/lowercase letters it's already part of the
    // method being called below
    if (DirectorySchema.omit(id) || (this.omitReadonly && this.schema.readonly(id)))
      return;

    if (this.attributesOnly) {
      printLine(String.format("%s%c", id, this.separator), false);
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
      printLine(String.format("%s%c ", id, this.separator), false);
      return;
    }

    // Loop on values for this attribute
    while (value.hasMoreElements()) {
      final Object entry = value.nextElement();
      final byte[] bytes = (entry instanceof byte[]) ? (byte[])entry : ((String)entry).getBytes();
      // don't change the sequence of the evalutation below
      // the check for binary must always superseeds check for printable if
      // we have to encode a string like a password
      boolean binary = (DirectorySchema.binary(id) || needsEncoding(bytes));
      if (this.toFiles) {
        try {
          FileOutputStream f = tempFile(id);
          f.write(binary ? encodeBytes(bytes).getBytes() : bytes);
        }
        catch (Exception e) {
          System.err.println(String.format("Error writing values of %s, %s", id, e.toString()));
          System.exit(1);
        }
      }
      else {
        if (bytes.length > 0)
          if (binary)
            printLine(String.format("%s%c%c %s", id, this.separator, this.separator, encodeBytes(bytes)), false);
          else
            printLine(String.format("%s%c %s", id, this.separator, StringUtility.bytesToString(bytes)), false);
        else
          printLine(String.format("%s%c ", id, this.separator), false);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printComment (DirectoryFileWriter)
  /**
   ** Print a comment.
   **
   ** @param  comment            the comment to print to the output stream.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void printComment(final String comment) {
    printLine(comment, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory Method to create a proper LDIF writer.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  file               the abstract path of the LDIF file to write.
   **                            <br>
   **                            Allowed object is {@link File}.
   */
  public static LDIFWriter build(final DirectorySchema schema, final File file)
    throws DirectoryException {

    return new LDIFWriter(schema, file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printSchema
  /**
   ** Default schema writer - assumes an ordinary entry.
   **
   ** @param  entry              a {@link Binding} containing schema to be
   **                            formatted to the output stream
   **                            <br>
   **                            Allowed object is {@link Binding}.
   */
  public void printSchema(final Binding entry)
    throws IOException {

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
   **                            output stream.
   **                            <br>
   **                            Allowed object is {@link LDAPRecord}.
   */
  public void printEntry(final LDAPRecord result)
    throws IOException {

    result.toStream(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  printAttribute
  /**
   ** Print an attribute of an entry
   **
   ** @param  name               the name of the attribute to format to the
   **                            output stream.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the attribute.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   */
  public void printAttribute(final String name, final DirectoryName value) {
    if (this.attributesOnly) {
      printLine(String.format("%s%c", name, this.separator), false);
      return;
    }

    final byte[] encoded = value.toString().getBytes(StandardCharsets.ISO_8859_1);
    if (needsEncoding(encoded))
      printLine(String.format("%s%c%c %s", name, this.separator, this.separator, encodeBytes(encoded)), false);
    else
      printLine(String.format("%s%c %s", name, this.separator, value), false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  printAttribute
  /**
   ** Print an attribute of an entry
   **
   ** @param  name               the name of the attribute to format to the
   **                            output stream.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void printAttribute(final String name, final String value) {
    if (this.attributesOnly) {
      printLine(String.format("%s%c", name, this.separator), false);
      return;
    }

    final byte[] encoded = value.getBytes(StandardCharsets.ISO_8859_1);
    if (needsEncoding(encoded))
      printLine(String.format("%s%c%c %s", name, this.separator, this.separator, encodeBytes(encoded)), false);
    else
      printLine(String.format("%s%c %s", name, this.separator, value), false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  printLine
  /**
   **
   ** @param  line               the given string being printed out.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  comment            indicates that a comment is written
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
  */
  protected void printLine(final String line, final boolean comment) {
    if (this.folding)
      breakLine(line, comment);
    else {
      this.writer.print(line);
      // don't use pw.println, because it outputs an extra CR in Win32
      this.writer.print('\n');
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
      filename = name + '.' + num;
      f = new File(filename);
      num++;
    }
    while (f.exists());
    printLine(String.format("%s%c %s", name, this.separator, filename), false);
    return new FileOutputStream(f);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   breakLine
  /**
   ** Outputs the String in LDIF line-continuation format. No line will be
   ** longer than the given max. A continuation line starts with a single blank
   ** space.
   **
   ** @param  line               the given string being printed out.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  comment            indicates that a comment is written
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  protected void breakLine(final String line, final boolean comment) {
    breakLine(line, MAX_LINE, comment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   breakLine
  /**
   ** Outputs the String in LDIF line-continuation format. No line will be
   ** longer than the given max. A continuation line starts with a single blank
   ** space.
   **
   ** @param  line               the given line being printed out.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  max                the maximum characters allowed in the line
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  comment            indicates that a comment is written
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  protected void breakLine(final String line, final int max, final boolean comment) {
    int leftToGo = line.length();
    int written  = 0;
    int maxChars = max;

    // Limit to max characters per line
    while (leftToGo > 0) {
      int remain = Math.min(maxChars, leftToGo);
      String s = line.substring(written, written + remain);
      if (comment)
        this.writer.print("# ");
      if (written != 0)
        this.writer.print(StringUtility.BLANK);
      else
        maxChars -= 1;

      this.writer.print(s);
      // don't use writer.println, because it outputs an extra CR in Win32
      this.writer.print('\n');

      written  += remain;
      leftToGo -= remain;
    }
  }
}