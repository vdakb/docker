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

    File        :   FeatureResourceBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureResourceBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common;

import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class FeatureResourceBundle
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  common
 **   <li>language code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FeatureResourceBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    // 00001 - 00010 cache utility related errors
    { FeatureError.CACHE_CATEGORY_ONLYONCE, "Cache category %1$s already assigned on the task" }

    // 00021 - 00060 object utility related errors
  , { FeatureError.OBJECT_REPOSITORY_NOTLOCKED,              "Cannot obtain exclusive lock to import data" }
  , { FeatureError.OBJECT_REPOSITORY_LOCKLOST,               "The exclusive lock to import data was lost during operation" }
  , { FeatureError.EXPORT_CATEGORY_MISSING,                  "At least one catagory has to be assigned to this task" }
  , { FeatureError.EXPORT_CATEGORY_ONLYONCE,                 "The Category [%1$s] to export %2$s already added to this task." }
  , { FeatureError.EXPORT_FILE_MANDATORY,                    "Specify at least one export -- a file or a resource collection." }
  , { FeatureError.EXPORT_FILE_ONLYONCE,                     "The Export file [%1$s] already added to this task." }
  , { FeatureError.EXPORT_FILE_ISDIRECTORY,                  "An export cannot be written to a directory." }
  , { FeatureError.EXPORT_FILE_NODIRECTORY,                  "The directory [%1$s] does not exists." }
  , { FeatureError.EXPORT_FILE_NOPERMISSION,                 "The export file [%1$s] is not writeable by the executing account." }
  , { FeatureError.EXPORT_FILE_DESCRIPTION,                  "A description must be provided for an export set." }
  , { FeatureError.IMPORT_FILE_CONSTRAINT,                   "A file must e specified to declare substitutions." }
  , { FeatureError.IMPORT_FILE_MANDATORY,                    "Specify at least one import -- a file or a resource collection." }
  , { FeatureError.IMPORT_FILE_ONLYONCE,                     "The Import file [%1$s] already added to this task." }
  , { FeatureError.IMPORT_FILE_NOTEXISTS,                    "The import file [%1$s] does not exists." }
  , { FeatureError.IMPORT_FILE_ISDIRECTORY,                  "Use a resource collection to import directories." }
  , { FeatureError.IMPORT_FILE_NOPERMISSION,                 "The import file %1$s is not readable by the executing account." }
  , { FeatureError.IMPORT_FILE_FETCH,                        "Error in fetching import file %1$s. Reason %2$s" }
  , { FeatureError.IMPORT_FILE_UNRESOLVED,                   "The Import file [%1$s] has missing dependencies:" }
  , { FeatureError.IMPORT_FILE_UNRESOLVED_OBJECT,            "   %1$s %2$s"}

    // 00051 - 00070 parameter related errors
  , { FeatureError.PARAMETER_NAME_EXCEPTION,                 "[%2$s]: %1$s" }

    // 00061 - 00080 library, resource bundle and plugin utility related errors
  , { FeatureError.UPLOAD_FILE_MANDATORY,                    "Specify at least one upload -- a file or a resource collection." }
  , { FeatureError.UPLOAD_FILE_NOTEXISTS,                    "The file [%1$s] does not exists." }
  , { FeatureError.UPLOAD_FILE_ISDIRECTORY,                  "Use a resource collection to upload directories." }
  , { FeatureError.UPLOAD_FILE_NOPERMISSION,                 "The file [%1$s] to upload is not readable by the executing account." }
  , { FeatureError.UPLOAD_FILE_ONLYONCE,                     "The file [%1$s] already added to this task." }
  , { FeatureError.UPLOAD_FILE_FETCH,                        "Error in fetching file [%1$s]. Reason %2$s" }
  , { FeatureError.UPLOAD_FILE_ERROR,                        "Error in upload of [%2$s]. Reason: %1$s" }
  , { FeatureError.DOWNLOAD_FILE_ERROR,                      "Error in download of [%2$s]. Reason: %1$s" }
  , { FeatureError.PLUGIN_FILE_NOTEXISTS,                    "The plugin ZIP-file [%1$s] does not exists." }
  , { FeatureError.PLUGIN_FILE_FETCH,                        "Error in fetching plugin archive [%1$s]. Reason %2$s" }
  , { FeatureError.PLUGIN_ONLY_ONCE,                         "The plugin class [%1$s] already added to this task." }
  , { FeatureError.PLUGIN_UPLOAD_ERROR,                      "Error in upload of [%2$s]. Reason: %1$s" }
  , { FeatureError.PLUGIN_DELETE_ERROR,                      "Error in delete of [%2$s]. Reason: %1$s" }
  , { FeatureError.PLUGIN_REGISTER_ERROR,                    "Error in registering the plugin. Reason: %1$s" }
  , { FeatureError.PLUGIN_UNREGISTER_ERROR,                  "Error in unregistering plugin. Reason: %1$s" }
  , { FeatureError.PLUGIN_CLASSNAME_ONLYONCE,                "The Class [%1$s] to unregister already added to this task." }

    // 00091 - 00110 composite deployment related errors
  , { FeatureError.COMPOSITE_FILE_MANDATORY,                 "Specify at least one composite archive -- a file or a resource collection." }
  , { FeatureError.COMPOSITE_FILE_ONLYONCE,                  "The Composite Archive file [%1$s] already added to this task." }
  , { FeatureError.COMPOSITE_FILE_ISDIRECTORY,               "Use a resource collection to import directories." }
  , { FeatureError.COMPOSITE_FILE_NOTEXISTS,                 "The Composite Archive file [%1$s] does not exists." }
  , { FeatureError.COMPOSITE_FILE_NOPERMISSION,              "The Composite Archive file %1$s is not readable by the executing account." }
  , { FeatureError.COMPOSITE_CONNECTION_ERROR,               "Error occured on server, check server log." }
  , { FeatureError.COMPOSITE_CONNECTION_TIMEOUT,             "Connection to the server timed out. Check server log for problems or increase the timeout value" }
  , { FeatureError.COMPOSITE_CONNECTION_REQUEST,             "Problem in sending HTTP request to the server. Check standard HTTP response code for %1$s" }
  , { FeatureError.COMPOSITE_DEPLOYMENT_ERROR,               "The Composite Archive file %1$s was not deployed: Reason %2$s." }
  , { FeatureError.COMPOSITE_UNDEPLOYMENT_ERROR,             "The Composite Archive file %1$s was not undeployed: Reason %2$s." }
  , { FeatureError.COMPOSITE_REDEPLOYMENT_ERROR,             "The Composite Archive file %1$s was not redeployed: Reason %2$s." }

    // 00111 - 00130 composite configuration related errors
  , { FeatureError.COMPOSITE_MANAGEMENT_ERROR,               "Composite Management Error: %1$s" }
  , { FeatureError.COMPOSITE_WORKFLOW_ERROR,                 "Composite Workflow Error: %1$s" }
  , { FeatureError.COMPOSITE_LOCATOR_ERROR,                  "The Locator could not be created for environment: Reason %1$s\n%2$s" }
  , { FeatureError.COMPOSITE_LOOKUP_ERROR,                   "The lookup of composite [%1$s] failed: Reason %2$s" }
  , { FeatureError.COMPOSITE_PARTITION_ONLYONCE,             "The partition [%1$s] already added to this task." }
  , { FeatureError.COMPOSITE_PARTITION_CONNECT,              "Initialize connection to SOA folder manager failed. Reason: %1$s" }
  , { FeatureError.COMPOSITE_PARTITION_DISCONNECT,           "Closing connection to SOA folder manager failed. Reason: %1$s" }
  , { FeatureError.COMPOSITE_PARTITION_EXISTS,               "The partition [%1$s] already exists in the system." }
  , { FeatureError.COMPOSITE_PARTITION_NOTEXISTS,            "The partition [%1$s] does not exists" }
  , { FeatureError.COMPOSITE_PARTITION_CREATE,               "The partition [%1$s] was not created: Reason %2$s." }
  , { FeatureError.COMPOSITE_PARTITION_DELETE,               "The partition [%1$s] was not deleted: Reason %2$s." }
  , { FeatureError.COMPOSITE_PARTITION_START,                "Starting all composites in partition [%1$s] failed: Reason %2$s." }
  , { FeatureError.COMPOSITE_PARTITION_STOP,                 "Stopping all composites in partition [%1$s] failed: Reason %2$s." }
  , { FeatureError.COMPOSITE_PARTITION_ACTIVATE,             "Activating all composites in partition [%1$s] failed: Reason %2$s." }
  , { FeatureError.COMPOSITE_PARTITION_RETIRE,               "Retirering all composites in partition [%1$s] failed: Reason %2$s." }
  , { FeatureError.COMPOSITE_PARTITION_LOOKUP,               "The lookup of partition [%1$s] failed: Reason %2$s" }
  , { FeatureError.COMPOSITE_WORKFLOW_NOTEXISTS,             "The composite with dn [%1$s] does not exists" }
  , { FeatureError.COMPOSITE_HUMANTASK_MISSING,              "The composite task flows for dn [%1$s] is empty" }
  , { FeatureError.COMPOSITE_HUMANTASK_CONFIGURED,           "The task flows for composite dn [%1$s] already configured. Specify enforce on the service task to override" }

    // 00131 - 00150 workflow registration related errors
  , { FeatureError.WORKFLOW_RESOURCE_MANDATORY,              "Specify at least one Resource -- a file or a resource collection." }
  , { FeatureError.WORKFLOW_RESOURCE_ONLYONCE,               "The Resource [%1$s] already added to this task." }
  , { FeatureError.WORKFLOW_DATASETFILE_MANDATORY,           "Specify at least one Request DataSet -- a file or a resource collection." }
  , { FeatureError.WORKFLOW_DATASETFILE_ONLYONCE,            "The Request DataSet file [%1$s] already added to this task." }
  , { FeatureError.WORKFLOW_DATASETFILE_DIRECTORY,           "Request DataSet's are always written to a directory." }
  , { FeatureError.WORKFLOW_PROCESS_MANDATORY,               "Specify at least one Process -- a file or a resource collection." }
  , { FeatureError.WORKFLOW_PROCESS_ONLYONCE,                "The provisioning process [%1$s] already added to this resource." }
  , { FeatureError.WORKFLOW_OPERATION_MANDATORY,             "Specify at least one operation." }
  , { FeatureError.WORKFLOW_OPERATION_ONLYONCE,              "The operation [%1$s] already added to this process." }
  , { FeatureError.WORKFLOW_ATTRIBUTE_ONLYONCE,              "The attribute [%1$s] already added to this %2$s." }
  , { FeatureError.WORKFLOW_REFERENCE_ONLYONCE,              "The attribute reference %1$s already added to this %2$s." }
  , { FeatureError.WORKFLOW_LOOKUPVALUE_ONLYONCE,            "The lookup value [%1$s] already added to this attribute reference." }
  , { FeatureError.WORKFLOW_PROCESSFORM_ONLYONCE,            "The process form [%1$s] already added to this process." }
  , { FeatureError.WORKFLOW_PROCESSFIELD_ONLYONCE,           "The process form field [%1$s] already added to this form." }
  , { FeatureError.WORKFLOW_FIELDPROPERTY_ONLYONCE,          "The property [%1$s] already added to this field." }
  , { FeatureError.WORKFLOW_REGISTER_ERROR,                  "Workflow [%1$s] of category [%2$s] not registered." }
  , { FeatureError.WORKFLOW_ENABLE_ERROR,                    "Workflow [%1$s] of category [%2$s] not enabled." }
  , { FeatureError.WORKFLOW_DISABLE_ERROR,                   "Workflow [%1$s] of category [%2$s] not disabled." }

    // 00151 - 00160 regular expression related errors
  , { FeatureError.EXPRESSION_UNDEFINED_BITVALUES,           "Undefined bit values are set in the regular expression compile options" }
  , { FeatureError.EXPRESSION_INVALID,                       "An error is contained in the regular expression [%1$s]: %2$s" }

    // 00161 - 00170 metadata namespace related errors
  , { FeatureError.NAMESPACE_PATH_MANDATORY,                 "Specify at least one namespace path -- a file or a resource collection." }
  , { FeatureError.NAMESPACE_PATH_ONLYONCE,                  "The namespace [%1$s] already added to this task." }

    // 00171 - 00180 metadata connection related errors
  , { FeatureError.METADATA_INSTANCE_ERROR,                  "Instance to metadata store not established." }
  , { FeatureError.METADATA_INSTANCE_CREATE,                 "Create instance to metadata store failed. Reason: %1$s" }
  , { FeatureError.METADATA_INSTANCE_CLOSE,                  "Closing instance to metadata store failed. Reason: %1$s" }
  , { FeatureError.METADATA_SESSION_CREATE,                  "Create session to metadata store failed. Reason: %1$s" }
  , { FeatureError.METADATA_SESSION_COMMIT,                  "Error occurred while commiting changes to MDS. Reason: %1$s" }
  , { FeatureError.METADATA_CHANGES_FLUSH,                   "Error occurred while flushing changes to MDS. Reason: %1$s" }

    // 00181 - 00190 metadata document related errors
  , { FeatureError.METADATA_DOCUMENT_ONLYONCE,               "The Metadata Object [%1$s] already added to this task." }
  , { FeatureError.METADATA_DOCUMENT_CREATE,                 "Error occurred while creating the Metadata Object %1$s. Reason: %2$s" }
  , { FeatureError.METADATA_DOCUMENT_TRANSFORMATION,         "Error occurred while transforming the Metadata Object %1$s. Reason: %2$s" }
  , { FeatureError.METADATA_DOCUMENT_REFERENCE,              "Error occurred while accessing the Metadata Object %1$s. Reason: %2$s" }
  , { FeatureError.METADATA_DOCUMENT_READONLY,               "Metadata Object [%1$s] in path [%2$s] is read only" }
  , { FeatureError.METADATA_DOCUMENT_UPDATE,                 "%1$s: Error occurred while updating the Metadata Object %2$s. Reason: %3$s" }
  , { FeatureError.METADATA_DOCUMENT_UPLOAD,                 "%1$s: Error occurred while uploading the Metadata Object %2$s. Reason: %3$s" }
  , { FeatureError.METADATA_DOCUMENT_DOWNLOAD,               "%1$s: Error occurred while downloading the Metadata Object %2$s. Reason: %3$s" }

    // 00191 - 00200 job trigger related errors
  , { FeatureError.JOB_SCHEDULE_NOT_SUPPORTED,               "Scheduler type [%1$s] does not support setting of attribute %2$s" }
  , { FeatureError.JOB_INSTANCE_OPERATION_FAILED,            "%1$s of Job Instance %2$s failed"}

    // 00201 - 00210 resource object related errors
  , { FeatureError.RESOURCE_OBJECT_ONLYONCE,                 "The Resource Object [%1$s] already added to this task." }
  , { FeatureError.RESOURCE_OBJECT_EXISTS,                   "The Resource Object [%1$s] already exists." }
  , { FeatureError.RESOURCE_OBJECT_NOTEXISTS,                "The Resource Object [%1$s] does not exists." }
  , { FeatureError.RESOURCE_OBJECT_AMBIGUOS,                 "The Resource Object [%1$s] is defined ambiguosly." }
  , { FeatureError.RESOURCE_OBJECT_CREATE,                   "The Resource Object [%1$s] was not created: Reason %2$s." }
  , { FeatureError.RESOURCE_OBJECT_MODIFY,                   "The Resource Object [%1$s] was not modified: Reason %2$s." }
  , { FeatureError.RESOURCE_OBJECT_DELETE,                   "The Resource Object [%1$s] was not deleted: Reason %2$s." }

    // 00211 - 00220 process definition related errors
  , { FeatureError.PROCESS_DEFINITION_ONLYONCE,              "The Process Definition [%1$s] already added to this task." }
  , { FeatureError.PROCESS_DEFINITION_EXISTS,                "The Process Definition [%1$s] already exists." }
  , { FeatureError.PROCESS_DEFINITION_NOTEXISTS,             "The Process Definition [%1$s] does not exists." }
  , { FeatureError.PROCESS_DEFINITION_AMBIGUOS,              "The Process Definition [%1$s] is defined ambiguosly." }
  , { FeatureError.PROCESS_DEFINITION_CREATE,                "The Process Definition [%1$s] was not created: Reason %2$s." }
  , { FeatureError.PROCESS_DEFINITION_MODIFY,                "The Process Definition [%1$s] was not modified: Reason %2$s." }
  , { FeatureError.PROCESS_DEFINITION_DELETE,                "The Process Definition [%1$s] was not deleted: Reason %2$s." }

    // 00221 - 00230 process form related errors
  , { FeatureError.PROCESS_FORM_ONLYONCE,                    "The Process Form [%1$s] already added to this task." }
  , { FeatureError.PROCESS_FORM_EXISTS,                      "The Process Form [%1$s] already exists." }
  , { FeatureError.PROCESS_FORM_NOTEXISTS,                   "The Process Form [%1$s] does not exists." }
  , { FeatureError.PROCESS_FORM_CREATE,                      "The Process Form [%1$s] was not created: Reason %2$s." }
  , { FeatureError.PROCESS_FORM_MODIFY,                      "The Process Form [%1$s] was not modified: Reason %2$s." }
  , { FeatureError.PROCESS_FORM_DELETE,                      "The Process Form [%1$s] was not deleted: Reason %2$s." }

    // 00231 - 00250 sandbox management related errors
  , { FeatureError.SANDBOX_MANDATORY,                        "Specify at least one sandbox -- a name or a resource collection." }
  , { FeatureError.SANDBOX_FILE_MANDATORY,                   "Specify Sandbox Archive file." }
  , { FeatureError.SANDBOX_FILE_ISDIRECTORY,                 "A Sandbox Archive cannot be a directory." }
  , { FeatureError.SANDBOX_FILE_NOTEXISTS,                   "The Sandbox Archive file %1$s does not exists." }
  , { FeatureError.SANDBOX_FILE_NOPERMISSION,                "The Sandbox Archive file %1$s is not readable by the executing account." }
  , { FeatureError.SANDBOX_FILE_ONLYONCE,                    "The Sandbox Archive file [%1$s] already added to this task." }
  , { FeatureError.SANDBOX_NAME_ONLYONCE,                    "The Sandbox [%1$s] already added to this task." }
  , { FeatureError.SANDBOX_NAME_INVALID,                     "The name [%1$s] is invalid for a Sandbox." }
  , { FeatureError.SANDBOX_EXISTS,                           "Sandbox [%1$s] already exists" }
  , { FeatureError.SANDBOX_NOTEXISTS,                        "Sandbox [%1$s] does not exists" }

    // 00241 - 00260 sandbox marshalling related errors
  , { FeatureError.SANDBOX_MODULE_REQUIRED,                  "Either a path or a connection to a metadata service is required to manage the Catalog Application Module." }
  , { FeatureError.SANDBOX_MODULE_ONLYONCE,                  "Only a path or a connection to a metadata service is allowed to manage the Catalog Application Module." }
  , { FeatureError.SANDBOX_BUNDLE_ONLYONCE,                  "The Resource Bundle [%1$s] already added to this sandbox." }
  , { FeatureError.SANDBOX_MARSHALL_FAILED,                  "Marshalling Sandbox [%1$s] to [%2$s] failed."}
  , { FeatureError.SANDBOX_MARSHALL_STOPPED,                 "Marshalling Sandbox [%1$s] to [%2$s] stopped."}
  , { FeatureError.SANDBOX_DIRECTORY_EXISTS,                 "Sandbox working directory [%1$s] already exists" }
  , { FeatureError.SANDBOX_DIRECTORY_ISFILE,                 "A Sandbox working directory cannot be a file." }
  , { FeatureError.SANDBOX_DIRECTORY_DELETE,                 "Sandbox path [%1$s] could not be deleted."}
  , { FeatureError.SANDBOX_DIRECTORY_MEMBER,                 "Could not visit file [%1$s] while creating a file list from file tree: Reason %2$s." }
  , { FeatureError.SANDBOX_DIRECTORY_COMPRESS,               "Sandbox [%1$s] could not be compressed: Reason %2$s."}

    // 00251 - 00260 entity publication related errors
  , { FeatureError.PUBLICATION_ASSIGN_FAILED,                "Entity Publication assigment of %1$s [%2$s] to %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.PUBLICATION_REVOKE_FAILED,                "Entity Publication revocation of %1$s [%2$s] from %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.SCOPERULE_ASSIGN_FAILED,                  "Admin Scope Rule assigment of %1$s [%2$s] to %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.SCOPERULE_REVOKE_FAILED,                  "Admin Scope Rule revocation of %1$s [%2$s] from %3$s [%4$s] failed. Reason: %5$s"}

    // 00261 - 00270 provisioning related errors
  , { FeatureError.ACCOUNT_EXISTS,                           "Primary account for %1$s [%2$s] already provisioned to %3$s [%4$s]"}
  , { FeatureError.ACCOUNT_NOTEXISTS,                        "Primary account for %1$s [%2$s] not provisioned to %3$s [%4$s]"}
  , { FeatureError.PROVISION_ASSIGN_FAILED,                  "Provision of %1$s [%2$s] to %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.PROVISION_REVOKE_FAILED,                  "Revoke of %1$s [%2$s] from %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.PROVISION_MODIFY_FAILED,                  "Modify of %1$s [%2$s] for %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.PROVISION_ENABLE_FAILED,                  "Enable of %1$s [%2$s] for %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.PROVISION_DISABLE_FAILED,                 "Disable of %1$s [%2$s] for %3$s [%4$s] failed. Reason: %5$s"}

    // 00271 - 00280 request related errors
  , { FeatureError.REQUEST_ASSIGN_FAILED,                    "Assigment request of %1$s [%2$s] to %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.REQUEST_REVOKE_FAILED,                    "Revocation request of %1$s [%2$s] from %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.REQUEST_MODIFY_FAILED,                    "Modification request of %1$s [%2$s] for %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.REQUEST_ENABLE_FAILED,                    "Enablement request of %1$s [%2$s] for %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.REQUEST_DISABLE_FAILED,                   "Disablement request of %1$s [%2$s] for %3$s [%4$s] failed. Reason: %5$s"}
  , { FeatureError.REQUEST_SUBMIT_FAILED,                    "Submitting request of %1$s [%2$s] for %3$s [%4$s] failed. Reason: %5$s"}

    // 00281 - 00290 request dataset related errors
  , { FeatureError.REQUEST_DATASET_ONLYONCE,                 "The Request DataSet [%1$s] already added to this task." }
  , { FeatureError.REQUEST_DATASET_EXISTS,                   "The Request DataSet already exists for Resource Object [%1$s]." }
  , { FeatureError.REQUEST_DATASET_NOTEXISTS,                "The Request DataSet does not exists for Resource Object [%1$s]." }
  , { FeatureError.REQUEST_DATASET_AMBIGUOS,                 "The Request DataSet is defined ambiguosly for Resource Object [%1$s]." }
  , { FeatureError.REQUEST_DATASET_CREATE,                   "The Request DataSet was not created for Resource Object [%1$s]: Reason %2$s." }
  , { FeatureError.REQUEST_DATASET_MODIFY,                   "The Request DataSet was not modified for Resource Object [%1$s]: Reason %2$s." }
  , { FeatureError.REQUEST_DATASET_DELETE,                   "The Request DataSet was not deleted for Resource Object [%1$s]: Reason %2$s." }

    // 00291 - 00300 application template related errors
  , { FeatureError.APPLICATION_TYPE_MISSING,                 "Type definition [%1$s] is missing." }
  , { FeatureError.APPLICATION_PATH_NOT_EXIST,               "The path [%1$s] does not exist." }
  , { FeatureError.APPLICATION_MUST_BE_DIR,                  "The path [%1$s] must be a directory." }
  , { FeatureError.APPLICATION_COPY_FAILED,                  "Creating resources has failed. Reason %1$s." }
  , { FeatureError.APPLICATION_TYPE_ONLY_ONCE,               "Type definition [%1$s] is allowed only once per operation." }

    // 01031 - 01060 cache utility related messages
  , { FeatureMessage.CACHE_CATEGORY_MISSING,                 "No cache category assigned; assuming all" }
  , { FeatureMessage.CACHE_CATEGORY_PROCEED,                 "Purging cache for categories %1$s succeeded" }
  , { FeatureMessage.CACHE_CATEGORY_FAILED,                  "Purging cache for categories %1$s failed" }

    // 01051 - 01060 operation related messages
  , { FeatureMessage.OPERATION_INVOKE,                       "Invoking operation %1$s"}
  , { FeatureMessage.OPERATION_INVOKE_SUCCESS,               "Operation %1$s completed successful"}
  , { FeatureMessage.OPERATION_INVOKE_ERROR,                 "Operation %1$s completed with error"}

    // 01061 - 01070 export assembly related messages
  , { FeatureMessage.ASSEMBLY_CREATE,                        "Creating assembly %1$s ..."}
  , { FeatureMessage.ASSEMBLY_SUCCESS,                       "Assembly %1$s created successful"}
  , { FeatureMessage.ASSEMBLY_ERROR,                         "Assembly %1$s completed with errors"}
  , { FeatureMessage.DEPENDENCY_CREATE,                      "Creating dependencies of assembly %1$s ..."}
  , { FeatureMessage.DEPENDENCY_SUCCESS,                     "Dependencies of assembly %1$s created successful"}
  , { FeatureMessage.DEPENDENCY_ERROR,                       "Dependencies of assembly %1$s completed with errors"}
  , { FeatureMessage.EXPORT_CATEGORY_SEARCH,                 "...Searched for %2$s %1$s and found:\t %3$s"}
  , { FeatureMessage.EXPORT_ASSEMBLY_EMPTY,                  "Assembly %1$s contains no result. Skip further processing and do not override extising files"}
  , { FeatureMessage.IMPORT_ASSEMBLY_EMPTY,                  "Assembly %1$s contains no result. Skip further processing"}
  , { FeatureMessage.IMPORT_ASSEMBLY_EMPTY,                  "Assembly %1$s contains no result. Skip further processing"}

    // 01081 - 01090 import compilation related messages
  , { FeatureMessage.IMPORT_OPERATION_START,                 "Starting Object Import for %1$s ..."}
  , { FeatureMessage.IMPORT_OPERATION_SUCCESS,               "Object Import for %1$s completed successful"}
  , { FeatureMessage.IMPORT_OPERATION_ERROR,                 "Object Import for %1$s completed with errors"}

    // 01091 - 01110 java archive, resource bundle and plugin related messages
  , { FeatureMessage.UPLOAD_FILE_FETCH,                      "Fetching file [%1$s]"}
  , { FeatureMessage.UPLOAD_FILE_FETCHED,                    "File [%1$s] fetched. Allocated buffer size %2$s"}
  , { FeatureMessage.UPLOAD_FILE_BEGIN,                      "Uploading %1$s started ..."}
  , { FeatureMessage.UPLOAD_FILE_COMPLETE,                   "Upload of %1$s completed."}
  , { FeatureMessage.DOWNLOAD_FILE_STORE,                    "Storing file [%1$s]"}
  , { FeatureMessage.DOWNLOAD_FILE_STORED,                   "File [%1$s] stored. Allocated file size %2$s"}
  , { FeatureMessage.PLUGIN_UPLOAD_BEGIN,                    "Upload of %1$s %2$s started ..."}
  , { FeatureMessage.PLUGIN_UPLOAD_COMPLETE,                 "Upload of %1$s %2$s completed."}
  , { FeatureMessage.PLUGIN_DELETE_BEGIN,                    "Delete of %1$s %2$s started ..."}
  , { FeatureMessage.PLUGIN_DELETE_COMPLETE,                 "Delete of %1$s %2$s completed."}
  , { FeatureMessage.PLUGIN_REGISTERED,                      "Plugin %1$s registered"}
  , { FeatureMessage.PLUGIN_UNREGISTRED,                     "Plugin %1$s unregistered"}

    // 01141 - 01150 request dataset documentation related messages
  , { FeatureMessage.REQUEST_DATASET_CREATE,                 "Generating Request DataSet [%1$s] for [%2$s] operation %3$s ..."}
  , { FeatureMessage.REQUEST_DATASET_SUCCESS,                "Request DataSet [%1$s] for [%2$s] operation %3$s generated successful"}
  , { FeatureMessage.REQUEST_DATASET_ERROR,                  "Request DataSet [%1$s] for [%2$s] operation %3$s completed with errors"}
  , { FeatureMessage.REQUEST_DATASET_SKIPPED,                "Generation of Request DataSet [%1$s] for [%2$s] skipped due to user request"}
  , { FeatureMessage.REQUEST_DATASET_CONFIRMATION_TITLE,     "Write Request DataSet %1$s"}
  , { FeatureMessage.REQUEST_DATASET_CONFIRMATION_MESSAGE,   "DataSet File %1$s already exists in\n%2$s.\n\nOverride the existing file?"}

    // 01151 - 01190 scheduled job related messages
  , { FeatureMessage.JOB_STATUS_0,                           "Status 0 ???"}
  , { FeatureMessage.JOB_STATUS_1,                           "Status 1 ???"}
  , { FeatureMessage.JOB_STATUS_STOPPED,                     "Stopped"}
  , { FeatureMessage.JOB_STATUS_3,                           "Status 3 ???"}
  , { FeatureMessage.JOB_STATUS_4,                           "Status 4 ???"}
  , { FeatureMessage.JOB_STATUS_RUNNING,                     "Running"}
  , { FeatureMessage.JOB_STATUS_6,                           "Status 6 ???"}
  , { FeatureMessage.JOB_STATUS_INTERRUPTED,                 "Interrupted"}
  , { FeatureMessage.JOB_INSTANCE_SEARCH,                    "Search for Job Instance [%1$s] ..."}
  , { FeatureMessage.JOB_INSTANCE_FOUND,                     "Found Job Instance [%1$s]"}
  , { FeatureMessage.JOB_INSTANCE_OPERATION_CREATE,          "Create"}
  , { FeatureMessage.JOB_INSTANCE_OPERATION_UPDATE,          "Update"}
  , { FeatureMessage.JOB_INSTANCE_OPERATION_DELETE,          "Delete"}
  , { FeatureMessage.JOB_INSTANCE_OPERATION_EXECUTE,         "Execute"}
  , { FeatureMessage.JOB_INSTANCE_OPERATION_HISTORY,         "History"}
  , { FeatureMessage.JOB_INSTANCE_OPERATION_BEGIN,           "%1$s Job Instance [%2$s] ..."}
  , { FeatureMessage.JOB_INSTANCE_OPERATION_SUCCESS,         "%1$s of Job Instance [%2$s] completed"}
  , { FeatureMessage.JOB_INSTANCE_OPERATION_FAILED ,         "%1$s of Job Instance [%2$s] failed with [%3$s]"}
  , { FeatureMessage.JOB_INSTANCE_OBSERVATION_STARTED,       "Observation of Job Instance [%1$s] started"}
  , { FeatureMessage.JOB_INSTANCE_OBSERVATION_FINISHED,      "Observation of Job Instance [%1$s] finished"}
  , { FeatureMessage.JOB_INSTANCE_COMMAND_START,             "Start"}
  , { FeatureMessage.JOB_INSTANCE_COMMAND_STOP,              "Stop"}
  , { FeatureMessage.JOB_INSTANCE_COMMAND_ENABLE,            "Enabling"}
  , { FeatureMessage.JOB_INSTANCE_COMMAND_DISABLE,           "Disabling"}
  , { FeatureMessage.JOB_INSTANCE_COMMAND_BEGIN,             "%1$s of Job Instance [%2$s] ..."}
  , { FeatureMessage.JOB_INSTANCE_COMMAND_SUCCESS,           "%1$s of Job Instance [%2$s] completed"}
  , { FeatureMessage.JOB_INSTANCE_COMMAND_FAILED,            "%1$s of Job Instance [%2$s] failed"}
  , { FeatureMessage.JOB_INSTANCE_STATUS_CHECK,              "Checking status of Job %1$s" }
  , { FeatureMessage.JOB_INSTANCE_STATUS_RESULT,             "Status of Job [%1$s] is %2$s" }
  , { FeatureMessage.JOB_INSTANCE_STATUS_UPDATE,             "Updating Status of Job [%1$s] to %2$s ..."}
  , { FeatureMessage.JOB_INSTANCE_STATUS_UPDATED,            "Status of Job [%1$s] updated to %2$s"}
  , { FeatureMessage.JOB_INSTANCE_PARAMETER_UPDATE,          "Updating Parameter of Job [%1$s] ..."}
  , { FeatureMessage.JOB_INSTANCE_PARAMETER_UPDATED,         "Parameter of Job [%1$s] updated"}
  , { FeatureMessage.JOB_INSTANCE_SCHEDULE_UPDATE,           "Updating Schedule of Job [%1$s] ..."}
  , { FeatureMessage.JOB_INSTANCE_SCHEDULE_UPDATED,          "Schedule of Job [%1$s] updated"}

    // 01191 - 01220 process form related messages
  , { FeatureMessage.PROCESS_FORM_CREATE,                    "Creating Process Form [%1$s] ..."}
  , { FeatureMessage.PROCESS_FORM_CREATED,                   "Process Form [%1$s] created"}
  , { FeatureMessage.PROCESS_FORM_MODIFY,                    "Modifying Process Form [%1$s] ..."}
  , { FeatureMessage.PROCESS_FORM_MODIFIED,                  "Process Form [%1$s] modified"}

    // 01121 - 01250 composite deploymnet related messages
  , { FeatureMessage.COMPOSITE_DEPLOY,                       "About to deploy Composite Archive %1$s ..."}
  , { FeatureMessage.COMPOSITE_DEPLOYED,                     "Composite Archive file %1$s successful deployed"}
  , { FeatureMessage.COMPOSITE_UNDEPLOY,                     "About to undeploy Composite %1$s ..."}
  , { FeatureMessage.COMPOSITE_UNDEPLOYED,                   "Composite %1$s successful undeployed"}
  , { FeatureMessage.COMPOSITE_REDEPLOY,                     "About to redeploy Composite Archive file %1$s ..."}
  , { FeatureMessage.COMPOSITE_REDEPLOYED,                   "Composite Archive file %1$s successful redeployed"}
  , { FeatureMessage.COMPOSITE_CONFIGURE,                    "About to configure Composite [%1$s] ..."}
  , { FeatureMessage.COMPOSITE_CONFIGURED,                   "Composite [%1$s] successful configured"}
  , { FeatureMessage.COMPOSITE_EXISTS,                       "Composite [%1$s] already deployed in the system. Performing redeploy only on object" }
  , { FeatureMessage.PARTITION_CREATE,                       "About to create partition %1$s ..."}
  , { FeatureMessage.PARTITION_CREATED,                      "Partition %1$s successful created"}
  , { FeatureMessage.PARTITION_DELETE,                       "About to delete partition %1$s ..."}
  , { FeatureMessage.PARTITION_DELETED,                      "Partition %1$s successful deleted"}
  , { FeatureMessage.PARTITION_START,                        "About to start composites in partition %1$s ..."}
  , { FeatureMessage.PARTITION_STARTED,                      "Composites in partition %1$s successful started"}
  , { FeatureMessage.PARTITION_STOP,                         "About to stop composites in partition %1$s ..."}
  , { FeatureMessage.PARTITION_STOPPED,                      "Composites in partition %1$s successful stopped"}
  , { FeatureMessage.PARTITION_ACTIVATE,                     "About to activate all composites in partition %1$s ..."}
  , { FeatureMessage.PARTITION_ACTIVATED,                    "All composites in partition %1$s successful activated"}
  , { FeatureMessage.PARTITION_RETIRE,                       "About to retire all composites in partition %1$s ..."}
  , { FeatureMessage.PARTITION_RETIRED,                      "All composites in partition %1$s successful retiredd"}

    // 01251 - 01270 workflow registration related messages
  , { FeatureMessage.WORKFLOW_INVENTORY_SEARCH,              "Search for Workflows of Category %1$s with Pattern [%2$s] and found:"}
  , { FeatureMessage.WORKFLOW_INVENTORY_EMPTY,               "No match found"}
  , { FeatureMessage.WORKFLOW_INVENTORY_SEPARATOR,           "--------------------------------------------------+-----------+---------------------+----------"}
  , { FeatureMessage.WORKFLOW_INVENTORY_NAME,                "Workflow"}
  , { FeatureMessage.WORKFLOW_INVENTORY_STATUS,              "Enabled"}
  , { FeatureMessage.WORKFLOW_INVENTORY_CATEGORY,            "Category"}
  , { FeatureMessage.WORKFLOW_INVENTORY_PROVIDER,            "Provider"}
  , { FeatureMessage.WORKFLOW_INVENTORY_REGISTER,            "About to register Workflow %1$s of category %2$s ..."}
  , { FeatureMessage.WORKFLOW_INVENTORY_REGISTERED,          "Workflow %1$s of category %2$s registered"}
  , { FeatureMessage.WORKFLOW_INVENTORY_ENABLE,              "About to enable Workflow %1$s of category %2$s ..."}
  , { FeatureMessage.WORKFLOW_INVENTORY_ENABLED,             "Workflow %1$s of category %2$s enabled"}
  , { FeatureMessage.WORKFLOW_INVENTORY_DISABLE,             "About to disable Workflow %1$s of category %2$s ..."}
  , { FeatureMessage.WORKFLOW_INVENTORY_DISABLED,            "Workflow %1$s of category %2$s disabled"}

    // 01271 - 01280 metadata store management related messages
  , { FeatureMessage.METADATA_CHANGES_FLUSH,                 "Flush Metadata Store changes ..."}
  , { FeatureMessage.METADATA_CHANGES_FLUSHED,               "Metadata Store changes flushed"}

    // 01281 - 01310 sandbox management related messages
  , { FeatureMessage.SANDBOX_EXPORT,                         "Exporting Sandbox [%1$s] ..."}
  , { FeatureMessage.SANDBOX_EXPORTED,                       "Sandbox [%1$s] exported"}
  , { FeatureMessage.SANDBOX_EXPORT_EMPTY,                   "Sandbox %1$s contains no changes. Skip further processing and do not override extising files"}
  , { FeatureMessage.SANDBOX_IMPORT,                         "Importing Sandbox [%1$s] ..."}
  , { FeatureMessage.SANDBOX_IMPORTED,                       "Sandbox [%1$s] imported"}
  , { FeatureMessage.SANDBOX_PUBLISH,                        "Publishing Sandbox [%1$s] ..."}
  , { FeatureMessage.SANDBOX_PUBLISHED,                      "Sandbox [%1$s] published"}
  , { FeatureMessage.SANDBOX_PUBLISH_EMPTY,                  "Sandbox %1$s contains no changes. Skip further processing and do not publish anything"}
  , { FeatureMessage.SANDBOX_COMMIT,                         "Commiting Sandbox [%1$s] ..."}
  , { FeatureMessage.SANDBOX_COMMITED,                       "Sandbox [%1$s] committed"}
  , { FeatureMessage.SANDBOX_ROLLBACK,                       "Rolling back Sandbox [%1$s] ..."}
  , { FeatureMessage.SANDBOX_ROLLEDBACK,                     "Sandbox [%1$s] rolled back"}
  , { FeatureMessage.SANDBOX_CREATE,                         "Creating Sandbox [%1$s] ..."}
  , { FeatureMessage.SANDBOX_CREATED,                        "Sandbox [%1$s] created"}
  , { FeatureMessage.SANDBOX_DELETE,                         "Deleting Sandbox [%1$s] ..."}
  , { FeatureMessage.SANDBOX_DELETED,                        "Sandbox [%1$s] deleted"}
  , { FeatureMessage.SANDBOX_MARSHALL,                       "Marshalling Sandbox [%1$s] to [%2$s] ..."}
  , { FeatureMessage.SANDBOX_MARSHALLED,                     "Sandbox [%1$s] marshalled to [%2$s] ..."}
  , { FeatureMessage.SANDBOX_MARSHALL_POPULATE,              "Populating model for Sandbox [%1$s] ..."}
  , { FeatureMessage.SANDBOX_MARSHALL_POPULATED,             "Model for Sandbox [%1$s] populated"}
  , { FeatureMessage.SANDBOX_MARSHALL_MODULE,                "Marshalling Application Module Customization for Sandbox [%2$s] to [%1$s] ..."}
  , { FeatureMessage.SANDBOX_MARSHALL_BACKEND,               "Marshalling Backend Customization for Sandbox [%2$s] to [%1$s] ..."}
  , { FeatureMessage.SANDBOX_MARSHALL_FRONTEND,              "Marshalling Frontend Customization for Sandbox [%2$s] to [%1$s] ..."}
  , { FeatureMessage.SANDBOX_MARSHALL_TASKFLOW,              "Marshalling Taslkflow Customization for Sandbox [%2$s] to [%1$s] ..."}
  , { FeatureMessage.SANDBOX_DIRECTORY_TITLE,                "Marshalling Sandbox %1$s"}
  , { FeatureMessage.SANDBOX_DIRECTORY_MESSAGE,              "Sandbox Directory %1$s already exists in\n%2$s.\n\nDelete all of the existing file?"}
  , { FeatureMessage.SANDBOX_DATASET_TITLE,                  "Generating Request DataSet %1$s"}
  , { FeatureMessage.SANDBOX_DATASET_MESSAGE,                "Request DataSet %1$s already exists.\n\nOverride the existing file?"}
  , { FeatureMessage.SANDBOX_DATASET_CREATE,                 "Creating Request DataSet [%1$s] ..."}
  , { FeatureMessage.SANDBOX_DATASET_CREATED,                "Request DataSet [%1$s] created"}
  , { FeatureMessage.SANDBOX_DATASET_MODIFY,                 "Modifying Request DataSet [%1$s] ..."}
  , { FeatureMessage.SANDBOX_DATASET_MODIFIED,               "Request DataSet [%1$s] modified"}

    // 01321 - 01330 entity publication related messages
  , { FeatureMessage.PUBLICATION_ASSIGN_BEGIN,                "Assigning Entity Publication for %1$s [%2$s] to %3$s [%4$s] ..."}
  , { FeatureMessage.PUBLICATION_ASSIGN_SUCCESS,              "Entity Publication for %1$s [%2$s] to %3$s [%4$s] assigned"}
  , { FeatureMessage.PUBLICATION_REVOKE_BEGIN,                "Revoking Entity Publication for %1$s [%2$s] from %3$s [%4$s] ..."}
  , { FeatureMessage.PUBLICATION_REVOKE_SUCCESS,              "Entity Publication for %1$s [%2$s] from %3$s [%4$s] revoked"}

    // 01341 - 01360 provisioning related messages
  , { FeatureMessage.PROVISION_ASSIGN_BEGIN,                  "Provision %1$s [%2$s] to %3$s [%4$s] ..."}
  , { FeatureMessage.PROVISION_ASSIGN_SUCCESS,                "%1$s [%2$s] for %3$s [%4$s] provisioned"}
  , { FeatureMessage.PROVISION_ASSIGN_SKIPPED,                "Provision %1$s [%2$s] for %3$s [%4$s] skipped"}
  , { FeatureMessage.PROVISION_REVOKE_BEGIN,                  "Revoking %1$s [%2$s] from %3$s [%4$s] ..."}
  , { FeatureMessage.PROVISION_REVOKE_SUCCESS,                "%1$s [%2$s] for %3$s [%4$s] revoked"}
  , { FeatureMessage.PROVISION_REVOKE_SKIPPED,                "Revoke %1$s [%2$s] for %3$s [%4$s] skipped"}
  , { FeatureMessage.PROVISION_MODIFY_BEGIN,                  "Modifying %1$s [%2$s] for %3$s [%4$s] ..."}
  , { FeatureMessage.PROVISION_MODIFY_SUCCESS,                "%1$s [%2$s] for %3$s [%4$s] modified"}
  , { FeatureMessage.PROVISION_MODIFY_SKIPPED,                "Modifying %1$s [%2$s] for %3$s [%4$s] skipped"}
  , { FeatureMessage.PROVISION_ENABLE_BEGIN,                  "Enable %1$s [%2$s] for %3$s [%4$s] ..."}
  , { FeatureMessage.PROVISION_ENABLE_SUCCESS,                "%1$s [%2$s] for %3$s [%4$s] enabled"}
  , { FeatureMessage.PROVISION_ENABLE_SKIPPED,                "Enable %1$s [%2$s] for %3$s [%4$s] skipped"}
  , { FeatureMessage.PROVISION_DISABLE_BEGIN,                 "Disable %1$s [%2$s] for %3$s [%4$s] ..."}
  , { FeatureMessage.PROVISION_DISABLE_SUCCESS,               "%1$s [%2$s] for %3$s [%4$s] disabled"}
  , { FeatureMessage.PROVISION_DISABLE_SKIPPED,               "Disable %1$s [%2$s] for %3$s [%4$s] skipped"}

    // 01361 - 01380 request related messages
  , { FeatureMessage.REQUEST_ASSIGN_BEGIN,                    "Requesting %1$s [%2$s] for %3$s [%4$s] ..."}
  , { FeatureMessage.REQUEST_ASSIGN_SUCCESS,                  "%1$s [%2$s] for %3$s [%4$s] requested"}
  , { FeatureMessage.REQUEST_ASSIGN_SKIPPED,                  "Request %1$s [%2$s] skipped"}
  , { FeatureMessage.REQUEST_REVOKE_BEGIN,                    "Requesting revoke of %1$s [%2$s] from %3$s [%4$s] ..."}
  , { FeatureMessage.REQUEST_REVOKE_SUCCESS,                  "Revoke of %1$s [%2$s] from %3$s [%4$s] requested"}
  , { FeatureMessage.REQUEST_REVOKE_SKIPPED,                  "Request to revoke %1$s [%2$s] skipped"}
  , { FeatureMessage.REQUEST_MODIFY_BEGIN,                    "Requesting modify of %1$s [%2$s] for %3$s [%4$s] ..."}
  , { FeatureMessage.REQUEST_MODIFY_SUCCESS,                  "Modify of %1$s [%2$s] for %3$s [%4$s] requested"}
  , { FeatureMessage.REQUEST_MODIFY_SKIPPED,                  "Request to modify %1$s [%2$s] skipped"}
  , { FeatureMessage.REQUEST_ENABLE_BEGIN,                    "Requesting enable of %1$s [%2$s] for %3$s [%4$s] ..."}
  , { FeatureMessage.REQUEST_ENABLE_SUCCESS,                  "Enable of %1$s [%2$s] for %3$s [%4$s] requested"}
  , { FeatureMessage.REQUEST_ENABLE_SKIPPED,                  "Request to enable %1$s [%2$s] skipped"}
  , { FeatureMessage.REQUEST_DISABLE_BEGIN,                   "Requesting disable of %1$s [%2$s] for %3$s [%4$s] ..."}
  , { FeatureMessage.REQUEST_DISABLE_SUCCESS,                 "Disable of %1$s [%2$s] for %3$s [%4$s] requested"}
  , { FeatureMessage.REQUEST_DISABLE_SKIPPED,                 "Request to disable %1$s [%2$s] skipped"}
  , { FeatureMessage.REQUEST_SUBMIT_BEGIN,                    "Submitting request %1$s [%2$s] for %3$s [%4$s] ..."}
  , { FeatureMessage.REQUEST_SUBMIT_SUCCESS,                  "Request [%1$s] submitted"}

    // 01381 - 01390 access policy related messages
  , { FeatureMessage.ACCESSPOLICY_ALLOW_ASSIGN,               "Assigning %1$s [%2$s] to %3$s [%4$s] as target for provisioning ..."}
  , { FeatureMessage.ACCESSPOLICY_ALLOW_REVOKE,               "Revoking %1$s [%2$s] from %3$s [%4$s] as target for provisioning ..."}
  , { FeatureMessage.ACCESSPOLICY_DENY_ASSIGN,                "Assigning %1$s [%2$s] to %3$s [%4$s] as target for denial ..."}
  , { FeatureMessage.ACCESSPOLICY_DENY_REVOKE,                "Revoking %1$s [%2$s] from %3$s [%4$s] as target for denial ..."}
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(FeatureResourceBundle.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)
  /**
   ** Returns an array, where each item in the array is a pair of objects.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of objects.
   */
  public Object[][] getContents() {
    return CONTENT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a String from this {@link ListResourceBundle}.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                key into the resource array.
   **
   ** @return                    the String resource
   */
  public static String string(final String key) {
    return RESOURCE.getString(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%1$s" occurrences in the string resource with the
   ** appropriate parameter.
   **
   ** @param  key                key into the resource array.
   ** @param  argument           the substitution parameter.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%1$s" and "%2$s" occurrences in the string resource
   ** with the appropriate parameter "n" from the parameters.
   **
   ** @param  key                key into the resource array.
   ** @param  argument1          the first substitution parameter.
   ** @param  argument2          the second substitution parameter.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%1$s", "%2$s" and "%3$s" occurrences in the string
   ** resource with the appropriate parameter "n" from the parameters.
   **
   ** @param  key                key into the resource array.
   ** @param  argument1          the first substitution parameter.
   ** @param  argument2          the second substitution parameter.
   ** @param  argument3          the third substitution parameter.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2, final Object argument3) {
    return RESOURCE.formatted(key, argument1, argument2, argument3);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object[] arguments) {
    return RESOURCE.formatted(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringFormat
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String stringFormat(final String key, final Object... arguments) {
    return RESOURCE.stringFormatted(key, arguments);
  }
}