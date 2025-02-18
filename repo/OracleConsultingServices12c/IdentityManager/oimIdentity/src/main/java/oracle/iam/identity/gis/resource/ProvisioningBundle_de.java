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
    Subsystem   :   Oracle Identity Manager Connector

    File        :   ProvisioningBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProvisioningBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.gis.service.provisioning.ProvisioningError;
import oracle.iam.identity.gis.service.provisioning.ProvisioningMessage;

////////////////////////////////////////////////////////////////////////////////
// class ProvisioningBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 */
public class ProvisioningBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GIS-00021 - 00040 identity related errors
    { ProvisioningError.INSUFFICIENT_INFORMATION,   "Required field information not provided for [%1$s]." }

     // GIS-00101 - 00110 permission assignment errors
  , { ProvisioningError.PERMISSION_ALREADY_GRANTED, "Berechtigung [%1$s] ist [%2$s] bereits zugewiesen" }
  , { ProvisioningError.PERMISSION_ALREADY_REVOKED, "Berechtigung [%1$s] ist [%2$s] bereits entzogen" }

     // GIS-01001 - 01010 permission assignment messages
  , { ProvisioningMessage.PERMISSION_GRANTED,       "Berechtigung [%1$s] wurde [%2$s] zugewiesen" }
  , { ProvisioningMessage.PERMISSION_REVOKED,       "Berechtigung [%1$s] wurde [%2$s] entzogen" }
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