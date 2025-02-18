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

package oracle.hst.foundation.object.parser;

import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import oracle.hst.foundation.object.Filter
import oracle.hst.foundation.object.Attribute

////////////////////////////////////////////////////////////////////////////////
// class FilterFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The FilterFactory (Groovy) DSL definition reader
 */
final class FilterFactory {

  /**
   ** The most important read method take the DSL definition
   ** Assign Expando Metadata Class to DSL shell.
   ** Use the createEMC CLosure to enable to recognize the 'and','co','ca' ...
   ** DSL root elements
   ** Process all DSL closures to follow the parser structure using the Closure
   ** delegates
   */
  static Filter build(final String expression) {
    return build(expression, null, null)
  }

  static Filter build(final String expression, final String dateAttributes, final String dateFormat) {
    final Script dslScript = new GroovyShell().parse(expression)

    // break the dateAttributes to an array
    def dateArray = null;
    if (dateAttributes != null) {
      // converting to lowercase as Groovy does not have function to check both
      // contains and ignore case at the same time.
      dateArray = dateAttributes.toLowerCase().split(",")
    }

    // enable meta scan of subclasses
    ExpandoMetaClass.enableGlobally()

    // redefine the or operator use Filter.or() or Filter | Filter
    Filter.metaClass.or       = { Filter f2 -> Filter.or(delegate, f2) }
    // redefine the and operator use Filter.and() or Filter &amp; Filter
    Filter.metaClass.and      = { Filter f2 -> Filter.and(delegate, f2) }
    // redefine the negation operator use Filter.negative() or -Filter
    Filter.metaClass.not      = { Filter.not(delegate) }

    dslScript.metaClass = createEMC(
      dslScript.class
    , { ExpandoMetaClass emc ->
        emc.or    = {
          f1, f2 -> return Filter.or(f1, f2)
        }
        emc.and   = {
          f1, f2 -> return Filter.and(f1, f2)
        }
        emc.not   = {
          f1 -> return Filter.not(f1)
        }
        emc.pr    = {
          String name -> return Filter.presence(name)
        }
        emc.eq    = {
          String name, String val -> return Filter.equal(Attribute.build(name, convert(name, val, dateArray, dateFormat)))
        }
        emc.lt    = {
          String name, String val -> return Filter.lessThan(Attribute.build(name, convert(name, val, dateArray, dateFormat)))
        }
        emc.le    = {
          String name, String val -> return Filter.lessThanOrEqual(Attribute.build(name, convert(name, val, dateArray, dateFormat)))
        }
        emc.gt    = {
          String name, String val -> return Filter.greaterThan(Attribute.build(name, convert(name, val, dateArray, dateFormat)))
        }
        emc.ge    = {
          String name, String val -> return Filter.greaterThanOrEqual(Attribute.build(name, convert(name, val, dateArray, dateFormat)))
        }
        emc.ew    = {
          String name, String val -> return Filter.endsWith(Attribute.build(name, val), false)
        }
        emc.sw   = {
          String name, String val -> return Filter.startsWith(Attribute.build(name, val), false)
        }
        emc.co   = {
          String name, String val -> return Filter.contains(Attribute.build(name, val), false)
        }
        emc.ca   = {
          String name, def val -> return Filter.containsAll(Attribute.build(name, val))
        }
        emc.coic = {
          String name, String val -> return Filter.contains(Attribute.build(name, val), true)
        }
        emc.ewic = {
          String name, String val -> return Filter.endsWith(Attribute.build(name, val), true)
        }
        emc.swic = {
          String name, String val -> return Filter.startsWith(Attribute.build(name, val), true)
        }
      }
    )
    // Lets process the definition script (delegates are called)
    dslScript.run()
  }

  static Object convert(final String name, final String value, String[] dateArray, String dateFormat) {
    Object returnValue = null;
    if (dateArray.grep(name.toLowerCase())) {
      // call the function to convert Date String to Long
      returnValue = this.convertStringDateToLong(value, dateFormat);
    }
    else {
      returnValue = value;
    }
    return returnValue;
  }

  static Object convertStringDateToLong(String date, String dateFormat) {
    long ldate = 0;
    if (dateFormat.endsWith(".0Z")) {
      // SimpleDateFormat cannot parse the .0 part of ldapDateformat
      // ( yyyyMMddhhmmss.0Z ), thus it requires special handling
      String[] sformat = dateFormat.split("\\.");
      dateFormat = sformat[0] + "'.0'Z";
      String[] sDateArr = date.split("\\.");
      if(date.endsWith(".0Z")) {
        date = sDateArr[0] + ".0+0000";
      }
    }
    final SimpleDateFormat f = new SimpleDateFormat(dateFormat);
    try {
      return f.parse(date).getTime();
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

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
}