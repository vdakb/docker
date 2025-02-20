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
    Subsystem   :   Zero Provisioning

    File        :   ZeroBundle_en.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    ZeroBundle_en.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      14.08.2023  SBernet     First release version
*/
package bka.iam.identity.zero.resources;

import bka.iam.identity.zero.ZeroError;
import bka.iam.identity.zero.ZeroMessage;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class ZeroBundle_en
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code english
 **   <li>language code common
 ** </ul>
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ZeroBundle_en extends ListResourceBundle {

  private static final String[][] CONTENT = {
  
    // ZRO-00001 - 00010 configuration related errors
    { ZeroError.APPLICATION_NOTFOUND,            "Application %1$s not found." }
  , { ZeroError.ORGANIZATION_NOTFOUND,           "Organization %1$s not found." }
  , { ZeroError.PROPERTY_NOTFOUND,               "The system property \"%1$s\" doesn't exists. Please provide \"%1$s\" as expected." }
  , { ZeroError.PROPERTY_INVALID,                "The system property \"%1$s\" isn't configured properly. Please provide \"%1$s\" as expected." }
    
    // ZRO-00011 - 00020 ldap related errors
  , { ZeroError.LDAP_ERROR,                      "LDAP Error: %1$s"}
  , { ZeroError.NAMING_DN_ERROR,                 "LDAP DN: %1$s malformed"}
  , { ZeroError.INDEX_RDN_OUT_RANGE,             "RDN index: %1$s outside range on DN: %2$s"}
    
    // ZRO-00021 - 00030 notification related errors
  , {ZeroError.NOTIFICATION_FAILED,              "Sending Notification is not successful. Event Exception occurred." }
  , {ZeroError.NOTIFICATION_EXCEPTION,           "Sending Notification is not successful. Notification Exception occurred." }
  , {ZeroError.NOTIFICATION_UNRESOLVED_DATA,     "Sending Notification is not successful. Notification Data not resolved." }
  , {ZeroError.NOTIFICATION_TEMPLATE_NOTFOUND,   "Sending Notification is not successful. Notification Template not found." }
  , {ZeroError.NOTIFICATION_TEMPLATE_AMBIGOUS,   "Sending Notification is not successful. Multiple template exception occurred." }
  , {ZeroError.NOTIFICATION_RESOLVER_NOTFOUND,   "Sending Notification is not successful. Notification Resolver not found." }
  , {ZeroError.NOTIFICATION_IDENTITY_NOTFOUND,   "Sending Notification is not successful. Identity Details not found." }
  , {ZeroError.NOTIFICATION_RECIPIENT_EMPTY,     "Sending Notification is not successful. List of recipients is empty." }

    // ZRO-01001 - 01010  account operation related message
  , { ZeroMessage.REQUEST_NEW_ACCOUNT,            "Request a new account with the following value:\n%1$s"}
  , { ZeroMessage.REQUEST_MODIFY_ACCOUNT,         "Modify an existing account with the following value:\n%1$s"}
  , { ZeroMessage.NO_ACCOUNT_TO_REQUEST,          "No accounts found on LDAP. No operations will be made on OIM."}
    
    // ZRO-01021 - 01030 LDAP related message
  , { ZeroMessage.LDAP_SEARCH_QUERY,              "Executing the following query %1$s base on the search base %2$s"}
  , { ZeroMessage.LDAP_SEARCH_RESULT,             "LDAP Server returns the following value:\n%1$s"}
  , { ZeroMessage.LDAP_ATTR_NOT_FOUND,            "Attribute %1$s not found in the entry %2$s"}
    
    // ZRO-01031 - 01040 notification related message
  , { ZeroMessage.NOTIFICATION_RESOLVE_INCOME,    "Collection of received substitution mapping:\n"}
  , { ZeroMessage.NOTIFICATION_RESOLVE_OUTCOME,   "Collection of returned substitution mapping:\n"}
  , { ZeroMessage.REPORTER_EMPTY,                 "No modification on accounts detected on %1$s. No notification to send."}
  , { ZeroMessage.REPORTER_NOT_FOUND,             "Reporter %1$s not found. No notification to send."}
 
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