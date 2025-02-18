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

    File        :   TestFilterParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestFilterParser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.v2;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.junit.TestBase;

import oracle.iam.platform.scim.BadRequestException;

import oracle.iam.platform.scim.entity.Filter;

////////////////////////////////////////////////////////////////////////////////
// class TestFilterParser
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Test coverage for validate and enforce the filter syntax on JSON objects
 ** representing SCIM resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestFilterParser extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestFilterParser</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestFilterParser() {
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
    final String[] parameter = { TestFilterParser.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testValidExpression
  /**
   ** Tests the parsing method with a valid filter string.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testValidExpression()
    throws Exception {

    valid("userName Eq \"bjensen\"",                       Filter.eq("userName", "bjensen"));
    valid("Username eq \"bjensen\"",                       Filter.eq("Username", "bjensen"));
    valid("userName eq \"bjensen\"",                       Filter.eq("userName", "bjensen"));

    valid("userName ne \"bjensen\"",                       Filter.not(Filter.eq("userName", "bjensen")));
    valid("userName co \"jensen\"",                        Filter.co("userName", "jensen"));
    valid("userName sw \"J\"",                             Filter.sw("userName", "J"));
    valid("userName ew \"sen\"",                           Filter.ew("userName", "sen"));
    valid("title pr",                                      Filter.pr("title"));
    valid("meta.lastModified gt \"2011-05-13T04:42:34Z\"", Filter.gt("meta.lastModified", "2011-05-13T04:42:34Z"));
    valid("meta.lastModified ge \"2011-05-13T04:42:34Z\"", Filter.ge("meta.lastModified", "2011-05-13T04:42:34Z"));
    valid("meta.lastModified lt \"2011-05-13T04:42:34Z\"", Filter.lt("meta.lastModified", "2011-05-13T04:42:34Z"));
    valid("meta.lastModified le \"2011-05-13T04:42:34Z\"", Filter.le("meta.lastModified", "2011-05-13T04:42:34Z"));
    valid(" title  pr  and  userType  eq  \"Employee\" ",  Filter.and(Filter.pr("title"), Filter.eq("userType", "Employee")));
    valid("title pr or userType eq \"Intern\"",            Filter.or(Filter.pr("title"), Filter.eq("userType", "Intern")));
    valid("not(userName ew \"sen\")",                      Filter.not(Filter.ew("userName", "sen")));
    valid("not ( userName ew \"sen\" ) ",                  Filter.not(Filter.ew("userName", "sen")));
    valid("userType eq \"Employee\" and (email co \"example.com\" or email co \"example.org\")", Filter.and(Filter.eq("userType", "Employee"), Filter.or( Filter.co("email", "example.com"), Filter.co("email", "example.org"))));
    valid("userName co \"\\ufe00\\\"\\n\\t\\\\\"",         Filter.co("userName", "\ufe00\"\n\t\\"));
    valid("urn:extension:members eq 25",                   Filter.eq("urn:extension:members", 25));
    valid("urn:extension:members eq 25.52",                Filter.eq("urn:extension:members", 25.52));
    valid("urn:extension:isActive eq true",                Filter.eq("urn:extension:isActive", true));
    valid("urn:extension:isActive eq false",               Filter.eq("urn:extension:isActive", false));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testInvalidExpression
  /**
   ** Tests the parsing method with a invalid filter string.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testInvalidExpression()
    throws Exception {

    invalid("");
    invalid("(");
    invalid(")");
    invalid("()");
    invalid("foo");
    invalid("( title pr ) eq ");
    invalid("username pr \"bjensen\"");
    invalid("meta.lastModified lte \"2011-05-13T04:42:34Z\"");
    invalid("username eq");
    invalid("title pr and userType eq \"Employee\" eq");
    invalid("title pr and userType eq true eq");
    invalid("title pr and userType eq 12345.23 eq");
    invalid("userName eq 'bjensen'");
    invalid("userName eq \"bjensen");
    invalid("userName eq \"bjensen\\");
    invalid("userName eq \"\\a\"");
    invalid("userName eq bjensen");
    invalid("userName co \"\\ufe\" or userName co \"a\"");
    invalid("userName bad \"john\"");
    invalid("userName eq (\"john\")");
    invalid("(userName eq \"john\"");
    invalid("userName eq \"john\")");
    invalid("userName eq \"john\" userName pr");
    invalid("userName pr and");
    invalid("and or");
    invalid("not ( and )");
    invalid("not userName pr");
    invalid("userName pr ()");
    invalid("() userName pr");
    invalid("(userName pr)()");
    invalid("()(userName pr)");
    invalid("userName[])");
    invalid("userName pr[)");
    invalid("userName pr [bar pr])");
    invalid("userName[userName pr)");
    invalid("userName[userName[bar pr]])");
    invalid("userName[userName pr]])");
    invalid("[value eq \"false\"]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valid
  /**
   ** Tests the parsing method with a valid filter string.
   **
   ** @param  expression         the string representation of the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  expected           the expected parsed filter instance.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws Exception          if an error occurs.
   */
  private void valid(final String expression, final Filter expected)
    throws Exception {

    final Filter parsed = Filter.from(expression);
    assertEquals(parsed, expected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalid
  /**
   ** Tests the parsing method with a invalid filter string.
   **
   ** @param  expression         the string representation of the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws Exception          if an error occurs.
   */
  private void invalid(final String expression)
    throws Exception {

    try {
      Filter.from(expression);
    }
    catch (BadRequestException e) {
      assertEquals(e.error().type(), BadRequestException.INVALID_FILTER);
    }
  }
}