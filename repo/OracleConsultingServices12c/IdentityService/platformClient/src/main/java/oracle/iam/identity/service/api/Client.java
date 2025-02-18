/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   Platform.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Platform.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.service.api;

import java.util.List;
import java.util.Hashtable;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.security.auth.login.LoginException;

import Thor.API.Operations.tcUserOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.AttestationOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcAccessPolicyOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;

import java.util.logging.Logger;

import oracle.iam.platform.Role;
import oracle.iam.platform.OIMClient;

import oracle.iam.platform.utils.NoSuchServiceException;

import oracle.iam.configservice.api.ConfigManager;

import oracle.iam.conf.api.SystemConfigurationService;

import oracle.iam.api.OIMService;

import oracle.iam.platform.authopss.api.AuthorizationService;

import oracle.iam.platformservice.api.PlatformService;
import oracle.iam.platformservice.api.AdminRoleService;
import oracle.iam.platformservice.api.AuthorizationOPSSService;
import oracle.iam.platformservice.api.EntityPublicationService;

import oracle.iam.identity.rolemgmt.api.RoleManager;

import oracle.iam.identity.usermgmt.api.UserManager;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;

import oracle.iam.catalog.api.CatalogService;
import oracle.iam.catalog.api.MetaDataDefinitionManager;

import oracle.iam.accesspolicy.api.AccessPolicyService;

import oracle.iam.passwordmgmt.api.PasswordMgmtService;

import oracle.iam.application.api.ApplicationManager;
import oracle.iam.application.api.ConnectorInfoManager;

import oracle.iam.provisioning.api.ApplicationService;
import oracle.iam.provisioning.api.ConnectorInfoService;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.api.ConnectorServerService;
import oracle.iam.provisioning.api.ApplicationConfigService;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.ProvisioningServiceInternal;
import oracle.iam.provisioning.api.ApplicationInstanceInternalService;

import oracle.iam.reconciliation.api.ReconConfigService;

import oracle.iam.request.api.RequestService;
import oracle.iam.request.api.RequestDataSetService;
import oracle.iam.request.api.UnauthenticatedRequestService;

import oracle.iam.requestprofile.api.RequestProfileService;

import oracle.iam.selfservice.self.selfmgmt.api.AuthenticatedSelfService;
import oracle.iam.selfservice.uself.uselfmgmt.api.UnauthenticatedSelfService;

import oracle.iam.connectors.icfintg.api.ConnectorBundleService;

import oracle.iam.certification.api.CertificationService;
import oracle.iam.certification.api.CertificationAdministrationService;

import oracle.iam.sod.api.SODCheckService;

import oracle.iam.policyengine.api.PolicyManager;
import oracle.iam.policyengine.api.PolicyTypeManager;

import oracle.iam.policyengine.api.RuleManager;

import oracle.iam.ida.api.ScanRunManager;
import oracle.iam.ida.api.ScanDefinitionManager;
import oracle.iam.ida.api.PolicyViolationManager;
import oracle.iam.ida.api.IDAConfigurationManager;
import oracle.iam.ida.api.PolicyViolationCauseManager;

