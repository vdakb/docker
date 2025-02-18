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

    File        :   TestListResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestListResponse.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.v2;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.JsonMappingException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.junit.TestBase;

import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.response.ListResponse;

import oracle.iam.platform.scim.v2.ResourceTypeResource;

////////////////////////////////////////////////////////////////////////////////
// class TestListResponse
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Test coverage for validate and enforce the list response.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestListResponse extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestListResponse</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestListResponse() {
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
    final String[] parameter = { TestListResponse.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testListResponse
  /**
   ** Test list response.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testListResponse()
    throws Exception {

    final String literal = "{\"schemas\":[\"urn:ietf:params:scim:api:messages:2.0:ListResponse\"],\"totalresults\":2,\"startIndex\":1,\"ItemsPerPage\":3,\"Resources\":[{\"userName\":\"bjensen\"},{\"userName\":\"jsmith\"}]}";
    ListResponse<ObjectNode> listResponse = Support.objectReader().forType(new TypeReference<ListResponse<ObjectNode>>() {}).readValue(literal);
    assertEquals(listResponse.total(),           2);
    assertEquals(listResponse.start(),           1);
    assertEquals(listResponse.items(),           3);
    assertEquals(listResponse.resource().size(), 2);

    ArrayList<ResourceTypeResource> resourceTypeList = new ArrayList<ResourceTypeResource>();
    resourceTypeList.add(new ResourceTypeResource("urn:test",  "test",  "test",  new URI("/test"),  new URI("urn:test"),  Collections.<ResourceTypeResource.Extension>emptyList()));
    resourceTypeList.add(new ResourceTypeResource("urn:test2", "test2", "test2", new URI("/test2"), new URI("urn:test2"), Collections.<ResourceTypeResource.Extension>emptyList()));

    final ListResponse<ResourceTypeResource> response = new ListResponse<ResourceTypeResource>(100, 1, 10, resourceTypeList);
    assertEquals(response.total(),           100);
    assertEquals(response.start(),           1);
    assertEquals(response.items(),           10);
    assertEquals(response.resource().size(), 2);

    String serialized = Support.objectWriter().writeValueAsString(response);
    final ListResponse<ResourceTypeResource> xxxxxxxx = Support.objectReader().forType(new TypeReference<ListResponse<ResourceTypeResource>>(){}).readValue(serialized);
    assertEquals(xxxxxxxx.total(),           100);
    assertEquals(xxxxxxxx.start(),           1);
    assertEquals(xxxxxxxx.items(),           10);
    assertEquals(xxxxxxxx.resource().size(), 2);

    assertEquals(serialized, Support.objectWriter().writeValueAsString(xxxxxxxx));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRequired
  /**
   ** Test list response.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testRequired()
    throws Exception {

    try {
      final ListResponse<ObjectNode> response = Support.objectReader().forType(
        new TypeReference<ListResponse<ObjectNode>>() {})
          .readValue(
          "{  \n" +
            "  \"schemas\":[  \n" +
            "    \"urn:ietf:params:scim:api:messages:2.0:ListResponse\"\n" +
            "  ],\n" +
            // test missing required property: totalResults
            "  \"startIndex\":1,\n" +
            // test case-insensitivity
            "  \"ItemsPerPage\":3,\n" +
            "  \"Resources\":[  \n" +
            "    {  \n" +
            "      \"userName\":\"bjensen\"\n" +
            "    },\n" +
            "    {  \n" +
            "      \"userName\":\"jsmith\"\n" +
            "    }\n" +
            "  ]\n" +
            "}");
      failed("Expected failure for missing required property 'totalResults'");
    }
    catch (JsonMappingException e) {
      assertTrue(e.getMessage(), e.getMessage().contains("Missing required creator property"));
    }
  }
}
