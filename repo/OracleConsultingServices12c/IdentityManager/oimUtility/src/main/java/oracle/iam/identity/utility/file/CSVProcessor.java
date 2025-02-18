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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVProcessor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVProcessor.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.io.IOException;
import java.io.EOFException;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.SystemException;

////////////////////////////////////////////////////////////////////////////////
// final class CSVProcessor
// ~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** This is the superclass for the reading and writing data from and to CSV
 ** files.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVProcessor extends CSVOperation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final CSVReader reader;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>CSVProcessor</code>.
   ** <br>
   ** This is a <code>CSVProcessor</code> zhat cannot be used in conjunction to
   ** read a CSV file.
   **
   ** @param  descriptor         the {@link CSVDescriptor}s which contains the
   **                            attributes handled by this processor.
   */
  public CSVProcessor(final CSVDescriptor descriptor) {
    // ensure inheritance
    this(descriptor, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>CSVProcessor</code>
   **
   ** @param  descriptor         the {@link CSVDescriptor}s which contains the
   **                            attributes handled by this processor.
   ** @param  reader             the {@link CSVReader}s which provides the
   **                            content  handled by this processor.
   */
  public CSVProcessor(final CSVDescriptor descriptor, final CSVReader reader) {
    // ensure inheritance
    super(descriptor);

    // initialize instance attributes
    this.reader = reader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ready
  /**
   ** Tells whether the reading stream is ready to be read.
   ** <p>
   ** A buffered character stream is ready if the buffer is not empty, or if the
   ** underlying character stream is ready.
   **
   ** @return                    <code>true</code> if the underlying character
   **                            stream is ready; otherwise <code>false</code>.
   */
  public boolean ready() {
    try {
      return this.reader.ready();
    }
    catch (IOException e) {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   linesProceed
  /**
   ** Returns the numbers of lines proceed on the file.
   **
   ** @return                    the numbers of lines proceed on the file.
   */
  public final int linesProceed() {
    return this.reader.linesProceed();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Closes the file.
   */
  public void close() {
    if (this.reader != null)
      try {
        this.reader.close();
      }
      catch (IOException e) {
        error("close", CSVError.NOTCLOSEDOUTPUT, e.getLocalizedMessage());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateDescriptor
  /**
   ** Reads a record from a CSV file and interprete it as the header of the
   ** file.
   ** <br>
   ** For this purpose it is important that the provided reader is not used
   ** before this method is invoked. We assume the first line of a CSV file
   ** is the header.
   **
   ** @throws SystemException     if the header could not be read or an declared
   **                            {@link CSVAttribute} is missing in the record.
   */
  protected void validateDescriptor()
    throws SystemException {

    if (this.reader == null)
      throw new SystemException(SystemError.ATTRIBUTE_IS_NULL, "reader");

    validateDescriptor(this.reader);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAll
  /**
   ** Reads the complete file into a vector object and sorts it afterwards.
   **
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   ** @return                    a {@link CSVResultSet}.
   **
   ** @throws IOException        in case of any other IO error
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  public CSVResultSet fetchAll(final boolean applyTransformer)
    throws IOException
    ,      CSVException {

    CSVResultSet resultSet = new CSVResultSet(this);
    resultSet.fetch(applyTransformer);
    return resultSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEntity
  /**
   ** Creates an {@link Map} by visting each declared {@link CSVAttribute}
   ** and extracting the value from the Reader.
   ** <br>
   ** For this purpose it is important that the array MUST be the header of a
   ** CSV file.
   **
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @return                    a <code>Map</code> with the tagged value pair
   **                            for each attribute in the descriptor.
   **
   ** @throws EOFException       at end of file after all the fields have
   **                            been read.
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  public Map<String, Object> readEntity(final boolean applyTransformer)
    throws EOFException
    ,      CSVException {

    return readEntity(this.reader, applyTransformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEntity
  /**
   ** Creates an {@link Map} by visting each declared {@link CSVAttribute}
   ** and extracting the value from the Reader.
   ** <br>
   ** For this purpose it is important that the array MUST be the header of a
   ** CSV file.
   **
   ** @param  reader             the columns of a record of an CVS file.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @return                    a <code>Map</code> with the tagged value pair
   **                            for each attribute in the descriptor.
   ** @throws EOFException       at end of file after all the fields have
   **                            been read.
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  protected Map<String, Object> readEntity(final CSVReader reader, final boolean applyTransformer)
    throws EOFException
    ,      CSVException {

    //prevent bogus input
    if (reader == null)
      throw new CSVException(SystemError.ARGUMENT_IS_NULL, "reader");

    if (!this.validated())
      validateDescriptor(reader);

    // throws EOF if end of file reached --> time to close the file
    String[] record = reader.readRecord();

    // if we are here the CSV record was fetched; otherwise the reader has
    // thrown an EOFException and the array is not filled
    Map<String, Object> entity = new HashMap<String, Object>(this.descriptor().size());

    // check if we have a valid line
    // this check assumes that the returned array of fields has the same length
    // as the descriptor specifies
    if (record.length == 1)
      // seems to be we have an empty line
      return entity;

    // iterate over all attributes and fetch the appropriate values
    Iterator<CSVAttribute> i = this.descriptor().attributeIterator();
    while (i.hasNext()) {
      CSVAttribute attribute = i.next();
      if (applyTransformer) {
        String value = null;
        // at first apply the format conversion, this creates the
        // appropriate object instance
        value = attribute.convertExternal(record[attribute.index()]);
        // the object instance the fetched string was converted is now
        // passed to the transformer
        value = attribute.transformInbound(value);
        entity.put(attribute.name(), value);
      }
      else
        entity.put(attribute.name(), record[attribute.index()]);
    }

    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEntity
  /**
   ** Writes all declared {@link CSVAttribute}s as the columns of an CSV file.
   ** <br>
   ** For this purpose it is important that the array MUST be the header of a
   ** CSV file.
   **
   ** @param  writer             the <code>CSVWriter</code> which recieve this
   **                            line.
   ** @param  resultSet          the {@link CSVResultSet} providing the data
   **                            to write.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   ** @param  append             the passed {@link CSVRecord} should be appended
   **                            at the end of the file handled by the
   **                            specified {@link CSVWriter}.
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       if the {@link CSVWriter} is already closed.
   */
  public void writeEntity(final CSVWriter writer, final CSVResultSet resultSet, final boolean applyTransformer, final boolean append)
    throws IOException
    ,      CSVException {

    if (!append)
      this.descriptor().write(writer);

    resultSet.rewind();
    while(resultSet.next())
      resultSet.get().write(this, writer, applyTransformer);
  }
}