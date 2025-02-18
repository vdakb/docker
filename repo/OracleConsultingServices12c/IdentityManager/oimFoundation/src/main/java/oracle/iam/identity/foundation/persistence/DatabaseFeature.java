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

    File        :   DatabaseFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLookup;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseFeature
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseFeature</code> provides the base feature description of
 ** a Database.
 ** <br>
 ** Implementation of a database may vary in locations of certain informations
 ** and object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class DatabaseFeature extends AbstractLookup {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseFeature</code> which is associated with
   ** the specified task.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   */
  public DatabaseFeature(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseFeature</code> which is associated with the
   ** specified task and belongs to the Metadata Descriptor specified by the
   ** given name.
   ** <br>
   ** The Metadata Descriptor will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   ** @param  instanceName       the name of the <code>Metadata Descriptor</code>
   **                            instance where this configuration wrapper will
   **                            obtains the values.
   **
   ** @throws TaskException      if the <code>Metadata Descriptor</code> is not
   **                            defined in the Oracle Identity Manager metadata
   **                            entries.
   */
  public DatabaseFeature(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator character for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link DatabaseConstant#MULTIVALUE_SEPARATOR} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DatabaseConstant#MULTIVALUE_SEPARATOR_DEFAULT}
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String multiValueSeparator() {
    return stringValue(DatabaseConstant.MULTIVALUE_SEPARATOR, DatabaseConstant.MULTIVALUE_SEPARATOR_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseDriver
  /**
   ** Returns depending on the Database Server that you are using the JDBC
   ** driver class.
   ** <p>
   ** Enter one of the following values as the JDBC driver class name:
   ** <pre>
   **  +----------------------+----------------------------------------------+
   **  | Database Type        | Driver Class Name                            |
   **  +----------------------+----------------------------------------------+
   **  | Oracle Database      | oracle.jdbc.driver.OracleDriver              |
   **  | Microsoft SQL Server | com.microsoft.sqlserver.jdbc.SQLServerDriver |
   **  | MySQL                | com.mysql.jdbc.Driver                        |
   **  | IBM DB2 UDB          | com.ibm.db2.jcc.DB2Driver                    |
   **  | Sybase               | com.sybase.jdbc2.jdbc.SybDriver              |
   **  +----------------------+----------------------------------------------+
   ** </pre>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#DATABASE_DRIVER_CLASS}.
   ** <p>
   ** If {@link DatabaseConstant#DATABASE_DRIVER_CLASS} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DatabaseConstant#DATABASE_DRIVER_ORACLE}
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String databaseDriverClass() {
    return stringValue(DatabaseConstant.DATABASE_DRIVER_CLASS, DatabaseConstant.DATABASE_DRIVER_ORACLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTimeStatement
  /**
   ** Returns the database statement to obtain the current timestamp from the
   ** database.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#DATABASE_SYSTEM_TIMESTAMP}.
   **
   ** @return                    the database statement to obtain the current
   **                            timestamp from the database.
   */
  public final String systemTimeStatement() {
    return stringValue(DatabaseConstant.DATABASE_SYSTEM_TIMESTAMP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enforceAutoCommit
  /**
   ** Returns <code>true</code> the JDBC Auto Commit feature needs stay on
   ** enabled after a connection was auqired from the pool.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ENFORCE_AUTO_COMMIT}.
   **
   ** @return                    <code>true</code> the JDBC Auto Commit feature
   **                            needs stay on enabled after a connection was
   **                            aquired from the pool.
   */
  public final boolean enforceAutoCommit() {
    return booleanValue(DatabaseConstant.ENFORCE_AUTO_COMMIT, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rowNumberAttribute
  /**
   ** Returns the name of the pseudo attribute used in paged result sets.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ROW_NUMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the pseudo attribute used in paged
   **                            result sets.
   */
  public final String rowNumberAttribute() {
    return stringValue(DatabaseConstant.ROW_NUMBER_ATTRIBUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatedAttribute
  /**
   ** Returns the name of the attribute to detect the created timestamp of a
   ** database entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ENTRY_CREATED_ATTRIBUTE}.
   **
   ** @return                    the name of the attribute to detect the
   **                            created timestamp.
   */
  public final String entryCreatedAttribute() {
    return stringValue(DatabaseConstant.ENTRY_CREATED_ATTRIBUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifiedAttribute
  /**
   ** Returns the name of the attribute to detect the modified timestamp of a
   ** database entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ENTRY_MODIFIED_ATTRIBUTE}.
   **
   ** @return                    the name of the attribute to detect the
   **                            modified timestamp.
   */
  public final String entryModifiedAttribute() {
    return stringValue(DatabaseConstant.ENTRY_MODIFIED_ATTRIBUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementPrefixRequired
  /**
   ** Returns the <code>true</code> if the entitlements has to be prefixed with
   ** the internal system identifier and the name of the
   ** <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ENTITLEMENT_PREFIX_REQUIRED}.
   **
   ** @return                    <code>true</code> the entitlements has to be
   **                            prefixed with the internal system identifier
   **                            and the name of the <code>IT Resource</code>;
   **                            otherwise <code>false</code>.
   */
  public final boolean entitlementPrefixRequired() {
    return booleanValue(DatabaseConstant.ENTITLEMENT_PREFIX_REQUIRED);
  }
}