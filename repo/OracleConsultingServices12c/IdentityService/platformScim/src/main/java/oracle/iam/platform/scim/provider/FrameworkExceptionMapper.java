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

    File        :   ApplicationExceptionMapper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationExceptionMapper.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.provider;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ExceptionMapper;

import javax.ws.rs.core.Response;

import oracle.iam.platform.scim.ResourceContext;

import oracle.iam.platform.scim.response.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationExceptionMapper
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A JAX-RS {@link javax.ws.rs.ext.ExceptionMapper ExceptionMapper} converting
 ** {@link javax.ws.rs.ProcessingException} to REST Error Responses.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Provider
public class FrameworkExceptionMapper implements ExceptionMapper<ProcessingException> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FrameworkExceptionMapper</code> allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FrameworkExceptionMapper() {
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
   ** Throwing a runtime exception results in a
   ** {@link Response.Status#INTERNAL_SERVER_ERROR} response.
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
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
      .entity(ErrorResponse.of(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), cause.getLocalizedMessage()).type("javax.ws.rs.ProcessingException"))
      .type(ResourceContext.MEDIA_TYPE)
      .build();
  }
}
