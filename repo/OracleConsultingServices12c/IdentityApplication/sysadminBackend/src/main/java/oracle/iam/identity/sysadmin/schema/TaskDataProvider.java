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

    File        :   TaskDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TaskDataProvider.


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

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.scheduler.vo.ScheduledTask;

import oracle.iam.scheduler.api.SchedulerService;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class TaskDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~
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
public class TaskDataProvider extends JMXDataProvider<TaskAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String OPERATOR_INVALID  = "tsk.operator";
  private static final String CRITERIA_INVALID  = "tsk.criteria";
  
  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7147324701678165206")
  private static final long   serialVersionUID = 8955888213680504392L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TaskDataProvider</code> data access object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TaskDataProvider() {
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
   **                            element is of type {@link TaskAdapter}.
   */
  @Override
  public List<TaskAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    final SchedulerService service  = service(SchedulerService.class);
    try {
      final Map<String, ScheduledTask> collection = CollectionUtility.<ScheduledTask>caseInsensitiveMap();
      final Map<String, String>        finder     = service.listScheduledTasks();
      if (finder != null && finder.size() > 0) {
        final Set<String> filter = filter(convertCriteria(searchCriteria), finder.keySet(), CollectionUtility.unmodifiable(finder.keySet()));
        for (String cursor : filter) {
          collection.put(cursor, service.lookupScheduledTask(cursor));
        }
      }
      return paginate(control, collection);
    }
    catch (Exception e) {
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
    return (searchCriteria == null) ? new SearchCriteria(TaskAdapter.PK, "", SearchCriteria.Operator.BEGINS_WITH) : searchCriteria;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginate
  /**
   ** Factory method to create a paginated result set from the given
   ** resultSet map.
   ** <b>
   ** Each element in the returned list is a {@link TaskAdapter}.
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
  private List<TaskAdapter> paginate(final HashMap<String, Object> parameter, final Map<String, ScheduledTask> resultSet) {
    final List<TaskAdapter> batch = new ArrayList<TaskAdapter>();
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
    final List<String> sorted  = CollectionUtility.sortedList(resultSet.keySet());
    for (int i = rangeStart - 1; i < rangeEnd; i++) {
      final TaskAdapter mab = TaskAdapter.build(resultSet.get(sorted.get(i)));
      batch.add(mab);
    }
    return new PagedArrayList<TaskAdapter>(batch, rangeStart, sorted.size());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methode:   filter
  /**
   ** Creates a {@link Set} of string which contains the filtered result of the
   ** {@link  Set} <code>collection</code> after applying the provided search
   ** criteria}
   **
   ** @param  searchCriteria     the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   ** @param  filter             the result of the filter algorithm evaluated so
   **                            far.
   ** @param  dictionary         the {@link Set} of string acting as the
   **                            dictionary to apply the search criteria.
   **
   ** @return                    the filtering result.
   */
  private Set<String> filter(final SearchCriteria criteria, Set<String> filter, final Set<String> dictionary) {
    final Object lhs = criteria.getFirstArgument();
    final Object rhs = criteria.getSecondArgument();
    if ((lhs instanceof String) && (rhs instanceof String))
      return match(criteria, dictionary);

    if (!((lhs instanceof SearchCriteria) && (rhs instanceof SearchCriteria)))
      throw new OIMRuntimeException(String.format(resourceBackendValue(CRITERIA_INVALID), criteria.toString()));

    final Set<String> lhsFilter = filter((SearchCriteria)lhs, filter, dictionary);
    final Set<String> rhsFilter = filter((SearchCriteria)rhs, filter, dictionary);
    switch (criteria.getOperator()) {
      case AND : filter = CollectionUtility.intersection(lhsFilter, rhsFilter);
                 break;
      case OR  : filter = CollectionUtility.union(lhsFilter, rhsFilter);
                 break;
      default  : throw new OIMRuntimeException(String.format(resourceBackendValue(OPERATOR_INVALID), criteria.getOperator().toString()));
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   match
  /**
   ** Creates a {@link Set} of string which contains the matched result of the
   ** {@link  Set} <code>collection</code> after applying the provided search
   ** criteria.
   **
   ** @param  searchCriteria     the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   ** @param  dictionary         the {@link Set} of string acting as the
   **                            dictionary to apply the search criteria.
   **
   ** @return                    the matching result.
   */
  private Set<String> match(final SearchCriteria criteria, final Set<String> collection) {
    final Set<String>             predicate  = CollectionUtility.caseInsensitiveSet();
    final String                  expression = criteria.getSecondArgument().toString();
    final SearchCriteria.Operator operator   = criteria.getOperator();
    for (String cursor : collection) {
      if (match(cursor, operator, expression))
        predicate.add(cursor);
    }
    return predicate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   match
  /**
   ** Creates a {@link Set} of string which contains the matched result of the
   ** {@link  Set} <code>collection</code> after applying the provided search
   ** operator with the specified <code>expression</code>.
   **
   ** @param  subject            the string to match.
   ** @param  operator           the search operator mapped to the ADF
   **                            Entity/View Definitions.
   ** @param  expression         the string expression to match.
   **
   ** @return                    <code>true</code> if the subject meets the
   **                            requirements aof <code>operator</code> and
   **                            <code>expression</code>; otherwise
   **                            <code>false</code>.
   */
  final boolean match(final String subject, final SearchCriteria.Operator operator, final String expression) {
    switch (operator) {
      case EQUAL               : return StringUtility.isEqual(subject, expression);
      case NOT_EQUAL           : return !StringUtility.isEqual(subject, expression);
      case CONTAINS            : return StringUtility.contains(subject, expression);
      case DOES_NOT_CONTAIN    : return !StringUtility.contains(subject, expression);
      case BEGINS_WITH         : return StringUtility.startsWith(subject, expression);
      case DOES_NOT_BEGIN_WITH : return !StringUtility.startsWith(subject, expression);
      case ENDS_WITH           : return StringUtility.endsWith(subject, expression);
      case DOES_NOT_END_WITH   : return !StringUtility.endsWith(subject, expression);
      default                  : throw new OIMRuntimeException(String.format(resourceBackendValue(OPERATOR_INVALID), operator.toString()));
    }
  }
}