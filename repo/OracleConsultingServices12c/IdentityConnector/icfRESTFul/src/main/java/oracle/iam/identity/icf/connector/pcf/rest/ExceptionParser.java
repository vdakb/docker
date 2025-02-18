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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   ExceptionParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ExceptionParser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest;

import java.io.InputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.ResponseProcessingException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.domain.Error;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

////////////////////////////////////////////////////////////////////////////////
// class ExceptionParser
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** An interface to convert PCF REST error responses object in transferrable
 ** {@link ServiceException}s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExceptionParser {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (private)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using
   ** "new ExceptionParser()"
   */
  private ExceptionParser() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Convert a JAX-RS response to a {@link ServiceException}.
   **
   ** @param  response           the JAX-RS response.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @return                    the converted {@link ServiceException}.
   **                            <br>
   **                            Possible object is {@link ServiceException}.
   */
  public static SystemException from(final Response response) {
    final Error       error  = new Error(response.getStatus());
    final InputStream stream = response.readEntity(InputStream.class);
    try {
      final JsonParser parser = MapperFactory.instance.reader().getFactory().createParser(stream);
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
//              case "error_code"         : error.type(parser.getValueAsString());
//                                          break;
              // PCF use "message" and "error_description" as the property name
              // of the detailed, human readable message in a SCIM error
              // response
              // PCF use "description"the property name of the detailed, human
              // readable message in a REST error response
              case "message"            :
              case "description"        :
              case "error_description"  : error.detail(parser.getValueAsString());
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
   ** Convert a JAX-RS result to a <code>ServiceException</code>.
   **
   ** @param  error              the REST Error response.
   **                            <br>
   **                            Allowed object is {@link Error}.
   **
   ** @return                    the converted {@link ServiceException}.
   **                            <br>
   **                            Possible object is {@link ServiceException}.
   */
  public static SystemException from(final Error error) {
    // if are able to read an error response, use it to build the exception.
    switch (error.status()) {
      case 304 : return ServiceException.notModified(error.detail());
      case 400 : return badRequest(error);
      case 401 : return ServiceException.unauthorized(error.detail());
      case 403 : return ServiceException.forbidden(error.detail());
      case 404 : return notFound(error);
      case 405 : return ServiceException.notAllowed(error.detail());
      case 406 : return ServiceException.notAcceptable(error.detail());
      case 409 : return ServiceException.conflict(error.detail());
      case 412 : return ServiceException.preCondition(error.detail());
      case 413 : return ServiceException.tooLarge(error.detail());
      case 415 : return ServiceException.mediaTypeUnsupported(error.detail());
      case 500 : return ServiceException.unexpected(error.detail());
      case 501 : return ServiceException.unavailable(error.detail());
      default  : return SystemException.abort(error.detail());
    }
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
   ** @return                    the <code>RestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException badRequest(final Error error) {
    // whole exception handling in this case is a black magic.
    // ICF does not define any checked exceptions so the developers are not
    // guided towards good exception handling.
    // But this nightmarish code is still needed to support bad connectors.
    if ("CF-BadQueryParameter".equals(error.type())) {
      return ServiceException.invalidFilter(error.detail());
    }
    else if ("CF-InvalidRelation".equals(error.type()) || "CF-AssociationNotEmpty".equals(error.type())) {
      // fallback to a generalized exception if nothing else could be discovered
      return ServiceException.notModified(String.format("%s: %s", error.type(), error.detail()));
    }
    else {  
      // fallback to a generalized exception if nothing else could be discovered
      return ServiceException.invalidValue(String.format("%s: %s", error.type(), error.detail()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notFound
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
   ** @return                    the <code>RestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException notFound(final Error error) {
    // whole exception handling in this case is a black magic.
    // ICF does not define any checked exceptions so the developers are not
    // guided towards good exception handling.
    // But this nightmarish code is still needed to support bad connectors.
    return ServiceException.notFound(String.format("%s: %s", error.type(), error.detail()));
  }
}