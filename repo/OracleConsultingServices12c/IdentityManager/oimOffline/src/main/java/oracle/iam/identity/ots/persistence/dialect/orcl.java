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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   orcl.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    orcl.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.persistence.dialect;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import oracle.hst.foundation.object.Pair;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.platform.utils.vo.OIMType;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.persistence.DatabaseError;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.persistence.DatabaseConstant;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseException;

import oracle.iam.identity.ots.persistence.Repository;

////////////////////////////////////////////////////////////////////////////////
// class orcl
// ~~~~~ ~~~~
/**
 ** The dictionary dialect to managed catalogs in a Oracle Identity Manager
 ** Database deployed on a Oracle Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class orcl extends Repository {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the error translation of an Oracle Database to our implementation*/
  private static Map<String, String> ERROR = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a {@link Repository} that referes to entities that doesn't
   ** belongs to any schema (not needed due to built-in functionality of entire
   ** implementation).
   */
  public orcl() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespaceValueFilter (Repository)
  /**
   ** Returns the filter that has to be applied on a search for lookup values in
   ** the Oracle Identity Manager.
   **
   ** @param  namespace          the name of the <code>Lookup Definition</code>
   **                            the values has to be obtained from the database
   **                            of Oracle Identity Manager.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for roles in the catalog.
   */
  @Override
  public final DatabaseFilter namespaceValueFilter(final String namespace)
    throws TaskException {

    return  DatabaseFilter.build(
      DatabaseFilter.build(Namespace.UNIQUE,        namespace,                DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.build(NamespaceValue.FOREIGIN, entity(Entity.NAMESPACE), DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogRoleFilter (Repository)
  /**
   ** Returns the filter that has to be applied on a search for roles.
   **
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for roles.
   */
  @Override
  public final DatabaseFilter catalogRoleFilter()
    throws TaskException {

    return DatabaseFilter.build(Catalog.TYPE, OIMType.Role.getValue(), DatabaseFilter.Operator.EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogApplicationFilter (Repository)
  /**
   ** Returns the filter that has to be applied on a search for application
   ** instances.
   **
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for application instances.
   */
  @Override
  public final DatabaseFilter catalogApplicationFilter()
    throws TaskException {

    return DatabaseFilter.build(Catalog.TYPE, OIMType.ApplicationInstance.getValue(), DatabaseFilter.Operator.EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogEntitlementFilter (Repository)
  /**
   ** Returns the filter that has to be applied on a search for entitlements
   ** that belongs to an specific application instance described by
   ** <code>objectKey</code> and <code>resourceKey</code>.
   **
   ** @param  objectKey          the system identifier of the
   **                            <code>Resource Object</code> the entitlements
   **                            belongs to.
   ** @param  resourceKey        the system identifier of the
   **                            <code>IT Resource</code> the entitlements
   **                            belongs to.
   ** @param  lookupKey          the system identifier of the
   **                            <code>Lookup Definition</code> the entitlements
   **                            belongs to.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for for entitlements that belongs to an
   **                            specific application instance.
   */
  @Override
  public final DatabaseFilter catalogEntitlementFilter(final long objectKey, final long resourceKey, final BigDecimal lookupKey)
    throws TaskException {

    return DatabaseFilter.build(
        DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(EntitlementList.RESOURCE, objectKey,   DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(EntitlementList.ENDPOINT, resourceKey, DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(
            DatabaseFilter.build(entity(Entity.PROPERTYCOLUMN), entity(Entity.PROPERTYENTITLEMENT), DatabaseFilter.Operator.EQUAL)
            // we cannot ignore the property type because therer can be more properties on a column
          , DatabaseFilter.build(Property.NAME,                 "Lookup Code",                      DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.Operator.AND
        )
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(Catalog.KEY,  entity(Entity.ENTITLEMENT),     DatabaseFilter.Operator.EQUAL)
            // we cannot ignore the type of the catalog item because matching only the id's can return also a role or
            // an application isntance if the id's are equal
          , DatabaseFilter.build(Catalog.TYPE, OIMType.Entitlement.getValue(), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(
            EntitlementList.VALUE
          , DatabaseSelect.build(
              null
            , entity(Entity.NAMESPACEVALUE)
            , DatabaseFilter.build("lkv.lku_key",  lookupKey, DatabaseFilter.Operator.EQUAL)
            , CollectionUtility.list(Pair.of("lkv_key", "lkv_key"))
            )
          , DatabaseFilter.Operator.IN
          )
        , DatabaseFilter.Operator.AND
        )
    , DatabaseFilter.Operator.AND
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupEntitlementFilter (Repository)
  /**
   ** Returns the filter that has to be applied on a search for entitlements
   ** that belongs to an specific application instance described by
   ** <code>objectKey</code> and <code>resourceKey</code>.
   **
   ** @param  resource           the system identifier of the
   **                            <code>Resource Object</code> the entitlements
   **                            belongs to.
   ** @param  endpoint           the system identifier of the
   **                            <code>IT Resource</code> the entitlements
   **                            belongs to.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for for entitlements that belongs to an
   **                            specific application instance.
   **
   ** @throws TaskException      if the object found for the entity keys are
   **                            not a {@link DatabaseEntity}s.
   */
  @Override
  public final DatabaseFilter namespaceEntitlementFilter(final long resource, final long endpoint)
    throws TaskException {

    return DatabaseFilter.build(
        DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(EntitlementList.RESOURCE, resource, DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(EntitlementList.ENDPOINT, endpoint, DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(
            DatabaseFilter.build(entity(Entity.NAMESPACEVALUE), entity(Entity.NAMESPACEENTITLEMENT), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(entity(Entity.PROPERTYCOLUMN), entity(Entity.PROPERTYENTITLEMENT),  DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.Operator.AND
        )
      , DatabaseFilter.build(Property.NAME,  "Lookup Code", DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.Operator.AND
      );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installEntity (Repository)
  /**
   ** Creates the administrative entities supported by an Oracle Database.
   **
   ** @return                    the administrative entities supported by an
   **                            Oracle Database.
   */
  @Override
  protected final Map<Entity, DatabaseEntity> installEntity() {
    /** the administrative entity catalog supported by an Oracle Database. */
    final Map<Entity, DatabaseEntity> mapping = new EnumMap<Entity, DatabaseEntity>(Entity.class);

    mapping.put(Entity.CATALOGROLE,             new Catalog.Role());
    mapping.put(Entity.CATALOGAPPLICATION,      new Catalog.Application());
    mapping.put(Entity.CATALOGENTITLEMENT,      new Catalog.Entitlement());
    mapping.put(Entity.ENTITLEMENT,             EntitlementList.build());
    mapping.put(Entity.NAMESPACE,               new Namespace());
    mapping.put(Entity.NAMESPACEVALUE,          new NamespaceValue());
    mapping.put(Entity.NAMESPACEENTITLEMENT,    new Namespace.Entitlement());
    mapping.put(Entity.PROPERTYCOLUMN,          new Property.Column());
    mapping.put(Entity.PROPERTYENTITLEMENT,     new Property.Entitlement());
    mapping.put(Entity.PROPERTYCATALOGJOIN,     new Property.CatalogJoin());
    mapping.put(Entity.PROPERTYENTITLEMENTJOIN, new Property.EntitlementJoin());

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime (Repository)
  /**
   ** The Database Server current time will be different from the local time.
   ** <p>
   ** This current implementation ask the connected server for the current
   ** time using the configured property
   ** {@link DatabaseConstant#DATABASE_SYSTEM_TIMESTAMP}.
   ** <p>
   ** The assumption is that the configuration provides a single query statement
   ** that is able to fectch the system timestamp from the database server. For
   ** example a statement like:
   ** <pre>
   **   SELECT systimestamp FROM dual
   ** </pre>
   ** will work for an Oracle Database.
   **
   ** @param  connection         the {@link Connection} to used for the search.
   **
   ** @return                    the timestamp of the remote system.
   **
   ** @throws DatabaseException  if the operation fails
   */
  @Override
  public Date systemTime(final Connection connection)
    throws DatabaseException {

    PreparedStatement statement = null;
    ResultSet         resultSet = null;
    Timestamp         timestamp =null;
    try {
      statement = DatabaseStatement.createPreparedStatement(connection, "SELECT systimestamp FROM dual");
      resultSet = statement.executeQuery();
      timestamp = resultSet.next() ? resultSet.getTimestamp(1) : new Timestamp(0L);
      return new Date(timestamp.getTime());
    }
    catch (SQLException e) {
      throw new DatabaseException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizeError (Repository)
  /**
   ** Returns the implementation specific error code for a vendor specific code.
   ** <p>
   ** The vendor code is taken from an SQLException that is catched somewhere
   ** and wrapped in a {@link DatabaseException}. This prefix the vendor
   ** specific code constantly with <code>SQL-</code>.
   **
   ** @param  throwable          the exception thrown by a process step that
   **                            may contain a vendor spefific error code.
   **
   ** @return                    a implementation specific error code if it's
   **                            translatable; otherwise <code>DBA-0001</code>
   **                            will be returned.
   */
  @Override
  public final String normalizeError(final DatabaseException throwable) {
    final String code = throwable.code();
    if (code.startsWith(DatabaseError.VENDOR)) {
      // lazy load error code table
      if (ERROR == null)
        installError();
      return ERROR.get(code);
    }
    else
      return DatabaseError.UNHANDLED;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installError
  /**
   ** Creates the error mapping to translate vendor specific error codes thrown
   ** by a JDBC Driver to our implementation.
   */
  private static void installError() {
    ERROR = new HashMap<String, String>();
//    ERROR.put("SQL-00911", DatabaseError.INVALID_SYNTAX);
    ERROR.put("SQL-01017", DatabaseError.CONNECTION_AUTHENTICATION);
    ERROR.put("SQL-01031", DatabaseError.CONNECTION_PERMISSION);
    ERROR.put("SQL-01917", DatabaseError.OBJECT_NOT_EXISTS);
    ERROR.put("SQL-01918", DatabaseError.OBJECT_NOT_EXISTS);
    ERROR.put("SQL-01919", DatabaseError.OBJECT_NOT_EXISTS);
    ERROR.put("SQL-01920", DatabaseError.OBJECT_ALREADY_EXISTS);
  }
}