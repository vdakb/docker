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

    File        :   DatabaseContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.math.BigDecimal;

import java.util.Map;
import java.util.Set;
import java.util.Date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.StringUtility;

import oracle.iam.identity.icf.dbms.DatabaseError;
import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseEndpoint;
import oracle.iam.identity.icf.dbms.DatabaseStatement;
import oracle.iam.identity.icf.dbms.DatabaseException;

import oracle.iam.identity.icf.resource.DatabaseBundle;

///////////////////////////////////////////////////////////////////////////////
// class DatabaseContext
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseContext</code> wraps the JDBC connection.
 ** <p>
 ** Define the test method meaning the wrapped connection is still valid.
 ** <br>
 ** Defines some useful method to work with prepared statements.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
 public class DatabaseContext extends oracle.iam.identity.icf.dbms.DatabaseContext {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private DatabaseSchema  schema  = null;
  private DatabaseDialect dialect = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseContext</code> which is associated with the
   ** specified {@link DatabaseEndpoint} for configuration purpose.
   **
   ** @param  endpoint           the {@link DatabaseEndpoint}
   **                            <code>IT Resource</code> definition where this
   **                            connector is associated with.
   **                            Allowed object is {@link DatabaseEndpoint}.
   */
  private DatabaseContext(final DatabaseEndpoint endpoint) {
    // ensure inherinstance
    super(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DatabaseContext</code> which is
   ** associated with the specified {@link DatabaseEndpoint} as the logger.
   **
   ** @param  endpoint           the {@link DatabaseEndpoint} IT Resource
   **                            definition where this connector is associated
   **                            with.
   **                            Allowed object is {@link DatabaseEndpoint}.
   **
   ** @return                    the context.
   **                            Possible object is
   **                            <code>DatabaseContext</code>.
   */
  public static DatabaseContext build(final DatabaseEndpoint endpoint) {
    return new DatabaseContext(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Return the schema upon request.
   **
   ** @return                    the schema.
   **                            Possible object {@link DatabaseSchema}.
   */
  public DatabaseSchema schema() {
    if (this.schema == null)
      this.schema = endpoint().fetchSchema() ? new DatabaseSchema.Server(this) : new DatabaseSchema.Static(this);
    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime (overridden)
  /**
   ** The Database Server current time will be different from the local time.
   ** <p>
   ** This current implementation ask the connected server for the current
   ** time using the configured property
   ** <code>Database.OIM.Feature.SYSTEM_TIMESTAMP</code>.
   ** <p>
   ** The assumption is that the configuration provides a single query statement
   ** that is able to fectch the system timestamp from the database server. For
   ** example a statement like:
   ** <pre>
   **   SELECT systimestamp FROM dual
   ** </pre>
   ** will work for an Oracle Database.
   **
   ** @param  query              the statement to lookup the current system time
   **                            of the specified {@link Connection}.
   **
   ** @return                    the timestamp of the remote system.
   **
   ** @throws SystemException    if the operation fails.
   */
  public Date systemTime(final String query)
    throws SystemException {

    // prevent bogus input
    if (StringUtility.empty(query))
      throw SystemException.argumentNull("query");

    PreparedStatement statement = null;
    ResultSet         resultSet = null;
    Timestamp         timestamp =null;
    try {
      statement = DatabaseStatement.createPreparedStatement(unwrap(), query);
      resultSet = statement.executeQuery();
      timestamp = resultSet.next() ? resultSet.getTimestamp(1) : new Timestamp(0L);
      return new Date(timestamp.getTime());
    }
    catch (SQLException e) {
      throw DatabaseException.normalized(statement, e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   test
  /**
   ** Determines if the underlying JDBC {@link java.sql.Connection} is valid.
   **
   ** @throws RuntimeException   if the underlying JDBC
   **                            {@link java.sql.Connection} is not valid
   **                            otherwise do nothing.
   */
  public void test() {
    final String method = "test";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // setAutoCommit() requires a connection to the server in most cases. But
      // some drivers may cache the autoCommit value and only connect to the
      // server if the value changes (namely DB2). So we have to actually change
      // the value twice and then set it back to the original value if the
      // connection is still valid. setAutoCommit() is very quick so 2 round
      // trips shouldn't be that bad.

      // This has the BAD side effect of actually causing preceding partial
      // transactions to be committed at this point. Also, PostgreSQL apparently
      // caches BOTH operations, so this still does not work against that DB.
      unwrap().setAutoCommit(!unwrap().getAutoCommit());
      unwrap().setAutoCommit(!unwrap().getAutoCommit());
      commit();
      debug(method, "connection tested");
    }
    catch (Exception e) {
      // anything, not just SQLException if the connection is not valid anymore,
      // a new one will be created, so there is no need to set auto commit back
      // to its original value
      try {
        rollback();
      }
      catch (SystemException x) {
        ; // intentionally left blank
      }
    }
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountSearch
  /**
   ** Reads all identifiers from the data dictionary that belongs to accounts
   ** which are created since the specified <code>timesatmp</code>.
   **
   ** @param  timestamp          the {@link Date} defining the time the accounts
   **                            are created after.
   **                            If <code>null</code> are passed all accounts
   **                            will be returned.
   ** @param  startRow           the start row of a page to fetch from the
   **                            Database Server.
   ** @param  lastRow            the last row of a page to fetch from the
   **                            Database Server.
   **
   ** @return                    a <code>Set</code> containing the available
   **                            values for the specified query string.
   **                            Each entry in the returned list is a
   **                            {@link String} with the name of the value.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public final Set<BigDecimal> accountSearch(final Date timestamp, final long startRow, final long lastRow)
    throws SystemException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.accountSearch(this, timestamp, startRow, lastRow, endpoint().timeOutResponse());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectEntity
  /**
   ** Fetchs a {@link DatabaseEntity} for the given key from the associated
   ** dictionary.
   **
   ** @param  entity             the key for the desired {@link DatabaseEntity}.
   **                            Allowed object is {@link DatabaseSchema.Entity}.
   **
   ** @return                    the {@link DatabaseEntity} for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>dictionary</code>
   **                            <code>null</code> will be returned.
   **                            Possible object {@link DatabaseEntity}.
   **
   ** @throws SystemException    if the requested <code>DatabaseDialect</code>
   **                            class is not found on the classpath or could
   **                            not be either created or accessed for the
   **                            specified <code>driverClass</code>.
   */
  public final DatabaseEntity dialectEntity(final DatabaseSchema.Entity entity)
    throws SystemException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.entity(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectCatalog
  /**
   ** Fetchs a {@link DatabaseCatalog} for the given type from the associated
   ** dictionary.
   **
   ** @param  type               the type of the desired
   **                            {@link DatabaseCatalog}.
   **                            Allowed object is {@link DatabaseSchema.Catalog}.
   **
   ** @return                    the {@link DatabaseCatalog} for the given
   **                            catalog type.
   **                            If <code>null</code> was passed as
   **                            <code>permission</code> <code>null</code> will
   **                            be returned.
   **                            Possible object {@link DatabaseCatalog}.
   **
   ** @throws SystemException    if the requested <code>DatabaseDialect</code>
   **                            class is not found on the classpath or could
   **                            not be either created or accessed for the
   **                            specified <code>driverClass</code>.
   */
  public final DatabaseCatalog dialectCatalog(final DatabaseSchema.Catalog type)
    throws SystemException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.catalog(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectOperation
  /**
   ** Fetchs a <code>String</code> for the given key from statement bundle or
   ** one of its parents.
   **
   ** @param  operation          the key for the desired operation string.
   **                            Allowed object
   **                            {@link DatabaseDialect.Operation}.
   **
   ** @return                    the string for the given operation.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>dictionary</code> an empty
   **                            string will be returned.
   **                            Possible object {@link String}.
   **
   ** @throws SystemException    if the requested <code>DatabaseDialect</code>
   **                            class is not found on the classpath or could
   **                            not be either created or accessed for the
   **                            specified <code>driverClass</code>.
   */
  public final String dialectOperation(final DatabaseDialect.Operation operation)
    throws SystemException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.operation(operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialectPermission
  /**
   ** Fetchs a {@link DatabaseCatalog} for the given permission type.
   **
   ** @param  type               the type of the desired
   **                            {@link DatabaseCatalog}.
   **                            <br>
   **                            Allowed object is {@link DatabaseSchema.Catalog}.
   **
   ** @return                    the {@link DatabaseCatalog} for the given
   **                            permission type. If <code>null</code> is passed
   **                            as <code>permission</code> <code>null</code>
   **                            will be returned.
   **                            <br>
   **                            Possible object is {@link DatabaseCatalog}.
   **
   ** @throws SystemException    if the requested <code>DatabaseDialect</code>
   **                            class is not found on the classpath or could
   **                            not be either created or accessed for the
   **                            specified <code>driverClass</code>.
   */
  public final DatabaseCatalog dialectPermission(final DatabaseSchema.Catalog type)
    throws SystemException {

    if (this.dialect == null)
      installDialect();

    return this.dialect.permission(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseValueTemplate
  /**
   ** Returns a string with all placeholders substituted by the corresponding
   ** value of the provided map.
   ** <p>
   ** A placeholder in generell is specified by
   ** <code>${<i>placeholder name</i>}</code> or
   ** <code>$[<i>placeholder name</i>]</code>.
   ** <p>
   ** The pattern <code>$[<i>placeholder name</i>]</code> request to subtitute
   ** the expression with directly with the correspondedning value taken from
   ** <code>mapping</code>.
   **
   ** @param  subject            the string containing placeholders.
   ** @param  expression         the regular expression to match the
   **                            placeholders within the subject.
   ** @param  options            match flags, a bit mask that may include
   **                            {@link Pattern#CASE_INSENSITIVE},
   **                            {@link Pattern#MULTILINE},
   **                            {@link Pattern#DOTALL},
   **                            {@link Pattern#UNICODE_CASE},
   **                            {@link Pattern#CANON_EQ},
   **                            {@link Pattern#UNIX_LINES},
   **                            {@link Pattern#LITERAL} and
   **                            {@link Pattern#COMMENTS}.
   ** @param  mapping            the {@link Map} with the subtitution of the
   **                            expressions.
   **
   ** @return                    a string with all placeholders substituted by
   **                            the corresponding value of the provided
   **                            mapping.
   */
  public String parseTemplate(final String subject, final String expression, final int options, final Map<String, String> mapping) {
    final String method = "parseTemplate";
    trace(method, Loggable.METHOD_ENTRY);

    // required by the dependency of Matcher on leveraging a StringBuffer as a
    // target we cannot switch to a StringBuilder instance to improve
    // performance
    StringBuffer result = new StringBuffer(subject.length());
    try {
      // If you will be using a particular regular expression often, should
      // create a Pattern object to store the regular expression.
      // You can then reuse the regex as often as you want by reusing the
      // Pattern object.
      Pattern pattern = Pattern.compile(expression, options);

      // To use the regular expression on a string, create a Matcher object by
      // calling pattern.matcher() passing the subject string to it. The Matcher
      // will do the actual searching, replacing or splitting.
      Matcher match = pattern.matcher(subject);
      while (match.find()) {
        if (match.groupCount() > 0) {
          // Capturing parentheses are numbered 1..groupCount() group number
          // zero is the entire regex match 1 is the value that we would like to
          // substitute
          final String name = match.group(1);
          if (!StringUtility.empty(name)) {
            // check if placeholder is well known and and a key entry with a
            // legal value is existing
            if (mapping.containsKey(name)) {
              final String value = mapping.get(name);
              if (!StringUtility.empty(value))
                match.appendReplacement(result, value);
            }
          }
        }
      }
      match.appendTail(result);
    }
    catch (PatternSyntaxException e) {
      // parameter expression does not contain a valid regular expression
      error(method, DatabaseBundle.string(DatabaseError.EXPRESSION_INVALID, expression, e.getDescription()));
    }
    catch (IllegalArgumentException ex) {
      // this exception indicates a bug in parameter options
      error(method, DatabaseBundle.string(DatabaseError.EXPRESSION_BITVALUES));
    }
    trace(method, Loggable.METHOD_EXIT);
    return result.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installDialect
  /**
   ** We lazily install the appropriate dialect.
   ** <p>
   ** This function does the loading.
   **
   ** @throws SystemException    if the requested <code>DatabaseDialect</code>
   **                            class is not found on the classpath or could
   **                            not be either created or accessed for the
   **                            specified <code>driverClass</code>.
   */
  private synchronized void installDialect()
    throws SystemException {

    if (this.dialect != null)
      return;

    this.dialect = DatabaseDialect.build(endpoint().driver());
  }
}