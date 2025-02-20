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

    File        :   AccountValidator.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountValidator.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
*/

package bka.iam.identity.event.req;

import oracle.iam.platform.Platform;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;

import oracle.iam.request.plugins.RequestDataValidator;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.event.AbstractEventHandler;

import bka.iam.identity.event.OrchestrationHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class AccountValidator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Request Validator instance is used to avoid request of
 ** <code>Application Instances</code> for identities that already have a
 ** primary account for a certain <code>Application Instances</code>.
 ** <p>
 ** To be more specific, the validator doesn't allow to raise a request if:
 ** <ol>
 **   <li>the request is raised for identities that already have a primary
 **       account of the application instance.
 **   <li>the accoumnt name attribute for the requested
 **       <code>Application Instances</code> is left empty at the request form
 **       which will lead to the account name pre-population of the account form
 **       that later provision an account with the same username to the same
 **       Target System. The result of this opreation will always fail because
 **       the username of the Traget System needs to be unique.
 **   <li>the accoumnt name attribute for the requested
 **       <code>Application Instances</code> is populate either manually or by
 **       pre-population plug-ins at the request form which prevents the account
 **       name pre-population of the account form that later provision an account
 **       but will create an account in the Target System with the same username.
 **       The result of this opreation will also fail because the username of the
 **       Traget System needs to be unique.
 **  </ol>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AccountValidator extends    AbstractEventHandler
                                implements RequestDataValidator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String REVOKED  = "Revoked";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountValidator</code> event handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AccountValidator() {
    // ensure inheritance
    super(OrchestrationHandler.LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  applicationName
  /**
   ** Returns the application name of the specified <code>applicationKey</code>.
   ** <br>
   ** The application name isn't required for the functionallity of this Request
   ** Validator but needed for the user information in the raised exception.
   **
   ** @param  applicationKey     the system identifier of an application
   **                            instance in Identity Manager.
   **
   ** @return                    the application instance name in Identity
   **                            Manager for the specified
   **                            <code>applicationKey</code>.
   */
  protected String applicationName(final String applicationKey) {
    final String method = "applicationName";
    trace(method, SystemMessage.METHOD_ENTRY);

    final ApplicationInstanceService service = Platform.getService(ApplicationInstanceService.class);
    ApplicationInstance instance = null;
    try {
      instance = service.findApplicationInstanceByKey(Long.valueOf(applicationKey).longValue());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return instance == null ? applicationKey : instance.getDisplayName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  loginName
  /**
   ** Returns the login name of the specified <code>userKey</code>.
   ** <br>
   ** The login name isn't required for the functionallity of this Request
   ** Validator but needed for the user information in the raised exception.
   **
   ** @param  userKey            the system identifier of a user in Identity
   **                            Manager.
   **
   ** @return                    the login name of the identity in Identity
   **                            Manager for the specified <code>userKey</code>.
   */
  protected String loginName(final String userKey) {
    final String method = "loginName";
    trace(method, SystemMessage.METHOD_ENTRY);

    final UserManager service = Platform.getService(UserManager.class);
    User identity = null;
    try {
      identity = service.getDetails(userKey, null, false);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return identity == null ? userKey : identity.getLogin();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  displayName
  /**
   ** Returns the login name of the specified <code>userKey</code>.
   ** <br>
   ** The login name isn't required for the functionallity of this Request
   ** Validator but needed for the user information in the raised exception.
   **
   ** @param  userKey            the system identifier of a user in Identity
   **                            Manager.
   **
   ** @return                    the login name of the identity in Identity
   **                            Manager for the specified <code>userKey</code>.
   */
  protected String displayName(final String userKey) {
    final String method = "displayName";
    trace(method, SystemMessage.METHOD_ENTRY);

    final UserManager service = Platform.getService(UserManager.class);
    User identity = null;
    try {
      identity = service.getDetails(userKey, null, false);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return identity == null ? userKey : identity.getDisplayName();
  }
}