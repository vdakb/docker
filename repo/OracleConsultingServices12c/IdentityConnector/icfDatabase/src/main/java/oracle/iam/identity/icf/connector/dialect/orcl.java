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

    File        :   orcl.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    orcl.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.dialect;

import java.util.Map;
import java.util.Date;
import java.util.EnumMap;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseAttribute;

import oracle.iam.identity.icf.connector.DatabaseSchema;
import oracle.iam.identity.icf.connector.DatabaseCatalog;
import oracle.iam.identity.icf.connector.DatabaseDialect;

////////////////////////////////////////////////////////////////////////////////
// class orcl
// ~~~~~ ~~~~
/**
 ** The dictionary dialect to managed accounts in an Oracle Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class orcl extends DatabaseDialect {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the account state we are interested on */
  private static final DatabaseFilter ACCOUNT_FILTER = DatabaseFilter.build("account_status", "OPEN", DatabaseFilter.Operator.EQUAL);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Dialect</code> for an Oracle Database that referes to
   ** entities that belongs to the specified schema.
   */
  public orcl() {
    // ensure inheritance
    super("sys");
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
   ** Creates the administrative entities supported by an Oracle Database.
   **
   ** @return                    the administrative entities supported by an
   **                            Oracle Database.
   */
  @Override
  protected final Map<DatabaseSchema.Entity, DatabaseEntity> installEntity() {
    /** the administrative entity catalog supported by an Oracle Database. */
    final Map<DatabaseSchema.Entity, DatabaseEntity> mapping = new EnumMap<DatabaseSchema.Entity, DatabaseEntity>(DatabaseSchema.Entity.class);

    mapping.put(DatabaseSchema.Entity.Catalog, DatabaseEntity.build(this.schema, "dba_tab_columns", new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("table_name",  "tableName")
    , DatabaseAttribute.build("column_name", "columnName")
    , DatabaseAttribute.build("data_type",   "dataType")
    , DatabaseAttribute.build("nullable",    "nilable", DatabaseAttribute.type(Boolean.class, REQUIRED))
    )));
    mapping.put(DatabaseSchema.Entity.Account, DatabaseEntity.build(this.schema, "dba_users", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("user_id",              "userId")
    , DatabaseAttribute.build("username",             "userName")
    , DatabaseAttribute.build("password",             "password",         DatabaseAttribute.type(SENSITIVE))
    , DatabaseAttribute.build("account_status",       "status")
    , DatabaseAttribute.build("default_tablespace",   "defaultTableSpace")
    , DatabaseAttribute.build("temporary_tablespace", "temporaryTableSpace")
    , DatabaseAttribute.build("created",              "created",          DatabaseAttribute.type(Date.class, OPERATIONAL))
    , DatabaseAttribute.build("profile",              "profile")
    , DatabaseAttribute.build("lock_date",            "locked",           DatabaseAttribute.type(Date.class, OPERATIONAL))
    , DatabaseAttribute.build("expiry_date",           "expire",          DatabaseAttribute.type(Date.class, OPERATIONAL))
    , DatabaseAttribute.build("external_name",         "externalName")
    )));
    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installCatalog (DatabaseDialect)
  /**
   ** Creates the administrative entity catalog supported by an Oracle Database.
   **
   ** @return                    the administrative catalog supported by an
   **                            Oracle Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installCatalog() {
    // the system catalog provided by an Oracle Database
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);

    mapping.put(DatabaseSchema.Catalog.Privilege,           DatabaseCatalog.build(this.schema, "system_privilege_map", DatabaseAttribute.build("name")));
    mapping.put(DatabaseSchema.Catalog.Role,                DatabaseCatalog.build(this.schema, "dba_roles",            DatabaseAttribute.build("role")));
    mapping.put(DatabaseSchema.Catalog.Profile,             DatabaseCatalog.build(this.schema, "dba_profiles",         DatabaseAttribute.build("profile", "profile", DatabaseAttribute.type(String.class), "distinct")));
    mapping.put(DatabaseSchema.Catalog.Schema,              DatabaseCatalog.build(this.schema, "dba_users",            DatabaseAttribute.build("username"),    DatabaseFilter.build("username",        DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")), DatabaseFilter.Operator.IN)));
    mapping.put(DatabaseSchema.Catalog.Synonym,             DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Synonym.id())
    )));
    mapping.put(DatabaseSchema.Catalog.Sequence,            DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Sequence.id())
    )));
    mapping.put(DatabaseSchema.Catalog.Table,               DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Table.id())
    )));
    mapping.put(DatabaseSchema.Catalog.View,                DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.View.id())
    )));
    mapping.put(DatabaseSchema.Catalog.Type,                DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Type.id())
    )));
    mapping.put(DatabaseSchema.Catalog.Function,            DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Function.id())
    )));
    mapping.put(DatabaseSchema.Catalog.Procedure,           DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Procedure.id())
    )));
    mapping.put(DatabaseSchema.Catalog.Package,             DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Package.id())
    )));
    mapping.put(DatabaseSchema.Catalog.Java,                DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.Java.id())
    )));
    mapping.put(DatabaseSchema.Catalog.DOTNET,              DatabaseCatalog.build(this.schema, "dba_objects",          new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("owner")
    , DatabaseAttribute.build("object_name")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.DOTNET.id())
    )));
    mapping.put(DatabaseSchema.Catalog.TablespacePermanent, DatabaseCatalog.build(this.schema, "dba_tablespaces",      new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("contents")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.TablespacePermanent.id())
    , DatabaseAttribute.build("tablespace_name")
    )));
    mapping.put(DatabaseSchema.Catalog.TablespaceTemporary, DatabaseCatalog.build(this.schema, "dba_tablespaces",      new int[]{0,1,2}, CollectionUtility.list(
      DatabaseAttribute.build("contents")
    , DatabaseAttribute.build(DatabaseSchema.Catalog.TablespaceTemporary.id())
    , DatabaseAttribute.build("tablespace_name")
    )));

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation (DatabaseDialect)
  /**
   ** Creates the administrative operations applicable on accounts and/or roles
   ** supported by an Oracle Database.
   **
   ** @return                    the administrative operations applicable on
   **                            accounts and/or roles supported by an Oracle
   **                            Database.
   */
  @Override
  protected final Map<Operation, String> installOperation() {
    final Map<Operation, String> mapping = new EnumMap<Operation, String>(Operation.class);

    mapping.put(Operation.ACCOUNT_CREATE,               "CREATE USER $[" + USERNAME + "] IDENTIFIED BY $[" + PASSWORD + "]");
    mapping.put(Operation.ACCOUNT_DELETE,               "DROP USER $["   + USERNAME + "] CASCADE");
    mapping.put(Operation.ACCOUNT_MODIFY,               "ALTER USER $["  + USERNAME + "] $[" + ATTRIBUTE_NAME+ "] $[" + ATTRIBUTE_VALUE + "]");
    mapping.put(Operation.ACCOUNT_PASSWORD,             "ALTER USER $["  + USERNAME + "] IDENTIFIED BY $[" + PASSWORD + "]");
    mapping.put(Operation.ACCOUNT_ENABLE,               "ALTER USER $["  + USERNAME + "] ACCOUNT UNLOCK");
    mapping.put(Operation.ACCOUNT_DISABLE,              "ALTER USER $["  + USERNAME + "] ACCOUNT LOCK");
    mapping.put(Operation.ACCOUNT_PRIVILEGE_GRANT,      "GRANT $[" + PERMISSION + "] TO $[" + USERNAME + "]");
    mapping.put(Operation.ACCOUNT_PRIVILEGE_GRANT_WITH, "GRANT $[" + PERMISSION + "] TO $["   + USERNAME + "] WITH ADMIN OPTION");
    mapping.put(Operation.ACCOUNT_PRIVILEGE_REVOKE,     "REVOKE $[" + PERMISSION + "] FROM $[" + USERNAME + "]");
    mapping.put(Operation.ACCOUNT_ROLE_GRANT,           "GRANT $["  + PERMISSION + "] TO $["   + USERNAME + "]");
    mapping.put(Operation.ACCOUNT_ROLE_GRANT_WITH,      "GRANT $["  + PERMISSION + "] TO $["   + USERNAME + "] WITH ADMIN OPTION");
    mapping.put(Operation.ACCOUNT_ROLE_REVOKE,          "REVOKE $[" + PERMISSION + "] FROM $[" + USERNAME + "]");
    mapping.put(Operation.ACCOUNT_OBJECT_GRANT,         "GRANT $[" + PERMISSION + "] ON $[" + OBJECT + "] TO $[" + USERNAME + "]");
    mapping.put(Operation.ACCOUNT_OBJECT_GRANT_WITH,    "GRANT $[" + PERMISSION + "] ON $[" + OBJECT + "] TO $[" + USERNAME + "] WITH ADMIN OPTION");
    mapping.put(Operation.ACCOUNT_OBJECT_REVOKE,        "REVOKE $[" + PERMISSION + "] ON $[" + OBJECT + "] FROM $[" + USERNAME + "]");

    mapping.put(Operation.ROLE_CREATE,                  "CREATE ROLE $[" + ROLENAME + "]");
    mapping.put(Operation.ROLE_CREATE_PROTECTED,        "CREATE ROLE $[" + ROLENAME + "] IDENTIFIED BY $[" + PASSWORD + "]");
    mapping.put(Operation.ROLE_DELETE,                  "DROP ROLE $["   + ROLENAME + "]");
    mapping.put(Operation.ROLE_PROTECT,                 "ALTER ROLE $["  + ROLENAME + "] IDENTIFIED BY $[" + PASSWORD + "]");
    mapping.put(Operation.ROLE_UNPROTECTED,             "ALTER ROLE $["  + ROLENAME + "] NOT IDENTIFIED");
    mapping.put(Operation.ROLE_PRIVILEGE_GRANT,         "GRANT $["  + PERMISSION + "] TO $["   + ROLENAME + "]");
    mapping.put(Operation.ROLE_PRIVILEGE_GRANT_WITH,    "GRANT $["  + PERMISSION + "] TO $["   + ROLENAME + "] WITH ADMIN OPTION");
    mapping.put(Operation.ROLE_PRIVILEGE_REVOKE,        "REVOKE $[" + PERMISSION + "] FROM $[" + ROLENAME + "]");
    mapping.put(Operation.ROLE_ROLE_GRANT,              "GRANT $["  + PERMISSION + "] TO $["   + ROLENAME + "]");
    mapping.put(Operation.ROLE_ROLE_GRANT_WITH,         "GRANT $["  + PERMISSION + "] TO $["   + ROLENAME + "] WITH ADMIN OPTION");
    mapping.put(Operation.ROLE_ROLE_REVOKE,             "REVOKE $[" + PERMISSION + "] FROM $[" + ROLENAME + "]");
    mapping.put(Operation.ROLE_OBJECT_GRANT,            "GRANT $[" + PERMISSION + "] ON $[" + OBJECT + "] TO $[" + ROLENAME + "]");
    mapping.put(Operation.ROLE_OBJECT_GRANT_WITH,       "GRANT $[" + PERMISSION + "] ON $[" + OBJECT + "] TO $[" + ROLENAME + "] WITH ADMIN OPTION");
    mapping.put(Operation.ROLE_OBJECT_REVOKE,           "REVOKE $[" + PERMISSION + "] ON $[" + OBJECT + "] FROM $[" + ROLENAME + "]");

    return mapping;
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installPermission (DatabaseDialect)
  /**
   ** Creates the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by an Oracle Database.
   **
   ** @return                    the administrative operations providing granted
   **                            permissions on objects to accounts and/or roles
   **                            supported by an Oracle Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installPermission() {
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);

    mapping.put(DatabaseSchema.Catalog.Privilege, DatabaseCatalog.build(this.schema, "dba_sys_privs",  new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("privilege", "name")
    , DatabaseAttribute.build("admin_option", "delegated")
    )));
    mapping.put(DatabaseSchema.Catalog.Role,      DatabaseCatalog.build(this.schema, "dba_role_privs",  new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("granted_role", "name")
    , DatabaseAttribute.build("admin_option", "delegated")
    )));
    mapping.put(DatabaseSchema.Catalog.Synonym,   DatabaseCatalog.build(this.schema, "dba_tab_privs,sys.dba_objects",  new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("dba_tab_privs.owner", "owner")
    , DatabaseAttribute.build("table_name",          "tableName")
    , DatabaseAttribute.build("privilege",           "permission")
    , DatabaseAttribute.build("grantable",           "delegated")
    )
/*                                                                        
    , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("owner")),      DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("table_name")), DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_name")), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_type")), "SYNONYM", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
*/
    ));
    mapping.put(DatabaseSchema.Catalog.Sequence,  DatabaseCatalog.build(this.schema, "dba_tab_privs,"+ this.schema + ".dba_objects", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("dba_tab_privs.owner", "owner")
    , DatabaseAttribute.build("table_name",          "tableName")
    , DatabaseAttribute.build("privilege",           "permission")
    , DatabaseAttribute.build("grantable",           "delegated")
    )
/*
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("owner")),      DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("table_name")), DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_name")), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_type")), "SEQUENCE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
*/
      )
    );
    mapping.put(DatabaseSchema.Catalog.Table, DatabaseCatalog.build(this.schema, "dba_tab_privs,sys.dba_objects", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("dba_tab_privs.owner", "owner")
    , DatabaseAttribute.build("table_name",          "tableName")
    , DatabaseAttribute.build("privilege",           "permission")
    , DatabaseAttribute.build("grantable",           "delegated")
    )
/*
      DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("owner")),      DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("table_name")), DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_name")), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_type")), "TABLE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
*/
      )
    );
    mapping.put(DatabaseSchema.Catalog.View, DatabaseCatalog.build(this.schema, "dba_tab_privs,sys.dba_objects", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("dba_tab_privs.owner", "owner")
    , DatabaseAttribute.build("table_name",          "tableName")
    , DatabaseAttribute.build("privilege",           "permission")
    , DatabaseAttribute.build("grantable",           "delegated")
    )
/*
      DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("owner")),      DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("table_name")), DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_name")), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_type")), "VIEW", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
*/
      )
    );
    mapping.put(DatabaseSchema.Catalog.Type, DatabaseCatalog.build(this.schema, "dba_tab_privs,sys.dba_objects", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("dba_tab_privs.owner", "owner")
    , DatabaseAttribute.build("table_name",          "tableName")
    , DatabaseAttribute.build("privilege",           "permission")
    , DatabaseAttribute.build("grantable",           "delegated")
    )
/*
      DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("owner")),      DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("table_name")), DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_name")), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_type")), "TYPE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
*/
      )
    );
    mapping.put(DatabaseSchema.Catalog.Function, DatabaseCatalog.build(this.schema, "dba_tab_privs,sys.dba_objects", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("dba_tab_privs.owner", "owner")
    , DatabaseAttribute.build("table_name",          "tableName")
    , DatabaseAttribute.build("privilege",           "permission")
    , DatabaseAttribute.build("grantable",           "delegated")
    )
/*
      DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("owner")),      DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("table_name")), DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_name")), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_type")), "FUNCTION", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
*/
      )
    );
    mapping.put(DatabaseSchema.Catalog.Procedure, DatabaseCatalog.build(this.schema, "dba_tab_privs,sys.dba_objects", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("dba_tab_privs.owner", "owner")
    , DatabaseAttribute.build("table_name",          "tableName")
    , DatabaseAttribute.build("privilege",           "permission")
    , DatabaseAttribute.build("grantable",           "delegated")
    )
/*
      DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("owner")),      DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("table_name")), DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_name")), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_type")), "PROCEDURE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
*/
      )
    );
    mapping.put(DatabaseSchema.Catalog.Package, DatabaseCatalog.build(this.schema, "dba_tab_privs,sys.dba_objects", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("dba_tab_privs.owner", "owner")
    , DatabaseAttribute.build("table_name",          "tableName")
    , DatabaseAttribute.build("privilege",           "permission")
    , DatabaseAttribute.build("grantable",           "delegated")
    )
/*
      DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("owner")),      DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("table_name")), DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_name")), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
        )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_type")), "PACKAGE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
*/
      )
    );
    mapping.put(DatabaseSchema.Catalog.Java, DatabaseCatalog.build(this.schema, "dba_tab_privs,sys.dba_objects", new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("grantee")
    , DatabaseAttribute.build("dba_tab_privs.owner", "owner")
    , DatabaseAttribute.build("table_name",          "tableName")
    , DatabaseAttribute.build("privilege",           "permission")
    , DatabaseAttribute.build("grantable",           "delegated")
    )
/*
      DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("owner")),      DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("owner")),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", DatabaseAttribute.build("table_name")), DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_name")), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", DatabaseAttribute.build("object_type")), "JAVA CLASS", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
*/
      )
    );
    return mapping;
  }
}