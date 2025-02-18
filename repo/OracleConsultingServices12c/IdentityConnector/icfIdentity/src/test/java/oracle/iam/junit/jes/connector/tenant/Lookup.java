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

package oracle.iam.junit.jes.connector.tenant;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.junit.jes.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** The test case lookup operation on organizations at the target system
 ** leveraging the connector implementation directly.
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
      CONTEXT.lookupOrganization("Unknown");
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupTop
  /**
   ** Test search request leveraging server context.
   */
  @Test
  public void lookupTop() {
    try {
      final Organization identity = CONTEXT.lookupOrganization(Tenant.TOP.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Tenant.TOP.value);
      assertEquals(identity.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()), Tenant.TOP.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupXellerate
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   */
  @Test
  public void lookupXellerate() {
    try {
      final Organization identity = CONTEXT.lookupOrganization(Tenant.LEGACY.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Tenant.LEGACY.value);
      assertEquals(identity.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()), Tenant.LEGACY.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSaxonyAnhalt
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   */
  @Test
  public void lookupSaxonyAnhalt() {
    try {
      final Organization identity = CONTEXT.lookupOrganization(Tenant.AN.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Tenant.AN.value);
      assertEquals(identity.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()), Tenant.AN.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupBrandenburg
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   */
  @Test
  public void lookupBrandenburg() {
    try {
      final Organization identity = CONTEXT.lookupOrganization(Tenant.BB.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Tenant.BB.value);
      assertEquals(identity.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()), Tenant.BB.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupBerlin
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   */
  @Test
  public void lookupBerlin() {
    try {
      final Organization identity = CONTEXT.lookupOrganization(Tenant.BE.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Tenant.BE.value);
      assertEquals(identity.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()), Tenant.BE.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupFederalPoliceOffice
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   */
  @Test
  public void lookupFederalPoliceOffice() {
    try {
      final Organization identity = CONTEXT.lookupOrganization(Tenant.BK.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Tenant.BK.value);
      assertEquals(identity.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()), Tenant.BK.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupFederalPolice
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   */
  @Test
  public void lookupFederalPolice() {
    try {
      final Organization identity = CONTEXT.lookupOrganization(Tenant.BP.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Tenant.BP.value);
      assertEquals(identity.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()), Tenant.BP.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupBadenWuerttemberg
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   */
  @Test
  public void lookupBadenWuerttemberg() {
    try {
      final Organization identity = CONTEXT.lookupOrganization(Tenant.BW.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Tenant.BW.value);
      assertEquals(identity.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()), Tenant.BW.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupBavaria
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   */
  @Test
  public void lookupBavaria() {
    try {
      final Organization identity = CONTEXT.lookupOrganization(Tenant.BY.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Tenant.BY.value);
      assertEquals(identity.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()), Tenant.BY.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}