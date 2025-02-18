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

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.request;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.connector.grafana.schema.User;
import oracle.iam.identity.icf.connector.grafana.schema.Team;
import oracle.iam.identity.icf.connector.grafana.schema.Organization;

import oracle.iam.identity.icf.connector.grafana.response.CreateResult;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** A factory for Grafana resource create requests.
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
   ** Build a request to create the provided Grafana resource at the Service
   ** Provider.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>Create</code> for
   **                            type {@link User}.
   */
  public static Create<User> user(final WebTarget target) {
    return new Create<User>(target.path("admin").path("users"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Build a request to create the provided Grafana resource at the Service
   ** Provider.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>Create</code> for
   **                            type {@link Team}.
   */
  public static Create<Team> team(final WebTarget target) {
    return new Create<Team>(target.path("teams"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Build a request to create the provided Grafana resource at the Service
   ** Provider.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>Create</code> for
   **                            type {@link Organization}.
   */
  public static Create<Organization> organization(final WebTarget target) {
    return new Create<Organization>(target.path("orgs"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST create request using POST.
   **
   ** @param  resource           the REST resource to POST.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the REST resource of the result.
   **                            <br>
   **                            Possible object is {@link CreateResult}.
   **
   ** @throws SystemException    if an error occurred.
   */
  public CreateResult invoke(final T resource)
    throws SystemException {

    Response response = null;
    try {
      final Entity payload = Entity.entity(MapperFactory.instance.writeValueAsString(resource), contentType());
      response = buildRequest().post(payload);
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        return MapperFactory.instance.readValue(response.readEntity(InputStream.class), CreateResult.class);
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