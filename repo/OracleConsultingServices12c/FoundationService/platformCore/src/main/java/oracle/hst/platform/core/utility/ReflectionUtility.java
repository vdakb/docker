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

    File        :   ReflectionUtility.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReflectionUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.utility;

import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.InvocationTargetException;

import java.util.Objects;
import java.util.Optional;

import java.security.PrivilegedAction;
import java.security.AccessController;

import oracle.hst.platform.core.SystemError;
import oracle.hst.platform.core.SystemBundle;
import oracle.hst.platform.core.SystemException;

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
     **                          <br>
     **                          Allowed object is {@link Class} of any type.
     **
     ** @return                  the package name of the class without the class
     **                          name.
     **                          <br>
     **                          Possible object is {@link String}.
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
     **                          <br>
     **                          Allowed object is {@link Class} of any type.
     **
     ** @return                  the package name of the class without the class
     **                          name.
     **                          <br>
     **                          Possible object is {@link String}.
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
     **                          <br>
     **                          Allowed object is {@link Class} of any type.
     **
     ** @return                  the canonical name of the specified class if it
     **                          exists, and <code>null</code> otherwise.
     **                          <br>
     **                          Possible object is {@link String}.
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
     **                          <br>
     **                          Allowed object is {@link Class} of any type.
     **
     ** @return                  the class name of the class without the package
     **                          name.
     **                          <br>
     **                          Possible object is {@link String}.
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
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  name             the fullqualified class name.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  loader           the class loader to use, if <code>null</code>
     **                          then the defining class loader of this class
     **                          will be utilized.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the privileged action to obtain desired
     **                          {@link Class}. The action could return
     **                          <code>null</code> if the class cannot be found.
     **                          <br>
     **                          Possible object is {@link Class} for type
     **                          <code>T</code>.
     */
    public static <T> Class<T> forName(final String name, final ClassLoader loader) {
      return AccessController.doPrivileged(ReflectionUtility.classForName(name, loader));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: defaultConstructor
    /**
     ** Returns privileged action to obtain the default constructor of given
     ** class.
     **
     ** @param  <T>              the type of the class instance to lookup the
     **                          constructor
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  clazz            the {@link Class} to get constructor from.
     **                          <br>
     **                          Allowed object is {@link Class} for type
     **                          <code>T</code>.
     ** @param  required         <code>true</code>, throws an exception if the
     **                          default constructor is missing; if
     **                          <code>false</code>, returns <code>null</code>
     **                          in that case.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the constructor of the class, or
     **                          <code>null</code>; depending on
     **                          <code>required</code>.
     **                          <br>
     **                          Possible object is {@link Constructor} for type
     **                          <code>T</code>.
     **
     ** @throws RuntimeException     if <code>clazz</code> is <code>null</code>.
     ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
     */
    public static <T> Constructor<T> defaultConstructor(final Class<T> clazz, final boolean required){
      return AccessController.doPrivileged(ReflectionUtility.defaultConstructor(clazz, required));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: declaredConstructor
    /**
     ** Returns the declared constructors of given {@link Class}.
     **
     ** @param  clazz              the class to obtain the constructors.
     **                            <br>
     **                            Allowed object is {@link Class} for type any.
     **
     ** @return                    the constructors of {@link Class}.
     **                            <br>
     **                            Possible object is array of
     **                            {@link Constructor}.
     **
     ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
     */
    @SuppressWarnings("cast")
    public static Constructor<?>[] declaredConstructor(final Class<?> clazz) {
      return AccessController.doPrivileged(ReflectionUtility.declaredConstructor(clazz));
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
     **                          <br>
     **                          Allowed object type is {@link Type}.
     **
     ** @return                  the {@link Class} metadata for
     **                          <code>type</code>.
     **                          <br>
     **                          Possible object type is {@link Optional}
     **                          containing a {@link Class} for type any.
     */
    public static Class<?> clazz(final Type type) {
      // TODO: it compiles but JDeveloper complains: Execptions not handled: X
      return clazzOptional(type).orElseThrow(() -> new RuntimeException("Error resolving runtime type for type: " + type));
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
     **                          <br>
     **                          Allowed object type is {@link Type}.
     **
     ** @return                  the {@link Class} metadata for
     **                          <code>type</code>.
     **                          <br>
     **                          Possible object type is {@link Optional}
     **                          containing a {@link Class} for type any.
     */
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
    // Method: declaredConstructor
    /**
     ** Returns the declared constructor of given {@link Class} with given
     ** parameters.
     **
     ** @param  clazz            the class to obtain the constructor.
     **                          <br>
     **                          Allowed object is {@link Class} for type any.
     ** @param  parameter        the constructor parameters.
     **                          <br>
     **                          Allowed object is array of {@link Class} for
     **                          type any.
     **
     ** @return                  the the constructor or <code>null</code>, when
     **                          constructor with given parameters is not found.
     **                          <br>
     **                          Possible object is {@link Constructor}.
     **
     ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
     */
    public static Constructor<?> declaredConstructor(final Class<?> clazz, final Class<?>... parameter) {
      return AccessController.doPrivileged(ReflectionUtility.declaredConstructor(clazz, parameter));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   instance
    /**
     ** Factory method to create {@link Class} instance with no-arg constructor.
     **
     ** @param  <T>              the type of the {@link Class} instance to
     **                          create,
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  constructor      the with no-arg constructor of a {@link Class}.
     **                          <br>
     **                          Allowed object is {@link Constructor} for type
     **                          <code>T</code>.
     **
     ** @return                  the created {@link Class} instance.
     **                          <br>
     **                          Possible object is <code>T</code>.
     **
     ** @throws SystemException      if the class instance couldn't be created.
     ** @throws NullPointerException if <code>constructor</code> is
     **                              <code>null</code>.
     */
    public static <T> T instance(final Constructor<T> constructor)
      throws SystemException {

      try {
        return Objects.requireNonNull(constructor).newInstance();
      }
      catch (IllegalAccessException | InvocationTargetException e) {
        throw SystemException.classNoAccess(constructor.getDeclaringClass().getName());
      }
      catch (InstantiationException e) {
        throw SystemException.classNotCreated(constructor.getDeclaringClass().getName());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: valueOf
    /**
     ** Returns the static valueOf(String) method.
     **
     ** @param  clazz            the class to obtain the method.
     **                          <br>
     **                          Allowed object is {@link Class} for type any.
     **
     ** @return                  the privileged action to get the method.
     **                          The action could return <code>null</code> if
     **                          the method is not present.
     **                          <br>
     **                          Possible object is {@link Method} for type any.
     */
    public static Method valueOf(final Class<?> clazz) {
      return AccessController.doPrivileged(ReflectionUtility.valueOfMethod(clazz));
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
  // Method:   declaringClass
  /**
   ** Returns the declaring class of an accessible object.
   ** <p>
   ** Supported are {@link Method}, {@link Field} and {@link Constructor}
   ** accessible object types.
   **
   ** @param  accessible         an accessible object.
   **                            <br>
   **                            Allowed object is {@link AccessibleObject}.
   **
   ** @return                    the declaring class of an accessible object.
   **                            <br>
   **                            Possible object is {@link Class} of type any.
   **
   ** @throws IllegalArgumentException in case the type of the accessible object
   **                                  is not supported.
   */
  public static Class<?> declaringClass(final AccessibleObject accessible) {
    if (accessible instanceof Member && (accessible instanceof Field || accessible instanceof Method || accessible instanceof Constructor)) {
      return ((Member)accessible).getDeclaringClass();
    }
    else {
      throw new IllegalArgumentException("Unsupported accessible object type: " + accessible.getClass().getName());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classForName
  /**
   ** Returns privileged action to obtain {@link Class} from given class name.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  <T>                the expected class type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  name               the fullqualified class name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  loader             the class loader to use, if <code>null</code>
   **                            then the defining class loader of this class
   **                            will be utilized.
   **                            <br>
   **                            Allowed object is {@link ClassLoader}.
   **
   ** @return                    the privileged action to obtain desired
   **                            {@link Class}. The action could return
   **                            <code>null</code> if the class cannot be found.
   **                            <br>
   **                            Possible object is {@link PrivilegedAction}
   **                            wrapping the desired {@link Class} of type
   **                            <code>T</code>.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> PrivilegedAction<Class<T>> classForName(final String name, final ClassLoader loader) {
    return new PrivilegedAction<Class<T>>() {
      @Override
      public Class<T> run() {
        if (loader != null) {
          try {
            return (Class<T>)Class.forName(name, false, loader);
          }
          catch (final ClassNotFoundException e) {
            System.err.println("Unable to load class " + name + " using the supplied class loader " + loader.getClass().getName() + ".");
          }
        }
        try {
          return (Class<T>)Class.forName(name);
        }
        catch (final ClassNotFoundException e) {
          System.err.println("Unable to load class " + name + " using the current class loader.");
        }
        return null;
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   declaredFields
  /**
   ** Returns privileged action to obtain fields declared on given class.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class for which to get the declared fields.
   **                            <br>
   **                            Allowed object is {@link Class} for type any.
   **
   ** @return                    the privileged action to obtain fields declared
   **                            on the <code>clazz</code> class.
   **                            <br>
   **                            Possible object is {@link PrivilegedAction}
   **                            wrapping the desired array of {@link Field}.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static PrivilegedAction<Field[]> declaredFields(final Class<?> clazz) {
    return new PrivilegedAction<Field[]>() {
      @Override
      public Field[] run() {
        return clazz.getDeclaredFields();
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultConstructor
  /**
   ** Returns privileged action to obtain the default constructor of given
   ** class.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  <T>                the java type the constructor.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the class to obtain the constructor.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   ** @param  required           <code>true</code>, throws an exception if the
   **                            default constructor is missing; if
   **                            <code>false</code>, returns <code>null</code>
   **                            in that case.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the privileged action to get the default
   **                            constructor.
   **                            <br>
   **                            Possible object is {@link PrivilegedAction}
   **                            wrapping the desired {@link Constructor} that
   **                            takes type <code>T</code> as an argument.
   **
   ** @throws RuntimeException   if a matching method is not found.
   ** @throws SecurityException  if access to the constructor is denied by
   **                            security manager.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static <T> PrivilegedAction<Constructor<T>> defaultConstructor(final Class<T> clazz, final boolean required) {
    return (PrivilegedAction<Constructor<T>>)() -> {
      try {
        return Objects.requireNonNull(clazz).getDeclaredConstructor();
      }
      catch (NoSuchMethodException e) {
        if (required) {
          throw new RuntimeException(SystemBundle.format(SystemError.CLASSCONSTRUCTORDEFAULT, clazz.getName(), e));
        }
        return null;
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   declaredConstructor
  /**
   ** Returns privileged action to obtain declared constructors of given class.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class to obtain the constructors.
   **                            <br>
   **                            Allowed object is {@link Class} for type any.
   **
   ** @return                    the privileged action to get the array of
   **                            constructors.
   **                            <br>
   **                            Possible object is {@link PrivilegedAction}
   **                            wrapping the desired {@link Constructor}s.
   **
   ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("cast")
  public static PrivilegedAction<Constructor<?>[]> declaredConstructor(final Class<?> clazz) {
    return (PrivilegedAction<Constructor<?>[]>)() -> {
      return Objects.requireNonNull(clazz).getDeclaredConstructors();
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   declaredConstructor
  /**
   ** Returns privileged action to obtain declared constructor of given class
   ** with given parameters.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class to obtain the constructor.
   **                            <br>
   **                            Allowed object is {@link Class} for type any.
   ** @param  parameter          the constructor parameters.
   **                            <br>
   **                            Allowed object is array of {@link Class} for
   **                            type any.
   **
   ** @return                    the privileged action to get the constructor or
   **                            <code>null</code>, when constructor with given
   **                            parameters is not found.
   **                            <br>
   **                            Possible object is {@link PrivilegedAction}
   **                            wrapping the desired {@link Constructor}.
   **
   ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("cast")
  public static PrivilegedAction<Constructor<?>> declaredConstructor(final Class<?> clazz, final Class<?>... parameter) {
    return (PrivilegedAction<Constructor<?>>)() -> {
      try {
        return Objects.requireNonNull(clazz).getDeclaredConstructor(parameter);
      }
      catch (final SecurityException e) {
        throw new RuntimeException(SystemBundle.format(SystemError.CLASSNOACCESS, clazz.getName()), e);
      }
      catch (NoSuchMethodException e) {
        throw new RuntimeException(SystemBundle.format(SystemError.CLASSMETHOD, clazz.getName(), parameter), e);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringConstructor
  /**
   ** Returns privileged action to obtain constructor that has a single
   ** parameter of String.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class to obtain the constructor.
   **                            <br>
   **                            Allowed object is {@link Class} for type any.
   **
   ** @return                    the privileged action to get the constructor.
   **                            The action could return <code>null</code> if
   **                            the constructor is not present.
   **                            <br>
   **                            Possible object is {@link PrivilegedAction}
   **                            wrapping the desired {@link Constructor}.
   **
   ** @throws NullPointerException if <code>clazz</code> is <code>null</code>.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static PrivilegedAction<Constructor> stringConstructor(final Class<?> clazz) {
    return (PrivilegedAction<Constructor>)() -> {
      try {
        return Objects.requireNonNull(clazz).getConstructor(String.class);
      }
      catch (final SecurityException e) {
        throw new RuntimeException(SystemBundle.format(SystemError.CLASSNOACCESS, clazz.getName()), e);
      }
      catch (final Exception e) {
        return null;
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueOfMethod
  /**
   ** Returns privileged action to obtain the static valueOf(String) method.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class to obtain the method.
   **                            <br>
   **                            Allowed object is {@link Class} for type any.
   **
   ** @return                    the privileged action to get the method.
   **                            The action could return <code>null</code> if
   **                            the method is not present.
   **                            <br>
   **                            Possible object is {@link PrivilegedAction}
   **                            wrapping the desired {@link Method}.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("unchecked")
  public static PrivilegedAction<Method> valueOfMethod(final Class<?> clazz) {
    return method(clazz, "valueOf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromMethod
  /**
   ** Returns privileged action to get the static fromString(String) method.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class for which to get the method.
   **                            <br>
   **                            Allowed object is {@link Class} for type any.
   **
   ** @return                    the privileged action to get the method.
   **                            The action could return <code>null</code> if
   **                            the method is not present.
   **                            <br>
   **                            Possible object is {@link PrivilegedAction}
   **                            wrapping the desired {@link Method}.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("unchecked")
  public static PrivilegedAction<Method> fromMethod(final Class<?> clazz) {
    return method(clazz, "fromString");
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
   **                            Allowed object is {@link Class} for type any.
   ** @param  name               the name of the method to be obtained.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the privileged action to get the method.
   **                            The action could return <code>null</code> if
   **                            the method is not present.
   **                            <br>
   **                            Possible object is {@link PrivilegedAction}
   **                            wrapping the desired {@link Method}.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static PrivilegedAction<Method> method(final Class<?> clazz, final String name) {
    return new PrivilegedAction<Method>() {
      @Override
      public Method run() {
        try {
          final Method method = clazz.getDeclaredMethod(name, String.class);
          if (Modifier.isStatic(method.getModifiers()) && method.getReturnType() == clazz) {
            return method;
          }
          return null;
        }
        catch (final NoSuchMethodException e) {
          return null;
        }
      }
    };
  }
}