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

    File        :   FlatFileWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlatFileWriter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;
import java.util.Iterator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileWriter
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Write text file described by position in lines.
 ** <p>
 ** Empty fields are represented by spaces in a row.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public final class FlatFileWriter extends BufferedWriter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the file encoding of the file to read */
  private final String  encoding;

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
   */
  public FlatFileWriter(final OutputStreamWriter writer) {
    // ensure inheritance
    super(writer);

    // initialize instance
    this.encoding = writer.getEncoding();
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
   ** @throws TaskException     if the <code>Writer</code> is already closed.
   */
  public void put(final String field)
    throws TaskException {

    try {
      if (field == null) {
        newLine();
        return;
      }

      // ordinary case, no surrounding quotes needed
      super.write(field);
    }
    catch (IOException e) {
      throw TaskException.general(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   putLine
  /**
   ** Write the entire line to this text file writer.
   ** <br>
   ** The line is closed by a newline character.
   **
   ** @param  processor          the {@link FlatFileProcessor} to control the
   **                            marshalling
   ** @param  entity             the {@link Map} containing the content of the
   **                            line to write
   **
   ** @throws TaskException      if the <code>Writer</code> is already closed.
   */
  public void putLine(final FlatFileProcessor processor, final Map<String, String> entity)
    throws TaskException {

    Iterator<FlatFileAttribute> i = processor.descriptor().attributeIterator();
    while (i.hasNext()) {
      final FlatFileAttribute attribute = i.next();
      String value   = entity.get(attribute.name());
      if (value == null) {
        processor.warn("putLine", FlatFileError.CONTENT_UNKNOWN, attribute.name());
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
      throw TaskException.general(e);
    }
  }
}