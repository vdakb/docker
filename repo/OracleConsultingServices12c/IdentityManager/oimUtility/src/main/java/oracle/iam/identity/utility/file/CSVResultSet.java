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

    File        :   CSVResultSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVResultSet.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.Map;

import java.io.EOFException;
import java.io.IOException;

////////////////////////////////////////////////////////////////////////////////
// final class CSVResultSet
// ~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** This class is an helper class to hold the content of a CSV file in a vector
 ** structure which is accessable from outside the class
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVResultSet {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the cursor used to access a record in the buffer */
  private transient int               cursor     = -1;

  /** the container for in memory processing */
  private transient Vector<CSVRecord> buffer     = null;

  /** the current record in the result set */
  private transient CSVRecord         record     = null;

  /** the processor used to read the CSV lines */
  private transient CSVProcessor      processor = null;

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
  public CSVResultSet(final CSVProcessor processor) {
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
   ** Returns the {@link CSVProcessor} of this <code>CSVResultSet</code>.
   **
   ** @return                    the {@link CSVProcessor} of this
   **                            <code>CSVResultSet</code>.
   */
  public final CSVProcessor processor() {
    return this.processor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descriptor
  /**
   ** Returns the {@link CSVDescriptor} of this <code>CSVResultSet</code>.
   **
   ** @return                    the {@link CSVDescriptor} of this
   **                            <code>CSVResultSet</code>.
   */
  public final CSVDescriptor descriptor() {
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
   ** {@link CSVKeyComparator}.
   ** <br>
   ** All elements in the buffer are <i>mutually comparable</i> using the
   ** {@link CSVKeyComparator}.
   */
  public void sort() {
    this.sort(new CSVKeyComparator());
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
  public void sort(final Comparator<CSVRecord> comparator) {
    Collections.sort(this.buffer, comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bof
  /**
   ** Whether the begin files has been reached.
   ** <br>
   ** A <code>CSVResultSet</code> cursor is initially positioned before the
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
   ** A <code>CSVResultSet</code> cursor is initially positioned before the
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
   ** A <code>CSVResultSet</code> cursor is initially positioned before the
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
   ** Retrieves the current row of this <code>CSVResultSet</code> object.
   **
   ** @return                    the column value.
   **
   */
  public CSVRecord get() {
    return this.record;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears the buffer of this <code>CSVResultSet</code> object.
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
   ** Add the {@link Map} to this <code>CSVResultSet</code> object.
   **
   ** @param  entity             the {@link Map} to add.
   **
   */
  public void add(final Map<String, Object> entity) {
    add(new CSVRecord(entity, processor.descriptor().identifier(entity), processor.descriptor().payload(entity)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the {@link CSVRecord} to this <code>CSVResultSet</code> object.
   **
   ** @param  record             the {@link CSVRecord} to add.
   **
   */
  public void add(final CSVRecord record) {
    if (buffer == null)
      buffer = new Vector<CSVRecord>(1000, 100);

    this.buffer.add(record);
    this.record = record;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes the current record form this <code>CSVResultSet</code> object.
   ** <br>
   ** After this operation the cursor will point to the next entry in the
   ** managed buffer.
   **
   ** @return                    the removed {@link CSVRecord}.
   */
  public CSVRecord remove() {
    CSVRecord tmp = this.record;
    next();
    this.buffer.remove(cursor);
    return tmp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getString
  /**
   ** Retrieves the value of the designated column in the current row of this
   ** <code>CSVResultSet</code> object as a <code>String</code> in the Java
   ** programming language.
   **
   ** @param  columnName         the CSV name of the column
   **
   ** @return                    the column value.
   **
   ** @throws CSVException       if an access error occurs
   */
  public String getString(final String columnName)
    throws CSVException {

    if (cursor == -1) {
      String[] arguments = {"getString", "fetch", columnName};
      throw new CSVException(CSVError.EXECUTION_FAILED, arguments);
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
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  public void fetch(final boolean applyTransformer)
    throws IOException
    ,      CSVException {

    try {
      if (buffer == null)
        buffer = new Vector<CSVRecord>(1000, 100);
      else
        buffer.clear();

      while(true) {
        Map<String, Object> entity = processor.readEntity(applyTransformer);
        if (entity.size() > 0)
          buffer.add(new CSVRecord(entity, processor.descriptor().identifier(entity), processor.descriptor().payload(entity)));
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
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  public void fetch(final int bulkSize, final boolean applyTransformer)
    throws EOFException
    ,      CSVException {

    try {
      if (buffer == null)
        buffer = new Vector<CSVRecord>(bulkSize, 100);
      else
        buffer.clear();

      for (int i = 0; i < bulkSize; i++) {
        final Map<String, Object> entity = processor.readEntity(applyTransformer);
        if (entity.size() > 0)
          buffer.add(new CSVRecord(entity, processor.descriptor().identifier(entity), processor.descriptor().payload(entity)));
      }
    }
    // thrown if end of file reached --> time to rewind
    catch (EOFException e) {
      rewind();
      throw e;
    }
  }
}