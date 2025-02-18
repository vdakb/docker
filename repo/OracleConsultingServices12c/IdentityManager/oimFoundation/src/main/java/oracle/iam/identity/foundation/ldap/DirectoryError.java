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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DirectoryError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

////////////////////////////////////////////////////////////////////////////////
// interface DirectoryError
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public interface DirectoryError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                           = "GDS-";

  // 00001 - 00010 system related errors
  static final String UNHANDLED                        = PREFIX + "00001";
  static final String GENERAL                          = PREFIX + "00002";
  static final String ABORT                            = PREFIX + "00003";
  static final String NOTIMPLEMENTED                   = PREFIX + "00004";

  // 00011 - 00012 method argument related errors
  static final String ARGUMENT_IS_NULL                 = PREFIX + "00011";

  // 00021 - 00030 instance state related errors
  static final String INSTANCE_ATTRIBUTE_IS_NULL       = PREFIX + "00021";

  // 00031 - 00040 file related errors
  static final String FILE_MISSING                     = PREFIX + "00031";
  static final String FILE_IS_NOT_A_FILE               = PREFIX + "00032";
  static final String FILE_OPEN                        = PREFIX + "00033";
  static final String FILE_CLOSE                       = PREFIX + "00034";
  static final String FILE_READ                        = PREFIX + "00035";
  static final String FILE_WRITE                       = PREFIX + "00036";

  // 00061 - 00070 connectivity errors
  static final String CONNECTION_UNKNOWN_HOST          = PREFIX + "00061";
  static final String CONNECTION_CREATE_SOCKET         = PREFIX + "00062";
  static final String CONNECTION_ERROR                 = PREFIX + "00063";
  static final String CONNECTION_TIMEOUT               = PREFIX + "00064";
  static final String CONNECTION_NOT_AVAILABLE         = PREFIX + "00065";
  static final String CONNECTION_SSL_HANDSHAKE         = PREFIX + "00066";
  static final String CONNECTION_SSL_ERROR             = PREFIX + "00067";
  static final String CONNECTION_SSL_DESELECTED        = PREFIX + "00068";
  static final String CONNECTION_AUTHENTICATION        = PREFIX + "00069";
  static final String CONNECTION_ENCODING_NOTSUPPORTED = PREFIX + "00070";
  static final String CONNECTION_UNWILLING_TO_PERFORM  = PREFIX + "00071";

  // 00071 - 00090 certificate related errors
  static final String CERTIFICATE_FILE_NOT_FOUND       = PREFIX + "00072";
  static final String CERTIFICATE_TYPE_NOT_AVAILABLE   = PREFIX + "00073";

  // 00081 - 00090 control extension support related errors
  static final String CONTROL_EXTENSION_EXISTS         = PREFIX + "00081";
  static final String CONTROL_EXTENSION_NOT_EXISTS     = PREFIX + "00082";
  static final String CONTROL_EXTENSION_SUPPORTED      = PREFIX + "00084";
  static final String CONTROL_EXTENSION_NOT_SUPPORTED  = PREFIX + "00085";

  // 00091 - 00130 operational errors
  static final String ENCODING_TYPE_NOT_SUPPORTED      = PREFIX + "00091";
  static final String OBJECT_NOT_CREATED               = PREFIX + "00092";
  static final String OBJECT_NOT_DELETED               = PREFIX + "00093";
  static final String OBJECT_ALREADY_EXISTS            = PREFIX + "00094";
  static final String OBJECT_NOT_EXISTS                = PREFIX + "00095";
  static final String OBJECT_NOT_ASSIGNED              = PREFIX + "00096";
  static final String OBJECT_NOT_REMOVED               = PREFIX + "00097";
  static final String OBJECT_ALREADY_ASSIGNED          = PREFIX + "00098";
  static final String OBJECT_ALREADY_REMOVED           = PREFIX + "00099";
  static final String OBJECT_AMBIGUOUS                 = PREFIX + "00100";
  static final String OBJECT_NOT_ENABLED               = PREFIX + "00101";
  static final String OBJECT_NOT_DISABLED              = PREFIX + "00102";
  static final String OBJECT_NOT_UPDATED               = PREFIX + "00103";
  static final String OBJECT_NOT_RENAMED               = PREFIX + "00104";
  static final String ATTRIBUTE_SCHEMA_VIOLATED        = PREFIX + "00105";
  static final String ATTRIBUTE_INVALID_DATA           = PREFIX + "00106";
  static final String ATTRIBUTE_INVALID_TYPE           = PREFIX + "00107";
  static final String ATTRIBUTE_INVALID_SIZE           = PREFIX + "00108";
  static final String ATTRIBUTE_UNRESOLVED             = PREFIX + "00109";
  static final String ATTRIBUTE_IN_USE                 = PREFIX + "00110";
  static final String INSUFFICIENT_INFORMATION         = PREFIX + "00111";
  static final String OPERATION_NOT_SUPPORTED          = PREFIX + "00112";
  static final String CONTEXT_NOT_EMPTY                = PREFIX + "00113";
  static final String PASSWORD_CHANGE_REQUIRES_SSL     = PREFIX + "00114";
  static final String ATTRIBUTE_NOT_ASSIGNED           = PREFIX + "00115";
  static final String ATTRIBUTE_ALREADY_ASSIGNED       = PREFIX + "00116";
  static final String ATTRIBUTE_ALREADY_REMOVED        = PREFIX + "00117";
  static final String OBJECT_ALREADY_ENABLED           = PREFIX + "00118";
  static final String OBJECT_ALREADY_DISABLED          = PREFIX + "00119";
  static final String OBJECT_ALREADY_UNLOCKED          = PREFIX + "00120";
  static final String OBJECT_ALREADY_LOCKED            = PREFIX + "00121";
  static final String HIERARCHY_PATH_NOT_RESOLVED      = PREFIX + "00122";
  static final String ROLE_PATH_NOT_RESOLVED           = PREFIX + "00123";
  static final String GROUP_PATH_NOT_RESOLVED          = PREFIX + "00124";
  static final String HIERARCHY_PATH_NOT_EXISTS        = PREFIX + "00125";

  // 00141 - 00160 file parsing related errors
  static final String LINE                             = PREFIX + "00141";
  static final String UNEXPECTED                       = PREFIX + "00142";
  static final String MISSING_SEPARATOR                = PREFIX + "00143";
  static final String INVALID_SEPARATOR                = PREFIX + "00144";
  static final String EXPECTING_SEPARATOR              = PREFIX + "00145";
  static final String EXPECTING_PREFIX                 = PREFIX + "00146";
  static final String LINE_PARSER_NOWHERE              = PREFIX + "00147";
  static final String CONSTRUCT_STRING                 = PREFIX + "00148";
  static final String CONSTRUCT_URL                    = PREFIX + "00149";
  static final String CHANGE_TYPE_UNKNOW               = PREFIX + "00150";
  static final String CHANGE_OPERATION_UNKNOW          = PREFIX + "00151";
  static final String CHANGE_TYPE_NOTSUPPORTED         = PREFIX + "00152";
}