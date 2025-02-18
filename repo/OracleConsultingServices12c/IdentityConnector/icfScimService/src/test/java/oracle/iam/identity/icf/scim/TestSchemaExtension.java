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

    File        :   TestSchemaExtension.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestSchemaExtension.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.GregorianCalendar;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.databind.JsonNode;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.schema.Entity;
import oracle.iam.identity.icf.scim.schema.Support;
import oracle.iam.identity.icf.scim.schema.Metadata;

import oracle.iam.identity.icf.scim.annotation.Schema;
import oracle.iam.identity.icf.scim.annotation.Attribute;
import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestSchemaExtension
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Test coverage for validate and enforce the schema extensions of a Resource
 ** Type on JSON objects representing SCIM resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestSchemaExtension extends TestBase {

  @JsonPropertyOrder({"first", "last", "middle"})
  private static class ExtensionName {
    @JsonProperty
    @Attribute(description="User's first name")
    private String first;

    @JsonProperty
    @Attribute(description="User's middle name")
    private String middle;

    @JsonProperty
    @Attribute(description="User's last name")
    private String last;
  }

  @JsonPropertyOrder({"userName", "password", "name"})
  private static class ExtensionUser<T extends ExtensionUser> extends Entity<T> {
    
    @JsonProperty
    @Attribute(description="A user's username")
    private String        userName;
    
    @JsonProperty
    @Attribute(description="The name of the user")
    private ExtensionName name;

    @JsonProperty
    @Attribute(description="The user's password", mutability = Definition.Mutability.WRITE_ONLY)
    private String        password;
  }

  @Schema(description = "Class to represent a favorite color", id="urn:ietf:params:scim:schemas:extension:oracle:1.0:Color", name="Color")
  private static class ExtensionClass {

    @JsonProperty
    @Attribute(description="Favorite color")
    private String color;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestSchemaExtension</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestSchemaExtension() {
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
    final String[] parameter = { TestSchemaExtension.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testGeneration
  /**
   ** Tests json generation of the schema annotation.
   **
   ** @throws Exception thrown if an error occurs.
   */
  @Test
  public void testGeneration()
    throws Exception {
    
    ExtensionUser user = extensionUser();
    String        userString = Support.objectWriter().writeValueAsString(user);
    JsonNode      userNode   = Support.objectReader().readTree(userString);
    // check some of the basic fields
    JsonNode      userName   = userNode.path("userName");
    assertEquals(userNode.path("userName").asText(), user.userName);
    assertEquals(userNode.path("id").asText(), user.id());
    assertEquals(userNode.path("externalId").asText(), user.externalId());
    // check the schemas
    assertEquals( Support.objectReader().treeToValue(userNode.path("schemas"), HashSet.class), user.namespace());

    // check the extension values
    final JsonNode       node = (JsonNode)user.extensionValues(Path.build(ExtensionClass.class)).get(0);
    assertEquals(userNode.path(
      "urn:ietf:params:scim:schemas:extension:oracle:1.0:Color").path("color").asText()
    , Support.nodeToValue(node, ExtensionClass.class).color
    );
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testGeneration
  /**
   ** Create a user (test class) with an extension.
   **
   ** @return                    a user for tests.
   **                            <br>
   **                            Possible object is {@link ExtensionUser}.
   **
   ** @throws ServiceException   if the path expression could not be parsed
   **                            or the filter derived from the path isn't
   **                            valid for matching.
   ** @throws URISyntaxException if the location attribute formed is not a valid
   **                            {@link URI}.
   */
  private ExtensionUser extensionUser()
    throws URISyntaxException
    ,      ServiceException {

    ExtensionName name = new ExtensionName();
    name.first  = "name:first";
    name.middle = "name:middle";
    name.last   = "name:last";

    Metadata meta = new Metadata();
    meta.created(new GregorianCalendar());
    meta.modified(new GregorianCalendar());
    meta.location(new URI("http://here/user"));
    meta.resourceType("some resource type");
    meta.version("1.0");

    ExtensionClass extensionClass = new ExtensionClass();
    extensionClass.color = "extension:color";

    ExtensionUser user = new ExtensionUser();
    user.password = "user:password";
    user.userName = "user:username";
    user.name     = name;
    user.id("user:id").externalId("user:externalId").metadata(meta);
    user.replaceExtensionValue(Path.build(extensionClass.getClass()), Support.valueToNode(extensionClass));
    
    return user;
  }
}