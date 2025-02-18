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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Special Account Request

    File        :   RequestError.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RequestError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package bka.iam.identity.ui;

////////////////////////////////////////////////////////////////////////////////
// interface RequestError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface RequestError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 00001 - 00010 system related errors
  static final String GENERAL                        = RequestConstant.PREFIX + "00001";
  static final String UNHANDLED                      = RequestConstant.PREFIX + "00002";
  static final String ABORT                          = RequestConstant.PREFIX + "00003";
  static final String NOTIMPLEMENTED                 = RequestConstant.PREFIX + "00004";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL               = RequestConstant.PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE              = RequestConstant.PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE             = RequestConstant.PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH         = RequestConstant.PREFIX + "00014";

  // 00021 - 00030 instance attribute related errors
  static final String ATTRIBUTE_IS_NULL              = RequestConstant.PREFIX + "00021";

  // 00032 - 00040 file related errors
  static final String FILE_MISSING                   = RequestConstant.PREFIX + "00031";
  static final String FILE_IS_NOT_A_FILE             = RequestConstant.PREFIX + "00032";
  static final String FILE_OPEN                      = RequestConstant.PREFIX + "00033";
  static final String FILE_CLOSE                     = RequestConstant.PREFIX + "00034";
  static final String FILE_READ                      = RequestConstant.PREFIX + "00035";
  static final String FILE_WRITE                     = RequestConstant.PREFIX + "00036";

  // 00041 - 00050 request related errors
  static final String REQUEST_CONFIGURATION_PROPERTY = RequestConstant.PREFIX + "00041";
  static final String REQUEST_CONFIGURATION_STREAM   = RequestConstant.PREFIX + "00042";
  static final String REQUEST_CONFIGURATION_PARSING  = RequestConstant.PREFIX + "00043";
  static final String REQUEST_APPLICATION_LOOKUP     = RequestConstant.PREFIX + "00044";
  static final String REQUEST_APPLICATION_NOTFOUND   = RequestConstant.PREFIX + "00045";
  static final String REQUEST_ENTITLEMENT_NOTFOUND   = RequestConstant.PREFIX + "00046";
  static final String REQUEST_ENTITLEMENT_AMBIGUOUS  = RequestConstant.PREFIX + "00047";
  static final String REQUEST_SELECTION_VIOLATED     = RequestConstant.PREFIX + "00048";
  static final String REQUEST_PREDECESSOR_VIOLATED   = RequestConstant.PREFIX + "00049";
  static final String REQUEST_FAILED                 = RequestConstant.PREFIX + "00050";

  // 00051 - 00060 evaluation related errors
  static final String EVALUATION_INTERNAL_ERROR      = RequestConstant.EVAL_PREFIX + "00051";
  static final String EVALUATION_UNKNOWN_ERROR       = RequestConstant.EVAL_PREFIX + "00052";
}