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

    File        :   DatabaseAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.util.List;

import org.identityconnectors.framework.spi.Configuration;

import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;
import org.identityconnectors.framework.spi.operations.SPIOperation;
import org.identityconnectors.framework.spi.operations.UpdateAttributeValuesOp;

import oracle.iam.identity.icf.connector.AbstractConnectorPool;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseConnector
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>DatabaseConnector</code> implements the basic functionality of an
 ** Identity Manager {@link AbstractConnectorPool} for a database target system.
 ** <p>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DatabaseConnector extends    AbstractConnectorPool
                                        implements TestOp
                                        ,          SchemaOp
                                        ,          SearchOp<DatabaseFilter>
                                        ,          CreateOp
                                        ,          DeleteOp
                                        ,          UpdateOp
                                        ,          UpdateAttributeValuesOp {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the operation the connector supports */
  protected static final List<Class<? extends SPIOperation>> OPERATION = CollectionUtility.list(
    CreateOp.class
  , DeleteOp.class
  , UpdateOp.class
  , SearchOp.class
  , SchemaOp.class
  , TestOp.class
  );

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Place holder for the {@link DatabaseConfiguration} passed into the
   ** callback {@link #init(Configuration)}.
   */
  protected DatabaseConfiguration config;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseConnector</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected DatabaseConnector(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the {@link DatabaseEndpoint} this context is using to connect and
   ** perform operations on the Database Service.
   **
   ** @return                    the {@link DatabaseEndpoint} this context is
   **                            using to connect and perform operations on the
   **                            Database Service.
   **                            <br>
   **                            Possible object {@link DatabaseEndpoint}.
   */
  public final DatabaseEndpoint endpoint() {
    return this.config.endpoint();
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
  public final Configuration getConfiguration() {
    return this.config;
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
   ** through check of the environment specified in the {@link Configuration},
   ** and can therefore be much slower.
   **
   ** @throws RuntimeException   if the connector is no longer alive.
   */
  @Override
  public void checkAlive() {
    final String method = "checkAlive";
    trace(method, Loggable.METHOD_ENTRY);
    test();
    // intentionally left blank
    trace(method, Loggable.METHOD_EXIT);
  }
}