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

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   ScimExceptionMapper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the ScimExceptionMapper class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-20-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.exception;

import bka.iam.identity.scim.extension.parser.Marshaller;

import com.fasterxml.jackson.databind.JsonNode;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
////////////////////////////////////////////////////////////////////////////////
// class ScimExceptionMapper
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The ScimExceptionMapper class is a JAX-RS exception mapper that converts a
 ** SCIM exception (ScimException) into an appropriate HTTP response. This class
 ** is responsible for handling exceptions in a SCIM service and ensuring that
 ** the proper response is returned to the client.
 ** 
 ** It implements the ExceptionMapper interface, and when a ScimException is
 ** thrown, it maps the exception to a HTTP response with the correct status code
 ** and error message.
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Provider
public class ScimExceptionMapper implements ExceptionMapper<ScimException> {

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toResponse
  /**
   ** Converts a ScimException into a Response object to be returned to the client.
   ** This method retrieves the error details from the exception and constructs
   ** a response with the appropriate status code, error message, and headers.
   ** 
   ** @param  error              The ScimException to be converted to a response.
   **                            Allowed object is {@link ScimException}.
   ** 
   ** @return                    The HTTP Response object that represents the
   **                            error.
   **                            Possible object is {@link Response}.
   */
  @Override
  public Response toResponse(ScimException error) {
    // Uggly stuff bu no easy way to get the logger from there...
    // Log the error (replace this with a proper logger in production)
    error.printStackTrace();

    // Convert the ScimException to a JsonNode
    JsonNode node = Marshaller.scimErrorToJsonNode(error);

    // Serialize the JsonNode to a JSON string
    String jsonResponse = node.toString();

    // Build the response
    return Response.status(error.getHttpCode().getStatusCode())
                   .entity(jsonResponse)
                   .header("Content-Type", "application/scim+json")
                   .build();
  }
}
