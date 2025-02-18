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

    File        :   WorkflowHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    WorkflowHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.workflowservice.vo.WorkflowDefinition;

import oracle.iam.platformservice.api.WorkflowRegistrationService;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class WorkflowHandler
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>WorkflowHandler</code> registers, enables, disables and lists
 ** workflows in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WorkflowHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for workflows. */
  public static final String          PREFIX = "Workflow";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the User Identity Instance to configure or to create */
  private WorkflowInstance            single;

  /** the name of the User Identity to configure or to create */
  private List<WorkflowInstance>      multiple       = new ArrayList<WorkflowInstance>();

  /** the business logic layer to operate on workflows */
  private WorkflowRegistrationService facade;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>WorkflowHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public WorkflowHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Called to inject the <code>partition</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  partition          the <code>partition</code> of the workflow
   **                            related to Identity Manager.
   */
  public void partition(final String partition) {
    if (this.single == null)
      this.single = new WorkflowInstance();

    this.single.partition(partition);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns the <code>partition</code> of the workflow related to Identity
   ** Manager.
   **
   ** @return                    the <code>partition</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String partition() {
    return this.single == null ? null : this.single.partition();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the <code>name</code> of the workflow related to Identity Manager.
   **
   ** @return                    the workflow of the workflow related to
   **                            Identity Manager.
   */
  public final String name() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revision
  /**
   ** Called to inject the <code>revision</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  revision           the revision ID of the composite related to
   **                            Identity Manager and/or SOA Suite.
   */
  public void revision(final String revision) {
    if (this.single == null)
      this.single = new WorkflowInstance();

    this.single.revision(revision);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   revision
  /**
   ** Returns the <code>revision</code> of the workflow related to Identity
   ** Manager.
   **
   ** @return                    the revision ID of the composite related to
   **                            Identity Manager and/or SOA Suite.
   */
  public final String revision() {
    return this.single == null ? null : this.single.revision();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Called to inject the <code>service</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  service            the <code>service</code> of the workflow
   **                            related to Identity Manager.
   */
  public void service(final String service) {
    if (this.single == null)
      this.single = new WorkflowInstance();

    this.single.service(service);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns the <code>service</code> of the workflow related to Identity
   ** Manager.
   **
   ** @return                    the <code>service</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String service() {
    return this.single == null ? null : this.single.service();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Called to inject the <code>category</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  category           the <code>category</code> of the workflow
   **                            related to Identity Manager.
   */
  public void category(final String category) {
    if (this.single == null)
      this.single = new WorkflowInstance();

    this.single.category(category);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns the <code>category</code> of the workflow related to Identity
   ** Manager.
   **
   ** @return                    the <code>category</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String category() {
    return this.single == null ? null : this.single.category();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Called to inject the <code>provider</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  provider           the <code>type</code> of the workflow related
   **                            to Identity Manager.
   */
  public void provider(final String provider) {
    if (this.single == null)
      this.single = new WorkflowInstance();

    this.single.provider(provider);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the <code>provider</code> of the workflow related to Identity
   ** Manager.
   **
   ** @return                    the <code>provider</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String provider() {
    return this.single == null ? null : this.single.provider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   payloadID
  /**
   ** Called to inject the <code>payloadID</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  payloadID          the <code>payloadID</code> of the workflow
   **                            related to Identity Manager.
   */
  public void payloadID(final String payloadID) {
    if (this.single == null)
      this.single = new WorkflowInstance();

    this.single.payloadID(payloadID);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   payloadID
  /**
   ** Returns the <code>payloadID</code> of the workflow related to Identity
   ** Manager.
   **
   ** @return                    the <code>payloadID</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String payloadID() {
    return this.single == null ? null : this.single.payloadID();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationID
  /**
   ** Called to inject the <code>operationID</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  operationID        the <code>operationID</code> of the workflow
   **                            related to Identity Manager.
   */
  public void operationID(final String operationID) {
    if (this.single == null)
      this.single = new WorkflowInstance();

    this.single.operationID(operationID);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   operationID
  /**
   ** Returns the <code>operationID</code> of the workflow related to Identity
   ** Manager.
   **
   ** @return                    the <code>operationID</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String operationID() {
    return this.single == null ? null : this.single.operationID();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   canonical
  /**
   ** Returns the <code>canonical</code> name of the workflow related to
   ** Identity Manager.
   **
   ** @return                    the <code>canonical</code> name of the workflow
   **                            related to Identity Manager.
   */
  public final String canonical() {
    return this.single == null ? null : this.single.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after or during an registration operation.
   **
   ** @param  name               the name of the parameter of the Identity
   **                            Manager workflow.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager workflow.
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
   **                            Identity Manager workflow.
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
   ** Called to inject the <code>name</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  name               the <code>name</code> of the workflow
   **                            related to Identity Manager.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = new WorkflowInstance();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified instance to the parameters that has to be applied.
   **
   ** @param  instance           the {@link WorkflowInstance} to add.
   **
   ** @throws BuildException     if the specified {@link WorkflowInstance} is
   **                            already assigned to this task.
   */
  public void addInstance(final WorkflowInstance instance) {
    // prevent bogus input
    if ((this.single != null && this.single.equals(instance)) || this.multiple.contains(instance))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, instance));

    // add the instance to the object to handle
    this.multiple.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers a new workflow in Identity Manager through the given
   ** {@link WorkflowRegistrationService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void register(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(WorkflowRegistrationService.class);

    if (this.single != null)
      register(this.single);

    for (WorkflowInstance i : this.multiple)
      register(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an existing workflow from Identity Manager through the discovered
   ** {@link WorkflowRegistrationService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void enable(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(WorkflowRegistrationService.class);

    if (this.single != null)
      enable(this.single);

    for (WorkflowInstance i : this.multiple)
      enable(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an existing workflow from Identity Manager through the discovered
   ** {@link WorkflowRegistrationService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disable(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(WorkflowRegistrationService.class);

    if (this.single != null)
      disable(this.single);

    for (WorkflowInstance i : this.multiple)
      disable(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   print
  /**
   ** Prints all existing workflows from Identity Manager through the discovered
   ** {@link WorkflowRegistrationService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void print(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(WorkflowRegistrationService.class);

    if (this.single != null)
      list(this.single);

    for (WorkflowInstance i : this.multiple)
      list(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link WorkflowInstance} exists in Identity
   ** Manager through the discovered {@link WorkflowRegistrationService}.
   **
   ** @param  instance           the {@link WorkflowInstance} to check for
   **                            existance.
   **
   ** @return                    <code>true</code> if the
   **                            {@link WorkflowInstance} exists in the backend
   **                            system; otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final WorkflowInstance instance)
    throws ServiceException {

    final List<WorkflowDefinition> resultSet = find(instance);
    // a precondition is that we have exactly one entry
    if (resultSet.size() != 1)
      return false;

    return (resultSet.size() == 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Lists all existing workflows within the Identity Manager through the
   ** discovered {@link WorkflowRegistrationService}.
   **
   ** @param  instance           the {@link WorkflowInstance} to list as a
   **                            pattern.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void list(final WorkflowInstance instance)
    throws ServiceException {

    info(FeatureResourceBundle.format(FeatureMessage.WORKFLOW_INVENTORY_SEARCH, instance.category(), instance));
    final List<WorkflowDefinition> resultSet = find(instance);
    if (resultSet.size() == 0)
      error(FeatureResourceBundle.string(FeatureMessage.WORKFLOW_INVENTORY_EMPTY));
    else {
      final Locale        locale = Locale.getDefault();
      final StringBuilder builder = new StringBuilder();
      final Formatter     formatter = new Formatter(builder);
      formatter.format(
        locale
      , "%-50s| %-10s| %-20s| %-10s"
      , FeatureResourceBundle.string(FeatureMessage.WORKFLOW_INVENTORY_NAME)
      , FeatureResourceBundle.string(FeatureMessage.WORKFLOW_INVENTORY_STATUS)
      , FeatureResourceBundle.string(FeatureMessage.WORKFLOW_INVENTORY_CATEGORY)
      , FeatureResourceBundle.string(FeatureMessage.WORKFLOW_INVENTORY_PROVIDER)
      );
      warning(FeatureResourceBundle.string(FeatureMessage.WORKFLOW_INVENTORY_SEPARATOR));
      warning(builder.toString());
      warning(FeatureResourceBundle.string(FeatureMessage.WORKFLOW_INVENTORY_SEPARATOR));
      for (WorkflowDefinition workflow : resultSet) {
        builder.delete(0, builder.length());
        formatter.format(locale, "%-50s| %-10b| %-20s| %-40s", workflow.getName(), workflow.isEnabled(), workflow.getCategory(), workflow.getProviderType());
        warning(builder.toString());
      }
      warning(FeatureResourceBundle.string(FeatureMessage.WORKFLOW_INVENTORY_SEPARATOR));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers a new workflow in Identity Manager through the discovered
   ** {@link WorkflowRegistrationService}.
   **
   ** @param  instance           the {@link WorkflowInstance} to register.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void register(final WorkflowInstance instance)
    throws ServiceException {

    final String[] argument = { PREFIX, instance.toString() };
    if (exists(instance))
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, argument));
    else {
      try {
        info(FeatureResourceBundle.format(FeatureMessage.WORKFLOW_INVENTORY_REGISTER, argument[1], instance.category()));
        this.facade.registerWorkFlowDefinition(instance.toDefinition());
        info(FeatureResourceBundle.format(FeatureMessage.WORKFLOW_INVENTORY_REGISTERED, argument[1], instance.category()));
      }
      catch (Exception e) {
        error(FeatureResourceBundle.format(FeatureError.WORKFLOW_REGISTER_ERROR, argument[1], instance.category()));
        error(e.getLocalizedMessage());
        if (failonerror())
          throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an existing workflow in Identity Manager through the discovered
   ** {@link WorkflowRegistrationService}.
   **
   ** @param  instance           the {@link WorkflowInstance} to enable.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void enable(final WorkflowInstance instance)
    throws ServiceException {

    final String[] argument = { PREFIX, instance.toString() };
    if (!exists(instance)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, argument));
    }
    else
      try {
        info(FeatureResourceBundle.format(FeatureMessage.WORKFLOW_INVENTORY_ENABLE, argument[1], instance.category()));
        this.facade.enableWorkFlowDefinition(argument[1]);
        info(FeatureResourceBundle.format(FeatureMessage.WORKFLOW_INVENTORY_ENABLED, argument[1], instance.category()));
      }
      catch (Exception e) {
        error(FeatureResourceBundle.format(FeatureError.WORKFLOW_ENABLE_ERROR, argument[1], instance.category()));
        error(e.getLocalizedMessage());
        if (failonerror())
          throw new ServiceException(ServiceError.UNHANDLED, e);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an existing workflow in Identity Manager through the discovered
   ** {@link WorkflowRegistrationService}.
   **
   ** @param  instance           the {@link WorkflowInstance} to enable.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disable(final WorkflowInstance instance)
    throws ServiceException {

    final String[] argument = { PREFIX, instance.toString() };
    if (!exists(instance))
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, argument));
    else
      try {
        info(FeatureResourceBundle.format(FeatureMessage.WORKFLOW_INVENTORY_DISABLE, argument[1], instance.category()));
        this.facade.disableWorkflowDefinition(argument[1]);
        info(FeatureResourceBundle.format(FeatureMessage.WORKFLOW_INVENTORY_DISABLED, argument[1], instance.category()));
      }
      catch (Exception e) {
        error(FeatureResourceBundle.format(FeatureError.WORKFLOW_DISABLE_ERROR, argument[1], instance.category()));
        error(e.getLocalizedMessage());
        if (failonerror())
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

    try {
      if (this.single != null)
        this.single.validate(false);

      for (WorkflowInstance i : this.multiple)
        i.validate(false);
    }
    catch (Exception e) {
      throw new BuildException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Find all existing workflows within the Identity Manager through the
   ** discovered {@link WorkflowRegistrationService} that matches the specified
   ** {@link WorkflowInstance}.
   **
   ** @param  instance           the {@link WorkflowInstance} to list as a
   **                            pattern.
   **
   ** @return                    the {@link List} of {@link WorkflowDefinition}s
   **                            the matche the specified criteria.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private List<WorkflowDefinition> find(final WorkflowInstance instance)
    throws ServiceException {

    try {
      return this.facade.getWorkflowDefinitions(instance.category(), instance.toString());
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }
}