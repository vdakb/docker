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

    File        :   CatalogBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package bka.iam.identity.resource;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class CatalogBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CatalogBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    /** Constant used to demarcate catalog entity related messages. */
    {Catalog.CAT_RISK_LABEL,                "Risk Level"}
  , {Catalog.CAT_RISK_HINT,                 "Risk Level"}
  , {Catalog.CAT_ROLE_OWNER_LABEL,          "Role Owner"}
  , {Catalog.CAT_ROLE_OWNER_HINT,           "Role Owner"}
  , {Catalog.CAT_ROLE_DESCRIPTION_LABEL,    "Role Description"}
  , {Catalog.CAT_ROLE_DESCRIPTION_HINT,     "Role Description"}
  , {Catalog.CAT_VALID_FROM_LABEL,          "Valid From"}
  , {Catalog.CAT_VALID_FROM_HINT,           "Valid From"}
  , {Catalog.CAT_PRIVILEGE_ACTION_LABEL,    "Action"}
  , {Catalog.CAT_PRIVILEGE_ACTION_HINT,     "Action"}
  , {Catalog.CAT_PRIVILEGE_ASSIGN_LABEL,    "Assign Privilege"}
  , {Catalog.CAT_PRIVILEGE_ASSIGN_HINT,     "Assign Privilege"}
  , {Catalog.CAT_PRIVILEGE_REVOKE_LABEL,    "Revoke Privilege"}
  , {Catalog.CAT_PRIVILEGE_REVOKE_LABEL,    "Revoke Privilege"}
  , {Catalog.CAT_BASIC_HEADER,              "Basic Information"}
  , {Catalog.CAT_ACCOUNT_HEADER,            "Account Settings"}
  , {Catalog.CAT_CONTACT_HEADER,            "Contact Information"}
  , {Catalog.CAT_ORGANIZATION_HEADER,       "Organizational Information"}
  , {Catalog.CAT_MISCELLANEOUS_HEADER,      "Miscellaneous Information"}

  /* Constant used to demarcate common application entities related messages. */
  , {Catalog.CAT_USR_IDENTIFIER_LABEL,      "Identifier"}
  , {Catalog.CAT_USR_LOGIN_NAME_LABEL,      "Login Name"}
  , {Catalog.CAT_USR_UNIFIED_LABEL,         "Unified Login"}
  , {Catalog.CAT_USR_UNIFIED_HINT,          "The Login Name used by this identity to be identified in P20 applications and services."}
  , {Catalog.CAT_USR_ANONYMIZED_LABEL,      "Anonymized Login"}
  , {Catalog.CAT_USR_ANONYMIZED_HINT,       "The Login Name used by this identity to obfuscated the real identity in outbound communications."}
  , {Catalog.CAT_USR_PRINCIPAL_NAME_LABEL,  "Principal Name"}
  , {Catalog.CAT_USR_PRINCIPAL_NAME_HINT,   "The user principal name of this account associated with the identity."}
  , {Catalog.CAT_USR_PASSWORD_LABEL,        "Password"}
  , {Catalog.CAT_USR_SAM_LABEL,             "Windows Login"}
  , {Catalog.CAT_USR_SAM_HINT,              "The Windows Login of this account associated with the identity."}
  , {Catalog.CAT_USR_WELCOME_LABEL,         "Welcome"}
  , {Catalog.CAT_USR_WELCOME_HINT,          "Should the user be shown a welcome message the next time they log in?"}
  , {Catalog.CAT_USR_TYPE_TITLE,            "Employee Type"}
  , {Catalog.CAT_USR_TYPE_LABEL,            "Type"}
  , {Catalog.CAT_USR_TYPE_HINT,             "The employee type of this account associated with the identity."}
  , {Catalog.CAT_USR_JOBROLE_LABEL,         "Job Role"}
  , {Catalog.CAT_USR_JOBROLE_HINT,          "The role/rank that the identity has in the organizational structure."}
  , {Catalog.CAT_USR_TITLE_LABEL,           "Title"}
  , {Catalog.CAT_USR_TITLE_HINT,            "The title of this account associated with the identity."}
  , {Catalog.CAT_USR_COMMON_NAME_LABEL,     "Common Name"}
  , {Catalog.CAT_USR_COMMON_NAME_HINT,      "The common name of this account associated with the identity."}
  , {Catalog.CAT_USR_FIRST_NAME_LABEL,      "First Name"}
  , {Catalog.CAT_USR_FIRST_NAME_HINT,       "The first name of this account associated with the identity."}
  , {Catalog.CAT_USR_LAST_NAME_LABEL,       "Last Name"}
  , {Catalog.CAT_USR_LAST_NAME_HINT,        "The last name of this account associated with the identity."}
  , {Catalog.CAT_USR_MIDDLE_NAME_LABEL,     "Middle Name"}
  , {Catalog.CAT_USR_MIDDLE_NAME_HINT,      "The middle name of this account associated with the identity."}
  , {Catalog.CAT_USR_DISPLAY_NAME_LABEL,    "Display Name"}
  , {Catalog.CAT_USR_DISPLAY_NAME_HINT,     "The display name of this account associated with the identity."}
  , {Catalog.CAT_USR_INITIALS_LABEL,        "Initials"}
  , {Catalog.CAT_USR_INITIALS_HINT,         "The Initials of this account associated with the identity."}
  , {Catalog.CAT_USR_SERVICE_ACCOUNT_LABEL, "Service Account"}
  , {Catalog.CAT_USR_SERVICE_ACCOUNT_HINT,  "Whether this account is a Service Account or not."}

  , {Catalog.CAT_USR_PARTICIPANT_LABEL,     "Participant"}
  , {Catalog.CAT_USR_PARTICIPANT_HINT,      "The participant to which this user account is assigned."}
  , {Catalog.CAT_USR_DIVISION_LABEL,        "Division"}
  , {Catalog.CAT_USR_DIVISION_HINT,         "The division of this account associated with the identity."}
  , {Catalog.CAT_USR_ORGANIZATION_LABEL,    "Organization Unit"}
  , {Catalog.CAT_USR_ORGANIZATION_HINT,     "The organizational unit of this account associated with the identity."}
  , {Catalog.CAT_USR_DEPARTMENT_LABEL,      "Department"}
  , {Catalog.CAT_USR_DEPARTMENT_HINT,       "The department of this account associated with the identity."}
  , {Catalog.CAT_USR_OFFICE_LABEL,          "Office"}
  , {Catalog.CAT_USR_OFFICE_HINT,           "The office of this account associated with the identity."}
  , {Catalog.CAT_USR_MANAGER_LABEL,         "Manager"}
  , {Catalog.CAT_USR_MANAGER_HINT,          "The manager of this account associated with the identity."}
  , {Catalog.CAT_USR_COMPANY_LABEL,         "Company"}
  , {Catalog.CAT_USR_COMPANY_HINT,          "The company of this account associated with the identity."}

  , {Catalog.CAT_USR_EMAIL_LABEL,           "e-Mail"}
  , {Catalog.CAT_USR_EMAIL_HINT,            "The e-Mail address of this account associated with the identity."}
  , {Catalog.CAT_USR_PHONE_LABEL,           "Telephone"}
  , {Catalog.CAT_USR_PHONE_HINT,            "The telephone number of this account associated with the identity."}
  , {Catalog.CAT_USR_FAX_LABEL,             "Facsimile"}
  , {Catalog.CAT_USR_FAX_HINT,              "The facsimile number of this account associated with the identity."}
  , {Catalog.CAT_USR_MOBILE_LABEL,          "Mobile"}
  , {Catalog.CAT_USR_MOBILE_HINT,           "The mobile number of this account associated with the identity."}
  , {Catalog.CAT_USR_PAGER_LABEL,           "Pager"}
  , {Catalog.CAT_USR_PAGER_HINT,            "The pager number of this account associated with the identity."}
  , {Catalog.CAT_USR_IPPHONE_LABEL,         "IP Phone"}
  , {Catalog.CAT_USR_IPPHONE_HINT,          "The IP Phone number of this account associated with the identity."}
  , {Catalog.CAT_USR_HOMEPHONE_LABEL,       "Home Phone"}
  , {Catalog.CAT_USR_HOMEPHONE_HINT,        "The Home Phone number of this account associated with the identity."}
  , {Catalog.CAT_USR_POSTALCODE_LABEL,      "Postal Code"}
  , {Catalog.CAT_USR_POSTALCODE_HINT,       "The postal code of this account associated with the identity."}
  , {Catalog.CAT_USR_POSTALADR_LABEL,       "Postal Address"}
  , {Catalog.CAT_USR_POSTALADR_HINT,        "The postal address of this account associated with the identity."}
  , {Catalog.CAT_USR_POBOX_LABEL,           "Post Box"}
  , {Catalog.CAT_USR_POBOX_HINT,            "The post box of this account associated with the identity."}
  , {Catalog.CAT_USR_COUNTRY_LABEL,         "Country"}
  , {Catalog.CAT_USR_COUNTRY_HINT,          "The country of this account associated with the identity."}
  , {Catalog.CAT_USR_STATE_LABEL,           "State"}
  , {Catalog.CAT_USR_STATE_HINT,            "The state of this account associated with the identity."}
  , {Catalog.CAT_USR_LOCALITY_LABEL,        "Locality"}
  , {Catalog.CAT_USR_LOCALITY_HINT,         "The locality of this account associated with the identity."}
  , {Catalog.CAT_USR_STREET_LABEL,          "Street"}
  , {Catalog.CAT_USR_STREET_HINT,           "The street of this account associated with the identity."}
  , {Catalog.CAT_USR_LANGUAGE_TITLE,        "Language"}
  , {Catalog.CAT_USR_LANGUAGE_LABEL,        "Language"}
  , {Catalog.CAT_USR_LANGUAGE_HINT,         "The language of this account associated with the identity."}
  , {Catalog.CAT_USR_TIMEZONE_TITLE,        "Time Zone"}
  , {Catalog.CAT_USR_TIMEZONE_LABEL,        "Time Zone"}
  , {Catalog.CAT_USR_TIMEZONE_HINT,         "The time zone of this account associated with the identity."}

  , {Catalog.CAT_FORM_NAME_LABEL,           "Name"}
  , {Catalog.CAT_UGP_NAME_HINT,             "The distinguished name of the group."}
  , {Catalog.CAT_UGP_HEADER_TEXT,           "Groups"}
  , {Catalog.CAT_UGP_LOOKUP_TITLE,          "Group"}
  , {Catalog.CAT_URL_NAME_HINT,             "The distinguished name of the role."}
  , {Catalog.CAT_URL_HEADER_TEXT,           "Roles"}
  , {Catalog.CAT_URL_LOOKUP_TITLE,          "Role"}
  , {Catalog.CAT_ORL_HEADER_TEXT,           "Organizations"}
  , {Catalog.CAT_ORL_LOOKUP_TITLE,          "Organization"}


  /** Constant used to demarcate application entities related messages. -- directory */
  , {Catalog.ODS_USR_SERVER_LABEL,          "Directory Sever"}
  , {Catalog.ODS_USR_SERVER_HINT,           "The IT Resource specifying the endpoint configuration to the Server where the directory service is deployed on"}
  , {Catalog.ODS_USR_PARENTDN_TITLE,        "Hierarchy"}
  , {Catalog.ODS_USR_PARENTDN_LABEL,        "Hierarchy"}
  , {Catalog.ODS_USR_PARENTDN_HINT,         "Hierarchy"}
  , {Catalog.ODS_USR_IDENTIFIER_HINT,       "The identifier used for this account to be identified in target directory."}
  , {Catalog.ODS_USR_LOGIN_NAME_HINT,       "The login name used for this account to be authenticated by Directory Service."}
  , {Catalog.ODS_USR_PASSWORD_HINT,         "The password used for this account to be authenticated by Directory Service."}
  , {Catalog.ODS_USR_LANGUAGE_TITLE,        "Directory Language"}
  , {Catalog.ODS_USR_COUNTRY_TITLE,         "Directory Country"}
  , {Catalog.ODS_USR_TIMEZONE_TITLE,        "Directory Time Zone"}
  , {Catalog.ODS_UGP_NAME_HINT,             "The distinguished name if the group in the Directory."}
  , {Catalog.ODS_UGP_LOOKUP_TITLE,          "Directory Group"}
  , {Catalog.ODS_URL_NAME_HINT,             "The distinguished name if the role in the Directory."}
  , {Catalog.ODS_URL_LOOKUP_TITLE,          "Directory Role"}

  /** Constant used to demarcate application entities related messages. -- eFBS */
  , {Catalog.FBS_USR_SERVER_LABEL,          "eFBS Service"}
  , {Catalog.FBS_USR_SERVER_HINT,           "The IT Resource specifying the endpoint configuration to the Server where the provisioning service is deployed on"}
  , {Catalog.FBS_USR_AID_LABEL,             "Login Id"}
  , {Catalog.FBS_USR_AID_HINT,              "The login Id used for this account to be identified in eFBS."}
  , {Catalog.FBS_USR_UID_HINT,              "The login name used for this account to be authenticated by eFBS."}
  , {Catalog.FBS_USR_VALID_FROM_LABEL,      "Valid from"}
  , {Catalog.FBS_USR_VALID_FROM_HINT,       "Start date of the period this account is valid in eFBS"}
  , {Catalog.FBS_USR_VALID_TO_LABEL,        "Valid through"}
  , {Catalog.FBS_USR_VALID_TO_HINT,         "End date of the period this account is valid in eFBS"}
  , {Catalog.FBS_USR_ADMIN_ACCOUNT_LABEL,   "Admin Account"}
  , {Catalog.FBS_USR_ADMIN_ACCOUNT_HINT,    "Whether this account is an Admin Account or not."}
  , {Catalog.FBS_EML_HEADER_TEXT,           "e-Mail Addresses" }
  , {Catalog.FBS_EML_VALUE_LABEL,           "e-Mail" }
  , {Catalog.FBS_EML_VALUE_HINT,            "A e-Mail address of this account associated with the identity." }
  , {Catalog.FBS_PHN_HEADER_TEXT,           "Telephone Numbers" }
  , {Catalog.FBS_PHN_VALUE_LABEL,           "Number" }
  , {Catalog.FBS_PHN_VALUE_HINT,            "A telephone number of this account associated with the identity." }
  , {Catalog.FBS_REQ_EDU_LABEL,             "Education" }
  , {Catalog.FBS_REQ_PRD_LABEL,             "Production" }
  , {Catalog.FBS_REQ_FA_LABEL,              "Administrator" }
  , {Catalog.FBS_REQ_SR_LABEL,              "Administrator Special Rights" }
  , {Catalog.FBS_REQ_SU_LABEL,              "Super User" }
  , {Catalog.FBS_REQ_GFA_LABEL,             "Global Administrator" }
  , {Catalog.FBS_REQ_GSU_LABEL,             "Global Super User" }
  , {Catalog.FBS_REQ_GED_LABEL,             "Administrator GED" }

  /** Constant used to demarcate application entities related messages. -- ad  */
  , {Catalog.ADS_USR_SERVER_LABEL,          "Active Directory"}
  , {Catalog.ADS_USR_SERVER_HINT,           "The IT Resource specifying the endpoint configuration to the Server where the provisioning service is deployed on"}
  , {Catalog.ADS_USR_MAILREDIRECT_LABEL,    "Mail Redirect"}
  , {Catalog.ADS_USR_MAILREDIRECT_HINT,     "E-mail address to which e-mail sent to the account must be redirected. This e-mail address overrides the one set in the EMail field."}
  , {Catalog.ADS_USR_MUST_LABEL,            "Change Password"}
  , {Catalog.ADS_USR_MUST_HINT,             "Flag that indicates whether or not the account must change the password at next logon. If the value is true (check box is selected), then the account must change the password at next logon."}
  , {Catalog.ADS_USR_NEVER_LABEL,           "Never Expires"}
  , {Catalog.ADS_USR_NEVER_HINT,            "Property to determine that the password for the account never expires."}
  , {Catalog.ADS_USR_LOCKED_LABEL,          "Locked"}
  , {Catalog.ADS_USR_LOCKED_HINT,           "Specifies whether the account must be locked or unlocked"}
  , {Catalog.ADS_USR_PASSWORDNO_LABEL,      "Password Not Required"}
  , {Catalog.ADS_USR_PASSWORDNO_HINT,       "Specifies whether or not Password is required. If it is true, then there is no need to specify the password. If it is false, then password is required."}
  , {Catalog.ADS_USR_EXPIRATION_LABEL,      "Expiration Date"}
  , {Catalog.ADS_USR_EXPIRATION_HINT,       "The date when the account expires."}
  , {Catalog.ADS_USR_USRHOMEDIR_LABEL,      "Home Directory"}
  , {Catalog.ADS_USR_USRHOMEDIR_HINT,       "Home directory of the user."}
  , {Catalog.ADS_USR_TSSHOMEDIR_LABEL,      "Terminal Home Directory"}
  , {Catalog.ADS_USR_TSSHOMEDIR_HINT,       "Full path of the home directory for the Terminal Server account Sample value: C:\\MyDirectory. During a provisioning operation, you must enter the full, absolute path of the home directory, as shown in the sample value."}
  , {Catalog.ADS_USR_TSSLOGINALLOW_LABEL,   "Terminal Allow Login"}
  , {Catalog.ADS_USR_TSSLOGINALLOW_HINT,    "Specifies whether terminal server login is allowed. Enable this option to allow an account to log on to a terminal server."}
  , {Catalog.ADS_USR_TSSPROFILEPATH_LABEL,  "Terminal Profile Path"}
  , {Catalog.ADS_USR_TSSPROFILEPATH_HINT,   "Profile that is used when the account logs on to a Terminal Server. The profile can be roaming or mandatory. A roaming profile remains the same, regardless of the computer from which the account logs in. The account can make changes to a roaming profile, but not to a mandatory profile. Any changes a account makes while logged in with a mandatory profile are retained only for that Terminal Services session. The changes are lost when the user starts another Terminal Service ssession."}

  /** Constant used to demarcate application entities related messages. -- pcf */
  , {Catalog.PCF_USR_SVC_LABEL,             "Cloud Foundry Foundation"}
  , {Catalog.PCF_USR_SVC_HINT,              "The IT Resource specifying the endpoint configuration to the Pivotal Cloud Foundry Foundation."}
  , {Catalog.PCF_USR_SID_HINT,              "The system name generated by the UAA to uniquely identity this account."}
  , {Catalog.PCF_USR_UID_HINT,              "The unique name used for this account to be authenticated by Pivotal Cloud Foundry Foundation."}
  , {Catalog.PCF_USR_EID_LABEL,             "External Name"}
  , {Catalog.PCF_USR_EID_HINT,              "External user ID if authenticated through an external identity provider."}
  , {Catalog.PCF_USR_PWD_HINT,              "The password used for this account to be authenticated by Pivotal Cloud Foundry Foundation."}
  , {Catalog.PCF_USR_OID_TITLE,             "Identity Store"}
  , {Catalog.PCF_USR_OID_LABEL,             "Identity Store"}
  , {Catalog.PCF_USR_OID_HINT,              "The alias of the Identity Store that authenticated this user. The value uaa indicates a user from the UAA's internal identity store."}
  , {Catalog.PCF_USR_IDP_TITLE,             "Identity Provider"}
  , {Catalog.PCF_USR_IDP_LABEL,             "Identity Provider"}
  , {Catalog.PCF_USR_IDP_HINT,              "The Identity Provider this user belongs to. The value 'uaa' refers to the default identity provider."}
  , {Catalog.PCF_USR_VFD_LABEL,             "Verified"}
  , {Catalog.PCF_USR_VFD_HINT,              "New users are automatically verified by default. Unverified users can be created by specifying verified: false. Becomes true when the user verifies their email address."}
  , {Catalog.PCF_UGP_SID_HINT,              "The unique name of this group the Pivotal Cloud Foundry Foundation uses to identify the group."}
  , {Catalog.PCF_ORL_SID_HINT,              "The unique name of this organization the Pivotal Cloud Foundry Foundation uses to identify the organization."}
  , {Catalog.PCF_ORL_SCP_TITLE,             "Role in Organization"}
  , {Catalog.PCF_ORL_SCP_LABEL,             "Role"}
  , {Catalog.PCF_ORL_SCP_HINT,              "The role of this account in this organization."}
  , {Catalog.PCF_SRL_HEADER,                "Spaces"}
  , {Catalog.PCF_SRL_SID_TITLE,             "Space"}
  , {Catalog.PCF_SRL_SID_HINT,              "The unique name of this space the Cloud Foundry Foundation uses to identify the space."}
  , {Catalog.PCF_SRL_SCP_TITLE,             "Role in Space"}
  , {Catalog.PCF_SRL_SCP_LABEL,             "Role"}
  , {Catalog.PCF_SRL_SCP_HINT,              "The role of this account in this space."}

  /** Constant used to demarcate application entities related messages. */
  , {Catalog.AJS_USR_SVC_LABEL,             "Atlassian Jira Server"}
  , {Catalog.AJS_USR_SVC_HINT,              "The IT Resource specifying the endpoint configuration to the Atlassian Jira Server."}
  , {Catalog.AJS_USR_UID_HINT,              "The unique name used for this account to be authenticated by Atlassian Jira Server."}
  , {Catalog.AJS_USR_PWD_HINT,              "The password used for this account to be authenticated by Atlassian Jira Server."}
  , {Catalog.AJS_GRP_GID_HINT,              "The unique name of this group the Atlassian Jira Server uses to identify the group."}
  , {Catalog.AJS_PRJ_HEADER,                "Projects"}
  , {Catalog.AJS_PRJ_PID_TITLE,             "Project"}
  , {Catalog.AJS_PRJ_PID_HINT,              "The unique name of this project the Atlassian Jira Server uses to identify the project."}
  , {Catalog.AJS_PRJ_RID_HINT,              "The unique name of this project role the Atlassian Jira Server uses to identify the project role."}

  /** Constant used to demarcate application entities related messages. -- apigee */
  , {Catalog.GAE_USR_SVC_LABEL,             "Google Apigee Edge"}
  , {Catalog.GAE_USR_SVC_HINT,              "The IT Resource specifying the endpoint configuration to the Google Apigee Edge."}
  , {Catalog.GAE_USR_OID_LABEL,             "Organization"}
  , {Catalog.GAE_USR_OID_HINT,              "Organization"}
  , {Catalog.GAE_USR_UID_LABEL,             "User Name"}
  , {Catalog.GAE_USR_UID_HINT,              "The unique name used for this account to be authenticated by Google Apigee Edge."}
  , {Catalog.GAE_USR_UPN_LABEL,             "User Mail"}
  , {Catalog.GAE_USR_UPN_HINT,              "The e-Mail address of the user for this account in Google Apigee Edge."}
  , {Catalog.GAE_USR_PWD_HINT,              "The password used for this account to be authenticated by Google Apigee Edge."}
  , {Catalog.GAE_URL_SID_LABEL,             "Scope"}
  , {Catalog.GAE_URL_SID_HINT,              "The unique name of this organization the Google Apigee Edge uses to identify the organization."}
  , {Catalog.GAE_URL_RID_TITLE,             "Role in Organization"}
  , {Catalog.GAE_URL_RID_HINT,              "The role of this account in this organization."}
  , {Catalog.GAE_DAP_HEADER,                "Applications"}
  , {Catalog.GAE_DAP_SID_TITLE,             "Application"}
  , {Catalog.GAE_DAP_SID_HINT,              "The unique name of the application granted to the developer in Google Apigee Edge."}

  /** Constant used to demarcate application entities related messages. -- ofs */
  , {Catalog.OFS_USR_SVC_LABEL,             "openfire™ Database Server"}
  , {Catalog.OFS_USR_SVC_HINT,              "The IT Resource specifying the endpoint configuration to the openfire™ Database."}
  , {Catalog.OFS_USR_UID_LABEL,             "User Name"}
  , {Catalog.OFS_USR_UID_HINT,              "The unique name used for this account to be authenticated by openfire™ XMPP Domain."}
  , {Catalog.OFS_USR_PWD_HINT,              "The password used for this account to be authenticated by openfire™ XMPP Domain."}
  , {Catalog.OFS_USR_ADM_LABEL,             "Administrator"}
  , {Catalog.OFS_USR_ADM_HINT,              "Authorization to administer the openfire™ XMPP domain via the administration console."}
  , {Catalog.OFS_UGP_GID_HINT,              "The unique name of this group the openfire™ XMPP Domain uses to identify the group."}
  , {Catalog.OFS_UGP_ADM_LABEL,             "Administrator"}
  , {Catalog.OFS_UGP_ADM_HINT,              "The permission give certain users administrative rights over the group."}

  /** Constant used to demarcate application entities related messages. -- moodle */
  , {Catalog.MDL_USR_SVC_LABEL,             "Moodle Service"}
  , {Catalog.MDL_USR_SVC_HINT,              "The IT Resource specifying the endpoint configuration to the Server where Moodle is deployed on."}
  , {Catalog.MDL_USR_UID_HINT,              "The system identifier used for this account to be authenticated by Moodle."}
  , {Catalog.MDL_USR_UPN_HINT,              "The unique name used for this account to be authenticated by Moodle."}
  , {Catalog.MDL_USR_PWD_HINT,              "The password used for this account to be authenticated by Moodle."}

  /** Constant used to demarcate application entities related messages. -- upc */
  , {Catalog.UPC_USR_SVC_LABEL,             "Universal Police Client"}
  , {Catalog.UPC_USR_SVC_HINT,              "The IT Resource specifying the endpoint configuration to the Universal Police Client Service Endpoint."}
  , {Catalog.UPC_USR_SID_HINT,              "The system name generated by Universal Police Client to uniquely identity this account."}
  , {Catalog.UPC_USR_UID_HINT,              "The unique name used for this account to be authenticated by Universal Police Client."}
  , {Catalog.UPC_USR_PWD_HINT,              "The password used for this account to be authenticated by Universal Police Client."}
  , {Catalog.UPC_UGP_SID_HINT,              "The unique name of this group the Universal Police Client uses to identify the group."}

  /** Constant used to demarcate application entities related messages. -- igs */
  , {Catalog.IGS_USR_SVC_LABEL,             "Service Endpoint"}
  , {Catalog.IGS_USR_SVC_HINT,              "The IT Resource specifying the endpoint configuration to the Identity Governance Service Endpoint."}
  , {Catalog.IGS_USR_SID_HINT,              "The system name generated by Identity Governance to uniquely identity this account."}
  , {Catalog.IGS_USR_UID_HINT,              "The unique name used for this account to be authenticated by Identity Governance."}
  , {Catalog.IGS_USR_PWD_HINT,              "The password used for this account to be authenticated by Identity Governance."}
  , {Catalog.IGS_URL_SID_HINT,              "The unique name of the role Identity Governance uses to assign the role."}
  , {Catalog.IGS_UTN_HEADER,                "Tenants"}
  , {Catalog.IGS_UTN_SID_TITLE,             "Tenant"}
  , {Catalog.IGS_UTN_SID_HINT,              "The unique name of the tenant  Identity Governance uses to assign the tenant."}
  , {Catalog.IGS_UTN_RID_TITLE,             "Role in Tenant"}
  , {Catalog.IGS_UTN_RID_HINT,              "The role of the account claims in this tenant."}

  /** Constant used to demarcate application entities related messages. -- oig */
  , {Catalog.OIG_USR_SVC_LABEL,             "Service Endpoint"}
  , {Catalog.OIG_USR_SVC_HINT,              "The IT Resource specifying the endpoint configuration to the Identity Governance Service Endpoint."}
  , {Catalog.OIG_USR_SID_HINT,              "The system identifier generated by Identity Governance to uniquely identity this account."}
  , {Catalog.OIG_USR_UID_HINT,              "The unique name used for this account to be authenticated by Identity Governance."}
  , {Catalog.OIG_UPR_HEADER,                "System Roles"}
  , {Catalog.OIG_UPR_SID_TITLE,             "Identity Governance System Role"}
  , {Catalog.OIG_UPR_SID_HINT,              "The unique name of the system role Identity Governance uses to assign the role."}
  , {Catalog.OIG_UPG_HEADER,                "Global Roles"}
  , {Catalog.OIG_UPG_SID_TITLE,             "Identity Governance Global Role"}
  , {Catalog.OIG_UPG_SID_HINT,              "The unique name of the global role Identity Governance uses to assign the role."}
  , {Catalog.OIG_UPS_HEADER,                "Scoped Roles"}
  , {Catalog.OIG_UPS_SID_TITLE,             "Identity Governance Scoped Role"}
  , {Catalog.OIG_UPS_SID_HINT,              "The unique name of the scoped role Identity Governance uses to assign the role."}
  , {Catalog.OIG_UPS_SCP_TITLE,             "Organizational Scope"}
  , {Catalog.OIG_UPS_SCP_LABEL,             "Scope"}
  , {Catalog.OIG_UPS_SCP_HINT,              "The organizational scope to assign on the role."}
  , {Catalog.OIG_UPS_HRC_LABEL,             "Hierarchy"}
  , {Catalog.OIG_UPS_HRC_HINT,              "Whether this role is applied to the entire hierarchy beneath the scope or not."}

  /** Constant used to demarcate application entities related messages. */
  , {Catalog.BDS_USR_SERVER_LABEL,          "Directory Sever"}
  , {Catalog.BDS_USR_SERVER_HINT,           "The IT Resource specifying the endpoint configuration to the Server where the directory service is deployed on"}

  /** Constant used to demarcate RedHat Keycloak application entities related messages. */
  , {Catalog.RKC_USR_ACTION_HEADER,         "Required Actions"}
  , {Catalog.RKC_USR_CREDENTIAL_HEADER,     "Disabled Credentials"}
  , {Catalog.RKC_USR_SERVER_LABEL,          "Keycloak Sever"}
  , {Catalog.RKC_USR_SERVER_HINT,           "The IT Resource specifying the endpoint configuration to the Server where the Keycloak service is deployed on"}
  , {Catalog.RKC_USR_SID_HINT,              "The identifier used for this account to be identified in target server."}
  , {Catalog.RKC_USR_NAME_HINT,             "The login name used for this account to be authenticated by Keycloak Service."}
  , {Catalog.RKC_USR_PASSWORD_HINT,         "The password used for this account to be authenticated by Keycloak Service."}
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
  , {Catalog.RKC_UGR_LOOKUP_TITLE,          "Keycloak Group"}
  , {Catalog.RKC_UGR_NAME_HINT,             "The name of the group in the Keycloak Server."}
  , {Catalog.RKC_URR_HEADER_TEXT,           "Realm Roles"}
  , {Catalog.RKC_URR_LOOKUP_TITLE,          "Keycloak Realm Role"}
  , {Catalog.RKC_URR_NAME_HINT,             "The name of the realm role in the Keycloak Server."}
  , {Catalog.RKC_UCR_HEADER_TEXT,           "Client Roles"}
  , {Catalog.RKC_UCR_LOOKUP_TITLE,          "Keycloak Client Role"}
  , {Catalog.RKC_UCR_NAME_HINT,             "The name of the client role in the Keycloak Server."}

  /** Constant used to demarcate PLX application entities related messages. */
  , {Catalog.PLX_USR_SERVER_LABEL,          "PLX LDAP Server"}
  , {Catalog.PLX_USR_SERVER_HINT,           "The IT Resource specifying the endpoint configuration to the Server where the PLX LDAP Server is deployed on"}
  , {Catalog.PLX_USR_SID_LABEL,             "SID"}
  , {Catalog.PLX_USR_SID_HINT,              "The SID of the entry associated with this account."}
  , {Catalog.PLX_USR_TENANT_LABEL,          "Tenant"}
  , {Catalog.PLX_USR_TENANT_HINT,           "The main organization this account associated with the identity."}
  , {Catalog.PLX_USR_DN_LABEL,              "Distinguished Name"}
  , {Catalog.PLX_USR_DN_HINT,               "The DN of this account associated with the identity."}
  , {Catalog.PLX_USR_PASSWORD_HINT,         "The password used for this account to be authenticated by PLX"}
  , {Catalog.PLX_USR_EXT_ATTR_HEADER,       "Extension Attributes"}
  , {Catalog.PLX_USR_EXT_ATTR1_LABEL,       "Extension Attribute 1"}
  , {Catalog.PLX_USR_EXT_ATTR1_HINT,        "Additional information beyond the standard set of attribute"}
  , {Catalog.PLX_USR_EXT_ATTR3_LABEL,       "Extension Attribute 3"}
  , {Catalog.PLX_USR_EXT_ATTR3_HINT,        "Additional information beyond the standard set of attribute"}
  , {Catalog.PLX_USR_EXT_ATTR4_LABEL,       "Extension Attribute 4"}
  , {Catalog.PLX_USR_EXT_ATTR4_HINT,        "Additional information beyond the standard set of attribute"}
  , {Catalog.PLX_USR_EXT_ATTR6_LABEL,       "Extension Attribute 6"}
  , {Catalog.PLX_USR_EXT_ATTR6_HINT,        "Additional information beyond the standard set of attribute"}
  , {Catalog.PLX_USR_OBJ_SID_LABEL,         "Object SID"}
  , {Catalog.PLX_USR_OBJ_SID_HINT,          "The Object SID of this account associated with the identity."}
  , {Catalog.PLX_USR_BUSINESS_CAT_LABEL,    "Business Category"}
  , {Catalog.PLX_USR_BUSINESS_CAT_HINT,     "The Business Category of this account associated with the identity."}
  , {Catalog.PLX_USR_CAR_LICENCE_LABEL,     "Car License"}
  , {Catalog.PLX_USR_CAR_LICENCE_HINT,      "The Car License of this account."}
  , {Catalog.PLX_USR_DEPARTMENT_NUM_LABEL,  "Department Number"}
  , {Catalog.PLX_USR_DEPARTMENT_NUM_HINT,   "The Department Number of this account associated with the identity."}
  , {Catalog.PLX_USR_EMPLOYEE_NUM_LABEL,    "Employee Number"}
  , {Catalog.PLX_USR_EMPLOYEE_NUM_HINT,     "The Employee Number of this account."}
  , {Catalog.PLX_USR_PREFERENCE_HEADER,     "Preferences"}
  , {Catalog.PLX_USR_PREF_LANG_LABEL,       "Preferred Language"}
  , {Catalog.PLX_USR_PREF_LANG_HINT,        "The Preferred Language of this account associated with the identity."}
  , {Catalog.PLX_USR_ORG_NAME_LABEL,        "Organizational Name"}
  , {Catalog.PLX_USR_ORG_NAME_HINT,         "The Organizational Name of this account associated with the identity."}
  , {Catalog.PLX_USR_ROOM_NUM_LABEL,        "Room Number"}
  , {Catalog.PLX_USR_ROOM_NUM_HINT,         "The Room Number of this account."}
  , {Catalog.PLX_USR_SECRETARY_LABEL,       "Secretary"}
  , {Catalog.PLX_USR_SECRETARY_HINT,        "The Secretary of this account."}
  , {Catalog.PLX_USR_UID_LABEL,             "UID"}
  , {Catalog.PLX_USR_UID_HINT,              "The User Identifier of this account."}
  , {Catalog.PLX_USR_HOME_POST_ADDR_LABEL,  "Home Postal Address"}
  , {Catalog.PLX_USR_HOME_POST_ADDR_HINT,   "The Home Postal Address of this account associated with the identity."}
  , {Catalog.PLX_USR_DESC_LABEL,            "Description"}
  , {Catalog.PLX_USR_DESC_HINT,             "The Description of this account associated with the identity."}
  , {Catalog.PLX_UGR_LOOKUP_TITLE,          "PLX Group"}
  , {Catalog.PLX_UGR_NAME_HINT,             "The DN of the group in the PLX."}
  , {Catalog.PLX_UPX_HEADER_TEXT,           "Proxy"}
  , {Catalog.PLX_UPX_LOOKUP_TITLE,          "PLX Proxy"}
  , {Catalog.PLX_UPX_NAME_HINT,             "The DN of the proxy in the PLX."}
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