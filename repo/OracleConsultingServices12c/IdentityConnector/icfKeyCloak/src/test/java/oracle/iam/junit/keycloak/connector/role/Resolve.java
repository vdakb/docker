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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.keycloak.connector.role;

import java.io.File;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.keycloak.schema.Role;
import oracle.iam.identity.icf.connector.keycloak.schema.Service;

import oracle.iam.junit.keycloak.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case lookup operation on identities at the target system leveraging
 ** the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resolve extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resolve</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Resolve() {
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
    final String[] parameter = {Resolve.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultRoles
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>default-roles-myrealm</b>
   */
  @Test
  public void defaultRoles() {
    resolve(RealmRole.DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   umaAuthorization
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>uma_authorization</b>
   */
  @Test
  public void umaAuthorization() {
    resolve(RealmRole.AUTHZ);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   offlineAccess
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>offline_access</b>
   */
  @Test
  public void offlineAccess() {
    resolve(RealmRole.OFFLINE);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolve
  /**
   ** Test lookup request leveraging server context.
   */
  protected void resolve(final Pair<String, File> identity) {
    final Role origin = role(identity.value);
    assertNotNull(origin);
    assertEquals(Service.NAME,  origin.name(), identity.tag);
    try {
      final Role target = CONTEXT.resolveRole(identity.tag);
      assertNotNull(target);
      assertEquals(Service.NAME, target.name(), origin.name());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}