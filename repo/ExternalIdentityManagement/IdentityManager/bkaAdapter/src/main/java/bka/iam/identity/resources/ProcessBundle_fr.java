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
    Subsystem   :   Common Shared Plugin

    File        :   ProcessBundle_fr.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ProcessBundle_fr.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  DSteding    First release version
*/

package bka.iam.identity.resources;

import bka.iam.identity.ProcessError;
import bka.iam.identity.ProcessMessage;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class ProcessBundle_fr
// ~~~~~ ~~~~~~~~~~~~~~~~
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
public class ProcessBundle_fr extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    // PRC-00001 - 00010 configuration related errors
    {ProcessError.PROPERTY_NOTFOUND,                "La propriété système [%1$s] n'existe pas. Veuillez fournir [%1$s] comme prévu." }
  , {ProcessError.PROPERTY_INVALID,                 "La propriété système [%1$s] n'est pas configurée correctement. Veuillez fournir [%1$s] comme prévu." }

    // PRC-00011 - 00020 notification related errors
  , {ProcessError.NOTIFICATION_FAILED,              "L'envoi de la notification n'a pas réussi. Une exception d'événement s'est produite." }
  , {ProcessError.NOTIFICATION_EXCEPTION,           "L'envoi de la notification n'a pas réussi. Une exception de notification s'est produite." }
  , {ProcessError.NOTIFICATION_UNRESOLVED_DATA,     "L'envoi de la notification n'a pas réussi. Données de notification non résolues." }
  , {ProcessError.NOTIFICATION_TEMPLATE_NOTFOUND,   "L'envoi de la notification n'a pas réussi. Modèle de notification introuvable." }
  , {ProcessError.NOTIFICATION_TEMPLATE_AMBIGOUS,   "L'envoi de la notification n'a pas réussi. Une exception de modèle multiple s'est produite." }
  , {ProcessError.NOTIFICATION_RESOLVER_NOTFOUND,   "L'envoi de la notification n'a pas réussi. Résolveur de notifications introuvable." }
  , {ProcessError.NOTIFICATION_IDENTITY_NOTFOUND,   "L'envoi de la notification n'a pas réussi. Détails d'identité introuvables." }
  , {ProcessError.NOTIFICATION_RECIPIENT_EMPTY,     "L'envoi de la notification n'a pas réussi. La liste des destinataires est vide." }

    // PRC-00021 - 00030 housekeeping related errors
  , {ProcessError.HOUSEKEEPING_CONNECTOR_NOTFOUND,  "Bundle de connecteurs pour la ressource informatique [%1$s] introuvable." }
  , {ProcessError.HOUSEKEEPING_DESCRIPTOR_NOTFOUND, "Les informations supplémentaires pour [%1$s] n'existent pas dans le magasin de métadonnées." }
    
    // PRC-00031 - 00040 Directory Synchronization related errors
  , {ProcessError.SEARCH_ENTRY_FAILED,              "Une erreur est survenu en cherchant [%1$s]. Raison: [%2$s]" }
  , {ProcessError.UPDATE_ENTRY_FAILED,              "Erreur en mettant à jour l'entré. Raison: [%1$s] " }

    // PRC-01001 - 01010 access policy related messages
  , {ProcessMessage.POLICY_ENTITLEMENT_NOTEXIST,    "Le droit suivant est supprimé dans la stratégie d'accès : [%1$s]#[%2$s].Raison : n'est plus disponible dans [%3$s]." }
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