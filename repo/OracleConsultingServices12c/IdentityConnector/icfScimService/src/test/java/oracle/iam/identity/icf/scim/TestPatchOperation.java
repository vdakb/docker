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

    File        :   TestPatchOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestPatchOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim;

import java.util.Date;
import java.util.Arrays;

import java.io.IOException;

import java.net.URI;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.core.Base64Variants;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.iam.identity.icf.foundation.object.Pair;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.schema.Support;
import oracle.iam.identity.icf.scim.schema.GenericResource;

import oracle.iam.identity.icf.scim.v2.request.Patch;
import oracle.iam.identity.icf.scim.v2.request.Operation;

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
   **
   ** @throws Exception if an error occurs.
   */
  //@Test
  public void testRequest() {
    try {
      final Patch.Request   request   = stream("patch-full-representation.json", Patch.Request.class);
      final JsonNode        prePatch  = jsonNode("patch-full-prepatch.json");
      final JsonNode        postPatch = jsonNode("patch-full-postpatch.json");
      final GenericResource resource  = new GenericResource((ObjectNode)prePatch);
      request.apply(resource.node());

      assertEquals(resource.node(), postPatch);
    }
    catch (IOException e) {
      failed(e);
    }
    catch (ServiceException e) {
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
  //@Test
  public void testBadRequest() {
    try {
      Support.objectReader().forType(Patch.Request.class).readValue(stream("patch-user-email-badrequest.json"));
    }
    catch (JsonProcessingException e) {
      // swallow
      ;
    }
    catch (IOException e) {
      failed(e);
    }

    try {
      Support.objectReader().forType(Patch.Request.class).readValue(stream("patch-user-email-subattribute.json"));
    }
    catch (JsonProcessingException e) {
      // swallow
      ;
    }
    catch (IOException e) {
      failed(e);
    }

    try {
      Support.objectReader().forType(Patch.Request.class).readValue(stream("patch-user-email-badpath.json"));
    }
    catch (JsonProcessingException e) {
      // swallow
      ;
    }
    catch (IOException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testBoolean
  /**
   ** Test noolean methods.
   */
  //@Test
  public void testBoolean() {
    try {
      Operation op = Operation.addBoolean("pathToBool", CollectionUtility.list(Boolean.TRUE, Boolean.FALSE));
      assertEquals(op.type(), Operation.Type.ADD);
      JsonNode node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).booleanValue(), Boolean.TRUE.booleanValue());
      assertEquals(node.get(1).booleanValue(), Boolean.FALSE.booleanValue());
      assertEquals(op.path(), Path.from("pathToBool"));

      op = Operation.addBoolean(Path.from("pathToBool"), CollectionUtility.list(Boolean.FALSE, Boolean.TRUE));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).booleanValue(), Boolean.FALSE.booleanValue());
      assertEquals(node.get(1).booleanValue(), Boolean.TRUE.booleanValue());
      assertEquals(op.path(), Path.from("pathToBool"));

      op = Operation.replace("pathToBool", Boolean.TRUE);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(Boolean.class), Boolean.TRUE);
      assertEquals(op.path(), Path.from("pathToBool"));

      op = Operation.replace(Path.from("pathToBool"), Boolean.FALSE);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(Boolean.class), Boolean.FALSE);
      assertEquals(op.path(), Path.from("pathToBool"));


      op = Operation.addBoolean(Path.from("pathToBool"), Boolean.FALSE);
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isBoolean());
    }
    catch (ServiceException e) {
      failed(e);
    }
    catch (JsonProcessingException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testInteger
  /**
   ** Test integer methods.
   */
  //@Test
  public void testInteger() {
    try {
      Operation op = Operation.addInteger("pathToInteger", CollectionUtility.list(1, 2));
      assertEquals(op.type(), Operation.Type.ADD);
      JsonNode node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).intValue(), 1);
      assertEquals(node.get(1).intValue(), 2);
      assertEquals(op.path(), Path.from("pathToInteger"));
    
      op = Operation.addInteger(Path.from("pathToInteger"), CollectionUtility.list(3, 4));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).intValue(), 3);
      assertEquals(node.get(1).intValue(), 4);
      assertEquals(op.path(), Path.from("pathToInteger"));

      op = Operation.replace("pathToInteger", 5);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(Integer.class).intValue(), 5);
      assertEquals(op.path(), Path.from("pathToInteger"));

      op = Operation.replace(Path.from("pathToInteger"), 6);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(Integer.class).intValue(), 6);
      assertEquals(op.path(), Path.from("pathToInteger"));

      op = Operation.addInteger(Path.from("pathToInteger"), 10);
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isInt());
    }
    catch (ServiceException e) {
      failed(e);
    }
    catch (JsonProcessingException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testLong
  /**
   ** Test long methods.
   */
  //@Test
  public void testLong() {
    try {
      Operation op = Operation.addLong("pathToLong", CollectionUtility.list(1L, 2L));
      assertEquals(op.type(), Operation.Type.ADD);
      JsonNode node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).longValue(), 1L);
      assertEquals(node.get(1).longValue(), 2L);
      assertEquals(op.path(), Path.from("pathToLong"));

      op = Operation.addLong(Path.from("pathToLong"), CollectionUtility.list(3L, 4L));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).longValue(), 3L);
      assertEquals(node.get(1).longValue(), 4L);
      assertEquals(op.path(), Path.from("pathToLong"));

      op = Operation.replace("pathToLong", 5L);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(Long.class).longValue(), 5L);
      assertEquals(op.path(), Path.from("pathToLong"));

      op = Operation.replace(Path.from("pathToLong"), 6L);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(Long.class).longValue(), 6L);
      assertEquals(op.path(), Path.from("pathToLong"));

      op = Operation.addLong(Path.from("pathToLong"), 10L);
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isLong());
    }
    catch (ServiceException e) {
      failed(e);
    }
    catch (JsonProcessingException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testDouble
  /**
   ** Test double methods.
   **
   ** @throws Exception if an error occurs.
   */
  //@Test
  public void testDouble() {
    try {
      Operation op = Operation.addDouble("pathToDouble", CollectionUtility.list(1.1, 1.2));
      assertEquals(op.type(), Operation.Type.ADD);
      JsonNode node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).doubleValue(), 1.1, 0.01);
      assertEquals(node.get(1).doubleValue(), 1.2, 0.01);
      assertEquals(op.path(), Path.from("pathToDouble"));

      op = Operation.addDouble(Path.from("pathToDouble"), CollectionUtility.list(2.1, 2.2));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).doubleValue(), 2.1, 0.01);
      assertEquals(node.get(1).doubleValue(), 2.2, 0.01);
      assertEquals(op.path(), Path.from("pathToDouble"));

      op = Operation.replace("pathToDouble", 734.2);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(Double.class), 734.2, 0.01);
      assertEquals(op.path(), Path.from("pathToDouble"));

      op = Operation.replace(Path.from("pathToDouble"), 0.3);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(Double.class), 0.3, 0.01);
      assertEquals(op.path(), Path.from("pathToDouble"));

      op = Operation.addDouble(Path.from("pathToDouble"), 10.1);
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isDouble());
    }
    catch (ServiceException e) {
      failed(e);
    }
    catch (JsonProcessingException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testDate
  /**
   ** Test date methods.
   **
   ** @throws Exception if an error occurs.
   */
  //@Test
  public void testDate() {
    Date d1 = new Date(89233675234L);
    Date d2 = new Date(89233675235L);
    Date d3 = new Date(89233675236L);
    Date d4 = new Date(89233675237L);
    Date d5 = new Date(89233675238L);
    Date d6 = new Date(89233675240L);

    try {
      Operation op = Operation.addDate("pathToDate", CollectionUtility.list(d1, d2));
      assertEquals(op.type(), Operation.Type.ADD);
      JsonNode node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
//      assertEquals(Generic.dateValue(node.get(0)), d1);
      assertEquals(op.values(String.class).get(0), Support.valueToNode(d1).textValue());
//      assertEquals(Generic.dateValue(node.get(1)), d2);
      assertEquals(op.values(String.class).get(1), Support.valueToNode(d2).textValue());
      assertEquals(op.path(), Path.from("pathToDate"));

      op = Operation.addDate(Path.from("pathToDate"), CollectionUtility.list(d3, d4));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
//      assertEquals(Generic.dateValue(node.get(0)), d3);
      assertEquals(op.values(String.class).get(0), Support.valueToNode(d3).textValue());
//      assertEquals(Generic.dateValue(node.get(1)), d4);
      assertEquals(op.values(String.class).get(1), Support.valueToNode(d4).textValue());
      assertEquals(op.path(), Path.from("pathToDate"));

      op = Operation.replace("pathToDate", d5);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(String.class), Support.valueToNode(d5).textValue());
      assertEquals(op.path(), Path.from("pathToDate"));

      op = Operation.replace(Path.from("pathToDate"), d6);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(String.class), Support.valueToNode(d6).textValue());
      assertEquals(op.path(), Path.from("pathToDate"));
    }
    catch (ServiceException e) {
      failed(e);
    }
    catch (JsonProcessingException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testString
  /**
   ** Test string methods.
   */
  //@Test
  public void testString() {
    try {
      Operation op = Operation.addString("pathToString", "value0");
      assertEquals(op.type(), Operation.Type.ADD);
      JsonNode node = node = op.node();
      assertTrue(node.isTextual());

      op = Operation.addString(Path.from("pathToString"), "value0");
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isTextual());

      op = Operation.addString("pathToString", CollectionUtility.list("value1", "value2"));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).textValue(), "value1");
      assertEquals(node.get(1).textValue(), "value2");
      assertEquals(op.path(), Path.from("pathToString"));

      op = Operation.addString(Path.from("pathToString"), CollectionUtility.list("value3", "value4"));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).textValue(), "value3");
      assertEquals(node.get(1).textValue(), "value4");
      assertEquals(op.path(), Path.from("pathToString"));

      op = Operation.replace("pathToString", "value1");
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(String.class), "value1");
      assertEquals(op.path(), Path.from("pathToString"));

      op = Operation.replace(Path.from("pathToString"), "value1");
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(String.class), "value1");
      assertEquals(op.path(), Path.from("pathToString"));

      op = Operation.remove(Path.from("roles[value eq 5 and role eq \"uid.generate\"]"));
      assertEquals(op.type(), Operation.Type.REMOVE);
      assertNull(op.value(String.class));
      assertEquals(op.path(), Path.from("roles[value eq 5 and role eq \"uid.generate\"]"));
    }
    catch (Exception e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testBinary
  /**
   ** Test binary methods.
   */
  //@Test
  public void testBinary() {
    byte[] ba1 = new byte[] {0x00, 0x01, 0x02};
    byte[] ba2 = new byte[] {0x02, 0x01, 0x00};
    byte[] ba3 = new byte[] {0x03, 0x04, 0x05};
    byte[] ba4 = new byte[] {0x05, 0x04, 0x03};
    byte[] ba5 = new byte[] {0x06, 0x07, 0x08};
    byte[] ba6 = new byte[] {0x09, 0x0a, 0x0b};

    try {
      Operation op = Operation.addBinary("pathToBinary", CollectionUtility.list(ba1, ba2));
      assertEquals(op.type(), Operation.Type.ADD);
      JsonNode node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertTrue(Arrays.equals(Base64Variants.getDefaultVariant().decode(node.get(0).textValue()), ba1));
      assertTrue(Arrays.equals(Base64Variants.getDefaultVariant().decode(node.get(1).textValue()), ba2));
      assertEquals(op.path(), Path.from("pathToBinary"));

      op = Operation.addBinary(Path.from("pathToBinary"), CollectionUtility.list(ba3, ba4));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertTrue(Arrays.equals(Base64Variants.getDefaultVariant().decode(node.get(0).textValue()), ba3));
      assertTrue(Arrays.equals(Base64Variants.getDefaultVariant().decode(node.get(1).textValue()), ba4));
      assertEquals(op.path(), Path.from("pathToBinary"));

      op = Operation.replace("pathToBinary", ba5);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertTrue(Arrays.equals(op.value(byte[].class), ba5));
      assertEquals(op.path(), Path.from("pathToBinary"));

      op = Operation.replace(Path.from("pathToBinary"), ba6);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertTrue(Arrays.equals(op.value(byte[].class), ba6));
      assertEquals(op.path(), Path.from("pathToBinary"));
    }
    catch (Exception e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testURI
  /**
   ** Test URI methods.
   */
  //@Test
  public void testURI() {
    try {
      URI uri1 = new URI("http://localhost:8080/apps/app1");
      URI uri2 = new URI("Users/1dd6d752-1744-47e5-a4a8-5f5670aa8905");
      URI uri3 = new URI("http://localhost:8080/apps/app2");
      URI uri4 = new URI("Users/1dd6d752-1744-47e5-a4a8-5f5670aa8998");
      URI uri5 = new URI("http://localhost:8080/apps/app3");
      URI uri6 = new URI("http://localhost:8080/apps/app4");

      Operation op = Operation.addURI("pathToURI", CollectionUtility.list(uri1, uri2));
      assertEquals(op.type(), Operation.Type.ADD);
      JsonNode node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).textValue(), uri1.toString());
      assertEquals(node.get(1).textValue(), uri2.toString());
      assertEquals(op.path(), Path.from("pathToURI"));

      op = Operation.addURI(Path.from("pathToURI"), CollectionUtility.list(uri3, uri4));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertEquals(node.get(0).textValue(), uri3.toString());
      assertEquals(node.get(1).textValue(), uri4.toString());
      assertEquals(op.path(), Path.from("pathToURI"));

      op = Operation.replace("pathToURI", uri5);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(String.class), uri5.toString());
      assertEquals(op.path(), Path.from("pathToURI"));

      op = Operation.replace(Path.from("pathToURI"), uri6);
      assertEquals(op.type(), Operation.Type.REPLACE);
      assertEquals(op.value(String.class), uri6.toString());
      assertEquals(op.path(), Path.from("pathToURI"));
    }
    catch (Exception e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testPair
  /**
   ** Test pair methods.
   */
  //@Test
  public void testPair() {
    Pair<String, Object> p1 = Pair.of("T1", "V1");
    Pair<String, Object> p2 = Pair.of("T2", "V2");
    Pair<String, Object> p3 = Pair.of("T3", "V3");
    Pair<String, Object> p4 = Pair.of("T4", "V4");
    Pair<String, Object> p5 = Pair.of("T5", "V5");
    Pair<String, Object> p6 = Pair.of("T6", "V6");

    try {
      Operation op = Operation.addPair("pathToPair", CollectionUtility.list(p1, p2));
      assertEquals(op.type(), Operation.Type.ADD);
      JsonNode node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertTrue(node.get(0).isObject());
      JsonNode pair = node.get(0);
      assertEquals(p1.value, pair.get(p1.tag).asText());
      assertTrue(node.get(1).isObject());
      pair = node.get(1);
      assertEquals(p2.value, pair.get(p2.tag).asText());
      assertEquals(op.path(), Path.from("pathToPair"));

      op = Operation.addPair(Path.from("pathToPair"), CollectionUtility.list(p3, p4));
      assertEquals(op.type(), Operation.Type.ADD);
      node = op.node();
      assertTrue(node.isArray());
      assertEquals(node.size(), 2);
      assertTrue(node.get(0).isObject());
      pair = node.get(0);
      assertEquals(p3.value, pair.get(p3.tag).asText());
      assertTrue(node.get(1).isObject());
      pair = node.get(1);
      assertEquals(p4.value, pair.get(p4.tag).asText());
      assertEquals(op.path(), Path.from("pathToPair"));

      op = Operation.replace("pathToPair", p5);
      assertEquals(op.type(), Operation.Type.REPLACE);
      node = op.node();
      assertTrue(node.isObject());
      assertEquals(p5.value, node.get(p5.tag).asText());
      assertEquals(op.path(), Path.from("pathToPair"));

      op = Operation.replace(Path.from("pathToPair"), p6);
      assertEquals(op.type(), Operation.Type.REPLACE);
      node = op.node();
      assertEquals(p6.value, node.get(p6.tag).asText());
      assertEquals(op.path(), Path.from("pathToPair"));
    }
    catch (Exception e) {
      failed(e);
    }
  }

  @Test
  public void testTenantAssign() {
    try {
      final Patch.Request   request   = stream("patch-tenant-roles-assign.json", Patch.Request.class);
      final JsonNode        prePatch  = jsonNode("patch-tenant-prepatch.json");
      final JsonNode        postPatch = jsonNode("patch-tenant-postpatch.json");
      final GenericResource resource  = new GenericResource((ObjectNode)prePatch);
      request.apply(resource.node());

      assertEquals(resource.node(), postPatch);
    }
    catch (IOException e) {
      failed(e);
    }
    catch (ServiceException e) {
      failed(e);
    }
  }

  //@Test
  public void testTenantRevoke() {
    try {
      final Patch.Request   request   = stream("patch-tenant-roles-revoke.json", Patch.Request.class);
      final JsonNode        prePatch  = jsonNode("patch-tenant-postpatch.json");
      final JsonNode        postPatch = jsonNode("patch-tenant-prepatch.json");
      final GenericResource resource  = new GenericResource((ObjectNode)prePatch);
      request.apply(resource.node());

      assertEquals(resource.node(), postPatch);
    }
    catch (IOException e) {
      failed(e);
    }
    catch (ServiceException e) {
      failed(e);
    }
  }
}