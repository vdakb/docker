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

    File        :   FlatFileSorting.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FlatFileSorting.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileSorting
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Text File Sorting Utility with changes made in order to be able to take
 ** randomly sorted input files as long as they have comparable fields.
 ** <br>
 ** Current separator is the semicolon ; char
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public final class FlatFileSorting extends FlatFileOperation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the file encoding of the temporary files to create */
  private String encoding;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FlatFileSorting</code> that will write the sorted file.
   **
   ** @param  descriptor         the {@link FlatFileDescriptor} used to extract the
   **                            content form the text files.
   ** @param  encoding           the file encoding of the temporary files to
   **                            create.
   */
  public FlatFileSorting(final FlatFileDescriptor descriptor, final String encoding) {
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
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sort
  /**
   ** Method expects parameters relation (the name of a table, which in this
   ** case is the .csv file to sort without the ".csv" in the file name) and the
   ** column to sort on (it checks the first row of the .csv file for the value
   ** passed).
   ** <br>
   ** Now, of course that is easy enough to change to fit your own needs -
   ** it worked fine for me and my FlatFiles.
   **
   ** @param  processor          the processor providing the data.
   ** @param  workingFolder      the location in the file system where the
   **                            temporary files will be created.
   **
   ** @return                    the abstract path to the {@link File}
   **                            containing the sorted records.
   **
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  public File sort(final FlatFileProcessor processor, final File workingFolder)
    throws TaskException {

    return this.sort(processor, workingFolder, new FlatFileKeyComparator());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sort
  /**
   ** Method expects parameters relation (the name of a table, which in this
   ** case is the .csv file to sort without the ".csv" in the file name) and the
   ** column to sort on (it checks the first row of the .csv file for the value
   ** passed).
   ** <br>
   ** Now, of course that is easy enough to change to fit your own needs -
   ** it worked fine for me and my FlatFiles.
   **
   ** @param  processor          the processor providing the data.
   ** @param  workingFolder      the location in the file system where the
   **                            temporary files will be created.
   ** @param  comparator         the {@link Comparator} used to sort.
   **
   ** @return                    the abstract path to the {@link File}
   **                            containing the sorted records.
   **
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  public File sort(final FlatFileProcessor processor, final File workingFolder, final Comparator<FlatFileRecord> comparator)
    throws TaskException {

    final String method = "sort";

    boolean      eof   = false;
    List<File>   file  = new ArrayList<File>();
    FlatFileResultSet chunk = new FlatFileResultSet(processor);
    do {
      // create the file handle
      File chunkFile = null;
      try {
        chunkFile = FileSystem.createTempFile(FlatFileOperation.TMPPREFIX, FlatFileOperation.TMPEXTENSION, workingFolder);
        file.add(chunkFile);
      }
      catch (IOException e) {
        throw new FlatFileException(SystemError.GENERAL, e);
      }

      chunk.clear();
      try {
        // read a chunk from the asociated processor
        // the chunk is storing the fetched content internally
        chunk.fetch(10000, false);
      }
      catch (EOFException e) {
        // if we reached the end of file  sort the last chunk ...
        debug(method, FlatFileMessage.ENDOFFILE, chunkFile.getName());
        eof = true;
      }
      // sort the chunk in memory
      chunk.sort(comparator);
      String[] parameter = { String.valueOf(chunk.size()), chunkFile.getName() };
      debug(method, FlatFileMessage.CHUNKSIZE, parameter);

      FlatFileWriter writer = null;
      try {
        writer = new FlatFileWriter(new OutputStreamWriter(new FileOutputStream(chunkFile), this.encoding));
        processor.writeEntity(writer, chunk, false, false);
      }
      catch (IOException e) {
        throw new FlatFileException(SystemError.GENERAL, e);
      }
      finally {
        try {
          writer.close();
        }
        catch (IOException e) {
          error(method, FlatFileError.NOTCLOSEDOUTPUT, chunkFile.getPath());
        }
      }

    } while(!eof);
    return merge(processor, file, workingFolder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeFiles
  /**
   **
   ** @param  processor          the processor providing the data.
   **
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  private File merge(final FlatFileProcessor processor, final List<File> file, final File workingFolder)
    throws TaskException {

    final String method = "merge";

    File      mergeFile   = null;
    FlatFileWriter mergeWriter = null;
    try {
      // create the file handle
      try {
        mergeFile = FileSystem.createTempFile("~csv", ".tmp", workingFolder);
      }
      catch (IOException e) {
        throw new FlatFileException(SystemError.GENERAL, e);
      }

      // create the reciever for the operation with the required file encoding
      mergeWriter = new FlatFileWriter(new OutputStreamWriter(new FileOutputStream(mergeFile), this.encoding));
      // always create the header in the result file
      processor.descriptor().write(mergeWriter);
    }
    catch (IOException e) {
      throw new FlatFileException(SystemError.GENERAL, e);
    }

    ArrayList<FlatFileRecord>    mergeResult = new ArrayList<FlatFileRecord>(file.size());
    ArrayList<FlatFileProcessor> mergeChunk  = new ArrayList<FlatFileProcessor>(file.size());
    try {
      boolean someFileStillHasRows = false;
      for (int i = 0; i < file.size(); i++) {
        final FlatFileReader    r = new FlatFileReader(new InputStreamReader(new FileInputStream(file.get(i)), this.encoding));
        final FlatFileProcessor p = new FlatFileProcessor(processor.descriptor(), r);
        p.logger(this.logger());
        mergeChunk.add(p);
        try {
          // get the first row of the current chunk
          final Map<String, Object> entity = p.readEntity(false);
          final FlatFileRecord           record = new FlatFileRecord(entity, processor.descriptor().identifier(entity), processor.descriptor().payload(entity));
          mergeResult.add(record);
          someFileStillHasRows = true;
        }
        catch (EOFException e) {
          debug(method, FlatFileMessage.EMPTYFILE, file.get(i).getName());
          mergeResult.add(null);
        }
      }

      FlatFileRecord row;
      FlatFileRecord min;
      while (someFileStillHasRows) {
        int   minIndex = 0;
        min = mergeResult.get(0);
        if (min != null)
          minIndex = 0;
        else
          minIndex = -1;

        // check which one is min
        for (int i = 1; i < mergeResult.size(); i++) {
          row = mergeResult.get(i);
          if (min != null) {
            if (row != null && row.compareKey(min) < 0) {
              minIndex = i;
              min      = row;
            }
          }
          else {
            if (row != null) {
              min      = row;
              minIndex = i;
            }
          }
        }

        if (minIndex < 0)
          someFileStillHasRows = false;
        else {
          // write to the sorted file
          min.write(processor, mergeWriter, false);

          // get another row from the file that had the min
          try {
            final Map<String, Object> entity = mergeChunk.get(minIndex).readEntity(false);
            final FlatFileRecord           record = new FlatFileRecord(entity, processor.descriptor().identifier(entity), processor.descriptor().payload(entity));
            mergeResult.set(minIndex, record);
          }
          catch (EOFException e) {
            debug(method, FlatFileMessage.CHUNKMERGED, file.get(minIndex).getName());
            mergeResult.set(minIndex, null);
          }
        }
      }
    }
    catch (Exception e) {

     for (int i = 0; i < mergeChunk.size(); i++) {
        String[] oldFile = { "chunk", String.valueOf(mergeChunk.get(i).linesProceed()) };
        debug(method, FlatFileMessage.LINECOUNT, oldFile);
      }

      throw new FlatFileException(SystemError.UNHANDLED, e);
    }
    finally {
      // close all the files
      try {
        mergeWriter.close();
      }
      catch (IOException e) {
        error(method, FlatFileError.NOTCLOSEDOUTPUT, mergeFile.getPath());
      }
      for (int i = 0; i < mergeChunk.size(); i++)
        mergeChunk.get(i).close();
       // delete the temporary created files
      for (int i = 0; i < file.size(); i++)
        file.get(i).delete();
   }
    return mergeFile;
  }
}