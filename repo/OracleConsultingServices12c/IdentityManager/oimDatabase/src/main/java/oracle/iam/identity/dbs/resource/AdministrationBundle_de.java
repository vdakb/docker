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

    File        :   AdministrationBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AdministrationBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.dbs.persistence.AdministrationMessage;
import oracle.iam.identity.dbs.persistence.AdministrationError;

////////////////////////////////////////////////////////////////////////////////
// class AdministrationBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AdministrationBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // DBA-00081 - 00090 regular expresssion errors
    { AdministrationError.EXPRESSION_BITVALUES, "Undefined bit values are set in the regular expression compile options" }
  , { AdministrationError.EXPRESSION_INVALID,   "An error is contained in the regular expression [%1$s]: [%2$s]" }

     // DBA-00091 - 00130 operational errors
  , { AdministrationError.OBJECT_CONNECTED,     "Eintrag [%1$s] konnte nicht gelöscht werden, solange er noch mit der Datenbank verbunden ist" }
  , { AdministrationError.OBJECT_NOT_ENABLED,   "Eintrag [%1$s] konnte nicht aktiviert werden" }
  , { AdministrationError.OBJECT_NOT_DISABLED,  "Eintrag[%1$s] konnte nicht deaktiviert werden" }
  , { AdministrationError.OBJECT_NOT_LOCKED,    "Eintrag [%1$s] konnte nicht gesperrt werden" }
  , { AdministrationError.OBJECT_NOT_UNLOCKED,  "Eintrag [%1$s] konnte nicht entsperrt werden" }

     // DBA-01021 - 01030 filtering related messages
  , { AdministrationMessage.EXCLUDE_ROLE,       "Role [%1$s] excluded due to [%2$s]" }
  , { AdministrationMessage.EXCLUDE_PRIVILEGE,  "Privilege [%1$s] excluded due to [%2$s]" }
  , { AdministrationMessage.EXCLUDE_ACCOUNT,    "Account [%1$s] excluded due to [%2$s]" }
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