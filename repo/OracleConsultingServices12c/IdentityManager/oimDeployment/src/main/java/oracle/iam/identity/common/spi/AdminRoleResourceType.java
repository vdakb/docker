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

    File        :   AdminRoleResourceType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    AdminRoleResourceType.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

////////////////////////////////////////////////////////////////////////////////
// enum AdminRoleResourceType
// ~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AdminRoleResourceType</code> represents resource type capabilities for
 ** admin role in Identity Manager that might be assigned or revoked operation.
 ** <p>
 ** There is no API to
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum AdminRoleResourceType {

    ACCESS_POLICY               ("OrclOIMAccessPolicy")
  , ADMIN_ROLE                  ("OrclOIMAdminRole")
  , APPLICATION                 ("OrclOIMApplicationInstance")
  , APPROVAL_POLICY             ("OrclOIMApprovalPolicy")
  , AUDIT_CONFIG                ("OrclOIMIdentityAuditConfiguration")
  , AUDIT_POLICY                ("OrclOIMIdentityAuditPolicy")
  , AUDIT_POLICY_VIOLATION      ("OrclOIMIdentityAuditPolicyViolation")
  , AUDIT_POLICY_VIOLATION_CAUSE("OrclOIMIdentityAuditPolicyViolationCause")
  , AUDIT_RULE                  ("OrclOIMIdentityAuditRule")
  , AUDIT_SCAN_DEF              ("OrclOIMIdentityAuditScanDefinition")
  , AUDIT_SCAN_RUN              ("OrclOIMIdentityAuditScanRun")
  , CATALOG_ITEM                ("OrclOIMCatalogItem")
  , CERTIFICATION               ("OrclOIMCertification")
  , CONFIG                      ("OrclOIMUserManagementConfiguration")
  , DASHBOARD                   ("OrclOIMDiagnosticDashboard")
  , ENTITLEMENT                 ("OrclOIMITResEntitlement")
  , NOTIFICATION                ("OrclOIMNotification")
  , ORGANIZATION                ("OrclOIMOrganization")
  , PASSWORDPOLICY              ("OrclOIMPasswordPolicy")
  , PLATFORM_SERVICE            ("OrclOIMPlatformService")
  , PLUGIN                      ("OrclOIMPlugIn")
  , RECONCILIATION              ("OrclOIMReconciliation")
  , REQUEST_DATASET             ("OrclOIMRequestDataset")
  , REQUEST_PROFILE             ("OrclOIMRequestProfile")
  , RESOURCE                    ("OrclOIMResourceObject")
  , ROLE                        ("OrclOIMRole")
  , ROLE_CATEGORY               ("OrclOIMRoleCategory")
  , SCHEDULER                   ("OrclOIMScheduler")
  , SYSTEM_PROPERTY             ("OrclOIMSystemProperties")
  , UI                          ("OrclOIMOIMUI")
  , USER                        ("OrclOIMUser")
  ;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String id;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AdminRoleResourceType</code> that allows use as a
   ** JavaBean.
   **
   ** @param  id                 the identifier of the resource type.
   */
  AdminRoleResourceType(final String id) {
    this.id = id;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  ////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the value of the id property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String id() {
    return this.id;
  }
}