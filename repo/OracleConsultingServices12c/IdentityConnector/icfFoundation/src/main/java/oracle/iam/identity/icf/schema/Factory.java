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
    Subsystem   :   Foundation Shared Library

    File        :   Factory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Factory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.schema;

import java.lang.reflect.Field;

import java.util.Set;
import java.util.Date;
import java.util.Stack;
import java.util.Calendar;
import java.util.Collection;

import java.math.BigDecimal;

import javax.lang.model.type.NullType;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.ObjectClassUtil;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class Factory
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** <code>Factory</code> implements the basic functionality for common schema
 ** operations of annotated classes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Factory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Set<Class<?>> SIMPLE = CollectionUtility.set(
    Date.class
  , Long.class
  , String.class
  , Integer.class
  , NullType.class
  );

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Factory</code> database connector that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Factory()" and enforces use of the public method below.
   */
  private Factory() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Returns the schema for a class.
   ** <br>
   ** This will walk the inheritance tree looking for information about the
   ** ICF schema of the objects represented. This information comes from
   ** annotations and introspection.
   **
   ** @param  schema             the schema builder to create object classes.
   **                            <br>
   **                            Allowed object is  {@link SchemaBuilder}.
   ** @param  clazz              the class to get the schema for.
   **                            <br>
   **                            Allowed object is array of {@link Class} for
   **                            type {@link Resource}.
   **
   ** @return                    the {@link SchemaBuilder} passed in for method
   **                            chaining purpose.
   **                            <br>
   **                            Possible object is {@link SchemaBuilder}.
   */
  @SuppressWarnings("unchecked")
  public static SchemaBuilder build(final SchemaBuilder schema, final Class<? extends Resource>... clazz) {
    for (Class<? extends Resource> cursor : clazz)
      schema.defineObjectClass(defineObjectClass(schema, cursor));
    return schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Returns the schema for a class.
   ** <br>
   ** This will walk the inheritance tree looking for information about the
   ** ICF schema of the objects represented. This information comes from
   ** annotations and introspection.
   **
   ** @param  schema             the schema builder to create object classes.
   **                            <br>
   **                            Allowed object is  {@link SchemaBuilder}.
   ** @param  clazz              the class to get the schema for.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Class} for type
   **                            {@link Resource}.
   **
   ** @return                    the {@link SchemaBuilder} passed in for method
   **                            chaining purpose.
   **                            <br>
   **                            Possible object is {@link SchemaBuilder}.
   */
  public static SchemaBuilder build(final SchemaBuilder schema, final Collection<Class<? extends Resource>> clazz) {
    for (Class<? extends Resource> cursor : clazz)
      schema.defineObjectClass(defineObjectClass(schema, cursor));
    return schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   describe
  /**
   ** Returns property descriptors for the given class.
   **
   ** @param  clazz              the class to get the property descriptors for.
   **
   ** @return                    a collection of property values.
   */
  public static Collection<Field> describe(final Class<?> clazz) {
    final Collection<Field> properties = CollectionUtility.list(clazz.getDeclaredFields());
    if (Discoverable.class.isAssignableFrom(clazz)) {
      Class<?> cursor = clazz.getSuperclass();
      while (cursor != Object.class) {
        properties.addAll(CollectionUtility.list(cursor.getDeclaredFields()));
        cursor = cursor.getSuperclass();
      }
    }
    return properties;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defineObjectClass
  /**
   ** Returns schema attributes for the given class.
   **
   ** @param  schema             the {@link SchemaBuilder} populating the entire
   **                            schema to build for a class.
   **                            <br>
   **                            Allowed object is {@link SchemaBuilder}.
   ** @param  clazz              the class to get the schema attributes for.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link Resource}.
   **
   ** @return                    the ICF {@link ObjectClassInfo} for the given
   **                            class.
   **                            <br>
   **                            Possible object is {@link ObjectClassInfo}.
   */
  public static ObjectClassInfo defineObjectClass(final SchemaBuilder schema, final Class<? extends Resource> clazz) {
    final Stack<String> visited = new Stack<String>();
    return attributes(schema, visited, clazz, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns ICF schema attributes for a class.
   **
   ** @param  schema             the {@link SchemaBuilder} populating the entire
   **                            schema to build for a class.
   **                            <br>
   **                            Allowed object is {@link SchemaBuilder}.
   ** @param  visited            a {@link Stack} containing the classes visited
   **                            prior to this class.
   **                            <br>
   **                            Allowed object is {@link Stack} where each
   **                            element is of type {@link String}.
   **                            This is used for cycle detection.
   ** @param  clazz              the class to get the attributes for.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link Resource}.
   ** @param  complex            <code>true</code> if the attribute to built is
   **                            an complex attribute.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the ICF {@link ObjectClassInfo} for the given
   **                            class.
   **                            <br>
   **                            Possible object is {@link ObjectClassInfo}.
   */
  @SuppressWarnings("unchecked")
  private static ObjectClassInfo attributes(final SchemaBuilder schema, final Stack<String> visited, final Class<? extends Resource> clazz, final boolean complex) {
    if (visited.contains(clazz.getCanonicalName()))
      throw new RuntimeException("Cycles detected in Schema");

    // add the class to the collection to allow cycle detection
    visited.push(clazz.getCanonicalName());

    final String className = className(clazz);
    ObjectClassInfoBuilder builder = classBuilder(className);
    builder.setEmbedded(complex);

    final Collection<Field> properties = describe(clazz);
    for (Field cursor : properties) {
      // only generate schema for annotated fields.
      if (!cursor.isAnnotationPresent(Attribute.class))
        continue;

      final Attribute property = cursor.getAnnotation(Attribute.class);

      // evaluate the name of the attribute either given by the annotated name
      // or the name of the attribute it self
      String name = property.value();
      if (StringUtility.empty(name))
        name = cursor.getName();

      AttributeInfoBuilder     attribute = new AttributeInfoBuilder()
        .setName(name)
        .setRequired(property.required())
        .setReturnedByDefault(property.returned() == Returned.DEFAULT || property.returned() == Returned.ALWAYS);
      switch (property.mutability()) {
        case GENERATED   : attribute.setGenerated(true);
                           attribute.setCreateable(false);
                           attribute.setUpdateable(false);
                           break;
        case MUTABLE     : attribute.setGenerated(false);
                           attribute.setCreateable(true);
                           attribute.setUpdateable(true);
                           break;
        case IMMUTABLE   : attribute.setCreateable(false);
                           attribute.setUpdateable(false);
                           break;
        case WRITEONLY   : attribute.setReadable(false);
                           break;
        case CREATABLE   : attribute.setCreateable(true);
                           attribute.setUpdateable(false);
                           break;
        case UPDATABLE   : attribute.setCreateable(false);
                           attribute.setUpdateable(true);
                           break;
      }
      Class type = cursor.getType();
      if (multivalue(cursor, property)) {
        // if this is a multivalued attribute the real attribute class is the
        // one specified in the annotation, not the list, set, array, etc.
        type = property.multiValueClass();
        final String embedName = String.format("%s.%s", className, name);
        if (!SIMPLE.contains(type)) {
          embeddedObjectClass(schema, visited, embedName, type);
        }
        attribute.setObjectClassName(embedName);
        attribute.setType(EmbeddedObject.class);
        attribute.setMultiValued(true);
      }
      else {
        attribute.setType(property.sensitive() ? GuardedString.class : attributeType(type));
      }
      builder.addAttributeInfo(attribute.build());
    }
    // get the current class from the cycle detection collector
    visited.pop();
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   embeddedObjectClass
  /**
   ** Returns embedded ICF schema attributes for a class.
   **
   ** @param  schema             the {@link SchemaBuilder} populating the entire
   **                            schema to build for a class.
   **                            <br>
   **                            Allowed object is {@link SchemaBuilder}.
   ** @param  builder            the {@link ObjectClassInfoBuilder} populating
   **                            the attribute schema to build for a class.
   **                            <br>
   **                            Allowed object is {@link ObjectClassInfoBuilder}.
   ** @param  visited            a {@link Stack} containing the classes visited
   **                            prior to this class.
   **                            <br>
   **                            Allowed object is {@link Stack} where each
   **                            element is of type {@link String}.
   **                            This is used for cycle detection.
   ** @param  clazz              the class to get the attributes for.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link Resource}.
   ** @param  complex            <code>true</code> if the attribute to built is
   **                            an complex attribute.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @SuppressWarnings("unchecked")
  private static void embeddedObjectClass(final SchemaBuilder schema, final Stack<String> visited, final String className, final Class<? extends Resource> clazz) {
    if (visited.contains(clazz.getCanonicalName()))
      throw new RuntimeException("Cycles detected in Schema");

    // add the class to the collection to allow cycle detection
    visited.push(clazz.getCanonicalName());

    final ObjectClassInfoBuilder builder = classBuilder(className);
    builder.setEmbedded(true);

    final Collection<Field> properties = describe(clazz);
    for (Field cursor : properties) {
      // only generate schema for annotated fields.
      if (!cursor.isAnnotationPresent(Attribute.class))
        continue;

      final Attribute          property  = cursor.getAnnotation(Attribute.class);

      // evaluate the name of the attribute either given by the annotated name
      // or the name of the attribute it self
      String name = property.value();
      if (StringUtility.empty(name))
        name = cursor.getName();

      AttributeInfoBuilder     attribute = new AttributeInfoBuilder()
        .setName(name)
        .setRequired(property.required())
        .setReturnedByDefault(property.returned() == Returned.DEFAULT || property.returned() == Returned.ALWAYS);

      switch (property.mutability()) {
        case GENERATED   : attribute.setGenerated(true);
                           attribute.setCreateable(false);
                           attribute.setUpdateable(false);
                           break;
        case MUTABLE     : attribute.setGenerated(false);
                           attribute.setCreateable(true);
                           attribute.setUpdateable(true);
                           break;
        case IMMUTABLE   : attribute.setCreateable(false);
                           attribute.setUpdateable(false);
                           break;
        case WRITEONLY   : attribute.setReadable(false);
                           break;
        case CREATABLE   : attribute.setCreateable(true);
                           attribute.setUpdateable(false);
                           break;
        case UPDATABLE   : attribute.setCreateable(false);
                           attribute.setUpdateable(true);
                           break;
      }
      Class type = cursor.getType();
      if (multivalue(cursor, property)) {
        // if this is a multivalued attribute the real attribute class is the
        // one specified in the annotation, not the list, set, array, etc.
        type = property.multiValueClass();
        final String embedName = String.format("%s.%s", className, name);
        if (!SIMPLE.contains(type)) {
          embeddedObjectClass(schema, visited, embedName, type);
        }
        attribute.setObjectClassName(embedName);
        attribute.setMultiValued(true);
      }
      else {
        attribute.setObjectClassName(className);
      }
      attribute.setType(EmbeddedObject.class);
      builder.addAttributeInfo(attribute.build());
    }
    // get the current class from the cycle detection collector
    visited.pop();
    schema.defineObjectClass(builder.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classBuilder
  /**
   ** Returns the schema builder for a class.
   **
   ** @param  className          the object class name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the object class builder.
   **                            <br>
   **                            Possible object is
   **                            {@link ObjectClassInfoBuilder}.
   */
  private static ObjectClassInfoBuilder classBuilder(final String className) {
    final ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();
    builder.setType(className);
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   className
  /**
   ** Returns the object class name for a class.
   **
   ** @param  clazz              the class to get the object class name for.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link Resource}.
   **
   ** @return                    the object class name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String className(final Class<? extends Resource> clazz) {
    final Schema schema = clazz.getAnnotation(Schema.class);
    final String name   = (schema == null) ? clazz.getSimpleName() : StringUtility.empty(schema.value()) ? clazz.getSimpleName() : schema.value();
    if (!ObjectClassUtil.isSpecialName(name)) {
      return ObjectClassUtil.createSpecialName(name.toUpperCase());
    }
    else
      return name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multivalue
  /**
   ** This method will determine if this attribute can have multiple values, and
   ** set that in the builder.
   **
   ** @param  descriptor          the property descriptor for the field to build
   **                             the attribute for.
   ** @param  property            the schema property annotation for the field
   **                             to build an attribute for.
   **
   ** @return                    <code>true</code> if the class is a collection
   **                            or an array, or <code>false</code> if not.
   */
  private static boolean multivalue(final Field descriptor, final Attribute property) {
    final boolean nulltype   = property.multiValueClass().equals(NullType.class);
    final boolean multivalue = multivalue(descriptor.getType());

    // if the multiValueClass attribute is present in the annotation, make sure
    // this is a collection or array.
    if (!nulltype && !multivalue)
      throw new RuntimeException("Property named " + descriptor.getName() + " is annotated with a multiValueClass, but is not a Collection or an array");

    if (nulltype && multivalue)
      throw new RuntimeException("Property named " + descriptor.getName() + " is not annotated with a multiValueClass, but is a Collection or an array");

    return (!nulltype && multivalue);
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
   ** @return                    <code>true</code> if the class is a collection
   **                            or an array, or <code>false</code> if not.
   */
  private static boolean multivalue(final Class<?> clazz) {
    return (clazz.isArray() && byte[].class != clazz) || Collection.class.isAssignableFrom(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeType
  /**
   ** Returns the attribute type for a given property descriptor.
   ** <br>
   ** This method will attempt to decide what attribute type should be in the
   ** schema based on the java class of the attribute.
   **
   ** @param  clazz              the java {@link Class} for an attribute of a
   **                            ICF object.
   **
   ** @return                    an attribute type.
   **                            <br>
   **                            Possible object is {@link Attribute.Type}.
   */
  private static Class attributeType(final Class clazz) {
    if ((clazz == Boolean.class) || (clazz == boolean.class)) {
      return Boolean.class;
    }
    else if ((clazz == Integer.class) || (clazz == int.class)) {
      return Integer.class;
    }
    else if ((clazz == Long.class) || (clazz == long.class)) {
      return Long.class;
    }
    else if ((clazz == Double.class) || (clazz == double.class) || (clazz == Float.class) || (clazz == float.class) || (clazz == BigDecimal.class)) {
      return Double.class;
    }
    else if ((clazz == Date.class) || (clazz == Calendar.class)) {
      return Long.class;
    }
    else {
      return String.class;
    }
  }
}