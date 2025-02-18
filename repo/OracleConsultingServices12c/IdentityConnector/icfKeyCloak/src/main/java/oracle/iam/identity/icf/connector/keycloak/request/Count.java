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

    File        :   Count.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Count.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.request;

import java.util.List;

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

////////////////////////////////////////////////////////////////////////////////
// class Count
// ~~~~~ ~~~~~
/**
 ** A factory for Keycloak resource count requests.
 ** <p>
 ** Meanwhile I start to hate Keycloak. The API isn't only inconsistent; its
 ** crap.
 ** <br>
 ** Each endpoint implements its own behavior that requires speciel treatments
 ** to get a proper result. As an example <code>/users</code> and
 ** <code>/roles</code> returning on a <code>count</code> endpoint a simple
 ** numeric value as a result (obviously the developers are to lazy to form a
 ** proper JSON-Object). Nevertheless <code>/groups</code> returning on a
 ** <code>count</code> endpoint a JSON-Object <code>{"count" : n}</code>. So we
 ** have to implement this specific object only for that purpose.
 ** <p>
 ** Lord throw brains out of heaven.
 **
 ** @param  <T>                  the type of the return value.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Count<T extends Number> extends Abstract<Resource, Count> {

  //////////////////////////////////////////////////////////////////////////////
  // sttic final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PATH = "count";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST resource count request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Count()" and enforces use of the public method below.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Count(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Build a request to count resources of a single resource type from the
   ** Service Provider belonging to users.
   **
   ** @param  <T>                the type of value to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>Count</code> for
   **                            {@link Number} of type <code>T</code>.
   */
  public static <T extends Number> Count<T> user(final WebTarget target) {
    return new Count<T>(target.path(ENDPOINT_USER).path(PATH));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Build a request to count resources of a single resource type from the
   ** Service Provider belonging to roles.
   **
   ** @param  <T>                the type of value to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>Count</code> for
   **                            {@link Number} of type <code>T</code>.
   */
  public static <T extends Number> Count<T> role(final WebTarget target) {
    return new Count<T>(target.path(ENDPOINT_ROLE).path(PATH));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Build a request to count resources of a single resource type from the
   ** Service Provider belonging to groups.
   **
   ** @param  <T>                the type of value to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>Count</code> for
   **                            {@link Number} of type <code>T</code>.
   */
  public static <T extends Number> Count<T> group(final WebTarget target) {
    return new Count<T>(target.path(ENDPOINT_GROUP).path(PATH));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST count request using GET to retrieve a numeric value for
   ** the ammount of resource type <code>T</code> at the Service Provider.
   **
   ** @param  clazz              the Java class type used to determine the
   **                            {@link Number} type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the {@link List} containing the search results.
   **                            <br>
   **                            Possible object is {@link Number} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException   if an error occurred.
   */
  public T invoke(final Class<T> clazz)
    throws SystemException {

    Response response = null;
    try {
      response  = buildRequest().get();
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // this is a hack to solve the unmarshalling of an entity if the
        // connector runs in the embedded Conncetor Server deployed in Identity
        // Manager
        // The native call of readEntity fails with an empty entity without any
        // exception; Aaaargh
        // the reason is that the org.eclipse.persistent JsonStructureReader
        // kicks in that isnt't able to resolve to JSON-POJO relation properly.
        // The solution is to explicitly create a Jackson parser for this
        // purpose to bypass the standard implementation
        return MapperFactory.instance.readerFor(clazz).readValue(response.readEntity(InputStream.class));
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
    finally {
      response.close();
    }
  }
}