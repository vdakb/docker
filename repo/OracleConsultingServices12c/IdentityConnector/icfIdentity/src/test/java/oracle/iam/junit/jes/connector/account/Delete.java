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

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Delete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.jes.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** The test case delete operation on identities at the target system leveraging
 ** the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Delete extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Delete</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Delete() {
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
    final String[] parameter = {Delete.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteUnknown
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteUnknown() {
    try {
      CONTEXT.deleteUser(Identity.UNKNOWN.value);
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSystemAdministrator
  /**
   ** Test reate request leveraging server context.
   */
  @Test
  public void deleteSystemAdministrator() {
    try {
      CONTEXT.deleteUser(Identity.XELSYSADM.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSystemInternal
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteSystemInternal() {
    try {
      CONTEXT.deleteUser(Identity.OIMINTERNAL.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSystemServer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteSystemServer() {
    try {
      CONTEXT.deleteUser(Identity.WEBLOGIC.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSystemLegacy
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteSystemLegacy() {
    try {
      CONTEXT.deleteUser(Identity.XELOPERATOR.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteMusterfrau
  /**
   ** Test delete request leveraging server context.
   ** <p>
   ** <b>Agathe Musterfrau</b>
   */
  @Test
  public void deleteMusterfrau() {
    try {
      CONTEXT.deleteUser(Identity.MUSTERFRAU.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteZitterbacke
  /**
   ** Test delete request leveraging server context.
   ** <p>
   ** <b>Agathe Musterfrau</b>
   */
  @Test
  public void deleteZitterbacke() {
    try {
      CONTEXT.deleteUser(Identity.ZITTERBACKE.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteCambrault
  /**
   ** Test delete request leveraging server context.
   ** <p>
   ** <b>Agathe Musterfrau</b>
   */
  @Test
  public void deleteCambrault() {
    try {
      CONTEXT.deleteUser(Identity.CAMBRAULT.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}