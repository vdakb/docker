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
    Subsystem   :   System Authorization Management

    File        :   EntityPublicationDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntityPublicationDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import oracle.iam.platform.authopss.api.PolicyConstants;

import oracle.iam.platform.authopss.vo.EntityPublication;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.platformservice.api.EntityPublicationService;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.ui.platform.utils.SerializationUtils;
import oracle.iam.ui.platform.utils.SearchCriteriaUtils;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.ui.platform.model.common.OIMClientFactory;
import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

////////////////////////////////////////////////////////////////////////////////
// class EntityPublicationDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by System Authorization Management customization.
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
public class EntityPublicationDataProvider extends AbstractDataProvider<EntityPublicationAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8665336690301967215")
  private static final long serialVersionUID = -6857844127519998588L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String            entityID;
  private String            entityType;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntityPublicationDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntityPublicationDataProvider() {
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
   **                            {@link EntityPublicationAdapter}.
   */
  @Override
  public List<EntityPublicationAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // reset any previuosly used criteria
    this.entityID   = null;
    this.entityType = null;
    // TODO This impl doesn't support QBE because of API lacks support.
    List<EntityPublicationAdapter> result = new ArrayList<EntityPublicationAdapter>();
    if (searchCriteria == null) {
      return new PagedArrayList<EntityPublicationAdapter>(result, 0, 0);
    }

    extractCriteria(searchCriteria);
    if (this.entityType == null || this.entityID == null)
      return new PagedArrayList<EntityPublicationAdapter>(result, 0, 0);

    if (!(this.entityType.equals(PolicyConstants.Resources.APPLICATION_INSTANCE.getId()) || this.entityType.equals(PolicyConstants.Resources.IT_RESOURCE_ENTITLEMENT.getId())))
      throw new OIMRuntimeException("Publication for this entity type has not been implemented.");

    Map<String, EntityPublicationAdapter> add = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, EntityPublicationAdapter.ADD), LinkedHashMap.class);
    Map<String, EntityPublicationAdapter> del = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, EntityPublicationAdapter.DEL), LinkedHashMap.class);
    Map<String, EntityPublicationAdapter> mod = SerializationUtils.deserializeFromString((String)super.getCriteriaAttrValue(searchCriteria, EntityPublicationAdapter.MOD), LinkedHashMap.class);

    // ensure the NPE can never happens if the result set is initially received
    add = add == null ? new LinkedHashMap<String, EntityPublicationAdapter>() : add;
    del = del == null ? new LinkedHashMap<String, EntityPublicationAdapter>() : del;
    mod = mod == null ? new LinkedHashMap<String, EntityPublicationAdapter>() : mod;

    // if the entity under control has to be created we don't have to
    // populate existing relations for the policy hence only added publications
    // are in the scope
    if (this.entityID.equals("-1")) {
      result = new ArrayList<EntityPublicationAdapter>(add.values());
      return new PagedArrayList<EntityPublicationAdapter>(result, 1, result.size());
    }

    final EntityPublicationService service = OIMClientFactory.getEntityPublicationService();
    try {
      final List<EntityPublication> resultSet = service.listEntityPublications(convertToResourcesEnum(this.entityType), this.entityID, convertParameter(control));
      return paginated(control, resultSet, add, del, mod);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   extractCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Application Instance.
   **
   ** @param criteria            the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   */
  private void extractCriteria(final SearchCriteria criteria) {
    // prevent bogus input
    if (criteria == null)
      return;

    SearchCriteria target = SearchCriteriaUtils.findCriteriaWithName(criteria, "Bind_entityType");
    if (target != null)
      this.entityType = (String)target.getSecondArgument();
    else {
      target = SearchCriteriaUtils.findCriteriaWithName(criteria, "bindEntityType");
      if (target != null) {
        this.entityType = (String)target.getSecondArgument();
      }
    }

    if (this.entityType == null)
      return;

    target = SearchCriteriaUtils.findCriteriaWithName(criteria, "Bind_appInstanceKey");
    if (target != null) {
      this.entityID = (String)target.getSecondArgument();
      return;
    }

    target = SearchCriteriaUtils.findCriteriaWithName(criteria, "bindEntityId");
    if (target != null) {
      this.entityID = (String)target.getSecondArgument();
      return;
    }

    target = SearchCriteriaUtils.findCriteriaWithName(criteria, "entityId");
    if (target != null) {
      this.entityID = (String)target.getSecondArgument();
      return;
    }

    Object                  lh = criteria.getFirstArgument();
    Object                  rh = criteria.getSecondArgument();
    SearchCriteria.Operator op = criteria.getOperator();
    if ((lh instanceof String) && (rh instanceof String)) {
      if (op == SearchCriteria.Operator.EQUAL && (((String)lh).equalsIgnoreCase("Bind_appInstanceKey") || ((String)lh).equalsIgnoreCase(ApplicationInstanceAdapter.PK))) {
        this.entityID = (String)rh;
      }
      else if (op == SearchCriteria.Operator.EQUAL && (((String)lh).equalsIgnoreCase("Bind_entitlementKey") || ((String)lh).equalsIgnoreCase(EntitlementAdapter.PK))) {
        this.entityID = (String)rh;
      }
    }

    if (lh instanceof SearchCriteria) {
      this.extractCriteria((SearchCriteria)lh);
    }

    if (rh instanceof SearchCriteria) {
      this.extractCriteria((SearchCriteria)rh);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   convertParameter
  /**
   ** Converts the attribute named defined by the ADF view controller to the
   ** natural names exposed by the OIM kernel API.
   **
   ** @param  parameter          the mapping of control parameter names defined
   **                            by the ADF view controller.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link Object} as the value.
   **
   ** @return                    the attribute name exposed by the OIM kernel
   **                            API if any.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link Object} as the value.
   */
  private HashMap<String, Object> convertParameter(final HashMap<String, Object> parameter) {
    // prevent bogus input
    if (parameter == null || parameter.size() == 0)
      return parameter;

    if (parameter.containsKey(ApplicationInstance.SORTEDBY)) {
      String value = parameter.get(ApplicationInstance.SORTEDBY).toString();
      value = this.convertAttributeName(value);
      parameter.put(ApplicationInstance.SORTEDBY, value);
    }

    return parameter;
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
   **                            Allowed object is {@link String}.
   **
   ** @return                    the attribute name exposed by the OIM kernel
   **                            API if any.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private String convertAttributeName(final String outboundName) {
    // prevent bogus input
    if (outboundName == null)
      return null;

    String inboundName = outboundName.trim();
    if (inboundName.equalsIgnoreCase("Bind_appInstanceKey")) {
      inboundName = "ID";
    }
    else if (inboundName.equalsIgnoreCase("resourceObjectName")) {
      inboundName = "OBJ.OBJ_NAME";
    }
    else if (inboundName.equalsIgnoreCase("endpointName")) {
      inboundName = "SVR.SVR_NAME";
    }
    else if (inboundName.equalsIgnoreCase("scopeName")) {
      inboundName = "Organization Name";
    }
    else {
      inboundName = outboundName;
    }

    return inboundName;
  }

  private PolicyConstants.Resources convertToResourcesEnum(String entityTypeAsStr) {
    // resolve the entity-type to enum.
    PolicyConstants.Resources entityTypeEnum = null;
    for (PolicyConstants.Resources res : PolicyConstants.Resources.values()) {
      if (res.getId().equals(entityTypeAsStr)) {
        entityTypeEnum = res;
        break;
      }
    }
    return entityTypeEnum;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   paginate
  /**
   ** Factory method to create a paginated result set from the given
   ** resultSet list.
   ** <b>
   ** Each element in the returned list is an {@link EntityPublicationAdapter}.
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
   **                            Allowed object is {link List} where each
   **                            element is of type
   **                            {@link EntityPublication}.
   ** @param  add                the collected additions to the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EntityPublicationAdapter} as the
   **                            value.
   ** @param  mod                the collected modifications of the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EntityPublicationAdapter} as the
   **                            value.
   ** @param  del                the collected deletions of the result set.
   **                            <br>
   **                            Allowed object is {link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EntityPublicationAdapter} as the
   **                            value.
   **
   ** @return                    a paginated result set from the {@link List}.
   **                            <br>
   **                            Possible object is {link List} where each
   **                            element is of type
   **                            {@link EntityPublicationAdapter}.
   */
  private List<EntityPublicationAdapter> paginated(final Map<String, Object> parameter, List<EntityPublication> resultSet, final Map<String, EntityPublicationAdapter> add, final Map<String, EntityPublicationAdapter> del, final Map<String, EntityPublicationAdapter> mod) {
    final List<EntityPublicationAdapter> result = new ArrayList<EntityPublicationAdapter>();
    if (resultSet == null && add.size() == 0)
      return result;

    int rangeStart = 0;
    // add all non-persisted entries to the collection of populated entries
    result.addAll(add.values());
    int totalRowCount = resultSet.size();
    if (parameter != null && parameter.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)parameter.get(ConstantsDefinition.SEARCH_STARTROW);
    }

    for (EntityPublication cursor : resultSet) {
      EntityPublicationAdapter mab = new EntityPublicationAdapter();
      mab.setPublicationId(cursor.getEntityPublicationId());
      mab.setEntityId(Long.valueOf(this.entityID));
      mab.setEntityType(this.entityType);
      mab.setScopeId(cursor.getScopeId());
      mab.setScopeName(cursor.getScopeName());
      mab.setScopeType(translateScopeType(cursor.getScopeType()));
      mab.setHierarchicalScope(cursor.isHierarchicalScope());
      result.add(mab);

      if (del.containsKey(mab.getScopeId())) {
        mab.setPendingAction(EntityPublicationAdapter.DEL);
      }
      else if (mod.containsKey(mab.getScopeId())) {
        // regardless what was fetched from the persitance layer override the
        // value by the actual value set in th UI
        mab.setHierarchicalScope(mod.get(mab.getScopeId()).getHierarchicalScope());
        mab.setPendingAction(EntityPublicationAdapter.MOD);
      }
      else
        mab.setPendingAction(EntityPublicationAdapter.NIL);
    }
    // increase the resultset size with the amount of non persisted entries
    totalRowCount += add.size();
    return new PagedArrayList<EntityPublicationAdapter>(result, rangeStart, totalRowCount);
  }

  protected static String translateScopeType(final String type) {
    return AbstractDataProvider.resourceBundleValue("oracle.iam.identity.sysauthz.bundle.Backend", AbstractDataProvider.resourceBundleKey(type, "act.type"), type);
  }

  protected static String translateScopeStatus(final String status) {
    return AbstractDataProvider.resourceBundleValue("oracle.iam.identity.sysauthz.bundle.Backend", AbstractDataProvider.resourceBundleKey(status, "act.status"), status);
  }
}