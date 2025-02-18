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

    File        :   FlatFileReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlatFileReader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.io.IOException;
import java.io.EOFException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileReader
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Read text file described by position in lines.
 ** <p>
 ** We optionally trim leading and trailing spaces on fields, even inside
 ** quotes.
 ** <p>
 ** File must normally end with a single CRLF, other wise you will get a null
 ** when trying to read a field on older JVMs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public final class FlatFileReader extends BufferedReader {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** e.g. # as a comment, only at the start of line.
   */
  private static final char comment         = '#';

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the file encoding of the file to read */
  private final String      encoding;

  /**
   ** The line we are parsing. null means none read yet. Line contains
   ** unprocessed chars. Processed ones are removed.
   */
  private String              lineBuffer   = null;

  /**
   ** How many lines we have proceed so far.
   ** <br>
   ** In Contrast to <code>lineCount</code> this number counts the effective
   ** lines with out comments etc.
   */
  private int                 linesProceed = 0;

  /** How many lines we have read so far. Used in error messages. */
  private int                 lineCount    = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Convenience Constructor, default to comma separator, <code>"</code> for
   ** quote, no multiline fields, with trimming.
   **
   ** @param  reader             {@link InputStreamReader} source of FlatFile Fields
   **                            to read.
   */
  public FlatFileReader(final InputStreamReader reader) {
    // ensure inheritance
    super(reader);

    // initialize instance
    this.encoding = reader.getEncoding();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
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
  // Method:   linesProceed
  /**
   ** Returns the numbers of lines proceed on the file.
   **
   ** @return                    the numbers of lines proceed on the file.
   */
  public final int linesProceed() {
    return this.linesProceed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readRecord
  /**
   ** Get all fields in the line
   **
   ** @return                    an array of strings, one for each field.
   **                            Possibly empty, but never <code>null</code>.
   **
   ** @throws EOFException       at end of file after all the fields have been
   **                            read.
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  public String readRecord()
    throws EOFException
    ,      TaskException {

    final String record = get();
    if (record != null)
      // increment the counter of effective lines
      this.linesProceed++;

    return record;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Read one field from the text file.
   **
   ** @return                    String value, even if the field is numeric.
   **                            Surrounded and embedded double quotes are
   **                            stripped. possibly "". null means end of line.
   **
   ** @throws EOFException       at end of file after all the fields have been
   **                            read.
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  public String get()
    throws EOFException
    ,      TaskException {

    ensureLineBuffer();
    return this.lineBuffer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureLineBuffer
  /**
   ** Make sure a line is available for parsing.
   ** <br>
   ** Does nothing if there already is one.
   **
   ** @throws EOFException      at end of file after all the fields have been
   **                           read.
   ** @throws TaskException     some problem reading the file, possibly
   **                           malformed data.
   */
  private void ensureLineBuffer()
    throws EOFException
    ,      TaskException {

    try {
      do {
        // this strips platform specific line ending
        this.lineBuffer = super.readLine();
        // skip all lines that are empty or a comment
        if (this.lineBuffer == null || (this.lineBuffer.length() > 0 && this.lineBuffer.charAt(0) != comment))
          break;
      } while (true);
    }
    catch (IOException e) {
      throw TaskException.general(e);
    }
    catch (Exception e) {
      String[] parameter = { e.getLocalizedMessage(), String.valueOf(lineCount), this.lineBuffer};
      throw new FlatFileException(FlatFileError.PARSER_ERROR, parameter, e);
    }

    // null means EOF, yet another inconsistent Java convention.
    if (this.lineBuffer == null)
      throw new EOFException();

    // apply standard line end for parser to find
    this.lineBuffer += SystemConstant.LINEBREAK;
    this.lineCount++;
  }
}