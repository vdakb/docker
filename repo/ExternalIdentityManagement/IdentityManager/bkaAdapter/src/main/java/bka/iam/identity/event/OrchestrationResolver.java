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
    Subsystem   :   Identity Manager Notification

    File        :   OrchestrationResolver.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OrchestrationResolver.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  DSteding    First release version
*/

package bka.iam.identity.event;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.TableFormatter;

import oracle.iam.conf.exception.SystemConfigurationServiceException;

import oracle.iam.notification.vo.NotificationAttribute;

import static oracle.iam.identity.utils.Constants.MLS_BASE_VALUE;

import oracle.iam.identity.foundation.event.AbstractNotificationResolver;

////////////////////////////////////////////////////////////////////////////////
// abstract class OrchestrationResolver
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrchestrationResolver</code> provide the basic implementation to
 ** resolve notification events that belongs to an identity.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class OrchestrationResolver extends AbstractNotificationResolver {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PROPERTY_MAIL_HAED = "BKA.Mail.Head";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>OrchestrationResolver</code> notification handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected OrchestrationResolver() {
    // ensure inheritance
    super(OrchestrationHandler.LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenName
  /**
   ** Configuring entity attributes required to be returned in the search
   ** aligning with attributes being shown as 'Available Data' in the
   ** notification template for this event.
   ** 
   ** @param  token              the anchor of the attributes to return.
   **
   ** @return                    the commonly used header to be placed in a
   **                            HTML-Notification template.
   */
  protected Set<String> tokenName(final NotificationAttribute token) {
    final Set<String> simple = new HashSet<String>();
    for (NotificationAttribute cursor : token.getSubtree())
      simple.add(cursor.getName());
    return simple;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mailHead
  /**
   ** Returns the commonly used header to be placed in a HTML-Notification
   ** template.
   **
   ** @return                    the commonly used header to be placed in a
   **                            HTML-Notification template.
   */
  protected String mailHead() {
    final String method = "mailHead";
    trace(method, SystemMessage.METHOD_ENTRY);
    String header = null;
    try {
      header = OrchestrationHandler.systemProperty(PROPERTY_MAIL_HAED);
    }
    catch (SystemConfigurationServiceException e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return header;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareData
  /**
   ** Convert input Map with parameters and values to the map which contain a
   ** prefix. e.g: convert attribute name from "First Name" to
   ** EffectedUser.First_Name Where prefix is EffectedUser
   **
   ** @param  prefix             the string to prefix attributes.
   ** @param  identity           the identity holds attributes which attribute
   **                            name will be prefixed with <b>prefix</b>
   **                            attribute.
   **
   ** @return                    prefixed map
   */
  protected Map<String, Object> prepareData(String prefix, final Map<String, Object> identity) {
    final String method = "prepareData";
    trace(method, SystemMessage.METHOD_ENTRY);

    // if the prefix is null don't prefix attribute name
    // WARNING: do not use a "." as delimiter here!!!!!
    prefix = (prefix == null ? "" : prefix + "#");
    // prepare a return Map
    final Map<String, Object> data = new HashMap<String, Object>();
    // check if the identity is provided
    if (identity != null) {
      // creating map containing mapping between tokens available for template
      // to their actual values
      for (Map.Entry<String, Object> entry : identity.entrySet()) {
        final char c[] = entry.getKey().replaceAll("\\s", "").toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        final String key = new String(c);
        if ((entry.getValue() instanceof Map)) {
          data.put(prefix + key, ((HashMap)entry.getValue()).get(MLS_BASE_VALUE));
        }
        else {
          data.put(prefix + key, entry.getValue());
        }
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatData
  /**
   ** Formats a {@link Map} as an output for debugging purpose.
   **
   ** @param  summary            the summary on top of the formatted collection.
   ** @param  mapping            the {@link Map} to format for debugging output.
   **
   ** @return                    the formatted string representation
   */
  protected String formatData(final String summary, final Map<String, Object> mapping) {
    final TableFormatter table  = new TableFormatter().header("Parameter").header("Value");
    for (Map.Entry<String, Object> entry : mapping.entrySet()) {
      final String value = entry.getValue() == null ? "<null>" : entry.getValue().toString();
      table.row().column(entry.getKey()).column(value);
    }
    final StringBuilder buffer = new StringBuilder(summary);
    table.print(buffer);
    return buffer.toString();
  }
}