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

    File        :   SecurityBundle_de.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SecurityBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SecurityBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SecurityBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // CVE-00011 - 00020 method argument related errors
    { SecurityError.ARGUMENT_IS_NULL,       "Argument %1$s darf nicht null sein!" }

     // CVE-00021 - 00030 key generation related errors
  , { SecurityError.ENCRYPTION_FATAL,       "Die Verschlüsselung hat eine Ausnahme ausgelöst. Eine mögliche Ursache ist, dass starke Verschlüsselungsalgorithmen verwendet werden und die Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files nicht in dieser Java Virtual Machine installiert sind!" }
  , { SecurityError.PASSWORD_FATAL,         "Passwort für Password Handler nicht gesetzt!" }
  , { SecurityError.PASSWORD_HANDLER,       "Das Passwort kann für den Kennwort-Handler nicht auf null oder leer gesetzt werden!" }

     // CVE-00031 - 00040 key generation related errors
  , { SecurityError.KEYTYPE_FATAL,          "Marker [%1$s] nicht gefunden!" }
  , { SecurityError.PUBLICKEY_FATAL,        "Problem beim Generieren des öffentlichen Schlüssels: %1$s" }
  , { SecurityError.PRIVATEKEY_FATAL,       "Problem beim Generieren des privaten Schlüssels: %1$s" }
  , { SecurityError.CERTIFICATE_FATAL,      "Problem beim Generieren des Zertifikats: %1$s" }
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