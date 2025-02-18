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

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Common Shared Plugin

    File        :   Bundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Bundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.policy;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.policy.usr.LoginName;
import oracle.iam.identity.policy.usr.CommonName;
import oracle.iam.identity.policy.usr.NamePolicyError;

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
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    {LoginName.class.getClass().getSimpleName(),    "Consulting User Name Policy - returns first char of First Name appended with Last name as user name"}
  , {CommonName.class.getClass().getSimpleName(),   "Consulting Common Name Policy - returns a concatenation of Last Name, First Name and Middle Name for common name as dictated through the pattern specified by system property \"OCS.Policy.CommonName\""}

  , {NamePolicyError.USERNAME_FAILURE,              "An error occurred while generating the User Name. Please provide %1$s as expected by %2$s Generator." }
  , {NamePolicyError.COMMONNAME_FAILURE,            "An error occurred while generating the Common Name. Please provide %1$s as expected by %2$s Generator." }
  , {NamePolicyError.PROPERTY_NOTFOUND,             "The system property %1$s doesn't exists. Please provide %1$s as expected by Name Policy Generator." }
  , {NamePolicyError.PROPERTY_INVALID,              "The system property %1$s isn't configured properly. Please provide %1$s as expected by Name Policy Generator." }

    // 00091 - 00100 process related errors
  , { NamePolicyError.IDENTITY_PRE_PROCESS_DATA,    "Encountered error during pre-processing of user event. Please provide %1$s as expected by %2$s." }
  , { NamePolicyError.IDENTITY_POST_PROCESS_DATA,   "Encountered error during post-processing of user event. Please provide %1$s as expected by %2$s." }
  , { NamePolicyError.IDENTITY_POST_PROCESS_SIMPLE, "Encountered error during post-processing of simple user event." }
  , { NamePolicyError.IDENTITY_POST_PROCESS_BULK,   "Encountered error during post-processing of bulk user event." }
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