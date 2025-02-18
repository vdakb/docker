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

    File        :   Abstract.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Abstract.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.request;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.request.Request;

////////////////////////////////////////////////////////////////////////////////
// abstract class Abstract
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** The payload for a request that can be used to access data for all Keycloak
 ** REST resource.
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
 ** @param  <R>                  the type of resource request.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the resources
 **                              implementing this class (resources can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;R&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Abstract<T extends Resource, R extends Abstract> extends Request<R> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type <code>User</code> provided by the Service Provider.
   */
  public static final String            ENDPOINT_USER       = "users";
  /**
   ** An HTTP URI to the endpoint is used to reset password for a
   ** <code>User</code> provided by the Service Provider.
   */
  public static final String            ENDPOINT_CREDENTIAL = "credentials";
  /**
   ** An HTTP URI to the endpoint is used to reset password for a
   ** <code>User</code> provided by the Service Provider.
   */
  public static final String            ENDPOINT_PASSWORD   = "reset-password";
  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type <code>Role</code> provided by the Service Provider.
   */
  public static final String            ENDPOINT_ROLE       = "roles";
  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type <code>Client</code> provided by the Service Provider.
   */
  public static final String            ENDPOINT_CLIENT      = "clients";
  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type <code>Role</code> granted to a user provided by the Service
   ** Provider.
   */
  public static final String            ENDPOINT_MEMBER     = "/role-mappings";
  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type <code>Group</code> provided by the Service Provider.
   */
  public static final String            ENDPOINT_GROUP      = "groups";
  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type <code>ActionType</code> provided by the Service Provider.
   */
  public static final String            ENDPOINT_ACTION     = "authentication/required-actions";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The filter expressions used to request a subset of resources.
   */
  protected String filter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  protected Abstract(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Sets the filtering request of resources.
   **
   ** @param  filter             the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Abstract</code> request to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>R</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final R filter(final String filter) {
    this.filter = filter;
    return (R)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build (overidden)
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   **
   ** @return                    the {@link WebTarget} for the request.
   */
  @Override
  protected WebTarget build() {
    WebTarget target = super.build();
    if (this.filter != null) {
      // from the docs:
      target = target.queryParam("q", this.filter);
    }
    return target;
  }
}