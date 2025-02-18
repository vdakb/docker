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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Zero Provisioning

    File        :   ZeroBundle_de.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    ZeroBundle_de.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      14.08.2023  SBernet     First release version
*/
package bka.iam.identity.zero.resources;

import bka.iam.identity.zero.ZeroError;
import bka.iam.identity.zero.ZeroMessage;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class ZeroBundle_de
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code german
 **   <li>language code common
 ** </ul>
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ZeroBundle_de extends ListResourceBundle {

  private static final String[][] CONTENT = {
  
    // ZRO-00001 - 00010 configuration related errors
    { ZeroError.APPLICATION_NOTFOUND,            "Anwendung %1$s nicht gefunden." }
  , { ZeroError.ORGANIZATION_NOTFOUND,           "Organisation %1$s wurde nicht gefunden." }
  , { ZeroError.PROPERTY_NOTFOUND,               "Die Systemeigenschaft „%1$s“ existiert nicht. Bitte geben Sie „%1$s“ wie erwartet ein." }
  , { ZeroError.PROPERTY_INVALID,                "Die Systemeigenschaft „%1$s“ ist nicht richtig konfiguriert. Bitte geben Sie „%1$s“ wie erwartet ein." }
    // ZRO-00011 - 00020 ldap related errors
  , { ZeroError.LDAP_ERROR,                      "LDAP-Fehler: %1$s"}
  , { ZeroError.NAMING_DN_ERROR,                 "LDAP-DN: %1$s fehlerhaft."}
  , { ZeroError.INDEX_RDN_OUT_RANGE,             "RDN index: %1$s außerhalb des Bereichs auf DN: %2$s."}
    
    // ZRO-00021 - 00030 notification related errors
  , {ZeroError.NOTIFICATION_FAILED,              "Das Senden der Benachrichtigung ist nicht erfolgreich. Es ist eine Ereignisausnahme aufgetreten." }
  , {ZeroError.NOTIFICATION_EXCEPTION,           "Das Senden der Benachrichtigung ist nicht erfolgreich. Benachrichtigungsausnahme ist aufgetreten." }
  , {ZeroError.NOTIFICATION_UNRESOLVED_DATA,     "Das Senden der Benachrichtigung ist nicht erfolgreich. Benachrichtigungsdaten nicht aufgelöst." }
  , {ZeroError.NOTIFICATION_TEMPLATE_NOTFOUND,   "Sending Notification is not successful. Notification Template not found." }
  , {ZeroError.NOTIFICATION_TEMPLATE_AMBIGOUS,   "Das Senden der Benachrichtigung ist nicht erfolgreich. Benachrichtigungsvorlage nicht gefunden." }
  , {ZeroError.NOTIFICATION_RESOLVER_NOTFOUND,   "Das Senden der Benachrichtigung ist nicht erfolgreich. Benachrichtigungslöser nicht gefunden." }
  , {ZeroError.NOTIFICATION_IDENTITY_NOTFOUND,   "Das Senden der Benachrichtigung ist nicht erfolgreich. Identitätsdetails nicht gefunden." }
  , {ZeroError.NOTIFICATION_RECIPIENT_EMPTY,     "Das Senden der Benachrichtigung ist nicht erfolgreich. Die Empfängerliste ist leer." }

    // ZRO-01001 - 01010  account operation related message
  , { ZeroMessage.REQUEST_NEW_ACCOUNT,            "Neues Angebot mit folgenden Werten:\n%1$s."}
  , { ZeroMessage.REQUEST_MODIFY_ACCOUNT,         "Bearbeiten eines vorhandenen Kontos mit den folgenden Werten:\n%1$s."}
  , { ZeroMessage.NO_ACCOUNT_TO_REQUEST,          "Keine Konten auf LDAP gefunden. Auf OIM werden keine Operationen durchgeführt."}
    
    // ZRO-01001 - 01010 ldap related messages
  , { ZeroMessage.REQUEST_NEW_ACCOUNT,            "Fordern Sie ein neues Konto mit folgendem Wert an:\n%1$s."}
  , { ZeroMessage.REQUEST_MODIFY_ACCOUNT,         "Ändern Sie ein vorhandenes Konto mit dem folgenden Wert:\n%1$s."}
  , { ZeroMessage.LDAP_ATTR_NOT_FOUND,            "Das Attribut %1$s wurde in der Eingabe %2$s nicht gefunden."}
    
  // ZRO-00021 - 00040 notification related message
  , { ZeroMessage.NOTIFICATION_RESOLVE_INCOME,    "Sammlung der empfangenen Substitutionszuordnungen:\n"}
  , { ZeroMessage.NOTIFICATION_RESOLVE_OUTCOME,   "Sammlung der empfangenen Substitutionszuordnungen:\n"}
  , { ZeroMessage.REPORTER_EMPTY,                 "Keine Änderung an den Konten, die auf %1$s erkannt wurden. Keine Benachrichtigung zum Senden."}
  , { ZeroMessage.REPORTER_NOT_FOUND,             "Reporter %1$s nicht gefunden. Keine Benachrichtigung zum Senden."}
 
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