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

    File        :   TaskBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TaskBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;

////////////////////////////////////////////////////////////////////////////////
// class TaskBundle_de
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class TaskBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OIM-00001 - 00010 task related errors
    { TaskError.UNHANDLED,                    "Eine unbehandelte Ausnahme ist aufgetreten: %1$s"}
  , { TaskError.GENERAL,                      "Allgemeiner Fehler: %1$s" }
  , { TaskError.ABORT,                        "Die Verarbeitung wird abgebrochen: %1$s"}
  , { TaskError.NOTIMPLEMENTED,               "Funktionalität ist nicht implementiert"}
  , { TaskError.CLASSNOTFOUND,                "Klasse [%1$s] konnte im Klassenpfad nicht gefunden werden"}
  , { TaskError.CLASSNOTCREATE,               "Klasse [%1$s] konnte nicht erzeugt werden"}
  , { TaskError.CLASSNOACCESS,                "Auf Klasse [%1$s] kann nicht zugegriffen werden"}
  , { TaskError.CLASSINVALID,                 "Klasse [%1$s] muss eine Ableitung von Klasse [%2$s] sein"}
  , { TaskError.CLASSCONSTRUCTOR,             "Klasse [%1$s] akzeptiert keinen Konstruktorparameter [%2$s]"}

     // OIM-00011 - 00015 method argument related errors
  , { TaskError.ARGUMENT_IS_NULL,             "Argument [%1$s] darf nicht null sein" }
  , { TaskError.ARGUMENT_BAD_TYPE,            "Argument [%1$s] hat nicht den erforderlichen Type" }
  , { TaskError.ARGUMENT_BAD_VALUE,           "Argument [%1$s] beinhaltet nicht den erforderlichen Wert" }
  , { TaskError.ARGUMENT_SIZE_MISMATCH,       "Die Anzahl der übergebenen Argumente entspricht nicht der erwarteten Anzahl" }
  , { TaskError.ARGUMENT_IS_NULLOREMPTY,      "Argument [%1$s] darf nicht null oder leer sein" }
  , { TaskError.ARGUMENT_VALUE_MISSING,       "Argument [%1$s] muss [%2$s] beinhalten" }

     // OIM-00016 - 00020 task and task attribute related errors
  , { TaskError.TASK_NOTFOUND,                "Task [%1$s] existiert nicht" }
  , { TaskError.TASK_ATTRIBUTE_MISSING,       "Task Attribute [%1$s] existiert nicht" }
  , { TaskError.TASK_ATTRIBUTE_NOT_INRANGE,   "Task Attribute [%1$s] ist nicht im gültigen Bereich, : nur { [%2$s] } sind erlaubte Werte" }
  , { TaskError.TASK_ATTRIBUTE_NOT_MAPPED,    "Wert [%2$s] für Attribute [%1$s] existiert nicht, Standardwert [%3$s] wird zugewiesen" }

     // OIM-00021 - 00030 IT Resource related errors
  , { TaskError.ITRESOURCE_NOT_FOUND,         "IT Resource [%1$s] existiert nicht" }
  , { TaskError.ITRESOURCE_AMBIGUOUS,         "IT Resource [%1$s] ist mehrfach definiert" }
  , { TaskError.ITRESOURCE_ATTRIBUTE_MISSING, "Attribut [%2$s] ist für IT Resource [%1$s] nicht definiert " }
  , { TaskError.ITRESOURCE_ATTRIBUTE_ISNULL,  "Attribut [%2$s] für IT Resource [%1$s] muss einen Wert besitzen" }

     // OIM-00031 - 00040 Lookup Definition related errors
  , { TaskError.LOOKUP_NOT_FOUND,             "Lookup Definition [%1$s] existiert nicht" }
  , { TaskError.LOOKUP_ENCODED_VALUE,         "Schlüssel [%2$s] existiert nicht in Lookup Definition [%1$s]" }
  , { TaskError.LOOKUP_INVALID_ATTRIBUTE,     "Ungültiges Attribut für Lookup Definition [%1$s]" }
  , { TaskError.LOOKUP_INVALID_VALUE,         "Ungültiger Wert [%2$s] für Lookup Definition [%1$s]" }
  , { TaskError.LOOKUP_ATTRIBUTE_MISSING,     "Lookup Definition Attribut [%1$s] ist nicht definiert" }
  , { TaskError.LOOKUP_ATTRIBUTE_ISNULL,      "Attribute [%2$s] ist mandatory in Lookup Definition [%1$s] and cannot be null" }

     // OIM-00041 - 00050 Resource Object related errors
  , { TaskError.RESOURCE_NOT_FOUND,           "Resource Object [%1$s] existiert nicht" }
  , { TaskError.RESOURCE_AMBIGUOUS,           "Resource Object [%1$s] ist mehrfach definiert" }
  , { TaskError.RESOURCE_RECONFIELD,          "Keine Datenabgleichsfelder definiert für Resource Object [%1$s]" }
  , { TaskError.RESOURCE_RECON_MULTIVALUE,    "Multi-Valued Attribute [%2$s] für Resource Object [%1$s] hat keinen Descriptor" }
  , { TaskError.PROCESSDEFINITION_NOT_FOUND,  "Keine Prozessdefinition(en) zur Provisionierung von Resource Object [%1$s] vorhanden" }
  , { TaskError.PROCESSDEFINITION_DEFAULT,    "Keins Standard-Prozessdefinition zur Provisionierung von Resource Object [%1$s] vorhanden" }
  , { TaskError.PROCESSFORM_NOT_FOUND,        "Formular-Definition für Prozessdefinition [%1$s] vorhanden" }
  , { TaskError.PROCESSFORM_AMBIGUOUS,        "Formular-Definition für Prozessdefinition [%1$s] ist mehrfach definiert" }

     // OIM-00051 - 00060 ResultSet related errors
  , { TaskError.COLUMN_NOT_FOUND,             "Spalte existiert nicht in [%1$s]" }
  , { TaskError.ENTITY_NOT_EXISTS,            "Definition des Entity [%1$s] kann nicht gelesen werden" }
  , { TaskError.ENTITY_NOT_FOUND,             "Entity %1$s konnte für [%2$s=%3$s] nicht gefunden werden" }
  , { TaskError.ENTITY_AMBIGUOUS,             "Entity %1$s ist mehrfach definiert für [%2$s=%3$s]" }

     // OIM-00061 - 00070 Reconciliation Event errors
  , { TaskError.EVENT_NOT_FOUND,              "Datenabgleichsereignis [%1$s] konnte im System nicht gefunden werden" }
  , { TaskError.EVENT_RECEIVE_ERROR,          "Fehler Event Received Error ist für Datenabgleichsereignis [%1$s] aufgetreten" }
  , { TaskError.EVENT_ILLEGAL_INPUT,          "Fehler Illegal Input ist für Datenabgleichsereignis [%1$s] aufgetreten" }

     // OIM-00071 - 00080 mapping related errors
  , { TaskError.ATTRIBUTE_MAPPING_EMPTY,      "Attribute mapping is empty after filter was applied" }
  , { TaskError.TRANSFORMATION_MAPPING_EMPTY, "Attribute mapping is empty after transformation was applied" }
  , { TaskError.ATTRIBUTE_KEY_NOTMAPPED,      "Attribute value is not mapped for [%1$s]" }
  , { TaskError.ATTRIBUTE_VALUE_ISNULL,       "Attribute value is null or empty for [%1$s]" }
  , { TaskError.ATTRIBUTE_NOT_MAPPABLE,       "Attribute could not be mappedfor [%1$s]" }
  , { TaskError.ATTRIBUTE_NOT_SINGLEVALUE,    "Attribute [%1$s] is not single-valued" }

     // OIM-00081 - 00090 dependency analyzer errors
  , { TaskError.DEPENDENCY_PARENT_CONFLICT,   "Ooops existing.predecessor [%1$s] is not null, but dependency list of the predecessor [%2$s] don't know the object"}

    // 00101 - 00110 Authorization related errors
  , { TaskError.ACCESS_DENIED,                "Access denied to perform operation in the context of logged in user"}

     // OIM-00111 - 00120 metadata connection related errors
  , { TaskError.METADATA_INSTANCE_CREATE,     "Create instance to Metadata Store failed. Reason: %1$s" }
  , { TaskError.METADATA_INSTANCE_CLOSE,      "Closing instance to Metadata Store failed. Reason: %1$s" }
  , { TaskError.METADATA_SESSION_CREATE,      "Create session to Metadata Store failed. Reason: %1$s" }
  , { TaskError.METADATA_SESSION_COMMIT,      "Error occurred while commiting changes to Metadata Store. Reason: %1$s" }

     // 00121 - 00120 metadata object related errors
  , { TaskError.METADATA_OBJECT_EMPTY,        "Path to Metadata Object cannot be empty" }
  , { TaskError.METADATA_OBJECT_NOTFOUND,     "No data found for Metadata Object [%1$s]" }

     // OIM-00131 - 00140 metadata object related errors
  , { TaskError.PROPERTY_NOTEXISTS,           "System Configuration Property with key [%1$s] does not exists" }
  , { TaskError.PROPERTY_VALUE_CONFIGURATION, "System Configuration Property with key [%1$s] is not configured properly" }

     // OIM-00141 - 00150 application instance related errors
  , { TaskError.INSTANCE_NOT_FOUND,           "Application Instance [%1$s] not found" }
  , { TaskError.INSTANCE_AMBIGUOUS,           "Application Instance [%1$s] ambiguous" }

     // OIM-01001 - 01010 reconciliation process related messages
  , { TaskMessage.RECONCILIATION_BEGIN,       "[%2$s] für [%1$s] unter Verwendung von IT Resource [%3$s] wurde gestartet ..." }
  , { TaskMessage.RECONCILIATION_COMPLETE,    "[%2$s] für [%1$s] unter Verwendung von IT Resource [%3$s] wurde beendet" }
  , { TaskMessage.RECONCILIATION_STOPPED,     "[%2$s] für [%1$s] unter Verwendung von IT Resource [%3$s] wurde mit Fehlermeldung [%4$s] abgebrochen" }
  , { TaskMessage.RECONCILIATION_SUCCESS,     "%1$s Einträge wurden mit [%2$s] abgeglichen" }
  , { TaskMessage.RECONCILIATION_ERROR,       "Eintrag [%1$s] wurde nicht mit [%2$s] abgeglichen" }
  , { TaskMessage.COLLECTING_BEGIN,           "Starte Zusammenstellung der Daten ..." }
  , { TaskMessage.COLLECTING_COMPLETE,        "Zusammenstellung der Daten beendet" }
  , { TaskMessage.RECONCILE_BEGIN,            "Abgleich der Daten für [%1$s] wurde gestartet ..." }
  , { TaskMessage.RECONCILE_COMPLETE,         "Abgleich der Daten für [%1$s] wurde beendet" }
  , { TaskMessage.RECONCILE_SKIP,             "Abgleich der Daten auf Grund Benutzeranforderung übersprungen" }

    // 01011 - 01020 reconciliation process related messages
  , { TaskMessage.PROVISIONING_BEGIN,         "[%2$s] für [%1$s] unter Verwendung von IT Resource [%3$s] wurde gestartet ..." }
  , { TaskMessage.PROVISIONING_COMPLETE,      "[%2$s] für [%1$s] unter Verwendung von IT Resource [%3$s] wurde beendet" }
  , { TaskMessage.PROVISIONING_STOPPED,       "[%2$s] für [%1$s] unter Verwendung von IT Resource [%3$s] wurde mit Fehlermeldung [%4$s] abgebrochen" }
  , { TaskMessage.PROVISIONING_SUCCESS,       "%1$s Einträge wurden nach [%2$s] provisioniert" }
  , { TaskMessage.PROVISIONING_ERROR,         "Eintrag [%1$s] wurde nicht nach [%2$s] provisioniert" }
  , { TaskMessage.PROVISIONING_SKIP,          "Provisionierung der Daten auf Grund Benutzeranforderung übersprungen" }

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
  , { TaskMessage.NOTCHANGED,                 "Datei [%1$s] wurde seit der letzten Ausführung nicht geändert" }
  , { TaskMessage.NOTAVAILABLE,               "Datei [%2$s] ist nicht verfügbar in [%1$s]" }
  , { TaskMessage.WILLING_TO_CHANGE,          "Datenabgleichs wird für [%1$s] Einträge vorgenommen" }
  , { TaskMessage.ABLE_TO_CHANGE,             "[%1$s] Einträge wurden erfolgreich abgeglichen, [%2$s] Einträge wurden ignoriert, [%3$s] Einträge sind fehlgeschlagen" }
  , { TaskMessage.NOTHING_TO_CHANGE,          "Keine Änderungen vorgefunden" }
  , { TaskMessage.ADDING_CHILDDATA,           "Child Daten werden hinzugefügt" }
  , { TaskMessage.WILLING_TO_DELETE,          "Datenabgleichs zur Löschung wird für [%1$s] Einträge vorgenommen" }
  , { TaskMessage.ABLE_TO_DELETE,             "Datenabgleichs zur Löschung wurde für [%1$s] Einträge vorgenommen" }
  , { TaskMessage.NOTHING_TO_DELETE,          "Keine Löschungen vorgefunden" }

     // OIM-01041 - 01050 reconciliation event related messages
  , { TaskMessage.EVENT_CREATED,              "Datenabgleichsereignis [%1$s] erzeugt für [%2$s]" }
  , { TaskMessage.EVENT_IGNORED,              "Ignoring reconciliation of %1$s [%2$s] due to no changes to previuos data." }
  , { TaskMessage.EVENT_IGNORED,              "Datenabgleich von %1$s [%2$s] ignoriert, da keine zu vorherigen Daten vorhanden ist." }
  , { TaskMessage.EVENT_CHILD_CREATED,        "Abhängiges Datenabgleichsereignis [%1$s] erzeugt für [%2$s] wegen Änderung in [%3$s]" }
  , { TaskMessage.EVENT_CHILD_IGNORED,        "Abhängiger Datenabgleich von [%1$s] innerhalb von [%2$s] ignoriert für [%3$s]" }
  , { TaskMessage.EVENT_FAILED,               "Datenabgleichsereignis [%1$s] ist fehlgeschlagen. Grund: %2$s" }
  , { TaskMessage.EVENT_FINISHED,             "Datenabgleichsereignis [%1$s] beendet" }
  , { TaskMessage.EVENT_PROCEED,              "Datenabgleichsereignis [%1$s] verarbeitet" }

     // OIM-01081 - 01110 entity object related messages
  , { TaskMessage.ENTITY_IDENTIFIER,          "%1$s [%2$s]" }
  , { TaskMessage.ENTITY_SYSTEM_PROPERTY,     "Systemeigenschaft" }
  , { TaskMessage.ENTITY_LOOKUP,              "Wertelisten Definition" }
  , { TaskMessage.ENTITY_SERVERTYPE,          "IT Resource Definition" }
  , { TaskMessage.ENTITY_SERVER,              "IT Resource" }
  , { TaskMessage.ENTITY_RESOURCE,            "Resource Objekt" }
  , { TaskMessage.ENTITY_PROCESS,             "Prozessinstance" }
  , { TaskMessage.ENTITY_PROCESSDEFINITION,   "Prozessdefinition" }
  , { TaskMessage.ENTITY_FORM,                "Forminstance" }
  , { TaskMessage.ENTITY_FORMDEFINITION,      "Formdefinition" }
  , { TaskMessage.ENTITY_SCHEDULERTASK,       "Scheduler Task" }
  , { TaskMessage.ENTITY_SCHEDULERJOB,        "Scheduler Job" }
  , { TaskMessage.ENTITY_EMAIL_TEMPLATE,      "e-Mail Vorlage" }
  , { TaskMessage.ENTITY_ROLE,                "Rolle" }
  , { TaskMessage.ENTITY_GROUP,               "Gruppe" }
  , { TaskMessage.ENTITY_POLICY,              "Richtlinie" }
  , { TaskMessage.ENTITY_ORGANIZATION,        "Organisation" }
  , { TaskMessage.ENTITY_IDENTITY,            "Identität" }
  , { TaskMessage.ENTITY_ACCOUNT,             "Konto" }
  , { TaskMessage.ENTITY_ENTITLEMENT,         "Berechtigung" }
  , { TaskMessage.ENTITY_CATALOG,             "Katalog" }
  , { TaskMessage.ENTITY_ATTRIBUTE,           "\nAttribute:\n%1$s" }
  , { TaskMessage.ENTITY_RECONCILE,           "Datenabgleich %1$s : [%2$s]" }
  , { TaskMessage.ENTITY_PROVISION,           "Provisionierung %1$s : [%2$s]" }

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
  , { TaskMessage.ENTITLEMENT_ACTION,         "\n%1$s von Typ %2$s"}
  , { TaskMessage.ENTITLEMENT_ACTION_ASSIGN,  "Zuweisung" }
  , { TaskMessage.ENTITLEMENT_ACTION_REVOKE,  "Entzug" }
  , { TaskMessage.ENTITLEMENT_ACTION_ENABLE,  "Aktivierung" }
  , { TaskMessage.ENTITLEMENT_ACTION_DISABLE, "Deaktivierung" }
  , { TaskMessage.ENTITLEMENT_RISK,           " mit Risiko der Stufe %1$s" }
  , { TaskMessage.ENTITLEMENT_RISK_NONE,      "Null" }
  , { TaskMessage.ENTITLEMENT_RISK_LOW,       "Niedrig" }
  , { TaskMessage.ENTITLEMENT_RISK_MEDIUM,    "Mittel" }
  , { TaskMessage.ENTITLEMENT_RISK_HIGH,      "Hoch" }

     // OIM-01051 - 01060 lookup reconciliation related messages
  , { TaskMessage.LOOKUP_CREATE_VALUE,        "Neuer Wert [%2$s] konnte der Lookup Definition [%1$s] nicht hinzugefügt werden." }
  , { TaskMessage.LOOKUP_REMOVE_VALUE,        "Alter Wert [%2$s] konnte aus Lookup Definition [%1$s] nicht entfernt werden." }
  , { TaskMessage.LOOKUP_DUPLICATE_VALUE,     "Neuer Wert [%2$s] ist mit dem existierenden Wert identisch in Lookup Definition [%1$s]." }
  , { TaskMessage.LOOKUP_FILTER_NOTACCEPTED,  "Wert [%2$s] wurde ignoriert, da die Filterbedingung [%1$s] diesen Wert ausschliesst." }
  };

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
}