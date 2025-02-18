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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Administration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Administration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.persistence;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractMetadataTask;

import oracle.iam.identity.foundation.persistence.DatabaseError;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;
import oracle.iam.identity.foundation.persistence.DatabaseSearch;
import oracle.iam.identity.foundation.persistence.DatabaseFeature;
import oracle.iam.identity.foundation.persistence.DatabaseResource;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseException;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.resource.DatabaseBundle;

import oracle.iam.identity.dbs.resource.AdministrationBundle;

////////////////////////////////////////////////////////////////////////////////
// class Administration
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Administration</code> is responsible to store and retrieve
 ** information from the data dictionary of a Database Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class Administration extends DatabaseConnection {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** This regular expression uses one group to substitute expressions with a
   ** String.
   ** <p>
   ** Groups are defined by parentheses. Note that ?: will define a
   ** group as "non-contributing"; that is, it will not contribute to the return
   ** values of the <code>group</code> method.
   */
  private static final String STRING_PATTERN = "\\$\\[((?:\\w|\\s)+)]";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Dialect             dialect;
  private Connection          connection;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Administration</code> which is associated
   ** with the specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link DatabaseConnection}.
   ** @param  resource           the {@link DatabaseResource} IT Resource
   **                            definition where this connector is associated
   **                            with.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public Administration(final AbstractMetadataTask task, final DatabaseResource resource)
    throws TaskException {

    // ensure inheritance
    super(task, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Administration</code> which is associated
   ** with the specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link DatabaseConnection}.
   ** @param  serverInstance     the system identifier of the
   **                            <code>IT Resource</code> instance where this
   **                            connector is associated with.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public Administration(final AbstractMetadataTask task, final Long serverInstance)
    throws TaskException {

    // ensure inheritance
    super(task, serverInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Administration</code> which is associated
   ** with the specified task.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseConnection}.
   ** @param  resource           the {@link DatabaseResource} IT Resource
   **                            definition where this connector is associated
   **                            with.
   ** @param  feature            the Lookup Definition providing the target
   **                            system specific features like objectClasses,
   **                            attribute id's etc.
   */
  public Administration(final Loggable loggable, final DatabaseResource resource, final DatabaseFeature feature) {
    // ensure inheritance
    super(loggable, resource, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwrap
  /**
   ** Returns the {@link Connection} this context is using to connect and
   ** perform operations on the database server.
   **
   ** @return                    the {@link Connection} this task is using to
   **                            connect and perform operations on the database
   **                            server.
   */
  public final Connection unwrap() {
    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Connects this dictionary to the database
   **
   ** @throws DatabaseException  if the operation fails
   */
  public Connection connect()
    throws TaskException {

    // prevent bogus state of instance
    if (this.connection != null)
      throw new DatabaseException(DatabaseError.INSTANCE_ILLEGAL_STATE, "connection");

    try {
      this.connection = super.connect();
    }
    catch (DatabaseException e) {
      final String code = this.dialectErrorCode(e);
      if (DatabaseError.CONNECTION_UNKNOWN_HOST.equals(code))
        throw new DatabaseException(code, this.resource.name());
      if (DatabaseError.CONNECTION_CREATE_SOCKET.equals(code))
        throw new DatabaseException(code, this.resource.name());
      if (DatabaseError.CONNECTION_TIMEOUT.equals(code))
        throw new DatabaseException(code, this.resource.name());
      if (DatabaseError.CONNECTION_ERROR.equals(code))
        throw new DatabaseException(code, this.resource.name());

      if (DatabaseError.CONNECTION_AUTHENTICATION.equals(code))
        throw new DatabaseException(code, this.resource.principalName());
      if (DatabaseError.CONNECTION_PERMISSION.equals(code)) {
        final String[] arguments = { this.resource.principalName(), this.resource.name() };
        throw new DatabaseException(code, arguments);
      }
    }
    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Close the connection aquired by {@link #connect()} and frees all resources.
   */
  public void disconnect() {

    // prevent bogus state of instance
    if (this.connection == null)
      return;

    super.disconnect(this.connection);
    this.connection = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime (override)
  /**
   ** The current time will be different from this system time.
   ** <p>
   ** To be able to plugin your own method this is the placeholder method to
   ** fetch the server time from the target system.
   ** <p>
   ** This current implementation check if the system is connected and ask the
   ** connected server for the system timestamp. If the connection is not valid
   ** at the time this method is invoked it returns simple the current data of
   ** the machine where this library is used.
   **
   ** @return                    the timestamp of the remote system if
   **                            applicable; the local time otherwise.
   **
   ** @throws TaskException      if the operation fails
   */
  @Override
  public Date systemTime()
    throws TaskException {

    try {
      if (this.connection != null && !this.connection.isClosed())
        return systemTime(this.connection);
      else
        return new Date();
    }
    catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectEntity
  /**
   ** Fetchs a {@link DatabaseEntity} for the given key from the associated
   ** dictionary.
   **
   ** @param  entity             the key for the desired {@link DatabaseEntity}.
   **
   ** @return                    the {@link DatabaseEntity} for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>dictionary</code>
   **                            <code>null</code> will be returned.
   **
   ** @throws TaskException      if the requested <code>Dialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the specified
   **                            <code>driverClass</code>.
   */
  public final DatabaseEntity dialectEntity(final Dialect.Entity entity)
    throws TaskException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.entity(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectCatalog
  /**
   ** Fetchs a {@link Catalog} for the given type from the associated
   ** dictionary.
   **
   ** @param  type               the type of the desired {@link Catalog}.
   **
   ** @return                    the {@link Catalog} for the given catalog key.
   **                            If <code>null</code> was passed as
   **                            <code>permission</code> <code>null</code> will
   **                            be returned.
   **
   ** @throws TaskException      if the requested <code>Dialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the specified
   **                            <code>driverClass</code>.
   */
  public final Catalog dialectCatalog(final Catalog.Type type)
    throws TaskException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.catalog(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectOperation
  /**
   ** Fetchs a <code>String</code> for the given key from statement bundle or
   ** one of its parents.
   **
   ** @param  operation          the key for the desired operation string.
   **
   ** @return                    the string for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>dictionary</code> an empty
   **                            string will be returned.
   **
   ** @throws TaskException      if the requested <code>Dialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the specified
   **                            <code>driverClass</code>.
   */
  public final String dialectOperation(final Dialect.Operation operation)
    throws TaskException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.operation(operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectPermission
  /**
   ** Fetchs a {@link Catalog} for the given permission key.
   **
   ** @param  type               the type of the desired {@link Catalog}.
   **
   ** @return                    the {@link Catalog} for the given permission
   **                            key. If <code>null</code> was passed as
   **                            <code>permission</code> <code>null</code> will
   **                            be returned.
   **
   ** @throws TaskException      if the requested <code>Dialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the specified
   **                            <code>driverClass</code>.
   */
  public final Catalog dialectPermission(final Catalog.Type type)
    throws TaskException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.permission(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectAccountFilter
  /**
   ** Returns the filter that has to be applied on a search for accounts.
   **
   ** @param  timestamp          the time after that an account in the target
   **                            system should be created or modified to be
   **                            included in the returning collection.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   **
   ** @throws TaskException      if the requested <code>Dialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the specified
   **                            <code>driverClass</code>.
   */
  public final DatabaseFilter dialectAccountFilter(final Date timestamp)
    throws TaskException {

    if (this.dialect == null)
      installDialect();

    DatabaseFilter filter = this.dialect.accountFilter();
    if (timestamp != null)
      filter = DatabaseFilter.build(dialectAccountTime(timestamp), filter, DatabaseFilter.Operator.AND);
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectAccountTime
  /**
   ** Returns the filter that has to be applied on a search for accounts.
   **
   ** @param  timestamp          the time after that an account in the target
   **                            system should be created or modified to be
   **                            included in the returning collection.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   **
   ** @throws TaskException      if the requested <code>Dialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the specified
   **                            <code>driverClass</code>.
   */
  public final DatabaseFilter dialectAccountTime(final Date timestamp)
    throws TaskException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.accountTime(timestamp);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectErrorCode
  /**
  /**
   ** Returns the implementation specific error code for a vendor specific code.
   ** <p>
   ** The vendor code is taken from an SQLException that is catched somewhere
   ** and wrapped in a {@link DatabaseException}. This prefix the vendor
   ** specific code constantly with <code>SQL-</code>.
   **
   ** @param  throwable          the exception thrown by a process step that
   **                            may contain a vendor spefific error code.
   **
   ** @return                    a implementation specific error code if it's
   **                            translatable; otherwise <code>DBA-0001</code>
   **                            will be returned.
   **
   ** @throws TaskException      if the requested <code>Dialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the specified
   **                            <code>driverClass</code>.
   */
  public final String dialectErrorCode(final DatabaseException throwable)
    throws TaskException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.normalizeError(throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupPrivileges
  /**
   ** Reads all predefined system privileges from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @param  exclusions         the {@link Set} with the privileges names which
   **                            should not be contained in the returning
   **                            {@link Set}.
   **
   ** @return                    a {@link Set} containing the available system
   **                            privileges without the specified
   **                            <code>exclusions</code>.
   **                            Each entry in the returned list is a
   **                            {@link String} containing the name of the
   **                            predefined system privilege.
   **
   ** @throws TaskException      in case an error does occure.
   */
  public final Set<String> lookupPrivileges(final Set<String> exclusions)
    throws TaskException {

    return select(dialectCatalog(Catalog.Type.Privilege), exclusions);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRoles
  /**
   ** Reads all role information from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @param  exclusions         the {@link Set} with the role names which
   **                            should not be contained in the returning
   **                            {@link Set}.
   **
   ** @return                    a <code>List</code> containing the available
   **                            roles  without the specified
   **                            <code>exclusions</code>..
   **                            Each entry in the returned list is a
   **                            {@link String} containing the name of the
   **                            role.
   **
   ** @throws TaskException      in case an error does occure.
   */
  public final Set<String> lookupRoles(final Set<String> exclusions)
    throws TaskException {

    return select(dialectCatalog(Catalog.Type.Role), exclusions);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupProfile
  /**
   ** Reads all profile information from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            profiles.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupProfile()
    throws TaskException {

    return select(dialectCatalog(Catalog.Type.Profile), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupTablespacePermanent
  /**
   ** Reads all permanant tablespace information from the data dictionary.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            permanent tablespace.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupTablespacePermanent()
    throws TaskException {

    return select(dialectCatalog(Catalog.Type.TablespacePermanent));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupTablespaceTemporary
  /**
   ** Reads all temporary tablespace information from the data dictionary.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            temporary tablespace.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupTablespaceTemporary()
    throws TaskException {

    return select(dialectCatalog(Catalog.Type.TablespaceTemporary));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSchema
  /**
   ** Reads all schema information from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            schemes.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupSchema()
    throws TaskException {

    return lookupCatalog(Catalog.Type.Schema);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupTable
  /**
   ** Reads all table information from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            tables.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupTable()
    throws TaskException {

    return lookupCatalog(Catalog.Type.Table);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupView
  /**
   ** Reads all view information from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            views.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupView()
    throws TaskException {

    return lookupCatalog(Catalog.Type.View);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupType
  /**
   ** Reads all user defined type information from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            user defined types.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupType()
    throws TaskException {

    return lookupCatalog(Catalog.Type.Type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupFunction
  /**
   ** Reads all stored function information from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            stored functions.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupFunction()
    throws TaskException {

    return lookupCatalog(Catalog.Type.Function);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupProcedure
  /**
   ** Reads all stored procedure information from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            stored procedures.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupProcedure()
    throws TaskException {

    return lookupCatalog(Catalog.Type.Procedure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupPackage
  /**
   ** Reads all stored package information from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            stored packages.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupPackage()
    throws TaskException {

    return lookupCatalog(Catalog.Type.Package);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupCatalog
  /**
   ** Reads all information of a specific catalog from the data dictionary.
   ** <p>
   ** There will be not a pagination implement on this due to we are expecting
   ** that enough memory is available.
   **
   ** @param  type               the type of the {@link Catalog} to select from
   **                            the data dictionary.
   **
   ** @return                    a <code>Set</code> containing the requested
   **                            {@link Catalog}.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> lookupCatalog(final Catalog.Type type)
    throws TaskException {

    return select(dialectCatalog(type));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountDetail
  /**
   ** Reads all account information from the data dictionary.
   **
   ** @param  username           the account name where the attributes should
   **                            be returned from the database.
   ** @param  returning          the names of the attributes comprising an
   **                            account in the database.
   **
   ** @return                    a <code>Map</code>  with the attributes
   **                            username, profile, default tablespace,
   **                            temporary tablespace
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Map<String, Object> accountDetail(final String username, final List<Pair<String, String>> returning)
    throws TaskException {

    // prevent bogus state of instance
    if (this.connection == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    // perform the database operation to fetch an account profile
    final DatabaseEntity entity = dialectEntity(Dialect.Entity.ACCOUNT);
    return super.findEntry(this.connection, entity, username, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadGrantedPrivileges
  /**
   ** Reads all system privileges from the data dictionary granted to the
   ** specified account name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            roles should be returned from the database.
   ** @param  exclusionName      the name of the {@link Set} which belongs to
   **                            the identifier where the exclusions are defined
   **                            in.
   ** @param  exclusions         the {@link Set} with the names privileges
   **                            which should not be contained in the returning
   **                            {@link List}.
   **
   ** @return                    a {@link List} containing the available
   **                            roles granted to the specified account name.
   **                            The does not contain any role specified by
   **                            <code>exclusion</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadGrantedPrivileges(final String grantee, final String exclusionName, final Set<String> exclusions)
    throws TaskException {

    // prevent bogus state of instance
    if (this.connection == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    final String method = "loadGrantedPrivileges";
    trace(method, SystemMessage.METHOD_ENTRY);

    final DatabaseEntity      entity    = dialectPermission(Catalog.Type.Privilege);
    final DatabaseFilter      filter    = DatabaseFilter.build(entity.primary(), grantee, DatabaseFilter.Operator.EQUAL);
    final DatabaseSelect      statement = DatabaseSelect.build(this, entity, filter, entity.returning());

    List<Map<String, Object>> list      = null;
    try {
      list = statement.execute(this.connection);
      // iterate the retrieved list in reverse order to avoid that we skip
      // entries in the middle
      for (int i = list.size() - 1; i > 0; i--) {
        final Map<String, Object> grant = list.get(i);
        final String name = (String)grant.get(entity.primary());
        // handle exclusions
        if (exclusions != null && exclusions.contains(name)) {
          final String[] arguments = {name, exclusionName };
          warning(AdministrationBundle.format(AdministrationMessage.EXCLUDE_PRIVILEGE, arguments));
          list.remove(i);
        }
      }
    }
    catch (DatabaseException e) {
      if (list != null)
        list.clear();
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadGrantedRoles
  /**
   ** Reads all role information from the data dictionary granted to the
   ** specified account name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            roles should be returned from the database.
   ** @param  exclusionName      the name of the {@link Set} which specifies the
   **                            lookup where the exclusions be defined in.
   ** @param  exclusions         the {@link Set} with the names roles which
   **                            should not be contained in the returning
   **                            {@link Set}.
   **
   ** @return                    a {@link Set} containing the available
   **                            roles granted to the specified account or role
   **                            name.
   **                            The does not contain any role specified by
   **                            <code>exclusion</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadGrantedRoles(final String grantee, final String exclusionName, final Set<String> exclusions)
    throws TaskException {

    // prevent bogus state of instance
    if (this.connection == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    final String method = "loadGrantedRoles";
    trace(method, SystemMessage.METHOD_ENTRY);

    final DatabaseEntity      entity    = dialectPermission(Catalog.Type.Role);
    final DatabaseFilter      filter    = DatabaseFilter.build(entity.primary(), grantee, DatabaseFilter.Operator.EQUAL);
    final DatabaseSelect      statement = DatabaseSelect.build(this, entity, filter, entity.returning());

    List<Map<String, Object>> list      = null;
    try {
      list = statement.execute(this.connection);
      // iterate the retrieved list in reverse order to avoid that we skip
      // entries in the middle
      for (int i = list.size() - 1; i > 0; i--) {
        final Map<String, Object> grant = list.get(i);
        final String name = (String)grant.get(entity.primary());
        // handle exclusions
        if (exclusions != null && exclusions.contains(name)) {
          final String[] arguments = {name, exclusionName };
          warning(AdministrationBundle.format(AdministrationMessage.EXCLUDE_PRIVILEGE, arguments));
          list.remove(i);
        }
      }
    }
    catch (DatabaseException e) {
      if (list != null)
        list.clear();
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadSequencePrivileges
  /**
   ** Reads all privileges on sequences from the data dictionary where the
   ** grantee is the specified account or role name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            sequences should be returned for from the
   **                            database.
   **
   ** @return                    an array of {@link Map} containing the granted
   **                            sequence privileges to the specified account or
   **                            role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadSequencePrivileges(final String grantee)
    throws TaskException {

    return loadGrantedPermission(grantee, Catalog.Type.Sequence);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadSynonymPrivileges
  /**
   ** Reads all privileges on synonyms from the data dictionary where the
   ** grantee is the specified account or role name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            synonyms should be returned for from the
   **                            database.
   **
   ** @return                    an array of {@link Map} containing the granted
   **                            synonyms privileges to the specified account or
   **                            role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadSynonymPrivileges(final String grantee)
    throws TaskException {

    return loadGrantedPermission(grantee, Catalog.Type.Synonym);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadTablePrivileges
  /**
   ** Reads all privileges on tables from the data dictionary where the grantee
   ** is the specified account or role name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            tables should be returned for from the
   **                            database.
   **
   ** @return                    an array of {@link Map} containing the granted
   **                            tables privileges to the specified account or
   **                            role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadTablePrivileges(final String grantee)
    throws TaskException {

    return loadGrantedPermission(grantee, Catalog.Type.Table);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadViewPrivileges
  /**
   ** Reads all privileges on views from the data dictionary where the grantee
   ** is the specified account or role name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            views should be returned for from the
   **                            database.
   **
   ** @return                    an array of {@link Map} containing the granted
   **                            views privileges to the specified account or
   **                            role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadViewPrivileges(final String grantee)
    throws TaskException {

    return loadGrantedPermission(grantee, Catalog.Type.View);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadViewPrivileges
  /**
   ** Reads all privileges on user defined types from the data dictionary where
   ** the grantee is the specified account or role name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            views should be returned for from the
   **                            database.
   **
   ** @return                    an array of {@link Map} containing the granted
   **                            views privileges to the specified account or
   **                            role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadTypePrivileges(final String grantee)
    throws TaskException {

    return loadGrantedPermission(grantee, Catalog.Type.Type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadFunctionPrivileges
  /**
   ** Reads all privileges on stored functions from the data dictionary where
   ** the grantee is the specified account or role name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            functions should be returned for from the
   **                            database.
   **
   ** @return                    an array of {@link Map} containing the granted
   **                            functions privileges to the specified account
   **                            or role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadFunctionPrivileges(final String grantee)
    throws TaskException {

    return loadGrantedPermission(grantee, Catalog.Type.Function);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadProcedurePrivileges
  /**
   ** Reads all privileges on stored procedures from the data dictionary where
   ** the grantee is the specified account or role name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            procedures should be returned for from the
   **                            database.
   **
   ** @return                    an array of {@link Map} containing the granted
   **                            procedures privileges to the specified account
   **                            or role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadProcedurePrivileges(final String grantee)
    throws TaskException {

    return loadGrantedPermission(grantee, Catalog.Type.Procedure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadPackagePrivileges
  /**
   ** Reads all privileges on stored packages from the data dictionary where
   ** the grantee is the specified account or role name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            packages should be returned for from the
   **                            database.
   **
   ** @return                    an array of {@link Map} containing the granted
   **                            packages privileges to the specified account
   **                            or role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadPackagePrivileges(final String grantee)
    throws TaskException {

    return loadGrantedPermission(grantee, Catalog.Type.Package);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadJavaPrivileges
  /**
   ** Reads all privileges on stored java classes from the data dictionary where
   ** the grantee is the specified account or role name.
   **
   ** @param  grantee            the account or role name where the granted
   **                            java classes should be returned for from the
   **                            database.
   **
   ** @return                    an array of {@link Map} containing the granted
   **                            java class privileges to the specified account
   **                            or role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadJavaPrivileges(final String grantee)
    throws TaskException {

    return loadGrantedPermission(grantee, Catalog.Type.JavaClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadGrantedPermission
  /**
   ** Reads all privileges on objects from the data dictionary where the grantee
   ** is the specified account or role name and the object itself matchs the
   ** specified type.
   **
   ** @param  grantee            the account or role name where the granted
   **                            objects privileges should be returned for from
   **                            the database.
   ** @param  type               the type of the object where the privileges
   **                            should be returned for from the database.
   **
   ** @return                    a {@link List} of {@link Map}s containing the
   **                            granted object privileges to the specified
   **                            account or role name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final List<Map<String, Object>> loadGrantedPermission(final String grantee, final Catalog.Type type)
    throws TaskException {

    // prevent bogus state of instance
    if (type == null)
      throw TaskException.argumentIsNull("type");

    // prevent bogus state of instance
    if (this.connection == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    final String method = "loadGrantedPermission";
    trace(method, SystemMessage.METHOD_ENTRY);

    List<Map<String, Object>> list = null;

    final Catalog catalog = dialectPermission(type);
    if (catalog == null) {
      warning(method, DatabaseBundle.format(DatabaseError.OPERATION_NOT_SUPPORTED, type.toString()));
      trace(method, SystemMessage.METHOD_EXIT);
    }
    else {
      final DatabaseFilter objectFilter = catalog.filter();
      DatabaseFilter granteeFilter = DatabaseFilter.build(catalog.primary(), grantee, DatabaseFilter.Operator.EQUAL);
      if (objectFilter != null)
        granteeFilter = DatabaseFilter.build(granteeFilter, objectFilter, DatabaseFilter.Operator.AND);

      final DatabaseSelect statement = DatabaseSelect.build(this, catalog, granteeFilter, catalog.returning());
      try {
        list = statement.execute(this.connection);
      }
      catch (DatabaseException e) {
        if (list != null)
         list.clear();
        throw e;
      }
      finally {
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountSearch
  /**
   ** Reads all identifiers from the data dictionary that belongs to accounts
   ** which are created since the specified <code>timesatmp</code>.
   **
   ** @param  timestamp          the {@link Date} defining the time the accounts
   **                            are created after.
   **                            If <code>null</code> are passed all accounts
   **                            will be returned.
   ** @param  startRow           the start row of a page to fetch from the
   **                            Database Server.
   ** @param  lastRow            the last row of a page to fetch from the
   **                            Database Server.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            values for the specified query string.
   **                            Each entry in the returned list is a
   **                            {@link String} with the name of the value.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public final Set<String> accountSearch(final Date timestamp, final long startRow, final long lastRow)
    throws TaskException {

    // prevent bogus state of instance
    if (this.connection == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    // get the names of the attributes the values has to be fetched from the
    // account entity providing the details of an account.
    final DatabaseEntity             entity    = dialectEntity(Dialect.Entity.ACCOUNT);
    final List<Pair<String, String>> primary   = CollectionUtility.list(Pair.of(entity.primary(), entity.primary()));
    // build the statement with the filter to fetch an account
    // account entity providing the details of an account.
    final DatabaseSearch             statement = new DatabaseSearch(this, entity, dialectAccountFilter(timestamp), primary);
    statement.prepare(this.connection);

    // build the filter to fetch an account
    // account entity providing the details of an account.
    // perform the database operation to fetch an account profile
    final List<Map<String, Object>> nativly = statement.execute(startRow, lastRow);
    statement.close();

    // reverse the native attribute name to the logical representation and
    // return it to the invoker by take care about the sort order
    final Set<String> logical = new LinkedHashSet<String>(nativly.size());
    for (Map<String, Object> account : nativly) {
      if (account.size() != 1)
        throw new TaskException("Could never happens");
      logical.add((String)account.get(primary.get(0).tag));
    }

    return logical;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCreate
  /**
   ** Creates a database account with the specified password.
   **
   ** @param  entity             the mapping describing the account to create
   **                            with all the option that are applicable.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountCreate(final Map<String, Object> entity)
    throws TaskException {

    final String method = "accountCreate";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String username = (String)entity.remove(Dialect.USERNAME);
    final String password = (String)entity.remove(Dialect.PASSWORD);

    if (StringUtility.isEmpty(username) || StringUtility.isEmpty(password))
      throw new DatabaseException(DatabaseError.INSUFFICIENT_INFORMATION, Dialect.Operation.ACCOUNT_CREATE.toString());

    Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.USERNAME, username);
    parameter.put(Dialect.PASSWORD, password);

    // prevent bogus state of instance
    String template = dialectOperation(Dialect.Operation.ACCOUNT_CREATE);
    if (StringUtility.isEmpty(template))
      throw new DatabaseException(DatabaseError.OPERATION_NOT_SUPPORTED, this.dialect.toString());

    template = parseTemplate(template, STRING_PATTERN, Pattern.MULTILINE, parameter);
    if (entity.size() > 0) {
      StringBuilder builder = new StringBuilder(template);
      for (String option : entity.keySet())
        builder.append(" ").append(option).append(" ").append(entity.get(option).toString());
    }

    try {
      execute(template);
    }
    catch (DatabaseException e) {
      final String code = this.dialectErrorCode(e);
      if (DatabaseError.INSUFFICIENT_PRIVILEGE.equals(code)) {
        final String[] arguments = { username, Dialect.Operation.ACCOUNT_CREATE.toString() };
        e = new DatabaseException(code, arguments);
      }
      // MySql only says 01396 that is translated to the code tested below
      // we assume that means the account already exists
      else if (DatabaseError.OPERATION_FAILED.equals(code)) {
        final String[] arguments = { username, this.resource.name() };
        e = new DatabaseException(DatabaseError.OBJECT_ALREADY_EXISTS, arguments);
      }
      // Oracle give as a meaning full error
      else if (DatabaseError.OBJECT_ALREADY_EXISTS.equals(code)) {
        final String[] arguments = { username, this.resource.name() };
        e = new DatabaseException(code, arguments);
      }
      else if (DatabaseError.OBJECT_NOT_CREATED.equals(code)) {
        final String[] arguments = { username, this.resource.name() };
        e = new DatabaseException(code, arguments);
      }
      else
        e =new DatabaseException(code);

      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountDelete
  /**
   ** Drops a database account from the database.
   **
   ** @param  accountname        the name of the account to delete.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountDelete(final String accountname)
    throws TaskException {

    final String method = "accountDelete";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map <String, String> parameter = new HashMap<String, String>(1);
    parameter.put(Dialect.USERNAME, accountname);
    try {
      execute(Dialect.Operation.ACCOUNT_DELETE, parameter);
    }
    catch (DatabaseException e) {
      final String code = this.dialectErrorCode(e);
      if (DatabaseError.INSUFFICIENT_PRIVILEGE.equals(code)) {
        final String[] arguments = { accountname, Dialect.Operation.ACCOUNT_DELETE.toString() };
        e = new DatabaseException(code, arguments);
      }
      else if (DatabaseError.OBJECT_NOT_EXISTS.equals(code)) {
        final String[] arguments = { accountname, this.resource.name() };
        e = new DatabaseException(code, arguments);
      }
      else if (DatabaseError.OBJECT_NOT_DELETED.equals(code)) {
        final String[] arguments = { accountname, this.resource.name() };
        e = new DatabaseException(code, arguments);
      }
      else
        e =new DatabaseException(code);

      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountEnable
  /**
   ** Enables a previuosly disable database account.
   **
   ** @param  accountname        the name of the account to enable.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountEnable(final String accountname)
    throws TaskException {

    final String method = "accountEnable";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map <String, String> parameter = new HashMap<String, String>(1);
    parameter.put(Dialect.USERNAME, accountname);
    try {
      execute(Dialect.Operation.ACCOUNT_ENABLE, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String[] arguments = { accountname, this.resource.name() };
        e= new DatabaseException(this.dialectErrorCode(e), arguments);
      }
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountDisable
  /**
   ** Disables a previuosly enable database account.
   **
   ** @param  accountname        the name of the account to disable.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountDisable(final String accountname)
    throws TaskException {

    final String method = "accountDisable";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map <String, String> parameter = new HashMap<String, String>(1);
    parameter.put(Dialect.USERNAME, accountname);
    try {
      execute(Dialect.Operation.ACCOUNT_DISABLE, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String[] arguments = { accountname, this.resource.name() };
        e= new DatabaseException(this.dialectErrorCode(e), arguments);
      }
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountModify
  /**
   ** Changes the data of a database account.
   **
   ** @param  accountname        the name of the account to change.
   ** @param  attributeName      the name of the attribute to change.
   ** @param  attributeValue     the value of the attribute to change.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountModify(final String accountname, final String attributeName, final String attributeValue)
    throws TaskException {

    final String method = "accountModify";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.USERNAME,        accountname);
    parameter.put(Dialect.ATTRIBUTE_NAME,  attributeName);
    parameter.put(Dialect.ATTRIBUTE_VALUE, attributeValue);
    try {
      execute(Dialect.Operation.ACCOUNT_MODIFY, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String[] arguments = { accountname, this.resource.name() };
        e= new DatabaseException(this.dialectErrorCode(e), arguments);
      }
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPassword
  /**
   ** Changes the password of a database account.
   **
   ** @param  username        the name of the account to change.
   ** @param  password           the password to set.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountPassword(final String username, final String password)
    throws TaskException {

    final String method = "accountPassword";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.USERNAME, username);
    parameter.put(Dialect.PASSWORD, password);
    try {
      execute(Dialect.Operation.ACCOUNT_PASSWORD, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String[] arguments = { username, this.resource.name() };
        e= new DatabaseException(this.dialectErrorCode(e), arguments);
      }
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountGrantPrivilege
  /**
   ** Grants privilege to an account.
   **
   ** @param  username           the name of the account the privileg has to be
   **                            granted to.
   ** @param  permission         the name of the privileg to grant.
   ** @param  delegated          the permission should be granted with
   **                            delegated administration privileges.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountGrantPrivilege(final String username, final String permission, final boolean delegated)
    throws TaskException {

    final String method = "accountGrantPrivilege";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.USERNAME,   username);
    parameter.put(Dialect.PERMISSION, permission);
    try {
      permissionGrant(delegated ? Dialect.Operation.ACCOUNT_PRIVILEGE_GRANT_WITH : Dialect.Operation.ACCOUNT_PRIVILEGE_GRANT, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRevokePrivilege
  /**
   ** Revokes a privilege from an account.
   **
   ** @param  username           the name of the account the privilege has to be
   **                            revoked from.
   ** @param  permission         the name of the privilege to revoke.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountRevokePrivilege(final String username, final String permission)
    throws TaskException {

    final String method = "accountRevokePrivilege";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.USERNAME,   username);
    parameter.put(Dialect.PERMISSION, permission);
    try {
      permissionRevoke(Dialect.Operation.ACCOUNT_PRIVILEGE_REVOKE, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountGrantRole
  /**
   ** Grants a role to an account.
   **
   ** @param  username           the name of the account the role has to be
   **                            granted to.
   ** @param  permission         the name of the role to grant.
   ** @param  delegated          the permission should be granted with delegated
   **                            administrator privileges.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountGrantRole(final String username, final String permission, final boolean delegated)
    throws TaskException {

    final String method = "accountGrantRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.USERNAME,   username);
    parameter.put(Dialect.PERMISSION, permission);
    try {
      permissionGrant(delegated ? Dialect.Operation.ACCOUNT_ROLE_GRANT_WITH : Dialect.Operation.ACCOUNT_ROLE_GRANT, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRevokeRole
  /**
   ** Revokes a role from an account.
   **
   ** @param  username           the name of the account the role has to be
   **                            revoked from.
   ** @param  permission         the name of the role to revoke.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountRevokeRole(final String username, final String permission)
    throws TaskException {

    final String method = "accountRevokeRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.USERNAME,   username);
    parameter.put(Dialect.PERMISSION, permission);
    try {
      permissionRevoke(Dialect.Operation.ACCOUNT_ROLE_REVOKE, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountGrantObject
  /**
   ** Grants permissions on an object to an account.
   **
   ** @param  username           the name of the account the permission has to
   **                            be granted to.
   ** @param  permission         the permission to grant.
   **                            A permission can be a SELECT, DELETE etc.
   ** @param  object             the name of the object on which the permission
   **                            should be granted.
   **                            An object can be a name of a table, view etc.
   ** @param  delegated          the permission should be granted with delegated
   **                            administrator privileges.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountGrantObject(final String username, final String permission, final String object, final boolean delegated)
    throws TaskException {

    final String method = "accountGrantObject";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(3);
    parameter.put(Dialect.USERNAME,   username);
    parameter.put(Dialect.PERMISSION, permission);
    parameter.put(Dialect.OBJECT,     object);
    try {
      permissionGrant(delegated ? Dialect.Operation.ACCOUNT_OBJECT_GRANT_WITH : Dialect.Operation.ACCOUNT_OBJECT_GRANT, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRevokeObject
  /**
   ** Revokes permissions on an object from an account.
   **
   ** @param  username           the name of the account the permission has to
   **                            be revoked from.
   ** @param  permission         the permission to revoke.
   **                            A permission can be a SELECT, DELETE etc.
   ** @param  object             the name of the object from which the
   **                            permission should be revoked.
   **                            An object can be a name of a table, view etc.
   **
   ** @throws TaskException      if the operation fails
   */
  public void accountRevokeObject(final String username, final String permission, final String object)
    throws TaskException {

    final String method = "accountRevokeObject";
    trace(method, SystemMessage.METHOD_ENTRY);
    Map <String, String> parameter = new HashMap<String, String>(3);
    parameter.put(Dialect.USERNAME,   username);
    parameter.put(Dialect.PERMISSION, permission);
    parameter.put(Dialect.OBJECT,     object);
    try {
      objectRevoke(Dialect.Operation.ACCOUNT_OBJECT_REVOKE, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleCreate
  /**
   ** Creates a database role with the specified password.
   **
   ** @param  rolename           the name of the role to create.
   ** @param  password           the password to set.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void roleCreate(final String rolename, final String password)
    throws TaskException {

    final String method = "roleCreate";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.ROLENAME, rolename);
    parameter.put(Dialect.PASSWORD, password);
    try {
      execute(StringUtility.isEmpty(password) ? Dialect.Operation.ROLE_CREATE : Dialect.Operation.ROLE_CREATE_PROTECTED, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String[] arguments = { rolename, this.resource.name() };
        e= new DatabaseException(this.dialectErrorCode(e), arguments);
      }
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleDelete
  /**
   ** Drops a database role.
   **
   ** @param  rolename           the name of the role to delete.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void roleDelete(final String rolename)
    throws TaskException {

    final String method = "roleDelete";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map <String, String> parameter = new HashMap<String, String>(1);
    parameter.put(Dialect.ROLENAME, rolename);
    try {
      execute(Dialect.Operation.ROLE_DELETE, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String[] arguments = { rolename, this.resource.name() };
        e= new DatabaseException(this.dialectErrorCode(e), arguments);
      }
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rolePassword
  /**
   ** Creates a database user with the specified password.
   **
   ** @param  rolename           the name of the role to change.
   ** @param  password           the password to set.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void rolePassword(final String rolename, final String password)
    throws TaskException {

    final String method = "rolePassword";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.ROLENAME, rolename);
    parameter.put(Dialect.PASSWORD, password);
    try {
      execute(StringUtility.isEmpty(password) ? Dialect.Operation.ROLE_PROTECT : Dialect.Operation.ROLE_UNPROTECTED, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String[] arguments = { rolename, this.resource.name() };
        e= new DatabaseException(this.dialectErrorCode(e), arguments);
      }
      throw e;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleGrantPrivilege
  /**
   ** Grants privilege to an role.
   **
   ** @param  rolename           the name of the role the privileg has to be
   **                            granted to.
   ** @param  permission         the name of the privileg to grant.
   ** @param  administrator      the permission should be granted with
   **                            administrator privileges.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void roleGrantPrivilege(final String rolename, final String permission, final boolean administrator)
    throws TaskException {

    final String method = "roleGrantPrivilege";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.ROLENAME,   rolename);
    parameter.put(Dialect.PERMISSION, permission);
    try {
      permissionGrant(administrator ? Dialect.Operation.ROLE_PRIVILEGE_GRANT_WITH : Dialect.Operation.ROLE_PRIVILEGE_GRANT, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleRevokePrivilege
  /**
   ** Revokes a privilege from an role.
   **
   ** @param  rolename           the name of the role the privilege has to be
   **                            revoked from.
   ** @param  permission         the name of the privilege to revoke.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void roleRevokePrivilege(final String rolename, final String permission)
    throws TaskException {

    final String method = "roleRevokePrivilege";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.ROLENAME,   rolename);
    parameter.put(Dialect.PERMISSION, permission);
    try {
      permissionRevoke(Dialect.Operation.ROLE_PRIVILEGE_REVOKE, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleGrantRole
  /**
   ** Grants a role to an role.
   **
   ** @param  rolename           the name of the role the role has to be
   **                            granted to.
   ** @param  permission         the name of the role to grant.
   ** @param  administrator      the permission should be granted with
   **                            administrator privileges.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void roleGrantRole(final String rolename, final String permission, final boolean administrator)
    throws TaskException {

    final String method = "roleGrantRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.ROLENAME,   rolename);
    parameter.put(Dialect.PERMISSION, permission);
    try {
      permissionGrant(administrator ? Dialect.Operation.ROLE_ROLE_GRANT_WITH : Dialect.Operation.ROLE_ROLE_GRANT, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleRevokeRole
  /**
   ** Revokes a role from an role.
   **
   ** @param  rolename           the name of the role the role has to be
   **                            revoked from.
   ** @param  permission         the name of the role to revoke.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void roleRevokeRole(final String rolename, final String permission)
    throws TaskException {

    final String method = "roleRevokeRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(2);
    parameter.put(Dialect.ROLENAME,   rolename);
    parameter.put(Dialect.PERMISSION, permission);
    try {
      permissionRevoke(Dialect.Operation.ROLE_ROLE_REVOKE, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleGrantObject
  /**
   ** Grants permissions on an object to a role.
   **
   ** @param  rolename           the name of the role the permissions has to be
   **                            granted to.
   ** @param  permission         the permission to grant.
   **                            A permission can be a SELECT, DELETE etc.
   ** @param  object             the name of the object on which the permission
   **                            should be granted.
   **                            An object can be a name of a table, view etc.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void roleGrantObject(final String rolename, final String permission, final String object)
    throws TaskException {

    final String method = "roleGrantObject";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map <String, String> parameter = new HashMap<String, String>(3);
    parameter.put(Dialect.ROLENAME,   rolename);
    parameter.put(Dialect.PERMISSION, permission);
    parameter.put(Dialect.OBJECT,     object);
    try {
      objectGrant(Dialect.Operation.ROLE_OBJECT_GRANT, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleRevokeObject
  /**
   ** Revokes permissions on an object from a role.
   **
   ** @param  rolename           the name of the role the permission has to be
   **                            revoked from.
   ** @param  permission         the permission to revoke.
   **                            A permission can be a SELECT, DELETE etc.
   ** @param  object             the name of the object from which the
   **                            permission should be revoked.
   **                            An object can be a name of a table, view etc.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void roleRevokeObject(final String rolename, final String permission, final String object)
    throws TaskException {

    final String method = "roleRevokeObject";
    trace(method, SystemMessage.METHOD_ENTRY);
    Map <String, String> parameter = new HashMap<String, String>(3);
    parameter.put(Dialect.ROLENAME,   rolename);
    parameter.put(Dialect.PERMISSION, permission);
    parameter.put(Dialect.OBJECT,     object);
    try {
      objectRevoke(Dialect.Operation.ROLE_OBJECT_REVOKE, parameter);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Performs changes on a specific user profile.
   **
   ** @param  dictionary         the SQL Query statement to execute as the
   **                            template.
   ** @param  parameter          the substitutions for the placeholder values
   **                            contained in the query template.
   **                            All values has to be escaped if the provided
   **                            string will violate normal character
   **                            convensions of the Database Service.
   **
   ** @throws TaskException      if the operation fails
   */
  protected void execute(final Dialect.Operation dictionary, final Map<String, String> parameter)
    throws TaskException {

    // prevent bogus state of instance
    if (parameter == null || parameter.isEmpty())
      throw new DatabaseException(DatabaseError.INSUFFICIENT_INFORMATION, "parameter");

    // prevent bogus state of instance
    if (dictionary == null)
      throw TaskException.argumentIsNull("dictionary");

    // prevent bogus state of instance
    String template = dialectOperation(dictionary);
    if (StringUtility.isEmpty(template))
      throw new DatabaseException(DatabaseError.OPERATION_NOT_SUPPORTED, dictionary.toString());

    execute(parseTemplate(template, STRING_PATTERN, Pattern.MULTILINE, parameter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select
  /**
   ** Reads all values from the data dictionary.
   **
   ** @param  catalog            the {@link Catalog} defining the query
   **                            properties for the database to retrieve desired
   **                            information.
   **
   ** @return                    a {@link Set} containing the available values
   **                            for the specified {@link Catalog}.
   **                            Each entry in the returned list is a
   **                            {@link String} with the aggregated value of
   **                            schema and name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected final Set<String> select(final Catalog catalog)
    throws TaskException {

    // perform an catalog search without an exclusion only if applicable
    return (catalog == null) ? null : select(catalog, catalog.filter(), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select
  /**
   ** Reads all values from the data dictionary.
   **
   ** @param  catalog            the {@link Catalog} defining the query
   **                            properties for the database to retrieve desired
   **                            information.
   ** @param  exclusions         the {@link Set} with the values which should
   **                            not be contained in the returning {@link Set}.
   **
   ** @return                    a {@link Set} containing the available values
   **                            for the specified {@link Catalog}.
   **                            Each entry in the {@link Set} is a
   **                            {@link String} with the value.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected final Set<String> select(final Catalog catalog, final Set<String> exclusions)
    throws TaskException {

    // perform an entity search without a filter
    return select(catalog, null, exclusions);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select
  /**
   ** Reads all values from the data dictionary the belongs to the specified
   ** {@link DatabaseEntity}.
   **
   ** @param  entity             the {@link DatabaseEntity} defining the query
   **                            properties for the database to retrieve desired
   **                            information.
   ** @param  exclusions         the {@link Set} with the values which should
   **                            not be contained in the returning {@link Set}.
   **
   ** @return                    a {@link Set} containing the available values
   **                            for the specified query string.
   **                            Each entry in the returned list is a
   **                            {@link String} with the name of the value.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected final Set<String> select(final DatabaseEntity entity, final Set<String> exclusions)
    throws TaskException {

    return select(entity, null, exclusions);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select
  /**
   ** Reads all values from the data dictionary the belongs to the specified
   ** {@link DatabaseEntity}.
   **
   ** @param  entity             the {@link DatabaseEntity} defining the query
   **                            properties for the database to retrieve desired
   **                            information.
   ** @param  filter             the filter expression to use for the search.
   ** @param  exclusions         the {@link Set} with the values which should
   **                            not be contained in the returning {@link Set}.
   **
   ** @return                    a {@link Set} containing the available values
   **                            for the specified query string.
   **                            Each entry in the returned list is a
   **                            {@link String} with the name of the value.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected final Set<String> select(final DatabaseEntity entity, final DatabaseFilter filter, final Set<String> exclusions)
    throws TaskException {

    Set<String> result = null;
    if (entity == null)
      return result;

    // prevent bogus state of instance
    if (this.connection == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    final List<Pair<String, String>> returning = entity.returning();
    final DatabaseSelect             statement = DatabaseSelect.build(this, entity, filter, returning);
    try {
      final List<Map<String, Object>> entries   = statement.execute(this.connection);
      result = new HashSet<String>(entries.size());
      for (Map<String, Object> entry : entries) {
        // all this crap below with trim() etc. we have to implement due to the
        // stupid behavior of an IBM database. Schema names are CHAR were the
        // object names itself are VARCHAR. Following ANSI, CHAR has to be
        // returned right padded with spaces thus we have to remove it.
        // (Which blind person is designing such things, realy IBM has no clue
        // what a database should be).
        String value = ((String)entry.remove(returning.get(0).value)).trim();
        for (int i = 1; i < returning.size(); i++)
          value = value.concat(".").concat(((String)entry.get(returning.get(i).value)).trim());

        result.add(value);
      }
      if (exclusions != null)
        result.removeAll(exclusions);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String code = this.dialectErrorCode(e);
        if (code == null) {
          e = new DatabaseException(e.getCause());
        }
        else if (DatabaseError.OBJECT_NOT_EXISTS.equals(code)) {
          final String[] arguments = { entity.type(), this.resource.name() };
          e = new DatabaseException(code, arguments);
        }
        else
          e =new DatabaseException(code);
      }
      throw e;
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grantPermission
  /**
   ** Grants the passed privilege (role or permission) to a subject; either an
   ** account or a role.
   **
   ** @param  template           the SQL Query statement to execute as the
   **                            template.
   ** @param  parameter          the mapping of the account or role and the
   **                            permissions to grant.
   **
   ** @throws TaskException      if the operation fails
   */
  private void permissionGrant(final Dialect.Operation template, final Map<String, String> parameter)
    throws TaskException {

    try {
      execute(template, parameter);
    }
    catch (DatabaseException e) {
      final String code = this.dialectErrorCode(e);
      if (e.getCause() instanceof SQLException) {
        if (DatabaseError.INSUFFICIENT_PRIVILEGE.equals(code)) {
          final String[] arguments = { this.resource.principalName(), template.toString() };
          e = new DatabaseException(code, arguments);
        }
        else if (DatabaseError.OBJECT_NOT_EXISTS.equals(code)) {
          final String[] arguments = { parameter.toString(), this.resource.name() };
          e = new DatabaseException(code, arguments);
        }
        else if (DatabaseError.PERMISSION_NOT_ASSIGNED.equals(code)) {
          e = new DatabaseException(code, parameter.toString());
        }
        else
          e =new DatabaseException(code);
      }
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permissionRevoke
  /**
   ** Revokes a permission from a subject; either an account or a role.
   **
   ** @param  template           the SQL Query statement to execute as the
   **                            template.
   ** @param  parameter          the mapping of the account or role and the
   **                            permission to be revoked from.
   **
   ** @throws TaskException      if the operation fails
   */
  private void permissionRevoke(final Dialect.Operation template, final Map <String, String> parameter)
    throws TaskException {

    try {
      execute(template, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String code = this.dialectErrorCode(e);
        if (DatabaseError.INSUFFICIENT_PRIVILEGE.equals(code))
          e = new DatabaseException(code, parameter.toString());
        else if (DatabaseError.OBJECT_NOT_EXISTS.equals(code)) {
          final String[] arguments = { parameter.toString(), this.resource.name() };
          e = new DatabaseException(code, arguments);
        }
        else if (DatabaseError.PERMISSION_NOT_REMOVED.equals(code)) {
          e = new DatabaseException(code, parameter.toString());
        }
        else
          e =new DatabaseException(code);
      }
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permissionGrant
  /**
   ** Grants the specified permission (select, delete, etc) on the specified
   ** object (table, view, etc.) to the specified subject; either an account or
   ** a role.
   **
   ** @param  template           the SQL Query statement to execute as the
   **                            template.
   ** @param  parameter          the mapping of the account or role and the
   **                            object permissions to grant.
   **
   ** @throws TaskException      if the operation fails
   */
  private void objectGrant(final Dialect.Operation template, Map <String, String> parameter)
    throws TaskException {

    try {
      execute(template, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String code = this.dialectErrorCode(e);
        if (DatabaseError.INSUFFICIENT_PRIVILEGE.equals(code)) {
          final String[] arguments = { parameter.toString(), template.toString() };
          e = new DatabaseException(code, arguments);
        }
        else if (DatabaseError.OBJECT_NOT_EXISTS.equals(code)) {
          final String[] arguments = { parameter.toString(), this.resource.name() };
          e = new DatabaseException(code, arguments);
        }
        else if (DatabaseError.PERMISSION_NOT_ASSIGNED.equals(code)) {
          e = new DatabaseException(code, parameter.toString());
        }
        else
          e =new DatabaseException(code);
      }
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectRevoke
  /**
   ** Revokes the specified permission (select, delete, etc) on the specified
   ** object (table, view, etc.) from the specified subject; either an account
   ** or a role.
   **
   ** @param  template           the SQL Query statement to execute as the
   **                            template.
   ** @param  parameter          the mapping of the account or role and the
   **                            object permissions to revoke.
   **
   ** @throws TaskException      if the operation fails
   */
  private void objectRevoke(final Dialect.Operation template, final Map <String, String> parameter)
    throws TaskException {

    try {
      execute(template, parameter);
    }
    catch (DatabaseException e) {
      if (e.getCause() instanceof SQLException) {
        final String code = this.dialectErrorCode(e);
        if (DatabaseError.INSUFFICIENT_PRIVILEGE.equals(code)) {
          final String[] arguments = { parameter.toString(), template.toString() };
          e = new DatabaseException(code, arguments);
        }
        else if (DatabaseError.OBJECT_NOT_EXISTS.equals(code)) {
          final String[] arguments = { parameter.toString(), this.resource.name() };
          e = new DatabaseException(code, arguments);
        }
        else if (DatabaseError.PERMISSION_NOT_REMOVED.equals(code)) {
          final String[] arguments = { parameter.toString(), this.resource.name() };
          e = new DatabaseException(code, arguments);
        }
        else
          e =new DatabaseException(code);
      }
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Performs changes on a specific user profile.
   **
   ** @param  operation          the SQL statement to execute.
   **
   ** @throws TaskException      if the operation fails
   */
  private void execute(final String operation)
    throws TaskException {

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    // prevent bogus state of instance
    if (this.connection == null) {
      DatabaseException e = new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");
      error(method, e.getLocalizedMessage());
      trace(method, SystemMessage.METHOD_EXIT);
      throw e;
    }

    // prevent bogus state of instance
    if (StringUtility.isEmpty(operation)) {
      TaskException e = TaskException.argumentIsNull("operation");
      error(method, e.getLocalizedMessage());
      trace(method, SystemMessage.METHOD_EXIT);
      throw e;
    }

    Statement statement = null;
    try {
      statement = DatabaseStatement.createStatement(this.connection);
      statement.execute(operation);
    }
    catch (SQLException e) {
      error(method, e.getLocalizedMessage());
      // produce an exception that take as the code the vendor specific error
      // code. The prefix of the error code of the exception created here will
      // be SQL to separate it form our own error prefix.
      throw new DatabaseException(e);
    }
    finally {
      DatabaseStatement.closeStatement(statement);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseValueTemplate
  /**
   ** Returns a string with all placeholders substituted by the corresponding
   ** value of the provided map.
   ** <p>
   ** A placeholder in generell is specified by
   ** <code>${<i>placeholder name</i>}</code> or
   ** <code>$[<i>placeholder name</i>]</code>.
   ** <p>
   ** The pattern <code>$[<i>placeholder name</i>]</code> request to subtitute
   ** the expression with directly with the correspondedning value taken from
   ** <code>mapping</code>.
   **
   ** @param  subject            the string containing placeholders.
   ** @param  expression         the regular expression to match the
   **                            placeholders within the subject.
   ** @param  options            match flags, a bit mask that may include
   **                            {@link Pattern#CASE_INSENSITIVE},
   **                            {@link Pattern#MULTILINE},
   **                            {@link Pattern#DOTALL},
   **                            {@link Pattern#UNICODE_CASE},
   **                            {@link Pattern#CANON_EQ},
   **                            {@link Pattern#UNIX_LINES},
   **                            {@link Pattern#LITERAL} and
   **                            {@link Pattern#COMMENTS}.
   ** @param  mapping            the {@link Map} with the subtitution of the
   **                            expressions.
   **
   ** @return                    a string with all placeholders substituted by
   **                            the corresponding value of the provided
   **                            mapping.
   */
  private String parseTemplate(final String subject, final String expression, final int options, final Map<String, String> mapping) {
    final String method = "parseTemplate";
    trace(method, SystemMessage.METHOD_ENTRY);

    // required by the dependency of Matcher on leveraging a StringBuffer as a
    // target we cannot switch to a StringBuilder instance to improve
    // performance
    StringBuffer result = new StringBuffer(subject.length());
    try {
      // If you will be using a particular regular expression often, should
      // create a Pattern object to store the regular expression.
      // You can then reuse the regex as often as you want by reusing the
      // Pattern object.
      Pattern pattern = Pattern.compile(expression, options);

      // To use the regular expression on a string, create a Matcher object by
      // calling pattern.matcher() passing the subject string to it. The Matcher
      // will do the actual searching, replacing or splitting.
      Matcher match = pattern.matcher(subject);
      while (match.find()) {
        if (match.groupCount() > 0) {
          // Capturing parentheses are numbered 1..groupCount() group number
          // zero is the entire regex match 1 is the value that we would like to
          // substitute
          final String name = match.group(1);
          if (!StringUtility.isEmpty(name)) {
            // check if placeholder is well known and and a key entry with a
            // legal value is existing
            if (mapping.containsKey(name)) {
              final String value = mapping.get(name);
              if (!StringUtility.isEmpty(value))
                match.appendReplacement(result, value);
            }
          }
        }
      }
      match.appendTail(result);
    }
    catch (PatternSyntaxException e) {
      // parameter expression does not contain a valid regular expression
      error(method, DatabaseBundle.format(AdministrationError.EXPRESSION_INVALID, expression, e.getDescription()));
    }
    catch (IllegalArgumentException ex) {
      // this exception indicates a bug in parameter options
      error(method, DatabaseBundle.string(AdministrationError.EXPRESSION_BITVALUES));
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return result.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installDialect
  /**
   ** We lazily install the appropriate dialect.
   ** <p>
   ** This function does the loading.
   **
   ** @throws TaskException      if the requested <code>Dialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the configured
   **                            <code>driverClass</code>.
   */
  private synchronized void installDialect()
    throws TaskException {

    if (this.dialect != null)
      return;

    this.dialect = Dialect.create(this.feature().databaseDriverClass());
  }
}