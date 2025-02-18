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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   Database.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Database.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.integration.openfire;

import java.util.Set;
import java.util.Map;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// interface Database
// ~~~~~~~~~ ~~~~~~~~
public interface Database {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default value of the separator to specify multiple value for a
   ** configuration tag name.
   */
  static final String MULTIVALUE_SEPARATOR_DEFAULT    = "|";

  /** Default value of the Oracle Type 3 JDBC Driver class. */
  static final String DATABASE_DRIVER_ORACLE3         = "oracle.jdbc.driver.OracleDriver";

  /** Default value of the Oracle Type 4 JDBC Driver class. */
  static final String DATABASE_DRIVER_ORACLE4         = "oracle.jdbc.OracleDriver";

  /** Default value of the Microsoft SQL Server JDBC Driver class. */
  static final String DATABASE_DRIVER_SQLSERVER       = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

  /** Default value of the MySQL Server JDBC Driver class. */
  static final String DATABASE_DRIVER_MYSQL           = "com.mysql.jdbc.Driver";

  /** Default value of the Sybase Server Type 2 JDBC Driver class. */
  static final String DATABASE_DRIVER_SYBASE2         = "com.sybase.jdbc2.jdbc.SybDriver";

  /** Default value of the Sybase Server Type 2 JDBC Driver class. */
  static final String DATABASE_DRIVER_SYBASE3         = "com.sybase.jdbc3.jdbc.SybDriver";

  /** Default value of the IBM Universal Database Server JDBC Driver class. */
  static final String DATABASE_DRIVER_IDMUDB          = "com.ibm.db2.jcc.DB2Driver";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface ICF
  // ~~~~~~~~~ ~~~
  interface ICF {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // interface Resource
    // ~~~~~~~~~ ~~~~~~~~
    public interface Resource {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the host name for the target system database.
       */
      static final String SERVER_HOST                 = "serverHost";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the listener port for the target system database.
       */
      static final String SERVER_PORT                 = "serverPort";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify depending on the Database Server that you are using the JDBC
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
      static final String DATABASE_DRIVER             = "databaseDriver";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the name of the database as specified in the
       ** {@link #SERVER_HOST} and {@link #SERVER_PORT} parameter.
       */
      static final String DATABASE_NAME               = "databaseName";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the cataloge schema name to be used.
       */
      static final String DATABASE_SCHEMA             = "databaseSchema";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the password of the target system account specified by the
       ** #PRINCIPAL_NAME parameter.
       */
      static final String SERVICE_NAME                = "serviceName";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the user name of the target system account to be used to
       ** establish a connection.
       */
      static final String PRINCIPAL_NAME              = "principalUsername";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify the password of the target system account specified by the
       ** #PRINCIPAL_NAME parameter.
       */
      static final String PRINCIPAL_PASSWORD          = "principalPassword";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify if you plan to configure SSL to secure communication between
       ** Identity Manager and the target system.
       */
      static final String SECURE_SOCKET               = "secureSocket";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of language the server is using.
       */
      static final String LOCALE_LANGUAGE             = "language";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of language region the server is using.
       */
      static final String LOCALE_COUNTRY              = "country";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** hold the name of time zone the server is using.
       */
      static final String LOCALE_TIMEZONE             = "timeZone";

      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the time (in milliseconds) within which the target system is
       ** expected to respond to a connection attempt.
       */
      static final String CONNECTION_TIMEOUT          = "connectionTimeOut";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the number of consecutive attempts to be made at establishing
       ** a connection with the target system.
       */
      static final String CONNECTION_RETRY_COUNT      = "connectionRetryCount";
      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specifiy the interval (in milliseconds) between consecutive attempts
       ** at establishing a connection with the target system.
       */
      static final String CONNECTION_RETRY_INTERVAL   = "connectionRetryInterval";
      /**
       ** Attribute tag which may be defined on a <code>IT Resource</code>
       ** to specify the timeout period the <code>Service Provider</code>
       ** consumer doesn't get a HTTP response.
       ** <p>
       ** If this property has not been specified, the default is to wait for the
       ** response until it is received.
       */
      static final String RESPONSE_TIMEOUT            = "responseTimeOut";
    }

    ////////////////////////////////////////////////////////////////////////////
    // interface Feature
    // ~~~~~~~~~ ~~~~~~~~
    public interface Feature {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the schemas needs to be
       ** fetched from the Service Provider.
       ** <p>
       ** Default: <code>false</code>
       */
      static final String FETCH_SCHEMA                = "fetchSchema";
      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the character that
       ** separates multiple values for the same entry tag name.
       */
      static final String MULTIVALUE_SEPARATOR        = "multiValueSeparator";
      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the database statement to
       ** obtain the current timestamp from the database.
       */
      static final String SYSTEM_TIMESTAMP            = "systemTimestamp";
      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify that the JDBC Auto Commit
       ** feature needs stay on enabled if an connection is auqired from the
       ** pool.
       ** <p>
       ** This feature is only necessary to support DB2. This stupid database
       ** still needs a commit after each select to remove the read locks from
       ** the page.
       */
      static final String ENFORCE_AUTO_COMMIT         = "enforceAutoCommit";
      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the pseudo column used by
       ** paged resultsets.
       */
      static final String ROW_NUMBER_ATTRIBUTE        = "pseudoRownumAttribute";
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface OIM
  // ~~~~~~~~~ ~~~
  interface OIM {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // interface Resource
    // ~~~~~~~~~ ~~~~~~~~
    public interface Resource {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      /**
       ** Attribute tag which must be defined on an <code>IT Resource</code> to
       ** specify depending on the Database Server that you are using the JDBC
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
      static final String DATABASE_DRIVER             = "Database Driver";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the name of the database as specified in the
       ** {@link DatabaseResource#SERVER_NAME} and
       ** {@link DatabaseResource#SERVER_PORT} parameter.
       */
      static final String DATABASE_NAME               = "Database Name";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the cataloge schema name to be used.
       */
      static final String DATABASE_SCHEMA             = "Database Schema";
      /**
       ** Attribute tag which may be defined on an <code>IT Resource</code> to
       ** specify the cataloge schema name to be used.
       */
      static final String SERVICE_NAME                = "Service Name";
    }

