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

    File        :   SecurityException.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SecurityException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform;

////////////////////////////////////////////////////////////////////////////////
// final class SecurityException
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** General unchecked exception thrown when any errors are raised during
 ** encryption/decryption, etc.
 ** <p>
 ** It is intended to provide very little information (if any) of the error
 ** causes, so that encryption/decryption internals are not revealed through
 ** error messages.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SecurityException extends java.lang.SecurityException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:281248092067194560")
  private static final long serialVersionUID = -4314741352632098412L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SecurityException</code> with <code>null</code> as
   ** its detail message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   ** <p>
   ** Default Constructor
   */
  protected SecurityException() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SecurityException</code> with <code>reason</code> as
   ** its detail message.
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
  protected SecurityException(final String reason) {
    // ensure inheritance
    super(reason);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SecurityException</code> and passes it the causing
   ** exception.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SecurityException(final Throwable cause) {
    // ensure inheritance
    super(cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SecurityException</code> with <code>reason</code>
   ** as its detail message and passes it the causing exception.
   **
   ** @param  reason             the detail message.
   **                            <br>
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SecurityException(final String reason, final Throwable cause) {
    // ensure inheritance
    super(reason, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simpleFailed
  /**
   ** Factory method to create a new <code>ServiceException</code> without any
   ** further information about the reason.
   ** <p>
   ** If some crirical operations like encryption fails, it is more secure not
   ** to return any information about the cause in nested exceptions; just
   ** simply fail.
   **
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException simpleFailed() {
    return new SecurityException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simpleFailed
  /**
   ** Factory method to create a new <code>ServiceException</code> without any
   ** further information about the reason.
   ** <p>
   ** If some crirical operations like encryption fails, it is more secure not
   ** to return any information about the cause in nested exceptions; just
   ** simply fail.
   **
   ** @param  reason             the detail message.
   **                            <br>
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException simpleFailed(final String reason) {
    return new SecurityException(reason);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simpleFailed
  /**
   ** Factory method to create a new <code>ServiceException</code> with just a
   ** causing exception.
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
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException simpleFailed(final Throwable cause) {
    return new SecurityException(cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encryptionFatal
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link SecurityError#ENCRYPTION_FATAL} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException encryptionFatal() {
    return new SecurityException(SecurityBundle.string(SecurityError.ENCRYPTION_FATAL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwortFatal
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link SecurityError#PASSWORD_FATAL} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException passwortFatal() {
    return new SecurityException(SecurityBundle.string(SecurityError.PASSWORD_FATAL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordHandler
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link SecurityError#PASSWORD_HANDLER} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException passwordHandler() {
    return new SecurityException(SecurityBundle.string(SecurityError.PASSWORD_HANDLER));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requireNonNull
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link SecurityError#ARGUMENT_IS_NULL} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  argument           the literal name of the missing aurgement.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException requireNonNull(final String argument) {
    return new SecurityException(SecurityBundle.string(SecurityError.ARGUMENT_IS_NULL, argument));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generatePublicKey
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link SecurityError#PUBLICKEY_FATAL} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
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
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException generatePublicKey(final Throwable cause) {
    return new SecurityException(SecurityBundle.string(SecurityError.PUBLICKEY_FATAL, cause.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generatePublicKey
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link SecurityError#PRIVATEKEY_FATAL} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
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
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException generatePrivateKey(final Throwable cause) {
    return new SecurityException(SecurityBundle.string(SecurityError.PRIVATEKEY_FATAL, cause.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateCertificate
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link SecurityError#CERTIFICATE_FATAL} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
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
   ** @return                    the <code>SecurityException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityException</code>.
   */
  public static SecurityException generateCertificate(final Throwable cause) {
    return new SecurityException(SecurityBundle.string(SecurityError.CERTIFICATE_FATAL, cause.getLocalizedMessage()));
  }
}