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

    File        :   HierarchyDeleteReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    HierarchyDeleteReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.reconciliation;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.tcResultSet;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import oracle.iam.conf.vo.SystemProperty;

import oracle.iam.conf.utils.Constants;

import oracle.iam.conf.api.SystemConfigurationService;

import oracle.iam.conf.exception.SystemConfigurationServiceException;

import oracle.iam.platform.entitymgr.EntityManager;

import oracle.iam.platform.entitymgr.vo.Entity;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.platform.entitymgr.spi.entity.Searchable;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.naming.Lookup;
import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class HierarchyDeleteReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>HierarchyDeleteReconciliation</code> acts as the service end
 ** point for the Oracle Identity Manager to reconcile delted organizational
 ** information from a Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class HierarchyDeleteReconciliation extends DeleteReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify the starting
   ** point of the organizational hierarchy to check for existance in the
   ** target system.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String ORGANIZATION_ROOT         = "Organization Root";

  /**
   ** Attribute tag which must be defined on this task to specify wether the
   ** starting point of the organizational hierarchy has also to be checked for
   ** existance in the target system or not.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String ORGANIZATION_ROOT_VALIDATE = "Organization Root Validate";

  /**
   ** The System Configuration Property that will be checked for proper
   ** configuration to execute this job successfully.
   ** <p>
   ** If this value provided by this property is configured as
   ** <code>false</code> all action executed by this job will fail hence the job
   ** will stop with exception if this property is not configured as
   ** <code>true</code>.
   */
  private static final String ORGANIZATION_ACTION          = "ORG.DisableDeleteActionEnabled";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute  = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,                 TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,           TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR,       TaskAttribute.MANDATORY)
    /**
     ** the task attribute with reconciliation operation
     ** <br>
     ** Allowed values Disabled | Deleted
     */
  , TaskAttribute.build(RECONCILE_OPERATION,        TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,                  TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation root element */
  , TaskAttribute.build(ORGANIZATION_ROOT,          TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation root element */
  , TaskAttribute.build(ORGANIZATION_ROOT_VALIDATE, TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies the search scope of the query
     ** <br>
     ** Allowed values are OneLevel | SubTree | Object
     */
  , TaskAttribute.build(SEARCHSCOPE,                TaskAttribute.MANDATORY)
    /** the task attribute that specifies the search base of the query */
  , TaskAttribute.build(SEARCHBASE,                 SystemConstant.EMPTY)
    /** the task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCHFILTER,               SystemConstant.EMPTY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final EntityManager       facade;
  private final Set<String>         searchReturning  = new HashSet<String>();
  private final Map<String, Object> searchControl    = new HashMap<String, Object>();

  private       SearchCriteria      searchFilter;
  private       Organization        organizationRoot;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>HierarchyDeleteReconciliation</code> scheduled
   ** task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public HierarchyDeleteReconciliation() {
    // ensure inheritance
    super();

    // initialize instance
    this.facade = service(EntityManager.class);
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
  // Method:   populateBatch (DeleteReconciliation)
  /**
   ** Creates a {@link List} of entries of a specific entity in Oracle Identity
   ** Manager. Each element in the {@link List} is an instance of {@link Map}
   ** that reflects the value mapping based on the reconciliation descriptor
   ** created in the initialization of the executing Scheduled Job.
   **
   ** @param  startrow           the starting row that has to be fetched from
   **                            the Organization entity.
   **                            This controls in conjunction with argument
   **                            <code>endrow</code> the memory consumption
   **                            needed by each iteration.
   ** @param  endrow             the end row that has to be fetched from
   **                            the Organization entity.
   **                            This controls in conjunction with argument
   **                            <code>startrow</code> the memory consumption
   **                            needed by each iteration.
   **
   ** @return                    the {@link List} that contains the attributes
   **                            for each entry fetched from Oracle Identity
   **                            Manager Organization entity.
   **                            Each entry is an instance of {@link Map}.
   **
   ** @throws TaskException      if the operation fails
   */
  @Override
  protected List<Map<String, Object>> populateBatch(final int startrow, final int endrow)
    throws TaskException {

    this.searchControl.put(Constants.SEARCH_STARTROW, new Integer(startrow));
    this.searchControl.put(Constants.SEARCH_ENDROW,   new Integer(endrow));
    List<Entity>              organization = null;
    List<Map<String, Object>> result       = null;
    try {
      organization = this.facade.findEntities(OrganizationManagerConstants.ORGANIZATION_ENTITY, this.searchFilter, this.searchReturning, startrow, endrow, OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), Searchable.SortOrder.ASCENDING);
      result       = new ArrayList<Map<String, Object>>(organization.size());
      // obtain the set of attributes that has to be returned by the search
      String[][] attribute = reconciliationMapping();
      for (Entity entity : organization) {
        // validate if the current organization is part of the process
        if (!booleanValue(ORGANIZATION_ROOT_VALIDATE) && entity.getID().equals(this.organizationRoot.getEntityId()))
          continue;

        // create the attribute mapping for reconciliation
        final Map<String, Object> element = new HashMap<String, Object>();
        for (int i = 0; i < attribute.length; i++) {
          element.put(attribute[i][1], entity.getAttribute(attribute[i][0]));
        }
        result.add(element);
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occure.
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    final String method  = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // check if the system is configured properly by reading the system
      // configuration property 'ORG.DisableDeleteActionEnabled'
      // if this value is configured as false all action executed by this job will
      // fail hence we can stop here if this property is not configured as true
      final SystemConfigurationService service = service(SystemConfigurationService.class);
      try {
        final SystemProperty property = service.getSystemProperty(ORGANIZATION_ACTION);
        if (property == null)
          throw new TaskException(TaskError.PROPERTY_NOTEXISTS, ORGANIZATION_ACTION);
        if (!SystemConstant.TRUE.equalsIgnoreCase(property.getPtyValue()))
          throw new TaskException(TaskError.PROPERTY_VALUE_CONFIGURATION, ORGANIZATION_ACTION);
      }
      catch (SystemConfigurationServiceException e) {
        throw new TaskException(TaskError.PROPERTY_NOTEXISTS, ORGANIZATION_ACTION);
      }

      // check if the specified organization exists by using the specified name
      OrganizationManager facade = service(OrganizationManager.class);
      try {
        this.organizationRoot = facade.getDetails(stringValue(ORGANIZATION_ROOT), null, true);
      }
      catch (Exception e) {
        throw new TaskException(e);
      }
      // create a filter that will search for all organization that are childs of
      // the passed in organization
      // Note: The search criteria has to use the name of the primary key column
      //       to get a query within the hierarchy.
      //       The root organization it self will be part of the result set
      //       returned by the query
      final List<String> hierarchy = new ArrayList<String>(1);
      hierarchy.add(this.organizationRoot.getEntityId());
      this.searchFilter = new SearchCriteria(
        new SearchCriteria(
          new SearchCriteria( OrganizationManagerConstants.AttributeName.ORG_STATUS.getId()
                            , OrganizationManagerConstants.AttributeValue.ORG_STATUS_DELETED.getId()
                            ,  SearchCriteria.Operator.NOT_EQUAL
          )
        , new SearchCriteria( OrganizationManagerConstants.AttributeName.ORG_STATUS.getId()
                            , OrganizationManagerConstants.AttributeValue.ORG_STATUS_DISABLED.getId()
                            , SearchCriteria.Operator.NOT_EQUAL)
        , SearchCriteria.Operator.AND
        )
      , new SearchCriteria( OrganizationManagerConstants.AttributeName.ID_FIELD.getId()
                          , hierarchy
                          , SearchCriteria.Operator.IN_HIERARCHY
        )
      , SearchCriteria.Operator.AND
      );

      // prepare the control mapping to specifiy how the serach for the batch has
      // to be performed
      this.searchControl.put(Constants.SEARCH_SORTEDBY,  OrganizationManagerConstants.AttributeName.ORG_NAME.getId());
      this.searchControl.put(Constants.SEARCH_SORTORDER, Searchable.SortOrder.ASCENDING);

      // create the set of attributes that has to be returned by the search
      String[][] attribute = reconciliationMapping();
      for (int i = 0; i < attribute.length; i++)
        this.searchReturning.add(attribute[i][0]);

      // extend the set of attributes returned from the search with the status
      // information
      this.searchReturning.add(OrganizationManagerConstants.AttributeName.ORG_STATUS.getId());
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
        String[] parameter = { status, this.descriptor.organizationStatus() };
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