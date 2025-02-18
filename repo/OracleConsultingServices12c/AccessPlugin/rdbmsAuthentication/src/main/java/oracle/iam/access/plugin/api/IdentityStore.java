/*
    ORACLE Deutschland B.V. & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Plugin
    Subsystem   :   OpenIdConnect Discovery

    File        :   DatabaseAuthentication.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseAuthentication.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-11  npopli      First release version
*/

package oracle.iam.access.plugin.api;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

////////////////////////////////////////////////////////////////////////////////
// abstract class IdentityStore
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
public abstract class IdentityStore {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>IdentityStore</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new IdentityStore()" and enforces use of the public method below.
   */
  private IdentityStore() {
   // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticate
  public static String authenticate(final InitialContext context, final String dataSource, final String username, final String password) {
    final String      sql        = " select name from users where name = ? and password = ?";
    final Connection  connection = aquire(lookup(context, dataSource));
    PreparedStatement statement  = null;
    try {
      statement = connection.prepareStatement(sql);
      statement.setString(1, username);
      statement.setString(2, password);
      final ResultSet rs = statement.executeQuery();
      if (rs != null && rs.next()) {
        return rs.getString("name");
      }
    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }
    finally {
      release(connection);
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  public static DataSource lookup(final InitialContext context, final String name) {
    try {
      InitialContext ctx = context;
      // if no context is provided use the default one the container supplies
      if( ctx == null) {
        ctx = new InitialContext();
      }
      return (DataSource)ctx.lookup(name);
    }
    catch (NamingException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Factory method to create a {@link java.sql.Connection} using the specified
   ** {@link DataSource}.
   **
   ** @param  dataSource         the JDBC {@link DataSource} used to obtain a
   **                            {@link Connection} from the pool.
   **                            <br>
   **                            Possible object {@link DataSource}.
   **
   ** @return                    a valid connection
   **                            <br>
   **                            Possible object {@link Connection}.
   */
  public static final Connection aquire(final DataSource dataSource) {
    try {
      return dataSource.getConnection();
    }
    catch (SQLException e) {
      // handle silenlty
      e.printStackTrace(System.err);
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release the given JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @param  connection         the JDBC connection to release.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   */
  public static final void release(final Connection connection) {
    if (connection != null) {
      try {
        // make sure that we will commit the unit of work if neccessary
        connection.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close a previous prepared statement.
   ** <br>
   ** If the statement has a dependence result set, the result set will also be
   ** closed.
   **
   ** @param  statement          the {@link PreparedStatement} to close.
   **                            <br>
   **                            Allowed object is {@link PreparedStatement}.
   */
  public static final void close(final PreparedStatement statement) {
    if (statement == null)
      return;

    try {
      // close the attached result set if any
      close(statement.getResultSet());
    }
    catch (SQLException e) {
      // handle silenlty
      e.printStackTrace(System.err);
    }
    finally {
      try {
        // close the statement itself
        statement.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close a JDBC resultset.
   **
   ** @param  resultSet          the {@link ResultSet} to close.
   **                            <br>
   **                            Allowed object is {@link ResultSet}.
   */
  public static final void close(final ResultSet resultSet) {
    if (resultSet != null)
      try {
        resultSet.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
  }
}
