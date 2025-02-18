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

    File        :   ProcessBundle.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ProcessBundle.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  DSteding    First release version
*/

package bka.iam.identity.resources;

import bka.iam.identity.ProcessError;
import bka.iam.identity.ProcessMessage;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class ProcessBundle
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  common
 **   <li>language code common
 ** </ul>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProcessBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    // PRC-00001 - 00010 configuration related errors
    {ProcessError.PROPERTY_NOTFOUND,                "The system property [%1$s] doesn't exists. Please provide [%1$s] as expected." }
  , {ProcessError.PROPERTY_INVALID,                 "The system property [%1$s] isn't configured properly. Please provide [%1$s] as expected." }

    // PRC-00011 - 00020 notification related errors
  , {ProcessError.NOTIFICATION_FAILED,              "Sending Notification is not successful. Event Exception occured." }
  , {ProcessError.NOTIFICATION_EXCEPTION,           "Sending Notification is not successful. Notification Exception occured." }
  , {ProcessError.NOTIFICATION_UNRESOLVED_DATA,     "Sending Notification is not successful. Notification Data not resolved." }
  , {ProcessError.NOTIFICATION_TEMPLATE_NOTFOUND,   "Sending Notification is not successful. Notification Template not found." }
  , {ProcessError.NOTIFICATION_TEMPLATE_AMBIGOUS,   "Sending Notification is not successful. Multiple template exception occured." }
  , {ProcessError.NOTIFICATION_RESOLVER_NOTFOUND,   "Sending Notification is not successful. Notification Resolver not found." }
  , {ProcessError.NOTIFICATION_IDENTITY_NOTFOUND,   "Sending Notification is not successful. Identity Details not found." }
  , {ProcessError.NOTIFICATION_RECIPIENT_EMPTY,     "Sending Notification is not successful. List of recipients is empty." }

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
  , {ProcessMessage.POLICY_ENTITLEMENT_NOTEXIST,    "The following entitlement is being deleted in the Access Policy: [%1$s]#[%2$s].Reason: Not available anymore in [%3$s]." }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    ProcessBundle.class.getName()
  , Locale.getDefault()
  , ProcessBundle.class.getClassLoader()
  );

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

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a String from this {@link ListResourceBundle}.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                key into the resource array.
   **
   ** @return                    the String resource
   */
  public static String string(final String key) {
    return RESOURCE.getString(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument            the subsitution value for <code>%1$s</code>.
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for <code>%1$s</code>.
   ** @param  argument2           the subsitution value for <code>%2$s</code>.
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for <code>%1$s</code>.
   ** @param  argument2           the subsitution value for <code>%2$s</code>.
   ** @param  argument3           the subsitution value for <code>%3$s</code>.
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2, final Object argument3) {
    return RESOURCE.formatted(key, argument1, argument2, argument3);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object[] arguments) {
    return RESOURCE.formatted(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringFormat
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String stringFormat(final String key, final Object... arguments) {
    return RESOURCE.stringFormatted(key, arguments);
  }
}