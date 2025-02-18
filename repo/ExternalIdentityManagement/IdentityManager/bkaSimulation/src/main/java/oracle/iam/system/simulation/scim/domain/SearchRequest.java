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

    File        :   SearchRequest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SearchRequest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.schema.Entity;

import oracle.iam.system.simulation.scim.annotation.Schema;
import oracle.iam.system.simulation.scim.annotation.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class SearchRequest
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Class representing a SCIM 2 search request.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id="urn:ietf:params:scim:api:messages:2.0:SearchRequest", name="Search Operation", description="SCIM 2.0 Search Request")
public class SearchRequest extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The property name of the 1-based index of the first query result.
   */
  public static final String START     = "startIndex";
  /**
   ** The property name of the integer indicating the desired maximum number of
   ** query results per page.
   */
  public static final String COUNT     = "count";
  /**
   ** The property name of the filter string used to request a subset of
   ** resources.
   */
  public static final String FILTER    = "filter";
  /**
   ** The property name of the multi-valued list of strings indicating the names
   ** of resource attributes to return in the response overriding the set of
   ** attributes that would be returned by default.
   */
  public static final String REQUESTED = "attributes";
  /**
   ** The property name of the multi-valued list of strings indicating the names
   ** of resource attributes to return in the response overriding the set of
   ** attributes that would be returned by default.
   */
  public static final String EMITTED   = "attributes";
  /**
   ** The property name of the mulit-valued list of strings indicating the names
   ** of resource attributes to be removed from the default set of attributes to
   ** return.
   */
  public static final String OMITTED   = "excludedAttributes";
  /**
   ** The property name of the string indicating the order in which the sortBy
   ** parameter is applied.
   */
  public static final String ORDER     = "sortOrder";
  /**
   ** The property name of the string indicating the attribute whose value shall
   ** be used to order the returned responses.
   */
  public static final String SORT     = "sortBy";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(START)
  @Attribute(description="The 1-based index of the first query result")
  private final Integer     start;

  @JsonProperty(COUNT)
  @Attribute(description="An integer indicating the desired maximum number of query results per page")
  private final Integer     count;

  @JsonProperty(FILTER)
  @Attribute(description="The filter string used to request a subset of resources")
  private final String      filter;

  @JsonProperty(EMITTED)
  @Attribute(description="A multi-valued list of strings indicating the names of resource attributes to return in the response overriding the set of attributes that would be returned by default")
  private final Set<String> emitted;

  @JsonProperty(OMITTED)
  @Attribute(description="A mulit-valued list of strings indicating the names of resource attributes to be removed from the default set of attributes to return")
  private final Set<String> omitted;

  @JsonProperty(ORDER)
  @Attribute(description="A string indicating the order in which the sortBy parameter is applied")
  private final Order       order;

  @JsonProperty(SORT)
  @Attribute(description = "A string indicating the attribute whose value shall be used to order the returned responses")
  private final String      sort;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Order
  // ~~~~ ~~~~~
  /**
   ** The order in which the sortBy parameter is applied.
   */
  public enum Order {
      /** the ascending sort order */
      ASCENDING("ascending")
      /** the descending sort order */
    , DESCENDING("descending")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Order</code> with a constraint value.
     **
     ** @param  value            the constraint name (used in SCIM schemas) of
     **                          the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Order(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @JsonValue
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper <code>Order</code> constraint from the
     ** given string value.
     **
     ** @param  value            the string value the order constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Order</code> constraint.
     **                          <br>
     **                          Possible object is {@link Order}.
     */
    @JsonCreator
    public static Order fromValue(final String value) {
      for (Order cursor : Order.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SearchRequest</code> with the specified properties.
   **
   ** @param  start              the 1-based index of the first query result.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   ** @param  filter             the filter string used to request a subset of
   **                            resources.
   ** @param  sort               the string indicating the attribute whose value
   **                            shall be used to order the returned responses.
   ** @param  order              the order in which the sortBy parameter is
   **                            applied.
   ** @param  emitted            the list of strings indicating the names of
   **                            resource attributes to return in the response
   **                            overriding the set of attributes that would be
   **                            returned by default.
   ** @param  omitted            the list of strings indicating the names of
   **                            resource attributes to be removed from the
   **                            default set of attributes to return.
   */
  public SearchRequest(@JsonProperty(START) final Integer start, @JsonProperty(COUNT) final Integer count, @JsonProperty(FILTER) final String filter, @JsonProperty(SORT) final String sort, @JsonProperty(ORDER) final Order order, @JsonProperty(EMITTED) final Set<String> emitted, @JsonProperty(OMITTED) final Set<String> omitted) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    this.start   = start;
    this.count   = count;
    this.filter  = filter;
    this.sort    = sort;
    this.order   = order;
    this.emitted = emitted;
    this.omitted = omitted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Returns the 1-based index of the first query result.
   **
   ** @return                    the 1-based index of the first query result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer start() {
    return this.start;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Returns the desired maximum number of query results per page.
   **
   ** @return                    the desired maximum number of query results per
   **                            page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer count() {
    return this.count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the filter string used to request a subset of resources.
   **
   ** @return                    the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sort
  /**
   ** Returns the string indicating the attribute whose value shall be used to
   ** order the returned responses.
   **
   ** @return                    the string indicating the attribute whose value
   **                            shall be used to order the returned responses
   **                            or <code>null</code> if sorting is not required.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String sort() {
    return this.sort;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   order
  /**
   ** Returns the order in which the sort parameter is applied.
   **
   ** @return                    the order in which the sort parameter is
   **                            applied or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            Possible object is {@link Order}.
   */
  public Order order() {
    return this.order;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   emitted
  /**
   ** Returns the list of strings indicating the names of resource attributes to
   ** return in the response overriding the set of attributes that would be
   ** returned by default.
   **
   ** @return                    the {@link Set} of strings indicating the
   **                            names of resource attributes to return.
   */
  public Set<String> emitted() {
    return this.emitted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   omitted
  /**
   ** Returns the list of strings indicating the names of resource attributes to
   ** be removed from the default set of attributes to return.
   **
   ** @return                    the list of strings indicating the names of
   **                            resource attributes to be removed.
   */
  public Set<String> omitted() {
    return this.omitted;
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
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (this.start   != null ? this.start.hashCode()   : 0);
    result = 31 * result + (this.count   != null ? this.count.hashCode()   : 0);
    result = 31 * result + (this.filter  != null ? this.filter.hashCode()  : 0);
    result = 31 * result + (this.sort    != null ? this.sort.hashCode()    : 0);
    result = 31 * result + (this.order   != null ? this.order.hashCode()   : 0);
    result = 31 * result + (this.emitted != null ? this.emitted.hashCode() : 0);
    result = 31 * result + (this.omitted != null ? this.omitted.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>SearchRequest</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>SearchRequest</code>s may be different even though they contain the
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
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    if (!super.equals(other)) {
      return false;
    }

    final SearchRequest that = (SearchRequest)other;
    if (this.start != null ? !this.start.equals(that.start) : that.start != null) {
      return false;
    }
    if (this.count != null ? !this.count.equals(that.count) : that.count != null) {
      return false;
    }
    if (this.filter != null ? !this.filter.equals(that.filter) : that.filter != null) {
      return false;
    }
    if (this.sort != null ? !this.sort.equals(that.sort) : that.sort != null) {
      return false;
    }
    if (this.order != null ? !this.order.equals(that.order) : that.order != null) {
      return false;
    }
    if (this.omitted != null ? !this.omitted.equals(that.omitted) : that.omitted != null) {
      return false;
    }
    return (this.emitted == null ? that.emitted == null : this.emitted.equals(that.emitted));
  }
}
