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

    File        :   EndsWith.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndsWith.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import oracle.hst.platform.core.SystemException;

////////////////////////////////////////////////////////////////////////////////
// class EndsWith
// ~~~~~ ~~~~~~~~
/**
 ** Filter based on strings.
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
public class EndsWith<T> extends AttributeFilter<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndsWith</code> w/ the {@link String} to compare.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   */
  public EndsWith(final Path path, final T value) {
    // ensure inheritance
    super(Type.EW, path, value);
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

    return visitor.endsWith(parameter, this);
  }
}