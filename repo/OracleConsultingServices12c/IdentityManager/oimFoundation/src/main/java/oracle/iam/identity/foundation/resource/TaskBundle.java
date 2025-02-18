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
    Subsystem   :   Common Shared Resource Facility

    File        :   TaskBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TaskBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;

////////////////////////////////////////////////////////////////////////////////
// class TaskBundle
// ~~~~~ ~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code common
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class TaskBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OIM-00001 - 00010 task related errors
    { TaskError.UNHANDLED,                    "Unhandled exception occured: %1$s" }
  , { TaskError.GENERAL,                      "Encounter some problems: %1$s" }
  , { TaskError.ABORT,                        "Execution aborted: %1$s"}
  , { TaskError.NOTIMPLEMENTED,               "Feature is not yet implemented"}
  , { TaskError.CLASSNOTFOUND,                "Class [%1$s] was not found in the classpath"}
  , { TaskError.CLASSNOTCREATE,               "Class [%1$s] has not been created"}
  , { TaskError.CLASSNOACCESS,                "Cannot access class [%1$s]"}
  , { TaskError.CLASSINVALID,                 "Class [%1$s] must be a subclass of [%2$s]"}
  , { TaskError.CLASSCONSTRUCTOR,             "Class [%1$s] don't accept constructor parameter [%2$s]"}

     // OIM-00011 - 00015 method argument related errors
  , { TaskError.ARGUMENT_IS_NULL,             "Passed argument [%1$s] must not be null" }
  , { TaskError.ARGUMENT_BAD_TYPE,            "Passed argument [%1$s] has a bad type" }
  , { TaskError.ARGUMENT_BAD_VALUE,           "Passed argument [%1$s] contains an invalid value" }
  , { TaskError.ARGUMENT_SIZE_MISMATCH,       "Passed argument array size don't match expected length" }
  , { TaskError.ARGUMENT_IS_NULLOREMPTY,      "Passed argument [%1$s] cannot be null or empty" }
  , { TaskError.ARGUMENT_VALUE_MISSING,       "Passed argument [%1$s] must contain a [%2$s]" }

     // 00016 - 00020 task and task attribute related errors
  , { TaskError.TASK_NOTFOUND,                "Task [%1$s] does not exists" }
  , { TaskError.TASK_ATTRIBUTE_MISSING,       "Missing task attribute [%1$s]" }
  , { TaskError.TASK_ATTRIBUTE_NOT_INRANGE,   "Attribute [%1$s] is not in range, : { [%2$s] } are valid values" }
  , { TaskError.TASK_ATTRIBUTE_NOT_MAPPED,    "Value [%2$s] for Attribute [%1$s] does not exists, assigning default [%3$s]" }

     // OIM-00021 - 00030 IT Resource related errors
  , { TaskError.ITRESOURCE_NOT_FOUND,         "IT Resource [%1$s] not found" }
  , { TaskError.ITRESOURCE_AMBIGUOUS,         "IT Resource [%1$s] ambiguous" }
  , { TaskError.ITRESOURCE_ATTRIBUTE_MISSING, "Attribute [%2$s] is missing for IT Resource [%1$s]" }
  , { TaskError.ITRESOURCE_ATTRIBUTE_ISNULL,  "Attribute [%2$s] is mandatory and cannot be null for IT Resource [%1$s]" }

     // OIM-00031 - 00040 Lookup Definition related errors
  , { TaskError.LOOKUP_NOT_FOUND,             "Lookup Definition [%1$s] not found" }
  , { TaskError.LOOKUP_ENCODED_VALUE,         "Key [%2$s] does not exists in Lookup Definition [%1$s]" }
  , { TaskError.LOOKUP_INVALID_ATTRIBUTE,     "Invalid Attribute for Lookup Definition [%1$s]" }
  , { TaskError.LOOKUP_INVALID_VALUE,         "Invalid Value [%2$s] for Lookup Definition [%1$s]" }
  , { TaskError.LOOKUP_ATTRIBUTE_MISSING,     "Missing  attribute [%2$s] in Lookup Definition [%1$s]" }
  , { TaskError.LOOKUP_ATTRIBUTE_ISNULL,      "Attribute [%2$s] is mandatory in Lookup Definition [%1$s] and cannot be null" }

     // OIM-00041 - 00050 Resource Object related errors
  , { TaskError.RESOURCE_NOT_FOUND,           "No Resource Object found for [%1$s]" }
  , { TaskError.RESOURCE_AMBIGUOUS,           "Resource Object [%1$s] defined ambiguous" }
  , { TaskError.RESOURCE_RECONFIELD,          "Resource Object [%1$s] has no reconciliation field" }
  , { TaskError.RESOURCE_RECON_MULTIVALUE,    "Multi-Valued Attribute [%2$s] on Resource Object [%1$s] has no descriptor" }
  , { TaskError.PROCESSDEFINITION_NOT_FOUND,  "Provisioning Process Definition(s) for Resource Object [%1$s] not found" }
  , { TaskError.PROCESSDEFINITION_DEFAULT,    "No Default Process Definition for Resource Object [%1$s] found" }
  , { TaskError.PROCESSFORM_NOT_FOUND,        "Form Definition for Provisioning Process [%1$s] not found" }
  , { TaskError.PROCESSFORM_AMBIGUOUS,        "Form Definition for Provisioning Process [%1$s] defined ambiguous" }

     // OIM-00051 - 00060 Entity ResultSet related errors
  , { TaskError.COLUMN_NOT_FOUND,             "No Column found for [%1$s]" }
  , { TaskError.ENTITY_NOT_EXISTS,            "Unable to read definition of entity [%1$s]" }
  , { TaskError.ENTITY_NOT_FOUND,             "Entity %1$s not found for [%2$s=%3$s]" }
  , { TaskError.ENTITY_AMBIGUOUS,             "Entity %1$s [%2$s=%3$s] defined ambiguous" }

    // OIM-00061 - 00070 Reconciliation Event errors
  , { TaskError.EVENT_NOT_FOUND,              "Reconciliation Event [%1$s] not in system" }
  , { TaskError.EVENT_RECEIVE_ERROR,          "Event Receive Error occurred for Reconciliation Event [%1$s]" }
  , { TaskError.EVENT_ILLEGAL_INPUT,          "Illegal Input Error occurred for Reconciliation Event [%1$s]" }
  , { TaskError.SCHEDULE_JOB_NOT_FOUND,       "Schedule Job [%1$s] not found" }
  , { TaskError.SCHEDULE_JOB_RUNNING,         "Schedule Job [%1$s] is still running" }

     // OIM-00071 - 00080 mapping related errors
  , { TaskError.ATTRIBUTE_MAPPING_EMPTY,      "Attribute mapping is empty after filter was applied" }
  , { TaskError.TRANSFORMATION_MAPPING_EMPTY, "Attribute mapping is empty after transformation was applied" }
  , { TaskError.ATTRIBUTE_KEY_NOTMAPPED,      "Attribute value is not mapped for [%1$s]" }
  , { TaskError.ATTRIBUTE_VALUE_ISNULL,       "Attribute value is null or empty for [%1$s]" }
  , { TaskError.ATTRIBUTE_NOT_MAPPABLE,       "Attribute could not be mappedfor [%1$s]" }
  , { TaskError.ATTRIBUTE_NOT_SINGLEVALUE,    "Attribute [%1$s] is not single-valued" }

    // OIM-00081 - 00090 dependency analyzer errors
  , { TaskError.DEPENDENCY_PARENT_CONFLICT,   "Ooops existing.predecessor [%1$s] is not null, but dependency list of the predecessor [%2$s] don't know the object"}

    // OIM-00091 - 00100 EntityManager related errors
  , { TaskError.NOSUCH_ENTITY,                "[%2$s] does not exists in [%1$s]"}

    // 00101 - 00110 Authorization related errors
  , { TaskError.ACCESS_DENIED,                "Access denied to perform operation in the context of logged in user"}

     // OIM-00111 - 00120 metadata connection related errors
  , { TaskError.METADATA_INSTANCE_CREATE,     "Create instance to Metadata Store failed. Reason: %1$s" }
  , { TaskError.METADATA_INSTANCE_CLOSE,      "Closing instance to Metadata Store failed. Reason: %1$s" }
  , { TaskError.METADATA_SESSION_CREATE,      "Create session to Metadata Store failed. Reason: %1$s" }
  , { TaskError.METADATA_SESSION_COMMIT,      "Error occurred while commiting changes to Metadata Store. Reason: %1$s" }

     // OIM-00121 - 00120 metadata object related errors
  , { TaskError.METADATA_OBJECT_EMPTY,        "Path to Metadata Object cannot be empty" }
  , { TaskError.METADATA_OBJECT_NOTFOUND,     "No data found for Metadata Object [%1$s]" }

     // OIM-00131 - 00140 metadata object related errors
  , { TaskError.PROPERTY_NOTEXISTS,           "System Configuration Property with key [%1$s] does not exists" }
  , { TaskError.PROPERTY_VALUE_CONFIGURATION, "System Configuration Property with key [%1$s] is not configured properly" }

     // OIM-00141 - 00150 application instance related errors
  , { TaskError.INSTANCE_NOT_FOUND,           "Application Instance [%1$s] not found" }
  , { TaskError.INSTANCE_AMBIGUOUS,           "Application Instance [%1$s] ambiguous" }

     // OIM-01001 - 01010 reconciliation process related messages
  , { TaskMessage.RECONCILIATION_BEGIN,       "[%2$s] for [%1$s] using IT Resource [%3$s] started ..." }
  , { TaskMessage.RECONCILIATION_COMPLETE,    "[%2$s] for [%1$s] using IT Resource [%3$s] completed" }
  , { TaskMessage.RECONCILIATION_STOPPED,     "[%2$s] for [%1$s] using IT Resource [%3$s] stopped with [%4$s]" }
  , { TaskMessage.RECONCILIATION_SUCCESS,     "%1$s entries was reconciled for [%2$s]" }
  , { TaskMessage.RECONCILIATION_ERROR,       "Entry [%1$s] was not reconciled for [%2$s]" }
  , { TaskMessage.COLLECTING_BEGIN,           "Starting collecting data ..." }
  , { TaskMessage.COLLECTING_COMPLETE,        "Collecting data completed" }
  , { TaskMessage.RECONCILE_BEGIN,            "Starting reconciliation of data for [%1$s] ..." }
  , { TaskMessage.RECONCILE_COMPLETE,         "Reconciliation of data for [%1$s] completed" }
  , { TaskMessage.RECONCILE_SKIP,             "Reconciliation skipped due to user request" }

     // OIM-01011 - 01020 reconciliation process related messages
  , { TaskMessage.PROVISIONING_BEGIN,         "[%2$s] for [%1$s] using IT Resource [%3$s] started ..." }
  , { TaskMessage.PROVISIONING_COMPLETE,      "[%2$s] for [%1$s] using IT Resource [%3$s] completed" }
  , { TaskMessage.PROVISIONING_STOPPED,       "[%2$s] for [%1$s] using IT Resource [%3$s] stopped" }
  , { TaskMessage.PROVISIONING_SUCCESS,       "%1$s entries was provisioned for [%2$s]" }
  , { TaskMessage.PROVISIONING_ERROR,         "Entry [%1$s] was not provisioned for [%2$s]" }
  , { TaskMessage.PROVISIONING_SKIP,          "Provisioning skipped due to user request" }

     // OIM-01021 - 01030 configuration related related messages
  , { TaskMessage.TASK_PARAMETER,             "\nTask Parameter:\n---------------\n%1$s" }
  , { TaskMessage.ITRESOURCE_PARAMETER,       "\nIT Resource Parameter:\n----------------------\n%1$s" }
  , { TaskMessage.TASK_DESCRIPTOR,            "\nDescriptor Properties:\n----------------------\n%1$s" }
  , { TaskMessage.PROFILE_MAPPING,            "\nProfile Mapping:\n----------------\n%1$s" }
  , { TaskMessage.ATTRIBUT_MAPPING,           "\nAttribute Mapping:\n------------------\n%1$s" }
  , { TaskMessage.ATTRIBUT_TRANSFORMATION,    "\nAttribute Transformation:\n-------------------------\n%1$s" }
  , { TaskMessage.MULTIVALUED_TRANSFORMATION, "\nMulti-Valued Transformation:\n----------------------------\n%1$s" }
  , { TaskMessage.LOOKUP_TRANSFORMATION,      "\nLookup Transformation:\n----------------------\n%1$s" }
  , { TaskMessage.ENTITLEMENT_MAPPING,        "\nEntitlement Mapping [%1$s]:%2$s" }
  , { TaskMessage.ATTRIBUT_CONTROL,           "\nAttribute Control:\n------------------\n%1$s" }

     // OIM-01031 - 01040 reconciliation process related messages
  , { TaskMessage.NOTCHANGED,                 "File [%1$s] not changed since last execution" }
  , { TaskMessage.NOTAVAILABLE,               "File [%2$s] not available in [%1$s]" }
  , { TaskMessage.WILLING_TO_CHANGE,           "Reconciliation will be done for [%1$s] entries" }
  , { TaskMessage.ABLE_TO_CHANGE,             "[%1$s] entries are reconciled successful, [%2$s] entries are ignored, [%3$s] entries are failed" }
  , { TaskMessage.NOTHING_TO_CHANGE,          "No changes detected" }
  , { TaskMessage.ADDING_CHILDDATA,           "Adding child data" }
  , { TaskMessage.WILLING_TO_DELETE,          "Delete Reconciliation will be done for [%1$s] entries" }
  , { TaskMessage.ABLE_TO_DELETE,             "Delete Reconciliation was done for [%1$s] entries" }
  , { TaskMessage.NOTHING_TO_DELETE,          "No deletions detected" }

     // OIM-01051 - 01060 scheduler related messages
  , { TaskMessage.SCHEDULE_JOB_STARTING,      "Starting Job Instance [%1$s] ..." }
  , { TaskMessage.SCHEDULE_JOB_STARTED,       "Starting of Job Instance [%1$s] completed" }

     // OIM-01041 - 01050 reconciliation event related messages
  , { TaskMessage.EVENT_CREATED,              "Reconciliation event [%1$s] created on [%2$s]" }
  , { TaskMessage.EVENT_IGNORED,              "Ignoring reconciliation of %1$s [%2$s] due to no changes to previuos data." }
  , { TaskMessage.EVENT_CHILD_CREATED,        "Reconciliation event [%1$s] on [%2$s] created due to changes in [%3$s]" }
  , { TaskMessage.EVENT_CHILD_IGNORED,        "Child Reconciliation of [%1$s] in [%2$s] ignored for [%3$s]" }
  , { TaskMessage.EVENT_FAILED,               "Reconciliation event [%1$s] failed. Reason: %2$s" }
  , { TaskMessage.EVENT_FINISHED,             "Reconciliation event [%1$s] finished" }
  , { TaskMessage.EVENT_PROCEED,              "Reconciliation event [%1$s] proceed" }

     // OIM-01081 - 01110 entity object related messages
  , { TaskMessage.ENTITY_IDENTIFIER,          "%1$s [%2$s]" }
  , { TaskMessage.ENTITY_SYSTEM_PROPERTY,     "System Configuration Property" }
  , { TaskMessage.ENTITY_LOOKUP,              "Lookup Definition" }
  , { TaskMessage.ENTITY_SERVERTYPE,          "IT Resource Definition" }
  , { TaskMessage.ENTITY_SERVER,              "IT Resource" }
  , { TaskMessage.ENTITY_RESOURCE,            "Resource Object" }
  , { TaskMessage.ENTITY_PROCESS,             "Process Instance" }
  , { TaskMessage.ENTITY_PROCESSDEFINITION,   "Process Definition" }
  , { TaskMessage.ENTITY_FORM,                "Form Instance" }
  , { TaskMessage.ENTITY_FORMDEFINITION,      "Form Definition" }
  , { TaskMessage.ENTITY_SCHEDULERTASK,       "Scheduler Task" }
  , { TaskMessage.ENTITY_SCHEDULERJOB,        "Scheduler Job" }
  , { TaskMessage.ENTITY_EMAIL_TEMPLATE,      "e-Mail Template" }
  , { TaskMessage.ENTITY_ORGANIZATION,        "Organization" }
  , { TaskMessage.ENTITY_POLICY,              "Access Policy" }
  , { TaskMessage.ENTITY_ROLE,                "Role" }
  , { TaskMessage.ENTITY_GROUP,               "Group" }
  , { TaskMessage.ENTITY_IDENTITY,            "Identity" }
  , { TaskMessage.ENTITY_ACCOUNT,             "Account" }
  , { TaskMessage.ENTITY_ENTITLEMENT,         "Entitlement" }
  , { TaskMessage.ENTITY_CATALOG,             "Catalog" }
  , { TaskMessage.ENTITY_ATTRIBUTE,           "\nAttributes:\n%1$s" }
  , { TaskMessage.ENTITY_RECONCILE,           "Reconcile %1$s : [%2$s]" }
  , { TaskMessage.ENTITY_PROVISION,           "Provision %1$s : [%2$s]" }

     // OIM-01111 - 01120 entity resolver related messages
  , { TaskMessage.KEY_TORESOLVE,              "Try to resolve %1$s [%2$s]" }
  , { TaskMessage.KEY_RESOLVED,               "Internal key %1$s was resolved to [%2$s] [%3$s]" }
  , { TaskMessage.NAME_TORESOLVE,             "Try to resolve %1$s [%2$s]" }
  , { TaskMessage.NAME_RESOLVED,              "Internal key for %1$s [%2$s] is [%3$s]" }

     // OIM-01121 - 01130 metadata connection related messages
  , {TaskMessage.METADATA_SESSION_CREATE,     "Create Metadata Store session..." }
  , {TaskMessage.METADATA_SESSION_CREATED,    "Metadata Store session created" }
  , {TaskMessage.METADATA_SESSION_COMMIT,     "Commiting changes in Metadata Store..." }
  , {TaskMessage.METADATA_SESSION_COMMITED,   "Changes in Metadata Store commited." }
  , {TaskMessage.METADATA_SESSION_ROLLBACK,   "Rolling back changes in Metadata Store..." }
  , {TaskMessage.METADATA_SESSION_ROLLEDBACK, "Changes in Metadata Store rolled back." }

     // OIM-01131 - 01140 metadata object related messages
  , {TaskMessage.METADATA_OBJECT_FETCH,       "Obtaining [%1$s] from Metadata Store..." }
  , {TaskMessage.METADATA_OBJECT_STORE,       "Storing [%1$s] in Metadata Store..." }
  , {TaskMessage.METADATA_OBJECT_MARSHAL,     "Marshalling [%1$s]..." }
  , {TaskMessage.METADATA_OBJECT_UNMARSHAL,   "Unmarshalling [%1$s]..." }

     // OIM-01141 - 01150 entitlement related messages
  , { TaskMessage.ENTITLEMENT_ACTION,         "\n%1$s of type %2$s" }
  , { TaskMessage.ENTITLEMENT_ACTION_ASSIGN,  "Assign" }
  , { TaskMessage.ENTITLEMENT_ACTION_REVOKE,  "Revoke" }
  , { TaskMessage.ENTITLEMENT_ACTION_ENABLE,  "Enable" }
  , { TaskMessage.ENTITLEMENT_ACTION_DISABLE, "Disable" }
  , { TaskMessage.ENTITLEMENT_RISK,           " with Risk of level %1$s" }
  , { TaskMessage.ENTITLEMENT_RISK_NONE,      "None" }
  , { TaskMessage.ENTITLEMENT_RISK_LOW,       "Low" }
  , { TaskMessage.ENTITLEMENT_RISK_MEDIUM,    "Medium" }
  , { TaskMessage.ENTITLEMENT_RISK_HIGH,      "High" }

      // OIM-01051 - 01060 lookup reconciliation related messages
  , { TaskMessage.LOOKUP_CREATE_VALUE,        "Not able to add [%2$s] to Lookup Definition [%1$s]." }
  , { TaskMessage.LOOKUP_REMOVE_VALUE,        "Not able to remove [%2$s] from Lookup Definition [%1$s]." }
  , { TaskMessage.LOOKUP_DUPLICATE_VALUE,     "New value [%2$s] is same as existing value in Lookup Definition [%1$s]." }
  , { TaskMessage.LOOKUP_FILTER_NOTACCEPTED,  "Lookup value [%2$s] ignored due to filter condition [%1$s] did not match." }
 };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    TaskBundle.class.getName()
  , Locale.getDefault()
  , TaskBundle.class.getClassLoader()
  );

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
  @Override
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
   **
   ** @param  key                 key into the resource array.
   ** @param  argument            the subsitution value for %1$s
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for %1$s
   ** @param  argument2           the subsitution value for [%2$s]
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for %1$s
   ** @param  argument2           the subsitution value for [%2$s]
   ** @param  argument3           the subsitution value for [%3$s]
   **
   ** @return                     the formatted String resource
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
   ** @return                     the formatted String resource
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