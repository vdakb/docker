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

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AssertionBundle_fr.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml;

import java.util.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AssertionBundle_fr
// ~~~~~ ~~~~~~~~~~~~~~~~~~
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
public class AssertionBundle_fr extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OAM-00001 - 00010 operations related errors
    { AssertionError.UNHANDLED,                       "Une exception non gérée s'est produite: %1$s" }
  , { AssertionError.GENERAL,                         "Rencontrer des problèmes: %1$s" }
  , { AssertionError.ABORT,                           "Exécution interrompue: %1$s" }
  , { AssertionError.NOTIMPLEMENTED,                  "La fonctionnalité n'est pas encore implémentée." }

     // OAM-00011 - 00020 method argument related errors
  , { AssertionError.ARGUMENT_IS_NULL,                "L'argument passé %1$s ne doit pas être nul !" }
  , { AssertionError.ARGUMENT_BAD_TYPE,               "L'argument transmis %1$s a un type incorrect !" }
  , { AssertionError.ARGUMENT_BAD_VALUE,              "L'argument transmis %1$s contient une valeur non valide !" }
  , { AssertionError.ARGUMENT_SIZE_MISMATCH,          "La taille du tableau d'arguments transmis ne correspond pas à la longueur attendue !" }

     // OAM-00021 - 00030 XML parser related errors
  , { AssertionError.PARSER_FATAL,                    "Fatale: [%1$s]" }
  , { AssertionError.PARSER_ERROR,                    "Erreur: [%1$s]" }
  , { AssertionError.PARSER_WARNING,                  "Attention: [%1$s]" }
  , { AssertionError.PARSER_FEATURE,                  "Fonctionnalité d'analyseur XML non prise en charge [%1$s] !" }

     // OAM-00031 - 00040 signature related errors
  , { AssertionError.SIGNATURE_MISSING,               "Aucune signature XML trouvée, validation rejetée !" }
  , { AssertionError.SIGNATURE_CORE_FAILED,           "La signature a échoué à la validation principale !" }

     // OAM-00041 - 00050 token validation related errors
  , { AssertionError.TOKEN_ISSUED,                    "Le jeton a été émis pour l'avenir [%1$s] !" }
  , { AssertionError.TOKEN_NOTAFTER,                  "Le jeton est expiré !" }
  , { AssertionError.TOKEN_NOTBEFORE,                 "Le jeton enfreint la règle du pas avant !" }

     // OAM-01001 - 01010 XML parser related messages
  , { AssertionMessage.PARSER_ENTITY_RESOLVED,        "ResolveEntity query: systemId=[%1$s] publicId=[%2$s] return=[%3$s]." }

     // OAM-01011 - 01020 signature related messages
  , { AssertionMessage.SIGNATURE_CORE_PASSED,         "La signature a passé la validation de base." }
  , { AssertionMessage.SIGNATURE_CORE_STATUS,         "Statut de validation de signature: %1$s." }
  , { AssertionMessage.SIGNATURE_STATUS_VALID,        "valide" }
  , { AssertionMessage.SIGNATURE_STATUS_INVALID,      "invalide" }
  , { AssertionMessage.SIGNATURE_STATUS_VALIDATED,    "validé" }
  , { AssertionMessage.SIGNATURE_STATUS_NOTVALIDATED, "pas validé" }
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