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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Zero Provisioning

    File        :   ZeroBundle_fr.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    ZeroBundle_fr.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      14.08.2023  SBernet     First release version
*/
package bka.iam.identity.zero.resources;

import bka.iam.identity.zero.ZeroError;
import bka.iam.identity.zero.ZeroMessage;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class ZeroBundle
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code french
 **   <li>language code common
 ** </ul>
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ZeroBundle_fr extends ListResourceBundle {

  private static final String[][] CONTENT = {
  
    // ZRO-00001 - 00010 configuration related errors
    { ZeroError.APPLICATION_NOTFOUND,            "Application %1$s non trouvé." }
  , { ZeroError.ORGANIZATION_NOTFOUND,           "Organization %1$s non trouvé." }
  , { ZeroError.PROPERTY_NOTFOUND,               "La propriété système \"%1$s\" n'existe pas. Veuillez fournir \"%1$s\" comme prévu." }
  , { ZeroError.PROPERTY_INVALID,                "La propriété système \"%1$s\" est mal configuré. Veuillez fournir \"%1$s\" comme prévu." }
    // ZRO-00011 - 00020 ldap related errors
  , { ZeroError.LDAP_ERROR,                      "Erreur LDAP: %1$s"}
  , { ZeroError.NAMING_DN_ERROR,                 "LDAP DN: %1$s mal formé"}
  , { ZeroError.INDEX_RDN_OUT_RANGE,             "Index RDN: %1$s en dehors de la plage par rapport au DN: %2$s"}
    
    // ZRO-00021 - 00030 notification related errors
  , {ZeroError.NOTIFICATION_FAILED,              "L'envoi de la notification n'a pas réussi. Event Exception occurred." }
  , {ZeroError.NOTIFICATION_EXCEPTION,           "L'envoi de la notification n'a pas réussi. Une exception de notification s'est produite." }
  , {ZeroError.NOTIFICATION_UNRESOLVED_DATA,     "L'envoi de la notification n'a pas réussi. Données de notification non résolues." }
  , {ZeroError.NOTIFICATION_TEMPLATE_NOTFOUND,   "L'envoi de la notification n'a pas réussi. Modèle de notification non trouvé." }
  , {ZeroError.NOTIFICATION_TEMPLATE_AMBIGOUS,   "L'envoi de la notification n'a pas réussi. Une exception sur les modèles de notification s'est produite." }
  , {ZeroError.NOTIFICATION_RESOLVER_NOTFOUND,   "L'envoi de la notification n'a pas réussi. Résolveur de notification introuvable." }
  , {ZeroError.NOTIFICATION_IDENTITY_NOTFOUND,   "L'envoi de la notification n'a pas réussi. Détails de l'identité non trouvée." }
  , {ZeroError.NOTIFICATION_RECIPIENT_EMPTY,     "L'envoi de la notification n'a pas réussi. La liste des destinataires est vide." }

    // ZRO-01001 - 01010  account operation related message
  , { ZeroMessage.REQUEST_NEW_ACCOUNT,            "Nouveau compte demandé avec les valeurs suivantes:\n%1$s"}
  , { ZeroMessage.REQUEST_MODIFY_ACCOUNT,         "Modification d'un compte existant avec les valeurs suivantes:\n%1$s"}
  , { ZeroMessage.NO_ACCOUNT_TO_REQUEST,          "No accounts found on LDAP. No queries will be made on OIM."}
    
    // ZRO-01001 - 01010 ldap related messages
  , { ZeroMessage.REQUEST_NEW_ACCOUNT,            "Requête pour un nouveau compte. ID de la requête:\n%1$s"}
  , { ZeroMessage.REQUEST_MODIFY_ACCOUNT,         "Modification d'un compte existant. ID de la requête:\n%1$s"}
  , { ZeroMessage.LDAP_ATTR_NOT_FOUND,            "Attribut %1$s non trouvé sur l'entrée %2$s"}
    
  // ZRO-00031 - 00040 notification related message
  , { ZeroMessage.NOTIFICATION_RESOLVE_INCOME,    "Collecte des mappages de substitution reçus:\n"}
  , { ZeroMessage.NOTIFICATION_RESOLVE_OUTCOME,   "Collecte des mappages de substitution reçus:\n"}
  , { ZeroMessage.REPORTER_EMPTY,                 "Aucune modification sur le compte détecté sur %1$s. Aucune notification envoyée."}
  , { ZeroMessage.REPORTER_NOT_FOUND,             "Le rapporteur %1$s est introuvable. Aucune notification envoyée."}
 
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