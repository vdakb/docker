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

    Copyright 2022 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Identity Services Integration

    File        :   Request.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    Request.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+------------------------------------
    1.0.0.0      17.08.2022  SBernet     First release version
*/
package bka.iam.identity.service.uid.request;

import bka.iam.identity.service.ServiceException;

import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class Request
// ~~~~~ ~~~~~~
/**
 ** A factory to generate a Unique Identifier request.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Request extends oracle.hst.platform.rest.request.Request<Request>{

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new REST register request that will POST the given resource
   ** to the given web target.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  public Request(WebTarget target) {
    super(target);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic resource request to request an
   ** Unique Identifier.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Request</code>.
   */
  public static Request build(final WebTarget target) {
    return new Request(target);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST request using POST to generate an UID.
   **
   ** @param  participantType    the identifier of the
   **                            <code>Participant Type</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            the identifier of the
   **                            <code>Country</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  state               the identifier of the
   **                            <code>State</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  participant        the identifier of the
   **                            <code>Participant</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the identifier of the
   **                            <code>Type</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the unique identifier just created.
   **
   ** @throws ServiceException   if the REST service provider responded with an
   **                            error.
   */
  public String invoke(final String participantType, final String country, final String state, final String participant, final String type)
    throws ServiceException {
    final Entity<JsonObject> payload = Entity.json(Json.createObjectBuilder()
                                                                  .add("ptt", participantType)
                                                                  .add("cid", country)
                                                                  .add("sid", state)
                                                                  .add("pts", participant)
                                                                  .add("tid", type).build());
    Response response = buildRequest().post(Entity.entity(payload.getEntity().toString(), this.contentType()));
    if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
      return uidParser(response);
    }
    else {
      throw ExceptionParser.from(response);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   uidParser
  /**
   ** Extract the uid value from a JAX-RS response.
   **
   ** @param  response           the JAX-RS response.
   **                            <br>
   **                            Allowed object is {@link Response}.
   **
   ** @return                    the extracted uid from the response.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  private String uidParser(final Response response) {
    final InputStream stream = response.readEntity(InputStream.class);
    final JsonObject  uid  = Json.createReader(stream).readObject();
    return uid.getString("uid");
  }
}
