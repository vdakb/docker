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

    File        :   GreaterThanOrEqual.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GreaterThanOrEqual.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import java.util.Map;

import oracle.hst.foundation.object.Entity;
import oracle.hst.foundation.object.Attribute;
import oracle.hst.foundation.object.FilterVisitor;

////////////////////////////////////////////////////////////////////////////////
// class GreaterThanOrEqual
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Determine if the {@link Entity} {@link Attribute} value is greater than or
 ** equals the one provided in the filter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class GreaterThanOrEqual<T> extends ComparableAttributeFilter<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String TAG = "ge";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GreaterThanOrEqual</code> w/ the {@link Attribute} to
   ** compare.
   **
   ** @param  attribute          the {@link Attribute} this
   **                            {@link ComparableAttributeFilter} belongs to.
   */
  public GreaterThanOrEqual(final Attribute attribute) {
    // ensure inheritance
    super(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tag (Filter)
  /**
   ** Determines the tag of this {@link ComparableAttributeFilter}.
   **
   ** @return                    the tag of this
   **                            {@link ComparableAttributeFilter}.
   */
  @Override
  public final String tag() {
    return TAG;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Filter)
  /**
   ** Determines whether the specified {@link Map} contains an attribute that
   ** has the same name and contains a value that is greater than or equals the
   ** value of the attribute that <code>FilterBuilder</code> placed into this
   ** filter.
   **
   ** @param  entity             the specified {@link Map}.
   **
   ** @return                    <code>true</code> if the entity matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  @Override
  public boolean accept(final Map<String, Object> entity) {
    return present(entity) && compare(entity) >= 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Filter)
  /**
   ** Determines whether the specified {@link Entity} contains an attribute that
   ** has the same name and contains a value that is greater than or equals the
   ** value of the attribute that <code>FilterBuilder</code> placed into this
   ** filter.
   **
   ** @param  entity             the specified {@link Entity}.
   **
   ** @return                    <code>true</code> if the entity matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  @Override
  public boolean accept(final Entity entity) {
    return present(entity) && compare(entity) >= 0;
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
    return visitor.greaterThanOrEqual(parameter, this);
  }
}