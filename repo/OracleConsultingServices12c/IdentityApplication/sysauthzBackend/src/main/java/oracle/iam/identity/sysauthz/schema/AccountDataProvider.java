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

    File        :   AccountDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountDataProvider.


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

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.ui.platform.utils.SearchCriteriaUtils;

import oracle.hst.foundation.naming.ServiceLocator;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class AccountDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
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
public class AccountDataProvider extends AbstractDataProvider<AccountAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String              OIU_KEY                     = "oiu_key";
  private static final String              APP_INSTANCE_KEY            = "app_instance_key";
  private static final String              ACCOUNT_TYPE                = "account_type";
  private static final String              REQUEST_KEY                 = "request_key";
  private static final String              POLICY_KEY                  = "pol_key";
  private static final String              USR_KEY                     = "usr_key";
  private static final String              USR_LOGIN                   = "usr_login";
  private static final String              USR_FIRST_NAME              = "usr_first_name";
  private static final String              USR_LAST_NAME               = "usr_last_name";
  private static final String              ACT_KEY                     = "act_key";
  private static final String              ACT_NAME                    = "act_name";
  private static final String              SERVICE_ACCOUNT             = "oiu_serviceaccount";
  private static final String              PROVISIONING_START          = "oiu_prov_start_date";
  private static final String              PROVISIONING_END            = "oiu_prov_end_date";
  private static final String              MECHANISM                   = "oiu_prov_mechanism";
  private static final String              RISK_MECHANISM              = "oiu_prov_mech_risk";
  private static final String              RISK_ITEM                   = "oiu_item_risk";
  private static final String              RISK_SUMMARY                = "oiu_summary_risk";
  private static final String              RISK_UPDATE                 = "oiu_risk_update_date";
  private static final String              LAST_CERTIFICATION_RISK     = "oiu_last_cert_risk";
  private static final String              LAST_CERTIFICATION_DECISION = "oiu_last_cert_decision";
  private static final String              SOD_VIOLATION               = "oiu_open_sod_violation";
  private static final String              OBI_KEY                     = "obi_key";
  private static final String              OBI_STATUS                  = "obi_status";
  private static final String              OBJ_KEY                     = "obj_key";
  private static final String              OBJ_NAME                    = "obj_name";
  private static final String              OBJ_TYPE                    = "obj_type";
  private static final String              ORC_KEY                     = "orc_key";
  private static final String              ORC_NAME                    = "orc_tos_instance_key";
  private static final String              ORC_STATUS                  = "orc_status";
  private static final String              ORC_ARCHIVED                = "orc_tasks_archived";
  private static final String              ORC_CREATE                  = "orc_create";
  private static final String              ORC_UPDATE                  = "orc_update";
  private static final String              OST_STATUS                  = "ost_status";

  private static final Map<String, String> FILTER                      = new HashMap<String, String>();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2796553054455290451")
  private static final long                serialVersionUID            = 338559125221077619L;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    synchronized(FILTER) {
      FILTER.put(AccountAdapter.APPLICATION_INSTANCE_KEY,   "oiu." + APP_INSTANCE_KEY);
      FILTER.put(AccountAdapter.OBJECT_NAME,                "obj." + OBJ_NAME);
      FILTER.put(AccountAdapter.OBJECT_TYPE,                "obj." + OBJ_TYPE);
      FILTER.put(AccountAdapter.PROCESS_INSTANCE_STATUS,    "ost." + OST_STATUS);
      FILTER.put(AccountAdapter.BENEFICIARY_LOGIN,          "usr." + USR_LOGIN);
      FILTER.put(AccountAdapter.BENEFICIARY_FIRST_NAME,     "usr." + USR_FIRST_NAME);
      FILTER.put(AccountAdapter.BENEFICIARY_LAST_NAME,      "usr." + USR_LAST_NAME);
      FILTER.put(AccountAdapter.MECHANISM,                  "oiu." + MECHANISM);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountDataProvider</code> data access object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountDataProvider() {
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
   **                            element is of type {@link AccountAdapter}.
   */
  @Override
  public List<AccountAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    final List<AccountAdapter> batch = new ArrayList<AccountAdapter>();
    // verify if the search is initiated by a view link which means that the
    // serach criteria contains either object key or the application instance
    // key prefixed with 'Bind_'
    SearchCriteria foreign  = SearchCriteriaUtils.findCriteriaWithName(searchCriteria, "Bind_appInstanceKey");
    if (foreign == null) {
      foreign = SearchCriteriaUtils.findCriteriaWithName(searchCriteria, "Bind_objectsKey");
      if (foreign == null)
        return batch;
    }

    Integer rangeStart = 0;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)control.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    Integer rangeEnd = 1;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd= (Integer)control.get(ConstantsDefinition.SEARCH_ENDROW);
    }

    Connection        connection = null;
    PreparedStatement statement  = null;
    DatabaseFilter    criteria   = extractCriteria(searchCriteria);
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      statement  = QueryProviderFactory.instance.preparedStatement("account.provisioned", DatabaseFilter.Operator.AND, connection, criteria, rangeStart, rangeEnd);
      final ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        final AccountAdapter mab = new AccountAdapter();
        mab.setAppInstanceKey(resultSet.getLong(APP_INSTANCE_KEY));
        mab.setAccountKey(resultSet.getLong(OIU_KEY));
        mab.setAccountType(resultSet.getString(ACCOUNT_TYPE));
        mab.setAccountProvisioningStart(resultSet.getDate(PROVISIONING_START));
        mab.setAccountProvisioningEnd(resultSet.getDate(PROVISIONING_END));
        mab.setAccountMechanism(resultSet.getString(MECHANISM));
        mab.setAccountRiskMechanism(resultSet.getInt(RISK_MECHANISM));
        mab.setAccountRiskItem(resultSet.getInt(RISK_ITEM));
        mab.setAccountRiskSummary(resultSet.getInt(RISK_SUMMARY));
        mab.setAccountRiskUpdate(resultSet.getDate(RISK_UPDATE));
        mab.setAccountLastCerificationRisk(resultSet.getInt(LAST_CERTIFICATION_RISK));
        mab.setAccountLastCerificationDecision(resultSet.getInt(LAST_CERTIFICATION_DECISION));
        mab.setRequestKey(resultSet.getLong(REQUEST_KEY));
        mab.setPolicyKey(resultSet.getLong(POLICY_KEY));
        mab.setObjectInstanceKey(resultSet.getLong(OBI_KEY));
        mab.setObjectInstanceStatus(resultSet.getString(OBI_STATUS));
        mab.setProcessInstanceKey(resultSet.getLong(ORC_KEY));
        mab.setProcessInstanceName(resultSet.getString(ORC_NAME));
        mab.setProcessInstanceStatus(resultSet.getString(OST_STATUS));
