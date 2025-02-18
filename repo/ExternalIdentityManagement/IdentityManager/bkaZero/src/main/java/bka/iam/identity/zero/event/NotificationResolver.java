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

    File        :   NotificationResolver.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    NotificationResolver.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      18.08.2023  SBernet     First release version
*/
package bka.iam.identity.zero.event;

import bka.iam.identity.zero.ZeroError;
import bka.iam.identity.zero.ZeroMessage;
import bka.iam.identity.zero.resources.ZeroBundle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.logging.TableFormatter;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.conf.api.SystemConfigurationService;
import oracle.iam.conf.exception.SystemConfigurationServiceException;
import oracle.iam.conf.vo.SystemProperty;
import oracle.iam.identity.foundation.event.AbstractNotificationResolver;
import oracle.iam.platform.Platform;
////////////////////////////////////////////////////////////////////////////////
// class NotificationResolver
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>NotificationResolver</code> provide the implementation to resolve
 ** notification events that belongs to changes in
 ** <code>Zero Provisionning</code>.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NotificationResolver extends AbstractNotificationResolver {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PROPERTY_MAIL_HAED = "BKA.Mail.Header";
  
  static final String PROPERTY_MAIL_FOOT = "BKA.Mail.Footer";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>NotificationResolver</code> notification handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public NotificationResolver() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getReplacedData (NotificationEventResolver)
  /**
   ** {@inheritDoc}
   */
  @Override
  public HashMap<String, Object> getReplacedData(final String eventType, final Map<String, Object> subject)
    throws Exception {

    final String method = "getReplacedData";
    trace(method, SystemMessage.METHOD_ENTRY);

    // write in the log list of the passed in attributes
    if (this.logger() != null && this.logger().debugLevel())
      debug(method, formatData(ZeroBundle.string(ZeroMessage.NOTIFICATION_RESOLVE_INCOME), subject));

    final HashMap<String, Object> replace  = new HashMap<String, Object>();
    // remove the policy key from the passed key/value pair mapping and put the
    // the key/value pairs of the policy metadata belonging to this key in the
    // mapping returned to the template engine
    final String applicationName = (String)subject.remove("applicationName");
    final String emailTo         = (String)subject.remove("emailRecipient");
    
    final Map<String, List<String>> added    = (Map<String, List<String>>) subject.remove("added");
    final Map<String, List<String>> modified = (Map<String, List<String>>) subject.remove("modified");
    final Map<String, List<String>> revoked  = (Map<String, List<String>>) subject.remove("revoked");  
    replace.put("applicationName",  applicationName);
    replace.put("head", mailHead());
    // Check email null
    replace.put("footer", mailFooter().replace("%s", emailTo));
    replace.put("email", emailTo);
    
    replace.put("accountCreated",  added);
    replace.put("accountModified",  modified);
    replace.put("accountRevoked",  revoked);
    
    
    // write in the log list of the returned attributes
    if (this.logger() != null && this.logger().debugLevel())
      debug(method, formatData(ZeroBundle.string(ZeroMessage.NOTIFICATION_RESOLVE_OUTCOME), replace));

    trace(method, SystemMessage.METHOD_EXIT);
    return replace;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  
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
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   mailHead
  /**
   ** Returns the commonly used header to be placed in a HTML-Notification
   ** template.
   **
   ** @return                    the commonly used header to be placed in a
   **                            HTML-Notification template.
   **                            
   ** @throws SystemConfigurationServiceException if property is misconfigured.
   */
  protected String mailHead()
    throws SystemConfigurationServiceException {
    final String method = "mailHead";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      return getSystemPropertieValue(PROPERTY_MAIL_HAED);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   mailFooter
  /**
   ** Returns the commonly used footer to be placed in a HTML-Notification
   ** template.
   **
   ** @return                    the commonly used footer to be placed in a
   **                            HTML-Notification template.
   **                            
   ** @throws SystemConfigurationServiceException if property is misconfigured.
   */
  protected String mailFooter()
    throws SystemConfigurationServiceException {
    final String method = "mailFooter";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      return getSystemPropertieValue(PROPERTY_MAIL_FOOT);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   mailHead
  /**
   ** Returns the value of the provided system property.
   **
   ** @param propertyKeyword     the keyworkd of the property.
   ** 
   ** @return                    Returns the value of the provided system
   **                            property.
   **                            
   ** @throws SystemConfigurationServiceException if property is missing or
   **         empty.
   */
  protected String getSystemPropertieValue(final String propertyKeyword)
    throws SystemConfigurationServiceException {
    final String method = "getSystemPropertieValue";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final SystemConfigurationService config   = Platform.getService(SystemConfigurationService.class);
    final SystemProperty             property = config.getSystemProperty(propertyKeyword);
    if (property == null)
      throw new SystemConfigurationServiceException(ZeroBundle.format(ZeroError.PROPERTY_NOTFOUND, propertyKeyword));
    
     final String value = property.getPtyValue();
    if (StringUtility.isEmpty(value, true))
      throw new SystemConfigurationServiceException(ZeroBundle.format(ZeroError.PROPERTY_INVALID, propertyKeyword));
    
    
    trace(method, SystemMessage.METHOD_EXIT);
    return value; 
  }
}