////////////////////////////////////////////////////////////////////////////////
// class Client
// ~~~~~ ~~~~~~
/**
 ** The entry point to Identity Manager Services is through {@link OIMClient}
 ** class.
 ** <br>
 ** <b>Note</b>:
 ** <code>Thor.API.tcUtilityFactory</code> used in earlier releases is no longer
 ** supported. Oracle recommends using the {@link OIMClient} for developing
 ** clients to integrate with Identity Manager.
 ** <p>
 ** The <code>Client</code> which is a subclass of {@link OIMClient} provides
 ** components a way to interact with elements in the Identity Manager runtime
 ** environment. The elements include
 ** <ul>
 **   <li>Business Services provided by Identity Manager features
 **   <li>Infrastructure Services provided by the Identity Manager Platform
 **   <li>Handle to external systems used by Identity Manager
 **       (Not to be confused with target systems used for provisioning) e.g
 **       DataSources, ToplinkSessions, MailServer Connection etc.
 ** </ul>
 ** This class was created to separate the client APIs for Identity Manager
 ** components and actual Identity Manager Client.
 ** <br>
 ** This client api requires access to the keystore and can provide login as
 ** admin.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Client extends OIMClient {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String NAME          = "Platform";
  public static final String SERVICE_USER  = "xelsysadm";
  public static final String INTERNAL_USER = "oiminternal";
  public static final String CALLBACK_USER = "weblogic";

  public static final String PROVIDER_URL  = "providerURL";
  public static final String LOGIN_CONFIG  = "java.security.auth.login.config";

  private static final String THIS          = Client.class.getName();
  private static final Logger LOGGER        = Logger.getLogger(THIS);

  //////////////////////////////////////////////////////////////////////////////
  // static attribute
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The path to thw JAAS login module configration.
   */
  private static Path        config;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private List<Role> role;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates an instance of <code>Client</code> to communicate with the
   ** Identity Manager application with a specified <code>environment</code>.
   **
   ** @param  environment        the environment used to create the initial
   **                            {@link OIMClient}; <code>null</code>
   **                            indicates an empty environment.
   **                            <br>
   **                            Allowed object is {@link Hashtable}.
   */
  private Client(final Hashtable environment) {
    // passing environment in constructor disables lookup for environment in
    // setup
    // in any case, we can always enforce manual environment settings by
    // OIMClient.setLookupEnv(configEnv) method.
    // ensure inheritance
    super(environment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Populates the environment if the application is deployed on a managed
   ** server reachable by the provided <code>url</code>.
   **
   ** @param  url                the string representation of an URL where
   **                            the managed server of Identity Manager is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the environment used to create the initial
   **                            {@link OIMClient}.
   **                            <br>
   **                            Possible object is {@link Hashtable}.
   */
  private static Hashtable environment(final String url) {
    final Hashtable<String, String> env = new Hashtable<String, String>();
    // hard-coded due to I'll never run this piece of code on WebSphere
    env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, "weblogic.jndi.WLInitialContextFactory");
    if (url != null) {
      env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, url);
    }
    env.put("weblogic.jndi.connectTimeout",      "3000");
    env.put("weblogic.jndi.responseReadTimeout", "1000");
    return env;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>Client</code> to communicate with
   ** Identity Manager application with an default environment.
   **
   ** @return                    the <code>Client</code> populated with the
   **                            environment configured.
   **                            <br>
   **                            Possible object is <code>Client</code>.
   */
  public static Client build() {
    return build(environment(System.getProperty(PROVIDER_URL)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>Client</code> to communicate with
   ** the Identity Manager application with <code>environment</code> specified.
   **
   ** @param  environment        the environment used to create the initial
   **                            {@link OIMClient}; <code>null</code>
   **                            indicates an empty environment.
   **                            <br>
   **                            Allowed object is {@link Hashtable}.
   **
   ** @return                    the <code>Client</code> populated with the
   **                            environment configured.
   **                            <br>
   **                            Possible object is <code>Client</code>.
   */
  public static Client build(final Hashtable environment) {
    // passing environment in constructor disables lookup for environment in
    // setup
    // in any case, we can always enforce manual environment settings by
    // OIMClient.setLookupEnv(configEnv) method.
    return new Client(environment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginAdministrator
  /**
   ** Creates a initial connection as Identity Manager administrator.
   ** <br>
   ** The user <code>xelsysadm</code> is used for that purpose.
   **
   ** @param  password           the credential of the user to authenticate.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @throws LoginException     if an error occurs attempting to establish a
   **                            connection.
   */
  public void loginAdministrator(final char[] password)
    throws LoginException {

    final String method = "loginAdministrator";
    LOGGER.entering(THIS, method);
    login(SERVICE_USER, password);
    LOGGER.exiting(THIS, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login (overridden)
  /**
   ** Establish a connection to Identity Manager server.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @param  username           the login name of the user to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the credential of the user to authenticate.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    the roles returned here are not users
   **                            enterprise roles. Don't use these roles for
   **                            API authorization.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Role}.
   **
   ** @throws LoginException     if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  public List<Role> login(final String username, final char[] password)
    throws LoginException {

    // create login handler configuration
    createConfig();
    // ensure inheritance
    this.role = super.login(username, password);
    return this.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logout
  /**
   ** Close a connection to Identity Manager server.
   */
  @Override
  public void logout() {
    // ensure inheritance
    super.logout();
    // remove login handler configuration
    removeConfig();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unauthenticatedSelfService
  /**
   ** Returns an instance of a Business Service of Identity Manager for a user
   ** who is not logged-in to the system to submit a request to register himself
   ** in the system and reset his forgotten/expired password.
   ** <br>
   ** Below is the code snippet that shows how to get an instance of this
   ** service:
   ** <pre>
   **   // returns an UnauthenticatedSelfService service instance
   **   final Platform platform = new Platform(Platform.environment("t3://oimhost:oimport"));
   **   final UnauthenticatedSelfService service = platform.service(UnauthenticatedSelfService.class);
   **   // return the list of challenge questions for the username provided as
   **   // an argument of the method.
   **   final String[] challengeQuestions = service.getChallengeQuestions(userName);
   **   ...
   ** </pre>
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link UnauthenticatedSelfService}.
   */
  public final UnauthenticatedSelfService unauthenticatedSelfService() {
    return service(UnauthenticatedSelfService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unauthenticatedRequestService
  /**
   ** Returns an instance of a Business Service of Identity Manager used for
   ** submission and tracking of requests that doesn't require authentication.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link UnauthenticatedRequestService}.
   */
  public final UnauthenticatedRequestService unauthenticatedRequestService() {
    return service(UnauthenticatedRequestService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticatedSelfService
  /**
   ** Returns an instance of a Business Service of Identity Manager used by a
   ** logged-in user to manage his profile.
   ** <br>
   ** It provides functionality to manage profile attributes, change password
   ** and manage challenge questions and answers. Apart from basic profile
   ** operations, there are proxy related operations to get proxy details and
   ** submit requests to assign, remove and update the proxy.
   ** <p>
   ** Below is the code snippet that shows how to get an instance of this
   ** service:
   ** <pre>
   **   // returns an AuthenticatedSelfService service instance
   **   final Platform platform = new Platform(Platform.environment("t3://oimhost:oimport"));
   **   platform.loginAs("xelsysadm");
   **   final AuthenticatedSelfService service = platform.service(AuthenticatedSelfService.class);
   **   // return the list of system challenge questions
   **   final String[] challengeQuestions = service.getSystemChallengeQuestions(userName);
   ** </pre>
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthenticatedSelfService}.
   */
  public final AuthenticatedSelfService authenticatedSelfService() {
    return service(AuthenticatedSelfService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizationService
  /**
   ** Returns an instance of a Business Service of Identity Manager for the
   ** authorization checks supported by the Identity Manager Authorization
   ** layer.
   ** <p>
   ** Clients need to call this service for authorization checks at their
   ** Policy Enforcement Points.
   ** <br>
   ** Examples of PEPs may be specific navigation items or action widgets on the
   ** user interface, or proactive checkpoints before processing a request in a
   ** feature, or an access control mechanism embedded in a custom Identity
   ** Manager client.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthorizationService}.
   */
  public final AuthorizationService authorizationService() {
    return service(AuthorizationService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizationOPSSService
  /**
   ** Returns an instance of a Business Service of Identity Manager for a
   ** contract that a class providing access control Implementation must implement.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link authorizationOPSSService}.
   */
  public final AuthorizationOPSSService authorizationOPSSService() {
    return service(AuthorizationOPSSService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platformService
  /**
   ** Returns an instance of a Business Service of Identity Manager to maintain
   ** common configurations like plug-In's, resource bundles etc.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link PlatformService}.
   */
  public final PlatformService platformService() {
    return service(PlatformService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to maintain
   ** entity (User, Role, Catalog etc.) configurations.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link ConfigManager}.
   */
  public final ConfigManager configManager() {
    return service(ConfigManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemConfigurationService
  /**
   ** Returns an instance of a Business Service of Identity Manager to maintain
   ** system configurations (Admin Console).
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link SystemConfigurationService}.
   */
  public final SystemConfigurationService systemConfigurationService() {
    return service(SystemConfigurationService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oimService
  /**
   ** Returns an instance of a Business Service of Identity Manager to  perform
   ** all requestable operations in Identity Manager.
   ** <br>
   ** Following operations are supported by this service:
   ** <ul>
   **   <li>Create User
   **   <li>Modify User
   **   <li>Enable User
   **   <li>Disable User
   **   <li>Delete User
   **   <li>Assign Roles
   **   <li>Remove from Roles
   **   <li>Modify Role Grant
   **   <li>Create Role
   **   <li>Modify Role
   **   <li>Delete Role
   **   <li>Provision Application Instance
   **   <li>Modify Account
   **   <li>Disable Account
   **   <li>Enable Account
   **   <li>Revoke Account
   **   <li>Provision Entitlement
   **   <li>Revoke Entitlement
   **   <li>Modify Entitlement
   ** </ul>
   ** Invoking <code>doOperation</code> method will result in to either request
   ** creation or direct operation based on the intent passed.
   ** <ul>
   **   <li>If intent is "ANY", operation will result in to either request or
   **       direct based on authorization of logged-in user.
   **   <li>If intent is "REQUEST", a request will be created for the operation.
   **   <li>If Intent is "DIRECT", operaion will be performed immediately,
   **       subject to authorization of logged-in user.
   ** </ul>
   ** <b>Note</b>:
   ** <br>
   ** <ul>
   **   <li>All bulk operations will result in to request creation.
   **   <li>"DIRECT" intent is not supported for bulk operations.
   **   <li>All operations with future effective date will result in to request
   **       creation.
   ** </ul>
   ** Sample for invoking "Assign Roles" operation:
   ** <pre>
   **   // get instance of OIMService
   **   final OIMClient  platform = login("UserLogin", "Passwd");
   **   final OIMService service  = platform.service(OIMService.class);
   **
   **   // construct RequestData object to assign role "expense approver" with
   **   // key "10" to user with key "12"
   **   final String      userKey     = "12"; // user with key 12
   **   final RequestData requestData = new RequestData();
   **   final Beneficiary beneficiary = new Beneficiary();
   **   beneficiary.setBeneficiaryKey(userKey);
   **   beneficiary.setBeneficiaryType(Beneficiary.USER_BENEFICIARY);
   **
   **   final RequestBeneficiaryEntity requestEntity = new RequestBeneficiaryEntity();
   **   requestEntity.setRequestEntityType(OIMType.Role);
   **   requestEntity.setEntitySubType("expense approver");
   **   requestEntity.setEntityKey("10"); // Role with key 10
   **   requestEntity.setOperation(RequestConstants.MODEL_ASSIGN_ROLES_OPERATION);
   **   final List&lt;RequestBeneficiaryEntity&gt; entities = new ArrayList&lt;RequestBeneficiaryEntity&gt;();
   **   entities.add(requestEntity);
   **
   **   beneficiary.setTargetEntities(entities);
   **
   **   final List&lt;Beneficiaryy&gt; beneficiaries = new ArrayList&lt;Beneficiaryy&gt;();
   **   beneficiaries.add(beneficiary);
   **   requestData.setBeneficiaries(beneficiaries);
   **
   **   OperationResult result = oimService.doOperation(requestData, OIMService.Intent.ANY);
   **   if( result.getRequestID() != null ) {
   **     // operation resulted in to request creation
   **     System.out.println("Request submitted with ID: " + result.getRequestID());
   **   }
   **   else {
   **     System.out.println("Role is assigned to user successfully");
   **   }
   ** </pre>
   ** Sample for disabling a user:
   ** <pre>
   **   // disable user with key "12".
   **   final RequestData  requestData = new RequestData();
   **   finalRequestEntity ent         = new RequestEntity();
   **   ent.setRequestEntityType(OIMType.User);
   **   String userKey = "12"; //User with key 12.
   **   ent.setEntityKey(userKey);
   **   ent.setOperation(RequestConstants.MODEL_DISABLE_OPERATION);
   **
   **   Listt&lt;RequestEntity&gt; entities = new ArrayListt&lt;RequestEntity&gt;();
   **   entities.add(ent);
   **   requestData.setTargetEntities(entities);
   **
   **   OperationResult result = oimService.doOperation(requestData, OIMService.Intent.ANY);
   ** </pre>
   ** Sample for provisioning an application instance to a user
   ** <pre>
   **   // construct RequestData object to provision  application instance
   **   // "Email Server1" with key "10" to a user with key "12"
   **   final String      userKey     = "12"; // user with key 12
   **   final RequestData requestData = new RequestData();
   **   final Beneficiary beneficiary = new Beneficiary();
   **   beneficiary.setBeneficiaryKey(userKey);
   **   beneficiary.setBeneficiaryType(Beneficiary.USER_BENEFICIARY);
   **
   **   final RequestBeneficiaryEntity requestEntity = new RequestBeneficiaryEntity();
   **   requestEntity.setRequestEntityType(OIMType.ApplicationInstance);
   **   requestEntity.setEntitySubType("Email Server1");
   **   requestEntity.setEntityKey("10"); // application instnace with key 10
   **   requestEntity.setOperation(RequestConstants.MODEL_PROVISION_APPLICATION_INSTANCE_OPERATION);
   **
   **   Listt&lt;RequestBeneficiaryEntityAttribute&gt; attrs = new ArrayListt&lt;RequestBeneficiaryEntityAttribute&gt;();
   **   RequestBeneficiaryEntityAttribute attr = new RequestBeneficiaryEntityAttribute("email id", "user@example.com", RequestBeneficiaryEntityAttribute.TYPE.String);
   **   attrs.add(attr);
   **
   **   requestEntity.setEntityData(attrs);
   **
   **   Listt&lt;RequestBeneficiaryEntity&gt; entities = new ArrayListt&lt;RequestBeneficiaryEntity&gt;();
   **   entities.add(requestEntity);
   **
   **   beneficiary.setTargetEntities(entities);
   **
   **   Listt&lt;Beneficiary&gt; beneficiaries = new ArrayListt&lt;Beneficiary&gt;();
   **   beneficiaries.add(beneficiary);
   **   requestData.setBeneficiaries(beneficiaries);
   **
   **   OperationResult result = oimService.doOperation(requestData, OIMService.Intent.ANY);
   ** </pre>
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link OIMService}.
   */
  public final OIMService oimService() {
    return service(OIMService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Organization</code>s.
   ** <br>
   ** To manage an <code>Organization</code>, it provides functionality to
   ** create, modify, enable, disable and delete the <code>Organization</code>.
   ** It also provides the support for bulk enable, disable and delete
   ** <code>Organization</code> operations.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link OrganizationManager}.
   */
  public final OrganizationManager organizationManager() {
    return service(OrganizationManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Role</code>s.
   ** <br>
   ** To manage an <code>Role</code>, it provides functionality to create,
   ** modify, enable, disable and delete the <code>Role</code>.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link RoleManager}.
   */
  public final RoleManager roleManager() {
    return service(RoleManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>User</code>s.
   ** <br>
   ** To manage an <code>User</code>, it provides functionality to create,
   ** modify, enable, disable and delete, lock, unlock , getting/setting the
   ** challenge questions &amp; answers , change/reset password of the
   ** <code>User</code>. It also provides the support for bulk modify, enable,
   ** disable, delete, lock and unlock <code>User</code> operations. Apart from
   ** basic <code>User</code> operations, there are proxy related operations to
   ** get <code>User</code>'s proxy details and assign, remove and update the
   ** proxy of a <code>User</code>.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link UserManager}.
   */
  public final UserManager userManager() {
    return service(UserManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordManagementService
  /**
   ** Returns an instance of a Business Service of Identity Manager for common
   ** password management functionalities like
   ** <ul>
   **   <li>Create/Update/Delete Password Policies.
   **   <li>Get description of the password policies.
   **   <li>Validate given passwords against the applicable Password Policy.
   **   <li>Get description of the password policy applicable to user and
   **       organization.
   ** </ul>
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link PasswordMgmtService}.
   */
  public final PasswordMgmtService passwordManagementService() {
    return service(PasswordMgmtService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationConfigService
  /**
   ** Returns an instance of a Business Service of Identity Manager to maintain
   ** application model configurations (Form Designer?).
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link ApplicationConfigService}.
   */
  public final ApplicationConfigService applicationConfigService() {
    return service(ApplicationConfigService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationService
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Application Instance</code>s.
   ** <br>
   ** To manage an <code>Application Instances</code>, it provides functionality
   ** to create, modify, enable, disable and delete of the
   ** <code>Application Instance</code>s.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link ApplicationService}.
   */
  public final ApplicationService applicationService() {
    return service(ApplicationService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Application</code>s (AOB?).
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link ApplicationManager}.
   */
  public final ApplicationManager applicationManager() {
    return service(ApplicationManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationInstanceService
  /**
   ** Returns an instance of a Business Service of Identity Manager to create
   ** and manage <code>Application Instance</code>s for both connected and
   ** disconnected <code>Application Instance</code>.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link ApplicationInstanceService}.
   */
  public final ApplicationInstanceService applicationInstanceService() {
    return service(ApplicationInstanceService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationInstanceInternalService
  /**
   ** Returns an instance of a Business Service of Identity Manager to create
   ** and manage <code>Application Instance</code>s for both connected and
   ** disconnected <code>Application Instance</code>.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link ApplicationInstanceInternalService}.
   */
  public final ApplicationInstanceInternalService applicationInstanceInternalService() {
    return service(ApplicationInstanceInternalService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementService
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Entitlement</code>s.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link EntitlementService}.
   */
  public final EntitlementService entitlementService() {
    return service(EntitlementService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityPublicationService
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** entity publications.
   ** <br>
   ** It provides methods to create, update, delete entity-publications in the
   ** persistent store.
   ** <p>
   ** Runtime authorization checks are based on these publications and
   ** authorization scopes of a logged-in user.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link EntityPublicationService}.
   */
  public final EntityPublicationService entityPublicationService() {
    return service(EntityPublicationService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adminRoleService
  /**
   ** Returns an instance of a Business Service of Identity Manager to query
   ** admin roles defined in an Identity Manager installation and manage scoped
   ** user memberships in these roles.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link AdminRoleService}.
   */
  public final AdminRoleService adminRoleService() {
    return service(AdminRoleService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogService
  /**
   ** Returns an instance of a Business Service of Identity Manager to perform
   ** all CRUD operation on <code>Catalog</code> entities.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link CatalogService}.
   */
  public final CatalogService catalogService() {
    return service(CatalogService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadataDefinitionManager
  /**
   ** Returns an instance of a Business Service of Identity Manager for
   ** handling all the information specific to catalog metadata definition.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link MetaDataDefinitionManager}.
   */
  public final MetaDataDefinitionManager metadataDefinitionManager() {
    return service(MetaDataDefinitionManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestProfileService
  /**
   ** Returns an instance of a Business Service of Identity Manager for
   ** managing <code>Request Profile</code>s and its related operations.
   ** <br>
   ** This service allows to create, modify, enable, disable and delete
   ** <code>Request Profile</code>s.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link RequestProfileService}.
   */
  public final RequestProfileService requestProfileService() {
    return service(RequestProfileService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestService
  /**
   ** Returns an instance of a Business Service of Identity Manager for
   ** managing <code>Request</code>s and its related operations.
   ** <br>
   ** This service allows
   ** <ul>
   **   <li>Submit,Validate,Search,Close,Withdraw requests
   **   <li>Search for request types and datasets
   **   <li>Get request History details
   **   <li>Get request count raised by/For an User
   ** </ul>
   ** Below is the code snippet that shows how to get an instance of this
   ** service:
   ** <pre>
   **   // returns a request service instance
   **   final RequestService service = Platform.getService(RequestService.class);
   ** </pre>
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link RequestService}.
   */
  public final RequestService requestService() {
    return service(RequestService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestDataSetService
  /**
   ** Returns an instance of a Business Service of Identity Manager for
   ** managing <code>Request Data Set</code>s.
   ** <br>
   ** This service allows:
   ** <ul>
   **   <li>Create new request data sets in the system.
   **   <li>Modify the existing data sets in the system.
   **   <li>Delete existing request data sets in the system.
   **   <li>Gets the existing data sets as a collection.
   ** </ul>
   ** Below is the code snippet that shows how to get an instance of this
   ** service:
   ** <pre>
   **   // returns a request data set service instance
   **   final RequestDataSetService service = Platform.getService(RequestDataSetService.class);
   **   // gets the list of request data sets that are available in the system.
   **   final List dataSets = service.getRequestDataSets();
   ** </pre>
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link EntitlementService}.
   */
  public final RequestDataSetService requestDataSetService() {
    return service(RequestDataSetService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessPolicyService
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Access Policy</code>s.
   ** <br>
   ** To manage an <code>Access Policy</code>, it provides functionality to
   ** create, modify, enable, disable and delete, of the
   ** <code>Access Policy</code>.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link AccessPolicyService}.
   */
  public final AccessPolicyService accessPolicyService() {
    return service(AccessPolicyService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisioningService
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Account</code>s.
   ** <br>
   ** It provides functionality to provision, enable, disable, revoke, modify
   ** <code>Account</code>s and grant, update and revoke entitlements.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link ProvisioningService}.
   */
  public final ProvisioningService provisioningService() {
    return service(ProvisioningService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisioningInternalService
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Account</code>s.
   ** <br>
   ** It provides functionality to provision, enable, disable, revoke, modify
   ** <code>Account</code>s and grant, update and revoke entitlements.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link ProvisioningServiceInternal}.
   */
  public final ProvisioningServiceInternal provisioningInternalService() {
    return service(ProvisioningServiceInternal.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconConfigService
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Reconcilaition Profile</code>s.
   ** <br>
   ** It provides functionality to import, export, delete and configure
   ** <code>Reconcilaition Profile</code>s.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link ReconConfigService}.
   */
  public final ReconConfigService reconConfigService() {
    return service(ReconConfigService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorServerService
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage a
   ** <code>ConnectorServer</code>.
   ** <br>
   ** It provides functionality to create, modify and delete of the
   ** <code>ConnectorServer</code>
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link ConnectorBundleService}.
   */
  public final ConnectorServerService connectorServerService() {
    return service(ConnectorServerService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorBundleService
  /**
   ** Returns an instance of a Business Service of Identity Manager to retrieve
   ** a <code>ConnectorBundle</code>.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link ConnectorBundleService}.
   */
  public final ConnectorBundleService connectorBundleService() {
    return service(ConnectorBundleService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorInfoService
  /**
   ** Returns an instance of a Business Service of Identity Manager to retrieve
   ** a <code>ConnectorInfo</code>s.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link ConnectorInfoService}.
   */
  public final ConnectorInfoService connectorInfoService() {
    return service(ConnectorInfoService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorInfoManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to maintain
   ** a list of <code>ConnectorInfo</code> instances. Each instance describes an
   ** identity connector.
   ** <p>
   ** {@link ConnectorInfoManager} can be obtained by calling the
   ** <code>getLocalManager</code> method on the
   ** <code>ConnectorInfoManagerFactory</code>, and a list of bundle URLs is
   ** passed to it. {@link ConnectorInfoManager} can also by obtained by calling
   ** <code>getRemoteManager</code> method on the
   ** <code>ConnectorInfoManagerFactory</code>.
   ** The <code>getRemoteManager</code> method accepts an instance of
   ** <code>RemoteFrameworkConnectionInfo</code> and, which is used for getting
   ** information about connectors deployed on Connector Server.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link ConnectorInfoManager}.
   */
  public final ConnectorInfoManager connectorInfoManager() {
    return service(ConnectorInfoManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sodCheckService
  /**
   ** Returns an instance of a Business Service of Identity Manager used for
   ** operations related to Segregation of Duties (SoD).
   ** <br>
   ** This service allows:
   ** <ul>
   **   <li>Checking if SOD Check is Enabled at System level.
   **   <li>Checking if SOD Check is Enabled for a Request
   **   <li>Initiate SOD Check given the request Id.
   ** </ul>
   ** Below is the code snippet that shows how to get an instance of this
   ** service:
   ** <pre>
   **   final Platform        platform = new Platform(Platform.environment("t3://oimhost:oimport"));
   **   // return an SODCheck service instance
   **   final SODCheckService service  = platform.service(SODCheckService.class);
   **   // initiates SOD Check
   **   service.initiateSODCheck(requestId);
   ** </pre>
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link SODCheckService}.
   */
  public final SODCheckService sodCheckService() {
    return service(SODCheckService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificationService
  /**
   ** Returns an instance of a Business Service of Identity Manager to the
   ** <code>Certification</code> feature.
   ** <br>
   ** The primary consumer of this service is the Identity Manager self-service UI.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link CertificationService}.
   */
  public final CertificationService certificationService() {
    return service(CertificationService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificationAdministrationService
  /**
   ** Returns an instance of a Business Service of Identity Manager to the
   ** <code>Certification</code> administration feature.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link CertificationAdministrationService}.
   */
  public final CertificationAdministrationService certificationAdministrationService() {
    return service(CertificationAdministrationService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ruleManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>Rule</code>s.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link RuleManager}.
   */
  public final RuleManager ruleManager() {
    return service(RuleManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policyManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** Policies.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link PolicyManager}.
   */
  public final PolicyManager policyManager() {
    return service(PolicyManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policyTypeManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** <code>PolicyType</code>s.
   ** <br>
   ** This class is responsible for:
   ** <ul>
   **   <li>Search operations on <code>PolicyType</code>s.
   **   <li>Path element search operations used by the UI.
   ** </ul>
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link PolicyManager}.
   */
  public final PolicyTypeManager policyTypeManager() {
    return service(PolicyTypeManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policyViolationManager
  /**
   ** Returns an instance of a Business Service of Identity Manager for
   ** <code>PolicyViolation</code> entity operations.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link PolicyViolationManager}.
   */
  public final PolicyViolationManager policyViolationManager() {
    return service(PolicyViolationManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policyViolationCauseManager
  /**
   ** Returns an instance of a Business Service of Identity Manager for
   ** <code>PolicyViolationCause</code> entity operations.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link PolicyViolationManager}.
   */
  public final PolicyViolationCauseManager policyViolationCauseManager() {
    return service(PolicyViolationCauseManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scanDefinitionManagerService
  /**
   ** Returns an instance of a Business Service of Identity Manager for
   ** <code>ScanDefinition</code> entity operations.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link ScanDefinitionManager}.
   */
  public final ScanDefinitionManager scanDefinitionManagerService() {
    return service(ScanDefinitionManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scanRunManager
  /**
   ** Returns an instance of a Business Service of Identity Manager for
   ** <code>ScanRun</code> entity operations.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is {@link ScanRunManager}.
   */
  public final ScanRunManager scanRunManager() {
    return service(ScanRunManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   idaConfigurationManager
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** the IDA configuration in MDS.
   **
   ** @return                    the Business Service instance.
   **                            <br>
   **                            Possible object is
   **                            {@link IDAConfigurationManager}.
   */
  public final IDAConfigurationManager idaConfigurationManager() {
    return service(IDAConfigurationManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupFacade
  /**
   ** Returns an instance of a Business Facade of Identity Manager to manage
   ** <code>Lookup Definition</code>s.
   **
   ** @return                    the Business Service instance for managing
   **                            <code>Lookup Definition</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcLookupOperationsIntf}.
   */
  public final tcLookupOperationsIntf lookupFacade() {
    return service(tcLookupOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointDefinitionFacade
  /**
   ** Returns an instance of a Business Facade of Identity Manager to manage
   ** <code>ITResource Type Definition</code>s.
   **
   ** @return                    the Business Service instance for managing
   **                            <code>ITResource Type Definition</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcITResourceDefinitionOperationsIntf}.
   */
  public final tcITResourceDefinitionOperationsIntf endpointDefinitionFacade() {
    return service(tcITResourceDefinitionOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointInstanceFacade
  /**
   ** Returns an instance of a Business Facade of Identity Manager to manage
   ** <code>ITResource Instance</code>s.
   **
   ** @return                    the Business Service instance for managing
   **                            <code>ITResource Instance</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcITResourceInstanceOperationsIntf}.
   */
  public final tcITResourceInstanceOperationsIntf endpointInstanceFacade() {
    return service(tcITResourceInstanceOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationFacade
  /**
   ** Returns an instance of a Business Facade for Identity Manager to manage
   ** <code>Organization</code>s.
   **
   ** @return                    the Business Service instance for managing
   **                            <code>Organization</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcOrganizationOperationsIntf}.
   */
  public final tcOrganizationOperationsIntf organizationFacade() {
    return service(tcOrganizationOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupFacade
  /**
   ** Returns an instance of a Business Facade for Identity Manager to manage
   ** <code>Group</code>s.
   **
   ** @return                    the Business Service instance for managing
   **                            <code>Group</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcGroupOperationsIntf}.
   */
  public final tcGroupOperationsIntf groupFacade() {
    return service(tcGroupOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userFacade
  /**
   ** Returns an instance of a Business Facade for Identity Manager to manage
   ** <code>User</code>s.
   **
   ** @return                    the Business Service instance for managing
   **                            <code>User</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcUserOperationsIntf}.
   */
  public final tcUserOperationsIntf userFacade() {
    return service(tcUserOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Resource Object</code>s.
   **
   ** @return                    a Business Service instance for managing
   **                            <code>Resource Object</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcObjectOperationsIntf}.
   */
  public final tcObjectOperationsIntf objectFacade() {
    return service(tcObjectOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessPolicyFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Access Policies</code>.
   **
   ** @return                    a Business Service instance for managing
   **                            <code>Access Policies</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcAccessPolicyOperationsIntf}.
   */
  public final tcAccessPolicyOperationsIntf accessPolicyFacade() {
    return service(tcAccessPolicyOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formDefinitionFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Form Definition</code>s.
   **
   ** @return                    a Business Service instance for managing
   **                            <code>Form Definition</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcFormDefinitionOperationsIntf}.
   */
  public final tcFormDefinitionOperationsIntf formDefinitionFacade() {
    return service(tcFormDefinitionOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formInstanceFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Form Instance</code>s.
   **
   ** @return                    a Business Service instance for managing
   **                            <code>Form Instance</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcFormInstanceOperationsIntf}.
   */
  public final tcFormInstanceOperationsIntf formInstanceFacade() {
    return service(tcFormInstanceOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisioningFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Provisioning</code>.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the application
   ** server Log in this case.
   **
   ** @return                    a Business Service instance for managing
   **                            <code>Provisioning</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link tcProvisioningOperationsIntf}.
   */
  public final tcProvisioningOperationsIntf provisioningFacade() {
    return service(tcProvisioningOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attestationFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Attestation</code>s.
   **
   ** @return                    a Business Service instance for managing
   **                            <code>Attestation</code>s.
   **                            <br>
   **                            Possible object is
   **                            {@link AttestationOperationsIntf}.
   */
  public final AttestationOperationsIntf attestationFacade() {
    return service(AttestationOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns a handle to a service available in an Identity Manager.
   ** <br>
   ** A service is either a Business Service (e.g UserManager) provided by a
   ** feature or an infrastructure service provided by the Identity Manager
   ** Platform. The argument to this method is the interface that establishes
   ** the service contract.
   ** <br>
   ** For examples, the UserService interface may establish the contract for the
   ** UserMgmt feature. Similarly the EntityManager interface establishes the
   ** contract for the Entity Manager.
   ** <br>
   ** Accessing a Service
   ** <pre>
   **  // create a new client
   **  final Platform platform = new Platform();
   **  // login as xelsysadm
   **  platform.loginAsAdmin();
   **
   **  // get a handle to the UserService (a Business Service)
   **  UserManager userService = platform.service(UserManager.class);
   **
   **  // get a handle to the OrchestrationEngine (Infrastructure Service)
   **  EntityManager entityMgr = platform.service(EntityManager.class);
   ** </pre>
   **
   ** @param  <T>                the expected class type of the service
   **                            contract.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the {@link Class} object corresponding to the
   **                            service interface.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @return                    a POJO handle to the service.
   **                            <br>
   **                            It needs not be cast to the requested Business
   **                            Facade.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws NoSuchServiceException if no such service handle is available in
   **                                Identity Manager.
   */
  public <T> T service(final Class<T> clazz)
    throws NoSuchServiceException {

    return super.getService(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConfig
  /**
   ** Creates the required JAAS Login Module configuration for Oracle WebLogic
   ** Server at a temporary stage.
   **
   ** @throws LoginException     if the temporary file with the configuration
   **                            could not be created or written.
   **                            <br>
   **                            The message text of the exception thrown
   **                            belongs to the I/O exception catched.
   */
  private static void createConfig()
    throws LoginException {

    // prepare filesystem
    if (System.getProperty(LOGIN_CONFIG) == null) {
      try {
        config = Files.createTempFile("authwl", ".conf");
        System.setProperty(LOGIN_CONFIG, config.toString());
        Files.write(config, "xellerate{weblogic.security.auth.login.UsernamePasswordLoginModule required debug=true;};".getBytes());
      }
      catch (IOException e) {
        throw new LoginException(e.getLocalizedMessage());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeConfig
  /**
   ** Removes the JAAS Login Module configuration for Oracle WebLogic
   ** Server from temporary stage.
   */
  private static void removeConfig() {
    if (config != null) {
      try {
        Files.delete(config);
      }
      catch (IOException e) {
        e.printStackTrace(System.err);
      }
    }
  }
}