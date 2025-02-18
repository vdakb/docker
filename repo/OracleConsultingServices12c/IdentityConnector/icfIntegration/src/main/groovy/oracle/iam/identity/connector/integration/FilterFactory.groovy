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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared collection facilities

    File        :   FilterFactory.groovy

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.identityconnectors.framework.common.objects.AttributeBuilder

import org.identityconnectors.framework.common.objects.filter.Filter
import org.identityconnectors.framework.common.objects.filter.FilterBuilder

////////////////////////////////////////////////////////////////////////////////
// class FilterFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The FilterFactory (Groovy) DSL definition reader
 */
final class FilterFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final GroovyShell groovy = new GroovyShell()

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** The most important read method take the DSL definition to assign
   ** <code>Expando Metadata Class</code> to DSL shell.
   ** <br>
   ** Use the createEMC CLosure to enable to recognize the 'and', 'contains',
   ** 'containsAll' ... DSL root elements.
   ** <br>
   ** Process all DSL closures to follow the parser structure using the Closure
   ** delegates.
   **
   ** @param  expression         the filter expression to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link Filter} chain created from the
   **                            expression.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  static Filter build(final String expression) {
    return build(expression, null, null)
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** The most important read method take the DSL definition to assign
   ** <code>Expando Metadata Class</code> to DSL shell.
   ** <br>
   ** Use the createEMC CLosure to enable to recognize the 'and', 'contains',
   ** 'containsAll' ... DSL root elements.
   ** <br>
   ** Process all DSL closures to follow the parser structure using the Closure
   ** delegates.
   **
   ** @param  expression         the filter expression to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  dateAttributes     the comma separated list of attributes that
   **                            needs to converted to date values.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  dateFormat         the format of the date string to convert each
   **                            date attribute value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link Filter} chain created from the
   **                            expression.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  static Filter build(final String expression, final String dateAttributes, final String dateFormat) {
    // break the dateAttributes to an array
    def dates = null;
    if (dateAttributes != null) {
      // converting to lowercase as Groovy does not have function to check both
      // contains and ignore case at the same time.
      dates = dateAttributes.toLowerCase().split(",")
    }

    final Script script = groovy.parse(expression)
    // enable meta scan of subclasses
    ExpandoMetaClass.enableGlobally()

    // redefine the or operator use Filter.or() or Filter | Filter
    Filter.metaClass.or       = { Filter f2 -> FilterBuilder.or(delegate, f2) }
    // redefine the and operator use Filter.and() or Filter &amp; Filter
    Filter.metaClass.and      = { Filter f2 -> FilterBuilder.and(delegate, f2) }
    // redefine the negation operator use FilterBuilder.not() or -Filter
    Filter.metaClass.not      = { FilterBuilder.not(delegate) }
    // redefine the negation operator use FilterBuilder.not() or -Filter
    Filter.metaClass.negative = { FilterBuilder.not(delegate) }

    script.metaClass = createEMC(script.class, { ExpandoMetaClass emc ->
        emc.not = {
          f1 -> return FilterBuilder.not(f1)
        }
        emc.or  = {
          lhs, rhs -> return FilterBuilder.or(lhs, rhs)
        }
        emc.and = {
          lhs, rhs -> return FilterBuilder.and(lhs, rhs)
        }
        emc.equalTo = {
          String name, String val -> return FilterBuilder.equalTo(AttributeBuilder.build(name, convert(name, val, dates, dateFormat)))
        }
        emc.equalToIgnoreCase = {
          String name, String val -> return FilterBuilder.equalToIgnoreCase(AttributeBuilder.build(name, convert(name, val, dates, dateFormat)))
        }
        emc.lessThan = {
          String name, String val -> return FilterBuilder.lessThan(AttributeBuilder.build(name, convert(name, val, dates, dateFormat)))
        }
        emc.lessThanOrEqualTo = {
          String name, String val -> return FilterBuilder.lessThanOrEqualTo(AttributeBuilder.build(name, convert(name, val, dates, dateFormat)))
        }
        emc.greaterThan = {
          String name, String val -> return FilterBuilder.greaterThan(AttributeBuilder.build(name, convert(name, val, dates, dateFormat)))
        }
        emc.greaterThanOrEqualTo = {
          String name, String val -> return FilterBuilder.greaterThanOrEqualTo(AttributeBuilder.build(name, convert(name, val, dates, dateFormat)))
        }
        emc.endsWith = {
          String name, String val -> return FilterBuilder.endsWith(AttributeBuilder.build(name, val))
        }
        emc.endsWithIgnoreCase = {
          String name, String val -> return FilterBuilder.endsWithIgnoreCase(AttributeBuilder.build(name, val))
        }
        emc.startsWith = {
          String name, String val -> return FilterBuilder.startsWith(AttributeBuilder.build(name, val))
        }
        emc.startsWithIgnoreCase = {
          String name, String val -> return FilterBuilder.startsWithIgnoreCase(AttributeBuilder.build(name, val))
        }
        emc.contains = {
          String name, String val -> return FilterBuilder.contains(AttributeBuilder.build(name, val))
        }
        emc.containsIgnoreCase = {
          String name, String val -> return FilterBuilder.containsIgnoreCase(AttributeBuilder.build(name, val))
        }
        emc.containsAllValues = {
          String name, def val -> return FilterBuilder.containsAllValues(AttributeBuilder.build(name, val))
        }
      }
    )
    // lets process the definition script (delegates are called)
    script.run()
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEMC
  /**
   ** The ExpandoMetaClass creation function.
   ** <p>
   ** This is a DSL start point creator, enabling to understand the DLS root
   ** elements use the cl closure to define them
   */
  private static ExpandoMetaClass createEMC(final Class clazz, final Closure cl) {
    final ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
    cl(emc)
    emc.initialize()
    return emc
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  private static Object convert(final String name, final String value, String[] dates, String format) {
    Object returnValue = null;
    if (dates.grep(name.toLowerCase())) {
      // call the function to convert Date String to Long
      returnValue = toLong(value, format);
    }
    else {
      returnValue = value;
    }
    return returnValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  private static Object toLong(String date, String format) {
    long ldate = 0;
    if (format.endsWith(".0Z")) {
      // SimpleDateFormat cannot parse the .0 part of ldapDateformat
      // ( yyyyMMddhhmmss.0Z ), thus it requires special handling
      String[] sformat = format.split("\\.");
      format = sformat[0] + "'.0'Z";
      String[] sDateArr = date.split("\\.");
      if(date.endsWith(".0Z")) {
        date = sDateArr[0] + ".0+0000";
      }
    }
    final SimpleDateFormat f = new SimpleDateFormat(format);
    try {
      return f.parse(date).getTime();
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}