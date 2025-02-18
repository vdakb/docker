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

    File        :   DatabaseException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import java.util.stream.Collectors;

import java.io.InputStream;
import java.io.IOException;

import java.net.SocketException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;

import java.security.cert.CertPathBuilderException;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;

import javax.net.ssl.SSLHandshakeException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.resource.DatabaseBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseException
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>Database</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseException extends SystemException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4550659113732465072")
  private static final long                               serialVersionUID = -3447168417643340773L;

  /** the error translation of a database to our implementation*/
  private static final Map<Package, Map<Integer, String>> errorMapping = new HashMap<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DatabaseException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  DatabaseException(final String code) {
    // ensure inheritance
    this(DatabaseBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DatabaseException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  DatabaseException(final ListResourceBundle bundle, final String code) {
    // ensure inheritance
    super(bundle, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DatabaseException</code> from a resource bundle code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for the placholder contained
   **                            in the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  DatabaseException(final String code, final String parameter) {
    // ensure inheritance
    this(DatabaseBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DatabaseException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  DatabaseException(final ListResourceBundle bundle, final String code, final String parameter) {
    // ensure inheritance
    super(bundle, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DatabaseException</code> from a code and a array with
   ** values for the placeholder contained in the resource string retrieved for
   ** the specified resource code.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}s.
   */
  DatabaseException(final String code, final Object... parameter) {
    // ensure inheritance
    this(DatabaseBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DatabaseException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}s.
   */
  DatabaseException(final ListResourceBundle bundle, final String code, final Object... parameter) {
    // ensure inheritance
    super(bundle, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Convert a JDBS @link SQLRecoverableException} to a
   ** <code>SystemException</code>.
   **
   ** @param  root               the {@link SQLRecoverableException} as the root
   **                            cause.
   **                            <br>
   **                            Allowed object is
   **                            {@link SQLRecoverableException}.
   ** @param  endpoint           the {@link DatabaseEndpoint} as the origin of
   **                            the exception.
   **                            <br>
   **                            Allowed object is {@link DatabaseEndpoint}.
   **
   ** @return                    the converted <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException from(final SQLRecoverableException root, final DatabaseEndpoint endpoint) {
    // investigate the exception further to be able to send back the real
    // reason why the request failed
    Throwable cause = root;
    while (cause.getCause() != null) {
      cause = cause.getCause();
    }
    if (UnknownHostException.class.isInstance(cause)) {
      return SystemException.unknownHost(endpoint.primaryHost());
    }
    else if (ConnectException.class.isInstance(cause)) {
      return SystemException.unavailable(cause);
    }
    else if (SocketException.class.isInstance(cause)) {
      return SystemException.createSocket(endpoint.primaryHost(), endpoint.primaryPort());
    }
    else if (SocketTimeoutException.class.isInstance(cause)) {
      return SystemException.timedOut(endpoint.primaryHost());
    }
    else if (SSLHandshakeException.class.isInstance(cause)) {
      return SystemException.createSocketSecure(endpoint.primaryHost(), endpoint.primaryPort());
    }
    else if (CertPathBuilderException.class.isInstance(cause)) {
      return SystemException.certificatePath(endpoint.primaryHost());
    }
    // Oracle wraps its own networking exception which is a subclass of
    // IOException
    else if (IOException.class.isInstance(cause)) {
      return SystemException.unknownHost(endpoint.primaryHost());
    }
    else
      return SystemException.unhandled(root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generic
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** <code>code</code> error keyword.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException generic(final String code) {
    return new DatabaseException(code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeout
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#STATEMENT_TIMEOUT} error keyword.
   **
   ** @param  statement          the SQL statement raising the excpetion.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException timeout(final String statement) {
    return new DatabaseException(DatabaseError.STATEMENT_TIMEOUT, statement);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   syntax
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#SYNTAX_INVALID} error keyword.
   **
   ** @param  statement          the SQL statement raising the excpetion.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException syntax(final String statement) {
    return new DatabaseException(DatabaseError.SYNTAX_INVALID, statement);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationFailed
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#OPERATION_FAILED} error keyword.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException operationFailed() {
    return new DatabaseException(DatabaseError.OPERATION_FAILED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationUnsupported
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#OPERATION_NOT_SUPPORTED} error keyword.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException operationUnsupported() {
    return new DatabaseException(DatabaseError.OPERATION_NOT_SUPPORTED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationUnauthorized
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#INSUFFICIENT_PRIVILEGE} error keyword.
   **
   ** @param  principal          the principal name raising the excpetion.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the operation raising the excpetion.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException operationUnauthorized(final String principal, final String operation) {
    return new DatabaseException(DatabaseError.INSUFFICIENT_PRIVILEGE, principal, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchCondition
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#SEARCH_CONDITION_FAILED} error keyword.
   **
   ** @param  criteria           the search criteria rasing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException searchCondition(final String criteria) {
    return new DatabaseException(DatabaseError.SEARCH_CONDITION_FAILED, criteria);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ambiguous
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#OBJECT_AMBIGUOUS} error keyword.
   **
   ** @param  entity             the database entity the exception belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  target             the database target the exception originates
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code> wrapping
   **                            the response status.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException ambiguous(final String entity, final String target) {
    return new DatabaseException(DatabaseError.OBJECT_AMBIGUOUS, entity, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   conflict
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#OBJECT_ALREADY_EXISTS} error keyword.
   **
   ** @param  entity             the database entity the exception belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  target             the database target the exception originates
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code> wrapping
   **                            the response status.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException conflict(final String entity, final String target) {
    return new DatabaseException(DatabaseError.OBJECT_ALREADY_EXISTS, entity, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notFound
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#OBJECT_NOT_EXISTS} error keyword.
   ** <p>
   ** Specified resource (e.g., User) or endpoint does not exist.
   **
   ** @param  entity             the database entity the exception belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  target             the database target the exception originates
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code> wrapping
   **                            the response status.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException notFound(final String entity, final String target) {
    return new DatabaseException(DatabaseError.OBJECT_NOT_EXISTS, entity, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notCreated
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#OBJECT_NOT_CREATED} error keyword.
   ** <p>
   ** The resource has not changed.
   **
   ** @param  entity             the database entity the exception belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  target             the database target the exception originates
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException notCreated(final String entity, final String target) {
    return new DatabaseException(DatabaseError.OBJECT_NOT_CREATED, entity, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notModified
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#OBJECT_NOT_MODIFIED} error keyword.
   ** <p>
   ** The resource has not changed.
   **
   ** @param  entity             the database entity the exception belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  target             the database target the exception originates
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException notModified(final String entity, final String target) {
    return new DatabaseException(DatabaseError.OBJECT_NOT_MODIFIED, entity, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notDeleted
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#OBJECT_NOT_DELETED} error keyword.
   ** <p>
   ** The resource has not changed.
   **
   ** @param  entity             the database entity the exception belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  target             the database target the exception originates
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException notDeleted(final String entity, final String target) {
    return new DatabaseException(DatabaseError.OBJECT_NOT_DELETED, entity, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unexpectedEOS
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#PATH_UNEXPECTED_EOS} error keyword.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException unexpectedEOS() {
    return new DatabaseException(DatabaseError.PATH_UNEXPECTED_EOS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unexpectedCharacter
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#PATH_UNEXPECTED_CHARACTER} error keyword.
   **
   ** @param  c                  the character thats unexpected.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   ** @param  pos                the position of the unexpected character in
   **                            the source.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  mark               the starting position of the parser detected
   **                            the unexpected character in the source.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException unexpectedCharacter(final char c, final int pos, final int mark) {
    return new DatabaseException(DatabaseError.PATH_UNEXPECTED_CHARACTER, c, pos, mark);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributePathExpected
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#PATH_EXPECT_ATTRIBUTE_NAME} error keyword.
   **
   ** @param  mark               the starting position of the parser detected
   **                            the unexpected character in the source.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException attributePathExpected(final int mark) {
    return new DatabaseException(DatabaseError.PATH_EXPECT_ATTRIBUTE_PATH, mark);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeNameExpected
  /**
   ** Factory method to create a new <code>DatabaseException</code> with the
   ** {@link DatabaseError#PATH_EXPECT_ATTRIBUTE_NAME} error keyword.
   **
   ** @param  mark               the starting position of the parser detected
   **                            the unexpected character in the source.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>DatabaseException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseException</code>.
   */
  public static DatabaseException attributeNameExpected(final int mark) {
    return new DatabaseException(DatabaseError.PATH_EXPECT_ATTRIBUTE_NAME, mark);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalized
  /**
   ** Returns the implementation specific error code for a vendor specific code.
   ** <p>
   ** The vendor code is taken from an SQLException that is catched somewhere
   ** and wrapped in a {@link DatabaseException}. This prefix the vendor
   ** specific code constantly with <code>SQL-</code>.
   **
   ** @param  statement          the JDBC {@link Statement} where the
   **                            exception occured.
   **                            <br>
   **                            Allowed object is {@link Statement}.
   ** @param  causing            the exception thrown by a process step that
   **                            may contain a vendor specific error code.
   **                            <br>
   **                            Allowed object is {@link DatabaseException}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    a implementation specific error code if it's
   **                            translatable; otherwise <code>DBA-00001</code>
   **                            will be returned.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the property file regarding the error code
   **                            mapping exists but could not be loaded.
   */
  public static SystemException normalized(final Statement statement, final SQLException causing, final Object... parameter)
    throws SystemException {

    // wrap the exception occured in throw it to the invoker for further
    // analysis
    try {
      return normalized(statement.getConnection(), causing, parameter);
    }
    // unfortunately getting the conection for a statement raise an SQLException
    catch (SQLException e) {
      return SystemException.unhandled(causing);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalized
  /**
   ** Returns the implementation specific error code for a vendor specific code.
   ** <p>
   ** The vendor code is taken from an SQLException that is catched somewhere
   ** and wrapped in a {@link DatabaseException}. This prefix the vendor
   ** specific code constantly with <code>SQL-</code>.
   **
   ** @param  connection         the JDBC {@link Connection} where the
   **                            exception occured.
   **                            <br>
   **                            Allowed object is {@link Statement}.
   ** @param  causing            the exception thrown by a process step that
   **                            may contain a vendor specific error code.
   **                            <br>
   **                            Allowed object is {@link DatabaseException}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    a implementation specific error code if it's
   **                            translatable; otherwise <code>DBA-00001</code>
   **                            will be returned.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the property file regarding the error code
   **                            mapping exists but could not be loaded.
   */
  public static SystemException normalized(final Connection connection, final SQLException causing, final Object... parameter)
    throws SystemException {

    // wrap the exception occured to throw it back to the invoker for further
    // analysis
    return DatabaseException.normalized(connection.getClass().getPackage(), causing, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalized
  /**
   ** Returns the implementation specific error code for a vendor specific code.
   ** <p>
   ** The vendor code is taken from an SQLException that is catched somewhere
   ** and wrapped in a {@link DatabaseException}. This prefix the vendor
   ** specific code constantly with <code>SQL-</code>.
   **
   ** @param  clazz              the class {@link Package} of the JDBC driver
   **                            to load the vendor specific error code.
   **                            <br>
   **                            Allowed object is {@link Package}.
   ** @param  causing            the exception thrown by a process step that
   **                            may contain a vendor specific error code.
   **                            <br>
   **                            Allowed object is {@link DatabaseException}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    a implementation specific error code if it's
   **                            translatable; otherwise <code>DBA-00001</code>
   **                            will be returned.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the property file regarding the error code
   **                            mapping exists but could not be loaded.
   */
  public static SystemException normalized(final Package clazz, final SQLException causing, final Object... parameter)
    throws SystemException {

    final Map<Integer, String> registry = of(clazz);
    if (registry != null) {
      final String code = registry.get(causing.getErrorCode());
      if (code != null && code.startsWith(DatabaseError.PREFIX))
        return new DatabaseException(code, parameter);
      else
        return SystemException.unhandled(causing);
    }
    else
      return SystemException.unhandled(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Returns the error mapping to translate vendor specific error codes thrown
   ** by a JDBC Driver to our implementation.
   **
   ** @param  clazz              the class {@link Package} of the JDBC driver
   **                            to load the vendor specific error code.
   **                            <br>
   **                            Allowed object is {@link Package}.
   **
   ** @return                    the error mapping to translate vendor specific
   **                            error codes.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link Integer} for the key
   **                            and {@link String} for the value.
   **
   ** @throws SystemException    if the property file regarding the error code
   **                            mapping exists but could not be loaded.
   */
  public static Map<Integer, String> of(final Package clazz)
    throws SystemException {

    // lazy load error code table
    if (errorMapping.get(clazz) == null)
      installError(clazz);

    return errorMapping.get(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installError
  /**
   ** Creates the error mapping to translate vendor specific error codes thrown
   ** by a JDBC Driver to our implementation.
   **
   ** @param  clazz              the class {@link Package} of the JDBC driver
   **                            to load the vendor specific error code.
   **                            <br>
   **                            Allowed object is {@link Package}.
   **
   ** @throws SystemException    if the property file regarding the error code
   **                            mapping exists but could not be loaded.
   */
  public static void installError(final Package clazz)
    throws SystemException {

    final InputStream stream = DatabaseError.class.getResourceAsStream("/META-INF/jdbc/" + clazz.getName() +".txt");
    if (stream == null)
      return;

    final Properties mapping = new Properties();
    try {
      mapping.load(stream);
    }
    catch (IOException e) {
      throw SystemException.unhandled(e);
    }
    errorMapping.put(clazz, mapping.entrySet().stream().collect(Collectors.toMap(e -> Integer.valueOf((String)e.getKey()), e -> String.valueOf(e.getValue()))));
  }
}