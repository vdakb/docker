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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Directory Service Connector

    File        :   ReconciliationBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ReconciliationBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.gds.service.reconciliation.ReconciliationError;
import oracle.iam.identity.gds.service.reconciliation.ReconciliationMessage;

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
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class ReconciliationBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GDS-00061 - 00070 mapping related errors
    { ReconciliationError.ATTRIBUTES_NOT_MAPPED,      "No attribute of descriptor [%1$s] could be mapped with reconciliation profile attributes of Resource Object [%2$s]" }
  , { ReconciliationError.IDENTIFIER_NOT_MAPPED,      "Reconciliation Identifier [%2$s] is not configured in attribute mapping of descriptor [%1$s]" }

    // GDS-01081 - 01090 lookup reconciliation related messages
  , { ReconciliationMessage.CREATE_VALUE,             "Not able to add [%2$s] to Lookup Definition [%1$s]." }
  , { ReconciliationMessage.DUPLICATE_VALUE,          "New value [%2$s] is same as existing value in Lookup Definition [%1$s]." }

    // GDS-01091 - 01100 reconciliation process related messages
  , { ReconciliationMessage.SEARCH_CRITERIA,          "Perform %2$s search in Search Base [%1$s] with Search Filter [%3$s]" }
  , { ReconciliationMessage.SEARCH_CRITERIA_SORTED,   "Perform %2$s search in Search Base [%1$s] with Search Filter [%3$s] and applied Sort Order [%4$s]" }
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