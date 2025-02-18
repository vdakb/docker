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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   NotAllowedException.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NotAllowedException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest;

////////////////////////////////////////////////////////////////////////////////
// class NotAllowedException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>REST</code> operations.
 ** <br>
 ** Signals that the HTTP method requested is not allowed for the requested
 ** resource.
 ** <p>
 ** This exception corresponds to HTTP response code 405 METHOD NOT ALLOWED.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NotAllowedException extends ProcessingException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The error keyword that indicates somthing is not permitted.
   */
  private static final String TYPE             = "notAllowed";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-95350032044034822")
  private static final long   serialVersionUID = -1768246460553880435L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>NotAllowedException</code> with the default message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   */
  private NotAllowedException() {
    // ensure inheritance
    this(ServiceBundle.string(ServiceError.REQUEST_METHOD_NOTALLOWED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>NotAllowedException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private NotAllowedException(final String message) {
    // ensure inheritance
    super(405, TYPE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>NotAllowedException</code> with
   ** the not-allowed error keyword.
   **
   ** @return                    the <code>NotAllowedException</code> wrapping
   **                            the HTTP-405 response status.
   **                            Possible object is
   **                            <code>NotAllowedException</code>.
   */
  public static NotAllowedException of() {
    return new NotAllowedException();
  }
}