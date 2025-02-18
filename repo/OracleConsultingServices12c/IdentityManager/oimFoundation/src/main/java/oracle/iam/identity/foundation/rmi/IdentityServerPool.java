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
    Subsystem   :   Oracle Identity Manager Connector

    File        :   ManagedServerPool.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ManagedServerPool.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.foundation.rmi;

import java.util.HashMap;

import com.oracle.oim.gcp.exceptions.ResourceConnectionCloseException;
import com.oracle.oim.gcp.exceptions.ResourceConnectionCreateException;
import com.oracle.oim.gcp.exceptions.ResourceConnectionValidationxception;

import com.oracle.oim.gcp.resourceconnection.ResourceConnection;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractConnectionPool;

import oracle.iam.identity.foundation.resource.IdentityServerBundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerPool
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ManagedServerPool</code> implements the base functionality
 ** of an Oracle Identity Manager pooled {@link IdentityServer} for a Identity
 ** Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class IdentityServerPool extends    AbstractConnectionPool
                                implements ResourceConnection {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private IdentityServer connection;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityServerPool</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityServerPool() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Returns the connection this {@link ResourceConnection} wrappes.
   **
   ** @return                    the connection this {@link ResourceConnection}
   **                            wrappes.
   */
  protected final IdentityServer connection() {
    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConnection (ResourceConnection)
  @Override
  public ResourceConnection createConnection(final HashMap parameter)
    throws ResourceConnectionCreateException {

    final String method    = "createConnection";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      final IdentityServerResource resource = IdentityServerResource.build(this, parameter);
      final IdentityServerFeature  feature  = IdentityServer.unmarshal(this, resource.feature());
      final IdentityServer         server   = IdentityServer.build(this, resource, feature);
    }
    catch (TaskException e) {
      throw new ResourceConnectionCreateException(IdentityServerBundle.format(IdentityServerError.UNHANDLED, e));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeConnection (ResourceConnection)
  @Override
  public void closeConnection()
    throws ResourceConnectionCloseException {

    final String method = "closeConnection";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      this.connection.disconnect();
    }
    catch (Exception e) {
      fatal(method, e);
      throw new ResourceConnectionCloseException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   heartbeat (ResourceConnection)
  @Override
  public void heartbeat()
    throws ResourceConnectionValidationxception {

    final String method = "heartbeat";
    trace(method, SystemMessage.METHOD_ENTRY);
    if (this.connection != null && this.connection.platform() != null)
      debug(method, "connection is available.");
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isValid (ResourceConnection)
  @Override
  public boolean isValid() {
    final String method = "isValid";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean valid = false;
    valid = (this.connection != null && this.connection.platform() == null);
    debug(method, "connection is valid.");
    trace(method, SystemMessage.METHOD_EXIT);
    return valid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns an object that implements the given interface to allow access to
   ** non-standard methods, or standard methods not exposed by the proxy.
   ** <p>
   ** If the receiver implements the interface then the result is the receiver
   ** or a proxy for the receiver. If the receiver is a wrapper and the wrapped
   ** object implements the interface then the result is the wrapped object or a
   ** proxy for the wrapped object. Otherwise return the result of calling
   ** <code>unwrap</code> recursively on the wrapped object or a proxy for that
   ** result. If the receiver is not a wrapper and does not implement the
   ** interface, then an {@link Exception} is thrown.
   **
   ** @param  <T>                the expected class type.
   ** @param  iface              a {@link Class} defining an interface that the
   **                            result must implement.
   **
   ** @return                    an object that implements the interface. May be
   **                            a proxy for the actual implementing object.
   */
  public final <T> T lookup(final Class<T> iface) {
    return this.connection.platform().getService(iface);
  }
}