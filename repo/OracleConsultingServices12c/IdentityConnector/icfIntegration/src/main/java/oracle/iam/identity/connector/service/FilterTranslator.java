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
    Subsystem   :   Connector Bundle Framework

    File        :   FilterTranslator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterTranslator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Set;
import java.util.HashSet;

import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanFilter;
import org.identityconnectors.framework.common.objects.filter.AttributeFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsAllValuesFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;

////////////////////////////////////////////////////////////////////////////////
// final class FilterTranslator
// ~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Filter translator which returns list of attribute names used in the provided
 ** {@link AttributeFilter} to translate.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class FilterTranslator extends AbstractFilterTranslator<Set<String>> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Set<String> attribute = new HashSet<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FilterTranslator</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FilterTranslator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAndExpression (overridden)
  /**
   ** Handling of 'And' filter, we can just return the {@link Set} of attribute
   ** names, cause both expressions were already processed.
   **
   ** @param  left               the left-hand-side expression.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  right              the right-hand-side expression.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createAndExpression(final Set<String> left, final Set<String> right) {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrExpression (overridden)
  /**
   ** Handling of 'Or' filter, we can just return the {@link Set} of attribute
   ** names, cause both expressions were already processed.
   **
   ** @param  left               the left-hand-side expression.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  right              the right-hand-side expression.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createOrExpression(final Set<String> left, final Set<String> right) {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContainsExpression (overridden)
  /**
   ** Handling <code>Contains</code>-{@link AttributeFilter}, the attribute name
   ** is added to the set of attributes.
   **
   ** @param  filter             the {@link AttributeFilter} to handle.
   **                            <br>
   **                            Allowed object is {@link ContainsFilter}.
   ** @param  not                <code>true</code> for negating filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createContainsExpression(final ContainsFilter filter, boolean not) {
    return handle(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContainsAllValuesExpression (overridden)
  /**
   ** Handling <code>ContainsAll</code>-{@link AttributeFilter}, the attribute
   ** name is added to the set of attributes.
   **
   ** @param  filter             the {@link AttributeFilter} to handle.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainsAllValuesFilter}.
   ** @param  not                <code>true</code> for negating filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createContainsAllValuesExpression(final ContainsAllValuesFilter filter, final boolean not) {
    return handle(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStartsWithExpression (overridden)
  /**
   ** Handling <code>StartsWith</code>-{@link AttributeFilter}, the attribute
   ** name is added to the set of attributes.
   **
   ** @param  filter             the {@link AttributeFilter} to handle.
   **                            <br>
   **                            Allowed object is {@link StartsWithFilter}.
   ** @param  not                <code>true</code> for negating filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createStartsWithExpression(final StartsWithFilter filter, final boolean not) {
    return handle(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEndsWithExpression (overridden)
  /**
   ** Handling <code>EndsWith</code>-{@link AttributeFilter}, the attribute name
   ** is added to the set of attributes.
   **
   ** @param  filter             the {@link AttributeFilter} to handle.
   **                            <br>
   **                            Allowed object is {@link EndsWithFilter}.
   ** @param  not                <code>true</code> for negating filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createEndsWithExpression(final EndsWithFilter filter, final boolean not) {
    return handle(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEqualsExpression (overridden)
  /**
   ** Handling <code>Equals</code>-{@link AttributeFilter}, the attribute name
   ** is added to the set of attributes.
   **
   ** @param  filter             the {@link AttributeFilter} to handle.
   **                            <br>
   **                            Allowed object is {@link EqualsFilter}.
   ** @param  not                <code>true</code> for negating filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createEqualsExpression(final EqualsFilter filter, final boolean not) {
    return handle(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGreaterThanExpression (overridden)
  /**
   ** Handling <code>GreaterThen</code>-{@link AttributeFilter}, the attribute
   ** name is added to the set of attributes.
   **
   ** @param  filter             the {@link AttributeFilter} to handle.
   **                            <br>
   **                            Allowed object is {@link GreaterThanFilter}.
   ** @param  not                <code>true</code> for negating filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createGreaterThanExpression(final GreaterThanFilter filter, final boolean not) {
    return handle(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGreaterThanOrEqualExpression (overridden)
  /**
   ** Handling <code>GreaterThanOrEqual</code>-{@link AttributeFilter}, the
   ** attribute name is added to the set of attributes.
   **
   ** @param  filter             the {@link AttributeFilter} to handle.
   **                            <br>
   **                            Allowed object is
   **                            {@link GreaterThanOrEqualFilter}.
   ** @param  not                <code>true</code> for negating filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createGreaterThanOrEqualExpression(final GreaterThanOrEqualFilter filter, final boolean not) {
    return handle(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLessThanExpression (overridden)
  /**
   ** Handling <code>LestThan</code>-{@link AttributeFilter}, the attribute name
   ** is added to the set of attributes.
   **
   ** @param  filter             the {@link AttributeFilter} to handle.
   **                            <br>
   **                            Allowed object is {@link LessThanFilter}.
   ** @param  not                <code>true</code> for negating filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createLessThanExpression(final LessThanFilter filter,final  boolean not) {
    return handle(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLessThanOrEqualExpression (overridden)
  /**
   ** Handling <code>LessThanOrEqual</code>-{@link AttributeFilter}, the
   ** attribute name is added to the set of attributes.
   **
   ** @param  filter             the {@link AttributeFilter} to handle.
   **                            <br>
   **                            Allowed object is
   **                            {@link LessThanOrEqualFilter}.
   ** @param  not                <code>true</code> for 'no' filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a {@link Set} of attribute names.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  protected Set<String> createLessThanOrEqualExpression(final LessThanOrEqualFilter filter, final boolean not) {
    return handle(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handle
  private Set<String> handle(final AttributeFilter filter) {
    this.attribute.add(filter.getName());
    return this.attribute;
  }
}