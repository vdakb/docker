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

    File        :   msql.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    msql.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.persistence.dialect;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.EnumMap;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.persistence.DatabaseError;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseException;

import oracle.iam.identity.dbs.persistence.Catalog;
import oracle.iam.identity.dbs.persistence.Dialect;

////////////////////////////////////////////////////////////////////////////////
// class msft
// ~~~~~ ~~~~
/**
 ** The dictionary dialect to managed accounts in a MySQL Database.
 */
public class msql extends Dialect {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the error translation of MySQL Database to our implementation*/
  private static Map<String, String> ERROR = null;

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
    return DatabaseFilter.NOP;
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
        installErrorTranslation();
      return ERROR.get(code);
    }
    else
      return DatabaseError.UNHANDLED;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installEntity (Dialect)
  /**
   ** Creates the administrative entity catalog supported by a MySQL Database.
   **
   ** @return                    the administrative entities supported by a
   **                            MySQL Database.
   */
  @Override
  protected final Map<Entity, DatabaseEntity> installEntity() {
    // the administrative entity catalog supported by a MySQL Database.
    final Map<Entity, DatabaseEntity> mapping = new EnumMap<Entity, DatabaseEntity>(Entity.class);

    mapping.put(Entity.ACCOUNT, DatabaseEntity.build(this.schema, "user", "user"));

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installCatalog (Dialect)
  /**
   ** Creates the administrative entity catalog supported by a MySQL Database.
   **
   ** @return                    the administrative catalog supported by a
   **                            MySQL Database.
   */
  @Override
  protected final Map<Catalog.Type, Catalog> installCatalog() {
    // the system catalog provided by a MySQL Database
    final Map<Catalog.Type, Catalog> mapping = new EnumMap<Catalog.Type, Catalog>(Catalog.Type.class);

    mapping.put(Catalog.Type.Schema,    Catalog.build(this.schema, "information_schema.schemata", "schema_name",  DatabaseFilter.NOP));
    mapping.put(Catalog.Type.Table,     Catalog.build(this.schema, "information_schema.tables",   "table_name",   "table_type",   "BASE TABLE"));
    mapping.put(Catalog.Type.View,      Catalog.build(this.schema, "information_schema.views",    "table_name",   DatabaseFilter.NOP));
    mapping.put(Catalog.Type.Function,  Catalog.build(this.schema, "information_schema.routines", "routine_name", "routine_type", "???????????"));
    mapping.put(Catalog.Type.Procedure, Catalog.build(this.schema, "information_schema.routines", "routine_name", "routine_type", "???????????"));

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation (Dialect)
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
  // Method:   installPermission (Dialect)
  /**
   ** Creates the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by a MySQL Database.
   **
   ** @return                    the administrative operations providing granted
   **                            permissions on objects to accounts and/or roles
   **                            supported by a MySQL Database.
   */
  @Override
  protected final Map<Catalog.Type, Catalog> installPermission() {
    // the administrative operations providing granted permissions on objects
    // to accounts and/or roles supported by a MySQL Database.
    final Map<Catalog.Type, Catalog> mapping = new EnumMap<Catalog.Type, Catalog>(Catalog.Type.class);

    mapping.put(Catalog.Type.Schema,    Catalog.build(this.schema, "information_schema.schema_privileges", "grantee", CollectionUtility.set("privilege_type granted", "is_grantable")));
    mapping.put(Catalog.Type.Privilege, Catalog.build(this.schema, "information_schema.user_privileges",   "grantee", CollectionUtility.set("privilege_type granted", "is_grantable")));

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installErrorTranslation
  /**
   ** Creates the error mapping to translate vendor specific error codes thrown
   ** by a JDBC Driver to our implementation.
   */
  private static void installErrorTranslation() {
    ERROR = new HashMap<String, String>();
    ERROR.put("SQL-01044", DatabaseError.CONNECTION_PERMISSION);
    ERROR.put("SQL-01045", DatabaseError.INSUFFICIENT_PRIVILEGE);
//    ERROR.put("SQL-01064", DatabaseError.INVALID_SYNTAX);
    ERROR.put("SQL-01141", DatabaseError.OBJECT_NOT_EXISTS);
    ERROR.put("SQL-01146", DatabaseError.OBJECT_NOT_EXISTS);
    ERROR.put("SQL-01396", DatabaseError.OPERATION_FAILED);
  }
}