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

    File        :   TypeEvaluator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TypeEvaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.entity;

import com.fasterxml.jackson.databind.JsonNode;

import oracle.hst.platform.core.utility.StringUtility;

import oracle.iam.platform.scim.ProcessingException;

////////////////////////////////////////////////////////////////////////////////
// class TypeEvaluator
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A filter visitor that will evaluate a filter on a {@link String} and
 ** return whether the {@link String} matches the filter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TypeEvaluator implements Filter.Visitor<Boolean, String> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The one and only instance of the <code>TypeEvaluator</code>
   ** <p>
   ** Singleton Pattern
   */
  private static final TypeEvaluator instance = new TypeEvaluator();

  private static final Path          PATH     = Path.build().attribute("type");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a SCIM <code>TypeEvaluator</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected TypeEvaluator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits an <code>and</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always
   ** evaluate to <code>true</code>.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link And}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final And filter, final String type)
    throws ProcessingException {

    for (Filter cursor : filter.filter()) {
      if (!cursor.accept(this, type)) {
        return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits an <code>or</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always
   ** evaluate to <code>false</code>.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Or}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Or filter, final String type)
    throws ProcessingException {

    for (Filter cursor : filter.filter()) {
      if (cursor.accept(this, type)) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>not</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Not}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Not filter, final String type)
    throws ProcessingException {

    return !filter.filter().accept(this, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>present</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Presence}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Presence filter, final String type)
    throws ProcessingException {

    // draft-ietf-scim-core-schema section 2.5 states "Unassigned attributes,
    // the null value, or empty array (in the case of a multi-valued
    // attribute) SHALL be considered to be equivalent in "state"
    return PATH.equals(filter.path()) && type != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits an <code>equality</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Equals}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Equals filter, final String type)
    throws ProcessingException {

    if (PATH.equals(filter.path()) && filter.value().isNull() && !StringUtility.empty(type)) {
      // draft-ietf-scim-core-schema section 2.4 states "Unassigned attributes,
      // the null value, or empty array (in the case of a multi-valued
      // attribute) SHALL be considered to be equivalent in "state"
      return true;
    }
    return PATH.equals(filter.path()) && StringUtility.equalIgnoreCase(type, filter.value().asText());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>greater than</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link GreaterThan}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final GreaterThan filter, final String type)
    throws ProcessingException {

    return PATH.equals(filter.path()) && StringUtility.compareIgnoreCase(type, filter.value().asText()) > 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>greater than or equal to</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link GreaterThanOrEqual}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final GreaterThanOrEqual filter, final String type)
    throws ProcessingException {

    return PATH.equals(filter.path()) && StringUtility.compareIgnoreCase(type, filter.value().asText()) >= 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>less than</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link LessThan}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final LessThan filter, final String type)
    throws ProcessingException {

    return PATH.equals(filter.path()) && StringUtility.compareIgnoreCase(type, filter.value().asText()) < 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>less than or equal to</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link LessThanOrEqual}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final LessThanOrEqual filter, final String type)
    throws ProcessingException {

    return PATH.equals(filter.path()) && StringUtility.compareIgnoreCase(type, filter.value().asText()) <= 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>starts with</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link StartsWith}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final StartsWith filter, final String type)
    throws ProcessingException {

    return PATH.equals(filter.path()) && StringUtility.startsWithIgnoreCase(type, filter.value().asText());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>ends with</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link EndsWith}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final EndsWith filter, final String type)
    throws ProcessingException {

    return PATH.equals(filter.path()) && StringUtility.endsWithIgnoreCase(type, filter.value().asText());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>contains</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Contains}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Contains filter, final String type)
    throws ProcessingException {

    return PATH.equals(filter.path()) && StringUtility.containsIgnoreCase(type, filter.value().asText());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>complex</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link ComplexFilter}.
   ** @param  type               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final ComplexFilter filter, final String type)
    throws ProcessingException {

    return filter.filter().accept(this, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionallity
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   evaluate
  /**
   ** Evaluate the provided filter against the provided {@link JsonNode}.
   **
   ** @param  filter             the filter to evaluate.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  type               the value of the type attribute to match with
   **                            the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the {@link JsonNode}
   **                            matches the filter or <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  public static boolean evaluate(final Filter filter, final String type)
    throws ProcessingException {

    return filter.accept(instance, type);
  }
}