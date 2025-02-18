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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   ServiceError.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest;

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
public interface ServiceError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                            = "GWS-";

  // 00001 - 00010 system related errors
  static final String UNHANDLED                         = PREFIX + "00001";

  // 00011 - 00015 method argument related errors
  static final String ARGUMENT_IS_NULL                  = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE                 = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE                = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH            = PREFIX + "00014";

  // 00021 - 00030 instance state related errors
  static final String INSTANCE_ATTRIBUTE_IS_NULL        = PREFIX + "00021";
  static final String INSTANCE_ILLEGAL_STATE            = PREFIX + "00022";

  // 00031 - 00040 connectivity errors
  static final String CONNECTION_UNKNOWN_HOST           = PREFIX + "00031";
  static final String CONNECTION_CREATE_SOCKET          = PREFIX + "00032";
  static final String CONNECTION_SECURE_SOCKET          = PREFIX + "00033";
  static final String CONNECTION_CERTIFICATE_HOST       = PREFIX + "00034";
  static final String CONNECTION_ERROR                  = PREFIX + "00035";
  static final String CONNECTION_TIMEOUT                = PREFIX + "00036";
  static final String CONNECTION_NOT_AVAILABLE          = PREFIX + "00037";
  static final String CONNECTION_AUTHENTICATION         = PREFIX + "00038";
  static final String CONNECTION_AUTHORIZATION          = PREFIX + "00039";

  // 00041 - 00050 request method errors
  static final String REQUEST_METHOD_NOTALLOWED         = PREFIX + "00041";
  static final String REQUEST_METHOD_NOTIMPLEMENTED     = PREFIX + "00042";
  static final String REQUEST_METHOD_ENTITY_ID_REQUIRED = PREFIX + "00043";
  static final String REQUEST_METHOD_ENTITY_ID_INVALID  = PREFIX + "00044";
  static final String REQUEST_METHOD_ENTITY_NOTFOUND    = PREFIX + "00045";
  static final String REQUEST_METHOD_ENTITY_EXISTS      = PREFIX + "00046";
  static final String REQUEST_METHOD_ENTITY_EMPTY       = PREFIX + "00047";
  static final String REQUEST_METHOD_ENTITY_ATTRIBUTE   = PREFIX + "00048";

  // 00051 - 00070 marshalling errors
  static final String PATH_UNEXPECTED_EOF_STRING        = PREFIX + "00051";
  static final String PATH_UNEXPECTED_EOF_FILTER        = PREFIX + "00052";
  static final String PATH_UNEXPECTED_CHARACTER         = PREFIX + "00053";
  static final String PATH_UNEXPECTED_TOKEN             = PREFIX + "00054";
  static final String PATH_UNRECOGNOIZED_OPERATOR       = PREFIX + "00055";
  static final String PATH_INVALID_FILTER               = PREFIX + "00056";
  static final String PATH_EXPECT_PARENTHESIS           = PREFIX + "00057";
  static final String PATH_INVALID_PARENTHESIS          = PREFIX + "00058";
  static final String PATH_EXPECT_ATTRIBUTE_PATH        = PREFIX + "00059";
  static final String PATH_EXPECT_ATTRIBUTE_NAME        = PREFIX + "00060";
  static final String PATH_INVALID_ATTRIBUTE_PATH       = PREFIX + "00061";
  static final String PATH_INVALID_ATTRIBUTE_NAME       = PREFIX + "00062";
  static final String PATH_INVALID_COMPARISON_VALUE     = PREFIX + "00063";
  static final String PATH_INVALID_VALUE_DEPTH          = PREFIX + "00064";
  static final String PATH_INVALID_VALUE_FILTER         = PREFIX + "00065";
  static final String PATH_INVALID_VALUE_ENCODING       = PREFIX + "00066";

  // 00071 - 00080 request parameter errors
  static final String PARAMETER_FILTER_NOTPERMITTED     = PREFIX + "00071";
  static final String PARAMETER_START_INVALID_VALUE     = PREFIX + "00072";
  static final String PARAMETER_COUNT_INVALID_VALUE     = PREFIX + "00073";
  static final String PARAMETER_SORT_INVALID_VALUE      = PREFIX + "00074";
  static final String PARAMETER_ORDER_INVALID_VALUE     = PREFIX + "00075";

  // 00081 - 00090 filtering errors
  static final String FILTER_INCONSISTENT_METHOD        = PREFIX + "00081";
  static final String FILTER_INCONSISTENT_EXPRESSION    = PREFIX + "00082";
  static final String FILTER_INVALID_VALUE_TYPE_LT      = PREFIX + "00083";
  static final String FILTER_INVALID_VALUE_TYPE_LE      = PREFIX + "00084";
  static final String FILTER_INVALID_VALUE_TYPE_GT      = PREFIX + "00085";
  static final String FILTER_INVALID_VALUE_TYPE_GE      = PREFIX + "00086";
  static final String FILTER_INVALID_TARGET_TYPE        = PREFIX + "00087";
  static final String FILTER_INVALID_TARGET_MATCH       = PREFIX + "00088";

  // 00091 - 00120 processing errors
  static final String PROCESS_UNEXPECTED                = PREFIX + "00091";
  static final String PROCESS_UNAVAILABLE               = PREFIX + "00092";
  static final String PROCESS_AUTHORIZATION             = PREFIX + "00093";
  static final String PROCESS_FORBIDDEN                 = PREFIX + "00094";
  static final String PROCESS_NOT_ALLOWED               = PREFIX + "00095";
  static final String PROCESS_NOT_ACCEPTABLE            = PREFIX + "00096";
  static final String PROCESS_MEDIATYPE_UNSUPPORTED     = PREFIX + "00097";
  static final String PROCESS_EXISTS                    = PREFIX + "00098";
  static final String PROCESS_EXISTS_NOT                = PREFIX + "00099";
  static final String PROCESS_PRECONDITION              = PREFIX + "00100";
  static final String PROCESS_POSTCONDITION             = PREFIX + "00101";
  static final String PROCESS_TOO_LARGE                 = PREFIX + "00102";
  static final String PROCESS_INVALID_VERSION           = PREFIX + "00103";
  static final String PROCESS_TOO_MANY                  = PREFIX + "00104";
  static final String PROCESS_MUTABILITY                = PREFIX + "00105";
  static final String PROCESS_SENSITIVE                 = PREFIX + "00106";
  static final String PROCESS_UNIQUENESS                = PREFIX + "00107";
  static final String PROCESS_NOTARGET                  = PREFIX + "00108";
  static final String PROCESS_INVALID_FILTER            = PREFIX + "00109";
  static final String PROCESS_INVALID_SYNTAX            = PREFIX + "00110";
  static final String PROCESS_INVALID_PATH              = PREFIX + "00111";
  static final String PROCESS_INVALID_VALUE             = PREFIX + "00112";

  // 00121 - 00130 patch operation errors
  static final String PATCH_OPERATIONTYPE_UNKNOWN       = PREFIX + "00121";
  static final String PATCH_MULTIVALUE_NOTPERMITTED     = PREFIX + "00122";
  static final String PATCH_OPERATION_VALUE_NOTNULL     = PREFIX + "00123";
  static final String PATCH_OPERATION_ADD_OBJECT        = PREFIX + "00124";
  static final String PATCH_OPERATION_ADD_ARRAY         = PREFIX + "00125";
  static final String PATCH_OPERATION_ADD_PATH          = PREFIX + "00126";
  static final String PATCH_OPERATION_REPLACE_VALUE     = PREFIX + "00127";
  static final String PATCH_OPERATION_REMOVE_PATH       = PREFIX + "00128";

  // 00131 - 00140 resource operational state errors
  static final String RESOURCE_EXISTS                   = PREFIX + "00131";
  static final String RESOURCE_NOT_EXISTS               = PREFIX + "00132";
  static final String RESOURCE_NOT_CREATED              = PREFIX + "00133";
  static final String RESOURCE_NOT_MODIFIED             = PREFIX + "00134";
  static final String RESOURCE_NOT_DELETED              = PREFIX + "00135";
  static final String RESOURCE_MATCH_IDENTIFIER         = PREFIX + "00136";

  // 00141 - 00160 http status related errors
  static final String HTTP_CODE_NOP                     = PREFIX + "00141";
  static final String HTTP_CODE_400                     = PREFIX + "00142";
  static final String HTTP_CODE_401                     = PREFIX + "00143";
  static final String HTTP_CODE_403                     = PREFIX + "00144";
  static final String HTTP_CODE_404                     = PREFIX + "00145";
  static final String HTTP_CODE_408                     = PREFIX + "00146";
  static final String HTTP_CODE_409                     = PREFIX + "00147";
  static final String HTTP_CODE_415                     = PREFIX + "00148";
  static final String HTTP_CODE_500                     = PREFIX + "00149";
  static final String HTTP_CODE_501                     = PREFIX + "00150";
  static final String HTTP_CODE_502                     = PREFIX + "00151";
  static final String HTTP_CODE_503                     = PREFIX + "00152";
  static final String HTTP_CODE_504                     = PREFIX + "00153";

  // 00161 - 00170 authorization errors
  static final String OAUTH_FLOW_NOT_FINISH             = PREFIX + "00161";
  static final String OAUTH_FLOW_WRONG_STATE            = PREFIX + "00162";
  static final String OAUTH_FLOW_ACCESS_TOKEN           = PREFIX + "00163";
  static final String OAUTH_FLOW_REFRESH_TOKEN          = PREFIX + "00164";
}
