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

    File        :   LDAPReader.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPReader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureConstant;
import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// abstract class LDAPReader
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** LDAP Data Interchange Format (LDIF) is a file format used to import
 ** directory data from an LDAP server and to describe a set of changes
 ** to be applied to data in a directory. This format is described in the
 ** Internet draft
 ** <a href="ftp://ftp.ietf.org/internet-drafts/draft-good-ldap-ldif-00.txt" target="_blank">The LDAP Data Interchange Format (LDIF) - Technical Specification</a>.
 ** <p>
 ** This class implements an LDIF or DSML input operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class LDAPReader extends LDAPFile {

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DataInputStream stream;

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
    @SuppressWarnings("compatibility:-5291032650190054074")
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
    // Method:   from
    /**
     ** Factory method to create a proper format from the given string value.
     **
     ** @param  type               the string value the format should be
     **                            returned for.
     **
     ** @return                    the format.
     */
    public static Format from(final String type) {
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
   ** Constructs an <code>LDAPReader</code> object to parse the LDIF or
   ** DSML from stdin.
   */
  protected LDAPReader() {
    // ensure inheritance
    super();

    // initialize instance
    this.stream = new DataInputStream(System.in);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPReader</code> object to parse the LDIF or DSML
   ** from a specified file.
   **
   ** @param  file               the abstract path of the LDIF or DSML file to
   **                            parse.
   **
   ** @throws FeatureException   if the specified file could not be found.
   */
  protected LDAPReader(final File file)
    throws FeatureException {

    // ensure inheritance
    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  file               the abstract path of the LDIF or DSML file to
   **                            parse.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **
   ** @throws FeatureException   if the specified file could not be found.
   */
  protected LDAPReader(final File file, final char separator)
    throws FeatureException {

    // ensure inheritance
    super(file, separator);

    // initialize instance
    try {
      this.stream = new DataInputStream(new FileInputStream(this.handle));
    }
    catch (FileNotFoundException e) {
      throw new FeatureException(FeatureError.FILE_MISSING, file.getAbsolutePath());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  handle             the name of the LDIF or DSML file to parse.
   **
   ** @throws FeatureException   if the specified file could not be found.
   */
  protected LDAPReader(final String handle)
    throws FeatureException {

    // ensure inheritance
    this(handle, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  handle             the name of the LDIF or DSML file to parse.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **
   ** @throws FeatureException   if the specified file could not be found.
   */
  protected LDAPReader(final String handle, final char separator)
    throws FeatureException {

    // ensure inheritance
    super(handle, separator);

    // initialize instance
    try {
      this.stream = new DataInputStream(new FileInputStream(handle));
    }
    catch (FileNotFoundException e) {
      throw new FeatureException(FeatureError.FILE_MISSING, handle);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  stream             the {@link DataInputStream} of the LDIF or DSML
   **                            file to parse.
   */
  protected LDAPReader(final DataInputStream stream) {
    // ensure inheritance
    this(stream, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  stream             the {@link DataInputStream} of the LDIF or DSML
   **                            file to parse.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  protected LDAPReader(final DataInputStream stream, final char separator) {
    // ensure inheritance
    super(separator);

    // initialize instance
    this.stream = stream;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory Method to create a proper DSML reader belonging to the provided
   ** version.
   **
   ** @param  format             the format of the file to parse either
   **                            <ul>
   **                              <li>Format#DSML
   **                              <li>Format#JSON
   **                              <li>Format#LDIF
   **                            </ul>.
   ** @param  file               the abstract path of the DSML file to parse.
   ** @param  version            the DSML version the parser has to support.
   **
   ** @return                    a {@link LDAPReader} for the specified
   **                            <code>format</code> and <code>version</code>.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  public static LDAPReader create (final String format, final int version, final File file)
    throws FeatureException {

    switch (Format.from(format)) {
      case DSML : return DSMLReader.create(file, version);
      case JSON : return JSONReader.create(file);
      case LDIF :
      default   : return LDIFReader.create(file);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (LDAPFile)
  /**
   ** Closes the embedded stream, flushing it first.
   ** <p>
   ** Once the stream has been closed, further read() invocations will cause an
   ** IOException to be thrown.
   ** <p>
   ** Closing a previously closed stream has no effect.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  @Override
  public void close()
    throws FeatureException {

    if (this.stream == null)
      new FeatureException(FeatureError.ATTRIBUTE_IS_NULL, "stream");

    try {
      this.stream.close();
    }
    catch (IOException e) {
      throw new FeatureException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextRecord
  /**
   ** Returns the next record in the LDIF/DSML/JSON data.
   ** <p>
   ** You can call this method repeatedly to iterate through all records in the
   ** LDIF/DSML/JSON data.
   **
   ** @return                    the next record as a {@link LDAPRecord} object
   **                            or <code>null</code> if there are no more
   **                            records.
   **
   ** @throws FeatureException   if an I/O error occurs
   **
   ** @see    LDAPRecord
   */
  public abstract LDAPRecord nextRecord()
    throws FeatureException;
}