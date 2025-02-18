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

    File        :   DatabaseInsert.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseInsert.


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
// class DatabaseInsert
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseUpdate</code> provides the description of an insert
 ** operations that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseInsert extends DatabaseModify {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseInsert</code> that can be applied on Database
   ** Servers and create records that match the filter condition by consecutive
   ** invocation of {@link #execute(Map)}.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  binding            the {@link List} of named-value pair attributes
   **                            that are part of this entity operation.
   */
  private DatabaseInsert(final DatabaseEntity entity, final List<Pair<String, DatabaseParameter>> binding) {
    // ensure inheritance
    this(entity, binding, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseInsert</code> that can be applied on Database
   ** Servers and create records that match the filter condition by consecutive
   ** invocation of {@link #execute(Map)}.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  binding            the {@link List} of named-value pair attributes
   **                            that are part of this entity operation.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   */
  private DatabaseInsert(final DatabaseEntity entity, final List<Pair<String, DatabaseParameter>> binding, final Collection<Pair<String, Integer>> returning) {
    // ensure inheritance
    super(entity, null, binding, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareModify (DatabaseModify)
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

    return prepareInsert(this.entity, this.binding, this.returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (DatabaseModify)
  /**
   ** Inserts in the {@link DatabaseEntity} this context belongs to and binds
   ** the passed attribute <code>data</code> to it.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Pair&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>;
   **                            never <code>null</code>.
   **
   ** @throws DatabaseException  if the operation fails
   */
  protected Map<String, Object> execute()
    throws DatabaseException {

    final Map<String, Object> result = new HashMap<String, Object>();
    try {
      int k = 1;
      // bind values to the place holders
      for (Pair<String, DatabaseParameter> cursor : this.binding) {
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

      // register the return parameter id any
      if (this.returning != null) {
        for (Pair<String, Integer> cursor : this.returning)
          this.statement.registerReturnParameter(k++, cursor.value.intValue());
      }

      int affected = this.statement.executeUpdate();
      // on create more than one entries can be affected thus we have to test
      // for at least one
      if (affected == 0)
        throw new DatabaseException(DatabaseError.OBJECT_NOT_CREATED, entity.toString());

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
   ** Factory method to create an <code>DatabaseInsert</code> that can be
   ** applied on Database Servers and create records that match the filter
   ** condition by consecutive invocation of {@link #execute(Connection)}.
    **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   **
   ** @return                    an instance of <code>DatabaseInsert</code>
   **                            with the properties provided.
   */
  public static DatabaseInsert build(final DatabaseEntity entity, final List<Pair<String, DatabaseParameter>> binding) {
    return new DatabaseInsert(entity, binding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseInsert</code> that can be
   ** applied on Database Servers and create records that match the filter
   ** condition by consecutive invocation of {@link #execute(Connection)}.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **
   ** @return                    an instance of <code>DatabaseInsert</code>
   **                            with the properties provided.
   */
  public static DatabaseInsert build(final DatabaseEntity entity, final List<Pair<String, DatabaseParameter>> binding, final Collection<Pair<String, Integer>> returning) {
    return new DatabaseInsert(entity, binding, returning);
  }
}