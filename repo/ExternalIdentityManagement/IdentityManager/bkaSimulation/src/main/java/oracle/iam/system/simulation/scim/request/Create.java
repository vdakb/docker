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

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.request;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import oracle.iam.system.simulation.ServiceException;

import oracle.iam.system.simulation.scim.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** A factory for SCIM create requests.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request extending
 **                              this class (requests can return their own
 **                              specific type instead of type defined by this
 **                              class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Create<T extends Resource> extends Return<Create> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final T resource;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new SCIM create request that will POST the given resource
   ** to the given web target.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   ** @param  resource           the SCIM resource to POST.
   */
  public Create(final WebTarget target, final T resource) {
    // ensure inheritance
    super(target);

    // initialize instance attributes
    this.resource = resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM create request using POST.
   **
   ** @return                    the successfully create SCIM resource.
   **
   ** @throws ServiceException   if an error occurred.
   */
  public T invoke()
    throws ServiceException {

    return (T)invoke(this.resource.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM create request using POST.
   **
   ** @param  <T>                the type of objects to return.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **
   ** @return                    the successfully create SCIM resource.
   **
   ** @throws ServiceException   if an error occurred.
   */
  public <T> T invoke(final Class<T> clazz)
    throws ServiceException {

    Response response = null;
    try {
      response = buildRequest().post(Entity.entity(this.resource, contentType()));
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        return response.readEntity(clazz);
      }
      else {
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