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

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Resolve.


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
import oracle.iam.identity.icf.connector.keycloak.schema.Service;
import oracle.iam.identity.icf.connector.keycloak.schema.Translator;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
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
    return new Resolve<User>(target.path(ENDPOINT_USER)).filter(Translator.criteria(Service.USERNAME, name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Factory method to create a {@link Role} lookup resource request.
   ** <p>
   ** Keycloak's API is very inconsistent, different strategies for finding REST
   ** resources must be applied to different endpoints to achieve a satisfactory
   ** result.
   ** <br>
   ** In order to find a specific role, a filter is necessary, but this filter
   ** is limited to the parameter <code>search</code>, while for other endpoints
   ** several parameters can be allied.
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
   **                            type {@link Role}.
   */
  @SuppressWarnings("unchecked")
  public static Resolve<Role> role(final WebTarget target, final String name) {
    return new Resolve<Role>(target.path(ENDPOINT_ROLE).queryParam("search", name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Factory method to create a {@link Group} lookup resource request.
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
   **                            type {@link Group}.
   */
  @SuppressWarnings("unchecked")
  public static Resolve<Group> group(final WebTarget target, final String name) {
    return new Resolve<Group>(target.path(ENDPOINT_GROUP).queryParam("search", name));
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
        // any search on Keycloak returns always an anonymous array but that can
        // be empty hence we have to go an extra mile to have an appropriate
        // value
        final T[] result = MapperFactory.instance.readerForArrayOf(clazz).readValue(response.readEntity(InputStream.class));
        return result.length == 0 ? null : result[0];
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