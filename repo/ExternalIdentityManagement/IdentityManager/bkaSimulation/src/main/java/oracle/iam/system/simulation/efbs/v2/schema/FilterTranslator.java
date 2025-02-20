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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   FilterTranslator.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterTranslator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.v2.schema;

import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.TimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import oracle.jdbc.OracleTypes;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.DateUtility;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.system.simulation.ProcessingException;

import oracle.iam.system.simulation.dbs.DatabaseEntity;
import oracle.iam.system.simulation.dbs.DatabaseFilter;

import oracle.iam.system.simulation.scim.object.Equals;
import oracle.iam.system.simulation.scim.object.Filter;
import oracle.iam.system.simulation.scim.object.Presence;
import oracle.iam.system.simulation.scim.object.Contains;
import oracle.iam.system.simulation.scim.object.EndsWith;
import oracle.iam.system.simulation.scim.object.LessThan;
import oracle.iam.system.simulation.scim.object.Translator;
import oracle.iam.system.simulation.scim.object.StartsWith;
import oracle.iam.system.simulation.scim.object.GreaterThan;
import oracle.iam.system.simulation.scim.object.LessThanOrEqual;
import oracle.iam.system.simulation.scim.object.GreaterThanOrEqual;

////////////////////////////////////////////////////////////////////////////////
// class FilterTranslator
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** FilterTranslator class translate filters to database WHERE clause.
 ** <p>
 ** The resource specific getAttributeName must be provided in real translator
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FilterTranslator extends Translator<DatabaseFilter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // create a date formatter with the specified format
  static final SimpleDateFormat formatter = new SimpleDateFormat(DateUtility.XML8601_ZULU);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String                  type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** FilterTranslator translate filters to database WHERE clause.
   **
   ** @param  entity             the the entity the filter has to be translated
   **                            for.
   */
  public FilterTranslator(final DatabaseEntity entity) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type = entity.type();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the name of the entity this {@link Translator} belongs to.
   **
   ** @return                    the name of the entity this {@link Translator}
   **                            belongs to.
   */
  public String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Main method to be called to translate a filter.
   **
   ** @param  entity             the the entity the filter has to be translated
   **                            for.
   ** @param  expression         the {@link Filter} to translate.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   **
   ** @throws ProcessingException if filter syntax becomes invalid.
   */
  public static List<DatabaseFilter> build(final DatabaseEntity entity, final Filter expression)
    throws ProcessingException {

    final FilterTranslator translator = new FilterTranslator(entity);
    return translator.translate(expression);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAND (overridden)
  /**
   ** Should be overridden by subclasses to create an AND expression if the
   ** native resource supports AND.
   **
   ** @param  lhs                the left expression. Will never be
   **                            <code>null</code>.
   ** @param  rhs                the right expression. Will never be
   **                            <code>null</code>.
   **
   ** @return                    the AND expression.
   **                            A return value of <code>null</code> means a
   **                            native AND query cannot be created for the
   **                            given expressions. In this case, the resulting
   **                            query will consist of the leftExpression only.
   */
  @Override
  protected DatabaseFilter createAND(final DatabaseFilter lhs, final DatabaseFilter rhs) {
    return DatabaseFilter.build(lhs, rhs, DatabaseFilter.Operator.AND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOR (overridden)
  /**
   ** Should be overridden by subclasses to create an OR expression if the
   ** native resource supports OR.
   **
   ** @param  lhs                the left expression. Will never be
   **                            <code>null</code>.
   ** @param  rhs                the right expression. Will never be
   **                            <code>null</code>.
   **
   ** @return                    the OR expression.
   **                            A return value of <code>null</code> means a
   **                            native OR query cannot be created for the
   **                            given expressions. In this case,
   **                            {@link #translate} may return multiple queries,
   **                            each of which must be run and results combined.
   */
  @Override
  protected DatabaseFilter createOR(final DatabaseFilter lhs, final DatabaseFilter rhs) {
    return DatabaseFilter.build(lhs, rhs, DatabaseFilter.Operator.OR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPR (overridden)
  /**
   ** Should be overridden by subclasses to create an EQUALS expression if the
   ** native resource supports EQUALS.
   **
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            NOT EQUALS.
   **
   ** @return                    the EQUALS expression.
   **                            A return value of <code>null</code> means a
   **                            native EQUALS query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  @Override
  protected DatabaseFilter createPR(final Presence filter, final boolean not) {
    final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(filter.path().toString());
    if (attribute == null)
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), (Object)null, not ? DatabaseFilter.Operator.EQ : DatabaseFilter.Operator.NOT_EQ);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEQ (overridden)
  /**
   ** Should be overridden by subclasses to create an EQUALS expression if the
   ** native resource supports EQUALS.
   **
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            NOT EQUALS.
   **
   ** @return                    the EQUALS expression.
   **                            A return value of <code>null</code> means a
   **                            native EQUALS query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  @Override
  protected DatabaseFilter createEQ(final Equals filter, final boolean not) {
    final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(filter.path().toString());
    if (attribute == null)
      return null;

    switch (attribute.value) {
      case OracleTypes.TIME      :
      case OracleTypes.DATE      :
      case OracleTypes.TIMESTAMP : final Calendar dateTime =  parseDate(filter.value().asText());
                                   return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), dateTime.getTime(),        not ? DatabaseFilter.Operator.NOT_EQ : DatabaseFilter.Operator.EQ);
      case OracleTypes.FLOAT     : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asLong(),   not ? DatabaseFilter.Operator.NOT_EQ : DatabaseFilter.Operator.EQ);
      case OracleTypes.DOUBLE    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asDouble(), not ? DatabaseFilter.Operator.NOT_EQ : DatabaseFilter.Operator.EQ);
      case OracleTypes.VARCHAR   :
      default                    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asText(),   not ? DatabaseFilter.Operator.NOT_EQ : DatabaseFilter.Operator.EQ);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGT (overridden)
  /**
   ** Should be overridden by subclasses to create a GREATER-THAN expression if
   ** the native resource supports GREATER-THAN.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN.
   **
   **
   ** @return                    the GREATER-THAN expression.
   **                            A return value of <code>null</code> means a
   **                            native GREATER-THAN query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  @Override
  protected DatabaseFilter createGT(final GreaterThan filter, final boolean not) {
    final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(filter.path().toString());
    if (attribute == null)
      return null;

    switch (attribute.value) {
      case OracleTypes.TIME      :
      case OracleTypes.DATE      :
      case OracleTypes.TIMESTAMP : final Calendar dateTime =  parseDate(filter.value().asText());
                                   return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), dateTime.getTime(),        not ? DatabaseFilter.Operator.LE : DatabaseFilter.Operator.GT);
      case OracleTypes.FLOAT     : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asLong(),   not ? DatabaseFilter.Operator.LE : DatabaseFilter.Operator.GT);
      case OracleTypes.DOUBLE    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asDouble(), not ? DatabaseFilter.Operator.LE : DatabaseFilter.Operator.GT);
      case OracleTypes.VARCHAR   :
      default                    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asText(),   not ? DatabaseFilter.Operator.LE : DatabaseFilter.Operator.GT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGE (overridden)
  /**
   ** Should be overridden by subclasses to create a GREATER-THAN-EQUAL
   ** expression if the native resource supports GREATER-THAN-EQUAL.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN-EQUAL.
   **
   ** @return                    the GREATER-THAN-EQUAL expression.
   **                            A return value of <code>null</code> means a
   **                            native GREATER-THAN-EQUAL query cannot be
   **                            created for the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  @Override
  protected DatabaseFilter createGE(final GreaterThanOrEqual filter, final boolean not) {
    final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(filter.path().toString());
    if (attribute == null)
      return null;

    switch (attribute.value) {
      case OracleTypes.TIME      :
      case OracleTypes.DATE      :
      case OracleTypes.TIMESTAMP : final Calendar dateTime =  parseDate(filter.value().asText());
                                   return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), dateTime.getTime(),        not ? DatabaseFilter.Operator.LT : DatabaseFilter.Operator.GE);
      case OracleTypes.FLOAT     : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asLong(),   not ? DatabaseFilter.Operator.LT : DatabaseFilter.Operator.GE);
      case OracleTypes.DOUBLE    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asDouble(), not ? DatabaseFilter.Operator.LT : DatabaseFilter.Operator.GE);
      case OracleTypes.VARCHAR   :
      default                    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asText(),   not ? DatabaseFilter.Operator.LT : DatabaseFilter.Operator.GE);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLT (overridden)
  /**
   ** Should be overridden by subclasses to create a LESS-THAN expression if
   ** the native resource supports LESS-THAN.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN.
   **
   ** @return                    the LESS-THAN expression.
   **                            A return value of <code>null</code> means a
   **                            native LESS-THAN query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  @Override
  protected DatabaseFilter createLT(final LessThan filter, final boolean not) {
    final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(filter.path().toString());
    if (attribute == null)
      return null;

    switch (attribute.value) {
      case OracleTypes.TIME      :
      case OracleTypes.DATE      :
      case OracleTypes.TIMESTAMP : final Calendar dateTime =  parseDate(filter.value().asText());
                                   return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), dateTime.getTime(),        not ? DatabaseFilter.Operator.GE : DatabaseFilter.Operator.LT);
      case OracleTypes.FLOAT     : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asLong(),   not ? DatabaseFilter.Operator.GE : DatabaseFilter.Operator.LT);
      case OracleTypes.DOUBLE    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asDouble(), not ? DatabaseFilter.Operator.GE : DatabaseFilter.Operator.LT);
      case OracleTypes.VARCHAR   :
      default                    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asText(),   not ? DatabaseFilter.Operator.GE : DatabaseFilter.Operator.LT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLE (overridden)
  /**
   ** Should be overridden by subclasses to create a LESS-THAN-EQUAL expression
   ** if the native resource supports LESS-THAN-EQUAL.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN-EQUAL.
   **
   ** @return                    the LESS-THAN-EQUAL expression.
   **                            A return value of <code>null</code> means a
   **                            native LESS-THAN-EQUAL query cannot be created
   **                            for the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  @Override
  protected DatabaseFilter createLE(final LessThanOrEqual filter, final boolean not) {
    final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(filter.path().toString());
    if (attribute == null)
      return null;

    switch (attribute.value) {
      case OracleTypes.TIME      :
      case OracleTypes.DATE      :
      case OracleTypes.TIMESTAMP : final Calendar dateTime =  parseDate(filter.value().asText());
                                   return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), dateTime.getTime(),        not ? DatabaseFilter.Operator.GT : DatabaseFilter.Operator.LE);
      case OracleTypes.FLOAT     : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asLong(),   not ? DatabaseFilter.Operator.GT : DatabaseFilter.Operator.LE);
      case OracleTypes.DOUBLE    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asDouble(), not ? DatabaseFilter.Operator.GT : DatabaseFilter.Operator.LE);
      case OracleTypes.VARCHAR   :
      default                    : return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asText(),   not ? DatabaseFilter.Operator.GT : DatabaseFilter.Operator.LE);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSW (overridden)
  /**
   ** Should be overridden by subclasses to create an STARTS-WITH expression if
   ** the native resource supports STARTS-WITH.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            STARTS-WITH.
   **
   ** @return                    the STARTS-WITH expression.
   **                            A return value of <code>null</code> means a
   **                            native STARTS-WITH query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  @Override
  protected DatabaseFilter createSW(final StartsWith filter, boolean not) {
    final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(filter.path().toString());
    if (attribute == null)
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asText(), not ? DatabaseFilter.Operator.NOT_SW : DatabaseFilter.Operator.SW);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEW (overridden)
  /**
   ** Should be overridden by subclasses to create an ENDS-WITH expression if
   ** the native resource supports ENDS-WITH.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            ENDS-WITH.
   **
   ** @return                    the ENDS-WITH expression.
   **                            A return value of <code>null</code> means a
   **                            native ENDS-WITH query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  @Override
  protected DatabaseFilter createEW(final EndsWith filter, boolean not) {
    final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(filter.path().toString());
    if (attribute == null)
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asText(), not ? DatabaseFilter.Operator.NOT_EW : DatabaseFilter.Operator.EW);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCO (overridden)
  /**
   ** Should be overridden by subclasses to create an CONTAINS expression if the
   ** native resource supports CONTAINS.
   **
   ** @param  filter             the contains filter. Will never be
   **                            <code>null</code>.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            CONTAINS.
   **
   ** @return                    the CONTAINS expression.
   **                            A return value of <code>null</code> means a
   **                            native CONTAINS query cannot be created for the
   **                            given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   */
  @Override
  protected DatabaseFilter createCO(final Contains filter, boolean not) {
    final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(filter.path().toString());
    if (attribute == null)
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.type, attribute.tag), filter.value().asText(), not ? DatabaseFilter.Operator.NOT_CO : DatabaseFilter.Operator.CO);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseDate
  /**
   ** Parses a date given as a string.
   ** <p>
   ** The format of the resulting string is <code>format</code>.
   **
   ** @param  date               a date formatted as a string.
   **                            the date must refere to the UTC time zone.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **
   ** @return                    the date as a {@link Calendar} or
   **                            <code>null</code> if argument string is null or
   **                            empty.
   **
   ** @throws ParseException     if a date/time component is out of range or
   **                            cannot be parsed
   */
  private static Calendar parseDate(final String date) {
    if (StringUtility.isEmpty(date))
      return null;

    // set the TimeZone of the internal calendar used by the formatter to the
    // requested TimeZone
    formatter.setTimeZone(DateUtility.UTC);
    try {
      formatter.parse(date);
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return formatter.getCalendar();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDate
  /**
   ** Returns the date of a {@link Calendar} instance to a String.
   **
   ** @param  calendar           the date as a {@link Calendar}
   **
   ** @return                    the date as a {@link Date} object
   */
  private static Date getDate(final Calendar calendar) {
    return getDate(calendar, DateUtility.UTC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDate
  /**
   ** Returns the date of a {@link Calendar} instance to a String.
   **
   ** @param  calendar           the date as a {@link Calendar}
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **
   ** @return                    the date as a {@link Date} object
   */
  private static Date getDate(final Calendar calendar, final TimeZone timeZone) {
    Date date = new Date(calendar.getTimeInMillis());
    if (!calendar.getTimeZone().equals(timeZone))
      date.setTime(calendar.getTimeInMillis() + timeZone.getRawOffset());

    return date;
  }
}