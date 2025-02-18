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

    File        :   CatalogItemDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogItemDataProvider.


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

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import oracle.iam.platform.authopss.exception.AccessDeniedException;

import oracle.iam.platform.utils.vo.OIMType;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.model.common.PagedArrayList;
import oracle.iam.ui.platform.model.common.OIMClientFactory;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.catalog.vo.Result;
import oracle.iam.catalog.vo.Catalog;

import oracle.iam.catalog.api.CatalogService;

import oracle.iam.catalog.exception.CatalogException;

import oracle.iam.catalog.repository.Repository;
import oracle.iam.catalog.repository.RepositoryFactory;

import oracle.hst.foundation.naming.ServiceLocator;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class CatalogItemDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
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
public class CatalogItemDataProvider extends AbstractDataProvider<CatalogItemAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String              PRIMARY          = "cat_id";
  private static final String              ID               = "ent_id";
  private static final String              TYPE             = "ent_type";
  private static final String              NAME             = "ent_name";
  private static final String              DISPLAY_NAME     = "ent_display_name";
  private static final String              DESCRIPTION      = "ent_description";
  private static final String              PARENT_ID        = "ent_key_parent";
  private static final String              PARENT_TYPE      = "ent_type_parent";
  private static final String              CATEGORY         = "category";
  private static final String              RISK             = "item_risk";
  private static final String              RISK_UPDATE      = "risk_update_date";
  private static final String              DELETED          = "deleted";
  private static final String              AUDITABLE        = "auditable";
  private static final String              REQUESTABLE      = "requestable";
  private static final String              CERTIFIABLE      = "certifiable";
  private static final String              AUDIT_OBJECTIVE  = "audit_objective";
  private static final String              APPROVAL_USER    = "approver_user";
  private static final String              APU_NAME         = "apu_name";
  private static final String              APU_DISPLAY_NAME = "apu_display_name";
  private static final String              APPROVAL_ROLE    = "approver_role";
  private static final String              APR_NAME         = "apr_name";
  private static final String              APR_DISPLAY_NAME = "apr_display_name";
  private static final String              CERTIFIER_USER   = "certifier_user";
  private static final String              CTU_NAME         = "ctu_name";
  private static final String              CTU_DISPLAY_NAME = "ctu_display_name";
  private static final String              CERTIFIER_ROLE   = "certifier_role";
  private static final String              CTR_NAME         = "ctr_name";
  private static final String              CTR_DISPLAY_NAME = "ctr_display_name";
  private static final String              FULFILMENT_USER  = "fulfillment_user";
  private static final String              FFU_NAME         = "ffu_name";
  private static final String              FFU_DISPLAY_NAME = "ffu_display_name";
  private static final String              FULFILMENT_ROLE  = "fulfillment_role";
  private static final String              FFR_NAME         = "ffr_name";
  private static final String              FFR_DISPLAY_NAME = "ffr_display_name";
  private static final String              TAGS             = "tags";
  private static final String              TAGS_UDF         = "tags_udf";


  private static final Map<String, String> FILTER           = new HashMap<String, String>();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7390510216504965051")
  private static final long                serialVersionUID = -2492166963476226226L;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    synchronized(FILTER) {
      FILTER.put(CatalogItemAdapter.PK,               "cat.catalog_id");
      FILTER.put(CatalogItemAdapter.ENTITY_ID,        "cat.entity_key");
      FILTER.put(CatalogItemAdapter.TYPE,             "cat.entity_type");
      FILTER.put(CatalogItemAdapter.NAME,             "cat.entity_name");
      FILTER.put(CatalogItemAdapter.DISPLAY_NAME,     "cat.entity_display_name");
      FILTER.put(CatalogItemAdapter.DESCRIPTION,      "cat.entity_description");
      FILTER.put(CatalogItemAdapter.CATEGORY,         "cat.category");
      FILTER.put(CatalogItemAdapter.DELETED,          "cat.is_deleted");
      FILTER.put(CatalogItemAdapter.AUDITABLE,        "cat.is_auditable");
      FILTER.put(CatalogItemAdapter.REQUESTABLE,      "cat.is_requestable");
      FILTER.put(CatalogItemAdapter.CERTIFIABLE,      "cat.certifiable");
      FILTER.put(CatalogItemAdapter.AUDIT_OBJECTIVE,  "cat.audit_objective");
      FILTER.put(CatalogItemAdapter.RISK,             "cat.item_risk");
      FILTER.put(CatalogItemAdapter.RISK_UPDATE,      "cat.risk_update_date");
      FILTER.put(CatalogItemAdapter.APPROVER_USER,    "cat.approver_user");
      FILTER.put(CatalogItemAdapter.APPROVER_ROLE,    "cat.approver_role");
      FILTER.put(CatalogItemAdapter.CERTIFIER_USER,   "cat.certifier_user");
      FILTER.put(CatalogItemAdapter.CERTIFIER_ROLE,   "cat.certifier_role");
      FILTER.put(CatalogItemAdapter.FULFILLMENT_USER, "cat.fulfillment_user");
      FILTER.put(CatalogItemAdapter.FULFILLMENT_ROLE, "cat.fulfillment_role");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CatalogItemDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CatalogItemDataProvider() {
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
   **                            element is of type {@link CatalogItemAdapter}.
   */
  @Override
  public List<CatalogItemAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    Integer rangeStart = 0;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)control.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    Integer rangeEnd = 1;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd= (Integer)control.get(ConstantsDefinition.SEARCH_ENDROW);
    }
    // default search is based on ID
    final DatabaseFilter           criteria = extractCriteria(searchCriteria == null ? new SearchCriteria(CatalogItemAdapter.PK, null, SearchCriteria.Operator.NOT_EQUAL) : searchCriteria);
    final List<CatalogItemAdapter> batch    = new ArrayList<CatalogItemAdapter>();

    Connection        connection = null;
    PreparedStatement statement  = null;
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      statement  = QueryProviderFactory.instance.preparedStatement("catalog.select", DatabaseFilter.Operator.AND, connection, criteria, rangeStart, rangeEnd);
      final ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        batch.add(build(resultSet));
      }
      return new PagedArrayList<CatalogItemAdapter>(batch, rangeStart, rangeEnd);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
    finally {
      DatabaseStatement.closeStatement(statement);
      if (connection != null)
        try {
          // make sure that we will commit our unit of work if neccessary
          connection.close();
        }
        catch (SQLException e) {
          // handle silenlty
          e.printStackTrace(System.err);
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   update (overridden)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object update.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void update(final CatalogItemAdapter mab) {
    final CatalogService service = OIMClientFactory.getCatalogService();;
    try {
      // fetch the existing catalog item by its system identifier
      // all other parameters can be null especially OperationContext is never
      // used
      // Using this service method has one drawback because it will not return
      // deleted items (its hardcoded) but due we are not allowing updates on
      // deleted items it is a minor issue
      final Catalog instance = build(service.getCatalogItemDetails(mab.getCatalogId(), null, null, null), mab);
      service.updateCatalogItems(instance);
    }
    catch (CatalogException e) {
      throw new OIMRuntimeException(e);
    }
    catch (AccessDeniedException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   delete (overridden)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object delete.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The implementation follows the harvesting approach due to there isn't a
   ** official API to delete a catalog item-
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void delete(final CatalogItemAdapter mab) {
    // intentionally left blank
    
    final Repository repository = RepositoryFactory.getRepositoryInstance();
    final Catalog        item   = new Catalog();
    item.setId(mab.getCatalogId());
    item.setEntityKey(String.valueOf(mab.getEntityId()));
    item.setEntityType(OIMType.valueOf(mab.getEntityType()));
    // deletion is based on entity id and entity type
    final List<Catalog> batch   = new ArrayList<Catalog>(1);
    batch.add(item);
    try {
      final Result result = repository.deleteCatalogItems(batch);
      if (result.getException() != null)
        throw new OIMRuntimeException(result.getException());
    }
    catch (CatalogException e) {
      throw new OIMRuntimeException(e);
    }
    catch (AccessDeniedException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupIdentity
  public static IdentityAdapter lookupIdentity(final String property, final Long identifier) {
    IdentityAdapter   identity   = null;
    Connection        connection = null;
    PreparedStatement statement  = null;
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      statement  = QueryProviderFactory.instance.preparedStatement(property, connection, DatabaseFilter.build("usr_key", identifier, DatabaseFilter.Operator.EQUAL));
      final ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      identity = new IdentityAdapter();
      identity.id(resultSet.getLong(ID));
      identity.name(resultSet.getString(NAME));
      identity.displayName(resultSet.getString(DISPLAY_NAME));
      return identity;
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
    finally {
      DatabaseStatement.closeStatement(statement);
      if (connection != null)
        try {
          // make sure that we will commit our unit of work if neccessary
          connection.close();
        }
        catch (SQLException e) {
          // handle silenlty
          e.printStackTrace(System.err);
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   extractCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Catalog Item.
   **
   ** @param criteria            the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   */
  private DatabaseFilter extractCriteria(final SearchCriteria criteria) {
    DatabaseFilter filter = null;
    Object lh = criteria.getFirstArgument();
    if (lh instanceof SearchCriteria) {
      filter = extractCriteria((SearchCriteria)lh);
    }

    Object rh = criteria.getSecondArgument();
    if (rh instanceof SearchCriteria) {
      filter = (filter == null) ? extractCriteria((SearchCriteria)rh) : DatabaseFilter.build(filter, extractCriteria((SearchCriteria)rh), criteria.getOperator() == SearchCriteria.Operator.AND ? DatabaseFilter.Operator.AND : DatabaseFilter.Operator.OR);
    }
    if (!(lh instanceof SearchCriteria && rh instanceof SearchCriteria)) {
      DatabaseFilter partial = null;
      final SearchCriteria.Operator op = criteria.getOperator();
      if (StringUtility.isEqual(CatalogItemAdapter.FK, (String)lh)) {
        lh = CatalogItemAdapter.PK;
      }
      else if (StringUtility.isEqual(CatalogItemAdapter.ENTITY_FK, (String)lh)) {
        lh = CatalogItemAdapter.ENTITY_ID;
      }

      switch (op) {
        case EQUAL               : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.EQUAL);
                                   break;
        case NOT_EQUAL           : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.NOT_EQUAL);
                                   break;
        case LESS_THAN           : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.LESS_THAN);
                                   break;
        case LESS_EQUAL          : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.LESS_EQUAL);
                                   break;
        case GREATER_THAN        : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.GREATER_THAN);
                                   break;
        case GREATER_EQUAL       : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.GREATER_EQUAL);
                                   break;
        case CONTAINS            : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.CONTAINS);
                                   break;
        case ENDS_WITH           : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.ENDS_WITH);
                                   break;
        case DOES_NOT_END_WITH   : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.NOT_ENDS_WITH);
                                   break;
        case BEGINS_WITH         : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.STARTS_WITH);
                                   break;
        case DOES_NOT_BEGIN_WITH : partial = DatabaseFilter.build(FILTER.get(lh), rh, DatabaseFilter.Operator.NOT_STARTS_WITH);
                                   break;
      }
      filter = (filter == null) ? partial : DatabaseFilter.build(filter, partial, DatabaseFilter.Operator.AND);
    }
    return filter;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create a <code>CatalogItemAdapter</code> from a
   ** {@link ResultSet}.
   ** <p>
   ** <b>Note</b>:
   ** The {@link ResultSet} pointer needs to be positioned correctly before this
   ** method is called.
   **
   ** @param  resultSet          the {@link ResultSet} provider to obtain the
   **                            attribute values from.
   **                            Allowed object is {@link ResultSet}.
   **
   ** @return                    the {@link CatalogItemAdapter} constructed
   **                            from the specfied <code>resultSet</code>.
   */
  private static CatalogItemAdapter build(final ResultSet resultSet) {
    final CatalogItemAdapter mab = new CatalogItemAdapter();
    try {
      mab.setCatalogId(resultSet.getLong(PRIMARY));
      mab.setEntityId(resultSet.getLong(ID));
      mab.setEntityType(resultSet.getString(TYPE));
      mab.setEntityName(resultSet.getString(NAME));
      mab.setEntityDisplayName(resultSet.getString(DISPLAY_NAME));
      mab.setEntityDescription(resultSet.getString(DESCRIPTION));
      mab.setEntityParentId(resultSet.getLong(PARENT_ID));
      mab.setEntityParentType(resultSet.getString(PARENT_TYPE));
      mab.setCategory(resultSet.getString(CATEGORY));
      mab.setRisk(resultSet.getInt(RISK));
      mab.setRiskUpdate(resultSet.getTimestamp(RISK_UPDATE));
      mab.setDeleted(resultSet.getInt(DELETED) == 0 ? Boolean.FALSE : Boolean.TRUE);
      mab.setAuditable(resultSet.getInt(AUDITABLE) == 0 ? Boolean.FALSE : Boolean.TRUE);
      mab.setRequestable(resultSet.getInt(REQUESTABLE) == 0 ? Boolean.FALSE : Boolean.TRUE);
      mab.setCertifiable(resultSet.getInt(CERTIFIABLE) == 0 ? Boolean.FALSE : Boolean.TRUE);
      mab.setAuditObjective(resultSet.getString(AUDIT_OBJECTIVE));
      mab.setApproverUser(resultSet.getString(APPROVAL_USER));
      mab.setApproverUserName(resultSet.getString(APU_NAME));
      mab.setApproverUserDisplayName(resultSet.getString(APU_DISPLAY_NAME));
      mab.setApproverRole(resultSet.getString(APPROVAL_ROLE));
      mab.setApproverRoleName(resultSet.getString(APR_NAME));
      mab.setApproverRoleDisplayName(resultSet.getString(APR_DISPLAY_NAME));
      mab.setCertifierUser(resultSet.getString(CERTIFIER_USER));
      mab.setCertifierUserName(resultSet.getString(CTU_NAME));
      mab.setCertifierUserDisplayName(resultSet.getString(CTU_DISPLAY_NAME));
      mab.setCertifierRole(resultSet.getString(CERTIFIER_ROLE));
      mab.setCertifierRoleName(resultSet.getString(CTR_NAME));
      mab.setCertifierRoleDisplayName(resultSet.getString(CTR_DISPLAY_NAME));
      mab.setFulfillmentUser(resultSet.getString(FULFILMENT_USER));
      mab.setFulfillmentUserName(resultSet.getString(FFU_NAME));
      mab.setFulfillmentUserDisplayName(resultSet.getString(FFU_DISPLAY_NAME));
      mab.setFulfillmentRole(resultSet.getString(FULFILMENT_ROLE));
      mab.setFulfillmentRoleName(resultSet.getString(FFR_NAME));
      mab.setFulfillmentRoleDisplayName(resultSet.getString(FFR_DISPLAY_NAME));
      mab.setTags(resultSet.getString(TAGS));
      mab.setUserTags(resultSet.getString(TAGS_UDF));
    }
    catch (SQLException e) {
      throw new OIMRuntimeException(e);
    }
    return mab;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  private Catalog build(final Catalog value, final CatalogItemAdapter mab) {
    value.setItemRisk(mab.getRisk());
    value.setCategoryName(mab.getCategory());
    value.setAuditable(mab.getAuditable());
    value.setCertifiable(mab.getCertifiable());
    value.setRequestable(mab.getRequestable());
    value.setEntityDisplayName(mab.getEntityDisplayName());
    value.setEntityDescription(mab.getEntityDescription());
    value.setAuditObjectives(mab.getAuditObjective());
    value.setApproverUser(mab.getApproverUser());
    value.setApproverUserDisplayName(mab.getApproverUserDisplayName());
    value.setApproverRole(mab.getApproverRole());
    value.setApproverRoleDisplayName(mab.getApproverRoleDisplayName());
    value.setCertifierUser(mab.getCertifierUser());
    value.setCertifierUserDisplayName(mab.getCertifierUserDisplayName());
    value.setCertifierRole(mab.getCertifierRole());
    value.setCertifierRoleDisplayName(mab.getCertifierRoleDisplayName());
    value.setFulFillMentUser(mab.getFulfillmentUser());
    value.setFulFillMentUserDisplayName(mab.getFulfillmentUserDisplayName());
    value.setFulFillMentRole(mab.getFulfillmentRole());
    value.setFulFillMentRoleDisplayName(mab.getFulfillmentRoleDisplayName());
    value.setUserDefinedTags(mab.getUserTags());
    return value;
  }
}