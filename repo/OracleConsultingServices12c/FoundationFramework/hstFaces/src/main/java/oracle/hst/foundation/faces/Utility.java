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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   Utility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Utility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces;

import java.lang.reflect.Array;

import java.util.Map;
import java.util.Collection;

////////////////////////////////////////////////////////////////////////////////
// class Utility
// ~~~~~ ~~~~~~~
/**
 ** Collection of general Utility methods that do not fit in one of the more
 ** specific classes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Utility {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Utility</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Utility() {
    // do not instantiate
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Returns <code>true</code> if the given string is <code>null</code> or is
   ** empty.
   **
   ** @param  string            the string to be checked on emptiness.
   **
   ** @return                   <code>true</code> if the given string is
   **                           <code>null</code> or is empty.
   */
  public static boolean empty(final String string) {
    return string == null || string.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Returns <code>true</code> if the given array is <code>null</code> or is
   ** empty.
   **
   ** @param  array             the array to be checked on emptiness.
   **
   ** @return                   <code>true</code> if the given array is
   **                           <code>null</code> or is empty.
   */
  public static boolean empty(final Object[] array) {
    return array == null || array.length == 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Returns <code>true</code> if the given collection is <code>null</code> or
   ** is empty.
   **
   ** @param  collection        the collection to be checked on emptiness.
   **
   ** @return                   <code>true</code> if the given collection is
   **                           <code>null</code> or is empty.
   */
  public static boolean empty(final Collection<?> collection) {
   return collection == null || collection.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Returns <code>true</code> if the given value is <code>null</code> or
   ** is empty.
   ** <p>
   ** Types of String, Collection, Map and Array are recognized. If none is
   ** recognized, then examine the emptiness of the toString() representation
   ** instead.
   **
   ** @param  value             the value to be checked on emptiness.
   **
   ** @return                   <code>true</code> if the given value is
   **                           <code>null</code> or is empty.
   */
  public static boolean empty(final Object value) {
    if (value == null)
     return true;
    else if (value instanceof String)
      return ((String)value).isEmpty();
    else if (value instanceof Collection<?>)
      return ((Collection<?>)value).isEmpty();
    else if (value instanceof Map<?, ?>)
      return ((Map<?, ?>)value).isEmpty();
    else if (value.getClass().isArray())
      return Array.getLength(value) == 0;
    else
      return value.toString() == null || value.toString().isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   coalesce
  /**
   ** Returns the first non-<code>null</code> object of the argument list, or
   ** <code>null</code> if there is no such element.
   **
   ** @param  <T>                the generic object type.
   ** @param  objects            the argument list of objects to be tested for
   **                            non-<code>null</code>.
   **
   ** @return                    the first non-<code>null</code> object of the
   **                            argument list, or <code>null</code> if there is
   **                            no such element.
   */
  @SuppressWarnings("unchecked")
  public static <T> T coalesce(final T... objects) {
    for (T object : objects) {
      if (object != null)
        return object;
    }
    return null;
  }
}