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

    File        :   ResponseBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements ResponseBuilder.class

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-20-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.parser;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.rest.HTTPContext.StatusCode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

import javax.ws.rs.core.Response;
////////////////////////////////////////////////////////////////////////////////
// class ResponseBuilder
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The ResponseBuilder class is responsible for creating HTTP responses.
 ** This class provides utility methods to build SCIM responses with
 ** appropriate status codes and resource data.
 ** 
 ** It includes methods to build responses for individual SCIM resources or
 ** lists of resources.
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResponseBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // Method:   response
  /**
   ** Creates a standard SCIM response with the given status code and resource.
   ** This method serializes the given SCIM resource into a JSON string and
   ** returns an HTTP response with the appropriate status code and headers.
   ** 
   ** @param statusCode The HTTP status code of the response.
   **                   Allowed object is {@link Integer}.
   ** @param resource   The resource object to be serialized into the response
   **                   entity.
   **                   Allowed object is {@link ScimResource}.
   ** 
   ** @return           A SCIM response with the specified status code and
   **                   entity.
   **                   Possible object is {@link Response}.
   ** 
   ** @throws ScimException if an error occurs during serialization.
   */
  public static Response response(int statusCode, ScimResource resource) throws ScimException {
    return response(statusCode,resource,null,null);
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   response
  /**
   ** This method returns an HTTP response with the appropriate status code
   ** and no content.
   ** 
   ** @param statusCode The HTTP status code of the response.
   **                   Allowed object is {@link Integer}.
   **                   
   ** @return           A SCIM response with the specified status code and
   **                   entity.
   **                   Possible object is {@link Response}.
   ** 
   ** @throws ScimException if an error occurs during serialization.
   */
  public static Response response(int statusCode)
    throws ScimException {
    
    // Build and return the response with the resource data.
    return Response.status(statusCode)
                   .entity(new String())
                   .header("Content-Type", "application/scim+json")
                   .build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   response
  /**
   ** Creates a standard SCIM response with the given status code and resource.
   ** This method serializes the given SCIM resource into a JSON string and
   ** returns an HTTP response with the appropriate status code and headers.
   ** 
   ** @param statusCode The HTTP status code of the response.
   **                   Allowed object is {@link Integer}.
   ** @param resource   The resource object to be serialized into the response
   **                   entity.
   **                   Allowed object is {@link ScimResource}.
   ** @param  emit      the {@link Set} of attributes to include in 
   **                   the response.
   ** @param  omit      the {@link Set} of attributes to exclude from
   **                   the response.
   **                   
   ** @return           A SCIM response with the specified status code and
   **                   entity.
   **                   Possible object is {@link Response}.
   ** 
   ** @throws ScimException if an error occurs during serialization.
   */
  public static Response response(int statusCode, ScimResource resource, final Set<String> omit, final Set<String> emit)
    throws ScimException {
    String response = null;
    try {
      // Convert the resource to a JsonNode using the ScimMarshaller.
      final JsonNode jsonNode = Marshaller.resourceToJsonNode(resource, omit, emit);
      final ObjectMapper mapper = new ObjectMapper();
      
      response = mapper.writeValueAsString(jsonNode);
    } 
     catch (ScimException e) {
      // If ScimException error occurs, forward directly.
      throw e;
    } catch (Exception e) {
      // If an error occurs, throw a ScimException with an internal server error.
      throw new ScimException(StatusCode.INTERNAL_SERVER_ERROR, e);
    }
    
    // Build and return the response with the resource data.
    return Response.status(statusCode)
                   .entity(response)
                   .header("Content-Type", "application/scim+json")
                   .build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   response
  /**
   ** Creates a SCIM response for a list of resources with the given status code.
   ** This method serializes a list of SCIM resources into a JSON string and
   ** returns an HTTP response with the appropriate status code and headers.
   **
   ** @param            statusCode The HTTP status code of the response.
   **                   Allowed object is {@link Integer}.
   ** @param            listResponse The list of SCIM resources to be serialized into the
   **                   response entity.
   **                   Allowed object is {@link ListResponse}.
   ** @param  emit      the {@link Set} of attributes to include in 
   **                   the response.
   ** @param  omit      the {@link Set} of attributes to exclude from
   **                   the response.
   **
   ** @return           A SCIM response with the specified status code and
   **                   list of resources.
   **                   Possible object is {@link Response}.
   **
   ** @throws ScimException if an error occurs during serialization.
   */
  public static Response response(int statusCode, ListResponse<? extends ScimResource> listResponse, final Set<String> omit, final Set<String> emit, final Integer count, final Integer startItem)
    throws ScimException {
    
    String response;
    try {
      // Convert the list of resources to a JsonNode using the ScimMarshaller.
      final JsonNode jsonNode = Marshaller.listResourceToJsonNode(listResponse, omit, emit, count, startItem);
      ObjectMapper mapper = new ObjectMapper();
      response = mapper.writeValueAsString(jsonNode);
    } catch (Exception e) {
      // If an error occurs, throw a ScimException with an internal server error.
      throw new ScimException(StatusCode.INTERNAL_SERVER_ERROR, e);
    }

    // Build and return the response with list of resources data.
    return Response.status(statusCode)
                   .entity(response)
                   .header("Content-Type", "application/scim+json")
                   .build();
  }
}