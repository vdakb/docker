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

    File        :   msft.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    msft.


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

import oracle.iam.identity.icf.connector.DatabaseSchema;
import oracle.iam.identity.icf.connector.DatabaseCatalog;
import oracle.iam.identity.icf.connector.DatabaseDialect;

////////////////////////////////////////////////////////////////////////////////
// class msft
// ~~~~~ ~~~~
/**
 ** The dictionary dialect to managed accounts in a SQL Server Database.
 */
public class msft extends DatabaseDialect {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Dialect</code> for a Microsoft SQLServer Database
   ** that referes to entities that belongs to the specified schema.
   */
  public msft() {
    // ensure inheritance
    super("????");
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
   ** Creates the administrative entities supported by a SQLServer Database.
   **
   ** @return                    the administrative entities supported by a
   **                            SQLServer Database.
   */
  @Override
  protected final Map<DatabaseSchema.Entity, DatabaseEntity> installEntity() {
    // the administrative entity catalog supported by a SQLServer Database.
    final Map<DatabaseSchema.Entity, DatabaseEntity> mapping = new EnumMap<DatabaseSchema.Entity, DatabaseEntity>(DatabaseSchema.Entity.class);

    mapping.put(DatabaseSchema.Entity.Account,   null);

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installCatalog (DatabaseDialect)
  /**
   ** Creates the administrative catalog supported by a SQLServer Database.
   **
   ** @return                    the administrative catalog supported by a
   **                            SQLServer Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installCatalog() {
    // the system catalog provided by a SQLServer Database
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);

    mapping.put(DatabaseSchema.Catalog.Privilege,           null);
    mapping.put(DatabaseSchema.Catalog.Role,                null);
    mapping.put(DatabaseSchema.Catalog.Profile,             null);
    mapping.put(DatabaseSchema.Catalog.Schema,              null);
    mapping.put(DatabaseSchema.Catalog.Sequence,            null);
    mapping.put(DatabaseSchema.Catalog.Synonym,             null);
    mapping.put(DatabaseSchema.Catalog.Table,               null);
    mapping.put(DatabaseSchema.Catalog.View,                null);
    mapping.put(DatabaseSchema.Catalog.Function,            null);
    mapping.put(DatabaseSchema.Catalog.Procedure,           null);
    mapping.put(DatabaseSchema.Catalog.Package,             null);
    mapping.put(DatabaseSchema.Catalog.Java,                null);
    mapping.put(DatabaseSchema.Catalog.DOTNET,              null);
    mapping.put(DatabaseSchema.Catalog.TablespacePermanent, null);
    mapping.put(DatabaseSchema.Catalog.TablespaceTemporary, null);

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation (DatabaseDialect)
  /**
   ** Creates the administrative operations applicable on accounts and/or roles
   ** supported by a SQLServer Database.
   **
   ** @return                    the administrative operations applicable on
   **                            accounts and/or roles supported by a SQLServer
   **                            Database.
   */
  @Override
  protected final Map<Operation, String> installOperation() {
    // the administrative operations applicable on accounts and/or roles
    // supported by a SQLServer Database.
    final Map<Operation, String> mapping = new EnumMap<Operation, String>(Operation.class);

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installPermission (DatabaseDialect)
  /**
   ** Creates the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by a SQLServer Database.
   **
   ** @return                    the administrative operations providing granted
   **                            permissions on objects to accounts and/or roles
   **                            supported by a SQLServer Database.
   */
  @Override
  protected final Map<DatabaseSchema.Catalog, DatabaseCatalog> installPermission() {
    // the administrative operations providing granted permissions on objects
    // to accounts and/or roles supported by a SQLServer Database.
    final Map<DatabaseSchema.Catalog, DatabaseCatalog> mapping = new EnumMap<DatabaseSchema.Catalog, DatabaseCatalog>(DatabaseSchema.Catalog.class);

    return mapping;
  }
}