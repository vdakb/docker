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

    File        :   CompositeHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CompositeHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.soa.management.util.ComponentFilter;

import oracle.soa.management.facade.Locator;
import oracle.soa.management.facade.Composite;
import oracle.soa.management.facade.LocatorFactory;

import oracle.bpel.services.workflow.WorkflowException;

import oracle.bpel.services.workflow.metadata.ITaskMetadataService;
import oracle.bpel.services.workflow.metadata.taskdefinition.model.TaskDefinition;

import oracle.bpel.services.workflow.runtimeconfig.IRuntimeConfigService;

import oracle.bpel.services.workflow.runtimeconfig.model.ObjectFactory;
import oracle.bpel.services.workflow.runtimeconfig.model.TaskDisplayInfoType;

import oracle.bpel.services.workflow.verification.IWorkflowContext;

import oracle.tip.pc.services.identity.config.ISConfigSchemaConstants;

import oracle.integration.platform.blocks.deploy.servlet.CompositeDeployerClient;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.SOAServerContext;

import oracle.hst.deployment.task.ServiceProvider;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class CompositeHandler
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>CompositeHandler</code> deployes, undeploys and redeploys composites
 ** in Oracle SOA Suite.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CompositeHandler extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for deployment. */
  public static final String PREFIX         = "Composite";

  /** Standard security identity context used by authentications. */
  public static final String DEFAULT_REALM  = ISConfigSchemaConstants.DEFAULT_REALM_NAME;

  /** Default engine type used by query service. */
  public static final String DEFAULT_ENGINE = "workflow";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** a single composite to deploy. */
  private CompositeInstance                   single   = null;

  /** the collevtion of composite to deploy. */
  private final Collection<CompositeInstance> multiple = new ArrayList<CompositeInstance>();

  private SOAServerContext                    context  = null;
  private Locator                             locator  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>CompositeHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public CompositeHandler(final ServiceFrontend frontend) {
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
   ** Identity Manager and/or Oracle SOA Suite.
   ** <p>
   ** The name of the partition in which to deploy the SOA composite
   ** application. The default value is <code>default</code>. If you do not
   ** specify a partition, the composite is automatically deployed into the
   ** default partition.
   **
   ** @param  partition          the name of the partition in which to deploy
   **                            the SOA composite application. The default
   **                            value is <code>default</code>.
   */
  public void partition(final String partition) {
    if (this.single == null)
      this.single = new CompositeInstance();

    this.single.partition(partition);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns the <code>partition</code> of the workflow related to Oracle
   ** Identity Manager and/or Oracle SOA Suite.
   ** <p>
   ** The name of the partition in which to deploy the SOA composite
   ** application. The default value is <code>default</code>. If you do not
   ** specify a partition, the composite is automatically deployed into the
   ** default partition.
   **
   ** @return                    the name of the partition in which to deploy
   **                            the SOA composite application. The default
   **                            value is <code>default</code>.
   */
  public final String partition() {
    return this.single == null ? null : this.single.partition();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Called to inject the <code>name</code> of the workflow related to
   ** Identity Manager and/or Oracle SOA Suite.
   **
   ** @param  name               the name of the SOA composite application.
   */
  public void name(final String name) {
    if (this.single == null)
      this.single = new CompositeInstance();

    this.single.name(name);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the <code>name</code> of the workflow related to Identity Manager
   ** and/or Oracle SOA Suite.
   **
   ** @return                    the workflow of the workflow related to Oracle
   **                            Identity Manager and/or Oracle SOA Suite.
   */
  public final String name() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revision
  /**
   ** Called to inject the <code>revision</code> of the workflow related to
   ** Oracle SOA Suite.
   **
   ** @param  version            the revision ID of the composite related to
   **                            Oracle SOA Suite.
   */
  public void revision(final String version) {
    if (this.single == null)
      this.single = new CompositeInstance();

    this.single.revision(version);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   revision
  /**
   ** Returns the <code>revision</code> of the composite related to Oracle
   ** SOA Suite.
   **
   ** @return                    the revision ID of the composite related to
   **                            Oracle SOA Suite.
   */
  public final String revision() {
    return this.single == null ? null : this.single.revision();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Called to inject the <code>location</code> of the composite related to
   ** Oracle SOA Suite.
   ** <p>
   ** This propery specifies the absolute path to one the following:
   ** <ul>
   **   <li>SOA archive (SAR) file.
   **       <br>
   **       A SAR file is a special JAR file that requires a prefix of
   **       <code>sca_</code> (for example,
   **       <code>sca_HelloWorld_rev1.0.jar</code>). The SAR file can be
   **       deployed with the deployment commands, but a regular
   **       <code>.jar</code> file is not treated as a special SAR file.
   **   <li>ZIP file that includes multiple SARs, metadata archives (MARs), or
   **       both.
   **   <li>Enterprise archive (EAR) file that contains a SAR file.
   ** </ul>
   **
   ** @param  location           the packed archive of the composite related
   **                            to Oracle SOA Suite.
   */
  public void location(final File location) {
    // check if we have this file already
    if (this.multiple.contains(location))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_FILE_ONLYONCE, location.getAbsolutePath()));

    if (this.single == null)
      this.single = new CompositeInstance();

    this.single.location(location);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Returns the <code>location</code> of the composite related to Oracle SOA
   ** Suite.
   ** <p>
   ** This propery specifies the absolute path to one the following:
   ** <ul>
   **   <li>SOA archive (SAR) file.
   **       <br>
   **       A SAR file is a special JAR file that requires a prefix of
   **       <code>sca_</code> (for example,
   **       <code>sca_HelloWorld_rev1.0.jar</code>). The SAR file can be
   **       deployed with the deployment commands, but a regular
   **       <code>.jar</code> file is not treated as a special SAR file.
   **   <li>ZIP file that includes multiple SARs, metadata archives (MARs), or
   **       both.
   **   <li>Enterprise archive (EAR) file that contains a SAR file.
   ** </ul>
   **
   ** @return                    the <code>file</code> of the composite related
   **                            to Oracle SOA Suite.
   */
  public final File location() {
    return this.single == null ? null : this.single.location();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   plan
  /**
   ** Called to inject the <code>plan</code> of the composite related to Oracle
   ** SOA Suite.
   ** <p>
   ** This property specifies the absolute path of a configuration plan to be
   ** applied to a specified SAR file or to all SAR files included in the ZIP
   ** file.
   **
   ** @param  plan               the <code>plan</code> of the composite related
   **                            to Oracle SOA Suite.
   */
  public void plan(final File plan) {
    if (this.single == null)
      this.single = new CompositeInstance();

    this.single.plan(plan);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   plan
  /**
   ** Returns the <code>plan</code> of the composite related to Oracle SOA
   ** Suite.
   ** <p>
   ** This property specifies the absolute path of a configuration plan to be
   ** applied to a specified SAR file or to all SAR files included in the ZIP
   ** file.
   **
   ** @return                    the <code>plan</code> of the composite related
   **                            to Oracle SOA Suite.
   */
  public final File plan() {
    return this.single == null ? null : this.single.plan();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   overwrite
  /**
   ** Called to inject the <code>overwrite</code> of the composite related
   ** to Oracle SOA Suite.
   ** <p>
   ** This property indicates whether to overwrite an existing SOA composite
   ** application file.
   ** <ul>
   **   <li><code>false</code> (default) - does not overwrite the file.
   **   <li><code>true</code>            - overwrites the file.
   ** </ul>
   **
   ** @param  overwrite          the <code>overwrite</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public void overwrite(final boolean overwrite) {
    if (this.single == null)
      this.single = new CompositeInstance();

    this.single.overwrite(overwrite);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   overwrite
  /**
   ** Returns the <code>overwrite</code> of the composite related to Oracle
   ** SOA Suite.
   ** <p>
   ** This property indicates whether to overwrite an existing SOA composite
   ** application file.
   ** <ul>
   **   <li><code>false</code> (default) - does not overwrite the file.
   **   <li><code>true</code>            - overwrites the file.
   ** </ul>
   **
   ** @return                    the <code>overwrite</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public final boolean overwrite() {
    return this.single == null ? false : this.single.overwrite();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keepOnRedeploy
  /**
   ** Called to inject the <code>keepOnRedeploy</code> of the composite related
   ** to Oracle SOA Suite.
   **
   ** @param  keepOnRedeploy     the <code>keepOnRedeploy</code> of the
   **                            composite related to Oracle SOA Suite.
   */
  public void keepOnRedeploy(final boolean keepOnRedeploy) {
    if (this.single == null)
      this.single = new CompositeInstance();

    this.single.keepOnRedeploy(keepOnRedeploy);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   keepOnRedeploy
  /**
   ** Returns the <code>keepOnRedeploy</code> of the composite related to Oracle
   ** SOA Suite.
   **
   ** @return                    the <code>keepOnRedeploy</code> of the
   **                            composite related to Oracle SOA Suite.
   */
  public final boolean keepOnRedeploy() {
    return this.single == null ? false : this.single.keepOnRedeploy();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forceDefault
  /**
   ** Called to inject the <code>forceDefault</code> of the composite related
   ** to Oracle SOA Suite.
   ** <p>
   ** This property indicates whether to set the new composite as the default.
   ** <ul>
   **   <li><code>true</code> (default) - makes it the default composite.
   **   <li><code>false</code>          - does not make it the default
   **                                     composite.
   ** </ul>
   **
   ** @param  forceDefault       the <code>forceDefault</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public void forceDefault(final boolean forceDefault) {
    if (this.single == null)
      this.single = new CompositeInstance();

    this.single.forceDefault(forceDefault);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   forceDefault
  /**
   ** Returns the <code>forceDefault</code> of the composite related to Oracle
   ** SOA Suite.
   ** <p>
   ** This property indicates whether to set the new composite as the default.
   ** <ul>
   **   <li><code>true</code> (default) - makes it the default composite.
   **   <li><code>false</code>          - does not make it the default
   **                                     composite.
   ** </ul>
   **
   ** @return                    the <code>forceDefault</code> of the composite
   **                            related to Oracle SOA Suite.
   */
  public final boolean forceDefault() {
    return this.single == null ? false : this.single.forceDefault();
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
  public void validate() {
    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    try {
      if (this.single != null)
        this.single.validate();

      for (CompositeInstance i : this.multiple)
        i.validate();
    }
    catch (Exception e) {
      throw new BuildException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Called to add a sinlge file to the set of file to import.
   **
   ** @param  instance           the {@link CompositeInstance} where an
   **                            deployment has to do for.
   **
   ** @throws BuildException     if the specified {@link File} is already part
   **                            of this import operation.
   */
  public void addInstance(final CompositeInstance instance) {
    // check if we have this file already
    if ((this.single != null && this.single.equals(instance)) || (this.multiple != null && this.multiple.contains(instance))) {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_FILE_ONLYONCE, instance.location().getAbsolutePath());
      error(message);
      if (failonerror())
        throw new BuildException(message);
    }
    else
      this.multiple.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deploy
  /**
   ** Deploys all composites to Oracle SOA Suite through the given
   ** {@link SOAServerContext}.
   **
   ** @param  context            the {@link SOAServerContext} used to perform
   **                            the operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void deploy(final SOAServerContext context)
    throws ServiceException {

    open(context);
    try {
      if (this.single != null)
        deploy(this.single);

      for (CompositeInstance i : this.multiple)
        deploy(i);
    }
    finally {
      close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   undeploy
  /**
   ** Undeploys a composite from Oracle SOA Suite through the given
   ** {@link SOAServerContext}.
   **
   ** @param  context            the {@link SOAServerContext} used to perform
   **                            the operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void undeploy(final SOAServerContext context)
    throws ServiceException {

    open(context);
    try {
      if (this.single != null)
        undeploy(this.single);

      for (CompositeInstance i : this.multiple)
        undeploy(i);
    }
    finally {
      close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redeploy
  /**
   ** Redeploys a composite to Oracle SOA Suite through the given
   ** {@link SOAServerContext}.
   **
   ** @param  context            the {@link SOAServerContext} used to perform
   **                            the operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void redeploy(final SOAServerContext context)
    throws ServiceException {

    open(context);
    try {
      if (this.single != null)
        redeploy(this.single);

      for (CompositeInstance i : this.multiple)
        redeploy(i);

      if (this.locator != null)
        this.locator.close();
    }
    finally {
      close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Configures all composites to Oracle SOA Suite through the given
   ** {@link SOAServerContext}.
   **
   ** @param  context            the {@link SOAServerContext} used to perform
   **                            the operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void configure(final SOAServerContext context)
    throws ServiceException {

    open(context);
    try {
      if (this.single != null)
        configure(this.single);

      for (CompositeInstance i : this.multiple)
        configure(i);

      if (this.locator != null)
        this.locator.close();
    }
    finally {
      close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open
  /**
   ** Factory method to create a locator that maps into a soa-infra instance to
   ** allow to search for component, instances, components, and composites.
   **
   ** @throws BuildException     if this {@link ServiceProvider} is not able to
   **                            create an ejb based  {@link Locator}.
   */
  private void open(final SOAServerContext context) {
    this.context = context;
    try {
      // create a locator that maps into a soa-infra instance to allow to search
      // for component, instances, components, and composites.
      // this facade allows to manage programmatically SOA composite
      // applications during runtime. The Facade API is part of Oracle SOA
      // Suite's Infrastructure Management Java API. The Facade API exposes
      // operations and attributes of composites, components, services,
      // references and so on. The Facade API provides an alternative to
      // managing composites with Oracle Enterprise Manager Fusion Middleware
      // Control.
      this.locator = LocatorFactory.createLocator(context.environment());
    }
    catch (Exception e) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_LOCATOR_ERROR, e.getLocalizedMessage(), StringUtility.formatCollection(context.environment())));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close the allocated resources maps into a soa-infra instance.
   */
  private void close() {
    this.context = null;
    if (this.locator != null)
      this.locator.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link CompositeInstance} exists in Oracle SOA
   ** Suite through the given {@link SOAServerContext}.
   **
   ** @param  instance           the {@link CompositeInstance} to check for
   **                            existance.
   */
  private boolean exists(final CompositeInstance instance) {
    final Composite composite = lookupComposite(instance);
    return composite != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deploy
  /**
   ** Deploys a composite to Oracle SOA Suite.
   **
   ** @param  instance           the {@link CompositeInstance} to deploy.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void deploy(final CompositeInstance instance)
    throws ServiceException {

    if (!exists(instance))
      deploy(instance, false);
    else {
      final String message = FeatureResourceBundle.format(FeatureMessage.COMPOSITE_EXISTS, instance.toString());
      warning(message);
      deploy(instance, true);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redeploy
  /**
   ** Redeploys a composite to Oracle SOA Suite.
   **
   ** @param  instance           the {@link CompositeInstance} to deploy.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void redeploy(final CompositeInstance instance)
    throws ServiceException {

    if (exists(instance))
      deploy(instance, true);
    else {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_WORKFLOW_NOTEXISTS, instance.toString());
      error(message);
      if (failonerror())
        throw new BuildException(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   undeploy
  /**
   ** Undeploys a composite to Oracle SOA Suite.
   **
   ** @param  instance           the {@link CompositeInstance} to undeploy.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void undeploy(final CompositeInstance instance)
    throws ServiceException {

    info(FeatureResourceBundle.format(FeatureMessage.COMPOSITE_UNDEPLOY, instance.name()));
    if (exists(instance)) {
      final CompositeDeployerClient deployer = this.context.deployer();
      deployer.setCompositeDN(instance.toString());
      deployer.setCommand(CompositeDeployerClient.COMMAND_UNDEPLOY);

      int response = request(deployer);
      if (response == 200)
        info(FeatureResourceBundle.format(FeatureMessage.COMPOSITE_UNDEPLOYED, instance.name()));
      else if (response != -1)
        error(FeatureResourceBundle.format(FeatureError.COMPOSITE_DEPLOYMENT_ERROR, instance.name(), deployer.getErrorMsg()));
    }
    else {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_WORKFLOW_NOTEXISTS, instance.toString());
      error(message);
      if (failonerror())
        throw new BuildException(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deploy
  /**
   ** Deploys or redeploys a composite to Oracle SOA Suite.
   **
   ** @param  instance           the {@link CompositeInstance} to deploy.
   ** @param  override           <code>true</code> if a redeployment of the
   **                            coposite has to be enforced.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void deploy(final CompositeInstance instance, final boolean override)
    throws ServiceException {

    info(FeatureResourceBundle.format(override ? FeatureMessage.COMPOSITE_REDEPLOY : FeatureMessage.COMPOSITE_DEPLOY, instance.location()));
    final CompositeDeployerClient deployer = this.context.deployer();
    deployer.setPartition(instance.partition());
    deployer.setOverwrite(instance.overwrite());
    deployer.setForceDefault(instance.forceDefault());
    deployer.setKeepInstancesOnRedeploy(instance.keepOnRedeploy());
    deployer.setSar(instance.location().getAbsolutePath());
    if (instance.plan() != null)
      deployer.setDeployPlan(instance.plan().getAbsolutePath());
    deployer.setCommand(override ? CompositeDeployerClient.COMMAND_REDEPLOY : CompositeDeployerClient.COMMAND_DEPLOY);

    int response = request(deployer);
    if (response == CompositeDeployerClient.SUCCESS) {
      info(FeatureResourceBundle.format(override ? FeatureMessage.COMPOSITE_REDEPLOYED : FeatureMessage.COMPOSITE_DEPLOYED, instance.location()));
      configure(instance);
    }
    else if (response != -1)
      error(FeatureResourceBundle.format(FeatureError.COMPOSITE_DEPLOYMENT_ERROR, instance.location(), deployer.getErrorMsg()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Sends the request to manage a composite to Oracle SOA Suite.
   **
   ** @param  deployer           the {@link CompositeDeployerClient} used to
   **                            perform the operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private int request(final CompositeDeployerClient deployer)
    throws ServiceException {

    int response = -1;
/*    
    try {
      response = deployer.sendRequest();
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(e);
    }
*/
    checkResponse(response, deployer.getErrorMsg());
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkResponse
  /**
   ** Determines if the response returned from the HTTP-Request is something
   ** that has to interrupt further processing.
   **
   ** @throws BuildException     in case an unrecoverable error has occured.
   */
  private void checkResponse(final int response, final String error) {
    if ((response != CompositeDeployerClient.SUCCESS) && (this.failonerror())) {
      if (response == CompositeDeployerClient.FAILURE) {
        if (!StringUtility.isEmpty(error)) {
          error(error);
          if (failonerror())
            throw new BuildException(error);
        }
        throw new BuildException(FeatureResourceBundle.string(FeatureError.COMPOSITE_CONNECTION_ERROR));
      }
      String message = null;
      if (response == -1) {
        message = FeatureResourceBundle.string(FeatureError.COMPOSITE_CONNECTION_TIMEOUT);
        error(message);
        if (failonerror())
          throw new BuildException(message);
      }
      message = FeatureResourceBundle.format(FeatureError.COMPOSITE_CONNECTION_REQUEST, response);
      error(message);
      if (failonerror())
        throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_CONNECTION_REQUEST, response));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Configures a composite in Oracle SOA Suite.
   **
   ** @param  instance           the {@link CompositeInstance} to configure.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void configure(final CompositeInstance instance)
    throws ServiceException {

    if (exists(instance)) {
      IWorkflowContext context = null;
      try {
        context = this.context.taskQueryService().authenticate(this.context.username(), this.context.password().toCharArray(), DEFAULT_REALM);
        configure(context, instance);
      }
      catch (Exception e) {
        throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_MANAGEMENT_ERROR, e.getLocalizedMessage()));
      }
      finally {
        if (context != null)
          try {
            this.context.taskQueryService().destroyWorkflowContext(context);
          }
          catch (WorkflowException e) {
            throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_WORKFLOW_ERROR, e.getLocalizedMessage()));
          }
      }
    }
    else {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_WORKFLOW_NOTEXISTS, instance.toString());
      error(message);
      if (failonerror())
        throw new BuildException(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Configures a composite in Oracle SOA Suite.
   **
   ** @param  instance           the {@link CompositeInstance} to configure.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void configure(final IWorkflowContext context, final CompositeInstance instance)
    throws ServiceException {

    info(FeatureResourceBundle.format(FeatureMessage.COMPOSITE_CONFIGURE, instance.toString()));
    // check first if we have something that needs to be configured because
    // the implementation below is time consuming
    if (CollectionUtility.empty(instance.humanTask())) {
      warning(FeatureResourceBundle.format(FeatureError.COMPOSITE_HUMANTASK_MISSING, instance.toString()));
      return;
    }

    final IRuntimeConfigService configService = this.context.runtimeConfig();
    try {
      final ObjectFactory       factory = new ObjectFactory();
      final Map<String, String> mapping = lookupNamespace(context, instance);
      for (CompositeInstance.Task task : instance.humanTask()) {
        final String                    namespace = mapping.get(task.name());
        final List<TaskDisplayInfoType> taskDisplay = new ArrayList<TaskDisplayInfoType>();
        for (CompositeInstance.Flow taskflow : task.taskFlow()) {
          final List<TaskDisplayInfoType> existing = configService.getTaskDisplayInfo(context, namespace, instance.revision(), instance.partition(), taskflow.name());
          TaskDisplayInfoType             flow = null;
          // if we got an empty list there is nothing configured hence we have
          // to create the task display info and add it to the list send to the
          // service later on
          if (CollectionUtility.empty(existing)) {
            flow = factory.createTaskDisplayInfoType();
            flow.setApplicationName(taskflow.name());
          }
          else if (existing.size() == 1) {
            flow = existing.get(0);
          }
          else {
            error("Oooops");
          }
          flow.setHostname(taskflow.host());
          if (taskflow.ssl()) {
            flow.setHttpPort("0");
            flow.setHttpsPort(String.valueOf(taskflow.port()));
          }
          else {
            flow.setHttpPort(String.valueOf(taskflow.port()));
            flow.setHttpsPort("0");
          }
          flow.setUri(taskflow.uri());
          taskDisplay.add(flow);
        }
        configService.setTaskDisplayInfo(context, namespace, instance.revision(), instance.partition(), taskDisplay);
      }
      info(FeatureResourceBundle.format(FeatureMessage.COMPOSITE_CONFIGURED, instance.toString()));
    }
    catch (WorkflowException e) {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_WORKFLOW_ERROR, e.getFaultName());
      error(message);
      if (failonerror())
        throw new BuildException(message);
    }
    catch (Exception e) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_MANAGEMENT_ERROR, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupComposite
  /**
   ** Returns the SCA {@link Composite} facade that belongs to the specified
   ** {@link CompositeInstance} by looking up the SCA {@link Composite} by its
   ** DN.
   **
   ** @param  instance           the {@link CompositeInstance} the
   **                            {@link Composite} facade is needed for.
   **
   ** @return                    the {@link Composite} found for
   **                            {@link CompositeInstance} or <code>null</code>
   **                            if the DN of {@link CompositeInstance} does not
   **                            exists.
   **
   ** @throws BuildException     if this {@link ServiceProvider} is configured
   **                            for fail on error and the {@link Composite}
   **                            does not exists.
   */
  private Composite lookupComposite(final CompositeInstance instance) {
    Composite composite = null;
    try {
      composite = this.locator.lookupComposite(instance.toString());
    }
    catch (Exception e) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_LOOKUP_ERROR, instance.toString(), e.getLocalizedMessage()));
    }
    return composite;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupNamespace
  /**
   ** Returns the SCA {@link Composite} namespaces for a
   ** {@link CompositeInstance} by querying the SCA {@link Composite} metadata.
   **
   ** @param  context            the administration context required to invoke
   **                            the metadata service.
   ** @param  instance           the {@link CompositeInstance} the
   **                            {@link Composite} facade is needed for.
   **
   ** @return                    the {@link Map} with the nampespaces the
   **                            belongs to the {@link CompositeInstance}.
   **
   ** @throws BuildException     if this operation fails in general.
   */
  private Map<String, String> lookupNamespace(final IWorkflowContext context, final CompositeInstance instance)
    throws ServiceException {

    final Map<String, String>  mapping = new HashMap<String, String>();
    final ITaskMetadataService service = this.context.taskMetadataService();
    // create filter used to find the human tasks deployed at the composite
    final ComponentFilter compfilter = new ComponentFilter();
    compfilter.setPartition(instance.partition());
    compfilter.setCompositeName(instance.name());
    compfilter.setRevision(instance.revision());
    compfilter.setEngineType(DEFAULT_ENGINE);
    try {
      for (CompositeInstance.Task task : instance.humanTask()) {
        compfilter.setComponentName(task.name());
        final TaskDefinition definition = service.getTaskDefinitionById(context, String.format("%s/%s", instance.toString(), task.name()));
        mapping.put(task.name(), definition.getTargetNamespace());
      }
    }
    catch (Exception e) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_MANAGEMENT_ERROR, e.getLocalizedMessage()));
    }
    return mapping;
  }
}