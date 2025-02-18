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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Special Account Request

    File        :   Bundle_fr.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Bundle_fr.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet     First release version
*/

package bka.iam.identity.ui.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import bka.iam.identity.ui.RequestError;
import bka.iam.identity.ui.RequestMessage;

////////////////////////////////////////////////////////////////////////////////
// class Bundle_fr
// ~~~~~ ~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code french
 **   <li>region   code common
 ** </ul>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bundle_fr extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // FIM-00001 - 00010 system related errors
    { RequestError.GENERAL,                        "Erreur générale: %1$s" }
  , { RequestError.UNHANDLED,                      "Une exception non gérée s'est produite: %1$s"}
  , { RequestError.ABORT,                          "Exécution interrompue pour la raison: %1$s"}
  , { RequestError.NOTIMPLEMENTED,                 "La fonctionnalité n'est pas encore implémentée."}

     // FIM-00011 - 00020 method argument related errors
  , { RequestError.ARGUMENT_IS_NULL,               "L'argument transmis %1$s ne doit pas être nul." }
  , { RequestError.ARGUMENT_BAD_TYPE,              "L'argument transmis %1$s a un type incorrect." }
  , { RequestError.ARGUMENT_BAD_VALUE,             "L'argument transmis %1$s contient une valeur non valide." }
  , { RequestError.ARGUMENT_SIZE_MISMATCH,         "La taille du tableau des arguments transmis ne correspond pas à la longueur attendue." }

     // FIM-00021 - 00030 instance attribute related errors
  , { RequestError.ATTRIBUTE_IS_NULL,              "L'état de l'attribut %1$s ne doit pas être nul." }

     // FIM-00032 - 00040 file related errors
  , { RequestError.FILE_MISSING,                   "Problèmes rencontrés pour trouver le fichier %1$s." }
  , { RequestError.FILE_IS_NOT_A_FILE,             "%1$s n'est pas un fichier." }
  , { RequestError.FILE_OPEN,                      "Problèmes rencontrés pour ouvrir le fichier %1$s." }
  , { RequestError.FILE_CLOSE,                     "Problèmes rencontrés pour fermer le fichier %1$s." }
  , { RequestError.FILE_READ,                      "Problèmes rencontrés lors de la lecture du fichier %1$s." }
  , { RequestError.FILE_WRITE,                     "Problèmes rencontrés lors de l'écriture du fichier %1$s." }

     // FIM-00041 - 00050 request related errors
  , { RequestError.REQUEST_CONFIGURATION_PROPERTY, "Impossible de configurer le modèle de demande car la propriété système %1$s est manquante." }
  , { RequestError.REQUEST_CONFIGURATION_STREAM,   "Impossible de configurer le modèle de demande en raison de l'erreur de flux %1$s." }
  , { RequestError.REQUEST_CONFIGURATION_PARSING,  "Impossible de configurer le modèle de demande en raison de l'erreur d'analyseur %1$s." }
  , { RequestError.REQUEST_APPLICATION_LOOKUP,     "<html><body>Échec de la recherche de l'application <b>%1$s</b>.</body></html>" }
  , { RequestError.REQUEST_APPLICATION_NOTFOUND,   "<html><body>Application <b>%1$s</b> introuvable.</body></html>" }
  , { RequestError.REQUEST_ENTITLEMENT_NOTFOUND,   "<html><body>Droit <b>%1$s</b> introuvable.</body></html>" }
  , { RequestError.REQUEST_ENTITLEMENT_AMBIGUOUS,  "<html><body>Droit <b>%1$s</b> défini de manière ambiguë.</body></html>" }
  , { RequestError.REQUEST_SELECTION_VIOLATED,     "<html><body>Veuillez sélectionner un modèle de compte dans l'environnement <b>%1$s</b>.</body></html>" }
  , { RequestError.REQUEST_PREDECESSOR_VIOLATED,   "<html><body>Veuillez demander un compte <b>normal</b> dans l'environnement <b>%1$s</b> pour %2$s<br/> avant de demander un compte en tant que <b>%3$s</b> dans l'environnement.</body></html>" }
  , { RequestError.REQUEST_FAILED,                 "<html><body>L'envoi de la demande de <b>%2$s</b> dans <b>%1$s</b> pour l'utilisateur %3$s a échoué en raison d'une erreur<br/><br/>%4$s<br/>.</body></html>" }

     // FIM-01041 - 01050 request related messages
  , { RequestMessage.REQUEST_SUBMIT_SUCCESS,       "La demande %1$s a été soumise pour approbation." }
  , { RequestMessage.REQUEST_COMPLETE_SUCCESS,     "Demande d'accès terminée avec succès." }

  // EVAL-00051 - 00060 evaluation related errors
  , { RequestError.EVALUATION_INTERNAL_ERROR,      "L'évaluation de la politique d'accés a échoué en raison d'une erreur interne." }
  , { RequestError.EVALUATION_UNKNOWN_ERROR,       "L'évaluation de la politique d'accés a produit un résultat inconnu. Processus: %1$s." }

  // EVAL-01051 - 01060 evaluation related messages
  , { RequestMessage.EVALUATION_SUCCEEDED,         "L'évaluation de la politique d'accès a été réalisée avec succés." }
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