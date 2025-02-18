/*
                         Copyright © 2023 Red.Security

    Licensed under the MIT License (the "License"); you may not use this file
    except in compliance with the License. You may obtain a copy of the License
    at

                      https://opensource.org/licenses/MIT

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to
    deal in the Software without restriction, including without limitation the
    rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
    sell copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
    IN THE SOFTWARE.

    ----------------------------------------------------------------------------

    System      :   Platform Service Extension
    Subsystem   :   Common Shared Utility Library

    File        :   ReflectionUtility.java

    Compiler    :   Java Developer Kit 8 (JDK8)

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    ReflectionUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-17  DSteding    First release version
*/

package redsecurity.platform.core.utility;

import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.InvocationTargetException;

import java.util.Objects;
import java.util.Optional;

import java.security.PrivilegedAction;
import java.security.AccessController;

import redsecurity.platform.core.SystemBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class ReflectionUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous methods for Java reflection. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ReflectionUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The package separator character '.' */
  private static final char PACKAGE_SEPARATOR_CHAR     = '.';

  /** The inner class separator character '$' */
  private static final char INNER_CLASS_SEPARATOR_CHAR = '$';

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Clazz
  // ~~~~~~~~ ~~~~~ ~~~~~
  /**
   ** Miscellaneous methods for Java reflection regarding {@link Class}es.
   */
  public static abstract class Clazz {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Clazz</code>.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Clazz()" and enforces use of the public method below.
     */
    private Clazz() {
      // should never be instantiated
      throw new AssertionError();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: packageName
    /**
     ** Returns the package name without the qualified class name.
     **
     ** @param  clazz            the class to get the package name for.
     **
     ** @return                  the package name of the class without the class
     **                          name.
     **
     ** @throws IllegalArgumentException if the class is null
     */
    public static String packageName(final Class<?> clazz) {
      return packageName(clazz.getName());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: packageName
    /**
     ** Returns the package name without the qualified class name.
     **
     ** @param  className        the className to get the package name for.
     **
     ** @return                  the package name of the class without the class
     **                          name.
     */
    public static String packageName(final String className) {
      int lastPeriod = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
      return (lastPeriod == -1) ? "" : className.substring(0, lastPeriod);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: canonicalName
    /**
     ** Returns the canonical name of the specified class as defined by the Java
     ** Language Specification.
     ** <br>
     ** Returns <code>null</code> if the underlying class does not have a
     ** canonical name (i.e., if it is a local or anonymous class or an array
     ** whose component type does not have a canonical name).
     **
     ** @param  clazz            the class to get the canonical name for.
    **
     ** @return                  the canonical name of the specified class if it
     **                          exists, and <code>null</code> otherwise.
     */
    public static String canonicalName(final Class<?> clazz) {
      return clazz.getCanonicalName();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: shortName
    /**
     ** Returns the class name without the qualified package name.
     **
     ** @param  clazz            the class to get the short name for
     **
     ** @return                  the class name of the class without the package
     **                          name.
     */
    public static String shortName(final Class<?> clazz) {
      int          lastDot   = 0;
      final char[] charArray = clazz.getName().toCharArray();
      for (int i = 0; i < charArray.length; i++) {
        if (charArray[i] == PACKAGE_SEPARATOR_CHAR)
          lastDot = i + 1;
        else if (charArray[i] == INNER_CLASS_SEPARATOR_CHAR)
          charArray[i] = PACKAGE_SEPARATOR_CHAR;
      }
      return new String(charArray, lastDot, charArray.length - lastDot);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: forName
    /**
     ** Returns the {@link Class} from given class name.
     **
     ** @param  <T>              the expected class type.
     ** @param  name             the fullqualified class name.
     ** @param  loader           the class loader to use, if <code>null</code>
     **                          then the defining class loader of this class
     **                          will be utilized.
     **
     ** @return                  the privileged action to obtain desired
     **                          {@link Class}. The action could return
     **                          <code>null</code> if the class cannot be found.
     */
    public static <T> Class<T> forName(final String name, final ClassLoader loader) {
      return AccessController.doPrivileged(ReflectionUtility.clazz(name, loader));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: constructor
    /**
     ** Returns privileged action to obtain the default constructor of given
     ** class.
     **
     ** @param  <T>              the type of the class instance to lookup the
     **                          constructor
     ** @param  clazz            the {@link Class} to get constructor from.
     **                          <code>T</code>.
     ** @param  required         <code>true</code>, throws an exception if the
     **                          default constructor is missing; if
     **                          <code>false</code>, returns <code>null</code>
     **                          in that case.
     **
     ** @return                  the constructor of the class, or
     **                          <code>null</code>; depending on
     **                          <code>required</code>.
     **
     ** @throws RuntimeException     if <code>clazz</code> is <code>null</code>.
     ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
     */
    public static <T> Constructor<T> constructor(final Class<T> clazz, final boolean required){
      return AccessController.doPrivileged(ReflectionUtility.constructor(clazz, required));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: constructor
    /**
     ** Returns the declared constructors of given {@link Class}.
     **
     ** @param  clazz              the class to obtain the constructors.
     **
     ** @return                    the constructors of {@link Class}.
     **
     ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
     */
    @SuppressWarnings("cast")
    public static Constructor<?>[] constructor(final Class<?> clazz) {
      return AccessController.doPrivileged(ReflectionUtility.constructor(clazz));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: clazz
    /**
     ** Returns the class type by runtime {@link Type}.
     ** <p>
     ** Applicable only for {@link Class}es, {@link ParameterizedType}s and
     ** {@link GenericArrayType}s.
     ** <p>
     ** An RuntimeException is thrown if raw type cannot be resolved.
     **
     ** @param  type             the runtime {@link Type} to get class metadata
     **                          from.
     **                          <br>
     **                          Must not be <code>null</code>.
     **
     ** @return                  the {@link Class} metadata for
     **                          <code>type</code>.
     */
    public static Class<?> clazz(final Type type) {
      // TODO: it compiles but JDeveloper complains: Execptions not handled: X
      return clazzOptional(type).orElseThrow(() -> new RuntimeException(SystemBundle.RESOURCE.string(SystemBundle.TYPE_RESOLVE, type)));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: clazzOptional
    /**
     ** Returns the class type by runtime {@link Type}.
     ** <p>
     ** Applicable only for {@link Class}es, {@link ParameterizedType}s and
     ** {@link GenericArrayType}s.
     ** <p>
     ** An empty {@link Optional} is returned if raw type cannot be resolved.
     **
     ** @param  type             the runtime {@link Type} to get class metadata
     **                          from.
     **                          <br>
     **                          Must not be <code>null</code>.
     **
     ** @return                  the {@link Class} metadata for
     **                          <code>type</code>.
     */
    @SuppressWarnings("cast")
    public static Optional<Class<?>> clazzOptional(final Type type) {
      if (type instanceof Class) {
        return Optional.of((Class<?>)type);
      }
      else if (type instanceof ParameterizedType) {
        return Optional.of((Class<?>)((ParameterizedType)type).getRawType());
      }
      else if (type instanceof GenericArrayType) {
        return Optional.of(((GenericArrayType)type).getClass());
      }
      else if (type instanceof TypeVariable) {
        final TypeVariable<?> variable = TypeVariable.class.cast(type);
        if (Objects.nonNull(variable.getBounds())) {
          Optional<Class<?>> specialized = Optional.empty();
          for (Type bound : variable.getBounds()) {
            final Optional<Class<?>> clazz = clazzOptional(bound);
            if (clazz.isPresent() && !Object.class.equals(clazz.get())) {
              if (!specialized.isPresent() || specialized.get().isAssignableFrom(clazz.get())) {
                specialized = Optional.of(clazz.get());
              }
            }
          }
          return specialized;
        }
      }
      return Optional.empty();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: constructor
    /**
     ** Returns the declared constructor of given {@link Class} with given
     ** parameters.
     **
     ** @param  clazz            the class to obtain the constructor.
     ** @param  parameter        the constructor parameters.
     **
     ** @return                  the the constructor or <code>null</code>, when
     **                          constructor with given parameters is not found.
     **
     ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
     */
    public static Constructor<?> constructor(final Class<?> clazz, final Class<?>... parameter) {
      return AccessController.doPrivileged(ReflectionUtility.constructor(clazz, parameter));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   instance
    /**
     ** Factory method to create {@link Class} instance with no-arg constructor.
     **
     ** @param  <T>              the type of the {@link Class} instance to
     **                          create,
     ** @param  constructor      the with no-arg constructor of a {@link Class}.
     **                          <br>
     **                          Must <b>not</b> be <code>null</code>.
     **
     ** @return                  the created {@link Class} instance.
     **
     ** @throws RuntimeException     if the class instance couldn't be created.
     ** @throws NullPointerException if <code>constructor</code> is
     **                              <code>null</code>.
     */
    public static <T> T instance(final Constructor<T> constructor)
      throws RuntimeException {

      try {
        return Objects.requireNonNull(constructor).newInstance();
      }
      catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(SystemBundle.RESOURCE.string(SystemBundle.CLASS_NOACCESS, constructor.getDeclaringClass().getName()));
      }
      catch (InstantiationException e) {
        throw new RuntimeException(SystemBundle.RESOURCE.string(SystemBundle.CLASS_NOTCREATE, constructor.getDeclaringClass().getName()));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: valueOf
    /**
     ** Returns the static valueOf(String) method.
     **
     ** @param  clazz            the class to obtain the method.
     **
     ** @return                  the privileged action to get the method.
     **                          The action could return <code>null</code> if
     **                          the method is not present.
     */
    public static Method valueOf(final Class<?> clazz) {
      return AccessController.doPrivileged(ReflectionUtility.valueOf(clazz));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ReflectionUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ReflectionUtility()" and enforces use of the public method below.
   */
  private ReflectionUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOf
  /**
   ** Returns privileged action to obtain the static valueOf(String) method.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class to obtain the method.
   **
   ** @return                    the privileged action to get the method.
   **                            The action could return <code>null</code> if
   **                            the method is not present.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("unchecked")
  public static PrivilegedAction<Method> valueOf(final Class<?> clazz) {
    return method(clazz, "valueOf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Returns privileged action to get the static fromString(String) method.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class for which to get the method.
   **
   ** @return                    the privileged action to get the method.
   **                            The action could return <code>null</code> if
   **                            the method is not present.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("unchecked")
  public static PrivilegedAction<Method> from(final Class<?> clazz) {
    return method(clazz, "from");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clazz
  /**
   ** Returns privileged action to obtain {@link Class} from given class name.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  <T>                the expected class type.
   ** @param  name               the fullqualified class name.
   ** @param  loader             the class loader to use, if <code>null</code>
   **                            then the defining class loader of this class
   **                            will be utilized.
   **
   ** @return                    the privileged action to obtain desired
   **                            {@link Class}. The action could return
   **                            <code>null</code> if the class cannot be found.
   **
   **
   ** @throws ClassCastException    if <code>T</code> is of wrong type.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> PrivilegedAction<Class<T>> clazz(final String name, final ClassLoader loader) {
    return new PrivilegedAction<Class<T>>() {
      @Override
      public Class<T> run() {
        if (loader != null) {
          try {
            return (Class<T>)Class.forName(name, false, loader);
          }
          catch (final ClassNotFoundException e) {
            System.err.println(SystemBundle.RESOURCE.string(SystemBundle.CLASS_NOTFOUND, name, loader.getClass().getName()));
          }
        }
        try {
          return (Class<T>)Class.forName(name);
        }
        catch (final ClassNotFoundException e) {
          // intentionally ignored
          System.err.println(SystemBundle.RESOURCE.string(SystemBundle.CLASS_NOTFOUND, name, "current"));
        }
        return null;
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns privileged action to obtain fields declared on given class.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class for which to get the declared fields.
   **
   ** @return                    the privileged action to obtain fields declared
   **                            on the <code>clazz</code> class.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static PrivilegedAction<Field[]> attribute(final Class<?> clazz) {
    return new PrivilegedAction<Field[]>() {
      @Override
      public Field[] run() {
        return clazz.getDeclaredFields();
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constructor
  /**
   ** Returns privileged action to obtain the default constructor of given
   ** class.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  <T>                the java type the constructor.
   ** @param  clazz              the class to obtain the constructor.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  required           <code>true</code>, throws an exception if the
   **                            default constructor is missing; if
   **                            <code>false</code>, returns <code>null</code>
   **                            in that case.
   **
   ** @return                    the privileged action to get the default
   **                            constructor.
   **
   ** @throws RuntimeException   if a matching method is not found.
   ** @throws SecurityException  if access to the constructor is denied by
   **                            security manager.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static <T> PrivilegedAction<Constructor<T>> constructor(final Class<T> clazz, final boolean required) {
    return (PrivilegedAction<Constructor<T>>)() -> {
      try {
        return Objects.requireNonNull(clazz).getDeclaredConstructor();
      }
      catch (NoSuchMethodException e) {
        if (required) {
          throw new RuntimeException(SystemBundle.RESOURCE.string(SystemBundle.CLASS_CONSTRUCTOR_DEFAULT, clazz.getName(), e));
        }
        return null;
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constructor
  /**
   ** Returns privileged action to obtain declared constructors of given class.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class to obtain the constructors.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the privileged action to get the array of
   **                            constructors.
   **
   ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("cast")
  public static PrivilegedAction<Constructor<?>[]> constructor(final Class<?> clazz) {
    return (PrivilegedAction<Constructor<?>[]>)() -> {
      return Objects.requireNonNull(clazz).getDeclaredConstructors();
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constructor
  /**
   ** Returns privileged action to obtain declared constructor of given class
   ** with given parameters.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  <T>                the expected class type.
   ** @param  clazz              the class object for which the constructor is
   **                            to be found.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  type               the desired method parameter types.
   **
   ** @return                    the privileged action to get the constructor or
   **                            <code>null</code>, when constructor with given
   **                            parameters is not found.
   **
   ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("cast")
  public static <T> PrivilegedAction<Constructor<T>> constructor(final Class<T> clazz, final Class<?>... type) {
    return (PrivilegedAction<Constructor<T>>)() -> {
      try {
        return Objects.requireNonNull(clazz).getDeclaredConstructor(type);
      }
      catch (final SecurityException e) {
        throw new RuntimeException(SystemBundle.RESOURCE.string(SystemBundle.CLASS_NOACCESS, clazz.getName()), e);
      }
      catch (NoSuchMethodException e) {
        throw new RuntimeException(SystemBundle.RESOURCE.string(SystemBundle.CLASS_METHOD, clazz.getName(), type), e);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Returns privileged action to get the static method of given name.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class for which to get the method.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  name               the name of the method to be obtained.
   **
   ** @return                    the privileged action to get the method.
   **                            The action could return <code>null</code> if
   **                            the method is not present.
   **
   ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static PrivilegedAction<Method> method(final Class<?> clazz, final String name) {
    return (PrivilegedAction<Method>)() -> {
      try {
        final Method method = Objects.requireNonNull(clazz).getDeclaredMethod(name, String.class);
        return (Modifier.isStatic(method.getModifiers()) && method.getReturnType() == clazz) ? method : null;
      }
      catch (final NoSuchMethodException e) {
        // intentionally ignored
        return null;
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Returns privileged action to obtain declared method of given class
   ** with given parameters.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  <T>                the expected class type.
   ** @param  clazz              the class object for which the constructor is
   **                            to be found.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  name               the name of the method to be obtained.
   ** @param  type               the desired method parameter types.
   **
   ** @return                    the privileged action to get the constructor or
   **                            <code>null</code>, when constructor with given
   **                            parameters is not found.
   **
   ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("cast")
  public static <T> PrivilegedAction<Method> method(final Class<T> clazz, final String name, final Class<?>... type) {
    return (PrivilegedAction<Method>)() -> {
      try {
        return Objects.requireNonNull(clazz).getDeclaredMethod(name, type);
      }
      catch (final SecurityException e) {
        throw new RuntimeException(SystemBundle.RESOURCE.string(SystemBundle.METHOD_NOACCESS, clazz.getName(), name), e);
      }
      catch (NoSuchMethodException e) {
        throw new RuntimeException(SystemBundle.RESOURCE.string(SystemBundle.METHOD_PARAMETER, clazz.getName(), name, type), e);
      }
    };
  }
}