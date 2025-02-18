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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Database Connector

    File        :   DatabaseFilterTranslator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseFilterTranslator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.icf.foundation.object.filter.Translator;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseFilterTranslator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DatabaseFilterTranslator</code> abstract class translate filters to
 ** database WHERE clause The resource specific getAttributeName must be
 ** provided in real translator.
 ** Base class to make it easier to implement Search. A search filter may
 ** contain operators (such as 'contains' or 'in') or may contain logical
 ** operators (such as 'AND', 'OR' or 'NOT') that a connector cannot implement
 ** using the native API of the target system or application. A connector developer should subclass AbstractFilterTranslator in order to declare which filter operations the connector does support. This allows the FilterTranslator instance to analyze a specified search filter and reduce the filter to its most efficient form. The default (and worst-case) behavior is to return a null expression, which means that the connector should return "everything" (that is, should return all values for every requested attribute) and rely on the common code in the framework to perform filtering. This "fallback" behavior is good (in that it ensures consistency of search behavior across connector implementations) but it is obviously better for performance and scalability if each connector performs as much filtering as the native API of the target can support.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseFilterTranslator extends Translator<DatabaseFilter> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ObjectClass    type;
  private final DatabaseSchema schema;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseFilterTranslator</code> with the specified
   ** value.
   **
   ** @param  schema             the schema mapping to resolve and translate
   **                            object class names and their attributes.
   **                            <br>
   **                            Allowed object is {@link DatabaseSchema}.
   ** @param  type               the object objectClass.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   */
  private DatabaseFilterTranslator(final DatabaseSchema schema, final ObjectClass type) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type   = type;
    this.schema = schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DatabaseFilterTranslator</code> with the
   ** specified value.
   **
   **
   ** @param  schema             the schema mapping to resolve and translate
   **                            object class names and their attributes.
   **                            <br>
   **                            Allowed object is {@link DatabaseSchema}.
   ** @param  type               the object objectClass.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    an instance of
   **                            <code>DatabaseFilterTranslator</code> with the
   **                            value provided.
   **                            <br>
   **                            Possible object is
   **                            <code>DatabaseFilterTranslator</code>.
   */
  public static DatabaseFilterTranslator build(final DatabaseSchema schema, final ObjectClass type) {
    return new DatabaseFilterTranslator(schema, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and (overridden)
  /**
   ** Factory method to create an <code>AND</code> expression of two
   ** {@link DatabaseFilter}s.
   **
   ** @param  lhs                the left hand side expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  rhs                the right hand side expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    the <code>AND</code> expression of both
   **                            database filter expressions.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  public DatabaseFilter and(final DatabaseFilter lhs, final DatabaseFilter rhs) {
    return DatabaseFilter.build(lhs, rhs, DatabaseFilter.Operator.AND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or (overridden)
  /**
   ** Factory method to create an <code>OR</code> expression of two
   ** {@link DatabaseFilter}s.
   **
   ** @param  lhs                the left hand side expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  rhs                the right hand side expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    the <code>OR</code> expression of both
   **                            database filter expressions.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  public DatabaseFilter or(final DatabaseFilter lhs, final DatabaseFilter rhs) {
    return DatabaseFilter.build(lhs, rhs, DatabaseFilter.Operator.OR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to creates an <code>equal-to</code> filter expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the SQL syntax of a <code>equal-to</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter eq(final Attribute filter) {
    return eq(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq (overidden)
  /**
   ** Factory method to creates an <code>equal-to</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>not-equal-to</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>equal-to</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>equal-to</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  protected DatabaseFilter eq(final Attribute filter, final boolean not) {
    return eq(resolve(filter.getName()), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to creates an <code>equal-to</code> filter expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the SQL syntax of a <code>equal-to</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter eq(final String prefix, final String value) {
    return eq(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to creates an <code>equal-to</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SQL syntax of a <code>equal-to</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter eq(final String prefix, final String value, final boolean not) {
    return DatabaseFilter.build(prefix, value, not ? DatabaseFilter.Operator.NOT_EQUAL :DatabaseFilter.Operator.EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create a <code>less-than</code> expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the <code>less-than</code> database filter
   **                            expressions.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter lt(final Attribute filter) {
    return lt(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt (overridden)
  /**
   ** Factory method to creates a <code>less-than</code> filter expression that
   ** might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a <code>less-than</code>
   ** expression if the native resource supports <code>less-than</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>greater-than-or-equal</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>less-than</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>less-than</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  protected DatabaseFilter lt(final Attribute filter, final boolean not) {
    return lt(resolve(filter.getName()), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to creates a <code>less-than</code> filter expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the SQL syntax of a <code>less-than</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter lt(final String prefix, final String value) {
    return lt(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to creates a <code>less-than</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SQL syntax of a <code>less-than</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter lt(final String prefix, final String value, final boolean not) {
    return DatabaseFilter.build(prefix, value, not ? DatabaseFilter.Operator.GREATER_EQUAL : DatabaseFilter.Operator.LESS_THAN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create a <code>less-than-or-equal-to</code>
   ** expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the <code>less-than-or-equal-to</code>
   **                            database filter expressions.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter le(final Attribute filter) {
    return le(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le (overridden)
  /**
   ** Factory method to creates a <code>less-than-or-equal-to</code> filter
   ** expression the might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>greater-than</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a
   **                            <code>less-than-or-equal-to</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>less-than-or-equal-to</code> query
   **                            cannot be created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  protected DatabaseFilter le(final Attribute filter, final boolean not) {
    return le(resolve(filter.getName()), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to creates a <code>less-than-or-equal-to</code> filter
   ** expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the SQL syntax of a
   **                            <code>less-than-or-equal-to</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter le(final String prefix, final String value) {
    return le(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to creates a <code>less-than-or-equal-to</code> filter
   ** expression that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SQL syntax of a
   **                            <code>less-than-or-equal-to</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter le(final String prefix, final String value, final boolean not) {
    return DatabaseFilter.build(prefix, value, not ? DatabaseFilter.Operator.GREATER_THAN : DatabaseFilter.Operator.LESS_EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create a <code>greater-than</code> expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the <code>greater-than</code> database filter
   **                            expressions.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter gt(final Attribute filter) {
    return gt(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt (overridden)
  /**
   ** Factory method to creates a <code>greater-than</code> filter expression
   ** that might be negated regarding <code>not</code>.
   ** <br>
   ** Should be overridden by subclasses to create a <code>greater-than</code>
   ** expression if the native resource supports <code>greater-than</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>less-than-or-equal-to</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>greater-than</code>
   **                            filter expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>greater-than</code> query cannot
   **                            be created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  protected DatabaseFilter gt(final Attribute filter, final boolean not) {
    return gt(resolve(filter.getName()), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to creates a <code>greater-than</code> filter expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the SQL syntax of a <code>greater-than</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter gt(final String prefix, final String value) {
    return gt(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to creates a <code>greater-than</code> filter expression
   ** that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SQL syntax of a <code>greater-than</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter gt(final String prefix, final String value, final boolean not) {
    return DatabaseFilter.build(prefix, value, not ? DatabaseFilter.Operator.LESS_EQUAL : DatabaseFilter.Operator.GREATER_THAN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create a <code>greater-than-or-equal-to</code>
   ** expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the <code>greater-than-or-equal-to</code>
   **                            database filter expressions.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter ge(final Attribute filter) {
    return ge(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge (overridden)
  /**
   ** Factory method to creates a <code>greater-than-or-equal-to</code> filter
   ** expression that might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>less-than</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a
   **                            <code>greater-than-or-equal-to</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>greater-than-or-equal-to</code>
   **                            query cannot be created for the given
   **                            expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  protected DatabaseFilter ge(final Attribute filter, final boolean not) {
    return ge(resolve(filter.getName()), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to creates a <code>greater-than-or-equal-to</code> filter
   ** expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the SQL syntax of a
   **                            <code>greater-than-or-equal-to</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter ge(final String prefix, final String value) {
    return ge(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to creates a <code>greater-than-or-equal-to</code> filter
   ** expression that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SQL syntax of a
   **                            <code>greater-than-or-equal-to</code> filter
   **                            expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter ge(final String prefix, final String value, final boolean not) {
    return DatabaseFilter.build(prefix, value, not ? DatabaseFilter.Operator.LESS_THAN : DatabaseFilter.Operator.GREATER_EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create a <code>starts-with</code> expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the SQL syntax of a<code>starts-with</code>
   **                            filter expressions.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter sw(final Attribute filter) {
    return sw(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw (overridden)
  /**
   ** Factory method to creates a <code>starts-with</code> filter expression
   ** that might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be
   **                            <code>NOT-starts-with</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SQL syntax of a<code>starts-with</code>
   **                            filter expressions.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>starts-with</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  protected DatabaseFilter sw(final Attribute filter, final boolean not) {
    return sw(resolve(filter.getName()), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to creates a <code>starts-with</code> filter
   ** expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the SQL syntax of a <code>starts-with</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter sw(final String prefix, final String value) {
    return sw(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to creates a <code>starts-with</code> filter expression
   ** that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SQL syntax of a <code>starts-with</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter sw(final String prefix, final String value, final boolean not) {
    return DatabaseFilter.build(prefix, value, not ? DatabaseFilter.Operator.NOT_STARTS_WITH : DatabaseFilter.Operator.STARTS_WITH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create an <code>ends-with</code> expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the SQL syntax of a<code>ends-with</code>
   **                            filter expressions.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter ew(final Attribute filter) {
    return ew(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew (overridden)
  /**
   ** Factory method to creates an <code>ends-with</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be
   **                            <code>NOT-ends-with</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>ends-with</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>ends-with</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  protected DatabaseFilter ew(final Attribute filter, final boolean not) {
    return ew(resolve(filter.getName()), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to creates an <code>ends-with</code> filter
   ** expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the SQL syntax of a <code>ends-with</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter ew(final String prefix, final String value) {
    return ew(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to creates an <code>ends-with</code> filter expression
   ** that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SQL syntax of a <code>ends-with</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter ew(final String prefix, final String value, final boolean not) {
    return DatabaseFilter.build(prefix, value, not ? DatabaseFilter.Operator.NOT_ENDS_WITH : DatabaseFilter.Operator.ENDS_WITH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create a <code>contains</code> expression.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    the SQL syntax of a<code>contains</code>
   **                            filter expressions.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter co(final Attribute filter) {
    return co(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co (overridden)
  /**
   ** Factory method to creates a <code>contains</code> filter expression that
   ** might be negated regarding <code>not</code>.
   **
   ** @param  filter             the ICF syntax of an attribute.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            should be <code>not-contains</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the sytanx of a <code>contains</code> filter
   **                            expression.
   **                            <br>
   **                            A return value of <code>null</code> means a
   **                            native <code>contains</code> query cannot be
   **                            created for the given expressions.
   **                            <br>
   **                            In this case, {@link #translate} may return an
   **                            empty query set, meaning fetch
   **                            <b>everything</b>. The filter will be
   **                            re-applied in memory to the resulting object
   **                            stream. This does not scale well, so if
   **                            possible, this method should be implemented.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  @Override
  protected DatabaseFilter co(final Attribute filter, final boolean not) {
    return co(resolve(filter.getName()), SchemaUtility.singleValue(filter, String.class), not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to creates a <code>contains</code> filter
   ** expression.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the SQL syntax of a <code>contains</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter co(final String prefix, final String value) {
    return ew(prefix, value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to creates a <code>contains</code> filter expression
   ** that might be negated regarding <code>not</code>.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  not                <code>true</code> if the evaluated expression
   **                            is negated.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SQL syntax of a <code>contains</code>
   **                            filter expression.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public DatabaseFilter co(final String prefix, final String value, final boolean not) {
    return DatabaseFilter.build(prefix, value, not ? DatabaseFilter.Operator.NOT_CONTAINS : DatabaseFilter.Operator.CONTAINS);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolve
  /**
   ** Takes name of an attribute and maps it accordingly to the schema to its
   ** natural name used in the persistence layer.
   ** <br>
   ** For example ...
   ** @param  filter             the name passed from the integration layer to
   **                            the connector.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the natural name used in the persistence layer.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private String resolve(String filter) {
    final String                type   = this.type.getObjectClassValue();
    final DatabaseSchema.Entity entity = this.schema.structural(type);
    if (Uid.NAME.equals(filter)) {
      filter = entity.primary;
    }
    else if (Name.NAME.equals(filter)) {
      filter = entity.secondary;
    }
    else if (OperationalAttributes.ENABLE_NAME.equals(filter)) {
      filter = entity.status;
    }
    else if (OperationalAttributes.PASSWORD_NAME.equals(filter)) {
      filter = entity.password;
    }
    // the attribute name is the alias formulated in the query and returned in
    // the connector object but the native implementation will differ from
    // this name
    return this.schema.dictionary(entity.id).lookup(filter).id;
  }
}