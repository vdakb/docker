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

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AssertionException.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AssertionException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml;

import java.time.ZonedDateTime;

////////////////////////////////////////////////////////////////////////////////
// class AssertionException
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>Assertion Service</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AssertionException extends Exception {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4518491709254020353")
  private static final long serialVersionUID = -5236900465282994577L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AssertionException</code> from a message and passes it
   ** the causing exception.
   **
   ** @param  message            the detail message.
   **                            <br>
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private AssertionException(final String message) {
    // ensure inheritance
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AssertionException</code> from a code and passes it
   ** the causing exception.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  private AssertionException(final String message, final Throwable causing) {
    // ensure inheritance
    super(message, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unhandled
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#UNHANDLED} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException unhandled(final Throwable causing) {
    return new AssertionException(AssertionBundle.string(AssertionError.UNHANDLED), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#GENERAL} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException general(final Throwable causing) {
    return general(AssertionBundle.string(AssertionError.GENERAL), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#GENERAL} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException general(final String message, final Throwable causing) {
    return new AssertionException(String.format(message, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#ABORT} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException abort(final Throwable causing) {
    return abort(AssertionBundle.string(AssertionError.ABORT), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#ABORT} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException abort(final String message) {
    return new AssertionException(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#ABORT} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException abort(final String message, final Throwable causing) {
    return new AssertionException(String.format(message, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requireNonNull
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#ARGUMENT_IS_NULL} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  argument           the literal name of the missing argument.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException requireNonNull(final String argument) {
    return new AssertionException(AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, argument));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   issuedAfter
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#TOKEN_ISSUED} error keyword.
   ** <p>
   ** The token provided is issued before the current the current datetime.
   **
   ** @param  argument           the literal date value of the violation.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException issuedAfter(final ZonedDateTime argument) {
    return new AssertionException(AssertionBundle.string(AssertionError.TOKEN_ISSUED, AssertionProcessor.TIMEFORMAT.format(argument.withZoneSameInstant(AssertionProcessor.TIMEZONE))));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notAfter
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#TOKEN_NOTAFTER} error keyword.
   ** <p>
   ** The token provided is not valid due to its expired.
   **
   ** @param  argument           the literal name of the missing argument.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException notAfter(final ZonedDateTime argument) {
    return new AssertionException(AssertionBundle.string(AssertionError.TOKEN_NOTAFTER, AssertionProcessor.TIMEFORMAT.format(argument.withZoneSameInstant(AssertionProcessor.TIMEZONE))));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notBefore
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link AssertionError#TOKEN_NOTBEFORE} error keyword.
   ** <p>
   ** The token provided is not valid due to its used before the current
   ** datetime.
   **
   ** @param  argument           the literal name of the missing argument.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AssertionException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AssertionException</code>.
   */
  public static AssertionException notBefore(final ZonedDateTime argument) {
    return new AssertionException(AssertionBundle.string(AssertionError.TOKEN_NOTBEFORE, AssertionProcessor.TIMEFORMAT.format(argument.withZoneSameInstant(AssertionProcessor.TIMEZONE))));
  }
}