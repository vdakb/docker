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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryFileReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFileReader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import java.io.IOException;
import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.naming.Binding;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryFileReader
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** LDAP Data Interchange Format (LDIF) is a file format used to import
 ** directory data from an LDAP server and to describe a set of changes
 ** to be applied to data in a directory. This format is described in the
 ** Internet draft
 ** <a href="ftp://ftp.ietf.org/internet-drafts/draft-good-ldap-ldif-00.txt" target="_blank">The LDAP Data Interchange Format (LDIF) - Technical Specification</a>.
 ** <p>
 ** This class implements an LDIF or DSML input operations.
 */
public abstract class DirectoryFileReader extends DirectoryFile {

  /////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String   STANDARD = "System.in";

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
   ** Constructs an <code>DirectoryFileReader</code> object to parse the LDIF or
   ** DSML from stdin.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public DirectoryFileReader()
    throws DirectoryException {

    // ensure inheritance
    this(STANDARD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  file               the abstract path of the LDIF or DSML file to
   **                            parse.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public DirectoryFileReader(final File file)
    throws DirectoryException {

    // ensure inheritance
    this(file, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  file               the abstract path of the LDIF or DSML file to
   **                            parse.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public DirectoryFileReader(final File file, final char separator)
    throws DirectoryException {

    // ensure inheritance
    super(file, separator);

    // initialize instance
    try {
      this.stream = new DataInputStream(new FileInputStream(this.handle));
    }
    catch (IOException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  handle             the name of the LDIF or DSML file to parse.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public DirectoryFileReader(final String handle)
    throws DirectoryException {

    // ensure inheritance
    this(handle, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  handle             the name of the LDIF or DSML file to parse.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public DirectoryFileReader(final String handle, final char separator)
    throws DirectoryException {

    // ensure inheritance
    super(handle, separator);

    // initialize instance
    try {
      this.stream = new DataInputStream(new FileInputStream(handle));
    }
    catch (IOException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  stream             the {@link DataInputStream} of the LDIF or DSML
   **                            file to parse.
   */
  public DirectoryFileReader(final DataInputStream stream) {
    // ensure inheritance
    this(stream, SEPARATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryFileReader</code> object to parse the LDIF or
   ** DSML from a specified file.
   **
   ** @param  stream             the {@link DataInputStream} of the LDIF or DSML
   **                            file to parse.
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   */
  public DirectoryFileReader(final DataInputStream stream, final char separator) {
    // ensure inheritance
    super(separator);

    // initialize instance
    this.stream = stream;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (DirectoryFileReader)
  /**
   ** Closes the embedded stream, flushing it first.
   ** <p>
   ** Once the stream has been closed, further read() invocations will cause an
   ** IOException to be thrown.
   ** <p>
   ** Closing a previously closed stream has no effect.
   **
   ** @throws DirectoryException if an I/O error occurs
   */
  @Override
  public void close()
    throws DirectoryException {

    if (this.stream == null)
      new DirectoryException(DirectoryError.INSTANCE_ATTRIBUTE_IS_NULL, "stream");

    try {
      this.stream.close();
    }
    catch (IOException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextRecord
  /**
   ** Returns the next record in the LDIF data.
   ** <p>
   ** You can call this method repeatedly to iterate through all records in the
   ** LDIF data.
   **
   ** @return                    the next record as a {@link Binding} object
   **                            or <code>null</code> if there are no more
   **                            records.
   **
   ** @throws DirectoryException if an I/O error occurs
   **
   ** @see    Binding
   */
  public abstract Binding nextRecord()
    throws DirectoryException;
}