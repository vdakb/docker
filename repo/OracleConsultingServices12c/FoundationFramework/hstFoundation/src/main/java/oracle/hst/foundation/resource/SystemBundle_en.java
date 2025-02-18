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
    Subsystem   :   Common shared runtime facilities

    File        :   SystemBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemBundle_en.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-11-12  DSteding    First release version
*/

package oracle.hst.foundation.resource;

import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.SystemMessage;

////////////////////////////////////////////////////////////////////////////////
// class SystemBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class SystemBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OHF-00001 - 00010 system related errors
    { SystemError.GENERAL,                   "General error: %1$s" }
  , { SystemError.UNHANDLED,                 "An unhandled exception has occured: %1$s!" }
  , { SystemError.ABORT,                     "Execution aborted due to reason: %1$s!" }
  , { SystemError.NOTIMPLEMENTED,            "Feature is not yet implemented!" }
  , { SystemError.CLASSNOTFOUND,             "Class %1$s was not found in the classpath!" }
  , { SystemError.CLASSNOTCREATE,            "Class %1$s has not been created!" }
  , { SystemError.CLASSINVALID,              "Class %1$s must be a subclass of %2$s!" }
  , { SystemError.CLASSCONSTRUCTOR,          "Class %1$s don't accept constructor parameter %2$s!" }

     // OHF-00011 - 00020 method argument related errors
  , { SystemError.ARGUMENT_IS_NULL,          "Passed argument %1$s must not be null!" }
  , { SystemError.ARGUMENT_BAD_TYPE,         "Passed argument %1$s has a bad type!" }
  , { SystemError.ARGUMENT_BAD_VALUE,        "Passed argument %1$s contains an invalid value!" }
  , { SystemError.ARGUMENT_SIZE_MISMATCH,    "Passed argument array size dont match expected length!" }

     // OHF-00021 - 00030 instance attribute related errors
  , { SystemError.ATTRIBUTE_IS_NULL,         "State of attribute %1$s must not be null!" }

     // OHF-00032 - 00040 file related errors
  , { SystemError.FILE_EXISTS,               "%1$s already exists!" }
  , { SystemError.FILE_MISSING,              "Encountered problems to find file %1$s!" }
  , { SystemError.FILE_NOT_FILE,             "%1$s is not a file!" }
  , { SystemError.FILE_NOT_DIRECTORY,        "%1$s is not a directory!" }
  , { SystemError.FILE_OPEN,                 "Encountered problems to open file %1$s!" }
  , { SystemError.FILE_CLOSE,                "Encountered problems to close file %1$s!" }
  , { SystemError.FILE_READ,                 "Encountered problems reading file %1$s!" }
  , { SystemError.FILE_WRITE,                "Encountered problems writing file %1$s!" }

     // OHF-00041 - 00050 java related errors
  , { SystemError.JAVAHOME_MISSING,          "JAVA_HOME is not set. Make sure Java is available on the m/c to proceed further!" }
  , { SystemError.COMMAND_ERROR,             "Error executing java runtime command %1$s!" }
  , { SystemError.COMMAND_EXEC,              "Error executing %1$s!" }
  , { SystemError.COMMAND_WAIT,              "Error waiting for %1$s!" }

     // OHF-00051 - 00060 command line related errors
  , { SystemError.CMDLINE_AMBIGIUOS,         "%1$s: commandline-option %2$s is defined ambiguity!" }
  , { SystemError.CMDLINE_ARGUMENT1,         "%1$s: commandline-option %2$s does not accept an argument!" }
  , { SystemError.CMDLINE_ARGUMENT2,         "%1$s: commandline-option %2$s,%2$s doss not accept an argument!" }
  , { SystemError.CMDLINE_REQUIRED1,         "%1$s: commandline-option %2$s requires an argument!" }
  , { SystemError.CMDLINE_REQUIRED2,         "%1$s: commandline-option %2$s,%3$s requires an argument!" }
  , { SystemError.CMDLINE_UNREGCOGNIZED1,    "%1$s: commandline-option %2$s not expected!" }
  , { SystemError.CMDLINE_UNREGCOGNIZED2,    "%1$s: commandline-option %2$s not expected in %2$s!" }
  , { SystemError.CMDLINE_ILLEGAL,           "%1$s: commandline-option %2$s is illegal!" }
  , { SystemError.CMDLINE_INVALID,           "%1$s: commandline-option %2$s is invalid!" }
  , { SystemError.CMDLINE_INVALIDVALUE,      "%1$s: commandline-option %2$s has wrong argument specification!" }

     // OHF-00061 - 00070 system timer related errors
  , { SystemError.TIMER_UNSUPPORTED,         "Task info is not being kept!!" }
  , { SystemError.TIMER_STILL_RUNNING,       "Can't start SystemWatch: it's already running!" }
  , { SystemError.TIMER_NOT_RUNNING,         "Can't stop SystemWatch: it's not running!" }
  , { SystemError.TIMER_NO_TASK,             "No tests run: can't get last interval!" }
  , { SystemError.TIMER_TASK_SWITCH,         "The task list is always kept to be able to calculate the min, max and average!" }

     // OHF-00061 - 00070 system timer related errors
  , { SystemError.THREAD_POOL_SIZE_CAPACITY, "Pool capacity cannot be negative!" }
  , { SystemError.THREAD_POOL_SIZE_MINIMUM,  "Minimum pool size cannot less than one!" }
  , { SystemError.THREAD_POOL_SIZE_MAXIMUM,  "Maximum pool size cannot less than one!" }
  , { SystemError.THREAD_POOL_KEEP_ALLIVE,   "Keep alive time cannot be negative!" }
  , { SystemError.THREAD_POOL_MIN_MAX_GT,    "Minimum Pool Size cannot be greater than Maximum Pool Size!" }

     // OHF-01001 - 01010 system timer related messages
  , { SystemMessage.TIMER_SUMMARY,           "\n\n%1$s: elapsed overall [ms] = %2$s\n\n" }
  , { SystemMessage.TIMER_ELAPSED,           "elapsed   [ms]" }
  , { SystemMessage.TIMER_PERCENT,           "    [%]" }
  , { SystemMessage.TIMER_COUNTER,           "    [#]" }
  , { SystemMessage.TIMER_METHOD,            "Thread::Class-Method" }

     // OHF-01011 - 01020 property mapping related messages
  , { SystemMessage.COLUMN_NAME,            "Column" }
  , { SystemMessage.COLUMN_VALUE,           "Value" }
  , { SystemMessage.MAPPING_NAME,           "Key" }
  , { SystemMessage.MAPPING_VALUE,          "Value" }
  , { SystemMessage.FEATURE_NAME,           "Feature" }
  , { SystemMessage.FEATURE_VALUE,          "Value" }
  , { SystemMessage.PROPERTY_NAME,          "Property" }
  , { SystemMessage.PROPERTY_VALUE,         "Value" }
 };

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)
  /**
   ** Returns an array, where each item in the array is a pair of objects.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of objects.
   */
  public Object[][] getContents() {
    return CONTENT;
  }
}