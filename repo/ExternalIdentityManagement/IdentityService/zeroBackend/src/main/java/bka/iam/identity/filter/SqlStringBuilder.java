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
    Subsystem   :   ZeRo Backend

    File        :   SqlStringBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   Builds SQL filter based on input criteria.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-05-25  AFarkas     First release version
*/

package bka.iam.identity.filter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.jpa.SearchFilter;

////////////////////////////////////////////////////////////////////////////////
// final class SqlStringBuilder
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Builds SQL filter based on input criteria
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SqlStringBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private static final String                     category = SqlStringBuilder.class.getName();
  private static final Logger                     logger   = Logger.create(category);
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Constructors
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SqlStringBuilder() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Method to interpret "and" operator.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is <code>StringBuilder</code>.
   **                            
   ** @param  filters            the {@link List} of {@link SearchFilter} objects containing
   **                            sub-filters.
   **                            <br>
   **                            Allowed object is {@link List} of {@link SearchFilter}s.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @param  columnMap          the {@link Map} of {@link String}s containing the
   **                            criterion-to-column mapping.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder and(StringBuilder builder, List<SearchFilter<?>> filters, List<Object> arguments, Map<String, String> columnMap) {
    for (int i = 0; i < filters.size(); i++) {
      SearchFilter filter = filters.get(i);
      builder = filter(builder, filter, arguments, columnMap);
      if (i < filters.size() - 1) {
        builder.append(" AND ");
      }
    }
    return builder;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Method to interpret "or" operator.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is <code>StringBuilder</code>.
   **                            
   ** @param  filters            the {@link List} of {@link SearchFilter} objects containing
   **                            sub-filters.
   **                            <br>
   **                            Allowed object is {@link List} of {@link SearchFilter}s.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @param  columnMap          the {@link Map} of {@link String}s containing the
   **                            criterion-to-column mapping.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder or(StringBuilder builder, List<SearchFilter<?>> filters, List<Object> arguments, Map<String, String> columnMap) {
    for (int i = 0; i < filters.size(); i++) {
      SearchFilter filter = filters.get(i);
      builder = filter(builder, filter, arguments, columnMap);
      if (i < filters.size() - 1) {
        builder.append(" OR ");
      }
    }
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   not
  /**
   ** Method to interpret "not" operator.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  filter             the {@link SearchFilter} object containing the filter
   **                            to be inverted.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @param  columnMap          the {@link Map} of column name mapings (criterion
   **                            name vs. column name).
   **                            <br>
   **                            Allowed object is {@link Map} of {@link String}s
   **                            and {@link String}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder not(StringBuilder builder, SearchFilter<?> filter, List<Object> arguments, Map<String, String> columnMap) {
    return builder.append(" NOT (").append(filter(new StringBuilder(), filter, arguments, columnMap)).append(")");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Method to append an "eq" condition to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link String} value (regardless of the actual type)
   **                            to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder equal(StringBuilder builder, String path, String value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" = ?");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   like
  /**
   ** Method to append a "sw", "ew" and "co" conditions to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link String} value (regardless of the actual type)
   **                            to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder like(StringBuilder builder, String path, String value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" LIKE ?");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Method to append an "lt" condition to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link Number} value to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder lt(StringBuilder builder, String path, Number value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" < ? ");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Method to append an "lt" condition to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link Date} value to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder lt(StringBuilder builder, String path, Date value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" < ?");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Method to append an "le" condition to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link Number} value to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder le(StringBuilder builder, String path, Number value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" <= ?");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Method to append an "le" condition to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link Date} value to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder le(StringBuilder builder, String path, Date value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" <= ?");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Method to append an "gt" condition to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link Number} value to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder gt(StringBuilder builder, String path, Number value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" > ?");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Method to append an "gt" condition to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link Date} value to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder gt(StringBuilder builder, String path, Date value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" > ?");
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Method to append an "ge" condition to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link Number} value to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link Number}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder ge(StringBuilder builder, String path, Number value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" >= ?");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Method to append an "ge" condition to SQL WHERE clause.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  path               the {@link String} containing the (already mapped)
   **                            column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @param  value              the {@link Date} value to append to the WHERE clause.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **                            
   ** @param  arguments          the input/output {@link List} of {@link Object}s to use as
   **                            bind values. Will be appended by the value supplied.
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  static StringBuilder ge(StringBuilder builder, String path, Date value, List<Object> arguments) {
    arguments.add(value);
    return builder.append(path).append(" >= ?");
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Method to return a {@link StringBuilder} containing SQL filter 
   ** usable as restrictions on a search.
   **
   ** @param  builder            the {@link StringBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   **                            
   ** @param  filter             the {@link SearchFilter} providing the
   **                            search criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **                            
   ** @param  arguments          the {@link List} of {@link Object}s to use as
   **                            arguments. May be empty (or null).
   **                            <br>
   **                            Allowed object is {@link List} of {@link Object}s.
   **                            
   ** @param  columnMap          the {@link Map} of column name mapings (criterion
   **                            name vs. column name).
   **                            <br>
   **                            Allowed object is {@link Map} of {@link String}s
   **                            and {@link String}s.
   **                            
   ** @return                    the {@link StringBuilder} containing the SQL
   **                            filter.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  public static StringBuilder filter(final StringBuilder builder, final SearchFilter filter, List<Object> arguments, Map<String, String> columnMap)
  throws IllegalArgumentException {
    final String method = "filter";
    logger.entering(category, method, "builder=", builder);
    final SearchFilter.Type type = filter.type();

    String columnName = "";
    if (filter.path() != null) {
      columnName = columnMap.get(filter.path().toUpperCase());
      if (columnName == null) {
        final String message = "Invalid criterion name used in filter: " + filter.path();
        logger.error(category, method, message);
        throw new IllegalArgumentException(message);
      } else {
        logger.trace(category, method, "Mapped column name: " + columnName);
      }
    }
    switch (type) {
      case OR  : return or (builder, ((SearchFilter.Or)filter).filter(), arguments, columnMap);
      case AND : return and(builder, ((SearchFilter.And)filter).filter(), arguments, columnMap);
      case NOT : return not(builder, ((SearchFilter.Not)filter).filter(), arguments, columnMap);

      case EQ  : return equal(builder, columnName, (String)filter.value().toString(), arguments);
      case SW  :
      case EW  :
      case CO  : final String pattern = (type == SearchFilter.Type.SW ? "" : "%") + filter.value() + (type == SearchFilter.Type.EW ? "" : "%");
                 return SqlStringBuilder.like(builder, columnName, pattern, arguments);
      case LT  : if (filter instanceof SearchFilter.Numeric) {
                   return lt(builder, columnName, (Number)filter.value(), arguments);
                 } else if (filter instanceof SearchFilter.Temporal) {
                   return lt(builder, columnName, (Date)filter.value(), arguments);
                 }
                 throw new IllegalArgumentException();
      case LE  : if (filter instanceof SearchFilter.Numeric) {
                   return le(builder, columnName, (Number)filter.value(), arguments);
                 } else if (filter instanceof SearchFilter.Temporal) {
                   return le(builder, columnName, (Date)filter.value(), arguments);
                 }
                 throw new IllegalArgumentException();
      case GT  : if (filter instanceof SearchFilter.Numeric) {
                   return gt(builder, columnName, (Number)filter.value(), arguments);
                 } else if (filter instanceof SearchFilter.Temporal) {
                   return gt(builder, columnName, (Date)filter.value(), arguments);
                 }
                 throw new IllegalArgumentException();
      case GE  : if (filter instanceof SearchFilter.Numeric) {
                   return ge(builder, columnName, (Number)filter.value(), arguments);
                 } else if (filter instanceof SearchFilter.Temporal) {
                   return ge(builder, columnName, (Date)filter.value(), arguments);
                 }
                 throw new IllegalArgumentException();
    }
    final String message = "Unknown filter type: " + type;
    logger.error(category, method, message);
    throw new IllegalArgumentException(message);
  }
}