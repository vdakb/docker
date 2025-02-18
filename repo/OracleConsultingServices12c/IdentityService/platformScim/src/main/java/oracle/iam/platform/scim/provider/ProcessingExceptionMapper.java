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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   ProcessingExceptionMapper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ProcessingExceptionMapper.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.provider;

import javax.ws.rs.core.Response;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ExceptionMapper;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.ProcessingException;

import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.response.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class ProcessingExceptionMapper
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A JAX-RS {@link ExceptionMapper ExceptionMapper} converting
 ** {@link ProcessingException} to SCIM Error Responses.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Provider
public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ProcessingExceptionMapper</code> allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ProcessingExceptionMapper() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toResponse (ExceptionMapper)
  /**
   ** Map a {@link ProcessingException} to a {@link Response}.
   ** <br>
   ** Throwing a runtime exception results in a
   ** {@link Response.Status#INTERNAL_SERVER_ERROR} if evaluation of
   ** {@link ErrorResponse} to an {@code JsonObject} fails.
   **
   ** @param  cause              the exception to map to a response.
   **                            <br>
   **                            Allowed object is {@link ProcessingException}.
   **
   ** @return                    a response mapped from the supplied exception.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @Override
  public Response toResponse(final ProcessingException cause) {
    final ErrorResponse response = cause.error();
    try {
      return Response.status(response.status()).entity(Support.objectWriter().writeValueAsString(response)).type(ResourceContext.MEDIA_TYPE).build();
    }
    catch (JsonProcessingException e) {
      e.printStackTrace(System.err);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorResponse.of(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getLocalizedMessage()).type("internal")).build();
    }
  }
}