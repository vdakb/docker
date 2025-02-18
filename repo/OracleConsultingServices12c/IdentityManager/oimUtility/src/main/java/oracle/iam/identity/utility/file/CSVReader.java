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

    File        :   CSVReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVReader.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.EOFException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.SystemConstant;

////////////////////////////////////////////////////////////////////////////////
// final class CSVReader
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** Read CSV (Comma Separated Value) files.
 ** <p>
 ** This format is used my Microsoft Word and Excel. Fields are separated by
 ** commas, and enclosed in quotes if they contain commas or quotes. Embedded
 ** quotes are doubled. Embedded spaces do not normally require surrounding
 ** quotes. The last field on the line is not followed by a comma. Null fields
 ** are represented by two commas in a row.
 ** <p>
 ** We optionally trim leading and trailing spaces on fields, even inside
 ** quotes.
 ** <p>
 ** File must normally end with a single CRLF, other wise you will get a null
 ** when trying to read a field on older JVMs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVReader extends BufferedReader {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** file state: We are in blanks before the field. */
  private static final int    SEEKING_START   = 0;

  /** line state: We have just hit a quote, might be doubled or might be last one. */
  private static final int    AFTER_END_QUOTE = 3;

  /** line state: We are in blanks after the field looking for the separator */
  private static final int    SKIPPING_TAIL   = 4;

  /** line state: of end of line char. */
  private static final int    EOL             = 0;

  /** line state: We are in the middle of an ordinary field. */
  private static final int    IN_PLAIN        = 1;

  /** line state: We are in middle of field surrounded in quotes. */
  private static final int    IN_QUOTED       = 2;

  /** category: ordinary character */
  private static final int    ORDINARY        = 1;

  /** category: quote mark " */
  private static final int    QUOTE           = 2;

  /** category: separator, e.g. comma, semicolon or tab. */
  private static final int    SEPARATOR       = 3;

  /** category: characters treated as white space. */
  private static final int    WHITESPACE      = 4;

  /**
   ** e.g. \n \r\n or \r, whatever system uses to separate lines in a text file.
   ** Only used inside multiline fields. The file itself should use Windows
   ** format \r \n, through \n by itself will also work.
   */
  private static final String lineSeparator   = System.getProperty( "line.separator" );

  /**
   ** e.g. # as a comment, only at the start of line.
   */
  private static final char   comment         = '#';

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the file encoding of the file to read */
  private final String     encoding;

  /**
   ** <code>true</code> if reader should allow quoted fields to span more than
   ** one line.
   ** Microsoft Excel sometimes generates files like this.
   */
  private final boolean    allowMultiLine;

  /**
   ** <code>true</code> if reader should trim lead/trail whitespace from fields
   ** returned.
   */
  private final boolean    trim;

  /**
   ** <code>false</code> means next <code>EOL</code> marks an empty field
   ** <code>true</code> means next <code>EOL</code> marks the end of all fields.
   */
  private boolean          allFieldsDone = true;

  /**
   ** quote character, usually <code>'\"'</code> <code>'\''</code> for SOL used
   ** to enclose fields containing a separator character.
   */
  private final char       quote;

  /**
   ** field separator character, usually <code>','</code> in North America,
   ** <code>';'</code> in Europe and sometimes <code>'\t'<code> for tab.
   */
  private final char       separator;

  /**
   ** The line we are parsing. null means none read yet. Line contains
   ** unprocessed chars. Processed ones are removed.
   */
  private String           lineBuffer   = null;

  /**
   ** How many lines we have proceed so far.
   ** <br>
   ** In Contrast to <code>lineCount</code> this number counts the effective
   ** lines with out comments etc.
   */
  private int              linesProceed = 0;

  /** How many lines we have read so far. Used in error messages. */
  private int              lineCount    = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Convenience Constructor, default to comma separator, <code>"</code> for
   ** quote, no multiline fields, with trimming.
   **
   ** @param  reader             {@link InputStreamReader} source of CSV Fields
   **                            to read.
   */
  public CSVReader(final InputStreamReader reader) {

    this(reader, SystemConstant.COMMA, SystemConstant.QUOTE, true, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor
   **
   ** @param  reader             {@link InputStreamReader} source of CSV Fields
   **                            to read.
   ** @param  separator          field separator character, usually
   **                            <code>','</code> in North America,
   **                            <code>';'</code> in Europe and sometimes
   **                            <code>'\t'</code> for tab.
   ** @param  quote              char to use to enclose fields containing a
   **                            separator, usually <code>'\"'</code>.
   ** @param  allowMultiLine     <code>true</code> if reader should allow quoted
   **                            fields to span more than one line. Microsoft
   **                            Excel sometimes generates files like this.
   ** @param  trim               <code>true</code> if {@link Reader} should trim
   **                            lead/trailing whitespace e.g. blanks, CR, LF,
   **                            TAB off fields.
   */
  public CSVReader(final InputStreamReader reader, final char separator, final char quote, final boolean allowMultiLine, final boolean trim) {
    // ensure inheritance
    super(reader);

    this.encoding       = reader.getEncoding();
    this.separator      = separator;
    this.quote          = quote;
    this.allowMultiLine = allowMultiLine;
    this.trim           = trim;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoding
  /**
   ** Returns the file encoding of this <code>CSVOperation</code>.
   **
   ** @return                    the file encoding of this
   **                            <code>CSVOperation</code>.
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
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  public String[] readRecord()
    throws EOFException
    ,      CSVException {

    final List<String> list = new ArrayList<String>();
    do {
      String field = get();
      if (field == null)
        break;

      list.add(field);
    }
    while (true);

    // increment the counter of effective lines
    this.linesProceed++;

    return list.toArray(new String[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Read one field from the CSV file.
   **
   ** @return                    String value, even if the field is numeric.
   **                            Surrounded and embedded double quotes are
   **                            stripped. possibly "". null means end of line.
   **
   ** @throws EOFException       at end of file after all the fields have been
   **                            read.
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  public String get()
    throws EOFException
    ,      CSVException {

    final StringBuilder field = new StringBuilder(allowMultiLine ? 512 : 64);

    // we implement the parser as a finite state automation with five states.
    int state = SEEKING_START;
    // start seeking, even if partway through a line don't need to maintain
    // state between fields.
    lineLoop:
    while (true) {
      ensureLineBuffer();

      charLoop:
      // loop for each char in the line to find a field guaranteed to leave
      // early by hitting EOL
      for (int i = 0; i < this.lineBuffer.length(); i++) {
        char c        = this.lineBuffer.charAt(i);
        int  category = categorize(c);
        switch (state) {
          case SEEKING_START : switch (category) {
                                 case WHITESPACE : // ignore
                                                   break;
                                 case QUOTE      : state = IN_QUOTED;
                                                   break;
                                 // end of empty field
                                 case SEPARATOR  : this.lineBuffer = this.lineBuffer.substring(i + 1);
                                                   return SystemConstant.EMPTY;
                                 // end of line
                                 case EOL        : if (this.allFieldsDone) {
                                                     // null to mark end of line
                                                     this.lineBuffer = null;
                                                     return this.lineBuffer;
                                                   }
                                                   else {
                                                     // empty field, usually after a comma
                                                     this.lineBuffer    = this.lineBuffer.substring(i);
                                                     this.allFieldsDone = true;
                                                     return SystemConstant.EMPTY;
                                                   }
                                 case ORDINARY   : field.append(c);
                                                   state = IN_PLAIN;
                                                   break;
                               }
                               break;
          // in middle of ordinary field
          case IN_PLAIN      : switch (category) {
                                 case QUOTE      : String[] arguments = { String.valueOf(lineCount) };
                                                   throw new CSVException(CSVError.MISSING_QUOTE_OPEN, arguments);
                                 // done
                                 case SEPARATOR  : this.lineBuffer = this.lineBuffer.substring(i + 1);
                                                   return trimLine(field.toString());
                                 // push EOL back
                                 case EOL        : this.lineBuffer    = this.lineBuffer.substring(i);
                                                   this.allFieldsDone = true;
                                                   return trimLine(field.toString());
                                 case WHITESPACE : field.append(SystemConstant.BLANK);
                                                   break;
                                 case ORDINARY   : field.append(c);
                                                   break;
                               }
                               break;
          // in middle of field surrounded in quotes
          case IN_QUOTED     : switch (category) {
                                 case QUOTE      : state = AFTER_END_QUOTE;
                                                   break;
                                 case EOL        : if (this.allowMultiLine) {
                                                     field.append(lineSeparator);
                                                     // we are done with that line,
                                                     // but not with the field.
                                                     // We don't want to return a
                                                     // null to mark the end of
                                                     // the line.
                                                     this.lineBuffer = null;
                                                     // will read next line and
                                                     // seek the end of the
                                                     // quoted field with state
                                                     // IN_QUOTED.
                                                     // continue seem to be better as a jump label
                                                     break charLoop;
                                                   }
                                                   else {
                                                     // no multiline fields allowed
                                                     this.allFieldsDone = true;
                                                     String[] arguments = { String.valueOf(this.lineCount) };
                                                     throw new CSVException(CSVError.MISSING_QUOTE_CLOSE, arguments);
                                                   }
                                 case WHITESPACE : field.append(SystemConstant.BLANK);
                                                   break;
                                 case SEPARATOR  :
                                 case ORDINARY   : field.append(c);
                                                   break;
                               }
                               break;
          // In situation like this "xxx" which may turn out to be xxx""xxx" or
          // "xxx"
          // we find out here.
          case AFTER_END_QUOTE : switch (category) {
                                 // was a double quote, e.g. a literal "
                                 case QUOTE      : field.append(c);
                                                   state = IN_QUOTED;
                                                   break;
                                 // we are done with field.
                                 case SEPARATOR  : this.lineBuffer = this.lineBuffer.substring(i + 1);
                                                   return trimLine(field.toString());
                                 // push back eol
                                 case EOL        : this.lineBuffer    = this.lineBuffer.substring(i);
                                                   this.allFieldsDone = true;
                                                   return trimLine(field.toString());
                                 // ignore trailing spaces up to separator
                                 case WHITESPACE : state = SKIPPING_TAIL;
                                                   break;
                                 case ORDINARY   : String[] arguments = { String.valueOf(lineCount) };
                                                   throw new CSVException(CSVError.MISSING_QUOTE_CLOSE, arguments);
                               }
                               break;
          // in spaces after field seeking separator
          case SKIPPING_TAIL:  switch (category) {
                                  // we are done.
                                  case SEPARATOR : this.lineBuffer = this.lineBuffer.substring(i + 1);
                                                   return trimLine(field.toString());
                                 // push back eol
                                  case EOL       : this.lineBuffer    = this.lineBuffer.substring(i);
                                                   this.allFieldsDone = true;
                                                   return trimLine(field.toString());
                                 // ignore trailing spaces up to separator
                                 case WHITESPACE : break;
                                 case QUOTE      :
                                 case ORDINARY   : String[] arguments = { String.valueOf(lineCount) };
                                                   throw new CSVException(CSVError.MISSING_QUOTE_CLOSE, arguments);
                               }
                               break;
        } // end switch(state)
      } // end charLoop
    } // end lineLoop
  } // end get

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureLineBuffer
  /**
   ** Make sure a line is available for parsing.
   ** <br>
   ** Does nothing if there already is one.
   **
   ** @throws EOFException      at end of file after all the fields have been
   **                           read.
   ** @throws CSVException      some problem reading the file, possibly
   **                           malformed data.
   */
  private void ensureLineBuffer()
    throws EOFException
    ,      CSVException {

    if (this.lineBuffer == null) {

      this.allFieldsDone = false;
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
        throw new CSVException(SystemError.GENERAL, e);
      }
      catch (Exception e) {
        String[] parameter = { e.getLocalizedMessage(), String.valueOf(lineCount), this.lineBuffer};
        throw new CSVException(CSVError.PARSER_ERROR, parameter, e);
      }

      // null means EOF, yet another inconsistent Java convention.
      if (this.lineBuffer == null)
        throw new EOFException();

      // apply standard line end for parser to find
      this.lineBuffer += SystemConstant.LINEBREAK;
      this.lineCount++;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   categorize
  /**
   ** Categorize a character for the finite state machine.
   **
   ** @param  c                  the character to categorize
   **
   ** @return                    integer representing the character's category.
   */
  private int categorize(final char c) {
    switch (c) {
      case ' '  :
      case '\r' :
      case 0xff : return WHITESPACE;
      // artificially applied to end of line
      case '\n' : return EOL;
      default   : if (c == quote)
                    return QUOTE;
                  // dynamically determined so can't use as case label
                  else if (c == separator)
                    return SEPARATOR;
                  // do our tests in crafted order, hoping for an early return
                  else if ('!' <= c && c <= '~')
                    return ORDINARY;
                  else if (0x00 <= c && c <= 0x20)
                    return WHITESPACE;
                  else if (Character.isWhitespace(c))
                    return WHITESPACE;
                  else
                    return ORDINARY;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimLine
  /**
   ** Trim the line, but only if we are in trimming mode.
   **
   ** @param  line               String to be trimmed.
   **
   ** @return                    String or trimmed string.
   */
  private String trimLine(final String line) {
    return (trim) ? line.trim() : line;
  }
}