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

    File        :   OrganizationTrustedReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OrganizationTrustedReconciliation.


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

import oracle.iam.conf.vo.SystemProperty;

import oracle.iam.conf.api.SystemConfigurationService;

import oracle.iam.conf.exception.SystemConfigurationServiceException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.naming.Lookup;
import oracle.iam.identity.foundation.naming.LookupValue;
import oracle.iam.identity.foundation.naming.Organization;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.utility.file.CSVDescriptor;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationTrustedReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrganizationTrustedReconciliation</code> implements the base
 ** functionality of a service end point for the Oracle Identity Manager
 ** Scheduler which handles organization data provided by CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class OrganizationTrustedReconciliation extends TrustedReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** parent organization name should assigned to a new created organization.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String ORGANIZATION_PARENT      = "Organization Parent";

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** organization type should assigned to a new created organization.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String ORGANIZATION_TYPE        = "Organization Type";

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** organization status should assigned to a new created organization.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String ORGANIZATION_STATUS     = "Organization Status";

  /**
   ** The System Configuration Property that will be checked for proper
   ** configuration to execute this job successfully.
   ** <p>
   ** If this value provided by this property is configured as
   ** <code>false</code> all action executed by this job will hence the job
   ** will stop with exception if this property is not configured as
   ** <code>true</code>.
   */
  private static final String ORGANIZATION_ACTION      = "ORG.DisableDeleteActionEnabled";

  /**
   ** the array with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation target */
    TaskAttribute.build(RECONCILE_OBJECT,          Organization.RESOURCE)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR,      TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,                 TaskAttribute.MANDATORY)
    /** the default parent organization name of the on-boarded organization if no parent supplied by the data  */
  , TaskAttribute.build(ORGANIZATION_PARENT,       SystemConstant.EMPTY)
    /** the default organization type of the on-boarded organization if no type supplied by the data  */
  , TaskAttribute.build(ORGANIZATION_TYPE,         Organization.DEFAULT_TYPE)
    /** the default organization status of the on-boarded organization if no type supplied by the data  */
  , TaskAttribute.build(ORGANIZATION_STATUS,       Organization.STATUS_ACTIVE)
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

  private String                        defaultType;
  private String                        defaultStatus;
  private String                        defaultParent;
  private boolean                       deleteEvents = false;

  private String                        profileType;
  private String                        profileParent;
  private String                        profileStatus;

  private tcLookupOperationsIntf       lookup;
  private tcOrganizationOperationsIntf organization;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OrganizationReconciliation</code> task adapter
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OrganizationTrustedReconciliation() {
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
   ** This will do trusted reconciliation of Oracle Identity Manager
   ** Organizations.
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

    String[] parameter = {TaskBundle.string(TaskMessage.ENTITY_ORGANIZATION), identifier };
    // produce the logging output only if the logging level is enabled for
    if (this.logger().debugLevel())
      debug(method, TaskBundle.format(TaskMessage.ENTITY_RECONCILE, parameter));

    // set organization type to default if no one specified
    if (StringUtility.isEmpty((String)data.get(this.profileType)))
      data.put(this.profileType, this.defaultType);

    // set status to default if no one specified
    if (StringUtility.isEmpty((String)data.get(this.profileStatus)))
      data.put(this.profileStatus, this.defaultStatus);

    // get some common fields that should be part of the data
    final String parentOrganization = (String)data.remove(this.profileParent);
    data.put(this.profileParent, validateOrganization(parentOrganization, this.defaultParent));
    try {
      // handle deletion at first
      if (CSVDescriptor.DELETE.equalsIgnoreCase(transaction)) {
        // check at first if the required action can be executed successful
        if (!this.deleteEvents)
          throw new TaskException(TaskError.PROPERTY_VALUE_CONFIGURATION, ORGANIZATION_ACTION);

        // create the reconciliation event for deletion
        processDeleteEvent(data);
      }
      else {
        // create a reconciliation event which have all data provided, means
        // no further child data processing necessary
        processRegularEvent(data);
      }
    }
    catch (SystemException e) {
      incrementFailed();
      throw TaskException.general(e);
    }
    catch (Exception e) {
      incrementFailed();
      throw TaskException.unhandled(e);
    }
    finally {
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

    this.organization     = service(tcOrganizationOperationsIntf.class);
    this.lookup           = lookupFacade();
    tcResultSet resultSet = null;

    this.profileType      = this.descriptor.organizationType();
    this.profileParent    = this.descriptor.organizationParent();
    this.profileStatus    = this.descriptor.organizationStatus();

     // check if the specified user type exists; if not create use the default
    this.defaultType = stringValue(ORGANIZATION_TYPE);
    filter.clear();
    filter.put(LookupValue.ENCODED, this.defaultType);
    try {
      resultSet = lookup.getLookupValues(Lookup.ORGANIZATION_TYPE, filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { ORGANIZATION_TYPE, Lookup.ORGANIZATION_TYPE, this.defaultType };
        warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default organization
        this.defaultType = Organization.DEFAULT_TYPE;
      }
    }
    catch (tcInvalidLookupException e) {
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.ORGANIZATION_TYPE);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }

    // check if the specified identity status exists; if not assign the default
    this.defaultStatus = stringValue(ORGANIZATION_STATUS);
    try {
      this.filter.clear();
      this.filter.put(LookupValue.ENCODED, this.defaultStatus);
      resultSet = this.lookup.getLookupValues(Lookup.ORGANIZATION_STATUS, this.filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { ORGANIZATION_STATUS, Lookup.ORGANIZATION_STATUS, this.defaultStatus };
        warning(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default role
        this.defaultStatus = Organization.STATUS_ACTIVE;
      }
    }
    catch (tcInvalidLookupException e) {
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.ORGANIZATION_STATUS);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }

    this.defaultParent = validateOrganization(stringValue(ORGANIZATION_PARENT), Organization.DEFAULT_NAME);

    // check if the system is configured properly by reading the system
    // configuration property 'ORG.DisableDeleteActionEnabled'
    // if this value is configured as false all action executed by this job will
    // fail hence we can stop here if this property is not configured as true
    final SystemConfigurationService service = service(SystemConfigurationService.class);
    try {
      final SystemProperty property = service.getSystemProperty(ORGANIZATION_ACTION);
      if (property != null)
        this.deleteEvents = SystemConstant.TRUE.equalsIgnoreCase(property.getPtyValue());
    }
    catch (SystemConfigurationServiceException e) {
      throw new TaskException(TaskError.PROPERTY_NOTEXISTS, ORGANIZATION_ACTION);
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

    this.organization = null;

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateOrganization
  /**
   ** Check for the existance of an organization.
   ** <p>
   ** If the organization does not exists the <code>defaultOrganization</code>
   ** will be returned.
   **
   ** @param  organization        the name of the Oracle Identity Manager
   **                             organization to validate.
   ** @param  defaultOrganization the name of the organization that will be
   **                             returned if the specified
   **                             <code>organization</code> does not exists.
   **
   ** @return                     the name of the organization to use as a
   **                             parent organization.
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
      tcResultSet resultSet = organizationFacade().findOrganizations(this.filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { this.profileParent, organization,  defaultOrganization};
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