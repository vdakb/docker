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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Java Enterprise Service Connector Library

    File        :   ServerError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServerError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.jes;

////////////////////////////////////////////////////////////////////////////////
// interface ServerError
// ~~~~~~~~~ ~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ServerError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                            = "JES-";

  // 00001 - 00010 connection related errors
  static final String CONNECTION_SERVERTYPE_UNSUPPORTED = PREFIX + "00001";
  static final String CONNECTION_LOGINCONFIG_NOTFOUND   = PREFIX + "00002";
//  static final String CONTEXT_CONNECTION_ERROR        = PREFIX + "00003";
//  static final String CONTEXT_AUTHENTICATION          = PREFIX + "00004";
  static final String CONTEXT_ACCESS_DENIED             = PREFIX + "00005";

  // 00011 - 00020 filtering errors
  static final String FILTER_METHOD_INCONSISTENT        = PREFIX + "00011";
  static final String FILTER_EXPRESSION_INCONSISTENT    = PREFIX + "00012";
  static final String FILTER_USAGE_INVALID_GE           = PREFIX + "00012";
  static final String FILTER_USAGE_INVALID_GT           = PREFIX + "00013";
  static final String FILTER_USAGE_INVALID_LE           = PREFIX + "00014";
  static final String FILTER_USAGE_INVALID_LT           = PREFIX + "00015";

  // 00021 - 00030 processing errors
  static final String PROCESS_EXISTS                    = PREFIX + "00021";
  static final String PROCESS_NOT_EXISTS                = PREFIX + "00022";
  static final String PROCESS_INVALID_FILTER            = PREFIX + "00023";

  // 00031 - 00040 object errors
  static final String OBJECT_NOT_EXISTS                 = PREFIX + "00031";
  static final String OBJECT_AMBIGUOUS                  = PREFIX + "00032";
}