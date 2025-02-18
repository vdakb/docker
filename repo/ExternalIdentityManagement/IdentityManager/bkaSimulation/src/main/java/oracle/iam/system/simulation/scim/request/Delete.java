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

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Delete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.request;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import oracle.iam.system.simulation.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** A factory for SCIM delete requests.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Delete extends Request<Delete> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String version;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new SCIM delete request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   */
  public Delete(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Delete the resource only if the resource has not been modified since the
   ** provided version.
   **
   ** @param  version            the version of the resource to compare.
   **
   ** @return                    the {@link Delete} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Delete}.
   */
  public Delete match(final String version) {
    this.version = version;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildRequest
  /**
   ** Factory method to create a {@link Invocation.Builder} for the request.
   **
   ** @return                     the {@link Invocation.Builder} for the request.
   */
  @Override
  Invocation.Builder buildRequest() {
    Invocation.Builder request = super.buildRequest();
    if (this.version != null) {
      request.header(HttpHeaders.IF_MATCH, this.version);
    }
    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM delete request using DELETE.
   **
   ** @throws ServiceException   if an error occurred.
   */
  public void invoke()
    throws ServiceException {

    Response response = null;
    try {
      response = buildRequest().delete();
      if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
        throw toException(response);
      }
    }
    catch (ProcessingException e) {
      throw ServiceException.build(e, target().getUri());
    }
    finally {
      if (response != null)
        response.close();
    }
  }
}