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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Dialect.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Dialect.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.persistence;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseException;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;

////////////////////////////////////////////////////////////////////////////////
// abstract class Dialect
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Declares global visible configuration properties.
 ** <p>
 ** Each dialect contains the same items, but the items have been different
 ** flavors that dialect.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public abstract class Dialect {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String USERNAME        = "USERNAME";
  public static final String ROLENAME        = "ROLENAME";
  public static final String PASSWORD        = "PASSWORD";

  public static final String ATTRIBUTE_NAME  = "attributename";
  public static final String ATTRIBUTE_VALUE = "attributevalue";
  public static final String PERMISSION      = "permission";
  public static final String OBJECT          = "object";

  private static final Map<String, Class<? extends Dialect>> DIALECT =
    new HashMap<String, Class<? extends Dialect>>() { {
      put("oracle.jdbc.OracleDriver",                     oracle.iam.identity.dbs.persistence.dialect.orcl.class);
      put("oracle.jdbc.driver.OracleDriver",              oracle.iam.identity.dbs.persistence.dialect.orcl.class);
      put("com.mysql.jdbc.Driver",                        oracle.iam.identity.dbs.persistence.dialect.msql.class);
      put("com.microsoft.sqlserver.jdbc.SQLServerDriver", oracle.iam.identity.dbs.persistence.dialect.msft.class);
      put("com.sybase.jdbc2.jdbc.SybDriver",              oracle.iam.identity.dbs.persistence.dialect.sy.class);
      put("com.sybase.jdbc3.jdbc.SybDriver",              oracle.iam.identity.dbs.persistence.dialect.sy.class);
      put("com.ibm.db2.jcc.DB2Driver",                    oracle.iam.identity.dbs.persistence.dialect.ibm.class);
    }
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the schema owning the administrative catalog supported by a data
   ** dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  protected final String                  schema;

  /**
   ** the administrative entities supported by a data dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<Entity,     DatabaseEntity> entity;

  /**
   ** the administrative catalog supported by a data dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<Catalog.Type, Catalog>      catalog;

  /**
   ** the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by a data dictionary.
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<Catalog.Type, Catalog>      permission;

  /**
   ** the administrative operations applicable on accounts and/or roles
   ** supported by a data dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<Operation,  String>         operation;

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
    ACCOUNT;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:8825496228074624240")
    private static final long serialVersionUID = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Operation
  // ~~~~~ ~~~~~~~~~
  /**
   ** The operational types perfomed by the administration.
   */
  public static enum Operation {
    // the account operation
    ACCOUNT_CREATE, ACCOUNT_DELETE, ACCOUNT_ENABLE, ACCOUNT_DISABLE
  , ACCOUNT_MODIFY, ACCOUNT_PASSWORD
  , ACCOUNT_PRIVILEGE_GRANT, ACCOUNT_PRIVILEGE_GRANT_WITH, ACCOUNT_PRIVILEGE_REVOKE
  , ACCOUNT_ROLE_GRANT, ACCOUNT_ROLE_GRANT_WITH, ACCOUNT_ROLE_REVOKE
  , ACCOUNT_OBJECT_GRANT, ACCOUNT_OBJECT_GRANT_WITH, ACCOUNT_OBJECT_REVOKE
  , ACCOUNT_FILTER_STATE
    // the role operation
  , ROLE_CREATE, ROLE_CREATE_PROTECTED, ROLE_DELETE, ROLE_ENABLE, ROLE_DISABLE
  , ROLE_MODIFY, ROLE_PROTECT, ROLE_UNPROTECTED
  , ROLE_PRIVILEGE_GRANT, ROLE_PRIVILEGE_GRANT_WITH, ROLE_PRIVILEGE_REVOKE
  , ROLE_ROLE_GRANT, ROLE_ROLE_GRANT_WITH, ROLE_ROLE_REVOKE
  , ROLE_OBJECT_GRANT, ROLE_OBJECT_GRANT_WITH, ROLE_OBJECT_REVOKE;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:3301717979448647687")
    private static final long serialVersionUID = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Dialect</code> that referes to entities that belongs to
   ** the specified schema.
   **
   ** @param  schema             the name of the schema that owns the dialect.
   */
  protected Dialect(final String schema) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.schema = schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountFilter
  /**
   ** Returns the filter that has to be applied on a search for accounts.
   **
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   */
  protected abstract DatabaseFilter accountFilter();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountTime
  /**
   ** Returns the filter that has to be applied on a search for accounts.
   **
   ** @param  timestamp          the time after that an account in the target
   **                            system should be created or modified to be
   **                            included in the returning collection.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   */
  protected abstract DatabaseFilter accountTime(final Date timestamp);

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
   */
  @SuppressWarnings("oracle.jdeveloper.java.null-map-return")
  protected DatabaseEntity entity(final Entity entity) {
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
  // Method:   catalog
  /**
   ** Fetchs a {@link Catalog} for the given key from the associated dialect.
   **
   ** @param  type               the {@link Catalog.Type} for the desired
   **                            database catalog.
   **
   ** @return                    the {@link Catalog} for the given catalog key.
   **                            If <code>null</code> was passed as
   **                            <code>permission</code> <code>null</code> will
   **                            be returned.
   **
   ** @throws ClassCastException if the object found for the given key is
   **                            not a {@link Catalog}.
   */
  @SuppressWarnings("oracle.jdeveloper.java.null-map-return")
  protected Catalog catalog(final Catalog.Type type) {
    // First, bounce bogus input
    if (type == null)
      return null;

    // Second, bounce bogus state
    if (this.catalog == null)
      this.catalog = installCatalog();

    // if no mapping is available return a null back to the caller to have an
    // fail safe behaviour; otherwise we can lookup the desired value in the
    // mapping.
    return this.catalog.get(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Fetchs a <code>String</code> for the given key from statement bundle or
   ** one of its parents.
   **
   ** @param  dictionary         the key for the desired string.
   **
   ** @return                    the string for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>resourceKey</code> an empty
   **                            string will be returned.
   */
  protected String operation(final Operation dictionary) {
    // First, bounce bogus input
    if (dictionary == null)
      return SystemConstant.EMPTY;

    // Second, bounce bogus state
    if (this.operation == null)
      this.operation = installOperation();

    // if no mapping is available return an empty string back to the caller to
    // have an fail safe behaviour; otherwise we can lookup the desired value in
    // the mapping.
    return this.operation.get(dictionary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** Fetchs a <code>String</code> for the given key from statement bundle or
   ** one of its parents.
   **
   ** @param  type               the {@link Catalog.Type} for the desired
   **                            permission catalog.
   **
   ** @return                    the string for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>resourceKey</code> an empty
   **                            string will be returned.
   **
   ** @throws ClassCastException if the object found for the given key is
   **                            not a string.
   */
  @SuppressWarnings("oracle.jdeveloper.java.null-map-return")
  protected Catalog permission(final Catalog.Type type) {
    // First, bounce bogus input
    if (type == null)
      return null;

    // Second, bounce bogus state
    if (this.permission == null)
      this.permission = installPermission();

    // if no mapping is available return an empty string back to the caller to
    // have an fail safe behaviour; otherwise we can lookup the desired value in
    // the mapping.
    return this.permission.get(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** The factory mathod to instanciate a <code>Dialect</code>. The concrete
   ** implementation is derived from the JDBC <code>driverClass</code>.
   **
   ** @param  driverClass        the JDBC driver class to lookup the
   **                            <code>Dialect</code> for.
   **
   ** @return                    the <code>Dialect</code> specific for the
   **                            specified JDBC <code>driverClass</code>.
   **
   ** @throws TaskException      if the requested <code>Dialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the specified
   **                            <code>driverClass</code>.
   */
  public static Dialect create(final String driverClass)
    throws TaskException {

    final Class<? extends Dialect> dialect = DIALECT.get(driverClass);
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
  // Method:   installCatalog
  /**
   ** Creates the administrative catalog supported by a data dictionary.
   **
   ** @return                    the administrative catalog supported by the
   **                            data dictionary.
   */
  protected abstract Map<Catalog.Type, Catalog> installCatalog();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation
  /**
   ** Creates the administrative operations applicable on accounts and/or roles
   ** supported by a data dictionary.
   **
   ** @return                    the administrative operations applicable on
   **                            accounts and/or roles supported by a data
   **                            dictionary.
   */
  protected abstract Map<Operation, String> installOperation();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installPermission
  /**
   ** Creates the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by a data dictionary.
   **
   ** @return                    the administrative catalog supported by the
   **                            data dictionary providing informations about
   **                            granted permissions.
   */
  protected abstract Map<Catalog.Type, Catalog> installPermission();

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