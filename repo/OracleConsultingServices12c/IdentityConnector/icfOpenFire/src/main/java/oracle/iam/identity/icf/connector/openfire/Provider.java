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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   Provider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Provider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire;

import java.util.Map;
import java.util.List;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseExists;
import oracle.iam.identity.icf.dbms.DatabaseAttribute;
import oracle.iam.identity.icf.dbms.DatabaseException;

import oracle.iam.identity.icf.connector.openfire.schema.Entity;

////////////////////////////////////////////////////////////////////////////////
// abstract class Provider
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** The descriptor to handle entities.
 ** <p>
 ** This object contains factory methods for each Java provider interface and
 ** Java element interface generated in the
 ** <code>oracle.iam.identity.sysauthz.schema</code> package.
 ** <p>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Provider<T extends Entity> extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The native name of the identifier attribute. */
  protected static final String           UID     = "username";

  /** The native name of the identifier attribute. */
  protected static final String           GID     = "groupname";

  /** The native name of the identifier attribute. */
  protected static final String           SID     = "serviceid";

  /** The native name of the identifier attribute */
  protected static final String           RID     = "roomid";

  /** The native name of the identifier attribute. */
  protected static final String           JID     = "jid";

  /** The alias name of the name attribute. */
  protected static final String           NAME    = "name";

  /** The alias name of the value attribute. */
  protected static final String           VALUE   = "propvalue";

  /** The alias name of the created attribute. */
  protected static final String           CREATED = "creationdate";

  /** The alias name of the created attribute. */
  protected static final String           UPDATED = "modificationdate";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the {@link Context} to operate on. */
  protected final Context                 context;

  /** the {@link DatabaseEntity} to access. */
  protected final DatabaseEntity          entity;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code> that operates on the specified
   ** {@link Context} <code>context</code> to maintain the entity for the given
   ** <code>name</code>.
   ** <p>
   ** The {@link DatabaseEntity} required is obtaind by querying the schema
   ** definition of the specified {@link Context} <code>context</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  name               the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  protected Provider(final Context context, final String name)
    throws SystemException {

    // ensure inheritance
    this(context, context.schema().dictionary(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code> that operates on the specified
   ** {@link Context} <code>context</code> to maintain the entity for the given
   ** <code>name</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  name               the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          the collection of attributes accomplishing the
   **                            description of an entity.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseAttribute}.
   */
  protected Provider(final Context context, final String name, final List<DatabaseAttribute> attribute) {
    // ensure inheritance
    this(context, DatabaseEntity.build(context.endpoint().databaseSchema(), name, new int[]{0}, attribute));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code> that operates on the specified
   ** {@link Context} <code>context</code> to maintain the given
   ** {@link DatabaseEntity} <code>entity</code>.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  entity             the {@link DatabaseEntity} maintained by this
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   */
  protected Provider(final Context context, final DatabaseEntity entity) {
    // ensure inheritance
    super(context);

    // initialize instance attributes
    this.context = context;
    this.entity  = entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Access methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Returns the {@link DatabaseEntity} maintained by this provider.
   **
   ** @return                    the {@link DatabaseEntity} maintained by this
   **                            provider.
   **                            <br>
   **                            Possible object is {@link DatabaseEntity}.
   */
  public final DatabaseEntity entity() {
    return this.entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Build a query to verify the existance of a known entity at the Service
   ** Provider.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    <code>true</code> if a matching entity exists
   **                            for the criteria.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    if a user already exists.
   */
  public final boolean exists(final DatabaseFilter criteria)
    throws SystemException {
    
    final String method = "exists";
    trace(method, Loggable.METHOD_ENTRY);
    final DatabaseExists statement = DatabaseExists.build(this.context, this.entity, criteria);
    try {
      final List<Map<String, Object>> result = statement.execute(this.context.connect(), this.context.endpoint().timeOutResponse());
      if (result.size() > 1)
        throw DatabaseException.ambiguous(criteria.toString(), this.entity.id());
      else
        return result.size() == 1;
    }
    finally {
      trace(method, Loggable.METHOD_ENTRY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Reads one entity from the Service Provider that matches the specified
   ** filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    an {@link Entity}
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws SystemException    if a user already exists.
   */
  public abstract T lookup(final DatabaseFilter criteria)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select
  /**
   ** Reads all entities from the Service Provider that matches the specified
   ** filter criteria.
   **
   ** @param  criteria           the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    a {@link List} containing the available values
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Entity} for type
   **                            <code>T</code>.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public abstract List<T> select(final DatabaseFilter criteria)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Reads all entities from the Service Provider that matches the specified
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
   ** @return                    a {@link List} containing the available values
   **                            that matches the specified filter criteria.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Entity} for type
   **                            <code>T</code>.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public abstract List<T> search(final DatabaseFilter criteria, final long startRow, final long lastRow)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (Provider)
  /**
   ** Build and executes instruction to create the provided {@link Entity} in
   ** the Service Provider.
   **
   ** @param  entity             the populated {@link Entity} that are part of
   **                            the instruction to create it at the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Entity} for type
   **                            <code>T</code>.
   **
   ** @return                    the {@link Entity} after the instructions
   **                            executed to create it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link Entity} for type
   **                            <code>T</code>.
   **
   ** @throws SystemException    the {@link Entity} of type <code>T</code>
   **                            could not be created.
   */
  public abstract T create(final T entity)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Build and executes instruction to modify the provided {@link Entity} in
   ** the Service Provider.
   **
   ** @param  identifier         the identifier of an {@link Entity} to modify
   **                            in the  Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the partialy populated {@link Entity} that are
   **                            part of the instruction to modify it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Entity} for type
   **                            <code>T</code>.
   **
   ** @return                    the {@link Entity} after the instructions
   **                            executed to modify it in the Service Provider.
   **                            <br>
   **                            Possible object is {@link Entity} for type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if the {@link Entity} of type <code>T</code>
   **                            could not be modified.
   */
  public abstract T modify(final String identifier, final T entity)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Build and executes instruction to delete the provided {@link Entity} in
   ** the Service Provider.
   **
   ** @param  entity             the partialy populated {@link Entity} that are
   **                            part of the instruction to delete it in the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Entity} for type
   **                            <code>T</code>.
   **
   ** @throws SystemException    the {@link Entity} of type <code>T</code>
   **                            could not be deleted.
   */
  public abstract void delete(final T entity)
    throws SystemException;
}