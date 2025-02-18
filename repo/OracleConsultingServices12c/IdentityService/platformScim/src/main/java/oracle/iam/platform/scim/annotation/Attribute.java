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

    File        :   Attribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    Attribute.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

////////////////////////////////////////////////////////////////////////////////
// annotation Attribute
// ~~~~~~~~~~ ~~~~~~~~~
/**
 ** Annotation for getter methods of a SCIM object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Target(value   =ElementType.FIELD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Attribute {

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
    // Method: of
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
    public static Type of(final String value) {
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
      READ_ONLY("readOnly")
      /** the attribute can be read, and written */
    , READ_WRITE("readWrite")
      /**
       ** the attribute can be read, and cannot be set after object creation
       ** <br>
       ** It can be set during object creation.
       */
    , IMMUTABLE("immutable")
      /**
       ** the attribute can only be written, and not read
       ** <br>
       ** This might be used for password hashes for example.
       */
    , WRITE_ONLY("writeOnly")
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
    // Method: of
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
    public static Mutability of(final String value) {
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
      ALWAYS("always")
      /** indicates thathe attribute is never returned */
    , NEVER("never")
      /** indicates that the attribute is returned by default */
    , DEFAULT("default")
      /** indicates that the attribute is only returned if requested */
    , REQUEST("request")
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
    // Method: of
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
    public static Returned of(final String value) {
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
      NONE("none")
      /**
       ** indicates that this attribute's value must be unique for a given
       ** server
       */
    , SERVER("server")
      /** indicates that this attribute's value must be globally unique */
    , GLOBAL("global")
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
    // Method: of
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
    public static Uniqueness of(final String value) {
      for(Uniqueness uniqueness : Uniqueness.values()) {
        if(uniqueness.value.equalsIgnoreCase(value)) {
          return uniqueness;
        }
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   caseExact
  /**
   ** Determines if the attribute value is case sensitive.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>caseExact</code> is <code>false</code> (i.e., case-insensitive).
   **
   ** @return                    <code>true</code> if the attribute value's
   **                            case sensitivity; <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean caseExact() default false;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Determines if the attribute value is required.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>required</code> is <code>false</code> (i.e., not REQUIRED).
   **
   ** @return                    <code>true</code> if the attribute value is
   **                            required; <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean required() default false;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the attribute.
   **
   ** @return                    the description of the attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String description() default "";

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueClass
  /**
   ** Determines if the attribute is multi-valued, this holds the type of the
   ** child object.
   **
   ** @return                    ror a multi-valued attribute, the type of the
   **                            child object.
   */
  Class multiValueClass() default NullType.class;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueness
  /**
   ** Returns uniqueness constraint for the attribute.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>uniqueness</code> is <code>none</code> (has no uniqueness enforced).
   **
   ** @return                    the uniqueness constraint for the attribute.
   **                            <br>
   **                            Possible object is {@link Uniqueness}.
   */
  Uniqueness uniqueness() default Uniqueness.NONE;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutability
  /**
   ** Returns mutability constraint for the attribute.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>mutability</code> is <code>readWrite</code> (i.e., modifiable).
   **
   ** @return                    the mutability constraint for the attribute.
   **                            <br>
   **                            Possible object is {@link Mutability}.
   */
  Mutability mutability() default Mutability.READ_WRITE;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returned
  /**
   ** Returns the return constraint for the attribute.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>returned</code> is <code>default</code> (the attribute value is
   ** returned by default).
   **
   ** @return                    the return constraint for the attribute.
   **                            <br>
   **                            Possible object is {@link Returned}.
   */
  Returned returned() default Returned.DEFAULT;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canonical
  /**
   ** Returns canonical values that may appear in an attribute.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>canonicalValues</code> is none assigned.
   **
   ** @return                    the canonical values that may appear in an
   **                            attribute.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  String[] canonical() default {};

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** Returns the reference types for the attribute.
   **
   ** @return                    the reference types for the attribute.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  String[] reference() default {};
}