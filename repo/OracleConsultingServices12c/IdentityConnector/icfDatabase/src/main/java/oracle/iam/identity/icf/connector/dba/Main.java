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

    ------------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Database Application Connector

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.dba;

import oracle.iam.identity.icf.connector.AbstractConnector;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.PoolableConnector;

import org.identityconnectors.framework.common.objects.Schema;

import org.identityconnectors.framework.spi.operations.SchemaOp;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.dbms.DatabaseConfiguration;

import oracle.iam.identity.icf.connector.DatabaseContext;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the basic functionality of an Identity Manager
 ** {@link PoolableConnector} for a database application system.
 ** <p>
 ** The life-cycle for the <code>Pool</code> is as follows:
 ** <br>
 ** {@link #init(Configuration)} is called then any of the operations
 ** implemented in the <code>Pool</code> and finally {@link #dispose()}. The
 ** {@link #init(Configuration)} and {@link #dispose()} allow for block
 ** operations. For instance bulk creates or deletes and the use of before and
 ** after actions. Once {@link #dispose()} is called the <code>Pool</code>
 ** object is discarded.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 ** <p>
 ** The database application connector is a basic, but easy to use connector for
 ** accounts in a relational database.
 ** <p>
 ** It supports create, update, search, and delete operations. It can also be
 ** used for pass-thru authentication, although it assumes the password is in
 ** clear text in the database.
 ** <p>
 ** This connector assumes that all account data is stored in a single database
 ** table. The delete action is implemented to simply remove the row from the
 ** table.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
@ConnectorClass(configurationClass=DatabaseConfiguration.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.connector.dba.Main.properties")
public class Main extends    AbstractConnector
                  implements SchemaOp {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String  LOGGER_CATEGORY = "JCS.CONNECTOR.DBA";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Place holder for the {@link Connection} passed into the callback
   ** {@link ConnectionFactory#setConnection(Connection)}.
   */
  private DatabaseContext       context;

  /**
   ** Place holder for the {@link Configuration} passed into the callback
   ** {@link Main#init(Configuration)}.
   */
  private DatabaseConfiguration config;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> database connector that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Main() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConfiguration (Connector)
  /**
   ** Return the configuration that was passed to {@link #init(Configuration)}.
   **
   ** @return                    the configuration that was passed to
   **                            {@link #init(Configuration)}.
   */
  @Override
  public Configuration getConfiguration() {
    return this.config;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (Connector)
  /**
   ** Initialize the connector with its configuration.
   ** <p>
   ** For instance in a JDBC Connector this would include the database URL,
   ** password, and user.
   **
   ** @param  configuration      the instance of the {@link Configuration}
   **                             object implemented by the
   **                             <code>Connector</code> developer and populated
   **                             with information in order to initialize the
   **                             <code>Connector</code>.
   */
  @Override
  public void init(final Configuration configuration) {
    final String method = "init";
    trace(method, Loggable.METHOD_ENTRY);
    this.config  = (DatabaseConfiguration)configuration;
    this.context = DatabaseContext.build(this.config.endpoint());
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispose (Connector)
  /**
   ** Dispose of any resources the Connector uses.
   */
  @Override
  public void dispose() {
    final String method = "dispose";
    trace(method, Loggable.METHOD_ENTRY);
    this.config  = null;
    this.context = null;
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkAlive (PoolableConnector)
  /**
   ** Checks if the connector is still alive.
   ** <p>
   ** A connector can spend a large amount of time in the pool before being
   ** used. This method is intended to check if the connector is alive and
   ** operations can be invoked on it (for instance, an implementation would
   ** check that the connector's physical connection to the resource has not
   ** timed out).
   ** <p>
   ** The major difference between this method and <code>TestOp.test()</code> is
   ** that this method must do only the minimum that is necessary to check that
   ** the connector is still alive. <code>TestOp.test()</code> does a more
   ** thorough check of the environment specified in the {@link Configuration},
   ** and can therefore be much slower.
   **
   ** @throws RuntimeException   if the connector is no longer alive.
   */
  //@Override
  public void checkAlive() {
    final String method = "checkAlive";
    trace(method, Loggable.METHOD_ENTRY);
    // intentionally left blank
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema (SchemaOp)
  /**
   ** Describes the types of objects this Connector supports.
   ** <p>
   ** This method is considered an operation since determining supported objects
   ** may require configuration information and allows this determination to be
   ** dynamic.
   ** <p>
   ** The special [@link Uid} attribute should never appear in the schema, as it
   ** is not a real attribute of an object, rather a reference to it.
   ** <br>
   ** If the resource object-class has a writable unique id attribute that is
   ** different than its <code>Name</code>, then the schema should contain a
   ** resource-specific attribute that represents this unique id.
   ** <br>
   ** For example, a Unix account object might contain unix <code>uid</code>.
   **
   ** @return                    the configuration that was passed to
   **                            {@link #init(Configuration)}.
   */
  @Override
  public Schema schema() {
    final String method = "schema";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, Loggable.METHOD_EXIT);
    return null;
  }
}