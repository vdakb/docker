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

    File        :   AnalyticsRoleExporter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AnalyticsRoleExporter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.4      2015-11-24  JBandeir    Fixed BusinessRoleEntity. Get Application
                                         Instance ID by name.
    3.1.0.3      2015-11-18  UFischer    invokeExport now catches WebServiceException
                                         and returns null while logging fatal
    3.1.0.2      2015-11-18  UFischer    more robust logging on failure in exportRole
    3.1.0.1      2015-11-13  UFischer    fixed entitlement without prefix causing
                                         StringIndexOutOfBoundsException
                                         added some more logging
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import javax.xml.datatype.XMLGregorianCalendar;

import javax.xml.ws.WebServiceException;

import Thor.API.tcResultSet;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import oracle.iam.catalog.vo.OIMType;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLOutputNode;
import oracle.hst.foundation.xml.XMLProcessor;

import oracle.iam.analytics.harvester.domain.role.Policy;
import oracle.iam.analytics.harvester.request.role.Exists;
import oracle.iam.analytics.harvester.request.role.ExistsResponse;
import oracle.iam.analytics.harvester.request.role.Export;
import oracle.iam.analytics.harvester.request.role.ExportResponse;
import oracle.iam.analytics.harvester.request.role.ObjectFactory;
import oracle.iam.analytics.harvester.service.role.Proxy;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.naming.ITResource;

import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.BusinessRoleEntity;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.soap.WebServiceResource;
import oracle.iam.identity.foundation.soap.WebServiceConnector;
import oracle.iam.identity.ots.persistence.Repository;

import oracle.iam.identity.ots.resource.HarvesterBundle;

import oracle.iam.identity.ots.service.ControllerError;
import oracle.iam.identity.ots.service.ControllerException;

import oracle.iam.identity.utility.file.XMLCatalogFactory;
import oracle.iam.identity.utility.file.XMLBusinessRoleFactory;

////////////////////////////////////////////////////////////////////////////////
// class AnalyticsRoleExporter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AnalyticsRoleExporter</code> acts as the service end point for
 ** the Oracle Identity Manager to off-board business roles information to an
 ** offlined XML file.
 ** <p>
 ** This service is intentionally used to extract roles from Oracle Identity
 ** Analytics and their associate <code>Policies</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.1
 ** @since   3.0.0.0
 */
