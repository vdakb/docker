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

    File        :   DSMLWriterV1.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DSMLWriterV1.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.util.Enumeration;

import java.io.File;

import javax.naming.NamingException;

import javax.naming.directory.Attribute;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.service.DirectoryService;
import oracle.jdeveloper.connection.iam.service.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// class DSMLWriterV1
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Class for outputting LDAP entries to a stream as DSML V1.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class DSMLWriterV1 extends DSMLWriter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriterV1</code> object to write the LDAP data to
   ** the specified {@link File} path.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  file               the {@link File} path of the DSML file to
   **                            write.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  DSMLWriterV1(final DirectorySchema schema, final File file)
    throws DirectoryException {

    // ensure inheritance
    super(schema, file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printPrologue (LDAPWriter)
  /**
   ** Print prologue to file.
   */
  @Override
  public final void printPrologue() {
    this.writer.println("<dsml:dsml xmlns:dsml=\"http://www.dsml.org/DSML\">");
    this.level++;
    this.writer.println(leftIndent("<dsml:directory-entries>", this.level * this.indent));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEpilogue (LDAPWriter)
  /**
   ** Print epilogue to file.
   */
  @Override
  public final void printEpilogue() {
    this.writer.println(leftIndent("</dsml:directory-entries>", this.level * this.indent));
    this.level--;
    this.writer.println("</dsml:dsml>");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntryStart (LDAPWriter)
  /**
   ** Print prologue to entry
   **
   ** @param  distinguishedName  the DN of the entry
   */
  @Override
  public void printEntryStart(String distinguishedName) {
    if (distinguishedName == null)
      distinguishedName = StringUtility.EMPTY;

    this.writer.println(leftIndent("<dsml:entry dn=\"" + distinguishedName + "\">", ++this.level * this.indent));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntryEnd (LDAPWriter)
  /**
   ** Print epilogue to entry.
   **
   ** @param  distinguishedName  the DN of the entry
   */
  @Override
  public void printEntryEnd(final String distinguishedName) {
    this.writer.println(leftIndent("</dsml:entry>", (this.level--) * this.indent));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printAttribute (LDAPWriter)
  /**
   ** Print an attribute of an entry
   **
   ** @param  attribute          the attribute to format to the output stream.
   */
  @Override
  public void printAttribute(final Attribute attribute) {
    final String type = attribute.getID();

    try {
      // Object classes are treated differently in DSML. Also, they are always
      // String-valued
      if (DirectoryService.OBJECTCLASS.equalsIgnoreCase(type)) {
        Enumeration<?> i = attribute.getAll();
        if (i != null) {
          this.writer.println(leftIndent("<dsml:objectclass>", (++this.level) * this.indent));
          this.level++;
          while (i.hasMoreElements()) {
            final String element = (String)i.nextElement();
            this.writer.println(leftIndent("<dsml:oc-value>" + element + "</dsml:oc-value>", this.level * this.indent));
          }
          this.level--;
          this.writer.println(leftIndent("</dsml:objectclass>", (this.level--) * this.indent));
        }
        return;
      }
      this.writer.println(leftIndent("<dsml:attr name=\"" + type + "\">",(++this.level) * this.indent));
      this.level++;

      // loop on values for this attribute
      for (int i = 0; i < attribute.size(); i++) {
        final String id = attribute.getID();
        // skip any attribute that's omitted by declaration or is a well known
        // operational attribute
        // don't do anything if the attribute should not be part of the output
        // don't worry about upercase/lowercase letters it's already part of the
        // method being called below
        if (DirectorySchema.omit(id) || (this.omitReadonly && this.schema.readonly(id)))
          continue;

        final Object entry = attribute.get(i);
        final byte[] bytes = (entry instanceof byte[]) ? (byte[])entry : ((String)entry).getBytes();
        // don't change the sequence of the evalutaion below
        // the check for binary must always superseeds check for printable if
        // we have to encode a string like a password
        boolean binary = (DirectorySchema.binary(id) || needsEncoding(bytes));
        if (binary) {
          this.writer.print(leftIndent("<dsml:value encoding=\"base64\">", this.level * this.indent));
          this.writer.print(encodeBytes(bytes));
          this.writer.println("</dsml:value>");
        }
        else {
          printEscapedValue("<dsml:value>", StringUtility.bytesToString(bytes), "</dsml:value>");
        }
      }
      this.level--;
      this.writer.println(leftIndent("</dsml:attr>", (this.level--) * this.indent));
    }
    catch (NamingException e) {
      printEscapedValue("<dsml:objectclass>", e.getLocalizedMessage(), "</dsml:objectclass>");
      return;
    }
  }
}