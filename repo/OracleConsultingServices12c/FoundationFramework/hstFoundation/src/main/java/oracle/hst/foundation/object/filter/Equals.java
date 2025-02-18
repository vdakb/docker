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

    File        :   Equals.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Equals.


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
// class Equals
// ~~~~~ ~~~~~~
/**
 ** Determines whether an {@link Entity entity} contains an
 ** {@link Attribute attribute} that matches a specific attribute value.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Equals<T> extends AttributeFilter<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String TAG = "eq";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Equals</code> filter which belongs to the specified
   ** {@link Attribute}.
   **
   ** @param  attribute          the {@link Attribute} this
   **                            {@link AttributeFilter} belongs to.
   */
  public Equals(final Attribute attribute) {
    // ensure inheritance
    super(attribute);
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
   ** Determines whether the specified {@link Map} contains an attribute that
   ** has the same name and contains a value that is equals the value of the
   ** attribute that <code>FilterBuilder</code> placed into this filter.
   ** <p>
   ** Note that in the case of a multi-valued attribute, equality of values
   ** means that:
   ** <ul>
   **   <li>the value of the attribute in the connector object and the value of
   **       the attribute in the filter must contain <em>the same number of
   **       elements</em>; and that
   **   <li>each element within the value of the attribute in the connector
   **       object must <em>equal the element that occupies the same position</em>
   **       within the value of the attribute in the filter.
   ** </ul>
   **
   ** @param  entity             the specified {@link Map}.
   **
   ** @return                    <code>true</code> if the entity matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  @Override
  public boolean accept(final Map<String, Object> entity) {
    boolean result = false;
    final Attribute thisAttr = attribute();
    final Object    attr     = entity.get(thisAttr.name());
    if (attr != null) {
      result = thisAttr.value().get(0).equals(attr);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Filter)
  /**
   ** Determines whether the specified {@link Entity} contains an attribute that
   ** has the same name and contains a value that is equals the value of the
   ** attribute that <code>FilterBuilder</code> placed into this filter.
   ** <p>
   ** Note that in the case of a multi-valued attribute, equality of values
   ** means that:
   ** <ul>
   **   <li>the value of the attribute in the connector object and the value of
   **       the attribute in the filter must contain <em>the same number of
   **       elements</em>; and that
   **   <li>each element within the value of the attribute in the connector
   **       object must <em>equal the element that occupies the same position</em>
   **       within the value of the attribute in the filter.
   ** </ul>
   **
   ** @param  entity             the specified {@link Entity}.
   **
   ** @return                    <code>true</code> if the entity matches (that
   **                            is, satisfies all selection criteria of) this
   **                            filter; otherwise <code>false</code>.
   */
  @Override
  public boolean accept(final Entity entity) {
    boolean result = false;
    final Attribute that = entity.get(name());
    if (that != null) {
      result = attribute().equals(that);
    }
    return result;
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
    return visitor.equal(parameter, this);
  }
}