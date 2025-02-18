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

    File        :   LoginName.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LoginName.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import com.thortech.xl.dataaccess.tcDataProvider;

import java.util.HashMap;

import java.io.Serializable;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import oracle.iam.platform.Platform;

import oracle.iam.platform.kernel.ValidationException;
import oracle.iam.platform.kernel.ValidationFailedException;

import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.platform.kernel.spi.ValidationHandler;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

////////////////////////////////////////////////////////////////////////////////
// class LoginName
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>LoginName</code> act as the service end point for the Oracle
 ** Identity Manager to generate login names.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class LoginName extends    AbstractServiceProvider
                       implements ValidationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String         LOGGER_CATEGORY = "OCS.USR.PROVISIONING";

  private static final char   DELIMITER       = '.';
  private static final String QUERY           = "select usr_key from usr where UPPER(usr_login) = UPPER(?)";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private DataSource          dataSource = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LoginName</code> task adpater.
   **
   ** @param  provider           the session provider connection
   */
  public LoginName(final tcDataProvider provider) {
    // ensure inheritance
    super(provider, LOGGER_CATEGORY);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ValidationHandler)
  /**
   ** Generate a unique login name based on <code>First Name</code> and
   ** <code>Last Name</code> of a Oracle Identity Manager User Profile.
   */
  public void validate(final long processId, final long eventId, final Orchestration orchestration)
    throws ValidationException
    ,      ValidationFailedException {

    HashMap<String, Serializable> parameters = orchestration.getParameters();
    final String userid    = (String)parameters.get(UserManagerConstants.AttributeName.USER_LOGIN.getId());
    if (StringUtility.isEmpty(userid)) {
      String firstname = (String)parameters.get(UserManagerConstants.AttributeName.FIRSTNAME.getId());
      String lastname  = (String)parameters.get(UserManagerConstants.AttributeName.LASTNAME.getId());
      parameters.put(UserManagerConstants.AttributeName.USER_LOGIN.getId(), generate(firstname, lastname));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ValidationHandler)
  /**
   ** Generate a unique login name based on <code>First Name</code> and
   ** <code>Last Name</code> of a Oracle Identity Manager User Profile.
   */
  public void validate(final long processId, final long eventId, final BulkOrchestration orchestration)
    throws ValidationException
    ,      ValidationFailedException {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** Called during creation of the orchestration engine at server startup.
   **
   ** @param  parameter          the parameter mapping passed to the
   **                            <code>EventHandler</code> obtained from the
   **                            descriptor and send by the Orchestration.
   */
  public void initialize(final HashMap<String, String> parameter) {
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);

    this.dataSource = Platform.getOperationalDS();
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generate a unique login name based on <code>First Name</code> and
   ** <code>Last Name</code> of a Oracle Identity Manager User Profile.
   **
   ** @param  namePrefix         the first part of the login name normaly the
   **                            first name of a user profile.
   ** @param  nameSuffix         the last part of the login name normaly the
   **                            last name of a user profile.
   **
   ** @return                    the generated login name.
   */
  public String generate(final String namePrefix, final String nameSuffix) {
    return generate(namePrefix, nameSuffix, DELIMITER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generate a unique login name based on <code>First Name</code> and
   ** <code>Last Name</code> of a Oracle Identity Manager User Profile.
   **
   ** @param  namePrefix         the first part of the login name normaly the
   **                            first name of a user profile.
   ** @param  nameSuffix         the last part of the login name normaly the
   **                            last name of a user profile.
   ** @param  delimiter          the character that separetes the name parts.
   **
   ** @return                    the generated login name.
   */
  public String generate(final String namePrefix, final String nameSuffix, final char delimiter) {
    final String method = "generate";
    trace(method, SystemMessage.METHOD_ENTRY);

    String capsFirstName = StringUtility.uncapitalize(StringUtility.replaceAccents(namePrefix));
    String capsLastName  = StringUtility.uncapitalize(StringUtility.replaceAccents(nameSuffix));
    String composedName  = capsFirstName + delimiter + capsLastName;

    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    try {
      int x = 0;
      connection = DatabaseConnection.aquire(this.dataSource);
      statement  = DatabaseStatement.createPreparedStatement(connection, QUERY);
      while (true) {
        statement.setString(1, composedName);
        statement.execute();
        resultSet = statement.getResultSet();
        if (!resultSet.next())
          break;

        composedName = capsFirstName + delimiter + (++x) + delimiter + capsLastName;
      }
    }
    catch (SQLException e) {
      error(method, e.getMessage());
    }
    catch (TaskException e) {
      error(method, e.getMessage());
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }

    trace(method, SystemMessage.METHOD_EXIT);
    return composedName;
  }
}