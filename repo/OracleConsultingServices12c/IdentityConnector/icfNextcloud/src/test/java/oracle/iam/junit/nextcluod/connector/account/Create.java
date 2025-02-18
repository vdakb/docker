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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Nextcloud Connector

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the interface
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.nextcloud.connector.account;

import java.io.File;

import oracle.iam.identity.icf.connector.nextcloud.schema.Service;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.nextcloud.schema.User;

import oracle.iam.identity.icf.foundation.object.Pair;
import oracle.iam.junit.nextcloud.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case create operation on identities at the target system leveraging
 ** the connector implementation directly.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Create extends TestFixture {

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
  // Method:   zitterbacke
  /**
   ** Test create request leveraging server context.
   ** <p>
   ** <b>Alfons Zitterbacke</b>
   */
  @Test
  public void zitterbacke() {
    create(Identity.ZITTERBACKE);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Test create request leveraging server context.
   */
  protected void create(final Pair<String, File> identity) {
    final User origin = user(identity.value);
    assertNotNull(origin);
    assertEquals(Service.LOGIN,     origin.login(), identity.tag);
    assertNotNull(Service.PASSWORD, origin.name());
    assertNotNull(Service.DISABLED, origin.disabled());
    assertNotNull(Service.ADMIN,    origin.administrator());
    assertNotNull(Service.NAME,     origin.name());
    assertNotNull(Service.EMAIL,    origin.email());
    try {
      final String identifier = CONTEXT.createUser(origin);
      assertNotNull(identifier);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}