    ////////////////////////////////////////////////////////////////////////////
    // interface Feature
    // ~~~~~~~~~ ~~~~~~~~
    interface Feature {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////
      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the schemas needs to be
       ** fetched from the Service Provider.
       ** <p>
       ** Default: <code>false</code>
       */
      static final String FETCH_SCHEMA                = "fetch-schema";
      /**
       ** Attribute tag which may be defined on a
       ** <code>Metadata Descriptor</code> to specify the character that
       ** separates multiple values for the same entry tag name.
       */
      static final String MULTIVALUE_SEPARATOR        = "multi-value-separator";
      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the database statement to
       ** obtain the current timestamp from the database.
       */
      static final String SYSTEM_TIMESTAMP            = "database-system-timestamp";
      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify that the JDBC Auto Commit
       ** feature needs stay on enabled if an connection is auqired from the
       ** pool.
       ** <p>
       ** This feature is only necessary to support DB2. This stupid database
       ** still needs a commit after each select to remove the read locks from
       ** the page.
       */
      static final String ENFORCE_AUTO_COMMIT         = "enforce-auto-commit";
      /**
       ** Attribute tag which must be defined on a
       ** <code>Metadata Descriptor</code> to specify the pseudo column used by
       ** paged resultsets.
       */
      static final String ROW_NUMBER_ATTRIBUTE        = "pseudo-rownum-attribute";
    }
  }
  /**
   ** the attribute tags of the feature definition in the
   ** <code>Metadata Descriptor</code>
   */
  static final Set<String> PROPERTY = CollectionUtility.set(
     OIM.Feature.FETCH_SCHEMA
   , OIM.Feature.MULTIVALUE_SEPARATOR
   , OIM.Feature.SYSTEM_TIMESTAMP
   , OIM.Feature.ENFORCE_AUTO_COMMIT
   , OIM.Feature.ROW_NUMBER_ATTRIBUTE
  );

  /**
   ** the attribute mapping of the feature transfer from the
   ** <code>IT Resource</code> to <code>Identity Connector</code>
   ** configuration
   */
  static final Map<String, String> RESOURCE = CollectionUtility.map(
    new String[]{ ICF.Resource.SERVER_HOST,     ICF.Resource.SERVER_PORT,     ICF.Resource.DATABASE_DRIVER, ICF.Resource.DATABASE_NAME, ICF.Resource.DATABASE_SCHEMA, ICF.Resource.SERVICE_NAME, ICF.Resource.PRINCIPAL_NAME,     ICF.Resource.PRINCIPAL_PASSWORD,     ICF.Resource.SECURE_SOCKET,     ICF.Resource.LOCALE_LANGUAGE,     ICF.Resource.LOCALE_COUNTRY,     ICF.Resource.LOCALE_TIMEZONE,     ICF.Resource.CONNECTION_TIMEOUT,     ICF.Resource.CONNECTION_RETRY_COUNT,     ICF.Resource.CONNECTION_RETRY_INTERVAL,     ICF.Resource.RESPONSE_TIMEOUT }
  , new String[]{ DatabaseResource.SERVER_NAME, DatabaseResource.SERVER_PORT, OIM.Resource.DATABASE_DRIVER, OIM.Resource.DATABASE_NAME, OIM.Resource.DATABASE_SCHEMA, OIM.Resource.SERVICE_NAME, DatabaseResource.PRINCIPAL_NAME, DatabaseResource.PRINCIPAL_PASSWORD, DatabaseResource.SECURE_SOCKET, DatabaseResource.LOCALE_LANGUAGE, DatabaseResource.LOCALE_COUNTRY, DatabaseResource.LOCALE_TIMEZONE, DatabaseResource.CONNECTION_TIMEOUT, DatabaseResource.CONNECTION_RETRY_COUNT, DatabaseResource.CONNECTION_RETRY_INTERVAL, DatabaseResource.RESPONSE_TIMEOUT }
  );

  /**
   ** the attribute mapping of the feature transfer from the
   ** <code>Metadata Descriptor</code> to <code>Identity Connector</code>
   ** configuration
   */
  static final Map<String, String> FEATURE = CollectionUtility.map(
    new String[]{ ICF.Feature.FETCH_SCHEMA, ICF.Feature.ENFORCE_AUTO_COMMIT, ICF.Feature.ROW_NUMBER_ATTRIBUTE, ICF.Feature.SYSTEM_TIMESTAMP }
  , new String[]{ OIM.Feature.FETCH_SCHEMA, OIM.Feature.ENFORCE_AUTO_COMMIT, OIM.Feature.ROW_NUMBER_ATTRIBUTE, OIM.Feature.SYSTEM_TIMESTAMP,}
  );
}