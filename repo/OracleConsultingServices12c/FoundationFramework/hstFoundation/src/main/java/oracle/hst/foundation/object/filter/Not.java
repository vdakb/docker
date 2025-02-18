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

    File        :   Not.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Not.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import java.util.Map;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.object.Filter;
import oracle.hst.foundation.object.Entity;
import oracle.hst.foundation.object.FilterVisitor;

import oracle.hst.foundation.resource.SystemBundle;

////////////////////////////////////////////////////////////////////////////////
// class Not
// ~~~~~ ~~~
/**
 ** Proxy the filter to return the negative of the value.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Not implements Filter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String TAG = "not";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the {@link Filter} this negative {@link Filter} belongs to. */
  private final Filter filter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Not</code> which negates the specified {@link Filter}.
   **
   ** @param  filter             the {@link Filter} that is being negated.
   */
  public Not(final Filter filter) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (filter == null)
      throw new IllegalArgumentException(SystemBundle.format(SystemError.ARGUMENT_IS_NULL, "filter"));

    // initialize instance attributes
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the internal filter that is being negated.
   **
   ** @return                    the internal filter that is being negated.
   */
  public Filter filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tag (Filter)
  /**
   ** Determines the tag of this {@link Filter}.
   **
   ** @return                    the tag of this {@link Filter}.
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
    return !this.filter.accept(entity);
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
    return !this.filter.accept(entity);
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
    return visitor.not(parameter, this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this {@link Filter}.
   ** <p>
   ** The string representation consists of a list of the set's elements in the
   ** order they are returned by its iterator, enclosed in curly brackets
   ** (<code>"{}"</code>). Adjacent elements are separated by the characters
   ** <code>", "</code> (comma and space). Elements are converted to strings as
   ** by <code>Object.toString()</code>.
   **
   ** @return                    a string representation of this {@link Filter}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("not(").append(filter()).append(")");
    return builder.toString();
  }
}