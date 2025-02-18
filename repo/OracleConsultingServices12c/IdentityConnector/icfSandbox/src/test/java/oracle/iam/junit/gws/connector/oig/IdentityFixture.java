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
    Subsystem   :   Oracle Identity Governance SCIM Connector

    File        :   IdentityFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    IdentityFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.connector.oig;

import org.junit.Assert;
import org.junit.BeforeClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.connector.scim.v2.Context;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.resource.SystemBundle;

public class IdentityFixture {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static Context context;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityFixture</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityFixture() {
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
   ** Annotating this method with <code>@BeforeClass</code> causes it to be run
   ** once before any of the test methods in the class.
   **
   ** @throws SystemException    if the authentication/authorization process
   **                            fails.
   */
  @BeforeClass
  public static void beforeClass()
    throws SystemException {

    context = Network.intranet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  /**
   ** Factory method to build an throw a {@link ConnectorException} driven by
   ** the resource bundle.
   ** <p>
   ** The keyword bound to the exception is <code>code</code>.
   ** <br>
   ** The detaild message of the exception thrown is build with a specific
   ** format that allows to parse for the error code and the message text which
   ** belongs to this error code.
   ** <br>
   ** That makes it possible to use the error code inside of an
   ** <code>Adapter</code> and also in the <code>Process Task</code> in Identity
   ** Manager.
   ** <br>
   ** The advantage of this behavior is that at the <code>Process Task</code> a
   ** detailed configuration can be configured how to handle specific error
   ** conditions.
   ** <b>Note</b>:
   ** This method should be called in a connector API implementation only.
   ** <br>
   ** Errors that belongs to the same code should be thrown in the operational
   ** implementation of the conector by an appropriate exception inherited from
   ** {@link SystemException}.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @throws ConnectorException the exception thrown with an appropriate
   **                            explanation of the reason as the detailed
   **                            message of the exception created.
   */
  public static void propagate(final String code, final Object... parameter) {
    // throw the exception with the format of the deatiled message descriced
    // above
    throw new ConnectorException(String.format("%s::%s", code, SystemBundle.string(code, parameter)));
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
  static void failed(final SystemException e) {
    failed(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link ConnectorException} thrown from the
   **                            <code>Connector</code>.
   */
  public static void failed(final ConnectorException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    failed(cause.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link Exception} thrown from the
   **                            <code>Connector</code>.
   */
  public static void failed(final Exception cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    failed(cause.getLocalizedMessage());
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
  static void failed(final String message) {
    Assert.fail(message);
  }
}