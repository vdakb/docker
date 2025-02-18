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

    File        :   ClassUtility.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ClassUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.utility;

import java.io.Serializable;

import oracle.iam.identity.icf.foundation.SystemConstant;

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

  /** Suffix for array class names */
  public static final String ARRAY_SUFFIX               = "[]";

  /** The package separator character '.' */
  private static final char  PACKAGE_SEPARATOR_CHAR     = SystemConstant.PERIOD;

  /** The inner class separator character '$' */
  private static final char  INNER_CLASS_SEPARATOR_CHAR = '$';

  public static final String CLASS                      = "class";
  public static final String TXT                        = "txt";
  public static final String LOG                        = "log";
  public static final String CFG                        = "cfg";
  public static final String XML                        = "xml";
  public static final String XSD                        = "xsd";
  public static final String XSL                        = "xsl";
  public static final String GIF                        = "gif";
  public static final String JPG                        = "jpg";
  public static final String PNG                        = "png";

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
   **
   ** @return                    the package name of the class without the class
   **                            name.
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
   **
   ** @return                    the package name of the class without the class
   **                            name.
   */
  public static String packageName(final String className) {
    int lastPeriod = className.lastIndexOf(SystemConstant.PERIOD);
    return (lastPeriod == -1) ? SystemConstant.EMPTY : className.substring(0, lastPeriod);
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
  // Method:   shortName
  /**
   ** Returns the class name without the qualified package name for the
   ** specified object.
   **
   ** @param  object             the object to get the short class name for.
   **
   ** @return                    the class name of the class without the package
   **                            name.
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
   **
   ** @return                    the class name of the class without the package
   **                            name.
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
    return classNameToFile(clazz.getName(), extension);
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
    return classNameToDir(clazz.getName());
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
  public static String classNameToDir(final String name) {
    String path =  name.replace(PACKAGE_SEPARATOR_CHAR, SystemConstant.SLASH);
    return path.substring(0, path.lastIndexOf("/"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNameToFile
  /**
   ** Converts a class name to a file name. All periods are replaced with
   ** slashes and the '.class' extension is added.
   **
   ** @param  name               the class name to convert.
   **
   ** @return                    a fullqualified pathname
   */
  public static String classNameToFile(final String name) {
    return classNameToFile(name, CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNameToFile
  /**
   ** Converts a class name to a file name. All periods are replaced with
   ** slashes and the supplied extension is added.
   **
   ** @param  name               the class name to convert.
   ** @param  extension          the file extension to append on the name.
   **
   ** @return                    a fullqualified pathname
   */
  public static String classNameToFile(final String name, final String extension) {
    return name.replace(PACKAGE_SEPARATOR_CHAR, SystemConstant.SLASH).concat(".").concat(extension);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isComparable
  /**
   ** Checks, whether the given classes are comparable.
   ** <br>
   ** This method will return <code>true</code>, if one of the classes is
   ** assignable from the other class.
   **
   ** @param  clazz1             the first class to compare
   ** @param  clazz2             the second class to compare
   **
   ** @return                    <code>true</code>, if the classes share a
   **                            direct relation, <code>false</code> otherwise.
     */
  public static boolean isComparable(final Class<?> clazz1, final Class<?> clazz2) {
    return (clazz1.isAssignableFrom(clazz2) || clazz2.isAssignableFrom(clazz1));
  }

  /**
   ** Returns <code>true</code> if a class implements <code>Serializable</code>
   ** and <code>false</code> otherwise.
   **
   ** @param  clazz              the class to check.
   **
   ** @return                    <code>true</code> if a class implements
   **                            <code>Serializable</code>; otherwise
   **                            <code>false</code>
   */
  public static boolean isSerializable(final Class<?> clazz) {
    return (Serializable.class.isAssignableFrom(clazz));
  }
}