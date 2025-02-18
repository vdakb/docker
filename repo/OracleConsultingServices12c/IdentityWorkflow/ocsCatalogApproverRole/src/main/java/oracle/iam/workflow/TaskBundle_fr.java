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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Approval Workflow
    Subsystem   :   Catalog Approver Role Approval

    File        :   TaskBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TaskBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/
package oracle.iam.workflow;

import java.util.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class TaskBundle_fr
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code french
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TaskBundle_fr extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    {"approval.title",                 "Approbation de la demande d'accès pour l'ID de demande  "}
  , {"approval.short",                 "Brève Description"}
  , {"approval.long",                  "Longue Description"}
  , {"approval.prefix",                "Tâche "}
  , {"approval.suffix",                " requiert votre attention. "}

  , {"approval.stage",                 "Etape d'approbation"}
  , {"approval.assignee",              "Destinataire"}

  , {"challenge.title",                "Tâche de défi pour l'ID de demande  "}
  , {"challenge.short",                "Brève Description"}
  , {"challenge.long",                 "Longue Description"}
    
  , {"challenge.beneficiary",          "Etape d'approbation du bénéficiaire"}
  , {"challenge.beneficiary.assignee", "Bénéficiaire destinataire"}
  , {"challenge.requester",            "Etape d'approbation du demandeur"}
  , {"challenge.requester.assignee",   "Demandeur destinataire"}
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