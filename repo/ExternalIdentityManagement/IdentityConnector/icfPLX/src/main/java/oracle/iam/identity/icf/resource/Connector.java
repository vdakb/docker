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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   Connector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Connector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.resource;
////////////////////////////////////////////////////////////////////////////////
// interface Connector
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Connector {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PREFIX                            = "GDS-";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Bundle
  // ~~~~~~~~~ ~~~~~~
  /**
   ** Declares global visible resource identifier used for connector bundle
   ** information purpose.
   */
  public interface Bundle {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String NAME                            = PREFIX + "00001";
    static final String VERSION                         = PREFIX + "00002";
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Endpoint
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** Declares global visible resource identifier used for target endpoint
   ** information purpose.
   */
  public interface Endpoint {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String HOST_LABEL                      = PREFIX + "00011";
    static final String HOST_HINT                       = PREFIX + "00012";
    static final String PORT_LABEL                      = PREFIX + "00013";
    static final String PORT_HINT                       = PREFIX + "00014";
    static final String SECURE_LABEL                    = PREFIX + "00015";
    static final String SECURE_HINT                     = PREFIX + "00016";
    static final String ROOT_CONTEXT_LABEL              = PREFIX + "00017";
    static final String ROOT_CONTEXT_HINT               = PREFIX + "00018";
    static final String PRINCIPAL_USERNAME_LABEL        = PREFIX + "00019";
    static final String PRINCIPAL_USERNAME_HINT         = PREFIX + "00020";
    static final String PRINCIPAL_PASSWORD_LABEL        = PREFIX + "00021";
    static final String PRINCIPAL_PASSWORD_HINT         = PREFIX + "00022";
    static final String COUNTRY_LABEL                   = PREFIX + "00023";
    static final String COUNTRY_HINT                    = PREFIX + "00024";
    static final String LANGUAGE_LABEL                  = PREFIX + "00025";
    static final String LANGUAGE_HINT                   = PREFIX + "00026";
    static final String TIMEZONE_LABEL                  = PREFIX + "00027";
    static final String TIMEZONE_HINT                   = PREFIX + "00028";
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Connection
  // ~~~~~~~~~ ~~~~~~~~~~
  /**
   ** Declares global visible resource identifier used for target endpoint
   ** connection information purpose.
   */
  public interface Connection {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String CONNECT_TIMEOUT_LABEL           = PREFIX + "00041";
    static final String CONNECT_TIMEOUT_HINT            = PREFIX + "00042";
    static final String CONNECT_RETRYCOUNT_LABEL        = PREFIX + "00043";
    static final String CONNECT_RETRYCOUNT_HINT         = PREFIX + "00044";
    static final String CONNECT_RETRYINTERVAL_LABEL     = PREFIX + "00045";
    static final String CONNECT_RETRYINTERVAL_HINT      = PREFIX + "00046";
    static final String RESPONSE_TIMEOUT_LABEL          = PREFIX + "00047";
    static final String RESPONSE_TIMEOUT_HINT           = PREFIX + "00048";
  }


  //////////////////////////////////////////////////////////////////////////////
  // interface Feature
  // ~~~~~~~~~ ~~~~~~~
  /**
   ** Declares global visible resource identifier used for target endpoint
   ** feature information purpose.
   */
  public interface Feature {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String URL_ENCODING_LABEL              = PREFIX + "00051";
    static final String URL_ENCODING_HINT               = PREFIX + "00052";
    static final String CONTEXT_FACTORY_LABEL           = PREFIX + "00053";
    static final String CONTEXT_FACTORY_HINT            = PREFIX + "00054";
    static final String SECURITY_PROVIDER_LABEL         = PREFIX + "00055";
    static final String SECURITY_PROVIDER_HINT          = PREFIX + "00056";
    static final String FAILOVER_LABEL                  = PREFIX + "00057";
    static final String FAILOVER_HINT                   = PREFIX + "00058";
    static final String REFERENTIAL_INTEGRITY_LABEL     = PREFIX + "00059";
    static final String REFERENTIAL_INTEGRITY_HINT      = PREFIX + "00060";
    static final String TIMESTAMP_FORMAT_LABEL          = PREFIX + "00061";
    static final String TIMESTAMP_FORMAT_HINT           = PREFIX + "00062";
    static final String SIMPLEPAGE_CONTROL_LABEL        = PREFIX + "00063";
    static final String SIMPLEPAGE_CONTROL_HINT         = PREFIX + "00064";
    static final String VIRUALLIST_CONTROL_LABEL        = PREFIX + "00065";
    static final String VIRUALLIST_CONTROL_HINT         = PREFIX + "00066";
    static final String SCHEMA_CONTAINER_LABEL          = PREFIX + "00067";
    static final String SCHEMA_CONTAINER_HINT           = PREFIX + "00068";
    static final String CATALOG_CONTAINER_LABEL         = PREFIX + "00069";
    static final String CATALOG_CONTAINER_HINT          = PREFIX + "00070";
    static final String CHANGELOG_CONTAINER_LABEL       = PREFIX + "00071";
    static final String CHANGELOG_CONTAINER_HINT        = PREFIX + "00072";
    static final String CHANGELOG_CHANGETYPE_LABEL      = PREFIX + "00073";
    static final String CHANGELOG_CHANGETYPE_HINT       = PREFIX + "00074";
    static final String CHANGELOG_CHANGENUMBER_LABEL    = PREFIX + "00075";
    static final String CHANGELOG_CHANGENUMBER_HINT     = PREFIX + "00076";
    static final String CHANGELOG_TARGETGUID_LABEL      = PREFIX + "00077";
    static final String CHANGELOG_TARGETGUID_HINT       = PREFIX + "00078";
    static final String CHANGELOG_TARGETDN_LABEL        = PREFIX + "00079";
    static final String CHANGELOG_TARGETDN_HINT         = PREFIX + "00080";
    static final String OBJECTCLASS_NAME_LABEL          = PREFIX + "00081";
    static final String OBJECTCLASS_NAME_HINT           = PREFIX + "00082";
    static final String BINARY_LABEL                    = PREFIX + "00083";
    static final String BINARY_HINT                     = PREFIX + "00084";
    static final String DISTINGUISHED_NAME_LABEL        = PREFIX + "00083";
    static final String DISTINGUISHED_NAME_HINT         = PREFIX + "00084";
    static final String DISTINGUISHED_NAME_PREFIX_LABEL = PREFIX + "00085";
    static final String DISTINGUISHED_NAME_PREFIX_HINT  = PREFIX + "00086";
    static final String ENTRY_IDENTIFIER_PREFIX_LABEL   = PREFIX + "00087";
    static final String ENTRY_IDENTIFIER_PREFIX_HINT    = PREFIX + "00088";
    static final String ENTRY_STATUS_PREFIX_LABEL       = PREFIX + "00089";
    static final String ENTRY_STATUS_PREFIX_HINT        = PREFIX + "00090";
    static final String ENTRY_CREATOR_PREFIX_LABEL      = PREFIX + "00091";
    static final String ENTRY_CREATOR_PREFIX_HINT       = PREFIX + "00092";
    static final String ENTRY_CREATED_PREFIX_LABEL      = PREFIX + "00093";
    static final String ENTRY_CREATED_PREFIX_HINT       = PREFIX + "00094";
    static final String ENTRY_MODIFIER_PREFIX_LABEL     = PREFIX + "00095";
    static final String ENTRY_MODIFIER_PREFIX_HINT      = PREFIX + "00096";
    static final String ENTRY_MODIFIED_PREFIX_LABEL     = PREFIX + "00097";
    static final String ENTRY_MODIFIED_PREFIX_HINT      = PREFIX + "00098";
    static final String ROLE_CLASS_LABEL                = PREFIX + "00099";
    static final String ROLE_CLASS_HINT                 = PREFIX + "00100";
    static final String ROLE_PREFIX_LABEL               = PREFIX + "00101";
    static final String ROLE_PREFIX_HINT                = PREFIX + "00102";
    static final String ROLE_MEMBER_PREFIX_LABEL        = PREFIX + "00103";
    static final String ROLE_MEMBER_PREFIX_HINT         = PREFIX + "00104";
    static final String SCOPE_CLASS_LABEL               = PREFIX + "00105";
    static final String SCOPE_CLASS_HINT                = PREFIX + "00106";
    static final String SCOPE_PREFIX_LABEL              = PREFIX + "00107";
    static final String SCOPE_PREFIX_HINT               = PREFIX + "00108";
    static final String SCOPE_MEMBER_PREFIX_LABEL       = PREFIX + "00109";
    static final String SCOPE_MEMBER_PREFIX_HINT        = PREFIX + "00110";
    static final String GROUP_CLASS_LABEL               = PREFIX + "00111";
    static final String GROUP_CLASS_HINT                = PREFIX + "00112";
    static final String GROUP_PREFIX_LABEL              = PREFIX + "00113";
    static final String GROUP_PREFIX_HINT               = PREFIX + "00114";
    static final String GROUP_MEMBER_PREFIX_LABEL       = PREFIX + "00115";
    static final String GROUP_MEMBER_PREFIX_HINT        = PREFIX + "00116";
    static final String ACCOUNT_CLASS_LABEL             = PREFIX + "00117";
    static final String ACCOUNT_CLASS_HINT              = PREFIX + "00118";
    static final String ACCOUNT_PREFIX_LABEL            = PREFIX + "00119";
    static final String ACCOUNT_PREFIX_HINT             = PREFIX + "00120";
    static final String ACCOUNT_ROLE_PREFIX_LABEL       = PREFIX + "00121";
    static final String ACCOUNT_ROLE_PREFIX_HINT        = PREFIX + "00122";
    static final String ACCOUNT_MEMBER_PREFIX_LABEL     = PREFIX + "00123";
    static final String ACCOUNT_MEMBER_PREFIX_HINT      = PREFIX + "00124";
    static final String ACCOUNT_GROUP_PREFIX_LABEL      = PREFIX + "00125";
    static final String ACCOUNT_GROUP_PREFIX_HINT       = PREFIX + "00126";
    static final String ACCOUNT_CREDENTIAL_PREFIX_LABEL = PREFIX + "00127";
    static final String ACCOUNT_CREDENTIAL_PREFIX_HINT  = PREFIX + "00128";
    static final String PROXY_PREFIX_LABEL              = PREFIX + "00129";
    static final String PROXY_PREFIX_HINT               = PREFIX + "00130";
    static final String PROXY_MEMBER_PREFIX_LABEL       = PREFIX + "00131";
    static final String PROXY_MEMBER_PREFIX_HINT        = PREFIX + "00132";
    static final String HOME_ORGANIZATION_DN_HINT       = PREFIX + "00133";
    static final String HOME_ORGANIZATION_DN_LABEL      = PREFIX + "00134";
    static final String PROXY_ORGANIZATION_DN_HINT      = PREFIX + "00135";
    static final String PROXY_ORGANIZATION_DN_LABEL     = PREFIX + "00136";
  }
}