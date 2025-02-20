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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Federated Identity Management
    Subsystem   :   Federal Criminal Police Office Frontend Customizations

    File        :   CatalogBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package bka.iam.identity.resource;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class CatalogBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~
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
public class CatalogBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     /** Constant used to demarcate catalog entity related messages. */
    {Catalog.CAT_RISK_LABEL,                "Riskostufe"}
  , {Catalog.CAT_RISK_HINT,                 "Riskostufe"}
  , {Catalog.CAT_ROLE_OWNER_LABEL,          "Rolleneigner"}
  , {Catalog.CAT_ROLE_OWNER_HINT,           "Rolleneigner"}
  , {Catalog.CAT_ROLE_DESCRIPTION_LABEL,    "Rollenbeschreibung"}
  , {Catalog.CAT_ROLE_DESCRIPTION_HINT,     "Rollenbeschreibung"}
  , {Catalog.CAT_VALID_FROM_LABEL,          "gültig ab"}
  , {Catalog.CAT_VALID_FROM_HINT,           "gültig ab"}
  , {Catalog.CAT_PRIVILEGE_ACTION_LABEL,    "Aktion"}
  , {Catalog.CAT_PRIVILEGE_ACTION_HINT,     "Aktion"}
  , {Catalog.CAT_PRIVILEGE_ASSIGN_LABEL,    "Vergabe von Berechtigungen"}
  , {Catalog.CAT_PRIVILEGE_ASSIGN_HINT,     "Vergabe von Berechtigungen"}
  , {Catalog.CAT_PRIVILEGE_REVOKE_LABEL,    "Vergabe von Berechtigungen"}
  , {Catalog.CAT_PRIVILEGE_REVOKE_LABEL,    "Vergabe von Berechtigungen"}
  , {Catalog.CAT_BASIC_HEADER,              "Basisinformationen"}
  , {Catalog.CAT_ACCOUNT_HEADER,            "Kontoinformationen"}
  , {Catalog.CAT_CONTACT_HEADER,            "Kontaktinformationen"}
  , {Catalog.CAT_ORGANIZATION_HEADER,       "Organisationsinformation"}
  , {Catalog.CAT_MISCELLANEOUS_HEADER,      "Sonstige Informationen"}

  /* Constant used to demarcate common application entities related messages. */
  , {Catalog.CAT_USR_IDENTIFIER_LABEL,      "Identifier"}
  , {Catalog.CAT_USR_LOGIN_NAME_LABEL,      "Benutzerkennung"}
  , {Catalog.CAT_USR_UNIFIED_LABEL,         "Vereinheitlichte Anmeldung"}
  , {Catalog.CAT_USR_UNIFIED_HINT,          "Der Anmeldename, der von dieser Identität verwendet wird, um durch P20 Applikationen und Dienste authentiisert und authorisiert zu werden."}
  , {Catalog.CAT_USR_ANONYMIZED_LABEL,      "Anonymisierte Anmeldung"}
  , {Catalog.CAT_USR_ANONYMIZED_HINT,       "Der Anmeldename, der von dieser Identität verwendet wird, um die tatsächliche Identität bei ausgehender Kommunikation zu verschleiern."}
  , {Catalog.CAT_USR_PRINCIPAL_NAME_LABEL,  "Prinzipalname"}
  , {Catalog.CAT_USR_PRINCIPAL_NAME_HINT,   "Der Prinzipalname dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_PASSWORD_LABEL,        "Kennwort"}
  , {Catalog.CAT_USR_SAM_LABEL,             "Windows Anmeldung"}
  , {Catalog.CAT_USR_SAM_HINT,              "Der Anmeldename, der von dieser Identität verwendet wird, um durch Microsoft Windows authentiisert und authorisiert zu werden."}
  , {Catalog.CAT_USR_WELCOME_LABEL,         "Willkommen"}
  , {Catalog.CAT_USR_WELCOME_HINT,          "Soll dem Benutzer bei der nächsten Anmeldung eine Willkommensnachricht angezeigt werden?"}
  , {Catalog.CAT_USR_TYPE_TITLE,            "Verzeichnisdienst Mitarbeitertyp"}
  , {Catalog.CAT_USR_TYPE_LABEL,            "Mitarbeitertyp"}
  , {Catalog.CAT_USR_TYPE_HINT,             "Der Mitarbeitertyp dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_JOBROLE_LABEL,         "Job Rolle"}
  , {Catalog.CAT_USR_JOBROLE_HINT,          "Die Rolle/Der Rang, den die Identität in der Organisationsstruktur hat."}
  , {Catalog.CAT_USR_TITLE_LABEL,           "Titel"}
  , {Catalog.CAT_USR_TITLE_HINT,            "Der Anrede Titel dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_COMMON_NAME_LABEL,     "Allgemeiner Name"}
  , {Catalog.CAT_USR_COMMON_NAME_HINT,      "Der Allgemeine Name dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_FIRST_NAME_LABEL,      "Vorname"}
  , {Catalog.CAT_USR_FIRST_NAME_HINT,       "Der Vorname dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_LAST_NAME_LABEL,       "Nachname"}
  , {Catalog.CAT_USR_LAST_NAME_HINT,        "Der Nachname dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_MIDDLE_NAME_LABEL,     "Weitere Vornamen"}
  , {Catalog.CAT_USR_MIDDLE_NAME_HINT,      "Die weitere Vornamen dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_DISPLAY_NAME_LABEL,    "Anzeige Name"}
  , {Catalog.CAT_USR_DISPLAY_NAME_HINT,     "Der Anzeige Name dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_INITIALS_LABEL,        "Initialen"}
  , {Catalog.CAT_USR_INITIALS_HINT,         "Die initialen dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_SERVICE_ACCOUNT_LABEL, "Service Konto"}
  , {Catalog.CAT_USR_SERVICE_ACCOUNT_HINT,  "Kennzeichnet ob das Konto als Service Konto verwendet wird."}

  , {Catalog.CAT_USR_PARTICIPANT_LABEL,     "Teilnehmer"}
  , {Catalog.CAT_USR_PARTICIPANT_HINT,      "Der Teilnehmer dem dieses Benutzerkontos zugeordent ist."}
  , {Catalog.CAT_USR_DIVISION_LABEL,        "Geschäftsbereich"}
  , {Catalog.CAT_USR_DIVISION_HINT,         "Der Geschäftsbereich dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_ORGANIZATION_LABEL,    "Organisation"}
  , {Catalog.CAT_USR_ORGANIZATION_HINT,     "Die Organisation dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_DEPARTMENT_LABEL,      "Abteilung"}
  , {Catalog.CAT_USR_DEPARTMENT_HINT,       "Die Abteilung dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_OFFICE_LABEL,          "Büro"}
  , {Catalog.CAT_USR_OFFICE_HINT,           "Die Büro dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_MANAGER_LABEL,         "Manager"}
  , {Catalog.CAT_USR_MANAGER_HINT,          "Der Manager dieses Kontos, das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_COMPANY_LABEL,         "Firma"}
  , {Catalog.CAT_USR_COMPANY_HINT,          "Die Firma dieses Kontos, das mit der Identität verbunden ist."}

  , {Catalog.CAT_USR_EMAIL_LABEL,           "e-Mail"}
  , {Catalog.CAT_USR_EMAIL_HINT,            "Die e-Mail Adresse dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_PHONE_LABEL,           "Telefon"}
  , {Catalog.CAT_USR_PHONE_HINT,            "Die Telefon Nummer dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_FAX_LABEL,             "Facsimile"}
  , {Catalog.CAT_USR_FAX_HINT,              "Die Telefax Nummer dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_MOBILE_LABEL,          "Mobil"}
  , {Catalog.CAT_USR_MOBILE_HINT,           "Die Mobil Nummer dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_PAGER_LABEL,           "Pager"}
  , {Catalog.CAT_USR_PAGER_HINT,            "Die Pager Nummer dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_IPPHONE_LABEL,         "IP Telefon"}
  , {Catalog.CAT_USR_IPPHONE_HINT,          "Die IP Telefon Nummer dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_HOMEPHONE_LABEL,       "Telefon (privat)"}
  , {Catalog.CAT_USR_HOMEPHONE_HINT,        "Die private Telefon Nummer dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_POSTALCODE_LABEL,      "Postleitzahl"}
  , {Catalog.CAT_USR_POSTALCODE_HINT,       "Die Postleitzahl dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_POSTALADR_LABEL,       "Adresse"}
  , {Catalog.CAT_USR_POSTALADR_HINT,        "Die Postadresse dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_POBOX_LABEL,           "Post Box"}
  , {Catalog.CAT_USR_POBOX_HINT,            "Die Post Box dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_COUNTRY_LABEL,         "Land"}
  , {Catalog.CAT_USR_COUNTRY_HINT,          "Das Land dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_STATE_LABEL,           "Bundesland"}
  , {Catalog.CAT_USR_STATE_HINT,            "Das Bundesland dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_LOCALITY_LABEL,        "Lokation"}
  , {Catalog.CAT_USR_LOCALITY_HINT,         "Das Lokation dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_STREET_LABEL,          "Straße"}
  , {Catalog.CAT_USR_STREET_HINT,           "Die Straße dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_LANGUAGE_TITLE,        "Verzeichnisdienst Sprache"}
  , {Catalog.CAT_USR_LANGUAGE_LABEL,        "Sprache"}
  , {Catalog.CAT_USR_LANGUAGE_HINT,         "Die Sprache dieses Benutzerkontos das mit der Identität verbunden ist."}
  , {Catalog.CAT_USR_TIMEZONE_TITLE,        "Verzeichnisdienst Zeitzone"}
  , {Catalog.CAT_USR_TIMEZONE_LABEL,        "Zeitzone"}
  , {Catalog.CAT_USR_TIMEZONE_HINT,         "Die Zeitzone dieses Benutzerkontos das mit der Identität verbunden ist."}

  , {Catalog.CAT_FORM_NAME_LABEL,           "Name"}
  , {Catalog.CAT_UGP_NAME_HINT,             "Die Name der Gruppe"}
  , {Catalog.CAT_UGP_HEADER_TEXT,           "Gruppen"}
  , {Catalog.CAT_UGP_LOOKUP_TITLE,          "Gruppe"}
  , {Catalog.CAT_URL_NAME_HINT,             "Die Name der Rolle "}
  , {Catalog.CAT_URL_HEADER_TEXT,           "Rollen"}
  , {Catalog.CAT_URL_LOOKUP_TITLE,          "Rolle"}
  , {Catalog.CAT_ORL_HEADER_TEXT,           "Organisationen"}
  , {Catalog.CAT_ORL_LOOKUP_TITLE,          "Organisation"}

      /** Constant used to demarcate application entities related messages. */
  , {Catalog.ODS_USR_SERVER_LABEL,          "Directory Server"}
  , {Catalog.ODS_USR_SERVER_HINT,           "Die IT Resource welche die Konfiguration des Endpunktes zum Verzeichnisdienst bereitstellt"}
  , {Catalog.ODS_USR_PARENTDN_TITLE,        "Verzeichnisdienst Hierarchie"}
  , {Catalog.ODS_USR_PARENTDN_LABEL,        "Hierarchie"}
  , {Catalog.ODS_USR_PARENTDN_HINT,         "Hierarchie"}
  , {Catalog.ODS_USR_IDENTIFIER_HINT,       "The identifier used for this account to be identified in target directory."}
  , {Catalog.ODS_USR_LOGIN_NAME_HINT,       "The login name used for this account to be authenticated by Directory Service."}
  , {Catalog.ODS_USR_PASSWORD_HINT,         "Das Kennwort das verwendet wird, um sich mit dem Benutzerkonto im Verzeichnisdienst anzumelden."}
  , {Catalog.ODS_USR_COUNTRY_TITLE,         "Verzeichnisdienst Land"}
  , {Catalog.ODS_USR_LANGUAGE_TITLE,        "Verzeichnisdienst Sprache"}
  , {Catalog.ODS_USR_TIMEZONE_TITLE,        "Verzeichnisdienst Zeitzone"}
  , {Catalog.ODS_UGP_NAME_HINT,             "Die Name der Gruppe im Verzeichnisdienst."}
  , {Catalog.ODS_UGP_LOOKUP_TITLE,          "Verzeichnisdienst Gruppe"}
  , {Catalog.ODS_URL_NAME_HINT,             "Die Name der Rolle im Verzeichnisdienst."}
  , {Catalog.ODS_URL_LOOKUP_TITLE,          "Verzeichnisdienst Rolle"}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.FBS_USR_SERVER_LABEL,          "eFBS Dienst"}
  , {Catalog.FBS_USR_SERVER_HINT,           "Die IT Resource welche die Konfiguration des Endpunktes zum Provisionierungsdienst bereitstellt"}
  , {Catalog.FBS_USR_AID_LABEL,             "Benutzer Id"}
  , {Catalog.FBS_USR_AID_HINT,              "Die Id die verwendet wird, um ein Benutzerkonto in eFBS zu identifizieren."}
  , {Catalog.FBS_USR_UID_HINT,              "Der eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto bei eFBS anzumelden."}
  , {Catalog.FBS_USR_VALID_FROM_LABEL,      "Gültig von"}
  , {Catalog.FBS_USR_VALID_FROM_HINT,       "Beginn der Gültigkeisdauer dieses Benutzerkontos in eFBS"}
  , {Catalog.FBS_USR_VALID_TO_LABEL,        "Gültig bis"}
  , {Catalog.FBS_USR_VALID_TO_HINT,         "End der Gültigkeisdauer dieses Benutzerkontos in eFBS"}
  , {Catalog.FBS_USR_ADMIN_ACCOUNT_LABEL,   "Admin Konto"}
  , {Catalog.FBS_USR_ADMIN_ACCOUNT_HINT,    "Kennzeichnet ob das Konto als Administrator Konto verwendet wird."}
  , {Catalog.FBS_EML_HEADER_TEXT,           "e-Mail Adressen" }
  , {Catalog.FBS_EML_VALUE_LABEL,           "e-Mail" }
  , {Catalog.FBS_EML_VALUE_HINT,            "Eine e-Mail Adresse dieses Benutzerkontos das mit der Identität verbunden ist." }
  , {Catalog.FBS_PHN_HEADER_TEXT,           "Telefonnummern" }
  , {Catalog.FBS_PHN_VALUE_LABEL,           "Nummer" }
  , {Catalog.FBS_PHN_VALUE_HINT,            "Eine Telefon Nummer dieses Benutzerkontos das mit der Identität verbunden ist." }
  , {Catalog.FBS_REQ_EDU_LABEL,             "Schulung" }
  , {Catalog.FBS_REQ_PRD_LABEL,             "Wirkumgebung" }
  , {Catalog.FBS_REQ_FA_LABEL,              "Fachadministrator" }
  , {Catalog.FBS_REQ_SR_LABEL,              "Fachanwender Sonderrechte" }
  , {Catalog.FBS_REQ_SU_LABEL,              "Super User" }
  , {Catalog.FBS_REQ_GFA_LABEL,             "Globaler Fachadministrator" }
  , {Catalog.FBS_REQ_GSU_LABEL,             "Globaler Super User" }
  , {Catalog.FBS_REQ_GED_LABEL,             "Fachanwender GED" }

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.ADS_USR_SERVER_LABEL,          "Active Directory"}
  , {Catalog.ADS_USR_SERVER_HINT,           "Die IT Resource welche die Konfiguration des Endpunktes zum Provisionierungsdienst bereitstellt."}
  , {Catalog.ADS_USR_MAILREDIRECT_LABEL,    "Mail Umleitung"}
  , {Catalog.ADS_USR_MAILREDIRECT_HINT,     "Eine e-Mail Adresse zu welcher e-Mail's die an dieses Benutzerkonto gesendet werden weitergeleitet werden."}
  , {Catalog.ADS_USR_MUST_LABEL,            "Änderung"}
  , {Catalog.ADS_USR_MUST_HINT,             "Flag that indicates whether or not the account must change the password at next logon. If the value is true (check box is selected), then the account must change the password at next logon."}
  , {Catalog.ADS_USR_NEVER_LABEL,           "Kein Ablauf"}
  , {Catalog.ADS_USR_NEVER_HINT,            "Property to determine that the password for the account never expires."}
  , {Catalog.ADS_USR_LOCKED_LABEL,          "Gesperrt"}
  , {Catalog.ADS_USR_LOCKED_HINT,           "Specifies whether the account must be locked or unlocked"}
  , {Catalog.ADS_USR_PASSWORDNO_LABEL,      "Kennwort nicht erforderlich"}
  , {Catalog.ADS_USR_PASSWORDNO_HINT,       "Specifies whether or not Kennwort is required. If it is true, then there is no need to specify the password. If it is false, then password is required."}
  , {Catalog.ADS_USR_EXPIRATION_LABEL,      "Ablaufdatum"}
  , {Catalog.ADS_USR_EXPIRATION_HINT,       "The date when the account expires."}
  , {Catalog.ADS_USR_USRHOMEDIR_LABEL,      "Home Directory"}
  , {Catalog.ADS_USR_USRHOMEDIR_HINT,       "Home directory of the user."}
  , {Catalog.ADS_USR_TSSHOMEDIR_LABEL,      "Terminal Home Directory"}
  , {Catalog.ADS_USR_TSSHOMEDIR_HINT,       "Full path of the home directory for the Terminal Server account Sample value: C:\\MyDirectory. During a provisioning operation, you must enter the full, absolute path of the home directory, as shown in the sample value."}
  , {Catalog.ADS_USR_TSSLOGINALLOW_LABEL,   "Terminal Allow Login"}
  , {Catalog.ADS_USR_TSSLOGINALLOW_HINT,    "Specifies whether terminal server login is allowed. Enable this option to allow an account to log on to a terminal server."}
  , {Catalog.ADS_USR_TSSPROFILEPATH_LABEL,  "Terminal Profile Path"}
  , {Catalog.ADS_USR_TSSPROFILEPATH_HINT,   "Profile that is used when the account logs on to a Terminal Server. The profile can be roaming or mandatory. A roaming profile remains the same, regardless of the computer from which the account logs in. The account can make changes to a roaming profile, but not to a mandatory profile. Any changes a account makes while logged in with a mandatory profile are retained only for that Terminal Services session. The changes are lost when the user starts another Terminal Service ssession."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.PCF_USR_SVC_LABEL,             "Cloud Foundry Foundation"}
  , {Catalog.PCF_USR_SVC_HINT,              "Die IT Resource welche die Konfiguration des Endpunktes zu Pivotal Cloud Foundry Foundation bereitstellt."}
  , {Catalog.PCF_USR_SID_HINT,              "Der eindeutige Bezeichner diese Benutzerkontos den Pivotal Cloud Foundry Foundation verwendet, um das Benutzerkonto zu identifizieren."}
  , {Catalog.PCF_USR_UID_HINT,              "Der eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto bei Cloud Foundry Foundation anzumelden."}
  , {Catalog.PCF_USR_EID_LABEL,             "Externe Benutzerkennung"}
  , {Catalog.PCF_USR_EID_HINT,              "Benutzerkennung bei Authentifizierung durch einen externen Identitätsanbieter."}
  , {Catalog.PCF_USR_PWD_HINT,              "Das Kennwort das verwendet wird, um sich mit dem Benutzerkonto bei Cloud Foundry Foundation anzumelden."}
  , {Catalog.PCF_USR_OID_TITLE,             "Benutzerspeicher"}
  , {Catalog.PCF_USR_OID_LABEL,             "Benutzerspeicher"}
  , {Catalog.PCF_USR_OID_HINT,              "Der Alias des Identitätsanbieters, der diesen Benutzer authentifiziert hat. Der Wert 'uaa' gibt einen Benutzer aus dem internen Benutzerspeicher der UAA an."}
  , {Catalog.PCF_USR_IDP_TITLE,             "Identity Provider"}
  , {Catalog.PCF_USR_IDP_LABEL,             "Identity Provider"}
  , {Catalog.PCF_USR_IDP_HINT,              "Der Identity Provider, der diesen Benutzer authentisiert. Der Wert 'uaa' bezieht sich auf den internen Identity Provider."}
  , {Catalog.PCF_USR_VFD_LABEL,             "Verifiziert"}
  , {Catalog.PCF_USR_VFD_HINT,              "Neue Benutzer werden standardmäßig automatisch überprüft. Nicht verifizierte Benutzer können durch Angabe von verified: false erstellt werden. Wird wahr, wenn der Benutzer seine E-Mail-Adresse überprüft."}
  , {Catalog.PCF_UGP_SID_HINT,              "Der eindeutige Name der Gruppe, um diese Gruppe zu identifizieren."}
  , {Catalog.PCF_ORL_SID_HINT,              "Der eindeutige Name der Organisation, den Pivotal Cloud Foundry verwendet, um diese Organisation zu identifizieren."}
  , {Catalog.PCF_ORL_SCP_TITLE,             "Rolle in Organisation"}
  , {Catalog.PCF_ORL_SCP_LABEL,             "Rolle"}
  , {Catalog.PCF_ORL_SCP_HINT,              "Die Rolle die dieses Benutzerkonto in dieser Organisation einnimmet."}
  , {Catalog.PCF_SRL_HEADER,                "Räume"}
  , {Catalog.PCF_SRL_SID_TITLE,             "Raum"}
  , {Catalog.PCF_SRL_SID_HINT,              "Der eindeutige Name des Raums, den Pivotal Cloud Foundry verwendet, um diesen Raum zu identifizieren."}
  , {Catalog.PCF_SRL_SCP_TITLE,             "Rolle im Raum"}
  , {Catalog.PCF_SRL_SCP_LABEL,             "Rolle"}
  , {Catalog.PCF_SRL_SCP_HINT,              "Die Rolle die dieses Benutzerkonto in diesem Raum einnimmt."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.AJS_USR_SVC_LABEL,             "Atlassian Jira Server"}
  , {Catalog.AJS_USR_SVC_HINT,              "Die IT Resource welche die Konfiguration des Endpunktes zu Atlassian Jira Server bereitstellt"}
  , {Catalog.AJS_USR_UID_HINT,              "Der eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto in Atlassian Jira Server anzumelden."}
  , {Catalog.AJS_USR_PWD_HINT,              "Das Kennwort das verwendet wird, um sich mit dem Benutzerkonto in Atlassian Jira Server anzumelden."}
  , {Catalog.AJS_GRP_GID_HINT,              "Der eindeutige Name der Gruppe, den Atlassian Jira Server verwendet, um diese Gruppe zu identifizieren."}
  , {Catalog.AJS_PRJ_HEADER,                "Projekte"}
  , {Catalog.AJS_PRJ_PID_TITLE,             "Projekt"}
  , {Catalog.AJS_PRJ_PID_HINT,              "Der eindeutige Name des Projekts, den Atlassian Jira Server verwendet, um diese Projekt zu identifizieren."}
  , {Catalog.AJS_PRJ_RID_HINT,              "Die Rolle, die dieses Benutzerkonto im Projekt einnimmt."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.GAE_USR_SVC_LABEL,             "Google Apigee Edge"}
  , {Catalog.GAE_USR_SVC_HINT,              "Die IT Resource welche die Konfiguration des Endpunktes zum Provisionierungsdienst bereitstellt."}
  , {Catalog.GAE_USR_OID_LABEL,             "Organisation"}
  , {Catalog.GAE_USR_OID_HINT,              "Organisation"}
  , {Catalog.GAE_USR_UID_LABEL,             "Benutzerkennung"}
  , {Catalog.GAE_USR_UID_HINT,              "Der eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto bei Google API Gateway anzumelden."}
  , {Catalog.GAE_USR_UPN_LABEL,             "e-Mail"}
  , {Catalog.GAE_USR_UPN_HINT,              "Die e-Mail Adresse des Kontos in Google Apigee Edge."}
  , {Catalog.GAE_USR_PWD_HINT,              "Das Kennwort das verwendet wird, um sich mit dem Benutzerkonto bei Google API Gateway anzumelden."}
  , {Catalog.GAE_URL_SID_LABEL,             "Scope"}
  , {Catalog.GAE_URL_SID_HINT,              "Der eindeutige Name der Organisation, den Google API Gateway verwendet, um diese Organisation zu identifizieren."}
  , {Catalog.GAE_URL_RID_TITLE,             "Rolle in Organisation"}
  , {Catalog.GAE_URL_RID_HINT,              "Die Rolle, die dieses Benutzerkonto in der Organisation einnimmt."}
  , {Catalog.GAE_DAP_HEADER,                "Applikationen"}
  , {Catalog.GAE_DAP_SID_TITLE,             "Applikation"}
  , {Catalog.GAE_DAP_SID_HINT,              "Der eindeutige Name der Applikation in Google Apigee Edge die dem Enwickler zugewiesen sit."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.OFS_USR_SVC_LABEL,             "openfire™ Database Server"}
  , {Catalog.OFS_USR_SVC_HINT,              "Die IT Resource welche die Konfiguration des Endpunktes zum openfire™ Database Server."}
  , {Catalog.OFS_USR_UID_LABEL,             "Benutzerkennung"}
  , {Catalog.OFS_USR_UID_HINT,              "Der eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto an der openfire™ XMPP Domain anzumelden."}
  , {Catalog.OFS_USR_PWD_HINT,              "Das Kennwort das verwendet wird, um sich mit dem Benutzerkonto an der openfire™ XMPP Domain anzumelden."}
  , {Catalog.OFS_USR_ADM_LABEL,             "Administrator"}
  , {Catalog.OFS_USR_ADM_HINT,              "Berechtigung zur Administration der openfire™ XMPP-Domäne über die Verwaltungskonsole."}
  , {Catalog.OFS_UGP_GID_HINT,              "Der eindeutige Name der Gruppe, den die openfire™ XMPP Domain verwendet, um diese Gruppe zu identifizieren."}
  , {Catalog.OFS_UGP_ADM_LABEL,             "Administrator"}
  , {Catalog.OFS_UGP_ADM_HINT,              "Die Berechtigung gibt bestimmten Benutzern Administratorrechte über die Gruppe. "}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.MDL_USR_SVC_LABEL,             "Moodle Service"}
  , {Catalog.MDL_USR_SVC_HINT,              "Die IT Resource welche die Konfiguration des Endpunktes zu Moodle bereitstellt."}
  , {Catalog.MDL_USR_UID_HINT,              "Der eindeutige Bezeichner diese Benutzerkontos das der Verzeichnisdienst verwendet, um das Benutzerkonto zu identifizieren."}
  , {Catalog.MDL_USR_UPN_HINT,              "Der eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto bei Moodle anzumelden."}
  , {Catalog.MDL_USR_PWD_HINT,              "Das Kennwort das verwendet wird, um sich mit dem Benutzerkonto bei Moodle anzumelden."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.UPC_USR_SVC_LABEL,             "Universal Police Client"}
  , {Catalog.UPC_USR_SVC_HINT,              "Die IT Resource welche die Konfiguration des Endpunktes zu Universal Police Client bereitstellt."}
  , {Catalog.UPC_USR_SID_HINT,              "Der eindeutige Bezeichner diese Benutzerkontos den Universal Police Client verwendet, um das Benutzerkonto zu identifizieren."}
  , {Catalog.UPC_USR_UID_HINT,              "Der eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto bei Universal Police Client anzumelden."}
  , {Catalog.UPC_USR_PWD_HINT,              "Das Kennwort das verwendet wird, um sich mit dem Benutzerkonto bei Universal Police Client anzumelden."}
  , {Catalog.UPC_UGP_SID_HINT,              "Der eindeutige Name der Gruppe, um diese Gruppe zu identifizieren."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.IGS_USR_SVC_LABEL,             "Service Endpunkt"}
  , {Catalog.IGS_USR_SVC_HINT,              "Die IT Resource welche die Konfiguration des Endpunktes zu Identity Governance bereitstellt."}
  , {Catalog.IGS_USR_SID_HINT,              "Der eindeutige Bezeichner diese Benutzerkontos den Identity Governance verwendet, um das Benutzerkonto zu identifizieren."}
  , {Catalog.IGS_USR_UID_HINT,              "Der eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto bei Identity Governance anzumelden."}
  , {Catalog.IGS_USR_PWD_HINT,              "Das Kennwort das verwendet wird, um sich mit dem Benutzerkonto bei Identity Governance anzumelden."}
  , {Catalog.IGS_URL_SID_HINT,              "Der eindeutige Name der Rolle, um die Rolle zu zuweisen."}
  , {Catalog.IGS_UTN_HEADER,                "Mandanten"}
  , {Catalog.IGS_UTN_SID_TITLE,             "Mandant"}
  , {Catalog.IGS_UTN_SID_HINT,              "Der eindeutige Name des Mandanten, den Identity Governance verwendet, um diesen Mandanten zu zuweisen."}
  , {Catalog.IGS_UTN_RID_TITLE,             "Rollen im Mandanten"}
  , {Catalog.IGS_UTN_RID_HINT,              "Die Rolle die dieses Benutzerkonto in dem Mandanten beansprucht."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.OIG_USR_SVC_LABEL,             "Service Endpoint"}
  , {Catalog.OIG_USR_SVC_HINT,              "Die IT Resource welche die Konfiguration des Endpunktes zu Identity Governance bereitstellt."}
  , {Catalog.OIG_USR_SID_HINT,              "Der eindeutige Bezeichner diese Benutzerkontos den Identity Governance verwendet, um das Benutzerkonto zu identifizieren."}
  , {Catalog.OIG_USR_UID_HINT,              "er eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto bei Identity Governance anzumelden."}
  , {Catalog.OIG_UPR_HEADER,                "Systemrollen"}
  , {Catalog.OIG_UPR_SID_TITLE,             "Identity Governance Systemrolle"}
  , {Catalog.OIG_UPR_SID_HINT,              "Der eindeutige Name der Systemrolle, um die Systemrolle zu zuweisen."}
  , {Catalog.OIG_UPG_HEADER,                "Globale Rolen"}
  , {Catalog.OIG_UPG_SID_TITLE,             "Identity Governance Globale Rolle"}
  , {Catalog.OIG_UPG_SID_HINT,              "Der eindeutige Name der globalen Rolle, um die globalen Rolle zu zuweisen."}
  , {Catalog.OIG_UPS_HEADER,                "Scoped Roles"}
  , {Catalog.OIG_UPS_SID_TITLE,             "Identity Governance Beschränkte Rolle"}
  , {Catalog.OIG_UPS_SID_HINT,              "Der eindeutige Name der beschränkte Rolle, um die beschränkte Rolle zu zuweisen."}
  , {Catalog.OIG_UPS_SCP_TITLE,             "Geltungsbereich Organisation"}
  , {Catalog.OIG_UPS_SCP_LABEL,             "Geltungsbereich"}
  , {Catalog.OIG_UPS_SCP_HINT,              "Der organisatorsiche Geltungsbereich für den die Beschränkung der Rolle gültig ist."}
  , {Catalog.OIG_UPS_HRC_LABEL,             "Hierarchisch"}
  , {Catalog.OIG_UPS_HRC_HINT,              "Die Berechtigung ist gültig für die gesamte Hierarchie unterhalb der gewählten Organisation oder nur für die Organisation."}

   /** Constant used to demarcate application entities related messages. */
  , {Catalog.BDS_USR_SERVER_LABEL,          "Directory Server"}
  , {Catalog.BDS_USR_SERVER_HINT,           "Die IT Resource welche die Konfiguration des Endpunktes zum Verzeichnisdienst bereitstellt"}

  /** Constant used to demarcate RedHat Keycloak application entities related messages. */
  , {Catalog.RKC_USR_ACTION_HEADER,         "Erforderliche Aktionen"}
  , {Catalog.RKC_USR_CREDENTIAL_HEADER,     "Deaktivierte Kennwörter"}
  , {Catalog.RKC_USR_SERVER_LABEL,          "Keycloak Sever"}
  , {Catalog.RKC_USR_SERVER_HINT,           "Die IT Resource welche die Konfiguration des Endpunktes zum Keycloak Dienst bereitstellt"}
  , {Catalog.RKC_USR_SID_HINT,              "Der eindeutige Bezeichner diese Benutzerkontos das der Keycloak Dienst verwendet, um das Benutzerkonto zu identifizieren."}
  , {Catalog.RKC_USR_NAME_HINT,             "Der eindeutige Name der Kennung die verwendet wird, um sich mit dem Benutzerkonto im Keycloak Dienst anzumelden."}
  , {Catalog.RKC_USR_PASSWORD_HINT,         "Das Kennwort das verwendet wird, um sich mit dem Benutzerkonto im Keycloak Dienst anzumelden."}
  , {Catalog.RKC_USR_VERIFIED_LABEL,        "Verifiziert"}
  , {Catalog.RKC_USR_VERIFIED_HINT,         "Attribut, das angibt, ob das Konto auf dem Keycloak-Server verifiziert ist."}
  , {Catalog.RKC_USR_OTP_LABEL,             "Ein Einmalpasswort"}
  , {Catalog.RKC_USR_OTP_HINT,              "Attribut, das den OTP-Status des Kontos auf dem Keycloak-Server angibt."}
  , {Catalog.RKC_USR_NOTBEF_LABEL,          "Nicht Bevor"}
  , {Catalog.RKC_USR_NOTBEF_HINT,           "Das Nicht vor -Attribut dieses Kontos."}
  , {Catalog.RKC_USR_AEV_LABEL,             "E-Mail bestätigen"}
  , {Catalog.RKC_USR_AEV_HINT,              "Die Aktion E-Mail überprüfen kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_ADA_LABEL,             "Konto Löschen"}
  , {Catalog.RKC_USR_ADA_HINT,              "Die Aktion Konto löschen kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_APV_LABEL,             "Profil Verifizieren"}
  , {Catalog.RKC_USR_APV_HINT,              "Die Aktion Profil überprüfen kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_APU_LABEL,             "Profil Aktualisieren"}
  , {Catalog.RKC_USR_APU_HINT,              "Die Aktion Profil aktualisieren kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_AUP_LABEL,             "Kennwort Aktualisieren"}
  , {Catalog.RKC_USR_AUP_HINT,              "Die Aktion Passwort aktualisieren kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_ALU_LABEL,             "Gebietsschema Aktualisieren"}
  , {Catalog.RKC_USR_ALU_HINT,              "Die Aktion Gebietsschema aktualisieren kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_ACO_LABEL,             "OTP konfigurieren"}
  , {Catalog.RKC_USR_ACO_HINT,              "Die Aktion Konfiguration der OTP kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_ATC_LABEL,             "Allgemeine Geschäftsbedingungen"}
  , {Catalog.RKC_USR_ATC_HINT,              "Die Aktion Allgemeine Geschäftsbedingunge kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_AWR_LABEL,             "Authentizitätsregistrierung"}
  , {Catalog.RKC_USR_AWR_HINT,              "Die Authentisierungsregistrierungsaktion kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_AWP_LABEL,             "Authn-Passwort"}
  , {Catalog.RKC_USR_AWP_HINT,              "Die Authentizitätskennwortaktion kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_USR_ADC_LABEL,             "Anmeldeinformationen löschen"}
  , {Catalog.RKC_USR_ADC_HINT,              "Die Aktion Anmeldeinformationen löschen kann vom Keycloak-Server erzwungen werden."}
  , {Catalog.RKC_UGR_LOOKUP_TITLE,          "Keycloak Gruppe"}
  , {Catalog.RKC_UGR_NAME_HINT,             "Der Name der Gruppe im Keycloak-Server."}
  , {Catalog.RKC_URR_HEADER_TEXT,           "Realm Rollen"}
  , {Catalog.RKC_URR_LOOKUP_TITLE,          "Keycloak Realm Rolle"}
  , {Catalog.RKC_URR_NAME_HINT,             "Der Name der Realm-Rolle im Keycloak-Server."}
  , {Catalog.RKC_UCR_HEADER_TEXT,           "Client Rollen"}
  , {Catalog.RKC_UCR_LOOKUP_TITLE,          "Keycloak Client Rolle"}
  , {Catalog.RKC_UCR_NAME_HINT,             "Der Name der Client-Rolle im Keycloak-Server."}
  
  /** Constant used to demarcate PLX application entities related messages. */
  , {Catalog.PLX_USR_SERVER_LABEL,          "PLX LDAP-Server"}
  , {Catalog.PLX_USR_SERVER_HINT,           "Die IT-Ressource, die die Endpunktkonfiguration für den Server angibt, auf dem der PLX LDAP-Server bereitgestellt wird"}
  , {Catalog.PLX_USR_SID_LABEL,             "SID"}
  , {Catalog.PLX_USR_SID_HINT,              "Die SID des Eintrags, der mit diesem Konto verbunden ist."}
  , {Catalog.PLX_USR_TENANT_LABEL,          "Mandant"}
  , {Catalog.PLX_USR_TENANT_HINT,           "Die Hauptorganisation, mit der dieses Konto verbunden ist."}
  , {Catalog.PLX_USR_DN_LABEL,              "Unterscheidungsname"}
  , {Catalog.PLX_USR_DN_HINT,               "Der DN dieses Kontos, der mit der Identität verbunden ist."}
  , {Catalog.PLX_USR_PASSWORD_HINT,         "Das Passwort, das für dieses Konto verwendet wird, um von PLX authentifiziert zu werden."}
  , {Catalog.PLX_USR_EXT_ATTR_HEADER,       "Erweiterungsattributs"}
  , {Catalog.PLX_USR_EXT_ATTR1_LABEL,       "Erweiterungsattribut 1"}
  , {Catalog.PLX_USR_EXT_ATTR1_HINT,        "Zusätzliche Informationen über den Standardsatz von Attributen hinaus"}
  , {Catalog.PLX_USR_EXT_ATTR3_LABEL,       "Erweiterungsattribut 3"}
  , {Catalog.PLX_USR_EXT_ATTR3_HINT,        "Zusätzliche Informationen über den Standardsatz von Attributen hinaus"}
  , {Catalog.PLX_USR_EXT_ATTR4_LABEL,       "Erweiterungsattribut 4"}
  , {Catalog.PLX_USR_EXT_ATTR4_HINT,        "Zusätzliche Informationen über den Standardsatz von Attributen hinaus"}
  , {Catalog.PLX_USR_EXT_ATTR6_LABEL,       "Erweiterungsattribut 6"}
  , {Catalog.PLX_USR_EXT_ATTR6_HINT,        "Zusätzliche Informationen über den Standardsatz von Attributen hinaus"}
  , {Catalog.PLX_USR_OBJ_SID_LABEL,         "Objekt-SID"}
  , {Catalog.PLX_USR_OBJ_SID_HINT,          "Die Objekt-SID dieses Kontos, das mit der Identität verbunden ist."}
  , {Catalog.PLX_USR_BUSINESS_CAT_LABEL,    "Geschäftskategorie"}
  , {Catalog.PLX_USR_BUSINESS_CAT_HINT,     "Die Geschäftskategorie dieses Kontos, das mit der Identität verbunden ist."}
  , {Catalog.PLX_USR_CAR_LICENCE_LABEL,     "Führerschein"}
  , {Catalog.PLX_USR_CAR_LICENCE_HINT,      "Der Führerschein dieses Kontos."}
  , {Catalog.PLX_USR_DEPARTMENT_NUM_LABEL,  "Abteilungsnummer"}
  , {Catalog.PLX_USR_DEPARTMENT_NUM_HINT,   "Die Abteilungsnummer dieses Kontos, das mit der Identität verbunden ist."}
  , {Catalog.PLX_USR_EMPLOYEE_NUM_LABEL,    "Mitarbeiternummer"}
  , {Catalog.PLX_USR_EMPLOYEE_NUM_HINT,     "Die Mitarbeiternummer dieses Kontos."}
  , {Catalog.PLX_USR_HOME_POST_ADDR_LABEL,  "Heimatanschrift"}
  , {Catalog.PLX_USR_HOME_POST_ADDR_HINT,   "Die Heimatanschrift dieses Kontos, das mit der Identität verbunden ist."}
  , {Catalog.PLX_USR_ORG_NAME_LABEL,        "Organisationsname"}
  , {Catalog.PLX_USR_ORG_NAME_HINT,         "Der Organisationsname dieses Kontos, das mit der Identität verbunden ist."}
  , {Catalog.PLX_USR_PREFERENCE_HEADER,     "Einstellungen"}
  , {Catalog.PLX_USR_PREF_LANG_LABEL,       "Bevorzugte Sprache"}
  , {Catalog.PLX_USR_PREF_LANG_HINT,        "Die bevorzugte Sprache dieses Kontos, das mit der Identität verbunden ist."}
  , {Catalog.PLX_USR_ROOM_NUM_LABEL,        "Raumnummer"}
  , {Catalog.PLX_USR_ROOM_NUM_HINT,         "Die Raumnummer dieses Kontos."}
  , {Catalog.PLX_USR_SECRETARY_LABEL,       "Sekretär/Sekretärin"}
  , {Catalog.PLX_USR_SECRETARY_HINT,        "Der Sekretär/die Sekretärin dieses Kontos."}
  , {Catalog.PLX_USR_UID_LABEL,             "Benutzerkennung (UID)"}
  , {Catalog.PLX_USR_UID_HINT,              "Die Benutzerkennung dieses Kontos."}
  , {Catalog.PLX_USR_DESC_LABEL,            "Beschreibung"}
  , {Catalog.PLX_USR_DESC_HINT,             "Die Beschreibung dieses Kontos, das mit der Identität verbunden ist."}
  , {Catalog.PLX_UGR_LOOKUP_TITLE,          "PLX Gruppe"}
  , {Catalog.PLX_UGR_NAME_HINT,             "Der DN der Gruppe in der PLX."}
  , {Catalog.PLX_UPX_HEADER_TEXT,           "Proxy"}
  , {Catalog.PLX_UPX_LOOKUP_TITLE,          "PLX Proxy"}
  , {Catalog.PLX_UPX_NAME_HINT,             "Der DN der Proxy in der PLX."}
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