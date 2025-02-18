/*
    Oracle Deutschland BV & Co. KG

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

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   AssertionStore.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionStore.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity;

import java.util.Set;
import java.util.HashSet;

import java.util.logging.Logger;

import java.io.Serializable;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.security.enterprise.CallerPrincipal;

import javax.security.enterprise.credential.Credential;

import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.CredentialValidationResult;

import oracle.hst.platform.core.entity.ExpiringMap;

import bka.iam.identity.jmx.TokenAsserterConfigurationMBean;

////////////////////////////////////////////////////////////////////////////////
// abstract class AssertionStore
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A literal representation of the {@code AssertionStoreDefinition}.
 **
 ** @param  <T>                  the java type of the configuration the
 **                              {@code AssertionStoreDefinition} relies on.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AssertionStore<T extends TokenAsserterConfigurationMBean> implements Serializable
                                                                                ,          IdentityStore {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String THIS             = AssertionStore.class.getName();
  private static final Logger LOGGER           = Logger.getLogger(THIS);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3169914586787457102")
  private static final long   serialVersionUID = -2061511050354600408L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

	protected T                                config;

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  protected DataSource                       dataSource = null;

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  protected ExpiringMap<String, Set<String>> permission = ExpiringMap.build();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a {@link IdentityStore} that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  protected AssertionStore() {
    // ensure inheritance
    super();

    // initialize instance attributes
    config();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (IdentityStore)
  /**
   ** Validates the given credential.
   ** As a convenience, a default implementation is provided that looks up an
   ** overload of this method that has, as its one and only parameter, a
   ** subclass of {@link Credential}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The overloaded method is only called when the actual type passed into this
   ** method will exactly match the parameter type of the overloaded method.
   ** There's no attempt being done to find the most specific overloaded method
   ** such as specified in JLS 15.2.
   ** <p>
   ** This method returns a {@link CredentialValidationResult} representing the
   ** result of the validation attempt: whether it succeeded or failed, and, for
   ** a successful validation, the {@link CallerPrincipal}, and possibly groups
   ** or other attributes, of the caller.
   ** 
   ** @param  credential         the credential to validate.
   **                            <br>
   **                            Allowed object is {@link Credential}.
   **
   ** @return                    the validation result.
   **                            <br>
   **                            Possible object is
   **                            {@link CredentialValidationResult}.
   */
  @Override
  public CredentialValidationResult validate(final Credential credential) {
    if (!(credential instanceof AssertionCredential))
      return CredentialValidationResult.NOT_VALIDATED_RESULT;

    try {
      final CallerPrincipal principal = validate((AssertionCredential)credential);
      return new CredentialValidationResult(config().getRealm(), principal, principal.getName(), "", authorize(principal.getName()));
    }
    catch (AssertionException e) {
      LOGGER.severe(e.getLocalizedMessage());
      return CredentialValidationResult.INVALID_RESULT;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   config
  /**
   ** Retrieve the configuration singleton.
   **
   ** @return                    the configuration singleton.
   **                            <br>
   **                            Possible object is <code>T</code>}.
   */
  @SuppressWarnings("unchecked") 
  protected abstract T config();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Given a user-provided token credential, return an optional principal.
   ** <p>
   ** If the credentials are valid and map to a principal, returns a
   ** {@link CallerPrincipal}.
   **
   ** @param  credential         a user-provided token credential.
   **                            <br>
   **                            Allowed object is {@link AssertionCredential}.
   **
   ** @return                    an authenticated principal.
   **                            <br>
   **                            Possible object is {@link CallerPrincipal}.
   **
   ** @throws AssertionException if the credentials cannot be authenticated due
   **                            to an underlying error.
   */
  protected abstract CallerPrincipal validate(final AssertionCredential credential)
    throws AssertionException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorize
  /**
   ** Returns the names of all permissions belonging to the user.
   ** <br>
   ** It loads the result from <code>permission</code> first. This is called
   ** from web path group verification, though it should not be.
   **
   ** @param  username           a name of the principal in this
   **                            {@link IdentityStore} whose permission listing
   **                            is needed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collected authorization names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws AssertionException if the permissions could not be populated due
   **                            to an underlying error.
   */
  protected final Set<String> authorize(final String username)
    throws AssertionException {

    // avoid digging down to the database if the principal was authorized before
    if (this.permission.containsKey(username))
      return this.permission.get(username);

    this.permission.put(username, permission(username));
    return this.permission.get(username);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticate
  /**
   ** Verifies that the given username still exists in the underlying database
   ** to ensure that a lockdown of princial the username belongs is detected.
   **
   ** @param  username           the login name of the user principal the
   **                            permissions are populated for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collected authorization names.
   **                            <br>
   **                            Possible object is array of {@link String}.
   **
   ** @throws AssertionException if user belonging to the specified
   **                            <code>username</code> doesn't exists or is
   **                            defined ambiguous.
   */
  protected boolean authenticate(final String username)
    throws AssertionException {

    ResultSet          resultSet  = null;
    Connection         connection = null;
    PreparedStatement  statement  = null;
    try {
      connection = aquire();
      statement  = connection.prepareStatement(config().getPrincipalQuery());
      statement.setString(1, username);
      resultSet = statement.executeQuery();
      if (!resultSet.next())
        throw AssertionException.unhandled("Ooops");
    }
    catch (SQLException e) {
      throw AssertionException.unhandled(e);
    }
    finally {
      if (statement != null)
        close(statement);
      if (connection != null)
        release(connection);
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** Populates the permissions assigned to the given principal username.
   **
   ** @param  username           a name of the principal in this
   **                            {@link IdentityStore} whose permission listing
   **                            is needed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collected authorization names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws AssertionException if the permissions could not be populated due
   **                            to an underlying error.
   */
  protected Set<String> permission(final String username)
    throws AssertionException {

    ResultSet         resultSet  = null;
    Connection        connection = null;
    PreparedStatement statement  = null;
    final Set<String> collector  = new HashSet<String>();
    collector.add("authenticated-user");
    try {
      connection = aquire();
      statement  = connection.prepareStatement(config().getPermissionQuery());
      statement.setString(1, username);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        // populate the permissions fetched from the database as appropriate
        collector.add(resultSet.getString(1));
      }
      return collector;
    }
    catch (SQLException e) {
      throw AssertionException.unhandled(e);
    }
    finally {
      if (statement != null)
        close(statement);
      if (connection != null)
        release(connection);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Aquire a connection from the JDBC DataSoure.
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @return                    the JDBC connection aquired.
   **                            <br>
   **                            Possible object is {@link Connection}.
   **
   ** @throws AssertionException if the JDBC {@link Connection} could not be
   **                            aquired.
   */
  protected final Connection aquire()
    throws AssertionException {

    try {
      return lookup().getConnection();
    }
    catch (SQLException e) {
      throw AssertionException.unhandled(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release the given JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @param  connection         the JDBC connection to close.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   */
  protected final void release(final Connection connection) {
    if (connection != null) {
      try {
        // make sure that we will commit our unit of work if neccessary
        connection.close();
      }
      catch (SQLException e) {
        // handle silenlty
        LOGGER.severe(e.getLocalizedMessage());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close a previous prepared statement
   ** <br>
   ** If the statement has a dependence result set, the result set will also be
   ** closed.
   **
   ** @param  statement          the {@link PreparedStatement} to close.
   **                            <br>
   **                            Allowed object is {@link PreparedStatement}.
   */
  protected final void close(final PreparedStatement statement) {
    // prevent bogus input
    if (statement == null)
      return;

    try {
      close(statement.getResultSet());
    }
    catch (SQLException e) {
      // handle silenlty
      LOGGER.severe(e.getLocalizedMessage());
    }
    finally {
      try {
        statement.close();
      }
      catch (SQLException e) {
        // handle silenlty
        LOGGER.severe(e.getLocalizedMessage());
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
  private void close(final ResultSet resultSet) {
    // prevent bogus input
    if (resultSet != null)
      try {
        resultSet.close();
      }
      catch (SQLException e) {
        // handle silenlty
        LOGGER.severe(e.getLocalizedMessage());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Initialize the JDBC DataSource.
   **
   ** @return                    the {@link DataSource} looked-up from the
   **                            JNDI tree.
   **                            <br>
   **                            Allowed object is {@link DataSource}.
   **
   ** @throws AuthenticationException if the JDBC {@link DataSource} could not
   **                                 be retrieved.
   */
  private DataSource lookup()
    throws AssertionException {

    if (this.dataSource == null) {
      try {
        InitialContext context = null;
        try {
          context = new InitialContext();
        }
        catch (NamingException e) {
          LOGGER.severe(e.getLocalizedMessage());
          throw AssertionException.unhandled(e);
        }
        this.dataSource = (DataSource)context.lookup(config().getDataSource());
      }
      catch (NamingException e) {
        LOGGER.severe(e.getLocalizedMessage());
        throw AssertionException.unhandled(e);
      }
    }
    return this.dataSource;
  }
}