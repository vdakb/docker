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

    File        :   HierarchyTrustedReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    HierarchyTrustedReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.reconciliation;

import java.util.List;
import java.util.Map;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.Lookup;
import oracle.iam.identity.foundation.naming.LookupValue;
import oracle.iam.identity.foundation.naming.Organization;

import oracle.iam.identity.foundation.ldap.DirectoryName;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;

////////////////////////////////////////////////////////////////////////////////
// class HierarchyTrustedReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>HierarchyTrustedReconciliation</code> acts as the service end
 ** point for the Oracle Identity Manager to reconcile organizational
 ** information from a Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class HierarchyTrustedReconciliation extends ObjectReconciliation {

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
  protected static final String        MAINTAIN_HIERARCHY  = "Maintain Hierarchy";

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
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,          TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,     TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /**
     ** the task attribute to specify that the organizational hierarchy should
     ** be resolved
     */
  , TaskAttribute.build(MAINTAIN_HIERARCHY,   TaskAttribute.MANDATORY)
    /**
     ** the default organization parent of the on-boarded organization if no
     ** superior supplied by the data
     */
  , TaskAttribute.build(ORGANIZATION_PARENT,  TaskAttribute.MANDATORY)
    /**
     ** the default organization type of the on-boarded organization if no type
     ** supplied by the data
     */
  , TaskAttribute.build(ORGANIZATION_TYPE,    Organization.DEFAULT_TYPE)
    /**
     ** the default organization type of the on-boarded organization if no type
     ** supplied by the data
     */
  , TaskAttribute.build(ORGANIZATION_STATUS,  Organization.STATUS_ACTIVE)
    /**
     ** the task attribute that specifies the search scope of the query
     ** <br>
     ** Allowed values are OneLevel | SubTree | Object
     */
  , TaskAttribute.build(SEARCHSCOPE,          TaskAttribute.MANDATORY)
    /** the task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCHFILTER,         TaskAttribute.MANDATORY)
    /** the task attribute that specifies the search base of the query */
  , TaskAttribute.build(SEARCHBASE,           SystemConstant.EMPTY)
    /** the task attribute that specifies the sort order applied to the query */
  , TaskAttribute.build(SEARCHORDER,          SystemConstant.EMPTY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,          SystemConstant.TRUE)
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
   **
   ** @param  multiValueFeature  the entry in the <code>Metadata Descriptor</code>
   **                            Server Feature that declares the multi-valued
   **                            attributes.
   */
  public HierarchyTrustedReconciliation(final String multiValueFeature) {
    // ensure inheritance
    super(multiValueFeature);
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
  // Method:   processSubject (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do tusted reconciliation of Oracle Identity Manager
   ** Organizations.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected final void processSubject(Map<String, Object> subject)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    // to improve performance by make a local reference to all the attributes
    // retrieved more than once from the associated connector
    final DirectoryConnector connector = this.connector();

    // extracts the known values of the organization to reconcile here
    // it will be no longer available after the attribute mapping has proceed
    final String         organizatioDN    = (String)subject.remove(connector.distinguishedNameAttribute());
    final List<String[]> component        = DirectoryName.explode(organizatioDN);
    final String[]       organizationName = component.remove(0);

    String[] parameter  = {TaskBundle.string(TaskMessage.ENTITY_ORGANIZATION), organizationName[1]};
    info(TaskBundle.format(TaskMessage.ENTITY_RECONCILE, parameter));
    try {
      // filter data in a new mapping
      Map<String, Object> data = createMaster(subject, false);

      // check if we have to maintain the DIT Hierarchy and transform it to the
      // organization
      if (booleanValue(MAINTAIN_HIERARCHY)) {
        data.put(this.profileParent, component.remove(0)[1]);
      }

      // apply transformation if needed
      data = transformMaster(data);

      data.put(this.profileParent, validateOrganization((String)data.get(this.profileParent), this.defaultParent));

      // set the parent organization to default if no one specified
      if (StringUtility.isEmpty((String)data.get(this.profileParent)))
        data.put(this.profileParent, this.defaultParent);

      // set user type to default if no one specified
      if (StringUtility.isEmpty((String)data.get(this.profileType)))
        data.put(this.profileType, this.defaultType);

      // set status to default if no one specified
      if (StringUtility.isEmpty((String)data.get(this.profileStatus)))
         data.put(this.profileStatus, this.defaultStatus);

      // check if the entry is changed
      if (ignoreEvent(data)) {
        // FIX: Defect DE-000122 Generic Directory Reconciliation does not count ignored events correctly
        //      explicitly increment the appropriate counter
        incrementIgnored();
        warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, this.descriptor.identifier(), organizationName[1]));
      }
      else {
        // create a reconciliation event which have all data provided, means
        // no further child data processing necessary
        processRegularEvent(data);
      }
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

    final String method  = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      this.profileStatus = this.descriptor.organizationStatus();
      this.profileType   = this.descriptor.organizationType();
      this.profileParent = this.descriptor.organizationParent();

      // check if the specified organization exists; if not use the default
      this.defaultParent = validateOrganization(stringValue(ORGANIZATION_PARENT), Organization.DEFAULT_NAME);

       // check if the specified user type exists; if not use the default
      this.defaultType   = validateType(stringValue(ORGANIZATION_TYPE), Organization.DEFAULT_TYPE);

       // check if the specified user type exists; if not use the default
      this.defaultStatus = validateStatus(stringValue(ORGANIZATION_STATUS), Organization.STATUS_ACTIVE);
    }
    finally {
      timerStop(method);
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateType
  /**
   ** Check for the existance of an organizational type.
   ** <p>
   ** If the type does not exists the <code>defaultStatus</code> will be
   ** returned.
   **
   ** @param  type                the organization type of the Oracle Identity
   **                             Manager to validate.
   ** @param  defaultType         the organization type that will be returned if
   **                             the specified <code>type</code> does not
   **                             exists.
   **
   ** @return                     the evaluated type information.
   **
   ** @throws TaskException       in case an error does occur.
   */
  protected String validateType(final String type, final String defaultType)
    throws TaskException {

    // prevent bogus input
    if (StringUtility.isEmpty(type))
      return defaultType;

    final String method  = "validateType";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
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
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
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
   ** @return                     the evaluated status information.
   **
   ** @throws TaskException       in case an error does occur.
   */
  protected String validateStatus(final String status, final String defaultStatus)
    throws TaskException {

    // prevent bogus input
    if (StringUtility.isEmpty(status))
      return defaultStatus;

    final String method  = "validateStatus";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
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
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}