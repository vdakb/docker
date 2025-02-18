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

    File        :   CSVAlign.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVAlign.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.ArrayList;

import java.io.IOException;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.utility.resource.CSVBundle;

////////////////////////////////////////////////////////////////////////////////
// final class CSVAlign
// ~~~~~ ~~~~~ ~~~~~~~~
/**
 ** Aligns a CSV file in columns.
 ** Usage
 ** <pre>
 **   java oracle.iam.identity.utility.file.CSVAlign somefile.csv
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVAlign {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  THIS = ClassUtility.shortName(CSVAlign.class);

  /**
   ** e.g. \n \r\n or \r, whatever system uses to separate lines in a text file.
   ** Only used inside multiline fields. The file itself should use Windows
   ** format \r \n, through \n by itself will also work.
   */
  private static final String lineSeparator   = System.getProperty( "line.separator" );

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the file encoding of the file to read */
  private final String        encoding;

  /** the width of an entire aligned line, less the line separator. */
  private int                 lineWidth;

  /**
   ** how many pending leading spaces are outstanding. -1 = nothing outstanding.
   ** 0 = comma 1 = comma then one space.
   */
  private int                 pending    = -1;

  /** where we accumulate an entire aligend line. */
  private StringBuilder       lineBuffer;

  /** Descriptors  for each column as an ArrayList for pass 1 */
  private ArrayList<Column>   columnList;

  /** Descriptors for each column as an array for pass 2 */
  private Column[]           columnArray;

  //////////////////////////////////////////////////////////////////////////////
  // class Column
  // ~~~~~ ~~~~~~
  /**
   ** Descriptor ao a single column in the CSV file.
   */
  class Column {
    /**
     ** <code>true</code> if one or more fields in this column require
     ** surrounding quotes. On output, all fields in an awkward column will get
     ** surrounding quotes. If a field contains , or quote it needs the
     ** surrounding quotes.
     */
    private boolean isAwkward = false;

    /**
     ** if <code>true</code> all fields contain only digits dot, plus and minus.
     ** Assume column is numeric until proven otherwise by finding a non-numeric
     ** char.
     */
    private boolean isNumeric = true;

    /**
     ** widest field in the column. Not including comma. If a column is
     ** <code>awkward</code> (needs surrounding quotes), then the length
     ** includes the quotes and any internal quoting.
     */
    private int maxWidth = 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Align a CSV file.
   **
   ** @param  file               CSV file to be aligned in columns.
   ** @param  encoding           the file encoding of the file to read.
   ** @param  separator          field separator character, usually
   **                            <code>','</code> in North America,
   **                            <code>';'</code> in Europe and sometimes
   **                            <code>'\t'</code> for tab.
   ** @param quote               char to use to enclose fields containing a
   **                            separator, usually <code>'\"'</code>.
   **
   ** @throws IOException        in case of any other IO error
   ** @throws CSVException       some problemd reading the file, possibly
   **                            malformed data.
   */
  public CSVAlign(final File file, final String encoding, final char separator, final char quote)
    throws IOException
    ,      CSVException {

    this.encoding = encoding;

    // read file to get max widths
    pass1(file, separator, quote);

    // calculate the aggregate width of all the aligned fields.
    calculateLineWidth();

    // read file again this time writing, widening fields to align.
    pass2(file, separator, quote);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to align a CSV file.
   ** <br>
   ** Aligns one file whose name is on the command line. Must have extension
   ** <code>.csv</code>
   ** <br>
   ** Usage:
   ** <pre>
   **   java oracle.iam.identity.utility.file.CSVAlign somefile.csv encoding
   ** </pre>
   **
   ** @param  args               name of csv file to align in columns.
   **
   ** @throws CSVException       if commandline parameters incomplete specified.
   */
  public static void main(final String[] args)
    throws CSVException {

    if (args.length != 2)
      throw new CSVException(CSVError.FILENAME_MISSING, THIS);

    String filename = args[0];
    if (!filename.endsWith(CSVOperation.CSVEXTENSION))
      throw new CSVException(CSVError.FILEEXTENSION_IS_BAD, THIS);

    try {
      new CSVAlign(new File(filename), args[1], SystemConstant.COMMA, SystemConstant.QUOTE);
    }
    catch (Exception e) {
      final String[] arguments = {THIS, "align", filename };
      System.err.println(CSVBundle.format(CSVError.EXECUTION_FAILED, arguments));
      System.err.println(e.getMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pass1
  /**
   ** pass1 of aligning a CSV file, get the maximum column widths
   **
   ** @param  file               CSV file to be aligned in columns.
   ** @param  separator          field separator character, usually
   **                            <code>','</code> in North America,
   **                            <code>';'</code> in Europe and sometimes
   **                            <code>'\t'</code> for tab.
   ** @param quote               char to use to enclose fields containing a
   **                            separator, usually <code>'\"'</code>.
   **
   ** @thows  IOException        in case of any other IO error
   ** @throws CSVException       some problemd reading the file, possibly
   **                            malformed data.
   */
  private void pass1(final File file, final char separator, final char quote)
    throws IOException
    ,      CSVException {

    CSVReader  csv = null;
    try {
      // first pass read
      csv = new CSVReader( new InputStreamReader(new FileInputStream(file), this.encoding)
                         , separator
                         , quote
                         , false // multiline
                         , false // trim
                         );
    }
    catch (IOException e) {
      throw new CSVException(SystemError.GENERAL, e);
    }

    this.columnList = new ArrayList<Column>(40); /*<Column>*/
    try {
      while (true) {
        String[] fields = csv.readRecord();

        for (int colIndex = 0; colIndex < fields.length; colIndex++) {
          if (colIndex >= this.columnList.size()) {
            this.columnList.add(new Column());
          }
          Column col = this.columnList.get(colIndex);
          String field = fields[colIndex];
          int width = field.length();
          // does this field need surrounding quotes?
          boolean isAwkward = col.isAwkward || field.indexOf(separator) >= 0 || field.indexOf(quote) >= 0;
          if (isAwkward) {
            // extra col for lead/trail quote.
            width += 2;
            // extra col for each doubled quote
            for (int i = 0; i < field.length(); i++)
              if (field.charAt(i) == quote) {
                width++;
            }
          }
          if (!col.isAwkward && isAwkward) {
            col.isAwkward = true;
            col.maxWidth += 2;
          }
          if (width > col.maxWidth)
            col.maxWidth = width;

          if (!StringUtility.isLegal(field, "0123456789.+-"))
            col.isNumeric = false;
        }
      }
    }
    // thrown if end of file reached --> time to close the file
    catch (EOFException e) {
      csv.close();
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   pass2
  /**
   ** Pass2 of aligning a CSV file, expand fields to the max width.
   **
   ** @param  file               CSV file to be aligned in columns.
   ** @param  separator          field separator character, usually
   **                            <code>','</code> in North America,
   **                            <code>';'</code> in Europe and sometimes
   **                            <code>'\t'</code> for tab.
   ** @param quote               char to use to enclose fields containing a
   **                            separator, usually <code>'\"'</code>.
   **
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  private void pass2(final File file, final char separator, final char quote)
    throws CSVException {

    File           tmp = null;
    BufferedWriter out = null;
    CSVReader  csv = null;
    try {
      csv = new CSVReader( new InputStreamReader(new FileInputStream(file), this.encoding)
                         , separator
                         , quote
                         , false // multiline
                         , true  // trim
                         );
      tmp = FileSystem.createTempFile(CSVOperation.TMPPREFIX, CSVOperation.TMPEXTENSION, file);
      out = new BufferedWriter(new FileWriter(tmp));
    }
    catch (IOException e) {
      throw new CSVException(SystemError.GENERAL, e);
    }

    try {
      while (true) {
        String[] fields = csv.readRecord();
        lineBuffer      = new StringBuilder(lineWidth);
        pending = -1;
        // may have short line.
        for (int colIndex = 0; colIndex < fields.length; colIndex++) {
          Column col = columnArray[colIndex];
          String field = fields[colIndex];
          if (col.isNumeric) {
            // right justify, can't be awkward
            // spaces, field, comma, space
            rightJustify(field, separator, col.maxWidth);
          }
          else {
            // left justify
            // field, comma, space, spaces
            if (col.isAwkward) {
              field = prepareAwkward(field, quote);
            }
            leftJustify(field, separator, col.maxWidth);
          }
        } // end for
        out.write(lineBuffer.toString());
        out.write(lineSeparator);
      } // end while
    } // end try
    catch (EOFException e) {
      try {
        csv.close();
        // swap tempFile output and input now that output is safely created.
        out.close();
      }
      catch (IOException io) {
        throw new CSVException(SystemError.GENERAL, e);
      }
      file.delete();
      tmp.renameTo(file);
    }
    catch (IOException e) {
      throw new CSVException(SystemError.GENERAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   calculateLineWidth
  /**
   ** calculate the with of the line after we have aligned it.
   */
  private void calculateLineWidth() {
    this.lineWidth = 0;
    int size = this.columnList.size();
    this.columnArray = this.columnList.toArray(new Column[size]);

    this.columnList = null;
    for (int i = 0; i < this.columnArray.length; i++)
      // one for comma and one fdor space
      this.lineWidth += this.columnArray[i].maxWidth + 2;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   leftJustify
  /**
   ** Append a field to the wholeLine left justified.
   **
   ** @param  field              field to add, with awkward encoding in place if
   **                            any.
   ** @param  separator          field separator character, usually
   **                            <code>','</code> in North America,
   **                            <code>';'</code> in Europe and sometimes
   **                            <code>'\t'</code> for tab.
   ** @param  columnWidth        width of the column
   */
  private void leftJustify(final String field, final char separator, final int columnWidth) {
    if (this.pending >= 0) {
      this.lineBuffer.append(separator);
      for (int j = 0; j < this.pending; j++)
        this.lineBuffer.append(' ');
    }

    this.lineBuffer.append(field);
    // if there is a subsequent field we have a , plus spaces to pad, plus
    // space.
    this.pending = columnWidth - field.length() + 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rightJustify
  /**
   ** Append a field to the wholeLine right justified
     *
   ** @param  field              field to add, with awkward encoding in place if
   **                            any.
   ** @param  separator          field separator character, usually
   **                            <code>','</code> in North America,
   **                            <code>';'</code> in Europe and sometimes
   **                            <code>'\t'</code> for tab.
   ** @param  columnWidth        width of the column
   */
  private void rightJustify(final String field, final char separator, final int columnWidth) {
    if (this.pending >= 0) {
      this.lineBuffer.append(separator);
      for (int j = 0; j < this.pending; j++)
        this.lineBuffer.append(' ');
    }

    int spaces = columnWidth - field.length();
    for (int j = 0; j < spaces; j++)
      this.lineBuffer.append(' ');

    this.lineBuffer.append(field);

    // comma plus space, if there is a subsequent field
    this.pending = 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareAwkward
  /**
   ** Prepare an akward string for output by enclosing it in " and doubling
   ** internal "
   **
   ** @param  s                  output field to prepare.
   ** @param quote               char to use to enclose fields containing a
   **                            separator, usually <code>'\"'</code>.
   **
   ** @return                    string enclosed in quotes with internal quotes
   **                            doubled.
   */
  private static String prepareAwkward(final String s, final char quote) {
    if (s.indexOf(quote) < 0)
      return quote + s + quote;

    final StringBuilder sb = new StringBuilder(s.length() + 10);
    sb.append(quote);
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == quote) {
        sb.append(quote);
        sb.append(quote);
      }
      else
        sb.append(c);
    }

    sb.append(quote);
    return sb.toString();
  }
}