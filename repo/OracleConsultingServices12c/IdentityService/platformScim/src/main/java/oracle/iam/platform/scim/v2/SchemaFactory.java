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

    File        :   SchemaFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SchemaFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.v2;

import java.lang.reflect.Field;

import java.util.Date;
import java.util.Stack;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collection;

import java.math.BigDecimal;

import java.net.URI;
import java.net.URL;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.rest.schema.Discoverable;

import oracle.iam.platform.scim.schema.Resource;
import oracle.iam.platform.scim.schema.Metadata;

import oracle.iam.platform.scim.AttributeDefinition;

import oracle.iam.platform.scim.annotation.Schema;
import oracle.iam.platform.scim.annotation.Attribute;

import static oracle.iam.platform.scim.annotation.Attribute.Type;
import static oracle.iam.platform.scim.annotation.Attribute.Returned;
import static oracle.iam.platform.scim.annotation.Attribute.Mutability;
import static oracle.iam.platform.scim.annotation.Attribute.Uniqueness;

////////////////////////////////////////////////////////////////////////////////
// abstract class SchemaFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>SchemaFactory</code> implements the basic functionality for common
 ** SCIM 2.0 schema operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class SchemaFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the attribute definition for the SCIM 2 standard schemas attribute */
  public static final AttributeDefinition             SCHEMAS;

  /** the attribute definition for the SCIM 2 standard id attribute */
  public static final AttributeDefinition             ID;

  /** the attribute definition for the SCIM 2 standard externalId attribute */
  public static final AttributeDefinition             EXTERNAL;

  /** the attribute definition for the SCIM 2 standard meta attribute */
  public static final AttributeDefinition             METADATA;

  /**
   ** The collection of attribute definitions for SCIM 2 standard common
   ** attributes: schemas, id, externalId, and meta.
   */
  public static final Collection<AttributeDefinition> COMMON;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    final Collection<AttributeDefinition> sub = attributes(Metadata.class);
    // Attention:
    // The attribute definitions below needs to exactly match the attribute
    // annotation defined for {@link oracle.iam.platform.scim.schema.Entity}
    // due to the heavy use of sets and hash maps that requires the exact values
    // return for hashCode and equals for the attribute definitions.
    ID       = AttributeDefinition.factory().name(Resource.IDENTIFIER).type(Type.STRING).required(false).caseExact(true).returned(Returned.ALWAYS).mutability(Mutability.READ_ONLY).uniqueness(Uniqueness.SERVER).description("A unique identifier for a SCIM resource as defined by the service provider").build();
    SCHEMAS  = AttributeDefinition.factory().name(Resource.SCHEMA).type(Type.STRING).required(true).multiValued(true).returned(Returned.ALWAYS).description("The schemas attribute is a REQUIRED attribute and is an array of Strings containing URIs that are used to indicate the namespaces of the SCIM schemas that define the attributes present in the current JSON structure").build();
    EXTERNAL = AttributeDefinition.factory().name(Resource.EXTERNAL).type(Type.STRING).caseExact(true).description("A String that is an identifier for the resource as defined by the provisioning client").build();
    METADATA = AttributeDefinition.factory().name(Resource.METADATA).type(Type.COMPLEX).mutability(Mutability.READ_ONLY).sub(sub.toArray(new AttributeDefinition[sub.size()])).description("A complex attribute containing resource metadata").build();
    COMMON   = CollectionUtility.unmodifiableList(SCHEMAS, ID, EXTERNAL, METADATA);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>SchemaFactory</code> database connector that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new SchemaFactory()" and enforces use of the public method below.
   */
  private SchemaFactory() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Returns the schema for a class.
   ** <br>
   ** This will walk the inheritance tree looking for information about the
   ** SCIM schema of the objects represented. This information comes from
   ** annotations and introspection.
   **
   ** @param  clazz              the class to get the schema for.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object is {@link SchemaResource}.
   */
  public static SchemaResource schema(final Class<?> clazz) {
    final Schema annotation = clazz.getAnnotation(Schema.class);
    // only generate schema for annotated classes.
    return (annotation == null) ? null : new SchemaResource(annotation.id(), annotation.name(), annotation.description(), attributes(clazz));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns schema attributes for the given class.
   **
   ** @param  clazz              the class to get the schema attributes for.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    a collection of attributes.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link AttributeDefinition}.
   */
  public static Collection<AttributeDefinition> attributes(final Class<?> clazz) {
    final Stack<String> visited = new Stack<String>();
    return attributes(visited, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns SCIM schema attributes for a class.
   **
   ** @param  visited            a {@link stack} containing the classes visited
   **                            prior to this class.
   **                            This is used for cycle detection.
   **                            <br>
   **                            Allowed object is  {@link Stack} where each
   **                            element is of type {@link String}.
   ** @param  clazz              the class to get the attributes for.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    a collection of SCIM schema attributes for the
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link AttributeDefinition}.
   */
  private static Collection<AttributeDefinition> attributes(final Stack<String> visited, final Class<?> clazz) {
    final String className = clazz.getCanonicalName();
    if (!AttributeDefinition.class.isAssignableFrom(clazz) && visited.contains(className))
      throw new RuntimeException("Cycles detected in Schema");

    final Collection<Field> properties = CollectionUtility.list(clazz.getDeclaredFields());
    if (Discoverable.class.isAssignableFrom(clazz)) {
      Class<?> cursor = clazz.getSuperclass();
      while (cursor != Object.class) {
        properties.addAll(CollectionUtility.list(cursor.getDeclaredFields()));
        cursor = cursor.getSuperclass();
      }
    }

    final Collection<AttributeDefinition> attributes = new ArrayList<AttributeDefinition>();
    for (Field cursor : properties) {
      if (cursor.getName().equals("attributes") && AttributeDefinition.class.isAssignableFrom(clazz) && visited.contains(className)) {
        // skip second nesting of subAttributes the second time around since
        // there is no subAttributes of subAttributes in SCIM.
        continue;
      }

      Attribute    schema   = null;
      JsonProperty outbound = null;
      if (cursor.isAnnotationPresent(Attribute.class)) {
        schema = cursor.getAnnotation(Attribute.class);
      }
      // generate schema for annotated fields only
      if (schema == null) {
        continue;
      }

      if (cursor.isAnnotationPresent(JsonProperty.class)) {
        outbound = cursor.getAnnotation(JsonProperty.class);
      }

      AttributeDefinition.Factory factory = AttributeDefinition.factory();
      addName(factory,            cursor, outbound);
      addDescription(factory,     schema);
      addRequired(factory,        schema);
      addCaseExact(factory,       schema);
      addMutability(factory,      schema);
      addUniqueness(factory,      schema);
      addReturned(factory,        schema);
      addReferenceTypes(factory,  schema);
      addMultiValued(factory,     cursor, schema);
      addCanonicalValues(factory, schema);

      Class property = cursor.getType();
      // if this is a multivalued attribute the real sub attribute class is the
      // one specified in the annotation, not the list, set, array, etc.
      if ((schema.multiValueClass() != NullType.class)) {
        property = schema.multiValueClass();
      }

      Type type = attributeType(property);
      factory.type(type);
      if (type == Type.COMPLEX) {
        // add this class to the list to allow cycle detection
        visited.push(clazz.getCanonicalName());
        Collection<AttributeDefinition> sub = attributes(visited, property);
        factory.sub(sub.toArray(new AttributeDefinition[sub.size()]));
        visited.pop();
      }
      attributes.add(factory.build());
    }
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addName
  /**
   ** This method will find the name for the attribute, and add it to the
   ** factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  descriptor         the property descriptor for the field to build
   **                            the attribute for.
   **                            <br>
   **                            Allowed object is {@link Field}.
   ** @param  property           the Jackson JsonProperty annotation for the
   **                            field.
   **                            <br>
   **                            Allowed object is {@link JsonProperty}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AttributeDefinition.Factory}.
   */
  private static AttributeDefinition.Factory addName(final AttributeDefinition.Factory factory, final Field descriptor, final JsonProperty property) {
    if (property != null && !property.value().equals(JsonProperty.USE_DEFAULT_NAME)) {
      factory.name(property.value());
    }
    else {
      factory.name(descriptor.getName());
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMultiValued
  /**
   ** This method will determine if this attribute can have multiple values, and
   ** set that in the factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  descriptor         the property descriptor for the field to build
   **                            the attribute for.
   **                            <br>
   **                            Allowed object is {@link Field}.
   ** @param  property           the schema property annotation for the field
   **                            to build an attribute for.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AttributeDefinition.Factory}.
   */
  private static AttributeDefinition.Factory addMultiValued(final AttributeDefinition.Factory factory, final Field descriptor, final Attribute property) {
    final boolean  notnull    = !property.multiValueClass().equals(NullType.class);
    final boolean  multivalue = multivalue(descriptor.getType());

    // if the multiValuedClass attribute is present in the annotation,
    // make sure this is a collection or array.
    if (notnull && !multivalue)
      throw new RuntimeException("Property named " + descriptor.getName() + " is annotated with a multiValuedClass, but is not a Collection or an array");

    if (!notnull && multivalue)
      throw new RuntimeException("Property named " + descriptor.getName() + " is not annotated with a multiValuedClass, but is a Collection or an array");

    factory.multiValued(notnull);
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDescription
  /**
   ** This method will find the description for the attribute, and set that in
   ** the factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  property           the schema property annotation for the field
   **                            to build an attribute for.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AttributeDefinition.Factory}.
   */
  private static AttributeDefinition.Factory addDescription(final AttributeDefinition.Factory factory, final Attribute property) {
    if (property != null) {
      factory.description(property.description());
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCaseExact
  /**
   ** This method will find the case exact boolean for the attribute, and set
   ** that in the factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  property           the schema property annotation for the field
   **                            to build an attribute for.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is {@link AttributeDefinition.Factory}.
   */
  private static AttributeDefinition.Factory addCaseExact(final AttributeDefinition.Factory factory, final Attribute property) {
    if (property != null) {
      factory.caseExact(property.caseExact());
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRequired
  /**
   ** This method will find the required boolean for the attribute, and set that
   ** in the factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  property           the schema property annotation for the field
   **                            to build an attribute for.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AttributeDefinition.Factory}.
   */
  private static AttributeDefinition.Factory addRequired(final AttributeDefinition.Factory factory, final Attribute property) {
    if (property != null) {
      factory.required(property.required());
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCanonicalValues
  /**
   ** This method will find the canonical values for the attribute, and set that
   ** in the factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  property           the schema property annotation for the field
   **                            to build an attribute for.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AttributeDefinition.Factory}.
   */
  private static AttributeDefinition.Factory addCanonicalValues(final AttributeDefinition.Factory factory, final Attribute property) {
    if (property != null) {
      factory.canonical(property.canonical());
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addReturned
  /**
   ** This method will find the returned constraint for the attribute, and set
   ** that in the factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  property           the schema property annotation for the field
   **                            to build an attribute for.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is {@link AttributeDefinition.Factory}.
   */
  public static AttributeDefinition.Factory addReturned(final AttributeDefinition.Factory factory, final Attribute property) {
    if (property != null) {
      factory.returned(property.returned());
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addUniqueness
  /**
   ** This method will find the uniqueness constraint for the attribute, and set
   ** that in the factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  property           the schema property annotation for the field
   **                            to build an attribute for.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AttributeDefinition.Factory}.
   */
  public static AttributeDefinition.Factory addUniqueness(final AttributeDefinition.Factory factory, final Attribute property) {
    if (property != null) {
      factory.uniqueness(property.uniqueness());
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addReferenceTypes
  /**
   ** This method will find the reference types for the attribute, and set that
   ** in the factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  property           the schema property annotation for the field
   **                            to build an attribute for.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AttributeDefinition.Factory}.
   */
  private static AttributeDefinition.Factory addReferenceTypes(final AttributeDefinition.Factory factory, final Attribute property) {
    if (property != null) {
      factory.reference(property.reference());
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMutability
  /**
   ** This method will find the mutability constraint for the attribute, and set
   ** that in the factory.
   **
   ** @param  factory            the factory for a scim attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeDefinition.Factory}.
   ** @param  property           the schema property annotation for the field
   **                            to build an attribute for.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the {@link AttributeDefinition.Factory} to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link AttributeDefinition.Factory}.
   */
  private static AttributeDefinition.Factory addMutability(final AttributeDefinition.Factory factory, final Attribute property) {
    if (property != null) {
      factory.mutability(property.mutability());
    }
    else {
      factory.mutability(Mutability.READ_WRITE);
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeType
  /**
   ** Returns the attribute type for a given property descriptor.
   ** <br>
   ** This method will attempt to decide what SCIM attribute type should be in
   ** the schema based on the java class of the attribute.
   **
   ** @param  clazz              the java {@link Class} for an attribute of a
   **                            SCIM object.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    an attribute type.
   **                            <br>
   **                            Possible object is {@link Type}.
   */
  private static Type attributeType(final Class clazz) {
    if ((clazz == Integer.class) || (clazz == int.class)) {
      return Type.INTEGER;
    }
    else if ((clazz == Boolean.class) || (clazz == boolean.class)) {
      return Type.BOOLEAN;
    }
    else if ((clazz == Double.class) || (clazz == double.class) || (clazz == Float.class) || (clazz == float.class) || (clazz == BigDecimal.class)) {
      return Type.DECIMAL;
    }
    else if ((clazz == String.class) || (clazz == boolean.class)) {
      return Type.STRING;
    }
    else if ((clazz == URI.class) || (clazz == URL.class)) {
      return Type.REFERENCE;
    }
    else if ((clazz == Date.class) || (clazz == Calendar.class)) {
      return Type.DATETIME;
    }
    else if ((clazz == byte[].class)) {
      return Type.BINARY;
    }
    else {
      return Type.COMPLEX;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multivalue
  /**
   ** Returns <code>true</code> if the supplied class is a collection or an
   ** array. This is primarily used to determine if it's a multi-valued
   ** attribute.
   **
   ** @param  clazz              the class to introspect.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    <code>true</code> if the class is a collection or
   **                            an array, or <code>false</code> if not.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private static boolean multivalue(final Class<?> clazz) {
    return (clazz.isArray() && byte[].class != clazz) || Collection.class.isAssignableFrom(clazz);
  }
}