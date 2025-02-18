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

    File        :   SearchRequest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SearchRequest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.request;

import java.util.Set;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.platform.scim.SearchControl;

import oracle.iam.platform.scim.schema.Entity;

import oracle.iam.platform.scim.annotation.Schema;
import oracle.iam.platform.scim.annotation.Attribute;

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
public class SearchRequest extends Entity<SearchRequest> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(SearchControl.START)
  @Attribute(description="The 1-based index of the first query result")
  private final Integer             start;

  @JsonProperty(SearchControl.COUNT)
  @Attribute(description="An integer indicating the desired maximum number of query results per page")
  private final Integer             count;

  @JsonProperty(SearchControl.FILTER)
  @Attribute(description="The filter string used to request a subset of resources")
  private final String              filter;

  @JsonProperty(SearchControl.EMITTED)
  @Attribute(description="A multi-valued list of strings indicating the names of resource attributes to return in the response overriding the set of attributes that would be returned by default")
  private final Set<String>         emitted;

  @JsonProperty(SearchControl.OMITTED)
  @Attribute(description="A mulit-valued list of strings indicating the names of resource attributes to be removed from the default set of attributes to return")
  private final Set<String>         omitted;

  @JsonProperty(SearchControl.SORT)
  @Attribute(description = "A string indicating the attribute whose value shall be used to order the returned responses")
  private final String              sort;

  @JsonProperty(SearchControl.ORDER)
  @Attribute(description="A string indicating the order in which the sortBy parameter is applied")
  private final SearchControl.Order order;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SearchRequest</code> with the specified properties.
   **
   ** @param  start              the 1-based index of the first search result.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  count              the desired maximum number of search results
   **                            per page.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  filter             the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  sort               the string indicating the attribute whose value
   **                            shall be used to order the returned responses.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  order              the order in which the sortBy parameter is
   **                            applied.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  emitted            the list of strings indicating the names of
   **                            resource attributes to return in the response
   **                            overriding the set of attributes that would be
   **                            returned by default.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  omitted            the list of strings indicating the names of
   **                            resource attributes to be removed from the
   **                            default set of attributes to return.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public SearchRequest(@JsonProperty(SearchControl.START) final Integer start, @JsonProperty(SearchControl.COUNT) final Integer count, @JsonProperty(SearchControl.FILTER) final String filter, @JsonProperty(SearchControl.SORT) final String sort, @JsonProperty(SearchControl.ORDER) final SearchControl.Order order, @JsonProperty(SearchControl.EMITTED) final Set<String> emitted, @JsonProperty(SearchControl.OMITTED) final Set<String> omitted) {
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
   ** Returns the 1-based index of the first search result.
   **
   ** @return                    the 1-based index of the first search result or
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
   ** Returns the desired maximum number of search results per page.
   **
   ** @return                    the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
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
   **                            or <code>null</code> if sorting is not
   **                            required.
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
   **                            Possible object is {@link SearchControl.Order}.
   */
  public SearchControl.Order order() {
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
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
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
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
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
    return Objects.hash(this.start, this.count, this.filter, this.sort, this.order, this.omitted, this.emitted);
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

    final SearchRequest that = (SearchRequest)other;
    // ensure inheritance
    return super.equals(that)
        && Objects.equals(this.start,   that.start)
        && Objects.equals(this.count,   that.count)
        && Objects.equals(this.filter,  that.filter)
        && Objects.equals(this.sort,    that.sort)
        && Objects.equals(this.order,   that.order)
        && Objects.equals(this.omitted, that.omitted)
        && Objects.equals(this.emitted, that.emitted)
    ;
  }
}