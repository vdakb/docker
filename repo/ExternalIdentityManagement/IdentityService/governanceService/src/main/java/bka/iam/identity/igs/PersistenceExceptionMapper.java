/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   PersistenceExceptionMapper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PersistenceExceptionMapper.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs;

import java.sql.SQLException;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ExceptionMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import oracle.hst.platform.jpa.PersistenceException;

import oracle.hst.platform.rest.response.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class PersistenceExceptionMapper
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A JAX-RS {@link javax.ws.rs.ext.ExceptionMapper ExceptionMapper} converting
 ** {@link oracle.hst.platform.jpa.PersistenceException} to REST Error
 ** Responses.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PersistenceExceptionMapper</code> allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PersistenceExceptionMapper() {
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
   **                            Allowed object is {@link PersistenceException}.
   **
   ** @return                    a response mapped from the supplied exception.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @Override
  public Response toResponse(final PersistenceException cause) {
    final Throwable wrapped = cause.getCause();
    // if we are eating our own food
    if (wrapped instanceof PersistenceException) {
      return toResponse((PersistenceException)wrapped);
    }
    // if no further exception is wrapped we have the root cause that leads
    // always to a HTTP 400
    else if (wrapped == null) {
      return Response.status(Response.Status.BAD_REQUEST)
        .entity(
          ErrorResponse.of(
            Response.Status.BAD_REQUEST.getStatusCode()
          , cause.getLocalizedMessage()
          )
          .type(cause.code())
          .description(cause.description())
          .jsonify()
        )
        .type(MediaType.APPLICATION_JSON)
        .build();
    }
    else if (wrapped instanceof SQLException) {
      final SQLException e = (SQLException)wrapped;
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(
           ErrorResponse.of(
             Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
           , e.getLocalizedMessage()
           )
           .type(String.valueOf(e.getErrorCode()))
           .jsonify()
        )
        .type(MediaType.APPLICATION_JSON)
        .build();
    }
    // if we have a nested PersistenceException digging deeper
    else if (wrapped != null) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(
           ErrorResponse.of(
             Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
           , wrapped.getLocalizedMessage()
           )
           .type("internal")
           .jsonify()
        )
        .type(MediaType.APPLICATION_JSON)
        .build();
    }
    else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(
          new ErrorResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).detail(cause.getLocalizedMessage()).jsonify())
        .build();
    }
  }
}