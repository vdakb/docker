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

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Search.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.request;

import java.util.List;
import java.util.Arrays;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import javax.ws.rs.core.Response;

import oracle.iam.identity.icf.connector.grafana.response.ListResult;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.grafana.schema.User;
import oracle.iam.identity.icf.connector.grafana.schema.Role;
import oracle.iam.identity.icf.connector.grafana.schema.Team;
import oracle.iam.identity.icf.connector.grafana.schema.RoleMember;
import oracle.iam.identity.icf.connector.grafana.schema.Organization;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** A factory for Grafana resource search requests.
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
public class Search<T extends Resource> extends Abstract<T, Search> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The parameter name of the 1-based index of the first page result.
   */
  public static final String START = "page";
  /**
   ** The parameter name of the integer indicating the desired maximum number of
   ** query results per page.
   */
  public static final String COUNT = "perpage";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The 1-based index of the first page in the current set of list results.
   */
  private Integer            start;
  /**
   ** The number of resources returned in a list response page.
   */
  private Integer            count;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST search request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Search()" and enforces use of the public method below.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Search(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   page
  /**
   ** Sets the pagination request of resources.
   **
   ** @param  start              the 1-based index of the first page result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>Search</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Search</code>.
   */
  public Search<T> page(final int start, final int count) {
    this.start = start;
    this.count = count;
    return this;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to {@link User}s.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Grafana expose the endpoint as <code>GET /api/users</code>. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link User}.
   */
  public static Search<User> user(final WebTarget target) {
    return new Search<User>(target.path("users"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Build a request to the endpoint thta's used to retrieve information about
   ** the resource type {@link Role}s provided by the Service Provider.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Grafana expose the endpoint as <code>GET /api/access-control/roles</code>. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link Role}.
   */
  public static Search<Role> role(final WebTarget target) {
    return new Search<Role>(target.path("access-control/roles"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to {@link RoleMember}s.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Grafana expose the endpoint as
   ** <code>GET /api/access-control/users/{id}/roles</code> where
   ** <code>id</code> the internal identifier of a user not the name. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link RoleMember}.
   */
  public static Search<RoleMember> role(final WebTarget target, final String id) {
    return new Search<RoleMember>(target.path("access-control/users").path(id).path("roles"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to {@link Team}s.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Grafana expose the endpoint as <code>GET /api/teams</code> where. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link Team}.
   */
  public static Search<Team> team(final WebTarget target) {
    return new Search<Team>(target.path("teams").path("search"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Build a request to query and retrieve resources of a resource type
   ** {@link Team} assigned to a {@link User} from the Service Provider.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Grafana expose the endpoint as <code>GET /api/users/{id}/teams</code>
   ** where <code>id</code> the internal identifier of a user not the name. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link Team}.
   */
  public static Search<Team> team(final WebTarget target, final String id) {
    return new Search<Team>(target.path("users").path(id).path("teams"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Build a search request to the endpoint is used to retrieve information
   ** about the resource type {@link Organization} provided by the Service
   ** Provider.
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
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link Organization}.
   */
  public static Search<Organization> organization(final WebTarget target) {
    return new Search<Organization>(target.path("orgs"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Build a search request to the endpoint is used to retrieve information
   ** about the resource type {@link Organization} granted to a {@link User}
   ** represented by <code>id</code> provided by the Service Provider.
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
   ** Grafana expose the endpoint as <code>GET /api/users/{id}/orgs</code>
   ** where <code>id</code> the internal identifier of a user not the name. 
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link Organization}.
   */
  public static Search<Organization> organization(final WebTarget target, final String id) {
    return new Search<Organization>(target.path("users").path(id).path("orgs"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build (overidden)
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   **
   ** @return                    the {@link WebTarget} for the request.
   */
  @Override
  protected final WebTarget build() {
    // ensure inheritance
    WebTarget target = super.build();
    // apply the start page query parameter if defined
    if (this.start != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(START, this.start);
    }
    // apply the desired maximum number of query results per page
    if (this.count != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(COUNT, this.count);
    }

    return target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listTeam
  /**
   ** Invoke the REST search request using GET to retrieve a collection of
   ** {@link Team}.
   **
   ** @return                    the {@link List} containing the search results.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Team}.
   **
   ** @throws SystemException   if an error occurred.
   */
  @SuppressWarnings("unchecked")
  public List<Team> listTeam()
    throws SystemException {

    Response response = null;
    try {
      response = buildRequest().get();
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        final ListResult result = MapperFactory.instance.readValue(response.readEntity(InputStream.class), ListResult.class);
        return result.resource();
      }
      else {
        throw ExceptionParser.from(response);
      }
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    catch (Exception e) {
      throw ServiceException.unhandled(e);
    }
    finally {
      response.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST search request using GET to retrieve an unamed array and
   ** returns an appropriate converted collection of type <code>T</code>.
   **
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the {@link List} containing the search results.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @throws SystemException   if an error occurred.
   */
  @SuppressWarnings("unchecked")
  public List<T> invoke(final Class<T> clazz)
    throws SystemException {

    Response response = null;
    try {
      response = buildRequest().get();
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // any search on Grafana returns always an anonymous array with teams as
        // an exception
        // searching for teams returns an object the represents a list result.
        return Arrays.asList(MapperFactory.instance.readerForArrayOf(clazz).readValue(response.readEntity(InputStream.class)));
      }
      else {
        throw ExceptionParser.from(response);
      }
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    catch (Exception e) {
      throw ServiceException.unhandled(e);
    }
    finally {
      response.close();
    }
  }
}