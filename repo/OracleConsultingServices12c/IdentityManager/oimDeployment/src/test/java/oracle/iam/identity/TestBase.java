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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   TestBase.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestBase.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

////////////////////////////////////////////////////////////////////////////////
// class TestBase
// ~~~~~ ~~~~~~~~
/**
 ** The <code>TestBase</code> implements the basic functionality of a Test Case.
 ** <p>
 ** Implemented by an extra class to keep it outside the test case classes
 ** itself.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The location of the configuration file the Test Cases data are populated
   ** from.
   */
  protected static final File RESOURCES = new File("src/test/resources");

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
  // Method:   assertNull
  /**
   ** Asserts that an object is <code>null</code>.
   ** <br>
   ** If it is not <code>null</code> an {@code AssertionError} is thrown.
   **
   ** @param  object             the {@link Object} to check for
   **                            <code>null</code>.
   ** @return                    the <code>SystemException</code> created.
   */
   public static void assertNull(final Object object) {
     assertNull(null, object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNull
  /**
   ** Asserts that an object is <code>null</code>.
   ** <br>
   ** If it is not <code>null</code> an {@code AssertionError} is thrown.
   **
   ** @param  message            the identifying message for the
   **                            {@code AssertionError}.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  object             the {@link Object} to check for
   **                            <code>null</code>.
   */
   public static void assertNull(final String message, final Object object) {
     Assert.assertNull(message, object);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNotNull
  /**
   ** Asserts that an object isn't <code>null</code>.
   ** <br>
   ** If it is <code>null</code> an {@code AssertionError} is thrown.
   **
   ** @param  object             the {@link Object} to check for
   **                            <code>null</code>.
   */
   public static void assertNotNull(final Object object) {
     assertNotNull(null, object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNotNull
  /**
   ** Asserts that an object isn't <code>null</code>.
   ** <br>
   ** If it is <code>null</code> an {@code AssertionError} with the specified
   ** <code>message</code> is thrown.
   **
   ** @param  message            the identifying message for the
   **                            {@code AssertionError}.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  object             the {@link Object} to check for
   **                            <code>null</code>.
   */
  public static void assertNotNull(final String message, final Object object) {
    Assert.assertNotNull(message, object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertTrue
  /**
   ** Asserts that a <code>boolean</code> is <code>true</code>.
   ** <br>
   ** If it is not an {@code AssertionError} is thrown.
   **
   ** @param  value              the <code>boolean</code> value to check.
   */
  public static void assertTrue(final boolean value) {
    assertTrue(null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertTrue
  /**
   ** Asserts that a <code>boolean</code> is <code>true</code>.
   ** <br>
   ** If it is not an {@code AssertionError} is thrown.
   **
   ** @param  message            the identifying message for the
   **                            {@code AssertionError}.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  value              the <code>boolean</code> value to check.
   */
  public static void assertTrue(final String message, final boolean value) {
    Assert.assertTrue(message, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertFalse
  /**
   ** Asserts that a <code>boolean</code> is <code>false</code>.
   ** <br>
   ** If it is not an {@code AssertionError} is thrown.
   ** @param  value              the <code>boolean</code> value to check.
   */
  public static void assertFalse(final boolean value) {
    assertFalse(null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertFalse
  /**
   ** Asserts that a <code>boolean</code> is <code>false</code>.
   ** <br>
   ** If it is not an {@code AssertionError} is thrown.
   **
   ** @param  message            the identifying message for the
   **                            {@code AssertionError}.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  value              the <code>boolean</code> value to check.
   */
  public static void assertFalse(final String message, final boolean value) {
    Assert.assertFalse(message, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertSame
  /**
   ** Asserts that two objects do refer to the same object.
   ** <br>
   ** If they do not refer to the same object, an {@link AssertionError} without
   ** a message.
   **
   ** @param  actual             the actual {@link Object} to compare to
   **                            <code>expected</code>.
   ** @param  expected           the expected {@link Object} to compare to
   **                            <code>actual</code>.
   */
  static public void assertSame(Object actual, Object expected) {
    assertSame(null, actual, expected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertSame
  /**
   ** Asserts that two objects do refer to the same object.
   ** <br>
   ** If they do not refer to the same object, an {@link AssertionError} is
   ** thrown with the given message.
   **
   ** @param  message            the identifying message for the
   **                            {@code AssertionError}.
   ** @param  actual             the actual {@link Object} to compare to
   **                            <code>expected</code>.
   ** @param  expected           the expected {@link Object} to compare to
   **                            <code>actual</code>.
   */
  static public void assertSame(String message, Object actual, Object expected) {
    Assert.assertSame(message, expected, actual);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNotSame
  /**
   ** Asserts that two objects do not refer to the same object. If they do refer
   ** to the same object, an {@link AssertionError} without a message.
   **
   ** @param  actual             the actual {@link Object} to compare to
   **                            <code>unexpected</code>.
   ** @param  unexpected         the unexpected {@link Object} you don't expect.
   */
  static public void assertNotSame(Object actual, Object unexpected) {
    assertNotSame(null, actual, unexpected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNotSame
  /**
   ** Asserts that two objects do not refer to the same object. If they do refer
   ** to the same object, an {@link AssertionError} is thrown with the given
   ** message.
   **
   ** @param  message            the identifying message for the
   **                            {@code AssertionError}.
   ** @param  actual             the actual {@link Object} to compare to
   **                            <code>unexpected</code>.
   ** @param  unexpected         the unexpected {@link Object} you don't expect.
   */
  static public void assertNotSame(String message, Object actual, Object unexpected) {
    Assert.assertNotSame(message, unexpected, actual);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertEquals
  /**
   ** Asserts that two objects are equal.
   ** <br>
   ** If they are not, an {@code AssertionError} without a message is thrown.
   ** <br>
   ** If <code>expected</code> and <code>actual</code> are <code>null</code>,
   ** they are considered equal.
   **
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   ** @param  expected           the expected {@link Object} to check or
   **                            <code>null</code>.
   */
   public static void assertEquals(final Object actual, final Object expected) {
     assertEquals(null, actual, expected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertEquals
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
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   ** @param  expected           the expected {@link Object} to check or
   **                            <code>null</code>.
   */
  public static void assertEquals(final String message, final Object actual, final Object expected) {
    Assert.assertEquals(message, expected, actual);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNotEquals
  /**
   ** Asserts that two objects are <b>not</b> equals.
   ** <br>
   ** If they are, an {@code AssertionError} without a message is thrown.
   ** <br>
   ** If <code>unexpected</code> and <code>actual</code> are <code>null</code>,
   ** they are considered equal.
   **
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   ** @param  unexpected         the unexpected {@link Object} to check or
   **                            <code>null</code>.
   */
  public static void assertNotEquals(final Object actual, final Object unexpected) {
    assertNotEquals(null, actual, unexpected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNotEquals
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
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   ** @param  unexpected         the unexpected {@link Object} to check or
   **                            <code>null</code>.
   */
  public static void assertNotEquals(final String message, final Object actual, final Object unexpected) {
    Assert.assertNotEquals(message, unexpected, actual);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Fails a test with the given message.
   **
   ** @param  t                  a throwable exception.
   **                            <br>
   **                            Must not be <code>null</code>.
   */
  public static void failed(final Throwable t) {
    failed(t.getMessage());
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
   */
  public static void failed(final String message) {
    Assert.fail(message);
  }
}