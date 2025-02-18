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

    File        :   Catalog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Catalog.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package bka.iam.identity.resource;

////////////////////////////////////////////////////////////////////////////////
// interface Catalog
// ~~~~~~~~~ ~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Catalog {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Constant used to demarcate catalog entity related messages. */
  static final String CAT                           = "CAT";
  static final String CAT_RISK_LABEL                = CAT     + "_RISK_LABEL";
  static final String CAT_RISK_HINT                 = CAT     + "_RISK_HINT";
  static final String CAT_ROLE_OWNER_LABEL          = CAT     + "_ROLE_OWNER_LABEL";
  static final String CAT_ROLE_OWNER_HINT           = CAT     + "_ROLE_OWNER_HINT";
  static final String CAT_ROLE_DESCRIPTION_LABEL    = CAT     + "_ROLE_DESCRIPTION_LABEL";
  static final String CAT_ROLE_DESCRIPTION_HINT     = CAT     + "_ROLE_DESCRIPTION_HINT";
  static final String CAT_VALID_FROM_LABEL          = CAT     + "_VALID_FROM_LABEL";
  static final String CAT_VALID_FROM_HINT           = CAT     + "_VALID_FROM_HINT";
  static final String CAT_PRIVILEGE_ACTION_LABEL    = CAT     + "_PRIVILEGE_ACTION_LABEL";
  static final String CAT_PRIVILEGE_ACTION_HINT     = CAT     + "_PRIVILEGE_ACTION_HINT";
  static final String CAT_PRIVILEGE_ASSIGN_LABEL    = CAT     + "_PRIVILEGE_ASSIGN_LABEL";
  static final String CAT_PRIVILEGE_ASSIGN_HINT     = CAT     + "_PRIVILEGE_ASSIGN_HINT";
  static final String CAT_PRIVILEGE_REVOKE_LABEL    = CAT     + "_PRIVILEGE_REVOKE_LABEL";
  static final String CAT_PRIVILEGE_REVOKE_HINT     = CAT     + "_PRIVILEGE_REVOKE_HINT";
  static final String CAT_BASIC_HEADER              = CAT     + "_BASIC_HEADER";
  static final String CAT_ACCOUNT_HEADER            = CAT     + "_ACCOUNT_HEADER";
  static final String CAT_CONTACT_HEADER            = CAT     + "_CONTACT_HEADER";
  static final String CAT_ORGANIZATION_HEADER       = CAT     + "_ORGANIZATION_HEADER";
  static final String CAT_MISCELLANEOUS_HEADER      = CAT     + "_MISCELLANEOUS_HEADER";

  /* Constant used to demarcate common application entities related messages. */
  static final String CAT_USR                       = CAT     + "_USR_";
  static final String CAT_UGP                       = CAT     + "_UGP_";
  static final String CAT_URL                       = CAT     + "_URL_";
  static final String CAT_ORL                       = CAT     + "_ORL_";
  static final String CAT_USR_IDENTIFIER_LABEL      = CAT_USR + "IDENTIFIER_LABEL";
  static final String CAT_USR_LOGIN_NAME_LABEL      = CAT_USR + "LOGIN_NAME_LABEL";
  static final String CAT_USR_UNIFIED_LABEL         = CAT_USR + "UNIFIED_LABEL";
  static final String CAT_USR_UNIFIED_HINT          = CAT_USR + "UNIFIED_HINT";
  static final String CAT_USR_ANONYMIZED_LABEL      = CAT_USR + "ANONYMIZED_LABEL";
  static final String CAT_USR_ANONYMIZED_HINT       = CAT_USR + "ANONYMIZED_HINT";
  static final String CAT_USR_PRINCIPAL_NAME_LABEL  = CAT_USR + "PRINCIPAL_NAME_LABEL";
  static final String CAT_USR_PRINCIPAL_NAME_HINT   = CAT_USR + "PRINCIPAL_NAME_HINT";
  static final String CAT_USR_PASSWORD_LABEL        = CAT_USR + "PASSWORD_LABEL";
  static final String CAT_USR_SAM_LABEL             = CAT_USR + "SAM_LABEL";
  static final String CAT_USR_SAM_HINT              = CAT_USR + "SAM_HINT";
  static final String CAT_USR_WELCOME_LABEL         = CAT_USR + "WELCOME_LABEL";
  static final String CAT_USR_WELCOME_HINT          = CAT_USR + "WELCOME_HINT";
  static final String CAT_USR_TYPE_TITLE            = CAT_USR + "TYPE_TITLE";
  static final String CAT_USR_TYPE_LABEL            = CAT_USR + "TYPE_LABEL";
  static final String CAT_USR_TYPE_HINT             = CAT_USR + "TYPE_HINT";
  static final String CAT_USR_JOBROLE_LABEL         = CAT_USR + "JOBROLE_LABEL";
  static final String CAT_USR_JOBROLE_HINT          = CAT_USR + "JOBROLE_HINT";
  static final String CAT_USR_TITLE_LABEL           = CAT_USR + "TITLE_LABEL";
  static final String CAT_USR_TITLE_HINT            = CAT_USR + "TITLE_HINT";
  static final String CAT_USR_COMMON_NAME_LABEL     = CAT_USR + "COMMON_NAME_LABEL";
  static final String CAT_USR_COMMON_NAME_HINT      = CAT_USR + "COMMON_NAME_HINT";
  static final String CAT_USR_FIRST_NAME_LABEL      = CAT_USR + "FIRST_NAME_LABEL";
  static final String CAT_USR_FIRST_NAME_HINT       = CAT_USR + "FIRST_NAME_HINT";
  static final String CAT_USR_LAST_NAME_LABEL       = CAT_USR + "LAST_NAME_LABEL";
  static final String CAT_USR_LAST_NAME_HINT        = CAT_USR + "LAST_NAME_HINT";
  static final String CAT_USR_MIDDLE_NAME_LABEL     = CAT_USR + "MIDDLE_NAME_LABEL";
  static final String CAT_USR_MIDDLE_NAME_HINT      = CAT_USR + "MIDDLE_NAME_HINT";
  static final String CAT_USR_DISPLAY_NAME_LABEL    = CAT_USR + "DISPLAY_NAME_LABEL";
  static final String CAT_USR_DISPLAY_NAME_HINT     = CAT_USR + "DISPLAY_NAME_HINT";
  static final String CAT_USR_INITIALS_LABEL        = CAT_USR + "INITIALS_LABEL";
  static final String CAT_USR_INITIALS_HINT         = CAT_USR + "INITIALS_HINT";
  static final String CAT_USR_SERVICE_ACCOUNT_LABEL = CAT_USR + "SERVICE_ACCOUNT_LABEL";
  static final String CAT_USR_SERVICE_ACCOUNT_HINT  = CAT_USR + "SERVICE_ACCOUNT_HINT";
  static final String CAT_USR_PARTICIPANT_LABEL     = CAT_USR + "PARTICIPANT_LABEL";
  static final String CAT_USR_PARTICIPANT_HINT      = CAT_USR + "PARTICIPANT_HINT";
  static final String CAT_USR_DIVISION_LABEL        = CAT_USR + "DIVISION_LABEL";
  static final String CAT_USR_DIVISION_HINT         = CAT_USR + "DIVISION_HINT";
  static final String CAT_USR_ORGANIZATION_LABEL    = CAT_USR + "ORGANIZATION_LABEL";
  static final String CAT_USR_ORGANIZATION_HINT     = CAT_USR + "ORGANIZATION_HINT";
  static final String CAT_USR_DEPARTMENT_LABEL      = CAT_USR + "DEPARTMENT_LABEL";
  static final String CAT_USR_DEPARTMENT_HINT       = CAT_USR + "DEPARTMENT_HINT";
  static final String CAT_USR_OFFICE_LABEL          = CAT_USR + "OFFICE_LABEL";
  static final String CAT_USR_OFFICE_HINT           = CAT_USR + "OFFICE_HINT";
  static final String CAT_USR_MANAGER_LABEL         = CAT_USR + "MANAGER_LABEL";
  static final String CAT_USR_MANAGER_HINT          = CAT_USR + "MANAGER_HINT";
  static final String CAT_USR_COMPANY_LABEL         = CAT_USR + "COMPANY_LABEL";
  static final String CAT_USR_COMPANY_HINT          = CAT_USR + "COMPANY_HINT";
  static final String CAT_USR_EMAIL_LABEL           = CAT_USR + "EMAIL_LABEL";
  static final String CAT_USR_EMAIL_HINT            = CAT_USR + "EMAIL_HINT";
  static final String CAT_USR_PHONE_LABEL           = CAT_USR + "PHONE_LABEL";
  static final String CAT_USR_PHONE_HINT            = CAT_USR + "PHONE_HINT";
  static final String CAT_USR_FAX_LABEL             = CAT_USR + "FAX_LABEL";
  static final String CAT_USR_FAX_HINT              = CAT_USR + "FAX_HINT";
  static final String CAT_USR_MOBILE_LABEL          = CAT_USR + "MOBILE_LABEL";
  static final String CAT_USR_MOBILE_HINT           = CAT_USR + "MOBILE_HINT";
  static final String CAT_USR_PAGER_LABEL           = CAT_USR + "PAGER_LABEL";
  static final String CAT_USR_PAGER_HINT            = CAT_USR + "PAGER_HINT";
  static final String CAT_USR_IPPHONE_LABEL         = CAT_USR + "IPPHONE_LABEL";
  static final String CAT_USR_IPPHONE_HINT          = CAT_USR + "IPPHONE_HINT";
  static final String CAT_USR_HOMEPHONE_LABEL       = CAT_USR + "HOMEPHONE_LABEL";
  static final String CAT_USR_HOMEPHONE_HINT        = CAT_USR + "HOMEPHONE_HINT";
  static final String CAT_USR_POSTALCODE_LABEL      = CAT_USR + "POSTALCODE_LABEL";
  static final String CAT_USR_POSTALCODE_HINT       = CAT_USR + "POSTALCODE_HINT";
  static final String CAT_USR_POSTALADR_LABEL       = CAT_USR + "POSTALADR_LABEL";
  static final String CAT_USR_POSTALADR_HINT        = CAT_USR + "POSTALADR_HINT";
  static final String CAT_USR_POBOX_LABEL           = CAT_USR + "POBOX_LABEL";
  static final String CAT_USR_POBOX_HINT            = CAT_USR + "POBOX_HINT";
  static final String CAT_USR_COUNTRY_LABEL         = CAT_USR + "COUNTRY_LABEL";
  static final String CAT_USR_COUNTRY_HINT          = CAT_USR + "COUNTRY_HINT";
  static final String CAT_USR_STATE_LABEL           = CAT_USR + "STATE_LABEL";
  static final String CAT_USR_STATE_HINT            = CAT_USR + "STATE_HINT";
  static final String CAT_USR_LOCALITY_LABEL        = CAT_USR + "LOCALITY_LABEL";
  static final String CAT_USR_LOCALITY_HINT         = CAT_USR + "LOCALITY_HINT";
  static final String CAT_USR_STREET_LABEL          = CAT_USR + "STREET_LABEL";
  static final String CAT_USR_STREET_HINT           = CAT_USR + "STREET_HINT";
  static final String CAT_USR_LANGUAGE_TITLE        = CAT_USR + "LANGUAGE_TITLE";
  static final String CAT_USR_LANGUAGE_LABEL        = CAT_USR + "LANGUAGE_LABEL";
  static final String CAT_USR_LANGUAGE_HINT         = CAT_USR + "LANGUAGE_HINT";
  static final String CAT_USR_TIMEZONE_TITLE        = CAT_USR + "TIMEZONE_TITLE";
  static final String CAT_USR_TIMEZONE_LABEL        = CAT_USR + "TIMEZONE_LABEL";
  static final String CAT_USR_TIMEZONE_HINT         = CAT_USR + "TIMEZONE_HINT";
  static final String CAT_FORM_NAME_LABEL           = CAT     + "_FORM_NAME_LABEL";
  static final String CAT_UGP_NAME_HINT             = CAT_UGP + "NAME_HINT";
  static final String CAT_UGP_HEADER_TEXT           = CAT_UGP + "HEADER_TEXT";
  static final String CAT_UGP_LOOKUP_TITLE          = CAT_UGP + "LOOKUP_TITLE";
  static final String CAT_URL_NAME_HINT             = CAT_URL + "NAME_HINT";
  static final String CAT_URL_HEADER_TEXT           = CAT_URL + "HEADER_TEXT";
  static final String CAT_URL_LOOKUP_TITLE          = CAT_URL + "LOOKUP_TITLE";
  static final String CAT_ORL_HEADER_TEXT           = CAT_ORL + "HEADER_TEXT";
  static final String CAT_ORL_LOOKUP_TITLE          = CAT_ORL + "LOOKUP_TITLE";


  /** Constant used to demarcate application entities related messages. */
  static final String ODS                           = "ODS";
  static final String ODS_USR                       = ODS     + "_USR_";
  static final String ODS_UGP                       = ODS     + "_UGP_";
  static final String ODS_URL                       = ODS     + "_URL_";
  static final String ODS_USR_SERVER_LABEL          = ODS_USR + "SERVER_LABEL";
  static final String ODS_USR_SERVER_HINT           = ODS_USR + "SERVER_HINT";
  static final String ODS_USR_PARENTDN_TITLE        = ODS_USR + "PARENTDN_TITLE";
  static final String ODS_USR_PARENTDN_LABEL        = ODS_USR + "PARENTDN_LABEL";
  static final String ODS_USR_PARENTDN_HINT         = ODS_USR + "PARENTDN_HINT";
  static final String ODS_USR_IDENTIFIER_HINT       = ODS_USR + "IDENTIFIER_HINT";
  static final String ODS_USR_LOGIN_NAME_HINT       = ODS_USR + "LOGIN_NAME_HINT";
  static final String ODS_USR_PASSWORD_HINT         = ODS_USR + "PASSWORD_HINT";
  static final String ODS_USR_LANGUAGE_TITLE        = ODS_USR + "LANGUAGE_TITLE";
  static final String ODS_USR_COUNTRY_TITLE         = ODS_USR + "COUNTRY_TITLE";
  static final String ODS_USR_TIMEZONE_TITLE        = ODS_USR + "TIMEZONE_TITLE";
  static final String ODS_UGP_LOOKUP_TITLE          = ODS_UGP + "LOOKUP_TITLE";
  static final String ODS_UGP_NAME_HINT             = ODS_UGP + "NAME_HINT";
  static final String ODS_URL_LOOKUP_TITLE          = ODS_URL + "LOOKUP_TITLE";
  static final String ODS_URL_NAME_HINT             = ODS_URL + "NAME_HINT";

  /** Constant used to demarcate application entities related messages. */
  static final String FBS                           = "FBS";
  static final String FBS_USR                       = FBS     + "_USR_";
  static final String FBS_EML                       = FBS     + "_M_";
  static final String FBS_PHN                       = FBS     + "_P_";
  static final String FBS_REQ                       = FBS     + "_REQ_";
  static final String FBS_USR_SERVER_LABEL          = FBS_USR + "SERVER_LABEL";
  static final String FBS_USR_SERVER_HINT           = FBS_USR + "SERVER_HINT";
  static final String FBS_USR_AID_LABEL             = FBS_USR + "AID_LABEL";
  static final String FBS_USR_AID_HINT              = FBS_USR + "AID_HINT";
  static final String FBS_USR_UID_HINT              = FBS_USR + "UID_HINT";
  static final String FBS_USR_VALID_FROM_LABEL      = FBS_USR + "VALID_FROM_LABEL";
  static final String FBS_USR_VALID_FROM_HINT       = FBS_USR + "VALID_FROM_HINT";
  static final String FBS_USR_VALID_TO_LABEL        = FBS_USR + "VALID_TO_LABEL";
  static final String FBS_USR_VALID_TO_HINT         = FBS_USR + "VALID_TO_HINT";
  static final String FBS_USR_ADMIN_ACCOUNT_LABEL   = FBS_USR + "ADMIN_ACCOUNT_LABEL";
  static final String FBS_USR_ADMIN_ACCOUNT_HINT    = FBS_USR + "ADMIN_ACCOUNT_HINT";
  static final String FBS_EML_HEADER_TEXT           = FBS_EML + "HEADER_TEXT";
  static final String FBS_EML_VALUE_LABEL           = FBS_EML + "VALUE_LABEL";
  static final String FBS_EML_VALUE_HINT            = FBS_EML + "VALUE_HINT";
  static final String FBS_PHN_HEADER_TEXT           = FBS_PHN + "HEADER_TEXT";
  static final String FBS_PHN_VALUE_LABEL           = FBS_PHN + "VALUE_LABEL";
  static final String FBS_PHN_VALUE_HINT            = FBS_PHN + "VALUE_HINT";
  static final String FBS_REQ_EDU_LABEL             = FBS_REQ + "EDU_LABEL";
  static final String FBS_REQ_PRD_LABEL             = FBS_REQ + "PRD_LABEL";
  static final String FBS_REQ_SU_LABEL              = FBS_REQ + "SU_LABEL";
  static final String FBS_REQ_GSU_LABEL             = FBS_REQ + "GSU_LABEL";
  static final String FBS_REQ_FA_LABEL              = FBS_REQ + "FA_LABEL";
  static final String FBS_REQ_SR_LABEL              = FBS_REQ + "SR_LABEL";
  static final String FBS_REQ_GFA_LABEL             = FBS_REQ + "GFA_LABEL";
  static final String FBS_REQ_GED_LABEL             = FBS_REQ + "GED_LABEL";

  /** Constant used to demarcate application entities related messages. */
  static final String ADS                           = "ADS";
  static final String ADS_USR                       = ADS     + "_USR_";
  static final String ADS_USR_SERVER_LABEL          = ADS_USR + "SERVER_LABEL";
  static final String ADS_USR_SERVER_HINT           = ADS_USR + "SERVER_HINT";
  static final String ADS_USR_MAILREDIRECT_LABEL    = ADS_USR + "MAILREDIRECT_LABEL";
  static final String ADS_USR_MAILREDIRECT_HINT     = ADS_USR + "MAILREDIRECT_HINT";
  static final String ADS_USR_LOCKED_LABEL          = ADS_USR + "LOCKED_LABEL";
  static final String ADS_USR_LOCKED_HINT           = ADS_USR + "LOCKED_HINT";
  static final String ADS_USR_MUST_LABEL            = ADS_USR + "MUST_LABEL";
  static final String ADS_USR_MUST_HINT             = ADS_USR + "MUST_HINT";
  static final String ADS_USR_NEVER_LABEL           = ADS_USR + "NEVER_LABEL";
  static final String ADS_USR_NEVER_HINT            = ADS_USR + "NEVER_HINT";
  static final String ADS_USR_PASSWORDNO_LABEL      = ADS_USR + "PASSWORDNO_LABEL";
  static final String ADS_USR_PASSWORDNO_HINT       = ADS_USR + "PASSWORDNO_HINT";
  static final String ADS_USR_EXPIRATION_LABEL      = ADS_USR + "EXPIRATION_LABEL";
  static final String ADS_USR_EXPIRATION_HINT       = ADS_USR + "EXPIRATION_HINT";
  static final String ADS_USR_USRHOMEDIR_LABEL      = ADS_USR + "USRHOMEDIR_LABEL";
  static final String ADS_USR_USRHOMEDIR_HINT       = ADS_USR + "USRHOMEDIR_HINT";
  static final String ADS_USR_TSSHOMEDIR_LABEL      = ADS_USR + "TSSHOMEDIR_LABEL";
  static final String ADS_USR_TSSHOMEDIR_HINT       = ADS_USR + "TSSHOMEDIR_HINT";
  static final String ADS_USR_TSSLOGINALLOW_LABEL   = ADS_USR + "TSSLOGINALLOW_LABEL";
  static final String ADS_USR_TSSLOGINALLOW_HINT    = ADS_USR + "TSSLOGINALLOW_HINT";
  static final String ADS_USR_TSSPROFILEPATH_LABEL  = ADS_USR + "TSSPROFILEPATH_LABEL";
  static final String ADS_USR_TSSPROFILEPATH_HINT   = ADS_USR + "TSSPROFILEPATH_HINT";

  /** Constant used to demarcate application entities related messages. */
  static final String PCF                           = "PCF";
  static final String PCF_USR                       = PCF     + "_USR_";
  static final String PCF_UGP                       = PCF     + "_UGP_";
  static final String PCF_ORL                       = PCF     + "_ORL_";
  static final String PCF_SRL                       = PCF     + "_SRL_";
  static final String PCF_USR_SVC_LABEL             = PCF_USR + "SVC_LABEL";
  static final String PCF_USR_SVC_HINT              = PCF_USR + "SVC_HINT";
  static final String PCF_USR_SID_HINT              = PCF_USR + "SID_HINT";
  static final String PCF_USR_UID_HINT              = PCF_USR + "UID_HINT";
  static final String PCF_USR_EID_LABEL             = PCF_USR + "EID_LABEL";
  static final String PCF_USR_EID_HINT              = PCF_USR + "EID_HINT";
  static final String PCF_USR_PWD_HINT              = PCF_USR + "PWD_HINT";
  static final String PCF_USR_OID_LABEL             = PCF_USR + "OID_LABEL";
  static final String PCF_USR_OID_TITLE             = PCF_USR + "OID_TITLE";
  static final String PCF_USR_OID_HINT              = PCF_USR + "OID_HINT";
  static final String PCF_USR_IDP_TITLE             = PCF_USR + "IDP_TITLE";
  static final String PCF_USR_IDP_LABEL             = PCF_USR + "IDP_LABEL";
  static final String PCF_USR_IDP_HINT              = PCF_USR + "IDP_HINT";
  static final String PCF_USR_VFD_LABEL             = PCF_USR + "VFD_LABEL";
  static final String PCF_USR_VFD_HINT              = PCF_USR + "VFD_HINT";
  static final String PCF_UGP_SID_HINT              = PCF_UGP + "SID_HINT";
  static final String PCF_ORL_SID_HINT              = PCF_ORL + "SID_HINT";
  static final String PCF_ORL_SCP_TITLE             = PCF_ORL + "SCP_TITLE";
  static final String PCF_ORL_SCP_LABEL             = PCF_ORL + "SCP_LABEL";
  static final String PCF_ORL_SCP_HINT              = PCF_ORL + "SCP_HINT";
  static final String PCF_SRL_HEADER                = PCF_SRL + "HEADER";
  static final String PCF_SRL_SID_TITLE             = PCF_SRL + "SID_TITLE";
  static final String PCF_SRL_SID_HINT              = PCF_SRL + "SID_HINT";
  static final String PCF_SRL_SCP_TITLE             = PCF_SRL + "SCP_TITLE";
  static final String PCF_SRL_SCP_LABEL             = PCF_SRL + "SCP_LABEL";
  static final String PCF_SRL_SCP_HINT              = PCF_SRL + "SCP_HINT";

  /** Constant used to demarcate application entities related messages. */
  static final String AJS                           = "AJS";
  static final String AJS_USR                       = AJS     + "_USR_";
  static final String AJS_GRP                       = AJS     + "_GRP_";
  static final String AJS_PRJ                       = AJS     + "_PRJ_";
  static final String AJS_USR_SVC_LABEL             = AJS_USR + "SVC_LABEL";
  static final String AJS_USR_SVC_HINT              = AJS_USR + "SVC_HINT";
  static final String AJS_USR_UID_HINT              = AJS_USR + "UID_HINT";
  static final String AJS_USR_PWD_HINT              = AJS_USR + "PWD_HINT";
  static final String AJS_GRP_GID_HINT              = AJS_GRP + "GID_HINT";
  static final String AJS_PRJ_HEADER                = AJS_PRJ + "HEADER";
  static final String AJS_PRJ_PID_TITLE             = AJS_PRJ + "PID_TITLE";
  static final String AJS_PRJ_PID_HINT              = AJS_PRJ + "PID_HINT";
  static final String AJS_PRJ_RID_HINT              = AJS_PRJ + "RID_HINT";

  /** Constant used to demarcate application entities related messages. */
  static final String GAE                           = "GAE";
  static final String GAE_USR                       = GAE     + "_USR_";
  static final String GAE_URL                       = GAE     + "_URL_";
  static final String GAE_DAP                       = GAE     + "_DAP_";
  static final String GAE_USR_SVC_LABEL             = GAE_USR + "SVC_LABEL";
  static final String GAE_USR_SVC_HINT              = GAE_USR + "SVC_HINT";
  static final String GAE_USR_OID_LABEL             = GAE_USR + "OID_LABEL";
  static final String GAE_USR_OID_HINT              = GAE_USR + "OID_HINT";
  static final String GAE_USR_UID_LABEL             = GAE_USR + "UID_LABEL";
  static final String GAE_USR_UID_HINT              = GAE_USR + "UID_HINT";
  static final String GAE_USR_UPN_LABEL             = GAE_USR + "UPN_LABEL";
  static final String GAE_USR_UPN_HINT              = GAE_USR + "UPN_HINT";
  static final String GAE_USR_PWD_HINT              = GAE_USR + "PWD_HINT";
  static final String GAE_URL_SID_LABEL             = GAE_URL + "SID_LABEL";
  static final String GAE_URL_SID_HINT              = GAE_URL + "SID_HINT";
  static final String GAE_URL_RID_TITLE             = GAE_URL + "RID_TITLE";
  static final String GAE_URL_RID_HINT              = GAE_URL + "RID_HINT";
  static final String GAE_DAP_HEADER                = GAE_DAP + "HEADER";
  static final String GAE_DAP_SID_TITLE             = GAE_DAP + "SID_TITLE";
  static final String GAE_DAP_SID_HINT              = GAE_DAP + "SID_HINT";

  /** Constant used to demarcate application entities related messages. */
  static final String OFS                           = "OFS";
  static final String OFS_USR                       = OFS     + "_USR_";
  static final String OFS_UGP                       = OFS     + "_UGP_";
  static final String OFS_USR_SVC_LABEL             = OFS_USR + "SVC_LABEL";
  static final String OFS_USR_SVC_HINT              = OFS_USR + "SVC_HINT";
  static final String OFS_USR_UID_LABEL             = OFS_USR + "UID_LABEL";
  static final String OFS_USR_UID_HINT              = OFS_USR + "UID_HINT";
  static final String OFS_USR_PWD_HINT              = OFS_USR + "PWD_HINT";
  static final String OFS_USR_ADM_LABEL             = OFS_USR + "ADM_LABEL";
  static final String OFS_USR_ADM_HINT              = OFS_USR + "ADM_HINT";
  static final String OFS_UGP_GID_HINT              = OFS_UGP + "GID_HINT";
  static final String OFS_UGP_ADM_LABEL             = OFS_UGP + "ADM_LABEL";
  static final String OFS_UGP_ADM_HINT              = OFS_UGP + "ADM_HINT";

  /** Constant used to demarcate application entities related messages. */
  static final String MDL                           = "MDL";
  static final String MDL_USR                       = MDL     + "_USR_";
  static final String MDL_USR_SVC_LABEL             = MDL_USR + "SVC_LABEL";
  static final String MDL_USR_SVC_HINT              = MDL_USR + "SVC_HINT";
  static final String MDL_USR_UID_HINT              = MDL_USR + "UID_HINT";
  static final String MDL_USR_UPN_HINT              = MDL_USR + "UPN_HINT";
  static final String MDL_USR_PWD_HINT              = MDL_USR + "PWD_HINT";

  /** Constant used to demarcate application entities related messages. */
  static final String UPC                           = "UPC";
  static final String UPC_USR                       = UPC     + "_USR_";
  static final String UPC_UGP                       = UPC     + "_UGP_";
  static final String UPC_USR_SVC_LABEL             = UPC_USR + "SVC_LABEL";
  static final String UPC_USR_SVC_HINT              = UPC_USR + "SVC_HINT";
  static final String UPC_USR_SID_HINT              = UPC_USR + "SID_HINT";
  static final String UPC_USR_UID_HINT              = UPC_USR + "UID_HINT";
  static final String UPC_USR_PWD_HINT              = UPC_USR + "PWD_HINT";
  static final String UPC_UGP_SID_HINT              = UPC_UGP + "SID_HINT";

  /** Constant used to demarcate application entities related messages. */
  static final String IGS                           = "IGS";
  static final String IGS_USR                       = IGS     + "_USR_";
  static final String IGS_URL                       = IGS     + "_URL_";
  static final String IGS_UTN                       = IGS     + "_UTN_";
  static final String IGS_USR_SVC_LABEL             = IGS_USR + "SVC_LABEL";
  static final String IGS_USR_SVC_HINT              = IGS_USR + "SVC_HINT";
  static final String IGS_USR_SID_HINT              = IGS_USR + "SID_HINT";
  static final String IGS_USR_UID_HINT              = IGS_USR + "UID_HINT";
  static final String IGS_USR_PWD_HINT              = IGS_USR + "PWD_HINT";
  static final String IGS_URL_SID_HINT              = IGS_URL + "SID_HINT";
  static final String IGS_UTN_HEADER                = IGS_UTN + "HEADER";
  static final String IGS_UTN_SID_TITLE             = IGS_UTN + "SID_TITLE";
  static final String IGS_UTN_SID_HINT              = IGS_UTN + "SID_HINT";
  static final String IGS_UTN_RID_TITLE             = IGS_UTN + "RID_TITLE";
  static final String IGS_UTN_RID_HINT              = IGS_UTN + "RID_HINT";

  /** Constant used to demarcate application entities related messages. */
  static final String OIG                           = "OIG";
  static final String OIG_USR                       = OIG     + "_USR_";
  static final String OIG_UPR                       = OIG     + "_UPR_";
  static final String OIG_UPG                       = OIG     + "_UPG_";
  static final String OIG_UPS                       = OIG     + "_UPS_";
  static final String OIG_UPO                       = OIG     + "_UPO_";
  static final String OIG_USR_SVC_LABEL             = OIG_USR + "SVC_LABEL";
  static final String OIG_USR_SVC_HINT              = OIG_USR + "SVC_HINT";
  static final String OIG_USR_SID_HINT              = OIG_USR + "SID_HINT";
  static final String OIG_USR_UID_HINT              = OIG_USR + "UID_HINT";
  static final String OIG_UPR_HEADER                = OIG_UPR + "HEADER";
  static final String OIG_UPR_SID_TITLE             = OIG_UPR + "SID_TITLE";
  static final String OIG_UPR_SID_HINT              = OIG_UPR + "SID_HINT";
  static final String OIG_UPG_HEADER                = OIG_UPG + "HEADER";
  static final String OIG_UPG_SID_TITLE             = OIG_UPG + "SID_TITLE";
  static final String OIG_UPG_SID_HINT              = OIG_UPG + "SID_HINT";
  static final String OIG_UPS_HEADER                = OIG_UPS + "HEADER";
  static final String OIG_UPS_SID_TITLE             = OIG_UPS + "SID_TITLE";
  static final String OIG_UPS_SID_HINT              = OIG_UPS + "SID_HINT";
  static final String OIG_UPS_SCP_TITLE             = OIG_UPS + "SCP_TITLE";
  static final String OIG_UPS_SCP_LABEL             = OIG_UPS + "SCP_LABEL";
  static final String OIG_UPS_SCP_HINT              = OIG_UPS + "SCP_HINT";
  static final String OIG_UPS_HRC_LABEL             = OIG_UPS + "HRC_LABEL";
  static final String OIG_UPS_HRC_HINT              = OIG_UPS + "HRC_HINT";
  static final String OIG_UPO_HEADER                = OIG_UPO + "HEADER";
  static final String OIG_UPO_SID_TITLE             = OIG_UPO + "SID_TITLE";
  static final String OIG_UPO_SID_HINT              = OIG_UPO + "SID_HINT";
  static final String OIG_UPO_OU_LABEL              = OIG_UPO + "OU_LABEL";
  static final String OIG_UPO_OU_HINT               = OIG_UPO + "OU_HINT";
  static final String OIG_UPO_OU_TITLE              = OIG_UPO + "OU_TITLE";
  
  

  /** Constant used to demarcate BKA Directory application entities related messages. */
  static final String BDS                           = "BDS";
  static final String BDS_USR                       = BDS     + "_USR_";
  static final String BDS_USR_SERVER_LABEL          = BDS_USR + "SERVER_LABEL";
  static final String BDS_USR_SERVER_HINT           = BDS_USR + "SERVER_HINT";

  /** Constant used to demarcate RedHat Keycloak application entities related messages. */
  static final String RKC                           = "RKC";
  static final String RKC_USR                       = RKC     + "_USR_";
  static final String RKC_UGR                       = RKC     + "_UGR_";
  static final String RKC_URR                       = RKC     + "_URR_";
  static final String RKC_UCR                       = RKC     + "_UCR_";
  static final String RKC_USR_ACTION_HEADER         = RKC_USR + "ACTION_HEADER";
  static final String RKC_USR_CREDENTIAL_HEADER     = RKC_USR + "CREDENTIAL_HEADER";
  static final String RKC_USR_SERVER_LABEL          = RKC_USR + "SERVER_LABEL";
  static final String RKC_USR_SERVER_HINT           = RKC_USR + "SERVER_HINT";
  static final String RKC_USR_SID_HINT              = RKC_USR + "SID_HINT";
  static final String RKC_USR_NAME_HINT             = RKC_USR + "NAME_HINT";
  static final String RKC_USR_PASSWORD_HINT         = RKC_USR + "PASSWORD_HINT";
  static final String RKC_USR_VERIFIED_LABEL        = RKC_USR + "VERIFIED_LABEL";
  static final String RKC_USR_VERIFIED_HINT         = RKC_USR + "VERIFIED_HINT";
  static final String RKC_USR_OTP_LABEL             = RKC_USR + "OTP_LABEL";
  static final String RKC_USR_OTP_HINT              = RKC_USR + "OTP_HINT";
  static final String RKC_USR_NOTBEF_LABEL          = RKC_USR + "NOTBEF_LABEL";
  static final String RKC_USR_NOTBEF_HINT           = RKC_USR + "NOTBEF_HINT";
  static final String RKC_USR_AEV_LABEL             = RKC_USR + "AEV_LABEL";
  static final String RKC_USR_AEV_HINT              = RKC_USR + "AEV_HINT";
  static final String RKC_USR_ADA_LABEL             = RKC_USR + "ADA_LABEL";
  static final String RKC_USR_ADA_HINT              = RKC_USR + "ADA_HINT";
  static final String RKC_USR_APV_LABEL             = RKC_USR + "APV_LABEL";
  static final String RKC_USR_APV_HINT              = RKC_USR + "APV_HINT";
  static final String RKC_USR_APU_LABEL             = RKC_USR + "APU_LABEL";
  static final String RKC_USR_APU_HINT              = RKC_USR + "APU_HINT";
  static final String RKC_USR_AUP_LABEL             = RKC_USR + "AUP_LABEL";
  static final String RKC_USR_AUP_HINT              = RKC_USR + "AUP_HINT";
  static final String RKC_USR_ALU_LABEL             = RKC_USR + "ALU_LABEL";
  static final String RKC_USR_ALU_HINT              = RKC_USR + "ALU_HINT";
  static final String RKC_USR_ACO_LABEL             = RKC_USR + "ACO_LABEL";
  static final String RKC_USR_ACO_HINT              = RKC_USR + "ACO_HINT";
  static final String RKC_USR_ATC_LABEL             = RKC_USR + "ATC_LABEL";
  static final String RKC_USR_ATC_HINT              = RKC_USR + "ATC_HINT";
  static final String RKC_USR_AWR_LABEL             = RKC_USR + "AWR_LABEL";
  static final String RKC_USR_AWR_HINT              = RKC_USR + "AWR_HINT";
  static final String RKC_USR_AWP_LABEL             = RKC_USR + "AWP_LABEL";
  static final String RKC_USR_AWP_HINT              = RKC_USR + "AWP_HINT";
  static final String RKC_USR_ADC_LABEL             = RKC_USR + "ADC_LABEL";
  static final String RKC_USR_ADC_HINT              = RKC_USR + "ADC_HINT";
  static final String RKC_UGR_LOOKUP_TITLE          = RKC_UGR + "LOOKUP_TITLE";
  static final String RKC_UGR_NAME_HINT             = RKC_UGR + "NAME_HINT";
  static final String RKC_URR_HEADER_TEXT           = RKC_URR + "HEADER_TEXT";
  static final String RKC_URR_LOOKUP_TITLE          = RKC_URR + "LOOKUP_TITLE";
  static final String RKC_URR_NAME_HINT             = RKC_URR + "NAME_HINT";
  static final String RKC_UCR_HEADER_TEXT           = RKC_UCR + "HEADER_TEXT";
  static final String RKC_UCR_LOOKUP_TITLE          = RKC_UCR + "LOOKUP_TITLE";
  static final String RKC_UCR_NAME_HINT             = RKC_UCR + "NAME_HINT";
  
  /** Constant used to demarcate PLX application entities related messages. */
  static final String PLX                           = "PLX";
  static final String PLX_USR                       = PLX     + "_USR_";
  static final String PLX_UGR                       = PLX     + "_UGR_";
  static final String PLX_UPX                       = PLX     + "_UPX_";
  static final String PLX_USR_SERVER_LABEL          = PLX_USR + "SERVER_LABEL";
  static final String PLX_USR_SERVER_HINT           = PLX_USR + "SERVER_HINT";
  static final String PLX_USR_SID_LABEL             = PLX_USR + "SID_LABEL";
  static final String PLX_USR_SID_HINT              = PLX_USR + "SID_HINT";
  static final String PLX_USR_TENANT_LABEL          = PLX_USR + "TENANT_LABEL";
  static final String PLX_USR_TENANT_HINT           = PLX_USR + "TENANT_HINT";
  static final String PLX_USR_DN_LABEL              = PLX_USR + "DN_LABEL";
  static final String PLX_USR_DN_HINT               = PLX_USR + "DN_HINT";
  static final String PLX_USR_PASSWORD_HINT         = PLX_USR + "PASSWORD_HINT";
  static final String PLX_USR_EXT_ATTR_HEADER       = PLX_USR + "EXT_ATTR_HEADER";
  static final String PLX_USR_EXT_ATTR1_LABEL       = PLX_USR + "EXT_ATTR1_LABEL";
  static final String PLX_USR_EXT_ATTR1_HINT        = PLX_USR + "EXT_ATTR1_HINT";
  static final String PLX_USR_EXT_ATTR3_LABEL       = PLX_USR + "EXT_ATTR3_LABEL";
  static final String PLX_USR_EXT_ATTR3_HINT        = PLX_USR + "EXT_ATTR3_HINT";
  static final String PLX_USR_EXT_ATTR4_HINT        = PLX_USR + "EXT_ATTR4_HINT";
  static final String PLX_USR_EXT_ATTR4_LABEL       = PLX_USR + "EXT_ATTR4_LABEL";
  static final String PLX_USR_EXT_ATTR6_HINT        = PLX_USR + "EXT_ATTR6_HINT";
  static final String PLX_USR_EXT_ATTR6_LABEL       = PLX_USR + "EXT_ATTR6_LABEL";
  static final String PLX_USR_OBJ_SID_HINT          = PLX_USR + "OBJ_SID_HINT";
  static final String PLX_USR_OBJ_SID_LABEL         = PLX_USR + "OBJ_SID_LABEL";
  static final String PLX_USR_BUSINESS_CAT_LABEL    = PLX_USR + "BUSINESS_CAT_LABEL";
  static final String PLX_USR_BUSINESS_CAT_HINT     = PLX_USR + "BUSINESS_CAT_HINT";
  static final String PLX_USR_CAR_LICENCE_LABEL     = PLX_USR + "CAR_LICENCE_LABEL";
  static final String PLX_USR_CAR_LICENCE_HINT      = PLX_USR + "CAR_LICENCE_HINT";
  static final String PLX_USR_DEPARTMENT_NUM_LABEL  = PLX_USR + "DEPARTMENT_NUM_LABEL";
  static final String PLX_USR_DEPARTMENT_NUM_HINT   = PLX_USR + "DEPARTMENT_NUM_HINT";
  static final String PLX_USR_EMPLOYEE_NUM_LABEL    = PLX_USR + "EMPLOYEE_NUM_LABEL";
  static final String PLX_USR_EMPLOYEE_NUM_HINT     = PLX_USR + "EMPLOYEE_NUM_HINT";
  static final String PLX_USR_HOME_POST_ADDR_LABEL  = PLX_USR + "HOME_POST_ADDR_LABEL";
  static final String PLX_USR_HOME_POST_ADDR_HINT   = PLX_USR + "HOME_POST_ADDR_HINT";
  static final String PLX_USR_ORG_NAME_LABEL        = PLX_USR + "ORG_NAME_LABEL";
  static final String PLX_USR_ORG_NAME_HINT         = PLX_USR + "ORG_NAME_HINT";
  static final String PLX_USR_PREFERENCE_HEADER     = PLX_USR + "PREFERENCE_HEADER";
  static final String PLX_USR_PREF_LANG_LABEL       = PLX_USR + "PREF_LANG_LABEL";
  static final String PLX_USR_PREF_LANG_HINT        = PLX_USR + "PREF_LANG_HINT";
  static final String PLX_USR_ROOM_NUM_LABEL        = PLX_USR + "ROOM_NUM_LABEL";
  static final String PLX_USR_ROOM_NUM_HINT         = PLX_USR + "ROOM_NUM_HINT";
  static final String PLX_USR_SECRETARY_LABEL       = PLX_USR + "SECRETARY_LABEL";
  static final String PLX_USR_SECRETARY_HINT        = PLX_USR + "SECRETARY_HINT";
  static final String PLX_USR_UID_LABEL             = PLX_USR + "UID_LABEL";
  static final String PLX_USR_UID_HINT              = PLX_USR + "UID_HINT";
  static final String PLX_USR_DESC_LABEL            = PLX_USR + "DESC_LABEL";
  static final String PLX_USR_DESC_HINT             = PLX_USR + "DESC_HINT";
  static final String PLX_UGR_LOOKUP_TITLE          = PLX_UGR + "LOOKUP_TITLE";
  static final String PLX_UGR_NAME_HINT             = PLX_UGR + "NAME_HINT";
  static final String PLX_UPX_HEADER_TEXT           = PLX_UPX + "HEADER_TEXT";
  static final String PLX_UPX_LOOKUP_TITLE          = PLX_UPX + "LOOKUP_TITLE";
  static final String PLX_UPX_NAME_HINT             = PLX_UPX + "NAME_HINT";
}