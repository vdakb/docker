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

package oracle.iam.junit.jes.connector.systemrole;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.jes.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case create operation on system roles at the target system
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
      CONTEXT.createSystemRole("Unknown");
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSystemAdministrator
  /**
   ** Test that a particular role could be created for its unique identifier.
   */
  @Test
  public void createSystemAdministrator() {
    try {
      assertEquals(CONTEXT.createSystemRole(RoleManagerConstants.SYS_ADMIN_ROLE_NAME), SystemRole.OLDADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOperator
  /**
   ** Test that a particular role could be created for its unique identifier.
   */
  @Test
  public void createOperator() {
    try {
      assertEquals(CONTEXT.createSystemRole(SystemRole.OPERATORS.tag), SystemRole.OPERATORS.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAllUser
  /**
   ** Test that a particular role could be created for its unique identifier.
   */
  @Test
  public void createAllUser() {
    try {
      assertEquals(CONTEXT.createSystemRole(SystemRole.ALLUSERS.tag), SystemRole.ALLUSERS.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSelfOperator
  /**
   ** Test that a particular role could be created for its unique identifier.
   */
  @Test
  public void createSelfOperator() {
    try {
      assertEquals(CONTEXT.createSystemRole(SystemRole.SELFOP.tag), SystemRole.SELFOP.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAdministrator
  /**
   ** Test that a particular role could be created for its unique identifier.
   */
  @Test
  public void createAdministrator() {
    try {
      assertEquals(CONTEXT.createSystemRole(SystemRole.NEWADMIN.tag), SystemRole.NEWADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBIReportAdministrator
  /**
   ** Test that a particular role could be created for its unique identifier.
   */
  @Test
  public void createBIReportAdministrator() {
    try {
      assertEquals(CONTEXT.createSystemRole(SystemRole.BIRADMIN.tag), SystemRole.BIRADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMNRTest1
  /**
   ** Test that a particular role could be created for its unique identifier.
   */
  @Test
  public void createMNRTest1() {
    try {
      assertEquals(CONTEXT.createSystemRole(SystemRole.MNRTEST1.tag), SystemRole.MNRTEST1.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMNRTest2
  /**
   ** Test that a particular role could be created for its unique identifier.
   */
  @Test
  public void createMNRTest2() {
    try {
      assertEquals(CONTEXT.createSystemRole(SystemRole.MNRTEST2.tag), SystemRole.MNRTEST2.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}