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

    File        :   JobHistoryDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JobHistoryDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.scheduler.vo.JobHistory;

import oracle.iam.scheduler.api.SchedulerService;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.ui.platform.exception.OIMRuntimeException;
import oracle.iam.ui.platform.model.common.OIMProgrammaticVO;

////////////////////////////////////////////////////////////////////////////////
// class JobHistoryDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
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
public class JobHistoryDataProvider extends AbstractDataProvider<JobHistoryAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTEXT_MISSING  = "job.missing";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9048280458899311223")
  private static final long   serialVersionUID = 4561456978009775261L;

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
   ** Constructs an empty <code>JobHistoryDataProvider</code> data access object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JobHistoryDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JobHistoryDataProvider</code> which use the
   ** passed {@link OIMProgrammaticVO} to localize static value lists.
   **
   ** @param  viewDefinition     the {@link OIMProgrammaticVO} used to localize
   **                            static value lists.
   **                            <br>
   **                            Allowed object is {@link OIMProgrammaticVO}.
   */
  public JobHistoryDataProvider(final OIMProgrammaticVO viewDefinition) {
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
   **                            element is of type {@link JobHistoryAdapter}.
   */
  @Override
  public List<JobHistoryAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // default search will be done by name
    final SearchCriteria criteria = getAttrCriteria(searchCriteria, JobHistoryAdapter.FK);
    if (criteria == null)
      throw new OIMRuntimeException(resourceBackendValue(CONTEXT_MISSING));

    final SchedulerService service = service(SchedulerService.class);
    try {
      final List<JobHistory> collection = service.getHistoryOfJob(criteria.getSecondArgument().toString());
      return paginate(control, collection);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginate
  /**
   ** Factory method to create a paginated result set from the given
   ** resultSet list.
   ** <b>
   ** Each element in the returned list is a {@link JobHistoryAdapter}.
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
   ** @return                    a paginated result set from the {@link List}.
   */
  private List<JobHistoryAdapter> paginate(final HashMap<String, Object> parameter, final List<JobHistory> resultSet) {
    final List<JobHistoryAdapter> batch = new ArrayList<JobHistoryAdapter>();
    if (resultSet == null)
      return batch;

    int rangeStart = 0;
    int rangeEnd   = 27;
    if (parameter != null && parameter.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)parameter.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    if (parameter != null && parameter.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd = (Integer)parameter.get(ConstantsDefinition.SEARCH_ENDROW);
      if (rangeEnd > resultSet.size())
        rangeEnd = resultSet.size();
    }
    for (int i = rangeStart - 1; i < rangeEnd; i++) {
      final JobHistory        cursor = resultSet.get(i);
      final JobHistoryAdapter mab    = JobHistoryAdapter.build(cursor);
      mab.setStatusDecode(this.status[mab.getStatus().intValue()].getDecoded());
      batch.add(mab);
    }
    return new PagedArrayList<JobHistoryAdapter>(batch, rangeStart, resultSet.size());
  }
}