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

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   SystemBundle_fr.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SystemBundle_fr
// ~~~~~ ~~~~~~~~~~~~~~~
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
 */
public class SystemBundle_fr extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OSF-00001 - 00010 system related errors
    { SystemError.UNHANDLED,                       "Une exception non gérée s'est produite: [%1$s]" }
  , { SystemError.GENERAL,                         "Erreur générale: [%1$s]" }
  , { SystemError.ABORT,                           "Exécution interrompue pour cause: [%1$s]" }
  , { SystemError.NOTIMPLEMENTED,                  "La fonctionnalité n'est pas encore implémentée." }
  , { SystemError.CLASSNOTFOUND,                   "La classe %1$s n'a pas été trouvée dans le chemin de classe." }
  , { SystemError.CLASSNOTCREATE,                  "La classe %1$s n'a pas été créée." }
  , { SystemError.CLASSNOACCESS,                   "L'accès à la classe %1$s n'est pas autorisé." }
  , { SystemError.CLASSINVALID,                    "La classe %1$s doit être une sous-classe de %s." }
  , { SystemError.CLASSMETHOD,                     "La classe %1$s n'accepte pas le paramêtre de constructeur %2$s." }
  , { SystemError.CLASSCONSTRUCTORDEFAULT,         "La classe %1$s n'a pas de constructeur sans arguments." }

     // OSF-00011 - 00020 method argument related errors
  , { SystemError.ARGUMENT_IS_NULL,                "L'argument transmis %1$s ne doit pas être nul." }
  , { SystemError.ARGUMENT_BAD_TYPE,               "L'argument transmis %1$s a un type incorrect." }
  , { SystemError.ARGUMENT_BAD_VALUE,              "L'argument transmis %1$s contient une valeur non valide." }
  , { SystemError.ARGUMENT_SIZE_MISMATCH,          "La taille du tableau des arguments transmis ne correspond pas à la longueur attendue." }

     // OSF-00021 - 00030 instance attribute related errors
  , { SystemError.ATTRIBUTE_IS_NULL,               "L'état de l'attribut %1$s ne doit pas être nul." }
  , { SystemError.INSTANCE_STATE,                  "État invalide de l'instance: attribut %1$s déjà initialisé."}

      // OSF-00031 - 00040 system property errors
  , { SystemError.PROPERTY_TYPE_INVALID,           "Impossible de créer une propriété système de type [%1$s]." }
  , { SystemError.PROPERTY_NAME_REQUIRED,          "Le nom de la propriété n'a pas été défini." }
  , { SystemError.PROPERTY_NAME_EXISTS,            "Une propriété existe déjà avec le nom [%1$s]." }
  , { SystemError.PROPERTY_DEFAULT_REQUIRED,       "La valeur par défaut des propriétés n'a pas été définie." }
  , { SystemError.PROPERTY_MINIMUM_VALUE,          "La valeur configurée de [%1$s] est inférieure à la valeur minimale de \"%2$d\" pour la propriété [%3$s] - utilisera la valeur par défaut de [%4$s] à la place." }
  , { SystemError.PROPERTY_MINIMUM_DEFAULT,        "La valeur minimale ne peut pas être supérieure à la valeur par défaut." }
  , { SystemError.PROPERTY_MINIMUM_COMPARABLE,     "Une valeur minimale ne peut être appliquée qu'aux propriétés qui implémentent Comparable." }
  , { SystemError.PROPERTY_MAXIMUM_VALUE,          "La valeur configurée de [%1$s] est supérieure à la valeur maximale de \"%2$d\" pour la propriété [%3$s] - utilisera la valeur par défaut de [%4$s] à la place." }
  , { SystemError.PROPERTY_MAXIMUM_DEFAULT,        "La valeur maximale ne peut pas être inférieure à la valeur par défaut." }
  , { SystemError.PROPERTY_MAXIMUM_COMPARABLE,     "Une valeur maximale ne peut être appliquée qu'aux propriétés qui implémentent Comparable." }

    // OSF-00041 - 00050 reflection/inrospection errors
  , { SystemError.INTROSPECT_EXCEPTION_IGNORE,     "Exception ignorée!\nAu lieu de cela, il renverra [null].\n%1$s" }

     // OSF-00051 - 00060 security context related errors
  , { SystemError.SECURITY_INITIALIZE,             "Erreur lors de l'initialisation de l'usine du gestionnaire de clés (l'opération a échoué)." }
  , { SystemError.SECURITY_UNRECOVERABLE,          "Erreur lors de l'initialisation de l'usine du gestionnaire de clés (clé irrécupérable)." }
  , { SystemError.SECURITY_PROVIDER,               "Erreur lors de l'initialisation du magasin de clés (fournisseur non enregistré)." }
  , { SystemError.SECURITY_ALGORITHM,              "Erreur lors de l'initialisation de l'usine du gestionnaire de clés (algorithme non pris en charge)." }
  , { SystemError.SECURITY_KEYSTORE_PASSWORD,      "Ni le mot de passe de clé ni le mot de passe du magasin de clés n'ont été définis pour le magasin de clés %1$s.\nIgnorer la configuration du magasin de clés et ignorer l'initialisation d'usine du gestionnaire de clés.\nLa fabrique de gestionnaire de clés ne sera pas configurée dans le contexte SSL actuel." }

     // OSF-00061 - 00070 trusted key store related errors
  , { SystemError.TRUSTED_IMPLEMENTATION,          "Erreur lors de l'initialisation de Trusted Store (l'implémentation [%1$s] n'est pas disponible)." }
  , { SystemError.TRUSTED_PROVIDER,                "Erreur lors de l'initialisation de Trusted Store (fournisseur « %1$s\" non enregistré)." }
  , { SystemError.TRUSTED_ALGORITHM,               "Erreur lors de l'initialisation du magasin de confiance (algorithme [%1$s] pour vérifier l'intégrité du magasin de clés introuvable)." }
  , { SystemError.TRUSTED_FILE_NOTFOUND,           "Impossible de trouver le fichier Trusted Store [%1$s]." }
  , { SystemError.TRUSTED_FILE_NOTLOADED,          "Erreur lors du chargement de Trusted Store à partir du fichier [%1$s]." }
  , { SystemError.TRUSTED_CERT_NOTLOADED,          "Impossible de charger les certificats Trusted Store." }

     // OSF-00071 - 00080 identity key store related errors
  , { SystemError.IDENTITY_IMPLEMENTATION,         "Erreur lors de l'initialisation d'Identity Store (implémentation [%1$s] non disponible)." }
  , { SystemError.IDENTITY_PROVIDER,               "Erreur lors de l'initialisation d'Identity Store (fournisseur [%1$s] non enregistré)." }
  , { SystemError.IDENTITY_ALGORITHM,              "Erreur lors de l'initialisation du magasin d'identités (algorithme [%1$s] pour vérifier l'intégrité du magasin de clés introuvable)." }
  , { SystemError.IDENTITY_FILE_NOTFOUND,          "Impossible de trouver le fichier Identity Store [%1$s]." }
  , { SystemError.IDENTITY_FILE_NOTLOADED,         "Erreur lors du chargement d'Identity Store à partir du fichier [%1$s]." }
  , { SystemError.IDENTITY_CERT_NOTLOADED,         "Impossible de charger les certificats du magasin d'identitéss." }

     // OSF-00081 - 00090 configuration related errors
  , { SystemError.PROPERTY_REQUIRED,               "La propriété de configuration[%1$s] est requise." }

     // OSF-00091 - 00090 connectivity errors
  , { SystemError.CONNECTION_UNKNOWN_HOST,         "L'hôte vers le serveur du fournisseur de services [%1$s] est inconnu." }
  , { SystemError.CONNECTION_CREATE_SOCKET,        "Pourrait créer une connexion réseau pour héberger [%1$s] sur le port [%2$s]." }
  , { SystemError.CONNECTION_SECURE_SOCKET,        "Pourrait créer une connexion réseau sécurisée pour héberger [%1$s] sur le port [%2$s]." }
  , { SystemError.CONNECTION_CERTIFICATE_PATH,     "Impossible de trouver un chemin de certification valide vers la cible demandée [%1$s]." }
  , { SystemError.CONNECTION_ERROR,                "Erreur rencontrée lors de la connexion au fournisseur de services." }
  , { SystemError.CONNECTION_TIMEOUT,              "La connexion au fournisseur de services a expiré; [%1$s]." }
  , { SystemError.CONNECTION_UNAVAILABLE,          "Le problème peut être lié à la connectivité physique ou le fournisseur de services n'est pas actif." }
  , { SystemError.CONNECTION_AUTHENTICATION,       "Le nom principal [%1$s] ou le mot de passe est incorrect, le système n'a pas pu accéder aux informations d'identification fournies." }
  , { SystemError.CONNECTION_AUTHORIZATION,        "Le nom principal [%1$s] n'est pas autorisé." }
  , { SystemError.CONNECTION_ENCODING_UNSUPPORTED, "Encodage URL [%1$s] non pris en charge." }

     // OSF-00101 - 00110 path parsing errors
  , { SystemError.PATH_UNEXPECTED_EOF_STRING,      "Chaîne de fin de chemin inattendue." }
  , { SystemError.PATH_UNEXPECTED_EOF_FILTER,      "Fin de chaîne de filtre inattendue." }
  , { SystemError.PATH_UNEXPECTED_CHARACTER,       "Caractère '%s' inattendu à la position %d pour les jetons commençant à %d." }
  , { SystemError.PATH_UNRECOGNOIZED_OPERATOR,     "Opérateur d'attribut inconnu '%s' à la position %d. Attendu : eq,ne,co,sw,ew,pr,gt,ge,lt,le !" }
  , { SystemError.PATH_INVALID_FILTER,             "Filtre de valeur non valide : %s." }
  , { SystemError.PATH_EXPECT_PARENTHESIS,         "'(' attendu à la position %d." }
  , { SystemError.PATH_INVALID_PARENTHESIS,        "Aucune parenthèse gauche ne correspond à la parenthèse droite à la position %d." }
  , { SystemError.PATH_EXPECT_ATTRIBUTE_PATH,      "Chemin vers l'attribut attendu à la position %d." }
  , { SystemError.PATH_EXPECT_ATTRIBUTE_NAME,      "Nom d'un attribut attendu à la position %d." }
  , { SystemError.PATH_INVALID_ATTRIBUTE_PATH,     "Chemin d'attribut non valide à la position %d : %s" }
  , { SystemError.PATH_INVALID_ATTRIBUTE_NAME,     "Nom d'attribut non valide à partir de la position %d : %s" }
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