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

    File        :   LookupValueDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LookupValueDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.utils.SerializationUtils;

import oracle.iam.ui.platform.model.common.PagedArrayList;
import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.naming.ServiceLocator;

import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class LookupValueDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
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
public class LookupValueDataProvider extends AbstractLookupDataProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String       SELECT           = "lkv.select";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1192013784562938897")
  private static final long serialVersionUID = -2004167398955389466L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupValueDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupValueDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupValueDataProvider</code> instance which belongs
   ** to specified name of a <code>Lookup Definition</code>.
   **
   ** @param  lookup             the name of a <code>Lookup Definition</code>.
   */
  public LookupValueDataProvider(final String lookup) {
    // ensure inheritance
    super(lookup);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

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
   **                            element is of type {@link LookupValueAdapter}.
   */
  @Override
  public List<LookupValueAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    final SearchCriteria lookupKey  = getAttrCriteria(searchCriteria, LookupAdapter.FK);
    final SearchCriteria lookupName = getAttrCriteria(searchCriteria, LookupValueAdapter.NAME);
    // prevent bogus instance state
    // normally it cannot happens but to be safe we need to verify if our own
    // code is correct
    if (lookupKey == null && lookupName == null)
      throw new OIMRuntimeException(resourceBackendValue("lku.missing"));

    Map<String, LookupValueAdapter> add = SerializationUtils.deserializeFromString((String)getCriteriaAttrValue(searchCriteria, LookupValueAdapter.ADD), LinkedHashMap.class);
    Map<String, LookupValueAdapter> del = SerializationUtils.deserializeFromString((String)getCriteriaAttrValue(searchCriteria, LookupValueAdapter.DEL), LinkedHashMap.class);
    Map<String, LookupValueAdapter> mod = SerializationUtils.deserializeFromString((String)getCriteriaAttrValue(searchCriteria, LookupValueAdapter.MOD), LinkedHashMap.class);

    // ensure the NPE can never happens if the result set is initially received
    add = add == null ? new LinkedHashMap<String, LookupValueAdapter>() : add;
    del = del == null ? new LinkedHashMap<String, LookupValueAdapter>() : del;
    mod = mod == null ? new LinkedHashMap<String, LookupValueAdapter>() : mod;

    // if the lookup definition under control has to be created we don't have
    // to populate existing relations for the lookup value hence only added
    // values are in the scope
    if (lookupKey != null && "-1".equals(lookupKey.getSecondArgument())) {
      final List<LookupValueAdapter> result = new ArrayList<LookupValueAdapter>(add.values());
      return new PagedArrayList<LookupValueAdapter>(result, 1, result.size());
    }
    else {
      Integer rangeStart = 0;
      if (control != null && control.containsKey(ConstantsDefinition.SEARCH_STARTROW)) {
        rangeStart = (Integer)control.get(ConstantsDefinition.SEARCH_STARTROW);
      }
      Integer rangeEnd = 1;
      if (control != null && control.containsKey(ConstantsDefinition.SEARCH_ENDROW)) {
        rangeEnd= (Integer)control.get(ConstantsDefinition.SEARCH_ENDROW);
      }
      final DatabaseFilter           criteria = extractCriteria(searchCriteria);
      final List<LookupValueAdapter> batch    = new ArrayList<LookupValueAdapter>(rangeEnd - rangeStart);

      Connection        connection = null;
      PreparedStatement statement  = null;
      try {
        // aquire a connection to the database from pool
        connection = ServiceLocator.getDatasource(QueryProviderFactory.DATASOURCE).getConnection();
        // prepare the sql statement
        statement  = QueryProviderFactory.instance.preparedStatement(SELECT, DatabaseFilter.Operator.AND, connection, criteria);
        // execute the sql statement and parse the result set
        final ResultSet resultSet = statement.executeQuery();
        if (resultSet == null && add.size() == 0)
          return batch;
        else {
          // add all non-persisted entries to the collection of populated entries
          batch.addAll(add.values());
          while (resultSet.next()) {
            LookupValueAdapter mab = new LookupValueAdapter();
            mab.setLookupKey(resultSet.getString(LKU_KEY));
            mab.setValueKey(resultSet.getString(LKV_KEY));
            mab.setEncoded(resultSet.getString(LKV_ENCODED));
            mab.setDecoded(resultSet.getString(LKV_DECODED));
            mab.setDisabled(resultSet.getString(LKV_DISABLED));
            if (del.containsKey(mab.getValueKey())) {
              mab = del.get(mab.getValueKey());
              mab.setPendingAction(LookupValueAdapter.DEL);
            }
            else if (mod.containsKey(mab.getValueKey())) {
              mab = mod.get(mab.getValueKey());
              mab.setPendingAction(LookupValueAdapter.MOD);
            }
            batch.add(mab);
          }
          return new PagedArrayList<LookupValueAdapter>(batch, rangeStart, rangeEnd);
        }
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
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
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   toMap
  /**
   ** Factory method to transform the model adapter bean to a {@link Map}.
   **
   ** @param  mab                the {@link LookupValueAdapter} model adapter
   **                            bean providing the data.
   **
   ** @return                    the {@link Map} with encoded, decoded and
   **                            status attribute set.
   */
  public static Map<String, String> toMap(final LookupValueAdapter mab) {
    final Map<String, String> value = new HashMap<String, String>();
    value.put(LookupValue.ENCODED,  mab.getEncoded());
    value.put(LookupValue.DECODED,  mab.getDecoded());
    value.put(LookupValue.DISABLED, mab.getDisabled());
    value.put(LookupValue.LANGUAGE, "en");
    value.put(LookupValue.COUNTRY,  "US");
    return value;
  }
}