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

    File        :   PropertyProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PropertyProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.provider;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseSelect;
import oracle.iam.identity.icf.dbms.DatabaseSearch;
import oracle.iam.identity.icf.dbms.DatabaseInsert;
import oracle.iam.identity.icf.dbms.DatabaseUpdate;
import oracle.iam.identity.icf.dbms.DatabaseDelete;
import oracle.iam.identity.icf.dbms.DatabaseParameter;
import oracle.iam.identity.icf.dbms.DatabaseException;

import oracle.iam.identity.icf.connector.openfire.Context;
import oracle.iam.identity.icf.connector.openfire.Provider;

import oracle.iam.identity.icf.connector.openfire.schema.Entity;
import oracle.iam.identity.icf.connector.openfire.schema.SystemProperty;

////////////////////////////////////////////////////////////////////////////////
// abstract class PropertyProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The descriptor to handle entity property relation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class PropertyProvider extends Provider<Entity.Property> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The native name of the identifier attribute */
  protected static final String NAME      = "name";

  /** The native name of the password attribute */
  protected static final String VALUE     = "propvalue";

  /** The native name of the vector attribute. */
  protected static final String VECTOR    = "iv";

  /** The native name of the encrypted attribute. */
  protected static final String ENCRYPTED = "encrypted";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /** The relation to the superordinated {@link Entity} */
  final Entity relation;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class System
  // ~~~~~ ~~~~~~
  /**
   ** Default implementation of the <code>PropertyProvider</code> interface for
   ** <code>System</code>s, which reads and writes data from the
   ** <code>ofProperty</code> database table.
   ** <br>
   ** <b>Warning</b>:
   ** <br>
   ** In virtually all cases a user property provider should not be used
   ** directly. Instead, use the {@link Entity.Property} returned by
   ** {@code User#property()} to create, read, update or delete user properties.
   ** <br>
   ** Failure to do so is likely to result in inconsistent data behavior and
   ** race conditions. Direct access to the user property provider is only
   ** provided for special-case logic.
   */
  public static class System extends Provider<SystemProperty> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The native name of the entity. */
    static final String ENTITY = "ofproperty";

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>System</code> <code>PropertyProvider</code> that
     ** belongs to the specified {@link Context}.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new System(Context)" and enforces use of the public static methods of
     ** enclosing class below.
     **
     ** @param  context          the {@link Context} where this relation belongs
     **                          to.
     **                          <br>
     **                          Allowed object is {@link Context}.
     **
     ** @throws SystemException  if the schema operation fails.
     */
    private System(final Context context)
      throws SystemException {

      super(context, ENTITY);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   lookup (Provider)
    /**
     ** Reads one {@link SystemProperty} from the Service Provider that matches
     ** the specified filter criteria.
     **
     ** @param  criteria         the filter criteria to build for the SQL
     **                          statement.
     **                          <br>
     **                          Allowed object is {@link DatabaseFilter}.
     **
     ** @return                  a {@link SystemProperty} that matches the
     **                          specified filter criteria.
     **                          <br>
     **                          Possible object is {@link SystemProperty}.
     **
     ** @throws SystemException  if the {@link SystemProperty} could not be
     **                          found or is ambiguously defined.
     */
    @Override
    public final SystemProperty lookup(final DatabaseFilter criteria)
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
        else {
          Map<String, Object> property = result.get(0);
          // if endcrypted then decrpyt
          if (property.get(ENCRYPTED) != null) {
            // intentionally left blank due to why we should do
            // or will it be better to throw an exception that we don't want to
            // handle properties that are security related somehow
            ;
          }
          return SystemProperty.build(property);
        }
      }
      finally {
        trace(method, Loggable.METHOD_ENTRY);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: select (Provider)
    /**
     ** Reads all {@link SystemProperty}s from the Service Provider that
     ** matches the specified filter criteria.
     **
     ** @param  criteria         the filter criteria to build for the SQL
     **                          statement.
     **                          <br>
     **                          Allowed object is {@link DatabaseFilter}.
     **
     ** @return                  a {@link List} containing all
     **                          {@link SystemProperty}s that matches the
     **                          specified filter criteria.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link SystemProperty}.
     **
     ** @throws SystemException  in case an error does occur.
     */
    @Override
    public final List<SystemProperty> select(final DatabaseFilter criteria)
      throws SystemException {

      final String method = "select";
      trace(method, Loggable.METHOD_ENTRY);
      final List<SystemProperty>     collector = new ArrayList<SystemProperty>();
      final DatabaseSelect statement = DatabaseSelect.build(this.context, this.entity, criteria, this.entity.attribute());
      try {
        final List<Map<String, Object>> result = statement.execute(this.context.endpoint().timeOutResponse());
        for (Map<String, Object> cursor : result)
          collector.add(SystemProperty.build(cursor));
      }
      finally {
        trace(method, Loggable.METHOD_ENTRY);
      }
      return collector;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: search (Provider)
    /**
     ** Reads all {@link SystemProperty}s from the Service Provider that
     ** matches the specified filter criteria.
     ** <br>
     ** The returned collection is limited to the records between
     ** <code>startRow</code> and <code>lastRow</code> and can be less than
     ** <code>lastRow - startRow</code>.
     **
     ** @param  criteria         the filter criteria to build for the SQL
     **                          statement.
     **                          <br>
     **                          Allowed object is {@link DatabaseFilter}.
     ** @param  startRow         the start row of a page to fetch from the
     **                          Database Server.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     ** @param  lastRow          the last row of a page to fetch from the
     **                          Database Server.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     **
     ** @return                  a {@link List} containing all
     **                          {@link SystemProperty}s that matches the
     **                          specified filter criteria.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link SystemProperty}.
     **
     ** @throws SystemException  in case an error does occur.
     */
    @Override
    public List<SystemProperty> search(final DatabaseFilter criteria, final long startRow, final long lastRow)
      throws SystemException {

      final String method = "search";
      trace(method, Loggable.METHOD_ENTRY);
      final List<SystemProperty>    collector = new ArrayList<SystemProperty>();
      final DatabaseSearch          statement = DatabaseSearch.build(this.context, this.entity, criteria, this.entity.attribute());
      final List<DatabaseParameter> parameter = statement.prepare(this.context.connect());
      try {
        final List<Map<String, Object>> result = statement.execute(parameter, startRow, lastRow, this.context.endpoint().timeOutResponse());
        for (Map<String, Object> cursor : result)
          collector.add(SystemProperty.build(cursor));
      }
      finally {
        trace(method, Loggable.METHOD_EXIT);
      }
      return collector;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Provider)
    /**
     ** Build and executes instruction to create the provided
     ** {@link SystemProperty} in the Service Provider.
     **
     ** @param  entity           the populated {@link SystemProperty} that are
     **                          part of the instruction to create the Service
     **                          Provider.
     **                          <br>
     **                          Allowed object is {@link SystemProperty}.
     **
     ** @return                  the {@link SystemProperty} after the
     **                          instructions executed to create it in the
     **                          Service Provider.
     **                          <br>
     **                          Possible object is {@link SystemProperty}.
     **
     ** @throws SystemException  if the {@link User} could not be created.
     */
    @Override
    public SystemProperty create(final SystemProperty entity)
      throws SystemException {

      return entity;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: modify  (Provider)
    /**
     ** Build and executes instruction to modify the provided
     ** {@link SystemProperty} in the Service Provider.
     **
     ** @param  entity           the partialy populated {@link SystemProperty}
     **                          that are part of the instruction to modify it
     **                          in the Service Provider.
     **                          <br>
     **                          Allowed object is {@link SystemProperty}.
     **
     ** @return                  the {@link SystemProperty} after the
     **                          instructions executed to modify it in the
     **                          Service Provider.
     **                          <br>
     **                          Possible object is {@link SystemProperty}.
     **
     ** @throws SystemException  if the {@link SystemProperty} could not be
     **                          modified.
     */
    @Override
    public final SystemProperty modify(final String identifier, final SystemProperty entity)
      throws SystemException {

      final DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), identifier, DatabaseFilter.Operator.EQUAL);
      if (!exists(filter))
        // the property does not exists since no exception was thrown, we have
        // to complain about the missing record
        throw DatabaseException.notFound(entity.name(), this.entity.id());

      final String method = "modify";
      trace(method, Loggable.METHOD_ENTRY);
      final DatabaseUpdate statement = DatabaseUpdate.build(this.context, this.entity, filter, entity.keySet());
      try {
        // execute statement usualy by verifying the operation has updated at
        // least one record
        statement.execute(this.context.connect(), entity, true, this.context.endpoint().timeOutResponse());
      }
      finally {
        trace(method, Loggable.METHOD_EXIT);
      }
      return entity;
    }

    /////////////////////////////////////////////////////////////////////////////
    // Method: delete (Provider)
    /**
     ** Build and executes instruction to delete the provided
     ** {@link SystemProperty} in the Service Provider.
     **
     ** @param  entity           the partialy populated {@link SystemProperty}
     **                          that are part of the instruction to delete it
     **                          in the Service Provider.
     **                          <br>
     **                          Allowed object is {@link SystemProperty}.
     **
     ** @throws SystemException  if the {@link User} could not be deleted.
     */
    @Override
    public void delete(final SystemProperty entity)
      throws SystemException {

      final DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), entity.name(), DatabaseFilter.Operator.EQUAL);
      if (!exists(filter))
        // the property does not exists since no exception was thrown, we have
        // to complain about the missing record
        throw DatabaseException.notFound(entity.name(), this.entity.id());

      final String method = "delete";
      trace(method, Loggable.METHOD_ENTRY);
      final DatabaseDelete statement = DatabaseDelete.build(this.context, this.entity, filter);
      try {
        // execute statement usualy by verifying the operation has deleted at
        // least one record
        statement.execute(this.context.connect(), true, this.context.endpoint().timeOutResponse());
      }
      finally {
        trace(method, Loggable.METHOD_EXIT);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class User
  // ~~~~~ ~~~~
  /**
   ** Default implementation of the <code>PropertyProvider</code> interface for
   ** <code>User</code>s, which reads and writes data from the
   ** <code>ofUserProp</code> database table.
   ** <br>
   ** This implementation will not explicitly verify if a user exists, when
   ** operating on its properties.
   ** <br>
   ** The methods of this implementation will not throw an exception if the
   ** user does not exists.
   ** <br>
   ** <b>Warning</b>:
   ** <br>
   ** In virtually all cases a user property provider should not be used
   ** directly. Instead, use the {@link Entity.Property} returned by
   ** {@code User#property()} to create, read, update or delete user properties.
   ** <br>
   ** Failure to do so is likely to result in inconsistent data behavior and
   ** race conditions. Direct access to the user property provider is only
   ** provided for special-case logic.
   */
  public static class User extends PropertyProvider {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The native name of the entity. */
    static final String ENTITY = "ofuserprop";

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>User</code> <code>PropertyProvider</code> that
     ** belongs to the specified {@link Context}.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new User(Context, String)" and enforces use of the public static
     ** methods of enclosing class below.
     **
     ** @param  context          the {@link Context} where this relation belongs
     **                          to.
     **                          <br>
     **                          Allowed object is {@link Context}.
     ** @param  user             the identifier of a superordinated entity to
     **                          to join with.
     **                          <br>
     **                          Allowed object is {@link Entity}.
     **
     ** @throws SystemException  if the schema operation fails.
     */
    private User(final Context context, final Entity user)
      throws SystemException {

      // ensure inheritance
      super(context, ENTITY, user);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Group
  // ~~~~~ ~~~~~
  /**
   ** Default implementation of the {@link PropertyProvider} interface for
   ** groups, which reads and writes data from the <code>ofGroupProp</code>
   ** database table.
   ** <br>
   ** This implementation will not explicitly verify if a group exists, when
   ** operating on its properties.
   ** <br>
   ** The methods of this implementation will not throw an exception if the
   ** group does not exists.
   ** <br>
   ** <b>Warning</b>:
   ** <br>
   ** In virtually all cases a group property provider should not be used
   ** directly. Instead, use the {@link Entity.Property} returned by
   ** {@code Group#property()} to create, read, update or delete group
   ** properties.
   ** <br>
   ** Failure to do so is likely to result in inconsistent data behavior and
   ** race conditions. Direct access to the user property provider is only
   ** provided for special-case logic.
   */
  public static class Group extends PropertyProvider {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The native name of the entity. */
    static final String ENTITY = "ofgroupprop";

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Group</code> <code>PropertyProvider</code> that
     ** belongs to the specified {@link Context}.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Group(Context, String)" and enforces use of the public static
     ** methods of enclosing class below.
     **
     ** @param  context          the {@link Context} where this relation belongs
     **                          to.
     **                          <br>
     **                          Allowed object is {@link Context}.
     ** @param  group            the identifier of a superordinated entity to
     **                          to join with.
     **                          <br>
     **                          Allowed object is {@link Entity}.
     **
     ** @throws SystemException  if the schema operation fails.
     */
    private Group(final Context context, final Entity group)
      throws SystemException {

      // ensure inheritance
      super(context, ENTITY, group);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Room
  // ~~~~~ ~~~~
      /**
       ** Default implementation of the <code>PropertyProvider</code> interface for
       ** <code>Room</code>s, which reads and writes data from the
       ** <code>ofRoomProp</code> database table.
       ** <br>
       ** This implementation will not explicitly verify if a user exists, when
       ** operating on its properties.
       ** <br>
       ** The methods of this implementation will not throw an exception if the
       ** user does not exists.
       ** <br>
       ** <b>Warning</b>:
       ** <br>
       ** In virtually all cases a user property provider should not be used
       ** directly. Instead, use the {@link Entity.Property} returned by
       ** {@code Room#property()} to create, read, update or delete user properties.
       ** <br>
       ** Failure to do so is likely to result in inconsistent data behavior and
       ** race conditions. Direct access to the user property provider is only
       ** provided for special-case logic.
       */
  public static class Room extends PropertyProvider {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The native name of the entity. */
    static final String ENTITY = "ofmucroomprop";

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Room</code> <code>PropertyProvider</code> that
     ** belongs to the specified {@link Context}.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Room(Context, String)" and enforces use of the public static
     ** methods of enclosing class below.
     **
     ** @param  context          the {@link Context} where this relation belongs
     **                          to.
     **                          <br>
     **                          Allowed object is {@link Context}.
     ** @param  room             the identifier of a superordinated entity to
     **                          to join with.
     **                          <br>
     **                          Allowed object is {@link Entity}.
     **
     ** @throws SystemException  if the schema operation fails.
     */
    private Room(final Context context, final Entity room)
      throws SystemException {

      // ensure inheritance
      super(context, ENTITY, room);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PropertyProvider</code> with the specified properties.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  name               the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  relation           the relation to a superordinated {@link Entity}
   **                            the properties to maintain belongs to.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  protected PropertyProvider(final Context context, final String name, final Entity relation)
    throws SystemException {

    // ensure inheritance
    super(context, name);

    // initialize instance attributes
    this.relation = relation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (Provider)
  /**
   ** Reads one {@link Entity.Property} from the Service Provider that matches
   ** the specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link Entity.Property} that matches the
   **                            specified filter criteria.
   **                            <br>
   **                            Possible object is {@link Entity.Property}.
   **
   ** @throws SystemException    if the {@link Entity.Property} could not be
   **                            found or is ambiguously defined.
   */
  @Override
  public final Entity.Property lookup(final DatabaseFilter criteria)
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
        return Entity.Property.build(result.get(0));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select (Provider)
  /**
   ** Reads all {@link Entity.Property}s from the Service Provider that matches
   ** the specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link List} containing all
   **                            {@link Entity.Property}s that matches the
   **                            specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type
   **                            {@link Entity.Property}.
   **
   ** @throws SystemException    if a user already exists.
   */
  @Override
  public final List<Entity.Property> select(final DatabaseFilter criteria)
    throws SystemException {

    final String method = "select";
    trace(method, Loggable.METHOD_ENTRY);
    DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), this.relation.primary()[0], DatabaseFilter.Operator.EQUAL);
    // extend the default filter criteria with the supplied criteria
    if (criteria != DatabaseFilter.NOP) {
      
    }
    final List<Entity.Property> collector = new ArrayList<Entity.Property>();
    final DatabaseSelect        statement = DatabaseSelect.build(this.context, this.entity, filter, this.entity.attribute());
    try {
      final List<Map<String, Object>> result = statement.execute(this.context.connect(), this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result)
        collector.add(Entity.Property.build(cursor));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (Provider)
  /**
   ** Reads all {@link Entity.Property}s from the Service Provider that matches
   ** the specified filter criteria.
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
   ** @return                    a {@link List} containing all
   **                            {@link Entity.Property}s that matches the
   **                            specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type
   **                            {@link Entity.Property}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public List<Entity.Property> search(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "search";
    trace(method, Loggable.METHOD_ENTRY);
    DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), this.relation.primary()[0], DatabaseFilter.Operator.EQUAL);
    // extend the default filter criteria with the supplied criteria
    if (criteria != DatabaseFilter.NOP) {
      
    }
    final List<Entity.Property>   collector = new ArrayList<Entity.Property>();
    final DatabaseSearch          statement = DatabaseSearch.build(this.context, this.entity, filter, this.entity.attribute());
    final List<DatabaseParameter> parameter = statement.prepare(this.context.connect());
    try {
      final List<Map<String, Object>> result = statement.execute(parameter, startRow, lastRow, this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result)
        collector.add(Entity.Property.build(cursor));
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (Provider)
  /**
   ** Build and executes instruction to create the provided
   ** {@link Entity.Property} in the Service Provider.
   **
   ** @param  entity             the populated {@link Entity.Property} that are
   **                            part of the instruction to create it at the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Entity.Property}.
   **
   ** @return                    the {@link Entity.Property} after the
   **                            instructions executed to create it in the
   **                            Service Provider.
   **                            <br>
   **                            Possible object is {@link Entity.Property}.
   **
   ** @throws SystemException    if the {@link Entity.Property} could not be
   **                            created.
   */
  @Override
  public final Entity.Property create(final Entity.Property entity)
    throws SystemException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(this.entity.primary().get(0).id(), this.relation.primary()[0],      DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.build(this.entity.primary().get(1).id(), entity.name(),              DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );
    if (!exists(filter))
      // the property exists since no exception was thrown, we have to complain
      // about the dublicate record
      throw DatabaseException.conflict(entity.name(), this.entity.id());

    final String method = "create";
    trace(method, Loggable.METHOD_ENTRY);
    final DatabaseInsert statement = DatabaseInsert.build(this.context, this.entity, entity.keySet());
    try {
      // execute statement usualy by verifying the operation has inserted at
      // least one record
      statement.execute(this.context.connect(), entity, true, this.context.endpoint().timeOutResponse());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify  (Provider)
  /**
   ** Build and executes instruction to modify the provided
   ** {@link Entity.Property} in the Service Provider.
   **
   ** @param  identifier         the identifier of an {@link Entity.Property} to
   **                            modify at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the partialy populated {@link Entity.Property}
   **                            that are part of the instruction to modify it
   **                            in the Service Provider.
   **                            <br>
   **                            Allowed object is {@link Entity.Property}.
   **
   ** @return                    the {@link Entity.Property} after the
   **                            instructions executed to modify it in the
   **                            Service Provider.
   **                            <br>
   **                            Possible object is {@link Entity.Property}.
   **
   ** @throws SystemException    if the {@link Entity.Property} could not be
   **                            modified.
   */
  @Override
  public final Entity.Property modify(final String identifier, final Entity.Property entity)
    throws SystemException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(this.entity.primary().get(0).id(), this.relation.primary()[0], DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.build(this.entity.primary().get(1).id(), identifier,                 DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );
    if (!exists(filter))
      // the property does not exists since no exception was thrown, we have
      // to complain about the missing record
      throw DatabaseException.notFound(identifier, this.entity.id());

    final String method = "modify";
    trace(method, Loggable.METHOD_ENTRY);
    final DatabaseUpdate statement = DatabaseUpdate.build(this.context, this.entity, filter, entity.keySet());
    try {
      // execute statement usualy by verifying the operation has updated at
      // least one record
      statement.execute(this.context.connect(), entity, true, this.context.endpoint().timeOutResponse());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (Provider)
  /**
   ** Build and executes instruction to delete the provided
   ** {@link Entity.Property} in the Service Provider.
   **
   ** @param  entity             the partialy populated {@link Entity.Property}
   **                            that are part of the instruction to delete it
   **                            at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link Entity.Property}.
   **
   ** @throws SystemException    if the {@link Entity.Property} could not be
   **                            deleted.
   */
  @Override
  public final void delete(final Entity.Property entity)
    throws SystemException {

    final String method = "delete";
    trace(method, Loggable.METHOD_ENTRY);
    DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), this.relation.primary()[0], DatabaseFilter.Operator.EQUAL);
    // may be we have to delete a particular property 
    if (entity != null) {
      filter = DatabaseFilter.build(
        filter
      , DatabaseFilter.build(this.entity.primary().get(1).id(), entity.name(), DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.Operator.AND              
      );
    }
    final DatabaseDelete statement = DatabaseDelete.build(this.context, this.entity, filter);
    try {
      // execute statement usualy by not verifying the operation has deleted at
      // least one record
      statement.execute(this.context.connect(), false, this.context.endpoint().timeOutResponse());
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gropued by functionalities
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   system
  /**
   ** Factory method to create an {@link System} <code>PropertyProvider</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    an instance of <code>PropertyProvider</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>PropertyProvider</code> for type
   **                            {@link System}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static PropertyProvider.System system(final Context context)
    throws SystemException {
    
    return new System(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Factory method to create an {@link User} <code>PropertyProvider</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  user               the superordinated {@link User} entity to join
   **                            with.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    an instance of <code>PropertyProvider</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>PropertyProvider</code> for type
   **                            {@link User}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static PropertyProvider.User user(final Context context, final Entity user)
    throws SystemException {
    
    return new User(context, user);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Factory method to create an {@link Group} <code>PropertyProvider</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  group              the superordinated {@link Group} entity to join
   **                            with.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    an instance of <code>PropertyProvider</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>PropertyProvider</code> for type
   **                            {@link Group}
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static PropertyProvider.Group group(final Context context, final Entity group)
    throws SystemException {
    
    return new Group(context, group);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Factory method to create an {@link Group} <code>PropertyProvider</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  room               the superordinated {@link Room} entity to join
   **                            with.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    an instance of <code>PropertyProvider</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>PropertyProvider</code> for type
   **                            {@link Room}
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static PropertyProvider.Room room(final Context context, final Entity room)
    throws SystemException {
    
    return new Room(context, room);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulkCreate
  /**
   ** Assigns the collection of {@link Entity.Property} to a {@link User}.
   ** <br>
   ** The {@link User} this operation belongs to was passed to the contructor
   ** of this {@link Provider}.
   **
   ** @param  property           the collection of {@link Entity.Property} to
   **                            associate with {@link User}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Entity.Property}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final void bulkCreate(final List<Entity.Property> property)
    throws SystemException {

    final String method = "bulkCreate";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      for (Entity.Property cursor : property) {
        // set relation identifier for flag to create
        cursor.attribute(this.entity.primary().get(0).id(), this.relation.primary()[0]);
        final DatabaseInsert  statement = DatabaseInsert.build(this.context, this.entity, cursor.keySet());
        // execute statement for the first property usualy by verifying the
        // operation has inserted at least one record
        statement.execute(this.context.connect(), cursor, true, this.context.endpoint().timeOutResponse());
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulkModify
  /**
   ** Modifies the collection of {@link Entity.Property} to a {@link User}.
   ** <br>
   ** The {@link User} this operation belongs to was passed to the contructor
   ** of this {@link Provider}.
   **
   ** @param  identifier         the identifier of an {@link Entity} to modify
   **                            in the  Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  property           the collection of {@link Entity.Property} to
   **                            associate with {@link User}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Entity.Property}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final void bulkModify(final String identifier, final List<Entity.Property> property)
    throws SystemException {

    final String method = "bulkModify";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      for (Entity.Property cursor : property) {
        final DatabaseFilter filter     = DatabaseFilter.build(
          DatabaseFilter.build(this.entity.primary().get(0).id(), this.relation.primary()[0], DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.build(this.entity.primary().get(1).id(), identifier,                 DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
        );
        final DatabaseUpdate  statement = DatabaseUpdate.build(this.context, this.entity, filter, cursor.keySet());
        // execute statement for the first property usualy by verifying the
        // operation has inserted at least one record
        statement.execute(this.context.connect(), cursor, true, this.context.endpoint().timeOutResponse());
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}