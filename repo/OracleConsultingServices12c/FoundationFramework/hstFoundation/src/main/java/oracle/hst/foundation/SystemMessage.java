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

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   SystemMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SystemMessage.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation;

////////////////////////////////////////////////////////////////////////////////
// interface SystemMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SystemMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Constant used to demarcate method entry/exit etc. */
  // Fixed DE-000126
  // Constant used to demarcate method entry/exit etc. needs to be part of
  // product independent interfaces
  static final String METHOD_ENTRY    = "entry";
  static final String METHOD_EXIT     = "exit";

  // 01001 - 01010 system timer related messages
  static final String TIMER_SUMMARY   = SystemConstant.PREFIX + "01001";
  static final String TIMER_ELAPSED   = SystemConstant.PREFIX + "01002";
  static final String TIMER_PERCENT   = SystemConstant.PREFIX + "01003";
  static final String TIMER_COUNTER   = SystemConstant.PREFIX + "01004";
  static final String TIMER_METHOD    = SystemConstant.PREFIX + "01005";

  // 01011 - 01020 property mapping related messages
  static final String COLUMN_NAME     = SystemConstant.PREFIX + "01011";
  static final String COLUMN_VALUE    = SystemConstant.PREFIX + "01012";
  static final String MAPPING_NAME    = SystemConstant.PREFIX + "01012";
  static final String MAPPING_VALUE   = SystemConstant.PREFIX + "01013";
  static final String FEATURE_NAME    = SystemConstant.PREFIX + "01014";
  static final String FEATURE_VALUE   = SystemConstant.PREFIX + "01015";
  static final String PROPERTY_NAME   = SystemConstant.PREFIX + "01016";
  static final String PROPERTY_VALUE  = SystemConstant.PREFIX + "01017";
}