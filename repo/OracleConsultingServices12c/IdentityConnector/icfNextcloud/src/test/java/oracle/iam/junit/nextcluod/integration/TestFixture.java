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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Nextcloud Connector

    File        :   TestFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    TestFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.nextcloud.integration;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.api.ConnectorFacade;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.foundation.object.Pair;

////////////////////////////////////////////////////////////////////////////////
// class TestFixture
// ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>TestFixture</code> implements the basic functionality of a Test
 ** Case that requires connection to the target system.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestFixture extends TestBaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static ConnectorFacade FACADE;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface User
  // ~~~~~~~~~ ~~~~
  public interface User {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The data provider belonging to Agathe Musterfrau <i>bp4711123</i>.
     */
    static final Pair<String, File> MUSTERFRAU    = Pair.of("bp4711123", new File(RESOURCES,   "gws/account-musterfrau.json"));
    /**
     ** The data provider belonging to Max Mustermann <i>bkbk4711123</i>.
     */
    static final Pair<String, File> MUSTERMANN    = Pair.of("bkbk4711123", new File(RESOURCES, "gws/account-mustermann.json"));
    /**
     ** The data provider belonging to Alfons Zitterbacke <i>an4711123</i>.
     */
    static final Pair<String, File> ZITTERBACKE   = Pair.of("an4711123", new File(RESOURCES,   "gws/account-zitterbacke.json"));
    /**
     ** The data provider belonging to Gerald Cambrault <i>an4711124</i>.
     */
    static final Pair<String, File> CAMBRAULT     = Pair.of("an4711124", new File(RESOURCES,   "gws/account-cambrault.json"));

    static final File              PROVISIONING   = new File(RESOURCES, "mds/gfn-account-provisioning.xml");
    static final File              RECONCILIATION = new File(RESOURCES, "mds/gfn-account-reconciliation.xml");
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Role
  // ~~~~~~~~~ ~~~~
  public interface Role {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final File              PROVISIONING   = new File(RESOURCES, "mds/gfn-role-provisioning.xml");
    static final File              RECONCILIATION = new File("src/test/resources/mds/gfn-role-reconciliation.xml");
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Group
  // ~~~~~~~~~ ~~~~~
  public interface Group {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final File              PROVISIONING   = new File(RESOURCES, "mds/gfn-group-provisioning.xml");
    static final File              RECONCILIATION = new File(RESOURCES, "mds/gfn-group-reconciliation.xml");
  }

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
   ** @throws AssertionException if the network connection could not
   **                            established.
   */
  @BeforeClass
  public static void beforeClass() {
    try {
      FACADE = facade(endpoint());
      assertNotNull(FACADE);
    }
    catch (TaskException e) {
      failed(e);
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
  public static void afterClass() {
    // intentionally left blank
  }
}