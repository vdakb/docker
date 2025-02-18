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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   AttributeFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeFilter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.entity;

import java.util.Objects;

import com.fasterxml.jackson.databind.node.ValueNode;

import oracle.iam.platform.scim.schema.Support;

////////////////////////////////////////////////////////////////////////////////
// abstract class AttributeFilter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Attribute comparison filter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AttributeFilter implements Filter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the {@link Type} of this {@link Filter}. */
  private final Type      type;

  /** the {@link Path} this {@link Filter} belongs to. */
  private final Path      path;
  private final ValueNode value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeFilter</code> which belongs to the specified
   ** {@link Path}.
   **
   ** @param  type               the {@link Type} this {@link Filter}
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  path               the {@link Path} this {@link Filter}
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link ValueNode} value to compare.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   */
  protected AttributeFilter(final Type type, final Path path, final ValueNode value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type  = type;
    this.path  = path;
    this.value = (value == null) ? Support.nodeFactory().nullNode() : value;
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
   ** @return                    the path to the attribute to filter by, or
   **                            <code>null</code> if this filter is not a
   **                            comparison filter or a value filter for complex
   **                            multi-valued attributes.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  @Override
  public Path path() {
    return this.path;
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
  public ValueNode value() {
    return this.value;
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
   **                            Possible object is <code>boolean</code>.
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
    return Objects.hash(this.path, this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>AttributeFilter</code> are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>AttributeFilter</code> may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>..
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final AttributeFilter that = (AttributeFilter)other;
    return Objects.equals(this.path,  that.path)
        && Objects.equals(this.value, that.value)
     ;
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
    final StringBuilder builder = new StringBuilder(this.path.toString());
    builder.append(' ');
    builder.append(this.type.value());
    builder.append(' ');
    builder.append(this.value.toString());
    return builder.toString();
  }
}