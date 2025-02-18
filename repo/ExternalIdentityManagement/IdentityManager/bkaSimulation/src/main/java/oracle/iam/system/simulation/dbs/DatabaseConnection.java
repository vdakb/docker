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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   DatabaseConnection.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseConnection.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

import java.net.ConnectException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.SQLRecoverableException;

import java.util.Properties;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseConnection
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseConnection</code> implements the base functionality
 ** of an Identity Manager Connector for a Database Service.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseConnection {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String ENVIRONMENT_ACCOUNT  = "user";
  protected static final String ENVIRONMENT_PASSWORD = "password";
  protected static final String ENVIRONMENT_INTERNAL = "internal_logon";
  protected static final String ENVIRONMENT_SYSDBA   = "SYSDBA";

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Attempts to establish a connection to the database by building the
   ** connection properties leveraging the provided parameter.
   **
   ** @param  flavor             the database flavor s one of:
   **                            <ul>
   **                              <li>orcl
   **                              <li>psql
   **                              <li>mysql
   **                            </ul>
   **                            Allowed object is {@link String}.
   ** @param  resource           the resource descriptor providing the
   **                            connection properties
   **                            <br>
   **                            Allowed object is {@link DatabaseResource}.
   **
   ** @return                    a connection to the database.
   **                            <br>
   **                            Possible object is {@link Connection}.
   **
   ** @throws DatabaseException  if a database access error occurs or the url is
   **                            <code>null</code>.
   */
  public static Connection aquire(final String flavor, final DatabaseResource resource)
    throws DatabaseException {

    // prevent bogus input
    if (resource == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "resource");

    final Properties environment = new Properties();
    environment.put(ENVIRONMENT_ACCOUNT,  resource.principalName());
    environment.put(ENVIRONMENT_PASSWORD, resource.principalPassword());

    Connection connection = null;

    int attempts = 1;
    String databaseURL = null;
    switch (flavor) {
      case "orcl" : databaseURL = String.format("jdbc:oracle:thin:@%s:%d/%s", resource.host, resource.port, resource.name);
                    break;
      case "psql" : databaseURL = String.format("jdbc:postgresql://%s:%d/%s", resource.host, resource.port, resource.name);
                    break;
      default     : throw new DatabaseException(DatabaseError.ARGUMENT_BAD_VALUE, flavor);
    }
    do {
      DriverManager.setLoginTimeout(resource.connectionTimeOut());
      try {
        connection = DriverManager.getConnection(databaseURL, environment);
        // always set auto commit to false to delegate the transaction control
        // to  the application
        connection.setAutoCommit(false);
        // some databases like MySQL needs to set the catalog
        // if the database is not specified, the connection is made with no
        // default database. In this case, either call the setCatalog() method
        // on the Connection instance, or fully specify table names using the
        // database name in your SQL
        if (!StringUtility.isEmpty(resource.name))
          connection.setCatalog(resource.name);
        break;
      }
      catch (SQLRecoverableException e) {
        attempts++;
        if (attempts > resource.connectionRetryCount()) {
          if (e.getCause() != null) {
            if (e.getCause() instanceof ConnectException)
              throw new DatabaseException(DatabaseError.CONNECTION_ERROR, e);
          }
          throw new DatabaseException(e);
        }
        try {
          Thread.sleep(resource.connectionRetryInterval());
        }
        catch (InterruptedException ex) {
          throw new DatabaseException(DatabaseError.UNHANDLED,  ex);
        }
      }
      catch (SQLException e) {
        throw new DatabaseException(e);
      }
    } while (attempts <= resource.connectionRetryCount());
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release the obtained JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @param  connection         the connection to close and release back to the
   **                            pool.
   */
  public static void release(final Connection connection) {
    if (connection != null) {
      try {
        // make sure that we will commit our unit of work if neccessary
        connection.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   opened
  /**
   ** Ensures that given JDBC connection is valid in terms of:
   ** <ol>
   **   <li><code>connection</code> is not <code>null</code>.
   **   <li><code>connection</code> is not closed.
   ** </ol>
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @param  connection         the JDBC connection to validate.
   **
   ** @return                    <code>true</code> if the given
   **                            {@link Connection} is non-<code>null</code> and
   **                            not closed; otherwise it returns
   **                            <code>false</code>.
   */
  public static final boolean opened(final Connection connection) {
    if (connection != null) {
      try {
        // make sure that we will commit our unit of work if neccessary
        return !connection.isClosed();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitConnection
  /**
   ** Commit a transaction in supplied connection.
   **
   ** @param  connection         the JDBC connection which will handle the
   **                            commit.
   **
   ** @throws DatabaseException  if connection cannot be commited.
   */
  public static final void commitConnection(final Connection connection)
    throws DatabaseException {

    if (null != connection) {
      try {
        connection.commit();
      }
      catch (SQLException e) {
        throw new DatabaseException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rollbackConnection
  /**
   ** Rollback a transaction in supplied connection
   **
   ** @param  connection         the JDBC connection which will handle the
   **                            rollback.
   **
   ** @throws DatabaseException  if connection cannot be rollback.
   */
  public static final void rollbackConnection(final Connection connection)
    throws DatabaseException {

    if (null != connection) {
      try {
        connection.rollback();
      }
      catch (SQLException e) {
         throw new DatabaseException(e);
      }
    }
  }
}