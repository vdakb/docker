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

    File        :   LookupDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LookupDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import Thor.API.Operations.tcLookupOperationsIntf;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidAttributeException;
import Thor.API.Exceptions.tcDuplicateLookupCodeException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.PagedArrayList;
import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.naming.ServiceLocator;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.naming.Lookup;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class LookupDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by Lookup customization.
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

public class LookupDataProvider extends AbstractDataProvider<LookupAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String LKU_ALIAS        = "lku";
  private static final String LKU_KEY          = "lku_key";
  private static final String LKU_NAME         = "lku_type_string_key";
  private static final String LKU_TYPE         = "lku_type";
  private static final String LKU_GROUP        = "lku_group";
  private static final String LKU_FIELD        = "lku_field";
  private static final String LKU_REQUIRED     = "lku_required";
  private static final String LKU_MEANING      = "lku_meaning";

  private static final String SELECT           = "lku.select";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8195517920704563443")
  private static final long   serialVersionUID = -1060026844467254784L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupDataProvider</code> data access object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupDataProvider() {
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
  public void create(final LookupAdapter mab) {
    // set the well know attributes of the Lookup Definition
    final Map<String, String> attribute = new HashMap<String, String>();
    attribute.put(Lookup.TYPE,     mab.getType());
    attribute.put(Lookup.FIELD,    mab.getField());
    attribute.put(Lookup.REQUIRED, mab.getRequired());
    // .. and update the group field
    // unfortunately we facing a bug if we try in the standard way to map the
    // pseudo column code specified Lookup.GROUP
    // A wrong value is mapped in the lookup definition Lookup Definition.Group
    // for the field information. Instead of LKU_GROUP the mispelled value
    // LKU_TYPE_GROUP is specified.
    // Therefore we are mapping the native column name
    attribute.put("LKU_GROUP",                 mab.getGroup());

    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      // ... add the Lookup Definition ...
      facade.addLookupCode(mab.getName());

      // .. and update the group field
      facade.updateLookupCode(mab.getName(), attribute);
    }
    catch (tcDuplicateLookupCodeException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcInvalidAttributeException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
    finally {
      facade.close();
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
  public void update(final LookupAdapter mab) {
    // set the well know attributes of the Lookup Definition
    final Map<String, String> attribute = new HashMap<String, String>();
    attribute.put(Lookup.TYPE,     mab.getType());
    attribute.put(Lookup.FIELD,    mab.getField());
    attribute.put(Lookup.REQUIRED, mab.getRequired());
    // .. and update the group field
    // unfortunately we facing a bug if we try in the standard way to map the
    // pseudo column code specified Lookup.GROUP
    // A wrong value is mapped in the lookup definition Lookup Definition.Group
    // for the field information. Instead of LKU_GROUP the mispelled value
    // LKU_TYPE_GROUP is specified.
    // Therefore we are mapping the native column name
    attribute.put("LKU_GROUP",                 mab.getGroup());

    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      facade.updateLookupCode(mab.getName(), attribute);
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcInvalidAttributeException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcAPIException e) {
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
  public void delete(final LookupAdapter mab) {
    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      // ... delete the Lookup Definition ...
      facade.removeLookupCode(mab.getName());
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
    finally {
      facade.close();
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
  public boolean exists(final LookupAdapter mab) {
    // intentionally left blank
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   lookup (DataProvider)
  /**
   ** Return a specific sysconfig object identified by the name.
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
  public LookupAdapter lookup(final LookupAdapter mab) {
    final String        identifier = mab.getLookupKey();
    final DatabaseFilter criteria  = (StringUtility.isEmpty(identifier) || identifier.equals("-1"))
      ? createFilter(SearchCriteria.Operator.EQUAL, LKU_NAME, mab.getName())
      : createFilter(SearchCriteria.Operator.EQUAL, LKU_KEY, identifier);

    Connection        connection = null;
    PreparedStatement statement  = null;
    try {
      // aquire a connection to the database from pool
      connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
      statement  = QueryProviderFactory.instance.preparedStatement(SELECT, null, connection, criteria);
      // execute the sql statement and parse the result set
      final ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      mab.setLookupKey(resultSet.getString(LKU_KEY));
      mab.setName(resultSet.getString(LKU_NAME));
      mab.setType(resultSet.getString(LKU_TYPE));
      mab.setGroup(resultSet.getString(LKU_GROUP));
      mab.setField(resultSet.getString(LKU_FIELD));
      mab.setMeaning(resultSet.getString(LKU_MEANING));
      mab.setRequired(resultSet.getString(LKU_REQUIRED));
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
   ** Return a list of sysconfig objects based on the given filter criteria.
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
   **                            element is of type {@link LookupAdapter}.
   */
  @Override
  public List<LookupAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    Integer rangeStart = 0;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
      rangeStart = (Integer)control.get(ConstantsDefinition.SEARCH_STARTROW);
    }
    Integer rangeEnd = 1;
    if (control != null && control.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
      rangeEnd= (Integer)control.get(ConstantsDefinition.SEARCH_ENDROW);
    }

    // default search is based on key
    final DatabaseFilter      criteria = extractCriteria(searchCriteria == null ? new SearchCriteria(LookupAdapter.PK, null, SearchCriteria.Operator.NOT_EQUAL) : searchCriteria);
    final List<LookupAdapter> batch    = new ArrayList<LookupAdapter>(rangeEnd - rangeStart);

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
        final LookupAdapter mab = new LookupAdapter();
        mab.setLookupKey(resultSet.getString(LKU_KEY));
        mab.setName(resultSet.getString(LKU_NAME));
        mab.setType(resultSet.getString(LKU_TYPE));
        mab.setGroup(resultSet.getString(LKU_GROUP));
        mab.setField(resultSet.getString(LKU_FIELD));
        mab.setMeaning(resultSet.getString(LKU_MEANING));
        mab.setRequired(resultSet.getString(LKU_REQUIRED));
        batch.add(mab);
      }
      return new PagedArrayList<LookupAdapter>(batch, rangeStart, rangeEnd);
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
  // Methode:   addValue
  /**
   ** Adds a value entry to the <code>Lookup Definition</code> specified by
   ** <code>name</code>.
   **
   ** @param  name               the name of the <code>Lookup Definition</code>
   **                            to take in account.
   ** @param  value              the {@link List} of {@link LookupValueAdapter}
   **                            model adapter beans, with fields set to be
   **                            added to the <code>Lookup Definition</code>.
   */
  public void addValue(final String name, final LookupValueAdapter value) {
    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      facade.addLookupValue(name, value.getEncoded(), value.getDecoded(), "en", "US");
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcInvalidValueException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   addValue
  /**
   ** Adds new value entries to the <code>Lookup Definition</code> specified by
   ** <code>name</code>.
   **
   ** @param  name               the name of the <code>Lookup Definition</code>
   **                            to take in account.
   ** @param  value              the {@link List} of {@link LookupValueAdapter}
   **                            model adapter beans, with fields set to be
   **                            added to the <code>Lookup Definition</code>.
   */
  public void addValue(final String name, final List<LookupValueAdapter> value) {
    final Map<String, String> encoded = new HashMap<String, String>(value.size());
    for (LookupValueAdapter cursor : value) {
      encoded.put(cursor.getEncoded(), cursor.getDecoded());
    }
    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      facade.addBulkLookupValues(name, encoded, "en", "Us");
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   modifyValue
  public void modifyValue(final String lookupName, final List<LookupValueAdapter> value) {
    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      for (LookupValueAdapter cursor : value) {
        facade.updateLookupValue(lookupName, cursor.getEncoded(), LookupValueDataProvider.toMap(cursor));
      }
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcInvalidValueException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcInvalidAttributeException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   removeValue
  /**
   ** Removes existing value entries for the <code>Lookup Definition</code>
   ** specified by <code>name</code>.
   **
   ** @param  name               the name of the <code>Lookup Definition</code>
   **                            to take in account.
   ** @param  value              the {@link List} of {@link LookupValueAdapter}
   **                            model adapter beans, with fields set to be
   **                            removed from the
   **                            <code>Lookup Definition</code>.
   */
  public void removeValue(final String name, final List<LookupValueAdapter> value) {
    final Set<String> encoded = new HashSet<String>(value.size());
    for (LookupValueAdapter cursor : value) {
      encoded.add(cursor.getEncoded());
    }
    final tcLookupOperationsIntf facade = service(tcLookupOperationsIntf.class);
    try {
      facade.removeBulkLookupValues(name, encoded);
    }
    catch (tcInvalidLookupException e) {
      throw new OIMRuntimeException(e);
    }
    catch (tcAPIException e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   extractCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Lookup.
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
      if (LookupAdapter.PK.equalsIgnoreCase((String)lh)) {
        partial = filterString(op, LKU_KEY, rh);
      }
      else if (LookupAdapter.NAME.equalsIgnoreCase((String)lh)) {
        partial = filterString(op, LKU_NAME, rh);
      }
      else if (LookupAdapter.TYPE.equalsIgnoreCase((String)lh)) {
        partial = filterString(op, LKU_TYPE, rh);
      }
      else if (LookupAdapter.GROUP.equalsIgnoreCase((String)lh)) {
        partial = filterString(op, LKU_GROUP, rh);
      }
      else if (LookupAdapter.FIELD.equalsIgnoreCase((String)lh)) {
        partial = createFilter(op, LKU_FIELD, rh);
      }
      else if (LookupAdapter.MEANING.equalsIgnoreCase((String)lh)) {
        partial = filterString(op, LKU_MEANING, rh);
      }
      else if (LookupAdapter.REQUIRED.equalsIgnoreCase((String)lh)) {
        partial = filterBoolean(op, LKU_REQUIRED, rh);
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
    return AbstractDataProvider.createFilter(operator, String.format("%s.%s", LKU_ALIAS, column), value);
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
    return AbstractDataProvider.booleanFilter(operator, String.format("%s.%s", LKU_ALIAS, column), value);
  }
}