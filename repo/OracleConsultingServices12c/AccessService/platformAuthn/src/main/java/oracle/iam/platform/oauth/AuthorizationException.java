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

    System      :   Oracle Access Manager OAuth Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AuthorizationException.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AuthorizationException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth;

import java.io.InputStream;
import java.io.IOException;

import java.net.URI;
import java.net.SocketException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;

import java.security.cert.CertPathBuilderException;

import javax.json.Json;

import javax.json.stream.JsonParser;

import javax.net.ssl.SSLHandshakeException;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.ResponseProcessingException;

import oracle.hst.platform.core.SystemException;

////////////////////////////////////////////////////////////////////////////////
// class AuthorizationException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>Authorization Service</code>
 ** operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AuthorizationException extends SystemException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7829017819657389291")
  private static final long serialVersionUID = 2705562286137586262L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AuthorizationException</code> from a resource bundle
   ** code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for the placeholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  private AuthorizationException(final String code, final String... parameter) {
    // ensure inheritance
    super(AuthorizationBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Convert a JAX-RS @link ProcessingException} to a
   ** <code>SystemException</code>.
   **
   ** @param  root               the {@link ProcessingException} as the root
   **                            cause.
   **                            <br>
   **                            Allowed object is {@link Exception}.
   ** @param  uri                the {@link URI} as the origin of the exception.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the converted <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException from(final ProcessingException root, final URI uri) {
    // investigate the exception further to be able to send back the real
    // reason why the request failed
    Throwable cause = root;
    while(cause.getCause() != null) {
      cause = cause.getCause();
    }
    if (UnknownHostException.class.isInstance(cause)) {
      return SystemException.unknownHost(uri.getHost());
    }
    else if (ConnectException.class.isInstance(cause)) {
      return SystemException.unavailable(cause);
    }
    else if (SocketException.class.isInstance(cause)) {
      return SystemException.createSocket(uri.getHost(), uri.getPort());
    }
    else if (SocketTimeoutException.class.isInstance(cause)) {
      return SystemException.timedOut(uri.getHost());
    }
    else if (SSLHandshakeException.class.isInstance(cause)) {
      return SystemException.createSocketSecure(uri.getHost(), uri.getPort());
    }
    else if (CertPathBuilderException.class.isInstance(cause)) {
      return SystemException.certificatePath(uri.getHost());
    }
    else
      return SystemException.unhandled(root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Convert a JAX-RS response to a <code>SystemException</code>.
   **
   ** @param  response           the JAX-RS response.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @return                    the converted <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException from(final Response response) {
    final Error error  = new Error(response.getStatus());
    InputStream stream = response.readEntity(InputStream.class);
    try {
      final JsonParser parser = Json.createParser(stream);
      try {
        while(parser.hasNext()) {
          String           name  = null;
          String           value = null;
          JsonParser.Event event = parser.next();
          if (event == JsonParser.Event.KEY_NAME) {
            name  = parser.getString();
            event = parser.next();
            if (event == JsonParser.Event.VALUE_STRING)
              value = parser.getString();
              switch (name) {
                // default SCIM error keyword
                case Error.CODE   : error.code(value);
                                    break;
                // default message property
                case Error.DETAIL : error.detail(value);
                                    break;
            }
          }
        }
      }
      finally {
        if (stream != null) {
          stream.close();
         }
         parser.close();
      }
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    return from(error);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Convert a JAX-RS response to a <code>SystemException</code>.
   **
   ** @param  error              the OAuth Error response.
   **                            <br>
   **                            Allowed object is {@link Error}.
   **
   ** @return                    the converted
   **                            <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException from(final Error error) {
    // if are able to read an error response, use it to build the exception.
    switch (error.status()) {
      // fallback to a generalized exception if nothing else could be discovered
      case 400 : return invalidValue(error.detail());
      case 404 : return notFound(error.detail());
      case 406 : return notAcceptable(error.detail());
      case 415 : return mediaTypeUnsupported(error.detail());
      case 500 : return unexpected(error.detail());
      case 501 : return unavailable(error.detail());
      default  : return abort(error.detail());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unexpected
  /**
   ** Factory method to create a new <code>ç</code> with
   ** the {@link AuthorizationError#PROCESS_UNEXPECTED} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  parameter          the substitutions for the placeholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the <code>AuthorizationException</code>
   **                            wrapping the HTTP-500 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthorizationException</code>.
   */
  public static AuthorizationException unexpected(final String parameter) {
    return build(AuthorizationError.PROCESS_UNEXPECTED, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unavailable
  /**
   ** Factory method to create a new <code>AuthorizationException</code> with
   ** the {@link AuthorizationError#PROCESS_UNAVAILABLE} error keyword.
   ** <p>
   ** The server is unable to handle the request due to temporary overloading or
   ** maintenance of the server.
   ** <br>
   ** The Service Provider OAuth API service is not currently running.
   **
   ** @param  parameter          the substitutions for the placeholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the <code>AuthorizationException</code>
   **                            wrapping the HTTP-500 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthorizationException</code>.
   */
  public static AuthorizationException unavailable(final String parameter) {
    return new AuthorizationException(AuthorizationError.PROCESS_UNAVAILABLE, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notFound
  /**
   ** Factory method to create a new <code>AuthorizationException</code> with
   ** the {@link AuthorizationError#PROCESS_EXISTS_NOT} error keyword.
   ** <p>
   ** Specified resource (e.g., User) or endpoint does not exist.
   **
   ** @param  parameter          the substitutions for the placeholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the <code>AuthorizationException</code>
   **                            wrapping the HTTP-404 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthorizationException</code>.
   */
  public static AuthorizationException notFound(final String parameter) {
    return build(AuthorizationError.PROCESS_EXISTS_NOT, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notAcceptable
  /**
   ** Factory method to create a new <code>AuthorizationException</code> with
   ** the {@link AuthorizationError#PROCESS_NOT_ACCEPTABLE} error keyword.
   ** <p>
   ** The Service Provider cannot produce a response matching the list of
   ** acceptable values defined in the request's proactive content negotiation
   ** headers, and that the Service Provider is unwilling to supply a default
   ** representation.
   ** <br>
   ** The corresponding HTTP status is <code>406</code>
   **
   ** @param  parameter          the substitutions for the placeholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the <code>AuthorizationException</code>
   **                            wrapping the HTTP-406 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static AuthorizationException notAcceptable(final String... parameter) {
    return build(AuthorizationError.PROCESS_NOT_ACCEPTABLE, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mediaTypeUnsupported
  /**
   ** Factory method to create a new <code>AuthorizationException</code> with
   ** the {@link AuthorizationError#PROCESS_MEDIATYPE_UNSUPPORTED} error
   ** keyword.
   ** <p>
   ** The Service Provider refuses to accept the request because the payload
   ** format is in an unsupported format.
   ** <br>
   ** The corresponding HTTP status is <code>415</code>
   **
   ** @param  parameter          the substitutions for the placeholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the <code>AuthorizationException</code>
   **                            wrapping the HTTP-415 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthorizationException</code>.
   */
  public static AuthorizationException mediaTypeUnsupported(final String... parameter) {
    return build(AuthorizationError.PROCESS_MEDIATYPE_UNSUPPORTED, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidValue
  /**
   ** Factory method to create a new <code>AuthorizationException</code> with
   ** the {@link AuthorizationError#PROCESS_INVALID_VALUE} error keyword.
   ** <p>
   ** A required value was missing, or the value specified was not compatible
   ** with the operation or attribute type (see Section 2.2 of [
   ** <a href="datatracker.ietf.org/doc/html/rfc7643">RFC7643</a>), or
   ** resource schema (see
   ** <a href="datatracker.ietf.org/doc/html/rfc7644#section-4">Section 4</a>
   ** of [<a href="datatracker.ietf.org/doc/html/rfc7643">RFC7643</a>)
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  parameter          the substitutions for the placeholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static AuthorizationException invalidValue(final String... parameter) {
    return build(AuthorizationError.PROCESS_INVALID_VALUE, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new <code>AuthorizationException</code> from a
   ** resource bundle code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for the placeholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the <code>AuthorizationException</code>
   **                            wrapping the HTTP-415 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>AuthorizationException</code>.
   */
  public static AuthorizationException build(final String code, final String... parameter) {
    return new AuthorizationException(code, parameter);
  }
}