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

    File        :   SchemaFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SchemaFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.v2.schema;

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

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.rest.schema.Discoverable;

import oracle.iam.system.simulation.scim.schema.Metadata;

import oracle.iam.system.simulation.scim.annotation.Schema;
import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

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
  public static final Definition             SCHEMAS;

  /** the attribute definition for the SCIM 2 standard id attribute */
  public static final Definition             ID;

  /** the attribute definition for the SCIM 2 standard externalId attribute */
  public static final Definition             EXTERNAL_ID;

  /** the attribute definition for the SCIM 2 standard meta attribute */
  public static final Definition             METADATA;

  /**
   ** The collection of attribute definitions for SCIM 2 standard common
   ** attributes: schemas, id, externalId, and meta.
   */
  public static final Collection<Definition> COMMON;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    Definition.Factory factory = new Definition.Factory()
      .name("schemas")
      .type(Definition.Type.STRING)
      .required(true)
      .caseExact(true)
      .multiValued(true)
      .returned(Definition.Returned.ALWAYS)
      .mutability(Definition.Mutability.READ_WRITE);
    SCHEMAS = factory.build();

    factory = new Definition.Factory()
      .name("id")
      .type(Definition.Type.STRING)
      .caseExact(true)
      .returned(Definition.Returned.ALWAYS)
      .mutability(Definition.Mutability.READ_ONLY);
    ID = factory.build();

    factory = new Definition.Factory()
      .name("externalId")
      .type(Definition.Type.STRING)
      .caseExact(true)
      .mutability(Definition.Mutability.READ_WRITE);
    EXTERNAL_ID = factory.build();

    factory = new Definition.Factory()
      .name("meta")
      .type(Definition.Type.COMPLEX)
      .mutability(Definition.Mutability.READ_ONLY);

    Collection<Definition> subAttributes = attributes(Metadata.class);
    factory.subAttributes(subAttributes.toArray(new Definition[subAttributes.size()]));

    METADATA = factory.build();
    COMMON   = CollectionUtility.unmodifiableList(SCHEMAS, ID, EXTERNAL_ID, METADATA);
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
   */
  public SchemaFactory() {
    // ensure inheritance
    super();
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
   **
   ** @return                    the schema.
   */
  public static SchemaResource schema(final Class<?> clazz) {
    final Schema annotation = clazz.getAnnotation(Schema.class);
    // only generate schema for annotated classes.
    if (annotation == null) {
      return null;
    }
    return new SchemaResource(annotation.id(), annotation.name(), annotation.description(), attributes(clazz));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns schema attributes for the given class.
   **
   ** @param  clazz              the class to get the schema attributes for.
   **
   ** @return                    a collection of attributes.
   */
  public static Collection<Definition> attributes(final Class clazz) {
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
   ** @param  clazz              the class to get the attributes for.
   **
   ** @return                    a collection of SCIM schema attributes for the
   */
  private static Collection<Definition> attributes(final Stack<String> visited, final Class<?> clazz) {
    final String className = clazz.getCanonicalName();
    if (!Definition.class.isAssignableFrom(clazz) && visited.contains(className))
      throw new RuntimeException("Cycles detected in Schema");

    final Collection<Field> properties = CollectionUtility.list(clazz.getDeclaredFields());
    if (Discoverable.class.isAssignableFrom(clazz)) {
      Class<?> cursor = clazz.getSuperclass();
//      while (cursor !=  Discoverable.class) {
      while (cursor != Object.class) {
        properties.addAll(CollectionUtility.list(cursor.getDeclaredFields()));
        cursor = cursor.getSuperclass();
      }
    }

    final Collection<Definition> attributes = new ArrayList<Definition>();
    for (Field cursor : properties) {
      if (cursor.getName().equals("subAttributes") && Definition.class.isAssignableFrom(clazz) && visited.contains(className)) {
        // skip second nesting of subAttributes the second time around since
        // there is no subAttributes of subAttributes in SCIM.
        continue;
      }

      Attribute    schemaProperty = null;
      JsonProperty jsonProperty   = null;
      if (cursor.isAnnotationPresent(Attribute.class)) {
        schemaProperty = cursor.getAnnotation(Attribute.class);
      }
      if (cursor.isAnnotationPresent(JsonProperty.class)) {
        jsonProperty = cursor.getAnnotation(JsonProperty.class);
      }

      // Only generate schema for annotated fields.
      if (schemaProperty == null) {
        continue;
      }

      Definition.Factory factory = new Definition.Factory();
      addName(factory,            cursor, jsonProperty);
      addDescription(factory,     schemaProperty);
      addCaseExact(factory,       schemaProperty);
      addRequired(factory,        schemaProperty);
      addReturned(factory,        schemaProperty);
      addUniqueness(factory,      schemaProperty);
      addReferenceTypes(factory,  schemaProperty);
      addMutability(factory,      schemaProperty);
      addMultiValued(factory,     cursor, schemaProperty);
      addCanonicalValues(factory, schemaProperty);

      Class propertyCls = cursor.getType();

      // if this is a multivalued attribute the real sub attribute class is the
      // the one specified in the annotation, not the list, set, array, etc.
      if ((schemaProperty.multiValueClass() != NullType.class)) {
        propertyCls = schemaProperty.multiValueClass();
      }

      Definition.Type type = attributeType(propertyCls);
      factory.type(type);
      if (type == Definition.Type.COMPLEX) {
        // Add this class to the list to allow cycle detection
        visited.push(clazz.getCanonicalName());
        Collection<Definition> subAttributes = attributes(visited, propertyCls);
        factory.subAttributes(subAttributes.toArray(new Definition[subAttributes.size()]));
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  descriptor          the property descriptor for the field to build
   **                             the attribute for.
   ** @param  property            the Jackson JsonProperty annotation for the
   **                             field.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addName(final Definition.Factory factory, final Field descriptor, final JsonProperty property) {
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  descriptor          the property descriptor for the field to build
   **                             the attribute for.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addMultiValued(final Definition.Factory factory, final Field descriptor, final Attribute property) {
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addDescription(final Definition.Factory factory, final Attribute property) {
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addCaseExact(final Definition.Factory factory, final Attribute property) {
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addRequired(final Definition.Factory factory, final Attribute property) {
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addCanonicalValues(final Definition.Factory factory, final Attribute property) {
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addReturned(final Definition.Factory factory, final Attribute property) {
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addUniqueness(final Definition.Factory factory, final Attribute property) {
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addReferenceTypes(final Definition.Factory factory, final Attribute property) {
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
   ** @param  factory             the factory for a scim attribute.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                     the {@link Definition.Factory} to allow method
   **                             chaining.
   **                             <br>
   **                             Possible object is {@link Definition.Factory}.
   */
  private static Definition.Factory addMutability(final Definition.Factory factory, final Attribute property) {
    if (property != null) {
      factory.mutability(property.mutability());
    }
    else {
      factory.mutability(Definition.Mutability.READ_WRITE);
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
   **
   ** @return                     an attribute type.
   **                             <br>
   **                             Possible object is {@link Definition.Type}.
   */
  private static Definition.Type attributeType(final Class clazz) {
    if ((clazz == Integer.class) || (clazz == int.class)) {
      return Definition.Type.INTEGER;
    }
    else if ((clazz == Boolean.class) || (clazz == boolean.class)) {
      return Definition.Type.BOOLEAN;
    }
    else if ((clazz == Double.class) || (clazz == double.class) || (clazz == Float.class) || (clazz == float.class) || (clazz == BigDecimal.class)) {
      return Definition.Type.DECIMAL;
    }
    else if ((clazz == String.class) || (clazz == boolean.class)) {
      return Definition.Type.STRING;
    }
    else if ((clazz == URI.class) || (clazz == URL.class)) {
      return Definition.Type.REFERENCE;
    }
    else if ((clazz == Date.class) || (clazz == Calendar.class)) {
      return Definition.Type.DATETIME;
    }
    else if ((clazz == byte[].class)) {
      return Definition.Type.BINARY;
    }
    else {
      return Definition.Type.COMPLEX;
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
   **
   ** @return                    <code>true</code> if the class is a collection or
   **                            an array, or <code>false</code> if not.
   */
  private static boolean multivalue(final Class<?> clazz) {
    return (clazz.isArray() && byte[].class != clazz) || Collection.class.isAssignableFrom(clazz);
  }
}