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
    Subsystem   :   Common shared naming facilities

    File        :   LoggingBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LoggingBundle_en.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-11-12  DSteding    First release version
*/

package oracle.hst.foundation.resource;

////////////////////////////////////////////////////////////////////////////////
// class LoggingBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~
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
public class LoggingBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // Logging Level
    { "OFF",     "OFF    " }
  , { "ALL",     "ALL    " }
  , { "FATAL",   "FATAL  " }
  , { "SEVERE",  "ERROR  " }
  , { "WARNING", "WARNING" }
  , { "CONFIG",  "INFO   " }
  , { "INFO",    "INFO   " }
  , { "FINE",    "DEBUG  " }
  , { "FINER",   "DEBUG  " }
  , { "FINEST",  "DEBUG  " }
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