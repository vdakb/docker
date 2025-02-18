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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DatabaseFilterTranslator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseFilterTranslator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import java.util.List;

import oracle.hst.foundation.object.Filter;
import oracle.hst.foundation.object.Attribute;
import oracle.hst.foundation.object.FilterTranslator;

import oracle.hst.foundation.object.filter.Equals;
import oracle.hst.foundation.object.filter.Presence;
import oracle.hst.foundation.object.filter.Contains;
import oracle.hst.foundation.object.filter.EndsWith;
import oracle.hst.foundation.object.filter.LessThan;
import oracle.hst.foundation.object.filter.StartsWith;
import oracle.hst.foundation.object.filter.ContainsAll;
import oracle.hst.foundation.object.filter.GreaterThan;
import oracle.hst.foundation.object.filter.LessThanOrEqual;
import oracle.hst.foundation.object.filter.GreaterThanOrEqual;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseFilterTranslator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** DatabaseFilterTranslator class translate filters to database WHERE clause.
 ** <p>
 ** The resource specific getAttributeName must be provided in real translator
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DatabaseFilterTranslator extends FilterTranslator<DatabaseFilter> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String entity;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** DatabaseFilterTranslator translate filters to database WHERE clause.
   **
   ** @param  entity             the name of the entity the filter has to be
   **                            translated for.
   */
  private DatabaseFilterTranslator(final String entity) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.entity = entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Returns the name of the entity this {@link FilterTranslator} belongs to.
   **
   ** @return                    the name of the entity this
   **                            {@link FilterTranslator} belongs to.
   */
  public String entity() {
    return this.entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Main method to be called to translate a filter.
   **
   ** @param  entity             the name of the entity the filter has to be
   **                            translated for.
   ** @param  expression         the {@link Filter} to translate.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static List<DatabaseFilter> build(final String entity, final Filter expression) {
    final DatabaseFilterTranslator translator = new DatabaseFilterTranslator(entity);
    return translator.translate(expression);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOr (overridden)
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
  protected DatabaseFilter createOr(final DatabaseFilter lhs, final DatabaseFilter rhs) {
    return DatabaseFilter.build(lhs, rhs, DatabaseFilter.Operator.OR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAnd (overridden)
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
  protected DatabaseFilter createAnd(final DatabaseFilter lhs, final DatabaseFilter rhs) {
    return DatabaseFilter.build(lhs, rhs, DatabaseFilter.Operator.AND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPresence (overridden)
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
  protected DatabaseFilter createPresence(final Presence filter, final boolean not) {
    final String name = filter.name();

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, name), (Object)null, not ? DatabaseFilter.Operator.NOT_EQUAL : DatabaseFilter.Operator.EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEquals (overridden)
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
  protected DatabaseFilter createEquals(final Equals filter, final boolean not) {
    final Attribute attribute = filter.attribute();
    if (!validAttribute(attribute))
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, attribute.name()), attribute.singleValue(), not ? DatabaseFilter.Operator.NOT_EQUAL : DatabaseFilter.Operator.EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGreaterThan (overridden)
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
  protected DatabaseFilter createGreaterThan(final GreaterThan filter, final boolean not) {
    final Attribute attribute = filter.attribute();
    if (!validAttribute(attribute))
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, attribute.name()), attribute.singleValue(), not ? DatabaseFilter.Operator.LESS_EQUAL : DatabaseFilter.Operator.GREATER_THAN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGreaterThanOrEqual (overridden)
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
  protected DatabaseFilter createGreaterThanOrEqual(final GreaterThanOrEqual filter, final boolean not) {
    final Attribute attribute = filter.attribute();
    if (!validAttribute(attribute))
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, attribute.name()), attribute.singleValue(), not ? DatabaseFilter.Operator.LESS_THAN : DatabaseFilter.Operator.GREATER_EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLessThan (overridden)
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
  protected DatabaseFilter createLessThan(final LessThan filter, final boolean not) {
    final Attribute attribute = filter.attribute();
    if (!validAttribute(attribute))
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, attribute.name()), attribute.singleValue(), not ? DatabaseFilter.Operator.GREATER_EQUAL : DatabaseFilter.Operator.LESS_THAN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLessThanOrEqual (overridden)
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
  protected DatabaseFilter createLessThanOrEqual(final LessThanOrEqual filter, final boolean not) {
    final Attribute attribute = filter.attribute();
    if (!validAttribute(attribute))
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, attribute.name()), attribute.singleValue(), not ? DatabaseFilter.Operator.GREATER_THAN : DatabaseFilter.Operator.LESS_EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStartsWith (overridden)
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
  protected DatabaseFilter createStartsWith(final StartsWith filter, boolean not) {
    final Attribute attribute = filter.attribute();
    if (!validAttribute(attribute))
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, attribute.name()), attribute.singleValue(), not ? DatabaseFilter.Operator.NOT_STARTS_WITH : DatabaseFilter.Operator.STARTS_WITH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEndsWith (overridden)
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
  protected DatabaseFilter createEndsWith(final EndsWith filter, boolean not) {
    final Attribute attribute = filter.attribute();
    if (!validAttribute(attribute))
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, attribute.name()), attribute.singleValue(), not ? DatabaseFilter.Operator.NOT_ENDS_WITH : DatabaseFilter.Operator.ENDS_WITH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContains (overridden)
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
  protected DatabaseFilter createContains(final Contains filter, boolean not) {
    final Attribute attribute = filter.attribute();
    if (!validAttribute(attribute))
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, attribute.name()), attribute.singleValue(), not ? DatabaseFilter.Operator.NOT_CONTAINS : DatabaseFilter.Operator.CONTAINS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContainsAll (overridden)
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
  protected DatabaseFilter createContainsAll(final ContainsAll filter, boolean not) {
    final Attribute attribute = filter.attribute();
    if (!validAttribute(attribute))
      return null;

    return DatabaseFilter.build(DatabaseEntity.qualified(this.entity, attribute.name()), attribute.value(), not ? DatabaseFilter.Operator.NOT_IN : DatabaseFilter.Operator.IN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validAttribute
  /**
   ** Validate the attribute to supported search types
   **
   ** @param  attribute          the attribute to validate.
   **
   ** @return                    <code>true</code> if the attribute represents
   **                            a searchable implementation; otherwise
   **                            <code>false</code>.
   */
  protected boolean validAttribute(final Attribute attribute) {
    // ignore streamed ( byte[] objects ) from query; otherwise let the database
    // process
    return (!byte[].class.equals(Attribute.singleValue(attribute).getClass()));
  }
}