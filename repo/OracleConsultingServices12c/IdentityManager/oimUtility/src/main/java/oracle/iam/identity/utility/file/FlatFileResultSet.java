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

    File        :   FlatFileResultSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlatFileResultSet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;
import java.util.Vector;
import java.util.Comparator;
import java.util.Collections;

import java.io.EOFException;
import java.io.IOException;

import java.io.Serializable;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileResultSet
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** This class is an helper class to hold the content of a text file in a vector
 ** structure which is accessable from outside the class
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public class FlatFileResultSet implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1187698389212248323")
  private static final long                serialVersionUID = 2037816877450097198L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the cursor used to access a record in the buffer */
  private transient int                    cursor           = -1;

  /** the container for in memory processing */
  private transient Vector<FlatFileRecord> buffer           = null;

  /** the current record in the result set */
  private transient FlatFileRecord         record           = null;

  /** the processor used to read the text lines */
  private transient FlatFileProcessor      processor        = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create an instance without doing more.
   **
   ** @param  processor          the processor to read the file
   */
  public FlatFileResultSet(final FlatFileProcessor processor) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.processor = processor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processor
  /**
   ** Returns the {@link FlatFileProcessor} of this <code>FlatFileResultSet</code>.
   **
   ** @return                    the {@link FlatFileProcessor} of this
   **                            <code>FlatFileResultSet</code>.
   */
  public final FlatFileProcessor processor() {
    return this.processor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descriptor
  /**
   ** Returns the {@link FlatFileDescriptor} of this <code>FlatFileResultSet</code>.
   **
   ** @return                    the {@link FlatFileDescriptor} of this
   **                            <code>FlatFileResultSet</code>.
   */
  public final FlatFileDescriptor descriptor() {
    return this.processor.descriptor();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   linesProcced
  /**
   ** Returns the numbers of lines in the file.
   **
   ** @return int
   */
  public int linesProcced() {
    return this.processor.linesProceed();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rewind
  /**
   ** Rewind the buffer position to the first record in the buffer.
   */
  public void rewind() {
    this.cursor = -1;
    this.record = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of components in this container.
   **
   ** @return                    the number of components in this container.
   */
   public int size() {
     return buffer.size();
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sort
  /**
   ** Sorts the internal buffer according to the order induced by a
   ** {@link FlatFileKeyComparator}.
   ** <br>
   ** All elements in the buffer are <i>mutually comparable</i> using the
   ** {@link FlatFileKeyComparator}.
   */
  public void sort() {
    this.sort(new FlatFileKeyComparator());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sort
  /**
   ** Sorts the internal buffer according to the order induced by a the specified
   ** {@link Comparator}.
   ** <br>
   ** All elements in the buffer are <i>mutually comparable</i> using the
   ** {@link Comparator}.
   **
   ** @param  comparator         the {@link Comparator} to sort the internal
   **                            buffer.
   */
  public void sort(final Comparator<FlatFileRecord> comparator) {
    Collections.sort(this.buffer, comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bof
  /**
   ** Whether the begin files has been reached.
   ** <br>
   ** A <code>FlatFileResultSet</code> cursor is initially positioned before the
   ** first row; the first call to the method <code>next</code> makes the first
   ** row the current row; the second call makes the second row the current row,
   ** and so on.
   **
   ** @return                    <code>true</code> if the new current row is
   **                            valid; <code>false</code> if there are no more
   **                            rows
   */
  public boolean bof() {
    return (cursor == -1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eof
  /**
   ** Whether the end of the files has been reached.
   ** <br>
   ** A <code>FlatFileResultSet</code> cursor is initially positioned before the
   ** first row; the first call to the method <code>next</code> makes the first
   ** row the current row; the second call makes the second row the current row,
   ** and so on.
   **
   ** @return                    <code>true</code> if the new current row is
   **                            valid; <code>false</code> if there are no more
   **                            rows
   */
  public boolean eof() {
    int size = buffer.size() - 1;
    return (size < 0 || cursor == size);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next
  /**
   ** Moves the cursor down one row from its current position.
   ** <br>
   ** A <code>FlatFileResultSet</code> cursor is initially positioned before the
   ** first row; the first call to the method <code>next</code> makes the first
   ** row the current row; the second call makes the second row the current row,
   ** and so on.
   **
   ** @return                    <code>true</code> if the new current row is
   **                            valid; <code>false</code> if there are no more
   **                            rows
   */
  public boolean next() {
    if (eof())
      return false;

    this.record = buffer.get(++cursor);
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Retrieves the current row of this <code>FlatFileResultSet</code> object.
   **
   ** @return                    the column value.
   **
   */
  public FlatFileRecord get() {
    return this.record;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears the buffer of this <code>FlatFileResultSet</code> object.
   */
  public void clear() {
    if (this.buffer != null) {
      rewind();
      this.buffer.clear();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the {@link Map} to this <code>FlatFileResultSet</code> object.
   **
   ** @param  entity             the {@link Map} to add.
   **
   */
  public void add(final Map<String, Object> entity) {
    add(new FlatFileRecord(entity, processor.descriptor().identifier(entity), processor.descriptor().payload(entity)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the {@link FlatFileRecord} to this <code>FlatFileResultSet</code> object.
   **
   ** @param  record             the {@link FlatFileRecord} to add.
   **
   */
  public void add(final FlatFileRecord record) {
    if (buffer == null)
      buffer = new Vector<FlatFileRecord>(1000, 100);

    this.buffer.add(record);
    this.record = record;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes the current record form this <code>FlatFileResultSet</code> object.
   ** <br>
   ** After this operation the cursor will point to the next entry in the
   ** managed buffer.
   **
   ** @return                    the removed {@link FlatFileRecord}.
   */
  public FlatFileRecord remove() {
    FlatFileRecord tmp = this.record;
    next();
    this.buffer.remove(cursor);
    return tmp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getString
  /**
   ** Retrieves the value of the designated column in the current row of this
   ** <code>FlatFileResultSet</code> object as a <code>String</code> in the Java
   ** programming language.
   **
   ** @param  columnName         the FlatFile name of the column
   **
   ** @return                    the column value.
   **
   ** @throws FlatFileException       if an access error occurs
   */
  public String getString(final String columnName)
    throws FlatFileException {

    if (cursor == -1) {
      String[] arguments = {"getString", "fetch", columnName};
      throw new FlatFileException(FlatFileError.EXECUTION_FAILED, arguments);
    }

    return record.getString(columnName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch
  /**
   ** Reads the complete file into a vector object.
   **
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @throws IOException        in case of any other IO error
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  public void fetch(final boolean applyTransformer)
    throws IOException
    ,      TaskException {

    try {
      if (this.buffer == null)
        this.buffer = new Vector<FlatFileRecord>(1000, 100);
      else
        this.buffer.clear();

      while(true) {
        Map<String, Object> entity = this.processor.readEntity(applyTransformer);
        if (entity.size() > 0)
          this.buffer.add(new FlatFileRecord(entity, this.processor.descriptor().identifier(entity), this.processor.descriptor().payload(entity)));
      }
    }
    // thrown if end of file reached --> time to rewind
    catch (EOFException e) {
      rewind();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch
  /**
   ** Reads the complete file into a vector object.
   **
   ** @param  bulkSize           the size of a bulk to handle.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @throws EOFException       the end of file is reached
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  public void fetch(final int bulkSize, final boolean applyTransformer)
    throws EOFException
    ,      TaskException {

    try {
      if (this.buffer == null)
        this.buffer = new Vector<FlatFileRecord>(bulkSize, 100);
      else
        this.buffer.clear();

      for (int i = 0; i < bulkSize; i++) {
        final Map<String, Object> entity = this.processor.readEntity(applyTransformer);
        if (entity.size() > 0)
          this.buffer.add(new FlatFileRecord(entity, this.processor.descriptor().identifier(entity), this.processor.descriptor().payload(entity)));
      }
    }
    // thrown if end of file reached --> time to rewind
    catch (EOFException e) {
      rewind();
      throw e;
    }
  }
}