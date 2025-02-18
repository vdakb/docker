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

    File        :   Bundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Bundle_de.


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
// class Bundle_de
// ~~~~~ ~~~~~~~~~
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
 ** @since   1.0.0.0
 */
public class Bundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    {LoginName.class.getClass().getSimpleName(),    "Richtlinie Benutzername - gibt als Benutzername den ersten Buchstaben des Vornames mit anghängtem Nachnamen als Benutzername zurück"}
  , {CommonName.class.getClass().getSimpleName(),   "Richtlinie allgemeiner Name - gibt als allgemeinen Namen den Nachnamen und Vornamen getrennt durch ein Komma zurück"}

  , {NamePolicyError.USERNAME_FAILURE,              "Beim Generieren des Benutzernamens ist ein Fehler aufgetreten. Geben Sie %1$s wie von %2$s Generator erwartet wird ein." }
  , {NamePolicyError.COMMONNAME_FAILURE,            "Beim Generieren des allgeminen Namens ist ein Fehler aufgetreten. Geben Sie %1$s wie von %2$s Generator erwartet wird ein." }

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