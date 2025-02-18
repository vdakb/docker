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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   UserHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    UserHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.text.ParseException;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.Calendar;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserCreateException;
import oracle.iam.identity.exception.UserDeleteException;
import oracle.iam.identity.exception.UserModifyException;
import oracle.iam.identity.exception.UserEnableException;
import oracle.iam.identity.exception.UserDisableException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.hst.foundation.utility.DateUtility;

import oracle.iam.identity.common.FeaturePlatformTask;

////////////////////////////////////////////////////////////////////////////////
// class UserHandler
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>UserHandler</code> creates,deletes and configures an user profiles in
 ** Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UserHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the User Identity Instance to configure or to create */
  private UserInstance        single;

  /** the name of the User Identity to configure or to create */
  private List<UserInstance>  multiple     = new ArrayList<UserInstance>();

  /** the business logic layer to operate on user profiles */
  private UserManager         userFacade;

  /** the business logic layer to operate on organizations */
  private OrganizationManager organizationFacade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UserHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public UserHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the user profile related to Identity Manager.
   **
   ** @return                    the name of the user profile related to
   **                            Identity Manager.
   */
  public final String userid() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Identity
   **                            Manager user profile.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager user profile.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    if (this.single == null)
      this.single = new UserInstance();

    // add the value pair to the parameters
    this.single.addParameter(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager user profile.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that is already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    if (this.single == null)
      this.single = new UserInstance();

    // add the value pairs to the parameters
    this.single.addParameter(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the <code>name</code> of the user profile related to
   ** Identity Manager.
   **
   ** @param  name               the <code>name</code> of the user profile
   **                            related to Identity Manager.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = new UserInstance();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  instance           the {@link UserInstance} to add.
   **
   ** @throws BuildException     if the specified {@link UserInstance} is
   **                            already assigned to this task.
   */
  public void addInstance(final UserInstance instance) {
    // prevent bogus input
    if ((this.single != null && this.single.equals(instance)) || this.multiple.contains(instance))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, instance.name()));

    // add the instance to the object to handle
    this.multiple.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new user profile in Identity Manager through the given
   ** {@link UserManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.userFacade = task.service(UserManager.class);
    this.organizationFacade = task.service(OrganizationManager.class);

    if (this.single != null)
      create(this.single);

    for (UserInstance i : this.multiple)
      create(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing user profile from Identity Manager through the
   ** discovered {@link UserManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.userFacade = task.service(UserManager.class);

    if (this.single != null)
      delete(this.single);

    for (UserInstance i : this.multiple)
      delete(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing user profile in Identity Manager through the
   ** discovered {@link UserManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.userFacade = task.service(UserManager.class);
    this.organizationFacade = task.service(OrganizationManager.class);

    if (this.single != null)
      modify(this.single);

    for (UserInstance i : this.multiple)
      modify(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an existing user profile from Identity Manager through the
   ** discovered {@link UserManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void enable(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.userFacade = task.service(UserManager.class);

    if (this.single != null)
      enable(this.single);

    for (UserInstance i : this.multiple)
      enable(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an existing user profile from Identity Manager through the
   ** discovered {@link UserManager}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disable(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.userFacade = task.service(UserManager.class);

    if (this.single != null)
      disable(this.single);

    for (UserInstance i : this.multiple)
      disable(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new user profile in Identity Manager through the discovered
   ** {@link UserManager}.
   **
   ** @param  instance           the {@link UserInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final UserInstance instance)
    throws ServiceException {

    if (exists(instance)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, UserManagerConstants.USER_ENTITY, instance.name()));
      modify(instance);
    }
    else
      try {
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, UserManagerConstants.USER_ENTITY, instance.name()));
        // extend the attribute mapping with the userid of the identity to
        // create
        final HashMap<String, Object> parameter = instance.parameter();
        parameter.put(UserManagerConstants.AttributeName.USER_LOGIN.getId(), instance.name());
        convertParameterValues(parameter);

        final String password = (String)parameter.remove(UserManagerConstants.AttributeName.PASSWORD.getId());
        // perform creation of user metadata only if at least one attribute
        // left
        if (parameter.size() == 0) {
          warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SKIPPED, UserManagerConstants.USER_ENTITY, instance.name()));
        }
        else {
          final UserManagerResult result = this.userFacade.create(new User(null, parameter));
          if (result.getStatus().equals("COMPLETED"))
            changePassword(result.getEntityId(), password, false);

          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, UserManagerConstants.USER_ENTITY, instance.name()));
        }
      }
      catch (UserCreateException e) {
        final String[] error = { UserManagerConstants.USER_ENTITY, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_CREATE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing user profile in Identity Manager through the
   ** discovered {@link UserManager}.
   **
   ** @param  instance           the {@link UserInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final UserInstance instance)
    throws ServiceException {

    final String entityID = entityID(instance.name());
    if (!StringUtility.isEmpty(entityID))
      try {
        final String[] arguments = { UserManagerConstants.USER_ENTITY, instance.name() };
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
        this.userFacade.delete(entityID, false);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
      }
      catch (UserDeleteException e) {
        final String[] error = { UserManagerConstants.USER_ENTITY, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_DELETE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      final String[] arguments = { UserManagerConstants.USER_ENTITY, instance.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing user profile in Identity Manager through the
   ** discovered {@link UserManager}.
   **
   ** @param  instance           the {@link UserInstance} to configure.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final UserInstance instance)
    throws ServiceException {

    final String entityID = entityID(instance.name());
    if (!StringUtility.isEmpty(entityID))
      try {
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, UserManagerConstants.USER_ENTITY, instance.name()));
        // extend the attribute mapping with the userid to create
        final HashMap<String, Object> parameter = instance.parameter();
        convertParameterValues(parameter);
        if (parameter.size() == 0) {
          warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SKIPPED, UserManagerConstants.USER_ENTITY, instance.name()));
        }
        else {
          final String password = (String)parameter.remove(UserManagerConstants.AttributeName.PASSWORD.getId());
          // perform modifications of user metadata only if at least one attribute
          // left
          if (parameter.size() != 0) {
            final User identity = new User(entityID, parameter);
            this.userFacade.modify(identity);
          }
          changePassword(entityID, password, false);
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, UserManagerConstants.USER_ENTITY, instance.name()));
        }
      }
      catch (UserModifyException e) {
        final String[] error = { UserManagerConstants.USER_ENTITY, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_MODIFY_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      final String[] arguments = { UserManagerConstants.USER_ENTITY, instance.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an existing user profile in Identity Manager through the
   ** discovered {@link UserManager}.
   **
   ** @param  instance           the {@link UserInstance} to enable.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void enable(final UserInstance instance)
    throws ServiceException {

    final String   entityID = entityID(instance.name());
    final String[] arguments = { UserManagerConstants.USER_ENTITY, instance.name() };
    if (!StringUtility.isEmpty(entityID))
      try {
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ENABLE_BEGIN, arguments));
        this.userFacade.enable(entityID, true);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ENABLE_SUCCESS, arguments));
      }
      catch (UserEnableException e) {
        final String[] error = { UserManagerConstants.USER_ENTITY, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_ENABLE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_ENABLE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an existing user profile in Identity Manager through the
   ** discovered {@link UserManager}.
   **
   ** @param  instance           the {@link UserInstance} to disable.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disable(final UserInstance instance)
    throws ServiceException {

    final String entityID = entityID(instance.name());
    if (!StringUtility.isEmpty(entityID))
      try {
        final String[] arguments = { UserManagerConstants.USER_ENTITY, instance.name() };
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DISABLE_BEGIN, arguments));
        this.userFacade.disable(entityID, true);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DISABLE_SUCCESS, arguments));
      }
      catch (UserDisableException e) {
        final String[] error = { UserManagerConstants.USER_ENTITY, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_DISABLE_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_DISABLE_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      final String[] arguments = { UserManagerConstants.USER_ENTITY, instance.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link UserInstance} exists in Identity Manager
   ** through the discovered {@link UserManager}.
   **
   ** @param  instance           the {@link UserInstance} to check for
   **                            existance.
   **
   ** @return                    <code>true</code> if the
   **                            {@link UserInstance} exists in the backend
   **                            system; otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final UserInstance instance)
    throws ServiceException {

    return !StringUtility.isEmpty(entityID(instance.name()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityID
  /**
   ** Returns an existing user profile of the Identity Manager through the
   ** discovered {@link UserManager}.
   **
   ** @param  userid             the id of the user to lookup.
   **
   ** @return                    the system identifier <code>EntityId</code> for
   **                            the specified userid.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public String entityID(final String userid)
    throws ServiceException {

    User identity = null;
    try {
      identity = this.userFacade.getDetails(userid, null, true);
    }
    catch (NoSuchUserException e) {
      final String[] arguments = { UserManagerConstants.USER_ENTITY, userid };
      debug(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return (identity == null) ? null : identity.getEntityId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrganization
  /**
   ** Returns an existing organization of the Identity Manager through the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  organization       the name of the organization to lookup.
   **
   ** @return                    the system identifier <code>EntityId</code> for
   **                            the specified organization name.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public String lookupOrganization(final String organization)
    throws ServiceException {

    try {
      final Organization entity = this.organizationFacade.getDetails(organization, null, true);
      if (entity == null) {
        final String[] arguments = { OrganizationManagerConstants.AttributeName.ORG_NAME.getId(), organization };
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      }
      return entity.getEntityId();
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    final ServiceOperation operation = this.operation();
    if (ServiceOperation.create == operation || ServiceOperation.modify == operation) {
      final boolean strict = ServiceOperation.create == operation;
      try {
        if (this.single != null)
          this.single.validate(strict);

        for (UserInstance i : this.multiple)
          i.validate(strict);
      }
      catch (Exception e) {
        throw new BuildException(e.getLocalizedMessage());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changePassword
  /**
   ** Changes the user's password.
   **
   ** @param  entityID           the ID of the user whose password is to be
   **                            changed. The <code>userLogin</code> flag will
   **                            decide what does userID contains.
   ** @param  password           the new password to set.
   ** @param  userLogin          <code>true</code> if the <code>entityID</code>
   **                            contains user login and <code>false</code> if
   **                            the <code>entityID</code> contains user key.
   * @throws ServiceException
   */
  private void changePassword(final String entityID, final String password, final boolean userLogin)
    throws ServiceException {
    try {
      // change the password of the affected user if required
      if (!StringUtility.isEmpty(password))
        this.userFacade.changePassword(entityID, password.toCharArray(), userLogin, null, true, false);
    }
    catch (NoSuchUserException e) {
      final String[] error = { UserManagerConstants.USER_ENTITY, entityID, e.getLocalizedMessage() };
      if (failonerror())
        throw new ServiceException(ServiceError.OPERATION_MODIFY_FAILED, error);
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, error));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertParameterValues
  private void convertParameterValues(final Map<String, Object> parameter)
    throws ServiceException {

    // extend the attribute mapping with the organization key the user
    // belongs to
    final String organizationName = (String)parameter.remove(OrganizationManagerConstants.AttributeName.ORG_NAME.getId());
    if (!StringUtility.isEmpty(organizationName))
      parameter.put(UserManagerConstants.AttributeName.USER_ORGANIZATION.getId(), Long.parseLong(lookupOrganization(organizationName)));

    // extend the attribute mapping with the manager key the user belongs to
    final String managerLogin = (String)parameter.remove(UserManagerConstants.AttributeName.MANAGER_NAME.getId());
    if (!StringUtility.isEmpty(managerLogin))
      parameter.put(UserManagerConstants.AttributeName.MANAGER_KEY.getId(), Long.parseLong(entityID(managerLogin)));

    if (parameter.containsKey(UserManagerConstants.AttributeName.ACCOUNT_START_DATE.getId()))
      convertDateValue(UserManagerConstants.AttributeName.ACCOUNT_START_DATE.getId(), parameter);

    if (parameter.containsKey(UserManagerConstants.AttributeName.ACCOUNT_END_DATE.getId()))
      convertDateValue(UserManagerConstants.AttributeName.ACCOUNT_END_DATE.getId(), parameter);

    if (parameter.containsKey(UserManagerConstants.AttributeName.PROVISIONINGDATE.getId()))
      convertDateValue(UserManagerConstants.AttributeName.ACCOUNT_END_DATE.getId(), parameter);

    if (parameter.containsKey(UserManagerConstants.AttributeName.PROVISIONEDDATE.getId()))
      convertDateValue(UserManagerConstants.AttributeName.ACCOUNT_END_DATE.getId(), parameter);

    if (parameter.containsKey(UserManagerConstants.AttributeName.DEPROVISIONINGDATE.getId()))
      convertDateValue(UserManagerConstants.AttributeName.ACCOUNT_END_DATE.getId(), parameter);

    if (parameter.containsKey(UserManagerConstants.AttributeName.DEPROVISIONEDDATE.getId()))
      convertDateValue(UserManagerConstants.AttributeName.ACCOUNT_END_DATE.getId(), parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertDateValue
  private void convertDateValue(final String attribute, final Map<String, Object> parameter) {
    String value = (String)parameter.get(attribute);
    try {
      final Calendar result = DateUtility.parseDate(value, DateUtility.XML8601);
      parameter.put(attribute, result.getTime());
    }
    catch (ParseException e) {
      throw new BuildException(e.getLocalizedMessage());
    }
  }
}