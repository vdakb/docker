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
    Subsystem   :   Filter

    File        :   Filter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    Filter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.filter;

import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import oracle.hst.platform.core.entity.Or;
import oracle.hst.platform.core.entity.And;
import oracle.hst.platform.core.entity.Not;

import oracle.hst.platform.core.logging.Logger;

import oracle.hst.platform.jpa.SearchFilter;

////////////////////////////////////////////////////////////////////////////////
// final class Filter
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Helper methods for filtering.
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Filter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String                     category = Filter.class.getName();
  private static final Logger                     logger   = Logger.create(category);
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Filter</code> object that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Filter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translate
  /**
   ** Factory method to create a {@link SearchFilter} applicable as restrictions
   ** on a search.
   **
   ** @param  filter             the SCIM-like {@link Filter} providing the search
   **                            criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the resulting {@link SearchFilter}.
   **                            <br>
   **                            Possible object is {@link SearchFilter}.
   */
  @SuppressWarnings("unchecked")
  public static SearchFilter translate(final oracle.hst.platform.core.entity.Filter filter) {
    final String method = "translate";
    logger.entering(category, method, "filter=", filter);

    Object value = filter.value();
    // try to parse the value to determine type
    logger.trace(category, method, "Going to process value: " + value);
    if (value != null) {
      try {
        // Number first
        value = Long.parseLong((String) value);
      } catch (NumberFormatException e1) {
        logger.trace(category, method, "Not a Number, how about a Date with time stamp?");
        try {
          // followed by Date with time - yyyy-MM-dd HH:mm:ss
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          value = formatter.parse((String) value);
        } catch (ParseException e2) {
          try {
            logger.trace(category, method, "Not a Date with time stamp, how about a Date without time stamp?");
            // followed by Date without time - yyyy-MM-dd
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            value = formatter.parse((String) value);
          } catch (ParseException e3) {
            logger.trace(category, method, "Not a Date, either, must be String (ignoring Boolean)");
          }
        }
      }
    } else {
      logger.trace(category, method, "Value is null, not processing");
    }
    switch (filter.type()) {
      case OR  : return disjunction(((Or)filter).filter());
      case AND : return conjunction(((And)filter).filter());
      case NOT : return SearchFilter.not(translate(((Not)filter).filter()));

      case SW  : return SearchFilter.sw(filter.path().segment(0).attribute(), (String) value);
      case EW  : return SearchFilter.ew(filter.path().segment(0).attribute(), (String) value);
      case CO  : return SearchFilter.co(filter.path().segment(0).attribute(), (String) value);
    // No way the numerics worked in original code - query parameter is always a string..
      case LE  : if (value instanceof Number) {
                  return SearchFilter.le(filter.path().segment(0).attribute(), (Number) value);
                 } else if (value instanceof Calendar) {
                   return SearchFilter.lt(filter.path().segment(0).attribute(), (Calendar) value);
                 }
                 throw new IllegalArgumentException();
      case LT  : if (value instanceof Number) {
                   return SearchFilter.lt(filter.path().segment(0).attribute(), (Number) value);
                 } else if (value instanceof Calendar) {
                   return SearchFilter.lt(filter.path().segment(0).attribute(), (Calendar) value);
                 }
                 throw new IllegalArgumentException();
      case GE  : if (value instanceof Number) {
                   return SearchFilter.ge(filter.path().segment(0).attribute(), (Number) value);
                 } else if (value instanceof Calendar) {
                   return SearchFilter.ge(filter.path().segment(0).attribute(), (Calendar) value);
                 }
                 throw new IllegalArgumentException();
      case GT  : if (value instanceof Number) {
                   return SearchFilter.gt(filter.path().segment(0).attribute(), (Number) value);
                 } else if (value instanceof Calendar) {
                   return SearchFilter.gt(filter.path().segment(0).attribute(), (Calendar) value);
                 }
                 throw new IllegalArgumentException();
      default  : if (value instanceof Boolean) {
                   return SearchFilter.eq(filter.path().segment(0).attribute(), (Boolean) value);
                 } else if (value instanceof Number) {
                   return SearchFilter.eq(filter.path().segment(0).attribute(), (Number) value);
                 } else if (value instanceof String) {
                   return SearchFilter.eq(filter.path().segment(0).attribute(), (String )value);
                 }
      final String message = "Unknown filter type: " + filter.type();
      logger.error(category, method, message);
      throw new IllegalArgumentException(message);
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
  public static SearchFilter<?> conjunction(final List<oracle.hst.platform.core.entity.Filter> filter) {
    // FIXME: not compilable ???
    // return SearchFilter.and(Streams.stream(filter).filter(f -> f != null).map(f -> translate(f)).collect(Collectors.toList()));
    final List<SearchFilter<?>> collector = new ArrayList<SearchFilter<?>>(filter.size());
    for (oracle.hst.platform.core.entity.Filter cursor : filter) {
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
  public static SearchFilter disjunction(final List<oracle.hst.platform.core.entity.Filter> filter) {
    // FIXME: not compilable ???
    // return SearchFilter.or(Streams.stream(filter).filter(f -> f != null).map(f -> translate(f)).collect(Collectors.toList()));
    final List<SearchFilter<?>> collector = new ArrayList<SearchFilter<?>>(filter.size());
    for (oracle.hst.platform.core.entity.Filter cursor : filter) {
      collector.add(translate(cursor));
    }
    return SearchFilter.or(collector);
  }
}