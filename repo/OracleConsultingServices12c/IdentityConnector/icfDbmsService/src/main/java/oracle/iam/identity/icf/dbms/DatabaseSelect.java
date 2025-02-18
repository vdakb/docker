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
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.resource.DatabaseBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseSearch
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseSelect</code> provides the description of a query that
 ** can be applied on Database Servers and returns all records that match the
 ** filter condition at once.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseSelect extends DatabaseStatement {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final List<DatabaseAttribute> returning;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseSelect</code> that can be applied on Database
   ** Servers and returns all records that match the filter condition at once.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the search. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   */
  protected DatabaseSelect(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseAttribute> returning) {
    // ensure inheritance
    super(loggable, entity, filter);

    // initailize instance attributes
    this.returning = returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseSelect</code> that can be
   ** applied on Database Servers and returns all records that match the filter
   ** condition at once.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated the <code>DatabaseSelect</code>.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the search. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   **
   ** @return                    an instance of <code>DatabaseSelect</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is <code>DatabaseSelect</code>.
   */
  public static DatabaseSelect build(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseAttribute> returning) {
    return new DatabaseSelect(loggable, entity, filter, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Builds and executes a SELECT-Statement for the {@link DatabaseEntity} this
   ** context belongs to that will return the attributes specified by
   ** <code>attribute</code>.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   ** @param  timeOut            the timeout period the client will wait for a
   **                            response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Map&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>;
   **                            never <code>null</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Map} with type
   **                            {@link String} as the key and type
   **                            {@link Object} as the value.
   **
   ** @throws SystemException    if the build of the filter condition fails.
   */
  public List<Map<String, Object>> execute(final Connection connection, final int timeOut)
    throws SystemException {

    try {
      prepare(connection);
      return execute(timeOut);
    }
    finally {
      // we have to close the statement after execution is done and free all
      // allocated resources
      close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Builds a SELECT-Statement for the {@link DatabaseEntity} this context
   ** belongs to, that will return the attributes specified by
   ** <code>returning</code> passed to the constructor.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   **
   ** @return                    the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   **
   ** @throws SystemException    if the build of the filter condition fails.
   */
  public List<DatabaseParameter> prepare(final Connection connection)
    throws SystemException {

    if (connection == null)
      throw SystemException.argumentNull("connection");

    if (this.returning == null)
      throw SystemException.attributeNull("returning");

    final String method ="prepare";
    trace(method, Loggable.METHOD_ENTRY);

    this.parameter = new ArrayList<DatabaseParameter>();
    try {
      this.query     = prepareSelect(this.entity, this.filter, this.parameter, this.returning);
      this.statement = createPreparedStatement(connection, this.query);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Selects in the specified context or object for entries that satisfy the
   ** given search filter.
   **
   ** @param  timeOut            the timeout period the client will wait for a
   **                            response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Map&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>; never
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Map} with type
   **                            {@link String} as the key and type
   **                            {@link Object} as the value.
   **
   ** @throws SystemException    in case the search operation cannot be
   **                            performed.
   */
  public List<Map<String, Object>> execute(final int timeOut)
    throws SystemException {

    return execute(this.parameter, timeOut);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Selects in the specified context or object for entries that satisfy the
   ** given search filter.
   **
   ** @param  parameter          the subtitition values for bind parameters
   **                            created during the evaluation of the
   **                            {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   ** @param  timeOut            the timeout period the client will wait for a
   **                            response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Map&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>;
   **                            never <code>null</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Map} with type
   **                            {@link String} as the key and type
   **                            {@link Object} as the value.
   **
   ** @throws SystemException    in case the search operation cannot be
   **                            performed.
   */
  public List<Map<String, Object>> execute(final List<DatabaseParameter> parameter, final int timeOut)
    throws SystemException {

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);
    debug(method, DatabaseBundle.string(DatabaseMessage.EXECUTE_STATEMENT, this.query));
    try {
      return execute(this.statement, parameter, this.returning, timeOut);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}