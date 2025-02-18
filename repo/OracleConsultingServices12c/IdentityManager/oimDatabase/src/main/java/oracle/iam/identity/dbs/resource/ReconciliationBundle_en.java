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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Database Account Connector

    File        :   ReconciliationBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ReconciliationBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.dbs.service.reconciliation.ReconciliationError;
import oracle.iam.identity.dbs.service.reconciliation.ReconciliationMessage;

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
 */
public class ReconciliationBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // DBS-00081 - 00090 parameter related errors
    { ReconciliationError.ATTRIBUTE_INCONSISTENT,     "A value must be provided for %2$s if option %1$s is selected"  }

    // DBS-01071 - 01080 lookup reconciliation related messages
  , { ReconciliationMessage.RECONFIELD_NOT_DESCRIBED, "Field %2$s is declared on %1$s as Multi-Valued, but no descriptor can be found. Field will not be reconciled" }
  , { ReconciliationMessage.ATTRIBUTE_NOT_MAPPED,     "Attribute %1$s %3$s not mapped for %2$s" }

    // DBS-01081 - 01090 lookup reconciliation related messages
  , { ReconciliationMessage.CREATE_VALUE,             "Not able to add [%2$s] to Lookup Definition [%1$s]." }
  , { ReconciliationMessage.DUPLICATE_VALUE,          "New value [%2$s] is same as existing value in Lookup Definition [%1$s]." }
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