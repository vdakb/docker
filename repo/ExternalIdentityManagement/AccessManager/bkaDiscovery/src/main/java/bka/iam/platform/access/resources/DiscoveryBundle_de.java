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

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Identity Provider Discovery

    File        :   DiscoveryBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DiscoveryBundle_de.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.resources;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class DiscoveryBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~
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
public class DiscoveryBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    {"cfg.configuration",   "JMX-Konfigurations-MBean zur Verwaltung der Identity Provider Konfiguration"}
  , {"cfg.action.save",     "Identity Provider Konfiguration speichern." }
  , {"cfg.action.refresh",  "Identity Provider Konfiguration aktualisieren." }
  , {"idp.action.list",     "Alle Identity Provider in IDP-Konfiguration auflisten." }
  , {"idp.action.register", "Identity Provider hinzufügen." }
  , {"idp.action.remove",   "Identity Provider entfernen." }
  , {"idp.action.modify",   "Identity Provider modifizieren." }
  , {"net.action.list",     "Alle Netzwerk Zuordnungen in IDP-Konfiguration auflisten." }
  , {"net.action.register", "Netzwerk Zuordnungen hinzufügen" }
  , {"net.action.remove",   "Netzwerk Zuordnungen entfernen" }
  , {"net.action.modify",   "Netzwerk Zuordnungen modifizieren" }


  , {"idp.error.register",  "Ein Identity Provider mit dem gleichen Identifier ist bereits registriert." }
  , {"idp.error.modify",    "Ein Identity Provider mit diesem Identifier ist nicht registriert." }
  , {"idp.error.remove",    "Ein Identity Provider mit diesem Identifier ist nicht registriert." }

  , {"net.error.register",  "Eine Netzwerk Zuordnungen mit dieser CIDR ist bereits registriert." }
  , {"net.error.modify",    "Eine Netzwerk Zuordnungen mit dieser CIDR ist nicht registriert." }
  , {"net.error.remove",    "Eine Netzwerk Zuordnungen mit dieser CIDR ist nicht registriert." }
  , {"net.error.id",        "Ein Identity Provider mit diesem Identifier ist nicht registriert." }
    
  , {"otp.action.list",     "Auflisten von konfigurierten OTP Benachrichtungsvorlagen." }
  , {"otp.action.register", "Hinzufügen einer OTP Benachrichtungsvorlage." }  
  , {"otp.action.remove",   "Entfernen einer OTP Benachrichtungsvorlage." }
  , {"otp.action.modify",   "Ändern einer OTP Benachrichtungsvorlage." }         
  , {"otp.error.register",  "Es existiert bereits eine OTP Benachrichtungsvorlage für die Sprachumgebung." }
  , {"otp.error.modify",    "Es existiert keine OTP Benachrichtungsvorlage für die Sprachumgebung." }
  , {"otp.error.remove",    "Es existiert keine OTP Benachrichtungsvorlage für die Sprachumgebung." }
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