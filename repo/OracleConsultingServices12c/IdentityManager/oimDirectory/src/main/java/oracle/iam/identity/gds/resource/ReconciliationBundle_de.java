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

    File        :   ReconciliationBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ReconciliationBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.gds.service.reconciliation.ReconciliationError;
import oracle.iam.identity.gds.service.reconciliation.ReconciliationMessage;

////////////////////////////////////////////////////////////////////////////////
// class ReconciliationBundle_de
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
 ** @since   0.0.0.2
 */
public class ReconciliationBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GDS-00061 - 00070 mapping related errors
    { ReconciliationError.ATTRIBUTES_NOT_MAPPED,      "Kein Attribute des Descriptor [%1$s] konnte mit den Abgleichsfeldern des Profiles des Resource Object [%2$s] in übereinstimmung gebracht werden" }
  , { ReconciliationError.IDENTIFIER_NOT_MAPPED,      "Eindeutiger Attribute Identifier [%2$s] ist im Attribute Mapping des Deskriptors [%1$s] nicht vorhanden" }

    // GDS-01081 - 01090 lookup reconciliation related messages
  , { ReconciliationMessage.CREATE_VALUE,             "Neuer Wert [%2$s] konnte der Lookup Definition [%1$s] nicht hinzugefügt werden." }
  , { ReconciliationMessage.DUPLICATE_VALUE,          "Neuer Wert [%2$s] ist mit dem existierenden Wert identisch in Lookup Definition [%1$s]." }

    // GDS-01091 - 01100 reconciliation process related messages
  , { ReconciliationMessage.SEARCH_CRITERIA,          "Ausführung %2$s Suche in Search Base [%1$s] mit Suchfilter [%3$s]" }
  , { ReconciliationMessage.SEARCH_CRITERIA_SORTED,   "Ausführung %2$s Suche in Search Base [%1$s] mit Suchfilter [%3$s] und Sortierung nach [%4$s]" }
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