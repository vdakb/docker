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
    Subsystem   :   CSV Flatfile Connector

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
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.service.reconciliation;

import java.util.Map;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;

import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.User;
import oracle.iam.identity.foundation.naming.Lookup;
import oracle.iam.identity.foundation.naming.LookupValue;
import oracle.iam.identity.foundation.naming.Organization;

import oracle.iam.identity.utility.file.CSVDescriptor;

////////////////////////////////////////////////////////////////////////////////
// class AccountTrustedReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountTrustedReconciliation</code> implements the base
 ** functionality of a service end point for the Oracle Identity Manager
 ** Scheduler which handles user data provided by CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AccountTrustedReconciliation extends TrustedReconciliation {

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
  private static final TaskAttribute[] attribute    = {
    /** the task attribute with reconciliation target */
    TaskAttribute.build(RECONCILE_OBJECT,          TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR,      TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,                 TaskAttribute.MANDATORY)
    /** the default organization of the on-boarded user if no type supplied by the data  */
  , TaskAttribute.build(IDENTITY_ORGANIZATION,     TaskAttribute.MANDATORY)
    /** the default user type of the on-boarded user if no type supplied by the data  */
  , TaskAttribute.build(IDENTITY_ROLE,             TaskAttribute.MANDATORY)
    /** the default employee type of the on-boarded user if no type supplied by the data  */
  , TaskAttribute.build(IDENTITY_TYPE,             TaskAttribute.MANDATORY)
    /** the task attribute to specify the default status for on-boarded users */
  , TaskAttribute.build(IDENTITY_STATUS,           TaskAttribute.MANDATORY)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,               TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,              TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are successfully proceeded */
  , TaskAttribute.build(WORKING_FOLDER,            TaskAttribute.MANDATORY)
    /** the fullqualified filename which are specifying the mapping for import  */
  , TaskAttribute.build(DATA_DESCRIPTOR,           TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATA_FILE,                 TaskAttribute.MANDATORY)
    /** the filename of the proceeded data  */
  , TaskAttribute.build(PROCEED_FILE,              TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,             TaskAttribute.MANDATORY)
    /** the character to separate single values */
  , TaskAttribute.build(SINGLE_VALUED_SEPARATOR,   TaskAttribute.MANDATORY)
    /** the character to separate single values */
  , TaskAttribute.build(MULTI_VALUED_SEPARATOR,    TaskAttribute.MANDATORY)
    /** the flag that indicates that the files should be compared  */
  , TaskAttribute.build(INCREMENTAL,               TaskAttribute.MANDATORY)
    /** the character to enclose values */
  , TaskAttribute.build(ENCLOSING_CHARACTER,       "\"")
  , TaskAttribute.build(RETRY_REMAINING_ENTRIES,   RETRY_REMAINING_ENTRIES_DEFAULT)
  , TaskAttribute.build(RETRY_REMAINING_THRESHOLD, RETRY_REMAINING_THRESHOLD_DEFAULT)
  };

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
   ** Constructs an empty <code>AccountTrustedReconciliation</code> task adapter
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountTrustedReconciliation() {
    // ensure inheritance
    super();
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
  // Method:   processSubject (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do trusted reconciliation of Oracle Identity Manager Users.
   **
   ** @param  identifier         the composed identifier of the {@link Map} to
   **                            reconcile.
   ** @param  transaction        the transaction code of the {@link Map} to
   **                            reconcile.
   ** @param  data               the {@link Map} to reconcile.
   ** @param  pass               the pass the subjec ist loaded from the file.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected final void processSubject(final String identifier, final String transaction, final Map<String, Object> data, final int pass)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = {TaskBundle.string(TaskMessage.ENTITY_IDENTITY), identifier };
    // produce the logging output only if the logging level is enabled for
    if (this.logger().debugLevel())
      debug(method, TaskBundle.format(TaskMessage.ENTITY_RECONCILE, parameter));

    try {
      //  verify if the identity organization is part of the data feed
      if (!StringUtility.isEmpty(this.identityOrganization)) {
        // whether the organization with the name exists or no organization
        // contained in the provided data substitute the organization name
        // with the default organization name
        data.put(this.identityOrganization, validateOrganization((String)data.get(this.identityOrganization), this.identityOrganizationDefault));
      }

      //  verify if the identity type is part of the data feed
      if (!StringUtility.isEmpty(this.identityType)) {
        // apply the default value for the identity type if its still not
        // existing in the mapping
        if (StringUtility.isEmpty((String)data.get(this.identityType)))
          data.put(this.identityType, this.identityTypeDefault);
      }

      //  verify if the identity role is part of the data feed
      if (!StringUtility.isEmpty(this.identityRole)) {
        // apply the default value for the identity role if its still not
        // existing in the mapping
        if (StringUtility.isEmpty((String)data.get(this.identityRole)))
          data.put(this.identityRole, this.identityRoleDefault);
      }

      //  verify if the identity status is part of the data feed
      if (!StringUtility.isEmpty(this.identityStatus)) {
        // apply the default value for the identity status if its still not
        // existing in the mapping
        if (StringUtility.isEmpty((String)data.get(this.identityStatus)))
           data.put(this.identityStatus, this.identityStatusDefault);
      }

      // handle deletion at first
      if (CSVDescriptor.DELETE.equalsIgnoreCase(transaction)) {
        // create the reconciliation event for deletion
        deleteEvent(data);
      }
      else {
        // if no manager is specified it is a good practice to remove
        // unnecessary mappings
        final String managerLogin = (String)data.get(this.identityManager);
        if (!StringUtility.isEmpty(managerLogin)) {
          // we will try to resolve the dependencies only as long the
          // threshold value to skip this operation is not reached
          // the file is in sequence after the first time the file has loaded
          if (CSVDescriptor.CREATE.equals(transaction) && pass < integerValue(RETRY_REMAINING_THRESHOLD)) {
            // reset attribute container
            filter.clear();
            // at second check if the manager login exists; if not push back
            filter.put(User.USERID, managerLogin);
            tcResultSet resultSet = this.user.findUsers(filter);
            if (resultSet.getRowCount() == 0) {
              String[] missing = { User.FIELD_MANAGER_LOGIN, parameter[1], managerLogin };
              warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, missing));
              // pushback the subject if at this time dependency exists that
              // cannot be resolved
              pushBack(parameter[1], managerLogin, data);
              return;
            }
          }
        }
        // create a reconciliation event which have all data provided, means
        // no further child data processing necessary
        processRegularEvent(data);
      }
    }
    catch (SystemException e) {
      throw TaskException.general(e);
    }
    catch (Exception e) {
      throw TaskException.unhandled(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waitForEventProceeed (Reconciliation)
  /**
   ** Waits with fursther reconciliation actions to the time the specified
   ** subject is reconciled through the back office engine.
   **
   ** @param  eventKey           the key of the event to wait for
   */
/*
  protected void waitForEventProceeed(long eventKey)
    throws SystemException {

    filter.clear();
    filter.put(ReconciliationManager.KEY, Long.toString(eventKey));
    waitForEventProceeed(filter);
  }
*/
  //////////////////////////////////////////////////////////////////////////////
  // Method:   waitForEventProceeed (Reconciliation)
  /**
   ** Waits with fursther reconciliation actions to the time the specified
   ** subject is reconciled through the back office engine.
   **
   ** @param  identifier         the identifying data of the event to wait for
   */
/*
  protected void waitForEventProceeed(Map identifier)
    throws SystemException {

    final String method = "waitForEventProceeed";
    trace(method, SystemMessage.METHOD_ENTRY);

    // create a string from current date without a time
    final SimpleDateFormat eventDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    final String           eventDate          = eventDateFormatter.format(new Date());

    try {
      while (true) {
        // chack if the operator requested to stop further execution of the task
        if (isStopped())
          break;
        // causes the currently executing thread to sleep to give the
        // offline event engine a chance to link the events
        sleep();
        // retirve the current status of the event
        tcResultSet resultSet = this.facade().findReconciliationEvent(identifier, eventDate, eventDate);
        // the specified event does not exists
        if (resultSet.getRowCount() == 0)
          break;
        // the specified event exists more than once
        if (resultSet.getRowCount() > 1)
          break;

        resultSet.goToRow(0);
        final String status = resultSet.getStringValue(ReconciliationManager.STATUS);
        // if we hav a status that will not end up with a successful reconciliation
        // or the event is linked already break the loop
        if (status.equals("Required Data Missing") || status.equals("No Match Found") || status.equals("Event Linked"))
          break;
      }
    }
    catch (tcColumnNotFoundException e) {
      stopExecution();
      throw new SystemException(e);
    }
    catch (tcAPIException e) {
      stopExecution();
      throw new SystemException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
*/

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
    this.identityOrganizationDefault = validateOrganization(stringValue(IDENTITY_ORGANIZATION), Organization.DEFAULT_NAME);

    tcResultSet resultSet     = null;
    // check if the specified identity role exists; if not assign the default
    this.identityRoleDefault = stringValue(IDENTITY_ROLE);
    try {
      this.filter.clear();
      this.filter.put(LookupValue.ENCODED, this.identityRoleDefault);
      resultSet = this.lookup.getLookupValues(Lookup.IDENTITY_ROLE, this.filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { IDENTITY_ROLE, this.identityRole, this.identityRoleDefault };
        warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default role
        this.identityRoleDefault = User.DEFAULT_ROLE;
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
      resultSet = this.lookup.getLookupValues(Lookup.IDENTITY_TYPE, this.filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { IDENTITY_TYPE, this.identityType, this.identityTypeDefault};
        warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default type
        this.identityTypeDefault = User.DEFAULT_TYPE;
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
}