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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   TestPatchGroup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestPatchGroup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.dto;

import org.junit.Test;

import java.net.URI;

import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.junit.TestBase;

import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.request.PatchRequest;
import oracle.iam.platform.scim.request.PatchOperation;

////////////////////////////////////////////////////////////////////////////////
// class TestPatchGroup
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Test Cases for patch operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestPatchGroup extends TestBase {

  public static class Add {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Long   value;
    @JsonProperty("display")
    private String display;
    @JsonProperty("$ref")
    private URI    ref;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Add</code> SCIM Resource that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Add() {
 	    // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestPatchGroup</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestPatchGroup() {
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
    final String[] parameter = { TestPatchGroup.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testCompleteEntity
  /**
   ** Test modify a complete entity.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testCompleteEntity()
    throws Exception {

/*
    final PatchRequest request = Support.objectReader().forType(PatchRequest.class).readValue(
      "{}"
    );
    equals(request.operation().size(), 0);
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAssignSingleUser
  /**
   ** Test assign a single user as a member.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testAssignSingleUser()
    throws Exception {

    final PatchRequest request = Support.objectReader().forType(PatchRequest.class).readValue(
      "{ \"schemas\"    : [\"urn:ietf:params:scim:api:messages:2.0:PatchOp\"]\n" + 
      ", \"Operations\" : [\n" + 
      "    { \"op\"     : \"add\"\n" + 
      "    , \"path\"   : \"members\"\n" + 
      "    , \"value\"  :  [\n" + 
      "        { \"type\"    : \"User\"\n" + 
      "        , \"value\"   : 10\n" + 
      "        , \"display\" : \"bk4711127\"\n" + 
      "        , \"$ref\"    : \"https://example.com/v2/Users/10\"\n" + 
      "        }\n" + 
      "      ]\n" + 
      "    }\n" + 
      "  ]\n" + 
      "}"
    );
    // verify schema definition
    assertEquals(request.namespace().size(), 1);
    assertEquals(request.namespace().iterator().next(), "urn:ietf:params:scim:api:messages:2.0:PatchOp");
    // verify operations
    assertEquals(request.operation().size(), 1);
    
    final PatchOperation op = request.operation().get(0);
    assertEquals(op.path().namespace(), null);
    assertEquals(op.type(),             PatchOperation.Type.ADD);
    final JsonNode node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 1);
    
    final Add user = Support.objectMapper().treeToValue(node.get(0), Add.class);
    assertNotNull(user);
    assertNotNull(user.ref);
    assertNotNull(user.type);
    assertNotNull(user.value);
    assertNotNull(user.display);
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAssignMultipleUser
  /**
   ** Test assign a multiple users as a member.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testAssignMultipleUser()
    throws Exception {
/*
    final PatchRequest request = Support.objectReader().forType(PatchRequest.class).readValue(
      "{}"
    );
    equals(request.operation().size(), 0);
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRevokeSingleUser
  /**
   ** Test revoke a single user as a member.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testRevokeSingleUser()
    throws Exception {
/*
    final PatchRequest request = Support.objectReader().forType(PatchRequest.class).readValue(
      "{}"
    );
    equals(request.operation().size(), 0);
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRevokeMultipleUser
  /**
   ** Test revoke a multiple users as a member.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testRevokeMultipleUser()
    throws Exception {
/*
    final PatchRequest request = Support.objectReader().forType(PatchRequest.class).readValue(
      "{}"
    );
    equals(request.operation().size(), 0);
*/
  }
}