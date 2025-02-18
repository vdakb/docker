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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   ConnectionPool.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectionPool.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.control;

import java.util.HashMap;

import com.oracle.oim.gcp.resourceconnection.ResourceConnection;

import com.oracle.oim.gcp.exceptions.ResourceConnectionCreateException;
import com.oracle.oim.gcp.exceptions.ResourceConnectionCloseException;
import com.oracle.oim.gcp.exceptions.ResourceConnectionValidationxception;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractConnectionPool;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.sap.service.resource.ConnectionBundle;

////////////////////////////////////////////////////////////////////////////////
// class ConnectionPool
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>ConnectionPool</code> implements the base functionality
 ** of an Oracle Identity Manager pooled {@link Connection} for a SAP system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class ConnectionPool extends    AbstractConnectionPool
                            implements ResourceConnection {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Connection connection;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ConnectionPool</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ConnectionPool() {
    // ensure inheritance
    super();
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
      final Resource resource  = new Resource(this, parameter);
      this.connection = new Connection(this, resource, Connection.unmarshal(this, resource.feature()));
      this.connection.connect();
    }
    catch (TaskException e) {
      throw new ResourceConnectionCreateException(TaskBundle.format(TaskError.UNHANDLED, e));
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
    this.connection.destinationName();
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isValid (ResourceConnection)
  @Override
  public boolean isValid() {
    final String method = "isValid";
    trace(method, SystemMessage.METHOD_ENTRY);
    boolean valid = false;
    if (this.connection == null)
      return valid;

    try {
      this.connection.ping();
      valid = true;
      debug(method, ConnectionBundle.format(ConnectionMessage.CONNECTION_VALID, this.connection.destinationName()));
    }
    catch (Exception e) {
      error(method, ConnectionBundle.format(ConnectionMessage.CONNECTION_INVALID, this.connection.destinationName()));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return valid;
  }
}