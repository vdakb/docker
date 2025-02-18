/*
    Oracle Deutschland GmbH

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
    Subsystem   :   Atlassian Jira Connector

    File        :   Assign.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Assign.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.request;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import oracle.iam.identity.icf.connector.jira.schema.Adressable;
import oracle.iam.identity.icf.connector.jira.schema.Group;
import oracle.iam.identity.icf.connector.jira.schema.User;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.rest.ServiceException;
import oracle.iam.identity.icf.rest.request.Request;

////////////////////////////////////////////////////////////////////////////////
// class Assign
// ~~~~~ ~~~~~~
/**
 ** A factory for REST create requests for memberships.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Assign<T extends Adressable> extends Request<Assign> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The default string format to assign a user to resource
   */
  public static final String     DEFAULT_BODY_FORMAT   = "{\"user\":[\"%s\"]}";
  /**
   ** The string format to assign a user to group
   */
  public static final String     GROUP_BODY_FORMAT     = "{\"name\":\"%s\"}";
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
  private Assign(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic create resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Create</code>.
   */
  public static Assign build(final WebTarget target) {
    return new Assign(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST create request using POST.
   **
   ** @param  beneficiary        The jira {@link User} identifier to assign by
   **                            a POST request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  <T>                the type of objects to return.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the {@link User} resource,
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if an error occurred.
   */
  public <T extends Adressable> T invoke(final String beneficiary, final Class<T> clazz)
    throws SystemException {

    Response response = null;
    try {
      final Entity entity = Entity.entity(String.format(Group.class == clazz ? GROUP_BODY_FORMAT : DEFAULT_BODY_FORMAT, beneficiary), contentType());
      response = buildRequest().post(entity);
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // the request is answered with a response body that we need at first to
        // parse and wrap in a java content type for later use
        return response.readEntity(clazz);
      }
      else {
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