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

    File        :   TestPatchOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestPatchOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.v2;

import java.util.Date;
import java.util.Arrays;

import java.io.IOException;

import java.net.URI;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.rest.ServiceException;

import oracle.iam.platform.scim.BadRequestException;

import oracle.iam.platform.scim.entity.Path;

import oracle.iam.platform.scim.schema.Generic;
import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.request.PatchRequest;
import oracle.iam.platform.scim.request.PatchOperation;

////////////////////////////////////////////////////////////////////////////////
// class TestPatchOperation
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Test coverage for patch operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestPatchOperation extends TestResource {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestPatchOperation</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestPatchOperation() {
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
    final String[] parameter = { TestPatchOperation.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRequest
  /**
   ** Test patch request.
   */
  @Test
  public void testRequest() {
    try {
      final PatchRequest patch     = stream("patch-full-representation.json", PatchRequest.class);
      final JsonNode     prePatch  = Support.objectReader().readTree(stream("patch-full-prepatch.json"));
      final Generic      resource  = Generic.build((ObjectNode)prePatch);
      patch.apply(resource);

      final JsonNode     postPatch = Support.objectReader().readTree(stream("patch-full-postpatch.json"));
      assertEquals(resource.objectNode(), postPatch);
    }
    catch (ServiceException | JsonProcessingException e) {
      failed(e);
    }
    catch (IOException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testPathLess
  /**
   ** Test patch request that has no path but whole values.
   */
  @Test
  public void testPathLess() {
    try {
      final PatchRequest patch     = stream("patch-user-email-without-path.json", PatchRequest.class);
      final JsonNode     prePatch  = Support.objectReader().readTree(stream("patch-user-email-prepatch.json"));
      final Generic      resource  = Generic.build((ObjectNode)prePatch);
      patch.apply(resource);

      final JsonNode     postPatch = Support.objectReader().readTree(stream("patch-user-email-postpatch.json"));
      assertEquals(resource.objectNode(), postPatch);
    }
    catch (ServiceException | JsonProcessingException e) {
      failed(e);
    }
    catch (IOException e) {
      failed(e);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   testBadRequest
  /**
   ** Test bad patch requests.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testBadRequest()
    throws Exception {

    try {
      stream("patch-user-email-badrequest.json", PatchRequest.class);
    }
    catch (JsonMappingException e) {
      assertEquals(((BadRequestException)e.getCause()).error().type(), BadRequestException.INVALID_PATH);
    }

    try {
      stream("patch-user-email-subattribute.json", PatchRequest.class);
    }
    catch (JsonMappingException e) {
      assertEquals(((BadRequestException)e.getCause()).error().type(), BadRequestException.INVALID_PATH);
    }

    try {
      stream("patch-user-email-badrequest.json", PatchRequest.class);
    }
    catch (JsonMappingException e) {
      assertEquals(((BadRequestException)e.getCause()).error().type(), BadRequestException.INVALID_PATH);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testBoolean
  /**
   ** Test string methods.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testBoolean()
    throws Exception {

    PatchOperation op = PatchOperation.addBoolean("pathToBool", CollectionUtility.list(Boolean.TRUE, Boolean.FALSE));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    JsonNode node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).booleanValue(), Boolean.TRUE.booleanValue());
    assertEquals(node.get(1).booleanValue(), Boolean.FALSE.booleanValue());
    assertEquals(op.path(), Path.from("pathToBool"));

    op = PatchOperation.addBoolean(Path.from("pathToBool"), CollectionUtility.list(Boolean.FALSE, Boolean.TRUE));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).booleanValue(), Boolean.FALSE.booleanValue());
    assertEquals(node.get(1).booleanValue(), Boolean.TRUE.booleanValue());
    assertEquals(op.path(), Path.from("pathToBool"));

    op = PatchOperation.replace("pathToBool", Boolean.TRUE);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(Boolean.class), Boolean.TRUE);
    assertEquals(op.path(), Path.from("pathToBool"));

    op = PatchOperation.replace(Path.from("pathToBool"), Boolean.FALSE);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(Boolean.class), Boolean.FALSE);
    assertEquals(op.path(), Path.from("pathToBool"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testInteger
  /**
   ** Test string methods.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testInteger()
    throws Exception {

    PatchOperation op = PatchOperation.addInteger("pathToInteger", CollectionUtility.list(1, 2));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    JsonNode node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).intValue(), 1);
    assertEquals(node.get(1).intValue(), 2);
    assertEquals(op.path(), Path.from("pathToInteger"));
    
    op = PatchOperation.addInteger(Path.from("pathToInteger"), CollectionUtility.list(3, 4));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).intValue(), 3);
    assertEquals(node.get(1).intValue(), 4);
    assertEquals(op.path(), Path.from("pathToInteger"));

    op = PatchOperation.replace("pathToInteger", 5);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(Integer.class).intValue(), 5);
    assertEquals(op.path(), Path.from("pathToInteger"));

    op = PatchOperation.replace(Path.from("pathToInteger"), 6);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(Integer.class).intValue(), 6);
    assertEquals(op.path(), Path.from("pathToInteger"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testLong
  /**
   ** Test string methods.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testLong()
    throws Exception {

    PatchOperation op = PatchOperation.addLong("pathToLong", CollectionUtility.list(1L, 2L));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    JsonNode node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).longValue(), 1L);
    assertEquals(node.get(1).longValue(), 2L);
    assertEquals(op.path(), Path.from("pathToLong"));

    op = PatchOperation.addLong(Path.from("pathToLong"), CollectionUtility.list(3L, 4L));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).longValue(), 3L);
    assertEquals(node.get(1).longValue(), 4L);
    assertEquals(op.path(), Path.from("pathToLong"));

    op = PatchOperation.replace("pathToLong", 5L);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(Long.class).longValue(), 5L);
    assertEquals(op.path(), Path.from("pathToLong"));

    op = PatchOperation.replace(Path.from("pathToLong"), 6L);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(Long.class).longValue(), 6L);
    assertEquals(op.path(), Path.from("pathToLong"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testDouble
  /**
   ** Test string methods.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testDouble()
    throws Exception {

    PatchOperation op = PatchOperation.addDouble("pathToDouble", CollectionUtility.list(1.1, 1.2));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    JsonNode node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).doubleValue(), 1.1, 0.01);
    assertEquals(node.get(1).doubleValue(), 1.2, 0.01);
    assertEquals(op.path(), Path.from("pathToDouble"));

    op = PatchOperation.addDouble(Path.from("pathToDouble"), CollectionUtility.list(2.1, 2.2));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).doubleValue(), 2.1, 0.01);
    assertEquals(node.get(1).doubleValue(), 2.2, 0.01);
    assertEquals(op.path(), Path.from("pathToDouble"));

    op = PatchOperation.replace("pathToDouble", 734.2);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(Double.class), 734.2, 0.01);
    assertEquals(op.path(), Path.from("pathToDouble"));

    op = PatchOperation.replace(Path.from("pathToDouble"), 0.3);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(Double.class), 0.3, 0.01);
    assertEquals(op.path(), Path.from("pathToDouble"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testDate
  /**
   ** Test date methods.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testDate()
    throws Exception {

    Date d1 = new Date(89233675234L);
    Date d2 = new Date(89233675235L);
    Date d3 = new Date(89233675236L);
    Date d4 = new Date(89233675237L);
    Date d5 = new Date(89233675238L);
    Date d6 = new Date(89233675240L);

    PatchOperation op = PatchOperation.addDate("pathToDate", CollectionUtility.list(d1, d2));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    JsonNode node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
//    assertEquals(Generic.dateValue(node.get(0)), d1);
    assertEquals(op.values(String.class).get(0), Support.valueToNode(d1).textValue());
//    assertEquals(Generic.dateValue(node.get(1)), d2);
    assertEquals(op.values(String.class).get(1), Support.valueToNode(d2).textValue());
    assertEquals(op.path(), Path.from("pathToDate"));

    op = PatchOperation.addDate(Path.from("pathToDate"), CollectionUtility.list(d3, d4));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
//    assertEquals(Generic.dateValue(node.get(0)), d3);
    assertEquals(op.values(String.class).get(0), Support.valueToNode(d3).textValue());
//    assertEquals(Generic.dateValue(node.get(1)), d4);
    assertEquals(op.values(String.class).get(1), Support.valueToNode(d4).textValue());
    assertEquals(op.path(), Path.from("pathToDate"));

    op = PatchOperation.replace("pathToDate", d5);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(String.class), Support.valueToNode(d5).textValue());
    assertEquals(op.path(), Path.from("pathToDate"));

    op = PatchOperation.replace(Path.from("pathToDate"), d6);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(String.class), Support.valueToNode(d6).textValue());
    assertEquals(op.path(), Path.from("pathToDate"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testString
  /**
   ** Test string methods.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testString()
    throws Exception {

    PatchOperation op = PatchOperation.addString("pathToString", CollectionUtility.list("value1", "value2"));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    JsonNode node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).textValue(), "value1");
    assertEquals(node.get(1).textValue(), "value2");
    assertEquals(op.path(), Path.from("pathToString"));

    op = PatchOperation.addString(Path.from("pathToString"), CollectionUtility.list("value1", "value2"));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).textValue(), "value1");
    assertEquals(node.get(1).textValue(), "value2");
    assertEquals(op.path(), Path.from("pathToString"));

    op = PatchOperation.replace("pathToString", "value1");
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(String.class), "value1");
    assertEquals(op.path(), Path.from("pathToString"));

    op = PatchOperation.replace(Path.from("pathToString"), "value1");
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(String.class), "value1");
    assertEquals(op.path(), Path.from("pathToString"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testBinary
  /**
   ** Test binary methods.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testBinary()
    throws Exception {

    byte[] ba1 = new byte[] {0x00, 0x01, 0x02};
    byte[] ba2 = new byte[] {0x02, 0x01, 0x00};
    byte[] ba3 = new byte[] {0x03, 0x04, 0x05};
    byte[] ba4 = new byte[] {0x05, 0x04, 0x03};
    byte[] ba5 = new byte[] {0x06, 0x07, 0x08};
    byte[] ba6 = new byte[] {0x09, 0x0a, 0x0b};

    PatchOperation op = PatchOperation.addBinary("pathToBinary", CollectionUtility.list(ba1, ba2));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    JsonNode node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertTrue(Arrays.equals(Base64Variants.getDefaultVariant().decode(node.get(0).textValue()), ba1));
    assertTrue(Arrays.equals(Base64Variants.getDefaultVariant().decode(node.get(1).textValue()), ba2));
    assertEquals(op.path(), Path.from("pathToBinary"));

    op = PatchOperation.addBinary(Path.from("pathToBinary"), CollectionUtility.list(ba3, ba4));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertTrue(Arrays.equals(Base64Variants.getDefaultVariant().decode(node.get(0).textValue()), ba3));
    assertTrue(Arrays.equals(Base64Variants.getDefaultVariant().decode(node.get(1).textValue()), ba4));
    assertEquals(op.path(), Path.from("pathToBinary"));

    op = PatchOperation.replace("pathToBinary", ba5);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertTrue(Arrays.equals(op.value(byte[].class), ba5));
    assertEquals(op.path(), Path.from("pathToBinary"));

    op = PatchOperation.replace(Path.from("pathToBinary"), ba6);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertTrue(Arrays.equals(op.value(byte[].class), ba6));
    assertEquals(op.path(), Path.from("pathToBinary"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testURI
  /**
   ** Test URI methods.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testURI()
    throws Exception {

    URI uri1 = new URI("http://localhost:8080/apps/app1");
    URI uri2 = new URI("Users/1dd6d752-1744-47e5-a4a8-5f5670aa8905");
    URI uri3 = new URI("http://localhost:8080/apps/app2");
    URI uri4 = new URI("Users/1dd6d752-1744-47e5-a4a8-5f5670aa8998");
    URI uri5 = new URI("http://localhost:8080/apps/app3");
    URI uri6 = new URI("http://localhost:8080/apps/app4");

    PatchOperation op = PatchOperation.addURI("pathToURI", CollectionUtility.list(uri1, uri2));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    JsonNode node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).textValue(), uri1.toString());
    assertEquals(node.get(1).textValue(), uri2.toString());
    assertEquals(op.path(), Path.from("pathToURI"));

    op = PatchOperation.addURI(Path.from("pathToURI"), CollectionUtility.list(uri3, uri4));
    assertEquals(op.type(), PatchOperation.Type.ADD);
    node = op.node();
    assertTrue(node.isArray());
    assertEquals(node.size(), 2);
    assertEquals(node.get(0).textValue(), uri3.toString());
    assertEquals(node.get(1).textValue(), uri4.toString());
    assertEquals(op.path(), Path.from("pathToURI"));

    op = PatchOperation.replace("pathToURI", uri5);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(String.class), uri5.toString());
    assertEquals(op.path(), Path.from("pathToURI"));

    op = PatchOperation.replace(Path.from("pathToURI"), uri6);
    assertEquals(op.type(), PatchOperation.Type.REPLACE);
    assertEquals(op.value(String.class), uri6.toString());
    assertEquals(op.path(), Path.from("pathToURI"));
  }

  @Test
  public void testTenantAssign()
    throws Exception {

    final PatchRequest   request   = stream("patch-tenant-roles-assign.json", PatchRequest.class);
    final JsonNode       prePatch  = Support.objectReader().readTree(stream("patch-tenant-prepatch.json"));
    final JsonNode       postPatch = Support.objectReader().readTree(stream("patch-tenant-postpatch.json"));
    final Generic        resource  = Generic.build((ObjectNode)prePatch);
    request.apply(resource);

    assertEquals(resource.objectNode(), postPatch);
  }

  @Test
  public void testTenantRevoke()
    throws Exception {

    final PatchRequest request   = stream("patch-tenant-roles-revoke.json", PatchRequest.class);
    final JsonNode     prePatch  = Support.objectReader().readTree(stream("patch-tenant-postpatch.json"));
    final JsonNode     postPatch = Support.objectReader().readTree(stream("patch-tenant-prepatch.json"));
    final Generic      resource  = Generic.build((ObjectNode)prePatch);
    request.apply(resource);

    assertEquals(resource.objectNode(), postPatch);
  }
}