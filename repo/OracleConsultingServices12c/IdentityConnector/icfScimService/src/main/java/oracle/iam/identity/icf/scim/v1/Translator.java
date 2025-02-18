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
    Subsystem   :   Generic SCIM 1 Connector

    File        :   Translator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Translator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.v1;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

import oracle.iam.identity.icf.scim.Path;
import oracle.iam.identity.icf.scim.FilterTranslator;

import oracle.iam.identity.icf.scim.v1.schema.Marshaller;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;
import org.identityconnectors.framework.common.objects.OperationalAttributeInfos;
import org.identityconnectors.framework.common.objects.filter.LessThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanOrEqualFilter;

////////////////////////////////////////////////////////////////////////////////
// class Translator
// ~~~~~ ~~~~~~~~~~
/**
 ** SCIM 1 implementation to translate ICF Filter in the SCIM 1 syntax.
 ** <p>
 ** A search filter may contain operators (such as 'contains' or 'in') or may
 ** contain logical operators (such as 'AND', 'OR' or 'NOT') that a connector
 ** cannot implement using the native API of the target system or application. A
 ** connector developer should subclass <code>Translator</code> in order to
 ** declare which filter operations the connector does support. This allows
 ** the <code>Translator</code> instance to analyze a specified search filter
 ** and reduce the filter to its most efficient form. The default (and
 ** worst-case) behavior is to return a <code>null</code> expression, which
 ** means that the connector should return "everything" (that is, should return
 ** all values for every requested attribute) and rely on the common code in the
 ** framework to perform filtering. This "fallback" behavior is good (in that it
 ** ensures consistency of search behavior across connector implementations) but
 ** it is obviously better for performance and scalability if each connector
 ** performs as much filtering as the native API of the target can support.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Translator extends FilterTranslator<String> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a SCIM 1 filter <code>Translator</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Translator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOR (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>OR</code> filter expression.
   **
   ** @param  lhs                the left hand side SCIM 1 <code>OR</code>
   **                            filter expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the right hand side SCIM 1 <code>OR</code>
   **                            filter expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the SCIM 1 <code>OR</code> expression.
   **                            A return value of <code>null</code> means a
   **                            native OR query cannot be created for the
   **                            given expressions. In this case,
   **                            {@link #translate} may return multiple queries,
   **                            each of which must be run and results combined.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createOR(final String lhs, final String rhs) {
    return new StringBuilder("(").append(lhs).append(" or ").append(rhs).append(")").toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAND (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>AND</code> filter expression.
   **
   ** @param  lhs                the left hand side SCIM 1 <code>AND</code>
   **                            filter expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the right hand side SCIM 1 <code>AND</code>
   **                            filter expression.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the SCIM 1 <code>AND</code> expression.
   **                            A return value of <code>null</code> means a
   **                            native AND query cannot be created for the
   **                            given expressions. In this case,
   **                            {@link #translate} may return multiple queries,
   **                            each of which must be run and results combined.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createAND(final String lhs, final String rhs) {
    return new StringBuilder("(").append(lhs).append(" and ").append(rhs).append(")").toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEQ (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>EQUAL</code> filter expression.
   **
   ** @param  filter             the ICF <code>equal</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EqualsFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            EQUALS.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SCIM 1 <code>EQUAL</code> filter
   **                            expression.
   **                            A return value of <code>null</code> means a
   **                            native EQUALS query cannot be created for
   **                            the given filter. In this case,
   **                            {@link #translate} may return an empty query
   **                            set, meaning fetch <b>everything</b>. The
   **                            filter will be re-applied in memory to the
   **                            resulting object stream. This does not scale
   **                            well, so if possible, you should implement this
   **                            method.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createEQ(final EqualsFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), not ? NE : EQ, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGT (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>GREATER-THAN</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>greater than</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link GreaterThanFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createGT(final GreaterThanFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), GT, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGE (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>GREATER-THAN-EQUAL</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>greater than or equal</code>
   **                            filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link GreaterThanOrEqualFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            GREATER-THAN-EQUAL.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createGE(final GreaterThanOrEqualFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), GE, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLT (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>LESS-THAN</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>less than</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link LessThanFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createLT(final LessThanFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), LT, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLE (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>LESS-THAN-EQUAL</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>less than or equal</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link LessThanOrEqualFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            LESS-THAN-EQUAL.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createLE(final LessThanOrEqualFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), LE, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSW (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>STARTS-WITH</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>starts with</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link StartsWithFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            STARTS-WITH.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createSW(final StartsWithFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), SW, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEW (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>ENDS-WITH</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>ends with</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EndsWithFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            ENDS-WITH.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createEW(final EndsWithFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), EW, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCO (ServiceFilterTranslator)
  /**
   ** Factory method to create a SCIM 1 <code>CONTAINS</code> filter
   ** expression.
   **
   ** @param  filter             the ICF <code>contains</code> filter.
   **                            <br>
   **                            Must never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ContainsFilter}.
   ** @param  not                <code>true</code> if this should be a NOT
   **                            CONTAINS.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected String createCO(final ContainsFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), SchemaUtility.singleValue(filter.getAttribute(), String.class), CO, not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a ICF to SCIM filter translator.
   **
   ** @return                    the path to the root of the JSON object that
   **                            represents the SCIM resource.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public static Translator build() {
    return new Translator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFilter
  protected String createFilter(final String name, final String value, final String operator, final boolean not) {
    String normalized = name;
    if (Uid.NAME.equals(name)) {
      normalized = Marshaller.IDENTIFIER;
    }
    else if (Name.NAME.equals(name)) {
      normalized = Marshaller.USERNAME;
    }
    else if (OperationalAttributeInfos.ENABLE.equals(name)) {
      normalized = Marshaller.STATUS;
    }

    final StringBuilder builder = new StringBuilder();
    if (not)
      builder.append("not (");
    builder.append(normalized).append(operator).append("\"").append(value).append("\"");
    if (not)
      builder.append(")");
    return builder.toString();
  }
}