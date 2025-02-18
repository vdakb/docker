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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   IdentityServerBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityServerBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.rmi.IdentityServerError;
import oracle.iam.identity.foundation.rmi.IdentityServerMessage;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
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
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class IdentityServerBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // RMI-00001 - 00010 operations related errors
    { IdentityServerError.UNHANDLED,                       "Unhandled exception occured: %1$s" }
  , { IdentityServerError.GENERAL,                         "Encounter some problems: %1$s" }
  , { IdentityServerError.ABORT,                           "Execution aborted: %1$s"}
  , { IdentityServerError.NOTIMPLEMENTED,                  "Feature is not yet implemented"}

     // RMI-00011 - 00020 context related errors
  , { IdentityServerError.CONTEXT_SERVERTYPE_NOTSUPPORTED, "Server Type [%1$s] not supported" }
  , { IdentityServerError.CONTEXT_ENCODING_NOTSUPPORTED,   "URL Encoding [%1$s] not supported" }
  , { IdentityServerError.CONTEXT_CONNECTION_ERROR,        "Unable to establish connection. Reason:\n%1$s"}
  , { IdentityServerError.CONTEXT_AUTHENTICATION,          "Principal Name [%1$s] or Password is incorrect, system failed to get access with supplied credentials" }
  , { IdentityServerError.CONTEXT_ACCESS_DENIED,           "Principal [%1$s] has insufficient privileges to perform operation [%2$s]" }

     // RMI-00021 - 00030 identity related errors
  , { IdentityServerError.IDENTITY_NOT_EXISTS,             "Identity [%1$s] does not exists" }
  , { IdentityServerError.IDENTITY_AMBIGUOUS,              "Identity [%1$s] defined ambiguously" }
  , { IdentityServerError.PERMISSION_NOT_EXISTS,           "Permission [%1$s] does not exists" }
  , { IdentityServerError.PERMISSION_AMBIGUOUS,            "Permission [%1$s] defined ambiguously" }
  , { IdentityServerError.ORGANIZATION_NOT_EXISTS,         "Organizational Scope [%1$s] does not exists" }
  , { IdentityServerError.ORGANIZATION_AMBIGUOUS,          "Organizational Scope [%1$s] defined ambiguously" }
  , { IdentityServerError.ROLE_NOT_EXISTS,                 "Role [%1$s] does not exists" }
  , { IdentityServerError.ROLE_AMBIGUOUS,                  "Role [%1$s] defined ambiguously" }
  , { IdentityServerError.ROLE_SEARCH_KEY_AMBIGUOUS,       "Search Key for role [%1$s] defined ambiguously" }

     // RMI-00031 - 00040 permission assignment errors
  , { IdentityServerError.PERMISSION_ACCESS_DENEID,        "Principal [%1$s] has insufficient privileges to perform operation [%2$s]" }
  , { IdentityServerError.PERMISSION_NOT_GRANTED,          "Not able to grant Permission [%1$s] to [%2$s]" }
  , { IdentityServerError.PERMISSION_NOT_REVOKED,          "Not able to revoke Permission [%1$s] from [%2$s]" }
  , { IdentityServerError.PERMISSION_NOT_MODIFIED,         "Not able to modify Permission [%1$s] for [%2$s]" }

    // RMI-01001 - 01010 system related messages
  , { IdentityServerMessage.CONNECTING_TO,                 "Connecting to [%1$s]" }
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