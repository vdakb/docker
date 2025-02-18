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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   TaskMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TaskMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

////////////////////////////////////////////////////////////////////////////////
// interface TaskMessage
// ~~~~~~~~~ ~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public interface TaskMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                            = "OIM-";

  // 01001 - 01010 reconciliation process related messages
  static final String RECONCILIATION_BEGIN              = PREFIX + "01001";
  static final String RECONCILIATION_COMPLETE           = PREFIX + "01002";
  static final String RECONCILIATION_STOPPED            = PREFIX + "01003";
  static final String RECONCILIATION_SUCCESS            = PREFIX + "01004";
  static final String RECONCILIATION_ERROR              = PREFIX + "01005";
  static final String COLLECTING_BEGIN                  = PREFIX + "01006";
  static final String COLLECTING_COMPLETE               = PREFIX + "01007";
  static final String RECONCILE_BEGIN                   = PREFIX + "01008";
  static final String RECONCILE_COMPLETE                = PREFIX + "01009";
  static final String RECONCILE_SKIP                    = PREFIX + "01010";

  // 01011 - 01020 provisioning process related messages
  static final String PROVISIONING_BEGIN                = PREFIX + "01011";
  static final String PROVISIONING_COMPLETE             = PREFIX + "01012";
  static final String PROVISIONING_STOPPED              = PREFIX + "01013";
  static final String PROVISIONING_SUCCESS              = PREFIX + "01014";
  static final String PROVISIONING_ERROR                = PREFIX + "01015";
  static final String PROVISIONING_SKIP                 = PREFIX + "01016";

  // 01021 - 01030 configuration related related messages
  static final String TASK_PARAMETER                    = PREFIX + "01021";
  static final String ITRESOURCE_PARAMETER              = PREFIX + "01022";
  static final String TASK_DESCRIPTOR                   = PREFIX + "01023";
  static final String PROFILE_MAPPING                   = PREFIX + "01024";
  static final String ATTRIBUT_MAPPING                  = PREFIX + "01025";
  static final String ATTRIBUT_TRANSFORMATION           = PREFIX + "01026";
  static final String MULTIVALUED_TRANSFORMATION        = PREFIX + "01027";
  static final String LOOKUP_TRANSFORMATION             = PREFIX + "01028";
  static final String ENTITLEMENT_MAPPING               = PREFIX + "01029";
  static final String ATTRIBUT_CONTROL                  = PREFIX + "01030";

  // 01031 - 01040 reconciliation process related messages
  static final String NOTCHANGED                        = PREFIX + "01031";
  static final String NOTAVAILABLE                      = PREFIX + "01032";
  static final String WILLING_TO_CHANGE                 = PREFIX + "01033";
  static final String ABLE_TO_CHANGE                    = PREFIX + "01034";
  static final String NOTHING_TO_CHANGE                 = PREFIX + "01035";
  static final String ADDING_CHILDDATA                  = PREFIX + "01036";
  static final String WILLING_TO_DELETE                 = PREFIX + "01037";
  static final String ABLE_TO_DELETE                    = PREFIX + "01038";
  static final String NOTHING_TO_DELETE                 = PREFIX + "01039";

  // 01041 - 01050 reconciliation event related messages
  static final String EVENT_CREATED                     = PREFIX + "01041";
  static final String EVENT_IGNORED                     = PREFIX + "01042";
  static final String EVENT_CHILD_CREATED               = PREFIX + "01043";
  static final String EVENT_CHILD_IGNORED               = PREFIX + "01044";
  static final String EVENT_FAILED                      = PREFIX + "01045";
  static final String EVENT_FINISHED                    = PREFIX + "01046";
  static final String EVENT_PROCEED                     = PREFIX + "01047";

  // 01051 - 01060 scheduler related messages
  static final String SCHEDULE_JOB_STARTING             = PREFIX + "01051";
  static final String SCHEDULE_JOB_STARTED              = PREFIX + "01052";

  // 01061 - 01080 dependency collector related messages
  static final String DEPENDENCY_RESOLVING              = PREFIX + "01061";
  static final String DEPENDENCY_CREATE_ROOT            = PREFIX + "01062";
  static final String DEPENDENCY_CREATE_UNRESULVED      = PREFIX + "01063";
  static final String DEPENDENCY_NODE_RESOLVED          = PREFIX + "01064";
  static final String DEPENDENCY_NODE_INSERT_RESOLVED   = PREFIX + "01065";
  static final String DEPENDENCY_NODE_ADDED_RESOLVED    = PREFIX + "01066";
  static final String DEPENDENCY_NODE_MOVED_RESOLVED    = PREFIX + "01067";
  static final String DEPENDENCY_NODE_INSERT_UNRESOLVED = PREFIX + "01068";
  static final String DEPENDENCY_NODE_ADDED_UNRESOLVED  = PREFIX + "01069";
  static final String DEPENDENCY_NODE_MOVED_UNRESOLVED  = PREFIX + "01070";
  static final String DEPENDENCY_NODE_NOT_RESOLVED      = PREFIX + "01071";
  static final String DEPENDENCY_NODE_IS_ACTIVE         = PREFIX + "01072";

  // 01081 - 01110 entity object related messages
  static final String ENTITY_IDENTIFIER                 = PREFIX + "01081";
  static final String ENTITY_SYSTEM_PROPERTY            = PREFIX + "01082";
  static final String ENTITY_LOOKUP                     = PREFIX + "01083";
  static final String ENTITY_SERVERTYPE                 = PREFIX + "01084";
  static final String ENTITY_SERVER                     = PREFIX + "01085";
  static final String ENTITY_RESOURCE                   = PREFIX + "01086";
  static final String ENTITY_PROCESS                    = PREFIX + "01087";
  static final String ENTITY_PROCESSDEFINITION          = PREFIX + "01088";
  static final String ENTITY_FORM                       = PREFIX + "01089";
  static final String ENTITY_FORMDEFINITION             = PREFIX + "01090";
  static final String ENTITY_SCHEDULERTASK              = PREFIX + "01091";
  static final String ENTITY_SCHEDULERJOB               = PREFIX + "01092";
  static final String ENTITY_EMAIL_TEMPLATE             = PREFIX + "01093";
  static final String ENTITY_ROLE                       = PREFIX + "01094";
  static final String ENTITY_GROUP                      = PREFIX + "01095";
  static final String ENTITY_POLICY                     = PREFIX + "01096";
  static final String ENTITY_ORGANIZATION               = PREFIX + "01097";
  static final String ENTITY_IDENTITY                   = PREFIX + "01098";
  static final String ENTITY_ACCOUNT                    = PREFIX + "01099";
  static final String ENTITY_ATTRIBUTE                  = PREFIX + "01100";
  static final String ENTITY_ENTITLEMENT                = PREFIX + "01101";
  static final String ENTITY_CATALOG                    = PREFIX + "01102";
  static final String ENTITY_RECONCILE                  = PREFIX + "01103";
  static final String ENTITY_PROVISION                  = PREFIX + "01104";

  // 01111 - 01120 entity resolver related messages
  static final String KEY_TORESOLVE                     = PREFIX + "01111";
  static final String KEY_RESOLVED                      = PREFIX + "01112";
  static final String NAME_TORESOLVE                    = PREFIX + "01113";
  static final String NAME_RESOLVED                     = PREFIX + "01114";

  // 01121 - 01130 metadata connection related messages
  static final String METADATA_SESSION_CREATE           = PREFIX + "01121";
  static final String METADATA_SESSION_CREATED          = PREFIX + "01122";
  static final String METADATA_SESSION_COMMIT           = PREFIX + "01123";
  static final String METADATA_SESSION_COMMITED         = PREFIX + "01124";
  static final String METADATA_SESSION_ROLLBACK         = PREFIX + "01125";
  static final String METADATA_SESSION_ROLLEDBACK       = PREFIX + "01126";

  // 01131 - 01140 metadata object related messages
  static final String METADATA_OBJECT_FETCH             = PREFIX + "01131";
  static final String METADATA_OBJECT_STORE             = PREFIX + "01132";
  static final String METADATA_OBJECT_MARSHAL           = PREFIX + "01133";
  static final String METADATA_OBJECT_UNMARSHAL         = PREFIX + "01134";

  // 01141 - 01150 entitlement related messages
  static final String ENTITLEMENT_ACTION                = PREFIX + "01141";
  static final String ENTITLEMENT_ACTION_ASSIGN         = PREFIX + "01142";
  static final String ENTITLEMENT_ACTION_REVOKE         = PREFIX + "01143";
  static final String ENTITLEMENT_ACTION_ENABLE         = PREFIX + "01144";
  static final String ENTITLEMENT_ACTION_DISABLE        = PREFIX + "01145";
  static final String ENTITLEMENT_RISK                  = PREFIX + "01146";
  static final String ENTITLEMENT_RISK_NONE             = PREFIX + "01147";
  static final String ENTITLEMENT_RISK_LOW              = PREFIX + "01148";
  static final String ENTITLEMENT_RISK_MEDIUM           = PREFIX + "01149";
  static final String ENTITLEMENT_RISK_HIGH             = PREFIX + "01150";

  // 01151 - 01160 lookup reconciliation related messages
  static final String LOOKUP_CREATE_VALUE               = PREFIX + "01151";
  static final String LOOKUP_REMOVE_VALUE               = PREFIX + "01152";
  static final String LOOKUP_DUPLICATE_VALUE            = PREFIX + "01153";
  static final String LOOKUP_FILTER_NOTACCEPTED         = PREFIX + "01154";
}