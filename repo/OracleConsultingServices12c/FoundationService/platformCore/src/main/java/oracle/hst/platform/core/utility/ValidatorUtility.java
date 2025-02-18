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

    File        :   ValidatorUtility.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ValidatorUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.utility;

////////////////////////////////////////////////////////////////////////////////
// abstract class ValidatorUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous collection of validation methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ValidatorUtility {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ValidatorUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ValidatorUtility()" and enforces use of the public method below.
   */
  private ValidatorUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elementIndex
  /**
   ** Ensures that <code>index</code> specifies a valid <i>element</i> in an
   ** array, collection or string of size <code>limit</code>.
   ** <br>
   ** An element index may range from zero, inclusive, to <code>limit</code>,
   ** exclusive.
   **
   ** @param  index              a number identifying an element of an array,
   **                            collection or string.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  limit              the size limit of that array, collection or
   **                            string.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the value of <code>index</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>index</code> is negative or is
   **                                   not less than <code>limit</code>.
   ** @throws IllegalArgumentException  if <code>limit</code> is negative.
   */
  public static int elementIndex(int index, int limit) {
    return elementIndex(index, limit, "index");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elementIndex
  /**
   ** Ensures that <code>index</code> specifies a valid <i>element</i> in an
   ** array, collection or string of size <code>limit</code>.
   ** <br>
   ** An element index may range from zero, inclusive, to <code>limit</code>,
   ** exclusive.
   **
   ** @param  index              a number identifying an element of an array,
   **                            collection or string.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  limit              the size limit of that array, collection or
   **                            string.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  label              the text to use to describe the index in an
   **                            error message.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of <code>index</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>index</code> is negative or is
   **                                   not less than <code>limit</code>.
   ** @throws IllegalArgumentException  if <code>limit</code> is negative.
   */
  public static int elementIndex(final int index, final int limit, final String label) {
    // carefully optimized for execution by hotspot (explanatory comment above)
    if (index < 0 || index >= limit)
      throw new IndexOutOfBoundsException(indexViolated(index, limit, label));

    return index;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexViolated
  /**
   ** Creates the message about element index violation send back by an
   ** exception or log.
   **
   ** @param  index              a number identifying an element of an array,
   **                            collection or string.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  limit              the size limit of that array, collection or
   **                            string.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  label              the text to use to describe the index in an
   **                            error message.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the message string about element index
   **                            violation.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String indexViolated(final int index, final int limit, final String label) {
    if (index < 0) {
      return String.format("%s (%s) must not be negative", label, index);
    }
    else if (limit < 0) {
      return String.format("Negative limit: " + limit);
    }
    // index >= size
    else {
      return String.format("%s (%s) must be less than limit (%s)", label, index, limit);
    }
  }
}
