/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   SchemaAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    SchemaAttribute.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;
////////////////////////////////////////////////////////////////////////////////
// class SchemaAttribute
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The SchemaAttribute class represents an attribute that can be defined
 ** within a SCIM schema. It holds various properties that describe the 
 ** characteristics of an attribute, such as its name, type, mutability, 
 ** and uniqueness. This class also supports the concept of sub-attributes,
 ** allowing attributes to have nested attributes, making it suitable for
 ** complex attribute structures in SCIM resources.
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SchemaAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // The name of the attribute (e.g., "userName", "email").
  private String name;

  // The type of the attribute (e.g., "string", "boolean", "integer").
  private String type;

  // Whether the attribute can have multiple values (e.g., "true" or "false").
  private Boolean multiValued;

  // A description of the attribute's purpose or usage.
  private String description;

  // Defines whether the attribute is mutable or not (e.g., "readOnly", "readWrite").
  private String mutability;

  // Defines whether the attribute should be returned in responses (e.g., "always", "default").
  private String returned;

  // Specifies the uniqueness of the attribute (e.g., "none", "global", "server").
  private String uniqueness;

  // Specifies whether the attribute is required for the resource (e.g., "true", "false").
  private Boolean required;

  // Defines if the value of the attribute should be case-sensitive (e.g., "true", "false").
  private Boolean caseExact;
  
  // Defines allowed value for the attribute (e.g., "work", "home").
  private String[] canonicalValues;

  // An array of sub-attributes for complex attributes, if any.
  private SchemaAttribute[] subAttributes;

  public SchemaAttribute(final String name, final String type, final Boolean multiValued, final String description, final String mutability, final String returned, final String uniqueness, final Boolean required, final Boolean caseExact, final String[] canonicalValues, final SchemaAttribute[] subAttributes) {
    super();
    
    this.name = name;
    this.type =          type          != null ? type : "string";
    this.multiValued =   multiValued   != null ? multiValued : false;
    this.description =   description   != null ? description : "";
    this.mutability =    mutability    != null ? mutability : "readWrite";
    this.returned =      returned      != null ? returned : "default";
    this.uniqueness =    uniqueness    != null ? uniqueness : "global";
    this.required =      required      != null ? required : false;
    this.caseExact =     caseExact     != null ? caseExact : false;
    this.canonicalValues =     canonicalValues     != null ? canonicalValues : new String[0];
    this.subAttributes = subAttributes != null ? subAttributes : new SchemaAttribute[0];
  }
  
  public SchemaAttribute() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Getters and Setters
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the name of the attribute.
   ** 
   ** @param name The name of the attribute.
   **             Allowed object is {@link String}.
   */
  public void setName(String name) {
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the name of the attribute.
   ** 
   ** @return The name of the attribute.
   **         Possible object is {@link String}.
   */
  public String getName() {
    return name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Sets the type of the attribute (e.g., "string", "boolean").
   ** 
   ** @param type The type of the attribute.
   **             Allowed object is {@link String}.
   */
  public void setType(String type) {
    this.type = type != null ? type : "";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getType
  /**
   ** Returns the type of the attribute.
   ** 
   ** @return The type of the attribute.
   **         Possible object is {@link String}.
   */
  public String getType() {
    return type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMultiValued
  /**
   ** Sets whether the attribute can have multiple values.
   ** 
   ** @param value     Whether the attribute is multi-valued (true/false).
   **                  Allowed object is {@link String}.
   */
  public void setMultiValued(Boolean value) {
    this.multiValued = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMultiValued
  /**
   ** Returns whether the attribute can have multiple values.
   ** 
   ** @return Whether the attribute is multi-valued.
   **         Possible object is {@link String}.
   */
  public Boolean getMultiValued() {
    return multiValued;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the description of the attribute.
   ** 
   ** @param description The description of the attribute.
   **                    Allowed object is {@link String}.
   */
  public void setDescription(String description) {
    this.description = description != null ? description : "";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the description of the attribute.
   ** 
   ** @return The description of the attribute.
   **         Possible object is {@link String}.
   */
  public String getDescription() {
    return description != null ? description : "";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMutability
  /**
   ** Sets the mutability of the attribute (e.g., "readOnly", "readWrite").
   ** 
   ** @param mutability The mutability of the attribute.
   **                   Allowed object is {@link String}.
   */
  public void setMutability(String mutability) {
    this.mutability = mutability != null ? mutability : "readWrite" ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMutability
  /**
   ** Returns the mutability of the attribute.
   ** 
   ** @return The mutability of the attribute.
   **         Possible object is {@link String}.
   */
  public String getMutability() {
    return mutability;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setReturned
  /**
   ** Sets whether the attribute should be returned in responses (e.g., "always", "default").
   ** 
   ** @param returned The returned value of the attribute.
   **                 Allowed object is {@link String}.
   */
  public void setReturned(String returned) {
    this.returned = returned != null ? returned : "default";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getReturned
  /**
   ** Returns whether the attribute is returned in responses.
   ** 
   ** @return The returned value of the attribute.
   **         Possible object is {@link String}.
   */
  public String getReturned() {
    return returned;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUniqueness
  /**
   ** Sets the uniqueness of the attribute (e.g., "none", "global", "server").
   ** 
   ** @param uniqueness The uniqueness of the attribute.
   **                   Allowed object is {@link String}.
   */
  public void setUniqueness(String uniqueness) {
    this.uniqueness = uniqueness;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUniqueness
  /**
   ** Returns the uniqueness of the attribute.
   ** 
   ** @return The uniqueness of the attribute.
   **         Possible object is {@link String}.
   */
  public String getUniqueness() {
    return uniqueness;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRequired
  /**
   ** Sets whether the attribute is required (e.g., "true", "false").
   ** 
   ** @param value     Whether the attribute is required.
   **                  Allowed object is {@link String}.
   */
  public void setRequired(Boolean value) {
    this.required = value != null ? value : false ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRequired
  /**
   ** Returns whether the attribute is required.
   ** 
   ** @return Whether the attribute is required.
   **         Possible object is {@link String}.
   */
  public Boolean getRequired() {
    return required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCaseExact
  /**
   ** Sets whether the attribute is case-sensitive.
   ** 
   ** @param caseExact Whether the attribute is case-sensitive.
   **                  Allowed object is {@link String}.
   */
  public void setCaseExact(Boolean caseExact) {
    this.caseExact = caseExact != null ? caseExact : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCaseExact
  /**
   ** Returns whether the attribute is case-sensitive.
   ** 
   ** @return Whether the attribute is case-sensitive.
   **         Possible object is {@link String}.
   */
  public Boolean getCaseExact() {
    return caseExact;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCanonicalValues
  /**
   ** Sets the allowed value in the attribute (i.e Canonical Value).
   ** 
   ** @param canonicalValues An array of canonical values.
   **                        Allowed object is {@link String[]} array.
   */
  public void setCanonicalValues(String[] canonicalValues) {
    this.canonicalValues = canonicalValues != null ? canonicalValues : new String[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSubAttributes
  /**
   ** Returns the allowed value in the attribute (i.e Canonical Value).
   ** 
   ** @return An array of canonical values.
   **         Possible object is {@link String[]} array.
   */
  public String[] getCanonicalValues() {
    if (canonicalValues != null)
      return canonicalValues;
    return new String[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSubAttributes
  /**
   ** Sets the sub-attributes of the attribute, if any (used for complex attributes).
   ** 
   ** @param subAttributes An array of sub-attributes.
   **                      Allowed object is {@link SchemaAttribute[]} array.
   */
  public void setSubAttributes(SchemaAttribute[] subAttributes) {
    this.subAttributes = subAttributes != null ? subAttributes : new SchemaAttribute[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSubAttributes
  /**
   ** Returns the sub-attributes of the attribute.
   ** 
   ** @return An array of sub-attributes.
   **         Possible object is {@link SchemaAttribute[]} array.
   */
  public SchemaAttribute[] getSubAttributes() {
    if (subAttributes != null)
      return subAttributes;
    return new SchemaAttribute[0];
  }
  
  public SchemaAttribute getSubSchemaAttribute(final String key) {
    for (SchemaAttribute subSchemaAttribute : getSubAttributes()) {
      if (subSchemaAttribute.getName().equals(key))
        return subSchemaAttribute;
    }
    return null;
  }
  
  public Boolean isSubAttributeSchema(final String key) {
    
    if (this.getName().equals(key)) {
      return true;
    }
    
    for (SchemaAttribute schemaAttribute : getSubAttributes()) {
      if (schemaAttribute.isSubAttributeSchema(key)) {
        return true;
      }
    }
    return false;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:  toString
  /**
   ** Provides a JSON string representation of the shema attribute
   ** 
   ** @return        The JSON string representation of the attribute value.
   **                Possible object is {@link String}.
   */
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("\"" + "name"        + "\":\"" + getName()        + "\",");
    builder.append("\"" + "type"        + "\":\"" + getType()        + "\",");
    builder.append("\"" + "multiValued" + "\":" + getMultiValued() + ",");
    builder.append("\"" + "description" + "\":\"" + getDescription().replace("\"", "\\\"") + "\",");
    builder.append("\"" + "mutability"  + "\":\"" + getMutability()  + "\",");
    builder.append("\"" + "returned"    + "\":\"" + getReturned()    + "\",");
    builder.append("\"" + "uniqueness"  + "\":\"" + getUniqueness()  + "\",");
    builder.append("\"" + "required"    + "\":" + getRequired()    + ",");
    builder.append("\"" + "caseExact"   + "\":" + getCaseExact()   + "");
    
    final String[] canonicalValue = getCanonicalValues();
    if (canonicalValue != null && canonicalValue.length > 0) {
      builder.append(",\"" + "canonicalValues"   + "\":[");
      for (int i = 0; i < canonicalValue.length; i++) {
        builder.append("\"" + canonicalValue[i].toString() + "\"");
        if (i < canonicalValue.length - 1)
          builder.append(",");
      }
      builder.append("]");
    }
    
    final SchemaAttribute[] subAttribute = getSubAttributes();
    if (subAttribute != null && subAttribute.length > 0) {
      builder.append(",\"" + "subAttributes"   + "\":[");
      for (int i = 0; i < subAttribute.length; i++) {
        builder.append(subAttribute[i].toString());
        if (i < subAttribute.length - 1)
          builder.append(",");
      }
      builder.append("]");
    }
    builder.append("}");
    return builder.toString();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:  clone
  /**
   ** Creates a deep copy of this SchemaAttribute.
   ** 
   ** @return        A cloned instance of the schema attribute.
   **                Possible object is {@link SchemaAttribute}.
   */
  @Override
  public SchemaAttribute clone() {
      SchemaAttribute attribute = new SchemaAttribute();
      attribute.setName(this.getName());
      attribute.setType(this.getType());
      attribute.setMultiValued(this.getMultiValued());
      attribute.setDescription(this.getDescription());
      attribute.setMutability(this.getMutability());
      attribute.setReturned(this.getReturned());
      attribute.setUniqueness(this.getUniqueness());
      attribute.setRequired(this.getRequired());
      attribute.setCaseExact(this.getCaseExact());
      
      if (this.getCanonicalValues() != null) {
        String[] canonicalValuesClone = new String[this.getCanonicalValues().length - 1];
         for (int i = 0; i < this.getCanonicalValues().length; i++) {
           canonicalValuesClone[i] = new String(this.getCanonicalValues()[i]);
         }
         attribute.setCanonicalValues(canonicalValuesClone);
      }
      if (this.getSubAttributes() != null) {
          SchemaAttribute[] subAttributesClone = new SchemaAttribute[this.getSubAttributes().length];
          for (int i = 0; i < this.getSubAttributes().length; i++) {
              subAttributesClone[i] = this.getSubAttributes()[i].clone();
          }
          attribute.setSubAttributes(subAttributesClone);
      }
      
      return attribute;
  }
}
