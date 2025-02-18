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

    File        :   JsonException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.json.simple;

////////////////////////////////////////////////////////////////////////////////
// class JsonException
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** An unchecked exception to indicates that some exception happened during
 * JSON processing.
 ** <br>
 ** <code>JsonException</code> explains why and where the error occurs in
 ** source JSON text.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JsonException extends RuntimeException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6026745706908922902")
  private static final long serialVersionUID = 6626664508330269681L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new runtime exception with the specified detail message.
   ** <br>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   */
  public JsonException(final String message) {
    // ensure inheritance
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new runtime exception with the specified cause.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   */
  public JsonException(final Throwable cause) {
    // ensure inheritance
    super(cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new runtime exception with the specified detail message and
   ** cause.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** The detail message associated with <code>cause</code> is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   */
  public JsonException(final String message, final Throwable cause) {
    // ensure inheritance
    super(message, cause);
  }
}