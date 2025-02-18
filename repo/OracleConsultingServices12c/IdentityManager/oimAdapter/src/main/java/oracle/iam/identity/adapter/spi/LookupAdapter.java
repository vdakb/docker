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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   LookupAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import java.util.Map;
import java.util.List;

import java.sql.Connection;

import Thor.API.Exceptions.tcAPIException;

import Thor.API.Operations.tcLookupOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.object.Pair;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.GenericAdapter;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.persistence.DatabaseException;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

////////////////////////////////////////////////////////////////////////////////
// class LookupAdapter
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Adpapter dedicated to operate on the Oracle Identity Manager Lookup
 ** Definition object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class LookupAdapter extends GenericAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final DatabaseEntity LOOKUP = DatabaseEntity.build(null, "lku",     "lku_key");
  private static final DatabaseEntity VALUE  = DatabaseEntity.build(null, "lku,lkv", "lkv_key");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupAdapter</code> task adpater that allows
   ** use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public LookupAdapter(tcDataProvider provider) {
    // ensure inheritance
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupAdapter</code> task adpater that allows
   ** use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public LookupAdapter(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodedValue
  /**
   ** Obtains the decoded value from the specifed <code>Lookup Definition</code>
   ** for the specified encoded value.
   **
   ** @param  lookupDefinition   the name of the <code>Lookup Definition</code>
   **                            providing the data.
   ** @param  encodedValue       the encoded value to lookup the decoded value
   **                            for from the <code>Lookup Definition</code>
   **                            specified by <code>lookupDefinition</code>.
   **
   ** @return                    the desired decoded value for the
   **                            specified encoded value of the
   **                            <code>Lookup Definition</code>.
   */
  public String decodedValue(final String lookupDefinition, final String encodedValue) {
    final tcLookupOperationsIntf facade = lookupFacade();
    String decodedValue = encodedValue;
    try {
      decodedValue = facade.getDecodedValueForEncodedValue(lookupDefinition, encodedValue);
      // to be failsafe return the encoded value if the the combination of
      // lookup definition and specified encoded value is not resolvable
      if (StringUtility.isEmpty(decodedValue))
        decodedValue = encodedValue;
    }
    catch (tcAPIException e) {
      // to be failsafe return the encoded value if the the combination of
      // lookup definition and specified encoded value is not resolvable
      return decodedValue;
    }
    return decodedValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodedValue
  /**
   ** Obtains the encoded value from the specifed <code>Lookup Definition</code>
   ** for the specified decoded value.
   **
   ** @param  lookupDefinition   the name of the <code>Lookup Definition</code>
   **                            providing the data.
   ** @param  decodedValue       the decoded value to lookup the encoded value
   **                            for from the <code>Lookup Definition</code>
   **                            specified by <code>lookupDefinition</code>.
   **
   ** @return                    the desired encoded value for the
   **                            specified decoded value of the
   **                            <code>Lookup Definition</code>.
   */
  public String encodedValue(final String lookupDefinition, final String decodedValue) {
    // initialize the database filter to lookup values from the Lookup
    // Definition this task is reconciling
    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build("lku.lku_type_string_key", lookupDefinition, DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.build(
        DatabaseFilter.build("lkv.lku_key",     LOOKUP,       DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.build("lkv.lkv_decoded", decodedValue, DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.Operator.AND
      )
    , DatabaseFilter.Operator.AND
    );
    final List<Pair<String, String>> returning = CollectionUtility.list(Pair.of("lkv_encoded", "lkv_encoded"));
    final DatabaseSelect statement = DatabaseSelect.build(this, VALUE, filter, returning);

    String found = null;
    Connection connection = null;
    try {
      connection = DatabaseConnection.aquire();
      final List<Map<String, Object>> dataSet = statement.execute(connection);
      // to be failsafe return the decoded value if the the combination of
      // lookup definition and specified decoded value is not resolvable
      if (dataSet.size() == 0 || dataSet.size() > 1)
        found = decodedValue;
      else 
        found = (String)dataSet.get(0).get("lkv_encoded");
    }
    catch (DatabaseException e) {
      // to be failsafe return the decoded value if the the combination of
      // lookup definition and specified decoded value is not resolvable
      return decodedValue;
    }
    finally {
      DatabaseConnection.release(connection);
    }
    return found;
  }
}