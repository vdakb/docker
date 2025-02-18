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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   ServiceBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.ServiceMessage;

////////////////////////////////////////////////////////////////////////////////
// class ServiceBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~
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
public class ServiceBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GWS-00001 - 00007 General error (Undefined)
    { ServiceError.UNHANDLED,                   "Eine unbehandelte Ausnahme is aufgetreten: [%1$s]"}

     // GWS-00021 - 00030 instance state related errors
  , { ServiceError.INSTANCE_ATTRIBUTE_IS_NULL,  "Ungültiger Instanzzustand: Attribute [%1$s] ist nicht initialisiert" }
  , { ServiceError.INSTANCE_ILLEGAL_STATE,      "Ungültiger Instanzzustand: Attribute [%1$s] ist bereits initialisiert" }

     // GWS-00031 - 00040 connectivity errors
  , { ServiceError.CONNECTION_UNKNOWN_HOST,     "Host [%1$s] ist nicht bekannt" }
  , { ServiceError.CONNECTION_CREATE_SOCKET,    "Die Netzwerkverbindung zu Host [%1$s] über Port [%2$s] kann nicht hergestellt werden" }
  , { ServiceError.CONNECTION_SECURE_SOCKET,    "Die gesicherte Netzwerkverbindung zu Host [%1$s] über Port [%2$s] kann nicht hergestellt werden" }
  , { ServiceError.CONNECTION_CERTIFICATE_HOST, "Unable to find valid certification path to requested target [%1$s]" }
  , { ServiceError.CONNECTION_ERROR,            "Die Verbindung zum Dienstanbieter kann nicht aufgebaut werden" }
  , { ServiceError.CONNECTION_TIMEOUT,          "Die Verbindung zum Dienstanbieter wurde wegen Zeitüberschreitung abgebrochen: [%1$s]" }
  , { ServiceError.CONNECTION_NOT_AVAILABLE,    "The problem may be with physical connectivity or Service Provider is not alive" }
  , { ServiceError.CONNECTION_AUTHENTICATION,   "Schlüssel ist inkorrekt, mit den angegebenen Information konnte keine Verbindung mit dem Dienstanbieter hergestellt werden" }
  , { ServiceError.CONNECTION_AUTHORIZATION,    "Benutzerkonto [%1$s] ist nicht autorisiert" }
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