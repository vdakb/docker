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
    Subsystem   :   Generic WebService Connector

    File        :   TestFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector;

import java.math.BigDecimal;

import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.scim.annotation.Schema;
import oracle.iam.identity.icf.scim.annotation.Attribute;
import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.scim.domain.type.MultiValued;

import oracle.iam.identity.icf.scim.schema.Entity;
import oracle.iam.identity.icf.scim.schema.Support;

import oracle.iam.identity.icf.scim.v2.schema.SchemaFactory;
import oracle.iam.identity.icf.scim.v2.schema.SchemaResource;

////////////////////////////////////////////////////////////////////////////////
// class TestFactory
// ~~~~~ ~~~~~~~~~~~
/**
 ** The test case to generate the schema for the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Required
  // ~~~~ ~~~~~~~~
  private enum Required {REQUIRED, NOT_REQUIRED,DEFAULT}

  //////////////////////////////////////////////////////////////////////////////
  // enum CaseExact
  // ~~~~ ~~~~~~~~~
  private enum CaseExact {CASE_EXACT, NOT_CASE_EXACT, DEFAULT}

  //////////////////////////////////////////////////////////////////////////////
  // enum Multivalued
  // ~~~~ ~~~~~~~~~~~
  private enum Multivalued {MULTIVALUED, NOT_MULTIVALUED, DEFAULT}

  //////////////////////////////////////////////////////////////////////////////
  // class TestObject1
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** Test class.
   */
  @Schema(id="urn:id:junit", description="description:TestObject1", name="name:TestObject1")
  public static class TestObject1 {

  }

  //////////////////////////////////////////////////////////////////////////////
  // class TestObject2
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** Test class.
   */
  @Schema(id="urn:id:TestObject2", description="description:TestObject2", name="name:TestObject2")
  public class TestObject2 extends Entity {

    @Attribute(description="description:stringField", required=true, caseExact=true)
    private String stringField;

    @Attribute(description="description:booleanObjectField", caseExact=true, returned=Definition.Returned.REQUEST)
    private Boolean booleanObjectField;

    @Attribute(description="description:booleanField", returned=Definition.Returned.NEVER)
    private boolean booleanField;

    @Attribute(description="description:integerObjectField", required=true)
    private Integer integerObjectField;

    @Attribute(description="description:integerField", required=true, caseExact=true, returned=Definition.Returned.ALWAYS)
    private int integerField;

    @Attribute(description="description:mutabilityImmutable", mutability=Definition.Mutability.IMMUTABLE)
    private String mutabilityImmutable;

    @Attribute(description="description:mutabilityReadWrite")
    private String mutabilityReadWrite;

    @Attribute(description="description:mutabilityWriteOnly", mutability=Definition.Mutability.WRITE_ONLY)
    private String mutabilityWriteOnly;

    @Attribute(description="description:mutabilityReadOnly", mutability=Definition.Mutability.READ_ONLY)
    private String mutabilityReadOnly;

    @Attribute(description="description:bigDecimal", mutability=Definition.Mutability.READ_ONLY)
    private BigDecimal bigDecimal;

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public String getStringField() {
      return stringField;
    }

    /**
     * Setter for attribute in test class.
     * @param stringField  attribute value.
     */
    public void setStringField(String stringField) {
      this.stringField = stringField;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public Boolean getBooleanObjectField() {
      return booleanObjectField;
    }

    /**
     * Setter for attribute in test class.
     * @param booleanObjectField  attribute value.
     */
    public void setBooleanObjectField(Boolean booleanObjectField) {
      this.booleanObjectField = booleanObjectField;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public boolean isBooleanField() {
      return booleanField;
    }

    /**
     * Setter for attribute in test class.
     * @param booleanField  attribute value.
     */
    public void setBooleanField(boolean booleanField) {
      this.booleanField = booleanField;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public Integer getIntegerObjectField() {
      return integerObjectField;
    }

    /**
     * Setter for attribute in test class.
     * @param integerObjectField  attribute value.
     */
    public void setIntegerObjectField(Integer integerObjectField) {
      this.integerObjectField = integerObjectField;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public int getIntegerField() {
      return integerField;
    }

    /**
     * Setter for attribute in test class.
     * @param integerField  attribute value.
     */
    public void setIntegerField(int integerField) {
      this.integerField = integerField;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public String getMutabilityImmutable() {
      return mutabilityImmutable;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public String getMutabilityReadOnly() {
      return mutabilityReadOnly;
    }

    /**
     * Setter for attribute in test class.
     * @param mutabilityWriteOnly  attribute value.
     */
    public void setMutabilityWriteOnly(String mutabilityWriteOnly) {
      this.mutabilityWriteOnly = mutabilityWriteOnly;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public String getMutabilityReadWrite() {
      return mutabilityReadWrite;
    }

    /**
     * Setter for attribute in test class.
     * @param mutabilityReadWrite  attribute value.
     */
    public void setMutabilityReadWrite(String mutabilityReadWrite) {
      this.mutabilityReadWrite = mutabilityReadWrite;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public BigDecimal getBigDecimal() {
      return bigDecimal;
    }

    /**
     * Setter for attribute in test class.
     * @param bigDecimal attribute value.
     */
    public void setBigDecimal(BigDecimal bigDecimal) {
      this.bigDecimal = bigDecimal;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class TestObject3_a
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Test class.
   */
  public static class TestObject3_a extends Entity {

    @Attribute(description = "description:stringField_3a") private String stringField_3a;

    /**
     * Getter for attribute in test class.
     * @return  attribute value.
     */
    public String getStringField_3a() {
      return stringField_3a;
    }

    /**
     * Setter for attribute in test class.
     * @param stringField_3a attribute value.
     */
    public void setStringField_3a(String stringField_3a) {
      this.stringField_3a = stringField_3a;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class TestObject3_b
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Test class.
   */
  public class TestObject3_b extends MultiValued<String> {
  }

  //////////////////////////////////////////////////////////////////////////////
  // class TestObject3_c
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Test class.
   */
  public static class TestObject3_c extends MultiValued<TestObject3_a> {
  }

  //////////////////////////////////////////////////////////////////////////////
  // class TestObject3_d
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Test class.
   */
  public static class TestObject3_d {
  }

  //////////////////////////////////////////////////////////////////////////////
  // class TestObject3
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** Test class.
   */
  @Schema(id = "urn:com.unboundid:schemas:TestObject3", description = "description:TestObject3", name = "TestObject3")
  public class TestObject3 extends Entity {

    @Attribute(description = "description:complexObject")
    private TestObject3_a complexObject;

    @Attribute(description = "description:multiValuedString", multiValueClass = TestObject3_b.class, canonical = { "one", "two", "three" })
    private List<MultiValued<String>> multiValuedString;

    @Attribute(description = "description:multiValuedComplex", multiValueClass = TestObject3_c.class, canonical = { "a", "b", "c" })
    private List<TestObject3_c> multiValuedComplex;

     @Attribute(description="description:multiValuedField_missingType", multiValueClass=TestObject3_d.class, canonical={"one", "two", "three"})
    private List<TestObject3_d> multiValuedField_missingType;

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public TestObject3_a getComplexObject() {
      return complexObject;
    }

    /**
     * Setter for attribute in test class.
     * @param complexObject attribute value.
     */
    public void setComplexObject(TestObject3_a complexObject) {
      this.complexObject = complexObject;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public List<TestObject3_c> getMultiValuedComplex() {
      return multiValuedComplex;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public List<MultiValued<String>> getMultiValuedString() {
      return multiValuedString;
    }

    /**
     * Setter for attribute in test class.
     * @param multiValuedString attribute value.
     */
    public void setMultiValuedString(List<MultiValued<String>> multiValuedString) {
      this.multiValuedString = multiValuedString;
    }

    /**
     * Setter for attribute in test class.
     * @param multiValuedComplex attribute value.
     */
    public void setMultiValuedComplex(List<TestObject3_c> multiValuedComplex) {
      this.multiValuedComplex = multiValuedComplex;
    }

    /**
     * Getter for attribute in test class.
     * @return attribute value.
     */
    public List<TestObject3_d> getMultiValuedField_missingType() {
      return multiValuedField_missingType;
    }

    /**
     * Setter for attribute in test class.
     * @param multiValuedField_missingType attribute value.
     */
    public void setMultiValuedField_missingType(List<TestObject3_d> multiValuedField_missingType) {
     this.multiValuedField_missingType = multiValuedField_missingType;
    }
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
   **
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    final ObjectMapper mapper = new ObjectMapper();
    try {
      SchemaResource schemaDefinition = SchemaFactory.schema(TestObject1.class);
      System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaDefinition));

      schemaDefinition = SchemaFactory.schema(TestObject2.class);
      tc2_checkSchema(schemaDefinition);
      List<String> expectedAttributes = CollectionUtility.list("stringField", "booleanObjectField", "booleanField", "integerObjectField", "integerField", "mutabilityReadWrite", "mutabilityReadOnly", "mutabilityWriteOnly", "mutabilityImmutable", "bigDecimal");
      for (Definition attribute : schemaDefinition.attribute()) {
        if (attribute.name().equals("stringField")) {
          // @Attrbibute(description="description:stringField", required=true, caseExact=true)
          checkAttribute(attribute, "description:stringField", Required.REQUIRED, CaseExact.CASE_EXACT, Multivalued.DEFAULT, Definition.Type.STRING, null, null, null, null, null);
        }
        else if (attribute.name().equals("booleanObjectField")) {
          // @Attrbibute(description="description:booleanObjectField", required=false, caseExact=true, returned = Definition.Returned.REQUEST)
          checkAttribute(attribute, "description:booleanObjectField", Required.NOT_REQUIRED, CaseExact.CASE_EXACT, Multivalued.DEFAULT, Definition.Type.BOOLEAN, Definition.Returned.REQUEST, null, null, null, null);
        }
        else if (attribute.name().equals("booleanField")) {
          // @Attrbibute(description="description:booleanField", required=false, caseExact=false, returned = Definition.Returned.NEVER)
          checkAttribute(attribute, "description:booleanField", Required.NOT_REQUIRED, CaseExact.NOT_CASE_EXACT, Multivalued.DEFAULT, Definition.Type.BOOLEAN, Definition.Returned.NEVER, null, null, null, null);
        }
        else if (attribute.name().equals("integerObjectField")) {
          // @Attrbibute(description="description:integerObjectField", required=true, caseExact=false, returned = Definition.Returned.DEFAULT)
          checkAttribute(attribute, "description:integerObjectField", Required.REQUIRED, CaseExact.NOT_CASE_EXACT, Multivalued.DEFAULT, Definition.Type.INTEGER, Definition.Returned.DEFAULT, null, null, null, null);
        }
        else if (attribute.name().equals("integerField")) {
          // @Attrbibute(description="description:integerField", required=true, caseExact=true, returned = Definition.Returned.ALWAYS)
          checkAttribute(attribute, "description:integerField", Required.REQUIRED, CaseExact.CASE_EXACT, Multivalued.DEFAULT, Definition.Type.INTEGER, Definition.Returned.ALWAYS, null, null, null, null);
        }
        else if (attribute.name().equals("mutabilityReadWrite")) {
          // @Attrbibute(description="description:mutabilityReadWrite", mutability = Definition.Mutability.READ_WRITE)
          checkAttribute(attribute, "description:mutabilityReadWrite", Required.DEFAULT, CaseExact.DEFAULT, Multivalued.DEFAULT, Definition.Type.STRING, null, Definition.Mutability.READ_WRITE, null, null, null);
        }
        else if (attribute.name().equals("mutabilityReadOnly")) {
          // @Attrbibute(description="description:mutabilityReadOnly", mutability = Definition.Mutability.READ_ONLY)
          checkAttribute(attribute, "description:mutabilityReadOnly", Required.DEFAULT, CaseExact.DEFAULT, Multivalued.DEFAULT, Definition.Type.STRING, null, Definition.Mutability.READ_ONLY, null, null, null);
        }
        else if (attribute.name().equals("mutabilityWriteOnly")) {
          // @Attrbibute(description="description:mutabilityWriteOnly", mutability = Definition.Mutability.WRITE_ONLY)
          checkAttribute(attribute, "description:mutabilityWriteOnly", Required.DEFAULT, CaseExact.DEFAULT, Multivalued.DEFAULT, Definition.Type.STRING, null, Definition.Mutability.WRITE_ONLY, null, null, null);
        }
        else if (attribute.name().equals("mutabilityImmutable")) {
          // @Attrbibute(description="description:mutabilityImmutable", mutability = Definition.Mutability.IMMUTABLE)
          checkAttribute(attribute, "description:mutabilityImmutable", Required.DEFAULT, CaseExact.DEFAULT, Multivalued.DEFAULT, Definition.Type.STRING, null, Definition.Mutability.IMMUTABLE, null, null, null);
        }
        else if (attribute.name().equals("bigDecimal")) {
          //  @Attribute(description="description:bigDecimal", mutability = Definition.Mutability.READ_ONLY)
          checkAttribute(attribute, "description:bigDecimal", Required.DEFAULT, CaseExact.DEFAULT, Multivalued.DEFAULT, Definition.Type.DECIMAL, null, Definition.Mutability.READ_ONLY, null, null, null);
        }

        markAttributeFound(expectedAttributes, attribute);
      }
      checkAllAttributesFound(expectedAttributes);
      System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaDefinition));

      schemaDefinition = SchemaFactory.schema(TestObject3.class);
      System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaDefinition));
      expectedAttributes = CollectionUtility.list("complexObject", "multiValuedString", "multiValuedComplex", "multiValuedField_missingType");

      for (Definition attribute : schemaDefinition.attribute()) {
        if (attribute.name().equals("complexObject")) {
          tc3_checkComplexObject(attribute);
        }
        else if (attribute.name().equals("multiValuedString")) {
          tc3_checkMultiValuedString(attribute);
        }
        markAttributeFound(expectedAttributes, attribute);
      }
      checkAllAttributesFound(expectedAttributes);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void tc2_checkSchema(final SchemaResource schemaDefinition) {
    // @SchemaInfo(id="urn:id:TestObject1", description="description:TestObject1", name = "name:TestObject1")
    assert "urn:id:TestObject1".equals(schemaDefinition.id());
    assert "description:TestObject1".equals(schemaDefinition.description());
    assert "name:TestObject1".equals(schemaDefinition.name());
  }

  private static void tc3_checkComplexObject(Definition attribute) {
    checkAttribute(attribute, "description:complexObject", Required.DEFAULT, CaseExact.DEFAULT, Multivalued.DEFAULT, Definition.Type.COMPLEX, null, null, null, null, null);

    Collection<Definition> subAttributes = attribute.attributes();
    assert (subAttributes != null);
    assert (subAttributes.size() > 0);
    List<String> expectedAttributes = CollectionUtility.list("stringField_3a");

    for (Definition subAttribute : subAttributes) {
      if (subAttribute.name().equals("stringField_3a")) {
        // @Attribute(description="description:stringField_3a")
        checkAttribute(subAttribute, "description:stringField_3a", Required.DEFAULT, CaseExact.DEFAULT, Multivalued.DEFAULT, Definition.Type.STRING, null, null, null, null, null);
      }
      markAttributeFound(expectedAttributes, subAttribute);
    }
    checkAllAttributesFound(expectedAttributes);
  }

  private static void tc3_checkMultiValuedString(Definition attribute) {
    checkAttribute(attribute, "description:complexObject", Required.DEFAULT, CaseExact.DEFAULT, Multivalued.DEFAULT, Definition.Type.COMPLEX, null, null, null, null, null);

    Collection<Definition> subAttributes = attribute.attributes();
    assert (subAttributes != null);
    assert (subAttributes.size() > 0);
    List<String> expectedAttributes = CollectionUtility.list("stringField_3a");

    for (Definition subAttribute : subAttributes) {
      if (subAttribute.name().equals("stringField_3a")) {
        // @Attribute(description="description:stringField_3a")
        checkAttribute(subAttribute, "description:stringField_3a", Required.DEFAULT, CaseExact.DEFAULT, Multivalued.DEFAULT, Definition.Type.STRING, null, null, null, null, null);
      }
      markAttributeFound(expectedAttributes, subAttribute);
    }
    checkAllAttributesFound(expectedAttributes);
  }

  private static void checkAttribute(Definition attribute, String description, Required required, CaseExact caseExact, Multivalued multivalued, Definition.Type type, Definition.Returned returned, Definition.Mutability mutability, Definition.Uniqueness uniqueness, Set canonicalValues, Set referenceType) {
    if (mutability == null) {
      mutability = Definition.Mutability.READ_WRITE;
    }

    if (returned == null) {
      returned = Definition.Returned.DEFAULT;
    }

    if (uniqueness == null) {
      uniqueness = Definition.Uniqueness.NONE;
    }

    assert description.equals(attribute.description());
    assert mutability.equals(attribute.mutability());
    assert returned.equals(attribute.returned());
    assert uniqueness.equals(attribute.uniqueness());

    switch (multivalued) {
      case MULTIVALUED     : assert attribute.multiValued();
                             break;
      case NOT_MULTIVALUED :
      default              : assert !attribute.multiValued();
                             break;
    }
    switch (required) {
      case REQUIRED        : assert attribute.required();
                             break;
      case NOT_REQUIRED    :
      default              : assert !attribute.required();
                             break;
    }
    switch (caseExact) {
      case NOT_CASE_EXACT  : assert attribute.caseExact();
                             break;
      case CASE_EXACT      :
      default              : assert !attribute.caseExact();
                             break;
    }

    assert canonicalValues.equals(attribute.canonical());
    assert referenceType.equals(attribute.reference());
    assert type.equals(attribute.type());
  }

  private static void checkAllAttributesFound(List<String> expectedAttributes) {
    assert expectedAttributes.size() > 0 : "Did not find all attributes : " + expectedAttributes;
  }

  private static void markAttributeFound(List<String> expectedAttributes, Definition attribute) {
    if (expectedAttributes.contains(attribute.name())) {
      expectedAttributes.remove(attribute.name());
    }
    else {
      assert  1==1 : "Found an unexpected attribute : " + attribute.name();
    }
  }

  private static void addSubAttributeFields(List<String> expectedAttributes) {
    expectedAttributes.addAll(Arrays.asList("type", "primary", "display", "value", "$ref"));
  }

  private static SchemaResource schemaResource()
    throws IOException {
    String schemaJson = "{\n" +
        "  \"schemas\": [\n" +
        "    \"urn:ietf:params:scim:schemas:core:2.0:Schema\"\n" +
        "  ],\n" +
        "  \"meta\": {\n" +
        "    \"resourceType\": \"Schema\",\n" +
        "    \"location\": \"https://example.com/scim/v2/Schemas/" +
        "urn:orclidentity:schemas:exampleSchema\"\n" +
        "  },\n" +
        "  \"id\": \"urn:orclidentity:schemas:exampleSchema\",\n" +
        "  \"name\": \"Example\",\n" +
        "  \"description\": \"Example schema\",\n" +
        "  \"attributes\": [\n" +
        "    {\n" +
        "      \"name\": \"string\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A String attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },    \n" +
        "    {\n" +
        "      \"name\": \"boolean\",\n" +
        "      \"type\": \"boolean\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A Boolean attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"decimal\",\n" +
        "      \"type\": \"decimal\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A Decimal attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"integer\",\n" +
        "      \"type\": \"integer\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An Integer attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"dateTime\",\n" +
        "      \"type\": \"dateTime\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A DateTime attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"binary\",\n" +
        "      \"type\": \"binary\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A Binary attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"reference\",\n" +
        "      \"type\": \"reference\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A Reference attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },    \n" +
        "    {\n" +
        "      \"name\": \"readOnlyAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A read-only attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readOnly\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"writeOnlyAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A write-only attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"writeOnly\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"readWriteAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A ReadWrite attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"immutableAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An Immutable attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"immutable\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"returnedAlwaysAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An attribute with returned=always.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"always\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"returnedNeverAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An attribute with returned=never.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"never\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"returnedDefaultAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An attribute with returned=default.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"returnedRequestAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An attribute with returned=request.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"request\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"uniquenessNoneAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An attribute with uniqueness=none.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"uniquenessServerAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An attribute with uniqueness=server.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"server\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"uniquenessGlobalAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An attribute with uniqueness=global.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"global\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"complex\",\n" +
        "      \"type\": \"complex\",\n" +
        "      \"subAttributes\": [\n" +
        "        {\n" +
        "          \"name\": \"subAttr1\",\n" +
        "          \"type\": \"string\",\n" +
        "          \"multiValued\": false,\n" +
        "          \"description\": \"A sub-attribute.\",\n" +
        "          \"required\": false,\n" +
        "          \"caseExact\": false,\n" +
        "          \"mutability\": \"readWrite\",\n" +
        "          \"returned\": \"default\",\n" +
        "          \"uniqueness\": \"none\"\n" +
        "        },\n" +
        "        {\n" +
        "          \"name\": \"subAttr2\",\n" +
        "          \"type\": \"string\",\n" +
        "          \"multiValued\": false,\n" +
        "          \"description\": \"Another sub-attribute.\",\n" +
        "          \"required\": false,\n" +
        "          \"caseExact\": false,\n" +
        "          \"mutability\": \"readWrite\",\n" +
        "          \"returned\": \"default\",\n" +
        "          \"uniqueness\": \"none\"\n" +
        "        }\n" +
        "      ],\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A complex single-valued attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"complexMultiValued\",\n" +
        "      \"type\": \"complex\",\n" +
        "      \"subAttributes\": [\n" +
        "        {\n" +
        "          \"name\": \"subAttr1\",\n" +
        "          \"type\": \"string\",\n" +
        "          \"multiValued\": false,\n" +
        "          \"description\": \"A sub-attribute.\",\n" +
        "          \"required\": false,\n" +
        "          \"caseExact\": false,\n" +
        "          \"mutability\": \"readWrite\",\n" +
        "          \"returned\": \"default\",\n" +
        "          \"uniqueness\": \"none\"\n" +
        "        },\n" +
        "        {\n" +
        "          \"name\": \"type\",\n" +
        "          \"type\": \"string\",\n" +
        "          \"multiValued\": false,\n" +
        "          \"description\": \"A sub-attribute with " +
        "canonical values.\",\n" +
        "          \"required\": false,\n" +
        "          \"canonicalValues\": [\n" +
        "            \"one\",\n" +
        "            \"two\",\n" +
        "            \"three\"\n" +
        "          ],\n" +
        "          \"caseExact\": false,\n" +
        "          \"mutability\": \"readWrite\",\n" +
        "          \"returned\": \"default\",\n" +
        "          \"uniqueness\": \"none\"\n" +
        "        }\n" +
        "      ],\n" +
        "      \"multiValued\": true,\n" +
        "      \"description\": \"A complex multi-valued attribute.\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    }\n" +
/*
        "    },\n" +
        "    {\n" +
        "      \"name\": \"wrongTypeCaseAttr\",\n" +
        "      \"type\": \"datetime\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"A DateTime attribute with type=datetime (instead of 'dateTime').\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readWrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"wrongMutabilityCaseAttr\",\n" +
        "      \"type\": \"string\",\n" +
        "      \"multiValued\": false,\n" +
        "      \"description\": \"An attribute with mutability=readwrite (instead of 'readWrite').\",\n" +
        "      \"required\": false,\n" +
        "      \"caseExact\": false,\n" +
        "      \"mutability\": \"readwrite\",\n" +
        "      \"returned\": \"default\",\n" +
        "      \"uniqueness\": \"none\"\n" +
        "    }\n" +
*/
        "  ]\n" +
        "}";
    return Support.objectReader().forType(SchemaResource.class).readValue(schemaJson);
  }
}