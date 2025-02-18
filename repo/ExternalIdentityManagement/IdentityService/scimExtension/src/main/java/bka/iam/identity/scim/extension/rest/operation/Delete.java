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

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the Delete class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-11-26  SBernet     First release version
*/
package bka.iam.identity.scim.extension.rest.operation;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.rest.HTTPContext;

import java.io.IOException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
///////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** The <code>Delete</code> class represents a RESTful DELETE operation.
 ** <p>
 ** This class encapsulates the functionality required to send a DELETE request
 ** to a specified <code>WebTarget</code> and handles the associated response.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Delete extends oracle.hst.platform.rest.request.Request<Delete> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new REST DELETE request.
   ** This constructor is private to prevent other classes from using "new Delete()"
   ** and enforces use of the public method below.
   **
   ** @param target          the {@link WebTarget} to send the request.
   **                        Allowed object is {@link WebTarget}.
   */
  private Delete(final WebTarget target) {
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic DELETE resource request.
   **
   ** @param target         the {@link WebTarget} to send the request.
   **                       Allowed object is {@link WebTarget}.
   **
   ** @return               a JAX-RS request.
   **                       Possible object is <code>Delete</code>.
   */
  public static Delete build(final WebTarget target) {
    return new Delete(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: invoke
  /**
   ** Invoke the REST DELETE request.
   **
   ** @return                 A {@link Response} object representing the HTTP
   **                         response.
   **                         Possible object is {@link Response}.
   **
   ** @throws ScimException   If the REST service provider responds with an error.
   */
  public Response invoke() throws ScimException {
    // Send the DELETE request and capture the response
    final Response response = buildRequest().delete();
    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
      } else {
        // Error case: throw a ScimException with the response details
        throw new ScimException(response);
      }
    } catch (ProcessingException e) {
      //Server exception
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e);
    } 
    catch (IOException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e);
    }
    finally {
      response.close();
    }
    return response;
  }
}
