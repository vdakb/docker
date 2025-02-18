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

package oracle.iam.junit.jes.connector.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.jes.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** The test case lookup operation on identities at the target system leveraging
 ** the connector implementation directly.
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
      CONTEXT.lookupUser(Identity.UNKNOWN.tag);
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSystemAdministrator
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupSystemAdministrator() {
    try {
      final User identity = CONTEXT.lookupUser(Identity.XELSYSADM.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Identity.XELSYSADM.value);
      assertEquals(identity.getLogin(),    Identity.XELSYSADM.tag.toUpperCase());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSystemInternal
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupSystemInternal() {
    try {
      final User identity = CONTEXT.lookupUser(Identity.OIMINTERNAL.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Identity.OIMINTERNAL.value);
      assertEquals(identity.getLogin(),    Identity.OIMINTERNAL.tag.toUpperCase());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSystemServer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupSystemServer() {
    try {
      final User identity = CONTEXT.lookupUser(Identity.WEBLOGIC.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Identity.WEBLOGIC.value);
      assertEquals(identity.getLogin(),    Identity.WEBLOGIC.tag.toUpperCase());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSystemLegacy
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupSystemLegacy() {
    try {
      final User identity = CONTEXT.lookupUser(Identity.XELOPERATOR.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Identity.XELOPERATOR.value);
      assertEquals(identity.getLogin(),    Identity.XELOPERATOR.tag.toUpperCase());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupMusterfrau
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupMusterfrau() {
    try {
      final User identity = CONTEXT.lookupUser(Identity.MUSTERFRAU.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Identity.MUSTERFRAU.value);
      assertEquals(identity.getLogin(),    Identity.MUSTERFRAU.tag.toUpperCase());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupZitterbacke
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupZitterbacke() {
    try {
      final User identity = CONTEXT.lookupUser(Identity.ZITTERBACKE.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Identity.ZITTERBACKE.value);
      assertEquals(identity.getLogin(),    Identity.ZITTERBACKE.tag.toUpperCase());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupCambrault
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupCambrault() {
    try {
      final User identity = CONTEXT.lookupUser(Identity.CAMBRAULT.tag);
      assertNotNull(identity);
      assertNotNull(identity.getEntityId());
      assertEquals(identity.getEntityId(), Identity.CAMBRAULT.value);
      assertEquals(identity.getLogin(),    Identity.CAMBRAULT.tag.toUpperCase());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}