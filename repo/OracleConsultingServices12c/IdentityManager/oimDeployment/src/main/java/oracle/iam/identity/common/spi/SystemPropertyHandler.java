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

    File        :   SystemPropertyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SystemPropertyHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import oracle.iam.conf.vo.SystemProperty;

import oracle.iam.conf.api.SystemConfigurationService;

import oracle.iam.conf.exception.StaleDataException;
import oracle.iam.conf.exception.InvalidDataLevelException;
import oracle.iam.conf.exception.MissingRequiredValueException;
import oracle.iam.conf.exception.NoSuchSystemPropertyExistException;
import oracle.iam.conf.exception.SystemConfigurationServiceException;
import oracle.iam.conf.exception.SystemPropertyAlreadyExistException;
import oracle.iam.conf.exception.InvalidSystemPropertyValueException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeaturePlatformTask;

////////////////////////////////////////////////////////////////////////////////
// class SystemPropertyHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~

/**
 * * <code>SystemPropertyHandler</code> creates,deletes and configures
 * * <code>System Property</code> in Identity Manager.
 * *
 * * @author  dieter.steding@oracle.com
 * * @version 1.0.0.0
 * * @since   1.0.0.0
 */
public class SystemPropertyHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The entity mapping key of the <code>System Property</code> configuration.
   */
  static final String                  SYSTEMPROPERTY_ENTITY = "SystemProperty";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name of the System Property Instance to configure or to create.
   */
  private SystemPropertyInstance       single;

  /**
   ** The name of the System Property Instance to configure or to create.
   */
  private List<SystemPropertyInstance> multiple = new ArrayList<SystemPropertyInstance>();

  /**
   ** The business logic layer to operate on system properties.
   */
  private SystemConfigurationService   facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemPropertyHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public SystemPropertyHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>System Property</code> in Identity Manager
   ** to handle.
   **
   ** @return                    the name of the <code>System Property</code> in
   **                            Identity Manager to handle.
   */
  public final String name() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyword
  /**
   ** Called to inject the argument for parameter <code>keyWord</code>.
   **
   ** @param  keyword            the key word assigned to a
   **                            <code>System Property</code> in Identity
   **                            Manager to handle.
   */
  public void keyword(final String keyword) {
    if (this.single == null) {
      this.single = new SystemPropertyInstance();
    }
    this.single.name(keyword);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyword
  /**
   ** Returns the key word assigned to a <code>System Property</code> in
   ** Identity Manager.
   **
   ** @return                    the key word assigned to a
   **                            <code>System Property</code> in Identity
   **                            Manager to handle.
   */
  public final String keyword() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Called to inject the argument for parameter <code>value</code>.
   **
   ** @param  value              the value of the <code>System Property</code>
   **                            in Identity Manager to handle.
   */
  public void value(final String value) {
    if (this.single == null) {
      this.single = new SystemPropertyInstance();
    }
    this.single.value(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the <code>System Property</code> in Identity Manager
   ** to handle.
   **
   ** @return                    the value of the <code>System Property</code>
   **                            in Identity Manager to handle.
   */
  public final String value() {
    return this.single == null ? null : this.single.value();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginRequired
  /**
   ** Called to inject the argument for parameter <code>login</code>.
   **
   ** @param  loginRequired      <code>true</code> login is required to read
   **                            the <code>System Property</code> from
   **                            Identity Manager; otherwise <code>false</code>.
   */
  public void loginRequired(final boolean loginRequired) {
    if (this.single == null) {
      this.single = new SystemPropertyInstance();
    }
    this.single.loginRequired(loginRequired);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginRequired
  /**
   ** Returns <code>true</code> login is required to read the
   ** <code>System Property</code> from Identity Manager.
   **
   ** @return                    <code>true</code> login is required to read
   **                            the <code>System Property</code> from
   **                            Identity Manager; otherwise <code>false</code>.
   */
  public final boolean loginRequired() {
    return this.single == null ? false : this.single.loginRequired();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ServiceProvider)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate()
    throws BuildException {

    if (this.single == null && this.multiple.size() == 0) {
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));
    }

    if (!(this.operation() == ServiceOperation.delete)) {
      try {
        if (this.single != null) {
          this.single.validate();
        }

        for (SystemPropertyInstance i : this.multiple) {
          i.validate();
        }
      }
      catch (Exception e) {
        throw new BuildException(e.getLocalizedMessage());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the  Identity
   **                            Manager <code>System Property</code>.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager <code>System Property</code>.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager <code>System Property</code>.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that is already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the <code>System Property</code> in
   **                            Identity Manager to handle.
   */
  @Override
  public void name(final String name) {
    if (this.single == null) {
      this.single = new SystemPropertyInstance();
    }

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
   ** @param  object             the {@link SystemPropertyInstance} to add.
   **
   ** @throws BuildException     if the specified {@link SystemPropertyInstance}
   **                            is already assigned to this task.
   */
  public void addInstance(final SystemPropertyInstance object) {
    // prevent bogus input
    if ((this.single != null && this.single.equals(object)) || this.multiple.contains(object)) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));
    }

    // add the instance to the object to handle
    this.multiple.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>System Property</code> in Identity Manager through the
   ** discovered {@link SystemConfigurationService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(SystemConfigurationService.class);

    if (this.single != null) {
      create(this.single);
    }

    for (SystemPropertyInstance i : this.multiple) {
      create(i);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>System Property</code> from Identity Manager
   ** through the given {@link SystemConfigurationService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(SystemConfigurationService.class);

    if (this.single != null) {
      delete(this.single);
    }

    for (SystemPropertyInstance i : this.multiple) {
      delete(i);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Updates an existing <code>System Property</code> in Identity Manager
   ** through the given {@link SystemConfigurationService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(SystemConfigurationService.class);

    if (this.single != null) {
      modify(this.single);
    }

    for (SystemPropertyInstance i : this.multiple) {
      modify(i);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>System Property</code> in Identity Manager through the
   ** discovered {@link SystemConfigurationService}.
   **
   ** @param  instance           the {@link SystemPropertyInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final SystemPropertyInstance instance)
    throws ServiceException {

    if (exists(instance)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, SYSTEMPROPERTY_ENTITY, instance.name()));
      modify(instance);
    }
    else {
      try {
        this.facade.addSystemProperty(SystemPropertyInstance.toProperty(instance));
      }
      catch (SystemPropertyAlreadyExistException e) {
        if (failonerror()) {
          throw new ServiceException(ServiceError.ABORT, e);
        }
        else {
          error(e.getMessage());
        }
      }
      catch (MissingRequiredValueException e) {
        if (failonerror()) {
          throw new ServiceException(ServiceError.ABORT, e);
        }
        else {
          error(e.getMessage());
        }
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Updates an existing <code>System Property</code> in Identity Manager
   ** through the given {@link SystemConfigurationService}.
   **
   ** @param  instance           the {@link SystemPropertyInstance} to update.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final SystemPropertyInstance instance)
    throws ServiceException {

    final SystemProperty property = find(instance);
    if (property == null) {
      if (failonerror()) {
        final String[] arguments = { SYSTEMPROPERTY_ENTITY, instance.name() };
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      }
      else {
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, SYSTEMPROPERTY_ENTITY, instance.name()));
      }
    }
    else {
      property.setPtyName(instance.name());
      property.setPtyValue(instance.value());
      property.setPtyLoginrequired(instance.loginRequired() ? "1" : "0");
      property.setPtyUpdate(new Date());
      try {
        this.facade.updateSystemProperty(property, property.getPtyUpdate());
      }
      catch (Exception e) {
        if (failonerror()) {
          throw new ServiceException(ServiceError.ABORT, e);
        }
        else {
          error(e.getMessage());
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>System Property</code> in Identity Manager
   ** through the discovered {@link SystemConfigurationService}.
   **
   ** @param  instance           the {@link SystemPropertyInstance} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final SystemPropertyInstance instance)
    throws ServiceException {

    if (!exists(instance)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, SYSTEMPROPERTY_ENTITY, instance.name()));
    }
    else {
      try {
        this.facade.deleteSystemProperty(instance.name(), new Date());
      }
      catch (InvalidDataLevelException e) {
        if (failonerror()) {
          throw new ServiceException(ServiceError.ABORT, e);
        }
        else {
          error(e.getMessage());
        }
      }
      catch (StaleDataException e) {
        if (failonerror()) {
          throw new ServiceException(ServiceError.ABORT, e);
        }
        else {
          error(e.getMessage());
        }
      }
      catch (InvalidSystemPropertyValueException e) {
        if (failonerror()) {
          throw new ServiceException(ServiceError.ABORT, e);
        }
        else {
          error(e.getMessage());
        }
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link SystemPropertyInstance} exists in Oracle
   ** Identity Manager through the discovered {@link SystemPropertyInstance}.
   **
   ** @param  instance           the {@link SystemPropertyInstance} to check for
   **                            existance.
   **
   ** @return                    <code>true</code> if the
   **                            {@link SystemPropertyInstance} exists in the
   **                            backend system; otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final SystemPropertyInstance instance)
    throws ServiceException {

    return find(instance) != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookup the specified {@link SystemPropertyInstance} in Identity Manager
   ** through the discovered {@link SystemPropertyInstance}.
   **
   ** @param  instance           the {@link SystemPropertyInstance} to lookup.
   **
   ** @return                    the {@link SystemProperty} that keyword match
   **                            the keywrd of the specified
   **                            {@link SystemProperty}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public SystemProperty find(final SystemPropertyInstance instance)
    throws ServiceException {

    try {
      return this.facade.getSystemProperty(instance.name());
    }
    catch (SystemConfigurationServiceException e) {
      // the causing exception is on the second level of the throwables
      Throwable throwable = e.getCause();
      if (throwable != null && throwable.getCause() != null) {
        throwable = throwable.getCause();
      }
      if (throwable instanceof NoSuchSystemPropertyExistException) {
        return null;
      }
      else {
        throw new ServiceException(e);
      }
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }
}
