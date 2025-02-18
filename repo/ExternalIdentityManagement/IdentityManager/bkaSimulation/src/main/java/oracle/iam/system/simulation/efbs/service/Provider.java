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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   eFBS SCIM Interface

    File        :   Provider.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Provider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.service;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.Types;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.ServerErrorException;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.rest.domain.ErrorResponse;
import oracle.iam.system.simulation.scim.domain.PatchOperation;

import oracle.iam.system.simulation.scim.schema.Name;
import oracle.iam.system.simulation.scim.schema.Email;
import oracle.iam.system.simulation.scim.schema.Metadata;
import oracle.iam.system.simulation.scim.schema.PhoneNumber;

import oracle.iam.system.simulation.scim.v2.schema.UserResource;
import oracle.iam.system.simulation.scim.v2.schema.EnterpriseUserExtension;

import oracle.iam.system.simulation.efbs.v2.schema.Account;
import oracle.iam.system.simulation.efbs.v2.schema.UserExtension;
import oracle.iam.system.simulation.efbs.v2.schema.FilterTranslator;

import oracle.iam.system.simulation.dbs.DatabaseError;
import oracle.iam.system.simulation.dbs.DatabaseCount;
import oracle.iam.system.simulation.dbs.DatabaseFilter;
import oracle.iam.system.simulation.dbs.DatabaseSelect;
import oracle.iam.system.simulation.dbs.DatabaseDelete;
import oracle.iam.system.simulation.dbs.DatabaseInsert;
import oracle.iam.system.simulation.dbs.DatabaseUpdate;
import oracle.iam.system.simulation.dbs.DatabaseExists;
import oracle.iam.system.simulation.dbs.DatabaseSearch;
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
  // Method:   accountExists
  /**
   ** Lookup an account by the specified public system identifier
   ** <code>id</code>.
   **
   ** @param  identifier         the public system identifier to lookup the
   **                            resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the advice which identifier <code>id</code> or
   **                            <code>username</code> is ment in the request.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
  public boolean accountExists(final String identifier, final boolean primary)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(primary ? Account.Attribute.ID.id : Account.Attribute.USERNAME.id, identifier, DatabaseFilter.Operator.EQ);
    final DatabaseExists lookup = DatabaseExists.build(Account.ENTITY, filter);
    try {
      return lookup.execute(this.connection);
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountLookup
  /**
   ** Lookup an account by the specified public system identifier
   ** <code>id</code> or unique identifier <code>username</code>.
   ** <p>
   ** If <code>primary</code> evaluate to <code>true</code> the attribute
   ** {@link Account#ID} will be used for the purpose of matching otherwise
   ** {@link Account#USERNAME} will be matched.
   **
   ** @param  identifier         the system identifier to lookup the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the advice which identifier <code>id</code> or
   **                            <code>username</code> is ment in the request.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  attribute          the {@link Set} of attributes to be returned in
   **                            the resources.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}
   **
   ** @return                    the {@link UserResource} mapped at
   **                            <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   **
   ** @throws ProcessingException if connection cannot be rollback.
   */
  public UserResource accountLookup(final String identifier, final boolean primary, final Set<String> attribute)
    throws ProcessingException {

    final DatabaseFilter filter = DatabaseFilter.build(primary ? Account.Attribute.ID.id : Account.Attribute.USERNAME.id, identifier, DatabaseFilter.Operator.EQ);
    final DatabaseSelect lookup = DatabaseSelect.build(Account.ENTITY, filter, returning(attribute));
    try {
      final List<List<Pair<String, Object>>> result = lookup.execute(this.connection);
      if (result != null) {
        if (result.size() > 1) {
          // ambigiuosly defined
          throw BadRequestException.tooMany(identifier);
        }
        else if (result.size() > 0) {
          // single hit
          return transform(result.get(0));
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
  // Method:   accountCount
  /**
   ** Count accounts.
   **
   ** @param  expression       the filter expression to apply to count accounts.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the amount of records matching the filter
   **                          expression.
   **                          <br>
   **                          Possible object is <code>int</code>.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public int accountCount(final Filter expression)
    throws ProcessingException {

    final DatabaseFilter filter = (expression == null) ? DatabaseFilter.NOP : FilterTranslator.build(Account.ENTITY, expression).get(0);
    final DatabaseCount  count  = DatabaseCount.build(Account.ENTITY, filter);
    try {
      return count.execute(this.connection);
    }
    catch (DatabaseException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("SQL").detail(e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountSearch
  /**
   ** Search account.
   **
   ** @param  start              the start index of the search result to return.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  count              the ammount of tupel to return.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  expression         the filter expression to apply on the search.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  attribute          the {@link Set} of attributes to be returned in
   **                            the resources.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}
   **
   ** @return                    the result set as a {@link List} where each
   **                            element is of type {@link UserResource}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  public List<UserResource> accountSearch(long start, long count, final Filter expression, final Set<String> attribute)
    throws ProcessingException {

    final List<UserResource> batch  = new ArrayList<UserResource>();
    final DatabaseFilter     filter = (expression == null) ? DatabaseFilter.NOP : FilterTranslator.build(Account.ENTITY, expression).get(0);
    final DatabaseSearch     search = DatabaseSearch.build(Account.ENTITY, filter, returning(attribute));
    try {
      search.prepare(this.connection);
      final List<List<Pair<String, Object>>> result = search.execute(start, start + count);
      for (List<Pair<String, Object>> record : result) {
        batch.add(transform(record));
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
  // Method:   accountCreate
  /**
   ** Create an account in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  resource           the {@link UserResource} mapped at
   **                            <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  public void accountCreate(final UserResource resource)
    throws ProcessingException {

    final DatabaseInsert insert = DatabaseInsert.build(Account.ENTITY, transform(resource), Account.SCHEMA.operational.values());
    try {
      final Map<String, Object> result = insert.execute(this.connection);
      commit();
      resource.id((String)result.get(Account.Attribute.ID.id));
      resource.metadata(metadata(result));
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
   ** @param  id                 the system identifier of the resource to
   **                            modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link UserResource} mapped at
   **                            <code>id</code> to update.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   **
   ** @throws ProcessingException if the account cannot be modified.
   */
  public void accountModify(final String id, final UserResource resource)
    throws ProcessingException {

    final DatabaseUpdate update  = DatabaseUpdate.build(Account.ENTITY, DatabaseFilter.build(Account.Attribute.ID.id, id, DatabaseFilter.Operator.EQ), transform(resource), Account.SCHEMA.operational.values());
    try {
      final Map<String, Object> result = update.execute(this.connection);
      commit();
      resource.id((String)result.get(Account.Attribute.ID.id));
      resource.metadata(metadata(result));
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
   ** @param  operation          the {@link List} of operations to update at
   **                            <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            elementis of type {@link PatchOperation}.
   **
   ** @throws ProcessingException if the account cannot be modified.
   */
  public void accountModify(final String id, final List<PatchOperation> operation)
    throws ProcessingException {

    final DatabaseUpdate update = DatabaseUpdate.build(Account.ENTITY, DatabaseFilter.build(Account.Attribute.ID.id, id, DatabaseFilter.Operator.EQ), modification(operation), Account.SCHEMA.operational.values());
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

    final DatabaseDelete delete = DatabaseDelete.build(Account.ENTITY, DatabaseFilter.build(Account.Attribute.ID.id, id, DatabaseFilter.Operator.EQ));
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
  // Method:   metadata
  /**
   ** Populate a {@link Metadata} from a {@link List} of database records.
   **
   ** @param  result             the data provider.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair} whose key is
   **                            the name of the database attribute and the
   **                            value the corresponding value.
   **
   ** @return                    the populated SCIM resource.
   **                            <br>
   **                            Allowed object is {@link UserResource}}.
   */
  private static Metadata metadata(final  Map<String, Object> result) {
    final Metadata metadata = new Metadata().resourceType(Account.SCHEMA.resource);

    // prevent bogus input
    if (CollectionUtility.empty(result))
      return metadata;

    metadata.version((String)result.get(Account.Attribute.VERSION.id));
    metadata.created(Calendar.getInstance());
    metadata.created().setTime((Date)result.get(Account.Attribute.CREATED.id));
    metadata.modified(Calendar.getInstance());
    metadata.modified().setTime((Date)result.get(Account.Attribute.UPDATED.id));
    return metadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Populate a {@link UserResource} from a {@link List} of database records.
   **
   ** @param  resultSet          the data provider.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair} whose key is
   **                            the name of the database attribute and the
   **                            value the corresponding value.
   ** @param  collector          the SCIM resource to populate.
   **                            <br>
   **                            Allowed object is {@link UserResource}}.
   */
  private static UserResource transform(final List<Pair<String, Object>> result) {
     final UserResource resource = new UserResource();
    // prevent bogus input
    if (CollectionUtility.empty(result))
      return resource;

    final Name                    name       = new Name();
    final Metadata                metadata   = new Metadata().resourceType(Account.SCHEMA.resource);
    final UserExtension           extension  = new UserExtension();
    final EnterpriseUserExtension enterprise = new EnterpriseUserExtension();
    boolean changedName       = false;
    boolean changedEnterprise = false;
    boolean changedExtension  = false;
    for (Pair<String, Object> attribute : result) {
      final Account.Attribute which = Account.Attribute.from(attribute.tag);
      switch(which) {
        case ID           : resource.id((String)attribute.value);
                            break;
        case VERSION      : metadata.version((String)attribute.value);
                            break;
        case CREATED      : metadata.created(Calendar.getInstance());
                            metadata.created().setTime((Date)attribute.value);
                            break;
        case UPDATED      : metadata.modified(Calendar.getInstance());
                            metadata.modified().setTime((Date)attribute.value);
                            break;
        case STATUS       : resource.active("1".equals(attribute.value));
                            break;
        case USERNAME     : resource.userName((String)attribute.value);
                            break;
        case LASTNAME     : name.familyName((String)attribute.value);
                            changedName = true;
                            break;
        case FIRSTNAME    : name.givenName((String)attribute.value);
                            changedName = true;
                            break;
        case EMAIL        : resource.email(CollectionUtility.list(new Email().value((String)attribute.value)));
                            break;
        case PHONE        : resource.phoneNumber(CollectionUtility.list(new PhoneNumber().value((String)attribute.value)));
                            break;
        case ORGANIZATION : enterprise.organization((String)attribute.value);
                            changedEnterprise = true;
                            break;
        case DIVISION     : enterprise.division((String)attribute.value);
                            changedEnterprise = true;
                            break;
        case DEPARTMENT   : enterprise.department((String)attribute.value);
                            changedEnterprise = true;
                            break;
        case VALIDFROM    : extension.validFrom((Date)attribute.value);
                            changedExtension = true;
                            break;
        case VALIDTHROUGH : extension.validTo((Date)attribute.value);
                            changedExtension = true;
                            break;
      }
    }
    resource.metadata(metadata);
    if (changedName)
      resource.name(name);
    if (changedEnterprise)
      resource.extension(enterprise);
    if (changedExtension)
      resource.extension(extension);
    return resource;
  }

  private static List<Pair<String, DatabaseParameter>> transform(final UserResource resource) {
    final List<Pair<String, DatabaseParameter>> payload = new ArrayList<Pair<String, DatabaseParameter>>();
    final Name                           name       = resource.name();
    final List<Email>                    email      = resource.email();
    final List<PhoneNumber>              phone      = resource.phoneNumber();
    final UserExtension                  extension  = resource.extension(UserExtension.class);
    final EnterpriseUserExtension        enterprise = resource.extension(EnterpriseUserExtension.class);
    // iterate over all permissible attributes defined for an account but add to
    // the payload only those that are not operational attributes
    for (Account.Attribute cursor : Account.Attribute.values()) {
      DatabaseParameter parameter = null;
      switch (cursor) {
        case STATUS       : parameter = DatabaseParameter.build(resource.active() ? "1" : "0", Types.CHAR);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case USERNAME     : parameter = DatabaseParameter.build(resource.userName(), Types.VARCHAR);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case LASTNAME     : parameter = name == null ? null : DatabaseParameter.build(name.familyName(), Types.VARCHAR);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case FIRSTNAME    : parameter = name == null ? null : DatabaseParameter.build(name.givenName(), Types.VARCHAR);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case EMAIL        : parameter = email == null ? null : DatabaseParameter.build(email.get(0).value(), Types.VARCHAR);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case PHONE        : parameter = email == null ? null : DatabaseParameter.build(phone.get(0).value(), Types.VARCHAR);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case ORGANIZATION : parameter = enterprise == null ? null : DatabaseParameter.build(enterprise.organization(), Types.VARCHAR);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case DIVISION     : parameter = enterprise == null ? null : DatabaseParameter.build(enterprise.division(), Types.VARCHAR);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case DEPARTMENT   : parameter = enterprise == null ? null : DatabaseParameter.build(enterprise.department(), Types.VARCHAR);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case VALIDFROM    : parameter = extension == null ? null : DatabaseParameter.build(extension.validFrom(), Types.TIMESTAMP);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
        case VALIDTHROUGH : parameter = extension == null ? null : DatabaseParameter.build(extension.validTo(), Types.TIMESTAMP);
                            payload.add(Pair.of(cursor.id, parameter));
                            break;
      }
    }
    return payload;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modification
  /**
   ** Populate a {@link List} modifications from a {@link List} of operations.
   **
   ** @param  resultSet          the data provider.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair} whose key is
   **                            the name of the database attribute and the
   **                            value the corresponding value.
   ** @param  collector          the SCIM resource to populate.
   **                            <br>
   **                            Allowed object is {@link UserResource}}.
   */
  private static List<Pair<String, DatabaseParameter>> modification(final List<PatchOperation> operation)
    throws ProcessingException {

    final List<Pair<String, DatabaseParameter>> payload = new ArrayList<Pair<String, DatabaseParameter>>();
    try {
      // iterate over all permissible attributes defined for an account but add to
      // the payload only those that are not operational attributes
      for (PatchOperation cursor : operation) {
        final Path path = cursor.path();
        // single valued
        if (path.size() == 1) {
          final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(path.element(0).attribute());
          if (attribute == null)
            throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_TOKEN, path.element(0).attribute()));

          Object value;
          switch(attribute.value.intValue()) {
            case Types.BOOLEAN   : value = cursor.value(Boolean.class);
                                   break;
            case Types.INTEGER   : value = cursor.value(Integer.class);
                                   break;
            case Types.NUMERIC   : value = cursor.value(Long.class);
                                   break;
            case Types.DOUBLE    : value = cursor.value(Double.class);
                                   break;
            case Types.FLOAT     : value = cursor.value(Float.class);
                                   break;
            case Types.DECIMAL   : value = cursor.value(BigDecimal.class);
                                   break;
            case Types.DATE      :
            case Types.TIME      :
            case Types.TIMESTAMP : value = cursor.value(Date.class);
                                   break;
            case Types.VARCHAR   : value = cursor.value(String.class);
                                   break;
            default              : throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_TOKEN, path.element(0).attribute()));
          }
          payload.add(Pair.of(attribute.tag, DatabaseParameter.build(value, attribute.value.intValue())));
        }
      }
    }
    catch (JsonProcessingException e) {
      throw new ServerErrorException(new ErrorResponse(500).type("JSON").detail(e.getLocalizedMessage()), e);
    }
    return payload;
  }

  private static List<Pair<String, String>> returning(final Set<String> requested)
    throws ProcessingException {

    final List<Pair<String, String>>         returning = new ArrayList<Pair<String, String>>();
    final Map<String, Pair<String, Integer>> permitted = Account.SCHEMA.permitted;
    for (String cursor : requested) {
      final Pair<String, Integer> attribute = permitted.get(cursor);
      if (attribute == null)
        throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_TOKEN, cursor));

      final Pair<String, String> pair = Pair.of(attribute.tag, attribute.tag);
      if (!returning.contains(pair)) {
        returning.add(pair);
      }
    }
    return returning;
  }
}