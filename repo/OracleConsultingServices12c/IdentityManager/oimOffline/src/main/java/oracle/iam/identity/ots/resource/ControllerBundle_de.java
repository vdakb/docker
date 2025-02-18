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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   ControllerBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ControllerBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.ots.service.ControllerError;
import oracle.iam.identity.ots.service.ControllerMessage;

////////////////////////////////////////////////////////////////////////////////
// class ControllerBundle_de
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
public class ControllerBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OTS-00061 - 00070 file system naming and lookup related errors
    { ControllerError.FILE_NOT_FOLDER,    "Verwendung von %2$s als %1$s ist unzlässige, es ist kein Verzeichnis" }
  , { ControllerError.FILE_NOT_FILE,      "Verwendung von %2$s als %1$s ist unzlässige, es ist keine Datei" }
  , { ControllerError.FILE_NOT_READABLE,  "%2$s kann nicht als %1$s verwendet werden, es kann nicht gelesen werden" }
  , { ControllerError.FILE_NOT_WRITABLE,  "%2$s kann nicht als %1$s verwendet werden, es kann nicht geschriben werden" }
  , { ControllerError.FILE_NOT_CREATABLE, "%1$s kann nicht erzeugt werden. Grund: %2$s" }

     // OTS-01021 - 01030 file handling related messages
  , { ControllerMessage.CREATING_URL,    "Erzeuge URL für %1$s" }
  , { ControllerMessage.CREATING_FOLDER, "Erzeuge Dateihandle für %1$s" }
  , { ControllerMessage.CREATING_FILE,   "Erzeuge Dateihandle für %1$s" }

     // OTS-01031 - 01040 provisioning process related messages
  , { ControllerMessage.COMMENT_START,   " <!-- Provisionierung von %1$s gestartet %2$s -->" }
  , { ControllerMessage.COMMENT_END,     " <!-- Provisionierung von %1$s beendet %2$s -->" }
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