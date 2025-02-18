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

    File        :   ITResourceHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ITResourceHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.apache.tools.ant.BuildException;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAttributeMissingException;
import Thor.API.Exceptions.tcInvalidAttributeException;
import Thor.API.Exceptions.tcITResourceNotFoundException;
import Thor.API.Exceptions.DuplicateITResourceInstanceException;

import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ITResourceHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>ITResourceHandler</code> creates,deletes and configures an
 ** <code>IT Resource</code> instance in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ITResourceHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for <code>IT Resource</code> instances. */
  public static final String PREFIX = "IT Resources.";

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition system key should be resolved
   */
  static final String KEY = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition name should be resolved
   */
  static final String NAME = PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition key should be resolved
   */
  static final String TYPE_KEY = "IT Resources Type Definition.Key";

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition name should be resolved
   */
  static final String TYPE_NAME = "IT Resources Type Definition.Server Type";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the name of the <code>IT Resource</code> instance to configure or to
   ** create
   */
  private ITResourceInstance single;

  /**
   ** the name of the <code>IT Resource</code> instance to configure or to
   ** create
   */
  private List<ITResourceInstance> multiple = new ArrayList<ITResourceInstance>();

  /** the business logic layer to operate on <code>IT Resource</code> types */
  private tcITResourceDefinitionOperationsIntf definitionFacade;

  /**
   ** the business logic layer to operate on <code>IT Resource</code> instances
   */
  private tcITResourceInstanceOperationsIntf instanceFacade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ITResourceHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ITResourceHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Called to inject the argument for parameter <code>type</code>.
   **
   ** @param  type               the type of the <code>IT Resource</code>
   **                            instance in Identity Manager.
   */
  public void type(final String type) {
    if (this.single == null)
      this.single = new ITResourceInstance();

    this.single.type(type);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the <code>IT Resource</code> instance of Identity
   ** Manager to handle.
   **
   ** @return                    the name of the <code>IT Resource</code>
   **                            instance in Identity Manager.
   */
  public final String type() {
    return this.single == null ? null : this.single.type();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>IT Resource</code> instance of Identity
   ** Manager to handle.
   **
   ** @return                    the name of the <code>IT Resource</code>
   **                            instance in Identity Manager.
   */
  public final String name() {
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
   **                            Manager <code>IT Resource</code> instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager <code>IT Resource</code>
   **                            instance.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    if (this.single == null)
      this.single = new ITResourceInstance();

    // add the value pair to the parameters
    this.single.addParameter(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager <code>IT Resource</code>
   **                            instance.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that is already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    if (this.single == null)
      this.single = new ITResourceInstance();

    // add the value pairs to the parameters
    this.single.addParameter(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractHandler)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException   in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    if (!(this.operation() == ServiceOperation.delete))
      try {
        if (this.single != null)
          this.single.validate();

        for (ITResourceInstance i : this.multiple)
          i.validate();
      }
      catch (Exception e) {
        throw new BuildException(e.getLocalizedMessage());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the <code>IT Resource</code>
   **                            instance in Identity Manager.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = new ITResourceInstance();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link ITResourceInstance} to add.
   **
   ** @throws BuildException     if the specified {@link ITResourceInstance} is
   **                            already assigned to this task.
   */
  public void addInstance(final ITResourceInstance object) {
    // prevent bogus input
    if ((this.single != null && this.single.equals(object)) || this.multiple.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.multiple.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>IT Resource</code> instance of the Identity Manager
   ** Server through the discovered {@link tcITResourceInstanceOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.definitionFacade = task.service(tcITResourceDefinitionOperationsIntf.class);
    this.instanceFacade   = task.service(tcITResourceInstanceOperationsIntf.class);
    try {
      if (this.single != null)
        create(this.single);

      for (ITResourceInstance i : this.multiple)
        create(i);
    }
    finally {
      if (this.instanceFacade != null)
        this.instanceFacade.close();
      if (this.definitionFacade != null)
        this.definitionFacade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>IT Resource</code> instance in Identity Manager
   ** Server through the discovered {@link tcITResourceInstanceOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.definitionFacade = task.service(tcITResourceDefinitionOperationsIntf.class);
    this.instanceFacade   = task.service(tcITResourceInstanceOperationsIntf.class);
    try {
      if (this.single != null)
        delete(this.single);

      for (ITResourceInstance i : this.multiple)
        delete(i);
    }
    finally {
      if (this.instanceFacade != null)
        this.instanceFacade.close();
      if (this.definitionFacade != null)
        this.definitionFacade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing <code>IT Resource</code> instance of the Identity
   ** Manager through the discovered {@link tcITResourceInstanceOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.definitionFacade = task.service(tcITResourceDefinitionOperationsIntf.class);
    this.instanceFacade   = task.service(tcITResourceInstanceOperationsIntf.class);
    try {
      if (this.single != null)
        modify(this.single);

      for (ITResourceInstance i : this.multiple)
        modify(i);
    }
    finally {
      if (this.instanceFacade != null)
        this.instanceFacade.close();
      if (this.definitionFacade != null)
        this.definitionFacade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>IT Resource</code> instance in Identity Manager
   ** through the discovered {@link tcITResourceInstanceOperationsIntf}.
   **
   ** @param  instance           the {@link ITResourceInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void create(final ITResourceInstance instance)
    throws ServiceException {

    if (exists(instance)) {
      final String[] arguments = { NAME, instance.name() };
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, arguments));
      try {
        long instancekey = instanceKey(instance);
        this.instanceFacade.updateITResourceInstanceParameters(instancekey, instance.parameter());
      }
      catch (tcInvalidAttributeException e) {
        if (failonerror())
          throw new BuildException(e.getLocalizedMessage());
        else
          error(e.getLocalizedMessage());
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
    else {
      // extends the parameter mapping with the type and name
      Map<String, String> parameter = new HashMap<String, String>();
      parameter.put(TYPE_KEY, definitionKey(instance));
      parameter.put(NAME, instance.name());
      try {
        long instancekey = this.instanceFacade.createITResourceInstance(parameter);
        this.instanceFacade.updateITResourceInstanceParameters(instancekey, instance.parameter());
      }
      catch (tcInvalidAttributeException e) {
        if (failonerror())
          throw new BuildException(e.getLocalizedMessage());
        else
          error(e.getLocalizedMessage());
      }
      catch (tcAttributeMissingException e) {
        if (failonerror())
          throw new BuildException(e.getLocalizedMessage());
        else
          error(e.getLocalizedMessage());
      }
      catch (DuplicateITResourceInstanceException e) {
        if (failonerror())
          throw new BuildException(e.getLocalizedMessage());
        else
          error(e.getLocalizedMessage());
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>IT Resource</code> instance of the Identity
   ** Manager through the discovered {@link tcITResourceInstanceOperationsIntf}.
   **
   ** @param  instance           the {@link ITResourceInstance} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void delete(final ITResourceInstance instance)
    throws ServiceException {

    if (!exists(instance)) {
      final String[] arguments = { NAME, instance.name() };
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    else
      try {
        long instancekey = instanceKey(instance);
        this.instanceFacade.deleteITResourceInstance(instancekey);
      }
      catch (tcITResourceNotFoundException e) {
        if (failonerror())
          throw new BuildException(e.getLocalizedMessage());
        else
          error(e.getLocalizedMessage());
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing <code>IT Resource</code> instance of the Identity
   ** Manager through the discovered {@link tcITResourceInstanceOperationsIntf}.
   **
   ** @param  instance           the {@link ITResourceInstance} to configure.
   **
   ** @throws BuildException     in either the <code>IT Resource</code> does not
   **                            exists or an attribute is not defined on the
   **                            <code>IT Resource</code> definition.
   **
   ** @throws ServiceException   in case an unhandled rror does occur.
   */
  private void modify(final ITResourceInstance instance)
    throws ServiceException {

    if (!exists(instance)) {
      final String[] arguments = { NAME, instance.name() };
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    else
      try {
        this.instanceFacade.updateITResourceInstanceParameters(instanceKey(instance), instance.parameter());
      }
      catch (tcInvalidAttributeException e) {
        if (failonerror())
          throw new BuildException(FeatureResourceBundle.format(FeatureError.PARAMETER_NAME_EXCEPTION, e.getLocalizedMessage(), instance.name()));
        else
          error(FeatureResourceBundle.format(FeatureError.PARAMETER_NAME_EXCEPTION, e.getLocalizedMessage(), instance.name()));
      }
      catch (tcITResourceNotFoundException e) {
        if (failonerror())
          throw new BuildException(e.getLocalizedMessage());
        else
          error(e.getLocalizedMessage());
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Returns an existing <code>IT Resource</code> type of the Identity Manager
   ** Server through the discovered {@link tcITResourceInstanceOperationsIntf}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private boolean exists(final ITResourceInstance instance)
    throws ServiceException {

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(TYPE_KEY, definitionKey(instance));
    filter.put(NAME, instance.name());
    try {
      tcResultSet resultSet = this.instanceFacade.findITResourceInstances(filter);
      return (resultSet.getRowCount() == 1);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instanceKey
  /**
   ** Returns an existing <code>IT Resource</code> instance of the Identity
   ** Manager through the discovered {@link tcITResourceInstanceOperationsIntf}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private long instanceKey(final ITResourceInstance instance)
    throws ServiceException {

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(TYPE_KEY, definitionKey(instance));
    filter.put(NAME, instance.name());
    try {
      tcResultSet    resultSet = this.instanceFacade.findITResourceInstances(filter);
      final String[] arguments = { NAME, instance.name() };
      if (resultSet.getRowCount() == 0) {
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
        else
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
      }
      if (resultSet.getRowCount() > 1) {
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_AMBIGUOS, arguments);
        else
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, arguments));
      }
      return resultSet.getLongValue(KEY);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   definitionKey
  /**
   ** Returns an existing <code>IT Resource</code> type of the Identity Manager
   ** Server through the discovered
   ** {@link tcITResourceDefinitionOperationsIntf}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private String definitionKey(final ITResourceInstance instance)
    throws ServiceException {

    final Map<String, String> filter = new HashMap<String, String>();
    try {
      filter.put(TYPE_NAME, instance.type());
      tcResultSet    resultSet = this.definitionFacade.getITResourceDefinition(filter);
      final String[] arguments = { TYPE_NAME, instance.type() };
      if (resultSet.getRowCount() == 0) {
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
        else
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
      }
      if (resultSet.getRowCount() > 1) {
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_AMBIGUOS, arguments);
        else
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, arguments));
      }
      return resultSet.getStringValue(TYPE_KEY);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }
}