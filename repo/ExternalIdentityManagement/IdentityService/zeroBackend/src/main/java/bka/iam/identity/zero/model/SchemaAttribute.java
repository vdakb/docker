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
    Subsystem   :   ZeRo Backend

    File        :   OIMEntitlement.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    OIMEntitlement.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.zero.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// final class OIMEntitlement
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Stores OIM Entitlement object.
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class SchemaAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String RESOURCE_NAME = "SchemaAttribute";
  
  // Names of the JSON attributes
  public static final String ATTR_NAME          = "name";
  public static final String ATTR_LABEL         = "label";
  public static final String ATTR_REQUIRED      = "required";
  public static final String ATTR_ENTITLEMENT   = "entitlement";
  public static final String ATTR_TYPE          = "type";
  public static final String ATTR_VARIANT_TYPE  = "variantType";
  public static final String ATTR_LENGTH        = "length";
  public static final String ATTR_ATTR_REF      = "attributeReference";
  public static final String ATTR_PROPERTIES    = "properties";
  public static final String ATTR_LOOKUP_NAME   = "lookupName";
  public static final String ATTR_LOOKUP_VALUES = "lookupValues";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(ATTR_NAME)
  private String name;

  @JsonProperty(ATTR_LABEL)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String label;

  @JsonProperty(ATTR_REQUIRED)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean required;

  @JsonProperty(ATTR_ENTITLEMENT)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean entitlement;

  @JsonProperty(ATTR_TYPE)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String type;

  @JsonProperty(ATTR_VARIANT_TYPE)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String variantType;

  @JsonProperty(ATTR_LENGTH)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long length;

  @JsonProperty(ATTR_ATTR_REF)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<SchemaAttribute> attrRef;

  @JsonProperty(ATTR_PROPERTIES)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Object> properties;
  
  @JsonProperty(ATTR_LOOKUP_NAME)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String lookupName;

  @JsonProperty(ATTR_LOOKUP_VALUES)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<Map<String, String>> lookupValues;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>SchemaAttribute</code> object that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SchemaAttribute() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of an attribute.
   **
   ** @param  value              the name of an attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of an attribute.
   **
   ** @return                    the name of an attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Sets the label of an attribute.
   **
   ** @param  value              the label of an attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute label(final String value) {
    this.label = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Returns the label of an attribute.
   **
   ** @return                    the label of an attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String label() {
    return this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Sets the 'required' flag of an attribute.
   **
   ** @param  value              the 'required' flag of an attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute required(final Boolean value) {
    this.required = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Returns the 'required' flag of an attribute.
   **
   ** @return                    the 'required' flag of an attribute.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean required() {
    return this.required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Sets the 'entitlement' flag of an attribute ("true" if this attribute
   ** is an entitlement).
   **
   ** @param  value              the 'entitlement' flag of an attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute entitlement(final Boolean value) {
    this.entitlement = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Returns  the 'entitlement' flag of an attribute ("true" if this
   ** attribute is an entitlement).
   **
   ** @return                    the 'entitlement' flag of an attribute.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean entitlement() {
    return this.entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type of an attribute.
   **
   ** @param  value              the type of an attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of an attribute.
   **
   ** @return                    the type of an attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String type() {
    return this.type;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   variantType
  /**
   ** Sets the variant type of an attribute.
   **
   ** @param  value              the variant type of an attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute variantType(final String value) {
    this.variantType = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   variantType
  /**
   ** Returns the variant type of an attribute.
   **
   ** @return                    the variant type of an attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String variantType() {
    return this.variantType;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   length
  /**
   ** Sets the length flag of an attribute.
   **
   ** @param  value              the length of an attribute.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute length(final Long value) {
    this.length = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   length
  /**
   ** Returns the length of an attribute.
   **
   ** @return                    the length of an attribute.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Long length() {
    return this.length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attrRef
  /**
   ** Sets the type of an attribute.
   **
   ** @param  value              the contained attribute reference within an attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute attrRef(final List<SchemaAttribute> value) {
    this.attrRef = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attrRef
  /**
   ** Returns the type of an attribute.
   **
   ** @return                    the type of an attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public List<SchemaAttribute> attrRef() {
    return this.attrRef;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Sets the properties of an attribute.
   **
   ** @param  value              the properties of an attribute.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Map}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute properties(final Map<String, Object> value) {
    this.properties = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Returns the properties of an attribute.
   **
   ** @return                    the properties of an attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public Map<String, Object> properties() {
    return this.properties;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupName
  /**
   ** Sets the lookupName of an attribute.
   **
   ** @param  value              the lookupName of an attribute.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Map}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute lookupName(final String value) {
    this.lookupName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupName
  /**
   ** Returns the lookup values of an attribute.
   **
   ** @return                    the lookup values of an attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String lookupName() {
    return this.lookupName;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupValues
  /**
   ** Sets the lookupValues of an attribute.
   **
   ** @param  value              the lookup values of an attribute.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Map}.
   **
   ** @return                    the <code>SchemaAttribute</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>SchemaAttribute</code>.
   */
  public SchemaAttribute lookupValues(final List<Map<String, String>> value) {
    this.lookupValues = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupValues
  /**
   ** Returns the lookup values of an attribute.
   **
   ** @return                    the lookup values of an attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public List<Map<String, String>> lookupValues() {
    return this.lookupValues;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this object.
   **
   ** @return                    a string representation of this object.
   **                            <br>
   **                            Possible object is <code>String</code>.
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer("SchemaAttribute object:");
    result.append("\" name=\"").append(this.name);
    result.append("\" label=\"").append(this.label);
    result.append("\" required=\"").append(this.required);
    result.append("\" entitlement=\"").append(this.entitlement);
    result.append("\" type=\"").append(this.type);
    result.append("\" variantType=\"").append(this.variantType);
    result.append("\" length=\"").append(this.length);
    result.append("\" attrRef=\"").append(this.attrRef);
    result.append("\" properties=\"").append(this.properties);
    result.append("\" lookupName=\"").append(this.lookupName);
    result.append("\" lookupValues=\"").append(this.lookupValues);
    result.append("\"");
    return result.toString();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.name,
                        this.label,
                        this.type,
                        this.variantType,
                        this.required,
                        this.length,
                        this.attrRef,
                        this.properties,
                        this.lookupName,
                        this.lookupValues);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Entitlement</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Entitlement</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final SchemaAttribute that = (SchemaAttribute)other;
    return Objects.equals(this.name,         that.name)
        && Objects.equals(this.label,        that.label)
        && Objects.equals(this.required,     that.required)
        && Objects.equals(this.type,         that.type)
        && Objects.equals(this.variantType,  that.variantType)
        && Objects.equals(this.length,       that.length)
        && Objects.equals(this.attrRef,      that.attrRef)
        && Objects.equals(this.properties,   that.properties)
        && Objects.equals(this.lookupName  , that.lookupName)
        && Objects.equals(this.lookupValues, that.lookupValues)
      ;
  }
}