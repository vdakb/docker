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

    File        :   PropertyDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PropertyDataProvider.


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

import oracle.iam.conf.exception.StaleDataException;
import oracle.iam.conf.exception.InvalidDataLevelException;
import oracle.iam.conf.exception.MissingRequiredValueException;
import oracle.iam.conf.exception.NoSuchSystemPropertyExistException;
import oracle.iam.conf.exception.SystemConfigurationServiceException;
import oracle.iam.conf.exception.SystemPropertyAlreadyExistException;
import oracle.iam.conf.exception.InvalidSystemPropertyValueException;
import oracle.iam.conf.exception.SystemConfigurationManagementException;

import oracle.iam.conf.vo.SystemProperty;

import oracle.iam.conf.api.SystemConfigurationService;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.naming.ServiceLocator;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class PropertyDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by Property customization.
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
public class PropertyDataProvider extends AbstractDataProvider<PropertyAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PTY_ALIAS         = "pty";
  private static final String PTY_KEY           = "pty_key";
  private static final String LKU_KEY           = "lku_key";
  private static final String PTY_KEYWORD       = "pty_keyword";
  private static final String PTY_NAME          = "pty_name";
  private static final String PTY_VALUE         = "pty_value";
  private static final String PTY_SYSTEM        = "pty_system";
  private static final String PTY_LOGINREQUIRED = "pty_loginrequired";
  private static final String PTY_RUNON         = "pty_run_on";
  private static final String PTY_DATA_LEVEL    = "pty_data_level";

  private static final String SELECT            = "pty.select";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3009389975223942910")
  private static final long   serialVersionUID  = 2300326831517396125L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PropertyDataProvider</code> data access object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PropertyDataProvider() {
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
  public void create(final PropertyAdapter mab) {
    // create the instance to transfer the data to the remote server
    final SystemProperty property = new SystemProperty();
    property.setPtyKeyword(mab.getPropertyName());
    property.setPtyValue(mab.getValue());
    property.setPtyName(mab.getName());
    property.setPtyDataLevel(String.valueOf(mab.getDataLevel()));
    property.setPtyLoginrequired(mab.getLoginRequired());

    final SystemConfigurationService facade = service(SystemConfigurationService.class);
    try {
      // ... add the Property ...
      facade.addSystemProperty(property);
    }
    catch (SystemConfigurationManagementException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SystemPropertyAlreadyExistException e) {
      throw new OIMRuntimeException(e);
    }
    catch (MissingRequiredValueException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SystemConfigurationServiceException e) {
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
  public void update(final PropertyAdapter mab) {
    // create the instance to transfer the data to the remote server
    final SystemProperty property = new SystemProperty();
    property.setId(mab.getPropertyKey().toString());
    property.setPtyKeyword(mab.getPropertyName());
    property.setPtyName(mab.getName());
    property.setPtyDataLevel(mab.getDataLevel().toString());
    property.setPtyValue(mab.getValue());
    property.setPtyLoginrequired(mab.getLoginRequired());
    property.setPtyUpdate(new Date());

    final SystemConfigurationService facade = service(SystemConfigurationService.class);
    try {
      facade.updateSystemProperty(property, property.getPtyUpdate());
    }
    catch (SystemConfigurationManagementException e) {
      throw new OIMRuntimeException(e);
    }
    catch (NoSuchSystemPropertyExistException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SystemPropertyAlreadyExistException e) {
      throw new OIMRuntimeException(e);
    }
    catch (StaleDataException e) {
      throw new OIMRuntimeException(e);
    }
    catch (InvalidDataLevelException e) {
      throw new OIMRuntimeException(e);
    }
    catch (InvalidSystemPropertyValueException e) {
      throw new OIMRuntimeException(e);
    }
    catch (MissingRequiredValueException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SystemConfigurationServiceException e) {
      throw new OIMRuntimeException(e);
    }
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
  public void delete(final PropertyAdapter mab) {
    final SystemConfigurationService facade = service(SystemConfigurationService.class);
    try {
      // ... delete the Property ...
      facade.deleteSystemProperty(mab.getPropertyName(), new Date());
    }
    catch (SystemConfigurationManagementException e) {
      throw new OIMRuntimeException(e);
    }
    catch (NoSuchSystemPropertyExistException e) {
      throw new OIMRuntimeException(e);
    }
    catch (StaleDataException e) {
      throw new OIMRuntimeException(e);
    }
    catch (InvalidDataLevelException e) {
      throw new OIMRuntimeException(e);
    }
    catch (InvalidSystemPropertyValueException e) {
      throw new OIMRuntimeException(e);
    }
    catch (SystemConfigurationServiceException e) {
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
  public boolean exists(final PropertyAdapter mab) {
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
  public PropertyAdapter lookup(final PropertyAdapter mab) {
    final Long           identifier = mab.getPropertyKey();
    final DatabaseFilter criteria   = ((identifier == null) || identifier.equals(-1L))
      ? createFilter(SearchCriteria.Operator.EQUAL, PTY_KEYWORD, mab.getPropertyName())
      : createFilter(SearchCriteria.Operator.EQUAL, PTY_KEY,     identifier);

    Connection        connection = null;
    PreparedStatement statement  = null;
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      statement  = QueryProviderFactory.instance.preparedStatement(SELECT, null, connection, criteria);
      // execute the sql statement and parse the result set
      final ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      mab.setPropertyKey(resultSet.getLong(PTY_KEY));
      mab.setPropertyName(resultSet.getString(PTY_KEYWORD));
      mab.setName(resultSet.getString(PTY_NAME));
      mab.setValue(resultSet.getString(PTY_VALUE));
      mab.setSystem(resultSet.getString(PTY_SYSTEM));
      mab.setLoginRequired(resultSet.getString(PTY_LOGINREQUIRED));
      mab.setRunOn(resultSet.getString(PTY_RUNON));
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
   ** Return a specific backend objects based on the given filter criteria.
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
   **                            element is of type {@link PropertyAdapter}.
   */
  @Override
  public List<PropertyAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    Integer rangeStart = 0;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)control.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    Integer rangeEnd = 1;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd= (Integer)control.get(ConstantsDefinition.SEARCH_ENDROW);
    }

    // default search is based on key
    final DatabaseFilter         criteria = extractCriteria(searchCriteria == null ? new SearchCriteria(PropertyAdapter.PK, null, SearchCriteria.Operator.NOT_EQUAL) : searchCriteria);
    final List<PropertyAdapter> batch     = new ArrayList<PropertyAdapter>(rangeEnd - rangeStart);

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
        final PropertyAdapter mab = new PropertyAdapter();
        mab.setPropertyKey(resultSet.getLong(PTY_KEY));
        mab.setPropertyName(resultSet.getString(PTY_KEYWORD));
        mab.setValue(resultSet.getString(PTY_VALUE));
        mab.setName(resultSet.getString(PTY_NAME));
        mab.setDataLevel(resultSet.getString(PTY_DATA_LEVEL));
        mab.setSystem(resultSet.getString(PTY_SYSTEM));
        mab.setLoginRequired(resultSet.getString(PTY_LOGINREQUIRED));
        mab.setRunOn(resultSet.getString(PTY_RUNON));
        batch.add(mab);
      }
      return new PagedArrayList<PropertyAdapter>(batch, rangeStart, rangeEnd);
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
   ** to the names declared in Identity Manager for entity Property.
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
      if (PropertyAdapter.PK.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, PTY_KEY, rhs);
      }
      else if (PropertyAdapter.PROPERTYNAME.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, PTY_KEYWORD, rhs);
      }
      else if (PropertyAdapter.VALUE.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, PTY_VALUE, rhs);
      }
      else if (PropertyAdapter.NAME.equalsIgnoreCase((String)lhs)) {
        partial = createFilter(op, PTY_NAME, rhs);
      }
      else if (PropertyAdapter.DATALEVEL.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, PTY_DATA_LEVEL, rhs);
      }
      else if (PropertyAdapter.SYSTEM.equalsIgnoreCase((String)lhs)) {
        partial = filterBoolean(op, PTY_SYSTEM, rhs);
      }
      else if (PropertyAdapter.LOGINREQUIRED.equalsIgnoreCase((String)lhs)) {
        partial = filterBoolean(op, PTY_LOGINREQUIRED, rhs);
      }
      else if (PropertyAdapter.RUNON.equalsIgnoreCase((String)lhs)) {
        partial = filterString(op, PTY_RUNON, rhs);
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
   ** @param  column             the name of the column the created filter will
   **                            take in account.
   ** @param  value              the value used to filter on.
   */
  private DatabaseFilter filterString(final SearchCriteria.Operator operator, final String column, final Object value) {
    return AbstractDataProvider.createFilter(operator, String.format("%s.%s", PTY_ALIAS, column), value);
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
    return AbstractDataProvider.booleanFilter(operator, String.format("%s.%s", PTY_ALIAS, column), value);
  }
}