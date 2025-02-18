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
    Subsystem   :   Generic REST Library

    File        :   ServiceError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest;

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
  static final String PREFIX                                = "GWS-";

  // 00031 - 00040 authorization errors
  static final String OAUTH_FLOW_NOT_FINISH                 = PREFIX + "00031";
  static final String OAUTH_FLOW_WRONG_STATE                = PREFIX + "00032";
  static final String OAUTH_FLOW_ACCESS_TOKEN               = PREFIX + "00033";
  static final String OAUTH_FLOW_REFRESH_TOKEN              = PREFIX + "00034";

  // 00041 - 00060 marshalling errors
  static final String PATH_UNEXPECTED_EOF_STRING            = PREFIX + "00041";
  static final String PATH_UNEXPECTED_EOF_FILTER            = PREFIX + "00042";
  static final String PATH_UNEXPECTED_CHARACTER             = PREFIX + "00043";
  static final String PATH_UNEXPECTED_TOKEN                 = PREFIX + "00044";
  static final String PATH_UNRECOGNOIZED_OPERATOR           = PREFIX + "00045";
  static final String PATH_INVALID_FILTER                   = PREFIX + "00046";
  static final String PATH_EXPECT_PARENTHESIS               = PREFIX + "00047";
  static final String PATH_INVALID_PARENTHESIS              = PREFIX + "00048";
  static final String PATH_EXPECT_ATTRIBUTE_PATH            = PREFIX + "00049";
  static final String PATH_EXPECT_ATTRIBUTE_NAME            = PREFIX + "00050";
  static final String PATH_INVALID_ATTRIBUTE_PATH           = PREFIX + "00051";
  static final String PATH_INVALID_ATTRIBUTE_NAME           = PREFIX + "00052";
  static final String PATH_INVALID_COMPARISON_VALUE         = PREFIX + "00053";
  static final String PATH_INVALID_VALUE_DEPTH              = PREFIX + "00054";
  static final String PATH_INVALID_VALUE_FILTER             = PREFIX + "00055";

  // 00061 - 00070 request parameter errors
  static final String PARAMETER_SORT_INVALID_VALUE          = PREFIX + "00061";

  // 00071 - 00080 filtering errors
  static final String FILTER_METHOD_INCONSISTENT            = PREFIX + "00071";
  static final String FILTER_EXPRESSION_INCONSISTENT        = PREFIX + "00072";
  static final String FILTER_USAGE_INVALID_GE               = PREFIX + "00072";
  static final String FILTER_USAGE_INVALID_GT               = PREFIX + "00073";
  static final String FILTER_USAGE_INVALID_LE               = PREFIX + "00074";
  static final String FILTER_USAGE_INVALID_LT               = PREFIX + "00075";

  // 00101 - 00120 processing errors
  static final String PROCESS_UNEXPECTED                    = PREFIX + "00101";
  static final String PROCESS_UNAVAILABLE                   = PREFIX + "00102";
  static final String PROCESS_AUTHORIZATION                 = PREFIX + "00103";
  static final String PROCESS_FORBIDDEN                     = PREFIX + "00104";
  static final String PROCESS_NOT_ALLOWED                   = PREFIX + "00105";
  static final String PROCESS_NOT_ACCEPTABLE                = PREFIX + "00106";
  static final String PROCESS_MEDIATYPE_UNSUPPORTED         = PREFIX + "00107";
  static final String PROCESS_EXISTS                        = PREFIX + "00108";
  static final String PROCESS_EXISTS_NOT                    = PREFIX + "00109";
  static final String PROCESS_PRECONDITION                  = PREFIX + "00110";
  static final String PROCESS_POSTCONDITION                 = PREFIX + "00111";
  static final String PROCESS_TOO_LARGE                     = PREFIX + "00112";
  static final String PROCESS_INVALID_VERSION               = PREFIX + "00113";
  static final String PROCESS_TOO_MANY                      = PREFIX + "00114";
  static final String PROCESS_MUTABILITY                    = PREFIX + "00115";
  static final String PROCESS_SENSITIVE                     = PREFIX + "00116";
  static final String PROCESS_UNIQUENESS                    = PREFIX + "00117";
  static final String PROCESS_NOTARGET                      = PREFIX + "00118";
  static final String PROCESS_INVALID_FILTER                = PREFIX + "00119";
  static final String PROCESS_INVALID_SYNTAX                = PREFIX + "00120";
  static final String PROCESS_INVALID_PATH                  = PREFIX + "00121";
  static final String PROCESS_INVALID_VALUE                 = PREFIX + "00122";

  // 00131 - 00140 object errors
  static final String OBJECT_NOT_EXISTS                     = PREFIX + "00131";
  static final String OBJECT_AMBIGUOUS                      = PREFIX + "00132";
}