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

    File        :   JSONReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    JSONReader.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

////////////////////////////////////////////////////////////////////////////////
// class JSONReader
// ~~~~~ ~~~~~~~~~~
/**
 * Reads, parses and converts JSON into JNDI <code>SearchResult</code>s.
 * <p>
 * Reads JSON, JavaScript Object Notation, from files, streams and readers, and
 * returns JNDI <code>SearchResult</code>s.
 *
 * @see JSONWriter
 * @see LDAPReader
 *
 * @author dieter.steding@oracle.com
 * @version 12.2.1.3.42.60.102
 * @since 12.2.1.3.42.60.102
 */
public class JSONReader extends LDAPReader {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONReader</code> object to read the JSON from the
   ** specified {@link File} path.
   **
   ** @param  file               the {@link File} path of the JSON file to read.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public JSONReader(final File file)
    throws DirectoryException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONReader</code> object to read the JSON from the
   ** specified file.
   **
   ** @param  file               the name of the JSON file to read.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public JSONReader(final String file)
    throws DirectoryException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONReader</code> object to read entries from a
   ** stream as JSON.
   **
   ** @param  stream             input stream providing the JSON data.
   */
  public JSONReader(final FileInputStream stream) {
    // ensure inheritance
    this(new DataInputStream(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>JSONReader</code> object to read the JSON data from an
   ** input stream.
   **
   ** @param  stream             input stream providing the JSON data.
   */
  public JSONReader(final DataInputStream stream) {
    // ensure inheritance
    super(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextRecord (DirectoryFileReader)
  /**
   ** Returns the next record in the LDIF data.
   ** <p>
   ** You can call this method repeatedly to iterate through all records in the
   ** LDIF data.
   **
   ** @return                    the next record as a {@link LDAPRecord} object
   **                            or <code>null</code> if there are no more
   **                            records.
   **
   ** @throws DirectoryException if an I/O error occurs.
   **
   ** @see    LDAPRecord
   */
  @Override
  public LDAPRecord nextRecord()
    throws DirectoryException {

    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory Method to create a proper JSON reader.
   **
   ** @param  file               the abstract path of the JSON file to parse.
   **
   ** @return                    an instance of {@link JSONReader}.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public static JSONReader build(final File file)
    throws DirectoryException {

    return new JSONReader(file);
  }
}