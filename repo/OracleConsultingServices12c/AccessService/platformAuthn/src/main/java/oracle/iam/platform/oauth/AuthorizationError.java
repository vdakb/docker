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

    System      :   Oracle Access Manager OAuth Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AuthorizationError.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    AuthorizationError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth;

////////////////////////////////////////////////////////////////////////////////
// interface AssertionError
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface AuthorizationError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                        = "GWS-";

  // 00001 - 00010 operations related errors
  static final String UNHANDLED                     = PREFIX + "00001";
  static final String GENERAL                       = PREFIX + "00002";
  static final String ABORT                         = PREFIX + "00003";
  static final String NOTIMPLEMENTED                = PREFIX + "00004";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL              = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE             = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE            = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH        = PREFIX + "00014";

  // 00031 - 00040 authorization errors
  static final String OAUTH_FLOW_NOT_FINISH         = PREFIX + "00031";
  static final String OAUTH_FLOW_WRONG_STATE        = PREFIX + "00032";
  static final String OAUTH_FLOW_ACCESS_TOKEN       = PREFIX + "00033";
  static final String OAUTH_FLOW_REFRESH_TOKEN      = PREFIX + "00034";

  // 00041 - 00050 processing errors
  static final String PROCESS_UNEXPECTED            = PREFIX + "00041";
  static final String PROCESS_UNAVAILABLE           = PREFIX + "00042";
  static final String PROCESS_AUTHORIZATION         = PREFIX + "00043";
  static final String PROCESS_NOT_ACCEPTABLE        = PREFIX + "00045";
  static final String PROCESS_MEDIATYPE_UNSUPPORTED = PREFIX + "00046";
  static final String PROCESS_EXISTS_NOT            = PREFIX + "00047";
  static final String PROCESS_INVALID_VALUE         = PREFIX + "00048";

  // 00051 - 00060 token syntax errors
  static final String TOKEN_PLAIN_OBJECT            = PREFIX + "00051";
  static final String TOKEN_PLAIN_HEADER            = PREFIX + "00052";
  static final String TOKEN_PLAIN_PAYLOAD           = PREFIX + "00053";
  static final String TOKEN_PLAIN_ENCODE            = PREFIX + "00054";
  static final String TOKEN_SIGNED_HEADER           = PREFIX + "00055";
  static final String TOKEN_SIGNED_PAYLOAD          = PREFIX + "00056";
  static final String TOKEN_SIGNED_SIGNATURE        = PREFIX + "00057";
  static final String TOKEN_SIGNED_ENCODE           = PREFIX + "00058";
  static final String TOKEN_ENCRYPTED_ENCODE        = PREFIX + "00059";

  // 00061 - 00070 token encryption/decryption errors
  static final String CRYPTO_STATE_UNDEFINED        = PREFIX + "00061";
  static final String CRYPTO_STATE_ENCRYPTED        = PREFIX + "00062";
  static final String CRYPTO_STATE_UNENCRYPTED      = PREFIX + "00063";
  static final String CRYPTO_ENCRYPTION_METHOD      = PREFIX + "00064";
  static final String CRYPTO_ALGORITHM_UNSUPPORTED  = PREFIX + "00065";
  static final String CRYPTO_ALGORITHM_NOTACCEPTED  = PREFIX + "00066";
  static final String CRYPTO_DECRYPTION_METHOD      = PREFIX + "00067";
  static final String CRYPTO_DECRYPTION_ALGORITHM   = PREFIX + "00068";

  // 00071 - 00090 token signing errors
  static final String SIGNING_SIGNER_METHOD         = PREFIX + "00071";
  static final String SIGNING_ALGORITHM_EXCEPTION   = PREFIX + "00072";
  static final String SIGNING_ALGORITHM_UNSUPPORTED = PREFIX + "00073";
  static final String SIGNING_ALGORITHM_NOTACCEPTED = PREFIX + "00074";
  static final String SIGNING_HMCA_UNSUPPORTED      = PREFIX + "00075";
  static final String SIGNING_HMCA_INVALIDTYPE      = PREFIX + "00076";
  static final String SIGNING_HMCA_INVALIDKEY       = PREFIX + "00077";
  static final String SIGNING_RSA_UNSUPPORTED       = PREFIX + "00078";
  static final String SIGNING_RSA_INVALIDTYPE       = PREFIX + "00079";
  static final String SIGNING_RSA_SIGNATURE         = PREFIX + "00080";
  static final String SIGNING_RSA_PRIVATEKEY        = PREFIX + "00081";
  static final String SIGNING_RSA_PUBLICKEY         = PREFIX + "00082";
  static final String SIGNING_STATE_UNDEFINED       = PREFIX + "00083";
  static final String SIGNING_STATE_UNSIGNED        = PREFIX + "00084";

  // 00091 - 00100 token validation related errors
  static final String TOKEN_EXPIRED                 = PREFIX + "00091";
  static final String TOKEN_NOTBEFORE               = PREFIX + "00092";
  static final String TOKEN_SIGNATURE               = PREFIX + "00093";

}