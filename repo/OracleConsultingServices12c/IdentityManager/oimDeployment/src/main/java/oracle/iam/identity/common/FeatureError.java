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

    File        :   FeatureError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common;

////////////////////////////////////////////////////////////////////////////////
// interface FeatureError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface FeatureError extends FeatureConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 00001 - 00006 cache utility related errors
  static final String CACHE_CATEGORY_ONLYONCE          = PREFIX + "00001";

  // 00021 - 00050 object utility related errors
  static final String OBJECT_REPOSITORY_NOTLOCKED      = PREFIX + "00021";
  static final String OBJECT_REPOSITORY_LOCKLOST       = PREFIX + "00022";
  static final String EXPORT_CATEGORY_MISSING          = PREFIX + "00023";
  static final String EXPORT_CATEGORY_ONLYONCE         = PREFIX + "00024";
  static final String EXPORT_FILE_MANDATORY            = PREFIX + "00025";
  static final String EXPORT_FILE_ONLYONCE             = PREFIX + "00026";
  static final String EXPORT_FILE_DESCRIPTION          = PREFIX + "00027";
  static final String EXPORT_FILE_ISDIRECTORY          = PREFIX + "00028";
  static final String EXPORT_FILE_NODIRECTORY          = PREFIX + "00029";
  static final String EXPORT_FILE_NOPERMISSION         = PREFIX + "00030";
  static final String IMPORT_FILE_CONSTRAINT           = PREFIX + "00031";
  static final String IMPORT_FILE_MANDATORY            = PREFIX + "00032";
  static final String IMPORT_FILE_ONLYONCE             = PREFIX + "00033";
  static final String IMPORT_FILE_ISDIRECTORY          = PREFIX + "00034";
  static final String IMPORT_FILE_NOTEXISTS            = PREFIX + "00035";
  static final String IMPORT_FILE_NOPERMISSION         = PREFIX + "00036";
  static final String IMPORT_FILE_FETCH                = PREFIX + "00037";
  static final String IMPORT_FILE_UNRESOLVED           = PREFIX + "00038";
  static final String IMPORT_FILE_UNRESOLVED_OBJECT    = PREFIX + "00039";

  // 00051 - 00070 parameter related errors
  static final String PARAMETER_NAME_EXCEPTION         = PREFIX + "00051";

  // 00061 - 00080 library, resource bundle and plugin utility related errors
  static final String UPLOAD_FILE_MANDATORY            = PREFIX + "00061";
  static final String UPLOAD_FILE_NOTEXISTS            = PREFIX + "00062";
  static final String UPLOAD_FILE_ISDIRECTORY          = PREFIX + "00063";
  static final String UPLOAD_FILE_NOPERMISSION         = PREFIX + "00064";
  static final String UPLOAD_FILE_ONLYONCE             = PREFIX + "00065";
  static final String UPLOAD_FILE_FETCH                = PREFIX + "00066";
  static final String UPLOAD_FILE_ERROR                = PREFIX + "00067";
  static final String DOWNLOAD_FILE_ERROR              = PREFIX + "00068";
  static final String PLUGIN_FILE_NOTEXISTS            = PREFIX + "00069";
  static final String PLUGIN_FILE_NOPERMISSION         = PREFIX + "00070";
  static final String PLUGIN_FILE_FETCH                = PREFIX + "00071";
  static final String PLUGIN_ONLY_ONCE                 = PREFIX + "00072";
  static final String PLUGIN_UPLOAD_ERROR              = PREFIX + "00073";
  static final String PLUGIN_DELETE_ERROR              = PREFIX + "00074";
  static final String PLUGIN_REGISTER_ERROR            = PREFIX + "00075";
  static final String PLUGIN_UNREGISTER_ERROR          = PREFIX + "00076";
  static final String PLUGIN_CLASSNAME_ONLYONCE        = PREFIX + "00077";

  // 00091 - 00110 composite deployment related errors
  static final String COMPOSITE_FILE_MANDATORY         = PREFIX + "00091";
  static final String COMPOSITE_FILE_ONLYONCE          = PREFIX + "00092";
  static final String COMPOSITE_FILE_ISDIRECTORY       = PREFIX + "00093";
  static final String COMPOSITE_FILE_NOTEXISTS         = PREFIX + "00094";
  static final String COMPOSITE_FILE_NOPERMISSION      = PREFIX + "00095";
  static final String COMPOSITE_CONNECTION_ERROR       = PREFIX + "00096";
  static final String COMPOSITE_CONNECTION_TIMEOUT     = PREFIX + "00097";
  static final String COMPOSITE_CONNECTION_REQUEST     = PREFIX + "00098";
  static final String COMPOSITE_DEPLOYMENT_ERROR       = PREFIX + "00099";
  static final String COMPOSITE_UNDEPLOYMENT_ERROR     = PREFIX + "00100";
  static final String COMPOSITE_REDEPLOYMENT_ERROR     = PREFIX + "00101";

  // 00111 - 00130 composite configuration related errors
  static final String COMPOSITE_MANAGEMENT_ERROR       = PREFIX + "00111";
  static final String COMPOSITE_WORKFLOW_ERROR         = PREFIX + "00112";
  static final String COMPOSITE_LOCATOR_ERROR          = PREFIX + "00113";
  static final String COMPOSITE_LOOKUP_ERROR           = PREFIX + "00114";
  static final String COMPOSITE_PARTITION_ONLYONCE     = PREFIX + "00115";
  static final String COMPOSITE_PARTITION_CONNECT      = PREFIX + "00116";
  static final String COMPOSITE_PARTITION_DISCONNECT   = PREFIX + "00117";
  static final String COMPOSITE_PARTITION_EXISTS       = PREFIX + "00118";
  static final String COMPOSITE_PARTITION_NOTEXISTS    = PREFIX + "00119";
  static final String COMPOSITE_PARTITION_CREATE       = PREFIX + "00120";
  static final String COMPOSITE_PARTITION_DELETE       = PREFIX + "00121";
  static final String COMPOSITE_PARTITION_START        = PREFIX + "00122";
  static final String COMPOSITE_PARTITION_STOP         = PREFIX + "00123";
  static final String COMPOSITE_PARTITION_ACTIVATE     = PREFIX + "00124";
  static final String COMPOSITE_PARTITION_RETIRE       = PREFIX + "00125";
  static final String COMPOSITE_PARTITION_LOOKUP       = PREFIX + "00126";
  static final String COMPOSITE_WORKFLOW_NOTEXISTS     = PREFIX + "00127";
  static final String COMPOSITE_HUMANTASK_MISSING      = PREFIX + "00128";
  static final String COMPOSITE_HUMANTASK_CONFIGURED   = PREFIX + "00129";

  // 00131 - 00150 workflow registration related errors
  static final String WORKFLOW_RESOURCE_MANDATORY      = PREFIX + "00131";
  static final String WORKFLOW_RESOURCE_ONLYONCE       = PREFIX + "00132";
  static final String WORKFLOW_DATASETFILE_MANDATORY   = PREFIX + "00133";
  static final String WORKFLOW_DATASETFILE_ONLYONCE    = PREFIX + "00134";
  static final String WORKFLOW_DATASETFILE_DIRECTORY   = PREFIX + "00135";
  static final String WORKFLOW_PROCESS_MANDATORY       = PREFIX + "00136";
  static final String WORKFLOW_PROCESS_ONLYONCE        = PREFIX + "00137";
  static final String WORKFLOW_OPERATION_MANDATORY     = PREFIX + "00138";
  static final String WORKFLOW_OPERATION_ONLYONCE      = PREFIX + "00139";
  static final String WORKFLOW_ATTRIBUTE_ONLYONCE      = PREFIX + "00140";
  static final String WORKFLOW_REFERENCE_ONLYONCE      = PREFIX + "00141";
  static final String WORKFLOW_LOOKUPVALUE_ONLYONCE    = PREFIX + "00142";
  static final String WORKFLOW_PROCESSFORM_ONLYONCE    = PREFIX + "00143";
  static final String WORKFLOW_PROCESSFIELD_ONLYONCE   = PREFIX + "00144";
  static final String WORKFLOW_FIELDPROPERTY_ONLYONCE  = PREFIX + "00145";
  static final String WORKFLOW_REGISTER_ERROR          = PREFIX + "00146";
  static final String WORKFLOW_ENABLE_ERROR            = PREFIX + "00147";
  static final String WORKFLOW_DISABLE_ERROR           = PREFIX + "00148";

  // 00151 - 00160 regular expression related errors
  static final String EXPRESSION_INVALID               = PREFIX + "00151";
  static final String EXPRESSION_UNDEFINED_BITVALUES   = PREFIX + "00152";

  // 00161 - 00170 metadata namespace related errors
  static final String NAMESPACE_PATH_MANDATORY         = PREFIX + "00161";
  static final String NAMESPACE_PATH_ONLYONCE          = PREFIX + "00162";

  // 00171 - 00180 metadata connection related errors
  static final String METADATA_INSTANCE_ERROR          = PREFIX + "00171";
  static final String METADATA_INSTANCE_CREATE         = PREFIX + "00172";
  static final String METADATA_INSTANCE_CLOSE          = PREFIX + "00173";
  static final String METADATA_SESSION_CREATE          = PREFIX + "00174";
  static final String METADATA_SESSION_COMMIT          = PREFIX + "00175";
  static final String METADATA_CHANGES_FLUSH           = PREFIX + "00176";

  // 00181 - 00190 metadata document related errors
  static final String METADATA_DOCUMENT_ONLYONCE       = PREFIX + "00181";
  static final String METADATA_DOCUMENT_CREATE         = PREFIX + "00182";
  static final String METADATA_DOCUMENT_TRANSFORMATION = PREFIX + "00183";
  static final String METADATA_DOCUMENT_REFERENCE      = PREFIX + "00184";
  static final String METADATA_DOCUMENT_READONLY       = PREFIX + "00185";
  static final String METADATA_DOCUMENT_UPDATE         = PREFIX + "00186";
  static final String METADATA_DOCUMENT_UPLOAD         = PREFIX + "00187";
  static final String METADATA_DOCUMENT_DOWNLOAD       = PREFIX + "00188";

  // 00191 - 00200 job trigger related errors
  static final String JOB_SCHEDULE_NOT_SUPPORTED       = PREFIX + "00191";
  static final String JOB_INSTANCE_OPERATION_FAILED    = PREFIX + "00192";

  // 00201 - 00210 resource object related errors
  static final String RESOURCE_OBJECT_ONLYONCE         = PREFIX + "00201";
  static final String RESOURCE_OBJECT_EXISTS           = PREFIX + "00202";
  static final String RESOURCE_OBJECT_NOTEXISTS        = PREFIX + "00203";
  static final String RESOURCE_OBJECT_AMBIGUOS         = PREFIX + "00204";
  static final String RESOURCE_OBJECT_CREATE           = PREFIX + "00205";
  static final String RESOURCE_OBJECT_MODIFY           = PREFIX + "00206";
  static final String RESOURCE_OBJECT_DELETE           = PREFIX + "00207";

  // 00211 - 00220 process definition related errors
  static final String PROCESS_DEFINITION_ONLYONCE      = PREFIX + "00211";
  static final String PROCESS_DEFINITION_EXISTS        = PREFIX + "00212";
  static final String PROCESS_DEFINITION_NOTEXISTS     = PREFIX + "00213";
  static final String PROCESS_DEFINITION_AMBIGUOS      = PREFIX + "00214";
  static final String PROCESS_DEFINITION_CREATE        = PREFIX + "00215";
  static final String PROCESS_DEFINITION_MODIFY        = PREFIX + "00216";
  static final String PROCESS_DEFINITION_DELETE        = PREFIX + "00217";

  // 00221 - 00230 process form related errors
  static final String PROCESS_FORM_ONLYONCE            = PREFIX + "00221";
  static final String PROCESS_FORM_EXISTS              = PREFIX + "00222";
  static final String PROCESS_FORM_NOTEXISTS           = PREFIX + "00223";
  static final String PROCESS_FORM_CREATE              = PREFIX + "00224";
  static final String PROCESS_FORM_MODIFY              = PREFIX + "00225";
  static final String PROCESS_FORM_DELETE              = PREFIX + "00226";

  // 00231 - 00250 sandbox management related errors
  static final String SANDBOX_MANDATORY                = PREFIX + "00231";
  static final String SANDBOX_FILE_MANDATORY           = PREFIX + "00232";
  static final String SANDBOX_FILE_ISDIRECTORY         = PREFIX + "00233";
  static final String SANDBOX_FILE_NOTEXISTS           = PREFIX + "00234";
  static final String SANDBOX_FILE_NOPERMISSION        = PREFIX + "00235";
  static final String SANDBOX_FILE_ONLYONCE            = PREFIX + "00236";
  static final String SANDBOX_NAME_ONLYONCE            = PREFIX + "00237";
  static final String SANDBOX_NAME_INVALID             = PREFIX + "00238";
  static final String SANDBOX_EXISTS                   = PREFIX + "00239";
  static final String SANDBOX_NOTEXISTS                = PREFIX + "00240";

  // 00241 - 00250 sandbox marshalling related errors
  static final String SANDBOX_MODULE_REQUIRED          = PREFIX + "00241";
  static final String SANDBOX_MODULE_ONLYONCE          = PREFIX + "00242";
  static final String SANDBOX_BUNDLE_ONLYONCE          = PREFIX + "00243";
  static final String SANDBOX_MARSHALL_FAILED          = PREFIX + "00244";
  static final String SANDBOX_MARSHALL_STOPPED         = PREFIX + "00245";
  static final String SANDBOX_DIRECTORY_EXISTS         = PREFIX + "00246";
  static final String SANDBOX_DIRECTORY_ISFILE         = PREFIX + "00247";
  static final String SANDBOX_DIRECTORY_DELETE         = PREFIX + "00248";
  static final String SANDBOX_DIRECTORY_MEMBER         = PREFIX + "00249";
  static final String SANDBOX_DIRECTORY_COMPRESS       = PREFIX + "00250";

  // 00251 - 00260 entity publication related errors
  static final String PUBLICATION_ASSIGN_FAILED        = PREFIX + "00251";
  static final String PUBLICATION_REVOKE_FAILED        = PREFIX + "00252";
  static final String SCOPERULE_ASSIGN_FAILED          = PREFIX + "00253";
  static final String SCOPERULE_REVOKE_FAILED          = PREFIX + "00254";

  // 00261 - 00270 provisioning related errors
  static final String ACCOUNT_EXISTS                   = PREFIX + "00261";
  static final String ACCOUNT_NOTEXISTS                = PREFIX + "00262";
  static final String PROVISION_ASSIGN_FAILED          = PREFIX + "00263";
  static final String PROVISION_REVOKE_FAILED          = PREFIX + "00264";
  static final String PROVISION_MODIFY_FAILED          = PREFIX + "00265";
  static final String PROVISION_ENABLE_FAILED          = PREFIX + "00266";
  static final String PROVISION_DISABLE_FAILED         = PREFIX + "00267";

  // 00271 - 00280 request related errors
  static final String REQUEST_ASSIGN_FAILED            = PREFIX + "00271";
  static final String REQUEST_REVOKE_FAILED            = PREFIX + "00272";
  static final String REQUEST_MODIFY_FAILED            = PREFIX + "00273";
  static final String REQUEST_ENABLE_FAILED            = PREFIX + "00274";
  static final String REQUEST_DISABLE_FAILED           = PREFIX + "00275";
  static final String REQUEST_SUBMIT_FAILED            = PREFIX + "00276";

  // 00281 - 00290 request dataset related errors
  static final String REQUEST_DATASET_ONLYONCE         = PREFIX + "00281";
  static final String REQUEST_DATASET_EXISTS           = PREFIX + "00282";
  static final String REQUEST_DATASET_NOTEXISTS        = PREFIX + "00283";
  static final String REQUEST_DATASET_AMBIGUOS         = PREFIX + "00284";
  static final String REQUEST_DATASET_CREATE           = PREFIX + "00285";
  static final String REQUEST_DATASET_MODIFY           = PREFIX + "00286";
  static final String REQUEST_DATASET_DELETE           = PREFIX + "00287";

  // 00291 - 00300 request dataset related errors
  static final String APPLICATION_TYPE_MISSING         = PREFIX + "00291";
  static final String APPLICATION_PATH_NOT_EXIST       = PREFIX + "00292";
  static final String APPLICATION_MUST_BE_DIR          = PREFIX + "00293";
  static final String APPLICATION_COPY_FAILED          = PREFIX + "00294";
  static final String APPLICATION_TYPE_ONLY_ONCE       = PREFIX + "00294";
}