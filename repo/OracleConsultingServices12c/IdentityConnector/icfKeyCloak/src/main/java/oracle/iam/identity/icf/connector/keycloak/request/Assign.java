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

    File        :   Assign.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Assign.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.request;

import java.util.List;

import java.io.IOException;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.keycloak.schema.Role;

////////////////////////////////////////////////////////////////////////////////
// class Assign
// ~~~~~ ~~~~~~
/**
 ** A factory for Keycloak resource modify resource requests that belongs to
 ** entitlements of a user.
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
public class Assign<T extends Resource> extends Abstract<T, Assign> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST modify request that will PUT the given resource
   ** to the given web target.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Assign(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Build a request to modify a known REST resource at the Service Provider.
   **
   ** @param  <T>                the type of the payload implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the request payload
   **                            extending this class (requests can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Assign} for type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Resource> Assign<T> group(final WebTarget target, final String beneficiary, final String id) {
    return new Assign<T>(target.path(ENDPOINT_USER).path(beneficiary).path(ENDPOINT_GROUP).path(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Build a request to modify a known REST resource at the Service Provider.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>Assign</code> for type
   **                            {@link Role}.
   */
  @SuppressWarnings("unchecked")
  public static Assign<Role> role(final WebTarget target, final String beneficiary) {
    return new Assign<Role>(target.path(ENDPOINT_USER).path(beneficiary).path(ENDPOINT_MEMBER).path("realm"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Build a request to modify a known REST resource at the Service Provider.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  client             the identifier of a <code>Client</code> resource.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a role
   **                            not the role name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>Assign</code> for type
   **                            {@link Role}.
   */
  public static Assign<Role> role(final WebTarget target, final String beneficiary, final String client) {
    return new Assign<>(target.path(ENDPOINT_USER).path(beneficiary).path(ENDPOINT_MEMBER).path(ENDPOINT_CLIENT).path(client));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST modify request using PUT.
   ** <br>
   ** Keycloak rapes many REST methods to create a so-called "fine-grained" API.
   ** <br>
   ** Nonsense.
   ** <br>
   ** This only means that methods that expect a request body are sent without a
   ** payload, which is not permitted at all in the shelf case.
   **
   ** @throws SystemException    if an error occurred.
   */
  public void invoke()
    throws SystemException {

    Response response = null;
    try {
      response = buildRequest().put(Entity.json(""));
      if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST modify request using POST to assign the role mapping.
   ** <p>
   ** The payload needs to formated like:
   ** <pre>
   **   [ { "id"   : "80504982-e090-4244-95ce-58a73bd97a28"
   **     , "name" : "uma_authorization" 
   **     }
   **   ]
   ** </pre>
   **
   ** @param  resource           the collection of {@link Role} resources to
   **                            assign.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Role}.
   **
   ** @throws SystemException    if an error occurred.
   */
  public void invoke(final List<Role> resource)
    throws SystemException {

    Response response = null;
    try {
      // Keycloak expects an array
      final Entity payload = Entity.entity(MapperFactory.instance.writeValueAsString(resource), contentType());
      response = buildRequest().post(payload);
      if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
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