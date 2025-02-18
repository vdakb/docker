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

    File        :   NotFoundException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NotFoundException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation;

import oracle.iam.system.simulation.rest.domain.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class NotFoundException
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>SCIM</code> operations.
 ** <br>
 ** Signals the specified version number does not match the resource's latest
 ** version number or a Service Provider refused to create a new, duplicate
 ** resource.
 ** <p>
 ** This exception corresponds to HTTP response code 404 NOT FOUND.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NotFoundException extends ProcessingException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8500240927099159987")
  private static final long serialVersionUID = 5907046321906841460L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>NotFoundException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the error message for this SCIM exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public NotFoundException(final String message) {
    // ensure inheritance
   super(404, null, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>NotFoundException</code> from a code and a causing
   ** exception.
   **
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
  public NotFoundException(final String type, final String message, final Throwable causing) {
    // ensure inheritance
    super(404, type, message, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>NotFoundException</code> from an error response.
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
  public NotFoundException(final ErrorResponse error, final Throwable causing) {
    // ensure inheritance
    super(error, causing);
  }
}