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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   GitLab Connector

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.gitlab;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends ServiceContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link User}s provided by the Service Provider.
   */
  static final String     ENDPOINT_USER    = "users";

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link User}s provided by the Service Provider.
   */
  static final String     ENDPOINT_GROUP   = "groups";

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link User}s provided by the Service Provider.
   */
  static final String     ENDPOINT_PROJECT = "projects";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the endpoint to access the GutLab API **/
  private final WebTarget target;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Context</code> which is associated with the specified
   ** {@link ServiceClient} for configuration purpose.
   **
   ** @param  client             the {@link ServiceClient}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceClient}.
   */
  private Context(final ServiceClient client) {
    // ensure inheritance
    super(client);

    // initialize instance attributes
    this.target = client.target(client.endpoint().contextURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a default context.
   **
   ** @param  client             the {@link ServiceClient}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceClient}.
   **
   ** @return                    a default context.
   **                            <br>
   **                            Possible object is {@link ServiceContext}.
   */
  public static Context build(final ServiceClient client) {
    return new Context(client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourcePing
  /**
   ** Returns the resource types supported by the Service Provider.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void resourcePing()
      throws SystemException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAccount
  /**
   ** Build a request to query and retrieve GitLab User resources from the
   ** Service Provider.
   **
   **
   ** @throws SystemException    if an error occurs.
   */
  public void searchAccount()
    throws SystemException {

    final String method = "searchAccount";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, Loggable.METHOD_EXIT);
  }
}