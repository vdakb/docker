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

    File        :   SearchControl.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SearchControl.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim;

import java.util.Set;
import java.util.LinkedHashSet;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

import oracle.hst.platform.core.utility.StringUtility;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

import oracle.iam.platform.scim.entity.Path;
import oracle.iam.platform.scim.entity.Filter;

import oracle.iam.platform.scim.request.SearchRequest;

////////////////////////////////////////////////////////////////////////////////
// class SearchControl
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Control to extend the functionality SCIM operation , such as
 ** <code>search</code> , to carry out additional operations on top of the
 ** search.
 ** <p>
 ** The <code>SearchControl</code> allows a client to request that the server
 ** send search results in small, manageable chunks within a specific range of
 ** entries. It also allows a client to move forward and backward through the
 ** results of a search operation, or jump directly to a particular entry.
 ** <p>
 ** <a name="emit"></a>
 ** <br>
 ** <b>Notes:</b>
 ** <br>
 ** The parameters <i>emitted</i> and <i>omitted</i> found in methods of this
 ** implementation are aimed at specifying the "attributes" and
 ** "excludedAttributes" query params regarded in section 3.9 of SCIM spec
 ** protocol document (RFC 7644).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SearchControl {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The default value applied to the 1-based index of the query result if
   ** there isn't a value specified in the query parameters.
   */
  public static final Integer DEFAULT_START = 1;
  /**
   ** The default value applied to the integer indicating the desired maximum
   ** number of query results per page if there isn't a value specified in the
   ** query parameters.
   */
  public static final Integer DEFAULT_COUNT = 500;
  /**
   ** The maximun value applied to the integer indicating the absolute maximum
   ** number of query results per page.
   */
  public static final Integer MAXIMUM_COUNT = 1000;

  /**
   ** The property name of the 1-based index of the first query result.
   */
  public static final String  START         = "startIndex";
  /**
   ** The property name of the integer indicating the desired maximum number of
   ** query results per page.
   */
  public static final String  COUNT         = "count";
  /**
   ** The property name of the filter string used to request a subset of
   ** resources.
   */
  public static final String  FILTER        = "filter";
  /**
   ** The property name of the multi-valued list of strings indicating the names
   ** of resource attributes to return in the response overriding the set of
   ** attributes that would be returned by default.
   */
  public static final String  EMITTED       = "attributes";
  /**
   ** The property name of the mulit-valued list of strings indicating the names
   ** of resource attributes to be removed from the default set of attributes to
   ** return.
   */
  public static final String  OMITTED       = "excludedAttributes";
  /**
   ** The property name of the string indicating the order in which the sortBy
   ** parameter is applied.
   */
  public static final String  ORDER         = "sortOrder";
  /**
   ** The property name of the string indicating the attribute whose value shall
   ** be used to order the returned responses.
   */
  public static final String  SORT          = "sortBy";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Integer       start;
  private final Integer       count;
  private final Filter        filter;

  private final boolean       exclude;
  private final Set<Path>     attribute;

  private final Path          sort;
  private final Order         order;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Order
  // ~~~~ ~~~~~
  /**
   ** The order in which the sortBy parameter is applied.
   */
  public static enum Order {
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
    // Method: of
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
    public static Order of(final String value) {
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
   ** Constructs a new <code>SearchControl</code> from the properties found in
   ** the {@link UriInfo} request.
   **
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
   */
  private SearchControl(final Integer start, final Integer count, final Filter filter, final Path sort, final Order order, final Set<Path> attribute, final boolean exclude) {
    // ensure inheritance
    super();

    // initalize instance attributes
    this.filter    = filter;
    this.start     = start;
    this.count     = count;
    this.sort      = sort;
    this.order     = order;
    this.attribute = attribute;
    this.exclude   = exclude;
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
   ** Returns the filter used to request a subset of resources.
   **
   ** @return                    the filter used to request a subset of
   **                            resources.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   */
  public final Filter filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exclude
  /**
   ** Whether the attribute are exclusions or not.
   **
   ** @return                    <code>true</code> if the collection of
   **                            attribute to take in account is excluded.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean exclude() {
    return this.exclude;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the collection of attribute to take in account.
   **
   ** @return                    the collection of attribute to take in account.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Path}.
   */
  public final Set<Path> attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sort
  /**
   ** Returns the attribute pathes whose value shall be used to order the
   ** returned responses.
   **
   ** @return                    the attribute pathes whose value
   **                            shall be used to order the returned responses
   **                            or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public final Path sort() {
    return this.sort;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   order
  /**
   ** Returns the order in which the sort parameter is applied
   **
   ** @return                    the order in which the sort parameter is
   **                            applied or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            Possible object is {@link Order}.
   */
  public final Order order() {
    return this.order;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>SearchControl</code> from the properties
   ** found in the {@link SearchRequest} specified.
   **
   ** @param  type               the resource type definition for resources to
   **                            prepare.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  request            the {@link SearchRequest} of the request to
   **                            build the respone for.
   **                            <br>
   **                            Allowed object is {@link SearchRequest}.
   **
   ** @return                    an instance of <code>SearchControl</code>
   **                            populated with the values provided.
   **                            <br>
   **                            Possible object is <code>SearchControl</code>.
   **
   ** @throws BadRequestException if an attribute path specified by
   **                             <code>attributes</code> and
   **                             <code>excludedAttributes</code> is invalid.
   */
  public static final SearchControl build(final ResourceTypeDefinition type, final SearchRequest request) 
    throws BadRequestException {

    return build(type, request.start(), request.count(), filter(request.filter()), sortBy(request.sort()), request.order(), request.emitted().toArray(new String[0]), request.omitted().toArray(new String[0]));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>SearchControl</code> from the properties
   ** found in the {@link UriInfo} request.
   **
   ** @param  type               the resource type definition for resources to
   **                            prepare.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  request            the {@link UriInfo} of the request to build the
   **                            respone for.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an instance of <code>SearchControl</code>
   **                            populated with the values provided.
   **                            <br>
   **                            Possible object is <code>SearchControl</code>.
   **
   ** @throws BadRequestException if an attribute path specified by
   **                             <code>attributes</code> and
   **                             <code>excludedAttributes</code> is invalid.
   */
  public static final SearchControl build(final ResourceTypeDefinition type, final UriInfo request)
    throws BadRequestException {

    final MultivaluedMap<String, String> query = request.getQueryParameters();
    return build(type, start(query), count(query), filter(query), sortBy(query), order(query), StringUtility.csv(query.getFirst(EMITTED)), StringUtility.csv(query.getFirst(OMITTED)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>SearchControl</code> from the properties
   ** found in the {@link UriInfo} request.
   **
   ** @param  type               the resource type definition for resources to
   **                            prepare.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
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
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param  sortBy             the attribute pathes whose value
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
   ** @param  emitted            a multi-valued list of strings indicating the
   **                            names of resource attributes to return in the
   **                            response, overriding the set of attributes that
   **                            would be returned by default.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   ** @param  omitted            a multi-valued list of strings indicating the
   **                            names of resource attributes to be removed from
   **                            the default set of attributes to return.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    an instance of <code>SearchControl</code>
   **                            populated with the values provided.
   **                            <br>
   **                            Possible object is <code>SearchControl</code>.
   **
   ** @throws BadRequestException if an attribute path specified by
   **                             <code>emitted</code> or <code>omitted</code>
   **                             is invalid.
   */
  public static final SearchControl build(final ResourceTypeDefinition type, final Integer start, final Integer count, final Filter filter, final Path sortBy, final Order order, final String[] emitted, final String[] omitted)
    throws BadRequestException {

    boolean   exclude   = false;
    Set<Path> attribute = null;
    if (emitted.length > 0) {
      attribute = new LinkedHashSet<Path>(emitted.length);
      for (String cursor : emitted) {
        // draft-ietf-scim-api section 3.9 states "The query parameter
        // attributes value is a comma-separated list of resource attribute
        // names in standard attribute notation (Section 3.10) form (e.g.,
        // userName, name, emails).
        // Note: filters on attributes like email[type eq "work"] not supported
        final Path normalized = type.normalizePath(Path.from(cursor)).withoutFilters();
        attribute.add(normalized);
      }
      exclude = false;
    }
    else if (omitted.length > 0) {
      attribute = new LinkedHashSet<Path>(omitted.length);
      for (String cursor : omitted) {
        // draft-ietf-scim-api section 3.9 states "The query parameter
        // attributes value is a comma-separated list of resource attribute
        // names in standard attribute notation (Section 3.10) form (e.g.,
        // userName, name, emails).
        // Note: filters on attributes like email[type eq "work"] not supported
        final Path normalized = type.normalizePath(Path.from(cursor)).withoutFilters();
        attribute.add(normalized);
      }
      exclude = true;
    }
    return new SearchControl(start, count, filter, sortBy, order, attribute, exclude);
  }

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
  private static Integer start(final MultivaluedMap<String, String> query)
    throws BadRequestException {

    final String value = parameter(query, START);
    if (value == null)
      return DEFAULT_START;

    final Integer start = Integer.valueOf(value);
    // 3.4.2.4: A value less than 1 SHALL be interpreted as 1.
    return start < DEFAULT_START ? DEFAULT_START : start;
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
  private static Integer count(final MultivaluedMap<String, String> query)
    throws BadRequestException {

    final String value = parameter(query, COUNT);
    if (value == null)
      return DEFAULT_COUNT;

    final Integer count = Integer.valueOf(value);
    // 3.4.2.4: A negative value SHALL be interpreted as 0.
    return count < 0 ? 0 : (count > MAXIMUM_COUNT) ? MAXIMUM_COUNT : count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the filter option from the query parameters.
   **
   ** @return                    the filter used to request a subset of
   **                            resources.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   */
  private static Filter filter(final MultivaluedMap<String, String> query)
    throws BadRequestException {

    return filter(parameter(query, FILTER));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the filter option from the string value.
   **
   ** @param  value              the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the filter used to request a subset of
   **                            resources.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  private static Filter filter(final String value)
    throws BadRequestException {

    return value == null ? null : Filter.from(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortBy
  /**
   ** Returns the attribute pathes whose value shall be used to order the
   ** returned responses from the query parameters.
   **
   ** @return                    the attribute pathes whose value
   **                            shall be used to order the returned responses
   **                            or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  private static Path sortBy(final MultivaluedMap<String, String> query)
    throws BadRequestException {

    return sortBy(parameter(query, SORT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortBy
  /**
   ** Returns the attribute pathes whose value shall be used to order the
   ** returned responses from the query parameters.
   **
   ** @param  value              the string representation of a path.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the attribute pathes whose value
   **                            shall be used to order the returned responses
   **                            or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  private static Path sortBy(final String value)
    throws BadRequestException {

    try {
      return value == null ? null : Path.from(value);
    }
    catch (BadRequestException e) {
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PARAMETER_SORT_INVALID_VALUE, value, e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   order
  /**
   ** Returns the attribute pathes whose value shall be used to order the
   ** returned responses from the query parameters.
   **
   ** @return                    the attribute pathes whose value
   **                            shall be used to order the returned responses
   **                            or <code>null</code> if sorting is not
   **                            required.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  private static Order order(final MultivaluedMap<String, String> query)
    throws BadRequestException {

    final String value = parameter(query, ORDER);
    try {
      return (value == null) ? Order.ASCENDING : Order.of(value);
    }
    catch (IllegalArgumentException e) {
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PARAMETER_SORT_INVALID_VALUE, value, e.getMessage()));
    }
  }

  private static String parameter(final MultivaluedMap<String, String> query, final String parameter) {
    return query.getFirst(parameter);   
  }
}