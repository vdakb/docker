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

    File        :   WebServiceBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    WebServiceBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.soap.WebServiceError;

////////////////////////////////////////////////////////////////////////////////
// class WebServiceBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
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
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class WebServiceBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // SOA-00001 - 00007 General error (Undefined)
    { WebServiceError.GENERAL,                          "Allgemeiner Fehler: %1$s" }
  , { WebServiceError.UNHANDLED,                        "Eine unbehandelte Ausnahme is aufgetreten: %1$s"}
  , { WebServiceError.ABORT,                            "Die Verarbeitung wird abgebrochen: %1$s"}
  , { WebServiceError.NOTIMPLEMENTED,                   "Funktionalität ist nicht implementiert"}

     // SOA-00011 - 00012 method argument related errors
  , { WebServiceError.ARGUMENT_IS_NULL,                 "Argument [%1$s] darf nicht null sein" }

     // SOA-00021 - 00030 instance state related errors
  , { WebServiceError.INSTANCE_ATTRIBUTE_IS_NULL,       "Ungültiger Instanzzustand: Attribute [%1$s] ist nicht initialisiert" }

     // SOA-00061 - 00080 connectivity errors
  , { WebServiceError.CONNECTION_UNKNOWN_HOST,          "Host [%1$s] ist nicht bekannt" }
  , { WebServiceError.CONNECTION_CREATE_SOCKET,         "Die Netzwerkverbindung zu Host [%1$s] über Port [%2$s] kann nicht hergestellt werden" }
  , { WebServiceError.CONNECTION_ERROR,                 "Die Verbindung zum Zielsystem kann nicht aufgebaut werden" }
  , { WebServiceError.CONNECTION_TIMEOUT,               "Die Verbindung zum Zielsystem wurde wegen Zeitüberschreitung abgebrochen: [%1$s]" }
  , { WebServiceError.CONNECTION_UNAVAILABLE,           "%1$s\nThe problem may be with physical connectivity or Target System is not alive" }
  , { WebServiceError.CONNECTION_SSL_HANDSHAKE,         "Das SSL Zertifikat ist möglicherwiese nicht für das Zielsystem odnungsgemäß generiert worden" }
  , { WebServiceError.CONNECTION_SSL_ERROR,             "Not able to invalidate SSL session." }
  , { WebServiceError.CONNECTION_SSL_DESELECTED,        "SSL option is not selected in IT Resource." }
  , { WebServiceError.CONNECTION_AUTHENTICATION,        "Administrator [%1$s] oder Kennwort ist inkorrekt.\nMit den angegebenen Information konnte keine Verbindung mit dem Zielsystem hergestellt werden" }
  , { WebServiceError.CONNECTION_AUTHORIZATION,         "Zugriff verweigert für Administrator [%1$s]" }
  , { WebServiceError.CONNECTION_ENCODING_NOTSUPPORTED, "URL Encoding [%1$s] not supported" }

     // SOA-00081 - 00090 connectivity errors
  , { WebServiceError.SERVICE_AUTHENTICATION,           "Administrator [%1$s] oder Kennwort ist inkorrekt.\nMit den angegebenen Information konnte der Dienst nicht aufgerufen werden" }
  , { WebServiceError.SERVICE_AUTHORIZATION,            "Zugriff verweigert für Administrator [%1$s]" }
  , { WebServiceError.SERVICE_UNAVAILABLE,              "Service ist nicht verfügbar" }
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