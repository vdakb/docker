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
    Subsystem   :   Foundation Shared Library

    File        :   SystemError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SystemError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation;

////////////////////////////////////////////////////////////////////////////////
// interface SystemError
// ~~~~~~~~~ ~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SystemError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 00001 - 00010 system related errors
  static final String UNHANDLED                       = SystemConstant.PREFIX + "00001";
  static final String GENERAL                         = SystemConstant.PREFIX + "00002";
  static final String ABORT                           = SystemConstant.PREFIX + "00003";
  static final String NOTIMPLEMENTED                  = SystemConstant.PREFIX + "00004";
  static final String CLASSNOTFOUND                   = SystemConstant.PREFIX + "00005";
  static final String CLASSNOTCREATE                  = SystemConstant.PREFIX + "00006";
  static final String CLASSNOACCESS                   = SystemConstant.PREFIX + "00007";
  static final String CLASSINVALID                    = SystemConstant.PREFIX + "00008";
  static final String CLASSMETHOD                     = SystemConstant.PREFIX + "00009";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL                = SystemConstant.PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE               = SystemConstant.PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE              = SystemConstant.PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH          = SystemConstant.PREFIX + "00014";

  // 00021 - 00030 instance state related errors
  static final String ATTRIBUTE_IS_NULL               = SystemConstant.PREFIX + "00021";
  static final String INSTANCE_STATE                  = SystemConstant.PREFIX + "00022";

  // 00031 - 00040 schema entry and attribute related errors
  static final String SCHEMA_ATTRIBUTE_IS_NULL        = SystemConstant.PREFIX + "00031";
  static final String SCHEMA_ATTRIBUTE_IS_BLANK       = SystemConstant.PREFIX + "00032";
  static final String SCHEMA_ATTRIBUTE_SINGLE         = SystemConstant.PREFIX + "00033";
  static final String SCHEMA_ATTRIBUTE_TYPE           = SystemConstant.PREFIX + "00034";
  static final String SCHEMA_NAME_ENTRY_MISSING       = SystemConstant.PREFIX + "00035";

  // 00041 - 00050 filter related errors
  static final String FILTER_INVALID                  = SystemConstant.PREFIX + "00041";
  static final String FILTER_ATTRIBUTE_COMPARABLE     = SystemConstant.PREFIX + "00042";
  static final String FILTER_INCONSISTENT_METHOD      = SystemConstant.PREFIX + "00043";
  static final String FILTER_INCONSISTENT_EXPRESSION  = SystemConstant.PREFIX + "00044";

  // 00051 - 00060 security context related errors
  static final String SECURITY_INITIALIZE             = SystemConstant.PREFIX + "00051";
  static final String SECURITY_UNRECOVERABLE          = SystemConstant.PREFIX + "00052";
  static final String SECURITY_PROVIDER               = SystemConstant.PREFIX + "00053";
  static final String SECURITY_ALGORITHM              = SystemConstant.PREFIX + "00054";
  static final String SECURITY_KEYSTORE_PASSWORD      = SystemConstant.PREFIX + "00055";

  // 00061 - 00070 trusted key store related errors
  static final String TRUSTED_IMPLEMENTATION          = SystemConstant.PREFIX + "00061";
  static final String TRUSTED_PROVIDER                = SystemConstant.PREFIX + "00062";
  static final String TRUSTED_ALGORITHM               = SystemConstant.PREFIX + "00063";
  static final String TRUSTED_FILE_NOTFOUND           = SystemConstant.PREFIX + "00064";
  static final String TRUSTED_FILE_NOTLOADED          = SystemConstant.PREFIX + "00065";
  static final String TRUSTED_CERT_NOTLOADED          = SystemConstant.PREFIX + "00066";

  // 00071 - 00080 identity key store related errors
  static final String IDENTITY_IMPLEMENTATION         = SystemConstant.PREFIX + "00071";
  static final String IDENTITY_PROVIDER               = SystemConstant.PREFIX + "00072";
  static final String IDENTITY_ALGORITHM              = SystemConstant.PREFIX + "00073";
  static final String IDENTITY_FILE_NOTFOUND          = SystemConstant.PREFIX + "00074";
  static final String IDENTITY_FILE_NOTLOADED         = SystemConstant.PREFIX + "00075";
  static final String IDENTITY_CERT_NOTLOADED         = SystemConstant.PREFIX + "00076";

  // 00081 - 00090 configuration state errors
  static final String PROPERTY_REQUIRED               = SystemConstant.PREFIX + "00081";

  // 00091 - 00100 connectivity errors
  static final String CONNECTION_UNKNOWN_HOST         = SystemConstant.PREFIX + "00091";
  static final String CONNECTION_CREATE_SOCKET        = SystemConstant.PREFIX + "00092";
  static final String CONNECTION_SECURE_SOCKET        = SystemConstant.PREFIX + "00093";
  static final String CONNECTION_CERTIFICATE_PATH     = SystemConstant.PREFIX + "00094";
  static final String CONNECTION_ERROR                = SystemConstant.PREFIX + "00095";
  static final String CONNECTION_TIMEOUT              = SystemConstant.PREFIX + "00096";
  static final String CONNECTION_UNAVAILABLE          = SystemConstant.PREFIX + "00097";
  static final String CONNECTION_AUTHENTICATION       = SystemConstant.PREFIX + "00098";
  static final String CONNECTION_AUTHORIZATION        = SystemConstant.PREFIX + "00099";
  static final String CONNECTION_ENCODING_UNSUPPORTED = SystemConstant.PREFIX + "00100";

  // 00101 - 00110 operational state errors
  static final String OBJECT_CLASS_UNSUPPORTED        = SystemConstant.PREFIX + "00101";
  static final String OBJECT_CLASS_REQUIRED           = SystemConstant.PREFIX + "00102";
  static final String OBJECT_VALUES_REQUIRED          = SystemConstant.PREFIX + "00103";
  static final String UNIQUE_IDENTIFIER_REQUIRED      = SystemConstant.PREFIX + "00104";
  static final String NAME_IDENTIFIER_REQUIRED        = SystemConstant.PREFIX + "00105";
}