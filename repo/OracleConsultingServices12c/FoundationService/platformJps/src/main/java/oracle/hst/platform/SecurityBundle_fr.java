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

    System      :   Oracle Security Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   SecurityBundle_fr.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SecurityBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SecurityBundle_fr
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
public class SecurityBundle_fr extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // CVE-00011 - 00020 method argument related errors
    { SecurityError.ARGUMENT_IS_NULL,       "L'argument passé %1$s ne doit pas être nul !" }

     // CVE-00021 - 00030 key generation related errors
  , { SecurityError.ENCRYPTION_FATAL,       "Le chiffrement a déclenché une exception. Une cause possible est que des algorithmes de cryptage puissants sont utilisés et que les fichiers de politique de juridiction de force illimitée de Java Cryptography Extension (JCE) ne sont pas installés dans cette machine virtuelle Java !" }
  , { SecurityError.PASSWORD_FATAL,         "Mot de passe non défini pour le gestionnaire de mots de passe !" }
  , { SecurityError.PASSWORD_HANDLER,       "Le mot de passe ne peut pas être défini sur null ou vide pour le gestionnaire de mots de passe !" }

  , { SecurityError.KEYTYPE_FATAL,          "Marqueur [%1$s] introuvable !" }
  , { SecurityError.PUBLICKEY_FATAL,        "Problème de rencontre lors de la génération de la clé publique: %1$s" }
  , { SecurityError.PRIVATEKEY_FATAL,       "Problème de rencontre lors de la génération de la clé privée: %1$s" }
  , { SecurityError.CERTIFICATE_FATAL,      "Problème de rencontre lors de la génération du certificat: %1$s" }
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