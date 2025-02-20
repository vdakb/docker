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
    Subsystem   :   Access Policy House Keeping

    File        :   NotificationResolver.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    NotificationResolver.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  DSteding    First release version
*/

package bka.iam.identity.event.ugp;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import oracle.iam.identity.vo.Identity;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.notification.vo.NotificationAttribute;

import oracle.iam.notification.api.NotificationService;

import oracle.hst.foundation.SystemMessage;

import bka.iam.identity.event.OrchestrationBundle;
import bka.iam.identity.event.OrchestrationError;
import bka.iam.identity.event.OrchestrationMessage;
import bka.iam.identity.event.OrchestrationHandler;
import bka.iam.identity.event.OrchestrationResolver;

////////////////////////////////////////////////////////////////////////////////
// class NotificationResolver
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>NotificationResolver</code> provide the basic implementation to
 ** resolve notification events that belongs to roles granted to or revoked from
 ** an identity.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NotificationResolver extends OrchestrationResolver {

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
      debug(method, formatData(OrchestrationBundle.string(OrchestrationMessage.NOTIFICATION_RESOLVE_INCOME), subject));

    final HashMap<String, Object> replace = new HashMap<String, Object>();
    try {
      // obtain notification API
      final NotificationService service = service(NotificationService.class);
      // read all static paramters from the notification event
      // all static data parameters will be populated with real values based on
      // the input parameter RoleManagerConstants.USER_KEYS_PARAM and
      // RoleManagerConstants.ROLE_KEYS_PARAM
      final List<NotificationAttribute> tokens = service.getStaticData(eventType);
      // check if the static data has been provided
      if (tokens == null || tokens.size() == 0) {
        error(method, OrchestrationBundle.format(OrchestrationError.NOTIFICATION_STATIC_DATA, "/metadata/bka-features-provisioning/NotificationEvent.xml"));
        // Return empty Map, method doesn't know what data should be resolved
        return replace;
      }

      // resolve the beneficiary and put the data in the mapping created above
      final Identity beneficiary = OrchestrationHandler.identity((String)subject.get(RoleManagerConstants.USER_KEYS_PARAM), tokenName(tokens.get(0)), false);
      // convert the return data to the following pattern:
      // EntityName.ParameterName
      replace.putAll(prepareData(null, beneficiary.getAttributes()));

     // resolve the subject and put the data in the mapping created above
      final Identity role = OrchestrationHandler.role((String)subject.get(RoleManagerConstants.ROLE_KEYS_PARAM), tokenName(tokens.get(1)));
      // Convert the return data to the following pattern:
      // EntityName.ParameterName
      // and add it to the return map
      replace.putAll(prepareData(null, role.getAttributes()));

      // write in the log list of the returned attributes
      if (this.logger() != null && this.logger().debugLevel())
        debug(method, formatData(OrchestrationBundle.string(OrchestrationMessage.NOTIFICATION_RESOLVE_OUTCOME), replace));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
      replace.put("html-head", mailHead());
    }
    return replace;
  }
}