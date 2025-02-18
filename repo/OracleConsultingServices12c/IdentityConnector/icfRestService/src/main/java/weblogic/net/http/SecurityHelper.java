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
*/

package weblogic.net.http;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 ** Utility class to read system properties in a secured context.
 */
public class SecurityHelper {
  public SecurityHelper() {
  }

  /**
   ** Gets the system property indicated by the specified key.
   ** <p>
   ** First, if there is a security manager, its
   ** <code>checkPropertyAccess</code> method is called with the key as
   ** its argument. This may result in a SecurityException.
   ** <p>
   ** If there is no current set of system properties, a set of system
   ** properties is first created and initialized in the same manner as
   ** for the <code>getProperties</code> method.
   **
   ** @param      key            the name of the system property.
   ** @return                    the string value of the system property,
   **                            or <code>null</code> if there is no property
   **                            with that key.
   **
   ** @exception  SecurityException        if a security manager exists and its
   **                                      <code>checkPropertyAccess</code>
   **                                      method doesn't allow  access to
   **                                      the specified system property.
   ** @exception  NullPointerException     if <code>key</code> is
   **                                      <code>null</code>.
   ** @exception  IllegalArgumentException if <code>key</code> is empty.
   ** @see        java.lang.System#setProperty
   ** @see        java.lang.SecurityException
   ** @see        java.lang.SecurityManager#checkPropertyAccess(java.lang.String)
   ** @see        java.lang.System#getProperties()
   */
  public static String getSystemProperty(final String key) {
    return System.getSecurityManager() != null ? (String)AccessController.doPrivileged(new PrivilegedAction<Object>() {
      public Object run() {
        return System.getProperty(key);
      }
    }) : System.getProperty(key);
  }

  /**
   ** Gets the system property indicated by the specified key.
   ** <p>
   ** First, if there is a security manager, its
   ** <code>checkPropertyAccess</code> method is called with the
   ** <code>key</code> as its argument.
   ** <p>
   ** If there is no current set of system properties, a set of system
   ** properties is first created and initialized in the same manner as
   ** for the <code>getProperties</code> method.
   **
   ** @param     key             the name of the system property.
   ** @param     def             a default value.
   ** @return                    the string value of the system property,
   **                            or the default value if there is no property
   **                            with that key.
   **
   ** @exception SecurityException        if a security manager exists and its
   **                                     <code>checkPropertyAccess</code>
   **                                     method doesn't allow access
   **                                     to the specified system property.
   ** @exception NullPointerException     if <code>key</code> is
   **                                     <code>null</code>.
   ** @exception IllegalArgumentException if <code>key</code> is empty.
   ** @see        java.lang.System#setProperty
   ** @see        java.lang.SecurityManager#checkPropertyAccess(java.lang.String)
   ** @see        java.lang.System#getProperties()
   */
  public static String getSystemProperty(final String key, final String def) {
    return System.getSecurityManager() != null ? (String)AccessController.doPrivileged(new PrivilegedAction<Object>() {
      public Object run() {
        return System.getProperty(key, def);
      }
    }) : System.getProperty(key, def);
  }

  /**
   ** Determines the integer value of the system property with the
   ** specified name.
   **
   ** <p>The first argument is treated as the name of a system
   ** property.  System properties are accessible through the {@link
   ** java.lang.System#getProperty(java.lang.String)} method. The
   ** string value of this property is then interpreted as an integer
   ** value using the grammar supported by {@link Integer#decode decode} and
   ** an {@link Integer} object representing this value is returned.
   **
   ** <p>The second argument is the default value. An {@link Integer} object
   ** that represents the value of the second argument is returned if there
   ** is no property of the specified name, if the property does not have
   ** the correct numeric format, or if the specified name is empty or
   ** {@link null}.
   **
   ** <p>In other words, this method returns an {@link Integer} object
   ** equal to the value of:
   **
   ** <blockquote>
   **  {@code getInteger(nm, new Integer(val))}
   ** </blockquote>
   **
   ** but in practice it may be implemented in a manner such as:
   **
   ** <blockquote><pre>
   ** Integer result = getInteger(nm, null);
   ** return (result == null) ? new Integer(val) : result;
   ** </pre></blockquote>
   **
   ** to avoid the unnecessary allocation of an {@link Integer}
   ** object when the default value is not needed.
   **
   ** @param   name              property name.
   ** @param   val               default value.
   ** @return                    the {@link Integer} value of the property.
   ** @throws  SecurityException for the same reasons as
   **                           {@link System#getProperty(String) System.getProperty}
   ** @see     java.lang.System#getProperty(java.lang.String)
   ** @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
   */
  public static Integer getInteger(final String name, final int val) {
    return System.getSecurityManager() != null ? (Integer)AccessController.doPrivileged(new PrivilegedAction<Integer>() {
      public Integer run() {
        return Integer.getInteger(name, val);
      }
    }) : Integer.getInteger(name, val);
  }

  /**
   ** Returns {@code true} if and only if the system property
   ** named by the argument exists and is equal to the string
   ** {@code "true"}. (Beginning with version 1.0.2 of the
   ** Java<small><sup>TM</sup></small> platform, the test of
   ** this string is case insensitive.) A system property is accessible
   ** through {@code getProperty}, a method defined by the
   ** {@link System} class.
   ** <p>
   ** If there is no property with the specified name, or if the specified
   ** name is empty or null, then {@code false} is returned.
   **
   ** @param   name              the system property name.
   ** @return                    the boolean value of the system property.
   ** @throws  SecurityException for the same reasons as
   **          {@link System#getProperty(String) System.getProperty}
   ** @see     java.lang.System#getProperty(java.lang.String)
   ** @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
   **/
  public static Boolean getBoolean(final String name) {
    return System.getSecurityManager() != null ? (Boolean)AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
      public Boolean run() {
        return Boolean.getBoolean(name);
      }
    }) : Boolean.getBoolean(name);
  }
}
