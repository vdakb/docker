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

    File        :   Export.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Export.


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
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import java.io.File;

import java.math.BigDecimal;

import java.sql.Connection;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.catalog.vo.OIMType;
import oracle.iam.catalog.vo.Catalog;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.persistence.DatabaseConnection;
import oracle.iam.identity.foundation.persistence.DatabaseSearch;

import oracle.iam.identity.utility.file.XMLCatalogFactory;

import oracle.iam.identity.foundation.offline.Entity;
import oracle.iam.identity.foundation.offline.RoleEntity;
import oracle.iam.identity.foundation.offline.CatalogEntity;
import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.CatalogListener;
import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.offline.EntitlementEntity;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseSelect;

import oracle.iam.identity.ots.service.ControllerError;
import oracle.iam.identity.ots.service.ControllerException;

import oracle.iam.identity.ots.resource.HarvesterBundle;

import oracle.iam.identity.ots.persistence.Repository;

import oracle.iam.identity.ots.persistence.dialect.Namespace;

///////////////////////////////////////////////////////////////////////////////
// class Export
// ~~~~~ ~~~~~~
/**
 ** The <code>Export</code> class implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** catalog data writing to an XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class Export extends CatalogHarvester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on a <code>Scheduled Task</code>
   ** to specify if the entitlement marshalled to a file needs to be stripped of
   ** form the encoded and/or decoded values.
   ** <br>
   ** This attribute is mandatory.
   */
   private static final String         ENTITLEMENT_PREFIX = "Entitlement Prefix Stripped";

  /**
   ** the array with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute  = {
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
  , TaskAttribute.build(MARSHALLER,           TaskAttribute.MANDATORY)
    /** the flag to process roles */
  , TaskAttribute.build(PROCESS_ROLE,         TaskAttribute.MANDATORY)
    /** the flag to process application instances */
  , TaskAttribute.build(PROCESS_APPLICATION,  TaskAttribute.MANDATORY)
    /** the flag to process application instances */
  , TaskAttribute.build(PROCESS_ENTITELEMENT, TaskAttribute.MANDATORY)
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,   TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,        TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the catalog root element of the XML file to produce */
  private XMLOutputNode              catalog           = null;

  /** the roles root element of the XML file to produce */
  private XMLOutputNode              roles             = null;

  /** the applications root element of the XML file to produce */
  private XMLOutputNode              applications      = null;

  /** the application root element of the XML file to produce */
  private XMLOutputNode              application      = null;

  /** the entitlements root element of the XML file to produce */
  private XMLOutputNode              entitlements      = null;

  /** the listener to marshall XML file */
  private CatalogListener            listener          = null;

  private Repository                 repository        = null;

  /**
   ** the array of attribute names that will be passed to a Catalog search
   ** operation to specify which attributes the search operation should return.
   */
  private List<Pair<String, String>> returning;

  /** the flag with entitlement prefix option */
  private boolean                    entitlementPrefix = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Export</code> scheduled job that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Export() {
    // ensure inheritance
    super(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process (EntityHandler)
  /**
   ** Reconciles a particular bulk of {@link CatalogEntity}s.
   **
   ** @param  batch              the {@link Collection} of {@link CatalogEntity}
   **                            to synchronize.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void process(final Collection<CatalogEntity> batch)
    throws TaskException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processRole (CatalogListener)
  /**
   ** Reconciles a particular batch of {@link RoleEntity}'s.
   **
   ** @param  batch              the {@link Collection} of
   **                            {@link EntitlementEntity}'s to synchronize.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void processRole(final Collection<RoleEntity> batch)
    throws TaskException {

    final String method = "processRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    // process the provided bulk and capture all errors
    for (RoleEntity entity : batch) {
      if (isStopped())
        break;
      try {
        // create the enclosing element of a role
        final XMLOutputNode role = this.roles.element(RoleEntity.SINGLE);
        writeEntity(entity, role);
      }
      catch (XMLException e) {
        error(method, HarvesterBundle.format(HarvesterError.OPERATION_EXPORT_FAILED, OIMType.Role.getValue(), entity.name(), e.getLocalizedMessage()));
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processInstance (CatalogListener)
  /**
   ** Reconciles a particular batch of {@link ApplicationEntity}'s.
   **
   ** @param  batch              the {@link Collection} of
   **                            {@link EntitlementEntity}'s to reconcile.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void processInstance(final Collection<ApplicationEntity> batch)
    throws TaskException {

    final String method = "processInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    // process the provided bulk and capture all errors
    for (ApplicationEntity entity : batch) {
      if (isStopped())
        break;

      try {
        // first check if we are doing this for the first time
        if (this.application == null)
          writeApplication(entity);

        // nevertheless how the current state of the application element will be
        // if the name of the current application marshalled to the XML file
        // does not match the name specified in the arguments it's required to
        // create a new one which will implicitly commit the pending application
        final XMLOutputNode attributeID = this.application.attributes().lookup(XMLCatalogFactory.ATTRIBUTE_ID);
        if (!entity.name().equals(attributeID.value()))
          // this will implictly commit the previous application created and
          // flush all depended XML nodes from the buffer to the XML files
          writeApplication(entity);
      }
      catch (XMLException e) {
        error(method, HarvesterBundle.format(HarvesterError.OPERATION_EXPORT_FAILED, OIMType.ApplicationInstance.getValue(), entity.name(), e.getLocalizedMessage()));
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processEntitlement (CatalogListener)
  /**
   ** Reconciles a particular batch of {@link EntitlementEntity}'s.
   **
   ** @param  entity             the {{@link ApplicationEntity} the specified
   **                            {@link Collection} of
   **                            {@link EntitlementEntity}'s belongs to.
   ** @param  namespace          the namespace the {@link EntitlementEntity}'s
   **                            belongs to.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  public void processEntitlement(final ApplicationEntity entity, final String namespace)
    throws TaskException {

    final Collection<EntitlementEntity> batch = entity.entitlement(namespace);
    if (batch == null || batch.size() == 0)
      return;

    final String method = "processEntitlement";
    trace(method, SystemMessage.METHOD_ENTRY);

    ensureNamespace(entity, namespace);
    // process the provided bulk and capture all errors
    for (EntitlementEntity entitlement : batch) {
      if (isStopped())
        break;
      try {
        // this will implictly commit the previous entitlement element created
        // but should never happens due to the condition checked above
        final XMLOutputNode node = this.entitlements.element(EntitlementEntity.SINGLE);
        writeEntity(entitlement, node);
      }
      catch (XMLException e) {
        error(method, HarvesterBundle.format(HarvesterError.OPERATION_EXPORT_FAILED, OIMType.Entitlement.getValue(), entity.name(), e.getLocalizedMessage()));
      }
    }
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
    info(HarvesterBundle.format(HarvesterMessage.EXPORTING_BEGIN, parameter));
    Connection connection = null;
    try {
      // set the current date as the timestamp on which this task has been last
      // reconciled at the start of execution
      // setting it here to have it the next time this scheduled task will
      // run the changes made during the execution of this task
      // updating this attribute will not perform to write it back to the
      // scheduled job attributes it's still in memory; updateLastReconciled()
      // will persist the change that we do here only if the job completes
      // successful
      connection = DatabaseConnection.aquire();
      lastReconciled(this.repository.systemTime(connection));
    }
    finally {
      DatabaseConnection.release(connection);
    }

    try {
      if (!isStopped() && booleanValue(PROCESS_ROLE))
        exportRoles();
      if (!isStopped() && booleanValue(PROCESS_APPLICATION))
        exportApplications();
      if (isStopped()) {
        warning(HarvesterBundle.format(HarvesterMessage.EXPORTING_STOPPED, parameter));
      }
      else {
        this.updateLastReconciled();
        info(HarvesterBundle.format(HarvesterMessage.EXPORTING_COMPLETE, parameter));
      }
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
      warning(HarvesterBundle.format(HarvesterMessage.EXPORTING_ERROR, arguments));
      throw e;
    }
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

    // initialize the flag with entitlement prefix option
    this.entitlementPrefix = booleanValue(ENTITLEMENT_PREFIX);

    final String className = stringValue(MARSHALLER);
    if (className.equalsIgnoreCase("this"))
      this.listener = this;
    else {
      try {
        // a little bit reflection
        final Class<?> clazz = Class.forName(className);
        this.listener = (CatalogListener)clazz.newInstance();
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

    // the database dialect we will need everywhere hence instantiate it only
    // once
    this.repository = Repository.create("oracle.jdbc.OracleDriver");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** The call back method just invoked before reconciliation takes place.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
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
      this.catalog = XMLProcessor.marshal(this, dataFile, this.format).element(CatalogEntity.SINGLE);
      this.catalog.attribute(XMLProcessor.ATTRIBUTE_XMLNS,     XMLCatalogFactory.NAMESPACE);
      this.catalog.attribute(XMLProcessor.ATTRIBUTE_XMLNS_XSI, XMLCatalogFactory.SCHEMA);
      this.catalog.attribute(XMLProcessor.ATTRIBUTE_SCHEMA,    XMLCatalogFactory.SCHEMA_LOCATION);
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
  // Method:   afterExecution (overridden)
  /**
   ** The call back method just invoked after reconciliation finished.
   ** <br>
   ** Close all resources requested before reconciliation takes place.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void afterExecution()
    throws TaskException {

    final String method = "afterExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);
    try {
      if (this.catalog != null)
        this.catalog.close();
    }
    catch (XMLException e) {
      throw new TaskException(e);
    }
    finally {
      // stop the task timer from gathering performance metrics
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returningAttributes
  /**
   ** Returns the array of attribute names that will be passed to a Catalog
   ** search operation to specify which attributes the search operation should
   ** return.
   **
   ** @return                   the array of attribute names that will be
   **                           passed to a Catalog search operation to specify
   **                           which attributes the search operation
   **                           should return.
   */
  private List<Pair<String, String>> returningAttributes() {
    // Lazy initialization of the attribute names returned for an account to
    // reconcile
    if (this.returning != null)
      return this.returning;

    final Set<String> name = this.descriptor.returningAttributes();
    this.returning = new ArrayList<Pair<String, String>>(name.size());
    for (String cursor : name) {
      this.returning.add(Pair.of(cursor, cursor));
    }
    return this.returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportRoles
  /**
   ** Export the <code>Roles</code> from catalog.
   **
   ** @throws TaskException
   */
  private void exportRoles()
    throws TaskException {

    final String method = "exportRoles";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);

    final Batch          batch      = new Batch(batchSize());
    final DatabaseSearch search     = new DatabaseSearch(this, this.repository.entity(Repository.Entity.CATALOGROLE), this.repository.catalogRoleFilter(), returningAttributes());
    Connection           connection = null;
    try {
      // the roles root element of the XML file to produce
      this.roles = this.catalog.element(RoleEntity.MULTIPLE);
      connection = DatabaseConnection.aquire();
      search.prepare(connection);
      List<Map<String, Object>> result = null;
      do {
        HarvesterBundle.string(HarvesterMessage.COLLECTING_BEGIN);
        result = search.execute(batch.start(), batch.end());
        HarvesterBundle.string(HarvesterMessage.COLLECTING_COMPLETE);
        if (result != null && result.size() > 0) {
          final List<RoleEntity> bulk = new ArrayList<RoleEntity>(result.size());
          for (Map<String, Object> catalog : result)
            bulk.add(createRoleEntity(catalog));

          this.listener.processRole(bulk);
        }
        batch.next();
      } while (!isStopped() && result != null && result.size() == batch.size());
    }
    catch (XMLException e) {
      throw new TaskException(e);
    }
    finally {
      search.close();
      DatabaseConnection.release(connection);
      try {
        if (this.roles != null)
          this.roles.commit();
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportApplications
  /**
   ** Export the <code>Application Instanes</code> from catalog.
   **
   ** @throws TaskException
   */
  private void exportApplications()
    throws TaskException {

    final String method = "exportApplications";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);

    final Batch          batch  = new Batch(batchSize());
    final DatabaseSearch search = new DatabaseSearch(this, this.repository.entity(Repository.Entity.CATALOGAPPLICATION), this.repository.catalogApplicationFilter(), returningAttributes());
    Connection connection = null;
    try {
      // this will implicitly commit all pending changes of sibling elements
      // like roles so far
      this.applications = this.catalog.element(ApplicationEntity.MULTIPLE);

      connection = DatabaseConnection.aquire();
      search.prepare(connection);
      List<Map<String, Object>> result = null;
      do {
        HarvesterBundle.string(HarvesterMessage.COLLECTING_BEGIN);
        result = search.execute(batch.start(), batch.end());
        HarvesterBundle.string(HarvesterMessage.COLLECTING_COMPLETE);
        if (result != null && result.size() > 0) {
          final List<ApplicationEntity> bulk = new ArrayList<ApplicationEntity>(result.size());
          for (Map<String, Object> catalog : result)
            bulk.add(createApplicationEntity(catalog));

          this.listener.processInstance(bulk);
        }
        batch.next();
      } while (!isStopped() && result != null && result.size() == batch.size());
    }
    catch (XMLException e) {
      throw new TaskException(e);
    }
    finally {
      search.close();
      DatabaseConnection.release(connection);
      try {
        if (this.applications != null)
          this.applications.commit();
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportEntitlement
  /**
   ** Exports entitlements belonging to a specific application instance on the
   ** basis of input provided.
   ** <p>
   ** It will return the entitlements after applying the entity level security.
   **
   ** @param  application        the name of the application instance the
   **                            entitlements needs to be populated for.
   **
   ** @throws TaskException      if the search for entitlements fails.
   */
  private void exportEntitlement(final ApplicationEntity application)
    throws TaskException {

    final String method = "exportEntitlement";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);

    final ApplicationInstance       instance   = populateInstance(application.name());
    final List<Map<String, Object>> namespaces = populateNamespace(instance);
    final DatabaseEntity            entity     = this.repository.entity(Repository.Entity.PROPERTYCATALOGJOIN);
    Connection                      connection = null;
    try {
      connection = DatabaseConnection.aquire();
      // entitlements needs to be exported by their namespace hence the name
      // of the Lookup Definition drives which entitlements has to be exported
      for (Map<String, Object> namespace : namespaces) {
        // check if a stop signal is pending
        if (isStopped())
          break;

        final Batch          batch  = new Batch(batchSize());
        final DatabaseSearch search = new DatabaseSearch(
          this
        , entity
        , this.repository.catalogEntitlementFilter(instance.getObjectKey(), instance.getItResourceKey(), (BigDecimal)namespace.get(Namespace.PRIMARY))
        , returningAttributes()
        );
        try {
          search.prepare(connection);
          List<Map<String, Object>> result = null;
          do {
            HarvesterBundle.string(HarvesterMessage.COLLECTING_BEGIN);
            result = search.execute(batch.start(), batch.end());
            HarvesterBundle.string(HarvesterMessage.COLLECTING_COMPLETE);
            if (result != null && result.size() > 0) {
              final String namespaceName = (String)namespace.get(Namespace.UNIQUE);
              application.namespace(namespaceName);
              for (Map<String, Object> catalog : result)
                application.add(namespaceName, createEntitlementEntity(instance, catalog));

              this.listener.processEntitlement(application, namespaceName);
              application.entitlement(namespaceName).clear();
            }
            batch.next();
          } while (!isStopped() && result != null && result.size() == batch.size());
        }
        finally {
          search.close();
        }
      }
    }
    finally {
      DatabaseConnection.release(connection);
      // stop the task timer from gathering performance metrics
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateNamespace
  /**
   ** Populates the namespaces aka <code>Lookup Definition</code>s belonging to
   ** a specific application instance on the basis of input provided.
   **
   ** @param  instance           the {@link ApplicationInstance} the
   **                            <code>Lookup Definition</code>s needs to be
   **                            populated for.
   **
   ** @return                    the list of namespaces aka
   **                            <code>Lookup Definition</code>s that belongs to
   **                            the specified {@link ApplicationInstance}.
   **                            Each entry of the returning {@link List} is a
   **                            {@link Map} where key and value are the name of
   **                            a namespace.
   **
   ** @throws TaskException      if the search for entitlements fails.
   */
  private List<Map<String, Object>> populateNamespace(final ApplicationInstance instance)
    throws TaskException {

    final String method = "populateNamespace";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);

    final DatabaseSelect propertyQuery = DatabaseSelect.build(
      this
    , this.repository.entity(Repository.Entity.PROPERTYENTITLEMENTJOIN)
    , this.repository.namespaceEntitlementFilter(instance.getObjectKey(), instance.getItResourceKey())
    , CollectionUtility.list(Pair.of(Namespace.PRIMARY, Namespace.PRIMARY))
    );
    final List<Pair<String, String>> returning = CollectionUtility.list(
      Pair.of(Namespace.PRIMARY, Namespace.PRIMARY)
    , Pair.of(Namespace.UNIQUE, Namespace.UNIQUE)
    );
    final DatabaseEntity namespaceEntity = this.repository.entity(Repository.Entity.NAMESPACE);
    final DatabaseSelect namespaceQuery  = DatabaseSelect.build(this, namespaceEntity, DatabaseFilter.build(namespaceEntity, propertyQuery, DatabaseFilter.Operator.IN), returning);

    Connection connection = null;
    try {
      connection = DatabaseConnection.aquire();
      return namespaceQuery.execute(connection);
    }
    finally {
      namespaceQuery.close();
      DatabaseConnection.release(connection);

      // stop the task timer from gathering performance metrics
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRoleEntity
  /**
   ** This is used to transfer all attributes of an catalog item to a generic
   ** attribute collection of an {@link RoleEntity}.
   **
   ** @param  catalog            the {@link Catalog} item to export to the
   **                            attribute mapping of an generic {@link Entity}.
   **
   ** @return                    the {@link RoleEntity} created from the
   **                            generic attribute mapping.
   */
  private RoleEntity createRoleEntity(final Map<String, Object> catalog)
    throws TaskException {

    // apply standard mapping and transformation
    final Map<String, Object> data   = createMapping(catalog);
    // first create an XML serializable Entity with an appropriate name
    final RoleEntity          entity = new RoleEntity((String)data.remove(this.descriptor.identifier()));
    // than to the standard transformation of the catalog data to an entity
    exportEntity(entity, data);
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createApplicationEntity
  /**
   ** This is used to transfer all attributes of an catalog item to a generic
   ** attribute collection of an {@link ApplicationEntity}.
   **
   ** @param  catalog            the {@link Catalog} item to export to the
   **                            attribute mapping of an generic {@link Entity}.
   **
   ** @return                    the {@link ApplicationEntity} created from the
   **                            generic attribute mapping.
   */
  private ApplicationEntity createApplicationEntity(final Map<String, Object> catalog)
    throws TaskException {

    // apply standard mapping and transformation
    final Map<String, Object> data   = createMapping(catalog);
    // first create an XML serializable Entity with an appropriate name
    final ApplicationEntity   entity = new ApplicationEntity((String)data.remove(this.descriptor.identifier()));
    // than to the standard transformation of the catalog data to an entity
    exportEntity(entity, data);
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   EntitlementEntity
  /**
   ** This is used to transfer all attributes of an catalog item to a generic
   ** attribute collection of an {@link EntitlementEntity}.
   ** <p>
   ** This method takes care about entitlement prefixing.
   **
   **
   ** @param  instance           the {@link ApplicationInstance} the
   **                            {@link EntitlementEntity} to create belongs to.
   ** @param  catalog            the {@link Catalog} item to export to the
   **                            attribute mapping of an generic
   **                            {@link EntitlementEntity}.
   **
   ** @return                    the {@link EntitlementEntity} created from the
   **                            generic attribute mapping.
   */
  private EntitlementEntity createEntitlementEntity(final ApplicationInstance instance, final Map<String, Object> catalog)
    throws TaskException {

    // apply standard mapping and transformation
    final Map<String, Object> data   = createMapping(catalog);
    String                    name   = (String)data.remove(this.descriptor.identifier());
    // remove any internal prefix regarding the IT Resource
    if (booleanValue(ENTITLEMENT_PREFIX)) {
      final String[] pattern     = this.binding.get(instance.getItResourceKey());
      final long     resourceKey = instance.getItResourceKey();
      String         prefix      = null;
      if (pattern == null || pattern.length == 0) {
        prefix = String.format(HarvesterDescriptor.DEFAULT_PATTERN_ENCODED, resourceKey, "");
      }
      else {
        prefix = String.format(pattern[1], resourceKey, pattern[0], "");
      }
      if (name.startsWith(prefix))
        name = name.substring(prefix.length());
    }
    // first create an XML serializable Entity with an appropriate name
    final EntitlementEntity entity = new EntitlementEntity(name);
    // than to the standard transformation of the catalog data to an entity
    exportEntity(entity, data);
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportEntity
  /**
   ** This is used to transfer all attributes of an catalog item to a generic
   ** attribute collection of an {@link Entity}.
   **
   ** @param  catalog            the {@link Catalog} item to export to the
   **                            attribute mapping of an generic {@link Entity}.
   */
  private void exportEntity(final EntitlementEntity entity, final Map<String, Object> catalog)
    throws TaskException {

    final String method = "exportEntity";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);

    // first adjust the risk level found on the catalog item but we have to be
    // careful in casting if a null value is received we'll get a string hence
    final Object riskLevel = catalog.remove(((HarvesterDescriptor)this.descriptor).riskLevel());
    if (riskLevel == null || (riskLevel instanceof String))
      entity.risk(EntitlementEntity.Risk.none);
    else {
      int risk = ((BigDecimal)riskLevel).intValue();
      switch (risk) {
        case 7  : entity.risk(EntitlementEntity.Risk.high);
                  break;
        case 5  : entity.risk(EntitlementEntity.Risk.medium);
                  break;
        case 3  :
        default : entity.risk(EntitlementEntity.Risk.low);
                  break;
      }
    }
    // first adjust the action found on the catalog item eblonging to delete and
    // request status
    final Boolean deleteStatus  = (Boolean)catalog.remove(((HarvesterDescriptor)this.descriptor).deleteStatus());
    final Boolean requestStatus = (Boolean)catalog.remove(((HarvesterDescriptor)this.descriptor).requestStatus());
    if ((deleteStatus == null) && (requestStatus == null))
      entity.action(EntitlementEntity.Action.assign);
    else if (deleteStatus == Boolean.TRUE)
      entity.action(EntitlementEntity.Action.revoke);
    else
      entity.action((requestStatus == Boolean.FALSE) ? EntitlementEntity.Action.disable : EntitlementEntity.Action.assign);
    // fourth add all other attributes
    entity.addAttribute(catalog);

    // stop the task timer from gathering performance metrics
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureNamespace
  /**
   ** This is used to create the an entitlements root element within the XML
   ** document.
   ** <p>
   ** The created <code>XMLOutputNode</code> aentitlements object can be used to
   ** add attributes to the catalog  element as well as other elements.
   */
  private void ensureNamespace(final EntitlementEntity application, final String name)
    throws TaskException {

    // prevent bogus input
    if (application == null)
      throw TaskException.argumentIsNull("application");

    if (StringUtility.isEmpty(name))
      throw TaskException.argumentIsNull("name");

    final String method = "ensureNamespace";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);

    try {
      // first check if we are doing this for the first time
      if (this.entitlements == null) {
        this.entitlements = this.application.element(EntitlementEntity.MULTIPLE);
        this.entitlements.attribute(XMLCatalogFactory.ATTRIBUTE_ID, name);
      }
      // nevertheless how the current state of the entitlements element will be
      // if the id of the current entitlements elemnts marshalled to the xml
      // file does not match the name specified in the arguments it's required
      // to create a new one which will implicitly commit the pending
      // entitlements elemnt
      final XMLOutputNode attributeID = this.entitlements.attributes().lookup(XMLCatalogFactory.ATTRIBUTE_ID);
      if (!name.equals(attributeID.value())) {
        // this will implictly commit the previous entitlements element created
        // and flush all pending XML nodes from the buffer to the XML files
        this.entitlements = this.application.element(EntitlementEntity.MULTIPLE);
        this.entitlements.attribute(XMLCatalogFactory.ATTRIBUTE_ID, name);
      }
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
  // Method:   writeApplication
  /**
   ** This is used to write the application element to the XML document.
   **
   ** @param  entity             the {@link ApplicationEntity} to write to the
   **                            XML document.
   */
  private void writeApplication(final ApplicationEntity entity)
    throws TaskException {

    final String method = "writeApplication";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);
    try {
      // this will implictly commit the previous lookup created but should
      // never happens due to the condition checked above
      this.application = this.applications.element(ApplicationEntity.SINGLE);
      writeEntity(entity, this.application);
      if (booleanValue(PROCESS_ENTITELEMENT))
        exportEntitlement(entity);
    }
    catch (XMLException e) {
      error(method, HarvesterBundle.format(HarvesterError.OPERATION_EXPORT_FAILED, OIMType.ApplicationInstance.getValue(), entity.name(), e.getLocalizedMessage()));
    }
    finally {
      // stop the task timer from gathering performance metrics
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEntity
  /**
   ** This is used to create the application element within the XML
   ** document.
   */
  private void writeEntity(final EntitlementEntity entity, final XMLOutputNode node)
    throws XMLException {

    final String method = "writeEntity";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);
    try {
      node.attribute(XMLCatalogFactory.ATTRIBUTE_ID, entity.name());
      node.attribute(XMLCatalogFactory.ATTRIBUTE_RISK,   String.valueOf(entity.risk().toString()));
      node.attribute(XMLCatalogFactory.ATTRIBUTE_ACTION, String.valueOf(entity.action().toString()));
      // add all other attributes as an attribute eleent
      final Map<String, Object> attribute = entity.attribute();
      // be safe and check if attributes exists on the entity to avoid NPE
      if (attribute != null && !attribute.isEmpty()) {
        for (Map.Entry<String, Object> entry : attribute.entrySet()) {
          final XMLOutputNode attachment = node.element(EntityFactory.ELEMENT_ATTRIBUTE);
          attachment.attribute(XMLCatalogFactory.ATTRIBUTE_ID, entry.getKey());
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
  // Method:   createMaster
  /**
   ** Do all action which should take place to create catalog data.
   **
   ** @param  catalog            the {@link Map} providing the raw data to
   **                            harvest.
   **
   ** @return                    the {@link Map} with requested data.
   **
   ** @throws TaskException      if data process fails.
   */
  private Map<String, Object> createMapping(final Map<String, Object> catalog)
    throws TaskException {
/*
    final String nullValue = nullValue();
    for (String attribute : catalog.keySet()) {
      final Object value = catalog.get(attribute);
      catalog.put(attribute, (value == null) ? nullValue : value);
    }
*/
    // apply standard mapping and transformation
    return transformMaster(createMaster(catalog, false));
  }
}