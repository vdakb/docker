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

    File        :   RoomProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoomProvider.


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

import oracle.iam.identity.icf.foundation.utility.DateUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseSearch;
import oracle.iam.identity.icf.dbms.DatabaseSelect;
import oracle.iam.identity.icf.dbms.DatabaseInsert;
import oracle.iam.identity.icf.dbms.DatabaseDelete;
import oracle.iam.identity.icf.dbms.DatabaseAttribute;
import oracle.iam.identity.icf.dbms.DatabaseParameter;
import oracle.iam.identity.icf.dbms.DatabaseException;

import oracle.iam.identity.icf.connector.openfire.Context;
import oracle.iam.identity.icf.connector.openfire.Provider;

import oracle.iam.identity.icf.connector.openfire.schema.Room;
import oracle.iam.identity.icf.connector.openfire.schema.Entity;
import oracle.iam.identity.icf.connector.openfire.schema.RoomMate;

////////////////////////////////////////////////////////////////////////////////
// final class RoomProvider
// ~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The descriptor to handle account entities.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class RoomProvider extends Provider<Room> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The native name of the entity. */
  static final String ENTITY      = "ofmucroom";

  /** The native name of the password attribute. */
  static final String NATURALNAME = "naturalname";

  /** The native name of the email attribute. */
  static final String DESCRIPTION = "description";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>RoomProvider</code> that belongs to the specified
   ** {@link Context}.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   */
  private RoomProvider(final Context context) {
    // ensure inheritance
    super(
      context
    , ENTITY
    , CollectionUtility.list(
        DatabaseAttribute.build(RID,         Room.RID,         DatabaseAttribute.type(String.class))
      , DatabaseAttribute.build(SID,         Room.SID,         DatabaseAttribute.type(String.class))
      , DatabaseAttribute.build(NAME,        Room.NAME,        DatabaseAttribute.type(String.class))
      , DatabaseAttribute.build(NATURALNAME, Room.NATURALNAME, DatabaseAttribute.type(String.class))
      , DatabaseAttribute.build(DESCRIPTION, Room.DESCRIPTION, DatabaseAttribute.type(String.class))
      )
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (Provider)
  /**
   ** Reads one {@link Room} from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link Room} that matches the specified
   **                            filter criteria.
   **                            <br>
   **                            Possible object is {@link Room}.
   **
   ** @throws SystemException    if the {@link Room} could not be found or is
   **                            ambiguously defined.
   */
  @Override
  public final Room lookup(final DatabaseFilter criteria)
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
        return Room.build(result.get(0));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select (Provider)
  /**
   ** Reads all {@link Room}s from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link List} containing all {@link Room}s
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Room}.
   **
   ** @throws SystemException    if a user already exists.
   */
  @Override
  public final List<Room> select(final DatabaseFilter criteria)
    throws SystemException {

    final String method = "select";
    trace(method, Loggable.METHOD_ENTRY);
    final List<Room>     collector = new ArrayList<Room>();
    final DatabaseSelect statement = DatabaseSelect.build(this.context, this.entity, criteria, this.entity.attribute());
    try {
      final List<Map<String, Object>> result = statement.execute(this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result)
        collector.add(Room.build(cursor));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (Provider)
  /**
   ** Reads all {@link Room}s from the Service Provider that matches the
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
   ** @return                    a {@link List} containing all {@link Room}s
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Room}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public List<Room> search(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "search";
    trace(method, Loggable.METHOD_ENTRY);
    final List<Room>              collector = new ArrayList<Room>();
    final DatabaseSearch          statement = DatabaseSearch.build(this.context, this.entity, criteria, this.entity.attribute());
    final List<DatabaseParameter> parameter = statement.prepare(this.context.connect());
    try {
      final List<Map<String, Object>> result = statement.execute(parameter, startRow, lastRow, this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result) {
        collector.add(Room.build(cursor));
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (Provider)
  /**
   ** Build and executes instruction to create the provided {@link Room} in
   ** the Service Provider.
   **
   ** @param  entity             the populated {@link Room} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Room}.
   **
   ** @return                    the {@link Room} after the instructions
   **                            executed to create it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link Room}.
   **
   ** @throws SystemException    if the {@link Room} could not be created.
   */
  @Override
  public final Room create(final Room entity)
    throws SystemException {

    if (exists(DatabaseFilter.build(this.entity.primary().get(0).id(), entity.rid(), DatabaseFilter.Operator.EQUAL)))
      // the room already exists since no exception, so:
      throw DatabaseException.conflict(entity.rid(), this.entity.id());

    final String method = "create";
    trace(method, Loggable.METHOD_ENTRY);
    final String pattern = Entity.convertTimestamp(DateUtility.now());
    entity.attribute(Entity.CREATED, pattern);
    entity.attribute(Entity.UPDATED, pattern);
    final DatabaseInsert   statement = DatabaseInsert.build(this.context, this.entity, entity.keySet());
    try {
      // execute statement usualy by verifying the operation has inserted at
      // least one record
      statement.execute(this.context.connect(), entity, true, this.context.endpoint().timeOutResponse());
      // create all property objects that associated with the room after the
      // room itself was created
      final List<Entity.Property> property = entity.property();
      if (property != null && property.size() > 0) {
        PropertyProvider.room(this.context, entity).bulkCreate(entity.property());
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
   ** Build and executes instruction to modify the provided {@link Room} in
   ** the Service Provider.
   **
   ** @param  identifier         the identifier of a {@link Room} to modify
   **                            in the  Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the partialy populated {@link Room} that are
   **                            part of the instruction to modify it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Room}.
   **
   ** @return                    the {@link Room} after the instructions
   **                            executed to modify it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link Room}.
   **
   ** @throws SystemException    if the {@link Room} could not be modified.
   */
  @Override
  public final Room modify(final String identifier, final Room entity)
    throws SystemException {

    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (Provider)
  /**
   ** Build and executes instruction to delete the provided {@link Room} in
   ** the Service Provider.
   **
   ** @param  entity             the partialy populated {@link Room} that are
   **                            part of the instruction to delete it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Room}.
   **
   ** @throws SystemException    if the {@link Room} could not be deleted.
   */
  @Override
  public void delete(final Room entity)
    throws SystemException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(this.entity.primary().get(0).id(), entity.sid(), DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.build(this.entity.primary().get(1).id(), entity.rid(), DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );
    if (!exists(filter))
      // the room does not exists since no exception was thrown, we have to
      // complain about the missing record
      throw DatabaseException.notFound(entity.rid(), this.entity.id());

    final String method = "delete";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // delete any properties that might be associated with the room before the
      // room itself will be deleted
      final PropertyProvider.Room property = PropertyProvider.room(this.context, entity);
      // passing null means all because we deleting the owning entity
      property.delete(null);
      // delete any memberships that might be associated with the room before
      // the room itself will be deleted
      final Provider<RoomMate> occupancy = RoomMateProvider.room(this.context);
      occupancy.delete(RoomMate.build(entity));
      // now the room is ready for delete
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
   ** Factory method to create a new instance of <code>RoomProvider</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    an instance of <code>RoomProvider</code>.
   **                            <br>
   **                            Possible object is <code>RoomProvider</code>.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static RoomProvider build(final Context context)
    throws SystemException {
    
    return new RoomProvider(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Build a query to verify the existance of a {@link Room} entity at the
   ** Service Provider.
   **
   ** @param  rid                the system identifier of the {@link Room}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if a matching entity exists
   **                            for the <code>rid</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    if a user already exists.
   */
  public final boolean exists(final String rid)
    throws SystemException {
    
    return super.exists(DatabaseFilter.build(this.entity.primary().get(0).id(), rid, DatabaseFilter.Operator.EQUAL));
  }
}