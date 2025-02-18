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
    Subsystem   :   Common shared collection facilities

    File        :   Contains.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Contains.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import oracle.hst.foundation.object.Attribute;
import oracle.hst.foundation.object.FilterVisitor;

////////////////////////////////////////////////////////////////////////////////
// class Contains
// ~~~~~ ~~~~~~~~
/**
 ** Filter based on strings.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Contains extends StringFilter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String TAG = "co";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Contains</code> w/ the {@link Attribute} to compare.
   **
   ** @param  attribute          the {@link Attribute} this
   **                            {@link SingleValueAttributeFilter} belongs to.
   ** @param  caseIgnore         advice that the comparison of the string value
   **                            ignores case sensitivity.
   */
  public Contains(final Attribute attribute, final boolean caseIgnore) {
    // ensure inheritance
    super(attribute, caseIgnore);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tag (Filter)
  /**
   ** Determines the tag of this {@link StringFilter}.
   **
   ** @return                    the tag of this {@link StringFilter}.
   */
  @Override
  public final String tag() {
    return TAG;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept(Filter)
  /**
   ** Applies a {@link FilterVisitor} to this <code>Filter</code>.
   **
   ** @param  <R>                 the return type of the visitor's methods.
   ** @param  <P>                 the type of the additional parameters to the
   **                             visitor's methods.
   ** @param  visitor             the filter visitor.
   ** @param  parameter           the optional additional visitor parameter.
   **
   ** @return                     a result as specified by the visitor.
   */
  @Override
  public <R, P> R accept(final FilterVisitor<R, P> visitor, final P parameter) {
    return visitor.contains(parameter, this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (StringFilter)
  /**
   ** Determines whether the specified <code>value</code> matches this filter.
   **
   ** @param  value              the value to match.
   **
   ** @return                    <code>true</code> if the value matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  @Override
  public boolean accept(final String value) {
    return value.contains(value());
  }
}