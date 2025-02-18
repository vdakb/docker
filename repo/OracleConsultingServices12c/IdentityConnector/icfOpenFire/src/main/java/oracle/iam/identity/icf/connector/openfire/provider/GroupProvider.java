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

    File        :   GroupProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    GroupProvider.


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

import oracle.iam.identity.icf.connector.openfire.schema.Group;
import oracle.iam.identity.icf.connector.openfire.schema.Member;
import oracle.iam.identity.icf.connector.openfire.schema.Entity;
import oracle.iam.identity.icf.connector.openfire.schema.User;
import oracle.iam.identity.icf.dbms.DatabaseUpdate;
import oracle.iam.identity.icf.foundation.utility.DateUtility;

////////////////////////////////////////////////////////////////////////////////
// final class GroupProvider
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The descriptor to handle group entities.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class GroupProvider extends Provider<Group>{

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The native name of the entity. */
  static final String ENTITY = "ofgroup";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GroupProvider</code> that belongs to the specified
   ** {@link Context}.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  private GroupProvider(final Context context)
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
   ** Reads one {@link Group} from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link Group} that matches the specified
   **                            filter criteria.
   **                            <br>
   **                            Possible object is {@link Group}.
   **
   ** @throws SystemException    if the {@link Group} could not be found or is
   **                            ambiguously defined.
   */
  @Override
  public final Group lookup(final DatabaseFilter criteria)
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
        return Group.build(result.get(0));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select (Provider)
  /**
   ** Reads all {@link Group}s from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link List} containing all {@link Group}s
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Group}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public final List<Group> select(final DatabaseFilter criteria)
    throws SystemException {

    final String method = "select";
    trace(method, Loggable.METHOD_ENTRY);
    final List<Group>    collector = new ArrayList<Group>();
    final DatabaseSelect statement = DatabaseSelect.build(this.context, this.entity, criteria, this.entity.attribute());
    try {
      final List<Map<String, Object>> result = statement.execute(this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result) {
        final Group entity = Group.build(cursor);
        collector.add(entity);
        // take care about associated properties
        final PropertyProvider property = PropertyProvider.group(this.context, entity);
        entity.property(property.select(DatabaseFilter.NOP));
      }
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (Provider)
  /**
   ** Reads all {@link Group}s from the Service Provider that matches the
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
   ** @return                    a {@link List} containing all {@link Group}s
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Group}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public List<Group> search(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "search";
    trace(method, Loggable.METHOD_ENTRY);
    final List<Group>             collector = new ArrayList<Group>();
    final DatabaseSearch          statement = DatabaseSearch.build(this.context, this.entity, criteria, this.entity.attribute());
    final List<DatabaseParameter> parameter = statement.prepare(this.context.connect());
    try {
      final List<Map<String, Object>> result = statement.execute(parameter, startRow, lastRow, this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result) {
        final Group entity = Group.build(cursor);
        collector.add(entity);
        // take care about associated properties
        final PropertyProvider property = PropertyProvider.group(this.context, entity);
        entity.property(property.select(DatabaseFilter.NOP));
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
   ** Build and executes instruction to create the provided {@link Group} in
   ** the Service Provider.
   **
   ** @param  entity             the populated {@link Group} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Group}.
   **
   ** @return                    the {@link Group} after the instructions
   **                            executed to create it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link Group}.
   **
   ** @throws SystemException    if the {@link Group} could not be created.
   */
  @Override
  public Group create(final Group entity)
    throws SystemException {

    if (exists(DatabaseFilter.build(this.entity.primary().get(0).id(), entity.gid(), DatabaseFilter.Operator.EQUAL)))
      // the group already exists since no exception, so:
      throw DatabaseException.conflict(entity.gid(), this.entity.id());

    final String method = "create";
    trace(method, Loggable.METHOD_ENTRY);

    final DatabaseInsert statement = DatabaseInsert.build(this.context, this.entity, entity.keySet());
    try {
      // execute statement usualy by verifying the operation has inserted at
      // least one record
      statement.execute(this.context.connect(), entity, true, this.context.endpoint().timeOutResponse());
      // create all property objects that associated with the group after the
      // group itself was created
      final List<Entity.Property> property = entity.property();
      if (property != null && property.size() > 0) {
        PropertyProvider.group(this.context, entity).bulkCreate(entity.property());
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify (Provider)
  /**
   ** Build and executes instruction to modify the provided {@link Group} in
   ** the Service Provider.
   **
   ** @param  gid                the identifier of a {@link Group} to modify
   **                            in the  Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the partialy populated {@link Group} that are
   **                            part of the instruction to modify it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Group}.
   **
   ** @return                    the {@link Entity} after the instructions
   **                            executed to modify it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link Group}.
   **
   ** @throws SystemException    if the {@link Group} could not be modified.
   */
  @Override
  public final Group modify(final String gid, final Group entity)
    throws SystemException {

    final DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), gid, DatabaseFilter.Operator.EQUAL);
    if (!exists(filter))
      // the user does not exists since no exception, so:
      throw DatabaseException.notFound(gid, this.entity.id());

    final String method = "modify";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      if (entity.keySet().size() > 0) {
        // evaluate the audit attributes required to update the user entry
        entity.attribute(User.UPDATED, Entity.convertTimestamp(DateUtility.now()));
        // create the statement lately to have the bindings populated correctly
        final DatabaseUpdate statement = DatabaseUpdate.build(this.context, this.entity, filter, entity.keySet());
        // execute statement usualy by verifying the operation has updated at
        // least one record
        statement.execute(this.context.connect(), entity, true, this.context.endpoint().timeOutResponse());
        // ensure that the entity have the value required to update dependencies
        if (entity.containsKey(Group.GID))
          entity.gid(gid);
      }
      // ensure that the entity have the value required to update dependencies
      if (!entity.containsKey(Group.GID))
        entity.gid(gid);
      // modify all property objects that associated with the user account after
      // the account itself was modified
      final List<Entity.Property> property = entity.property();
      if (property != null && property.size() > 0) {
        PropertyProvider.user(this.context, entity).bulkModify(gid, entity.property());
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
   ** Build and executes instruction to delete the provided {@link Group} in
   ** the Service Provider.
   **
   ** @param  entity             the partialy populated {@link Group} that are
   **                            part of the instruction to delete it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Group}.
   **
   ** @throws SystemException    the {@link Group} could not be deleted.
   */
  @Override
  public void delete(final Group entity)
    throws SystemException {

    final DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), entity.gid(), DatabaseFilter.Operator.EQUAL);
    if (!exists(filter))
      // the group does not exists since no exception was thrown, we have to
      // complain about the missing record
      throw DatabaseException.notFound(entity.gid(), this.entity.id());

    final String method = "delete";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // delete any properties that might be associated with the group before
      // the group itself will be deleted
      final PropertyProvider.Group property = PropertyProvider.group(this.context, entity);
      // passing null means all because we deleting the owning entity
      property.delete(null);
      // delete any memberships that might be associated with the group before
      // the group itself will be deleted
      final Provider<Member> member = new MemberProvider.Group(this.context);
      member.delete(Member.build(entity));
      // now the group is ready for delete
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
   ** Factory method to create a new instance of <code>GroupProvider</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    an instance of <code>GroupProvider</code>.
   **                            <br>
   **                            Possible object is <code>GroupProvider</code>.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static GroupProvider build(final Context context)
    throws SystemException {

    return new GroupProvider(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Build a query to verify the existance of a {@link Group} entity at the
   ** Service Provider.
   **
   ** @param  gid                the system identifier of the {@link Group}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if a matching entity exists
   **                            for the <code>gid</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    if a user already exists.
   */
  public final boolean exists(final String gid)
    throws SystemException {

    return super.exists(DatabaseFilter.build(this.entity.primary().get(0).id(), gid, DatabaseFilter.Operator.EQUAL));
  }
}