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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   TestModify.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestModify.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.pcf;

import oracle.iam.identity.foundation.TaskException;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ObjectClass;

////////////////////////////////////////////////////////////////////////////////
// class TestModify
// ~~~~~ ~~~~~~~~~~
/**
 ** The test case to modify an entry in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestModify extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    try {
      uid = Network.facade(Network.intranet()).update(ObjectClass.ACCOUNT, uid, MODIFY, null);
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      final String   message = e.getLocalizedMessage();
      final String[] parts   = message.split("::");
      if (parts.length > 1)
        System.out.println(parts[0].concat("::").concat(parts[1]));
      else
        System.out.println(message);
    }
    catch (TaskException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }
}