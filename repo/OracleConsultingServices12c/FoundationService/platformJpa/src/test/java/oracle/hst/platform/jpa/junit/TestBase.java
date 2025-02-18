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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   JPA Unit Testing

    File        :   TestBase.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestBase.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.junit;

import javax.sql.DataSource;
import java.sql.SQLException;

import org.junit.Assert;

import oracle.jdbc.pool.OracleDataSource;

import oracle.hst.platform.jpa.context.SessionAdapter;
import oracle.hst.platform.jpa.converter.Boolean2Integer;
import oracle.hst.platform.jpa.bootstrap.PersistenceUnit;
import oracle.hst.platform.jpa.bootstrap.DataSourceProxy;

import oracle.hst.platform.jpa.bootstrap.PersistenceBoot;
import oracle.hst.platform.jpa.context.SessionProvider;
import oracle.hst.platform.jpa.junit.model.User;

import org.junit.Before;

////////////////////////////////////////////////////////////////////////////////
// class TestBase
// ~~~~~ ~~~~~~~~
/**
 ** The <code>TestBase</code> implements the basic functionality of a Test Case.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final PersistenceUnit igs = PersistenceUnit
    .instance("igs", Boolean2Integer.class, User.class)
    .nonJtaDataSource(dataSource(true))
    // Log level configuration is included in the definition of the
    // persistence unit in the persistence.xml file, as follows:
    .property("eclipselink.logging.level.sql",      "FINE")
    .property("eclipselink.logging.parameters",     "true")
    // register the adapter that downstream the session context to the
    // connected database
    .property("eclipselink.session-event-listener", SessionAdapter.class.getName())
  ;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected PersistenceBoot provider = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestBase</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestBase() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNull
  /**
   ** Asserts that an object isn't <code>null</code>.
   ** <br>
   ** If it is an {@code AssertionError} is thrown.
   **
   ** @param  object             the {@link Object} to check or
   **                            <code>null</code>
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
   public static void notNull(final Object object) {
     notNull(null, object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNull
  /**
   ** Asserts that an object isn't <code>null</code>.
   ** <br>
   ** If it is an {@code AssertionError} is thrown.
   **
   ** @param  message            the identifying message for the
   **                            {@code AssertionError}.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  object             the {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  public static void notNull(final String message, final Object object) {
    Assert.assertTrue(message, object != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Asserts that two objects are equal.
   ** <br>
   ** If they are not, an {@code AssertionError} without a message is thrown.
   ** <br>
   ** If <code>expected</code> and <code>actual</code> are <code>null</code>,
   ** they are considered equal.
   **
   ** @param  expected           the expected {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
   public static void equals(final Object expected, final Object actual) {
     equals(null, expected, actual);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Asserts that two objects are equal.
   ** <br>
   ** If they are not, an {@code AssertionError} is thrown with the given
   ** message.
   ** <br>
   ** If <code>expected</code> and <code>actual</code> are <code>null</code>,
   ** they are considered equal.
   **
   ** @param  message            the identifying message for the
   **                            {@code AssertionError}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  expected           the expected {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  public static void equals(final String message, final Object expected, final Object actual) {
    Assert.assertEquals(message, expected, actual);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notEquals
  /**
   ** Asserts that two objects are <b>not</b> equals.
   ** <br>
   ** If they are, an {@code AssertionError} without a message is thrown.
   ** <br>
   ** If <code>unexpected</code> and <code>actual</code> are <code>null</code>,
   ** they are considered equal.
   **
   ** @param  unexpected         the unexpected {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  public static void notEquals(final Object unexpected, final Object actual) {
    notEquals(null, unexpected, actual);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notEquals
  /**
   ** Asserts that two objects are <b>not</b> equals.
   ** <br>
   ** If they are, an {@code AssertionError} is thrown with the given message.
   ** <br>
   ** If <code>unexpected</code> and <code>actual</code> are <code>null</code>,
   ** they are considered equal.
   **
   ** @param  message            the identifying message for the
   **                            {@code AssertionError}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  unexpected         the unexpected {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  public static void notEquals(final String message, final Object unexpected, final Object actual) {
    Assert.assertNotEquals(message, unexpected, actual);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Fails a test with the given message.
   **
   ** @param  message            the identifying message for the
   **                            {@link AssertionError}.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static void failed(final String message) {
    Assert.fail(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before
  /**
   ** Aquire an {@link EntityManager} before each test.
   */
  @Before
  public void before() {
    SessionProvider.put(SessionAdapter.CLIENT, "igssysadm");
    this.provider = PersistenceBoot.instance(TestBase.igs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSource
  private static DataSource dataSource(final boolean proxied) {
    final OracleDataSource rootSource;
    try {
      rootSource = new OracleDataSource();
      rootSource.setServerName("laurel.cinnamonstar.net");
      rootSource.setServiceName("mdr.cinnamonstar.net");
      rootSource.setPortNumber(1521);
      rootSource.setDriverType("thin");
      rootSource.setUser("igd_igs");
      rootSource.setPassword("Welcome1");
    }
    catch (SQLException e){
      throw new IllegalStateException(e);
    }
    return proxied ? DataSourceProxy.DSSPY.dataSource(rootSource) : DataSourceProxy.NONE.dataSource(rootSource);
  }
}