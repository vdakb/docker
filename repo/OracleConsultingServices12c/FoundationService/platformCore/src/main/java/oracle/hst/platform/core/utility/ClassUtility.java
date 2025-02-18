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

    File        :   ClassUtility.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ClassUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.utility;

////////////////////////////////////////////////////////////////////////////////
// abstract class ClassUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Miscellaneous class utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ClassUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The package separator character '.' */
  private static final char  PACKAGE_SEPARATOR_CHAR     = '.';

  /** The inner class separator character '$' */
  private static final char  INNER_CLASS_SEPARATOR_CHAR = '$';

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ClassUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ClassUtility()" and enforces use of the public method below.
   */
  private ClassUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packageName
  /**
   ** Returns the package name without the qualified class name.
   **
   ** @param  clazz              the class to get the package name for.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the package name of the class without the class
   **                            name.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if the class is null
   */
  public static String packageName(final Class<?> clazz) {
    return packageName(clazz.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packageName
  /**
   ** Returns the package name without the qualified class name.
   **
   ** @param  className          the className to get the package name for.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the package name of the class without the class
   **                            name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String packageName(final String className) {
    return ReflectionUtility.Clazz.packageName(className);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canonicalName
  /**
   ** Returns the canonical name of the specified object as defined by the Java
   ** Language Specification.
   ** <br>
   ** Returns <code>null</code> if the underlying object class does not have a
   ** canonical name (i.e., if it is a local or anonymous class or an array
   ** whose component type does not have a canonical name).
   **
   ** @param  object             the object to get the canonical name for.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the canonical name of the specified object if
   **                            it exists, and <code>null</code> otherwise.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String canonicalName(final Object object) {
    return canonicalName(object.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canonicalName
  /**
   ** Returns the canonical name of the specified class as defined by the Java
   ** Language Specification.
   ** <br>
   ** Returns <code>null</code> if the underlying class does not have a
   ** canonical name (i.e., if it is a local or anonymous class or an array
   ** whose component type does not have a canonical name).
   **
   ** @param  clazz              the class to get the canonical name for.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the canonical name of the specified class if it
   **                            exists, and <code>null</code> otherwise.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String canonicalName(final Class<?> clazz) {
    return ReflectionUtility.Clazz.canonicalName(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shortName
  /**
   ** Returns the class name without the qualified package name for the
   ** specified object.
   **
   ** @param  object             the object to get the short class name for.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the class name of the class without the package
   **                            name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String shortName(final Object object) {
    return shortName(object.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shortName
  /**
   ** Returns the class name without the qualified package name.
   **
   ** @param  clazz              the class to get the short name for
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the class name of the class without the package
   **                            name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String shortName(final Class<?> clazz) {
    return shortName(clazz.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shortName
  /**
   ** Returns the class name without the qualified package name.
   **
   ** @param  className          the className to get the short name for
   **
   ** @return                    the class name of the class without the package
   **                            name
   ** @throws IllegalArgumentException if the className is empty
   */
  public static String shortName(final String className) {
    char[] charArray = className.toCharArray();
    int lastDot = 0;
    for (int i = 0; i < charArray.length; i++) {
      if (charArray[i] == PACKAGE_SEPARATOR_CHAR)
        lastDot = i + 1;
      else if (charArray[i] == INNER_CLASS_SEPARATOR_CHAR)
        charArray[i] = PACKAGE_SEPARATOR_CHAR;
    }
    return new String(charArray, lastDot, charArray.length - lastDot);
  }
}