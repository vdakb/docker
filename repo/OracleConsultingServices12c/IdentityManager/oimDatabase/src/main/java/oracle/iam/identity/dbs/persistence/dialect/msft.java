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

    File        :   msft.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    msft.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.persistence.dialect;

import java.util.Date;
import java.util.Map;
import java.util.EnumMap;
import java.util.HashMap;

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
 ** The dictionary dialect to managed accounts in a SQL Server Database.
 */
public class msft extends Dialect {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the error translation of SQLServer Database to our implementation*/
  private static Map<String, String> ERROR = null;

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
    return DatabaseFilter.NOP;
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
   ** Creates the administrative entities supported by a SQLServer Database.
   **
   ** @return                    the administrative entities supported by a
   **                            SQLServer Database.
   */
  @Override
  protected final Map<Entity, DatabaseEntity> installEntity() {
    // the administrative entity catalog supported by a SQLServer Database.
    final Map<Entity, DatabaseEntity> mapping = new EnumMap<Entity, DatabaseEntity>(Entity.class);

    mapping.put(Entity.ACCOUNT,   null);

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installCatalog (Dialect)
  /**
   ** Creates the administrative catalog supported by a SQLServer Database.
   **
   ** @return                    the administrative catalog supported by a
   **                            SQLServer Database.
   */
  @Override
  protected final Map<Catalog.Type, Catalog> installCatalog() {
    // the system catalog provided by a SQLServer Database
    final Map<Catalog.Type, Catalog> mapping = new EnumMap<Catalog.Type, Catalog>(Catalog.Type.class);

    mapping.put(Catalog.Type.Privilege,           null);
    mapping.put(Catalog.Type.Role,                null);
    mapping.put(Catalog.Type.Profile,             null);
    mapping.put(Catalog.Type.Schema,              null);
    mapping.put(Catalog.Type.Sequence,            null);
    mapping.put(Catalog.Type.Synonym,             null);
    mapping.put(Catalog.Type.Table,               null);
    mapping.put(Catalog.Type.View,                null);
    mapping.put(Catalog.Type.Function,            null);
    mapping.put(Catalog.Type.Procedure,           null);
    mapping.put(Catalog.Type.Package,             null);
    mapping.put(Catalog.Type.JavaClass,           null);
    mapping.put(Catalog.Type.DOTNET,              null);
    mapping.put(Catalog.Type.TablespacePermanent, null);
    mapping.put(Catalog.Type.TablespaceTemporary, null);

    return mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation (Dialect)
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
  // Method:   installPermission (Dialect)
  /**
   ** Creates the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by a SQLServer Database.
   **
   ** @return                    the administrative operations providing granted
   **                            permissions on objects to accounts and/or roles
   **                            supported by a SQLServer Database.
   */
  @Override
  protected final Map<Catalog.Type, Catalog> installPermission() {
    // the administrative operations providing granted permissions on objects
    // to accounts and/or roles supported by a SQLServer Database.
    final Map<Catalog.Type, Catalog> mapping = new EnumMap<Catalog.Type, Catalog>(Catalog.Type.class);

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
    ERROR.put("SQL-00000", "DBA-?????");
  }
}