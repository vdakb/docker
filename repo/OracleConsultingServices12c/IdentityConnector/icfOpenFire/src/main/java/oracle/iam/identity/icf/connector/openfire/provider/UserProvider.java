/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you enthered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   UserProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    UserProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.provider;

import java.util.Map;
import java.util.List;
import java.util.Base64;
import java.util.ArrayList;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.DateUtility;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseSelect;
import oracle.iam.identity.icf.dbms.DatabaseSearch;
import oracle.iam.identity.icf.dbms.DatabaseInsert;
import oracle.iam.identity.icf.dbms.DatabaseDelete;
import oracle.iam.identity.icf.dbms.DatabaseUpdate;
import oracle.iam.identity.icf.dbms.DatabaseParameter;
import oracle.iam.identity.icf.dbms.DatabaseException;

import oracle.iam.identity.icf.connector.openfire.Context;
import oracle.iam.identity.icf.connector.openfire.Provider;

import oracle.iam.identity.icf.connector.openfire.schema.User;
import oracle.iam.identity.icf.connector.openfire.schema.Member;
import oracle.iam.identity.icf.connector.openfire.schema.Entity;
import oracle.iam.identity.icf.connector.openfire.schema.SystemProperty;

////////////////////////////////////////////////////////////////////////////////
// final class UserProvider
// ~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The descriptor to handle account entities.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class UserProvider extends Provider<User> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The native name of the entity. */
  static final String ENTITY    = "ofuser";

  /** The native name of the unencrypted password attribute. */
  static final String PLAINTEXT = "plainpassword";

  /** The native name of the encrypted password attribute. */
  static final String ENCYRPTED = "encryptedpassword";

  /** The native name of the email attribute. */
  static final String EMAIL     = "email";

  /** The native name of the storedkey attribute. */
  static final String STOREDKEY = "storedkey";

  /** The native name of the serverkey attribute. */
  static final String SERVERKEY = "serverkey";

  /** The native name of the salt attribute. */
  static final String SALT      = "salt";

  /** The native name of the iterations attribute. */
  static final String ITERATION = "iterations";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private AdministratorProvider admin = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>UserProvider</code> that belongs to the specified
   ** {@link Context}.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  private UserProvider(final Context context)
    throws SystemException {

    // ensure inheritance
    super(context, ENTITY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (Provider)
  /**
   ** Reads one {@link User} from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link User} that matches the specified
   **                            filter criteria.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if the {@link User} could not be found or is
   **                            ambiguously defined.
   */
  @Override
  public final User lookup(final DatabaseFilter criteria)
    throws SystemException {

    final String method = "lookup";
    trace(method, Loggable.METHOD_ENTRY);
    final DatabaseSelect statement = DatabaseSelect.build(this.context, this.entity, criteria, this.entity.attribute());
    try {
      final List<Map<String, Object>> result = statement.execute(this.context.connect(), this.context.endpoint().timeOutResponse());
      if (result.size() == 0)
        throw DatabaseException.notFound(criteria.toString(), this.entity.id());
      else if (result.size() > 1)
        throw DatabaseException.ambiguous(criteria.toString(), this.entity.id());
      else
        return User.build(result.get(0));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select (Provider)
  /**
   ** Reads all {@link User}s from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link List} containing all {@link User}s
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link User}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public final List<User> select(final DatabaseFilter criteria)
    throws SystemException {

    final String method = "select";
    trace(method, Loggable.METHOD_ENTRY);
    final List<User>     collector = new ArrayList<User>();
    final DatabaseSelect statement = DatabaseSelect.build(this.context, this.entity, criteria, this.entity.attribute());
    try {
      final List<Map<String, Object>> result = statement.execute(this.context.connect(), this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result)
        collector.add(collectDependencies(User.build(cursor)));
    }
    finally {
      // reset the administrator system property so that the next call to this
      // method request it from scratch
      this.admin = null;
      trace(method, Loggable.METHOD_ENTRY);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (Provider)
  /**
   ** Reads all {@link User}s from the Service Provider that matches the
   ** specified filter criteria.
   ** <br>
   ** The returned collection is limited to the records between
   ** <code>startRow</code> and <code>lastRow</code> and can be less than
   ** <code>lastRow - startRow</code>.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  startRow           the start row of a page to fetch from the
   **                            Database Server.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  lastRow            the last row of a page to fetch from the
   **                            Database Server.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    a {@link List} containing all {@link User}s
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link User}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public List<User> search(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "search";
    trace(method, Loggable.METHOD_ENTRY);
    final List<User>              collector = new ArrayList<User>();
    final DatabaseSearch          statement = DatabaseSearch.build(this.context, this.entity, criteria, this.entity.attribute());
    final List<DatabaseParameter> parameter = statement.prepare(this.context.connect());
    try {
      final List<Map<String, Object>> result = statement.execute(parameter, startRow, lastRow, this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result)
        collector.add(collectDependencies(User.build(cursor)));
    }
    finally {
      // reset the administrator system property so that the next call to this
      // method request it from scratch
      this.admin = null;
      trace(method, Loggable.METHOD_EXIT);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (Provider)
  /**
   ** Build and executes instruction to create the provided {@link User} in
   ** the Service Provider.
   **
   ** @param  entity             the populated {@link User} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the {@link User} after the instructions
   **                            executed to create it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if the {@link User} could not be created.
   */
  @Override
  public final User create(final User entity)
    throws SystemException {

    if (exists(DatabaseFilter.build(this.entity.primary().get(0).id(), entity.uid(), DatabaseFilter.Operator.EQUAL)))
      // the user already exists since no exception, so:
      throw DatabaseException.conflict(entity.uid(), this.entity.id());

    final String method = "create";
    trace(method, Loggable.METHOD_ENTRY);
    // special attention is required for administrators due to such entities
    // are not based at the same table as the user to create itself
    final Object administrator = entity.remove(User.ADMIN);
    // evaluate the timestamp required to create the user entry
    final String pattern = Entity.convertTimestamp(DateUtility.now());
    try {
      // evaluate credentials
      credential(entity);
      // evaluate the audit attributes required to create the user entry
      entity.attribute(Entity.CREATED, pattern);
      entity.attribute(Entity.UPDATED, pattern);
      // create the statement lately to have the bindings populated correctly
      final DatabaseInsert statement = DatabaseInsert.build(this.context, this.entity, entity.keySet());
      // execute statement usualy by verifying the operation has inserted at
      // least one record
      statement.execute(this.context.connect(), entity, true, this.context.endpoint().timeOutResponse());

      // check if administrator permissions are granted
      if (Boolean.TRUE.equals(administrator))
        this.context.systemProperty(SystemProperty.ADMINISTRATOR, AdministratorProvider.build(this.context).assign(entity.uid()).post());
      // create all flag objects that associated with the user account after the
      // account itself was created
      final List<User.Flag> flag = entity.flag();
      if (flag != null && flag.size() > 0) {
        FlagProvider.build(this.context, entity).bulk(flag);
      }
      // create all property objects that associated with the user account after
      // the account itself was created
      final List<Entity.Property> property = entity.property();
      if (property != null && property.size() > 0) {
        PropertyProvider.user(this.context, entity).bulkCreate(entity.property());
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify  (Provider)
  /**
   ** Build and executes instruction to modify the provided {@link User} in
   ** the Service Provider.
   **
   ** @param  uid                the identifier of an {@link User} to modify
   **                            in the  Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the partialy populated {@link User} that are
   **                            part of the instruction to modify it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the {@link User} after the instructions
   **                            executed to modify it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException    if the {@link User} could not be modified.
   */
  @Override
  public final User modify(final String uid, final User entity)
    throws SystemException {

    final DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), uid, DatabaseFilter.Operator.EQUAL);
    if (!exists(filter))
      // the user does not exists since no exception, so:
      throw DatabaseException.notFound(uid, this.entity.id());

    final String method = "modify";
    trace(method, Loggable.METHOD_ENTRY);
    // special attention is required for administrators due to such entities
    // are not based at the same table as the user to create itself
    final Boolean administrator = (Boolean)entity.remove(User.ADMIN);
    // special attention also required for status flags due to such entities
    // are not based at the same table as the user to create itself
    final Boolean locked        = (Boolean)entity.remove(User.LOCKED);
    final boolean rename        = entity.containsKey(User.UID) && !uid.equals(entity.stringValue(User.UID));
    try {
      // evaluate credentials
      credential(entity);
      if (entity.keySet().size() > 0) {
        // evaluate the audit attributes required to update the user entry
        entity.attribute(User.UPDATED, Entity.convertTimestamp(DateUtility.now()));
        // create the statement lately to have the bindings populated correctly
        final DatabaseUpdate statement = DatabaseUpdate.build(this.context, this.entity, filter, entity.keySet());
        // execute statement usualy by verifying the operation has updated at
        // least one record
        statement.execute(this.context.connect(), entity, true, this.context.endpoint().timeOutResponse());
      }
      // check if administrator permissions are modified
      if (administrator != null) {
        if (administrator) {
          if (rename) {
            this.context.systemProperty(SystemProperty.ADMINISTRATOR, AdministratorProvider.build(this.context).revoke(uid).post());
          }
          this.context.systemProperty(SystemProperty.ADMINISTRATOR, AdministratorProvider.build(this.context).assign(entity.uid()).post());
        }
        else {
          this.context.systemProperty(SystemProperty.ADMINISTRATOR, AdministratorProvider.build(this.context).revoke(rename ? uid : entity.uid()).post());
        }
      }
      // check if account status needs to be modified
      if (locked != null) {
        final User.Flag    status   = User.Flag.build(User.Flag.LOCK);
        final FlagProvider provider = FlagProvider.build(this.context, entity);
        if (locked) {
          if (rename) {
            provider.modify(uid, status);
          }
          else {
            provider.create(status.start(Entity.convertTimestamp(DateUtility.now())));
          }
        }
        else {
          provider.delete(status);
        }
      }
      else if (rename) {
        final User.Flag    status   = User.Flag.build(User.Flag.LOCK);
        final FlagProvider provider = FlagProvider.build(this.context, entity);
        provider.modify(uid, status);
      }
      // ensure that the entity have the value required to update dependencies
      if (!entity.containsKey(User.UID))
        entity.uid(uid);
      // modify all property objects that associated with the user account after
      // the account itself was modified
      final List<Entity.Property> property = entity.property();
      if (property != null && property.size() > 0) {
        PropertyProvider.user(this.context, entity).bulkModify(uid, entity.property());
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (Provider)
  /**
   ** Build and executes instruction to delete the provided {@link User} in
   ** the Service Provider.
   **
   ** @param  entity             the partialy populated {@link User} that are
   **                            part of the instruction to delete it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @throws SystemException    if the {@link User} could not be deleted.
   */
  @Override
  public void delete(final User entity)
    throws SystemException {

    final String         uid    = entity.uid();
    final DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), uid, DatabaseFilter.Operator.EQUAL);
    if (!exists(filter))
      // the user does not exists since no exception was thrown, we have to
      // complain about the missing record
      throw DatabaseException.notFound(uid, this.entity.id());

    final String method = "delete";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // delete any flags that might be associated with the user account before
      // the user account itself will be deleted
      final FlagProvider flag = FlagProvider.build(this.context, entity);
      // passing null means all because we deleting the owning entity
      flag.delete(null);
      // delete any properties that might be associated with the user account
      // before the user account itself will be deleted
      final PropertyProvider.User property = PropertyProvider.user(this.context, entity);
      // passing null means all because we deleting the owning entity
      property.delete(null);
      // delete any memberships that might be associated with the user account
      // before the user account itself will be deleted
      final Provider<Member> member = new MemberProvider.User(this.context);
      member.delete(Member.build(entity));
      // special attention is required for administrators due to such entities
      // are not based on permissions
      final AdministratorProvider administrator = AdministratorProvider.build(this.context);
      if (administrator.granted(uid))
        this.context.systemProperty(SystemProperty.ADMINISTRATOR, administrator.revoke(uid).post());
      // now the user account is ready for delete
      final DatabaseDelete statement = DatabaseDelete.build(this.context, this.entity, filter);
      // execute statement usualy by verifying the operation has deleted at
      // least one record
      statement.execute(this.context.connect(), true, this.context.endpoint().timeOutResponse());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gropued by functionalities
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new instance of <code>UserProvider</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    an instance of <code>UserProvider</code>.
   **                            <br>
   **                            Possible object is <code>UserProvider</code>.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static UserProvider build(final Context context)
    throws SystemException {
    
    return new UserProvider(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Build a query to verify the existance of a {@link User} entity at the
   ** Service Provider.
   **
   ** @param  uid                the system identifier of the {@link User}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if a matching entity exists
   **                            for the <code>uid</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    if a user already exists.
   */
  public final boolean exists(final String uid)
    throws SystemException {
    
    return super.exists(DatabaseFilter.build(this.entity.primary().get(0).id(), uid, DatabaseFilter.Operator.EQUAL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Generates salted and encrypted password.
   **
   ** @param  entity             the {@link User} to generate the credentials
   **                            for.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the password wrapper providing access to the
   **                            computed values.
   **                            <br>
   **                            Possible object is
   **                            <code>PasswordProvider</code>.
   **
   ** @throws SystemException    if internal error occur.
   */
  private void credential(final User entity)
    throws SystemException {
    
    // evaluate salted and encrypt password
    final PasswordProvider password =
      entity.containsKey(User.PASSWORD_PLAINTEXT)
    ? PasswordProvider.obfuscate(this.context, entity.passwordPlaintext())
    : entity.containsKey(User.PASSWORD_ENCRYPTED) ? PasswordProvider.obfuscate(this.context, entity.passwordEncrypted()) : null
    ;
    if (password != null) {
      final Base64.Encoder encoder = Base64.getEncoder();
      entity.attribute(User.SALT,               encoder.encodeToString(password.shaker));
      entity.attribute(User.PASSWORD_ENCRYPTED, password.encrypted);
      entity.attribute(User.ITERATION,          password.iteration);
      entity.attribute(User.STOREDKEY,          encoder.encodeToString(password.stored));
      entity.attribute(User.SERVERKEY,          encoder.encodeToString(password.server));
    }
    else {
      entity.remove(User.SALT);
      entity.remove(User.PASSWORD_ENCRYPTED);
      entity.remove(User.ITERATION);
      entity.remove(User.STOREDKEY);
      entity.remove(User.SERVERKEY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectDependencies
  /**
   ** Reads all dependend information like flags or properties that belongs to
   ** the specified {@link User} entity from the Service Provider.
   **
   ** @param  entity             a {@link User} entity.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the same {@link User} entity with collected
   **                            dependencies.
   **                            <br>
   **                            Possible object is {@link User}.
   */
  private User collectDependencies(final User entity)
    throws SystemException {
    
    // lazy init the system properties for administrators to avoid instantiation
    // and parsing every time a user is fetched fro the database
    if (this.admin == null)
      this.admin = AdministratorProvider.build(this.context);
    entity.administrator(admin.granted(entity.uid()));

    // take care about associated flags
    final FlagProvider flag = FlagProvider.build(this.context, entity);
    // no further limits required to populate the flags for the user entity
    entity.flag(flag.select(DatabaseFilter.NOP));

    // take care about associated properties
    final PropertyProvider property = PropertyProvider.user(this.context, entity);
    // no further limits required to populate the properties for the user entity
    entity.property(property.select(DatabaseFilter.NOP));
    
    return entity;
  }
}