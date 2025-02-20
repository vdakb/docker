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

    File        :   DatabaseUpdate.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseUpdate.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import oracle.jdbc.OracleTypes;

import oracle.hst.foundation.object.Pair;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseUpdate
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseUpdate</code> provides the description of an update
 ** operations that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseUpdate extends DatabaseModify {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseUpdate</code> that can be applied on Database
   ** Servers and modifies records that match the filter condition by consecutive
   ** invocation of {@link #execute(Map)}.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the {@link List} of named-value pair attributes
   **                            that are part of this entity operation.
   */
  private DatabaseUpdate(final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, DatabaseParameter>> binding) {
    // ensure inheritance
    this(entity, filter, binding, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseUpdate</code> that can be applied on Database
   ** Servers and modifies records that match the filter condition by consecutive
   ** invocation of {@link #execute(Map)}.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the {@link List} of named-value pair attributes
   **                            that are part of this entity operation.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   */
  private DatabaseUpdate(final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, DatabaseParameter>> binding, final Collection<Pair<String, Integer>> returning) {
    // ensure inheritance
    super(entity, filter, binding, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare (DatabaseModify)
  /**
   ** Prepares an INSERT-Statement for the {@link DatabaseEntity} this
   ** context belongs.
   **
   ** @return                    the statement string ready for bind and
   **                            execution operations.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  @Override
  protected String prepareModify()
    throws DatabaseException {

    return prepareUpdate(this.entity, this.filter, this.parameter, this.binding, this.returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (DatabaseModify)
  /**
   ** Updates in the {@link DatabaseEntity} this context belongs to and binds
   ** the passed attribute <code>data</code> to it.
   **
   ** @throws DatabaseException  if the operation fails
   */
  @Override
  protected Map<String, Object> execute()
    throws DatabaseException {

    final Map<String, Object> result = new HashMap<String, Object>();
    try {
      int k = 1;
      // bind values to the place holders
      for (Pair<String, DatabaseParameter> cursor : this.binding) {
        // skip unmapped values
        if (cursor.value == null) {
          this.statement.setNull(k++, OracleTypes.NULL);
        }
        else {
          switch(cursor.value.type()) {
            case OracleTypes.BOOLEAN   : this.statement.setBoolean(k++, cursor.value.booleanValue());
                                         break;
            case OracleTypes.INTEGER   : this.statement.setInt(k++, cursor.value.integerValue());
                                         break;
            case OracleTypes.NUMERIC   : this.statement.setLong(k++, cursor.value.longValue());
                                         break;
            case OracleTypes.DOUBLE    : this.statement.setDouble(k++, cursor.value.doubleValue());
                                         break;
            case OracleTypes.FLOAT     : this.statement.setFloat(k++, cursor.value.floatValue());
                                         break;
            case OracleTypes.DECIMAL   : this.statement.setBigDecimal(k++, cursor.value.bigDecimalValue());
                                         break;
            case OracleTypes.DATE      : final Long date = cursor.value.longValue();
                                         this.statement.setDate(k++, date == null ? null : new java.sql.Date(date));
                                         break;
            case OracleTypes.TIME      : final Long time = cursor.value.longValue();
                                         this.statement.setTime(k++, time == null ? null : new java.sql.Time(time));
                                         break;
            case OracleTypes.TIMESTAMP : final Long timestamp = cursor.value.longValue();
                                         this.statement.setTimestamp(k++, timestamp == null ? null : new java.sql.Timestamp(timestamp));
                                         break;
            case OracleTypes.VARCHAR   : this.statement.setString(k++, cursor.value.stringValue());
                                         break;
            default                    : this.statement.setObject(k++, cursor.value.value());
          }
        }
      }

      // set the bindings for the filter criteria evaluated so far in the
      // preparation of update
      for (int j = 0; j < this.parameter.size(); j++ ) {
        final DatabaseParameter parameter = this.parameter.get(j);
        switch (parameter.type()) {
          case OracleTypes.BOOLEAN   : this.statement.setBoolean(k++, parameter.booleanValue());
                                       break;
          case OracleTypes.INTEGER   : this.statement.setInt(k++, parameter.integerValue());
                                       break;
          case OracleTypes.NUMERIC   : this.statement.setLong(k++, parameter.longValue());
                                       break;
          case OracleTypes.DOUBLE    : this.statement.setDouble(k++, parameter.doubleValue());
                                       break;
          case OracleTypes.FLOAT     : this.statement.setFloat(k++, parameter.floatValue());
                                       break;
          case OracleTypes.DECIMAL   : this.statement.setBigDecimal(k++, parameter.bigDecimalValue());
                                       break;
          case OracleTypes.DATE      : this.statement.setDate(k++, new java.sql.Date(parameter.dateValue().getTime()));
                                       break;
          case OracleTypes.TIME      : this.statement.setTime(k++, new java.sql.Time(parameter.dateValue().getTime()));
                                       break;
          case OracleTypes.TIMESTAMP : this.statement.setTimestamp(k++, new java.sql.Timestamp(parameter.longValue()));
                                       break;
          case OracleTypes.VARCHAR   : this.statement.setString(k++, parameter.stringValue());
                                       break;
          default                    : this.statement.setObject(k++, parameter.value());
        }
      }

      // register the return parameter id any
      if (this.returning != null) {
        for (Pair<String, Integer> cursor : this.returning)
          this.statement.registerReturnParameter(k++, cursor.value.intValue());
      }

      int affected = this.statement.executeUpdate();
      // on update more than one entries can be affected thus we have to test
      // for at least one
      if (affected == 0) {
        final String[] arguments = {this.filter.toString(), this.entity.toString()};
        throw new DatabaseException(DatabaseError.OBJECT_NOT_MODIFIED, arguments);
      }

      // obtain returning values from returning result set if any
      if (this.returning != null) {
        populate(result);
      }
    }
    catch (SQLSyntaxErrorException e) {
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw new DatabaseException(e);
    }
    catch (SQLException e) {
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw new DatabaseException(e);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseUpdate</code> that can be
   ** applied on Database Servers and modifies records that match the filter
   ** condition by consecutive invocation of {@link #execute(Connection)}.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   **
   ** @return                    an instance of <code>DatabaseUpdate</code>
   **                            with the properties provided.
   */
  public static DatabaseUpdate build(final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, DatabaseParameter>> binding) {
    return new DatabaseUpdate(entity, filter, binding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseUpdate</code> that can be
   ** applied on Database Servers and modifies records that match the filter
   ** condition by consecutive invocation of {@link #execute(Connection)}.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the names of the attributes that are part of
   **                            this entity operation.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **
   ** @return                    an instance of <code>DatabaseUpdate</code>
   **                            with the properties provided.
   */
  public static DatabaseUpdate build(final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, DatabaseParameter>> binding, final Collection<Pair<String, Integer>> returning) {
    return new DatabaseUpdate(entity, filter, binding, returning);
  }
}