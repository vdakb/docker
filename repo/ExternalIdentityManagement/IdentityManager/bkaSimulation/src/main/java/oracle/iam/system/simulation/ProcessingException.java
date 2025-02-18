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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   ProcessingException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProcessingException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation;

import oracle.iam.system.simulation.rest.domain.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class ProcessingException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>SCIM</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProcessingException extends ServiceException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1465104488581986713")
  private static final long serialVersionUID = 996424371363001259L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ErrorResponse error;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessingException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  status             the HTTP response status.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  type               the SCIM detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the error message for this SCIM exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  ProcessingException(final int status, final String type, final String message) {
    // ensure inheritance
    super(fromStatus(status));

    // initialize instance attributes
    this.error = new ErrorResponse(status).type(type).detail(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessingException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  status             the HTTP response status.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  type               the SCIM detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the error message for this SCIM exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  ProcessingException(final int status, final String type, final String message, final Throwable cause) {
    // ensure inheritance
    super(fromStatus(status), cause);

    // initialize instance attributes
    this.error = new ErrorResponse(status).type(type).detail(String.format("%s\n%s", message, cause.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessingException</code> from an error response.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  error              the error response.
   **                            <br>
   **                            Allowed object is {@link ErrorResponse}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  ProcessingException(final ErrorResponse error, final Throwable cause) {
    // ensure inheritance
    super(fromStatus(error.status()), cause);

    // initialize instance attributes
    this.error = error;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Returns the error for this exception.
   **
   ** @return                    the error for this exception.
   **                            <br>
   **                            Possible object is {@link ErrorResponse}.
   */
  public final ErrorResponse error() {
    return this.error;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromStatus
  /**
   ** Factory method to convert a <code>status</code> to a code.
   **
   ** @param  status             the HTTP response status.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the code for the exception.
   */
  private static String fromStatus(final int status) {
    switch (status) {
      case 400 : return ServiceError.HTTP_RESPONSE_CODE_400;
      case 401 : return ServiceError.HTTP_RESPONSE_CODE_401;
      case 403 : return ServiceError.HTTP_RESPONSE_CODE_403;
      case 404 : return ServiceError.HTTP_RESPONSE_CODE_404;
      case 408 : return ServiceError.HTTP_RESPONSE_CODE_408;
      case 500 : return ServiceError.HTTP_RESPONSE_CODE_500;
      case 501 : return ServiceError.HTTP_RESPONSE_CODE_501;
      case 502 : return ServiceError.HTTP_RESPONSE_CODE_502;
      case 503 : return ServiceError.HTTP_RESPONSE_CODE_503;
      case 504 : return ServiceError.HTTP_RESPONSE_CODE_504;
      default  : return ServiceError.HTTP_RESPONSE_CODE_NOP;
    }
  }
}