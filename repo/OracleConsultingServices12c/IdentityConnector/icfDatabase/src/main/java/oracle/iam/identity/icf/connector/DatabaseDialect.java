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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Database Connector

    File        :   DatabaseDialect.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseDialect.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.math.BigDecimal;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashSet;

import oracle.iam.identity.icf.foundation.SystemConstant;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseSearch;
import oracle.iam.identity.icf.dbms.DatabaseEndpoint;
import oracle.iam.identity.icf.dbms.DatabaseException;

import oracle.iam.identity.icf.connector.dialect.msft;
import oracle.iam.identity.icf.connector.dialect.msql;
import oracle.iam.identity.icf.connector.dialect.orcl;
import oracle.iam.identity.icf.connector.dialect.syb;
import oracle.iam.identity.icf.connector.dialect.ibm;

import org.identityconnectors.framework.common.objects.AttributeInfo;

////////////////////////////////////////////////////////////////////////////////
// abstract class DatabaseDialect
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Declares global visible configuration properties.
 ** <p>
 ** Each dialect contains the same items, but the items have been different
 ** flavors that dialect.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DatabaseDialect {

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

  /** mandatory */
  public static final Set<AttributeInfo.Flags>                                REQUIRED    = EnumSet.of(AttributeInfo.Flags.REQUIRED);
  /** not returned by default and not readable*/
  public static final Set<AttributeInfo.Flags>                                SENSITIVE   = EnumSet.of(AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT, AttributeInfo.Flags.NOT_READABLE);
  /** operational attributes are not creatable, updatabale, returned by default */
  public static final Set<AttributeInfo.Flags>                                OPERATIONAL = EnumSet.of(AttributeInfo.Flags.NOT_CREATABLE, AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT);

  static final Map<DatabaseEndpoint.Driver, Class<? extends DatabaseDialect>> DIALECT     = new EnumMap<DatabaseEndpoint.Driver, Class<? extends DatabaseDialect>>(DatabaseEndpoint.Driver.class);

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    DIALECT.put(DatabaseEndpoint.Driver.ORACLE2,   orcl.class);
    DIALECT.put(DatabaseEndpoint.Driver.ORACLE4,   orcl.class);
    DIALECT.put(DatabaseEndpoint.Driver.MYSQL,     msql.class);
    DIALECT.put(DatabaseEndpoint.Driver.SQLSERVER, msft.class);
    DIALECT.put(DatabaseEndpoint.Driver.SYBASE2,   syb.class);
    DIALECT.put(DatabaseEndpoint.Driver.SYBASE3,   syb.class);
    DIALECT.put(DatabaseEndpoint.Driver.UDB,       ibm.class);
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
  protected final String                                schema;

  /**
   ** the administrative entities supported by a data dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<DatabaseSchema.Entity, DatabaseEntity>    entity;

  /**
   ** the administrative catalog supported by a data dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<DatabaseSchema.Catalog, DatabaseCatalog> catalog;

  /**
   ** the administrative operations providing granted permissions on
   ** objects to accounts and/or roles supported by a data dictionary.
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<DatabaseSchema.Catalog, DatabaseCatalog> permission;

  /**
   ** the administrative operations applicable on accounts and/or roles
   ** supported by a data dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<Operation,  String>                     operation;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

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
   ** Constructs a <code>DatabaseDialect</code> that referes to entities that
   ** belongs to the specified schema.
   **
   ** @param  schema             the name of the schema that owns the dialect.
   **                            Allowed object is {@link String}.
   */
  protected DatabaseDialect(final String schema) {
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
   ** @param  timestamp          the time after that an account in the target
   **                            system should be created or modified to be
   **                            included in the returning collection.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   **
   ** @throws DatabaseException  if the requested <code>DatabaseDialect</code>
   **                            class is not found on the classpath or could
   **                            not be either created or accessed for the
   **                            specified <code>driverClass</code>.
   */
  protected final DatabaseFilter accountFilter(final Date timestamp)
    throws DatabaseException {

    DatabaseFilter filter = accountFilter();
    if (timestamp != null)
      filter = DatabaseFilter.build(accountTime(timestamp), filter, DatabaseFilter.Operator.AND);
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountFilter
  /**
   ** Returns the filter that has to be applied on a search for accounts.
   **
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
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
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    a {@link DatabaseFilter} to be applied on a
   **                            search for user accounts.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  protected abstract DatabaseFilter accountTime(final Date timestamp);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Fetchs a {@link DatabaseEntity} for the given key from the associated
   ** dictionary.
   **
   ** @param  entity             the key for the desired {@link DatabaseEntity}.
   **                            Allowed object is {@link DatabaseSchema.Entity}.
   **                            <br>
   **                            Allowed object is
   **                            {@link DatabaseSchema.Entity}.
   **
   ** @return                    the {@link DatabaseEntity} for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>resourceKey</code>
   **                            <code>null</code> will be returned.
   **                            Possible object {@link DatabaseEntity}.
   **                            <br>
   **                            Possible object is {@link DatabaseEntity}.
   */
  public DatabaseEntity entity(final DatabaseSchema.Entity entity) {
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
   ** Fetchs a {@link DatabaseCatalog} for the given key from the associated
   ** dialect.
   **
   ** @param  type               the {@link DatabaseSchema.Catalog} for the
   **                            desired database catalog.
   **                            <br>
   **                            Allowed object is
   **                            {@link DatabaseSchema.Catalog}.
   **
   ** @return                    the {@link DatabaseCatalog} for the given
   **                            catalog key.
   **                            If <code>null</code> was passed as
   **                            <code>permission</code> <code>null</code> will
   **                            be returned.
   **                            <br>
   **                            Possible object is {@link DatabaseCatalog}.
   **
   ** @throws ClassCastException if the object found for the given key is
   **                            not a {@link DatabaseCatalog}.
   */
  public DatabaseCatalog catalog(final DatabaseSchema.Catalog type) {
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
   **                            <br>
   **                            Allowed object is {@link Operation}.
   **
   ** @return                    the string for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>resourceKey</code> an empty
   **                            string will be returned.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String operation(final Operation dictionary) {
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
   ** @param  type               the {@link DatabaseSchema.Catalog} for the
   **                            desired permission catalog.
   **                            <br>
   **                            Allowed object is
   **                            {@link DatabaseSchema.Catalog}.
   **
   ** @return                    the string for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>resourceKey</code> an empty
   **                            string will be returned.
   **                            <br>
   **                            Possible object is {@link DatabaseCatalog}.
   **
   ** @throws ClassCastException if the object found for the given key is
   **                            not a string.
   */
  public DatabaseCatalog permission(final DatabaseSchema.Catalog type) {
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
  // Method:   build
  /**
   ** The factory mathod to instanciate a <code>DatabaseDialect</code>. The
   ** concrete implementation is derived from the JDBC <code>driverClass</code>.
   **
   ** @param  driver             the JDBC driver class to lookup the
   **                            <code>DatabaseDialect</code> for.
   **                            <br>
   **                            Allowed object is
   **                            {@link DatabaseEndpoint.Driver}.
   **
   ** @return                    the <code>DatabaseDialect</code> specific for
   **                            the specified JDBC <code>driverClass</code>.
   **                            <br>
   **                            Possible object is {@link DatabaseDialect}.
   **
   ** @throws SystemException    if the requested <code>DatabaseDialect</code>
   **                            class is not found on the classpath or could
   **                            not be either created or accessed for the
   **                            specified <code>driverClass</code>.
   */
  public static DatabaseDialect build(final DatabaseEndpoint.Driver driver)
    throws SystemException {

    final Class<? extends DatabaseDialect> dialect = DIALECT.get(driver);
    // we dont have an implementation for the driver if it is not registered
    if (dialect == null)
      throw SystemException.classNotFound(driver.id());

    try {
      // a little bit reflection
      return dialect.newInstance();
    }
    catch (InstantiationException e) {
      throw SystemException.classNotCreated(dialect.getName());
    }
    catch (IllegalAccessException e) {
      throw SystemException.classNoAccess(dialect.getName());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountSearch
  /**
   ** Reads all identifiers from the data dictionary that belongs to accounts
   ** which are created since the specified <code>timesatmp</code>.
   **
   ** @param  context            the Database Service endpoint connection which
   **                            is used to discover this
   **                            {@link DatabaseSchema}.
   **                            <br>
   **                            Allowed object is {@link DatabaseContext}.
   ** @param  timestamp          the {@link Date} defining the time the accounts
   **                            are created after.
   **                            If <code>null</code> are passed all accounts
   **                            will be returned.
   **                            <br>
   **                            Allowed object is {@link Date}.
   ** @param  startRow           the start row of a page to fetch from the
   **                            Database Server.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  lastRow            the last row of a page to fetch from the
   **                            Database Server.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  timeOut            the timeout period the client will wait for a
   **                            response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a {@link Set} containing the available values
   **                            for the specified query string.
   **                            <br>
   **                            Possible object is {@link Set} where each.
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final Set<BigDecimal> accountSearch(final DatabaseContext context, final Date timestamp, final long startRow, final long lastRow, final int timeOut)
    throws SystemException {

    // get the names of the attributes the values has to be fetched from the
    // account entity providing the details of an account.
    final DatabaseEntity entity = entity(DatabaseSchema.Entity.Account);
    try {
      // obtain a connection form the pool
      context.connect();
      // build the statement with the filter to fetch an account
      // account entity providing the details of an account.
      final DatabaseSearch statement = DatabaseSearch.build(context, entity, accountFilter(timestamp), entity.attribute());
      statement.prepare(context.unwrap());

      // build the filter to fetch an account
      // account entity providing the details of an account.
      // perform the database operation to fetch an account profile
      final List<Map<String, Object>> nativly = statement.execute(startRow, lastRow, timeOut);
      statement.close();

      // reverse the native attribute name to the logical representation and
      // return it to the invoker by take care about the sort order
      final Set<BigDecimal> logical = new LinkedHashSet<BigDecimal>(nativly.size());
      for (Map<String, Object> account : nativly) {
        if (account.size() != 1)
          throw SystemException.unhandled("Could never happens");
        logical.add((BigDecimal)account.get(entity.attribute().get(0).alias()));
      }
      return logical;
    }
    finally {
      // release connection to the pool and free up resources
      context.disconnect();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installEntity
  /**
   ** Creates the administrative entities supported by a data dictionary.
   **
   ** @return                    the administrative entities supported by
   **                            the data dictionary.
   **                            <br>
   **                            Possible object is {@link Map} where each.
   **                            element is of type
   **                            {@link DatabaseSchema.Entity} for the key and
   **                            {@link DatabaseEntity} for the value.
   */
  protected abstract Map<DatabaseSchema.Entity, DatabaseEntity> installEntity();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installCatalog
  /**
   ** Creates the administrative catalog supported by a data dictionary.
   **
   ** @return                    the administrative catalog supported by the
   **                            data dictionary.
   **                            <br>
   **                            Possible object is {@link Map} where each.
   **                            element is of type
   **                            {@link DatabaseSchema.Catalog} for the key and
   **                            {@link DatabaseCatalog} for the value.
   */
  protected abstract Map<DatabaseSchema.Catalog, DatabaseCatalog> installCatalog();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installOperation
  /**
   ** Creates the administrative operations applicable on accounts and/or roles
   ** supported by a data dictionary.
   **
   ** @return                    the administrative operations applicable on
   **                            accounts and/or roles supported by a data
   **                            dictionary.
   **                            <br>
   **                            Possible object is {@link Map} where each.
   **                            element is of type {@link Operation} for the
   **                            key and {@link String} for the value.
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
   **                            <br>
   **                            Possible object is {@link Map} where each.
   **                            element is of type
   **                            {@link DatabaseSchema.Catalog} for the key and
   **                            {@link DatabaseCatalog} for the value.
   */
  protected abstract Map<DatabaseSchema.Catalog, DatabaseCatalog> installPermission();
}