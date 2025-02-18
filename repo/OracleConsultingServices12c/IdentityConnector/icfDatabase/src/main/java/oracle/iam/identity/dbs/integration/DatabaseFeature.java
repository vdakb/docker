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

    File        :   DatabaseFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DatabaseFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.integration;

import java.util.Map;

import java.io.File;
import java.io.InputStream;

import org.xml.sax.InputSource;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.integration.TargetFeature;

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
 ** @since   1.0.0.0
 */
public class DatabaseFeature extends TargetFeature {

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
   ** and does not belongs to an Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DatabaseFeature</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  private DatabaseFeature(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchSchema
  /**
   ** Returns the value of the extended configuration attribute that specifies
   ** the schema definition needs always to be fetched from the Service
   ** Provider.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Database.OIM.Feature#FETCH_SCHEMA}.
   ** <p>
   ** If {@link Database.OIM.Feature#FETCH_SCHEMA} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** <code>false</code>.
   **
   ** @return                    the value of the extended configuration
   **                            attribute that specifies schema definition
   **                            needs always to be fetched from the Service
   **                            Provider.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean fetchSchema() {
    return booleanValue(Database.OIM.Feature.FETCH_SCHEMA, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator character for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Database.OIM.Feature#MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link Database.OIM.Feature#MULTIVALUE_SEPARATOR} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link Database.OIM.Feature#MULTIVALUE_SEPARATOR_DEFAULT}.
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String multiValueSeparator() {
    return stringValue(Database.OIM.Feature.MULTIVALUE_SEPARATOR, Database.MULTIVALUE_SEPARATOR_DEFAULT);
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
   **  | Oracle Database      | oracle.jdbc.OracleDriver                     |
   **  |                      | oracle.jdbc.driver.OracleDriver              |
   **  | Microsoft SQL Server | com.microsoft.sqlserver.jdbc.SQLServerDriver |
   **  | MySQL                | com.mysql.jdbc.Driver                        |
   **  | IBM DB2 UDB          | com.ibm.db2.jcc.DB2Driver                    |
   **  | Sybase               | com.sybase.jdbc2.jdbc.SybDriver              |
   **  |                      | com.sybase.jdbc3.jdbc.SybDriver              |
   **  +----------------------+----------------------------------------------+
   ** </pre>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Database.OIM.Resource#DATABASE_DRIVER}.
   ** <p>
   ** If {@link Database.OIM.Resource#DATABASE_DRIVER} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link Database.OIM.Resource#DATABASE_DRIVER_ORACLE4}.
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String databaseDriverClass() {
    return stringValue(Database.OIM.Resource.DATABASE_DRIVER, Database.DATABASE_DRIVER_ORACLE4);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTimeStatement
  /**
   ** Returns the database statement to obtain the current timestamp from the
   ** database.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Database.OIM.Feature#SYSTEM_TIMESTAMP}.
   **
   ** @return                    the database statement to obtain the current
   **                            timestamp from the database.
   */
  public final String systemTimeStatement() {
    return stringValue(Database.OIM.Feature.SYSTEM_TIMESTAMP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enforceAutoCommit
  /**
   ** Returns <code>true</code> the JDBC Auto Commit feature needs stay on
   ** enabled after a connection was auqired from the pool.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Database.OIM.Feature#ENFORCE_AUTO_COMMIT}.
   **
   ** @return                    <code>true</code> the JDBC Auto Commit feature
   **                            needs stay on enabled after a connection was
   **                            aquired from the pool.
   */
  public final boolean enforceAutoCommit() {
    return booleanValue(Database.OIM.Feature.ENFORCE_AUTO_COMMIT, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rowNumberAttribute
  /**
   ** Returns the name of the pseudo attribute used in paged result sets.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link Database.OIM.Feature#ROW_NUMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the pseudo attribute used in paged
   **                            result sets.
   */
  public final String rowNumberAttribute() {
    return stringValue(Database.OIM.Feature.ROW_NUMBER_ATTRIBUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registry  (TargetResource)
  /**
   ** Returns the mapping of attribute names defined on this
   ** <code>IT Resource</code> and the parameter names expected by connector
   ** bundle configuration.
   **
   ** @return                    the mapping of attribute names defined
   **                            on this <code>IT Resource</code> and the
   **                            parameter names expected by connector bundle
   **                            configuration.
   */
  @Override
  public final Map<String, String> registry() {
    return Database.FEATURE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Metadata Descriptor</code> which
   ** is associated with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to be populated manually
   ** and does not belongs to an Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an instance of <code>DatabaseFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseFeature</code>.
   */
  public static DatabaseFeature build(final Loggable loggable) {
    return new DatabaseFeature(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to configure <code>Metadata Descriptor</code> from a
   ** {@link File}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    an instance of <code>DatabaseFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseFeature</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DatabaseFeature build(final Loggable loggable, final File file)
    throws TaskException {

    final DatabaseFeature feature = new DatabaseFeature(loggable);
    DatabaseFeatureFactory.configure(feature, file);
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to configure <code>Metadata Descriptor</code> from a
   ** {@link InputStream}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  stream             the mapping configuration file for the
   **                            descriptor to create as a XML stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    an instance of <code>DatabaseFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseFeature</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DatabaseFeature build(final Loggable loggable, final InputStream stream)
    throws TaskException {

    final DatabaseFeature feature = new DatabaseFeature(loggable);
    DatabaseFeatureFactory.configure(feature, stream);
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to configure <code>Metadata Descriptor</code> from a
   ** {@link InputSource}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    an instance of <code>DatabaseFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseFeature</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DatabaseFeature build(final Loggable loggable, final InputSource source)
    throws TaskException {

    final DatabaseFeature feature = new DatabaseFeature(loggable);
    DatabaseFeatureFactory.configure(feature, source);
    return feature;
  }
}