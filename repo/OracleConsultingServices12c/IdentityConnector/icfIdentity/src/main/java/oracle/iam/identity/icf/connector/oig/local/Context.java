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
    Subsystem   :   Identity Governance Connector

    File        :   Context.java
    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.oig.local;

import javax.security.auth.login.LoginException;

import oracle.iam.identity.service.spi.Platform;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.jes.ServerEndpoint;
import oracle.iam.identity.icf.jes.ServerException;

import oracle.iam.identity.icf.connector.oig.Service;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** This is connector server specific context class that extends Java Enterprise
 ** Service interface.
 ** <p>
 ** This is the flavor of the server context if the connector bundle is deployed
 ** on a local connector server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends Service {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The object that implements the given interface to allow access to
   ** non-standard methods, or standard methods not exposed by the proxy.
   */
  protected Platform platform = new Platform();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a local <code>Context</code> which is associated with the
   ** specified {@link ServerEndpoint} for configuration purpose.
   **
   ** @param  endpoint           the {@link ServerEndpoint}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServerEndpoint}.
   */
  private Context(final ServerEndpoint endpoint) {
    // ensure inheritance
    super(endpoint);

    // initialize instance
    this.platform.startup();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstrcat base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shutdown (Service}
  /**
   ** Callback notification to signal dependency injection is done to perform
   ** free up resources and the instance becomes out of service.
   */
  @Override
  public final void shutdown() {
    // initialize instance
    this.platform.shutdown();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service (Service}
  /**
   ** Returns an instance of a Business Facade by invoking the method platform
   ** service resolver to return the appropriate instance of the desired
   ** Business Facade.
   ** <br>
   ** The utility factory keeps track of created Business Facade and on
   ** execution of close() will free all aquired resources of the created
   ** Business Facade.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @param  <T>                the expected class type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @Override
  public final <T> T service(final Class<T> serviceClass) {
    return this.platform.service(serviceClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login (Service)
  /**
   ** Establish a connection to Identity Manager server.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @throws SystemException    if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  public final void login()
    throws SystemException {

    final String method = "login";
    entering(method);
    final ServerEndpoint endpoint = endpoint();
    try {
      // establish the connection to the target system
      this.platform.login(endpoint().principalUsername());
    }
    catch (LoginException e) {
      throw ServerException.from(e, endpoint);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logout (Service)
  /**
   ** Close a connection to Identity Manager server.
   */
  @Override
  public final void logout() {
    final String method = "logout";
    entering(method);
    // release connection to the pool and free up resources
    this.platform.logout();
    exiting(method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a default context.
   **
   ** @param  endpoint           the {@link ServerEndpoint}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServerEndpoint}.
   **
   ** @return                    a default context.
   **                            <br>
   **                            Possible object is <code>Local</code>.
   */
  public static Context build(final ServerEndpoint endpoint) {
    return new Context(endpoint);
  }
}