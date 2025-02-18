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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Minimalistic JSON Parser

    File        :   JsonParserException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonParserException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.json.simple;

////////////////////////////////////////////////////////////////////////////////
// class JsonParserException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** An unchecked exception to indicate that an input does not qualify as valid
 ** JSON.
 ** <br>
 ** <code>JsonParserException</code> explains why and where the error occurs in
 ** source JSON text.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JsonParserException extends RuntimeException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final int   UNEXPECTED_CHAR      = 0;
  public static final int   UNEXPECTED_TOKEN     = 1;
  public static final int   UNEXPECTED_EXCEPTION = 2;
  public static final int   UNEXPECTED_EOF       = 3;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5609525338287536204")
  private static final long serialVersionUID     = -9169896976098102414L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final long line;
  private final long column;
  private final long offset;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonParserException</code> with the specified
   ** detail message.
   ** <br>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  offset             the absolute index of the character at which
   **                            the error or warning occurred.
   ** @param  line               the line number of the end of the text that
   **                            caused the error or warning.
   ** @param  column             the column number of the end of the text that
   **                            caused the error or warning.
   ** @param  subject            the detail message. The detail message is saved
   **                            for later retrieval by the
   **                            {@link #getMessage()} method.
   */
  JsonParserException(final long offset, final long line, final long column, int errorType, final Object subject) {
    // ensure inheritance
    super(toMessage(line, column, errorType, subject));

    // initialize instance attributes
    this.offset = offset;
    this.line   = line;
    this.column = column;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonParserException</code> with the specified
   ** detail message.
   ** <br>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  offset             the absolute index of the character at which
   **                            the error or warning occurred.
   ** @param  line               the line number of the end of the text that
   **                            caused the error or warning.
   ** @param  column             the column number of the end of the text that
   **                            caused the error or warning.
   ** @param  cause              the causing exception.
   */
  public JsonParserException(final long offset, final long line, final long column, final Throwable cause) {
    // ensure inheritance
		super(toMessage(line, column, UNEXPECTED_EXCEPTION, cause), cause);

    // initialize instance
    this.offset = offset;
    this.line   = line;
    this.column = column;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   offset
  /**
   ** Returns the absolute index of the character at which the error occurred.
   ** <p>
   ** If possible, the JSON parser should provide the line position of the first
   ** character after the text associated with the character stream.
   ** <br>
   ** The index of the first character of a document is 0.
   **
   ** @return                    the character offset at which the error
   **                            occurred, will be &gt;= 0, or -1 if none is
   **                            available.
   */
  public long offset() {
    return this.offset;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   line
  /**
   ** Return the line number in which the error occurred.
   ** <br>
   ** Lines are delimited by line ends.
   ** <p>
   ** <strong>Warning:</strong> The return value from the method is intended
   ** only as an approximation for the sake of diagnostics; it is not intended
   ** to provide sufficient information to edit the character content of the
   ** original JSON payload. In some cases, these "line" numbers match what
   ** would be displayed as columns, and in others they may not match the source
   ** text.
   ** <p>
   ** The return value is an approximation of the line number in the character
   ** stream or external parsed entity.
   ** <p>
   ** If possible, the JSON parser should provide the line position of the first
   ** character after the text associated with the character stream.
   ** <br>
   ** The first line counts as 1.
   **
   ** @return                    the line in which the error occurred, will be
   **                            &gt;= 1, or -1 if none is available.
   */
  public long line() {
    return this.line;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Return the index of the character at which the error occurred.
   ** <br>
   ** This is zero-based number of Java <code>char</code> values since the last
   ** line end.
   ** <p>
   ** <strong>Warning:</strong> The return value from the method is intended
   ** only as an approximation for the sake of diagnostics; it is not intended
   ** to provide sufficient information to edit the character content of the
   ** original chracter stream.
   ** <br>
   ** For example, when lines contain combining character sequences, wide
   ** characters, surrogate pairs, or bi-directional text, the value may not
   ** correspond to the column in a text editor's display.
   ** <p>
   ** The return value is an approximation of the column number in the character
   ** stream or external parsed entity.
   ** <p>
   ** If possible, the JSON parser should provide the line position of the first
   ** character after the text associated with the character stream.
   ** <br>
   ** The index of the first character of a line is 0.
   **
   ** @return                    the column in which the error occurred, will be
   **                            &gt;= 0, or -1 if none is available.
   */
  public long column() {
    return this.column;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toMessage
  private static String toMessage(final long line, final long column, final int errorType, final Object subject) {
    final StringBuilder sb = new StringBuilder();
    if (errorType == UNEXPECTED_CHAR) {
      sb.append("Unexpected character (");
      sb.append(subject);
      sb.append(") at position ");
      sb.append(line);
      sb.append(":");
      sb.append(column);
      sb.append(".");
    }
    else if (errorType == UNEXPECTED_TOKEN) {
      sb.append("Unexpected token at position ");
      sb.append(line);
      sb.append(":");
      sb.append(column);
      sb.append(". ");
      sb.append(subject);
      sb.append(" expected.");
    }
    else if (errorType == UNEXPECTED_EXCEPTION) {
      sb.append("Unexpected exception ");
      sb.append(subject);
      sb.append(" occur at position ");
      sb.append(line);
      sb.append(":");
      sb.append(column);
      sb.append(".");
    }
    else if (errorType == UNEXPECTED_EOF) {
      sb.append("Unexpected end of input at position ");
      sb.append(line);
      sb.append(":");
      sb.append(column);
      sb.append(" ");
      sb.append(subject);
    }
   else {
      sb.append("Unkown error at position ");
      sb.append(line);
      sb.append(":");
      sb.append(column);
      sb.append(".");
    }
    return sb.toString();
  }
}