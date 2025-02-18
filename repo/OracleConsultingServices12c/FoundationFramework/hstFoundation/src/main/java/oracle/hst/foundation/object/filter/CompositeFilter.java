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

    File        :   CompositeFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CompositeFilter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import java.util.Collection;
import java.util.LinkedList;

import oracle.hst.foundation.object.Filter;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class CompositeFilter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Useful for the AND, OR, XOR, etc..
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
abstract class CompositeFilter implements Filter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String        OR  = " or ";
  static final String        AND = " and ";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the left side of a composite based filter. */
  private String             conjunction;

  /** the left side of a composite based filter. */
  private LinkedList<Filter> filter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CompositeFilter</code> w/ the left and right
   ** {@link Filter}s provided.
   **
   ** @param  lhs                the left side of the <code>CompositeFilter</code>.
   ** @param  rhs                the right side of the <code>CompositeFilter</code>.
   */
  public CompositeFilter(final String conjunction, final Filter lhs, final Filter rhs) {
    // ensure inheritance
    this(conjunction, CollectionUtility.list(lhs, rhs));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CompositeFilter</code> w/ the left and right
   ** {@link Filter}s provided.
   **
   ** @param  filter             the left side of a composite based filter.
   */
  public CompositeFilter(final String conjunction, final Collection<Filter> filter) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.filter      = new LinkedList<Filter>(filter);
    this.conjunction = conjunction;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lhs
  /**
   ** Returns the left side of a composite based filter.
   **
   ** @return                    the left side of a composite based filter.
   */
  public Filter lhs() {
    return this.filter.getFirst();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rhs
  /**
   ** Returns the right side of a composite based filter.
   **
   ** @return                    the right side of a composite based filter.
   */
  public Filter rhs() {
    if (this.filter.size() > 2) {
      LinkedList<Filter> right = new LinkedList<Filter>(this.filter);
      right.removeFirst();
      return new And(right);
    }
    else if (this.filter.size() == 2) {
      return filter.getLast();
    }
    else {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the internal filter that is being negated.
   **
   ** @return                    the internal filter that is being negated.
   */
  public Collection<Filter> filter() {
    return CollectionUtility.unmodifiable(this.filter);
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
    return this.conjunction;
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
    StringBuilder builder = new StringBuilder().append('(');
    boolean first = true;
    for (final Filter cursor : this.filter) {
      if (first) {
        first = false;
      }
      else {
        builder.append(this.conjunction);
      }
      builder.append(cursor);
    }
    return builder.append(')').toString();
  }
}