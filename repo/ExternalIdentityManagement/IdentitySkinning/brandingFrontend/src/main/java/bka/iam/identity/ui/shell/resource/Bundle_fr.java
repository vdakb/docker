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
    Subsystem   :   Branding Customization

    File        :   Bundle_fr.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Bundle_fr.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet     First release version
*/

package bka.iam.identity.ui.shell.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import bka.iam.identity.ui.BrandingError;

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
     // BB-00001 - 00010 system related errors
    { BrandingError.GENERAL,                         "Erreur générale: %1$s" }
  , { BrandingError.UNHANDLED,                       "Une exception non gérée s'est produite: %1$s"}
  , { BrandingError.ABORT,                           "Exécution interrompue pour la raison : %1$s"}
  , { BrandingError.NOTIMPLEMENTED,                  "La fonctionnalité n'est pas encore implémentte"}

     // BB-00011 - 00020 method argument related errors
  , { BrandingError.ARGUMENT_IS_NULL,                "L'argument transmis %1$s ne doit pas être nul" }
  , { BrandingError.ARGUMENT_BAD_TYPE,               "L'argument passé %1$s a un mauvais type" }
  , { BrandingError.ARGUMENT_BAD_VALUE,              "L'argument passé %1$s contient une valeur non valide" }
  , { BrandingError.ARGUMENT_SIZE_MISMATCH,          "La taille du tableau d'arguments transmis ne correspond pas à la longueur attendue" }

     // BB-00021 - 00030 instance attribute related errors
  , { BrandingError.ATTRIBUTE_IS_NULL,               "L'état de l'attribut %1$s ne doit pas être nul" }

     // BB-00032 - 00040 file related errors
  , { BrandingError.FILE_MISSING,                    "Problèmes rencontrés pour trouver le fichier %1$s" }
  , { BrandingError.FILE_IS_NOT_A_FILE,              "%1$s n'est pas un fichier" }
  , { BrandingError.FILE_OPEN,                       "Problèmes rencontrés pour ouvrir le fichier %1$s" }
  , { BrandingError.FILE_CLOSE,                      "Problèmes rencontrés pour fermer le fichier %1$s" }
  , { BrandingError.FILE_READ,                       "Problèmes rencontrés lors de la lecture du fichier %1$s" }
  , { BrandingError.FILE_WRITE,                      "Problèmes rencontrés lors de l'écriture du fichier %1$s" }

    // BB-00041 - 00050 file related errors
  , { BrandingError.BRANDING_CONFIGURATION_PROPERTY, "Impossible de configurer le modèle de demande car la propriété système %1$s est manquante." }
  , { BrandingError.BRANDING_CONFIGURATION_STREAM,   "Impossible de configurer le modèle de demande en raison de l'erreur de flux %1$s.\" }" }
  , { BrandingError.BRANDING_CONFIGURATION_PARSING,  "Impossible de configurer le modèle de demande en raison d'une erreur de l'analyseur %1$s." }
  , { BrandingError.BRANDING_NOT_DEFINED,            "Le composant %l$s n'est pas défini dans le model."}
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