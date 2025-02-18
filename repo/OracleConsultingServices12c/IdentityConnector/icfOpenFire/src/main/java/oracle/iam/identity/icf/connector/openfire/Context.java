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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   Context.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire;

import java.util.List;
import java.util.Date;

import java.sql.SQLException;

import oracle.iam.identity.icf.connector.openfire.schema.Entity;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.dbms.DatabaseError;
import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseSchema;
import oracle.iam.identity.icf.dbms.DatabaseContext;
import oracle.iam.identity.icf.dbms.DatabaseMessage;
import oracle.iam.identity.icf.dbms.DatabaseEndpoint;
import oracle.iam.identity.icf.dbms.DatabaseException;

import oracle.iam.identity.icf.resource.DatabaseBundle;

import oracle.iam.identity.icf.connector.openfire.schema.User;
import oracle.iam.identity.icf.connector.openfire.schema.Room;
import oracle.iam.identity.icf.connector.openfire.schema.Group;
import oracle.iam.identity.icf.connector.openfire.schema.Member;
import oracle.iam.identity.icf.connector.openfire.schema.RoomMate;
import oracle.iam.identity.icf.connector.openfire.schema.SystemProperty;

import oracle.iam.identity.icf.connector.openfire.provider.UserProvider;
import oracle.iam.identity.icf.connector.openfire.provider.RoomProvider;
import oracle.iam.identity.icf.connector.openfire.provider.GroupProvider;
import oracle.iam.identity.icf.connector.openfire.provider.MemberProvider;
import oracle.iam.identity.icf.connector.openfire.provider.PropertyProvider;
import oracle.iam.identity.icf.connector.openfire.provider.RoomMateProvider;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** The <code>Context</code> wraps the JDBC connection.
 ** <p>
 ** Define the test method meaning the wrapped connection is still valid.
 ** <br>
 ** Defines come useful method to work with prepared statements.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends DatabaseContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String SCHEMA = "/META-INF/dbs/openfire.json";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  DatabaseSchema      schema = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Context</code> which is associated with the specified
   ** {@link DatabaseEndpoint} for configuration purpose.
   **
   ** @param  endpoint           the {@link DatabaseEndpoint}
   **                            <code>IT Resource</code> definition where this
   **                            connector is associated with.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   */
  private Context(final DatabaseEndpoint endpoint) {
    // ensure inherinstance
    super(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Context</code> which is associated with
   ** the specified {@link DatabaseEndpoint} as the logger.
   **
   ** @param  endpoint           the {@link DatabaseEndpoint} IT Resource
   **                            definition where this connector is associated
   **                            with.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   **
   ** @return                    the context.
   **                            <br>
   **                            Possible object is {@link Context}.
   */
  public static Context build(final DatabaseEndpoint endpoint) {
    return new Context(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   test
  /**
   ** Determines if the underlying JDBC {@link java.sql.Connection} is valid.
   **
   ** @throws RuntimeException   if the underlying JDBC
   **                            {@link java.sql.Connection} is not valid
   **                            otherwise do nothing.
   */
  public void test() {
    final String method = "test";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // obtain a connection from the pool
      connect();
      unwrap().isValid(endpoint().timeOutConnect());
      debug(method, DatabaseBundle.string(DatabaseMessage.CONNECTION_ALIVE));
    }
    catch (SQLException e) {
      try {
        rollback();
      }
      catch (SystemException x) {
        // intentionally ignored
        ;
      }
      fatal(method, e);
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      Main.propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Return the schema upon request.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object {@link DatabaseSchema}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public DatabaseSchema schema()
    throws SystemException {

    if (this.schema == null) {
      // locate the schema declaration at the class path by leveraging the class
      // loader of the bundle where the library is part of.
      this.schema = DatabaseSchema.load(endpoint(), Context.class.getResourceAsStream(SCHEMA));
    }
    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userSearch
  /**
   ** Reads all {@link User}s from the Service Provider that matches the
   ** specified filter criteria.
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
   ** @return                    a {@link List} containing the available values
   **                            for the specified query string.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link User}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final List<User> userSearch(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "userSearch";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<User> provider = UserProvider.build(this);
    try {
      // perform operation
      return provider.search(criteria, startRow, lastRow);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userCreate
  /**
   ** Build and executes the statement to create the provided user resource at
   ** the Service Provider.
   **
   ** @param  user               the {@link User} of named-value pairs that are
   **                            part of this user entity operation.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the created {@link User}.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException   if an error occurs.
   */
  public User userCreate(final User user)
    throws SystemException {

    final String method = "userCreate";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<User> provider = UserProvider.build(this);
    try {
      // perform operation
      provider.create(user);
      commit();
      return user;
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userModify
  /**
   ** Build and executes the statement to modify the provided user resource at
   ** the Service Provider.
   **
   ** @param  uid                the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  user               the {@link User} of named-value pairs that are
   **                            part of this user entity operation.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the modified {@link User}.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws SystemException   if an error occurs.
   */
  public User userModify(final String uid, final User user)
    throws SystemException {

    final String method = "userModify";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<User> provider = UserProvider.build(this);
    try {
      // perform operation
      provider.modify(uid, user);
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return user;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userDelete
  /**
   ** Deletes a known user resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void userDelete(final String id)
    throws SystemException {

    final String method = "userDelete";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<User> provider = UserProvider.build(this);
    try {
      // perform operation
      provider.delete(User.build(id));
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userAssign
  /**
   ** Creates a membership association to a {@link Group} resource at the
   ** Service Provider.
   **
   ** @param  entity             the populated {@link Member} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Member}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void userAssign(final Member entity)
    throws SystemException {

    final String method = "userAssign";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<Member> provider = MemberProvider.user(this);
    try {
      // perform operation
      provider.create(entity);
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userRevoke
  /**
   ** Deletes a membership association from a {@link Group} resource at the
   ** Service Provider.
   **
   ** @param  entity             the partialy populated {@link Member} that are
   **                            part of the instruction to delete it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Member}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void userRevoke(final Member entity)
    throws SystemException {

    final String method = "userRevoke";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<Member> provider = MemberProvider.user(this);
    try {
      // perform operation
      provider.delete(entity);
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userAssign
  /**
   ** Creates a occupancy association to a {@link Room} resource at the
   ** Service Provider.
   **
   ** @param  entity             the populated {@link RoomMate} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link RoomMate}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void userAssign(final RoomMate entity)
    throws SystemException {

    final String method = "userAssign";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<RoomMate> provider = RoomMateProvider.user(this);
    try {
      // perform operation
      provider.create(entity);
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userRevoke
  /**
   ** Deletes a occupancy association from a {@link Room} resource at the
   ** Service Provider.
   **
   ** @param  entity             the partialy populated {@link RoomMate} that
   **                            are part of the instruction to delete it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link RoomMate}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void userRevoke(final RoomMate entity)
    throws SystemException {

    final String method = "userRevoke";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<RoomMate> provider = RoomMateProvider.user(this);
    try {
      // perform operation
      provider.delete(entity);
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupSearch
  /**
   ** Reads all identifiers from the data dictionary that belongs to groups.
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
   ** @return                    a {@link List} containing the available values
   **                            for the specified query string.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Group}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final List<Group> groupSearch(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "groupSearch";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<Group> provider = GroupProvider.build(this);
    try {
      // perform operation
      return provider.search(criteria, startRow, lastRow);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupCreate
  /**
   ** Build and executes the request to create the provided new group resource
   ** at the Service Provider.
   **
   ** @param  entity             the {@link Group} of named-value pairs that are
   **                            part of this user entity operation.
   **                            <br>
   **                            Allowed object is {@link Group}.
   **
   ** @throws SystemException   if an error occurs.
   */
  public void groupCreate(final Group entity)
    throws SystemException {

    final String method = "groupCreate";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<Group> provider = GroupProvider.build(this);
    try {
      // perform operation
      provider.create(
        // setup the default properties required for a group to be visisble in
        // the admin UI
        entity
          .add("sharedRoster.showInRoster", "nobody")
          .add("sharedRoster.displayName",  null)
          .add("sharedRoster.groupList",    null)
      );
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupDelete
  /**
   ** Deletes a known group resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void groupDelete(final String id)
    throws SystemException {

    final String method = "groupDelete";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<Group> provider = GroupProvider.build(this);
    try {
      // perform operation
      provider.delete(Group.build(id));
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupAssign
  /**
   ** Creates a membership association to a {@link User} resource at the Service
   ** Provider.
   **
   ** @param  entity             the populated {@link Member} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Member}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void groupAssign(final Member entity)
    throws SystemException {

    final String method = "groupAssign";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<Member> provider = MemberProvider.group(this);
    try {
      // perform operation
      provider.create(entity);
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupRevoke
  /**
   ** Deletes a membership association of a {@link User} resource at the Service
   ** Provider.
   **
   ** @param  entity             the partialy populated {@link Member} that are
   **                            part of the instruction to delete it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Member}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void groupRevoke(final Member entity)
    throws SystemException {

    final String method = "groupRevoke";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<Member> provider = MemberProvider.group(this);
    try {
      // perform operation
      provider.delete(entity);
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roomSearch
  /**
   ** Reads all identifiers from the data dictionary that belongs to chat rooms.
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
   ** @return                    a {@link List} containing the available values
   **                            for the specified query string.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Room}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final List<Room> roomSearch(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "roomSearch";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<Room> provider = RoomProvider.build(this);
    try {
      // perform operation
      return provider.search(criteria, startRow, lastRow);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roomAssign
  /**
   ** Creates a occupancy association to a {@link User} resource at the
   ** Service Provider.
   **
   ** @param  entity             the populated {@link RoomMate} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link RoomMate}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void roomAssign(final RoomMate entity)
    throws SystemException {

    final String method = "roomAssign";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<RoomMate> provider = RoomMateProvider.room(this);
    try {
      // perform operation
      provider.create(entity);
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roomRevoke
  /**
   ** Deletes a occupancy association from a {@link User} resource at the
   ** Service Provider.
   **
   ** @param  entity             the partialy populated {@link RoomMate} that
   **                            are part of the instruction to delete it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link RoomMate}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void roomRevoke(final RoomMate entity)
    throws SystemException {

    final String method = "roomRevoke";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<RoomMate> provider = RoomMateProvider.room(this);
    try {
      // perform operation
      provider.delete(entity);
      commit();
    }
    catch (SystemException e) {
      // in case of an error rollback the entire transactions done so far
      rollback();
      // rethrow exception
      throw e;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchTime
  /**
   ** Returns the filter that has to be applied on an incremental search.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The value passed in is usually define in the operational options of a
   ** search operation of the connector. Due to the limits of ICF there isn't
   ** any capability to transfer {@link Date} values. Only {@link Long} values
   ** can pass the serialization interfaces.
   ** <br>
   ** For that reason this method accepts a {@link Long} value.
   **
   ** @param  timestamp          the time after that an account in the target
   **                            system should be created or modified to be
   **                            included in the returning collection.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public final DatabaseFilter searchTime(final Long timestamp) {
    final String pattern = Entity.convertTimestamp(timestamp);
    return DatabaseFilter.build(
      DatabaseFilter.build("creationdate",     pattern, DatabaseFilter.Operator.GREATER_THAN)
    , DatabaseFilter.build("modificationdate", pattern, DatabaseFilter.Operator.GREATER_THAN)
    , DatabaseFilter.Operator.OR
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adminstratorProperty
  /**
   ** Reads all configuration overrides the application provides.
   **
   ** @return                    a {@link SystemProperty} populated with value
   **                            of <code>admin.authorizedJIDs</code>.
   **                            <br>
   **                            Possible object is {@link SystemProperty}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final SystemProperty adminstratorProperty()
    throws SystemException {

    // perform the database operation to fetch the desired property
    return systemProperty(SystemProperty.ADMINISTRATOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperty
  /**
   ** Reads a configuration the application provides for the specified
   ** <code>name</code>.
   **
   ** @param  name               the name of the desired property.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value to return if
   **                            <code>name</code> is not mapped as a
   **                            {@link SystemProperty}.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the value of a {@link SystemProperty} mapped
   **                            at <code>name</code> or
   **                            <code>defaultValue</code> if no
   **                            {@link SystemProperty} is mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final boolean systemProperty(final String name, final boolean defaultValue)
    throws SystemException {

    final SystemProperty property = systemProperty(name);
    return property == null ? defaultValue : Boolean.valueOf(property.value()).booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperty
  /**
   ** Reads a configuration the application provides for the specified
   ** <code>name</code>.
   **
   ** @param  name               the name of the desired property.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value to return if
   **                            <code>name</code> is not mapped as a
   **                            {@link SystemProperty}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the value of a {@link SystemProperty} mapped
   **                            at <code>name</code> or
   **                            <code>defaultValue</code> if no
   **                            {@link SystemProperty} is mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final int systemProperty(final String name, final int defaultValue)
    throws SystemException {

    try {
      return Integer.valueOf(systemProperty(name).value()).intValue();
    }
    catch (DatabaseException e) {
      if (DatabaseError.OBJECT_NOT_EXISTS.equals(e.code())) {
        return defaultValue;
      }
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperty
  /**
   ** Reads a configuration the application provides for the specified
   ** <code>name</code>.
   **
   ** @param  name               the name of the desired property.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value to return if
   **                            <code>name</code> is not mapped as a
   **                            {@link SystemProperty}.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the value of a {@link SystemProperty} mapped
   **                            at <code>name</code> or
   **                            <code>defaultValue</code> if no
   **                            {@link SystemProperty} is mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is <code>long</code>.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final long systemProperty(final String name, final long defaultValue)
    throws SystemException {

    try {
      return Long.valueOf(systemProperty(name).value()).longValue();
    }
    catch (DatabaseException e) {
      if (DatabaseError.OBJECT_NOT_EXISTS.equals(e.code())) {
        return defaultValue;
      }
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperty
  /**
   ** Reads a configuration the application provides for the specified
   ** <code>name</code>.
   **
   ** @param  name               the name of the desired property.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value to return if
   **                            <code>name</code> is not mapped as a
   **                            {@link SystemProperty}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of a {@link SystemProperty} mapped
   **                            at <code>name</code> or
   **                            <code>defaultValue</code> if no
   **                            {@link SystemProperty} is mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final String systemProperty(final String name, final String defaultValue)
    throws SystemException {

    try {
      return systemProperty(name).value();
    }
    catch (DatabaseException e) {
      if (DatabaseError.OBJECT_NOT_EXISTS.equals(e.code())) {
        return defaultValue;
      }
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperty
  /**
   ** Reads a configuration property the application provides.
   **
   ** @param  name               the name of the desired property.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link SystemProperty} containing the
   **                            available values for the specified query string.
   **                            <br>
   **                            Possible object is {@link SystemProperty}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final SystemProperty systemProperty(final String name)
    throws SystemException {

    final String method = "systemProperty";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<SystemProperty> provider = PropertyProvider.system(this);
    try {
      return provider.lookup(DatabaseFilter.build(Entity.Property.NAME, name, DatabaseFilter.Operator.EQUAL));
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperty
  /**
   ** Writes a configuration property the application provides.
   **
   ** @param  identifier         the identifier of a {@link SystemProperty} to
   **                            modify in the  Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  property           the property to write.
   **                            <br>
   **                            Allowed object is {@link SystemProperty}.
   **
   ** @return                    the {@link SystemProperty} containing the
   **                            available values after persisted.
   **                            <br>
   **                            Possible object is {@link SystemProperty}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final SystemProperty systemProperty(final String identifier, final SystemProperty property)
    throws SystemException {

    final String method = "systemProperty";
    trace(method, Loggable.METHOD_ENTRY);
    final Provider<SystemProperty> provider = PropertyProvider.system(this);
    try {
      return provider.modify(identifier, property);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}