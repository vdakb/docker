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
    Subsystem   :   Generic SCIM Library

    File        :   TestErrorResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestErrorResponse.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.identity.icf.rest.domain.Error;
import oracle.iam.identity.icf.scim.schema.Support;

import oracle.iam.identity.icf.scim.response.ErrorResponse;

import oracle.iam.identity.icf.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestErrorResponse
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Test coverage for validate and enforce the error response.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestErrorResponse extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int    STATUS = 400;
  private static final String TYPE   = Error.Type.MUTABILITY.value();
  private static final String DETAIL = "Attribute 'id' is readOnly";
  private static final String SCHEMA = Support.annotatedId(ErrorResponse.class);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestErrorResponse</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestErrorResponse() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = {TestErrorResponse.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testStringStatus
  /**
   ** Confirms that an ErrorResponse can be deserialized when the status field
   ** is a JSON string.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testStringStatus()
      throws Exception {

    final ObjectNode node = (ObjectNode)Support.objectReader().readTree(
      "{\"schemas\": [\"" + SCHEMA + "\"]"
    + ",\"scimType\":\"" + TYPE + "\""
    + ",\"detail\":\"" + DETAIL + "\""
    + ",\"status\":" + STATUS
    + "}"
    );

    final ErrorResponse response = Support.nodeToValue(node, ErrorResponse.class);
    assertEquals(response.status(), Integer.valueOf(STATUS));
    assertEquals(response.type(), TYPE);
    assertEquals(response.detail(), DETAIL);
    assertEquals(response.namespace().size(), 1);
    assertEquals(response.namespace().iterator().next(), SCHEMA);

    final String        serialized   = Support.objectWriter().writeValueAsString(response);
    final ErrorResponse deserialized = Support.objectReader().forType(ErrorResponse.class).readValue(serialized);
    assertEquals(response, deserialized);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testNumberStatus
  /**
   ** Confirms that an {@link ErrorResponse} can be deserialized when the status
   ** field is a JSON number.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testNumberStatus()
      throws Exception {

    final ObjectNode node = (ObjectNode)Support.objectReader().readTree(
      "{\"schemas\":[\"" + SCHEMA + "\"]"
    + ",\"scimType\":\"" + TYPE + "\""
    + ",\"detail\":\"" + DETAIL + "\""
    + ",\"status\":" + STATUS
    + "}"
    );

    final ErrorResponse response = Support.nodeToValue(node, ErrorResponse.class);
    assertEquals(response.status(), Integer.valueOf(STATUS));
    assertEquals(response.type(), TYPE);
    assertEquals(response.detail(), DETAIL);
    assertEquals(response.namespace().size(), 1);
    assertEquals(response.namespace().iterator().next(), SCHEMA);

    final String        serialized   = Support.objectWriter().writeValueAsString(response);
    final ErrorResponse deserialized = Support.objectReader().forType(ErrorResponse.class).readValue(serialized);
    assertEquals(response, deserialized);
  }
}