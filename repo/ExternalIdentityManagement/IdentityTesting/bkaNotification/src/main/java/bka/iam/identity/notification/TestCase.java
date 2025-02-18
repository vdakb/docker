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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Unit Testing
    Subsystem   :   Notification

    File        :   TestCase.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestCase.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package bka.iam.identity.notification;

import oracle.iam.notification.vo.NotificationEvent;

import oracle.iam.notification.api.NotificationService;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;

////////////////////////////////////////////////////////////////////////////////
// class TestCase
// ~~~~~ ~~~~~~~~
/**
 ** The test case to send notifications.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Identity SN4711123 = new Identity("SN4711123", "Steding", "Dieter", "Dieter Steding");

  static final String[] RECIPIENT = { SN4711123.loginName };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  IdentityServer server;

  //////////////////////////////////////////////////////////////////////////////
  // Memeber classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class Identity
  // ~~~~~ ~~~~~~~~
  static class Identity {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
    
    String loginName;
    String lastName;
    String firstName;
    String displayName;

    public Identity(final String loginName, final String lastName, final String firstName, final String displayName) {
      super();
      this.loginName   = loginName;
      this.lastName    = lastName;
      this.firstName   = firstName;
      this.displayName = displayName;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestCase</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  TestCase() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link IdentityServer} using the appropriate network
   ** environment.
   **
   ** @throws TaskException      if the {@link IdentityServer} could not be
   **                            connect.
   */
  void connect()
    throws TaskException {

    this.server = Network.intranet();
    this.server.connect();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Closes the managed application context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   */
  void disconnect() {
    this.server.disconnect();
    this.server = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sendNotification
  /**
   ** Call notification Engine passing an event object to it.
   **
   ** @param  event              the {@link NotificationEvent} to send.
   **                            <br>
   **                            Allowed object is {@link NotificationEvent}.
   **
   ** @throws NotificationException
   */
  void sendNotification(final NotificationEvent event)
    throws TaskException {
    try {
      final NotificationService service = this.server.service(NotificationService.class);
      service.notify(event);
    }
    catch(Exception e) {
      throw TaskException.unhandled(e);
    }
  }
}