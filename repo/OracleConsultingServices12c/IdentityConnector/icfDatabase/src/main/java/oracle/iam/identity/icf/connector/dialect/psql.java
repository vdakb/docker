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

    File        :   psql.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    psql.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.dialect;

import java.util.Map;
import java.util.Date;
import java.util.EnumMap;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseAttribute;

import oracle.iam.identity.icf.connector.DatabaseSchema;
import oracle.iam.identity.icf.connector.DatabaseCatalog;
import oracle.iam.identity.icf.connector.DatabaseDialect;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class psql
// ~~~~~ ~~~~
/**
 ** The dictionary dialect to managed accounts in a PostgreSQL Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class psql extends DatabaseDialect {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the account state we are interested on */
  private static final DatabaseFilter ACCOUNT_FILTER = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Dialect</code> for a PostgreSQL Database that referes
   ** to entities that belongs to the specified schema.
   */
  public psql() {
    // ensure inheritance
    super("pg_catalog");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountFilter (DatabaseDialect)
  /**
   ** Returns the filter that has to be applied on a search for accounts.
   **
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   */
  @Override
  public final DatabaseFilter accountFilter() {
    return ACCOUNT_FILTER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountTime (DatabaseDialect)
  /**
   ** Returns the filter that has to be applied on a search for accounts.
   **
   ** @param  timestamp          the time after that an account in the target
   **                            system should be created or modified to be
   **                            included in the returning collection.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   */
  @Override
  public final DatabaseFilter accountTime(final Date timestamp) {
    return DatabaseFilter.build("created",  timestamp, DatabaseFilter.Operator.GREATER_EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installEntity (DatabaseDialect)
  /**
   ** Creates the administrative entities supported by an PostgreSQL Database.
   **
   ** @return                    the administrative entities supported by an
   **                            PostgreSQL Database.
   */
  @Override
  protected final Map<DatabaseSchema.Entity, DatabaseEntity> installEntity() {
    /** the administrative entity catalog supported by a PostgreSQL Database. */
    final Map<DatabaseSchema.Entity, DatabaseEntity> mapping = new EnumMap<DatabaseSchema.Entity, DatabaseEntity>(DatabaseSchema.Entity.class);

    mapping.put(DatabaseSchema.Entity.Account, DatabaseEntity.build(this.schema, "pg_user", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("usesysid",             "systemId")
    , DatabaseAttribute.build("username",             "userName",            DatabaseAttribute.type(REQUIRED))
    , DatabaseAttribute.build("password",             "password",            DatabaseAttribute.type(SENSITIVE))
    , DatabaseAttribute.build("valuntil",             "passwordExpiry")
    , DatabaseAttribute.build("useconfig",            "runtimeConfig")
    , DatabaseAttribute.build("usesuper",             "superUser")
    , DatabaseAttribute.build("userepl",              "replicationUser")
    , DatabaseAttribute.build("usebypassrls",         "bypassRowLevelSecurity")
    , DatabaseAttribute.build("usecreatedb",          "createDB")
    )));
    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installCatalog (DatabaseDialect)
  /**
   ** Creates the administrative entity catalog supported by a PostgreSQL
   ** Database.
   **
   ** @return                    the administrative catalog supported by an
   **                            PostgreSQL Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installCatalog() {
    // the system catalog provided by an PostgreSQL Database
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);
    mapping.put(DatabaseSchema.Catalog.Role,     DatabaseCatalog.build(this.schema, "pg_role",      DatabaseAttribute.build("oid")));
    mapping.put(DatabaseSchema.Catalog.Sequence, DatabaseCatalog.build(this.schema, "pg_sequences", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("sequencename")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Sequence.id())
    , DatabaseAttribute.build("sequenceowner")
    )));
    mapping.put(DatabaseSchema.Catalog.Table,    DatabaseCatalog.build(this.schema, "pg_tables", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("tablename")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Table.id())
    , DatabaseAttribute.build("tableowner")
    )));
    mapping.put(DatabaseSchema.Catalog.View,     DatabaseCatalog.build(this.schema, "pg_views", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("viewname")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.View.id())
    , DatabaseAttribute.build("viewowner")
    )));
    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation (DatabaseDialect)
  /**
   ** Creates the administrative operations applicable on accounts and/or roles
   ** supported by an PostgreSQL Database.
   **
   ** @return                    the administrative operations applicable on
   **                            accounts and/or roles supported by a PostgreSQL
   **                            Database.
   */
  @Override
  protected final Map<Operation, String> installOperation() {
    final Map<Operation, String> mapping = new EnumMap<Operation, String>(Operation.class);
    return mapping;
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installPermission (DatabaseDialect)
  /**
   ** Creates the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by an PostgreSQL Database.
   **
   ** @return                    the administrative operations providing granted
   **                            permissions on objects to accounts and/or roles
   **                            supported by an PostgreSQL Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installPermission() {
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);
    mapping.put(DatabaseSchema.Catalog.Role,      DatabaseCatalog.build(this.schema, "pg_roles", new int[]{0}, CollectionUtility.list(
        DatabaseAttribute.build("oid",            "systemID")
      , DatabaseAttribute.build("rolename",       "name")
      , DatabaseAttribute.build("rolpassword",    "password")
      , DatabaseAttribute.build("rolvaliduntil",  "passwordExpiry")
      , DatabaseAttribute.build("rolsuper",       "delegated")
      , DatabaseAttribute.build("rolinherit",     "inherited")
      , DatabaseAttribute.build("rolcanlogin",    "login")
      , DatabaseAttribute.build("rolcreatedb",    "createDB")
      , DatabaseAttribute.build("rolcreaterole",  "createRole")
      , DatabaseAttribute.build("rolconnlimit",   "connectionLimit")
      , DatabaseAttribute.build("rolbypassrls",   "bypassRowLevelSecurity")
      , DatabaseAttribute.build("rolconfig",      "runtimeConfig")
    )));
    return mapping;
  }
}