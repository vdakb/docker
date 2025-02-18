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

    File        :   SystemError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SystemError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation;

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
  static final String GENERAL                   = SystemConstant.PREFIX + "00001";
  static final String UNHANDLED                 = SystemConstant.PREFIX + "00002";
  static final String ABORT                     = SystemConstant.PREFIX + "00003";
  static final String NOTIMPLEMENTED            = SystemConstant.PREFIX + "00004";
  static final String CLASSNOTFOUND             = SystemConstant.PREFIX + "00005";
  static final String CLASSNOTCREATE            = SystemConstant.PREFIX + "00006";
  static final String CLASSNOACCESS             = SystemConstant.PREFIX + "00007";
  static final String CLASSINVALID              = SystemConstant.PREFIX + "00008";
  static final String CLASSCONSTRUCTOR          = SystemConstant.PREFIX + "00009";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL          = SystemConstant.PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE         = SystemConstant.PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE        = SystemConstant.PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH    = SystemConstant.PREFIX + "00014";

  // 00021 - 00030 instance attribute related errors
  static final String ATTRIBUTE_IS_NULL         = SystemConstant.PREFIX + "00021";

  // 00032 - 00040 file related errors
  static final String FILE_EXISTS               = SystemConstant.PREFIX + "00031";
  static final String FILE_MISSING              = SystemConstant.PREFIX + "00032";
  static final String FILE_NOT_FILE             = SystemConstant.PREFIX + "00033";
  static final String FILE_NOT_DIRECTORY        = SystemConstant.PREFIX + "00034";
  static final String FILE_OPEN                 = SystemConstant.PREFIX + "00035";
  static final String FILE_CLOSE                = SystemConstant.PREFIX + "00036";
  static final String FILE_READ                 = SystemConstant.PREFIX + "00037";
  static final String FILE_WRITE                = SystemConstant.PREFIX + "00038";

  // 00041 - 00050 java related errors
  static final String JAVAHOME_MISSING          = SystemConstant.PREFIX + "00041";
  static final String COMMAND_ERROR             = SystemConstant.PREFIX + "00042";
  static final String COMMAND_EXEC              = SystemConstant.PREFIX + "00043";
  static final String COMMAND_WAIT              = SystemConstant.PREFIX + "00044";

  // 00051 - 00060 command line related errors
  static final String CMDLINE_AMBIGIUOS         = SystemConstant.PREFIX + "00051";
  static final String CMDLINE_ARGUMENT1         = SystemConstant.PREFIX + "00052";
  static final String CMDLINE_ARGUMENT2         = SystemConstant.PREFIX + "00053";
  static final String CMDLINE_REQUIRED1         = SystemConstant.PREFIX + "00054";
  static final String CMDLINE_REQUIRED2         = SystemConstant.PREFIX + "00055";
  static final String CMDLINE_UNREGCOGNIZED1    = SystemConstant.PREFIX + "00056";
  static final String CMDLINE_UNREGCOGNIZED2    = SystemConstant.PREFIX + "00057";
  static final String CMDLINE_ILLEGAL           = SystemConstant.PREFIX + "00058";
  static final String CMDLINE_INVALID           = SystemConstant.PREFIX + "00059";
  static final String CMDLINE_INVALIDVALUE      = SystemConstant.PREFIX + "00060";

  // 00061 - 00070 system timer related errors
  static final String TIMER_UNSUPPORTED         = SystemConstant.PREFIX + "00061";
  static final String TIMER_STILL_RUNNING       = SystemConstant.PREFIX + "00062";
  static final String TIMER_NOT_RUNNING         = SystemConstant.PREFIX + "00063";
  static final String TIMER_NO_TASK             = SystemConstant.PREFIX + "00064";
  static final String TIMER_TASK_SWITCH         = SystemConstant.PREFIX + "00066";

  // 00071 - 00080 thread pool related errors
  static final String THREAD_POOL_SIZE_CAPACITY = SystemConstant.PREFIX + "00071";
  static final String THREAD_POOL_SIZE_MINIMUM  = SystemConstant.PREFIX + "00072";
  static final String THREAD_POOL_SIZE_MAXIMUM  = SystemConstant.PREFIX + "00073";
  static final String THREAD_POOL_KEEP_ALLIVE   = SystemConstant.PREFIX + "00074";
  static final String THREAD_POOL_MIN_MAX_GT    = SystemConstant.PREFIX + "00075";
}