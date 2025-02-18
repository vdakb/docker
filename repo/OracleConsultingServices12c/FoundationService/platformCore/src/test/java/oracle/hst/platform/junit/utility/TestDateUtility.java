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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   TestDateUtility.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestDateUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.junit.utility;

import java.util.Calendar;

import java.util.regex.Pattern;

import java.text.SimpleDateFormat;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.hst.platform.core.utility.DateUtility;

import oracle.hst.platform.junit.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestDateUtility
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Test coverage for date and calender formatting and conversion.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestDateUtility extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestDateUtility</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestDateUtility() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = { TestDateUtility.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSystemDate
  @Test
  public void testSystemDate() {
    System.out.println(DateUtility.formatDate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSystemDateXMLZulu
  @Test
  public void testSystemDateXMLZulu() {
    System.out.println(DateUtility.formatDate(DateUtility.XML8601_ZULU));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testConversion
  @Test
  public void testConversion() {
    final String origin = "2010-01-23T04:56:22Z";
    try {
      final Calendar         calendar  = DateUtility.parseDate(origin, DateUtility.XML8601_ZULU);
      final SimpleDateFormat formatter = new SimpleDateFormat(DateUtility.XML8601_ZULU);
      formatter.setTimeZone(DateUtility.UTC);
      final String           value     = formatter.format(calendar.getTime());
      assertEquals(origin, value);
    }
    catch (Exception e) {
      failed(e);
    }
  }

  @Test
  public void testConversionDisplay() {
    final String origin = "2010-01-23T04:56:22Z";
    try {
      final Calendar calendar = DateUtility.parseDate(origin, DateUtility.XML8601_ZULU);
      final String   value    = DateUtility.formatDate(calendar.getTime(), DateUtility.XML8601_ZULU);
      assertEquals(origin, value);
    }
    catch (Exception e) {
      failed(e);
    }
  }
  @Test
  public void testConversionFormat() {
    final String origin = "2010-01-23T04:56:22Z";
    try {
      final Calendar calendar = DateUtility.parseDate(origin, DateUtility.XML8601_ZULU);
      final String   value    = DateUtility.formatDate(calendar, DateUtility.XML8601_ZULU);
      assertEquals(origin, value);
    }
    catch (Exception e) {
      failed(e);
    }
  }

  @Test
  public void testDetectDateValue() {
    final String iso8601      = "2010-01-23 04:56:22";
    final String iso8601_zulu = "2010-01-23 04:56:22Z";
    final String xml8601      = "2010-01-23T04:56:22";
    final String xml8601_zulu = "2010-01-23T04:56:22Z";
    final String text         = "Hallo";
    Pattern pp = Pattern.compile("^[0-9,-]*T[0-9,:]*Z$");
    assertTrue(pp.matcher(xml8601_zulu).matches());
    assertFalse(pp.matcher(iso8601_zulu).matches());
    assertFalse(pp.matcher(iso8601).matches());
    assertFalse(pp.matcher(xml8601).matches());
    assertFalse(pp.matcher(text).matches());
  }
}