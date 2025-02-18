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

    File        :   ProcessingExceptionMapper.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ProcessingExceptionMapper.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package oracle.hst.platform.rest.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ExceptionMapper;

import oracle.hst.platform.rest.ProcessingException;

import oracle.hst.platform.rest.response.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class ProcessingExceptionMapper
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A JAX-RS {@link ExceptionMapper ExceptionMapper} converting
 ** {@link ProcessingException} to REST Error Responses.
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
   ** {@link ErrorResponse} to an {@code JSonObject} fails.
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
    return Response.status(response.status()).entity(response.jsonify()).type(MediaType.APPLICATION_JSON).build();
  }
}