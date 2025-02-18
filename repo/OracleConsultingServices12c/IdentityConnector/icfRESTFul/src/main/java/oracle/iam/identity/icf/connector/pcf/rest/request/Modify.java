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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Modify.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Modify.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest.request;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.request.Request;

import oracle.iam.identity.icf.connector.pcf.rest.ExceptionParser;

////////////////////////////////////////////////////////////////////////////////
// class Modify
// ~~~~~ ~~~~~~
/**
 ** A factory for REST modify requests.
 **
 ** @param  <T>                  the type of the payload implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>T</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Modify<T extends Resource> extends Request<Modify> {

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
   ** Constructs a a new REST modify request that will PATCH the given resource
   ** to the given web target.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  resource           the REST resource to PUT or PATCH.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   */
  private Modify(final WebTarget target, final T resource) {
    // ensure inheritance
    super(target);

    // initialize instance attributes
    this.resource = resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic modify resource request.
   **
   ** @param  <T>                the type of the payload implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the request payload
   **                            extending this class (requests can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  resource           the REST resource to PUT or PATCH.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Modify</code>.
   */
  public static <T extends Resource> Modify build(final WebTarget target, final T resource) {
    return new Modify<T>(target, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST modify request using PUT.
   **
   ** @param  <R>                the type of objects to return.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>R</code>.
   **
   ** @return                    the successfully modified REST resource.
   **                            <br>
   **                            Possible object is <code>R</code>.
   **
   ** @throws SystemException    if an error occurred.
   */
  public <R> R invoke(final Class<R> clazz)
    throws SystemException {

    Response response = null;
    try {
      response = buildRequest().put(Entity.entity(this.resource, contentType()));
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        return (R)response.readEntity(clazz);
      }
      else {
        throw ExceptionParser.from(response);
      }
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }
    finally {
      if (response != null)
        response.close();
    }
  }
}