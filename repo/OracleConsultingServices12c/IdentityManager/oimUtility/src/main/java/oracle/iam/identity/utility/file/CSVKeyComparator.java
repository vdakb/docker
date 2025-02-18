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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVKeyComparator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVKeyComparator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Comparator;

////////////////////////////////////////////////////////////////////////////////
// final class CSVKeyComparator
// ~~~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This class is an helper class to compare the identifier of two CSV tuples.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVKeyComparator implements Comparator<CSVRecord> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CSVKeyComparator</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CSVKeyComparator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare (Comparator)
  /**
   ** Compares its two arguments for order.
   ** <br>
   ** Returns a negative integer, zero, or a positive integer as the first
   ** argument is less than, equal to, or greater than the second.
   ** <p>
   ** It is generally the case, but <i>not</i> strictly required that
   ** <code>(compare(x, y) == 0) == (x.equals(y))</code>. Generally speaking,
   ** any comparator that violates this condition should clearly indicate this
   ** fact. The recommended language is "Note: this comparator imposes orderings
   ** that are inconsistent with equals."
   **
   ** @param  lhs              the left-hand-side {@link CSVRecord} to be
   **                          compared.
   ** @param  rhs              the right-hand-side {@link CSVRecord} to be
   **                          compared.
   **
   ** @return                  a negative integer, zero, or a positive integer
   **                          as the first argument is less than, equal to,
   **                          or greater than the second.
   **
   ** @throws ClassCastException if the arguments' types prevent them from
   **                            being compared by this Comparator.
   */
  @Override
  public int compare(final CSVRecord lhs, final CSVRecord rhs) {
    return lhs.compareKey(rhs);
  }
}