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

    File        :   EndpointTypeParameterDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndpointTypeParameterDataProvider.


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
import java.util.LinkedHashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcITResourceDefinitionNotFoundException;

import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;

import oracle.iam.identity.foundation.naming.ITResourceType;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.utils.SerializationUtils;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

////////////////////////////////////////////////////////////////////////////////
// class EndpointTypeParameterDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by <code>IT Resource Type</code> customization.
 ** <p>
 ** Implementing the dataprovider functionalities to retrieve and manage
 ** <code>IT Resource Type</code> parameters.
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
public class EndpointTypeParameterDataProvider extends AbstractDataProvider<EndpointTypeParameterAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTEXT_REQUIRED = "svd.required";
  private static final String OPERATOR_INVALID = "svd.operator";
  private static final String CRITERIA_INVALID = "svd.criteria";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2389129368516936124")
  private static final long   serialVersionUID = 3295600206680379599L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointTypeParameterDataProvider</code> data
   ** access object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EndpointTypeParameterDataProvider() {
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
   **                            {@link EndpointTypeParameterAdapter}.
   */
  @Override
  public List<EndpointTypeParameterAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // default search will be done by endpointTypeKey
    final SearchCriteria criteria = getAttrCriteria(searchCriteria, EndpointTypeParameterAdapter.FK);
    if (criteria == null)
      throw new OIMRuntimeException(resourceBackendValue(CONTEXT_REQUIRED));

    final SearchCriteria.Operator op = criteria.getOperator();
    if (!(op == SearchCriteria.Operator.EQUAL))
      throw new OIMRuntimeException(String.format(resourceBackendValue(OPERATOR_INVALID), op.toString()));

    // code below looks strange because we are thinking that the proper types
    // are configured correctly in the entity definitions to assume that ADF has
    // the knowlege about it
    // unfortunately such type information got lost during them are passed
    // through the binding layer hence instead to be able to easely cast the
    // query argument to the expected type we have to convert it
    final Long rhs = Long.valueOf(criteria.getSecondArgument().toString());
    if (rhs == null)
      throw new OIMRuntimeException(String.format(resourceBackendValue(CRITERIA_INVALID), criteria.toString()));

    Map<Long, EndpointTypeParameterAdapter> add = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, EndpointTypeParameterAdapter.ADD), LinkedHashMap.class);
    Map<Long, EndpointTypeParameterAdapter> del = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, EndpointTypeParameterAdapter.DEL), LinkedHashMap.class);
    Map<Long, EndpointTypeParameterAdapter> mod = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, EndpointTypeParameterAdapter.MOD), LinkedHashMap.class);

    // ensure the NPE can never happens if the result set is initially received
    add = add == null ? new LinkedHashMap<Long, EndpointTypeParameterAdapter>() : add;
    del = del == null ? new LinkedHashMap<Long, EndpointTypeParameterAdapter>() : del;
    mod = mod == null ? new LinkedHashMap<Long, EndpointTypeParameterAdapter>() : mod;

    List<EndpointTypeParameterAdapter> result = new ArrayList<EndpointTypeParameterAdapter>();
    // if the IT Resource Type under control has to be created we don't have to
    // populate existing relations for the IT Resource Type hence only added
    // parameters are in the scope
    if (rhs.equals(-1L)) {
      result = new ArrayList<EndpointTypeParameterAdapter>(add.values());
    }
    else {
      final tcITResourceDefinitionOperationsIntf service = service(tcITResourceDefinitionOperationsIntf.class);
      try {
        final tcResultSet resultSet = service.getITResourceDefinitionParameters(rhs.longValue());
        final int count = resultSet.getRowCount();
        for (int i = 0; i < count; i++) {
          resultSet.goToRow(i);
          result.add(build(rhs, resultSet));
        }
      }
      catch (tcITResourceDefinitionNotFoundException e) {
        throw new OIMRuntimeException(e);
      }
      catch (tcAPIException e) {
        throw new OIMRuntimeException(e);
      }
      finally {
        service.close();
      }
    }
    return paginated(result, add, mod, del);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginated
  /**
   ** Factory method to create a paginated result set from the given
   ** resultSet list.
   ** <b>
   ** Each element in the returned list is an
   ** {@link EndpointTypeParameterAdapter}.
   **
   ** @param  resultSet          the {@link List} provider to obtain the
   **                            attribute values from.
   **                            <br>
   **                            Possible object is {link List} where each
   **                            element is of type
   **                            {@link EndpointTypeParameterAdapter}.
   ** @param  add                the collected additions to the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link Long} for the key
   **                            and {@link EndpointTypeParameterAdapter} as the
   **                            value.
   ** @param  mod                the collected modifications of the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link Long} for the key
   **                            and {@link EndpointTypeParameterAdapter} as the
   **                            value.
   ** @param  del                the collected deletions of the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link Long} for the key
   **                            and {@link EndpointTypeParameterAdapter} as the
   **                            value.
   **
   ** @return                    a paginated result set from the {@link List}.
   **                            <br>
   **                            Possible object is {link List} where each
   **                            element is of type
   **                            {@link EndpointTypeParameterAdapter}.
   */
  private List<EndpointTypeParameterAdapter> paginated(List<EndpointTypeParameterAdapter> resultSet, final Map<Long, EndpointTypeParameterAdapter> add, final Map<Long, EndpointTypeParameterAdapter> del, final Map<Long, EndpointTypeParameterAdapter> mod) {
    if (resultSet == null && add.size() == 0)
      return resultSet;

    // add all non-persisted entries to the collection of populated entries
    resultSet.addAll(add.values());
    for (EndpointTypeParameterAdapter cursor : resultSet) {
      if (del.containsKey(cursor.getParameterKey())) {
        // regardless what was fetched from the persitance layer override the
        // value by the actual value set in th UI
        final EndpointTypeParameterAdapter mab = del.get(cursor.getParameterKey());
        cursor.setEncrypted(mab.getEncrypted());
        cursor.setName(mab.getName());
        cursor.setValue(mab.getValue());
        cursor.setPendingAction(EndpointAdministratorAdapter.DEL);
      }
      else if (mod.containsKey(cursor.getParameterKey())) {
        // regardless what was fetched from the persitance layer override the
        // value by the actual value set in th UI
        final EndpointTypeParameterAdapter mab = mod.get(cursor.getParameterKey());
        cursor.setEncrypted(mab.getEncrypted());
        cursor.setName(mab.getName());
        cursor.setValue(mab.getValue());
        cursor.setPendingAction(EndpointAdministratorAdapter.MOD);
      }
      else
        cursor.setPendingAction(EndpointAdministratorAdapter.NIL);
    }
    return new PagedArrayList<EndpointTypeParameterAdapter>(resultSet, 1, resultSet.size());
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create a <code>EndpointTypeParameterAdapter</code> from
   ** a {@link tcResultSet}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The {@link tcResultSet} pointer needs to be positioned correctly before
   ** this method is called.
   **
   ** @param  endpointTypeKey    the {@link EndpointTypeAdapter} identifier the
   **                            parameter to build belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  resultSet          the {@link tcResultSet} provider to obtain the
   **                            attribute values from.
   **                            <br>
   **                            Allowed object is {@link tcResultSet}.
   **
   ** @return                    the model adapter bean populated from the
   **                            specfied <code>resultSet</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>EndpointTypeParameterAdapter</code>.
   */
  private static EndpointTypeParameterAdapter build(final Long endpointTypeKey, final tcResultSet resultSet) {
    final EndpointTypeParameterAdapter mab = EndpointTypeParameterAdapter.build(endpointTypeKey);
    try {
      mab.setParameterKey(resultSet.getLongValue(ITResourceType.PARAMETER_KEY));
      mab.setName(resultSet.getStringValue(ITResourceType.PARAMETER_NAME));
      mab.setValue(resultSet.getStringValue(ITResourceType.PARAMETER_DEFAULT));
      mab.setEncrypted(resultSet.getBooleanValue(ITResourceType.PARAMETER_ENCRYPTED));
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
    return mab;
  }
}