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

    File        :   DSMLV2Writer.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DSMLV2Writer.


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
// class DSMLV2Writer
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Class for outputting LDAP entries to a stream as DSML V2.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class DSMLV2Writer extends DSMLWriter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLV2Writer</code> object to write the LDAP data to
   ** stdout.
   */
  protected DSMLV2Writer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLV2Writer</code> object to write the LDAP data to
   ** the specified {@link File} path.
   **
   ** @param  file               the {@link File} path of the DSML file to
   **                            write.
   **
   ** @throws FeatureException   an I/O error has occurred.
   */
  protected DSMLV2Writer(final File file)
    throws FeatureException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLV2Writer</code> object to write the LDAP data to
   ** the specified {@link File} path.
   **
   ** @param  file               the name of the DSML file to write.
   **
   ** @throws FeatureException an I/O error has occurred.
   */
  protected DSMLV2Writer(final String file)
    throws FeatureException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLV2Writer</code> object to write the LDAP data to
   ** an output stream.
   **
   ** @param  stream             output stream receiving the DSML data.
   */
  protected DSMLV2Writer(final DataOutputStream stream) {
    // ensure inheritance
    this(new PrintWriter(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLV2Writer</code> object to output entries to a
   ** stream as DSML.
   **
   ** @param  writer             output stream receiving the DSML data.
   */
  protected DSMLV2Writer(final PrintWriter writer) {
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
    this.writer.println("<dsml:batchRequest xmlns:dsml=\"urn:oasis:names:tc:DSML:2:0:core\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEpilogue (LDAPWriter)
  /**
   ** Print epilogue to file.
   */
  @Override
  public final void printEpilogue() {
    this.writer.println("</dsml:batchRequest>");
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

    this.writer.println(StringUtility.leftIndent("<dsml:addRequest dn=\"" + distinguishedName + "\">", ++this.level * this.indent));
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
    this.writer.println(StringUtility.leftIndent("</dsml:addRequest>", (this.level--) * this.indent));
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
      // Object classes are no longer treated differently in DSML V2
      this.writer.println(StringUtility.leftIndent("<dsml:attr name=\"" + attribute.getID() + "\">",(++this.level) * this.indent));
      this.level++;
      // Loop on values for this attribute
      for (int i = 0; i < attribute.size(); i++) {
        final Object entry = attribute.get(i);
        final byte[] bytes = (entry instanceof byte[]) ? (byte[])entry : ((String)entry).getBytes();
        // don't change the sequence of the evalutaion below
        // the check for binary must always superseeds check for printable if
        // we have to encode a string like a password
        boolean binary = (binaries(attribute.getID()) || needsEncoding(bytes));
        if (binary) {
          this.writer.print(StringUtility.leftIndent("<dsml:value encoding=\"base64\">", this.level * this.indent));
          this.writer.print(encodeBytes(bytes));
          this.writer.println("</dsml:value>");
        }
        else {
          printEscapedValue("<dsml:value>", StringUtility.bytesToString(bytes), "</dsml:value>");
        }
      }
      this.level--;
      this.writer.println(StringUtility.leftIndent("</dsml:attr>", (this.level--) * this.indent));
    }
    catch (NamingException e) {
      printEscapedValue("<dsml:objectclass>", e.getLocalizedMessage(), "</dsml:objectclass>");
      return;
    }
  }
}