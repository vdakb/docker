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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Database Account Connector

    File        :   TrustedReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TrustedReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.service.reconciliation;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;

import Thor.API.Operations.tcLookupOperationsIntf;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.User;
import oracle.iam.identity.foundation.naming.Lookup;
import oracle.iam.identity.foundation.naming.LookupValue;
import oracle.iam.identity.foundation.naming.Organization;

////////////////////////////////////////////////////////////////////////////////
// class TrustedReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TrustedReconciliation</code> acts as the service end point for the
 ** Oracle Identity Manager to reconcile account information from a Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class TrustedReconciliation extends AccountReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify which lookup
   ** definition defines the set of dataobjects a new created group has access
   ** to.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String ACCOUNT_PASSWORD   = "Account Password";

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
  protected static final String IDENTITY_STATUS     = "Identity Status";

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
    /** the task attribute to specify the organization for on-boarded users */
  , TaskAttribute.build(IDENTITY_ORGANIZATION, TaskAttribute.MANDATORY)
    /** the task attribute to specify the role for on-boarded users */
  , TaskAttribute.build(IDENTITY_TYPE,         TaskAttribute.MANDATORY)
    /** the task attribute to specify the emploxee for on-boarded users */
  , TaskAttribute.build(IDENTITY_ROLE,         TaskAttribute.MANDATORY)
    /** the task attribute to specify the excelude accounts */
  , TaskAttribute.build(EXCLUDED_USER)
    /** the task attribute to specify the password for on-boarded users */
  , TaskAttribute.build(ACCOUNT_PASSWORD)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the value holder for the fieldnames of mandatory attributes in Oracle
  // Identity Manager
  private String           fieldIdentityLogin;
  private String           fieldIdentityStatus;
  private String           fieldIdentityRole;
  private String           fieldIdentityType;
  private String           fieldIdentityOrganization;

  // the value holder for the defaults of mandatory attributes in Oracle
  // Identity Manager
  private String           defaultIdentityStatus;
  private String           defaultIdentityRole;
  private String           defaultIdentityType;
  private String           defaultIdentityOrganization;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TrustedReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TrustedReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

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

    this.fieldIdentityLogin        = this.descriptor.identityLogin();
    this.fieldIdentityStatus       = this.descriptor.identityStatus();
    this.fieldIdentityRole         = this.descriptor.identityRole();
    this.fieldIdentityType         = this.descriptor.identityType();
    this.fieldIdentityOrganization = this.descriptor.identityOrganization();

    this.defaultIdentityOrganization = stringValue(IDENTITY_ORGANIZATION);
    // check if the specified organization exists; if not create use the default
    this.defaultIdentityOrganization = validateOrganization(stringValue(IDENTITY_ORGANIZATION), Organization.DEFAULT_NAME);

    tcLookupOperationsIntf lookup    = lookupFacade();
    tcResultSet            resultSet = null;
    try {
      final Map<String, String>    filter = new HashMap<String, String>();
      // check if the specified identity role exists; if not assign the default
      this.defaultIdentityRole = stringValue(IDENTITY_ROLE);
      try {
        filter.clear();
        filter.put(LookupValue.ENCODED, this.defaultIdentityRole);
        resultSet = lookup.getLookupValues(Lookup.IDENTITY_ROLE, filter);
        if (resultSet.getRowCount() == 0) {
          String[] parameter = { IDENTITY_ROLE, this.fieldIdentityRole, defaultIdentityRole };
          warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
          // override with default role
          this.defaultIdentityRole = User.DEFAULT_ROLE;
        }
      }
      catch (tcInvalidLookupException e) {
        throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.IDENTITY_ROLE);
      }
      catch (tcAPIException e) {
        throw new TaskException(e);
      }

      // check if the specified identity type exists; if not assign the default
      this.defaultIdentityType = stringValue(IDENTITY_TYPE);
      try {
        filter.clear();
        filter.put(LookupValue.ENCODED, this.defaultIdentityType);
        resultSet = lookup.getLookupValues(Lookup.IDENTITY_TYPE, filter);
        if (resultSet.getRowCount() == 0) {
          String[] parameter = { IDENTITY_TYPE, this.fieldIdentityType, defaultIdentityType};
          warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
          // override with default type
          this.defaultIdentityType = User.DEFAULT_TYPE;
        }
      }
      catch (tcInvalidLookupException e) {
        throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.IDENTITY_TYPE);
      }
      catch (tcAPIException e) {
        throw new TaskException(e);
      }

      // check if the specified identity status exists; if not assign the default
      this.defaultIdentityStatus = stringValue(IDENTITY_STATUS);
      try {
        filter.clear();
        filter.put(LookupValue.ENCODED, this.defaultIdentityStatus);
        resultSet = lookup.getLookupValues(Lookup.IDENTITY_STATUS, filter);
        if (resultSet.getRowCount() == 0) {
          String[] parameter = { IDENTITY_STATUS, this.fieldIdentityStatus, this.defaultIdentityStatus };
          warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
          // override with default role
          this.defaultIdentityRole = User.STATUS_ACTIVE;
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
      lookup.close();
    }
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

    final Map<String, String> filter = new HashMap<String, String>();
    try {
      filter.clear();
      filter.put(Organization.NAME, organization);
      tcResultSet resultSet = organizationFacade().findOrganizations(filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { this.fieldIdentityOrganization, organization, defaultOrganization};
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
  // Method:   processSubject
  /**
   ** Do all action which should take place for trusted reconciliation for an
   ** particular subject.
   **
   ** @param  username           the name of the user to reconcile trusted.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected void processSubject(String username)
    throws TaskException {

    // check if the current thread is able to execute or a stop signal
    // is pending
    if (isStopped())
      return;

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map<String, Object> master = this.connection.accountDetail(username, returningAttributes());
    master = transformMaster(createMaster(master, false));

    // Whether the organization with the name exists or no organization
    // contained in the provided data substitute the organization name
    // with the default organization name
    master.put(this.fieldIdentityOrganization, validateOrganization((String)master.get(this.fieldIdentityOrganization), this.defaultIdentityOrganization));

    // apply the default value for the identity type if its still not existing
    // in the mapping
    if (StringUtility.isEmpty((String)master.get(this.fieldIdentityType)))
      master.put(this.fieldIdentityType, this.defaultIdentityType);

    // apply the default value for the identity role if its still not existing
    // in the mapping
    if (StringUtility.isEmpty((String)master.get(this.fieldIdentityRole)))
      master.put(this.fieldIdentityRole, this.defaultIdentityRole);

    // apply the default value for the identity status if its still not
    // existing in the mapping
    if (StringUtility.isEmpty((String)master.get(this.fieldIdentityStatus)))
      master.put(this.fieldIdentityStatus, this.defaultIdentityStatus);

    try {
      // create a reconciliation event which have all data provided, means
      // no further child data processing necessary
      processRegularEvent(master);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}