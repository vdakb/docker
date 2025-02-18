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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   IdentityServerBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityServerBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.rmi.IdentityServerError;
import oracle.iam.identity.foundation.rmi.IdentityServerMessage;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
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
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class IdentityServerBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OIM-00001 - 00007 General error (Undefined)
    { IdentityServerError.GENERAL,                         "Allgemeiner Fehler: %1$s" }
  , { IdentityServerError.UNHANDLED,                       "Eine unbehandelte Ausnahme is aufgetreten: %1$s"}
  , { IdentityServerError.ABORT,                           "Die Verarbeitung wird abgebrochen: %1$s"}
  , { IdentityServerError.NOTIMPLEMENTED,                  "Funktionalität ist nicht implementiert"}

     // OIM-00011 - 00020 context related errors
  , { IdentityServerError.CONTEXT_SERVERTYPE_NOTSUPPORTED, "Server Typ [%1$s] nicht unterstützt" }
  , { IdentityServerError.CONTEXT_ENCODING_NOTSUPPORTED,   "URL Encoding [%1$s] nicht unterstützt" }
  , { IdentityServerError.CONTEXT_CONNECTION_ERROR,        "Die Verbindung zum Zielsystem kann nicht aufgebaut werden. Grund:\n%1$s"}
  , { IdentityServerError.CONTEXT_AUTHENTICATION,          "Administrator [%1$s] oder Kennwort ist inkorrekt, mit den angegebenen Information konnte keine Verbindung mit dem Zielsystem hergestellt werden" }
  , { IdentityServerError.CONTEXT_ACCESS_DENIED,           "Administrator [%1$s] hat unzureichende Berechtigungen für die Ausführung von [%2$s]" }

     // OIM-00021 - 00030 identity related errors
  , { IdentityServerError.IDENTITY_NOT_EXISTS,             "Identität [%1$s] existiert nicht" }
  , { IdentityServerError.IDENTITY_AMBIGUOUS,              "Identität [%1$s] ist mehrfach definiert" }
  , { IdentityServerError.PERMISSION_NOT_EXISTS,           "Berechtigung [%1$s] existiert nicht" }
  , { IdentityServerError.PERMISSION_AMBIGUOUS,            "Berechtigung [%1$s] ist mehrfach definiert" }
  , { IdentityServerError.ORGANIZATION_NOT_EXISTS,         "Organisation [%1$s] existiert nicht" }
  , { IdentityServerError.ORGANIZATION_AMBIGUOUS,          "Organisation [%1$s] ist mehrfach definiert" }
  , { IdentityServerError.ROLE_NOT_EXISTS,                 "Rolle [%1$s] existiert nicht" }
  , { IdentityServerError.ROLE_AMBIGUOUS,                  "Rolle [%1$s] ist mehrfach definiert" }
  , { IdentityServerError.ROLE_SEARCH_KEY_AMBIGUOUS,       "Suchschlüssel für Rolle [%1$s] ist mehrfach definiert" }

     // GIS-00031 - 00040 permission assignment errors
  , { IdentityServerError.PERMISSION_ACCESS_DENEID,        "Administrator [%1$s] hat unzureichende Berechtigungen für die Ausführung von [%2$s]" }
  , { IdentityServerError.PERMISSION_NOT_GRANTED,          "Berechtigung [%1$s] konnte [%2$s] nicht zugewiesen werden" }
  , { IdentityServerError.PERMISSION_NOT_REVOKED,          "Berechtigung [%1$s] konnte [%2$s] nicht entzogen werden" }
  , { IdentityServerError.PERMISSION_NOT_MODIFIED,         "Berechtigung [%1$s] konnte für [%2$s] ncht geändert werden" }

    // OIM-01001 - 01010 system related messages
  , { IdentityServerMessage.CONNECTING_TO,                 "Verbindungsaufbau zu [%1$s]" }
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