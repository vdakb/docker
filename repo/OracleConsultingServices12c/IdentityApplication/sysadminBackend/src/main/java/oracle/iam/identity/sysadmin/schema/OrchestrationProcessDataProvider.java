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
    Subsystem   :   System Administration Management

    File        :   OrchestrationProcessDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OrchestrationProcessDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;


import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.hst.foundation.naming.ServiceLocator;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class OrchestrationProcessDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by System Administration Management customization.
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
public class OrchestrationProcessDataProvider extends JMXDataProvider<OrchestrationProcessAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String   ORP_ALIAS        = "orp";
  private static final String   ORP_KEY          = "id";
  private static final String   ORP_NAME         = "name";
  private static final String   ORP_STAGE        = "stage";
  private static final String   ORP_OPERATION    = "operation";
  private static final String   ORP_CHANGETYPE   = "changetype";
  private static final String   ORP_ENTITYID     = "entityid";
  private static final String   ORP_ENTITYTYPE   = "entitytype";
  private static final String   ORP_BULKPARENT   = "bulkparentid";
  private static final String   ORP_PARENT       = "parentprocessid";
  private static final String   ORP_DEPENDEND    = "depprocessid";
  private static final String   ORP_STATUS       = "status";
  private static final String   ORP_RETRY        = "retry";
  private static final String   ORP_CREATEDON    = "createdon";
  private static final String   ORP_MODIFIEDON   = "modifiedon";

  private static final String   SELECT           = "orp.select";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3920790143944817505")
  private static final long     serialVersionUID = -5445191004020982940L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OrchestrationProcessDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OrchestrationProcessDataProvider() {
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
   **                            {@link OrchestrationProcessAdapter}.
   */
  @Override
  public List<OrchestrationProcessAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    Integer rangeStart = 0;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)control.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    Integer rangeEnd = 1;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd= (Integer)control.get(ConstantsDefinition.SEARCH_ENDROW);
    }

    // default search is based on key
    final DatabaseFilter                    criteria = extractCriteria(searchCriteria == null ? new SearchCriteria(OrchestrationProcessAdapter.PK, null, SearchCriteria.Operator.NOT_EQUAL) : searchCriteria);
    final List<OrchestrationProcessAdapter> batch    = new ArrayList<OrchestrationProcessAdapter>();

    Connection        connection = null;
    PreparedStatement statement  = null;
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      statement  = QueryProviderFactory.instance.preparedStatement(SELECT, null, connection, criteria, rangeStart, rangeEnd);
      // execute the sql statement and parse the result set
      final ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        final OrchestrationProcessAdapter mab = new OrchestrationProcessAdapter();
        mab.setProcessId(resultSet.getLong(ORP_KEY));
        mab.setProcessName(resultSet.getString(ORP_NAME));
        mab.setBulkParentId(resultSet.getLong(ORP_BULKPARENT));
        mab.setStatus(resultSet.getString(ORP_STATUS));
        mab.setParentId(resultSet.getLong(ORP_PARENT));
        mab.setDependendId(resultSet.getLong(ORP_DEPENDEND));
        mab.setEntityId(resultSet.getString(ORP_ENTITYID));
        mab.setEntityType(resultSet.getString(ORP_ENTITYTYPE));
        mab.setOperation(resultSet.getString(ORP_OPERATION));
        mab.setStage(resultSet.getString(ORP_STAGE));
        mab.setChangeType(resultSet.getString(ORP_CHANGETYPE));
        mab.setRetry(resultSet.getLong(ORP_RETRY));
        mab.setCreatedOn(resultSet.getTimestamp(ORP_CREATEDON));
        mab.setModifiedOn(resultSet.getTimestamp(ORP_MODIFIEDON));
        //mab.setOutOfBandEvents(resultSet.getString(ORP_OOPERATION));

        batch.add(mab);
      }
      return new PagedArrayList<OrchestrationProcessAdapter>(batch, rangeStart, rangeEnd);
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
  // Methods of grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   extractCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Process Instance.
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
      if (OrchestrationProcessAdapter.PK.equalsIgnoreCase((String)lh)) {
        partial = createFilter(op, ORP_KEY, rh);
      }
      else if (OrchestrationProcessAdapter.NAME.equalsIgnoreCase((String)lh)) {
        partial = createFilter(op, ORP_NAME, rh);
      }
      else if (OrchestrationProcessAdapter.OPERATION.equalsIgnoreCase((String)lh)) {
        partial = createFilter(op, ORP_OPERATION, rh);
      }
      else if (OrchestrationProcessAdapter.STAGE.equalsIgnoreCase((String)lh)) {
        partial = createFilter(op, ORP_STAGE, rh);
      }
      else if (OrchestrationProcessAdapter.CHANGETYPE.equalsIgnoreCase((String)lh)) {
        partial = createFilter(op, ORP_CHANGETYPE, rh);
      }
      else if (OrchestrationProcessAdapter.STATUS.equalsIgnoreCase((String)lh)) {
        partial = createFilter(op, ORP_STATUS, rh);
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
   ** @param  column             the name of the column the created filter will
   **                            take in account.
   ** @param  value              the value used to filter on.
   */
  private static DatabaseFilter createFilter(final SearchCriteria.Operator operator, final String column, final Object value) {
    final String qualified = String.format("%s.%s", ORP_ALIAS, column);
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
}