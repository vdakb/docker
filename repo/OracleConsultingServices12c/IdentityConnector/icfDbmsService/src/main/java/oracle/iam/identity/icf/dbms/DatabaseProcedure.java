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

    File        :   DatabaseProcedure.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseProcedure.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Watch;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

import oracle.iam.identity.icf.resource.DatabaseBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseStatement
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseProcedure</code> provides the description of a callable
 ** statement that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseProcedure extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final short QUERY_TIMEOUT = 20;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the system watch to gather performance metrics */
  protected final Watch     watch;

  private CallableStatement statement;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseProcedure</code> .
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  watch              the {@link Watch} to gather the performance
   **                            metrics.
   **                            <br>
   **                            Allowed object is {@link Watch}.
   */
  public DatabaseProcedure(final Loggable loggable, final Watch watch) {
    // ensure inheritance
    super(loggable);

    // initialize instance attributes
    this.watch = watch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the {@link CallableStatement} this context belongs to.
   **
   ** @return                    the {@link CallableStatement} this context
   **                            belongs to.
   **                            <br>
   **                            Possible object is {@link CallableStatement}.
   */
  public final CallableStatement context() {
    return this.statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCallableStatement
  /**
   ** Creates a {@link CallableStatement} object for calling database stored
   ** procedures. The {@link CallableStatement} object provides methods for
   ** setting up its IN and OUT parameters, and methods for executing the call
   ** to a stored procedure.
   ** <p>
   ** Result sets created using the returned {@link CallableStatement} object
   ** will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of the
   ** created result sets can be determined by calling
   ** <code>getHoldability()</code>.
   **
   ** @param  connection         the JDBC connection to create a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new default {@link CallableStatement} object
   **                            containing the pre-compiled SQL statement.
   **                            <br>
   **                            Possible object is {@link CallableStatement}.
   **
   ** @throws SystemException    if SQL statement can't created.
   */
  public static final CallableStatement createCallableStatement(final Connection connection, final String string)
    throws SystemException {

    // prevent bogus input
    if (connection == null)
      throw SystemException.argumentNull("connection");

    CallableStatement statement = null;
    try {
      statement = connection.prepareCall(string);
      statement.setQueryTimeout(QUERY_TIMEOUT);
    }
    catch (SQLException e) {
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw DatabaseException.normalized(statement, e);
    }
    return statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCallableStatement
  /**
   ** Creates a {@link CallableStatement} object that will generate
   ** {@link ResultSet} objects with the given type and concurrency. This method
   ** is the same as the prepareCall method above, but it allows the default
   ** result set type and concurrency to be overridden. The holdability of the
   ** created result sets can be determined by calling getHoldability().
   **
   ** @param  connection         the JDBC connection to create a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               a result set type; one of:
   **                            <ul>
   **                              <li>{@link ResultSet#TYPE_FORWARD_ONLY}
   **                              <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
   **                              <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
   **                            </ul>
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  concurrency        a concurrency type; one of:
   **                            <ul>
   **                              <li>{@link ResultSet#CONCUR_READ_ONLY}
   **                              <li>{@link ResultSet#CONCUR_UPDATABLE}
   **                            </ul>
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a new default {@link CallableStatement} object
   **                            containing the pre-compiled SQL statement.
   **                            <br>
   **                            Possible object is {@link CallableStatement}.
   **
   ** @throws SystemException    if SQL statement can't created.
   */
  public static final CallableStatement createCallableStatement(final Connection connection, final String string, final int type, final int concurrency)
    throws SystemException {

    // prevent bogus input
    if (connection == null)
      throw SystemException.argumentNull("connection");

    CallableStatement statement = null;
    try {
      statement = connection.prepareCall(string, type, concurrency);
      statement.setQueryTimeout(QUERY_TIMEOUT);
    }
    catch (SQLException e) {
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw DatabaseException.normalized(statement, e);
    }
    return statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeStatement
  /**
   **  Close a callable statement.
   **  <br>
   **  If the statement has a dependence result set, the result set will also
   **  be closed.
   **
   ** @param  statement          the stored procedure statement to close
   **                            <br>
   **                            Allowed object is {@link CallableStatement}.
   */
  public static final void closeStatement(final CallableStatement statement) {
    // prevent bogus input
    if (statement != null)
      try {
        statement.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timerStart
  /**
   ** Starts a timer for a named task.
   ** <p>
   ** The results are undefined if {@link #timerStop(String)} or timing methods
   ** are called without invoking this method.
   **
   ** @param  name               the name of the task to start
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @see    #timerStop(String)
   */
  protected void timerStart(final String name) {
    synchronized (this.watch) {
      // start the task to gather performance metrics
      this.watch.start(DatabaseBundle.location(getClass(), name));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timerStop
  /**
   ** Stops a timer for a named task.
   ** <p>
   ** The results are undefined if {@link #timerStop(String)} or timing methods
   ** are called without invoking this method.
   **
   ** @param  name               the name of the task to start
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @see    #timerStart(String)
   */
  protected void timerStop(final String name) {
    synchronized (this.watch) {
      // stop the task from gathering performance metrics
      this.watch.stop(DatabaseBundle.location(getClass(), name));
    }
  }
}