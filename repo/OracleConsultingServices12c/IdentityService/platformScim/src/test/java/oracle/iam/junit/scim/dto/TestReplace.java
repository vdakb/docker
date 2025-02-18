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

    File        :   TestReplace.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestReplace.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.dto;

import java.util.List;

import java.util.function.Predicate;

import java.util.stream.Collectors;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.junit.TestBase;

import oracle.iam.platform.scim.ProcessingException;

import oracle.iam.platform.scim.entity.Filter;

////////////////////////////////////////////////////////////////////////////////
// class TestReplace
// ~~~~~ ~~~~~~~~~~~
/**
 ** Test Cases for patch operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestReplace extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestReplace</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestReplace() {
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
    final String[] parameter = { TestReplace.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testCreate
  /**
   ** Test create request.
   **
   ** @throws Exception if an error occurs.
   */
  @Test
  public void testCreate()
    throws Exception
    ,      ProcessingException {

    final Container         gggggg  = new Container();
    final Filter            filter  = Filter.or(Filter.eq("value", 10L), Filter.gt("value", 40L));
//    final Filter            filter  = Filter.eq("value", 10L);
    final Predicate<Entity> visitor = (entity) -> {
      try {
        return Evaluator.evaluate(filter, entity);
      }
      catch (ProcessingException e) {
        throw new javax.ws.rs.ProcessingException(e.getLocalizedMessage());
      }
    };
    List<Entity> remove = gggggg.role.stream().filter(visitor).collect(Collectors.toList());
    System.out.println(remove);
//    Visitor eval = new Visitor();
//    eval.apply(filter, 100L);
  }
}