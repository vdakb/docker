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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

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

package oracle.iam.identity.adapter.spi;

import java.util.HashMap;

import java.io.Serializable;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcPropertyNotFoundException;
import Thor.API.Exceptions.tcDuplicatePropertyException;
import Thor.API.Operations.tcPropertyOperationsIntf;

import oracle.iam.platform.Platform;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;

import oracle.iam.platform.kernel.spi.PreProcessHandler;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

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
public class GlobalUID implements PreProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static finale attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String   STATEMENT = "OCS.GUID.Statement";
  private static final String   FORMAT    = "OCS.GUID.Format";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                format     = null;
  private DataSource            dataSource = null;
  private String                statement  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

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
  public GlobalUID(){
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (EventHandler)
  /**
   ** Called during creation of the orchestration engine at server startup.
   **
   ** @param  parameter          the parameter mapping passed to the
   **                            <code>EventHandler</code> obtained from the
   **                            descriptor and send by the Orchestration.
   */
  @Override
  public void initialize(final HashMap<String, String> parameter) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancel (Cancellable)
  /**
   ** Method containing the logic that need to be executed if the orchestration
   ** is cancelled.
   ** <p>
   ** This would be called only for asynchronous actions.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestartion event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   **
   ** @return                    the outcome of the event handler.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public boolean cancel(final long processId, final long eventId, final AbstractGenericOrchestration orchestration) {
    throw new RuntimeException("Method not implemented");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compensate (Compensable)
  /**
   ** Method containing the logic that need to be executed to compensate the
   ** changes made by current event handler if the orchestration fails.
   ** <p>
   ** This method is allways called by the plugin framework if something goes
   ** wrong.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestartion event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   */
  @Override
  public void compensate(final long processId, final long eventId, final AbstractGenericOrchestration orchestration) {
    throw new RuntimeException("Method not implemented");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PreProcessHandler)
  /**
   ** Generates a Global UID.
   **
   ** @return                    the passed String with the replaced characters
   */
  public EventResult execute(long processId, long eventId, Orchestration orchestration) {
    final String method = "execute";
    System.out.println(method + " "+ SystemMessage.METHOD_ENTRY);

    final HashMap<String, Serializable> parameter = orchestration.getParameters();

    // the result is formatted as a decimal integer
    final String guid = generate();
    parameter.put("Global UID", guid);

    System.out.println(method + " "+ SystemMessage.METHOD_EXIT);
    return new EventResult();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PreProcessHandler)
  /**
   ** Generates a Global UID.
   **
   ** @return                    the passed String with the replaced characters
   */
  public BulkEventResult execute(final long processId, final long eventId, final BulkOrchestration orchestration) {
    final String method = "execute";
    System.out.println(method + " "+ SystemMessage.METHOD_ENTRY);

    System.out.println(method + " "+ SystemMessage.METHOD_EXIT);
    return new BulkEventResult();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a Global UID.
   **
   ** @return                    the passed String with the replaced characters
   */
  public String generate() {
    final String method = "generate";
    System.out.println(method + " "+ SystemMessage.METHOD_ENTRY);

    String guid = generate(this.format);

    System.out.println(method + " "+ SystemMessage.METHOD_EXIT);
    return guid;
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
    System.out.println(method + " "+ SystemMessage.METHOD_ENTRY);

    String            guid       = null;
    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    try {
      tcPropertyOperationsIntf facade = Platform.getService(tcPropertyOperationsIntf.class);

      this.dataSource = Platform.getOperationalDS();
      this.format    = facade.getPropertyValue(FORMAT);
      this.statement = facade.getPropertyValue(STATEMENT);

      connection = DatabaseConnection.aquire(this.dataSource);
      statement  = DatabaseStatement.createPreparedStatement(connection, this.statement);
      statement.execute();

      resultSet  = statement.getResultSet();
      long id    = resultSet.getLong(0);
      // the result is formatted as a decimal integer
      guid = format(id, paddingFormat);
    }
    catch (tcPropertyNotFoundException e) {
      System.err.println(method + " "+ e.getMessage());
    }
    catch (tcDuplicatePropertyException e) {
      System.err.println(method + " "+ e.getMessage());
    }
    catch (tcAPIException e) {
      System.err.println(method + " "+ e.getMessage());
    }
    catch (SQLException e) {
      System.err.println(method + " "+ e.getMessage());
    }
    catch (TaskException e) {
      System.err.println(method + " "+ e.getMessage());
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }

    System.out.println(method + " "+ SystemMessage.METHOD_EXIT);
    return guid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formates the passed identifier to the required format.
   **
   ** @param  identifier         the identifier to convert.
   **                            <b>Note:</b>
   **                            Must be a decimal value.
   ** @paran  format             the format string indicate how the arguments
   **                            should be processed and where they should be
   **                            inserted in the text.
   **
   ** @return                    the passed String with the replaced characters
   */
  private String format(long identifier, final String format) {
    // the result is formatted as a decimal integer
    return String.format(format, identifier);
  }
}