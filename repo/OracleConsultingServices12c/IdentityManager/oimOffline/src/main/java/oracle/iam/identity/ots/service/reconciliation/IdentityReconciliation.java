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

    File        :   IdentityReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.reconciliation;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.Collection;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import oracle.iam.identity.rolemgmt.vo.Role;

import oracle.iam.identity.rolemgmt.api.RoleManager;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.exception.RoleGrantRevokeException;
import oracle.iam.identity.exception.ValidationFailedException;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.naming.User;
import oracle.iam.identity.foundation.naming.Lookup;
import oracle.iam.identity.foundation.naming.LookupValue;
import oracle.iam.identity.foundation.naming.Organization;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.Identity;
import oracle.iam.identity.foundation.offline.Identity.Action;
import oracle.iam.identity.foundation.offline.RoleEntity;

import oracle.iam.identity.foundation.persistence.DatabaseConnection;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

import oracle.iam.identity.utility.file.XMLEntityFactory;

////////////////////////////////////////////////////////////////////////////////
// class IdentityReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>IdentityReconciliation</code> implements the base functionality of
 ** a service end point for the Oracle Identity Manager Scheduler which handles
 ** identity data provided by XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class IdentityReconciliation extends EntityReconciliation<Identity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify which default
   ** organization should a onboarded user be assign if the loaded organization
   ** could not be resolved.
   ** <br>
   ** This attribute is optional.
   */
  private static final String IDENTITY_ORGANIZATION = "Identity Organization";

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** role name should assigned to a onboarded user if no value is loaded from
   ** the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String IDENTITY_ROLE         = "Identity Role";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** identity type value should assigned to a onboarded user if no value is
   ** loaded from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String IDENTITY_TYPE         = "Identity Type";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** identity status value should assigned to a onboarded user if no value is
   ** loaded from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String IDENTITY_STATUS       = "Identity Status";

  /**
   ** the array with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation target */
    TaskAttribute.build(RECONCILE_OBJECT,     TaskAttribute.MANDATORY)
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
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,        TaskAttribute.MANDATORY)
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

  // the value holder for the fieldnames of mandatory attributes in Oracle
  // Identity Manager
  private String                       identityStatus;
  private String                       identityRole;
  private String                       identityType;
  private String                       identityOrganization;
  private String                       identityManager;

  // the value holder for the defaults of mandatory attributes in Oracle
  // Identity Manager
  private String                       identityStatusDefault;
  private String                       identityRoleDefault;
  private String                       identityTypeDefault;
  private String                       identityOrganizationDefault;

  private tcLookupOperationsIntf       lookup;
  private tcOrganizationOperationsIntf organization;
  private tcUserOperationsIntf         user;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityReconciliation</code> scheduled job that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityReconciliation() {
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
    try {
      if (gatherOnly()) {
        incrementIgnored(bulk.size());
        info(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
      }
      else {
        for (Identity identity : bulk) {
          if (isStopped())
            break;

          // accordingly to the XSD it is acceptable that an identity provided
          // by the XML file has only roles relationships means we need to check
          // if attributes are provided and only if there are something we need
          // to went through the reconciliation API.
          if (!CollectionUtility.empty(identity.attribute()))
            reconcile(identity);
          else {
            processRoles(identity);
          }
        }
      }
    }
    finally {
      info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
      trace(method, SystemMessage.METHOD_EXIT);
    }
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
   ** The call back method just invoked before reconciliation finished.
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

    this.user                        = this.userFacade();
    this.organization                = this.organizationFacade();
    this.lookup                      = this.lookupFacade();

    this.identityStatus              = this.descriptor.identityStatus();
    this.identityRole                = this.descriptor.identityRole();
    this.identityType                = this.descriptor.identityType();
    this.identityOrganization        = this.descriptor.identityOrganization();
    this.identityManager             = this.descriptor.identityManager();

    this.identityOrganizationDefault = stringValue(IDENTITY_ORGANIZATION);
    // check if the specified organization exists; if not create use the default
    this.identityOrganizationDefault = validateOrganization(this.identityOrganizationDefault, Organization.DEFAULT_NAME);

    tcResultSet resultSet     = null;
    // check if the specified identity role exists; if not assign the default
    this.identityRoleDefault = stringValue(IDENTITY_ROLE);
    try {
      this.filter.clear();
      this.filter.put(LookupValue.ENCODED, this.identityRoleDefault);
      resultSet = this.lookup.getLookupValues(Lookup.IDENTITY_TYPE, this.filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { IDENTITY_ROLE, this.identityRole, this.identityRoleDefault };
        warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default role
        this.identityRoleDefault = User.DEFAULT_TYPE;
      }
    }
    catch (tcInvalidLookupException e) {
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.IDENTITY_ROLE);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }

    // check if the specified identity type exists; if not assign the default
    this.identityTypeDefault = stringValue(IDENTITY_TYPE);
    try {
      this.filter.clear();
      this.filter.put(LookupValue.ENCODED, this.identityTypeDefault);
      resultSet = this.lookup.getLookupValues(Lookup.IDENTITY_ROLE, this.filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { IDENTITY_TYPE, this.identityType, this.identityTypeDefault};
        warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default type
        this.identityTypeDefault = User.DEFAULT_ROLE;
      }
    }
    catch (tcInvalidLookupException e) {
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.IDENTITY_TYPE);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }

    // check if the specified identity status exists; if not assign the default
    this.identityStatusDefault = stringValue(IDENTITY_STATUS);
    try {
      this.filter.clear();
      this.filter.put(LookupValue.ENCODED, this.identityStatusDefault);
      resultSet = this.lookup.getLookupValues(Lookup.IDENTITY_STATUS, this.filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { IDENTITY_STATUS, this.identityStatus, this.identityStatusDefault };
        warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default role
        this.identityStatusDefault = User.STATUS_ACTIVE;
      }
    }
    catch (tcInvalidLookupException e) {
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.IDENTITY_STATUS);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
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

    this.lookup.close();
    this.lookup = null;

    this.organization.close();
    this.organization = null;

    this.user.close();
    this.user = null;

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconcile
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** {@link Identity}.
   ** <br>
   ** This will do trusted reconciliation of Oracle Identity Manager Identities.
   **
   ** @param  identity           the {@link Identity} to reconcile.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void reconcile(final Identity identity)
    throws TaskException {

    final String method = "reconcile";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Map<String, Object> attribute = identity.attribute();
    // put the name of the account as the id in the attributes to ensure that
    // the master mapping picks it up correctly
    attribute.put(XMLEntityFactory.ATTRIBUTE_ID, identity.name());
    // hmmmm, should we really chain
    final Map<String, Object> data = transformMaster(createMaster(attribute, false));
    try {
      // Whether the organization with the name exists or no organization
      // contained in the provided data hence substitute the organization name
      // with the default organization name specified by the job configuration
      data.put(this.identityOrganization, validateOrganization((String)data.get(this.identityOrganization), this.identityOrganizationDefault));

      // apply the default value for the identity type if its still not existing
      // in the mapping
      if (StringUtility.isEmpty((String)data.get(this.identityType)))
        data.put(this.identityType, this.identityTypeDefault);

      // apply the default value for the identity role if its still not existing
      // in the mapping
      if (StringUtility.isEmpty((String)data.get(this.identityRole)))
        data.put(this.identityRole, this.identityRoleDefault);

      // apply the default value for the identity status if its still not
      // existing in the mapping
      switch (identity.action()) {
        case create  : 
        case modify  : data.put(this.identityStatus, this.identityStatusDefault);
                       break;
        case enable  : data.put(this.identityStatus, User.STATUS_ACTIVE);
                       break;
        case disable : data.put(this.identityStatus, User.STATUS_DISABLED);
                       break;
      }
      if (identity.action() == Identity.Action.delete)
        // create a reconciliation dlete event which have all data provided,
        // means no further child data processing necessary
        processDeleteEvent(data);
      else
        // create a reconciliation event which have all data provided, means no
        // further child data processing necessary
        processRegularEvent(data);
    }
    catch (SystemException e) {
      throw TaskException.general(e);
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processRoles
  /**
   ** Revokes and/or assigne roles form and/or to the specifed {@link Identity}.
   **
   ** @param  identity           the {@link Identity} to handle in the namespace
   **                            of roles.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void processRoles(final Identity identity)
    throws TaskException {

    final String recipient = lookupIdentity(identity.name());
    final List<RoleEntity> roleToRevoke = identity.roleToRevoke();
    if (!CollectionUtility.empty(roleToRevoke))
      revokeRoles(recipient, roleToRevoke);

    final List<RoleEntity> roleToAssign = identity.roleToAssign();
    if (!CollectionUtility.empty(roleToAssign))
      assignRoles(recipient, roleToAssign);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRoles
  /**
   ** Assignes {@link RoleEntity}'s to the specifed {@link Identity}.
   **
   ** @param  identity           the {@link Identity} to assign the
   **                            {@link RoleEntity}'s to.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private void assignRoles(final String identity, final Collection<RoleEntity> role)
    throws TaskException {

    final Set<String> recipient = new HashSet<String>(1);
    final RoleManager facade    = service(RoleManager.class);

    final String method = "assignRoles";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    recipient.add(identity);
    for (RoleEntity entity : role) {
      final String roleKey = lookupRole(entity.name());
      if (!StringUtility.isEmpty(roleKey))
        try {
          facade.grantRole(roleKey, recipient);
        }
        catch (Exception e) {
          fatal(method, e);
        }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeRoles
  /**
   ** Revokes {@link RoleEntity}'s form the specifed {@link Identity}.
   **
   ** @param  identity           the {@link Identity} to revoke the
   **                            {@link RoleEntity}'s from.
   **
   ** @throws TaskException      in case an error does occur.
   */
  private void revokeRoles(final String identity, final Collection<RoleEntity> role)
    throws TaskException {

    final Set<String> recipient = new HashSet<String>(1);
    final RoleManager facade    = service(RoleManager.class);

    final String method = "revokeRoles";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    recipient.add(identity);
    for (RoleEntity entity : role) {
      final String roleKey = lookupRole(entity.name());
      if (!StringUtility.isEmpty(roleKey))
        try {
          facade.revokeRoleGrant(roleKey, recipient);
        }
        catch (ValidationFailedException e) {
          fatal(method, e);
        }
        catch (RoleGrantRevokeException e) {
          fatal(method, e);
        }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateOrganization
  /**
   ** Check for the existance of an organization.
   ** <p>
   ** If the organization does not exists the <code>deafultOrganization</code>
   ** will be returned.
   **
   ** @param  organization        the name of the Oracle Identity Manager
   **                             organization to validate.
   ** @param  defaultOrganization the name of the organization that will be
   **                             returned if the specified
   **                             <code>organization</code> does not exists.
   **
   ** @return                     the name of the organization to use as a
   **                             home organization.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected String validateOrganization(final String organization, final String defaultOrganization)
    throws TaskException {

    // prevent bogus input
    if (StringUtility.isEmpty(organization))
      return defaultOrganization;

    try {
      this.filter.clear();
      this.filter.put(Organization.NAME, organization);
      tcResultSet resultSet = organizationFacade().findOrganizations(filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { this.identityOrganization, organization, defaultOrganization};
        warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default organization
        return defaultOrganization;
      }
      else
        return organization;
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
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
   ** Lookups the identifier of a {@link Role} from Oracle Identity Manager by
   ** its name.
   ** <p>
   **
   ** @param  identifier         the name of a {@link Role} to lookup.
   **
   ** @return                    the identifier of the {@link Role} that match
   **                            the name or <code>null</code> if no
   **                            {@link Role} match the specified name.
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
      if (resultSet.next())
        id = resultSet.getString(1);
      if (resultSet.wasNull())
        id = null;
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
}