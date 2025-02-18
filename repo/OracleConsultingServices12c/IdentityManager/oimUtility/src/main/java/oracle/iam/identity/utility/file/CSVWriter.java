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

    File        :   CSVWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVWriter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Iterator;
import java.util.Map;

import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.SystemConstant;

////////////////////////////////////////////////////////////////////////////////
// final class CSVWriter
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** Write CSV (Comma Separated Value) files.
 ** <p>
 ** This format is used by Microsoft Word and Excel. Fields are separated by
 ** commas, and enclosed in quotes if they contain commas or quotes. Embedded
 ** quotes are doubled. Embedded spaces do not normally require surrounding
 ** quotes. The last field on the line is not followed by a comma.
 ** <p>
 ** Empty fields are represented by two commas in a row.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVWriter extends BufferedWriter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the file encoding of the file to read */
  private final String  encoding;

  /**
   ** <code>true</code> if reader should trim lead/trail whitespace from fields
   ** returned.
   */
  private final boolean trim;

  /**
   ** quote character, usually <code>'\"'</code> <code>'\''</code> for SOL used
   ** to enclose fields containing a separator character.
   */
  private final char    quote;

  /** how much extra quoting you want */
  private final int     quoteLevel;

  /**
   ** field separator character, usually <code>','</code> in North America,
   ** <code>';'</code> in Europe and sometimes <code>'\t'<code> for tab.
   */
  private final char    separator;

  /**
   ** <code>true</code> if there has was a field previously written to this
   ** line, meaning there is a comma pending to be written.
   */
  private boolean       unclosedLine = false;

  /**
   ** How many lines we have proceed so far.
   ** <br>
   ** In Contrast to <code>lineCount</code> this number counts the effective
   ** lines with out comments etc.
   */
  private int           linesProceed = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Convenience Constructor, defaults to quotelevel 1, comma separator , trim.
   **
   ** @param  writer             {@link OutputStreamWriter} where fields will be
   **                            written to.
   **
   ** @throws CSVException       some problem in migration the writer to a
   **                            {@link OutputStreamWriter}.
   */
  public CSVWriter(final OutputStreamWriter writer)
    throws CSVException {

    this(writer, 1, SystemConstant.COMMA, SystemConstant.QUOTE, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Convenience Constructor, defaults comma separator , trim.
   **
   ** @param  writer             ths {@link OutputStreamWriter} where fields
   **                            will be written to.
   ** @param  quoteLevel         0 = minimal quotes <br>
   **                            1 = quotes also around fields containing spaces<br>
   **                            2 = quotes around all fields, whether or not they contain commas,
   **                            quotes or spaces.
   **
   ** @throws CSVException       some problem in convertion of the specified
   **                            writer to an {@link OutputStreamWriter}.
   */
  public CSVWriter(final OutputStreamWriter writer, final int quoteLevel)
    throws CSVException {

    this(writer, quoteLevel, SystemConstant.COMMA, SystemConstant.QUOTE, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor
   **
   ** @param  writer             ths {@link OutputStreamWriter} where fields
   **                            will be written to.
   ** @param  quoteLevel         0 = minimal quotes <br>
   **                            1 = quotes also around fields containing spaces<br>
   **                            2 = quotes around all fields, whether or not they contain commas,
   **                            quotes or spaces.
   ** @param  separator          field separator character, usually
   **                            <code>','</code> in North America,
   **                            <code>';'</code> in Europe and sometimes
   **                            <code>'\t'</code> for tab.
   ** @param  quote              char to use to enclose fields containing a
   **                            separator, usually <code>'\"'</code>.
   ** @param  trim               <code>true</code> if {@link Writer} should trim
   **                            lead/trailing whitespace e.g. BLANKS, CR, LF,
   **                            TAB before writing the field.
   **
   */
  public CSVWriter(final OutputStreamWriter writer, final int quoteLevel, final char separator, final char quote, final boolean trim) {
    // ensure inheritance
    super(writer);

    // initialize instance
    this.encoding   = writer.getEncoding();
    this.quoteLevel = quoteLevel;
    this.separator  = separator;
    this.quote      = quote;
    this.trim       = trim;
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
  // Method:   newLine (overridden)
  /**
   ** Write a new line in the CVS output file to demark the end of record.
   **
   ** @throws IOException    if the {@link Writer} is already closed.
   */
  @Override
  public void newLine()
    throws IOException {

    super.newLine();
    this.unclosedLine = false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put
  /**
   ** Write one csv field to the file, followed by a separator unless it is the
   ** last field on the line. Lead and trailing blanks will be removed.
   **
   ** @param  field              the string to write.
   **                            Any additional quotes or embedded quotes will
   **                            be provided by put. Null means start a new
   **                            line.
   **
   ** @throws CSVException       if the {@link Writer} is already closed.
   */
  public void put(String field)
    throws CSVException {

    try {
      if (field == null) {
        newLine();
        return;
      }

      if (unclosedLine)
        super.write(separator);

      if (trim)
        field = field.trim();

      if (field.indexOf(quote) >= 0) {
        // worst case, needs surrounding quotes and internal quotes doubled
        super.write(quote);
        for (int i = 0; i < field.length(); i++) {
          char c = field.charAt(i);
          if (c == quote) {
            super.write(quote);
            super.write(quote);
          }
          else
            super.write(c);
        }
        super.write(quote);
      }
      else if (quoteLevel == 2 || quoteLevel == 1 && field.indexOf(' ') >= 0 || field.indexOf(separator) >= 0) {
        // need surrounding quotes
        super.write(quote);
        super.write(field);
        super.write(quote);
      }
      else {
        // ordinary case, no surrounding quotes needed
        super.write(field);
      }
      // make a note to print trailing comma later
      unclosedLine = true;
    }
    catch (IOException e) {
      throw new CSVException(SystemError.GENERAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   putLine
  /**
   ** Write the entire line to this CSV file writer.
   ** <br>
   ** The line is closed by a newline character.
   **
   ** @param  processor          the {@link CSVProcessor} to control the
   **                            marshalling
   ** @param  entity             the {@link Map} containing the content of the
   **                            line to write
   **
   ** @throws CSVException       if the {@link Writer} is already closed.
   */
//  public void putLine(CSVProcessor processor, Map entity, boolean applyTransformer)
  public void putLine(final CSVProcessor processor, final Map<String, String> entity)
    throws CSVException {

    Iterator<CSVAttribute> i = processor.descriptor().attributeIterator();
    while (i.hasNext()) {
      final CSVAttribute attribute = i.next();
      String value   = entity.get(attribute.name());
      if (value == null) {
        processor.warn("putLine", CSVError.CONTENT_UNKNOWN, attribute.name());
        put(SystemConstant.EMPTY);
      }
      else {
/*
        if (applyTransformer) {
          // at first apply the transformation, this creates an new object
          // instance
          value = attribute.transformOutbound(value);

          // the object instance is now converted
          put(attribute.convertInternal(value));
        }
        else {
        }
*/
        put(value);
      }
    }
    // increment the counter of effective lines
    this.linesProceed++;

    try {
      newLine();
    }
    catch (IOException e) {
      throw new CSVException(SystemError.GENERAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commentLine
  /**
   ** Write a comment line in the CVS output file to demark the end of record.
   **
   ** @param  comment            the comment to write
   **
   ** @throws CSVException       if the {@link Writer} is already closed.
   */
  public void commentLine(final String comment)
    throws CSVException {

    try {
      // don't bother to write last pending comma on the line
      super.write("#");
      super.write(comment);
      newLine();
    }
    catch (IOException e) {
      throw new CSVException(SystemError.GENERAL, e);
    }
  }
}