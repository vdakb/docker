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
    Subsystem   :   Generic Database Connector

    File        :   DatabaseAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.SQLRecoverableException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import oracle.iam.identity.icf.resource.DatabaseBundle;

///////////////////////////////////////////////////////////////////////////////
// class DatabaseContext
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseContext</code> wraps the JDBC connection.
 ** <p>
 ** Define the test method meaning the wrapped connection is still valid.
 ** <br>
 ** Defines come useful method to work with prepared statements.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseContext<T extends DatabaseContext> extends AbstractLoggable<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final DatabaseEndpoint endpoint;

  private Connection             connection;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseContext</code> which is associated with the
   ** specified {@link DatabaseEndpoint} for configuration purpose.
   **
   ** @param  endpoint           the {@link DatabaseEndpoint}
   **                            <code>IT Resource</code> definition where this
   **                            connector is associated with.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   */
  protected DatabaseContext(final DatabaseEndpoint endpoint) {
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
   ** Returns the {@link DatabaseEndpoint} this context is using to connect and
   ** perform operations on the Database Service.
   **
   ** @return                    the {@link DatabaseEndpoint} this context is
   **                            using to connect and perform operations on the
   **                            Database Service.
   **                            <br>
   **                            Possible object {@link DatabaseEndpoint}.
   */
  public final DatabaseEndpoint endpoint() {
    return this.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwrap
  /**
   ** Returns the {@link Connection} this context is using to connect and
   ** perform operations on the database server.
   **
   ** @return                    the {@link Connection} this context is using to
   **                            connect and perform operations on the Database
   **                            Service.
   **                            <br>
   **                            Possible object {@link Connection}.
   */
  public final Connection unwrap() {
    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DatabaseContext</code> which is
   ** associated with the specified {@link DatabaseEndpoint} as the logger.
   **
   ** @param  endpoint           the {@link DatabaseEndpoint} IT Resource
   **                            definition where this connector is associated
   **                            with.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   **
   ** @return                    the context.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseContext</code>.
   */
  public static DatabaseContext build(final DatabaseEndpoint endpoint) {
    return new DatabaseContext(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release the given JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @param  connection         the JDBC connection to release.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   */
  public static final void release(final Connection connection) {
    if (connection != null) {
      try {
        // make sure that we will commit the unit of work if neccessary
        connection.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link Connection} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @return                    the context this connector use to communicate
   **                            with the Database Service.
   **                            <br>
   **                            Possible object {@link Connection}.
   **
   ** @throws SystemException    if the {@link Connection} could not be
   **                            established at the first time this method is
   **                            invoked.
   */
  public Connection connect()
    throws SystemException {

    if (this.connection == null) {
      // Constructs an Database Service context object using environment
      // properties.
      this.connection = aquire();
    }
    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Release the given JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   */
  public final void disconnect() {
    final String method = "disconnect";
    trace(method, Loggable.METHOD_ENTRY);
    release(this.connection);
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Commit a transaction.
   **
   ** @throws SystemException    if connection cannot be commited.
   */
  public final void commit()
    throws SystemException {

    final String method = "commit";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      if (null != connection) {
        this.connection.commit();
      }
    }
    catch (SQLException e) {
      throw DatabaseException.normalized(this.connection, e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rollback
  /**
   ** Rollback a transaction.
   **
   ** @throws SystemException    if connection cannot be rollback.
   */
  public final void rollback()
    throws SystemException {

    final String method = "rollback";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      if (null != this.connection) {
        this.connection.rollback();
      }
    }
    catch (SQLException e) {
      throw DatabaseException.normalized(this.connection, e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Performs changes on a specific user profile.
   **
   ** @param  operation          the SQL statement to execute.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void execute(final String operation)
    throws SystemException {

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);

    // prevent bogus state of instance
    if (this.connection == null) {
      final SystemException e = SystemException.instanceState("connection");
      error(method, e.getMessage());
      trace(method, Loggable.METHOD_EXIT);
      throw e;
    }

    // prevent bogus state of instance
    if (StringUtility.empty(operation)) {
      final SystemException e = SystemException.argumentNull("operation");
      error(method, e.getMessage());
      trace(method, Loggable.METHOD_EXIT);
      throw e;
    }

    Statement statement = null;
    try {
      statement = DatabaseStatement.createStatement(this.connection);
      statement.execute(operation);
    }
    catch (DatabaseException e) {
      fatal(method, e);
      throw e;
    }
    catch (SQLException e) {
      fatal(method, e);
      // produce an exception that take as the code the vendor specific error
      // code. The prefix of the error code of the exception created here will
      // be SQL to separate it form our own error prefix.
      throw DatabaseException.normalized(this.connection.getClass().getPackage(), e);
    }
    finally {
      DatabaseStatement.closeStatement(statement);
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Factory method to create a {@link java.sql.Connection} using the basic
   ** driver manager.
   **
   ** @return                    a valid connection
   **                            <br>
   **                            Possible object {@link Connection}.
   **
   ** @throws SystemException    if the requested <code>Connection</code> could
   **                            not be aquired.
   */
  protected Connection aquire()
    throws SystemException {

    // create the connection base on the configuration.
    final String     url = this.endpoint.serviceURL();
    DriverManager.setLoginTimeout(this.endpoint.timeOutConnect());
    int        attempts = 1;
    Connection ret      = null;
    do {
      try {
        // load the driver class ...
        final Class driver = Class.forName(this.endpoint.databaseDriver());
        info(DatabaseBundle.string(DatabaseMessage.CONNECTING_BEGIN, url, this.endpoint.principalUsername()));
        try {
          ret = DriverManager.getConnection(url, this.endpoint.principalUsername(), CredentialAccessor.string(this.endpoint.principalPassword()));
          // always set auto commit to false to delegate the transaction control
          // to the connector
          ret.setAutoCommit(this.endpoint.enforceAutoCommit());
          // some databases like MySQL needs to set the catalog
          // if the database is not specified, the connection is made with no
          // default database. In this case, either call the setCatalog() method
          // on the Connection instance, or fully specify table names using the
          // database name in your SQL
          if (!StringUtility.empty(this.endpoint.rootContext()))
            ret.setCatalog(this.endpoint.rootContext());
          info(DatabaseBundle.string(DatabaseMessage.CONNECTING_SUCCESS, url, this.endpoint.principalUsername()));
          break;
        }
        catch (SQLRecoverableException e) {
          if (attempts == this.endpoint.retryCount()) {
            // checked exception are not allowed in the access method lets use
            // the exception softening pattern
            throw DatabaseException.from(e, this.endpoint);
          }
          try {
            Thread.sleep(this.endpoint.retryInterval());
          }
          catch (InterruptedException ex) {
            // intentionally left blank
            ;
          }
        }
        catch (SQLException e) {
          // checked exception are not allowed in the access method lets use the
          // exception softening pattern
          throw DatabaseException.normalized(driver.getPackage(), e, this.endpoint.principalUsername());
        }
      }
      catch (ClassNotFoundException e) {
        throw SystemException.classNotFound(this.endpoint.databaseDriver());
      }
    } while (attempts++ < this.endpoint.retryCount());
    return ret;
  }
}