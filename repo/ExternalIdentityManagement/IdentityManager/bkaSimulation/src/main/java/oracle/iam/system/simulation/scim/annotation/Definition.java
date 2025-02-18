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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Definition.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Definition.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.annotation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class Definition
// ~~~~~ ~~~~~~~~~~
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
public class Definition {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the attribute's name */
  @Attribute(description="The attribute's name.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final String                 name;

  /** the attribute's data type constraint */
  @Attribute(description="The attribute's data type.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final  Type                  type;

  /** indicating whether or not this attribute is required to be present */
  @Attribute(description="A Boolean value that specifies if the attribute is required.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final boolean                required;

  /**
   ** indicating whether or not searches for this object will be case exact.
   ** If <code>true</code>, then this attribute will only be matched if the case
   ** of the value exactly matches the search string's case.
   */
  @Attribute(description="A Boolean value that specifies if the String attribute is case sensitive.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final boolean                caseExact;

  /** indicating whether or not this attribute can have multiple values */
  @Attribute(description="Boolean value indicating the attribute's plurality.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final boolean                multiValued;

  /** the attribute's human readable description */
  @Attribute(description="The attribute's human readable description.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final String                 description;

  /** a set of canonical values that this attribute may contain */
  @Attribute(description="A collection of suggested canonical values that MAY be used.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=String.class)
  private final Collection<String>     canonical;

  /** indicate the SCIM resource types that may be referenced */
  @Attribute(description="A multi-valued array of JSON strings that indicate the SCIM resource types that may be referenced.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=String.class)
  private final Collection<String>     reference;

  /** the sub-attributes of this attribute */
  @Attribute(description = "When an attribute is of type \"complex\", \"subAttributes\" defines set of sub-attributes.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=Definition.class)
  private final Collection<Definition> attributes;

  /**
   ** specifies how the service provider enforces uniqueness of attribute
   ** values
   */
  @Attribute(description="A single keyword value that specifies how the service provider enforces uniqueness of attribute values.", required=false, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final Uniqueness             uniqueness;

  /**
   ** indicates when an  attribute and associated values are returned in
   ** response to a GET request or in response to a PUT, POST, or PATCH
   ** request
   */
  @Attribute(description="A single keyword that indicates when an attribute and associated values are returned in response to a GET request or in response to a PUT, POST, or PATCH request.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final Returned               returned;

  /**
   ** indicates the circumstances under which the value of the attribute can be
   ** (re)defined
   */
  @Attribute(description="A single keyword indicating the circumstances under which the value of the attribute can be (re)defined.", required=true, caseExact=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private final Mutability             mutability;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** The <code>Type</code> implements the base functionality for describing an
   ** attribute type constraint in a SCIM <code>Service Provider</code> schema.
   ** <br>
   ** The following data types are used:
   ** <ul>
   **   <li>string
   **   <li>boolean
   **   <li>decimal
   **   <li>integer
   **   <li>dateTime
   **   <li>binary
   **   <li>reference
   **   <li>complex
   ** </ul>
   ** A SCIM attribute may also be multi-valued.
   ** <br>
   ** <b>All</b> members of a multi-valued attribute must be of the same data
   ** type.
   */
  public enum Type {
      /** A JSON string. */
      STRING("string")
      /**
       ** A JSON boolean value.
       ** <br>
       ** May have either of the literal values true or false.
       */
    , BOOLEAN("boolean")
      /** A JSON floating-point number */
    , DECIMAL("decimal")
      /** A JSON integer number. */
    , INTEGER("integer")
      /**
       ** A JSON string representing a timestamp.
       ** <br>
       ** SCIM DateTime values are always encoded as an xsd:dateTime value, as
       ** specified by XML Schema, section 3.3.7.
       */
    , DATETIME("dateTime")
      /**
       ** A JSON string representing a binary value.
       ** <br>
       ** SCIM binary values are always base64-encoded.
       */
    , BINARY("binary")
      /**
       ** A JSON string representing a reference to another resource.
       ** <br>
       ** A SCIM reference is always a URI: This may be a URN, the URL of
       ** another SCIM resource, or simply another URL. URLs may be absolute or
       ** relative.
       */
    , REFERENCE("reference")
      /**
       ** A JSON object.
       ** <br>
       ** A SCIM complex attribute is a composition of sub-attributes; these
       ** sub-attributes may have any data type except for 'complex'.
       */
    , COMPLEX("complex")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Type</code> with a constraint value.
     **
     ** @param  value            the constraint name (used in SCIM schemas) of
     **                          the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Type(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @JsonValue
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper <code>Type</code> constraint from the
     ** given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Type</code> constraint.
     **                          <br>
     **                          Possible object is {@link Type}.
     */
    @JsonCreator
    public static Type fromValue(final String value) {
      for (Type cursor : Type.values()) {
        // some vendors prefere lowercase letters always (especially Oracle)
        if (cursor.value.equalsIgnoreCase(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Mutability
  // ~~~~ ~~~~~~~~~~
  /**
   ** The <code>Mutability</code> implements the base functionality for
   ** describing an attribute mutability in a SCIM <code>Service Provider</code>
   ** schema.
   */
  public enum Mutability {
      /** the attribute can be read, but not written */
      READ_ONLY("readOnly"),

    /** the attribute can be read, and written */
    READ_WRITE("readWrite"),

    /**
     ** the attribute can be read, and cannot be set after object creation
     ** <br>
     ** It can be set during object creation.
     */
    IMMUTABLE("immutable"),

    /**
     ** the attribute can only be written, and not read
     ** <br>
     ** This might be used for password hashes for example.
     */
    WRITE_ONLY("writeOnly")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Mutability</code> with a constraint name.
     **
     ** @param  value            the value of the mutability constraint (used in
     **                          SCIM schemas) of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Mutability(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the mutability constraint.
     **
     ** @return                  the value of the mutability constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @JsonValue
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper <code>Type</code> from the given
     ** string value.
     **
     ** @param  value            the string value the mutability constraint
     **                          should be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Mutability</code> constraint.
     **                          <br>
     **                          Possible object is {@link Mutability}.
     */
    @JsonCreator
    public static Mutability fromValue(final String value) {
      for (Mutability cursor : Mutability.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Returned
  // ~~~~ ~~~~~~~~
  /**
   ** The <code>Returned</code> implements the base functionality for describing
   ** an attribute return constraint in a SCIM <code>Service Provider</code>
   ** schema.
   */
  public enum Returned {
      /** indicates that the attribute is always returned */
      ALWAYS("always"),

    /** indicates thatthe attribute is never returned */
    NEVER("never"),

    /** indicates that the attribute is returned by default */
    DEFAULT("default"),

    /** indicates that the attribute is only returned if requested */
    REQUEST("request")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Returned</code> with a return constraint name.
     **
     ** @param  value            the value of the return constraint (used in
     **                          SCIM schemas) of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Returned(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the return constraint.
     **
     ** @return                  the value of the return constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @JsonValue
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper <code>Returned</code> from the given
     ** string value.
     **
     ** @param  value            the string value the return constraint should
     **                          be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Returned</code> constraint.
     **                          <br>
     **                          Possible object is {@link Returned}.
     */
    @JsonCreator
    public static Returned fromName(final String value) {
      for(Returned returned : Returned.values()) {
        if(returned.value.equalsIgnoreCase(value)) {
          return returned;
        }
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Uniqueness
  // ~~~~ ~~~~~~~~~~
  /**
   ** The <code>Uniqueness</code> implements the base functionality for
   ** describing an attribute uniqueness constraint in a SCIM
   ** <code>Service Provider</code> schema.
   */
  public enum Uniqueness {
      /** indicates that this attribute's value need not be unique */
      NONE("none"),

    /**
     ** indicates that this attribute's value must be unique for a given
     ** server
     */
    SERVER("server"),

    /** indicates that this attribute's value must be globally unique */
    GLOBAL("global")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Uniqueness</code> with a constraint value.
     **
     ** @param  value            the value of the uniqueness constraint (used in
     **                          SCIM schemas) of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Uniqueness(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the uniqueness constraint.
     **
     ** @return                  the value of the uniqueness constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @JsonValue
    public String value() {
      return value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper <code>Uniqueness</code> from the given
     ** string value.
     **
     ** @param  value            the string value the uniqueness constraint
     **                          should be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Uniqueness</code> constraint.
     **                          <br>
     **                          Possible object is {@link Uniqueness}.
     */
    @JsonCreator
    public static Uniqueness fromValue(final String value) {
      for(Uniqueness uniqueness : Uniqueness.values()) {
        if(uniqueness.value.equalsIgnoreCase(value)) {
          return uniqueness;
        }
      }
      throw new IllegalArgumentException(value);
    }
  }

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
    private String                 name;

    /** the attribute's data type */
    private Type                   type;

    /** indicating whether or not this attribute is required to be present */
    private boolean                required;

    /**
     ** indicating whether or not searches for this object will be case exact.
     ** If <code>true</code>, then this attribute will only be matched if the
     ** case of the value exactly matches the search string's case.
     */
    private boolean                caseExact;

    /** indicating whether or not this attribute can have multiple values */
    private boolean                multiValued;

    /** attribute's human readable description */
    private String                 description;

    /** a set of canonical values that this attribute may contain. */
    private Collection<String>     canonical;

    /** indicate the SCIM resource types that may be referenced */
    private Collection<String>     referenceTypes;

    /** the sub-attributes of this attribute. */
    private Collection<Definition> subAttributes;

    /**
     ** specifies how the service provider enforces uniqueness of attribute
     ** values
     */
    private Uniqueness             uniqueness;

    /**
     ** indicates when an  attribute and associated values are returned in
     ** response to a GET request or in response to a PUT, POST, or PATCH
     ** request
     */
    private Returned               returned;

    /**
     ** indicates the circumstances under which the value of the attribute can
     ** be (re)defined
     */
    private Mutability             mutability;

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
    public Factory() {
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
    // Method: subAttributes
    /**
     ** Adds the sub-attributes of the attribute.
     **
     ** @param  value            the sub-attributes of the attribute.
     **                          <br>
     **                          Allowed object is array of {@link Definition}.
     **
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
     */
    public Factory subAttributes(final Definition... value) {
      if (value != null && value.length > 0) {
        if (this.subAttributes == null) {
          this.subAttributes = new LinkedList<Definition>();
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     **                          Allowed object is {@link Mutability}.
     **
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** @return                  the {@link Factory} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Factory}.
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
     ** Factory method to create a <code>Definition</code> from the configured
     ** prooerties.
     **
     ** @return                  an newly created instance of
     **                          <code>Definition</code> providing.
     **                          <br>
     **                          Possible object <code>Definition</code>.
     */
    public Definition build() {
      return new Definition(this.name, this.description, this.type, this.subAttributes, this.multiValued, this.required, this.caseExact, this.returned, this.mutability, this.uniqueness, this.canonical, this.referenceTypes);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Definition</code> with the specified properties
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
   **                            Allowed object array of {@link Definition}s.
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
// Definition(@JsonProperty(value="name", required=true) final String name,  @JsonProperty(value="type", required=true) final Type type, @JsonProperty(value="subAttributes") final Collection<Definition> attributes, @JsonProperty(value="multiValued", required=true) final boolean multiValued, @JsonProperty(value="description") final String description, @JsonProperty(value="required", required=true) final boolean required, @JsonProperty(value="canonicalValues") final Collection<String> canonical, @JsonProperty(value="caseExact") final boolean caseExact, @JsonProperty(value="mutability", required=true) final Mutability mutability, @JsonProperty(value="returned", required=true) final Returned returned, @JsonProperty(value="uniqueness") final Uniqueness uniqueness, @JsonProperty(value="referenceTypes") final Collection<String> reference) {
  Definition(@JsonProperty(value="name", required=true) final String name, @JsonProperty(value="description") final String description, @JsonProperty(value="type", required=true) final Type type, @JsonProperty(value="subAttributes") final Collection<Definition> attributes, @JsonProperty(value="multiValued") final Boolean multiValued, @JsonProperty(value="required", required=true) final Boolean required, @JsonProperty(value="caseExact") final Boolean caseExact, @JsonProperty(value="returned", required=true) final Returned returned, @JsonProperty(value="mutability") final Mutability mutability, @JsonProperty(value="uniqueness") final Uniqueness uniqueness, @JsonProperty(value="canonicalValues") final Collection<String> canonical, @JsonProperty(value="referenceTypes") final Collection<String> reference) {
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
   **                            Possible object array of {@link String}.
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
   **                            Possible object array of {@link Type}.
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
   **                            Possible object collection of
   **                            {@link Definition}.
   */
  @JsonGetter("subAttributes")
  public Collection<Definition> attributes() {
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
   **                            Possible object <code>boolean</code>.
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
   **                            Possible object array of {@link String}.
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
   **                            Possible object <code>boolean</code>.
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
   **                            Possible object array of {@link String}s.
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
   **                            Possible object <code>boolean</code>.
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
   **                            Possible object {@link Mutability}.
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
   **                            Possible object {@link Returned}.
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
   **                            Possible object {@link Uniqueness}.
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
   **                            Possible object array of {@link String}s.
   */
  @JsonGetter("referenceTypes")
  public Collection<String> reference() {
    return this.reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (this.type        != null ? this.type.hashCode() : 0);
    result = 31 * result + (this.attributes  != null ? this.attributes.hashCode() : 0);
    result = 31 * result + (this.multiValued ? 1 : 0);
    result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
    result = 31 * result + (this.required    ? 1 : 0);
    result = 31 * result + (this.canonical   != null ? this.canonical.hashCode() : 0);
    result = 31 * result + (this.caseExact   ? 1 : 0);
    result = 31 * result + (this.mutability  != null ? this.mutability.hashCode() : 0);
    result = 31 * result + (this.returned    != null ? this.returned.hashCode() : 0);
    result = 31 * result + (this.uniqueness  != null ? this.uniqueness.hashCode() : 0);
    result = 31 * result + (this.reference   != null ? this.reference.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Definition</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Definition</code>s may be different even though they contain the
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

    final Definition that = (Definition)other;
    if (this.caseExact != that.caseExact)
      return false;

    if (this.multiValued != that.multiValued)
      return false;

    if (this.required != that.required)
      return false;

    if (this.description != null ? !this.description.equals(that.description) : that.description != null)
      return false;

    if (this.mutability != null ? !this.mutability.equals(that.mutability) : that.mutability != null)
      return false;

    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    if (this.returned != null ? !this.returned.equals(that.returned) : that.returned != null)
      return false;

    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    if (this.uniqueness != null ? !this.uniqueness.equals(that.uniqueness) : that.uniqueness != null)
      return false;

    if (this.attributes != null ? !this.attributes.equals(that.attributes) : that.attributes != null)
      return false;

    if (this.canonical != null ? !this.canonical.equals(that.canonical) : that.canonical != null)
      return false;

    if (this.reference != null ? !this.reference.equals(that.reference) : that.reference != null)
      return false;

    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character " " (space).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private String indented(final String indent) {
    final StringBuilder builder = new StringBuilder(indent);
    builder.append("Name: ").append(name());
    builder.append(" Description: ").append(description());
    builder.append(" readOnly: ");
    builder.append(" required: ").append(required());
    builder.append(" caseExact: ").append(caseExact());
    builder.append(System.lineSeparator());
    if (attributes() != null) {
      for (Definition a : attributes()) {
        builder.append(a.indented(indent + "  "));
      }
    }
    return builder.toString();
  }
}