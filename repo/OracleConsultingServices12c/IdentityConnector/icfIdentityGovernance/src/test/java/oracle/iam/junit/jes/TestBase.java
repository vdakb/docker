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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Identity Governance Provisioning

    File        :   TestBase.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestBase.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes;

import java.util.Map;
import java.util.HashMap;

import java.util.stream.Stream;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import org.junit.Assert;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

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

  /**
   ** The location of the configuration file the Test Cases data are populated
   ** from.
   */
  static final File RESOURCES = new File("src/test/resources");

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
  // Method:   data
  /**
   ** Load a text file’s content and convert it into a {@link Map}.
   **
   ** @param  path               the relative path to the file to load from
   **                            the resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Map} providing the configuration
   **                            key-value pairs.
   **
   ** @throws TaskException      if an I/O error occurs.
   */
  public static Map<String, Object> data(final String path)
    throws TaskException {

    final Map<String, Object> map = new HashMap<>();
    try (Stream<String> lines = Files.lines(new File(RESOURCES, path).toPath())) {
      lines.filter(
        line -> line.contains(":")).forEach(line -> {
          final String[] pair = line.split(":", 2);
          map.put(pair[0].trim(), pair[1].trim());
        }
      );
      return map;
    }
    catch (NoSuchFileException e) {
      throw TaskException.abort(TaskBundle.format(TaskError.ITRESOURCE_NOT_FOUND, path));
    }
    catch (IOException e) {
      throw TaskException.unhandled(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNotNull
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
   public static void assertNotNull(final Object object) {
     assertNotNull(null, object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNotNull
  /**
   ** Asserts that an object isn't <code>null</code>.
   ** <br>
   ** If it is not an {@code AssertionError} is thrown.
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
  public static void assertNotNull(final String message, final Object object) {
    assertTrue(message, object != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertTrue
  /**
   ** Asserts that a <code>boolean</code> is <code>true</code>.
   ** <br>
   ** If it is not an {@code AssertionError} is thrown.
   **
   ** @param  value              the <code>boolean</code> value to check.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the <code>boolean</code> value to check.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the <code>boolean</code> value to check.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public static void assertFalse(final String message, final boolean value) {
    Assert.assertFalse(message, value);
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
   ** @param  expected           the expected {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
   public static void assertEquals(final Object expected, final Object actual) {
     assertEquals(null, expected, actual);
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
  public static void assertEquals(final String message, final Object expected, final Object actual) {
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
   ** @param  unexpected         the unexpected {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  actual             the actual {@link Object} to check or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  public static void assertNotEquals(final Object unexpected, final Object actual) {
    assertNotEquals(null, unexpected, actual);
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
  public static void assertNotEquals(final String message, final Object unexpected, final Object actual) {
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
}