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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   ReconciliationBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ReconciliationBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.ots.service.reconciliation.ReconciliationError;
import oracle.iam.identity.ots.service.reconciliation.ReconciliationMessage;

////////////////////////////////////////////////////////////////////////////////
// class ReconciliationBundle_en
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
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class ReconciliationBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // IRR-00011 - 00020 role reconciliation process related errors
    { ReconciliationError.ROLE_ASSIGNED,    "Role [%1$s] not assigned to Identity [%2$s]. Reason: %3$s" }
  , { ReconciliationError.ROLE_REVOKED,     "Role [%1$s] not revoked from Identity [%2$s]. Reason: %3$s" }

     // IRR-01011 - 01020 role reconciliation process related messages
  , { ReconciliationMessage.ROLE_IGNORED,   "Managing Roles for Identity [%1$s] ignored due to user request" }
  , { ReconciliationMessage.ROLE_ASSIGN,    "Assign Role [%1$s] to Identity [%2$s] ..." }
  , { ReconciliationMessage.ROLE_ASSIGNED,  "Role [%1$s] assigned to Identity [%2$s]" }
  , { ReconciliationMessage.ROLE_REVOKE,    "Revoke Role [%1$s] from Identity [%2$s] ..." }
  , { ReconciliationMessage.ROLE_REVOKED,   "Role [%1$s] revoked from Identity [%2$s]" }
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