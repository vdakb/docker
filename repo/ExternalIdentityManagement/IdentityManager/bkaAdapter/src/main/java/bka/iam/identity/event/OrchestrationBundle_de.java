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

    Copyright 2019 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Common Shared Plugin

    File        :   OrchestrationBundle_de.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OrchestrationBundle_de.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
*/

package bka.iam.identity.event;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class OrchestrationBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
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
 ** @since   1.0.0.0
 */
public class OrchestrationBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    // ORC-00001 - 00010 configuration related errors
    {OrchestrationError.PROPERTY_NOTFOUND,                 "Die Systemeigenschaft [%1$s] existiert nicht. Bitte geben Sie [%1$s] wie erwartet an." }
  , {OrchestrationError.PROPERTY_INVALID,                  "Die Systemeigenschaft [%1$s] ist nicht richtig konfiguriert. Bitte geben Sie [%1$s] wie erwartet an." }

    // ORC-00011 - 00020 notification related errors
  , {OrchestrationError.NOTIFICATION_FAILED,               "Versand der Benachrichtigung war nicht erfolgreich. Event Exception aufgetreten." }
  , {OrchestrationError.NOTIFICATION_EXCEPTION,            "Versand der Benachrichtigung war nicht erfolgreich. Notification Exception aufgetreten." }
  , {OrchestrationError.NOTIFICATION_STATIC_DATA,          "Statische Daten sind leer, die Bereitstellung statischer Daten erfolgt in der Konfiguration des Ereignisses, z.B. [%1$s]" }
  , {OrchestrationError.NOTIFICATION_UNRESOLVED_DATA,      "Versand der Benachrichtigung war nicht erfolgreich. Notification Data not resolved." }
  , {OrchestrationError.NOTIFICATION_TEMPLATE_NOTFOUND,    "Versand der Benachrichtigung war nicht erfolgreich. Notification Template not found." }
  , {OrchestrationError.NOTIFICATION_TEMPLATE_AMBIGOUS,    "Versand der Benachrichtigung war nicht erfolgreich. Multiple template exception occured." }
  , {OrchestrationError.NOTIFICATION_RESOLVER_NOTFOUND,    "Versand der Benachrichtigung war nicht erfolgreich. Notification Resolver not found." }
  , {OrchestrationError.NOTIFICATION_IDENTITY_NOTFOUND,    "Versand der Benachrichtigung war nicht erfolgreich. Identity Details not found." }

    // ORC-00021 - 00030 account request related errors
  , {OrchestrationError.ACCOUNT_EXISTS_ANY,                 "Folgende Benutzerkonten existieren bereits:"}
    
     // 00031 - 00040 Unique Identifier related messages
  , {OrchestrationError.UID_TENANT_NOT_MATCH,               "Der Tenant der UID [%1$s] stimmt nicht mit ihrer Organisation überein."}
  , {OrchestrationError.UID_ALREADY_USED,                   "Die UID [%1$s] wird bereits verwendet."}
  , {OrchestrationError.UID_MODIFY_NOT_ALLOWED,             "Änderung einer UID ist nicht erlaubt."}

    // ORC-01001 - 01010 UI related messages
  , {OrchestrationMessage.PRINCIPAL_NAME_LABEL,             "Principal Name"}
  , {OrchestrationMessage.PRINCIPAL_NAME_UNCHNAGED,         "Wert für Principal Name nicht geändert; Prüfung ausgesetzt"}
  , {OrchestrationMessage.PARTICIPANT_LABEL,                "Teilnehmer"}
  , {OrchestrationMessage.PARTICIPANT_UNCHNAGED,            "Wert für Teilnehmer nicht geändert; Prüfung ausgesetzt"}

    // ORC-01011 - 01020 account request related errors
  , {OrchestrationMessage.ACCOUNT_RULE_VIOLATED,            "Provisioning status for user [%1$s] validated, implicite validation rule is violated"}
  , {OrchestrationMessage.ACCOUNT_RULE_SATIISFIED,          "Provisioning status for user [%1$s] validated, Implicite validation rule is satisfied"}
  , {OrchestrationMessage.ACCOUNT_VALIDATION_SINGLE,        "Validating provisioning status for user [%1$s] at application [%2$s]"}
  , {OrchestrationMessage.ACCOUNT_VALIDATION_MULTIPLE,      "Validating provisioning status for user [%1$s] at application [%2$s] for account name [%3$s]"}

    // ORC-01021 - 01030 notification related messages
  , {OrchestrationMessage.NOTIFICATION_RESOLVE_INCOME,      "Eingegangene Ersetzungsdaten:\n"}
  , {OrchestrationMessage.NOTIFICATION_RESOLVE_OUTCOME,     "Zurückgegebene Ersetzungsdaten:\n"}
  , {OrchestrationMessage.NOTIFICATION_ROLE_GRANTED,        "Benachrichtigung über Zuweisung der Rolle [%1$s] an Identität [%2$s] unter Verwendung der Vorlage [%3$s] versandt." }
  , {OrchestrationMessage.NOTIFICATION_ROLE_REVOKED,        "Benachrichtigung über Entzug der Rolle [%1$s] an Identität [%2$s] unter Verwendung der Vorlage [%3$s] versandt." }
  , {OrchestrationMessage.NOTIFICATION_ACCOUNT_GRANTED,     "Benachrichtigung über Zuweisung des Benutzerkontos [%1$s] an Identität [%2$s] unter Verwendung der Vorlage [%3$s] versandt." }
  , {OrchestrationMessage.NOTIFICATION_ACCOUNT_REVOKED,     "Benachrichtigung über Entzug der Benutzerkontos [%1$s] an Identität [%2$s] unter Verwendung der Vorlage [%3$s] versandt." }
  , {OrchestrationMessage.NOTIFICATION_ENTITLEMENT_GRANTED, "Benachrichtigung über Zuweisung der Berechtigung [%1$s] an Identität [%2$s] unter Verwendung der Vorlage [%3$s] versandt." }
  , {OrchestrationMessage.NOTIFICATION_ENTITLEMENT_REVOKED, "Benachrichtigung über Entzug der Berechtigung [%1$s] an Identität [%2$s] unter Verwendung der Vorlage [%3$s] versandt." }

    // 00041 - 00050 Organization related errors
  , {OrchestrationError.ATTRIBUTE_MUST_BE_UNIQUE,           "Attribut [%1$s] müssen eindeutig sein." }
  , {OrchestrationError.ATTRIBUTE_MUST_NOT_BE_BLANK,        "Attribut [%1$s] darf nicht leer sein." }
  , {OrchestrationError.ORGANIZATION_SEARCH_ERROR,          "Fehler beim Suchen von Organisationen - [%1$s]." }
  , {OrchestrationError.ORGANIZATION_MEMBERS,               "Beim Suchen nach Organisationsmitgliedern ist ein Fehler aufgetreten - [%1$s]." }

  // 00051 - 00060 Application related errors
  , {OrchestrationError.APPLICATION_SEARCH_BY_OBJ,          "Die Anwendung kann von Ressourcenobjekten nicht gefunden werden [%1$s], [%2$s]." }
  , {OrchestrationError.ACCOUNT_SEARCH,                     "Konten können per Antrag nicht gefunden werden [%1$s]." }
  , {OrchestrationError.ACCOUNT_UPDATE,                     "Fehler bei der Kontoaktualisierung [%1$s]." }

  // 00061 - 00070 Process task related errors
  , {OrchestrationError.PROCESS_TASK_BY_NAME,               "Prozessaufgabe kann nicht nach Name gefunden werden [%1$s]." }
  , {OrchestrationError.PROCESS_TASK_NOT_FOUND,             "Prozessaufgabe kann anhand der ID nicht gefunden werden [%1$s]." }

  // 00071 - 00080 Authentication related errors
  , {OrchestrationError.LOGIN_FAILED,                       "Die Anmeldung ist während der Legacy-Authentifizierung fehlgeschlagen." }
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
  public Object[][] getContents() {
    return CONTENT;
  }
}