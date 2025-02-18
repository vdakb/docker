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

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.request;

import java.lang.reflect.Array;

import java.util.List;
import java.util.Arrays;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import oracle.iam.identity.icf.connector.keycloak.schema.*;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** A factory for Keycloak resource search requests.
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
public class Search<T extends Resource> extends Abstract<T, Search> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The parameter name of the 0-based index of the first query result.
   */
  public static final String START  = "first";
  /**
   ** The parameter name of the integer indicating the desired maximum number of
   ** query results per page.
   */
  public static final String COUNT  = "max";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The 0-based index of the first result in the current set of list results.
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
   ** @param  start              the 0-based index of the first query result.
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
   ** from the Service Provider belonging to users.
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
    return new Search<User>(target.path(ENDPOINT_USER));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to user credentials.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link Credential}.
   */
  // FIXME: ist this is required
  public static Search<Credential> credential(final WebTarget target, final String id) {
    return new Search<Credential>(target.path(ENDPOINT_USER).path(id).path(ENDPOINT_CREDENTIAL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOfGroup
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to user groups.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link Group}.
   */
  public static Search<Group> memberOfGroup(final WebTarget target, final String id) {
    return new Search<Group>(target.path(ENDPOINT_USER).path(id).path(ENDPOINT_GROUP));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to roles.
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
    return new Search<Role>(target.path(ENDPOINT_ROLE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to clients.
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
   **                            {@link Client}.
   */
  public static Search<Client> client(final WebTarget target) {
    return new Search<>(target.path(ENDPOINT_CLIENT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientRole
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to clients.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  clientId           the identifier of the client the search belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search} for type
   **                            {@link Role}.
   */
  public static Search<Role> clientRole(final WebTarget target, final String clientId) {
    return new Search<>(target.path(ENDPOINT_CLIENT).path(clientId).path(ENDPOINT_ROLE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to groups.
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
   **                            {@link Group}.
   */
  public static Search<Group> group(final WebTarget target) {
    return new Search<Group>(target.path(ENDPOINT_GROUP));
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
    WebTarget target = super.build();
    if (this.start != null && this.count != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(START, this.start).queryParam(COUNT, this.count);
    }

    return target;
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
   **                            Possible object is array of <code>T</code>.
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
        // any search on Keycloak returns always an anonymous array
        return Arrays.asList(MapperFactory.instance.readerForArrayOf(clazz).readValue(response.readEntity(InputStream.class)));
      }
      // special care needs to take in acount on resources that been searched
      // some of them are responding HTTP-409 instead to return en empty result
      // an example that requires such special treatment because are query roles
      // of a user if that the user has no roles assigned.
      else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
        return Arrays.asList((T[])Array.newInstance(clazz, 0));
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
      if (response != null) {
        response.close();
      }
    }
  }
}