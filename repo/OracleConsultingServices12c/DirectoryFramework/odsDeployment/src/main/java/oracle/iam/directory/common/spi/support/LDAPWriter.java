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

    File        :   LDAPWriter.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPWriter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.naming.directory.Attribute;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureConstant;
import oracle.iam.directory.common.FeatureException;

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
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class LDAPWriter extends LDAPFile {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String    STANDARD        = "System.out";

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected boolean                attributesOnly;
  protected final PrintWriter      writer;
  protected final List<String>     binaryAttribute = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////
  // enum Format
  // ~~~~ ~~~~~~
  /**
   ** Java class for output file format.
   */
  public enum Format {

      LDIF(FeatureConstant.FORMAT_LDIF)
    , DSML(FeatureConstant.FORMAT_DSML)
    , JSON(FeatureConstant.FORMAT_JSON)
    ;
    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:5094733185332671947")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String      type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Format</code> that allows use as a JavaBean.
     **
     ** @param  type             the format type.
     */
    Format(final String type) {
      // initialize instance attributes
      this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the value of the type property.
     **
     ** @return                    possible object is {@link String}.
     */
    public String type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   fromValue
    /**
     ** Factory method to create a proper format from the given string value.
     **
     ** @param  type               the string value the format should be
     **                            returned for.
     **
     ** @return                    the format.
     */
    public static Format fromValue(final String type) {
      for (Format cursor : Format.values()) {
        if (cursor.type.equals(type))
          return cursor;
      }
      throw new IllegalArgumentException(type);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to write the LDIF or DSML
   ** data to stdout.
   */
  public LDAPWriter() {
    // ensure inheritance
    super(STANDARD);

    // initialize the output stream
    this.attributesOnly = false;
    this.writer         = new PrintWriter(System.out);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to write the LDIF or
   ** DSML to a specified file.
   **
   ** @param  file               the abstract path of the LDIF file to parse or
   **                            write.
   **
   ** @throws FeatureException   an I/O error has occurred.
   */
  public LDAPWriter(final File file)
    throws FeatureException {

    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to write the LDIF or
   ** DSML to a specified file.
   **
   ** @param  file               the abstract path of the LDIF file to parse or
   **                            write.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **
   ** @throws FeatureException an I/O error has occurred.
   */
  public LDAPWriter(final File file, final char separator)
    throws FeatureException {

    // ensure inheritance
    super(file, separator);

    this.attributesOnly = false;
    // initialize the output stream
    try {
      this.writer = new PrintWriter(file);
    }
    catch (FileNotFoundException e) {
      throw new FeatureException(FeatureError.FILE_MISSING, file.getAbsolutePath());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to write the LDIF or
   ** DSML to a specified file.
   **
   ** @param  file             the name of the LDIF file to parse or write.
   **
   ** @throws FeatureException an I/O error has occurred.
   */
  public LDAPWriter(final String file)
    throws FeatureException {

    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to write the LDIF or
   ** DSML to a specified file.
   **
   ** @param  file               the name of the LDIF file to parse or write.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **
   ** @throws FeatureException an I/O error has occurred.
   */
  public LDAPWriter(final String file, final char separator)
    throws FeatureException {

    // ensure inheritance
    super(file, separator);

    this.attributesOnly = false;
    // initialize the output stream
    try {
      this.writer = new PrintWriter(file, StringUtility.ASCII.name());
    }
    catch (FileNotFoundException e) {
      throw new FeatureException(FeatureError.FILE_MISSING, file);
    }
    catch (UnsupportedEncodingException e) {
      throw new FeatureException(FeatureError.FILE_ENCODING_TYPE, StringUtility.ASCII.name());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to output entries to
   ** a stream as LDIF or DSML.
   **
   ** @param  stream             output stream
   */
  public LDAPWriter(final OutputStream stream) {
    this(new PrintWriter(stream), SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to output entries to
   ** a stream as LDIF or DSML.
   **
   ** @param  stream             output stream
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  public LDAPWriter(final OutputStream stream, final char separator) {
    // ensure inheritance
    this(new PrintWriter(stream), separator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to output entries to
   ** a stream as LDIF or DSML.
   **
   ** @param  writer             output stream
   */
  public LDAPWriter(final PrintWriter writer) {
    this(writer, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPWriter</code> object to output entries to
   ** a stream as LDIF or DSML.
   **
   ** @param  writer             output stream
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  public LDAPWriter(final PrintWriter writer, final char separator) {
    // ensure inheritance
    super(separator);

    // initialize the output stream
    this.attributesOnly = false;
    this.writer         = writer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesOnly
  /**
   ** Sets the option that only attribute names should be written to the file.
   **
   ** @param  attributesOnly     <code>true</code> if only the attribute names
                                 should be written to the file.
   */
  public void attributesOnly(final boolean attributesOnly) {
    this.attributesOnly = attributesOnly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributesOnly
  /**
   ** Returns the option that only attribute names should be written to the
   ** file.
   **
   ** @return                    <code>true</code> if only the attribute names
                                 should be written to the file: otherwise
   **                            <code>false</code>
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
  // Method:   create
  /**
   ** Factory method to create an instance of {@link LDAPWriter} which spools
   ** out the reaoutl set of an LDAP search.
   **
   ** @param  format             the format descriptor one of
   **                            <ul>
   **                              <li>{@link FeatureConstant#FORMAT_LDIF}
   **                              <li>{@link FeatureConstant#FORMAT_DSML}
   **                              <li>{@link FeatureConstant#FORMAT_JSON}
   **                             </ul>
   ** @param  version            the DSML version the writer has to support.
   ** @param  export             the absolut {@link File} path of the
   **                            destination.
   **
   ** @return                    an instance of {@link LDAPWriter}.
   **
   ** @throws FeatureException   an I/O error has occurred.
   */
  public static LDAPWriter create(final String format, final int version, final File export)
    throws FeatureException {

    switch (Format.fromValue(format)) {
      case DSML : return DSMLWriter.create(export, version);
      case JSON : return JSONWriter.create(export);
      case LDIF :
      default   : return LDIFWriter.create(export);// TODO: throw a FeatureException
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
   ** @param  distinguishedName  the DN of the entry
   */
  public abstract void printEntryStart(final String distinguishedName);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printAttribute
  /**
   ** Print an attribute of an entry
   **
   ** @param  attribute          the attribute to format to the output stream
   */
  public abstract void printAttribute(final Attribute attribute);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printComment
  /**
   ** Print a comment.
   **
   ** @param  comment            the comment to print to the output stream
   */
  public abstract void printComment(final String comment);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printEntryEnd
  /**
   ** Print epilogue to entry.
   **
   ** @param  distinguishedName  the DN of the entry
   */
  public abstract void printEntryEnd(final String distinguishedName);
}