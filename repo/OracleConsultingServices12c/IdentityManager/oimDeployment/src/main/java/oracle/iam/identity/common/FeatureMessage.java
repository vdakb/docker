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

    File        :   FeatureMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureMessage.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common;

////////////////////////////////////////////////////////////////////////////////
// interface FeatureMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface FeatureMessage extends FeatureConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 01031 - 01040 cache utility related messages
  static final String CACHE_CATEGORY_MISSING               = PREFIX + "01031";
  static final String CACHE_CATEGORY_PROCEED               = PREFIX + "01032";
  static final String CACHE_CATEGORY_FAILED                = PREFIX + "01033";

  // 01051 - 01060 operation related messages
  static final String OPERATION_INVOKE                     = PREFIX + "01051";
  static final String OPERATION_INVOKE_SUCCESS             = PREFIX + "01052";
  static final String OPERATION_INVOKE_ERROR               = PREFIX + "01053";

  // 01061 - 01070 assembly related messages
  static final String ASSEMBLY_CREATE                      = PREFIX + "01061";
  static final String ASSEMBLY_SUCCESS                     = PREFIX + "01062";
  static final String ASSEMBLY_ERROR                       = PREFIX + "01063";
  static final String DEPENDENCY_CREATE                    = PREFIX + "01064";
  static final String DEPENDENCY_SUCCESS                   = PREFIX + "01065";
  static final String DEPENDENCY_ERROR                     = PREFIX + "01066";
  static final String EXPORT_CATEGORY_SEARCH               = PREFIX + "01067";
  static final String EXPORT_ASSEMBLY_EMPTY                = PREFIX + "01068";
  static final String IMPORT_ASSEMBLY_EMPTY                = PREFIX + "01069";

  // 01081 - 01090 import compilation related messages
  static final String IMPORT_OPERATION_START               = PREFIX + "01081";
  static final String IMPORT_OPERATION_SUCCESS             = PREFIX + "01082";
  static final String IMPORT_OPERATION_ERROR               = PREFIX + "01083";

  // 01091 - 01110 java archive, resource bundle and plugin related messages
  static final String UPLOAD_FILE_FETCH                    = PREFIX + "01091";
  static final String UPLOAD_FILE_FETCHED                  = PREFIX + "01092";
  static final String UPLOAD_FILE_BEGIN                    = PREFIX + "01093";
  static final String UPLOAD_FILE_COMPLETE                 = PREFIX + "01094";
  static final String DOWNLOAD_FILE_STORE                  = PREFIX + "01095";
  static final String DOWNLOAD_FILE_STORED                 = PREFIX + "01096";
  static final String PLUGIN_UPLOAD_BEGIN                  = PREFIX + "01097";
  static final String PLUGIN_UPLOAD_COMPLETE               = PREFIX + "01098";
  static final String PLUGIN_DELETE_BEGIN                  = PREFIX + "01099";
  static final String PLUGIN_DELETE_COMPLETE               = PREFIX + "01100";
  static final String PLUGIN_REGISTERED                    = PREFIX + "01101";
  static final String PLUGIN_UNREGISTRED                   = PREFIX + "01102";

  // 01141 - 01150 request dataset documentation related messages
  static final String REQUEST_DATASET_CREATE               = PREFIX + "01141";
  static final String REQUEST_DATASET_SUCCESS              = PREFIX + "01142";
  static final String REQUEST_DATASET_ERROR                = PREFIX + "01143";
  static final String REQUEST_DATASET_SKIPPED              = PREFIX + "01144";
  static final String REQUEST_DATASET_CONFIRMATION_TITLE   = PREFIX + "01145";
  static final String REQUEST_DATASET_CONFIRMATION_MESSAGE = PREFIX + "01146";

  // 01151 - 01190 scheduled job related messages
  static final String JOB_STATUS_0                         = PREFIX + "01151";
  static final String JOB_STATUS_1                         = PREFIX + "01152";
  static final String JOB_STATUS_STOPPED                   = PREFIX + "01153";
  static final String JOB_STATUS_3                         = PREFIX + "01154";
  static final String JOB_STATUS_4                         = PREFIX + "01155";
  static final String JOB_STATUS_RUNNING                   = PREFIX + "01156";
  static final String JOB_STATUS_6                         = PREFIX + "01157";
  static final String JOB_STATUS_INTERRUPTED               = PREFIX + "01158";
  static final String JOB_INSTANCE_SEARCH                  = PREFIX + "01159";
  static final String JOB_INSTANCE_FOUND                   = PREFIX + "01160";
  static final String JOB_INSTANCE_OPERATION_CREATE        = PREFIX + "01161";
  static final String JOB_INSTANCE_OPERATION_UPDATE        = PREFIX + "01162";
  static final String JOB_INSTANCE_OPERATION_DELETE        = PREFIX + "01163";
  static final String JOB_INSTANCE_OPERATION_EXECUTE       = PREFIX + "01164";
  static final String JOB_INSTANCE_OPERATION_HISTORY       = PREFIX + "01165";
  static final String JOB_INSTANCE_OPERATION_BEGIN         = PREFIX + "01166";
  static final String JOB_INSTANCE_OPERATION_SUCCESS       = PREFIX + "01167";
  static final String JOB_INSTANCE_OPERATION_FAILED        = PREFIX + "01168";
  static final String JOB_INSTANCE_OBSERVATION_STARTED     = PREFIX + "01169";
  static final String JOB_INSTANCE_OBSERVATION_FINISHED    = PREFIX + "01170";
  static final String JOB_INSTANCE_COMMAND_START           = PREFIX + "01171";
  static final String JOB_INSTANCE_COMMAND_STOP            = PREFIX + "01172";
  static final String JOB_INSTANCE_COMMAND_ENABLE          = PREFIX + "01173";
  static final String JOB_INSTANCE_COMMAND_DISABLE         = PREFIX + "01174";
  static final String JOB_INSTANCE_COMMAND_BEGIN           = PREFIX + "01175";
  static final String JOB_INSTANCE_COMMAND_SUCCESS         = PREFIX + "01176";
  static final String JOB_INSTANCE_COMMAND_FAILED          = PREFIX + "01177";
  static final String JOB_INSTANCE_STATUS_CHECK            = PREFIX + "01178";
  static final String JOB_INSTANCE_STATUS_RESULT           = PREFIX + "01179";
  static final String JOB_INSTANCE_STATUS_UPDATE           = PREFIX + "01180";
  static final String JOB_INSTANCE_STATUS_UPDATED          = PREFIX + "01181";
  static final String JOB_INSTANCE_PARAMETER_UPDATE        = PREFIX + "01182";
  static final String JOB_INSTANCE_PARAMETER_UPDATED       = PREFIX + "01183";
  static final String JOB_INSTANCE_SCHEDULE_UPDATE         = PREFIX + "01184";
  static final String JOB_INSTANCE_SCHEDULE_UPDATED        = PREFIX + "01185";

  // 01191 - 01220 process form related messages
  static final String PROCESS_FORM_CREATE                  = PREFIX + "01191";
  static final String PROCESS_FORM_CREATED                 = PREFIX + "01192";
  static final String PROCESS_FORM_MODIFY                  = PREFIX + "01193";
  static final String PROCESS_FORM_MODIFIED                = PREFIX + "01194";

  // 01121 - 01250 composite deploymnet related messages
  static final String COMPOSITE_DEPLOY                     = PREFIX + "01121";
  static final String COMPOSITE_DEPLOYED                   = PREFIX + "01122";
  static final String COMPOSITE_UNDEPLOY                   = PREFIX + "01123";
  static final String COMPOSITE_UNDEPLOYED                 = PREFIX + "01124";
  static final String COMPOSITE_REDEPLOY                   = PREFIX + "01125";
  static final String COMPOSITE_REDEPLOYED                 = PREFIX + "01126";
  static final String COMPOSITE_CONFIGURE                  = PREFIX + "01127";
  static final String COMPOSITE_CONFIGURED                 = PREFIX + "01128";
  static final String COMPOSITE_EXISTS                     = PREFIX + "01129";
  static final String PARTITION_CREATE                     = PREFIX + "01230";
  static final String PARTITION_CREATED                    = PREFIX + "01231";
  static final String PARTITION_DELETE                     = PREFIX + "01232";
  static final String PARTITION_DELETED                    = PREFIX + "01233";
  static final String PARTITION_START                      = PREFIX + "01234";
  static final String PARTITION_STARTED                    = PREFIX + "01235";
  static final String PARTITION_STOP                       = PREFIX + "01236";
  static final String PARTITION_STOPPED                    = PREFIX + "01237";
  static final String PARTITION_ACTIVATE                   = PREFIX + "01238";
  static final String PARTITION_ACTIVATED                  = PREFIX + "01239";
  static final String PARTITION_RETIRE                     = PREFIX + "01240";
  static final String PARTITION_RETIRED                    = PREFIX + "01241";

  // 01251 - 01270 workflow registration related messages
  static final String WORKFLOW_INVENTORY_SEARCH            = PREFIX + "01251";
  static final String WORKFLOW_INVENTORY_EMPTY             = PREFIX + "01252";
  static final String WORKFLOW_INVENTORY_SEPARATOR         = PREFIX + "01253";
  static final String WORKFLOW_INVENTORY_NAME              = PREFIX + "01254";
  static final String WORKFLOW_INVENTORY_CATEGORY          = PREFIX + "01255";
  static final String WORKFLOW_INVENTORY_PROVIDER          = PREFIX + "01256";
  static final String WORKFLOW_INVENTORY_STATUS            = PREFIX + "01257";
  static final String WORKFLOW_INVENTORY_REGISTER          = PREFIX + "01258";
  static final String WORKFLOW_INVENTORY_REGISTERED        = PREFIX + "01259";
  static final String WORKFLOW_INVENTORY_ENABLE            = PREFIX + "01260";
  static final String WORKFLOW_INVENTORY_ENABLED           = PREFIX + "01261";
  static final String WORKFLOW_INVENTORY_DISABLE           = PREFIX + "01262";
  static final String WORKFLOW_INVENTORY_DISABLED          = PREFIX + "01263";

  // 01271 - 01280 metadata store management related messages
  static final String METADATA_CHANGES_FLUSH               = PREFIX + "01271";
  static final String METADATA_CHANGES_FLUSHED             = PREFIX + "01272";

  // 01281 - 01310 sandbox management related messages
  static final String SANDBOX_EXPORT                       = PREFIX + "01281";
  static final String SANDBOX_EXPORTED                     = PREFIX + "01282";
  static final String SANDBOX_EXPORT_EMPTY                 = PREFIX + "01283";
  static final String SANDBOX_IMPORT                       = PREFIX + "01284";
  static final String SANDBOX_IMPORTED                     = PREFIX + "01285";
  static final String SANDBOX_PUBLISH                      = PREFIX + "01286";
  static final String SANDBOX_PUBLISHED                    = PREFIX + "01287";
  static final String SANDBOX_PUBLISH_EMPTY                = PREFIX + "01288";
  static final String SANDBOX_COMMIT                       = PREFIX + "01289";
  static final String SANDBOX_COMMITED                     = PREFIX + "01290";
  static final String SANDBOX_ROLLBACK                     = PREFIX + "01291";
  static final String SANDBOX_ROLLEDBACK                   = PREFIX + "01292";
  static final String SANDBOX_CREATE                       = PREFIX + "01293";
  static final String SANDBOX_CREATED                      = PREFIX + "01294";
  static final String SANDBOX_DELETE                       = PREFIX + "01295";
  static final String SANDBOX_DELETED                      = PREFIX + "01296";
  static final String SANDBOX_MARSHALL                     = PREFIX + "01297";
  static final String SANDBOX_MARSHALLED                   = PREFIX + "01298";
  static final String SANDBOX_MARSHALL_POPULATE            = PREFIX + "01299";
  static final String SANDBOX_MARSHALL_POPULATED           = PREFIX + "01300";
  static final String SANDBOX_MARSHALL_MODULE              = PREFIX + "01301";
  static final String SANDBOX_MARSHALL_BACKEND             = PREFIX + "01302";
  static final String SANDBOX_MARSHALL_FRONTEND            = PREFIX + "01303";
  static final String SANDBOX_MARSHALL_TASKFLOW            = PREFIX + "01304";
  static final String SANDBOX_DIRECTORY_TITLE              = PREFIX + "01305";
  static final String SANDBOX_DIRECTORY_MESSAGE            = PREFIX + "01306";
  static final String SANDBOX_DATASET_TITLE                = PREFIX + "01307";
  static final String SANDBOX_DATASET_MESSAGE              = PREFIX + "01308";
  static final String SANDBOX_DATASET_CREATE               = PREFIX + "01309";
  static final String SANDBOX_DATASET_CREATED              = PREFIX + "01310";
  static final String SANDBOX_DATASET_MODIFY               = PREFIX + "01311";
  static final String SANDBOX_DATASET_MODIFIED             = PREFIX + "01312";

  // 01321 - 01330 entity publication related messages
  static final String PUBLICATION_ASSIGN_BEGIN             = PREFIX + "01321";
  static final String PUBLICATION_ASSIGN_SUCCESS           = PREFIX + "01322";
  static final String PUBLICATION_REVOKE_BEGIN             = PREFIX + "01323";
  static final String PUBLICATION_REVOKE_SUCCESS           = PREFIX + "01324";

  // 01341 - 01360 provisioning related messages
  static final String PROVISION_ASSIGN_BEGIN               = PREFIX + "01341";
  static final String PROVISION_ASSIGN_SUCCESS             = PREFIX + "01342";
  static final String PROVISION_ASSIGN_SKIPPED             = PREFIX + "01343";
  static final String PROVISION_REVOKE_BEGIN               = PREFIX + "01344";
  static final String PROVISION_REVOKE_SUCCESS             = PREFIX + "01345";
  static final String PROVISION_REVOKE_SKIPPED             = PREFIX + "01346";
  static final String PROVISION_MODIFY_BEGIN               = PREFIX + "01347";
  static final String PROVISION_MODIFY_SUCCESS             = PREFIX + "01348";
  static final String PROVISION_MODIFY_SKIPPED             = PREFIX + "01349";
  static final String PROVISION_ENABLE_BEGIN               = PREFIX + "01350";
  static final String PROVISION_ENABLE_SUCCESS             = PREFIX + "01351";
  static final String PROVISION_ENABLE_SKIPPED             = PREFIX + "01352";
  static final String PROVISION_DISABLE_BEGIN              = PREFIX + "01353";
  static final String PROVISION_DISABLE_SUCCESS            = PREFIX + "01354";
  static final String PROVISION_DISABLE_SKIPPED            = PREFIX + "01355";

  // 01361 - 01380 request related messages
  static final String REQUEST_ASSIGN_BEGIN                 = PREFIX + "01361";
  static final String REQUEST_ASSIGN_SUCCESS               = PREFIX + "01362";
  static final String REQUEST_ASSIGN_SKIPPED               = PREFIX + "01363";
  static final String REQUEST_REVOKE_BEGIN                 = PREFIX + "01364";
  static final String REQUEST_REVOKE_SUCCESS               = PREFIX + "01365";
  static final String REQUEST_REVOKE_SKIPPED               = PREFIX + "01366";
  static final String REQUEST_MODIFY_BEGIN                 = PREFIX + "01367";
  static final String REQUEST_MODIFY_SUCCESS               = PREFIX + "01368";
  static final String REQUEST_MODIFY_SKIPPED               = PREFIX + "01369";
  static final String REQUEST_ENABLE_BEGIN                 = PREFIX + "01360";
  static final String REQUEST_ENABLE_SUCCESS               = PREFIX + "01371";
  static final String REQUEST_ENABLE_SKIPPED               = PREFIX + "01372";
  static final String REQUEST_DISABLE_BEGIN                = PREFIX + "01373";
  static final String REQUEST_DISABLE_SUCCESS              = PREFIX + "01374";
  static final String REQUEST_DISABLE_SKIPPED              = PREFIX + "01375";
  static final String REQUEST_SUBMIT_BEGIN                 = PREFIX + "01376";
  static final String REQUEST_SUBMIT_SUCCESS               = PREFIX + "01377";

  // 01381 - 01390 access policy related messages
  static final String ACCESSPOLICY_ALLOW_ASSIGN            = PREFIX + "01381";
  static final String ACCESSPOLICY_ALLOW_REVOKE            = PREFIX + "01382";
  static final String ACCESSPOLICY_DENY_ASSIGN             = PREFIX + "01383";
  static final String ACCESSPOLICY_DENY_REVOKE             = PREFIX + "01384";
}