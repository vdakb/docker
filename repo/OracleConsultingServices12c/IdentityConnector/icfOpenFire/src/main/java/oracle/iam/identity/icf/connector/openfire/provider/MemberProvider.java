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

    File        :   MemberProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    MemberProvider.


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

////////////////////////////////////////////////////////////////////////////////
// class MemberProvider
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The descriptor to handle account group relation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class MemberProvider extends Provider<Member> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The native name of the user group relation. */
  static final String ENTITY = "ofgroupuser";

  /** The native name of the administrator attribute. */
  static final String ADM    = "administrator";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class User
  // ~~~~~ ~~~~
  /**
   ** The descriptor to handle group account relation.
   */
  public static class User extends MemberProvider {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>User</code> <code>MemberProvider</code> that
     ** belongs to the specified {@link Context}.
     **
     ** @param  context          the {@link Context} where this relation belongs
     **                          to.
     **                          <br>
     **                          Allowed object is {@link Context}.
     **
     ** @throws SystemException  if the schema operation fails.
     */
    public User(final Context context)
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
     ** Returns all {@link Member}s from the Service Provider that belongs to
     ** the specified user identifier <code>uid</code>.
     **
     ** @param  uid              the identifier to search for the group where
     **                          the specified <code>uid</code> is member of.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the collection of {@link Member}s that belongs
     **                          to the specified identifier.
     **                          <br>
     **                          Possible objetc is {@link List} where each
     **                          element is of type {@link Member}.
     **
     ** @throws SystemException  in case an error does occur.
     */
    @Override
    public final List<Member> select(final String uid)
      throws SystemException {

      return select(DatabaseFilter.build(this.entity.primary().get(1).id(), uid, DatabaseFilter.Operator.EQUAL));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: delete (Provider)
    /**
     ** Build and executes instruction to delete the provided {@link Member} in
     ** the Service Provider.
     ** <p>
     ** This method checks if the complete set
     **
     ** @param  entity             the partialy populated {@link Member} that are
     **                            part of the instruction to delete it in the
     **                            Service Provider.
     **                            <br>
     **                            Allowed object is {@link Member}.
     **
     ** @throws SystemException    the {@link Member} could not be deleted.
     */
    @Override
    public void delete(final Member entity)
      throws SystemException {

      final UserProvider user = UserProvider.build(this.context);
      if (!user.exists(entity.uid()))
        // the user does not exists since no exception was thrown, we have to
        // complain about the missing record
        throw DatabaseException.notFound(entity.uid(), user.entity().id());

      // if the membership does not belong to a group identifier we cannot
      // verify if a group exists for the context the statement is executed
      if (entity.gid() != null) {
        final GroupProvider group = GroupProvider.build(this.context);
        if (!group.exists(entity.gid()))
          // the group does not exists since no exception was thrown, we have to
          // complain about the missing record
          throw DatabaseException.notFound(entity.gid(), group.entity().id());
      }

      DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(1).id(), entity.uid(), DatabaseFilter.Operator.EQUAL);
      if (entity.gid() != null) {
        filter = DatabaseFilter.build(
          filter
        , DatabaseFilter.build(this.entity.primary().get(0).id(), entity.gid(), DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
        );
        // apply any check for existance only if a concrete record is meant
        if (!exists(filter))
          throw DatabaseException.notFound(entity.toString(), this.entity.id());
      }

      final String method = "delete";
      trace(method, Loggable.METHOD_ENTRY);
      final DatabaseDelete statement = DatabaseDelete.build(this.context, this.entity, filter);
      try {
        // execute statement usualy by only verifying the operation has deleted
        // at least one record if not a concrete record is meant
        statement.execute(this.context.connect(), (entity.gid() != null), this.context.endpoint().timeOutResponse());
      }
      finally {
        trace(method, Loggable.METHOD_EXIT);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Group
  // ~~~~~ ~~~~~
  /**
   ** The descriptor to handle account group relation.
   */
  public static class Group extends MemberProvider {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Group</code> <code>MemberProvider</code> that
     ** belongs to the specified {@link Context}.
     **
     ** @param  context          the {@link Context} where this relation belongs
     **                          to.
     **                          <br>
     **                          Allowed object is {@link Context}.
     **
     ** @throws SystemException  if the schema operation fails.
     */
    public Group(final Context context)
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
     ** Returns all {@link Member}s from the Service Provider that belongs to
     ** the specified group identifier <code>gid</code>.
     **
     ** @param  gid              the identifier to search for the members of
     **                          the specified <code>gid</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the collection of {@link Member}s that belongs
     **                          to the specified identifier <code>gid</code>.
     **                          <br>
     **                          Possible objetc is {@link List} where each
     **                          element is of type {@link Member}.
     **
     ** @throws SystemException  in case an error does occur.
     */
    @Override
    public final List<Member> select(final String gid)
      throws SystemException {

      return select(DatabaseFilter.build(this.entity.primary().get(0).id(), gid, DatabaseFilter.Operator.EQUAL));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: delete (Provider)
    /**
     ** Build and executes instruction to delete the provided {@link Member} in
     ** the Service Provider.
     **
     ** @param  entity           the partialy populated {@link Member} that are
     **                          part of the instruction to delete it in the
     **                          Service Provider.
     **                          <br>
     **                          Allowed object is {@link Member}.
     **
     ** @throws SystemException  the {@link Member} could not be deleted.
     */
    @Override
    public void delete(final Member entity)
      throws SystemException {

      // if the membership does not belong to a user identifier we cannot verify
      // if a user exists for the context the statement is executed
      if (entity.uid() != null) {
        final UserProvider user = UserProvider.build(this.context);
        if (!user.exists(entity.uid()))
          // the user does not exists since no exception was thrown, we have to
          // complain about the missing record
          throw DatabaseException.notFound(entity.uid(), user.entity().id());
      }

      final GroupProvider group = GroupProvider.build(this.context);
      if (!group.exists(entity.gid()))
        // the group does not exists since no exception was thrown, we have to
        // complain about the missing record
        throw DatabaseException.notFound(entity.gid(), group.entity().id());

      DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), entity.gid(), DatabaseFilter.Operator.EQUAL);
      if (entity.uid() != null) {
        filter = DatabaseFilter.build(
          filter
        , DatabaseFilter.build(this.entity.primary().get(1).alias(), entity.uid(), DatabaseFilter.Operator.EQUAL)
        , DatabaseFilter.Operator.AND
        );
        // apply any check for existance only if a concrete record is meant
        if (!exists(filter))
          throw DatabaseException.notFound(entity.toString(), this.entity.id());
      }

      final String method = "delete";
      trace(method, Loggable.METHOD_ENTRY);
      final DatabaseDelete statement = DatabaseDelete.build(this.context, this.entity, filter);
      try {
        // execute statement usualy by not verifying the operation has deleted
        // at least one record
        statement.execute(this.context.connect(), (entity.uid() != null), this.context.endpoint().timeOutResponse());
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
   ** Constructs an <code>MemberProvider</code> that belongs to the
   ** specified database <code>schema</code> name.
   **
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  protected MemberProvider(final Context context)
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
   ** Reads one {@link Member} from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link Member} that matches the specified
   **                            filter criteria.
   **                            <br>
   **                            Possible object is {@link Member}.
   **
   ** @throws SystemException    if the {@link Member} could not be found or is
   **                            ambiguously defined.
   */
  @Override
  public final Member lookup(final DatabaseFilter criteria)
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
        return Member.build(result.get(0));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select (Provider)
  /**
   ** Returns all {@link Member}s from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria         the filter criteria to build for the SQL
   **                          statement.
   **                          <br>
   **                          Allowed object is {@link DatabaseFilter}.
   **
   ** @return                  a {@link List} containing all {@link Member}s
   **                          that matches the specified filter criteria.
   **                          <br>
   **                          Possible object is {@link List} where each
   **                          element is of type {@link Member}.
   **
   ** @throws SystemException  in case an error does occur.
   */
  @Override
  public final List<Member> select(final DatabaseFilter criteria)
    throws SystemException {

    final String method = "select";
    trace(method, Loggable.METHOD_ENTRY);
    final List<Member>   collector = new ArrayList<Member>();
    final DatabaseSelect statement = DatabaseSelect.build(this.context, this.entity, criteria, this.entity.attribute());
    try {
      final List<Map<String, Object>> result = statement.execute(this.context.connect(), this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result)
        collector.add(Member.build(cursor));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (Provider)
  /**
   ** Reads all {@link Member}s from the Service Provider that matches the
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
   ** @return                    a collection containing all {@link Member}s
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Member}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public final List<Member> search(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "search";
    trace(method, Loggable.METHOD_ENTRY);
    final List<Member>            collector = new ArrayList<Member>();
    final DatabaseSearch          statement = DatabaseSearch.build(this.context, this.entity, criteria, this.entity.attribute());
    final List<DatabaseParameter> parameter = statement.prepare(this.context.connect());
    try {
      final List<Map<String, Object>> result = statement.execute(parameter, startRow, lastRow, this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result) {
        collector.add(Member.build(cursor));
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
   ** Build and executes instruction to create the provided {@link Member} in
   ** the Service Provider.
   **
   ** @param  entity             the populated {@link Member} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Member}.
   **
   ** @return                    the {@link Member} after the instructions
   **                            executed to create it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link Member}.
   **
   ** @throws SystemException    if the {@link Member} could not be created.
   */
  @Override
  public final Member create(final Member entity)
    throws SystemException {

    final UserProvider user = UserProvider.build(this.context);
    if (!user.exists(entity.uid()))
      // the user does not exists since no exception was thrown, we have to
      // complain about the missing record
      throw DatabaseException.notFound(entity.uid(), user.entity().id());

    final GroupProvider group = GroupProvider.build(this.context);
    if (!group.exists(entity.gid()))
      // the group does not exists since no exception was thrown, we have to
      // complain about the missing record
      throw DatabaseException.notFound(entity.gid(), group.entity().id());

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(this.entity.primary().get(0).id(), entity.gid(), DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.build(this.entity.primary().get(1).id(), entity.uid(), DatabaseFilter.Operator.EQUAL)
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
   ** Build and executes instruction to modify the provided {@link Member} in
   ** the Service Provider.
   **
   ** @param  identifier         the identifier of a {@link Member} to modify
   **                            in the  Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the partialy populated {@link Member} that are
   **                            part of the instruction to modify it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Member}.
   **
   ** @return                    the {@link Member} after the instructions
   **                            executed to modify it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link Member}.
   **
   ** @throws SystemException    if the {@link Member} could not be modified.
   */
  @Override
  public final Member modify(final String identifier, final Member entity)
    throws SystemException {

    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gropued by functionalities
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Factory method to create a <code>MemberProvider</code> that maintains
   ** memberships for the provided {@link User} <code>entity</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    an instance of <code>MemberProvider</code>.
   **                            <br>
   **                            Possible object is <code>MemberProvider</code>
   **                            for type {@link User}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static MemberProvider.User user(final Context context)
    throws SystemException {
    
    return new User(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Factory method to create a <code>MemberProvider</code> that maintains
   ** memberships for the provided {@link Group} <code>entity</code>.
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
   **                            {@link group}
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static MemberProvider.Group group(final Context context)
    throws SystemException {
    
    return new Group(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select
  /**
   ** Returns the collection of counterparts for the specified indentifier;
   ** either a group or a user.
   **
   ** @param  id                 the identifier to search for the counterpart.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collection of {@link Member}s that belongs
   **                            to the specified identifier.
   **                            <br>
   **                            Possible objetc is {@link List} where each
   **                            element is of type {@link Member}.
   **
   ** @throws SystemException    if the database operation fails.
   */
  public abstract List<Member> select(final String id)
    throws SystemException;
}