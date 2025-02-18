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

    File        :   AttributeDefinition.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeDefinition.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.LinkedList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.iam.platform.scim.annotation.Attribute;

import oracle.iam.platform.scim.schema.Address;

import static oracle.iam.platform.scim.annotation.Attribute.Type;
import static oracle.iam.platform.scim.annotation.Attribute.Returned;
import static oracle.iam.platform.scim.annotation.Attribute.Mutability;
import static oracle.iam.platform.scim.annotation.Attribute.Uniqueness;

////////////////////////////////////////////////////////////////////////////////
// class AttributeDefinition
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Represents SCIM attribute characteristics which are worth to model using
 ** Java enums: mutability, returned, type and uniqueness.
 ** <p>
 ** See section 2.2 of RFC 7643.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AttributeDefinition {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the attribute's name */
  @Attribute(description="The attribute's name.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final String                          name;

  /** the attribute's data type constraint */
  @Attribute(description="The attribute's data type.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final  Type                           type;

  /** indicating whether or not this attribute is required to be present */
  @Attribute(description="A Boolean value that specifies if the attribute is required.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final boolean                         required;

  /**
   ** indicating whether or not searches for this object will be case exact.
   ** If <code>true</code>, then this attribute will only be matched if the case
   ** of the value exactly matches the search string's case.
   */
  @Attribute(description="A Boolean value that specifies if the String attribute is case sensitive.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final boolean                         caseExact;

  /** indicating whether or not this attribute can have multiple values */
  @Attribute(description="Boolean value indicating the attribute's plurality.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final boolean                         multiValued;

  /** the attribute's human readable description */
  @Attribute(description="The attribute's human readable description.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final String                          description;

  /** a set of canonical values that this attribute may contain */
  @Attribute(description="A collection of suggested canonical values that MAY be used.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=String.class)
  private final Collection<String>              canonical;

  /** indicate the SCIM resource types that may be referenced */
  @Attribute(description="A multi-valued array of JSON strings that indicate the SCIM resource types that may be referenced.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=String.class)
  private final Collection<String>              reference;

  /** the sub-attributes of this attribute */
  @Attribute(description = "When an attribute is of type \"complex\", \"subAttributes\" defines set of sub-attributes.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=AttributeDefinition.class)
  private final Collection<AttributeDefinition> attributes;

  /**
   ** specifies how the service provider enforces uniqueness of attribute
   ** values
   */
  @Attribute(description="A single keyword value that specifies how the service provider enforces uniqueness of attribute values.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final Uniqueness                      uniqueness;

  /**
   ** indicates when an  attribute and associated values are returned in
   ** response to a GET request or in response to a PUT, POST, or PATCH
   ** request
   */
  @Attribute(description="A single keyword that indicates when an attribute and associated values are returned in response to a GET request or in response to a PUT, POST, or PATCH request.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final Returned                        returned;

  /**
   ** indicates the circumstances under which the value of the attribute can be
   ** (re)defined
   */
  @Attribute(description="A single keyword indicating the circumstances under which the value of the attribute can be (re)defined.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final Mutability                      mutability;

  //////////////////////////////////////////////////////////////////////////////
  // class Factory
  // ~~~~~ ~~~~~~~
  /**
   ** Factory class to build SCIM Attribute Definitions.
   */
  public static class Factory {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the attribute's name */
    private String                          name;

    /** the attribute's data type */
    private Type                            type;

    /** indicating whether or not this attribute is required to be present */
    private boolean                         required;

    /**
     ** indicating whether or not searches for this object will be case exact.
     ** If <code>true</code>, then this attribute will only be matched if the
     ** case of the value exactly matches the search string's case.
     */
    private boolean                         caseExact;

    /** indicating whether or not this attribute can have multiple values */
    private boolean                         multiValued;

    /** attribute's human readable description */
    private String                          description;

    /** a set of canonical values that this attribute may contain. */
    private Collection<String>              canonical;

    /** indicate the SCIM resource types that may be referenced */
    private Collection<String>              referenceTypes;

    /** the sub-attributes of this attribute. */
    private Collection<AttributeDefinition> subAttributes;

    /**
     ** specifies how the service provider enforces uniqueness of attribute
     ** values
     */
    private Uniqueness                      uniqueness;

    /**
     ** indicates when an  attribute and associated values are returned in
     ** response to a GET request or in response to a PUT, POST, or PATCH
     ** request
     */
    private Returned                        returned;

    /**
     ** indicates the circumstances under which the value of the attribute can
     ** be (re)defined
     */
    private Mutability                      mutability;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Factory</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Factory() {
      // Defaults according to
      // https://tools.ietf.org/html/draft-ietf-scim-core-schema-20#section-2.2
      this.type       = Type.STRING;
      this.caseExact  = false;
      this.mutability = Mutability.READ_WRITE;
      this.returned   = Returned.DEFAULT;
      this.uniqueness = Uniqueness.NONE;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the attribute name.
     **
     ** @param  value            the attribute name.
     **                          <br>
     **                          Allowed object is {@link String}}.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory name(final String value) {
      this.name = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Sets the type constraint of the attribute.
     **
     ** @param  value            the type constraint of the attribute.
     **                          <br>
     **                          Allowed object is {@link Type}.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory type(final Type value) {
      this.type = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: required
    /**
     ** Sets a boolean indicating if the attribute is required.
     **
     ** @param  value            a boolean indicating if the attribute is
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory required(final boolean value) {
      this.required = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: caseExact
    /**
     ** Sets a boolean indicating if the value of the attribute should be
     ** treated as case sensitive.
     ** <br>
     ** If <code>true</code>, the attribute's value should be treated as case
     ** sensitive.
     **
     ** @param  value            a boolean indicating if the value of the
     **                          attribute should be treated as case sensitive.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory caseExact(final boolean value) {
      this.caseExact = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: multiValued
    /**
     ** Sets a boolean indicating if the attribute is multi-valued.
     **
     ** @param  value            a boolean indicating if the attribute is
     **                          multi-valued.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory multiValued(final boolean value) {
      this.multiValued = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Sets the description of the attribute.
     **
     ** @param  value            the description of the attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory description(final String value) {
      this.description = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: canonical
    /**
     ** Adds possible canonical values for this attribute.
     ** <br>
     ** This is only relevant for multi-valued attributes.
     **
     ** @param  value            the possible canonical values for this
     **                          attribute.
     **                          <br>
     **                          Allowed object is array of {@link String}.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory canonical(final String... value) {
      if(value != null && value.length > 0) {
        if (this.canonical == null) {
          this.canonical = new HashSet<String>();
        }
        this.canonical.addAll(Arrays.asList(value));
      }
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: reference
    /**
     ** Adds reference types for the attribute.
     **
     ** @param  type             the reference types for the attribute.
     **                          <br>
     **                          Allowed object is array of {@link String}.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory reference(final String... type) {
      if (type != null && type.length > 0) {
        if (this.referenceTypes == null) {
          this.referenceTypes = new HashSet<String>();
        }
        this.referenceTypes.addAll(Arrays.asList(type));
      }
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sub
    /**
     ** Adds the sub-attributes of the attribute.
     **
     ** @param  value            the sub-attributes of the attribute.
     **                          <br>
     **                          Allowed object is array of
     **                          {@link AttributeDefinition}.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory sub(final AttributeDefinition... value) {
      if (value != null && value.length > 0) {
        if (this.subAttributes == null) {
          this.subAttributes = new LinkedList<AttributeDefinition>();
        }
        this.subAttributes.addAll(Arrays.asList(value));
      }
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: returned
    /**
     ** Sets the return constraint for the attribute.
     **
     ** @param  value            the return constraint for the attribute.
     **                          <br>
     **                          Allowed object is {@link Returned}.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory returned(final Returned value) {
      this.returned = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mutability
    /**
     ** Sets the mutablity constraint for the attribute.
     **
     ** @param  value            the mutablity constraint for the attribute.
     **                          <br>
     **                          Allowed object is {@link Mutability}.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory mutability(final Mutability value) {
      this.mutability = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: uniqueness
    /**
     **  Sets the uniqueness constraint of the attribute.
     **
     ** @param  value            the uniqueness constraint of the attribute.
     **                          <br>
     **                          Allowed object is {@link Uniqueness}.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory uniqueness(final Uniqueness value) {
      this.uniqueness = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: clear
    /**
     ** Clears all values in this factory, so that it could be used again.
     **
     ** @return                  the <code>Factory</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Factory</code>.
     */
    public Factory clear() {
      this.name           = null;
      this.type           = Type.STRING;
      this.subAttributes  = null;
      this.multiValued    = false;
      this.description    = null;
      this.required       = false;
      this.canonical      = null;
      this.caseExact      = false;
      this.mutability     = Mutability.READ_WRITE;
      this.returned       = Returned.DEFAULT;
      this.uniqueness     = Uniqueness.NONE;
      this.referenceTypes = null;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a <code>AttributeDefinition</code> from the
     ** configured properties.
     **
     ** @return                  an newly created instance of
     **                          <code>AttributeDefinition</code> providing.
     **                          <br>
     **                          Possible object is
     **                          <code>AttributeDefinition</code>.
     */
    public AttributeDefinition build() {
      return new AttributeDefinition(this.name, this.description, this.type, this.subAttributes, this.multiValued, this.required, this.caseExact, this.returned, this.mutability, this.uniqueness, this.canonical, this.referenceTypes);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeDefinition</code> with the specified
   ** properties.
   **
   ** @param  name               the attribute's name.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  description        the attribute's human readable description.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  type               the attribute's data type constraint.
   **                            <br>
   **                            Allowed object {@link Type}.
   ** @param  attributes         the sub-attributes of this attribute.
   **                            <br>
   **                            Allowed object array of
   **                            {@link AttributeDefinition}s.
   ** @param  multiValued        indicating whether or not this attribute can
   **                            have multiple values.
   **                            <br>
   **                            Allowed object <code>boolean</code>.
   ** @param  required           indicating whether or not this attribute is
   **                            required to be present.
   **                            <br>
   **                            Allowed object {@link Boolean}.
   ** @param  caseExact          indicating whether or not searches for this
   **                            object will be case exact. If
   **                            <code>true</code>, then this attribute will
   **                            only be matched if the case of the value
   **                            exactly matches the search string's case.
   **                            <br>
   **                            Allowed object {@link Boolean}.
   ** @param  returned           indicates when an  attribute and associated
   **                            values are returned in response to a GET
   **                            request or in response to a PUT, POST, or PATCH
   **                            request.
   **                            <br>
   **                            Allowed object {@link Returned}.
   ** @param  mutability         indicates the circumstances under which the
   **                            value of the attribute can be (re)defined.
   **                            <br>
   **                            Allowed object {@link Mutability}.
   ** @param  uniqueness         specifies how the service provider enforces
   **                            uniqueness of attribute values.
   **                            <br>
   **                            Allowed object {@link Uniqueness}.
   ** @param  canonical          a set of canonical values that this attribute
   **                            may contain.
   **                            <br>
   **                            Allowed object array of {@link String}s.
   ** @param  reference          indicate the SCIM resource types that may be
   **                            referenced.
   **                            <br>
   **                            Allowed object array of {@link String}s.
   */
  @JsonCreator
  AttributeDefinition(@JsonProperty(value="name", required=true) final String name, @JsonProperty(value="description") final String description, @JsonProperty(value="type", required=true) final Type type, @JsonProperty(value="subAttributes") final Collection<AttributeDefinition> attributes, @JsonProperty(value="multiValued") final Boolean multiValued, @JsonProperty(value="required", required=true) final Boolean required, @JsonProperty(value="caseExact") final Boolean caseExact, @JsonProperty(value="returned", required=true) final Returned returned, @JsonProperty(value="mutability") final Mutability mutability, @JsonProperty(value="uniqueness") final Uniqueness uniqueness, @JsonProperty(value="canonicalValues") final Collection<String> canonical, @JsonProperty(value="referenceTypes") final Collection<String> reference) {
    // ensure inheritance
    super();

    // following RFC 7643 some of the values are fallback to defualt
    // If not otherwise stated in RFC 7643 Section 7, SCIM attributes have the
    // following characteristics:
    // o  "required" is "false" (i.e., not REQUIRED)
    // o  "canonicalValues": none assigned (for example, the "type"
    //                       sub-attribute as described in Section 2.4)
    // o  "caseExact" is "false" (i.e., case-insensitive)
    // o  "mutability" is "readWrite" (i.e., modifiable)
    // o  "returned" is "default" (the attribute value is returned by default)
    // o  "uniqueness" is "none" (has no uniqueness enforced)
    // o  "type" is "string" (Section 2.3.1).
    this.name        = name;
    this.description = description;
    this.type        = type        == null ? Type.STRING           : type;
    this.attributes  = attributes  == null ? null                  : CollectionUtility.unmodifiableList(attributes);
    this.multiValued = multiValued == null ? false                 : multiValued.booleanValue();
    this.required    = required    == null ? false                 : required.booleanValue();
    this.caseExact   = caseExact   == null ? false                 : caseExact.booleanValue();;
    this.returned    = returned    == null ? Returned.DEFAULT      : returned;
    this.mutability  = mutability  == null ? Mutability.READ_WRITE : mutability;
    this.uniqueness  = uniqueness  == null ? Uniqueness.NONE       : uniqueness;
    this.canonical   = canonical   == null ? null                  : CollectionUtility.unmodifiableList(canonical);
    this.reference   = reference   == null ? null                  : CollectionUtility.unmodifiableList(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the attribute.
   **
   ** @return                    the name of the attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @JsonGetter("name")
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type constraint of the value for this attribute.
   **
   ** @return                    the type constraint of the value for this
   **                            attribute.
   **                            <br>
   **                            Possible object is {@link Type}.
   */
  @JsonGetter("type")
  public Type type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns the subattributes of the attribute.
   **
   ** @return                    the subattributes of the attribute.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link AttributeDefinition}.
   */
  @JsonGetter("subAttributes")
  public Collection<AttributeDefinition> attributes() {
    return this.attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValued
  /**
   ** Determines if the attribute allows multiple values.
   **
   * @return                     indicating whether or not this attribute can
   **                            have multiple values.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @JsonGetter("multiValued")
  public boolean multiValued() {
    return this.multiValued;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the attribute.
   **
   ** @return                    the description of the attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @JsonGetter("description")
  public String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Determines if the attribute is required.
   **
   ** @return                    indicating whether or not this attribute is
   **                            required to be present.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @JsonGetter("required")
  public boolean required() {
    return this.required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canonical
  /**
   ** Returns the canonical values of the attribute.
   **
   ** @return                    a set of canonical values that this attribute
   **                            may contain.
   **                            <br>
   **                            Possible object is array of {@link String}s.
   */
  @JsonGetter("canonicalValues")
  public Collection<String> canonical() {
    return this.canonical;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   caseExact
  /**
   ** Determines if the attribute value case is sensitive.
   **
   ** @return                    indicating whether or not searches for this
   **                            object will be case exact. If
   **                            <code>true</code>, then this attribute will
   **                            only be matched if the case of the value
   **                            exactly matches the search string's case.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @JsonGetter("caseExact")
  public boolean caseExact() {
    return this.caseExact;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutability
  /**
   ** Returns the mutability constraint for this attribute.
   **
   ** @return                    indicates the circumstances under which the
   **                            value of the attribute can be (re)defined.
   **                            <br>
   **                            Possible object is
   **                            {@link Mutability}.
   */
  @JsonGetter("mutability")
  public Mutability mutability() {
    return this.mutability;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returned
  /**
   ** Returns the return constraint for this attribute.
   **
   ** @return                    indicates when an  attribute and associated
   **                            values are returned in response to a GET
   **                            request or in response to a PUT, POST, or PATCH
   **                            request.
   **                            <br>
   **                            Possible object is {@link Returned}.
   */
  @JsonGetter("returned")
  public Returned returned() {
    return this.returned;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueness
  /**
   ** Returns the uniqueness constraint fo this attribute.
   **
   ** @return                    specifies how the service provider enforces
   **                            uniqueness of attribute values.
   **                            <br>
   **                            Possible object is
   **                            {@link Uniqueness}.
   */
  @JsonGetter("uniqueness")
  public Uniqueness uniqueness() {
    return this.uniqueness;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** Returns the reference types for this attribute.
   **
   ** @return                    indicate the SCIM resource types that may be
   **                            referenced.
   **                            <br>
   **                            Possible object is array of {@link String}s.
   */
  @JsonGetter("referenceTypes")
  public Collection<String> reference() {
    return this.reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   factory
  /**
   ** Factory method to create a SCIM <code>AttributeDefinition</code>
   ** {@link Factory}.
   **
   ** @return                    an instance of a SCIM
   **                            <code>AttributeDefinition</code>
   **                            {@link Factory}.
   **                            <br>
   **                            Possible object is {@link Factory}.
   */
  public static Factory factory() {
    return new Factory();
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
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
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
    return Objects.hash(
      this.name
    , this.type
    , this.attributes
    , this.multiValued
    , this.description
    , this.required
    , this.canonical
    , this.caseExact
    , this.mutability
    , this.returned
    , this.uniqueness
    , this.reference
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>AttributeDefinition</code>s are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>AttributeDefinition</code>s may be different even though they
   ** contain the same set of names with the same values, but in a different
   ** order.
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

    final AttributeDefinition that = (AttributeDefinition)other;
    return Objects.equals(this.name,        that.name)
        && Objects.equals(this.type,        that.type)
        && Objects.equals(this.required,    that.required)
        && Objects.equals(this.returned,    that.returned)
        && Objects.equals(this.canonical,   that.canonical)
        && Objects.equals(this.reference,   that.reference)
        && Objects.equals(this.caseExact,   that.caseExact)
        && Objects.equals(this.mutability,  that.mutability)
        && Objects.equals(this.uniqueness,  that.uniqueness)
        && Objects.equals(this.attributes,  that.attributes)
        && Objects.equals(this.multiValued, that.multiValued)
        && Objects.equals(this.description, that.description)
     ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character " " (space).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                   the string representation of this instance.
   */
  @Override
  public String toString() {
    return indented("");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indented
  /**
   ** Called by toString.
   ** <br>
   ** This is used to format the output of the object a little to improve
   ** readability.
   **
   ** @param  indent             the string to use for each indent increment.
   **                            For example, one might use "  " for a 2 space
   **                            indent.
   **
   ** @return                    a string representation of this attribute.
   */
  private String indented(final String indent) {
    StringBuilder builder = new StringBuilder();
    builder.append(indent);
    builder.append("Name: ").append(name());
    builder.append(" Description: ").append(description());
    builder.append(" readOnly: ");
    builder.append(" required: ").append(required());
    builder.append(" caseExact: ").append(caseExact());
    builder.append(System.lineSeparator());
    if (attributes() != null) {
      for (AttributeDefinition a : attributes()) {
        builder.append(a.indented(indent + "  "));
      }
    }
    return builder.toString();
  }
}