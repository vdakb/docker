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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.request;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import javax.ws.rs.core.Response;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.grafana.schema.User;
import oracle.iam.identity.icf.connector.grafana.schema.Role;
import oracle.iam.identity.icf.connector.grafana.schema.Team;
import oracle.iam.identity.icf.connector.grafana.schema.Organization;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** A factory for Grafana resource lookup requests.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Usually we use constant declarations to evaluate the targets of an endpoint.
 ** <br>
 ** Grafana, caught in its own stupidity, exposes a very inconsistent API, which
 ** is why the attempt to represent this via constants failed.
 ** <br>
 ** For this reason, all calculations of paths to endpoints involve hard-coded
 ** strings.
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
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Lookup<T extends Resource> extends Abstract<T, Lookup> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new REST lookup request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Lookup()" and enforces use of the public method below.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Lookup(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Build a lookup request to the endpoint is used to retrieve information
   ** about the resource type {@link User} represented by <code>id</code>
   ** provided by the Service Provider.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Grafana expose the endpoint as <code>GET /api/users</code>. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to
   **                            match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Lookup</code> for type
   **                            {@link User}.
   */
  @SuppressWarnings("unchecked")
  public static Lookup<User> user(final WebTarget target, final String id) {
    return new Lookup<User>(target.path("users").path(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Build a lookup request to the endpoint is used to retrieve information
   ** about the resource type {@link Role} represented by <code>id</code>
   ** provided by the Service Provider.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Grafana expose the endpoint as <code>GET /api/access-control/roles</code>. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to
   **                            match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            role not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Lookup</code> for type
   **                            {@link Role}.
   */
  @SuppressWarnings("unchecked")
  public static Lookup<Role> role(final WebTarget target, final String id) {
    return new Lookup<Role>(target.path("access-control/roles").path(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Build a lookup request to the endpoint is used to retrieve information
   ** about the resource type {@link Team} represented by <code>id</code>
   ** provided by the Service Provider.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Grafana expose the endpoint as <code>GET /api/teams</code>. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to
   **                            match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            team not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Lookup</code> for type
   **                            {@link Team}.
   */
  @SuppressWarnings("unchecked")
  public static Lookup<Team> team(final WebTarget target, final String id) {
    return new Lookup<Team>(target.path("teams").path(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Build a lookup request to the endpoint is used to retrieve information
   ** about the resource type {@link Organization} represented by
   ** <code>id</code> provided by the Service Provider.
   ** <p>
   ** The <code>Organization</code> API is divided in two resources,
   ** <code>/api/org</code> (current organization) and <code>/api/orgs</code>
   ** (admin organizations).
   ** <br>
   ** One big difference between these are that the admin of all organizations
   ** API only works with basic authentication, see
   ** <a href="https://grafana.com/docs/grafana/latest/developers/http_api/org/#admin-organizations-api">Admin Organizations API</a>
   ** for more information.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Grafana expose the endpoint as <code>GET /api/orgs</code>. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to
   **                            match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of an
   **                            organization not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Lookup</code> for type
   **                            {@link Organization}.
   */
  @SuppressWarnings("unchecked")
  public static Lookup<Organization> organization(final WebTarget target, final String id) {
    return new Lookup<Organization>(target.path("orgs").path(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST lookup request.
   **
   ** @param  <T>                the type of resource to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully lookedup REST resource.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws SystemException    if the REST service provider responded with an
   **                            error.
   */
  @SuppressWarnings("unchecked")
  public <T> T invoke(final Class<T> clazz)
    throws SystemException {

    final Response response = buildRequest().get();
    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        return MapperFactory.instance.readValue(response.readEntity(InputStream.class), clazz);
      }
      else {
        throw ServiceException.from(response);
      }
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    finally {
      response.close();
    }
  }
}