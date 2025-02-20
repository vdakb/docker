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

    File        :   Create.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Create.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.pid.request;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.platform.rest.request.Request;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** A factory for create requests.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Create extends Request<Create> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST create request that will POST the given resource
   ** to the given web target.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Create(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic create resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Create</code>.
   */
  public static Create build(final WebTarget target) {
    return new Create(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST create request using POST.
   **
   ** @param  prefix             the prefix of the
   **                            <code>Anonymous Identifier</code> to generate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  usedby             the identity to generate the
   **                            <code>Anonymous Identifier</code> for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully created REST resource.
   **
   ** @throws TaskException      if the REST service provider responded with an
   **                            error.
   */
  public String invoke(final String prefix, final String usedby)
    throws TaskException {

    Response response = buildRequest().post(Entity.form(new Form().param("prefix", prefix).param("usedby", usedby)));
    if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
      return response.readEntity(String.class);
    }
    else {
      throw ExceptionParser.from(response);
    }
  }
}