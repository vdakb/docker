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
    Subsystem   :   Common Shared Collection Utility

    File        :   Filter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Filter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.collection;

////////////////////////////////////////////////////////////////////////////////
// interface Filter
// ~~~~~~~~~ ~~~~~~
/**
 ** A value filters for entries of a collection.
 ** <p>
 ** Subclasses must implement an accept method: public boolean accept(E); for
 ** which the accept method returns <code>true</code> are said to match the
 ** filter.
 **
 ** @see FilteringIterator
 */
public interface Filter<E> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Filter NULL = new Filter() {

    ////////////////////////////////////////////////////////////////////////////
    // Method:   accept
    /**
     ** Returns <code>true</code> if the specified {@link Object} matches the
     ** requirements of this filter; returns <code>false</code> otherwise.
     **
     ** @param  test              the object to test if this filter will
     **                           accepting the instance.
     **
     ** @return                   whether the passed object instance was
     **                           accepted by this filter.
     */
    public boolean accept(final Object test) {
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   toString
    /**
     ** Returns a string representation of this instance.
     **
     ** @return                   the string representation of this instance.
     */
    public String toString() {
      return "NULL";
    }
  };

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept
  /**
   ** Returns <code>true</code> if the specified {@link Object} matches the
   ** requirements of this filter; returns <code>false</code> otherwise.
   **
   ** @param  test              the object to test if this filter will
   **                           accepting the instance.
   **
   ** @return                   whether the passed object instance was
   **                           accepted by this filter.
   */
  boolean accept(final E test);
}