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

    File        :   JobDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JobDataProvider.


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

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.platform.authopss.exception.AccessDeniedException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.SearchResult;

import oracle.iam.scheduler.exception.SchedulerException;

import oracle.iam.scheduler.api.SchedulerService;

import oracle.iam.scheduler.exception.LastModifyDateNotSetException;
import oracle.iam.scheduler.exception.SchedulerAccessDeniedException;
import oracle.iam.scheduler.exception.RequiredParameterNotSetException;
import oracle.iam.scheduler.exception.ParameterValueTypeNotSupportedException;
import oracle.iam.scheduler.exception.IncorrectScheduleTaskDefinationException;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.PagedArrayList;
import oracle.iam.ui.platform.model.common.OIMProgrammaticVO;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

////////////////////////////////////////////////////////////////////////////////
// class JobDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~
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
public class JobDataProvider extends JMXDataProvider<JobAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  private static final String OPERATOR_INVALID = "job.operator";
  private static final String CRITERIA_INVALID = "job.criteria";

  /** the criteria name to use for a search by task name */
  private static final String CRITERIA_TASK    = "TASK_FILTER";

  /** the criteria name to use for a search by job name */
  private static final String CRITERIA_NAME    = "JOB_NAME_FILTER";

  /** the criteria name to use for a search by job status */
  private static final String CRITERIA_STATUS  = "JOB_STATUS_FILTER";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5313737212301742661")
  private static final long   serialVersionUID = 4791825906809794114L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  protected JobStatus[]       status;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JobDataProvider</code> data access object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JobDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JobDataProvider</code> which use the passed
   ** {@link OIMProgrammaticVO} to localize static value lists.
   **
   ** @param  viewDefinition     the {@link OIMProgrammaticVO} used to localize
   **                            static value lists.
   **                            <br>
   **                            Allowed object is {@link OIMProgrammaticVO}.
   */
  public JobDataProvider(final OIMProgrammaticVO viewDefinition) {
    // ensure inheritance
    super();

    // initalize instance attributes
    this.status = JobStatus.build(viewDefinition);
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
   **                            element is of type {@link JobAdapter}.
   */
  @Override
  public List<JobAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    final SearchCriteria   criteria = convertCriteria(searchCriteria);
    final SchedulerService service  = service(SchedulerService.class);
    try {
      final Map<String, SearchResult> collection = service.searchJobs(criteria);
      return paginate(control, collection, collection == null ? 0 : collection.size());
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   create (overridden)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object creation.
   **
   ** @param  mab                the model adapter bean, with attributes set.
   **                            <br>
   **                            Allowed object is {@link JobAdapter}.
   */
  @Override
  public void create(final JobAdapter mab) {
    final JobDetails       instance = JobAdapter.build(mab);
    final SchedulerService service  = service(SchedulerService.class);
    try {
      service.addJob(instance);
    }
    catch (AccessDeniedException e) {
      throw new OIMRuntimeException(e.getLocalizedMessage());
    }
    catch (SchedulerAccessDeniedException e) {
      throw new OIMRuntimeException(e.getLocalizedMessage());
    }
    catch (IncorrectScheduleTaskDefinationException e) {
      throw new OIMRuntimeException(e.getLocalizedMessage());
    }
    catch (ParameterValueTypeNotSupportedException e) {
      throw new OIMRuntimeException(e.getLocalizedMessage());
    }
    catch (RequiredParameterNotSetException e) {
      throw new OIMRuntimeException(String.format("%s: %s", e.getParameterName(), e.getLocalizedMessage()));
    }
    catch (SchedulerException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   update (overridden)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object update.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   **                            <br>
   **                            Allowed object is {@link JobAdapter}.
   */
  @Override
  public void update(final JobAdapter mab) {
    // if this instance is pretty new we don't have a key now hence we populate
    // the value holder accordingly to this state
    final JobDetails       instance = JobAdapter.build(mab);
    final SchedulerService service  = service(SchedulerService.class);
    try {
      service.updateJob(instance);
    }
    catch (AccessDeniedException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SchedulerAccessDeniedException e) {
      throw new OIMRuntimeException(e);
    }
    catch (IncorrectScheduleTaskDefinationException e) {
      throw new OIMRuntimeException(e);
    }
    catch (ParameterValueTypeNotSupportedException e) {
      throw new OIMRuntimeException(e);
    }
    catch (RequiredParameterNotSetException e) {
      throw new OIMRuntimeException(e);
    }
    catch (LastModifyDateNotSetException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SchedulerException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   delete (overridden)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object delete.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void delete(final JobAdapter mab) {
    final JobDetails details = find(mab.getName());
    // perform the delete
    final SchedulerService service = service(SchedulerService.class);
    try {
      service.deleteJob(details);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   lookup (overridden)
  /**
   ** Return a specific backend object identified by the name.
   ** <p>
   ** The ModelAdapterBean argument implementation class must correspond to the
   ** DataProvider implementation. In other words, this method implemented in
   ** AttributeDataProvider expects a AttributeAdapterBean as an argument.
   ** <br>
   ** If the DataProvider and ModelAdapterBean types do not match, and exception
   ** will be thrown.
   ** <p>
   ** The ModelAdapterBean must has its "key" fields set. All other fields are
   ** ignored. In most cases, the field field is simply the name.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   **
   ** @return                    object identified by the given name.
   */
  @Override
  public JobAdapter lookup(final JobAdapter mab) {
    // perform the search
    final SchedulerService service = service(SchedulerService.class);
    try {
      service.getJobDetail(mab.getName());
      return mab;
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables a scheduled job in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  instance           the name of the job instance to anable.
   */
  public void enable(final String instance) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables a scheduled job in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  instance           the name of the job instance to anable.
   */
  public void disable(final String instance) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Starts a scheduled job in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  instance           the name of the job instance to start.
   */
  public void start(final String instance) {
    final SchedulerService service = service(SchedulerService.class);
    try {
      int status = service.getStatusOfJob(instance);
      if (status == JobStatus.RUNNING)
        ;
      else {
        service.triggerNow(instance);
        do {
          // obtain the status of the job after it was triggered
          status = service.getStatusOfJob(instance);
        } while (status == JobStatus.STARTED || status == JobStatus.STOPPED);
      }
    }
    catch (SchedulerAccessDeniedException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SchedulerException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Stops a scheduled job in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  instance           the name of the job instance to stop.
   */
  public void stop(final String instance) {
    final SchedulerService service = service(SchedulerService.class);
    try {
      int status = service.getStatusOfJob(instance);
      // check if the job is running
      if (status == JobStatus.RUNNING) {
        service.stopJob(instance);
      }
    }
    catch (SchedulerAccessDeniedException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SchedulerException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pause
  /**
   ** Pause a scheduled job in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  instance           the name of the job instance to stop.
   */
  public void pause(final String instance) {
    final SchedulerService service = service(SchedulerService.class);
    try {
      int status = service.getStatusOfJob(instance);
      // check if the job is running
      if (status == JobStatus.RUNNING) {
        service.pauseJob(instance);
      }
    }
    catch (SchedulerAccessDeniedException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SchedulerException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   convertCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Job.
   **
   ** @param searchCriteria      the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   **
   ** @return                    the search criteria mapped to the OIM
   **                            declarations.
   **                            Possible object is {@link SearchCriteria}.
   */
  private SearchCriteria convertCriteria(final SearchCriteria searchCriteria) {
    // default search will be done by name
    SearchCriteria criteria = searchCriteria;
    if (criteria == null) {
      criteria = new SearchCriteria(JobAdapter.PK, "", SearchCriteria.Operator.BEGINS_WITH);
    }

    Object lhs = criteria.getFirstArgument();
    if (lhs instanceof SearchCriteria) {
      lhs = convertCriteria((SearchCriteria)lhs);
    }
    else if (lhs instanceof String) {
      lhs = convertAttributeName((String)lhs);
    }

    Object rhs = criteria.getSecondArgument();
    if (rhs instanceof SearchCriteria) {
      rhs = convertCriteria((SearchCriteria)rhs);
    }

    // if the status is part of the search criteria to apply we need to convert
    // the string passed for the right hand argument to the proper type of
    // integer to satify the requirements of the API
    if (lhs.equals(CRITERIA_STATUS))
      rhs = Integer.valueOf(rhs.toString());

    // apply the correct search operator the API understands
    final SearchCriteria.Operator op = criteria.getOperator();
    switch (op) {
      case EQUAL       : return new SearchCriteria(lhs, rhs,                        SearchCriteria.Operator.EQUAL);
      case NOT_EQUAL   : return new SearchCriteria(lhs, rhs,                        SearchCriteria.Operator.NOT_EQUAL);
      case CONTAINS    : return new SearchCriteria(lhs, "*" + rhs.toString() + "*", SearchCriteria.Operator.EQUAL);
      case ENDS_WITH   : return new SearchCriteria(lhs, "*" + rhs.toString(),       SearchCriteria.Operator.EQUAL);
      case BEGINS_WITH : return new SearchCriteria(lhs, rhs.toString() + "*",       SearchCriteria.Operator.EQUAL);
      default          : throw new OIMRuntimeException(String.format(resourceBackendValue(OPERATOR_INVALID), op.toString()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   convertAttributeName
  /**
   ** Converts the attribute name defined in the ADF view query criteria to the
   ** natural names exposed by the OIM kernel API.
   **
   ** @param  outboundName       the attribute name defined in the ADF view
   **                            query criteria.
   **
   ** @return                    the attribute name exposed by the OIM kernel
   **                            API if any.
   */
  private String convertAttributeName(final String outboundName) {
    // prevent bogus input
    String inboundName = outboundName;
    if (inboundName == null)
      return null;

    inboundName = inboundName.trim();
    if (inboundName.equals(JobAdapter.PK)) {
      inboundName = CRITERIA_NAME;
    }
    else if (inboundName.equals(JobAdapter.STATUS)) {
      inboundName = CRITERIA_STATUS;
    }
    else if (inboundName.equals(JobAdapter.TASK) || inboundName.equals(JobAdapter.FK_TASK)) {
      inboundName = CRITERIA_TASK;
    }
    else {
      throw new OIMRuntimeException(String.format(resourceBackendValue(CRITERIA_INVALID), outboundName));
    }
    return inboundName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginate
  /**
   ** Factory method to create a paginated result set from the given
   ** resultSet map.
   ** <b>
   ** Each element in the returned list is a {@link ResourceObjectAdapter}.
   **
   ** @param  control            contains pagination, sort attribute, and sort
   **                            direction.
   **                            Allowed object is {@link HashMap}.
   ** @param  resultSet          the {@link Map} provider to obtain the
   **                            attribute values from.
   **                            Allowed object is {@link Map}.
   ** @param  totalRowCount      the size of the entire result set
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a paginated result set from the {@link Map}.
   */
  private List<JobAdapter> paginate(final HashMap<String, Object> parameter, final Map<String, SearchResult> resultSet, final int totalRowCount) {
    final List<JobAdapter> batch = new ArrayList<JobAdapter>();
    if (resultSet == null)
      return batch;

    int rangeStart = 0;
    int rangeEnd   = 27;
    if (parameter != null && parameter.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)parameter.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    if (parameter != null && parameter.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd = (Integer)parameter.get(ConstantsDefinition.SEARCH_ENDROW);
      if (rangeEnd > totalRowCount)
        rangeEnd = totalRowCount;
    }
    final List<String> sorted  = CollectionUtility.sortedList(resultSet.keySet());
    for (int i = rangeStart - 1; i < rangeEnd; i++) {
      final SearchResult cursor = resultSet.get(sorted .get(i));
      final JobAdapter   mab    = JobAdapter.build(cursor.getTaskDetail(), cursor.getJobHistory(), cursor.getTaskStatus());
      mab.setStatusDecode(this.status[mab.getStatus().intValue()].getDecoded());
      batch.add(mab);
    }
    return new PagedArrayList<JobAdapter>(batch, rangeStart, totalRowCount);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Returns an existing job from Oracle Identity Manager through the
   ** discovered {@link SchedulerService}.
   **
   ** @param  instance           the name of the job instance to lookup.
   **
   ** @return                    the {@link JobDetails} for the specified
   **                            instance.
   */
  private JobDetails find(final String instance) {
    final SchedulerService service = service(SchedulerService.class);
    try {
      return service.getJobDetail(instance);
    }
    catch (SchedulerException e) {
      throw new OIMRuntimeException(e);
    }
  }
}