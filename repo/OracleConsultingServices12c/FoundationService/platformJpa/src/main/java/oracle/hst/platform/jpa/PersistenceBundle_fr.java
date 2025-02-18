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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Common Shared Utility

    File        :   PersistenceBundle_fr.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PersistenceBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class PersistenceBundle_fr
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class PersistenceBundle_fr extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // JPA-00001 - 00010 system related errors
    { PersistenceError.UNHANDLED,       "Une exception non gérée s'est produite"}
  , { PersistenceError.GENERAL,         "Erreur générale" }
  , { PersistenceError.ABORT,           "Exécution interrompue"}

    // JPA-00011 - 00020 operational errors
  , { PersistenceError.EXISTS,          "L'entité [%1$s] avec l'identifiant [%2$s] existe déjà !"}
  , { PersistenceError.NOT_EXISTS,      "L'entité [%1$s] avec l'identifiant [%2$s] n'existe pas !"}
  , { PersistenceError.AMBIGUOUS,       "L'entité [%1$s] avec l'identifiant [%2$s] est définie de manière ambiguë !"}
  , { PersistenceError.NOT_CREATABLE,   "L'opération de création n'est pas autorisée pour l'entité [%1$s] !"}
  , { PersistenceError.NOT_CREATED,     "L'opération de création a échoué pour l'entité [%1$s] !"}
  , { PersistenceError.NOT_MODIFIABLE,  "L'opération de modification n'est pas autorisée pour l'entité [%1$s] !"}
  , { PersistenceError.NOT_MODIFIED,    "Échec de l'opération de modification pour l'entité [%1$s] !"}
  , { PersistenceError.NOT_DELETABLE,   "L'opération de suppression n'est pas autorisée pour l'entité [%1$s] !"}
  , { PersistenceError.NOT_DELETED,     "L'opération de suppression a échoué pour l'entité [%1$s] !"}

    // JPA-00021 - 00030 attribute validation errors
  , { PersistenceError.NOT_NULL,        "L'attribut [%2$s] ne doit pas être nul ou vide pour l'entité [%1$s] !"}

    // JPA-00031 - 00040 search criteria errors
  , { PersistenceError.CRITERIA_SPEC,   "La condition de requête ne répond pas à la spécification !"}
  , { PersistenceError.CRITERIA_NESTED, "Vous ne pouvez pas imbriquer [%2$s] dans [%1$s] !"}
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