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

    File        :   Or.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Or.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.util.List;

import oracle.hst.platform.core.SystemException;

////////////////////////////////////////////////////////////////////////////////
// class Or
// ~~~~~ ~~
/**
 ** Logically "OR" together the two specified instances of {@link Filter}.
 ** <br>
 ** The resulting <i>disjunct</i> <code>Filter</code> is <code>true</code> if
 ** and only if at least one of the specified filters is <code>true</code>.
 **
 ** @param  <T>                  the type of the contained filter value.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the filters
 **                              implementing this interface (compare operations
 **                              can use their own specific types instead of
 **                              types defined by this interface only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Or<T> extends CompositeFilter<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Or</code> w/ the left and right {@link Filter}s
   ** provided.
   **
   ** @param  lhs                the left side of the <code>Or</code>
   **                            {@link Filter}.
   **                            <br>
   **                            Allowed object is {@link Filter} of type
   **                            <code>T</code>.
   ** @param  rhs                the right side of the <code>Or</code>
   **                            {@link Filter}.
   **                            <br>
   **                            Allowed object is {@link Filter} of type
   **                            <code>T</code>.
   */
  public Or(final Filter<T> lhs, final Filter<T> rhs) {
    // ensure inheritance
    super(Type.OR, lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Or</code> w/ the left and right {@link Filter}s
   ** provided.
   **
   ** @param  filter             the left side of a composite based filter.
   */
  public Or(final List<Filter<T>> filter) {
    // ensure inheritance
    super(Type.OR, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Filter)
  /**
   ** Applies a {@link Visitor} to this <code>Filter</code>.
   **
   ** @param  <R>                the return type of the visitor's methods.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  visitor            the filter visitor.
   **                            <br>
   **                            Allowed object is {@link Visitor}}.
   ** @param  parameter          the optional additional visitor parameter.
   **                            <br>
   **                            Allowed object is <code>P</code>.
   **
   ** @return                    a result as specified by the visitor.
   **                            <br>
   **                            Possible object is <code>R</code>.
   **
   ** @throws SystemException    if the filter is not valid for matching.
   */
  @Override
  public <R> R accept(final Visitor<R, T> visitor, final T parameter)
    throws SystemException {

    return visitor.or(parameter, this);
  }
}