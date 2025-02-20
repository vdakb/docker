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

    File        :   ListResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResponse.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.domain;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.scim.schema.Entity;

import oracle.iam.system.simulation.scim.annotation.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class ListResponse
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Class representing a SCIM 2 list response.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** This class isnt't annotated by a schema annotation because the intended usage
 ** is for clients only where the schema isn't importatnt
 **
 ** @param  <T>                  the type of the returned resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ListResponse<T> extends    Entity<ListResponse>
                             implements Iterable<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The property name of the number of results returned by the list or query
   ** operation.
   */
  public static final String TOTAL    = "totalResults";
  /**
   ** The property name of the 1-based index of the first result in the current
   ** set of list results.
   */
  public static final String START    = "startIndex";
  /**
   ** The property name of the number of resources returned in a list response
   ** page.
   */
  public static final String ITEMS    = "itemsPerPage";
  /**
   ** The property name of the multi-valued schema containing the requested
   ** resources.
   */
  public static final String SCHEMA   = "schemas";
  /**
   ** The property name of the multi-valued list of complex objects containing
   ** the requested resources.
   */
  public static final String RESOURCE = "Resources";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(value=TOTAL, required=true)
  @Attribute(description="The total number of results returned by the list or query operation")
  private final int     total;

  @JsonProperty(value=RESOURCE, required=true)
  @Attribute(description="A multi-valued list of complex objects containing the requested resources")
  private final List<T> resource;

  @JsonProperty(START)
  @Attribute(description="The 1-based index of the first result in the current set of list results")
  private final Integer start;

  @JsonProperty(ITEMS)
  @Attribute(description="The number of resources returned in a list response page")
  private final Integer items;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a SCIM <code>ListResponse</code> result obtaining the
   ** properties from the given value mapping
   **
   ** @param  properties         the value mapping
   */
  @JsonCreator(mode=JsonCreator.Mode.DELEGATING)
  public ListResponse(final Map<String, Object> properties) {
    // ensure inheritance
    super();

    // initialize instance attributes
    validate(properties, new String[] {TOTAL, RESOURCE});
    this.total    = (Integer)properties.get(TOTAL);
    this.start    = properties.containsKey(START) ? (Integer)properties.get(START) : null;
    this.items    = properties.containsKey(ITEMS) ? (Integer)properties.get(ITEMS) : null;
    this.resource = (List<T>)properties.get(RESOURCE);
    if (properties.containsKey(SCHEMA)) {
      namespace((Collection<String>)properties.get(SCHEMA));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a SCIM <code>ListResponse</code> with the specified properties
   **
   ** @param  total              the total number of results returned.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  resource           a multi-valued list of complex objects
   **                            containing the requested resources.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>T</code>.
   ** @param  start              the 1-based index of hte first result in the
   **                            current set of list results.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  items              the number of resources returned in a list
   **                            response page.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public ListResponse(final int total, final List<T> resource, final Integer start, final Integer items) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.total    = total;
    this.start    = start;
    this.items    = items;
    this.resource = resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a SCIM <code>ListResponse</code>.
   **
   ** @param  resources          a multi-valued list of complex objects
   **                            containing the requested resources.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   */
  public ListResponse(final Collection<T> resources) {
    this.total    = resources.size();
    this.resource = new ArrayList<T>(resources);
    this.start    = null;
    this.items    = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total
  /**
   ** Returns the total number of results returned by the list or query
   ** operation.
   **
   ** @return                    the total number of results returned by the
   **                            list or query operation.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int total() {
    return this.total;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Returns the 1-based index of hte first result in the current set of list
   ** results.
   **
   ** @return                    the 1-based index of the first result in the
   **                            current set of list results or
   **                            <code>null</code> if pagination is not used and
   **                            the full results are returned.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int start() {
    return this.start;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   items
  /**
   ** Returns the number of resources returned in a list response page.
   **
   ** @return                    the number of resources returned in a list
   **                            response page.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int items() {
    return this.items;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the list of resources returned by the list or query operation.
   **
   ** @return                    the list of resources returned by the list or
   **                            query operation.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type &lt;T&gt;.
   */
  public final List<T> resource() {
    return CollectionUtility.unmodifiableList(this.resource);
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
   */
  @Override
  public final Iterator<T> iterator() {
    return this.resource.iterator();
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
    result = 31 * result + this.resource.hashCode();
    result = 31 * result + (this.start != null ? this.start.hashCode() : 0);
    result = 31 * result + (this.items != null ? this.items.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>SchemaResource</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>SchemaResource</code>s may be different even though they contain the
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

    return (this.resource.equals(that.resource));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the resource to the list of results of this
   ** <code>ListResponse</code>.
   **
   ** @param  resource           a SCIM resource.
   */
  public final void add(final T resource) {
    this.resource.add(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  private void validate(final Map<String, Object> properties, final String[] required) {
    for (final String cursor : required) {
      if (!properties.containsKey(cursor))
        throw new IllegalStateException(String.format("Missing required creator property '%s'", cursor));
    }
  }
}