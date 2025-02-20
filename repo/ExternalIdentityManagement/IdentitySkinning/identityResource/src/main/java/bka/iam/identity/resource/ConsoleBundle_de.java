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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Federated Identity Management
    Subsystem   :   Federal Criminal Police Office Frontend Customizations

    File        :   ConsoleBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConsoleBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package bka.iam.identity.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ConsoleBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~
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
public class ConsoleBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    /** Constant used to demarcate catalog entity related messages. */
    {Console.USR_ANONYMIZED_LABEL,   "Anonymisierte Anmeldung"}
  , {Console.USR_ANONYMIZED_HINT,    "Der Anmeldename, der von dieser Identität verwendet wird, um die tatsächliche Identität bei ausgehender Kommunikation zu verschleiern."}
  , {Console.USR_UNIFIED_LABEL,      "Vereinheitlichte Anmeldung"}
  , {Console.USR_UNIFIED_HINT,       "Der Anmeldename, der von dieser Identität verwendet wird, um durch P20 Applikationen und Dienste authentiisert und authorisiert zu werden."}
  , {Console.USR_PARTICIPANT_TITLE,  "Teilnehmer"}
  , {Console.USR_PARTICIPANT_LABEL,  "Teilnehmer"}
  , {Console.USR_PARTICIPANT_HINT,   "Teilnehmer"}
  , {Console.USR_ORGANIZATION_LABEL, "Organisationseinheit"}
  , {Console.USR_ORGANIZATION_HINT,  "Organisationseinheit"}
  , {Console.USR_DIVISION_LABEL,     "Bereich"}
  , {Console.USR_DIVISION_HINT,      "Präsidium oder Behörde"}
  , {Console.USR_DEPARTMENT_LABEL,   "Abteilung"}
  , {Console.USR_DEPARTMENT_HINT,    "Abteilung"}
  , {Console.USR_GENERATED_LABEL,    "Generierter Identifier"}
  , {Console.USR_GENERATED_HINT,     "Dieser Name wird später verwendet, um den Benutzer während der Authentifizierungszu identifizieren.\nEin Prinzipal Name ist nicht mit einer E-Mail-Adresse identisch.\nManchmal kann ein Prinzipal Name mit der E-Mail-Adresse eines Benutzers übereinstimmen, dies ist jedoch keine allgemeine Regel."}
  , {Console.USR_GENERATION_LABEL,   "Prinzipal Name"}
  , {Console.USR_GENERATION_HINT,    "Kennung, die durch die Datenquelle generiert wurde, aus der die Identität bezogen wurde."}
  , {Console.USR_JOBROLE_LABEL,      "Job Rolle"}
  , {Console.USR_JOBROLE_HINT,       "Die Rolle/Der Rang, den die Identität in der Organisationsstruktur hat."}
  , {Console.USR_QUALIFIED_LABEL,    "Qualifizierter Identifier"}
  , {Console.USR_QUALIFIED_HINT,     "Qualifizierter Identifier"}

    /** Constant used to demarcate authorization entity related messages. */
  , {Console.ACC_BADGE_TITLE,        "Autorisierung" }
  , {Console.ACC_BADGE_HINT,         "Sehen Sie, wem Benutzerkonten und Berechtigungen gewährt wurden." }
  , {Console.ACC_APPLICATION_MENU,   "Anwendungsinstanzen" }
  , {Console.ACC_ENTITLEMENT_MENU,   "Berechtigungen" }

    /** Constant used to demarcate catalog entity related messages. */
  , {Console.CAT_BADGE_TITLE,        "Katalog" }
  , {Console.CAT_BADGE_HINT,         "Katalog Einträge bearbeiten" }

    /** Constant used to demarcate eFBS entity related messages. */
  , {Console.FBS_TITLE,              "Anforderung eFBS" }
  , {Console.FBS_ACTION_LABEL,       "Anforderung eFBS" }
  , {Console.FBS_ACTION_HINT,        "Anforderung spezieller Benutzerkonten für das Fachverfahren eFBS." }

  /** Constant used to demarcate access policy evaluation related messages. */
  , {Console.EVAL_ACTION_LABEL,       "Auswerten" }
  , {Console.EVAL_ACTION_HINT,        "Sofortige Auswertung der Zugriffsrichtlinien für den Benutzer." }
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