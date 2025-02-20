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

package oracle.iam.junit.jes.connector.globalrole;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.platform.authopss.vo.AdminRole;

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
      CONTEXT.lookupGlobalRole("Unkown");
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"RMI-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMSystemAdministrator
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMSystemAdministrator() {
    try {
      final AdminRole identity = CONTEXT.lookupGlobalRole("OrclOIMSystemAdministrator");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 1L);
      assertFalse(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMSystemAdministrator");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMSystemConfigurator
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMSystemConfigurator() {
    try {
      final AdminRole identity = CONTEXT.lookupGlobalRole("OrclOIMSystemConfigurator");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 2L);
      assertFalse(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMSystemConfigurator");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMCatalogAdmin
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMCatalogAdmin() {
    try {
      final AdminRole identity = CONTEXT.lookupGlobalRole("OrclOIMCatalogAdmin");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 3L);
      assertFalse(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMCatalogAdmin");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMCertificationAdministrator
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMCertificationAdministrator() {
    try {
      final AdminRole identity = CONTEXT.lookupGlobalRole("OrclOIMCertificationAdministrator");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 22L);
      assertFalse(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMCertificationAdministrator");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMCertificationViewer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMCertificationViewer() {
    try {
      final AdminRole identity = CONTEXT.lookupGlobalRole("OrclOIMCertificationViewer");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 23L);
      assertFalse(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMCertificationViewer");
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}