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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Service Library
    Subsystem   :   ZeRo Backend

    File        :   SqlHelper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file contains various SQL helper methods.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-05-25  AFarkas     First release version
*/

package bka.iam.identity.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Date;
import java.util.List;

import oracle.hst.platform.core.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// final class SqlHelper
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** Contains various SQL helper methods.
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class SqlHelper {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String  NAME   = "SqlHelper";

  private static final String CLASS  = SqlHelper.class.getName();
  private static final Logger LOGGER = Logger.create(CLASS);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SqlHelper() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPreparedStatement
  /**
   ** Helper method to allow using try-with-resource.
   **
   ** @param  connection         the SQL {@link Connection} to operate upon.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   **
   ** @param  sqlQuery           the {@link String} containing the SQL query.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   **
   ** @param  arguments          the {@link List} of {@link Object}s for argument values.
   **                            The arguments are evaluated and bound to the appropriate
   **                            position with methods corresponding to argument type.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **
   ** @return                    the {@link PreparedStatement} with bind variables filled.
   **
   ** @throws SQLException       if an SQL error occurs.
   */
  public static PreparedStatement createPreparedStatement(Connection connection, String sqlQuery, List<Object> arguments) throws SQLException {
    final String method = "createPreparedStatement";
    LOGGER.entering(CLASS, method, "sqlQuery=", sqlQuery, "arguments=", arguments);
    PreparedStatement statement = connection.prepareStatement(sqlQuery);
    // We consider very few argument types, this method is far from being generic
    if (arguments != null) {
      for (int i = 0; i < arguments.size(); i++) {
        Object arg = arguments.get(i);
        if (arg instanceof String) {
          statement.setString(i + 1, (String) arg);
        } else if (arg instanceof Integer) {
          statement.setInt(i + 1, (Integer) arg);
        } else if (arg instanceof Long) {
          statement.setLong(i + 1, (Long) arg);
        } else if (arg instanceof Date) {
          statement.setDate(i + 1, new java.sql.Date(((Date) arg).getTime()));
        } else if (arg instanceof Boolean) {
          statement.setBoolean(i + 1, (Boolean) arg);
        } else {
          SQLException e = new SQLException("Unknown argument type, Arg class " + arg.getClass().getName());
          LOGGER.throwing(CLASS, method, e);
          throw e; 
        }
      }
    } else {
      LOGGER.trace(CLASS, method, "No arguments are supplied, return the statement right away");
    }
    LOGGER.exiting(CLASS, method);
    return statement;
  }
}
