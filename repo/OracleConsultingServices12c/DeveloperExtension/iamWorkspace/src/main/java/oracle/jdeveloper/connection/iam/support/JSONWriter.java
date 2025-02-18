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

    File        :   JSONWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    JSONWriter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.io.File;

import javax.naming.NamingException;

import javax.naming.directory.Attribute;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// class JSONWriter
// ~~~~~ ~~~~~~~~~~
/**
 ** Class for outputting LDAP entries to a stream as JSON.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class JSONWriter extends LDAPWriter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONWriter</code> object to write the LDAP to a
   ** specified {@link File} path.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  file               the {@link File} path of the JSON file to
   **                            write.
   **                            <br>
   **                            Allowed object is {@link File}.c   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  private JSONWriter(final DirectorySchema schema, final File file)
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
    this.writer.print("[");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEpilogue (LDAPWriter)
  /**
   ** Print epilogue to file.
   */
  @Override
  public final void printEpilogue() {
    this.writer.print("]");
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

    this.writer.print("{\"dn\":\"" + distinguishedName + "\"");
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
    this.writer.print("}");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printAttribute (LDAPWriter)
  /**
   ** Print an attribute of an entry
   **
   ** @param  attribute          the attribute to format to the output stream
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

    try {
      printEscapedValue(",\"", id, "\":[");
      // Loop on values for this attribute
      for (int i = 0; i < attribute.size(); i++) {
        if (i > 0)
          this.writer.print(',');
        final Object entry = attribute.get(i);
        final byte[] bytes = (entry instanceof byte[]) ? (byte[])entry : ((String)entry).getBytes();
        printEscapedValue("\"", StringUtility.bytesToString(bytes), "\"");
      }
      this.writer.print("]");
    }
    catch (NamingException e) {
      printEscapedValue("\"", e.getLocalizedMessage(), "\"");
      return;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printComment (LDAPWriter)
  /**
   ** Print a comment.
   **
   ** @param  comment            the comment to print to the output stream
   */
  @Override
  public void printComment(final String comment) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory Method to create a proper JSON writer.
   **
   ** @param  schema             the schema definition the entries to export
   **                            are relying on.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  file               the {@link File} path of the JSON file to
   **                            write.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    the <code>JSONWriter</code> for
   **                            <code>file</code>.
   **                            <br>
   **                            Possible object is <code>JSONWriter</code>.
   **
   ** @throws DirectoryException an I/O error has occurred.
   */
  public static JSONWriter build(final DirectorySchema schema, final File file)
    throws DirectoryException {

    return new JSONWriter(schema, file);
  }

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
    this.writer.print(value);
    this.writer.print(epilog);
  }
}