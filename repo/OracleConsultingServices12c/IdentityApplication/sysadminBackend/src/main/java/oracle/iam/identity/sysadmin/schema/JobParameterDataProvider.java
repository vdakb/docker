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

    File        :   JobParameterDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JobParameterDataProvider.


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

import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.JobParameter;
import oracle.iam.scheduler.vo.ScheduledTask;

import oracle.iam.scheduler.api.SchedulerService;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.utils.SerializationUtils;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class JobParameterDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
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
public class JobParameterDataProvider extends AbstractDataProvider<JobParameterAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTEXT_MISSING  = "job.missing";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2172602312959424199")
  private static final long   serialVersionUID = 4665487207649440451L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JobParameterDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JobParameterDataProvider() {
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
   **                            element is of type {@link JobParameterAdapter}.
   */
  @Override
  public List<JobParameterAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // default search will be done by name
    final SearchCriteria criteriaName = getAttrCriteria(searchCriteria, JobParameterAdapter.FK_JOB);
    if (criteriaName == null)
      throw new OIMRuntimeException(resourceBackendValue(CONTEXT_MISSING));

    // default search will be done by name
    final SearchCriteria criteriaTask = getAttrCriteria(searchCriteria, JobParameterAdapter.FK_TASK);
    if (criteriaTask == null)
      throw new OIMRuntimeException(resourceBackendValue(CONTEXT_MISSING));

    Map<String, JobParameterAdapter> add = SerializationUtils.deserializeFromString((String)getCriteriaAttrValue(searchCriteria, JobParameterAdapter.ADD), LinkedHashMap.class);
    Map<String, JobParameterAdapter> mod = SerializationUtils.deserializeFromString((String)getCriteriaAttrValue(searchCriteria, JobParameterAdapter.MOD), LinkedHashMap.class);
    Map<String, JobParameterAdapter> del = SerializationUtils.deserializeFromString((String)getCriteriaAttrValue(searchCriteria, JobParameterAdapter.DEL), LinkedHashMap.class);

    // ensure the NPE can never happens if the result set is initially received
    add = add == null ? new LinkedHashMap<String, JobParameterAdapter>() : add;
    mod = mod == null ? new LinkedHashMap<String, JobParameterAdapter>() : mod;
    del = del == null ? new LinkedHashMap<String, JobParameterAdapter>() : del;

    final List<JobParameterAdapter> batch   = new ArrayList<JobParameterAdapter>();
    final SchedulerService          service = service(SchedulerService.class);
    try {
      // at first ask the task definition to get the parameters for fulfillment
      // this step is required due to the fact that the task definition can be
      // change out-of-band to the jobs base on that task
      final ScheduledTask             task       = service.lookupScheduledTask(criteriaTask.getSecondArgument().toString());
      final Map<String, JobParameter> definition = task.getParameters();
      // secondary ask the job definition to get the parameters for fulfillment
      // this step is required due to the fact that the job instance can have
      // parameters that needs to be changed accordingly to new or drop
      // parameters in the task definition
      final JobDetails                details = service.getJobDetail(criteriaName.getSecondArgument().toString());
      // may be we have a new one hance there cannot be any parameter at the
      // time being than create a empty map to avoid NPE
      final Map<String, JobParameter> runtime = details == null ? new HashMap<String, JobParameter>() : details.getParams();
      // the sequence for generate the parameter key on the fly
      int sequence = 0;
      // merge the keys of the obtained parameters to a unqiue set
      final List<String> sorted  = CollectionUtility.sortedList(CollectionUtility.union(definition.keySet(), runtime.keySet()));
      if (sorted != null && sorted.size() > 0) {
        // now walk through the parameter definition an declare the state of
        // each parameter found in the runtime
        // 1. if a parameter is define in the definition set and not found in
        //    the set of runtime parameters it must be new.
        // 2. if a parameter is not existing in the definition set but found in
        //    the set of runtime parameters it must be deleted.
        // 3. if a parameter with the same name is found in both sets the value
        //    to use is the value of the runtime set
        for (String cursor : sorted) {
          JobParameterAdapter mab = null;
          if (definition.keySet().contains(cursor) && !runtime.keySet().contains(cursor)) {
            mab = JobParameterAdapter.build(definition.get(cursor));
            mab.setPendingAction(JobParameterAdapter.ADD);
            if (mab.getParameterKey() == null)
              // set the parameter key to keep them unique
              mab.setParameterKey(String.valueOf(--sequence));
          }
          else if (!definition.keySet().contains(cursor) && runtime.keySet().contains(cursor)) {
            mab = JobParameterAdapter.build(runtime.get(cursor));
            mab.setPendingAction(JobParameterAdapter.DEL);
            if (mab.getParameterKey() == null)
              // set the parameter key to keep them unique
              mab.setParameterKey(String.valueOf(--sequence));
          }
          else {
            mab = JobParameterAdapter.build(runtime.get(cursor));
            if (mab.getParameterKey() == null)
              // set the parameter key to keep them unique
              mab.setParameterKey(String.valueOf(--sequence));
            if (mod.containsKey(cursor)) {
              mab.setPendingAction(JobParameterAdapter.MOD);
            }
          }
          batch.add(mab);
        }
      }
      return new PagedArrayList<JobParameterAdapter>(batch, 1, batch.size());
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }
}