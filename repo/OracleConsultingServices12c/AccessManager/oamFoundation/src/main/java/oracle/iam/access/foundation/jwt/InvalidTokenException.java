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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   InvalidTokenException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    InvalidTokenException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

////////////////////////////////////////////////////////////////////////////////
// class InvalidTokenException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception throws if the JSON Web Token could not be parsed properly. It does
 ** not conform to the JSON Web Token specification.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class InvalidTokenException extends TokenException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2944008313521155980")
  private static final long serialVersionUID = 8627156155780392696L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new JSON Web Token Exception with the specified detail
   ** message. The cause is not initialized, and may subsequently be
   ** initialized by a call to {@link #initCause}.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later
   **                            retrieval by the {@link #getMessage()} method.
   */
  public InvalidTokenException(final String message) {
    // ensure inheritance
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new JSON Web Token Exception with the specified detail
   ** message and cause.
   ** <p>
   ** Note that the detail message associated with <code>cause</code> is
   ** <i>not</i> automatically incorporated in this runtime exception's detail
   ** message.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later
   **                            retrieval by the {@link #getMessage()} method.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   */
  public InvalidTokenException(final String message, final Throwable cause) {
    // ensure inheritance
    super(message, cause);
  }
}