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

    Copyright © 2019. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Password Reset Administration

    File        :   ServiceBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceBundle_de.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.identity.resources;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ServiceBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Property bundle for MBean annotations.
 ** <p>
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  common
 **   <li>language code common
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

  private static final String CONTENT[][] = {
    {"pwr.configuration",   "JMX-Konfigurations-MBean zur Verwaltung der Credential Collector Konfiguration"}
  , {"pwr.action.save",     "Credential Collector Konfiguration speichern." }
  , {"pwr.action.refresh",  "Credential Collector Konfiguration aktualisieren." }
  , {"pwr.action.list",     "Alle Netzwerk Zuordnungen in Credential Collector Konfiguration auflisten." }
  , {"pwr.action.register", "Netzwerk Zuordnungen hinzufügen" }
  , {"pwr.action.remove",   "Netzwerk Zuordnungen entfernen" }
  , {"pwr.action.modify",   "Netzwerk Zuordnungen modifizieren" }

  , {"pwr.error.register",  "Eine Netzwerk Zuordnungen mit dieser CIDR ist bereits registriert." }
  , {"pwr.error.modify",    "Eine Netzwerk Zuordnungen mit dieser CIDR ist nicht registriert." }
  , {"pwr.error.remove",    "Eine Netzwerk Zuordnungen mit dieser CIDR ist nicht registriert." }
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