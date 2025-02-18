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
    Subsystem   :   Connector Bundle Framework

    File        :   Reconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Reconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Set;
import java.util.List;
import java.util.Date;
import java.util.HashSet;

import java.io.EOFException;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.net.NoRouteToHostException;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import groovy.lang.GroovyRuntimeException;

import org.xml.sax.InputSource;

import org.identityconnectors.framework.api.ConnectorFacade;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.objects.filter.Filter;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.MDSIOException;

import oracle.iam.request.vo.ApprovalConstants;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import oracle.iam.identity.connector.integration.FilterFactory;
import oracle.iam.identity.connector.integration.BundleLocator;
import oracle.iam.identity.connector.integration.TargetFeature;
import oracle.iam.identity.connector.integration.ServerResource;
import oracle.iam.identity.connector.integration.TargetResource;
import oracle.iam.identity.connector.integration.FrameworkException;

import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Reconciliation</code> acts as the service end point for Identity
 ** Manager to reconcile metadata information from any Service Provider.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public abstract class Reconciliation extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default format to prefix <code>Lookup Definition</code> encoded entries.
   */
  protected static final String PATTREN_ENCODED_VALUE = "%d~%s";
  /**
   ** Default format to prefix <code>Lookup Definition</code> decoded entries.
   */
  protected static final String PATTREN_DECODED_VALUE = "%s~%s";

  /** the name of the objectClass to reconcile */
  protected static final String OBJECT_CLASS          = "Object Class";

  /**
   ** Attribute tag which may be defined on this task to specify the objectClass
   ** for the search container in a <code>Service Provider</code>.
   ** <br>
   ** This attribute is optional.
   */
  public static final String SEARCH_CONTAINER         = "Search Container";

  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve objects from a <code>Service Provider</code>.
   ** <br>
   ** This attribute is optional.
   */
  public static final String SEARCH_BASE              = "Search Base";

  /**
   ** Attribute tag which must be defined on this task to specify which filter
   ** criteria has to be applied to retrieve <code>Service Provider</code>
   ** entries.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String SEARCH_FILTER         = "Search Filter";

  /**
   ** The default size of a batch a reconciliation process will query resources
   ** from a Service Provider.
   */
  protected static final int    BATCH_SIZE_DEFAULT    = 500;

  /**
   ** The name a connector service consumer will put in the appropriate options
   ** of a reconciliation process to configure the size of a batch of resources
   ** returned from a Service Provider.
   */
  protected static final String BATCH_SIZE            = "batchSize";

  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search order of resources
   ** returned from a <code>Service Provider</code>.
   */
  protected static final String SEARCH_ORDER          = "searchOrder";

  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure that a filter should be used in
   ** conjunction with the value of {@link #SYNCHRONIZATION_TOKEN} to decrease
   ** the result set size in operational mode to the changes made in the target
   ** system after the value of {@link #SYNCHRONIZATION_TOKEN}
   */
  protected static final String INCREMENTAL           = "incremental";

  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the synchronization token .
   */
  protected static final String SYNCHRONIZATION_TOKEN = "synchronizationToken";

  /**
   ** The SQL statement string to lookup an <code>Application Instance</code>
   ** by the names of a <code>Resource Object</code> and an
   ** <code>IT Resource</code>.
   */
  private static final String   LOOKUP_INSTANCE       = "select app_instance_name from app_instance where app_instance.itresource_key = ? and app_instance.object_key = ?";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer to describe the connection to the target system */
  protected TargetFeature       feature;
  protected TargetResource      resource;
  protected ObjectClass         conatinerClass;

  /** the abstraction layer to describe the connection to the connector server */
  protected ServerResource      endpoint;
  protected ConnectorFacade     connector;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> with the specified logging
   ** category.
   **
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected Reconciliation(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connector
  /**
   ** Returns the {@link ConnectorFacade} which handles the communication with
   ** the target system.
   **
   ** @return                    the {@link ConnectorFacade} which handles the
   **                            communication with the target system.
   **                            <br>
   **                            Possiblle object {@link ConnectorFacade}.
   */
  protected final ConnectorFacade connector() {
    return this.connector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClass
  /**
   ** Returns the {@link ObjectClass} based on "Object Class" scheduled task
   ** parameter.
   **
   ** @return                    the resolved {@link ObjectClass}.
   **                            <br>
   **                            Possiblle object {@link ObjectClass}.
   */
  protected ObjectClass objectClass() {
    return DescriptorTransformer.objectClass(objectClassName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClass
  /**
   ** Returns the name of connector attribute whose value is supposed to be used
   ** as {@link ObjectClass}.
   **
   ** @return                    the name of connector attribute whose value is
   **                            supposed to be used as {@link ObjectClass}.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  protected String objectClassName() {
    return stringValue(OBJECT_CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchContainer
  /**
   ** Returns the {@link ObjectClass} based on "Search Container" scheduled
   ** task parameter.
   **
   ** @return                    the resolved {@link ObjectClass}.
   **                            <br>
   **                            Possiblle object {@link ObjectClass}.
   */
  protected ObjectClass searchContainer() {
    return DescriptorTransformer.objectClass(searchContainerClassName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchContainerClassName
  /**
   ** Returns the name of connector attribute whose value is supposed to be used
   ** as {@link ObjectClass}.
   **
   ** @return                    the name of connector attribute whose value is
   **                            supposed to be used as {@link ObjectClass}.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  protected String searchContainerClassName() {
    return stringValue(SEARCH_CONTAINER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   incremental
  /**
   ** Returns reconciliation strategy.
   ** <br>
   ** <code>true</code> means that a filter should be used with the timestamp
   ** attribute {@link #TIMESTAMP} in the search to decrease the result set size
   ** in operational mode to the changes made in the target system after the
   ** timestamp of {@link #TIMESTAMP}; <code>false</code> means a full
   ** reconciliation will happens.
   **
   ** @return                    <code>true</code> that a filter should be used
   **                            with the timestamp attribute {@link #TIMESTAMP}
   **                            in the search to decrease the result set size
   **                            in operational mode to the changes made in the
   **                            target system after the timestamp of
   **                            {@link #TIMESTAMP}; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possiblle object {@link Boolean}.
   */
  public Boolean incremental() {
    return Boolean.valueOf(booleanValue(INCREMENTAL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizationToken
  /**
   ** Returns value of the synchronization token usuallly the timestamp of the
   ** last execution the job completed succesfully.
   **
   ** @return                    the synchronization token usuallly the
   **                            timestamp of the last execution the job
   **                            completed succesfully.
   **                            <br>
   **                            Possiblle object {@link String}.
   */
  public String synchronizationToken() {
    // the expected format of the synchronization token  must be compliant
    // to RFC 4517
    return DateUtility.display(timestampValue(TIMESTAMP), DateUtility.RFC4517_ZULU_NANO);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   singleValue
  /**
   ** Obtain the single value form the psecified {@link Attribute}.
   ** @param  attribute          the ICF attribute providing acces to the
   **                            values.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the first value of the {@link Attribute}
   **                            specified or <code>null</code> if the
   **                            {@link Attribute} has no values.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected static String singleValue(final Attribute attribute) {
    // prevent bogus input
    if (attribute == null)
      return null;

    final List<Object> value = attribute.getValue();
    return (CollectionUtility.empty(value)) ? null : (String)value.get(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groovify
  /**
   ** Replace any occurenc of a dot (<code>.</code>) character with an
   ** underscore (<code>_</code>) character before its passed to anywhere in the
   ** Groovy world.
   **
   ** @param  subject            the value to handle.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @return                    a string where any occurence of a dot is
   **                            replaced by an underscore.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected static String groovify(final String subject) {
    // GroovyShell is stupid because its not understanding dotted syntax of a
    // variable therefore we need to replace any dot with a different character
    // so that GroovyShell is able to compile and evaluate
    return subject.replaceAll("\\.", "_");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    configure();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Initalize the configuration capabilities.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void configure()
    throws TaskException {

    final String method = "configure";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      this.resource = populateResource();
      final String feature = this.resource.feature();
      if (!StringUtility.isEmpty(feature))
        this.feature = populateFeature(feature);
      final String server = this.resource.connectorServer();
      if (!StringUtility.isEmpty(server))
        this.endpoint = ServerResource.build(this, server);

      // produce the logging output only if the logging level is enabled for
      if (this.logger.debugLevel()) {
        if (this.endpoint != null)
          debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.endpoint.toString()));
        debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.resource.toString()));
      }
      if (!StringUtility.isEmpty(stringValue(SEARCH_CONTAINER))) {
        this.conatinerClass = DescriptorTransformer.objectClass(stringValue(SEARCH_CONTAINER));
      }
      this.connector = BundleLocator.create(this.endpoint, this.resource, this.feature);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime
  /**
   ** The current time might be different from system timestamp Identity Manager
   ** is running on.
   ** <p>
   ** To be able to plugin your own method this is the placeholder method to
   ** fetch the system time from the target system.
   **
   ** @return                    the timestamp of the remote system if
   **                            applicable; the local time otherwise.
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @throws TaskException      if the operation fails
   */
  protected Date systemTime()
    throws TaskException {

    return DateUtility.now();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateResource
  /**
   ** Initalize the IT Resource capabilities.
   ** <p>
   ** The approach implementing this method should be:
   ** <ol>
   **   <li>If only  Application Name is configured as job parameter and
   **       IT Resource or Reconciliation Object is not configured, then
   **       IT Resource and Reconciliation Object will be fetched by the mapping
   **       with the given application name.
   **   <li>If IT Resource and Reconciliation Object are configured as job
   **       parameters then they gets priority even application name is also
   **       configured as job parameter
   **   <li>{@link #configure()} method logic handle the precedence of for cases
   **       mentioned in point #1 &amp; #2
   ** </ol>
   ** 
   ** @return                    a {@link TargetResource} populated and
   **                            validated.
   **                            <br>
   **                            Possible object is {@link TargetResource}.
   **
   ** @throws TaskException      if the initialization of the
   **                            <code>IT Resource</code> fails.
   */
  protected abstract TargetResource populateResource()
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationOptions
  /**
   ** Factory method used in the lookup reconciliation search to configure the
   ** {@link OperationOptions} passed to the bundle from the default parameters
   ** of a job like <code>Incremental</code>.
   **
   ** @return                    the {@link OperationOptions} containing the
   **                            defaults to query the target system .
   **                            <br>
   **                            Possible object is {@link OperationOptions}.
   */
  protected final OperationOptions operationOptions() {
    return operationBuilder().build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationBuilder
  /**
   ** Factory method used in the main reconciliation search to configure the
   ** {@link OperationOptions} passed to the bundle from the default parameters
   ** of a job like <code>Incremental</code>.
   **
   ** @return                    the {@link OperationOptionsBuilder} containing
   **                            the defaults to query the target system.
   **                            <br>
   **                            Possible object is
   **                            {@link OperationOptionsBuilder}.
   */
  protected abstract OperationOptionsBuilder operationBuilder();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationalException
  /**
   ** Takes a {@link ConnectorException} thrown at any operation and throws back
   ** a {@link TaskException} that contains a better explanation what really was
   ** going wrong by verifiying the root cause auf the
   ** {@link ConnectorException} thrown by an operation.
   **
   ** @param  cause              the causing {@link ConnectorException}.
   **                            <br>
   **                            Allowed object is {@link ConnectorException}.
   **
   ** @return                    the {@link TaskException} with the explanation
   **                            of the causing {@link ConnectorException}.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  protected final TaskException operationalException(final ConnectorException cause) {
    Throwable t = cause.getCause();
    if (t != null) {
      if ((t instanceof UnknownHostException)) {
        return FrameworkException.unknownHost(this.endpoint.serverName(), t);
      }
      else if (t instanceof NoRouteToHostException) {
          return FrameworkException.createSocket(this.endpoint.serverName(), this.endpoint.serverPort());
      }
      else if ((t instanceof ConnectException)) {
        return FrameworkException.unavailable(this.endpoint.serverName(), this.endpoint.serverPort(), t);
      }
      else if ((t instanceof SocketTimeoutException))
        return FrameworkException.timeout(this.endpoint.serverName(), this.endpoint.serverPort(), t);
      else if ((t instanceof EOFException)) {
        return FrameworkException.createSocket(this.endpoint.serverName(), this.endpoint.serverPort());
      }
      else {
        return FrameworkException.unhandled(cause);
      }
    }
    else {
      return FrameworkException.abort(cause);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filterAttributes
  /**
   ** Returns attribute names which are defined in the {@link Filter}.
   **
   ** @return                    the {@link Set} of attribute names,
   **                            <code>null</code> if the {@link Filter} is not
   **                            defined.
   **                            <br>
   **                            Possiblle object {@link Set} where each element
   **                            is of type {@link String}.
   */
  protected final Set<String> filterAttributes() {
    final Filter filter = filter();
    if (filter == null)
      return new HashSet<String>();

    final FilterTranslator  translator = new FilterTranslator();
    final List<Set<String>> attributes = translator.translate(filter);
    return (attributes != null && attributes.size() == 1) ? attributes.get(0) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Parses the filter specified in task parameter.
   **
   ** @return                    parsed {@link Filter}, or <code>null</code> if
   **                            the filter is not defined.
   **                            <br>
   **                            Possiblle object {@link Filter}.
   */
  protected final Filter filter() {
    Filter filter = null;
    try {
      final String expression = stringValue(SEARCH_FILTER);
      if (!StringUtility.isBlank(expression)) {
        String dateAttributes = stringValue(TIMESTAMP);
        String dateFormat     = stringValue(TIMESTAMP_FORMAT);
        filter = FilterFactory.build(expression, dateAttributes, dateFormat);
      }
    }
    catch (GroovyRuntimeException e) {
      throw new IllegalArgumentException("Invalid Filter [" + e.getMessageWithoutLocationText() + ']');
    }
    catch (RuntimeException e) {
      throw new IllegalArgumentException("Invalid Filter [" + e.getMessage() + ']');
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateInstance
  /**
   ** Lookups an {@link ApplicationInstance} from Identity Manager.
   **
   ** @param  applicationName    the name of an {@link ApplicationInstance} to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ApplicationInstance} that match the
   **                            specified name or <code>null</code> if no
   **                            {@link ApplicationInstance} match the specified
   **                            name.
   **                            <br>
   **                            Possible object {@link ApplicationInstance}.
   **
   ** @throws TaskException       in case an error does occur.
   */
  protected ApplicationInstance populateInstance(final String applicationName)
    throws TaskException {

    final String method = "populateInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    final ApplicationInstanceService facade = service(ApplicationInstanceService.class);
    ApplicationInstance instance = null;
    try {
      instance = facade.findApplicationInstanceByName(applicationName);
    }
    // in case the application with the name does not exists in Identity Manager
    catch (ApplicationInstanceNotFoundException e) {
      final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, ApplicationInstance.APPINST_NAME, applicationName};
      error(method, TaskBundle.format(TaskError.ENTITY_NOT_FOUND, arguments));
    }
    // in case a wrong attribute name is passed as a returning value to the
    // search
    catch (GenericAppInstanceServiceException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateInstance
  /**
   ** Lookups an {@link ApplicationInstance} from Identity Manager.
   ** <pre>
   **   SELECT app_instance_name
   **   FROM   app_instance
   **   WHERE  app_instance.itresource_key = ?
   **   AND    app_instance.object_key     = ?
   ** </pre>
   **
   ** @param  objectKey          the internal identifier of a
   **                            <code>Resource Object</code> the desired
   **                            {@link ApplicationInstance} belongs to.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  resourceKey        the internal identifier of a
   **                            <code>IT Resource</code> the desired
   **                            {@link ApplicationInstance} belongs to.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the {@link ApplicationInstance} that match the
   **                            specified name or <code>null</code> if no
   **                            {@link ApplicationInstance} match the specified
   **                            name.
   **                            <br>
   **                            Possible object {@link String}.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected String populateInstance(final long objectKey, final long resourceKey)
    throws TaskException {

    final String method = "populateInstance";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    String            name       = null;
    try {
      connection = DatabaseConnection.aquire();
      statement  = DatabaseStatement.createPreparedStatement(connection, LOOKUP_INSTANCE);
      statement.setLong(1, resourceKey);
      statement.setLong(2, objectKey);
      resultSet  = statement.executeQuery();
      // position the cursor on the next available row
      if (resultSet.next()) {
        // fetch at least one value to ensure the result set is positoned
        // properly
        name = resultSet.getString(1);
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateFeature
  /**
   ** Factory method to configure the <code>Metadata Descriptor</code> defining
   ** the special features of the Service Provider endpoint from a path.
   **
   ** @param  path               the path to the feature configration stored in
   **                            the Metadata Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>TargetFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is {@link TargetFeature}.
   **
   ** @throws TaskException      in case marshalling the
   **                            <code>Metadata Descriptor</code> fails.
   */
  protected final TargetFeature populateFeature(final String path)
    throws TaskException {

    final String method  = "populateFeature";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session  = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      if (document == null)
        throw new TaskException(TaskError.METADATA_OBJECT_NOTFOUND, path);

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      return populateFeature(document.read());
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
    catch (MDSIOException e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateFeature
  /**
   ** Factory method to configure the <code>Metadata Descriptor</code> defining
   ** the special features of the Service Provider endpoint from an
   ** {@link InputSource}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    an instance of <code>TargetFeature</code>
   **                            populated from the XML document sepcified.
   **                            <br>
   **                            Possible object is {@link TargetFeature}.
   **
   ** @throws TaskException      in case marshalling the
   **                            <code>Metadata Descriptor</code> fails.
   */
  protected abstract TargetFeature populateFeature(final InputSource source)
    throws TaskException;
}