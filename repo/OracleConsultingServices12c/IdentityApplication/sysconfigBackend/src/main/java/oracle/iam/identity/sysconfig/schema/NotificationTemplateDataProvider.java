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
    Subsystem   :   System Configuration Management

    File        :   NotificationTemplateDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    NotificationTemplateDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import java.util.Set;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.notification.vo.NotificationTemplate;

import oracle.iam.notification.api.NotificationService;

import oracle.iam.notification.exception.MultipleTemplateException;
import oracle.iam.notification.exception.TemplateNotFoundException;
import oracle.iam.notification.exception.LastModifyDateNotSetException;
import oracle.iam.notification.exception.TemplateAlreadyExistsException;
import oracle.iam.notification.exception.NotificationManagementException;

import oracle.hst.foundation.naming.ServiceLocator;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class NotificationTemplateDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by NotificationTemplate customization.
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
public class NotificationTemplateDataProvider extends AbstractDataProvider<NotificationTemplateAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String TPL_ALIAS        = "tpl";
  private static final String TPL_KEY          = "id";
  private static final String TPL_NAME         = "templatename";
  private static final String TPL_EVENT        = "eventname";
  private static final String TPL_DESCRIPTION  = "description";
  private static final String TPL_STATUS       = "status";
  private static final String TPL_SNMP         = "snmpsupported";
  private static final String TPL_DATA_LEVEL   = "datalevel";

  private static final String SELECT           = "tpl.select";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3667524548802243480")
  private static final long   serialVersionUID = 6271822839255744956L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>NotificationTemplateDataProvider</code> data
   ** access object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public NotificationTemplateDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   create (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object creation.
   **
   ** @param  mab                the model adapter bean, with attributes set.
   */
  @Override
  public void create(final NotificationTemplateAdapter mab) {
    // create the instance to transfer the data to the remote server
    final NotificationTemplate template = new NotificationTemplate();
    template.setTemplatename(mab.getName());
    template.setEventname(mab.getEvent());
    template.setDescription(mab.getDescription());
    template.setStatus(String.valueOf(mab.getStatus()));
    template.setSnmpsupported(mab.getSnmp().booleanValue());

    final NotificationService facade = service(NotificationService.class);
    try {
      // ... add the Template ...
      facade.addTemplate(template);
    }
    catch (TemplateAlreadyExistsException e) {
      throw new OIMRuntimeException(e);
    }
    catch (NotificationManagementException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   update (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object update.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void update(final NotificationTemplateAdapter mab) {
    // create the instance to transfer the data to the remote server
    final NotificationTemplate template = new NotificationTemplate();
    template.setTemplatename(mab.getName());
    template.setEventname(mab.getEvent());
    template.setDescription(mab.getDescription());
    template.setStatus(String.valueOf(mab.getStatus()));
    template.setSnmpsupported(mab.getSnmp().booleanValue());

    final NotificationService facade = service(NotificationService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   delete (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object delete.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void delete(final NotificationTemplateAdapter mab) {
    final NotificationService facade = service(NotificationService.class);
    try {
      // ... delete the Notification Template ...
      facade.deleteTemplate(mab.getId().longValue(), new Date());
    }
    catch (TemplateNotFoundException e) {
      throw new OIMRuntimeException(e);
    }
    catch (MultipleTemplateException e) {
      throw new OIMRuntimeException(e);
    }
    catch (LastModifyDateNotSetException e) {
      throw new OIMRuntimeException(e);
    }
    catch (NotificationManagementException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   exists (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** to check if the B-Object exists
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public boolean exists(final NotificationTemplateAdapter mab) {
    // intentionally left blank
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   lookup (DataProvider)
  /**
   ** Return a specific backend objects identified by the key fields set in
   ** the specified ModelAdapterBean.
   ** <p>
   ** The ModelAdapterBean argument implementation class must correspond to the
   ** DataProvider implementation. In other words, this method implemented in
   ** AttributeDataProvider expects a AttributeAdapterBean as an argument.
   ** <br>
   ** If the DataProvider and ModelAdapterBean types do not match, and exception
   ** will be thrown.
   ** <p>
   ** The ModelAdapterBean must has its "key" fields set. All other fields are
   ** ignored. In most cases, the field field is simply the name.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   **
   ** @return                    object identified by the given name.
   */
  @Override
  public NotificationTemplateAdapter lookup(NotificationTemplateAdapter mab) {
    final Long           identifier = mab.getId();
    final DatabaseFilter criteria   = ((identifier == null) || identifier.equals(-1L))
      ? createFilter(SearchCriteria.Operator.EQUAL, TPL_NAME, mab.getName())
      : createFilter(SearchCriteria.Operator.EQUAL, TPL_KEY,  identifier);

    Connection        connection = null;
    PreparedStatement statement  = null;
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      statement  = QueryProviderFactory.instance.preparedStatement(SELECT, null, connection, criteria);
      // execute the sql statement and parse the result set
      final ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      mab = build(resultSet);
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
    return mab;
  }

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
   **                            {@link NotificationTemplateAdapter}.
   */
  @Override
  public List<NotificationTemplateAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    Integer rangeStart = 0;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)control.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    Integer rangeEnd = 1;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd= (Integer)control.get(ConstantsDefinition.SEARCH_ENDROW);
    }

    // default search is based on key
    final DatabaseFilter                    criteria = extractCriteria(searchCriteria == null ? new SearchCriteria(NotificationTemplateAdapter.PK, null, SearchCriteria.Operator.NOT_EQUAL) : searchCriteria);
    final List<NotificationTemplateAdapter> batch    = new ArrayList<NotificationTemplateAdapter>(rangeEnd - rangeStart);

    Connection        connection = null;
    PreparedStatement statement  = null;
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      // prepare the sql statement
      statement  = QueryProviderFactory.instance.preparedStatement(SELECT, null, connection, criteria, rangeStart, rangeEnd);
      // execute the sql statement and parse the result set
      final ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        batch.add(build(resultSet));
      }
      return new PagedArrayList<NotificationTemplateAdapter>(batch, rangeStart, rangeEnd);
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
   ** to the names declared in Identity Manager for entity Notification
   ** Template.
   **
   ** @param criteria            the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   */
  private DatabaseFilter extractCriteria(final SearchCriteria criteria) {
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
      final SearchCriteria.Operator op = criteria.getOperator();
      if (NotificationTemplateAdapter.PK.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, TPL_KEY, rhs);
      }
      else if (NotificationTemplateAdapter.NAME.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, TPL_NAME, rhs);
      }
      else if (NotificationTemplateAdapter.EVENT.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, TPL_EVENT, rhs);
      }
      else if (NotificationTemplateAdapter.STATUS.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, TPL_STATUS, rhs);
      }
      else if (NotificationTemplateAdapter.DATALEVEL.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, TPL_DATA_LEVEL, rhs);
      }
      else if (NotificationTemplateAdapter.SNMP.equalsIgnoreCase((String)lhs)) {
        partial = filterBoolean(op, TPL_SNMP, rhs);
      }
      filter = (filter == null) ? partial : DatabaseFilter.build(filter, partial, DatabaseFilter.Operator.AND);
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   filterString
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
  private static DatabaseFilter filterString(final SearchCriteria.Operator operator, final String column, final Object value) {
    return AbstractDataProvider.createFilter(operator, String.format("%s.%s", TPL_ALIAS, column), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   filterBoolean
  /**
   ** Factory method to create a {@link DatabaseFilter}.
   **
   ** @param  operator           the filter operator.
   ** @param  column             the name of the column the created filter will
   **                            take in account.
   ** @param  value              the value used to filter on.
   */
  private DatabaseFilter filterBoolean(final SearchCriteria.Operator operator, final String column, final Object value) {
    return AbstractDataProvider.booleanFilter(operator, String.format("%s.%s", TPL_ALIAS, column), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create a <code>NotificationTemplateAdapter</code> from a
   ** {@link ResultSet}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The {@link ResultSet} pointer needs to be positioned correctly before this
   ** method is called.
   **
   ** @param  resultSet          the {@link ResultSet} provider to obtain the
   **                            attribute values from.
   **                            Allowed object is {@link ResultSet}.
   **
   ** @return                    the {@link NotificationTemplateAdapter}
   **                            constructed from the specfied
   **                            <code>resultSet</code>.
   */
  private static NotificationTemplateAdapter build(final ResultSet resultSet) {
    final NotificationTemplateAdapter mab = new NotificationTemplateAdapter();
    try {
      mab.setId(resultSet.getLong(TPL_KEY));
      mab.setEvent(resultSet.getString(TPL_EVENT));
      mab.setName(resultSet.getString(TPL_NAME));
      mab.setDescription(resultSet.getString(TPL_DESCRIPTION));
      mab.setStatus(resultSet.getString(TPL_STATUS));
      mab.setSnmp(resultSet.getString(TPL_SNMP));
      mab.setDataLevel(resultSet.getString(TPL_DATA_LEVEL));
    }
    catch (SQLException e) {
      throw new OIMRuntimeException(e);
    }
    return mab;
  }
}