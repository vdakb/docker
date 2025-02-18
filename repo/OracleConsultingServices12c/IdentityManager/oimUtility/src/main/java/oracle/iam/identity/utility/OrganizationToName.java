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

    File        :   OrganizationToName.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OrganizationToName.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationToName
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrganizationToName</code> transforms a given the system identifier
 ** of an organization to its name by looking up the organization with the given
 ** system identifier.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public class OrganizationToName extends AbstractStatusTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The SQL statement string to lookup an <code>Organization</code> name using
   ** the internal identifier of an <code>Organization</code>.
   */
  private static final String LOOKUP_ORGANIZATION = "select act_name from act where act_key = ?";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>OrganizationToName</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   */
  public OrganizationToName(final Logger logger) {
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
   ** it to be after this method completes. The {@link Map} <code>subject</code>
   ** contains all transformed values hence the transformation done here too.
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
      subject.put(attributeName, lookupOrganization(String.valueOf(value)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrganization
  /**
   ** Lookups the name of an <code>Organization</code> from Oracle Identity
   ** Manager.
   **
   ** @param  identifier         the unique name of an <code>Organization</code>
   **                            to lookup.
   **
   ** @return                    the name  of the <code>Organization</code>
   **                            that  match the specified parameter
   **                            <code>identifier</code> or <code>null</code> if
   **                            no <code>Organization</code>s name match the
   **                            specified parameter <code>identifier</code>.
   */
  private String lookupOrganization(final String identifier) {
    // prevent bogus input
    if (StringUtility.isEmpty(identifier))
      return null;

    final String method = "lookupOrganization";
    trace(method, SystemMessage.METHOD_ENTRY);

    String            key        = null;
    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    try {
      connection = DatabaseConnection.aquire();
      statement  = DatabaseStatement.createPreparedStatement(connection, LOOKUP_ORGANIZATION);
      statement.setString(1, identifier);
      resultSet  = statement.executeQuery();
      if (resultSet.next()) {
        key = resultSet.getString(1);
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
    return key;
  }
}