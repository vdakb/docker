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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Google API Gateway

    File        :   Provider.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Provider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.persistence;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.sql.Types;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import oracle.sql.TIMESTAMP;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.NotFoundException;
import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.ServerErrorException;
import oracle.iam.system.simulation.ResourceConflictException;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.rest.domain.ErrorResponse;

import oracle.iam.system.simulation.apigee.schema.User;
import oracle.iam.system.simulation.apigee.schema.Tenant;
import oracle.iam.system.simulation.apigee.schema.Company;
import oracle.iam.system.simulation.apigee.schema.Product;
import oracle.iam.system.simulation.apigee.schema.Property;
import oracle.iam.system.simulation.apigee.schema.UserRole;
import oracle.iam.system.simulation.apigee.schema.Developer;
import oracle.iam.system.simulation.apigee.schema.RoleResult;
import oracle.iam.system.simulation.apigee.schema.UserResult;
import oracle.iam.system.simulation.apigee.schema.Application;
import oracle.iam.system.simulation.apigee.schema.TenantResult;
import oracle.iam.system.simulation.apigee.schema.MemberResult;
import oracle.iam.system.simulation.apigee.schema.ProductResult;
import oracle.iam.system.simulation.apigee.schema.CompanyResult;
import oracle.iam.system.simulation.apigee.schema.DeveloperResult;
import oracle.iam.system.simulation.apigee.schema.ApplicationResult;

import oracle.iam.system.simulation.dbs.DatabaseError;
import oracle.iam.system.simulation.dbs.DatabaseFilter;
import oracle.iam.system.simulation.dbs.DatabaseSelect;
import oracle.iam.system.simulation.dbs.DatabaseDelete;
import oracle.iam.system.simulation.dbs.DatabaseInsert;
import oracle.iam.system.simulation.dbs.DatabaseUpdate;
import oracle.iam.system.simulation.dbs.DatabaseResource;
import oracle.iam.system.simulation.dbs.DatabaseStatement;
import oracle.iam.system.simulation.dbs.DatabaseParameter;
import oracle.iam.system.simulation.dbs.DatabaseException;
import oracle.iam.system.simulation.dbs.DatabaseConnection;

