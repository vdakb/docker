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

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Identity Governance Provisioning

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector.systemrole;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.rolemgmt.vo.Role;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.jes.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** The test case lookup operation on roles at the target system leveraging the
 ** connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Lookup extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Lookup</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Lookup() {
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
    final String[] parameter = {Lookup.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupUnknown
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupUnknown() {
    try {
      CONTEXT.lookupSystemRole("Unknown");
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"RMI-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSystemAdministrator
  /**
   ** Test that a particular role could be fetched by its unique identifier.
   */
  @Test
  public void lookupSystemAdministrator() {
    try {
      final Role identity = CONTEXT.lookupSystemRole(RoleManagerConstants.SYS_ADMIN_ROLE_NAME);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), "1");
      assertEquals(identity.getName(), RoleManagerConstants.SYS_ADMIN_ROLE_NAME);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAdministrator
  /**
   ** Test that a particular role could be fetched by its unique identifier.
   */
  @Test
  public void lookupAdministrator() {
    try {
      final Role identity = CONTEXT.lookupSystemRole("Administrators");
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), "5");
      assertEquals(identity.getName(), "Administrators");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupBIReportAdministrator
  /**
   ** Test that a particular role could be fetched by its unique identifier.
   */
  @Test
  public void lookupBIReportAdministrator() {
    try {
      final Role identity = CONTEXT.lookupSystemRole("BIReportAdministrator");
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), "6");
      assertEquals(identity.getName(), "BIReportAdministrator");
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}