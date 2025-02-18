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

    File        :   Harvester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Harvester.


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
import java.util.HashSet;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;

import oracle.iam.identity.rolemgmt.vo.Role;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;

import oracle.iam.request.vo.ApprovalConstants;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.xml.XMLFormat;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AttributeTransformation;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ITResource;
import oracle.iam.identity.foundation.naming.ITResourceType;
import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.foundation.persistence.DatabaseException;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.utility.file.XMLEntityFactory;

import oracle.iam.identity.ots.service.Controller;

////////////////////////////////////////////////////////////////////////////////
// abstract class Harvester
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** The <code>Harvester</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Scheduler which handles data
 ** provided by a XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
abstract class Harvester extends Controller {

  /**
   ** Attribute tag which must be defined on a scheduled task to specify the
   ** fullqualified name of the Java Class (<code>EntityFactory</code>) governs
   ** the process of serializing Java content trees into XML data into,
   ** optionally validating the XML data as it is marshalled.
   */
  protected static final String MARSHALLER      = "Marshaller Implementation";

  /**
   ** Attribute tag which must be defined on a scheduled task to specify the
   ** fullqualified name of the Java Class (<code>EntityFactory</code>) governs
   ** the process of deserializing XML data into newly created Java content
   ** trees, optionally validating the XML data as it is unmarshalled.
   */
  protected static final String UNMARSHALLER    = "Unmarshaller Implementation";

  /**
   ** Attribute tag which must be defined on a scheduled task to specify if the
   ** entire file has to be validate against the XML schema before it will be
   ** unmarshalled.
   */
  protected static final String VALIDATE_SCHEMA = "Validate Import";

  /** the category of the logging facility to use */
  private static final String   LOGGER_CATEGORY = "OCS.ARC.HARVESTER";

  /**
   ** The SQL statement string to lookup an <code>Application Instance</code>
   ** by the names of a <code>Resource Object</code> and an
   ** <code>IT Resource</code>.
   */
  private static final String   LOOKUP_INSTANCE = "select app_instance_name from app_instance where app_instance.itresource_key = ? and app_instance.object_key = ?";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the binding to resolve the entitlement prefix for specific types of
   ** <code>IT Resource</code>s
   */
  protected final Map<Long, String[]> binding   = new HashMap<Long, String[]>();

