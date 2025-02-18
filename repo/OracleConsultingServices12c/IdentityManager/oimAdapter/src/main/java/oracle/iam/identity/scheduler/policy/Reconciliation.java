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

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Common Scheduler Operations

    File        :   Reconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Reconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2008-28-06  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcUserNotFoundException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcObjectNotFoundException;
import Thor.API.Exceptions.tcProvisioningNotAllowedException;

import com.thortech.xl.vo.ResourceData;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.foundation.offline.Identity;
import oracle.iam.identity.foundation.offline.Application;
import oracle.iam.identity.foundation.offline.AccessPolicy;
import oracle.iam.identity.foundation.offline.ApplicationAccount;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import oracle.iam.identity.model.Configuration;

import oracle.iam.identity.resource.ResolverBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Reconciliation</code> act as the service end point for the Oracle
 ** Identity Manager to resolve <code>Access Policies</code>.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
abstract class Reconciliation extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute to define the mapping of the entitlements on the source and
   ** the target system.
   */
  protected static final String SERVER_DEFINITION        = "Server Definition";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** type of reconciliation operation should be performed.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String RECONCILIATION_OPERATION = "Reconciliation Operation";

  /**
   ** Attribute to advice which organization should be handled by a specific
   ** task.
   */
  protected static final String FILTER_ORGANIZATION      = "Organization Filter";

  /**
   ** Attribute to advice which user should be handled by a specific
   ** task.
   */
  protected static final String FILTER_IDENTITY          = "Identity Filter";

  /**
   ** Attribute to advice which resource object status should be handled by a
   ** specific task.
   */
  protected static final String OBJECTTYPE               = "Resource Type";

  /**
   ** Attribute to advice which resource object status should be handled by a
   ** specific task.
   */
  protected static final String OBJECTSTATUS             = "Resource Status";

  /**
   ** Attribute to advice which process type should be handled by a specific
   ** task.
   */
  protected static final String PROCESSTYPE              = "Process Type";

  /** the category of the logging facility to use */
  private static final String   LOGGER_CATEGORY          = "OCS.POLICY.RECONCILIATION";

  private static final String   DATASOURCE               = "jdbc/oimOperationalDB";

  private static final String   DATE_QUERY               = "SELECT systimestamp FROM DUAL";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected int                 userWildcardPos        = -1;
  protected int                 orgWildcardPos         = -1;
  protected int                 affectedEntries        = 0;

  /**
   ** the flag indicating that the transformation of one or more attributes is
   ** required.
   */
  protected Date                lastExecution;

  /** the flag to indicate that any pre-condition check has failed */
  private boolean               preConditionViolated   = false;

  /** the JDBC connection to direct access the Oracle Identity Manager database */
  private Connection            connection             = null;

  /** the mapping of entitlements from the source and the target system */
  private Configuration         configuration          = null;

  private ApplicationAccount[]  sourceServer           = null;

  private AccessPolicy[]        policy                 = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> task adpater that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Reconciliation() {
    // ensure inheritance
    this(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> with the specified logging
   ** category.
   **
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  protected Reconciliation(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preConditionViolated
  /**
   ** Sets the flag to indicate a pre-condition check has failed.
   **
   ** @param preConditionViolated the flag to indicate a pre-condition check has
   **                             failed to set.
   */
  protected final void preConditionViolated(final boolean preConditionViolated) {
    this.preConditionViolated = preConditionViolated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preConditionViolated
  /**
   ** Returns the flag to indicate any pre-condition check has failed.
   **
   ** @return                    the flag to indicate any pre-condition check
   **                            has failed.
   */
  protected final boolean preConditionViolated() {
    return this.preConditionViolated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Returns the JDBC connection associated with this task.
   **
   ** @return             the JDBC connection associated with this
   **                     <code>Connector</code>
   */
  protected final Connection connection() {
    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastExecution
  /**
   ** Returns the timestamp where this task was last executed.
   **
   ** @return             the timestamp where this task was last executed.
   */
  protected final Date lastExecution() {
    return this.lastExecution;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceServer
  /**
   ** Returns the List of source server with this task.
   **
   ** @return             the List of server with this task.
   */
  protected final ApplicationAccount[] sourceServer() {
    return this.sourceServer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policy
  /**
   ** Returns the List of policies with this task.
   **
   ** @return             the List of policies with this task.
   */
  protected final AccessPolicy[] policy() {
    return this.policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open
  /**
   ** The specified name of a JDBC DataSource is used to aquire a database
   ** connection used for provisioning and reconciliation.
   ** <p>
   ** The JDBC {@link Connection} should be released by invoking the
   ** <code>close()</code> method on this instance. If this is omitted may be
   ** the connection pool provided by the specified JDBC DataSource has no
   ** longer an available connection.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void open()
    throws TaskException {

    open(DATASOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open
  /**
   ** The specified name of a JDBC DataSource is used to aquire a database
   ** connection used for provisioning and reconciliation.
   ** <p>
   ** The name of the JDBC DataSource passed in must be the JNDI name of the
   ** configuration. Use AS Control to check the JNDI name before you configure
   ** the name in the IT Resource definition.
   ** <p>
   ** The JDBC {@link Connection} should be released by invoking the
   ** <code>close()</code> method on this instance. If this is omitted may be
   ** the connection pool provided by the specified JDBC DataSource has no
   ** longer an available connection.
   **
   ** @param  dataSource         the datasource used to establish a connection.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void open(final String dataSource)
    throws TaskException {

    final String method = "open";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      this.connection = DatabaseConnection.connection(dataSource);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close the connection aquired by {@link #open(String)} and frees all
   ** resources.
   **
   ** @throws TaskException      the excepetion thrown if any goes wrong
   */
  protected void close()
    throws TaskException {

    DatabaseConnection.release(this.connection);
    this.connection = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   countChanges
  /**
   ** Load the a <code>Resource Object</code> and their dependencies.
   **
   ** @return                    the numbers of changes.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected final int countChanges()
    throws TaskException {

    final String method = "countChanges";
    trace(method, SystemMessage.METHOD_ENTRY);

    ResultSet         resultSet = null;
    PreparedStatement statement = null;
    try {
      open();
      statement = DatabaseStatement.createPreparedStatement(this.connection(), prepareCountQuery().toString());
      resultSet = executeStatement(statement);
      while (!isStopped() && resultSet.next()) {
        // reset the iterator to fetch the values
        int column = 0;
        // reset the iterator to fetch the values
        this.affectedEntries = resultSet.getInt(++column);
      }
      return this.affectedEntries;
    }
    catch (SQLException e) {
      throw new TaskException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverDate
  /**
   ** Returns the current date from the server.
   **
   ** @return                    the current datetime from the database server.
   **
   ** @throws TaskException      if the operation fails
   */
  protected final Date serverDate()
    throws TaskException {

    final String method = "serverDate";
    trace(method, SystemMessage.METHOD_ENTRY);

    PreparedStatement statement = null;
    ResultSet         resultSet = null;
    Timestamp         timestamp = null;
    try {
      statement = DatabaseStatement.createPreparedStatement(this.connection, DATE_QUERY);
      resultSet = statement.executeQuery();
      timestamp = resultSet.next() ? resultSet.getTimestamp(1) : new Timestamp(0L);
      return new Date(timestamp.getTime());
    }
    catch (SQLException e) {
      throw new TaskException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConfiguration
  /**
   ** Load the a <code>Configuration</code>s and their dependencies.
   */
  protected final void createConfiguration() {
    final String method = "createConfiguration";
    trace(method, SystemMessage.METHOD_ENTRY);

    info(ResolverBundle.format(ResolverMessage.PRECONDITION_CONFIGURATION_BEGIN, reconcileObject()));
    try {
      this.configuration = new Configuration(this, stringValue(SERVER_DEFINITION));
      if (this.configuration.size() == 0) {
        preConditionViolated(true);
        error(method, ResolverBundle.format(ResolverMessage.PRECONDITION_CONFIGURATION_MAPPING_EMPTY, stringValue(SERVER_DEFINITION)));
      }
      else {
        info(ResolverBundle.format(ResolverMessage.PRECONDITION_CONFIGURATION_COMPLETE, reconcileObject()));
      }
    }
    catch (TaskException e) {
      preConditionViolated(true);
      error(method, ResolverBundle.format(ResolverMessage.PRECONDITION_CONFIGURATION_FAILED, reconcileObject()));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServer
  /**
   ** Load the a <code>Resource Object</code>.
   */
  protected final void createServer() {
    final String method = "createServer";
    trace(method, SystemMessage.METHOD_ENTRY);

    info(ResolverBundle.format(ResolverMessage.PRECONDITION_RESOURCE_MAPPING_BEGIN, reconcileObject()));
    TreeSet<Application> server = new TreeSet<Application>();
    try {
      Iterator<String> i = this.configuration.keySet().iterator();
      while (!preConditionViolated() && i.hasNext()) {
        final String serverName = i.next();
        server.add(ResourceFactory.createServer(this, serverName));
        /*
        else {
          info(ResolverBundle.format(ResolverMessage.PRECONDITION_RESOURCE_MAPPING_EMPTY, serverName));
          preConditionViolated(true);
        }
       */
      }
    }
    catch (TaskException e) {
      preConditionViolated(true);
      error(method, e.getLocalizedMessage());
    }

    if (preConditionViolated())
      info(ResolverBundle.format(ResolverMessage.PRECONDITION_RESOURCE_MAPPING_FAILED, reconcileObject()));
    else {
      this.sourceServer = new ApplicationAccount[server.size()];
      this.sourceServer = server.toArray(this.sourceServer);
      info(ResolverBundle.format(ResolverMessage.PRECONDITION_RESOURCE_MAPPING_COMPLETE, reconcileObject()));
    }

    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPolicies
  /**
   ** Maps the a <code>Access Policy</code> to the <code>Resource Object</code>.
   */
  protected final void createPolicies() {
    final String method = "createPolicies";
    trace(method, SystemMessage.METHOD_ENTRY);

    info(ResolverBundle.format(ResolverMessage.PRECONDITION_POLICY_MAPPING_BEGIN, reconcileObject()));
    Set<AccessPolicy> policy = null;
    try {
      policy = AccessPolicyFactory.create(this, this.sourceServer);
      if (policy == null || policy.size() == 0) {
        info(ResolverBundle.format(ResolverMessage.PRECONDITION_POLICY_MAPPING_EMPTY, this.sourceServer.toString()));
        preConditionViolated(true);
      }
    }
    catch (TaskException e) {
      preConditionViolated(true);
      error(method, e.getLocalizedMessage());
    }

    if (preConditionViolated())
      info(ResolverBundle.format(ResolverMessage.PRECONDITION_POLICY_MAPPING_FAILED, reconcileObject()));
    else {
      this.policy = new AccessPolicy[policy.size()];
      this.policy = policy.toArray(this.policy);
      info(ResolverBundle.format(ResolverMessage.PRECONDITION_POLICY_MAPPING_COMPLETE, reconcileObject()));
    }

    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateStringValue
  /**
   ** Validates a task parameter.
   **
   ** @param  name               the name of the task parameter to validate.
   ** @param  range              the value enumeration permitted for the task
   **                            parameter.
   **
   ** @return                    the valid parameter value.
   **
   ** @throws TaskException      if the value specified for the parameter is not
   **                            in the range of allowed values.
   */
  protected final String validateStringValue(final String name, final String[] range)
    throws TaskException {

    final String value = stringValue(name);
    int i = 0;
    for (; i < range.length; i++) {
      if (range[i].equalsIgnoreCase(value))
        break;
    }

    if (i == range.length) {
      StringBuilder buffer = new StringBuilder();
      for (int j = 0; j < range.length; j++) {
        if (j > 0)
          buffer.append(" | ");
        buffer.append(range[j]);
      }
      final String[] parameter = {name, buffer.toString() };
      throw new TaskException(TaskError.TASK_ATTRIBUTE_NOT_INRANGE, parameter);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeStatement
  /**
   ** Executes the SQL statement.
   ** <p>
   ** Before the statement is executed all values are bound to the placeholders.
   **
   ** @param  statement          the {@link PreparedStatement} to execute.
   **
   ** @return                    the result set of the executed query.
   **
   ** @throws TaskException      if the bind or execution fails.
   */
  abstract ResultSet executeStatement(final PreparedStatement statement)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareCountQuery
  /**
   ** Build the SQL Query to count affected entries.
   **
   ** @return                    the SQL Query to count affected entries.
   */
  abstract StringBuilder prepareCountQuery();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProvisionedResources
  /**
   ** Loads all provisioned <code>Resource Objetc</code>s for the
   ** {@link ApplicationAccount}.
   **
   ** @param  account            the <code>User Account</code> the provisioned
   **                            <code>Resource Objetc</code>s should be loaded.
   ** @param  objectType         to advice which object type should be handled.
   **
   ** @throws ResolverException  in case an error does occur.
   */
  protected final void createProvisionedResources(final Identity account, final String objectType)
    throws ResolverException {

    final String method = "createProvisionedReources";
    trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = {
      TaskBundle.string(TaskMessage.ENTITY_IDENTITY)
    , account.name()
    , TaskBundle.string(TaskMessage.ENTITY_ACCOUNT)
    };

    String columnName = null;
    try {
      tcResultSet resource = userFacade().getObjectsByType(account.key(), objectType);
      final int   rowCount = resource.getRowCount();
      if (rowCount == 0) {
        info(ResolverBundle.format(ResolverMessage.ACCOUNT_RESOURCE_EMPTY, parameter));
      }
      else {
        for (int i = 0; i < rowCount; i++) {
          resource.goToRow(i);
          columnName          = ResourceObject.KEY;
          final long   key    = resource.getLongValue(columnName);
          columnName          = ResourceObject.NAME;
          final String name   = resource.getStringValue(columnName);
          columnName          = ResourceObject.STATUS;
          final String status = resource.getStringValue(columnName);
          columnName          = ResourceObject.USER_INSTANCE;
          // do not Resource Objects as resources to provision if they are the
          // data sources instead join the object instance key of the source
          // systen with the account
          for (int j = 0; j < this.sourceServer.length; j++) {
            if (this.sourceServer[j].key() != key) {
              account.addAssignedApplication(ResourceFactory.create(key, name, status));
            }
          }
        }
      }
    }
    catch (tcUserNotFoundException e) {
      throw new ResolverException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcColumnNotFoundException e) {
      throw new ResolverException(TaskError.COLUMN_NOT_FOUND, columnName);
    }
    catch (tcAPIException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisionResource
  /**
   ** Returns the <code>Process Instance Key</code> created by a direct
   ** provisioning action.
   **
   ** @param  account            the user identity the specified
   **                            {@link Application} <code>resource</code> needs
   **                            to be returned.
   ** @param  resource           the {@link Application} to verify if its
   **                            provisioned to <code>account</code>.
   **
   ** @return                    the instance key of the provisioned resource.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected final long provisionResource(final Identity account, final Application resource)
    throws TaskException {

    try {
      // TODO: We need something here that checkes if the object is already
      //       provisioned to the user like following statement
      // objectFacade().getAssociatedUsers(resource.key())
      final ResourceData data = userFacade().provisionResource(account.key(), resource.key());
      return Long.parseLong(data.getOiuKey());
    }
    catch (tcUserNotFoundException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_IDENTITY), account.name()};
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcObjectNotFoundException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_RESOURCE), resource.name()};
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcProvisioningNotAllowedException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_RESOURCE), resource.name()};
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
  }
}
