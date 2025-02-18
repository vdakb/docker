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

    File        :   LocatorBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LocatorBundle_en.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-11-12  DSteding    First release version
*/

package oracle.hst.foundation.resource;

import oracle.hst.foundation.naming.LocatorError;

////////////////////////////////////////////////////////////////////////////////
// class LocatorBundle_en
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
public class LocatorBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // General error (Undefined)
    { LocatorError.GENERAL,                "General error: %1$s" }
  , { LocatorError.UNHANDLED,              "An unhandled exception has occured: %1$s" }
  , { LocatorError.ABORT,                  "Execution aborted due to reason: %1$s" }
  , { LocatorError.NOTIMPLEMENTED,         "Feature is not yet implemented!" }
  , { LocatorError.CLASSNOTFOUND,          "Class %1$s was not found in the classpath!" }
  , { LocatorError.CLASSNOTCREATE,         "Class %1$s has not been created!" }
  , { LocatorError.CLASSINVALID,           "Class %1$s must be a subclass of %2$s!" }
  , { LocatorError.CLASSCONSTRUCTOR,       "Class %1$s don't accept constructor parameter %2$s!" }

    // Argument errors
  , { LocatorError.ARGUMENT_IS_NULL,       "Passed argument %1$s must not be null!" }
  , { LocatorError.ARGUMENT_BAD_TYPE,      "Passed argument %1$s has a bad type!" }
  , { LocatorError.ARGUMENT_BAD_VALUE,     "Passed argument %1$s contains an invalid value!" }
  , { LocatorError.ARGUMENT_SIZE_MISMATCH, "Passed argument array size dont match expected length!" }

    // JNDI naming and lookup related errors
  , { LocatorError.ATTRIBUTE_IS_NULL,      "State of attribute %1$s must not be null!" }
  , { LocatorError.LOCATOR_INITIALIZE,     "Could not initialize service locator!" }
  , { LocatorError.CONTEXT_CONNECTION,     "Could connect to JNDI context at %1$s!" }
  , { LocatorError.CONTEXT_INITIALIZE,     "Could not initialize JNDI context!" }
  , { LocatorError.CONTEXT_CLOSE,          "Could not close JNDI context!" }
  , { LocatorError.CONTEXT_ENVIRONMENT,    "Could not obtain environment from context!" }
  , { LocatorError.OBJECT_LOOKUP,          "Lookup failed in JNDI context for object %1$s!" }
  , { LocatorError.OBJECT_CREATION,        "Could not create object %1$s!" }
  , { LocatorError.OBJECT_ACCESS,          "Access to object %1$s has been failed!" }
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