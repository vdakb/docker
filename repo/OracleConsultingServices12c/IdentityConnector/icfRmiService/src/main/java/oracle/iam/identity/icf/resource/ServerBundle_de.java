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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Java Enterprise Service Connector Library

    File        :   ServerBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServerBundle_de.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.jes.ServerError;
import oracle.iam.identity.icf.jes.ServerMessage;

////////////////////////////////////////////////////////////////////////////////
// class ServerBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~
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
public class ServerBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // JES-00001 - 00010 connection related errors
    { ServerError.CONNECTION_SERVERTYPE_UNSUPPORTED, "Der für die IT Resource konfigurierte Server Type [%1$s] wird nicht unterstütz!" }
  , { ServerError.CONNECTION_LOGINCONFIG_NOTFOUND,   "Die Konfiguration des Anmeldemoduls wurde im Pfad [%1$s] nicht gefunden!" }
  , { ServerError.CONTEXT_ACCESS_DENIED,             "Der Zugriff zur Ausführung des Vorgangs im Kontext des angemeldeten Benutzers wurde verweigert!" }

    // JES-00011 - 00020 filtering errors
  , { ServerError.FILTER_METHOD_INCONSISTENT,       "Übersetzungsmethode ist inkonsistent: %s" }
  , { ServerError.FILTER_EXPRESSION_INCONSISTENT,   "Übersetzungsausdruck ist inkonsistent: %s" }
  , { ServerError.FILTER_USAGE_INVALID_GE,          "Der Filter \"Größer oder gleich\" vergleicht möglicherweise keine booleschen oder binären Attributwerte." }
  , { ServerError.FILTER_USAGE_INVALID_GT,          "Der Filter \"Größer als\" vergleicht keine booleschen oder binären Attributwerte." }
  , { ServerError.FILTER_USAGE_INVALID_LE,          "Der Filter \"Kleiner oder gleich\" vergleicht keine booleschen oder binären Attributwerte." }
  , { ServerError.FILTER_USAGE_INVALID_LT,          "Der Filter \"Kleiner als\" vergleicht keine booleschen oder binären Attributwerte." }

    // JES-00021 - 00040 processing errors
  , { ServerError.PROCESS_EXISTS,                   "%s" }
  , { ServerError.PROCESS_NOT_EXISTS,               "%s" }
  , { ServerError.PROCESS_INVALID_FILTER,           "%s" }

    // JES-00031 - 00040 object errors
  , { ServerError.OBJECT_NOT_EXISTS,                "%1$s mit %2$s [%3$s] existiert nicht beim Dienstanbieter." }
  , { ServerError.OBJECT_AMBIGUOUS,                 "%1$s mit %2$s [%3$s] ist mehrdeutig beim Dienstanbieter definiert." }

    // JES-01001 - 01020 system related messages
  , { ServerMessage.CONFIGURING,                     "Konfiguriere Umgebung für Service URL [%1$s] ..." }
  , { ServerMessage.CONFIGURED,                      "Umgebung für Service URL [%1$s] konfiguriert." }
  , { ServerMessage.CONNECTING,                      "Verbindungsaufbau zu Service URL [%1$s] ..." }
  , { ServerMessage.CONNECTED,                       "Verbindung zu Service URL [%1$s] aufgebaut." }
  , { ServerMessage.CONNECTION_ALIVE,                "Verbindung zu Service URL [%1$s] ist erfügbar." }
  , { ServerMessage.LOGGINGIN,                       "Log in into service URL [%1$s] as [%2$s] ..." }
  , { ServerMessage.LOGGEDIN,                        "[%2$s] logged in into service URL [%1$s]." }
  , { ServerMessage.LOGGINGOUT,                      "[%2$s] wird von Service URL [%1$s] abgemeldet ..." }
  , { ServerMessage.LOGGEDOUT,                       "[%2$s] wurde von Service URL [%1$s] abgemeldet." }
  , { ServerMessage.DISCONNECTING,                   "Verbindung zu Service URL [%1$s] wird abgebaut ..." }
  , { ServerMessage.DISCONNECTED,                    "Verbindung zu Service URL [%1$s] abgebaut." }
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