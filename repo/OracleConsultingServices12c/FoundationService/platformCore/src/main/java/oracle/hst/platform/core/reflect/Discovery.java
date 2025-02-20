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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   Discovery.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Discovery.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.reflect;

import java.lang.annotation.Annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collection;

import oracle.hst.platform.core.SystemError;
import oracle.hst.platform.core.SystemBundle;

import oracle.hst.platform.core.logging.Logger;

import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.core.utility.CollectionUtility;

//////////////////////////////////////////////////////////////////////////////
// abstract class Discovery
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** Collection of utility methods for working with reflection.
 ** <br>
 ** This class only uses basic reflection mechanisms provided by the Reflection
 ** API. It provides methods that are missing in the standard API.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Discovery {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The category of the logging facility to use. */
  private static final String category = Discovery.class.getName();
  /** The associated with the logging category */
  private static final Logger logger   = Logger.create(category);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Discovery</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Discovery()" and enforces use of the public method below.
   */
  private Discovery() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Returns <code>true</code> if the given string is <code>null</code> or is
   ** empty.
   **
   ** @param  value              the value to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the given string is
   **                            <code>null</code> or is empty.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
	public static boolean empty(final String value) {
		return StringUtility.empty(value);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
	/**
	 ** Returns <code>true</code> if the given {@link Set} is <code>null</code> or
   ** is empty.
   **
   ** @param  value              the {@link Set} to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link Set}.
   **
   ** @return                    <code>true</code> if the given
   **                            {@link Set} is <code>null</code> or is empty.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean empty(final Set<?> value) {
		return CollectionUtility.empty(value);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
	/**
	 ** Returns <code>true</code> if the given {@link List} is <code>null</code>
   ** or is empty.
   **
   ** @param  value              the {@link List} to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link List}.
   **
   ** @return                    <code>true</code> if the given
   **                            {@link List} is <code>null</code> or is empty.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean empty(final List<?> value) {
		return CollectionUtility.empty(value);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
	/**
	 ** Returns <code>true</code> if the given {@link Map} is <code>null</code> or
   ** is empty.
   **
   ** @param  value              the {@link Map} to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link Map}.
   **
   ** @return                    <code>true</code> if the given
   **                            {@link Map} is <code>null</code> or is empty.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean empty(final Map<?, ?> value) {
		return CollectionUtility.empty(value);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
	/**
	 ** Returns <code>true</code> if the given {@link Collection} is
   ** <code>null</code> or is empty.
   **
   ** @param  value              the {@link Collection} to be checked on
   **                            emptiness.
   **                            <br>
   **                            Allowed object is {@link Collection}.
   **
   ** @return                    <code>true</code> if the given
   **                            {@link Collection} is <code>null</code> or is
   **                            empty.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean empty(final Collection<?> value) {
		return CollectionUtility.empty(value);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Returns <code>true</code> if the given object is <code>null</code> or an
   ** empty array or has an empty toString() result.
   **
   ** @param  value              the value to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the given object is
   **                            <code>null</code> or an empty array or has an
   **                            empty toString() result.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean empty(final Object value) {
    if (value == null)
      return true;
    else if (value instanceof String)
      return StringUtility.empty((String)value);
    else if (value instanceof Set)
      return CollectionUtility.empty((Set<?>)value);
    else if (value instanceof Map)
      return CollectionUtility.empty((Map<?, ?>)value);
    else if (value instanceof List)
      return CollectionUtility.empty((List<?>)value);
    else if (value instanceof Collection)
      return CollectionUtility.empty((Collection<?>)value);
    else if (value.getClass().isArray())
      return Array.getLength(value) == 0;
    else
      return value.toString() == null || value.toString().isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Returns the class object associated with the given class name, using the
   ** context class loader and if that fails the defining class loader of the
   ** current class.
   **
   ** @param  <T>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  className          the fully qualified class name of the class
   **                            for which an instance needs to be created.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the class object associated with the given
   **                            class name.
   **                            <br>
   **                            Possible object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   ** @throws IllegalStateException if the class cannot be loaded.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> Optional<Class<T>> find(final String className) {
    try {
      return Optional.of(clazz(className));
    }
    catch (Exception ignore) {
      // intentionally left blank
      logger.trace(category, "find", SystemBundle.string(SystemError.INTROSPECT_EXCEPTION_IGNORE, ignore.getLocalizedMessage()));
      return Optional.ofNullable(null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clazz
  /**
   ** Returns the class object associated with the given class name, using the
   ** context class loader and if that fails the defining class loader of the
   ** current class.
   **
   ** @param  <T>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  className          the fully qualified class name of the class
   **                            for which an instance needs to be created.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the class object associated with the given
   **                            class name.
   **                            <br>
   **                            Possible object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   ** @throws IllegalStateException if the class cannot be loaded.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> Class<T> clazz(final String className) {
    try {
      return (Class<T>)(Class.forName(className, true, Thread.currentThread().getContextClassLoader()));
    }
    catch (Exception e) {
      try {
        return (Class<T>)Class.forName(className);
      }
      catch (Exception ignore) {
        // intentionally left blank
        logger.trace(category, "clazz", SystemBundle.string(SystemError.INTROSPECT_EXCEPTION_IGNORE, ignore.getLocalizedMessage()));
        Optional.ofNullable(null);        
      }
      throw new IllegalStateException(SystemBundle.format(SystemError.ABORT, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignable
  /**
   ** Returns <code>true</code> if an instance of the given class could also be
   ** an instance of one of the given classes.
   ** <br>
   ** Basically this method calls @link{java.lang.Class#isAssignableFrom(Class)}
   ** and therefore only acts as an alias.
   **
   ** @param  clazz              {@link Class} to be checked if it could also be
   **                            an instance of one of the given classes.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  type               the collection of classes to be tested.
   **                            <br>
   **                            Allowed object is array of {@link Class} of any
   **                            type.
   **
   ** @return                    <code>true</code> if the given class is
   **                            assignable to one of the given type; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>
   */
  public static boolean assignable(final Class<?> clazz, final Class<?>... type) {
    for (Class<?> other : type) {
      if (clazz == null ? other == null : other.isAssignableFrom(clazz))
        return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classAnnotation
  /**
   ** Return the collection of class level {@link Annotation}.
   **
   ** @param  clazz              the class the {@link Annotation}s should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the class level {@link Annotation}s, if any.
   **                            <br>
   **                            Possible object is array of {@link Annotation}.
   */
  public static Annotation[] classAnnotation(final Class<?> clazz) {
    return clazz.getAnnotations();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   methodAnnotation
  /**
   ** Return the collection of method level {@link Annotation}.
   **
   ** @param  clazz              the class the {@link Annotation}s should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  name               the class the {@link Annotation}s should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the method level {@link Annotation}s, if any.
   **                            <br>
   **                            Possible object is array of {@link Annotation}.
   */
  public static Annotation[] methodAnnotation(final Class<?> clazz, final String name) {
    final Optional<Method> method = method(clazz, name);
    return method.isPresent() ? method.get().getAnnotations() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeAnnotation
  /**
   ** Return the collection of field level {@link Annotation}.
   **
   ** @param  clazz              the {@link Class} to introspect.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  name               the class the field the {@link Annotation}s
   **                            should be returned for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the field level {@link Annotation}s, if any.
   **                            <br>
   **                            Possible object is array of {@link Annotation}.
   */
  public static Annotation[] attributeAnnotation(final Class<?> clazz, final String name) {
    final Optional<Field> attribute = attribute(clazz, name);
    return attribute.isPresent() ? attribute.get().getAnnotations() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constructor
  /**
   ** Finds a constructor based on the given parameter types and returns
   ** <code>null</code> is none is found.
   **
   ** @param  <T>                the expected class type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the class object for which the constructor is
   **                            to be found.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   ** @param  type               the desired method parameter types.
   **                            <br>
   **                            Allowed object is array of {@link Class} for
   **                            any type.
   **
   ** @return                    a constructor if one is found, null otherwise.
   **                            <br>
   **                            Possible object is {@link Optional} with a
   **                            {@link Constructor} for type <code>T</code> as
   **                            subject.
   */
  public static <T> Optional<Constructor<T>> constructor(final Class<T> clazz, final Class<?>... type) {
    try {
      return Optional.of(clazz.getConstructor(type));
    }
    catch (Exception ignore) {
      // intentionally left blank
      logger.trace(category, "constructor", "Ignoring thrown exception; the sole intent is to return null instead.");
      return Optional.ofNullable(null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Finds a method based on the method name, amount of parameters and limited
   ** typing and returns <code>null</code> is none is found.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This supports overloading, but a limited one. Given an actual parameter of
   ** type Long, this will select a method accepting Number when the choice is
   ** between Number and a non-compatible type like String. However, it will NOT
   ** select the best match if the choice is between Number and Long.
   **
   ** @param  base               the object in which the method is to be found.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  name               the name of the method to be found.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the method parameters.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    a method if one is found, <code>null</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is {@link Optional} with a
   **                            {@link Method} as subject.
   */
  public static Optional<Method> method(final Object base, final String name, final Object... parameter) {
    return method(base.getClass(), name, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Finds a method based on the method name, amount of parameters and limited
   ** typing and returns <code>null</code> is none is found.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This supports overloading, but a limited one. Given an actual parameter of
   ** type Long, this will select a method accepting Number when the choice is
   ** between Number and a non-compatible type like String. However, it will NOT
   ** select the best match if the choice is between Number and Long.
   **
   ** @param  base               the class object in which the method is to be
   **                            found.
   **                            <br>
   **                            Allowed object is {@link Class} for any type.
   ** @param  name               the name of the method to be found.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the method parameters.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    a method if one is found, <code>null</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is {@link Optional} with a
   **                            {@link Method} as subject.
   */
  public static Optional<Method> method(final Class<?> base, final String name, final Object... parameter) {
    final List<Method> collector = new ArrayList<>();
    for (Class<?> clazz = base; clazz != null; clazz = clazz.getSuperclass()) {
      for (Method method : clazz.getDeclaredMethods()) {
        if (method.getName().equals(name) && method.getParameterTypes().length == parameter.length && !overridden(collector, method))
          collector.add(method);
      }
    }
    // may be overloaded methods were found
    // try to find closest match
    return (collector.size() == 1) ? Optional.of(collector.get(0)) : Optional.ofNullable(closest(collector, parameter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Returns a {@link Method} object that reflects the specified declared
   ** method of the class or interface represented by {@link Class}
   ** <code>clazz</code> object. The <code>name</code>} parameter is a
   ** {@link String} that specifies the simple name of the desired method, and
   ** the <code>parameter</code> parameter is an array of {@link Class} objects
   ** that identify the method's formal parameter types, in declared order. If
   ** more than one method with the same parameter types is declared in a class,
   ** and one of these methods has a return type that is more specific than any
   ** of the others, that method is returned; otherwise one of the methods is
   ** chosen arbitrarily.
   ** <p>
   ** If the {@link Class} <code>clazz</code> object represents an array type,
   ** then this method does not find the {@code clone()} method.
   **
   ** @param  clazz              the {@link Class} to introspect.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  name               the name of the method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameter array.
   **                            <br>
   **                            Allowed object is array of {@link Class}.
   **
   ** @return                    the {@link Method} object for the method of
   **                            class <code>clazz</code> matching the
   **                            specified name and parameters.
   **                            <br>
   **                            Possible object is {@link Method}.
   */
  public static Optional<Method> method(final Class<?> clazz, final String name, final Class<?>... parameter) {
    try {
      return Optional.of(clazz.getDeclaredMethod(name, parameter));
    }
    catch(Exception ignore) {
      // intentionally left blank
      logger.trace(category, "method", SystemBundle.string(SystemError.INTROSPECT_EXCEPTION_IGNORE, ignore.getLocalizedMessage()));
      return Optional.ofNullable(null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Finds methods having the given annotation.
   **
   ** @param  <T>                the expected annotation type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  base               the object in which the method is to be found.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  annotation         the {@link Annotation} of the methods to be
   **                            found.
   **                            <br>
   **                            Allowed object is {@link Class} for annotation
   **                            type <code>T</code>.
   **
   ** @return                    the collection of matching methods.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Method}.
   */
  public static <T extends Annotation> List<Method> method(final Object base, final Class<T> annotation) {
    return method(base.getClass(), annotation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Finds methods having the given annotation.
   **
   ** @param  <T>                the expected annotation type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  base               the class object in which the method is to be
   **                            found.
   **                            <br>
   **                            Allowed object is {@link Class} for any type.
   ** @param  annotation         the {@link Annotation} of the methods to be
   **                            found.
   **                            <br>
   **                            Allowed object is {@link Class} for annotation
   **                            type <code>T</code>.
   **
   ** @return                    the collection of matching methods.
   **                            <br>
   **                            possible object is {@link List} where each
   **                            element is of type {@link Method}.
   */
  public static <T extends Annotation> List<Method> method(final Class<?> base, final Class<T> annotation) {
    final List<Method> collector = new ArrayList<>();
    for (Class<?> clazz = base; clazz != null; clazz = clazz.getSuperclass()) {
      for (Method method : clazz.getDeclaredMethods()) {
        if (method.isAnnotationPresent(annotation) && !overridden(collector, method)) {
          collector.add(method);
        }
      }
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeType
  /**
   ** Returns the type of a class attribute based on the field name if it exists
   ** within the class.
   ** <br>
   ** Calling this method is equal to calling #
   ** {@link Discovery#attribute(java.lang.Class, java.lang.String)} with a
   ** <code>null</code> check and finally return the type via
   ** {@link Field#getType()}.
   **
   ** @param  base               the object in which the attribute is to be
   **                            found.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  name               the name of the attribute to be found.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the type of the field if it can be found,
   **                            otherwise <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   */
  public static Class<?> attributeType(final Class<?> base, final String name) {
    final Optional<Field> field =  attribute(base, name);
    return field.isPresent() ? field.get().getType() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Finds a class attribute based on the field name.
   ** <br>
   ** This method traverses through the super classes of the given class and
   ** tries to find the field as declared field within these classes. When the
   ** object class is reached the traversing stops. If the field can not be
   ** found, <code>null</code> is returned.
   **
   ** @param  base               the object in which the attribute is to be
   **                            found.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  name               the name of the attribute to be found.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the attribute with the given field name if the
   **                            field can be found, otherwise
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Optional} with a
   **                            {@link Field} as subject.
   */
  public static Optional<Field> attribute(final Object base, final String name) {
    return attribute(base != null ? base.getClass() : null, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Finds an class attribute based on the field name.
   **
   ** @param  clazz              the class object for which the field is to be
   **                            found.
   **                            <br>
   **                            Allowed object is {@link Class} for any type.
   ** @param  name               the name of the attribute to be found.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the attribute with the given field name if the
   **                            field can be found, otherwise
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Optional} with a
   **                            {@link Field} as subject.
   */
  public static Optional<Field> attribute(final Class<?> clazz, final String name) {
    for (Class<?> cursor = clazz; cursor != null; cursor = cursor.getSuperclass()) {
      for (Field field : cursor.getDeclaredFields()) {
        if (field.getName().equals(name))
          return Optional.of(field);
      }
    }
    return Optional.empty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   access
  /**
   ** Returns the value of the field of the given instance on the given field
   ** name.
   **
   ** @param  <T>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  instance           the instance to access the given field on.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  name               the name of the field to be accessed on the
   **                            given instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of the field of the given instance on
   **                            the given field name.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   ** @throws IllegalStateException If the field cannot be accessed.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T access(final Object instance, final String name) {
    try {
      final Field field = attribute(instance, name).orElseThrow(NoSuchFieldException::new);
      return access(instance, field);
    }
    catch (Exception e) {
      throw new IllegalStateException(SystemBundle.format(SystemError.ABORT, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   access
  /**
   ** Returns the value of the field of the given instance on the given field.
   **
   ** @param  <T>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  instance           the instance to access the given field on.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  field              the field to be accessed on the given instance.
   **                            <br>
   **                            Allowed object is {@link Field}.
   **
   ** @return                    the value of the field of the given instance on
   **                            the given field.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   ** @throws IllegalStateException If the field cannot be accessed.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T access(final Object instance, final Field field) {
    try {
      // ensure that the field is accessible regardless which modifier the
      // field is assinged to
      field.setAccessible(true);
      return (T)field.get(instance);
    }
    catch (Exception e) {
      throw new IllegalStateException(SystemBundle.format(SystemError.ABORT, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies the value of the field of the given instance on the given field
   ** name with the given value.
   **
   ** @param  <T>                the expected field type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  instance           the instance to access the given field on.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  name               the name of the field to be accessed on the
   **                            given instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the new value of the field of the given
   **                            instance on the given field name.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    rhe old value of the field of the given
   **                            instance on the given field name.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   ** @throws IllegalStateException If the field cannot be modified.
   */
  public static <T> T modify(final Object instance, final String name, final T value) {
    try {
      final Field field = attribute(instance, name).orElseThrow(NoSuchFieldException::new);
      return modify(instance, field, value);
    }
    catch (Exception e) {
      throw new IllegalStateException(SystemBundle.format(SystemError.ABORT, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies the value of the given field of the given instance with the
   ** given value.
   **
   ** @param  <T>                the expected field type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  instance           the instance to access the given field on.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  field              the field to be accessed on the given instance.
   **                            <br>
   **                            Allowed object is {@link Field}.
   ** @param  value              the new value of the field of the given
   **                            instance on the given field name.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    rhe old value of the field of the given
   **                            instance on the given field name.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   ** @throws IllegalStateException If the field cannot be modified.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T modify(Object instance, Field field, T value) {
    try {
      // ensure that the field is accessible regardless which modifier the
      // field is assinged to
      field.setAccessible(true);
      final Object old = field.get(instance);
      field.set(instance, value);
      return (T)old;
    }
    catch (Exception e) {
      throw new IllegalStateException(SystemBundle.format(SystemError.ABORT, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setter
  /**
   ** Invoke setter method of the given instance on the given property name with
   ** the given property value and return the result.
   ** <br>
   ** If the property name is dot-separated, then it will be invoked
   ** recursively.
   **
   ** @param  bean               the bean instance to invoke the given setter
   **                            method on.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  name               the name of the property setter method to be
   **                            invoked on the given instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the property value to be set.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @throws IllegalStateException if the setter method cannot be invoked.
   */
  public static void setter(final Object bean, final String name, final Object value) {
    Object target = bean;
    String item = name;
    int recurse = name.lastIndexOf('.');
    if (recurse > 0) {
      target = getter(target, name.substring(0, recurse));
      item   = name.substring(recurse + 1);
    }

    final String capitalized = StringUtility.capitalizeWord(item);
    invoke(target, "set" + capitalized, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getter
  /**
   ** Invoke getter method of the given instance on the given property name and
   ** return the result.
   ** <br>
   ** If the property name is dot-separated, then it will be invoked
   ** recursively.
   **
   **
   ** @param  <T>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  bean               the bean instance to invoke the given getter
   **                            method on.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  name               the name of the property getter method to be
   **                            invoked on the given instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the desired value of given property getter
   **                            method bean, if any.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   ** @throws IllegalStateException if the getter method cannot be invoked.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T getter(final Object bean, final String name) {
    Object result = bean;
    for (String item : name.split("\\.")) {
      final String capitalized = StringUtility.capitalizeWord(item);
      Optional<Method> booleanGetter = method(result, "is" + capitalized);
      if (booleanGetter.isPresent())
        result = invoke(result, booleanGetter.get());
      else
        result = invoke(result, "get" + capitalized);
    }
    return (T)result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke a method of the given instance on the given method name with the
   ** given parameters and return the result.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The current implementation assumes for simplicity that no one of the given
   ** parameters is <code>null</code>. If one of them is still
   ** <code>null</code>, a NullPointerException will be thrown.
   **
   ** @param  <T>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  instance           the instance to invoke the given method on.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  name               the name of the method to be invoked on the
   **                            given instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the method parameters, if any.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the result of the method invocation, if any.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   ** @throws NullPointerException  if one of the given parameters is
   **                               <code>null</code>.
   ** @throws IllegalStateException if the method cannot be invoked.
   */
  public static <T> T invoke(final Object instance, final String name, final Object... parameter) {
    try {
      final Method method = method(instance, name, parameter).orElseThrow(NoSuchMethodException::new);
      return invoke(instance, method, parameter);
    }
    catch (Exception e) {
      throw new IllegalStateException(SystemBundle.format(SystemError.ABORT, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke methods of the given instance having the given annotation.
   **
   ** @param  <T>                the expected annotation type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  instance           the instance to invoke the methods having the
   **                            given annotation on.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   ** @param  annotation         the {@link Annotation} of the methods to be
   **                            invoked.
   **                            <br>
   **                            Allowed object is {@link Class} for annotation
   **                            type <code>T</code>.
   **
   ** @throws IllegalStateException if the method cannot be invoked.
   */
  public static <T extends Annotation> void invoke(final Object instance, final Class<T> annotation) {
    for (Method method : method(instance, annotation)) {
      try {
        invoke(instance, method);
      }
      catch (Exception e) {
        throw new IllegalStateException(SystemBundle.format(SystemError.ABORT, e.getLocalizedMessage()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke a method of the given instance on the given method name with the
   ** given parameters and return the result.
   **
   ** @param  <T>                the expected return type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  instance           the instance to invoke the given method on.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  method             the method to be invoked on the given instance.
   **                            <br>
   **                            Allowed object is {@link Method}.
   ** @param  parameter          the method parameters, if any.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the result of the method invocation, if any.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   ** @throws IllegalStateException if the method cannot be invoked.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T invoke(final Object instance, final Method method, final Object... parameter) {
    try {
      // ensure that the method is accessible regardless which modifier is
      // declared on
      method.setAccessible(true);
      return (T)method.invoke(instance, parameter);
    }
    catch (Exception e) {
      throw new IllegalStateException(SystemBundle.format(SystemError.ABORT, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   overridden
  private static boolean overridden(final List<Method> visited, final Method method) {
    for (Method cursor : visited) {
      if (Arrays.equals(cursor.getParameterTypes(), method.getParameterTypes())) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closest
  private static Method closest(final List<Method> method, final Object... parameter) {
    for (Method cursor : method) {
      final Class<?>[] type = cursor.getParameterTypes();
      boolean    match = true;
      for (int i = 0; i < parameter.length; i++) {
        if (!type[i].isInstance(parameter[i])) {
          match = false;
          break;
        }
      }
      // if all type parameters were expected and for none of them the actual
      // parameter was NOT an instance, there's a match.
      if (match)
        return cursor;
      // else, at least one parameter was not an instance
      // go ahead a test then next methods
    }
    return null;
  }
}