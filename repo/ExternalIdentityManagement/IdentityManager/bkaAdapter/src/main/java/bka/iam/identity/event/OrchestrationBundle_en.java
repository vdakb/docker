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

    Copyright 2019 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Common Shared Plugin

    File        :   OrchestrationBundle_en.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OrchestrationBundle_en.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
*/

package bka.iam.identity.event;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class OrchestrationBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OrchestrationBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    // ORC-00001 - 00010 configuration related errors
    {OrchestrationError.PROPERTY_NOTFOUND,                  "The system property [%1$s] doesn't exists. Please provide [%1$s] as expected." }
  , {OrchestrationError.PROPERTY_INVALID,                   "The system property [%1$s] isn't configured properly. Please provide [%1$s] as expected." }

    // ORC-00011 - 00020 notification related errors
  , {OrchestrationError.NOTIFICATION_FAILED,                "Sending Notification is not successful.Event Exception occured." }
  , {OrchestrationError.NOTIFICATION_EXCEPTION,             "Sending Notification is not successful.Notification Exception occured.." }
  , {OrchestrationError.NOTIFICATION_STATIC_DATA,           "Static data are empty, provide static data in the notification event xml e.g. [%1$s]" }
  , {OrchestrationError.NOTIFICATION_UNRESOLVED_DATA,       "Sending Notification is not successful.Notification Data not resolved." }
  , {OrchestrationError.NOTIFICATION_TEMPLATE_NOTFOUND,     "Sending Notification is not successful.Notification Template not found." }
  , {OrchestrationError.NOTIFICATION_TEMPLATE_AMBIGOUS,     "Sending Notification is not successful.Multiple template exception occured." }
  , {OrchestrationError.NOTIFICATION_RESOLVER_NOTFOUND,     "Sending Notification is not successful.Notification Resolver not found." }
  , {OrchestrationError.NOTIFICATION_IDENTITY_NOTFOUND,     "Sending Notification is not successful.Identity Details not found." }

    // ORC-00021 - 00030 account request related errors
  , {OrchestrationError.ACCOUNT_EXISTS_ANY,                 "Account already exists:"}

    // 00031 - 00040 Unique Identifier related messages
  , {OrchestrationError.UID_TENANT_NOT_MATCH,               "The UID tenant: [%1$s] does not match with their organization."}
  , {OrchestrationError.UID_ALREADY_USED,                   "UID [%1$s] is already used."}
  , {OrchestrationError.UID_MODIFY_NOT_ALLOWED,             "UID modification is not allowed."}

    // ORC-01001 - 01010 UI related messages
  , {OrchestrationMessage.PRINCIPAL_NAME_LABEL,             "Principal Name"}
  , {OrchestrationMessage.PRINCIPAL_NAME_UNCHNAGED,         "Value of Principal Name unchanged; bypass validation"}
  , {OrchestrationMessage.PARTICIPANT_LABEL,                "Participant"}
  , {OrchestrationMessage.PARTICIPANT_UNCHNAGED,            "Value of Participant unchanged; bypass validation"}

    // ORC-01011 - 01020 account request related errors
  , {OrchestrationMessage.ACCOUNT_RULE_VIOLATED,            "Provisioning status for user [%1$s] validated, implicite validation rule is violated"}
  , {OrchestrationMessage.ACCOUNT_RULE_SATIISFIED,          "Provisioning status for user [%1$s] validated, Implicite validation rule is satisfied"}
  , {OrchestrationMessage.ACCOUNT_VALIDATION_SINGLE,        "Validating provisioning status for user [%1$s] at application [%2$s]"}
  , {OrchestrationMessage.ACCOUNT_VALIDATION_MULTIPLE,      "Validating provisioning status for user [%1$s] at application [%2$s] for account name [%3$s]"}

    // ORC-01021 - 01030 notification related messages
  , {OrchestrationMessage.NOTIFICATION_RESOLVE_INCOME,      "Collection of received substitution mapping:\n"}
  , {OrchestrationMessage.NOTIFICATION_RESOLVE_OUTCOME,     "Collection of returned substitution mapping:\n"}
  , {OrchestrationMessage.NOTIFICATION_ROLE_GRANTED,        "Notification about grant of Role [%1$s] send to Identity [%2$s] leveraging template [%3$s]." }
  , {OrchestrationMessage.NOTIFICATION_ROLE_REVOKED,        "Notification about revoke of Role [%1$s] send to Identity [%2$s] leveraging template [%3$s]." }
  , {OrchestrationMessage.NOTIFICATION_ACCOUNT_GRANTED,     "Notification about grant of Account [%1$s] send to Identity [%2$s] leveraging template [%3$s]." }
  , {OrchestrationMessage.NOTIFICATION_ACCOUNT_REVOKED,     "Notification about revoke of Account [%1$s] send to Identity [%2$s] leveraging template [%3$s]." }
  , {OrchestrationMessage.NOTIFICATION_ENTITLEMENT_GRANTED, "Notification about grant of Entitlement [%1$s] send to Identity [%2$s] leveraging template [%3$s]." }
  , {OrchestrationMessage.NOTIFICATION_ENTITLEMENT_REVOKED, "Notification about revoke of Entitlement [%1$s] send to Identity [%2$s] leveraging template [%3$s]." }

    // 00041 - 00050 Organization related errors
  , {OrchestrationError.ATTRIBUTE_MUST_BE_UNIQUE,           "Attribute [%1$s] must be unique." }
  , {OrchestrationError.ATTRIBUTE_MUST_NOT_BE_BLANK,        "Attribute [%1$s] must not be blank." }
  , {OrchestrationError.ORGANIZATION_SEARCH_ERROR,          "Error while searching for organizations - [%1$s]." }
  , {OrchestrationError.ORGANIZATION_MEMBERS,               "Error while searching for organization members - [%1$s]." }

    // 00051 - 00060 Application related errors
  , {OrchestrationError.APPLICATION_SEARCH_BY_OBJ,          "Application cannot be found by resource objects [%1$s], [%2$s]." }
  , {OrchestrationError.ACCOUNT_SEARCH,                     "Accounts cannot be found by application [%1$s]." }
  , {OrchestrationError.ACCOUNT_UPDATE,                     "Account update error [%1$s]." }

    // 00061 - 00070 Process task related errors
  , {OrchestrationError.PROCESS_TASK_BY_NAME,               "Process task cannot be found by name [%1$s]." }
  , {OrchestrationError.PROCESS_TASK_NOT_FOUND,             "Process task cannot be found by ID [%1$s]." }

    // 00071 - 00080 Authentication related errors
  , {OrchestrationError.LOGIN_FAILED,                       "Login failed during legacy authentication." }
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