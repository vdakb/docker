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
    Subsystem   :   Deployment Utilities 12c

    File        :   ServiceMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceMessage.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment;

////////////////////////////////////////////////////////////////////////////////
// interface ServiceMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ServiceMessage extends ServiceConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 01001 - 01010 task related messages
  static final String TASK_VALIDATING                        = PREFIX + "01001";
  static final String TASK_VALIDATED                         = PREFIX + "01002";
  static final String TASK_EXECUTING                         = PREFIX + "01003";
  static final String TASK_EXECUTED                          = PREFIX + "01004";

  // 01011 - 01030 server connection related messages
  static final String SERVER_CONNECTING                      = PREFIX + "01011";
  static final String SERVER_CONTEXT_CONNECTING              = PREFIX + "01012";
  static final String SERVER_CONNECTED                       = PREFIX + "01013";
  static final String SERVER_DISCONNECTING                   = PREFIX + "01014";
  static final String SERVER_CONTEXT_DISCONNECTING           = PREFIX + "01015";
  static final String SERVER_DISCONNECTED                    = PREFIX + "01016";

  // 01021 - 01030 server status related messages
  static final String SERVER_CONTROL                         = PREFIX + "01021";
  static final String SERVER_CONTROL_COMPLETE                = PREFIX + "01022";
  static final String SERVER_CONTROL_FAILED                  = PREFIX + "01023";
  static final String SERVER_CONTROL_STATUS                  = PREFIX + "01024";

  // 01031 - 01040 server security related messages
  static final String SECURITY_ENTITY_ROLE                   = PREFIX + "01031";
  static final String SECURITY_ENTITY_USER                   = PREFIX + "01032";
  static final String SECURITY_ENTITY_ALIAS                  = PREFIX + "01033";
  static final String SECURITY_ENTITY_CREDENTIAL             = PREFIX + "01034";
  static final String SECURITY_ENTITY_POLICY                 = PREFIX + "01035";
  static final String SECURITY_ENTITY_CODEBASE               = PREFIX + "01036";
  static final String SECURITY_ENTITY_PRINCIPAL              = PREFIX + "01037";
  static final String SECURITY_ENTITY_PERMISSION             = PREFIX + "01038";

  // 01041 - 01050 managed bean operation related messages
  static final String OPERATION_INVOKE                       = PREFIX + "01041";
  static final String OPERATION_INVOKE_SUCCESS               = PREFIX + "01042";
  static final String OPERATION_INVOKE_ERROR                 = PREFIX + "01043";
  static final String OPERATION_INVOKE_PAYLOAD               = PREFIX + "01044";

  // 01051 - 01070 object/entity operation related messages
  static final String OPERATION_CREATE_BEGIN                 = PREFIX + "01051";
  static final String OPERATION_CREATE_SUCCESS               = PREFIX + "01052";
  static final String OPERATION_CREATE_SKIPPED               = PREFIX + "01053";
  static final String OPERATION_DELETE_BEGIN                 = PREFIX + "01054";
  static final String OPERATION_DELETE_SUCCESS               = PREFIX + "01055";
  static final String OPERATION_DELETE_SKIPPED               = PREFIX + "01056";
  static final String OPERATION_ENABLE_BEGIN                 = PREFIX + "01057";
  static final String OPERATION_ENABLE_SUCCESS               = PREFIX + "01058";
  static final String OPERATION_DISABLE_BEGIN                = PREFIX + "01059";
  static final String OPERATION_DISABLE_SUCCESS              = PREFIX + "01060";
  static final String OPERATION_MODIFY_BEGIN                 = PREFIX + "01061";
  static final String OPERATION_MODIFY_SUCCESS               = PREFIX + "01062";
  static final String OPERATION_MODIFY_SKIPPED               = PREFIX + "01063";
  static final String OPERATION_VALIDATE_BEGIN               = PREFIX + "01064";
  static final String OPERATION_VALIDATE_SUCCESS             = PREFIX + "01065";
  static final String OPERATION_ASSIGN_BEGIN                 = PREFIX + "01066";
  static final String OPERATION_ASSIGN_SUCCESS               = PREFIX + "01067";
  static final String OPERATION_REVOKE_BEGIN                 = PREFIX + "01068";
  static final String OPERATION_REVOKE_SUCCESS               = PREFIX + "01069";

  // 01071 - 01080 export documentation related messages
  static final String DOCUMENT_CREATE                        = PREFIX + "01071";
  static final String DOCUMENT_SUCCESS                       = PREFIX + "01072";
  static final String DOCUMENT_ERROR                         = PREFIX + "01073";
  static final String DOCUMENT_CONFIRMATION_TITLE            = PREFIX + "01076";
  static final String DOCUMENT_CONFIRMATION_MESSAGE          = PREFIX + "01077";

  // 01081 - 01090 metadata connection related messages
  static final String METADATA_INSTANCE_CREATE               = PREFIX + "01081";
  static final String METADATA_INSTANCE_CREATED              = PREFIX + "01082";
  static final String METADATA_INSTANCE_CLOSE                = PREFIX + "01083";
  static final String METADATA_INSTANCE_CLOSED               = PREFIX + "01084";
  static final String METADATA_SESSION_CREATE                = PREFIX + "01085";
  static final String METADATA_SESSION_CREATED               = PREFIX + "01086";

  // 01091 - 01110 metadata documentation related messages
  static final String METADATA_PATH_CREATE                   = PREFIX + "01091";
  static final String METADATA_PATH_EXISTS                   = PREFIX + "01092";
  static final String METADATA_PATH_NOTEXISTS                = PREFIX + "01093";
  static final String METADATA_DOCUMENT_UPLOADED             = PREFIX + "01094";
  static final String METADATA_DOCUMENT_UPLOAD_DELETED       = PREFIX + "01095";
  static final String METADATA_DOCUMENT_DOWNLOADED           = PREFIX + "01096";
  static final String METADATA_DOCUMENT_REMOVED              = PREFIX + "01097";
  static final String METADATA_DOCUMENT_NOTEXISTS            = PREFIX + "01098";
  static final String METADATA_DOCUMENT_EMPTY                = PREFIX + "01099";
  static final String METADATA_DOCUMENT_OBJECT_MODE          = PREFIX + "01100";
  static final String METADATA_DOCUMENT_DOCUMENT_MODE        = PREFIX + "01101";
  static final String METADATA_DOCUMENT_STREAMED_MODE        = PREFIX + "01102";
  static final String METADATA_DOCUMENT_CONFIRMATION_TITLE   = PREFIX + "01103";
  static final String METADATA_DOCUMENT_CONFIRMATION_MESSAGE = PREFIX + "01104";

  // 01111 - 01120 http operations related messages
  static final String HTTPSERVICE_LOCATION_MOVED             = PREFIX + "01111";
  static final String HTTPSERVICE_LOCATION_MOVED_PERMANENT   = PREFIX + "01112";
  static final String HTTPSERVICE_DOWNLOAD_SKIPPED           = PREFIX + "01113";

  // 01121 - 01130 webservice operations related messages
  static final String WEBSERVICE_RESTART                     = PREFIX + "01121";
  static final String WEBSERVICE_SUBJECT_LOCATED             = PREFIX + "01122";
  static final String WEBSERVICE_POLICY_ATTACHED             = PREFIX + "01123";
  static final String WEBSERVICE_POLICY_ALREADY_ATTACHED     = PREFIX + "01124";
  static final String WEBSERVICE_POLICY_DETACHED             = PREFIX + "01125";
  static final String WEBSERVICE_POLICY_ALREADY_DETACHED     = PREFIX + "01126";
  static final String WEBSERVICE_POLICY_ENABLED              = PREFIX + "01127";
  static final String WEBSERVICE_POLICY_ALREADY_ENABLED      = PREFIX + "01128";
  static final String WEBSERVICE_POLICY_DISABLED             = PREFIX + "01129";
  static final String WEBSERVICE_POLICY_ALREADY_DISABLED     = PREFIX + "01130";

  // 01131 - 01140 webservice reporting related messages
  static final String WEBSERVICE_POLICY_REPORT_PREFIX        = PREFIX + "01131";
  static final String WEBSERVICE_POLICY_REPORT_ENTITY        = PREFIX + "01132";
  static final String WEBSERVICE_POLICY_REPORT_CATEGORY      = PREFIX + "01133";
  static final String WEBSERVICE_POLICY_REPORT_STATUS        = PREFIX + "01134";
  static final String WEBSERVICE_POLICY_REPORT_EMPTY         = PREFIX + "01135";
  static final String WEBSERVICE_POLICY_REPORT_SEPARATOR     = PREFIX + "01136";
  static final String WEBSERVICE_POLICY_REPORT_HEADER        = PREFIX + "01137";
  static final String WEBSERVICE_POLICY_REPORT_FORMAT        = PREFIX + "01138";

  // 01141 - 01150 fusion middleware reporting related messages
  static final String FUSION_INVENTORY_ENTITY                = PREFIX + "01141";
  static final String FUSION_INVENTORY_EMPTY                 = PREFIX + "01142";
  static final String FUSION_INVENTORY_SEPARATOR             = PREFIX + "01143";
  static final String FUSION_INVENTORY_FORMAT                = PREFIX + "01144";

  // 01151 - 01160 opss related messages
  static final String OPSS_CONTEXT_CREATE                    = PREFIX + "01151";
  static final String OPSS_CONTEXT_CREATED                   = PREFIX + "01152";
  static final String OPSS_CREDENTIALSTORE_OBTAIN            = PREFIX + "01153";
  static final String OPSS_CREDENTIALSTORE_OBTAINED          = PREFIX + "01154";
  static final String OPSS_CREDENTIAL_OBTAIN                 = PREFIX + "01155";
  static final String OPSS_CREDENTIAL_OBTAINED               = PREFIX + "01156";
}