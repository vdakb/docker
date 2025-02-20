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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Not.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Not.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.object;

import com.fasterxml.jackson.databind.node.ValueNode;

import oracle.iam.system.simulation.ServiceException;

import oracle.iam.system.simulation.scim.Path;

////////////////////////////////////////////////////////////////////////////////
// class Not
// ~~~~~ ~~~
/**
 ** Proxy the filter to return the negative of the value.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Not implements Filter {

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
    return Type.NOT;
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
   **                            Possible object is {@link ValueNode}.
   */
  @Override
  public final ValueNode value() {
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
   */
  @Override
  public boolean isComplex() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept(Filter)
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

    return visitor.not(parameter, this);
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
   ** Two <code>Presence</code> filter are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>Presence</code> filter may be different even though they contain the
   ** same set of names with the same values, but in a different order.
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

    final Not that = (Not)other;
    return this.filter.equals(that.filter);
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
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder("not (");
    builder.append(this.filter).append(")");
    return builder.toString();
  }
}