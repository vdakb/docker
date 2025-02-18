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

    File        :   EntitlementInstanceDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementInstanceDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.sql.ResultSet;

import java.sql.SQLException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.provisioning.vo.RiskSummary;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.naming.ServiceLocator;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementInstanceDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
public class EntitlementInstanceDataProvider extends AbstractDataProvider<EntitlementInstanceAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String              ENT_ASSIGN_KEY            = "ent_assign_key";
  private static final String              ENT_LIST_KEY              = "ent_list_key";
  private static final String              ENT_LIST_CODE             = "ent_code";
  private static final String              ENT_LIST_VALUE            = "ent_value";
  private static final String              ENT_LIST_NAME             = "ent_display_name";
  private static final String              ENT_ASSIGN_STATUS         = "ent_status";
  private static final String              ENT_ASSIGN_FROM           = "valid_from_date";
  private static final String              ENT_ASSIGN_UNTIL          = "valid_to_date";
  private static final String              ENT_ASSIGN_MECHANISM      = "ent_assign_prov_mechanism";
  private static final String              ENT_ASSIGN_RISK_UPDATE    = "ent_assign_risk_update_date";
  private static final String              ENT_ASSIGN_ITEM_RISK      = "ent_assign_item_risk";
  private static final String              ENT_ASSIGN_MECHANISM_RISK = "ent_assign_prov_mech_risk";
  private static final String              ENT_ASSIGN_SUMMARY_RISK   = "ent_assign_summary_risk";
  private static final String              IDENTITY_KEY              = "usr_key";
  private static final String              IDENTITY_LOGIN_NAME       = "usr_login";
  private static final String              IDENTITY_FIRST_NAME       = "usr_first_name";
  private static final String              IDENTITY_LAST_NAME        = "usr_last_name";
  private static final String              ORC_KEY                   = "orc_key";
  private static final String              ORC_NAME                  = "orc_tos_instance_key";
  private static final String              ORC_TYPE                  = "account_type";
  private static final String              ACT_KEY                   = "act_key";
  private static final String              ACT_NAME                  = "act_name";
  private static final String              OBJ_KEY                   = "obj_key";
  private static final String              OBJ_NAME                  = "obj_name";

  private static final String              SELECT                    = "entitlement.provisioned";

  private static final Map<String, String> FILTER                    = new HashMap<String, String>();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3126868619871150508")
  private static final long                serialVersionUID          = 6118087847570009810L;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    synchronized(FILTER) {
      FILTER.put(ENT_ASSIGN_KEY,       "ent_assign." + ENT_ASSIGN_KEY);
      FILTER.put(ENT_ASSIGN_STATUS,    "ent_assign." + ENT_ASSIGN_STATUS);
      FILTER.put(ENT_ASSIGN_MECHANISM, "ent_assign." + ENT_ASSIGN_MECHANISM);
      FILTER.put(ENT_LIST_KEY,         "ent_list."   + ENT_LIST_KEY);
      FILTER.put(IDENTITY_LOGIN_NAME,  "usr."        + IDENTITY_LOGIN_NAME);
      FILTER.put(ORC_NAME,             "orc."        + ORC_NAME);
      FILTER.put(ACT_NAME,             "act."        + ACT_NAME);
      FILTER.put(OBJ_NAME,             "obj."        + OBJ_NAME);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntitlementInstanceDataProvider</code> data
   ** access object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntitlementInstanceDataProvider() {
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
   **                            {@link EntitlementInstanceAdapter}.
   */
  @Override
  public List<EntitlementInstanceAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // TODO This impl doesn't support QBE because of API lacks support.

    Integer rangeStart = 0;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)control.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    Integer rangeEnd = 1;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd= (Integer)control.get(ConstantsDefinition.SEARCH_ENDROW);
    }
    final DatabaseFilter                   criteria = extractCriteria(searchCriteria);
    final List<EntitlementInstanceAdapter> batch    = new ArrayList<EntitlementInstanceAdapter>();

    Connection        connection = null;
    PreparedStatement statement  = null;
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      statement  = QueryProviderFactory.instance.preparedStatement(SELECT, DatabaseFilter.Operator.AND, connection, criteria, rangeStart, rangeEnd);
      final ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        final EntitlementInstanceAdapter mab = new EntitlementInstanceAdapter();
        mab.setInstanceKey(resultSet.getLong(ENT_ASSIGN_KEY));
        mab.setEntitlementKey(resultSet.getLong(ENT_LIST_KEY));
        mab.setEntitlementCode(resultSet.getString(ENT_LIST_CODE));
        mab.setEntitlementValue(resultSet.getString(ENT_LIST_VALUE));
        mab.setEntitlementDisplayName(resultSet.getString(ENT_LIST_NAME));
        mab.setResourceKey(resultSet.getLong(OBJ_KEY));
        mab.setResourceName(resultSet.getString(OBJ_NAME));
        mab.setBeneficiaryKey(resultSet.getLong(IDENTITY_KEY));
        mab.setBeneficiaryLogin(resultSet.getString(IDENTITY_LOGIN_NAME));
        mab.setBeneficiaryFirstName(resultSet.getString(IDENTITY_FIRST_NAME));
        mab.setBeneficiaryLastName(resultSet.getString(IDENTITY_LAST_NAME));
        mab.setOrganizationKey(resultSet.getLong(ACT_KEY));
        mab.setOrganizationName(resultSet.getString(ACT_NAME));
        mab.setAccountKey(resultSet.getLong(ORC_KEY));
        mab.setAccountName(resultSet.getString(ORC_NAME));
        mab.setAccountType(resultSet.getString(ORC_TYPE));
        mab.setProvisionStatus(resultSet.getString(ENT_ASSIGN_STATUS));
        mab.setProvisionMechanism(resultSet.getString(ENT_ASSIGN_MECHANISM));
        mab.setValidFrom(resultSet.getDate(ENT_ASSIGN_FROM));
        mab.setValidUntil(resultSet.getDate(ENT_ASSIGN_UNTIL));
        final RiskSummary risk = new RiskSummary(
          // provisioningMethodRisk
          resultSet.getInt(ENT_ASSIGN_MECHANISM_RISK)
          // itemRisk
        , resultSet.getInt(ENT_ASSIGN_ITEM_RISK)
          // openSodViolations
        , false
          // lastCertificationDecision
        , null
          // lastCertificationRisk
        , null
          // riskUpdateDate
        , resultSet.getDate(ENT_ASSIGN_RISK_UPDATE)
          // summaryRisk
        , resultSet.getInt(ENT_ASSIGN_SUMMARY_RISK)
        );
        mab.setRiskSummary(risk);
        batch.add(mab);
      }
      return new PagedArrayList<EntitlementInstanceAdapter>(batch, rangeStart, rangeEnd);
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   extractCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Entitlement Instance.
   **
   ** @param criteria            the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   */
  private DatabaseFilter extractCriteria(final SearchCriteria criteria) {
    final Object lh = criteria.getFirstArgument();
    DatabaseFilter filter = null;
    if (lh instanceof SearchCriteria) {
      filter = extractCriteria((SearchCriteria)lh);
    }

    final Object rh = criteria.getSecondArgument();
    if (rh instanceof SearchCriteria) {
      filter = (filter == null) ? extractCriteria((SearchCriteria)rh) : DatabaseFilter.build(filter, extractCriteria((SearchCriteria)rh), DatabaseFilter.Operator.AND);
    }

    if (!(lh instanceof SearchCriteria && rh instanceof SearchCriteria)) {
      DatabaseFilter partial = null;
      final SearchCriteria.Operator op = criteria.getOperator();
      if (((String)lh).equalsIgnoreCase("Bind_entitlementKey") || ((String)lh).equalsIgnoreCase(EntitlementInstanceAdapter.ENTITLEMENT_KEY)) {
        partial = DatabaseFilter.build(FILTER.get(ENT_LIST_KEY), rh, DatabaseFilter.Operator.EQUAL);
      }
      else if (((String)lh).equalsIgnoreCase("Bind_instanceKey") || ((String)lh).equalsIgnoreCase(EntitlementInstanceAdapter.KEY)) {
        partial = DatabaseFilter.build(FILTER.get(ENT_ASSIGN_KEY), rh, DatabaseFilter.Operator.EQUAL);
      }
      else if (EntitlementInstanceAdapter.STATUS.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(ENT_ASSIGN_STATUS), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(ENT_ASSIGN_STATUS), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      else if (EntitlementInstanceAdapter.MECHANISM.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(ENT_ASSIGN_MECHANISM), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(ENT_ASSIGN_MECHANISM), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      else if (EntitlementInstanceAdapter.ACCOUNT_NAME.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(ORC_NAME), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(ORC_NAME), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      else if (EntitlementInstanceAdapter.BENEFICIARY_LOGIN.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(IDENTITY_LOGIN_NAME), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(IDENTITY_LOGIN_NAME), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      filter = (filter == null) ? partial : DatabaseFilter.build(filter, partial, DatabaseFilter.Operator.AND);
    }
    return filter;
  }
}