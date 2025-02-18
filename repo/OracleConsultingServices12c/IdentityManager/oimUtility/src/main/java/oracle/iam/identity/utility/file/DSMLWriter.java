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
    Subsystem   :   Common Shared LDAP Facilities

    File        :   DSMLWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DSMLWriter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Enumeration;

import java.io.File;
import java.io.DataOutputStream;
import java.io.PrintWriter;

import javax.naming.NamingException;

import javax.naming.directory.Attribute;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.ldap.DirectoryException;
import oracle.iam.identity.foundation.ldap.DirectoryFileWriter;

////////////////////////////////////////////////////////////////////////////////
// class DSMLWriter
// ~~~~~ ~~~~~~~~~~
/**
 ** Class for outputting LDAP entries to a stream as DSML.
 */
public class DSMLWriter extends DirectoryFileWriter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriter</code> object to write the  DSML data to
   ** stdout.
   */
  public DSMLWriter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriter</code> object to write the DSML to a
   ** specified {@link File} path.
   **
   ** @param  file               the {@link File} path of the DSML file to
   **                            write.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public DSMLWriter(final File file)
    throws DirectoryException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriter</code> object to write the DSML to a
   ** specified file.
   **
   ** @param  file               the name of the DSML file to write.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public DSMLWriter(final String file)
    throws DirectoryException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriter</code> object to write the DSML data to an
   ** output stream.
   **
   ** @param  stream             output stream receiving the DSML data.
   */
  public DSMLWriter(final DataOutputStream stream) {
    // ensure inheritance
    this(new PrintWriter(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriter</code> object to output entries to a
   ** stream as DSML.
   **
   ** @param  writer             output stream receiving the DSML data.
   */
  public DSMLWriter(final PrintWriter writer) {
    // ensure inheritance
    super(writer);
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
    this.writer.print("<dsml:dsml xmlns:dsml=\"http://www.dsml.org/DSML\"><dsml:directory-entries>");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEpilogue (DirectoryFileWriter)
  /**
   ** Print epilogue to file.
   */
  @Override
  public final void printEpilogue() {
    this.writer.print("</dsml:directory-entries></dsml:dsml>");
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

    this.writer.print("<dsml:entry dn=\"" + distinguishedName + "\">");
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
    this.writer.print("</dsml:entry>");
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
    final String type = attribute.getID();

    try {
      // Object classes are treated differently in DSML. Also, they are always
      // String-valued
      if (OBJECT_CLASS.equalsIgnoreCase(type)) {
        Enumeration<?> i = attribute.getAll();
        if (i != null) {
          this.writer.print("<dsml:objectclass>");
          while (i.hasMoreElements()) {
            final String element = (String)i.nextElement();
            this.writer.print("<dsml:oc-value>" + element + "</dsml:oc-value>");
          }
          this.writer.print("</dsml:objectclass>");
        }
        return;
      }
      this.writer.print("<dsml:attr name=\"" + attribute.getID() + "\">");
      // Loop on values for this attribute
      for (int i = 0; i < attribute.size(); i++) {
        final Object entry = attribute.get(i);
        final byte[] bytes = (entry instanceof byte[]) ? (byte[])entry : ((String)entry).getBytes();
        // don't change the sequence of the evalutaion below
        // the check for binary must always superseeds check for printable if
        // we have to encode a string like a password
        boolean binary = (binaryAttribute(attribute.getID()) || needsEncoding(bytes));
        if (binary) {
          this.writer.print("<dsml:value encoding=\"base64\">");
          this.writer.print(encodeBytes(bytes));
          this.writer.print("</dsml:value>");
        }
        else {
          printEscapedValue("<dsml:value>", StringUtility.bytesToString(bytes), "</dsml:value>");
        }
      }
      this.writer.print("</dsml:attr>");
    }
    catch (NamingException e) {
      this.writer.print("<dsml:objectclass>" + e.getLocalizedMessage() + "</dsml:objectclass>");
      return;
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
    this.writer.print(String.format("<!-- %s -->", comment));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  printEscapedValue
  /**
   ** Print the element start, the value with escaping of special characters,
   ** and the element end
   **
   ** @param  prolog             element start
   ** @param  value              value to be escaped
   ** @param  epilog             element end
   */
  protected void printEscapedValue(String prolog, String value, String epilog) {
    this.writer.print(prolog);

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
    this.writer.print(epilog);
  }
}