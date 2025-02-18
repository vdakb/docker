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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Database Connector

    File        :   DatabaseBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.dbms.DatabaseError;
import oracle.iam.identity.icf.dbms.DatabaseMessage;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseBundle_fr
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
public class DatabaseBundle_fr extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // DBS-00051 - 00060 schema discovery errors
    { DatabaseError.SCHEMA_DESCRIPTOR_ERROR,     "L'opération du descripteur de schéma a échoué: [%1$s]." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_EMPTY,     "Le descripteur de schéma est vide." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_NOTFOUND,  "Le descripteur de schéma n'a pas pu être localisé." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_INVALID,   "Le descripteur de schéma n'est pas valide." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_PARSE,     "Une erreur s'est produite lors de l'analyse du fichier de schéma: \" %1$s\"" }
  , { DatabaseError.SCHEMA_DESCRIPTOR_PRIMARY,   "Identifiant système non défini pour l'entité [%1$s]." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_SECONDARY, "Identifiant unique non défini pour l'entité [%1$s]." }

     // DBS-00061 - 00070 statment errors
  , { DatabaseError.CONNECTION_NOT_CONNECTED,    "Not connected." }
  , { DatabaseError.CONNECTION_CLOSED,           "Connection closed." }
  , { DatabaseError.STATEMENT_CLOSED,            "Statement closed." }
  , { DatabaseError.STATEMENT_TIMEOUT,           "Statement timedout." }

     // DBS-00071 - 00080 access operational errors
  , { DatabaseError.SYNTAX_INVALID,              "Il y a une erreur dans la syntaxe SQL: [%1$s]." }
  , { DatabaseError.OPERATION_FAILED,            "Opération signalée comme ayant échoué par le service de base de données." }
  , { DatabaseError.OPERATION_NOT_SUPPORTED,     "Opération non prise en charge par le service de base de données." }
  , { DatabaseError.INSUFFICIENT_PRIVILEGE,      "Le principal [%1$s] a des privilèges insuffisants pour effectuer l'opération [%2$s]." }
  , { DatabaseError.INSUFFICIENT_INFORMATION,    "Les informations de champ obligatoires ne sont pas fournies pour [%1$s]." }
  , { DatabaseError.SEARCH_CONDITION_FAILED,     "Échec de la création de la condition de recherche [%1$s]." }

     // DBS-00081 - 00090 regular expresssion errors
  , { DatabaseError.EXPRESSION_BITVALUES,        "Les valeurs de bits non définies sont définies dans les options de compilation des expressions régulières." }
  , { DatabaseError.EXPRESSION_INVALID,          "Une erreur est contenue dans l'expression régulière [%1$s]: [%2$s]." }

     // DBS-00091 - 00100 path expresssion errors
  , { DatabaseError.PATH_UNEXPECTED_EOS,         "Chaîne de fin de chemin inattendue." }
  , { DatabaseError.PATH_UNEXPECTED_CHARACTER,   "Caractère inattendu [%1$s] à la position \"%2%d\" pour le jeton commençant à \"%3%d\"." }
  , { DatabaseError.PATH_EXPECT_ATTRIBUTE_PATH,  "Chemin d'attribut attendu à la position \"%1%d\"." }
  , { DatabaseError.PATH_EXPECT_ATTRIBUTE_NAME,  "Nom d'attribut attendu à la position \"%1%d\"." }

     // DBS-00101 - 00120 object operational errors
  , { DatabaseError.OBJECT_NOT_CREATED,          "L'entrée n'a pas été créée pour [%1$s] dans [%2$s]." }
  , { DatabaseError.OBJECT_NOT_MODIFIED,         "L'entrée n'a pas été modifiée pour [%1$s] dans [%2$s]." }
  , { DatabaseError.OBJECT_NOT_DELETED,          "L'entrée n'a pas été supprimée pour [%1$s] dans [%2$s]." }
  , { DatabaseError.OBJECT_ALREADY_EXISTS,       "L'entrée existe déjà pour [%1$s] dans [%2$s]." }
  , { DatabaseError.OBJECT_NOT_EXISTS,           "L'entrée n'existe pas pour [%1$s] dans [%2$s]." }
  , { DatabaseError.OBJECT_AMBIGUOUS,            "L'entrée est définie de manière ambiguë pour [%1$s] dans [%2$s]." }
  , { DatabaseError.PARENT_NOT_EXISTS,           "L'entrée supérieure [%1$s] n'existe pas pour [%1$s] dans [%2$s]." }
  , { DatabaseError.PARENT_AMBIGUOUS,            "L'entrée supérieure [%1$s] est définie de manière ambiguë pour [%1$s] dans [%2$s]." }
  , { DatabaseError.PERMISSION_NOT_ASSIGNED,     "Impossible d'attribuer l'autorisation pour [%1$s] à [%2$s]." }
  , { DatabaseError.PERMISSION_NOT_REMOVED,      "Impossible de supprimer l'autorisation pour [%1$s] de [%2$s]." }
  , { DatabaseError.PERMISSION_ALREADY_ASSIGNED, "Autorisation déjà attribuée à [%1$s] dans [%2$s]." }
  , { DatabaseError.PERMISSION_ALREADY_REMOVED,  "Autorisation déjà supprimée de [%1$s] dans [%2$s]." }

     // DBS-01001 - 01010 system related messages
  , { DatabaseMessage.CONNECTING_BEGIN,          "Connexion à l'URL de service [%1$s] avec l'utilisateur [%2$s] ..." }
  , { DatabaseMessage.CONNECTING_SUCCESS,        "Connexion à l'URL de service [%1$s] pour l'utilisateur [%2$s] établie." }

     // DBS-01011 - 01020 system related messages
  , { DatabaseMessage.CONNECTION_ALIVE,          "Connexion disponible" }

  , { DatabaseMessage.EXECUTE_STATEMENT,         "En cours d'exécution : %1$s" }
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