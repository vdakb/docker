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

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   AuthenticationException.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AuthenticationException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.jacc;

////////////////////////////////////////////////////////////////////////////////
// class AuthenticationException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** An exception thrown to indicate that an {@link Authenticator} is
 ** <b>unable</b> to check the validity of the given credentials.
 ** <p>
 ** <b>DO NOT USE THIS TO INDICATE THAT THE CREDENTIALS ARE INVALID.</b>
 */
public class AuthenticationException extends Exception {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5831461100637785702")
  private static final long serialVersionUID = 3693692177136700479L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AuthenticationException</code> with the specified
   ** detail message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the detail message (which is saved for later
   **                            retrieval by the {@link #getMessage()} method).
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public AuthenticationException(final String message) {
    // ensure inheritance
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AuthenticationException</code> with the specified cause
   ** and a detail message of
   ** <code>(causing==null ? null : causing.toString())</code> (which typically
   ** contains the class and detail message of <code>causing</code>).
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public AuthenticationException(final Throwable causing) {
    // ensure inheritance
    super(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AuthenticationException</code> the specified detail
   ** message and cause.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>causing</code> is <b>not</b>
   ** automatically incorporated in this exception's detail message.
   **
   ** @param  message            the detail message (which is saved for later
   **                            retrieval by the {@link #getMessage()} method).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public AuthenticationException(final String message, final Throwable causing) {
    // ensure inheritance
    super(message, causing);
  }
}