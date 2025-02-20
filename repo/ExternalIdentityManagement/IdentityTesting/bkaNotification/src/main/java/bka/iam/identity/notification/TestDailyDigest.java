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

    File        :   TestDailyDigest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestDailyDigest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package bka.iam.identity.notification;

import java.util.HashMap;

import oracle.iam.notification.vo.NotificationEvent;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class TestDailyDigest
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The test case to send notification about password expires.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestDailyDigest extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String TEMPLATE  = "bka-systemhealth-digest";

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
  TestDailyDigest() {
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
    final HashMap<String, Object> metric    = new HashMap<>();
    metric.put("openTask",             new Integer(78889));
    metric.put("nouserMatchFound",     new Integer(4711));
    metric.put("dataValidationFailed", new Integer(12));
    metric.put("failedJob",            new Integer(68));
    metric.put("failedOrchestration",  new Integer(677445));
    final HashMap<String, Object> parameter = new HashMap<>();
    parameter.put("User",               "1");
    parameter.put("metric",             metric);

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