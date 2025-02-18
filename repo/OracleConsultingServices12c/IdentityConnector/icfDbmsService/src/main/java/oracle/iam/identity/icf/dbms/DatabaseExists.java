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

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseExists
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseExists</code> provides the description of a query that
 ** can be applied on Database Servers and returns one records that match the
 ** filter condition at once.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
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
   **                            instantiated this {@link AbstractLoggable}.
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
   */
  private DatabaseExists(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter) {
    // ensure inheritance
    super(loggable, entity, filter, CollectionUtility.list(DatabaseAttribute.build("x", "x")));
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
   **                            instantiated this {@link DatabaseSelect}.
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
   **
   ** @return                    an instance of <code>DatabaseExists</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is <code>DatabaseExists</code>.
   */
  public static DatabaseExists build(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter) {
    return new DatabaseExists(loggable, entity, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare (overridden)
  /**
   ** Builds a SELECT-Statement for the {@link DatabaseEntity} this context
   ** belongs to, that will return the existance of a record which match the
   ** <code>filter</code> passed to the constructor.
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
   ** @throws SystemException    if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  @Override
  public List<DatabaseParameter> prepare(final Connection connection)
    throws SystemException {

    if (connection == null)
      throw SystemException.argumentNull("connection");

    final String method ="prepare";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      this.parameter = new ArrayList<DatabaseParameter>();
      this.query     = prepareExists(this.entity, this.filter, this.parameter);
      this.statement = createPreparedStatement(connection, this.query);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return this.parameter;
  }
}