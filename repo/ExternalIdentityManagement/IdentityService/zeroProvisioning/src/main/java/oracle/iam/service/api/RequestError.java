/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   RequestError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RequestError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.service.api;

////////////////////////////////////////////////////////////////////////////////
// interface RequestError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface RequestError extends RequestConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 00001 - 00010 system related errors
  static final String GENERAL                        = PREFIX + "00001";
  static final String UNHANDLED                      = PREFIX + "00002";
  static final String ABORT                          = PREFIX + "00003";
  static final String NOTIMPLEMENTED                 = PREFIX + "00004";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL               = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE              = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE             = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH         = PREFIX + "00014";

  // 00021 - 00030 instance attribute related errors
  static final String ATTRIBUTE_IS_NULL              = PREFIX + "00021";

  // 00041 - 00050 request related errors
  static final String REQUEST_APPLICATION_NOTFOUND   = PREFIX + "00045";
  static final String REQUEST_FAILED                 = PREFIX + "00050";
}