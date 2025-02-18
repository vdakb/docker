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

    File        :   TestParserOption.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestParserOption.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.v2;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.platform.scim.BadRequestException;

import oracle.iam.platform.scim.entity.Path;
import oracle.iam.platform.scim.entity.Filter;

import oracle.iam.platform.scim.marshal.Parser;

import oracle.iam.junit.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestParserOption
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Test coverage for using {@code ParserOptions} with {@code Parser}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestParserOption extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestParserOption</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestParserOption() {
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
    final String[] parameter = { TestParserOption.class.getName() };
    JUnitCore.main(parameter);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSemicolon
  /**
   ** Tests {@link Parser.Option} with an extended attribute name character.
   */
  @Test
  public void testSemicolon() {
    final String attribute  = "attribute;x-tag";
    final String expression = attribute + " eq 123";
    // verify filter is rejected by default
    assertFalse(Parser.option().get().contains(';'));
    try {
      Parser.filter(expression);
      failed("Parser should have rejected '" + expression + "'");
    }
    catch (BadRequestException e) {
      assertTrue(e.error().detail().startsWith("Unexpected character [;] at position 9"));
    }

    // verify filter is permitted after we specify the option.
    Parser.option().add(';');
    assertTrue(Parser.option().get().contains(';'));

    try {
      final Filter filter = Parser.filter(expression);
      assertEquals(filter.path().toString(),  attribute);
      assertEquals(filter.type().value(),  "eq");
      assertEquals(filter.value().toString(), "123");
    }
    catch (BadRequestException e) {
      failed(e);
    }
    // verify attribute is rejected after we remove the option.
    Parser.option().clear();
    assertFalse(Parser.option().get().contains(';'));
    try {
      Parser.filter(expression);
      failed("Parser should have rejected '" + expression + "'");
    }
    catch (BadRequestException e) {
      assertTrue(e.error().detail().startsWith("Unexpected character [;] at position 9"));
    }
  }
}