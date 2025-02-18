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

    File        :   ReconciliationEventDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReconciliationEventDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.platform.entitymgr.spi.entity.Searchable;

import oracle.iam.reconciliation.config.vo.TargetAttribute;

import oracle.iam.reconciliation.api.ReconOperationsService;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.naming.ServiceLocator;

import oracle.iam.identity.foundation.persistence.DatabaseSort;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class ReconciliationEventDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by Reconciliation Event customization.
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
public class ReconciliationEventDataProvider extends AbstractDataProvider<ReconciliationEventAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String RCE_ALIAS        = "rce";
  private static final String OBJ_ALIAS        = "obj";
  private static final String RCE_KEY          = "re_key";
  private static final String JOB_KEY          = "rj_key";
  private static final String BATCH_KEY        = "rb_key";
  private static final String OBJ_KEY          = "obj_key";
  private static final String OBJ_NAME         = "obj_name";
  private static final String USR_KEY          = "usr_key";
  private static final String ORG_KEY          = "act_key";
  private static final String ACT_KEY          = "orc_key";
  private static final String ENTITY_TYPE      = "re_entity_type";
  private static final String CHANGE_TYPE      = "re_change_type";
  private static final String STATUS           = "re_status";
  private static final String LINK_SOURCE      = "re_link_source";
  private static final String NOTE             = "re_note";
  private static final String REASON           = "re_reason";
  private static final String ACTION_DATE      = "re_action_date";
  private static final String KEY_FIELD        = "re_keyfield";

  private static final String SELECT           = "event.reconciled";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4525363951499312688")
  private static final long   serialVersionUID = -7967082480690185193L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ReconciliationEventDataProvider</code> data
   ** access object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ReconciliationEventDataProvider() {
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
   **                            {@link ReconciliationEventAdapter}.
   */
  @Override
  public List<ReconciliationEventAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    Integer rangeStart = 0;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)control.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    Integer rangeEnd = 1;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd= (Integer)control.get(ConstantsDefinition.SEARCH_ENDROW);
    }

    // default search is based on key
    final DatabaseFilter          criteria  = extractCriteria(searchCriteria == null ? new SearchCriteria(ReconciliationEventAdapter.PK, null, SearchCriteria.Operator.NOT_EQUAL) : searchCriteria);
    // default search is sorted on name and ascending
    final HashMap<String, Object> parameter = convertParameter(control);
    if (!parameter.containsKey(ConstantsDefinition.SEARCH_SORTEDBY)) {
      parameter.put(ConstantsDefinition.SEARCH_SORTEDBY,  RCE_KEY);
      parameter.put(ConstantsDefinition.SEARCH_SORTORDER, Searchable.SortOrder.DESCENDING);
    }
    final String                           sorted  = (String)parameter.get(ConstantsDefinition.SEARCH_SORTEDBY);
    final Searchable.SortOrder             order   = (Searchable.SortOrder)parameter.get(ConstantsDefinition.SEARCH_SORTORDER);
    final DatabaseSort                     orderby = DatabaseSort.build(sorted, (order == null ||  order == Searchable.SortOrder.ASCENDING) ? DatabaseSort.Operator.ASCENDING : DatabaseSort.Operator.DESCENDING);
    final List<ReconciliationEventAdapter> batch   = new ArrayList<ReconciliationEventAdapter>();

    Connection        connection = null;
    PreparedStatement statement  = null;
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      statement  = QueryProviderFactory.instance.preparedStatement(SELECT, DatabaseFilter.Operator.AND, connection, criteria, orderby, rangeStart, rangeEnd);
      final ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        batch.add(build(resultSet));
      }
      return new PagedArrayList<ReconciliationEventAdapter>(batch, rangeStart, rangeEnd);
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
  // Method:   closeEvent
  /**
   ** Close the <code>Reconciliation Event</code> with the given
   ** identifier.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Reconciliation Event</code> to
   **                            reevaluate.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void closeEvent(final Long identifier) {
    final ReconOperationsService facade = service(ReconOperationsService.class);
    try {
      facade.closeReconciliationEvent(identifier);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reevaluateEvent
  /**
   ** Re-Evaluates the <code>Reconciliation Event</code> with the given
   ** identifier.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Reconciliation Event</code> to
   **                            reevaluate.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void reevaluateEvent(final Long identifier) {
    final ReconOperationsService facade = service(ReconOperationsService.class);
    try {
      facade.processReconciliationEvent(identifier);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   linkEvent (ReconciliationEventAM)
  /**
   ** Link the <code>Reconciliation Event</code> with the given
   ** identifier to the specififed identity.
   **
   ** @param  identifier         the system identifier of the
   **                            <code>Reconciliation Event</code> to link.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  identity           the system identifier of the
   **                            <code>Identity</code> to link.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void linkEvent(final Long identifier, final Long identity) {
    final ReconOperationsService facade = service(ReconOperationsService.class);
    try {
      facade.linkEventToResourceInstanceForUser(identifier, identity);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
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
   **                            Allowed object is {@link HashMap} where each
   **                            element is of type [@link String} for the key
   **                            and {@link Object} for the value.
   **
   ** @return                    the attribute name exposed by the OIM kernel
   **                            API if any.
   **                            <br>
   **                            Possible object is {@link HashMap} where each
   **                            element is of type [@link String} for the key
   **                            and {@link Object} for the value.
   */
  private static HashMap<String, Object> convertParameter(final HashMap<String, Object> parameter) {
    // prevent bogus input
    if (parameter == null || parameter.size() == 0)
      return parameter;

    if (parameter.containsKey(ConstantsDefinition.SEARCH_SORTEDBY)) {
      String value = parameter.get(ConstantsDefinition.SEARCH_SORTEDBY).toString();
      parameter.put(ConstantsDefinition.SEARCH_SORTEDBY, convertAttributeName(value));
    }
    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   convertAttributeName
  /**
   ** Converts the attribute name defined in the ADF view query criteria to the
   ** natural names exposed by the OIM database.
   **
   ** @param  outboundName       the attribute name defined in the ADF view
   **                            query criteria.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the attribute name exposed by the OIM database
   **                            if any.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String convertAttributeName(final String outboundName) {
    // prevent bogus input
    String inboundName = outboundName;
    if (inboundName == null)
      return null;

    inboundName = inboundName.trim();
    if (inboundName.equalsIgnoreCase(ReconciliationEventAdapter.PK)) {
      inboundName = RCE_KEY;
    }
    else if (inboundName.equalsIgnoreCase(ReconciliationEventAdapter.JOB_KEY)) {
      inboundName = JOB_KEY;
    }
    else if (inboundName.equalsIgnoreCase(ReconciliationEventAdapter.BATCH_KEY)) {
      inboundName = BATCH_KEY;
    }
    else if (inboundName.equalsIgnoreCase(ReconciliationEventAdapter.OBJ_KEY)) {
      inboundName = OBJ_KEY;
    }
    else if (inboundName.equalsIgnoreCase(ReconciliationEventAdapter.OBJ_NAME)) {
      inboundName = OBJ_NAME;
    }
    else if (inboundName.equalsIgnoreCase(ReconciliationEventAdapter.ENTITY_TYPE)) {
      inboundName = ENTITY_TYPE;
    }
    else if (inboundName.equalsIgnoreCase(ReconciliationEventAdapter.CHANGE_TYPE)) {
      inboundName = CHANGE_TYPE;
    }

    return inboundName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   extractCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Reconciliation Event.
   **
   ** @param criteria            the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   **
   ** @return                    the {@link DatabaseFilter} to apply on a
   **                            search.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  private static DatabaseFilter extractCriteria(final SearchCriteria criteria) {
    final Object lhs = criteria.getFirstArgument();
    DatabaseFilter filter = null;
    if (lhs instanceof SearchCriteria) {
      filter = extractCriteria((SearchCriteria)lhs);
    }

    final Object rhs = criteria.getSecondArgument();
    if (rhs instanceof SearchCriteria) {
      filter = (filter == null) ? extractCriteria((SearchCriteria)rhs) : DatabaseFilter.build(filter, extractCriteria((SearchCriteria)rhs), DatabaseFilter.Operator.AND);
    }

    if (!(lhs instanceof SearchCriteria && rhs instanceof SearchCriteria)) {
      DatabaseFilter partial = null;
      final String                  name = (String)lhs;
      final SearchCriteria.Operator op   = criteria.getOperator();
      if (ReconciliationEventAdapter.PK.equalsIgnoreCase((String)lhs)) {
        partial = createFilter(op, RCE_KEY, rhs);
      }
      else if (ReconciliationEventAdapter.JOB_KEY.equalsIgnoreCase(name)) {
        partial = createFilter(op, JOB_KEY, rhs);
      }
      else if (ReconciliationEventAdapter.BATCH_KEY.equalsIgnoreCase(name)) {
        partial = createFilter(op, BATCH_KEY, rhs);
      }
      // verify if the search is initiated by a view link which means that the
      // serach criteria contains a object key prefixed with 'Bind_'
      else if (ReconciliationEventAdapter.OBJ_KEY.equalsIgnoreCase(name) || ReconciliationEventAdapter.BIND_OBJ_KEY.equalsIgnoreCase(name)) {
        partial = createFilter(op, OBJ_KEY, rhs);
      }
      else if (ReconciliationEventAdapter.OBJ_NAME.equalsIgnoreCase(name)) {
        final String qualified = String.format("%s.%s", OBJ_ALIAS, OBJ_NAME);
        partial = createFilter(op, qualified, rhs);
      }
      else if (ReconciliationEventAdapter.USR_KEY.equalsIgnoreCase(name)) {
        partial = createFilter(op, USR_KEY, rhs);
      }
      else if (ReconciliationEventAdapter.ORG_KEY.equalsIgnoreCase(name)) {
        partial = createFilter(op, BATCH_KEY, rhs);
      }
      else if (ReconciliationEventAdapter.ACT_KEY.equalsIgnoreCase(name)) {
        partial = createFilter(op, BATCH_KEY, rhs);
      }
      else if (ReconciliationEventAdapter.ENTITY_TYPE.equalsIgnoreCase(name)) {
        partial = createFilter(op, ENTITY_TYPE, rhs);
      }
      else if (ReconciliationEventAdapter.CHANGE_TYPE.equalsIgnoreCase(name)) {
        partial = createFilter(op, CHANGE_TYPE, rhs);
      }
      else if (ReconciliationEventAdapter.STATUS.equalsIgnoreCase(name)) {
        partial = createFilter(op, STATUS, rhs);
      }
      else if (ReconciliationEventAdapter.LINK_SOURCE.equalsIgnoreCase(name)) {
        partial = createFilter(op, LINK_SOURCE, rhs);
      }
      else if (ReconciliationEventAdapter.NOTE.equalsIgnoreCase(name)) {
        partial = createFilter(op, NOTE, rhs);
      }
      else if (ReconciliationEventAdapter.REASON.equalsIgnoreCase(name)) {
        partial = createFilter(op, REASON, rhs);
      }
      else if (ReconciliationEventAdapter.ACTION_DATE.equalsIgnoreCase(name)) {
        partial = createFilter(op, ACTION_DATE, rhs);
      }
      else if (ReconciliationEventAdapter.KEY_FIELD.equalsIgnoreCase(name)) {
        partial = createFilter(op, KEY_FIELD, rhs);
      }
      filter = (filter == null) ? partial : DatabaseFilter.build(filter, partial, DatabaseFilter.Operator.AND);
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   createFilter
  /**
   ** Factory method to create a {@link DatabaseFilter}.
   **
   ** @param  operator           the filter operator.
   **                            <br>
   **                            Allowed object is
   **                            {@link SearchCriteria.Operator}.
   ** @param  column             the name of the column the created filter will
   **                            take in account.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value used to filter on.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the {@link DatabaseFilter} to apply on a
   **                            search.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  private static DatabaseFilter createFilter(final SearchCriteria.Operator operator, final String column, final Object value) {
    final String qualified = String.format("%s.%s", RCE_ALIAS, column);
    switch (operator) {
      case GREATER_EQUAL : return DatabaseFilter.build(qualified, value, DatabaseFilter.Operator.GREATER_EQUAL);
      case GREATER_THAN  : return DatabaseFilter.build(qualified, value, DatabaseFilter.Operator.GREATER_THAN);
      case LESS_EQUAL    : return DatabaseFilter.build(qualified, value, DatabaseFilter.Operator.LESS_EQUAL);
      case LESS_THAN     : return DatabaseFilter.build(qualified, value, DatabaseFilter.Operator.LESS_THAN);
      case EQUAL         : return DatabaseFilter.build(qualified, value, DatabaseFilter.Operator.EQUAL);
      case NOT_EQUAL     : return DatabaseFilter.build(qualified, value, DatabaseFilter.Operator.NOT_EQUAL);
      case ENDS_WITH     : return DatabaseFilter.build(qualified, value, DatabaseFilter.Operator.ENDS_WITH);
      case CONTAINS      : return DatabaseFilter.build(qualified, value, DatabaseFilter.Operator.CONTAINS);
      default            : return DatabaseFilter.build(qualified, value, DatabaseFilter.Operator.STARTS_WITH);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create a <code>ReconciliationProfileAdapter</code> from
   ** a {@link TargetAttribute}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The {@link ResultSet} pointer needs to be positioned correctly before this
   ** method is called.
   **
   ** @param  resultSet          the {@link ResultSet} provider to obtain the
   **                            attribute values from.
   **                            <br>
   **                            Allowed object is {@link ResultSet}.
   **
   ** @return                    the {@link ReconciliationEventAdapter}
   **                            constructed from the specfied
   **                            <code>attribute</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link ReconciliationEventAdapter}.
   */
  private static ReconciliationEventAdapter build(final ResultSet resultSet)
    throws SQLException {

    final ReconciliationEventAdapter mab = ReconciliationEventAdapter.build();
    mab.setEventKey(resultSet.getLong(RCE_KEY));
    mab.setJobKey(resultSet.getLong(JOB_KEY));
    mab.setBatchKey(resultSet.getLong(BATCH_KEY));
    mab.setObjectKey(resultSet.getLong(OBJ_KEY));
    mab.setObjectName(resultSet.getString(OBJ_NAME));
    mab.setUserKey(resultSet.getLong(USR_KEY));
    mab.setOrganizationKey(resultSet.getLong(ORG_KEY));
    mab.setAccountKey(resultSet.getLong(ACT_KEY));
    mab.setEntityType(resultSet.getString(ENTITY_TYPE));
    mab.setChangeType(resultSet.getString(CHANGE_TYPE));
    mab.setStatus(resultSet.getString(STATUS));
    mab.setLinkSource(resultSet.getString(LINK_SOURCE));
    mab.setNote(resultSet.getString(NOTE));
    mab.setReason(resultSet.getString(REASON));
    mab.setActionDate(resultSet.getDate(ACTION_DATE));
    mab.setKeyField(resultSet.getString(KEY_FIELD));
    return mab;
  }
}