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

    File        :   ClassUtility.java

    Compiler    :   Java Developer Kit 8 (JDK8)

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    ClassUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-17  DSteding    First release version
*/

package redsecurity.platform.core.utility;

////////////////////////////////////////////////////////////////////////////////
// abstract class ClassUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Miscellaneous class utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ClassUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CLASS = "class";
  public static final String XML   = "xml";
  public static final String XSD   = "xsd";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ClassUtility</code>.
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
   **
   ** @return                    the canonical name of the specified object if
   **                            it exists, and <code>null</code> otherwise.
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
   **
   ** @return                    the canonical name of the specified class if it
   **                            exists, and <code>null</code> otherwise.
   */
  public static String canonicalName(final Class<?> clazz) {
    return clazz.getCanonicalName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unqualifiedName
  /**
   ** Returns the unqualified name (i.e., the name without package information)
   ** for the provided class.
   *
   ** @param  clazz             the class to get the unqualified name for.
   **
   ** @return                    the unqualified name of the specified class if it
   **                            exists, and <code>null</code> otherwise.
   */
  public static String unqualifiedName(final Class<?> clazz) {
    final String className = clazz.getName();
    final int    period    = className.lastIndexOf('.');
    return (period > 0) ? className.substring(period + 1) : className;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classToFile
  /**
   ** Converts a class to a file name. All periods are replaced with
   ** slashes and the '.class' extension is added.
   **
   ** @param  clazz              the class to convert.
   **
   ** @return                    a fullqualified pathname
   */
  public static String classToFile(final Class<?> clazz) {
    return classToFile(clazz, CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classToFile
  /**
   ** Converts a class to a file name. All periods are replaced with
   ** slashes and the supplied extension is added.
   **
   ** @param  clazz              the class to convert.
   ** @param  extension          the file extension to append on the name.
   **
   ** @return                    a fullqualified pathname
   */
  public static String classToFile(final Class<?> clazz, final String extension) {
    return classToFile(clazz.getName(), extension);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classToFile
  /**
   ** Converts a class name to a file name. All periods are replaced with
   ** slashes and the '.class' extension is added.
   **
   ** @param  name               the class name to convert.
   **
   ** @return                    a fullqualified pathname
   */
  public static String classToFile(final String name) {
    return classToFile(name, CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classToFile
  /**
   ** Converts a class name to a file name. All periods are replaced with
   ** slashes and the supplied extension is added.
   **
   ** @param  name               the class name to convert.
   ** @param  extension          the file extension to append on the name.
   **
   ** @return                    a fullqualified pathname
   */
  public static String classToFile(final String name, final String extension) {
    return name.replace('$', '/').concat(".").concat(extension);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classToDir
  /**
   ** Converts a class to a directory name. All periods are replaced with
   ** slashes and name of the class will be removed.
   **
   ** @param  clazz              the class to convert.
   **
   ** @return                    a relative pathname
   */
  public static String classNameToDir(final Class<?> clazz) {
    return classToDir(clazz.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNameToDir
  /**
   ** Converts a class name to a directory name. All periods are replaced with
   ** slashes and name of the class will be removed.
   **
   ** @param  name               the class name to convert.
   **
   ** @return                    a fullqualified pathname
   */
  public static String classToDir(final String name) {
    String path =  name.replace('$', '/');
    return path.substring(0, path.lastIndexOf('/'));
  }
}