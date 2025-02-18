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

    File        :   IdentityRoleHarvester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityRoleHarvester.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

import java.util.HashMap;
import java.util.Collection;

import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleCategory;
import oracle.iam.identity.rolemgmt.vo.RoleManagerResult;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleCategoryManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.NoSuchRoleCategoryException;
import oracle.iam.identity.exception.RoleCategoryLookupException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.BusinessRoleEntity;

import oracle.iam.identity.ots.resource.HarvesterBundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityRoleHarvester
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>IdentityRoleHarvester</code> acts as the service end point for the
 ** Oracle Identity Manager to on-board business roles information from an
 ** offlined XML file.
 ** <p>
 ** This service is intentionally used to created, modify roles in Oracle
 ** Identity Manager and associate them with the appropriate
 ** <code>Access Policies</code>. The <code>Access Policies</code> might exists
 ** or needs to be created based on the detail information any abstract role
 ** handled by this service provides.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class IdentityRoleHarvester extends RoleHarvester {

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** role category should assigned to a new created role.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String ROLE_CATEGORY    = "Role Category";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation object */
    TaskAttribute.build(RECONCILE_OBJECT,     "Role")
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
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,   TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,        TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the business logic layer to operate on roles */
  private RoleManager                       roleFacade;

  /**
   ** the internal system identifier of the default role category belonging to
   ** {@link RoleManagerConstants#DEFAULT_ROLE_CATEGORY_NAME}
   */
  private Long                              defaultCategory;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityRoleHarvester</code> scheduled job that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityRoleHarvester() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process (EntityListener)
  /**
   ** Reconciles a particular bulk of {@link BusinessRoleEntity}'s.
   ** <br>
   ** This will do creation and modification of Oracle Identity Manager Roles.
   **
   ** @param  bulk               the {@link Collection} of
   **                            {@link BusinessRoleEntity}'s to manage.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  public void process(final Collection<BusinessRoleEntity> bulk)
    throws TaskException {

    final String method = "process";
    trace(method, SystemMessage.METHOD_ENTRY);

    // validate the effort to do
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(bulk.size())));
    if (gatherOnly()) {
      incrementIgnored(bulk.size());
      info(TaskBundle.format(TaskMessage.RECONCILE_SKIP, reconcileObject()));
    }
    else {
      if (this.roleFacade == null)
        this.roleFacade = service(RoleManager.class);

      for (BusinessRoleEntity entity : bulk) {
        if (isStopped())
          break;
        try {
          if (!CollectionUtility.empty(entity.attribute()))
            processRole(entity);
          else {
            // if there is no further attribute as the identifier we have not
            // enough values for reconciliation
            error(method, HarvesterBundle.format(HarvesterError.OBJECT_ATTRIBUTE_MISSING, RoleManagerConstants.ROLE_ENTITY_NAME, entity.name()));
            incrementFailed();
          }
        }
        catch (TaskException e) {
          // TODO: improve error handling
          error(method, HarvesterBundle.format(HarvesterError.OPERATION_IMPORT_FAILED, RoleManagerConstants.ROLE_ENTITY_NAME, entity.name()));
        }
      }
    }
    this.roleFacade = null;
    info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
    trace(method, SystemMessage.METHOD_EXIT);
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

     // check if the specified user type exists; if not use the default
    this.defaultCategory = lookupCategory(RoleManagerConstants.DEFAULT_ROLE_CATEGORY_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processRole
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** {@link BusinessRoleEntity}.
   ** <br>
   ** This will do creation and modification of Oracle Identity Manager Roles.
   **
   ** @param  role               the {@link BusinessRoleEntity} to manage.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void processRole(final BusinessRoleEntity role)
    throws TaskException {

    final String method = "processRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    create(role);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new role in the Oracle Identity Manager Server through the
   ** discovered {@link RoleManager}.
   **
   ** @param  entity             the {@link BusinessRoleEntity} to create.
   **
   ** @throws HarvesterException in case an error does occur.
   */
  public void create(final BusinessRoleEntity entity)
    throws HarvesterException {

    final String method ="create";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String entityID = lookup(entity);
    if (!StringUtility.isEmpty(entityID)) {
      error(method, HarvesterBundle.format(HarvesterError.OBJECT_ELEMENT_EXISTS, RoleManagerConstants.ROLE_ENTITY_NAME, entity.name()));
      trace(method, SystemMessage.METHOD_EXIT);
      modify(entity);
    }
    else {
      timerStart(method);
      // extend the attribute mapping with the name of the role to create
      try {
        // apply standard attribute mapping and transformation
        final HashMap<String, Object> parameter = new HashMap<String, Object>(transformMaster(createMaster(entity.attribute(), false, true)));
        // transform the role category to a valid identifier
        parameter.put(RoleManagerConstants.ROLE_CATEGORY_KEY, lookupCategory((String)parameter.remove(RoleManagerConstants.ROLE_CATEGORY_NAME)));
        // remove the e_mail Address if its not valid
        if (parameter.containsKey(RoleManagerConstants.ROLE_EMAIL)) {
          final String email = (String)parameter.get(RoleManagerConstants.ROLE_EMAIL);
          if (!StringUtility.isEmail(email))
            parameter.remove(RoleManagerConstants.ROLE_EMAIL);
        }
        // provide information about creation of a role at info level because
        // this is expected
        info(HarvesterBundle.format(HarvesterMessage.OPERATION_CREATE_BEGIN, RoleManagerConstants.ROLE_ENTITY_NAME, entity.name()));
        final Role              subject = new Role(parameter);
        final RoleManagerResult result  = this.roleFacade.create(subject);
        if (result.getFailedResults().isEmpty()) {
          // fetch through the ID of ceated role to keep further processes in
          // sync
          entity.key(result.getEntityId());
          info(HarvesterBundle.format(HarvesterMessage.OPERATION_CREATE_SUCCESS, RoleManagerConstants.ROLE_ENTITY_NAME, entity.name()));
          incrementSuccess();
        }
        else {
          incrementFailed();
          final String[]           arguments = { RoleManagerConstants.ROLE_ENTITY_NAME, entity.name(), result.getFailedResults().get(result.getEntityId()) };
          final HarvesterException throwable = new HarvesterException(HarvesterError.OPERATION_MODIFY_FAILED, arguments);
          error(method, throwable.getLocalizedMessage());
          throw throwable;
        }
      }
      catch (Exception e) {
        incrementFailed();
        final String[]           arguments = { RoleManagerConstants.ROLE_ENTITY_NAME, entity.name(), e.getLocalizedMessage() };
        final HarvesterException throwable = new HarvesterException(HarvesterError.OPERATION_CREATE_FAILED, arguments);
        fatal(method, throwable);
        throw throwable;
      }
      finally {
        timerStop(method);
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing role in Oracle Identity Manager Server through the
   ** discovered {@link RoleManager}.
   **
   ** @param  entity             the {@link BusinessRoleEntity} to update.
   **
   ** @throws HarvesterException in case an error does occur.
   */
  public void modify(final BusinessRoleEntity entity)
    throws HarvesterException {

    final String method ="modify";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String entityID = lookup(entity);
    if (StringUtility.isEmpty(entityID)) {
      trace(method, SystemMessage.METHOD_EXIT);
      final String[] arguments = { RoleManagerConstants.ROLE_ENTITY_NAME, entity.name() };
      throw new HarvesterException(HarvesterError.OBJECT_ELEMENT_NOTFOUND, arguments);
    }
    else {
      timerStart(method);
      try {
        // apply standard attribute mapping and transformation
        final HashMap<String, Object> parameter = new HashMap<String, Object>(transformMaster(createMaster(entity.attribute(), false, true)));
        // transform the role category to a valid identifier
        if (parameter.containsKey(RoleManagerConstants.ROLE_CATEGORY_NAME))
          parameter.put(RoleManagerConstants.ROLE_CATEGORY_KEY, lookupCategory((String)parameter.remove(RoleManagerConstants.ROLE_CATEGORY_NAME)));
        // remove the e_mail Address if its not valid
        if (parameter.containsKey(RoleManagerConstants.ROLE_EMAIL)) {
          final String email = (String)parameter.get(RoleManagerConstants.ROLE_EMAIL);
          if (!StringUtility.isEmail(email))
            parameter.remove(RoleManagerConstants.ROLE_EMAIL);
        }
        // modify the role only if the are parameter that requires modification
        // extend the attribute mapping with the name of the role to create
        if (!parameter.isEmpty()) {
          // provide information about creation of a role at warning level
          // because this is not expected
          warning(HarvesterBundle.format(HarvesterMessage.OPERATION_MODIFY_BEGIN, RoleManagerConstants.ROLE_ENTITY_NAME, entity.name()));
          final Role              identity = new Role(entityID, parameter);
          final RoleManagerResult result   = this.roleFacade.modify(identity);
          if (result.getFailedResults().isEmpty()) {
            warning(HarvesterBundle.format(HarvesterMessage.OPERATION_MODIFY_SUCCESS, RoleManagerConstants.ROLE_ENTITY_NAME, entity.name()));
            incrementSuccess();
          }
          else {
            incrementFailed();
            final String[]           arguments = { RoleManagerConstants.ROLE_ENTITY_NAME, entity.name(), result.getFailedResults().get(result.getEntityId()) };
            final HarvesterException throwable = new HarvesterException(HarvesterError.OPERATION_MODIFY_FAILED, arguments);
            error(method, throwable.getLocalizedMessage());
            throw throwable;
          }
        }
        else
          incrementIgnored();
      }
      catch (Exception e) {
        final String[]           arguments = { RoleManagerConstants.ROLE_ENTITY_NAME, entity.name(), e.getLocalizedMessage() };
        final HarvesterException throwable = new HarvesterException(HarvesterError.OPERATION_MODIFY_FAILED, arguments);
        fatal(method, throwable);
        throw throwable;
      }
      finally {
        timerStop(method);
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns an existing role from Identity Manager repository through the
   ** discovered {@link RoleManager}.
   **
   ** @param  instance           the {@link BusinessRoleEntity} to lookup.
   **
   ** @return                    the system identifier of a role.
   **
   ** @throws HarvesterException in case an error does occur.
   */
  public String lookup(final BusinessRoleEntity instance)
    throws HarvesterException {

    final String method ="lookup";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    Role identity = null;
    try {
      identity = this.roleFacade.getDetails(RoleManagerConstants.ROLE_NAME, instance.name(), null);
      instance.key(Long.parseLong(identity.getEntityId()));
    }
    catch (NoSuchRoleException e) {
      instance.key(-1L);
    }
    catch (Exception e) {
      throw new HarvesterException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return (identity == null) ? null : identity.getEntityId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupCategory
  /**
   ** Returns an existing role categoory in Oracle Identity Manager through the
   ** discovered {@link RoleCategoryManager}.
   **
   ** @param  category           the name of a role category to lookup.
   **
   ** @return                    the internal system identifier of the category
   **                            or <code>null</code> if the specified
   **                            <code>category</code> does not exists.
   **
   ** @throws HarvesterException in case an error does occur.
   */
  private Long lookupCategory(final String category)
    throws HarvesterException {

     // prevent bogus input
    if (StringUtility.isEmpty(category))
      return this.defaultCategory;

    final String method ="lookupCategory";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final RoleCategoryManager categoryFacade  = service(RoleCategoryManager.class);
    try {
      final RoleCategory identity = categoryFacade.getDetails(RoleManagerConstants.ROLE_CATEGORY_NAME, category, null);
      return new Long(identity.getEntityId());
    }
    catch (NoSuchRoleCategoryException e) {
      // "Entity %1$s \"%2$s=%3$s\" defined ambiguous"
      warning(TaskBundle.format(TaskError.ENTITY_NOT_EXISTS, ROLE_CATEGORY, RoleManagerConstants.ROLE_CATEGORY_NAME, category));
      return this.defaultCategory;
    }
    catch (SearchKeyNotUniqueException e) {
      // "Entity %1$s \"%2$s=%3$s\" defined ambiguous"
      warning(TaskBundle.format(TaskError.ENTITY_AMBIGUOUS, ROLE_CATEGORY, RoleManagerConstants.ROLE_CATEGORY_NAME, category));
      return this.defaultCategory;
    }
    catch (RoleCategoryLookupException e) {
      throw new HarvesterException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}