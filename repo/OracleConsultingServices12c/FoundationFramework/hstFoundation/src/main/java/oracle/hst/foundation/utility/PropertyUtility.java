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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   PropertyUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PropertyUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import java.util.Map;
import java.util.Properties;

import java.security.AccessController;
import java.security.PrivilegedAction;

////////////////////////////////////////////////////////////////////////////////
// abstract class PropertyUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Helper class containing convenience methods for reading system properties.
 ** Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class PropertyUtility {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PropertyUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new PropertyUtility()" and enforces use of the public method below.
   */
  private PropertyUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperties
  /**
   ** Returns system properties.
   ** <p>
   ** This method delegates to {@link System#getProperties()} while running it
   ** in a privileged code block.
   **
   ** @return                    the privileged action to obtain system
   **                            properties.
   */
  public static PrivilegedAction<Properties> systemProperties() {
    return new PrivilegedAction<Properties>() {
      @Override
      public Properties run() {
        return System.getProperties();
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperty
  /**
   ** Returns system property.
   ** <p>
   ** This method delegates to {@link System#getProperty(String)} while running
   ** it in a privileged code block.
   **
   ** @param  name               the system property name.
   **
   ** @return                    the privileged action to obtain system property
   **                            value that will return <code>null</code> if
   **                            there's no such system property.
   */
  public static PrivilegedAction<String> systemProperty(final String name) {
    return new PrivilegedAction<String>() {
      @Override
      public String run() {
        return System.getProperty(name);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperty
  /**
   ** Returns system property.
   ** <p>
   ** This method delegates to {@link System#getProperty(String)} while running
   ** it in a privileged code block.
   **
   ** @param  name               the system property name.
   ** @param  defaultValue       the default property value.
   **
   ** @return                    the privileged action to obtain system property
   **                            value that will return the default value if
   **                            there's no such system property.
   */
  public static PrivilegedAction<String> systemProperty(final String name, final String defaultValue) {
    return new PrivilegedAction<String>() {
      @Override
      public String run() {
        return System.getProperty(name, defaultValue);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the value of the property with a given name converted to
   ** <code>boolean</code>.
   ** <p>
   ** Returns <code>false</code> if the value is not convertible.
   **
   ** @param  properties         the name-value map of properties.
   ** @param  name               the property name.
   **
   ** @return                    <code>boolean</code> property value or
   **                            <code>false</code> if the property is not
   **                            convertible.
   */
  public static boolean property(final Map<String, Object> properties, final String name) {
    return properties.containsKey(name) && property(properties.get(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the value of the property converted to <code>boolean</code>.
   ** <p>
   ** Returns <code>false</code> if the value is not convertible.
   **
   ** @param  value              the property value.
   **
   ** @return                    <code>boolean</code> property value or
   **                            <code>false</code> if the property is not
   **                            convertible.
   */
  public static boolean property(final Object value) {
    return (value instanceof Boolean) ? Boolean.class.cast(value) : value != null && Boolean.parseBoolean(value.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of a specified property.
   ** <p>
   ** If the property is not set or the real value type is not compatible with
   ** <code>defaultValue</code> type, the specified <code>defaultValue</code> is
   ** returned. Calling this method is equivalent to calling
   ** <code>PropertyUtility.value(properties, name, defaultValue, (Class&lt;T&gt;)defaultValue.getClass())</code>.
   **
   ** @param  <T>                the extected type of the property value.
   ** @param  properties         the {@link Map} of properties to get the
   **                            property value from.
   ** @param  name               the name of the property.
   ** @param  defaultValue       the default value if property is not
   **                            registered.
   **
   ** @return                    the value of the property mapped at
   **                            <code>name</code> in the specified property
   **                            {@link Map} or <code>defaultValue</code> if
   **                            there is no value mapped at name or the value
   **                            type is not compatible with the
   **                            <code>defaultValue</code> type.
   */
  public static <T> T value(final Map<String, ?> properties, final String name, final T defaultValue) {
    return value(properties, name, defaultValue, (Class<T>)defaultValue.getClass(), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of a specified property.
   ** <p>
   ** If the property is not set or the real value type is not compatible with
   ** <code>defaultValue</code> type, the specified <code>defaultValue</code> is
   ** returned. Calling this method is equivalent to calling
   ** <code>PropertyUtility.value(properties, name, defaultValue, (Class&lt;T&gt;)defaultValue.getClass())</code>.
   **
   ** @param  <T>                the extected type of the property value.
   ** @param  properties         the {@link Map} of properties to get the
   **                            property value from.
   ** @param  name               the name of the property.
   ** @param  defaultValue       the default value if property is not
   **                            registered.
   ** @param  legacy             the legacy fallback map, where key is the
   **                            actual property name, value is the old property
   **                            name.
   **
   ** @return                    the value of the property mapped at
   **                            <code>name</code> in the specified property
   **                            {@link Map} or <code>defaultValue</code> if
   **                            there is no value mapped at name or the value
   **                            type is not compatible with the
   **                            <code>defaultValue</code> type.
   */
  public static <T> T value(final Map<String, ?> properties, final String name, final T defaultValue, final Map<String, String> legacy) {
    return value(properties, name, defaultValue, (Class<T>)defaultValue.getClass(), legacy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of a specified property.
   ** <p>
   ** If the property is not set or the real value type is not compatible with
   ** <code>defaultValue</code> type, the specified <code>defaultValue</code> is
   ** returned. Calling this method is equivalent to calling
   ** <code>PropertyUtility.value(properties, name, defaultValue, (Class&lt;T&gt;) defaultValue.getClass())</code>.
   **
   ** @param  <T>                the extected type of the property value.
   ** @param  properties         the {@link Map} of properties to get the
   **                            property value from.
   ** @param  name               the name of the property.
   ** @param  defaultValue       the default value if property is not
   **                            registered.
   ** @param  type               the Java {@link Class} type to retrieve the
   **                            value as.
   ** @param  legacy             the legacy fallback map, where key is the
   **                            actual property name, value is the old property
   **                            name.
   **
   ** @return                    the value of the property mapped at
   **                            <code>name</code> in the specified property
   **                            {@link Map} or <code>defaultValue</code> if
   **                            there is no value mapped at name or the value
   **                            type is not compatible with the
   **                            <code>defaultValue</code> type.
   */
  public static <T> T value(final Map<String, ?> properties, final String name, final T defaultValue, final Class<T> type, final Map<String, String> legacy) {
    final T value = value(properties, name, type, legacy);
    return (value == null) ? defaultValue : value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of a specified property.
   ** <p>
   ** If the property is not set or the real value type is not compatible with
   ** the specified value type, returns <code>null</code>.
   **
   ** @param  <T>                the extected type of the property value.
   ** @param  properties         the {@link Map} of properties to get the
   **                            property value from.
   ** @param  name               the name of the property.
   ** @param  type               the Java {@link Class} type to retrieve the
   **                            value as.
   ** @param  legacy             the legacy fallback map, where key is the
   **                            actual property name, value is the old property
   **                            name.
   **
   ** @return                    the value of the property mapped at
   **                            <code>name</code> in the specified property
   **                            {@link Map} or <code>null</code> if
   **                            there is no value mapped at name.
   */
  public static <T> T value(final Map<String, ?> properties, final String name, final Class<T> type, final Map<String, String> legacy) {
    Object value = properties.get(name);
    if (value == null) {
      value = legacyValue(properties, legacy, name);
    }
    if (value == null) {
      return null;
    }
    return convert(value, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Convert <code>Object</code> value to a value of the specified class type.
   **
   ** @param  <T>                the extected type of the conversion.
   ** @param  value              the <code>Object</code> value to convert.
   ** @param  type               the conversion type.
   **
   ** @return                    the value converted to the specified class
   **                            type.
   */
  public static <T> T convert(final Object value, final Class<T> type) {
    if (!type.isInstance(value)) {
      // TODO: Move string value readers from server to common and utilize them here
      final Constructor constructor = AccessController.doPrivileged(ReflectionUtility.stringConstructor(type));
      if (constructor != null) {
        try {
          return type.cast(constructor.newInstance(value));
        }
        catch (Exception e) {
          // calling the constructor wasn't successful - ignore and try valueOf()
          ;
        }
      }
      final Method valueOf = AccessController.doPrivileged(ReflectionUtility.valueOfMethod(type));
      if (valueOf != null) {
        try {
          return type.cast(valueOf.invoke(null, value));
        }
        catch (Exception e) {
          // calling valueOf wasn't successful
          ;
        }
      }
      return null;
    }
    return type.cast(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   legacyValue
  private static Object legacyValue(final Map<String, ?> properties, final Map<String, String> legacy, final String name) {
    return (legacy == null || !legacy.containsKey(name)) ? null : properties.get(legacy.get(name));
  }
}