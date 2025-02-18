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
    Subsystem   :   Common Shared Utility

    File        :   SystemError.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SystemError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core;


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

  /** the default message prefix. */
  static final String PREFIX                          = "OSF-";

  // 00001 - 00010 system related errors
  static final String UNHANDLED                       = PREFIX + "00001";
  static final String GENERAL                         = PREFIX + "00002";
  static final String ABORT                           = PREFIX + "00003";
  static final String NOTIMPLEMENTED                  = PREFIX + "00004";
  static final String CLASSNOTFOUND                   = PREFIX + "00005";
  static final String CLASSNOTCREATE                  = PREFIX + "00006";
  static final String CLASSNOACCESS                   = PREFIX + "00007";
  static final String CLASSINVALID                    = PREFIX + "00008";
  static final String CLASSMETHOD                     = PREFIX + "00009";
  static final String CLASSCONSTRUCTORDEFAULT         = PREFIX + "00010";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL                = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE               = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE              = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH          = PREFIX + "00014";

  // 00021 - 00030 instance state related errors
  static final String ATTRIBUTE_IS_NULL               = PREFIX + "00021";
  static final String INSTANCE_STATE                  = PREFIX + "00022";

  // 00031 - 00040 system property errors
  static final String PROPERTY_TYPE_INVALID           = PREFIX + "00031";
  static final String PROPERTY_NAME_REQUIRED          = PREFIX + "00032";
  static final String PROPERTY_NAME_EXISTS            = PREFIX + "00033";
  static final String PROPERTY_DEFAULT_REQUIRED       = PREFIX + "00034";
  static final String PROPERTY_MINIMUM_VALUE          = PREFIX + "00035";
  static final String PROPERTY_MINIMUM_DEFAULT        = PREFIX + "00036";
  static final String PROPERTY_MINIMUM_COMPARABLE     = PREFIX + "00037";
  static final String PROPERTY_MAXIMUM_VALUE          = PREFIX + "00038";
  static final String PROPERTY_MAXIMUM_DEFAULT        = PREFIX + "00039";
  static final String PROPERTY_MAXIMUM_COMPARABLE     = PREFIX + "00040";

  // 00041 - 00050 reflection/inrospection errors
  static final String INTROSPECT_EXCEPTION_IGNORE     = PREFIX + "00041";

  // 00051 - 00060 security context related errors
  static final String SECURITY_INITIALIZE             = PREFIX + "00051";
  static final String SECURITY_UNRECOVERABLE          = PREFIX + "00052";
  static final String SECURITY_PROVIDER               = PREFIX + "00053";
  static final String SECURITY_ALGORITHM              = PREFIX + "00054";
  static final String SECURITY_KEYSTORE_PASSWORD      = PREFIX + "00055";

  // 00061 - 00070 trusted key store related errors
  static final String TRUSTED_IMPLEMENTATION          = PREFIX + "00061";
  static final String TRUSTED_PROVIDER                = PREFIX + "00062";
  static final String TRUSTED_ALGORITHM               = PREFIX + "00063";
  static final String TRUSTED_FILE_NOTFOUND           = PREFIX + "00064";
  static final String TRUSTED_FILE_NOTLOADED          = PREFIX + "00065";
  static final String TRUSTED_CERT_NOTLOADED          = PREFIX + "00066";

  // 00071 - 00080 identity key store related errors
  static final String IDENTITY_IMPLEMENTATION         = PREFIX + "00071";
  static final String IDENTITY_PROVIDER               = PREFIX + "00072";
  static final String IDENTITY_ALGORITHM              = PREFIX + "00073";
  static final String IDENTITY_FILE_NOTFOUND          = PREFIX + "00074";
  static final String IDENTITY_FILE_NOTLOADED         = PREFIX + "00075";
  static final String IDENTITY_CERT_NOTLOADED         = PREFIX + "00076";

  // 00081 - 00090 configuration state errors
  static final String PROPERTY_REQUIRED               = PREFIX + "00081";

  // 00091 - 00100 connectivity errors
  static final String CONNECTION_UNKNOWN_HOST         = PREFIX + "00091";
  static final String CONNECTION_CREATE_SOCKET        = PREFIX + "00092";
  static final String CONNECTION_SECURE_SOCKET        = PREFIX + "00093";
  static final String CONNECTION_CERTIFICATE_PATH     = PREFIX + "00094";
  static final String CONNECTION_ERROR                = PREFIX + "00095";
  static final String CONNECTION_TIMEOUT              = PREFIX + "00096";
  static final String CONNECTION_UNAVAILABLE          = PREFIX + "00097";
  static final String CONNECTION_AUTHENTICATION       = PREFIX + "00098";
  static final String CONNECTION_AUTHORIZATION        = PREFIX + "00099";
  static final String CONNECTION_ENCODING_UNSUPPORTED = PREFIX + "00100";

  // 00101 - 00120 marshalling errors
  static final String PATH_UNEXPECTED_EOF_STRING      = PREFIX + "00101";
  static final String PATH_UNEXPECTED_EOF_FILTER      = PREFIX + "00102";
  static final String PATH_UNEXPECTED_CHARACTER       = PREFIX + "00103";
  static final String PATH_UNRECOGNOIZED_OPERATOR     = PREFIX + "00104";
  static final String PATH_INVALID_FILTER             = PREFIX + "00105";
  static final String PATH_EXPECT_PARENTHESIS         = PREFIX + "00106";
  static final String PATH_INVALID_PARENTHESIS        = PREFIX + "00107";
  static final String PATH_EXPECT_ATTRIBUTE_PATH      = PREFIX + "00108";
  static final String PATH_EXPECT_ATTRIBUTE_NAME      = PREFIX + "00109";
  static final String PATH_INVALID_ATTRIBUTE_PATH     = PREFIX + "00110";
  static final String PATH_INVALID_ATTRIBUTE_NAME     = PREFIX + "00111";
  static final String PATH_INVALID_URN                = PREFIX + "00112";
}