package oracle.jdeveloper.connection.oim.service;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>IdentityService</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityServiceException extends Exception {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServiceException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   */
  public IdentityServiceException(final String message) {
    // ensure inheritance
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServiceException</code> from a code and passes
   ** it the causing exception.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   */
  public IdentityServiceException(final String message, final Throwable causing) {
    // ensure inheritance
    super(message, causing);
  }
}