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

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AssertionError.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AssertionError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml;

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
public interface AssertionError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                 = "OAM-";

  // 00001 - 00010 operations related errors
  static final String UNHANDLED              = PREFIX + "00001";
  static final String GENERAL                = PREFIX + "00002";
  static final String ABORT                  = PREFIX + "00003";
  static final String NOTIMPLEMENTED         = PREFIX + "00004";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL       = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE      = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE     = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH = PREFIX + "00014";

  // 00021 - 00030 XML parser related errors
  static final String PARSER_FATAL           = PREFIX + "00021";
  static final String PARSER_ERROR           = PREFIX + "00022";
  static final String PARSER_WARNING         = PREFIX + "00023";
  static final String PARSER_FEATURE         = PREFIX + "00024";

  // 00031 - 00040 signature related errors
  static final String SIGNATURE_MISSING      = PREFIX + "00031";
  static final String SIGNATURE_CORE_FAILED  = PREFIX + "00032";

  // 00041 - 00050 token validation related errors
  static final String TOKEN_ISSUED           = PREFIX + "00041";
  static final String TOKEN_NOTAFTER         = PREFIX + "00042";
  static final String TOKEN_NOTBEFORE        = PREFIX + "00043";
}