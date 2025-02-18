/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   SearchRequest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SearchRequest.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.hst.platform.jpa;

////////////////////////////////////////////////////////////////////////////////
// class SearchRequest
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Control to extend the functionality JPA operation, such as
 ** <code>search</code>, to carry out additional operations on top of the
 ** search.
 ** <p>
 ** The <code>SearchRequest</code> allows a client to request that the server
 ** send search results in small, manageable chunks within a specific range of
 ** entries. It also allows a client to move forward and backward through the
 ** results of a search operation, or jump directly to a particular entry.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SearchRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The default value applied to the 0-based index of the query result if
   ** there isn't a value specified in the query parameters.
   */
  public static final Integer       DEFAULT_START = 0;
  /**
   ** The default value applied to the integer indicating the desired maximum
   ** number of query results per page if there isn't a value specified in the
   ** query parameters.
   */
  public static final Integer       DEFAULT_COUNT = 25;

  public final static SearchRequest ALL           = SearchRequest.of(DEFAULT_START, Integer.MAX_VALUE);
  public final static SearchRequest ONE           = SearchRequest.of(DEFAULT_START, 1);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Boolean                   total;
  private Integer                   count;
  private Integer                   start;
  private final SortOption          order;
  private final SearchFilter        filter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SearchRequest</code> with the specified batch size and
   ** sort order to be applied on a search operation.
   **
   ** @param  start              the o-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  count              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  total              <code>true</code> if total number of entities
   **                            in the desired result set needs to be
   **                            estimated; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   ** @param  filter             the {@link SearchFilter} criteria to apply.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   ** @param  order              the {@link SortOption} operation to apply.
   **                            <br>
   **                            Allowed object is {@link SortOption}.
   */
  public SearchRequest(final Integer start, final Integer count, final Boolean total, final SearchFilter filter, final SortOption order) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.total  = total == null ? Boolean.FALSE : total;
    this.start  = validateInteger("start", start, DEFAULT_START, DEFAULT_START);
    this.count  = validateInteger("count", count, DEFAULT_COUNT, 1);
    this.order  = order;
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total
  /**
   ** Whether to run the <code>COUNT(id)</code> query to estimate total number
   ** of results. This will be available by
   ** {@code Persistence#total(Class)}.
   **
   ** @return                    <code>true</code> if total number of entities
   **                            in the desired result set needs to be
   **                            estimated; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Boolean} .
   */
  public final Boolean total() {
    return this.total;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Returns the 0-based index of the first search result.
   **
   ** @return                    the 0-based index of the first search result or
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
  // Method:   order
  /**
   ** Returns the {@link SearchFilter} to apply on the search.
   **
   ** @return                    the {@link SearchFilter} to apply on the
   **                            search.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   */
  public final SearchFilter filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   order
  /**
   ** Returns the {@link SortOption} to apply on the search.
   **
   ** @return                    the {@link SortOption} to apply on the search.
   **                            <br>
   **                            Possible object is {@link SortOption}.
   */
  public final SortOption order() {
    return this.order;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasNext
  /**
   ** Returns a clone of the current page which returns all results matching the
   ** current ordering.
   **
   ** @return                    a clone of the current page which returns all
   **                            results matching the current ordering.
   */
  public SearchRequest all() {
    return new SearchRequest(null, null, this.total, null, this.order);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasPrevious
  /**
   ** Whether there's a previous {@link SearchRequest} that can accessed from
   ** the current one.
   ** <br>
   ** Will return <code>false</code> in case the current {@link SearchRequest}
   ** already refers to the first search result.
   **
   ** @return                    <code>true</code> a previous
   **                            {@link SearchRequest} can accessed from this
   **                            one.
   **                            <br>
   **                            Possible object is  <code>SearchRequest</code>.
   */
   public boolean hasPrevious() {
    return this.start > 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   first
  /**
   ** Factory method to create a new <code>SearchRequest</code> returning the
   ** <code>SearchRequest</code> requesting the first search result}.
   **
   ** @param  count              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  total              <code>true</code> if total number of entities
   **                            in the desired result set needs to be
   **                            estimated; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   ** @param  filter             the {@link SearchFilter} criteria to apply.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the <code>SearchRequest</code> requesting the
   **                            first search result.
   **                            <br>
   **                            Possible object is <code>SearchRequest</code>.
   */
  public static final SearchRequest first(final Integer count, final Boolean total, final SearchFilter filter) {
    return first(count, total, filter, SortOption.NONE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   first
  /**
   ** Factory method to create a new <code>SearchRequest</code> returning the
   ** <code>SearchRequest</code> requesting the first search result.
   **
   ** @param  total              <code>true</code> if total number of entities
   **                            in the desired result set needs to be
   **                            estimated; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   ** @param  filter             the {@link SearchFilter} criteria to apply.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   ** @param  order              the {@link SortOption} to apply.
   **                            <br>
   **                            Allowed object is {@link SortOption}.
   **
   ** @return                    the <code>SearchRequest</code> requesting the
   **                            first search result.
   **                            <br>
   **                            Possible object is <code>SearchRequest</code>.
   */
  public final static SearchRequest first(final Boolean total, final SearchFilter filter, final SortOption order) {
    return first(DEFAULT_COUNT, total, filter, order);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>SearchRequest</code> returning the
   ** <code>SearchRequest</code> requesting of given <code>start</code> and
   ** <code>count</code>.
   **
   ** @param  start              the 0-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  count              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  filter             the {@link SearchFilter} criteria to apply.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the <code>SearchRequest</code> requesting the
   **                            first search result.
   **                            <br>
   **                            Possible object is <code>SearchRequest</code>.
   */
  public static final SearchRequest of(final Integer start, final Integer count, final SearchFilter filter) {
    return of(start, count, Boolean.FALSE, filter, SortOption.NONE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   first
  /**
   ** Factory method to create a new <code>SearchRequest</code> returning the
   ** <code>SearchRequest</code> requesting the first search result.
   **
   ** @param  count              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  total              <code>true</code> if total number of entities
   **                            in the desired result set needs to be
   **                            estimated; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   ** @param  filter             the {@link SearchFilter} criteria to apply.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   ** @param  order              the {@link SortOption} to apply.
   **                            <br>
   **                            Allowed object is {@link SortOption}.
   **
   ** @return                    the <code>SearchRequest</code> requesting the
   **                            first search result.
   **                            <br>
   **                            Possible object is <code>SearchRequest</code>.
   */
  public final static SearchRequest first(final Integer count, final Boolean total, final SearchFilter filter, final SortOption order) {
    return of(DEFAULT_START, count, total, filter, order);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>SearchRequest</code> for the given
   ** properties.
   **
   ** @param  start              the 0-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  count              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  total              <code>true</code> if total number of entities
   **                            in the desired result set needs to be
   **                            estimated; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   ** @param  filter             the {@link SearchFilter} criteria to apply.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   ** @param  order              the {@link SortOption} to apply.
   **                            <br>
   **                            Allowed object is {@link SortOption}.
   **
   ** @return                    the <code>SearchRequest</code> to apply on a
   **                            search operation.
   **                            <br>
   **                            Possible object is  <code>SearchRequest</code>.
   */
  public static final SearchRequest of(final Integer start, final Integer count, final Boolean total, final SearchFilter filter, final SortOption order) {
    return new SearchRequest(start, count, total, filter, order);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   previous
  /**
   ** Move a <code>SearchRequest</code> returning the previous chunk of a result
   ** set.
   **
   ** @return                    <code>true</code> a next previous chunk can
   **                            accessed from this one.
   **                            <br>
   **                            Possible object is  <code>SearchRequest</code>.
   */
  public final boolean previous() {
    this.start -= this.count;
    return hasPrevious();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>SearchRequest</code> returning the
   ** <code>SearchRequest</code> requesting of given <code>start</code> and
   ** <code>count</code>.
   **
   ** @param  start              the 0-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  count              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>SearchRequest</code> requesting the
   **                            first search result.
   **                            <br>
   **                            Possible object is <code>SearchRequest</code>.
   */
  private static final SearchRequest of(final Integer start, final Integer count) {
    return of(start, count, Boolean.FALSE, null, SortOption.NONE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateInteger
  private static int validateInteger(final String argument, final Integer value, int defaultValue, int minValue) {
    if (value == null)
      return defaultValue;
    else if (value < minValue)
      throw new IllegalArgumentException("Argument '" + argument + "' may not be less than " + minValue);
    else
      return value;
  }
}
