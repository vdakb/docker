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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   TestFilterTranslator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestFilterTranslator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector.pcf;

import java.util.List;

import java.text.SimpleDateFormat;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.FilterBuilder;
import org.identityconnectors.framework.common.objects.filter.LessThanFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanOrEqualFilter;

import oracle.iam.identity.connector.integration.FilterFactory;

import oracle.iam.identity.icf.foundation.SystemError;

import oracle.iam.identity.icf.connector.AbstractConnector;

import oracle.iam.identity.icf.foundation.utility.DateUtility;

import oracle.iam.identity.icf.scim.FilterTranslator;

public class TestFilterTranslator extends FilterTranslator<String> {

  public TestFilterTranslator() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAND (overridden)
  @Override
  protected String createAND(final String lhs, final String rhs) {
    return new StringBuilder("(").append(lhs).append(" and ").append(rhs).append(")").toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOR (overridden)
  @Override
  protected String createOR(final String lhs, final String rhs) {
    return new StringBuilder("(").append(lhs).append(" or ").append(rhs).append(")").toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEQ (overridden)
  @Override
  protected String createEQ(final EqualsFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), singleValue(filter.getAttribute(), String.class), " eq ", not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGT (overridden)
  @Override
  protected String createGT(final GreaterThanFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), singleValue(filter.getAttribute(), String.class), " gt ", not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGE (overridden)
  @Override
  protected String createGE(final GreaterThanOrEqualFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), singleValue(filter.getAttribute(), String.class), " ge ", not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLT (overridden)
  @Override
  protected String createLT(final LessThanFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), singleValue(filter.getAttribute(), String.class), " lt ", not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLE (overridden)
  @Override
  protected String createLE(final LessThanOrEqualFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), singleValue(filter.getAttribute(), String.class), " le ", not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEW (overridden)
  @Override
  protected String createEW(final EndsWithFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), singleValue(filter.getAttribute(), String.class), " ew ", not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSW (overridden)
  @Override
  protected String createSW(final StartsWithFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), singleValue(filter.getAttribute(), String.class), " sw ", not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCO (overridden)
  @Override
  protected String createCO(final ContainsFilter filter, final boolean not) {
    return createFilter(filter.getAttribute().getName(), singleValue(filter.getAttribute(), String.class), " co ", not);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFilter
  protected String createFilter(final String name, final String value, final String operator, final boolean not) {
    final StringBuilder builder = new StringBuilder();
    if (not)
      builder.append("not (");
    builder.append(name).append(operator).append("\"").append(value).append("\"");
    if (not)
      builder.append(")");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   singleValue
  /**
   ** Obtains a single value from the specified {@link Attribute}.
   **
   ** @param  <T>                the type of the attribute value.
   ** @param  attribute          the {@link Attribute} providing values.
   **                            Allowed object {@link Attribute}.
   ** @param  expected           the expected type of the attribute value.
   **                            Allowed object {@link Class}.
   **
   ** @return                    the attribute value as of type
   ** <code>&lt;T&gt;</code>.
   **                            Possible object <code>&lt;T&gt;</code>.
   */
  public static <T> T singleValue(final Attribute attribute, final Class<T> expected) {
    final List<Object> values = attribute.getValue();
    if (values == null) {
      return null;
    }
    if (values.size() > 1)
      AbstractConnector.propagate(SystemError.SCHEMA_ATTRIBUTE_SINGLE, attribute.getName());

    Object value = values.get(0);
    if (!expected.isAssignableFrom(value.getClass()))
			AbstractConnector.propagate(SystemError.SCHEMA_ATTRIBUTE_TYPE, attribute.getName(), expected, value.getClass());

    return (T)value;
  }

  public static void main(String[] args) {
    Filter filter     = FilterFactory.build("startsWith('userName', 'B')");
    if (true) {
      final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
      final String           criteria  = formatter.format(DateUtility.now());
      filter = FilterBuilder.and(
        filter
      , FilterBuilder.or(
          FilterBuilder.greaterThan(AttributeBuilder.build("meta.created",      criteria))
        , FilterBuilder.greaterThan(AttributeBuilder.build("meta.lastModified", criteria))
        )
      );
    }
    TestFilterTranslator translator = new TestFilterTranslator();
    try {
      final List<String> expression = translator.translate(filter);
      System.out.println(expression);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
 }
}