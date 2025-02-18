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
    Subsystem   :   Generic SCIM Library

    File        :   TestFilterEvaluator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestFilterEvaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim;

import java.util.Date;

import org.junit.Test;
import org.junit.BeforeClass;

import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.databind.JsonNode;

import oracle.iam.identity.icf.TestBase;

import oracle.iam.identity.icf.scim.schema.Support;

import oracle.iam.identity.icf.scim.utility.FilterEvaluator;

////////////////////////////////////////////////////////////////////////////////
// class TestFilterEvaluator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Test coverage for evaluating SCIM 2 filters.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestFilterEvaluator extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static Date     date;
  private static JsonNode node;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestFilterEvaluator</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestFilterEvaluator() {
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
    final String[] parameter = { TestFilterEvaluator.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Setup some basic schemas.
   **
   ** @throws Exception          if an error occurs.
   */
  @BeforeClass
  public static void prepare()
    throws Exception {

    date = new Date();
    node = Support.objectReader().readTree(
      "{\n"
    + "\"externalId\": \"user:externalId\",\n"
    + "    \"id\": \"user:id\",\n"
    + "    \"meta\": {\n"
    + "        \"created\": \"2014-02-27T11:29:39Z\",\n"
    + "        \"lastModified\": \"2015-02-27T11:29:39Z\",\n"
    + "        \"location\": \"http://here/user\",\n"
    + "        \"resourceType\": \"some resource type\",\n"
    + "        \"version\": \"1.0\"\n"
    + "    },\n"
    + "    \"name\": {\n"
    + "        \"first\": \"name:first\",\n"
    + "        \"last\": \"name:last\",\n"
    + "        \"middle\": \"name:middle\"\n"
    + "    },\n"
    + "    \"shoeSize\" : \"12W\",\n"
    + "    \"weight\" : 175.6,\n"
    + "    \"children\" : 5,\n"
    + "    \"true\" : true,\n"
    + "    \"false\" : false,\n"
    + "    \"null\" : null,\n"
    + "    \"empty\" : [],\n"
    + "    \"addresses\": [\n"
    + "      {\n"
    + "        \"type\": \"work\",\n"
    + "        \"streetAddress\": \"100 Universal City Plaza\",\n"
    + "        \"locality\": \"Hollywood\",\n"
    + "        \"region\": \"CA\",\n"
    + "        \"postalCode\": \"91608\",\n"
    + "        \"priority\": 0,\n"
    + "        \"country\": \"USA\",\n"
    + "        \"formatted\": \"100 Universal City Plaza\\nHollywood, CA 91608 USA\",\n"
    + "        \"primary\": true\n"
    + "      },\n"
    + "      {\n"
    + "        \"type\": \"home\",\n"
    + "        \"streetAddress\": \"456 Hollywood Blvd\",\n"
    + "        \"locality\": \"Hollywood\",\n"
    + "        \"region\": \"CA\",\n"
    + "        \"postalCode\": \"91608\",\n"
    + "        \"priority\": 10,\n"
    + "        \"country\": \"USA\",\n"
    + "        \"formatted\": \"456 Hollywood Blvd\\nHollywood, CA 91608 USA\"\n"
    + "      }\n"
    + "    ],\n"
    + "    \"password\": \"user:password\",\n"
    + "    \"schemas\": ["
    + "    \"urn:orclidentity:schemas:baseSchema\", "
    + "    \"urn:orclidentity:schemas:favoriteColor\""
    + "    ],\n"
    + "    \"urn:orclidentity:schemas:favoriteColor\": {\n"
    + "        \"favoriteColor\": \"extension:favoritecolor\"\n"
    + "    },\n"
    + "    \"userName\": \"user:username\",\n"
    + "    \"friends\":[\n"
    + "      {\n"
    + "        \"displayName\": \"Babs Jensen\",\n"
    + "        \"$ref\": \"Users/BabsJensen\"\n"
    + "      }\n"
    + "    ]\n"
    + "}"
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSanity
  /**
   ** Check the full filter spec representation against.
   ** <br>
   ** Shouldn't be any issues.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testSanity()
    throws Exception {

    match(true,  "name.first eq \"nAme:fiRst\"");
    match(false, "naMe.fIrst ne \"nAme:fiRst\"");
    match(true,  "null eq null");
    match(true,  "unassigned eq null");
    match(true,  "empty eq null");
    match(false, "null ne null");
    match(false, "unassigned ne null");
    match(false, "empty ne null");
    match(true,  "name.first co \"nAme:fiRst\"");
    match(true,  "name.first sw \"nAme:fiRst\"");
    match(true,  "naMe.First ew \"nAme:fiRst\"");
    match(true,  "name.first sw \"nAme:\"");
    match(true,  "name.first ew \":fiRst\"");
    match(false, "not (weight gt 175.2)");
    match(true,  "weight gt 175.2");
    match(true,  "weight gt 175");
    match(false, "weight gt 175.6");
    match(true,  "weight ge 175.6");
    match(true,  "weight ge 175");
    match(true,  "Weight lt 175.8");
    match(true,  "weight lt 176");
    match(false, "weight lt 175.6");
    match(true,  "weight le 175.6");
    match(true,  "weight le 176");
    match(true,  "children gt 4.5");
    match(true,  "children gt 4");
    match(false, "children gt 5");
    match(true,  "children ge 5");
    match(true,  "children ge 4");
    match(true,  "Children lt 5.5");
    match(true,  "children lt 6");
    match(false, "children lt 5");
    match(true,  "children le 5");
    match(true,  "children le 6");
    match(true,  "children pr");
    match(false, "null pr");
    match(false, "unassigned pr");
    match(false, "empty pr");
    match(true,  "true eq true and false eq false");
    match(false, "true eq true and true eq false");
    match(true,  "true eq true or false eq false");
    match(true,  "true eq true or true eq false");
    match(false, "not(true eq true)");
    match(true,  "not(true eq false)");
    match(true,  "addresses[type eq \"home\" and streetAddress co \"Hollywood\"]");
    match(false, "addresses[type eq \"work\" and streetAddress co \"Hollywood\"]");
    match(true,  "addresses.type eq \"work\" and addresses.streetAddress co \"Hollywood\"");
    match(true,  "addresses[priority gt 5 and StreetAddress co \"Hollywood\"]");
    match(true,  "friends[$ref eq \"Users/BabsJensen\"]");
    match(false, "friends[$ref eq \"Users/Nonexistent\"]");
    match(true,  "addresses.priority ge 10");
    match(true,  "addresses.priority le 0");
    match(true,  "meta.created eq \"2014-02-27T11:29:39Z\"");
//    match(true,  "meta.created eq \"" + DateTimeUtils.format(date, TimeZone.getTimeZone("CST")) + "\"");
//    match(true,  "meta.created eq \"" + DateTimeUtils.format(date, TimeZone.getTimeZone("PST")) + "\"");
//    match(true,  "meta.created ge \"" + DateTimeUtils.format(date, TimeZone.getTimeZone("CST")) + "\"");
//    match(true,  "meta.created le \"" + DateTimeUtils.format(date, TimeZone.getTimeZone("CST")) + "\"");
//    match(false, "meta.created gt \"" + DateTimeUtils.format(date, TimeZone.getTimeZone("CST")) + "\"");
//    match(false, "meta.created lt \"" + DateTimeUtils.format(date, TimeZone.getTimeZone("CST")) + "\"");
//    match(false, "meta.created gt \"" + DateTimeUtils.format(new Date(date.getTime() + 1000), TimeZone.getTimeZone("CST")) + "\"");
//    match(true,  "meta.created lt \"" + DateTimeUtils.format(new Date(date.getTime() + 1000), TimeZone.getTimeZone("CST")) + "\"");
//    match(true,  "meta.created gt \"" + DateTimeUtils.format(new Date(date.getTime() - 1000), TimeZone.getTimeZone("CST")) + "\"");
//    match(false, "meta.created lt \"" + DateTimeUtils.format(new Date(date.getTime() - 1000), TimeZone.getTimeZone("CST")) + "\"");

    match(true,  "schemas[value eq \"urn:orclidentity:schemas:baseSchema\"]");
    match(true,  "schemas eq \"urn:orclidentity:schemas:baseSchema\"");
    match(false, "schemas[value eq \"urn:orclidentity:schemas:something\"]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testLessThan
  /**
   ** Test the less than filter.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testLessThan()
    throws Exception {

    // node value is greater than that in filter
    final Filter bad_gt  = Filter.lt("children", new Integer(4));
    // node value is equal to that in filter
    final Filter bad_eq  = Filter.lt("children", new Integer(5));
    // node value is less than that in filter
    final Filter good_lt = Filter.lt("children", new Integer(7));

    assertFalse(FilterEvaluator.evaluate(bad_gt, node));
    assertFalse(FilterEvaluator.evaluate(bad_eq, node));
    assertTrue(FilterEvaluator.evaluate(good_lt, node));

    assertEquals(bad_gt.type(),  Filter.Type.LT);
    assertEquals(bad_eq.type(),  Filter.Type.LT);
    assertEquals(good_lt.type(), Filter.Type.LT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testLessThanOrEqual
  /**
   ** Test the less than or equal filter.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testLessThanOrEqual()
    throws Exception {

    // node value is greater than that in filter
    final Filter bad_gt  = Filter.le("children", new Integer(4));
    // node value is equal to that in filter
    final Filter bad_eq  = Filter.le("children", new Integer(5));
    // node value is less than that in filter
    final Filter good_lt = Filter.le("children", new Integer(7));

    assertFalse(FilterEvaluator.evaluate(bad_gt, node));
    assertTrue(FilterEvaluator.evaluate(bad_eq,  node));
    assertTrue(FilterEvaluator.evaluate(good_lt, node));

    assertEquals(bad_gt.type(),  Filter.Type.LE);
    assertEquals(bad_eq.type(),  Filter.Type.LE);
    assertEquals(good_lt.type(), Filter.Type.LE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testGreaterThan
  /**
   ** Test the greater than filter.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testGreaterThan()
    throws Exception {

    // node value is less than that in filter
    final Filter bad_le  = Filter.gt("children", new Integer(7));
    // node value is equal to that in filter
    final Filter bad_eq  = Filter.gt("children", new Integer(5));
    // node value is greater than that in filter
    final Filter good_lt = Filter.gt("children", new Integer(4));

    assertTrue(FilterEvaluator.evaluate(good_lt, node));
    assertFalse(FilterEvaluator.evaluate(bad_eq, node));
    assertFalse(FilterEvaluator.evaluate(bad_le, node));

    assertEquals(good_lt.type(), Filter.Type.GT);
    assertEquals(bad_eq.type(),  Filter.Type.GT);
    assertEquals(bad_le.type(),  Filter.Type.GT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testGreaterThanOrEqual
  /**
   ** Test the greater than or equal filter.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testGreaterThanOrEqual()
    throws Exception {

    // node value is greater than that in filter
    Filter good_gt = Filter.ge("children", new Integer(4));
    // node value is equal to that in filter
    Filter good_eq = Filter.ge("children", new Integer(5));
    // node value is less than that in filter
    Filter bad_le  = Filter.ge("children", new Integer(7));

    assertTrue(FilterEvaluator.evaluate(good_gt, node));
    assertTrue(FilterEvaluator.evaluate(good_eq, node));
    assertFalse(FilterEvaluator.evaluate(bad_le, node));

    assertEquals(good_gt.type(), Filter.Type.GE);
    assertEquals(good_eq.type(), Filter.Type.GE);
    assertEquals(bad_le.type(),  Filter.Type.GE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Test that filters matching.
   **
   ** @param  expected           the expected result.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  expression         the filter expression to evaluate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws Exception          if an error occurs.
   */
  private void match(final boolean expected, final String filter)
    throws Exception {

    assertEquals(FilterEvaluator.evaluate(Filter.from(filter), node), expected);
  }
}