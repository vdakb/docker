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

    File        :   orcl.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    orcl.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.persistence.dialect;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.EnumMap;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.persistence.DatabaseError;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseException;

import oracle.iam.identity.dbs.persistence.Catalog;
import oracle.iam.identity.dbs.persistence.Dialect;

////////////////////////////////////////////////////////////////////////////////
// class orcl
// ~~~~~ ~~~~
/**
 ** The dictionary dialect to managed accounts in an Oracle Database.
 */
public class orcl extends Dialect {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the account state we are interested on */
  private static final DatabaseFilter             ACCOUNT_FILTER     = DatabaseFilter.build("account_status", "OPEN", DatabaseFilter.Operator.EQUAL);

  /** the account state we are interested on */
  private static final List<Pair<String, String>> CATALOG_PERMISSION = CollectionUtility.list(
    Pair.of("dba_tab_privs.owner", "owner")
  , Pair.of("table_name",          "name")
  , Pair.of("privilege",           "permission")
  , Pair.of("grantable",           "delegated")
  );

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the error translation of an Oracle Database to our implementation*/
  private static Map<String, String>   ERROR          = null;

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
  // Method:   accountFilter (Dialect)
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
  // Method:   accountTime (Dialect)
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
  // Method:   normalizeError (Dialect)
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
  // Method:   installEntity (Dialect)
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

