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
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.reconciliation;

import java.util.Map;
import java.util.Collection;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.naming.Lookup;
import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.OrganizationEntity;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationTrustedReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrganizationTrustedReconciliation</code> acts as the service end
 ** point for the Oracle Identity Manager to reconcile organizational
 ** information from a offlined XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class OrganizationTrustedReconciliation extends EntityReconciliation<OrganizationEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** parent organization name should assigned to a new created organization.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String        ORGANIZATION_PARENT = "Parent Organizational Name";

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** organization type name should assigned to a new created organization.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String        ORGANIZATION_TYPE   = "Organizational Type";

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** organization status should assigned to a new created organization.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String        ORGANIZATION_STATUS = "Organizational Status";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation target */
    TaskAttribute.build(RECONCILE_OBJECT,     TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /**
     ** the default organization parent of the on-boarded organization if no
     ** superior supplied by the data
     */
  , TaskAttribute.build(ORGANIZATION_PARENT,  TaskAttribute.MANDATORY)
    /**
     ** the default organization type of the on-boarded organization if no type
     ** supplied by the data
     */
  , TaskAttribute.build(ORGANIZATION_TYPE,    oracle.iam.identity.foundation.naming.Organization.DEFAULT_TYPE)
    /**
     ** the default organization type of the on-boarded organization if no type
     ** supplied by the data
     */
  , TaskAttribute.build(ORGANIZATION_STATUS,  oracle.iam.identity.foundation.naming.Organization.STATUS_ACTIVE)
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

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String defaultParent;
  protected String defaultType;
  protected String defaultStatus;

  private String   profileParent;
  private String   profileType;
  private String   profileStatus;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OrganizationTrustedReconciliation</code>
   ** scheduled task that allows use as a JavaBean.
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
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process (EntityListener)
  /**
   ** Reconciles a particular bulk of {@link OrganizationEntity}'s.
   ** <br>
   ** This will do trusted reconciliation of Oracle Identity Manager
   ** Organizations.
   **
   ** @param  bulk               the {@link Collection} of
   **                            {@link OrganizationEntity}'s to reconcile.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  public void process(final Collection<OrganizationEntity> bulk)
    throws TaskException {

    final String method = "process";
    trace(method, SystemMessage.METHOD_ENTRY);

    // validate the effort to do
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(bulk.size())));
    try {
      if (gatherOnly()) {
        incrementIgnored(bulk.size());
        warning(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
      }
      else {
        for (OrganizationEntity organization : bulk) {
          if (isStopped())
            break;

          reconcile(organization);
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
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
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

    this.profileStatus = this.descriptor.organizationStatus();
    this.profileType   = this.descriptor.organizationType();
    this.profileParent = this.descriptor.organizationParent();

    // check if the specified organization exists; if not use the default
    this.defaultParent = validateOrganization(stringValue(ORGANIZATION_PARENT), oracle.iam.identity.foundation.naming.Organization.DEFAULT_NAME);

     // check if the specified user type exists; if not use the default
    this.defaultType   = validateType(stringValue(ORGANIZATION_TYPE), oracle.iam.identity.foundation.naming.Organization.DEFAULT_TYPE);

     // check if the specified user type exists; if not use the default
    this.defaultStatus = validateStatus(stringValue(ORGANIZATION_STATUS), oracle.iam.identity.foundation.naming.Organization.STATUS_ACTIVE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconcile
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** {@link OrganizationEntity}.
   ** <br>
   ** This will do trusted reconciliation of Oracle Identity Manager Identities.
   **
   ** @param  organization       the {@link OrganizationEntity} to reconcile.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void reconcile(final OrganizationEntity organization)
    throws TaskException {

    final String method = "reconcile";
    trace(method, SystemMessage.METHOD_ENTRY);

    try{
      final Map<String, Object> attribute = organization.attribute();
      attribute.put(this.descriptor.identifier(), organization.name());
      // hmmmm, should we really chain
      final Map<String, Object> data = transformMaster(createMaster(organization.attribute(), false));
      // set the parent organization to default if no one specified
      if (StringUtility.isEmpty((String)data.get(this.profileParent)))
        data.put(this.profileParent, this.defaultParent);

      // set user type to default if no one specified
      if (StringUtility.isEmpty((String)data.get(this.profileType)))
        data.put(this.profileType, this.defaultType);

      // set status to default if no one specified
      if (StringUtility.isEmpty((String)data.get(this.profileStatus)))
         data.put(this.profileStatus, this.defaultStatus);

      // create a reconciliation event which have all data provided, means no
      // further child data processing necessary
      processRegularEvent(data);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
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

    try {
      this.filter.clear();
      this.filter.put(oracle.iam.identity.foundation.naming.Organization.NAME, organization);
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
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateType
  /**
   ** Check for the existance of an organizational type.
   ** <p>
   ** If the type does not exists the <code>defaultType</code> will be
   ** returned.
   **
   ** @param  type                the organization type of Oracle Identity
   **                             Manager to validate.
   ** @param  defaultType         the organization type that will be returned if
   **                             the specified <code>type</code> does not
   **                             exists.
   **
   ** @return                     a valid type applicable at an organization.
   **
   ** @throws TaskException       in case an error does occur.
   */
  protected String validateType(final String type, final String defaultType)
    throws TaskException {

    // prevent bogus input
    if (StringUtility.isEmpty(type))
      return defaultType;

    try {
      this.filter.clear();
      this.filter.put(LookupValue.ENCODED, type);
      tcResultSet resultSet = lookupFacade().getLookupValues(Lookup.ORGANIZATION_TYPE, this.filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { type, this.profileType };
        info(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default organization
        return defaultType;
      }
      else
        return type;
    }
    catch (tcInvalidLookupException e) {
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.ORGANIZATION_TYPE);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateStatus
  /**
   ** Check for the existance of an organizational status.
   ** <p>
   ** If the status does not exists the <code>defaultStatus</code> will be
   ** returned.
   **
   ** @param  status              the organization status of the Oracle Identity
   **                             Manager to validate.
   ** @param  defaultStatus       the organization status that will be returned
   **                             if the specified <code>type</code> does not
   **                             exists.
   **
   ** @return                     a valid status applicable at an organization.
   **
   ** @throws TaskException       in case an error does occur.
   */
  protected String validateStatus(final String status, final String defaultStatus)
    throws TaskException {

    // prevent bogus input
    if (StringUtility.isEmpty(status))
      return defaultStatus;

    try {
      this.filter.clear();
      this.filter.put(LookupValue.ENCODED, status);
      tcResultSet resultSet = lookupFacade().getLookupValues(Lookup.ORGANIZATION_STATUS, this.filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = { status, this.profileStatus };
        info(TaskBundle.format(TaskError.TASK_ATTRIBUTE_NOT_MAPPED, parameter));
        // override with default status
        return defaultStatus;
      }
      else
        return status;
    }
    catch (tcInvalidLookupException e) {
      throw new TaskException(TaskError.LOOKUP_NOT_FOUND, Lookup.ORGANIZATION_TYPE);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
  }
}