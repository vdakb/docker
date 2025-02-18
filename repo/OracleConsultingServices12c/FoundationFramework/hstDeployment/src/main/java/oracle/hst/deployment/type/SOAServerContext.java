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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities

    File        :   SOAServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SOAServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import java.util.Map;
import java.util.HashMap;

import java.net.URL;
import java.net.MalformedURLException;

import oracle.bpel.services.workflow.WorkflowException;

import oracle.bpel.services.workflow.client.IWorkflowServiceClient;
import oracle.bpel.services.workflow.client.IWorkflowServiceClientConstants;
import oracle.bpel.services.workflow.client.WorkflowServiceClientFactory;

import oracle.bpel.services.workflow.task.ITaskService;
import oracle.bpel.services.workflow.user.IUserMetadataService;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.report.ITaskReportService;
import oracle.bpel.services.workflow.metadata.ITaskMetadataService;
import oracle.bpel.services.workflow.runtimeconfig.IRuntimeConfigService;

import oracle.integration.platform.blocks.deploy.servlet.CompositeDeployerClient;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class SOAServerContext
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>SOAServerContext</code> server is a special server and runtime
 ** implementation of {@link ServerContext} tooling that can adjust its
 ** behaviour by a server type definition file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SOAServerContext extends RMIServerContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String      CONTEXT_TYPE = "soa";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private IWorkflowServiceClient  platform     = null;
  private CompositeDeployerClient deployer     = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class WebLogic
  // ~~~~~ ~~~~~~~~
  /**
   ** <code>WebLogic</code> defines the attribute restriction to
   ** {@link ServerType.WebLogic}.
   */
  public static class WebLogic extends SOAServerContext{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>WebLogic</code> server context for the specified
     ** <code>host</code> and <code>port</code>.
     ** <br>
     ** The required security context is provided by <code>username</code> and
     ** <code>password</code>.
     ** <p>
     ** <b>Note</b>:
     ** This constructor is mainly used for testing prupose only.
     ** The ANT task and type that leverage this type will be use the non-arg
     ** default constructor and inject their configuration values by the
     ** appropriate setters.
     **
     ** @param  host             the value for the attribute <code>host</code>
     **                          used as the RMI/JNDI provider.
     **                          Allowed object is {@link String}.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the RMI/JNDI provider.
     **                          Allowed object is {@link String}.
     ** @param  contextURL       the fullqualified context URL to the server.
     **                          Allowed object is {@link String}.
     ** @param  username         the name of the administrative user.
     **                          Allowed object is {@link String}.
     ** @param  password         the password of the administrative user.
     **                          Allowed object is {@link String}.
     */
    public WebLogic(final String host, final String port, final String contextURL, final String username, final String password){
      // ensure inheritance
      super(new ServerType.WebLogic(), "t3", host, port, "weblogic.jndi.WLInitialContextFactory", contextURL, username, password);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>WebLogic</code> server context for the specified
     ** <code>host</code> and <code>port</code>.
     ** <br>
     ** The required security context is provided by {@link SecurityPrincipal}
     ** <code>principal</code>.
     ** <p>
     ** <b>Note</b>:
     ** This constructor is mainly used for testing prupose only.
     ** The ANT task and type that leverage this type will be use the non-arg
     ** default constructor and inject their configuration values by the
     ** appropriate setters..
     **
     ** @param  host             the value for the attribute <code>host</code>
     **                          used as the RMI/JNDI provider.
     **                          Allowed object is {@link String}.
     ** @param  port             the value for the attribute <code>port</code>
     **                          used as the RMI/JNDI provider.
     **                          Allowed object is {@link String}.
     ** @param  contextURL       the fullqualified context URL to the server.
     **                          Allowed object is {@link String}.
     ** @param  principal        the security principal used to establish a
     **                          connection to the server.
     **                          Allowed object is {@link SecurityPrincipal}.
     */
    public WebLogic(final String host, final String port, final String contextURL, final SecurityPrincipal principal){
      // ensure inheritance
      super(new ServerType.WebLogic(), "t3", host, port, "weblogic.jndi.WLInitialContextFactory", contextURL, principal);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SOAServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SOAServerContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SOAServerContext</code> for the specified
   ** <code>type</code>, <code>protocol</code>, <code>host</code> and
   ** <code>port</code>.
   ** <br>
   ** The required security context is provided by <code>username</code> and
   ** <code>password</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the RMI/JNDI provider.
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the RMI/JNDI
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the RMI/JNDI provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the RMI/JNDI provider.
   **                            Allowed object is {@link String}.
   ** @param  contextFactory     the context factory to bind to the server.
   **                            Allowed object is {@link String}.
   ** @param  contextURL         the fullqualified context URL to the server.
   **                            Allowed object is {@link String}.
   ** @param  username           the name of the administrative user.
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            Allowed object is {@link String}.
   */
  public SOAServerContext(final ServerType type, final String protocol, final String host, final String port, final String contextFactory, final String contextURL, final String username, final String password) {
    // ensure inheritance
    super(type, protocol, host, port, contextFactory, contextURL, username, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SOAServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the RMI/JNDI provider.
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the RMI/JNDI
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the RMI/JNDI provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the RMI/JNDI provider.
   **                            Allowed object is {@link String}.
   ** @param  contextFactory     the context factory to bind to the server.
   **                            Allowed object is {@link String}.
   ** @param  contextURL         the fullqualified context URL to the server.
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  public SOAServerContext(final ServerType type, final String protocol, final String host, final String port, final String contextFactory, final String contextURL, final SecurityPrincipal principal) {
    // ensure inheritance
    super(type, protocol, host, port, contextFactory, contextURL, principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platform
  /**
   ** Returns the logical context object of this task.
   **
   ** @return                    the logical context object of this task.
   */
  public final IWorkflowServiceClient platform() {
    return this.platform;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runtimeConfig
  /**
   ** Returns the runtime config service.
   **
   ** @return                    an {@link IRuntimeConfigService} providing
   **                            access to the runtime config service.
   **                            Possible object is
   **                            {@link IRuntimeConfigService}.
   **
   ** @throws ServiceException   if context isn't in the shape to operate due
   **                            to missing connectivity.
   */
  public final IRuntimeConfigService runtimeConfig()
    throws ServiceException {

    // prevent bogus instance state
    if (this.platform == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return this.platform.getRuntimeConfigService();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskService
  /**
   ** Returns the task service for remote and local EJBs.
   **
   ** @return                    an {@link ITaskService} providing access to the
   **                            task service.
   **                            Possible object is {@link ITaskService}.
   **
   ** @throws ServiceException   if context isn't in the shape to operate due
   **                            to missing connectivity.
   */
  public final ITaskService taskService()
    throws ServiceException {

    // prevent bogus instance state
    if (this.platform == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return this.platform.getTaskService();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskMetadataService
  /**
   ** Returns the task metadata service.
   **
   ** @return                    an {@link ITaskMetadataService} providing
   **                            access to the task metadata service.
   **                            Possible object is
   **                            {@link ITaskMetadataService}.
   **
   ** @throws ServiceException   if context isn't in the shape to operate due
   **                            to missing connectivity.
   */
  public final ITaskMetadataService taskMetadataService()
    throws ServiceException {

    // prevent bogus instance state
    if (this.platform == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return this.platform.getTaskMetadataService();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskQueryService
  /**
   ** Returns the task query service.
   **
   ** @return                    an {@link ITaskQueryService} providing access
   **                            to the task query service.
   **                            Possible object is {@link ITaskQueryService}.
   **
   ** @throws ServiceException   if context isn't in the shape to operate due
   **                            to missing connectivity.
   */
  public final ITaskQueryService taskQueryService()
    throws ServiceException {

    // prevent bogus instance state
    if (this.platform == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return this.platform.getTaskQueryService();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskReportService
  /**
   ** Returns the task query service.
   **
   ** @return                    an {@link ITaskReportService} providing access
   **                            to the task query service.
   **                            Possible object is {@link ITaskReportService}.
   **
   ** @throws ServiceException   if context isn't in the shape to operate due
   **                            to missing connectivity.
   */
  public final ITaskReportService taskReportService()
    throws ServiceException {

    // prevent bogus instance state
    if (this.platform == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return this.platform.getTaskReportService();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userMetadataService
  /**
   ** Returns the user metadata service.
   **
   ** @return                    an {@link IUserMetadataService} providing
   **                            access to the task metadata service.
   **                            Possible object is
   **                            {@link IUserMetadataService}.
   **
   ** @throws ServiceException   if context isn't in the shape to operate due
   **                            to missing connectivity.
   */
  public final IUserMetadataService userMetadataService()
    throws ServiceException {

    // prevent bogus instance state
    if (this.platform == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return this.platform.getUserMetadataService();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deployer
  /**
   ** Return the deployment context.
   **
   ** @return                    the deployment context.
   **                            Possible object is
   **                            {@link CompositeDeployerClient}.
   **
   ** @throws ServiceException   if the composite deployer cannot be
   **                            instantiated.
   */
  public final CompositeDeployerClient deployer()
    throws ServiceException {

    if (this.deployer != null)
      return deployer;

    // prevent bogus instance state
    if (this.platform == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    final IRuntimeConfigService config= runtimeConfig();
    try {
      ;
      final URL url = new URL(config.getInternalServerURL());
      // Constructs an connection object using environment properties and
      // connection request controls.
      this.deployer = new CompositeDeployerClient(url);
      // switch of any logging output that will be produced
      this.deployer.setLogger(null);
      this.deployer.setBasicAuth("default", this.username(), this.password());
    }
    catch (MalformedURLException e) {
      throw new ServiceException(e);
    }
    catch (WorkflowException e) {
      throw new ServiceException(e);
    }
    return this.deployer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Creates the {@link Map} from the attributes of this task that afterwards
   ** can be passed to establish a connection to the target system.
   **
   ** @param  providerURL        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **
   ** @return                    the context this connector use to communicate
   **                            with the SOA server.
   **                            Possible object is {@link Map} where each
   **                            element is a {@link String} mapped to a
   **                            {@link String}.
   */
  private final Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> properties(final String providerURL) {
    final Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> environment = new HashMap<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String>();
    environment.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.CLIENT_TYPE,                 "REMOTE");
    environment.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_INITIAL_CONTEXT_FACTORY, this.contextFactory());
    environment.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_PROVIDER_URL,            providerURL);
    environment.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_PRINCIPAL,      this.username());
    environment.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_CREDENTIALS,    this.password());
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextType (AbstractContext)
  /**
   ** Returns the specific type of the implemented context.
   **
   ** @return                    the specific type of the implemented context.
   **                            Possible object is {@link String}.
   */
  @Override
  public String contextType() {
    return CONTEXT_TYPE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (overridden)
  /**
   ** Establish a connection to the SOA server and creates the client to the use
   ** during task execution.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @throws ServiceException   if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  public void connect()
    throws ServiceException {

    // everytime to check
    validate();

    if (this.platform == null) {
      // Passing environment in constructor disables lookup for environment in
      // setup. In any case, we can always enforce manual environment settings
      // by OIMClient.setLookupEnv(configEnv) method.
      try {
        this.platform = WorkflowServiceClientFactory.getWorkflowServiceClient(properties(serviceURL()), null);
        established(this.platform != null);
      }
      catch (WorkflowException e) {
        throw new ServiceException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (AbstractContext)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void disconnect()
    throws ServiceException {

    if (this.deployer != null)
      this.deployer.endSession();

    this.deployer = null;
    // we need to reset explicitly the attribute to null to be able to detect
    // new connection requests
    this.platform = null;
    established(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to perform.
   */
  @Override
  public void validate() {
    // a principal is required for this context type ...
    if (principal() == null)
      handleAttributeMissing("principal");

    // ... and therefore needs also be validated
    principal().validate();

    // ensure inheritance
    super.validate();
  }
}