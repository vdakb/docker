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
    Subsystem   :   Identity Governance Service Provisioning

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.junit.jes.integration.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case for resolving accounts by its username at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
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
  // Method:   resolveUnknown
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveUnknown() {
    try {
      FACADE.resolveUsername(ObjectClass.ACCOUNT, Account.UNKNOWN.tag, null);
    }
    catch (ConnectorException e) {
      // swallow the expected exception but fail in any other case
      if (!e.getLocalizedMessage().startsWith("JES-00022"))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveSystemAdministrator
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveSystemAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.ACCOUNT, Account.UNKNOWN.tag, null), Account.XELSYSADM.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveSystemOperator
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveSystemOperator() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.ACCOUNT, Account.XELOPERATOR.tag, null), Account.XELOPERATOR.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServerAdministrator
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServerAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.ACCOUNT, Account.WEBLOGIC.tag, null), Account.WEBLOGIC.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveInternalAdministrator
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveInternalAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.ACCOUNT, Account.OIMINTERNAL.tag, null), Account.OIMINTERNAL.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveRegularUser
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveRegularUser() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.ACCOUNT, Account.CAMBRAULT.tag, null).getUidValue(), Account.CAMBRAULT.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }
}