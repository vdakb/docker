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

    File        :   DatabaseSelect.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseSelect.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseExists
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseExists</code> provides the description of a query that
 ** can be applied on Database Servers and returns one records that match the
 ** filter condition at once.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DatabaseExists extends DatabaseSelect {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseExists</code> that can be applied on Database
   ** Servers and returns all records that match the filter condition at once.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the search. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   */
  private DatabaseExists(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, String>> returning) {
    // ensure inheritance
    super(loggable, entity, filter, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseExists</code> that can be
   ** applied on Database Servers and returns all records that match the filter
   ** condition at once.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated the {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the search. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **
   ** @return                    an instance of <code>DatabaseExists</code>
   **                            with the properties provided.
   */
  public static DatabaseExists build(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, String>> returning) {
    return new DatabaseExists(loggable, entity, filter, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare (overridden)
  /**
   ** Builds a SELECT-Statement for the {@link DatabaseEntity} this context
   ** belongs to, that will return the existance of a record which match the
   ** <code>filter</code> passed to the constructor.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **
   ** @return                    the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  @Override
  public List<DatabaseParameter> prepare(final Connection connection)
    throws DatabaseException {

    // prevent bogus input
    if (connection == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "connection");

    // prevent bogus instance state
    if (this.returning == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "returning");

    final String method ="prepare";
    trace(method, SystemMessage.METHOD_ENTRY);

    this.parameter = new ArrayList<DatabaseParameter>();
    try {
      this.query     = prepareExists(this.entity, this.filter, this.parameter);
      this.statement = createPreparedStatement(connection, this.query);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return this.parameter;
  }
}