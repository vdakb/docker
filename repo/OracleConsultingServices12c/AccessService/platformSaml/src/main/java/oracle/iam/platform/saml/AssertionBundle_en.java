/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AssertionBundle_en.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml;

import java.util.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AssertionBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~~
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
 ** @since   1.0.0.0
 */
public class AssertionBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OAM-00001 - 00010 operations related errors
    { AssertionError.UNHANDLED,                       "Unhandled exception occured: %1$s" }
  , { AssertionError.GENERAL,                         "Encounter some problems: %1$s" }
  , { AssertionError.ABORT,                           "Execution aborted: %1$s" }
  , { AssertionError.NOTIMPLEMENTED,                  "Feature is not yet implemented!" }

     // OAM-00011 - 00020 method argument related errors
  , { AssertionError.ARGUMENT_IS_NULL,                "Passed argument %1$s must not be null!" }
  , { AssertionError.ARGUMENT_BAD_TYPE,               "Passed argument %1$s has a bad type!" }
  , { AssertionError.ARGUMENT_BAD_VALUE,              "Passed argument %1$s contains an invalid value!" }
  , { AssertionError.ARGUMENT_SIZE_MISMATCH,          "Passed argument array size dont match expected length!" }

     // OAM-00021 - 00030 XML parser related errors
  , { AssertionError.PARSER_FATAL,                    "Fatal: [%1$s]" }
  , { AssertionError.PARSER_ERROR,                    "Error: [%1$s]" }
  , { AssertionError.PARSER_WARNING,                  "Warning: [%1$s]" }
  , { AssertionError.PARSER_FEATURE,                  "Unsupported XML parser feature [%1$s]!" }

     // OAM-00031 - 00040 signature related errors
  , { AssertionError.SIGNATURE_MISSING,               "No XML Signature Found, validation discarded!" }
  , { AssertionError.SIGNATURE_CORE_FAILED,           "Signature failed core validation!" }

     // OAM-00041 - 00050 token validation related errors
  , { AssertionError.TOKEN_ISSUED,                    "Token was issued for the future [%1$s]!" }
  , { AssertionError.TOKEN_NOTAFTER,                  "Token expired at [%1$s]!" }
  , { AssertionError.TOKEN_NOTBEFORE,                 "Token is used before its allowed use [%1$s]!" }

     // OAM-01001 - 01010 XML parser related messages
  , { AssertionMessage.PARSER_ENTITY_RESOLVED,        "ResolveEntity query: systemId=[%1$s] publicId=[%2$s] return=[%3$s]." }

     // OAM-01011 - 01020 signature related messages
  , { AssertionMessage.SIGNATURE_CORE_PASSED,         "Signature passed core validation." }
  , { AssertionMessage.SIGNATURE_CORE_STATUS,         "Signature validation status: %1$s." }
  , { AssertionMessage.SIGNATURE_STATUS_VALID,        "valid" }
  , { AssertionMessage.SIGNATURE_STATUS_INVALID,      "invalid" }
  , { AssertionMessage.SIGNATURE_STATUS_VALIDATED,    "validated" }
  , { AssertionMessage.SIGNATURE_STATUS_NOTVALIDATED, "not validated" }
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
  @Override
  public Object[][] getContents() {
    return CONTENT;
  }
}