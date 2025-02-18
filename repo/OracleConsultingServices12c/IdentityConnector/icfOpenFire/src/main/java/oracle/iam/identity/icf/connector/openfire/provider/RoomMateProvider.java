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

    File        :   RoomRoomMateProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoomMateProvider.


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
import oracle.iam.identity.icf.dbms.DatabaseDelete;
import oracle.iam.identity.icf.dbms.DatabaseParameter;
import oracle.iam.identity.icf.dbms.DatabaseException;

import oracle.iam.identity.icf.connector.openfire.Context;
import oracle.iam.identity.icf.connector.openfire.Provider;

import oracle.iam.identity.icf.connector.openfire.schema.Member;
import oracle.iam.identity.icf.connector.openfire.schema.RoomMate;

////////////////////////////////////////////////////////////////////////////////
// class RoomMateProvider
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The descriptor to handle account room relation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class RoomMateProvider extends Provider<RoomMate> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The native name of the user group relation. */
  static final String ENTITY = "ofmucmember";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class User
  // ~~~~~ ~~~~
  /**
   ** The descriptor to handle group account relation.
   */
  public static class User extends RoomMateProvider {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>User</code> <code>RoomMateProvider</code> that
     ** belongs to the specified {@link Context}.
     **
     ** @param  context          the {@link Context} where this relation belongs
     **                          on.
     **                          <br>
     **                          Allowed object is {@link Context}.
     **
     ** @throws SystemException  if the schema operation fails.
     */
    private User(final Context context)
      throws SystemException {

      // ensure inheritance
      super(context);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: select (MemberProvider)
    /**
     ** Returns all {@link RoomMate}s from the Service Provider that belongs to
     ** the specified user identifier <code>uid</code>.
     **
     ** @param  uid              the identifier to search for the rooms where
     **                          the specified <code>uid</code> is member of.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the collection of {@link RoomMate}s that
     **                          belongs to the specified identifier.
     **                          <br>
     **                          Possible objetc is {@link List} where each
     **                          element is of type {@link RoomMate}.
     **
     ** @throws SystemException  in case an error does occur.
     */
    @Override
    public final List<RoomMate> select(final String uid)
      throws SystemException {

      return select(DatabaseFilter.build(this.entity.primary().get(1).id(), uid, DatabaseFilter.Operator.EQUAL));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: delete (Provider)
    /**
     ** Build and executes instruction to delete the provided {@link RoomMate}
     ** in the Service Provider.
     **
     ** @param  entity             the partialy populated {@link RoomMate} that
     **                            are part of the instruction to delete it in
     **                            the Service Provider.
     **                            <br>
     **                            Allowed object is {@link RoomMate}.
     **
     ** @throws SystemException    the {@link RoomMate} could not be deleted.
     */
    @Override
    public void delete(final RoomMate entity)
      throws SystemException {

      final String method = "delete";
      trace(method, Loggable.METHOD_ENTRY);
      // Don't apply any check for existance here due to no assignment may
      // exists so we make always a shot in the blue
      final DatabaseFilter    filter    = DatabaseFilter.build(
        DatabaseFilter.build(this.entity.primary().get(0).id(), entity.rid(), DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.build(this.entity.primary().get(1).id(), entity.jid(), DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.Operator.AND
      );
      final DatabaseDelete statement = DatabaseDelete.build(this.context, this.entity, filter);
      try {
        // execute statement usualy not by verifying the operation has deleted
        // at least one record
        statement.execute(this.context.connect(), false, this.context.endpoint().timeOutResponse());
      }
      finally {
        trace(method, Loggable.METHOD_EXIT);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Room
  // ~~~~~ ~~~~
  /**
   ** The descriptor to handle chat room relation.
   */
  public static class Room extends RoomMateProvider {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Room</code> <code>RoomMateProvider</code> that
     ** belongs to the specified {@link Context}.
     **
     ** @param  context          the {@link Context} where this relation belongs
     **                          to.
     **                          <br>
     **                          Allowed object is {@link Context}.
     **
     ** @throws SystemException  if the schema operation fails.
     */
    public Room(final Context context)
      throws SystemException {

      // ensure inheritance
      super(context);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: select (RoomMateProvider)
    /**
     ** Returns all {@link Member}s from the Service Provider that belongs to
     ** the specified room identifier <code>rid</code>.
     **
     ** @param  rid              the identifier to search for the members of
     **                          the specified <code>rid</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the collection of {@link RoomMate}s that
     **                          belongs to the specified identifier
     **                          <code>rid</code>.
     **                          <br>
     **                          Possible objetc is {@link List} where each
     **                          element is of type {@link RoomMate}.
     **
     ** @throws SystemException  in case an error does occur.
     */
    @Override
    public final List<RoomMate> select(final String rid)
      throws SystemException {

      return select(DatabaseFilter.build(this.entity.primary().get(0).id(), rid, DatabaseFilter.Operator.EQUAL));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: delete (Provider)
    /**
     ** Build and executes instruction to delete the provided {@link RoomMate}
     ** in the Service Provider.
     **
     ** @param  entity             the partialy populated {@link RoomMate} that
     **                            are part of the instruction to delete it in
     **                            the Service Provider.
     **                            <br>
     **                            Allowed object is {@link RoomMate}.
     **
     ** @throws SystemException    the {@link RoomMate} could not be deleted.
     */
    @Override
    public void delete(final RoomMate entity)
      throws SystemException {

      final String method = "delete";
      trace(method, Loggable.METHOD_ENTRY);
      // Don't apply any check for existance here due to no assignment may
      // exists
      final DatabaseFilter    filter    = DatabaseFilter.build(
        DatabaseFilter.build(this.entity.primary().get(0).id(), entity.rid(),  DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.build(this.entity.primary().get(1).id(), entity.jid(),  DatabaseFilter.Operator.EQUAL)
      , DatabaseFilter.Operator.AND
      );
      final DatabaseDelete statement = DatabaseDelete.build(this.context, this.entity, filter);
      try {
        // execute statement usualy not by verifying the operation has deleted
        // at least one record
        statement.execute(this.context.connect(), false, this.context.endpoint().timeOutResponse());
      }
      finally {
        trace(method, Loggable.METHOD_EXIT);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>RoomMateProvider</code> that belongs to the
   ** specified database <code>schema</code> name.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  protected RoomMateProvider(final Context context)
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
   ** Reads one {@link RoomMate} from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link RoomMate} that matches the specified
   **                            filter criteria.
   **                            <br>
   **                            Possible object is {@link RoomMate}.
   **
   ** @throws SystemException    if the {@link RoomMate} could not be found or
   **                            is ambiguously defined.
   */
  @Override
  public final RoomMate lookup(final DatabaseFilter criteria)
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
        return RoomMate.build(result.get(0));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select (Provider)
  /**
   ** Reads all {@link RoomMate}s from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link List} containing all {@link RoomMate}s
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link RoomMate}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public final List<RoomMate> select(final DatabaseFilter criteria)
    throws SystemException {

    final String method = "select";
    trace(method, Loggable.METHOD_ENTRY);
    final List<RoomMate> collector = new ArrayList<RoomMate>();
    final DatabaseSelect    statement = DatabaseSelect.build(this.context, this.entity, criteria, this.entity.attribute());
    try {
      final List<Map<String, Object>> result = statement.execute(this.context.connect(), this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result)
        collector.add(RoomMate.build(cursor));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (Provider)
  /**
   ** Reads all {@link RoomMate}s from the Service Provider that matches the
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
   ** @return                    a {@link List} containing all {@link RoomMate}s
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link RoomMate}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public List<RoomMate> search(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "search";
    trace(method, Loggable.METHOD_ENTRY);
    final List<RoomMate>          collector = new ArrayList<RoomMate>();
    final DatabaseSearch          statement = DatabaseSearch.build(this.context, this.entity, criteria, this.entity.attribute());
    final List<DatabaseParameter> parameter = statement.prepare(this.context.connect());
    try {
      final List<Map<String, Object>> result = statement.execute(parameter, startRow, lastRow, this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result) {
        collector.add(RoomMate.build(cursor));
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
   ** Build and executes instruction to create the provided {@link RoomMate} in
   ** the Service Provider.
   **
   ** @param  entity             the populated {@link RoomMate} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link RoomMate}.
   **
   ** @return                    the {@link RoomMate} after the instructions
   **                            executed to create it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link RoomMate}.
   **
   ** @throws SystemException    if the {@link RoomMate} could not be created.
   */
  @Override
  public final RoomMate create(final RoomMate entity)
    throws SystemException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(this.entity.primary().get(0).id(), entity.rid(), DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.build(this.entity.primary().get(1).id(), entity.jid(), DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );
    if (exists(filter))
      // the assignment already exists since no exception, so:
      throw DatabaseException.conflict(entity.toString(), this.entity.id());

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
  // Method:   modify (Provider)
  /**
   ** Build and executes instruction to modify the provided {@link RoomMate} in
   ** the Service Provider.
   **
   ** @param  identifier         the identifier of a {@link RoomMate} to modify
   **                            in the  Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the partialy populated {@link RoomMate} that
   **                            are part of the instruction to modify it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link RoomMate}.
   **
   ** @return                    the {@link RoomMate} after the instructions
   **                            executed to modify it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link RoomMate}.
   **
   ** @throws SystemException    if the {@link RoomMate} could not be modified.
   */
  @Override
  public final RoomMate modify(final String identifier, final RoomMate entity)
    throws SystemException {

    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gropued by functionalities
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Factory method to create a <code>RoomMateProvider</code> that maintains
   ** occupancies for {@link User}s.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    an instance of <code>RoomMateProvider</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RoomMateProvider</code> for type
   **                            {@link User}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static RoomMateProvider.User user(final Context context)
    throws SystemException {
    
    return new User(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   room
  /**
   ** Factory method to create a <code>RoomMateProvider</code> that maintains
   ** occupancies for {@link Room}s.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    an instance of <code>RoomMateProvider</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RoomMateProvider</code> for type
   **                            {@link Room}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static RoomMateProvider.Room room(final Context context)
    throws SystemException {
    
    return new Room(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select
  /**
   ** Returns the collection of counterparts for the specified indentifier;
   ** either a room or a user.
   **
   ** @param  id                 the identifier to search for the counterpart.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collection of {@link RoomMate}s that
   **                            belongs to the specified identifier.
   **                            <br>
   **                            Possible objetc is {@link List} where each
   **                            element is of type {@link RoomMate}.
   **
   ** @throws SystemException    if the database operation fails.
   */
  public abstract List<RoomMate> select(final String id)
    throws SystemException;
}