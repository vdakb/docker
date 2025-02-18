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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   Repository.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Repository.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.persistence;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import java.math.BigDecimal;

import java.sql.Connection;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseException;

////////////////////////////////////////////////////////////////////////////////
// abstract class Repository
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Declares global visible configuration properties.
 ** <p>
 ** Each dialect contains the same items, but the items have been different
 ** flavors that dialect.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public abstract class Repository {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Map<String, Class<? extends Repository>> REPOSITORY =
    new HashMap<String, Class<? extends Repository>>() { {
      put("oracle.jdbc.OracleDriver",  oracle.iam.identity.ots.persistence.dialect.orcl.class);
    }
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the administrative entities supported by a data dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<Entity, DatabaseEntity> entity;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Entity
  // ~~~~~ ~~~~~~
  /**
   ** The entity types handled by the administration.
   */
  public static enum Entity {
    // the entities
      CATALOGROLE
    , CATALOGAPPLICATION
    , CATALOGENTITLEMENT
    , NAMESPACE
    , NAMESPACEVALUE
    , NAMESPACEENTITLEMENT
    , ENTITLEMENT
    , PROPERTYCOLUMN
    , PROPERTYENTITLEMENT
    , PROPERTYENTITLEMENTJOIN
    , PROPERTYCATALOGJOIN
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-3400959447185635382")
    private static final long serialVersionUID = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Repository</code> that referes to entities that doesn't
   ** belongs to any schema (not needed due to built-in functionality of entire
   ** implementation).
   */
  protected Repository() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespaceValueFilter
  /**
   ** Returns the filter that has to be applied on a search for lookup values in
   ** the Oracle Identity Manager.
   **
   ** @param  namespace          the name of the <code>Lookup Definition</code>
   **                            the values has to be obtained from the database
   **                            of Oracle Identity Manager.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for roles in the catalog.
   **
   ** @throws TaskException      if the object found for the entity keys are
   **                            not a {@link DatabaseEntity}s.
   */
  public abstract DatabaseFilter namespaceValueFilter(final String namespace)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogRoleFilter
  /**
   ** Returns the filter that has to be applied on a search for roles in the
   ** catalog.
   **
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for roles in the catalog.
   **
   ** @throws TaskException      if the object found for the entity keys are
   **                            not a {@link DatabaseEntity}s.
   */
  public abstract DatabaseFilter catalogRoleFilter()
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogApplicationFilter
  /**
   ** Returns the filter that has to be applied on a search for application
   ** instances in the catalog.
   **
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for application instances in the
   **                            catalog.
   **
   ** @throws TaskException      if the object found for the entity keys are
   **                            not a {@link DatabaseEntity}s.
   */
  public abstract DatabaseFilter catalogApplicationFilter()
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogEntitlementFilter
  /**
   ** Returns the filter that has to be applied on a search for entitlements in
   ** the catalog that belongs to an specific application instance described by
   ** <code>objectKey</code> and <code>resourceKey</code>.
   **
   ** @param  objectKey          the system identifier of the
   **                            <code>Resource Object</code> the entitlements
   **                            belongs to.
   ** @param  resourceKey        the system identifier of the
   **                            <code>IT Resource</code> the entitlements
   **                            belongs to.
   ** @param  lookupKey          the system identifier of the
   **                            <code>Lookup Definition</code> the entitlements
   **                            belongs to.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for for entitlements in the catalog that
   **                            belongs to an specific application instance.
   **
   ** @throws TaskException      if the object found for the entity keys are
   **                            not a {@link DatabaseEntity}s.
   */
  public abstract DatabaseFilter catalogEntitlementFilter(final long objectKey, final long resourceKey, final BigDecimal lookupKey)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespaceEntitlementFilter
  /**
   ** Returns the filter that has to be applied on a search for entitlements in
   ** <code>Lookup Definition</code>s that belongs to an specific application
   ** instance described by <code>objectKey</code> and <code>resourceKey</code>.
   **
   ** @param  resource           the system identifier of the
   **                            <code>Resource Object</code> the entitlements
   **                            belongs to.
   ** @param  endpoint           the system identifier of the
   **                            <code>IT Resource</code> the entitlements
   **                            belongs to.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for for entitlements in
   **                            <code>Lookup Definition</code>s that belongs to
   **                            an specific application instance.
   **
   ** @throws TaskException      if the object found for the entity keys are
   **                            not a {@link DatabaseEntity}s.
   */
  public abstract DatabaseFilter namespaceEntitlementFilter(final long resource, final long endpoint)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Fetchs a {@link DatabaseEntity} for the given key from the associated
   ** dictionary.
   **
   ** @param  entity             the key for the desired {@link DatabaseEntity}.
   **
   ** @return                    the {@link DatabaseEntity} for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>resourceKey</code>
   **                            <code>null</code> will be returned.
   **
   ** @throws TaskException      if the object found for the given key is
   **                            not a {@link DatabaseEntity}.
   */
  @SuppressWarnings("oracle.jdeveloper.java.null-map-return")
  public DatabaseEntity entity(final Entity entity)
    throws TaskException {

    // First, bounce bogus input
    if (entity == null)
      return null;

    // Second, bounce bogus state
    if (this.entity == null)
      this.entity= installEntity();

    // if no mapping is available return a null back to the caller to have an
    // fail safe behaviour; otherwise we can lookup the desired value in the
    // mapping.
    return this.entity.get(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** The factory mathod to instanciate a <code>Repository</code>. The concrete
   ** implementation is derived from the JDBC <code>driverClass</code>.
   **
   ** @param  driverClass        the JDBC driver class to lookup the
   **                            <code>Repository</code> for.
   **
   ** @return                    the <code>Repository</code> specific for the
   **                            specified JDBC <code>driverClass</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Repository create(final String driverClass)
    throws TaskException {

    final Class<? extends Repository> dialect = REPOSITORY.get(driverClass);
    // we dont have an implementation for the driver if it is not registered
    if (dialect == null)
      throw TaskException.classNotFound(driverClass);

    try {
      // a little bit reflection
      return dialect.newInstance();
    }
    catch (InstantiationException e) {
      throw TaskException.classNotCreated(dialect.getName());
    }
    catch (IllegalAccessException e) {
      throw TaskException.classNoAccess(dialect.getName());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installEntity
  /**
   ** Creates the administrative entities supported by a data dictionary.
   **
   ** @return                    the administrative entities supported by
   **                            the data dictionary.
   */
  protected abstract Map<Entity, DatabaseEntity> installEntity();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime
  /**
   ** The current time will be different from this system time.
   ** <p>
   ** To be able to plugin your own method this is the placeholder method to
   ** fetch the server time from the target system.
   ** <p>
   ** This current implementation check if the system is connected and ask the
   ** connected server for the system timestamp. If the connection is not valid
   ** at the time this method is invoked it returns simple the current data of
   ** the machine where this library is used.
   **
   ** @param  connection         the {@link Connection} to used for the search.
   **
   ** @return                    the timestamp of the remote system if
   **                            applicable; the local time otherwise.
   **
   ** @throws TaskException      if the operation fails
   */
  public abstract Date systemTime(final Connection connection)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizeError
  /**
   ** Returns the implementation specific error code for a vendor specific code.
   ** <p>
   ** The vendor code is taken from an SQLException that is catched somewhere
   ** and wrapped in a {@link DatabaseException}. This prefix the vendor
   ** specific code constantly with <code>SQL-</code>.
   **
   ** @param  throwable          the exception thrown by a process step that
   **                            may contain a vendor spefific error code.
   **
   ** @return                    a implementation specific error code if it's
   **                            translatable; otherwise <code>DBA-0001</code>
   **                            will be returned.
   */
  protected abstract String normalizeError(final DatabaseException throwable);
}