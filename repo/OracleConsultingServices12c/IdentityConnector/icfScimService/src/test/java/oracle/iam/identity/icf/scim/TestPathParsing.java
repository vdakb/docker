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

    File        :   TestPathParsing.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestPathParsing.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestPathParsing
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Test coverage for parsing SCIM 2 paths.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestPathParsing extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  private static class Tupel {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Path   expected;
    private String expression;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     **
     ** @param  expression       the string representation of the path to
     **                          from.
     **                          <br>
     **                          Allowed object is array of {@link String}.
     ** @param  expected         the expected parsed path instance.
     **                          <br>
     **                          Allowed object is array of {@link Path}.
     */
    private Tupel(final String expression, final Path expected) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.expected   = expected;
      this.expression = expression;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestPathParsing</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestPathParsing() {
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
    final String[] parameter = { TestPathParsing.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testParseValidPath
  /**
   ** Tests the {@code from} method with a valid path string.
   */
  @Test()
  public void testParseValidPath() {
    try {
      for (Tupel cursor : validPath()) {
        final Path parsed = Path.from(cursor.expression);
        assertEquals(parsed, cursor.expected);
      }
    }
    catch (ServiceException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testParseInvalidPath
  /**
   ** Tests the {@code from} method with an invalid path string.
   ** @param  expression         the string representation of the path to
   **                            from.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @Test
  public void testParseInvalidPath() {
    try {
      for (Tupel cursor : invalidPath()) {
        Path.from(cursor.expression);
        failed("Unexpected successful from of invalid path: " + cursor.expression);
      }
    }
    catch (ServiceException e) {
      assertTrue(e.getMessage().startsWith("Unexpected end of path string"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testParseValidPath
  /**
   ** Tests a set of valid path strings.
   */
  private Tupel[] validPath()
    throws ServiceException {

    return new Tupel[] {
      new Tupel("attr",               Path.build().attribute("attr"))
    , new Tupel("urn:extension:attr", Path.build("urn:extension", "attr"))
    , new Tupel("attr.subAttr",       Path.build().attribute("attr").attribute("subAttr"))
    , new Tupel("attr[subAttr eq \"78750\"].subAttr", Path.build().attribute("attr", Filter.eq("subAttr", "78750")).attribute("subAttr"))
    , new Tupel("attr.$ref", Path.build().attribute("attr").attribute("$ref"))
    , new Tupel("attr[$ref eq \"/Users/xxx\"]", Path.build().attribute("attr", Filter.eq("$ref", "/Users/xxx")))
    , new Tupel("urn:extension:attr[subAttr eq \"0815\"].subAttr", Path.build("urn:extension").attribute("attr", Filter.eq("subAttr", "0815")).attribute("subAttr"))
    , new Tupel("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:employeeNumber", Path.build("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").attribute("employeeNumber"))
//    , new Tupel("urn:oracle:schemas:sample:profile:1.0:topicPreferences[(id eq \"urn:x-redsecurity:topic:clothing:shoes\" and strength eq 10 and timeStamp eq \"2015-10-12T14:57:36.494Z\")]", Path.from("urn:oracle:schemas:sample:profile:1.0").attribute("topicPreferences", Filter.and(Filter.eq("id", "urn:x-redsecurity:topic:clothing:shoes"), Filter.eq("strength", 10), Filter.eq("timeStamp", "2015-10-12T14:57:36.494Z")))
    // the following does not technically conform to the SCIM spec but our path
    // impl is lenient so it may be used with any JSON object
    , new Tupel("", Path.build())
    , new Tupel("urn:extension:", Path.build("urn:extension"))
    , new Tupel("urn:extension:attr[subAttr eq \"0815\"].subAttr[subSub pr].this.is.crazy[type eq \"good\"].deep", Path.build("urn:extension").attribute("attr", Filter.eq("subAttr", "0815")).attribute("subAttr", Filter.pr("subSub")).attribute("this").attribute("is").attribute("crazy", Filter.eq("type", "good")).attribute("deep"))
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidPath
  /**
   ** Retrieves a set of invalid path strings.
   **
   ** @return                    a set of invalid path strings.
   */
  private Tupel[] invalidPath() {
    return new Tupel[] {
      new Tupel(".", null)
    , new Tupel("attr.", null)
    , new Tupel("urn:attr", null)
    , new Tupel("attr[].subAttr", null)
    , new Tupel(".attr", null)
    , new Tupel("urn:extension:.", null)
    , new Tupel("urn:extension:.attr", null)
    , new Tupel("attr[subAttr eq 123].", null)
    };
  }
}