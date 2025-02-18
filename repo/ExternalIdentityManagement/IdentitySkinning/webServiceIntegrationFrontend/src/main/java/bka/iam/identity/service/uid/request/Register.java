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

    File        :   Register.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    Register.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.08.2022  SBernet     First release version
*/

package bka.iam.identity.service.uid.request;

import bka.iam.identity.service.ServiceException;

import javax.json.Json;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import oracle.hst.platform.rest.request.Request;

////////////////////////////////////////////////////////////////////////////////
// class Register
// ~~~~~ ~~~~~~~~
/**
 ** A factory to create a request for registering a Unique Identifier
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Register extends Request<Register> {

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
  public Register(WebTarget target) {
    // ensure inheritance
    super(target);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic register resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Register</code>.
   */
  public static Register build(final WebTarget target) {
    return new Register(target);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST register request using POST.
   **
   ** @param  uniqueId           the value of the
   **                            <code>Unique Identifier</code> to register.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully created REST resource.
   **
   ** @throws ServiceException      if the REST service provider responded with an
   **                            error.
   */
  public String invoke(final String uniqueId)
    throws ServiceException {

    Response response = buildRequest().post(Entity.json(Json.createObjectBuilder()
                                                            .add("uid", uniqueId)));
    
    if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
      return response.readEntity(String.class);
    }
    else {
      throw ExceptionParser.from(response);
    }
  }
  
}
