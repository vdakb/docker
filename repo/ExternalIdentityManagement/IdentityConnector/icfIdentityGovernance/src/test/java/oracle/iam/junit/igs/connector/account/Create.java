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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Identity Governance Service SCIM

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.connector.account;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.scim.schema.Name;
import oracle.iam.identity.icf.scim.schema.Email;
import oracle.iam.identity.icf.scim.schema.PhoneNumber;

import oracle.iam.identity.icf.scim.v2.schema.AccountResource;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case create operation on users at the target system leveraging the
 ** connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Create extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Create</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Create() {
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
    final String[] parameter = {Create.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Test create request.
   **
   ** bjensen@example.com
   */
  @Test
  public void execute() {
    final List<Email>  emails = new ArrayList<Email>();
    emails.add(new Email().type(Email.WORK).value("bjensen@example.com").primary(true));
    emails.add(new Email().type(Email.HOME).value("babs@jensen.org"));

    final List<PhoneNumber>  phone = new ArrayList<PhoneNumber>();
    phone.add(new PhoneNumber().type(PhoneNumber.WORK).value("555-555-5555").primary(true));
    phone.add(new PhoneNumber().type(PhoneNumber.MOBILE).value("555-555-4444"));

    final AccountResource request   = new AccountResource()
    .active(true)
    .userName("bjensen@example.com")
    .displayName("Babs Jensen")
    .nickName("Babs")
    .name(
      new Name()
        .familyName("Jensen")
        .givenName("Barbara")
        .middleName("Jane")
        .honorificPrefix("Ms.")
        .honorificSuffix("III")
      )
    .userType("Employee")
    .preferredLanguage("en")
    .locale("en-US")
    .email(emails)
    .phoneNumber(phone)
    ;
    try {
      Base.latest = CONTEXT.createAccount(request);
      assertNotNull(Base.latest);
      assertNotNull(Base.latest.id());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}