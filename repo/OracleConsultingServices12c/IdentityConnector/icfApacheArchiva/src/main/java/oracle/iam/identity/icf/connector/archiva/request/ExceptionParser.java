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
    Subsystem   :   Apache Archiva Connector

    File        :   ExceptionParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ExceptionParser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva.request;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.core.JsonParser;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.domain.Error;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

////////////////////////////////////////////////////////////////////////////////
// class ExceptionParser
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** An interface to convert ARCIVA REST error responses object in transferrable
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
      case 401 : return ServiceException.unauthorized(error.detail());
      case 403 : return ServiceException.forbidden(error.detail());
      case 404 : return ServiceException.notFound(error.detail());
      default  : return SystemException.abort(error.detail());
    }
  }
}