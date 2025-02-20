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

    File        :   Assign.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Assign.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.request;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class Assign
// ~~~~~ ~~~~~~
/**
 ** A factory for Grafana grant requests.
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
public class Assign<T extends Resource> extends Abstract<T, Assign> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST grant request that will POST the given resource to
   ** the given web target.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Assign(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Build a request to modify member privileges at a known team resource
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
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Assign}.
   */
  public static <T extends Resource> Assign<T> team(final WebTarget target, final String id) {
    return new Assign<T>(target.path("teams").path(id).path("members"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Build a request to modify member privileges at a known organization
   ** resource represented by <code>id</code> at the Service Provider.
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
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Assign}.
   */
  public static <T extends Resource> Assign<T> organization(final WebTarget target, final String id) {
    return new Assign<T>(target.path("orgs").path(id).path("users"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST assign operation request using PUT.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the internal identifier of a
   **                            user not the login name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurred.
   */
  public void invoke(final String beneficiary)
    throws SystemException {

    Response response = null;
    try {
      final StringBuilder builder = new StringBuilder("{");
      builder.append("\"userId\":").append(beneficiary);
      builder.append("}");
      response = buildRequest().post(Entity.json(builder.toString()));
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST assign operation request using PUT.
   **
   ** @param  beneficiary        the unique identifier of a <code>User</code>
   **                            account.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the unique identifier of a
   **                            user not the internal identifier.
   **                            <br>
   **                            Allowed object is {@link String}..
   ** @param  role               the permission granted to the given resource
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurred.
   */
  public void invoke(final String beneficiary, final String role)
    throws SystemException {

    Response response = null;
    try {
      final StringBuilder builder = new StringBuilder("{");
      builder.append("\"loginOrEmail\":\"").append(beneficiary).append("\"");
      builder.append(",");
      builder.append("\"role\":\"").append(role).append("\"");
      builder.append("}");
      response = buildRequest().post(javax.ws.rs.client.Entity.json(builder.toString()));
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