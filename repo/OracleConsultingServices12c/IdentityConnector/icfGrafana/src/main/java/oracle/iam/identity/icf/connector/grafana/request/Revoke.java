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

    File        :   Revoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Revoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.request;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.connector.grafana.schema.Entity;

////////////////////////////////////////////////////////////////////////////////
// class Revoke
// ~~~~~ ~~~~~~
/**
 ** A factory for Grafana revoke requests.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Revoke extends Abstract<Entity, Revoke> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST grant request that will DELETE the given resource
   ** from the given web target.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Revoke(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Build a request to modify member privileges at a known role resource
   ** represented by <code>id</code> at the Service Provider.
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
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            role not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  beneficiary        the <code>Entity</code> usually a
   **                            <code>User</code> to revoke from the team
   **                            represented by <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Revoke}.
   */
  public static <T extends Entity> Revoke role(final WebTarget target, final String id, final String beneficiary) {
    // DELETE /api/access-control/users/1/roles/AFUXBHKnk
    return new Revoke(target.path("access-control").path(id).path("users").path(beneficiary).path("roles").path(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Build a request to modify member privileges at a known team resource
   ** represented by <code>id</code> at the Service Provider.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            team not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  beneficiary        the <code>Entity</code> usually a
   **                            <code>User</code> to revoke from the team
   **                            represented by <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Revoke}.
   */
  public static Revoke team(final WebTarget target, final String id, final String beneficiary) {
    // DELETE /api/teams/2/members/3
    return new Revoke(target.path("teams").path(id).path("members").path(beneficiary));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Build a request to modify member privileges at a known organization
   ** resource represented by <code>id</code> at the Service Provider.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of an
   **                            organization not the name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  beneficiary        the <code>Entity</code> usually a
   **                            <code>User</code> to revoke from the
   **                            organization represented by <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Revoke}.
   */
  public static Revoke organization(final WebTarget target, final String id, final String beneficiary) {
    return new Revoke(target.path("orgs").path(id).path("users").path(beneficiary));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST assign operation request using PUT.
   **
   ** @throws SystemException    if an error occurred.
   */
  public void invoke()
    throws SystemException {

    Response response = null;
    try {
      response = buildRequest().delete();
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
}