/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
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
    Subsystem   :   Virtual Resource Management

    File        :   ResourceFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcObjectNotFoundException;

import Thor.API.Operations.tcObjectOperationsIntf;

import com.thortech.xl.dataaccess.tcDataSet;
import com.thortech.xl.dataaccess.tcDataSetException;

import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.orb.dataaccess.tcDataAccessException;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractServiceTask;

import oracle.iam.identity.foundation.naming.FormDefinition;
import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.foundation.offline.Application;
import oracle.iam.identity.foundation.offline.ApplicationAccount;
import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.model.Resource;

////////////////////////////////////////////////////////////////////////////////
// abstract class ResourceFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A factory to create <code>Resource Object</code> and their instance
 ** relationships.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class ResourceFactory extends Factory {

  //////////////////////////////////////////////////////////////////////////////
  // statis final attribute
  //////////////////////////////////////////////////////////////////////////////

  private static final String depenedencyQuery  = "select obj_key, obj_name from obj, obd where obd_parent_key = ? and obj.obj_key = obd_child_key";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a <code>Resource Object</code> provisionable to users.
   **
   ** @param  resourceKey        the internal system identifier of the
   **                            <code>Resource Object</code> to create.
   ** @param  resourceName       the name of the <code>Resource Object</code>.
   ** @param  resourceStatus     the status of the <code>Resource Object</code>
   **
   ** @return                     the wrapper of a <code>Resource Object</code>
   **                             interpreted as a <code>Role</code> by the
   **                             framework.
   */
  public static final ApplicationEntity create(final long resourceKey, final String resourceName, final String resourceStatus) {
    return new ApplicationEntity(resourceKey, resourceName, resourceStatus);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRole
  /**
   ** Creates a <code>Resource Object</code> provisionable to users interpreted
   ** as a role.
   **
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  parent             the <code>Resource Object</code> that owns the
   **                            {@link Resource.Role}.
   **
   ** @throws ResolverException  if the operation fails
   */
  public static final void createRole(final AbstractServiceTask task, final ApplicationEntity parent)
    throws ResolverException {

    final String method = "createRole";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_RESOURCE);

    PreparedStatementUtil   statement = new PreparedStatementUtil();
    statement.setStatement(task.provider(), depenedencyQuery);
    statement.setLong  (1, parent.key());
    try {
      statement.execute();
      tcDataSet dataSet = statement.getDataSet();
      if (dataSet.getRowCount() > 0) {
        for (int i = 0; i < dataSet.getRowCount(); i++) {
          dataSet.goToRow(i);
          parameter[1] = dataSet.getString("obj_key");
          parameter[2] = dataSet.getString("obj_name");
          task.debug(method, TaskBundle.format(TaskMessage.KEY_TORESOLVE, parameter));
          Resource.Role role = new Resource.Role(dataSet.getLong("obj_key"), parameter[2]);
//          role.addParent(parent);
          task.debug(method, TaskBundle.format(TaskMessage.KEY_RESOLVED, parameter));
        }
      }
    }
    catch (tcDataAccessException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
    catch (tcDataSetException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServer
  /**
   ** Loads the <code>Resource Object</code> from Oracle Identity Manager that
   ** is designed as a {@link Resource.Server}
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  objectName         the name of the <code>Resource Object</code> to
   **                            load.
   **
   ** @return                    the {@link Resource.Server} instance the given
   **                            <code>Resource Object</code> belongs to if it's
   **                            exists.
   **
   ** @throws ResolverException  if the given <code>Resource Object</code> is
   **                            not a regular instance.
   */
  public static ApplicationEntity createServer(final AbstractServiceTask task, final String objectName)
    throws TaskException {

    final String method = "createServer";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[4];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_RESOURCE);
    parameter[1] = objectName;
    task.debug(method, TaskBundle.format(TaskMessage.NAME_TORESOLVE, parameter));

    String            column = null;
    ApplicationEntity server = null;
    // create the filter to restrict the result set to retrieve
    Map<String, String> filter = new HashMap<String, String>();
    filter.put(ResourceObject.NAME, parameter[1]);
    try {
      tcResultSet resultSet = facade(task).findObjects(filter);
      int         rowCount  = resultSet.getRowCount();
      if (rowCount == 0)
        throw new ResolverException(TaskError.RESOURCE_NOT_FOUND, parameter);
      if (rowCount > 1)
        throw new ResolverException(TaskError.RESOURCE_AMBIGUOUS, parameter);

      server = new ApplicationAccount(resultSet.getLongValue(ResourceObject.KEY), parameter[1]);

      column = ResourceObject.KEY;
      parameter[2] = resultSet.getStringValue(column);
      task.debug(method, TaskBundle.format(TaskMessage.NAME_RESOLVED, parameter));

      // perfom all pre-condition checks of the Resource Object
      checkPreCondition(task, server, resultSet.getStringValue(ResourceObject.TYPE), resultSet.getStringValue(ResourceObject.ORDER_FOR));

      parameter[0] = TaskBundle.string(TaskMessage.ENTITY_PROCESSDEFINITION);
      parameter[1] = server.name();
      resultSet    = facade(task).getProcessesForObject(server.key());
      rowCount     = resultSet.getRowCount();
      if (rowCount == 0)
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
      if (rowCount > 1)
        throw new TaskException(TaskError.RESOURCE_AMBIGUOUS, parameter);

      column = FormDefinition.KEY;
      server.mainForm(ProcessFormFactory.createForm(task, resultSet.getLongValue(column)));
      server.childForm(ProcessFormFactory.createChildForm(task, server.mainForm()));
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, column);
    }
    catch (tcObjectNotFoundException e) {
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
    return server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkPreCondition
  /**
   ** Returns <code>true</code> if the specified {@link Resource} meets all
   ** pre-condition checks.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  resource           the {@link Resource} wrapper to test.
   ** @param  type               the String that has to be checked against the
   **                            requeired value for "Type" of a
   **                            <code>Resource Object</code>.
   ** @param  orderable          the String that has to be checked against the
   **                            requeired value for "Order For" of a
   **                            <code>Resource Object</code>.
   **
   ** @throws ResolverException  if any pre-comdition is violated
   */
  private static void checkPreCondition(final AbstractServiceTask task, final Application resource, final String type, final String orderable)
    throws ResolverException {

    String[] parameter = new String[4];

    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_RESOURCE);
    parameter[1] = resource.name();

    // perfomr all pre-condition checks of the Resource Object
    parameter[2] = resource.type();
    parameter[3] = type;
    if (!parameter[2].equals(parameter[3]))
      throw new ResolverException(ResolverError.TYPE_MISMATCH, parameter);

    parameter[2] = resource.orderableFor();
    parameter[3] = orderable;
    if (!parameter[2].equals(parameter[3]))
      throw new ResolverException(ResolverError.ORDER_MISMATCH, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facade
  /**
   ** Returns an instance of a Business Facade instance for
   ** <code>Resource Object</code>s associatiated with this provisioning task.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the Business Facade instance.
   **
   ** @return                    a Business Facade instance.
   */
  private static final tcObjectOperationsIntf facade(final AbstractServiceTask task) {
    return task.service(tcObjectOperationsIntf.class);
  }
}