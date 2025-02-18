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

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.request;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.keycloak.schema.User;
import oracle.iam.identity.icf.connector.keycloak.schema.Role;
import oracle.iam.identity.icf.connector.keycloak.schema.Group;
import oracle.iam.identity.icf.connector.keycloak.schema.Member;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** A factory for Keycloak resource lookup requests.
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
   ** Factory method to create a {@link User} lookup resource request.
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
   **                            This has to be the system identifier of a user
   **                            not the login name.
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
    return new Lookup<User>(target.path(ENDPOINT_USER).path(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOf
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to user roles.
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
   **                            This has to be the system identifier of a user
   **                            not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>Lookup</code> for type
   **                            {@link Member}.
   */
  public static Lookup<Member> member(final WebTarget target, final String id) {
    return new Lookup<Member>(target.path(ENDPOINT_USER).path(id).path(ENDPOINT_MEMBER));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Factory method to create a {@link Role} lookup resource request.
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
   **                            This has to be the system identifier of a role
   **                            not the name.
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
    return new Lookup<Role>(target.path("roles-by-id").path(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Factory method to create a {@link Group} lookup resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute) to match.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a group
   **                            not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Lookup</code> for type
   **                            {@link Group}.
   */
  @SuppressWarnings("unchecked")
  public static Lookup<Group> group(final WebTarget target, final String id) {
    return new Lookup<Group>(target.path(ENDPOINT_GROUP).path(id));
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