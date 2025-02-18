/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtaul Resource Management

    File        :   ResolverMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ResolverMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

////////////////////////////////////////////////////////////////////////////////
// interface ResolverMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ResolverMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the mining message prefix. */
  static final String PROVISION                                     = "Provision";
  static final String REVOKE                                        = "Revoke";
  static final String ENABLE                                        = "Enable";
  static final String DISABLE                                       = "Disable";

  static final String PREFIX                                        = "POL-";

  static final String PRECONDITION_STARTED                          = PREFIX + "01101";
  static final String PRECONDITION_COMPLETED                        = PREFIX + "01102";
  static final String PRECONDITION_STOPPED                          = PREFIX + "01103";
  static final String PRECONDITION_CONFIGURATION_BEGIN              = PREFIX + "01104";
  static final String PRECONDITION_CONFIGURATION_COMPLETE           = PREFIX + "01105";
  static final String PRECONDITION_CONFIGURATION_FAILED             = PREFIX + "01106";
  static final String PRECONDITION_CONFIGURATION_MAPPING_EMPTY      = PREFIX + "01107";
  static final String PRECONDITION_RESOURCE_MAPPING_BEGIN           = PREFIX + "01109";
  static final String PRECONDITION_RESOURCE_MAPPING_COMPLETE        = PREFIX + "01110";
  static final String PRECONDITION_RESOURCE_MAPPING_FAILED          = PREFIX + "01111";
  static final String PRECONDITION_RESOURCE_MAPPING_EMPTY           = PREFIX + "01112";
  static final String PRECONDITION_ROLE_MAPPING_BEGIN               = PREFIX + "01113";
  static final String PRECONDITION_ROLE_MAPPING_COMPLETE            = PREFIX + "01114";
  static final String PRECONDITION_ROLE_MAPPING_FAILED              = PREFIX + "01115";
  static final String PRECONDITION_ROLE_MAPPING_EMPTY               = PREFIX + "01116";
  static final String PRECONDITION_POLICY_MAPPING_BEGIN             = PREFIX + "01117";
  static final String PRECONDITION_POLICY_MAPPING_COMPLETE          = PREFIX + "01118";
  static final String PRECONDITION_POLICY_MAPPING_FAILED            = PREFIX + "01119";
  static final String PRECONDITION_POLICY_MAPPING_EMPTY             = PREFIX + "01120";
  static final String PRECONDITION_POLICY_FORMSIZE_MISMATCH         = PREFIX + "01121";
  static final String PRECONDITION_POLICY_FORMKEY_MISMATCH          = PREFIX + "01122";

  static final String MINIMG_ACCOUNT_ENTITLEMENT_BEGIN              = PREFIX + "01131";
  static final String MINIMG_ACCOUNT_ENTITLEMENT_COMPLETE           = PREFIX + "01132";
  static final String MINIMG_ACCOUNT_ENTITLEMENT_FAILED             = PREFIX + "01132";
  static final String MINIMG_ACCOUNT_ENTITLEMENT_EMPTY              = PREFIX + "01134";

  static final String ACCOUNT_RESOURCE_EMPTY                        = PREFIX + "01141";
  static final String ACCOUNT_RESOURCE_NORESOURCES                  = PREFIX + "01142";
  static final String ACCOUNT_RESOURCE_CANDIATE                     = PREFIX + "01144";

  static final String OBJECT_PROVISIONING_NOACTION                  = PREFIX + "01153";
  static final String OBJECT_PROVISIONED_EXISTS                     = PREFIX + "01154";
  static final String OBJECT_PROVISIONING_STARTED                   = PREFIX + "01155";
  static final String OBJECT_PROVISIONING_ORDERED                   = PREFIX + "01156";
  static final String OBJECT_PROVISIONING_PROVISIONED               = PREFIX + "01157";
  static final String OBJECT_PROVISIONING_REVOKED                   = PREFIX + "01158";
}