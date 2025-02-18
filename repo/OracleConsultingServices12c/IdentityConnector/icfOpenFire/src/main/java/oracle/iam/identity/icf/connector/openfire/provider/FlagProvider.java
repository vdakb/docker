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

    File        :   FlagProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlagProvider.


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

import oracle.iam.identity.icf.connector.openfire.schema.User;
import oracle.iam.identity.icf.dbms.DatabaseUpdate;

////////////////////////////////////////////////////////////////////////////////
// final class FlagProvider
// ~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Default implementation of the <code>FlagProvider</code> interface for
 ** <code>User</code>s, which reads and writes data from the
 ** <code>ofUserFlag</code> database table.
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
 ** directly. Instead, use the {@link User.Flag} returned by
 ** {@code User#flag()} to create, read, update or delete user properties.
 ** <br>
 ** Failure to do so is likely to result in inconsistent data behavior and
 ** race conditions. Direct access to the user property provider is only
 ** provided for special-case logic.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class FlagProvider extends Provider<User.Flag> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The native name of the entity. */
  static final String ENTITY = "ofuserflag";

  /** The native name of the start time attribute. */
  static final String START  = "starttime";

  /** The native name of the end time attribute. */
  static final String END    = "endtime";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /** The relation to the superordinated {@link User}. */
  final User relation;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FlagProvider</code> with the specified properties.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  relation           the relation to a superordinated {@link User}
   **                            the flag to maintain belongs to.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  protected FlagProvider(final Context context, final User relation)
    throws SystemException {

    // ensure inheritance
    super(context, ENTITY);

    // initialize instance attributes
    this.relation = relation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (Provider)
  /**
   ** Reads one {@link User.Flag} from the Service Provider that matches the
   ** specified filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link User.Flag} that matches the specified
   **                            filter criteria.
   **                            <br>
   **                            Possible object is {@link User.Flag}.
   **
   ** @throws SystemException    if the {@link User.Flag} could not be found or
   **                            is ambiguously defined.
   */
  @Override
  public final User.Flag lookup(final DatabaseFilter criteria)
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
        return User.Flag.build(result.get(0));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select (Provider)
  /**
   ** Reads all properties from the Service Provider that matches the specified
   ** filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link List} containing all
   **                            {@link User.Flag}s that matches the specified
   **                            filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link User.Flag}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public final List<User.Flag> select(final DatabaseFilter criteria)
    throws SystemException {

    final String method = "select";
    trace(method, Loggable.METHOD_ENTRY);
    DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), this.relation.primary()[0], DatabaseFilter.Operator.EQUAL);
    // extend the default filter criteria with the supplied criteria
    if (criteria != DatabaseFilter.NOP) {
      
    }
    final List<User.Flag> collector = new ArrayList<User.Flag>();
    final DatabaseSelect    statement = DatabaseSelect.build(this.context, this.entity, filter, this.entity.attribute());
    try {
      final List<Map<String, Object>> result = statement.execute(this.context.connect(), this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result)
        collector.add(User.Flag.build(cursor));
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (Provider)
  /**
   ** Reads all properties from the Service Provider that matches the specified
   ** filter criteria.
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
   **                            {@link User.Flag}s that matches the specified
   **                            filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link User.Flag}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  @Override
  public List<User.Flag> search(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException {

    final String method = "search";
    trace(method, Loggable.METHOD_ENTRY);
    final List<User.Flag>         collector = new ArrayList<User.Flag>();
    final DatabaseSearch          statement = DatabaseSearch.build(this.context, this.entity, criteria, this.entity.attribute());
    final List<DatabaseParameter> parameter = statement.prepare(this.context.connect());
    try {
      final List<Map<String, Object>> result = statement.execute(parameter, startRow, lastRow, this.context.endpoint().timeOutResponse());
      for (Map<String, Object> cursor : result) {
        collector.add(User.Flag.build(cursor));
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
   ** Build and executes instruction to create the provided {@link User.Flag} in
   ** the Service Provider.
   **
   ** @param  entity             the populated {@link User.Flag} that are part
   **                            of the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link User.Flag}.
   **
   ** @return                    the {@link User.Flag} after the instructions
   **                            executed to create it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link User.Flag}.
   **
   ** @throws SystemException    if the {@link User.Flag} could not be created.
   */
  @Override
  public final User.Flag create(final User.Flag entity)
    throws SystemException {

    final String method = "create";
    trace(method, Loggable.METHOD_ENTRY);
    // set the relation
    entity.attribute(this.entity.primary().get(0).id(), this.relation.primary()[0]);
    // regardless of whether the flag already exists or not, there is always
    // only one instance of a flag henc delete any occurence first
    delete(entity);
    final DatabaseInsert statement = DatabaseInsert.build(this.context, this.entity, entity.keySet());
    try {
      // execute statement for usualy by verifying the operation has inserted at
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
   ** Build and executes instruction to modify the provided {@link User.Flag} in
   ** the Service Provider.
   **
   ** @param  uid                the identifier of a {@link User.Flag} to
   **                            modify in the  Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the partialy populated {@link User.Flag} that
   **                            are part of the instruction to modify it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link User.Flag}.
   **
   ** @return                    the {@link User.Flag} after the instructions
   **                            executed to modify it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link User.Flag}.
   **
   ** @throws SystemException    if the {@link User.Flag} could not be modified.
   */
  @Override
  public final User.Flag modify(final String uid, final User.Flag entity)
    throws SystemException {

    final DatabaseFilter filter = DatabaseFilter.build(
      DatabaseFilter.build(this.entity.primary().get(0).id(), this.relation.primary()[0], DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.build(this.entity.primary().get(1).id(), uid,                        DatabaseFilter.Operator.EQUAL)
    , DatabaseFilter.Operator.AND
    );
    if (!exists(filter))
      // the property does not exists since no exception was thrown, we have
      // to complain about the missing record
      throw DatabaseException.notFound(uid, this.entity.id());

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
   ** Build and executes instruction to delete the provided {@link User.Flag} in
   ** the Service Provider.
   **
   ** @param  entity             the partialy populated {@link User.Flag} that
   **                            are part of the instruction to delete it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link User.Flag}.
   **
   ** @throws SystemException    if the {@link User.Flag} could not be deleted.
   */
  @Override
  public final void delete(final User.Flag entity)
    throws SystemException {

    final String method = "delete";
    trace(method, Loggable.METHOD_ENTRY);
    DatabaseFilter filter = DatabaseFilter.build(this.entity.primary().get(0).id(), this.relation.primary()[0], DatabaseFilter.Operator.EQUAL);
    // may be we have to delete a particular flag 
    if (entity != null) {
      filter = DatabaseFilter.build(
        filter
      , DatabaseFilter.build(this.entity.primary().get(1).alias(), entity.name(), DatabaseFilter.Operator.EQUAL)
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
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gropued by functionalities
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link User} <code>FlagProvider</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  user               the superordinated {@link User} entity to join
   **                            with.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    an instance of <code>PropertyProvider</code>.
   **                            <br>
   **                            Possible object is <code>FlagProvider</code>
   **                            for type {@link User}
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static FlagProvider build(final Context context, final User user)
    throws SystemException {

    return new FlagProvider(context, user);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulk
  /**
   ** Assigns the collection of {@link User.Flag}s to an {@link User}.
   ** <br>
   ** The {@link User} this operation belongs to was passed to the contructor
   ** of this {@link Provider}.
   **
   ** @param  flag               the collection of {@link User.Flag}s to
   **                            associate with {@link User}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link User.Flag}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final void bulk(final List<User.Flag> flag)
    throws SystemException {

    final String method = "bulk";
    trace(method, Loggable.METHOD_ENTRY);
    // set relation identifier for flag to create
    final User.Flag      first     = flag.get(0).attribute(this.entity.primary().get(0).id(), this.relation.primary()[0]);
    final DatabaseInsert statement = DatabaseInsert.build(this.context, this.entity, first.keySet());
    try {
      // execute statement for the first flag usualy by verifying the operation
      // has inserted at least one record
      statement.execute(this.context.connect(), first, true, this.context.endpoint().timeOutResponse());
      // do all the stuff for the remaining flags
      for (int i = 1; i < flag.size(); i++) {
        // set relation identifier for flag to create
        final User.Flag cursor = flag.get(i).attribute(this.entity.primary().get(0).id(), this.relation.primary()[0]);
        // execute statement usualy by verifying the operation has inserted at
        // least one record
        statement.execute(cursor, true, this.context.endpoint().timeOutResponse());
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}