////////////////////////////////////////////////////////////////////////////////
// class Provider
// ~~~~~ ~~~~~~~~
/**
 ** The persistence layer to access the database schmema
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Provider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the one and only instance of the <code>Provider</code>
   ** <p>
   ** Singleton Pattern
   **
   ** Yes I know it should never be public but I'm lazy
   */
  public static final Provider instance   = new Provider();

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  Connection                   connection = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Provider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Attempts to establish a connection to the database by building the
   ** connection properties leveraging the provided parameter.
   **
   ** @param  flavor             the database flavor s one of:
   **                            <ul>
   **                              <li>orcl
   **                              <li>psql
   **                              <li>mysql
   **                            </ul>
   **                            Allowed object is {@link String}.
   ** @param  resource           the resource descriptor providing the
   **                            connection properties
   **                            <br>
   **                            Allowed object is {@link DatabaseResource}.
   **
   ** @throws DatabaseException  if a database access error occurs or the url is
   **                            <code>null</code>.
   */
  public void aquire(final String flavor, final DatabaseResource resource)
    throws DatabaseException {

    if (this.connection == null) {
      this.connection = DatabaseConnection.aquire(flavor, resource);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release the obtained JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   */
  public void release() {
    DatabaseConnection.release(this.connection);
    this.connection = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   opened
  /**
   ** Ensures that ths JDBC connection is valid in terms of:
   ** <ol>
   **   <li><code>connection</code> is not <code>null</code>.
   **   <li><code>connection</code> is not closed.
   ** </ol>
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @return                    <code>true</code> if the given
   **                            {@link Connection} is non-<code>null</code> and
   **                            not closed; otherwise it returns
   **                            <code>false</code>.
   */
  public final boolean opened() {
    return DatabaseConnection.opened(this.connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Commit any transaction.
   **
   ** @throws DatabaseException  if connection cannot be commited.
   */
  public final void commit()
    throws DatabaseException {

    DatabaseConnection.commitConnection(this.connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rollback
  /**
   ** Rollback a transaction in supplied connection
   **
   ** @throws DatabaseException  if connection cannot be rollback.
   */
  public final void rollback()
    throws DatabaseException {

    DatabaseConnection.rollbackConnection(this.connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparedStatement
  /**
   ** Creates a new {@link PreparedStatement} for sending parameterized SQL
   ** statements to the database.
   ** <p>
   ** A SQL statement with or without IN parameters can be pre-compiled and
   ** stored in a {@link PreparedStatement} object. This object can then be used
   ** to efficiently execute this statement multiple times.
   ** <p>
   ** Result sets created using the returned {@link PreparedStatement} object
   ** will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of the
   ** created result sets can be determined by calling
   ** <code>getHoldability()</code>.
   **
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new default {@link PreparedStatement} object
   **                            containing the pre-compiled SQL statement.
   **                            <br>
   **                            Possible object is {@link PreparedStatement}.
   **
   ** @throws DatabaseException  if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public final PreparedStatement preparedStatement(final String string)
    throws DatabaseException {

    // prevent bogus state
    if (this.connection == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    return DatabaseStatement.createPreparedStatement(this.connection, string);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparedStatement
  /**
   ** Creates a {@link PreparedStatement} object that will generate
   ** <code>ResultSet</code> objects with the given <code>type</code> and
   ** <code>concurrency</code>. This method is the same as the prepareStatement
   ** method above, but it allows the default result set type and concurrency to
   ** be overridden. The holdability of the created result sets can be
   ** determined by calling getHoldability().
   ** <p>
   ** A SQL statement with or without IN parameters can be pre-compiled and
   ** stored in a {@link PreparedStatement} object. This object can then be used
   ** to efficiently execute this statement multiple times.
   **
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               a result set type; one of:
   **                            <ul>
   **                              <li>TYPE_FORWARD_ONLY
   **                              <li>TYPE_SCROLL_INSENSITIVE
   **                              <li>TYPE_SCROLL_SENSITIVE
   **                            </ul>
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  concurrency        a concurrency type; one of:
   **                            <ul>
   **                              <li>CONCUR_READ_ONLY
   **                              <li>CONCUR_UPDATABLE
   **                            </ul>
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a new {@link PreparedStatement} object
   **                            containing the pre-compiled SQL statement that
   **                            will produce <code>ResultSet</code> objects
   **                            with the given <code>type</code> and
   **                            <code>concurrency</code>.
   **                            <br>
   **                            Possible object is {@link PreparedStatement}.
   **
   ** @throws DatabaseException  if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public final PreparedStatement preparedStatement(final String string, int type, int concurrency)
    throws DatabaseException {

    // prevent bogus state
    if (this.connection == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "connection");

    return DatabaseStatement.createPreparedStatement(this.connection, string, type, concurrency);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountSearch
  /**
   ** Search account.
   **
   ** @return                    the result set as a {@link UserResult}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public UserResult accountSearch()
    throws ProcessingException {

    final List<UserResult.Entry> batch  = new ArrayList<UserResult.Entry>();
    final DatabaseSelect         search = DatabaseSelect.build(Account.ENTITY, DatabaseFilter.NOP, returning(Account.MAIL));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add(new UserResult.Entry().name((String)cursor.value));
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return new UserResult().list(batch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountExists
  /**
   ** Lookup an account by the specified public system identifier
   ** <code>id</code>.
   **
   ** @param  identifier         the public system identifier to lookup the
   **                            resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the user resource mapped
   **                            at either <code>id</code> or
   **                            <code>username</code> (determined by
   **                            <code>primary</code>).
   **                            <br>
   **                            Possible  object is <code>boolean</code>.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public boolean accountExists(final String identifier)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(Account.MAIL, identifier, DatabaseFilter.Operator.EQ);
    final DatabaseSelect lookup = DatabaseSelect.build(Account.ENTITY, filter, returning(Entity.VERSION));
    try {
      final List<List<Pair<String, Object>>> result = lookup.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        return (result.size() == 1);
      }
      return false;
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountLookup
  /**
   ** Lookup an account by the specified public system identifier
   ** <code>identifier</code>.
   **
   ** @param  identifier         the system identifier to lookup the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link User} mapped at
   **                            <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public User accountLookup(final String identifier)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(Account.MAIL, identifier, DatabaseFilter.Operator.EQ);
    final DatabaseSelect lookup = DatabaseSelect.build(Account.ENTITY, filter, returning(Account.MAIL, Account.LASTNAME, Account.FIRSTNAME));
    try {
      final List<List<Pair<String, Object>>> result = lookup.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        else if (result.size() > 0) {
          // single hit
          final User                       batch = new User();
          final List<Pair<String, Object>> tupel = result.get(0);
          for (Pair<String, Object> cursor : tupel) {
            switch (cursor.tag) {
              case Account.MAIL      : batch.emailId((String)cursor.value);
                                       break;
              case Account.LASTNAME  : batch.lastName((String)cursor.value);
                                       break;
              case Account.FIRSTNAME : batch.firstName((String)cursor.value);
                                       break;
            }
          }
          return batch;
        }
        else {
          // nothing found
          return null;
        }
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      lookup.close();
    }
    // nothing found
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCreate
  /**
   ** Create an account in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  resource           the {@link User} mapped at
   **                            <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  public void accountCreate(final User resource)
    throws ProcessingException {

    if (accountExists(resource.emailId()))
      throw ResourceConflictException.uniqueness(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_EXISTS, resource.emailId(), Account.RESOURCE));

    final DatabaseInsert insert  = DatabaseInsert.build(Account.ENTITY, transform(resource));
    try {
      insert.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountModify
  /**
   ** Updates an account in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  id                 the system identifier to delete the
   **                            resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link User} mapped at
   **                            <code>id</code> to update.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @throws ProcessingException if the account cannot be modified.
   */
  public void accountModify(final String id, final User resource)
    throws ProcessingException {

    if (!accountExists(resource.emailId()))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, resource.emailId(), Account.RESOURCE));

    final DatabaseUpdate update  = DatabaseUpdate.build(Account.ENTITY, DatabaseFilter.build(Account.MAIL, id, DatabaseFilter.Operator.EQ), transform(resource));
    try {
      update.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountDelete
  /**
   ** Deletes an account in the database.
   **
   ** @param  id                 the identifier of the resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ProcessingException if the account cannot be deleted.
   */
  public void accountDelete(final String id)
    throws ProcessingException {

    final DatabaseDelete delete = DatabaseDelete.build(Account.ENTITY, DatabaseFilter.build(Account.MAIL, id, DatabaseFilter.Operator.EQ));
    try {
      delete.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupSearch
  /**
   ** Search userrole.
   **
   ** @return                    the result set as a {@link TenantResult}.
   **                            <br>
   **                            Possible object is {@link TenantResult}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public List<String> groupSearch()
    throws ProcessingException {

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseSelect search = DatabaseSelect.build(UserGroup.ENTITY, DatabaseFilter.NOP, returning(Entity.NAME));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return batch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationSearch
  /**
   ** Search organizations.
   **
   ** @return                    the result set as a {@link TenantResult}.
   **                            <br>
   **                            Possible object is {@link TenantResult}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public TenantResult organizationSearch()
    throws ProcessingException {

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseSelect search = DatabaseSelect.build(Organization.ENTITY, DatabaseFilter.NOP, returning(Entity.NAME));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return new TenantResult().list(batch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationExists
  /**
   ** Lookup an organization by the specified public unique
   ** <code>identifier</code>.
   **
   ** @param  identifier         the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the user resource mapped
   **                            at either <code>id</code> or
   **                            <code>username</code> (determined by
   **                            <code>primary</code>).
   **                            <br>
   **                            Possible  object is <code>boolean</code>.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public boolean organizationExists(final String identifier)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(Organization.NAME, identifier, DatabaseFilter.Operator.EQ);
    final DatabaseSelect lookup = DatabaseSelect.build(Organization.ENTITY, filter, returning(Organization.NAME));
    try {
      final List<List<Pair<String, Object>>> result = lookup.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        return (result.size() == 1);
      }
      return false;
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationLookup
  /**
   ** Lookup an organization by the specified public unique
   ** <code>identifier</code>.
   **
   ** @param  identifier         the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link User} mapped at
   **                            <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public Tenant organizationLookup(final String identifier)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(Organization.NAME, identifier, DatabaseFilter.Operator.EQ);
    final DatabaseSelect lookup = DatabaseSelect.build(Organization.ENTITY, filter, returning(Organization.TYPE, Organization.NAME, Organization.DISPLAYNAME, Organization.CREATEDON, Organization.CREATEDBY, Organization.UPDATEDON, Organization.UPDATEDBY));
    try {
      final List<List<Pair<String, Object>>> result = lookup.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        else if (result.size() > 0) {
          // single hit
          final Tenant                     batch = new Tenant();
          final List<Pair<String, Object>> tupel = result.get(0);
          for (Pair<String, Object> cursor : tupel) {
            switch (cursor.tag) {
              case Organization.TYPE        : batch.type((String)cursor.value);
                                              break;
              case Organization.NAME        : batch.name((String)cursor.value);
                                              break;
              case Organization.DISPLAYNAME : batch.displayName((String)cursor.value);
                                              break;
              case Organization.CREATEDON   : batch.createdAt(timestamp(cursor.value));
                                              break;
              case Organization.CREATEDBY   : batch.createdBy((String)cursor.value);
                                              break;
              case Organization.UPDATEDON   : batch.modifiedAt(timestamp(cursor.value));
                                              break;
              case Organization.UPDATEDBY   : batch.modifiedBy((String)cursor.value);
                                              break;
            }
          }
          return organizationEnvironment(batch);
        }
        else {
          // nothing found
          return null;
        }
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      lookup.close();
    }
    // nothing found
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationCreate
  /**
   ** Create an organization in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  resource           the {@link Tenant} to create.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the {@link Tenant} created.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  public Tenant organizationCreate(final Tenant resource)
    throws ProcessingException {

    if (organizationExists(resource.name()))
      throw ResourceConflictException.uniqueness(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_EXISTS, resource.name(), Organization.RESOURCE));

    final DatabaseInsert insert  = DatabaseInsert.build(
      Organization.ENTITY
    , transform(resource)
    , CollectionUtility.set(
        Pair.of("created_by", Types.VARCHAR)
      , Pair.of("created_on", Types.TIMESTAMP)
      , Pair.of("updated_by", Types.VARCHAR)
      , Pair.of("updated_on", Types.TIMESTAMP)
      )
    );
    try {
      final Map<String, Object> result = insert.execute(this.connection);
      resource.createdBy((String)result.get("created_by"));
      resource.createdAt(Long.valueOf(((Date)result.get("created_on")).getTime()));
      resource.modifiedBy((String)result.get("updated_by"));
      resource.modifiedAt(Long.valueOf(((Date)result.get("updated_on")).getTime()));
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    return organizationLookup(resource.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationModify
  /**
   ** Updates an organization in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            to update.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link Tenant} mapped at
   **                            <code>tenant</code> to update.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the modified {@link Tenant}.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   **
   ** @throws ProcessingException if the organization cannot be modified.
   */
  public Tenant organizationModify(final String tenant, final Tenant resource)
    throws ProcessingException {

    if (!organizationExists(resource.name()))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, resource.name(), Organization.RESOURCE));

    final DatabaseUpdate update  = DatabaseUpdate.build(
      Organization.ENTITY
    , DatabaseFilter.build(Organization.NAME, tenant, DatabaseFilter.Operator.EQ)
    , transform(resource)
    , CollectionUtility.set(
        Pair.of("created_by", Types.VARCHAR)
      , Pair.of("created_on", Types.TIMESTAMP)
      , Pair.of("updated_by", Types.VARCHAR)
      , Pair.of("updated_on", Types.TIMESTAMP)
      )
    );
    try {
      final Map<String, Object> result = update.execute(this.connection);
      resource.createdBy((String)result.get("created_by"));
      resource.createdAt(Long.valueOf(((Date)result.get("created_on")).getTime()));
      resource.modifiedBy((String)result.get("updated_by"));
      resource.modifiedAt(Long.valueOf(((Date)result.get("updated_on")).getTime()));
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationDelete
  /**
   ** Delete an organization in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  id                 the identifier of the resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  public void organizationDelete(final String id)
    throws ProcessingException {

    if (!organizationExists(id))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, id, Organization.RESOURCE));

    final DatabaseDelete delete  = DatabaseDelete.build(Organization.ENTITY, DatabaseFilter.build(Entity.NAME, id, DatabaseFilter.Operator.EQ));
    try {
      delete.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleSearch
  /**
   ** Search userroles.
   **
   ** @return                    the result set as a {@link RoleResult}.
   **                            <br>
   **                            Possible object is {@link RoleResult}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public List<String> userroleSearch()
    throws ProcessingException {

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseSelect search = DatabaseSelect.build(UserGroup.ENTITY, DatabaseFilter.NOP, returning(Entity.NAME));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return batch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleSearch
  /**
   ** Search organization userroless.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the organization resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result set as a {@link RoleResult}.
   **                            <br>
   **                            Possible object is {@link RoleResult}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public RoleResult userroleSearch(final String tenant)
    throws ProcessingException {

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseSelect search = DatabaseSelect.build(Organization.ORL, DatabaseFilter.build(Organization.ORL, tenant, DatabaseFilter.Operator.EQ), returning(Entity.NAME));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return new RoleResult().list(batch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleExists
  /**
   ** Lookup an userrole by the specified public unique <code>identifier</code>.
   **
   ** @param  group              the unique userrole identifier to lookup
   **                            the role resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the resource mapped
   **                            at <code>id</code> and <code>name</code>.
   **                            <br>
   **                            Possible  object is <code>boolean</code>.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public boolean userroleExists(final String group)
    throws ProcessingException {

    final List<String> entry = userroleLookup(group);
    return entry != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleExists
  /**
   ** Lookup an userrole from an organization by the specified public unique
   ** <code>identifier</code>.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the organization resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the unique userrole identifier to lookup
   **                            the role resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the resource mapped
   **                            at <code>id</code> and <code>name</code>.
   **                            <br>
   **                            Possible  object is <code>boolean</code>.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public boolean userroleExists(final String tenant, final String role)
    throws ProcessingException {

    final UserRole.Entry entry = userroleLookup(tenant, role);
    return entry != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleLookup
  /**
   ** Lookup an userrole by the specified public unique
   ** <code>identifier</code>.
   **
   ** @param  group              the unique userrole identifier to lookup
   **                            the role resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link UserRole.Entry} mapped at
   **                            <code>tenant</code> and <code>role</code>.
   **                            <br>
   **                            Possible object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public List<String> userroleLookup(final String group)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(Entity.NAME, group, DatabaseFilter.Operator.EQ);
    final DatabaseSelect select = DatabaseSelect.build(UserGroup.ENTITY, filter, returning(Entity.NAME));
    try {
      final List<List<Pair<String, Object>>> result = select.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(group);
        }
        else if (result.size() > 0) {
          // single hit
          final List<String>               batch = new ArrayList<String>();
          final List<Pair<String, Object>> tupel = result.get(0);
          for (Pair<String, Object> cursor : tupel) {
            switch (cursor.tag) {
              case Entity.NAME : batch.add((String)cursor.value);
                                 break;
            }
          }
          return batch;
        }
        else {
          // nothing found
          return null;
        }
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      select.close();
    }
    // nothing found
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleLookup
  /**
   ** Lookup an userrole by the specified public unique
   ** <code>identifier</code>.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the organization role identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link UserRole.Entry} mapped at
   **                            <code>tenant</code> and <code>role</code>.
   **                            <br>
   **                            Possible object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public UserRole.Entry userroleLookup(final String tenant, final String role)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Entity.NAME,     role,   DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect select = DatabaseSelect.build(Organization.ORL, filter, returning(Entity.NAME));
    try {
      final List<List<Pair<String, Object>>> result = select.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(role);
        }
        else if (result.size() > 0) {
          // single hit
          final UserRole.Entry             batch = new UserRole.Entry();
          final List<Pair<String, Object>> tupel = result.get(0);
          for (Pair<String, Object> cursor : tupel) {
            switch (cursor.tag) {
              case Entity.NAME : batch.role((String)cursor.value);
                                 break;
            }
          }
          return batch;
        }
        else {
          // nothing found
          return null;
        }
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      select.close();
    }
    // nothing found
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleModify
  /**
   ** Updates an userrole in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  tenant             the system identifier of the role resource
   **                            to update.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the name of the userrole to update.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link UserRole.Entry} mapped at
   **                            <code>tenant</code> and <code>role</code> to
   **                            update.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the modified {@link UserRole.Entry}.
   **                            <br>
   **                            Possible object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if the userrole cannot be modified.
   */
  public UserRole.Entry userroleModify(final String tenant, final String role, final UserRole.Entry resource)
    throws ProcessingException {

    if (!userroleExists(tenant, role))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, role, Organization.USERROLE));

    final DatabaseUpdate update  = DatabaseUpdate.build(
      Organization.ORL
    , DatabaseFilter.build(
        DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ)
      , DatabaseFilter.build(Entity.NAME,     role,   DatabaseFilter.Operator.EQ)
      , DatabaseFilter.Operator.AND
      )
    , transform(resource)
    );
    try {
      update.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleCreate
  /**
   ** Create an organization userrole.
   **
   ** @param  resource           the {@link UserRole.Entry} to map at
   **                            <code>tenant</code> and <code>role</code>.
   **                            <br>
   **                            Allowed object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public void userroleCreate(final UserRole.Entry resource)
    throws ProcessingException {

    if (userroleExists(resource.tenant(), resource.role()))
      throw ResourceConflictException.uniqueness(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_EXISTS, resource.role(), Organization.USERROLE));

    final DatabaseInsert insert = DatabaseInsert.build(Organization.ORL, transform(resource));
    try {
      insert.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleDelete
  /**
   ** Deletes an organization userroles.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the role organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public void userroleDelete(final String tenant, final String role)
    throws ProcessingException {

    if (!userroleExists(tenant, role))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, role, Organization.USERROLE));

    final DatabaseDelete delete = DatabaseDelete.build(
      Organization.ORL
    , DatabaseFilter.build(
        DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ)
      , DatabaseFilter.build(Entity.NAME,     role,   DatabaseFilter.Operator.EQ)
      , DatabaseFilter.Operator.AND
      )
    );
    try {
      delete.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleAssign
  /**
   ** Assigns a userroles to a grantee.
   **
   ** @param  role               the name of the userrole to populate the
   **                            members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  grantee            the unique user identifier to lookup
   **                            the role assignment resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link User} information created.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public User userroleAssign(final String role, final String grantee)
    throws ProcessingException {

    if (!userroleExists(role))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, role, UserGroup.RESOURCE));

    if (userroleAssigned(role, grantee))
      throw ResourceConflictException.uniqueness(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_EXISTS, grantee, UserGroup.USERGROUP));

    final List<Pair<String, DatabaseParameter>> payload = new ArrayList<Pair<String, DatabaseParameter>>();
    payload.add(Pair.of(UserGroup.Member.ROLE.id,      DatabaseParameter.build(role,    Types.VARCHAR)));
    payload.add(Pair.of(UserGroup.Member.MAIL.id,      DatabaseParameter.build(grantee, Types.VARCHAR)));
    payload.add(Pair.of(UserGroup.Member.CREATEDBY.id, DatabaseParameter.build("apigee.bka.bund.de", Types.VARCHAR)));
    payload.add(Pair.of(UserGroup.Member.UPDATEDBY.id, DatabaseParameter.build("apigee.bka.bund.de", Types.VARCHAR)));
    final DatabaseInsert insert = DatabaseInsert.build(UserGroup.UGP, payload);
    try {
      insert.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      insert.close();
    }
    
    // populate the user response
    final User           user   = accountLookup(grantee);
    final DatabaseFilter filter = DatabaseFilter.build(Account.MAIL,     grantee, DatabaseFilter.Operator.EQ);
    final DatabaseSelect search = DatabaseSelect.build(Organization.URL, filter, returning(Organization.FK, Entity.NAME));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      if (result != null) {
        if (result.size() > 0) {
          final UserRole member = new UserRole();
          for (List<Pair<String, Object>> cursor : result) {
            final UserRole.Entry entry = new UserRole.Entry();
            for (Pair<String, Object> pair : cursor) {
              switch(pair.tag) {
                case Organization.FK : entry.tenant((String)pair.value);
                                       break;
                case Entity.NAME     : entry.role((String)pair.value);
                                       break;
              }
            }
            member.add(entry);
          }
          user.role(member);
        }
      }
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return user;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleRevoke
  /**
   ** Revokes a userroles from a grantee.
   **
   ** @param  role               the name of the userrole to delete the
   **                            member from.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  grantee            the unique user identifier to remove from the
   **                            role.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public void userroleRevoke(final String role, final String grantee)
    throws ProcessingException {

    if (!userroleExists(role))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, role, UserGroup.RESOURCE));
            
    if (!userroleAssigned(role, grantee))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, grantee, UserGroup.USERGROUP));

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Entity.NAME,  role,    DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Account.MAIL, grantee, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    ); 
    final DatabaseDelete delete  = DatabaseDelete.build(UserGroup.UGP, filter);
    try {
      delete.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleMember
  /**
   ** Returns the members of an organization userroles.
   **
   ** @param  tenant             the identifier of the organization to associate
   **                            the resource to create with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the name of the userrole to populate the
   **                            members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link UserRole} with the memebers of the
   **                            user role mapped at <code>tenant</code> and
   **                            <code>role</code>.
   **                            <br>
   **                            Allowed object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public MemberResult userroleMember(final String tenant, final String role)
    throws ProcessingException {

    if (!userroleExists(tenant, role))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, role, Organization.USERROLE));

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Entity.NAME,     role,         DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect search = DatabaseSelect.build(Organization.URL, filter, returning(Account.MAIL));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return new MemberResult().list(batch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleMember
  /**
   ** Returns the members of a userroles.
   **
   ** @param  group              the name of the userrole to populate the
   **                            members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link UserRole} with the memebers of the
   **                            user role mapped at <code>tenant</code> and
   **                            <code>role</code>.
   **                            <br>
   **                            Allowed object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public MemberResult userroleMember(final String group)
    throws ProcessingException {

    if (!userroleExists(group))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, group, UserGroup.USERGROUP));

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseFilter filter = DatabaseFilter.build(Entity.NAME, group, DatabaseFilter.Operator.EQ);
    final DatabaseSelect search = DatabaseSelect.build(UserGroup.UGP, filter, returning(Account.MAIL));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return new MemberResult().list(batch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleMember
  /**
   ** Returns the certain member of an organization userroles.
   **
   ** @param  tenant             the identifier of the organization to associate
   **                            the resource to create with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the name of the userrole to populate the
   **                            members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  grantee            the identifier of the user to populate the
   **                            roles for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link UserRole} with the memebers of the
   **                            user role mapped at <code>tenant</code> and
   **                            <code>role</code>.
   **                            <br>
   **                            Allowed object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public UserResult.Entry userroleMember(final String tenant, final String role, final String grantee)
    throws ProcessingException {

    if (!userroleExists(tenant, role))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, role, Organization.USERROLE));

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(
        DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ)
      , DatabaseFilter.build(Entity.NAME,     role,   DatabaseFilter.Operator.EQ)
      , DatabaseFilter.Operator.AND
      )
    , DatabaseFilter.build(Account.MAIL, grantee, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect search = DatabaseSelect.build(Organization.URL, filter, returning(Account.MAIL));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(grantee);
        }
        else if (result.size() > 0) {
          // single hit
          final UserResult.Entry           batch = new UserResult.Entry();
          final List<Pair<String, Object>> tupel = result.get(0);
          for (Pair<String, Object> cursor : tupel) {
            switch (cursor.tag) {
              case Account.MAIL : batch.name((String)cursor.value);
                                  break;
            }
          }
          return batch;
        }
        else {
          // nothing found
          return null;
        }
      }
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleAssigned
  /**
   ** Lookup an userrole assignment by the specified public unique role
   ** identifier.
   **
   ** @param  role               the name of the userrole to populate the
   **                            members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  grantee            the unique user identifier to lookup
   **                            the role assignment resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the resource mapped
   **                            at <code>id</code> and <code>name</code> for
   **                            <code>grantee</code>.
   **                            <br>
   **                            Possible  object is <code>boolean</code>.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public boolean userroleAssigned(final String role, final String grantee)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(UserGroup.NAME, role,    DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Account.MAIL,   grantee, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect lookup = DatabaseSelect.build(Organization.URL, filter, returning(Entity.VERSION));
    try {
      final List<List<Pair<String, Object>>> result = lookup.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(grantee);
        }
        return (result.size() == 1);
      }
      return false;
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleAssigned
  /**
   ** Lookup an userrole assignment from an organization by the specified public
   ** unique <code>id</code>.
   **
   ** @param  tenant             the identifier of the organization to associate
   **                            the resource to create with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the name of the userrole to populate the
   **                            members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  grantee            the unique user identifier to lookup
   **                            the role assignment resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the resource mapped
   **                            at <code>id</code> and <code>name</code> for
   **                            <code>grantee</code>.
   **                            <br>
   **                            Possible  object is <code>boolean</code>.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public boolean userroleAssigned(final String tenant, final String role, final String grantee)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(
        DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ)
      , DatabaseFilter.build(Entity.NAME,     role,   DatabaseFilter.Operator.EQ)
      , DatabaseFilter.Operator.AND
      )
    , DatabaseFilter.build(Account.MAIL, grantee, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect lookup = DatabaseSelect.build(Organization.URL, filter, returning(Entity.VERSION));
    try {
      final List<List<Pair<String, Object>>> result = lookup.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(grantee);
        }
        return (result.size() == 1);
      }
      return false;
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleAssign
  /**
   ** Assigns an organization userroles to a grantee.
   **
   ** @param  tenant             the unique organization identifier to assign
   **                            the organization resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the name of the userrole to assign the
   **                            members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  grantee            the unique user identifier to assign
   **                            the role resource to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link User} information created.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public User userroleAssign(final String tenant, final String role, final String grantee)
    throws ProcessingException {

    if (!userroleExists(tenant, role))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, role, Organization.USERROLE));

    if (userroleAssigned(tenant, role, grantee))
      throw ResourceConflictException.uniqueness(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_EXISTS, grantee, Organization.USERROLE));

    final List<Pair<String, DatabaseParameter>> payload = new ArrayList<Pair<String, DatabaseParameter>>();
    payload.add(Pair.of(Organization.Member.TENANT.id,    DatabaseParameter.build(tenant,               Types.VARCHAR)));
    payload.add(Pair.of(Organization.Member.ROLE.id,      DatabaseParameter.build(role,                 Types.VARCHAR)));
    payload.add(Pair.of(Organization.Member.MAIL.id,      DatabaseParameter.build(grantee,              Types.VARCHAR)));
    payload.add(Pair.of(Organization.Member.VERSION.id,   DatabaseParameter.build("00000000",           Types.VARCHAR)));
    payload.add(Pair.of(Organization.Member.CREATEDBY.id, DatabaseParameter.build("apigee.bka.bund.de", Types.VARCHAR)));
    payload.add(Pair.of(Organization.Member.UPDATEDBY.id, DatabaseParameter.build("apigee.bka.bund.de", Types.VARCHAR)));
    final DatabaseInsert insert = DatabaseInsert.build(Organization.URL, payload);
    try {
      insert.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      insert.close();
    }
    
    // populate the user response
    final User user = accountLookup(grantee);
    final DatabaseFilter filter = DatabaseFilter.build(Account.MAIL, grantee, DatabaseFilter.Operator.EQ);
    final DatabaseSelect search = DatabaseSelect.build(Organization.URL, filter, returning(Organization.FK, Entity.NAME));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      if (result != null) {
        if (result.size() > 0) {
          final UserRole member = new UserRole();
          for (List<Pair<String, Object>> cursor : result) {
            final UserRole.Entry entry = new UserRole.Entry();
            for (Pair<String, Object> pair : cursor) {
              switch(pair.tag) {
                case Organization.FK : entry.tenant((String)pair.value);
                                       break;
                case Entity.NAME     : entry.role((String)pair.value);
                                       break;
              }
            }
            member.add(entry);
          }
          user.role(member);
        }
      }
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return user;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleRevoke
  /**
   ** Revokes an organization userroles from a grantee.
   **
   ** @param  tenant             the identifier of the organization to
   **                            unassociate the resource to delete with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the name of the userrole to delete the
   **                            member from.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  grantee            the unique user identifier to remove from the
   **                            role.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public void userroleRevoke(final String tenant, final String role, final String grantee)
    throws ProcessingException {

    if (!userroleExists(tenant, role))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, role, Organization.USERROLE));
            
    if (!userroleAssigned(tenant, role, grantee))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, grantee, Organization.USERROLE));

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(
        DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ)
      , DatabaseFilter.build(Entity.NAME,     role,   DatabaseFilter.Operator.EQ)
      , DatabaseFilter.Operator.AND
      )
    , DatabaseFilter.build(Account.MAIL, grantee, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    ); 
    final DatabaseDelete delete  = DatabaseDelete.build(Organization.URL, filter);
    try {
      delete.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerSearch
  /**
   ** Search developers.
   **
   ** @param  tenant             the identifier of the organization the desired
   **                            resources are associate the with.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result set as a {@link DeveloperResult}.
   **                            <br>
   **                            Possible object is {@link DeveloperResult}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public DeveloperResult developerSearch(final String tenant)
    throws ProcessingException {

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseSelect search = DatabaseSelect.build(Organization.DEV, DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ), returning(Account.MAIL));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        final Developer item = new Developer();
        item.tenant(tenant);
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return new DeveloperResult().list(batch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerExists
  /**
   ** Lookup a developer by the specified public unique
   ** <code>identifier</code>.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the unique developer identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the user resource mapped
   **                            at either <code>id</code> or
   **                            <code>username</code> (determined by
   **                            <code>primary</code>).
   **                            <br>
   **                            Possible  object is <code>boolean</code>.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public boolean developerExists(final String tenant, final String identifier)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Organization.FK,  tenant,     DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Entity.MAIL,      identifier, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect lookup = DatabaseSelect.build(Organization.DEV, filter, returning("id"));
    try {
      final List<List<Pair<String, Object>>> result = lookup.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        return (result.size() == 1);
      }
      return false;
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerLookup
  /**
   ** Lookup a developer by the specified public unique
   ** <code>identifier</code>.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the unique developer identifier to lookup
   **                            the resource in the organization.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Developer} mapped at
   **                            <code>tenant</code> and
   **                            <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public Developer developerLookup(final String tenant, final String identifier)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Account.ORGANIZATION, tenant,     DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Account.MAIL,         identifier, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect select = DatabaseSelect.build(Organization.DEV, filter, returning(Account.ID, Account.ORGANIZATION, Account.STATUS, Account.MAIL, Account.USERNAME, Account.LASTNAME, Account.FIRSTNAME, Account.CREATEDON, Account.CREATEDBY, Account.UPDATEDON, Account.UPDATEDBY));
    try {
      final List<List<Pair<String, Object>>> result = select.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        else if (result.size() > 0) {
          // single hit
          final Developer                  item = new Developer();
          final List<Pair<String, Object>> tupel = result.get(0);
          for (Pair<String, Object> cursor : tupel) {
            switch (cursor.tag) {
              case Account.ID           : item.id((String)cursor.value);
                                          break;
              case Account.STATUS       : item.status((String)cursor.value);
                                          break;
              case Account.MAIL         : item.email((String)cursor.value);
                                          break;
              case Account.USERNAME     : item.userName((String)cursor.value);
                                          break;
              case Account.LASTNAME     : item.lastName((String)cursor.value);
                                          break;
              case Account.FIRSTNAME    : item.firstName((String)cursor.value);
                                          break;
              case Account.ORGANIZATION : item.tenant((String)cursor.value);
                                          break;
              case Account.CREATEDON    : item.createdAt(timestamp(cursor.value));
                                          break;
              case Account.CREATEDBY    : item.createdBy((String)cursor.value);
                                          break;
              case Account.UPDATEDON    : item.modifiedAt(timestamp(cursor.value));
                                          break;
              case Account.UPDATEDBY    : item.modifiedBy((String)cursor.value);
                                          break;
            }
          }
          // fake the company, apps and attribute collections
          item.application(developerApplication(item));
          item.company(developerCompany(tenant, identifier));
          return item;
        }
        else {
          // nothing found
          return null;
        }
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      select.close();
    }
    // nothing found
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerApplication
  /**
   ** Search for applications granted to a developer by the specified public
   ** unique <code>identifier</code>.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the unique developer identifier to lookup
   **                            the resource in the organization.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link List} of applications granted to a
   **                            developer at <code>tenant</code> and
   **                            <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link List}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public List<String> developerApplication(final Developer developer)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build("org", developer.tenant(), DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build("dev", developer.id(),     DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect select = DatabaseSelect.build(Organization.ARL, filter, returning("app"));
    try {
      final List<List<Pair<String, Object>>> result = select.execute(this.connection);
      if (result != null) {
        final List<String> grant = new ArrayList<>(result.size());
        for (List<Pair<String, Object>> outer : result) {
          for (Pair<String, Object> app : outer) {
            grant.add((String)app.value);
          }
        }
        return grant;
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      select.close();
    }
    // nothing found
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerCompany
  /**
   ** Search for companies granted to a developer by the specified public
   ** unique <code>identifier</code>.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the unique developer identifier to lookup
   **                            the resource in the organization.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link List} of companies granted to a
   **                            developer at <code>tenant</code> and
   **                            <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link List}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public List<String> developerCompany(final String tenant, final String identifier)
    throws ProcessingException {

    // nothing found
    final List<String> grants = new ArrayList<>();
    return grants;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerCreate
  /**
   ** Create a developer in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  resource           the {@link Developer} mapped at organization
   **                            <code>identifier</code>.
   **                            <br>
   **                            Allowed object is {@link Developer}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  public void developerCreate(final Developer resource)
    throws ProcessingException {

    final DatabaseInsert insert  = DatabaseInsert.build(
      Organization.DEV
    , transform(resource)
    , CollectionUtility.set(
        Pair.of("id",         Types.VARCHAR)
      , Pair.of("status",     Types.VARCHAR)
      , Pair.of("created_by", Types.VARCHAR)
      , Pair.of("created_on", Types.TIMESTAMP)
      , Pair.of("updated_by", Types.VARCHAR)
      , Pair.of("updated_on", Types.TIMESTAMP)
      )
    );
    try {
      final Map<String, Object> result = insert.execute(this.connection);
      resource.id((String)result.get("id"));
      resource.status((String)result.get("status"));
      resource.createdBy((String)result.get("created_by"));
      resource.createdAt(Long.valueOf(((Date)result.get("created_on")).getTime()));
      resource.modifiedBy((String)result.get("updated_by"));
      resource.modifiedAt(Long.valueOf(((Date)result.get("updated_on")).getTime()));
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerModify
  /**
   ** Updates a developer in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer is associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the unique identifier of the developer resource
   **                            to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link Tenant} mapped at
   **                            <code>tenant</code> to update.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the modified {@link Tenant}.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   **
   ** @throws ProcessingException if the organization cannot be modified.
   */
  public Developer developerModify(final String tenant, final String identifier, final Developer resource)
    throws ProcessingException {

    if (!developerExists(tenant, identifier))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, identifier, Organization.DEVELOPER));

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Organization.FK,  tenant,     DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Entity.MAIL,      identifier, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseUpdate update  = DatabaseUpdate.build(
      Organization.DEV
    , filter
    , transform(resource)
    , CollectionUtility.set(
        Pair.of("id",         Types.VARCHAR)
      , Pair.of("status",     Types.VARCHAR)
      , Pair.of("created_by", Types.VARCHAR)
      , Pair.of("created_on", Types.TIMESTAMP)
      , Pair.of("updated_by", Types.VARCHAR)
      , Pair.of("updated_on", Types.TIMESTAMP)
      )
    );
    try {
      final Map<String, Object> result = update.execute(this.connection);
      resource.id((String)result.get("id"));
      resource.status((String)result.get("status"));
      resource.createdBy((String)result.get("created_by"));
      resource.createdAt(Long.valueOf(((Date)result.get("created_on")).getTime()));
      resource.modifiedBy((String)result.get("updated_by"));
      resource.modifiedAt(Long.valueOf(((Date)result.get("updated_on")).getTime()));
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerDelete
  /**
   ** Delete a developer in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            the developer is associated with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the unique identifier of the developer resource
   **                            to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  public void developerDelete(final String tenant, final String identifier)
    throws ProcessingException {

    if (!developerExists(tenant, identifier))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, identifier, Organization.DEVELOPER));

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Organization.FK, tenant,     DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Entity.MAIL,     identifier, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseDelete delete  = DatabaseDelete.build(Organization.DEV, filter);
    try {
      delete.execute(this.connection);
      commit();
    }
    catch (DatabaseException e) {
      try {
        rollback();
      }
      catch (DatabaseException x) {
        x.printStackTrace();
      }
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   companySearch
  /**
   ** Search companys.
   **
   ** @param  tenant             the identifier of the organization the desired
   **                            resources are associate the with.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result set as a {@link CompanyResult}.
   **                            <br>
   **                            Possible object is {@link CompanyResult}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public CompanyResult companySearch(final String tenant)
    throws ProcessingException {

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseSelect search = DatabaseSelect.build(Organization.CMP, DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ), returning(Entity.NAME));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return new CompanyResult().list(batch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   companyLookup
  /**
   ** Lookup an company by the specified public unique <code>identifier</code>.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the company identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link UserRole.Entry} mapped at
   **                            <code>tenant</code> and <code>role</code>.
   **                            <br>
   **                            Possible object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public Company companyLookup(final String tenant, final String identifier)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Organization.FK, tenant,     DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Entity.NAME,     identifier, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect select = DatabaseSelect.build(Organization.CMP, filter, returning(Entity.NAME, Entity.STATUS, Entity.DISPLAYNAME, Entity.CREATEDON, Entity.CREATEDBY, Entity.UPDATEDON, Entity.UPDATEDBY));
    try {
      final List<List<Pair<String, Object>>> result = select.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        else if (result.size() > 0) {
          // single hit
          final Company                    item  = new Company().tenant(tenant);
          final List<Pair<String, Object>> tupel = result.get(0);
          for (Pair<String, Object> cursor : tupel) {
            switch (cursor.tag) {
              case Entity.NAME         : item.name((String)cursor.value);
                                         break;
              case Entity.STATUS       : item.status((String)cursor.value);
                                         break;
              case Entity.DISPLAYNAME  : item.displayName((String)cursor.value);
                                         break;
              case Entity.CREATEDON    : item.createdAt(timestamp(cursor.value));
                                         break;
              case Entity.CREATEDBY    : item.createdBy((String)cursor.value);
                                         break;
              case Entity.UPDATEDON    : item.modifiedAt(timestamp(cursor.value));
                                         break;
              case Entity.UPDATEDBY    : item.modifiedBy((String)cursor.value);
                                         break;
            }
          }
          return item;
        }
        else {
          // nothing found
          return null;
        }
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      select.close();
    }
    // nothing found
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   productSearch
  /**
   ** Search products.
   **
   ** @param  tenant             the identifier of the organization the desired
   **                            resources are associate the with.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result set as a {@link ProductResult}.
   **                            <br>
   **                            Possible object is {@link ProductResult}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public ProductResult productSearch(final String tenant)
    throws ProcessingException {

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseSelect search = DatabaseSelect.build(Organization.PRD, DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ), returning(Entity.NAME));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return new ProductResult().list(batch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   productLookup
  /**
   ** Lookup a product by the specified public unique <code>identifier</code>.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the product identifier to lookup the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Product} mapped at
   **                            <code>tenant</code> and
   **                            <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public Product productLookup(final String tenant, final String identifier)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Organization.FK, tenant,     DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Entity.NAME,     identifier, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect select = DatabaseSelect.build(Organization.PRD, filter, returning(Entity.NAME, Entity.DISPLAYNAME, Entity.DESCRIPTION, Entity.CREATEDON, Entity.CREATEDBY, Entity.UPDATEDON, Entity.UPDATEDBY, "approval", "uris", "proxy", "scope", "environment", "timequota", "timeinterval", "timeunit"));
    try {
      final List<List<Pair<String, Object>>> result = select.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        else if (result.size() > 0) {
          // single hit
          final Product                    item  = new Product();
          final List<Pair<String, Object>> tupel = result.get(0);
          for (Pair<String, Object> cursor : tupel) {
            switch (cursor.tag) {
              case Entity.NAME         : item.name((String)cursor.value);
                                         break;
              case Entity.DISPLAYNAME  : item.displayName((String)cursor.value);
                                         break;
              case Entity.DESCRIPTION  : item.description((String)cursor.value);
                                         break;
              case "approval"          : item.approval((String)cursor.value);
                                         break;
              case "uris"              : if (cursor.value != null)
                                           item.resource(CollectionUtility.list(((String)cursor.value).split(",")));
                                         break;
              case "proxy"             : if (cursor.value != null)
                                           item.proxy(CollectionUtility.list(((String)cursor.value).split(",")));
                                         break;
              case "scope"             : if (cursor.value != null)
                                           item.scope(CollectionUtility.list(((String)cursor.value).split(",")));
                                         break;
              case "environment"       : if (cursor.value != null)
                                           item.environment(CollectionUtility.list(((String)cursor.value).split(",")));
                                         break;
              case "timequota"         : item.quota((String)cursor.value);
                                         break;
              case "timeinterval"      : item.timeInterval((String)cursor.value);
                                         break;
              case "timeunit"          : item.timeUnit((String)cursor.value);
                                         break;
              case Entity.CREATEDON    : item.createdAt(timestamp(cursor.value));
                                         break;
              case Entity.CREATEDBY    : item.createdBy((String)cursor.value);
                                         break;
              case Entity.UPDATEDON    : item.modifiedAt(timestamp(cursor.value));
                                         break;
              case Entity.UPDATEDBY    : item.modifiedBy((String)cursor.value);
                                         break;
            }
          }
          return item;
        }
        else {
          // nothing found
          return null;
        }
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      select.close();
    }
    // nothing found
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationSearch
  /**
   ** Search applications.
   **
   ** @param  tenant             the identifier of the organization the desired
   **                            resources are associate the with.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result set as a {@link ApplicationResult}.
   **                            <br>
   **                            Possible object is {@link ApplicationResult}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public ApplicationResult applicationSearch(final String tenant)
    throws ProcessingException {

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseSelect search = DatabaseSelect.build(Organization.APP, DatabaseFilter.build(Organization.FK, tenant, DatabaseFilter.Operator.EQ), returning(Entity.ID));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return new ApplicationResult().list(batch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationLookup
  /**
   ** Lookup an application by the specified public unique
   ** <code>identifier</code>.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the application identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Application} mapped at
   **                            <code>tenant</code> and
   **                            <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link Application}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public Application applicationLookup(final String tenant, final String identifier)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(Organization.FK, tenant,     DatabaseFilter.Operator.EQ)
    , DatabaseFilter.build(Entity.ID,       identifier, DatabaseFilter.Operator.EQ)
    , DatabaseFilter.Operator.AND
    );
    final DatabaseSelect select = DatabaseSelect.build(Organization.APP, filter, returning(Entity.ID, Entity.STATUS, Entity.NAME, Entity.DISPLAYNAME, Entity.CREATEDON, Entity.CREATEDBY, Entity.UPDATEDON, Entity.UPDATEDBY));
    try {
      final List<List<Pair<String, Object>>> result = select.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        else if (result.size() > 0) {
          // single hit
          final Application                item  = new Application();
          final List<Pair<String, Object>> tupel = result.get(0);
          for (Pair<String, Object> cursor : tupel) {
            switch (cursor.tag) {
              case Entity.ID           : item.id((String)cursor.value);
                                         break;
              case Entity.STATUS       : item.status((String)cursor.value);
                                         break;
              case Entity.NAME         : item.name((String)cursor.value);
                                         break;
              case Entity.DISPLAYNAME  : item.displayName((String)cursor.value);
                                         break;
              case Entity.CREATEDON    : item.createdAt(timestamp(cursor.value));
                                         break;
              case Entity.CREATEDBY    : item.createdBy((String)cursor.value);
                                         break;
              case Entity.UPDATEDON    : item.modifiedAt(timestamp(cursor.value));
                                         break;
              case Entity.UPDATEDBY    : item.modifiedBy((String)cursor.value);
                                         break;
            }
          }
          return item;
        }
        else {
          // nothing found
          return null;
        }
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      select.close();
    }
    // nothing found
    return null;
  }

  private Tenant organizationEnvironment(final Tenant tenant)
    throws ProcessingException {

    final List<String>   batch  = new ArrayList<String>();
    final DatabaseFilter filter = DatabaseFilter.build(Organization.FK, tenant.name(), DatabaseFilter.Operator.EQ);
    final DatabaseSelect search = DatabaseSelect.build(Organization.ENV, filter, returning(Organization.NAME));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record)
          batch.add((String)cursor.value);
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return organizationProperties(tenant.environment(batch));
  }

  private Tenant organizationProperties(final Tenant tenant)
    throws ProcessingException {

    final List<Property.Pair> batch  = new ArrayList<Property.Pair>();
    final DatabaseFilter      filter = DatabaseFilter.build(Organization.FK, tenant.name(), DatabaseFilter.Operator.EQ);
    final DatabaseSelect      search = DatabaseSelect.build(Organization.PRP, filter, returning(Organization.NAME, Organization.VALUE));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute();
      for (List<Pair<String, Object>> record : result) {
        final Property.Pair pair = new Property.Pair();
        for (Pair<String, Object> cursor : record) {
          switch (cursor.tag) {
            case Organization.NAME  : pair.name((String)cursor.value);
                                      break;
            case Organization.VALUE : pair.value((String)cursor.value);
                                      break;
          }
        }
        batch.add(pair);
      }
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
    finally {
      search.close();
    }
    return tenant.properties(new Property().list(batch));
  }

  private static List<Pair<String, String>> returning(final String... requested) {
    final List<Pair<String, String>> returning = new ArrayList<Pair<String, String>>();
    for (String cursor : requested) {
      final Pair<String, String> pair = Pair.of(cursor, cursor);
      if (!returning.contains(pair)) {
        returning.add(pair);
      }
    }
    return returning;
  }

  private static List<Pair<String, DatabaseParameter>> transform(final User resource) {
    final List<Pair<String, DatabaseParameter>> payload = new ArrayList<Pair<String, DatabaseParameter>>();
    // iterate over all permissible attributes defined for an account but add to
    // the payload only those that are not operational attributes
    for (Account.Attribute cursor : Account.Attribute.values()) {
      switch (cursor) {
        case MAIL      : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.emailId(),    Types.VARCHAR)));
                         break;
        case PASSWORD  : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.password(),   Types.VARCHAR)));
                         break;
        case LASTNAME  : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.lastName(),   Types.VARCHAR)));
                         break;
        case FIRSTNAME : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.firstName(),  Types.VARCHAR)));
                         break;
        case VERSION   : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.version(),    Types.VARCHAR)));
                         break;
        case CREATEDBY : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.createdBy(),  Types.VARCHAR)));
                         break;
        case UPDATEDBY : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.modifiedBy(), Types.VARCHAR)));
                         break;
      }
    }
    return payload;
  }

  private static List<Pair<String, DatabaseParameter>> transform(final Tenant resource) {
    final List<Pair<String, DatabaseParameter>> payload = new ArrayList<Pair<String, DatabaseParameter>>();
    // iterate over all permissible attributes defined for an account but add to
    // the payload only those that are not operational attributes
    for (Organization.Attribute cursor : Organization.Attribute.values()) {
      switch (cursor) {
        case NAME         : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.name(),        Types.VARCHAR)));
                            break;
        case TYPE         : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.type(),        Types.VARCHAR)));
                            break;
        case DISPLAYNAME  : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.displayName(), Types.VARCHAR)));
                            break;
        case VERSION   : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.version(),        Types.VARCHAR)));
                         break;
        case CREATEDBY    : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.createdBy(),   Types.VARCHAR)));
                            break;
        case UPDATEDBY    : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.modifiedBy(),  Types.VARCHAR)));
                            break;
      }
    }
    return payload;
  }

  private static List<Pair<String, DatabaseParameter>> transform(final UserRole.Entry resource) {
    final List<Pair<String, DatabaseParameter>> payload = new ArrayList<Pair<String, DatabaseParameter>>();
    // iterate over all permissible attributes defined for an account but add to
    // the payload only those that are not operational attributes
    for (Organization.Userrole cursor : Organization.Userrole.values()) {
      switch (cursor) {
        case TENANT    : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.tenant(),     Types.VARCHAR)));
                         break;
        case NAME      : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.role(),       Types.VARCHAR)));
                         break;
        case VERSION   : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.version(),    Types.VARCHAR)));
                         break;
        case CREATEDBY : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.createdBy(),  Types.VARCHAR)));
                         break;
        case UPDATEDBY : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.modifiedBy(), Types.VARCHAR)));
                         break;
      }
    }
    return payload;
  }

  private static List<Pair<String, DatabaseParameter>> transform(final UserRole.Member resource) {
    final List<Pair<String, DatabaseParameter>> payload = new ArrayList<Pair<String, DatabaseParameter>>();
    // iterate over all permissible attributes defined for an account but add to
    // the payload only those that are not operational attributes
    for (Organization.Member cursor : Organization.Member.values()) {
      switch (cursor) {
        case TENANT    : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.tenant(),     Types.VARCHAR)));
                         break;
        case ROLE      : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.role(),       Types.VARCHAR)));
                         break;
        case MAIL      : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.user(),       Types.VARCHAR)));
                         break;
        case VERSION   : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.version(),    Types.VARCHAR)));
                         break;
        case CREATEDBY : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.createdBy(),  Types.VARCHAR)));
                         break;
        case UPDATEDBY : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.modifiedBy(), Types.VARCHAR)));
                         break;
      }
    }
    return payload;
  }

  private static List<Pair<String, DatabaseParameter>> transform(final Developer resource) {
    final List<Pair<String, DatabaseParameter>> payload = new ArrayList<Pair<String, DatabaseParameter>>();
    // iterate over all permissible attributes defined for an account but add to
    // the payload only those that are not operational attributes
    for (Organization.Developer cursor : Organization.Developer.values()) {
      switch (cursor) {
        case MAIL      : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.email(),      Types.VARCHAR)));
                         break;
        case STATUS    : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.status(),     Types.VARCHAR)));
                         break;
        case USERNAME  : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.userName(),   Types.VARCHAR)));
                         break;
        case LASTNAME  : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.lastName(),   Types.VARCHAR)));
                         break;
        case FIRSTNAME : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.firstName(),  Types.VARCHAR)));
                         break;
        case TENANT    : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.tenant(),     Types.VARCHAR)));
                         break;
        case VERSION   : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.version(),    Types.VARCHAR)));
                         break;
        case CREATEDBY : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.createdBy(),  Types.VARCHAR)));
                         break;
        case UPDATEDBY : payload.add(Pair.of(cursor.id, DatabaseParameter.build(resource.modifiedBy(), Types.VARCHAR)));
                         break;
      }
    }
    return payload;
  }
  
  private static Long timestamp(final Object value)
    throws ServerErrorException {

    try {
      if (value instanceof TIMESTAMP)
        return Long.valueOf(((TIMESTAMP)value).dateValue().getTime());
      else
        return Long.valueOf(((Timestamp)value).getTime());
    }
    catch (SQLException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }
}