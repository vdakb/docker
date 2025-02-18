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

    File        :   ProvisioningBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProvisioningBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.gis.service.provisioning.ProvisioningError;
import oracle.iam.identity.gis.service.provisioning.ProvisioningMessage;

////////////////////////////////////////////////////////////////////////////////
// class ProvisioningBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 */
public class ProvisioningBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GIS-00021 - 00040 identity related errors
    { ProvisioningError.INSUFFICIENT_INFORMATION,   "Required field information not provided for [%1$s]." }

     // GIS-00101 - 00110 permission assignment errors
  , { ProvisioningError.PERMISSION_ALREADY_GRANTED, "Permission [%1$s] already granted to [%2$s]" }
  , { ProvisioningError.PERMISSION_ALREADY_REVOKED, "Permission [%1$s] already revoked from [%2$s]" }

     // GIS-01001 - 01010 permission assignment messages
  , { ProvisioningMessage.PERMISSION_GRANTED,       "Permission [%1$s] granted to [%2$s]" }
  , { ProvisioningMessage.PERMISSION_REVOKED,       "Permission [%1$s] revoked from [%2$s]" }
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