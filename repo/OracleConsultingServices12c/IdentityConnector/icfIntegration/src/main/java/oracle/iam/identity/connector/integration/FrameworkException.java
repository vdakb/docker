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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Connector Bundle Integration

    File        :   FrameworkException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FrameworkException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import oracle.iam.identity.foundation.TaskException;

import org.identityconnectors.framework.api.ConnectorKey;

import org.identityconnectors.framework.common.exceptions.InvalidCredentialException;

////////////////////////////////////////////////////////////////////////////////
// class FrameworkException
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>ConnectorFactory</code> if any goes
 ** wrong during bundle introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FrameworkException extends TaskException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5494268081732467792")
  private static final long serialVersionUID = -3221723905434552442L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FrameworkException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  private FrameworkException(final String code, final Object... parameter) {
    // ensure inheritance
    super(FrameworkBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FrameworkException</code> with the specified detail
   ** message and cause. 
   ** <p>
   ** Note that the detail message associated with {@code cause} is <i>not</i>
   ** automatically incorporated in this runtime exception's detail message.
   ** 
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method). (A
   **                            <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  private FrameworkException(final String code, final Throwable cause) {
    // ensure inheritance
    super(FrameworkBundle.RESOURCE, code, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unknownHost
  /**
   ** Factory method to create a new <code>FrameworkException</code> with the
   ** host name keyword.
   **
   ** @param  host               the IP or name of the host which is unknown.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the root cause of the exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link FrameworkException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link FrameworkException}.
   */
  public static FrameworkException unknownHost(final String host, final Throwable cause) {
    return new FrameworkException(FrameworkError.CONNECTION_UNKNOWN_HOST, host, cause.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unavailable
  /**
   ** Factory method to create a new <code>FrameworkException</code> with the
   ** unavailable keyword.
   **
   ** @param  host               the IP or name of the host to connect to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the port the host should listen on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  cause              the root cause of the exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link FrameworkException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link FrameworkException}.
   */
  public static FrameworkException unavailable(final String host, final int port, final Throwable cause) {
    return new FrameworkException(FrameworkError.CONNECTION_NOT_AVAILABLE, host, port, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocket
  /**
   ** Factory method to create a new <code>FrameworkException</code> with the
   ** create socket keyword.
   **
   ** @param  host               the IP or name of the host.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the port the host should listen on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the {@link FrameworkException} wrapping the
   **                            properties provided.
   **                            <br>
   **                            Possible object is {@link FrameworkException}.
   */
  public static FrameworkException createSocket(final String host, final int port) {
    return new FrameworkException(FrameworkError.CONNECTION_CREATE_SOCKET, host, port);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeout
  /**
   ** Factory method to create a new <code>FrameworkException</code> with the
   ** timeout keyword.
   **
   ** @param  host               the IP or name of the host which is timed out.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the port the host should listen on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  cause              the root cause of the exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link FrameworkException} wrapping the
   **                            properties provided.
   **                            <br>
   **                            Possible object is {@link FrameworkException}.
   */
  public static FrameworkException timeout(final String host, final int port, final Throwable cause) {
    return new FrameworkException(FrameworkError.CONNECTION_TIMEOUT, host, port, cause.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authentication
  /**
   ** Factory method to create a new <code>FrameworkException</code> with the
   ** authentication keyword.
   **
   ** @param  cause              the root cause of the exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link InvalidCredentialException}.
   **
   ** @return                    the {@link FrameworkException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link FrameworkException}.
   */
  public static FrameworkException authentication(final InvalidCredentialException cause) {
    return new FrameworkException(FrameworkError.CONNECTION_AUTHENTICATION, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bundleNotFound
  /**
   ** Factory method to create a new <code>FrameworkException</code> with the
   ** bundle unavailable keyword.
   **
   ** @param  token              the bundle token.
   **                            <br>
   **                            Allowed object is {@link ConnectorKey}.
   **
   ** @return                    the {@link FrameworkException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link FrameworkException}.
   */
  public static FrameworkException bundleNotFound(final ConnectorKey token) {
    return new FrameworkException(FrameworkError.CONNECTOR_BUNDLE_NOTFOUND, token);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionRequired
  /**
   ** Factory method to create a new <code>FrameworkException</code> with the
   ** option required keyword.
   **
   ** @param  name               the name of the required option that is missed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link FrameworkException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link FrameworkException}.
   */
  public static FrameworkException optionRequired(final String name) {
    return new FrameworkException(FrameworkError.CONNECTOR_OPTION_REQUIRED, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionMapping
  /**
   ** Factory method to create a new <code>FrameworkException</code> with the
   ** option mapping keyword.
   **
   ** @param  name               the name of the required option that is not
   **                            mapped.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link FrameworkException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link FrameworkException}.
   */
  public static FrameworkException optionMapping(final String name) {
    return new FrameworkException(FrameworkError.CONNECTOR_OPTION_MAPPING, name);
  }
}