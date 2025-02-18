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

    File        :   EndpointAdministratorDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndpointAdministratorDataProvider.


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
import Thor.API.Exceptions.tcITResourceNotFoundException;

import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.utils.SerializationUtils;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.identity.foundation.naming.Group;
import oracle.iam.identity.foundation.naming.ITResource;

////////////////////////////////////////////////////////////////////////////////
// class EndpointAdministratorDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by <code>IT Resource</code> instance
 ** customization.
 ** <p>
 ** Implementing the dataprovider functionalities to retrieve and manage
 ** <code>IT Resource</code> administrators.
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
public class EndpointAdministratorDataProvider extends AbstractDataProvider<EndpointAdministratorAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTEXT_REQUIRED = "svr.required";
  private static final String OPERATOR_INVALID = "svr.operator";
  private static final String CRITERIA_INVALID = "svr.criteria";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8134660870809007762")
  private static final long   serialVersionUID = 8180661095809964100L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointAdministratorDataProvider</code> data
   ** access object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EndpointAdministratorDataProvider() {
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
   ** <p>
   ** The search expects the <code>IT Resource</code> for which the
   ** administrative roles are to be returned as the primary selector.
   ** <br>
   ** If this expectation isn't satisfied an appropriate runtime exception is
   ** thrown.
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
   **                            {@link EndpointAdministratorAdapter}.
   */
  @Override
  public List<EndpointAdministratorAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // default search will be done by endpointTypeKey
    final SearchCriteria criteria = getAttrCriteria(searchCriteria, EndpointAdministratorAdapter.SVR_FK);
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

    Map<Long, EndpointAdministratorAdapter> add = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, EndpointAdministratorAdapter.ADD), LinkedHashMap.class);
    Map<Long, EndpointAdministratorAdapter> del = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, EndpointAdministratorAdapter.DEL), LinkedHashMap.class);
    Map<Long, EndpointAdministratorAdapter> mod = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, EndpointAdministratorAdapter.MOD), LinkedHashMap.class);

    // ensure the NPE can never happens if the result set is initially received
    add = add == null ? new LinkedHashMap<Long, EndpointAdministratorAdapter>() : add;
    del = del == null ? new LinkedHashMap<Long, EndpointAdministratorAdapter>() : del;
    mod = mod == null ? new LinkedHashMap<Long, EndpointAdministratorAdapter>() : mod;

    List<EndpointAdministratorAdapter> result = new ArrayList<EndpointAdministratorAdapter>();
    // if the IT Resource under control has to be created we don't have to
    // populate existing relations for the IT Resource hence only added
    // administrators are in the scope
    if (rhs.equals(-1L)) {
      result = new ArrayList<EndpointAdministratorAdapter>(add.values());
    }
    else {
      final tcITResourceInstanceOperationsIntf service = service(tcITResourceInstanceOperationsIntf.class);
      try {
        final tcResultSet resultSet = service.getAdministrators(rhs.longValue());
        final int         count     = resultSet.getRowCount();
        for (int i = 0; i < count; i++) {
          resultSet.goToRow(i);
          result.add(build(rhs, resultSet));
        }
      }
      catch (tcITResourceNotFoundException e) {
        throw new OIMRuntimeException(e);
      }
      catch (tcAPIException e) {
        throw new OIMRuntimeException(e);
      }
      finally {
        service.close();
      }
    }
    return paginated(control, result, add, del, mod);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginated
  /**
   ** Factory method to create a paginated result set from the given
   ** resultSet list.
   ** <b>
   ** Each element in the returned list is an
   ** {@link EndpointAdministratorAdapter}.
   **
   ** @param  control            contains pagination, sort attribute, and sort
   **                            direction.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link Object} as the value.
   ** @param  resultSet          the {@link List} provider to obtain the
   **                            attribute values from.
   **                            <br>
   **                            Possible object is {link List} where each
   **                            element is of type
   **                            {@link EndpointAdministratorAdapter}.
   ** @param  add                the collected additions to the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link Long} for the key
   **                            and {@link EndpointAdministratorAdapter} as the
   **                            value.
   ** @param  mod                the collected modifications of the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link Long} for the key
   **                            and {@link EndpointAdministratorAdapter} as the
   **                            value.
   ** @param  del                the collected deletions of the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link Long} for the key
   **                            and {@link EndpointAdministratorAdapter} as the
   **                            value.
   **
   ** @return                    a paginated result set from the {@link List}.
   **                            <br>
   **                            Possible object is {link List} where each
   **                            element is of type
   **                            {@link EndpointAdministratorAdapter}.
   */
  private List<EndpointAdministratorAdapter> paginated(final Map<String, Object> parameter, List<EndpointAdministratorAdapter> resultSet, final Map<Long, EndpointAdministratorAdapter> add, final Map<Long, EndpointAdministratorAdapter> del, final Map<Long, EndpointAdministratorAdapter> mod) {
    if (resultSet == null && add.size() == 0)
      return resultSet;

    for (EndpointAdministratorAdapter cursor : resultSet) {
      if (del.containsKey(cursor.getGroupKey())) {
        // regardless what was fetched from the persitance layer override the
        // value by the actual value set in th UI
        final EndpointAdministratorAdapter mab = del.get(cursor.getGroupKey());
        cursor.setReadAccess(mab.getReadAccess());
        cursor.setWriteAccess(mab.getWriteAccess());
        cursor.setDeleteAccess(mab.getDeleteAccess());
        cursor.setPendingAction(EndpointAdministratorAdapter.DEL);
      }
      else if (mod.containsKey(cursor.getGroupKey())) {
        // regardless what was fetched from the persitance layer override the
        // value by the actual value set in th UI
        final EndpointAdministratorAdapter mab = mod.get(cursor.getGroupKey());
        cursor.setReadAccess(mab.getReadAccess());
        cursor.setWriteAccess(mab.getWriteAccess());
        cursor.setDeleteAccess(mab.getDeleteAccess());
        cursor.setPendingAction(EndpointAdministratorAdapter.MOD);
      }
      else
        cursor.setPendingAction(EndpointAdministratorAdapter.NIL);
    }
    // add all non-persisted entries to the collection of populated entries
    resultSet.addAll(add.values());
    return new PagedArrayList<EndpointAdministratorAdapter>(resultSet, 1, resultSet.size());
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create a <code>EndpointAdministratorAdapter</code> from
   ** a {@link tcResultSet}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The {@link tcResultSet} pointer needs to be positioned correctly before
   ** this method is called.
   **
   ** @param  endpointKey        the {@link EndpointTypeAdapter} identifier the
   **                            administrator to build belongs to.
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
   **                            {@link EndpointAdministratorAdapter}.
   */
  private static EndpointAdministratorAdapter build(final Long endpointKey, final tcResultSet resultSet) {
    final EndpointAdministratorAdapter mab = EndpointAdministratorAdapter.build(endpointKey);
    try {
      mab.setGroupKey(resultSet.getLongValue(Group.KEY));
      mab.setGroupName(resultSet.getStringValue(Group.NAME));
      mab.setReadAccess(resultSet.getBooleanValue(ITResource.PERMISSION_READ));
      mab.setWriteAccess(resultSet.getBooleanValue(ITResource.PERMISSION_WRITE));
      mab.setDeleteAccess(resultSet.getBooleanValue(ITResource.PERMISSION_DELETE));
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
    return mab;
  }
}