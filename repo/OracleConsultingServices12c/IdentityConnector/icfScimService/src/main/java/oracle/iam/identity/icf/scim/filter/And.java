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
    Subsystem   :   Generic SCIM Library

    File        :   And.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    And.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.filter;

import java.util.Collection;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.Filter;

////////////////////////////////////////////////////////////////////////////////
// class And
// ~~~~~ ~~~
/**
 ** Logically "ANDs" together the two specified instances of {@link Filter}.
 ** The resulting <i>conjunct</i> <code>Filter</code> is <code>true</code> if
 ** and only if both of the specified filters are <code>true</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class And extends CompositeFilter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>And</code> w/ the left and right {@link Filter}s
   ** provided.
   **
   ** @param  lhs                the left side of the <code>And</code>.
   ** @param  rhs                the right side of the <code>And</code>.
   */
  public And(final Filter lhs, final Filter rhs) {
    // ensure inheritance
    super(Type.AND, lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>And</code> w/ the left and right {@link Filter}s
   ** provided.
   **
   ** @param  filter             the left side of a composite based filter.
   */
  public And(final Collection<Filter> filter) {
    // ensure inheritance
    super(Type.AND, filter);
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
   ** @param  <P>                the type of the additional parameters to the
   **                            visitor's methods.
   **                            <br>
   **                            Allowed object is <code>&lt;P&gt;</code>.
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
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  @Override
  public <R, P> R accept(final Visitor<R, P> visitor, final P parameter)
    throws ServiceException {

    return visitor.and(parameter, this);
  }
}