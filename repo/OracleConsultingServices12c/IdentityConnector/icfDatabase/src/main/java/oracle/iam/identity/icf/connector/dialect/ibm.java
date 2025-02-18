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

    File        :   ibm.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ibm.


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
// class ibm
// ~~~~~ ~~~
/**
 ** The dictionary dialect to managed accounts in a IBM UDB Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ibm extends DatabaseDialect {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Dialect</code> for a IBM Universal Database
   ** that referes to entities that belongs to the specified schema.
   */
  public ibm() {
    // ensure inheritance
    super("syscat");
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
   ** Creates the administrative entities supported by a IBM UDB Database.
   **
   ** @return                    the administrative entities supported by a
   **                            IBM UDB Database.
   */
  @Override
  protected final Map<DatabaseSchema.Entity, DatabaseEntity> installEntity() {
    // the administrative entity catalog supported by a IBM UDB Database.
    final Map<DatabaseSchema.Entity, DatabaseEntity> mapping = new EnumMap<DatabaseSchema.Entity, DatabaseEntity>(DatabaseSchema.Entity.class);

    mapping.put(DatabaseSchema.Entity.Account, null);

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installCatalog (DatabaseDialect)
  /**
   ** Creates the administrative catalog supported by a IBM UDB Database.
   **
   ** @return                    the administrative catalog supported by a
   **                            IBM UDB Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installCatalog() {
    // the system catalog provided by a IBM UDB Database
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);

    mapping.put(DatabaseSchema.Catalog.Privilege,           null);
    mapping.put(DatabaseSchema.Catalog.Role,                DatabaseCatalog.build(this.schema, "roles",       DatabaseAttribute.build("rolename")));
    mapping.put(DatabaseSchema.Catalog.Profile,             null);
    mapping.put(DatabaseSchema.Catalog.Synonym,             DatabaseCatalog.build(this.schema, "tables",      new int[]{0, 1}, CollectionUtility.list(
      DatabaseAttribute.build("tabschema")
    , DatabaseAttribute.build("tabname")
    )
    , DatabaseFilter.build("type", "A", DatabaseFilter.Operator.EQUAL)));
    mapping.put(DatabaseSchema.Catalog.Sequence,            DatabaseCatalog.build(this.schema, "sequences",   new int[]{0, 1}, CollectionUtility.list(
      DatabaseAttribute.build("seqname")
    , DatabaseAttribute.build("seqschema")
    )));
    mapping.put(DatabaseSchema.Catalog.Table,               DatabaseCatalog.build(this.schema, "tables",      new int[]{0, 1}, CollectionUtility.list(
      DatabaseAttribute.build("tabschema")
    , DatabaseAttribute.build("tabname")
    )
    , DatabaseFilter.build("type", "T", DatabaseFilter.Operator.EQUAL)));
    mapping.put(DatabaseSchema.Catalog.View,                DatabaseCatalog.build(this.schema, "tables",      new int[]{0, 1}, CollectionUtility.list(
      DatabaseAttribute.build("tabschema")
    , DatabaseAttribute.build("tabname")
    )
    , DatabaseFilter.build("type", "V", DatabaseFilter.Operator.EQUAL)));
    mapping.put(DatabaseSchema.Catalog.Function,            DatabaseCatalog.build(this.schema, "routines",      new int[]{0, 1}, CollectionUtility.list(
      DatabaseAttribute.build("routineschema")
    , DatabaseAttribute.build("routinename")
    )
    , DatabaseFilter.build("routinetype", "F", DatabaseFilter.Operator.EQUAL)));
    mapping.put(DatabaseSchema.Catalog.Procedure,           DatabaseCatalog.build(this.schema, "routines",      new int[]{0, 1}, CollectionUtility.list(
      DatabaseAttribute.build("routineschema")
    , DatabaseAttribute.build("routinename")
    )
    , DatabaseFilter.build("routinetype", "P", DatabaseFilter.Operator.EQUAL)));
    mapping.put(DatabaseSchema.Catalog.TablespacePermanent, DatabaseCatalog.build(this.schema, "tablespaces",   new int[]{0}, CollectionUtility.list(
      DatabaseAttribute.build("tbspace")
    )
    , DatabaseFilter.build("datatype", "L", DatabaseFilter.Operator.EQUAL)));
    mapping.put(DatabaseSchema.Catalog.TablespaceTemporary, DatabaseCatalog.build(this.schema, "tablespaces", DatabaseAttribute.build("tbspace")
    ,     DatabaseFilter.build(DatabaseFilter.build("datatype", "T", DatabaseFilter.Operator.EQUAL), DatabaseFilter.build("datatype", "U", DatabaseFilter.Operator.EQUAL),DatabaseFilter.Operator.AND)));

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation (DatabaseDialect)
  /**
   ** Creates the administrative operations applicable on accounts and/or roles
   ** supported by a IBM UDB Database.
   **
   ** @return                    the administrative operations applicable on
   **                            accounts and/or roles supported by a IBM UDB
   **                            Database.
   */
  @Override
  protected final Map<Operation, String> installOperation() {
    // the administrative operations applicable on accounts and/or roles
    // supported by a IBM UDB Database.
    final Map<Operation, String> mapping = new EnumMap<Operation, String>(Operation.class);

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installPermission (DatabaseDialect)
  /**
   ** Creates the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by a IBM UDB Database.
   **
   ** @return                    the administrative operations providing granted
   **                            permissions on objects to accounts and/or roles
   **                            supported by a IBM UDB Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installPermission() {
    // the administrative operations providing granted permissions on objects
    // to accounts and/or roles supported by a IBM UDB Database.
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);

    return mapping;
  }
}