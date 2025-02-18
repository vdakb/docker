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

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   Endpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Endpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.api;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import java.text.ParseException;

import javax.json.JsonObject;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;

import oracle.hst.platform.core.entity.Or;
import oracle.hst.platform.core.entity.And;
import oracle.hst.platform.core.entity.Not;
import oracle.hst.platform.core.entity.Path;
import oracle.hst.platform.core.entity.Filter;

import oracle.hst.platform.core.marshal.JsonTrimmer;

import oracle.hst.platform.core.utility.StringUtility;

import oracle.hst.platform.jpa.SearchFilter;
import oracle.hst.platform.jpa.SortOption;
import oracle.hst.platform.jpa.SearchRequest;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;
import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.ForbiddenException;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.ResourceConflictException;

import oracle.hst.platform.rest.response.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class Endpoint
// ~~~~~ ~~~~~~~~
/**
 ** A generic handler which is invoked to process http exchanges.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The property name of the 1-based index of the first query result.
   */
  public static final String  START   = "startIndex";
  /**
   ** The property name of the integer indicating the desired maximum number of
   ** query results per page.
   */
  public static final String  COUNT   = "count";
  /**
   ** The property name of the filter string used to request a subset of
   ** resources.
   */
  public static final String  FILTER  = "filter";
  /**
   ** The property name of the multi-valued list of strings indicating the names
   ** of resource attributes to return in the response overriding the set of
   ** attributes that would be returned by default.
   */
  public static final String  EMITTED = "attributes";
  /**
   ** The property name of the mulit-valued list of strings indicating the names
   ** of resource attributes to be removed from the default set of attributes to
   ** return.
   */
  public static final String  OMITTED = "excludedAttributes";
  /**
   ** The property name of the string indicating the order in which the sortBy
   ** parameter is applied.
   */
  public static final String  ORDER   = "sortOrder";
  /**
   ** The property name of the string indicating the attribute whose value shall
   ** be used to order the returned responses.
   */
  public static final String  SORT    = "sortBy";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Endpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Endpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   ** Factory method to create a {@link SearchRequest} from the given request
   ** query <code>contet</code>.
   **
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the JPA {@link SearchRequest} parsed from the
   **                            query part of an HTTP request.
   **                            Possible object is {@link SearchRequest}.
   **
   ** @throws BadRequestException if the value for parameter
   **                             <code>startIndex</code> or <code>count</code>
   **                             isn't evaluated as a integer number.
   */
  protected static SearchRequest searchRequest(final UriInfo context)
    throws BadRequestException {

    final MultivaluedMap<String, String> query = context.getQueryParameters();
    try {
      return SearchRequest.of(
       // JPA's start index is 0-base but the REST API should look like SCIM
      // hence the start index is 1-based
        start(query)
      , count(query)
      , Boolean.TRUE
      , translate(Filter.from(filter(query)))
      , SortOption.by(SortOption.Order.by(sortBy(query), order(query)))
      );
    }
    catch (ParseException e) {
      throw (BadRequestException.invalidFilter(e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceTrimmer
  /**
   ** Factory method to create a {@link SearchRequest} from the given request
   ** query <code>contet</code>.
   **
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the JPA {@link SearchRequest} parsed from the
   **                            query part of an HTTP request.
   **                            Possible object is {@link SearchRequest}.
   **
   ** @throws BadRequestException if the value for parameter
   **                             <code>startIndex</code> or <code>count</code>
   **                             isn't evaluated as a integer number.
   */
  protected static JsonTrimmer resourceTrimmer(final UriInfo context)
    throws BadRequestException {

    final MultivaluedMap<String, String> query   = context.getQueryParameters();
    final String[]                       emitted = StringUtility.csv(query.getFirst(EMITTED));
    final String[]                       omitted = StringUtility.csv(query.getFirst(OMITTED));
    boolean   exclude   = false;
    Set<Path> attribute = null;
    if (emitted.length > 0) {
      attribute = new LinkedHashSet<Path>(emitted.length);
      for (String cursor : emitted) {
        attribute.add(Path.build(cursor));
      }
      exclude = false;
    }
    else if (omitted.length > 0) {
      attribute = new LinkedHashSet<Path>(omitted.length);
      for (String cursor : omitted) {
        attribute.add(Path.build(cursor));
      }
      exclude = true;
    }
    return JsonTrimmer.build(attribute, exclude);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Returns the 1-based index of the first search result.
   **
   ** @param  query              the {@link MultivaluedMap} representing the
   **                            query part of an HTTP request.
   **                            <br>
   **                            Allowed object is {@link MultivaluedMap} where
   **                            each element is of type {@link String} for the
   **                            key and {@link String} as the value.
   **
   ** @return                    the 1-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Possible object is {@link Integer}.
   **
   ** @throws BadRequestException if the value for parameter
   **                             <code>startIndex</code> isn't evaluated as a
   **                             integer number.
   */
  protected static Integer start(final MultivaluedMap<String, String> query)
    throws BadRequestException {

    final String value = parameter(query, START);
    if (value == null)
      return SearchRequest.DEFAULT_START;

    try {
      final Integer start = Integer.valueOf(value);
      // 3.4.2.4: A value less than 1 SHALL be interpreted as 1.
      // keep in mind JPA is 0-based REST is 1-based
      return start < 1 ? SearchRequest.DEFAULT_START : start - 1;
    }
    catch (NumberFormatException e) {
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PARAMETER_START_INVALID_VALUE, value, e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Returns the desired maximum number of search results per page.
   **
   ** @param  query              the {@link MultivaluedMap} representing the
   **                            query part of an HTTP request.
   **                            <br>
   **                            Allowed object is {@link MultivaluedMap} where
   **                            each element is of type {@link String} for the
   **                            key and {@link String} as the value.
   **
   ** @return                    the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Possible object is {@link Integer}.
   **
   ** @throws BadRequestException if the value for parameter <code>count</code>
   **                             isn't evaluated as a integer number.
   */
  protected static Integer count(final MultivaluedMap<String, String> query)
    throws BadRequestException {

    final String value = parameter(query, COUNT);
    if (value == null)
      return SearchRequest.DEFAULT_COUNT;

    try {
      final Integer count = Integer.valueOf(value);
      // 3.4.2.4: A negative value SHALL be interpreted as 0.
      return count < 0 ? 0 : count;
    }
    catch (NumberFormatException e) {
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PARAMETER_COUNT_INVALID_VALUE, value, e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the attribute path whose value shall be used to request a subset
   ** of resources from the query parameters.
   **
   ** @param  query              the {@link MultivaluedMap} representing the
   **                            query part of an HTTP request.
   **                            <br>
   **                            Allowed object is {@link MultivaluedMap} where
   **                            each element is of type {@link String} for the
   **                            key and {@link String} as the value.
   **
   ** @return                    the attribute path whose value shall be used to
   **                            request a subset of resources from the query
   **                            parameters or <code>null</code> if filtering is
   **                            not required.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected static String filter(final MultivaluedMap<String, String> query) {
    return parameter(query, FILTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortBy
  /**
   ** Returns the attribute pathes whose value shall be used to order the
   ** returned responses from the query parameters.
   **
   ** @param  query              the {@link MultivaluedMap} representing the
   **                            query part of an HTTP request.
   **                            <br>
   **                            Allowed object is {@link MultivaluedMap} where
   **                            each element is of type {@link String} for the
   **                            key and {@link String} as the value.
   **
   ** @return                    the attribute pathes whose value
   **                            shall be used to order the returned responses
   **                            or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected static String sortBy(final MultivaluedMap<String, String> query) {
    return parameter(query, SORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   order
  /**
   ** Returns the attribute pathes whose value shall be used to order the
   ** returned responses from the query parameters.
   **
   ** @param  query              the {@link MultivaluedMap} representing the
   **                            query part of an HTTP request.
   **                            <br>
   **                            Allowed object is {@link MultivaluedMap} where
   **                            each element is of type {@link String} for the
   **                            key and {@link String} as the value.
   **
   ** @return                    the attribute pathes whose value
   **                            shall be used to order the returned responses
   **                            or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link SortOption.Direction}.
   **
   ** @throws BadRequestException if the obtained parameter
   **                             <code>sortOrder</code> is not in the permitted
   **                             range.
   */
  protected static SortOption.Direction order(final MultivaluedMap<String, String> query)
    throws BadRequestException {

    final String value = parameter(query, ORDER);
    try {
      return (value == null) ? null : SortOption.Direction.from(value);
    }
    catch (IllegalArgumentException e) {
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PARAMETER_SORT_INVALID_VALUE, value, e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   success
  /**
   ** Factory method to create a new <code>sucess</code> {@link Response}.
   **
   ** @param  payload             the JSON payload of the response.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the {@link Response} wrapping the
   **                            HTTP-200 response status.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  protected static Response success(final JsonObject payload) {
    return Response.status(Response.Status.OK).entity(payload).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forbidden
  /**
   ** Factory method to create a new <code>forbidden</code> {@link Response}.
   **
   ** @return                    the {@link ForbiddenException} wrapping the
   **                            HTTP-403 response status.
   **                            <br>
   **                            Possible object is {@link ForbiddenException}.
   */
  protected static ForbiddenException forbidden() {
    return ForbiddenException.notPermitted(EndpointBundle.string(EndpointError.OPERATION_NOT_PERMITTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   conflict
  /**
   ** Factory method to create a new <code>uniqueness</code> violation wrapped
   ** in a {@link ResourceConflictException} with the resource identifier
   ** <code>resource</code> and the entity identifier <code>identifier</code>
   ** (usually the primary key of entity).
   **
   ** @param  resource           the resource identifier to which the
   **                            {@link Response} belongs.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the entity identifier to which the
   **                            {@link Response} belongs.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the {@link ResourceConflictException} wrapping
   **                            the HTTP-409 response status.
   **                            <br>
   **                            Possible object is
   **                            {@link ResourceConflictException}.
   */
  protected static ResourceConflictException conflict(final String resource, final String identifier) {
    return ResourceConflictException.of(ServiceBundle.string(ServiceError.RESOURCE_EXISTS, identifier, resource));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notFound
  /**
   ** Factory method to create a new <code>NotFound</code> error wrapped in a
   ** {@link NotFoundException} with the resource identifier
   ** <code>resource</code> and the entity identifier <code>identifier</code>
   ** (usually the primary key of entity).
   **
   ** @param  resource           the resource identifier to which the
   **                            {@link Response} belongs.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the entity identifier to which the
   **                            {@link Response} belongs.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the {@link NotFoundException} wrapping the
   **                            HTTP-404 response status.
   **                            <br>
   **                            Possible object is {@link NotFoundException}.
   */
  protected static NotFoundException notFound(final String resource, final String identifier) {
    return NotFoundException.of(resource, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutability
  /**
   ** Factory method to create a new <code>mutability</code> iolation wrapped in
   ** a {@link BadRequestException}.
   **
   ** @return                    the {@link BadRequestException} wrapping the
   **                            HTTP-400 response status.
   **                            <br>
   **                            Possible object is {@link BadRequestException}.
   */
  protected static BadRequestException mutability() {
    return BadRequestException.mutability(EndpointBundle.string(EndpointError.OPERATION_NOT_PERMITTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorResponse
  /**
   ** Forms a JAX-RS {@link Response} from the specified {@link ErrorResponse}.
   **
   ** @param  error              the {@link ErrorResponse} send back to the
   **                            client.
   **                            <br>
   **                            Allowed object is {@link ErrorResponse}.
   **
   ** @return                    the {@link Response} wrapping the response as
   **                            an entity.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  protected static Response errorResponse(final ErrorResponse error) {
    return Response.status(error.status()).entity(error).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Retruns the first value of the supplied parameter <code>name</code>.
   **
   ** @param  query              the query parameter applied on a request.
   **                            <br>
   **                            Allowed object is {@link MultivaluedMap} where
   **                            each element is of type {@link String} as the
   **                            key and {@link String} for the value.
   ** @param  name               the name of the parameter to return the value
   **                            for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @return                    the first value for the specified
   **                            parameter <code>name</code> or
   **                            <code>null</code> if the parameter
   **                            <code>name</code> is not in the map.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected static String parameter(final MultivaluedMap<String, String> query, final String name) {
    return query.getFirst(name);   
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translate
  /**
   ** Factory method to create a {@link SearchFilter} applicable as restrictions
   ** on a search.
   **
   ** @param  filter             the SCIM {@link Filter} providing the search
   **                            criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the resulting {@link SearchFilter}.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   */
  @SuppressWarnings("unchecked")
  protected static SearchFilter translate(final Filter filter) {
    // prevent bogus input
    if (filter == null)
      return SearchFilter.NOP;

    final Object value = filter.value();
    switch (filter.type()) {
      case OR  : return disjunction(((Or)filter).filter());
      case AND : return conjunction(((And)filter).filter());
      case NOT : return SearchFilter.not(translate(((Not)filter).filter()));

      case SW  : return SearchFilter.sw(filter.path().segment(0).attribute(), (String)value);
      case EW  : return SearchFilter.ew(filter.path().segment(0).attribute(), (String)value);
      case CO  : return SearchFilter.co(filter.path().segment(0).attribute(), (String)value);
      case LE  : if (value instanceof Number) {
                  return SearchFilter.le(filter.path().segment(0).attribute(), (Number)value);
                 }
                 throw new IllegalArgumentException();
      case LT  : if (value instanceof Number) {
                   return SearchFilter.lt(filter.path().segment(0).attribute(), (Number)value);
                 }
                 throw new IllegalArgumentException();
      case GE  : if (value instanceof Number) {
                   return SearchFilter.ge(filter.path().segment(0).attribute(), (Number)value);
                 }
                 throw new IllegalArgumentException();
      case GT  : if (value instanceof Number) {
                   return SearchFilter.gt(filter.path().segment(0).attribute(), (Number)value);
                 }
                 throw new IllegalArgumentException();
      default  : if (value instanceof Boolean) {
                   return SearchFilter.eq(filter.path().segment(0).attribute(), (Boolean)value);
                 }
                 else if (value instanceof Number) {
                   return SearchFilter.eq(filter.path().segment(0).attribute(), (Number)value);
                 }
                 else if (value instanceof String) {
                   return SearchFilter.eq(filter.path().segment(0).attribute(), (String)value);
                 }
      throw new IllegalArgumentException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   conjunction
  /**
   ** Logically "ANDs" together the instances of an {@link And} {@link Filter}.
   ** <br>
   ** The resulting <i>conjunct</i> {@link SearchFilter} is <code>true</code> if
   ** and only if all of the specified filters are <code>true</code>.
   **
   ** @param  filter             the {@link And} {@link Filter} to transform to
   **                            a {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Filter}.
   **
   ** @return                    the resulting <i>conjunct</i>
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   */
  protected static SearchFilter<?> conjunction(final List<Filter> filter) {
    // FIXME: not compilable ???
    // return SearchFilter.and(Streams.stream(filter).filter(f -> f != null).map(f -> translate(f)).collect(Collectors.toList()));
    final List<SearchFilter<?>> collector = new ArrayList<SearchFilter<?>>(filter.size());
    for (Filter cursor : filter) {
      collector.add(translate(cursor));
    }
    return SearchFilter.and(collector);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disjunction
  /**
   ** Logically "ORs" together the instances of an {@link Or} {@link Filter}.
   ** <br>
   ** The resulting <i>disjunct</i> {@link SearchFilter} is <code>true</code> if
   ** and only if at least one of the specified filters is <code>true</code>.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Filter}.
   **
   ** @return                    the resulting <i>disjunct</i>
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   */
  protected static SearchFilter disjunction(final List<Filter> filter) {
    // FIXME: not compilable ???
    // return SearchFilter.or(Streams.stream(filter).filter(f -> f != null).map(f -> translate(f)).collect(Collectors.toList()));
    final List<SearchFilter<?>> collector = new ArrayList<SearchFilter<?>>(filter.size());
    for (Filter cursor : filter) {
      collector.add(translate(cursor));
    }
    return SearchFilter.or(collector);
  }
}