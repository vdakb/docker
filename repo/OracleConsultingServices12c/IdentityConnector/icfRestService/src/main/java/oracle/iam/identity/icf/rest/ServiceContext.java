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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic REST Library

    File        :   ServiceContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

///////////////////////////////////////////////////////////////////////////////
// class ServiceContext
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceContext extends AbstractLoggable<ServiceContext> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final ServiceClient client;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceContext</code> which is associated with the
   ** specified {@link ServiceEndpoint} for configuration purpose.
   **
   ** @param  client             the {@link ServiceClient}
   **                            <code>IT Resource</code> definition where this
   **                            connector is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceClient}.
   */
  protected ServiceContext(final ServiceClient client) {
    // ensure inheritance
    super(client);

    // initialize instance attributes
    this.client = client;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Returns the {@link ServiceClient} this context is using to connect and
   ** perform operations on the Service Provider.
   **
   ** @return                    the {@link ServiceClient} this context is
   **                            using to connect and perform operations on the
   **                            Service Provider.
   **                            <br>
   **                            Possible object {@link ServiceClient}.
   */
  public final ServiceClient client() {
    return this.client;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the {@link ServiceEndpoint} this context is using to connect and
   ** perform operations on the Service Provider.
   **
   ** @return                    the {@link ServiceEndpoint} this context is
   **                            using to connect and perform operations on the
   **                            Service Provider.
   **                            <br>
   **                            Possible object {@link ServiceEndpoint}.
   */
  public final ServiceEndpoint endpoint() {
    return this.client.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildTarget
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   ** <p>
   ** Create a JAX-RS web target whose URI refers to the
   ** <code>ServiceEndpoint.contextURL()</code> the JAX-RS / Jersey application
   ** is deployed at.
   ** <p>
   ** This method is an equivalent of calling
   ** <code>client().target(endpoint.contextURL())</code>.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  protected WebTarget buildTarget() {
    return this.client.target(this.client.contextURL());
  }
}