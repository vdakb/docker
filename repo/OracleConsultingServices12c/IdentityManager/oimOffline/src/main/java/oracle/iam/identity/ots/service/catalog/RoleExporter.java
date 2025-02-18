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

    File        :   RoleExporter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleExporter.


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

import java.io.File;

import java.sql.Connection;

import oracle.iam.catalog.vo.CatalogConstants;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLOutputNode;
import oracle.hst.foundation.xml.XMLProcessor;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.BusinessRoleEntity;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseSearch;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.utility.file.XMLCatalogFactory;
import oracle.iam.identity.utility.file.XMLBusinessRoleFactory;

import oracle.iam.identity.ots.resource.HarvesterBundle;

import oracle.iam.identity.ots.service.ControllerError;
import oracle.iam.identity.ots.service.ControllerException;

import oracle.iam.identity.ots.persistence.Repository;

////////////////////////////////////////////////////////////////////////////////
// abstract class RoleExporter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>RoleExporter</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** a XML file either to export Role definitions and their Access Policies.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
abstract class RoleExporter extends Harvester {

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
  protected static final String ENTITLEMENT_PREFIX = "Entitlement Prefix Stripped";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the roles root element of the XML file to produce */
  protected XMLOutputNode            roles              = null;

  protected Repository               repository         = null;

  /**
   ** the array of attribute names that will be passed to a Catalog search
   ** operation to specify which attributes the search operation should return.
   */
  private List<Pair<String, String>> returning;

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
  protected RoleExporter() {
    // ensure inheritance
    super(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

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
    final Batch          batch      = new Batch(batchSize());
    final DatabaseEntity entity     = this.repository.entity(Repository.Entity.CATALOGROLE);
    final DatabaseSearch search     = new DatabaseSearch(this, entity, this.repository.catalogRoleFilter(), returningAttributes());
    Connection           connection = null;
    try {
      // set the current date as the timestamp on which this task has been
      // last reconciled at the start of execution setting it here to have it
      // the next time this scheduled task will run the changes made during
      // the execution of this task updating this attribute will not perform
      // to write it back to the scheduled job attributes it's still in
      // memory; updateLastReconciled() will persist the change that we do
      // here only if the job completes successful
      connection = DatabaseConnection.aquire();
      lastReconciled(this.repository.systemTime(connection));
      search.prepare(connection);
      List<Map<String, Object>> result = null;
      do {
        HarvesterBundle.string(HarvesterMessage.COLLECTING_BEGIN);
        result = search.execute(batch.start(), batch.end());
        HarvesterBundle.string(HarvesterMessage.COLLECTING_COMPLETE);
        if (result != null && result.size() > 0) {
          ensureRoot();
          for (Map<String, Object> catalog : result) {
            final BusinessRoleEntity role = exportRole((String)catalog.get(CatalogConstants.CATALOG_ENTITY_NAME));
            if (role == null) {
              incrementFailed();
            }
            else if (gatherOnly()) {
              incrementIgnored();
            }
            else {
              writeRole(role);
              incrementSuccess();
            }
          }
        }
        batch.next();
      } while (!isStopped() && result != null && result.size() == batch.size());

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
      search.close();
      DatabaseConnection.release(connection);
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
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
      this.roles = XMLProcessor.marshal(this, dataFile, this.format).element(BusinessRoleEntity.SINGLE);
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
  protected abstract BusinessRoleEntity exportRole(final String name)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeRole
  /**
   ** This is used to create the role element and all child nodes.
   **
   ** @param  role               the {@link BusinessRoleEntity} to write to the
   **                            XML stream.
   */
  private void writeRole(final BusinessRoleEntity role)
    throws TaskException {

    // marshall the Java content tree to the XML file
    try {
      final XMLOutputNode root = this.roles.element(BusinessRoleEntity.SINGLE);
      Map<String, Object> subject = role.attribute();
      subject = transformMaster(createMaster(subject, false));
      role.attribute().clear();
      role.attribute().putAll(subject);
      writeEntity(role, root);
      for (ApplicationEntity app : role.application()) {
        final XMLOutputNode application = root.element(ApplicationEntity.SINGLE);
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
   ** This is used to create the entitlement element within the XML document.
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
      node.attribute(XMLBusinessRoleFactory.ATTRIBUTE_ACTION, String.valueOf(entity.action().toString()));
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
   */
  private void ensureRoot()
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
}