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

    File        :   ProcessFormHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProcessFormHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ProcessFormHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>ProcessFormHandler</code> creates, deletes and configures a
 ** <code>Process Forms</code> in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProcessFormHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for <code>Process Form</code> instances. */
  static final String PREFIX = "Structure Utility.";

  /**
   ** the mapping key contained in a collection to specify that the form system
   ** key should be resolved
   */
  static final String KEY = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the form name
   ** should be resolved
   */
  static final String NAME = PREFIX + "Table Name";

  /**
   ** the mapping key contained in a collection to specify that the form type
   ** should be resolved
   */
  static final String TYPE = PREFIX + "Form Type";

  /**
   ** the mapping key contained in a collection to specify that the form name
   ** should be resolved
   */
  static final String DESCRIPTION = PREFIX + "Description";

  /**
   ** the mapping key contained in a collection to specify that the form active
   ** version should be resolved
   */
  static final String VERSION_ACTIVE = PREFIX + "Active Version";

  /**
   ** the mapping key contained in a collection to specify that the form latest
   ** version should be resolved
   */
  static final String VERSION_LATEST = PREFIX + "Latest Version";

  /** Column prefix name for <code>Process Form</code> columns. */
  static final String COLUMN_PREFIX = PREFIX + "Additional Columns.";

  /**
   ** the mapping key contained in a collection to specify that the column key
   ** should be resolved
   */
  static final String COLUMN_KEY = COLUMN_PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the column name
   ** should be resolved
   */
  static final String COLUMN_NAME = COLUMN_PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the column label
   ** should be resolved
   */
  static final String COLUMN_LABEL = COLUMN_PREFIX + "Field Label";

  /**
   ** the mapping key contained in a collection to specify that the column type
   ** should be resolved
   */
  static final String COLUMN_TYPE = COLUMN_PREFIX + "Field Type";

  /**
   ** the mapping key contained in a collection to specify that the column
   ** variant type should be resolved
   */
  static final String COLUMN_VARIANT = COLUMN_PREFIX + "Variant Type";

  /**
   ** the mapping key contained in a collection to specify that the column type
   ** should be resolved
   */
  static final String COLUMN_LENGTH = COLUMN_PREFIX + "Length";

  /**
   ** the mapping key contained in a collection to specify that the column
   ** default value should be resolved
   */
  static final String COLUMN_VALUE = COLUMN_PREFIX + "Default Value";

  /**
   ** the mapping key contained in a collection to specify that the column flag
   ** encrypted should be resolved
   */
  static final String COLUMN_ENCRYPTED = COLUMN_PREFIX + "Encrypted";

  /** Prefix name for <code>Process Form</code> child table properties. */
  static final String CHILD_PREFIX = PREFIX + "Child Tables.";

  /**
   ** the mapping key contained in a collection to specify that the child table
   ** key should be resolved
   */
  static final String CHILD_KEY = CHILD_PREFIX + "Child Key";

  /**
   ** the mapping key contained in a collection to specify that the child table
   ** version should be resolved
   */
  static final String CHILD_VERSION = CHILD_PREFIX + "Child Version";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the name of the <code>Process Form</code> instance to configure or to
   ** create
   */
  private ProcessFormInstance single;

  /**
   ** the list of the <code>Process Form</code> instance to configure or to
   ** create
   */
  private List<ProcessFormInstance> multiple = new ArrayList<ProcessFormInstance>();

  /**
   ** the business logic layer to operate on <code>Process Form</code>
   ** definitions
   */
  private tcFormDefinitionOperationsIntf definitionFacade;

  /**
   ** the business logic layer to operate on <code>Process Form</code>
   ** instances
   */
  private tcFormInstanceOperationsIntf instanceFacade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProcessFormHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ProcessFormHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Resource Object</code> instance of Identity
   ** Manager to handle.
   **
   ** @return                    the name of the <code>Process Form</code>
   **                            instance in Identity Manager.
   */
  public final String name() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the <code>Process Form</code>
   **                            instance in Identity Manager.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = new ProcessFormInstance();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Identity
   **                            Manager <code>Process Form</code> instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager <code>Process Form</code>
   **                            instance.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    if (this.single == null)
      this.single = new ProcessFormInstance();

    // add the value pair to the parameters
    this.single.addParameter(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager <code>Process Form</code>
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
      this.single = new ProcessFormInstance();

    // add the value pairs to the parameters
    this.single.addParameter(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
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

        for (ProcessFormInstance i : this.multiple)
          i.validate();
      }
      catch (Exception e) {
        throw new BuildException(e.getLocalizedMessage());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link ProcessFormInstance} to add.
   **
   ** @throws BuildException     if the specified {@link ProcessFormInstance} is
   **                            already assigned to this task.
   */
  public void addInstance(final ProcessFormInstance object)
    throws BuildException {

    // prevent bogus input
    if ((this.single != null && this.single.equals(object)) || this.multiple.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.multiple.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>Process Form</code> in Identity Manager through the
   ** discovered {@link tcFormDefinitionOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.instanceFacade   = task.service(tcFormInstanceOperationsIntf.class);
    this.definitionFacade = task.service(tcFormDefinitionOperationsIntf.class);
    try {
      if (this.single != null)
        create(this.single);
      for (ProcessFormInstance cursor : this.multiple)
        create(cursor);
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
   ** Deletes an existing <code>Process Form</code> from Identity Manager
   ** through the given {@link tcFormDefinitionOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.instanceFacade   = task.service(tcFormInstanceOperationsIntf.class);
    this.definitionFacade = task.service(tcFormDefinitionOperationsIntf.class);
    try {
      ;
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
   ** Mofifies an existing <code>Process Form</code> in Identity Manager through
   ** the discovered {@link tcFormDefinitionOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.instanceFacade   = task.service(tcFormInstanceOperationsIntf.class);
    this.definitionFacade = task.service(tcFormDefinitionOperationsIntf.class);
    try {
      if (this.single != null)
        modify(this.single);
      for (ProcessFormInstance cursor : this.multiple)
        modify(cursor);
    }
    finally {
      if (this.instanceFacade != null)
        this.instanceFacade.close();
      if (this.definitionFacade != null)
        this.definitionFacade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Returns <code>true</code> if a <code>Process Form</code> definition exists
   ** in Identity Manager through the discovered
   ** {@link tcFormDefinitionOperationsIntf}.
   **
   ** @param  instance           the {@link ProcessFormInstance} to check for
   **                            existance.
   **
   ** @return                    <code>true</code> if the
   **                            {@link ProcessFormInstance} exists in the
   **                            backend system; otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final ProcessFormInstance instance)
    throws ServiceException {

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(NAME, instance.name());
    try {
      tcResultSet resultSet = this.definitionFacade.findForms(filter);
      return (resultSet.getRowCount() == 1);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a user defined <code>Process Form</code> definition in Oracle
   ** Identity Manager through the discovered
   ** {@link tcFormDefinitionOperationsIntf}.
   **
   ** @param  instance          the {@link ProcessFormInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void create(final ProcessFormInstance instance)
    throws ServiceException {

    if (!exists(instance)) {
      info(FeatureResourceBundle.format(FeatureMessage.PROCESS_FORM_CREATE, instance.name()));
      try {
        final Map<String, String> parameter = new HashMap<String, String>();
        parameter.put(NAME, instance.name());
        parameter.put(DESCRIPTION, instance.stringParameter("description"));
        parameter.put(TYPE, "P");
        this.definitionFacade.createForm(parameter);
        info(FeatureResourceBundle.format(FeatureMessage.PROCESS_FORM_CREATED, instance.name()));
      }
      catch (Exception e) {
        final String message = FeatureResourceBundle.format(FeatureError.PROCESS_FORM_CREATE, instance.name(), e.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
    else {
      warning(FeatureResourceBundle.format(FeatureError.PROCESS_FORM_EXISTS, instance.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies a user defined <code>Process Form</code> definition in Oracle
   ** Identity Manager through the discovered
   ** {@link tcFormDefinitionOperationsIntf}.
   **
   ** @param  instance           the {@link ProcessFormInstance} to configure.
   **
   ** @throws BuildException     in either the <code>Process Form</code> does
   **                            not exists or an attribute is not defined on
   **                            the <code>Process Form</code> definition.
   ** @throws ServiceException   in case an unhandled rror does occur.
   */
  private void modify(final ProcessFormInstance instance)
    throws ServiceException {

    if (!exists(instance)) {
      final String[] arguments = { NAME, instance.name() };
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    else {
      info(FeatureResourceBundle.format(FeatureMessage.PROCESS_FORM_MODIFY, instance.name()));
      try {
        info(FeatureResourceBundle.format(FeatureMessage.PROCESS_FORM_MODIFIED, instance.name()));
      }
      catch (Exception e) {
        final String message = FeatureResourceBundle.format(FeatureError.PROCESS_FORM_MODIFY, instance.name(), e.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   definitionKey
  /**
   ** Returns an existing <code>Process Form</code> definition of the Oracle
   ** Identity Manager through the discovered
   ** {@link tcFormDefinitionOperationsIntf}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private String definitionKey(final ProcessFormInstance instance)
    throws ServiceException {

    final Map<String, String> filter = new HashMap<String, String>();
    try {
      filter.put(NAME, instance.name());
      tcResultSet    resultSet = this.definitionFacade.findForms(filter);
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
      return resultSet.getStringValue(KEY);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }
}