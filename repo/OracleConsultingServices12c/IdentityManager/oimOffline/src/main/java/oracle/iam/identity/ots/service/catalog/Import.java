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

    File        :   Import.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Import.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2016-03-04  DSteding    Fixed DE-000159
                                         Catalog Item cannot be managed by
                                         Access Request Catalog Import if it
                                         was soft-deleted previuosly.
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import oracle.iam.platform.utils.vo.OIMType;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.catalog.vo.Result;
import oracle.iam.catalog.vo.Catalog;
import oracle.iam.catalog.vo.MetaData;
import oracle.iam.catalog.vo.CatalogConstants;
import oracle.iam.catalog.vo.MetaDataDefinition;

import oracle.iam.catalog.util.Utility;

import oracle.iam.catalog.api.CatalogService;

import oracle.iam.catalog.exception.CatalogException;

import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleManagerResult;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.provisioning.vo.Entitlement;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.provisioning.exception.GenericEntitlementServiceException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.RoleEntity;
import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.CatalogEntity;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.ApplicationEntity;

import oracle.iam.identity.foundation.persistence.DatabaseConnection;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

import oracle.iam.identity.ots.resource.HarvesterBundle;

///////////////////////////////////////////////////////////////////////////////
// class Import
// ~~~~~ ~~~~~~
/**
 ** The <code>Import</code> class implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** catalog data provided by XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class Import extends CatalogHarvester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on a <code>Scheduled Task</code>
   ** to specify if the entitlement loaded from a file needs to be prefixed with
   ** the internal system identifier and/or the name of the
   ** <code>IT Resource</code>.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          ENTITLEMENT_PREFIX = "Entitlement Prefix Required";

  /**
   ** the array with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute          = {
    /** the task attribute with reconciliation object */
    TaskAttribute.build(RECONCILE_OBJECT,     "Catalog")
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

  /** the factory to unmarshal the XML file */
  private EntityFactory<CatalogEntity> factory    = null;

  /** the database connection to lookup catalog items */
  private Connection                   connection = null;

  /** the database statement used to lookup catalog items */
  private PreparedStatement            statement  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Import</code> scheduled job that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Import() {
    // ensure inheritance
    super(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process (EntityListener)
  /**
   ** Reconciles a particular bulk of {@link CatalogEntity}s.
   **
   ** @param  bulk               the {@link Collection} of {@link CatalogEntity}
   **                            to synchronize with the catalog.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void process(final Collection<CatalogEntity> bulk)
    throws TaskException {

    final String method = "process";
    trace(method, SystemMessage.METHOD_ENTRY);

    // notify about the effort to do
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(bulk.size())));
    for (CatalogEntity catalog : bulk) {
      if (isStopped())
        break;
      final List<RoleEntity> role = catalog.role();
      if (!CollectionUtility.empty(role))
        try {
          processRole(role);
        }
        catch (TaskException e) {
          continue;
        }

      final List<ApplicationEntity> instance = catalog.application();
      if (!CollectionUtility.empty(instance))
        try {
          processInstance(instance);
        }
        catch (TaskException e) {
          continue;
        }
    }
    info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processRole (CatalogListener)
  /**
   ** Reconciles a particular batch of {@link RoleEntity}'s.
   **
   ** @param  bulk               the {@link Collection} of {@link RoleEntity}
   **                            to synchronize with the catalog.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void processRole(final Collection<RoleEntity> bulk)
    throws TaskException {

    final String method = "processRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // skip any further processing if the entity type is excluded in general
    if (!booleanValue(PROCESS_ROLE)) {
      warning(method, HarvesterBundle.string(HarvesterMessage.IMPORT_SKIP));
      incrementIgnored(bulk.size());
    }
    else {
      for (RoleEntity entity : bulk) {
        final Role role = populateRole(entity.name());
        if (role == null)
          incrementFailed();
        else {
          // ensure the correct value mapping of the identifier to enable
          // transformation and anything else on this attribute
          entity.attribute().put(EntityFactory.ATTRIBUTE_ID, entity.name());
          // apply standard attribute mapping and transformation but keep in
          // mind we don't reconcile accounts hence set the account flag to
          // false
          final Map<String, Object> data = transformMaster(createMaster(entity.attribute(), false));
          // remove the role key itself from the attribute mapping of a role it
          // was accidentely retrieved by the lookup operation executed above
          role.getAttributes().remove(RoleManagerConstants.ROLE_KEY);
          modifyRole(role, data);
          // we need to update the catalog per item and unfortunetaly not in
          // bulk because each item can have a diffrent set of metadata
          // attributes to update hence the algorithm implemented in the kernel
          // will fail if are not each item to be updated provides the same
          // metadata
          final Catalog item = createCatalog(role.getEntityId(), OIMType.Role, entity.risk(), entity.action(), data);
          if (item != null) {
            if (item.getMetadata().size() > 0)
              modifyCatalog(item);
            else
              this.catalog.incrementFailed();
           }
           else
             this.catalog.incrementFailed();
        }
      }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processInstance (CatalogListener)
  /**
   ** Reconciles a particular batch of {@link ApplicationEntity}'s.
   **
   ** @param  batch              the {@link Collection} of
   **                            {@link ApplicationEntity}'s to synchronize with
   **                            the catalog.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void processInstance(final Collection<ApplicationEntity> batch)
    throws TaskException {

    final String method = "processInstance";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // skip any further processing if the entity type is excluded in general
    if (!booleanValue(PROCESS_APPLICATION)) {
      warning(method, HarvesterBundle.string(HarvesterMessage.IMPORT_SKIP));
      incrementIgnored(batch.size());
    }
    else {
      for (ApplicationEntity entity : batch) {
        ApplicationInstance instance = populateInstance(entity.name());
        if (instance == null)
          incrementFailed();
        else {
          // ensure the correct value mapping of the identifier to enable
          // transformation and anything else on this attribute
          entity.attribute().put(EntityFactory.ATTRIBUTE_ID, entity.name());
          // apply standard attribute mapping and transformation but keep in
          // mind we don't reconcile accounts hence set the account flag to
          // false
          final Map<String, Object> data = transformMaster(createMaster(entity.attribute(), false));
          modifyInstance(instance, data);
          // we need to update the catalog per item and unfortunetaly not in
          // bulk because each item can have a diffrent set of metadata
          // attributes to update hence the algorithm implemented in the kernel
          // will fail if are not each item to be updated provides the same
          // metadata
          final Catalog item = createCatalog(String.valueOf(instance.getApplicationInstanceKey()), OIMType.ApplicationInstance, entity.risk(), entity.action(), data);
          if (item != null) {
            if (item.getMetadata().size() > 0)
              modifyCatalog(item);
            else
              this.catalog.incrementFailed();
           }
           else
             this.catalog.incrementFailed();
        }
      }
    }

    // should never occur but to be safe process all entitlement namespaces
    // assigned so for to the applications
    for (ApplicationEntity entity : batch) {
      // how to handle the entitlements
      for (String namespace : entity.namespace())
        processEntitlement(entity, namespace);
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processEntitlement (CatalogListener)
  /**
   ** Reconciles a particular batch of {@link EntitlementEntity}'s.
   **
   ** @param  application        the {{@link ApplicationEntity} the specified
   **                            {@link Set} of {@link EntitlementEntity}'s
   **                            belongs to.
   ** @param  namespace          the namespace the {@link EntitlementEntity}'s
   **                            belongs to.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void processEntitlement(final ApplicationEntity application, final String namespace)
    throws TaskException {

    final Collection<EntitlementEntity> batch = application.entitlement(namespace);
    if (batch == null || batch.size() == 0)
      return;

    final String method = "processEntitlement";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // skip any further processing if the entity type is excluded in general
    if (!booleanValue(PROCESS_ENTITELEMENT)) {
      warning(method, HarvesterBundle.string(HarvesterMessage.IMPORT_SKIP));
      incrementIgnored(batch.size());
    }
    else {
      final ApplicationInstance instance = populateInstance(application.name());
      if (instance == null)
        incrementFailed(batch.size());
      else {
        final String[]                pattern  = this.binding.get(instance.getItResourceKey());
        final long                    resource = instance.getItResourceKey();
        final EntitlementService      facade   = service(EntitlementService.class);
        final HashMap<String, Object> noattr   = new HashMap<String, Object>();
        final SearchCriteria          criteria = new SearchCriteria(
          new SearchCriteria(Entitlement.ENTITLEMENT_OBJECTKEY,     instance.getObjectKey(),     SearchCriteria.Operator.EQUAL)
        , new SearchCriteria(Entitlement.ENTITLEMENT_ITRESOURCEKEY, instance.getItResourceKey(), SearchCriteria.Operator.EQUAL)
        , SearchCriteria.Operator.AND
        );

        for (EntitlementEntity entity : batch) {
          // transform the name of the entitlement to the stupid prefixed naming
          // convention
          String entitlementName = entity.name();
          if (booleanValue(ENTITLEMENT_PREFIX)) {
            if (pattern == null || pattern.length == 0)
              entitlementName = String.format(HarvesterDescriptor.DEFAULT_PATTERN_ENCODED, resource, entitlementName);
            else
              entitlementName = String.format(pattern[1], resource, pattern[0], entitlementName);
          }

          final SearchCriteria filter = new SearchCriteria(
            criteria
          , new SearchCriteria(Entitlement.ENTITLEMENT_NAME, entitlementName, SearchCriteria.Operator.EQUAL)
          , SearchCriteria.Operator.AND
          );
          try {
            final List<Entitlement> result = facade.findEntitlements(filter, noattr);
            if (result.size() == 0) {
              incrementFailed();
              error(method, HarvesterBundle.format(HarvesterError.ENTITLEMENT_NOTFOUND, entitlementName, namespace));
            }
            else if (result.size() > 1) {
              incrementFailed();
              error(method, HarvesterBundle.format(HarvesterError.ENTITLEMENT_AMBIGUOUS, entitlementName, namespace));
            }
            else {
              // ensure the correct value mapping of the identifier to enable
              // transformation and anything else on this attribute
              entity.attribute().put(EntityFactory.ATTRIBUTE_ID, entitlementName);
              // apply standard attribute mapping and transformation but keep in
              // mind we don't reconcile accounts hence set the account flag to
              // false
              final Map<String, Object> data = transformMaster(createMaster(entity.attribute(), false));
              final Entitlement entitlement = result.get(0);
              modifyEntitlement(entitlement, data);
              // we need to update the catalog per item and unfortunetaly not in
              // bulk because each item can have a diffrent set of metadata
              // attributes to update hence the algorithm implemented in the
              // kernel will fail if are not each item to be updated provides
              // the same metadata
              final Catalog item = createCatalog(String.valueOf(entitlement.getEntitlementKey()), OIMType.Entitlement, entity.risk(), entity.action(), data);
              if (item != null) {
                if (item.getMetadata().size() > 0)
                  modifyCatalog(item);
                else
                  this.catalog.incrementFailed();
              }
              else
                this.catalog.incrementFailed();
            }
          }
          catch (GenericEntitlementServiceException e) {
            incrementFailed();
            throw new HarvesterException(e);
          }
        }
      }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
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

    final String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
    info(HarvesterBundle.format(HarvesterMessage.IMPORTING_BEGIN, parameter));
    if (this.dataFileAvailable(lastReconciled())) {
      info(HarvesterBundle.format(HarvesterMessage.IMPORT_BEGIN, parameter));
      try {
        this.connection = DatabaseConnection.aquire();
        this.statement  = DatabaseStatement.createPreparedStatement(this.connection, "SELECT * FROM catalog WHERE entity_key = ? and entity_type = ?");

        this.factory.populate(this, this.dataFile(), booleanValue(VALIDATE_SCHEMA));

        this.updateLastReconciled();
        info(HarvesterBundle.format(HarvesterMessage.IMPORT_COMPLETE, parameter));
        info(HarvesterBundle.format(HarvesterMessage.IMPORT_CATALOG_SUMMARY, this.catalog.asStringArray()));
      }
      catch (TaskException e) {
        // copy the data file to the error folder
        // we let the new file where it is, so the next time the scheduled
        // task is running we have the same file or a new one
        try {
          FileSystem.copy(dataFile(), errorFile());
        }
        catch (SystemException ex) {
          warning(ex.getLocalizedMessage());
        }
        final String[] arguments = { reconcileObject(), getName() , dataFile().getAbsolutePath() , e.getLocalizedMessage()};
        // notify user about the problem
        warning(HarvesterBundle.format(HarvesterMessage.IMPORTING_STOPPED, arguments));
        throw e;
      }
      finally {
        DatabaseStatement.closeStatement(this.statement);
        DatabaseConnection.release(this.connection);
      }
    }
    info(HarvesterBundle.format(HarvesterMessage.IMPORTING_COMPLETE, parameter));
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

    final String className = stringValue(UNMARSHALLER);
    try {
      // a little bit reflection
      final Class<?> clazz = Class.forName(className);
      this.factory = (EntityFactory<CatalogEntity>)clazz.newInstance();
      this.factory.uniqueName(EntityFactory.Unique.fromValue(((HarvesterDescriptor)this.descriptor).uniqueName()));
    }
    catch (ClassNotFoundException e) {
      throw TaskException.classNotFound(className);
    }
    catch (InstantiationException e) {
      throw TaskException.classNotCreated(className);
    }
    catch (IllegalAccessException e) {
      throw TaskException.classNoAccess(className);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRole
  /**
   ** Modifies a {@link Role} item with specific data of the specified
   ** attribute mapping.
   **
   ** @param  role               the {@link Role} providing the actual data
   **                            of the role to modify.
   ** @param  data               the attribute mapping taken from an
   **                            {@link EntitlementEntity} providing the data to
   **                            modify the {@link Role} <code>role</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private void modifyRole(final Role role, final Map<String, Object> data)
    throws TaskException {

    final String method = "modifyRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // skip processing of the particular entity if requested
    if (booleanValue(GATHERONLY)) {
      warning(method, HarvesterBundle.string(HarvesterMessage.IMPORT_SKIP));
      incrementIgnored();
    }
    else {
      boolean changed = false;
      // we don't have to take care about of specific infomation of the catalog
      // item like decription
      // any change on such attributes are updating the catalog immediatly by the
      // event handlers deployed on the system
      // for the time being we will update the display name and the description
      // on the role to propagete those changes to the catalog before the
      // catalog item itself is updated
      try {
        String sourceValue = null;
        String targetValue = null;
        if (data.containsKey(CatalogConstants.CATALOG_ENTITY_DISPLAY_NAME)) {
          sourceValue =(String)data.remove(CatalogConstants.CATALOG_ENTITY_DISPLAY_NAME);
          targetValue = role.getDisplayName();
          if (targetValue != null && sourceValue != null && !targetValue.equals(sourceValue)) {
            role.setDisplayName(sourceValue);
            changed = true;
          }
        }
        if (data.containsKey(CatalogConstants.CATALOG_ENTITY_DESCRIPTION)) {
          sourceValue =(String)data.remove(CatalogConstants.CATALOG_ENTITY_DESCRIPTION);
          // special case the role description can only have 1024 characters
          // hence if the length of the source value exceeds this capacity we
          // need to truncate the source value and put it back to the data so
          // that the catalog will be update the description properly
          if (sourceValue != null && sourceValue.length() > 1024) {
            sourceValue = sourceValue.substring(0, 1024);
          }
          targetValue = role.getDescription();
          if (targetValue != null && sourceValue != null && !targetValue.equals(sourceValue)) {
            role.setDescription(sourceValue);
            changed = true;
          }
        }

        if (changed) {
          final RoleManager       facade = service(RoleManager.class);
          final RoleManagerResult result = facade.modify(role);
          if (result.getFailedResults().size() > 0) {
            incrementFailed(result.getFailedResults().size());
            error(method, HarvesterBundle.format(HarvesterError.MODIFY_ROLE, role.getDisplayName()));
          }
          else {
            incrementSuccess();
          }
        }
        else {
          incrementIgnored();
          warning(HarvesterBundle.format(HarvesterError.MODIFY_ROLE_IGNORED, role.getDisplayName()));
        }
      }
      catch (Exception e) {
        incrementFailed();
        error(method, HarvesterBundle.format(HarvesterError.MODIFY_ROLE, role.getDisplayName()));
      }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyInstance
  /**
   ** Modifies an {@link ApplicationInstance} item with specific data of the
   ** specified attribute mapping.
   **
   ** @param  instance           the {@link ApplicationInstance} providing the
   **                            actual data of the instance to modify.
   ** @param  data               the attribute mapping taken from an
   **                            {@link EntitlementEntity} providing the data to
   **                            modify the {@link ApplicationInstance}
   **                            <code>instance</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private void modifyInstance(final ApplicationInstance instance, final Map<String, Object> data)
    throws TaskException {

    final String method = "modifyInstance";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // skip processing of the particular entity if requested
    if (booleanValue(GATHERONLY)) {
      warning(method, HarvesterBundle.string(HarvesterMessage.IMPORT_SKIP));
      incrementIgnored();
    }
    else {
      boolean changed = false;
      try {
        String sourceValue = null;
        String targetValue = null;
        // we have to take care about specific infomation of the catalog item
        // like decription because unfortunately any change on such an attribute
        // is not reflected immediatly in the catalog if the entity is updated
        // itself
        // from this perspective we have to update the application instance itself
        // without removing the requested information form the entity to propagate
        // it to the catalog item also
        if (data.containsKey(CatalogConstants.CATALOG_ENTITY_DISPLAY_NAME)) {
          sourceValue =(String)data.get(CatalogConstants.CATALOG_ENTITY_DISPLAY_NAME);
          targetValue = instance.getDisplayName();
          if (targetValue != null && sourceValue != null && !targetValue.equals(sourceValue)) {
            instance.setDisplayName(sourceValue);
            changed = true;
          }
        }
        if (data.containsKey(CatalogConstants.CATALOG_ENTITY_DESCRIPTION)) {
          sourceValue =(String)data.get(CatalogConstants.CATALOG_ENTITY_DESCRIPTION);
          targetValue = instance.getDescription();
          if (targetValue != null && sourceValue != null && !targetValue.equals(sourceValue)) {
            instance.setDescription(sourceValue);
            changed = true;
          }
        }

        if (changed) {
          final ApplicationInstanceService facade = service(ApplicationInstanceService.class);
          facade.updateApplicationInstance(instance);
          incrementSuccess();
        }
        else {
          incrementIgnored();
          warning(HarvesterBundle.format(HarvesterError.MODIFY_INSTANCE_IGNORED, instance.getApplicationInstanceName()));
        }
      }
      catch (Exception e) {
        incrementFailed();
        error(method, HarvesterBundle.format(HarvesterError.MODIFY_INSTANCE, instance.getApplicationInstanceName()));
      }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyEntitlement
  /**
   ** Modifies an {@link Entitlement} item with specific data of the specified
   ** attribute mapping.
   **
   ** @param  entitlement        the {@link Entitlement} providing the actual
   **                            data of the instance to modify.
   ** @param  data               the attribute mapping taken from an
   **                            {@link EntitlementEntity} providing the data to
   **                            modify the {@link Entitlement}
   **                            <code>entitlement</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private void modifyEntitlement(final Entitlement entitlement, Map<String, Object> data)
    throws TaskException {

    final String method = "modifyEntitlement";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // skip processing of the particular entity if requested
    if (booleanValue(GATHERONLY)) {
      warning(method, HarvesterBundle.string(HarvesterMessage.IMPORT_SKIP));
      incrementIgnored();
    }
    else {
      boolean changed = false;
      try {
        String sourceValue = null;
        String targetValue = null;
        // we have to take care about specific infomation of the catalog item
        // like decription because unfortunately any change on such an attribute
        // is not reflected immediatly in the catalog
        // from this perspective we have to update the application instance
        // itself without removing the requested information form the entity to
        // propagate it to the catalog item also
        if (data.containsKey(CatalogConstants.CATALOG_ENTITY_DESCRIPTION)) {
          sourceValue =(String)data.get(CatalogConstants.CATALOG_ENTITY_DESCRIPTION);
          targetValue = entitlement.getDescription();
          if (targetValue != null && sourceValue != null && !targetValue.equals(sourceValue)) {
            entitlement.setDescription(sourceValue);
            changed = true;
          }
        }

        if (changed) {
          final EntitlementService facade = service(EntitlementService.class);
          facade.updateEntitlement(entitlement);
          incrementSuccess();
        }
        else {
          incrementIgnored();
          warning(HarvesterBundle.format(HarvesterError.MODIFY_ENTITLEMENT_IGNORED, entitlement.getEntitlementCode()));
        }
      }
      catch (Exception e) {
        incrementFailed();
        error(method, HarvesterBundle.format(HarvesterError.MODIFY_ENTITLEMENT, entitlement.getEntitlementCode()));
      }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyCatalog
  /**
   ** Modifies the catalog item, this can also be used to update the tags and
   ** metadata value.
   **
   ** @param  catalog            the bulk of {@link Catalog} items that has to
   **                            be updated.
   **
   ** @throws TaskException      that exception occured while performing any
   **                            catalog related operations.
   */
  private final void modifyCatalog(final Catalog catalog) {
    final String method = "modifyCatalog";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    if (booleanValue(GATHERONLY)) {
      this.catalog.incrementIgnored();
      warning(method, HarvesterBundle.string(HarvesterMessage.IMPORT_SKIP));
    }
    else {
      final List<Catalog>  bulk    = new ArrayList<Catalog>(1);
      bulk.add(catalog);
      final CatalogService service = service(CatalogService.class);
      final Result         result  = service.updateCatalogMetadataValues(bulk);
      if (result.isStatusFlag())
        this.catalog.incrementSuccess();
      else {
        this.catalog.incrementFailed();
        fatal(method, result.getException());
      }
    }

    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCatalog
  /**
   ** Creates a {@link Catalog} item.
   **
   ** @param  entityKey          the primary key of Entities like Role,
   **                            Entitlement and Application Instances.
   ** @param  entityType         one of the entity type like
   **                            <ul>
   **                              <li>{@link OIMType#Role}
   **                              <li>{@link OIMType#Entitlement}
   **                              <li>{@link OIMType#ApplicationInstance}
   **                             </ul>
   ** @param  risk                the {@link EntitlementEntity.Risk} level to
   **                             translate the semantic representation of a
   **                             risk level to the physical representation in
   **                             the persistence layer.
   ** @param  action
   ** @param  data               the attribute mapping providing the data the
   **                            specified {@link Harvester} <code>catalog</code>
   **                            has to be modified with.
   */
  private Catalog createCatalog(final String entityKey, final OIMType entityType, final EntitlementEntity.Risk risk, EntitlementEntity.Action action, final Map<String, Object> data) {
    final String method = "createCatalog";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Catalog catalog = catalogDetail(entityKey, entityType);
    // transfer the data of the entity to the catalog if there is an existing
    // catalog item
    if (catalog == null) {
      error(method, HarvesterBundle.format(HarvesterError.CATALOG_NOTFOUND, entityType.getValue(), entityKey));
    }
    else {
      // remove all data from the mapping that are not handled as metadata
      data.remove(CatalogConstants.CATALOG_ID);
      data.remove(CatalogConstants.CATALOG_ENTITY_KEY);
      data.remove(CatalogConstants.CATALOG_ENTITY_NAME);
      data.remove(CatalogConstants.CATALOG_ENTITY_TYPE);
      data.remove(CatalogConstants.CATALOG_ITEM_TAGS);

      // put the item risk in the data mapping and adjust the risk update date
      // if required
      if (!catalog.getItemRisk().equals(risk.level())) {
        data.put(CatalogConstants.CATALOG_ITEM_RISK,              risk.level().toString());
        // we don't need to evaluate the update date on our own we have only to
        // tag
        data.put(CatalogConstants.CATALOG_RISK_SCORE_UPDATE_DATE, SystemConstant.EMPTY);
      }

      // analyze how the owner principle is delivered in the feed
      // the rule which is applied here says that the certifier user needs to
      // be te same as the approver user if an approver user is defined and the
      // certifier user isn't defined
      final String approverUser  = (String)data.get(CatalogConstants.CATALOG_APPROVER_USER);
      final String certifierUser = (String)data.get(CatalogConstants.CATALOG_CERTFIER_USER);
      if (!StringUtility.isEmpty(approverUser) && StringUtility.isEmpty(certifierUser))
        data.put(CatalogConstants.CATALOG_CERTFIER_USER, approverUser);

      // analyze how the state of a catalog item has to be
      final boolean deleteStatus      = catalog.isDeleted();
      final boolean requestStatus     = catalog.isRequestable();
      EntitlementEntity.Action status = EntitlementEntity.Action.assign;
      if (deleteStatus == Boolean.TRUE)
        status = EntitlementEntity.Action.revoke;
      else
        status = !requestStatus ? EntitlementEntity.Action.disable : EntitlementEntity.Action.enable;

      if (status != action)
        switch (action) {
          case assign  :
          case enable  : data.put(CatalogConstants.IS_CATALOG_DELETED,          "0");
                         data.put(CatalogConstants.IS_CATALOG_ITEM_REQUESTABLE, "1");
                         break;
          case revoke  : data.put(CatalogConstants.IS_CATALOG_DELETED,          "1");
                         data.put(CatalogConstants.IS_CATALOG_ITEM_REQUESTABLE, "0");
                         break;
          case disable : data.put(CatalogConstants.IS_CATALOG_DELETED,          "0");
                         data.put(CatalogConstants.IS_CATALOG_ITEM_REQUESTABLE, "0");
                         break;
        }

      // a quick check to ensure that we update the catalog category correctly
      // category is mandatory for a catalog item hence if the supplier
      // specifies an empty category we have to fallback to the default category
      // dependend on the entity type passed in
      if (data.containsKey(CatalogConstants.CATALOG_CATEGORY)) {
        final String requested = (String)data.remove(CatalogConstants.CATALOG_CATEGORY);
        data.put(CatalogConstants.CATALOG_CATEGORY, StringUtility.isEmpty(requested) ? entityType.getValue() : requested);
      }

      if (!CollectionUtility.empty(data.keySet())) {
        final List<MetaData> payload = new ArrayList<MetaData>();
        for (String name : data.keySet()) {
          final Object   value    = data.get(name);
          final MetaData metadata = createMetaData(name, (value != null) ? value.toString() : null);
          payload.add(metadata);
        }
        catalog.setMetadata(payload);
      }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return catalog;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMetaData
  /**
   ** Create the information related to all the catalog metadata data.
   **
   ** @param  name               the name of the database column to upate.
   ** @param  value              the value for the database column to upate
   **                            with.
   **
   ** @return                    the {@link MetaData} created.
   */
  private MetaData createMetaData(final String name, final String value) {
    final MetaDataDefinition definition = new MetaDataDefinition();
    definition.setDbColumnName(name);

    final MetaData metadata = new MetaData();
    metadata.setMetaDataDefinition(definition);
    metadata.setValue(value);
    return metadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogDetail
  /**
   ** Returns the details of the catalog items this should be used.
   ** <p>
   ** The catalog VO will contain the metadata tags and the categories of the
   ** Harvester items. User can either pass the catalogId to get the Details of
   ** catalog item, this can be called from the UI. Or they can pass the
   ** entityKey and entityType for getting the details of catalog Items.
   ** This can be called from Request team or Provisioning team to get the
   ** details of Harvester, if they dont have catalog id with them.
   ** <p>
   ** Fixed DE-000159 Catalog Item cannot be managed by Access Request Catalog
   ** Import if it was soft-deleted previuosly.
   ** <br>
   ** The fix implements the direct access to the database instaed of using the
   ** API because the API excludes explicitly the soft-deleted items which isn't
   ** sufficient for the purpose of this service.
   **
   ** @param  entityKey          the primary key of Entities like Role,
   **                            Entitlement and Application Instances.
   ** @param  entityType         one of the entity type like
   **                            <ul>
   **                              <li>{@link OIMType#Role}
   **                              <li>{@link OIMType#Entitlement}
   **                              <li>{@link OIMType#ApplicationInstance}
   **                             </ul>
   */
  private Catalog catalogDetail(final String entityKey, final OIMType entityType) {
    final String method = "catalogDetail";
    trace(method, SystemMessage.METHOD_ENTRY);

    ResultSet         resultSet  = null;
    Catalog           item       = null;
    try {
      this.statement.setString(1, entityKey);
      this.statement.setString(2, entityType.getValue());
      resultSet = this.statement.executeQuery();
      // Identity Manager R2 PS3 change the interface to populate catalog items
      // from a SQL result set
      // formerly only the SQL result set needs to be passed to the method
      // Utility.getCatalogVoFromResultSet but now the signature is extended by
      // a list of attributes which will be returned for each catalog item in
      // the list of retuned items.
      // the method itself validates if a null-value is passed in and returns
      // all attributes of an catalog item including the custom attributes also.
      final List<Catalog> catalog = Utility.getCatalogVoFromResultSet(resultSet, null);
      if (catalog.size() != 0)
        item = catalog.get(0);
    }
    catch (SQLException e) {
      fatal(method, e);
    }
    catch (CatalogException e) {
      fatal(method, e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return item;
  }
}
