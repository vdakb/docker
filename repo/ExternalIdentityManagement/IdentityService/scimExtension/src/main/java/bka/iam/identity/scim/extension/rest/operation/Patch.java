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

    File        :   Patch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the Patch class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-11-26  SBernet     First release version
*/
package bka.iam.identity.scim.extension.rest.operation;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.PatchRequest;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
///////////////////////////////////////////////////////////////////////////////
// class Patch
// ~~~~~ ~~~~~
/**
 ** The <code>Patch</code> class represents a RESTful PATCH operation.
 ** <p>
 ** This class encapsulates functionality required to send a PATCH request
 ** to a specified <code>WebTarget</code> and handle the associated response.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Patch extends oracle.hst.platform.rest.request.Request<Patch> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Constructs a new REST PATCH request.
   ** This constructor is private to prevent other classes from using "new Patch()"
   ** and enforces use of the public method below.
   **
   ** @param  target             The {@link WebTarget} to send the request to.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Patch(final WebTarget target) {
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Factory method to create a generic PATCH resource request.
   **
   ** @param  target             The {@link WebTarget} to send the request to.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    A new {@link Patch} instance.
   **                            <br>
   **                            Possible object is {@link Patch}.
   */
  public static Patch build(final WebTarget target) {
    return new Patch(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Sends the PATCH request and returns the response entity as a specific type.
   **
   ** @param  <R>                The type of resource to return.
   **                            <br>
   **                            Allowed object is {@link ScimResource}.
   ** @param  clazz              The {@link Class} of the resource type.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  entity             The entity to be sent in the PATCH request.
   **                            <br>
   **                            Allowed object is {@link PatchRequest}.
   **
   ** @return                    The successfully updated resource of type
   **                            <code>R</code>.
   **                            <br>
   **                            Possible object is {@link ScimResource}.
   **
   ** @throws ScimException      If the REST service provider responds with an
   **                            error.
   */
  public <R extends ScimResource> R invoke(final Class<R> clazz, final PatchRequest entity)
  throws ScimException {

    final Response response = buildRequest().method("PATCH", Entity.entity(entity.toString(), "application/scim+json"));
    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(clazz);
        // Read the response entity as a string
        final String responseValue = response.readEntity(String.class);

        // Parse the JSON into the expected resource type
        return Unmarshaller.jsonNodeToResource(new ObjectMapper().readTree(responseValue), resourceDescriptor, clazz);
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
}
