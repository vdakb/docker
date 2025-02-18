/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   TestAccount.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestAccount.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.io.StringReader;

import javax.json.Json;

import javax.json.JsonObject;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import oracle.iam.identity.igs.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestAccount
// ~~~~~ ~~~~~~~~~~~
/**
 ** The test marshallin/unmarshalling <code>AccountEntity</code> resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestAccount extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestAccount</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestAccount() {
    // ensure TestAccount
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
    final String[] parameter = { TestAccount.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAccountCreateSingle
  /**
   ** Test unmarshallin/marshalling a provisioning request for an account in
   ** an known application in the form:
   ** <pre>
   **   { "account"    : "azitterbacke"
   **   , "action"     : "create"
   **   , "attributes" : [
   **       { "id"     : "firstName"
   **       , "value"  : "Alfons"
   **       }
   **     , { "id"     : "lastName"
   **       , "value"  : "Zitterbacke"
   **       }
   **     ]
   **   }
   ** </pre>
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testAccountCreateSingle()
    throws Exception {


    final String literal = fromFile("test-account-create-single.json");
    final AccountEntity request = Schema.unmarshalAccount(
      Json.createReader(new StringReader(literal))
    );
    notNull(request);
    // as documented two attributes firstName/lastName are set
    equals(request.size(),  2);

    equals("azitterbacke",                     request.id());
    equals(AccountEntity.Action.create.name(), request.action().name());
    equals("Alfons",                           request.value("firstName"));
    equals("Zitterbacke",                      request.value("lastName"));

    // marshal into a new response
    final JsonObject response = Schema.marshalAccount(request);
    System.out.println(literal);
    System.err.println(response);
    equals(literal, response.toString());

    // check entries one is the di otherone is the accounts element
    equals(response.size(),  3);
  }
}