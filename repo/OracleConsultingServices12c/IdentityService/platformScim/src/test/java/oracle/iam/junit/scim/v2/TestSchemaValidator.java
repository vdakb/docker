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

    File        :   TestSchemaValidator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestSchemaValidator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.v2;

import java.lang.reflect.Field;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.BeforeClass;

import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.core.Base64Variants;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;

import java.nio.file.Files;

import java.nio.charset.StandardCharsets;

import java.nio.file.Paths;

import oracle.iam.platform.scim.AttributeDefinition;
import oracle.iam.platform.scim.BadRequestException;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.annotation.Attribute;

import oracle.iam.platform.scim.entity.Path;
import oracle.iam.platform.scim.entity.Filter;

import oracle.iam.platform.scim.request.PatchOperation;

import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.v2.UserResource;
import oracle.iam.platform.scim.v2.SchemaFactory;
import oracle.iam.platform.scim.v2.SchemaResource;
import oracle.iam.platform.scim.v2.SchemaValidator;
import oracle.iam.platform.scim.v2.EnterpriseUserResource;

////////////////////////////////////////////////////////////////////////////////
// class TestSchemaValidator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Test coverage for validate and enforce the schema constraints of a Resource
 ** Type on JSON objects representing SCIM resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestSchemaValidator extends TestResource {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static SchemaResource core;
  private static SchemaResource test;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestSchemaValidator</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestSchemaValidator() {
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
    final String[] parameter = { TestSchemaValidator.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Setup some basic schemas.
   **
   ** @throws Exception          if an error occurs.
   */
  @BeforeClass
  public static void prepare()
    throws Exception {

    // boolean attribute definition
    final AttributeDefinition bool            = AttributeDefinition.factory().name("boolean").type(Attribute.Type.BOOLEAN).build();
    // decimal attribute definition
    final AttributeDefinition decimal         = AttributeDefinition.factory().name("decimal").type(Attribute.Type.DECIMAL).build();
    // integer attribute definition
    final AttributeDefinition integer         = AttributeDefinition.factory().name("integer").type(Attribute.Type.INTEGER).build();
    // string attribute definition
    final AttributeDefinition string          = AttributeDefinition.factory().name("string").type(Attribute.Type.STRING).build();
    // string with canonical values attribute definition
    final AttributeDefinition stringCanonical = AttributeDefinition.factory().name("stringCanonical").type(Attribute.Type.STRING).canonical("value1", "value2").build();
    // datetime attribute definition
    final AttributeDefinition datetime        = AttributeDefinition.factory().name("datetime").type(Attribute.Type.DATETIME).build();
    // binary attribute definition
    final AttributeDefinition binary          = AttributeDefinition.factory().name("binary").type(Attribute.Type.BINARY).build();
    // reference attribute definition
    final AttributeDefinition reference       = AttributeDefinition.factory().name("reference").type(Attribute.Type.REFERENCE).build();
    // complex attribute definition
    final AttributeDefinition complex         = AttributeDefinition.factory().name("complex").type(Attribute.Type.COMPLEX)
      .sub(string)
      .sub(stringCanonical)
      .sub(datetime)
      .sub(binary)
      .sub(reference)
      .sub(bool)
      .sub(decimal)
      .sub(integer)
      .build();

    final List<AttributeDefinition> definition = new ArrayList<AttributeDefinition>();
    definition.add(string);
    definition.add(stringCanonical);
    definition.add(datetime);
    definition.add(binary);
    definition.add(reference);
    definition.add(bool);
    definition.add(decimal);
    definition.add(integer);
    definition.add(complex);
    // multi-valued string attribute definition
    definition.add(
      AttributeDefinition.factory()
      .name("mvstring")
      .type(Attribute.Type.STRING)
      .multiValued(true)
      .build()  
    );
    // multi-valued string with canonical values attribute definition
    definition.add(
      AttributeDefinition.factory()
      .name("mvstringCanonical")
      .type(Attribute.Type.STRING)
      .canonical("value1", "value2")
      .multiValued(true)
      .build()  
    );
    // multi-valued datetime values attribute definition
    definition.add(
      AttributeDefinition.factory()
      .name("mvdatetime")
      .type(Attribute.Type.DATETIME)
      .multiValued(true)
      .build()  
    );
    // multi-valued binary values attribute definition
    definition.add(
      AttributeDefinition.factory()
      .name("mvbinary")
      .type(Attribute.Type.BINARY)
      .multiValued(true)
      .build()  
    );
    // multi-valued reference values attribute definition
    definition.add(
      AttributeDefinition.factory()
      .name("mvreference")
      .type(Attribute.Type.REFERENCE)
      .multiValued(true)
      .build()  
    );
    // multi-valued boolean values attribute definition
    definition.add(
      AttributeDefinition.factory()
      .name("mvboolean")
      .type(Attribute.Type.BOOLEAN)
      .multiValued(true)
      .build()  
    );
    // multi-valued decimal values attribute definition
    definition.add(
      AttributeDefinition.factory()
      .name("mvdecimal")
      .type(Attribute.Type.DECIMAL)
      .multiValued(true)
      .build()  
    );
    // multi-valued integer values attribute definition
    definition.add(
      AttributeDefinition.factory()
      .name("mvinteger")
      .type(Attribute.Type.INTEGER)
      .multiValued(true)
      .build()  
    );
    // multi-valued complex values attribute definition
    definition.add(
      AttributeDefinition.factory()
      .name("mvcomplex")
      .type(Attribute.Type.COMPLEX)
      .multiValued(true)
      .sub(string)
      .sub(stringCanonical)
      .sub(datetime)
      .sub(binary)
      .sub(reference)
      .sub(bool)
      .sub(decimal)
      .sub(integer)
      .build()  
    );
    TestSchemaValidator.core = SchemaFactory.schema(UserResource.class);
    TestSchemaValidator.test = new SchemaResource("urn:id:test", "test", "", definition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSanity
  /**
   ** Check the full User Spec representation against the Spec schemas.
   ** <br>
   ** Shouldn't be any issues.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testSanity()
    throws Exception {

    final SchemaResource core        = SchemaFactory.schema(UserResource.class);
    final SchemaResource extension   = SchemaFactory.schema(EnterpriseUserResource.class);
    ResourceTypeDefinition type      = ResourceTypeDefinition.builder("test", "/test").core(core).optional(extension).build();
    SchemaValidator        validator = SchemaValidator.of(type);
    
    final String bjensen = new String(Files.readAllBytes(Paths.get("./src/test/resources/user-full-representation.json")));
    ObjectNode resource = (ObjectNode)Support.objectReader().readTree(bjensen);
    // remove any read only attributes since they are suppose to be ignored
    // on create and replace
    resource = validator.removeReadOnly(resource);
    // keep for later comparision
    final JsonNode snapshot = resource.deepCopy();

    // check create
    SchemaValidator.Result result = validator.onCreate(resource);
    // make sure there are no issues
    assertTrue(result.mutability().toString(), result.mutability().isEmpty());
    assertTrue(result.path().toString(), result.path().isEmpty());
    assertTrue(result.syntax().toString(), result.syntax().isEmpty());
    // make sure the ObjectNode wasn't modified during the check
    assertEquals(resource, snapshot);

    // check replace
    result = validator.onReplace(resource, null);
    // make sure there are no issues
    assertTrue(result.mutability().toString(), result.mutability().isEmpty());
    assertTrue(result.path().toString(),       result.path().isEmpty());
    assertTrue(result.syntax().toString(),     result.syntax().isEmpty());
    // make sure the ObjectNode wasn't modified during the check
    assertEquals(resource, snapshot);

    // check modify
    String patch =
        "{  \n" +
            "  \"op\":\"add\",\n" +
            "  \"value\":{  \n" +
            // check for case insensitive behavior
            "    \"passWORD\":\"password\",\n" +
            "    \"name\":{  \n" +
            "      \"givenName\":\"Barbara\",\n" +
            // check for case insensitive behavior
            "      \"FAMILYName\":\"Jensen\",\n" +
            "      \"formatted\":\"Barbara Ann Jensen\"\n" +
            "    },\n" +
            "    \"emails\":[  \n" +
            "      {  \n" +
            // check for case insensitive behavior
            "        \"VALUE\":\"bjensen@example.com\",\n" +
            "        \"type\":\"work\"\n" +
            "      },\n" +
            "      {  \n" +
            "        \"value\":\"babs@jensen.org\",\n" +
            "        \"type\":\"home\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"urn:ietf:params:scim:schemas:extension:" +
            "enterprise:2.0:User\":{  \n" +
            "      \"employeeNumber\":\"701984\"\n" +
            "    },\n" +
            "    \"addresses\":[  \n" +
            "      {  \n" +
            "        \"type\":\"work\",\n" +
            "        \"streetAddress\":\"13809 Research Blvd\",\n" +
            "        \"locality\":\"Austin\",\n" +
            "        \"region\":\"TX\",\n" +
            "        \"postalCode\":\"78750\",\n" +
            "        \"country\":\"USA\",\n" +
            "        \"formatted\":\"13809 Research Blvd\\n" +
            "Austin, TX 78750 USA\",\n" +
            "        \"primary\":true\n" +
            "      },\n" +
            "      {  \n" +
            "        \"type\":\"home\",\n" +
            "        \"streetAddress\":\"456 Hollywood Blvd\",\n" +
            "        \"locality\":\"Hollywood\",\n" +
            "        \"region\":\"CA\",\n" +
            "        \"postalCode\":\"91608\",\n" +
            "        \"country\":\"USA\",\n" +
            "        \"formatted\":\"456 Hollywood Blvd\\n" +
            "Hollywood, CA 91608 USA\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    PatchOperation operation = Support.objectReader().forType(PatchOperation.class).readValue(patch);
    result = validator.onModify(Collections.singleton(operation), resource);
    // make sure there are no issues
    assertTrue(result.mutability().toString(), result.mutability().isEmpty());
    assertTrue(result.path().toString(), result.path().isEmpty());
    assertTrue(result.syntax().toString(), result.syntax().isEmpty());
    // make sure the patch operation wasn't modified during the check
    assertEquals(operation, Support.objectReader().forType(PatchOperation.class).readValue(patch));
    // make sure the ObjectNode wasn't modified during the check
    assertEquals(resource, snapshot);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testCoreSchema
  /**
   ** Test to ensure not including the core schema in the schemas attribute
   ** should result in syntax error.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testCoreSchema()
    throws Exception {

    // create one schema extension with a required attribute.
    final AttributeDefinition reqAttr        = AttributeDefinition.factory().name("test").type(Attribute.Type.STRING).required(true).build();
    final SchemaResource      extWithReqAttr = new SchemaResource("urn:id:testExt", "testExt", "", Collections.singleton(reqAttr));

    // not including the core schema should be an error.
    final ObjectNode resource =Support.nodeFactory().objectNode();
    resource.putArray("schemas"). add("urn:id:testExt");
    resource.put("userName", "test");
    resource.putObject("urn:id:testExt").put("test", "test");

    final ResourceTypeDefinition type      = ResourceTypeDefinition.builder("test", "/test").core(core).required(extWithReqAttr).build();
    final SchemaValidator        validator = SchemaValidator.of(type);
    final SchemaValidator.Result result    = validator.onCreate(resource);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testExtensionSchema
  /**
   ** Test to ensure schema extensions are checked correctly.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testExtensionSchema()
    throws Exception {

    // create one schema extension with an optional attribute.
    final AttributeDefinition optAttr        = AttributeDefinition.factory().name("test").type(Attribute.Type.STRING).build();
    final SchemaResource      extWithOptAttr = new SchemaResource("urn:id:testExt", "testExt", "", Collections.singleton(optAttr));
    // create one schema extension with a required attribute.
    final AttributeDefinition reqAttr        = AttributeDefinition.factory(). name("test").type(Attribute.Type.STRING). required(true). build();
    final SchemaResource      extWithReqAttr = new SchemaResource("urn:id:testExt", "testExt", "", Collections.singleton(reqAttr));

    final ObjectNode extNotIn = Support.nodeFactory().objectNode();
    extNotIn.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User");
    extNotIn.put("userName", "test");

    final ObjectNode extInSchemas = Support.nodeFactory().objectNode();
    extInSchemas.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:id:testExt");
    extInSchemas.put("userName", "test");

    final ObjectNode extNotInSchemas = Support.nodeFactory().objectNode();
    extNotInSchemas.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User");
    extNotInSchemas.put("userName", "test");
    extNotInSchemas.putObject("urn:id:testExt").put("test", "test");

    final ObjectNode extIn = Support.nodeFactory().objectNode();
    extIn.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:id:testExt");
    extIn.put("userName", "test");
    extIn.putObject("urn:id:testExt").put("test", "test");

    ObjectNode undefinedInSchemas = Support.nodeFactory().objectNode();
    undefinedInSchemas.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:id:undefined");
    undefinedInSchemas.put("userName", "test");

    final ObjectNode undefinedNotInSchemas = Support.nodeFactory().objectNode();
    undefinedNotInSchemas.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User");
    undefinedNotInSchemas.put("userName", "test");
    undefinedNotInSchemas.putObject("urn:id:undefined").put("test", "test");

    ObjectNode undefinedIn = Support.nodeFactory().objectNode();
    undefinedIn.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:id:undefined");
    undefinedIn.put("userName", "test");
    undefinedIn.putObject("urn:id:undefined").put("test", "test");

    final ObjectNode notObject =Support.nodeFactory().objectNode();
    notObject.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:id:testExt");
    notObject.put("userName", "test");
    notObject.putArray("urn:id:testExt").addObject().put("test", "test");

    final Object[][] provider = new Object[][] {
        new Object[]{extWithReqAttr, true,  extNotIn,              1, 0}
      , new Object[]{extWithReqAttr, true,  extInSchemas,          1, 0}
      , new Object[]{extWithReqAttr, true,  extNotInSchemas,       2, 0}
      , new Object[]{extWithReqAttr, true,  extIn,                 0, 0}
      , new Object[]{extWithReqAttr, false, extNotIn,              0, 0}
      , new Object[]{extWithReqAttr, false, extInSchemas,          1, 0}
      , new Object[]{extWithReqAttr, false, extNotInSchemas,       1, 0}
      , new Object[]{extWithReqAttr, false, extIn,                 0, 0},

        new Object[]{extWithReqAttr, false, undefinedInSchemas,    1, 1}
      , new Object[]{extWithReqAttr, false, undefinedNotInSchemas, 1, 1}
      , new Object[]{extWithReqAttr, false, undefinedIn,           1, 2}
      , new Object[]{extWithReqAttr, false, notObject,             1, 1},

        new Object[]{extWithOptAttr, true,  extNotIn,              1, 0}
      , new Object[]{extWithOptAttr, true,  extInSchemas,          0, 0}
      , new Object[]{extWithOptAttr, true,  extNotInSchemas,       2, 0}
      , new Object[]{extWithOptAttr, true,  extIn,                 0, 0}
      , new Object[]{extWithOptAttr, false, extNotIn,              0, 0}
      , new Object[]{extWithOptAttr, false, extInSchemas,          0, 0}
      , new Object[]{extWithOptAttr, false, extNotInSchemas,       1, 0}
      , new Object[]{extWithOptAttr, false, extIn,                 0, 0},

        new Object[]{extWithOptAttr, false, undefinedInSchemas,    1, 1}
      , new Object[]{extWithOptAttr, false, undefinedNotInSchemas, 1, 1}
      , new Object[]{extWithOptAttr, false, undefinedIn,           1, 2}
      , new Object[]{extWithOptAttr, false, notObject,             1, 1}
    };
    for (int i = 0; i < provider.length; i++) {
      final SchemaResource schema   = (SchemaResource)provider[i][0];
      final Boolean        required = (Boolean)provider[i][1];
      final ObjectNode     node     = (ObjectNode)provider[i][2];
      final Integer        onCreate = (Integer)provider[i][3];
      final Integer        onModify = (Integer)provider[i][4];
      testSchemaExtension(schema, required, node, onCreate, onModify);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSchemaModify
  /**
   ** Test to ensure modifications using patch operations on the schemas
   ** attribute are checked correctly.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testSchemaModify()
    throws Exception {

    final ObjectNode resource = Support.nodeFactory().objectNode();
    resource.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:id:extWithReqAttr");
    resource.put("userName", "test");
    resource.putObject("urn:id:extWithReqAttr").put("test", "test");

    // create one schema extension with an optional attribute.
    AttributeDefinition            optAttr        = AttributeDefinition.factory().name("test").type(Attribute.Type.STRING).build();
    SchemaResource                 extWithOptAttr = new SchemaResource("urn:id:extWithOptAttr", "extWithOptAttr", "", Collections.singleton(optAttr));
    // create one schema extension with a required attribute
    final AttributeDefinition     reqAttr         = AttributeDefinition.factory().name("test").type(Attribute.Type.STRING).required(true).build();
    final SchemaResource          extWithReqAttr  = new SchemaResource("urn:id:extWithReqAttr", "extWithReqAttr", "", Collections.singleton(reqAttr));

    final ResourceTypeDefinition type             = ResourceTypeDefinition.builder("test", "/test").core(core).required(extWithReqAttr).optional(extWithOptAttr).build();
    final SchemaValidator        validator        = SchemaValidator.of(type);

    // shouldn't be able to remove the core schema
    List<PatchOperation> patchOps = new LinkedList<PatchOperation>();
    patchOps.add(PatchOperation.remove(Path.build().attribute("schemas", Filter.eq("value", "urn:ietf:params:scim:schemas:core:2.0:User"))));
    SchemaValidator.Result result = validator.onModify(patchOps, resource);
    assertEquals(result.syntax().toString(), result.syntax().size(), 2);

    // shouldn't be able to remove a required schema extension
    patchOps = new LinkedList<PatchOperation>();
    patchOps.add(PatchOperation.remove(Path.build().attribute("schemas", Filter.eq("value", "urn:id:extWithReqAttr"))));
    result = validator.onModify(patchOps, resource);
    assertEquals(result.syntax().toString(), result.syntax().size(), 3);

    // shouldn't be able to replace the core schema
    patchOps = new LinkedList<PatchOperation>();
    patchOps.add(PatchOperation.replace(Path.build().attribute("schemas", Filter.eq("value", "urn:ietf:params:scim:schemas:core:2.0:User")), TextNode.valueOf("urn:id:extWithOptAttr")));
    result = validator.onModify(patchOps, resource);
    assertEquals(result.syntax().toString(), result.syntax().size(), 3);//2);

    // shouldn't be able to replace a required schema extension
    patchOps = new LinkedList<PatchOperation>();
    patchOps.add(PatchOperation.replace(Path.build().attribute("schemas", Filter.eq("value", "urn:id:extWithReqAttr")), TextNode.valueOf("urn:id:extWithOptAttr")));
    result = validator.onModify(patchOps, resource);
    assertEquals(result.syntax().toString(), result.syntax().size(), 4);//3);

    // shouldn't be able to add an undefined schema extension
    patchOps = new LinkedList<PatchOperation>();
    patchOps.add(PatchOperation.add(Path.build().attribute("schemas"), Support.nodeFactory().arrayNode().add("urn:id:undefined")));
    result = validator.onModify(patchOps, resource);
    assertEquals(result.syntax().toString(), result.syntax().size(), 2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRequired
  /**
   ** Test to ensure required attributes are checked correctly.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testRequired()
    throws Exception {


    // optional attribute
    final AttributeDefinition optAttr  = AttributeDefinition.factory().name("test").type(Attribute.Type.STRING).required(false).build();
    // required attribute
    AttributeDefinition reqAttr         = AttributeDefinition.factory().name("test").type(Attribute.Type.STRING).required(true).build();
    // optional attribute, optional sub-attribute
    AttributeDefinition optAttrOptSub   = AttributeDefinition.factory().name("test").type(Attribute.Type.COMPLEX).required(false).sub(optAttr).build();
    // optional attribute, required sub-attribute
    AttributeDefinition optAttrReqSub   = AttributeDefinition.factory().name("test").type(Attribute.Type.COMPLEX).required(false).sub(reqAttr).build();
    // required attribute, required sub-attribute
    AttributeDefinition reqAttrReqSub   = AttributeDefinition.factory().name("test").type(Attribute.Type.COMPLEX).required(true).sub(reqAttr).build();
    // optional multi-valeud attribute
    AttributeDefinition optMVAttr       = AttributeDefinition.factory().name("test").type(Attribute.Type.STRING).required(false).multiValued(true).build();
    // required multi-valued attribute
    AttributeDefinition reqMVAttr       = AttributeDefinition.factory().name("test").type(Attribute.Type.STRING).required(true).multiValued(true).build();
    // optional multi-valued attribute, optional sub-attribute
    AttributeDefinition optMVAttrOptSub = AttributeDefinition.factory().name("test").type(Attribute.Type.COMPLEX).required(false).sub(optAttr).multiValued(true).build();
    // optional multi-valued attribute, required sub-attribute
    AttributeDefinition optMVAttrReqSub = AttributeDefinition.factory().name("test").type(Attribute.Type.COMPLEX).required(false).sub(reqAttr).multiValued(true).build();
    // required multi-valued attribute, required sub-attribute
    AttributeDefinition reqMVAttrReqSub = AttributeDefinition.factory().name("test").type(Attribute.Type.COMPLEX).required(true).sub(reqAttr).multiValued(true).build();

    // attribute not present
    final ObjectNode notPresent = Support.nodeFactory().objectNode();
    notPresent.putArray("schemas").add("urn:id:test");
    // attribute with null value
    final ObjectNode nullValue = Support.nodeFactory().objectNode();
    nullValue.putArray("schemas").add("urn:id:test");
    nullValue.putNull("test");
    // attribute with not present sub-attribute
    final ObjectNode subNotPresent = Support.nodeFactory().objectNode();
    subNotPresent.putArray("schemas").add("urn:id:test");
    subNotPresent.putObject("test");
    // attribute with null value sub-attribute
    final ObjectNode subNullValue = Support.nodeFactory().objectNode();
    subNullValue.putArray("schemas").add("urn:id:test");
    subNullValue.putObject("test").putNull("test");
    // attribute with empty array
    final ObjectNode emptyArray = Support.nodeFactory().objectNode();
    emptyArray.putArray("schemas").add("urn:id:test");
    emptyArray.putArray("test");
    // attribute with one element not present sub-attribute
    final ObjectNode arrayNotPresent = Support.nodeFactory().objectNode();
    arrayNotPresent.putArray("schemas").add("urn:id:test");
    arrayNotPresent.putArray("test").addObject();
    // attribute with one element null value sub-attribute
    final ObjectNode arrayNullValue = Support.nodeFactory().objectNode();
    arrayNullValue.putArray("schemas").add("urn:id:test");
    arrayNullValue.putArray("test").addObject().putNull("test");

    final Object[][] provider = new Object[][] {
        new Object[]{optAttr,         notPresent,      0}
      , new Object[]{optAttr,         nullValue,       0}
      , new Object[]{reqAttr,         notPresent,      1}
      , new Object[]{reqAttr,         nullValue,       1}
      , new Object[]{optAttrOptSub,   notPresent,      0}
      , new Object[]{optAttrOptSub,   nullValue,       0}
      , new Object[]{optAttrOptSub,   subNotPresent,   0}
      , new Object[]{optAttrOptSub,   subNullValue,    0}
      , new Object[]{optAttrReqSub,   notPresent,      0}
      , new Object[]{optAttrReqSub,   nullValue,       0}
      , new Object[]{optAttrReqSub,   subNotPresent,   1}
      , new Object[]{optAttrReqSub,   subNullValue,    1}
      , new Object[]{reqAttrReqSub,   notPresent,      1}
      , new Object[]{reqAttrReqSub,   nullValue,       1}
      , new Object[]{reqAttrReqSub,   subNotPresent,   1}
      , new Object[]{reqAttrReqSub,   subNullValue,    1}
      , new Object[]{optMVAttr,       notPresent,      0}
      , new Object[]{optMVAttr,       emptyArray,      0}
      , new Object[]{reqMVAttr,       notPresent,      1}
      , new Object[]{reqMVAttr,       emptyArray,      1}
      , new Object[]{optMVAttrOptSub, notPresent,      0}
      , new Object[]{optMVAttrOptSub, emptyArray,      0}
      , new Object[]{optMVAttrOptSub, arrayNotPresent, 0}
      , new Object[]{optMVAttrOptSub, arrayNullValue,  0}
      , new Object[]{optMVAttrReqSub, notPresent,      0}
      , new Object[]{optMVAttrReqSub, emptyArray,      0}
      , new Object[]{optMVAttrReqSub, arrayNotPresent, 1}
      , new Object[]{optMVAttrReqSub, arrayNullValue,  1}
      , new Object[]{reqMVAttrReqSub, notPresent,      1}
      , new Object[]{reqMVAttrReqSub, emptyArray,      1}
      , new Object[]{reqMVAttrReqSub, arrayNotPresent, 1}
      , new Object[]{reqMVAttrReqSub, arrayNullValue,  1}
    };
    for (int i = 0; i < provider.length; i++) {
      final AttributeDefinition definition  = (AttributeDefinition)provider[i][0];
      final ObjectNode     node             = (ObjectNode)provider[i][1];
      final Integer        expected         = (Integer)provider[i][2];
      testRequired(definition, node, expected);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testMutability
  /**
   ** Test the attribute mutability constratins are checked correctly.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testMutability()
    throws Exception {

    // read-only attribute
    final AttributeDefinition readOnly = AttributeDefinition.factory().name("readOnly").type(Attribute.Type.STRING).mutability(Attribute.Mutability.READ_ONLY).build();
    // immutable attribute
    final AttributeDefinition immutable = AttributeDefinition.factory().name("immutable").type(Attribute.Type.STRING).mutability(Attribute.Mutability.IMMUTABLE).build();

    final List<AttributeDefinition> definition = new ArrayList<AttributeDefinition>();
    definition.add(readOnly);
    definition.add(immutable);

    final SchemaResource         schema    = new SchemaResource("urn:id:test", "test", "", definition);
    final ResourceTypeDefinition type      = ResourceTypeDefinition.builder("test", "/test").core(schema).build();
    final SchemaValidator        validator = SchemaValidator.of(type);

    // can not create read-only
    ObjectNode o = Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.put("readOnly", "value");
    SchemaValidator.Result result = validator.onCreate(o);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "read-only"));

    // can not replace read-only
    result = validator.onReplace(o, null);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "read-only"));

    // can not add read-only in patch
    result = validator.onModify(Collections.singleton( PatchOperation.add(Path.build().attribute("readOnly"), TextNode.valueOf("value"))), null);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "read-only"));

    // can not remove read-only in patch
    result = validator.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("readOnly"))), null);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "read-only"));

    // can not replace read-only in patch
    result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("readOnly"), TextNode.valueOf("value"))), null);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "read-only"));

    // can create immutable
    o =Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.put("immutable", "value");
    result = validator.onCreate(o);
    assertTrue(result.mutability().toString(), result.mutability().isEmpty());

    // can replace immutable if not already present
    o =Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.put("immutable", "value");
    result = validator.onReplace(o, null);
    assertTrue(result.mutability().toString(), result.mutability().isEmpty());

    // Ccan replace if it is the same
    o =Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.put("immutable", "value");
    result = validator.onReplace(o, o);
    assertTrue(result.mutability().toString(), result.mutability().isEmpty());

    // can not replace if value already present and different
    o =Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.put("immutable", "value");
    result = validator.onReplace(o.deepCopy().put("immutable", "newValue"), o);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "immutable"));

    // can add immutable in patch if not already present
    result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("immutable"), TextNode.valueOf("value"))), null);
    assertTrue(result.mutability().toString(), result.mutability().isEmpty());

    // can not replace immutable in patch if not already present
    result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("immutable"), TextNode.valueOf("value"))), null);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "immutable"));

    // can not add immutable in patch if it is the same
    result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("immutable"), TextNode.valueOf("value"))), o);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "immutable"));

    // can not replace immutable in patch if it is the same
    result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("immutable"), TextNode.valueOf("value"))), o);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "immutable"));

    // can not add immutable in patch if already present and different
    result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("immutable"), TextNode.valueOf("newValue"))), o);
    assertEquals(result.mutability().toString(), result.mutability().size(), 2);
    assertTrue(containsIssue(result.mutability(), "immutable"));

    // can not replace immutable in patch if already present and different
    result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("immutable"), TextNode.valueOf("newValue"))), o);
    assertEquals(result.mutability().toString(), result.mutability().size(), 2);
    assertTrue(containsIssue(result.mutability(), "immutable"));

    // can not remove immutable
    result = validator.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("immutable"))), null);
    assertEquals(result.mutability().toString(), result.mutability().size(), 1);
    assertTrue(containsIssue(result.mutability(), "immutable"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSimpleMultiValuedAttribute
  /**
   ** Test to ensure simple, multi-valued attributes has subvalues accessible by
   ** be removed or replaced.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testSimpleMultiValuedAttribute()
    throws Exception {

    // use the test schema, and add multiple sub-attribute values to the
    // mvstring simple attribute
    final ResourceTypeDefinition type      = ResourceTypeDefinition.builder("test", "/test").core(test).build();
    final SchemaValidator        validator = SchemaValidator.of(type);
    // create array node with the sub-attributes used for testing
    final ArrayNode values = Support.nodeFactory().arrayNode();
    values.add(Support.nodeFactory().textNode("value1"));
    values.add(Support.nodeFactory().textNode("value2"));
    values.add(Support.nodeFactory().textNode("value3"));
    // create the node for testing, and set the mvstring test values
    ObjectNode o = Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.set("mvstring", values);

    // create result schema validator for this test.
    SchemaValidator.Result result = validator.onCreate(o);
    // attempt a patch modify with a replace operation
    result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("mvstring", Filter.eq("value","value3")), "replacedValue4")), null);
    assertEquals(Arrays.toString(result.filter().toArray(new String[0])), result.filter().size(), 0);
    // attempt a patch modify with a remove operation
    result = validator.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("mvstring", Filter.eq("value","value1")))), null);
    assertEquals(Arrays.toString(result.filter().toArray(new String[0])), result.filter().size(), 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUndefinedAttribute
  /**
   ** Test that undefined attributes are detected correctly.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testUndefinedAttribute()
    throws Exception {

    // core attribute is undefined
    final ObjectNode node = Support.nodeFactory().objectNode();
    node.putArray("schemas").add(UserResource.ID).add(EnterpriseUserResource.ID);
    node.put("userName", "test");
    node.put("undefined", "value");

    final SchemaResource         core       = SchemaFactory.schema(UserResource.class);
    final SchemaResource         enterprise = SchemaFactory.schema(EnterpriseUserResource.class);
    final ResourceTypeDefinition type       = ResourceTypeDefinition.builder("test", "/test").core(core).optional(enterprise).build();
    final SchemaValidator        validator  = SchemaValidator.of(type);
    SchemaValidator.Result result = validator.onCreate(node);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "is undefined for schema"));

    result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onSearch(Filter.from("undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 1);
    assertTrue(containsIssue(result.filter(), "is undefined"));

    // core sub-attribute is undefined
    final ObjectNode coreSubUndefined = Support.nodeFactory().objectNode();
    coreSubUndefined.putArray("schemas").add(UserResource.ID).add(EnterpriseUserResource.ID);
    coreSubUndefined.put("userName", "test");
    coreSubUndefined.putObject("name").put("undefined", "value");

    result = validator.onCreate(coreSubUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "is undefined for attribute"));

    result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("name").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("name").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("name").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onSearch(Filter.from("name.undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 1);
    assertTrue(containsIssue(result.filter(), "is undefined"));

    // Extended attribute is undefined
    final ObjectNode extendedUndefined = Support.nodeFactory().objectNode();
    extendedUndefined.putArray("schemas").add(UserResource.ID).add(EnterpriseUserResource.ID);
    extendedUndefined.put("userName", "test");
    extendedUndefined.putObject("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").put("undefined", "value");

    result = validator.onCreate(extendedUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "is undefined for schema"));

    result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onModify(Collections.singleton(PatchOperation.remove(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onSearch(Filter.from("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 1);
    assertTrue(containsIssue(result.filter(), "is undefined"));

    // xxtended sub-attribute is undefined
    ObjectNode extendedSubUndefined = Support.nodeFactory().objectNode();
    extendedSubUndefined.putArray("schemas").add(UserResource.ID).add(EnterpriseUserResource.ID);
    extendedSubUndefined.put("userName", "test");
    extendedSubUndefined.putObject(EnterpriseUserResource.ID).putObject("manager").put("$ref", "https://value").put("value", "value").put("undefined", "value");

    result = validator.onCreate(extendedSubUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "is undefined for attribute"));

    result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build(EnterpriseUserResource.ID).attribute("manager").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build(EnterpriseUserResource.ID).attribute("manager").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onModify(Collections.singleton(PatchOperation.remove(Path.build(EnterpriseUserResource.ID).attribute("manager").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = validator.onSearch(Filter.from("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:manager.undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 1);
    assertTrue(containsIssue(result.filter(), "is undefined"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAllowUndefinedAttribute
  /**
   ** Test case for the allow undefined attribute options.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testAllowUndefinedAttribute()
    throws Exception {


    final SchemaResource         core       = SchemaFactory.schema(UserResource.class);
    final SchemaResource         extension  = SchemaFactory.schema(EnterpriseUserResource.class);
    final ResourceTypeDefinition type       = ResourceTypeDefinition.builder("test", "/test").core(core).optional(extension).build();
    final SchemaValidator        relax      = SchemaValidator.of(type).with(SchemaValidator.Option.RELAX);
    final SchemaValidator        weak       = SchemaValidator.of(type).with(SchemaValidator.Option.WEAK);

    // core attribute is undefined
    ObjectNode coreUndefined =Support.nodeFactory().objectNode();
    coreUndefined.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User");
    coreUndefined.put("userName", "test");
    coreUndefined.put("undefined", "value");

    SchemaValidator.Result result = relax.onCreate(coreUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 0);

    result = weak.onCreate(coreUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "is undefined for schema"));

    result = relax.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = weak.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = relax.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = weak.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = relax.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = weak.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = relax.onSearch(Filter.from("undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 0);

    result = weak.onSearch(Filter.from("undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 1);
    assertTrue(containsIssue(result.filter(), "is undefined"));

    // core sub-attribute is undefined
    ObjectNode coreSubUndefined =Support.nodeFactory().objectNode();
    coreSubUndefined.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User");
    coreSubUndefined.put("userName", "test");
    coreSubUndefined.putObject("name").put("undefined", "value");

    result = relax.onCreate(coreSubUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "is undefined for attribute"));

    result = weak.onCreate(coreSubUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 0);

    result = relax.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("name").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = weak.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("name").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = relax.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("name").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = weak.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("name").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = relax.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("name").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = weak.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("name").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = relax.onSearch(Filter.from("name.undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 1);
    assertTrue(containsIssue(result.filter(), "is undefined"));

    result = weak.onSearch(Filter.from("name.undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 0);

    // extended attribute namespace is undefined
    ObjectNode extendedUndefined =Support.nodeFactory().objectNode();
    extendedUndefined.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User");
    extendedUndefined.put("userName", "test");
    extendedUndefined.putObject("urn:undefined").put("undefined", "value");

    // this should still be an error since all namespaces must be defined in the
    // schemas attribute.
    result = relax.onCreate(extendedUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);

    extendedUndefined =Support.nodeFactory().objectNode();
    extendedUndefined.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:undefined");
    extendedUndefined.put("userName", "test");
    extendedUndefined.putObject("urn:undefined").put("undefined", "value");

    result = weak.onCreate(extendedUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "is undefined"));

    result = relax.onModify(Collections.singleton(PatchOperation.add(Path.build("urn:undefined").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = weak.onModify(Collections.singleton(PatchOperation.add(Path.build("urn:undefined").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = relax.onModify(Collections.singleton(PatchOperation.replace(Path.build("urn:undefined").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = weak.onModify(Collections.singleton(PatchOperation.replace(Path.build("urn:undefined").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = relax.onModify(Collections.singleton(PatchOperation.remove(Path.build("urn:undefined").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = weak.onModify(Collections.singleton(PatchOperation.remove(Path.build("urn:undefined").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = relax.onSearch(Filter.from("urn:undefined:undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 0);

    result = weak.onSearch(Filter.from("urn:undefined:undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 1);
    assertTrue(containsIssue(result.filter(), "is undefined"));

    // extended attribute is undefined
    extendedUndefined =Support.nodeFactory().objectNode();
    extendedUndefined.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User");
    extendedUndefined.put("userName", "test");
    extendedUndefined.putObject("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").put("undefined", "value");

    result = relax.onCreate(extendedUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 0);

    result = weak.onCreate(extendedUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "is undefined for schema"));

    result = relax.onModify(Collections.singleton(PatchOperation.add(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = weak.onModify(Collections.singleton(PatchOperation.add(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = relax.onModify(Collections.singleton(PatchOperation.replace(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = weak.onModify(Collections.singleton(PatchOperation.replace(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = relax.onModify(Collections.singleton(PatchOperation.remove(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);

    result = weak.onModify(Collections.singleton(PatchOperation.remove(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);
    assertTrue(containsIssue(result.path(), "is undefined"));

    result = relax.onSearch(Filter.from("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 0);

    result = weak.onSearch(Filter.from("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 1);
    assertTrue(containsIssue(result.filter(), "is undefined"));

    // extended sub-attribute is undefined
    ObjectNode extendedSubUndefined = Support.nodeFactory().objectNode();
    extendedSubUndefined.putArray("schemas").add("urn:ietf:params:scim:schemas:core:2.0:User").add("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User");
    extendedSubUndefined.put("userName", "test");
    extendedSubUndefined.putObject("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").putObject("manager").put("$ref", "https://value").put("value", "value").put("undefined", "value");

    result = relax.onCreate(extendedSubUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "is undefined for attribute"));

    result = weak.onCreate(extendedSubUndefined);
    assertEquals(result.syntax().toString(), result.syntax().size(), 0);

    result = relax.onModify(Collections.singleton(PatchOperation.add(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("manager").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);
    assertFalse(containsIssue(result.path(), "is undefined"));

    result = weak.onModify(Collections.singleton(PatchOperation.add(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("manager").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);

    result = relax.onModify(Collections.singleton(PatchOperation.replace(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("manager").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);
    assertFalse(containsIssue(result.path(), "is undefined"));

    result = weak.onModify(Collections.singleton(PatchOperation.replace(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("manager").attribute("undefined"), TextNode.valueOf("value"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);

    result = relax.onModify(Collections.singleton(PatchOperation.remove(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("manager").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 0);
    assertFalse(containsIssue(result.path(), "is undefined"));

    result = weak.onModify(Collections.singleton(PatchOperation.remove(Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("manager").attribute("undefined"))), null);
    assertEquals(result.path().toString(), result.path().size(), 1);

    result = relax.onSearch(Filter.from("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:manager.undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 0);
    assertFalse(containsIssue(result.filter(), "is undefined"));

    result = weak.onSearch(Filter.from("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:manager.undefined eq \"value\""));
    assertEquals(result.filter().toString(), result.filter().size(), 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testBinaryAttribute
  /**
   ** Test to ensure binary attribute values are checked for the correct type,
   ** regardless if they are represented as BinaryNode or TextNode.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testBinaryAttribute()
    throws Exception {

    final ResourceTypeDefinition type      = ResourceTypeDefinition.builder("test", "/test").core(test).build();
    final SchemaValidator        validator = SchemaValidator.of(type);

    // test as TextNode
    ObjectNode o =Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.set("binary",Support.nodeFactory().textNode(Base64Variants.getDefaultVariant().encode(new byte[]{0x00})));
    SchemaValidator.Result result = validator.onCreate(o);
    assertEquals(result.syntax().toString(), result.syntax().size(), 0);

    // test as BinaryNode
    o =Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.set("binary",Support.nodeFactory().binaryNode(new byte[]{0x00}));
    result = validator.onCreate(o);
    assertEquals(result.syntax().toString(), result.syntax().size(), 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAttributeValueType
  /**
   ** Test to ensure attribute values are checked for the correct type.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testAttributeValueTyp()
    throws Exception {

    final Object[][] provider = new Object[][] {
        // wrong attribute value types
        new Object[]{"string",          Support.nodeFactory().numberNode(1)}
      , new Object[]{"string",          Support.nodeFactory().booleanNode(true)}
      , new Object[]{"string",          Support.nodeFactory().objectNode()}
      , new Object[]{"string",          Support.nodeFactory().arrayNode()}
      , new Object[]{"stringCanonical", Support.nodeFactory().textNode("value3")}
      , new Object[]{"datetime",        Support.nodeFactory().textNode("notdatetime")}
      , new Object[]{"datetime",        Support.nodeFactory().numberNode(1)}
      , new Object[]{"datetime",        Support.nodeFactory().booleanNode(true)}
      , new Object[]{"datetime",        Support.nodeFactory().objectNode()}
      , new Object[]{"datetime",        Support.nodeFactory().arrayNode()}
      , new Object[]{"binary",          Support.nodeFactory().textNode("()$#@_@")}
      , new Object[]{"binary",          Support.nodeFactory().numberNode(1)}
      , new Object[]{"binary",          Support.nodeFactory().booleanNode(true)}
      , new Object[]{"binary",          Support.nodeFactory().objectNode()}
      , new Object[]{"binary",          Support.nodeFactory().arrayNode()}
      , new Object[]{"reference",       Support.nodeFactory().textNode("rtp:\\")}
      , new Object[]{"reference",       Support.nodeFactory().numberNode(1)}
      , new Object[]{"reference",       Support.nodeFactory().booleanNode(true)}
      , new Object[]{"reference",       Support.nodeFactory().objectNode()}
      , new Object[]{"reference",       Support.nodeFactory().arrayNode()}
      , new Object[]{"boolean",         Support.nodeFactory().textNode("string")}
      , new Object[]{"boolean",         Support.nodeFactory().numberNode(1)}
      , new Object[]{"boolean",         Support.nodeFactory().objectNode()}
      , new Object[]{"boolean",         Support.nodeFactory().arrayNode()}
      , new Object[]{"decimal",         Support.nodeFactory().textNode("string")}
      , new Object[]{"decimal",         Support.nodeFactory().booleanNode(true)}
      , new Object[]{"decimal",         Support.nodeFactory().objectNode()}
      , new Object[]{"decimal",         Support.nodeFactory().arrayNode()}
      , new Object[]{"integer",         Support.nodeFactory().textNode("string")}
      , new Object[]{"integer",         Support.nodeFactory().booleanNode(true)}
      , new Object[]{"integer",         Support.nodeFactory().objectNode()}
      , new Object[]{"integer",         Support.nodeFactory().arrayNode()}
      , new Object[]{"integer",         Support.nodeFactory().numberNode(1.1)}
      , new Object[]{"complex",         Support.nodeFactory().textNode("string")}
      , new Object[]{"complex",         Support.nodeFactory().numberNode(1)}
      , new Object[]{"complex",         Support.nodeFactory().booleanNode(true)}
    };
    for (int i = 0; i < provider.length; i++) {
      final String   field = (String)provider[i][0];
      final JsonNode node  = (JsonNode)provider[i][1];
      testAttributeValueType(field, node);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testValidationException
  /**
   ** Test that the proper exceptions are thrown if errors are found during
   ** schema checking.
   ** <br>
   ** This test uses a data provider for its data, and uses reflection to set
   ** private fields of the results to make the test simpler.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testValidationException()
    throws Exception {

    final Object[][] provider = new Object[][] {
        new Object[]{"syntaxIssueOne, syntaxIssueTwo",         Arrays.asList("syntaxIssueOne", "syntaxIssueTwo"), Collections.emptyList(),                                   Collections.emptyList()}
      , new Object[]{"mutabilityIssueOne, mutabilityIssueTwo", Collections.emptyList(),                           Arrays.asList("mutabilityIssueOne", "mutabilityIssueTwo"), Collections.emptyList()}
      , new Object[]{"pathIssueOne, pathIssueTwo",             Collections.emptyList(),                           Collections.emptyList(),                                   Arrays.asList("pathIssueOne", "pathIssueTwo")}
      , new Object[]{"syntaxIssueOne, syntaxIssueTwo",         Arrays.asList("syntaxIssueOne", "syntaxIssueTwo"), Arrays.asList("mutabilityIssueOne", "mutabilityIssueTwo"), Arrays.asList("pathIssueOne", "pathIssueTwo")}
      , new Object[]{"mutabilityIssueOne",                     Collections.emptyList(),                           Arrays.asList("mutabilityIssueOne"),                       Arrays.asList("pathIssueOne", "pathIssueTwo")}
      , new Object[]{null,                                     Collections.emptyList(),                           Collections.emptyList(),                                   Collections.emptyList()}
    };
    for (int i = 0; i < provider.length; i++) {
      final String       expected   = (String)provider[i][0];
      final List<String> syntax     = (List<String>)provider[i][1];
      final List<String> mutability = (List<String>)provider[i][2];
      final List<String> path       = (List<String>)provider[i][3];
      testValidationException(expected, syntax, mutability, path);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSchemaExtension
  /**
   ** Test to ensure schema extensions are checked correctly.
   **
   ** @param  extension          the schema extension.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  required           whether the schema extension is required.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  node               the object node to check.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  expectedOnCreate   the amount of expected errors on create.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  expectedOnPatch    the amount of expected errors on patch.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws Exception          if an error occurs.
   */
  private void testSchemaExtension(final SchemaResource extension, final boolean required, final ObjectNode node, final int expectedOnCreate, final int expectedOnPatch)
    throws Exception {

    final ResourceTypeDefinition type       = required ? ResourceTypeDefinition.builder("test", "/test").core(core).required(extension).build() : ResourceTypeDefinition.builder("test", "/test").core(core).optional(extension).build();
    final SchemaValidator        validator  = SchemaValidator.of(type);
    SchemaValidator.Result       result     = validator.onCreate(node);
    assertEquals(result.syntax().toString(), result.syntax().size(), expectedOnCreate);

    // partial patch
    result = validator.onModify(Collections.singleton(PatchOperation.add(node)), null);
    assertEquals(result.syntax().toString(), result.syntax().size(), expectedOnPatch);

    result = validator.onModify(Collections.singleton(PatchOperation.replace(node)), null);
    assertEquals(result.syntax().toString(), result.syntax().size(), expectedOnPatch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRequired
  /**
   ** Test to ensure required attributes are checked correctly.
   **
   ** @param  definition         the attribute definition.
   **                            <br>
   **                            Allowed object is {@link AttributeDefinition}.
   ** @param  node               the object node to check.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  expect             whether an error is expected.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws Exception          if an error occurs.
   */
  private void testRequired(final AttributeDefinition definition, final ObjectNode node, final int expect)
    throws Exception {

    final SchemaResource         schema    = new SchemaResource("urn:id:test", "test", "", Collections.singleton(definition));
    final ResourceTypeDefinition type      = ResourceTypeDefinition.builder("test", "/test").core(schema).build();
    final SchemaValidator        validator = SchemaValidator.of(type);
    
    SchemaValidator.Result result = validator.onCreate(node);
    assertEquals(result.syntax().toString(), result.syntax().size(), expect);
    // can't remove required attributes in patch
    if (definition.required()) {
      // verify case insensitive behavior
      result = validator.onModify(Collections.singleton(PatchOperation.remove(Path.from("TEST"))), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    }
    if (definition.attributes() != null && definition.attributes().iterator().next().required()) {
      result = validator.onModify(Collections.singleton(PatchOperation.remove(Path.build().attribute("test").attribute("test"))), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAttributeValueType
  /**
   ** Test to ensure attribute values are checked for the correct type.
   **
   ** @param  name               the attribute name to test.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  node               the value node to test.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @throws Exception          if an error occurs.
   */
  private void testAttributeValueType(final String name, final JsonNode node)
    throws Exception {

    final ResourceTypeDefinition type      = ResourceTypeDefinition.builder("test", "/test").core(test).build();
    final SchemaValidator        validator = SchemaValidator.of(type);

    // first test as an attribute
    ObjectNode o = Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.set(name, node);

    SchemaValidator.Result result = validator.onCreate(o);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "Value"));

    if (!((node.isArray() || node.isObject()) && node.size() == 0)) {
      // partial patch
      result = validator.onModify(Collections.singleton(PatchOperation.add(o)), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));

      result = validator.onModify(Collections.singleton(PatchOperation.replace(o)), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));

      // path'ed patch
      result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute(name), node)), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));

      result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute(name), node)), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));

      result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute(name, Filter.eq("test", "test")), node)), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.path(), "not multi-valued"));
    }

    // then test a an sub-attribute
    if (!name.equals("complex")) {
      o =Support.nodeFactory().objectNode();
      o.putArray("schemas").add("urn:id:test");
      o.putObject("complex").set(name, node);
      result = validator.onCreate(o);
      assertFalse(result.syntax().toString(), result.syntax().isEmpty());
      assertTrue(containsIssue(result.syntax(), "Value"));

      if (!((node.isArray() || node.isObject()) && node.size() == 0)) {
        // partial patch
        result = validator.onModify(Collections.singleton(PatchOperation.add(o)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));

        result = validator.onModify(Collections.singleton(PatchOperation.replace(o)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));

        // path'ed patch
        result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("complex").attribute(name), node)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));

        result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("complex").attribute(name), node)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));
      }
    }

    // then test as a single-value for multi-valued attributes
    if (!node.isArray()) {
      o = Support.nodeFactory().objectNode();
      o.putArray("schemas").add("urn:id:test");
      o.set("mv" + name, node);
      result = validator.onCreate(o);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));

      if (!((node.isArray() || node.isObject()) && node.size() == 0)) {
        // partial patch
        result = validator.onModify(Collections.singleton(PatchOperation.add(o)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));

        result = validator.onModify(Collections.singleton(PatchOperation.replace(o)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));

        // path'ed patch
        result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("mv" + name), node)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));

        result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("mv" + name), node)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));
      }
    }

    // then test as a multi-valued attribute
    o = Support.nodeFactory().objectNode();
    o.putArray("schemas").add("urn:id:test");
    o.putArray("mv" + name).add(node);
    result = validator.onCreate(o);
    assertEquals(result.syntax().toString(), result.syntax().size(), 1);
    assertTrue(containsIssue(result.syntax(), "Value"));

    if (!((node.isArray() || node.isObject()) && node.size() == 0)) {
      // partial patch
      result = validator.onModify(Collections.singleton(PatchOperation.add(o)), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));

      result = validator.onModify(Collections.singleton(PatchOperation.replace(o)), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));

      // path'ed patch
      result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("mv" + name), Support.nodeFactory().arrayNode().add(node))), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));

      result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("mv" + name), Support.nodeFactory().arrayNode().add(node))), null);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));
    }

    // finally test as a sub-attribute of a multi-valued attribute
    if (!name.equals("complex")) {
      o =Support.nodeFactory().objectNode();
      o.putArray("schemas").add("urn:id:test");
      o.putArray("mvcomplex").addObject().set(name, node);
      result = validator.onCreate(o);
      assertEquals(result.syntax().toString(), result.syntax().size(), 1);
      assertTrue(containsIssue(result.syntax(), "Value"));

      if (!((node.isArray() || node.isObject()) && node.size() == 0)) {
        // partial patch
        result = validator.onModify(Collections.singleton(PatchOperation.add(o)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));

        result = validator.onModify(Collections.singleton(PatchOperation.replace(o)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));

        // path'ed patch
        result = validator.onModify(Collections.singleton(PatchOperation.add(Path.build().attribute("mvcomplex").attribute(name), node)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));

        result = validator.onModify(Collections.singleton(PatchOperation.replace(Path.build().attribute("mvcomplex").attribute(name), node)), null);
        assertEquals(result.syntax().toString(), result.syntax().size(), 1);
        assertTrue(containsIssue(result.syntax(), "Value"));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testValidationException
  /**
   ** Test that the proper exceptions are thrown if errors are found during
   ** schema checking.
   ** <br>
   ** This test uses a data provider for its data, and uses reflection to set
   ** private fields of the result to make the test simpler.
   **
   ** @param  expected           the expected exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  syntax             a collection of syntax issues.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   ** @param  mutability         a collection of mutability issues.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   ** @param  path               a collection of path issues.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws Exception          if an error occurs.
   */
  private void testValidationException(final String expected, final List<String> syntax, final List<String> mutability, final List<String> path)
    throws Exception {

    BadRequestException caught = null;

    final SchemaValidator.Result result = results(syntax, mutability, path);
    try {
      result.throwViolation();
    }
    catch(BadRequestException e) {
      caught = e;
      assertEquals(caught.error().detail(), expected);
    }

    if (!syntax.isEmpty()) {
      assertNotNull(caught);
      assertEquals(caught.error().type(), BadRequestException.INVALID_SYNTAX);
    }
    else if (!mutability.isEmpty()) {
      assertNotNull(caught);
      assertEquals(caught.error().type(), BadRequestException.MUTABILITY);
    }
    else if (!path.isEmpty()) {
      assertNotNull(caught);
      assertEquals(caught.error().type(), BadRequestException.INVALID_PATH);
    }
    else {
      assertNull("Bad exception thrown", caught);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   results
  /**
   ** Test that the proper exceptions are thrown if errors are found during
   ** schema checking.
   ** <br>
   ** This test uses a data provider for its data, and uses reflection to set
   ** private fields of the result to make the test simpler.
   **
   ** @param  syntax             a collector for syntax issues.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   ** @param  mutability         a collector for mutability issues.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   ** @param  path               a collector for path issues.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws Exception          if an error occurs.
   */
  private SchemaValidator.Result results(final List<String> syntax, final List<String> mutability, final List<String> path)
    throws Exception {

    final SchemaValidator.Result result = new SchemaValidator.Result();

    Field mutator = SchemaValidator.Result.class.getDeclaredField("syntax");
    mutator.setAccessible(true);
    mutator.set(result, syntax);

    mutator = SchemaValidator.Result.class.getDeclaredField("path");
    mutator.setAccessible(true);
    mutator.set(result, path);

    mutator = SchemaValidator.Result.class.getDeclaredField("mutability");
    mutator.setAccessible(true);
    mutator.set(result, mutability);

    return result;
  }

  private boolean containsIssue(final Collection<String> issue, final String text) {
    for (String cursor : issue) {
      if (cursor.contains(text)) {
        return true;
      }
    }
    return false;
  }
}