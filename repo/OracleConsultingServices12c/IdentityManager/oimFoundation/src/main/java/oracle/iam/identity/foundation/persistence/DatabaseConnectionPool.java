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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DatabaseConnectionPool.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseConnectionPool.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import java.util.concurrent.Executor;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Array;
import java.sql.Struct;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.SQLWarning;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;

import com.oracle.oim.gcp.resourceconnection.ResourceConnection;

import com.oracle.oim.gcp.exceptions.ResourceConnectionCreateException;
import com.oracle.oim.gcp.exceptions.ResourceConnectionCloseException;
import com.oracle.oim.gcp.exceptions.ResourceConnectionValidationxception;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractConnectionPool;

import oracle.iam.identity.foundation.AbstractResource;
import oracle.iam.identity.foundation.resource.TaskBundle;
import oracle.iam.identity.foundation.resource.DatabaseBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseConnectionPool
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseConnectionPool</code> implements the base functionality
 ** of an Oracle Identity Manager pooled {@link Connection} for a Database
 ** Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DatabaseConnectionPool extends    AbstractConnectionPool
                                    implements Connection
                                    ,          ResourceConnection {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Connection connection = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseConnectionPool</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DatabaseConnectionPool() {
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
  protected final Connection connection() {
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
      // Bug 26895672 - must get password through getITResourceInstanceParameters
      parameter.put(DatabaseResource.PRINCIPAL_PASSWORD, AbstractResource.fetchStringValue((String)parameter.get(AbstractResource.POOL_CONNECTION_NAME), DatabaseResource.PRINCIPAL_PASSWORD));
      
      @SuppressWarnings("unchecked")
      final DatabaseResource resource = new DatabaseResource(this, parameter);
      final DatabaseFeature  feature  = DatabaseConnection.unmarshal(this, resource.feature());
      try {
        Class.forName(feature.databaseDriverClass());
      }
      catch (ClassNotFoundException e) {
        throw new ResourceConnectionCreateException(TaskBundle.format(TaskError.CLASSNOTFOUND, e));
      }

      final Properties environment = new Properties();
      environment.put(DatabaseConnection.ENVIRONMENT_ACCOUNT,  resource.principalName());
      environment.put(DatabaseConnection.ENVIRONMENT_PASSWORD, resource.principalPassword());
      if (resource.secureSocket()) {
//        str3 = (String)ConnectionParams.get("Connection Properties");
//        environment = getConnectionProp(str3);
      }

      int attempts = 1;
      do {
        // sets the maximum time in seconds that a driver will wait while
        // attempting to connect to a database.
        DriverManager.setLoginTimeout(resource.connectionTimeout());
        try {
          debug(method, DatabaseBundle.format(DatabaseMessage.CONNECTING_TO, resource.databaseURL()));
          this.connection = DriverManager.getConnection(resource.databaseURL(), environment);
          this.connection.setAutoCommit(feature.enforceAutoCommit());
          break;
        }
        catch (SQLException e) {
          attempts++;
          if (attempts > resource.connectionRetryCount()) {
            error(method, DatabaseBundle.string(DatabaseError.CONNECTION_ERROR));
            throw new DatabaseException(e);
          }
          try {
            Thread.sleep(resource.connectionRetryInterval());
          }
          catch (InterruptedException ex) {
            throw new DatabaseException(ex);
          }
        }
      } while (attempts <= resource.connectionRetryCount());
    }
    catch (TaskException e) {
      throw new ResourceConnectionCreateException(DatabaseBundle.format(DatabaseError.UNHANDLED, e));
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
      this.connection.close();
    }
    catch (SQLException e) {
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
    try {
      if ((this.connection != null) && (!this.connection.isClosed()))
        debug(method, "connection is available.");
    }
    catch (SQLException e) {
      fatal(method, e);
      throw new ResourceConnectionValidationxception(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isValid (ResourceConnection)
  @Override
  public boolean isValid() {
    final String method = "isValid";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean valid = false;
    try {
      valid = this.connection.isClosed();
      debug(method, "connection is valid.");
    }
    catch (SQLException e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return valid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwrap (Wrapper)
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
   ** interface, then an {@link SQLException} is thrown.
   **
   ** @param  iface              a {@link Class} defining an interface that the
   **                            result must implement.
   **
   ** @return                    an object that implements the interface. May be
   **                            a proxy for the actual implementing object.
   **
   ** @throws SQLException       if no object found that implements the
   **                            interface.
   */
  @Override
  public final <T> T unwrap(final Class<T> iface)
    throws SQLException {

    return this.connection.unwrap(iface);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isWrapperFor (Wrapper)
  /**
   ** Returns <code>true</code> if this either implements the interface argument
   ** or is directly or indirectly a wrapper for an object that does. Returns
   ** <code>false</code> otherwise. If this implements the interface then return
   ** <code>true</code>, else if this is a wrapper then return the result of
   ** recursively calling <code>isWrapperFor</code> on the wrapped object. If
   ** this does not implement the interface and is not a wrapper, return
   ** <code>false</code>. This method should be implemented as a low-cost
   ** operation compared to <code>unwrap</code> so that callers can use this
   ** method to avoid expensive <code>unwrap</code> calls that may fail. If this
   ** method returns <code>true</code> then calling <code>unwrap</code> with the
   ** same argument should succeed.
   **
   ** @param  iface              a {@link Class} defining an interface that the
   **                            result must implement.
   **
   ** @return                    <code>true</code> if this implements the
   **                            interface or directly or indirectly wraps an
   **                            object that does.
   **
   ** @throws SQLException       if an error occurs while determining whether
   **                            this is a wrapper for an object with the given
   **                            interface.
   */
  @Override
  public final boolean isWrapperFor(final java.lang.Class<?> iface)
    throws SQLException {

    return this.connection.isWrapperFor(iface);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isValid (Connection)
  /**
   ** Returns <code>true</code> if the connection has not been closed and is
   ** still valid. The driver shall submit a query on the connection or use some
   ** other mechanism that positively verifies the connection is still valid
   ** when this method is called.
   ** <p>
   ** The query submitted by the driver to validate the connection shall be
   ** executed in the context of the current transaction.
   **
   ** @param  timeout            the time in seconds to wait for the database
   **                            operation used to validate the connection to
   **                            complete. If the timeout period expires before
   **                            the operation completes, this method returns
   **                            <code>false</code>. A value of <code>0</code>
   **                            indicates a timeout is not applied to the
   **                            database operation.
   **
   ** @return                    <code>true</code> if the connection is valid,
   **                            <code>false</code> otherwise.
   **
   ** @throws SQLException       if the value supplied for <code>timeout</code>
   **                            is less then <code>0</code>.
   */
  @Override
  public final boolean isValid(final int timeout)
    throws SQLException {

    return this.connection.isValid(timeout);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isClosed (Connection)
  /**
   ** Retrieves whether this {@link Connection} object has been closed. A
   ** connection is closed if the method <code>close</code> has been called on
   ** it or if certain fatal errors have occurred. This method is guaranteed to
   ** return <code>true</code> only whenit is called after the method
   ** <code>Connection.close</code> has been called.
   ** <p>
   ** This method generally cannot be called to determine whether a connection
   ** to a database is valid or invalid. A typical clientcan determine that a
   ** connection is invalid by catching anyexceptions that might be thrown when
   ** an operation is attempted.
   **
   ** @return                    <code>true</code> if this {@link Connection}
   **                            object is closed; <code>false</code> if it is
   **                            still open.
   **
   ** @throws SQLException       if a database access error occurs.
   */
  @Override
  public final boolean isClosed()
    throws SQLException {

    return this.connection.isClosed();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAutoCommit (Connection)
  /**
   ** Sets this connection's auto-commit mode to the given state. If a
   ** connection is in auto-commit mode, then all its SQL statements will be
   ** executed and committed as individual transactions. Otherwise, its SQL
   ** statements are grouped into transactions that are terminated by a call to
   ** either the method {@link #commit()} or the method {@link #rollback()}. By
   ** default, new connections are in auto-commit mode.
   ** <p>
   ** The commit occurs when the statement completes. The time when the
   ** statement  completes depends on the type of SQL Statement:
   ** <ul>
   **   <li>For DML statements, such as Insert, Update or Delete, and DDL
   **       statements, the statement is complete as soon as it has finished
   **       executing.
   **   <li>For Select statements, the statement is complete when the associated
   **       result set is closed.
   **   <li>For {@link CallableStatement} objects or for statements that return
   **       multiple results, the statement is complete when all of the
   **       associated result sets have been closed, and all update counts and
   **       output parameters have been retrieved.
   ** </ul>
   ** <p>
   ** <b>NOTE:</b>
   ** <br>
   ** If this method is called during a transaction and the auto-commit mode is
   ** changed, the transaction is committed. If {@link #setAutoCommit(boolean)}
   ** is called and the auto-commit mode is not changed, the call is a no-op.
   **
   ** @param  autoCommit         <code>true</code> to enable auto-commit mode;
   **                            <code>false</code> to disable it.
   **
   ** @throws SQLException       if a database access error occurs,
   **                            setAutoCommit(true) is called while
   **                            participating in a distributed transaction, or
   **                            this method is called on a closed connection
   **
   ** @see    #getAutoCommit
   */
  @Override
  public final void setAutoCommit(final boolean autoCommit)
    throws SQLException {

    this.connection.setAutoCommit(autoCommit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAutoCommit (Connection)
  /**
   ** Retrieves the current auto-commit mode for this {@link Connection} object.
   **
   ** @return                    the current state of this {@link Connection}
   **                            object's auto-commit mode.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection.
   **
   ** @see   #setAutoCommit
   */
  @Override
  public final boolean getAutoCommit()
    throws SQLException {

    return this.connection.getAutoCommit();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setReadOnly (Connection)
  /**
   ** Puts this connection in read-only mode as a hint to the driver to enable
   ** database optimizations.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** This method cannot be called during a transaction.
   **
   ** @param  readOnly           <code>true</code> enables read-only mode;
   **                            <code>false</code> disables it.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or this method
   **                            is called during a transaction.
   */
  @Override
  public final void setReadOnly(final boolean readOnly)
    throws SQLException {

    this.connection.setReadOnly(readOnly);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isReadOnly (Connection)
  /**
   ** Retrieves whether this {@link Connection} object is in read-only mode.
   **
   ** @return                    <code>true</code> if this {@link Connection}
   **                            object is read-only; <code>false</code>
   **                            otherwise.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection.
   */
  @Override
  public final boolean isReadOnly()
    throws SQLException {

    return this.connection.isReadOnly();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNetworkTimeout (Connection)
  /**
   ** Sets the maximum period a {@link Connection} or objects created from the
   ** {@link Connection} will wait for the database to reply to any one request.
   ** If any request remains unanswered, the waiting method will return with a
   ** {@link SQLException}, and the {@link Connection} or objects created from
   ** the {@link Connection} will be marked as closed. Any subsequent use of the
   ** objects, with the exception of the <code>close</code>, {@link #isClosed()}
   ** or {@link #isValid()} methods, will result in  a {@link SQLException}.
   ** <p>
   ** <b>Note</b>: This method is intended to address a rare but serious
   **              condition where network partitions can cause threads issuing
   **              JDBC calls to hang uninterruptedly in socket reads, until the
   **              OS TCP-TIMEOUT (typically 10 minutes). This method is related
   **              to the {@link #abort abort()} method which provides an
   **              administrator thread a means to free any such threads in
   **              cases where the JDBC connection is accessible to the
   **              administrator thread.
   **<p>
   ** The <code>setNetworkTimeout</code> method will cover cases where there is
   ** no administrator thread, or it has no access to the connection. This
   ** method is severe in it's effects, and should be given a high enough value
   ** so it is never triggered before any more normal timeouts, such as
   ** transaction timeouts.
   ** <p>
   ** JDBC driver implementations  may also choose to support the
   ** {@code setNetworkTimeout} method to impose a limit on database response
   ** time, in environments where no network is present.
   ** <p>
   ** Drivers may internally implement some or all of their API calls with
   ** multiple internal driver-database transmissions, and it is left to the
   ** driver implementation to determine whether the limit will be applied
   ** always to the response to the API call, or to any single request made
   ** during the API call.
   ** <p>
   ** This method can be invoked more than once, such as to set a limit for an
   ** area of JDBC code, and to reset to the default on exit from this area.
   ** Invocation of this method has no impact on already outstanding requests.
   ** <p>
   ** The {@code Statement.setQueryTimeout()} timeout value is independent of
   ** the timeout value specified in {@code setNetworkTimeout}. If the query
   ** timeout expires before the network timeout then the statement execution
   ** will be canceled. If the network is still active the result will be that
   ** both the statement and connection are still usable. However if the network
   ** timeout expires before the query timeout or if the statement timeout fails
   ** due to network problems, the connection will be marked as closed, any
   ** resources held by the connection will be released and both the connection
   ** and statement will be unusable.
   ** <p>
   ** When the driver determines that the {@code setNetworkTimeout} timeout
   ** value has expired, the JDBC driver marks the connection closed and
   ** releases any resources held by the connection.
   ** <p>
   ** This method checks to see that there is an <code>SQLPermission</code>
   ** object before allowing the method to proceed. If a
   ** <code>SecurityManager</code> exists and its <code>checkPermission</code>
   ** method denies calling <code>setNetworkTimeout</code>, this method throws a
   ** <code>java.lang.SecurityException</code>.
   **
   ** @param  executor           the <code>Executor</code> implementation which
   **                            will be used by <code>setNetworkTimeout</code>.
   ** @param  milliseconds       the time in milliseconds to wait for the
   **                            database operation to complete. If the JDBC
   **                            driver does not support milliseconds, the JDBC
   **                            driver will round the value up to the nearest
   **                            second. If the timeout period expires before
   **                            the operation completes, a SQLException will be
   **                            thrown.
   **                            A value of <code>0</code> indicates that there
   **                            is not timeout for database operations.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection, the
   **                            {@code executor} is {@code null}, or the value
   **                            specified for <code>seconds</code> is less than
   **                            <code>0</code>.
   ** @throws SecurityException  if a security manager exists and its
   **                            <code>checkPermission</code> method denies
   **                            calling <code>setNetworkTimeout</code>.
   **
   ** @see    Statement#setQueryTimeout
   ** @see    #getNetworkTimeout
   ** @see    #abort
   ** @see    Executor
   **
   ** @since  3.1.0.0
   */
  @Override
  public final void setNetworkTimeout(final Executor executor, final int milliseconds)
    throws SQLException {

    this.connection.setNetworkTimeout(executor, milliseconds);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNetworkTimeout (Connection)
  /**
   ** Retrieves the number of milliseconds the driver will wait for a database
   ** request to complete. If the limit is exceeded, a
   ** {@link SQLException} is thrown.
   **
   ** @return                    the current timeout limit in milliseconds; zero
   **                            means there is no limit
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed
   **                            {@link Connection}.
   **
   ** @see #setNetworkTimeout
   ** @since 1.7
   **/
  @Override
  public final int getNetworkTimeout()
    throws SQLException {

    return this.connection.getNetworkTimeout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSchema (Connection)
  /**
   ** Sets the given schema name to access.
   ** <p>
   ** If the driver does not support schemas, it will silently ignore this
   ** request.
   ** <p>
   ** Calling {@code setSchema} has no effect on previously created or prepared
   ** {@code Statement} objects. It is implementation defined whether a DBMS
   ** prepare operation takes place immediately when the {@code Connection}
   ** method {@code prepareStatement} or {@code prepareCall} is invoked. For
   ** maximum portability, {@code setSchema} should be called before a
   ** {@code Statement} is created or prepared.
   **
   ** @param  schema             the name of a schema in which to work
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection.
   **
   ** @see    #getSchema
   **
   ** @since  3.1.0.0
   */
  @Override
  public final void setSchema(final String schema)
    throws SQLException {

    this.connection.setSchema(schema);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSchema (Connection)
  /**
   ** Retrieves this {@link Connection} object's current schema name.
   **
   ** @return                    the current schema name or <code>null</code> if
   **                            there is none.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection.
   **
   ** @see    #setSchema
   **
   ** @since  3.1.0.0
   */
  @Override
  public final String getSchema()
    throws SQLException {

    return this.connection.getSchema();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTransactionIsolation (Connection)
  /**
   ** Attempts to change the transaction isolation level for this
   ** {@link Connection} object to the one given. The constants defined in the
   ** interface {@link Connection} are the possible transaction isolation
   ** levels.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** If this method is called during a transaction, the result is
   ** implementation-defined.
   **
   ** @param  level              one of the following {@link Connection}
   **                            constants:
   **                            <ul>
   **                              <li><code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
   **                              <li><code>Connection.TRANSACTION_READ_COMMITTED</code>,
   **                              <li><code>Connection.TRANSACTION_REPEATABLE_READ</code>,
   **                              <li><code>Connection.TRANSACTION_SERIALIZABLE</code>, or
   **                            </ul>.
   **                            <b>Note:</b>
   **                            <br>
   **                            That <code>Connection.TRANSACTION_NONE</code>
   **                            cannot be used because it specifies that
   **                            transactions are not supported.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            parameter is not one of the {@link Connection}
   **                            constants.
   **
   ** @see    DatabaseMetaData#supportsTransactionIsolationLevel
   ** @see    #getTransactionIsolation
   */
  @Override
  public final void setTransactionIsolation(final int level)
    throws SQLException {

    this.connection.setTransactionIsolation(level);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTransactionIsolation (Connection)
  /**
   ** Retrieves this {@link Connection} object's current transaction isolation
   ** level.
   **
   ** @return                    the current transaction isolation level, which
   **                            will be one of the following constants:
   **                            <ul>
   **                              <li><code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
   **                              <li><code>Connection.TRANSACTION_READ_COMMITTED</code>,
   **                              <li><code>Connection.TRANSACTION_REPEATABLE_READ</code>,
   **                              <li><code>Connection.TRANSACTION_SERIALIZABLE</code>, or
   **                              <li><code>Connection.TRANSACTION_NONE</code>.
   **                            </ul>.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection.
   **
   ** @see    #setTransactionIsolation
   */
  @Override
  public final int getTransactionIsolation()
    throws SQLException {

    return this.connection.getTransactionIsolation();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSavepoint (Connection)
  /**
   ** Creates an unnamed savepoint in the current transaction and returns the
   ** new {@link Savepoint} object that represents it.
   ** <p>
   ** If setSavepoint is invoked outside of an active transaction, a transaction
   ** will be started at this newly created savepoint.
   **
   ** @return                    the new {@link Savepoint} object.
   **
   ** @throws                    SQLException if a database access error occurs,
   **                            this method is called while participating in a
   **                            distributed transaction, this method is called
   **                            on a closed connection or this
   **                            {@link Connection} object is currently in
   **                            auto-commit mode.
   */
  @Override
  public final Savepoint setSavepoint()
    throws SQLException {

    return this.connection.setSavepoint();
  }

   //////////////////////////////////////////////////////////////////////////////
  // Method:   setSavepoint (Connection)
  /**
   ** Creates a savepoint with the given name in the current transaction and
   ** returns the new {@link Savepoint} object that represents it.
   ** <p>
   ** If setSavepoint is invoked outside of an active transaction, a transaction
   ** will be started at this newly created savepoint.
   **
   ** @param  name               a <code>String</code> containing the name of
   **                            the savepoint.
   **
   ** @return                    the new {@link Savepoint} object.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called while participating in a distributed
   **                            transaction, this method is called on a closed
   **                            connection or this {@link Connection} object
   **                            is currently in auto-commit mode.
   */
  @Override
  public final Savepoint setSavepoint(final String name)
    throws SQLException {

    return this.connection.setSavepoint(name);
  }

   //////////////////////////////////////////////////////////////////////////////
  // Method:   releaseSavepoint (Connection)
  /**
   ** Removes the specified {@link Savepoint} and subsequent {@link Savepoint}
   ** objects from the current transaction. Any reference to the savepoint after
   ** it have been removed will cause an {@link SQLException} to be thrown.
   **
   ** @param  savepoint          the {@link Savepoint} object to be removed.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            {@link Savepoint} object is not a valid
   **                            savepoint in the current transaction.
   */
  @Override
  public final void releaseSavepoint(final Savepoint savepoint)
    throws SQLException {

    this.connection.releaseSavepoint(savepoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHoldability (Connection)
  /**
   ** Changes the default holdability of <code>ResultSet</code> objects created
   ** using this {@link Connection} object to the given holdability. The default
   ** holdability of <code>ResultSet</code> objects can be determined by
   ** invoking  {@link DatabaseMetaData#getResultSetHoldability}.
   **
   ** @param  holdability        a <code>ResultSet</code> holdability constant;
   **                            one of
   **                            <ul>
   **                              <li><code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
   **                              <li><code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
   **                            </ul>.
   **
   ** @throws SQLException       if a database access occurs, this method is
   **                            called on a closed connection, or the given
   **                            parameter is not a <code>ResultSet</code>
   **                            constant indicating holdability.
   **
   ** @see    #getHoldability
   ** @see    DatabaseMetaData#getResultSetHoldability
   */
  @Override
  public final void setHoldability(final int holdability)
    throws SQLException {

    this.connection.setHoldability(holdability);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHoldability (Connection)
  /**
   ** Retrieves the current holdability of <code>ResultSet</code> objects
   ** created using this {@link Connection} object.
   **
   ** @return                    the holdability, one of
   **                            <ul>
   **                              <li><code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
   **                              <li><code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
   **                            </ul>.
   ** @throws SQLException       if a database access occurs, this method is
   **                            called on a closed connection.
   **
   ** @see    #setHoldability
   ** @see    DatabaseMetaData#getResultSetHoldability
   */
  @Override
  public final int getHoldability()
    throws SQLException {

    return this.connection.getHoldability();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCatalog (Connection)
  /**
   ** Sets the given catalog name in order to select a subspace of this
   ** {@link Connection} object's database in which to work.
   ** <p>
   ** If the driver does not support catalogs, it will silently ignore this
   ** request.
   **
   ** @param  catalog            the name of a catalog (subspace in this
   **                            {@link Connection} object's database) in which
   **                            to work.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection.
   **
   ** @see    #getCatalog
   */
  @Override
  public final void setCatalog(final String catalog)
    throws SQLException {

    this.connection.setCatalog(catalog);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCatalog (Connection)
  /**
   ** Retrieves this {@link Connection} object's current catalog name.
   **
   ** @return                   the current catalog name or <code>null</code> if
   **                           there is none.
   **
   ** @throws SQLException      if a database access error occurs or this method
   **                           is called on a closed connection.
   **
   ** @see    #setCatalog
   */
  @Override
  public final String getCatalog()
    throws SQLException {

    return this.connection.getCatalog();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClientInfo (Connection)
  /**
   ** Sets the value of the client info property specified by name to the value
   ** specified by value.
   ** <p>
   ** Applications may use the
   ** <code>DatabaseMetaData.getClientInfoProperties</code> method to determine
   ** the client info properties supported by the driver and the maximum length
   ** that may be specified for each property.
   ** <p>
   ** The driver stores the value specified in a suitable location in the
   ** database. For example in a special register, session parameter, or system
   ** table column. For efficiency the driver may defer setting the value in the
   ** database until the next time a statement is executed or prepared. Other
   ** than storing the client information in the appropriate place in the
   ** database, these methods shall not alter the behavior of the connection in
   ** anyway. The values supplied to these methods are used for accounting,
   ** diagnostics and debugging purposes only.
   ** <p>
   ** The driver shall generate a warning if the client info name specified is
   ** not recognized by the driver.
   ** <p>
   ** If the value specified to this method is greater than the maximum length
   ** for the property the driver may either truncate the value and generate a
   ** warning or generate a <code>SQLClientInfoException</code>. If the driver
   ** generates a <code>SQLClientInfoException</code>, the value specified was
   ** not set on the connection.
   ** <p>
   ** The following are standard client info properties.  Drivers are not
   ** required to support these properties however if the driver supports a
   ** client info property that can be described by one of the standard
   ** properties, the standard property name should be used.
   ** <ul>
   **   <li>ApplicationName - the name of the application currently utilizing
   **                         the connection
   **   <li>ClientUser      - the name of the user that the application using
   **                         the connection is performing work for. This may
   **                         not be the same as the user name that was used in
   **                         establishing the connection.
   **   <li>ClientHostname  - the hostname of the computer the application using
   **                         the connection is running on.
   ** </ul>
   **
   ** @param  name               the name of the client info property to set
   ** @param  value              the value to set the client info property to.
   **                            If the value is <code>null</code>, the current
   **                            value of the specified property is cleared.
   **
   ** @throws SQLClientInfoException if the database server returns an error
   **                                while setting the client info value on the
   **                                database server or this method is called on
   **                                a closed connection.
   */
  @Override
  public final void setClientInfo(final String name, final String value)
    throws SQLClientInfoException {

    this.connection.setClientInfo(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClientInfo (Connection)
  /**
   ** Returns the value of the client info property specified by name. This
   ** method may return null if the specified client info property has not been
   ** set and does not have a default value. This method will also return
   ** <code>null</code> if the specified client info property name is not
   ** supported by the driver.
   ** <p>
   ** Applications may use the
   ** <code>DatabaseMetaData.getClientInfoProperties</code> method to determine
   ** the client info properties supported by the driver.
   **
   ** @param  name               the name of the client info property to
   **                            retrieve.
   **
   ** @return                    the value of the client info property
   **                            specified.
   **
   ** @throws SQLException       if the database server returns an error when
   **                            fetching the client info value from the
   **                            database or this method is called on a closed
   **                            connection.
   */
  @Override
  public final String getClientInfo(final String name)
    throws SQLException {

    return this.connection.getClientInfo(name);
  }

    //////////////////////////////////////////////////////////////////////////////
  // Method:   setClientInfo (Connection)
  /**
   ** Sets the value of the connection's client info properties. The
   ** {@link Properties} object contains the names and values of the client info
   ** properties to be set. The set of client info properties contained in the
   ** properties list replaces the current set of client info properties on the
   ** connection. If a property that is currently set on the connection is not
   ** present in the properties list, that property is cleared. Specifying an
   ** empty properties list will clear all of the properties on the connection.
   ** See {@link #setClientInfo (String, String)} for more information.
   ** <p>
   ** If an error occurs in setting any of the client info properties, a
   ** {@link SQLClientInfoException} is thrown. The
   ** {@link SQLClientInfoException} contains information indicating which
   ** client info properties were not set. The state of the client information
   ** is unknown because some databases do not allow multiple client info
   ** properties to be set atomically. For those databases, one or more
   ** properties may have been set before the error occurred.
   **
   ** @param  properties         the list of client info properties to set.
   **
   ** @throws SQLClientInfoException if the database server returns an error
   **                                while setting the clientInfo values on the
   **                                database server or this method is called on
   **                                a closed connection
   */
  @Override
  public final void setClientInfo(final Properties properties)
    throws SQLClientInfoException {

    this.connection.setClientInfo(properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClientInfo (Connection)
  /**
   ** Returns a list containing the name and current value of each client info
   ** property supported by the driver. The value of a client info property may
   ** be null if the property has not been set and does not have a default
   ** value.
   **
   ** @return                    a {@link Properties} object that contains the
   **                            name and current value of each of the client
   **                            info properties supported by the driver.
   **
   ** @throws SQLException       if the database server returns an error when
   **                            fetching the client info values from the
   **                            database or this method is called on a closed
   **                            connection
   */
  @Override
  public final Properties getClientInfo()
    throws SQLException {

    return this.connection.getClientInfo();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMetaData (Connection)
  /**
   ** Retrieves a {@link DatabaseMetaData} object that contains metadata about
   ** the database to which this {@link Connection} object represents a
   ** connection. The metadata includes information about the database's tables,
   ** its supported SQL grammar, its stored procedures, the capabilities of this
   ** connection, and so on.
   **
   ** @return                    a {@link DatabaseMetaData} object for this
   **                            {@link Connection} object.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection
   */
  @Override
  public final DatabaseMetaData getMetaData()
    throws SQLException {

    return this.connection.getMetaData();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTypeMap (Connection)
  /**
   ** Installs the given <code>TypeMap</code> object as the type map for this
   ** {@link Connection} object. The type map will be used for the custom
   ** mapping of SQL structured types and distinct types.
   **
   ** @param  map                the {@link Map} object to install as the
   **                            replacement for this {@link Connection}
   **                            object's default type map.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            parameter is not a {@link Map} object.
   **
   ** @see    #getTypeMap
   */
  @Override
  public final void setTypeMap(final Map<String, Class<?>> map)
    throws SQLException {

    this.connection.setTypeMap(map);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTypeMap (Connection)
  /**
   ** Retrieves the <code>Map</code> object associated with this
   ** {@link Connection} object. Unless the application has added an entry, the
   ** type map returned will be empty.
   **
   ** @return                    the {@link Map} object associated with this
   **                            {@link Connection} object.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection.
   **
   ** @see    #setTypeMap
   */
  @Override
  public final Map<String, Class<?>> getTypeMap()
    throws SQLException {

    return this.connection.getTypeMap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearWarnings (Connection)
  /**
   ** Clears all warnings reported for this {@link Connection} object. After a
   ** call to this method, the method <code>getWarnings</code> returns
   ** <code>null</code> until a new warning is reported for this
   ** {@link Connection} object.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection
   */
  @Override
  public final void clearWarnings()
    throws SQLException {

    this.connection.clearWarnings();
  }

   //////////////////////////////////////////////////////////////////////////////
  // Method:   getWarnings (Connection)
  /**
   ** Retrieves the first warning reported by calls on this {@link Connection}
   ** object. If there is more than one warning, subsequent warnings will be
   ** chained to the first one and can be retrieved by calling the method
   ** {@link SQLWarning#getNextWarning()} on the warning that was retrieved
   ** previously.
   ** <p>
   ** This method may not be called on a closed connection; doing so will cause
   ** an {@link SQLException} to be thrown.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** Subsequent warnings will be chained to this {@link SQLWarning}.
   **
   ** @return                    the first {@link SQLWarning} object or
   **                            <code>null</code> if there are none.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection.
   **
   ** @see    SQLWarning
   */
  @Override
  public final SQLWarning getWarnings()
    throws SQLException {

    return this.connection.getWarnings();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit (Connection)
  /**
   ** Makes all changes made since the previous commit/rollback permanent and
   ** releases any database locks currently held by this {@link Connection}
   ** object. This method should be used only when auto-commit mode has been
   ** disabled.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called while participating in a distributed
   **                            transaction, if this method is called on a
   **                            closed connection or this {@link Connection}
   **                            object is in auto-commit mode
   ** @see #setAutoCommit
   */
  @Override
  public final void commit()
    throws SQLException {

    this.connection.commit();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rollback (Connection)
  /**
   ** Undoes all changes made in the current transaction and releases any
   ** database locks currently held by this {@link Connection} object.
   ** <p>
   ** This method should be used only when auto-commit mode has been disabled.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called while participating in a distributed
   **                            transaction, if this method is called on a
   **                            closed connection or this {@link Connection}
   **                            object is in auto-commit mode
   ** @see #setAutoCommit
   */
  @Override
  public final void rollback()
    throws SQLException {

    this.connection.rollback();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rollback (Connection)
  /**
   ** Undoes all changes made after the given {@link Savepoint} object was set.
   ** <p>
   ** This method should be used only when auto-commit mode has been disabled.
   **
   ** @param  savepoint          the {@link Savepoint} object to roll back to
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called while participating in a distributed
   **                            transaction, if this method is called on a
   **                            closed connection or this {@link Connection}
   **                            object is in auto-commit mode
   ** @see Savepoint
   ** @see #rollback()
   */
  @Override
  public final void rollback(final Savepoint savepoint)
    throws SQLException {

    this.connection.rollback(savepoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createArrayOf (Connection)
  /**
   ** Factory method for creating {@link Array} objects.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** When <code>createArrayOf</code> is used to create an array object that
   ** maps to a primitive data type, then it is implementation-defined whether
   ** the <code>Array</code> object is an array of that primitive data type or
   ** an array of {@link Object}.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** The JDBC driver is responsible for mapping the elements {@link Object}
   ** array to the default JDBC SQL type defined in java.sql.Types for the given
   ** class of {@link Object}. The default mapping is specified in Appendix B of
   ** the JDBC specification. If the resulting JDBC type is not the appropriate
   ** type for the given typeName then it is implementation defined whether an
   ** {@link SQLException} is thrown or the driver supports the resulting
   ** conversion.
   **
   ** @param  typeName           the SQL name of the type the elements of the
   **                            array map to. The typeName is a
   **                            database-specific name which may be the name of
   **                            a built-in type, a user-defined type or a
   **                            standard SQL type supported by this database.
   **                            This is the value returned by
   **                            {@link Array#getBaseTypeName()}.
   ** @param  elements           the elements that populate the returned object.
   **
   ** @return                    an Array object whose elements map to the
   **                            specified SQL type.
   **
   ** @throws SQLException       if a database error occurs, the JDBC type is
   **                            not appropriate for the typeName and the
   **                            conversion is not supported, the typeName is
   **                            null or this method is called on a closed
   **                            connection.
   */
  @Override
  public final Array createArrayOf(final String typeName, final Object[] elements)
    throws SQLException {

    return this.connection.createArrayOf(typeName, elements);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStruct (Connection)
  /**
   ** Factory method for creating Struct objects.
   **
   ** @param  typeName           the SQL type name of the SQL structured type
   **                            that this {@link Struct} object maps to. The
   **                            typeName is the name of  a user-defined type
   **                            that has been defined for this database. It is
   **                            the value returned by
   **                            {@link Struct#getSQLTypeName()}.
   ** @param  attributes         the attributes that populate the returned
   **                            object.
   **
   ** @return                    a {@link Struct} object that maps to the given
   **                            SQL type and is populated with the given
   **                            attributes.
   **
   ** @throws SQLException       if a database error occurs, the JDBC type is
   **                            not appropriate for the typeName and the
   **                            conversion is not supported, the typeName is
   **                            null or this method is called on a closed
   **                            connection.
   */
  @Override
  public final Struct createStruct(final String typeName, final Object[] attributes)
    throws SQLException {

    return this.connection.createStruct(typeName, attributes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBlob (Connection)
  /**
   ** Constructs an object that implements the {@link Blob} interface. The
   ** object returned initially contains no data. The
   ** <code>setBinaryStream</code> and <code>setBytes</code> methods of the
   ** {@link Blob} interface may be used to add data to the {@link Blob}.
   **
   ** @return                    an object that implements the {@link Blob}
   **                            interface.
   **
   ** @throws SQLException       if an object that implements the {@link Blob}
   **                            interface can not be constructed, this method
   **                            is called on a closed connection or a database
   **                            access error occurs.
   */
  @Override
  public final Blob createBlob()
    throws SQLException {

    return this.connection.createBlob();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createClob (Connection)
  /**
   ** Constructs an object that implements the {@link Clob} interface. The
   ** object returned initially contains no data. The
   ** <code>setAsciiStream</code>, <code>setCharacterStream</code> and
   ** <code>setString</code> methods of the {@link Clob} interface may be used
   ** to add data to the {@link Clob} .
   **
   ** @return                    an object that implements the {@link Clob}
   **                            interface.
   **
   ** @throws SQLException       if an object that implements the {@link Clob}
   **                            interface can not be constructed, this method
   **                            is called on a closed connection or a database
   **                            access error occurs.
   */
  @Override
  public final Clob createClob()
    throws SQLException {

    return this.connection.createClob();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNClob (Connection)
  /**
   ** Constructs an object that implements the {@link NClob} interface. The
   ** object returned initially contains no data. The
   ** <code>setAsciiStream</code>, <code>setCharacterStream</code> and
   ** <code>setString</code> methods of the {@link NClob} interface may be used
   ** to add data to the {@link NClob}.
   **
   ** @return                    an object that implements the {@link NClob}
   **                            interface.
   **
   ** @throws SQLException       if an object that implements the {@link NClob}
   **                            interface can not be constructed, this method
   **                            is called on a closed connection or a database
   **                            access error occurs.
   */
  @Override
  public final NClob createNClob()
    throws SQLException {

    return this.connection.createNClob();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSQLXML (Connection)
  /**
   ** Constructs an object that implements the {@link SQLXML} interface. The
   ** object returned initially contains no data. The
   ** <code>createXmlStreamWriter</code> object and <code>setString</code>
   ** method of the {@link SQLXML} interface may be used to add data to the
   ** {@link SQLXML} object.
   **
   ** @return                    an object that implements the {@link SQLXML}
   **
   ** @throws SQLException       if an object that implements the {@link SQLXML}
   **                            interface can not be constructed, this method
   **                            is called on a closed connection or a database
   **                            access error occurs.
   */
  @Override
  public final SQLXML createSQLXML()
    throws SQLException {

    return this.connection.createSQLXML();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStatement (Connection)
  /**
   ** Releases this {@link Connection} object's database and JDBC resources
   ** immediately instead of waiting for them to be automatically released.
   ** <p>
   ** Calling the method <code>close</code> on a {@link Connection} object that
   ** is already closed is a no-op.
   ** <p>
   ** It is <b>strongly recommended</b> that an application explicitly commits
   ** or rolls back an active transaction prior to calling the
   ** <code>close</code> method. If the <code>close</code> method is called and
   ** there is an active transaction, the results are implementation-defined.
   **
   ** @throws SQLException       if a database access error occurs
   */
  @Override
  public final void close()
    throws SQLException {

    this.connection.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort (Connection)
  /**
   ** Terminates an open connection. Calling <code>abort</code> results in:
   ** <ul>
   **   <li>The connection marked as closed.
   **   <li>Closes any physical connection to the database.
   **   <li>Releases resources used by the connection.
   **   <li>Insures that any thread that is currently accessing the connection
   **       will either progress to completion or throw a
   **       {@link SQLException}.
   ** </ul>
   ** Calling <code>abort</code> marks the connection closed and releases any
   ** resources. Calling <code>abort</code> on a closed connection is a no-op.
   ** <p>
   ** It is possible that the aborting and releasing of the resources that are
   ** held by the connection can take an extended period of time. When the
   ** <code>abort</code> method returns, the connection will have been marked as
   ** closed and the <code>Executor</code> that was passed as a parameter to
   ** abort may still be executing tasks to release resources.
   ** <p>
   ** This method checks to see that there is an <code>SQLPermission</code>
   ** object before allowing the method to proceed. If a
   ** <code>SecurityManager</code> exists and its <code>checkPermission</code>
   ** method denies calling <code>abort</code>, this method throws a
   ** <code>java.lang.SecurityException</code>.
   **
   ** @param  executor           the <code>Executor</code> implementation which
   **                            will be used by <code>abort</code>.
   **
   ** @throws SQLException       if a database access error occurs or the
   **                            {@code executor} is {@code null}
   ** @throws SecurityException  if a security manager exists and its
   **                            <code>checkPermission</code> method denies
   **                            calling <code>abort</code>
   **
   ** @see    Executor
   **
   ** @since  3.1.0.0
   */
  public final void abort(final Executor executor)
    throws SQLException {

    this.connection.abort(executor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nativeSQL (Connection)
  /**
   ** Converts the given SQL statement into the system's native SQL grammar. A
   ** driver may convert the JDBC SQL grammar into its system's native SQL
   ** grammar prior to sending it. This method returns the native form of the
   ** statement that the driver would have sent.
   **
   ** @param  sql                an SQL statement that may contain one or more
   **                            '?' parameter placeholders.
   **
   ** @return                    the native form of this statement
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection
   */
  @Override
  public final String nativeSQL(final String sql)
    throws SQLException {

    return this.connection.nativeSQL(sql);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStatement (Connection)
  /**
   ** Creates a {@link Statement} object for sending SQL statements to the
   ** database. SQL statements without parameters are normally executed using
   ** {@link Statement} objects. If the same SQL statement is executed many
   ** times, it may be more efficient to use a  {@link PreparedStatement}
   ** object.
   ** <p>
   ** Result sets created using the returned {@link Statement} object will by
   ** default be type <code>TYPE_FORWARD_ONLY</code> and have a concurrency
   ** level of <code>CONCUR_READ_ONLY</code>. The holdability of the created
   ** result sets can be determined by  calling {@link #getHoldability}.
   **
   ** @return                    a new default {@link Statement} object
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection
     */
  @Override
  public final Statement createStatement ()
    throws SQLException {

    return this.connection.createStatement();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStatement (Connection)
  /**
   ** Creates a {@link Statement} object that will generate
   ** <code>ResultSet</code> objects with the given type and concurrency.
   ** <p>
   ** This method is the same as the {@link #createStatement()} method above,
   ** but it allows the default result set type and concurrency to be
   ** overridden. The holdability of the created result sets can be determined
   ** by calling {@link #getHoldability}.
   **
   ** @param  type               a result set type; one of
   **                            <ul>
   **                              <li><code>ResultSet.TYPE_FORWARD_ONLY</code>,
   **                              <li><code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
   **                              <li><code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
   **                            </ul>
   ** @param concurrency         a concurrency type; one of
   **                            <ul>
   **                              <li><code>ResultSet.CONCUR_READ_ONLY</code> or
   **                              <li><code>ResultSet.CONCUR_UPDATABLE</code>
   **                            </ul>
   **
   ** @return                    a new default {@link Statement} object
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection
   */
  @Override
  public final Statement createStatement(final int type, final int concurrency)
    throws SQLException {

    return this.connection.createStatement(type, concurrency);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStatement (Connection)
  /**
   ** Creates a {@link Statement} object that will generate
   ** <code>ResultSet</code> objects with the given type, concurrency, and
   ** holdability.
   ** <p>
   ** This method is the same as the {@link #createStatement()} method
   ** above, but it allows the default result set type, concurrency, and
   ** holdability to be overridden.
   **
   ** @param  type               a result set type; one of
   **                            <ul>
   **                              <li><code>ResultSet.TYPE_FORWARD_ONLY</code>,
   **                              <li><code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
   **                              <li><code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
   **                            </ul>
   ** @param  concurrency        a concurrency type; one of
   **                            <ul>
   **                              <li><code>ResultSet.CONCUR_READ_ONLY</code> or
   **                              <li><code>ResultSet.CONCUR_UPDATABLE</code>
   **                            </ul>
   ** @param  holdability        one of the following <code>ResultSet</code>
   **                            constants:
   **                            <ul>
   **                              <li><code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
   **                              <li><code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
   **                            </ul>
   **
   ** @return                    a new default {@link Statement} object
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection
   */
  @Override
  public final Statement createStatement(final int type, final int concurrency, final int holdability)
    throws SQLException {

    return this.connection.createStatement(type, concurrency, holdability);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareStatement (Connection)
  /**
   ** Creates a {@link PreparedStatement} object for sending parameterized SQL
   ** statements to the database.
   ** <p>
   ** A SQL statement with or without IN parameters can be pre-compiled and
   ** stored in a {@link PreparedStatement} object. This object can then be used
   ** to efficiently execute this statement multiple times.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** This method is optimized for handling parametric SQL statements that
   ** benefit from precompilation. If the driver supports precompilation, the
   ** method <code>prepareStatement</code> will send the statement to the
   ** database for precompilation. Some drivers may not support precompilation.
   ** In this case, the statement may not be sent to the database until the
   ** {@link PreparedStatement} object is executed. This has no direct effect on
   ** users; however, it does affect which methods throw certain SQLExceptions.
   ** <p>
   ** Result sets created using the returned {@link PreparedStatement} object
   ** will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
   ** the created result sets can be determined by calling
   ** {@link #getHoldability}.
   **
   ** @param  sql                an SQL statement that may contain one or more
   **                            '?' IN parameter placeholders.
   **
   ** @return                    a new default {@link PreparedStatement} object
   **                            containing the pre-compiled SQL statement
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection
   */
  @Override
  public final PreparedStatement prepareStatement(final String sql)
    throws SQLException {

    return this.connection.prepareStatement(sql);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareStatement (Connection)
  /**
   ** Creates a {@link PreparedStatement} object that will generate
   ** <code>ResultSet</code> objects with the given type and concurrency. This
   ** method is the same as the <code>prepareStatement</code> method above, but
   ** it allows the default result set type and concurrency to be overridden.
   ** The holdability of the created result sets can be determined by calling
   ** {@link #getHoldability}.
   **
   ** @param  sql                a <code>String</code> object that is the SQL
   **                            statement to be sent to the database; may
   **                            contain one or more '?' IN parameters
   ** @param  type               a result set type; one of
   **                            <ul>
   **                              <li><code>ResultSet.TYPE_FORWARD_ONLY</code>,
   **                              <li><code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
   **                              <li><code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
   **                            </ul>
   ** @param  concurrency        a concurrency type; one of
   **                            <ul>
   **                              <li><code>ResultSet.CONCUR_READ_ONLY</code> or
   **                              <li><code>ResultSet.CONCUR_UPDATABLE</code>
   **                            </ul>
   **
   ** @return                    a new {@link PreparedStatement} object
   **                            containing the pre-compiled SQL statement that
   **                            will produce <code>ResultSet</code> objects
   **                            with the given type and concurrency.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            parameters are not <code>ResultSet</code>
   **                            constants indicating type and concurrency.
   */
  @Override
  public final PreparedStatement prepareStatement(final String sql, final int type, final int concurrency)
    throws SQLException {

    return this.connection.prepareStatement(sql, type, concurrency);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareStatement (Connection)
  /**
   ** Creates a {@link PreparedStatement} object that will generate
   ** <code>ResultSet</code> with the given type, concurrency, and holdability.
   ** <p>
   ** This method is the same as the <code>prepareStatement</code> method above,
   ** but it allows the default result set type, concurrency, and holdability to
   ** be overridden.
   ** @param  sql                a <code>String</code> object that is the SQL
   **                            statement to be sent to the database; may
   **                            contain one or more '?' IN parameters
   ** @param  type               a result set type; one of
   **                            <ul>
   **                              <li><code>ResultSet.TYPE_FORWARD_ONLY</code>,
   **                              <li><code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
   **                              <li><code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
   **                            </ul>
   ** @param  concurrency        a concurrency type; one of
   **                            <ul>
   **                              <li><code>ResultSet.CONCUR_READ_ONLY</code> or
   **                              <li><code>ResultSet.CONCUR_UPDATABLE</code>
   **                            </ul>
   ** @param  holdability        one of the following <code>ResultSet</code>
   **                            constants:
   **                            <ul>
   **                              <li><code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
   **                              <li><code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
   **                            </ul>
   **
   ** @return                    a new {@link PreparedStatement} object
   **                            containing the pre-compiled SQL statement that
   **                            will produce <code>ResultSet</code> objects
   **                            with the given type and concurrency.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            parameters are not <code>ResultSet</code>
   **                            constants indicating type and concurrency.
   */
  @Override
  public final PreparedStatement prepareStatement(final String sql, final int type, final int concurrency, final int holdability)
    throws SQLException {

    return this.connection.prepareStatement(sql, type, concurrency, holdability);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareStatement (Connection)
  /**
   ** Creates a default {@link PreparedStatement} object that has the capability
   ** to retrieve auto-generated keys. The given constant tells the driver
   ** whether it should make auto-generated keys available for retrieval. This
   ** parameter is ignored if the SQL statement is not an <code>INSERT</code>
   ** statement, or an SQL statement able to return auto-generated keys (the
   ** list of such statements is vendor-specific).
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** This method is optimized for handling parametric SQL statements that
   ** benefit from precompilation. If the driver supports precompilation, the
   ** method <code>prepareStatement</code> will send the statement to the
   ** database for precompilation. Some drivers may not support precompilation.
   ** In this case, the statement may not be sent to the database until the
   ** {@link PreparedStatement} object is executed. This has no direct effect on
   ** users; however, it does affect which methods throw certain SQLExceptions.
   ** <p>
   ** Result sets created using the returned {@link PreparedStatement} object
   ** will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
   ** the created result sets can be determined by calling
   ** {@link #getHoldability}.
   **
   ** @param  sql                an SQL statement that may contain one or more
   **                            '?' IN parameter placeholders.
   ** @param autoGeneratedKeys   a flag indicating whether auto-generated keys
   **                            should be returned; one of
   **                            {@link Statement#RETURN_GENERATED_KEYS} or
   **                            {@link Statement#NO_GENERATED_KEYS}.
   **
   ** @return                    a new {@link PreparedStatement} object,
   **                            containing the pre-compiled SQL statement, that
   **                            will have the capability of returning
   **                            auto-generated keys.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            parameter is not a {@link Statement} constant
   **                            indicating whether auto-generated keys should
   **                            be returned.
   */
  @Override
  public final PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys)
    throws SQLException {

    return this.connection.prepareStatement(sql, autoGeneratedKeys);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareStatement (Connection)
  /**
   ** Creates a default {@link PreparedStatement} object capable of returning
   ** the auto-generated keys designated by the given array. This array contains
   ** the indexes of the columns in the target table that contain the
   ** auto-generated keys that should be made available. The driver will ignore
   ** the array if the SQL statement is not an <code>INSERT</code> statement, or
   ** an SQL statement able to return auto-generated keys (the list of such
   ** statements is vendor-specific).
   ** <p>
   ** An SQL statement with or without IN parameters can be pre-compiled and
   ** stored in a {@link PreparedStatement} object. This object can then be used
   ** to efficiently execute this statement multiple times.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** This method is optimized for handling parametric SQL statements that
   ** benefit from precompilation. If the driver supports precompilation, the
   ** method <code>prepareStatement</code> will send the statement to the
   ** database for precompilation. Some drivers may not support precompilation.
   ** In this case, the statement may not be sent to the database until the
   ** {@link PreparedStatement} object is executed. This has no direct effect on
   ** users; however, it does affect which methods throw certain SQLExceptions.
   ** <p>
   ** Result sets created using the returned {@link PreparedStatement} object
   ** will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
   ** the created result sets can be determined by calling
   ** {@link #getHoldability}.
   *
   ** @param  sql                an SQL statement that may contain one or more
   **                            '?' IN parameter placeholders.
   ** @param columnIndexes       an array of column indexes indicating the
   **                            columns that should be returned from the
   **                            inserted row or rows
   **
   ** @return                    a new {@link PreparedStatement} object,
   **                            containing the pre-compiled SQL statement, that
   **                            is capable of returning the auto-generated keys
   **                            designated by the given array of column
   **                            indexes.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            parameter is not a {@link Statement} constant
   **                            indicating whether auto-generated keys should
   **                            be returned
   */
  @Override
  public final PreparedStatement prepareStatement(final String sql, final int[] columnIndexes)
    throws SQLException {

    return this.connection.prepareStatement(sql, columnIndexes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareStatement (Connection)
  /**
   ** Creates a default {@link PreparedStatement} object capable of returning
   ** the auto-generated keys designated by the given array. This array contains
   ** the indexes of the columns in the target table that contain the
   ** auto-generated keys that should be made available. The driver will ignore
   ** the array if the SQL statement is not an <code>INSERT</code> statement, or
   ** an SQL statement able to return auto-generated keys (the list of such
   ** statements is vendor-specific).
   ** <p>
   ** An SQL statement with or without IN parameters can be pre-compiled and
   ** stored in a {@link PreparedStatement} object. This object can then be used
   ** to efficiently execute this statement multiple times.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** This method is optimized for handling parametric SQL statements that
   ** benefit from precompilation. If the driver supports precompilation, the
   ** method <code>prepareStatement</code> will send the statement to the
   ** database for precompilation. Some drivers may not support precompilation.
   ** In this case, the statement may not be sent to the database until the
   ** {@link PreparedStatement} object is executed. This has no direct effect on
   ** users; however, it does affect which methods throw certain SQLExceptions.
   ** <p>
   ** Result sets created using the returned {@link PreparedStatement} object
   ** will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of
   ** the created result sets can be determined by calling
   ** {@link #getHoldability}.
   *
   ** @param  sql                an SQL statement that may contain one or more
   **                            '?' IN parameter placeholders.
   ** @param  columnNames        an array of column names indicating the columns
   **                            that should be returned from the inserted row
   **                            or rows
   **
   ** @return                    a new {@link PreparedStatement} object,
   **                            containing the pre-compiled SQL statement, that
   **                            is capable of returning the auto-generated keys
   **                            designated by the given array of column
   **                            names.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            parameter is not a {@link Statement} constant
   **                            indicating whether auto-generated keys should
   **                            be returned
   */
  @Override
  public final PreparedStatement prepareStatement(final String sql, final String[] columnNames)
    throws SQLException {

    return this.connection.prepareStatement(sql, columnNames);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareCall (Connection)
  /**
   ** Creates a {@link CallableStatement} object for calling database stored
   ** procedures. The {@link CallableStatement} object provides methods for
   ** setting up its IN and OUT parameters, and methods for executing the call
   ** to a stored procedure.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** This method is optimized for handling stored procedure call statements.
   ** Some drivers may send the call statement to the database when the method
   ** <code>prepareCall</code> is done; others may wait until the
   ** {@link CallableStatement} object is executed. This has no direct effect on
   ** users; however, it does affect which method throws certain SQLExceptions.
   ** <p>
   ** Result sets created using the returned {@link CallableStatement} object
   ** will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of the
   ** created result sets can be determined by calling {@link #getHoldability}.
   **
   ** @param  sql                an SQL statement that may contain one or more
   **                            '?' IN parameter placeholders. Typically this
   **                            statement is specified using JDBC call escape
   **                            syntax.
   **
   ** @return                    a new default {@link CallableStatement} object
   **                            containing the pre-compiled SQL statement.
   **
   ** @throws SQLException       if a database access error occurs or this
   **                            method is called on a closed connection
   */
  @Override
  public final CallableStatement prepareCall(final String sql)
    throws SQLException {

    return this.connection.prepareCall(sql);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareCall (Connection)
  /**
   ** Creates a {@link CallableStatement} object that will generate
   ** <code>ResultSet</code> objects with the given type and concurrency. This
   ** method is the same as the <code>prepareCall</code> method above, but it
   ** allows the default result set type and concurrency to be overridden. The
   ** holdability of the created result sets can be determined by calling
   ** {@link #getHoldability}.
   **
   ** @param  sql                an SQL statement that may contain one or more
   **                            '?' IN parameter placeholders. Typically this
   **                            statement is specified using JDBC call escape
   **                            syntax.
   ** @param  type               a result set type; one of
   **                            <ul>
   **                              <li><code>ResultSet.TYPE_FORWARD_ONLY</code>,
   **                              <li><code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
   **                              <li><code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
   **                            </ul>
   ** @param  concurrency        a concurrency type; one of
   **                            <ul>
   **                              <li><code>ResultSet.CONCUR_READ_ONLY</code> or
   **                              <li><code>ResultSet.CONCUR_UPDATABLE</code>
   **                            </ul>
   **
   ** @return                    a new {@link CallableStatement} object
   **                            containing the pre-compiled SQL statement that
   **                            will produce <code>ResultSet</code> objects
   **                            with the given type and concurrency.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            parameters are not <code>ResultSet</code>
   **                            constants indicating type and concurrency
   */
  @Override
  public final CallableStatement prepareCall(final String sql, final int type, final int concurrency)
    throws SQLException {

    return this.connection.prepareCall(sql, type, concurrency);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareCall (Connection)
  /**
   ** Creates a {@link CallableStatement} object that will generate
   ** <code>ResultSet</code> objects with the given type and concurrency. This
   ** method is the same as the <code>prepareCall</code> method above, but it
   ** allows the default result set type, result set concurrency type and
   ** holdability to be overridden.
   **
   ** @param  sql                an SQL statement that may contain one or more
   **                            '?' IN parameter placeholders. Typically this
   **                            statement is specified using JDBC call escape
   **                            syntax.
   ** @param  type               a result set type; one of
   **                            <ul>
   **                              <li><code>ResultSet.TYPE_FORWARD_ONLY</code>,
   **                              <li><code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
   **                              <li><code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
   **                            </ul>
   ** @param  concurrency        a concurrency type; one of
   **                            <ul>
   **                              <li><code>ResultSet.CONCUR_READ_ONLY</code> or
   **                              <li><code>ResultSet.CONCUR_UPDATABLE</code>
   **                            </ul>
   ** @param  holdability        one of the following <code>ResultSet</code>
   **                            constants:
   **                            <ul>
   **                              <li><code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
   **                              <li><code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
   **                            </ul>
   **
   ** @return                    a new {@link CallableStatement} object,
   **                            containing the pre-compiled SQL statement, that
   **                            will generate <code>ResultSet</code> objects
   **                            with the given type, concurrency, and
   **                            holdability.
   **
   ** @throws SQLException       if a database access error occurs, this method
   **                            is called on a closed connection or the given
   **                            parameters are not <code>ResultSet</code>
   **                            constants indicating type, concurrency, and
   **                            holdability.
   */
  @Override
  public final CallableStatement prepareCall(final String sql, final int type, final int concurrency, final int holdability)
    throws SQLException {

    return this.connection.prepareCall(sql, type, concurrency, holdability);
  }
}