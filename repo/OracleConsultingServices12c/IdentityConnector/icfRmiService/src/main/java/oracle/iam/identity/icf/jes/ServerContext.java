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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Java Enterprise Service Connector Library

    File        :   ServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServerContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.jes;

import java.util.Properties;

import javax.naming.Context;

import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

///////////////////////////////////////////////////////////////////////////////
// class ServerContext
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>ServerContext</code> wraps the RMI connection.
 ** <p>
 ** Define the test method meaning the wrapped connection is still valid.
 ** <br>
 ** Defines come useful method to work with remote method invocation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerContext<T extends ServerContext> extends AbstractLoggable<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ServerEndpoint endpoint;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerContext</code> which is associated with the
   ** specified {@link ServerEndpoint} for configuration purpose.
   **
   ** @param  endpoint           the {@link ServerEndpoint}
   **                            <code>IT Resource</code> definition where this
   **                            connector is associated with.
   **                            <br>
   **                            Allowed object is {@link ServerEndpoint}.
   */
  protected ServerContext(final ServerEndpoint endpoint) {
    // ensure inherinstance
    super(endpoint);

    // initialize instance attributes
    this.endpoint = endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the {@link ServerEndpoint} this context is using to connect and
   ** perform operations on the Service Provider.
   **
   ** @return                    the {@link ServerEndpoint} this task is
   **                            using to connect and perform operations on the
   **                            Service Provider.
   **                            <br>
   **                            Possible object {@link ServerEndpoint}.
   */
  public final ServerEndpoint endpoint() {
    return this.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerContext</code> which is
   ** associated with the specified {@link ServerEndpoint} as the logger.
   **
   ** @param  endpoint           the {@link ServerEndpoint} IT Resource
   **                            definition where this connector is associated
   **                            with.
   **                            <br>
   **                            Allowed object is {@link ServerContext}.
   **
   ** @return                    the context.
   **                            <br>
   **                            Possible object is
   **                            <code>ServerContext</code>.
   */
  public static ServerContext build(final ServerEndpoint endpoint) {
    return new ServerContext(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** with this task that afterwards can be passed to establish a connection to
   ** the target system.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Managed Server.
   **                            <br>
   **                            Possible object is {@link Properties}.
   */
  protected final Properties environment() {
    return environment(this.endpoint.serviceURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of this task that
   ** afterwards can be passed to establish a connection to the target system.
   **
   ** @param  providerURL        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the context this connector use to communicate
   **                            with the RMI server.
   **                            <br>
   **                            Possible object is {@link Properties}.
   */
  protected final Properties environment(final String providerURL) {
    final Properties environment = new Properties();
    // Set up environment for creating initial context
    environment.put(Context.INITIAL_CONTEXT_FACTORY, this.endpoint.contextFactory());
    environment.put(Context.PROVIDER_URL,            providerURL);
    return environment;
  }
}