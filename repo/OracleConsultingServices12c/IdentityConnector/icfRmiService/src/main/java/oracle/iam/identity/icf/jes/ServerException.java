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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Java Enterprise Service Connector Library

    File        :   ServerException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServerException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.jes;

import javax.security.auth.login.LoginException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.resource.ServerBundle;

////////////////////////////////////////////////////////////////////////////////
// class ServerException
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>ServerEndpoint</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerException extends SystemException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String MALFORMED        = "java.net.MalformedURLException: ";
  private static final String UNKNOWNHOST      = "java.net.UnknownHostException: ";
  private static final String CONNECTION       = "java.net.ConnectException: ";
  private static final String ROUTER           = "No available router to destination.";
  private static final String SECURITY         = "javax.security.auth.login.LoginException: java.lang.SecurityException: ";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8947979135321928349")
  private static final long   serialVersionUID = 3564847407949405351L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServerException</code> from a resource bundle code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for the placholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private ServerException(final String code, final String parameter) {
    // ensure inheritance
    super(ServerBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverTypeUnsupported
  /**
   ** Factory method to create a new <code>ServerException</code> with the
   ** {@link ServerError#CONNECTION_SERVERTYPE_UNSUPPORTED} error keyword.
   **
   ** @param  type               the type of the JEE  Server violating the
   ** constraints.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServerException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServerException</code>.
   */
  public static ServerException serverTypeUnsupported(final String type) {
    return new ServerException(ServerError.CONNECTION_SERVERTYPE_UNSUPPORTED, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverTypeUnsupported
  /**
   ** Factory method to create a new <code>ServerException</code> with the
   ** {@link ServerError#CONNECTION_LOGINCONFIG_NOTFOUND} error keyword.
   **
   ** @param  path               the path to the JAAS login module raising the
   **                            issue.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServerException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServerException</code>.
   */
  public static ServerException loginConfiguration(final String path) {
    return new ServerException(ServerError.CONNECTION_LOGINCONFIG_NOTFOUND, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessDenied
  /**
   ** Factory method to create a new <code>ServerException</code> with the
   ** {@link ServerError#CONTEXT_ACCESS_DENIED} error keyword.
   ** <p>
   ** Specified resource (e.g., User) or endpoint does not exist.
   **
   ** @param  message            the error message for this RMI exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServerException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServerException</code>.
   */
  public static ServerException accessDenied(final String message) {
    return new ServerException(ServerError.CONTEXT_ACCESS_DENIED, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notFound
  /**
   ** Factory method to create a new <code>ServerException</code> with the
   ** {@link ServerError#PROCESS_NOT_EXISTS} error keyword.
   ** <p>
   ** Specified resource (e.g., User) or endpoint does not exist.
   **
   ** @param  message            the error message for this RMI exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServerException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServerException</code>.
   */
  public static ServerException notFound(final String message) {
    return new ServerException(ServerError.PROCESS_NOT_EXISTS, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidFilter
  /**
   ** Factory method to create a new <code>ServerException</code> with the
   ** {@link ServerError#PROCESS_INVALID_FILTER} error keyword.
   ** <p>
   ** The specified filter syntax was invalid or the specified attribute and
   ** filter comparison combination is not supported.
   **
   ** @param  message            the error message for this RMI exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServerException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServerException</code>.
   */
  public static ServerException invalidFilter(final String message) {
    return new ServerException(ServerError.PROCESS_INVALID_FILTER, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Convert a JDBS @link SQLRecoverableException} to a
   ** <code>SystemException</code>.
   **
   ** @param  root               the {@link Exception} as the root cause.
   **                            <br>
   **                            Allowed object is {@link LoginException}.
   ** @param  endpoint           the {@link ServerEndpoint} as the origin of
   **                            the exception.
   **                            <br>
   **                            Allowed object is {@link ServerEndpoint}.
   **
   ** @return                    the converted <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException from(final LoginException root, final ServerEndpoint endpoint) {
    // investigate the exception further to be able to send back the real reason
    // why the request failed
    Throwable embedded = root;
    while (embedded.getCause() != null) {
      embedded = embedded.getCause();
    }

    final String message = root.getMessage();
    if (message.startsWith(MALFORMED))
      return SystemException.abort(root);
    else if (message.startsWith(UNKNOWNHOST))
      return SystemException.unknownHost(endpoint.primaryHost());
    else if (message.startsWith(CONNECTION))
      if (message.contains(ROUTER))
        return endpoint.secureSocket()
             ? SystemException.createSocketSecure(endpoint.primaryHost(), endpoint.primaryPort())
             : SystemException.createSocket(endpoint.primaryHost(), endpoint.primaryPort())
        ;
      else
        return SystemException.unavailable(root);
    else if (message.startsWith(SECURITY))
      return SystemException.authentication(endpoint.principalUsername());
    else
      return SystemException.unhandled(root);
  }
}