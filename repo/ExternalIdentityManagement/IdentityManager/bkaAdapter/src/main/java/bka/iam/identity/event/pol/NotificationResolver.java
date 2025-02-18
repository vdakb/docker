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

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    NotificationResolver.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.event.pol;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import oracle.iam.accesspolicy.vo.AccessPolicy;

import oracle.iam.accesspolicy.api.AccessPolicyService;

import static oracle.iam.accesspolicy.utils.Constants.POLICY_KEY;

import oracle.hst.foundation.SystemMessage;

import bka.iam.identity.event.OrchestrationBundle;
import bka.iam.identity.event.OrchestrationMessage;
import bka.iam.identity.event.OrchestrationResolver;

////////////////////////////////////////////////////////////////////////////////
// class NotificationResolver
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>NotificationResolver</code> provide the implementation to resolve
 ** notification events that belongs to changes in <code>Access Policies</code>.
 **
 ** @author  sylvert.bernet@oracle.com
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

    final HashMap<String, Object> replace  = new HashMap<String, Object>();
    // remove the policy key from the passed key/value pair mapping and put the
    // the key/value pairs of the policy metadata belonging to this key in the
    // mapping returned to the template engine
    final String policyKey = (String)subject.remove(POLICY_KEY);
    if (policyKey != null) {
      final AccessPolicyService service = service(AccessPolicyService.class);
      final AccessPolicy        policy  = service.getAccessPolicy(policyKey, true);
      for (Map.Entry<String, Object> attribute : policy.getAttributes().entrySet()) {
        final String key = attribute.getKey().replace(' ', '_');
        replace.put("pol_" + key, attribute.getValue());
      }
    }
    // remove the name of the endpoint from the passed key/value pair mapping
    // and put a new key/value pair with this name in the mapping returned to
    // the template engine
    replace.put("endpoint", subject.remove("endpoint"));

    final Object changed = subject.remove("changed");
    // add remaning attributes from parameters
    final StringBuilder builder = new StringBuilder();
    // convert list by html bullet point
    if (changed instanceof java.util.List) {
      final List<String> value = (List<String>)changed;
      for (String cursor : value) {
        builder.append(String.format("<div class=\"tr\"><div class=\"td\" style=\"padding-bottom:0rem !important;\">%s</div></div>", cursor));
      }
    }
    else {
      builder.append(String.format("<div class=\"tr\"><div class=\"td\">%s</div></div>", changed));
    }
    builder.append("</div>");
    replace.put("tbody", builder.toString());
    // write in the log list of the returned attributes
    if (this.logger() != null && this.logger().debugLevel())
      debug(method, formatData(OrchestrationBundle.string(OrchestrationMessage.NOTIFICATION_RESOLVE_OUTCOME), replace));

    trace(method, SystemMessage.METHOD_EXIT);
    replace.put("html-head", mailHead());
    return replace;
  }
}