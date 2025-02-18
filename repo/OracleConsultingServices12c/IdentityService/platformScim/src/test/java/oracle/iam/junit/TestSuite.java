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

    File        :   TestSuite.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestSuite.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit;

import org.junit.runner.Result;
import org.junit.runners.Suite;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;

import org.junit.runner.notification.Failure;

import oracle.iam.junit.scim.v2.TestFilterParser;
import oracle.iam.junit.scim.v2.TestPathParsing;
import oracle.iam.junit.scim.v2.TestListResponse;
import oracle.iam.junit.scim.v2.TestParserOption;
import oracle.iam.junit.scim.v2.TestUserResource;
import oracle.iam.junit.scim.v2.TestGroupResource;
import oracle.iam.junit.scim.v2.TestErrorResponse;
import oracle.iam.junit.scim.v2.TestSchemaValidator;
import oracle.iam.junit.scim.v2.TestFilterEvaluator;

////////////////////////////////////////////////////////////////////////////////
// class TestSuite
// ~~~~~ ~~~~~~~~~
/**
 ** Run the entire test case suite.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
  { TestParserOption.class
  , TestPathParsing.class
  , TestFilterParser.class
  , TestFilterEvaluator.class
  , TestListResponse.class
  , TestErrorResponse.class
  , TestSchemaValidator.class
  , TestGroupResource.class
  , TestUserResource.class
  }
)
public class TestSuite {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestSuite</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestSuite() {
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
    final String[] parameter = { TestSuite.class.getName() };
    final Result   result    = JUnitCore.runClasses(TestSuite.class);
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
    System.out.println(result.wasSuccessful());
  }
}