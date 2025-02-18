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

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.dbs;

import java.sql.SQLException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.dbms.DatabaseSchema;
import oracle.iam.identity.icf.dbms.DatabaseContext;
import oracle.iam.identity.icf.dbms.DatabaseEndpoint;
import oracle.iam.identity.icf.dbms.DatabaseMessage;

import oracle.iam.identity.icf.resource.DatabaseBundle;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** The <code>Context</code> wraps the JDBC connection.
 ** <p>
 ** Define the test method meaning the wrapped connection is still valid.
 ** <br>
 ** Defines come useful method to work with prepared statements.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends DatabaseContext {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  DatabaseSchema                  schema    = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Context</code> which is associated with the specified
   ** {@link DatabaseEndpoint} for configuration purpose.
   **
   ** @param  endpoint           the {@link DatabaseEndpoint}
   **                            <code>IT Resource</code> definition where this
   **                            connector is associated with.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   */
  private Context(final DatabaseEndpoint endpoint) {
    // ensure inherinstance
    super(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Context</code> which is associated with
   ** the specified {@link DatabaseEndpoint} as the logger.
   **
   ** @param  endpoint           the {@link DatabaseEndpoint} IT Resource
   **                            definition where this connector is associated
   **                            with.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   **
   ** @return                    the context.
   **                            <br>
   **                            Possible object is {@link Context}.
   */
  public static Context build(final DatabaseEndpoint endpoint) {
    return new Context(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   test
  /**
   ** Determines if the underlying JDBC {@link java.sql.Connection} is valid.
   **
   ** @throws RuntimeException   if the underlying JDBC
   **                            {@link java.sql.Connection} is not valid
   **                            otherwise do nothing.
   */
  public void test() {
    final String method = "test";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // obtain a connection from the pool
      connect();
      unwrap().isValid(endpoint().timeOutConnect());
      debug(method, DatabaseBundle.string(DatabaseMessage.CONNECTION_ALIVE));
    }
    catch (SQLException e) {
      try {
        rollback();
      }
      catch (SystemException x) {
        ; // intentionally left blank
      }
      fatal(method, e);
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      Main.propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Return the schema upon request.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object {@link DatabaseSchema}.
   **
   ** @throws SystemException    if the jave type class defined for an attribute
   **                            is not found at the class path.
   */
  public DatabaseSchema schema()
    throws SystemException {

    if (this.schema == null) {
      // locate the schema declaration at the class path by leveraging the class
      // loader of the bundle where the library is part of.
      this.schema = null;//DatabaseSchema.load(endpoint(), Context.class.getResourceAsStream(SCHEMA));
    }
    return this.schema;
  }
}
