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
    Subsystem   :   Generic REST Library

    File        :   ListResponse.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResponse.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.response;

import java.util.List;
import java.util.Iterator;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

////////////////////////////////////////////////////////////////////////////////
// class ListResponse
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Class representing a REST list response.
 **
 ** @param  <T>                  the type of the returned resources.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ListResponse<T> implements Iterable<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /**
   ** The property name of the number of results returned by the list or query
   ** operation.
   */
  public static final String TOTAL  = "total";
  /**
   ** The property name of the 1-based index of the first result in the current
   ** set of list results.
   */
  public static final String START  = "start";
  /**
   ** The property name of the number of resources returned in a list response
   ** page.
   */
  public static final String ITEMS  = "items";
  /**
   ** The property name of the multi-valued list of complex objects containing
   ** the requested resources.
   */
  public static final String RESULT = "result";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(value=TOTAL, required=true)
  private final Long          total;

  @JsonProperty(START)
  private final Integer       start;

  @JsonProperty(ITEMS)
  private final Integer       items;

  @JsonProperty(value=RESULT, required=true)
  private final Collection<T> result;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a REST <code>ListResponse</code> with the specified properties
   **
   ** @param  total              the total number of results returned.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  start              the 1-based index of hte first result in the
   **                            current set of list results.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  items              the number of resources returned in a list
   **                            response page.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  result             a multi-valued list of complex objects
   **                            containing the requested resources.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  public ListResponse(final Long total, final Integer start, final Integer items, final List<T> result) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.total  = total;
    this.start  = start;
    this.items  = items;
    this.result = result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Iterable)
  /**
   ** Returns an {@link Iterator} over elements the resource elements.
   **
   ** @return                    an {@link Iterator} over elements the resource
   **                            elements.
   **                            <br>
   **                            Possible object is {@link Iterator} over
   **                            elements of type &lt;T&gt;.
   */
  @Override
  public final Iterator<T> iterator() {
    return this.result.iterator();
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
   **       an execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (int)(this.total ^ (this.total >>> 32));
    result = 31 * result + (this.start != null ? this.start.hashCode() : 0);
    result = 31 * result + (this.items != null ? this.items.hashCode() : 0);
    result = 31 * result + this.result.hashCode();
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>ListResponse</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>ListResponse</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    if (!super.equals(other))
      return false;

    final ListResponse that = (ListResponse)other;
    if (this.total != that.total)
      return false;

    if (this.start != null ? !this.start.equals(that.start) : that.start != null)
      return false;

    if (this.items != null ? !this.items.equals(that.items) : that.items != null)
      return false;

    return (this.result.equals(that.result));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the resource to the list of results of this
   ** <code>ListResponse</code>.
   **
   ** @param  resource           a REST resource.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   */
  public final void add(final T resource) {
    this.result.add(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Gets the start index of this <code>ListResponse</code>.
   **
   ** @return                    start index in this <code>ListResponse</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int start() {
    return this.start;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   items
  /**
   ** Gets the number of items in this <code>ListResponse</code>.
   **
   ** @return                    number of items in this <code>ListResponse</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int items() {
    return this.items;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total
  /**
   ** Gets the total number of items of this <code>ListResponse</code>.
   **
   ** @return                    total nunmber of items of this <code>ListResponse</code>.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public final long total() {
    return this.total;
  }
}