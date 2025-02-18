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

    File        :   ResourceObjectAdministratorDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceObjectAdministratorDataProvider.


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
import Thor.API.Exceptions.tcObjectNotFoundException;

import Thor.API.Operations.tcObjectOperationsIntf;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.utils.SerializationUtils;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.identity.foundation.naming.Group;
import oracle.iam.identity.foundation.naming.ResourceObject;

////////////////////////////////////////////////////////////////////////////////
// class ResourceObjectAdministratorDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by <code>Resource Object</code> Administrator
 ** customization.
 ** <p>
 ** Implementing the dataprovider functionalities to retrieve and manage
 ** <code>IT Resource</code> parameters.
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
public class ResourceObjectAdministratorDataProvider extends AbstractDataProvider<ResourceObjectAdministratorAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTEXT_REQUIRED = "obj.required";
  private static final String OPERATOR_INVALID = "obj.operator";
  private static final String CRITERIA_INVALID = "obj.criteria";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2222328695771194364")
  private static final long   serialVersionUID = 169536317643523441L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ResourceObjectAdministratorDataProvider</code>
   ** data access object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ResourceObjectAdministratorDataProvider() {
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
   **                            {@link ResourceObjectAdministratorAdapter}.
   */
  @Override
  public List<ResourceObjectAdministratorAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // default search will be done by objectKey
    final SearchCriteria criteria = getAttrCriteria(searchCriteria, ResourceObjectAdministratorAdapter.OBJ_FK);
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

    Map<Long, ResourceObjectAdministratorAdapter> add = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, ResourceObjectAdministratorAdapter.ADD), LinkedHashMap.class);
    Map<Long, ResourceObjectAdministratorAdapter> del = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, ResourceObjectAdministratorAdapter.DEL), LinkedHashMap.class);
    Map<Long, ResourceObjectAdministratorAdapter> mod = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, ResourceObjectAdministratorAdapter.MOD), LinkedHashMap.class);

    // ensure the NPE can never happens if the result set is initially received
    add = add == null ? new LinkedHashMap<Long, ResourceObjectAdministratorAdapter>() : add;
    del = del == null ? new LinkedHashMap<Long, ResourceObjectAdministratorAdapter>() : del;
    mod = mod == null ? new LinkedHashMap<Long, ResourceObjectAdministratorAdapter>() : mod;

    List<ResourceObjectAdministratorAdapter> result = new ArrayList<ResourceObjectAdministratorAdapter>();
    // if the Resource Object under control has to be created we don't have to
    // populate existing relations for the Resource Object hence only added
    // parameters are in the scope
    if (rhs.equals(-1L)) {
      final List<ResourceObjectAdministratorAdapter> list = CollectionUtility.list(add.values());
      return new PagedArrayList<ResourceObjectAdministratorAdapter>(list, 1, list.size());
    }
    else {
      final tcObjectOperationsIntf service = service(tcObjectOperationsIntf.class);
      try {
        final tcResultSet resultSet = service.getAdministrators(rhs.longValue());
        final int         count     = resultSet.getRowCount();
        for (int i = 0; i < count; i++) {
          resultSet.goToRow(i);
          result.add(build(rhs, resultSet));
        }
      }
      catch (tcObjectNotFoundException e) {
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
    // default search will be done by object key
    SearchCriteria criteria = searchCriteria;
    if (criteria == null) {
      criteria = new SearchCriteria(ResourceObject.KEY, "*", SearchCriteria.Operator.EQUAL);
    }
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
      case CONTAINS    : map.put(lhs.toString(), "*" + rhs.toString() + "*");
                         break;
      case ENDS_WITH   : map.put(lhs.toString(), "*" + rhs.toString());
                         break;
      case BEGINS_WITH : map.put(lhs.toString(), rhs.toString() + "*");
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
   **                            <br>
   **                            Allowed object is {@link String}
   **
   ** @return                    the attribute name exposed by the OIM kernel
   **                            API if any.
   **                            <br>
   **                            Possible object is {@link String}
   */
  private String convertAttributeName(final String outboundName) {
    // prevent bogus input
    String inboundName = outboundName;
    if (inboundName == null)
      return null;

    inboundName = inboundName.trim();
    if (inboundName.equals(ResourceObjectAdministratorAdapter.OBJ_PK) || inboundName.equals(ResourceObjectAdministratorAdapter.OBJ_FK))
      inboundName = ResourceObject.KEY;
    else if (inboundName.equals(ResourceObjectAdministratorAdapter.NAME))
      inboundName = Group.NAME;
    else
      throw new OIMRuntimeException(String.format(resourceBackendValue(CRITERIA_INVALID), outboundName));

    return inboundName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginated
  /**
   ** Factory method to create a paginated result set from the given
   ** resultSet list.
   ** <b>
   ** Each element in the returned list is an
   ** {@link ResourceObjectAdministratorAdapter}.
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
   **                            {@link ResourceObjectAdministratorAdapter}.
   ** @param  add                the collected additions to the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link Long} for the key
   **                            and {@link ResourceObjectAdministratorAdapter} as the
   **                            value.
   ** @param  mod                the collected modifications of the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link Long} for the key
   **                            and {@link ResourceObjectAdministratorAdapter} as the
   **                            value.
   ** @param  del                the collected deletions of the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link Long} for the key
   **                            and {@link ResourceObjectAdministratorAdapter} as the
   **                            value.
   **
   ** @return                    a paginated result set from the {@link List}.
   **                            <br>
   **                            Possible object is {link List} where each
   **                            element is of type
   **                            {@link ResourceObjectAdministratorAdapter}.
   */
  private List<ResourceObjectAdministratorAdapter> paginated(final Map<String, Object> parameter, List<ResourceObjectAdministratorAdapter> resultSet, final Map<Long, ResourceObjectAdministratorAdapter> add, final Map<Long, ResourceObjectAdministratorAdapter> del, final Map<Long, ResourceObjectAdministratorAdapter> mod) {
    if (resultSet == null && add.size() == 0)
      return resultSet;

    for (ResourceObjectAdministratorAdapter cursor : resultSet) {
      if (del.containsKey(cursor.getGroupKey())) {
        // regardless what was fetched from the persitance layer override the
        // value by the actual value set in th UI
        final ResourceObjectAdministratorAdapter mab = del.get(cursor.getGroupKey());
        cursor.setWriteAccess(mab.getWriteAccess());
        cursor.setDeleteAccess(mab.getDeleteAccess());
        cursor.setPendingAction(ResourceObjectAdministratorAdapter.DEL);
      }
      else if (mod.containsKey(cursor.getGroupKey())) {
        // regardless what was fetched from the persitance layer override the
        // value by the actual value set in th UI
        final ResourceObjectAdministratorAdapter mab = mod.get(cursor.getGroupKey());
        cursor.setWriteAccess(mab.getWriteAccess());
        cursor.setDeleteAccess(mab.getDeleteAccess());
        cursor.setPendingAction(ResourceObjectAdministratorAdapter.MOD);
      }
      else
        cursor.setPendingAction(ResourceObjectAdministratorAdapter.NIL);
    }
    // add all non-persisted entries to the collection of populated entries
    resultSet.addAll(add.values());
    return new PagedArrayList<ResourceObjectAdministratorAdapter>(resultSet, 1, resultSet.size());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create a <code>ResourceObjectAdministratorAdapter</code>
   ** from a {@link tcResultSet}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The {@link tcResultSet} pointer needs to be positioned correctly before
   ** this method is called.
   **
   ** @param  objectKey          the {@link ResourceObjectAdapter} identifier
   **                            the administrator role to build belongs to.
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
   **                            {@link ResourceObjectAdministratorAdapter}.
   */
  private static ResourceObjectAdministratorAdapter build(final Long objectKey, final tcResultSet resultSet) {
    final ResourceObjectAdministratorAdapter mab = ResourceObjectAdministratorAdapter.build(objectKey);
    try {
      mab.setGroupKey(resultSet.getLongValue(Group.KEY));
      mab.setGroupName(resultSet.getStringValue(Group.NAME));
      mab.setWriteAccess(resultSet.getBooleanValue(ResourceObject.PERMISSION_WRITE));
      mab.setDeleteAccess(resultSet.getBooleanValue(ResourceObject.PERMISSION_DELETE));
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
    return mab;
  }
}