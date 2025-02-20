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

    File        :   TestPasswordGenerated.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestPasswordGenerated.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package bka.iam.identity.notification;

import java.util.HashMap;

import oracle.iam.passwordmgmt.vo.Constants;

import oracle.iam.notification.vo.NotificationEvent;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class TestPasswordGenerated
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to send notification about password expired.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestPasswordGenerated extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String TEMPLATE  = "bka-password-generated";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestPasswordExpired</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  TestPasswordGenerated() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    final HashMap<String, Object> parameter = new HashMap<>();
    parameter.put(Constants.USER_ID,       SN4711123.loginName);
    parameter.put(Constants.FIRST_NAME,    SN4711123.firstName);
    parameter.put(Constants.LAST_NAME,     SN4711123.lastName);
    parameter.put(Constants.DISPLAY_NAME,  SN4711123.displayName);
    parameter.put(Constants.USER_PASSWORD, "Tabaluga");

    final NotificationEvent event = new NotificationEvent();
    event.setSender(null);
    event.setParams(parameter);
    event.setUserIds(RECIPIENT);
    event.setTemplateName(TEMPLATE);

    final TestPasswordExpired test = new TestPasswordExpired();
    try {
      test.connect();
      test.sendNotification(event);
    }
    catch(TaskException e) {
      e.printStackTrace();
    }
    finally {
      test.disconnect();
    }
  }
}