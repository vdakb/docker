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
    Subsystem   :   Generic REST Library

    File        :   ForbiddenException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ForbiddenException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest;

////////////////////////////////////////////////////////////////////////////////
// class ForbiddenException
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>REST</code> operations.
 ** <br>
 ** Signals the server does not support the requested operation.
 ** <p>
 ** This exception corresponds to HTTP response code 403 FORBIDDEN.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ForbiddenException extends ProcessingException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The error keyword that indicates something is not permitted.
   */
  private static final String TYPE             = "notPermitted";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5014962921309429827")
  private static final long   serialVersionUID = 8671890639806069630L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ForbiddenException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private ForbiddenException(final String message) {
    // ensure inheritance
    super(403, TYPE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notPermitted
  /**
   ** Factory method to create a new <code>ForbiddenException</code> with the
   ** notPermitted error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ForbiddenException</code> wrapping
   **                            the HTTP-403 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ForbiddenException</code>.
   */
  public static ForbiddenException notPermitted(final String message) {
    return new ForbiddenException(message);
  }
}