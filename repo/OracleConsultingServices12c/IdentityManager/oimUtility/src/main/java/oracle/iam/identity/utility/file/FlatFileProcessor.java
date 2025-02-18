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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Text Stream Facilities

    File        :   FlatFileProcessor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlatFileProcessor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;
import java.util.Iterator;
import java.util.LinkedHashMap;

import java.io.IOException;
import java.io.EOFException;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class FlatFileProcessor
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A processor that checks a flat file against {@link FlatFileDescriptor}
 ** <code>XML Schema</code>.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class FlatFileProcessor extends FlatFileOperation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final FlatFileReader reader;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>FlatFileProcessor</code>.
   ** <br>
   ** This is a <code>FlatFileProcessor</code> zhat cannot be used in
   ** conjunction to read a flat file.
   **
   ** @param  descriptor         the {@link FlatFileDescriptor}s which contains
   **                            the attributes handled by this processor.
   */
  public FlatFileProcessor(final FlatFileDescriptor descriptor) {
    // ensure inheritance
    this(descriptor, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>FlatFileProcessor</code>
   **
   ** @param  descriptor         the {@link FlatFileDescriptor}s which contains
   **                            the attributes handled by this processor.
   ** @param  reader             the {@link FlatFileReader}s which provides the
   **                            content  handled by this processor.
   */
  public FlatFileProcessor(final FlatFileDescriptor descriptor, final FlatFileReader reader) {
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
        error("close", FlatFileError.NOTCLOSEDOUTPUT, e.getLocalizedMessage());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAll
  /**
   ** Reads the complete file into a vector object and sorts it afterwards.
   **
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   ** @return                    a {@link FlatFileResultSet}.
   **
   ** @throws IOException        in case of any other IO error
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  public FlatFileResultSet fetchAll(final boolean applyTransformer)
    throws IOException
    ,      TaskException {

    final FlatFileResultSet resultSet = new FlatFileResultSet(this);
    resultSet.fetch(applyTransformer);
    return resultSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEntity
  /**
   ** Creates an {@link Map} by visting each declared {@link FlatFileAttribute}
   ** and extracting the value from the Reader.
   ** <br>
   ** For this purpose it is important that the array MUST be the header of a
   ** FlatFile file.
   **
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @return                    a <code>Map</code> with the tagged value pair
   **                            for each attribute in the descriptor.
   **
   ** @throws EOFException       at end of file after all the fields have
   **                            been read.
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  public Map<String, Object> readEntity(final boolean applyTransformer)
    throws EOFException
    ,      TaskException {

    return readEntity(this.reader, applyTransformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEntity
  /**
   ** Creates an {@link Map} by visting each declared {@link FlatFileAttribute}
   ** and extracting the value from the Reader.
   ** <br>
   ** For this purpose it is important that the array MUST be the header of a
   ** FlatFile file.
   **
   ** @param  reader             the columns of a record of an flat file.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @return                    a <code>Map</code> with the tagged value pair
   **                            for each attribute in the descriptor.
   ** @throws EOFException       at end of file after all the fields have
   **                            been read.
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  protected Map<String, Object> readEntity(final FlatFileReader reader, final boolean applyTransformer)
    throws EOFException
    ,      TaskException {

    //prevent bogus input
    if (reader == null)
      throw TaskException.argumentIsNull("reader");

    // throws EOF if end of file reached --> time to close the file
    String record = reader.readRecord();

    // if we are here the FlatFile record was fetched; otherwise the reader has
    // thrown an EOFException and the array is not filled
    final Map<String, Object> entity = new LinkedHashMap<String, Object>(this.descriptor().size());

    // iterate over all attributes and fetch the appropriate values
    final Iterator<FlatFileAttribute> i = this.descriptor().attributeIterator();
    while (i.hasNext()) {
      FlatFileAttribute attribute = i.next();
      if (applyTransformer) {
        String value = null;
        // at first apply the format conversion, this creates the
        // appropriate object instance
        value = attribute.convertExternal(attribute.read(record));
        // the object instance the fetched string was converted is now
        // passed to the transformer
        value = attribute.transformInbound(value);
        entity.put(attribute.name(), value);
      }
      else
        entity.put(attribute.name(), attribute.read(record));
    }

    return entity;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEntity
  /**
   ** Writes all declared {@link FlatFileAttribute}s as the columns of an FlatFile file.
   ** <br>
   ** For this purpose it is important that the array MUST be the header of a
   ** FlatFile file.
   **
   ** @param  writer             the <code>FlatFileWriter</code> which recieve this
   **                            line.
   ** @param  resultSet          the {@link FlatFileResultSet} providing the data
   **                            to write.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   ** @param  append             the passed {@link FlatFileRecord} should be appended
   **                            at the end of the file handled by the
   **                            specified {@link FlatFileWriter}.
   **
   ** @throws IOException        if the {@link FlatFileWriter} is already closed.
   ** @throws TaskException      if the {@link FlatFileWriter} is already closed.
   */
  public void writeEntity(final FlatFileWriter writer, final FlatFileResultSet resultSet, final boolean applyTransformer, final boolean append)
    throws IOException
    ,      TaskException {

    if (!append)
      this.descriptor().write(writer);

    resultSet.rewind();
    while(resultSet.next())
      resultSet.get().write(this, writer, applyTransformer);
  }
}