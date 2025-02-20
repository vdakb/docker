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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   EndpointBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    EndpointBundle_fr.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.api;

import java.util.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class EndpointBundle_fr
// ~~~~~ ~~~~~~~~~~~~~~~~~
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
public class EndpointBundle_fr extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // UID-00011 - 00020 method argument related errors
    { EndpointError.ARGUMENT_IS_NULL,              "L'argument passé [%1$s] ne peut pas être nul." }
  , { EndpointError.ARGUMENT_BAD_TYPE,             "L'argument passé [%1$s] est du mauvais type." }
  , { EndpointError.ARGUMENT_BAD_VALUE,            "L'argument transmis [%1$s] contient une valeur non valide." }
  , { EndpointError.ARGUMENT_LENGTH_MISMATCH,      "La longueur de la valeur transmise pour l'argument [%1$s] ne correspond pas à la longueur attendue." }
  , { EndpointError.ARGUMENT_SIZE_MISMATCH,        "La taille du tableau pour l'argument passé [%1$s] ne correspond pas à la taille attendue." }

     // UID-00021 - 00030 method invokation related errors
  , { EndpointError.METHOD_NOT_PERMITTED,          "Vous obtenez cette erreur car vous avez appelé une méthode qui n'est pas autorisée pour ce point de terminaison." }

     // UID-00031 - 00040 operation related errors
  , { EndpointError.OPERATION_NOT_PERMITTED,       "Vous obtenez cette erreur car vous avez appelé une opération qui n'est pas autorisée pour l'état actuel de l'entité." }

     // 00041 - 00050 tenant related errors
  , { EndpointError.TENANT_LOOKUP_NOT_PERMITTED,   "Vous n'êtes pas autorisé à rechercher des identifiants uniques dans le locataire [%1$s]." }
  , { EndpointError.TENANT_LOOKUP_NOT_PERMITTED,   "Vous n'êtes pas autorisé à rechercher un identifiant unique dans le locataire [%1$s]." }
  , { EndpointError.TENANT_MODIFY_NOT_PERMITTED,   "Vous n'êtes pas autorisé à modifier l'identifiant unique du locataire [%1$s]." }
  , { EndpointError.TENANT_GENERATE_NOT_PERMITTED, "Vous n'êtes pas autorisé à générer un identifiant unique dans le locataire [%1$s]." }
  , { EndpointError.TENANT_REGISTER_NOT_PERMITTED, "Vous n'êtes pas autorisé à enregistrer un identifiant unique dans le locataire [%1$s]." }
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