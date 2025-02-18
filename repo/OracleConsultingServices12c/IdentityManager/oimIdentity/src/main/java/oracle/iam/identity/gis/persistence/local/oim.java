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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Identity Manager Connector

    File        :   oim.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    oim.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.persistence.local;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;

import oracle.iam.identity.foundation.persistence.DatabaseError;
import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseException;

import oracle.iam.identity.gis.persistence.Dialect;

////////////////////////////////////////////////////////////////////////////////
// class oim
// ~~~~~ ~~~
/**
 ** The dictionary dialect to managed accounts in an Oracle Identity Manager
 ** locally.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class oim extends Dialect {

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
   ** Constructs a <code>Dialect</code> for an Oracle Identity Manager
   ** Repository that referes to entities that belongs to the specified schema.
   */
  public oim() {
    // ensure inheritance
    super("sys");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

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

    mapping.put(Entity.ROLEMEMBERSHIP, DatabaseEntity.build(this.schema, SystemPermissionMember.ENTITY, SystemPermissionMember.PRIMARY));

    return mapping;
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
    ERROR.put("SQL-00904", DatabaseError.SYNTAX_INVALID);
    ERROR.put("SQL-00923", DatabaseError.SYNTAX_INVALID);
    ERROR.put("SQL-01013", DatabaseError.ABORT);
    ERROR.put("SQL-01017", DatabaseError.CONNECTION_AUTHENTICATION);
    ERROR.put("SQL-01031", DatabaseError.CONNECTION_PERMISSION);
    ERROR.put("SQL-01400", DatabaseError.INSUFFICIENT_INFORMATION);
    ERROR.put("SQL-01917", DatabaseError.OBJECT_NOT_EXISTS);
    ERROR.put("SQL-01918", DatabaseError.OBJECT_NOT_EXISTS);
    ERROR.put("SQL-01919", DatabaseError.OBJECT_NOT_EXISTS);
    ERROR.put("SQL-01920", DatabaseError.OBJECT_ALREADY_EXISTS);
  }
}