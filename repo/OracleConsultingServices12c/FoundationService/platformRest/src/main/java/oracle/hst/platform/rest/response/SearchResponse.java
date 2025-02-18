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

    File        :   SearchResponse.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SearchResponse.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.response;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;

import java.io.IOException;

import oracle.hst.platform.rest.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class SearchResponse
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A utility {@link ListResponseStream} that will filter, sort, and paginate
 ** the search results for simple search implementations that always returns the
 ** entire result set.
 **
 ** @param  <T>                  the type of the contained resources.
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
public class SearchResponse<T> extends ListResponseStream<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Long    total;
  private final Integer start;
  private final Integer items;
  private final List<T> resource = new LinkedList<T>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>SearchResponse</code> for results from a search
   ** operation.
   **
   ** @param  total              the estimated total results of the result.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  start              the 1-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  items              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @throws BadRequestException if an attribute path specified by
   **                             <code>attributes</code> and
   **                             <code>excludedAttributes</code> is invalid.
   */
  private SearchResponse(final Long total, final Integer start, final Integer items) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.total = total;
    this.start = (start == null) ? 1 : start;
    this.items = items;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write (ListResponseStream)
  /**
   ** Start streaming the contents of the list response.
   ** <br>
   ** The list response will be considered complete upon return.
   **
   ** @param  writer             the list response writer stream used to stream
   **                            back elements of the list response.
   **                            <br>
   **                            Allowed object is {@link ListResponseWriter}
   **                            for type <code>T</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  @Override
  public void write(final ListResponseWriter<T> writer)
    throws IOException {

    List<T> result = this.resource;
    if (!result.isEmpty()) {
      result = result.subList(0, Math.min(this.items, result.size()));
    }
    writer.total(this.total == null ? result.size() : this.total);
    writer.start(this.start);
    writer.items(result.size());

    for (T cursor : result) {
      writer.resource(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>SearchResponse</code>.
   **
   ** @param  <T>                the implementation type of the
   **                            {@link Resource} to return in the response.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  total              the estimated total results of the result.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  start              the 1-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  items              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of
   **                            <code>SearchResponse</code> populated with the
   **                            values provided.
   **                            <br>
   **                            Possible object is <code>SearchResponse</code>.
   */
  public static <T> SearchResponse<T> build(final Long total, final Integer start, final Integer items) {
    return new SearchResponse<T>(total, start, items);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add a resource to include in the search results.
   **
   ** @param  value              the resource value to add.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the {@link SearchResponse} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   */
  public SearchResponse add(final T value) {
    this.resource.add(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add resources to include in the search results.
   **
   ** @param  values             the collection of resource to add.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the {@link SearchResponse} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   */
  public SearchResponse add(final Collection<T> values) {
    this.resource.addAll(values);
    return this;
  }
}