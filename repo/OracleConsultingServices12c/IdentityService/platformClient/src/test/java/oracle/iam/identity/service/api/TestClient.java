/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   TestClient.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestClient.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.service.api;

import javax.security.auth.login.LoginException;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import oracle.hst.platform.core.SystemException;

import oracle.iam.identity.service.Network;

////////////////////////////////////////////////////////////////////////////////
// class TestClient
// ~~~~~ ~~~~~~~~~~
/**
 ** The test case belonging to login/logout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestClient extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestClient</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestClient() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login
  /**
   ** Tests need to share computationally expensive setup.
   ** <br>
   ** While this can compromise the independence of tests, it is a necessary
   ** optimization.
   ** <p>
   ** Annotating this method with <code>&#064;BeforeClass</code> causes it to be
   ** run once before any of the test methods in the class.
   ** <p>
   ** The <code>&#064;BeforeClass</code> methods of superclasses will be run
   ** before those of the current class, unless they are shadowed in the current
   ** class.
   **
   ** @throws SystemException    if the authentication/authorization process
   **                            fails.
   */
  @BeforeClass
  public static void login()
    throws SystemException {

    try {
      context.login("xelsysadm", "Sophie20061990$".toCharArray());
    }
    catch (LoginException e) {
      Network.CONSOLE.fatal(e);
      throw SystemException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Test that login is possible.
   */
  @Test
  public void empty() {
    // intentionally left blank
  }
}