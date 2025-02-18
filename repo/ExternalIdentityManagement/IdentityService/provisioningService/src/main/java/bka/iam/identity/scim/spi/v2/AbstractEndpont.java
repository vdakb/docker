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
    Subsystem   :   Identity Governance Provisioning

    File        :   AbstractEndpont.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractEndpont.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.spi.v2;

import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collection;

import java.text.ParseException;

import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.node.ValueNode;

import oracle.hst.platform.core.utility.DateUtility;

import oracle.hst.platform.core.function.CheckedFunction;

import oracle.hst.platform.jpa.SortOption;
import oracle.hst.platform.jpa.SearchFilter;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

import oracle.iam.platform.scim.entity.Or;
import oracle.iam.platform.scim.entity.And;
import oracle.iam.platform.scim.entity.Not;
import oracle.iam.platform.scim.entity.Path;
import oracle.iam.platform.scim.entity.Filter;

import oracle.iam.platform.scim.SearchControl;
import oracle.iam.platform.scim.NotFoundException;
import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.BadRequestException;
import oracle.iam.platform.scim.ResourceConflictException;

import oracle.iam.identity.scim.api.ResourceService;

import oracle.iam.platform.scim.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class AbstractEndpont
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A JAX-RS resource implementation of an endpoint that provides filter
 ** capabilties on search API's.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AbstractEndpont<T extends Resource> implements ResourceService<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The regular expression to detect a date value
   ** <p>
   ** Singleton Pattern
   */
  private static final Pattern DATE_PATTERN = Pattern.compile("^[0-9,-]*T[0-9,:]*Z$");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractEndpont</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractEndpont() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the resulting {@link SearchFilter}.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if the specified {@link Path} did not yield an
   **                             attribute that could be operated on.
   */
  @SuppressWarnings("unchecked")
  protected static SearchFilter translate(final Filter filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    switch (filter.type()) {
      case OR  : return disjunction(((Or)filter).filter(),  resolver);
      case AND : return conjunction(((And)filter).filter(), resolver);
      case NOT : return SearchFilter.not(translate(((Not)filter).filter(), resolver));

      case EQ  : return eq(filter, resolver);
      case LE  : return le(filter, resolver);
      case LT  : return lt(filter, resolver);
      case GE  : return ge(filter, resolver);
      case GT  : return gt(filter, resolver);
      case SW  : return sw(filter, resolver); //SearchFilter.sw(resolver.apply(filter.path()), filter.value().textValue());
      case EW  : return ew(filter, resolver); //SearchFilter.ew(resolver.apply(filter.path()), filter.value().textValue());
      case CO  : return co(filter, resolver); //SearchFilter.co(resolver.apply(filter.path()), filter.value().textValue());
      default  : throw BadRequestException.invalidFilter(filter.toString());
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
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the resulting <i>conjunct</i>
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if the specified {@link Path} did not yield an
   **                             attribute that could be operated on.
   */
  protected static SearchFilter<?> conjunction(final Collection<Filter> filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // FIXME: not compilable ???
    // return SearchFilter.and(Streams.stream(filter).filter(f -> f != null).map(f -> translate(f)).collect(Collectors.toList()));
    final List<SearchFilter<?>> collector = new ArrayList<SearchFilter<?>>(filter.size());
    for (Filter cursor : filter) {
      collector.add(translate(cursor, resolver));
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
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the resulting <i>disjunct</i>
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if the specified {@link Path} did not yield an
   **                             attribute that could be operated on.
   */
  protected static SearchFilter disjunction(final Collection<Filter> filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // FIXME: not compilable ???
    // return SearchFilter.or(Streams.stream(filter).filter(f -> f != null).map(f -> translate(f)).collect(Collectors.toList()));
    final List<SearchFilter<?>> collector = new ArrayList<SearchFilter<?>>(filter.size());
    for (Filter cursor : filter) {
      collector.add(translate(cursor, resolver));
    }
    return SearchFilter.or(collector);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translate
  /**
   ** Factory method to create a {@link SortOption} applicable as restrictions
   ** on a search.
   **
   ** @param  sortBy             the SCIM {@link Path} providing the search
   **                            criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  order              the order in which the sortBy parameter is
   **                            applied.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the resulting {@link SortOption}.
   **                            <br>
   **                            Possible object is {@link SortOption}.
   **
   ** @throws ProcessingException if the specified {@link Path} did not yield an
   **                             attribute that could be operated on.
   */
  protected static SortOption translate(final Path sortBy, final SearchControl.Order order, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    final String inbound = resolver.apply(sortBy);
    // convert the SCIM sort control in a JPA equivalent
    return (inbound == null) ? null : SortOption.by(order == SearchControl.Order.DESCENDING ? SortOption.Order.descending(inbound) : SortOption.Order.ascending(inbound));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create a new <code>equal to</code>
   ** {@link SearchFilter} that honors the specific type of the provided
   ** value node.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the {@link SearchFilter} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if <code>node</code> is not of the expected
   **                             type.
   */
  private static SearchFilter eq(final Filter filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // obtain the attribute mapping first to avoid pointless evaluation of the
    // filter criteria
    final String    attribute = resolver.apply(filter.path());
    final ValueNode nodeValue = filter.value();
    if (nodeValue.isBoolean()) {
      return SearchFilter.eq(attribute, nodeValue.booleanValue());
    }
    else if (nodeValue.isNumber()) {
      return SearchFilter.eq(attribute, nodeValue.numberValue());
    }
    else if (nodeValue.isTextual()) {
      final String value = nodeValue.textValue();
      // maybe we got a date representation
      if (isDate(value)) {
        try {
          return SearchFilter.eq(attribute, DateUtility.parseDate(value, DateUtility.XML8601_ZULU));
        }
        catch (ParseException e) {
          throw BadRequestException.invalidValue(value);
        }
      }
      else {
        return SearchFilter.eq(attribute, value);
      }
    }
    throw BadRequestException.invalidValue(nodeValue.toPrettyString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create a new <code>less than</code>
   ** {@link SearchFilter} that honors the specific type of the provided
   ** value node.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the {@link SearchFilter} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if <code>node</code> is not of the expected
   **                             type.
   */
  private static SearchFilter lt(final Filter filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // obtain the attribute mapping first to avoid pointless evaluation of the
    // filter criteria
    final String    attribute = resolver.apply(filter.path());
    final ValueNode nodeValue = filter.value();
    if (nodeValue.isNumber()) {
      return SearchFilter.lt(attribute, nodeValue.numberValue());
    }
    if (nodeValue.isTextual()) {
      final String value = nodeValue.textValue();
      // maybe we got a date representation
      if (isDate(value)) {
        try {
          return SearchFilter.lt(attribute, DateUtility.parseDate(value, DateUtility.XML8601_ZULU));
        }
        catch (ParseException e) {
          throw BadRequestException.invalidValue(value);
        }
      }
      else {
        return SearchFilter.lt(attribute, value);
      }
    }
    throw BadRequestException.invalidValue(nodeValue.toPrettyString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create a new <code>less than or equal to</code>
   ** {@link SearchFilter} that honors the specific type of the provided
   ** value node.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the {@link SearchFilter} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if <code>node</code> is not of the expected
   **                             type.
   */
  private static SearchFilter le(final Filter filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // obtain the attribute mapping first to avoid pointless evaluation of the
    // filter criteria
    final String    attribute = resolver.apply(filter.path());
    final ValueNode nodeValue = filter.value();
    if (nodeValue.isNumber()) {
      return SearchFilter.le(attribute, nodeValue.numberValue());
    }
    if (nodeValue.isTextual()) {
      final String value = nodeValue.textValue();
      // maybe we got a date representation
      if (isDate(value)) {
        try {
          return SearchFilter.le(attribute, DateUtility.parseDate(value, DateUtility.XML8601_ZULU));
        }
        catch (ParseException e) {
          throw BadRequestException.invalidValue(value);
        }
      }
      else {
        return SearchFilter.le(attribute, value);
      }
    }
    throw BadRequestException.invalidValue(nodeValue.toPrettyString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create a new <code>greater than</code>
   ** {@link SearchFilter} that honors the specific type of the provided
   ** value node.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the {@link SearchFilter} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if <code>node</code> is not of the expected
   **                             type.
   */
  private static SearchFilter gt(final Filter filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // obtain the attribute mapping first to avoid pointless evaluation of the
    // filter criteria
    final String    attribute = resolver.apply(filter.path());
    final ValueNode nodeValue = filter.value();
    if (nodeValue.isNumber()) {
      return SearchFilter.gt(attribute, nodeValue.numberValue());
    }
    if (nodeValue.isTextual()) {
      final String value = nodeValue.asText();
      // maybe we got a date representation
      if (isDate(value)) {
        try {
          return SearchFilter.gt(attribute, DateUtility.parseDate(value, DateUtility.XML8601_ZULU));
        }
        catch (ParseException e) {
          throw BadRequestException.invalidValue(value);
        }
      }
      else {
        return SearchFilter.gt(attribute, value);
      }
    }
    throw BadRequestException.invalidValue(nodeValue.toPrettyString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create a new <code>greater than or equal to</code>
   ** {@link SearchFilter} that honors the specific type of the provided
   ** value node.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the {@link SearchFilter} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if <code>node</code> is not of the expected
   **                             type.
   */
  private static SearchFilter ge(final Filter filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // obtain the attribute mapping first to avoid pointless evaluation of the
    // filter criteria
    final String    attribute = resolver.apply(filter.path());
    final ValueNode nodeValue = filter.value();
    if (nodeValue.isNumber()) {
      return SearchFilter.ge(attribute, nodeValue.numberValue());
    }
    if (nodeValue.isTextual()) {
      final String value = nodeValue.textValue();
      // maybe we got a date representation
      if (isDate(value)) {
        try {
          return SearchFilter.ge(attribute, DateUtility.parseDate(value, DateUtility.XML8601_ZULU));
        }
        catch (ParseException e) {
          throw BadRequestException.invalidValue(value);
        }
      }
      else {
        return SearchFilter.ge(attribute, value);
      }
    }
    throw BadRequestException.invalidValue(nodeValue.toPrettyString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create a new <code>starts with</code>
   ** {@link SearchFilter} that honors the specific type of the provided
   ** value node.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the {@link SearchFilter} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if <code>node</code> is not of the expected
   **                             type.
   */
  private static SearchFilter sw(final Filter filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // obtain the attribute mapping first to avoid pointless evaluation of the
    // filter criteria
    final String    attribute = resolver.apply(filter.path());
    final ValueNode nodeValue = filter.value();
    if (nodeValue.isNumber()) {
      return SearchFilter.sw(attribute, nodeValue.numberValue());
    }
    if (nodeValue.isTextual()) {
      final String value = nodeValue.textValue();
      // maybe we got a date representation
      if (isDate(value)) {
        try {
          return SearchFilter.sw(attribute, DateUtility.parseDate(value, DateUtility.XML8601_ZULU));
        }
        catch (ParseException e) {
          throw BadRequestException.invalidValue(value);
        }
      }
      else {
        return SearchFilter.sw(attribute, value);
      }
    }
    throw BadRequestException.invalidValue(nodeValue.toPrettyString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create a new <code>ends with</code>
   ** {@link SearchFilter} that honors the specific type of the provided
   ** value node.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the {@link SearchFilter} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if <code>node</code> is not of the expected
   **                             type.
   */
  private static SearchFilter ew(final Filter filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // obtain the attribute mapping first to avoid pointless evaluation of the
    // filter criteria
    final String    attribute = resolver.apply(filter.path());
    final ValueNode nodeValue = filter.value();
    if (nodeValue.isNumber()) {
      return SearchFilter.ew(attribute, nodeValue.numberValue());
    }
    if (nodeValue.isTextual()) {
      final String value = nodeValue.asText();
      // maybe we got a date representation
      if (isDate(value)) {
        try {
          return SearchFilter.ew(attribute, DateUtility.parseDate(value, DateUtility.XML8601_ZULU));
        }
        catch (ParseException e) {
          throw BadRequestException.invalidValue(value);
        }
      }
      else {
        return SearchFilter.ew(attribute, value);
      }
    }
    throw BadRequestException.invalidValue(nodeValue.toPrettyString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create a new <code>contains</code> {@link SearchFilter}
   ** that honors the specific type of the provided value node.
   **
   ** @param  filter             the {@link Or} {@link Filter} to transform to a
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   ** @param  resolver           the naming resolver applied to translate
   **                            the outbound naming convention of search
   **                            criteria to the internally used naming
   **                            convention.
   **                            <br>
   **                            The mapping key specifies the name of the
   **                            attribute as used in formulating the filter
   **                            criteria outside. The value of the mapping is
   **                            then the name of the attribute, as it is used
   **                            internally to formulate the filter criteria to
   **                            pass it to the persistenc layer.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the {@link SearchFilter} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   **
   ** @throws ProcessingException if <code>node</code> is not of the expected
   **                             type.
   */
  private static SearchFilter co(final Filter filter, final CheckedFunction<Path, String, ProcessingException> resolver)
    throws ProcessingException {

    // obtain the attribute mapping first to avoid pointless evaluation of the
    // filter criteria
    final String    attribute = resolver.apply(filter.path());
    final ValueNode nodeValue = filter.value();
    if (nodeValue.isNumber()) {
      return SearchFilter.co(attribute, nodeValue.numberValue());
    }
    if (nodeValue.isTextual()) {
      final String value = nodeValue.textValue();
      // maybe we got a date representation
      if (isDate(value)) {
        try {
          return SearchFilter.co(attribute, DateUtility.parseDate(value, DateUtility.XML8601_ZULU));
        }
        catch (ParseException e) {
          throw BadRequestException.invalidValue(value);
        }
      }
      else {
        return SearchFilter.ew(attribute, value);
      }
    }
    throw BadRequestException.invalidValue(nodeValue.toPrettyString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requiredValue
  /**
   ** Factory method to create a new <code>InvalidValue</code> error wrapped in
   ** a {@link BadRequestException} with the attribute <code>identifier</code>.
   **
   ** @param  identifier         the identifier of the attribute the value is
   **                            required for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link NotFoundException} wrapping the
   **                            HTTP-404 response status.
   **                            <br>
   **                            Possible object is {@link NotFoundException}.
   */
  protected static BadRequestException requiredValue(final String identifier) {
    return BadRequestException.invalidValue(String.format("Per schema definition a value is required for %s", identifier));
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
   **                            resource belongs.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the entity identifier to which the
   **                            resource belongs.
   **                            <br>
   **                            Allowed object is {@link String}.
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
  // Method:   conflict
  /**
   ** Factory method to create a new <code>uniqueness</code> violation wrapped
   ** in a {@link ResourceConflictException} with the resource identifier
   ** <code>resource</code> and the entity identifier <code>identifier</code>
   ** (usually the primary key of entity).
   **
   ** @param  resource           the resource identifier to which the
   **                            resource belongs.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the entity identifier to which the
   **                            resource belongs.
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

  private static boolean isDate(final String text) {
    return DATE_PATTERN.matcher(text).matches();
  }
}