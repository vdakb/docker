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

    File        :   ContainsAll.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ContainsAll.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import java.util.Map;
import java.util.List;

import oracle.hst.foundation.object.Entity;
import oracle.hst.foundation.object.Attribute;
import oracle.hst.foundation.object.FilterVisitor;

////////////////////////////////////////////////////////////////////////////////
// class ContainsAll
// ~~~~~ ~~~~~~~~~~~
/**
 ** Filter based on attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class ContainsAll<T> extends AttributeFilter<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String TAG = "ca";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the <code>Attribute</code>. */
  private final String       name;

  /** the values of the <code>Attribute</code>. */
  private final List<Object> values;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ContainsAll</code> which belongs to the specified
   ** {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} this
   **                            {@link AttributeFilter} belongs to.
   */
  public ContainsAll(final Attribute attribute) {
    // ensure inheritance
    super(attribute);

    // prevent bogus input
    if (attribute.value() == null)
      throw new IllegalArgumentException("Must be a non null value!");

    // initialize instance attributes
    this.name   = attribute.name();
    this.values = attribute.value();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tag (Filter)
  /**
   ** Determines the tag of this {@link AttributeFilter}.
   **
   ** @return                    the tag of this {@link AttributeFilter}.
   */
  @Override
  public final String tag() {
    return TAG;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Filter)
  /**
   ** Determines whether the specified {@link Map} matches this filter.
   ** <br>
   ** Return the opposite the internal filters return value.
   **
   ** @param  entity             the specified {@link Map}.
   **
   ** @return                    <code>true</code> if the entity matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  @Override
  public boolean accept(final Map<String, Object> entity) {
   return entity.values().containsAll(this.values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Filter)
  /**
   ** Determines whether the specified {@link Entity} matches this filter.
   ** <br>
   ** Return the opposite the internal filters return value.
   **
   ** @param  entity             the specified {@link Entity}.
   **
   ** @return                    <code>true</code> if the entity matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  @Override
  public boolean accept(final Entity entity) {
   final Attribute found = entity.get(this.name);
   return found != null && found.value() != null && found.value().containsAll(this.values);
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
    return visitor.containsAll(parameter, this);
  }
}