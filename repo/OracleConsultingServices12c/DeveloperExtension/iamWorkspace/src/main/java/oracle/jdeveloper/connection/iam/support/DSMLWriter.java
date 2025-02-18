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

    File        :   DSMLWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DSMLWriter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.io.File;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// abstract class DSMLWriter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Class for outputting LDAP entries to a stream as DSML.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
abstract class DSMLWriter extends LDAPWriter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected int level  = 0;
  protected int indent = 2;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriter</code> object to write the LDAP data to the
   ** specified {@link File} path.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  file               the {@link File} path of the DSML file to
   **                            write.c   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  protected DSMLWriter(final DirectorySchema schema, final File file)
    throws DirectoryException {

    // ensure inheritance
    super(schema, file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printComment (LDAPWriter)
  /**
   ** Print a comment.
   **
   ** @param  comment            the comment to print to the output stream
   */
  @Override
  public void printComment(final String comment) {
    this.writer.println("<!--");
    this.writer.println(comment);
    this.writer.println("-->");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory Method to create a proper DSML writer belonging to the provided
   ** version.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  file               the {@link File} path of the DSML file to
   **                            write.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  version            the DSML version the writer has to support.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a proper DSML writer belonging to the provided
   **                            version.
   **                            <br>
   **                            Possible object is <code>DSMLWriter</code>.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public static DSMLWriter build(final DirectorySchema schema, final File file, final int version)
    throws DirectoryException {

    switch (version) {
      case 1  : return new DSMLWriterV1(schema, file);
      case 2  : return new DSMLWriterV2(schema, file);
      default : return null;// TODO: throw a DirectoryException
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   leftIndent
  /**
   ** Padding a string to a passed length.
   **
   ** @param  input              the string to indent.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  length             the length to pad to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the padded input string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String leftIndent(final String input, final int length) {
    return leftIndent(input, StringUtility.BLANK, length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   leftIndent
  /**
   ** Padding a string to a passed length.
   **
   ** @param  input              the string to indent.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  indentChar         the character to indent with.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   ** @param  length             the length to pad to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the padded input string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String leftIndent(final String input, final char indentChar, final int length) {
    final StringBuilder buffer = new StringBuilder(input.length() + length);
    fillBufferWith(buffer, indentChar, length);
    buffer.append(input);
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  printEscapedValue
  /**
   ** Print the element start, the value with escaping of special characters,
   ** and the element end
   **
   ** @param  prolog             element start
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              value to be escaped
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  epilog             element end
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected void printEscapedValue(String prolog, String value, String epilog) {
    this.writer.print(leftIndent(prolog, this.level * this.indent));

    int l = value.length();
    char[] text = new char[l];
    value.getChars(0, l, text, 0);
    for (int i = 0; i < l; i++) {
      char c = text[i];
      switch (c) {
        case '<' : this.writer.print("&lt;");
                   break;
        case '&' : this.writer.print("&amp;");
                   break;
        default  : this.writer.print(c);
      }
    }
    this.writer.println(epilog);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fillBufferWith
  //
  private static final void fillBufferWith(final StringBuilder buffer, final char fillChar, int length) {
    for (int i = 0; i < length; i++)
      buffer.append(fillChar);
  }
}