public class AnalyticsRoleExporter extends Harvester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String ENTITLEMENT_PREFIX     = "Entitlement Prefix Stripped";
  protected static final String SAPUM_MASTERSYSTEMNAME = "Master system name";

  /**
   ** the array with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute  = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,           TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation object */
  , TaskAttribute.build(RECONCILE_OBJECT,     "Role")
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATAFILE,             TaskAttribute.MANDATORY)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,          TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,         TaskAttribute.MANDATORY)
    /** the class name of the entity factory  */
  //, TaskAttribute.build(MARSHALLER,           TaskAttribute.MANDATORY)
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,   TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,        TaskAttribute.MANDATORY)
  };

  private static final String PROPERTY_LOOKUP_CODE        = "Lookup Code";
  private static final String PROPERTY_FIELD_TYPE         = "LookupField";
  private static final String PROPERTY_COLUMN_NAME        = "name";

  private static final String  ROLE_ATTR_DISPLAYNAME      = "DISPLAYNAME";
  private static final String  ROLE_ATTR_DESCRIPTION      = "DESCRIPTION";
  private static final String  ROLE_ATTR_COMMENT          = "COMMENT";
  private static final String  ROLE_ATTR_DEPARTMENT       = "DEPARTMENT";
  private static final String  ROLE_ATTR_JOBCODE          = "JOBCODE";
  private static final String  ROLE_ATTR_TYPE             = "TYPE";
  private static final String  ROLE_ATTR_RISK             = "RISK";
  private static final String  ROLE_ATTR_STARTDATE        = "STARTDATE";
  private static final String  ROLE_ATTR_ENDDATE          = "ENDDATE";
  private static final String  ROLE_ATTR_CUSTOMPROPERTY   = "CUSTOMPROPERTY";
  private static final String  ROLE_ATTR_BUSINESSUNIT     = "BUSINESSUNIT";
  private static final String  ROLE_ATTR_OWNERSHIP        = "OWNERSHIP";


  /**
   ** The SQL statement string to lookup a <code>Lokup Definition</code> of a
   ** process form tagged as entitlement.
   */
  private static final String LOOKUP_ATTRIBUTE     = "SELECT sdp.sdp_property_value as name FROM sdc, sdp WHERE sdc.sdc_key = sdp.sdc_key AND sdc.sdc_name = ? AND sdc.sdc_field_type = ? AND sdp.sdp_property_name = ?";

  /**
   ** The SQL statement string to lookup the roles names
   ** available on the system
   */
  private static final String LOOKUP_ROLES     = "SELECT ugp_name FROM ugp where ugp.ugp_role_category_key != 2";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer to describe the connection to the target system */
  private WebServiceResource  resource;

  /** the abstraction layer to communicate with the target system */
  private WebServiceConnector connector;

  /** the abstraction layer to communicate with the target system */
  private Dispatch<Object>    dispatcher;

  /** the factories to marshall/unmarshal XML */
  private final ObjectFactory proxyFactory = new ObjectFactory();

  /** the roles root element of the XML file to produce */
  protected XMLOutputNode       roles              = null;

  protected Repository          repository         = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AnalyticsRoleExporter</code> scheduled job that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AnalyticsRoleExporter() {
    // ensure inheritance
    super(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the reconciliation task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {
    final String method = "onExecution";

    final String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
    info(HarvesterBundle.format(HarvesterMessage.EXPORTING_BEGIN, parameter));
    try {
      HarvesterBundle.string(HarvesterMessage.COLLECTING_BEGIN);
      final List<String> result = getOimRoles();
      HarvesterBundle.string(HarvesterMessage.COLLECTING_COMPLETE);

      for (String roleName : result) {
        info(HarvesterBundle.format(HarvesterMessage.OPERATION_EXPORT_BEGIN, OIMType.Role.getValue(), roleName));

        ensureRoot();

        final BusinessRoleEntity role = exportRole(roleName);

        if (role == null) {
          incrementFailed();
          warning(method, HarvesterBundle.format(HarvesterError.OPERATION_EXPORT_FAILED, OIMType.Role.getValue(), roleName, BusinessRoleEntity.class.getName() + " was null"));
        }
        else if (gatherOnly()) {
          incrementIgnored();
          debug(method, HarvesterBundle.format(HarvesterMessage.OPERATION_EXPORT_IGNORED, OIMType.Role.getValue(), roleName));
        }
        else {
          writeRole(role);
          incrementSuccess();
          info(HarvesterBundle.format(HarvesterMessage.OPERATION_EXPORT_SUCCESS, OIMType.Role.getValue(), roleName));
        }
      }
      this.updateLastReconciled();
      if (isStopped()) {
        warning(HarvesterBundle.format(HarvesterMessage.EXPORTING_STOPPED, parameter));
      }
      else {
        info(HarvesterBundle.format(HarvesterMessage.EXPORTING_COMPLETE, parameter));
      }
    }
    catch (TaskException e) {
      final String[] arguments = { reconcileObject(), getName() , dataFile().getAbsolutePath() , e.getLocalizedMessage()};
      // notify user about the problem
      warning(HarvesterBundle.format(HarvesterMessage.EXPORTING_ERROR, arguments));
      throw e;
    }
    finally {
      try {
        if (this.roles != null)
          this.roles.commit();
      }
      catch (XMLException e) {
        throw new TaskException(e);
      }
    }
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
    // this will produce the trace of the configured task parameter and create
    // the abstract file paths to the data and error directories
    super.initialize();

    initializeConnector();

    // the database dialect we will need everywhere hence instantiate it only
    // once
    this.repository = Repository.create("oracle.jdbc.OracleDriver");
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOimRoles
  /**
   ** Returns the a {@link List} of {@link String}s with names of the roles
   ** available on Oracle Identity Manager.
   **
   ** @return                    List of OIM roles names.
   **
   ** @throws HarvesterException in the event of database access fails.
   */
  protected List<String> getOimRoles()
    throws HarvesterException {

    final String method = "getOimRoles";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    List<String>      roles      = new ArrayList<String>();
    try {
      connection = DatabaseConnection.aquire();
      statement  = DatabaseStatement.createPreparedStatement(connection, LOOKUP_ROLES);
      resultSet  = statement.executeQuery();

      while(resultSet.next()) {
        roles.add(resultSet.getString(1));
      }
    }
    catch (Exception e) {
      throw new HarvesterException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return roles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  exportRole
  /**
   ** Factory method to create a particular {@link BusinessRoleEntity} to be
   ** exported to a XML file.
   **
   ** @param  name               the name of a particular
   **                            {@link BusinessRoleEntity} to export.
   **
   ** @return                    the {@link BusinessRoleEntity} created.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  protected BusinessRoleEntity exportRole(final String name)
    throws TaskException {

    final String method = "exportRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);

    BusinessRoleEntity entity = null;
    try {
      if (invokeExists(name)) {
        entity = invokeExport(name);
      }
    } finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeConnector
  /**
   ** Initalize the connector capabilities.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected void initializeConnector()
    throws TaskException {

    final String method = "initializeConnector";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      this.resource  = new WebServiceResource(this, this.resourceName());
      this.connector = new WebServiceConnector(this, this.resource);
      // produce the logging output only if the logging level is enabled for
      if (this.logger.debugLevel())
        debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.connector.toString()));
//      final String xxx = UTF_ENCODING;
      // ensure that instantiating the binding context the classloader is always
      // passed in otherwise the package resolver may throw
      final JAXBContext context = JAXBContext.newInstance(Exists.class, ExistsResponse.class, Export.class, ExportResponse.class);
      // creating the webservice client proxy at this time we are able to check
      // implicitly the configuration and the availability of the service
      // endpoint
      this.dispatcher = this.connector.createDispatch(this.connector.createService(Proxy.PATH, Proxy.NAME), Proxy.PORT, context, Service.Mode.PAYLOAD);
    }
    catch (JAXBException e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  invokeExists
  /**
   ** Retrieve a particular {@link BusinessRoleEntity} from Oracle Identity
   ** Anlaytics.
   **
   ** @param  payload            a particular {@link BusinessRoleEntity} to
   **                            create wrapped in the proper XML.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  private boolean invokeExists(final String payload)
    throws TaskException {

    final String method = "invokeExists";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    final Exists  request = this.proxyFactory.createExists();
    request.setIn0(payload);
    debug(method, HarvesterBundle.format(HarvesterMessage.SERVICE_REQUEST_PAYLOAD, payload));
    try {
      final Object         response  = this.dispatcher.invoke(request);
      final ExistsResponse value     = (ExistsResponse)response;
      return value.isExists();
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  invokeExport
  /**
   ** Retrieve a particular {@link BusinessRoleEntity} from Oracle Identity
   ** Anlaytics.
   **
   ** @param  payload            a particular {@link BusinessRoleEntity} to
   **                            create wrapped in the proper XML.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  private BusinessRoleEntity invokeExport(final String payload)
    throws TaskException {

    final String method = "invokeExport";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Export request = this.proxyFactory.createExport();
    request.setIn0(payload);
    debug(method, HarvesterBundle.format(HarvesterMessage.SERVICE_REQUEST_PAYLOAD, payload));
    try {
      final Object         response  = this.dispatcher.invoke(request);
      final ExportResponse value     = (ExportResponse)response;
      return transform(value.getPolicy());
    }
    catch (WebServiceException e) {
      fatal(method, e);
      return null;
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Parse the content of the {@link Policy} and create a
   ** {@link BusinessRoleEntity} from it.
   **
   ** @param  policy             the {@link Policy} retrieved from the service
   **                            call to be transformed in a
   **                            {@link BusinessRoleEntity}.
   **
   ** @return                    the {@link BusinessRoleEntity} build from the
   **                            specified {@link Policy}.
   **
   ** @throws TaskException      if the transformation failes for any reason.
   */
  private BusinessRoleEntity transform(final Policy policy)
    throws TaskException  {
    final String method = "transform";
    trace(method, SystemMessage.METHOD_ENTRY);

    final BusinessRoleEntity role = new BusinessRoleEntity(policy.getId());
    role.risk(policy.getRisk());
    role.action(BusinessRoleEntity.Action.assign);

    role.addAttribute(ROLE_ATTR_DISPLAYNAME , policy.getDisplayName());
    role.addAttribute(ROLE_ATTR_DESCRIPTION , policy.getDescription());
    role.addAttribute(ROLE_ATTR_COMMENT     , policy.getComment());
    role.addAttribute(ROLE_ATTR_DEPARTMENT  , policy.getDepartment());
    role.addAttribute(ROLE_ATTR_JOBCODE     , policy.getJobCode());
    role.addAttribute(ROLE_ATTR_TYPE        , policy.getType());
    role.addAttribute(ROLE_ATTR_RISK        , policy.getRisk());

    XMLGregorianCalendar calendarValue = policy.getStartDate();
    if (calendarValue != null) {
      role.addAttribute(ROLE_ATTR_STARTDATE , calendarValue.toString());
    }
    calendarValue = policy.getEndDate();
    if (calendarValue != null) {
      role.addAttribute(ROLE_ATTR_ENDDATE   , calendarValue.toString());
    }

    // category is mandatory for a role hence we have to ensure that this
    // attribute is part of the spool out
    List<String> valueList = policy.getCustomProperty();
    if (!CollectionUtility.empty(valueList)) {
      if (!StringUtility.isEmpty(valueList.get(0))) {
        role.addAttribute(ROLE_ATTR_CUSTOMPROPERTY  , valueList.get(0));
      }
      else {
        role.addAttribute(ROLE_ATTR_CUSTOMPROPERTY  , OIMType.Role.getValue());
      }
    }
    else {
      role.addAttribute(ROLE_ATTR_CUSTOMPROPERTY    , OIMType.Role.getValue());
    }

    valueList = policy.getOwnerShip();
    if (!CollectionUtility.empty(valueList)) {
      final String value = StringUtility.listToString(valueList, this.connector.multiValueSeparator().charAt(0));
      if (!StringUtility.isEmpty(value)) {
        role.addAttribute(ROLE_ATTR_OWNERSHIP, value);
      }
    }
    valueList = policy.getBusinessUnit();
    if (!CollectionUtility.empty(valueList)) {
      final String value = StringUtility.listToString(policy.getBusinessUnit(), this.connector.multiValueSeparator().charAt(0));
      if (!StringUtility.isEmpty(value)) {
        role.addAttribute(ROLE_ATTR_BUSINESSUNIT, value);
      }
    }


    for (Policy.Resource resource : policy.getResource()) {

      final Long              objectKey   = lookupObject(resource.getNamespace());
      final Long              resourceKey = lookupResource(resource.getEndpoint());

      final ApplicationEntity entity      = new ApplicationEntity(populateInstance(objectKey, resourceKey));

      role.addApplication(entity);

      for (Policy.Resource.Entitlements namespace : resource.getEntitlements()) {
        final String lookup = lookupAttribute(namespace.getId());
        for (Policy.Resource.Entitlements.Entitlement entitlement : namespace.getEntitlement()) {
          String name = entitlement.getId();

          if (booleanValue(ENTITLEMENT_PREFIX)) {
            final String[] pattern = this.binding.get(resourceKey);
            String         prefix  = null;

            if (pattern == null || pattern.length == 0) {
              prefix = String.format(HarvesterDescriptor.DEFAULT_PATTERN_ENCODED, resourceKey, "");
            }
            else {
              prefix = String.format(pattern[1], resourceKey, pattern[0], "");
            }

            if (name.startsWith(prefix)) {
              name = name.substring(prefix.length());
            }else{
              // Specific for "SAP UM" Connector type using the value of the property Master "System Name"
              prefix = String.format(HarvesterDescriptor.DEFAULT_PATTERN_ENCODED, resourceKey, isSAPUM_prefix(resourceKey, SAPUM_MASTERSYSTEMNAME), "");
              if (name.startsWith(prefix)) {
                name = name.substring(prefix.length());
              } else if(this.logger.traceLevel()) {
                trace(method, HarvesterBundle.format(HarvesterError.ENTITLEMENT_NO_PREFIX, name));
              }
            }

          }

          entity.add(lookup, new EntitlementEntity(name));
        }
      }
    }


    trace(method, SystemMessage.METHOD_EXIT);
    return role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isSAPUM_prefix
  /**
   ** Lookups a {@link ITResource} from Oracle Identity Manager and
   ** check if is SAP UM type and have the property.
   **
   ** @param  resourceKey        the system itentifier of a
   **                            <code>IT Resource</code> to lookup.
   **
   ** @param  propertyName       the name of the property to return
   **
   ** @return                    the internal identifier of the desired
   **                            <code>IT Resource</code>.
   **
   ** @throws HarvesterException in case an error does occur.
   */
  protected String isSAPUM_prefix(final long resourceKey, final String propertyName)
    throws HarvesterException {

    final String method = "isSAPUM_prefix";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    String rtnValue = null;
    try {
      final tcITResourceInstanceOperationsIntf   instance   = service(tcITResourceInstanceOperationsIntf.class);
      tcResultSet resultSet = instance.getITResourceInstanceParameters(resourceKey);

      int count = resultSet.getRowCount();
      for (int i = 0; i < count; i++) {
        resultSet.goToRow(i);
        if ((resultSet.getStringValue(ITResource.PARAMETER_NAME).equalsIgnoreCase(propertyName))){
            rtnValue = resultSet.getStringValue(ITResource.PARAMETER_VALUE);
          }
      }
      return rtnValue;
    }
    catch (Exception e) {
      throw new HarvesterException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  lookupAttribute
  /**
   ** Lookup of the attribute the of a process form tagged as an entitlement and
   ** joined with the specified <code>Lookup Definition</code> as the
   ** entitlement namespace.
   ** <p>
   ** We assume that a <code>Lookup Definition</code> joined with a entitlement
   ** process form is used only once.
   ** <pre>
   ** SELECT sdp.sdp_property_value as lookup
   ** FROM   sdc, sdp
   ** WHERE  sdc.sdc_key           = sdp.sdc_key
   ** AND    sdc.sdc_name          = ?
   ** AND    sdc.sdc_field_type    = ?
   ** AND    sdp.sdp_property_name = ?
   ** </pre>
   **
   ** @param  namespace          the namespace of the
   **                            <code>Lookup Definition</code> potentionally
   **                            joined with a process form as an entitlement.
   **
   ** @return                    the name of the process form attribute
   */
  private final String lookupAttribute(final String namespace)
    throws HarvesterException {

    final String method = "lookupAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    String            lookup     = null;
    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    try {
      connection = DatabaseConnection.aquire();
      statement  = DatabaseStatement.createPreparedStatement(connection, LOOKUP_ATTRIBUTE);
      statement.setString(1, namespace);
      statement.setString(2, PROPERTY_FIELD_TYPE);
      statement.setString(3, PROPERTY_LOOKUP_CODE);
      resultSet  = statement.executeQuery();
      if (resultSet.next())
        lookup = resultSet.getString(PROPERTY_COLUMN_NAME);
    }
    catch (Exception e) {
      throw new HarvesterException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return lookup;
  }

  @Override
  protected void beforeExecution()
    throws TaskException {
    // ensure inheritance
    super.beforeExecution();

    final String method = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);
    /** the format specification */
    try {
      final File      dataFile = dataFile();
      // check if we have write access to the file if its exists
      if (dataFile.exists() && !dataFile.canWrite()) {
        final String[] values = { DATAFILE, dataFile.getName()};
        throw new ControllerException(ControllerError.FILE_NOT_WRITABLE, values);
      }

      // any roles or applications element can only be created within a catalog
      // element as its parent
      this.roles = XMLProcessor.marshal(this, dataFile, this.format).element(BusinessRoleEntity.MULTIPLE);
      this.roles.attribute(XMLProcessor.ATTRIBUTE_XMLNS,     XMLBusinessRoleFactory.NAMESPACE);
      this.roles.attribute(XMLProcessor.ATTRIBUTE_XMLNS_XSI, XMLBusinessRoleFactory.SCHEMA);
      this.roles.attribute(XMLProcessor.ATTRIBUTE_SCHEMA,    XMLBusinessRoleFactory.SCHEMA_LOCATION);
    }
    catch (XMLException e) {
      throw new TaskException(e);
    }
    finally {
      // stop the task timer from gathering performance metrics
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateDescripor
  /**
   ** Validate the definition contained in the descriptor by fetching the
   ** defined catalog metadata and drop all that are not exists in the
   ** underlying repository.
   **
   ** @throws HarvesterException if validation fails in general.
   */
  @Override
  protected void validateDescripor()
    throws HarvesterException {

    // intentionally left blank to avoid removing of any descriptor attribute
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeRole
  /**
   ** This is used to create the role element and all child nodes.
   **
   ** @param  role               the {@link BusinessRoleEntity} to write to the
   **                            XML stream.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void writeRole(final BusinessRoleEntity role)
    throws TaskException {

    // marshall the Java content tree to the XML file
    try {
      final XMLOutputNode root = this.roles.element(BusinessRoleEntity.SINGLE);
      Map<String, Object> subject = role.attribute();
      subject = transformMaster(createMaster(subject, false));
      role.attribute().clear();
      role.attribute().putAll(subject);

      writeEntity(role, root);

      final XMLOutputNode applications = root.element(ApplicationEntity.MULTIPLE);
      for (ApplicationEntity app : role.application()) {
        final XMLOutputNode application = applications.element(ApplicationEntity.SINGLE);
        application.attribute(XMLCatalogFactory.ATTRIBUTE_ID, app.name());
        for (String ns : app.namespace()) {
          final XMLOutputNode namespace = application.element(EntitlementEntity.MULTIPLE);
          namespace.attribute(XMLCatalogFactory.ATTRIBUTE_ID, ns);
          for (EntitlementEntity xxxxxx : app.entitlement(ns)) {
            final XMLOutputNode entitlement = namespace.element(EntitlementEntity.SINGLE);
            entitlement.attribute(XMLCatalogFactory.ATTRIBUTE_ID, xxxxxx.name());
          }
        }
      }
    }
    catch (XMLException e) {
      throw new HarvesterException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEntity
  /**
   ** This is used to create the application element within the XML
   ** document.
   **
   ** @param  entity             the {@link EntitlementEntity} currently in
   **                            scope.
   ** @param  node               the {@link XMLOutputNode} to populate.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void writeEntity(final EntitlementEntity entity, final XMLOutputNode node)
    throws XMLException {

    final String method = "writeEntity";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);
    try {
      node.attribute(XMLBusinessRoleFactory.ATTRIBUTE_ID,     entity.name());
      node.attribute(XMLBusinessRoleFactory.ATTRIBUTE_RISK,   String.valueOf(entity.risk().toString()));

      // add all other attributes as an attribute eleent
      final Map<String, Object> attribute = entity.attribute();
      // be safe and check if attributes exists on the entity to avoid NPE
      if (attribute != null && !attribute.isEmpty()) {
        for (Map.Entry<String, Object> entry : attribute.entrySet()) {
          final XMLOutputNode attachment = node.element(EntityFactory.ELEMENT_ATTRIBUTE);
          attachment.attribute(XMLBusinessRoleFactory.ATTRIBUTE_ID, entry.getKey());
          // to be safe check fist if the value meets all requirements
          if (entry.getValue() != null)
            attachment.value(entry.getValue().toString());
        }
      }
    }
    finally {
      // stop the task timer from gathering performance metrics
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureRoot
  /**
   ** This is used to create the error report root element.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void ensureRoot()
    throws TaskException {

    if (this.roles == null) {
      final String method = "ensureRoot";
      trace(method, SystemMessage.METHOD_ENTRY);
      // start the task to gather performance metrics
      timerStart(method);

      try {
        final File dataFile = dataFile();
        // check if we have write access to the file if its exists
        if (dataFile.exists() && !dataFile.canWrite()) {
          final String[] values = { DATAFILE, dataFile.getName()};
          throw new ControllerException(ControllerError.FILE_NOT_WRITABLE, values);
        }
        // any role element can only be created within a roles element as its
        // parent
        this.roles = XMLProcessor.marshal(this, dataFile, this.format).element(BusinessRoleEntity.MULTIPLE);
        this.roles.attribute(XMLBusinessRoleFactory.ATTRIBUTE_XMLNS,     XMLBusinessRoleFactory.NAMESPACE);
        this.roles.attribute(XMLBusinessRoleFactory.ATTRIBUTE_XMLNS_XSI, XMLBusinessRoleFactory.SCHEMA);
        this.roles.attribute(XMLBusinessRoleFactory.ATTRIBUTE_SCHEMA,    XMLBusinessRoleFactory.SCHEMA_LOCATION);
      }
      catch (XMLException e) {
        throw new TaskException(e);
      }
      finally {
        // stop the task timer from gathering performance metrics
        timerStop(method);
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }
  }
}