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
    Subsystem   :   CSV Flatfile Connector

    File        :   ControllerBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ControllerBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.csv.service.ControllerMessage;

////////////////////////////////////////////////////////////////////////////////
// class ControllerBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
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
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class ControllerBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // CSV-00021 - 00030 file handling related messages
    { ControllerMessage.CREATING_URL,            "Erzeuge URL für %1$s" }
  , { ControllerMessage.CREATING_FOLDER,         "Erzeuge Dateihandle für %1$s" }
  , { ControllerMessage.CREATING_FILE,           "Erzeuge Dateihandle für %1$s" }

     // CSV-00031 - 00040 provisioning process related messages
  , { ControllerMessage.COMMENT_START,           " --- Provisionierung von %1$s gestartet %2$s ---" }
  , { ControllerMessage.COMMENT_END,             " --- Provisionierung von %1$s beendet %2$s ---" }
  , { ControllerMessage.STATEMENT,               "Ausführung um %1$s Abfrage %2$s" }

     // CSV-01081 - 01090 lookup reconciliation related messages
  , { ControllerMessage.CREATE_VALUE,            "Neuer Wert [%2$s] konnte der Lookup Definition [%1$s] nicht hinzugefügt werden." }
  , { ControllerMessage.REMOVE_VALUE,            "Alter Wert [%2$s] konnte aus Lookup Definition [%1$s] nicht entfernt werden." }
  , { ControllerMessage.DUPLICATE_VALUE,         "Neuer Wert [%2$s] ist mit dem existierenden Wert identisch in Lookup Definition [%1$s]." }

     // CSV-01091 - 01100 reconciliation process related messages
  , { ControllerMessage.RECONCILE_RESOLVABLES,   "Verarbeite verbliebene zuweisbare Einträge for %1$s" }
  , { ControllerMessage.RECONCILE_UNRESOLVABLES, "Verarbeite verbliebene bisher nicht zuweisbare Einträge for %1$s" }
  , { ControllerMessage.RECONCILE_REMAININGROOT, "Process remaining entries for %1$s" }
  , { ControllerMessage.RECONCILE_REMAININGNODE, "Process depended entry %1$s" }
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