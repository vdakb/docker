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

    File        :   SearchResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SearchResponse.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.response;

import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;

import java.net.URI;

import java.io.IOException;

import oracle.iam.platform.scim.SearchControl;
import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.BadRequestException;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.entity.Path;
import oracle.iam.platform.scim.entity.Filter;
import oracle.iam.platform.scim.entity.FilterEvaluator;

import oracle.iam.platform.scim.schema.Generic;
import oracle.iam.platform.scim.schema.Resource;

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
public class SearchResponse<T extends Resource> extends ListResponseStream<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Long                 total;
  private final Integer              start;
  private final Integer              count;
  private final Filter               filter;
  private final List<Resource>       resource = new LinkedList<Resource>();
  private final FilterEvaluator      evaluator;
  private final Preparer<Resource>   preparator;
  private final Comparator<Resource> comparator;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>SearchResponse</code> for results from a search
   ** operation.
   **
   ** @param  type               the resource type definition of result
   **                            resources.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  total              the estimated total results of the result.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  start              the 1-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  count              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  filter             the filter used to request a subset of
   **                            resources.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  sort               the attribute pathes whose value
   **                            shall be used to order the returned responses
   **                            or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Path}.
   ** @param  order              the order in which the sort parameter is
   **                            applied or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Order}.
   ** @param attribute           the collection of attribute to take in account.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param exclude             <code>true</code> if the collection of
   **                            attribute to take in account is excluded.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   ** @param  baseUri            the resource type base URI.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @throws BadRequestException if an attribute path specified by
   **                             <code>attributes</code> and
   **                             <code>excludedAttributes</code> is invalid.
   */
  @SuppressWarnings("unchecked")
  private SearchResponse(final ResourceTypeDefinition type, final Long total, final Integer start, final Integer count, final Filter filter, final Path sortBy, SearchControl.Order order, final Set<Path> attribute, final boolean exclude, final URI baseUri) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.total      = total;
    this.start      = (start == null) ? 1 : start;
    this.count      = count;
    this.filter     = filter;
    this.evaluator  = FilterEvaluator.build(type);
    this.preparator = Preparer.<Resource>build(type, attribute, exclude, baseUri);
    if(sortBy != null) {
      this.comparator = Comparator.<Resource>build(type, sortBy, order);
    }
    else {
      this.comparator = null;
    }
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
   **
   ** @param  writer             the list response writer stream used to stream
   **                            back elements of the list response.
   **                            <br>
   **                            Allowed object is {@link ListResponseWriter}.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void write(final ListResponseWriter writer)
    throws IOException {

    if (this.comparator != null) {
      Collections.sort(this.resource, this.comparator);
    }
    List<Resource> result = this.resource;
    if (!result.isEmpty()) {
      result = result.subList(0, Math.min(this.count, result.size()));
    }
    writer.total(this.total == null ? result.size() : this.total);
    writer.start(this.start);
    writer.items(result.size());

    for (Resource resource : result) {
      writer.resource(this.preparator.retrieved(resource));
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
   ** @param  type               the resource type definition of result
   **                            resources.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  total              the estimated total results of the result.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  control            the control of the search.
   **                            <br>
   **                            Allowed object is {@link SearchControl}.
   ** @param  baseUri            the resource type base URI.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    an instance of
   **                            <code>SearchResponse</code> populated with the
   **                            values provided.
   **                            <br>
   **                            Possible object is <code>SearchResponse</code>.
   **
   ** @throws BadRequestException if an attribute path specified by
   **                             <code>attributes</code> and
   **                             <code>excludedAttributes</code> is invalid.
   */
  public static <T extends Resource> SearchResponse build(final ResourceTypeDefinition type, final Long total, final SearchControl control, final URI baseUri)
    throws BadRequestException {

    return new SearchResponse<T>(type, total, control.start(), control.count(), control.filter(), control.sort(), control.order(), control.attribute(), control.exclude(), baseUri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAll
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
   **
   ** @throws ProcessingException if an error occurs during filtering or setting
   **                             the meta attributes.
   */
  public SearchResponse addAll(final Collection<T> values)
    throws ProcessingException {

    for (T resource : values) {
      add(resource);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add a resource to include in the search results.
   **
   ** @param  resource           the resource to add.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the {@link SearchResponse} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   **
   ** @throws ProcessingException if an error occurs during filtering or setting
   **                             the meta attributes.
   */
  public SearchResponse add(final T resource)
    throws ProcessingException {

    // convert to Generic
    Generic generic;
    if (resource instanceof Generic) {
      // make a copy
      generic = Generic.build(((Generic)resource).objectNode().deepCopy());
    }
    else {
      generic = resource.generic();
    }

    // set meta attributes so they can be used in the following filter
    // evaluation
    this.preparator.locate(generic);
    if (this.filter == null || this.filter.accept(this.evaluator, generic.objectNode())) {
      this.resource.add(generic);
    }
    return this;
  }
}