  /** the format specification constant over all requests*/
  protected XMLFormat                 format    = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Harvester</code> with the specified data
   ** direction.
   **
   ** @param  inbound            <code>true</code> if this
   **                            <code>Harvester</code> is importing catalog
   **                            data; otherwise <code>false</code>.
   */
  protected Harvester(final boolean inbound) {
    // ensure inheritance
    super(LOGGER_CATEGORY);

    // initialze instance attribtes
    this.descriptor = new HarvesterDescriptor(this, inbound);
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

    // perfom the instance specific configuration
    configureDescriptor();

    this.format = new XMLFormat(String.format(XMLEntityFactory.PROLOG, stringValue(FILE_ENCODING)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureDescriptor
  /**
   ** Configure the descriptor capabilities.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void configureDescriptor()
    throws TaskException {

    final String method = "configureDescriptor";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final String path = stringValue(RECONCILE_DESCRIPTOR);
    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session  = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      if (document == null)
        throw new TaskException(TaskError.METADATA_OBJECT_NOTFOUND, path);

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      HarvesterDescriptorFactory.configure((HarvesterDescriptor)this.descriptor, document);
      // prevent misconfiguration of mapping descriptor
      if (this.descriptor.attributeMapping().isEmpty())
        throw new TaskException(TaskError.ATTRIBUTE_MAPPING_EMPTY);

      // validate the descriptor definitions
      validateDescripor();

      // produce the logging output only if the logging level is enabled for
      if (this.logger != null && this.logger.debugLevel())
        debug(method, this.descriptor.toString());

      final tcITResourceDefinitionOperationsIntf definition = service(tcITResourceDefinitionOperationsIntf.class);
      final tcITResourceInstanceOperationsIntf   instance   = service(tcITResourceInstanceOperationsIntf.class);
      final Map<String, String>                  filter     = new HashMap<String, String>();
      final Map<String, String[]>                type       = ((HarvesterDescriptor)this.descriptor).type();
      for (Map.Entry<String, String[]> entry : type.entrySet()) {
        final String[] pattern = entry.getValue();
        filter.clear();
        filter.put(ITResourceType.NAME, entry.getKey());
        tcResultSet resultSet = definition.getITResourceDefinition(filter);
        int         count     = resultSet.getRowCount();
        // if a IT Resource Type is mapped which returns multiple entries then
        // we have a either a wildcard in the specified IT Resource Type mapping
        // or a corrupted repository; regardless what the reason will be we have
        // no chance to fix it hence stop any further processing
        if (count > 1) {
          throw new TaskException(TaskError.INSTANCE_AMBIGUOUS, entry.getKey());
        }
        // if a IT Resource Type is mapped which is currently not configured we
        // will ignore this because we can strongly assume that no IT Resource
        // belongs to such type (expecting that the dependency errors are solved
        // at deployment time)
        else if (count == 1) {
          resultSet.goToRow(0);
          resultSet = instance.findITResourceInstancesByDefinition(resultSet.getLongValue(ITResourceType.KEY));
          count     = resultSet.getRowCount();
          for (int i = 0; i < count; i++) {
            resultSet.goToRow(i);
            final String[] argument = new String[pattern.length];
            System.arraycopy(pattern, 0, argument, 0, pattern.length);
            final long instanceKey = resultSet.getLongValue(ITResource.KEY);
            // check if we have a name for the IT Resource parameter; if not per
            // contract we will use the name of the IT Resource instead
            if (StringUtility.isEmpty(argument[0])) {
              argument[0] = resultSet.getStringValue(ITResource.NAME);
            }
            else {
              final tcResultSet parameter = instance.getITResourceInstanceParameters(instanceKey);
              // get the name of the parameter from the IT Resource definition
              for (int j = 0; j < parameter.getRowCount(); j++) {
                parameter.goToRow(j);
                final String parameterName = parameter.getStringValue(ITResource.PARAMETER_NAME);
                if (argument[0].equals(parameterName)) {
                  argument[0] = parameter.getStringValue(ITResource.PARAMETER_VALUE);
                  break;
                }
              }
            }
            this.binding.put(new Long(instanceKey), argument);
          }
        }
      }
    }
    catch (Exception e) {
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
  protected void validateDescripor()
    throws HarvesterException {

    final String method  = "validateDescripor";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final AttributeMapping        attribute      = this.descriptor.attributeMapping();
    final AttributeTransformation transformation = this.descriptor.transformationMapping();

    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    try {
      connection = DatabaseConnection.aquire();
      statement  = DatabaseStatement.createPreparedStatement(connection, "SELECT 'x' FROM dual WHERE EXISTS (SELECT 'x' FROM catalog_metadata_definition where db_column_name = ?)");
      for (String name : attribute.keySet()) {
        if (((HarvesterDescriptor)this.descriptor).inbound())
          name = (String)attribute.get(name);
        statement.setString(1, name);
        resultSet = statement.executeQuery();
        if (!resultSet.next()) {
          attribute.remove(name);
          transformation.remove(name);
        }
      }
    }
    catch (SQLException e) {
      throw new HarvesterException(e);
    }
    catch (DatabaseException e) {
      throw new HarvesterException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateRole
  /**
   ** Lookups a {@link Role} from Oracle Identity Manager by its internal
   ** identifier.
   ** <p>
   ** The {@link Role} returned has only the name, display name and description
   ** beside the entity key as attributes filled.
   **
   ** @param  identifier         the internal system identifier of a
   **                            {@link Role} to lookup.
   **
   ** @return                    the {@link Role} that match the specified key
   **                            or <code>null</code> if no {@link Role} match
   **                            the specified name.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected Role populateRole(final String identifier)
    throws TaskException {

    final String method = "populateRole";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Set<String> returning = new HashSet<String>();
    returning.add(RoleManagerConstants.ROLE_DISPLAY_NAME);
    returning.add(RoleManagerConstants.ROLE_DESCRIPTION);

    final RoleManager facade = service(RoleManager.class);

    Role role = null;
    try {
      role = facade.getDetails(RoleManagerConstants.ROLE_NAME, identifier, returning);
    }
    // in case the role with the name is defined ambiguous in Oracle Identity
    // Manager
    catch (SearchKeyNotUniqueException e) {
      final String[] arguments = {RoleManagerConstants.ROLE_ENTITY_NAME, RoleManagerConstants.ROLE_KEY, identifier.toString()};
      throw new TaskException(TaskError.ENTITY_AMBIGUOUS, arguments);
    }
    // in case the role with the name does not exists in Oracle Identity Manager
    catch (NoSuchRoleException e) {
      final String[] arguments = {RoleManagerConstants.ROLE_ENTITY_NAME, RoleManagerConstants.ROLE_KEY, identifier.toString()};
      error(method, TaskBundle.format(TaskError.ENTITY_NOT_FOUND, arguments));
    }
    // in case a wrong attribute name is passed as a returning value to the
    // search
    catch (RoleLookupException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateInstance
  /**
   ** Lookups an {@link ApplicationInstance} from Oracle Identity Manager.
   **
   ** @param  applicationName    the name of an {@link ApplicationInstance} to
   **                            lookup.
   **
   ** @return                    the {@link ApplicationInstance} that match the
   **                            specified name or <code>null</code> if no
   **                            {@link ApplicationInstance} match the specified
   **                            name.
   **
   ** @throws HarvesterException in case an error does occur.
   */
  protected ApplicationInstance populateInstance(final String applicationName)
    throws HarvesterException {

    final String method = "populateInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    final ApplicationInstanceService facade = service(ApplicationInstanceService.class);
    ApplicationInstance instance = null;
    try {
      instance = facade.findApplicationInstanceByName(applicationName);
    }
    // in case the application with the name does not exists in Oracle Identity
    // Manager
    catch (ApplicationInstanceNotFoundException e) {
      final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, ApplicationInstance.APPINST_NAME, applicationName};
      error(method, TaskBundle.format(TaskError.ENTITY_NOT_FOUND, arguments));
    }
    // in case a wrong attribute name is passed as a returning value to the
    // search
    catch (GenericAppInstanceServiceException e) {
      throw new HarvesterException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateInstance
  /**
   ** Lookups an {@link ApplicationInstance} from Oracle Identity Manager.
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
   ** @param  resourceKey        the internal identifier of a
   **                            <code>IT Resource</code> the desired
   **                            {@link ApplicationInstance} belongs to.
   **
   ** @return                    the {@link ApplicationInstance} that match the
   **                            specified name or <code>null</code> if no
   **                            {@link ApplicationInstance} match the specified
   **                            name.
   **
   ** @throws HarvesterException in case an error does occur.
   */
  protected String populateInstance(final long objectKey, final long resourceKey)
    throws HarvesterException {

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
      throw new HarvesterException(e);
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
  // Method:   lookupObject
  /**
   ** Lookups an {@link ApplicationInstance} from Oracle Identity Manager.
   **
   ** @param  objectName         the name of a <code>Resource Object</code> to
   **                            lookup.
   **
   ** @return                    the internal identifier of the desired
   **                            <code>Resource Object</code>.
   **
   ** @throws HarvesterException in case an error does occur.
   */
  protected long lookupObject(final String objectName)
    throws HarvesterException {

    final String method = "lookupObject";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ResourceObject.NAME, objectName);
    try {
      final tcObjectOperationsIntf facade    = service(tcObjectOperationsIntf.class);
      final tcResultSet            resultSet = facade.findObjects(filter);
      int count = resultSet.getRowCount();
      if (count == 0) {
        // if a Resource Object is not mapped we have no chance to fix it hence
        // stop any further processing
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, objectName);
      }
      else if (count > 1) {
        // if a Resource Resource is mapped which returns multiple entries then
        // we have a either a wildcard in the specified IT Resource name or a
        // corrupted repository; regardless what the reason will be we have no
        // chance to fix it hence stop any further processing
        throw new TaskException(TaskError.RESOURCE_AMBIGUOUS, objectName);
      }
      return resultSet.getLongValue(ResourceObject.KEY);
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
  // Method:   lookupResource
  /**
   ** Lookups an {@link ApplicationInstance} from Oracle Identity Manager.
   **
   ** @param  resourceName       the name of a <code>IT Resource</code> to
   **                            lookup.
   **
   ** @return                    the internal identifier of the desired
   **                            <code>IT Resource</code>.
   **
   ** @throws HarvesterException in case an error does occur.
   */
  protected long lookupResource(final String resourceName)
    throws HarvesterException {

    final String method = "lookupResource";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ITResource.NAME, resourceName);
    try {
      final tcITResourceInstanceOperationsIntf facade    = service(tcITResourceInstanceOperationsIntf.class);
      final tcResultSet                        resultSet = facade.findITResourceInstances(filter);
      int count = resultSet.getRowCount();
      if (count == 0) {
        // if an IT Resource is not mapped we have no chance to fix it hence
        // stop any further processing
        throw new TaskException(TaskError.ITRESOURCE_NOT_FOUND, resourceName);
      }
      else if (count > 1) {
        // if an IT Resource is mapped which returns multiple entries then we
        // have a either a wildcard in the specified IT Resource name or a
        // corrupted repository; regardless what the reason will be we have no
        // chance to fix it hence stop any further processing
        throw new TaskException(TaskError.ITRESOURCE_AMBIGUOUS, resourceName);
      }
      return resultSet.getLongValue(ITResource.KEY);
    }
    catch (Exception e) {
      throw new HarvesterException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}