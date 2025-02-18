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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   GlassFish Server Security Realm

    File        :   Database.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Database.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.realm;

import java.util.List;
import java.util.Arrays;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Enumeration;

import java.io.Reader;

import java.nio.charset.Charset;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.security.auth.login.LoginException;

import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;

////////////////////////////////////////////////////////////////////////////////
// class Database
// ~~~~~ ~~~~~~~~
/**
 ** Implementation of a <b>Realm</b> that authenticates users via the <em>Java
 ** Authentication and Authorization Service</em> (JAAS) leveraging a database.
 ** <p>
 ** The value configured for the <code>realmName</code> property is passed to
 ** the <code>javax.security.auth.login.LoginContext</code> constructor, to
 ** specify the <em>application name</em> used to select the set of relevant
 ** <code>LoginModules</code> required.
 ** <p>
 ** The JAAS Specification describes the result of a successful login as a
 ** <code>javax.security.auth.Subject</code> instance, which can contain zero or
 ** more <code>java.security.Principal</code> objects in the return value of the
 ** <code>Subject.getPrincipals()</code> method.
 ** <br>
 ** However, it provides no guidance on how to distinguish Principals that
 ** describe the individual user (and are thus appropriate to return as the
 ** value of request.getUserPrincipal() in a web application) from the
 ** Principal(s) that describe the authorized roles for this user.
 ** <br>
 ** To maintain as much independence as possible from the underlying
 ** <code>LoginMethod</code> implementation executed by JAAS, the following
 ** policy is implemented by this Realm:
 ** <ul>
 **   <li>The JAAS <code>LoginModule</code> is assumed to return a
 **        <code>Subject</code> with at least one <code>Principal</code>
 **        instance representing the user himself or herself, and zero or more
 **        separate <code>Principals</code> representing the security roles
 **        authorized for this user.
 **    <li>On the <code>Principal</code> representing the user, the Principal
 **        name is an appropriate value to return via the Servlet API method
 **        <code>HttpServletRequest.getRemoteUser()</code>.
 **    <li>On the <code>Principals</code> representing the security roles, the
 **        name is the name of the authorized security role.
 **    <li>This Realm will be configured with two lists of fully qualified Java
 **        class names of classes that implement
 **        <code>java.security.Principal</code> - one that identifies class(es)
 **        representing a user, and one that identifies class(es) representing a
 **        security role.
 **    <li>As this Realm iterates over the <code>Principals</code> returned by
 **        <code>Subject.getPrincipals()</code>, it will identify the first
 **        <code>Principal</code> that matches the "user classes" list as the
 **        <code>Principal</code> for this user.
 **    <li>As this Realm iterates over the <code>Principals</code> returned by
 **        <code>Subject.getPrincipals()</code>, it will accumulate the set of
 **        all <code>Principals</code> matching the "role classes" list as
 **        identifying the security roles for this user.
 **    <li>It is a configuration error for the JAAS login method to return a
 **        validated <code>Subject</code> without a <code>Principal</code> that
 **        matches the "user classes" list.
 ** </ul>
 ** <P>
 ** The <b>Database</b> needs the following properties in its
 ** configuration:
 ** <br>
 ** <b>Required</b>
 ** <ul>
 **   <li>jaas-context     - JAAS context name used to access LoginModule for
 **                          authentication.
 **   <li>data-source      - the jndi name of datasource without the prefix
 **                          <code>/jdbc</code>.
 **   <li>password-query   - the JDBC statement to run obtaining the password of
 **                          a valid user.
 **   <li>permission-query - the JDBC statement to run to obtain the permissions
 **                          granted to a valid user.
 ** </ul>
 ** <br>
 ** <b>Optional</b>
 ** <ul>
 **   <li>digest-algorithm - the digest algorithm
 **                          <br>
 **                          Default: <code>SHA-256</code>
 **   <li>digest-encoding  - the character encoding to apply on a password
 **                          digest.
 **                          <br>
 **                          Possible Vakues: <code>base64</code> | <code>hexadecimal</code>
 **                          <br>
 **                          Default: <code>base64</code>
 **   <li>password-charset - the character set encoding a password digest.
 **                          <br>
 **                          Default: <code>Charset.defaultCharset().name()</code>
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Database extends Realm {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String              passwordQuery;
  private String              permissionQuery;
  private DataSource          dataSource;
  private Password.Codec      passwordCodec;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Database</code> {@link Realm} that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public Database() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstrcat base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAuthType (Realm)
  /**
   ** Returns a short (preferably less than fifteen characters) description of
   ** the kind of authentication which is supported by this realm.
   **
   ** @return                    the description of the kind of authentication
   **                            that is directly supported by this realm.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getAuthType() {
    return "jdbc";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGroupNames (Realm)
  /**
   ** Returns the name of all the groups that the specified user belongs to.
   ** <br>
   ** It loads the result from <code>groupCache</code> first. This is called
   ** from web path group verification, though it should not be.
   ** <p>
   ** <b>WARNING</b>:
   ** <br>
   ** Does not have access to user's entitlements, so it does not return groups
   ** based on assertion.
   **
   ** @param  username           the name of the user in this realm whose group
   **                            listing is needed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the enumeration of group names assigned to all
   **                            users authenticated by this realm belonging to
   **                            the specified <code>username</code>.
   **                            <br>
   **                            Possible object is {@link Enumeration} where
   **                            each element is of type {@link String}.
   */
  @Override
  public Enumeration<String> getGroupNames(final String username) {
    Vector<String> vector = this.groupCache.get(username);
    if (vector == null) {
      authorize(username, authorize(username));
      vector = this.groupCache.get(username);
    }
    return vector.elements();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (overridden)
  /**
   ** Initialize a realm with some properties.
   ** <br>
   ** This can be used when instantiating realms from their descriptions. This
   ** method is invoked from Realm during initialization.
   **
   ** @param  store              the initialization parameters used by this
   **                            realm.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @throws BadRealmException    if the configuration parameters identify a
   **                              corrupt realm.
   ** @throws NoSuchRealmException if the configuration parameters specify a
   **                              realm which doesn't exist.
   */
  @Override
  protected void init(final Properties store)
    throws BadRealmException
    ,      NoSuchRealmException {

    // ensure inheritance
    super.init(store);

    this.passwordQuery      = propertyRequired(store, "password-query");
    this.permissionQuery    = propertyRequired(store, "permission-query");
    final String dataSource = propertyRequired(store, "data-source");
    try {
      InitialContext context = null;
      try {
        context = new InitialContext();
      }
      catch (NamingException e) {
        throw new BadRealmException(RealmBundle.string(RealmError.CONTEXT_INITIALIZE, e.getLocalizedMessage()));
      }
      this.dataSource =  (DataSource)context.lookup(dataSource);
    }
    catch (NamingException e) {
      throw new BadRealmException(RealmBundle.string(RealmError.OBJECT_LOOKUP, dataSource));
    }

    String algorithm   = propertyOptional(store, "digest-algorithm", Password.DIGEST_ALGORITHM_DEFAULT);
    this.passwordCodec = algorithm.equalsIgnoreCase("none") ? Password.plain() : Password.digest(algorithm, Charset.forName(propertyOptional(store, "password-charset", Charset.defaultCharset().name())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticateUser
  /**
   ** Invoke the native authentication call.
   **
   ** @param  username           the username to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the given password.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the groups belonging to a valid user or
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is array of {@link String}.
   **
   ** @throws LoginException     if either the user does not exists or there is
   **                            no password to validate.
   **/
  public String[] authenticateUser(final String username, final byte[] password)
    throws LoginException {

    return validate(username, password) ? addAssignGroups(authorize(username)) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertUser
  /**
   ** Invoke the native authentication call.
   **
   ** @param  username           the username to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the groups belonging to a valid user or
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is array of {@link String}.
   **
   ** @throws LoginException     if either the user does not exists or there is
   **                            no password to validate.
   **/
  public String[] assertUser(final String username)
    throws LoginException {

    return validate(username) ? addAssignGroups(authorize(username)) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the user credetials by lookup the user in the connected database
   ** repository and compares the password hashes.
   **
   ** @param  username           the username to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the given password.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    <code>true</code> if the user password match;
   **                            otheriwse <code>false</code>.
   **
   ** @throws LoginException     if either the user does not exists or there is
   **                            no password to validate.
   */
  private boolean validate(final String username, final byte[] password)
    throws LoginException {
    
    // prevent bogus input
    if (username == null || username.length() == 0)
      throw new LoginException("User does not exists");

    ResultSet          resultSet  = null;
    Connection         connection = null;
    PreparedStatement  statement  = null;
    try {
      connection = this.dataSource.getConnection();
      statement  = connection.prepareStatement(this.passwordQuery);
      statement.setString(1, username);
      resultSet = statement.executeQuery();
      if (!resultSet.next())
        throw new LoginException("No user found for username!" + username);

      try (Reader reader = resultSet.getCharacterStream(1)) {
        final char[] buffer = new char[1024];
        int    length  = reader.read(buffer);
        // password should be required
        if (length <= 0)
          throw new LoginException("User has no password!" + username);

        // since buffer contains 1024 elements arbitrarily initialized,
        // construct a new char[] that has the right no of char elements to be
        // used for equal comparison
        final char[] encoded = new char[length];
        System.arraycopy(encoded, 0, buffer, 0, length);
      }
      final String database = resultSet.getString(1);
      // password should be required
      if (database == null)
        throw new LoginException("User has no password!" + username);

      // handle password
      byte[] encoded = this.passwordCodec.encode(password);
      byte[] trimmed = database.trim().getBytes();
      return Arrays.equals(trimmed, encoded);
    }
    catch (Exception e) {
      throw new LoginException(e.getLocalizedMessage());
    }
    finally {
      if (statement != null)
        close(statement);
      if (connection != null)
        release(connection);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the user existance by lookup the user in the connected database
   ** repository.
   **
   ** @param  username           the username to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the user was found;
   **                            otheriwse <code>false</code>.
   **
   ** @throws LoginException     if either the user does not exists.
   */
  private boolean validate(final String username)
    throws LoginException {

    // prevent bogus input
    if (username == null || username.length() == 0)
      throw new LoginException(RealmBundle.string(RealmError.USER_REQUIRED));

    ResultSet          resultSet  = null;
    Connection         connection = null;
    PreparedStatement  statement  = null;
    try {
      connection = this.dataSource.getConnection();
      statement  = connection.prepareStatement(this.passwordQuery);
      statement.setString(1, username);
      resultSet = statement.executeQuery();
      if (!resultSet.next())
        throw new LoginException(RealmBundle.string(RealmError.USER_NOTFOUND, username));
      return true;
    }
    catch (Exception e) {
      throw new LoginException(e.getLocalizedMessage());
    }
    finally {
      if (statement != null)
        close(statement);
      if (connection != null)
        release(connection);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorize
  /**
   ** Populates the permissions assigned to the given principal username.
   **
   ** @param  username           the login name of the user principal the
   **                            permissions are populated for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collected autorization names.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  private String[] authorize(final String username) {
    ResultSet          resultSet  = null;
    Connection         connection = null;
    PreparedStatement  statement  = null;
    final List<String> collector  = new ArrayList<String>();
    collector.add("authenticated-user");
    try {
      connection = this.dataSource.getConnection();
      statement  = connection.prepareStatement(this.permissionQuery);
      statement.setString(1, username);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        // populate the permissions fetched from the database as appropriate
        collector.add(resultSet.getString(1));
      }
      final String[] array = new String[collector.size()];
      return collector.toArray(array);
    }
    catch (SQLException e) {
      throw new RuntimeException(e.getLocalizedMessage());
    }
    finally {
      if (statement != null)
        close(statement);
      if (connection != null)
        release(connection);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorize
  /**
   ** Delegate method for caching users permissions a principal username belongs
   ** to.
   **
   ** @param  username           the login name of the user principal the
   **                            permissions are cached for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  permission         the collection of permission granted to
   **                            the principal username.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  private void authorize(final String username, final String[] permission) {
    Vector<String> v = null;
    if (permission == null) {
      v = new Vector<String>();
    }
    else {
      v = new Vector<>(permission.length + 1);
      for (String group : permission) {
        v.add(group);
      }
    }
    synchronized (this) {
      this.groupCache.put(username, v);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release the given JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @param                     the JDBC connection to close.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   */
  private void release(final Connection connection) {
    if (connection != null) {
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
  private void close(final PreparedStatement statement) {
    // prevent bogus input
    if (statement == null)
      return;

    try {
      close(statement.getResultSet());
    }
    catch (SQLException e) {
      // handle silenlty
      e.printStackTrace(System.err);
    }
    finally {
      try {
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
  private void close(final ResultSet resultSet) {
    // prevent bogus input
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