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
    Subsystem   :   Grafana Connector

    File        :   TestFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.comc
    Purpose     :   This file implements the interface
                    TestFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.grafana.connector;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.grafana.Context;

import oracle.iam.identity.icf.connector.grafana.schema.User;
import oracle.iam.identity.icf.connector.grafana.schema.Team;
import oracle.iam.identity.icf.connector.grafana.schema.Role;
import oracle.iam.identity.icf.connector.grafana.schema.Organization;

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
public class TestFixture extends TestBaseConnector {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static Context CONTEXT;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Identity
  // ~~~~~~~~~ ~~~~~~~~
  public interface Identity {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

   /**
    ** The data provider belonging to Agathe Musterfrau <i>bp4711123</i>.
    */
   static final Pair<String, File> ADMIN       = Pair.of("admin", new File(RESOURCES,       "account-admin.json"));
   /**
    ** The data provider belonging to Gerald Cambrault <i>an4711124</i>.
    */
   static final Pair<String, File> CAMBRAULT   = Pair.of("an4711124", new File(RESOURCES,   "account-cambrault.json"));
   /**
    ** The data provider belonging to Agathe Musterfrau <i>bp4711123</i>.
    */
   static final Pair<String, File> MUSTERFRAU  = Pair.of("bp4711123", new File(RESOURCES,   "account-musterfrau.json"));
   /**
    ** The data provider belonging to Max Mustermann <i>bkbk4711123</i>.
    */
   static final Pair<String, File> MUSTERMANN  = Pair.of("bkbk4711123", new File(RESOURCES, "account-mustermann.json"));
   /**
    ** The data provider belonging to Alfons Zitterbacke <i>an4711123</i>.
    */
   static final Pair<String, File> ZITTERBACKE = Pair.of("an4711123", new File(RESOURCES,   "account-zitterbacke.json"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Department
  // ~~~~~~~~~ ~~~~~~~~~~
  public interface Department {

   /**
    ** The data provider belonging to default organization <i>Main Org.</i>.
    */
   static final Pair<String, File> MAIN   = Pair.of("Main Org.", new File(RESOURCES, "organization-main.json"));
   /**
    ** The data provider belonging to custom organization <i>DI41</i>.
    */
   static final Pair<String, File> DI41   = Pair.of("DI41",      new File(RESOURCES, "organization-di41.json"));
   /**
    ** The data provider belonging to default organization <i>DI42-1</i>.
    */
   static final Pair<String, File> DI41_1 = Pair.of("DI41-1",    new File(RESOURCES, "organization-di41-1.json"));
   /**
    ** The data provider belonging to default organization <i>DI41-2</i>.
    */
   static final Pair<String, File> DI41_2 = Pair.of("DI41-2",    new File(RESOURCES, "organization-di41-2.json"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Tenant
  // ~~~~~~~~~ ~~~~~~
  public interface Tenant {

   /**
    ** The data provider belonging to team <i>Saxony-Anhalt</i>.
    */
   static final Pair<String, File> AN = Pair.of("Saxony-Anhalt",   new File(RESOURCES, "team-an.json"));
   /**
    ** The data provider belonging to team <i>Brandenburg</i>.
    */
   static final Pair<String, File> BB  = Pair.of("Brandenburg",    new File(RESOURCES, "team-bb.json"));
   /**
    ** The data provider belonging to team <i>Federal Police.</i>.
    */
   static final Pair<String, File> BK  = Pair.of("Federal Criminal Police Office", new File(RESOURCES, "team-bk.json"));
   /**
    ** The data provider belonging to team <i>Federal Police.</i>.
    */
   static final Pair<String, File> BP  = Pair.of("Federal Police", new File(RESOURCES, "team-bp.json"));
   /**
    ** The data provider belonging to team <i>Federal Police.</i>.
    */
   static final Pair<String, File> SN  = Pair.of("Saxony",         new File(RESOURCES, "team-sn.json"));
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
   ** @throws SystemException    if the network connection could not
   **                            established.
   */
  @BeforeClass
  public static void beforeClass()
    throws SystemException {

    try {
      CONTEXT = context();
      assertNotNull(CONTEXT);
    }
    catch (SystemException e) {
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Loads a descriptor and converts it to a {@link User} resource
   ** representation.
   **
   ** @param  path               the path to the data source to load.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    the {@link User} resource representation load
   **                            from <code>path</code>.
   **                            <br>
   **                            Possible object is {@link User}.
   */
  protected User user(final File path)  {
    try {
      // convert JSON file to entity
      return MapperFactory.instance.readValue(path, User.class);
    }
    catch (Exception e) {
      failed(TaskException.unhandled(e));
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Loads a descriptor and converts it to a {@link Team} resource
   ** representation.
   **
   ** @param  path               the path to the data source to load.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    the {@link Team} resource representation load
   **                            from <code>path</code>.
   **                            <br>
   **                            Possible object is {@link Team}.
   */
  protected Team team(final File path)  {
    try {
      // convert JSON file to entity
      return MapperFactory.instance.readValue(path, Team.class);
    }
    catch (Exception e) {
      failed(TaskException.unhandled(e));
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Loads a descriptor and converts it to a {@link Organization} resource
   ** representation.
   **
   ** @param  path               the path to the data source to load.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    the {@link Organization} resource
   **                            representation load from <code>path</code>.
   **                            <br>
   **                            Possible object is {@link Organization}.
   */
  protected Organization organization(final File path)  {
    try {
      // convert JSON file to entity
      return MapperFactory.instance.readValue(path, Organization.class);
    }
    catch (Exception e) {
      failed(TaskException.unhandled(e));
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Loads a descriptor and converts it to a {@link Role} resource
   ** representation.
   **
   ** @param  path               the path to the data source to load.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    the {@link Role} resource representation load
   **                            from <code>path</code>.
   **                            <br>
   **                            Possible object is {@link Role}.
   */
  public Role role(final File path) {
    try {
      // convert JSON file to entity
      return MapperFactory.instance.readValue(path, Role.class);
    }
    catch (Exception e) {
      failed(TaskException.unhandled(e));
      return null;
    }
  }
}