    mapping.put(Entity.ACCOUNT, DatabaseEntity.build(this.schema, "dba_users", "username"));

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installCatalog (Dialect)
  /**
   ** Creates the administrative entity catalog supported by an Oracle Database.
   **
   ** @return                    the administrative catalog supported by an
   **                            Oracle Database.
   */
  @Override
  protected final Map<Catalog.Type, Catalog> installCatalog() {
    // the system catalog provided by an Oracle Database
    final Map<Catalog.Type, Catalog> mapping = new EnumMap<Catalog.Type, Catalog>(Catalog.Type.class);

    mapping.put(Catalog.Type.Privilege,           Catalog.build(this.schema, "system_privilege_map", "name"));
    mapping.put(Catalog.Type.Role,                Catalog.build(this.schema, "dba_roles",            "role"));
    mapping.put(Catalog.Type.Profile,             Catalog.build(this.schema, "dba_profiles",         "profile", DatabaseFilter.NOP, CollectionUtility.list(Pair.of("distinct profile", "profile"))));
    mapping.put(Catalog.Type.Schema,              Catalog.build(this.schema, "dba_users",            "username",    DatabaseFilter.build("username", DatabaseEntity.build(this.schema, "dba_objects","owner"), DatabaseFilter.Operator.IN)));
    mapping.put(Catalog.Type.Synonym,             Catalog.build(this.schema, "dba_objects",          "object_type", "SYNONYM",    CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.Sequence,            Catalog.build(this.schema, "dba_objects",          "object_type", "SEQUENCE",   CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.Table,               Catalog.build(this.schema, "dba_objects",          "object_type", "TABLE",      CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.View,                Catalog.build(this.schema, "dba_objects",          "object_type", "VIEW",       CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.Type,                Catalog.build(this.schema, "dba_objects",          "object_type", "TYPE",       CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.Function,            Catalog.build(this.schema, "dba_objects",          "object_type", "FUNCTION",   CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.Procedure,           Catalog.build(this.schema, "dba_objects",          "object_type", "PROCEDURE",  CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.Package,             Catalog.build(this.schema, "dba_objects",          "object_type", "PACKAGE",    CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.JavaClass,           Catalog.build(this.schema, "dba_objects",          "object_type", "JAVA CLASS", CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.DOTNET,              Catalog.build(this.schema, "dba_objects",          "object_type", "DOTNET",     CollectionUtility.set("owner", "object_name")));
    mapping.put(Catalog.Type.TablespacePermanent, Catalog.build(this.schema, "dba_tablespaces",      "contents",    "PERMANENT",  CollectionUtility.set("tablespace_name")));
    mapping.put(Catalog.Type.TablespaceTemporary, Catalog.build(this.schema, "dba_tablespaces",      "contents",    "TEMPORARY",  CollectionUtility.set("tablespace_name")));

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation (Dialect)
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
  // Method:   installPermission (Dialect)
  /**
   ** Creates the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by an Oracle Database.
   **
   ** @return                    the administrative operations providing granted
   **                            permissions on objects to accounts and/or roles
   **                            supported by an Oracle Database.
   */
  @Override
  protected final Map<Catalog.Type, Catalog> installPermission() {
    final Map<Catalog.Type, Catalog> mapping = new EnumMap<Catalog.Type, Catalog>(Catalog.Type.class);

    mapping.put(Catalog.Type.Privilege, Catalog.build(this.schema, "dba_sys_privs",  "grantee", DatabaseFilter.NOP, CollectionUtility.list(Pair.of("privilege", "name"), Pair.of("admin_option", "delegated"))));
    mapping.put(Catalog.Type.Role,      Catalog.build(this.schema, "dba_role_privs", "grantee", DatabaseFilter.NOP, CollectionUtility.list(Pair.of("granted_role", "name"), Pair.of("admin_option", "delegated"))));
    mapping.put(Catalog.Type.Synonym,   Catalog.build(this.schema, "dba_tab_privs,sys.dba_objects", "grantee"
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "owner"),      DatabaseEntity.build(this.schema, "dba_objects", "owner"),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "table_name"), DatabaseEntity.build(this.schema, "dba_objects", "object_name"), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", "object_type"), "SYNONYM", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
      , CATALOG_PERMISSION
      )
    );
    mapping.put(Catalog.Type.Sequence,  Catalog.build(this.schema, "dba_tab_privs,"+ this.schema + ".dba_objects", "grantee"
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "owner"),      DatabaseEntity.build(this.schema, "dba_objects", "owner"),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "table_name"), DatabaseEntity.build(this.schema, "dba_objects", "object_name"), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", "object_type"), "SEQUENCE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
      , CATALOG_PERMISSION
      )
    );
    mapping.put(Catalog.Type.Table, Catalog.build(this.schema, "dba_tab_privs,sys.dba_objects", "grantee"
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "owner"),      DatabaseEntity.build(this.schema, "dba_objects", "owner"),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "table_name"), DatabaseEntity.build(this.schema, "dba_objects", "object_name"), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", "object_type"), "TABLE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
      , CATALOG_PERMISSION
      )
    );
    mapping.put(Catalog.Type.View, Catalog.build(this.schema, "dba_tab_privs,sys.dba_objects", "grantee"
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "owner"),      DatabaseEntity.build(this.schema, "dba_objects", "owner"),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "table_name"), DatabaseEntity.build(this.schema, "dba_objects", "object_name"), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", "object_type"), "VIEW", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
      , CATALOG_PERMISSION
      )
    );
    mapping.put(Catalog.Type.Type, Catalog.build(this.schema, "dba_tab_privs,sys.dba_objects", "grantee"
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "owner"),      DatabaseEntity.build(this.schema, "dba_objects", "owner"),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "table_name"), DatabaseEntity.build(this.schema, "dba_objects", "object_name"), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", "object_type"), "TYPE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
      , CATALOG_PERMISSION
      )
    );
    mapping.put(Catalog.Type.Function, Catalog.build(this.schema, "dba_tab_privs,sys.dba_objects", "grantee"
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "owner"),      DatabaseEntity.build(this.schema, "dba_objects", "owner"),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "table_name"), DatabaseEntity.build(this.schema, "dba_objects", "object_name"), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", "object_type"), "FUNCTION", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
      , CATALOG_PERMISSION
      )
    );
    mapping.put(Catalog.Type.Procedure, Catalog.build(this.schema, "dba_tab_privs,sys.dba_objects", "grantee"
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "owner"),      DatabaseEntity.build(this.schema, "dba_objects", "owner"),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "table_name"), DatabaseEntity.build(this.schema, "dba_objects", "object_name"), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", "object_type"), "PROCEDURE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
      , CATALOG_PERMISSION
      )
    );
    mapping.put(Catalog.Type.Package, Catalog.build(this.schema, "dba_tab_privs,sys.dba_objects", "grantee"
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "owner"),      DatabaseEntity.build(this.schema, "dba_objects", "owner"),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "table_name"), DatabaseEntity.build(this.schema, "dba_objects", "object_name"), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
        )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", "object_type"), "PACKAGE", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
      , CATALOG_PERMISSION
      )
    );
    mapping.put(Catalog.Type.JavaClass, Catalog.build(this.schema, "dba_tab_privs,sys.dba_objects", "grantee"
      , DatabaseFilter.build(
          DatabaseFilter.build(
            DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "owner"),      DatabaseEntity.build(this.schema, "dba_objects", "owner"),       DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_tab_privs", "table_name"), DatabaseEntity.build(this.schema, "dba_objects", "object_name"), DatabaseFilter.Operator.EQUAL)
          , DatabaseFilter.Operator.AND
          )
        , DatabaseFilter.build(DatabaseEntity.build(this.schema, "dba_objects", "object_type"), "JAVA CLASS", DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
      )
      , CATALOG_PERMISSION
      )
    );
    return mapping;
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