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

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// abstract class DSMLWriter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Class for outputting LDAP entries to a stream as DSML.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DSMLWriter extends LDAPWriter {

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
   ** Constructs an <code>DSMLWriter</code> object to write the LDAP data to
   ** stdout.
   */
  protected DSMLWriter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriter</code> object to write the LDAP data to the
   ** specified {@link File} path.
   **
   ** @param  file               the {@link File} path of the DSML file to
   **                            write.
   **
   ** @throws FeatureException   an I/O error has occurred.
   */
  protected DSMLWriter(final File file)
    throws FeatureException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriter</code> object to write the LDAP data to the
   ** specified {@link File} path.
   **
   ** @param  file               the name of the DSML file to write.
   **
   ** @throws FeatureException an I/O error has occurred.
   */
  protected DSMLWriter(final String file)
    throws FeatureException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLWriter</code> object to write the LDAP data to an
   ** output stream.
   **
   ** @param  stream             output stream receiving the DSML data.
   */
  protected DSMLWriter(final DataOutputStream stream) {
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
  protected DSMLWriter(final PrintWriter writer) {
    // ensure inheritance
    super(writer);
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
  // Method:   create
  /**
   ** Factory Method to create a proper DSML writer belonging to the provided
   ** version.
   **
   ** @param  file               the abstract path of the DSML file to write.
   ** @param  version            the DSML version the writer has to support.
   **
   ** @return                    a proper DSML writer belonging to the provided
   **                            version.
   **
   ** @throws FeatureException   an I/O error has occurred.
   */
  public static DSMLWriter create (final File file, final int version)
    throws FeatureException {

    switch (version) {
      case 1  : return new DSMLV1Writer(file);
      case 2  : return new DSMLV2Writer(file);
      default : return null;// TODO: throw a FeatureException
    }
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
    this.writer.print(StringUtility.leftIndent(prolog, this.level * this.indent));

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
}