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

    File        :   TestApplication.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestApplication.


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
// class TestApplication
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The test marshallin/unmarshalling <code>ApplicationEntity</code> resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestApplication extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestApplication</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestApplication() {
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
    final String[] parameter = { TestApplication.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAccountCreateSingle
  /**
   ** Test unmarshallin/marshalling a provisioning request to create a single
   ** account with a surrounding <code>ApplicationEntity</code> in the form:
   ** <pre>
   **   { "application": "CTSAccount"
   **   , "accounts" : [
   **       { "id"         : "azitterbacke"
   **       , "action"     : "create"
   **       , "attributes" : [
   **           { "id"     : "firstName"
   **           , "value"  : "Alfons"
   **           }
   **         , { "id"     : "lastName"
   **           , "value"  : "Zitterbacke"
   **           }
   **         ]
   **       , "entitlements"       : [
   **           { "namespace"      : "group"
   **           , "actions"        : [
   **                { "action"     : "revoke"
   **                , "risk"       : "low"
   **                , "attributes" : [
   **                    { "id"     : "name"
   **                    , "value"  : "cn=Dude,dc=example,dc=com"
   **                    }
   **                  ]
   **                }
   **              , { "action"     : "assign"
   **                , "risk"       : "low"
   **                , "attributes" : [
   **                    { "id"     : "name"
   **                    , "value"  : "cn=Other Dude,dc=example,dc=com"
   **                    }
   **                  ]
   **                }
   **             ]
   **           }
   **         ]
   **       }
   **     ] 
   **   } 
   ** </pre
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testAccountCreateSingle()
    throws Exception {

    final String literal = fromFile("test-application-account-create-single.json");
    final ApplicationEntity request = Schema.unmarshalApplication(
      Json.createReader(new StringReader(literal))
    );
    notNull(request);
    // as documented one account is requested
    equals(request.size(),  1);

    final AccountEntity subject = request.get(0);
    notNull(subject);
    equals("azitterbacke",                     subject.id());
    equals(AccountEntity.Action.create.name(), subject.action().name());
    equals("Alfons",                           subject.value("firstName"));
    equals("Zitterbacke",                      subject.value("lastName"));

    // marshal into a new response
    final JsonObject response = Schema.marshalApplication(request);
    equals(literal, response.toString());

    // check entries one is the di otherone is the accounts element
    equals(response.size(),  2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAccountDeleteSingle
  /**
   ** Test unmarshallin/marshalling a provisioning request to a delete a single
   ** account with a surrounding <code>ApplicationEntity</code> in the form:
   ** <pre>
   **   { "application": "CTSAccount"
   **   , "accounts"   : [
   **       { "id"     : "azitterbacke"
   **       , "action" : "delete"
   **       }
   **     ] 
   **   } 
   ** </pre>
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testAccountDeleteSingle()
    throws Exception {

    final String literal = fromFile("test-application-account-delete-single.json");
    final ApplicationEntity request = Schema.unmarshalApplication(
      Json.createReader(new StringReader(literal))
    );
    notNull(request);
    equals(request.size(),  1);

    final AccountEntity subject = request.get(0);
    notNull(subject);
    notNull(subject);
    equals("azitterbacke",                     subject.id());
    equals(AccountEntity.Action.delete.name(), subject.action().name());
    equals(subject.size(), 0);
    equals(subject.namespace(), null);

    // marshal into a new response
    final JsonObject response = Schema.marshalApplication(request);
    equals(literal, response.toString());

    // check entries one is the di otherone is the accounts element
    equals(response.size(),  2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAccountMixedMultiple
  /**
   ** Test unmarshallin/marshalling provisioning request for multiple accounts
   ** and diffenrent actions with a surrounding <code>ApplicationEntity</code>
   ** in the form:
   ** <pre>
   **   { "application": "CTSAccount"
   **   , "accounts" : [
   **       { "id"         : "azitterbacke"
   **       , "action"     : "create"
   **       , "attributes" : [
   **           { "id"     : "firstName"
   **           , "value"  : "Alfons"
   **           }
   **         , { "id"     : "lastName"
   **           , "value"  : "Zitterbacke"
   **           }
   **         ]
   **       , "entitlements"       : [
   **           { "namespace"      : "group"
   **           , "actions"        : [
   **               { "action"     : "revoke"
   **               , "risk"       : "low"
   **               , "attributes" : [
   **                   { "id"     : "name"
   **                   , "value"  : "cn=Dude,dc=example,dc=com"
   **                   }
   **                 ]
   **               }
   **             , { "action"     : "assign"
   **               , "risk"       : "low"
   **               , "attributes" : [
   **                   { "id"     : "name"
   **                   , "value"  : "cn=Other Dude,dc=example,dc=com"
   **                   }
   **                 ]
   **               }
   **             ]
   **           }
   **         ]
   **       }
   **     , { "id"         : "mmustermann"
   **       , "action"     : "modify"
   **       , "attributes" : [
   **           { "id"     : "firstName"
   **           , "value"  : "Max"
   **           }
   **         , { "id"     : "lastName"
   **           , "value"  : "Mustermann"
   **           }
   **         ]
   **       }
   **     , { "id"         : "amusterfrau"
   **       , "action"     : "delete"
   **       }
   **     ]
   **   }
   ** </pre>
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testAccountMixedMultiple()
    throws Exception {
    
    final String literal = fromFile("test-application-account-mixed-multiple.json");
    final ApplicationEntity request = Schema.unmarshalApplication(
      Json.createReader(new StringReader(literal))
    );
    notNull(request);
    equals(request.size(),  3);

    final AccountEntity azitterbacke = request.get(0);
    notNull(azitterbacke);
    equals("azitterbacke",                     azitterbacke.id());
    equals(AccountEntity.Action.create.name(), azitterbacke.action().name());
    equals("Alfons",                           azitterbacke.value("firstName"));
    equals("Zitterbacke",                      azitterbacke.value("lastName"));
    notEquals(azitterbacke.namespace(), null);
    equals(azitterbacke.namespace().size(), 1);

    final AccountEntity mmustermann = request.get(1);
    notNull(mmustermann);
    equals("mmustermann",                      mmustermann.id());
    equals(AccountEntity.Action.modify.name(), mmustermann.action().name());
    equals(mmustermann.size(), 2);
    equals("Max",                              mmustermann.value("firstName"));
    equals("Mustermann",                       mmustermann.value("lastName"));
    equals(mmustermann.namespace(), null);

    final AccountEntity amusterfrau = request.get(2);
    notNull(mmustermann);
    equals("amusterfrau",                      amusterfrau.id());
    equals(AccountEntity.Action.delete.name(), amusterfrau.action().name());
    equals(amusterfrau.size(), 0);
    equals(amusterfrau.namespace(), null);
  }
}