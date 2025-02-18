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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Directory Service Connector

    File        :   AccountTrustedReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountTrustedReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    0.0.0.2      2012-11-09  DSteding    Fixed DE-000040
                                         Log the identifier of the identity
                                         being reconciled
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.reconciliation;

import java.util.List;
import java.util.Map;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;

import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;

import oracle.hst.foundation.SystemMessage;
import  oracle.hst.foundation.SystemConstant;

import  oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.User;
import oracle.iam.identity.foundation.naming.Lookup;
import oracle.iam.identity.foundation.naming.LookupValue;
import oracle.iam.identity.foundation.naming.Organization;

import oracle.iam.identity.foundation.ldap.DirectoryName;
import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;

////////////////////////////////////////////////////////////////////////////////
// class AccountTrustedReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountTrustedReconciliation</code> acts as the service end point
 ** for the Oracle Identity Manager to reconcile user information from a
 ** Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AccountTrustedReconciliation extends ObjectReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify that the
   ** organization should be derived from the distinguished name of the
   ** account entry.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String MAINTAIN_HIERARCHY    = "Maintain Hierarchy";

  /**
   ** Attribute tag which may be defined on this task to specify which default
   ** organization should a onboarded user be assign if the loaded organization
   ** could not be resolved.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String IDENTITY_ORGANIZATION = "Identity Organization";

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** role name should assigned to a onboarded user if no value is loaded from
   ** the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String IDENTITY_ROLE         = "Identity Role";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** identity type value should assigned to a onboarded user if no value is
   ** loaded from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String IDENTITY_TYPE         = "Identity Type";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** identity status value should assigned to a onboarded user if no value is
   ** loaded from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String IDENTITY_STATUS       = "Identity Status";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,           TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,      TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR,  TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,             TaskAttribute.MANDATORY)
    /** the task attribute to specify the default role for on-boarded users */
  , TaskAttribute.build(IDENTITY_ROLE,         TaskAttribute.MANDATORY)
    /** the task attribute to specify the default type for on-boarded users */
  , TaskAttribute.build(IDENTITY_TYPE,         TaskAttribute.MANDATORY)
    /** the task attribute to specify the default status for on-boarded users */
  , TaskAttribute.build(IDENTITY_STATUS,       TaskAttribute.MANDATORY)
    /**
     ** the task attribute to specify that the organizational hierarchy should
     ** be resolved
     */
  , TaskAttribute.build(MAINTAIN_HIERARCHY,    TaskAttribute.MANDATORY)
    /** the task attribute to specify the default organization for on-boarded users */
  , TaskAttribute.build(IDENTITY_ORGANIZATION, TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies the search scope of the query
     ** <br>
     ** Allowed values are OneLevel | SubTree | Object
     */
  , TaskAttribute.build(SEARCHSCOPE,          TaskAttribute.MANDATORY)
    /** the task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCHFILTER,          TaskAttribute.MANDATORY)
    /** the task attribute that specifies the search base of the query */
  , TaskAttribute.build(SEARCHBASE,            SystemConstant.EMPTY)
    /** the task attribute that specifies the sort order applied to the query */
  , TaskAttribute.build(SEARCHORDER,           SystemConstant.EMPTY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,           SystemConstant.TRUE)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the value holder for the fieldnames of mandatory attributes in Oracle
  // Identity Manager
  private String                       profileStatus;
  private String                       profileRole;
  private String                       profileType;
  private String                       profileOrganization;
  private String                       profileManager;

  // the value holder for the defaults of mandatory attributes in Oracle
  // Identity Manager
  private String                       profileStatusDefault;
  private String                       profileRoleDefault;
  private String                       profileTypeDefault;
  private String                       profileOrganizationDefault;

  private tcLookupOperationsIntf       lookup;
  private tcOrganizationOperationsIntf organization;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountTrustedReconciliation</code> scheduled
   ** task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountTrustedReconciliation() {
    // ensure inheritance
    super(DirectoryConstant.ACCOUNT_MULTIVALUE_ATTRIBUTE);
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
  protected final TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do target reconciliation of Oracle Identity Manager Users.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected final void processSubject(Map<String, Object> subject)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // to improve performance by make a local reference to all the attributes
    // retrieved more than once from the associated connector
    final DirectoryConnector connector = this.connector();

    // extracts the known valus of the account to reconcile here
    // it will be no longer available after the attribute mapping has proceed
    final String         accountDN   = (String)subject.remove(connector.distinguishedNameAttribute());
    final List<String[]> component   = DirectoryName.explode(accountDN);
    final String[]       accountName = component.remove(0);

    try {
      // filter data in a new mapping
      Map<String, Object> data = createMaster(subject, false);

      // check if we have to maintain the DIT Hierarchy and transform it to the
      // organization
      if (booleanValue(MAINTAIN_HIERARCHY))
        data.put(this.profileOrganization, ((String[])component.remove(0))[1]);

      // apply transformation if needed
      data = transformMaster(data);

      // Whether the organization with the name exists or no organization is
      // provided in the data substitute the organization name with the default
      // organization name
      data.put(this.profileOrganization, validateOrganization((String)data.get(this.profileOrganization), this.profileOrganizationDefault));

      // apply the default value for the identity type if its still not existing
      // in the mapping
      if (StringUtility.isEmpty((String)data.get(this.profileType)))
        data.put(this.profileType, this.profileTypeDefault);

      // apply the default value for the identity role if its still not existing
      // in the mapping
      if (StringUtility.isEmpty((String)data.get(this.profileRole)))
        data.put(this.profileRole, this.profileRoleDefault);

      // apply the default value for the identity status if its still not
      // existing in the mapping
      if (StringUtility.isEmpty((String)data.get(this.profileStatus)))
        data.put(this.profileStatus, this.profileStatusDefault);

      // create a reconciliation event which have all data provided, means
      // no further child data processing necessary
      processRegularEvent(data);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
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

    final String method  = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      this.lookup              = this.lookupFacade();
      this.organization        = this.organizationFacade();

      this.profileStatus       = this.descriptor.identityStatus();
      this.profileRole         = this.descriptor.identityRole();
      this.profileType         = this.descriptor.identityType();
      this.profileOrganization = this.descriptor.identityOrganization();
      this.profileManager      = this.descriptor.identityManager();

      this.profileOrganizationDefault = stringValue(IDENTITY_ORGANIZATION);
      // check if the specified organization exists; if not create use the default
      this.profileOrganizationDefault = validateOrganization(stringValue(IDENTITY_ORGANIZATION), Organization.DEFAULT_NAME);

      tcResultSet resultSet        = null;
      // check if the specified identity role exists; if not assign the default
      this.profileRoleDefault = stringValue(IDENTITY_ROLE);
      try {
        this.filter.clear();
        this.filter.put(LookupValue.ENCODED, this.profileRoleDefault);
        resultSet = this.lookup.getLookupValues(Lookup.IDENTITY_ROLE, this.filter);
        if (resultSet.getRowCount() == 0) {
          String[] parameter = { IDENTITY_ROLE, this.profileRole, this.profileRoleDefault };
          warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
          // override with default role
          this.profileRoleDefault = User.DEFAULT_ROLE;
        }
      }
      catch (tcInvalidLookupException e) {
        throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.IDENTITY_ROLE);
      }
      catch (tcAPIException e) {
        throw new TaskException(e);
      }

      // check if the specified identity type exists; if not assign the default
      this.profileTypeDefault = stringValue(IDENTITY_TYPE);
      try {
        this.filter.clear();
        this.filter.put(LookupValue.ENCODED, this.profileTypeDefault);
        resultSet = this.lookup.getLookupValues(Lookup.IDENTITY_TYPE, this.filter);
        if (resultSet.getRowCount() == 0) {
          String[] parameter = { IDENTITY_TYPE, this.profileType, this.profileTypeDefault};
          warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
          // override with default type
          this.profileTypeDefault = User.DEFAULT_TYPE;
        }
      }
      catch (tcInvalidLookupException e) {
        throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.IDENTITY_TYPE);
      }
      catch (tcAPIException e) {
        throw new TaskException(e);
      }

      // check if the specified identity status exists; if not assign the default
      this.profileStatusDefault = stringValue(IDENTITY_STATUS);
      try {
        this.filter.clear();
        this.filter.put(LookupValue.ENCODED, this.profileStatusDefault);
        resultSet = this.lookup.getLookupValues(Lookup.IDENTITY_STATUS, this.filter);
        if (resultSet.getRowCount() == 0) {
          String[] parameter = { IDENTITY_STATUS, this.profileStatus, this.profileStatusDefault };
          warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
          // override with default role
          this.profileStatusDefault = User.STATUS_ACTIVE;
        }
      }
      catch (tcInvalidLookupException e) {
        throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.IDENTITY_STATUS);
      }
      catch (tcAPIException e) {
        throw new TaskException(e);
      }
    }
    finally {
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

    this.lookup.close();
    this.lookup = null;

    this.organization.close();
    this.organization = null;

    // ensure inheritance
    super.afterExecution();
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
   ** @throws TaskException       in case an error does occur.
   */
  protected String validateOrganization(final String organization, final String defaultOrganization)
    throws TaskException {

    // prevent bogus input
    if (StringUtility.isEmpty(organization))
      return defaultOrganization;

    final String method  = "validateOrganization";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      this.filter.clear();
      this.filter.put(Organization.NAME, organization);
      tcResultSet resultSet = organizationFacade().findOrganizations(filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { this.profileOrganization, organization, defaultOrganization};
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
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}