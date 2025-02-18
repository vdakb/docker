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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   FlatFileComparator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FlatFileComparator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;

import java.io.File;
import java.io.EOFException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileComparator
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Flat File Comparator Utility with changes made in order to be able to take
 ** randomly sorted input files as long as they have a key fields.
 ** <br>
 ** Current separator is the semicolon ; char
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public final class FlatFileComparator extends FlatFileOperation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the file encoding of the temporary files to create */
  private final String encoding;

  /** counter to kepp track the amount of deletions that are detected */
  private int          deletions;

  /** counter to kepp track the amount of additions that are detected */
  private int          additions;

  /** counter to kepp track the amount of modifications that are detected */
  private int          modifcations;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FlatFileComparator</code> that will write the detected
   ** modifications to the file with the specified name.
   **
   ** @param  descriptor         the {@link FlatFileDescriptor} used to extract the
   **                            content form the text files.
   ** @param  encoding           the file encoding of the temporary files to
   **                            create.
   */
  public FlatFileComparator(final FlatFileDescriptor descriptor, final String encoding) {
    // ensure inheritance
    super(descriptor);

    // initialize instance attributes
    this.encoding = encoding;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoding
  /**
   ** Returns the file encoding of this <code>FlatFileOperation</code>.
   **
   ** @return                    the file encoding of this
   **                            <code>FlatFileOperation</code>.
   */
  public final String encoding() {
    return this.encoding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changes
  /**
   ** Returns <code>true</code> if there are any differences detected between
   ** the two files.
   **
   ** @return                    <code>true</code> if there are any differences;
   **                            otherwise <code>false</code>.
   */
  public boolean changes() {
    return ((this.additions > 0) || (this.modifcations > 0) || (this.deletions > 0));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   additions
  /**
   ** Returns the number of additions detected.
   **
   ** @return                    the number of additions detected.
   */
  public int additions() {
    return this.additions;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares oldFile with newFile and writes Differences to iFile
   **
   ** @param  oldFile            the name of the file containing the old data.
   ** @param  newFile            the name of the file containing the new data.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the file will not be created.
   **
   ** @throws IOException        if the {@link FlatFileWriter} cannot be closed.
   ** @throws TaskException      some problem in migration the writer to a
   **                            <code>PrintWriter</code>.
   */
  public void compare(final FlatFileReader oldFile, final FlatFileReader newFile, final FlatFileWriter writer)
    throws IOException
    ,      TaskException {

    final FlatFileProcessor oldProcessor = new FlatFileProcessor(this.descriptor(), oldFile);
    final FlatFileProcessor newProcessor = new FlatFileProcessor(this.descriptor(), newFile);

    compare(oldProcessor, newProcessor, writer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares oldData with newData and writes differences to file
   **
   ** @param  oldData            the processor providing the old data.
   ** @param  newData            the processor providing the new data.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the file will not be created.
   **
   ** @throws IOException        if the {@link FlatFileWriter} cannot be closed.
   ** @throws TaskException      some problem in migration the writer to a
   **                            <code>PrintWriter</code>.
   */
  protected void compare(final FlatFileProcessor oldData, final FlatFileProcessor newData, final FlatFileWriter writer)
    throws IOException
    ,      TaskException {

    compare(new FlatFileResultSet(oldData), new FlatFileResultSet(newData), writer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares oldFile with newFile and writes Differences to iFile
   **
   ** @param  oldData            the name of the file containing the old data.
   ** @param  newData            the name of the file containing the new data.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the file will not be created.
   **
   ** @throws IOException        if the {@link FlatFileWriter} is already closed.
   ** @throws TaskException      some problem in migration the writer to a
   **                            <code>PrintWriter</code>.
   */
  private void compare(final FlatFileResultSet oldData, final FlatFileResultSet newData, final FlatFileWriter writer)
    throws IOException
    ,      TaskException {

    final String method = "compare";

    try {
      info(FlatFileMessage.LOADING);

      // Read all lines from the existing file into a buffer object and sort
      // them lexicographically
      oldData.fetch(false);
      String[] oldFile = { "old", String.valueOf(oldData.linesProcced()) };
      debug(method, FlatFileMessage.LINECOUNT, oldFile);

      // Read all lines from the new file into a buffer object and sort them
      // lexicographically
      newData.fetch(false);
      String[] newFile = { "new", String.valueOf(newData.linesProcced()) };
      debug(method, FlatFileMessage.LINECOUNT, newFile);

      difference(oldData, newData, writer);
    }
    finally {
      // Clean up
      writer.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares oldFile with newFile and writes Differences to iFile
   **
   ** @param  oldFile            the name of the file containing the old data.
   ** @param  newFile            the name of the file containing the new data.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the file will not be created.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   ** @param  workingFolder      the location in the file system where the
   **                            temporary files will be created.
   **
   ** @throws IOException        if the {@link FlatFileWriter} cannot be closed.
   ** @throws TaskException      some problem in migration the writer to a
   **                            <code>PrintWriter</code>.
   */
  public void compare(final FlatFileReader oldFile, final FlatFileReader newFile, final FlatFileWriter writer, final boolean applyTransformer, final File workingFolder)
    throws IOException
    ,      TaskException {

    compare(new FlatFileProcessor(this.descriptor(), oldFile), new FlatFileProcessor(this.descriptor(), newFile), writer, applyTransformer, workingFolder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares oldData with newData and writes differences to file
   **
   ** @param  oldData            the processor providing the old data.
   ** @param  newData            the processor providing the new data.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the file will not be created.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   ** @param  workingFolder      the location in the file system where the
   **                            temporary files will be created.
   **
   ** @throws IOException        if the {@link FlatFileWriter} cannot be closed.
   ** @throws TaskException      some problem in migration the writer to a
   **                            <code>PrintWriter</code>.
   */
  protected void compare(final FlatFileProcessor oldData, final FlatFileProcessor newData, final FlatFileWriter writer, boolean applyTransformer, File workingFolder)
    throws IOException
    ,      TaskException {

    try {
      difference(oldData, newData, writer, applyTransformer, workingFolder);
    }
    finally {
      // Clean up
      writer.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   difference
  /**
   ** Writes the detected differences to the file and extending each line with a
   ** transactional code thereby
   ** <ul>
   **   <li>CRE means the line is new
   **   <li>UPD means the payload of the line is modified
   **   <li>DEL means the key of the line is not conatined in the new file but
   **           in old file
   ** </ul>
   ** The detected differences is written to the <code>FlatFileWriter</code> which is
   ** passed to the constructor of this instance.
   **
   ** @param  oldData            the name of the file containing the old data.
   ** @param  newData            the name of the file containing the new data.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the file will not be created.
   **
   ** @throws IOException        if the descriptor cannot be written to
   **                            {@link FlatFileWriter}.
   ** @throws TaskException      if the detected lines connaot be written to the
   **                            writer.
   */
  private void difference(final FlatFileResultSet oldData, final FlatFileResultSet newData, final FlatFileWriter writer)
    throws IOException
    ,      TaskException {

    final FlatFileProcessor processor = new FlatFileProcessor(this.descriptor());
    processor.logger(this.logger());

    // sort both buffer.
    oldData.sort();
    newData.sort();

    FlatFileRecord oldRecord = null;
    FlatFileRecord newRecord = null;

    // reset the counter
    this.additions    = 0;
    this.modifcations = 0;
    this.deletions    = 0;

    // prepare the working file to indicate the transaction status of a data set
    info(FlatFileMessage.PROCESSING);
    // loop through the data sets of each result set until one of them has
    // reached the end of buffer
    while (oldData.next()) {
      // get a data set in the old file to compare with the data set in new
      // file
      oldRecord = oldData.get();

      int compareKey = -1;
      // position the new file on the next data set
      if (newData.next()) {
        // loop until we need to examine the next line in old file
        do {
          // get a data set in the new file to compare with the data set in old
          // file
          newRecord   = newData.get();
          // compare key parts of the data sets
          compareKey  = oldRecord.compareKey(newRecord);
          // there is an additional data set in the new file that is ordered in
          // before matching old data set with a different key
          if (compareKey > 0) {
            // handle the new data set
            handleAdd(processor, writer, newRecord);
            // get the next data set in the new file to compare with the data
            // set in old file
            if (!newData.next())
              break;
          }
        } while (compareKey > 0);
        // loop until we need to examine the next line in new file
        while (compareKey < 0 && !oldData.eof()) {
          // handle the old data set
          handleDel(processor, writer, oldRecord);

          // get the next data set in the new file to compare with the data set
          // in old file
          oldData.next();
          oldRecord = oldData.get();
          // compare key parts of the data sets to evaluate the loop exit condition
          compareKey = oldRecord.compareKey(newRecord);
        }

        // the key parts are identically, but the end of the new file is not
        // reached yet
        if (compareKey == 0) {
          // is there a data set with the same key but different payload ?
          // handle the appropriate call back whether data sets are equal
          if (oldRecord.comparePayload(newRecord))
            handleEqual(processor, writer, newRecord);
          else
            handleMod(processor, writer, newRecord);
        }
      }
      else {
        // loop until we reach the end of old file
        while (!oldData.eof()) {
          // handle the appropriate call back for old data set
          handleDel(processor, writer, oldRecord);

          // get the next data set in the new file to compare with the data set
          // in old file
          oldData.next();
          oldRecord = oldData.get();
        }
        // there still one data set left due to exit condition of loop
        // handle the appropriate call back for old data set
        handleDel(processor, writer, oldRecord);
      }
    }

    // if there may be lines left in the new files
    // do to the lexicographical sort of the ressultset they are greater as the
    // last value in the old file and so they mut be new
    while (newData.next()) {
      newRecord = newData.get();
      handleAdd(processor, writer, newRecord);
    }

    // print status
    if (changes())
      info(FlatFileMessage.COMPLETED);
    else
      info(FlatFileMessage.IDENTICALLY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   difference
  /**
   ** Writes the detected differences to the file and extending each line with a
   ** transactional code thereby:
   ** <ul>
   **   <li>CRE means the line is new
   **   <li>UPD means the payload of the line is modified
   **   <li>DEL means the key of the line is not conatined in the new file but
   **           in old file
   ** </ul>
   ** The detected differences is written to the <code>FlatFileWriter</code> which is
   ** passed to the constructor of this instance.
   **
   ** @param  oldData            the processor providing the old data.
   ** @param  newData            the processor providing the new data.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the file will not be created.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   ** @param  workingFolder      the location in the file system where the
   **                            temporary files will be created.
   **
   ** @throws IOException        if the descriptor cannot be written to
   **                            {@link FlatFileWriter} or it's not possible to read
   **                            an entity from the files.
   ** @throws TaskException      if the detected lines connaot be written to the
   **                            writer.
   */
  protected void difference(final FlatFileProcessor oldData, final FlatFileProcessor newData, final FlatFileWriter writer, boolean applyTransformer, final File workingFolder)
    throws IOException
    ,      TaskException {

    final String       method    = "difference";
    final FlatFileProcessor processor = new FlatFileProcessor(this.descriptor());
    processor.logger(this.logger());

    final FlatFileSorting sorting = new FlatFileSorting(this.descriptor(), this.encoding);
    sorting.logger(this.logger());

    File oldSorted = sorting.sort(oldData, workingFolder);
    File newSorted = sorting.sort(newData, workingFolder);

    // reset the counter
    this.additions    = 0;
    this.modifcations = 0;
    this.deletions    = 0;

    // prepare the working file to indicate the transaction status of a data set
    info(FlatFileMessage.PROCESSING);

    FlatFileProcessor oldProcessor  = null;
    FlatFileProcessor newProcessor  = null;
    try {
      oldProcessor  = new FlatFileProcessor(this.descriptor(), new FlatFileReader(new InputStreamReader(new FileInputStream(oldSorted), this.encoding)));
      oldProcessor.logger(this.logger());
      newProcessor  = new FlatFileProcessor(this.descriptor(), new FlatFileReader(new InputStreamReader(new FileInputStream(newSorted), this.encoding)));
      newProcessor.logger(this.logger());
    }
    catch (IOException e) {
      if (oldProcessor != null)
        oldProcessor.close();
      if (newProcessor != null)
        newProcessor.close();
      // delete workfiles
      oldSorted.delete();
      newSorted.delete();
      throw TaskException.general(e);
    }

    FlatFileRecord oldRecord = null;
    // read the first line from the old file
    try {
      Map<String, Object> entity = oldProcessor.readEntity(applyTransformer);
      oldRecord  = new FlatFileRecord(entity, oldProcessor.descriptor().identifier(entity), oldProcessor.descriptor().payload(entity));
    }
    catch (EOFException e) {
      oldRecord = null;
    }

    FlatFileRecord newRecord = null;
    // read the first line from the new file
    try {
      final Map<String, Object> entity = newProcessor.readEntity(applyTransformer);
      newRecord   = new FlatFileRecord(entity, newProcessor.descriptor().identifier(entity), newProcessor.descriptor().payload(entity));
    }
    catch (EOFException e) {
      newRecord = null;
    }

    // loop through the data sets of each result set until one of them has
    // reached the end of buffer
    while ((oldRecord != null && newRecord != null)) {
      // compare key parts of the data sets
      int compareKey = oldRecord.compareKey(newRecord);
      // there are additional data set in the new file that is ordered in
      // before matching old data set with a different key
      if (compareKey > 0) {
        // handle the new data set
        handleAdd(processor, writer, newRecord);
        // get the next data set in the new file to compare with the data
        // set in old file in the next step of iteration
        try {
          final Map<String, Object> entity = newProcessor.readEntity(applyTransformer);
          newRecord  = new FlatFileRecord(entity, newProcessor.descriptor().identifier(entity), newProcessor.descriptor().payload(entity));
        }
        catch (EOFException e) {
           newRecord = null;
        }
      }
      // check if we have a record in the oldfile which is no lonegr part of
      // the new file
      if (compareKey < 0) {
        // handle the old data set
        handleDel(processor, writer, oldRecord);
        // get the next data set in the new file to compare with the data
        // set in old file in the next step of iteration
        try {
          final Map<String, Object> entity = oldProcessor.readEntity(applyTransformer);
          oldRecord  = new FlatFileRecord(entity, oldProcessor.descriptor().identifier(entity), oldProcessor.descriptor().payload(entity));
        }
        catch (EOFException e) {
           oldRecord = null;
        }
      }
      // if the keys are identically check the payload to detect the
      // modifications in the new file
      if (compareKey == 0) {
        // is there a data set with the same key but different payload ?
        // handle the appropriate call back whether data sets are equal
        if (oldRecord.comparePayload(newRecord))
          handleEqual(processor, writer, newRecord);
        else
          handleMod(processor, writer, newRecord);

        // get the next data set in the old file to compare with the data
        // set in new file in the next step of iteration
        try {
          final Map<String, Object> entity = oldProcessor.readEntity(applyTransformer);
          oldRecord  = new FlatFileRecord(entity, oldProcessor.descriptor().identifier(entity), oldProcessor.descriptor().payload(entity));
        }
        catch (EOFException e) {
          oldRecord = null;
        }

        // get the next data set in the new file to compare with the data
        // set in old file in the next step of iteration
        try {
          final Map<String, Object> entity = newProcessor.readEntity(applyTransformer);
          newRecord  = new FlatFileRecord(entity, newProcessor.descriptor().identifier(entity), newProcessor.descriptor().payload(entity));
        }
        catch (EOFException e) {
          newRecord = null;
        }
      }
    }

    if (newRecord == null && oldRecord != null) {
      // there still one data set left due to exit condition of loop
      // handle the appropriate call back for old data set
      handleDel(processor, writer, oldRecord);
      // loop until we reach the end of old file
      while (oldRecord != null) {
        // get the next data set in the old file to compare with the data
        // set in new file
        try {
          final Map<String, Object> entity = oldProcessor.readEntity(applyTransformer);
          oldRecord  = new FlatFileRecord(entity, oldProcessor.descriptor().identifier(entity), oldProcessor.descriptor().payload(entity));
          handleDel(processor, writer, oldRecord);
        }
        catch (EOFException e) {
          oldRecord = null;
        }
      }
    }

    if (newRecord != null && oldRecord == null) {
      // there still one data set left due to exit condition of loop
      // handle the appropriate call back for old data set
      handleAdd(processor, writer, newRecord);
      // loop until we reach the end of old file
      while (newRecord != null) {
        // get the next data set in the new file
        try {
          final Map<String, Object> entity = newProcessor.readEntity(applyTransformer);
          newRecord  = new FlatFileRecord(entity, newProcessor.descriptor().identifier(entity), newProcessor.descriptor().payload(entity));
          handleAdd(processor, writer, newRecord);
        }
        catch (EOFException e) {
           newRecord = null;
        }
      }
    }
    // close the processor regardless if they reached already the end of file or
    // not; this ensures that we can safly delete the temporary files in the
    // file system
    oldProcessor.close();
    newProcessor.close();

    String[] oldFile = { "old", String.valueOf(oldData.linesProceed()) };
    debug(method, FlatFileMessage.LINECOUNT, oldFile);
    // delete workfile
    oldSorted.delete();

    String[] newFile = { "new", String.valueOf(newData.linesProceed()) };
    debug(method, FlatFileMessage.LINECOUNT, newFile);
    // delete workfile
    newSorted.delete();

    // print status
    if (changes())
      info(FlatFileMessage.COMPLETED);
    else
      info(FlatFileMessage.IDENTICALLY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleEqual
  /**
   ** Handles the specified FlatFile if this data set is identically in the old and
   ** new file.
   ** <br>
   ** Normaly do nothing.
   **
   ** @param  processor          the  {@link FlatFileProcessor} to transfer the
   **                            data from the specified {@link FlatFileRecord}
   **                            <code>record</code> the specified
   **                            {@link FlatFileWriter} <code>writer</code>.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the data set will not be
   **                            created.
   ** @param  record             the data set to handle.
   **
   ** @throws IOException        if the {@link FlatFileWriter} is already closed.
   ** @throws TaskException      if the transformation cannot be applied.
   */
  protected void handleEqual(FlatFileProcessor processor, FlatFileWriter writer, FlatFileRecord record)
    throws IOException
    ,      TaskException {

    // Do nothing in this case
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleAdd
  /**
   ** Handles the specified line if this data set does only occur in the new
   ** file.
   ** <br>
   ** Prefix the data set with <code>"CRE"</code>.
   **
   ** @param  processor          the  {@link FlatFileProcessor} to transfer the
   **                            data from the specified {@link FlatFileRecord}
   **                            <code>record</code> the specified
   **                            {@link FlatFileWriter} <code>writer</code>.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the data set will not be
   **                            created.
   ** @param  record             the data set to handle.
   **
   ** @throws IOException        if the {@link FlatFileWriter} is already closed.
   ** @throws TaskException      if the transformation cannot be applied.
   */
  protected void handleAdd(final FlatFileProcessor processor, final FlatFileWriter writer, final FlatFileRecord record)
    throws IOException
    ,      TaskException {

    this.additions++;
    write(processor, writer, record);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleDel
  /**
   ** Handles the specified line if this data set does only occur in the old
   ** file.
   ** <br>
   ** Prefix the data set with <code>"DEL"</code>.
   **
   ** @param  processor          the  {@link FlatFileProcessor} to transfer the
   **                            data from the specified {@link FlatFileRecord}
   **                            <code>record</code> the specified
   **                            {@link FlatFileWriter} <code>writer</code>.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the data set will not be
   **                            created.
   ** @param  record             the data set to handle.
   **
   ** @throws IOException        if the {@link FlatFileWriter} is already closed.
   ** @throws TaskException      if the transformation cannot be applied.
   */
  protected void handleDel(final FlatFileProcessor processor, final FlatFileWriter writer, final FlatFileRecord record)
    throws IOException
    ,      TaskException {

    this.deletions++;
    write(processor, writer, record);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleMod
  /**
   ** Handles the specified line if this data set doccurs in both files, the
   ** keys are equal but the payload is different..
   ** <br>
   ** Prefix the line with <code>"MOD"</code>.
   **
   ** @param  processor          the  {@link FlatFileProcessor} to transfer the
   **                            data from the specified {@link FlatFileRecord}
   **                            <code>record</code> the specified
   **                            {@link FlatFileWriter} <code>writer</code>.
   ** @param  writer             {@link FlatFileWriter} which will receive the
   **                            modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the data set will not be
   **                            created.
   ** @param  record             the data set to handle.
   **
   ** @throws IOException        if the {@link FlatFileWriter} is already closed.
   ** @throws TaskException      if the transformation cannot be applied.
   */
  protected void handleMod(final FlatFileProcessor processor, final FlatFileWriter writer, final FlatFileRecord record)
    throws IOException
    ,      TaskException {

    this.modifcations++;
    write(processor, writer, record);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Print the specified data set to the target file
   **
   ** @param  processor          the  {@link FlatFileProcessor} to transfer the
   **                            data from the specified {@link FlatFileRecord}
   **                            <code>record</code> the specified
   **                            {@link FlatFileWriter} <code>writer</code>.
   ** @param  writer             the {@link FlatFileWriter} which will receive
   **                            the modifications. If the files passed to
   **                            {@link #compare(FlatFileReader, FlatFileReader, FlatFileWriter)}
   **                            are identically the data set will not be
   **                            created.
   ** @param  record             the data set to handle.
   **
   ** @throws IOException        if the {@link FlatFileWriter} is already closed.
   ** @throws TaskException      if the transformation cannot be applied.
   */
  protected void write(final FlatFileProcessor processor, final FlatFileWriter writer, final FlatFileRecord record)
    throws IOException
    ,      TaskException {

    record.write(processor, writer, false);
  }
}
