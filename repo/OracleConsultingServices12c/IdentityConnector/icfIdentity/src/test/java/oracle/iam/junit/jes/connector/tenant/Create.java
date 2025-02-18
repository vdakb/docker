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

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector.tenant;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.jes.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case create operation on organizations at the target system
 ** leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
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
  // Method:   createUnknown
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createUnknown() {
    try {
      CONTEXT.createOrganization("Unknown");
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTop
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createTop() {
    try {
      assertEquals(CONTEXT.createOrganization(Tenant.TOP.tag), Tenant.TOP.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createXellerate
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createXellerate() {
    try {
      assertEquals(CONTEXT.createOrganization(Tenant.LEGACY.tag), Tenant.LEGACY.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSaxonyAnhalt
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createSaxonyAnhalt() {
    try {
      assertEquals("AN", CONTEXT.createOrganization(Tenant.AN.tag), Tenant.AN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBrandenburg
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createBrandenburg() {
    try {
      assertEquals("BB", CONTEXT.createOrganization(Tenant.BB.tag), Tenant.BB.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBerlin
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createBerlin() {
    try {
      assertEquals("BE", CONTEXT.createOrganization(Tenant.BE.tag), Tenant.BE.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFederalPoliceOffice
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createFederalPoliceOffice() {
    try {
      assertEquals("BK", CONTEXT.createOrganization(Tenant.BK.tag), Tenant.BK.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFederalPolice
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createFederalPolice() {
    try {
      assertEquals("BP", CONTEXT.createOrganization(Tenant.BP.tag), Tenant.BP.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBadenWuerttemberg
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createBadenWuerttemberg() {
    try {
      assertEquals("BW", CONTEXT.createOrganization(Tenant.BW.tag), Tenant.BW.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBavaria
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createBavaria() {
    try {
      assertEquals("BY", CONTEXT.createOrganization(Tenant.BY.tag), Tenant.BY.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}