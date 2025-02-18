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
    Subsystem   :   Test Case Skeleton

    File        :   TestCaseBasic.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestCaseBasic.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit;

import org.junit.Assert;

////////////////////////////////////////////////////////////////////////////////
// class TestCaseBasic
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The basic test case providing common functionality used leveraging the
 ** either <code>Connector Bundle</code> or <code>Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestCaseBasic {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the size of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String BATCH_SIZE         = "batchSize";
  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the start index of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String BATCH_START        = "batchStart";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search base(s) of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String SEARCH_BASE        = "searchBase";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search filter of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String SEARCH_FILTER      = "searchFilter";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search order of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String SEARCH_ORDER       = "searchOrder";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to obtain entries from the Service Provider incrementally means based
   ** on a synchronization token like <code>changeNumber</code> of timestamps.
   */
  public static final String INCREMENTAL        = "incremental";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the synchronization strategy of
   ** resources returned from a <code>Service Provider</code>.
   */
  public static final String SYNCHRONIZE        = "synchronize";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to specifiy the value of a synchronization token.
   */
  public static final String SYNCHRONIZE_TOKEN  = "synchronizeToken";

  /**
   ** The default start inedx of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Identity Manager.
   */
  static final int           BATCH_START_DEFAULT = 1;
  /**
   ** The default size limit of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Identity Manager.
   */
  static final int           BATCH_SIZE_DEFAULT  = 500;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestCaseBasic</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected TestCaseBasic() {
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
}