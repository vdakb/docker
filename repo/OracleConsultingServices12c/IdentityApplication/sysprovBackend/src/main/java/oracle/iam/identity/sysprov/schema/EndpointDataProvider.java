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

    File        :   EndpointDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndpointDataProvider.


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

import Thor.API.Exceptions.tcAPIException;

import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.identity.foundation.naming.ITResource;
import oracle.iam.identity.foundation.naming.ITResourceType;

////////////////////////////////////////////////////////////////////////////////
// class EndpointDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by System Provisioning Management customization.
 ** <p>
 ** Implementing the dataprovider functionalities to retrieve and manage
 ** <code>IT Resource</code>s.
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
public class EndpointDataProvider extends AbstractDataProvider<EndpointAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////


  private static final String OPERATOR_INVALID = "svr.operator";
  private static final String CRITERIA_INVALID = "svr.criteria";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9160479467366513669")
  private static final long   serialVersionUID = -214995806565079527L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointDataProvider</code> data access object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EndpointDataProvider() {
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
   **                            element is of type {@link EndpointAdapter}.
   */
  @Override
  public List<EndpointAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    final Map<String, String>                empty   = new HashMap<String, String>();
    final Map<String, String>                filter  = criteriaToMap(searchCriteria, empty);
    final tcITResourceInstanceOperationsIntf service = service(tcITResourceInstanceOperationsIntf.class);
    try {
      final tcResultSet resultSet = service.findITResourceInstances(filter);
      return paginate(control, resultSet, resultSet.getRowCount());
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   criteriaToMap
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Resource Object.
   **
   ** @param  searchCriteria     the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   **
   ** @return                    the search criteria mapped to the OIM
   **                            declarations.
   */
  private Map<String, String> criteriaToMap(final SearchCriteria searchCriteria, Map<String, String> map) {
    // default search will be done by name
    SearchCriteria criteria = searchCriteria;
    if (criteria == null)
      criteria = new SearchCriteria(EndpointAdapter.NAME, "%", SearchCriteria.Operator.EQUAL);

    Object lhs = criteria.getFirstArgument();
    if (lhs instanceof SearchCriteria) {
      map = criteriaToMap((SearchCriteria)lhs, map);
    }
    else if (lhs instanceof String) {
      lhs = convertAttributeName((String)lhs);
    }

    Object rhs = criteria.getSecondArgument();
    if (rhs instanceof SearchCriteria) {
      map = criteriaToMap((SearchCriteria)rhs, map);
    }

    // apply the correct search operator the API understands
    final SearchCriteria.Operator op = criteria.getOperator();
    switch (op) {
      case EQUAL       : map.put(lhs.toString(), rhs.toString());
                         break;
      case CONTAINS    : map.put(lhs.toString(), "%" + rhs.toString() + "%");
                         break;
      case ENDS_WITH   : map.put(lhs.toString(), "%" + rhs.toString());
                         break;
      case BEGINS_WITH : map.put(lhs.toString(), rhs.toString() + "%");
                         break;
      case AND         : break;
      default          : throw new OIMRuntimeException(String.format(resourceBackendValue(OPERATOR_INVALID), op.toString()));
    }
    return map;
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
    if (inboundName.equals(EndpointAdapter.PK))
      inboundName = ITResource.KEY;
    else if (inboundName.equals(EndpointAdapter.NAME))
      inboundName = ITResource.NAME;
    else if (inboundName.equals(EndpointAdapter.TYPE) || inboundName.equals(EndpointAdapter.TYPE_FK))
      inboundName = ITResourceType.NAME;
    else if (inboundName.equals(EndpointAdapter.REMOTE))
      inboundName = ITResource.REMOTE_NAME;
    else
      throw new OIMRuntimeException(String.format(resourceBackendValue(CRITERIA_INVALID), outboundName));

    return inboundName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginate
  /**
   ** Factory method to create a paginated result set from the given
   ** {@link tcResultSet}.
   ** <b>
   ** Each element in the returned list is an {@link EndpointAdapter}.
   **
   ** @param  control            contains pagination, sort attribute, and sort
   **                            direction.
   **                            Allowed object is {@link HashMap}.
   ** @param  resultSet          the {@link tcResultSet} provider to obtain the
   **                            attribute values from.
   **                            Allowed object is {@link tcResultSet}.
   ** @param  totalRowCount      the size of the entire result set
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a paginated result set from the
   **                            {@link tcResultSet}.
   */
  private List<EndpointAdapter> paginate(final HashMap<String, Object> parameter, final tcResultSet resultSet, final int totalRowCount) {
    final List<EndpointAdapter> batch = new ArrayList<EndpointAdapter>();
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
    return new PagedArrayList<EndpointAdapter>(batch, rangeStart, totalRowCount);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create a <code>EndpointAdapter</code> from a
   ** {@link tcResultSet}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The {@link tcResultSet} pointer needs to be positioned correctly before
   ** this method is called.
   **
   ** @param  resultSet          the {@link tcResultSet} provider to obtain the
   **                            attribute values from.
   **                            Allowed object is {@link tcResultSet}.
   **
   ** @return                    the model adapter bean populated from the
   **                            specfied <code>resultSet</code>.
   **                            Possible object is {@link EndpointAdapter}.
   */
  private static EndpointAdapter build(final tcResultSet resultSet) {
    final EndpointAdapter mab = EndpointAdapter.build();
    try {
      mab.setEndpointKey(resultSet.getLongValue(ITResource.KEY));
      mab.setEndpointName(resultSet.getStringValue(ITResource.NAME));
      mab.setEndpointType(resultSet.getStringValue(ITResourceType.NAME));
      mab.setRemoteManager(resultSet.getStringValue(ITResource.REMOTE_NAME));
     }
    catch (tcColumnNotFoundException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
    return mab;
  }
}