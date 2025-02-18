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

    File        :   TestFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestFixture.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.service.api;

import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import oracle.hst.platform.core.SystemException;

import oracle.iam.identity.service.Network;

////////////////////////////////////////////////////////////////////////////////
// class TestFixture
// ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>TestFixture</code> implements the base behavior of a Test Case.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static Client context;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestFixture</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestFixture() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeClass
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
   ** @throws SystemException    if the network connection could not
   **                            established.
   */
  @BeforeClass
  public static void network()
    throws SystemException {

    try {
      context = Network.extranet();
    }
    catch (Throwable t) {
      Network.CONSOLE.fatal(t);
      throw SystemException.general(t);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterClass
  /**
   ** If expensive external resources allocate  in a {@link eforeClass} method
   ** it's required to release them after all the tests in the class have run.
   ** <p>
   ** Annotating a method with with <code>&#064;AfterClass</code> causes it to
   ** be run after all the tests in the class have been run.
   ** <br>
   ** All <code>&#064;AfterClass</code> methods are guaranteed to run even if a
   ** {@link BeforeClass} method throws an exception.
   */
  @AfterClass
  public static void logout() {
    if (context != null)
      context.logout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Fails a test with the given exception.
   **
   ** @param  message            the identifying exception for the
   **                            <code>AssertionError</code>.
   **                            <br>
   **                            Allowed object is {@link SystemException}.
   */
  public static void failed(final SystemException e) {
    failed(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Fails a test with the given message.
   **
   ** @param  message            the identifying message for the
   **                            <code>AssertionError</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static void failed(final String message) {
    Assert.fail(message);
  }
}