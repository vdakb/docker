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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Directory Service Connector

    File        :   ProvisioningBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProvisioningBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.gds.service.provisioning.ProvisioningError;
import oracle.iam.identity.gds.service.provisioning.ProvisioningMessage;

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
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class ProvisioningBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // 00071 - 00080 configuration related errors
    { ProvisioningError.FEATURE_PROPERTY_MISSING, "Entry %1$s is missing in Server Feature configuration" }
  , { ProvisioningError.MAPPING_PROPERTY_MISSING, "Attribute %1$s is missing in process data mapping" }

    // 00081 - 00090 provisioning process related errors
  , { ProvisioningError.ENTRY_CREATE,             "Creation of Directory Service entry %1$s in %2$s failed. %3$s" }
  , { ProvisioningError.ENTRY_DELETE,             "Deletion of Directory Service entry %1$s in %2$s failed. %3$s" }
  , { ProvisioningError.ENTRY_MODIFY,             "Modification of Directory Service entry %1$s in %2$s failed. %3$s" }

  , { ProvisioningMessage.SEARCH_CRITERIA,        "Perform %2$s search in Search Base %1$s with Search Filter %3$s" }
  , { ProvisioningMessage.SEARCH_RESULT,          "Result of search is %1$s" }

  , { ProvisioningMessage.ENTRY_CREATE,           "Creating Directory Service entry %1$s in %2$s ..." }
  , { ProvisioningMessage.ENTRY_CREATED,          "Directory Service entry %1$s in %2$s created." }
  , { ProvisioningMessage.ENTRY_DELETE,           "Deleting Directory Service entry %1$s in %2$s ..." }
  , { ProvisioningMessage.ENTRY_DELETED,          "Directory Service entry %1$s in %2$s deleted." }
  , { ProvisioningMessage.ENTRY_MODIFY,           "Modifying Directory Service entry %1$s in %2$s ..." }
  , { ProvisioningMessage.ENTRY_MODIFIED,         "Directory Service entry %1$s in %2$s modified." }
  , { ProvisioningMessage.ENTRY_UPDATE_PASSWORD,  "Updating Directory Service entry password for %1$s" }
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