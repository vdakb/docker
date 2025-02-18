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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Administration Management

    File        :   TaskParameterDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TaskParameterDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.scheduler.vo.JobParameter;
import oracle.iam.scheduler.vo.ScheduledTask;

import oracle.iam.scheduler.api.SchedulerService;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.utils.SerializationUtils;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class TaskParameterDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by System Administration Management customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class TaskParameterDataProvider extends AbstractDataProvider<TaskParameterAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTEXT_MISSING  = "tsk.missing";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-424194373642471214")
  private static final long   serialVersionUID = 6686251957102825448L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TaskParameterDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TaskParameterDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   search (DataProvider)
  /**
   ** Return a list of backend objects based on the given filter criteria.
   **
   ** @param  searchCriteria     the OIM search criteria to submit.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  requested          the {@link Set} of attributes to be returned in
   **                            search results.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  control            contains pagination, sort attribute, and sort
   **                            direction.
   **                            <br>
   **                            Allowed object is {@link HashMap} where each
   **                            element is of type [@link String} for the key
   **                            and {@link Object} for the value.
   **
   ** @return                    a list of backend objects.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type
   **                            {@link TaskParameterAdapter}.
   */
  @Override
  public List<TaskParameterAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // default search will be done by name
    final SearchCriteria criteria = getAttrCriteria(searchCriteria, TaskParameterAdapter.FK_TASK);
    if (criteria == null)
      throw new OIMRuntimeException(resourceBackendValue(CONTEXT_MISSING));

    Map<String, TaskParameterAdapter> add = SerializationUtils.deserializeFromString((String)getCriteriaAttrValue(searchCriteria, TaskParameterAdapter.ADD), LinkedHashMap.class);
    Map<String, TaskParameterAdapter> mod = SerializationUtils.deserializeFromString((String)getCriteriaAttrValue(searchCriteria, TaskParameterAdapter.MOD), LinkedHashMap.class);
    Map<String, TaskParameterAdapter> del = SerializationUtils.deserializeFromString((String)getCriteriaAttrValue(searchCriteria, TaskParameterAdapter.DEL), LinkedHashMap.class);

    // ensure the NPE can never happens if the result set is initially received
    add = add == null ? new LinkedHashMap<String, TaskParameterAdapter>() : add;
    mod = mod == null ? new LinkedHashMap<String, TaskParameterAdapter>() : mod;
    del = del == null ? new LinkedHashMap<String, TaskParameterAdapter>() : del;

    final List<TaskParameterAdapter> batch   = new ArrayList<TaskParameterAdapter>();
    final SchedulerService           service = service(SchedulerService.class);
    try {
      final ScheduledTask             details = service.lookupScheduledTask(criteria.getSecondArgument().toString());
      final Map<String, JobParameter> runtime = details.getParameters();
      if (runtime != null && runtime.size() > 0) {
        final List<String> sorted  = CollectionUtility.sortedList(runtime.keySet());
        for (String cursor : sorted) {
          TaskParameterAdapter mab  = TaskParameterAdapter.build(runtime.get(cursor));
           if (del.containsKey(mab.getName())) {
            mab = del.get(mab.getName());
            mab.setPendingAction(TaskParameterAdapter.DEL);
          }
          else if (mod.containsKey(mab.getName())) {
            mab = mod.get(mab.getName());
            mab.setPendingAction(TaskParameterAdapter.MOD);
          }
          batch.add(mab);
        }
      }
      return new PagedArrayList<TaskParameterAdapter>(batch, 1, runtime.size());
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }
}