//        mab.setProcessInstanceArchived(resultSet.getString(ORC_ARCHIVED));
        mab.setProcessInstanceCreateDate(resultSet.getDate(ORC_CREATE));
        mab.setProcessInstanceUpdateDate(resultSet.getDate(ORC_UPDATE));
        mab.setResourceKey(resultSet.getLong(OBJ_KEY));
        mab.setResourceName(resultSet.getString(OBJ_NAME));
        mab.setResourceType(resultSet.getString(OBJ_TYPE));
        mab.setBeneficiaryKey(resultSet.getLong(USR_KEY));
        mab.setBeneficiaryLogin(resultSet.getString(USR_LOGIN));
        mab.setBeneficiaryFirstName(resultSet.getString(USR_FIRST_NAME));
        mab.setBeneficiaryLastName(resultSet.getString(USR_LAST_NAME));
        mab.setOrganizationKey(resultSet.getLong(ACT_KEY));
        mab.setOrganizationName(resultSet.getString(ACT_NAME));
        batch.add(mab);
      }
      return new PagedArrayList<AccountAdapter>(batch, rangeStart, rangeEnd);
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
      if (((String)lh).equalsIgnoreCase(ApplicationInstanceAdapter.FK) || ((String)lh).equalsIgnoreCase(ApplicationInstanceAdapter.PK)) {
        partial = DatabaseFilter.build(FILTER.get(ApplicationInstanceAdapter.PK), rh, DatabaseFilter.Operator.EQUAL);
      }
      else if (AccountAdapter.PROCESS_INSTANCE_STATUS.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.PROCESS_INSTANCE_STATUS), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.PROCESS_INSTANCE_STATUS), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      else if (AccountAdapter.MECHANISM.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.MECHANISM), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.MECHANISM), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      else if (AccountAdapter.BENEFICIARY_LOGIN.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.BENEFICIARY_LOGIN), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.BENEFICIARY_LOGIN), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      else if (AccountAdapter.BENEFICIARY_FIRST_NAME.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.BENEFICIARY_FIRST_NAME), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.BENEFICIARY_FIRST_NAME), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      else if (AccountAdapter.BENEFICIARY_LAST_NAME.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.BENEFICIARY_LAST_NAME), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.BENEFICIARY_LAST_NAME), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      else if (AccountAdapter.ORGANIZATION_NAME.equalsIgnoreCase((String)lh)) {
        switch (op) {
          case EQUAL       : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.ORGANIZATION_NAME), rh, DatabaseFilter.Operator.EQUAL);
                             break;
          case BEGINS_WITH : partial = DatabaseFilter.build(FILTER.get(AccountAdapter.ORGANIZATION_NAME), rh, DatabaseFilter.Operator.STARTS_WITH);
                             break;
        }
      }
      filter = (filter == null) ? partial : DatabaseFilter.build(filter, partial, DatabaseFilter.Operator.AND);
    }
    return filter;
  }
}