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

    Author      :   dieter.steding@icloud.comc
    Purpose     :   This file implements the interface
                    TestFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.nextcloud.connector;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.nextcloud.Context;

import oracle.iam.identity.icf.connector.nextcloud.schema.User;
import oracle.iam.identity.icf.connector.nextcloud.schema.Role;

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
   static final Pair<String, File> ADMIN       = Pair.of("admin", new File(RESOURCES, "admin.json"));
   /**
    ** The data provider belonging to Agathe Musterfrau <i>bp4711123</i>.
    */
   static final Pair<String, File> MUSTERFRAU  = Pair.of("bp4711123", new File(RESOURCES, "account-musterfrau.json"));
   /**
    ** The data provider belonging to Max Mustermann <i>bkbk4711123</i>.
    */
   static final Pair<String, File> MUSTERMANN  = Pair.of("bkbk4711123", new File(RESOURCES, "account-mustermann.json"));
   /**
    ** The data provider belonging to Alfons Zitterbacke <i>an4711123</i>.
    */
   static final Pair<String, File> ZITTERBACKE = Pair.of("an4711123", new File(RESOURCES, "account-zitterbacke.json"));
   /**
    ** The data provider belonging to Gerald Cambrault <i>an4711124</i>.
    */
   static final Pair<String, File> CAMBRAULT   = Pair.of("an4711124", new File(RESOURCES, "account-cambrault.json"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Action
  // ~~~~~~~~~ ~~~~~~~
  public interface Action {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The data provider belonging to required action
     ** <i>VERIFY_EMAIL</i>.
     */
    static final Pair<String, String> VE = Pair.of("VERIFY_EMAIL", "Verify Email");
    /**
     ** The data provider belonging to required action
     ** <i>UPDATE_EMAIL</i>.
     */
    static final Pair<String, String> UE = Pair.of("UPDATE_EMAIL", "Update Email");
    /**
     ** The data provider belonging to required action
     ** <i>UPDATE_PROFILE</i>.
     */
    static final Pair<String, String> UP = Pair.of("UPDATE_PROFILE", "Update Profile");
    /**
     ** The data provider belonging to required action
     ** <i>CONFIGURE_TOTP</i>.
     */
    static final Pair<String, String> CO = Pair.of("CONFIGURE_TOTP", "Configure OTP");
    /**
     ** The data provider belonging to required action
     ** <i>TERMS_AND_CONDITIONS</i>.
     */
    static final Pair<String, String> TC = Pair.of("TERMS_AND_CONDITIONS", "Terms and Conditions");
    /**
     ** The data provider belonging to required action
     ** <i>UPDATE_PASSWORD</i>.
     */
    static final Pair<String, String> LP = Pair.of("UPDATE_PASSWORD", "Update Password");
    /**
     ** The data provider belonging to required action
     ** <i>update_user_locale</i>.
     */
    static final Pair<String, String> UL = Pair.of("update_user_locale", "Update User Locale");
    /**
     ** The data provider belonging to required action
     ** <i>update_user_locale</i>.
     */
    static final Pair<String, String> WR = Pair.of("webauthn-register", "Webauthn Register");
    /**
     ** The data provider belonging to required action
     ** <i>update_user_locale</i>.
     */
    static final Pair<String, String> WP = Pair.of("webauthn-register-passwordless", "Webauthn Register Passwordless");
  }
  //////////////////////////////////////////////////////////////////////////////
  // interface RealmGroup
  // ~~~~~~~~~ ~~~~~~~~~~
  public interface RealmGroup {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The data provider belonging to custom group <i>pid-admin</i>.
     */
    static final Pair<String, File> PID = Pair.of("pid-admin", new File(RESOURCES, "group-pid-admin.json"));
    /**
     ** The data provider belonging to custom group <i>uid-admin</i>.
     */
    static final Pair<String, File> UID = Pair.of("uid-admin", new File(RESOURCES, "group-uid-admin.json"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface RealmRole
  // ~~~~~~~~~ ~~~~~~~~~
  public interface RealmRole {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The data provider belonging to default role <i>default-realm-myrealm</i>.
     */
    static final Pair<String, File> DEFAULT = Pair.of("default-roles-myrealm", new File(RESOURCES, "role-default.json"));
    /**
     ** The data provider belonging to default role <i>offline_access</i>.
     */
    static final Pair<String, File> OFFLINE = Pair.of("offline_access", new File(RESOURCES, "role-offline.json"));
    /**
     ** The data provider belonging to default role <i>uma-authorization</i>.
     */
    static final Pair<String, File> AUTHZ  = Pair.of("uma_authorization", new File(RESOURCES, "role-authorization.json"));
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
  // Method:   user
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