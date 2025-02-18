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

    File        :   Get.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the Get operation for SCIM resources.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-11-26  SBernet     First release version
*/
package bka.iam.identity.scim.extension.rest.operation;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.SchemaDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
///////////////////////////////////////////////////////////////////////////////
// class Get
// ~~~~~ ~~~
/**
 ** The <code>Get</code> class represents a RESTful GET operation.
 ** <p>
 ** This class encapsulates functionality required to send a GET request
 ** to a specified <code>WebTarget</code> and handles associated responses.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Get extends oracle.hst.platform.rest.request.Request<Get> {
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new REST Get request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Lookup()" and enforces use of the public method below.
   **
   ** @param  target             The {@link WebTarget} to send the request to.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Get(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a GET request for a specified target.
   **
   ** @param  target             The {@link WebTarget} to send the request to.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    A new {@link Get} instance.
   **                            <br>
   **                            Possible object is {@link Get}.
   */
  public static Get build(final WebTarget target) {
    return new Get(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Sends the GET request and returns the response entity as a specific type.
   **
   ** @param  <R>                The type of resource to return.
   **                            <br>
   **                            Allowed object is {@link ScimResource}.
   ** @param  clazz              The {@link Class} of the resource type.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    The retrieved resource of type <code>R</code>.
   **                            <br>
   **                            Possible object is {@link ScimResource}.
   **
   ** @throws ScimException      If the REST service provider responds with an
   **                            error.
   */
  public <R extends ScimResource> R invoke(final Class<R> clazz)
    throws ScimException {
    
    final Response response = buildRequest().get();
    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(clazz);
        // this is a hack to solve the unmarshalling of an entity if the
        // connector runs in the embedded Conncetor Server deployed in Identity
        // Manager
        // The native call of readEntity fails with an empty entity without any
        // exception; Aaaargh
        // the reason is that the org.eclipse.persistent JsonStructureReader
        // kicks in that isnt't able to resolve to JSON-POJO relation properly.
        // The solution is to explicitly create a Jackson parser for this
        // purpose to bypass the standard implementation
        final String  responseValue = response.readEntity(String.class);
        return Unmarshaller.jsonNodeToResource(new ObjectMapper().readTree(responseValue), resourceDescriptor, clazz);
      }
      else {
        throw new ScimException(response);
      }
    }
    catch (ProcessingException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    finally {
      response.close();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokeList
  /**
   ** Sends the GET request and returns the response entity as a list of
   ** resources.
   **
   ** @param  <R>                The type of resource to return.
   **                            <br>
   **                            Allowed object is {@link ScimResource}.
   ** @param  clazz              The {@link Class} of the resource type.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    A list of retrieved resources.
   **                            <br>
   **                            Possible object is {@link ListResponse}.
   **
   ** @throws ScimException      If the REST service provider responds with an
   **                            error.
   ** @throws ServerException    If a server-side error occurs during processing.
   */
  public <R extends ScimResource> ListResponse<R> invokeList(final Class<R> clazz)
    throws ScimException {
    
    final Response response = buildRequest().get();
    
    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // Read the response entity as a string
        final String responseValue = response.readEntity(String.class);
        final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(clazz);
        // Parse the JSON into a list of resources using ScimUnmarshaller
        // We are assuming response from OIM is a the true. Don't need to check
        // the answer with the schema.
        return Unmarshaller.jsonNodeToListResponse(new ObjectMapper().readTree(responseValue), resourceDescriptor, clazz);
      } else {
        throw new ScimException(response);
      }
    } catch (ProcessingException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    } catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    } finally {
      response.close();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokeSchema
  /**
   ** Sends the GET request and returns the response entity as a schema descriptor.
   **
   ** @return                    The retrieved {@link SchemaDescriptor}.
   **                            <br>
   **                            Possible object is {@link SchemaDescriptor}.
   **
   ** @throws ScimException      If the REST service provider responds with an
   **                            error.
   */
  public SchemaDescriptor invokeSchema()
    throws ScimException {

    final Response response = buildRequest().get();
    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // this is a hack to solve the unmarshalling of an entity if the
        // connector runs in the embedded Conncetor Server deployed in Identity
        // Manager
        // The native call of readEntity fails with an empty entity without any
        // exception; Aaaargh
        // the reason is that the org.eclipse.persistent JsonStructureReader
        // kicks in that isnt't able to resolve to JSON-POJO relation properly.
        // The solution is to explicitly create a Jackson parser for this
        // purpose to bypass the standard implementation
        final String  responseValue = response.readEntity(String.class);
        return Unmarshaller.jsonNodetoSchema(new ObjectMapper().readTree(responseValue));
      }
      else {
        throw new ScimException(response);
      }
    }
    catch (ProcessingException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    finally {
      response.close();
    }
  }
}
