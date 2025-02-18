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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   SearchResponseStream.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SearchResponseStream.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.response;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;

import java.io.IOException;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MultivaluedMap;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.BadRequestException;

import oracle.iam.system.simulation.ServiceException;
import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.schema.Resource;
import oracle.iam.system.simulation.scim.schema.GenericResource;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.domain.SearchRequest;

import oracle.iam.system.simulation.scim.utility.SchemaFilterEvaluator;
import oracle.iam.system.simulation.scim.v2.ResourceTypeDefinition;

////////////////////////////////////////////////////////////////////////////////
// class SearchResponseStream
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A utility {@link ListResponseStream} that will filter, sort, and paginate
 ** the search results for simple search implementations that always returns the
 ** entire result set.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SearchResponseStream<T extends Resource> extends ListResponseStream<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Integer                      start;
  private final Integer                      count;
  private final Filter                       filter;
  private final List<Resource>               resource = new LinkedList<Resource>();
  private final SchemaFilterEvaluator        evaluator;
  private final ResourcePreparer<Resource>   preparator;
  private final ResourceComparator<Resource> comparator;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>SearchResponseStream</code> for results from a
   ** search operation.
   **
   ** @param  resourceType       the resource type definition of result
   **                            resources.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  request            the UriInfo from the search operation.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @throws BadRequestException if an attribute path specified by attributes
   **                             and excludedAttributes is invalid.
   */
  public SearchResponseStream(final ResourceTypeDefinition resourceType, final UriInfo request)
    throws BadRequestException {

    // ensure inheritance
    super();

    final MultivaluedMap<String, String> query = request.getQueryParameters();

    final String filterString     = null;
    final String startIndexString = "";   // query.getFirst(
    final String countString      = "50"; //query.getFirst(QUERY_PARAMETER_PAGE_SIZE);
    final String sortByString     = "";   //query.getFirst(QUERY_PARAMETER_SORT_BY);
    final String sortOrderString  = "";   //query.getFirst(QUERY_PARAMETER_SORT_ORDER);

    if (filterString != null) {
      this.filter = Filter.from(filterString);
    }
    else {
      this.filter = null;
    }
    if (startIndexString != null) {
      int i = Integer.valueOf(startIndexString);
      // 3.4.2.4: A value less than 1 SHALL be interpreted as 1.
      this.start = i < 1 ? 1 : i;
    }
    else {
      this.start = null;
    }
    if (countString != null) {
      int i = Integer.valueOf(countString);
      // 3.4.2.4: A negative value SHALL be interpreted as 0.
      this.count = i < 0 ? 0 : i;
    }
    else {
      this.count = null;
    }
    Path sortBy;
    try {
      sortBy = sortByString == null ? null : Path.from(sortByString);
    }
    catch (BadRequestException e) {
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PARAMETER_SORT_INVALID_VALUE, sortByString, e.getMessage()));
    }
    SearchRequest.Order order = sortOrderString == null ? SearchRequest.Order.ASCENDING : SearchRequest.Order.fromValue(sortOrderString);
    if(sortBy != null) {
      this.comparator = new ResourceComparator<Resource>(sortBy, order, resourceType);
    }
    else {
      this.comparator = null;
    }
    this.evaluator  = new SchemaFilterEvaluator(resourceType);
    this.preparator = new ResourcePreparer<Resource>(resourceType, request);
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
  public void write(final ListResponseWriter<T> writer)
    throws IOException {

    if (this.comparator != null) {
      Collections.sort(this.resource, this.comparator);
    }
    List<Resource> result = this.resource;
    if (this.start != null) {
      if (this.start > this.resource.size()) {
        result = Collections.emptyList();
      }
      else {
        result = this.resource.subList(this.start - 1, this.resource.size());
      }
    }
    if (this.count != null && !result.isEmpty()) {
      result = result.subList(0, Math.min(count, result.size()));
    }
    writer.totalResults(this.resource.size());
    if (this.start != null || this.count != null) {
      writer.startIndex(this.start == null ? 1 : this.start);
      writer.itemsPerPage(result.size());
    }
    for (Resource resource : result) {
      writer.resource((T)this.preparator.trimRetrieved(resource));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  ////////////////////////////////////////////////////////////////////////////

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
   ** @return                    the {@link SearchResponseStream} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link SearchResponseStream}.
   **
   ** @throws ServiceException   if an error occurs during filtering or setting
   **                            the meta attributes.
   */
  public SearchResponseStream addAll(final Collection<T> values)
    throws ServiceException {

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
   ** @return                    the {@link SearchResponseStream} to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            {@link SearchResponseStream}.
   **
   ** @throws ServiceException   if an error occurs during filtering or setting
   **                            the meta attributes.
   */
  public SearchResponseStream add(final T resource)
    throws ServiceException {

    // Convert to GenericScimResource
    GenericResource generic;
    if (resource instanceof GenericResource) {
      // Make a copy
      generic = new GenericResource(((GenericResource)resource).objectNode().deepCopy());
    }
    else {
      generic = resource.generic();
    }

    // set meta attributes so they can be used in the following filter eval
    this.preparator.resourceTypeLocation(generic);

    if (this.filter == null) {// || this.filter.visit(this.evaluator, generic.objectNode())) {
      this.resource.add(generic);
    }
    return this;
  }
}
