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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   Connection.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Connection.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.control;

import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.Properties;
import java.util.LinkedHashMap;

import java.text.ParseException;

import java.io.File;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunctionTemplate;

import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.JCoParameterList;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractConnector;
import oracle.iam.identity.foundation.AbstractMetadataTask;

import oracle.iam.identity.sap.service.resource.ConnectionBundle;

////////////////////////////////////////////////////////////////////////////////
// class Connection
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Connection</code> implements the base functionality of an Oracle
 ** Identity Manager {@link AbstractConnector} for a SAP system.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class Connection extends AbstractConnector {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the wrapper of target specific parameters where this connector is attached
   ** to
   */
  protected final Resource resource;

  /**
   ** the wrapper of target specific features where this connector is attached
   ** to
   */
  protected final Feature  feature;

  private JCoRepository    repository;
  private JCoDestination   destination;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Connection</code> which is associated with the
   ** specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractConnector}.
   ** @param  serverInstance     the system identifier of the
   **                            <code>IT Resource</code> instance where this
   **                            connector is associated with.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public Connection(final AbstractMetadataTask task, final Long serverInstance)
    throws TaskException {

    this(task, new Resource(task, serverInstance));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Connection</code> which is associated with the
   ** specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractConnector}.
   ** @param  resource           the {@link Resource} IT Resource definition
   **                            where this connector is associated with.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public Connection(final AbstractMetadataTask task, final Resource resource)
    throws TaskException {

    // ensure inheritance
    this(task, resource, unmarshal(task, resource.feature()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Connection</code> which is associated with the
   ** specified task.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractConnector}.
   ** @param  resource           the {@link Resource} IT Resource definition
   **                            where this connector is associated with.
   ** @param  feature            the Metadata Descriptor providing the target
   **                            system specific features like objectClasses,
   **                            attribute id's etc.
   */
  public Connection(final Loggable loggable, final Resource resource, final Feature feature) {
    // ensure inheritance
    super(loggable);

    // create the property mapping for the connection control
    this.resource = resource;
    this.feature  = feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the {@link Resource} this connection belongs to.
   **
   ** @return                    the {@link Resource} this connection belongs
   **                            to.
   */
  public final Resource resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   feature
  /**
   ** Returns the {@link Feature} metadata descriptor this connection belongs
   ** to.
   **
   ** @return                    the {@link Feature} metadata descriptor this
   **                            connection belongs to.
   */
  public final Feature feature() {
    return this.feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destination
  /**
   ** Returns the destination.
   */
  public final JCoDestination destination() {
    return this.destination;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repository
  /**
   ** Returns the repository.
   */
  public final JCoRepository repository() {
    return this.repository;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destinationName
  /**
   ** Returns name of destination.
   ** Destination Name is a user defined non-unique name.
   */
  public final String destinationName() {
    return this.destination.getDestinationName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cuaManaged
  /**
   ** Returns the <code>true</code> if a Central User Administration is in front
   ** of the SAP/R3 System to manage where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Resource#CENTRAL_USER_ADMINISTRATION}.
   ** <br>
   ** If {@link Resource#CENTRAL_USER_ADMINISTRATION} is not mapped in the
   ** underlying mapping this method returns <code>false</code>.
   **
   ** @return                    whether or not a Central User Administration is
   **                            in front of the SAP/R3 System to manage where
   **                            this IT Resource belongs to.
   */
  public final boolean cuaManaged() {
    return this.resource.cuaManaged();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemLanguage
  /**
   ** Returns the logon language of the SAP/R3 System account specified by the
   ** <code>principalName</code> parameter to establish a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Resource#SYSTEM_LANGUAGE}.
   **
   ** @return                    the logon language of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   */
  public final String systemLanguage() {
    return this.resource.systemLanguage();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTimeZone
  /**
   ** Returns the timezone of the SAP/R3 System where this IT Resource belongs
   ** to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Resource#SYSTEM_TIMEZONE}.
   **
   ** @return                    the timezone of the SAP/R3 System where this IT
   **                            Resource belongs to.
   */
  public final String systemTimeZone() {
    return this.resource.systemTimeZone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   masterSystemName
  /**
   ** Returns the RFC destination value that is used for identification of the
   ** SAP/R3 System. This value must be same as that of the Logical System name.
   ** <p>
   ** Sample value: EH6CLNT001
   ** <br>
   ** Here the sample value is based on the following format used in SAP system:
   ** <br>
   ** <code>&lt;SYSTEM_ID&gt;CLNT&lt;CLIENT_NUM&gt;</code>
   ** <br>
   ** In this sample value, EH6 is the System ID of the target system and 001 is
   ** the client number. where this IT Resource belongs to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Resource#MASTER_SYSTEM_NAME}.
   **
   ** @return                    the RFC destination value that is used for
   **                            identification of the SAP/R3 System.
   **                            This value must be same as that of the Logical
   **                            System name.
   */
  public final String masterSystemName() {
    return this.resource.masterSystemName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryConnectionCount
  /**
   ** Returns the number of consecutive attempts to be made at establishing a
   ** connection with the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Resource#CONNECTION_RETRY_COUNT}.
   ** <br>
   ** If {@link Resource#CONNECTION_RETRY_COUNT} is not mapped in the underlying
   ** mapping this method returns
   ** {@link Resource#CONNECTION_RETRY_COUNT_DEFAULT}.
   **
   ** @return                    the number of consecutive attempts to be made
   **                            at establishing a connection with the SAP/R3
   **                            System.
   */
  public final int retryConnectionCount() {
    return this.resource.retryConnectionCount();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryConnectionInterval
  /**
   ** Returns the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Resource#CONNECTION_RETRY_INTERVAL}.
   ** If {@link Resource#CONNECTION_RETRY_INTERVAL} is not mapped in the
   ** underlying mapping this method returns
   ** {@link Resource#CONNECTION_RETRY_INTERVAL_DEFAULT}.
   **
   ** @return                    the interval (in milliseconds) between
   **                            consecutive attempts at establishing a
   **                            connection with the SAP/R3 System.
   */
  public final int retryConnectionInterval() {
    return this.resource.retryConnectionInterval();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleFutureDated
  /**
   ** Whether future dated roles have to be reconciled form the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Feature#ROLE_FUTURE_DATED}.
   ** <p>
   ** If {@link Feature#ROLE_FUTURE_DATED} is not mapped in the underlying
   ** mapping this method returns <code>true</code>.
   **
   ** @return                    <code>true</code> if future dated roles have to
   **                            be reconciled form the SAP/R3 System; otherwise
   **                            <code>false</code>.
   */
  public final boolean roleFutureDated() {
    return this.feature.roleFutureDated();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rolePastDated
  /**
   ** Whether future dated roles have to be reconciled form the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Feature#ROLE_PAST_DATED}.
   ** <p>
   ** If {@link Feature#ROLE_PAST_DATED} is not mapped in the underlying
   ** mapping this method returns <code>true</code>.
   **
   ** @return                    <code>true</code> if future dated roles have to
   **                            be reconciled form the SAP/R3 System; otherwise
   **                            <code>false</code>.
   */
  public final boolean rolePastDated() {
    return this.feature.rolePastDated();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleObjectClass
  /**
   ** Returns the schema definition of the objectClass role of a SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Feature#ROLE_OBJECT_CLASS}.
   ** <p>
   ** If {@link Feature#ROLE_OBJECT_CLASS} is not mapped in the underlying
   ** mapping this method returns {@link Feature#ROLE_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the schema definition of the objectClass role
   **                            of a SAP/R3 System.
   */
  public final String roleObjectClass() {
    return this.feature.roleObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupObjectClass
  /**
   ** Returns the schema definition of the objectClass group of a SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Feature#ROLE_OBJECT_CLASS}.
   ** <p>
   ** If {@link Feature#GROUP_OBJECT_CLASS} is not mapped in the underlying
   ** mapping this method returns {@link Feature#GROUP_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the schema definition of the objectClass group
   **                            of a SAP/R3 System.
   */
  public final String groupObjectClass() {
    return this.feature.groupObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profileObjectClass
  /**
   ** Returns the schema definition of the objectClass profile of a SAP/R3
   ** System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Feature#PROFILE_OBJECT_CLASS}.
   ** <p>
   ** If {@link Feature#PROFILE_OBJECT_CLASS} is not mapped in the underlying
   ** mapping this method returns {@link Feature#PROFILE_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the schema definition of the objectClass
   **                            profile of a SAP/R3 System.
   */
  public final String profileObjectClass() {
    return this.feature.profileObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameterObjectClass
  /**
   ** Returns the schema definition of the objectClass parameter of a SAP/R3
   ** System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link Feature#PARAMETER_OBJECT_CLASS}.
   ** <p>
   ** If {@link Feature#PARAMETER_OBJECT_CLASS} is not mapped in the underlying
   ** mapping this method returns
   ** {@link Feature#PARAMETER_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the schema definition of the objectClass
   **                            parameter of a SAP/R3 System.
   */
  public final String parameterObjectClass() {
    return this.feature.parameterObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Feature} from a path.
   **
   ** @param  task               the {@link AbstractMetadataTask} where the
   **                            object to create will belong to.
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **
   ** @return                    the {@link Feature} created from the specified
   **                            propertyFile.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  public static Feature unmarshal(final AbstractMetadataTask task, final String path)
    throws ConnectionException {

    Feature feature = new Feature(task);
    try {
      final MDSSession session  = task.createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      FeatureFactory.configure(feature, document);
    }
    catch (ReferenceException e) {
      throw new ConnectionException(e);
    }
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Feature} from a path.
   **
   ** @param  loggable           the {@link Loggable} where the object to create
   **                            will belong to.
   ** @param  path               the absolute path for the descriptor in the
   **                            file system that has to be parsed.
   **
   ** @return                    the {@link Feature} created from the specified
   **                            propertyFile.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  public static Feature unmarshal(final Loggable loggable, final File path)
    throws ConnectionException {

    final Feature feature = new Feature(loggable);
    FeatureFactory.configure(feature, path);
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                   the string representation of this instance.
   */
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();

    StringUtility.formatValuePair(buffer, Resource.APPLICATION_SERVER_HOST, this.resource.applicationHost());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.APPLICATION_SERVER_ROUTE, this.resource.applicationRoute());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.APPLICATION_SERVER_GROUP, this.resource.applicationGroup());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.MESSAGE_SERVER_HOST,      this.resource.messageHost());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.MESSAGE_SERVER_PORT,      this.resource.messagePort());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.GATEWAY_SERVER_HOST,      this.resource.gatewayHost());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.GATEWAY_SERVICE_NAME,     this.resource.gatewayName());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.SYSTEM_NAME,              this.resource.systemName());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.SYSTEM_NUMBER,            this.resource.systemNumber());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.SYSTEM_LANGUAGE,          this.resource.systemLanguage());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.SYSTEM_TIMEZONE,          this.resource.systemTimeZone());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.MASTER_SYSTEM_NAME,       this.resource.masterSystemName());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.PRINCIPAL_NAME,           this.resource.principalName());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.PRINCIPAL_PASSWORD,       "********");
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.PRINCIPAL_CODEPAGE,       this.resource.principalCodepage());
    buffer.append(SystemConstant.LINEFEED);
    StringUtility.formatValuePair(buffer, Resource.SECURE_SOCKET,            this.resource.secureSocket());
    buffer.append(SystemConstant.LINEFEED);
    if (this.resource.secureSocket()) {
      StringUtility.formatValuePair(buffer, Feature.SECURE_LEVEL,              this.feature.secureLevel());
      buffer.append(SystemConstant.LINEFEED);
      StringUtility.formatValuePair(buffer, Feature.SECURE_NAME_REMOTE,        this.feature.secureNameRemote());
      buffer.append(SystemConstant.LINEFEED);
      StringUtility.formatValuePair(buffer, Feature.SECURE_NAME_LOCAL,         this.feature.secureNameLocal());
      buffer.append(SystemConstant.LINEFEED);
      StringUtility.formatValuePair(buffer, Feature.SECURE_LIBRARY_PATH,       this.feature.secureLibraryPath());
      buffer.append(SystemConstant.LINEFEED);
    }
    buffer.append(this.feature.toString());
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Close the connection wirh the SAP system.
   **
   ** @throws ConnectionException if the operation fails.
   */
  public void connect()
    throws ConnectionException {

    connect(environment());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Close the connection wirh the SAP system.
   **
   ** @param  environment        the {@link Properties} for the destination or
   **                            <code>null</code> if the destination has not
   **                            found.
   ** @throws ConnectionException if the operation fails.
   */
  public void connect(final Properties environment)
    throws ConnectionException {

    final String method = "connect";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      debug(method, ConnectionBundle.format(ConnectionMessage.CONNECTION_OPEN, this.resource.applicationHost()));
      // passing the host as the destination and destination's connection
      // properties to the SAP target
      this.destination = DestinationProvider.instance().create(this.resource.applicationHost(), environment);
      this.repository  = this.destination.getRepository();
      if (JCoContext.isStateful(this.destination))
        debug(method, ConnectionBundle.format(ConnectionMessage.CONNECTION_ISOPEN, this.destination.getDestinationName()));
      else
        debug(method, ConnectionBundle.format(ConnectionMessage.CONNECTION_OPENED, this.destination.getDestinationName()));
    }
    catch (JCoException e) {
      this.destination = null;
      this.repository  = null;
      throw new ConnectionException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Close the connection wirh the SAP system.
   **
   ** @throws ConnectionException if the operation fails.
   */
  public void disconnect()
    throws ConnectionException {

    // prevent bogus state of the connection
    if (this.destination == null)
      return;

    final String method = "disconnect";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      trace(method, ConnectionBundle.format(ConnectionMessage.CONNECTION_CLOSE, this.destination.getDestinationName()));
      DestinationProvider.endTransaction(this.destination);
      trace(method, ConnectionBundle.format(ConnectionMessage.CONNECTION_CLOSED, this.destination.getDestinationName()));
      this.repository  = null;
      this.destination = null;
    }
    catch (Exception e) {
      fatal(method, e);
      throw new ConnectionException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ping
  /**
   ** Executes a connection test for this connection.
   **
   **
   ** @throws ConnectionException  if something went wrong during the ping
   **                              initialized properly.
   */
  public void ping()
    throws ConnectionException {

    final String method = "ping";
    trace(method, SystemMessage.METHOD_ENTRY);
    // Connect to repository and return the function according to the BAPI name
    // passed
    try {
      this.destination.ping();
    }
    catch (JCoException e) {
      throw new ConnectionException(ConnectionError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startTransaction
  /**
   ** Begins the stateful call sequence for calls to the specified destination.
   ** <p>
   ** The connections used between begin(JCoDestination) and end(JCoDestination)
   ** won't be reset or closed.
   */
  public void startTransaction() {
    DestinationProvider.startTransaction(this.destination);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endTransaction
  /**
   ** Begins the stateful call sequence for calls to the specified destination.
   ** <p>
   ** The connections used between begin(JCoDestination) and end(JCoDestination)
   ** won't be reset or closed.
   **
   ** @throws ConnectionException if the operation fails.
   */
  public void endTransaction()
    throws ConnectionException {

    DestinationProvider.endTransaction(this.destination);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   function
  /**
   ** Returns the JCoFunction after executing a BAPI
   **
   ** @param  functionName       the name of the BAPI to be executed.
   **                            For example: BAPI_USER_GETDETAILS.
   **
   ** @return                    the {@link JCoFunction} for the associated BAPI
   **                            Name and {@link JCoDestination} having the
   **                            connection properties
   */
  public JCoFunction function(final String functionName)
    throws ConnectionException {

    final String method = "function";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // Connect to repository and return the function according to the BAPI name
      // passed
      final JCoRepository       repository = this.destination.getRepository();
      final JCoFunctionTemplate template   = repository.getFunctionTemplate(functionName);
      return template.getFunction();
    }
    catch (JCoException e) {
      throw new ConnectionException(ConnectionError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Executes the {@link JCoFunction} on the wrapped destination synchronously.
   **
   ** @param  function           the [@link JCoFunction} to execute on the
   **                            wrapped destination synchronously.
   **
   ** @throws ConnectionException if an exception occurred during the call
   **                             execution.
   */
  public void execute(final JCoFunction function)
    throws ConnectionException {

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      function.execute(this.destination);
    }
    catch (JCoException e) {
      throw new ConnectionException(ConnectionError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Executes the {@link JCoFunction} on the wrapped destination synchronously.
   **
   ** @param  function           the [@link JCoFunction} to execute on the
   **                            wrapped destination synchronously.
   ** @param  transaction        the transaction ID to use for the tRFC call.
   **
   ** @throws ConnectionException if an exception occurred during the call
   **                             execution.
   */
  public void execute(final JCoFunction function, final String transaction)
    throws ConnectionException {

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      function.execute(this.destination, transaction);
    }
    catch (JCoException e) {
      throw new ConnectionException(ConnectionError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Executes the {@link JCoFunction} on the wrapped destination in
   ** transactional mode using the tRfc protocol.
   **
   ** @param  function           the [@link JCoFunction} to execute on the
   **                            wrapped destination synchronously.
   ** @param  transaction        the transaction ID to use for the tRFC call.
   ** @param  queue              the inbound queue to use for the qRFC call.
   **
   ** @throws ConnectionException if an exception occurred during the call
   **                             execution.
   */
  public void execute(final JCoFunction function, final String transaction, final String queue)
    throws ConnectionException {

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      function.execute(this.destination, transaction, queue);
    }
    catch (JCoException e) {
      throw new ConnectionException(ConnectionError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAccount
  /**
   ** Lookups an <code>Account</code> in the target system.
   **
   ** @param  account            the account id.
   **
   ** @return                    <code>true</code> if the account exists;
   **                            otherwise <code>false</code>.
   */
  public boolean lookupAccount(final String account)
    throws ConnectionException {

    final String method = "lookupAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    int number = 0;
    try {
      final Function function = new Function(this, "BAPI_USER_EXISTENCE_CHECK", account);
      function.execute();
      final JCoStructure returning = function.returning();
      number = returning.getInt("NUMBER");
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return (number != 124) && (number == 88);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enableAccount
  /**
   ** Enables an <code>Account</code> in the target system.
   **
   ** @param  account            the account id to enable.
   **
   */
  public void enableAccount(final String account)
    throws ConnectionException {

    final String method = "enableAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      final Calendar value    = DateUtility.parseDate("9999-12-31", "yyyy-MM-dd");
      final Function function = new Function(this, "BAPI_USER_CHANGE", account);
      function.importSegmentValue("LOGONDATA", "GLTGB", DateUtility.getDate(value), true);
      function.execute(this.resource.connectionRetryCount(), this.resource.connectionRetryInterval());
      function.verify();
    }
    catch (ParseException e) {
      throw new ConnectionException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disableAccount
  /**
   ** Disables an <code>Account</code> in the target system.
   **
   ** @param  account            the account id to disable.
   **
   */
  public void disableAccount(final String account)
    throws ConnectionException {

    final String method = "disableAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      Date now = new Date();
      long mymillidate = now.getTime();
      mymillidate -= 86400000L;
      now = new Date(mymillidate);
      final String   date     = DateUtility.display(now, "yyyy-MM-dd");
      final Calendar value    = DateUtility.parseDate(date, "yyyy-MM-dd");
      final Function function = new Function(this, "BAPI_USER_CHANGE", account);
      function.importSegmentValue("LOGONDATA", "GLTGB", DateUtility.getDate(value), true);
      function.execute(this.resource.connectionRetryCount(), this.resource.connectionRetryInterval());
      function.verify();
    }
    catch (ParseException e) {
      throw new ConnectionException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** IT Resource that afterwards can be passed to a {@link DestinationProvider}
   ** to establish a connection to the target system.
   **
   ** @return                    the environment context this connector use to
   **                            communicate with the SAP server.
   */
  protected Properties environment() {

    final Properties environment = new Properties();
    // set the mandatory properties needed to establish a connection
    environment.put(DestinationProvider.JCO_ASHOST, this.resource.applicationHost());
    environment.put(DestinationProvider.JCO_SYSNR,  this.resource.systemNumber());
    environment.put(DestinationProvider.JCO_LANG,   this.resource.systemLanguage());
    environment.put(DestinationProvider.JCO_CLIENT, this.resource.clientLogon());
    environment.put(DestinationProvider.JCO_USER,   this.resource.principalName());
    environment.put(DestinationProvider.JCO_PASSWD, this.resource.principalPassword());

    if (!StringUtility.isEmpty(this.resource.applicationRoute()))
      environment.setProperty(DestinationProvider.JCO_SAPROUTER, this.resource.applicationRoute());

    if (!StringUtility.isEmpty(this.resource.systemName()))
      environment.setProperty(DestinationProvider.JCO_R3NAME, this.resource.systemName());

    if (!StringUtility.isEmpty(this.resource.messageHost()))
      environment.setProperty(DestinationProvider.JCO_MSHOST, this.resource.messageHost());

    if (!StringUtility.isEmpty(this.resource.applicationGroup()))
      environment.setProperty(DestinationProvider.JCO_GROUP, this.resource.applicationGroup());

    if (this.resource.systemLanguage().equalsIgnoreCase("ja") && this.resource.secureSocket())
      environment.setProperty(DestinationProvider.JCO_CODEPAGE, "8000");

    if ((this.resource.secureSocket())) {
      environment.setProperty(DestinationProvider.JCO_SNC_MODE,        "1");
      environment.setProperty(DestinationProvider.JCO_SNC_PARTNERNAME, this.feature.secureNameRemote());
      environment.setProperty(DestinationProvider.JCO_SNC_MYNAME,      this.feature.secureNameLocal());
      environment.setProperty(DestinationProvider.JCO_SNC_QOP,         String.valueOf(this.feature.secureLevel()));
      environment.setProperty(DestinationProvider.JCO_SNC_LIBRARY,     this.feature.secureLibraryPath());
    }
    else {
      environment.setProperty(DestinationProvider.JCO_SNC_MODE,        "0");
    }
    return environment;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   readParameter
  /**
   ** This method determines the valid help values (F4) for a parameter or field
   ** of a structured BAPI parameter.
   ** <p>
   ** The help values are based on check tables, fixed domain values, or value
   ** helps defined in a data element. If no search help of this type is
   ** defined, no help values can be specified.
   ** <br>
   ** This method can only be used for BAPI parameters. A check is made to see
   ** whether the field exists in the named parameter, in the method, and in the
   ** business object. You must send the name or object type name of the
   ** business object, the method, and the parameter with the name defined in
   ** the Business Object Repository (BOR), to the Helpvalues.GetList method. If
   ** you have a field from a structured parameter, you also need to specify its
   ** name. You cannot obtain the total value help for all fields of a structure
   ** by not explicitly specifying a field when calling the method.
   ** <br>
   ** As an alternative, you can use details in the parameter ExplicitShlp to
   ** send specific search helps. The parameters ObjType, ObjName, Method,
   ** Parameter and Field must still be filled, so that the BAPI authorization
   ** checks can be made.
   ** <br>
   ** <b>Note</b> that this method only takes elementary search helps into
   ** account. For collective search helps, read the documentation on the method
   ** Helpvalues.GetSearchHelp.
   ** <br>
   ** The help values - together with other useful information - are returned in
   ** the parameter Helpvalues. The data is arranged in the left column by
   ** default.
   **
   ** @param  method             the name of a BAPI for the business object type
   **                            <code>USER</code>, for example, GetList.
   **
   ** @return                    the {@link Map}...
   */
  public Map<String, Object> readParameter(final String method, final String argument, final String attribute, final String[] returning)
    throws ConnectionException {

    final JCoFunction      function  = function("BAPI_HELPVALUES_GET");
    final JCoParameterList parameter = function.getImportParameterList();
    parameter.setValue("OBJTYPE",   "USER");
    parameter.setValue("METHOD",    method);
    parameter.setValue("PARAMETER", argument);
    parameter.setValue("FIELD",     attribute);
    return populateParameter(function, attribute.equals("DCPFM"), returning);
  }

  public Map<String, Object> readParameter(final String method, final String argument, final String attribute, final String selection, final String sign, final String option, final String low, final String[] returning)
    throws ConnectionException {

    final JCoFunction      function  = function("BAPI_HELPVALUES_GET");
    final JCoParameterList parameter = function.getImportParameterList();
    parameter.setValue("OBJTYPE",   "USER");
    parameter.setValue("METHOD",    method);
    parameter.setValue("PARAMETER", argument);
    parameter.setValue("FIELD",     attribute);

    final JCoTable extened = function.getTableParameterList().getTable("SELECTION_FOR_HELPVALUES");
    extened.appendRow();
    extened.setValue("SELECT_FLD", selection);
    extened.setValue("SIGN",       sign);
    extened.setValue("OPTION",     option);
    extened.setValue("LOW",        low);
    return populateParameter(function, false, returning);
  }

  public Map<String, Object> readPermission(final String method, final String argument, final String attribute, final String selectionName, final String selectionType, final String[] returning)
    throws ConnectionException {

    final JCoFunction      function  = function("BAPI_HELPVALUES_GET");
    final JCoParameterList parameter = function.getImportParameterList();
    parameter.setValue("OBJTYPE",   "USER");
    parameter.setValue("METHOD",    method);
    parameter.setValue("PARAMETER", argument);
    parameter.setValue("FIELD",     attribute);

    JCoStructure structure = parameter.getStructure("EXPLICIT_SHLP");
    structure.setValue("SHLPNAME", selectionName);
    structure.setValue("SHLPTYPE", selectionType);
    return populateParameter(function, false, returning);
  }

  public Map<String, Object> readParameter(final String method, final String argument, final String attribute, final String selectionName, final String selectionType, final String[] returning)
    throws ConnectionException {

    final JCoFunction      function  = function("BAPI_HELPVALUES_GET");
    final JCoParameterList parameter = function.getImportParameterList();
    parameter.setValue("OBJTYPE",   "USER");
    parameter.setValue("METHOD",    method);
    parameter.setValue("PARAMETER", argument);
    parameter.setValue("FIELD",     attribute);

    JCoStructure structure = parameter.getStructure("EXPLICIT_SHLP");
    structure.setValue("SHLPNAME", selectionName);
    structure.setValue("SHLPTYPE", selectionType);

    return populateParameter(function, false, returning);
  }

  public Map<String, Object> readParameter(final String method, final String argument, final String attribute, final String selectionName, final String selectionType, final String selection, final String sign, final String option, final String low, final String[] returning)
    throws ConnectionException {

    final JCoFunction      function  = function("BAPI_HELPVALUES_GET");
    final JCoParameterList parameter = function.getImportParameterList();
    parameter.setValue("OBJTYPE",   "USER");
    parameter.setValue("METHOD",    method);
    parameter.setValue("PARAMETER", argument);
    parameter.setValue("FIELD",     attribute);

    JCoStructure structure = parameter.getStructure("EXPLICIT_SHLP");
    structure.setValue("SHLPNAME", selectionName);
    structure.setValue("SHLPTYPE", selectionType);

    final JCoTable extened = function.getTableParameterList().getTable("SELECTION_FOR_HELPVALUES");
    extened.appendRow();
    extened.setValue("SELECT_FLD", selection);
    extened.setValue("SIGN",       sign);
    extened.setValue("OPTION",     option);
    extened.setValue("LOW",        low);
    return populateParameter(function, false, returning);
  }

  private Map<String, Object> populateParameter(final JCoFunction function, final boolean defaultParameter, final String[] returning)
    throws ConnectionException {

    int encodedOffset = 0;
    int encodedLength = 0;
    int decodedOffset = 0;
    int decodedLength = 0;
    try {
      function.execute(this.destination);
    }
    catch (JCoException e) {
      throw new ConnectionException(e);
    }

    // loop through segments and get the corresponding encode and decode
    // values to be put in the Map
    final JCoTable segment     = function.getTableParameterList().getTable("DESCRIPTION_FOR_HELPVALUES");
    final int      segmentSize = segment.getNumRows();
    if (segmentSize > 0) {
      for (int i = 0; i < segmentSize; i++) {
        segment.setRow(i);
        final String field = segment.getString("FIELDNAME");
        if (field.equals(returning[0])) {
          encodedOffset = Integer.parseInt(segment.getString("OFFSET"));
          encodedLength = Integer.parseInt(segment.getString("LENG"));
          encodedLength += encodedOffset;
        }
        if (field.equals(returning[1])) {
          decodedOffset = Integer.parseInt(segment.getString("OFFSET"));
          decodedLength = Integer.parseInt(segment.getString("LENG"));
          decodedLength += decodedOffset;
        }
      }
    }

    // Hit list matching selection criteria
    final JCoTable            criteria     = function.getTableParameterList().getTable("HELPVALUES");
    final int                 criteriaSize = criteria.getNumRows();
    final Map<String, Object> values       = new LinkedHashMap<String, Object>(criteriaSize);
    if (criteriaSize > 0) {
      String encoded = null;
      String decoded = null;
      int    i2      = -1;
      int    i3      = -1;
      for (int i = 0; i < criteriaSize; i++) {
        criteria.setRow(i);
        final String raw = criteria.getString("HELPVALUES");
        if (defaultParameter) {
            i2 = raw.indexOf('N');
            i3 = raw.indexOf('1');
        }
        if (raw != null && raw.length() == 0) {
          encoded = null;
          decoded = null;
        }
        else if (raw.length() < encodedLength) {
          encoded = raw.substring(encodedOffset);
          decoded = null;
        }
        else if (raw.length() > encodedLength && raw.length() < decodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);
          if (i2 != -1)
            decoded = raw.substring(i2);
          else if (i3 != -1)
            decoded = raw.substring(i3);
          else if ((decodedOffset > 0) && (raw.length() > decodedOffset))
            decoded = raw.substring(decodedOffset);
        }
        else if (raw.length() > decodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);
          if (i2 != -1)
            decoded = raw.substring(i2);
          else if (i3 != -1)
            decoded = raw.substring(i3);
          else
            decoded = raw.substring(decodedOffset, decodedLength);
        }
        else if (raw.length() == encodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);
          decoded = null;
        }
        else if (raw.length() == decodedLength) {
          encoded = raw.substring(encodedOffset, encodedLength);
          if (i2 != -1)
            decoded = raw.substring(i2);
          else if (i3 != -1)
            decoded = raw.substring(i3);
          else
            decoded = raw.substring(decodedOffset);
        }
        values.put(encoded, decoded);
      }
    }
    return values;
  }
}