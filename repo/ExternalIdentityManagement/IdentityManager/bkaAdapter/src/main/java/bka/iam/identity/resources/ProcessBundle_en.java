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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Common Shared Plugin

    File        :   ProcessBundle_en.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ProcessBundle_en.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  DSteding    First release version
*/

package bka.iam.identity.resources;

import bka.iam.identity.ProcessError;
import bka.iam.identity.ProcessMessage;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class ProcessBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProcessBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    // PRC-00001 - 00010 configuration related errors
    {ProcessError.PROPERTY_NOTFOUND,              "The system property [%1$s] doesn't exists. Please provide [%1$s] as expected." }
  , {ProcessError.PROPERTY_INVALID,               "The system property [%1$s] isn't configured properly. Please provide [%1$s] as expected." }

    // PRC-00011 - 00020 notification related errors
  , {ProcessError.NOTIFICATION_FAILED,            "Sending Notification is not successful. Event Exception occured." }
  , {ProcessError.NOTIFICATION_EXCEPTION,         "Sending Notification is not successful. Notification Exception occured." }
  , {ProcessError.NOTIFICATION_UNRESOLVED_DATA,   "Sending Notification is not successful. Notification Data not resolved." }
  , {ProcessError.NOTIFICATION_TEMPLATE_NOTFOUND, "Sending Notification is not successful. Notification Template not found." }
  , {ProcessError.NOTIFICATION_TEMPLATE_AMBIGOUS, "Sending Notification is not successful. Multiple template exception occured." }
  , {ProcessError.NOTIFICATION_RESOLVER_NOTFOUND, "Sending Notification is not successful. Notification Resolver not found." }
  , {ProcessError.NOTIFICATION_IDENTITY_NOTFOUND, "Sending Notification is not successful. Identity Details not found." }
  , {ProcessError.NOTIFICATION_RECIPIENT_EMPTY,   "Sending Notification is not successful. List of recipients is empty." }

    // PRC-00021 - 00030 housekeeping related errors
  , {ProcessError.HOUSEKEEPING_CONNECTOR_NOTFOUND,  "Connector Bundle for IT Resource [%1$s] not found." }
  , {ProcessError.HOUSEKEEPING_DESCRIPTOR_NOTFOUND, "Additional information for [%1$s] does not exist in the Metadata Store." }

    // PRC-00031 - 00040 Directory Synchronization related errors
  , {ProcessError.SEARCH_ENTRY_FAILED,              "Error when searching [%1$s]. Reason: [%2$s]" }
  , {ProcessError.UPDATE_ENTRY_FAILED,              "Update entry failed. Reason: [%1$s] " }

    // 00041 - 00050 Request cleanup related errors
  , {ProcessError.REQUEST_STAGES_FAILED,            "Error when searching request stages [%1$s]. Reason: [%2$s]" }
  , {ProcessError.REQUEST_SEARCH_FAILED,            "Error when searching requests [%1$s]. Reason: [%2$s]" }
    
    // PRC-01001 - 01010 access policy related messages
  , {ProcessMessage.POLICY_ENTITLEMENT_NOTEXIST,  "The following entitlement is being deleted in the access policy: [%1$s]#[%2$s].Reason: Not available anymore in [%3$s]." }
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