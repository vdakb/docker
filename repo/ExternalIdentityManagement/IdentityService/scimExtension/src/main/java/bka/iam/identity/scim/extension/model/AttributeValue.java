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

    File        :   AttributeValue.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the AttributeValue class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/

package bka.iam.identity.scim.extension.model;

import java.net.URI;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;
////////////////////////////////////////////////////////////////////////////////
// class AttributeValue
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The AttributeValue class represents the value of an attribute in SCIM.
 ** It supports both simple values (e.g., String, Integer, Boolean) and complex
 ** attributes containing sub-attributes.
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AttributeValue {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The primary value of the attribute. */
  private Object value;

  /** Sub-attributes for complex attributes. */
  private Attribute[] subAttributes;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with a simple value.
   ** 
   ** @param value        The value of the attribute.
   **                     Allowed object is {@link Object}.
   */
  public AttributeValue(Object value) {
    this.value = value;
    this.subAttributes = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with sub-attributes for a complex attribute.
   ** 
   ** @param  subAttributes The value of the attribute.
   **                       Allowed object is {@link Attribute[]}.
   */
  public AttributeValue(Attribute[] subAttributes) {
    this.value = null;
    this.subAttributes = subAttributes.clone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  AttributeValue(Attribute subAttribute)
  /**
   ** Initializes an AttributeValue with a single sub-attribute.
   ** 
   ** @param subAttribute The single sub-attribute.
   **                     Allowed object is {@link Attribute}.
   */
  public AttributeValue(Attribute subAttribute) {
    this.value = null;
    this.subAttributes = new Attribute[] { subAttribute };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with a String value.
   ** 
   ** @param  value        The value of the attribute.
   **                      Allowed object is {@link String}.
   */
  public AttributeValue(String value) {
  this.value = value;
    // No sub-attributes for String value.
    this.subAttributes = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with a Boolean value.
   ** 
   ** @param  value        The value of the attribute.
   **                      Allowed object is {@link Boolean}.
   */
  public AttributeValue(Boolean value) {
    this.value = value;
    this.subAttributes = null;  // No sub-attributes for Boolean value.
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with a XMLGregorianCalendar (DateTime) value.
   ** 
   ** @param  value        The value of the attribute.
   **                      Allowed object is {@link XMLGregorianCalendar}.
   */
  public AttributeValue(XMLGregorianCalendar value) {
    this.value = value;
    this.subAttributes = null;  // No sub-attributes for DateTime value.
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with a URI (Reference) value.
   ** 
   ** @param  value        The value of the attribute.
   **                      Allowed object is {@link URI}.
   */
  public AttributeValue(URI value) {
    this.value = value;
    this.subAttributes = null;  // No sub-attributes for URI value.
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with a byte array (Binary) value.
   ** 
   ** @param  value        The value of the attribute.
   **                      Allowed object is {@link byte[]}.
   */
  public AttributeValue(byte[] value) {
    this.value = value;
    this.subAttributes = null;  // No sub-attributes for Binary value.
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with an Integer value.
   ** 
   ** @param  value        The value of the attribute.
   **                      Allowed object is {@link Integer}.
   */
  public AttributeValue(Integer value) {
    this.value = value;
    this.subAttributes = null;  // No sub-attributes for Integer value.
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with a Double value (Decimal).
   ** 
   ** @param  value        The value of the attribute.
   **                      Allowed object is {@link Double}.
   */
  public AttributeValue(Double value) {
    this.value = value;
    this.subAttributes = null;  // No sub-attributes for Double value.
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an AttributeValue with a Data value.
   ** 
   ** @param  value        The date value of the attribute.
   **                      Allowed object is {@link Date}.
   */
  public AttributeValue(Date value) {
    this.value = value;
    this.subAttributes = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  getValue
  /**
   ** Retrieves the value of the attribute.
   ** 
   ** @return        The value of the attribute.
   **                Possible object is {@link Object}.
   */
  public Object getValue() {
    return this.value;
  }
  
  public String getType() {
    if (isString()) {
      return "string";
    }
    else if (isBoolean()) {
      return "boolean";
    }
    else if (isDateTime()) {
      return "dateTime";
    }
    else if (isDecimal()) {
      return "decimal";
    }
    else if (isInteger()) {
      return "integer";
    }
    else if (isReference()) {
      return "reference";
    }
    return "complex";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  setSimpleValue
  /**
   ** Sets a simple value for the attribute.
   ** 
   ** @param value        The new value to set.
   **                     Allowed object is {@link Object}.
   ** 
   ** @return             The updated value of the attribute.
   **                     Possible object is {@link Object}.
   */
  public Object setSimpleValue(final Object value) {
    this.value = value;
    return this.value;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:  getValueAsString
  /**
   ** Returns the string representation of the value.
   ** If the value is null, it returns null; otherwise, it returns the string
   ** representation of the value.
   ** 
   ** @return        The string representation of the value, or null
   **                if the value is null.
   **                Possible object is {@link String}.
   */
  public String getValueAsString() {
    if (this.value == null)
      return null;
    return String.valueOf(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  getSubAttributes
  /**
   ** Retrieves the sub-attributes of the attribute.
   ** 
   ** @return        The sub-attributes.
   **                Possible object is {@link Attribute[]}.
   */
  public Attribute[] getSubAttributes() {
    return this.subAttributes;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:  getSubAttribute
  /**
   ** Retrieves the sub-attributes of the attribute.
   ** @param         subAttributeName name of the attribute
   ** @return        The sub-attribute match the subAttibute name
   **                Possible object is {@link Attribute[]}.
   */
  public Attribute getSubAttribute(String subAttributeName) {
    Attribute attribute = null;
    
    if (this.subAttributes == null || subAttributeName == null)
      return null;
    
    for(Attribute subAttribute: this.subAttributes){
      if(subAttribute.getName().equals(subAttributeName)){
        attribute = subAttribute;
        break;
      }
    }
    
    return attribute;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:  getSubAttributeStringValue
  /**
   ** Retrieves the sub-attribute string value
   ** @param subAttributeName Name of the attribute
   ** @param defaultValue     Default value in case the subAttributeName doesn't exist       
   ** @return                 The sub-attribute match the subAttibute name
   **                         Possible object is {@link Attribute[]}.
   */
  public String getSubAttributeStringValue(String subAttributeName, String defaultValue) {
    String attributeValue = null;
    Attribute attribute = null;
    
    if (this.subAttributes == null || subAttributeName == null)
      return null;
    
    for(Attribute subAttribute: this.subAttributes){
      if(subAttribute.getName().equals(subAttributeName)){
        attribute = subAttribute;
        break;
      }
    }
    
    if(attribute != null){
      attributeValue = attribute.getValue().getStringValue();
    }
    else{
      attributeValue = defaultValue;
    }
    
    
    return attributeValue;
  }
  
  public String getSubAttributeStringValue(String subAttributeName) {
    return getSubAttributeStringValue(subAttributeName,null);
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSimpleValue
  /**
   ** Returns the value as a String.
   ** 
   ** @return         The String value of the attribute.
   **                 Possible object is {@link String}.
   */
  public String getStringValue() {
    return (String) this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBooleanValue
  /**
   ** Returns the value as a Boolean.
   ** 
   ** @return         The Boolean value of the attribute.
   **                 Possible object is {@link Boolean}.
   */
  public Boolean getBooleanValue() {
    return (Boolean) this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIntegerValue
  /**
   ** Returns the value as an Integer.
   ** 
   ** @return         The Integer value of the attribute.
   **                 Possible object is {@link Integer}.
   */
  public Integer getIntegerValue() {
    return (Integer) this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBinaryValue
  /**
   ** Returns the value as a byte array (Binary).
   ** 
   ** @return         The byte array (Binary) value of the attribute.
   **                 Possible object is {@link byte[]}.
   */
  public byte[] getBinaryValue() {
    return (byte[]) this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSimpleValue
  /**
   ** Returns the value as a Double (Decimal).
   ** 
   ** @return         The Double value of the attribute.
   **                 Possible object is {@link Double}.
   */
  public Double getDecimalValue() {
    return (Double) this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDateTimeValue
  /**
   ** Returns the value as an XMLGregorianCalendar (DateTime).
   ** 
   ** @return         The DateTime value of the attribute.
   **                 Possible object is {@link XMLGregorianCalendar}.
   */
  public Date getDateTimeValue() {
    return (Date) this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getReferenceValue
  /**
   ** Returns the value as a URI (Reference).
   ** 
   ** @return         The URI value of the attribute.
   **                 Possible object is {@link URI}.
   */
  public URI getReferenceValue() {
    return (URI) this.value;
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Type Checking Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isString
  /**
   ** Checks if the value is of type String.
   ** 
   ** @return         True if the value is a String, otherwise false.
   **                 Possible object is {@link Boolean}.
   */
  public boolean isString() {
    return (value instanceof String);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isBoolean
  /**
   ** Checks if the value is of type Boolean.
   ** 
   ** @return         True if the value is a Boolean, otherwise false.
   **                 Possible object is {@link Boolean}.
   */
  public Boolean isBoolean() {
    return (value instanceof Boolean);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isInteger
  /**
   ** Checks if the value is of type Integer.
   ** 
   ** @return         True if the value is an Integer, otherwise false.
   **                 Possible object is {@link Boolean}.
   */
  public Boolean isInteger() {
    return (value instanceof Integer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isBinary
  /**
   ** Checks if the value is of type Binary (byte[]).
   ** 
   ** @return        True if the value is a byte array (Binary), otherwise false.
   **                Possible object is {@link Boolean}.
   */
  public Boolean isBinary() {
    return (value instanceof byte[]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isDecimal
  /**
   ** Checks if the value is of type Decimal (Double).
   ** 
   ** @return        True if the value is a Double (Decimal), otherwise false.
   **                Possible object is {@link Boolean}.
   */
  public Boolean isDecimal() {
    return (value instanceof Double);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isDateTime
  /**
   ** Checks if the value is of type DateTime (XMLGregorianCalendar).
   ** 
   ** @return        True if the value is an XMLGregorianCalendar (DateTime), otherwise false.
   **                Possible object is {@link Boolean}.
   */
  public Boolean isDateTime() {
    return (value instanceof Date);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  
  /**
   ** Checks if the value is of type DateTime (XMLGregorianCalendar).
   ** 
   ** @return        True if the value is an XMLGregorianCalendar (DateTime),
   **                false otherwise.
   **                Possible object is {@link Boolean}.
   */
  public Boolean isReference() {
    return (value instanceof URI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Utility Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  toString
  /**
   ** Provides a string representation of the attribute value, including
   ** sub-attributes if present.
   ** 
   ** @return        The string representation of the attribute value.
   **                Possible object is {@link String}.
   */
  @Override
  public String toString() {
    if (this.value != null) {
      return "\"" + this.value + "\"";
    } else if (this.subAttributes != null) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < this.subAttributes.length; i++) {
        sb.append(this.subAttributes[i]);
        if (i < this.subAttributes.length - 1) {
          sb.append(",");
        }
      }
      return sb.toString();
    }
    return "";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  clone
  /**
   ** Creates a deep copy of this AttributeValue.
   ** 
   ** @return        A cloned instance of the attribute value.
   **                Possible object is {@link AttributeValue}.
   */
  @Override
  public AttributeValue clone() {
    if (this.value != null) {
      return new AttributeValue(this.value);
    }
    return new AttributeValue(this.subAttributes);
  }
}
