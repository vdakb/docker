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
    Subsystem   :   SBranding Customization

    File        :   Bundle_en.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Bundle_en.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet     First release version
*/

package bka.iam.identity.ui.shell.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import bka.iam.identity.ui.BrandingError;

////////////////////////////////////////////////////////////////////////////////
// class Bundle_en
// ~~~~~ ~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // BB-00001 - 00010 system related errors
    { BrandingError.GENERAL,                         "General error: %1$s" }
  , { BrandingError.UNHANDLED,                       "An unhandled exception has occured: %1$s"}
  , { BrandingError.ABORT,                           "Execution aborted due to reason: %1$s"}
  , { BrandingError.NOTIMPLEMENTED,                  "Feature is not yet implemented"}

     // BB-00011 - 00020 method argument related errors
  , { BrandingError.ARGUMENT_IS_NULL,                "Passed argument %1$s must not be null" }
  , { BrandingError.ARGUMENT_BAD_TYPE,               "Passed argument %1$s has a bad type" }
  , { BrandingError.ARGUMENT_BAD_VALUE,              "Passed argument %1$s contains an invalid value" }
  , { BrandingError.ARGUMENT_SIZE_MISMATCH,          "Passed argument array size dont match expected length" }

     // BB-00021 - 00030 instance attribute related errors
  , { BrandingError.ATTRIBUTE_IS_NULL,               "State of attribute %1$s must not be null" }

     // BB-00032 - 00040 file related errors
  , { BrandingError.FILE_MISSING,                    "Encountered problems to find file %1$s" }
  , { BrandingError.FILE_IS_NOT_A_FILE,              "%1$s is not a file" }
  , { BrandingError.FILE_OPEN,                       "Encountered problems to open file %1$s" }
  , { BrandingError.FILE_CLOSE,                      "Encountered problems to close file %1$s" }
  , { BrandingError.FILE_READ,                       "Encountered problems reading file %1$s" }
  , { BrandingError.FILE_WRITE,                      "Encountered problems writing file %1$s" }

    // BB-00041 - 00050 file related errors
  , { BrandingError.BRANDING_CONFIGURATION_PROPERTY, "Cannot configure request model due to System Property %1$s is missing." }
  , { BrandingError.BRANDING_CONFIGURATION_STREAM,   "Cannot configure request model due to stream error %1$s." }
  , { BrandingError.BRANDING_CONFIGURATION_PARSING,  "Cannot configure request model due to parser error %1$s." }
  , { BrandingError.BRANDING_NOT_DEFINED,            "The component %l$s is not defined."}
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