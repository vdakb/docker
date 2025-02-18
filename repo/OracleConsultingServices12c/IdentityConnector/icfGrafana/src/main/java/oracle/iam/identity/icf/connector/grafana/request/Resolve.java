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

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.request;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import oracle.iam.identity.icf.connector.grafana.response.ListResult;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.grafana.schema.User;
import oracle.iam.identity.icf.connector.grafana.schema.Team;
import oracle.iam.identity.icf.connector.grafana.schema.Organization;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
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
public class Resolve<T extends Resource> extends Abstract<T, Resolve> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new REST lookup request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Resolve()" and enforces use of the public method below.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Resolve(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Factory method to create a {@link User} lookup resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>userName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Resolve</code> for
   **                            type {@link User}.
   */
  @SuppressWarnings("unchecked")
  public static Resolve<User> user(final WebTarget target, final String name) {
    return new Resolve<User>(target.path("users/lookup").queryParam("loginOrEmail", name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Factory method to create a {@link Team} lookup resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>name</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Resolve</code> for
   **                            type {@link Team}.
   */
  @SuppressWarnings("unchecked")
  public static Resolve<Team> team(final WebTarget target, final String name) {
    return new Resolve<Team>(target.path("teams").path("search").queryParam("name", name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Factory method to create an {@link Organization} lookup resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>name</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Resolve</code> for
   **                            type {@link Organization}.
   */
  @SuppressWarnings("unchecked")
  public static Resolve<Organization> organization(final WebTarget target, final String name) {
    return new Resolve<Organization>(target.path("orgs").path("name").path(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST resolve request using GET to retrieve a certain
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
  public Team invoke()
    throws SystemException {

    Response response = null;
    try {
      response = buildRequest().get();
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // any search for teams on Grafana returns a list result
        final ListResult result = MapperFactory.instance.readValue(response.readEntity(InputStream.class), ListResult.class);
        return (result.total() != 1) ? null : result.resource().get(0);
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