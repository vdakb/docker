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

    File        :   IdentityRoleReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityRoleReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.reconciliation;

import java.io.File;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.Collection;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.exception.RoleGrantException;
import oracle.iam.identity.exception.RoleGrantRevokeException;
import oracle.iam.identity.exception.ValidationFailedException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.offline.Identity;
import oracle.iam.identity.foundation.offline.RoleEntity;
import oracle.iam.identity.foundation.offline.EntityFactory;

import oracle.iam.identity.utility.file.XMLEntityFactory;
import oracle.iam.identity.utility.file.XMLIdentityRoleFactory;

import oracle.iam.analytics.harvester.domain.Result;
import oracle.iam.analytics.harvester.domain.Status;
import oracle.iam.analytics.harvester.domain.Severity;
import oracle.iam.analytics.harvester.domain.ObjectFactory;

import oracle.iam.identity.ots.service.ControllerError;
import oracle.iam.identity.ots.service.ControllerException;

import oracle.iam.identity.ots.resource.ReconciliationBundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityRoleReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>IdentityRoleReconciliation</code> implements the base
 ** functionality of a service end point for the Oracle Identity Manager
 ** Scheduler which handles identity data provided by XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class IdentityRoleReconciliation extends EntityReconciliation<Identity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the XML attribute specifies the location context of a message record.
   */
  static final String ELEMENT_LOCATION  = "location";

  /**
   ** the XML attribute specifies the name the timestamp of a message record.
   */
  static final String ELEMENT_TIMESTAMP = "timestamp";

  /**
   ** the XML attribute specifies the name the text of a message record.
   */
  static final String ELEMENT_TEXT      = "text";

  /**
   ** the XML attribute that identifies a the overall status of a request in
   ** Oracle Identity Analytics.
   */
  static final String ATTRIBUTE_STATUS  = "status";

  /**
   ** the array with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation object */
    TaskAttribute.build(RECONCILE_OBJECT, "Identity Role")
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,        TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATAFILE,         TaskAttribute.MANDATORY)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,      TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,     TaskAttribute.MANDATORY)
    /** the class name of the entity factory  */
  , TaskAttribute.build(UNMARSHALLER,     TaskAttribute.MANDATORY)
    /** the validation required before unmarshalling  */
  , TaskAttribute.build(VALIDATE_SCHEMA,  TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,    TaskAttribute.MANDATORY)
  };

  /**
   ** The SQL statement string to lookup a <code>User</code> login name using
   ** the internal system identifier of a <code>User</code>.
   */
  private static final String          LOOKUP_USER        = "SELECT usr_key FROM usr WHERE UPPER(usr_login) = UPPER(?)";

  /**
   ** The SQL statement string to lookup a <code>Role</code> name using the
   ** internal system identifier of a <code>Role</code>.
   */
  private static final String          LOOKUP_ROLE        = "SELECT ugp_key FROM ugp WHERE ugp_name = ?";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the identity root element of the XML file to produce */
  protected XMLOutputNode       identities    = null;

  protected final ObjectFactory objectFactory = new ObjectFactory();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityRoleReconciliation</code> scheduled job
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityRoleReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process (EntityListener)
  /**
   ** Reconciles a particular bulk of {@link Identity}.
   ** <br>
   ** This will do trusted reconciliation of Oracle Identity Manager Identities.
   **
   ** @param  bulk               the {@link Collection} of {@link Identity} to
   **                            reconcile.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  public void process(final Collection<Identity> bulk)
    throws TaskException {

    final String method = "process";
    trace(method, SystemMessage.METHOD_ENTRY);
    // validate the effort to do
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(bulk.size())));
    if (gatherOnly()) {
      incrementIgnored(bulk.size());
      info(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
      for (Identity identity : bulk) {
        final Result result  = this.objectFactory.createResult(Status.IGNORED);
        final String message = ReconciliationBundle.format(ReconciliationMessage.ROLE_IGNORED, identity.name());
        recordMessage(result, Severity.WARNING, method, message);
        writeResult(identity, result);
      }
    }
    else {
      for (Identity identity : bulk) {
        if (isStopped())
          break;

        final Result result    = this.objectFactory.createResult();
        final String loginName = identity.name();
        // accordingly to the XSD it is acceptable that an identity provided
        // by the XML file has only roles relationships means we need to
        // check if attributes are provided and only if there are something
        // we need to went through the reconciliation API.
        final String recipient = lookupIdentity(loginName);
        if (StringUtility.isEmpty(recipient)) {
          result.setStatus(Status.ERROR);
          recordMessage(result, Severity.ERROR, method, TaskBundle.format(TaskError.ENTITY_NOT_FOUND, TaskBundle.string(TaskMessage.ENTITY_IDENTITY), UserManagerConstants.AttributeName.USER_LOGIN.getId(), identity.name()));
          incrementFailed();
        }
        else {
          final List<RoleEntity> roleToRevoke = identity.roleToRevoke();
          if (!CollectionUtility.empty(roleToRevoke)) {
            final Result response = revokeRoles(loginName, recipient, roleToRevoke);
            result.setStatus(response.getStatus());
            result.getMessage().addAll(response.getMessage());
          }

          final List<RoleEntity> roleToAssign = identity.roleToAssign();
          if (!CollectionUtility.empty(roleToAssign)) {
            final Result response = assignRoles(loginName, recipient, roleToAssign);
            result.setStatus(response.getStatus());
            result.getMessage().addAll(response.getMessage());
          }
          incrementSuccess();
        }
        writeResult(identity, result);
      }
    }
    info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractSchedulerBaseTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeReconcile (Reconciliation)
  /**
   ** The call back method just invoked before reconciliation starts.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void beforeReconcile()
    throws TaskException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterReconcile (Reconciliation)
  /**
   ** The call back method just invoked after reconciliation finished.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void afterReconcile()
    throws TaskException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  @Override
  public void initialize()
    throws TaskException {

    // this will produce the trace of the configured task parameter and create
    // the abstract file paths to the data and error directories
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    // this will produce the trace of the configured task parameter
    debug(method, TaskBundle.format(TaskMessage.TASK_PARAMETER, this.toString()));
    final String className = stringValue(UNMARSHALLER);
    try {
      createDataFolder();
      createErrorFolder();
      // a little bit reflection
      final Class<?> clazz = Class.forName(className);
      this.factory = (EntityFactory<Identity>)clazz.newInstance();
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
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
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

      final File responseFile = errorFile();
      // check if we have write access to the file if its exists
      if (responseFile.exists() && !responseFile.canWrite()) {
        final String[] values = { DATAFILE, responseFile.getName()};
        throw new ControllerException(ControllerError.FILE_NOT_WRITABLE, values);
      }

      final XMLFormat format = new XMLFormat(String.format(XMLEntityFactory.PROLOG, stringValue(FILE_ENCODING)));

      // any identity element can only be created within an identities element
      // as its parent
      this.identities = XMLProcessor.marshal(this, responseFile, format).element(Identity.MULTIPLE);
      this.identities.attribute(XMLProcessor.ATTRIBUTE_XMLNS,     XMLEntityFactory.NAMESPACE);
      this.identities.attribute(XMLProcessor.ATTRIBUTE_XMLNS_XSI, XMLEntityFactory.SCHEMA);
      this.identities.attribute(XMLProcessor.ATTRIBUTE_SCHEMA,    XMLIdentityRoleFactory.SCHEMA);
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
      if (this.identities != null)
        this.identities.close();
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
  // Method:   assignRoles
  /**
   ** Assignes {@link RoleEntity}'s to the specifed {@link Identity}.
   **
   ** @param  login              the login name of the identity to assign the
   **                            roles to.
   ** @param  identity           the internal identifier the login name belongs
   **                            to the {@link RoleEntity}'s will be assigned
   **                            to.
   ** @param  role               the {@link Collection} of {@link RoleEntity}
   **                            to assign.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private Result assignRoles(final String login, final String identity, final Collection<RoleEntity> role) {
    final String      method    = "assignRoles";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Set<String> recipient = new HashSet<String>(1);
    final RoleManager facade    = service(RoleManager.class);
    final Result      result    = this.objectFactory.createResult();
    timerStart(method);
    recipient.add(identity);
    for (RoleEntity entity : role) {
      final String roleKey = lookupRole(entity.name());
      if (StringUtility.isEmpty(roleKey)) {
        result.setStatus(Status.ERROR);
        recordMessage(result, Severity.ERROR, method, TaskBundle.format(TaskError.ENTITY_NOT_FOUND, TaskBundle.string(TaskMessage.ENTITY_ROLE), RoleManagerConstants.GROUP_NAME, entity.name()));
      }
      else
        try {
          info(ReconciliationBundle.format(ReconciliationMessage.ROLE_ASSIGN, entity.name(), login));
          facade.grantRole(roleKey, recipient);
          recordMessage(result, Severity.INFORMATION, method, ReconciliationBundle.format(ReconciliationMessage.ROLE_ASSIGNED, entity.name(), login));
          info(ReconciliationBundle.format(ReconciliationMessage.ROLE_ASSIGNED, entity.name(), login));
        }
        catch (ValidationFailedException e) {
          result.setStatus(Status.WARNING);
          recordMessage(result, Severity.WARNING, method, ReconciliationBundle.format(ReconciliationError.ROLE_ASSIGNED, entity.name(), login, e.getErrorMessage()));
        }
        catch (RoleGrantException e) {
          result.setStatus(Status.FATAL);
          recordMessage(result, Severity.FATAL, method, TaskBundle.format(TaskError.UNHANDLED, e.getLocalizedMessage()));
        }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeRoles
  /**
   ** Revokes {@link RoleEntity}'s form the specifed {@link Identity}.
   **
   ** @param  login              the login name of the identity to revoke the
   **                            roles from.
   ** @param  identity           the internal identifier the login name belongs
   **                            to the {@link RoleEntity}'s will be revoked
   **                            from.
   ** @param  role               the {@link Collection} of {@link RoleEntity}
   **                            to revoke.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private Result revokeRoles(final String login, final String identity, final Collection<RoleEntity> role) {
    final String method = "revokeRoles";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Set<String> recipient = new HashSet<String>(1);
    final RoleManager facade    = service(RoleManager.class);
    final Result      result    = this.objectFactory.createResult();
    timerStart(method);
    recipient.add(identity);
    for (RoleEntity entity : role) {
      final String roleKey = lookupRole(entity.name());
      if (StringUtility.isEmpty(roleKey)) {
        result.setStatus(Status.ERROR);
        recordMessage(result, Severity.ERROR, method, TaskBundle.format(TaskError.ENTITY_NOT_FOUND, TaskBundle.string(TaskMessage.ENTITY_ROLE), RoleManagerConstants.GROUP_NAME, entity.name()));
      }
      else
        try {
          info(ReconciliationBundle.format(ReconciliationMessage.ROLE_REVOKE, entity.name(), login));
          facade.revokeRoleGrant(roleKey, recipient);
          recordMessage(result, Severity.INFORMATION, method, ReconciliationBundle.format(ReconciliationMessage.ROLE_REVOKED, entity.name(), login));
          info(ReconciliationBundle.format(ReconciliationMessage.ROLE_REVOKED, entity.name(), login));
        }
        catch (ValidationFailedException e) {
          result.setStatus(Status.WARNING);
          recordMessage(result, Severity.WARNING, method, ReconciliationBundle.format(ReconciliationError.ROLE_REVOKED, entity.name(), login, e.getErrorMessage()));
        }
        catch (RoleGrantRevokeException e) {
          result.setStatus(Status.FATAL);
          recordMessage(result, Severity.FATAL, method, TaskBundle.format(TaskError.UNHANDLED, e.getLocalizedMessage()));
        }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupIdentiy
  /**
   ** Lookups login name of a <code>User</code> from Oracle Identity Manager.
   **
   ** @param  identifier         the login name of an identity to lookup.
   **
   ** @return                    the login name of a <code>User</code> that
   **                            match the parameter <code>identifier</code> or
   **                            <code>null</code> if no <code>User</code> match
   **                            the parameter <code>identifier</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private String lookupIdentity(final String identifier) {
    final String method = "lookupIdentity";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      return lookupEntity(identifier, LOOKUP_USER);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRole
  /**
   ** Lookups the identifier of a <code>Role</code> from Oracle Identity Manager
   ** by its name.
   ** <p>
   **
   ** @param  identifier         the name of a <code>Role</code> to lookup.
   **
   ** @return                    the identifier of the <code>Role</code> that
   **                            match the name or <code>null</code> if no
   **                            <code>Role</code> match the specified name.
   */
  private String lookupRole(final String identifier) {
    final String method = "lookupRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      return lookupEntity(identifier, LOOKUP_ROLE);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupEntity
  /**
   ** Lookups login name of a <code>User</code> from Oracle Identity Manager.
   **
   ** @param  identifier         the login name of an identity to lookup.
   **
   ** @return                    the login name of a <code>User</code> that
   **                            match the parameter <code>identifier</code> or
   **                            <code>null</code> if no <code>User</code> match
   **                            the parameter <code>identifier</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private String lookupEntity(final String identifier, final String query) {
    final String method = "lookupEntity";

    String            id         = null;
    Connection        connection = null;
    PreparedStatement statement  = null;
    ResultSet         resultSet  = null;
    try {
      connection = DatabaseConnection.aquire();
      statement  = DatabaseStatement.createPreparedStatement(connection, query);
      statement.setString(1, identifier);
      resultSet  = statement.executeQuery();
      if (resultSet.next()) {
        id = resultSet.getString(1);
        if (resultSet.wasNull())
          id = null;
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    return id;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeResult
  /**
   ** This is used to create the error report of an identity element within the
   ** XML document.
   **
   ** @param  entity             the {@link Identity} currently in scope.
   ** @param  result             the {@link Result} collector where the process
   **                            status is written to.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void writeResult(final Identity entity, final Result result)
    throws TaskException {

    final String method = "writeResult";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);
    try {
      final XMLOutputNode identity = this.identities.element(Identity.SINGLE);
      identity.attribute(XMLIdentityRoleFactory.ATTRIBUTE_ID, entity.name());
      XMLOutputNode node = result.toXMLOutputNode(identity);

      // add all other attributes as an attribute eleent
      final Map<String, Object> attribute = entity.attribute();
      // be safe and check if attributes exists on the entity to avoid NPE
      if (attribute != null && !attribute.isEmpty()) {
        for (Map.Entry<String, Object> entry : attribute.entrySet()) {
          final XMLOutputNode attachment = identity.element(EntityFactory.ELEMENT_ATTRIBUTE);
          attachment.attribute(XMLIdentityRoleFactory.ATTRIBUTE_ID, entry.getKey());
          // to be safe check fist if the value meets all requirements
          if (entry.getValue() != null)
            attachment.value(entry.getValue().toString());
        }
      }
      final XMLOutputNode roles = identity.element(RoleEntity.MULTIPLE);
      for (RoleEntity application : entity.role()) {
        node = roles.element(RoleEntity.SINGLE);
        node.attribute(XMLIdentityRoleFactory.ATTRIBUTE_ID, application.name());
      }
      identity.commit();
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
    final Result.Message record = this.objectFactory.createResultMessage(severity, this.lastReconciled(), ReconciliationBundle.location(this.prefix, location), text);
    result.getMessage().add(record);
  }
}