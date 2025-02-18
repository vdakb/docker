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

    File        :   AttributeFilter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    AttributeFilter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.util.List;
import java.util.LinkedList;

import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class CompositeFilter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Useful for the AND, OR, XOR, etc.
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
public abstract class CompositeFilter<T> implements Filter<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the {@link Type} of this {@link Filter}. */
  private final Type            type;

  /** the left side of a composite based filter. */
  private LinkedList<Filter<T>> filter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CompositeFilter</code> w/ the left and right
   ** {@link Filter}s provided.
   **
   ** @param  type               the {@link Type} this {@link Filter}
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  lhs                the left side of the
   **                            <code>CompositeFilter</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} of type
   **                            <code>T</code>.
   ** @param  rhs                the right side of the
   **                            <code>CompositeFilter</code>.
   **                            <br>
   **                            Allowed object is {@link Filter} of type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  protected CompositeFilter(final Type type, final Filter<T> lhs, final Filter<T> rhs) {
    // ensure inheritance
    this(type, CollectionUtility.list(lhs, rhs));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CompositeFilter</code> w/ the left and right
   ** {@link Filter}s provided.
   **
   ** @param  type               the {@link Type} this {@link Filter}
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  filter             the collection of a composite based filter.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Filter} of type
   **                            <code>T</code>.
   */
  public CompositeFilter(final Type type, final List<Filter<T>> filter) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.filter = new LinkedList<Filter<T>>(filter);
    this.type   = type;
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
   **                            <br>
   **                            Possible object is {@link Filter} of type
   **                            <code>T</code>.
   */
  public Filter<T> lhs() {
    return this.filter.getFirst();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rhs
  /**
   ** Returns the right side of a composite based filter.
   **
   ** @return                    the right side of a composite based filter.
   **                            <br>
   **                            Possible object is {@link Filter} of type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public Filter rhs() {
    if (this.filter.size() > 2) {
      final LinkedList<Filter> right = new LinkedList<Filter>(this.filter);
      right.removeFirst();
      return new And(right);
    }
    else if (this.filter.size() == 2) {
      return this.filter.getLast();
    }
    else {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the internal filter that is being merged.
   **
   ** @return                    the internal filter that is being merged.
   **                            <br>
   **                            Possible object is {@link List} where
   **                            each element is of type {@link Filter} for type
   **                            <code>T</code>.
   */
  public List<Filter<T>> filter() {
    return CollectionUtility.unmodifiable(this.filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type (Filter)
  /**
   ** Returns the {@link Type} of this {@link Filter}.
   **
   ** @return                    the {@link Type} of  this {@link Filter}.
   **                            <br>
   **                            Possible object is {@link Type}.
   */
  @Override
  public final Type type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path (Filter)
  /**
   ** Returns the path to the attribute to filter by, or <code>null</code> if
   ** this filter is not a comparison filter or a value filter for complex
   ** multi-valued attributes.
   **
   ** @return                    the path to the attribute to filter by or
   **                            <code>null</code> if this filter is not a
   **                            comparison filter or a value filter for complex
   **                            multi-valued attributes.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  @Override
  public final Path path() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value (Filter)
  /**
   ** Returns the comparison value, or <code>null</code> if this filter is not a
   ** comparison filter or a value filter for complex multi-valued attributes.
   **
   ** @return                    the comparison value, or <code>null</code> if
   **                            this filter is not a comparison filter or a
   **                            value filter for complex multi-valued attributes.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @Override
  public final T value() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isComplex (Filter)
  /**
   ** Whether this filter is a complex multi-valued attribute value filter.
   **
   ** @return                    <code>true</code> if this filter is a complex
   **                            multi-valued attribute value filter or
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>bolean</code>.
   */
  @Override
  public boolean isComplex() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return this.filter.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>CompositeFilter</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>CompositeFilter</code>s may be different even though they contain
   ** the same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final CompositeFilter that = (CompositeFilter)other;
    return this.filter.containsAll(that.filter);
  }

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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder('(');
    for (int i = 0; i < this.filter.size(); i++) {
      if (i != 0) {
        builder.append(' ').append(this.type.value()).append(' ');
      }
      builder.append(this.filter.get(i));
    }
    return builder.append(')').toString();
  }
}