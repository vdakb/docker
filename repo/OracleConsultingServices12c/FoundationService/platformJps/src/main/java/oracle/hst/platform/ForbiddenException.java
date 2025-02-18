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

    System      :   Oracle Security Foundation Library
    Subsystem   :   Common shared runtime facilities

    File        :   ForbiddenException.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ForbiddenException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform;

////////////////////////////////////////////////////////////////////////////////
// final class ForbiddenException
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** An exception that should be thrown (or otherwise returned to the client for
 ** async implementations) by {@code Provider} implementations if the
 ** authorization failed.
 ** <p>
 ** This can be used by a mechanism to determine if an authorization failure
 ** was due to missing permissions vs some other form of internal failure.
 */
public class ForbiddenException extends SecurityException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7622792567182059241")
  private static final long serialVersionUID = -1574225205678678052L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ForbiddenException</code> with <code>null</code> as
   ** its detail message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   ** <p>
   ** Default Constructor
   */
  public ForbiddenException() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ForbiddenException</code> with <code>reason</code>
   ** as its detail message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  reason             the detail message.
   **                            <br>
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public ForbiddenException(final String reason) {
    // ensure inheritance
    super(reason);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ForbiddenException</code> and passes it the causing
   ** exception.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public ForbiddenException(final Throwable causing) {
    // ensure inheritance
    super(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ForbiddenException</code> with <code>reason</code>
   ** as its detail message and passes it the causing exception.
   **
   ** @param  reason             the detail message.
   **                            <br>
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public ForbiddenException(final String reason, final Throwable causing) {
    // ensure inheritance
    super(reason, causing);
  }
}