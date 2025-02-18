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

    File        :   LDAPReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LDAPReader.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;

import java.io.FileNotFoundException;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

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
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public abstract class LDAPReader extends LDAPFile {

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DataInputStream stream;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPReader</code> object to parse the LDIF, DSML or
   ** JSON from stdin.
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
   ** Constructs an <code>LDAPReader</code> object to parse the LDIF, DSML or
   ** JSON from the specified file.
   **
   ** @param  file               the abstract path of the LDIF, DSMLor JSON file
   **                            to parse.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  protected LDAPReader(final File file)
    throws DirectoryException {

    // ensure inheritance
    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryReader</code> object to parse the LDIF, DSML
   ** or JSON from the specified file.
   **
   ** @param  file               the abstract path of the LDIF or DSML file to
   **                            parse.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  protected LDAPReader(final File file, final char separator)
    throws DirectoryException {

    // ensure inheritance
    super(file, separator);

    // initialize instance
    try {
      this.stream = new DataInputStream(new FileInputStream(this.handle));
    }
    catch (FileNotFoundException e) {
      throw new DirectoryException(Bundle.format(Bundle.FILE_MISSING, file.getAbsolutePath()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryReader</code> object to parse the LDIF, DSML
   ** or JSON from the specified file.
   **
   ** @param  handle             the name of the LDIF or DSML file to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  protected LDAPReader(final String handle)
    throws DirectoryException {

    // ensure inheritance
    this(handle, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryReader</code> object to parse the LDIF, DSML
   ** or JSON from the specified file.
   **
   ** @param  handle             the name of the LDIF or DSML file to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  protected LDAPReader(final String handle, final char separator)
    throws DirectoryException {

    // ensure inheritance
    super(handle, separator);

    // initialize instance
    try {
      this.stream = new DataInputStream(new FileInputStream(handle));
    }
    catch (FileNotFoundException e) {
      throw new DirectoryException(Bundle.format(Bundle.FILE_MISSING, handle));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryReader</code> object to parse the LDIF, DSML
   ** or JSON from the stream.
   **
   ** @param  stream             the {@link DataInputStream} of the LDIF or DSML
   **                            file to parse.
   **                            <br>
   **                            Allowed object is {@link DataInputStream}.
   */
  protected LDAPReader(final DataInputStream stream) {
    // ensure inheritance
    this(stream, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryReader</code> object to parse the LDIF, DSML
   ** or JSON from the stream.
   **
   ** @param  stream             the {@link DataInputStream} of the LDIF or DSML
   **                            file to parse.
   **                            <br>
   **                            Allowed object is {@link DataInputStream}.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **                            <br>
   **                            Allowed object is <code>char</code>.
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
  // Method:   build
  /**
   ** Factory Method to create a proper DSML reader belonging to the provided
   ** version.
   **
   ** @param  format             the format of the file to parse either
   **                            <ul>
   **                              <li> {@link Format#DSML2}
   **                              <li> {@link Format#DSML1}
   **                              <li> {@link Format#LDIF}
   **                              <li> {@link Format#JSON}
   **                              </ul>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  file               the abstract path of the DSML file to parse.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    a <code>LDAPReader</code> for the specified
   **                            <code>format</code> and <code>version</code>.
   **                            <br>
   **                            Possible object is <code>LDAPReader</code>.
   **
   ** @throws DirectoryException if an I/O error occurs
   */
  public static LDAPReader build(final String format, final File file)
    throws DirectoryException {

    switch (Format.from(format)) {
      case DSML1 : return DSMLReader.build(file, 1);
      case DSML2 : return DSMLReader.build(file, 2);
      case JSON  : return JSONReader.build(file);
      case LDIF  :
      default    : return LDIFReader.build(file);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (DirectoryFile)
  /**
   ** Closes the embedded stream, flushing it first.
   ** <p>
   ** Once the stream has been closed, further read() invocations will cause an
   ** IOException to be thrown.
   ** <p>
   ** Closing a previously closed stream has no effect.
   **
   ** @throws DirectoryException if an I/O error occurs.
   */
  @Override
  public void close()
    throws DirectoryException {

    try {
      this.stream.close();
    }
    catch (IOException e) {
      throw new DirectoryException("Ooops");
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
   ** @throws DirectoryException if an I/O error occurs.
   **
   ** @see    LDAPRecord
   */
  public abstract LDAPRecord nextRecord()
    throws DirectoryException;
}