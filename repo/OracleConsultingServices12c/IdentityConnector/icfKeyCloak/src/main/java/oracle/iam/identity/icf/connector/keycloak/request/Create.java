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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.request;

import java.io.IOException;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.keycloak.schema.User;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** A factory for Keycloak resource create requests.
 **
 ** @param  <T>                  the type of the payload implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Create<T extends Resource> extends Abstract<T, Create> {

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
  // Method:   user
  /**
   ** Build a request to create the provided new KeyCloak resource at the
   ** Service Provider.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public static Create<User> user(final WebTarget target) {
    return new Create<User>(target.path(ENDPOINT_USER));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST create request using POST.
   **
   ** @param  <T>                the type of the payload implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  resource           the REST resource to POST.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the successfully created REST resource.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws SystemException    if an error occurred.
   */
  public <T extends Resource> T invoke(final T resource)
    throws SystemException {

    Response response = null;
    try {
      final Entity payload = Entity.entity(MapperFactory.instance.writeValueAsString(resource), contentType());
      response = buildRequest().post(payload);
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        return null;
      }
      else {
        throw ExceptionParser.from(response);
      }
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
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