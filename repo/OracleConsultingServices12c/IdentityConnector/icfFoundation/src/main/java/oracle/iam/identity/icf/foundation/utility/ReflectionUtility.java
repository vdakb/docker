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

    File        :   ReflectionUtility.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReflectionUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.AccessibleObject;

import java.security.PrivilegedAction;
import java.security.AccessController;

////////////////////////////////////////////////////////////////////////////////
// abstract class ReflectionUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Miscellaneous methods for Java reflection. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ReflectionUtility {

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
   **
   ** @return                    the declaring class of an accessible object.
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
   ** @param  name               the fullqualified class name.
   ** @param  loader             the class loader to use, if <code>null</code>
   **                            then the defining class loader of this class
   **                            will be utilized.
   **
   ** @return                    the privileged action to obtain desired
   **                            {@link Class}. The action could return
   **                            <code>null</code> if the class cannot be found.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("unchecked")
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
   **
   ** @return                    the privileged action to obtain fields declared
   **                            on the <code>clazz</code> class.
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
  // Method:   declaredConstructor
  /**
   ** Returns privileged action to obtain declared constructors of given class.
   ** <p>
   ** If run using security manager, the returned privileged action must be
   ** invoked within a doPrivileged block.
   **
   ** @param  clazz              the class to obtain the constructors.
   **
   ** @return                    the privileged action to get the array of
   **                            constructors.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static PrivilegedAction<Constructor<?>[]> declaredConstructor(final Class<?> clazz) {
    return new PrivilegedAction<Constructor<?>[]>() {
      @Override
      public Constructor<?>[] run() {
        return clazz.getDeclaredConstructors();
      }
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
   ** @param  parameter          the constructor parameters.
   **
   ** @return                    the privileged action to get the constructor or
   **                            <code>null</code>, when constructor with given
   **                            parameters is not found.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static PrivilegedAction<Constructor<?>> declaredConstructor(final Class<?> clazz, final Class<?>... parameter) {
    return new PrivilegedAction<Constructor<?>>() {
      @Override
      public Constructor<?> run() {
        try {
          return clazz.getDeclaredConstructor(parameter);
        }
        catch (NoSuchMethodException e) {
          return null;
        }
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
   **
   ** @return                    the privileged action to get the constructor.
   **                            The action could return <code>null</code> if
   **                            the constructor is not present.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  public static PrivilegedAction<Constructor> stringConstructor(final Class<?> clazz) {
    return new PrivilegedAction<Constructor>() {
      @Override
      public Constructor run() {
        try {
          return clazz.getConstructor(String.class);
        }
        catch (final SecurityException e) {
          throw e;
        }
        catch (final Exception e) {
          return null;
        }
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
   **
   ** @return                    the privileged action to get the method.
   **                            The action could return <code>null</code> if
   **                            the method is not present.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  @SuppressWarnings("unchecked")
  public static PrivilegedAction<Method> valueOfMethod(final Class<?> clazz) {
    return method(clazz, "valueOf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromStringMethod
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
  public static PrivilegedAction<Method> fromStringMethod(final Class<?> clazz) {
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
   ** @param  name               the name of the method to be obtained.
   **
   ** @return                    the privileged action to get the method.
   **                            The action could return <code>null</code> if
   **                            the method is not present.
   **
   ** @see    AccessController#doPrivileged(java.security.PrivilegedAction)
   */
  private static PrivilegedAction<Method> method(final Class<?> clazz, final String name) {
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