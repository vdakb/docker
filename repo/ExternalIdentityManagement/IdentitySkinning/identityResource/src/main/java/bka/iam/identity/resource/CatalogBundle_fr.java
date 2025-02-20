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

    File        :   CatalogBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package bka.iam.identity.resource;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class CatalogBundle_fr
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code french
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CatalogBundle_fr extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     /** Constant used to demarcate application entities related messages. */
    {Catalog.CAT_RISK_LABEL,                "Niveau de risque"}
  , {Catalog.CAT_RISK_HINT,                 "Niveau de risque"}
  , {Catalog.CAT_ROLE_OWNER_LABEL,          "Propriétaire du rôle"}
  , {Catalog.CAT_ROLE_OWNER_HINT,           "Propriétaire du rôle"}
  , {Catalog.CAT_ROLE_DESCRIPTION_LABEL,    "Description du rôle"}
  , {Catalog.CAT_ROLE_DESCRIPTION_HINT,     "Description du rôle"}
  , {Catalog.CAT_VALID_FROM_LABEL,          "Valide à partir de"}
  , {Catalog.CAT_VALID_FROM_HINT,           "Valide à partir de"}
  , {Catalog.CAT_PRIVILEGE_ACTION_LABEL,    "Action"}
  , {Catalog.CAT_PRIVILEGE_ACTION_HINT,     "Action"}
  , {Catalog.CAT_PRIVILEGE_ASSIGN_LABEL,    "Attribuer un privilège"}
  , {Catalog.CAT_PRIVILEGE_ASSIGN_HINT,     "Attribuer un privilège"}
  , {Catalog.CAT_PRIVILEGE_REVOKE_LABEL,    "Révoquer le privilège "}
  , {Catalog.CAT_PRIVILEGE_REVOKE_LABEL,    "Révoquer le privilège "}
  , {Catalog.CAT_BASIC_HEADER,              "Informations de base"}
  , {Catalog.CAT_ACCOUNT_HEADER,            "Paramètres du compte"}
  , {Catalog.CAT_CONTACT_HEADER,            "Informations de contact"}
  , {Catalog.CAT_ORGANIZATION_HEADER,       "Information organisationnelle"}
  , {Catalog.CAT_MISCELLANEOUS_HEADER,      "Informations diverses"}

    /* Constant used to demarcate common application entities related messages. */
  , {Catalog.CAT_USR_IDENTIFIER_LABEL,      "Identifiant"}
  , {Catalog.CAT_USR_LOGIN_NAME_LABEL,      "Connexion unifiée"}
  , {Catalog.CAT_USR_UNIFIED_LABEL,         "Connexion unifiée"}
  , {Catalog.CAT_USR_UNIFIED_HINT,          "Le login utilisé par cette identité pour être authentifié et autorisé par les applications et services P20."}
  , {Catalog.CAT_USR_ANONYMIZED_LABEL,      "Connexion anonyme"}
  , {Catalog.CAT_USR_ANONYMIZED_HINT,       "Le nom de connexion utilisé par cette identité pour masquer la véritable identité dans les communications sortantes."}
  , {Catalog.CAT_USR_PRINCIPAL_NAME_LABEL,  "Nom principal"}
  , {Catalog.CAT_USR_PRINCIPAL_NAME_HINT,   "Nom d'utilisateur principal de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_PASSWORD_LABEL,        "Mot de passe"}
  , {Catalog.CAT_USR_SAM_LABEL,             "Connexion Windows"}
  , {Catalog.CAT_USR_SAM_HINT,              "Le nom de connexion Windows de ce compte associé à l’identité."}
  , {Catalog.CAT_USR_WELCOME_LABEL,         "Bienvenu"}
  , {Catalog.CAT_USR_WELCOME_HINT,          "L'utilisateur doit-il recevoir un message de bienvenue lors de sa prochaine connexion?"}
  , {Catalog.CAT_USR_TYPE_TITLE,            "Type d'employé"}
  , {Catalog.CAT_USR_TYPE_LABEL,            "Type"}
  , {Catalog.CAT_USR_TYPE_HINT,             "Le type d'employé de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_JOBROLE_LABEL,         "Rôle de l'emploi"}
  , {Catalog.CAT_USR_JOBROLE_HINT,          "Le rôle/rang que l'identité a dans la structure organisationnelle."}
  , {Catalog.CAT_USR_TITLE_LABEL,           "Titre"}
  , {Catalog.CAT_USR_TITLE_HINT,            "Le titre de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_COMMON_NAME_LABEL,     "Nom commun"}
  , {Catalog.CAT_USR_COMMON_NAME_HINT,      "Nom commun de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_FIRST_NAME_LABEL,      "Prénom"}
  , {Catalog.CAT_USR_FIRST_NAME_HINT,       "Le prénom de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_LAST_NAME_LABEL,       "Nom de famille"}
  , {Catalog.CAT_USR_LAST_NAME_HINT,        "Nom de famille de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_MIDDLE_NAME_LABEL,     "Deuxième nom"}
  , {Catalog.CAT_USR_MIDDLE_NAME_HINT,      "Le deuxième prénom de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_DISPLAY_NAME_LABEL,    "Afficher un nom"}
  , {Catalog.CAT_USR_DISPLAY_NAME_HINT,     "Nom d'affichage de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_INITIALS_LABEL,        "Initiales"}
  , {Catalog.CAT_USR_INITIALS_HINT,         "Les initiales de ce compte associ� � l'identit�."}
  , {Catalog.CAT_USR_SERVICE_ACCOUNT_LABEL, "Compte de service"}
  , {Catalog.CAT_USR_SERVICE_ACCOUNT_HINT,  "Que ce compte soit un compte de service ou non."}

  , {Catalog.CAT_USR_PARTICIPANT_LABEL,     "Participant"}
  , {Catalog.CAT_USR_PARTICIPANT_HINT,      "Le participant auquel ce compte utilisateur est attribué."}
  , {Catalog.CAT_USR_DIVISION_LABEL,        "Division"}
  , {Catalog.CAT_USR_DIVISION_HINT,         "Division de ce compte utilisateur associé à l'identité."}
  , {Catalog.CAT_USR_ORGANIZATION_LABEL,    "Organisation"}
  , {Catalog.CAT_USR_ORGANIZATION_HINT,     "L'unité organisationnelle de ce compte utilisateur associé à l'identité."}
  , {Catalog.CAT_USR_DEPARTMENT_LABEL,      "Département"}
  , {Catalog.CAT_USR_DEPARTMENT_HINT,       "Département de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_OFFICE_LABEL,          "Bureau"}
  , {Catalog.CAT_USR_OFFICE_HINT,           "Le bureau de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_MANAGER_LABEL,         "Directeur"}
  , {Catalog.CAT_USR_MANAGER_HINT,          "Le gestionnaire de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_COMPANY_LABEL,         "Company"}
  , {Catalog.CAT_USR_COMPANY_HINT,          "The company of this account associated with the identity."}

  , {Catalog.CAT_USR_EMAIL_LABEL,           "e-Mail"}
  , {Catalog.CAT_USR_EMAIL_HINT,            "L'adresse e-mail de ce compte associ� � l'identit�."}
  , {Catalog.CAT_USR_PHONE_LABEL,           "Téléphone"}
  , {Catalog.CAT_USR_PHONE_HINT,            "Le numéro de téléphone de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_FAX_LABEL,             "Facsimilé"}
  , {Catalog.CAT_USR_FAX_HINT,              "Le numéro de télécopie de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_MOBILE_LABEL,          "Mobile"}
  , {Catalog.CAT_USR_MOBILE_HINT,           "Le numéro de mobile de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_PAGER_LABEL,           "Pager"}
  , {Catalog.CAT_USR_PAGER_HINT,            "Le numéro de pager de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_IPPHONE_LABEL,         "Téléphone IP"}
  , {Catalog.CAT_USR_IPPHONE_HINT,          "Le numéro de téléphone IP de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_HOMEPHONE_LABEL,       "Téléphone fixe"}
  , {Catalog.CAT_USR_HOMEPHONE_HINT,        "Le numéro de téléphone résidentiel de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_POSTALCODE_LABEL,      "Code postale"}
  , {Catalog.CAT_USR_POSTALCODE_HINT,       "Le code postal de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_POSTALADR_LABEL,       "Adresse postale"}
  , {Catalog.CAT_USR_POSTALADR_HINT,        "L'adresse postale de ce compte associée à l'identité."}
  , {Catalog.CAT_USR_POBOX_LABEL,           "Boîte aux lettres"}
  , {Catalog.CAT_USR_POBOX_HINT,            "La boîte aux lettres de ce compte associée à l'identité."}
  , {Catalog.CAT_USR_COUNTRY_LABEL,         "Pays"}
  , {Catalog.CAT_USR_COUNTRY_HINT,          "Le pays de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_STATE_LABEL,           "État"}
  , {Catalog.CAT_USR_STATE_HINT,            "L'état de ce compte associé à l'identité."}
  , {Catalog.CAT_USR_LOCALITY_LABEL,        "Localité"}
  , {Catalog.CAT_USR_LOCALITY_HINT,         "La localité de ce compte associée à l'identité."}
  , {Catalog.CAT_USR_STREET_LABEL,          "Rue"}
  , {Catalog.CAT_USR_STREET_HINT,           "La rue de ce compte associée à l'identité."}
  , {Catalog.CAT_USR_LANGUAGE_TITLE,        "Langue du répertoire"}
  , {Catalog.CAT_USR_LANGUAGE_LABEL,        "Langue"}
  , {Catalog.CAT_USR_LANGUAGE_HINT,         "La langue de ce compte associée à l'identité."}
  , {Catalog.CAT_USR_TIMEZONE_TITLE,        "Fuseau horaire du répertoire"}
  , {Catalog.CAT_USR_TIMEZONE_LABEL,        "Fuseau horaire"}
  , {Catalog.CAT_USR_TIMEZONE_HINT,         "Fuseau horaire de ce compte associé à l'identité."}

  , {Catalog.CAT_FORM_NAME_LABEL,           "Nom"}
  , {Catalog.CAT_UGP_NAME_HINT,             "Le nom distinctif si le groupe dans le répertoire."}
  , {Catalog.CAT_UGP_HEADER_TEXT,           "Groupes"}
  , {Catalog.CAT_UGP_LOOKUP_TITLE,          "Groupe"}
  , {Catalog.CAT_URL_NAME_HINT,             "Le nom distinctif si le rôle dans le répertoire."}
  , {Catalog.CAT_URL_HEADER_TEXT,           "Les rôles"}
  , {Catalog.CAT_URL_LOOKUP_TITLE,          "Rôle"}
  , {Catalog.CAT_ORL_HEADER_TEXT,           "Organisations"}
  , {Catalog.CAT_ORL_LOOKUP_TITLE,          "Organisation"}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.ODS_USR_SERVER_LABEL,          "Serveur d'annuaire"}
  , {Catalog.ODS_USR_SERVER_HINT,           "La ressource informatique spécifiant la configuration du point de terminaison au serveur sur lequel le service d'annuaire est déployé"}
  , {Catalog.ODS_USR_PARENTDN_TITLE,        "Hiérarchie des répertoires"}
  , {Catalog.ODS_USR_PARENTDN_LABEL,        "Hiérarchie"}
  , {Catalog.ODS_USR_PARENTDN_HINT,         "Hiérarchie"}
  , {Catalog.ODS_USR_IDENTIFIER_HINT,       "L'identifiant utilisé pour que ce compte soit identifié dans le répertoire cible."}
  , {Catalog.ODS_USR_LOGIN_NAME_HINT,       "Nom de connexion utilisé pour que ce compte soit authentifié par le service d'annuaire."}
  , {Catalog.ODS_USR_PASSWORD_HINT,         "Mot de passe utilisé pour authentifier ce compte par le service d'annuaire."}
  , {Catalog.ODS_USR_COUNTRY_TITLE,         "Pays du répertoire"}
  , {Catalog.ODS_USR_LANGUAGE_TITLE,        "Langue du répertoire"}
  , {Catalog.ODS_USR_TIMEZONE_TITLE,        "Fuseau horaire du répertoire"}
  , {Catalog.ODS_UGP_NAME_HINT,             "Le nom distinctif si le groupe dans le répertoire."}
  , {Catalog.ODS_UGP_LOOKUP_TITLE,          "Groupe d'répertoire"}
  , {Catalog.ODS_URL_NAME_HINT,             "Le nom distinctif si le rôle dans le répertoire."}
  , {Catalog.ODS_URL_LOOKUP_TITLE,          "Rôle d'répertoire"}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.FBS_USR_SERVER_LABEL,          "Service eFBS"}
  , {Catalog.FBS_USR_SERVER_HINT,           "La ressource informatique spécifiant la configuration du point de terminaison au serveur sur lequel le service de provisionnement est déployé."}
  , {Catalog.FBS_USR_AID_LABEL,             "Identifiant d'utilisateur"}
  , {Catalog.FBS_USR_AID_HINT,              "L'identifiant unique de ce compte utilisateur qu' eFBS utilise pour identifier le compte utilisateur."}
  , {Catalog.FBS_USR_UID_HINT,              "Le nom unique utilisé pour que ce compte soit authentifié par eFBS."}
  , {Catalog.FBS_USR_VALID_FROM_LABEL,      "Valide à partir de"}
  , {Catalog.FBS_USR_VALID_FROM_HINT,       "Date de début de la période de validité de ce compte dans eFBS."}
  , {Catalog.FBS_USR_VALID_TO_LABEL,        "Valable jusqu'au"}
  , {Catalog.FBS_USR_VALID_TO_HINT,         "Date de fin de la période de validité de ce compte dans eFBS."}
  , {Catalog.FBS_USR_ADMIN_ACCOUNT_LABEL,   "Compte administrateur"}
  , {Catalog.FBS_USR_ADMIN_ACCOUNT_HINT,    "Que ce compte soit un compte administrateur ou non."}
  , {Catalog.FBS_EML_HEADER_TEXT,           "Adresses eMail"}
  , {Catalog.FBS_EML_VALUE_LABEL,           "eMail"}
  , {Catalog.FBS_EML_VALUE_HINT,            "Une adresse e-mail de ce compte associée à l'identité."}
  , {Catalog.FBS_PHN_HEADER_TEXT,           "Numéros de téléphone"}
  , {Catalog.FBS_PHN_VALUE_LABEL,           "Nombre"}
  , {Catalog.FBS_PHN_VALUE_HINT,            "Un numéro de téléphone de ce compte associé à l'identité."}
  , {Catalog.FBS_REQ_EDU_LABEL,             "Éducation"}
  , {Catalog.FBS_REQ_PRD_LABEL,             "Production"}
  , {Catalog.FBS_REQ_FA_LABEL,              "Administrateur"}
  , {Catalog.FBS_REQ_SR_LABEL,              "Droits spéciaux d'un Administrateur"}
  , {Catalog.FBS_REQ_SU_LABEL,              "Super Utilisateur"}
  , {Catalog.FBS_REQ_GFA_LABEL,             "Administrateur Global"}
  , {Catalog.FBS_REQ_GSU_LABEL,             "Super Utilisateur Global"}
  , {Catalog.FBS_REQ_GED_LABEL,             "Administrateur GED"}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.ADS_USR_SERVER_LABEL,          "Active Directory"}
  , {Catalog.ADS_USR_SERVER_HINT,           "La ressource informatique spécifiant la configuration du noeud final sur le serveur sur lequel le service d'approvisionnement est déployé."}
  , {Catalog.ADS_USR_MAILREDIRECT_LABEL,    "Redirection de courrier"}
  , {Catalog.ADS_USR_MAILREDIRECT_HINT,     "Adresse e-mail vers laquelle les e-mails envoyés au compte doivent être redirigés. Cette adresse e-mail remplace celle définie dans le champ EMail."}
  , {Catalog.ADS_USR_MUST_LABEL,            "Changer le mot de passe"}
  , {Catalog.ADS_USR_MUST_HINT,             "Drapeau qui indique si le compte doit ou non changer le mot de passe à la prochaine connexion. Si la valeur est vraie (la case est cochée), le compte doit changer le mot de passe à la prochaine connexion."}
  , {Catalog.ADS_USR_NEVER_LABEL,           "N'expire jamais"}
  , {Catalog.ADS_USR_NEVER_HINT,            "Propriété pour déterminer que le mot de passe du compte n'expire jamais."}
  , {Catalog.ADS_USR_LOCKED_LABEL,          "Fermé à clé"}
  , {Catalog.ADS_USR_LOCKED_HINT,           "Indique si le compte doit être verrouillé ou déverrouillé"}
  , {Catalog.ADS_USR_PASSWORDNO_LABEL,      "Mot de passe non requis"}
  , {Catalog.ADS_USR_PASSWORDNO_HINT,       "Spécifie si le mot de passe est requis ou non. Si c'est vrai, alors il n'est pas nécessaire de spécifier le mot de passe. S'il est faux, le mot de passe est requis."}
  , {Catalog.ADS_USR_EXPIRATION_LABEL,      "Date d'expiration"}
  , {Catalog.ADS_USR_EXPIRATION_HINT,       "La date à laquelle le compte expire."}
  , {Catalog.ADS_USR_USRHOMEDIR_LABEL,      "Répertoire personnel"}
  , {Catalog.ADS_USR_USRHOMEDIR_HINT,       "Répertoire personnel de l'utilisateur."}
  , {Catalog.ADS_USR_TSSHOMEDIR_LABEL,      "Répertoire de base du terminal"}
  , {Catalog.ADS_USR_TSSHOMEDIR_HINT,       "Chemin d'accès complet du répertoire de base du compte Terminal Server Exemple de valeur : C:\\MyDirectory. Au cours d'une opération d'approvisionnement, vous devez entrer le chemin d'accès complet et absolu du répertoire de base, comme indiqué dans l'exemple de valeur."}
  , {Catalog.ADS_USR_TSSLOGINALLOW_LABEL,   "Terminal Autoriser la connexion"}
  , {Catalog.ADS_USR_TSSLOGINALLOW_HINT,    "Spécifie si la connexion au serveur Terminal Server est autorisée. Activez cette option pour autoriser un compte à se connecter à un serveur de terminaux."}
  , {Catalog.ADS_USR_TSSPROFILEPATH_LABEL,  "Chemin du profil du terminal"}
  , {Catalog.ADS_USR_TSSPROFILEPATH_HINT,   "Profil utilisé lorsque le compte se connecte à un serveur Terminal Server. Le profil peut être itinérant ou obligatoire. Un profil itinérant reste le même, quel que soit l'ordinateur à partir duquel le compte se connecte. Le compte peut apporter des modifications à un profil itinérant, mais pas à un profil obligatoire. Toutes les modifications apportées par un compte lorsqu'il est connecté avec un profil obligatoire ne sont conservées que pour cette session des services Terminal Server. Les modifications sont perdues lorsque l'utilisateur démarre une autre session de service Terminal Server."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.PCF_USR_SVC_LABEL,             "Cloud Foundry Foundation"}
  , {Catalog.PCF_USR_SVC_HINT,              "La ressource informatique spécifiant la configuration du point de terminaison à Cloud Foundry Foundation."}
  , {Catalog.PCF_USR_SID_HINT,              "Identificateur unique de ce compte utilisateur que Pivotal Cloud Foundry Foundation utilise pour identifier le compte utilisateur."}
  , {Catalog.PCF_USR_UID_HINT,              "Le nom unique utilisé pour que ce compte soit authentifié par Cloud Foundry Foundation."}
  , {Catalog.PCF_USR_EID_LABEL,             "Nom externe"}
  , {Catalog.PCF_USR_EID_HINT,              "ID utilisateur externe s'il est authentifié via un fournisseur d'identité externe."}
  , {Catalog.PCF_USR_PWD_HINT,              "Le mot de passe utilisé pour se connecter au compte utilisateur Pivotal Cloud Foundry Foundation."}
  , {Catalog.PCF_USR_OID_TITLE,             "Magasin d'identité"}
  , {Catalog.PCF_USR_OID_LABEL,             "Magasin d'identité"}
  , {Catalog.PCF_USR_OID_HINT,              "L'alias du magasin d'identités qui a authentifié cet utilisateur. La valeur uaa indique un utilisateur du magasin d'identité interne de l'UAA."}
  , {Catalog.PCF_USR_IDP_TITLE,             "Fournisseur d'identité"}
  , {Catalog.PCF_USR_IDP_LABEL,             "Fournisseur d'identité"}
  , {Catalog.PCF_USR_IDP_HINT,              "Le fournisseur d'identité auquel appartient cet utilisateur. La valeur 'uaa' fait référence au fournisseur d'identité par défaut."}
  , {Catalog.PCF_USR_VFD_LABEL,             "Vérifié"}
  , {Catalog.PCF_USR_VFD_HINT,              "Les nouveaux utilisateurs sont automatiquement vérifiés par défaut. Les utilisateurs non vérifiés peuvent être créés en spécifiant vérifié : faux. Devient vrai lorsque l'utilisateur vérifie son adresse e-mail."}
  , {Catalog.PCF_UGP_SID_HINT,              "Le nom unique de ce groupe que la Cloud Foundry Foundation utilise pour identifier le groupe."}
  , {Catalog.PCF_ORL_SID_HINT,              "Le nom unique de cette organisation que Pivotal Cloud Foundry Foundation utilise pour identifier l'organisation."}
  , {Catalog.PCF_ORL_SCP_TITLE,             "Rôle dans l'organisation"}
  , {Catalog.PCF_ORL_SCP_LABEL,             "Rôle"}
  , {Catalog.PCF_ORL_SCP_HINT,              "Le rôle de ce compte dans cette organisation."}
  , {Catalog.PCF_SRL_HEADER,                "Les espaces"}
  , {Catalog.PCF_SRL_SID_TITLE,             "Espace"}
  , {Catalog.PCF_SRL_SID_HINT,              "Le nom unique de cet espace utilisé par la Cloud Foundry Foundation pour identifier l'espace."}
  , {Catalog.PCF_SRL_SCP_TITLE,             "Rôle dans l'espace"}
  , {Catalog.PCF_SRL_SCP_LABEL,             "Rôle"}
  , {Catalog.PCF_SRL_SCP_HINT,              "Le rôle de ce compte dans cet espace."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.AJS_USR_SVC_LABEL,             "Atlassian Jira Server"}
  , {Catalog.AJS_USR_SVC_HINT,              "La ressource informatique spécifiant la configuration du point de terminaison au serveur Atlassian Jira."}
  , {Catalog.AJS_USR_UID_HINT,              "Le nom unique utilisé pour que ce compte soit authentifié par Atlassian Jira Server."}
  , {Catalog.AJS_USR_PWD_HINT,              "Le mot de passe utilisé pour se connecter au compte utilisateur Atlassian Jira Server."}
  , {Catalog.AJS_GRP_GID_HINT,              "Le nom unique de ce groupe que le serveur Atlassian Jira utilise pour identifier le groupe."}
  , {Catalog.AJS_PRJ_HEADER,                "Projets"}
  , {Catalog.AJS_PRJ_PID_TITLE,             "Projet"}
  , {Catalog.AJS_PRJ_PID_HINT,              "Le nom unique de ce projet que le serveur Atlassian Jira utilise pour identifier le projet."}
  , {Catalog.AJS_PRJ_RID_HINT,              "Le nom unique de ce rôle de projet que le serveur Atlassian Jira utilise pour identifier le rôle de projet."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.GAE_USR_SVC_LABEL,             "Google Apigee Edge"}
  , {Catalog.GAE_USR_SVC_HINT,              "La ressource informatique spécifiant la configuration du point de terminaison à Google Apigee Edge."}
  , {Catalog.GAE_USR_OID_LABEL,             "Organisation"}
  , {Catalog.GAE_USR_OID_HINT,              "Organisation"}
  , {Catalog.GAE_USR_UID_LABEL,             "Nom d'utilisateur"}
  , {Catalog.GAE_USR_UID_HINT,              "Nom unique utilisé pour que ce compte soit authentifié par Google Apigee Edge."}
  , {Catalog.GAE_USR_UPN_LABEL,             "Courrier de l'utilisateur"}
  , {Catalog.GAE_USR_UPN_HINT,              "L'adresse e-mail de l'utilisateur pour ce compte dans Google Apigee Edge."}
  , {Catalog.GAE_USR_PWD_HINT,              "Le mot de passe utilisé pour se connecter au compte utilisateur Google Apigee Edge."}
  , {Catalog.GAE_URL_SID_LABEL,             "Porté"}
  , {Catalog.GAE_URL_SID_HINT,              "Le nom unique de cette organisation que Google API Gateway verwendet utilise pour identifier l'organisation."}
  , {Catalog.GAE_URL_RID_TITLE,             "Rôle dans l'organisation"}
  , {Catalog.GAE_URL_RID_HINT,              "Le rôle de ce compte dans cette organisation."}
  , {Catalog.GAE_DAP_HEADER,                "Applications"}
  , {Catalog.GAE_DAP_SID_TITLE,             "Application"}
  , {Catalog.GAE_DAP_SID_HINT,              "Nom unique de l'application accordé au développeur dans Google Apigee Edge."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.OFS_USR_SVC_LABEL,             "openfire™ Database Server"}
  , {Catalog.OFS_USR_SVC_HINT,              "La ressource informatique spécifiant la configuration du point de terminaison à openfire™ Database."}
  , {Catalog.OFS_USR_UID_LABEL,             "Nom d'utilisateur"}
  , {Catalog.OFS_USR_UID_HINT,              "Nom unique utilisé pour que ce compte soit authentifié par openfire™ XMPP Domain."}
  , {Catalog.OFS_USR_PWD_HINT,              "Le mot de passe utilisé pour se connecter au compte utilisateur XMPP Domain."}
  , {Catalog.OFS_USR_ADM_LABEL,             "Administrateur"}
  , {Catalog.OFS_USR_ADM_HINT,              "Autorisation d'administrer le domaine openfire™ XMPP via la console d'administration."}
  , {Catalog.OFS_UGP_GID_HINT,              "Le nom unique de ce groupe que le serveur openfire™ XMPP Domain utilise pour identifier le groupe."}
  , {Catalog.OFS_UGP_ADM_LABEL,             "Administrateur"}
  , {Catalog.OFS_UGP_ADM_HINT,              "L'autorisation donne à certains utilisateurs des droits d'administrateur sur le groupe."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.MDL_USR_SVC_LABEL,             "Service Moodle"}
  , {Catalog.MDL_USR_SVC_HINT,              "La ressource informatique spécifiant la configuration du point de terminaison au serveur sur lequel Moodle est déployé."}
  , {Catalog.MDL_USR_UID_HINT,              "L'identifiant système utilisé pour que ce compte soit authentifié par Moodle."}
  , {Catalog.MDL_USR_UPN_HINT,              "Le nom unique utilisé pour que ce compte soit authentifié par Moodle."}
  , {Catalog.MDL_USR_PWD_HINT,              "Le mot de passe utilisé pour que ce compte soit authentifié par Moodle."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.UPC_USR_SVC_LABEL,             "Universal Police Client"}
  , {Catalog.UPC_USR_SVC_HINT,              "La ressource informatique spécifiant la configuration du point de terminaison à Universal Police Client."}
  , {Catalog.UPC_USR_SID_HINT,              "Identificateur unique de ce compte utilisateur que Universal Police Client utilise pour identifier le compte utilisateur."}
  , {Catalog.UPC_USR_UID_HINT,              "Le nom unique utilisé pour que ce compte soit authentifié par Universal Police Client."}
  , {Catalog.UPC_USR_PWD_HINT,              "Le mot de passe utilisé pour se connecter au compte utilisateur Universal Police Client."}
  , {Catalog.UPC_UGP_SID_HINT,              "Le nom unique de ce groupe que la Universal Police Client utilise pour identifier le groupe."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.IGS_USR_SVC_LABEL,             "Point de terminaison du service"}
  , {Catalog.IGS_USR_SVC_HINT,              "Ressource informatique qui fournit la configuration du point de terminaison pour la gouvernance des identités."}
  , {Catalog.IGS_USR_SID_HINT,              "L'identifiant unique de ce compte utilisateur qu'Identity Governance utilise pour identifier le compte utilisateur."}
  , {Catalog.IGS_USR_UID_HINT,              "Nom unique de l'identifiant utilisé pour se connecter à Identity Governance avec le compte utilisateur."}
  , {Catalog.IGS_USR_PWD_HINT,              "Mot de passe utilisé pour se connecter à Identity Governance avec le compte utilisateur."}
  , {Catalog.IGS_URL_SID_HINT,              "Le nom unique du rôle pour identifier ce rôle."}
  , {Catalog.IGS_UTN_HEADER,                "Clientèle"}
  , {Catalog.IGS_UTN_SID_TITLE,             "Client"}
  , {Catalog.IGS_UTN_SID_HINT,              "Le nom unique de cette organisation que Identity Governance utilise pour identifier l'organisation."}
  , {Catalog.IGS_UTN_RID_TITLE,             "Rôles chez le client"}
  , {Catalog.IGS_UTN_RID_HINT,              "Rôle revendiqué par ce compte d'utilisateur dans ce locataire."}

     /** Constant used to demarcate application entities related messages. */
  , {Catalog.OIG_USR_SVC_LABEL,             "Point de terminaison du service"}
  , {Catalog.OIG_USR_SVC_HINT,              "Ressource informatique qui fournit la configuration du point de terminaison pour la gouvernance des identités."}
  , {Catalog.OIG_USR_SID_HINT,              "L'identifiant unique de ce compte utilisateur qu'Identity Governance utilise pour identifier le compte utilisateur."}
  , {Catalog.OIG_USR_UID_HINT,              "Nom unique de l'identifiant utilisé pour se connecter à Identity Governance avec le compte utilisateur."}
  , {Catalog.OIG_UPR_HEADER,                "System Roles"}
  , {Catalog.OIG_UPR_SID_TITLE,             "Identity Governance System Role"}
  , {Catalog.OIG_UPR_SID_HINT,              "The unique name of the system role Identity Governance uses to assign the role."}
  , {Catalog.OIG_UPG_HEADER,                "Global Roles"}
  , {Catalog.OIG_UPG_SID_TITLE,             "Identity Governance Global Role"}
  , {Catalog.OIG_UPG_SID_HINT,              "The unique name of the global role Identity Governance uses to assign the role."}
  , {Catalog.OIG_UPS_HEADER,                "Scoped Roles"}
  , {Catalog.OIG_UPS_SID_TITLE,             "Identity Governance Scoped Role"}
  , {Catalog.OIG_UPS_SID_HINT,              "The unique name of the scoped role Identity Governance uses to assign the role."}
  , {Catalog.OIG_UPS_SCP_TITLE,             "Organisational Scope"}
  , {Catalog.OIG_UPS_SCP_LABEL,             "Scope"}
  , {Catalog.OIG_UPS_SCP_HINT,              "The organisational scope to assign on the role."}
  , {Catalog.OIG_UPS_HRC_LABEL,             "Hierarchy"}
  , {Catalog.OIG_UPS_HRC_HINT,              "Whether this role is applied to the entire hierarchy beneath the scope or not."}

    /** Constant used to demarcate application entities related messages. */
  , {Catalog.BDS_USR_SERVER_LABEL,          "Serveur d'annuaire"}
  , {Catalog.BDS_USR_SERVER_HINT,           "La ressource informatique spécifiant la configuration du point de terminaison au serveur sur lequel le service d'annuaire est déployé"}

  /** Constant used to demarcate RedHat Keycloak application entities related messages. */
  , {Catalog.RKC_USR_ACTION_HEADER,         "Actions requises"}
  , {Catalog.RKC_USR_CREDENTIAL_HEADER,     "Mots de passe désactivés"}
  , {Catalog.RKC_USR_SERVER_LABEL,          "Keycloak Sever"}
  , {Catalog.RKC_USR_SERVER_HINT,           "La ressource informatique spécifiant la configuration du point de terminaison sur le serveur sur lequel le service Keycloak est déployé."}
  , {Catalog.RKC_USR_SID_HINT,              "L'identifiant utilisé pour que ce compte soit identifié sur le serveur cible."}
  , {Catalog.RKC_USR_NAME_HINT,             "The login name used for this account to be authenticated by Keycloak Service."}
  , {Catalog.RKC_USR_PASSWORD_HINT,         "Mot de passe utilisé pour authentifier ce compte par le service d'annuaire."}
  , {Catalog.RKC_USR_VERIFIED_LABEL,        "Verified"}
  , {Catalog.RKC_USR_VERIFIED_HINT,         "Attribute that indicates if the account is verified on the Keycloak Server."}
  , {Catalog.RKC_USR_OTP_LABEL,             "One Time Password"}
  , {Catalog.RKC_USR_OTP_HINT,              "Attribute that indicates the OTP status of the account on the Keycloak Server."}
  , {Catalog.RKC_USR_NOTBEF_LABEL,          "Not Before"}
  , {Catalog.RKC_USR_NOTBEF_HINT,           "The Not Before attribute of this account."}
  , {Catalog.RKC_USR_AEV_LABEL,             "Verify e-Mail"}
  , {Catalog.RKC_USR_AEV_HINT,              "Verify e-Mail action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_ADA_LABEL,             "Delete Account"}
  , {Catalog.RKC_USR_ADA_HINT,              "Delete Account action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_APV_LABEL,             "Verify Profile"}
  , {Catalog.RKC_USR_APV_HINT,              "Verify Profile action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_APU_LABEL,             "Update Profile"}
  , {Catalog.RKC_USR_APU_HINT,              "Update Profile action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_AUP_LABEL,             "Update Password"}
  , {Catalog.RKC_USR_AUP_HINT,              "Update Password action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_ALU_LABEL,             "Update Locale"}
  , {Catalog.RKC_USR_ALU_HINT,              "Update Locale action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_ACO_LABEL,             "Configure OTP"}
  , {Catalog.RKC_USR_ACO_HINT,              "Configure OTP action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_ATC_LABEL,             "Terms and Cond"}
  , {Catalog.RKC_USR_ATC_HINT,              "Terms and Cond action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_AWR_LABEL,             "Authn Register"}
  , {Catalog.RKC_USR_AWR_HINT,              "Authn Register action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_AWP_LABEL,             "Authn Password"}
  , {Catalog.RKC_USR_AWP_HINT,              "Authn Password action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_USR_ADC_LABEL,             "Delete Credential"}
  , {Catalog.RKC_USR_ADC_HINT,              "Delete Credential action can be enforced by the Keycloak Server."}
  , {Catalog.RKC_UGR_LOOKUP_TITLE,          "Groupe Keycloak"}
  , {Catalog.RKC_UGR_NAME_HINT,             "Le nom du groupe dans le serveur Keycloak."}
  , {Catalog.RKC_URR_HEADER_TEXT,           "Rôles du royaume"}
  , {Catalog.RKC_URR_LOOKUP_TITLE,          "Rôle du royaume de Keycloak"}
  , {Catalog.RKC_URR_NAME_HINT,             "Le nom du rôle du domaine dans le serveur Keycloak."}
  , {Catalog.RKC_UCR_HEADER_TEXT,           "Rôles des clients"}
  , {Catalog.RKC_UCR_LOOKUP_TITLE,          "Rôle du client Keycloak"}
  , {Catalog.RKC_UCR_NAME_HINT,             "Le nom du rôle client dans le serveur Keycloak."}
    
  /** Constant used to demarcate PLX application entities related messages. */
  , {Catalog.PLX_USR_SERVER_LABEL,          "Serveur LDAP PLX"}
  , {Catalog.PLX_USR_SERVER_HINT,           "La ressource informatique spécifiant la configuration du point de terminaison pour le serveur sur lequel le serveur LDAP PLX est déployé"}
  , {Catalog.PLX_USR_SID_LABEL,             "SID"}
  , {Catalog.PLX_USR_SID_HINT,              "Le SID de l'entrée associé à ce compte."}
  , {Catalog.PLX_USR_TENANT_LABEL,          "Locataire"}
  , {Catalog.PLX_USR_TENANT_HINT,           "L'organisation principale avec laquelle ce compte est associé."}
  , {Catalog.PLX_USR_DN_LABEL,              "Nom Distingué"}
  , {Catalog.PLX_USR_DN_HINT,               "Le DN de ce compte associé à l'identité."}
  , {Catalog.PLX_USR_PASSWORD_HINT,         "Le mot de passe utilisé pour ce compte afin d'?tre authentifié par PLX."}
  , {Catalog.PLX_USR_EXT_ATTR_HEADER,       "Attributs d'Extension"}
  , {Catalog.PLX_USR_EXT_ATTR1_LABEL,       "Attribut d'Extension 1"}
  , {Catalog.PLX_USR_EXT_ATTR1_HINT,        "Informations supplémentaires au-delà de l'ensemble standard d'attributs."}
  , {Catalog.PLX_USR_EXT_ATTR3_LABEL,       "Attribut d'Extension 3"}
  , {Catalog.PLX_USR_EXT_ATTR3_HINT,        "Informations supplémentaires au-delà de l'ensemble standard d'attributs."}
  , {Catalog.PLX_USR_EXT_ATTR4_LABEL,       "Attribut d'Extension 4"}
  , {Catalog.PLX_USR_EXT_ATTR4_HINT,        "Informations supplémentaires au-delà de l'ensemble standard d'attributs."}
  , {Catalog.PLX_USR_EXT_ATTR6_LABEL,       "Attribut d'Extension 6"}
  , {Catalog.PLX_USR_EXT_ATTR6_HINT,        "Informations supplémentaires au-delà de l'ensemble standard d'attributs."}
  , {Catalog.PLX_USR_OBJ_SID_LABEL,         "SID d'Objet"}
  , {Catalog.PLX_USR_OBJ_SID_HINT,          "Le SID d'objet de ce compte associé à l'identité."}
  , {Catalog.PLX_USR_BUSINESS_CAT_LABEL,    "Catégorie d'Entreprise"}
  , {Catalog.PLX_USR_BUSINESS_CAT_HINT,     "La catégorie d'entreprise de ce compte associé à l'identité."}
  , {Catalog.PLX_USR_CAR_LICENCE_LABEL,     "Permis de Conduire"}
  , {Catalog.PLX_USR_CAR_LICENCE_HINT,      "Le permis de conduire de ce compte."}
  , {Catalog.PLX_USR_DEPARTMENT_NUM_LABEL,  "Numéro de Département"}
  , {Catalog.PLX_USR_DEPARTMENT_NUM_HINT,   "Le numéro de département de ce compte associé à l'identité."}
  , {Catalog.PLX_USR_EMPLOYEE_NUM_LABEL,    "Numéro d'Employé"}
  , {Catalog.PLX_USR_EMPLOYEE_NUM_HINT,     "Le numéro d'employé de ce compte."}
  , {Catalog.PLX_USR_HOME_POST_ADDR_LABEL,  "Adresse Postale Domicile"}
  , {Catalog.PLX_USR_HOME_POST_ADDR_HINT,   "L'adresse postale domicile de ce compte associé à l'identité."}
  , {Catalog.PLX_USR_ORG_NAME_LABEL,        "Nom de l'Organisation"}
  , {Catalog.PLX_USR_ORG_NAME_HINT,         "Le nom de l'organisation de ce compte associé à l'identité."}
  , {Catalog.PLX_USR_PREFERENCE_HEADER,     "Préférence"}
  , {Catalog.PLX_USR_PREF_LANG_LABEL,       "Langue Préférée"}
  , {Catalog.PLX_USR_PREF_LANG_HINT,        "La langue préférée de ce compte associée à l'identité."}
  , {Catalog.PLX_USR_ROOM_NUM_LABEL,        "Numéro de Salle"}
  , {Catalog.PLX_USR_ROOM_NUM_HINT,         "Le numéro de salle de ce compte."}
  , {Catalog.PLX_USR_SECRETARY_LABEL,       "Secrétaire"}
  , {Catalog.PLX_USR_SECRETARY_HINT,        "Le secrétaire de ce compte."}
  , {Catalog.PLX_USR_UID_LABEL,             "UID"}
  , {Catalog.PLX_USR_UID_HINT,              "L'identifiant utilisateur de ce compte."}
  , {Catalog.PLX_USR_DESC_LABEL,            "Description"}
  , {Catalog.PLX_USR_DESC_HINT,             "La description de ce compte associé à l'identité."}
  , {Catalog.PLX_UGR_LOOKUP_TITLE,          "Groupe PLX"}
  , {Catalog.PLX_UGR_NAME_HINT,             "Le DN du groupe dans le PLX."}
  , {Catalog.PLX_UPX_HEADER_TEXT,           "Proxy"}
  , {Catalog.PLX_UPX_LOOKUP_TITLE,          "Proxy PLX"}
  , {Catalog.PLX_UPX_NAME_HINT,             "Le DN du proxy dans le PLX."}
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