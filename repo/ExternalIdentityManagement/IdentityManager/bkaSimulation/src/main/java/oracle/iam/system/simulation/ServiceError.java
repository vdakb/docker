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

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   ServiceError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation;

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
  static final String PREFIX                               = "SVC-";

  // 00001 - 00010 system related errors
  static final String UNHANDLED                            = PREFIX + "00001";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL                     = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE                    = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE                   = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH               = PREFIX + "00014";

  // 00021 - 00030 instance state related errors
  static final String INSTANCE_ATTRIBUTE_IS_NULL           = PREFIX + "00021";
  static final String INSTANCE_ILLEGAL_STATE               = PREFIX + "00022";

  // 00031 - 00040 connectivity errors
  static final String CONNECTION_UNKNOWN_HOST              = PREFIX + "00031";
  static final String CONNECTION_CREATE_SOCKET             = PREFIX + "00032";
  static final String CONNECTION_SECURE_SOCKET             = PREFIX + "00033";
  static final String CONNECTION_CERTIFICATE_HOST          = PREFIX + "00034";
  static final String CONNECTION_ERROR                     = PREFIX + "00035";
  static final String CONNECTION_TIMEOUT                   = PREFIX + "00036";
  static final String CONNECTION_NOT_AVAILABLE             = PREFIX + "00037";
  static final String CONNECTION_AUTHENTICATION            = PREFIX + "00038";
  static final String CONNECTION_AUTHORIZATION             = PREFIX + "00039";

  // 00041 - 00050 operational state errors
  static final String UNSUPPORTED                          = PREFIX + "00041";
  static final String OBJECT_CLASS_MISSING                 = PREFIX + "00042";
  static final String UNIQUE_IDENTIFIER_MISSING            = PREFIX + "00043";
  static final String OBJECT_NOT_CREATED                   = PREFIX + "00044";
  static final String OBJECT_NOT_DELETED                   = PREFIX + "00045";
  static final String OBJECT_ALREADY_EXISTS                = PREFIX + "00046";
  static final String OBJECT_NOT_EXISTS                    = PREFIX + "00047";
  static final String ATTRIBUTE_EMPTY                      = PREFIX + "00048";
  static final String ATTRIBUTE_NAME_EMPTY                 = PREFIX + "00049";

  // 00051 - 00070 marshalling errors
  static final String PATH_UNEXPECTED_EOF_STRING           = PREFIX + "00051";
  static final String PATH_UNEXPECTED_EOF_FILTER           = PREFIX + "00052";
  static final String PATH_UNEXPECTED_CHARACTER            = PREFIX + "00053";
  static final String PATH_UNEXPECTED_TOKEN                = PREFIX + "00054";
  static final String PATH_UNRECOGNOIZED_OPERATOR          = PREFIX + "00055";
  static final String PATH_INVALID_FILTER                  = PREFIX + "00056";
  static final String PATH_EXPECT_PARENTHESIS              = PREFIX + "00057";
  static final String PATH_INVALID_PARENTHESIS             = PREFIX + "00058";
  static final String PATH_EXPECT_ATTRIBUTE_PATH           = PREFIX + "00059";
  static final String PATH_EXPECT_ATTRIBUTE_NAME           = PREFIX + "00060";
  static final String PATH_INVALID_ATTRIBUTE_PATH          = PREFIX + "00061";
  static final String PATH_INVALID_ATTRIBUTE_NAME          = PREFIX + "00062";
  static final String PATH_INVALID_COMPARISON_VALUE        = PREFIX + "00063";
  static final String PATH_INVALID_VALUE_DEPTH             = PREFIX + "00064";
  static final String PATH_INVALID_VALUE_FILTER            = PREFIX + "00065";
  static final String PATH_INVALID_VALUE_ENCODING          = PREFIX + "00066";

  // 00071 - 00080 request method errors
  static final String REQUEST_METHOD_NOTALLOWED            = PREFIX + "00071";
  static final String REQUEST_METHOD_NOTIMPLEMENTED        = PREFIX + "00072";
  static final String REQUEST_METHOD_ENTITY_ID_REQUIRED    = PREFIX + "00073";
  static final String REQUEST_METHOD_ENTITY_ID_INVALID     = PREFIX + "00074";
  static final String REQUEST_METHOD_ENTITY_NOTFOUND       = PREFIX + "00075";
  static final String REQUEST_METHOD_ENTITY_EXISTS         = PREFIX + "00076";
  static final String REQUEST_METHOD_ENTITY_EMPTY          = PREFIX + "00077";
  static final String REQUEST_METHOD_ENTITY_ATTRIBUTE      = PREFIX + "00078";

  // 00081 - 00090 request parameter errors
  static final String PARAMETER_FILTER_NOTPERMITTED        = PREFIX + "00081";
  static final String PARAMETER_SORT_INVALID_VALUE         = PREFIX + "00082";

  // 00091 - 00100 filtering errors
  static final String FILTER_INCONSISTENT_METHOD           = PREFIX + "00091";
  static final String FILTER_INCONSISTENT_EXPRESSION       = PREFIX + "00092";

  // 00101 - 00110 patch operation errors
  static final String PATCH_OPERATIONTYPE_UNKNOWN          = PREFIX + "00101";
  static final String PATCH_MULTIVALUE_NOTPERMITTED        = PREFIX + "00102";

  // 00111 - 00130 http status related errors
  static final String HTTP_RESPONSE_CODE_NOP               = PREFIX + "00111";
  static final String HTTP_RESPONSE_CODE_400               = PREFIX + "00112";
  static final String HTTP_RESPONSE_CODE_401               = PREFIX + "00113";
  static final String HTTP_RESPONSE_CODE_403               = PREFIX + "00114";
  static final String HTTP_RESPONSE_CODE_404               = PREFIX + "00115";
  static final String HTTP_RESPONSE_CODE_408               = PREFIX + "00116";
  static final String HTTP_RESPONSE_CODE_500               = PREFIX + "00117";
  static final String HTTP_RESPONSE_CODE_501               = PREFIX + "00118";
  static final String HTTP_RESPONSE_CODE_502               = PREFIX + "00119";
  static final String HTTP_RESPONSE_CODE_503               = PREFIX + "00120";
  static final String HTTP_RESPONSE_CODE_504               = PREFIX + "00121";
}