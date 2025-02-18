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

    File        :   msql.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    msql.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.dialect;

import java.util.Map;
import java.util.Date;
import java.util.EnumMap;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseAttribute;

import oracle.iam.identity.icf.connector.DatabaseSchema;
import oracle.iam.identity.icf.connector.DatabaseCatalog;
import oracle.iam.identity.icf.connector.DatabaseDialect;

////////////////////////////////////////////////////////////////////////////////
// class msft
// ~~~~~ ~~~~
/**
 ** The dictionary dialect to managed accounts in a MySQL Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class msql extends DatabaseDialect {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Dialect</code> for a MySQL Database that referes to
   ** entities that belongs to the specified schema.
   */
  public msql() {
    // ensure inheritance
    super("mysql");
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
    return DatabaseFilter.NOP;
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
    return DatabaseFilter.NOP;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installEntity (DatabaseDialect)
  /**
   ** Creates the administrative entity catalog supported by a MySQL Database.
   **
   ** @return                    the administrative entities supported by a
   **                            MySQL Database.
   */
  @Override
  protected final Map<DatabaseSchema.Entity, DatabaseEntity> installEntity() {
    // the administrative entity catalog supported by a MySQL Database.
    final Map<DatabaseSchema.Entity, DatabaseEntity> mapping = new EnumMap<DatabaseSchema.Entity, DatabaseEntity>(DatabaseSchema.Entity.class);

    mapping.put(DatabaseSchema.Entity.Account, DatabaseEntity.build(this.schema, "user", DatabaseAttribute.build("user")));

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installCatalog (DatabaseDialect)
  /**
   ** Creates the administrative entity catalog supported by a MySQL Database.
   **
   ** @return                    the administrative catalog supported by a
   **                            MySQL Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installCatalog() {
    // the system catalog provided by a MySQL Database
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);

    mapping.put(DatabaseSchema.Catalog.Schema,    DatabaseCatalog.build(this.schema, "information_schema.schemata", DatabaseAttribute.build("schema_name")));
    mapping.put(DatabaseSchema.Catalog.Table,     DatabaseCatalog.build(this.schema, "information_schema.tables",   DatabaseAttribute.build("table_name"),   "table_type",   "BASE TABLE"));
    mapping.put(DatabaseSchema.Catalog.View,      DatabaseCatalog.build(this.schema, "information_schema.views",    DatabaseAttribute.build("table_name")));
    mapping.put(DatabaseSchema.Catalog.Function,  DatabaseCatalog.build(this.schema, "information_schema.routines", DatabaseAttribute.build("routine_name"), "routine_type", "???????????"));
    mapping.put(DatabaseSchema.Catalog.Procedure, DatabaseCatalog.build(this.schema, "information_schema.routines", DatabaseAttribute.build("routine_name"), "routine_type", "???????????"));

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation (DatabaseDialect)
  /**
   ** Creates the administrative operations applicable on accounts and/or roles
   ** supported by a MySQL Database.
   **
   ** @return                    the administrative operations applicable on
   **                            accounts and/or roles supported by a MySQL
   **                            Database.
   */
  @Override
  protected final Map<Operation, String> installOperation() {
    // the administrative operations applicable on accounts and/or roles
    // supported by a MySQL Database.
    final Map<Operation, String> mapping = new EnumMap<Operation, String>(Operation.class);

    mapping.put(Operation.ACCOUNT_CREATE,               "CREATE USER '$[" + USERNAME + "]'@'%' IDENTIFIED BY '$[" + PASSWORD + "]'");
    mapping.put(Operation.ACCOUNT_DELETE,               "DROP USER '$["   + USERNAME + "]'@'%'");
    mapping.put(Operation.ACCOUNT_PRIVILEGE_GRANT,      "GRANT $[" + PERMISSION + "] ON *.* TO $[" + USERNAME + "]");
    mapping.put(Operation.ACCOUNT_PRIVILEGE_GRANT_WITH, "GRANT $[" + PERMISSION + "] ON *.* TO $["   + USERNAME + "] WITH GRANT OPTION");
    mapping.put(Operation.ACCOUNT_PRIVILEGE_REVOKE,     "REVOKE $[" + PERMISSION + "], GRANT OPTION ON *.* FROM $[" + USERNAME + "]");
    mapping.put(Operation.ACCOUNT_OBJECT_GRANT,         "GRANT $[" + PERMISSION + "] ON $[" + OBJECT + "] TO $[" + USERNAME + "]");
    mapping.put(Operation.ACCOUNT_OBJECT_GRANT_WITH,    "GRANT $[" + PERMISSION + "] ON $[" + OBJECT + "] TO $["   + USERNAME + "] WITH GRANT OPTION");
    mapping.put(Operation.ACCOUNT_OBJECT_REVOKE,        "REVOKE $[" + PERMISSION + "] ON $[" + OBJECT + "], GRANT OPTION FROM $[" + USERNAME + "]");

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installPermission (DatabaseDialect)
  /**
   ** Creates the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by a MySQL Database.
   **
   ** @return                    the administrative operations providing granted
   **                            permissions on objects to accounts and/or roles
   **                            supported by a MySQL Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installPermission() {
    // the administrative operations providing granted permissions on objects
    // to accounts and/or roles supported by a MySQL Database.
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);
    mapping.put(DatabaseSchema.Catalog.Schema,    DatabaseCatalog.build(this.schema, "information_schema.schema_privileges", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("privilege_type granted")
    , DatabaseAttribute.build("is_grantable")
    )));
    mapping.put(DatabaseSchema.Catalog.Privilege, DatabaseCatalog.build(this.schema, "information_schema.user_privileges",   new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("privilege_type granted")
    , DatabaseAttribute.build("is_grantable")
    )));
    return mapping;
  }
}