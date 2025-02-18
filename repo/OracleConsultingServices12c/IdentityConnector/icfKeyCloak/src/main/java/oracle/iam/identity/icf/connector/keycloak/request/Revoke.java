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

    File        :   Revoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Revoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.request;

import oracle.iam.identity.icf.connector.keycloak.schema.Group;
import oracle.iam.identity.icf.connector.keycloak.schema.Role;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.rest.ServiceException;
import oracle.iam.identity.icf.rest.utility.MapperFactory;
import oracle.iam.identity.icf.schema.Resource;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

////////////////////////////////////////////////////////////////////////////////
// class Revoke
// ~~~~~ ~~~~~~
/**
 ** A factory for Keycloak resource delete requests that belongs to entitlements
 ** of a user.
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
public class Revoke<T extends Resource> extends Delete<T, Revoke> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST delete request that will DELETE the given resource
   ** to the given web target.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Revoke(final WebTarget target) {
    // ensure inheritance
    super(target.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
   **                            Possible object is <code>Revoke</code> for type
   **                            {@link Role}.
   */
  public static Revoke<Role> role(final WebTarget target, final String beneficiary) {
    return new Revoke<>(target.path(ENDPOINT_USER).path(beneficiary).path(ENDPOINT_MEMBER).path("realm"));
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
   **                            Possible object is <code>Revoke</code> for type
   **                            {@link Role}.
   */
  public static Revoke<Role> role(final WebTarget target, final String beneficiary, final String client) {
    return new Revoke<>(target.path(ENDPOINT_USER).path(beneficiary).path(ENDPOINT_MEMBER).path(ENDPOINT_CLIENT).path(client));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
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
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a group
   **                            not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Modify} for type
   **                            <code>T</code>.
   */
  public static Revoke<Group> group(final WebTarget target, final String beneficiary, final String id) {
    return new Revoke<>(target.path(ENDPOINT_USER).path(beneficiary).path(ENDPOINT_GROUP).path(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST delete request using DELETE.
   ** <br>
   ** Keycloak rapes many REST methods to create a so-called "fine-grained" API.
   ** <br>
   ** Nonsense.
   ** <br>
   ** <a href="https://stackoverflow.com/questions/299628">is not strictly forbidden</a>
   ** A DELETE with an entity body <a href="https://stackoverflow.com/questions/299628">is not strictly forbidden</a>
   ** but it's very uncommon and ignored by some frameworks/servers. The need of
   ** an entity body may indicate that a DELETE is not used as it is intended.
   ** <p>
   ** For instance:
   ** <br>
   ** If a <code>GET /customers/4711</code> returns one customer and you send a
   ** <code>DELETE /customers/4711</code> the next <code>GET</code> on this
   ** resource should return a <code>404</code>. You deleted a resource
   ** <b>identified by a URL</b> like
   ** <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.7">defined in the spec</a>.
   **
   ** @param  resource           the {@link Role} resource to revoke.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @throws SystemException    if an error occurred.
   */
  public void invoke(final List<Role> resource)
    throws SystemException {

    Response response = null;
    try {
      // Keycloak expects an array
      final Entity payload = Entity.entity(MapperFactory.instance.writeValueAsString(resource), contentType());
      response = buildRequest().method("DELETE", payload);

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