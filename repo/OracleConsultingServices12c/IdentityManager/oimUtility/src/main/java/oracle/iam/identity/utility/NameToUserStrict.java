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

    Copyright © 2006. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Utility Facilities

    File        :   NameToUserStrict.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NameToUserStrict.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

////////////////////////////////////////////////////////////////////////////////
// class NameToUserStrict
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>NameToUserStrict</code> transforms a given name to the system
 ** identifier of user by looking up the user with the given login name.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public class NameToUserStrict extends AbstractStatusTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The SQL statement string to lookup a <code>User</code> key using the
   ** login name of a <code>User</code>.
   */
  private static final String LOOKUP_USER = "select usr_key from usr where usr_login = ?";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>NameToUserStrict</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose.
   */
  public NameToUserStrict(final Logger logger) {
    // ensure inheritance
    super(logger);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (AttributeTransformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation in <code>subject</code>.
   ** <p>
   ** The {@link Map} <code>origin</code> contains all untouched values and has
   ** it to be aftre this method completes. The {@link Map} <code>subject</code>
   ** contains all transformed values hence tha transformation done here too.
   **
   ** @param  attributeName      the specific attribute in the {@link Map}
   **                            <code>origin</code> that has to be transformed.
   ** @param  origin             the {@link Map} to transform.
   ** @param  subject            the transformation of the specified
   **                            {@link Map} <code>origin</code>.
   */
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
    final Object value = origin.get(attributeName);
    // if we not got a null value put it without transformation in the returning
    // container
    if (value == null)
      // return an empty string instead to lookup what not exists
      subject.put(attributeName, value);
    else
      // return the converted value as is
      subject.put(attributeName, lookupIdentity(String.valueOf(value)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupIdentity
  /**
   ** Lookups the internal system identifier of a <code>User</code> from Oracle
   ** Identity Manager.
   **
   ** @param  identifier         the login name of an identity to lookup.
   **
   ** @return                    the internal identifier of the
   **                            <code>User</code> that login name match the
   **                            specified parameter <code>identifier</code> or
   **                            <code>null</code> if no <code>User</code>s
   **                            login name match the specified parameter
   **                            <code>identifier</code>.
   */
  private String lookupIdentity(final String identifier) {
    // prevent bogus input
    if (StringUtility.isEmpty(identifier))
      return null;

    final String method = "lookupIdentity";
    trace(method, SystemMessage.METHOD_ENTRY);

    BigDecimal        key        = null;
    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    try {
      connection = DatabaseConnection.aquire();
      statement  = DatabaseStatement.createPreparedStatement(connection, LOOKUP_USER);
      statement.setString(1, identifier);
      resultSet  = statement.executeQuery();
      if (resultSet.next()) {
        key = resultSet.getBigDecimal(1);
        if (resultSet.wasNull())
          key = null;
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return (key == null) ? null : key.toPlainString();
  }
}