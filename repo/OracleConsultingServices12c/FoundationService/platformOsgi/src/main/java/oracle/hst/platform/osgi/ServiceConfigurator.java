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
    Subsystem   :   OSGI Container Interface

    File        :   ServiceConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceConfigurator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.osgi;

import java.lang.reflect.Method;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

////////////////////////////////////////////////////////////////////////////////
// class DataSourceConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Configure a java bean from a given Map of properties.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Object              subject;
  private final Map<String, Method> setters;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ServiceConfigurator</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ServiceConfigurator()" and enforces use of the public method below.
   */
  private ServiceConfigurator(final Object subject) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.subject = subject;
    this.setters = findSetters(subject);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   push
  /**
   ** Configure a java bean from a given {@link Properties}.
   **
   ** @param  subject            the bean to populate.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  config             the properties to set.
   **                            The keys in the {@link Properties} have to
   **                            match the bean property names.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   */
  public static void push(final Object subject, final Properties config) {
    final Map<String, String> map = new HashMap<>();
    for (String key : config.stringPropertyNames()) {
      map.put(key, config.getProperty(key));
    }
    push(subject, map);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   push
  /**
   ** Configure a java bean from a given {@link Map} of properties.
   **
   ** @param  subject            the bean to populate.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  config             the properties to set.
   **                            The keys in the {@link Map} have to match the
   **                            bean property names.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link String} as the value.
   */
  public static void push(final Object subject, final Map<String, String> config) {
    ServiceConfigurator configurator = new ServiceConfigurator(subject);
    for (String key : config.keySet()) {
      configurator.trySetProperty(key, config.get(key));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findSetters
  /**
   ** Evaluates the setter methods for the specified object instance.
   **
   ** @param  subject            the bean to introspect.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the mapping of method names to {@link Method}s.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Method} as the value.
   */
  private static Map<String, Method> findSetters(final Object subject) {
    Map<String, Method> setters = new HashMap<String, Method>();
    for (Method method : subject.getClass().getMethods()) {
      String name = method.getName();
      if (name.startsWith("set") && method.getParameterTypes().length == 1) {
        String key1 = name.substring(3, 4).toLowerCase() + name.substring(4);
        String key2 = name.substring(3, 4) + name.substring(4);
        setters.put(key1, method);
        setters.put(key2, method);
      }
    }
    return setters;
  }

  private void trySetProperty(final String key, final String value) {
    try {
      Method method = this.setters.get(key);
      if (method == null) {
        throw new IllegalArgumentException("No setter in " + this.subject.getClass() + " for property " + key);
      }
      Class<?> clazz = method.getParameterTypes()[0];
      if (clazz == int.class || clazz == Integer.class) {
        method.invoke(this.subject, Integer.parseInt(value));
      }
      else if (clazz == long.class || clazz == Long.class) {
        method.invoke(this.subject, Long.parseLong(value));
      }
      else if (clazz == boolean.class || clazz == Boolean.class) {
        method.invoke(this.subject, Boolean.parseBoolean(value));
      }
      else if (clazz == String.class) {
        method.invoke(this.subject, value);
      }
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Error setting property " + key + ":" + e.getMessage(), e);
    }
  }
}