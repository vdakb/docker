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

    System      :   Foundation Shared Library
    Subsystem   :   Generic REST Library

    File        :   ExceptionMapper.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationExceptionMapper.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package oracle.hst.platform.rest.provider;

import javax.ws.rs.WebApplicationException;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ExceptionMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

import oracle.hst.platform.rest.response.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationExceptionMapper
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A JAX-RS {@link javax.ws.rs.ext.ExceptionMapper ExceptionMapper} converting
 ** {@link javax.ws.rs.WebApplicationException} to REST Error Responses.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationExceptionMapper</code> allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ApplicationExceptionMapper() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toResponse (ExceptionMapper)
  /**
   ** Map an exception to a {@link Response}.
   ** <br>
   ** Returning <code>null</code> results in a
   ** {@link Response.Status#NO_CONTENT} response.
   ** <br>
   ** Throwing a runtime exception results in a
   ** {@link Response.Status#INTERNAL_SERVER_ERROR} response.
   **
   ** @param  cause              the exception to map to a response.
   **                            <br>
   **                            Allowed object is
   **                            {@link WebApplicationException}.
   **
   ** @return                    a response mapped from the supplied exception.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @Override
  public Response toResponse(final WebApplicationException cause) {
    final Response.StatusType status = cause.getResponse().getStatusInfo();
    return Response.status(status)
      .entity(ErrorResponse.of(status.getStatusCode(), cause.getLocalizedMessage()).type(status.getReasonPhrase()).description(ServiceBundle.string(ServiceError.HTTP_CODE_403)))
      .type(MediaType.APPLICATION_JSON)
      .build();
  }
}