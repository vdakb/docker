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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Atlassian Jira Connector

    File        :   ExceptionParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ExceptionParser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.request;

import java.util.Map;
import java.util.Collection;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.core.type.TypeReference;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.domain.Error;

import oracle.iam.identity.icf.rest.ServiceMessage;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

////////////////////////////////////////////////////////////////////////////////
// class ExceptionParser
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** An interface to convert JIRA REST error responses object in transferrable
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
          // latch the current filed token for further processing
          final String field  = parser.getCurrentName();
          String       detail = null;
          if (JsonToken.NOT_AVAILABLE != parser.nextToken()) {
            // skip any suspicious stuff
            if (field != null) {
              switch (field) {
                  // JIRA use sometimes "errors" as the property name of the
                  // detailed, human readable message in a REST error response
                case "errors"         : detail = messageMap(parser);
                                        break;
                  // JIRA use sometimes "errorMessages" as the property name of
                  // the detailed, human readable message in a REST error
                  // response
                case "errorMessages" : detail = messageCollector(parser);
                                       break;
              }
              if (detail != null) {
                error.detail(detail);
              }
            }
            // there isn't any detailed information what was going wrong
            else {
              error.detail(ServiceBundle.string(ServiceMessage.UNSPECIFIED_ERROR));
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
      case 400 : return ServiceException.badRequest(error);
      case 401 : return ServiceException.unauthorized(error.detail());
      case 403 : return ServiceException.forbidden(error.detail());
      case 404 : return ServiceException.notFound(error.detail());
      case 409 : return ServiceException.conflict(error.detail());
      case 415 : return ServiceException.mediaTypeUnsupported(error.detail());
      case 500 : return ServiceException.unexpected(error.detail());
      case 501 : return ServiceException.unavailable(error.detail());
      default  : return SystemException.abort(error.detail());
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
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>RestException</code> wrapping
   **                            the HTTP-400 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceException</code>.
   */
  public static ServiceException notFound(final String error) {
    // whole exception handling in this case is a black magic.
    // ICF does not define any checked exceptions so the developers are not
    // guided towards good exception handling.
    // But this nightmarish code is still needed to support bad connectors.
    return ServiceException.notFound(error);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   messageCollector
  /**
   ** Creates a string form an JSON array of arbitrary strings.
   **
   ** @param  parser             the {@link JsonParser} positioned just before
   **                            the array of arbitrary strings.
   **                            <br>
   **                            Allowed object is {@link JsonParser}.
   **
   ** @return                    the string with the collected messages.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IOException        in case a parsing error occurs.
   */
  private static String messageCollector(final JsonParser parser)
    throws IOException {

    final  Collection<String> errors = parser.readValueAs(new TypeReference<Collection<String>>(){});
    if (errors != null && errors.size() > 0) {
      int i = 0;
      final StringBuilder collector = new StringBuilder();
      for (String cursor : errors) {
        if (i++ > 0)
        collector.append('\n');
        collector.append(cursor);
      }
      return collector.toString();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   messageMap
  /**
   ** Creates a string form an JSON key/value pair of arbitrary strings.
   **
   ** @param  parser             the {@link JsonParser} positioned just before
   **                            the key/value pair of arbitrary strings.
   **                            <br>
   **                            Allowed object is {@link JsonParser}.
   **
   ** @return                    the string with the collected messages.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IOException        in case a parsing error occurs.
   */
  private static String messageMap(final JsonParser parser)
    throws IOException {

    final Map<String, String> errors = parser.readValueAs(new TypeReference<Map<String, String>>(){});
    if (errors != null && errors.size() > 0) {
      int i = 0;
      final StringBuilder collector = new StringBuilder();
      for (Map.Entry<String, String> entry : errors.entrySet()) {
          if (i++ > 0)
            collector.append('\n');
        collector.append(entry.getValue());
      }
      return collector.toString();
    }
    return null;
  }
}