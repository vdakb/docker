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

    File        :   RoleHarvester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleHarvester.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

import java.util.Map;
import java.util.Date;

import java.io.File;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.persistence.DatabaseConnection;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.EntityListener;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.offline.BusinessRoleEntity;

import oracle.iam.identity.utility.file.XMLBusinessRoleFactory;

import oracle.iam.analytics.harvester.domain.Result;

import oracle.iam.identity.ots.service.ControllerError;
import oracle.iam.identity.ots.service.ControllerException;

import oracle.iam.identity.ots.resource.HarvesterBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class RoleHarvester
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>RoleHarvester</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** a XML file either import Role definitions and their Access Policies.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
abstract class RoleHarvester extends    Harvester
                             implements EntityListener<BusinessRoleEntity> {

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
  protected static final String               ENTITLEMENT_PREFIX = "Entitlement Prefix Required";

  /**
   ** The SQL statement string to lookup an <code>Entitlement</code> reconciled
   ** in a <ode>Lookup Definition</code>.
   */
  private static final String                 LOOKUP_ENTITLEMENT = "SELECT 'x' FROM dual WHERE EXISTS (SELECT lkv.lkv_key FROM lkv, lku WHERE lkv.lku_key = lku.lku_key AND lku.lku_type_string_key = ? AND lkv.lkv_encoded = ?)";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the factory to unmarshal the XML file */
  protected EntityFactory<BusinessRoleEntity> entityFactory      = null;

  /** the roles root element of the XML file to produce */
  protected XMLOutputNode                     roles              = null;

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
  protected RoleHarvester() {
    // ensure inheritance
    super(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullValue (EntityListener)
  /**
   ** Returns the value which represents a <code>null</code> for an attribute
   ** element.
   ** <p>
   ** Such specification is required to distinct between empty attribute
   ** elements which are not passed through and overriding an already existing
   ** metadata to make it <code>null</code>.
   **
   ** @return                    the value which represents a <code>null</code> for
   **                            an attribute element.
   */
  @Override
  public String nullValue() {
    return ((HarvesterDescriptor)this.descriptor).nullValue();
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

    final String method="onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
    info(HarvesterBundle.format(HarvesterMessage.IMPORTING_BEGIN, parameter));
    if (this.dataFileAvailable(lastReconciled())) {
      info(HarvesterBundle.format(HarvesterMessage.IMPORT_BEGIN, parameter));
      try {
        // set the current date as the timestamp on which this task has been
        // last reconciled at the start of execution setting it here to have it
        // the next time this scheduled task will query for the changes made
        // during the execution of this task updating this attribute will not
        // perform to write it back to the scheduled job attributes it's still
        // in memory; updateLastReconciled() will persist the change that we do
        // here only if the job completes successful
        lastReconciled(new Date());
        this.entityFactory.populate(this, this.dataFile(), booleanValue(VALIDATE_SCHEMA));
        this.updateLastReconciled();

        // finalize error reporting by committing the root element
        if (this.roles != null)
          try {
            this.roles.close();
          }
          catch (XMLException e) {
            fatal(method, e);
          }

        info(HarvesterBundle.format(HarvesterMessage.IMPORT_COMPLETE, parameter));
      }
      catch (TaskException e) {
        final String[] arguments = {reconcileObject(), getName() , dataFile().getAbsolutePath() , e.getLocalizedMessage()};
        // notify user about the problem
        warning(HarvesterBundle.format(HarvesterMessage.IMPORTING_STOPPED, arguments));
        trace(method, SystemMessage.METHOD_EXIT);
        throw e;
      }
    }
    info(HarvesterBundle.format(HarvesterMessage.IMPORTING_COMPLETE, parameter));
    trace(method, SystemMessage.METHOD_EXIT);
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
      this.entityFactory = (EntityFactory<BusinessRoleEntity>)clazz.newInstance();
      this.entityFactory.uniqueName(EntityFactory.Unique.fromValue(((HarvesterDescriptor)this.descriptor).uniqueName()));
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
  // Method:  existsEntitlement
  /**
   ** Verifies the existance of an entitlement value in the specified
   ** <code>Lookup Definition</code> namespace.
   ** <p>
   ** We performing an exact match here hence any internal stuff regarding
   ** prefixing of entitlements has to be done before invocatio of this method.
   ** <pre>
   ** SELECT 'x'
   ** FROM   dual
   ** WHERE  EXISTS (
   **   SELECT lkv.lkv_key
   **   FROM   lkv, lku
   **   WHERE  lkv.lku_key = lku.lku_key
   **   AND    lku.lku_type_string_key = ?
   **   AND lkv.lkv_encoded = ?
   ** )
   ** </pre>
   **
   ** @param  namespace          the namespace of the
   **                            <code>Lookup Definition</code>.
   ** @param  entitlement        the desired entitlement value contained in the
   **                            specified <code>Lookup Definition</code>.
   **
   ** @return                    <code>true</code> if both are exists.
   **
   ** @throws HarvesterException in the event of database access fails.
   */
  protected final boolean existsEntitlement(final String namespace, final String entitlement)
    throws HarvesterException {

    final String method = "existsEntitlement";
    trace(method, SystemMessage.METHOD_ENTRY);

    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    boolean           exists     = false;
    try {
      connection = DatabaseConnection.aquire();
      statement  = DatabaseStatement.createPreparedStatement(connection, LOOKUP_ENTITLEMENT);
      statement.setString(1, namespace);
      statement.setString(2, entitlement);
      resultSet  = statement.executeQuery();
      // position the cursor on the next available row
      if (resultSet.next()) {
        // fetch at least one value to ensure the result set is positoned
        // properly
        resultSet.getString(1);
        // check if a we have something
        exists = !resultSet.wasNull();
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
    return exists;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeResult
  /**
   ** This is used to create the error report of an entity element within the
   ** XML document.
   **
   ** @param  entity             the {@link BusinessRoleEntity} currently in
   **                            scope.
   ** @param  result             the {@link Result} collector where the process
   **                            status is written to.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void writeResult(final BusinessRoleEntity entity, final Result result)
    throws TaskException {

    final String method = "writeResult";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);
    ensureRoot();
    try {
      final XMLOutputNode role     = this.roles.element(BusinessRoleEntity.SINGLE);
      role.attribute(XMLBusinessRoleFactory.ATTRIBUTE_ID,   entity.name());
      EntitlementEntity.Risk risk = entity.risk();
      if (risk != null)
        role.attribute(XMLBusinessRoleFactory.ATTRIBUTE_RISK, risk.toString());

      XMLOutputNode node = result.toXMLOutputNode(role);

      // add all other attributes as an attribute eleent
      final Map<String, Object> attribute = entity.attribute();
      // be safe and check if attributes exists on the entity to avoid NPE
      if (attribute != null && !attribute.isEmpty()) {
        for (Map.Entry<String, Object> entry : attribute.entrySet()) {
          final XMLOutputNode attachment = role.element(EntityFactory.ELEMENT_ATTRIBUTE);
          attachment.attribute(XMLBusinessRoleFactory.ATTRIBUTE_ID, entry.getKey());
          // to be safe check fist if the value meets all requirements
          if (entry.getValue() != null)
            attachment.value(entry.getValue().toString());
        }
      }
      final XMLOutputNode applications = role.element(ApplicationEntity.MULTIPLE);
      for (ApplicationEntity application : entity.application()) {
        node = applications.element(ApplicationEntity.SINGLE);
        node.attribute(XMLBusinessRoleFactory.ATTRIBUTE_ID, application.name());
        for (String namespace : application.namespace()) {
          final XMLOutputNode container = node.element(EntitlementEntity.MULTIPLE);
          container.attribute(XMLBusinessRoleFactory.ATTRIBUTE_ID, namespace);
          for (EntitlementEntity entitlement : application.entitlement(namespace)) {
            final XMLOutputNode xxx = container.element(EntitlementEntity.SINGLE);
            xxx.attribute(XMLBusinessRoleFactory.ATTRIBUTE_ID, entitlement.name());
          }
        }
      }
      role.commit();
    }
    catch (XMLException e) {
      throw TaskException.general(e);
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
        final File errorFile = errorFile();
        // check if we have write access to the file if its exists
        if (errorFile.exists() && !errorFile.canWrite()) {
          final String[] values = { DATAFILE, errorFile.getName()};
          throw new ControllerException(ControllerError.FILE_NOT_WRITABLE, values);
        }

        // any role element can only be created within a roles element as its
        // parent
        this.roles = XMLProcessor.marshal(this, errorFile, this.format).element(BusinessRoleEntity.MULTIPLE);
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
  }
}