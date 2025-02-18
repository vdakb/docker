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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Plugin Shared Library
    Subsystem   :   Common Shared Plugin

    File        :   RoleNotificationResolver.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleNotificationResolver.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.event.role;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import oracle.iam.identity.vo.Identity;

import static oracle.iam.identity.utils.Constants.ROLEKEY;
import static oracle.iam.identity.utils.Constants.USERKEY;
import static oracle.iam.identity.utils.Constants.DISPLAYNAME;
import static oracle.iam.identity.utils.Constants.MLS_BASE_VALUE;

import oracle.iam.identity.rolemgmt.vo.Role;

import oracle.iam.identity.rolemgmt.api.RoleManager;

import oracle.iam.identity.usermgmt.api.UserManager;

import oracle.iam.notification.vo.NotificationAttribute;

import oracle.iam.notification.api.NotificationService;

import oracle.iam.identity.foundation.event.AbstractNotificationResolver;

////////////////////////////////////////////////////////////////////////////////
// class RoleNotificationResolver
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>RoleNotificationResolver</code> provide the basic implementation
 ** to resolve notification events that belongs to roles.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 */
public class RoleNotificationResolver extends AbstractNotificationResolver {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String LOGGER_CATEGORY = "OCS.USR.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleNotificationResolver</code> event handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleNotificationResolver() {
    super(LOGGER_CATEGORY);
  }

  /* (non-Javadoc)
   * @see oracle.iam.notification.impl.NotificationEventResolver#getReplacedData(
   *  java.lang.String, java.util.Map)
   */
  @Override
  public HashMap<String, Object> getReplacedData(final String eventType, final Map<String, Object> subject)
    throws Exception {

    final String userKey = (String)subject.get(USERKEY);
    // TODO: check if we have "role_key"
    final String mappingKey = ROLEKEY;
    final String roleKey = (String)subject.get(ROLEKEY);

    final HashMap<String, Object> substitution = new HashMap<String, Object>();
    // Mapping token with their actual value for user attributes.
    if (userKey != null) {
      final UserManager                 entity  = service(UserManager.class);
      final NotificationService         service = service(NotificationService.class);
      final List<NotificationAttribute> tokens  = service.getStaticData(eventType);

      // configuring user attributes required to be returned in the search
      // aligning with attributes being showed as 'Available Data' in the
      // notification template for this event.
      Set<String> returning = new HashSet<String>();
      for (NotificationAttribute token : tokens.get(0).getSubtree())
        returning.add(token.getName());

      // getting values for the attributes using userKey
      Identity user = entity.getDetails(userKey, returning, false);
      HashMap<String, Object> attributes = user.getAttributes();

      // creating map containing mapping between tokens available for template
      // to their actual values
      String key = null;

      for (Map.Entry<String, Object> entry : attributes.entrySet()) {
        key = entry.getKey();
        if (key != null) {
          if ((entry.getValue() instanceof java.util.Map) && (key.equalsIgnoreCase(DISPLAYNAME))) {
            key = key.replace(' ', '_');
            substitution.put(key, ((HashMap)entry.getValue()).get(MLS_BASE_VALUE));
          }
          else {
            key = key.replace(' ', '_');
            substitution.put(key, entry.getValue());
          }
        }
      }
    }

    // Mapping token associated with Role entity in the template with its actual value
    if (roleKey != null) {
      RoleManager entity    = service(RoleManager.class);
      Set<String> returning = new HashSet<String>();

      // configuring role attributes required to be returned in the search
      // aligning with attributes being showed as 'Available Data' in the
      // notification template for this event.
      returning.add("Role Name");

      Role role = entity.getDetails(roleKey, returning);
      substitution.put("UGP_ROLENAME", role.getName());
    }
    return substitution;
  }
}