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

    File        :   DatabaseModify.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseModify.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import java.util.Set;
import java.util.Map;

import java.sql.Connection;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseModify
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseModify</code> provides the description of insert and
 ** update operations that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
abstract class DatabaseModify extends DatabaseStatement {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Set<String> binding;
  protected final String[]    returning;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseModify</code> that can be applied on Database
   ** Servers and create records that match the filter condition by consecutive
   ** invocation of {@link #execute{Map)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   */
  protected DatabaseModify(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final Set<String> binding) {
    // ensure inheritance
    this(loggable, entity, filter, binding, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseModify</code> that can be applied on Database
   ** Servers and modifies records that match the filter condition by consecutive
   ** invocation of {@link #execute{Map)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the search. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   */
  protected DatabaseModify(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final Set<String> binding, final String[] returning) {
    // ensure inheritance
    super(loggable, entity, filter);

    // initailize instance attributes
    this.binding   = binding;
    this.returning = returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Builds and executes an INSERT/UPDATE-Statement for the
   ** {@link DatabaseEntity} this context belongs to.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   ** @param  data               the attributes of the entry to create or modify.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public abstract void execute(final Connection connection, final Map<String, Object> data)
    throws DatabaseException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Inserts in the {@link DatabaseEntity} this context belongs to and binds
   ** the passed attribute <code>data</code> to it.
   **
   ** @param  data               the attributes of the entry to create.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public abstract void execute(final Map<String, Object> data)
    throws DatabaseException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Builds a SQL-Statement for the {@link DatabaseEntity} this context
   ** belongs that will return the attributes specified by
   ** <code>returning</code>.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   ** @param  query              the SQL query to prepare.
   **
   ** @throws DatabaseException  if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  protected void prepare(final Connection connection, final String query)
    throws DatabaseException {

    if (connection == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "connection");

    if (this.statement != null)
      throw new DatabaseException(DatabaseError.INSTANCE_ILLEGAL_STATE, "statement");

    final String method ="prepare";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      this.query     = query;
      this.statement = createPreparedStatement(connection, this.query);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}