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
    Subsystem   :   Generic REST Library

    File        :   ServiceException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest;

import java.io.InputStream;
import java.io.IOException;

import java.net.URI;
import java.net.SocketException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;

import java.security.cert.CertPathBuilderException;

import javax.net.ssl.SSLHandshakeException;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.domain.Error;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

////////////////////////////////////////////////////////////////////////////////
// class ServiceException
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>REST</code> operations.
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
  @SuppressWarnings("compatibility:-95820991442125773")
  private static final long serialVersionUID = 1417701139285505095L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

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
  protected ServiceException(final String code, final String parameter) {
    // ensure inheritance
    super(ServiceBundle.RESOURCE, code, parameter);
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
   ** Convert a JAX-RS response to a <code>ServiceException</code>.
   **
   ** @param  response           the JAX-RS response.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @return                    the converted <code>ServiceException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException from(final Response response) {
    final Error       error  = new Error(response.getStatus());
    final InputStream stream = response.readEntity(InputStream.class);
    try {
      final JsonParser parser = MapperFactory.instance.getFactory().createParser(stream);
      try {
        parser.nextToken();
        boolean stop = false;
        while (!stop && parser.nextToken() != JsonToken.END_OBJECT) {
          final String field = parser.getCurrentName();
          parser.nextToken();
          // skip any suspicious stuff
          if (field != null) {
            switch (field) {
              // PCF use "error_code" as the property name of the error type in
              // a REST error response
              case "error_code"       :
              case "type"             :
              // default SCIM error keyword
              case Error.TYPE         : error.type(Error.Type.from(parser.getValueAsString()));
                                        break;
              // Keycloak use sometimes "error" as the property name of the
              // detailed, human readable message in a REST error response
              // sometimes as "errorMessage"
              case "error"             :
              case "errorMessage"      :
              // PCF use "message" and "error_description" as the property name
              // of the detailed, human readable message in a SCIM error
              // response
              case "message"           :
              case "error_description" :
              // PCF use "description"the property name of the detailed, human
              // readable message in a REST error response
              case "description"       :
              // default message property
              case Error.DETAIL        : error.detail(parser.getValueAsString());
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
   ** @param  error              the REST Error response.
   **                            <br>
   **                            Allowed object is {@link Error}.
   **
   ** @return                    the converted <code>ServiceException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static SystemException from(final Error error) {
    // if are able to read an error response, use it to build the exception.
    switch (error.status()) {
      case 304 : return notModified(error.detail());
      case 400 : return badRequest(error);
      case 401 : return unauthorized(error.detail());
      case 403 : return forbidden(error.detail());
      case 404 : return notFound(error.detail());
      case 405 : return notAllowed(error.detail());
      case 406 : return notAcceptable(error.detail());
      case 409 : return conflict(error.detail());
      case 412 : return preCondition(error.detail());
      case 413 : return tooLarge(error.detail());
      case 415 : return mediaTypeUnsupported(error.detail());
      case 500 : return unexpected(error.detail());
      case 501 : return unavailable(error.detail());
      default  : return abort(error.detail());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unexpected
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_UNEXPECTED} error keyword.
   ** <p>
   ** The server encountered an unexpected condition that prevented it from
   ** fulfilling the request.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-500 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException unexpected(final String message) {
    return new ServiceException(ServiceError.PROCESS_UNEXPECTED, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unavailable
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_UNAVAILABLE} error keyword.
   ** <p>
   ** The server is unable to handle the request due to temporary overloading or
   ** maintenance of the server.
   ** <br>
   ** The Service Provider REST API service is not currently running.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-500 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException unavailable(final String message) {
    return new ServiceException(ServiceError.PROCESS_UNAVAILABLE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   badRequest
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** HTTP-<code>400</code> status.
   ** <p>
   ** Transform REST exception to something more usable.
   **
   ** @param  error              the error details for this exception.
   **                            <br>
   **                            Allowed object is {@link Error}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException badRequest(final Error error) {
    if (error.type() != null) {
      switch(error.type()) {
        case INVALID_VERSION :  return invalidVersion(error.detail());
        case TOO_MANY        :  return tooMany(error.detail());
        case UNIQUENESS      :  return uniqueness(error.detail());
        case MUTABILITY      :  return mutability(error.detail());
        case SENSITIVE       :  return sensitive(error.detail());
        case INVALID_PATH    :  return invalidPath(error.detail());
        case INVALID_FILTER  :  return invalidFilter(error.detail());
        case INVALID_SYNTAX  :  return invalidSyntax(error.detail());
        // fallback to a generalized exception if nothing else could be
        // discovered and assume a value is wrong
        case NONE           :
        case INVALID_VALUE  :
        default             : return invalidValue(error.detail());
      }
    }
    else {
      // whole exception handling in this case is a black magic.
      // PCF does not define any checked exceptions so the developers are not
      // guided towards good exception handling.
      // But this nightmarish code is still needed to support bad connectors.
      if (error.detail().startsWith("Invalid filter"))
        return invalidFilter(error.detail());
    }
    // fallback to a generalized exception if nothing else could be discovered
    return invalidValue(error.detail());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unauthorized
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_AUTHORIZATION} error keyword.
   ** <p>
   ** The request is not authorized.
   ** <br>
   ** The authentication credentials included with this request are missing or
   ** invalid.
   ** <br>
   ** The corresponding HTTP status is <code>401</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-401 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException unauthorized(final String message) {
    return new ServiceException(ServiceError.PROCESS_AUTHORIZATION, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forbidden
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_FORBIDDEN} error keyword.
   ** <p>
   ** The request was valid, but the server is refusing action.
   ** <br>
   ** The user might not have the necessary permissions for a resource, or may
   ** need an account of some sort.
   ** <br>
   ** The corresponding HTTP status is <code>403</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-403 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException forbidden(final String message) {
    return new ServiceException(ServiceError.PROCESS_FORBIDDEN, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notAllowed
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_NOT_ALLOWED} error keyword.
   ** <p>
   ** The HTTP verb specified in the request (DELETE, GET, POST, PUT) is not
   ** supported for this request URI.
   ** <br>
   ** The corresponding HTTP status is <code>405</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-405 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException notAllowed(final String message) {
    return new ServiceException(ServiceError.PROCESS_NOT_ALLOWED, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notAcceptable
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_NOT_ACCEPTABLE} error keyword.
   ** <p>
   ** The Service Provider cannot produce a response matching the list of
   ** acceptable values defined in the request's proactive content negotiation
   ** headers, and that the Service Provider is unwilling to supply a default
   ** representation.
   ** <br>
   ** The corresponding HTTP status is <code>406</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-406 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException notAcceptable(final String message) {
    return new ServiceException(ServiceError.PROCESS_NOT_ACCEPTABLE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mediaTypeUnsupported
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_MEDIATYPE_UNSUPPORTED} error keyword.
   ** <p>
   ** The Service Provider refuses to accept the request because the payload
   ** format is in an unsupported format.
   ** <br>
   ** The corresponding HTTP status is <code>415</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-415 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException mediaTypeUnsupported(final String message) {
    return new ServiceException(ServiceError.PROCESS_MEDIATYPE_UNSUPPORTED, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   conflict
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_EXISTS} error keyword.
   ** <p>
   ** The specified version number does not match the resource's latest version
   ** number, or the Service Provider refused to create a new, duplicate
   ** resource.
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException conflict(final String message) {
    return new ServiceException(ServiceError.PROCESS_EXISTS, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notFound
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_EXISTS_NOT} error keyword.
   ** <p>
   ** Specified resource (e.g., User) or endpoint does not exist.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-404 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException notFound(final String message) {
    return new ServiceException(ServiceError.PROCESS_EXISTS_NOT, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preCondition
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_PRECONDITION} error keyword.
   ** <p>
   ** Failed to update. Resource has changed on the server.
   ** <p>
   ** The corresponding HTTP status is <code>412</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-412 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException preCondition(final String message) {
    return new ServiceException(ServiceError.PROCESS_PRECONDITION, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notModified
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_POSTCONDITION} error keyword.
   ** <p>
   ** The resource has not changed.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-304 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException notModified(final String message) {
    return new ServiceException(ServiceError.PROCESS_POSTCONDITION, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tooLarge
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_TOO_LARGE} error keyword.
   ** <p>
   ** The server is refusing to process a request because the request payload is
   ** larger than the server is willing or able to process.
   ** <p>
   ** The corresponding HTTP status is <code>413</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-412 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException tooLarge(final String message) {
    return new ServiceException(ServiceError.PROCESS_TOO_LARGE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidVersion
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_INVALID_VERSION} error keyword.
   ** <p>
   ** The specified REST protocol version is not supported.
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException invalidVersion(final String message) {
    return new ServiceException(ServiceError.PROCESS_INVALID_VERSION, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tooMany
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_TOO_MANY} error keyword.
   ** <p>
   ** The specified filter yields many more results than the server is
   ** willing to calculate or process.
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException tooMany(final String message) {
    return new ServiceException(ServiceError.PROCESS_TOO_MANY, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutability
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_MUTABILITY} error keyword.
   ** <p>
   ** The attempted modification is not compatible with the target
   ** attributes mutability or current state (e.g., modification of an
   ** immutable attribute with an existing value).
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException mutability(final String message) {
    return new ServiceException(ServiceError.PROCESS_MUTABILITY, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sensitive
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_SENSITIVE} error keyword.
   ** <p>
   ** The attempted modification is not compatible with the target
   ** attributes mutability or current state (e.g., modification of an
   ** immutable attribute with an existing value).
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException sensitive(final String message) {
    return new ServiceException(ServiceError.PROCESS_SENSITIVE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueness
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_UNIQUENESS} error keyword.
   ** <p>
   ** One or more of the attribute values is already in use or is reserved.
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException uniqueness(final String message) {
    return new ServiceException(ServiceError.PROCESS_UNIQUENESS, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   noTarget
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_INVALID_VALUE} error keyword.
   ** <p>
   ** The path attribute was invalid or malformed.
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException noTarget(final String message) {
    return new ServiceException(ServiceError.PROCESS_NOTARGET, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidFilter
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_INVALID_FILTER} error keyword.
   ** <p>
   ** The specified filter syntax was invalid or the specified attribute and
   ** filter comparison combination is not supported.
   ** <p>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException invalidFilter(final String message) {
    return new ServiceException(ServiceError.PROCESS_INVALID_FILTER, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidSyntax
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_INVALID_SYNTAX} error keyword.
   ** <p>
   ** The request body message structure was invalid or did not conform to
   ** the request schema.
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException invalidSyntax(final String message) {
    return new ServiceException(ServiceError.PROCESS_INVALID_SYNTAX, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidPath
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_INVALID_PATH} error keyword.
   ** <p>
   ** The path attribute was invalid or malformed.
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException invalidPath(final String message) {
    return new ServiceException(ServiceError.PROCESS_INVALID_PATH, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidValue
  /**
   ** Factory method to create a new <code>ServiceException</code> with the
   ** {@link ServiceError#PROCESS_INVALID_VALUE} error keyword.
   ** <p>
   ** A required value was missing, or the value specified was not compatible
   ** with the operation or attribute type (see Section 2.2 of [
   ** <a href="https://tools.ietf.org/html/rfc7643">RFC7643</a>), or
   ** resource schema (see
   ** <a href="https://tools.ietf.org/html/rfc7644#section-4">Section 4</a>
   ** of [<a href="https://tools.ietf.org/html/rfc7643">RFC7643</a>)
   ** <br>
   ** The corresponding HTTP status is <code>400</code>
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ServiceException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException invalidValue(final String message) {
    return new ServiceException(ServiceError.PROCESS_INVALID_VALUE, message);
  }
}