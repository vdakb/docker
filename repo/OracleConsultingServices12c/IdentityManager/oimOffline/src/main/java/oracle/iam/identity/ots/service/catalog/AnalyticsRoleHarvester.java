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

    File        :   AnalyticsRoleHarvester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AnalyticsRoleHarvester.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.Collection;

import java.text.ParseException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import javax.xml.ws.Service;
import javax.xml.ws.Dispatch;
import javax.xml.ws.WebServiceException;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.provisioning.vo.FormInfo;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.BusinessRoleEntity;

import oracle.iam.identity.foundation.persistence.DatabaseException;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.soap.WebServiceResource;
import oracle.iam.identity.foundation.soap.WebServiceConnector;

import oracle.iam.analytics.harvester.domain.Status;
import oracle.iam.analytics.harvester.domain.Result;
import oracle.iam.analytics.harvester.domain.Severity;

import oracle.iam.analytics.harvester.domain.role.Role;
import oracle.iam.analytics.harvester.domain.role.Policy;
import oracle.iam.analytics.harvester.domain.role.Context;
import oracle.iam.analytics.harvester.domain.role.ObjectFactory;

import oracle.iam.analytics.harvester.request.role.Create;
import oracle.iam.analytics.harvester.request.role.Modify;
import oracle.iam.analytics.harvester.request.role.CreateResponse;
import oracle.iam.analytics.harvester.request.role.ModifyResponse;

import oracle.iam.analytics.harvester.service.role.Proxy;

import oracle.iam.identity.ots.resource.HarvesterBundle;

