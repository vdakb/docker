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

    File        :   DatabaseConstant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DatabaseConstant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

////////////////////////////////////////////////////////////////////////////////
// interface DatabaseConstant
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
public interface DatabaseConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default value of the separator sto specify multiple value for a
   ** configuration tag name.
   */
  static final String MULTIVALUE_SEPARATOR_DEFAULT = "|";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the character that separates multiple values for the same entry
   ** tag name.
   */
  static final String MULTIVALUE_SEPARATOR         = "multi-value-separator";

  /** Default value of the Oracle JDBC Driver class. */
  static final String DATABASE_DRIVER_ORACLE       = "oracle.jdbc.OracleDriver";

  /** Default value of the Microsoft SQL Server JDBC Driver class. */
  static final String DATABASE_DRIVER_SQLSERVER    = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

  /** Default value of the MySQL Server JDBC Driver class. */
  static final String DATABASE_DRIVER_MYSQL        = "com.mysql.jdbc.Driver";

  /** Default value of the Sybase Server JDBC Driver class. */
  static final String DATABASE_DRIVER_SYBASE       = "com.sybase.jdbc2.jdbc.SybDriver";

  /** Default value of the IBM Universal Database Server JDBC Driver class. */
  static final String DATABASE_DRIVER_IDMUDB       = "com.ibm.db2.jcc.DB2Driver";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify depending on the Database Server that you are using the JDBC
   ** driver class.
   ** <p>
   ** Enter one of the following values as the JDBC driver class name:
   ** <pre>
   **  +----------------------+----------------------------------------------+
   **  | Database Type        | Driver Class Name                            |
   **  +----------------------+----------------------------------------------+
   **  | Oracle Database      | oracle.jdbc.OracleDriver                     |
   **  |                      | oracle.jdbc.driver.OracleDriver              |
   **  | Microsoft SQL Server | com.microsoft.sqlserver.jdbc.SQLServerDriver |
   **  | MySQL                | com.mysql.jdbc.Driver                        |
   **  | Sybase               | com.sybase.jdbc2.jdbc.SybDriver              |
   **  |                      | com.sybase.jdbc3.jdbc.SybDriver              |
   **  | IBM DB2 UDB          | com.ibm.db2.jcc.DB2Driver                    |
   **  +----------------------+----------------------------------------------+
   ** </pre>
   */
  static final String DATABASE_DRIVER_CLASS        = "database-driver-class";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the database statement to obtain the current timestamp from the
   ** database.
   */
  static final String DATABASE_SYSTEM_TIMESTAMP    = "database-system-timestamp";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify that the JDBC Auto Commit feature needs stay on enabled if an
   ** connection is auqired from the pool.
   ** <p>
   ** This feature is only necessary to support DB2. This stupid database still
   ** needs a commit after each select to remove the read locks from the page.
   */
  static final String ENFORCE_AUTO_COMMIT          = "enforce-auto-commit";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the pseudo column used by paged resultsets.
   */
  static final String ROW_NUMBER_ATTRIBUTE         = "pseudo-rownum-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the create timestamp attribute of an entry in a
   ** database.
   */
  static final String ENTRY_CREATED_ATTRIBUTE      = "entry-created-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the modified timestamp attribute of an entry in a
   ** database.
   */
  static final String ENTRY_MODIFIED_ATTRIBUTE     = "entry-modified-attribute";

  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify if the entitlement loaded from a Database Server needs to be
   ** prefixed with the internal system identifier and/or the name of the
   ** <code>IT Resource</code>.
   */
  static final String ENTITLEMENT_PREFIX_REQUIRED  = "entitlement-prefix-required";
}