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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   DirectoryException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryException.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.service;

import javax.naming.NamingException;
import javax.naming.InvalidNameException;
import javax.naming.CommunicationException;
import javax.naming.AuthenticationException;
import javax.naming.ServiceUnavailableException;

import oracle.jdeveloper.connection.iam.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryException
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>DirectoryService</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryException extends EndpointException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7445116167205424856")
  private static final long serialVersionUID = 7247827520858224786L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public DirectoryException(final String message) {
    // ensure inheritance
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> from a code and passes it the
   ** causing exception.
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
  public DirectoryException(final String message, final Throwable causing) {
    // ensure inheritance
    super(message, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link CommunicationException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link CommunicationException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link CommunicationException}.
   */
  public DirectoryException(final CommunicationException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_CONNECTION, throwable(causing)), throwable(causing));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link AuthenticationException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link AuthenticationException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AuthenticationException}.
   ** @param  principalName      the administrative user name cauisng the
   **                            exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public DirectoryException(final AuthenticationException causing, final String principalName) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_AUTHENTICATION, principalName), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link ServiceUnavailableException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link ServiceUnavailableException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link ServiceUnavailableException}.
   */
  public DirectoryException(final ServiceUnavailableException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_UNAVAILABLE, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link InvalidNameException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link NamingException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link InvalidNameException}.
   */
  public DirectoryException(final InvalidNameException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link NamingException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link NamingException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link NamingException}.
   */
  public DirectoryException(final NamingException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_UNHANDLED, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwable
  private static Throwable throwable(final Throwable throwable) {
    if (throwable.getCause() == null)
      return throwable;

    return throwable(throwable.getCause());
  }
}