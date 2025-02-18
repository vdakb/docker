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

    File        :   AccessPolicyFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcPolicyNotFoundException;
import Thor.API.Operations.tcAccessPolicyOperationsIntf;

import com.thortech.xl.dataaccess.tcDataSet;
import com.thortech.xl.dataaccess.tcDataSetException;

import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.orb.dataaccess.tcDataAccessException;

import com.thortech.xl.vo.AccessPolicyResourceData;
import com.thortech.xl.vo.PolicyChildTableRecord;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractServiceTask;

import oracle.iam.identity.foundation.naming.Group;
import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.RoleEntity;
import oracle.iam.identity.foundation.offline.AccessPolicy;
import oracle.iam.identity.foundation.offline.ProcessForm;
import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.offline.ApplicationAccount;
import oracle.iam.identity.foundation.offline.EntitlementEntity;

////////////////////////////////////////////////////////////////////////////////
// abstract class AccessPolicyFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** A factory to create <code>Access Policies</code> and their instance
 ** relationships.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class AccessPolicyFactory extends Factory {

  //////////////////////////////////////////////////////////////////////////////
  // statis final attribute
  //////////////////////////////////////////////////////////////////////////////

  private static final String nameQuery   = "select pol.pol_key, pol.pol_name from pol where pol.pol_name = ? ";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Loads all <code>Access Policies</code> from Oracle Identity Manager that
   ** are defined in the specified {@link List}.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  server             the <code>Resource Object</code>s that will be
   **                            provisioned by a <code>Access Policy</code>.
   ** @param  name               the name of the <code>Access Policy</code> to
   **                            fetch from the repository.
   **
   ** @return                    the collection of <code>Access Policies</code>
   **                            belonging to the specified
   **                            {@link ApplicationAccount}s and match the
   **                            specified <code>name</code>.
   **
   ** @throws TaskException      if the operation fails.
   */
  public static AccessPolicy create(final AbstractServiceTask task, final ApplicationEntity[] server, final String name)
    throws TaskException {

    final String method = "create";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_POLICY);

    AccessPolicy policy = null;

    PreparedStatementUtil   statement = new PreparedStatementUtil();
    statement.setStatement(task.provider(), nameQuery);
    statement.setString(1, name);
    try {
      statement.execute();
      tcDataSet dataSet = statement.getDataSet();
      if (dataSet.getRowCount() > 0) {
        for (int i = 0; i < dataSet.getRowCount(); i++) {
          dataSet.goToRow(i);
          parameter[1] = dataSet.getString("pol_name");
          parameter[2] = dataSet.getString("pol_key");
          task.debug(method, TaskBundle.format(TaskMessage.NAME_TORESOLVE, parameter));
          // create the policy wrapper; the constructor adds the created policy
          // to the passed server object
          policy = new AccessPolicy(Long.parseLong(parameter[2]), parameter[1]);
          createRole(task, policy);
          createResource(task, server, policy);
//          createEntitlement(task, policy);
          task.debug(method, TaskBundle.format(TaskMessage.NAME_RESOLVED, parameter));
        }
      }
    }
    catch (tcDataAccessException e) {
      throw new TaskException(e);
    }
    catch (tcDataSetException e) {
      throw new TaskException(e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
    return policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Loads all <code>Access Policies</code> from Oracle Identity Manager that
   ** are defined in the specified {@link List}.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  server             the <code>Resource Object</code>s that will be
   **                            provisioned by a <code>Access Policy</code>.
   **
   ** @return                    the collection of <code>Access Policies</code>
   **                            belonging to the specified
   **                            {@link ApplicationAccount}s.
   **
   ** @throws TaskException      if the operation fails
   */
  public static Set<AccessPolicy> create(final AbstractServiceTask task, final ApplicationAccount[] server)
    throws TaskException {

    final String method = "create";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_POLICY);

    Set<AccessPolicy> result = null;

    PreparedStatementUtil   statement = new PreparedStatementUtil();
    statement.setStatement(task.provider(), prepareServerQuery(server));
    for (int bindingPosition = 0; bindingPosition < server.length; bindingPosition++)
      statement.setLong(bindingPosition + 1, server[bindingPosition].key());

    try {
      statement.execute();
      final tcDataSet dataSet  = statement.getDataSet();
      final int       dataSize = dataSet.getRowCount();
      if (dataSet.getRowCount() > 0) {
        result = new TreeSet<AccessPolicy>();
        for (int i = 0; i < dataSize; i++) {
          dataSet.goToRow(i);
          // we cannot use the foundation naming interface here
          // the query is performend against the database direcly due to
          // increase performance
          parameter[1] = dataSet.getString("pol_key");
          parameter[2] = dataSet.getString("pol_name");
          task.debug(method, TaskBundle.format(TaskMessage.KEY_TORESOLVE, parameter));

          // create the policy wrapper
          AccessPolicy policy = new  AccessPolicy(Long.parseLong(parameter[1]), parameter[2]);
          createResource(task, server, policy);
          createRole(task, policy);
          createEntitlement(task, server, policy);

          // check if the policy meets the requirements
          // the requirements are:
          // 1. the policy muts provision the specified server; this is ensured
          //    by the query condition
          // 2. at least one entitlement mus be provisioned by the policy
          // 3. at leats one usergroup must be assigned to the policy
          if (policy.hasEntitlement() && policy.hasRole()) {
            for (int j = 0; j < server.length; j++)
              server[j].addPolicy(policy);
            result.add(policy);
          }
          task.debug(method, TaskBundle.format(TaskMessage.KEY_RESOLVED, parameter));
        }
      }
    }
    catch (tcDataAccessException e) {
      throw new TaskException(e);
    }
    catch (tcDataSetException e) {
      throw new TaskException(e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns a list of roles that are assigned to the policy.
   ** <p>
   ** The groups are based on the user that is currently logged on.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  policy             the <code>Access Policy</code> that group
   **                            assignment is requested.
   **
   ** @return                    a list of roles that are assigned to the
   **                            policy.
   */
  public static Set<RoleEntity> role(final AbstractServiceTask task, final AccessPolicy policy) {
    return policy.role();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessPolicyFacade
  /**
   ** Returns an instance of a Business Facade instance for
   ** <code>Access Policies</code>.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   **
   ** @return                    a <code>Access Policiy</code> Business Facade
   **                            instance.
   */
  private static tcAccessPolicyOperationsIntf facade(final AbstractServiceTask task) {
    return task.service(tcAccessPolicyOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns a {@link List} of <code>Access Policy</code>s that are assigned
   ** for provisioning to the policy.
   ** <p>
   ** The <code>Access Policy</code>s are based on the user that is currently
   ** logged on.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  policy             the <code>Access Policy</code> that resource
   **                            assignment is requested.
   **
   ** @return                    a {@link List} of <code>Access Policy</code>s
   **                            that are assigned for provisioning to the
   **                            policy.
   */
  public static Set<ApplicationEntity> application(final AbstractServiceTask task, final AccessPolicy policy) {
    return policy.application();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResource
  /**
   ** Creates the {@link List} of <code>Resource Object</code>s that are
   ** assigned for provisioning to the policy.
   ** <p>
   ** The retrieved <code>Resource Object</code>s are based on the user that is
   ** currently logged on.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  policy             the <code>Access Policy</code> that resource
   **                            assignment is requested.
   */
  private static void createResource(final AbstractServiceTask task, final ApplicationEntity[] server, final AccessPolicy policy)
    throws TaskException {

    final String method = "createResource";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final tcResultSet resultSet = facade(task).getAssignedObjects(policy.key());
      final int         rowCount  = resultSet.getRowCount();
      for (int i = 0; i < rowCount; i++) {
        final String resourceName = resultSet.getStringValue(ResourceObject.NAME);
        for (int j = 0; j < server.length; j++) {
          if (resourceName.equals(server[j].name()))
            policy.addResource(server[j]);
        }
      }
    }
    catch (tcPolicyNotFoundException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_POLICY), Long.toString(policy.key()) };
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, ResourceObject.NAME);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRole
  /**
   ** Creates the {@link List} of <code>Role</code>s that triggering the passed
   ** policy.
   ** <p>
   ** The retrieved <code>Role</code>s are based on the user that is currently
   ** logged on.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  policy             the <code>Access Policy</code> that group
   **                            assignment is requested.
   */
  private static void createRole(final AbstractServiceTask task, final AccessPolicy policy)
    throws TaskException {

    final String method = "createRole";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final tcResultSet resultSet = facade(task).getAssignedGroups(policy.key());
      final int         rowCount  = resultSet.getRowCount();
      for (int i = 0; i < rowCount; i++) {
        final String roleName = resultSet.getStringValue(Group.NAME);
        policy.addRole(RoleFactory.create(task, roleName, policy));
      }
    }
    catch (tcPolicyNotFoundException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_POLICY), Long.toString(policy.key()) };
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, Group.NAME);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntitlement
  /**
   * Creates the {@link List} of <code>Entitlement</code>s that are provisioned
   * by the passed policy.
   *
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  server             the <code>Resource Object</code>s that will be
   **                            provisioned by a <code>Access Policy</code>.
   ** @param  policy             the <code>Access Policy</code> where the data
   **                            will be created for and check for consistency.
   **
   ** @throws TaskException      if the operation fails
   */
  private static void createEntitlement(final AbstractServiceTask task, final ApplicationEntity[] server, final AccessPolicy policy)
    throws TaskException {

    final String method = "createEntitlement";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    AccessPolicyResourceData[] data =  createEntitlementData(task, policy);

    // perform the consistency checks
    checkProcessFormConsistency(policy, data, server);
    checkEntitlementFormConsistency(policy, data, server);

    for (int i = 0; i < data.length; i++) {
      // the consistency checks had ensured that we will find at least one
      // child table mapping; no further checks required here so far
      Map mapping = data[i].getChildTables();
      Iterator form = mapping.keySet().iterator();
      while (form.hasNext()) {
        final String                   formKey  = (String)form.next();
        final PolicyChildTableRecord[] record = data[i].getChildTableRecords(formKey);
        // skip all of the further processing if the policy does not define
        // entitlements
        if (record == null || record.length == 0)
          continue;

        // we must transform the field names to a sorted vector to ensure that
        // the order how the values are fetched from the result set are in the
        // same order as this will be done on the Account.
        // This is achieved by transfering all field names to a TreeSet that
        // sorts the entries in natural order and transforme this collection
        // back in a array of Objects to facilitate the iteration. The data
        // provided by getRecordData() returning a Map. Therefore further
        // casting to classes is not necessary.
        // we probe with record[0] in the assumption it must be always the
        // same structure
        Set<? extends String> sorter = new TreeSet<String>(record[0].getRecordData().keySet());
        String[] field = new String[sorter.size()];
        field = sorter.toArray(field);

        // create an array of Entitlement that big enough to hold all records
        // for data.
        // Entitlement content will hashed by the wrapper so dont bother
        // that we use a variable on the stack instead of creating every
        // time a new one in case a field set will be fetched from the
        // database
        final EntitlementEntity[] entitlement = new EntitlementEntity[record.length];

        // create an array of string that big enough to hold all values for
        // the columns contained in the fieldSet.
        // Entitlement content will hashed by the wrapper so dont bother
        // that we use a variable on the stack instead of creating every
        // time a new one in case a field set will be fetched from the
        // database
        final String[] value = new String[field.length];

        for (int k = 0; k < record.length; k++) {
          // navigate to the record and obtain the data
          Map entry = record[k].getRecordData();
          // the sequence how the values has to be fetched from the result set
          // are deterministic see above; we can build a simple for loop to
          // hold the values
          for (int l = 0; l < field.length; l++)
            value[l] = (String)entry.get(field[l]);

          // create the entitlement wrapper that aggregates all single values in
          // one hash value; the key of the row is not interesting here because
          // a policy is a data definition not data instance
          // to be consistent we pass the record number of the policy data
          // definition as the row identifier to the entitlement constructor
          // TODO: !!!We don't know the correct server instance!!!
          entitlement[k] = new EntitlementEntity(null, k, value);
        }
        // the entitlement mapping is using the key of the process form instead
        // of the process form name.
        // the Access Policies doesn't know the name of the process form at
        // chiled table level where they are providing the data for, only the
        // key is know by the policies. It's possible to resolve the name by
        // passing the key to the ProcessFormFactory, but this will decrease the
        // performance. Only for convinient in the matching phase we use the key
        // of the process form. The Account Entitlement Mining will do this too.
        policy.addEntitlement(formKey, entitlement);
      }
    }
    task.trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntitlement
  /**
   ** Check if the Access Policy that is described by
   ** <code>AccessPolicyResourceData[]</code> is consistent to
   ** the <code>Resource Object</code>s provided by <code>server</code>.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  policy             the <code>Access Policy</code> where the data
   **                            will be created for and check for consistency.
   **
   ** @throws TaskException      if any inconsistency is detected
   */
  private static AccessPolicyResourceData[] createEntitlementData(final AbstractServiceTask task, final AccessPolicy policy)
    throws TaskException {

    AccessPolicyResourceData[] data = null;
    try {
      data = facade(task).getDataSpecifiedForObjects(policy.key());
    }
    catch (tcPolicyNotFoundException e) {
      final String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_POLICY), policy.name() };
      throw new ResolverException(TaskError.RESOURCE_NOT_FOUND, parameter);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkProcessFormConsistency
  /**
   ** Pre-CCheck at Process Form Level between provided
   ** <code>AccessPolicyResourceData</code> and the
   ** <code>Resource Object</code>s represented by <code>server</code>
   **
   ** @param  policy             the <code>Access Policy</code> where the data
   **                            will be check for consistency.
   ** @param  data               the data to check for consistency
   ** @param  server             the <code>Resource Object</code>s as the
   **                            requirement definition.
   **
   ** @throws TaskException      if any inconsistency is detected
   */
  private static void checkProcessFormConsistency(final AccessPolicy policy, final AccessPolicyResourceData[] data, final ApplicationEntity[] server)
    throws TaskException {

    // pre-check if the Access Policy does exact the same number of forms
    // that are specified by the list of servers
    if (data.length != server.length) {
      final String[] parameter = { policy.name(), String.valueOf(server.length), String.valueOf(data.length) };
      throw new ResolverException(ResolverError.POLICY_FORMSIZE_MISMATCH, parameter);
    }

    // transfer all parent process forms of the specified server resources to
    // a collection that we can easily check that the Access Policy is
    // containing data defininitions only for thoose
    List<ProcessForm> processForm = new ArrayList<ProcessForm>(server.length);
    for (int i = 0; i < server.length; i++)
      processForm.add(server[i].mainForm());

    // pre-check if the Access Policy does only refere to process forms that
    // are defined on the provided list of resources
    ProcessForm conflict = null;
    for (int i = 0; i < data.length; i++) {
      ProcessForm check = new ProcessForm(data[i].getFormDefinitionKey(), data[i].getFormName());
      if (!processForm.contains(check)) {
        conflict = check;
        break;
      }
    }
    // abort all if a conflict is detected
    if (conflict != null) {
      final String[] parameter = { policy.name(), conflict.name() };
      throw new ResolverException(ResolverError.POLICY_FORM_MISMATCH, parameter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkEntitlementFormConsistency
  /**
   ** Pre-Check at Entitlement Process Form Level between provided
   ** <code>AccessPolicyResourceData</code> and the
   ** <code>Resource Object</code>s represented by <code>server</code>
   **
   ** @param  policy             the <code>Access Policy</code> where the data
   **                            will be check for consistency.
   ** @param  data               the data to check for consistency
   ** @param  server             the <code>Resource Object</code>s as the
   **                            requirement definition.
   **
   ** @throws ResolverException  if any inconsistency is detected
   */
  private static void checkEntitlementFormConsistency(final AccessPolicy policy, final AccessPolicyResourceData[] data, final ApplicationEntity[] server)
    throws ResolverException {

    // transfer all child process forms of the specified server resources to
    // a collection that we can easily check that the Access Policy is
    // containing data defininitions only for thoose
    List<ProcessForm> processForm = new ArrayList<ProcessForm>(server.length);
    for (int i = 0; i < server.length; i++) {
      ProcessForm[] child = server[i].childForm();
      for (int j = 0; j < child.length; j++)
        processForm.add(child[j]);
    }
    // pre-check if the Access Policy does only refere to child process forms
    // that are defined on the provided list of resources
    ProcessForm conflict = null;
    for (int i = 0; i < data.length; i++) {
      Map mapping = data[i].getChildTables();
      Iterator j = mapping.keySet().iterator();
      while (j.hasNext()) {
        final String formKey = (String)j.next();
        ProcessForm check = new ProcessForm(formKey, (String)mapping.get(formKey));
        if (!processForm.contains(check)) {
          conflict = check;
          break;
        }
      }
    }
    // abort all if a conflict is detected
    if (conflict != null) {
      final String[] parameter = { policy.name(), conflict.name() };
      throw new ResolverException(ResolverError.POLICY_FORM_MISMATCH, parameter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareDataQuery
  /**
   ** Build the SQL Query to fetch affected entries.
   **
   ** @return                    the SQL Query to fetch affected entries.
   */
  private static String prepareServerQuery(final ApplicationAccount[] server) {
    StringBuffer query = new StringBuffer();
    query.append("select pol.pol_key, pol.pol_name from pol, pop, obj where ");

    if (server.length == 1) {
      query.append("obj.obj_key = ? ");
    }
    else {
      query.append("obj.obj_key in (");
      for (int i = 0; i < server.length; i++) {
        if (i > 0)
          query.append(", ");
        query.append("?");
      }
      query.append(") ");
    }
    query.append("and pop.obj_key = obj.obj_key and pop.pol_key = pol.pol_key");
    return query.toString();
  }
}