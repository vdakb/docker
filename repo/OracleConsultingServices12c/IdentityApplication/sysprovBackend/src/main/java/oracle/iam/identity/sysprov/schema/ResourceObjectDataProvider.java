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
    Subsystem   :   System Provisioning Management

    File        :   ResourceObjectDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceObjectDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcObjectOperationsIntf;

import Thor.API.Exceptions.tcAPIException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.identity.foundation.naming.ResourceObject;

////////////////////////////////////////////////////////////////////////////////
// class ResourceObjectDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by System Provisioning Management customization.
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
public class ResourceObjectDataProvider extends AbstractDataProvider<ResourceObjectAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7374705233311234843")
  private static final long serialVersionUID = -5562993953425402978L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ResourceObjectDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ResourceObjectDataProvider() {
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
   **                            {@link ResourceObjectAdapter}.
   */
  @Override
  public List<ResourceObjectAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    final Map<String, String>    empty   = new HashMap<String, String>();
    final Map<String, String>    filter  = criteriaToMap(searchCriteria, empty);
    final tcObjectOperationsIntf service = service(tcObjectOperationsIntf.class);
    try {
      final tcResultSet resultSet = service.findProvisionableObjects(filter);
      return paginate(control, resultSet, resultSet.getRowCount());
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   criteriaToMap
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Resource Object.
   **
   ** @param  searchCriteria     the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  map                the search criteria mapped to the OIM
   **                            declarations.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type [@link String} for the key
   **                            and {@link String} for the value.
   **
   ** @return                    the search criteria mapped to the OIM
   **                            declarations.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type [@link String} for the key
   **                            and {@link String} for the value.
   */
  private Map<String, String> criteriaToMap(final SearchCriteria searchCriteria, Map<String, String> map) {
    // default search will be done by name
    SearchCriteria criteria = searchCriteria;
    if (criteria == null) {
      criteria = new SearchCriteria(ResourceObject.NAME, "*", SearchCriteria.Operator.EQUAL);
    }

    final Object                  lhs = criteria.getFirstArgument();
    final Object                  rhs = criteria.getSecondArgument();
    final SearchCriteria.Operator op = criteria.getOperator();
    if (lhs != null && (lhs instanceof SearchCriteria)) {
      map = criteriaToMap((SearchCriteria)lhs, map);
    }

    if (rhs != null && (rhs instanceof SearchCriteria)) {
      map = criteriaToMap((SearchCriteria)rhs, map);
    }

    if ((lhs instanceof String) && (rhs instanceof String)) {
      if (ResourceObject.KEY.equals(lhs)) {
        map.put(ResourceObject.KEY, rhs.toString());
      }
      else if (ResourceObject.NAME.equals(lhs)) {
        if (op == SearchCriteria.Operator.EQUAL) {
          map.put(ResourceObject.NAME, rhs.toString());
        }
        else if (op == SearchCriteria.Operator.BEGINS_WITH) {
          map.put(ResourceObject.NAME, rhs.toString() + "*");
        }
        else if (op == SearchCriteria.Operator.ENDS_WITH) {
          map.put(ResourceObject.NAME, "*" + rhs.toString());
        }
        else if (op == SearchCriteria.Operator.CONTAINS) {
          map.put(ResourceObject.NAME, "*" + rhs.toString() + "*");
        }
        else {
          throw new OIMRuntimeException("Operator " + op.toString() + " not supported.");
        }
      }
      else if (ResourceObject.TYPE.equals(lhs)) {
        if (op == SearchCriteria.Operator.EQUAL) {
          map.put(ResourceObject.TYPE, rhs.toString());
        }
        else if (op == SearchCriteria.Operator.BEGINS_WITH) {
          map.put(ResourceObject.TYPE, rhs.toString() + "*");
        }
        else if (op == SearchCriteria.Operator.ENDS_WITH) {
          map.put(ResourceObject.TYPE, "*" + rhs.toString());
        }
        else if (op == SearchCriteria.Operator.CONTAINS) {
          map.put(ResourceObject.TYPE, "*" + rhs.toString() + "*");
        }
        else {
          throw new OIMRuntimeException("Operator " + op.toString() + " not supported.");
        }
      }
      else if (ResourceObject.ORDER_FOR.equals(lhs)) {
        map.put(ResourceObject.ORDER_FOR, rhs.toString());
      }
      else if (ResourceObject.ALLOWALL.equals(lhs)) {
        map.put(ResourceObject.ALLOWALL, rhs.toString());
      }
      else if (ResourceObject.ALLOWMULTIPLE.equals(lhs)) {
        map.put(ResourceObject.ALLOWMULTIPLE, rhs.toString());
      }
    }
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginate
  /**
   ** Factory method to create a paginated result set from the given
   ** {@link tcResultSet}.
   ** <b>
   ** Each element in the returned list is a {@link ResourceObjectAdapter}.
   **
   ** @param  control            contains pagination, sort attribute, and sort
   **                            direction.
   **                            <br>
   **                            Allowed object is {@link HashMap}.
   ** @param  resultSet          the {@link tcResultSet} provider to obtain the
   **                            attribute values from.
   **                            <br>
   **                            Allowed object is {@link tcResultSet}.
   ** @param  totalRowCount      the size of the entire result set
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a paginated result set from the
   **                            {@link tcResultSet}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type
   **                            [@link ResourceObjectAdapter}.
   */
  private List<ResourceObjectAdapter> paginate(final HashMap<String, Object> parameter, final tcResultSet resultSet, final int totalRowCount) {
    final List<ResourceObjectAdapter> batch = new ArrayList<ResourceObjectAdapter>();
    if (resultSet == null)
      return batch;

    int rangeStart = 0;
    int rangeEnd   = 27;
    try {
      if (parameter != null && parameter.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
        rangeStart = (Integer)parameter.get(ConstantsDefinition.SEARCH_STARTROW);
      }
      if (parameter != null && parameter.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
        rangeEnd = (Integer)parameter.get(ConstantsDefinition.SEARCH_ENDROW);
        if (rangeEnd > totalRowCount)
          rangeEnd = totalRowCount;
      }
      for (int i = rangeStart - 1; i < rangeEnd; i++) {
        resultSet.goToRow(i);
        batch.add(build(resultSet));
      }
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
    return new PagedArrayList<ResourceObjectAdapter>(batch, rangeStart, totalRowCount);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create a <code>ResourceObjectAdapter</code> from a
   ** {@link tcResultSet}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The {@link tcResultSet} pointer needs to be positioned correctly before
   ** this method is called.
   **
   ** @param  resultSet          the {@link tcResultSet} provider to obtain the
   **                            attribute values from.
   **                            <br>
   **                            Allowed object is {@link tcResultSet}.
   **
   ** @return                    the model adapter bean populated from the
   **                            specfied <code>resultSet</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link ResourceObjectAdapter}.
   */
  private static ResourceObjectAdapter build(final tcResultSet resultSet) {
    final ResourceObjectAdapter mab = ResourceObjectAdapter.build();
    mab.processResultSet(resultSet);
    return mab;
  }
}