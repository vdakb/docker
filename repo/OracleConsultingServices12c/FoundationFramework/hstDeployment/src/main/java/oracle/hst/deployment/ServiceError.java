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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ServiceError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment;

////////////////////////////////////////////////////////////////////////////////
// interface ServiceError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ServiceError extends ServiceConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 00001 - 00010 task related errors
  static final String UNHANDLED                             = PREFIX + "00001";
  static final String GENERAL                               = PREFIX + "00002";
  static final String ABORT                                 = PREFIX + "00003";
  static final String NOTIMPLEMENTED                        = PREFIX + "00004";
  static final String CLASSNOTFOUND                         = PREFIX + "00005";
  static final String CLASSNOTCREATE                        = PREFIX + "00006";
  static final String CLASSINVALID                          = PREFIX + "00007";
  static final String CLASSCONSTRUCTOR                      = PREFIX + "00008";
  static final String CLASSCONSTRUCTORARG                   = PREFIX + "00009";
  static final String CLASSCONSTRUCTORNOARG                 = PREFIX + "00010";

  // 00011 - 00020 task related errors
  static final String CONSOLE_UNAVAILABLE                   = PREFIX + "00011";
  static final String CONSOLE_INPUT                         = PREFIX + "00012";
  static final String TASK_ATTRIBUTE_MISSING                = PREFIX + "00013";
  static final String TASK_ATTRIBUTE_MIXEDUP                = PREFIX + "00014";
  static final String TASK_ELEMENT_MISSING                  = PREFIX + "00015";
  static final String TASK_ELEMENT_MIXEDUP                  = PREFIX + "00016";
  static final String TASK_ELEMENT_ONLYONCE                 = PREFIX + "00017";
  static final String TASK_ELEMENT_UNEXPECTED               = PREFIX + "00018";
  static final String TASK_ELEMENT_UNEXPECTED_CONTEXT       = PREFIX + "00019";

  // 00021 - 00030 type related errors
  static final String TYPE_ATTRIBUTE_MANDATORY              = PREFIX + "00021";
  static final String TYPE_ATTRIBUTE_FORMAT                 = PREFIX + "00022";
  static final String TYPE_ATTRIBUTE_VALUE                  = PREFIX + "00023";
  static final String TYPE_ATTRIBUTE_MISSING                = PREFIX + "00024";
  static final String TYPE_ATTRIBUTE_MIXEDUP                = PREFIX + "00025";
  static final String TYPE_REFERENCE_MISMATCH               = PREFIX + "00026";
  static final String TYPE_REFERENCE_MISSING                = PREFIX + "00027";
  static final String TYPE_PARAMETER_EMPTY                  = PREFIX + "00028";
  static final String TYPE_PARAMETER_MISSING                = PREFIX + "00029";
  static final String TYPE_PARAMETER_MANDATORY              = PREFIX + "00030";

  // 00031 - 00040 context related errors
  static final String CONTEXT_MANDATORY                     = PREFIX + "00031";
  static final String CONTEXT_INITIALIZE                    = PREFIX + "00032";
  static final String CONTEXT_CONNECTION                    = PREFIX + "00033";
  static final String CONTEXT_ACCESS_DENIED                 = PREFIX + "00034";
  static final String CONTEXT_CLOSE                         = PREFIX + "00035";
  static final String CONTEXT_ENVIRONMENT                   = PREFIX + "00036";

  // 00041 - 00050 XML structure related errors
  static final String OBJECT_ELEMENT_MANDATORY              = PREFIX + "00040";
  static final String OBJECT_ELEMENT_ONLYONCE               = PREFIX + "00041";
  static final String OBJECT_ELEMENT_NOTFOUND               = PREFIX + "00042";
  static final String OBJECT_ELEMENT_AMBIGUOS               = PREFIX + "00043";
  static final String OBJECT_ELEMENT_EXISTS                 = PREFIX + "00044";
  static final String OBJECT_ELEMENT_ALREADYASSINGED        = PREFIX + "00045";
  static final String OBJECT_ELEMENT_NOTASSINGED            = PREFIX + "00046";
  static final String OBJECT_OPERATION_MANDATORY            = PREFIX + "00047";
  static final String OBJECT_OPERATION_INVALID              = PREFIX + "00048";
  static final String OBJECT_PARAMETER_MANDATORY            = PREFIX + "00049";
  static final String OBJECT_PARAMETER_ONLYONCE             = PREFIX + "00050";

  // 00051 - 00060 server related errors
  static final String CONTROL_OBJECTNAME_MALFORMED          = PREFIX + "00051";
  static final String CONTROL_SERVERNAME_NOTFOUND           = PREFIX + "00052";
  static final String CONTROL_ATTRIBUTENAME_NOTFOUND        = PREFIX + "00053";
  static final String CONTROL_BEANINSTANCE_NOTFOUND         = PREFIX + "00054";
  static final String CONTROL_QUERY_FAILED                  = PREFIX + "00055";

  // 00061 - 00070 regular expression related errors
  static final String EXPRESSION_INVALID                    = PREFIX + "00061";
  static final String EXPRESSION_UNDEFINED_BITVALUES        = PREFIX + "00062";

  // 00071 - 00080 metadata namespace related errors
  static final String METADATA_CONFIG_ONLYONCE              = PREFIX + "00071";
  static final String CUSTCLASS_CONFIG_ONLYONCE             = PREFIX + "00072";
  static final String METADATA_NAMESPACE_ABSOLUE            = PREFIX + "00073";
  static final String METADATA_NAMESPACE_NOTEXISTS          = PREFIX + "00074";
  static final String METADATA_NAMESPACE_REFERENCE          = PREFIX + "00075";
  static final String METADATA_NAMESPACE_TYPE               = PREFIX + "00076";
  static final String METADATA_NAMESPACE_MANDATORY          = PREFIX + "00077";
  static final String METADATA_NAMESPACE_ONLYONCE           = PREFIX + "00078";
  static final String METADATA_CUSTCLASS_ONLYONCE           = PREFIX + "00079";

  // 00081 - 00090 metadata connection related errors
  static final String METADATA_INSTANCE_CREATE              = PREFIX + "00081";
  static final String METADATA_INSTANCE_CLOSE               = PREFIX + "00082";
  static final String METADATA_SESSION_CREATE               = PREFIX + "00083";
  static final String METADATA_SESSION_COMMIT               = PREFIX + "00084";

  // 00091 - 00100 metadata sandbox related errors
  static final String METADATA_SANDBOX_CREATE               = PREFIX + "00091";
  static final String METADATA_SANDBOX_ACTIVATE             = PREFIX + "00092";
  static final String METADATA_SANDBOX_NOTACTIVE            = PREFIX + "00093";

  // 00101 - 00110 metadata document related errors
  static final String METADATA_DOCUMENT_ONLYONCE            = PREFIX + "00101";
  static final String METADATA_DOCUMENT_CREATE              = PREFIX + "00102";
  static final String METADATA_DOCUMENT_TRANSFORMATION      = PREFIX + "00103";
  static final String METADATA_DOCUMENT_REFERENCE           = PREFIX + "00104";
  static final String METADATA_DOCUMENT_READONLY            = PREFIX + "00105";
  static final String METADATA_DOCUMENT_UPDATE              = PREFIX + "00106";
  static final String METADATA_DOCUMENT_UPLOAD              = PREFIX + "00107";
  static final String METADATA_DOCUMENT_DOWNLOAD            = PREFIX + "00108";

  // 00111 - 00120 managed bean related errors
  static final String MANAGEDBEAN_OBJECTNAME_MALFORMED      = PREFIX + "00111";
  static final String MANAGEDBEAN_DOMAIN_NOTFOUND           = PREFIX + "00112";
  static final String MANAGEDBEAN_SERVER_NOTFOUND           = PREFIX + "00113";
  static final String MANAGEDBEAN_INSTANCE_NOTFOUND         = PREFIX + "00114";
  static final String MANAGEDBEAN_SIGNATURE_NOTFOUND        = PREFIX + "00115";
  static final String MANAGEDBEAN_ATTRIBUTE_NOTFOUND        = PREFIX + "00116";

  // 00121 - 00130 server security related errors
  static final String SECURITY_REALM_NOTEXISTS              = PREFIX + "00121";
  static final String SECURITY_AUTHENTICATOR_NOTEXISTS      = PREFIX + "00122";

  // 00131 - 00140 operational related errors
  static final String OPERATION_UNSUPPORTED                 = PREFIX + "00131";
  static final String OPERATION_CREATE_FAILED               = PREFIX + "00132";
  static final String OPERATION_DELETE_FAILED               = PREFIX + "00133";
  static final String OPERATION_ENABLE_FAILED               = PREFIX + "00134";
  static final String OPERATION_DISABLE_FAILED              = PREFIX + "00135";
  static final String OPERATION_MODIFY_FAILED               = PREFIX + "00136";
  static final String OPERATION_VALIDATE_FAILED             = PREFIX + "00137";
  static final String OPERATION_ASSIGN_FAILED               = PREFIX + "00138";
  static final String OPERATION_REVOKE_FAILED               = PREFIX + "00139";
  static final String OPERATION_REPORT_FAILED               = PREFIX + "00140";

  // 00141 - 00150 http status related errors
  static final String HTTP_RESPONSE_CODE_400                = PREFIX + "00141";
  static final String HTTP_RESPONSE_CODE_401                = PREFIX + "00142";
  static final String HTTP_RESPONSE_CODE_403                = PREFIX + "00143";
  static final String HTTP_RESPONSE_CODE_404                = PREFIX + "00144";
  static final String HTTP_RESPONSE_CODE_408                = PREFIX + "00145";
  static final String HTTP_RESPONSE_CODE_500                = PREFIX + "00146";
  static final String HTTP_RESPONSE_CODE_501                = PREFIX + "00147";
  static final String HTTP_RESPONSE_CODE_502                = PREFIX + "00148";
  static final String HTTP_RESPONSE_CODE_503                = PREFIX + "00149";
  static final String HTTP_RESPONSE_CODE_504                = PREFIX + "00150";

  // 00151 - 00160 http operation related errors
  static final String HTTP_CONNECTION_MALFORMED_URI         = PREFIX + "00151";
  static final String HTTP_CONNECTION_MALFORMED_URL         = PREFIX + "00152";
  static final String HTTP_CONNECTION_PROTOCOL              = PREFIX + "00153";
  static final String HTTP_CONNECTION_ENCODING              = PREFIX + "00154";
  static final String HTTP_CONNECTION_UNKNOWN_HOST          = PREFIX + "00155";
  static final String HTTP_CONNECTION_UNAVAILABLE           = PREFIX + "00156";
  static final String HTTP_CONNECTION_REQUEST_TIMEOUT       = PREFIX + "00157";
  static final String HTTP_CONNECTION_SOCKET_TIMEOUT        = PREFIX + "00158";
  static final String HTTP_CONNECTION_RESPONSE_CONVERT      = PREFIX + "00159";

  // 00161 - 00180 webservice related errors
  static final String WEBSERVICE_PROVIDER_NOTFOUND          = PREFIX + "00161";
  static final String WEBSERVICE_PROVIDERPORT_NOTFOUND      = PREFIX + "00162";
  static final String WEBSERVICE_APPLICATION_NOTFOUND       = PREFIX + "00163";
  static final String WEBSERVICE_APPLICATION_AMBIGUOS       = PREFIX + "00164";
  static final String WEBSERVICE_APPLICATION_VERSION        = PREFIX + "00165";
  static final String WEBSERVICE_WEBSERVICE_EMPTY           = PREFIX + "00166";
  static final String WEBSERVICE_WEBSERVICE_NOTFOUND        = PREFIX + "00167";
  static final String WEBSERVICE_WEBSERVICE_AMBIGUOS        = PREFIX + "00168";
  static final String WEBSERVICE_COMPOSITE_NOTFOUND         = PREFIX + "00169";
  static final String WEBSERVICE_MODULE_TYPE_UNKNOWN        = PREFIX + "00170";
  static final String WEBSERVICE_WEBSERVICE_PORT            = PREFIX + "00171";
  static final String WEBSERVICE_POLICY_MANAGER             = PREFIX + "00172";
  static final String WEBSERVICE_POLICY_NOTFOUND            = PREFIX + "00173";
  static final String WEBSERVICE_POLICY_AMBIGUOS            = PREFIX + "00174";
  static final String WEBSERVICE_POLICY_INVALID             = PREFIX + "00175";
  static final String WEBSERVICE_POLICY_EMPTY               = PREFIX + "00176";
  static final String WEBSERVICE_POLICY_INCOMPATIBLE        = PREFIX + "00177";
  static final String WEBSERVICE_POLICY_SERVER_CLIENT       = PREFIX + "00178";
  static final String WEBSERVICE_POLICY_CLIENT_SERVER       = PREFIX + "00179";

  // 00181 - 00200 LDAP connectivity errors
  static final String LDAP_CONNECTION_UNKNOWN_HOST          = PREFIX + "00181";
  static final String LDAP_CONNECTION_CREATE_SOCKET         = PREFIX + "00182";
  static final String LDAP_CONNECTION_ERROR                 = PREFIX + "00183";
  static final String LDAP_CONNECTION_TIMEOUT               = PREFIX + "00184";
  static final String LDAP_CONNECTION_NOT_AVAILABLE         = PREFIX + "00185";
  static final String LDAP_CONNECTION_SSL_HANDSHAKE         = PREFIX + "00186";
  static final String LDAP_CONNECTION_SSL_ERROR             = PREFIX + "00187";
  static final String LDAP_CONNECTION_SSL_DESELECTED        = PREFIX + "00188";
  static final String LDAP_CONNECTION_AUTHENTICATION        = PREFIX + "00189";
  static final String LDAP_CONNECTION_ENCODING_NOTSUPPORTED = PREFIX + "00190";
  static final String LDAP_CONNECTION_UNWILLING_TO_PERFORM  = PREFIX + "00191";

  // 00201 - 00210 LDAP control errors
  static final String LDAP_CONTROL_EXTENSION_EXISTS         = PREFIX + "00201";
  static final String LDAP_CONTROL_EXTENSION_NOT_EXISTS     = PREFIX + "00202";
  static final String LDAP_CONTROL_EXTENSION_SUPPORTED      = PREFIX + "00203";
  static final String LDAP_CONTROL_EXTENSION_NOT_SUPPORTED  = PREFIX + "00204";

  // 00211 - 00220 LDAP certificate errors
  static final String LDAP_CERTIFICATE_TYPE_NOT_AVAILABLE   = PREFIX + "00211";
  static final String LDAP_CERTIFICATE_FILE_NOT_FOUND       = PREFIX + "00212";

  // 00221 - 00230 opss errors
  static final String OPSS_CONTEXT_ERROR                    = PREFIX + "00221";
  static final String OPSS_CREDENTIALSTORE_ERROR            = PREFIX + "00222";
  static final String OPSS_CREDENTIALSTORE_MISSING          = PREFIX + "00223";
  static final String OPSS_CREDENTIALMAP_MISSING            = PREFIX + "00224";
  static final String OPSS_CREDENTIALALIAS_MISSING          = PREFIX + "00225";

  // 00231 - 00240 filter errors
  static final String FILTER_PROPERTY_FILE                  = PREFIX + "00231";
  static final String FILTER_STYLESHEET_FILE                = PREFIX + "00232";
}