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

    File        :   TestAPI.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestAPI.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.service.api;

import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

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

import javax.security.auth.login.LoginException;

import oracle.hst.platform.core.SystemException;

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

import oracle.iam.identity.service.Network;

import org.junit.BeforeClass;

////////////////////////////////////////////////////////////////////////////////
// class TestAPI
// ~~~~~ ~~~~~~~
/**
 ** The test case belonging to login/logout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAPI extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestAPI</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestAPI() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login
  /**
   ** Tests need to share computationally expensive setup.
   ** <br>
   ** While this can compromise the independence of tests, it is a necessary
   ** optimization.
   ** <p>
   ** Annotating this method with <code>&#064;BeforeClass</code> causes it to be
   ** run once before any of the test methods in the class.
   ** <p>
   ** The <code>&#064;BeforeClass</code> methods of superclasses will be run
   ** before those of the current class, unless they are shadowed in the current
   ** class.
   **
   ** @throws SystemException    if the authentication/authorization process
   **                            fails.
   */
  @BeforeClass
  public static void login()
    throws SystemException {

    try {
      context.login("xelsysadm", "Sophie20061990$".toCharArray());
    }
    catch (LoginException e) {
      Network.CONSOLE.fatal(e);
      throw SystemException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unauthenticatedSelfService
  /**
   ** Test to instantiate a Business Service of Identity Manager for a user who
   ** is not logged-in to the system to submit a request to register himself in
   ** the system and reset his forgotten/expired password.
   */
  @Test
  public void unauthenticatedSelfService() {
    final UnauthenticatedSelfService service = context.unauthenticatedSelfService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unauthenticatedRequestService
  /**
   ** Test to instantiate a Business Service of Identity Manager used for
   ** submission and tracking of requests that doesn't require authentication.
   */
  @Test
  public void unauthenticatedRequestService() {
    final UnauthenticatedRequestService service = context.unauthenticatedRequestService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticatedSelfService
  /**
   ** Test to instantiate a Business Service of Identity Manager used by a
   ** logged-in user to manage his profile.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Ensure the the class UserInfoInterface is available on the classpath at
   ** runtime. This class is included in ipf.jar file which is available under
   ** oracle_common/modules/oracle.ipf directory.
   */
  @Test
  public void authenticatedSelfService() {
    final AuthenticatedSelfService service = context.authenticatedSelfService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizationService
  /**
   ** Test to instantiate a Business Service of Identity Manager for the
   ** authorization checks supported by the Identity Manager Authorization
   ** layer.
   */
  @Test
  public void authorizationService() {
    final AuthorizationService service = context.authorizationService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizationOPSSService
  /**
   ** Test to instantiate a Business Service of Identity Manager for a contract
   ** that a class providing access control Implementation must implement.
   */
  @Test
  public void authorizationOPSSService() {
    final AuthorizationOPSSService service = context.authorizationOPSSService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platformService
  /**
   ** Test to instantiate a Business Service of Identity Manager to maintain
   ** common configurations like plug-In's, resource bundles etc.
   */
  @Test
  public void platformService() {
    final PlatformService service = context.platformService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to maintain
   ** entity (User, Role, Catalog etc.) configurations.
   */
  @Test
  public void configManager() {
    final ConfigManager service = context.configManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemConfigurationService
  /**
   ** Test to instantiate a Business Service of Identity Manager to maintain
   ** system configurations (Admin Console).
   */
  @Test
  public void systemConfigurationService() {
    final SystemConfigurationService service = context.systemConfigurationService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oimService
  /**
   ** Test to instantiate a Business Service of Identity Manager to  perform
   ** all requestable operations in Identity Manager.
   */
  @Test
  public void oimService() {
    final OIMService service = context.oimService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Organization</code>s.
   */
  @Test
  public void organizationManager() {
    final OrganizationManager service = context.organizationManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Role</code>s.
   */
  @Test
  public void roleManager() {
    final RoleManager service = context.roleManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>User</code>s.
   */
  @Test
  public void userManager() {
    final UserManager service = context.userManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordManagementService
  /**
   ** Test to instantiate a Business Service of Identity Manager for common
   ** password management functionalities like
   */
  @Test
  public void passwordManagementService() {
    final PasswordMgmtService service = context.passwordManagementService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationConfigService
  /**
   ** Test to instantiate a Business Service of Identity Manager to maintain
   ** application model configurations (Form Designer?).
   */
  @Test
  public void applicationConfigService() {
    final ApplicationConfigService service = context.applicationConfigService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationService
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Application Instance</code>s.
   */
  @Test
  public void applicationService() {
    final ApplicationService service = context.applicationService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Application</code>s (AOB?).
   */
  @Test
  public void applicationManager() {
    final ApplicationManager service = context.applicationManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationInstanceService
  /**
   ** Test to instantiate a Business Service of Identity Manager to create
   ** and manage <code>Application Instance</code>s for both connected and
   ** disconnected <code>Application Instance</code>.
   */
  @Test
  public void applicationInstanceService() {
    final ApplicationInstanceService service = context.applicationInstanceService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationInstanceInternalService
  /**
   ** Test to instantiate a Business Service of Identity Manager to create
   ** and manage <code>Application Instance</code>s for both connected and
   ** disconnected <code>Application Instance</code>.
   */
  @Test
  public void applicationInstanceInternalService() {
    final ApplicationInstanceInternalService service = context.applicationInstanceInternalService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementService
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Entitlement</code>s.
   */
  @Test
  public void entitlementService() {
    final EntitlementService service = context.entitlementService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityPublicationService
  /**
   ** Returns an instance of a Business Service of Identity Manager to manage
   ** entity publications.
   */
  @Test
  public void entityPublicationService() {
    final EntityPublicationService service = context.entityPublicationService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adminRoleService
  /**
   ** Test to instantiate a Business Service of Identity Manager to query admin
   ** roles defined in an Identity Manager installation and manage scoped user
   ** memberships in these roles.
   */
  @Test
  public void adminRoleService() {
    final AdminRoleService service = context.adminRoleService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogService
  /**
   ** Test to instantiate a Business Service of Identity Manager to perform all
   ** CRUD operation on <code>Catalog</code> entities.
   */
  @Test
  public void catalogService() {
    final CatalogService service = context.catalogService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadataDefinitionManager
  /**
   ** Test to instantiate a Business Service of Identity Manager for handling
   ** all the information specific to catalog metadata definition.
   */
  @Test
  public void metadataDefinitionManager() {
    final MetaDataDefinitionManager service = context.metadataDefinitionManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestProfileService
  /**
   ** Test to instantiate a Business Service of Identity Manager for managing
   ** <code>Request Profile</code>s and its related operations.
   */
  @Test
  public void requestProfileService() {
    final RequestProfileService service = context.requestProfileService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestService
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Request</code>s and its related operations.
   */
  @Test
  public void requestService() {
    final RequestService service = context.requestService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestDataSetService
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Request Data Set</code>s.
   */
  @Test
  public void requestDataSetService() {
    final RequestDataSetService service = context.requestDataSetService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessPolicyService
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Access Policy</code>s.
   */
  @Test
  public void accessPolicyService() {
    final AccessPolicyService service = context.accessPolicyService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisioningService
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Account</code>s.
   */
  @Test
  public void provisioningService() {
    final ProvisioningService service = context.provisioningService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisioningInternalService
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Account</code>s.
   */
  @Test
  public void provisioningInternalService() {
    final ProvisioningServiceInternal service = context.provisioningInternalService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconConfigService
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Reconcilaition Profile</code>s.
   */
  @Test
  public void reconConfigService() {
    final ReconConfigService service = context.reconConfigService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorServerService
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage a
   ** <code>ConnectorServer</code>.
   */
  @Test
  public void connectorServerService() {
    final ConnectorServerService service = context.connectorServerService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorBundleService
  /**
   ** Test to instantiate a Business Service of Identity Manager to retrieve
   ** a <code>ConnectorBundle</code>.
   */
  @Test
  public void connectorBundleService() {
    final ConnectorBundleService service = context.connectorBundleService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorInfoService
  /**
   ** Test to instantiate a Business Service of Identity Manager to retrieve a
   ** <code>ConnectorInfo</code>s.
   */
  @Test
  public void connectorInfoService() {
    final ConnectorInfoService service = context.connectorInfoService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorInfoManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to maintain a
   ** list of <code>ConnectorInfo</code> instances. Each instance describes an
   ** identity connector.
   */
  @Test
  public void connectorInfoManager() {
    final ConnectorInfoManager service = context.connectorInfoManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sodCheckService
  /**
   ** Test to instantiate a Business Service of Identity Manager used for
   ** operations related to Segregation of Duties (SoD).
   */
  @Test
  public void sodCheckService() {
    final SODCheckService service = context.sodCheckService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificationService
  /**
   ** Test to instantiate a Business Service of Identity Manager to the
   ** <code>Certification</code> feature.
   */
  @Test
  public void certificationService() {
    final CertificationService service = context.certificationService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificationAdministrationService
  /**
   ** Test to instantiate a Business Service of Identity Manager to the
   ** <code>Certification</code> administration feature.
   */
  @Test
  public void certificationAdministrationService() {
    final CertificationAdministrationService service = context.certificationAdministrationService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ruleManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Rule</code>s.
   */
  @Test
  public void ruleManager() {
    final RuleManager service = context.ruleManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policyManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>Policies</code>s.
   */
  @Test
  public void policyManager() {
    final PolicyManager service = context.policyManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policyTypeManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage
   ** <code>PolicyType</code>s.
   */
  @Test
  public void policyTypeManager() {
    final PolicyTypeManager service = context.policyTypeManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policyViolationManager
  /**
   ** Test to instantiate a Business Service of Identity Manager for
   ** <code>PolicyViolation</code> entity operations.
   */
  @Test
  public void policyViolationManager() {
    final PolicyViolationManager service = context.policyViolationManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policyViolationCauseManager
  /**
   ** Test to instantiate a Business Service of Identity Manager for
   ** <code>PolicyViolationCause</code> entity operations.
   */
  @Test
  public void policyViolationCauseManager() {
    final PolicyViolationCauseManager service = context.policyViolationCauseManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scanDefinitionManagerService
  /**
   ** Test to instantiate a Business Service of Identity Manager for
   ** <code>ScanDefinition</code> entity operations.
   */
  @Test
  public void scanDefinitionManagerService() {
    final ScanDefinitionManager service = context.scanDefinitionManagerService();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scanRunManager
  /**
   ** Test to instantiate a Business Service of Identity Manager for
   ** <code>ScanRun</code> entity operations.
   */
  @Test
  public void scanRunManager() {
    final ScanRunManager service = context.scanRunManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   idaConfigurationManager
  /**
   ** Test to instantiate a Business Service of Identity Manager to manage the
   ** IDA configuration in MDS.
   */
  @Test
  public void idaConfigurationManager() {
    final IDAConfigurationManager service = context.idaConfigurationManager();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupFacade
  /**
   ** Returns an instance of a Business Facade of Identity Manager to manage
   ** <code>Lookup Definition</code>s.
   */
  @Test
  public void lookupFacade() {
    final tcLookupOperationsIntf service = context.lookupFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointDefinitionFacade
  /**
   ** Returns an instance of a Business Facade of Identity Manager to manage
   ** <code>ITResource Type Definition</code>s.
   */
  @Test
  public void endpointDefinitionFacade() {
    final tcITResourceDefinitionOperationsIntf service = context.endpointDefinitionFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointInstanceFacade
  /**
   ** Returns an instance of a Business Facade of Identity Manager to manage
   ** <code>ITResource Instance</code>s.
   */
  @Test
  public void endpointInstanceFacade() {
    final tcITResourceInstanceOperationsIntf service = context.endpointInstanceFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationFacade
  /**
   ** Returns an instance of a Business Facade for Identity Manager to manage
   ** <code>Organization</code>s.
   */
  @Test
  public void organizationFacade() {
    final tcOrganizationOperationsIntf service = context.organizationFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupFacade
  /**
   ** Returns an instance of a Business Facade for Identity Manager to manage
   ** <code>Group</code>s.
   */
  @Test
  public void groupFacade() {
    final tcGroupOperationsIntf service = context.groupFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userFacade
  /**
   ** Returns an instance of a Business Facade for Identity Manager to manage
   ** <code>User</code>s.
   */
  @Test
  public void userFacade() {
    final tcUserOperationsIntf service = context.userFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Resource Object</code>s.
   */
  @Test
  public void objectFacade() {
    final tcObjectOperationsIntf service = context.objectFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessPolicyFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Access Policies</code>.
   */
  @Test
  public void accessPolicyFacade() {
    final tcAccessPolicyOperationsIntf service = context.accessPolicyFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formDefinitionFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Form Definition</code>s.
   */
  @Test
  public void formDefinitionFacade() {
    final tcFormDefinitionOperationsIntf service = context.formDefinitionFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formInstanceFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Form Instance</code>s.
   */
  @Test
  public void formInstanceFacade() {
    final tcFormInstanceOperationsIntf service = context.formInstanceFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisioningFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Provisioning</code>.
   */
  @Test
  public void provisioningFacade() {
    final tcProvisioningOperationsIntf service = context.provisioningFacade();
    Assert.assertNotNull(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attestationFacade
  /**
   ** Returns an instance of a Business Service for Identity Manager to manage
   ** <code>Attestation</code>s.
   */
  @Test
  public void attestationFacade() {
    final AttestationOperationsIntf service = context.attestationFacade();
    Assert.assertNotNull(service);
  }
}