////////////////////////////////////////////////////////////////////////////////
// class AnalyticsRoleHarvester
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AnalyticsRoleHarvester</code> acts as the service end point for
 ** the Oracle Identity Manager to on-board business roles information from an
 ** offlined XML file.
 ** <p>
 ** This service is intentionally used to created, modify roles in Oracle
 ** Identity Analytics and associate them with the appropriate
 ** <code>Policies</code>. The <code>Policies</code> might exists or needs to
 ** be created based on the detail information any abstract role handled by
 ** this service provides.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class AnalyticsRoleHarvester extends RoleHarvester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String          PROPERTY_LOOKUP_CODE   = "Lookup Code";

  private static final String          PROPERTY_COLUMN_NAME   = "name";
  private static final String          PROPERTY_COLUMN_TYPE   = "type";
  private static final String          PROPERTY_COLUMN_LABEL  = "label";

  /**
   ** The SQL statement string to lookup an <code>Attribute</code> of a process
   ** form tagged as entitlement and providing a <ode>Lookup Definition</code>
   ** for the associated entitlements.
   */
  private static final String          LOOKUP_ATTRIBUTE       = "SELECT sdc.sdc_name as name, sdc.sdc_label as label, sdc.sdc_field_type as type FROM sdc, sdp WHERE sdc.sdk_key = ? AND sdc.sdc_key = sdp.sdc_key AND sdp.sdp_property_name = ? AND sdp.sdp_property_value = ?";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,          TaskAttribute.MANDATORY)
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
  , TaskAttribute.build(UNMARSHALLER,         TaskAttribute.MANDATORY)
    /** the validation required before unmarshalling  */
  , TaskAttribute.build(VALIDATE_SCHEMA,      TaskAttribute.MANDATORY)
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,   TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,        TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer to describe the connection to the target system */
  private WebServiceResource  resource;

  /** the abstraction layer to communicate with the target system */
  private WebServiceConnector connector;

  /** the abstraction layer to communicate with the target system */
  private Dispatch<Object>    dispatcher;

  /** the transaction context  */
  private Context             context;

  /** the factories to marshall/unmarshal XML */
  private final oracle.iam.analytics.harvester.domain.ObjectFactory       baseFactory  = new oracle.iam.analytics.harvester.domain.ObjectFactory();
  private final oracle.iam.analytics.harvester.domain.role.ObjectFactory  roleFactory  = new oracle.iam.analytics.harvester.domain.role.ObjectFactory();
  private final oracle.iam.analytics.harvester.request.role.ObjectFactory proxyFactory = new oracle.iam.analytics.harvester.request.role.ObjectFactory();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** Simple value holder to keep track of an field descriptor in Oracle
   ** Identity Manager
   */
  private class Attribute {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String name;
    private final String label;
    private final String type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Attribute</code> with the specified properties.
     **
     ** @param  name             the physical name of the field defined in
     **                          Oracl Identity Manager.
     **                          This is name name of the column defined in the
     **                          user defined table.
     ** @param  label            the logical name of the field used in every
     **                          end-user UI and integration layer.
     ** @param  type             the logical type of the field in Oracle
     **                          Identity Manager
     */
    Attribute(final String name, final String label, final String type) {
      // ensure inheritance
      super();

      // initailize instance attributes
      this.name  = name;
      this.label = label;
      this.type  = type;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AnalyticsRoleHarvester</code> scheduled job that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AnalyticsRoleHarvester() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  process (EntityListener)
  /**
   ** Reconciles a particular batch of {@link BusinessRoleEntity}'s.
   **
   ** @param  bulk               the {@link Collection} of
   **                            {@link BusinessRoleEntity}'s to reconcile.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void process(final Collection<BusinessRoleEntity> bulk)
    throws TaskException {

    final String method = "process";
    trace(method, SystemMessage.METHOD_ENTRY);

    // validate the effort to do
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(bulk.size())));
    for (BusinessRoleEntity entity : bulk) {
      if (isStopped())
        break;
      // create the collection that will be return all messages which have
      // warnings or failed
      final Result result = this.baseFactory.createResult();
      if (populateRole(entity.name()) != null) {
        result.setStatus(Status.WARNING);
        recordMessage(result, Severity.WARNING, method, HarvesterBundle.format(HarvesterError.OBJECT_ELEMENT_EXISTS, RoleManagerConstants.ROLE_ENTITY_NAME, entity.name()));
        final Result response = modifyRole(entity);
        result.setStatus(response.getStatus());
        result.getMessage().addAll(response.getMessage());
      }
      else {
        final Result response = createRole(entity);
        result.setStatus(response.getStatus());
        result.getMessage().addAll(response.getMessage());
      }
      writeResult(entity, result);
    }
    trace(method, SystemMessage.METHOD_EXIT);
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
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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

      final HarvesterDescriptor descriptor = (HarvesterDescriptor)this.descriptor;
      // the JAXBContext instance is initialized from a list of colon separated
      // Java package names.
      // Each java package contains JAXB mapped classes, schema-derived classes
      // and/or user annotated classes. Additionally, the java package may
      // contain JAXB package annotations that must be processed.
      // (see JLS 3rd Edition, Section 7.4.1. Package Annotations).
      final JAXBContext         context    = JAXBContext.newInstance("oracle.iam.analytics.harvester.domain:oracle.iam.analytics.harvester.domain.role:oracle.iam.analytics.harvester.request.role", ObjectFactory.class.getClassLoader());
      // creating the webservice client proxy at this time we are able to check
      // implicitly the configuration and the availability of the service
      // endpoint
      this.dispatcher = this.connector.createDispatch(this.connector.createService(Proxy.PATH, Proxy.NAME), Proxy.PORT, context, Service.Mode.PAYLOAD);
      this.context    = this.roleFactory.createContext(descriptor.uniqueName(), descriptor.relationShip(), descriptor.exactMatch(), gatherOnly(), descriptor.ignoreWarning());
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
  // Method:  createRole
  /**
   ** Creates a particular {@link BusinessRoleEntity} in Oracle Identity
   ** Anlaytics.
   **
   ** @param  entity             a particular {@link BusinessRoleEntity} to
   **                            create.
   **
   ** @return                    a {@link Result} recorded for the operations
   **                            performed.
   */
  private Result createRole(final BusinessRoleEntity entity) {
    final String method = "createRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Result result = this.baseFactory.createResult();
    try {
      final Policy policy = toPolicy(entity, result);
      if (policy != null) {
        final Result response = invokeCreate(policy);
        result.setStatus(response.getStatus());
        result.getMessage().addAll(response.getMessage());
      }
    }
    catch (TaskException e) {
      result.setStatus(Status.FATAL);
      recordMessage(result, Severity.FATAL, method, e.getLocalizedMessage());
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  modifyRole
  /**
   ** Modifies a particular {@link BusinessRoleEntity} in Oracle Identity
   ** Anlaytics.
   **
   ** @param  entity             a particular {@link BusinessRoleEntity} to
   **                            modify.
   **
   ** @return                    a {@link Result} recorded for the operations
   **                            performed.
   */
  private Result modifyRole(final BusinessRoleEntity entity) {
    final String method = "modifyRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Result result = this.baseFactory.createResult();
    try {
      final Policy policy = toPolicy(entity, result);
      if (policy != null) {
        final Result response = invokeModify(policy);
        result.setStatus(response.getStatus());
        result.getMessage().addAll(response.getMessage());
      }
    }
    catch (TaskException e) {
      result.setStatus(Status.FATAL);
      recordMessage(result, Severity.FATAL, method, e.getLocalizedMessage());
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  invokeCreate
  /**
   ** Creates a particular {@link BusinessRoleEntity} in Oracle Identity
   ** Anlaytics.
   **
   ** @param  payload            a particular {@link Role} wrapper to create in
   **                            Oracle Identity Analytics.
   **
   ** @return                    a {@link Result} recorded for the operations
   **                            performed.
   **
   ** @throws HarvesterException thrown if the web service invocation fails.
   */
  private Result invokeCreate(final Policy payload)
    throws HarvesterException {

    final String method = "invokeCreate";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Create request = this.proxyFactory.createCreate(this.context, payload);
    try {
      final CreateResponse response = (CreateResponse)this.dispatcher.invoke(request);
      return response.getResult();
    }
    catch (WebServiceException e) {
      throw new HarvesterException(HarvesterError.ABORT, e.getLocalizedMessage());
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  invokeModify
  /**
   ** Modifies a particular {@link BusinessRoleEntity} in Oracle Identity
   ** Anlaytics.
   **
   ** @param  payload            a particular {@link Policy} wrapper to modify
   **                            in Oracle Identity Analytics.
   **
   ** @return                    a {@link Result} recorded for the operations
   **                            performed.
   **
   ** @throws HarvesterException thrown if the web service invocation fails.
   */
  private Result invokeModify(final Policy payload)
    throws HarvesterException {

    final String method = "invokeModify";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Modify request = this.proxyFactory.createModify(this.context, payload);
    try {
      final ModifyResponse response = (ModifyResponse)this.dispatcher.invoke(request);
      return response.getResult();
    }
    catch (WebServiceException e) {
      throw new HarvesterException(HarvesterError.ABORT, e.getLocalizedMessage());
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  toPolicy
  /**
   ** Factory method to transform a {@link BusinessRoleEntity} to a
   ** {@link Policy}.
   **
   ** @param  entity             the {@link BusinessRoleEntity} to transform
   **
   ** @return                    the {@link Policy} transformed so far.
   **
   ** @throws TaskException      if data processing fails.
   */
  private final Policy toPolicy(final BusinessRoleEntity entity, final Result result)
    throws TaskException {

    final String method = "toPolicy";

    Policy policy = this.roleFactory.createPolicy(entity.name());
    transform(entity, policy);

    final List<ApplicationEntity> lae = entity.application();
    final List<Policy.Resource>   lpr = policy.getResource();
    for (ApplicationEntity application : lae) {
      final ApplicationInstance instance = populateInstance(application.name());
      if (instance == null) {
        result.setStatus(Status.ERROR);
        recordMessage(result, Severity.ERROR, method, HarvesterBundle.format(HarvesterError.INSTANCE_NOTFOUND, application.name()));
      }
      else {
        final Set<String>                        san      = application.namespace();
        final Policy.Resource                    resource = this.roleFactory.createPolicyResource(instance.getObjectName(), instance.getItResourceName());
        final List<Policy.Resource.Entitlements> namelist = resource.getEntitlements();
        for (String namespace : san) {
          final Attribute attribute = lookupAttribute(instance, namespace);
          if (attribute != null) {
            final List<EntitlementEntity>                        see        = application.entitlement(namespace);
            final Policy.Resource.Entitlements                   container  = this.roleFactory.createPolicyResourceEntitlements(attribute.name);
            final List<Policy.Resource.Entitlements.Entitlement> entitylist = container.getEntitlement();
            for (EntitlementEntity entitlement : see) {
              String value = entitlement.name();
              if (booleanValue(ENTITLEMENT_PREFIX)) {
                final String[] pattern     = this.binding.get(instance.getItResourceKey());
                final long     resourceKey = instance.getItResourceKey();

                if (pattern == null || pattern.length == 0) {
                  value = String.format(HarvesterDescriptor.DEFAULT_PATTERN_ENCODED, resourceKey, value);
                }
                else {
                  value = String.format(pattern[1], resourceKey, pattern[0], value);
                }
              }
              // check existance of the entitlement value
              if (existsEntitlement(namespace, value)) {
                Policy.Resource.Entitlements.Entitlement ent = this.roleFactory.createPolicyResourceEntitlementsEntitlement(value);
                entitylist.add(ent);
              }
              else {
                result.setStatus(Status.ERROR);
                recordMessage(result, Severity.ERROR, method, HarvesterBundle.format(HarvesterError.ENTITLEMENT_NOTFOUND, entitlement.name(), namespace));
              }
            }
            // verify if all entitlements are transformed
            if (entitylist.size() == see.size())
              namelist.add(container);
            else {
              result.setStatus(Status.ERROR);
              recordMessage(result, Severity.ERROR, method, HarvesterBundle.string(HarvesterError.REQUEST_ENTITLEMENT_MISSED));
            }
          }
        }
        // verify if all namespaces are transformed
        if (namelist.size() == san.size())
          lpr.add(resource);
        else {
          result.setStatus(Status.ERROR);
          recordMessage(result, Severity.ERROR, method, HarvesterBundle.string(HarvesterError.REQUEST_NAMESPACE_MISSED));
        }
      }
    }
    // verify if all applications are transformed
    if (lpr.size() != lae.size()) {
      result.setStatus(Status.ERROR);
      recordMessage(result, Severity.ERROR, method, HarvesterBundle.string(HarvesterError.REQUEST_APPLICATION_MISSED));
      policy = null;
    }
    return policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  toRole
  /**
   ** Factory method to transform a {@link BusinessRoleEntity} to a
   ** {@link Role}.
   **
   ** @param  entity             the {@link BusinessRoleEntity} to transform
   **
   ** @return                    the {@link Role} transformed so far.
   **
   ** @throws TaskException      if data processing fails.
   */
  private final Role toRole(final BusinessRoleEntity entity)
    throws TaskException {

    final Role role = this.roleFactory.createRole(entity.name());
    transform(entity, role);
    return role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  transform
  /**
   ** Factory method to transform a {@link BusinessRoleEntity} to a
   ** {@link Role}.
   **
   ** @param  entity             the {@link BusinessRoleEntity} to transform
   **
   ** @return                    the {@link Role} transformed so far.
   **
   ** @throws TaskException      if data processing fails.
   */
  private final void transform(final BusinessRoleEntity entity, final Role role)
    throws TaskException {

    final String              format  = ((HarvesterDescriptor)this.descriptor).dateFormat();
    // set the attributes defined on the business role as properties on the
    // transformation result
    switch (entity.risk()) {
      case high   : role.setRisk(Role.RISK_LEVEL_HIGH);
                    break;
      case medium : role.setRisk(Role.RISK_LEVEL_MEDIUM);
                    break;
      case low    :
      default     : role.setRisk(Role.RISK_LEVEL_LOW);
    }
    final Map<String, Object> mapping = this.descriptor.attributeMapping().filterByEncoded(entity.attribute(), false);
    for (Map.Entry<String, Object> entry : mapping.entrySet()) {
      final String name  = entry.getKey();
      final String value = (String)entry.getValue();
      if (name.equalsIgnoreCase(Proxy.ATTRIBUTE_DISPLAYNAME))
        role.setDisplayName(value);
      else if (name.equalsIgnoreCase(Proxy.ATTRIBUTE_DESCRIPTION))
        role.setDescription(value);
      else if (name.equalsIgnoreCase(Proxy.ATTRIBUTE_COMMENT))
        role.setComment(value);
      else if (name.equalsIgnoreCase(Proxy.ATTRIBUTE_DEPARTMENT))
        role.setDepartment(value);
      else if (name.equalsIgnoreCase(Proxy.ATTRIBUTE_JOBCODE))
        role.setJobCode(value);
      else if (name.equalsIgnoreCase(Proxy.ATTRIBUTE_TYPE))
        role.setType(value);
      else if (name.equalsIgnoreCase(Proxy.ATTRIBUTE_STARTDATE))
        try {
          final Date date = DateUtility.getDate(DateUtility.parseDate(value, format));
          role.setStartDate(DateUtility.toXMLGregorianCalendar(date));
        }
        catch (ParseException e) {
          role.setStartDate((Date)null);
          fatal("transform", e);
        }
      else if (name.equalsIgnoreCase(Proxy.ATTRIBUTE_ENDDATE))
        try {
          final Date date = DateUtility.getDate(DateUtility.parseDate(value, format));
          role.setEndDate(DateUtility.toXMLGregorianCalendar(date));
        }
        catch (ParseException e) {
          role.setEndDate((Date)null);
          fatal("transform", e);
        }
      else if (name.startsWith(Proxy.ELEMENT_PROPERTY.toUpperCase())) {
        role.getCustomProperty().add(value);
      }
      else if (name.equalsIgnoreCase(Proxy.ELEMENT_OWNERSHIP)) {
        role.getOwnerShip().add(value);
        /* FixME bring it to work
        if (!StringUtility.isEmpty(value)) {
          for (String cursor : value.split("\\|"))
            role.getOwnerShip().add(cursor);
        }
        */
      }
      else if (name.equalsIgnoreCase(Proxy.ELEMENT_BUSINESSUNIT)) {
        role.getBusinessUnit().add(value);
        /* FixME bring it to work
        if (!StringUtility.isEmpty(value)) {
          for (String cursor : value.split("\\|"))
            role.getBusinessUnit().add(cursor);
        }
        */
      }
    }
    // verify if all mandatory attributes like display name are set
    if (StringUtility.isEmpty(role.getDisplayName()))
      role.setDisplayName(entity.name());
    if (StringUtility.isEmpty(role.getDescription()))
      role.setDescription(entity.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  lookupAttribute
  /**
   ** Lookup of the attribute the of a process form tagged as an entitlement and
   ** joined with the specified <code>Lookup Definition</code> as the entitlement
   ** namespace.
   ** <p>
   ** We assume that a <code>Lookup Definition</code> joined with a entitlement
   ** process form is used only once.
   ** <pre>
   ** SELECT sdc.sdc_name as name
   ** ,      sdc.sdc_label as label
   ** ,      sdc.sdc_field_type as type
   ** FROM   sdc, sdp
   ** WHERE  sdc.sdk_key            = ?
   ** AND    sdc.sdc_key            = sdp.sdc_key
   ** AND    sdp.sdp_property_name  = ?
   ** AND    sdp.sdp_property_value = ?
   ** </pre>
   **
   ** @param  instance           the {@link ApplicationInstance} with child
   **                            forms belonging to an account form which might
   **                            have a <code>Lookup Definition</code> joined.
   ** @param  namespace          the namespace of the
   **                            <code>Lookup Definition</code> potentionally
   **                            joined with a process form as an entitlement.
   **
   ** @return                    the name of the process form attribute
   */
  private final Attribute lookupAttribute(final ApplicationInstance instance, final String namespace)
    throws HarvesterException {

    final String method = "lookupAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);

    Connection           connection = null;
    PreparedStatement    statement  = null;
    ResultSet            resultSet  = null;
    Attribute            attribute  = null;
    final List<FormInfo> form       = instance.getChildForms();
    try {
      connection = DatabaseConnection.aquire();
      statement  = DatabaseStatement.createPreparedStatement(connection, LOOKUP_ATTRIBUTE);
      statement.setString(2, PROPERTY_LOOKUP_CODE);
      statement.setString(3, namespace);
      for (FormInfo cursor : form) {
        statement.setLong(1, cursor.getFormKey());
        resultSet  = statement.executeQuery();
        if (resultSet.next()) {
          // dependent on the iteration we have not an attribute discovered so
          // far hance we are safe for the moment
          if (attribute == null)
            attribute = new Attribute(resultSet.getString(PROPERTY_COLUMN_NAME), resultSet.getString(PROPERTY_COLUMN_LABEL), resultSet.getString(PROPERTY_COLUMN_TYPE));
          // if we have already an attribute discovered than we are ambigous to
          // decide which of them has to be used
          // on the other hand it is really a stpid design to use the same
          // lookup for multiple chhild forms on the same account form
          else {
            final String[] arguments = { namespace, instance.getApplicationInstanceName() };
            throw new HarvesterException(HarvesterError.NAMEPACE_AMBIGUOUS, arguments);
          }
        }
      }
      if (attribute == null) {
        final String[] arguments = { namespace, instance.getApplicationInstanceName() };
        throw new HarvesterException(HarvesterError.NAMEPACE_NOTFOUND, arguments);
      }
    }
    catch (DatabaseException e) {
      throw new HarvesterException(e);
    }
    catch (SQLException e) {
      throw new HarvesterException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  recordMessage
  /**
   ** Factory method to create a {@link Result.Message} that will be spooled to
   ** the response file.
   **
   ** @param  result             the {@link Result} recorder to receive the
   **                            created record.
   ** @param  severity           the {@link Severity} of the record to log.
   ** @param  location           the location context the event belongs to.
   ** @param  text               the string message to log.
   */
  private void recordMessage(final Result result, final Severity severity, final String location, final String text) {
    final Result.Message record = this.baseFactory.createResultMessage(severity, this.lastReconciled(), HarvesterBundle.location(this.prefix, location), text);
    result.getMessage().add(record);
  }
}