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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic SCIM Interface

    File        :   ServerErrorException.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServerErrorException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim;

import oracle.iam.platform.scim.response.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class ServerErrorException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>SCIM</code> operations.
 ** <br>
 ** Signals an error while looking up resources and attributes.
 ** <p>
 ** This exception corresponds to HTTP response code 500 INTERNAL SERVER ERROR.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerErrorException extends ProcessingException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The error keyword that indicates something went wrong.
   */
  private static final String INTERNAL         = "internal";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4550237225591080395")
  private static final long   serialVersionUID = 3463391907095474443L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServerErrorException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the error message for this SCIM exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private ServerErrorException(final String message) {
    // ensure inheritance
    super(500, INTERNAL, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServerErrorException</code> from an error response.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  error              the error response.
   **                            <br>
   **                            Allowed object is {@link ErrorResponse}.
   */
  private ServerErrorException(final ErrorResponse error) {
    // ensure inheritance
    super(error);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Factory method to create a new <code>ServerErrorException</code> with the
   ** internal error keyword.
   **
   ** @param  message            the error message for this SCIM exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServerErrorException</code> wrapping
   **                            the HTTP-500 response status.
   **                            Possible object is
   **                            <code>ServerErrorException</code>.
   */
  public static ServerErrorException internal(final String message) {
    return new ServerErrorException(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Factory method to create a new <code>ServerErrorException</code> with the
   ** internal error keyword.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>ServerErrorException</code> wrapping
   **                            the HTTP-500 response status.
   **                            Possible object is
   **                            <code>ServerErrorException</code>.
   */
  public static ServerErrorException internal(final Throwable cause) {
    cause.printStackTrace(System.err);
    return new ServerErrorException(cause.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Factory method to create a new <code>ServerErrorException</code> with the
   ** internal error keyword.
   **
   ** @param  error              the error response.
   **                            <br>
   **                            Allowed object is {@link ErrorResponse}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>ServerErrorException</code> wrapping
   **                            the HTTP-500 response status.
   **                            Possible object is
   **                            <code>ServerErrorException</code>.
   */
  public static ServerErrorException internal(final ErrorResponse error, final Throwable cause) {
    cause.printStackTrace(System.err);
    return new ServerErrorException(error);
  }
}