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

    System      :   Oracle Identity Manager Plugin Shared Library
    Subsystem   :   Common Shared Plugin

    File        :   GlobalUID.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    GlobalUID.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.event.spi;

import java.util.Map;
import java.util.HashMap;

import java.io.Serializable;

import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import oracle.iam.platform.Platform;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.platform.pluginframework.Plugin;
import oracle.iam.platform.pluginframework.PluginFramework;
import oracle.iam.platform.pluginframework.PluginStoreException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.event.AbstractPreProcessHandler;

////////////////////////////////////////////////////////////////////////////////
// class GlobalUID
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>GlobalUID</code> act as the service end point for the Oracle
 ** Identity Manager to generate Global UID's.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class GlobalUID extends AbstractPreProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String GENERATOR         = "guid.generator";
  private static final String FORMAT            = "guid.format";
  private static final String FIELDNAME         = "guid.attribute";

  private static final String DEFAULT_GENERATOR = "select uid_seq.nextval from dual";
  private static final String DEFAULT_FORMAT    = "%1$08d";
  private static final String DEFAULT_FIELDNAME = "%1$08d";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String              generator         = DEFAULT_GENERATOR;
  private String              format            = DEFAULT_FORMAT;
  private String              fieldname         = DEFAULT_FIELDNAME;

  private DataSource          dataSource        = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GlobalUID</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GlobalUID() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PreProcessHandler)
  /**
   ** This function is called on one-off orchestration operations.<p>
   ** In this implementation we will generate a global unique id.
   ** <p>
   ** This handler will work for CREATE operations only.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventID            the identifier of the orchestration event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestration parameters, operation.
   **
   ** @return                    a {@link EventResult} indicating the
   **                            operation has proceed.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public EventResult execute(final long processId, final long eventID, final Orchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    // retrieves all current attributes in the orchestration
    // these are the values that are being added or changed
    // this map does not contain all user atttibutes on existing records
    final HashMap<String, Serializable> parameter = orchestration.getParameters();

    // Returns the type of operation for branching or other purposes
    // Value is one of CREATE, MODIFY, DELETE
    String operation = orchestration.getOperation();
    if ("CREATE".equalsIgnoreCase(operation)) {
      // the result is formatted as a decimal integer prefix with the pattern
      // specified by System Configuration entry OCS.GUID.Format
      // put the generated GUID in the attribute set
      parameter.put(this.fieldname, generate(this.format));
    }

    trace(method, SystemMessage.METHOD_EXIT);
    // Event Result is a way for the event handler to notify the kernel of any
    // failures or errors and also if any subsequent actions need to be taken
    // (immediately or in a deferred fashion). It can also be used to indicate
    // if the kernel should restart this orchestration or veto it if the event
    // handler doesn't want to notify the kernel of anything it shouldn't return
    // a null value  instead an empty EventResult object
    return new EventResult();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PreProcessHandler)
  /**
   ** This function is called on bulk orchestration operations.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestration event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestration parameters, operation.
   **
   ** @return                    a {@link BulkEventResult} indicating the
   **                            operation has proceed.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public BulkEventResult execute(final long processId, final long eventId, final BulkOrchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    trace(method, SystemMessage.METHOD_EXIT);
    // even if you don't implement a bulk handler you generally want to return
    // the BulkEventResult class otherwise bulk orchestrations will error out
    // and orphan
    return new BulkEventResult();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** Called during creation of the orchestration engine at server startup.
   **
   ** @param  parameter          the parameter mapping passed to the
   **                            <code>EventHandler</code> obtained from the
   **                            descriptor and send by the Orchestration.
   */
  @Override
  public void initialize(final HashMap<String, String> parameter) {
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      final Plugin plugin = PluginFramework.getPluginRegistry().getPlugin(EVENT, this.getClass().getName());
      final Map    data   = plugin.getMetadata();
      // obtain the statement to generate a unique id
      this.generator = (String)data.get(GENERATOR);
      if (StringUtility.isEmpty(this.generator))
        this.generator = DEFAULT_GENERATOR;

      // obtain the format of the GUID to generate
      this.format = (String)data.get(FORMAT);
      if (StringUtility.isEmpty(this.format))
        this.format = DEFAULT_FORMAT;

      // obtain the name of the attribute the GUID will be stored
      this.fieldname = (String)data.get(FIELDNAME);
      if (StringUtility.isEmpty(this.fieldname))
        this.fieldname = DEFAULT_FIELDNAME;
    }
    catch (PluginStoreException e) {
      fatal(method, e);
    }
    catch (Exception e) {
      fatal(method, e);
    }
    this.dataSource = Platform.getOperationalDS();
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a Global UID the meets the requirements of the specified format.
   **
   ** @param  paddingFormat      the format string indicate how the arguments
   **                            should be processed and where they should be
   **                            inserted in the text.
   **
   ** @return                    the passed String with the replaced characters
   */
  private String generate(final String paddingFormat) {
    final String method = "generate";
    trace(method, SystemMessage.METHOD_ENTRY);

    String            guid       = null;
    Connection        connection = null;
    ResultSet         resultSet  = null;
    PreparedStatement statement  = null;
    try {
      connection = DatabaseConnection.aquire(this.dataSource);
      statement  = DatabaseStatement.createPreparedStatement(connection, this.generator);
      resultSet  = statement.executeQuery();
      if (resultSet.next()) {
        // the result is formatted as a decimal integer
        guid = String.format(paddingFormat, resultSet.getLong(1));
      }
      else {
        guid = "99999999";
      }
    }
    catch (SQLException e) {
      fatal(method, e);
    }
    catch (TaskException e) {
      error(method, e.getMessage());
    }
    DatabaseStatement.closeResultSet(resultSet);
    DatabaseStatement.closeStatement(statement);
    DatabaseConnection.release(connection);
    trace(method, SystemMessage.METHOD_EXIT);
    return guid;
  }
}