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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   ServiceException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation;

import java.net.URI;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;

import java.security.cert.CertPathBuilderException;

import javax.net.ssl.SSLHandshakeException;

import javax.ws.rs.ProcessingException;

import oracle.hst.foundation.SystemException;

import oracle.iam.system.simulation.resource.ServiceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ServiceException
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>SCIM</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceException extends SystemException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:849278999123140034")
  private static final long serialVersionUID = 8155176648275957052L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServiceException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public ServiceException(String code) {
    // ensure inheritance
    super(ServiceBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServiceException</code> from a resource bundle code.
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
  public ServiceException(final String code, final String parameter) {
    // ensure inheritance
    super(ServiceBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServiceException</code> from a code and a array with
   ** values for the placeholder contained in the resource string retrieved for
   ** the specified resource code.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}s.
   */
  public ServiceException(final String code, final String... parameter) {
    // ensure inheritance
    super(ServiceBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServiceException</code> and passes it the causing
   ** exception.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public ServiceException(final Throwable causing) {
    // ensure inheritance
    this(ServiceError.UNHANDLED, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServiceException</code> from a code and a causing
   ** exception.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public ServiceException(final String code, final Throwable causing) {
    // ensure inheritance
    super(ServiceBundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServiceException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public ServiceException(final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(ServiceBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServiceException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  public ServiceException(final String code, final Throwable causing, final String... parameter) {
    // ensure inheritance
    super(ServiceBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new <code>ServiceException</code> from the
   ** specified HTTP response status.
   **
   ** @param  status             the HTTP response status.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the {@link ServiceException} warpping the HTTP
   **                            response status as the code for the exception.
   */
  public static ServiceException build(final int status) {
    switch (status) {
      case 400 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_400);
      case 401 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_401);
      case 403 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_403);
      case 404 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_404);
      case 408 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_408);
      case 500 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_500);
      case 501 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_501);
      case 502 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_502);
      case 503 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_503);
      case 504 : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_504);
      default  : return new ServiceException(ServiceError.HTTP_RESPONSE_CODE_NOP, String.valueOf(status));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convert a JAX-RS @link ProcessingException} to a {@link ServiceException}.
   **
   ** @param  root               the {@link ProcessingException} as the root
   **                            cause.
   ** @param  uri                the {@link URI} as the origin of the exception.
   **
   ** @return                    the converted {@link ServiceException}.
   */
  public static ServiceException build(final ProcessingException root, final URI uri) {
    // investigate the exception further to be able to send back the real
    // reason why the request failed
    Throwable cause = root;
    while(cause.getCause() != null) {
      cause = cause.getCause();
    }
    if (UnknownHostException.class.isInstance(cause)) {
      return new ServiceException(ServiceError.CONNECTION_UNKNOWN_HOST, uri.getHost());
    }
    else if (SocketException.class.isInstance(cause)) {
      return new ServiceException(ServiceError.CONNECTION_CREATE_SOCKET, String.valueOf(uri.getPort()));
    }
    else if (SocketTimeoutException.class.isInstance(cause)) {
      return new ServiceException(ServiceError.CONNECTION_TIMEOUT, uri.getHost());
    }
    else if (SSLHandshakeException.class.isInstance(cause)) {
      return new ServiceException(ServiceError.CONNECTION_SECURE_SOCKET, uri.getHost());
    }
    else if (CertPathBuilderException.class.isInstance(cause)) {
      return new ServiceException(ServiceError.CONNECTION_CERTIFICATE_HOST, uri.getHost());
    }
    else
      return new ServiceException(ServiceError.UNHANDLED, root);
  }
}