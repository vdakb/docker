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

    File        :   DSMLWriter.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DSMLWriter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.io.File;
import java.io.PrintWriter;
import java.io.DataOutputStream;

import javax.naming.NamingException;

import javax.naming.directory.Attribute;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// class JSONWriter
// ~~~~~ ~~~~~~~~~~
/**
 ** Class for outputting LDAP entries to a stream as JSON.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JSONWriter extends LDAPWriter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONWriter</code> object to write the LDAP data to
   ** stdout.
   */
  protected JSONWriter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONWriter</code> object to write the LDAP to a
   ** specified {@link File} path.
   **
   ** @param  file               the {@link File} path of the JSON file to
   **                            write.
   **
   ** @throws FeatureException   an I/O error has occurred.
   */
  protected JSONWriter(final File file)
    throws FeatureException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONWriter</code> object to write the LDAP to a
   ** specified file.
   **
   ** @param  file               the name of the JSON file to write.
   **
   ** @throws FeatureException an I/O error has occurred.
   */
  protected JSONWriter(final String file)
    throws FeatureException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONWriter</code> object to write the LDAP data to an
   ** output stream.
   **
   ** @param  stream             output stream receiving the JSON data.
   */
  protected JSONWriter(final DataOutputStream stream) {
    // ensure inheritance
    this(new PrintWriter(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONWriter</code> object to output entries to a
   ** stream as DSML.
   **
   ** @param  writer             output stream receiving the JSON data.
   */
  protected JSONWriter(final PrintWriter writer) {
    // ensure inheritance
    super(writer);
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
      distinguishedName = SystemConstant.EMPTY;

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
    try {
      printEscapedValue(",\"", attribute.getID(), "\":[");
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory Method to create a proper JSON writer.
   **
   ** @param  file               the abstract path of the JSON file to write.
   **
   ** @return                    the {@link JSONWriter} for <code>file</code>.
   **
   ** @throws FeatureException   an I/O error has occurred.
   */
  public static JSONWriter create(final File file)
    throws FeatureException {

    return new JSONWriter(file);
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