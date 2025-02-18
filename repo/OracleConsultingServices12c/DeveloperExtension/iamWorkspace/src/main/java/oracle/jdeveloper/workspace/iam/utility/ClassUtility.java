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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ClassUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ClassUtility.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.utility;

////////////////////////////////////////////////////////////////////////////////
// abstract class ClassUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Miscellaneous class utility methods. Mainly for internal use.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class ClassUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CLASS                 = "class";
  public static final String XML                   = "xml";
  public static final String XSD                   = "xsd";

  /** The package separator character '.' */
  public static final char  PACKAGE_SEPARATOR_CHAR = '.';

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ClassUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ClassUtility()" and enforces use of the public method below.
   */
  private ClassUtility() {
    // should never be instantiated
    throw new UnsupportedOperationException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    return name.replace(PACKAGE_SEPARATOR_CHAR, '/').concat(".").concat(extension);
  }

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
   **
   ** @throws IllegalArgumentException if the class is null
   */
  public static String packageName(final String className) {
    int lastPeriod = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    return (lastPeriod == -1) ? StringUtility.EMPTY : className.substring(0, lastPeriod);
  }
}